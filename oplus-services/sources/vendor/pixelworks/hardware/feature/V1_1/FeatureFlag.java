package vendor.pixelworks.hardware.feature.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class FeatureFlag {
    public static final int ASIC_PLUS = 512;
    public static final int CHIP_TYPE = 255;
    public static final int FPGA = 256;
    public static final int SUPPORT_DUAL = 1;
    public static final int SUPPORT_EMV = 32;
    public static final int SUPPORT_HDR10 = 128;
    public static final int SUPPORT_IMV = 8;
    public static final int SUPPORT_IMV_DUAL = 16;
    public static final int SUPPORT_MEMC = 2;
    public static final int SUPPORT_MEMC_DUAL = 4;
    public static final int SUPPORT_SR = 64;

    public static final String toString(int i) {
        if (i == 1) {
            return "SUPPORT_DUAL";
        }
        if (i == 2) {
            return "SUPPORT_MEMC";
        }
        if (i == 4) {
            return "SUPPORT_MEMC_DUAL";
        }
        if (i == 8) {
            return "SUPPORT_IMV";
        }
        if (i == 16) {
            return "SUPPORT_IMV_DUAL";
        }
        if (i == 32) {
            return "SUPPORT_EMV";
        }
        if (i == 64) {
            return "SUPPORT_SR";
        }
        if (i == 128) {
            return "SUPPORT_HDR10";
        }
        if (i == 255) {
            return "CHIP_TYPE";
        }
        if (i == 256) {
            return "FPGA";
        }
        if (i == 512) {
            return "ASIC_PLUS";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("SUPPORT_DUAL");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("SUPPORT_MEMC");
            i2 |= 2;
        }
        if ((i & 4) == 4) {
            arrayList.add("SUPPORT_MEMC_DUAL");
            i2 |= 4;
        }
        if ((i & 8) == 8) {
            arrayList.add("SUPPORT_IMV");
            i2 |= 8;
        }
        if ((i & 16) == 16) {
            arrayList.add("SUPPORT_IMV_DUAL");
            i2 |= 16;
        }
        if ((i & 32) == 32) {
            arrayList.add("SUPPORT_EMV");
            i2 |= 32;
        }
        if ((i & 64) == 64) {
            arrayList.add("SUPPORT_SR");
            i2 |= 64;
        }
        if ((i & 128) == 128) {
            arrayList.add("SUPPORT_HDR10");
            i2 |= 128;
        }
        if ((i & 255) == 255) {
            arrayList.add("CHIP_TYPE");
            i2 |= 255;
        }
        if ((i & 256) == 256) {
            arrayList.add("FPGA");
            i2 |= 256;
        }
        if ((i & 512) == 512) {
            arrayList.add("ASIC_PLUS");
            i2 |= 512;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
