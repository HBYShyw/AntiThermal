package android.window;

import android.os.Parcel;

/* loaded from: classes.dex */
public class WindowContainerTransactionExtImpl implements IWindowContainerTransactionExt {
    private OplusWCTExtendInfo mExtendInfo;
    private WindowContainerTransaction mWct;

    public WindowContainerTransactionExtImpl(Object base) {
        this.mWct = (WindowContainerTransaction) base;
    }

    public void setWCTExtendInfo(Object extInfo) {
        this.mExtendInfo = (OplusWCTExtendInfo) extInfo;
    }

    public Object getWCTExtendInfo() {
        return this.mExtendInfo;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mExtendInfo, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mExtendInfo = (OplusWCTExtendInfo) in.readParcelable(OplusWCTExtendInfo.class.getClassLoader(), OplusWCTExtendInfo.class);
    }
}
