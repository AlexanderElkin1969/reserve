package pro.sky.reserve.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class DogReport{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    private Long adoption;

    private LocalDate date;

    @Lob
    private byte[] photo;

    String text;

    public DogReport(Long adoption, LocalDate date, byte[] photo, String text) {
        this.adoption = adoption;
        this.date = date;
        this.photo = photo;
        this.text = text;
    }

    public DogReport() {
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
        DogReport dogReport = (DogReport) o;
        return getId() == dogReport.getId() && Objects.equals(getAdoption(), dogReport.getAdoption());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAdoption());
    }

    @Override
    public String toString() {
        return "DogReport{" +
                "id=" + id +
                ", adoptionId=" + adoption +
                ", reportDate=" + date +
                ", text='" + text + '\'' +
                '}';
    }

}
