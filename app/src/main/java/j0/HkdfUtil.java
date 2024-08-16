package j0;

import i0.EncryptException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import t0.InvalidArgumentException;

/* compiled from: HkdfUtil.java */
/* renamed from: j0.e, reason: use source file name */
/* loaded from: classes.dex */
public class HkdfUtil {
    private static byte[] a(byte[] bArr, byte[] bArr2, byte[] bArr3, int i10, String str) {
        try {
            if (bArr3 != null) {
                Mac mac = Mac.getInstance(str);
                if (i10 <= mac.getMacLength() * 255 && i10 >= 0) {
                    if (bArr2 == null || bArr2.length == 0) {
                        bArr2 = new byte[mac.getMacLength()];
                    }
                    mac.init(new SecretKeySpec(bArr2, str));
                    byte[] bArr4 = new byte[i10];
                    mac.init(new SecretKeySpec(mac.doFinal(bArr), str));
                    byte[] bArr5 = new byte[0];
                    int i11 = 1;
                    int i12 = 0;
                    while (true) {
                        mac.update(bArr5);
                        mac.update(bArr3);
                        mac.update((byte) i11);
                        bArr5 = mac.doFinal();
                        if (bArr5.length + i12 < i10) {
                            System.arraycopy(bArr5, 0, bArr4, i12, bArr5.length);
                            i12 += bArr5.length;
                            i11++;
                        } else {
                            System.arraycopy(bArr5, 0, bArr4, i12, i10 - i12);
                            return bArr4;
                        }
                    }
                } else {
                    throw new InvalidArgumentException("keyLen is too large or negative");
                }
            } else {
                throw new InvalidArgumentException("info is null");
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    public static byte[] b(byte[] bArr, byte[] bArr2, byte[] bArr3, int i10) {
        return a(bArr, bArr2, bArr3, i10, "HmacSHA256");
    }
}
