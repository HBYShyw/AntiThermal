package android.os.storage;

import android.os.Parcel;
import android.util.Log;
import com.android.internal.util.IndentingPrintWriter;

/* loaded from: classes.dex */
public class VolumeInfoExtImpl implements IVolumeInfoExt {
    private VolumeInfo mBase;
    public int readOnlyType = -1;
    public boolean hasMountedStateBrocasted = false;

    public VolumeInfoExtImpl(Object base) {
        this.mBase = (VolumeInfo) base;
    }

    public int getReadOnlyType() {
        return this.readOnlyType;
    }

    public void setReadOnlyTypeValue(int value) {
        if (value < -1 || value > 2) {
            Log.w("VolumeInfoExtImpl", "value illegal, must in [-1, 2]");
        } else {
            this.readOnlyType = value;
        }
    }

    public void initFromParcel(Parcel in) {
        this.readOnlyType = in.readInt();
        this.hasMountedStateBrocasted = in.readBoolean();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.readOnlyType);
        parcel.writeBoolean(this.hasMountedStateBrocasted);
    }

    public void dump(Object pw) {
        ((IndentingPrintWriter) pw).println();
        ((IndentingPrintWriter) pw).printPair("readOnlyType", Integer.valueOf(this.readOnlyType));
        ((IndentingPrintWriter) pw).println();
        ((IndentingPrintWriter) pw).printPair("hasMountedStateBrocasted", Boolean.valueOf(this.hasMountedStateBrocasted));
    }
}
