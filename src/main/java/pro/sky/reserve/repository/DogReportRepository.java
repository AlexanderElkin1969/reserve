package pro.sky.reserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.reserve.entity.DogReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DogReportRepository extends JpaRepository<DogReport, Integer> {

    Optional<DogReport> findByAdoptionAndDate(Long adoption, LocalDate date);

    List<DogReport> findAllById(Integer id);

    List<DogReport> findAllByDate(LocalDate localDate);

}
