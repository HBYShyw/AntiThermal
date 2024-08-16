package android.os.storage;

import android.os.Parcel;
import com.android.internal.util.IndentingPrintWriter;

/* loaded from: classes.dex */
public class StorageVolumeExtImpl implements IStorageVolumeExt {
    private StorageVolume mBase;
    public int mReadonlyType;

    public StorageVolumeExtImpl(Object base) {
        this.mBase = (StorageVolume) base;
    }

    public void init(int readonlyType) {
        this.mReadonlyType = readonlyType;
    }

    public int getReadOnlyType() {
        return this.mReadonlyType;
    }

    public void setReadOnlyType(int readonlyType) {
        this.mReadonlyType = readonlyType;
    }

    public void initFromParcel(Parcel in) {
        this.mReadonlyType = in.readInt();
    }

    public void dump(Object pw) {
        ((IndentingPrintWriter) pw).printPair("mReadonlyType", Integer.valueOf(this.mReadonlyType));
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mReadonlyType);
    }
}
