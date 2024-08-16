package com.oplus.splitscreen;

import android.app.Activity;
import android.app.OplusActivityTaskManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import android.view.RemoteAnimationDefinition;
import android.view.SurfaceControl;
import com.oplus.app.IActivityMultiWindowAllowanceObserver;
import com.oplus.app.SplitScreenParams;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusSplitScreenManagerInternal {
    public static final int FIRST_OPLUS_EXIT_REASON_CODE = 200;
    public static final int OPLUS_EXIT_REASON_APP_REQUEST = 200;
    public static final int OPLUS_EXIT_REASON_CONTROL_BAR_MENU_MAXIMIZE = 201;
    private static final String PKG_SYSTEMUI = "com.android.systemui";
    private static final String TAG = "OplusSplitScreenManager";
    private static IOplusSplitScreenSession sSplitScreenSession;

    private OplusSplitScreenManagerInternal() {
        sSplitScreenSession = getSplitScreenSession();
    }

    public static OplusSplitScreenManagerInternal getInstance() {
        return LazyHolder.INSTANCE;
    }

    private IOplusSplitScreenSession getSplitScreenSession() {
        if (sSplitScreenSession == null) {
            try {
                sSplitScreenSession = OplusActivityTaskManager.getInstance().getSplitScreenSession();
            } catch (RemoteException e) {
                Log.e(TAG, "getSplitScreenSession error");
            }
        }
        return sSplitScreenSession;
    }

    private boolean checkCaller(Context context, String pkg) {
        if (context == null || !pkg.equals(context.getPackageName())) {
            return false;
        }
        return true;
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

    public List<String> getRecentUsedAppWithMultiApp(int maxNum, long period, boolean supportSplitScreen, List<String> exclude) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.getRecentUsedAppWithMultiApp(maxNum, period, supportSplitScreen, exclude);
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "getRecentUsedAppWithMultiApp error");
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

    public void notifyFoldUpdatingComplete() {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.notifyFoldUpdatingComplete();
            }
        } catch (Exception e) {
            Log.e(TAG, "notifyFoldUpdatingComplete error");
        }
    }

    public void setOverrideRemoteAnimations(RemoteAnimationDefinition definition) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.setOverrideRemoteAnimations(definition);
            }
        } catch (Exception e) {
            Log.e(TAG, "setOverrideRemoteAnimations error");
        }
    }

    public void setSplitTasksState(int taskIdf, int taskIds, boolean addSplitPair) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.setSplitTasksState(taskIdf, taskIds, addSplitPair);
            }
        } catch (Exception e) {
            Log.e(TAG, "setSplitTasksState error");
        }
    }

    public boolean isTopActivityFinishDraw(int taskId) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.isTopActivityFinishDraw(taskId);
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "isTopActivityFinishDraw error");
            return false;
        }
    }

    public SurfaceControl getWallpaperDisplayAreaSurface() {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.getWallpaperDisplayAreaSurface();
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "getWallpaperDisplayAreaSurface error");
            return null;
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

    public Map<String, Rect> getCurrentEmbeddedRects(int displayId) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.getCurrentEmbeddedRects(displayId);
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "getCurrentEmbeddedRects error");
            return null;
        }
    }

    public Map<String, Rect> calculateThreeLayoutRects(int displayId, Intent intent, int userId) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.calculateThreeLayoutRects(displayId, intent, userId);
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "calculateThreeLayoutRects error");
            return null;
        }
    }

    public Map<String, Rect> calculateReplaceLayoutRects(int displayId, Intent intent, int userId, boolean isleftOrTop) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.calculateReplaceLayoutRects(displayId, intent, userId, isleftOrTop);
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "calculateReplaceLayoutRects error");
            return null;
        }
    }

    public Rect getCurrentRectForTask(int taskId, int display) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                return session.getCurrentRectForTask(taskId, display);
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "getCurrentRectForTask error");
            return null;
        }
    }

    public void startThreeSplitFromNormal(int taskId, Intent intent, int userId) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.startThreeSplitFromNormal(taskId, intent, userId);
            }
        } catch (Exception e) {
            Log.e(TAG, "startThreeSplitFromNormal error");
        }
    }

    public void startReplaceSplitWhenNormal(int taskId, Intent intent, int userId, int index) {
        try {
            IOplusSplitScreenSession session = getSplitScreenSession();
            if (session != null) {
                session.startReplaceSplitWhenNormal(taskId, intent, userId, index);
            }
        } catch (Exception e) {
            Log.e(TAG, "startReplaceSplitWhenNormal error");
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

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final OplusSplitScreenManagerInternal INSTANCE = new OplusSplitScreenManagerInternal();

        private LazyHolder() {
        }
    }
}
