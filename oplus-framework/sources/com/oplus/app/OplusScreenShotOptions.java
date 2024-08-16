package com.oplus.app;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public class OplusScreenShotOptions implements Parcelable {
    public static final Parcelable.Creator<OplusScreenShotOptions> CREATOR = new Parcelable.Creator<OplusScreenShotOptions>() { // from class: com.oplus.app.OplusScreenShotOptions.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusScreenShotOptions createFromParcel(Parcel source) {
            return new OplusScreenShotOptions(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusScreenShotOptions[] newArray(int size) {
            return new OplusScreenShotOptions[size];
        }
    };
    public SurfaceControl[] mExcludeLayers;
    public boolean mFullDisplay;
    public SurfaceControl mLayer;
    public Rect mSourceCrop;
    public int[] mTasks;

    public OplusScreenShotOptions() {
    }

    public OplusScreenShotOptions(OplusScreenShotOptions options) {
        this.mLayer = options.mLayer;
        this.mExcludeLayers = options.mExcludeLayers;
        this.mSourceCrop = options.mSourceCrop;
        this.mFullDisplay = options.mFullDisplay;
        this.mTasks = options.mTasks;
    }

    private OplusScreenShotOptions(Parcel source) {
        int taskSize = source.readInt();
        if (taskSize > 0) {
            int[] iArr = new int[taskSize];
            this.mTasks = iArr;
            source.readIntArray(iArr);
        }
        int layerFlag = source.readInt();
        if (layerFlag > 0) {
            this.mLayer = (SurfaceControl) source.readTypedObject(SurfaceControl.CREATOR);
        }
        int excludeLayersSize = source.readInt();
        if (excludeLayersSize > 0) {
            SurfaceControl[] surfaceControlArr = new SurfaceControl[excludeLayersSize];
            this.mExcludeLayers = surfaceControlArr;
            source.readTypedArray(surfaceControlArr, SurfaceControl.CREATOR);
        }
        this.mSourceCrop = (Rect) source.readParcelable(null);
        this.mFullDisplay = source.readBoolean();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        int[] iArr = this.mTasks;
        if (iArr != null) {
            int taskSize = iArr.length;
            dest.writeInt(taskSize);
            dest.writeIntArray(this.mTasks);
        } else {
            dest.writeInt(0);
        }
        if (this.mLayer != null) {
            dest.writeInt(1);
            dest.writeTypedObject(this.mLayer, 0);
        } else {
            dest.writeInt(0);
        }
        SurfaceControl[] surfaceControlArr = this.mExcludeLayers;
        if (surfaceControlArr != null) {
            int layerSize = surfaceControlArr.length;
            dest.writeInt(layerSize);
            dest.writeTypedArray(this.mExcludeLayers, 0);
        } else {
            dest.writeInt(0);
        }
        dest.writeParcelable(this.mSourceCrop, 0);
        dest.writeBoolean(this.mFullDisplay);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusScreenShotOptions { ");
        sb.append("mLayer = " + this.mLayer);
        sb.append(", mExcludeLayers = " + this.mExcludeLayers);
        sb.append(", mTasks = " + this.mTasks);
        sb.append(", mSourceCrop = " + this.mSourceCrop);
        sb.append(", mFullDisplay = " + this.mFullDisplay);
        sb.append("}");
        return sb.toString();
    }
}
