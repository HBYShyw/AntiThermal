package android.window;

import android.os.Parcel;

/* loaded from: classes.dex */
public class TransitionInfoExtImpl implements ITransitionInfoExt {
    private OplusTransitionExtendedInfo mExtendedInfo;
    private TransitionInfo mInfo;

    public TransitionInfoExtImpl(Object base) {
        this.mInfo = (TransitionInfo) base;
    }

    public void setExtendedInfo(Object extendedInfo) {
        this.mExtendedInfo = (OplusTransitionExtendedInfo) extendedInfo;
    }

    public Object getExtendedInfo() {
        return this.mExtendedInfo;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mExtendedInfo, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mExtendedInfo = (OplusTransitionExtendedInfo) in.readParcelable(OplusTransitionExtendedInfo.class.getClassLoader(), OplusTransitionExtendedInfo.class);
    }
}
