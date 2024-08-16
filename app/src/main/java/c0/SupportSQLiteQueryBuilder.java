package c0;

import java.util.regex.Pattern;

/* compiled from: SupportSQLiteQueryBuilder.java */
/* renamed from: c0.f, reason: use source file name */
/* loaded from: classes.dex */
public final class SupportSQLiteQueryBuilder {

    /* renamed from: j, reason: collision with root package name */
    private static final Pattern f4731j = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

    /* renamed from: b, reason: collision with root package name */
    private final String f4733b;

    /* renamed from: d, reason: collision with root package name */
    private String f4735d;

    /* renamed from: e, reason: collision with root package name */
    private Object[] f4736e;

    /* renamed from: a, reason: collision with root package name */
    private boolean f4732a = false;

    /* renamed from: c, reason: collision with root package name */
    private String[] f4734c = null;

    /* renamed from: f, reason: collision with root package name */
    private String f4737f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f4738g = null;

    /* renamed from: h, reason: collision with root package name */
    private String f4739h = null;

    /* renamed from: i, reason: collision with root package name */
    private String f4740i = null;

    private SupportSQLiteQueryBuilder(String str) {
        this.f4733b = str;
    }

    private static void a(StringBuilder sb2, String str, String str2) {
        if (f(str2)) {
            return;
        }
        sb2.append(str);
        sb2.append(str2);
    }

    private static void b(StringBuilder sb2, String[] strArr) {
        int length = strArr.length;
        for (int i10 = 0; i10 < length; i10++) {
            String str = strArr[i10];
            if (i10 > 0) {
                sb2.append(", ");
            }
            sb2.append(str);
        }
        sb2.append(' ');
    }

    public static SupportSQLiteQueryBuilder c(String str) {
        return new SupportSQLiteQueryBuilder(str);
    }

    private static boolean f(String str) {
        return str == null || str.length() == 0;
    }

    public SupportSQLiteQueryBuilder d(String[] strArr) {
        this.f4734c = strArr;
        return this;
    }

    public SupportSQLiteQuery e() {
        if (f(this.f4737f) && !f(this.f4738g)) {
            throw new IllegalArgumentException("HAVING clauses are only permitted when using a groupBy clause");
        }
        StringBuilder sb2 = new StringBuilder(120);
        sb2.append("SELECT ");
        if (this.f4732a) {
            sb2.append("DISTINCT ");
        }
        String[] strArr = this.f4734c;
        if (strArr != null && strArr.length != 0) {
            b(sb2, strArr);
        } else {
            sb2.append(" * ");
        }
        sb2.append(" FROM ");
        sb2.append(this.f4733b);
        a(sb2, " WHERE ", this.f4735d);
        a(sb2, " GROUP BY ", this.f4737f);
        a(sb2, " HAVING ", this.f4738g);
        a(sb2, " ORDER BY ", this.f4739h);
        a(sb2, " LIMIT ", this.f4740i);
        return new SimpleSQLiteQuery(sb2.toString(), this.f4736e);
    }

    public SupportSQLiteQueryBuilder g(String str) {
        this.f4739h = str;
        return this;
    }

    public SupportSQLiteQueryBuilder h(String str, Object[] objArr) {
        this.f4735d = str;
        this.f4736e = objArr;
        return this;
    }
}
