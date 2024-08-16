package com.android.server.biometrics.sensors.face.aidl;

import android.hardware.biometrics.face.AuthenticationFrame;
import android.hardware.biometrics.face.BaseFrame;
import android.hardware.biometrics.face.Cell;
import android.hardware.biometrics.face.EnrollmentFrame;
import android.hardware.face.FaceAuthenticationFrame;
import android.hardware.face.FaceDataFrame;
import android.hardware.face.FaceEnrollCell;
import android.hardware.face.FaceEnrollFrame;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class AidlConversionUtils {
    private static final byte FACE_ACQUIRED_AUTH_SUCCESS = 114;
    private static final byte FACE_ACQUIRED_CAMERA_PREVIEW = 119;
    private static final byte FACE_ACQUIRED_ENROLL_KEEP = 113;
    private static final byte FACE_ACQUIRED_ENROLL_RESET = 115;
    private static final byte FACE_ACQUIRED_ENROLL_SUCCESS = 112;
    private static final byte FACE_ACQUIRED_HKER = 104;
    private static final byte FACE_ACQUIRED_NO_FACE = 101;
    private static final byte FACE_ACQUIRED_UPDATE_MODE = 116;
    private static final byte FACE_ACQUIRED_UPDATE_MODE_ALL_FAIL = 118;
    private static final byte FACE_ACQUIRED_UPDATE_MODE_PARTITAL_FAIL = 117;
    private static final byte PALMPRINT_ACQUIRED_AUTH_SUCCESS = 54;
    private static final byte PALMPRINT_ACQUIRED_CAMERA_PREVIEW = 59;
    private static final byte PALMPRINT_ACQUIRED_ENROLL_KEEP = 53;
    private static final byte PALMPRINT_ACQUIRED_ENROLL_RESET = 55;
    private static final byte PALMPRINT_ACQUIRED_ENROLL_SUCCESS = 52;
    private static final byte PALMPRINT_ACQUIRED_GOOD = 61;
    private static final byte PALMPRINT_ACQUIRED_HACKER = 44;
    private static final byte PALMPRINT_ACQUIRED_INSUFFICIENT = 62;
    private static final byte PALMPRINT_ACQUIRED_NOT_DETECTED = 72;
    private static final byte PALMPRINT_ACQUIRED_NO_FACE = 41;
    private static final byte PALMPRINT_ACQUIRED_POOR_GAZE = 71;
    private static final byte PALMPRINT_ACQUIRED_SENSOR_DIRTY = 82;
    private static final byte PALMPRINT_ACQUIRED_TOO_BRIGHT = 63;
    private static final byte PALMPRINT_ACQUIRED_TOO_CLOSE = 65;
    private static final byte PALMPRINT_ACQUIRED_TOO_DARK = 64;
    private static final byte PALMPRINT_ACQUIRED_TOO_DIFFERENT = 75;
    private static final byte PALMPRINT_ACQUIRED_TOO_FAR = 66;
    private static final byte PALMPRINT_ACQUIRED_TOO_HIGH = 67;
    private static final byte PALMPRINT_ACQUIRED_TOO_LEFT = 70;
    private static final byte PALMPRINT_ACQUIRED_TOO_LOW = 68;
    private static final byte PALMPRINT_ACQUIRED_TOO_MUCH_MOTION = 73;
    private static final byte PALMPRINT_ACQUIRED_TOO_RIGHT = 69;
    private static final byte PALMPRINT_ACQUIRED_UPDATE_MODE = 56;
    private static final byte PALMPRINT_ACQUIRED_UPDATE_MODE_ALL_FAIL = 58;
    private static final byte PALMPRINT_ACQUIRED_UPDATE_MODE_PARTITAL_FAIL = 57;
    private static final String TAG = "AidlConversionUtils";

    public static int toFrameworkAcquiredInfo(byte b) {
        int i = 41;
        if (b != 41) {
            i = 44;
            if (b != 44) {
                i = 75;
                if (b != 75) {
                    i = 82;
                    if (b != 82) {
                        i = 101;
                        if (b != 101) {
                            i = 104;
                            if (b != 104) {
                                switch (b) {
                                    case 1:
                                        return 0;
                                    case 2:
                                        return 1;
                                    case 3:
                                        return 2;
                                    case 4:
                                        return 3;
                                    case 5:
                                        return 4;
                                    case 6:
                                        return 5;
                                    case 7:
                                        return 6;
                                    case 8:
                                        return 7;
                                    case 9:
                                        return 8;
                                    case 10:
                                        return 9;
                                    case 11:
                                        return 10;
                                    case 12:
                                        return 11;
                                    case 13:
                                        return 12;
                                    case 14:
                                        return 13;
                                    case 15:
                                        return 14;
                                    case 16:
                                        return 15;
                                    case 17:
                                        return 16;
                                    case 18:
                                        return 17;
                                    case 19:
                                        return 18;
                                    case 20:
                                        return 19;
                                    case 21:
                                        return 20;
                                    case 22:
                                        return 21;
                                    case 23:
                                        return 22;
                                    case 24:
                                        return 24;
                                    case 25:
                                        return 25;
                                    case 26:
                                        return 26;
                                    default:
                                        switch (b) {
                                            case 52:
                                                return 52;
                                            case 53:
                                                return 53;
                                            case 54:
                                                return 54;
                                            case 55:
                                                return 55;
                                            case 56:
                                                return 56;
                                            case 57:
                                                return 57;
                                            case 58:
                                                return 58;
                                            case 59:
                                                return 59;
                                            default:
                                                switch (b) {
                                                    case 61:
                                                        return 61;
                                                    case 62:
                                                        return 62;
                                                    case 63:
                                                        return 63;
                                                    case 64:
                                                        return 64;
                                                    case 65:
                                                        return 65;
                                                    case 66:
                                                        return 66;
                                                    case 67:
                                                        return 67;
                                                    case 68:
                                                        return 68;
                                                    case 69:
                                                        return 69;
                                                    case 70:
                                                        return 70;
                                                    case 71:
                                                        return 71;
                                                    case 72:
                                                        return 72;
                                                    case 73:
                                                        return 73;
                                                    default:
                                                        switch (b) {
                                                            case 112:
                                                                return 112;
                                                            case 113:
                                                                return 113;
                                                            case 114:
                                                                return 114;
                                                            case 115:
                                                                return 115;
                                                            case 116:
                                                                return 116;
                                                            case 117:
                                                                return 117;
                                                            case 118:
                                                                return 118;
                                                            case 119:
                                                                return 119;
                                                            default:
                                                                return 23;
                                                        }
                                                }
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
        return i;
    }

    public static int toFrameworkEnrollmentStage(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return 0;
        }
    }

    public static int toFrameworkError(byte b) {
        switch (b) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 8;
            case 8:
                return 16;
            default:
                return 17;
        }
    }

    private AidlConversionUtils() {
    }

    public static FaceAuthenticationFrame toFrameworkAuthenticationFrame(AuthenticationFrame authenticationFrame) {
        return new FaceAuthenticationFrame(toFrameworkBaseFrame(authenticationFrame.data));
    }

    public static FaceEnrollFrame toFrameworkEnrollmentFrame(EnrollmentFrame enrollmentFrame) {
        return new FaceEnrollFrame(toFrameworkCell(enrollmentFrame.cell), toFrameworkEnrollmentStage(enrollmentFrame.stage), toFrameworkBaseFrame(enrollmentFrame.data));
    }

    public static FaceDataFrame toFrameworkBaseFrame(BaseFrame baseFrame) {
        return new FaceDataFrame(toFrameworkAcquiredInfo(baseFrame.acquiredInfo), baseFrame.vendorCode, baseFrame.pan, baseFrame.tilt, baseFrame.distance, baseFrame.isCancellable);
    }

    public static FaceEnrollCell toFrameworkCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        return new FaceEnrollCell(cell.x, cell.y, cell.z);
    }

    public static byte convertFrameworkToAidlFeature(int i) throws IllegalArgumentException {
        if (i == 1) {
            return (byte) 0;
        }
        if (i == 2) {
            return (byte) 1;
        }
        Slog.e(TAG, "Unsupported feature : " + i);
        throw new IllegalArgumentException();
    }

    public static int convertAidlToFrameworkFeature(byte b) throws IllegalArgumentException {
        if (b == 0) {
            return 1;
        }
        if (b == 1) {
            return 2;
        }
        Slog.e(TAG, "Unsupported feature : " + ((int) b));
        throw new IllegalArgumentException();
    }
}
