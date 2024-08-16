package mb;

import java.util.Set;
import kotlin.collections.s0;
import oc.FqName;
import oc.Name;
import za.DefaultConstructorMarker;
import za.Lambda;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v1 mb.i, still in use, count: 1, list:
  (r0v1 mb.i) from 0x006e: FILLED_NEW_ARRAY (r0v1 mb.i), (r1v2 mb.i), (r4v2 mb.i), (r6v2 mb.i), (r8v2 mb.i), (r10v2 mb.i), (r12v2 mb.i) A[WRAPPED] elemType: mb.i
	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:88)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:87)
	at jadx.core.utils.InsnRemover.removeAllAndUnbind(InsnRemover.java:238)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:180)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* compiled from: PrimitiveType.kt */
/* renamed from: mb.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class PrimitiveType {
    BOOLEAN("Boolean"),
    CHAR("Char"),
    BYTE("Byte"),
    SHORT("Short"),
    INT("Int"),
    FLOAT("Float"),
    LONG("Long"),
    DOUBLE("Double");


    /* renamed from: j, reason: collision with root package name */
    public static final Set<PrimitiveType> f15233j;

    /* renamed from: e, reason: collision with root package name */
    private final Name f15243e;

    /* renamed from: f, reason: collision with root package name */
    private final Name f15244f;

    /* renamed from: g, reason: collision with root package name */
    private final ma.h f15245g;

    /* renamed from: h, reason: collision with root package name */
    private final ma.h f15246h;

    /* renamed from: i, reason: collision with root package name */
    public static final a f15232i = new a(null);

    /* compiled from: PrimitiveType.kt */
    /* renamed from: mb.i$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: PrimitiveType.kt */
    /* renamed from: mb.i$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<FqName> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final FqName invoke() {
            FqName c10 = StandardNames.f15283u.c(PrimitiveType.this.c());
            za.k.d(c10, "BUILT_INS_PACKAGE_FQ_NAME.child(arrayTypeName)");
            return c10;
        }
    }

    /* compiled from: PrimitiveType.kt */
    /* renamed from: mb.i$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<FqName> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final FqName invoke() {
            FqName c10 = StandardNames.f15283u.c(PrimitiveType.this.e());
            za.k.d(c10, "BUILT_INS_PACKAGE_FQ_NAME.child(this.typeName)");
            return c10;
        }
    }

    static {
        Set<PrimitiveType> h10;
        h10 = s0.h(new PrimitiveType("Char"), new PrimitiveType("Byte"), new PrimitiveType("Short"), new PrimitiveType("Int"), new PrimitiveType("Float"), new PrimitiveType("Long"), new PrimitiveType("Double"));
        f15233j = h10;
    }

    private PrimitiveType(String str) {
        ma.h a10;
        ma.h a11;
        Name f10 = Name.f(str);
        za.k.d(f10, "identifier(typeName)");
        this.f15243e = f10;
        Name f11 = Name.f(str + "Array");
        za.k.d(f11, "identifier(\"${typeName}Array\")");
        this.f15244f = f11;
        ma.l lVar = ma.l.PUBLICATION;
        a10 = ma.j.a(lVar, new c());
        this.f15245g = a10;
        a11 = ma.j.a(lVar, new b());
        this.f15246h = a11;
    }

    public static PrimitiveType valueOf(String str) {
        return (PrimitiveType) Enum.valueOf(PrimitiveType.class, str);
    }

    public static PrimitiveType[] values() {
        return (PrimitiveType[]) f15242s.clone();
    }

    public final FqName b() {
        return (FqName) this.f15246h.getValue();
    }

    public final Name c() {
        return this.f15244f;
    }

    public final FqName d() {
        return (FqName) this.f15245g.getValue();
    }

    public final Name e() {
        return this.f15243e;
    }
}
