import java.util.*;

public class RailFenceCipher {
    
    // === Normal Rail Fence Methods ===
    
    public static String railFenceEncrypt(String text, int rails) {
        if (rails <= 1) {
            return text;
        }
        
        // Create fence arrays
        StringBuilder[] fence = new StringBuilder[rails];
        for (int i = 0; i < rails; i++) {
            fence[i] = new StringBuilder();
        }
        
        int rail = 0;
        int direction = 1;  // 1 for down, -1 for up
        
        for (char c : text.toCharArray()) {
            fence[rail].append(c);
            rail += direction;
            
            // Change direction at the top or bottom rail
            if (rail == 0 || rail == rails - 1) {
                direction *= -1;
            }
        }
        
        // Join all rails
        StringBuilder result = new StringBuilder();
        for (StringBuilder railText : fence) {
            result.append(railText);
        }
        
        return result.toString();
    }
    
    public static String railFenceDecrypt(String cipherText, int rails) {
        if (rails <= 1) {
            return cipherText;
        }
        
        // Create pattern matrix
        char[][] pattern = new char[rails][cipherText.length()];
        for (int i = 0; i < rails; i++) {
            Arrays.fill(pattern[i], '\n');
        }
        
        // Mark the positions where characters should be placed
        int rail = 0;
        int direction = 1;
        
        for (int i = 0; i < cipherText.length(); i++) {
            pattern[rail][i] = '*';
            rail += direction;
            
            if (rail == 0 || rail == rails - 1) {
                direction *= -1;
            }
        }
        
        // Fill the pattern with ciphertext characters
        int charIndex = 0;
        for (int r = 0; r < rails; r++) {
            for (int c = 0; c < cipherText.length(); c++) {
                if (pattern[r][c] == '*' && charIndex < cipherText.length()) {
                    pattern[r][c] = cipherText.charAt(charIndex);
                    charIndex++;
                }
            }
        }
        
        // Read the plaintext by following the rail pattern
        StringBuilder result = new StringBuilder();
        rail = 0;
        direction = 1;
        
        for (int i = 0; i < cipherText.length(); i++) {
            result.append(pattern[rail][i]);
            rail += direction;
            
            if (rail == 0 || rail == rails - 1) {
                direction *= -1;
            }
        }
        
        return result.toString();
    }
    
    // === Revised Rail Fence Methods (start from bottom rail) ===
    
    public static String railFenceEncryptRevised(String text, int rails) {
        if (rails <= 1) {
            return text;
        }
        
        // Create fence arrays
        StringBuilder[] fence = new StringBuilder[rails];
        for (int i = 0; i < rails; i++) {
            fence[i] = new StringBuilder();
        }
        
        int rail = rails - 1;  // Start from bottom rail
        int direction = -1;    // Start going up
        
        for (char c : text.toCharArray()) {
            fence[rail].append(c);
            rail += direction;
            
            // Change direction at the top or bottom rail
            if (rail == 0 || rail == rails - 1) {
                direction *= -1;
            }
        }
        
        // Join all rails
        StringBuilder result = new StringBuilder();
        for (StringBuilder railText : fence) {
            result.append(railText);
        }
        
        return result.toString();
    }
    
    public static String railFenceDecryptRevised(String cipherText, int rails) {
        if (rails <= 1) {
            return cipherText;
        }
        
        // Create pattern matrix
        char[][] pattern = new char[rails][cipherText.length()];
        for (int i = 0; i < rails; i++) {
            Arrays.fill(pattern[i], '\n');
        }
        
        // Mark the positions where characters should be placed
        int rail = rails - 1;  // Start from bottom rail
        int direction = -1;    // Start going up
        
        for (int i = 0; i < cipherText.length(); i++) {
            pattern[rail][i] = '*';
            rail += direction;
            
            if (rail == 0 || rail == rails - 1) {
                direction *= -1;
            }
        }
        
        // Fill the pattern with ciphertext characters
        int charIndex = 0;
        for (int r = 0; r < rails; r++) {
            for (int c = 0; c < cipherText.length(); c++) {
                if (pattern[r][c] == '*' && charIndex < cipherText.length()) {
                    pattern[r][c] = cipherText.charAt(charIndex);
                    charIndex++;
                }
            }
        }
        
        // Read the plaintext by following the rail pattern
        StringBuilder result = new StringBuilder();
        rail = rails - 1;  // Start from bottom rail
        direction = -1;    // Start going up
        
        for (int i = 0; i < cipherText.length(); i++) {
            result.append(pattern[rail][i]);
            rail += direction;
            
            if (rail == 0 || rail == rails - 1) {
                direction *= -1;
            }
        }
        
        return result.toString();
    }
    
    // === Caesar Cipher Methods ===
    
    public static String caesarEncrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                result.append((char) ((c - base + shift) % 26 + base));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    public static String caesarDecrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                result.append((char) ((c - base - shift + 26) % 26 + base));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    // === Combined Encryption/Decryption for Revised Mode ===
    
    public static String encryptRevised(String text, int rails) {
        String railEncrypted = railFenceEncryptRevised(text, rails);
        return caesarDecrypt(railEncrypted, rails);
    }
    
    public static String decryptRevised(String cipherText, int rails) {
        String caesarDecrypted = caesarEncrypt(cipherText, rails);
        return railFenceDecryptRevised(caesarDecrypted, rails);
    }
    
    // Helper method to visualize the rail fence pattern
    public static void visualizeRailFence(String text, int rails, boolean revised) {
        if (rails <= 1) {
            System.out.println("Text: " + text);
            return;
        }
        
        char[][] pattern = new char[rails][text.length()];
        for (int i = 0; i < rails; i++) {
            Arrays.fill(pattern[i], ' ');
        }
        
        int rail = revised ? rails - 1 : 0;
        int direction = revised ? -1 : 1;
        
        for (int i = 0; i < text.length(); i++) {
            pattern[rail][i] = text.charAt(i);
            rail += direction;
            
            if (rail == 0 || rail == rails - 1) {
                direction *= -1;
            }
        }
        
        System.out.println("Rail Fence Pattern (" + (revised ? "Revised" : "Normal") + "):");
        for (int r = 0; r < rails; r++) {
            System.out.print("Rail " + r + ": ");
            for (int c = 0; c < text.length(); c++) {
                System.out.print(pattern[r][c] + " ");
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Rail Fence Cipher ===");
        System.out.println("Choose mode:");
        System.out.println("1) Normal Rail Fence (starts from top rail)");
        System.out.println("2) Revised Rail Fence (starts from bottom rail + Caesar shift)");
        System.out.print("Enter 1 or 2: ");
        int mode = scanner.nextInt();
        
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter the message to encrypt: ");
        String message = scanner.nextLine();
        
        System.out.print("Enter the number of rails: ");
        int numRails = scanner.nextInt();
        
        if (numRails <= 0) {
            System.out.println("Number of rails must be positive!");
            scanner.close();
            return;
        }
        
        System.out.println("\nOriginal message: " + message);
        System.out.println("Number of rails: " + numRails);
        
        if (mode == 1) {
            // Normal mode
            System.out.println("\n--- Normal Rail Fence Mode ---");
            visualizeRailFence(message, numRails, false);
            
            String encrypted = railFenceEncrypt(message, numRails);
            System.out.println("\nEncrypted: " + encrypted);
            
            String decrypted = railFenceDecrypt(encrypted, numRails);
            System.out.println("Decrypted: " + decrypted);
            
            if (message.equals(decrypted)) {
                System.out.println("✓ Normal encryption/decryption successful!");
            } else {
                System.out.println("✗ Error in normal encryption/decryption!");
            }
        } else {
            // Revised mode
            System.out.println("\n--- Revised Rail Fence Mode ---");
            visualizeRailFence(message, numRails, true);
            
            String railEncrypted = railFenceEncryptRevised(message, numRails);
            System.out.println("\nAfter Rail Fence encryption: " + railEncrypted);
            
            String fullyEncrypted = caesarDecrypt(railEncrypted, numRails);
            System.out.println("Fully encrypted (with Caesar left shift): " + fullyEncrypted);
            
            String caesarDecrypted = caesarEncrypt(fullyEncrypted, numRails);
            System.out.println("After Caesar right shift: " + caesarDecrypted);
            
            String fullyDecrypted = railFenceDecryptRevised(caesarDecrypted, numRails);
            System.out.println("Fully decrypted: " + fullyDecrypted);
            
            if (message.equals(fullyDecrypted)) {
                System.out.println("✓ Revised encryption/decryption successful!");
            } else {
                System.out.println("✗ Error in revised encryption/decryption!");
            }
        }
        
        scanner.close();
    }
}
