package mb;

import oc.ClassId;
import oc.Name;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'h' uses external variables
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
/* compiled from: UnsignedType.kt */
/* loaded from: classes2.dex */
public final class n {

    /* renamed from: h, reason: collision with root package name */
    public static final n f15349h;

    /* renamed from: i, reason: collision with root package name */
    public static final n f15350i;

    /* renamed from: j, reason: collision with root package name */
    public static final n f15351j;

    /* renamed from: k, reason: collision with root package name */
    public static final n f15352k;

    /* renamed from: l, reason: collision with root package name */
    private static final /* synthetic */ n[] f15353l;

    /* renamed from: e, reason: collision with root package name */
    private final ClassId f15354e;

    /* renamed from: f, reason: collision with root package name */
    private final Name f15355f;

    /* renamed from: g, reason: collision with root package name */
    private final ClassId f15356g;

    static {
        ClassId e10 = ClassId.e("kotlin/UByte");
        za.k.d(e10, "fromString(\"kotlin/UByte\")");
        f15349h = new n("UBYTE", 0, e10);
        ClassId e11 = ClassId.e("kotlin/UShort");
        za.k.d(e11, "fromString(\"kotlin/UShort\")");
        f15350i = new n("USHORT", 1, e11);
        ClassId e12 = ClassId.e("kotlin/UInt");
        za.k.d(e12, "fromString(\"kotlin/UInt\")");
        f15351j = new n("UINT", 2, e12);
        ClassId e13 = ClassId.e("kotlin/ULong");
        za.k.d(e13, "fromString(\"kotlin/ULong\")");
        f15352k = new n("ULONG", 3, e13);
        f15353l = a();
    }

    private n(String str, int i10, ClassId classId) {
        this.f15354e = classId;
        Name j10 = classId.j();
        za.k.d(j10, "classId.shortClassName");
        this.f15355f = j10;
        this.f15356g = new ClassId(classId.h(), Name.f(j10.b() + "Array"));
    }

    private static final /* synthetic */ n[] a() {
        return new n[]{f15349h, f15350i, f15351j, f15352k};
    }

    public static n valueOf(String str) {
        return (n) Enum.valueOf(n.class, str);
    }

    public static n[] values() {
        return (n[]) f15353l.clone();
    }

    public final ClassId b() {
        return this.f15356g;
    }

    public final ClassId c() {
        return this.f15354e;
    }

    public final Name d() {
        return this.f15355f;
    }
}
