package com.oplus.dynamicframerate;

import android.app.ActivityThread;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.view.DisplayInfo;
import android.view.animation.AnimationUtils;
import com.oplus.dynamicframerate.AnimationSpeedAware;
import com.oplus.dynamicframerate.FRTCConfigManager;
import com.oplus.dynamicframerate.util.FramerateUtil;
import com.oplus.vrr.IOPlusRefreshRate;

/* loaded from: classes.dex */
public class DynamicFrameRateController implements IDynamicFrameRateController {
    private static final String DEBUG_PROPERTY = "debug.os.perf.vrr.adfr.tfc.enable";
    private static final String FRTC_DESCRIPTION_PREFIX = "version-2-";
    private static final String FRTC_SUPPORT_PROPERTY_NAME = "persist.oplus.display.vrr.adfr";
    private static final String FRTC_TRACE_COUNTER_NAME = "FRTC-framerate";
    private static final boolean HAS_FEATURE = true;
    protected static final int MSG_CANCEL_STATE = 0;
    protected static final int MSG_SCHEDULE_TRAVERVALS = 1;
    private static final String SYSTEM_SERVER_PACKAGE_NAME = "android";
    private static final int TIMEOUT_TO_IDLE = 3000;
    private static volatile DynamicFrameRateController sInstance;
    private final int mDefaultFramerate;
    private boolean mEnableStableFrameRange;
    private String mFRTCDescription;
    private FRTCConfigManager mFrtcConfigManager;
    private Handler mHandler;
    private Runnable mIdleRunnable;
    private int mIdleStableFrameCount;
    private boolean mIs72Valid;
    private boolean mIs90Valid;
    private boolean mIsEnabled;
    private final boolean mIsSystem;
    private IOPlusRefreshRate mOplusRefreshRateService;
    private String mPackageName;
    private int mPendingFramerate;
    private long mPreviousFrameIntervalNanos;
    private int mPreviousFramerate;
    private SceneManager mSceneManager;
    private int mStableFrameCount;
    private static final String TAG = DynamicFrameRateController.class.getSimpleName();
    private static final boolean IS_FRTC_SUPPORT = SystemProperties.getBoolean("persist.oplus.display.vrr.adfr", true);
    private int mMaintainframes = 1;
    private long mPrevFrameTime = 0;
    private long mCurFrameTime = 0;
    private long mPrevFrameTimetest = 0;
    private long mCurFrameTimetest = 0;
    private int mFrames = 0;
    private int mPrevFrameState = -1;
    private int mCurFrameState = -1;
    private int mCountLastIdleChangeFrames = 0;

    private DynamicFrameRateController() {
        this.mPreviousFrameIntervalNanos = 0L;
        String packageName = getPackageName();
        this.mPackageName = packageName;
        boolean equals = "android".equals(packageName);
        this.mIsSystem = equals;
        this.mOplusRefreshRateService = getOplusRefreshRateService();
        this.mFRTCDescription = FRTC_DESCRIPTION_PREFIX + this.mPackageName;
        FRTCConfigManager fRTCConfigManager = new FRTCConfigManager(this.mOplusRefreshRateService, this.mPackageName);
        this.mFrtcConfigManager = fRTCConfigManager;
        int defaultFrameRate = fRTCConfigManager.getDefaultFrameRate();
        this.mDefaultFramerate = defaultFrameRate;
        this.mPreviousFramerate = defaultFrameRate;
        this.mPreviousFrameIntervalNanos = FramerateUtil.getFrameIntervalFromFramerate(defaultFrameRate);
        if (!SystemProperties.get(DEBUG_PROPERTY).isEmpty()) {
            this.mIsEnabled = SystemProperties.getBoolean(DEBUG_PROPERTY, true);
        } else {
            this.mIsEnabled = this.mFrtcConfigManager.isPackageEnable();
        }
        if (equals) {
            this.mIsEnabled = false;
        }
        SceneManager sceneManager = new SceneManager(this.mIsEnabled, this);
        this.mSceneManager = sceneManager;
        sceneManager.setMultiWindowIdleFrameIntervalNanos(FramerateUtil.getFrameIntervalFromFramerate(this.mFrtcConfigManager.getFrameRateConfig().mMuitiWindowIdleFramerate));
        OplusDebugUtil.i(TAG, "init info: mPackageName = " + this.mPackageName + ", mIsEnabled = " + this.mIsEnabled);
        if (this.mIsEnabled) {
            boolean isHighCapability = this.mFrtcConfigManager.isHighCapability();
            this.mEnableStableFrameRange = isHighCapability;
            if (isHighCapability || OplusDebugUtil.DEBUG_STABLEFRAMERATE) {
                if (OplusDebugUtil.DEBUG_FRAME_STABLE_PARAM == -1) {
                    this.mStableFrameCount = 7;
                } else {
                    this.mStableFrameCount = OplusDebugUtil.DEBUG_FRAME_STABLE_PARAM;
                }
                if (OplusDebugUtil.DEBUG_IDLE_STABLE_FRAMES == -1) {
                    this.mIdleStableFrameCount = 3;
                } else {
                    this.mIdleStableFrameCount = OplusDebugUtil.DEBUG_IDLE_STABLE_FRAMES;
                }
            }
            this.mHandler = new MyHandler(Looper.getMainLooper());
            this.mIdleRunnable = new Runnable() { // from class: com.oplus.dynamicframerate.DynamicFrameRateController.1
                @Override // java.lang.Runnable
                public void run() {
                    OplusDebugUtil.i(DynamicFrameRateController.TAG, "mIdleRunnable: run");
                    int defaultFramerate = DynamicFrameRateController.this.mFrtcConfigManager.getDefaultFrameRate();
                    if (DynamicFrameRateController.this.mPreviousFramerate != defaultFramerate) {
                        OplusDebugUtil.i(DynamicFrameRateController.TAG, "mIdleRunnable: run- setFrameRateTargetControl to " + defaultFramerate);
                        DynamicFrameRateController.this.dispatchTargetFramerate(defaultFramerate);
                        DynamicFrameRateController.this.mSceneManager.resetAnimationInfo();
                    }
                }
            };
            this.mSceneManager.setHandler(this.mHandler);
        }
    }

    public static boolean hasInstance() {
        return sInstance != null;
    }

    public static DynamicFrameRateController getInstance() {
        if (sInstance == null) {
            synchronized (DynamicFrameRateController.class) {
                if (sInstance == null) {
                    sInstance = new DynamicFrameRateController();
                }
            }
        }
        return sInstance;
    }

    public SceneManager getSceneManager() {
        return this.mSceneManager;
    }

    private String getPackageName() {
        String pkgName = ActivityThread.currentPackageName();
        if (pkgName != null) {
            return pkgName;
        }
        return "";
    }

    public boolean isEnable() {
        return this.mIsEnabled;
    }

    public IAnimationSpeedAware getAnimationSpeedAware() {
        return this.mSceneManager.getAnimationSpeedAware();
    }

    public void handlePendingFramerateChange() {
        dispatchTargetFramerate(this.mPendingFramerate);
        OplusDebugUtil.i(TAG, "handlePendingFramerateChange: dispatch pending framerate change task, for target " + this.mPendingFramerate);
    }

    public void onHandleDisplayEvent(int displayId, int event, DisplayInfo info) {
        if (this.mIsEnabled && displayId == 0 && event == 2 && info != null && info.name != null) {
            float refreshRate = info.getRefreshRate();
            int framerateThreshold = (int) refreshRate;
            OplusDebugUtil.i(TAG, "handleDisplayEvent: update framerateThreshold to " + framerateThreshold);
            FramerateUtil.updateFramerateThreshold(framerateThreshold);
        }
    }

    public boolean isHighCapability() {
        return this.mFrtcConfigManager.isHighCapability() && isUnderTargetFramerate();
    }

    public boolean onDoFrameFinished(long frameIntervalNanos, boolean isPreDraw) {
        boolean hasFramerateChange = false;
        if (!this.mIsEnabled) {
            return false;
        }
        int frameRateThreshold = FramerateUtil.getFramerateThreshold();
        if (120 > frameRateThreshold || 144 == frameRateThreshold) {
            return false;
        }
        AnimationSpeedAware.AnimationInfo info = this.mSceneManager.getUpdatedAnimationInfo();
        int targetFrameRate = getTargetFrameRate(info);
        if (this.mEnableStableFrameRange) {
            if (OplusDebugUtil.DEBUG) {
                OplusDebugUtil.d(TAG, "normal stable frame range check: beforechange:  mPreviousFramerate = " + this.mPreviousFramerate + " targetFrameRate = " + targetFrameRate + " mMaintainframes = " + this.mMaintainframes);
            }
            if (this.mPreviousFramerate == targetFrameRate) {
                this.mMaintainframes++;
            } else {
                int i = this.mMaintainframes;
                if (i < this.mStableFrameCount) {
                    targetFrameRate = this.mPreviousFramerate;
                    this.mMaintainframes = i + 1;
                } else {
                    this.mMaintainframes = 1;
                }
            }
            if (OplusDebugUtil.DEBUG) {
                OplusDebugUtil.d(TAG, "normal stable frame range check: afterchange:  mPreviousFramerate = " + this.mPreviousFramerate + " targetFrameRate = " + targetFrameRate + " mMaintainframes = " + this.mMaintainframes);
            }
            this.mCurFrameTime = AnimationUtils.currentAnimationTimeMillis();
            int i2 = info.state;
            this.mCurFrameState = i2;
            long j = this.mPrevFrameTime;
            if (j != 0 && this.mCurFrameTime - j < 100 && i2 != 0 && this.mPrevFrameState == 0) {
                this.mCountLastIdleChangeFrames = 1;
                targetFrameRate = this.mPreviousFramerate;
                if (OplusDebugUtil.DEBUG) {
                    OplusDebugUtil.d(TAG, "normal stable frame range check case1 : this frame mCurFrameState = " + AnimationSpeedAware.getStateString(this.mCurFrameState) + " mCountLastIdleChangeFrames = " + this.mCountLastIdleChangeFrames);
                }
            } else {
                int i3 = this.mCountLastIdleChangeFrames;
                if (i3 > 0 && i3 <= this.mIdleStableFrameCount) {
                    this.mCountLastIdleChangeFrames = i3 + 1;
                    targetFrameRate = this.mPreviousFramerate;
                    if (OplusDebugUtil.DEBUG) {
                        OplusDebugUtil.d(TAG, "normal stable frame range check case2 : this frame mCurFrameState = " + AnimationSpeedAware.getStateString(this.mCurFrameState) + " mCountLastIdleChangeFrames = " + this.mCountLastIdleChangeFrames);
                    }
                } else if (i3 > this.mIdleStableFrameCount) {
                    this.mCountLastIdleChangeFrames = i3 + 1;
                    if (OplusDebugUtil.DEBUG) {
                        OplusDebugUtil.d(TAG, "normal stable frame range check case3 : this frame mCurFrameState = " + AnimationSpeedAware.getStateString(this.mCurFrameState) + " mCountLastIdleChangeFrames = " + this.mCountLastIdleChangeFrames);
                    }
                }
            }
            this.mPrevFrameTime = this.mCurFrameTime;
            this.mPrevFrameState = this.mCurFrameState;
        }
        int i4 = this.mPreviousFramerate;
        if (i4 != targetFrameRate) {
            if (targetFrameRate > frameRateThreshold) {
                String str = TAG;
                OplusDebugUtil.i(str, "onDoFrameFinished: targetFrameRate is greater than framerate-threshold, , mPreviousFramerate = " + this.mPreviousFramerate + ", targetFrameRate = " + targetFrameRate + ", frameRateThreshold = " + frameRateThreshold + ", frameIntervalNanos = " + frameIntervalNanos);
                if (this.mPreviousFramerate != frameRateThreshold) {
                    OplusDebugUtil.i(str, "onDoFrameFinished: mPreviousFramerate != frameRateThreshold,  frameRateThreshold = " + frameRateThreshold + ", mPreviousFramerate = " + this.mPreviousFramerate + ", so change targetFrameRate to frameRateThreshold");
                    dispatchTargetFramerate(frameRateThreshold);
                }
            } else {
                hasFramerateChange = (targetFrameRate == this.mDefaultFramerate && i4 == frameRateThreshold) ? false : true;
                OplusDebugUtil.i(TAG, "onDoFrameFinished: mPreviousFramerate = " + this.mPreviousFramerate + ", targetFrameRate = " + targetFrameRate + ", frameRateThreshold = " + frameRateThreshold + ", frameIntervalNanos = " + frameIntervalNanos + ", hasFramerateChange = " + hasFramerateChange + ", Animation.state = " + info.state);
                if (OplusDebugUtil.DEBUG) {
                    OplusDebugUtil.trace("onDoFrameFinished: mPreviousFramerate = " + this.mPreviousFramerate + ", targetFrameRate = " + targetFrameRate + ", frameRateThreshold = " + frameRateThreshold + ", frameIntervalNanos = " + frameIntervalNanos + ", hasFramerateChange = " + hasFramerateChange + ", Animation.state = " + info.state);
                }
                if (isPreDraw && hasFramerateChange) {
                    this.mPendingFramerate = targetFrameRate;
                } else {
                    dispatchTargetFramerate(targetFrameRate);
                }
            }
        }
        return hasFramerateChange;
    }

    @Override // com.oplus.dynamicframerate.IDynamicFrameRateController
    public void onAnimationInfoSlientUpdate(AnimationSpeedAware.AnimationInfo info) {
        int targetFrameRate;
        if (this.mIsEnabled && this.mPreviousFramerate != (targetFrameRate = getTargetFrameRate(info))) {
            OplusDebugUtil.i(TAG, "onAnimationInfoSlientUpdate: mPreviousFramerate = " + this.mPreviousFramerate + ", targetFrameRate = " + targetFrameRate);
            if (OplusDebugUtil.DEBUG) {
                OplusDebugUtil.trace("onAnimationInfoSlientUpdate: mPreviousFramerate = " + this.mPreviousFramerate + ", targetFrameRate = " + targetFrameRate);
            }
            dispatchTargetFramerate(targetFrameRate);
        }
    }

    private boolean isUnderTargetFramerate() {
        int i = this.mPreviousFramerate;
        if (i == 90) {
            this.mIs90Valid = FramerateUtil.isSameFrameInterval(this.mPreviousFrameIntervalNanos, FramerateUtil.getFrameIntervalNanos());
        } else if (i == 72) {
            this.mIs72Valid = FramerateUtil.isSameFrameInterval(this.mPreviousFrameIntervalNanos, FramerateUtil.getFrameIntervalNanos());
        }
        if (OplusDebugUtil.DEBUG) {
            Trace.traceBegin(8L, "isUnderTargetFramerate: mPreviousFrameIntervalNanos = " + this.mPreviousFrameIntervalNanos + ", FramerateUtil.getFrameIntervalNanos() = " + FramerateUtil.getFrameIntervalNanos() + ", mIs90Valid = " + this.mIs90Valid + ", mIs72Valid = " + this.mIs72Valid);
            Trace.traceEnd(8L);
        }
        return this.mIs90Valid || this.mIs72Valid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchTargetFramerate(int targetFrameRate) {
        setFrameRateTargetControl(targetFrameRate);
        this.mPreviousFramerate = targetFrameRate;
        this.mPreviousFrameIntervalNanos = FramerateUtil.getFrameIntervalFromFramerate(targetFrameRate);
    }

    private int getTargetFrameRate(AnimationSpeedAware.AnimationInfo info) {
        FRTCConfigManager.FrameRateConfig frameRateConfig = this.mFrtcConfigManager.getFrameRateConfig();
        if (!AnimationSpeedAware.isDynamicFramerateState(info.state)) {
            if (OplusDebugUtil.DEBUG) {
                OplusDebugUtil.d(TAG, "getTargetFrameRate: framerate default targetFrameRate = " + this.mDefaultFramerate + ", state = " + AnimationSpeedAware.getStateString(info.state));
                OplusDebugUtil.trace("getTargetFrameRate: framerate default targetFrameRate = " + this.mDefaultFramerate + ", state = " + AnimationSpeedAware.getStateString(info.state));
            }
            return this.mDefaultFramerate;
        }
        if (info.state == 4) {
            if (OplusDebugUtil.DEBUG) {
                OplusDebugUtil.d(TAG, "getTargetFrameRate: scrollbar fade framerate targetFrameRate = " + frameRateConfig.getScrollBarFadeFrameRate());
                OplusDebugUtil.trace("getTargetFrameRate: scrollbar fade framerate targetFrameRate = " + frameRateConfig.getScrollBarFadeFrameRate());
            }
            return frameRateConfig.getScrollBarFadeFrameRate();
        }
        int ret = frameRateConfig.getTargetFrameRate(info.mVelocity);
        if (info.state == 1) {
            int inputIdleFramerate = frameRateConfig.getInputIdleFrameRate();
            if (OplusDebugUtil.DEBUG) {
                OplusDebugUtil.d(TAG, "getTargetFrameRate: input idle framerate targetFrameRate = " + inputIdleFramerate);
                OplusDebugUtil.trace("getTargetFrameRate: input idle framerate targetFrameRate = " + inputIdleFramerate);
            }
            if (ret < inputIdleFramerate) {
                return inputIdleFramerate;
            }
            return ret;
        }
        if (OplusDebugUtil.DEBUG) {
            OplusDebugUtil.d(TAG, "getTargetFrameRate: velocity-based targetFrameRate = " + ret + ", state = " + AnimationSpeedAware.getStateString(info.state) + ", velocity = " + info.mVelocity);
            OplusDebugUtil.trace("getTargetFrameRate: velocity-based targetFrameRate = " + ret + ", state = " + AnimationSpeedAware.getStateString(info.state) + ", velocity = " + info.mVelocity);
        }
        return ret;
    }

    private void setFrameRateTargetControl(int frameRate) {
        String str = TAG;
        OplusDebugUtil.i(str, "setFrameRateTargetControl: set framerate to " + frameRate);
        IOPlusRefreshRate iOPlusRefreshRate = this.mOplusRefreshRateService;
        if (iOPlusRefreshRate == null) {
            if (OplusDebugUtil.DEBUG) {
                OplusDebugUtil.e(str, "setFrameRateTargetControl: OplusRefreshRateService not avaliable");
                return;
            }
            return;
        }
        try {
            iOPlusRefreshRate.setFrameRateTargetControl(frameRate, this.mPackageName, false, this.mFRTCDescription);
            Trace.traceCounter(8L, FRTC_TRACE_COUNTER_NAME, frameRate);
        } catch (RemoteException e) {
            OplusDebugUtil.w(TAG, "setFrameRateTargetControl: RemoteException" + e.getMessage());
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mIdleRunnable);
            if (frameRate != this.mFrtcConfigManager.getDefaultFrameRate()) {
                this.mHandler.postDelayed(this.mIdleRunnable, 3000L);
            }
        }
    }

    private IOPlusRefreshRate getOplusRefreshRateService() {
        IOPlusRefreshRate ret = null;
        try {
            IBinder b = ServiceManager.getService("oplus_vrr_service");
            if (b == null) {
                OplusDebugUtil.e(TAG, "getOplusRefreshRateService: can't get service binder: IOPlusRefreshRate");
            }
            ret = IOPlusRefreshRate.Stub.asInterface(b);
            if (ret == null) {
                OplusDebugUtil.e(TAG, "getOplusRefreshRateService: can't get service interface: IOPlusRefreshRate");
            }
        } catch (Exception e) {
            OplusDebugUtil.e(TAG, "getOplusRefreshRateService: can't get service interface: IOPlusRefreshRate, exception info: " + e.getMessage());
        }
        return ret;
    }

    private boolean isInMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /* loaded from: classes.dex */
    class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DynamicFrameRateController.this.mSceneManager.getAnimationSpeedAware().handleCancelState(msg.arg1);
                    return;
                case 1:
                    DynamicFrameRateController.this.mSceneManager.handleScheduleTraversals(msg.obj);
                    return;
                default:
                    return;
            }
        }
    }
}
