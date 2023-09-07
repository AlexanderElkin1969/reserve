package pro.sky.reserve.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.reserve.service.ReserveTelegramBotService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static pro.sky.reserve.constants.BotConstants.*;

@Service
public class ReserveTelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(ReserveTelegramBotUpdatesListener.class);

    private static final Long[] shelterVolunteersGroupChatId = new Long[2];

    /**     matrix  -  "МАТРИЦА" показывающая историю или состояние запроса
     *      ключем является ChatId пользователя, если этого ключа нет, то пользователь обратился впервые
     *      значение 0 по ключу означает, что пользователь находится в меню выбора НОМЕРА_ПРИЮТА
     *      значение 1(или 2) - пользователь выбрал приют кошек (или приют собак) и находится в меню выбора НОМЕРА_МЕНЮ
     *      значение Х1(или Х2) - пользователь выбрал Х НОМЕР_МЕНЮ (в нашем случае 1, 2 или 3) и находится в меню выбора НОМЕРА_ДЕЙСТВИЯ
     *      при выполнении выбранного ДЕЙСТВИЯ значение по ключу обнуляется
     */
    private static final Map<Long, Integer> matrix = new HashMap<Long, Integer>();

    private final TelegramBot telegramBot;

    private final ReserveTelegramBotService telegramBotService;


    public ReserveTelegramBotUpdatesListener(TelegramBot telegramBot, ReserveTelegramBotService telegramBotService) {
        this.telegramBot = telegramBot;
        this.telegramBotService = telegramBotService;
    }

    @Value("${cat.shelter.volunteers.group.chat}")
    Long catShelterVolunteersGroupChatId ;

    @Value("${dog.shelter.volunteers.group.chat}")
    Long dogShelterVolunteersGroupChatId ;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
        shelterVolunteersGroupChatId[0] = catShelterVolunteersGroupChatId;
        shelterVolunteersGroupChatId[1] = dogShelterVolunteersGroupChatId;
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);
                Long id = update.message().chat().id();
                if (matrix.containsKey(id)){
                    if (update.message() != null) {
                        String text = update.message().text();
                        if (Objects.nonNull(text)) {
                            char choice = text.charAt(0);
                            int value = matrix.get(id);
                            if (value == 0) {
//  выбор номера приюта
                                switch (choice) {
                                    case '1':
                                        matrix.put(id, 1);
                                        telegramBotService.sendMessage(id, TEXT_MENU, ParseMode.Markdown);
                                        break;
                                    case '2':
                                        matrix.put(id, 2);
                                        telegramBotService.sendMessage(id, TEXT_MENU, ParseMode.Markdown);
                                        break;
                                    default:
                                        telegramBotService.sendMessage(id, TEXT_GREETINGS_SHORT, ParseMode.Markdown);
                                }
                            }else if (value > 0 && value <10){
//  выбор номера меню
                                switch (choice) {
                                    case '0':
                                        matrix.put(id, 0);
                                        callVolunteer( id, value, " просит связаться с ним.");
                                        break;
                                    case '1':
                                        matrix.put(id, 10 + value);
                                        telegramBotService.sendMessage(id, TEXT_ACTION_1, ParseMode.Markdown);
                                        break;
                                    case '2':
                                        matrix.put(id, 20 + value);
                                        telegramBotService.sendMessage(id, TEXT_ACTION_2, ParseMode.Markdown);
                                        break;
                                    case '3':
                                        matrix.put(id, 30 + value);
                                        telegramBotService.sendMessage(id, TEXT_ACTION_3, ParseMode.Markdown);
                                        break;
                                    case '#':
                                        matrix.put(id, 0);
                                        telegramBotService.sendMessage(id, TEXT_GREETINGS_SHORT, ParseMode.Markdown);
                                        break;
                                    default:
                                        telegramBotService.sendMessage(id, TEXT_MENU, ParseMode.Markdown);
                                }
                            }else if (value > 10 && value <100){
//  выбор номера действия
                                int menuNumber = value/10;
                                int shelterNumber = value - 10*menuNumber;
                                if (choice == '0'){
                                    matrix.put(id, 0);
                                    callVolunteer( id, shelterNumber, " просит связаться с ним.");
                                }else if (choice == '@'){
                                    matrix.put(id, 0);
                                    callVolunteer( id, shelterNumber, " передал: " + text.substring(1));
                                }else if (choice == '#'){
                                    matrix.put(id, value%10);
                                    telegramBotService.sendMessage(id, TEXT_MENU, ParseMode.Markdown);
                                }else {
                                    int actionNumber = Integer.parseInt(String.valueOf(choice));
                                    if (actionNumber == 0){
                                        returnToBeginning(id);
                                    }else {
                                        if (menuNumber == 1){
                                            if(actionNumber > 5){
                                                returnToBeginning(id);
                                            }else {
                                                if (shelterNumber == 1) {
                                                    telegramBotService.sendMessage(id, CAT_SHELTER_INFORMATION_LIST.get(actionNumber - 1), ParseMode.Markdown);
                                                } else {
                                                    telegramBotService.sendMessage(id, DOG_SHELTER_INFORMATION_LIST.get(actionNumber - 1), ParseMode.Markdown);
                                                }
                                            }
                                        }
                                        if (menuNumber == 2){
                                            telegramBotService.sendMessage(id, RECOMMENDATIONS_LIST.get(actionNumber-1), ParseMode.Markdown);
                                        }
                                        if (menuNumber == 3){
                                            if (actionNumber == 1){
                                                telegramBotService.sendMessage(id, REPORT_FORM, ParseMode.Markdown);
                                            }else if (actionNumber == 2) {

//   если выбрана 2, то вызываем метод приема отчета

                                            }else {
                                                returnToBeginning(id);
                                            }
                                        }
                                        matrix.put(id, 0);
                                    }
                                }
                            }

                        }
                    }
                }else {
                    if ((!catShelterVolunteersGroupChatId.equals(id)) && (!dogShelterVolunteersGroupChatId.equals(id))) {
                        telegramBotService.sendMessage(id, TEXT_GREETINGS_LONG, ParseMode.Markdown);
                        matrix.put(id, 0);
                    }
                }
            });
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void callVolunteer(Long id, int shelterNumber, String message){
        matrix.put(id, 0);
        telegramBotService.sendMessage(id, "Ваше сообщение передано волонтеру " + shelterNumber +
                " приюта.", ParseMode.Markdown);
        telegramBotService.sendMessage(shelterVolunteersGroupChatId[shelterNumber - 1],
                "Пользователь с Id: " + id + message, ParseMode.Markdown);
    }

    private void returnToBeginning(Long id){
        matrix.put(id, 0);
        telegramBotService.sendMessage(id, TEXT_GREETINGS_SHORT, ParseMode.Markdown);
    }

}
