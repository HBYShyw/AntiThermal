package android.hardware.fingerprint;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;

/* loaded from: classes.dex */
public final class OplusFingerprint implements Parcelable {
    public static final Parcelable.Creator<OplusFingerprint> CREATOR = new Parcelable.Creator<OplusFingerprint>() { // from class: android.hardware.fingerprint.OplusFingerprint.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusFingerprint createFromParcel(Parcel in) {
            return new OplusFingerprint(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusFingerprint[] newArray(int size) {
            return new OplusFingerprint[size];
        }
    };
    private static final String EMPTY_NAME = "NULL";
    private int mBiometricId;
    private long mDeviceId;
    private int mFingerFlags;
    private int mGroupId;
    private CharSequence mName;

    public OplusFingerprint(CharSequence name, int groupId, int fingerId, long deviceId, int fingerFlags) {
        this.mFingerFlags = 0;
        this.mName = name;
        this.mBiometricId = fingerId;
        this.mDeviceId = deviceId;
        this.mGroupId = groupId;
        this.mFingerFlags = fingerFlags;
    }

    private OplusFingerprint(Parcel in) {
        this.mFingerFlags = 0;
        this.mName = in.readString();
        this.mBiometricId = in.readInt();
        this.mDeviceId = in.readLong();
        this.mGroupId = in.readInt();
        this.mFingerFlags = in.readInt();
    }

    public String toString() {
        String res = "{flag=" + Integer.toHexString(this.mFingerFlags) + ",mGroupId=" + this.mGroupId + ",fingerId=" + this.mBiometricId + ",deviceId=" + this.mDeviceId + "}";
        return res;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        String name = getName() != null ? getName().toString() : EMPTY_NAME;
        out.writeString(name);
        out.writeInt(getBiometricId());
        out.writeLong(getDeviceId());
        out.writeInt(this.mGroupId);
        out.writeInt(this.mFingerFlags);
    }

    public int getFingerFlags() {
        return this.mFingerFlags;
    }

    public CharSequence getName() {
        CharSequence charSequence;
        String defaultLanguage = Locale.getDefault().getLanguage();
        if ("en".equals(defaultLanguage) && (charSequence = this.mName) != null) {
            return charSequence.toString().replaceAll("[\\u200b-\\u200f]", "");
        }
        return this.mName;
    }

    public int getBiometricId() {
        return this.mBiometricId;
    }

    public long getDeviceId() {
        return this.mDeviceId;
    }
}
