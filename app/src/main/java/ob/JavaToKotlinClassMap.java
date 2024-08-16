package ob;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import kotlin.collections.r;
import mb.CompanionObjectMapping;
import mb.PrimitiveType;
import mb.StandardNames;
import nb.FunctionClassKind;
import oc.ClassId;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;
import oc.SpecialNames;
import sd.StringNumberConversions;
import sd.v;
import xc.JvmPrimitiveType;

/* compiled from: JavaToKotlinClassMap.kt */
/* renamed from: ob.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaToKotlinClassMap {

    /* renamed from: a, reason: collision with root package name */
    public static final JavaToKotlinClassMap f16339a;

    /* renamed from: b, reason: collision with root package name */
    private static final String f16340b;

    /* renamed from: c, reason: collision with root package name */
    private static final String f16341c;

    /* renamed from: d, reason: collision with root package name */
    private static final String f16342d;

    /* renamed from: e, reason: collision with root package name */
    private static final String f16343e;

    /* renamed from: f, reason: collision with root package name */
    private static final ClassId f16344f;

    /* renamed from: g, reason: collision with root package name */
    private static final FqName f16345g;

    /* renamed from: h, reason: collision with root package name */
    private static final ClassId f16346h;

    /* renamed from: i, reason: collision with root package name */
    private static final ClassId f16347i;

    /* renamed from: j, reason: collision with root package name */
    private static final ClassId f16348j;

    /* renamed from: k, reason: collision with root package name */
    private static final HashMap<FqNameUnsafe, ClassId> f16349k;

    /* renamed from: l, reason: collision with root package name */
    private static final HashMap<FqNameUnsafe, ClassId> f16350l;

    /* renamed from: m, reason: collision with root package name */
    private static final HashMap<FqNameUnsafe, FqName> f16351m;

    /* renamed from: n, reason: collision with root package name */
    private static final HashMap<FqNameUnsafe, FqName> f16352n;

    /* renamed from: o, reason: collision with root package name */
    private static final HashMap<ClassId, ClassId> f16353o;

    /* renamed from: p, reason: collision with root package name */
    private static final HashMap<ClassId, ClassId> f16354p;

    /* renamed from: q, reason: collision with root package name */
    private static final List<a> f16355q;

    /* compiled from: JavaToKotlinClassMap.kt */
    /* renamed from: ob.c$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final ClassId f16356a;

        /* renamed from: b, reason: collision with root package name */
        private final ClassId f16357b;

        /* renamed from: c, reason: collision with root package name */
        private final ClassId f16358c;

        public a(ClassId classId, ClassId classId2, ClassId classId3) {
            za.k.e(classId, "javaClass");
            za.k.e(classId2, "kotlinReadOnly");
            za.k.e(classId3, "kotlinMutable");
            this.f16356a = classId;
            this.f16357b = classId2;
            this.f16358c = classId3;
        }

        public final ClassId a() {
            return this.f16356a;
        }

        public final ClassId b() {
            return this.f16357b;
        }

        public final ClassId c() {
            return this.f16358c;
        }

        public final ClassId d() {
            return this.f16356a;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return za.k.a(this.f16356a, aVar.f16356a) && za.k.a(this.f16357b, aVar.f16357b) && za.k.a(this.f16358c, aVar.f16358c);
        }

        public int hashCode() {
            return (((this.f16356a.hashCode() * 31) + this.f16357b.hashCode()) * 31) + this.f16358c.hashCode();
        }

        public String toString() {
            return "PlatformMutabilityMapping(javaClass=" + this.f16356a + ", kotlinReadOnly=" + this.f16357b + ", kotlinMutable=" + this.f16358c + ')';
        }
    }

    static {
        List<a> m10;
        JavaToKotlinClassMap javaToKotlinClassMap = new JavaToKotlinClassMap();
        f16339a = javaToKotlinClassMap;
        StringBuilder sb2 = new StringBuilder();
        FunctionClassKind functionClassKind = FunctionClassKind.f15969j;
        sb2.append(functionClassKind.c().toString());
        sb2.append('.');
        sb2.append(functionClassKind.b());
        f16340b = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        FunctionClassKind functionClassKind2 = FunctionClassKind.f15971l;
        sb3.append(functionClassKind2.c().toString());
        sb3.append('.');
        sb3.append(functionClassKind2.b());
        f16341c = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        FunctionClassKind functionClassKind3 = FunctionClassKind.f15970k;
        sb4.append(functionClassKind3.c().toString());
        sb4.append('.');
        sb4.append(functionClassKind3.b());
        f16342d = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        FunctionClassKind functionClassKind4 = FunctionClassKind.f15972m;
        sb5.append(functionClassKind4.c().toString());
        sb5.append('.');
        sb5.append(functionClassKind4.b());
        f16343e = sb5.toString();
        ClassId m11 = ClassId.m(new FqName("kotlin.jvm.functions.FunctionN"));
        za.k.d(m11, "topLevel(FqName(\"kotlin.jvm.functions.FunctionN\"))");
        f16344f = m11;
        FqName b10 = m11.b();
        za.k.d(b10, "FUNCTION_N_CLASS_ID.asSingleFqName()");
        f16345g = b10;
        oc.i iVar = oc.i.f16464a;
        f16346h = iVar.k();
        f16347i = iVar.j();
        f16348j = javaToKotlinClassMap.g(Class.class);
        f16349k = new HashMap<>();
        f16350l = new HashMap<>();
        f16351m = new HashMap<>();
        f16352n = new HashMap<>();
        f16353o = new HashMap<>();
        f16354p = new HashMap<>();
        ClassId m12 = ClassId.m(StandardNames.a.T);
        za.k.d(m12, "topLevel(FqNames.iterable)");
        FqName fqName = StandardNames.a.f15292b0;
        FqName h10 = m12.h();
        FqName h11 = m12.h();
        za.k.d(h11, "kotlinReadOnly.packageFqName");
        FqName g6 = oc.e.g(fqName, h11);
        ClassId classId = new ClassId(h10, g6, false);
        ClassId m13 = ClassId.m(StandardNames.a.S);
        za.k.d(m13, "topLevel(FqNames.iterator)");
        FqName fqName2 = StandardNames.a.f15290a0;
        FqName h12 = m13.h();
        FqName h13 = m13.h();
        za.k.d(h13, "kotlinReadOnly.packageFqName");
        ClassId classId2 = new ClassId(h12, oc.e.g(fqName2, h13), false);
        ClassId m14 = ClassId.m(StandardNames.a.U);
        za.k.d(m14, "topLevel(FqNames.collection)");
        FqName fqName3 = StandardNames.a.f15294c0;
        FqName h14 = m14.h();
        FqName h15 = m14.h();
        za.k.d(h15, "kotlinReadOnly.packageFqName");
        ClassId classId3 = new ClassId(h14, oc.e.g(fqName3, h15), false);
        ClassId m15 = ClassId.m(StandardNames.a.V);
        za.k.d(m15, "topLevel(FqNames.list)");
        FqName fqName4 = StandardNames.a.f15296d0;
        FqName h16 = m15.h();
        FqName h17 = m15.h();
        za.k.d(h17, "kotlinReadOnly.packageFqName");
        ClassId classId4 = new ClassId(h16, oc.e.g(fqName4, h17), false);
        ClassId m16 = ClassId.m(StandardNames.a.X);
        za.k.d(m16, "topLevel(FqNames.set)");
        FqName fqName5 = StandardNames.a.f15300f0;
        FqName h18 = m16.h();
        FqName h19 = m16.h();
        za.k.d(h19, "kotlinReadOnly.packageFqName");
        ClassId classId5 = new ClassId(h18, oc.e.g(fqName5, h19), false);
        ClassId m17 = ClassId.m(StandardNames.a.W);
        za.k.d(m17, "topLevel(FqNames.listIterator)");
        FqName fqName6 = StandardNames.a.f15298e0;
        FqName h20 = m17.h();
        FqName h21 = m17.h();
        za.k.d(h21, "kotlinReadOnly.packageFqName");
        ClassId classId6 = new ClassId(h20, oc.e.g(fqName6, h21), false);
        FqName fqName7 = StandardNames.a.Y;
        ClassId m18 = ClassId.m(fqName7);
        za.k.d(m18, "topLevel(FqNames.map)");
        FqName fqName8 = StandardNames.a.f15302g0;
        FqName h22 = m18.h();
        FqName h23 = m18.h();
        za.k.d(h23, "kotlinReadOnly.packageFqName");
        ClassId classId7 = new ClassId(h22, oc.e.g(fqName8, h23), false);
        ClassId d10 = ClassId.m(fqName7).d(StandardNames.a.Z.g());
        za.k.d(d10, "topLevel(FqNames.map).cr…mes.mapEntry.shortName())");
        FqName fqName9 = StandardNames.a.f15304h0;
        FqName h24 = d10.h();
        FqName h25 = d10.h();
        za.k.d(h25, "kotlinReadOnly.packageFqName");
        m10 = r.m(new a(javaToKotlinClassMap.g(Iterable.class), m12, classId), new a(javaToKotlinClassMap.g(Iterator.class), m13, classId2), new a(javaToKotlinClassMap.g(Collection.class), m14, classId3), new a(javaToKotlinClassMap.g(List.class), m15, classId4), new a(javaToKotlinClassMap.g(Set.class), m16, classId5), new a(javaToKotlinClassMap.g(ListIterator.class), m17, classId6), new a(javaToKotlinClassMap.g(Map.class), m18, classId7), new a(javaToKotlinClassMap.g(Map.Entry.class), d10, new ClassId(h24, oc.e.g(fqName9, h25), false)));
        f16355q = m10;
        javaToKotlinClassMap.f(Object.class, StandardNames.a.f15291b);
        javaToKotlinClassMap.f(String.class, StandardNames.a.f15303h);
        javaToKotlinClassMap.f(CharSequence.class, StandardNames.a.f15301g);
        javaToKotlinClassMap.e(Throwable.class, StandardNames.a.f15329u);
        javaToKotlinClassMap.f(Cloneable.class, StandardNames.a.f15295d);
        javaToKotlinClassMap.f(Number.class, StandardNames.a.f15323r);
        javaToKotlinClassMap.e(Comparable.class, StandardNames.a.f15331v);
        javaToKotlinClassMap.f(Enum.class, StandardNames.a.f15325s);
        javaToKotlinClassMap.e(Annotation.class, StandardNames.a.G);
        Iterator<a> it = m10.iterator();
        while (it.hasNext()) {
            f16339a.d(it.next());
        }
        for (JvmPrimitiveType jvmPrimitiveType : JvmPrimitiveType.values()) {
            JavaToKotlinClassMap javaToKotlinClassMap2 = f16339a;
            ClassId m19 = ClassId.m(jvmPrimitiveType.g());
            za.k.d(m19, "topLevel(jvmType.wrapperFqName)");
            PrimitiveType f10 = jvmPrimitiveType.f();
            za.k.d(f10, "jvmType.primitiveType");
            ClassId m20 = ClassId.m(StandardNames.c(f10));
            za.k.d(m20, "topLevel(StandardNames.g…e(jvmType.primitiveType))");
            javaToKotlinClassMap2.a(m19, m20);
        }
        for (ClassId classId8 : CompanionObjectMapping.f15213a.a()) {
            JavaToKotlinClassMap javaToKotlinClassMap3 = f16339a;
            ClassId m21 = ClassId.m(new FqName("kotlin.jvm.internal." + classId8.j().b() + "CompanionObject"));
            za.k.d(m21, "topLevel(FqName(\"kotlin.…g() + \"CompanionObject\"))");
            ClassId d11 = classId8.d(SpecialNames.f16449d);
            za.k.d(d11, "classId.createNestedClas…AME_FOR_COMPANION_OBJECT)");
            javaToKotlinClassMap3.a(m21, d11);
        }
        for (int i10 = 0; i10 < 23; i10++) {
            JavaToKotlinClassMap javaToKotlinClassMap4 = f16339a;
            ClassId m22 = ClassId.m(new FqName("kotlin.jvm.functions.Function" + i10));
            za.k.d(m22, "topLevel(FqName(\"kotlin.…m.functions.Function$i\"))");
            javaToKotlinClassMap4.a(m22, StandardNames.a(i10));
            javaToKotlinClassMap4.c(new FqName(f16341c + i10), f16346h);
        }
        for (int i11 = 0; i11 < 22; i11++) {
            FunctionClassKind functionClassKind5 = FunctionClassKind.f15972m;
            f16339a.c(new FqName((functionClassKind5.c().toString() + '.' + functionClassKind5.b()) + i11), f16346h);
        }
        JavaToKotlinClassMap javaToKotlinClassMap5 = f16339a;
        FqName l10 = StandardNames.a.f15293c.l();
        za.k.d(l10, "nothing.toSafe()");
        javaToKotlinClassMap5.c(l10, javaToKotlinClassMap5.g(Void.class));
    }

    private JavaToKotlinClassMap() {
    }

    private final void a(ClassId classId, ClassId classId2) {
        b(classId, classId2);
        FqName b10 = classId2.b();
        za.k.d(b10, "kotlinClassId.asSingleFqName()");
        c(b10, classId);
    }

    private final void b(ClassId classId, ClassId classId2) {
        HashMap<FqNameUnsafe, ClassId> hashMap = f16349k;
        FqNameUnsafe j10 = classId.b().j();
        za.k.d(j10, "javaClassId.asSingleFqName().toUnsafe()");
        hashMap.put(j10, classId2);
    }

    private final void c(FqName fqName, ClassId classId) {
        HashMap<FqNameUnsafe, ClassId> hashMap = f16350l;
        FqNameUnsafe j10 = fqName.j();
        za.k.d(j10, "kotlinFqNameUnsafe.toUnsafe()");
        hashMap.put(j10, classId);
    }

    private final void d(a aVar) {
        ClassId a10 = aVar.a();
        ClassId b10 = aVar.b();
        ClassId c10 = aVar.c();
        a(a10, b10);
        FqName b11 = c10.b();
        za.k.d(b11, "mutableClassId.asSingleFqName()");
        c(b11, a10);
        f16353o.put(c10, b10);
        f16354p.put(b10, c10);
        FqName b12 = b10.b();
        za.k.d(b12, "readOnlyClassId.asSingleFqName()");
        FqName b13 = c10.b();
        za.k.d(b13, "mutableClassId.asSingleFqName()");
        HashMap<FqNameUnsafe, FqName> hashMap = f16351m;
        FqNameUnsafe j10 = c10.b().j();
        za.k.d(j10, "mutableClassId.asSingleFqName().toUnsafe()");
        hashMap.put(j10, b12);
        HashMap<FqNameUnsafe, FqName> hashMap2 = f16352n;
        FqNameUnsafe j11 = b12.j();
        za.k.d(j11, "readOnlyFqName.toUnsafe()");
        hashMap2.put(j11, b13);
    }

    private final void e(Class<?> cls, FqName fqName) {
        ClassId g6 = g(cls);
        ClassId m10 = ClassId.m(fqName);
        za.k.d(m10, "topLevel(kotlinFqName)");
        a(g6, m10);
    }

    private final void f(Class<?> cls, FqNameUnsafe fqNameUnsafe) {
        FqName l10 = fqNameUnsafe.l();
        za.k.d(l10, "kotlinFqName.toSafe()");
        e(cls, l10);
    }

    private final ClassId g(Class<?> cls) {
        if (!cls.isPrimitive()) {
            cls.isArray();
        }
        Class<?> declaringClass = cls.getDeclaringClass();
        if (declaringClass == null) {
            ClassId m10 = ClassId.m(new FqName(cls.getCanonicalName()));
            za.k.d(m10, "topLevel(FqName(clazz.canonicalName))");
            return m10;
        }
        ClassId d10 = g(declaringClass).d(Name.f(cls.getSimpleName()));
        za.k.d(d10, "classId(outer).createNes…tifier(clazz.simpleName))");
        return d10;
    }

    private final boolean j(FqNameUnsafe fqNameUnsafe, String str) {
        String z02;
        boolean v02;
        Integer j10;
        String b10 = fqNameUnsafe.b();
        za.k.d(b10, "kotlinFqName.asString()");
        z02 = v.z0(b10, str, "");
        if (z02.length() > 0) {
            v02 = v.v0(z02, '0', false, 2, null);
            if (!v02) {
                j10 = StringNumberConversions.j(z02);
                return j10 != null && j10.intValue() >= 23;
            }
        }
        return false;
    }

    public final FqName h() {
        return f16345g;
    }

    public final List<a> i() {
        return f16355q;
    }

    public final boolean k(FqNameUnsafe fqNameUnsafe) {
        return f16351m.containsKey(fqNameUnsafe);
    }

    public final boolean l(FqNameUnsafe fqNameUnsafe) {
        return f16352n.containsKey(fqNameUnsafe);
    }

    public final ClassId m(FqName fqName) {
        za.k.e(fqName, "fqName");
        return f16349k.get(fqName.j());
    }

    public final ClassId n(FqNameUnsafe fqNameUnsafe) {
        za.k.e(fqNameUnsafe, "kotlinFqName");
        if (!j(fqNameUnsafe, f16340b) && !j(fqNameUnsafe, f16342d)) {
            if (!j(fqNameUnsafe, f16341c) && !j(fqNameUnsafe, f16343e)) {
                return f16350l.get(fqNameUnsafe);
            }
            return f16346h;
        }
        return f16344f;
    }

    public final FqName o(FqNameUnsafe fqNameUnsafe) {
        return f16351m.get(fqNameUnsafe);
    }

    public final FqName p(FqNameUnsafe fqNameUnsafe) {
        return f16352n.get(fqNameUnsafe);
    }
}
