package android.view;

import android.os.Parcel;
import com.oplus.animation.LaunchViewInfo;

/* loaded from: classes.dex */
public class RemoteAnimationTargetExtImpl implements IRemoteAnimationTargetExt {
    private LaunchViewInfo mLaunchViewInfo;
    private RemoteAnimationTarget mRemoteAnimationTarget;
    private SurfaceControl mTaskLeash;

    public RemoteAnimationTargetExtImpl(Object base) {
        this.mRemoteAnimationTarget = (RemoteAnimationTarget) base;
    }

    public Object getOplusLaunchViewInfo() {
        return this.mLaunchViewInfo;
    }

    public void setOplusLaunchViewInfo(Object viewInfo) {
        this.mLaunchViewInfo = (LaunchViewInfo) viewInfo;
    }

    public SurfaceControl getTaskLeash() {
        return this.mTaskLeash;
    }

    public void setTaskLeash(SurfaceControl taskLeash) {
        this.mTaskLeash = taskLeash;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mLaunchViewInfo, flags);
        dest.writeParcelable(this.mTaskLeash, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mLaunchViewInfo = (LaunchViewInfo) in.readParcelable(LaunchViewInfo.class.getClassLoader(), LaunchViewInfo.class);
        this.mTaskLeash = (SurfaceControl) in.readParcelable(SurfaceControl.class.getClassLoader(), SurfaceControl.class);
    }
}
