package com.oplus.wrapper.hardware.fingerprint;

import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FingerprintManager {
    private static final String TAG = "FingerprintManager";
    private final android.hardware.fingerprint.FingerprintManager mFingerprintManager;

    public FingerprintManager(android.hardware.fingerprint.FingerprintManager fingerprintManager) {
        this.mFingerprintManager = fingerprintManager;
    }

    public List<Fingerprint> getEnrolledFingerprints(int userId) {
        List<android.hardware.fingerprint.Fingerprint> fingerprints = this.mFingerprintManager.getEnrolledFingerprints(userId);
        if (fingerprints == null) {
            return null;
        }
        List<Fingerprint> fingerprintList = new ArrayList<>(fingerprints.size());
        for (android.hardware.fingerprint.Fingerprint fingerprint : fingerprints) {
            if (fingerprint != null) {
                fingerprintList.add(new Fingerprint(fingerprint));
            }
        }
        return fingerprintList;
    }

    public boolean hasEnrolledTemplates(int userId) {
        return this.mFingerprintManager.hasEnrolledTemplates(userId);
    }

    public void remove(Fingerprint fp, int userId) {
        android.hardware.fingerprint.Fingerprint fingerprint = fp.getFingerprint();
        if (fingerprint == null) {
            Slog.i(TAG, "Fingerprint is null");
        } else {
            this.mFingerprintManager.remove(fingerprint, userId, null);
        }
    }
}
