package pro.sky.reserve.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.reserve.entity.CatReport;
import pro.sky.reserve.entity.DogReport;
import pro.sky.reserve.repository.DogReportRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DogReportService {

    private final DogReportRepository dogReportRepository;

    public DogReportService(DogReportRepository dogReportRepository) {
        this.dogReportRepository = dogReportRepository;
    }

    @Transactional
    public void createDogReport(DogReport dogReport) {
        dogReportRepository.save(dogReport);
    }

    public DogReport getDogReportById(Integer id) {
        return dogReportRepository.findById(id).get();
    }

    @Transactional
    public void updateDogReport(DogReport dogReport) {
        dogReportRepository.save(dogReport);
    }

    public DogReport deleteDogReport(Integer id) {
        DogReport dogReport = dogReportRepository.findById(id).get();
        dogReportRepository.deleteById(id);
        return dogReport;
    }

    public Optional<DogReport> findByAdoptionAndDate(Long adoptionId, LocalDate reportDate){
        return dogReportRepository.findByAdoptionAndDate(adoptionId, reportDate);
    }

    public List<DogReport> readByDate(LocalDate localDate){
        return List.copyOf(dogReportRepository.findAllByDate(localDate));
    }

    public List<DogReport> readAllById(Integer id){
        return List.copyOf(dogReportRepository.findAllById(id));
    }

}
