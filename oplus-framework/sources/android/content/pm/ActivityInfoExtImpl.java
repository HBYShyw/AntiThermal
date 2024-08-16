package android.content.pm;

import android.os.Parcel;
import android.util.Printer;

/* loaded from: classes.dex */
public class ActivityInfoExtImpl implements IActivityInfoExt {
    private ActivityInfo mActivityInfo;
    private boolean mDisableOverrideMinAspectRatio;
    private float mMinAspectRatioForCompatMode = 0.0f;
    private int mOplusFlags;

    public ActivityInfoExtImpl(Object base) {
        this.mActivityInfo = (ActivityInfo) base;
    }

    public void setOplusFlags(int flags) {
        this.mOplusFlags = flags;
    }

    public int getOplusFlags() {
        return this.mOplusFlags;
    }

    public Object getActivityInfo() {
        return this.mActivityInfo;
    }

    public void copy(IActivityInfoExt other) {
        this.mOplusFlags = other.getOplusFlags();
        this.mActivityInfo = (ActivityInfo) other.getActivityInfo();
        this.mMinAspectRatioForCompatMode = other.getOverrideMinAspectRatioForCompatMode();
    }

    public void readFromParcel(Parcel in) {
        this.mOplusFlags = in.readInt();
        this.mMinAspectRatioForCompatMode = in.readFloat();
    }

    public void writeToParcel(Parcel dest) {
        dest.writeInt(this.mOplusFlags);
        dest.writeFloat(this.mMinAspectRatioForCompatMode);
    }

    public void dumpFront(Printer pw, String prefix) {
        if (this.mOplusFlags != 0) {
            pw.println(prefix + "oplusFlags=0x" + Integer.toHexString(this.mOplusFlags));
        }
    }

    public int getRealConfigChanged(String name, int changes) {
        OplusPackageManager pm = new OplusPackageManager();
        if (!pm.inCptWhiteList(693, name)) {
            return changes;
        }
        int configChange = changes | 1152;
        return configChange;
    }

    public void setOverrideMinAspectRatioForCompatMode(float minAspectRatio) {
        this.mMinAspectRatioForCompatMode = minAspectRatio;
    }

    public float getRawOverrideMinAspectRatioForCompatMode() {
        return this.mMinAspectRatioForCompatMode;
    }

    public float getOverrideMinAspectRatioForCompatMode() {
        if (this.mDisableOverrideMinAspectRatio) {
            return 0.0f;
        }
        return this.mMinAspectRatioForCompatMode;
    }

    public void disableOverrideMinAspectRatioForCompatMode(boolean disable) {
        this.mDisableOverrideMinAspectRatio = disable;
    }

    public boolean inOplusCompatMode() {
        return !this.mDisableOverrideMinAspectRatio && this.mMinAspectRatioForCompatMode > 0.0f;
    }
}
