package com.oplus.zoomwindow;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class OplusZoomWindowInfo implements Parcelable {
    public static final Parcelable.Creator<OplusZoomWindowInfo> CREATOR = new Parcelable.Creator<OplusZoomWindowInfo>() { // from class: com.oplus.zoomwindow.OplusZoomWindowInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusZoomWindowInfo createFromParcel(Parcel source) {
            return new OplusZoomWindowInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusZoomWindowInfo[] newArray(int size) {
            return new OplusZoomWindowInfo[size];
        }
    };
    public String cpnName;
    public int cvActionFlag;
    public Bundle extension;
    public int inputMethodType;
    public boolean inputShow;
    public int lastExitMethod;
    public float leftScaleOfFloatHandleCenter;
    public String lockPkg;
    public int lockUserId;
    public float rightScaleOfFloatHandleCenter;
    public int rotation;
    public int sideOfFloatHandle;
    public int systemRotation;
    public boolean windowShown;
    public int windowType;
    public String zoomPkg;
    public Rect zoomRect;
    public int zoomUserId;

    public OplusZoomWindowInfo() {
        this.zoomRect = new Rect();
        this.extension = new Bundle();
        this.cvActionFlag = 0;
        this.windowType = 0;
        this.sideOfFloatHandle = 0;
        this.zoomRect = new Rect();
    }

    public OplusZoomWindowInfo(Parcel in) {
        this.zoomRect = new Rect();
        this.extension = new Bundle();
        this.cvActionFlag = 0;
        this.windowType = 0;
        this.sideOfFloatHandle = 0;
        this.rotation = in.readInt();
        this.systemRotation = in.readInt();
        this.windowShown = in.readByte() != 0;
        this.lockPkg = in.readString();
        this.zoomRect = (Rect) in.readParcelable(null);
        this.zoomPkg = in.readString();
        this.lockUserId = in.readInt();
        this.zoomUserId = in.readInt();
        this.inputShow = in.readByte() != 0;
        this.cpnName = in.readString();
        this.lastExitMethod = in.readInt();
        this.inputMethodType = in.readInt();
        this.extension = in.readBundle();
        this.cvActionFlag = in.readInt();
        this.windowType = in.readInt();
        this.leftScaleOfFloatHandleCenter = in.readFloat();
        this.rightScaleOfFloatHandleCenter = in.readFloat();
        this.sideOfFloatHandle = in.readInt();
    }

    public OplusZoomWindowInfo(OplusZoomWindowInfo in) {
        this.zoomRect = new Rect();
        this.extension = new Bundle();
        this.cvActionFlag = 0;
        this.windowType = 0;
        this.sideOfFloatHandle = 0;
        if (in != null) {
            this.rotation = in.rotation;
            this.systemRotation = in.systemRotation;
            this.windowShown = in.windowShown;
            this.lockPkg = in.lockPkg;
            this.zoomRect = in.zoomRect;
            this.zoomPkg = in.zoomPkg;
            this.lockUserId = in.lockUserId;
            this.zoomUserId = in.zoomUserId;
            this.inputShow = in.inputShow;
            this.cpnName = in.cpnName;
            this.lastExitMethod = in.lastExitMethod;
            this.inputMethodType = in.inputMethodType;
            this.extension = in.extension;
            this.cvActionFlag = in.cvActionFlag;
            this.leftScaleOfFloatHandleCenter = in.leftScaleOfFloatHandleCenter;
            this.rightScaleOfFloatHandleCenter = in.rightScaleOfFloatHandleCenter;
            this.sideOfFloatHandle = in.sideOfFloatHandle;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.rotation);
        parcel.writeInt(this.systemRotation);
        parcel.writeByte(this.windowShown ? (byte) 1 : (byte) 0);
        parcel.writeString(this.lockPkg);
        parcel.writeParcelable(this.zoomRect, 0);
        parcel.writeString(this.zoomPkg);
        parcel.writeInt(this.lockUserId);
        parcel.writeInt(this.zoomUserId);
        parcel.writeByte(this.inputShow ? (byte) 1 : (byte) 0);
        parcel.writeString(this.cpnName);
        parcel.writeInt(this.lastExitMethod);
        parcel.writeInt(this.inputMethodType);
        parcel.writeBundle(this.extension);
        parcel.writeInt(this.cvActionFlag);
        parcel.writeInt(this.windowType);
        parcel.writeFloat(this.leftScaleOfFloatHandleCenter);
        parcel.writeFloat(this.rightScaleOfFloatHandleCenter);
        parcel.writeInt(this.sideOfFloatHandle);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusZoomWindowInfo = { ");
        sb.append(" windowType = " + this.windowType);
        sb.append(" cvActionFlag = " + this.cvActionFlag);
        sb.append(" pName = " + this.zoomPkg);
        sb.append(" rotation = " + this.rotation);
        sb.append(" systemRotation = " + this.systemRotation);
        sb.append(" shown = " + this.windowShown);
        sb.append(" lockPkg = " + this.lockPkg);
        sb.append(" zoomRect = " + this.zoomRect);
        sb.append(" lockUserId = " + this.lockUserId);
        sb.append(" zoomUserId = " + this.zoomUserId);
        sb.append(" inputShow = " + this.inputShow);
        sb.append(" cpnName = " + this.cpnName);
        sb.append(" lastExitMethod = " + this.lastExitMethod);
        sb.append(" inputMethodType = " + this.inputMethodType);
        sb.append(" leftScaleOfFloatHandleCenter = " + this.leftScaleOfFloatHandleCenter);
        sb.append(" rightScaleOfFloatHandleCenter = " + this.rightScaleOfFloatHandleCenter);
        sb.append(" sideOfFloatHandle = " + this.sideOfFloatHandle);
        sb.append(" extension = " + this.extension);
        sb.append("}");
        return sb.toString();
    }
}
