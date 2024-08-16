package vendor.pixelworks.hardware.display.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class MemcMode {
    public static final int BYPASS = 21;
    public static final int BYPASS2PT = 22;
    public static final int DSI_SWITCH_2PT = 24;
    public static final int DSI_SWITCH_2RFB = 25;
    public static final int FRC = 3;
    public static final int FRC2RFB = 11;
    public static final int FRC_CANCEL = 4;
    public static final int FRC_POST = 26;
    public static final int FRC_PREPARE = 1;
    public static final int FRC_PREPARE_DONE = 2;
    public static final int FRC_PREPARE_RFB = 5;
    public static final int FRC_PREPARE_TIMEOUT = 6;
    public static final int KICKOFF60_DISABLE = 19;
    public static final int KICKOFF60_ENABLE = 18;
    public static final int PT = 17;
    public static final int PT2BYPASS = 20;
    public static final int PT2RFB = 16;
    public static final int PTLOW_PREPARE = 23;
    public static final int PT_PREPARE = 12;
    public static final int PT_PREPARE_DONE = 13;
    public static final int PT_PREPARE_TIMEOUT = 14;
    public static final int RFB = 0;
    public static final int RFB2FRC = 7;
    public static final int RFB2PT = 15;
    public static final int RFB_POST = 28;
    public static final int RFB_PREPARE = 8;
    public static final int RFB_PREPARE_DELAY = 27;
    public static final int RFB_PREPARE_DONE = 9;
    public static final int RFB_PREPARE_TIMEOUT = 10;

    public static final String toString(int i) {
        if (i == 0) {
            return "RFB";
        }
        if (i == 1) {
            return "FRC_PREPARE";
        }
        if (i == 2) {
            return "FRC_PREPARE_DONE";
        }
        if (i == 3) {
            return "FRC";
        }
        if (i == 4) {
            return "FRC_CANCEL";
        }
        if (i == 5) {
            return "FRC_PREPARE_RFB";
        }
        if (i == 6) {
            return "FRC_PREPARE_TIMEOUT";
        }
        if (i == 7) {
            return "RFB2FRC";
        }
        if (i == 8) {
            return "RFB_PREPARE";
        }
        if (i == 9) {
            return "RFB_PREPARE_DONE";
        }
        if (i == 10) {
            return "RFB_PREPARE_TIMEOUT";
        }
        if (i == 11) {
            return "FRC2RFB";
        }
        if (i == 12) {
            return "PT_PREPARE";
        }
        if (i == 13) {
            return "PT_PREPARE_DONE";
        }
        if (i == 14) {
            return "PT_PREPARE_TIMEOUT";
        }
        if (i == 15) {
            return "RFB2PT";
        }
        if (i == 16) {
            return "PT2RFB";
        }
        if (i == 17) {
            return "PT";
        }
        if (i == 18) {
            return "KICKOFF60_ENABLE";
        }
        if (i == 19) {
            return "KICKOFF60_DISABLE";
        }
        if (i == 20) {
            return "PT2BYPASS";
        }
        if (i == 21) {
            return "BYPASS";
        }
        if (i == 22) {
            return "BYPASS2PT";
        }
        if (i == 23) {
            return "PTLOW_PREPARE";
        }
        if (i == 24) {
            return "DSI_SWITCH_2PT";
        }
        if (i == 25) {
            return "DSI_SWITCH_2RFB";
        }
        if (i == 26) {
            return "FRC_POST";
        }
        if (i == 27) {
            return "RFB_PREPARE_DELAY";
        }
        if (i == 28) {
            return "RFB_POST";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("RFB");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("FRC_PREPARE");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("FRC_PREPARE_DONE");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("FRC");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("FRC_CANCEL");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("FRC_PREPARE_RFB");
            i2 |= 5;
        }
        if ((i & 6) == 6) {
            arrayList.add("FRC_PREPARE_TIMEOUT");
            i2 |= 6;
        }
        if ((i & 7) == 7) {
            arrayList.add("RFB2FRC");
            i2 |= 7;
        }
        if ((i & 8) == 8) {
            arrayList.add("RFB_PREPARE");
            i2 |= 8;
        }
        if ((i & 9) == 9) {
            arrayList.add("RFB_PREPARE_DONE");
            i2 |= 9;
        }
        if ((i & 10) == 10) {
            arrayList.add("RFB_PREPARE_TIMEOUT");
            i2 |= 10;
        }
        if ((i & 11) == 11) {
            arrayList.add("FRC2RFB");
            i2 |= 11;
        }
        if ((i & 12) == 12) {
            arrayList.add("PT_PREPARE");
            i2 |= 12;
        }
        if ((i & 13) == 13) {
            arrayList.add("PT_PREPARE_DONE");
            i2 |= 13;
        }
        if ((i & 14) == 14) {
            arrayList.add("PT_PREPARE_TIMEOUT");
            i2 |= 14;
        }
        if ((i & 15) == 15) {
            arrayList.add("RFB2PT");
            i2 |= 15;
        }
        if ((i & 16) == 16) {
            arrayList.add("PT2RFB");
            i2 |= 16;
        }
        if ((i & 17) == 17) {
            arrayList.add("PT");
            i2 |= 17;
        }
        if ((i & 18) == 18) {
            arrayList.add("KICKOFF60_ENABLE");
            i2 |= 18;
        }
        if ((i & 19) == 19) {
            arrayList.add("KICKOFF60_DISABLE");
            i2 |= 19;
        }
        if ((i & 20) == 20) {
            arrayList.add("PT2BYPASS");
            i2 |= 20;
        }
        if ((i & 21) == 21) {
            arrayList.add("BYPASS");
            i2 |= 21;
        }
        if ((i & 22) == 22) {
            arrayList.add("BYPASS2PT");
            i2 |= 22;
        }
        if ((i & 23) == 23) {
            arrayList.add("PTLOW_PREPARE");
            i2 |= 23;
        }
        if ((i & 24) == 24) {
            arrayList.add("DSI_SWITCH_2PT");
            i2 |= 24;
        }
        if ((i & 25) == 25) {
            arrayList.add("DSI_SWITCH_2RFB");
            i2 |= 25;
        }
        if ((i & 26) == 26) {
            arrayList.add("FRC_POST");
            i2 |= 26;
        }
        if ((i & 27) == 27) {
            arrayList.add("RFB_PREPARE_DELAY");
            i2 |= 27;
        }
        if ((i & 28) == 28) {
            arrayList.add("RFB_POST");
            i2 |= 28;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
