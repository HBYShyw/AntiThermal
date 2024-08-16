package e1;

import java.util.Formatter;

/* compiled from: HexStringUtils.java */
/* renamed from: e1.f, reason: use source file name */
/* loaded from: classes.dex */
public class HexStringUtils {
    public static String a(byte[] bArr) {
        StringBuilder sb2 = new StringBuilder(bArr.length * 2);
        Formatter formatter = new Formatter(sb2);
        for (byte b10 : bArr) {
            formatter.format("%02x", Byte.valueOf(b10));
        }
        return sb2.toString();
    }

    public static byte[] b(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i10 = 0; i10 < length; i10 += 2) {
            bArr[i10 / 2] = (byte) ((Character.digit(str.charAt(i10), 16) << 4) + Character.digit(str.charAt(i10 + 1), 16));
        }
        return bArr;
    }
}
