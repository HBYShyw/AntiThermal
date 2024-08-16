package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class PackagePermission implements Parcelable {
    public static final Parcelable.Creator<PackagePermission> CREATOR = new Parcelable.Creator<PackagePermission>() { // from class: android.content.pm.PackagePermission.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PackagePermission createFromParcel(Parcel in) {
            PackagePermission pkgPermission = new PackagePermission();
            pkgPermission.readFromParcel(in);
            return pkgPermission;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PackagePermission[] newArray(int size) {
            return new PackagePermission[size];
        }
    };
    public long mAccept;
    public int mId;
    public String mPackageName;
    public long mPrompt;
    public long mReject;
    public int mTrust;

    public PackagePermission() {
    }

    protected PackagePermission(Parcel in) {
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mId);
        out.writeString(this.mPackageName);
        out.writeLong(this.mAccept);
        out.writeLong(this.mReject);
        out.writeLong(this.mPrompt);
        out.writeInt(this.mTrust);
    }

    public void readFromParcel(Parcel in) {
        this.mId = in.readInt();
        this.mPackageName = in.readString();
        this.mAccept = in.readLong();
        this.mReject = in.readLong();
        this.mPrompt = in.readLong();
        this.mTrust = in.readInt();
    }

    public PackagePermission copy() {
        PackagePermission copy = new PackagePermission();
        copy.mId = this.mId;
        copy.mPackageName = this.mPackageName;
        copy.mAccept = this.mAccept;
        copy.mReject = this.mReject;
        copy.mPrompt = this.mPrompt;
        copy.mTrust = this.mTrust;
        return copy;
    }

    public String toString() {
        return "[mPackageName=" + this.mPackageName + ", mAccept=" + this.mAccept + ", mReject=" + this.mReject + ", mPrompt=" + this.mPrompt + ", mTrust=" + this.mTrust + "]";
    }
}
