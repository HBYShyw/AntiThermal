package com.oplus.dynamicframerate;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Choreographer;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.VelocityTracker;
import android.view.animation.AnimationUtils;
import com.oplus.dynamicframerate.util.FramerateUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class AnimationSpeedAware implements IAnimationSpeedAware {
    private static final int ANIMATION_SPEED_RATE_DENOMINATOR = 10;
    private static final float INFINITY_VELOCITY = 10000.0f;
    private static final int INPUT_IDLE_TIMEOUT = 300;
    private static final int KEY_REPEAT_FIRST_DELAY = 750;
    private static final int SECOND_OF_MS = 1000;
    private static final String STRING_STATE_FLING_ANIMATION = "STATE_FLING_ANIMATION";
    private static final String STRING_STATE_IDLE = "STATE_IDLE";
    private static final String STRING_STATE_INPUT_IDLE = "STATE_INPUT_IDLE";
    private static final String STRING_STATE_SCROLL_ANIMATION = "STATE_SCROLL_ANIMATION";
    private static final String STRING_STATE_SCROLL_BAR_FADE_ANIMATION = "STATE_SCROLL_BAR_FADE_ANIMATION";
    private static final String STRING_STATE_WINDOW_ANIMATION = "STATE_WINDOW_ANIMATION";
    private static final String TAG = AnimationSpeedAware.class.getSimpleName();
    private static final int WINDOW_ANIMATION_INVALID_MILLIS = 100;
    private Handler mHandler;
    private int mMaxHeight;
    private int mMaxWidth;
    private ISceneManager mSceneManager;
    private int mState;
    private UserActionStats mUserActionStats;
    private boolean mWindowAnimaitonEnable;
    private InputEventInfo mInputEventInfo = new InputEventInfo();
    private FlingAnimationInfo mFlingAnimationInfo = new FlingAnimationInfo();
    private ScrollAnimationInfo mScrollAnimationInfo = new ScrollAnimationInfo();
    private ScrollBarAnimationInfo mScrollBarAnimationInfo = new ScrollBarAnimationInfo();
    private WindowAnimationInfo mWindowAnimationInfo = new WindowAnimationInfo();
    private AnimationInfo mAnimationInfo = new AnimationInfo();
    private int mWindowAnimationSpeedRate = FRTCConfigManager.WINDOW_ANIMATION_SPEED_RATE;

    public AnimationSpeedAware(ISceneManager sceneManager) {
        this.mSceneManager = sceneManager;
        Context context = ActivityThread.currentApplication().getApplicationContext();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        this.mMaxHeight = dm.heightPixels;
        this.mMaxWidth = dm.widthPixels;
        OplusDebugUtil.i(TAG, "AnimationSpeedAware init: mMaxHeight = " + this.mMaxHeight + ", mMaxWidth = " + this.mMaxWidth);
        if (OplusDebugUtil.DEBUG_USERACTIONSTATS) {
            this.mUserActionStats = new UserActionStats();
        }
        this.mWindowAnimaitonEnable = false;
    }

    public static String getStateString(int state) {
        switch (state) {
            case 0:
                return STRING_STATE_IDLE;
            case 1:
                return STRING_STATE_INPUT_IDLE;
            case 2:
                return STRING_STATE_SCROLL_ANIMATION;
            case 3:
                return STRING_STATE_FLING_ANIMATION;
            case 4:
                return STRING_STATE_SCROLL_BAR_FADE_ANIMATION;
            case 5:
                return STRING_STATE_WINDOW_ANIMATION;
            default:
                return "";
        }
    }

    public static boolean isDynamicFramerateState(int state) {
        return state != 0;
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onEventHandled(Object vri, MotionEvent ev) {
        if (!isInMainThread()) {
            OplusDebugUtil.w(TAG, "onEventHandled: do not accept input event in main thread");
            return;
        }
        InputEventInfo info = this.mInputEventInfo;
        info.mAction = ev.getActionMasked();
        boolean isLightDebugInfoOutput = false;
        boolean isDown = false;
        switch (info.mAction) {
            case 0:
                info.mActivePointerId = ev.getPointerId(ev.getActionIndex());
                info.mIsUnknowPointer = false;
                isLightDebugInfoOutput = true;
                this.mSceneManager.onVriUpdate(vri);
                info.mIsMoving = true;
                isDown = true;
                break;
            case 1:
                isLightDebugInfoOutput = true;
                info.mIsMoving = false;
                break;
            case 3:
                isLightDebugInfoOutput = true;
                info.mIsMoving = false;
                break;
            case 5:
                info.mActivePointerId = ev.getPointerId(ev.getActionIndex());
                info.mIsUnknowPointer = false;
                break;
            case 6:
                int index = ev.getActionIndex();
                int pointerId = ev.getPointerId(index);
                if (info.mIsUnknowPointer) {
                    if (ev.getPointerCount() <= 2) {
                        info.mActivePointerId = ev.getPointerId(index == 0 ? 1 : 0);
                        info.mIsUnknowPointer = false;
                        break;
                    }
                } else if (info.mActivePointerId == pointerId) {
                    if (ev.getPointerCount() > 2) {
                        info.mIsUnknowPointer = true;
                        break;
                    } else {
                        info.mActivePointerId = ev.getPointerId(index == 0 ? 1 : 0);
                        info.mIsUnknowPointer = false;
                        break;
                    }
                }
                break;
        }
        if (info.mActivePointerId == -1) {
            OplusDebugUtil.e(TAG, "onEventHandled: INVALID_POINTER_ID, info: " + info);
            return;
        }
        if (!info.mIsUnknowPointer) {
            int index2 = ev.findPointerIndex(info.mActivePointerId);
            if (index2 == -1) {
                index2 = 0;
            }
            info.mX = (int) ev.getX(index2);
            info.mY = (int) ev.getY(index2);
        }
        info.mVelocityTracker.addMovement(ev);
        info.mVsyncTime = AnimationUtils.currentAnimationTimeMillis();
        if (isDown) {
            info.mLastDownTime = info.mVsyncTime;
        }
        if (info.mUpdated) {
            isLightDebugInfoOutput = false;
        }
        info.mUpdated = true;
        if (OplusDebugUtil.DEBUG || isLightDebugInfoOutput) {
            OplusDebugUtil.i(TAG, "onEventHandled: info: " + info);
        }
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.trace("onEventHandled: info: " + info);
        }
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onFlingStart(int duration, int velocity, int position) {
        long newVsyncTime = AnimationUtils.currentAnimationTimeMillis();
        int newVelocity = Math.abs(velocity);
        FlingAnimationInfo info = this.mFlingAnimationInfo;
        if (newVsyncTime == info.mVsyncTime || info.mUpdated) {
            info.mVsyncTime = newVsyncTime;
            info.mInitVelocity = Math.max(newVelocity, info.mInitVelocity);
            info.mPosition = position;
        } else {
            info.mDuration = duration;
            info.mInitVelocity = newVelocity;
            info.mUpdated = true;
            info.mVsyncTime = newVsyncTime;
            info.mPosition = position;
        }
        onFlingStateUpdate(0);
        info.mStarted = true;
        info.mFinished = false;
        info.mStartCount++;
        OplusDebugUtil.i(TAG, "onFlingStart: animation info: " + info);
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.trace("onFlingStart: animation info: " + info);
        }
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onFlingPositionUpdate(int velocity, int position) {
        FlingAnimationInfo info = this.mFlingAnimationInfo;
        int newVel = Math.abs(velocity);
        if (info.mUpdated) {
            newVel = Math.max(newVel, info.mVelocity);
        }
        info.mVelocity = newVel;
        info.mPosition = position;
        info.mUpdated = true;
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.trace("onFlingPositionUpdate: animation info: " + info);
            OplusDebugUtil.d(TAG, "onFlingPositionUpdate: animation info: " + info);
        }
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onFlingStateUpdate(int state) {
        FlingAnimationInfo info = this.mFlingAnimationInfo;
        info.mState = state;
        info.mUpdated = true;
        info.mIsSplineState = state == 0;
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.trace("onFlingStateUpdate: animation info: " + info);
            OplusDebugUtil.d(TAG, "onFlingStateUpdate: animation info: " + info);
        }
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onFlingFinish() {
        FlingAnimationInfo info = this.mFlingAnimationInfo;
        info.mFinished = true;
        info.mUpdated = true;
        info.mStartCount--;
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.trace("onFlingFinish: animation info: " + info);
            OplusDebugUtil.d(TAG, "onFlingFinish: animation info: " + info);
        }
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onScrollBarFadeStart(int duration) {
        ScrollBarAnimationInfo info = this.mScrollBarAnimationInfo;
        info.mUpdated = true;
        info.mDuration = duration;
        info.mStarted = true;
        info.mVsyncTime = AnimationUtils.currentAnimationTimeMillis();
        OplusDebugUtil.i(TAG, "onScrollBarFadeStart: animation info: " + info);
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.trace("onScrollBarFadeStart: animation info: " + info);
        }
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onScrollBarFadeEnd() {
        ScrollBarAnimationInfo info = this.mScrollBarAnimationInfo;
        info.mUpdated = true;
        info.mVsyncTime = AnimationUtils.currentAnimationTimeMillis();
        info.mEnded = true;
        OplusDebugUtil.i(TAG, "onScrollBarFadeEnd: animation info: " + info);
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.trace("onScrollBarFadeEnd: animation info: " + info);
        }
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.mScrollAnimationInfo.mUpdated = true;
        this.mScrollAnimationInfo.mXOffset = l - oldl;
        this.mScrollAnimationInfo.mYOffset = t - oldt;
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.trace("onScrollChanged: left from " + oldl + " to " + l + ", offset = " + (l - oldl) + ", top from " + oldt + " to " + t + ", offset = " + (t - oldt));
            OplusDebugUtil.d(TAG, "onScrollChanged: left from " + oldl + " to " + l + ", offset = " + (l - oldl) + ", top from " + oldt + " to " + t + ", offset = " + (t - oldt));
        }
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onSetMatrix(SurfaceControl sc, float[] float9) {
        if (!this.mWindowAnimaitonEnable) {
            return;
        }
        int type = sc.mSurfaceControlExt.getAnimationType(sc);
        if (type <= 0) {
            OplusDebugUtil.i(TAG, "onSetMatrix: type = " + type + ", don't satisfy dynamic framerate");
            return;
        }
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.d(TAG, "onSetMatrix: type = " + type + ", float9 = " + Arrays.toString(float9));
        }
        this.mWindowAnimationInfo.mUpdated = true;
        this.mWindowAnimationInfo.mVsyncTime = AnimationUtils.currentAnimationTimeMillis();
        int w = this.mMaxHeight;
        int h = this.mMaxWidth;
        this.mWindowAnimationInfo.updateWindowInfo(sc, w, h, float9);
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void onDeliverInputEvent(InputEvent event) {
        if (!isInMainThread()) {
            OplusDebugUtil.w(TAG, "onDeliverInputEvent: do not accept input event in main thread");
        } else if (event instanceof KeyEvent) {
            OplusDebugUtil.i(TAG, "onDeliverInputEvent: key event");
            this.mInputEventInfo.mUpdated = true;
        }
    }

    public String toString() {
        return "AnimationSpeedAware{" + Integer.toHexString(System.identityHashCode(this)) + " , mState: " + this.mState + "}";
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void syncInfoTogether() {
        int previousState = this.mState;
        switch (this.mState) {
            case 0:
                handleIdleState();
                break;
            case 1:
                handleInputIdleState();
                break;
            case 2:
                handleScrollAnimationState();
                break;
            case 3:
                handleFlingAnimationState();
                break;
            case 4:
                handleScrollBarFadeState();
                break;
            case 5:
                handleWindowAnimationState();
                break;
        }
        this.mAnimationInfo.state = this.mState;
        this.mSceneManager.onDoFrameFinished();
        if (OplusDebugUtil.DEBUG_USERACTIONSTATS) {
            this.mUserActionStats.mFramesCount++;
            boolean isNeedUpdate = this.mUserActionStats.mFramesCount % UserActionStats.PRINT_FRAMES_INTERVAL == 0;
            if (isNeedUpdate || previousState != this.mState) {
                this.mUserActionStats.mCurFrameTimeMs = AnimationUtils.currentAnimationTimeMillis();
                long durFrametime = this.mUserActionStats.mCurFrameTimeMs - this.mUserActionStats.mPrevFrameTimeMs;
                long[] jArr = this.mUserActionStats.mAllStateDurTimeStatsMs;
                jArr[previousState] = jArr[previousState] + durFrametime;
                UserActionStats userActionStats = this.mUserActionStats;
                userActionStats.mPrevFrameTimeMs = userActionStats.mCurFrameTimeMs;
            }
            int velocityStats = this.mAnimationInfo.mVelocity;
            int pkgIndex = this.mUserActionStats.mPkgIndex;
            if (pkgIndex >= 0) {
                this.mUserActionStats.changeVecStats(velocityStats, this.mState, pkgIndex);
            }
        }
        boolean isNeedUpdate2 = OplusDebugUtil.DEBUG;
        if (!isNeedUpdate2 && previousState != this.mState) {
            OplusDebugUtil.i(TAG, "syncInfoTogether: from " + getStateString(previousState) + " to " + getStateString(this.mState));
        }
        if (OplusDebugUtil.DEBUG && (this.mFlingAnimationInfo.mUpdated || this.mInputEventInfo.mUpdated || this.mScrollAnimationInfo.mUpdated || this.mScrollBarAnimationInfo.mUpdated || this.mWindowAnimationInfo.mUpdated)) {
            OplusDebugUtil.trace("syncInfoTogether: from " + getStateString(previousState) + " to " + getStateString(this.mState) + "\n\t" + this.mFlingAnimationInfo + "\n\t" + this.mInputEventInfo + "\n\t" + this.mScrollAnimationInfo + "\n\t" + this.mScrollBarAnimationInfo + "\n\t" + this.mWindowAnimationInfo + "\n\t" + this.mAnimationInfo);
            OplusDebugUtil.d(TAG, "syncInfoTogether: from " + getStateString(previousState) + " to " + getStateString(this.mState) + "\n\t" + this.mFlingAnimationInfo + "\n\t" + this.mInputEventInfo + "\n\t" + this.mScrollAnimationInfo + "\n\t" + this.mScrollBarAnimationInfo + "\n\t" + this.mWindowAnimationInfo + "\n\t" + this.mAnimationInfo);
        }
        resetAllInfo();
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void handleCancelState(int state) {
        if (this.mAnimationInfo.state == state) {
            this.mAnimationInfo.state = 0;
            int i = this.mAnimationInfo.state;
            this.mState = i;
            this.mSceneManager.onStateSlientUpdate(i);
        }
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.i(TAG, "handleCancelState: cancel state: " + getStateString(state) + " now state: " + getStateString(this.mState));
        }
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void updateScreenSize(int width, int height) {
        OplusDebugUtil.i(TAG, "updateScreenSize: width: " + width + ", height: " + height);
        this.mMaxWidth = width;
        this.mMaxHeight = height;
    }

    @Override // com.oplus.dynamicframerate.IAnimationSpeedAware
    public void resetAnimationInfo() {
        if (this.mInputEventInfo.mIsMoving) {
            this.mInputEventInfo.mIsMoving = false;
            OplusDebugUtil.w(TAG, "resetAnimationInfo: hit case for resetting input-idle state");
        }
    }

    public AnimationInfo getAnimationInfo() {
        return this.mAnimationInfo;
    }

    private void handleIdleState() {
        if (this.mWindowAnimationInfo.mUpdated) {
            commonWindowAnimationInfoHandle();
            return;
        }
        if (this.mScrollAnimationInfo.mUpdated && this.mInputEventInfo.mUpdated && this.mInputEventInfo.mAction == 2) {
            commonScrollAnimationInfoHandle();
            return;
        }
        if (this.mScrollBarAnimationInfo.mUpdated && !this.mScrollAnimationInfo.mUpdated && this.mScrollBarAnimationInfo.mStarted && this.mInputEventInfo.mVsyncTime + 750 <= this.mScrollBarAnimationInfo.mVsyncTime) {
            this.mState = 4;
            scheduleCancelState(4, this.mScrollBarAnimationInfo.mDuration);
        }
        boolean toFling = false;
        if (this.mFlingAnimationInfo.mUpdated) {
            if (this.mFlingAnimationInfo.mStarted) {
                this.mState = 3;
                toFling = true;
                this.mAnimationInfo.mVelocity = this.mFlingAnimationInfo.mInitVelocity;
            }
            commonFlingAnimationInfoHandle();
        }
        if ((this.mInputEventInfo.mUpdated || this.mInputEventInfo.mIsMoving) && !toFling) {
            commonOtherInputEventHandle();
        }
    }

    private void handleInputIdleState() {
        handleIdleState();
    }

    private void handleScrollAnimationState() {
        if (this.mScrollAnimationInfo.mUpdated && this.mInputEventInfo.mUpdated && this.mInputEventInfo.mAction == 2) {
            commonScrollAnimationInfoHandle();
            return;
        }
        boolean isFlingStart = false;
        if (this.mFlingAnimationInfo.mUpdated) {
            if (this.mFlingAnimationInfo.mStarted) {
                this.mState = 3;
                this.mAnimationInfo.mVelocity = this.mFlingAnimationInfo.mInitVelocity;
                isFlingStart = true;
            }
            commonFlingAnimationInfoHandle();
        }
        if (this.mInputEventInfo.mUpdated || this.mInputEventInfo.mIsMoving) {
            if (!isFlingStart || this.mInputEventInfo.mAction != 1) {
                commonOtherInputEventHandle();
            }
        }
    }

    private void handleFlingAnimationState() {
        if (this.mScrollAnimationInfo.mUpdated && this.mInputEventInfo.mUpdated && this.mInputEventInfo.mAction == 2) {
            commonScrollAnimationInfoHandle();
            return;
        }
        if (this.mFlingAnimationInfo.mUpdated) {
            if (this.mFlingAnimationInfo.mFinished) {
                this.mState = 0;
            } else if (this.mFlingAnimationInfo.mStarted) {
                this.mAnimationInfo.mVelocity = this.mFlingAnimationInfo.mInitVelocity;
            } else {
                this.mAnimationInfo.mVelocity = this.mFlingAnimationInfo.mVelocity;
            }
            commonFlingAnimationInfoHandle();
        }
        if (this.mInputEventInfo.mUpdated || this.mInputEventInfo.mIsMoving) {
            commonOtherInputEventHandle();
        }
    }

    private void handleScrollBarFadeState() {
        if (this.mScrollAnimationInfo.mUpdated && this.mInputEventInfo.mUpdated && this.mInputEventInfo.mAction == 2) {
            commonScrollAnimationInfoHandle();
            return;
        }
        if (this.mScrollBarAnimationInfo.mUpdated) {
            if (this.mScrollBarAnimationInfo.mStarted) {
                if (this.mInputEventInfo.mVsyncTime + 750 <= this.mScrollBarAnimationInfo.mVsyncTime) {
                    this.mState = 4;
                    scheduleCancelState(4, this.mScrollBarAnimationInfo.mDuration);
                }
            } else if (this.mScrollBarAnimationInfo.mEnded) {
                this.mState = 0;
            }
        }
        if (this.mFlingAnimationInfo.mUpdated) {
            if (this.mFlingAnimationInfo.mStarted) {
                this.mState = 3;
                this.mAnimationInfo.mVelocity = this.mFlingAnimationInfo.mInitVelocity;
            }
            commonFlingAnimationInfoHandle();
        }
        if (this.mInputEventInfo.mUpdated || this.mInputEventInfo.mIsMoving) {
            commonOtherInputEventHandle();
        }
    }

    private void handleWindowAnimationState() {
        if (!this.mWindowAnimationInfo.mUpdated) {
            this.mState = 0;
        }
        handleIdleState();
    }

    private void commonWindowAnimationInfoHandle() {
        this.mWindowAnimationInfo.updateVelocity();
        this.mState = 5;
        this.mAnimationInfo.mVelocity = this.mWindowAnimationInfo.mVelocity;
        if (this.mInputEventInfo.mUpdated && this.mInputEventInfo.mAction == 2) {
            this.mInputEventInfo.mVelocityTracker.computeCurrentVelocity(1000);
            float xVelocity = this.mInputEventInfo.getXVelocity();
            float yVelocity = this.mInputEventInfo.getYVelocity();
            this.mAnimationInfo.mVelocity = this.mScrollAnimationInfo.chooseVelocity(Math.round(yVelocity), Math.round(xVelocity));
            AnimationInfo animationInfo = this.mAnimationInfo;
            animationInfo.mVelocity = Math.max(animationInfo.mVelocity, this.mAnimationInfo.mVelocity);
        }
    }

    private void commonFlingAnimationInfoHandle() {
        if (!this.mFlingAnimationInfo.mIsSplineState) {
            this.mState = 0;
        }
        if (this.mFlingAnimationInfo.mFinished && this.mFlingAnimationInfo.mStartCount <= 0) {
            this.mState = 0;
        }
    }

    private void commonScrollAnimationInfoHandle() {
        this.mState = 2;
        this.mInputEventInfo.mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = this.mInputEventInfo.getXVelocity();
        float yVelocity = this.mInputEventInfo.getYVelocity();
        ScrollAnimationInfo scrollAnimationInfo = this.mScrollAnimationInfo;
        scrollAnimationInfo.mVelocity = scrollAnimationInfo.chooseVelocity(Math.round(yVelocity), Math.round(xVelocity));
        this.mAnimationInfo.mVelocity = (this.mScrollAnimationInfo.mVelocity * FRTCConfigManager.SCROLL_ANIMATION_SPEED_RATE) / 10;
        this.mFlingAnimationInfo.mIsSplineState = true;
    }

    private void commonOtherInputEventHandle() {
        if (isInputIdleState()) {
            this.mState = 1;
            if (this.mInputEventInfo.mUpdated) {
                this.mInputEventInfo.mVelocityTracker.computeCurrentVelocity(1000);
                int xVelocity = Math.abs(Math.round(this.mInputEventInfo.getXVelocity()));
                int yVelocity = Math.abs(Math.round(this.mInputEventInfo.getYVelocity()));
                this.mAnimationInfo.mVelocity = Math.max(yVelocity, xVelocity);
                return;
            }
            return;
        }
        this.mState = 0;
        checkInputEventInfo(this.mInputEventInfo);
    }

    private boolean isInputIdleState() {
        boolean isTimeout = this.mInputEventInfo.mVsyncTime - this.mInputEventInfo.mLastDownTime > 300;
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.d(TAG, "isInputIdleState: isTimeout = " + isTimeout);
        }
        return this.mInputEventInfo.mIsMoving && isTimeout;
    }

    private void scheduleCancelState(int state, int delay) {
        Handler handler = this.mHandler;
        if (handler != null) {
            Message msg = handler.obtainMessage(0);
            msg.arg1 = state;
            this.mHandler.sendMessageDelayed(msg, delay);
        }
    }

    private void checkInputEventInfo(InputEventInfo info) {
        switch (info.mAction) {
            case 1:
            case 3:
                info.clearVelocityTracker();
                return;
            case 2:
            default:
                return;
        }
    }

    private void resetAllInfo() {
        this.mFlingAnimationInfo.reset();
        this.mInputEventInfo.reset();
        this.mScrollAnimationInfo.reset();
        this.mScrollBarAnimationInfo.reset();
        this.mWindowAnimationInfo.reset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getFrameIntervalNanos() {
        return Choreographer.getMainThreadInstance().getFrameIntervalNanos();
    }

    private boolean isInMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /* loaded from: classes.dex */
    public abstract class BaseInfo {
        boolean mUpdated;
        long mVsyncTime;

        abstract void reset();

        public BaseInfo() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ScrollAnimationInfo extends BaseInfo {
        int mVelocity;
        int mXOffset;
        int mYOffset;

        ScrollAnimationInfo() {
            super();
        }

        @Override // com.oplus.dynamicframerate.AnimationSpeedAware.BaseInfo
        public void reset() {
            this.mUpdated = false;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(ScrollAnimationInfo.class.getSimpleName());
            sb.append(": mXOffset = ").append(this.mXOffset).append(", mYOffset = ").append(this.mYOffset).append(", mVelocity = ").append(this.mVelocity).append(", mVsyncTime = ").append(this.mVsyncTime).append(", mUpdated = ").append(this.mUpdated);
            return sb.toString();
        }

        public int chooseVelocity(int yVelocity, int xVelocity) {
            if (Math.abs(this.mXOffset) > Math.abs(this.mYOffset)) {
                return Math.abs(xVelocity);
            }
            return Math.abs(yVelocity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class InputEventInfo extends BaseInfo {
        int mAction;
        int mActivePointerId;
        boolean mIsMoving;
        boolean mIsUnknowPointer;
        long mLastDownTime;
        VelocityTracker mVelocityTracker;
        int mX;
        int mY;

        InputEventInfo() {
            super();
            this.mVelocityTracker = VelocityTracker.obtain();
        }

        @Override // com.oplus.dynamicframerate.AnimationSpeedAware.BaseInfo
        public void reset() {
            this.mX = 0;
            this.mY = 0;
            this.mAction = -1;
            this.mUpdated = false;
        }

        public void clearVelocityTracker() {
            this.mVelocityTracker.clear();
        }

        public float getXVelocity() {
            if (!this.mIsUnknowPointer) {
                return AnimationSpeedAware.this.mInputEventInfo.mVelocityTracker.getXVelocity(this.mActivePointerId);
            }
            return 10000.0f;
        }

        public float getYVelocity() {
            if (!this.mIsUnknowPointer) {
                return AnimationSpeedAware.this.mInputEventInfo.mVelocityTracker.getYVelocity(this.mActivePointerId);
            }
            return 10000.0f;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(InputEventInfo.class.getSimpleName());
            sb.append(": mX = ").append(this.mX).append(", mY = ").append(this.mY).append(", mAction = ").append(this.mAction).append(", mActivePointerId = ").append(this.mActivePointerId).append(", xVelocity = ").append(this.mVelocityTracker.getXVelocity(this.mActivePointerId)).append(", yVelocity = ").append(this.mVelocityTracker.getYVelocity(this.mActivePointerId)).append(", mLastDownTime = ").append(this.mLastDownTime).append(", mIsUnknowPointer = ").append(this.mIsUnknowPointer).append(", mVsyncTime = ").append(this.mVsyncTime).append(", mIsMoving = ").append(this.mIsMoving).append(", mUpdated = ").append(this.mUpdated);
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ScrollBarAnimationInfo extends BaseInfo {
        int mDuration;
        boolean mEnded;
        boolean mStarted;

        ScrollBarAnimationInfo() {
            super();
        }

        @Override // com.oplus.dynamicframerate.AnimationSpeedAware.BaseInfo
        public void reset() {
            this.mUpdated = false;
            this.mStarted = false;
            this.mEnded = false;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(ScrollBarAnimationInfo.class.getSimpleName());
            sb.append(": mDuration = ").append(this.mDuration).append(", mEnded = ").append(this.mEnded).append(", mStarted = ").append(this.mStarted).append(", mVsyncTime = ").append(this.mVsyncTime).append(", mUpdated = ").append(this.mUpdated);
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class FlingAnimationInfo extends BaseInfo {
        static final int BALLISTIC = 2;
        static final int CUBIC = 1;
        static final int SPLINE = 0;
        int mDuration;
        boolean mFinished;
        int mInitVelocity;
        boolean mIsSplineState;
        int mPosition;
        int mStartCount;
        boolean mStarted;
        int mState;
        int mVelocity;

        FlingAnimationInfo() {
            super();
        }

        @Override // com.oplus.dynamicframerate.AnimationSpeedAware.BaseInfo
        public void reset() {
            this.mUpdated = false;
            this.mFinished = false;
            this.mStarted = false;
            this.mStartCount = 0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(FlingAnimationInfo.class.getSimpleName());
            sb.append(": mDuration = ").append(this.mDuration).append(", mVelocity = ").append(this.mVelocity).append(", mInitVelocity = ").append(this.mInitVelocity).append(", mPosition = ").append(this.mPosition).append(", mState = ").append(this.mState).append(", mStartCount = ").append(this.mStartCount).append(", mFinished = ").append(this.mFinished).append(", mStarted = ").append(this.mStarted).append(", mIsSplineState = ").append(this.mIsSplineState).append(", mVsyncTime = ").append(this.mVsyncTime).append(", mUpdated = ").append(this.mUpdated);
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class WindowAnimationInfo extends BaseInfo {
        int mVelocity;
        Map<Object, WindowInfo> mWindowInfoMaps;

        WindowAnimationInfo() {
            super();
            this.mWindowInfoMaps = new HashMap();
        }

        void updateWindowInfo(Object window, int w, int h, float[] float9) {
            synchronized (this) {
                WindowInfo windowInfo = this.mWindowInfoMaps.getOrDefault(window, null);
                if (windowInfo == null) {
                    windowInfo = new WindowInfo();
                    this.mWindowInfoMaps.put(window, windowInfo);
                    windowInfo.setSize(w, h);
                }
                windowInfo.mVsyncTime = this.mVsyncTime;
                windowInfo.setFloat9(float9);
                windowInfo.mUpdated = true;
            }
        }

        void updateVelocity() {
            int maxVelocity = 0;
            synchronized (this) {
                Set<Object> kSet = this.mWindowInfoMaps.keySet();
                Iterator<Object> iterator = kSet.iterator();
                while (iterator.hasNext()) {
                    Object key = iterator.next();
                    WindowInfo windowInfo = this.mWindowInfoMaps.get(key);
                    if (this.mVsyncTime - windowInfo.mVsyncTime > 100) {
                        iterator.remove();
                    } else {
                        maxVelocity = Math.max(maxVelocity, windowInfo.updateVelocity());
                    }
                }
            }
            this.mVelocity = maxVelocity;
        }

        @Override // com.oplus.dynamicframerate.AnimationSpeedAware.BaseInfo
        public void reset() {
            this.mUpdated = false;
            clearWindowInfo();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            synchronized (this) {
                sb.append(WindowAnimationInfo.class.getSimpleName()).append(": mWindowInfoMaps = ").append(this.mWindowInfoMaps).append(", mVsyncTime = ").append(this.mVsyncTime).append(", mUpdated = ").append(this.mUpdated);
            }
            return sb.toString();
        }

        void clearWindowInfo() {
            synchronized (this) {
                if (this.mWindowInfoMaps.isEmpty()) {
                    return;
                }
                Set<Object> kSet = this.mWindowInfoMaps.keySet();
                Iterator<Object> iterator = kSet.iterator();
                while (iterator.hasNext()) {
                    Object key = iterator.next();
                    WindowInfo windowInfo = this.mWindowInfoMaps.get(key);
                    if (this.mVsyncTime - windowInfo.mVsyncTime > 100) {
                        iterator.remove();
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class WindowInfo {
            int mOriHeight;
            int mOriWidth;
            int mP0X;
            int mP0Y;
            int mP2X;
            int mP2Y;
            int mVelocity;
            long mVsyncTime;
            float[] mFloat9 = new float[9];
            boolean mIsFirst = true;
            boolean mUpdated = false;

            WindowInfo() {
            }

            int updateVelocity() {
                float[] fArr = this.mFloat9;
                int p0X = computeMatrix(0, 0, fArr[0], fArr[1], fArr[2]);
                float[] fArr2 = this.mFloat9;
                int p0Y = computeMatrix(0, 0, fArr2[4], fArr2[3], fArr2[5]);
                int i = this.mOriWidth;
                int i2 = this.mOriHeight;
                float[] fArr3 = this.mFloat9;
                int p2X = computeMatrix(i, i2, fArr3[0], fArr3[1], fArr3[2]);
                int i3 = this.mOriWidth;
                int i4 = this.mOriHeight;
                float[] fArr4 = this.mFloat9;
                int p2Y = computeMatrix(i3, i4, fArr4[4], fArr4[3], fArr4[5]);
                if (this.mIsFirst) {
                    this.mVelocity = 10000;
                    this.mIsFirst = false;
                    return 10000;
                }
                int frameRate = FramerateUtil.getFramerateFromInterval(AnimationSpeedAware.this.getFrameIntervalNanos());
                int v0 = computeVelocity(p0X, p0Y, this.mP0X, this.mP0Y, frameRate);
                int v2 = computeVelocity(p2X, p2Y, this.mP2X, this.mP2Y, frameRate);
                int max = (Math.max(v0, v2) * AnimationSpeedAware.this.mWindowAnimationSpeedRate) / 10;
                this.mVelocity = max;
                this.mP0X = p0X;
                this.mP0Y = p0Y;
                this.mP2X = p2X;
                this.mP2Y = p2Y;
                return max;
            }

            void setSize(int w, int h) {
                this.mOriWidth = w;
                this.mOriHeight = h;
                this.mP2X = w;
                this.mP2Y = h;
            }

            int computeMatrix(int x, int y, float scale, float skew, float trans) {
                return (int) ((x * scale) + (y * skew) + trans);
            }

            int computeVelocity(int x0, int y0, int x1, int y1, int frameRate) {
                int velocity = Math.max(Math.abs(x0 - x1) * frameRate, Math.abs(y0 - y1) * frameRate);
                if (OplusDebugUtil.DEBUG) {
                    OplusDebugUtil.e(AnimationSpeedAware.TAG, "computeVelocity: x0 = " + x0 + ", y0 = " + y0 + ", x1 = " + x1 + ", y1 = " + y1 + ", frameRate = " + frameRate + ", velocity = " + velocity);
                }
                return Math.max(Math.abs(x0 - x1) * frameRate, Math.abs(y0 - y1) * frameRate);
            }

            void setFloat9(float[] float9) {
                if (float9 == null || float9.length < this.mFloat9.length) {
                    OplusDebugUtil.e(AnimationSpeedAware.TAG, "setFloat9: float9 == null || float9.length < 9");
                    return;
                }
                int i = 0;
                while (true) {
                    float[] fArr = this.mFloat9;
                    if (i < fArr.length) {
                        fArr[i] = float9[i];
                        i++;
                    } else {
                        return;
                    }
                }
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append(WindowInfo.class.getSimpleName()).append(": mOriWidth = ").append(this.mOriWidth).append(", mOriHeight = ").append(this.mOriHeight).append(", mP0X = ").append(this.mP0X).append(", mP0Y = ").append(this.mP0Y).append(", mP2X = ").append(this.mP2X).append(", mP2Y = ").append(this.mP2Y).append(", mVelocity = ").append(this.mVelocity).append(", mVsyncTime = ").append(this.mVsyncTime).append(", mFloat9 = ").append(Arrays.toString(this.mFloat9)).append(", mIsFirst = ").append(this.mIsFirst).append(", mUpdated = ").append(this.mUpdated);
                return sb.toString();
            }
        }
    }

    /* loaded from: classes.dex */
    public class AnimationInfo {
        public int mVelocity;
        public int state;

        public AnimationInfo() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(AnimationInfo.class.getSimpleName());
            sb.append(": mVelocity = ").append(this.mVelocity).append(", state = ").append(AnimationSpeedAware.getStateString(this.state));
            return sb.toString();
        }
    }
}
