package Backend;

public abstract class Hesap {

    private int hesapNo;
    private double bakiye;
    private String hesapTuru;

    public Hesap(int hesapNo, double baslangicBakiyesi, String hesapTuru) {
        this.hesapNo = hesapNo;
        this.bakiye = baslangicBakiyesi;
        this.hesapTuru = hesapTuru;
    }



    public void paraYatir(double miktar) {
        if (miktar > 0) {
            this.bakiye += miktar;
            System.out.println(hesapNo + " nolu hesaba " + miktar + " TL yatırıldı.");
        }
    }



    public abstract boolean paraCek(double miktar);



    public double getBakiye() {
        return bakiye;
    }



    protected void bakiyeAzalt(double miktar) {
        this.bakiye -= miktar;
    }


    public int getHesapNo() {
        return hesapNo;
    }


    public String getHesapTuru() {
        return hesapTuru;
    }


    @Override
    public String toString() {
        return "No: " + hesapNo + " | Tür: " + hesapTuru + " | Bakiye: " + bakiye + " TL";
    }
}