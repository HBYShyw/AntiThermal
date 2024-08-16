package yb;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections._Sets;
import kotlin.collections.m0;
import kotlin.collections.s0;
import mb.StandardNames;
import oc.FqName;

/* compiled from: JvmAnnotationNames.kt */
/* loaded from: classes2.dex */
public final class c0 {

    /* renamed from: a, reason: collision with root package name */
    private static final FqName f20048a;

    /* renamed from: b, reason: collision with root package name */
    private static final FqName f20049b;

    /* renamed from: c, reason: collision with root package name */
    private static final FqName f20050c;

    /* renamed from: d, reason: collision with root package name */
    private static final List<FqName> f20051d;

    /* renamed from: e, reason: collision with root package name */
    private static final FqName f20052e;

    /* renamed from: f, reason: collision with root package name */
    private static final FqName f20053f;

    /* renamed from: g, reason: collision with root package name */
    private static final List<FqName> f20054g;

    /* renamed from: h, reason: collision with root package name */
    private static final FqName f20055h;

    /* renamed from: i, reason: collision with root package name */
    private static final FqName f20056i;

    /* renamed from: j, reason: collision with root package name */
    private static final FqName f20057j;

    /* renamed from: k, reason: collision with root package name */
    private static final FqName f20058k;

    /* renamed from: l, reason: collision with root package name */
    private static final Set<FqName> f20059l;

    /* renamed from: m, reason: collision with root package name */
    private static final Set<FqName> f20060m;

    /* renamed from: n, reason: collision with root package name */
    private static final Set<FqName> f20061n;

    /* renamed from: o, reason: collision with root package name */
    private static final Map<FqName, FqName> f20062o;

    static {
        List<FqName> m10;
        List<FqName> m11;
        Set k10;
        Set l10;
        Set k11;
        Set l11;
        Set l12;
        Set l13;
        Set l14;
        Set l15;
        Set l16;
        Set<FqName> l17;
        Set<FqName> h10;
        Set<FqName> h11;
        Map<FqName, FqName> l18;
        FqName fqName = new FqName("org.jspecify.nullness.Nullable");
        f20048a = fqName;
        FqName fqName2 = new FqName("org.jspecify.nullness.NullnessUnspecified");
        f20049b = fqName2;
        FqName fqName3 = new FqName("org.jspecify.nullness.NullMarked");
        f20050c = fqName3;
        m10 = kotlin.collections.r.m(b0.f20029l, new FqName("androidx.annotation.Nullable"), new FqName("androidx.annotation.Nullable"), new FqName("android.annotation.Nullable"), new FqName("com.android.annotations.Nullable"), new FqName("org.eclipse.jdt.annotation.Nullable"), new FqName("org.checkerframework.checker.nullness.qual.Nullable"), new FqName("javax.annotation.Nullable"), new FqName("javax.annotation.CheckForNull"), new FqName("edu.umd.cs.findbugs.annotations.CheckForNull"), new FqName("edu.umd.cs.findbugs.annotations.Nullable"), new FqName("edu.umd.cs.findbugs.annotations.PossiblyNull"), new FqName("io.reactivex.annotations.Nullable"), new FqName("io.reactivex.rxjava3.annotations.Nullable"));
        f20051d = m10;
        FqName fqName4 = new FqName("javax.annotation.Nonnull");
        f20052e = fqName4;
        f20053f = new FqName("javax.annotation.CheckForNull");
        m11 = kotlin.collections.r.m(b0.f20028k, new FqName("edu.umd.cs.findbugs.annotations.NonNull"), new FqName("androidx.annotation.NonNull"), new FqName("androidx.annotation.NonNull"), new FqName("android.annotation.NonNull"), new FqName("com.android.annotations.NonNull"), new FqName("org.eclipse.jdt.annotation.NonNull"), new FqName("org.checkerframework.checker.nullness.qual.NonNull"), new FqName("lombok.NonNull"), new FqName("io.reactivex.annotations.NonNull"), new FqName("io.reactivex.rxjava3.annotations.NonNull"));
        f20054g = m11;
        FqName fqName5 = new FqName("org.checkerframework.checker.nullness.compatqual.NullableDecl");
        f20055h = fqName5;
        FqName fqName6 = new FqName("org.checkerframework.checker.nullness.compatqual.NonNullDecl");
        f20056i = fqName6;
        FqName fqName7 = new FqName("androidx.annotation.RecentlyNullable");
        f20057j = fqName7;
        FqName fqName8 = new FqName("androidx.annotation.RecentlyNonNull");
        f20058k = fqName8;
        k10 = _Sets.k(new LinkedHashSet(), m10);
        l10 = _Sets.l(k10, fqName4);
        k11 = _Sets.k(l10, m11);
        l11 = _Sets.l(k11, fqName5);
        l12 = _Sets.l(l11, fqName6);
        l13 = _Sets.l(l12, fqName7);
        l14 = _Sets.l(l13, fqName8);
        l15 = _Sets.l(l14, fqName);
        l16 = _Sets.l(l15, fqName2);
        l17 = _Sets.l(l16, fqName3);
        f20059l = l17;
        h10 = s0.h(b0.f20031n, b0.f20032o);
        f20060m = h10;
        h11 = s0.h(b0.f20030m, b0.f20033p);
        f20061n = h11;
        l18 = m0.l(ma.u.a(b0.f20021d, StandardNames.a.H), ma.u.a(b0.f20023f, StandardNames.a.L), ma.u.a(b0.f20025h, StandardNames.a.f15337y), ma.u.a(b0.f20026i, StandardNames.a.P));
        f20062o = l18;
    }

    public static final FqName a() {
        return f20058k;
    }

    public static final FqName b() {
        return f20057j;
    }

    public static final FqName c() {
        return f20056i;
    }

    public static final FqName d() {
        return f20055h;
    }

    public static final FqName e() {
        return f20053f;
    }

    public static final FqName f() {
        return f20052e;
    }

    public static final FqName g() {
        return f20048a;
    }

    public static final FqName h() {
        return f20049b;
    }

    public static final FqName i() {
        return f20050c;
    }

    public static final Set<FqName> j() {
        return f20061n;
    }

    public static final List<FqName> k() {
        return f20054g;
    }

    public static final List<FqName> l() {
        return f20051d;
    }

    public static final Set<FqName> m() {
        return f20060m;
    }
}
