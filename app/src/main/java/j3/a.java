package j3;

/* compiled from: COUICompatUtil.java */
/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private static volatile a f12938a;

    /* renamed from: b, reason: collision with root package name */
    private static final char[] f12939b = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.'};

    /* renamed from: c, reason: collision with root package name */
    private static final int[] f12940c = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 8, 13, 13, 4, 17, 52, 21, 8, 4, 22, 52, 47, 8, 4, 22, 48, 17, 0, 15, 15, 4, 17};

    /* renamed from: d, reason: collision with root package name */
    private static final int[] f12941d = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 14, 18, 52, 28, 14, 11, 14, 17, 27, 20, 8, 11, 3};

    /* renamed from: e, reason: collision with root package name */
    private static final int[] f12942e = {6, 4, 19, 28, 14, 11, 14, 17, 40, 44, 47, 30, 43, 44, 34, 40, 39};

    /* renamed from: f, reason: collision with root package name */
    private static final int[] f12943f = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 8, 13, 13, 4, 17, 52, 2, 14, 13, 19, 4, 13, 19, 52, 17, 4, 18, 52, 28, 14, 13, 5, 8, 6, 20, 17, 0, 19, 8, 14, 13, 48, 17, 0, 15, 15, 4, 17};

    /* renamed from: g, reason: collision with root package name */
    private static final int[] f12944g = {0, 13, 3, 17, 14, 8, 3, 52, 21, 8, 4, 22, 52, 40, 15, 15, 14, 27, 0, 18, 4, 47, 8, 4, 22};

    /* renamed from: h, reason: collision with root package name */
    private static final int[] f12945h = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 2, 11, 8, 2, 10, 19, 14, 15};

    /* renamed from: i, reason: collision with root package name */
    private static final int[] f12946i = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 8, 13, 13, 4, 17, 52, 22, 8, 3, 6, 4, 19, 52, 26, 1, 18, 37, 8, 18, 19, 47, 8, 4, 22, 48, 17, 0, 15, 15, 4, 17};

    /* renamed from: j, reason: collision with root package name */
    private static final int[] f12947j = {17, 14, 52, 14, 15, 15, 14, 52, 19, 7, 4, 12, 4, 52, 21, 4, 17, 18, 8, 14, 13};

    private a() {
    }

    public static a c() {
        if (f12938a == null) {
            synchronized (a.class) {
                if (f12938a == null) {
                    f12938a = new a();
                }
            }
        }
        return f12938a;
    }

    public String a() {
        int length = f12946i.length;
        char[] cArr = new char[length];
        for (int i10 = 0; i10 < length; i10++) {
            cArr[i10] = f12939b[f12946i[i10]];
        }
        return String.valueOf(cArr);
    }

    public String b() {
        int length = f12943f.length;
        char[] cArr = new char[length];
        for (int i10 = 0; i10 < length; i10++) {
            cArr[i10] = f12939b[f12943f[i10]];
        }
        return String.valueOf(cArr);
    }

    public String d() {
        int length = f12941d.length;
        char[] cArr = new char[length];
        for (int i10 = 0; i10 < length; i10++) {
            cArr[i10] = f12939b[f12941d[i10]];
        }
        return String.valueOf(cArr);
    }

    public String e() {
        int length = f12942e.length;
        char[] cArr = new char[length];
        for (int i10 = 0; i10 < length; i10++) {
            cArr[i10] = f12939b[f12942e[i10]];
        }
        return String.valueOf(cArr);
    }

    public String f() {
        int length = f12947j.length;
        char[] cArr = new char[length];
        for (int i10 = 0; i10 < length; i10++) {
            cArr[i10] = f12939b[f12947j[i10]];
        }
        return String.valueOf(cArr);
    }
}
