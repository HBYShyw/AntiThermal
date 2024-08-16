package android.view;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.view.WindowInsets;
import com.android.internal.policy.DecorView;
import com.oplus.bracket.OplusBracketLog;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class OplusMirrorViewSysUIHelper {
    private static final int COMPAT_VIEW_BOTTOM_OFFSET = 50;
    private static final boolean DISABLE_COMPAT = SystemProperties.getBoolean("persist.debug.SysUiCompat.disableCompat", false);
    private static final int HIDE_SYSTEM_BARS_TIMEOUT = 2500;
    public static final int MSG_APPLY_SYS_UI_VISIBILITY = 1;
    private static final String TAG = "SystemUiCompatibilityHelper";
    private WeakReference<View> mCompatSystemBarsView;
    private final OplusViewMirrorManager mMirrorManager;
    private int mOriginDispatchedSysUiVis = -1;
    private int mOriginViewSysUIVis = -1;
    private boolean mApplyingSysUIVisibility = false;
    private final Handler mHandler = new Handler(Looper.myLooper(), new Handler.Callback() { // from class: android.view.OplusMirrorViewSysUIHelper$$ExternalSyntheticLambda0
        @Override // android.os.Handler.Callback
        public final boolean handleMessage(Message message) {
            boolean lambda$new$0;
            lambda$new$0 = OplusMirrorViewSysUIHelper.this.lambda$new$0(message);
            return lambda$new$0;
        }
    });

    public OplusMirrorViewSysUIHelper(OplusViewMirrorManager oplusViewMirrorManager) {
        this.mMirrorManager = oplusViewMirrorManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(Message message) {
        if (message.what != 1) {
            return false;
        }
        handleApplySysUIVisibility((InsetsController) message.obj, message.arg1 == 1, message.arg2);
        return true;
    }

    public static String sysUIVisToString(int systemUIVisibility) {
        String str = ViewDebug.flagsToString(View.class, "mSystemUiVisibility", systemUIVisibility);
        return str.length() == 0 ? "0" : str;
    }

    public static boolean isNavBarVisible(int systemUiVisibility) {
        return (systemUiVisibility & 2) == 0;
    }

    public static boolean isNavBarRequestedVisible(int curDispatchedSysUiVis, InsetsController controller) {
        return (!isNavBarVisible(curDispatchedSysUiVis) || controller == null || (controller.getRequestedVisibleTypes() & WindowInsets.Type.navigationBars()) == 0) ? false : true;
    }

    public static boolean isStatusBarVisible(int systemUiVisibility) {
        return (systemUiVisibility & 4) == 0;
    }

    public static boolean isStatusBarRequestedVisible(int curDispatchedSysUiVis, InsetsController controller) {
        return (!isStatusBarVisible(curDispatchedSysUiVis) || controller == null || (controller.getRequestedVisibleTypes() & WindowInsets.Type.statusBars()) == 0) ? false : true;
    }

    public void initSysUIVisibility() {
        ViewRootImpl hostViewRoot;
        if (!DISABLE_COMPAT && (hostViewRoot = this.mMirrorManager.getHostViewRoot()) != null && hostViewRoot.mView != null && OplusViewMirrorUtils.shouldAdjustSysUIVisibility(hostViewRoot.mWindowAttributes.getTitle())) {
            int i = hostViewRoot.mView.mSystemUiVisibility;
            this.mOriginViewSysUIVis = i;
            if ((i & 7) != 0) {
                this.mOriginDispatchedSysUiVis = i & 7;
            } else {
                this.mOriginDispatchedSysUiVis = hostViewRoot.mDispatchedSystemUiVisibility;
            }
            if (OplusBracketLog.DEBUG) {
                OplusBracketLog.d(TAG, String.format("initSysUIVisibility: mOriginViewSysUIVis=%s, mOriginDispatchedSysUiVis=%s", sysUIVisToString(this.mOriginViewSysUIVis), sysUIVisToString(this.mOriginDispatchedSysUiVis)));
            }
            scheduleApplySysUIVisibility(hostViewRoot.getInsetsController(), false, WindowInsets.Type.systemBars());
        }
    }

    public void initCompatSystemBarsViewVisibility() {
        ViewRootImpl hostViewRoot;
        View compatView;
        if (!DISABLE_COMPAT && (hostViewRoot = this.mMirrorManager.getHostViewRoot()) != null && OplusViewMirrorUtils.TITLE_DOU_YU_PLAYER_ACTIVITY.equals(hostViewRoot.mWindowAttributes.getTitle().toString()) && (compatView = getCompatSystemBarsView()) != null) {
            this.mCompatSystemBarsView = new WeakReference<>(compatView);
            OplusBracketLog.d(TAG, "initSystemBarsViewVisibility: compat " + compatView);
            compatView.setVisibility(4);
        }
    }

    public void hookDispatchDispatchSystemUiVisibilityChanged(ViewRootImpl viewRoot) {
        int curViewSysUiVis;
        if (DISABLE_COMPAT) {
            return;
        }
        ViewRootImpl hostViewRoot = this.mMirrorManager.getHostViewRoot();
        if (this.mApplyingSysUIVisibility || !this.mMirrorManager.isInMirrorMode() || hostViewRoot == null || hostViewRoot.mView == null || hostViewRoot != viewRoot) {
            return;
        }
        CharSequence title = viewRoot.mWindowAttributes.getTitle();
        if (OplusViewMirrorUtils.shouldAdjustSysUIVisibility(title)) {
            int curDispatchedSysUiVis = viewRoot.mDispatchedSystemUiVisibility;
            if (OplusViewMirrorUtils.alwaysUpdateOriginViewSysUIVisibility(title) && this.mOriginViewSysUIVis != (curViewSysUiVis = viewRoot.mView.mSystemUiVisibility)) {
                if (OplusBracketLog.DEBUG) {
                    OplusBracketLog.d(TAG, "dispatchSystemUiVisibilityChanged: originDispatchedSysUI=" + sysUIVisToString(this.mOriginDispatchedSysUiVis) + " originVSysUI=" + sysUIVisToString(this.mOriginViewSysUIVis) + " curVSysUI=" + sysUIVisToString(curViewSysUiVis));
                }
                this.mOriginViewSysUIVis = curViewSysUiVis;
                this.mOriginDispatchedSysUiVis = curViewSysUiVis & 7;
            }
            InsetsController insetsController = viewRoot.getInsetsController();
            if (isStatusBarRequestedVisible(curDispatchedSysUiVis, insetsController) || isNavBarRequestedVisible(curDispatchedSysUiVis, insetsController)) {
                if (OplusBracketLog.DEBUG) {
                    OplusBracketLog.d(TAG, "dispatchSystemUiVisibilityChanged: hide systemBars, vis=" + sysUIVisToString(curDispatchedSysUiVis));
                }
                scheduleApplySysUIVisibility(viewRoot.getInsetsController(), false, WindowInsets.Type.systemBars(), false, HIDE_SYSTEM_BARS_TIMEOUT);
            }
        }
    }

    public void restoreCompatSystemBarsViewVisibility() {
        WeakReference<View> weakReference;
        View view;
        if (!DISABLE_COMPAT && this.mMirrorManager.getHostViewRoot() != null && (weakReference = this.mCompatSystemBarsView) != null && (view = weakReference.get()) != null) {
            view.setVisibility(0);
            OplusBracketLog.d(TAG, "restoreCompatSystemBarsViewVisibility: compat " + view);
        }
    }

    public void restoreSysUIVisibility() {
        ViewRootImpl hostViewRoot;
        if (DISABLE_COMPAT || (hostViewRoot = this.mMirrorManager.getHostViewRoot()) == null || hostViewRoot.mView == null || this.mOriginViewSysUIVis == -1 || this.mOriginDispatchedSysUiVis == -1) {
            return;
        }
        View hostView = hostViewRoot.mView;
        if (OplusViewMirrorUtils.shouldAdjustSysUIVisibility(hostViewRoot.mWindowAttributes.getTitle())) {
            if (OplusBracketLog.DEBUG) {
                OplusBracketLog.d(TAG, String.format("restoreSysUIVisibility: origin{sysUI=%s, viewSysUI=%s}, current{sysUI=%s, viewSysUI=%s}", sysUIVisToString(this.mOriginDispatchedSysUiVis), sysUIVisToString(this.mOriginViewSysUIVis), sysUIVisToString(hostViewRoot.mDispatchedSystemUiVisibility), sysUIVisToString(hostView.mSystemUiVisibility)));
            }
            hostView.setSystemUiVisibility(this.mOriginViewSysUIVis);
            int typesToHide = 0;
            int typesToShow = 0;
            if (isStatusBarVisible(this.mOriginDispatchedSysUiVis)) {
                typesToShow = 0 | 1;
            } else {
                typesToHide = 0 | 1;
            }
            if (isNavBarVisible(this.mOriginDispatchedSysUiVis)) {
                typesToShow |= 2;
            } else {
                typesToHide |= 2;
            }
            if (typesToShow != 0) {
                scheduleApplySysUIVisibility(hostViewRoot.getInsetsController(), true, typesToShow);
            }
            if (typesToHide != 0) {
                scheduleApplySysUIVisibility(hostViewRoot.getInsetsController(), false, typesToHide);
            }
        }
    }

    public void release(boolean die) {
        if (!DISABLE_COMPAT && !this.mMirrorManager.isInMirrorMode()) {
            this.mOriginViewSysUIVis = -1;
            this.mOriginDispatchedSysUiVis = -1;
            this.mApplyingSysUIVisibility = false;
            this.mCompatSystemBarsView = null;
        }
    }

    public void handleApplySysUIVisibility(InsetsController insetsController, boolean show, int types) {
        if (DISABLE_COMPAT || insetsController == null) {
            return;
        }
        this.mApplyingSysUIVisibility = true;
        if (show) {
            insetsController.show(types);
        } else {
            insetsController.hide(types);
        }
        this.mApplyingSysUIVisibility = false;
    }

    private void scheduleApplySysUIVisibility(InsetsController insetsController, boolean show, int types) {
        scheduleApplySysUIVisibility(insetsController, show, types, true, 0);
    }

    private void scheduleApplySysUIVisibility(InsetsController insetsController, boolean z, int i, boolean z2, int i2) {
        if (DISABLE_COMPAT) {
            return;
        }
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
        }
        if (z2) {
            handleApplySysUIVisibility(insetsController, z, i);
        } else {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, z ? 1 : 0, i, insetsController), i2);
        }
    }

    private View getCompatSystemBarsView() {
        ViewRootImpl hostViewRoot = this.mMirrorManager.getHostViewRoot();
        if (hostViewRoot == null || !(hostViewRoot.mView instanceof DecorView)) {
            return null;
        }
        DecorView decorView = hostViewRoot.getView();
        InsetsState state = hostViewRoot.getInsetsController().getState();
        InsetsSource statusBarInsetsSource = state.getWrapper().getExtImpl().peekDefaultSource(WindowInsets.Type.statusBars());
        if (statusBarInsetsSource == null) {
            return null;
        }
        Rect statusBarRect = new Rect(statusBarInsetsSource.getFrame());
        for (int i = 0; i < decorView.getChildCount(); i++) {
            View child = decorView.getChildAt(i);
            statusBarRect.bottom += 50;
            Rect viewRect = new Rect(child.mLeft, child.mTop, child.mRight, child.mBottom);
            if (child.getId() == -1 && child.getClass().equals(View.class) && !viewRect.isEmpty() && statusBarRect.contains(viewRect)) {
                return child;
            }
        }
        return null;
    }
}
