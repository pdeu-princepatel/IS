import java.util.Scanner;

public class caser {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter the text to Encrypt:");
        String text = sc.nextLine();

        System.out.println("Choose Caesar Cipher type:");
        System.out.println("1. Normal Caesar Cipher");
        System.out.println("2. Twisted Revised Caesar Cipher");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter shift value (0-25): ");
                int shiftNormal = sc.nextInt();
                Normal normal = new Normal();
                String encryptedNormal = normal.encrypt(text, shiftNormal);
                String decryptedNormal = normal.decrypt(encryptedNormal, shiftNormal);
                System.out.println("\n--- Normal Caesar Cipher ---");
                System.out.println("Encrypted Text: " + encryptedNormal);
                System.out.println("Decrypted Text: " + decryptedNormal);
                break;

            case 2:
                System.out.print("Enter shift value (0-25): ");
                int shiftRevised = sc.nextInt();
                Revised revised = new Revised();
                String encryptedRevised = revised.encrypt(text, shiftRevised);
                String decryptedRevised = revised.decrypt(encryptedRevised, shiftRevised);
                System.out.println("\n--- Twisted Revised Caesar Cipher ---");
                System.out.println("Encrypted Text: " + encryptedRevised);
                System.out.println("Decrypted Text: " + decryptedRevised);
                break;

            default:
                System.out.println("Invalid option.");
                break;
        }

        sc.close();
    }
}
class Normal {
    String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append((char) (((c - 'A' + shift) % 26) + 'A'));
            } else if (Character.isLowerCase(c)) {
                result.append((char) (((c - 'a' + shift) % 26) + 'a'));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    String decrypt(String text, int shift) {
        return encrypt(text, 26 - (shift % 26));
    }
}
class Revised {
    // Encrypt with case-mirroring and shift
    public String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                // Mirror to lowercase, then shift in lowercase range
                char mirrored = (char) ('z' - (c - 'A'));
                char shifted = (char) ((mirrored - 'a' + shift) % 26 + 'a');
                result.append(shifted);
            } else if (Character.isLowerCase(c)) {
                // Mirror to uppercase, then shift in uppercase range
                char mirrored = (char) ('Z' - (c - 'a'));
                char shifted = (char) ((mirrored - 'A' + shift) % 26 + 'A');
                result.append(shifted);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    // Decrypt by reversing the steps
    public String decrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLowerCase(c)) {
                // Reverse Caesar shift in lowercase, then mirror to uppercase
                char unshifted = (char) ((c - 'a' - shift + 26) % 26 + 'a');
                char original = (char) ('A' + ('z' - unshifted));
                result.append(original);
            } else if (Character.isUpperCase(c)) {
                // Reverse Caesar shift in uppercase, then mirror to lowercase
                char unshifted = (char) ((c - 'A' - shift + 26) % 26 + 'A');
                char original = (char) ('a' + ('Z' - unshifted));
                result.append(original);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }
}
