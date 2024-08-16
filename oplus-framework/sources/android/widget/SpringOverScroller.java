package android.widget;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.oplus.util.OplusContextUtil;

/* loaded from: classes.dex */
public class SpringOverScroller extends OverScroller {
    private static final int FLING_MODE = 1;
    public static final float OPLUS_FLING_FRICTION_FAST = 0.76f;
    public static final float OPLUS_FLING_FRICTION_LIST_OPTIMIZE = 0.008f;
    public static final float OPLUS_FLING_FRICTION_NORMAL = 1.06f;
    public static final int OPLUS_FLING_MODE_FAST = 0;
    public static final int OPLUS_FLING_MODE_NORMAL = 1;
    private static final int REST_MODE = 2;
    private static final int SCROLL_DEFAULT_DURATION = 250;
    private static final int SCROLL_MODE = 0;
    private static final float SOLVER_TIMESTEP_SEC = 0.016f;
    private Interpolator mInterpolator;
    private int mMode;
    private ReboundOverScroller mScrollerX;
    private ReboundOverScroller mScrollerY;

    public static OverScroller newInstance(Context context) {
        if (OplusContextUtil.isOplusOSStyle(context)) {
            OverScroller scroller = new SpringOverScroller(context);
            return scroller;
        }
        OverScroller scroller2 = new OverScroller(context);
        return scroller2;
    }

    public static OverScroller newInstance(Context context, boolean isScrollView) {
        if (OplusContextUtil.isOplusOSStyle(context)) {
            OverScroller scroller = new SpringOverScroller(context);
            SpringOverScroller s = (SpringOverScroller) scroller;
            s.mScrollerX.mIsScrollView = isScrollView;
            s.mScrollerY.mIsScrollView = isScrollView;
            return scroller;
        }
        return new OverScroller(context);
    }

    public SpringOverScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
        float refreshTime;
        this.mMode = 2;
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        Display display = displayManager.getDisplay(0);
        if (display == null) {
            refreshTime = SOLVER_TIMESTEP_SEC;
        } else {
            float refreshTime2 = display.getRefreshRate();
            refreshTime = Math.round(10000.0f / refreshTime2) / 10000.0f;
        }
        this.mScrollerX = new ReboundOverScroller(refreshTime);
        this.mScrollerY = new ReboundOverScroller(refreshTime);
        if (interpolator == null) {
            this.mInterpolator = new Scroller.ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
    }

    public SpringOverScroller(Context context) {
        this(context, null);
    }

    public void setFlingFriction(float friction) {
        this.mScrollerX.mFlingFriction = friction;
        this.mScrollerY.mFlingFriction = friction;
    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null) {
            this.mInterpolator = new Scroller.ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
    }

    @Override // android.widget.OverScroller
    public boolean computeScrollOffset() {
        if (isOplusFinished()) {
            return false;
        }
        switch (this.mMode) {
            case 0:
                long time = AnimationUtils.currentAnimationTimeMillis();
                long elapsedTime = time - this.mScrollerX.mScrollStartTime;
                int duration = this.mScrollerX.mScrollDuration;
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
                if (!this.mScrollerX.update() && !this.mScrollerY.update()) {
                    abortAnimation();
                    return true;
                }
                return true;
            default:
                return true;
        }
    }

    public final boolean isOplusFinished() {
        return this.mScrollerX.isAtRest() && this.mScrollerY.isAtRest() && this.mMode != 0;
    }

    public final int getOplusCurrX() {
        return (int) Math.round(this.mScrollerX.getCurrentValue());
    }

    public final int getOplusCurrY() {
        return (int) Math.round(this.mScrollerY.getCurrentValue());
    }

    public final int getOplusFinalX() {
        return (int) this.mScrollerX.getEndValue();
    }

    public final int getOplusFinalY() {
        return (int) this.mScrollerY.getEndValue();
    }

    @Override // android.widget.OverScroller
    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        if (startY <= maxY && startY >= minY) {
            fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            return;
        }
        springBack(startX, startY, minX, maxX, minY, maxY);
    }

    @Override // android.widget.OverScroller
    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        fling(startX, startY, velocityX, velocityY);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY) {
        this.mMode = 1;
        this.mScrollerX.fling(startX, velocityX);
        this.mScrollerY.fling(startY, velocityY);
    }

    @Override // android.widget.OverScroller
    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        boolean springBackX = this.mScrollerX.springBack(startX, minX, maxX);
        boolean springBackY = this.mScrollerY.springBack(startY, minY, maxY);
        if (springBackX || springBackY) {
            this.mMode = 1;
        }
        return springBackX || springBackY;
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
    public void abortAnimation() {
        this.mMode = 2;
        this.mScrollerX.setAtRest();
        this.mScrollerY.setAtRest();
    }

    public boolean isScrollingInDirection(float xvel, float yvel) {
        int dx = (int) (this.mScrollerX.mEndValue - this.mScrollerX.mStartValue);
        int dy = (int) (this.mScrollerY.mEndValue - this.mScrollerY.mStartValue);
        return !isFinished() && Math.signum(xvel) == Math.signum((float) dx) && Math.signum(yvel) == Math.signum((float) dy);
    }

    @Override // android.widget.OverScroller
    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        this.mScrollerY.notifyEdgeReached(startY, finalY, overY);
        springBack(0, startY, 0, 0, 0, 0);
    }

    @Override // android.widget.OverScroller
    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        this.mScrollerX.notifyEdgeReached(startX, finalX, overX);
        springBack(startX, 0, 0, 0, 0, 0);
    }

    @Override // android.widget.OverScroller
    public float getCurrVelocity() {
        double velX = this.mScrollerX.getVelocity();
        double velY = this.mScrollerY.getVelocity();
        return (int) Math.sqrt((velX * velX) + (velY * velY));
    }

    public void setOplusFriction(float friction) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ReboundOverScroller {
        private static final float FLING_CHANGE_INCREASE_STEP = 1.2f;
        private static final float FLING_CHANGE_REDUCE_STEP = 0.6f;
        private static final float FLING_DXDT_RATIO = 0.167f;
        private static final float FLOAT_1 = 1.0f;
        private static final float FLOAT_2 = 2.0f;
        private static final int NUM_60 = 60;
        private static final int SPRING_BACK_ADJUST_TENSION_VALUE = 100;
        private static final int SPRING_BACK_ADJUST_THRESHOLD = 180;
        private static final float SPRING_BACK_FRICTION = 12.19f;
        private static final int SPRING_BACK_STOP_THRESHOLD = 2;
        private static final float SPRING_BACK_TENSION = 16.0f;
        private static float sTimeIncrease = 1.0f;
        private ReboundConfig mConfig;
        private double mEndValue;
        private boolean mIsSpringBack;
        private float mRefreshTime;
        private int mScrollDuration;
        private int mScrollFinal;
        private int mScrollStart;
        private long mScrollStartTime;
        private double mStartValue;
        private boolean mTensionAdjusted;
        private PhysicsState mCurrentState = new PhysicsState();
        private PhysicsState mPreviousState = new PhysicsState();
        private PhysicsState mTempState = new PhysicsState();
        private float mFlingFriction = 1.06f;
        private double mRestSpeedThreshold = 100.0d;
        private double mDisplacementFromRestThreshold = 0.05d;
        private int mOppoCount = 1;
        private boolean mIsScrollView = false;
        private ReboundConfig mFlingConfig = new ReboundConfig(1.06f, 0.0d);
        private ReboundConfig mSpringBackConfig = new ReboundConfig(12.1899995803833d, 16.0d);

        ReboundOverScroller(float refreshTime) {
            this.mRefreshTime = refreshTime;
            setConfig(this.mFlingConfig);
        }

        void fling(int start, int velocity) {
            this.mOppoCount = 1;
            sTimeIncrease = 1.0f;
            this.mFlingConfig.setFriction(this.mFlingFriction);
            this.mFlingConfig.setTension(0.0d);
            setConfig(this.mFlingConfig);
            setCurrentValue(start, true);
            setVelocity(velocity);
        }

        boolean springBack(int start, int min, int max) {
            setCurrentValue(start, false);
            if (start > max || start < min) {
                if (start > max) {
                    setEndValue(max);
                } else if (start < min) {
                    setEndValue(min);
                }
                this.mIsSpringBack = true;
                this.mSpringBackConfig.setFriction(12.1899995803833d);
                this.mSpringBackConfig.setTension(16.0d);
                setConfig(this.mSpringBackConfig);
                return true;
            }
            setConfig(new ReboundConfig(this.mFlingFriction, 0.0d));
            return false;
        }

        void startScroll(int start, int distance, int duration) {
            this.mScrollStart = start;
            this.mScrollFinal = start + distance;
            this.mScrollDuration = duration;
            this.mScrollStartTime = AnimationUtils.currentAnimationTimeMillis();
            setConfig(this.mFlingConfig);
        }

        void setConfig(ReboundConfig config) {
            if (config == null) {
                throw new IllegalArgumentException("springConfig is required");
            }
            this.mConfig = config;
        }

        void setCurrentValue(double currentValue, boolean setAtRest) {
            this.mStartValue = currentValue;
            if (!this.mIsScrollView) {
                this.mPreviousState.mPosition = 0.0d;
                this.mTempState.mPosition = 0.0d;
            }
            this.mCurrentState.mPosition = currentValue;
            if (setAtRest) {
                setAtRest();
            }
        }

        double getCurrentValue() {
            return this.mCurrentState.mPosition;
        }

        double getVelocity() {
            return this.mCurrentState.mVelocity;
        }

        void setVelocity(double velocity) {
            if (velocity == this.mCurrentState.mVelocity) {
                return;
            }
            this.mCurrentState.mVelocity = velocity;
        }

        double getEndValue() {
            return this.mEndValue;
        }

        void setEndValue(double endValue) {
            if (this.mEndValue == endValue) {
                return;
            }
            this.mStartValue = getCurrentValue();
            this.mEndValue = endValue;
        }

        void setAtRest() {
            this.mEndValue = this.mCurrentState.mPosition;
            this.mTempState.mPosition = this.mCurrentState.mPosition;
            this.mCurrentState.mVelocity = 0.0d;
            this.mIsSpringBack = false;
        }

        boolean isAtRest() {
            return Math.abs(this.mCurrentState.mVelocity) <= this.mRestSpeedThreshold && (getDisplacementDistanceForState(this.mCurrentState) <= this.mDisplacementFromRestThreshold || this.mConfig.mTension == 0.0d);
        }

        void updateScroll(float q) {
            PhysicsState physicsState = this.mCurrentState;
            int i = this.mScrollStart;
            physicsState.mPosition = i + Math.round((this.mScrollFinal - i) * q);
        }

        void notifyEdgeReached(int start, int end, int over) {
            this.mCurrentState.mPosition = start;
            this.mPreviousState.mPosition = 0.0d;
            this.mPreviousState.mVelocity = 0.0d;
            this.mTempState.mPosition = 0.0d;
            this.mTempState.mVelocity = 0.0d;
        }

        double getDisplacementDistanceForState(PhysicsState state) {
            return Math.abs(this.mEndValue - state.mPosition);
        }

        boolean update() {
            if (isAtRest()) {
                return false;
            }
            double position = this.mCurrentState.mPosition;
            double velocity = this.mCurrentState.mVelocity;
            double tempPosition = this.mTempState.mPosition;
            double d = this.mTempState.mVelocity;
            if (this.mIsSpringBack) {
                double displacement = getDisplacementDistanceForState(this.mCurrentState);
                if (!this.mTensionAdjusted && displacement < 180.0d) {
                    this.mConfig.mTension += 100.0d;
                    this.mTensionAdjusted = true;
                } else if (displacement < 2.0d) {
                    this.mCurrentState.mPosition = this.mEndValue;
                    this.mTensionAdjusted = false;
                    this.mIsSpringBack = false;
                    return false;
                }
            } else if (this.mOppoCount < 60) {
                sTimeIncrease += 0.020000001f;
                this.mConfig.mFriction += 0.020000001415610313d;
            } else {
                float f = sTimeIncrease;
                sTimeIncrease = f - ((f - 0.6f) / 60.0f);
                this.mConfig.mFriction -= (sTimeIncrease - 0.6f) / 60.0f;
            }
            double aAcceleration = (this.mConfig.mTension * (this.mEndValue - tempPosition)) - (this.mConfig.mFriction * this.mPreviousState.mVelocity);
            float f2 = this.mRefreshTime;
            double tempPosition2 = ((f2 * velocity) / 2.0d) + position;
            double tempPosition3 = f2;
            double tempVelocity = ((tempPosition3 * aAcceleration) / 2.0d) + velocity;
            double aAcceleration2 = this.mConfig.mTension;
            double aVelocity = this.mEndValue;
            double bAcceleration = (aAcceleration2 * (aVelocity - tempPosition2)) - (this.mConfig.mFriction * tempVelocity);
            float f3 = this.mRefreshTime;
            double tempPosition4 = ((f3 * tempVelocity) / 2.0d) + position;
            double tempVelocity2 = ((f3 * bAcceleration) / 2.0d) + velocity;
            double cAcceleration = (this.mConfig.mTension * (this.mEndValue - tempPosition4)) - (this.mConfig.mFriction * tempVelocity2);
            float f4 = this.mRefreshTime;
            double tempPosition5 = (f4 * tempVelocity2) + position;
            double tempPosition6 = f4;
            double tempVelocity3 = (tempPosition6 * cAcceleration) + velocity;
            double dAcceleration = (this.mConfig.mTension * (this.mEndValue - tempPosition5)) - (this.mConfig.mFriction * tempVelocity3);
            double dxdt = (velocity + ((tempVelocity + tempVelocity2) * 2.0d) + tempVelocity3) * 0.16699999570846558d;
            double dvdt = (aAcceleration + ((bAcceleration + cAcceleration) * 2.0d) + dAcceleration) * 0.16699999570846558d;
            float f5 = this.mRefreshTime;
            double dVelocity = f5;
            double position2 = position + (dVelocity * dxdt);
            double dxdt2 = f5;
            this.mTempState.mVelocity = tempVelocity3;
            this.mTempState.mPosition = tempPosition5;
            this.mCurrentState.mVelocity = velocity + (dxdt2 * dvdt);
            this.mCurrentState.mPosition = position2;
            this.mOppoCount++;
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public static class ReboundConfig {
            double mFriction;
            double mTension;

            ReboundConfig(double friction, double tension) {
                this.mFriction = frictionFromOrigamiValue((float) friction);
                this.mTension = tensionFromOrigamiValue((float) tension);
            }

            void setFriction(double friction) {
                this.mFriction = frictionFromOrigamiValue((float) friction);
            }

            void setTension(double tension) {
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

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public static class PhysicsState {
            double mPosition;
            double mVelocity;

            PhysicsState() {
            }
        }
    }
}
