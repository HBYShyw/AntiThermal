package j0;

import i0.EncryptException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import t0.InvalidArgumentException;

/* compiled from: RsaUtil.java */
/* renamed from: j0.g, reason: use source file name */
/* loaded from: classes.dex */
public class RsaUtil {
    public static byte[] a(byte[] bArr, PublicKey publicKey) {
        try {
            if (bArr == null) {
                throw new InvalidArgumentException("plain is null");
            }
            if (publicKey != null) {
                Cipher cipher = Cipher.getInstance("RSA/None/OAEPPadding");
                cipher.init(1, publicKey);
                try {
                    return cipher.doFinal(bArr);
                } catch (IllegalBlockSizeException unused) {
                    throw new IllegalBlockSizeException("Data is too large for key size, input must be under " + ((((RSAPublicKey) publicKey).getModulus().bitLength() / 8) - 41) + " bytes.");
                }
            }
            throw new InvalidArgumentException("publicKey is null");
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    public static byte[] b(byte[] bArr, PrivateKey privateKey) {
        return c(bArr, privateKey, "SHA256withRSA/PSS");
    }

    private static byte[] c(byte[] bArr, PrivateKey privateKey, String str) {
        try {
            if (bArr == null) {
                throw new InvalidArgumentException("data is null");
            }
            if (privateKey != null) {
                Signature signature = Signature.getInstance(str);
                signature.initSign(privateKey);
                signature.update(bArr);
                return signature.sign();
            }
            throw new InvalidArgumentException("privateKey is null");
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }
}
