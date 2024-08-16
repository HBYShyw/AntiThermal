package l3;

/* compiled from: COUICompatUtil.java */
/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private static volatile a f14580a;

    /* renamed from: b, reason: collision with root package name */
    private static final char[] f14581b = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.'};

    /* renamed from: c, reason: collision with root package name */
    private static final int[] f14582c = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 8, 13, 13, 4, 17, 52, 21, 8, 4, 22, 52, 47, 8, 4, 22, 48, 17, 0, 15, 15, 4, 17};

    /* renamed from: d, reason: collision with root package name */
    private static final int[] f14583d = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 14, 18, 52, 28, 14, 11, 14, 17, 27, 20, 8, 11, 3};

    /* renamed from: e, reason: collision with root package name */
    private static final int[] f14584e = {6, 4, 19, 28, 14, 11, 14, 17, 40, 44, 47, 30, 43, 44, 34, 40, 39};

    /* renamed from: f, reason: collision with root package name */
    private static final int[] f14585f = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 8, 13, 13, 4, 17, 52, 2, 14, 13, 19, 4, 13, 19, 52, 17, 4, 18, 52, 28, 14, 13, 5, 8, 6, 20, 17, 0, 19, 8, 14, 13, 48, 17, 0, 15, 15, 4, 17};

    /* renamed from: g, reason: collision with root package name */
    private static final int[] f14586g = {0, 13, 3, 17, 14, 8, 3, 52, 21, 8, 4, 22, 52, 40, 15, 15, 14, 27, 0, 18, 4, 47, 8, 4, 22};

    /* renamed from: h, reason: collision with root package name */
    private static final int[] f14587h = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 2, 11, 8, 2, 10, 19, 14, 15};

    /* renamed from: i, reason: collision with root package name */
    private static final int[] f14588i = {2, 14, 12, 52, 2, 14, 11, 14, 17, 52, 8, 13, 13, 4, 17, 52, 22, 8, 3, 6, 4, 19, 52, 26, 1, 18, 37, 8, 18, 19, 47, 8, 4, 22, 48, 17, 0, 15, 15, 4, 17};

    /* renamed from: j, reason: collision with root package name */
    private static final int[] f14589j = {17, 14, 52, 14, 15, 15, 14, 52, 19, 7, 4, 12, 4, 52, 21, 4, 17, 18, 8, 14, 13};

    private a() {
    }

    public static a b() {
        if (f14580a == null) {
            synchronized (a.class) {
                if (f14580a == null) {
                    f14580a = new a();
                }
            }
        }
        return f14580a;
    }

    public String a() {
        int length = f14587h.length;
        char[] cArr = new char[length];
        for (int i10 = 0; i10 < length; i10++) {
            cArr[i10] = f14581b[f14587h[i10]];
        }
        return String.valueOf(cArr);
    }

    public String c() {
        int length = f14582c.length;
        char[] cArr = new char[length];
        for (int i10 = 0; i10 < length; i10++) {
            cArr[i10] = f14581b[f14582c[i10]];
        }
        return String.valueOf(cArr);
    }
}
