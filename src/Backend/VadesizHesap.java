package Backend;

public class VadesizHesap extends Hesap {

    public VadesizHesap(int hesapNo, double baslangicBakiyesi) {
        super(hesapNo, baslangicBakiyesi, "Vadesiz");
    }


    @Override
    public boolean paraCek(double miktar) {
        if (getBakiye() >= miktar) {
            bakiyeAzalt(miktar);
            System.out.println("Vadesiz hesaptan " + miktar + " TL çekildi.");
            return true;
        }
        else {
            System.out.println("Yetersiz Bakiye!");
            return false;
        }
    }
}