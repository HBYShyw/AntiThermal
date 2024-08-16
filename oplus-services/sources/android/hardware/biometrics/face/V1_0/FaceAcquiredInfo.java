package android.hardware.biometrics.face.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class FaceAcquiredInfo {
    public static final int FACE_OBSCURED = 19;
    public static final int FACE_TOO_HIGH = 6;
    public static final int FACE_TOO_LEFT = 9;
    public static final int FACE_TOO_LOW = 7;
    public static final int FACE_TOO_RIGHT = 8;
    public static final int GOOD = 0;
    public static final int INSUFFICIENT = 1;
    public static final int NOT_DETECTED = 11;
    public static final int PAN_TOO_EXTREME = 16;
    public static final int POOR_GAZE = 10;
    public static final int RECALIBRATE = 13;
    public static final int ROLL_TOO_EXTREME = 18;
    public static final int SENSOR_DIRTY = 21;
    public static final int START = 20;
    public static final int TILT_TOO_EXTREME = 17;
    public static final int TOO_BRIGHT = 2;
    public static final int TOO_CLOSE = 4;
    public static final int TOO_DARK = 3;
    public static final int TOO_DIFFERENT = 14;
    public static final int TOO_FAR = 5;
    public static final int TOO_MUCH_MOTION = 12;
    public static final int TOO_SIMILAR = 15;
    public static final int VENDOR = 22;

    public static final String toString(int i) {
        if (i == 0) {
            return "GOOD";
        }
        if (i == 1) {
            return "INSUFFICIENT";
        }
        if (i == 2) {
            return "TOO_BRIGHT";
        }
        if (i == 3) {
            return "TOO_DARK";
        }
        if (i == 4) {
            return "TOO_CLOSE";
        }
        if (i == 5) {
            return "TOO_FAR";
        }
        if (i == 6) {
            return "FACE_TOO_HIGH";
        }
        if (i == 7) {
            return "FACE_TOO_LOW";
        }
        if (i == 8) {
            return "FACE_TOO_RIGHT";
        }
        if (i == 9) {
            return "FACE_TOO_LEFT";
        }
        if (i == 10) {
            return "POOR_GAZE";
        }
        if (i == 11) {
            return "NOT_DETECTED";
        }
        if (i == 12) {
            return "TOO_MUCH_MOTION";
        }
        if (i == 13) {
            return "RECALIBRATE";
        }
        if (i == 14) {
            return "TOO_DIFFERENT";
        }
        if (i == 15) {
            return "TOO_SIMILAR";
        }
        if (i == 16) {
            return "PAN_TOO_EXTREME";
        }
        if (i == 17) {
            return "TILT_TOO_EXTREME";
        }
        if (i == 18) {
            return "ROLL_TOO_EXTREME";
        }
        if (i == 19) {
            return "FACE_OBSCURED";
        }
        if (i == 20) {
            return "START";
        }
        if (i == 21) {
            return "SENSOR_DIRTY";
        }
        if (i == 22) {
            return "VENDOR";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("GOOD");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("INSUFFICIENT");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("TOO_BRIGHT");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("TOO_DARK");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("TOO_CLOSE");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("TOO_FAR");
            i2 |= 5;
        }
        if ((i & 6) == 6) {
            arrayList.add("FACE_TOO_HIGH");
            i2 |= 6;
        }
        if ((i & 7) == 7) {
            arrayList.add("FACE_TOO_LOW");
            i2 |= 7;
        }
        if ((i & 8) == 8) {
            arrayList.add("FACE_TOO_RIGHT");
            i2 |= 8;
        }
        if ((i & 9) == 9) {
            arrayList.add("FACE_TOO_LEFT");
            i2 |= 9;
        }
        if ((i & 10) == 10) {
            arrayList.add("POOR_GAZE");
            i2 |= 10;
        }
        if ((i & 11) == 11) {
            arrayList.add("NOT_DETECTED");
            i2 |= 11;
        }
        if ((i & 12) == 12) {
            arrayList.add("TOO_MUCH_MOTION");
            i2 |= 12;
        }
        if ((i & 13) == 13) {
            arrayList.add("RECALIBRATE");
            i2 |= 13;
        }
        if ((i & 14) == 14) {
            arrayList.add("TOO_DIFFERENT");
            i2 |= 14;
        }
        if ((i & 15) == 15) {
            arrayList.add("TOO_SIMILAR");
            i2 |= 15;
        }
        if ((i & 16) == 16) {
            arrayList.add("PAN_TOO_EXTREME");
            i2 |= 16;
        }
        if ((i & 17) == 17) {
            arrayList.add("TILT_TOO_EXTREME");
            i2 |= 17;
        }
        if ((i & 18) == 18) {
            arrayList.add("ROLL_TOO_EXTREME");
            i2 |= 18;
        }
        if ((i & 19) == 19) {
            arrayList.add("FACE_OBSCURED");
            i2 |= 19;
        }
        if ((i & 20) == 20) {
            arrayList.add("START");
            i2 |= 20;
        }
        if ((i & 21) == 21) {
            arrayList.add("SENSOR_DIRTY");
            i2 |= 21;
        }
        if ((i & 22) == 22) {
            arrayList.add("VENDOR");
            i2 |= 22;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
