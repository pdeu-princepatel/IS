import java.util.*;
public class Columnarcipher {
    
    public static String encryptStandard(String text, String key) {
        int col = key.length();
        int row = (int) Math.ceil((double) text.length() / col);

        char[][] matrix = new char[row][col];
        int k = 0;

        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (k < text.length()) {
                    matrix[i][j] = text.charAt(k++);
                } else {
                    matrix[i][j] = 'X'; // padding
                }
            }
        }

      
        Integer[] order = getKeyOrder(key);

        
        StringBuilder cipher = new StringBuilder();
        for (int idx : order) {
            for (int i = 0; i < row; i++) {
                cipher.append(matrix[i][idx]);
            }
        }
        return cipher.toString();
    }

    public static String decryptStandard(String cipher, String key) {
        int col = key.length();
        int row = (int) Math.ceil((double) cipher.length() / col);

        char[][] matrix = new char[row][col];

        Integer[] order = getKeyOrder(key);

        int k = 0;
       
        for (int idx : order) {
            for (int i = 0; i < row; i++) {
                if (k < cipher.length()) {
                    matrix[i][idx] = cipher.charAt(k++);
                }
            }
        }

      
        StringBuilder plain = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                plain.append(matrix[i][j]);
            }
        }
        return plain.toString().replaceAll("X+$", ""); // remove padding
    }

    
    public static String encryptRevised(String text, String key) {
        int col = key.length();
        int row = (int) Math.ceil((double) text.length() / col);

        char[][] matrix = new char[row][col];
        int k = 0;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (k < text.length()) {
                    matrix[i][j] = text.charAt(k++);
                } else {
                    matrix[i][j] = 'X';
                }
            }
        }

       
        for (int i = 0; i < row; i++) {
            matrix[i] = shiftRow(matrix[i], i + 1);
        }

       
        Integer[] order = getKeyOrder(key);

        StringBuilder cipher = new StringBuilder();
        for (int idx : order) {
            for (int i = 0; i < row; i++) {
                cipher.append(matrix[i][idx]);
            }
        }
        return cipher.toString();
    }

    public static String decryptRevised(String cipher, String key) {
        int col = key.length();
        int row = (int) Math.ceil((double) cipher.length() / col);

        char[][] matrix = new char[row][col];
        Integer[] order = getKeyOrder(key);

        int k = 0;
       
        for (int idx : order) {
            for (int i = 0; i < row; i++) {
                if (k < cipher.length()) {
                    matrix[i][idx] = cipher.charAt(k++);
                }
            }
        }

       
        for (int i = 0; i < row; i++) {
            matrix[i] = shiftRow(matrix[i], col - (i + 1)); 
        }

        StringBuilder plain = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                plain.append(matrix[i][j]);
            }
        }
        return plain.toString().replaceAll("X+$", "");
    }
    private static Integer[] getKeyOrder(String key) {
        Character[] chars = new Character[key.length()];
        for (int i = 0; i < key.length(); i++) {
            chars[i] = key.charAt(i);
        }
        Integer[] order = new Integer[key.length()];
        for (int i = 0; i < key.length(); i++) order[i] = i;

        Arrays.sort(order, Comparator.comparingInt(i -> chars[i]));
        return order;
    }

    private static char[] shiftRow(char[] row, int shift) {
        int n = row.length;
        char[] newRow = new char[n];
        for (int i = 0; i < n; i++) {
            newRow[(i + shift) % n] = row[i];
        }
        return newRow;
    }

    public static void main(String[] args) {
        String text = "HELLOWORLD";
        String key = "SECRET";

        System.out.println("=== Standard Columnar Cipher ===");
        String encStd = encryptStandard(text, key);
        String decStd = decryptStandard(encStd, key);
        System.out.println("Plaintext:  " + text);
        System.out.println("Encrypted:  " + encStd);
        System.out.println("Decrypted:  " + decStd);

        System.out.println("\n=== Revised Columnar Cipher ===");
        String encRev = encryptRevised(text, key);
        String decRev = decryptRevised(encRev, key);
        System.out.println("Plaintext:  " + text);
        System.out.println("Encrypted:  " + encRev);
        System.out.println("Decrypted:  " + decRev);
    }
}


