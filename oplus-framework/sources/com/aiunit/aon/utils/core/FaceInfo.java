package com.aiunit.aon.utils.core;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class FaceInfo implements Parcelable {
    private static final String CHARSET_UTF8 = "UTF-8";
    public static final Parcelable.Creator<FaceInfo> CREATOR = new Parcelable.Creator<FaceInfo>() { // from class: com.aiunit.aon.utils.core.FaceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FaceInfo createFromParcel(Parcel source) {
            return new FaceInfo(source.readInt(), source.readInt(), source.readFloat(), source.readFloat(), source.readFloat(), source.readBundle(getClass().getClassLoader()));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FaceInfo[] newArray(int i) {
            return new FaceInfo[i];
        }
    };
    private Bundle mExtra;
    private int mHeight;
    private float mPitch;
    private float mRoll;
    private int mWidth;
    private float mYaw;

    public FaceInfo(int width, int height, float yaw, float pitch, float roll) {
        this.mWidth = -1;
        this.mHeight = -1;
        this.mYaw = -1.0f;
        this.mPitch = -1.0f;
        this.mRoll = -1.0f;
        this.mWidth = width;
        this.mHeight = height;
        this.mYaw = yaw;
        this.mPitch = pitch;
        this.mRoll = roll;
        this.mExtra = new Bundle();
    }

    public FaceInfo(int width, int height, float yaw, float pitch, float roll, Bundle extraData) {
        this.mWidth = -1;
        this.mHeight = -1;
        this.mYaw = -1.0f;
        this.mPitch = -1.0f;
        this.mRoll = -1.0f;
        this.mWidth = width;
        this.mHeight = height;
        this.mYaw = yaw;
        this.mPitch = pitch;
        this.mRoll = roll;
        this.mExtra = extraData;
    }

    public FaceInfo() {
        this.mWidth = -1;
        this.mHeight = -1;
        this.mYaw = -1.0f;
        this.mPitch = -1.0f;
        this.mRoll = -1.0f;
        this.mExtra = new Bundle();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mWidth);
        parcel.writeInt(this.mHeight);
        parcel.writeFloat(this.mYaw);
        parcel.writeFloat(this.mPitch);
        parcel.writeFloat(this.mRoll);
        parcel.writeBundle(this.mExtra);
    }

    public void readFromParcel(Parcel parcel) {
        this.mWidth = parcel.readInt();
        this.mHeight = parcel.readInt();
        this.mYaw = parcel.readFloat();
        this.mPitch = parcel.readInt();
        this.mRoll = parcel.readInt();
        this.mExtra = parcel.readBundle(getClass().getClassLoader());
    }

    public int getmWidth() {
        return this.mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmHeight() {
        return this.mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public float getmYaw() {
        return this.mYaw;
    }

    public void setmYaw(float mYaw) {
        this.mYaw = mYaw;
    }

    public float getmPitch() {
        return this.mPitch;
    }

    public void setmPitch(float mPitch) {
        this.mPitch = mPitch;
    }

    public float getmRoll() {
        return this.mRoll;
    }

    public void setmRoll(float mRoll) {
        this.mRoll = mRoll;
    }

    public Bundle getExtraData() {
        return this.mExtra;
    }

    public void addExtraStringData(String key, String value) {
        if (this.mExtra == null) {
            this.mExtra = new Bundle();
        }
        this.mExtra.putString(key, value);
    }

    public void addExtraFloatData(String key, float value) {
        if (this.mExtra == null) {
            this.mExtra = new Bundle();
        }
        this.mExtra.putFloat(key, value);
    }

    public void addExtraIntData(String key, int value) {
        if (this.mExtra == null) {
            this.mExtra = new Bundle();
        }
        this.mExtra.putInt(key, value);
    }
}
