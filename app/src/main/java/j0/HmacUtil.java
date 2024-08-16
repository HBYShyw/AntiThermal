package j0;

import i0.EncryptException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import t0.InvalidArgumentException;

/* compiled from: HmacUtil.java */
/* renamed from: j0.f, reason: use source file name */
/* loaded from: classes.dex */
public class HmacUtil {
    private static byte[] a(byte[] bArr, byte[] bArr2, String str) {
        if (bArr2 != null) {
            try {
                if (bArr2.length != 0) {
                    Mac mac = Mac.getInstance(str);
                    mac.init(new SecretKeySpec(bArr2, str));
                    return mac.doFinal(bArr);
                }
            } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidArgumentException e10) {
                throw new EncryptException(e10);
            }
        }
        throw new InvalidArgumentException("key is null or empty");
    }

    public static byte[] b(byte[] bArr, byte[] bArr2) {
        return a(bArr, bArr2, "HmacSHA256");
    }
}
