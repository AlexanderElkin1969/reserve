package pro.sky.reserve.entity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class CatReport extends Report{

    public CatReport(Long id, LocalDate now, byte[] data, String text) {
        super();
    }

}
