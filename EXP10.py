# plaintext = "GRONSFELD"
# key = 1234
# ciphertext = "HTRRTHHPEFGH"

def encrypt(p,k):
    listP =list(p)
    last=listP[-1]
    listK =list(k)
    Cipher=""
    lp = len(listP)
    lk =len(listK)
    if(lp % lk != 0):
        rep=lk - (lp % lk)
        for i in range(0,rep):
            listP.append(last)
    print("after padding:")
    print(listP)
    for i in range(0,lp):
       Cipher = Cipher + (chr)(ord(listP[i])+ int(listK[i % len(k)]))
    return Cipher

def encryption(p):
    listw = p.split()
    wordlen = len(p.split())
    print(wordlen)
    cipher =""
    for i in range(wordlen):
        for j in range(len(listw[i])):
            encrypted_char = chr((i + j) + ord(p[j]))
            print(encrypted_char)
            cipher += encrypted_char
            
    print(cipher)
    return cipher

if __name__ == "__main__":    
    # plaintext = input("Enter your plaintext:")
    # key = input("Enter your key:")
    plaintext = "GRONSFELD"
    key = "1234"    
    ciphertext = encrypt(plaintext,key)
    print(f"Key: {key}")
    print(f"Ciphertext: {ciphertext}")
    PLAIN = "RAG BABY"
    # CIPHER = "SCJ DDFD"
    CIPHER = encryption(PLAIN)
    print(f"Ciphertext: {CIPHER}")