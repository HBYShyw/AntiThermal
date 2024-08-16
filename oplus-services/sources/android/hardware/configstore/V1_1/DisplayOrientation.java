package android.hardware.configstore.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DisplayOrientation {
    public static final byte ORIENTATION_0 = 0;
    public static final byte ORIENTATION_180 = 2;
    public static final byte ORIENTATION_270 = 3;
    public static final byte ORIENTATION_90 = 1;

    public static final String toString(byte b) {
        if (b == 0) {
            return "ORIENTATION_0";
        }
        if (b == 1) {
            return "ORIENTATION_90";
        }
        if (b == 2) {
            return "ORIENTATION_180";
        }
        if (b == 3) {
            return "ORIENTATION_270";
        }
        return "0x" + Integer.toHexString(Byte.toUnsignedInt(b));
    }

    public static final String dumpBitfield(byte b) {
        byte b2;
        ArrayList arrayList = new ArrayList();
        arrayList.add("ORIENTATION_0");
        if ((b & 1) == 1) {
            arrayList.add("ORIENTATION_90");
            b2 = (byte) 1;
        } else {
            b2 = 0;
        }
        if ((b & 2) == 2) {
            arrayList.add("ORIENTATION_180");
            b2 = (byte) (b2 | 2);
        }
        if ((b & 3) == 3) {
            arrayList.add("ORIENTATION_270");
            b2 = (byte) (b2 | 3);
        }
        if (b != b2) {
            arrayList.add("0x" + Integer.toHexString(Byte.toUnsignedInt((byte) (b & (~b2)))));
        }
        return String.join(" | ", arrayList);
    }
}
