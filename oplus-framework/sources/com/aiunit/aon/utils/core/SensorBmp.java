package com.aiunit.aon.utils.core;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class SensorBmp implements Parcelable {
    private static final String CHARSET_UTF8 = "UTF-8";
    public static final Parcelable.Creator<SensorBmp> CREATOR = new Parcelable.Creator<SensorBmp>() { // from class: com.aiunit.aon.utils.core.SensorBmp.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SensorBmp createFromParcel(Parcel parcel) {
            return new SensorBmp(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SensorBmp[] newArray(int size) {
            return new SensorBmp[size];
        }
    };
    private Bitmap mBitmap;
    private String[] mSensorResults;

    protected SensorBmp(Parcel parcel) {
        readFromParcel(parcel);
    }

    public SensorBmp(String[] sensorResults, Bitmap bitmap) {
        this.mSensorResults = sensorResults;
        this.mBitmap = bitmap;
    }

    public SensorBmp(Bitmap bitmap) {
        this.mSensorResults = null;
        this.mBitmap = bitmap;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(this.mSensorResults);
        parcel.writeParcelable(this.mBitmap, i);
    }

    public void readFromParcel(Parcel parcel) {
        this.mSensorResults = parcel.readStringArray();
        this.mBitmap = (Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader());
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public String[] getSensorResults() {
        return this.mSensorResults;
    }

    public void setSensorResults(String[] sensorResults) {
        this.mSensorResults = sensorResults;
    }
}
