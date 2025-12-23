package Frontend;

import Backend.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MusteriEkrani extends JFrame {

    private Musteri musteri;
    private DefaultListModel<Hesap> hesapListModel;
    private JList<Hesap> hesapListesi;

    public MusteriEkrani(Musteri musteri) {
        this.musteri = musteri;

        setTitle("Müşteri Paneli: " + musteri.getAdSoyad());
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel pnlUst = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnZaman = new JButton("⏳ 1 Ay İleri Git");
        btnZaman.setBackground(Color.ORANGE);
        JButton btnCikis = new JButton("Çıkış Yap");

        pnlUst.add(new JLabel("Aktif Kullanıcı: " + musteri.getAdSoyad() + "  "));
        pnlUst.add(btnZaman);
        pnlUst.add(btnCikis);
        add(pnlUst, BorderLayout.NORTH);

        hesapListModel = new DefaultListModel<>();
        hesapListesi = new JList<>(hesapListModel);
        hesapListesiUpdate();
        JScrollPane scrollPane = new JScrollPane(hesapListesi);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Hesaplarınız"));

        JPanel sagPanel = new JPanel(new GridLayout(2, 1));

        JPanel pnlYeniHesap = new JPanel(new GridLayout(5, 1));
        pnlYeniHesap.setBorder(BorderFactory.createTitledBorder("Yeni Hesap Aç"));

        JRadioButton rbVadesiz = new JRadioButton("Vadesiz Hesap", true);
        JRadioButton rbVadeli = new JRadioButton("Vadeli Hesap");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbVadesiz); bg.add(rbVadeli);

        JTextField txtIlkBakiye = new JTextField("0");
        JButton btnHesapAc = new JButton("Hesap Oluştur");

        pnlYeniHesap.add(rbVadesiz);
        pnlYeniHesap.add(rbVadeli);
        pnlYeniHesap.add(new JLabel("İlk Bakiye:"));
        pnlYeniHesap.add(txtIlkBakiye);
        pnlYeniHesap.add(btnHesapAc);

        JPanel pnlIslemler = new JPanel(new GridLayout(4, 1, 5, 5));
        pnlIslemler.setBorder(BorderFactory.createTitledBorder("Seçili Hesap İşlemleri"));

        JButton btnYatir = new JButton("Para Yatır");
        JButton btnCek = new JButton("Para Çek");
        JButton btnTransfer = new JButton("Havale / Transfer");
        JButton btnKredi = new JButton("Kredi Çek 💰");
        JButton btnBorcOde = new JButton("Borç Öde");
        JButton btnGecmis = new JButton("Hesap Geçmişi (Dekont)");

        pnlIslemler.add(btnYatir);
        pnlIslemler.add(btnCek);
        pnlIslemler.add(btnTransfer);
        pnlIslemler.add(btnKredi);
        pnlIslemler.add(btnBorcOde);
        pnlIslemler.add(btnGecmis);

        sagPanel.add(pnlYeniHesap);
        sagPanel.add(pnlIslemler);

        add(scrollPane, BorderLayout.CENTER);
        add(sagPanel, BorderLayout.EAST);


        btnCikis.addActionListener(e -> {
            new GirisEkrani().setVisible(true);
            this.dispose();
        });


        btnHesapAc.addActionListener(e -> {
            try {
                double bakiye = Double.parseDouble(txtIlkBakiye.getText());
                int rastgeleHesapNo = 1000 + new Random().nextInt(9000);

                Hesap yeniHesap;
                if (rbVadesiz.isSelected()) {
                    yeniHesap = new VadesizHesap(rastgeleHesapNo, bakiye);
                }
                else {
                    yeniHesap = new VadeliHesap(rastgeleHesapNo, bakiye, 15.0);
                }

                musteri.hesapEkle(yeniHesap);
                VeriTabani.kaydet();
                hesapListesiUpdate();
                JOptionPane.showMessageDialog(this, "Hesap Açıldı! No: " + rastgeleHesapNo);

            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Geçersiz bakiye girişi.");
            }
        });


        btnYatir.addActionListener(e -> {
            Hesap h = hesapListesi.getSelectedValue();
            if (h != null) {
                String miktarStr = JOptionPane.showInputDialog(this, "Yatırılacak Miktar:");
                if (miktarStr != null) {
                    try {
                        double miktar = Double.parseDouble(miktarStr);
                        h.paraYatir(miktar);
                        VeriTabani.kaydet();
                        hesapListesi.repaint();
                        JOptionPane.showMessageDialog(this, "Para yatırıldı.");
                    }
                    catch (Exception ex) { JOptionPane.showMessageDialog(this, "Hata oluştu!"); }
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Lütfen bir hesap seçin.");
            }
        });


        btnCek.addActionListener(e -> {
            Hesap h = hesapListesi.getSelectedValue();
            if (h != null) {
                String miktarStr = JOptionPane.showInputDialog(this, "Çekilecek Miktar:");
                if (miktarStr != null) {
                    try {
                        double miktar = Double.parseDouble(miktarStr);
                        if (h.paraCek(miktar)) {
                            VeriTabani.kaydet();
                            hesapListesi.repaint();
                            JOptionPane.showMessageDialog(this, "İşlem Başarılı");
                        }
                        else {
                            JOptionPane.showMessageDialog(this, "Yetersiz Bakiye!");
                        }
                    }
                    catch (Exception ex) { JOptionPane.showMessageDialog(this, "Hata oluştu!"); }
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Lütfen bir hesap seçin.");
            }
        });


        btnTransfer.addActionListener(e -> {
            Hesap gonderen = hesapListesi.getSelectedValue();

            if (gonderen == null) {
                JOptionPane.showMessageDialog(this, "Lütfen para göndereceğiniz kendi hesabınızı seçin.");
                return;
            }

            ArrayList<AliciSecenek> aliciListesi = new ArrayList<>();

            for (Musteri m : Banka.musteriler) {
                for (Hesap h : m.getHesaplar()) {
                    if (h.getHesapNo() != gonderen.getHesapNo()) {
                        aliciListesi.add(new AliciSecenek(m, h));
                    }
                }
            }

            if (aliciListesi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Sistemde para gönderilecek başka hesap bulunamadı.");
                return;
            }

            Object secim = JOptionPane.showInputDialog(
                    this,
                    "Lütfen alıcıyı seçiniz:",
                    "Transfer İşlemi",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    aliciListesi.toArray(),
                    aliciListesi.get(0)
            );

            if (secim != null) {
                AliciSecenek secilenAlici = (AliciSecenek) secim;
                Hesap aliciHesap = secilenAlici.getHesap();

                String miktarStr = JOptionPane.showInputDialog(this,
                        "Alıcı: " + secilenAlici.toString() + "\nTransfer Miktarı:");

                if (miktarStr != null) {
                    try {
                        double miktar = Double.parseDouble(miktarStr);

                        boolean sonuc = Banka.paraTransferi(gonderen, aliciHesap, miktar);

                        if (sonuc) {
                            JOptionPane.showMessageDialog(this, "Transfer Başarılı!\n" +
                                    miktar + " TL -> " + secilenAlici.getMusteriAd());
                            hesapListesi.repaint();
                        }
                        else {
                            JOptionPane.showMessageDialog(this, "Yetersiz Bakiye!");
                        }
                    }
                    catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Geçersiz tutar girdiniz.");
                    }
                }
            }
        });


        btnGecmis.addActionListener(e -> {
            Hesap seciliHesap = hesapListesi.getSelectedValue();
            if (seciliHesap != null) {

                ArrayList<Islem> islemler = seciliHesap.getIslemler();

                String[] kolonlar = {"Tarih", "Açıklama", "Tutar"};

                String[][] veri = new String[islemler.size()][3];

                for (int i = 0; i < islemler.size(); i++) {
                    Islem islem = islemler.get(islemler.size() - 1 - i);
                    veri[i][0] = islem.getTarihString();
                    veri[i][1] = islem.getAciklama();
                    veri[i][2] = islem.getTutar() + " TL";
                }

                JTable tablo = new JTable(veri, kolonlar);
                tablo.setFillsViewportHeight(true);

                JDialog dialog = new JDialog(this, "Hesap Hareketleri: " + seciliHesap.getHesapNo(), true);
                dialog.setSize(500, 400);
                dialog.setLocationRelativeTo(this);
                dialog.add(new JScrollPane(tablo));
                dialog.setVisible(true);

            }
            else {
                JOptionPane.showMessageDialog(this, "Geçmişi görüntülemek için bir hesap seçin.");
            }
        });


        btnZaman.addActionListener(e -> {
            int cevap = JOptionPane.showConfirmDialog(this,
                    "1 Ay ileri gidilecek. Onaylıyor musunuz?",
                    "Zaman Simülasyonu", JOptionPane.YES_NO_OPTION);

            if (cevap == JOptionPane.YES_OPTION) {
                String rapor = Banka.birAyIleriSar();

                hesapListesi.repaint();
                JOptionPane.showMessageDialog(this, rapor);
            }
        });


        btnKredi.addActionListener(e -> {
            Hesap seciliHesap = hesapListesi.getSelectedValue();

            if (seciliHesap != null) {
                if (seciliHesap instanceof KrediAlabilir) {

                    KrediAlabilir krediHesabi = (KrediAlabilir) seciliHesap;
                    double limit = krediHesabi.krediLimitiniOgren();

                    String miktarStr = JOptionPane.showInputDialog(this,
                            "Kredi Limitin: " + limit + " TL\nÇekmek istediğin tutar:");

                    if (miktarStr != null) {
                        try {
                            double miktar = Double.parseDouble(miktarStr);

                            if (miktar > 0 && miktar <= limit) {
                                krediHesabi.krediAl(miktar);

                                VeriTabani.kaydet();
                                hesapListesi.repaint();
                                JOptionPane.showMessageDialog(this, "Kredi onaylandı! Hesabınıza " + miktar + " TL yattı.");
                            }
                            else {
                                JOptionPane.showMessageDialog(this, "Geçersiz tutar veya limit aşıldı.");
                            }

                        }
                        catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Sayısal değer girin.");
                        }
                    }

                }
                else {
                    JOptionPane.showMessageDialog(this,
                            "Bu hesap türü kredi çekmek için uygun değildir.\nLütfen Vadesiz Hesap seçin.",
                            "Hata", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Lütfen bir hesap seçin.");
            }
        });


        btnBorcOde.addActionListener(e -> {
            Hesap seciliHesap = hesapListesi.getSelectedValue();

            if (seciliHesap != null && seciliHesap instanceof VadesizHesap) {
                VadesizHesap vh = (VadesizHesap) seciliHesap;

                if (vh.getKrediBorcu() > 0) {
                    String miktarStr = JOptionPane.showInputDialog(this,
                            "Toplam Borç: " + vh.getKrediBorcu() + " TL\n" +
                                    "Asgari Ödeme: " + vh.getAylikAsgariOdeme() + " TL\n" +
                                    "Ödemek istediğiniz tutar:");

                    if (miktarStr != null) {
                        try {
                            double miktar = Double.parseDouble(miktarStr);
                            if (vh.borcOde(miktar)) {
                                VeriTabani.kaydet();
                                hesapListesi.repaint();
                                JOptionPane.showMessageDialog(this, "Ödeme başarıyla alındı.");
                            }
                            else {
                                JOptionPane.showMessageDialog(this, "Yetersiz bakiye!");
                            }
                        }
                        catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Geçersiz tutar.");
                        }
                    }
                }
                else {
                    JOptionPane.showMessageDialog(this, "Bu hesabın kredi borcu yoktur.");
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Lütfen bir Vadesiz Hesap seçin.");
            }
        });
    }


    private void hesapListesiUpdate() {
        hesapListModel.clear();
        for (Hesap h : musteri.getHesaplar()) {
            hesapListModel.addElement(h);
        }
        if (!hesapListModel.isEmpty()) {
            hesapListesi.setSelectedIndex(0);
        }
    }


    private Hesap hesapBul(int hesapNo) {
        for (Musteri m : Banka.musteriler) {
            for (Hesap h : m.getHesaplar()) {
                if (h.getHesapNo() == hesapNo) return h;
            }
        }
        return null;
    }


    private static class AliciSecenek {

        private Musteri musteri;
        private Hesap hesap;


        public AliciSecenek(Musteri musteri, Hesap hesap) {
            this.musteri = musteri;
            this.hesap = hesap;
        }


        public Hesap getHesap() {
            return hesap;
        }


        public String getMusteriAd() {
            return musteri.getAdSoyad();
        }


        @Override
        public String toString() {
            return musteri.getAdSoyad() + " (" + hesap.getHesapTuru() + " - " + hesap.getHesapNo() + ")";
        }
    }
}