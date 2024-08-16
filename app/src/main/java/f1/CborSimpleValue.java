package f1;

/* compiled from: CborSimpleValue.java */
/* renamed from: f1.q, reason: use source file name */
/* loaded from: classes.dex */
public class CborSimpleValue extends CborOther {

    /* renamed from: d, reason: collision with root package name */
    private final CborSimpleValueEnum f11292d;

    /* renamed from: e, reason: collision with root package name */
    private final int f11293e;

    public CborSimpleValue(CborSimpleValueEnum cborSimpleValueEnum) {
        super(CborOtherEnum.SIMPLE_VALUE);
        this.f11293e = cborSimpleValueEnum.b();
        this.f11292d = cborSimpleValueEnum;
    }

    public CborSimpleValueEnum h() {
        return this.f11292d;
    }

    public int i() {
        return this.f11293e;
    }

    @Override // f1.CborOther
    public String toString() {
        CborSimpleValueEnum cborSimpleValueEnum = this.f11292d;
        if (cborSimpleValueEnum != CborSimpleValueEnum.RESERVED && cborSimpleValueEnum != CborSimpleValueEnum.UNASSIGNED) {
            return cborSimpleValueEnum.toString();
        }
        return "simple(" + this.f11293e + ")";
    }

    public CborSimpleValue(CborSimpleValueEnum cborSimpleValueEnum, long j10) {
        this(cborSimpleValueEnum);
        e(j10);
    }

    public CborSimpleValue(int i10) {
        super(i10 <= 23 ? CborOtherEnum.SIMPLE_VALUE : CborOtherEnum.SIMPLE_VALUE_FOLLOWING_BYTE);
        this.f11292d = CborSimpleValueEnum.a(i10);
        this.f11293e = i10;
    }

    public CborSimpleValue(int i10, long j10) {
        this(i10);
        e(j10);
    }
}
