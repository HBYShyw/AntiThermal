package com.android.server.location.fudger;

import android.location.Location;
import android.location.LocationResult;
import android.os.SystemClock;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.Random;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LocationFudger {
    private static final int APPROXIMATE_METERS_PER_DEGREE_AT_EQUATOR = 111000;
    private static final double CHANGE_PER_INTERVAL = 0.03d;
    private static final double MAX_LATITUDE = 89.999990990991d;
    private static final float MIN_ACCURACY_M = 200.0f;
    private static final double NEW_WEIGHT = 0.03d;

    @VisibleForTesting
    static final long OFFSET_UPDATE_INTERVAL_MS = 3600000;
    private static final double OLD_WEIGHT = Math.sqrt(0.9991d);
    private final float mAccuracyM;

    @GuardedBy({"this"})
    private Location mCachedCoarseLocation;

    @GuardedBy({"this"})
    private LocationResult mCachedCoarseLocationResult;

    @GuardedBy({"this"})
    private Location mCachedFineLocation;

    @GuardedBy({"this"})
    private LocationResult mCachedFineLocationResult;
    private final Clock mClock;

    @GuardedBy({"this"})
    private double mLatitudeOffsetM;

    @GuardedBy({"this"})
    private double mLongitudeOffsetM;

    @GuardedBy({"this"})
    private long mNextUpdateRealtimeMs;
    private final Random mRandom;

    private static double metersToDegreesLatitude(double d) {
        return d / 111000.0d;
    }

    private static double wrapLatitude(double d) {
        if (d > MAX_LATITUDE) {
            d = 89.999990990991d;
        }
        if (d < -89.999990990991d) {
            return -89.999990990991d;
        }
        return d;
    }

    private static double wrapLongitude(double d) {
        double d2 = d % 360.0d;
        if (d2 >= 180.0d) {
            d2 -= 360.0d;
        }
        return d2 < -180.0d ? d2 + 360.0d : d2;
    }

    public LocationFudger(float f) {
        this(f, SystemClock.elapsedRealtimeClock(), new SecureRandom());
    }

    @VisibleForTesting
    LocationFudger(float f, Clock clock, Random random) {
        this.mClock = clock;
        this.mRandom = random;
        this.mAccuracyM = Math.max(f, MIN_ACCURACY_M);
        resetOffsets();
    }

    public void resetOffsets() {
        this.mLatitudeOffsetM = nextRandomOffset();
        this.mLongitudeOffsetM = nextRandomOffset();
        this.mNextUpdateRealtimeMs = this.mClock.millis() + 3600000;
    }

    public LocationResult createCoarse(LocationResult locationResult) {
        synchronized (this) {
            if (locationResult != this.mCachedFineLocationResult && locationResult != this.mCachedCoarseLocationResult) {
                LocationResult map = locationResult.map(new Function() { // from class: com.android.server.location.fudger.LocationFudger$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return LocationFudger.this.createCoarse((Location) obj);
                    }
                });
                synchronized (this) {
                    this.mCachedFineLocationResult = locationResult;
                    this.mCachedCoarseLocationResult = map;
                }
                return map;
            }
            return this.mCachedCoarseLocationResult;
        }
    }

    public Location createCoarse(Location location) {
        synchronized (this) {
            if (location != this.mCachedFineLocation && location != this.mCachedCoarseLocation) {
                updateOffsets();
                Location location2 = new Location(location);
                location2.removeBearing();
                location2.removeSpeed();
                location2.removeAltitude();
                location2.setExtras(null);
                double wrapLatitude = wrapLatitude(location2.getLatitude());
                double wrapLongitude = wrapLongitude(location2.getLongitude()) + wrapLongitude(metersToDegreesLongitude(this.mLongitudeOffsetM, wrapLatitude));
                double wrapLatitude2 = wrapLatitude + wrapLatitude(metersToDegreesLatitude(this.mLatitudeOffsetM));
                double wrapLatitude3 = wrapLatitude(Math.round(wrapLatitude2 / r5) * metersToDegreesLatitude(this.mAccuracyM));
                double wrapLongitude2 = wrapLongitude(Math.round(wrapLongitude / r5) * metersToDegreesLongitude(this.mAccuracyM, wrapLatitude3));
                location2.setLatitude(wrapLatitude3);
                location2.setLongitude(wrapLongitude2);
                location2.setAccuracy(Math.max(this.mAccuracyM, location2.getAccuracy()));
                synchronized (this) {
                    this.mCachedFineLocation = location;
                    this.mCachedCoarseLocation = location2;
                }
                return location2;
            }
            return this.mCachedCoarseLocation;
        }
    }

    private synchronized void updateOffsets() {
        long millis = this.mClock.millis();
        if (millis < this.mNextUpdateRealtimeMs) {
            return;
        }
        double d = OLD_WEIGHT;
        this.mLatitudeOffsetM = (this.mLatitudeOffsetM * d) + (nextRandomOffset() * 0.03d);
        this.mLongitudeOffsetM = (d * this.mLongitudeOffsetM) + (nextRandomOffset() * 0.03d);
        this.mNextUpdateRealtimeMs = millis + 3600000;
    }

    private double nextRandomOffset() {
        return this.mRandom.nextGaussian() * (this.mAccuracyM / 4.0d);
    }

    private static double metersToDegreesLongitude(double d, double d2) {
        return (d / 111000.0d) / Math.cos(Math.toRadians(d2));
    }
}
