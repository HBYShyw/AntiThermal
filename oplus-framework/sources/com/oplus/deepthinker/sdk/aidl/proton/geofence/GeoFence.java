package com.oplus.deepthinker.sdk.aidl.proton.geofence;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

/* loaded from: classes.dex */
public class GeoFence implements Parcelable {
    public static final Parcelable.Creator<GeoFence> CREATOR = new Parcelable.Creator<GeoFence>() { // from class: com.oplus.deepthinker.sdk.aidl.proton.geofence.GeoFence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeoFence createFromParcel(Parcel in) {
            return new GeoFence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeoFence[] newArray(int size) {
            return new GeoFence[size];
        }
    };
    public static final int DEFAULT_DISTANCE_TO_CENTER = -1;
    public static final int GEOFENCE_IN = 1;
    public static final int GEOFENCE_OUT = 2;
    public static final int GEOFENCE_UNKNOWN = 0;
    private static final String TAG = "GeoFence";
    private Location mCurLocation;
    private int mDistanceToCenter;
    private long mExpiration;
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private int mStatus;

    protected GeoFence(Parcel in) {
        this.mExpiration = -1L;
        this.mStatus = 0;
        this.mDistanceToCenter = -1;
        this.mCurLocation = null;
        this.mLongitude = in.readDouble();
        this.mLatitude = in.readDouble();
        this.mRadius = in.readFloat();
        this.mExpiration = in.readLong();
        this.mStatus = in.readInt();
        this.mDistanceToCenter = in.readInt();
        this.mCurLocation = (Location) in.readParcelable(Location.class.getClassLoader());
    }

    public GeoFence(double longitude, double latitude, float radius) {
        this.mExpiration = -1L;
        this.mStatus = 0;
        this.mDistanceToCenter = -1;
        this.mCurLocation = null;
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mRadius = radius;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(this.mLongitude);
        parcel.writeDouble(this.mLatitude);
        parcel.writeFloat(this.mRadius);
        parcel.writeLong(this.mExpiration);
        parcel.writeInt(this.mStatus);
        parcel.writeInt(this.mDistanceToCenter);
        parcel.writeParcelable(this.mCurLocation, flags);
    }

    public boolean isExpired() {
        long j = this.mExpiration;
        return j != -1 && j > SystemClock.elapsedRealtime();
    }

    public String toString() {
        return "mLongitute: " + this.mLongitude + " , mLatitude: " + this.mLatitude + " , mRadius: " + this.mRadius + " , mExpiration: " + this.mExpiration + " , mStatus: " + this.mStatus + " , mDistanceToCenter: " + this.mDistanceToCenter;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setExpiration(long expiration) {
        this.mExpiration = expiration;
    }

    public long getExpiration() {
        return this.mExpiration;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setDistanceToCenter(int distance) {
        this.mDistanceToCenter = distance;
    }

    public int getDistanceToCenter() {
        return this.mDistanceToCenter;
    }

    public void setLocation(Location location) {
        this.mCurLocation = location;
    }

    public Location getLocation() {
        return this.mCurLocation;
    }
}
