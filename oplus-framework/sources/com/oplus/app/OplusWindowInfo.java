package com.oplus.app;

import android.content.ComponentName;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.SurfaceControl;
import android.view.WindowManager;

/* loaded from: classes.dex */
public class OplusWindowInfo implements Parcelable {
    public static final Parcelable.Creator<OplusWindowInfo> CREATOR = new Parcelable.Creator<OplusWindowInfo>() { // from class: com.oplus.app.OplusWindowInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusWindowInfo createFromParcel(Parcel source) {
            return new OplusWindowInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusWindowInfo[] newArray(int size) {
            return new OplusWindowInfo[size];
        }
    };
    public ComponentName componentName;
    public Rect contentFrame;
    public Bundle extension;
    public boolean isEmbedded;
    public boolean isSplitMode;
    public Rect mBounds;
    public Rect mFrame;
    public SurfaceControl mSurfaceControl;
    public String packageName;
    public int taskId;
    public int type;
    public int userId;
    public Rect visibleFrame;
    public WindowManager.LayoutParams windowAttributes;
    public String windowName;
    public int windowingMode;

    public OplusWindowInfo() {
        this.taskId = -1;
        this.mBounds = new Rect();
        this.isEmbedded = false;
        this.isSplitMode = false;
        this.windowAttributes = new WindowManager.LayoutParams();
        this.mFrame = new Rect();
        this.contentFrame = new Rect();
        this.visibleFrame = new Rect();
        this.extension = new Bundle();
    }

    public OplusWindowInfo(OplusWindowInfo info) {
        this.taskId = -1;
        this.mBounds = new Rect();
        this.isEmbedded = false;
        this.isSplitMode = false;
        this.windowAttributes = new WindowManager.LayoutParams();
        this.mFrame = new Rect();
        this.contentFrame = new Rect();
        this.visibleFrame = new Rect();
        this.extension = new Bundle();
        if (info != null) {
            this.windowName = info.windowName;
            this.userId = info.userId;
            this.packageName = info.packageName;
            this.componentName = info.componentName;
            this.taskId = info.taskId;
            this.windowingMode = info.windowingMode;
            this.mBounds = info.mBounds;
            this.isEmbedded = info.isEmbedded;
            this.isSplitMode = info.isSplitMode;
            this.type = info.type;
            this.windowAttributes = info.windowAttributes;
            this.mFrame = info.mFrame;
            this.contentFrame = info.contentFrame;
            this.visibleFrame = info.visibleFrame;
            this.extension = info.extension;
            this.mSurfaceControl = info.mSurfaceControl;
        }
    }

    private OplusWindowInfo(Parcel source) {
        this.taskId = -1;
        this.mBounds = new Rect();
        this.isEmbedded = false;
        this.isSplitMode = false;
        this.windowAttributes = new WindowManager.LayoutParams();
        this.mFrame = new Rect();
        this.contentFrame = new Rect();
        this.visibleFrame = new Rect();
        this.extension = new Bundle();
        this.windowName = source.readString();
        this.userId = source.readInt();
        this.packageName = source.readString();
        this.componentName = (ComponentName) source.readParcelable(null);
        this.taskId = source.readInt();
        this.windowingMode = source.readInt();
        this.mBounds = (Rect) source.readParcelable(null);
        this.isEmbedded = source.readBoolean();
        this.isSplitMode = source.readBoolean();
        this.type = source.readInt();
        this.windowAttributes = (WindowManager.LayoutParams) source.readParcelable(null);
        this.mFrame = (Rect) source.readParcelable(null);
        this.contentFrame = (Rect) source.readParcelable(null);
        this.visibleFrame = (Rect) source.readParcelable(null);
        this.extension = source.readBundle();
        int layerFlag = source.readInt();
        if (layerFlag > 0) {
            this.mSurfaceControl = (SurfaceControl) source.readParcelable(null);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.windowName);
        dest.writeInt(this.userId);
        dest.writeString(this.packageName);
        dest.writeParcelable(this.componentName, 0);
        dest.writeInt(this.taskId);
        dest.writeInt(this.windowingMode);
        dest.writeParcelable(this.mBounds, 0);
        dest.writeBoolean(this.isEmbedded);
        dest.writeBoolean(this.isSplitMode);
        dest.writeInt(this.type);
        dest.writeParcelable(this.windowAttributes, 0);
        dest.writeParcelable(this.mFrame, 0);
        dest.writeParcelable(this.contentFrame, 0);
        dest.writeParcelable(this.visibleFrame, 0);
        dest.writeBundle(this.extension);
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null && surfaceControl.isValid()) {
            dest.writeInt(1);
            dest.writeParcelable(this.mSurfaceControl, 0);
        } else {
            dest.writeInt(0);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusWindowInfo { ");
        sb.append("window = " + this.windowName);
        sb.append(", userId = " + this.userId);
        sb.append(", package = " + this.packageName);
        sb.append(", componentName = " + this.componentName);
        sb.append(", taskId = " + this.taskId);
        sb.append(", windowingMode = " + this.windowingMode);
        sb.append(", mBounds = " + this.mBounds);
        sb.append(", isEmbedded = " + this.isEmbedded);
        sb.append(", isSplitMode = " + this.isSplitMode);
        sb.append(", type = " + this.type);
        sb.append(", mframe = " + this.mFrame);
        sb.append(", content frame = " + this.contentFrame);
        sb.append(", visible frame = " + this.visibleFrame);
        sb.append(", windowAttributes = " + this.windowAttributes);
        sb.append(", SurfaceControl = " + this.mSurfaceControl);
        sb.append("}");
        return sb.toString();
    }
}
