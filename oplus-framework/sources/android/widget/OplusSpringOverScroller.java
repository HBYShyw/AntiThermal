package android.widget;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.view.Choreographer;
import android.view.Display;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.oplus.dynamicframerate.DynamicFrameRateController;
import com.oplus.scrolloptim.ScrOptController;

/* loaded from: classes.dex */
public class OplusSpringOverScroller {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_REFRESH_RATE = 60;
    private static final int FLING_MODE = 1;
    public static final float OPLUS_FLING_FRICTION_FAST = 0.76f;
    public static final float OPLUS_FLING_FRICTION_LIST_OPTIMIZE = 0.008f;
    public static final int OPLUS_FLING_MODE_FAST = 0;
    public static final int OPLUS_FLING_MODE_NORMAL = 1;
    private static final int PARAM_INDEX_FRICTION_FOR_HIGH_VELOCITY = 3;
    private static final int PARAM_INDEX_FRICTION_FOR_LOW_VELOCITY = 5;
    private static final int PARAM_INDEX_FRICTION_FOR_MID_VELOCITY = 4;
    private static final int PARAM_INDEX_HIGH_VELOCITY = 2;
    private static final int PARAM_INDEX_LOW_VELOCITY = 0;
    private static final int PARAM_INDEX_MID_VELOCITY = 1;
    private static final int SCROLL_DEFAULT_DURATION = 250;
    private static final int SCROLL_MODE = 0;
    private static final float SOLVER_TIMESTEP_SEC = 0.016f;
    private static int mRefreshRate;
    private Context mContext;
    private Interpolator mInterpolator;
    private int mMode;
    private ReboundOverScroller mScrollerX;
    private ReboundOverScroller mScrollerY;
    private static final String TAG = OplusSpringOverScroller.class.getSimpleName();
    public static float OPLUS_FLING_FRICTION_NORMAL = 0.12f;

    public void setScrollXIsScrollView(boolean isScrollView) {
    }

    public void setScrollYIsScrollView(boolean isScrollView) {
    }

    private float getRefreshTime(Context context) {
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        Display display = displayManager.getDisplay(0);
        if (display == null) {
            return SOLVER_TIMESTEP_SEC;
        }
        float refreshTime = display.getRefreshRate();
        return Math.round(10000.0f / refreshTime) / 10000.0f;
    }

    private int getRefreshRate(Context context) {
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        Display display = displayManager.getDisplay(0);
        if (display == null) {
            return 60;
        }
        return (int) display.getRefreshRate();
    }

    public OplusSpringOverScroller(Context context, Interpolator interpolator) {
        this.mScrollerX = new ReboundOverScroller();
        this.mScrollerY = new ReboundOverScroller();
        this.mScrollerX.setScrollerName("ScrollerX - ");
        this.mScrollerY.setScrollerName("ScrollerY - ");
        if (interpolator == null) {
            this.mInterpolator = new Scroller.ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
        this.mContext = context;
    }

    public OplusSpringOverScroller(Context context) {
        this(context, null);
    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null) {
            this.mInterpolator = new Scroller.ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
    }

    public boolean computeScrollOffset() {
        if (isFinished()) {
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
                this.mScrollerX.updateScroll(1.0f);
                this.mScrollerY.updateScroll(1.0f);
                abortAnimation();
                return true;
            case 1:
                if (!this.mScrollerX.isFinished() && !this.mScrollerX.update() && !this.mScrollerX.continueWhenFinished()) {
                    this.mScrollerX.finish();
                }
                if (!this.mScrollerY.isFinished() && !this.mScrollerY.update() && !this.mScrollerY.continueWhenFinished()) {
                    this.mScrollerY.finish();
                    return true;
                }
                return true;
            default:
                return true;
        }
    }

    public boolean isFinished() {
        return this.mScrollerX.isFinished() && this.mScrollerY.isFinished();
    }

    public final boolean isOplusFinished() {
        return this.mScrollerX.isFinished() && this.mScrollerY.isFinished();
    }

    public int getCurrX() {
        return (int) this.mScrollerX.getCurrentValue();
    }

    public final int getOplusCurrX() {
        return (int) Math.round(this.mScrollerX.getCurrentValue());
    }

    public int getCurrY() {
        return (int) this.mScrollerY.getCurrentValue();
    }

    public final int getOplusCurrY() {
        return (int) Math.round(this.mScrollerY.getCurrentValue());
    }

    public int getFinalX() {
        return (int) this.mScrollerX.getEndValue();
    }

    public final int getOplusFinalX() {
        return (int) Math.round(this.mScrollerX.getEndValue());
    }

    public int getFinalY() {
        return (int) this.mScrollerY.getEndValue();
    }

    public final int getOplusFinalY() {
        return (int) Math.round(this.mScrollerY.getEndValue());
    }

    public final int getOplusStartX() {
        return this.mScrollerX.mStart;
    }

    public final int getOplusStartY() {
        return this.mScrollerY.mStart;
    }

    public void setFinalX(int newX) {
        this.mScrollerX.setEndValue(newX);
    }

    public void setFinalY(int newY) {
        this.mScrollerY.setEndValue(newY);
    }

    public final void forceFinished(boolean finished) {
        ReboundOverScroller reboundOverScroller = this.mScrollerX;
        this.mScrollerY.mFinished = finished;
        reboundOverScroller.mFinished = finished;
    }

    public void extendDuration(int extend) {
        this.mScrollerX.extendDuration(extend);
        this.mScrollerY.extendDuration(extend);
    }

    public boolean isOverScrolled() {
        boolean scrollerXOverScrolled = (this.mScrollerX.mFinished || this.mScrollerX.mState == 0) ? false : true;
        boolean scrollerYOverScrolled = (this.mScrollerY.mFinished || this.mScrollerY.mState == 0) ? false : true;
        return scrollerXOverScrolled || scrollerYOverScrolled;
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        this.mMode = 1;
        log("OverScroller fling startX " + startX + " startY " + startY + " velocityX " + velocityX + " velocityY " + velocityY + " minX " + minX + " maxX " + maxX + " minY " + minY + " maxY " + maxY + " overX " + overX + " overY=" + overY);
        this.mScrollerX.fling(startX, minX, maxX, velocityX, overX);
        this.mScrollerY.fling(startY, minY, maxY, velocityY, overY);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
    }

    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        boolean springBackX = this.mScrollerX.springBack(startX, minX, maxX);
        boolean springBackY = this.mScrollerY.springBack(startY, minY, maxY);
        this.mMode = 1;
        return springBackX || springBackY;
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, 250);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mMode = 0;
        this.mScrollerX.startScroll(startX, dx, duration);
        this.mScrollerY.startScroll(startY, dy, duration);
    }

    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    public int timePassed() {
        return -1;
    }

    public boolean isScrollingInDirection(float xvel, float yvel) {
        int dx = (int) (this.mScrollerX.mFinal - this.mScrollerX.mStart);
        int dy = (int) (this.mScrollerY.mFinal - this.mScrollerY.mStart);
        return !isFinished() && Math.signum(xvel) == Math.signum((float) dx) && Math.signum(yvel) == Math.signum((float) dy);
    }

    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        this.mScrollerY.notifyEdgeReached(startY, finalY, overY);
        springBack(0, startY, 0, 0, 0, 0);
    }

    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        this.mScrollerX.notifyEdgeReached(startX, finalX, overX);
        springBack(startX, 0, 0, 0, 0, 0);
    }

    public float getCurrVelocity() {
        double velX = this.mScrollerX.getVelocity();
        double velY = this.mScrollerY.getVelocity();
        return (int) Math.sqrt((velX * velX) + (velY * velY));
    }

    public void setOplusFriction(float friction) {
    }

    public void setFriction(float friction) {
        this.mScrollerX.setFriction(friction);
        this.mScrollerY.setFriction(friction);
    }

    public void setFlingFriction(float friction) {
        this.mScrollerX.mFlingFriction = friction;
        this.mScrollerY.mFlingFriction = friction;
    }

    public final int getDuration() {
        return Math.max(this.mScrollerX.mDuration, this.mScrollerY.mDuration);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:10:0x0041. Please report as an issue. */
    public void setSpringConfigFromRus(String springConfig) {
        String springConfig2 = springConfig.replaceAll(" ", "");
        int strLength = springConfig2.length();
        int indexOfLeftParenthesis = springConfig2.indexOf("(");
        if (indexOfLeftParenthesis >= strLength - 1) {
            Log.d(TAG, "index out of range : spring_config " + springConfig2);
            return;
        }
        String paramStr = springConfig2.substring(indexOfLeftParenthesis + 1, strLength - 1);
        try {
            String[] params = paramStr.split(",");
            int paramsLength = params.length;
            for (int i = 0; i < paramsLength; i++) {
                switch (i) {
                    case 0:
                        this.mScrollerX.setLowVelocityThreshold(Integer.valueOf(params[i]).intValue());
                        this.mScrollerY.setLowVelocityThreshold(Integer.valueOf(params[i]).intValue());
                    case 1:
                        this.mScrollerX.setMidVelocityThreshold(Integer.valueOf(params[i]).intValue());
                        this.mScrollerY.setMidVelocityThreshold(Integer.valueOf(params[i]).intValue());
                    case 2:
                        this.mScrollerX.setHighVelocityThreshold(Integer.valueOf(params[i]).intValue());
                        this.mScrollerY.setHighVelocityThreshold(Integer.valueOf(params[i]).intValue());
                    case 3:
                        OPLUS_FLING_FRICTION_NORMAL = Float.valueOf(params[i]).floatValue();
                        Log.d(TAG, "set OPLUS_FLING_FRICTION_NORMAL " + OPLUS_FLING_FRICTION_NORMAL);
                    case 4:
                        this.mScrollerX.setMidVelocityFriction(Float.valueOf(params[i]).floatValue());
                        this.mScrollerY.setMidVelocityFriction(Float.valueOf(params[i]).floatValue());
                    case 5:
                        this.mScrollerX.setLowVelocityFriction(Float.valueOf(params[i]).floatValue());
                        this.mScrollerY.setLowVelocityFriction(Float.valueOf(params[i]).floatValue());
                    default:
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to parse spring_config " + springConfig2 + " " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ReboundOverScroller {
        private static final int BALLISTIC = 2;
        private static final int CUBIC = 1;
        private static final double DELTA_POSITION_HIGH = 0.35d;
        private static final double DELTA_POSITION_LOW = 0.2d;
        private static final double DELTA_POSITION_MID = 0.3d;
        private static final double DELTA_POSITION_VELOCITY_HIGH = 8000.0d;
        private static final double DELTA_POSITION_VELOCITY_LOW = 5000.0d;
        private static final float FLING_CHANGE_INCREASE_STEP = 1.2f;
        private static final float FLING_CHANGE_REDUCE_STEP = 0.6f;
        private static final float FLING_DXDT_RATIO = 0.167f;
        private static final float FLOAT_1 = 1.0f;
        private static final float FLOAT_2 = 2.0f;
        private static final float GRAVITY = 2000.0f;
        private static final double INCREASE_FRICTION_COEF = 0.00125d;
        private static final double MIN_FLING_FRICTION_REDUCE = 2.0d;
        private static final int NUM_60 = 60;
        private static final double REDUCE_FRICTION_COEF = 0.00125d;
        private static final int SPLINE = 0;
        private static final int SPRING_BACK_ADJUST_TENSION_VALUE = 100;
        private static final int SPRING_BACK_ADJUST_THRESHOLD = 180;
        private static final float SPRING_BACK_FRICTION = 12.19f;
        private static final int SPRING_BACK_STOP_THRESHOLD = 2;
        private static final float SPRING_BACK_TENSION = 16.0f;
        private static final long TIME_ADJUST_FRICTION = 480;
        private static final double VELOCITY_REDUCE_FRICTION = 2000.0d;
        private static final float sTimeIncrease = 1.0f;
        private ReboundConfig mConfig;
        private PhysicsState mCurrentState;
        private float mDeceleration;
        private double mDisplacementFromRestThreshold;
        private int mDuration;
        private double mFinal;
        private boolean mFinished;
        private ReboundConfig mFlingConfig;
        private float mFlingFriction;
        private boolean mIsSpringBack;
        private long mLastFlingUpdateTime;
        private int mMax;
        private int mMin;
        private int mOplusCount;
        private int mOver;
        private PhysicsState mPreviousState;
        private float mRefreshTime;
        private double mRestSpeedThreshold;
        private String mScrollerName;
        private int mSplineDistance;
        private int mSplineDuration;
        private double mSplineMinDelta;
        private ReboundConfig mSpringBackConfig;
        private float mSpringBackTensionMultiple;
        private int mStart;
        private long mStartTime;
        private int mState;
        private PhysicsState mTempState;
        private boolean mTensionAdjusted;
        private static double MIN_VELOCITY_ADJUST_FRICTION = 1000.0d;
        private static double MID_VELOCITY_ADJUST_FRICTION = 4000.0d;
        private static final double FLING_FRICTION_DIVISOR = 10000.0d;
        private static double MAX_VELOCITY_ADJUST_FRICTION = FLING_FRICTION_DIVISOR;
        private static double MID_FLING_BASE_FRICTION = 3.4d;
        private static double SLOW_FLING_BASE_FRICTION = 3.8d;

        private ReboundOverScroller() {
            this.mState = 0;
            float f = OplusSpringOverScroller.OPLUS_FLING_FRICTION_NORMAL;
            this.mFlingFriction = f;
            this.mRestSpeedThreshold = 5.0d;
            this.mDisplacementFromRestThreshold = 0.05d;
            this.mOplusCount = 1;
            this.mSpringBackTensionMultiple = 0.83f;
            this.mFlingConfig = new ReboundConfig(f, 0.0d);
            this.mSpringBackConfig = new ReboundConfig(12.1899995803833d, 16.0d);
            this.mCurrentState = new PhysicsState();
            this.mPreviousState = new PhysicsState();
            this.mTempState = new PhysicsState();
            setConfig(this.mFlingConfig);
            this.mFinished = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void fling(int start, int min, int max, int velocity, int over) {
            this.mSplineMinDelta = getSplineMinDelta(Math.abs(velocity));
            int velocity2 = (int) (velocity / 1.5d);
            OplusSpringOverScroller.log(this + " fling start " + start + " min " + min + " max " + max + " velocity " + velocity2 + " over " + over);
            this.mMin = min;
            this.mMax = max;
            this.mOver = over;
            this.mFinished = false;
            initFling(start, velocity2);
            this.mSplineDuration = 0;
            this.mDuration = 0;
            if (start < min || start > max) {
                startAfterEdge(start, min, max, velocity2);
                return;
            }
            this.mState = 0;
            int[] simulateResult = calculateFinalPosition(-1);
            double finalPosition = simulateResult[0];
            float duration = simulateResult[1];
            this.mSplineDistance = (int) (finalPosition - start);
            int i = (int) duration;
            this.mSplineDuration = i;
            this.mDuration = i;
            this.mFinal = finalPosition;
            OplusSpringOverScroller.log(this + " fling splineDistance " + this.mSplineDistance + " mDuration " + this.mDuration + " mFinal " + this.mFinal + " finalDistance " + finalPosition);
            initFling(start, velocity2);
            if (this.mFinal < min) {
                this.mFinal = min;
                adjustDuration();
                initFling(start, velocity2);
            }
            if (this.mFinal > max) {
                this.mFinal = max;
                adjustDuration();
                initFling(start, velocity2);
            }
            OplusSpringOverScroller.log(this + " fling after adjust splineDistance " + this.mSplineDistance + " mDuration " + this.mDuration + " mFinal " + this.mFinal);
            onFlingStart(this.mDuration, velocity2, start);
        }

        private void initFling(int start, int velocity) {
            this.mOplusCount = 1;
            this.mFinished = false;
            this.mFlingConfig.setFriction(this.mFlingFriction);
            this.mFlingConfig.setTension(0.0d);
            setConfig(this.mFlingConfig);
            setCurrentValue(start);
            setVelocity(velocity);
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mLastFlingUpdateTime = AnimationUtils.currentAnimationTimeMillis();
        }

        private double getSplineMinDelta(float absVelocity) {
            if (absVelocity <= DELTA_POSITION_VELOCITY_LOW) {
                return DELTA_POSITION_LOW;
            }
            if (absVelocity > DELTA_POSITION_VELOCITY_HIGH) {
                return DELTA_POSITION_HIGH;
            }
            return DELTA_POSITION_MID;
        }

        private void adjustDuration() {
            int[] simulateResult = calculateFinalPosition((int) this.mFinal);
            float duration = simulateResult[1];
            OplusSpringOverScroller.log(this + " adjustDuration old duration " + this.mDuration + " new duration = " + duration + " splineDuration = " + this.mSplineDuration);
            this.mDuration = (int) duration;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setScrollerName(String name) {
            this.mScrollerName = name;
        }

        private void startAfterEdge(int start, int min, int max, int velocity) {
            boolean z;
            OplusSpringOverScroller.log(this + " startAfterEdge start " + start + " min " + min + " max " + max + " velocity" + velocity);
            boolean z2 = true;
            if (start > min && start < max) {
                this.mFinished = true;
                return;
            }
            if (start > max) {
                z = true;
            } else {
                z = false;
            }
            boolean positive = z;
            int edge = positive ? max : min;
            int overDistance = start - edge;
            if (overDistance * velocity < 0) {
                z2 = false;
            }
            boolean keepIncreasing = z2;
            if (!keepIncreasing) {
                int[] simulateResult = calculateFinalPosition(-1);
                double totalDistance = Math.abs(start - simulateResult[0]);
                if (totalDistance > Math.abs(overDistance)) {
                    fling(start, positive ? min : start, positive ? start : max, velocity, this.mOver);
                    return;
                } else {
                    springBack(start, min, max);
                    return;
                }
            }
            startBounceAfterEdge(start, edge, velocity);
        }

        private static float getDeceleration(int velocity) {
            if (velocity > 0) {
                return -2000.0f;
            }
            return GRAVITY;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void extendDuration(int extend) {
            long time = AnimationUtils.currentAnimationTimeMillis();
            int elapsedTime = (int) (time - this.mStartTime);
            int i = elapsedTime + extend;
            this.mSplineDuration = i;
            this.mDuration = i;
            this.mFinished = false;
        }

        private void fitOnBounceCurve(int start, int end, int velocity) {
            float f = this.mDeceleration;
            float durationToApex = (-velocity) / f;
            float velocitySquared = velocity * velocity;
            float distanceToApex = (velocitySquared / FLOAT_2) / Math.abs(f);
            float distanceToEdge = Math.abs(end - start);
            float totalDuration = (float) Math.sqrt(((distanceToApex + distanceToEdge) * MIN_FLING_FRICTION_REDUCE) / Math.abs(this.mDeceleration));
            this.mStartTime -= (int) ((totalDuration - durationToApex) * 1000.0f);
            PhysicsState physicsState = this.mCurrentState;
            this.mStart = end;
            physicsState.mPosition = end;
            this.mCurrentState.mVelocity = (int) ((-this.mDeceleration) * totalDuration);
        }

        private void onEdgeReached() {
            setConfig(this.mSpringBackConfig);
            float distance = Math.abs((float) (this.mSplineDistance - this.mFinal));
            int i = this.mOver;
            if (distance > i) {
                distance = i;
            }
            this.mOver = (int) distance;
            this.mState = 2;
            this.mFinal = this.mStart + ((int) (this.mCurrentState.mVelocity > 0.0d ? distance : -distance));
            int[] simulateResult = calculateFinalPosition(-1);
            int ballisticDuration = simulateResult[1];
            this.mDuration = ballisticDuration;
        }

        private void startBounceAfterEdge(int start, int end, int velocity) {
            this.mDeceleration = getDeceleration(velocity == 0 ? start - end : velocity);
            OplusSpringOverScroller.log(this + "startBounceAfterEdge");
            fitOnBounceCurve(start, end, velocity);
            onEdgeReached();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean continueWhenFinished() {
            OplusSpringOverScroller.log(this + "continueWhenFinished");
            switch (this.mState) {
                case 0:
                    int i = this.mDuration;
                    if (i < this.mSplineDuration) {
                        this.mStartTime += i;
                        this.mCurrentState.mPosition = this.mFinal;
                        this.mStart = (int) this.mFinal;
                        this.mDeceleration = getDeceleration((int) this.mCurrentState.mVelocity);
                        onEdgeReached();
                        OplusSpringOverScroller.log(this + "SPLINE continue");
                        break;
                    } else {
                        OplusSpringOverScroller.log(this + "SPLINE stop");
                        return false;
                    }
                case 1:
                    OplusSpringOverScroller.log(this + "CUBIC stop");
                    return false;
                case 2:
                    this.mStartTime += this.mDuration;
                    startSpringBack((int) this.mFinal, this.mStart, 0);
                    OplusSpringOverScroller.log(this + "BALLISTIC continue");
                    break;
            }
            update();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean springBack(int start, int min, int max) {
            this.mFinished = true;
            PhysicsState physicsState = this.mCurrentState;
            this.mStart = start;
            physicsState.mPosition = start;
            this.mFinal = start;
            this.mDuration = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            OplusSpringOverScroller.log(this + " spring start " + start + " min " + min + " max " + max);
            if (start < min) {
                startSpringBack(start, min, 0);
            }
            if (start > max) {
                startSpringBack(start, max, 0);
            }
            return true ^ this.mFinished;
        }

        private void startSpringBack(int start, int end, int velocity) {
            setConfig(this.mSpringBackConfig);
            this.mState = 1;
            this.mFinished = false;
            PhysicsState physicsState = this.mCurrentState;
            this.mStart = start;
            physicsState.mPosition = start;
            PhysicsState physicsState2 = this.mCurrentState;
            physicsState2.mVelocity = -physicsState2.mVelocity;
            this.mFinal = end;
            int delta = end - start;
            this.mOver = Math.abs(delta);
            this.mTempState.mPosition = 0.0d;
            this.mTempState.mVelocity = 0.0d;
            int[] simulateResult = calculateFinalPosition(-1);
            this.mDuration = simulateResult[1];
            OplusSpringOverScroller.log(this + " startSpringBack end mDuration " + this.mDuration + " mCurrentState.mPosition = " + this.mCurrentState.mPosition + " mCurrentState.mVelocity = " + this.mCurrentState.mVelocity);
            this.mFinished = false;
            this.mTempState.mPosition = this.mCurrentState.mPosition;
            this.mTempState.mVelocity = this.mCurrentState.mVelocity;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startScroll(int start, int distance, int duration) {
            this.mStart = start;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mFinal = start + distance;
            this.mDuration = duration;
            this.mFinished = false;
            setConfig(this.mFlingConfig);
        }

        private void setConfig(ReboundConfig config) {
            this.mConfig = config;
        }

        private void setCurrentValue(int currentValue) {
            PhysicsState physicsState = this.mCurrentState;
            this.mStart = currentValue;
            physicsState.mPosition = currentValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setFriction(float friction) {
            this.mFlingFriction = friction;
            this.mFlingFriction = friction;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double getCurrentValue() {
            return this.mCurrentState.mPosition;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double getVelocity() {
            return this.mCurrentState.mVelocity;
        }

        private void setVelocity(double velocity) {
            this.mCurrentState.mVelocity = velocity;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double getEndValue() {
            return this.mFinal;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setEndValue(double endValue) {
            this.mFinal = endValue;
            this.mSplineDistance = (int) (endValue - this.mStart);
            this.mFinished = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void finish() {
            this.mCurrentState.mPosition = this.mFinal;
            this.mTempState.mPosition = this.mCurrentState.mPosition;
            this.mCurrentState.mVelocity = 0.0d;
            this.mIsSpringBack = false;
            this.mFinished = true;
            onFlingFinish();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isFinished() {
            return this.mFinished;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateScroll(float q) {
            PhysicsState physicsState = this.mCurrentState;
            int i = this.mStart;
            physicsState.mPosition = i + Math.round(q * (this.mFinal - i));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyEdgeReached(int start, int end, int over) {
            this.mCurrentState.mPosition = start;
            this.mPreviousState.mPosition = 0.0d;
            this.mPreviousState.mVelocity = 0.0d;
            this.mTempState.mPosition = 0.0d;
            this.mTempState.mVelocity = 0.0d;
        }

        private double getDisplacementDistanceForState(PhysicsState state) {
            return Math.abs(this.mFinal - state.mPosition);
        }

        private int[] calculateFinalPosition(int finalValue) {
            int savedPosition = (int) this.mCurrentState.mPosition;
            int savedVelocity = (int) this.mCurrentState.mVelocity;
            int savedFrameCount = this.mOplusCount;
            boolean savedFinished = isFinished();
            this.mTempState.mPosition = 0.0d;
            this.mTempState.mVelocity = 0.0d;
            OplusSpringOverScroller.log(this + " calculateFinalPosition finalValue " + finalValue + " savedPosition " + savedPosition + " savedVelocity " + savedVelocity);
            this.mRefreshTime = ((float) (Choreographer.getInstance().getFrameIntervalNanos() / 1000000)) / 1000.0f;
            this.mOplusCount = 1;
            while (!isFinished()) {
                double originPosition = this.mCurrentState.mPosition;
                calculateOnceWithRebound(true);
                double newPosition = this.mCurrentState.mPosition;
                double deltaPosition = Math.abs(newPosition - originPosition);
                if (lostVelocity()) {
                    OplusSpringOverScroller.log(this + " calculateFinalPosition lostVelocity");
                    this.mFinished = true;
                }
                if (deltaPosition < this.mSplineMinDelta) {
                    OplusSpringOverScroller.log(this + " calculateFinalPosition deltaPosition < " + this.mSplineMinDelta);
                    this.mFinished = true;
                }
                if (finalValue != -1 && (originPosition - finalValue) * (newPosition - finalValue) <= 0.0d) {
                    this.mCurrentState.mPosition = this.mFinal;
                    OplusSpringOverScroller.log(this + " calculateFinalPosition reaching edge " + this.mFinal);
                    this.mFinished = true;
                }
            }
            int resultPosition = (int) this.mCurrentState.mPosition;
            int resultDuration = (int) (this.mOplusCount * this.mRefreshTime * 1000.0f);
            this.mCurrentState.mPosition = savedPosition;
            this.mCurrentState.mVelocity = savedVelocity;
            this.mOplusCount = savedFrameCount;
            this.mTempState.mPosition = 0.0d;
            this.mTempState.mVelocity = 0.0d;
            this.mFinished = savedFinished;
            return new int[]{resultPosition, resultDuration};
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean update() {
            if (isFinished()) {
                return false;
            }
            float frameInternalTime = ((float) (Choreographer.getInstance().getFrameIntervalNanos() / 1000000)) / 1000.0f;
            float realInternalTime = ((float) (AnimationUtils.currentAnimationTimeMillis() - this.mLastFlingUpdateTime)) / 1000.0f;
            this.mRefreshTime = Math.min(frameInternalTime, realInternalTime);
            this.mLastFlingUpdateTime = AnimationUtils.currentAnimationTimeMillis();
            double positionBeforeUpdate = this.mCurrentState.mPosition;
            calculateOnceWithRebound(false);
            double positionAfterUpdate = this.mCurrentState.mPosition;
            double deltaPosition = Math.abs(positionAfterUpdate - positionBeforeUpdate);
            if (deltaPosition < this.mSplineMinDelta && this.mRefreshTime != 0.0f) {
                return false;
            }
            if (this.mState == 2 && lostVelocity()) {
                return false;
            }
            double d = positionBeforeUpdate - this.mFinal;
            double d2 = this.mCurrentState.mPosition;
            double d3 = this.mFinal;
            if (d * (d2 - d3) > 0.0d) {
                onFlingPositionUpdate((int) this.mCurrentState.mVelocity, (int) this.mCurrentState.mPosition);
                return true;
            }
            this.mCurrentState.mPosition = d3;
            return false;
        }

        private boolean lostVelocity() {
            if (Math.abs(this.mCurrentState.mVelocity) < this.mRestSpeedThreshold) {
                OplusSpringOverScroller.log(this + " lostVelocity");
                return true;
            }
            return false;
        }

        private void calculateOnceWithRebound(boolean simulate) {
            double position = this.mCurrentState.mPosition;
            double velocity = this.mCurrentState.mVelocity;
            double tempPosition = this.mTempState.mPosition;
            double d = this.mTempState.mVelocity;
            adjustFrictionByStartVelocity();
            double aAcceleration = this.mConfig.mTension * (this.mFinal - tempPosition);
            float f = this.mRefreshTime;
            double tempPosition2 = f;
            double tempPosition3 = ((tempPosition2 * velocity) / MIN_FLING_FRICTION_REDUCE) + position;
            double tempVelocity = ((f * aAcceleration) / MIN_FLING_FRICTION_REDUCE) + velocity;
            double aAcceleration2 = this.mConfig.mTension;
            double aVelocity = this.mFinal;
            double bAcceleration = (aAcceleration2 * (aVelocity - tempPosition3)) - (this.mConfig.mFriction * tempVelocity);
            float f2 = this.mRefreshTime;
            double tempPosition4 = f2;
            double tempPosition5 = ((tempPosition4 * tempVelocity) / MIN_FLING_FRICTION_REDUCE) + position;
            double tempVelocity2 = ((f2 * bAcceleration) / MIN_FLING_FRICTION_REDUCE) + velocity;
            double d2 = this.mConfig.mTension;
            double bVelocity = this.mFinal;
            double cAcceleration = (d2 * (bVelocity - tempPosition5)) - (this.mConfig.mFriction * tempVelocity2);
            float f3 = this.mRefreshTime;
            double tempPosition6 = f3;
            double tempPosition7 = (tempPosition6 * tempVelocity2) + position;
            double tempVelocity3 = (f3 * cAcceleration) + velocity;
            double dAcceleration = (this.mConfig.mTension * (this.mFinal - tempPosition7)) - (this.mConfig.mFriction * tempVelocity3);
            double dxdt = (((tempVelocity + tempVelocity2) * MIN_FLING_FRICTION_REDUCE) + velocity + tempVelocity3) * 0.16699999570846558d;
            double dxdt2 = bAcceleration + cAcceleration;
            double dvdt = (aAcceleration + (dxdt2 * MIN_FLING_FRICTION_REDUCE) + dAcceleration) * 0.16699999570846558d;
            float f4 = this.mRefreshTime;
            this.mTempState.mVelocity = tempVelocity3;
            this.mTempState.mPosition = tempPosition7;
            this.mCurrentState.mVelocity = velocity + (f4 * dvdt);
            this.mCurrentState.mPosition = position + (f4 * dxdt);
            this.mOplusCount++;
        }

        private void adjustFrictionByStartVelocity() {
            if (this.mIsSpringBack || this.mOplusCount != 1) {
                return;
            }
            if (Math.abs(this.mCurrentState.mVelocity) > MID_VELOCITY_ADJUST_FRICTION && Math.abs(this.mCurrentState.mVelocity) < MAX_VELOCITY_ADJUST_FRICTION) {
                this.mConfig.mFriction = MID_FLING_BASE_FRICTION;
            } else if (Math.abs(this.mCurrentState.mVelocity) <= MID_VELOCITY_ADJUST_FRICTION) {
                this.mConfig.mFriction = SLOW_FLING_BASE_FRICTION;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLowVelocityThreshold(int lowVelocityThreshold) {
            MIN_VELOCITY_ADJUST_FRICTION = lowVelocityThreshold;
            OplusSpringOverScroller.log("set MIN_VELOCITY_ADJUST_FRICTION " + MIN_VELOCITY_ADJUST_FRICTION);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setMidVelocityThreshold(int midVelocityThreshold) {
            MID_VELOCITY_ADJUST_FRICTION = midVelocityThreshold;
            OplusSpringOverScroller.log("set MID_VELOCITY_ADJUST_FRICTION " + MID_VELOCITY_ADJUST_FRICTION);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setHighVelocityThreshold(int highVelocityThreshold) {
            MAX_VELOCITY_ADJUST_FRICTION = highVelocityThreshold;
            OplusSpringOverScroller.log("set MAX_VELOCITY_ADJUST_FRICTION " + MAX_VELOCITY_ADJUST_FRICTION);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLowVelocityFriction(float lowVelocityFriction) {
            SLOW_FLING_BASE_FRICTION = lowVelocityFriction;
            OplusSpringOverScroller.log("set SLOW_FLING_BASE_FRICTION " + SLOW_FLING_BASE_FRICTION);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setMidVelocityFriction(float midVelocityFriction) {
            MID_FLING_BASE_FRICTION = midVelocityFriction;
            OplusSpringOverScroller.log("set MID_FLING_BASE_FRICTION " + MID_FLING_BASE_FRICTION);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class ReboundConfig {
            double mFriction;
            double mTension;

            private ReboundConfig(double friction, double tension) {
                this.mFriction = frictionFromOrigamiValue((float) friction);
                this.mTension = tensionFromOrigamiValue((float) tension);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setFriction(double friction) {
                this.mFriction = frictionFromOrigamiValue((float) friction);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setTension(double tension) {
                this.mTension = tensionFromOrigamiValue((float) tension);
            }

            private float frictionFromOrigamiValue(float value) {
                if (value == 0.0f) {
                    return 0.0f;
                }
                return ((value - 8.0f) * 3.0f) + 25.0f;
            }

            private double tensionFromOrigamiValue(float oValue) {
                if (oValue == 0.0f) {
                    return 0.0d;
                }
                return ((oValue - 30.0f) * 3.62f) + 194.0f;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class PhysicsState {
            double mPosition;
            double mVelocity;

            private PhysicsState() {
            }
        }

        public String toString() {
            return this.mScrollerName;
        }

        public void onFlingStart(int duration, int velocity, int position) {
            DynamicFrameRateController.getInstance().getAnimationSpeedAware().onFlingStart(duration, velocity, position);
            ScrOptController.getInstance().getSceneManager().onFlingStart();
        }

        public void onFlingFinish() {
            DynamicFrameRateController.getInstance().getAnimationSpeedAware().onFlingFinish();
            ScrOptController.getInstance().getSceneManager().onFlingFinish();
        }

        public void onFlingPositionUpdate(int velocity, int position) {
            DynamicFrameRateController.getInstance().getAnimationSpeedAware().onFlingPositionUpdate(velocity, position);
            ScrOptController.getInstance().getSceneManager().onFlingPositionUpdate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String content) {
        Log.d(TAG, content);
    }
}
