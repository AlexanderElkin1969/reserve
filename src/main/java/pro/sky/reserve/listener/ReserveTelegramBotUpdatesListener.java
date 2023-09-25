package pro.sky.reserve.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.sky.reserve.entity.CatReport;
import pro.sky.reserve.entity.DogReport;
import pro.sky.reserve.service.CatReportService;
import pro.sky.reserve.service.DogReportService;
import pro.sky.reserve.service.ReserveTelegramBotService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static pro.sky.reserve.constants.BotConstants.*;

@Service
public class ReserveTelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(ReserveTelegramBotUpdatesListener.class);

    private static final Long[] shelterVolunteersGroupChatId = new Long[2];

    /**
     *      <u>matrix</u>  -  "МАТРИЦА" показывающая историю или состояние запроса
     *      ключем является ChatId пользователя, если этого ключа нет, то пользователь обратился впервые
     *      значение 0 по ключу означает, что пользователь находится в меню выбора НОМЕРА_ПРИЮТА
     *      значение 1(или 2) - пользователь выбрал приют кошек (или приют собак) и находится в меню выбора НОМЕРА_МЕНЮ
     *      значение Х1(или Х2) - пользователь выбрал Х НОМЕР_МЕНЮ (в нашем случае 1, 2 или 3) и находится в меню выбора НОМЕРА_ДЕЙСТВИЯ
     *      при выполнении выбранного ДЕЙСТВИЯ значение по ключу обнуляется
     */
    private static final Map<Long, Integer> matrix = new HashMap<Long, Integer>();

    private final TelegramBot telegramBot;

    private final ReserveTelegramBotService telegramBotService;

    private final CatReportService catReportService;

    private final DogReportService dogReportService;


    public ReserveTelegramBotUpdatesListener(TelegramBot telegramBot,
                                             ReserveTelegramBotService telegramBotService,
                                             CatReportService catReportService,
                                             DogReportService dogReportService) {
        this.telegramBot = telegramBot;
        this.telegramBotService = telegramBotService;
        this.catReportService = catReportService;
        this.dogReportService = dogReportService;
    }

    @Value("${cat.shelter.volunteers.group.chat}")
    private Long catShelterVolunteersGroupChatId ;

    @Value("${dog.shelter.volunteers.group.chat}")
    private Long dogShelterVolunteersGroupChatId ;

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
                if (update.message() != null) {
                    Long id = update.message().chat().id();
                    if (matrix.containsKey(id)) {
                        int value = matrix.get(id);
                        String text = update.message().text();
                        if (Objects.nonNull(text)) {
                            char choice = text.charAt(0);
                            if (value == 0) {
                                choosingShelter(id, choice);                    //  выбор номера приюта
                            } else if (value > 0 && value < 10) {
                                choosingMenu(id, value, choice);                //  выбор номера меню
                            } else if (value > 10 && value < 100) {
                                                                                //  выбор номера действия
                                int menuNumber = value / 10;
                                int shelterNumber = value - 10 * menuNumber;
                                if (choice == '0') {
                                    callVolunteer(id, shelterNumber, " просит связаться с ним.");
                                } else if (choice == '@') {
                                    callVolunteer(id, shelterNumber, " передал: " + text.substring(1));
                                } else if (choice == '#') {
                                    matrix.put(id, value % 10);
                                    telegramBotService.sendMessage(id, TEXT_MENU, ParseMode.Markdown);
                                } else {
                                    int actionNumber = Integer.parseInt(String.valueOf(choice));
                                    if (actionNumber == 0) {
                                        returnToBeginning(id);
                                    } else {
                                        if (menuNumber == 1) {
                                            giveInformation(id, shelterNumber, actionNumber);
                                        }
                                        if (menuNumber == 2) {
                                            makeRecommendation(id, shelterNumber, actionNumber);
                                        }
                                        if (menuNumber == 3) {
                                            helpWithReport(id, value, actionNumber);
                                        }
                                    }
                                }
                            } else {
                                acceptTextReport(id, text);
                            }
                        }else if (value > 200){
                            acceptPhotoReport(id, update);
                        }
                    } else {
                        if ((!catShelterVolunteersGroupChatId.equals(id)) && (!dogShelterVolunteersGroupChatId.equals(id))) {
                            telegramBotService.sendMessage(id, TEXT_GREETINGS_LONG, ParseMode.Markdown);
                            matrix.put(id, 0);
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void choosingShelter(Long id, char choice){
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
    }

    private void choosingMenu(Long id, int value, char choice){
        switch (choice) {
            case '0':
                callVolunteer(id, value, " просит связаться с ним.");
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
    }

    private void giveInformation(Long id, int shelterNumber, int actionNumber){
        if (actionNumber > 5) {
            returnToBeginning(id);
        } else {
            if (shelterNumber == 1) {
                telegramBotService.sendMessage(id, CAT_SHELTER_INFORMATION_LIST.get(actionNumber - 1), ParseMode.Markdown);
            } else {
                telegramBotService.sendMessage(id, DOG_SHELTER_INFORMATION_LIST.get(actionNumber - 1), ParseMode.Markdown);
            }
        }
        matrix.put(id, 0);
    }

    private void makeRecommendation(Long id, int shelterNumber, int actionNumber){
        if (shelterNumber == 1 && actionNumber > 6){
            telegramBotService.sendMessage(id, "Данные рекомендации не для кошек.", ParseMode.Markdown);
        }else {
            telegramBotService.sendMessage(id, RECOMMENDATIONS_LIST.get(actionNumber - 1), ParseMode.Markdown);
        }
        matrix.put(id, 0);
    }

    private void helpWithReport(Long id, int value, int actionNumber){
        if (actionNumber == 1) {
            matrix.put(id, 0);
            telegramBotService.sendMessage(id, REPORT_FORM, ParseMode.Markdown);
        } else if (actionNumber == 2) {
            matrix.put(id, 200 + value);
            telegramBotService.sendMessage(id, "Пришлите текст отчета.", ParseMode.Markdown);
        } else {
            returnToBeginning(id);
        }
    }

    private void acceptPhotoReport(Long id, Update update){
        String text = update.message().text();
        PhotoSize[] photoSizes = update.message().photo();
        if (photoSizes == null){
            telegramBotService.sendMessage(id, "Пришлите фото питомца.", ParseMode.Markdown);
        }else {
            PhotoSize photoSize = photoSizes[photoSizes.length - 1];
            GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));
            if (getFileResponse.isOk()){
                try {
                    String extension = StringUtils.getFilenameExtension(getFileResponse.file().filePath());
                    byte[] photo = telegramBot.getFileContent(getFileResponse.file());
                    if (matrix.get(id)%2 == 0){
                        Optional<DogReport> report = dogReportService.findByAdoptionIdAndReportDate(id, LocalDate.now());
                        if (report.isPresent()) {
                            DogReport dogReport = report.get();
                            text = dogReport.getText();
                            dogReport.setPhoto(photo);
                            DogReport updateReport = dogReportService.updateDogReport(dogReport);
                        }else {
                            dogReportService.createDogReport(new DogReport(id, LocalDate.now(), photo, text));
                        }
                    }else {
                        Optional<CatReport> report = catReportService.findByAdoptionIdAndReportDate(id, LocalDate.now());
                        if (report.isPresent()) {
                            CatReport catReport = report.get();
                            text = catReport.getText();
                            catReport.setPhoto(photo);
                            CatReport updateReport = catReportService.updateCatReport(catReport);
                        }else {
                            catReportService.createCatReport(new CatReport(id, LocalDate.now(), photo, text));
                        }
                    }
                    if (Objects.nonNull(photo)&&Objects.nonNull(text)){
                        telegramBotService.sendMessage(id, "Ваш отчет успешно принят. Хорошего дня.", ParseMode.Markdown);
                        matrix.put(id, 0);
                    }else {
                        telegramBotService.sendMessage(id, "Пришлите текст.", ParseMode.Markdown);
                    }
                }catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void acceptTextReport(Long id, String text){
        if (matrix.get(id) % 2 == 0) {
            Optional<DogReport> report = dogReportService.findByAdoptionIdAndReportDate(id, LocalDate.now());
            if (report.isPresent()) {
                DogReport dogReport = report.get();
                dogReport.setText(text);
                DogReport updateReport = dogReportService.updateDogReport(dogReport);
                telegramBotService.sendMessage(id, "Ваш отчет успешно принят. Хорошего дня.", ParseMode.Markdown);
                matrix.put(id, 0);
            } else {
                DogReport updateReport =  dogReportService.createDogReport(new DogReport(id, LocalDate.now(), null, text));
                telegramBotService.sendMessage(id, "Пришлите фото питомца.", ParseMode.Markdown);
            }
        } else {
            Optional<CatReport> report = catReportService.findByAdoptionIdAndReportDate(id, LocalDate.now());
            if (report.isPresent()) {
                CatReport catReport = report.get();
                catReport.setText(text);
                CatReport updateReport = catReportService.updateCatReport(catReport);
                telegramBotService.sendMessage(id, "Ваш отчет успешно принят. Хорошего дня.", ParseMode.Markdown);
                matrix.put(id, 0);
            } else {
                CatReport updateReport = catReportService.createCatReport(new CatReport(id, LocalDate.now(), null, text));
                telegramBotService.sendMessage(id, "Пришлите фото питомца.", ParseMode.Markdown);
            }
        }
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
