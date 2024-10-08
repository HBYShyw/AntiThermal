package vendor.pixelworks.hardware.display.V1_2;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class VendorConfig {
    public static final int CALIBRATION = 263;
    public static final int FRAME_RATE_CHANGED = 269;
    public static final int GET_SECONDARY_DISPLAY = 268;
    public static final int SET_CALI_PATTERN = 265;
    public static final int SET_FRAMEBUFFER_RESOLUTION = 271;
    public static final int SET_GAME_MODE = 261;
    public static final int SET_HDR_FORMAL = 258;
    public static final int SET_HDR_SETTING = 260;
    public static final int SET_LOW_LATENCY = 270;
    public static final int SET_MEMC_SETTING = 266;
    public static final int SET_N2M_ENABLE = 262;
    public static final int SET_ORIENTATION = 257;
    public static final int SET_PANEL_INFO = 1024;
    public static final int SET_SDR2HDR_SETTING = 267;
    public static final int SET_SERVICE_DEBUG = 259;
    public static final int SET_WCG_GAMUT = 264;
    public static final int SR_ENABLE = 273;
    public static final int START_TRANSITION = 256;
    public static final int TYPE_MAX = 268;
    public static final int TYPE_MAX_V1_1 = 272;
    public static final int TYPE_MAX_V1_2 = 1279;
    public static final int TYPE_MIN_V1_2 = 1024;

    public static final String toString(int i) {
        if (i == 256) {
            return "START_TRANSITION";
        }
        if (i == 257) {
            return "SET_ORIENTATION";
        }
        if (i == 258) {
            return "SET_HDR_FORMAL";
        }
        if (i == 259) {
            return "SET_SERVICE_DEBUG";
        }
        if (i == 260) {
            return "SET_HDR_SETTING";
        }
        if (i == 261) {
            return "SET_GAME_MODE";
        }
        if (i == 262) {
            return "SET_N2M_ENABLE";
        }
        if (i == 263) {
            return "CALIBRATION";
        }
        if (i == 264) {
            return "SET_WCG_GAMUT";
        }
        if (i == 265) {
            return "SET_CALI_PATTERN";
        }
        if (i == 266) {
            return "SET_MEMC_SETTING";
        }
        if (i == 267) {
            return "SET_SDR2HDR_SETTING";
        }
        if (i == 268) {
            return "TYPE_MAX";
        }
        if (i == 268) {
            return "GET_SECONDARY_DISPLAY";
        }
        if (i == 269) {
            return "FRAME_RATE_CHANGED";
        }
        if (i == 270) {
            return "SET_LOW_LATENCY";
        }
        if (i == 271) {
            return "SET_FRAMEBUFFER_RESOLUTION";
        }
        if (i == 272) {
            return "TYPE_MAX_V1_1";
        }
        if (i == 273) {
            return "SR_ENABLE";
        }
        if (i == 1024) {
            return "TYPE_MIN_V1_2";
        }
        if (i == 1024) {
            return "SET_PANEL_INFO";
        }
        if (i == 1279) {
            return "TYPE_MAX_V1_2";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 256;
        if ((i & 256) == 256) {
            arrayList.add("START_TRANSITION");
        } else {
            i2 = 0;
        }
        if ((i & 257) == 257) {
            arrayList.add("SET_ORIENTATION");
            i2 |= 257;
        }
        if ((i & 258) == 258) {
            arrayList.add("SET_HDR_FORMAL");
            i2 |= 258;
        }
        if ((i & 259) == 259) {
            arrayList.add("SET_SERVICE_DEBUG");
            i2 |= 259;
        }
        if ((i & 260) == 260) {
            arrayList.add("SET_HDR_SETTING");
            i2 |= 260;
        }
        if ((i & 261) == 261) {
            arrayList.add("SET_GAME_MODE");
            i2 |= 261;
        }
        if ((i & 262) == 262) {
            arrayList.add("SET_N2M_ENABLE");
            i2 |= 262;
        }
        if ((i & 263) == 263) {
            arrayList.add("CALIBRATION");
            i2 |= 263;
        }
        if ((i & 264) == 264) {
            arrayList.add("SET_WCG_GAMUT");
            i2 |= 264;
        }
        if ((i & 265) == 265) {
            arrayList.add("SET_CALI_PATTERN");
            i2 |= 265;
        }
        if ((i & 266) == 266) {
            arrayList.add("SET_MEMC_SETTING");
            i2 |= 266;
        }
        if ((i & 267) == 267) {
            arrayList.add("SET_SDR2HDR_SETTING");
            i2 |= 267;
        }
        int i3 = i & 268;
        if (i3 == 268) {
            arrayList.add("TYPE_MAX");
            i2 |= 268;
        }
        if (i3 == 268) {
            arrayList.add("GET_SECONDARY_DISPLAY");
            i2 |= 268;
        }
        if ((i & 269) == 269) {
            arrayList.add("FRAME_RATE_CHANGED");
            i2 |= 269;
        }
        if ((i & 270) == 270) {
            arrayList.add("SET_LOW_LATENCY");
            i2 |= 270;
        }
        if ((i & 271) == 271) {
            arrayList.add("SET_FRAMEBUFFER_RESOLUTION");
            i2 |= 271;
        }
        if ((i & 272) == 272) {
            arrayList.add("TYPE_MAX_V1_1");
            i2 |= 272;
        }
        if ((i & 273) == 273) {
            arrayList.add("SR_ENABLE");
            i2 |= 273;
        }
        int i4 = i & 1024;
        if (i4 == 1024) {
            arrayList.add("TYPE_MIN_V1_2");
            i2 |= 1024;
        }
        if (i4 == 1024) {
            arrayList.add("SET_PANEL_INFO");
            i2 |= 1024;
        }
        if ((i & 1279) == 1279) {
            arrayList.add("TYPE_MAX_V1_2");
            i2 |= 1279;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
