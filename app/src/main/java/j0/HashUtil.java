package j0;

import e1.Base64Utils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* compiled from: HashUtil.java */
/* renamed from: j0.d, reason: use source file name */
/* loaded from: classes.dex */
public class HashUtil {
    private static byte[] a(String str, byte[]... bArr) {
        MessageDigest messageDigest = MessageDigest.getInstance(str);
        for (byte[] bArr2 : bArr) {
            messageDigest.update(bArr2);
        }
        return messageDigest.digest();
    }

    public static String b(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Base64Utils.b(c(str.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e10) {
            e10.printStackTrace();
            return null;
        }
    }

    public static byte[] c(byte[]... bArr) {
        return a("SHA-256", bArr);
    }
}
