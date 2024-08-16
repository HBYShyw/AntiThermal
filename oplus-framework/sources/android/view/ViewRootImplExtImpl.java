package android.view;

import android.app.ActivityThread;
import android.app.OplusActivityTaskManager;
import android.app.WindowConfiguration;
import android.common.OplusFeatureCache;
import android.common.OplusFrameworkFactory;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BLASTBufferQueue;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.util.TypedValue;
import android.view.SurfaceControl;
import android.view.ViewRootImpl;
import android.view.WindowManager;
import android.view.debug.IOplusViewDebugManager;
import android.view.inputmethod.InputMethodManager;
import android.view.performance.IOplusViewRootPerfInjector;
import com.oplus.animation.LaunchViewInfo;
import com.oplus.bracket.OplusBracketModeManager;
import com.oplus.darkmode.IOplusDarkModeManager;
import com.oplus.darkmode.OplusDarkModeData;
import com.oplus.debug.InputLog;
import com.oplus.dynamicframerate.DynamicFrameRateController;
import com.oplus.screenmode.OplusRefreshRateInjector;
import com.oplus.screenshot.OplusLongshotViewRoot;
import com.oplus.scrolloptim.ScrOptController;
import com.oplus.uifirst.IOplusUIFirstManager;
import com.oplus.util.OplusTypeCastingHelper;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class ViewRootImplExtImpl implements IViewRootImplExt {
    private static final String AC_PRELOAD_DISPLAY_NAME = "ActivityPreloadDisplay";
    private static final int DEBUG_CANCELDRAW_COUNT = 50;
    private static final int GESTURE_START_AREA_HEIGHT = 50;
    private static final int GESTURE_START_AREA_WIDTH = 50;
    private static final int MAX_SCREEN_WIDTH_DP = 2000;
    private static final int MAX_WAITING_SYNC_FRAME = 60;
    private static final int MIN_SCREEN_WIDTH_DP = 0;
    private static final String MIRAGE_APP_SHARE_DISPALY = "Mirage_appshare_display";
    private static final String MIRAGE_CAR_DISPALY = "Mirage_car_display";
    private static final int MIRAGE_ID_BASE = 10000;
    private static final int MIRAGE_ID_TVMODE = 2020;
    private static final String SETTINGS_FOLD_MODE = "oplus_system_folding_mode";
    private static final int SHIFT_ROUND_BIT_NUM_DP = 16;
    private static final int SHIFT_ROUND_BIT_NUM_ROTATION = 8;
    private static final String TAG = "ViewRootImplExtImpl";
    private static final String UNSPPORT_LOCAL_LAYOUT_PACKAGE = "com.tencent.mm";
    private int insertFrameCount;
    private boolean mIsMirageDisplayAdded;
    private OplusActivityTaskManager mOplusAtm;
    private OplusLetterBoxedTouchpadHelper mOplusLetterBoxedTouchpadHelper;
    private IOplusScrollToTopManager mOplusScrollToTopManager;
    private IOplusViewRootPerfInjector mOplusViewRootPerfInjector;
    private IRemoteTaskWindowInsetHelperExt mRTWindowInsetHelper;
    private boolean mSplitScreenImmersiveEnable;
    private IOplusSystemUINavigationGesture mSystemUINavigationGesture;
    private IOplusUIFirstManager mUIFirstMgr;
    public OplusViewRootImplHooks mViewRootHooks;
    private ViewRootImpl mViewRootImpl;
    private static final boolean DEBUG_PANIC = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final List<String> PACKAGES_NEED_SHOW_SURFACE_VIEW_BG = Arrays.asList("com.qiyi.video", "tv.danmaku.bili");
    private static final List<String> PACKAGES_NEED_SYNC_BINDER = Arrays.asList("com.baidu.input");
    private static final boolean IS_OSIE_NEED_NO_COMPRESS = SystemProperties.getBoolean("debug.sys.osie.neednocompress", false);
    private String mIpeQuickNotesState = "ipe_quick_notes_state";
    public IOplusDarkModeManager mManager = null;
    private OplusRefreshRateInjector mOplusRefreshRateInjector = null;
    private float mGlobalScale = 1.0f;
    private boolean mPendingBufferCountSetting = false;
    private int mCancelDrawCount = 0;
    private boolean mWaitingLastSyncDraw = false;
    private int mWaitingSyncDrawFrame = 0;

    public ViewRootImplExtImpl(Object base) {
        this.mViewRootImpl = (ViewRootImpl) base;
    }

    public void init(ViewRootImpl viewRootImpl, Context context) {
        this.mViewRootImpl = viewRootImpl;
        hookViewRootImplHooks(context);
    }

    public void hookNewInstance(Context context) {
        this.mManager = ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).newOplusDarkModeManager();
        this.mOplusRefreshRateInjector = OplusRefreshRateInjector.newInstance(context);
        this.mOplusViewRootPerfInjector = new OplusViewRootPerfInjector(this.mViewRootImpl);
        this.mOplusLetterBoxedTouchpadHelper = new OplusLetterBoxedTouchpadHelper(this.mViewRootImpl);
        this.mRTWindowInsetHelper = new RemoteTaskWindowInsetHelperExtImpl(context);
        this.mOplusAtm = new OplusActivityTaskManager();
        DynamicFrameRateController.getInstance().getSceneManager().onUpdateInternalDisplay(this.mViewRootImpl.getWrapper().getDisplay());
        this.mUIFirstMgr = (IOplusUIFirstManager) OplusFeatureCache.getOrCreate(IOplusUIFirstManager.DEFAULT, new Object[0]);
    }

    public IOplusViewRootPerfInjector getOplusViewRootPerfInjector() {
        return this.mOplusViewRootPerfInjector;
    }

    public void logConfigurationNightError(Context context, boolean useAutoDark) {
        this.mManager.logConfigurationNightError(context, useAutoDark);
    }

    public void logForceDarkAllowedStatus(Context context, boolean forceDarkAllowedDefault) {
        this.mManager.logForceDarkAllowedStatus(context, forceDarkAllowedDefault);
    }

    public void forceDarkWithoutTheme(Context context, View view, boolean useAutoDark) {
        this.mManager.forceDarkWithoutTheme(context, view, useAutoDark);
    }

    public int changeSystemUiVisibility(int mSystemUiVisibility) {
        return this.mManager.changeSystemUiVisibility(mSystemUiVisibility);
    }

    public void setDarkModeProgress(View view, Configuration globalConfig) {
        this.mManager.setDarkModeProgress(view, globalConfig);
        OplusBracketModeManager.getInstance().onConfigChange(view, globalConfig);
    }

    public void refreshForceDark(View view, Parcelable mOplusDarkModeData) {
        this.mManager.refreshForceDark(view, (OplusDarkModeData) OplusTypeCastingHelper.typeCasting(OplusDarkModeData.class, mOplusDarkModeData));
    }

    public void updateLogLevel() {
        InputLog.updateLogLevel();
    }

    public boolean isLevelDebug() {
        return InputLog.isLevelDebug();
    }

    public void debugInputStageDeliverd(String mTag, int flag, InputEvent event, String stage, String detail) {
        InputLog.debugInputStageDeliverd(mTag, flag, event, stage, detail);
    }

    public void v(String tag, String msg) {
        if (InputLog.isLevelVerbose()) {
            InputLog.v(tag, msg);
        }
    }

    public void debugEventHandled(String tag, InputEvent event, String detail) {
        InputLog.debugEventHandled(tag, event, detail);
    }

    public void debugInputEventEnqueue(String tag, InputEvent event, boolean immediately, boolean scheduled) {
        InputLog.debugInputEventEnqueue(tag, event, immediately, scheduled);
    }

    public void debugInputEventFinished(String tag, int flag, InputEvent event) {
        InputLog.debugInputEventFinished(tag, flag, event);
    }

    public void debugInputEventStart(String tag, InputEvent event) {
        InputLog.debugInputEventStart(tag, event);
    }

    public void setRefreshRateIfNeed(boolean ifNeed, Context context, View view, ViewRootImpl.W window) {
        if (ifNeed) {
            this.mOplusRefreshRateInjector.setRefreshRateIfNeed(context, (ViewGroup) view, window.asBinder());
        }
    }

    public void dispatchDetachedFromWindow(View view) {
        this.mViewRootHooks.dispatchDetachedFromWindow(view);
    }

    public void hookSetView(View view, Context context) {
        this.mViewRootHooks.setView(view);
        ((IOplusBurmeseZgHooks) OplusFrameworkFactory.getInstance().getFeature(IOplusBurmeseZgHooks.DEFAULT, new Object[0])).initBurmeseZgFlag(context);
    }

    public ViewRootImpl.W createWindowClient() {
        return this.mViewRootHooks.createWindowClient(this.mViewRootImpl);
    }

    public void hookViewRootImplHooks(Context context) {
        this.mViewRootHooks = new OplusViewRootImplHooks(this.mViewRootImpl, context);
    }

    public void setConnected(boolean isConnected) {
        OplusLongshotViewRoot longshot;
        OplusViewRootImplHooks oplusViewRootImplHooks = this.mViewRootHooks;
        if (oplusViewRootImplHooks != null && (longshot = oplusViewRootImplHooks.getLongshotViewRoot()) != null) {
            longshot.setConnected(isConnected);
        }
    }

    public boolean isConnected() {
        OplusLongshotViewRoot longshot;
        OplusViewRootImplHooks oplusViewRootImplHooks = this.mViewRootHooks;
        if (oplusViewRootImplHooks != null && (longshot = oplusViewRootImplHooks.getLongshotViewRoot()) != null) {
            return longshot.isConnected();
        }
        return false;
    }

    public void checkIsFragmentAnimUI() {
        this.mOplusViewRootPerfInjector.checkIsFragmentAnimUI();
    }

    public boolean isFragmentAnimUI() {
        return this.mOplusViewRootPerfInjector.isFragmentAnimUI();
    }

    public void showSoftInput(String pkg) {
        this.mViewRootImpl.getWrapper().getChoreographer().mChoreographerExt.showSoftInput(false);
        if (pkg != null && pkg.contains(UNSPPORT_LOCAL_LAYOUT_PACKAGE)) {
            this.mOplusViewRootPerfInjector.setIsFragmentAnimUI(false);
        }
    }

    public void hideSoftInputFromWindow(String pkg) {
        if (pkg != null && pkg.contains(UNSPPORT_LOCAL_LAYOUT_PACKAGE)) {
            this.mOplusViewRootPerfInjector.setIsFragmentAnimUI(true);
        }
    }

    public boolean checkTraversalsImmediatelyProssible(boolean isFirst) {
        return this.mOplusViewRootPerfInjector.checkTraversalsImmediatelyProssible(isFirst);
    }

    public boolean checkTraversalsImmediatelyProssibleInTraversals(boolean isFirst, boolean mIsInTraversal) {
        return this.mOplusViewRootPerfInjector.checkTraversalsImmediatelyProssibleInTraversals(isFirst, mIsInTraversal);
    }

    public boolean disableRelayout() {
        return this.mOplusViewRootPerfInjector.disableRelayout();
    }

    public void disableClickIfNeededWhenInputEventStart(InputEvent inputEvent) {
        OplusLetterBoxedTouchpadHelper oplusLetterBoxedTouchpadHelper = this.mOplusLetterBoxedTouchpadHelper;
        if (oplusLetterBoxedTouchpadHelper != null) {
            oplusLetterBoxedTouchpadHelper.disableClickIfNeededWhenInputEventStart(inputEvent);
        }
    }

    public void enableClickIfNeededWhenInputEventFinish(InputEvent inputEvent) {
        OplusLetterBoxedTouchpadHelper oplusLetterBoxedTouchpadHelper = this.mOplusLetterBoxedTouchpadHelper;
        if (oplusLetterBoxedTouchpadHelper != null) {
            oplusLetterBoxedTouchpadHelper.enableClickIfNeededWhenInputEventFinish(inputEvent);
        }
    }

    public boolean isClickDisabled() {
        OplusLetterBoxedTouchpadHelper oplusLetterBoxedTouchpadHelper = this.mOplusLetterBoxedTouchpadHelper;
        if (oplusLetterBoxedTouchpadHelper != null) {
            return oplusLetterBoxedTouchpadHelper.isClickDisabled();
        }
        return false;
    }

    public float getWindowGlobalScale() {
        return this.mGlobalScale;
    }

    public IRemoteTaskWindowInsetHelperExt getRTWindowInsetHelper() {
        return this.mRTWindowInsetHelper;
    }

    public void initSystemUINavigationGesture(ViewRootImpl viewRootImpl, Context context) {
        this.mSystemUINavigationGesture = new OplusSystemUINavigationGesture(viewRootImpl, context);
    }

    public void checkKeyguardAndConfig(String tag) {
        this.mSystemUINavigationGesture.checkKeyguardAndConfig(tag);
    }

    public void handleGestureMotionDown(View view) {
        this.mSystemUINavigationGesture.handleGestureMotionDown(view);
    }

    public void handleGestureConfigCheck() {
        this.mSystemUINavigationGesture.handleGestureConfigCheck();
    }

    public void setSystemGestureExclusionRegion(List<Rect> rects) {
        this.mSystemUINavigationGesture.setSystemGestureExclusionRegion(rects);
    }

    public boolean processGestureEvent(MotionEvent event) {
        if (Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "processGestureEvent coord:x=" + event.getX() + " y=" + event.getY() + " rawX=" + event.getRawX() + " rawY=" + event.getRawY());
        }
        try {
            return this.mSystemUINavigationGesture.processGestureEvent(event);
        } finally {
            Trace.traceEnd(8L);
        }
    }

    public void setLastReportedMergedConfiguration(View mView, Configuration newConfig, Context context) {
    }

    public void onWindowFocusChangedByRoot(boolean hasWindowFocus, View view, MergedConfiguration configuration) {
    }

    public void updateInputEventForBracketModeIfNeeded(View view, InputEvent event, InputEventReceiver receiver) {
        OplusBracketModeManager.getInstance().updateInputEventInTouchPanel(view, event, receiver);
    }

    public void updateInputEventToInputMethod(InputEvent event) {
        if (event instanceof MotionEvent) {
            int action = ((MotionEvent) event).getAction();
            InputMethodManager.getInstanceForDisplay(this.mViewRootImpl.getDisplayId()).getWrapper().getExtImpl().onViewRootTouchEvent(this.mViewRootImpl, (MotionEvent) event);
            if (action == 0 || action == 1) {
                this.mUIFirstMgr.ofbBoostHint(Process.myPid(), 0, 0, 0, 402, 0, 0L, 0L, 0L);
            }
        }
    }

    public int getColorMode(int colorMode) {
        return OplusColorModeManager.getInstance().getColorMode(colorMode);
    }

    public String getOsieLayerName(String tag, String title) {
        if (IS_OSIE_NEED_NO_COMPRESS) {
            return tag + "[" + title + "]";
        }
        return tag;
    }

    public int getBaseSize(WindowManager.LayoutParams lp, TypedValue value, Resources res) {
        if (lp.type == 2005) {
            res.getValue(201654463, value, true);
        }
        return (int) value.getDimension(res.getDisplayMetrics());
    }

    public boolean showSurfaceViewBackground(int subLayer) {
        String name = this.mViewRootImpl.getTitle().toString();
        if (subLayer == -2 || subLayer == -1) {
            Log.d(TAG, "showSurfaceViewBackground subLayer:" + subLayer + ", name:" + name);
            for (String pkgName : PACKAGES_NEED_SHOW_SURFACE_VIEW_BG) {
                if (name.contains(pkgName)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void debugCancelDraw(String tag, boolean cancelDraw, boolean isViewVisible) {
        int i;
        if (!cancelDraw && (i = this.mCancelDrawCount) > 0) {
            if (i >= 50) {
                Log.d(tag, " debugCancelDraw  cancelDraw=" + cancelDraw + ",count = " + this.mCancelDrawCount + "," + this.mViewRootImpl);
            }
            this.mCancelDrawCount = 0;
        } else if (cancelDraw && isViewVisible) {
            this.mCancelDrawCount++;
        }
        if (this.mCancelDrawCount == 50) {
            Log.d(tag, " debugCancelDraw some OnPreDrawListener onPreDraw return false,cancelDraw=" + cancelDraw + ",count=" + this.mCancelDrawCount + "," + this.mViewRootImpl);
        }
    }

    public void dump(String prefix, PrintWriter writer) {
        String str = prefix + "  ";
        writer.println(prefix + TAG);
        writer.println(prefix + "  mCancelDrawCount:" + this.mCancelDrawCount);
        writer.println(prefix + "  mWaitingLastSyncDraw:" + this.mWaitingLastSyncDraw);
    }

    public void hookCreateSyncIfNeeded(String tag, boolean syncBuffer, int seqId) {
        this.mWaitingLastSyncDraw = syncBuffer;
        this.mWaitingSyncDrawFrame = 0;
        if (DEBUG_PANIC && syncBuffer) {
            Log.d(tag, " hookCreateSyncIfNeeded seqId=" + seqId + " ,this=" + this);
        }
    }

    public void hookReportDrawFinished(String tag, int seqId) {
        if (DEBUG_PANIC && this.mWaitingLastSyncDraw) {
            Log.d(tag, " hookReportDrawFinished  frame=" + this.mWaitingSyncDrawFrame + ",seqId=" + seqId);
        }
        this.mWaitingSyncDrawFrame = 0;
        this.mWaitingLastSyncDraw = false;
    }

    public boolean cancelAndRedraw(String tag, boolean cancelAndRedraw, boolean isViewVisible, boolean syncBuffer) {
        if (!cancelAndRedraw && this.mWaitingLastSyncDraw && isViewVisible) {
            if (this.mWaitingSyncDrawFrame < 60) {
                Log.d(tag, " cancelAndRedraw  mWaitingLastSyncDraw = " + this.mWaitingLastSyncDraw + ",syncBuffer=" + syncBuffer + ",mWaitingSyncDrawFrame = " + this.mWaitingSyncDrawFrame);
                this.mWaitingSyncDrawFrame++;
                return true;
            }
        } else {
            debugCancelDraw(tag, cancelAndRedraw, isViewVisible);
        }
        return cancelAndRedraw;
    }

    public void registerRemoteAnimationsForWindow(RemoteAnimationDefinition definition) {
        OplusWindowManager oplusWm = new OplusWindowManager();
        if (this.mViewRootImpl.mWindow != null && definition != null) {
            try {
                oplusWm.registerRemoteAnimationsForWindow(this.mViewRootImpl.mWindow, definition);
            } catch (RemoteException e) {
                Log.e(TAG, "registerRemoteAnimationsForWin remoteException", new Throwable());
            }
        }
    }

    public void unregisterRemoteAnimationsForWindow() {
        OplusWindowManager oplusWm = new OplusWindowManager();
        if (this.mViewRootImpl.mWindow != null) {
            try {
                oplusWm.unregisterRemoteAnimationsForWindow(this.mViewRootImpl.mWindow);
            } catch (RemoteException e) {
                Log.e(TAG, "unregisterRemoteAnimationsForWindow remoteException", new Throwable());
            }
        }
    }

    public void setLaunchViewInfoForWindow(Object launchViewInfo) {
        OplusWindowManager oplusWm = new OplusWindowManager();
        if (this.mViewRootImpl.mWindow != null && launchViewInfo != null) {
            try {
                LaunchViewInfo oplusInfo = (LaunchViewInfo) launchViewInfo;
                oplusWm.setOplusLaunchViewInfoForWindow(this.mViewRootImpl.mWindow, oplusInfo);
            } catch (RemoteException e) {
                Log.e(TAG, "setOplusLaunchViewInfoForWindow remoteException", new Throwable());
            }
        }
    }

    public void clearLaunchViewInfoForWindow() {
        OplusWindowManager oplusWm = new OplusWindowManager();
        if (this.mViewRootImpl.mWindow != null) {
            try {
                oplusWm.clearOplusLaunchViewInfoForWindow(this.mViewRootImpl.mWindow);
            } catch (RemoteException e) {
                Log.e(TAG, "clearOplusLaunchViewInfoForWindow, remoteException:", new Throwable());
            }
        }
    }

    public int wrapConfigInfoIntoFlags(int flags, int smallestScreenWidthDp, int rotation, boolean relayoutAsync) {
        int newFlags = flags;
        if (smallestScreenWidthDp >= 0 && smallestScreenWidthDp <= 2000) {
            newFlags = smallestScreenWidthDp << (newFlags + 16);
        }
        if (rotation > 0) {
            newFlags = rotation << (newFlags + 8);
        }
        if (newFlags != flags && this.mViewRootImpl.mWindowAttributes.isFullscreen()) {
            Log.d(TAG, "wrapConfigInfoIntoFlags rotation=" + rotation + ", smallestScreenWidthDp=" + smallestScreenWidthDp + ", relayoutAsync=" + relayoutAsync + ", newFlags=" + newFlags + ", title=" + ((Object) this.mViewRootImpl.getTitle()));
        }
        return newFlags;
    }

    public void attachToWindow() {
        OplusViewMirrorManager.getInstance().attachToWindow(this.mViewRootImpl);
    }

    public boolean shouldDrawOnMirrorContent(View view) {
        return OplusViewMirrorManager.getInstance().shouldDrawOnMirrorContent(this.mViewRootImpl, view);
    }

    public void addMirrorSurfaceControl(IBinder mirrorToken, SurfaceControl mirrorRoot) {
        OplusWindowManager oplusWm = new OplusWindowManager();
        if (this.mViewRootImpl.mWindow != null && mirrorRoot != null && mirrorToken != null) {
            oplusWm.addBracketMirrorRootLeash(mirrorToken, mirrorRoot, this.mViewRootImpl.getSurfaceControl());
        }
    }

    public void removeMirrorSurfaceControl(IBinder mirrorToken) {
        OplusWindowManager oplusWm = new OplusWindowManager();
        if (this.mViewRootImpl.mWindow != null && mirrorToken != null) {
            oplusWm.removeBracketMirrorRootLeash(mirrorToken);
        }
    }

    public void notifySurfaceViewReplaced() {
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        for (int i = 0; i < this.mViewRootImpl.getWrapper().getSurfaceChangedCallbacks().size(); i++) {
            ((ViewRootImpl.SurfaceChangedCallback) this.mViewRootImpl.getWrapper().getSurfaceChangedCallbacks().get(i)).surfaceReplaced(transaction);
        }
        this.mViewRootImpl.applyTransactionOnDraw(transaction);
    }

    public void drawViewRoot() {
        OplusViewMirrorManager.getInstance().drawRoot(this.mViewRootImpl);
    }

    public void setFrame(int width, int height) {
        OplusViewMirrorManager.getInstance().relayout(this.mViewRootImpl, width, height);
    }

    public void doDie() {
        OplusViewMirrorManager.getInstance().die(this.mViewRootImpl);
    }

    public void notifySurfaceDestroyed() {
        OplusViewMirrorManager.getInstance().notifySurfaceDestroyed(this.mViewRootImpl);
    }

    public void updateBlastSurfaceIfNeeded(BLASTBufferQueue blastBufferQueue) {
        if (blastBufferQueue != null && blastBufferQueue.isSameSurfaceControl(this.mViewRootImpl.getSurfaceControl())) {
            OplusViewMirrorManager.getInstance().updateBlastSurface(this.mViewRootImpl);
        }
    }

    public void updateSurfaceViewRelativeZIfNeed(SurfaceView view, SurfaceControl.Transaction t) {
        if (view != null && view.getSurfaceControl() != null && view.getSurfaceControl().isValid()) {
            OplusViewMirrorManager.getInstance().updateSurfaceViewRelativeZ(this.mViewRootImpl, view, t);
        }
    }

    public Rect adjustSurfaceViewFrameIfNeed(SurfaceView view, Rect inRect) {
        if (view != null) {
            return OplusViewMirrorManager.getInstance().adjustSurfaceViewFrameIfNeed(this.mViewRootImpl, view, inRect);
        }
        return inRect;
    }

    public void hookDispatchDispatchSystemUiVisibilityChanged() {
        OplusViewMirrorManager.getInstance().hookDispatchDispatchSystemUiVisibilityChanged(this.mViewRootImpl);
    }

    public void setScrollToTopRootView(View view, WindowManager.LayoutParams params) {
        initScrollToTopManager();
        this.mOplusScrollToTopManager.setWindowRootView(view, params);
    }

    public void setScrollToTopWinFrame(Rect winFrame) {
        initScrollToTopManager();
        this.mOplusScrollToTopManager.setWindowFrame(winFrame);
    }

    public void handleWindowFocusChanged(Context context, boolean hasFocus) {
        initScrollToTopManager();
        this.mOplusScrollToTopManager.handleWindowFocusChanged(context, hasFocus);
    }

    public void postShowGuidePopupRunnable(View decorView) {
        initScrollToTopManager();
        this.mOplusScrollToTopManager.postShowGuidePopupRunnable(decorView);
    }

    public void processPointerEvent(MotionEvent e, Context context, boolean handled) {
        if (handled && e.getAction() == 1) {
            Log.d(TAG, "MotionEvent " + e + " handled by client, just return");
        } else {
            initScrollToTopManager();
            this.mOplusScrollToTopManager.processPointerEvent(e, context);
        }
    }

    public void onWindowDying() {
        initScrollToTopManager();
        this.mOplusScrollToTopManager.onWindowDying();
        IOplusSystemUINavigationGesture iOplusSystemUINavigationGesture = this.mSystemUINavigationGesture;
        if (iOplusSystemUINavigationGesture != null) {
            iOplusSystemUINavigationGesture.unRegisterNavGestureListener();
        }
    }

    private void initScrollToTopManager() {
        if (this.mOplusScrollToTopManager == null) {
            this.mOplusScrollToTopManager = ((IOplusScrollToTopManager) OplusFeatureCache.getOrCreate(IOplusScrollToTopManager.DEFAULT, new Object[0])).newInstance();
        }
    }

    public boolean needUpdateInternalDisplay(Context context, Display display) {
        boolean movedToDifferentDisplay;
        if (context != null) {
            int activityDisplayId = context.getDisplayId();
            DisplayManagerGlobal displayManagerGlobal = DisplayManagerGlobal.getInstance();
            if (displayManagerGlobal == null) {
                return false;
            }
            DisplayInfo displayInfo = displayManagerGlobal.getDisplayInfo(display.getDisplayId());
            if (displayInfo != null && MIRAGE_APP_SHARE_DISPALY.equals(displayInfo.name)) {
                return false;
            }
            if ((activityDisplayId < 10000 || display.getDisplayId() != 0) && ((activityDisplayId != 0 || display.getDisplayId() < 10000) && ((activityDisplayId != 2020 || display.getDisplayId() != 0) && (activityDisplayId != 0 || display.getDisplayId() != 2020)))) {
                movedToDifferentDisplay = false;
            } else {
                movedToDifferentDisplay = true;
            }
            if (movedToDifferentDisplay) {
                Slog.i(TAG, "need to update dispalyId=" + activityDisplayId + " Context=" + context);
                return true;
            }
        }
        return false;
    }

    public void intersectOverrideWindowBoundsIfNeed(MergedConfiguration mergedConfiguration, Rect outRect) {
        if (this.mViewRootImpl.getDisplayId() == 0) {
            sanitizeWindowBounds(outRect);
            return;
        }
        int windowType = this.mViewRootImpl.mWindowAttributes.type;
        if ((windowType != 2010 && windowType != 2003) || Process.myUid() != 1000) {
            return;
        }
        Rect overrideWindowBounds = mergedConfiguration.getOverrideConfiguration().windowConfiguration.getBounds();
        if (!overrideWindowBounds.isEmpty()) {
            outRect.intersect(overrideWindowBounds);
        }
        Slog.i(TAG, "intersectOverrideWindowBoundsIfNeed outRect =:" + outRect);
    }

    public boolean updateAlwaysConsumeSystemBarsIfNeeded(boolean pendingAlwaysConsumeSystemBars) {
        boolean changed = this.mViewRootImpl.mPendingAlwaysConsumeSystemBars != pendingAlwaysConsumeSystemBars && this.mViewRootImpl.getHostVisibility() == 0;
        if (changed) {
            this.mViewRootImpl.mAttachInfo.mAlwaysConsumeSystemBars = pendingAlwaysConsumeSystemBars;
            this.mViewRootImpl.mApplyInsetsRequested = true;
            Slog.d(TAG, "updateAlwaysConsumeSystemBarsIfNeeded: pendingAlwaysConsumeSystemBars has changed, we should apply it to make sure the layout is correct");
        }
        return changed;
    }

    public boolean shouldBlockResizeReportForSplashScreen(WindowManager.LayoutParams windowAttrs, String packageName) {
        if (windowAttrs != null && windowAttrs.type == 3 && windowAttrs.getTitle().toString().contains("Splash Screen") && "com.android.systemui".equals(packageName)) {
            Slog.d(TAG, "Block resize report for SplashScreen of systemui.");
            return true;
        }
        return false;
    }

    public boolean changeActivityPreloadDisplay(int displayId, String displayName) {
        if (displayId == 0 && displayName != null && displayName.equals(AC_PRELOAD_DISPLAY_NAME)) {
            Slog.i(TAG, "changeActivityPreloadDisplay to default!");
            return true;
        }
        return false;
    }

    public boolean isZoomWindowMode(int windowMode) {
        return windowMode == 100;
    }

    private void sanitizeWindowBounds(Rect bounds) {
        try {
            Resources r = ActivityThread.currentActivityThread().getSystemContext().getResources();
            WindowConfiguration winConfig = r.getConfiguration().windowConfiguration;
            if (winConfig != null && winConfig.getWindowingMode() == 6 && bounds.height() <= dpToPx(80.0f, r)) {
                int adjustment = bounds.top + dpToPx(220.0f, r);
                if (ViewRootImpl.DEBUG_DIALOG) {
                    Slog.d(TAG, "sanitizeWindowBounds old=" + bounds + ", newBottom=" + adjustment + ", title=" + ((Object) this.mViewRootImpl.getTitle()) + ", this=" + this.mViewRootImpl);
                }
                bounds.bottom = adjustment;
            }
        } catch (Exception e) {
        }
    }

    private static int dpToPx(float dpValue, Resources res) {
        return (int) TypedValue.applyDimension(1, dpValue, res.getDisplayMetrics());
    }

    public void updateSplitScreenImmersiveFlag(Bundle bundle) {
        this.mSplitScreenImmersiveEnable = bundle.getBoolean("SplitScreenImmersiveEnable", false);
        this.mGlobalScale = bundle.getFloat("WindowGlobalScale", 1.0f);
    }

    public WindowInsets adjustWindowInsetsForDispatchIfNeed(WindowInsets windowInsets) {
        if (this.mSplitScreenImmersiveEnable) {
            return windowInsets.removeCutoutInsets();
        }
        return windowInsets;
    }

    public boolean isInterceptedProcessPointerEvent(String tag, MotionEvent event, Context context) {
        if (isStylusEvent(event) && tag.contains("StatusBar") && context != null && !Process.isIsolated() && Settings.Global.getInt(context.getContentResolver(), this.mIpeQuickNotesState, 0) == 1 && Settings.Global.getInt(context.getContentResolver(), "oplus_system_folding_mode", 0) == 1) {
            int width = dpToPx(50.0f, context.getResources());
            int height = dpToPx(50.0f, context.getResources());
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            Rect rect = new Rect(dm.widthPixels - width, 0, dm.widthPixels, height);
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                Slog.e(TAG, "ProcessPointerEvent  Intercepted StatusBar");
                return true;
            }
        }
        return false;
    }

    private boolean isStylusEvent(MotionEvent event) {
        if (event == null || !event.isFromSource(16386)) {
            return false;
        }
        int tool = event.getToolType(0);
        return tool == 2 || tool == 4;
    }

    public void updateRecordSurfaceViewState(boolean hasSurfaceView) {
        IBinder windowToken = this.mViewRootImpl.getWindowToken();
        if (DEBUG_PANIC) {
            Log.d(TAG, "notifyDrawFinished, updateRecordSurfaceViewState windowToken=" + windowToken);
        }
        if (windowToken != null) {
            try {
                this.mOplusAtm.updateRecordSurfaceViewState(windowToken, hasSurfaceView);
            } catch (Exception e) {
                Log.e(TAG, "updateRecordSurfaceViewState exception: ", new Throwable());
            }
        }
    }

    public void markBeforeDispatchTouchEvent(MotionEvent event, String title) {
        getViewDebugManager().markBeforeDispatchTouchEvent(event, title);
    }

    public void markAfterDispatchTouchEvent(MotionEvent event) {
        getViewDebugManager().markAfterDispatchTouchEvent(event);
    }

    public void markAndDumpWindowFocusChangeMsg(String tag, Handler handler) {
        getViewDebugManager().markAndDumpWindowFocusChangeMsg(tag, handler);
    }

    public String markPerformLayout(View hostView, WindowManager.LayoutParams windowAttrs) {
        return getViewDebugManager().markPerformLayout(hostView, windowAttrs);
    }

    public String markPerformMeasure(View hostView, WindowManager.LayoutParams windowAttrs, int childWidthMeasureSpec, int childHeightMeasureSpec) {
        return getViewDebugManager().markPerformMeasure(hostView, windowAttrs, childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public String markPerformDraw(View hostView, WindowManager.LayoutParams windowAttrs) {
        return getViewDebugManager().markPerformDraw(hostView, windowAttrs);
    }

    public String markScheduleTraversals(View hostView, WindowManager.LayoutParams windowAttrs) {
        return getViewDebugManager().markScheduleTraversals(hostView, windowAttrs);
    }

    public void markOnHandleMessageImpl(String msgName) {
        getViewDebugManager().markOnHandleMessageImpl(msgName);
    }

    public void markPerformMeasureReason(String reason) {
        getViewDebugManager().markPerformMeasureReason(reason);
    }

    public void markPerformLayoutReason(String reason) {
        getViewDebugManager().markPerformLayoutReason(reason);
    }

    public void markOnSetFrame(Rect frame, WindowManager.LayoutParams windowAttrs) {
        getViewDebugManager().markOnSetFrame(frame, windowAttrs);
    }

    public void markHandleAppVisibility(boolean visible, WindowManager.LayoutParams windowAttrs) {
        getViewDebugManager().markHandleAppVisibility(visible, windowAttrs);
    }

    public void markShowInsets(int insetsType, boolean fromIme) {
        getViewDebugManager().markShowInsets(insetsType, fromIme);
    }

    public void markHideInsets(int insetsType, boolean fromIme) {
        getViewDebugManager().markHideInsets(insetsType, fromIme);
    }

    public void markOnPerformTraversalsStart(View hostView, boolean first) {
        getViewDebugManager().markOnPerformTraversalsStart(hostView, first);
    }

    public void markOnPerformTraversalsEnd(View hostView) {
        getViewDebugManager().markOnPerformTraversalsEnd(hostView);
    }

    public void markHandleWindowFocusChange(boolean windowFocusChanged, boolean upcomingWindowFocus, boolean added, WindowManager.LayoutParams windowAttributes) {
        getViewDebugManager().markHandleWindowFocusChange(windowFocusChanged, upcomingWindowFocus, added, windowAttributes);
    }

    public IOplusViewDebugManager getViewDebugManager() {
        return (IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0]);
    }

    public boolean shouldSkipScheduleTraversals(Object vri) {
        return DynamicFrameRateController.getInstance().getSceneManager().shouldSkipScheduleTraversals(vri);
    }

    public void onUpdateInternalDisplay(Display display) {
        DynamicFrameRateController.getInstance().getSceneManager().onUpdateInternalDisplay(display);
    }

    public void setPendingBufferCountSetting(boolean pendingBufferCountSetting) {
        this.mPendingBufferCountSetting = pendingBufferCountSetting;
        if (ScrOptController.getInstance().getOptimConfig().checkOptEnable() && ScrOptController.getInstance().checkFrameInsertEnable()) {
            this.insertFrameCount = ScrOptController.getInstance().getOptimConfig().checkInsertNum();
        }
    }

    public void checkPendingBufferCountSetting(Surface surface) {
        if (surface != null && surface.isValid() && this.mPendingBufferCountSetting) {
            if (this.insertFrameCount > 1) {
                surface.getSurfaceExt().setMaxDequeuedBufferCount(this.insertFrameCount + 1);
            }
            Log.d(TAG, "setMaxDequeuedBufferCount: " + (this.insertFrameCount + 1));
            this.mPendingBufferCountSetting = false;
        }
    }

    public void updateTaskBarInset(View view, WindowInsets insets, InsetsState insetsState) {
        OplusTaskBarUtils.getInstance().updateTaskBarInset(view, insets, insetsState);
    }

    public boolean isSupportLocalLayout(int displayId) {
        DisplayManagerGlobal displayManagerGlobal;
        DisplayInfo displayInfo;
        Configuration configuration = this.mViewRootImpl.mContext.getResources().getConfiguration();
        if (configuration.windowConfiguration.getWindowingMode() == 120 && (this.mViewRootImpl.mWindowAttributes.type != 1 || (UNSPPORT_LOCAL_LAYOUT_PACKAGE.equals(this.mViewRootImpl.mWindowAttributes.packageName) && !configuration.windowConfiguration.getBounds().equals(this.mViewRootImpl.mWinFrame)))) {
            return false;
        }
        if ((configuration.getOplusExtraConfiguration().getScenario() == 2 || UNSPPORT_LOCAL_LAYOUT_PACKAGE.equals(this.mViewRootImpl.mWindowAttributes.packageName)) && !configuration.windowConfiguration.getBounds().equals(this.mViewRootImpl.mWinFrame)) {
            return false;
        }
        if (displayId == 0 && PACKAGES_NEED_SYNC_BINDER.contains(ActivityThread.currentPackageName())) {
            return false;
        }
        return displayId == 0 || (displayManagerGlobal = DisplayManagerGlobal.getInstance()) == null || (displayInfo = displayManagerGlobal.getDisplayInfo(displayId)) == null || !MIRAGE_CAR_DISPALY.equals(displayInfo.name);
    }

    public void onDisplayChanged(int changedDisplayId) {
        View view;
        ViewRootImpl viewRootImpl = this.mViewRootImpl;
        if (viewRootImpl == null || (view = viewRootImpl.mView) == null) {
            return;
        }
        this.mIsMirageDisplayAdded = false;
        try {
            Context context = view.getContext();
            DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
            Display[] displays = displayManager.getDisplays();
            for (Display display : displays) {
                int displayId = display.getDisplayId();
                String displayName = display.getName();
                if (displayId >= 10000 && displayName.equals(MIRAGE_CAR_DISPALY)) {
                    this.mIsMirageDisplayAdded = true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to get DisplayInfo " + e.getMessage());
        }
    }

    public boolean isMirageDisplayAdded() {
        return this.mIsMirageDisplayAdded;
    }

    public void hookSetBinderUxFlag(boolean z, WindowManager.LayoutParams layoutParams) {
        ViewRootImpl viewRootImpl;
        if (layoutParams == null || (viewRootImpl = this.mViewRootImpl) == null) {
            return;
        }
        Context context = viewRootImpl.getWrapper().getContext();
        Configuration configuration = context != null ? context.getResources().getConfiguration() : null;
        if (layoutParams.type == 3 || (configuration != null && configuration.windowConfiguration.getActivityType() == 2)) {
            if (DEBUG_PANIC) {
                Slog.i(TAG, "hookSetBinderUxFlag:" + z + " mBasePackageName:" + this.mViewRootImpl.mBasePackageName);
            }
            ((IOplusUIFirstManager) OplusFeatureCache.getOrCreate(IOplusUIFirstManager.DEFAULT, new Object[0])).setBinderThreadUxFlag(-1, z ? 1 : 0);
        }
    }
}
