package Frontend;

import Backend.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class MusteriEkrani extends JFrame {
    private Musteri musteri;
    private DefaultListModel<Hesap> hesapListModel;
    private JList<Hesap> hesapListesi;


    public MusteriEkrani(Musteri musteri) {
        this.musteri = musteri;

        setTitle("Müşteri Paneli: " + musteri.getAdSoyad());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());


        hesapListModel = new DefaultListModel<>();
        hesapListesiUpdate();
        hesapListesi = new JList<>(hesapListModel);

        JScrollPane scrollPane = new JScrollPane(hesapListesi);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Hesaplarınız"));


        JPanel sagPanel = new JPanel(new GridLayout(2, 1));


        JPanel pnlYeniHesap = new JPanel(new GridLayout(5, 1));
        pnlYeniHesap.setBorder(BorderFactory.createTitledBorder("Yeni Backend.Hesap Aç"));

        JRadioButton rbVadesiz = new JRadioButton("Vadesiz Backend.Hesap", true);
        JRadioButton rbVadeli = new JRadioButton("Vadeli Backend.Hesap");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbVadesiz); bg.add(rbVadeli);

        JTextField txtIlkBakiye = new JTextField("0");
        JButton btnHesapAc = new JButton("Backend.Hesap Oluştur");

        pnlYeniHesap.add(rbVadesiz);
        pnlYeniHesap.add(rbVadeli);
        pnlYeniHesap.add(new JLabel("İlk Bakiye:"));
        pnlYeniHesap.add(txtIlkBakiye);
        pnlYeniHesap.add(btnHesapAc);


        JPanel pnlIslemler = new JPanel(new GridLayout(3, 1, 5, 5));
        pnlIslemler.setBorder(BorderFactory.createTitledBorder("Seçili Backend.Hesap İşlemleri"));

        JButton btnYatir = new JButton("Para Yatır");
        JButton btnCek = new JButton("Para Çek");
        JButton btnTransfer = new JButton("Havale/Transfer Yap");

        pnlIslemler.add(btnYatir);
        pnlIslemler.add(btnCek);
        pnlIslemler.add(btnTransfer);

        sagPanel.add(pnlYeniHesap);
        sagPanel.add(pnlIslemler);

        add(scrollPane, BorderLayout.CENTER);
        add(sagPanel, BorderLayout.EAST);


        btnHesapAc.addActionListener(e -> {
            try {
                double bakiye = Double.parseDouble(txtIlkBakiye.getText());
                int rastgeleHesapNo = 1000 + new Random().nextInt(9000);

                Hesap yeniHesap;
                if (rbVadesiz.isSelected()) {
                    yeniHesap = new VadesizHesap(rastgeleHesapNo, bakiye);
                } else {
                    yeniHesap = new VadeliHesap(rastgeleHesapNo, bakiye, 15.0);
                }

                musteri.hesapEkle(yeniHesap);
                hesapListesiUpdate();
                JOptionPane.showMessageDialog(this, "Backend.Hesap Açıldı! No: " + rastgeleHesapNo);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bir bakiye girin.");
            }
        });


        btnYatir.addActionListener(e -> {
            Hesap seciliHesap = hesapListesi.getSelectedValue();
            if (seciliHesap != null) {
                String miktarStr = JOptionPane.showInputDialog(this, "Yatırılacak Miktar:");
                if(miktarStr != null) {
                    double miktar = Double.parseDouble(miktarStr);
                    seciliHesap.paraYatir(miktar);
                    hesapListesi.repaint();
                }
            } else {
                uyariVer();
            }
        });


        btnCek.addActionListener(e -> {
            Hesap seciliHesap = hesapListesi.getSelectedValue();
            if (seciliHesap != null) {
                String miktarStr = JOptionPane.showInputDialog(this, "Çekilecek Miktar:");
                if(miktarStr != null) {
                    double miktar = Double.parseDouble(miktarStr);
                    boolean sonuc = seciliHesap.paraCek(miktar);
                    if(sonuc) {
                        JOptionPane.showMessageDialog(this, "İşlem Başarılı");
                        hesapListesi.repaint();
                    } else {
                        JOptionPane.showMessageDialog(this, "Bakiye Yetersiz!");
                    }
                }
            } else {
                uyariVer();
            }
        });


        btnTransfer.addActionListener(e -> {
            Hesap gonderenHesap = hesapListesi.getSelectedValue();
            if (gonderenHesap == null) {
                JOptionPane.showMessageDialog(this, "Lütfen önce kendi listenizden para gönderecek hesabı seçin!");
                return;
            }


            class HesapSecenek {
                Hesap hesap;
                String sahibi;

                public HesapSecenek(Hesap hesap, String sahibi) {
                    this.hesap = hesap;
                    this.sahibi = sahibi;
                }

                @Override
                public String toString() {
                    return "Hesap No: " + hesap.getHesapNo() + " - " + sahibi;
                }
            }



            java.util.ArrayList<HesapSecenek> secenekler = new java.util.ArrayList<>();


            for (Musteri m : Banka.musteriler) {
                for (Hesap h : m.getHesaplar()) {
                    if (h != gonderenHesap) {
                        secenekler.add(new HesapSecenek(h, m.getAdSoyad()));
                    }
                }
            }

            if (secenekler.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Sistemde başka hesap bulunamadı.");
                return;
            }


            HesapSecenek[] secenekDizisi = secenekler.toArray(new HesapSecenek[0]);

            HesapSecenek secilenSecenek = (HesapSecenek) JOptionPane.showInputDialog(
                    this,
                    "Alıcı Hesabı Seçin:",
                    "Transfer",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    secenekDizisi,
                    secenekDizisi[0]
            );


            if (secilenSecenek != null) {

                Hesap aliciHesap = secilenSecenek.hesap;

                String miktarStr = JOptionPane.showInputDialog(this, "Gönderilecek Miktar (TL):");
                if (miktarStr != null && !miktarStr.isEmpty()) {
                    try {
                        double miktar = Double.parseDouble(miktarStr);
                        boolean sonuc = Banka.paraTransferi(gonderenHesap, aliciHesap, miktar);

                        if (sonuc) {
                            JOptionPane.showMessageDialog(this,
                                    "Transfer Başarılı!\n" +
                                            "Alıcı: " + secilenSecenek.sahibi + "\n" +
                                            "Tutar: " + miktar + " TL"
                            );
                            hesapListesi.repaint();
                        } else {
                            JOptionPane.showMessageDialog(this, "Yetersiz Bakiye!");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Geçersiz Tutar");
                    }
                }
            }
        });
    }


    private void hesapListesiUpdate() {
        hesapListModel.clear();
        for (Hesap h : musteri.getHesaplar()) {
            hesapListModel.addElement(h);
        }
    }


    private Hesap hedefHesabiBul(int hesapNo) {
        for (Musteri m : Banka.musteriler) {
            for (Hesap h : m.getHesaplar()) {
                if (h.getHesapNo() == hesapNo) {
                    return h;
                }
            }
        }
        return null;
    }

    private void uyariVer() {
        JOptionPane.showMessageDialog(this, "Lütfen listeden bir hesap seçin.");
    }
}