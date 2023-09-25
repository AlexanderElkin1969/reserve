package pro.sky.reserve.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.reserve.entity.CatReport;
import pro.sky.reserve.service.CatReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cat_report")
public class CatReportController {

    private final CatReportService catReportService;

    public CatReportController(CatReportService catReportService) {
        this.catReportService = catReportService;
    }

    @PostMapping
    public ResponseEntity<CatReport> createCatReport(@RequestBody CatReport catReport) {
        return ResponseEntity.ok(catReportService.createCatReport(catReport));
    }

    @GetMapping("{reportId}")
    public ResponseEntity<CatReport> getCatReport(@PathVariable Integer reportId) {
        return ResponseEntity.ok(catReportService.getCatReport(reportId));
    }

    @PutMapping
    public ResponseEntity<CatReport> updateCatReport(@RequestBody CatReport catReport) {
        return ResponseEntity.ok(catReportService.updateCatReport(catReport));
    }

    @DeleteMapping("{reportId}")
    public ResponseEntity<CatReport> deleteCatReport(@PathVariable Integer reportId) {
        return ResponseEntity.ok(catReportService.deleteCatReport(reportId));
    }

    @GetMapping("/adoptionId/{adoptionId)}")
    public ResponseEntity<List<CatReport>> getAllCatReports(Long adoptionId) {
        return ResponseEntity.ok(catReportService.readAllByAdoptionId(adoptionId));
    }
    @GetMapping("/date")
    public  ResponseEntity<List<CatReport>> getReportByDate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return ResponseEntity.ok(catReportService.readAllByDate(date));
    }





}
