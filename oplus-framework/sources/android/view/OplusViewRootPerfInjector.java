package android.view;

import android.app.OplusActivityThreadExtImpl;
import android.content.pm.OplusPackageManager;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.Slog;
import android.view.ViewRootImpl;
import android.view.WindowManager;
import android.view.performance.IOplusViewRootPerfInjector;
import android.view.performance.OplusChoreographerPerfInjector;

/* loaded from: classes.dex */
public class OplusViewRootPerfInjector implements IOplusViewRootPerfInjector {
    private static boolean DEBUG = false;
    private static final int DISABLE_FORCE_RELAYOUT_SDK_LOWER_R = 736;
    private static final String TAG = "OplusViewRootPerfInjector";
    private static final int TYPE_FRAGMENT_ANIM_BOOST = 6;
    private String mBasePackageName;
    private Choreographer mChoreographer;
    private Handler mHandler;
    private boolean mIsFragmentAnimUI;
    private OplusChoreographerPerfInjector mOplusChoreographerPerfInjector;
    private OplusPackageManager mPm;
    private ViewRootImpl.TraversalRunnable mTraversalRunnable;
    public boolean mTraversalScheduled;
    private ViewRootImpl mViewRoot;
    public WindowManager.LayoutParams mWindowAttributes;
    private boolean mFirstFrameScheduled = false;
    private boolean FRAME_ONT = true;
    private boolean mIsOptApp = false;
    private int mDoFrameIndex = 0;
    private boolean mDisableRelayout = false;

    public OplusViewRootPerfInjector(ViewRootImpl viewRoot) {
        this.mViewRoot = viewRoot;
        this.mHandler = viewRoot.getWrapper().getHandler();
        this.mChoreographer = this.mViewRoot.getWrapper().getChoreographer();
        this.mWindowAttributes = this.mViewRoot.getWrapper().getWindowAttributes();
        this.mOplusChoreographerPerfInjector = new OplusChoreographerPerfInjector(this.mChoreographer);
        this.mTraversalScheduled = this.mViewRoot.getWrapper().getTraversalScheduled();
        this.mTraversalRunnable = this.mViewRoot.getWrapper().getTraversalRunnable();
        initViewRoomImpl();
    }

    @Override // android.view.performance.IOplusViewRootPerfInjector
    public void initViewRoomImpl() {
        String str;
        this.mBasePackageName = this.mViewRoot.getWrapper().getBasePackageName();
        this.mPm = OplusPackageManager.getOplusPackageManager(null);
        if (DEBUG) {
            Slog.d(TAG, "initViewRootImpl mBasePackageName" + this.mBasePackageName);
        }
        boolean z = SystemProperties.getBoolean("persist.oppo.frameopts", true);
        this.FRAME_ONT = z;
        if (z && (str = this.mBasePackageName) != null) {
            try {
                this.mDisableRelayout = this.mPm.inCptWhiteList(DISABLE_FORCE_RELAYOUT_SDK_LOWER_R, str);
            } catch (Exception e) {
                Log.e(TAG, "failed to query whitelist, package name: " + this.mBasePackageName);
                this.mIsOptApp = false;
            }
        }
    }

    @Override // android.view.performance.IOplusViewRootPerfInjector
    public void checkIsFragmentAnimUI() {
        WindowManager.LayoutParams layoutParams = this.mWindowAttributes;
        if (layoutParams != null && layoutParams.getTitle() != null) {
            String title = this.mWindowAttributes.getTitle().toString();
            String activityName = title.substring(title.indexOf("/") + 1);
            try {
                if (this.mPm.inOplusStandardWhiteList("sys_launch_opt_whitelist", 6, activityName)) {
                    this.mIsFragmentAnimUI = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "failed to query whitelist, activity name: " + activityName);
                this.mIsFragmentAnimUI = false;
            }
        }
    }

    @Override // android.view.performance.IOplusViewRootPerfInjector
    public boolean isFragmentAnimUI() {
        return this.mIsFragmentAnimUI;
    }

    @Override // android.view.performance.IOplusViewRootPerfInjector
    public void setIsFragmentAnimUI(boolean isFragmentAnimUI) {
        this.mIsFragmentAnimUI = isFragmentAnimUI;
    }

    @Override // android.view.performance.IOplusViewRootPerfInjector
    public boolean checkTraversalsImmediatelyProssible(boolean isFirst) {
        if (DEBUG) {
            Slog.d(TAG, "checkTraversalsImmediatelyProssible isFirst " + isFirst + " mFirstFrameScheduled " + this.mFirstFrameScheduled);
        }
        if (isFirst && !this.mFirstFrameScheduled && scheduleTraversalsImmediately()) {
            this.mFirstFrameScheduled = true;
            return true;
        }
        return false;
    }

    @Override // android.view.performance.IOplusViewRootPerfInjector
    public boolean checkTraversalsImmediatelyProssibleInTraversals(boolean isFirst, boolean mIsInTraversal) {
        if (DEBUG) {
            Slog.d(TAG, "checkTraversalsImmediatelyProssibleInTraversals isFirst " + isFirst + " mIsInTraversal " + mIsInTraversal);
        }
        if (isFirst && mIsInTraversal) {
            return scheduleTraversalsImmediately();
        }
        return false;
    }

    private boolean scheduleTraversalsImmediately() {
        int i;
        if (DEBUG) {
            Slog.d(TAG, "scheduleTraversalsImmediatelys DoFrameOptEnabled " + OplusActivityThreadExtImpl.sDoFrameOptEnabled);
        }
        if (!this.FRAME_ONT || !OplusActivityThreadExtImpl.sDoFrameOptEnabled || (i = this.mDoFrameIndex) > 1 || !this.mIsOptApp) {
            return false;
        }
        if (i == 1 && OplusActivityThreadExtImpl.sDoFrameOptEnabled) {
            OplusActivityThreadExtImpl.sDoFrameOptEnabled = false;
        }
        this.mDoFrameIndex++;
        Trace.traceBegin(8L, "scheduleTraversalsImmediately");
        if (DEBUG) {
            Slog.d(TAG, "scheduleTraversalsImmediately");
        }
        this.mViewRoot.getWrapper().setTraversalScheduled(true);
        try {
            this.mHandler.getLooper().getQueue().removeSyncBarrier(this.mViewRoot.getWrapper().getTraversalBarrier());
        } catch (IllegalStateException e) {
            Log.v(TAG, "The specified message queue synchronization  barrier token has not been posted or has already been removed ");
        }
        this.mViewRoot.getWrapper().setTraversalBarrier(this.mHandler.getLooper().getQueue().postSyncBarrier());
        this.mOplusChoreographerPerfInjector.postCallbackImmediately(3, this.mTraversalRunnable, null, 0L);
        this.mOplusChoreographerPerfInjector.doFrameImmediately();
        if (!this.mViewRoot.getWrapper().getUnbufferedInputDispatch()) {
            this.mViewRoot.getWrapper().scheduleConsumeBatchedInput();
        }
        this.mViewRoot.getWrapper().notifyRendererOfFramePending();
        this.mViewRoot.getWrapper().pokeDrawLockIfNeeded();
        Trace.traceEnd(8L);
        return true;
    }

    @Override // android.view.performance.IOplusViewRootPerfInjector
    public boolean disableRelayout() {
        return this.mDisableRelayout;
    }
}
