package Frontend;

import Backend.Banka;
import Backend.Musteri;
import Backend.VeriTabani;
import javax.swing.*;
import java.awt.*;

public class GirisEkrani extends JFrame {

    public GirisEkrani() {

        VeriTabani.yukle();

        setTitle("Banka Sistemi - Güvenli Giriş");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel pnlMerkez = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlMerkez.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtTc = new JTextField();
        JPasswordField txtSifre = new JPasswordField();
        JButton btnGiris = new JButton("Giriş Yap");
        JButton btnKayit = new JButton("Kayıt Ol");

        pnlMerkez.add(new JLabel("TC Kimlik No:"));
        pnlMerkez.add(txtTc);
        pnlMerkez.add(new JLabel("Şifre:"));
        pnlMerkez.add(txtSifre);
        pnlMerkez.add(btnKayit);
        pnlMerkez.add(btnGiris);

        add(new JLabel("Hoşgeldiniz, Lütfen Giriş Yapın", SwingConstants.CENTER), BorderLayout.NORTH);
        add(pnlMerkez, BorderLayout.CENTER);



        btnGiris.addActionListener(e -> {
            String tc = txtTc.getText();
            String sifre = new String(txtSifre.getPassword());

            Musteri m = Banka.girisYap(tc, sifre);
            if (m != null) {
                new MusteriEkrani(m).setVisible(true);
                this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "Hatalı TC veya Şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnKayit.addActionListener(e -> {
            JTextField kAd = new JTextField();
            JTextField kSoyad = new JTextField();
            JTextField kTc = new JTextField();
            JPasswordField kSifre = new JPasswordField();

            Object[] message = {
                    "Ad:", kAd,
                    "Soyad:", kSoyad,
                    "TC Kimlik:", kTc,
                    "Şifre Belirle:", kSifre
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Yeni Müşteri Kaydı", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (!kTc.getText().isEmpty() && !new String(kSifre.getPassword()).isEmpty()) {
                    Banka.musteriEkle(kAd.getText(), kSoyad.getText(), kTc.getText(), new String(kSifre.getPassword()));
                    JOptionPane.showMessageDialog(this, "Kayıt Başarılı! Şimdi giriş yapabilirsiniz.");
                }
                else {
                    JOptionPane.showMessageDialog(this, "TC ve Şifre boş olamaz.");
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new GirisEkrani().setVisible(true));
    }
}