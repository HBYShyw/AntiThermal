package android.hardware.soundtrigger.V2_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SoundModelType {
    public static final int GENERIC = 1;
    public static final int KEYPHRASE = 0;
    public static final int UNKNOWN = -1;

    public static final String toString(int i) {
        if (i == -1) {
            return "UNKNOWN";
        }
        if (i == 0) {
            return "KEYPHRASE";
        }
        if (i == 1) {
            return "GENERIC";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = -1;
        if ((i & (-1)) == -1) {
            arrayList.add("UNKNOWN");
        } else {
            i2 = 0;
        }
        arrayList.add("KEYPHRASE");
        if ((i & 1) == 1) {
            arrayList.add("GENERIC");
            i2 |= 1;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
