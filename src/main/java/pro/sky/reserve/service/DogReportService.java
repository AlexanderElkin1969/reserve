package pro.sky.reserve.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public DogReport createDogReport(DogReport dogReport) {
        dogReportRepository.save(dogReport);
        return dogReport;
    }

    public DogReport getDogReport(Integer id) {
        return dogReportRepository.findById(id).get();
    }

    @Transactional
    public DogReport updateDogReport(DogReport dogReport) {
        dogReportRepository.save(dogReport);
        return dogReport;
    }

    public DogReport deleteDogReport(Integer id) {
        DogReport dogReport = dogReportRepository.findById(id).get();
        dogReportRepository.deleteById(id);
        return dogReport;
    }

    public Optional<DogReport> findByAdoptionIdAndReportDate(Long adoptionId, LocalDate reportDate){
        return dogReportRepository.findByAdoptionIdAndReportDate(adoptionId, reportDate);
    }

    public List<DogReport> readAllByDate(LocalDate localDate){
        return List.copyOf(dogReportRepository.findAllByReportDate(localDate));
    }

    public List<DogReport> readAllByAdoptionId(Long adoptionId){
        return List.copyOf(dogReportRepository.findAllByAdoptionId(adoptionId));
    }

}
