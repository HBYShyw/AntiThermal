package android.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.View;
import com.android.internal.policy.DecorView;
import com.google.android.collect.Sets;
import com.oplus.flexiblewindow.FlexibleWindowManager;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusFlexibleViewManager {
    private static final String TAG = "OplusFlexibleViewManager";
    private static final String WX_LAUNCHER_ACTIVITY = "com.tencent.mm/.ui.LauncherUI";
    private static final String WX_RECYCLERVIEW = "WxRecyclerView";
    private static volatile OplusFlexibleViewManager sInstance;
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final Set<String> FLEXIBLE_KEEP_MEASURE_ACTIVITIES = Sets.newArraySet();
    private static Configuration sLastReportedConfig = new Configuration();

    private OplusFlexibleViewManager() {
    }

    public static OplusFlexibleViewManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusFlexibleViewManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusFlexibleViewManager();
                }
            }
        }
        return sInstance;
    }

    public static Configuration getLastReportedConfig() {
        return sLastReportedConfig;
    }

    public static void setLastReportedConfig(Configuration configuration) {
        if (configuration != null) {
            sLastReportedConfig.updateFrom(configuration);
        }
    }

    public void hookSetMeasureDimension(View view, int measuredWidth, int measuredHeight) {
    }

    public void hookAfterMeasure(View view) {
        forceAdjustWXRecyclerView(view);
    }

    private boolean keepMeasureSpecIfNeed(View view, int measuredWidth, int measuredHeight) {
        if (view == null || view.getViewRootImpl() == null) {
            return false;
        }
        ViewRootImpl viewRoot = view.getViewRootImpl();
        String shortComponentName = obtainActivityInfoFromViewRoot(viewRoot);
        if (!TextUtils.isEmpty(shortComponentName) && FLEXIBLE_KEEP_MEASURE_ACTIVITIES.contains(shortComponentName)) {
            Configuration config = viewRoot.mContext.getResources().getConfiguration();
            Rect appBounds = config.windowConfiguration.getAppBounds();
            boolean isFlexibleScenario = isFlexibleSubDisplayScenario(config);
            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();
            int rootWidth = view.getRootView().getMeasuredWidth();
            int rootHeight = view.getRootView().getMeasuredHeight();
            if (isFlexibleScenario && !appBounds.isEmpty() && appBounds.width() == measuredWidth && appBounds.height() == measuredHeight) {
                if (DEBUG) {
                    Log.d(TAG, "keepMeasureSpecIfNeed, viewW=" + viewWidth + ", rootW=" + rootWidth + ", viewH=" + viewHeight + ", rootH=" + rootHeight + ", measuredWidth=" + measuredWidth + ", measuredWidth=" + measuredHeight + ", appBounds=" + appBounds + ", view=" + view);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    private void forceAdjustMeasureSpec(View view) {
        if (view == null || view.getViewRootImpl() == null) {
            return;
        }
        if (view.getViewWrapper().getViewExt().shouldKeepMeasureSpec()) {
            Configuration config = view.getViewRootImpl().mContext.getResources().getConfiguration();
            Rect appBounds = config.windowConfiguration.getAppBounds();
            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();
            int oldMeasureWidth = View.MeasureSpec.getSize(view.mOldWidthMeasureSpec);
            int oldMeasureHeight = View.MeasureSpec.getSize(view.mOldHeightMeasureSpec);
            if (DEBUG) {
                Log.d(TAG, "forceAdjustMeasureSpec viewWidth=" + viewWidth + ", viewHeight=" + viewHeight + ", oldMeasureWidth=" + oldMeasureWidth + ", oldMeasureHeight=" + oldMeasureHeight + ", appBounds=" + appBounds + ", view=" + view);
            }
            if (viewWidth > 0 && viewHeight > 0 && viewWidth != oldMeasureWidth && viewHeight != oldMeasureHeight && appBounds.width() == oldMeasureWidth) {
                Log.d(TAG, "forceAdjustMeasureSpec with old measure view=" + view + ", appBounds=" + appBounds);
                view.setMeasuredDimension(oldMeasureWidth, oldMeasureHeight);
            }
        }
        view.getViewWrapper().getViewExt().setKeepMeasureSpec(false);
    }

    private void forceAdjustWXRecyclerView(View view) {
        int currentScenario;
        boolean isSubToMain;
        if (view != null && FlexibleWindowManager.getInstance().hasFSDFeature()) {
            ViewRootImpl viewRoot = view.getViewRootImpl();
            int displayId = viewRoot != null ? viewRoot.getDisplayId() : -1;
            Configuration currentConfig = view.getResources().getConfiguration();
            boolean z = false;
            if (currentConfig == null) {
                currentScenario = 0;
            } else {
                currentScenario = currentConfig.getOplusExtraConfiguration().getScenario();
            }
            Rect appBounds = currentConfig != null ? currentConfig.windowConfiguration.getAppBounds() : null;
            int lastScenario = sLastReportedConfig.getOplusExtraConfiguration().getScenario();
            if (currentScenario != 0 || lastScenario != 3) {
                isSubToMain = false;
            } else {
                isSubToMain = true;
            }
            if (WX_LAUNCHER_ACTIVITY.equals(obtainActivityInfoFromViewRoot(viewRoot)) && view.getClass().getName().contains(WX_RECYCLERVIEW) && view.getLayoutParams().width == -1) {
                z = true;
            }
            boolean isWXRecyclerView = z;
            if (displayId == 0 && isSubToMain && isWXRecyclerView && appBounds != null && view.mMeasuredWidth != appBounds.width()) {
                Slog.d(TAG, "forceAdjustWXRecyclerView and view is: " + view + " view.mMeasuredWidth is: " + view.mMeasuredWidth + " appBounds.width() is: " + appBounds.width());
                view.setMeasuredDimension(appBounds.width(), view.mMeasuredHeight);
            }
        }
    }

    private boolean isFlexibleSubDisplayScenario(Configuration configuration) {
        return configuration != null && configuration.getOplusExtraConfiguration().getScenario() == 3;
    }

    private String obtainActivityInfoFromViewRoot(ViewRootImpl viewRoot) {
        if (viewRoot != null && (viewRoot.getView() instanceof DecorView)) {
            DecorView decorView = viewRoot.getView();
            Context windowContext = decorView.getWrapper().getWindow().getContext();
            if (windowContext instanceof Activity) {
                return ((Activity) windowContext).getComponentName().flattenToShortString();
            }
            return null;
        }
        return null;
    }
}
