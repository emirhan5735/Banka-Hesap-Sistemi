package Backend;

public class VadesizHesap extends Hesap implements KrediAlabilir {


    private double krediBorcu = 0;
    private double aylikAsgariOdeme = 0;
    private boolean asgariOdendiMi = false;

    public VadesizHesap(int hesapNo, double baslangicBakiyesi) {
        super(hesapNo, baslangicBakiyesi, "Vadesiz");
    }

    @Override
    public boolean paraCek(double miktar, String aciklama) {
        if (getBakiye() >= miktar) {
            bakiyeAzalt(miktar);
            islemEkle(aciklama, -miktar);
            return true;
        }
        return false;
    }


    @Override
    public double krediLimitiniOgren() {
        double limit = getBakiye() * 5;
        if (limit < 5000) return 5000;
        return limit;
    }


    @Override
    public void krediAl(double miktar) {
        paraYatir(miktar, "Kredi Kullanımı (Nakit Avans)");

        double faizliBorc = miktar * 1.20;
        this.krediBorcu += faizliBorc;

        this.aylikAsgariOdeme = this.krediBorcu * 0.10;

        this.asgariOdendiMi = false;
    }


    public boolean borcOde(double miktar) {
        if (getBakiye() >= miktar) {
            bakiyeAzalt(miktar);
            islemEkle("Kredi Borcu Ödemesi", -miktar);

            this.krediBorcu -= miktar;

            if (this.krediBorcu < 0) this.krediBorcu = 0;

            if (miktar >= this.aylikAsgariOdeme || this.krediBorcu == 0) {
                this.asgariOdendiMi = true;

                if (this.krediBorcu == 0) {
                    this.aylikAsgariOdeme = 0;
                }
            }
            return true;
        }
        return false;
    }


    public String aySonuIslemi() {
        String mesaj = "";

        if (this.krediBorcu > 0 && !this.asgariOdendiMi) {
            double gecikmeFaizi = this.krediBorcu * 0.05;
            this.krediBorcu += gecikmeFaizi;

            this.aylikAsgariOdeme = this.krediBorcu * 0.10;

            mesaj = String.format("DİKKAT: %d nolu hesaba %.2f TL gecikme faizi eklendi!", getHesapNo(), gecikmeFaizi);
        }

        if (this.krediBorcu > 0) {
            this.asgariOdendiMi = false;
        }

        return mesaj;
    }


    public double getKrediBorcu() {
        return krediBorcu;
    }


    public double getAylikAsgariOdeme() {
        return aylikAsgariOdeme;
    }


    public boolean isAsgariOdendiMi() {
        return asgariOdendiMi;
    }


    @Override
    public String toString() {
        String temelBilgi = super.toString();
        if (krediBorcu > 0) {
            return temelBilgi + " | BORÇ: " + String.format("%.2f", krediBorcu) + " TL";
        }
        return temelBilgi;
    }
}