package android.hardware.fingerprint;

import android.os.Parcel;

/* loaded from: classes.dex */
public class FingerprintExtImpl implements IFingerprintExt {
    private Fingerprint mBase;
    public int mBiometricId;
    public int mFingerFlags = 0;
    public int mGroupId;

    public FingerprintExtImpl(Object base) {
        this.mBase = (Fingerprint) base;
    }

    public void init(int groupId) {
        this.mGroupId = groupId;
    }

    public void readFromParcel(Parcel in) {
        this.mFingerFlags = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mFingerFlags);
    }

    public void setFingerFlags(int fingerFlags) {
        this.mFingerFlags = fingerFlags;
    }

    public int getFingerFlags() {
        return this.mFingerFlags;
    }
}
