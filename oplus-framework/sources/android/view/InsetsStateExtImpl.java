package android.view;

import android.graphics.Rect;
import android.os.Parcel;
import android.util.SparseArray;

/* loaded from: classes.dex */
public class InsetsStateExtImpl implements IInsetsStateExt {
    private int mExtraDisplayCutoutMode = -1;
    private IRemoteTaskWindowInsetHelperExt mRTWindowInsetHelper;
    InsetsState mState;

    public InsetsStateExtImpl(Object base) {
        this.mState = (InsetsState) base;
    }

    public void removeSourceByType(int types) {
        if (types == 0) {
            return;
        }
        SparseArray<InsetsSource> sources = this.mState.getWrapper().getSources();
        for (int i = sources.size() - 1; i >= 0; i--) {
            InsetsSource source = sources.valueAt(i);
            if ((source.getType() & types) != 0) {
                sources.removeAt(i);
            }
        }
    }

    public InsetsSource peekDefaultSource(int type) {
        if (type == 0) {
            return null;
        }
        SparseArray<InsetsSource> sources = this.mState.getWrapper().getSources();
        for (int i = sources.size() - 1; i >= 0; i--) {
            InsetsSource source = sources.valueAt(i);
            if (source.getType() == type) {
                return source;
            }
        }
        return null;
    }

    public InsetsSource peekNavigationBarSource(int type, int flags) {
        if (type == 0) {
            return null;
        }
        SparseArray<InsetsSource> sources = this.mState.getWrapper().getSources();
        for (int i = sources.size() - 1; i >= 0; i--) {
            InsetsSource source = sources.valueAt(i);
            if (source.getType() == type && source.getFlags() == flags) {
                return source;
            }
        }
        return null;
    }

    public void setExtraDisplayCutoutMode(int extraCutoutMode) {
        this.mExtraDisplayCutoutMode = extraCutoutMode;
    }

    public int getExtraDisplayCutoutMode() {
        return this.mExtraDisplayCutoutMode;
    }

    public void readFromParcel(Parcel in) {
        this.mExtraDisplayCutoutMode = in.readInt();
    }

    public void writeToParcel(Parcel dest) {
        dest.writeInt(this.mExtraDisplayCutoutMode);
    }

    public void setRTWindowInsetHelper(IRemoteTaskWindowInsetHelperExt helper) {
        this.mRTWindowInsetHelper = helper;
    }

    public void updateInsetSource(InsetsSource source, int type, Rect frame) {
        IRemoteTaskWindowInsetHelperExt iRemoteTaskWindowInsetHelperExt = this.mRTWindowInsetHelper;
        if (iRemoteTaskWindowInsetHelperExt != null) {
            iRemoteTaskWindowInsetHelperExt.updateInsetSourceIfNeeded(source, type, frame);
        }
    }
}
