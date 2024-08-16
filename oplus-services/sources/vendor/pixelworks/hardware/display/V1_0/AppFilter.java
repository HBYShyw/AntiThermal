package vendor.pixelworks.hardware.display.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class AppFilter {
    public static final int BLACK = 1;
    public static final int NORMAL = 0;
    public static final int WHITE = 2;

    public static final String toString(int i) {
        if (i == 0) {
            return "NORMAL";
        }
        if (i == 1) {
            return "BLACK";
        }
        if (i == 2) {
            return "WHITE";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("NORMAL");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("BLACK");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("WHITE");
            i2 |= 2;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
