package pro.sky.reserve.service;

import org.springframework.stereotype.Service;
import pro.sky.reserve.entity.DogReport;
import pro.sky.reserve.repository.DogReportRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DogReportService {

    private final DogReportRepository dogReportRepository;

    public DogReportService(DogReportRepository dogReportRepository) {
        this.dogReportRepository = dogReportRepository;
    }

    public DogReport createDogReport(DogReport catReport) {
        return dogReportRepository.save(catReport);
    }

    public DogReport getDogReportById(Integer id) {
        return dogReportRepository.findById(id).get();
    }

    public DogReport updateDogReport(DogReport catReport) {
        return dogReportRepository.save(catReport);
    }

    public DogReport deleteDogReport(Integer id) {
        DogReport dogReport = dogReportRepository.findById(id).get();
        dogReportRepository.deleteById(id);
        return dogReport;
    }

    public List<DogReport> readByDate(LocalDate localDate){
        return List.copyOf(dogReportRepository.findAllByReportDate(localDate));
    }

    public List<DogReport> readAllById(Integer id){
        return List.copyOf(dogReportRepository.findAllById(id));
    }

}
