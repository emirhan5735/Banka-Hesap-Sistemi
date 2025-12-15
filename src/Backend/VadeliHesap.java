package Backend;

public class VadeliHesap extends Hesap {

    private double faizOrani;

    public VadeliHesap(int hesapNo, double baslangicBakiyesi, double faizOrani) {
        super(hesapNo, baslangicBakiyesi, "Vadeli");
        this.faizOrani = faizOrani;
    }


    @Override
    public boolean paraCek(double miktar) {
        if (getBakiye() >= miktar) {
            bakiyeAzalt(miktar);
            System.out.println("Vadeli hesaptan para çekildi. (Dikkat: Vade bozuldu)");
            return true;
        }
        else {
            System.out.println("Yetersiz Bakiye!");
            return false;
        }
    }


    public double getFaizOrani() {
        return faizOrani;
    }
}