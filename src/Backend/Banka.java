package Backend;

import java.util.ArrayList;

public class Banka {

    public static ArrayList<Musteri> musteriler = new ArrayList<>();

    public static void musteriEkle(String ad, String soyad, String tc, String sifre) {
        musteriler.add(new Musteri(ad, soyad, tc, sifre));
        VeriTabani.kaydet();
    }


    public static Musteri girisYap(String tc, String sifre) {
        for (Musteri m : musteriler) {
            if (m.getTcKimlik().equals(tc) && m.sifreKontrol(sifre)) {
                return m;
            }
        }
        return null;
    }


    public static boolean paraTransferi(Hesap gonderen, Hesap alici, double miktar) {
        if (gonderen.paraCek(miktar, "Transfer -> " + alici.getHesapNo())) {

            alici.paraYatir(miktar, "Transfer <- " + gonderen.getHesapNo());

            VeriTabani.kaydet();

            return true;
        }
        return false;
    }


    public static String birAyIleriSar() {
        StringBuilder rapor = new StringBuilder();
        rapor.append("Zaman ileri sarıldı...\n");

        for (Musteri m : musteriler) {
            for (Hesap h : m.getHesaplar()) {
                if (h instanceof VadeliHesap) {
                    VadeliHesap vh = (VadeliHesap) h;
                    if (vh.getBakiye() > 0) {
                        double faizKazanci = (vh.getBakiye() * vh.getFaizOrani()) / 1200.0;
                        vh.paraYatir(faizKazanci, "Aylık Faiz Getirisi");
                    }
                }
                else if (h instanceof VadesizHesap) {
                    VadesizHesap vh = (VadesizHesap) h;
                    String sonuc = vh.aySonuIslemi();
                    if (!sonuc.isEmpty()) {
                        rapor.append(sonuc).append("\n");
                    }
                }
            }
        }
        VeriTabani.kaydet();
        return rapor.toString();
    }
}