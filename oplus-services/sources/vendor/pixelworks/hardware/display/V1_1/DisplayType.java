package vendor.pixelworks.hardware.display.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class DisplayType {
    public static final long DISPLAY_BUILTIN_2 = 3;
    public static final long DISPLAY_EXTERNAL = 1;
    public static final long DISPLAY_EXTERNAL_2 = 4;
    public static final long DISPLAY_MAX = 5;
    public static final long DISPLAY_PLUGGABLE = 1;
    public static final long DISPLAY_PRIMARY = 0;
    public static final long DISPLAY_SECONDARY = 3;
    public static final long DISPLAY_VIRTUAL = 2;

    public static final String toString(long j) {
        if (j == 0) {
            return "DISPLAY_PRIMARY";
        }
        if (j == 1) {
            return "DISPLAY_EXTERNAL";
        }
        if (j == 1) {
            return "DISPLAY_PLUGGABLE";
        }
        if (j == 2) {
            return "DISPLAY_VIRTUAL";
        }
        if (j == 3) {
            return "DISPLAY_BUILTIN_2";
        }
        if (j == 3) {
            return "DISPLAY_SECONDARY";
        }
        if (j == 4) {
            return "DISPLAY_EXTERNAL_2";
        }
        if (j == 5) {
            return "DISPLAY_MAX";
        }
        return "0x" + Long.toHexString(j);
    }

    public static final String dumpBitfield(long j) {
        long j2;
        ArrayList arrayList = new ArrayList();
        arrayList.add("DISPLAY_PRIMARY");
        long j3 = j & 1;
        if (j3 == 1) {
            arrayList.add("DISPLAY_EXTERNAL");
            j2 = 1;
        } else {
            j2 = 0;
        }
        if (j3 == 1) {
            arrayList.add("DISPLAY_PLUGGABLE");
            j2 |= 1;
        }
        if ((j & 2) == 2) {
            arrayList.add("DISPLAY_VIRTUAL");
            j2 |= 2;
        }
        long j4 = j & 3;
        if (j4 == 3) {
            arrayList.add("DISPLAY_BUILTIN_2");
            j2 |= 3;
        }
        if (j4 == 3) {
            arrayList.add("DISPLAY_SECONDARY");
            j2 |= 3;
        }
        if ((j & 4) == 4) {
            arrayList.add("DISPLAY_EXTERNAL_2");
            j2 |= 4;
        }
        if ((j & 5) == 5) {
            arrayList.add("DISPLAY_MAX");
            j2 |= 5;
        }
        if (j != j2) {
            arrayList.add("0x" + Long.toHexString(j & (~j2)));
        }
        return String.join(" | ", arrayList);
    }
}
