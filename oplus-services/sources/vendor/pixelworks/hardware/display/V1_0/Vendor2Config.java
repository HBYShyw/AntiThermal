package vendor.pixelworks.hardware.display.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class Vendor2Config {
    public static final int BYPASS_MODE = 520;
    public static final int CALIBRATION = 518;
    public static final int CM_COLOR_GAMUT = 516;
    public static final int CM_COLOR_TEMP_MODE = 515;
    public static final int COLOR_TEMP_VALUE = 517;
    public static final int DATA_GAIN = 512;
    public static final int DISPLAY_BRIGHTNESS = 514;
    public static final int FORCE_LUT = 513;
    public static final int PQ_TARGET = 519;
    public static final int TYPE_MAX = 521;

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
        if ((i & 521) == 521) {
            arrayList.add("TYPE_MAX");
            i2 |= 521;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
