package vendor.pixelworks.hardware.display.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class DualChannelState {
    public static final int CFGOFF = 5;
    public static final int CFGOFF_PRE = 4;
    public static final int CFGON = 3;
    public static final int CFGON_PRE = 2;
    public static final int PWROFF = 7;
    public static final int PWROFF_PREPARE = 6;
    public static final int PWRON = 1;
    public static final int PWRON_PREPARE = 0;

    public static final String toString(int i) {
        if (i == 0) {
            return "PWRON_PREPARE";
        }
        if (i == 1) {
            return "PWRON";
        }
        if (i == 2) {
            return "CFGON_PRE";
        }
        if (i == 3) {
            return "CFGON";
        }
        if (i == 4) {
            return "CFGOFF_PRE";
        }
        if (i == 5) {
            return "CFGOFF";
        }
        if (i == 6) {
            return "PWROFF_PREPARE";
        }
        if (i == 7) {
            return "PWROFF";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("PWRON_PREPARE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("PWRON");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("CFGON_PRE");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("CFGON");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("CFGOFF_PRE");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("CFGOFF");
            i2 |= 5;
        }
        if ((i & 6) == 6) {
            arrayList.add("PWROFF_PREPARE");
            i2 |= 6;
        }
        if ((i & 7) == 7) {
            arrayList.add("PWROFF");
            i2 |= 7;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
