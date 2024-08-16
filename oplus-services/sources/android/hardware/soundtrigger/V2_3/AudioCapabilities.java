package android.hardware.soundtrigger.V2_3;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AudioCapabilities {
    public static final int ECHO_CANCELLATION = 1;
    public static final int NOISE_SUPPRESSION = 2;

    public static final String toString(int i) {
        if (i == 1) {
            return "ECHO_CANCELLATION";
        }
        if (i == 2) {
            return "NOISE_SUPPRESSION";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("ECHO_CANCELLATION");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("NOISE_SUPPRESSION");
            i2 |= 2;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
