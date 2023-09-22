package pro.sky.reserve.entity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class DogReport extends Report{

    public DogReport(Long id, LocalDate now, byte[] data, String text) {
        super();
    }

}
