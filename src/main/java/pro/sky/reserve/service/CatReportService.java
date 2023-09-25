package pro.sky.reserve.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.reserve.entity.CatReport;
import pro.sky.reserve.repository.CatReportRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CatReportService {

    private final CatReportRepository catReportRepository;

    public CatReportService(CatReportRepository catReportRepository) {
        this.catReportRepository = catReportRepository;
    }

    @Transactional
    public CatReport createCatReport(CatReport catReport) {
        catReportRepository.save(catReport);
        return catReport;
    }

    public CatReport getCatReport(Integer id) {
        return catReportRepository.findById(id).get();
    }

    @Transactional
    public CatReport updateCatReport(CatReport catReport) {
        catReportRepository.save(catReport);
        return catReport;
    }

    public CatReport deleteCatReport(Integer id) {
        CatReport catReport = catReportRepository.findById(id).get();
        catReportRepository.deleteById(id);
        return catReport;
    }

    public Optional<CatReport> findByAdoptionIdAndReportDate(Long adoptionId, LocalDate reportDate){
        return catReportRepository.findByAdoptionIdAndReportDate(adoptionId, reportDate);
    }

    public List<CatReport> readAllByDate(LocalDate localDate){
        return List.copyOf(catReportRepository.findAllByReportDate(localDate));
    }

    public List<CatReport> readAllByAdoptionId(Long adoptionId){
        return List.copyOf(catReportRepository.findAllByAdoptionId(adoptionId));
    }

}
