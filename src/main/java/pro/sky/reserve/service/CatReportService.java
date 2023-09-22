package pro.sky.reserve.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.reserve.entity.CatReport;
import pro.sky.reserve.repository.CatReportRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class CatReportService {

    private final CatReportRepository catReportRepository;

    public CatReportService(CatReportRepository catReportRepository) {
        this.catReportRepository = catReportRepository;
    }

    @Transactional
    public void createCatReport(CatReport catReport) {
        catReportRepository.save(catReport);
    }

    public CatReport getCatReportById(Integer id) {
        return catReportRepository.findById(id).get();
    }

    @Transactional
    public void updateCatReport(CatReport catReport) {
        catReportRepository.save(catReport);
    }

    public CatReport deleteCatReport(Integer id) {
        CatReport catReport = catReportRepository.findById(id).get();
        catReportRepository.deleteById(id);
        return catReport;
    }

    public List<CatReport> readByDate(LocalDate localDate){
        return List.copyOf(catReportRepository.findAllByReportDate(localDate));
    }

    public List<CatReport> readAllById(Integer id){
        return List.copyOf(catReportRepository.findAllById(id));
    }


}
