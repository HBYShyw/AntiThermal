package android.widget;

import android.content.Context;
import android.util.Log;

/* loaded from: classes.dex */
public class FlingOptimizerOverScroller {
    private static final float DECELERATION_TIME_SLOPE_GETTIME = 0.56f;
    private static final float DENSITY_MEDIUM = 160.0f;
    private static final float INCH_METER = 39.37f;
    private static final float INFLEXION = 0.35f;
    private static final float NUM_ONE = 1.0f;
    private static final float SLOP_EPARAME_TERGET_DISTANCE = 1.5f;
    private static final float SLOW_DOWN_TUNNING_DISTANCE = 5.4f;
    private static final float SLOW_DOWN_TUNNING_VELOCITY = 4.0f;
    private static final float SPLINE_DISTANCE_COMPLETE = 0.9f;
    private static final String TAG = "FlingOptimizerOverScroller";
    private final float mPhysicalCoeff;
    private final float mPpi;
    private float mDecelerationRate = (float) (Math.log(0.78d) / Math.log(0.9d));
    private boolean DEBUG = false;

    public FlingOptimizerOverScroller(Context context) {
        float f = context.getResources().getDisplayMetrics().density * DENSITY_MEDIUM;
        this.mPpi = f;
        this.mPhysicalCoeff = f * 386.0878f * DECELERATION_TIME_SLOPE_GETTIME;
    }

    public int fling(int velocityY) {
        return velocityY;
    }

    public double getSplineFlingDistanceTuning(int velocity, float flingFriction) {
        double deceleration = Math.log((Math.abs(velocity) * INFLEXION) / (this.mPhysicalCoeff * flingFriction));
        float f = this.mDecelerationRate;
        double decelMinusOne = f - 1.0d;
        double distance = this.mPhysicalCoeff * flingFriction * Math.exp((f / decelMinusOne) * deceleration);
        if (this.DEBUG) {
            Log.v(TAG, "getSplineFlingDistanceTuning  distance = " + distance + "\u3000velocity = " + velocity + " deceleration = " + deceleration + " flingFriction = " + flingFriction + " mPhysicalCoeff = " + this.mPhysicalCoeff);
        }
        return 1.5d * distance;
    }

    public int getSplineFlingDurationTuning(int velocity, float flingFriction) {
        double deceleration = Math.log((Math.abs(velocity) * INFLEXION) / (this.mPhysicalCoeff * flingFriction));
        double decelMinusOne = this.mDecelerationRate - 1.0d;
        int finalDuration = (int) (Math.exp(deceleration / decelMinusOne) * 1000.0d);
        if (this.DEBUG) {
            Log.v(TAG, "getSplineFlingDurationTuning  deceleration = " + deceleration + " decelMinusOne = " + decelMinusOne + " finalDuration = " + finalDuration);
        }
        return (int) (finalDuration * SLOP_EPARAME_TERGET_DISTANCE);
    }

    public double getUpdateDistance(long currentTime, int splineDuration, int splineDistance) {
        float distanceCoef1 = ((float) Math.exp(-5.309999942779541d)) + 1.0f;
        float distanceCoef2 = (float) Math.exp((((float) currentTime) / splineDuration) * (-5.4f));
        float distanceCoef = distanceCoef1 - distanceCoef2;
        double distance = splineDistance * distanceCoef;
        if (Math.abs(distance) < Math.abs(splineDistance) * SPLINE_DISTANCE_COMPLETE) {
            return distance;
        }
        if (distanceCoef >= 1.0f) {
            distanceCoef = 1.0f;
        }
        if (this.DEBUG) {
            Log.v(TAG, "getUpdateDistance distanceCoef = " + distanceCoef + " distance =" + distance + " splineDistance = " + splineDistance + " currentTime = " + currentTime + " splineDuration = " + splineDuration + " distanceCoef1 = " + distanceCoef1 + " distanceCoef2 = " + distanceCoef2);
        }
        return splineDistance * distanceCoef;
    }

    public float getUpdateVelocity(long currentTime, int splineDuration, int velocity) {
        float velocityCoef = (float) Math.exp((((float) currentTime) / splineDuration) * (-4.0f));
        if (this.DEBUG) {
            Log.v(TAG, "getUpdateVelocity  currentTime = " + currentTime + " splineDuration = " + splineDuration + " velocity = " + velocity + " updateVelocity = " + (velocity * velocityCoef));
        }
        return velocity * velocityCoef;
    }
}
