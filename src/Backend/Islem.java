package Backend;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Islem implements Serializable {

    private String aciklama;
    private double tutar;
    private LocalDateTime tarih;

    public Islem(String aciklama, double tutar) {
        this.aciklama = aciklama;
        this.tutar = tutar;
        this.tarih = LocalDateTime.now();
    }


    public String getTarihString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return tarih.format(format);
    }


    public String getAciklama() {
        return aciklama;
    }


    public double getTutar() {
        return tutar;
    }
}