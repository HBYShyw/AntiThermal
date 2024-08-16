package oplus.util;

/* loaded from: classes.dex */
public class OplusSafeCenterFeature {
    private static boolean mAssociateStartFeature = true;

    public static boolean isAssociationStartEnabled() {
        return mAssociateStartFeature;
    }

    public static void setAssociationStartFeature(boolean enabled) {
        mAssociateStartFeature = enabled;
    }

    public static boolean isLaunchRecordEnabled() {
        return true;
    }
}
