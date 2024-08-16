package sd;

import java.nio.charset.Charset;

/* compiled from: Charsets.kt */
/* renamed from: sd.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class Charsets {

    /* renamed from: a, reason: collision with root package name */
    public static final Charsets f18468a = new Charsets();

    /* renamed from: b, reason: collision with root package name */
    public static final Charset f18469b;

    /* renamed from: c, reason: collision with root package name */
    public static final Charset f18470c;

    /* renamed from: d, reason: collision with root package name */
    public static final Charset f18471d;

    /* renamed from: e, reason: collision with root package name */
    public static final Charset f18472e;

    /* renamed from: f, reason: collision with root package name */
    public static final Charset f18473f;

    /* renamed from: g, reason: collision with root package name */
    public static final Charset f18474g;

    /* renamed from: h, reason: collision with root package name */
    private static volatile Charset f18475h;

    /* renamed from: i, reason: collision with root package name */
    private static volatile Charset f18476i;

    static {
        Charset forName = Charset.forName("UTF-8");
        za.k.d(forName, "forName(\"UTF-8\")");
        f18469b = forName;
        Charset forName2 = Charset.forName("UTF-16");
        za.k.d(forName2, "forName(\"UTF-16\")");
        f18470c = forName2;
        Charset forName3 = Charset.forName("UTF-16BE");
        za.k.d(forName3, "forName(\"UTF-16BE\")");
        f18471d = forName3;
        Charset forName4 = Charset.forName("UTF-16LE");
        za.k.d(forName4, "forName(\"UTF-16LE\")");
        f18472e = forName4;
        Charset forName5 = Charset.forName("US-ASCII");
        za.k.d(forName5, "forName(\"US-ASCII\")");
        f18473f = forName5;
        Charset forName6 = Charset.forName("ISO-8859-1");
        za.k.d(forName6, "forName(\"ISO-8859-1\")");
        f18474g = forName6;
    }

    private Charsets() {
    }

    public final Charset a() {
        Charset charset = f18476i;
        if (charset != null) {
            return charset;
        }
        Charset forName = Charset.forName("UTF-32BE");
        za.k.d(forName, "forName(\"UTF-32BE\")");
        f18476i = forName;
        return forName;
    }

    public final Charset b() {
        Charset charset = f18475h;
        if (charset != null) {
            return charset;
        }
        Charset forName = Charset.forName("UTF-32LE");
        za.k.d(forName, "forName(\"UTF-32LE\")");
        f18475h = forName;
        return forName;
    }
}
