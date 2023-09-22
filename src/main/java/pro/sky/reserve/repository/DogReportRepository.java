package pro.sky.reserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.reserve.entity.DogReport;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DogReportRepository extends JpaRepository<DogReport, Integer> {

    List<DogReport> findAllById(Integer id);

    List<DogReport> findAllByReportDate(LocalDate localDate);

}
