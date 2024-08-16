package oplus.telephony;

import android.telephony.TelephonyManager;

/* loaded from: classes.dex */
public class TelephonyManagerExt {
    public static boolean isConcurrentCallsPossible() {
        return TelephonyManager.isConcurrentCallsPossible();
    }

    public static boolean isDsdsMode() {
        return TelephonyManager.isDsdsMode();
    }

    public boolean isDsdaOrDsdsTransitionMode() {
        return TelephonyManager.getDefault().isDsdaOrDsdsTransitionMode();
    }

    public boolean isDsdsTransitionMode() {
        return TelephonyManager.getDefault().isDsdsTransitionMode();
    }

    public static boolean isDsdsTransitionSupported() {
        return TelephonyManager.isDsdsTransitionSupported();
    }
}
