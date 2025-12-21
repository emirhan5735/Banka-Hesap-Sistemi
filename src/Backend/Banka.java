package Backend;

import java.util.ArrayList;

public class Banka {

    public static ArrayList<Musteri> musteriler = new ArrayList<>();


    public static void musteriEkle(String ad, String soyad, String tc) {
        musteriler.add(new Musteri(ad, soyad, tc));
    }



    public static boolean paraTransferi(Hesap gonderen, Hesap alici, double miktar) {
        if(gonderen.paraCek(miktar)) {
            alici.paraYatir(miktar);
            System.out.println("Transfer Başarılı: " + miktar + " TL gönderildi.");
            return true;
        } else {
            System.out.println("Transfer Başarısız: Yetersiz Bakiye.");
            return false;
        }
    }
}