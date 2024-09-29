import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TugasPengenalanJFC extends JFrame implements ChangeListener {

    private JTextArea textArea;

    // Komponen untuk Slider (FPS)  
    static final int FPS_MIN = 0;
    static final int FPS_MAX = 30;
    static final int FPS_INIT = 15;
    private Timer timer;
    private boolean frozen = false;
    private int delay;
    private JSlider fpsSlider;
    private JLabel fpsLabel;

    // Komponen untuk JPasswordField
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    // Komponen Spinner untuk Bulan dan Tahun
    private JSpinner monthSpinner;
    private JSpinner yearSpinner;
    private JFormattedTextField dateField;

    public TugasPengenalanJFC() {
        setTitle("Form Pendaftaran Nasabah Bank");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Membuat JTextArea untuk simulasi input/output
        textArea = new JTextArea(10, 20);  // Kurangi ukuran text area
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        // Membuat Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // Membuat Menu "A Menu"
        JMenu aMenu = new JMenu("Menu Utama");

        // Membuat item untuk membuka dialog pemilihan tabungan
        JMenuItem chooseTabunganItem = new JMenuItem("Pilih Jenis Tabungan");

        // Membuat item untuk mengatur FPS dan password
        JMenuItem setFPSItem = new JMenuItem("Atur Jumlah Frekuensi Transaksi Per Bulan");
        JMenuItem checkPasswordItem = new JMenuItem("Cek Password");
        JMenuItem chooseDateItem = new JMenuItem("Pilih Bulan dan Tahun");

        // Membuat item Reset dan Exit
        JMenuItem resetItem = new JMenuItem("Reset");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Aksi untuk membuka dialog pemilihan tabungan
        chooseTabunganItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NameChooserDialog dialog = new NameChooserDialog(TugasPengenalanJFC.this);
                dialog.setVisible(true);
                String chosenName = dialog.getSelectedName();

                if (chosenName != null) {
                    textArea.setText("Jenis Tabungan yang Dipilih: " + chosenName);
                } else {
                    textArea.setText("Mohon Pilih jenis tabungan");
                }
            }
        });

        // Aksi untuk mengatur FPS
        setFPSItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFPSDialog();
            }
        });

        // Aksi untuk cek password
        checkPasswordItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPasswordDialog();
            }
        });

        // Aksi untuk memilih bulan dan tahun
        chooseDateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDateChooserDialog();
            }
        });

        // Aksi untuk Reset
        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(""); // Menghapus semua inputan dan output
                fpsSlider.setValue(FPS_INIT); // Reset slider FPS
                passwordField.setText(""); // Reset password field
                confirmPasswordField.setText(""); // Reset confirm password field
            }
        });

        // Aksi untuk Exit
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Keluar dari aplikasi
            }
        });

        // Menambahkan item ke dalam "A Menu"
        aMenu.add(chooseTabunganItem);
        aMenu.add(setFPSItem);
        aMenu.add(checkPasswordItem);
        aMenu.add(chooseDateItem);
        aMenu.add(resetItem);
        aMenu.add(exitItem);

        // Menambahkan menu ke menu bar
        menuBar.add(aMenu);

        // Menambahkan menu bar ke frame
        setJMenuBar(menuBar);

        // Inisialisasi Slider untuk FPS
        fpsSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 10);
        fpsSlider.setMajorTickSpacing(10);
        fpsSlider.setMinorTickSpacing(1);
        fpsSlider.setPaintTicks(true);
        fpsSlider.setPaintLabels(true);
        fpsSlider.setFont(new Font("Serif", Font.ITALIC, 15));
        fpsSlider.addChangeListener(this);

        fpsLabel = new JLabel("Jumlah Frekuensi Transaksi Per Bulan: " + 10);

        // Inisialisasi timer untuk FPS animasi
        delay = 1000 / FPS_INIT;
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Animation frame update at FPS: " + (1000 / delay));
            }
        });
        startAnimation();
    }

    private void showFPSDialog() {
        JPanel fpsPanel = new JPanel();
        fpsPanel.setLayout(new BoxLayout(fpsPanel, BoxLayout.Y_AXIS));
        fpsPanel.add(new JLabel("Atur Jumlah Frekuensi Transaksi Per Bulan"));
        fpsSlider.setValue((int) fpsSlider.getValue());
        fpsPanel.add(fpsSlider);
        JOptionPane.showMessageDialog(this, fpsPanel, "Pengaturan FPS", JOptionPane.PLAIN_MESSAGE);
    }

    private void showPasswordDialog() {
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));

        passwordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);
        passwordPanel.add(new JLabel("Password:"));
        passwordPanel.add(passwordField);
        passwordPanel.add(new JLabel("Konfirmasi Password:"));
        passwordPanel.add(confirmPasswordField);

        JButton checkPasswordButton = new JButton("Cek Password");
        checkPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (password.equals(confirmPassword)) {
                    textArea.setText("Password Cocok.");
                } else {
                    textArea.setText("Password Tidak Cocok.");
                }
            }
        });

        passwordPanel.add(checkPasswordButton);

        JOptionPane.showMessageDialog(this, passwordPanel, "Cek Password", JOptionPane.PLAIN_MESSAGE);
    }

    private void showDateChooserDialog() {
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new GridLayout(3, 2, 5, 5));

        // Spinner for Month
        JLabel monthLabel = new JLabel("Bulan:");
        String[] months = new SimpleDateFormat().getDateFormatSymbols().getMonths();
        SpinnerListModel monthModel = new SpinnerListModel(months);
        monthSpinner = new JSpinner(monthModel);
        datePanel.add(monthLabel);
        datePanel.add(monthSpinner);

        // Spinner for Year
        JLabel yearLabel = new JLabel("Tahun:");
        SpinnerNumberModel yearModel = new SpinnerNumberModel(2007, 1900, 2100, 1);
        yearSpinner = new JSpinner(yearModel);
        datePanel.add(yearLabel);
        datePanel.add(yearSpinner);

        // Spinner for Another Date (Month/Year)
        JLabel anotherDateLabel = new JLabel("Tanggal Lahir (MM/YYYY):");
        DateFormat format = new SimpleDateFormat("MM/yyyy");
        dateField = new JFormattedTextField(new DateFormatter(format));
        dateField.setValue(Calendar.getInstance().getTime());
        datePanel.add(anotherDateLabel);
        datePanel.add(dateField);

        JOptionPane.showMessageDialog(this, datePanel, "Pilih tanggal Bulan dan Tahun Lahir", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        // Menjalankan GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TugasPengenalanJFC().setVisible(true);
            }
        });
    }

    // Mulai animasi FPS
    public void startAnimation() {
        frozen = false;
        timer.start();
    }

    // Hentikan animasi FPS
    public void stopAnimation() {
        frozen = true;
        timer.stop();
    }

    // Mengatasi perubahan slider FPS
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int fps = (int) source.getValue();
            fpsLabel.setText("Jumlah Frekuensi Transaksi Per Bulan: " + fps);
            if (fps == 0) {
                if (!frozen) stopAnimation();
            } else {
                delay = 1000 / fps;
                timer.setDelay(delay);
                timer.setInitialDelay(delay);
                if (frozen) startAnimation();
            }
        }
    }

    private class NameChooserDialog extends JDialog {
        private String selectedName;

        public NameChooserDialog(Frame owner) {
            super(owner, "Pilih Jenis Tabungan", true);
            setLayout(new FlowLayout());
            setSize(300, 200);

            String[] tabunganNames = {
                "Tabungan Umum", 
                "Tabungan Pendidikan", 
                "Tabungan Haji", 
                "Tabungan Emas", 
                "Tabungan Berjangka", 
                "Tabungan Rencana", 
                "Tabungan Syariah", 
                "Tabungan Bisnis", 
                "Tabungan Online", 
                "Tabungan Remaja"
            };
            JComboBox<String> tabunganComboBox = new JComboBox<>(tabunganNames);
            add(tabunganComboBox);
            
            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedName = (String) tabunganComboBox.getSelectedItem();
                    dispose();
                }
            });
            add(okButton);

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setVisible(true);
        }

        public String getSelectedName() {
            return selectedName;
        }
    }
}
