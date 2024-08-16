package android.widget;

import android.content.Context;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.Scroller;
import com.oplus.util.OplusContextUtil;

/* loaded from: classes.dex */
public class OplusOverScroller extends OverScroller {
    private static final boolean DBG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final int DEFAULT_DURATION = 250;
    private static final float DEFAULT_TIME_GAP = 16.0f;
    private static final int FLING_MODE = 1;
    private static final int MAXIMUM_FLING_VELOCITY_LIST = 2500;
    private static final int MAX_VELOCITY = 9000;
    public static final float OPLUS_FLING_FRICTION_FAST = 1.65f;
    public static final float OPLUS_FLING_FRICTION_NORMAL = 2.05f;
    public static final int OPLUS_FLING_MODE_FAST = 0;
    public static final int OPLUS_FLING_MODE_NORMAL = 1;
    private static final double PI = 3.1415926d;
    private static final int SCROLL_MODE = 0;
    private static final int SPRING_BACK_DURATION = 600;
    private static final String TAG = "OplusOverScroller";
    private static int sMaximumVelocity;
    private static int sOverscrollDistance;
    private final boolean mFlywheel;
    private Interpolator mInterpolator;
    private int mMode;
    private final OplusSplineOverScroller mScrollerX;
    private final OplusSplineOverScroller mScrollerY;

    public OplusOverScroller(Context context) {
        this(context, null);
    }

    public OplusOverScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    public OplusOverScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
        if (interpolator == null) {
            this.mInterpolator = new Scroller.ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
        this.mFlywheel = flywheel;
        this.mScrollerX = new OplusSplineOverScroller(context);
        this.mScrollerY = new OplusSplineOverScroller(context);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        sOverscrollDistance = metrics.heightPixels;
    }

    public OplusOverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY) {
        this(context, interpolator, true);
    }

    public OplusOverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY, boolean flywheel) {
        this(context, interpolator, flywheel);
    }

    public void setEnableScrollList(boolean flag) {
        this.mScrollerX.mIsScrollList = flag;
        this.mScrollerY.mIsScrollList = flag;
    }

    public static OverScroller newInstance(Context context) {
        if (OplusContextUtil.isOplusOSStyle(context)) {
            OverScroller mScroller = new OplusOverScroller(context);
            return mScroller;
        }
        OverScroller mScroller2 = new OverScroller(context);
        return mScroller2;
    }

    public static OverScroller newInstance(Context context, boolean isScrollList) {
        if (OplusContextUtil.isOplusOSStyle(context)) {
            OverScroller mScroller = new OplusOverScroller(context);
            OplusOverScroller mOplusScroller = (OplusOverScroller) mScroller;
            mOplusScroller.mScrollerX.mIsScrollList = isScrollList;
            mOplusScroller.mScrollerY.mIsScrollList = isScrollList;
            return mScroller;
        }
        return new OverScroller(context);
    }

    void setInterpolator(Interpolator interpolator) {
        if (interpolator == null) {
            this.mInterpolator = new Scroller.ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
    }

    public void setOplusFriction(float friction) {
        this.mScrollerX.setFriction(friction);
        this.mScrollerY.setFriction(friction);
    }

    public boolean isOplusFinished() {
        return this.mScrollerX.mFinished && this.mScrollerY.mFinished;
    }

    public final void oplusForceFinished(boolean finished) {
        this.mScrollerX.mFinished = finished;
        this.mScrollerY.mFinished = finished;
    }

    public int getOplusCurrX() {
        return this.mScrollerX.mCurrentPosition;
    }

    public int getOplusCurrY() {
        return this.mScrollerY.mCurrentPosition;
    }

    @Override // android.widget.OverScroller
    public float getCurrVelocity() {
        float squaredNorm = this.mScrollerX.mCurrVelocity * this.mScrollerX.mCurrVelocity;
        return FloatMath.sqrt(squaredNorm + (this.mScrollerY.mCurrVelocity * this.mScrollerY.mCurrVelocity));
    }

    public final int getOplusStartX() {
        return this.mScrollerX.mStart;
    }

    public final int getOplusStartY() {
        return this.mScrollerY.mStart;
    }

    public int getOplusFinalX() {
        return this.mScrollerX.mFinal;
    }

    public int getOplusFinalY() {
        return this.mScrollerY.mFinal;
    }

    @Deprecated
    public int getOplusDuration() {
        return Math.max(this.mScrollerX.mDuration, this.mScrollerY.mDuration);
    }

    @Deprecated
    public void extendDuration(int extend) {
        this.mScrollerX.extendDuration(extend);
        this.mScrollerY.extendDuration(extend);
    }

    @Deprecated
    public void setFinalX(int newX) {
        this.mScrollerX.setFinalPosition(newX);
    }

    @Deprecated
    public void setFinalY(int newY) {
        this.mScrollerY.setFinalPosition(newY);
    }

    @Override // android.widget.OverScroller
    public boolean computeScrollOffset() {
        if (isOplusFinished()) {
            return false;
        }
        switch (this.mMode) {
            case 0:
                long time = AnimationUtils.currentAnimationTimeMillis();
                long elapsedTime = time - this.mScrollerX.mStartTime;
                int duration = this.mScrollerX.mDuration;
                if (elapsedTime < duration) {
                    float q = this.mInterpolator.getInterpolation(((float) elapsedTime) / duration);
                    this.mScrollerX.updateScroll(q);
                    this.mScrollerY.updateScroll(q);
                    return true;
                }
                abortAnimation();
                return true;
            case 1:
                if (!this.mScrollerX.mFinished && !this.mScrollerX.update() && !this.mScrollerX.continueWhenFinished()) {
                    this.mScrollerX.finish();
                }
                if (!this.mScrollerY.mFinished && !this.mScrollerY.update() && !this.mScrollerY.continueWhenFinished()) {
                    this.mScrollerY.finish();
                    return true;
                }
                return true;
            default:
                return true;
        }
    }

    @Override // android.widget.OverScroller
    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, 250);
    }

    @Override // android.widget.OverScroller
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mMode = 0;
        this.mScrollerX.startScroll(startX, dx, duration);
        this.mScrollerY.startScroll(startY, dy, duration);
    }

    @Override // android.widget.OverScroller
    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        this.mMode = 1;
        boolean spingbackX = this.mScrollerX.springback(startX, minX, maxX);
        boolean spingbackY = this.mScrollerY.springback(startY, minY, maxY);
        return spingbackX || spingbackY;
    }

    @Override // android.widget.OverScroller
    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
    }

    @Override // android.widget.OverScroller
    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        int velocityY2;
        int velocityX2 = velocityX;
        if (this.mFlywheel && !isOplusFinished()) {
            float oldVelocityX = this.mScrollerX.mCurrVelocity;
            float oldVelocityY = this.mScrollerY.mCurrVelocity;
            if (Math.signum(velocityX2) == Math.signum(oldVelocityX) && Math.signum(velocityY) == Math.signum(oldVelocityY)) {
                velocityX2 = (int) (velocityX2 + oldVelocityX);
                velocityY2 = (int) (velocityY + oldVelocityY);
                this.mMode = 1;
                this.mScrollerX.fling(startX, velocityX2, minX, maxX, overX);
                this.mScrollerY.fling(startY, velocityY2, minY, maxY, overY);
            }
        }
        velocityY2 = velocityY;
        this.mMode = 1;
        this.mScrollerX.fling(startX, velocityX2, minX, maxX, overX);
        this.mScrollerY.fling(startY, velocityY2, minY, maxY, overY);
    }

    @Override // android.widget.OverScroller
    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        this.mScrollerX.notifyEdgeReached(startX, finalX, overX);
    }

    @Override // android.widget.OverScroller
    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        this.mScrollerY.notifyEdgeReached(startY, finalY, overY);
    }

    @Override // android.widget.OverScroller
    public boolean isOverScrolled() {
        return ((this.mScrollerX.mFinished || this.mScrollerX.mState == 0) && (this.mScrollerY.mFinished || this.mScrollerY.mState == 0)) ? false : true;
    }

    @Override // android.widget.OverScroller
    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    public int timePassed() {
        long time = AnimationUtils.currentAnimationTimeMillis();
        long startTime = Math.min(this.mScrollerX.mStartTime, this.mScrollerY.mStartTime);
        return (int) (time - startTime);
    }

    public boolean isScrollingInDirection(float xvel, float yvel) {
        int dx = this.mScrollerX.mFinal - this.mScrollerX.mStart;
        int dy = this.mScrollerY.mFinal - this.mScrollerY.mStart;
        return !isOplusFinished() && Math.signum(xvel) == Math.signum((float) dx) && Math.signum(yvel) == Math.signum((float) dy);
    }

    public void setFlingFriction(float friction) {
        this.mScrollerX.mOrigamiFriction = friction;
        this.mScrollerY.mOrigamiFriction = friction;
    }

    public void setOplusFlingMode(int mode) {
        switch (mode) {
            case 0:
                setFlingFriction(1.65f);
                return;
            case 1:
                return;
            default:
                throw new IllegalArgumentException("wrong fling argument");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class OplusSplineOverScroller {
        private static final int BALLISTIC = 2;
        private static final float BALLISTIC_THRESHOLD = 0.91f;
        private static final float BASE_DENSITY_FACTOR = 160.0f;
        private static final int CUBIC = 1;
        private static final float END_TENSION = 1.0f;
        private static final float FLING_CHANGE_INCREASE_STEP = 1.2f;
        private static final float FLING_CHANGE_REDUCE_STEP = 0.6f;
        private static final float FLING_CONTROL_ONE_X = 0.0f;
        private static final float FLING_CONTROL_ONE_Y = 0.17f;
        private static final float FLING_CONTROL_TWO_X = 0.25f;
        private static final float FLING_CONTROL_TWO_Y = 0.85f;
        private static final float FLING_DXDT_RATIO = 0.0694f;
        private static final int FLING_SPLINE = 3;
        private static final float FLOAT_1 = 1.0f;
        private static final float FLOAT_2 = 2.0f;
        private static final float FLOAT_25 = 25.0f;
        private static final float FLOAT_3 = 3.0f;
        private static final float FLOAT_8 = 8.0f;
        private static final float GRAVITY = 2000.0f;
        private static final float INCH_METER = 39.37f;
        private static final float INFLEXION = 0.35f;
        private static final int NB_SAMPLES = 100;
        private static final int NUM_10 = 10;
        private static final int NUM_100 = 100;
        private static final int NUM_1000 = 1000;
        private static final int NUM_60 = 60;
        private static final int NUM_800 = 800;
        private static final int OVER_SPLINE = 4;
        private static final float P1 = 0.175f;
        private static final float P2 = 0.35000002f;
        private static final float PHYSICAL_COFF_FACTOR = 0.84f;
        private static final double SOLVER_TIMESTEP_SEC = 0.016d;
        private static final int SPLINE = 0;
        private static final float START_TENSION = 0.5f;
        private static final float VISCOUS_FLUID_SCALE = 14.0f;
        private static float sViscousFluidNormalize;
        private float mCurrVelocity;
        private int mCurrentPosition;
        private float mDeceleration;
        private int mDuration;
        private double mEndValue;
        private int mFinal;
        private PathInterpolator mFlingInterpolator;
        private int mLastPosition;
        private double mLastVelocity;
        private int mOver;
        private boolean mOverSplineStart;
        private float mPhysicalCoeff;
        private float mReboundFriction;
        private int mScrollerDistance;
        private int mSplineDistance;
        private int mSplineState;
        private int mStart;
        private long mStartTime;
        private int mVelocity;
        private static final float DECELERATION_RATE = (float) (Math.log(0.78d) / Math.log(0.9d));
        private static final float[] SPLINE_POSITION = new float[101];
        private static final float[] SPLINE_TIME = new float[101];
        private static float sTimeIncrease = 1.0f;
        private int mOplusCount = 1;
        private float mStartV = 0.0f;
        private double mLastDetla = 0.0d;
        private boolean mIsScrollList = false;
        private boolean mOverSpring = false;
        private float mFlingFriction = ViewConfiguration.getScrollFriction();
        private int mState = 0;
        private float mOrigamiFriction = 2.05f;
        private float mReboundTension = 0.0f;
        private int mRestThreshold = 40;
        private boolean mFinished = true;

        void setFriction(float friction) {
            this.mFlingFriction = friction;
        }

        OplusSplineOverScroller(Context context) {
            float ppi = context.getResources().getDisplayMetrics().density * BASE_DENSITY_FACTOR;
            this.mPhysicalCoeff = 386.0878f * ppi * 0.84f;
            sViscousFluidNormalize = 1.0f;
            sViscousFluidNormalize = 1.0f / viscousFluid(1.0f, VISCOUS_FLUID_SCALE);
            this.mFlingInterpolator = new PathInterpolator(0.0f, FLING_CONTROL_ONE_Y, FLING_CONTROL_TWO_X, 0.85f);
        }

        void updateScroll(float q) {
            this.mCurrentPosition = this.mStart + Math.round((this.mFinal - r0) * q);
        }

        private static float getDeceleration(int velocity) {
            if (velocity > 0) {
                return -2000.0f;
            }
            return GRAVITY;
        }

        private void adjustDuration(int start, int oldFinal, int newFinal) {
            int oldDistance = oldFinal - start;
            int newDistance = newFinal - start;
            float x = Math.abs(newDistance / oldDistance);
            int index = (int) (x * 100.0f);
            if (index < 100) {
                float x_inf = index / 100.0f;
                float x_sup = (index + 1) / 100.0f;
                float[] fArr = SPLINE_TIME;
                float t_inf = fArr[index];
                float t_sup = fArr[index + 1];
                float timeCoef = (((x - x_inf) / (x_sup - x_inf)) * (t_sup - t_inf)) + t_inf;
                this.mDuration = (int) (this.mDuration * timeCoef);
            }
        }

        void startScroll(int start, int distance, int duration) {
            this.mFinished = false;
            this.mCurrentPosition = start;
            this.mStart = start;
            this.mFinal = start + distance;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = duration;
            this.mDeceleration = 0.0f;
            this.mVelocity = 0;
        }

        void finish() {
            this.mCurrentPosition = this.mFinal;
            sTimeIncrease = 1.0f;
            this.mFinished = true;
        }

        void setFinalPosition(int position) {
            this.mFinal = position;
            this.mFinished = false;
        }

        void extendDuration(int extend) {
            long time = AnimationUtils.currentAnimationTimeMillis();
            int elapsedTime = (int) (time - this.mStartTime);
            this.mDuration = elapsedTime + extend;
            this.mFinished = false;
        }

        boolean springback(int start, int min, int max) {
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "springback start=" + start + ", min=" + min + ", max=" + max);
            }
            this.mFinished = true;
            this.mCurrentPosition = start;
            this.mStart = start;
            this.mFinal = start;
            this.mVelocity = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 0;
            if (start < min) {
                startSpringback(start, min, 0);
            } else if (start > max) {
                startSpringback(start, max, 0);
            }
            return true ^ this.mFinished;
        }

        private void startSpringback(int start, int end, int velocity) {
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "startSpringback start=" + start + ", end=" + end + ", velocity=" + velocity);
            }
            this.mOplusCount = 1;
            this.mFinished = false;
            this.mState = 1;
            this.mCurrentPosition = start;
            this.mStart = start;
            this.mFinal = end;
            int delta = start - end;
            this.mDeceleration = getDeceleration(delta);
            this.mVelocity = -delta;
            this.mOver = Math.abs(delta);
            this.mDuration = (int) (Math.sqrt((delta * (-2.0d)) / this.mDeceleration) * 1000.0d);
        }

        void fling(int start, int velocity, int min, int max, int over) {
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "fling start=" + start + ", velocity=" + velocity + ", min=" + min + ", max=" + max + ", over=" + over);
            }
            this.mOplusCount = 1;
            this.mOver = over;
            this.mFinished = false;
            this.mReboundFriction = frictionFromOrigamiValue(this.mOrigamiFriction);
            this.mCurrVelocity = velocity;
            this.mVelocity = velocity;
            this.mDuration = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mCurrentPosition = start;
            this.mStart = start;
            this.mStartV = velocity;
            this.mLastPosition = start;
            this.mOverSpring = false;
            if (start > max || start < min) {
                this.mOverSpring = true;
                startAfterEdge(start, min, max, velocity);
                return;
            }
            this.mState = 0;
            double totalDistance = 0.0d;
            if (velocity != 0) {
                this.mDuration = getSplineFlingDuration(velocity) + 100;
                totalDistance = getSplineFlingDistance(velocity);
                this.mEndValue = totalDistance;
                this.mLastVelocity = velocity;
            }
            int signum = (int) (Math.signum(velocity) * totalDistance);
            this.mSplineDistance = signum;
            int i = signum + start;
            this.mFinal = i;
            if (i < min) {
                this.mFinal = min;
            }
            if (this.mFinal > max) {
                this.mFinal = max;
            }
            if (over != 0 && !this.mIsScrollList) {
                int i2 = this.mStart;
                this.mFinal = i2;
                if (i2 > OplusOverScroller.sOverscrollDistance || this.mFinal < (-OplusOverScroller.sOverscrollDistance)) {
                    float sign = Math.signum(velocity);
                    this.mFinal = ((int) sign) * OplusOverScroller.sOverscrollDistance;
                }
                this.mStart = 0;
                this.mSplineState = 3;
                this.mState = 2;
            }
        }

        private double getSplineDeceleration(int velocity) {
            return Math.log((Math.abs(velocity) * INFLEXION) / (this.mPhysicalCoeff * 0.006f));
        }

        private double getSplineFlingDistance(int velocity) {
            double l = getSplineDeceleration(velocity);
            float f = DECELERATION_RATE;
            double decelMinusOne = f - 1.0d;
            return this.mFlingFriction * this.mPhysicalCoeff * Math.exp((f / decelMinusOne) * l);
        }

        private int getSplineFlingDuration(int velocity) {
            double l = getSplineDeceleration(velocity);
            double decelMinusOne = DECELERATION_RATE - 1.0d;
            return (int) (Math.exp(l / decelMinusOne) * 1000.0d);
        }

        private void fitOnBounceCurve(int start, int end, int velocity) {
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "fitOnBounceCurve() start=" + start + ", end=" + end + ", velocity=" + velocity);
            }
            float f = this.mDeceleration;
            float durationToApex = (-velocity) / f;
            float distanceToApex = ((velocity * velocity) / FLOAT_2) / Math.abs(f);
            float distanceToEdge = Math.abs(end - start);
            float totalDuration = (float) Math.sqrt(((distanceToApex + distanceToEdge) * 2.0d) / Math.abs(this.mDeceleration));
            this.mStartTime -= (int) ((totalDuration - durationToApex) * 1000.0f);
            this.mStart = end;
            this.mVelocity = (int) ((-this.mDeceleration) * totalDuration);
        }

        private void startBounceAfterEdge(int start, int end, int velocity) {
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "startBounceAfterEdge() start=" + start + ", end=" + end + ", velocity=" + velocity);
            }
            this.mScrollerDistance = start;
            this.mDeceleration = getDeceleration(velocity == 0 ? start - end : velocity);
            fitOnBounceCurve(start, end, velocity);
            onEdgeReached();
        }

        private void startAfterEdge(int start, int min, int max, int velocity) {
            if (start > min && start < max) {
                Log.e("OverScroller", "startAfterEdge called from a valid position");
                this.mFinished = true;
                return;
            }
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "startAfterEdge() start=" + start + ", min=" + min + ", max=" + max + ", velocity=" + velocity);
            }
            boolean positive = start > max;
            int edge = positive ? max : min;
            int overDistance = start - edge;
            boolean keepIncreasing = overDistance * velocity >= 0;
            if (keepIncreasing) {
                startBounceAfterEdge(start, edge, velocity);
                return;
            }
            double totalDistance = getSplineFlingDistance(velocity);
            if (totalDistance > Math.abs(overDistance)) {
                fling(start, velocity, positive ? min : start, positive ? start : max, this.mOver);
            } else {
                startSpringback(start, edge, velocity);
            }
        }

        void notifyEdgeReached(int start, int end, int over) {
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "notifyEdgeReached() start=" + start + ", end=" + end + ", over=" + over);
            }
            if (this.mState == 0) {
                this.mOver = over;
                this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                this.mState = 1;
                startAfterEdge(start, end, end, (int) (this.mCurrVelocity / 1000.0f));
            }
        }

        private void onEdgeReached() {
            float sign = Math.signum(this.mVelocity);
            int i = this.mVelocity;
            float distance = (i * i) / 1600.0f;
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "onEdgeReached() mVelocity=" + this.mVelocity + ", distance=" + distance + ", mOver=" + this.mOver + ", mDeceleration=" + this.mDeceleration);
            }
            int i2 = this.mOver;
            if (distance > i2) {
                int i3 = this.mVelocity;
                this.mDeceleration = (((-sign) * i3) * i3) / (i2 * FLOAT_2);
                distance = i2;
            }
            this.mOplusCount = 1;
            this.mOver = (int) distance;
            this.mState = 2;
            int i4 = this.mStart;
            int i5 = this.mVelocity;
            this.mFinal = i4 + ((int) (i5 > 0 ? distance : -distance));
            int decelerate = i5 > 0 ? -800 : 800;
            this.mDuration = -((i5 * 1000) / decelerate);
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "onEdgeReached() mFinal=" + this.mFinal + ", mDuration=" + this.mDuration);
            }
            this.mSplineState = 4;
            this.mOverSplineStart = true;
        }

        boolean continueWhenFinished() {
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "continueWhenFinished mState=" + this.mState);
            }
            switch (this.mState) {
                case 0:
                    if (this.mIsScrollList && this.mOver != 0) {
                        int i = this.mFinal;
                        this.mCurrentPosition = i;
                        this.mStart = i;
                        this.mVelocity = ((int) this.mCurrVelocity) / 10;
                        this.mScrollerDistance = 0;
                        onEdgeReached();
                        break;
                    } else {
                        return false;
                    }
                case 1:
                    return false;
                case 2:
                    this.mStartTime += this.mDuration;
                    startSpringback(this.mFinal, this.mStart, 0);
                    break;
            }
            update();
            return true;
        }

        /* JADX WARN: Code restructure failed: missing block: B:41:0x0157, code lost:
        
            if ((r12 - r10) >= (r10 - r49.mScrollerDistance)) goto L40;
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x0169, code lost:
        
            r10 = r12;
            r49.mOplusCount++;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x0167, code lost:
        
            if ((r12 - r10) > (r10 - r49.mScrollerDistance)) goto L43;
         */
        /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0056. Please report as an issue. */
        /* JADX WARN: Type inference failed for: r4v13 */
        /* JADX WARN: Type inference failed for: r4v14 */
        /* JADX WARN: Type inference failed for: r4v15, types: [int, boolean] */
        /* JADX WARN: Type inference failed for: r4v16 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        boolean update() {
            ?? r4;
            boolean z;
            float x;
            float x2;
            long time = AnimationUtils.currentAnimationTimeMillis();
            long j = time - this.mStartTime;
            int timePassed = (int) (this.mOplusCount * OplusOverScroller.DEFAULT_TIME_GAP);
            if (OplusOverScroller.DBG) {
                Log.d(OplusOverScroller.TAG, "update() mState=" + this.mState + ", mOplusCount=" + this.mOplusCount + ", mCurrentPosition=" + this.mCurrentPosition);
            }
            double distance = 0.0d;
            switch (this.mState) {
                case 0:
                    double position = this.mLastPosition;
                    double velocity = this.mLastVelocity;
                    if (this.mOplusCount < 60) {
                        sTimeIncrease += 0.020000001f;
                        this.mReboundFriction += 0.020000001f;
                    } else {
                        float f = sTimeIncrease;
                        float f2 = f - ((f - 0.6f) / 60.0f);
                        sTimeIncrease = f2;
                        this.mReboundFriction -= (f2 - 0.6f) / 60.0f;
                    }
                    float f3 = this.mReboundTension;
                    double distance2 = f3;
                    double tempVelocity = this.mEndValue;
                    float f4 = this.mReboundFriction;
                    double tempPosition = f4;
                    double aAcceleration = (distance2 * (tempVelocity - 0.0d)) - (tempPosition * this.mLastVelocity);
                    double tempPosition2 = ((velocity * SOLVER_TIMESTEP_SEC) / 2.0d) + position;
                    double tempPosition3 = aAcceleration * SOLVER_TIMESTEP_SEC;
                    double tempVelocity2 = velocity + (tempPosition3 / 2.0d);
                    double d = f3 * (tempVelocity - tempPosition2);
                    double tempPosition4 = f4;
                    double bAcceleration = d - (tempPosition4 * tempVelocity2);
                    double tempPosition5 = position + ((tempVelocity2 * SOLVER_TIMESTEP_SEC) / 2.0d);
                    double tempPosition6 = bAcceleration * SOLVER_TIMESTEP_SEC;
                    double tempVelocity3 = velocity + (tempPosition6 / 2.0d);
                    double cAcceleration = (f3 * (tempVelocity - tempPosition5)) - (f4 * tempVelocity3);
                    double tempPosition7 = position + (tempVelocity3 * SOLVER_TIMESTEP_SEC);
                    double tempPosition8 = cAcceleration * SOLVER_TIMESTEP_SEC;
                    double tempVelocity4 = velocity + tempPosition8;
                    double dAcceleration = (f3 * (tempVelocity - tempPosition7)) - (f4 * tempVelocity4);
                    double dxdt = (((tempVelocity2 + tempVelocity3) * 2.0d) + velocity + tempVelocity4) * 0.06939999759197235d;
                    double dvdt = (aAcceleration + ((bAcceleration + cAcceleration) * 2.0d) + dAcceleration) * 0.06939999759197235d;
                    double d2 = position + (dxdt * SOLVER_TIMESTEP_SEC);
                    float currV = (float) (velocity + (dvdt * SOLVER_TIMESTEP_SEC));
                    double distance3 = currV * SOLVER_TIMESTEP_SEC;
                    double distance4 = Math.abs(distance3);
                    double aVelocity = this.mLastDetla;
                    if (distance4 <= aVelocity || this.mOplusCount <= 1) {
                        float f5 = this.mCurrVelocity;
                        int i = this.mRestThreshold;
                        if (f5 <= (-i) || f5 >= i) {
                            int delta = (int) Math.round(distance3);
                            if (delta == 0) {
                                delta = (int) (Math.abs(distance3) / distance3);
                            }
                            this.mCurrentPosition = this.mLastPosition + delta;
                            this.mLastDetla = Math.abs(distance3);
                            int i2 = this.mCurrentPosition;
                            this.mLastPosition = i2;
                            this.mOplusCount++;
                            this.mCurrVelocity = currV;
                            this.mLastVelocity = currV;
                            if (this.mIsScrollList) {
                                if ((delta > 0 && i2 >= this.mFinal) || (delta < 0 && i2 <= this.mFinal)) {
                                    this.mCurrentPosition = this.mFinal;
                                    return false;
                                }
                                return true;
                            }
                            return true;
                        }
                    }
                    this.mFinal = this.mCurrentPosition;
                    finish();
                    return false;
                case 1:
                    float x3 = this.mFlingInterpolator.getInterpolation(Math.min(timePassed * 0.0016666667f, 1.0f));
                    int i3 = this.mFinal;
                    int i4 = this.mStart;
                    distance = (i3 - i4) * x3;
                    long time2 = Math.round(distance);
                    this.mCurrentPosition = i4 + ((int) time2);
                    if (OplusOverScroller.DBG) {
                        Log.d(OplusOverScroller.TAG, "update CUBIC x=" + x3 + ", distance=" + distance + ", mFinal=" + this.mFinal + ", mCurrentPosition=" + this.mCurrentPosition);
                    }
                    int i5 = this.mCurrentPosition;
                    int i6 = this.mFinal;
                    if (i5 != i6) {
                        r4 = 1;
                        this.mOplusCount += r4;
                        this.mCurrentPosition = this.mStart + ((int) Math.round(distance));
                        return r4;
                    }
                    this.mCurrentPosition = i6;
                    finish();
                    return false;
                case 2:
                    if (this.mSplineState == 4) {
                        if (!this.mIsScrollList || !this.mOverSpring) {
                            float x4 = viscousFluid(timePassed * (1.0f / this.mDuration), VISCOUS_FLUID_SCALE);
                            distance = (this.mFinal - this.mStart) * x4;
                            if (OplusOverScroller.DBG) {
                                Log.d(OplusOverScroller.TAG, "update mSplineState == OVER_SPLINE x=" + x4 + ", distance=" + distance + ", mFinal=" + this.mFinal + ", mScrollerDistance=" + this.mScrollerDistance);
                            }
                            while (true) {
                                int i7 = this.mFinal;
                                if (((i7 < 0 && distance >= this.mScrollerDistance) || (i7 > 0 && distance <= this.mScrollerDistance)) && !this.mIsScrollList) {
                                    this.mOplusCount = this.mOplusCount + 1;
                                    float x5 = viscousFluid(((int) (r7 * OplusOverScroller.DEFAULT_TIME_GAP)) * (1.0f / this.mDuration), VISCOUS_FLUID_SCALE);
                                    distance = (this.mFinal - this.mStart) * x5;
                                    if (OplusOverScroller.DBG) {
                                        Log.d(OplusOverScroller.TAG, "update while mOplusCount=" + this.mOplusCount + ", distance=" + distance);
                                    }
                                    this.mOverSplineStart = true;
                                    x4 = x5;
                                }
                            }
                            if (this.mOverSplineStart) {
                                float x6 = viscousFluid(((int) ((this.mOplusCount + 1) * OplusOverScroller.DEFAULT_TIME_GAP)) * (1.0f / this.mDuration), VISCOUS_FLUID_SCALE);
                                int i8 = this.mFinal;
                                double nextDistance = (i8 - this.mStart) * x6;
                                if (i8 < 0) {
                                    x2 = x6;
                                    break;
                                } else {
                                    x2 = x6;
                                }
                                if (i8 > 0) {
                                    break;
                                }
                                this.mOverSplineStart = false;
                                x = x2;
                            } else {
                                x = x4;
                            }
                            int i9 = this.mFinal;
                            if ((i9 >= 0 || this.mCurrentPosition > i9) && ((i9 <= 0 || this.mCurrentPosition < i9) && x <= BALLISTIC_THRESHOLD && Math.round(distance) != 0)) {
                                r4 = 1;
                                this.mOplusCount += r4;
                                this.mCurrentPosition = this.mStart + ((int) Math.round(distance));
                                return r4;
                            }
                            this.mFinal = this.mStart + ((int) Math.round(distance));
                            return false;
                        }
                        z = false;
                    } else {
                        z = false;
                    }
                    this.mFinal = this.mCurrentPosition;
                    return z;
                default:
                    r4 = 1;
                    this.mOplusCount += r4;
                    this.mCurrentPosition = this.mStart + ((int) Math.round(distance));
                    return r4;
            }
        }

        private static float viscousFluid(float x, float distance) {
            float x2 = ((1.0f - 0.36787945f) * (1.0f - ((float) Math.exp(1.0f - ((((1.0f - ((float) Math.log(1.0f / (1.0f - 0.36787945f)))) / distance) + x) * distance))))) + 0.36787945f;
            float x3 = sViscousFluidNormalize;
            return x2 * x3;
        }

        private float frictionFromOrigamiValue(float value) {
            if (value == 0.0f) {
                return 0.0f;
            }
            return ((value - 8.0f) * FLOAT_3) + FLOAT_25;
        }
    }
}
