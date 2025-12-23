package Backend;

import java.io.*;
import java.util.ArrayList;

public class VeriTabani {

    private static final String DOSYA_ADI = "banka_verileri.dat";

    public static void kaydet() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DOSYA_ADI))) {
            oos.writeObject(Banka.musteriler);
            System.out.println("Veriler kaydedildi.");
        }
        catch (IOException e) {
            System.err.println("Kayıt sırasında hata oluştu: " + e.getMessage());
        }
    }


    public static void yukle() {
        File dosya = new File(DOSYA_ADI);
        if (dosya.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DOSYA_ADI))) {
                Banka.musteriler = (ArrayList<Musteri>) ois.readObject();
                System.out.println("Veritabanı yüklendi: " + Banka.musteriler.size() + " müşteri var.");
            }
            catch (IOException | ClassNotFoundException e) {
                System.err.println("Veri yükleme hatası: " + e.getMessage());
                Banka.musteriler = new ArrayList<>();
            }
        }
        else {
            Banka.musteriler = new ArrayList<>();
            System.out.println("Kayıtlı veri bulunamadı, yeni veritabanı oluşturuluyor.");
        }
    }
}