package nb;

import mb.StandardNames;
import oc.FqName;
import oc.Name;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import za.k;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'l' uses external variables
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
/* compiled from: FunctionClassKind.kt */
/* renamed from: nb.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class FunctionClassKind {

    /* renamed from: i, reason: collision with root package name */
    public static final a f15968i;

    /* renamed from: j, reason: collision with root package name */
    public static final FunctionClassKind f15969j = new FunctionClassKind("Function", 0, StandardNames.f15283u, "Function", false, false);

    /* renamed from: k, reason: collision with root package name */
    public static final FunctionClassKind f15970k = new FunctionClassKind("SuspendFunction", 1, StandardNames.f15275m, "SuspendFunction", true, false);

    /* renamed from: l, reason: collision with root package name */
    public static final FunctionClassKind f15971l;

    /* renamed from: m, reason: collision with root package name */
    public static final FunctionClassKind f15972m;

    /* renamed from: n, reason: collision with root package name */
    private static final /* synthetic */ FunctionClassKind[] f15973n;

    /* renamed from: e, reason: collision with root package name */
    private final FqName f15974e;

    /* renamed from: f, reason: collision with root package name */
    private final String f15975f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f15976g;

    /* renamed from: h, reason: collision with root package name */
    private final boolean f15977h;

    /* compiled from: FunctionClassKind.kt */
    /* renamed from: nb.c$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: FunctionClassKind.kt */
        /* renamed from: nb.c$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0082a {

            /* renamed from: a, reason: collision with root package name */
            private final FunctionClassKind f15978a;

            /* renamed from: b, reason: collision with root package name */
            private final int f15979b;

            public C0082a(FunctionClassKind functionClassKind, int i10) {
                k.e(functionClassKind, "kind");
                this.f15978a = functionClassKind;
                this.f15979b = i10;
            }

            public final FunctionClassKind a() {
                return this.f15978a;
            }

            public final int b() {
                return this.f15979b;
            }

            public final FunctionClassKind c() {
                return this.f15978a;
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof C0082a)) {
                    return false;
                }
                C0082a c0082a = (C0082a) obj;
                return this.f15978a == c0082a.f15978a && this.f15979b == c0082a.f15979b;
            }

            public int hashCode() {
                return (this.f15978a.hashCode() * 31) + Integer.hashCode(this.f15979b);
            }

            public String toString() {
                return "KindWithArity(kind=" + this.f15978a + ", arity=" + this.f15979b + ')';
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final Integer d(String str) {
            if (str.length() == 0) {
                return null;
            }
            int length = str.length();
            int i10 = 0;
            for (int i11 = 0; i11 < length; i11++) {
                int charAt = str.charAt(i11) - '0';
                if (!(charAt >= 0 && charAt < 10)) {
                    return null;
                }
                i10 = (i10 * 10) + charAt;
            }
            return Integer.valueOf(i10);
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0032 A[LOOP:0: B:2:0x0011->B:10:0x0032, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:11:0x0030 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final FunctionClassKind a(FqName fqName, String str) {
            boolean z10;
            boolean D;
            k.e(fqName, "packageFqName");
            k.e(str, "className");
            for (FunctionClassKind functionClassKind : FunctionClassKind.values()) {
                if (k.a(functionClassKind.c(), fqName)) {
                    D = StringsJVM.D(str, functionClassKind.b(), false, 2, null);
                    if (D) {
                        z10 = true;
                        if (!z10) {
                            return functionClassKind;
                        }
                    }
                }
                z10 = false;
                if (!z10) {
                }
            }
            return null;
        }

        public final FunctionClassKind b(String str, FqName fqName) {
            k.e(str, "className");
            k.e(fqName, "packageFqName");
            C0082a c10 = c(str, fqName);
            if (c10 != null) {
                return c10.c();
            }
            return null;
        }

        public final C0082a c(String str, FqName fqName) {
            k.e(str, "className");
            k.e(fqName, "packageFqName");
            FunctionClassKind a10 = a(fqName, str);
            if (a10 == null) {
                return null;
            }
            String substring = str.substring(a10.b().length());
            k.d(substring, "this as java.lang.String).substring(startIndex)");
            Integer d10 = d(substring);
            if (d10 != null) {
                return new C0082a(a10, d10.intValue());
            }
            return null;
        }
    }

    static {
        FqName fqName = StandardNames.f15280r;
        f15971l = new FunctionClassKind("KFunction", 2, fqName, "KFunction", false, true);
        f15972m = new FunctionClassKind("KSuspendFunction", 3, fqName, "KSuspendFunction", true, true);
        f15973n = a();
        f15968i = new a(null);
    }

    private FunctionClassKind(String str, int i10, FqName fqName, String str2, boolean z10, boolean z11) {
        this.f15974e = fqName;
        this.f15975f = str2;
        this.f15976g = z10;
        this.f15977h = z11;
    }

    private static final /* synthetic */ FunctionClassKind[] a() {
        return new FunctionClassKind[]{f15969j, f15970k, f15971l, f15972m};
    }

    public static FunctionClassKind valueOf(String str) {
        return (FunctionClassKind) Enum.valueOf(FunctionClassKind.class, str);
    }

    public static FunctionClassKind[] values() {
        return (FunctionClassKind[]) f15973n.clone();
    }

    public final String b() {
        return this.f15975f;
    }

    public final FqName c() {
        return this.f15974e;
    }

    public final Name d(int i10) {
        Name f10 = Name.f(this.f15975f + i10);
        k.d(f10, "identifier(\"$classNamePrefix$arity\")");
        return f10;
    }
}
