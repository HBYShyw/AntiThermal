package f1;

/* compiled from: CborSimpleValueEnum.java */
/* renamed from: f1.r, reason: use source file name */
/* loaded from: classes.dex */
public enum CborSimpleValueEnum {
    FALSE(20),
    TRUE(21),
    NULL(22),
    UNDEFINED(23),
    RESERVED(0),
    UNASSIGNED(0);


    /* renamed from: e, reason: collision with root package name */
    private final int f11301e;

    CborSimpleValueEnum(int i10) {
        this.f11301e = i10;
    }

    public static CborSimpleValueEnum a(int i10) {
        switch (i10 & 31) {
            case 20:
                return FALSE;
            case 21:
                return TRUE;
            case 22:
                return NULL;
            case 23:
                return UNDEFINED;
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
                return RESERVED;
            default:
                return UNASSIGNED;
        }
    }

    public int b() {
        return this.f11301e;
    }
}
