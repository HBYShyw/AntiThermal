package qb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import kotlin.collections.r;
import ma.u;
import za.DefaultConstructorMarker;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'A' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:451)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByField(EnumVisitor.java:372)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByWrappedInsn(EnumVisitor.java:337)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:322)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:262)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInvoke(EnumVisitor.java:293)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:266)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* compiled from: KotlinTarget.kt */
/* renamed from: qb.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class KotlinTarget {
    public static final KotlinTarget A;
    public static final KotlinTarget B;
    public static final KotlinTarget C;
    public static final KotlinTarget D;
    public static final KotlinTarget E;
    public static final KotlinTarget F;
    public static final KotlinTarget G;
    public static final KotlinTarget H;

    /* renamed from: i, reason: collision with root package name */
    private static final Set<KotlinTarget> f17224i;

    /* renamed from: j, reason: collision with root package name */
    private static final Set<KotlinTarget> f17226j;

    /* renamed from: k, reason: collision with root package name */
    private static final List<KotlinTarget> f17228k;

    /* renamed from: l, reason: collision with root package name */
    private static final List<KotlinTarget> f17230l;

    /* renamed from: m, reason: collision with root package name */
    private static final List<KotlinTarget> f17232m;

    /* renamed from: n, reason: collision with root package name */
    private static final List<KotlinTarget> f17234n;

    /* renamed from: o, reason: collision with root package name */
    private static final List<KotlinTarget> f17236o;

    /* renamed from: p, reason: collision with root package name */
    private static final List<KotlinTarget> f17238p;

    /* renamed from: q, reason: collision with root package name */
    private static final List<KotlinTarget> f17239q;

    /* renamed from: r, reason: collision with root package name */
    private static final List<KotlinTarget> f17240r;

    /* renamed from: s, reason: collision with root package name */
    private static final List<KotlinTarget> f17241s;

    /* renamed from: t, reason: collision with root package name */
    private static final List<KotlinTarget> f17242t;

    /* renamed from: u, reason: collision with root package name */
    private static final List<KotlinTarget> f17243u;

    /* renamed from: v, reason: collision with root package name */
    private static final List<KotlinTarget> f17244v;

    /* renamed from: w, reason: collision with root package name */
    private static final Map<AnnotationUseSiteTarget, KotlinTarget> f17245w;

    /* renamed from: e, reason: collision with root package name */
    private final String f17249e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f17250f;

    /* renamed from: x, reason: collision with root package name */
    public static final KotlinTarget f17246x = new KotlinTarget("CLASS", 0, "class", false, 2, null);

    /* renamed from: y, reason: collision with root package name */
    public static final KotlinTarget f17247y = new KotlinTarget("ANNOTATION_CLASS", 1, "annotation class", false, 2, null);

    /* renamed from: z, reason: collision with root package name */
    public static final KotlinTarget f17248z = new KotlinTarget("TYPE_PARAMETER", 2, "type parameter", false);
    public static final KotlinTarget I = new KotlinTarget("TYPE", 11, "type usage", false);
    public static final KotlinTarget J = new KotlinTarget("EXPRESSION", 12, "expression", false);
    public static final KotlinTarget K = new KotlinTarget("FILE", 13, "file", false);
    public static final KotlinTarget L = new KotlinTarget("TYPEALIAS", 14, "typealias", false);
    public static final KotlinTarget M = new KotlinTarget("TYPE_PROJECTION", 15, "type projection", false);
    public static final KotlinTarget N = new KotlinTarget("STAR_PROJECTION", 16, "star projection", false);
    public static final KotlinTarget O = new KotlinTarget("PROPERTY_PARAMETER", 17, "property constructor parameter", false);
    public static final KotlinTarget P = new KotlinTarget("CLASS_ONLY", 18, "class", false);
    public static final KotlinTarget Q = new KotlinTarget("OBJECT", 19, "object", false);
    public static final KotlinTarget R = new KotlinTarget("STANDALONE_OBJECT", 20, "standalone object", false);
    public static final KotlinTarget S = new KotlinTarget("COMPANION_OBJECT", 21, "companion object", false);
    public static final KotlinTarget T = new KotlinTarget("INTERFACE", 22, "interface", false);
    public static final KotlinTarget U = new KotlinTarget("ENUM_CLASS", 23, "enum class", false);
    public static final KotlinTarget V = new KotlinTarget("ENUM_ENTRY", 24, "enum entry", false);
    public static final KotlinTarget W = new KotlinTarget("LOCAL_CLASS", 25, "local class", false);
    public static final KotlinTarget X = new KotlinTarget("LOCAL_FUNCTION", 26, "local function", false);
    public static final KotlinTarget Y = new KotlinTarget("MEMBER_FUNCTION", 27, "member function", false);
    public static final KotlinTarget Z = new KotlinTarget("TOP_LEVEL_FUNCTION", 28, "top level function", false);

    /* renamed from: a0, reason: collision with root package name */
    public static final KotlinTarget f17214a0 = new KotlinTarget("MEMBER_PROPERTY", 29, "member property", false);

    /* renamed from: b0, reason: collision with root package name */
    public static final KotlinTarget f17215b0 = new KotlinTarget("MEMBER_PROPERTY_WITH_BACKING_FIELD", 30, "member property with backing field", false);

    /* renamed from: c0, reason: collision with root package name */
    public static final KotlinTarget f17216c0 = new KotlinTarget("MEMBER_PROPERTY_WITH_DELEGATE", 31, "member property with delegate", false);

    /* renamed from: d0, reason: collision with root package name */
    public static final KotlinTarget f17217d0 = new KotlinTarget("MEMBER_PROPERTY_WITHOUT_FIELD_OR_DELEGATE", 32, "member property without backing field or delegate", false);

    /* renamed from: e0, reason: collision with root package name */
    public static final KotlinTarget f17218e0 = new KotlinTarget("TOP_LEVEL_PROPERTY", 33, "top level property", false);

    /* renamed from: f0, reason: collision with root package name */
    public static final KotlinTarget f17219f0 = new KotlinTarget("TOP_LEVEL_PROPERTY_WITH_BACKING_FIELD", 34, "top level property with backing field", false);

    /* renamed from: g0, reason: collision with root package name */
    public static final KotlinTarget f17221g0 = new KotlinTarget("TOP_LEVEL_PROPERTY_WITH_DELEGATE", 35, "top level property with delegate", false);

    /* renamed from: h0, reason: collision with root package name */
    public static final KotlinTarget f17223h0 = new KotlinTarget("TOP_LEVEL_PROPERTY_WITHOUT_FIELD_OR_DELEGATE", 36, "top level property without backing field or delegate", false);

    /* renamed from: i0, reason: collision with root package name */
    public static final KotlinTarget f17225i0 = new KotlinTarget("BACKING_FIELD", 37, "backing field", false, 2, null);

    /* renamed from: j0, reason: collision with root package name */
    public static final KotlinTarget f17227j0 = new KotlinTarget("INITIALIZER", 38, "initializer", false);

    /* renamed from: k0, reason: collision with root package name */
    public static final KotlinTarget f17229k0 = new KotlinTarget("DESTRUCTURING_DECLARATION", 39, "destructuring declaration", false);

    /* renamed from: l0, reason: collision with root package name */
    public static final KotlinTarget f17231l0 = new KotlinTarget("LAMBDA_EXPRESSION", 40, "lambda expression", false);

    /* renamed from: m0, reason: collision with root package name */
    public static final KotlinTarget f17233m0 = new KotlinTarget("ANONYMOUS_FUNCTION", 41, "anonymous function", false);

    /* renamed from: n0, reason: collision with root package name */
    public static final KotlinTarget f17235n0 = new KotlinTarget("OBJECT_LITERAL", 42, "object literal", false);

    /* renamed from: o0, reason: collision with root package name */
    private static final /* synthetic */ KotlinTarget[] f17237o0 = a();

    /* renamed from: g, reason: collision with root package name */
    public static final a f17220g = new a(null);

    /* renamed from: h, reason: collision with root package name */
    private static final HashMap<String, KotlinTarget> f17222h = new HashMap<>();

    /* compiled from: KotlinTarget.kt */
    /* renamed from: qb.n$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        Set<KotlinTarget> D0;
        Set<KotlinTarget> r02;
        List<KotlinTarget> m10;
        List<KotlinTarget> m11;
        List<KotlinTarget> m12;
        List<KotlinTarget> m13;
        List<KotlinTarget> m14;
        List<KotlinTarget> m15;
        List<KotlinTarget> m16;
        List<KotlinTarget> m17;
        List<KotlinTarget> e10;
        List<KotlinTarget> e11;
        List<KotlinTarget> e12;
        List<KotlinTarget> e13;
        Map<AnnotationUseSiteTarget, KotlinTarget> l10;
        boolean z10 = false;
        int i10 = 2;
        DefaultConstructorMarker defaultConstructorMarker = null;
        A = new KotlinTarget("PROPERTY", 3, "property", z10, i10, defaultConstructorMarker);
        boolean z11 = false;
        int i11 = 2;
        DefaultConstructorMarker defaultConstructorMarker2 = null;
        B = new KotlinTarget("FIELD", 4, "field", z11, i11, defaultConstructorMarker2);
        C = new KotlinTarget("LOCAL_VARIABLE", 5, "local variable", z10, i10, defaultConstructorMarker);
        D = new KotlinTarget("VALUE_PARAMETER", 6, "value parameter", z11, i11, defaultConstructorMarker2);
        E = new KotlinTarget("CONSTRUCTOR", 7, "constructor", z10, i10, defaultConstructorMarker);
        F = new KotlinTarget("FUNCTION", 8, "function", z11, i11, defaultConstructorMarker2);
        G = new KotlinTarget("PROPERTY_GETTER", 9, "getter", z10, i10, defaultConstructorMarker);
        H = new KotlinTarget("PROPERTY_SETTER", 10, "setter", z11, i11, defaultConstructorMarker2);
        for (KotlinTarget kotlinTarget : values()) {
            f17222h.put(kotlinTarget.name(), kotlinTarget);
        }
        KotlinTarget[] values = values();
        ArrayList arrayList = new ArrayList();
        for (KotlinTarget kotlinTarget2 : values) {
            if (kotlinTarget2.f17250f) {
                arrayList.add(kotlinTarget2);
            }
        }
        D0 = _Collections.D0(arrayList);
        f17224i = D0;
        r02 = _Arrays.r0(values());
        f17226j = r02;
        KotlinTarget kotlinTarget3 = f17246x;
        m10 = r.m(f17247y, kotlinTarget3);
        f17228k = m10;
        m11 = r.m(W, kotlinTarget3);
        f17230l = m11;
        m12 = r.m(P, kotlinTarget3);
        f17232m = m12;
        KotlinTarget kotlinTarget4 = Q;
        m13 = r.m(S, kotlinTarget4, kotlinTarget3);
        f17234n = m13;
        m14 = r.m(R, kotlinTarget4, kotlinTarget3);
        f17236o = m14;
        m15 = r.m(T, kotlinTarget3);
        f17238p = m15;
        m16 = r.m(U, kotlinTarget3);
        f17239q = m16;
        KotlinTarget kotlinTarget5 = A;
        KotlinTarget kotlinTarget6 = B;
        m17 = r.m(V, kotlinTarget5, kotlinTarget6);
        f17240r = m17;
        KotlinTarget kotlinTarget7 = H;
        e10 = CollectionsJVM.e(kotlinTarget7);
        f17241s = e10;
        KotlinTarget kotlinTarget8 = G;
        e11 = CollectionsJVM.e(kotlinTarget8);
        f17242t = e11;
        e12 = CollectionsJVM.e(F);
        f17243u = e12;
        KotlinTarget kotlinTarget9 = K;
        e13 = CollectionsJVM.e(kotlinTarget9);
        f17244v = e13;
        AnnotationUseSiteTarget annotationUseSiteTarget = AnnotationUseSiteTarget.CONSTRUCTOR_PARAMETER;
        KotlinTarget kotlinTarget10 = D;
        l10 = m0.l(u.a(annotationUseSiteTarget, kotlinTarget10), u.a(AnnotationUseSiteTarget.FIELD, kotlinTarget6), u.a(AnnotationUseSiteTarget.PROPERTY, kotlinTarget5), u.a(AnnotationUseSiteTarget.FILE, kotlinTarget9), u.a(AnnotationUseSiteTarget.PROPERTY_GETTER, kotlinTarget8), u.a(AnnotationUseSiteTarget.PROPERTY_SETTER, kotlinTarget7), u.a(AnnotationUseSiteTarget.RECEIVER, kotlinTarget10), u.a(AnnotationUseSiteTarget.SETTER_PARAMETER, kotlinTarget10), u.a(AnnotationUseSiteTarget.PROPERTY_DELEGATE_FIELD, kotlinTarget6));
        f17245w = l10;
    }

    private KotlinTarget(String str, int i10, String str2, boolean z10) {
        this.f17249e = str2;
        this.f17250f = z10;
    }

    private static final /* synthetic */ KotlinTarget[] a() {
        return new KotlinTarget[]{f17246x, f17247y, f17248z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, f17214a0, f17215b0, f17216c0, f17217d0, f17218e0, f17219f0, f17221g0, f17223h0, f17225i0, f17227j0, f17229k0, f17231l0, f17233m0, f17235n0};
    }

    public static KotlinTarget valueOf(String str) {
        return (KotlinTarget) Enum.valueOf(KotlinTarget.class, str);
    }

    public static KotlinTarget[] values() {
        return (KotlinTarget[]) f17237o0.clone();
    }

    /* synthetic */ KotlinTarget(String str, int i10, String str2, boolean z10, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, i10, str2, (i11 & 2) != 0 ? true : z10);
    }
}
