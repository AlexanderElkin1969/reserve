package pro.sky.reserve.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.reserve.entity.DogReport;
import pro.sky.reserve.service.DogReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dog_report")
public class DogReportController {

    private final DogReportService dogReportService;

    public DogReportController(DogReportService dogReportService) {
        this.dogReportService = dogReportService;
    }

    @PostMapping
    public ResponseEntity<DogReport> createDogReport(@RequestBody DogReport dogReport) {
        return ResponseEntity.ok(dogReportService.createDogReport(dogReport));
    }

    @GetMapping("{reportId}")
    public ResponseEntity<DogReport> getDogReport(@PathVariable Integer reportId) {
        return ResponseEntity.ok(dogReportService.getDogReport(reportId));
    }

    @PutMapping
    public ResponseEntity<DogReport> updateDogReport(@RequestBody DogReport dogReport) {
        return ResponseEntity.ok(dogReportService.updateDogReport(dogReport));
    }

    @DeleteMapping("{reportId}")
    public ResponseEntity<DogReport> deleteDogReport(@PathVariable Integer reportId) {
        return ResponseEntity.ok(dogReportService.deleteDogReport(reportId));
    }

    @GetMapping("/adoptionId/{adoptionId}")
    public ResponseEntity<List<DogReport>> getAllDogReports(Long adoptionId) {
        return ResponseEntity.ok(dogReportService.readAllByAdoptionId(adoptionId));
    }
    @GetMapping("/date")
    public  ResponseEntity<List<DogReport>> getReportByDate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return ResponseEntity.ok(dogReportService.readAllByDate(date));
    }
}
