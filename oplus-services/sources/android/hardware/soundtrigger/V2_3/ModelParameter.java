package android.hardware.soundtrigger.V2_3;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ModelParameter {
    public static final int INVALID = -1;
    public static final int THRESHOLD_FACTOR = 0;

    public static final String toString(int i) {
        if (i == -1) {
            return "INVALID";
        }
        if (i == 0) {
            return "THRESHOLD_FACTOR";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = -1;
        if ((i & (-1)) == -1) {
            arrayList.add("INVALID");
        } else {
            i2 = 0;
        }
        arrayList.add("THRESHOLD_FACTOR");
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
