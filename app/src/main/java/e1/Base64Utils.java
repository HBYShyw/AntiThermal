package e1;

import android.util.Base64;

/* compiled from: Base64Utils.java */
/* renamed from: e1.a, reason: use source file name */
/* loaded from: classes.dex */
public class Base64Utils {
    public static byte[] a(String str) {
        return Base64.decode(str, 2);
    }

    public static String b(byte[] bArr) {
        return Base64.encodeToString(bArr, 2);
    }
}
