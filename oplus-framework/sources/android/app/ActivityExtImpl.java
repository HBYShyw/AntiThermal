package android.app;

import android.common.OplusFeatureCache;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManagerGlobal;
import android.view.debug.OplusViewExtraInfoHelper;
import android.view.rgbnormalize.IOplusRGBNormalizeManager;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.debug.InputLog;
import com.oplus.flexiblewindow.FlexibleWindowManager;
import com.oplus.util.OplusTypeCastingHelper;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import oplus.util.OplusLogUtil;

/* loaded from: classes.dex */
public class ActivityExtImpl implements IActivityExt {
    public static final int CALLERS_DEPTH = 5;
    private static final String TAG = "ActivityExtImpl";
    private Activity mBase;
    private boolean mSupportSplitScreenWindowingMode = false;
    private boolean mInPocketStudioMultiWindowModeBlackList = false;
    private int mScenario = 0;
    private boolean mIsSupportMiniPermissionDialog = false;

    public ActivityExtImpl(Object base) {
        this.mBase = (Activity) base;
    }

    public void onCreateForActivity(Activity activity, Bundle savedInstanceState) {
        ((IOplusCommonInjector) OplusFeatureCache.getOrCreate(IOplusCommonInjector.DEFAULT, new Object[0])).onCreateForActivity(activity, savedInstanceState);
    }

    public boolean isLoggable() {
        return "true".equals(SystemProperties.get("persist.sys.assert.panic"));
    }

    public void hookForInputLogIsLevelVerbose(String msg) {
        if (InputLog.isLevelVerbose()) {
            InputLog.v(TAG, msg);
        }
    }

    public void hookForInputLogV(String msg) {
        InputLog.v(TAG, msg);
    }

    public void hookForInputLogOnTouchEvent(String localClassName, MotionEvent event) {
        if (InputLog.isLevelVerbose() && event.getAction() == 0) {
            InputLog.v(TAG, "dispatchTouch onTouchEvent not handled:" + event + ", to:" + localClassName);
        }
    }

    public void debugEventHandled(InputEvent event, String detail) {
        InputLog.debugEventHandled(TAG, event, detail);
    }

    public void osenseSendFling(MotionEvent ev, int duration) {
        try {
            OplusActivityManager.getInstance().sendFlingTransit(ev, duration);
        } catch (Exception ex) {
            Log.e(TAG, "Exception = " + ex);
        }
    }

    public boolean isAppDebuggable(boolean isAppDebuggable) {
        return isAppDebuggable && isLoggable();
    }

    public boolean shouldInterceptBackKeyForMultiSearch(IBinder token, KeyEvent event) {
        try {
            if (event.getKeyCode() == 4 && event.getAction() == 0 && OplusActivityTaskManager.getInstance().shouldInterceptBackKeyForMultiSearch(token, true)) {
                InputLog.debugEventHandled(TAG, event, "intercept by multi-search");
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isZoomSupportMultiWindow(Activity activity, int windowingMode) {
        return windowingMode == 100 && activity != null && activity.getActivityThread() != null && activity.getActivityThread().mOplusActivityThreadExt.isZoomSupportMultiWindow(activity, windowingMode);
    }

    public void dumpViewExtraInfo(String prefix, PrintWriter writer) {
        OplusViewExtraInfoHelper.getInstance().dump(prefix, writer);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        updateCloseOnTouchOutside(newConfig);
    }

    public void onAttach() {
        updateCloseOnTouchOutside(this.mBase.mCurrentConfig);
        init();
    }

    private void updateCloseOnTouchOutside(Configuration config) {
        Window window = this.mBase.getWindow();
        if (window != null && FlexibleWindowManager.isFlexibleActivity(config)) {
            boolean flexibleActivitySuitable = FlexibleWindowManager.isFlexibleActivitySuitable(config);
            window.getWrapper().getExtImpl().setCloseOnTouchOutForFlexible(flexibleActivitySuitable);
            if (OplusLogUtil.DEBUG_PANIC) {
                Log.d(TAG, "updateCloseOnTouchOutside: activity=" + this.mBase + " flexibleActivitySuitable=" + flexibleActivitySuitable + " callers=" + Debug.getCallers(5));
            }
        }
    }

    public boolean isSupportFlexibleSubDisplay() {
        return FlexibleWindowManager.getInstance().hasFSDFeature();
    }

    public boolean isInMultiWindowModeInPocketStudio() {
        int i = this.mScenario;
        return i > 0 && i != 3 && this.mSupportSplitScreenWindowingMode;
    }

    public boolean isSupportSplitScreenWindowingMode() {
        return this.mSupportSplitScreenWindowingMode;
    }

    public void onScenarioChanged(Configuration newConfig) {
        int newScenario;
        OplusBaseConfiguration baseConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, newConfig);
        if (baseConfig != null && this.mScenario != (newScenario = baseConfig.mOplusExtraConfiguration.getScenario())) {
            this.mScenario = newScenario;
            this.mSupportSplitScreenWindowingMode = getActivityConfigs().getBoolean(FlexibleWindowManager.KEY_SUPPORT_SPLIT_SCREEN_WINDOWING_MODE, false);
        }
    }

    public void init() {
        OplusBaseConfiguration baseConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, this.mBase.mCurrentConfig);
        if (baseConfig != null) {
            this.mScenario = baseConfig.mOplusExtraConfiguration.getScenario();
        }
        if (this.mScenario > 0) {
            this.mSupportSplitScreenWindowingMode = getActivityConfigs().getBoolean(FlexibleWindowManager.KEY_SUPPORT_SPLIT_SCREEN_WINDOWING_MODE, false);
        }
        this.mIsSupportMiniPermissionDialog = OplusFeatureConfigManager.getInstacne().hasFeature(IOplusFeatureConfigList.FEATURE_PERMISSION_INTERCEPT);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0043, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0040, code lost:
    
        if (android.os.Trace.isTagEnabled(64) == false) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Bundle getActivityConfigs() {
        Bundle configs = new Bundle();
        try {
            try {
                if (Trace.isTagEnabled(64L)) {
                    Trace.traceBegin(64L, "getActivityConfigs");
                }
                configs = OplusActivityTaskManager.getInstance().getActivityConfigs(this.mBase.getActivityToken(), this.mBase.getPackageName());
            } catch (RemoteException | IllegalStateException e) {
                Log.e(TAG, "getActivityConfigs Exception ", e);
            }
        } finally {
            if (Trace.isTagEnabled(64L)) {
                Trace.traceEnd(64L);
            }
        }
    }

    public void dumpAllViewRootImpl(View view, String prefix, PrintWriter writer) {
        ArrayList<View> roots = WindowManagerGlobal.getInstance().getWindowViews();
        Iterator<View> it = roots.iterator();
        while (it.hasNext()) {
            View v = it.next();
            if (v != view && v.getViewRootImpl() != null) {
                v.getViewRootImpl().dump(prefix, writer);
            }
        }
        ((IOplusRGBNormalizeManager) OplusFeatureCache.getOrCreate(IOplusRGBNormalizeManager.DEFAULT, new Object[0])).onDumpActivity(view.getContext());
    }

    public boolean isSupportMiniPermissionDialog() {
        return this.mIsSupportMiniPermissionDialog;
    }
}
