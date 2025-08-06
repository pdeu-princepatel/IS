import java.util.*;

public class CompletePlayfairCipher {
    
    static char[][] keyTable = new char[5][5];
    
    // Convert string to lowercase
    static void toLowerCase(StringBuilder text) {
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                text.setCharAt(i, (char)(ch + 32));
            }
        }
    }
    
    // Remove spaces
    static void removeSpaces(StringBuilder text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                text.deleteCharAt(i);
                i--;
            }
        }
    }
    
    // Replace 'J' with 'I' always
    static void replaceJWithI(StringBuilder text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == 'j') {
                text.setCharAt(i, 'i');
            }
        }
    }
    
    // Make text length even by appending 'x'
    static void prepareText(StringBuilder text) {
        if (text.length() % 2 != 0) {
            text.append('x');
        }
    }
    
    // Generate key table (5x5 matrix)
    static void generateKeyTable(StringBuilder key) {
        int[] used = new int[26];
        int i = 0, j = 0;
        
        for (int k = 0; k < key.length(); k++) {
            char ch = key.charAt(k);
            if (ch == 'j') ch = 'i';
            if (used[ch - 'a'] == 0) {
                used[ch - 'a'] = 1;
                keyTable[i][j++] = ch;
                if (j == 5) {
                    j = 0;
                    i++;
                }
            }
        }
        
        for (char ch = 'a'; ch <= 'z'; ch++) {
            if (ch == 'j') continue;
            if (used[ch - 'a'] == 0) {
                used[ch - 'a'] = 1;
                keyTable[i][j++] = ch;
                if (j == 5) {
                    j = 0;
                    i++;
                }
            }
        }
    }
    
    // Find position of a character in the matrix
    static int[] findPosition(char ch) {
        int[] pos = new int[2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (keyTable[i][j] == ch) {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }
    
    // Find positions of two characters in the matrix (for traditional Playfair)
    static void findPosition(char a, char b, int[] pos) {
        if (a == 'j') a = 'i';
        if (b == 'j') b = 'i';
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (keyTable[i][j] == a) {
                    pos[0] = i;
                    pos[1] = j;
                }
                if (keyTable[i][j] == b) {
                    pos[2] = i;
                    pos[3] = j;
                }
            }
        }
    }
    
    // ========== TRADITIONAL PLAYFAIR METHODS ==========
    
    // Traditional encryption: Process pairs of letters
    static void encryptTraditional(StringBuilder text) {
        int[] pos = new int[4];
        for (int i = 0; i < text.length(); i += 2) {
            findPosition(text.charAt(i), text.charAt(i + 1), pos);
            if (pos[0] == pos[2]) {
                // Same row: pick next column (wrap around)
                text.setCharAt(i, keyTable[pos[0]][(pos[1] + 1) % 5]);
                text.setCharAt(i + 1, keyTable[pos[0]][(pos[3] + 1) % 5]);
            } else if (pos[1] == pos[3]) {
                // Same column: pick next row (wrap around)
                text.setCharAt(i, keyTable[(pos[0] + 1) % 5][pos[1]]);
                text.setCharAt(i + 1, keyTable[(pos[2] + 1) % 5][pos[1]]);
            } else {
                // Different row/col: take rectangle corners
                text.setCharAt(i, keyTable[pos[0]][pos[3]]);
                text.setCharAt(i + 1, keyTable[pos[2]][pos[1]]);
            }
        }
    }
    
    // Traditional decryption: Process pairs of letters
    static void decryptTraditional(StringBuilder text) {
        int[] pos = new int[4];
        for (int i = 0; i < text.length(); i += 2) {
            findPosition(text.charAt(i), text.charAt(i + 1), pos);
            if (pos[0] == pos[2]) {
                // Same row: pick previous column (wrap around)
                text.setCharAt(i, keyTable[pos[0]][(pos[1] + 4) % 5]);
                text.setCharAt(i + 1, keyTable[pos[0]][(pos[3] + 4) % 5]);
            } else if (pos[1] == pos[3]) {
                // Same column: pick previous row (wrap around)
                text.setCharAt(i, keyTable[(pos[0] + 4) % 5][pos[1]]);
                text.setCharAt(i + 1, keyTable[(pos[2] + 4) % 5][pos[1]]);
            } else {
                // Different row/col: take rectangle corners
                text.setCharAt(i, keyTable[pos[0]][pos[3]]);
                text.setCharAt(i + 1, keyTable[pos[2]][pos[1]]);
            }
        }
    }
    
    // ========== REVISED PLAYFAIR METHODS ==========
    
    // Revised encryption: Each letter produces 2-character block
    static String encryptLetterRevised(char letter) {
        if (letter == 'j') letter = 'i';
        
        int[] pos = findPosition(letter);
        int row = pos[0];
        int col = pos[1];
        
        char first, second;
        
        // Rule: Same Row - pick next column (wrap around) for both
        first = keyTable[row][(col + 1) % 5];
        second = keyTable[row][(col + 2) % 5];
        
        return String.valueOf(first) + second;
    }
    
    // Encrypt entire text using revised method
    static String encryptRevised(StringBuilder text) {
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            String encryptedBlock = encryptLetterRevised(letter);
            result.append(encryptedBlock);
        }
        
        return result.toString();
    }
    
    // Revised decryption: Each 2-char block produces 1 original letter
    static char decryptBlockRevised(String block) {
        if (block.length() != 2) {
            return 'x'; // Invalid block
        }
        
        char first = block.charAt(0);
        char second = block.charAt(1);
        
        // Find positions of both characters
        int[] pos1 = findPosition(first);
        int[] pos2 = findPosition(second);
        
        int row1 = pos1[0], col1 = pos1[1];
        int row2 = pos2[0], col2 = pos2[1];
        
        // Reverse the encryption logic
        // If both characters are in the same row, find the original position
        if (row1 == row2) {
            // Find which column would produce these two characters
            for (int col = 0; col < 5; col++) {
                char expectedFirst = keyTable[row1][(col + 1) % 5];
                char expectedSecond = keyTable[row1][(col + 2) % 5];
                
                if (expectedFirst == first && expectedSecond == second) {
                    return keyTable[row1][col];
                }
            }
        }
        
        // If not found, return first character's position (fallback)
        return keyTable[row1][col1];
    }
    
    // Decrypt entire text using revised method
    static String decryptRevised(String encryptedText) {
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < encryptedText.length(); i += 2) {
            if (i + 1 < encryptedText.length()) {
                String block = encryptedText.substring(i, i + 2);
                char decryptedLetter = decryptBlockRevised(block);
                result.append(decryptedLetter);
            }
        }
        
        return result.toString();
    }
    
    // Print matrix
    static void printKeyTable() {
        System.out.println("Key Table:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(keyTable[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    // Main driver
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("=== COMPLETE PLAYFAIR CIPHER ===");
        System.out.print("Enter keyword: ");
        StringBuilder key = new StringBuilder(sc.nextLine());
        
        System.out.print("Enter plaintext: ");
        StringBuilder text = new StringBuilder(sc.nextLine());
        
        // Preprocess input
        toLowerCase(key);
        toLowerCase(text);
        removeSpaces(key);
        removeSpaces(text);
        replaceJWithI(key);
        replaceJWithI(text);
        generateKeyTable(key);
        
        printKeyTable();
        System.out.println("Original text: " + text);
        
        // ========== TRADITIONAL PLAYFAIR ==========
        System.out.println("\n--- TRADITIONAL PLAYFAIR ---");
        StringBuilder textForTraditional = new StringBuilder(text.toString());
        prepareText(textForTraditional);
        
        StringBuilder encryptedTraditional = new StringBuilder(textForTraditional.toString());
        encryptTraditional(encryptedTraditional);
        System.out.println("Traditional Encrypted: " + encryptedTraditional);
        
        StringBuilder decryptedTraditional = new StringBuilder(encryptedTraditional.toString());
        decryptTraditional(decryptedTraditional);
        System.out.println("Traditional Decrypted: " + decryptedTraditional);
        
        // ========== REVISED PLAYFAIR ==========
        System.out.println("\n--- REVISED PLAYFAIR ---");
        String encryptedRevised = encryptRevised(text);
        System.out.println("Revised Encrypted: " + encryptedRevised);
        
        String decryptedRevised = decryptRevised(encryptedRevised);
        System.out.println("Revised Decrypted: " + decryptedRevised);
        
        sc.close();
    }
} 