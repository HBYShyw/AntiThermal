package android.graphics;

/* loaded from: classes.dex */
public class OplusDarkModeThirdPartyFilter extends ColorFilter {
    public static final int TYPE_MAKE_DARK_RANGE = 2;
    public static final int TYPE_MAKE_FULL_DARK = 3;
    public static final int TYPE_MAKE_FULL_LIGHT = 4;
    public static final int TYPE_MAKE_LIGHT_RANGE = 1;
    public static final int TYPE_UNKNOWN = 0;
    private float mHSLBgMaxL;
    private float mHSLFgMinL;
    private float mLABBgMaxL;
    private float mLABFgMinL;
    private int mType;

    private static native long native_CreateDarkModeThirdPartyFilter(int i, float f, float f2);

    public OplusDarkModeThirdPartyFilter(int type, float HSLBgMaxL, float HSLFgMinL, float LABBgMaxL, float LABFgMinL) {
        this.mType = type;
        this.mHSLBgMaxL = HSLBgMaxL;
        this.mHSLFgMinL = HSLFgMinL;
        this.mLABBgMaxL = LABBgMaxL;
        this.mLABFgMinL = LABFgMinL;
    }

    public float getLABBgMaxL() {
        return this.mLABBgMaxL;
    }

    public float getLABFgMinL() {
        return this.mLABFgMinL;
    }

    public int getType() {
        return this.mType;
    }

    long createNativeInstance() {
        int i = this.mType;
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
                return native_CreateDarkModeThirdPartyFilter(i, this.mHSLBgMaxL, this.mHSLFgMinL);
            default:
                return 0L;
        }
    }
}
