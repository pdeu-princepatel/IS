
def modInverse(e, phi):
    for d in range(2, phi):
        if (e * d) % phi == 1:
            return d
    return -1


def generateKeys():
    p = 3
    q = 11

    n = p * q
    phi = (p - 1) * (q - 1)
    print(phi)
    e = 0
    for e in range(2, phi):
        if gcd(e, phi) == 1:
            break

    d = modInverse(e, phi)

    return e, d, n
def gcd(a, b):
    while b != 0:
        a, b = b, a % b
    return a
def encrypt(M,e,n):
    
    x = M ** e
    C=x%n
    return C

def decrypt(C,d,n):
    y =C ** d
    M =y%n
    return M

if __name__ == "__main__":
    
    e, d, n = generateKeys()
  
    print(f"Public Key (e, n): ({e}, {n})")
    print(f"Private Key (d, n): ({d}, {n})")

    M = 12

    print(f"Original Message: {M}")
    
    C = encrypt(M, e, n)
    print(f"Encrypted Message: {C}")

    decrypted = decrypt(C, d, n)
    print(f"Decrypted Message: {decrypted}")