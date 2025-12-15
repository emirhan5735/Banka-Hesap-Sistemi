package Backend;

import java.util.ArrayList;

public class Banka {
    // Veritabanı niyetine statik bir liste
    public static ArrayList<Musteri> musteriler = new ArrayList<>();

    // Müşteri Ekleme
    public static void musteriEkle(String ad, String soyad, String tc) {
        musteriler.add(new Musteri(ad, soyad, tc));
    }

    // Transfer İşlemi
    // Gönderen ve Alıcı hesap nesnelerini bulup işlemi yapar.
    public static boolean paraTransferi(Hesap gonderen, Hesap alici, double miktar) {
        // 1. Gönderen hesabın bakiyesi yeterli mi? (paraCek metodu false dönerse yetersizdir)
        if(gonderen.paraCek(miktar)) {
            // 2. Para çekildiyse alıcıya yatır
            alici.paraYatir(miktar);
            System.out.println("Transfer Başarılı: " + miktar + " TL gönderildi.");
            return true;
        } else {
            System.out.println("Transfer Başarısız: Yetersiz Bakiye.");
            return false;
        }
    }
}