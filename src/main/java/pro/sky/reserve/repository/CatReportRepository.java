package pro.sky.reserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.reserve.entity.CatReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CatReportRepository extends JpaRepository<CatReport, Integer> {

    Optional<CatReport> findByAdoptionAndDate(Long adoption, LocalDate date);

    List<CatReport> findAllById(Integer id);

    List<CatReport> findAllByDate(LocalDate localDate);

}
