package com.oplus.internal.telephony.ratconfiguration;

import android.os.SystemProperties;
import android.telephony.Rlog;

/* loaded from: classes.dex */
public class RatConfiguration {
    public static final String CDMA = "C";
    public static final String DELIMITER = "/";
    public static final String GSM = "G";
    public static final String LOG_TAG = "RatConfig";
    public static final String LTEFDD = "Lf";
    public static final String LTETDD = "Lt";
    public static final int MASK_CDMA = 32;
    public static final int MASK_GSM = 1;
    public static final int MASK_LTEFDD = 16;
    public static final int MASK_LTETDD = 8;
    public static final int MASK_NR = 64;
    public static final int MASK_TDSCDMA = 2;
    public static final int MASK_WCDMA = 4;
    protected static final int MD_MODE_LCTG = 16;
    protected static final int MD_MODE_LFWCG = 15;
    protected static final int MD_MODE_LFWG = 14;
    protected static final int MD_MODE_LTCTG = 17;
    protected static final int MD_MODE_LTG = 8;
    protected static final int MD_MODE_LTTG = 13;
    protected static final int MD_MODE_LWCG = 11;
    protected static final int MD_MODE_LWCTG = 12;
    protected static final int MD_MODE_LWG = 9;
    protected static final int MD_MODE_LWTG = 10;
    protected static final int MD_MODE_UNKNOWN = 0;
    public static final String NR = "N";
    public static final String PROPERTY_BUILD_RAT_CONFIG = "ro.vendor.mtk_protocol1_rat_config";
    public static final String PROPERTY_IS_USING_DEFAULT_CONFIG = "ro.boot.opt_using_default";
    public static final String PROPERTY_RAT_CONFIG = "ro.vendor.mtk_ps1_rat";
    public static final String TDSCDMA = "T";
    public static final String WCDMA = "W";
    private static int sMaxRat = 0;
    private static boolean sMaxRatInitialized = false;
    private static int sActivedRat = 0;
    private static boolean sIsDefaultConfig = true;

    protected static int ratToBitmask(String rat) {
        int iRat = 0;
        if (rat.contains(CDMA)) {
            iRat = 0 | 32;
        }
        if (rat.contains(LTEFDD)) {
            iRat |= 16;
        }
        if (rat.contains(LTETDD)) {
            iRat |= 8;
        }
        if (rat.contains(WCDMA)) {
            iRat |= 4;
        }
        if (rat.contains(TDSCDMA)) {
            iRat |= 2;
        }
        if (rat.contains(GSM)) {
            iRat |= 1;
        }
        if (rat.contains(NR)) {
            return iRat | 64;
        }
        return iRat;
    }

    protected static synchronized int getMaxRat() {
        int i;
        synchronized (RatConfiguration.class) {
            if (!sMaxRatInitialized) {
                String maxRat = SystemProperties.get(PROPERTY_BUILD_RAT_CONFIG, "");
                sMaxRat = ratToBitmask(maxRat);
                sIsDefaultConfig = SystemProperties.getInt(PROPERTY_IS_USING_DEFAULT_CONFIG, 1) != 0;
                sMaxRatInitialized = true;
                logd("getMaxRat: initial " + sMaxRat + " " + sMaxRat);
            }
            i = sMaxRat;
        }
        return i;
    }

    protected static boolean checkRatConfig(int iRat) {
        int maxrat = getMaxRat();
        if ((iRat | maxrat) == maxrat) {
            return true;
        }
        logd("checkRatConfig: FAIL with " + String.valueOf(iRat));
        return false;
    }

    protected static int getRatConfig() {
        int defaultRatConfig = getMaxRat();
        if (defaultRatConfig == 0) {
            sActivedRat = 0;
            return 0;
        }
        if (sIsDefaultConfig) {
            sActivedRat = defaultRatConfig;
            return defaultRatConfig;
        }
        String rat = SystemProperties.get(PROPERTY_RAT_CONFIG, "");
        if (rat.length() > 0) {
            int ratToBitmask = ratToBitmask(rat);
            sActivedRat = ratToBitmask;
            if (!checkRatConfig(ratToBitmask)) {
                logd("getRatConfig: invalid PROPERTY_RAT_CONFIG, set to max_rat");
                sActivedRat = getMaxRat();
            }
        } else {
            logd("getRatConfig: ger property PROPERTY_RAT_CONFIG fail, initialize");
            sActivedRat = getMaxRat();
        }
        return sActivedRat;
    }

    protected static String ratToString(int iRat) {
        String rat = (iRat & 32) == 32 ? "/C" : "";
        if ((iRat & 16) == 16) {
            rat = rat + "/Lf";
        }
        if ((iRat & 8) == 8) {
            rat = rat + "/Lt";
        }
        if ((iRat & 4) == 4) {
            rat = rat + "/W";
        }
        if ((iRat & 2) == 2) {
            rat = rat + "/T";
        }
        if ((iRat & 1) == 1) {
            rat = rat + "/G";
        }
        if ((iRat & 64) == 64) {
            rat = rat + "/N";
        }
        if (rat.length() > 0) {
            return rat.substring(1);
        }
        return rat;
    }

    public static boolean isC2kSupported() {
        return ((getMaxRat() & getRatConfig()) & 32) == 32;
    }

    public static boolean isNrSupported() {
        return ((getMaxRat() & getRatConfig()) & 64) == 64;
    }

    public static String getActiveRatConfig() {
        String rat = ratToString(getRatConfig());
        logd("getActiveRatConfig: " + rat);
        return rat;
    }

    private static void logd(String msg) {
        Rlog.d(LOG_TAG, msg);
    }
}
