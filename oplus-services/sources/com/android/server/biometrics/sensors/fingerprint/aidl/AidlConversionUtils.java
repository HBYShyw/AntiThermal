package com.android.server.biometrics.sensors.fingerprint.aidl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class AidlConversionUtils {
    private static final int FINGERPRINT_ACQUIRED_ALREADY_ENROLLED = 1002;
    private static final byte FINGERPRINT_ACQUIRED_ALREADY_ENROLLED_AIDL = 102;
    private static final int FINGERPRINT_ACQUIRED_TOO_SIMILAR = 1001;
    private static final byte FINGERPRINT_ACQUIRED_TOO_SIMILAR_AIDL = 101;

    public static int toFrameworkAcquiredInfo(byte b) {
        if (b == 0) {
            return 8;
        }
        if (b == 1) {
            return 0;
        }
        if (b == 2) {
            return 1;
        }
        if (b == 3) {
            return 2;
        }
        if (b == 4) {
            return 3;
        }
        if (b == 5) {
            return 4;
        }
        if (b == 6) {
            return 5;
        }
        if (b == 7) {
            return 6;
        }
        if (b == 8) {
            return 7;
        }
        if (b == 9) {
            return 8;
        }
        if (b == 10) {
            return 10;
        }
        if (b == 11) {
            return 9;
        }
        if (b == 12) {
            return 8;
        }
        if (b == 14) {
            return 11;
        }
        if (b == 101) {
            return 1001;
        }
        return b == 102 ? 1002 : 8;
    }

    public static int toFrameworkError(byte b) {
        if (b == 0) {
            return 17;
        }
        if (b == 1) {
            return 1;
        }
        if (b == 2) {
            return 2;
        }
        if (b == 3) {
            return 3;
        }
        if (b == 4) {
            return 4;
        }
        if (b == 5) {
            return 5;
        }
        if (b == 6) {
            return 6;
        }
        if (b == 7) {
            return 8;
        }
        if (b == 8) {
            return 18;
        }
        return b == 9 ? 19 : 17;
    }

    private AidlConversionUtils() {
    }
}
