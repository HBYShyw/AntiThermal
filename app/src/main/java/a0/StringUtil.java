package a0;

/* compiled from: StringUtil.java */
/* renamed from: a0.c, reason: use source file name */
/* loaded from: classes.dex */
public class StringUtil {

    /* renamed from: a, reason: collision with root package name */
    public static final String[] f3a = new String[0];

    public static void a(StringBuilder sb2, int i10) {
        for (int i11 = 0; i11 < i10; i11++) {
            sb2.append("?");
            if (i11 < i10 - 1) {
                sb2.append(",");
            }
        }
    }

    public static StringBuilder b() {
        return new StringBuilder();
    }
}
