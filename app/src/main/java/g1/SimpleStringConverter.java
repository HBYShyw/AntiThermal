package g1;

import android.util.Base64;

/* compiled from: SimpleStringConverter.java */
/* renamed from: g1.a, reason: use source file name */
/* loaded from: classes.dex */
public class SimpleStringConverter {
    public static String a() {
        return b("Y29tLm9wcG8ubWFya2V0");
    }

    public static String b(String str) {
        return new String(Base64.decode(str, 0));
    }

    public static String c() {
        return b("Y29tLmhleXRhcC5tYXJrZXQ=");
    }

    public static String d() {
        return b("Y29tLm5lYXJtZS5nYW1lY2VudGVy");
    }
}
