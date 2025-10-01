import random

def R1(g, x, p):
    return (g ** x) % p

def R2(g, y, p):
    return (g ** y) % p

def Kalice(R2, x, p):
    return (R2 ** x) % p

def Kbob(R1, y, p):
    return (R1 ** y) % p

def comp(K1, K2):
    return (K1 == K2)

def unqxy(P):
    x = random.randrange(P)
    y = random.randrange(P)
    return x, y

if __name__ == '__main__':

    P = 19
    G = 3

    X, Y = unqxy(P)

    r1 = R1(G, X, P)
    print(f"Alice's private key (X): {X}")
    print(f"Alice's public key (r1): {r1}")

    r2 = R2(G, Y, P)
    print(f"Bob's private key (Y): {Y}")
    print(f"Bob's public key (r2): {r2}")

    K1 = Kalice(r2, X, P)
    print(f"Alice's shared secret (K1): {K1}")
  
    K2 = Kbob(r1, Y, P)
    print(f"Bob's shared secret (K2): {K2}")
    print(f"Do the shared secrets match? {comp(K1, K2)}")

