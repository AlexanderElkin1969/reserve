package pro.sky.reserve.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class CatReport{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    private Long adoption;

    private LocalDate date;

    @Lob
    private byte[] photo;

    String text;

    public CatReport(Long adoption, LocalDate date, byte[] photo, String text) {
        this.adoption = adoption;
        this.date = date;
        this.photo = photo;
        this.text = text;
    }

    public CatReport() {
    }

    public int getId() {
        return id;
    }

    public Long getAdoption() {
        return adoption;
    }

    public LocalDate getDate() {
        return date;
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

    public void setAdoption(Long adoption) {
        this.adoption = adoption;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
        return getId() == catReport.getId() && Objects.equals(getAdoption(), catReport.getAdoption());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAdoption());
    }

    @Override
    public String toString() {
        return "CatReport{" +
                "id=" + id +
                ", adoption=" + adoption +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }

}
