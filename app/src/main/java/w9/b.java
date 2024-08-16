package w9;

import android.icu.text.AlphabeticIndex;
import android.text.TextUtils;
import ja.WordQueryCompat;
import ja.ZhuyinUtils;
import java.util.Locale;

/* compiled from: ComplexSortUtils.java */
/* loaded from: classes2.dex */
public class b {

    /* renamed from: e, reason: collision with root package name */
    public static final Locale f19412e = new Locale("ar");

    /* renamed from: f, reason: collision with root package name */
    public static final Locale f19413f = new Locale("el");

    /* renamed from: g, reason: collision with root package name */
    public static final Locale f19414g = new Locale("he");

    /* renamed from: h, reason: collision with root package name */
    public static final Locale f19415h = new Locale("sr");

    /* renamed from: i, reason: collision with root package name */
    public static final Locale f19416i = new Locale("uk");

    /* renamed from: j, reason: collision with root package name */
    public static final Locale f19417j = new Locale("th");

    /* renamed from: k, reason: collision with root package name */
    private static b f19418k;

    /* renamed from: l, reason: collision with root package name */
    private static c f19419l;

    /* renamed from: m, reason: collision with root package name */
    private static c f19420m;

    /* renamed from: a, reason: collision with root package name */
    protected final AlphabeticIndex.ImmutableIndex f19421a;

    /* renamed from: b, reason: collision with root package name */
    private final int f19422b;

    /* renamed from: c, reason: collision with root package name */
    private final int f19423c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f19424d;

    public b(c cVar) {
        if (cVar == null) {
            f19419l = c.d();
        } else if (cVar.toString().equals("ur_PK")) {
            f19419l = new c(new Locale("ar_EG"));
        } else {
            f19419l = cVar;
        }
        Locale f10 = f19419l.f();
        this.f19424d = f19419l.k();
        AlphabeticIndex maxLabelCount = new AlphabeticIndex(f19419l.e()).setMaxLabelCount(300);
        if (f10 != null) {
            maxLabelCount.addLabels(f10);
        }
        AlphabeticIndex.ImmutableIndex buildImmutableIndex = maxLabelCount.addLabels(Locale.ENGLISH).addLabels(Locale.JAPANESE).addLabels(Locale.KOREAN).addLabels(f19417j).addLabels(f19412e).addLabels(f19414g).addLabels(f19413f).addLabels(f19416i).addLabels(f19415h).buildImmutableIndex();
        this.f19421a = buildImmutableIndex;
        int bucketCount = buildImmutableIndex.getBucketCount();
        this.f19422b = bucketCount;
        this.f19423c = bucketCount - 1;
    }

    public static synchronized b e() {
        b bVar;
        synchronized (b.class) {
            c cVar = f19420m;
            if (cVar == null) {
                f19420m = c.d();
            } else {
                if (cVar.toString().equals("ur_PK")) {
                    f19420m = new c(new Locale("ar_EG"));
                }
                f19420m = c.d();
            }
            if (f19418k == null || !f19419l.toString().equals(f19420m.toString())) {
                f19418k = new b(c.d());
            }
            bVar = f19418k;
        }
        return bVar;
    }

    private boolean f(char c10) {
        return c10 >= 19968 && c10 <= 40869;
    }

    private boolean g(char c10) {
        return (c10 >= 'A' && c10 <= 'Z') || (c10 >= 'a' && c10 <= 'z');
    }

    public int a() {
        return this.f19422b + 1;
    }

    public int b(String str) {
        boolean z10;
        if (TextUtils.isEmpty(str)) {
            return this.f19423c;
        }
        StringBuilder sb2 = new StringBuilder();
        if (!y5.b.c() && Locale.getDefault().equals(Locale.TAIWAN)) {
            sb2.append(ZhuyinUtils.a(str));
        } else {
            for (int i10 = 0; i10 < str.length(); i10++) {
                char charAt = str.charAt(i10);
                if (f(charAt)) {
                    sb2.append(WordQueryCompat.a(charAt));
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
            return this.f19423c;
        }
        char charAt2 = sb3.charAt(0);
        if (y5.b.c() && !g(charAt2)) {
            return this.f19423c;
        }
        int bucketIndex = this.f19421a.getBucketIndex(sb3);
        if (bucketIndex < 0) {
            return -1;
        }
        if (bucketIndex == 0) {
            return this.f19423c;
        }
        return bucketIndex >= this.f19423c ? bucketIndex + 1 : bucketIndex;
    }

    public String c(int i10) {
        if (i10 < 0 || i10 >= a()) {
            return "";
        }
        int i11 = this.f19423c;
        if (i10 == i11) {
            return "#";
        }
        if (i10 > i11) {
            i10--;
        }
        return this.f19421a.getBucket(i10) != null ? this.f19421a.getBucket(i10).getLabel() : "";
    }

    public String d(int i10, String str) {
        String c10 = c(i10);
        return (!TextUtils.equals(c10, "…") || str == null || y5.b.c() || !Locale.getDefault().equals(Locale.TAIWAN)) ? c10 : ZhuyinUtils.a(str);
    }
}
