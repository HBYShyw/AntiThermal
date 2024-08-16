package vendor.pixelworks.hardware.display.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class HdrFormalType {
    public static final int HDR_FORMAL_HDR = 2;
    public static final int HDR_FORMAL_HDR_MEMC = 12;
    public static final int HDR_FORMAL_HDR_MEMC_DUAL2 = 42;
    public static final int HDR_FORMAL_HDR_SDR = 1;
    public static final int HDR_FORMAL_MEMC = 10;
    public static final int HDR_FORMAL_MEMC_DUAL2 = 40;
    public static final int HDR_FORMAL_NONE = 0;

    public static final String toString(int i) {
        if (i == 0) {
            return "HDR_FORMAL_NONE";
        }
        if (i == 1) {
            return "HDR_FORMAL_HDR_SDR";
        }
        if (i == 2) {
            return "HDR_FORMAL_HDR";
        }
        if (i == 10) {
            return "HDR_FORMAL_MEMC";
        }
        if (i == 12) {
            return "HDR_FORMAL_HDR_MEMC";
        }
        if (i == 40) {
            return "HDR_FORMAL_MEMC_DUAL2";
        }
        if (i == 42) {
            return "HDR_FORMAL_HDR_MEMC_DUAL2";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("HDR_FORMAL_NONE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("HDR_FORMAL_HDR_SDR");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("HDR_FORMAL_HDR");
            i2 |= 2;
        }
        if ((i & 10) == 10) {
            arrayList.add("HDR_FORMAL_MEMC");
            i2 |= 10;
        }
        if ((i & 12) == 12) {
            arrayList.add("HDR_FORMAL_HDR_MEMC");
            i2 |= 12;
        }
        if ((i & 40) == 40) {
            arrayList.add("HDR_FORMAL_MEMC_DUAL2");
            i2 |= 40;
        }
        if ((i & 42) == 42) {
            arrayList.add("HDR_FORMAL_HDR_MEMC_DUAL2");
            i2 |= 42;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
