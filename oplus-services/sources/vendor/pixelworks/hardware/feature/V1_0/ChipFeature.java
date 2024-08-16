package vendor.pixelworks.hardware.feature.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class ChipFeature {
    public static final int IRIS2 = 0;
    public static final int IRIS2_PLUS = 1;
    public static final int IRIS3 = 2;
    public static final int IRIS5 = 3;
    public static final int IRIS5_DUAL = 6;
    public static final int IRIS6 = 4;
    public static final int IRIS7 = 7;
    public static final int IRIS7_DUAL = 8;
    public static final int IRISSOFT = 5;
    public static final int IRIS_FPGA = 30;

    public static final String toString(int i) {
        if (i == 0) {
            return "IRIS2";
        }
        if (i == 1) {
            return "IRIS2_PLUS";
        }
        if (i == 2) {
            return "IRIS3";
        }
        if (i == 3) {
            return "IRIS5";
        }
        if (i == 4) {
            return "IRIS6";
        }
        if (i == 5) {
            return "IRISSOFT";
        }
        if (i == 6) {
            return "IRIS5_DUAL";
        }
        if (i == 7) {
            return "IRIS7";
        }
        if (i == 8) {
            return "IRIS7_DUAL";
        }
        if (i == 30) {
            return "IRIS_FPGA";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("IRIS2");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("IRIS2_PLUS");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("IRIS3");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("IRIS5");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("IRIS6");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("IRISSOFT");
            i2 |= 5;
        }
        if ((i & 6) == 6) {
            arrayList.add("IRIS5_DUAL");
            i2 |= 6;
        }
        if ((i & 7) == 7) {
            arrayList.add("IRIS7");
            i2 |= 7;
        }
        if ((i & 8) == 8) {
            arrayList.add("IRIS7_DUAL");
            i2 |= 8;
        }
        if ((i & 30) == 30) {
            arrayList.add("IRIS_FPGA");
            i2 |= 30;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
