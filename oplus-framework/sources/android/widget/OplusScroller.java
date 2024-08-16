package android.widget;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

/* loaded from: classes.dex */
public class OplusScroller {
    private static final int DEFAULT_DURATION = 250;
    private static final int DEFAULT_TIME_GAP = 15;
    private static final int FLING_MODE = 1;
    private static final int FLING_SCROLL_BACK_DURATION = 750;
    private static final int FLING_SCROLL_BACK_MODE = 3;
    private static final int FLING_SPRING_MODE = 2;
    private static final int GALLERY_LIST_MODE = 5;
    private static final int GALLERY_TIME_GAP = 25;
    private static final int SCROLL_LIST_MODE = 4;
    private static final int SCROLL_MODE = 0;
    final boolean DEBUG_SPRING;
    private int DeltaCurrV;
    final String TAG;
    private int fmCurrY;
    private int fmLastCurrY;
    private float mCoeffX;
    private float mCoeffY;
    private int mCount;
    private int mCurrV;
    private int mCurrVX;
    private int mCurrVY;
    private int mCurrX;
    private int mCurrY;
    public float mDeceleration;
    private float mDeltaX;
    private float mDeltaY;
    private int mDuration;
    private float mDurationReciprocal;
    private int mFinalX;
    private int mFinalY;
    private boolean mFinished;
    private Interpolator mInterpolator;
    private int mLastCurrV;
    private int mLastCurrY;
    private int mMaxX;
    private int mMaxY;
    private int mMinX;
    private int mMinY;
    private int mMode;
    private final float mPpi;
    private long mStartTime;
    private int mStartX;
    private int mStartY;
    private float mVelocity;
    private float mViscousFluidNormalize;
    private float mViscousFluidScale;

    public OplusScroller(Context context) {
        this(context, null);
    }

    public OplusScroller(Context context, Interpolator interpolator) {
        this.TAG = "OplusScroller";
        this.DEBUG_SPRING = false;
        this.mLastCurrV = 0;
        this.mCurrV = 0;
        this.DeltaCurrV = 0;
        this.mCoeffX = 0.0f;
        this.mCoeffY = 1.0f;
        this.mCount = 1;
        this.fmLastCurrY = 0;
        this.mLastCurrY = 0;
        this.mFinished = true;
        this.mInterpolator = interpolator;
        float f = context.getResources().getDisplayMetrics().density * 160.0f;
        this.mPpi = f;
        this.mDeceleration = f * 386.0878f * ViewConfiguration.getScrollFriction();
    }

    public final boolean isFinished() {
        return this.mFinished;
    }

    public final void forceFinished(boolean finished) {
        this.mFinished = finished;
    }

    public final int getDuration() {
        return this.mDuration;
    }

    public final int getCurrX() {
        return this.mCurrX;
    }

    public final int getCurrY() {
        return this.mCurrY;
    }

    public float getCurrVelocity() {
        return this.mVelocity - ((this.mDeceleration * timePassed()) / 2000.0f);
    }

    public final int getStartX() {
        return this.mStartX;
    }

    public final int getStartY() {
        return this.mStartY;
    }

    public final int getFinalX() {
        return this.mFinalX;
    }

    public final int getFinalY() {
        return this.mFinalY;
    }

    public final int getCurrVX() {
        return this.mCurrVX;
    }

    public final int getCurrVY() {
        return this.mCurrVY;
    }

    public boolean computeScrollOffset() {
        float x;
        if (this.mFinished) {
            return false;
        }
        int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
        int i = this.mMode;
        if (4 == i) {
            timePassed = this.mCount * 15;
        } else if (5 == i) {
            timePassed = this.mCount * 25;
        }
        if (timePassed < this.mDuration) {
            switch (i) {
                case 0:
                case 5:
                    float timePassedSeconds = timePassed;
                    float x2 = timePassedSeconds * this.mDurationReciprocal;
                    Interpolator interpolator = this.mInterpolator;
                    if (interpolator == null) {
                        x = viscousFluid(x2);
                    } else {
                        x = interpolator.getInterpolation(x2);
                    }
                    this.mCurrX = this.mStartX + Math.round(this.mDeltaX * x);
                    int round = this.mStartY + Math.round(this.mDeltaY * x);
                    this.mCurrY = round;
                    if (this.mCurrX == this.mFinalX && round == this.mFinalY) {
                        this.mFinished = true;
                        break;
                    }
                    break;
                case 1:
                    float timePassedSeconds2 = timePassed;
                    float timePassedSeconds3 = timePassedSeconds2 / 1000.0f;
                    float distance = (this.mVelocity * timePassedSeconds3) - (((this.mDeceleration * timePassedSeconds3) * timePassedSeconds3) / 2.0f);
                    int round2 = this.mStartX + Math.round(this.mCoeffX * distance);
                    this.mCurrX = round2;
                    int min = Math.min(round2, this.mMaxX);
                    this.mCurrX = min;
                    this.mCurrX = Math.max(min, this.mMinX);
                    int round3 = this.mStartY + Math.round(this.mCoeffY * distance);
                    this.mCurrY = round3;
                    int min2 = Math.min(round3, this.mMaxY);
                    this.mCurrY = min2;
                    this.mCurrY = Math.max(min2, this.mMinY);
                    float velocity = this.mVelocity - (this.mDeceleration * timePassedSeconds3);
                    this.mCurrVX = Math.round(this.mCoeffX * velocity);
                    this.mCurrVY = Math.round(this.mCoeffY * velocity);
                    if (this.mCurrX == this.mFinalX && this.mCurrY == this.mFinalY) {
                        this.mFinished = true;
                        break;
                    }
                    break;
                case 2:
                    float timePassedSeconds4 = timePassed / 1000.0f;
                    float distance2 = (this.mVelocity * timePassedSeconds4) - (((this.mDeceleration * timePassedSeconds4) * timePassedSeconds4) / 2.0f);
                    int round4 = this.mStartX + Math.round(this.mCoeffX * distance2);
                    this.mCurrX = round4;
                    int min3 = Math.min(round4, this.mMaxX);
                    this.mCurrX = min3;
                    this.mCurrX = Math.max(min3, this.mMinX);
                    int round5 = this.mStartY + Math.round(this.mCoeffY * distance2);
                    this.mCurrY = round5;
                    int min4 = Math.min(round5, this.mMaxY);
                    this.mCurrY = min4;
                    int max = Math.max(min4, this.mMinY);
                    this.mCurrY = max;
                    int i2 = this.mCurrX;
                    if (i2 == this.mFinalX || max == this.mFinalY) {
                        startScroll(i2, max, (int) (this.mDeltaX + (r3 - i2)), (int) (this.mDeltaY + (this.mFinalY - max)), FLING_SCROLL_BACK_DURATION);
                        this.mMode = 3;
                        break;
                    }
                    break;
                case 3:
                    float x3 = viscousFluid(timePassed * this.mDurationReciprocal);
                    this.mCurrX = this.mStartX + Math.round(this.mDeltaX * x3);
                    int round6 = this.mStartY + Math.round(this.mDeltaY * x3);
                    this.mCurrY = round6;
                    if (this.mCurrX == this.mFinalX && round6 == this.mFinalY) {
                        this.mFinished = true;
                        break;
                    }
                    break;
                case 4:
                    float x4 = (timePassed + 200) / 2000.0f;
                    if (timePassed == 15) {
                        this.DeltaCurrV = 0;
                        this.mLastCurrV = 0;
                        this.fmLastCurrY = 0;
                    }
                    float x5 = ((1.0f - ((float) Math.exp((-4.0f) * x4))) - 1.0f) + ((float) Math.exp(-0.4000000059604645d));
                    this.mCurrX = this.mStartX + ((int) (this.mDeltaX * x5));
                    int i3 = (this.mStartY + ((int) (this.mDeltaY * x5))) - this.DeltaCurrV;
                    this.fmCurrY = i3;
                    this.mCurrV = i3 - this.fmLastCurrY;
                    int i4 = this.mLastCurrV;
                    if (i4 != 0 && Math.abs(i4) < Math.abs(this.mCurrV)) {
                        int i5 = this.DeltaCurrV;
                        int i6 = this.mCurrV;
                        int i7 = this.mLastCurrV;
                        this.DeltaCurrV = i5 + (i6 - i7);
                        this.fmCurrY -= i6 - i7;
                        this.mCurrV = i7;
                    }
                    int i8 = this.fmCurrY;
                    this.fmLastCurrY = i8;
                    this.mLastCurrV = this.mCurrV;
                    this.mCurrY = i8;
                    int v = ((i8 - this.mLastCurrY) * 250) / 15;
                    int round7 = Math.round(v);
                    this.mCurrVY = round7;
                    int i9 = this.mCurrY;
                    this.mLastCurrY = i9;
                    if ((this.mCurrX == this.mFinalX && i9 == this.mFinalY) || round7 == 0) {
                        this.mFinalY = i9;
                        this.mFinished = true;
                        break;
                    }
                    break;
            }
        } else {
            if (i == 2) {
                startScroll(this.mCurrX, this.mCurrY, (int) (this.mDeltaX + (this.mFinalX - r8)), (int) (this.mDeltaY + (this.mFinalY - r9)), FLING_SCROLL_BACK_DURATION);
                this.mMode = 3;
                return true;
            }
            this.mCurrX = this.mFinalX;
            this.mCurrY = this.mFinalY;
            this.mFinished = true;
        }
        return true;
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, 250);
    }

    public void startScrollList(int startX, int startY, int dx, int dy, int duration) {
        startScroll(startX, startY, dx, dy, duration);
        this.mMode = 4;
        this.mViscousFluidNormalize = 1.0f;
        this.mViscousFluidNormalize = 1.0f / getInterpolation(1.0f);
        this.mLastCurrY = 0;
        this.mCount = 1;
    }

    public void startGalleryList(int startX, int startY, int dx, int dy, int duration) {
        startScroll(startX, startY, dx, dy, duration);
        this.mMode = 5;
        this.mCount = 1;
    }

    public void setCount(int n) {
        this.mCount = n;
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mMode = 0;
        this.mFinished = false;
        this.mDuration = duration;
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mStartX = startX;
        this.mStartY = startY;
        this.mFinalX = startX + dx;
        this.mFinalY = startY + dy;
        this.mDeltaX = dx;
        this.mDeltaY = dy;
        this.mDurationReciprocal = 1.0f / this.mDuration;
        this.mViscousFluidScale = 8.0f;
        this.mViscousFluidNormalize = 1.0f;
        this.mViscousFluidNormalize = 1.0f / viscousFluid(1.0f);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        this.mMode = 1;
        this.mFinished = false;
        float velocity = (float) Math.hypot(velocityX, velocityY);
        this.mVelocity = velocity;
        this.mDuration = (int) ((1000.0f * velocity) / this.mDeceleration);
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mStartX = startX;
        this.mStartY = startY;
        float f = velocity == 0.0f ? 1.0f : velocityX / velocity;
        this.mCoeffX = f;
        this.mCoeffY = velocity != 0.0f ? velocityY / velocity : 1.0f;
        int totalDistance = (int) ((velocity * velocity) / (this.mDeceleration * 2.0f));
        this.mMinX = minX;
        this.mMaxX = maxX;
        this.mMinY = minY;
        this.mMaxY = maxY;
        int round = Math.round(totalDistance * f) + startX;
        this.mFinalX = round;
        int min = Math.min(round, this.mMaxX);
        this.mFinalX = min;
        this.mFinalX = Math.max(min, this.mMinX);
        int round2 = Math.round(totalDistance * this.mCoeffY) + startY;
        this.mFinalY = round2;
        int min2 = Math.min(round2, this.mMaxY);
        this.mFinalY = min2;
        this.mFinalY = Math.max(min2, this.mMinY);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int springOffsetX, int springOffsetY) {
        fling(startX, startY, velocityX, velocityY, minX - springOffsetX, maxX + springOffsetX, minY - springOffsetY, maxY + springOffsetY);
        this.mDeltaY = 0.0f;
        this.mDeltaX = 0.0f;
        int i = this.mFinalX;
        if (i > maxX || i < minX) {
            this.mMode = 2;
            if (i > maxX) {
                this.mDeltaX = maxX - i;
            } else {
                this.mDeltaX = minX - i;
            }
        }
        int i2 = this.mFinalY;
        if (i2 > maxY || i2 < minY) {
            this.mMode = 2;
            if (i2 > maxY) {
                this.mDeltaY = maxY - i2;
            } else {
                this.mDeltaY = minY - i2;
            }
        }
    }

    private float viscousFluid(float x) {
        float x2;
        float x3 = x * this.mViscousFluidScale;
        if (x3 < 1.0f) {
            x2 = x3 - (1.0f - ((float) Math.exp(-x3)));
        } else {
            x2 = 0.36787945f + ((1.0f - 0.36787945f) * (1.0f - ((float) Math.exp(1.0f - x3))));
        }
        return x2 * this.mViscousFluidNormalize;
    }

    private float getInterpolation(float x) {
        return (1.0f - ((float) Math.exp(-(x * 12.0f)))) * (1.0f - 0.36787945f) * this.mViscousFluidNormalize;
    }

    public void abortAnimation() {
        this.mCurrX = this.mFinalX;
        this.mCurrY = this.mFinalY;
        this.mFinished = true;
    }

    public void extendDuration(int extend) {
        int passed = timePassed();
        int i = passed + extend;
        this.mDuration = i;
        this.mDurationReciprocal = 1.0f / i;
        this.mFinished = false;
    }

    public int timePassed() {
        return (int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
    }

    public void setFinalX(int newX) {
        this.mFinalX = newX;
        this.mDeltaX = newX - this.mStartX;
        this.mFinished = false;
    }

    public void setFinalY(int newY) {
        this.mFinalY = newY;
        this.mDeltaY = newY - this.mStartY;
        this.mFinished = false;
    }

    public final void setFriction(float friction) {
        this.mDeceleration = computeDeceleration(friction);
    }

    private float computeDeceleration(float friction) {
        return this.mPpi * 386.0878f * friction;
    }
}
