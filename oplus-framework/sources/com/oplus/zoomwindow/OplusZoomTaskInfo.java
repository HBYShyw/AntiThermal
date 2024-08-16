package com.oplus.zoomwindow;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.window.WindowContainerToken;

/* loaded from: classes.dex */
public class OplusZoomTaskInfo implements Parcelable {
    public static final Parcelable.Creator<OplusZoomTaskInfo> CREATOR = new Parcelable.Creator<OplusZoomTaskInfo>() { // from class: com.oplus.zoomwindow.OplusZoomTaskInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusZoomTaskInfo createFromParcel(Parcel source) {
            return new OplusZoomTaskInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusZoomTaskInfo[] newArray(int size) {
            return new OplusZoomTaskInfo[size];
        }
    };
    public String callPkg;
    public Bundle extension;
    public boolean landScape;
    public int launchFrom;
    public String pkgName;
    public int rotation;
    public float scale;
    public Rect scaleRect;
    public int state;
    public int taskId;
    public WindowContainerToken token;
    public int topChildTaskId;
    public int userId;
    public Rect windowCrop;

    public OplusZoomTaskInfo() {
        this.scaleRect = new Rect();
        this.windowCrop = new Rect();
        this.extension = new Bundle();
    }

    public OplusZoomTaskInfo(Parcel in) {
        this.scaleRect = new Rect();
        this.windowCrop = new Rect();
        this.extension = new Bundle();
        this.token = (WindowContainerToken) WindowContainerToken.CREATOR.createFromParcel(in);
        this.taskId = in.readInt();
        this.topChildTaskId = in.readInt();
        this.userId = in.readInt();
        this.pkgName = in.readString();
        this.callPkg = in.readString();
        this.state = in.readInt();
        this.launchFrom = in.readInt();
        this.scaleRect = (Rect) in.readParcelable(null, Rect.class);
        this.scale = in.readFloat();
        this.windowCrop = (Rect) in.readParcelable(null, Rect.class);
        this.landScape = in.readBoolean();
        this.rotation = in.readInt();
        this.extension = in.readBundle();
    }

    public OplusZoomTaskInfo(OplusZoomTaskInfo source) {
        this.scaleRect = new Rect();
        this.windowCrop = new Rect();
        this.extension = new Bundle();
        if (source != null) {
            this.token = source.token;
            this.taskId = source.taskId;
            this.topChildTaskId = source.topChildTaskId;
            this.userId = source.userId;
            this.pkgName = source.pkgName;
            this.callPkg = source.callPkg;
            this.state = source.state;
            this.launchFrom = source.launchFrom;
            this.scaleRect.set(source.scaleRect);
            this.scale = source.scale;
            this.windowCrop.set(source.windowCrop);
            this.landScape = source.landScape;
            this.rotation = source.rotation;
        }
    }

    public void copy(OplusZoomTaskInfo source) {
        if (source != null) {
            this.token = source.token;
            this.taskId = source.taskId;
            this.topChildTaskId = source.topChildTaskId;
            this.userId = source.userId;
            this.pkgName = source.pkgName;
            this.callPkg = source.callPkg;
            this.state = source.state;
            this.launchFrom = source.launchFrom;
            this.scaleRect.set(source.scaleRect);
            this.scale = source.scale;
            this.windowCrop.set(source.windowCrop);
            this.landScape = source.landScape;
            this.rotation = source.rotation;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        this.token.writeToParcel(dest, flags);
        dest.writeInt(this.taskId);
        dest.writeInt(this.topChildTaskId);
        dest.writeInt(this.userId);
        dest.writeString(this.pkgName);
        dest.writeString(this.callPkg);
        dest.writeInt(this.state);
        dest.writeInt(this.launchFrom);
        dest.writeParcelable(this.scaleRect, flags);
        dest.writeFloat(this.scale);
        dest.writeParcelable(this.windowCrop, flags);
        dest.writeBoolean(this.landScape);
        dest.writeInt(this.rotation);
        dest.writeBundle(this.extension);
    }

    public String toString() {
        return "OplusZoomTaskInfo{token=" + this.token + ", taskId=" + this.taskId + ", topChildTaskId=" + this.topChildTaskId + ", userId=" + this.userId + ", pkgName='" + this.pkgName + "', callPkg='" + this.callPkg + "', state=" + this.state + ", launchFrom=" + this.launchFrom + ", scaleRect=" + this.scaleRect + ", scale=" + this.scale + ", windowCrop=" + this.windowCrop + ", landScape=" + this.landScape + ", rotation=" + this.rotation + ", extension=" + this.extension + '}';
    }
}
