package vendor.pixelworks.hardware.display.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class Vendor2Config {
    public static final int BRIGHTNESS_BLEND_CURVE = 525;
    public static final int BYPASS_MODE = 520;
    public static final int CALIBRATION = 518;
    public static final int CM_COLOR_GAMUT = 516;
    public static final int CM_COLOR_TEMP_MODE = 515;
    public static final int CM_RATIO_SET = 526;
    public static final int COLOR_TEMP_VALUE = 517;
    public static final int DATA_GAIN = 512;
    public static final int DISPLAY_BRIGHTNESS = 514;
    public static final int FORCE_LUT = 513;
    public static final int GAMUT_BLEND_GAIN = 529;
    public static final int PANEL_APL_VALUE = 527;
    public static final int PANEL_OEM_ID = 524;
    public static final int PANEL_POWER_MODE = 522;
    public static final int PANEL_REFRESH_RATE = 521;
    public static final int PQ_TARGET = 519;
    public static final int SET_FEATURE = 528;
    public static final int TYPE_MAX = 521;
    public static final int TYPE_MAX_V1_1 = 530;
    public static final int WHITE_POINT_SHIFT = 523;

    public static final String toString(int i) {
        if (i == 512) {
            return "DATA_GAIN";
        }
        if (i == 513) {
            return "FORCE_LUT";
        }
        if (i == 514) {
            return "DISPLAY_BRIGHTNESS";
        }
        if (i == 515) {
            return "CM_COLOR_TEMP_MODE";
        }
        if (i == 516) {
            return "CM_COLOR_GAMUT";
        }
        if (i == 517) {
            return "COLOR_TEMP_VALUE";
        }
        if (i == 518) {
            return "CALIBRATION";
        }
        if (i == 519) {
            return "PQ_TARGET";
        }
        if (i == 520) {
            return "BYPASS_MODE";
        }
        if (i == 521) {
            return "TYPE_MAX";
        }
        if (i == 521) {
            return "PANEL_REFRESH_RATE";
        }
        if (i == 522) {
            return "PANEL_POWER_MODE";
        }
        if (i == 523) {
            return "WHITE_POINT_SHIFT";
        }
        if (i == 524) {
            return "PANEL_OEM_ID";
        }
        if (i == 525) {
            return "BRIGHTNESS_BLEND_CURVE";
        }
        if (i == 526) {
            return "CM_RATIO_SET";
        }
        if (i == 527) {
            return "PANEL_APL_VALUE";
        }
        if (i == 528) {
            return "SET_FEATURE";
        }
        if (i == 529) {
            return "GAMUT_BLEND_GAIN";
        }
        if (i == 530) {
            return "TYPE_MAX_V1_1";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 512;
        if ((i & 512) == 512) {
            arrayList.add("DATA_GAIN");
        } else {
            i2 = 0;
        }
        if ((i & 513) == 513) {
            arrayList.add("FORCE_LUT");
            i2 |= 513;
        }
        if ((i & 514) == 514) {
            arrayList.add("DISPLAY_BRIGHTNESS");
            i2 |= 514;
        }
        if ((i & 515) == 515) {
            arrayList.add("CM_COLOR_TEMP_MODE");
            i2 |= 515;
        }
        if ((i & 516) == 516) {
            arrayList.add("CM_COLOR_GAMUT");
            i2 |= 516;
        }
        if ((i & 517) == 517) {
            arrayList.add("COLOR_TEMP_VALUE");
            i2 |= 517;
        }
        if ((i & 518) == 518) {
            arrayList.add("CALIBRATION");
            i2 |= 518;
        }
        if ((i & 519) == 519) {
            arrayList.add("PQ_TARGET");
            i2 |= 519;
        }
        if ((i & 520) == 520) {
            arrayList.add("BYPASS_MODE");
            i2 |= 520;
        }
        int i3 = i & 521;
        if (i3 == 521) {
            arrayList.add("TYPE_MAX");
            i2 |= 521;
        }
        if (i3 == 521) {
            arrayList.add("PANEL_REFRESH_RATE");
            i2 |= 521;
        }
        if ((i & 522) == 522) {
            arrayList.add("PANEL_POWER_MODE");
            i2 |= 522;
        }
        if ((i & 523) == 523) {
            arrayList.add("WHITE_POINT_SHIFT");
            i2 |= 523;
        }
        if ((i & 524) == 524) {
            arrayList.add("PANEL_OEM_ID");
            i2 |= 524;
        }
        if ((i & 525) == 525) {
            arrayList.add("BRIGHTNESS_BLEND_CURVE");
            i2 |= 525;
        }
        if ((i & 526) == 526) {
            arrayList.add("CM_RATIO_SET");
            i2 |= 526;
        }
        if ((i & 527) == 527) {
            arrayList.add("PANEL_APL_VALUE");
            i2 |= 527;
        }
        if ((i & 528) == 528) {
            arrayList.add("SET_FEATURE");
            i2 |= 528;
        }
        if ((i & 529) == 529) {
            arrayList.add("GAMUT_BLEND_GAIN");
            i2 |= 529;
        }
        if ((i & 530) == 530) {
            arrayList.add("TYPE_MAX_V1_1");
            i2 |= 530;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
