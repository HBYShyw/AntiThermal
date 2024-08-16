package vendor.oplus.hardware.felica.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class FelicaStatus {
    public static final byte FAILED = 1;
    public static final byte IOERROR = 3;
    public static final byte SUCCESS = 0;
    public static final byte UNSUPPORTED_OPERATION = 2;

    public static final String toString(byte b) {
        if (b == 0) {
            return "SUCCESS";
        }
        if (b == 1) {
            return "FAILED";
        }
        if (b == 2) {
            return "UNSUPPORTED_OPERATION";
        }
        if (b == 3) {
            return "IOERROR";
        }
        return "0x" + Integer.toHexString(Byte.toUnsignedInt(b));
    }

    public static final String dumpBitfield(byte b) {
        byte b2;
        ArrayList arrayList = new ArrayList();
        arrayList.add("SUCCESS");
        if ((b & 1) == 1) {
            arrayList.add("FAILED");
            b2 = (byte) 1;
        } else {
            b2 = 0;
        }
        if ((b & 2) == 2) {
            arrayList.add("UNSUPPORTED_OPERATION");
            b2 = (byte) (b2 | 2);
        }
        if ((b & 3) == 3) {
            arrayList.add("IOERROR");
            b2 = (byte) (b2 | 3);
        }
        if (b != b2) {
            arrayList.add("0x" + Integer.toHexString(Byte.toUnsignedInt((byte) (b & (~b2)))));
        }
        return String.join(" | ", arrayList);
    }
}
