package vendor.pixelworks.hardware.display.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LayerBufferType {
    public static final int BUFFER_TYPE_HDR_VIDEO = 7;
    public static final int BUFFER_TYPE_HW_VIDEO = 3;
    public static final int BUFFER_TYPE_UI = 0;
    public static final int BUFFER_TYPE_VIDEO = 1;

    public static final String toString(int i) {
        if (i == 0) {
            return "BUFFER_TYPE_UI";
        }
        if (i == 1) {
            return "BUFFER_TYPE_VIDEO";
        }
        if (i == 3) {
            return "BUFFER_TYPE_HW_VIDEO";
        }
        if (i == 7) {
            return "BUFFER_TYPE_HDR_VIDEO";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("BUFFER_TYPE_UI");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("BUFFER_TYPE_VIDEO");
        } else {
            i2 = 0;
        }
        if ((i & 3) == 3) {
            arrayList.add("BUFFER_TYPE_HW_VIDEO");
            i2 |= 3;
        }
        if ((i & 7) == 7) {
            arrayList.add("BUFFER_TYPE_HDR_VIDEO");
            i2 |= 7;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
