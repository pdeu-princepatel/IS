public class SimpleHillCipher {

    // Map A..Z -> 0..25
    static int charToNum(char c) {
        return c - 'A';
    }

    // Map 0..25 -> A..Z
    static char numToChar(int n) {
        return (char) (n + 'A');
    }

    // Proper modulo 26 that never returns negative
    static int mod26(int x) {
        x %= 26;
        if (x < 0) x += 26;
        return x;
    }

    // === Revised mode settings (A-Z, 0-9, space, punctuation) ===
    static final String REVISED_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .,!?";
    static final int REVISED_MOD = REVISED_CHARSET.length();

    static int modM(int x, int m) { x %= m; if (x < 0) x += m; return x; }
    static int rCharToNum(char c) { return REVISED_CHARSET.indexOf(c); }
    static char rNumToChar(int n) { return REVISED_CHARSET.charAt(n); }
    static int invMod(int a, int m) { a = modM(a, m); for (int x = 1; x < m; x++) { if (modM(a * x, m) == 1) return x; } return -1; }

    // Determinant of 2x2 matrix [[a,b],[c,d]] modulo 26
    static int det2x2(int[][] k) {
        int a = k[0][0], b = k[0][1], c = k[1][0], d = k[1][1];
        return mod26(a * d - b * c);
    }

    // Brute-force modular inverse of a (mod 26); returns -1 if none
    static int invMod26(int a) {
        a = mod26(a);
        for (int x = 1; x < 26; x++) {
            if (mod26(a * x) == 1) return x;
        }
        return -1;
    }

    // Inverse of 2x2 matrix modulo 26. Throws if not invertible.
    static int[][] inverse2x2(int[][] k) {
        int det = det2x2(k);
        int detInv = invMod26(det);
        if (detInv == -1) {
            throw new IllegalArgumentException("Key matrix is not invertible mod 26 (det=" + det + ")");
        }
        int a = k[0][0], b = k[0][1], c = k[1][0], d = k[1][1];
        int[][] adj = new int[2][2];
        adj[0][0] = d;    adj[0][1] = -b;
        adj[1][0] = -c;   adj[1][1] = a;
        int[][] inv = new int[2][2];
        inv[0][0] = mod26(adj[0][0] * detInv);
        inv[0][1] = mod26(adj[0][1] * detInv);
        inv[1][0] = mod26(adj[1][0] * detInv);
        inv[1][1] = mod26(adj[1][1] * detInv);
        return inv;
    }

    // Inverse of 2x2 matrix modulo arbitrary m (for revised mode)
    static int[][] inverse2x2Mod(int[][] k, int m) {
        int a = k[0][0], b = k[0][1], c = k[1][0], d = k[1][1];
        int det = modM(a * d - b * c, m);
        int detInv = invMod(det, m);
        if (detInv == -1) throw new IllegalArgumentException("Key not invertible mod " + m + " (det=" + det + ")");
        int[][] adj = new int[2][2];
        adj[0][0] = d;    adj[0][1] = -b;
        adj[1][0] = -c;   adj[1][1] = a;
        int[][] inv = new int[2][2];
        inv[0][0] = modM(adj[0][0] * detInv, m);
        inv[0][1] = modM(adj[0][1] * detInv, m);
        inv[1][0] = modM(adj[1][0] * detInv, m);
        inv[1][1] = modM(adj[1][1] * detInv, m);
        return inv;
    }

    // Multiply 2x2 matrix by 2x1 vector modulo 26
    static int[] mul2x2Vec(int[][] k, int[] v) {
        int[] r = new int[2];
        r[0] = mod26(k[0][0] * v[0] + k[0][1] * v[1]);
        r[1] = mod26(k[1][0] * v[0] + k[1][1] * v[1]);
        return r;
    }

    // Multiply 2x2 matrix by 2x1 vector modulo m (revised)
    static int[] mul2x2VecMod(int[][] k, int[] v, int m) {
        int[] r = new int[2];
        r[0] = modM(k[0][0] * v[0] + k[0][1] * v[1], m);
        r[1] = modM(k[1][0] * v[0] + k[1][1] * v[1], m);
        return r;
    }

    // Keep only A..Z, convert to uppercase, and pad with 'X' to even length
    static String cleanAndPad(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= 'a' && ch <= 'z') ch = (char) (ch - 32);
            if (ch >= 'A' && ch <= 'Z') sb.append(ch);
        }
        if (sb.length() % 2 == 1) sb.append('X');
        return sb.toString();
    }

    // Revised: keep only chars from REVISED_CHARSET (uppercase where applicable). Pad with space
    static String revisedCleanAndPad(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= 'a' && ch <= 'z') ch = (char) (ch - 32);
            if (REVISED_CHARSET.indexOf(ch) >= 0) sb.append(ch); else sb.append(' ');
        }
        if (sb.length() % 2 == 1) sb.append(' ');
        return sb.toString();
    }

    // Encrypt plaintext using 2x2 key (normal)
    static String encrypt(String plaintext, int[][] key) {
        String p = cleanAndPad(plaintext);
        StringBuilder c = new StringBuilder();
        for (int i = 0; i < p.length(); i += 2) {
            int[] block = new int[2];
            block[0] = charToNum(p.charAt(i));
            block[1] = charToNum(p.charAt(i + 1));
            int[] enc = mul2x2Vec(key, block);
            c.append(numToChar(enc[0]));
            c.append(numToChar(enc[1]));
        }
        return c.toString();
    }

    // Decrypt ciphertext using 2x2 key (normal)
    static String decrypt(String ciphertext, int[][] key) {
        int[][] inv = inverse2x2(key);
        StringBuilder p = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            int[] block = new int[2];
            block[0] = charToNum(ciphertext.charAt(i));
            block[1] = charToNum(ciphertext.charAt(i + 1));
            int[] dec = mul2x2Vec(inv, block);
            p.append(numToChar(dec[0]));
            p.append(numToChar(dec[1]));
        }
        return p.toString();
    }

    // Encrypt (revised 2x2): dynamic key per block + Caesar-style post-shift
    static String encryptRevised(String plaintext, int[][] key) {
        String p = revisedCleanAndPad(plaintext);
        StringBuilder c = new StringBuilder();
        for (int i = 0; i < p.length(); i += 2) {
            int[] block = new int[2];
            block[0] = rCharToNum(p.charAt(i));
            block[1] = rCharToNum(p.charAt(i + 1));
            int blockIndex = i / 2;
            int s = 1 + (blockIndex % (REVISED_MOD - 1));
            int[][] dyn = new int[2][2];
            dyn[0][0] = modM(key[0][0] * s, REVISED_MOD);
            dyn[0][1] = modM(key[0][1] * s, REVISED_MOD);
            dyn[1][0] = modM(key[1][0] * s, REVISED_MOD);
            dyn[1][1] = modM(key[1][1] * s, REVISED_MOD);
            int[] enc = mul2x2VecMod(dyn, block, REVISED_MOD);
            int sum = (block[0] + block[1]) % REVISED_MOD;
            int shift = modM(sum + blockIndex, REVISED_MOD);
            enc[0] = modM(enc[0] + shift, REVISED_MOD);
            enc[1] = modM(enc[1] + shift, REVISED_MOD);
            c.append(rNumToChar(enc[0]));
            c.append(rNumToChar(enc[1]));
        }
        return c.toString();
    }

    // Decrypt (revised 2x2): always brute-force shift for correctness and simplicity
    static String decryptRevised(String ciphertext, int[][] key) {
        StringBuilder p = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            int[] y = new int[2];
            y[0] = rCharToNum(ciphertext.charAt(i));
            y[1] = rCharToNum(ciphertext.charAt(i + 1));
            int blockIndex = i / 2;
            int s = 1 + (blockIndex % (REVISED_MOD - 1));
            int[][] dyn = new int[2][2];
            dyn[0][0] = modM(key[0][0] * s, REVISED_MOD);
            dyn[0][1] = modM(key[0][1] * s, REVISED_MOD);
            dyn[1][0] = modM(key[1][0] * s, REVISED_MOD);
            dyn[1][1] = modM(key[1][1] * s, REVISED_MOD);
            int[][] invDyn = inverse2x2Mod(dyn, REVISED_MOD);

            int[] chosen = null;
            for (int shift = 0; shift < REVISED_MOD; shift++) {
                int[] temp = new int[2];
                temp[0] = modM(y[0] - shift, REVISED_MOD);
                temp[1] = modM(y[1] - shift, REVISED_MOD);
                int[] cand = mul2x2VecMod(invDyn, temp, REVISED_MOD);
                int sum = (cand[0] + cand[1]) % REVISED_MOD;
                int expected = modM(sum + blockIndex, REVISED_MOD);
                if (expected == shift) { chosen = cand; break; }
            }
            if (chosen == null) { chosen = mul2x2VecMod(invDyn, y, REVISED_MOD); }
            p.append(rNumToChar(chosen[0]));
            p.append(rNumToChar(chosen[1]));
        }
        return p.toString();
    }

    // Helper to print a 2x2 matrix
    static void print2x2(int[][] m) {
        System.out.println("[" + mod26(m[0][0]) + " " + mod26(m[0][1]) + "]");
        System.out.println("[" + mod26(m[1][0]) + " " + mod26(m[1][1]) + "]");
    }

    // Read a single letter token from scanner and validate A..Z
    static char readKeyChar(java.util.Scanner sc) {
        while (true) {
            String tok = sc.next().trim();
            if (tok.isEmpty()) continue;
            char ch = tok.charAt(0);
            if (ch >= 'a' && ch <= 'z') ch = (char)(ch - 32);
            if (ch >= 'A' && ch <= 'Z') return ch;
            System.out.print("Please enter a letter A..Z: ");
        }
    }

    // Read a single character that must exist in REVISED_CHARSET
    static char readKeyCharFromSet(java.util.Scanner sc) {
        while (true) {
            String tok = sc.next().trim();
            if (tok.isEmpty()) continue;
            char ch = tok.charAt(0);
            if (ch >= 'a' && ch <= 'z') ch = (char)(ch - 32);
            if (REVISED_CHARSET.indexOf(ch) >= 0) return ch;
            System.out.print("Please enter a character from the set: " + REVISED_CHARSET + " : ");
        }
    }

    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);

        System.out.println("=== Simple Hill Cipher (2x2) ===");
        System.out.println("Choose mode:");
        System.out.println("1) Normal (A..Z, mod 26)");
        System.out.println("2) Revised (A..Z, 0-9, space, . , ! ?, mod " + REVISED_MOD + ")");
        System.out.print("Enter 1 or 2: ");
        int mode = sc.nextInt();

        if (mode == 1) {
            System.out.println("\n-- Normal Mode --");
            System.out.println("Choose key option:");
            System.out.println("1) Use default key [[D, D], [C, F]] (numbers [[3,3],[2,5]])");
            System.out.println("2) Enter custom key letters (A..Z)");
            System.out.print("Enter 1 or 2: ");
            int choice = sc.nextInt();

            int[][] key = new int[2][2];
            if (choice == 1) {
                key[0][0] = 3; key[0][1] = 3; key[1][0] = 2; key[1][1] = 5;
                System.out.println("Using default key [[D, D], [C, F]]");
            } else {
                System.out.println("Enter key matrix LETTERS (A..Z) for 2x2 matrix:");
                System.out.print("a (row 0 col 0): "); key[0][0] = charToNum(readKeyChar(sc));
                System.out.print("b (row 0 col 1): "); key[0][1] = charToNum(readKeyChar(sc));
                System.out.print("c (row 1 col 0): "); key[1][0] = charToNum(readKeyChar(sc));
                System.out.print("d (row 1 col 1): "); key[1][1] = charToNum(readKeyChar(sc));
            }
            key[0][0] = mod26(key[0][0]); key[0][1] = mod26(key[0][1]);
            key[1][0] = mod26(key[1][0]); key[1][1] = mod26(key[1][1]);

            System.out.println("\nKey (mod 26):");
            print2x2(key);
            int det = det2x2(key); int detInv = invMod26(det);
            System.out.println("det(key) mod 26 = " + det);
            if (detInv == -1) { System.out.println("This key is NOT invertible modulo 26. Choose a different key."); sc.close(); return; }
            else System.out.println("det(key)^{-1} mod 26 = " + detInv);

            sc.nextLine();
            System.out.print("\nEnter plaintext (letters only are kept): ");
            String plaintext = sc.nextLine();
            String ciphertext = encrypt(plaintext, key);
            System.out.println("\nCiphertext: " + ciphertext);
            String decrypted = decrypt(ciphertext, key);
            System.out.println("Decrypted:  " + decrypted);
            sc.close();
        } else {
            System.out.println("\n-- Revised Mode --");
            System.out.println("Character set: " + REVISED_CHARSET);
            System.out.println("Choose key option:");
            System.out.println("1) Use default key [[1,2],[3,5]] (invertible mod " + REVISED_MOD + ")");
            System.out.println("2) Enter custom key characters from the set above");
            System.out.print("Enter 1 or 2: ");
            int choice = sc.nextInt();

            int[][] key = new int[2][2];
            if (choice == 1) {
                key[0][0] = 1; key[0][1] = 2; key[1][0] = 3; key[1][1] = 5;
                System.out.println("Using default key [[1, 2], [3, 5]]");
            } else {
                System.out.println("Enter key matrix CHARACTERS from the set above:");
                System.out.print("a (row 0 col 0): "); key[0][0] = rCharToNum(readKeyCharFromSet(sc));
                System.out.print("b (row 0 col 1): "); key[0][1] = rCharToNum(readKeyCharFromSet(sc));
                System.out.print("c (row 1 col 0): "); key[1][0] = rCharToNum(readKeyCharFromSet(sc));
                System.out.print("d (row 1 col 1): "); key[1][1] = rCharToNum(readKeyCharFromSet(sc));
            }
            key[0][0] = modM(key[0][0], REVISED_MOD); key[0][1] = modM(key[0][1], REVISED_MOD);
            key[1][0] = modM(key[1][0], REVISED_MOD); key[1][1] = modM(key[1][1], REVISED_MOD);

            int a = key[0][0], b = key[0][1], c = key[1][0], d = key[1][1];
            int det = modM(a * d - b * c, REVISED_MOD);
            int detInv = invMod(det, REVISED_MOD);
            System.out.println("\nKey (indices mod " + REVISED_MOD + "): [" + a + " " + b + "] [" + c + " " + d + "]");
            System.out.println("det(key) mod " + REVISED_MOD + " = " + det);
            if (detInv == -1) { System.out.println("This key is NOT invertible modulo " + REVISED_MOD + ". Choose a different key."); sc.close(); return; }
            else System.out.println("det(key)^{-1} mod " + REVISED_MOD + " = " + detInv);

            sc.nextLine();
            System.out.print("\nEnter plaintext (unsupported chars become space): ");
            String plaintext = sc.nextLine();
            String ciphertext = encryptRevised(plaintext, key);
            System.out.println("\nCiphertext: " + ciphertext);
            String decrypted = decryptRevised(ciphertext, key);
            System.out.println("Decrypted:  " + decrypted);
            sc.close();
        }
    }
}
