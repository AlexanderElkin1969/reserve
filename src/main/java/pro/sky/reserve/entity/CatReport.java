package pro.sky.reserve.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class CatReport{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    private Long adoptionId;

    private LocalDate reportDate;

    @Lob
    private byte[] photo;

    String text;

    public CatReport(Long adoptionId, LocalDate reportDate, byte[] photo, String text) {
        this.adoptionId = adoptionId;
        this.reportDate = reportDate;
        this.photo = photo;
        this.text = text;
    }

    public CatReport() {
    }

    public int getId() {
        return id;
    }

    public Long getAdoptionId() {
        return adoptionId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAdoptionId(Long adoptionId) {
        this.adoptionId = adoptionId;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatReport catReport = (CatReport) o;
        return getId() == catReport.getId() && Objects.equals(getAdoptionId(), catReport.getAdoptionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAdoptionId());
    }

    @Override
    public String toString() {
        return "CatReport{" +
                "id=" + id +
                ", adoptionId=" + adoptionId +
                ", reportDate=" + reportDate +
                ", text='" + text + '\'' +
                '}';
    }

}
