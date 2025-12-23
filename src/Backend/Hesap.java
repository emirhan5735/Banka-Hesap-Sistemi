package Backend;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Hesap implements Serializable {

    private static final long serialVersionUID = 1L;

    private int hesapNo;
    private double bakiye;
    private String hesapTuru;
    private double toplamKrediBorcu = 0;
    private double aylikAsgariOdeme = 0;
    private boolean buAyAsgariOdendiMi = false;


    private ArrayList<Islem> islemler;

    public Hesap(int hesapNo, double baslangicBakiyesi, String hesapTuru) {
        this.hesapNo = hesapNo;
        this.bakiye = baslangicBakiyesi;
        this.hesapTuru = hesapTuru;
        this.islemler = new ArrayList<>();


        islemEkle("Hesap Açılışı", baslangicBakiyesi);
    }


    public void islemEkle(String aciklama, double tutar) {
        Islem yeniIslem = new Islem(aciklama, tutar);
        islemler.add(yeniIslem);
    }

    public ArrayList<Islem> getIslemler() {
        return islemler;
    }


    public void paraYatir(double miktar, String aciklama) {
        if (miktar > 0) {
            this.bakiye += miktar;
            islemEkle(aciklama, miktar);
        }
    }


    public void paraYatir(double miktar) {
        paraYatir(miktar, "Para Yatırma");
    }


    public abstract boolean paraCek(double miktar, String aciklama);


    public boolean paraCek(double miktar) {
        return paraCek(miktar, "Para Çekme");
    }

    protected void bakiyeAzalt(double miktar) {
        this.bakiye -= miktar;
    }


    public double getBakiye() {
        return bakiye;
    }


    public int getHesapNo() {
        return hesapNo;
    }


    public String getHesapTuru() {
        return hesapTuru;
    }


    @Override
    public String toString() {
        return "No: " + hesapNo + " | " + hesapTuru + " | " + bakiye + " TL";
    }


    public void krediCek(double miktar) {
        this.bakiye += miktar;

        double faizliBorc = miktar * 1.20;

        this.toplamKrediBorcu += faizliBorc;

        this.aylikAsgariOdeme = this.toplamKrediBorcu / 10;

        this.buAyAsgariOdendiMi = false;

        System.out.println("Kredi çekildi. Yeni Bakiye: " + this.bakiye);
        System.out.println("Toplam Borç: " + this.toplamKrediBorcu);
    }


    public boolean borcOde(double odenecekTutar) {
        if (this.bakiye >= odenecekTutar) {
            this.bakiye -= odenecekTutar;

            this.toplamKrediBorcu -= odenecekTutar;

            if (this.toplamKrediBorcu <= 0) {
                this.toplamKrediBorcu = 0;
                this.aylikAsgariOdeme = 0;
                this.buAyAsgariOdendiMi = true;
            }
            else {
                if (odenecekTutar >= this.aylikAsgariOdeme) {
                    this.buAyAsgariOdendiMi = true;
                }
            }
            System.out.println("Borç ödendi. Kalan Borç: " + this.toplamKrediBorcu);
        }
        else {
            System.out.println("Yetersiz bakiye, borç ödenemedi.");
        }
        return false;
    }
}