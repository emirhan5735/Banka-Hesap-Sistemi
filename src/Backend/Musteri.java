package Backend;

import java.io.Serializable;
import java.util.ArrayList;

public class Musteri implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ad;
    private String soyad;
    private String tcKimlik;
    private String sifre;
    private ArrayList<Hesap> hesaplar;

    public Musteri(String ad, String soyad, String tcKimlik, String sifre) {
        this.ad = ad;
        this.soyad = soyad;
        this.tcKimlik = tcKimlik;
        this.sifre = sifre;
        this.hesaplar = new ArrayList<>();
    }

    public boolean sifreKontrol(String girilenSifre) {
        return this.sifre.equals(girilenSifre);
    }

    public void hesapEkle(Hesap hesap) {
        hesaplar.add(hesap);
    }

    public ArrayList<Hesap> getHesaplar() {
        return hesaplar;
    }

    public String getAdSoyad() {
        return ad + " " + soyad;
    }

    public String getTcKimlik() {
        return tcKimlik;
    }
}