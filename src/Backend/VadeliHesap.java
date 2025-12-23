package Backend;

public class VadeliHesap extends Hesap {

    private double faizOrani;

    public VadeliHesap(int hesapNo, double baslangicBakiyesi, double faizOrani) {
        super(hesapNo, baslangicBakiyesi, "Vadeli");
        this.faizOrani = faizOrani;
    }

    @Override
    public boolean paraCek(double miktar, String aciklama) {
        if (getBakiye() >= miktar) {
            bakiyeAzalt(miktar);
            islemEkle(aciklama + " (Vade Bozuldu)", -miktar);
            return true;
        }
        return false;
    }

    public double getFaizOrani() { return faizOrani; }
}