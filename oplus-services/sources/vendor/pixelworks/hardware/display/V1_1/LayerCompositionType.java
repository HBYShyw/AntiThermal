package vendor.pixelworks.hardware.display.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LayerCompositionType {
    public static final int COMPOSITION_TYPE_GPU = 0;
    public static final int COMPOSITION_TYPE_HWC = 2;

    public static final String toString(int i) {
        if (i == 0) {
            return "COMPOSITION_TYPE_GPU";
        }
        if (i == 2) {
            return "COMPOSITION_TYPE_HWC";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("COMPOSITION_TYPE_GPU");
        int i2 = 2;
        if ((i & 2) == 2) {
            arrayList.add("COMPOSITION_TYPE_HWC");
        } else {
            i2 = 0;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
