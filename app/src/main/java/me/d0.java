package me;

import sd.Charsets;

/* compiled from: -JvmPlatform.kt */
/* loaded from: classes2.dex */
public final class d0 {
    public static final byte[] a(String str) {
        za.k.e(str, "<this>");
        byte[] bytes = str.getBytes(Charsets.f18469b);
        za.k.d(bytes, "this as java.lang.String).getBytes(charset)");
        return bytes;
    }

    public static final String b(byte[] bArr) {
        za.k.e(bArr, "<this>");
        return new String(bArr, Charsets.f18469b);
    }
}
