package a8;

import android.icu.text.AlphabeticIndex;
import android.text.TextUtils;
import c8.WordQuery;
import java.util.Locale;
import y5.AppFeature;

/* compiled from: ComplexSortUtils.java */
/* loaded from: classes.dex */
public class a {

    /* renamed from: e, reason: collision with root package name */
    public static final Locale f85e = new Locale("ar");

    /* renamed from: f, reason: collision with root package name */
    public static final Locale f86f = new Locale("el");

    /* renamed from: g, reason: collision with root package name */
    public static final Locale f87g = new Locale("he");

    /* renamed from: h, reason: collision with root package name */
    public static final Locale f88h = new Locale("sr");

    /* renamed from: i, reason: collision with root package name */
    public static final Locale f89i = new Locale("uk");

    /* renamed from: j, reason: collision with root package name */
    public static final Locale f90j = new Locale("th");

    /* renamed from: k, reason: collision with root package name */
    private static a f91k;

    /* renamed from: l, reason: collision with root package name */
    private static b f92l;

    /* renamed from: m, reason: collision with root package name */
    private static b f93m;

    /* renamed from: a, reason: collision with root package name */
    protected final AlphabeticIndex.ImmutableIndex f94a;

    /* renamed from: b, reason: collision with root package name */
    private final int f95b;

    /* renamed from: c, reason: collision with root package name */
    private final int f96c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f97d;

    public a(b bVar) {
        if (bVar == null) {
            h(b.d());
            bVar = b.d();
        } else if (bVar.toString().equals("ur_PK")) {
            h(new b(new Locale("ar_EG")));
        } else {
            h(bVar);
        }
        Locale f10 = bVar.f();
        this.f97d = bVar.k();
        AlphabeticIndex maxLabelCount = new AlphabeticIndex(bVar.e()).setMaxLabelCount(300);
        if (f10 != null) {
            maxLabelCount.addLabels(f10);
        }
        AlphabeticIndex.ImmutableIndex buildImmutableIndex = maxLabelCount.addLabels(Locale.ENGLISH).addLabels(Locale.JAPANESE).addLabels(Locale.KOREAN).addLabels(f90j).addLabels(f85e).addLabels(f87g).addLabels(f86f).addLabels(f89i).addLabels(f88h).buildImmutableIndex();
        this.f94a = buildImmutableIndex;
        int bucketCount = buildImmutableIndex.getBucketCount();
        this.f95b = bucketCount;
        this.f96c = bucketCount - 1;
    }

    private char d(char c10) {
        return WordQuery.a(c10);
    }

    public static synchronized a e() {
        a aVar;
        synchronized (a.class) {
            b bVar = f93m;
            if (bVar == null) {
                f93m = b.d();
            } else {
                if (bVar.toString().equals("ur_PK")) {
                    f93m = new b(new Locale("ar_EG"));
                }
                f93m = b.d();
            }
            if (f91k == null || !f92l.toString().equals(f93m.toString())) {
                f91k = new a(b.d());
            }
            aVar = f91k;
        }
        return aVar;
    }

    private boolean f(char c10) {
        return c10 >= 19968 && c10 <= 40869;
    }

    private boolean g(char c10) {
        return (c10 >= 'A' && c10 <= 'Z') || (c10 >= 'a' && c10 <= 'z');
    }

    private static synchronized void h(b bVar) {
        synchronized (a.class) {
            f92l = bVar;
        }
    }

    public int a() {
        return this.f95b + 1;
    }

    public int b(String str) {
        boolean z10;
        if (TextUtils.isEmpty(str)) {
            return this.f96c;
        }
        StringBuilder sb2 = new StringBuilder();
        if (AppFeature.D() && Locale.getDefault().equals(Locale.TAIWAN)) {
            sb2.append(WordQuery.c(str));
        } else {
            for (int i10 = 0; i10 < str.length(); i10++) {
                char charAt = str.charAt(i10);
                if (f(charAt)) {
                    sb2.append(d(charAt));
                } else {
                    sb2.append(charAt);
                }
            }
        }
        String sb3 = sb2.toString();
        int length = sb3.length();
        int i11 = 0;
        while (i11 < length) {
            int codePointAt = Character.codePointAt(sb3, i11);
            if (Character.isDigit(codePointAt) || codePointAt == 35) {
                z10 = true;
                break;
            }
            if (!Character.isSpaceChar(codePointAt) && codePointAt != 43 && codePointAt != 40 && codePointAt != 41 && codePointAt != 46 && codePointAt != 45 && codePointAt != 35) {
                break;
            }
            i11 += Character.charCount(codePointAt);
        }
        z10 = false;
        if (z10) {
            return this.f96c;
        }
        char charAt2 = sb3.charAt(0);
        if (!AppFeature.D() && !g(charAt2)) {
            return this.f96c;
        }
        int bucketIndex = this.f94a.getBucketIndex(sb3);
        if (bucketIndex < 0) {
            return -1;
        }
        if (bucketIndex == 0) {
            return this.f96c;
        }
        return bucketIndex >= this.f96c ? bucketIndex + 1 : bucketIndex;
    }

    public String c(int i10) {
        if (i10 < 0 || i10 >= a()) {
            return "";
        }
        int i11 = this.f96c;
        if (i10 == i11) {
            return "#";
        }
        if (i10 > i11) {
            i10--;
        }
        AlphabeticIndex.ImmutableIndex immutableIndex = this.f94a;
        return (immutableIndex == null || immutableIndex.getBucket(i10) == null) ? "#" : this.f94a.getBucket(i10).getLabel();
    }
}
