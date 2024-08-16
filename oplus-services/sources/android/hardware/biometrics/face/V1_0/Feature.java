package android.hardware.biometrics.face.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class Feature {
    public static final int REQUIRE_ATTENTION = 1;
    public static final int REQUIRE_DIVERSITY = 2;

    public static final String toString(int i) {
        if (i == 1) {
            return "REQUIRE_ATTENTION";
        }
        if (i == 2) {
            return "REQUIRE_DIVERSITY";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("REQUIRE_ATTENTION");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("REQUIRE_DIVERSITY");
            i2 |= 2;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
