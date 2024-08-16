package android.hardware.tv.cec.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class HdmiPortType {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    public static final String toString(int i) {
        if (i == 0) {
            return "INPUT";
        }
        if (i == 1) {
            return "OUTPUT";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("INPUT");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("OUTPUT");
        } else {
            i2 = 0;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
