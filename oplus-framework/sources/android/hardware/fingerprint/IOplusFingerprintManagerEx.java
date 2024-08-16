package android.hardware.fingerprint;

/* loaded from: classes.dex */
public interface IOplusFingerprintManagerEx {

    /* loaded from: classes.dex */
    public static abstract class OpticalFingerprintListener {
        public void onOpticalFingerprintUpdate(int status) {
        }
    }

    default int regsiterOpticalFingerprintListener(OpticalFingerprintListener listener) {
        return -1;
    }

    default void showFingerprintIcon() {
    }

    default long getLockoutAttemptDeadline() {
        return -1L;
    }

    default void hideFingerprintIcon() {
    }

    default int getFailedAttempts() {
        return -1;
    }
}
