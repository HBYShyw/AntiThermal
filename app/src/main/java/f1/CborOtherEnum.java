package f1;

/* compiled from: CborOtherEnum.java */
/* renamed from: f1.o, reason: use source file name */
/* loaded from: classes.dex */
public enum CborOtherEnum {
    SIMPLE_VALUE,
    SIMPLE_VALUE_FOLLOWING_BYTE,
    IEEE_754_HALF_PRECISION_FLOAT,
    IEEE_754_SINGLE_PRECISION_FLOAT,
    IEEE_754_DOUBLE_PRECISION_FLOAT,
    RESERVED,
    BREAK;

    public static CborOtherEnum a(int i10) {
        int i11 = i10 & 31;
        if (i11 < 24) {
            return SIMPLE_VALUE;
        }
        if (i11 == 24) {
            return SIMPLE_VALUE_FOLLOWING_BYTE;
        }
        if (i11 == 25) {
            return IEEE_754_HALF_PRECISION_FLOAT;
        }
        if (i11 == 26) {
            return IEEE_754_SINGLE_PRECISION_FLOAT;
        }
        if (i11 == 27) {
            return IEEE_754_DOUBLE_PRECISION_FLOAT;
        }
        if (i11 == 31) {
            return BREAK;
        }
        return RESERVED;
    }
}
