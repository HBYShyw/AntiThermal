package lc;

import jc.c;
import jc.j;
import jc.k;
import jc.x;
import qc.j;

/* compiled from: Flags.java */
/* renamed from: lc.b, reason: use source file name */
/* loaded from: classes2.dex */
public class Flags {
    public static final b A;
    public static final b B;
    public static final b C;
    public static final b D;
    public static final b E;
    public static final b F;
    public static final b G;
    public static final b H;
    public static final b I;
    public static final b J;
    public static final b K;
    public static final b L;
    public static final b M;
    public static final b N;
    public static final b O;

    /* renamed from: a, reason: collision with root package name */
    public static final b f14666a;

    /* renamed from: b, reason: collision with root package name */
    public static final b f14667b;

    /* renamed from: c, reason: collision with root package name */
    public static final b f14668c;

    /* renamed from: d, reason: collision with root package name */
    public static final d<x> f14669d;

    /* renamed from: e, reason: collision with root package name */
    public static final d<k> f14670e;

    /* renamed from: f, reason: collision with root package name */
    public static final d<c.EnumC0065c> f14671f;

    /* renamed from: g, reason: collision with root package name */
    public static final b f14672g;

    /* renamed from: h, reason: collision with root package name */
    public static final b f14673h;

    /* renamed from: i, reason: collision with root package name */
    public static final b f14674i;

    /* renamed from: j, reason: collision with root package name */
    public static final b f14675j;

    /* renamed from: k, reason: collision with root package name */
    public static final b f14676k;

    /* renamed from: l, reason: collision with root package name */
    public static final b f14677l;

    /* renamed from: m, reason: collision with root package name */
    public static final b f14678m;

    /* renamed from: n, reason: collision with root package name */
    public static final b f14679n;

    /* renamed from: o, reason: collision with root package name */
    public static final d<j> f14680o;

    /* renamed from: p, reason: collision with root package name */
    public static final b f14681p;

    /* renamed from: q, reason: collision with root package name */
    public static final b f14682q;

    /* renamed from: r, reason: collision with root package name */
    public static final b f14683r;

    /* renamed from: s, reason: collision with root package name */
    public static final b f14684s;

    /* renamed from: t, reason: collision with root package name */
    public static final b f14685t;

    /* renamed from: u, reason: collision with root package name */
    public static final b f14686u;

    /* renamed from: v, reason: collision with root package name */
    public static final b f14687v;

    /* renamed from: w, reason: collision with root package name */
    public static final b f14688w;

    /* renamed from: x, reason: collision with root package name */
    public static final b f14689x;

    /* renamed from: y, reason: collision with root package name */
    public static final b f14690y;

    /* renamed from: z, reason: collision with root package name */
    public static final b f14691z;

    /* compiled from: Flags.java */
    /* renamed from: lc.b$b */
    /* loaded from: classes2.dex */
    public static class b extends d<Boolean> {
        public b(int i10) {
            super(i10, 1);
        }

        private static /* synthetic */ void f(int i10) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "kotlin/reflect/jvm/internal/impl/metadata/deserialization/Flags$BooleanFlagField", "get"));
        }

        @Override // lc.Flags.d
        /* renamed from: g, reason: merged with bridge method [inline-methods] */
        public Boolean d(int i10) {
            Boolean valueOf = Boolean.valueOf(((1 << this.f14693a) & i10) != 0);
            if (valueOf == null) {
                f(0);
            }
            return valueOf;
        }

        @Override // lc.Flags.d
        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public int e(Boolean bool) {
            if (bool.booleanValue()) {
                return 1 << this.f14693a;
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Flags.java */
    /* renamed from: lc.b$c */
    /* loaded from: classes2.dex */
    public static class c<E extends j.a> extends d<E> {

        /* renamed from: c, reason: collision with root package name */
        private final E[] f14692c;

        public c(int i10, E[] eArr) {
            super(i10, g(eArr));
            this.f14692c = eArr;
        }

        private static /* synthetic */ void f(int i10) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "enumEntries", "kotlin/reflect/jvm/internal/impl/metadata/deserialization/Flags$EnumLiteFlagField", "bitWidth"));
        }

        private static <E> int g(E[] eArr) {
            if (eArr == null) {
                f(0);
            }
            int length = eArr.length - 1;
            if (length == 0) {
                return 1;
            }
            for (int i10 = 31; i10 >= 0; i10--) {
                if (((1 << i10) & length) != 0) {
                    return i10 + 1;
                }
            }
            throw new IllegalStateException("Empty enum: " + eArr.getClass());
        }

        @Override // lc.Flags.d
        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public E d(int i10) {
            int i11 = (1 << this.f14694b) - 1;
            int i12 = this.f14693a;
            int i13 = (i10 & (i11 << i12)) >> i12;
            for (E e10 : this.f14692c) {
                if (e10.getNumber() == i13) {
                    return e10;
                }
            }
            return null;
        }

        @Override // lc.Flags.d
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public int e(E e10) {
            return e10.getNumber() << this.f14693a;
        }
    }

    /* compiled from: Flags.java */
    /* renamed from: lc.b$d */
    /* loaded from: classes2.dex */
    public static abstract class d<E> {

        /* renamed from: a, reason: collision with root package name */
        public final int f14693a;

        /* renamed from: b, reason: collision with root package name */
        public final int f14694b;

        /* JADX WARN: Incorrect types in method signature: <E::Lqc/j$a;>(Llc/b$d<*>;[TE;)Llc/b$d<TE;>; */
        public static d a(d dVar, j.a[] aVarArr) {
            return new c(dVar.f14693a + dVar.f14694b, aVarArr);
        }

        public static b b(d<?> dVar) {
            return new b(dVar.f14693a + dVar.f14694b);
        }

        public static b c() {
            return new b(0);
        }

        public abstract E d(int i10);

        public abstract int e(E e10);

        private d(int i10, int i11) {
            this.f14693a = i10;
            this.f14694b = i11;
        }
    }

    static {
        b c10 = d.c();
        f14666a = c10;
        f14667b = d.b(c10);
        b c11 = d.c();
        f14668c = c11;
        d<x> a10 = d.a(c11, x.values());
        f14669d = a10;
        d<k> a11 = d.a(a10, k.values());
        f14670e = a11;
        d<c.EnumC0065c> a12 = d.a(a11, c.EnumC0065c.values());
        f14671f = a12;
        b b10 = d.b(a12);
        f14672g = b10;
        b b11 = d.b(b10);
        f14673h = b11;
        b b12 = d.b(b11);
        f14674i = b12;
        b b13 = d.b(b12);
        f14675j = b13;
        b b14 = d.b(b13);
        f14676k = b14;
        f14677l = d.b(b14);
        b b15 = d.b(a10);
        f14678m = b15;
        f14679n = d.b(b15);
        d<jc.j> a13 = d.a(a11, jc.j.values());
        f14680o = a13;
        b b16 = d.b(a13);
        f14681p = b16;
        b b17 = d.b(b16);
        f14682q = b17;
        b b18 = d.b(b17);
        f14683r = b18;
        b b19 = d.b(b18);
        f14684s = b19;
        b b20 = d.b(b19);
        f14685t = b20;
        b b21 = d.b(b20);
        f14686u = b21;
        b b22 = d.b(b21);
        f14687v = b22;
        f14688w = d.b(b22);
        b b23 = d.b(a13);
        f14689x = b23;
        b b24 = d.b(b23);
        f14690y = b24;
        b b25 = d.b(b24);
        f14691z = b25;
        b b26 = d.b(b25);
        A = b26;
        b b27 = d.b(b26);
        B = b27;
        b b28 = d.b(b27);
        C = b28;
        b b29 = d.b(b28);
        D = b29;
        b b30 = d.b(b29);
        E = b30;
        F = d.b(b30);
        b b31 = d.b(c11);
        G = b31;
        b b32 = d.b(b31);
        H = b32;
        I = d.b(b32);
        b b33 = d.b(a11);
        J = b33;
        b b34 = d.b(b33);
        K = b34;
        L = d.b(b34);
        b c12 = d.c();
        M = c12;
        N = d.b(c12);
        O = d.c();
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void a(int i10) {
        Object[] objArr = new Object[3];
        if (i10 != 1) {
            if (i10 == 2) {
                objArr[0] = "kind";
            } else if (i10 != 5) {
                if (i10 != 6) {
                    if (i10 != 8) {
                        if (i10 != 9) {
                            if (i10 != 11) {
                                objArr[0] = "visibility";
                            }
                        }
                    }
                }
                objArr[0] = "memberKind";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/metadata/deserialization/Flags";
            switch (i10) {
                case 3:
                    objArr[2] = "getConstructorFlags";
                    break;
                case 4:
                case 5:
                case 6:
                    objArr[2] = "getFunctionFlags";
                    break;
                case 7:
                case 8:
                case 9:
                    objArr[2] = "getPropertyFlags";
                    break;
                case 10:
                case 11:
                    objArr[2] = "getAccessorFlags";
                    break;
                default:
                    objArr[2] = "getClassFlags";
                    break;
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }
        objArr[0] = "modality";
        objArr[1] = "kotlin/reflect/jvm/internal/impl/metadata/deserialization/Flags";
        switch (i10) {
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
    }

    public static int b(boolean z10, x xVar, k kVar, boolean z11, boolean z12, boolean z13) {
        if (xVar == null) {
            a(10);
        }
        if (kVar == null) {
            a(11);
        }
        return f14668c.e(Boolean.valueOf(z10)) | f14670e.e(kVar) | f14669d.e(xVar) | J.e(Boolean.valueOf(z11)) | K.e(Boolean.valueOf(z12)) | L.e(Boolean.valueOf(z13));
    }
}
