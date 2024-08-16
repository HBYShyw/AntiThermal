package com.oplus.splitscreen;

import android.R;
import android.app.Activity;
import android.app.AppGlobals;
import android.app.OplusActivityTaskManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import com.oplus.app.IActivityMultiWindowAllowanceObserver;
import com.oplus.app.IOplusSplitScreenObserver;
import com.oplus.app.SplitScreenParams;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oplus.util.OplusStatistics;

/* loaded from: classes.dex */
public class OplusSplitScreenManager {
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final int DIVIDER_INSETS_FOR_FOLDER = 21;
    public static final String EVENT_DISMISS_SPLIT_SCREEN = "dismissSplitScreen";
    private static final String EVENT_ID_SPLIT_SCREEN_LAUNCH = "split_screen_launch";
    public static final String EVENT_SPLIT_SCREEN_EXITING = "splitScreenExiting";
    public static final String EVENT_SPLIT_SCREEN_MINIMIZED_CHANGED = "splitScreenMinimizedChange";
    public static final String EVENT_SPLIT_SCREEN_MODE_CHANGED = "splitScreenModeChange";
    public static final int FIRST_OPLUS_EXIT_REASON_CODE = 200;
    private static final String FORBID_SPLITSCREEN_FEATURE = "oplus.customize.splitscreen.disable";
    public static final String KEY_DISMISS_SPLIT_SCREEN_TYPE = "dismissSplitScreenType";
    public static final String KEY_IS_EXITING = "isSplitExiting";
    public static final String KEY_IS_IN_SPLIT_SCREEN_MODE = "isInSplitScreenMode";
    public static final String KEY_IS_MINIMIZED = "isMinimized";
    private static final String KEY_LAUNCH_STYLE = "start_style";
    public static final String KEY_POCKET_SPLIT_SCREEN_TYPE = "pocketSplitScreenType";
    public static final int LAUNCH_AREA_BOTTOM = 4;
    public static final int LAUNCH_AREA_INVALID = -1;
    public static final int LAUNCH_AREA_LEFT = 1;
    public static final int LAUNCH_AREA_RIGHT = 3;
    public static final int LAUNCH_AREA_TOP = 2;
    public static final int OPLUS_EXIT_REASON_APP_REQUEST = 200;
    public static final int OPLUS_EXIT_REASON_CONTROL_BAR_MENU_MAXIMIZE = 201;
    private static final String PKG_EXSERVICEUI = "com.oplus.exserviceui";
    private static final String PKG_SYSTEMUI = "com.android.systemui";
    private static final String SETTINGS_FORBID_SPLITSCREEN = "forbid_splitscreen_by_ep";
    public static final int SPLIT_LEFT_OR_TOP_POSITION = 0;
    public static final int SPLIT_RIGHT_OR_BOTTOM_POSITION = 1;
    private static final String SPLIT_SCREEN_APPID = "20232";
    public static final int SPLIT_SCREEN_FROM_BREENO = 5;
    public static final int SPLIT_SCREEN_FROM_FLOAT_ASSISTANT = 4;
    public static final int SPLIT_SCREEN_FROM_MENU = 2;
    public static final int SPLIT_SCREEN_FROM_NONE = -1;
    public static final int SPLIT_SCREEN_FROM_RECENT = 3;
    public static final int SPLIT_SCREEN_FROM_SERVICE = 1;
    private static final String SPLIT_SCREEN_STATISTIC_ID = "20232001";
    public static final int STATE_APP_NOT_SUPPORT = 1006;
    public static final int STATE_BLACK_LIST = 1004;
    public static final int STATE_CHILDREN_MODE = 1005;
    public static final int STATE_FORBID_SPECIAL_APP = 1008;
    public static final int STATE_FORCE_FULLSCREEN = 1007;
    public static final int STATE_INVALID = 1000;
    public static final int STATE_SINGLE_HAND = 1003;
    public static final int STATE_SNAPSHOT = 1002;
    public static final int STATE_SUPPORT = 1001;
    private static final String TAG = "OplusSplitScreenManager";
    private static volatile OplusSplitScreenManager sInstance;
    private static IOplusSplitScreenSession sSplitScreenSession;
    private Boolean mHasColorSplitFeature = null;
    private Boolean mHasLargeScreenFeature = null;
    private OplusActivityTaskManager mOAms;

    private OplusSplitScreenManager() {
        this.mOAms = null;
        this.mOAms = OplusActivityTaskManager.getInstance();
    }

    public static OplusSplitScreenManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusSplitScreenManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusSplitScreenManager();
                }
            }
        }
        return sInstance;
    }

    public int getSplitScreenState(Intent intent) {
        if (hasForbidScreenScreenFeature()) {
            Log.i(TAG, "getSplitScreenState is disabled for enterprise order");
            return 1000;
        }
        if (isEnterpriseDisableSplitScreen()) {
            Log.i(TAG, "getSplitScreenState isEnterpriseDisableSplitScreen");
            return 1000;
        }
        if (intent == null) {
            throw new IllegalArgumentException("getSplitScreenState intent=null");
        }
        if (DEBUG) {
            Log.i(TAG, "getSplitScreenState");
        }
        try {
            return this.mOAms.getSplitScreenState(intent);
        } catch (RemoteException e) {
            Log.e(TAG, "getSplitScreenState remoteException ");
            e.printStackTrace();
            return 1000;
        }
    }

    public int getTopAppSplitScreenState() {
        if (hasForbidScreenScreenFeature()) {
            Log.i(TAG, "getTopAppSplitScreenState is disabled for enterprise order");
            return 1000;
        }
        try {
            return this.mOAms.getSplitScreenState(null);
        } catch (RemoteException e) {
            Log.e(TAG, "getTopAppSplitScreenState remoteException ");
            e.printStackTrace();
            return 1000;
        }
    }

    @Deprecated
    public void swapDockedFullscreenStack() {
    }

    public boolean splitScreenForTopApp(int type) {
        if (hasForbidScreenScreenFeature()) {
            Log.i(TAG, "splitScreenForTopApp is disabled for enterprise order");
            return false;
        }
        if (isEnterpriseDisableSplitScreen()) {
            Log.i(TAG, "splitScreenForTopApp isEnterpriseDisableSplitScreen");
            return false;
        }
        if (type == 3) {
            throw new IllegalArgumentException("splitScreenForTopApp type is abnormal");
        }
        if (DEBUG) {
            Log.i(TAG, "splitScreenForTopApp type:" + type);
        }
        try {
            return this.mOAms.splitScreenForTopApp(type);
        } catch (RemoteException e) {
            Log.e(TAG, "splitScreenForTopApp RemoteException");
            return false;
        }
    }

    public boolean splitScreenForRecentTasks(int taskId) {
        if (hasForbidScreenScreenFeature()) {
            Log.i(TAG, "splitScreenForRecentTasks is disabled for enterprise order");
            return false;
        }
        if (isEnterpriseDisableSplitScreen()) {
            Log.i(TAG, "splitScreenForRecentTasks isEnterpriseDisableSplitScreen");
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "splitScreenForRecentTasks taskId:" + taskId);
        }
        try {
            return this.mOAms.splitScreenForRecentTasks(taskId);
        } catch (RemoteException e) {
            Log.e(TAG, "splitScreenForRecentTasks RemoteException");
            return false;
        }
    }

    public void onSplitScreenLaunched(int type) {
        HashMap<String, String> logMap = new HashMap<>();
        logMap.put(KEY_LAUNCH_STYLE, type + "");
        OplusStatistics.onCommon((Context) AppGlobals.getInitialApplication(), SPLIT_SCREEN_APPID, SPLIT_SCREEN_STATISTIC_ID, EVENT_ID_SPLIT_SCREEN_LAUNCH, (Map<String, String>) logMap, false);
        if (DEBUG) {
            Log.i(TAG, "onSplitScreenLaunched logMap:" + logMap);
        }
    }

    public int splitScreenForEdgePanel(Intent intent, int userId) {
        if (hasForbidScreenScreenFeature()) {
            Log.i(TAG, "splitScreenForEdgePanel is disabled for enterprise order");
            return 0;
        }
        if (isEnterpriseDisableSplitScreen()) {
            Log.i(TAG, "splitScreenForEdgePanel isEnterpriseDisableSplitScreen");
            return 0;
        }
        if (intent == null) {
            throw new IllegalArgumentException("getSplitScreenState intent=null");
        }
        if (DEBUG) {
            Log.i(TAG, "splitWindowForTopApp intent:" + intent);
        }
        try {
            return this.mOAms.splitScreenForEdgePanel(intent, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "splitScreenForEdgePanel failed");
            return 0;
        }
    }

    public int getVersion() {
        return 1;
    }

    public int setTaskWindowingModeSplitScreen(int taskId) {
        if (DEBUG) {
            Log.d(TAG, "setTaskWindowingModeSplitScreen, taskId = " + taskId);
        }
        try {
            return this.mOAms.setTaskWindowingModeSplitScreen(taskId);
        } catch (RemoteException e) {
            Log.e(TAG, "setTaskWindowingModeSplitScreen RemoteException");
            return 0;
        }
    }

    public boolean setSplitScreenObserver(IOplusSplitScreenObserver observer) {
        if (observer == null) {
            Log.e(TAG, "SystemUi setSplitScreenObserver error, observer is null");
            return false;
        }
        if (DEBUG) {
            Log.d(TAG, "SystemUi setSplitScreenObserver");
        }
        try {
            return this.mOAms.setSplitScreenObserver(observer);
        } catch (RemoteException e) {
            Log.e(TAG, "SystemUi setSplitScreenObserver RemoteException");
            return false;
        }
    }

    public void notifySplitScreenStateChanged(String event, Bundle bundle, boolean broadcast) {
        if (DEBUG) {
            Log.d(TAG, "SystemUi notifyStateChanged");
        }
        try {
            this.mOAms.notifySplitScreenStateChanged(event, bundle, broadcast);
        } catch (RemoteException e) {
            Log.e(TAG, "notifyStateChanged RemoteException");
        }
    }

    public boolean isInSplitScreenMode() {
        try {
            return this.mOAms.isInSplitScreenMode();
        } catch (Exception e) {
            Log.e(TAG, "isInSplitScreenMode RemoteException");
            return false;
        }
    }

    public boolean dismissSplitScreenMode(int type) {
        if (DEBUG) {
            Log.d(TAG, "dismissSplitScreenMode type = " + type);
        }
        try {
            return this.mOAms.dismissSplitScreenMode(type);
        } catch (RemoteException e) {
            Log.e(TAG, "dismissSplitScreenMode RemoteException");
            return false;
        }
    }

    public boolean registerSplitScreenObserver(IOplusSplitScreenObserver observer) {
        if (observer == null) {
            Log.e(TAG, "registerSplitScreenObserver error, observer is null");
            return false;
        }
        try {
            return this.mOAms.registerSplitScreenObserver(observer.hashCode(), observer);
        } catch (RemoteException e) {
            Log.e(TAG, "registerSplitScreenObserver failed");
            return false;
        }
    }

    public boolean unregisterSplitScreenObserver(IOplusSplitScreenObserver observer) {
        if (observer == null) {
            Log.e(TAG, "unregisterSplitScreenObserver error, observer is null");
            return false;
        }
        try {
            return this.mOAms.unregisterSplitScreenObserver(observer.hashCode(), observer);
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterSplitScreenObserver failed");
            return false;
        }
    }

    public Bundle getSplitScreenStatus(String event) {
        if (DEBUG) {
            Log.d(TAG, "getSplitScreenStatus event = " + event);
        }
        try {
            return this.mOAms.getSplitScreenStatus(event);
        } catch (RemoteException e) {
            Log.e(TAG, "getSplitScreenCurrentState failed");
            return null;
        }
    }

    private boolean hasForbidScreenScreenFeature() {
        try {
            return AppGlobals.getPackageManager().hasSystemFeature(FORBID_SPLITSCREEN_FEATURE, 0);
        } catch (RemoteException e) {
            Log.e(TAG, "hasForbidScreenScreenFeature RemoteException");
            return false;
        }
    }

    private boolean isEnterpriseDisableSplitScreen() {
        try {
            return Settings.Secure.getInt(AppGlobals.getInitialApplication().getContentResolver(), SETTINGS_FORBID_SPLITSCREEN, 0) == 1;
        } catch (Exception e) {
            Log.e(TAG, "isEnterpriseDisableSplitScreen error");
            return false;
        }
    }

    public boolean splitScreenForEdgePanel(Intent intent, boolean launchToPrimary, int launchArea) {
        if (hasForbidScreenScreenFeature()) {
            Log.i(TAG, "splitScreenForEdgePanel is disabled for enterprise order");
            return false;
        }
        if (isEnterpriseDisableSplitScreen()) {
            Log.i(TAG, "splitScreenForEdgePanel isEnterpriseDisableSplitScreen");
            return false;
        }
        if (intent == null) {
            Log.e(TAG, "splitScreenForEdgePanel error, intent is null");
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "splitScreenForEdgePanel intent:" + intent + ", launchToPrimary = " + launchToPrimary);
        }
        try {
            return this.mOAms.splitScreenForEdgePanelExt(intent, launchToPrimary, launchArea);
        } catch (Exception e) {
            Log.e(TAG, "splitScreenForEdgePanel failed");
            return false;
        }
    }

    public boolean splitScreenForApplication(Context context, Intent intent, int position) {
        if (hasForbidScreenScreenFeature()) {
            Log.i(TAG, "splitScreenForApplication is disabled for enterprise order");
            return false;
        }
        if (isEnterpriseDisableSplitScreen()) {
            Log.i(TAG, "splitScreenForApplication isEnterpriseDisableSplitScreen");
            return false;
        }
        if (intent == null || context == null) {
            Log.e(TAG, "splitScreenForApplication error, intent is null");
            return false;
        }
        try {
            return this.mOAms.splitScreenForApplication(context, intent, position);
        } catch (Exception e) {
            Log.e(TAG, "splitScreenForApplication failed");
            return false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001d, code lost:
    
        if (android.os.SystemProperties.getBoolean("persist.sys.large_screen_split_feature_enable", true) != false) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean hasLargeScreenFeature() {
        if (this.mHasLargeScreenFeature == null) {
            boolean z = (isFoldDevice() && !isDragonflyDevice()) || isTabletDevice();
            this.mHasLargeScreenFeature = Boolean.valueOf(z);
        }
        return this.mHasLargeScreenFeature.booleanValue();
    }

    public boolean isDragonflyDevice() {
        return OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_FOLD_REMAP_DISPLAY_DISABLED);
    }

    public boolean hasFolderFeature() {
        return hasLargeScreenFeature();
    }

    public boolean isFolderInnerScreen() {
        try {
            return this.mOAms.isFolderInnerScreen();
        } catch (RemoteException e) {
            Log.e(TAG, "isFolderInnerScreen error");
            return false;
        }
    }

    public Rect getMinimizedBounds(int dockedSide) {
        try {
            return this.mOAms.getMinimizedBounds(dockedSide);
        } catch (RemoteException e) {
            Log.e(TAG, "getMinimizedBounds error");
            return new Rect();
        }
    }

    public Bundle getLeftRightBoundsForIme() {
        try {
            return this.mOAms.getLeftRightBoundsForIme();
        } catch (RemoteException e) {
            Log.e(TAG, "getLeftRightBoundsForIme error");
            return new Bundle();
        }
    }

    public boolean hasNewDragSplitFeature() {
        return hasColorSplitFeature();
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0017, code lost:
    
        if (android.os.SystemProperties.getBoolean("persist.sys.color_split_feature_enable", true) != false) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean hasColorSplitFeature() {
        if (this.mHasColorSplitFeature == null) {
            boolean z = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.OPLUS_FEATURE_COLOR_SPLIT_FEATURE);
            this.mHasColorSplitFeature = Boolean.valueOf(z);
        }
        return this.mHasColorSplitFeature.booleanValue();
    }

    public boolean isFoldDevice() {
        return OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_FOLD);
    }

    public boolean isTabletDevice() {
        return OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_TABLET);
    }

    public int getDividerInsets(Resources res) {
        if (hasColorSplitFeature()) {
            return (int) (res.getDisplayMetrics().density * 21.0f);
        }
        return res.getDimensionPixelSize(R.dimen.harmful_app_message_padding_left);
    }

    private IOplusSplitScreenSession getSplitScreenSession() {
        IOplusSplitScreenSession iOplusSplitScreenSession;
        synchronized (OplusSplitScreenManager.class) {
            if (sSplitScreenSession == null) {
                try {
                    sSplitScreenSession = this.mOAms.getSplitScreenSession();
                } catch (RemoteException e) {
                    Log.e(TAG, "getSplitScreenSession error");
                }
            }
            iOplusSplitScreenSession = sSplitScreenSession;
        }
        return iOplusSplitScreenSession;
    }

    public void registerStackDivider(Context context, IOplusStackDividerConnection conn) {
        if (!checkCaller(context, "com.android.systemui")) {
            return;
        }
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.registerStackDivider(conn);
            }
        } catch (Exception e) {
            Log.e(TAG, "registerStackDivider error");
        }
    }

    public void setSplitControlBarRegion(Rect region, boolean isPrimary) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.setSplitControlBarRegion(region, isPrimary);
            }
        } catch (Exception e) {
            Log.e(TAG, "setSplitControlBarRegion error");
        }
    }

    public void notifySplitRootTaskId(int root, int primary, int secondary) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.notifySplitRootTaskId(root, primary, secondary);
            }
        } catch (Exception e) {
            Log.e(TAG, "notifySplitRootTaskId error");
        }
    }

    public void setSplitRootTaskAlwaysOnTop(boolean onTop) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.setSplitRootTaskAlwaysOnTop(onTop);
            }
        } catch (Exception e) {
            Log.e(TAG, "setSplitRootTaskAlwaysOnTop error");
        }
    }

    public boolean moveChildrenTaskToBack(int rootTaskId) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.moveChildrenTaskToBack(rootTaskId);
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "moveChildrenTaskToBack error");
            return false;
        }
    }

    public List<String> getRecentUsedApp(int maxNum, long period, boolean supportSplitScreen, List<String> exclude) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.getRecentUsedApp(maxNum, period, supportSplitScreen, exclude);
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "getRecentUsedApp error");
            return null;
        }
    }

    public void setSplitRequestedOrientation(int requestedOrientation) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.setSplitRequestedOrientation(requestedOrientation);
            }
        } catch (Exception e) {
            Log.e(TAG, "setSplitRequestedOrientation error");
        }
    }

    public boolean requestSwitchToSplitScreen(Activity requestActivity, SplitScreenParams params) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.requestSwitchToSplitScreen(requestActivity.getActivityToken(), params);
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "requestSwitchToSplitScreen error");
            return false;
        }
    }

    public boolean requestSwitchToFullScreen(Activity requestActivity) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.requestSwitchToFullScreen(requestActivity.getActivityToken());
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "requestSwitchToFullScreen error");
            return false;
        }
    }

    public void registerActivityMultiWindowAllowanceObserver(Activity activity, IActivityMultiWindowAllowanceObserver observer) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.registerActivityMultiWindowAllowanceObserver(activity.getActivityToken(), observer.asBinder());
            }
        } catch (Exception e) {
            Log.e(TAG, "registerActivityMultiWindowAllowanceObserver error");
        }
    }

    public void unregisterActivityMultiWindowAllowanceObserver(Activity activity, IActivityMultiWindowAllowanceObserver observer) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.unregisterActivityMultiWindowAllowanceObserver(activity.getActivityToken(), observer.asBinder());
            }
        } catch (Exception e) {
            Log.e(TAG, "unregisterActivityMultiWindowAllowanceObserver error");
        }
    }

    public void removeSelfSplitTaskIfNeed(int toTopTaskId, int toBottomTaskId) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.removeSelfSplitTaskIfNeed(toTopTaskId, toBottomTaskId);
            }
        } catch (Exception e) {
            Log.e(TAG, "removeSelfSplitTaskIfNeed error");
        }
    }

    public void startZoomWindowFromSplit(int taskId, Rect bounds, float scale) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.startZoomWindowFromSplit(taskId, bounds, scale);
            }
        } catch (Exception e) {
            Log.e(TAG, "startZoomWindowFromSplit error");
        }
    }

    public void maintainSplitToZoomTaskState(int taskId, boolean maintain) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.maintainSplitToZoomTaskState(taskId, maintain);
            }
        } catch (Exception e) {
            Log.e(TAG, "maintainSplitToZoomTaskState error");
        }
    }

    public int getLastLayerForTask(int taskId) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.getLastLayerForTask(taskId);
            }
            return -1;
        } catch (Exception e) {
            Log.e(TAG, "getLastLayerForTask error");
            return -1;
        }
    }

    public void hideTargetSplashScreen(int taskId) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.hideTargetSplashScreen(taskId);
            }
        } catch (Exception e) {
            Log.e(TAG, "hideTargetSplashScreen error");
        }
    }

    private boolean checkCaller(Context context, String pkg) {
        if (context == null || !pkg.equals(context.getPackageName())) {
            return false;
        }
        return true;
    }

    public void updateEnterNormalType(int type) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.updateEnterNormalType(type);
            }
        } catch (Exception e) {
            Log.e(TAG, "updateEnterNormalType error");
        }
    }
}
