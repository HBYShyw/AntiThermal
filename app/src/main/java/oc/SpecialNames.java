package oc;

/* compiled from: SpecialNames.kt */
/* renamed from: oc.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class SpecialNames {

    /* renamed from: a, reason: collision with root package name */
    public static final SpecialNames f16446a = new SpecialNames();

    /* renamed from: b, reason: collision with root package name */
    public static final Name f16447b;

    /* renamed from: c, reason: collision with root package name */
    public static final Name f16448c;

    /* renamed from: d, reason: collision with root package name */
    public static final Name f16449d;

    /* renamed from: e, reason: collision with root package name */
    public static final Name f16450e;

    /* renamed from: f, reason: collision with root package name */
    public static final Name f16451f;

    /* renamed from: g, reason: collision with root package name */
    public static final Name f16452g;

    /* renamed from: h, reason: collision with root package name */
    public static final Name f16453h;

    /* renamed from: i, reason: collision with root package name */
    public static final Name f16454i;

    /* renamed from: j, reason: collision with root package name */
    public static final Name f16455j;

    /* renamed from: k, reason: collision with root package name */
    public static final Name f16456k;

    /* renamed from: l, reason: collision with root package name */
    public static final Name f16457l;

    /* renamed from: m, reason: collision with root package name */
    public static final Name f16458m;

    /* renamed from: n, reason: collision with root package name */
    public static final Name f16459n;

    /* renamed from: o, reason: collision with root package name */
    public static final Name f16460o;

    /* renamed from: p, reason: collision with root package name */
    public static final Name f16461p;

    /* renamed from: q, reason: collision with root package name */
    public static final Name f16462q;

    /* renamed from: r, reason: collision with root package name */
    public static final Name f16463r;

    static {
        Name i10 = Name.i("<no name provided>");
        za.k.d(i10, "special(\"<no name provided>\")");
        f16447b = i10;
        Name i11 = Name.i("<root package>");
        za.k.d(i11, "special(\"<root package>\")");
        f16448c = i11;
        Name f10 = Name.f("Companion");
        za.k.d(f10, "identifier(\"Companion\")");
        f16449d = f10;
        Name f11 = Name.f("no_name_in_PSI_3d19d79d_1ba9_4cd0_b7f5_b46aa3cd5d40");
        za.k.d(f11, "identifier(\"no_name_in_P…_4cd0_b7f5_b46aa3cd5d40\")");
        f16450e = f11;
        Name i12 = Name.i("<anonymous>");
        za.k.d(i12, "special(ANONYMOUS_STRING)");
        f16451f = i12;
        Name i13 = Name.i("<unary>");
        za.k.d(i13, "special(\"<unary>\")");
        f16452g = i13;
        Name i14 = Name.i("<unary-result>");
        za.k.d(i14, "special(\"<unary-result>\")");
        f16453h = i14;
        Name i15 = Name.i("<this>");
        za.k.d(i15, "special(\"<this>\")");
        f16454i = i15;
        Name i16 = Name.i("<init>");
        za.k.d(i16, "special(\"<init>\")");
        f16455j = i16;
        Name i17 = Name.i("<iterator>");
        za.k.d(i17, "special(\"<iterator>\")");
        f16456k = i17;
        Name i18 = Name.i("<destruct>");
        za.k.d(i18, "special(\"<destruct>\")");
        f16457l = i18;
        Name i19 = Name.i("<local>");
        za.k.d(i19, "special(\"<local>\")");
        f16458m = i19;
        Name i20 = Name.i("<unused var>");
        za.k.d(i20, "special(\"<unused var>\")");
        f16459n = i20;
        Name i21 = Name.i("<set-?>");
        za.k.d(i21, "special(\"<set-?>\")");
        f16460o = i21;
        Name i22 = Name.i("<array>");
        za.k.d(i22, "special(\"<array>\")");
        f16461p = i22;
        Name i23 = Name.i("<receiver>");
        za.k.d(i23, "special(\"<receiver>\")");
        f16462q = i23;
        Name i24 = Name.i("<get-entries>");
        za.k.d(i24, "special(\"<get-entries>\")");
        f16463r = i24;
    }

    private SpecialNames() {
    }

    public static final Name b(Name name) {
        return (name == null || name.g()) ? f16450e : name;
    }

    public final boolean a(Name name) {
        za.k.e(name, "name");
        String b10 = name.b();
        za.k.d(b10, "name.asString()");
        return (b10.length() > 0) && !name.g();
    }
}
