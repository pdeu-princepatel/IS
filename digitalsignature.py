import hashlib

def SHA1(message):
   
    message_string = str(message)
    return int(hashlib.sha1(message_string.encode()).hexdigest(), 16)

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
    print(f"Phi(n): {phi}")
    
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

def sender_A(message, private_key, n):
    
    message_digest = SHA1(message)
    signature = pow(message_digest, private_key, n)
    return signature , message

def sender_B(digital_signature, message, public_key, n):
    received_digest = SHA1(message)
    print("message_digest:",received_digest)
    
    decrypted_digest = pow(digital_signature, public_key, n)
    print(decrypted_digest)
    return received_digest == decrypted_digest

if __name__ == "__main__":
   
    e, d, n = generateKeys()
    print(f"\nPublic Key (e, n): ({e}, {n})")
    print(f"Private Key (d, n): ({d}, {n})")

    M = 15
    print(f"\nOriginal Message: {M}")

    sign , mess = sender_A(M, d, n)
    print("Sender A output")
    print("digital_signature:",sign)
    print("Message:",mess)
    
    print("inside sender B")
    is_authentic = sender_B(sign, mess, e, n)

    if is_authentic:
        print("Message is authentic.")
    else:
        print("Message is not authentic.")

