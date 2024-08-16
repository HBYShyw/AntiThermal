package com.android.server.pm;

import android.util.SparseBooleanArray;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageVerificationState {
    private boolean mIntegrityVerificationComplete;
    private boolean mSufficientVerificationComplete;
    private boolean mSufficientVerificationPassed;
    private final VerifyingSession mVerifyingSession;
    private final SparseBooleanArray mSufficientVerifierUids = new SparseBooleanArray();
    private final SparseBooleanArray mRequiredVerifierUids = new SparseBooleanArray();
    private final SparseBooleanArray mUnrespondedRequiredVerifierUids = new SparseBooleanArray();
    private final SparseBooleanArray mExtendedTimeoutUids = new SparseBooleanArray();
    private boolean mRequiredVerificationComplete = false;
    private boolean mRequiredVerificationPassed = true;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageVerificationState(VerifyingSession verifyingSession) {
        this.mVerifyingSession = verifyingSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VerifyingSession getVerifyingSession() {
        return this.mVerifyingSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addRequiredVerifierUid(int i) {
        this.mRequiredVerifierUids.put(i, true);
        this.mUnrespondedRequiredVerifierUids.put(i, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkRequiredVerifierUid(int i) {
        return this.mRequiredVerifierUids.get(i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addSufficientVerifier(int i) {
        this.mSufficientVerifierUids.put(i, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkSufficientVerifierUid(int i) {
        return this.mSufficientVerifierUids.get(i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setVerifierResponseOnTimeout(int i, int i2) {
        if (checkRequiredVerifierUid(i)) {
            this.mSufficientVerifierUids.clear();
            if (this.mUnrespondedRequiredVerifierUids.get(i, false)) {
                setVerifierResponse(i, i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setVerifierResponse(int i, int i2) {
        if (this.mRequiredVerifierUids.get(i)) {
            if (i2 != 1) {
                if (i2 == 2) {
                    this.mSufficientVerifierUids.clear();
                } else {
                    this.mRequiredVerificationPassed = false;
                    this.mUnrespondedRequiredVerifierUids.clear();
                    this.mSufficientVerifierUids.clear();
                    this.mExtendedTimeoutUids.clear();
                }
            }
            this.mExtendedTimeoutUids.delete(i);
            this.mUnrespondedRequiredVerifierUids.delete(i);
            if (this.mUnrespondedRequiredVerifierUids.size() == 0) {
                this.mRequiredVerificationComplete = true;
                return;
            }
            return;
        }
        if (this.mSufficientVerifierUids.get(i)) {
            if (i2 == 1) {
                this.mSufficientVerificationPassed = true;
                this.mSufficientVerificationComplete = true;
            }
            this.mSufficientVerifierUids.delete(i);
            if (this.mSufficientVerifierUids.size() == 0) {
                this.mSufficientVerificationComplete = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void passRequiredVerification() {
        if (this.mUnrespondedRequiredVerifierUids.size() > 0) {
            throw new RuntimeException("Required verifiers still present.");
        }
        this.mRequiredVerificationPassed = true;
        this.mRequiredVerificationComplete = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isVerificationComplete() {
        if (!this.mRequiredVerificationComplete) {
            return false;
        }
        if (this.mSufficientVerifierUids.size() == 0) {
            return true;
        }
        return this.mSufficientVerificationComplete;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInstallAllowed() {
        if (!this.mRequiredVerificationComplete || !this.mRequiredVerificationPassed) {
            return false;
        }
        if (this.mSufficientVerificationComplete) {
            return this.mSufficientVerificationPassed;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean extendTimeout(int i) {
        if (!checkRequiredVerifierUid(i) || timeoutExtended(i)) {
            return false;
        }
        this.mExtendedTimeoutUids.append(i, true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean timeoutExtended(int i) {
        return this.mExtendedTimeoutUids.get(i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIntegrityVerificationResult(int i) {
        this.mIntegrityVerificationComplete = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isIntegrityVerificationComplete() {
        return this.mIntegrityVerificationComplete;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean areAllVerificationsComplete() {
        return this.mIntegrityVerificationComplete && isVerificationComplete();
    }
}
