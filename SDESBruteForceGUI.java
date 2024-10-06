import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SDESBruteForceGUI {
    private JFrame frame;
    private JTextArea txtCipherTextPlainTextPairs;
    private JButton btnStart;
    private SDESImplementation sdes;

    public SDESBruteForceGUI() {
        this.sdes = new SDESImplementation();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("S-DES Brute Force Key Finder");

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblCipherTextPlainTextPairs = new JLabel("Plain/Cipher Text Pairs (8-bit each, one pair per line, separated by space):");
        lblCipherTextPlainTextPairs.setBounds(10, 14, 400, 14);
        panel.add(lblCipherTextPlainTextPairs);

        txtCipherTextPlainTextPairs = new JTextArea();
        txtCipherTextPlainTextPairs.setBounds(10, 30, 250, 60);
        panel.add(txtCipherTextPlainTextPairs);

        btnStart = new JButton("Start Brute Force");
        btnStart.setBounds(10, 100, 250, 23);
        panel.add(btnStart);

        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] pairs = txtCipherTextPlainTextPairs.getText().split("\n");
                StringBuilder results = new StringBuilder();
                long startTime = System.currentTimeMillis(); // Record start time
                for (String pair : pairs) {
                    String[] parts = pair.split(" ");
                    if (parts.length == 2 && parts[0].length() == 8 && parts[1].length() == 8) {
                        try {
                            int plainText = Integer.parseInt(parts[0].trim(), 2);
                            int cipherText = Integer.parseInt(parts[1].trim(), 2);
                            ArrayList<String> keys = bruteForceKey(plainText, cipherText);
                            for (String key : keys) {
                                results.append("Plain: ").append(parts[0].trim())
                                        .append(" Cipher: ").append(parts[1].trim())
                                        .append(" Key: ").append(key).append("\n");
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Please enter valid 8-bit plain text and 8-bit cipher text pairs.");
                            return;
                        }
                    } else {
                        System.out.println("Please enter valid 8-bit plain text and 8-bit cipher text pairs separated by space.");
                        return;
                    }
                }
                long endTime = System.currentTimeMillis();
                long timeElapsed = endTime - startTime;
                System.out.println(results.toString());
                System.out.println("Time taken for brute force: " + timeElapsed + " ms");
            }
        });

        frame.setVisible(true);
    }

    private ArrayList<String> bruteForceKey(int plainText, int cipherText) {
        ArrayList<String> keys = new ArrayList<>();
        for (int key = 0; key < 1024; key++) {
            int subKey = SDESImplementation.keyExpansion(key);
            int left = plainText >> 4 & 0xF;
            int right = plainText & 0xF;
            int round1 = SDESImplementation.roundFunction(right, subKey);
            left = left ^ round1;
            int temp = left;
            left = right;
            right = temp;
            int round2 = SDESImplementation.roundFunction(right, subKey);
            left = left ^ round2;
            int encrypted = (left << 4) | right;
            if (SDESImplementation.finalPermutation(encrypted) == cipherText) {
                String binaryKey = Integer.toBinaryString(key);
                while (binaryKey.length() < 10) {
                    binaryKey = "0" + binaryKey;
                }
                keys.add(binaryKey);
            }
        }
        return keys;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    SDESBruteForceGUI window = new SDESBruteForceGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}