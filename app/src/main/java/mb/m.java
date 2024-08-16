package mb;

import oc.ClassId;
import oc.Name;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'g' uses external variables
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
public final class m {

    /* renamed from: g, reason: collision with root package name */
    public static final m f15342g;

    /* renamed from: h, reason: collision with root package name */
    public static final m f15343h;

    /* renamed from: i, reason: collision with root package name */
    public static final m f15344i;

    /* renamed from: j, reason: collision with root package name */
    public static final m f15345j;

    /* renamed from: k, reason: collision with root package name */
    private static final /* synthetic */ m[] f15346k;

    /* renamed from: e, reason: collision with root package name */
    private final ClassId f15347e;

    /* renamed from: f, reason: collision with root package name */
    private final Name f15348f;

    static {
        ClassId e10 = ClassId.e("kotlin/UByteArray");
        za.k.d(e10, "fromString(\"kotlin/UByteArray\")");
        f15342g = new m("UBYTEARRAY", 0, e10);
        ClassId e11 = ClassId.e("kotlin/UShortArray");
        za.k.d(e11, "fromString(\"kotlin/UShortArray\")");
        f15343h = new m("USHORTARRAY", 1, e11);
        ClassId e12 = ClassId.e("kotlin/UIntArray");
        za.k.d(e12, "fromString(\"kotlin/UIntArray\")");
        f15344i = new m("UINTARRAY", 2, e12);
        ClassId e13 = ClassId.e("kotlin/ULongArray");
        za.k.d(e13, "fromString(\"kotlin/ULongArray\")");
        f15345j = new m("ULONGARRAY", 3, e13);
        f15346k = a();
    }

    private m(String str, int i10, ClassId classId) {
        this.f15347e = classId;
        Name j10 = classId.j();
        za.k.d(j10, "classId.shortClassName");
        this.f15348f = j10;
    }

    private static final /* synthetic */ m[] a() {
        return new m[]{f15342g, f15343h, f15344i, f15345j};
    }

    public static m valueOf(String str) {
        return (m) Enum.valueOf(m.class, str);
    }

    public static m[] values() {
        return (m[]) f15346k.clone();
    }

    public final Name b() {
        return this.f15348f;
    }
}
