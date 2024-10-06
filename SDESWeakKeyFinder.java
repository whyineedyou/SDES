import java.util.Scanner;

public class SDESWeakKeyFinder {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an 8-bit plain text (binary format): ");
        String plainTextBinary = scanner.nextLine();
        int plainText = Integer.parseInt(plainTextBinary, 2);

        if (plainTextBinary.length() != 8) {
            System.out.println("The input is not an 8-bit number.");
            return;
        }

        boolean weakKeyFound = false;
        for (int key1 = 0; key1 < 1024; key1++) {
            for (int key2 = key1 + 1; key2 < 1024; key2++) {
                int encryptedWithKey1 = SDESImplementation.encrypt(plainText, key1);
                int encryptedWithKey2 = SDESImplementation.encrypt(plainText, key2);
                if (encryptedWithKey1 != encryptedWithKey2) {
                    continue;
                }
                if (!weakKeyFound) {
                    weakKeyFound = true;
                    System.out.println("Weak keys found for plain text " + plainTextBinary + ":");
                }
                System.out.println("Key1: " + formatBinary(key1, 10) + ", Key2: " + formatBinary(key2, 10));
            }
        }
        if (!weakKeyFound) {
            System.out.println("No weak keys found for the given plain text.");
        }
        scanner.close();
    }

    private static String formatBinary(int num, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString(num)).replace(' ', '0');
    }
}