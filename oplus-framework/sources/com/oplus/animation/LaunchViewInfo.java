package com.oplus.animation;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public class LaunchViewInfo implements Parcelable {
    public static final Parcelable.Creator<LaunchViewInfo> CREATOR = new Parcelable.Creator<LaunchViewInfo>() { // from class: com.oplus.animation.LaunchViewInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LaunchViewInfo createFromParcel(Parcel in) {
            return new LaunchViewInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LaunchViewInfo[] newArray(int size) {
            return new LaunchViewInfo[size];
        }
    };
    public String mLaunchPackage;
    public Point mViewLocation;
    public SurfaceControl mViewSurface;

    public LaunchViewInfo(SurfaceControl surface, Point location, String launchPackage) {
        this.mViewSurface = surface;
        this.mViewLocation = location;
        this.mLaunchPackage = launchPackage;
    }

    private LaunchViewInfo(Parcel in) {
        this.mViewSurface = (SurfaceControl) in.readTypedObject(SurfaceControl.CREATOR);
        this.mViewLocation = (Point) in.readTypedObject(Point.CREATOR);
        this.mLaunchPackage = in.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(this.mViewSurface, flags);
        dest.writeTypedObject(this.mViewLocation, flags);
        dest.writeString(this.mLaunchPackage);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "LaunchViewInfo{surface=" + this.mViewSurface + ", location=" + this.mViewLocation + ", launchPackage=" + this.mLaunchPackage + "}";
    }
}
