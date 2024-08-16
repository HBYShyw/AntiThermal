package j0;

import i0.EncryptException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import javax.crypto.KeyAgreement;
import s0.CryptoParameters;
import t0.InvalidArgumentException;

/* compiled from: EccUtil.java */
/* renamed from: j0.c, reason: use source file name */
/* loaded from: classes.dex */
public class EccUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final CryptoParameters.b f12931a = CryptoParameters.b.f17973e;

    public static byte[] a(PrivateKey privateKey, PublicKey publicKey) {
        try {
            if (privateKey != null && publicKey != null) {
                KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
                keyAgreement.init(privateKey);
                keyAgreement.doPhase(publicKey, true);
                return keyAgreement.generateSecret();
            }
            throw new InvalidArgumentException("privateKey is null or publicKey is null");
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    public static byte[] b(byte[] bArr, PrivateKey privateKey) {
        return c(bArr, privateKey, "SHA256withECDSA");
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
