package Backend;

import java.util.ArrayList;

public class Musteri {
    private String ad;
    private String soyad;
    private String tcKimlik;
    private ArrayList<Hesap> hesaplar;

    public Musteri(String ad, String soyad, String tcKimlik) {
        this.ad = ad;
        this.soyad = soyad;
        this.tcKimlik = tcKimlik;
        this.hesaplar = new ArrayList<>();
    }

    public void hesapEkle(Hesap hesap) {
        hesaplar.add(hesap);
    }

    public void hesapSil(int hesapNo) {
        hesaplar.removeIf(h -> h.getHesapNo() == hesapNo);
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

    @Override
    public String toString() {
        return ad + " " + soyad + " (TC: " + tcKimlik + ")";
    }
}