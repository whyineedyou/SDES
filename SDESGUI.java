import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SDESGUI {
    private JFrame frame;
    private JTextField txtData;
    private JTextField txtKey;
    private JTextField txtEncrypted;
    private JButton btnEncrypt;

    public SDESGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("S-DES Encryption");

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblData = new JLabel("Data (ASCII or 8-bit):");
        lblData.setBounds(10, 14, 76, 14);
        panel.add(lblData);

        txtData = new JTextField();
        txtData.setBounds(92, 11, 86, 20);
        panel.add(txtData);
        txtData.setColumns(10);

        JLabel lblKey = new JLabel("Key (10-bit):");
        lblKey.setBounds(10, 45, 76, 14);
        panel.add(lblKey);

        txtKey = new JTextField();
        txtKey.setBounds(92, 42, 86, 20);
        panel.add(txtKey);
        txtKey.setColumns(10);

        btnEncrypt = new JButton("Encrypt");
        btnEncrypt.setBounds(10, 80, 89, 23);
        panel.add(btnEncrypt);

        txtEncrypted = new JTextField();
        txtEncrypted.setBounds(107, 80, 86, 23);
        panel.add(txtEncrypted);
        txtEncrypted.setColumns(10);
        txtEncrypted.setEditable(false);

        btnEncrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String dataStr = txtData.getText();
                    int key = Integer.parseInt(txtKey.getText(), 2);

                    int data;
                    if (isBinary(dataStr)) {
                        data = Integer.parseInt(dataStr, 2);
                    } else {
                        data = (int) dataStr.charAt(0);
                    }

                    int encrypted = SDESImplementation.encrypt(data, key);
                    String encryptedBinary = Integer.toBinaryString(encrypted);
                    // 确保输出为8位，不足8位时前面补0
                    encryptedBinary = String.format("%8s", encryptedBinary).replace(' ', '0');
                    txtEncrypted.setText(encryptedBinary);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid binary numbers or ASCII characters.");
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    SDESGUI window = new SDESGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 判断字符串是否是二进制数
    private boolean isBinary(String str) {
        try {
            Integer.parseInt(str, 2);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}