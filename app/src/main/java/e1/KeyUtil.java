package e1;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import t0.InvalidArgumentException;

/* compiled from: KeyUtil.java */
/* renamed from: e1.h, reason: use source file name */
/* loaded from: classes.dex */
public class KeyUtil {
    public static PrivateKey a(byte[] bArr, String str) {
        if (bArr == null) {
            throw new InvalidArgumentException("privateKeyBytes is null");
        }
        if (str != null) {
            return KeyFactory.getInstance(str).generatePrivate(new PKCS8EncodedKeySpec(bArr));
        }
        throw new InvalidArgumentException("algorithm is null");
    }

    public static PublicKey b(byte[] bArr, String str) {
        if (bArr == null) {
            throw new InvalidArgumentException("publicKeyBytes is null");
        }
        if (str != null) {
            return KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(bArr));
        }
        throw new InvalidArgumentException("algorithm is null");
    }

    public static SecretKey c(byte[] bArr, String str) {
        if (bArr == null) {
            throw new InvalidArgumentException("secretKeyBytes is null");
        }
        if (str != null) {
            return new SecretKeySpec(bArr, str);
        }
        throw new InvalidArgumentException("algorithm is null");
    }

    public static SecretKey d(int i10) {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(i10);
        return keyGenerator.generateKey();
    }

    public static KeyPair e(String str) {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(new ECGenParameterSpec(str), new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }
}
