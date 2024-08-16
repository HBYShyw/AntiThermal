package f1;

/* compiled from: AdditionalInformationEnum.java */
/* renamed from: f1.a, reason: use source file name */
/* loaded from: classes.dex */
public enum AdditionalInformationEnum {
    DIRECT(0),
    ONE_BYTE(24),
    TWO_BYTES(25),
    FOUR_BYTES(26),
    EIGHT_BYTES(27),
    RESERVED(28),
    INDEFINITE(31);


    /* renamed from: e, reason: collision with root package name */
    private final int f11264e;

    AdditionalInformationEnum(int i10) {
        this.f11264e = i10;
    }

    public static AdditionalInformationEnum a(int i10) {
        int i11 = i10 & 31;
        if (i11 < 24) {
            return DIRECT;
        }
        if (i11 == 24) {
            return ONE_BYTE;
        }
        if (i11 == 25) {
            return TWO_BYTES;
        }
        if (i11 == 26) {
            return FOUR_BYTES;
        }
        if (i11 == 27) {
            return EIGHT_BYTES;
        }
        if (i11 == 31) {
            return INDEFINITE;
        }
        throw new CborException("Undefined additional info value " + i11);
    }

    public int b() {
        return this.f11264e;
    }
}
