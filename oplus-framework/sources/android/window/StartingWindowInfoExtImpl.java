package android.window;

import android.os.Parcel;

/* loaded from: classes.dex */
public class StartingWindowInfoExtImpl implements IStartingWindowInfoExt {
    private StartingWindowInfo mBase;
    private OplusStartingWindowExtendedInfo mExtendedInfo;

    public StartingWindowInfoExtImpl(Object base) {
        this.mBase = (StartingWindowInfo) base;
    }

    public void setExtendedInfo(Object extendedInfo) {
        this.mExtendedInfo = (OplusStartingWindowExtendedInfo) extendedInfo;
    }

    public Object getExtendedInfo() {
        return this.mExtendedInfo;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mExtendedInfo, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mExtendedInfo = in.readParcelable(OplusStartingWindowExtendedInfo.class.getClassLoader());
    }
}
