package com.android.server.display.util;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class OplusDisplayPanelFeatureHelper {
    public static final int FINGER_LAYER_HIDE = 0;
    public static final int FINGER_LAYER_SHOW = 1;
    public static final int MTK_FINGER_LAYER_HIDE = 1;
    public static final int MTK_FINGER_LAYER_SHOW = 0;
    public static final int OMMDPAOD = 7;
    public static final int OMMDPFFL = 6;
    public static final int OMMDPMAX_BRIGHTNESS = 8;
    public static final int OMMDPPANEL_ID = 5;
    public static final int OMMDPPANEL_INFO = 9;
    public static final int OMMDPPOWER_VDDI = 1;
    public static final int OMMDPPOWER_VDDR = 2;
    public static final int OMMDPPOWER_VGATE = 3;
    public static final int OMMDPSEED = 4;
    public static final int OMMDP_AUDIO_READY = 15;
    public static final int OMMDP_CABC_STATUS = 184;
    public static final int OMMDP_CCD_CHECK = 10;
    public static final int OMMDP_DIMLAYER_BL_EN = 23;
    public static final int OMMDP_DIMLAY_HBM = 22;
    public static final int OMMDP_DIM_ALPHA = 13;
    public static final int OMMDP_DIM_DC_ALPHA = 14;
    public static final int OMMDP_DOC = 27;
    public static final int OMMDP_DUMP_INFO = 16;
    public static final int OMMDP_FPPRESS = 28;
    public static final int OMMDP_HBM = 12;
    public static final int OMMDP_OPLUS_SHUTDOWN_FLAG = 210;
    public static final int OMMDP_PANEL_BLANK = 24;
    public static final int OMMDP_PANEL_DSC = 17;
    public static final int OMMDP_PANEL_REG = 21;
    public static final int OMMDP_POWER_STATUS = 18;
    public static final int OMMDP_PQ_TRIGGER = 68;
    public static final int OMMDP_PWM_PULSE = 201;
    public static final int OMMDP_PWM_TURBO = 199;
    public static final int OMMDP_REGULATOR_CONTROL = 19;
    public static final int OMMDP_ROUND_CORNER = 26;
    public static final int OMMDP_SAU_CLOSEBL = 20;
    public static final int OMMDP_SERIAL_NUMBER = 11;
    public static final int OMMDP_SPR = 25;
    private static final String TAG = "OplusDisplayPanelFeature";

    public static ArrayList<Integer> getDisplayPanelFeatureValue(int i) {
        if (AidlDisplayPanelFeature.isAvailable()) {
            return AidlDisplayPanelFeature.getDisplayPanelFeatureValue(i);
        }
        if (HidlDisplayPanelFeature.isAvailable()) {
            return HidlDisplayPanelFeature.getDisplayPanelFeatureValue(i);
        }
        return null;
    }

    public static String getDisplayPanelFeatureValueAsString(int i) {
        if (AidlDisplayPanelFeature.isAvailable()) {
            return AidlDisplayPanelFeature.getDisplayPanelFeatureValueAsString(i);
        }
        if (HidlDisplayPanelFeature.isAvailable()) {
            return HidlDisplayPanelFeature.getDisplayPanelFeatureValueAsString(i);
        }
        return null;
    }

    public static int[] getDisplayPanelFeatureValueAsIntArray(int i) {
        if (AidlDisplayPanelFeature.isAvailable()) {
            return AidlDisplayPanelFeature.getDisplayPanelFeatureValueAsIntArray(i);
        }
        if (HidlDisplayPanelFeature.isAvailable()) {
            return HidlDisplayPanelFeature.getDisplayPanelFeatureValueAsIntArray(i);
        }
        return null;
    }

    public static int getDisplayPanelFeatureValueAsInt(int i) {
        if (AidlDisplayPanelFeature.isAvailable()) {
            return AidlDisplayPanelFeature.getDisplayPanelFeatureValueAsInt(i);
        }
        if (HidlDisplayPanelFeature.isAvailable()) {
            return HidlDisplayPanelFeature.getDisplayPanelFeatureValueAsInt(i);
        }
        return -1;
    }

    public static boolean isFODHwDimlayer() {
        if (AidlDisplayPanelFeature.isAvailable()) {
            return AidlDisplayPanelFeature.isFODHwDimlayer();
        }
        if (HidlDisplayPanelFeature.isAvailable()) {
            return HidlDisplayPanelFeature.isFODHwDimlayer();
        }
        return false;
    }

    public static void setDisplayPanelFeatureValue(int i, int i2) {
        if (AidlDisplayPanelFeature.isAvailable()) {
            AidlDisplayPanelFeature.setDisplayPanelFeatureValue(i, i2);
        } else if (HidlDisplayPanelFeature.isAvailable()) {
            HidlDisplayPanelFeature.setDisplayPanelFeatureValue(i, i2);
        }
    }

    public static void setDisplayPanelFeatureValueArray(int i, int[] iArr) {
        if (AidlDisplayPanelFeature.isAvailable()) {
            AidlDisplayPanelFeature.setDisplayPanelFeatureValueArray(i, iArr);
        } else if (HidlDisplayPanelFeature.isAvailable()) {
            HidlDisplayPanelFeature.setDisplayPanelFeatureValueArray(i, iArr);
        }
    }

    public static int setDisplayPanelFeatureValueForMtk(int i, int i2) {
        if (AidlDisplayPanelFeature.isAvailable()) {
            return AidlDisplayPanelFeature.setDisplayPanelFeatureValueForMtk(i, i2);
        }
        if (HidlDisplayPanelFeature.isAvailable()) {
            return HidlDisplayPanelFeature.setDisplayPanelFeatureValueForMtk(i, i2);
        }
        return -1;
    }

    public static ArrayList<String> getDisplayPanelInfo(int i) {
        if (AidlDisplayPanelFeature.isAvailable()) {
            return AidlDisplayPanelFeature.getDisplayPanelInfo(i);
        }
        if (HidlDisplayPanelFeature.isAvailable()) {
            return HidlDisplayPanelFeature.getDisplayPanelInfo(i);
        }
        return null;
    }

    public static String getDisplayPanelInfoAsString(int i) {
        if (AidlDisplayPanelFeature.isAvailable()) {
            return AidlDisplayPanelFeature.getDisplayPanelInfoAsString(i);
        }
        if (HidlDisplayPanelFeature.isAvailable()) {
            return HidlDisplayPanelFeature.getDisplayPanelInfoAsString(i);
        }
        return null;
    }
}
