package Frontend;

import Backend.Banka;
import Backend.Musteri;
import Backend.VadesizHesap;
import javax.swing.*;
import java.awt.*;

public class AnaEkran extends JFrame {
    private DefaultListModel<Musteri> listModel;
    private JList<Musteri> musteriListesi;
    private JTextField txtAd, txtSoyad, txtTc;

    public AnaEkran() {
        setTitle("Backend.Banka Otomasyonu - Giriş");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        listModel = new DefaultListModel<>();

        musteriListesi = new JList<>(listModel);


        JPanel panelEkle = new JPanel(new GridLayout(4, 2));
        panelEkle.setBorder(BorderFactory.createTitledBorder("Yeni Müşteri Ekle"));

        txtAd = new JTextField();
        txtSoyad = new JTextField();
        txtTc = new JTextField();
        JButton btnEkle = new JButton("Müşteri Ekle");

        panelEkle.add(new JLabel("Ad:")); panelEkle.add(txtAd);
        panelEkle.add(new JLabel("Soyad:")); panelEkle.add(txtSoyad);
        panelEkle.add(new JLabel("TC:")); panelEkle.add(txtTc);
        panelEkle.add(new JLabel("")); panelEkle.add(btnEkle); // Boşluk için label


        JButton btnGiris = new JButton("Seçili Müşterinin İşlemlerine Git >");
        btnGiris.setFont(new Font("Arial", Font.BOLD, 14));
        btnGiris.setBackground(Color.CYAN);


        add(panelEkle, BorderLayout.NORTH);
        add(new JScrollPane(musteriListesi), BorderLayout.CENTER);
        add(btnGiris, BorderLayout.SOUTH);


        btnEkle.addActionListener(e -> {
            String ad = txtAd.getText();
            String soyad = txtSoyad.getText();
            String tc = txtTc.getText();

            if (!ad.isEmpty() && !soyad.isEmpty() && !tc.isEmpty()) {
                Musteri yeniMusteri = new Musteri(ad, soyad, tc);
                Banka.musteriler.add(yeniMusteri); // Backend.Banka listesine ekle
                listModel.addElement(yeniMusteri); // Ekrana ekle

                txtAd.setText(""); txtSoyad.setText(""); txtTc.setText("");
                JOptionPane.showMessageDialog(this, "Müşteri Eklendi!");
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.");
            }
        });


        btnGiris.addActionListener(e -> {
            Musteri seciliMusteri = musteriListesi.getSelectedValue();
            if (seciliMusteri != null) {
                // Seçilen müşteriyi parametre olarak diğer ekrana gönderiyoruz
                new MusteriEkrani(seciliMusteri).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen listeden bir müşteri seçin.");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnaEkran().setVisible(true));
    }
}