package com.oplus.flexiblewindow;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.app.Application;
import android.app.OplusActivityTaskManager;
import android.app.TaskInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback;
import com.oplus.util.OplusTypeCastingHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class FlexibleWindowManager {
    private static final String ACTION_POCKET_STUDIO = "oplus.intent.action.pocket.studio.canvas";
    public static final String AVOID_MOVE_TO_FRONT = "android.activity.avoidMoveToFront";
    public static final int BIG_SCREEN_SMALLEST_SWDP = 600;
    public static final int FLEXIBLE_ACTIVITY_POSITION_INVALID = -1;
    public static final int FLEXIBLE_ACTIVITY_POSITION_LEFT = 1;
    public static final int FLEXIBLE_ACTIVITY_POSITION_RIGHT = 2;
    public static final int FLEXIBLE_POCKET_STUDIO_UNSUPPORTED = 0;
    public static final int FLEXIBLE_RESIZE_MODE_COMPAT = 2;
    public static final int FLEXIBLE_RESIZE_MODE_COMPAT_FULLSCREEN = 3;
    public static final int FLEXIBLE_RESIZE_MODE_SPLITSCREEN = 1;
    private static final String FORBID_SPLITSCREEN_FEATURE = "oplus.customize.splitscreen.disable";
    public static final String KEY_ACTIVITY_NO_ANIM = "androidx.flexible.activityNoAnimation";
    public static final String KEY_CANVAS_FOCUS_INDEX = "androidx.flexible.focusIndex";
    public static final String KEY_CANVAS_INTENT_LIST = "androidx.flexible.intentList";
    public static final String KEY_CANVAS_LAYOUT_ORIENTATION = "androidx.flexible.layoutOrientation";
    public static final String KEY_CANVAS_TASK_ID_LIST = "androidx.flexible.taskIdList";
    public static final String KEY_CANVAS_USER_ID = "androidx.flexible.userId";
    public static final String KEY_CANVAS_USER_ID_LIST = "androidx.flexible.userIdList";
    public static final String KEY_FLEXIBLE_ACTIVITY_DESCENDANT = "androidx.activity.FlexibleDescendant";
    public static final String KEY_FLEXIBLE_ACTIVITY_POSITION = "androidx.activity.FlexiblePosition";
    public static final String KEY_FLEXIBLE_COMPAT_RATIO = "androidx.flexible.CompatRatio";
    public static final String KEY_FLEXIBLE_LAUNCH_BOUNDS = "androidx.flexible.LaunchBounds";
    public static final String KEY_FLEXIBLE_LAUNCH_MAX_BOUNDS = "androidx.flexible.LaunchMaxBounds";
    public static final String KEY_FLEXIBLE_LAUNCH_PREFERRED_BOUNDS = "androidx.flexible.LaunchPreferredBounds";
    public static final String KEY_FLEXIBLE_RESIZE_MODE = "androidx.flexible.ResizeMode";
    public static final String KEY_FLEXIBLE_SCREEN_ORIENTATION = "androidx.flexible.ScreenOrientation";
    public static final String KEY_FLEXIBLE_STABLE_RECT = "androidx.flexible.StableRect";
    public static final String KEY_FLEXIBLE_START_ACTIVITY = "androidx.activity.StartFlexibleActivity";
    public static final String KEY_FLEXIBLE_WINDOW_METRICS_BOUNDS = "androidx.flexible.WindowMetricsBounds";
    public static final String KEY_LAUNCH_CONTAINER_TASK_ID = "androidx.activity.LaunchContainerTaskId";
    public static final String KEY_LAUNCH_EMBEDDED_TASK_ID = "androidx.activity.LaunchEmbeddedTaskId";
    public static final String KEY_LAUNCH_EXIT_MINIMIZED = "androidx.flexible.exitMinimized";
    public static final String KEY_LAUNCH_TASK_ADJUST_INPUT_METHOD = "androidx.activity.AdjustInputMethod";
    public static final String KEY_LAUNCH_TASK_CAPTION_NAME = "androidx.activity.CaptionName";
    public static final String KEY_LAUNCH_TASK_CORNER_RADIUS = "androidx.activity.LaunchCornerRadius";
    public static final String KEY_LAUNCH_TASK_EMBEDDED = "androidx.activity.LaunchEmbedded";
    public static final String KEY_LAUNCH_TASK_FOCUS_CHANGE_WITH_NONFLEXIBLE = "androidx.activity.FocusChangeWithNonFlexible";
    public static final String KEY_LAUNCH_TASK_HAS_CAPTION = "androidx.activity.HasCaption";
    public static final String KEY_LAUNCH_TASK_IGNORE_SYSTEM_BAR = "androidx.activity.IgnoreSystemBar";
    public static final String KEY_LAUNCH_TASK_MAINTAIN_TASK_STATE = "androidx.activity.MaintainTaskState";
    public static final String KEY_LAUNCH_TASK_MAX_SCALE = "androidx.activity.MaxScale";
    public static final String KEY_LAUNCH_TASK_MIN_SCALE = "androidx.activity.MinScale";
    public static final String KEY_LAUNCH_TASK_NEED_DAFAULT_ANIMATION = "androidx.activity.NeedDefaultAnimation";
    public static final String KEY_LAUNCH_TASK_RESIZABLE = "androidx.activity.LaunchResizable";
    public static final String KEY_LAUNCH_TASK_SCALE = "androidx.activity.LaunchScale";
    public static final String KEY_LAUNCH_TASK_SCENARIO = "androidx.activity.LaunchScenario";
    public static final String KEY_LAUNCH_TASK_SHADOW_RADIUS = "androidx.activity.LaunchShadowRadius";
    public static final String KEY_LAUNCH_TASK_SIMULATED_DENSITY = "androidx.activity.SimulatedDensity";
    public static final String KEY_LAUNCH_TASK_SIMULATED_WIDTH = "androidx.activity.SimulatedWidth";
    public static final String KEY_LAUNCH_TASK_SUPPORT_RATIOS = "androidx.activity.SupportRatios";
    public static final String KEY_SUB_DISPLAY_START_FROM_EXTRA_LAUNCHER = "subDisplay.activity.StartFromExtraLauncher";
    public static final String KEY_SUPPORT_SPLIT_SCREEN_WINDOWING_MODE = "androidx.activity.SupportsSplitScreenWindowingMode";
    public static final String KEY_TASK_FRAME = "androidx.flexible.taskFrame";
    public static final String KEY_TASK_HANDLE_FRAME = "androidx.flexible.taskHandleFrame";
    public static final int LAUNCH_SCENARIO_CANVAS = 2;
    public static final int LAUNCH_SCENARIO_DEFAULT = 0;
    public static final int LAUNCH_SCENARIO_FLEXIBLE = 1;
    public static final int LAUNCH_SCENARIO_SUB_DISPLAY = 3;
    public static final int LAUNCH_TASK_MARGIN = 8;
    public static final int LAYOUT_ORIENTATION_HORIZONTAL = 2;
    public static final int LAYOUT_ORIENTATION_UNSET = 0;
    public static final int LAYOUT_ORIENTATION_VERTICAL = 1;
    public static final int MARGIN_GLOBAL_MODE = 16;
    public static final int MARGIN_SECURE_RESERVE = 56;
    private static final String SETTINGS_FORBID_SPLITSCREEN = "forbid_splitscreen_by_ep";
    private static final String TAG = "FlexibleWindowManager";
    public static final int THREE_SPLIT_PADDING = 10;
    private static volatile FlexibleWindowManager sInstance;
    private boolean mHasFSDFeature;
    private boolean mHasPocketStudioFeature;
    private boolean mIsFlipDevice;
    private boolean mIsFoldDevice;
    private final ArrayMap<IEmbeddedWindowCallback, EmbeddedWindowContainerCallbackDelegate> mCallbackMap = new ArrayMap<>();
    private OplusActivityTaskManager mOAtms = OplusActivityTaskManager.getInstance();

    /* loaded from: classes.dex */
    public interface IEmbeddedWindowCallback {
        void adjustFlexiblePositionForIme(boolean z, int i);

        void autoScaleToOriginPosition();

        void notifyCanvasContainerReleaseTasks();

        void notifyDragStart();

        void notifyFlexibleTaskVanish(int i);

        void notifyFlexibleTaskVanish(ActivityManager.RunningTaskInfo runningTaskInfo);

        void notifyTaskToFullScreen(int i);

        void onCanvasPositionChanged();

        void onRecentsAnimationExecuting(boolean z, int i);

        void startReplaceSplitWhenNormalSplit(int i, Intent intent, int i2, int i3);

        void startThreeSplitFromNormalSplit(Intent intent, int i);

        default void notifyTaskRectOrientationChange(ActivityManager.RunningTaskInfo taskInfo, Rect rect) {
        }

        default void notifyTaskEmbeddedStatus(ActivityManager.RunningTaskInfo taskInfo, boolean isBind) {
        }
    }

    /* loaded from: classes.dex */
    private class EmbeddedWindowContainerCallbackDelegate extends IEmbeddedWindowContainerCallback.Stub {
        private final IEmbeddedWindowCallback mCallBack;

        public EmbeddedWindowContainerCallbackDelegate(IEmbeddedWindowCallback callBack) {
            this.mCallBack = callBack;
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void adjustFlexiblePositionForIme(boolean imeVisible, int offset) {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.adjustFlexiblePositionForIme(imeVisible, offset);
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void onRecentsAnimationExecuting(boolean executing, int reorderMode) {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.onRecentsAnimationExecuting(executing, reorderMode);
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void autoScaleToOriginPosition() {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.autoScaleToOriginPosition();
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void startThreeSplitFromNormalSplit(Intent intent, int userId) {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.startThreeSplitFromNormalSplit(intent, userId);
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void startReplaceSplitWhenNormalSplit(int taskId, Intent intent, int userId, int index) {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.startReplaceSplitWhenNormalSplit(taskId, intent, userId, index);
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyFlexibleTaskVanish(ActivityManager.RunningTaskInfo taskInfo) {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.notifyFlexibleTaskVanish(taskInfo);
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyCanvasContainerReleaseTasks() {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.notifyCanvasContainerReleaseTasks();
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void onCanvasPositionChanged() {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.onCanvasPositionChanged();
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyDragStart() {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.notifyDragStart();
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyTaskToFullScreen(int taskId) {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.notifyTaskToFullScreen(taskId);
            }
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyTaskRectOrientationChange(ActivityManager.RunningTaskInfo taskInfo, Rect rect) {
            this.mCallBack.notifyTaskRectOrientationChange(taskInfo, rect);
        }

        @Override // com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback
        public void notifyTaskEmbeddedStatus(ActivityManager.RunningTaskInfo taskInfo, boolean isBind) {
            IEmbeddedWindowCallback iEmbeddedWindowCallback = this.mCallBack;
            if (iEmbeddedWindowCallback != null) {
                iEmbeddedWindowCallback.notifyTaskEmbeddedStatus(taskInfo, isBind);
            }
        }
    }

    private FlexibleWindowManager() {
        OplusFeatureConfigManager featureManager = OplusFeatureConfigManager.getInstance();
        this.mHasPocketStudioFeature = featureManager.hasFeature(IOplusFeatureConfigList.OPLUS_FEATURE_PACKETSTUDIO_SUPPORT);
        this.mIsFoldDevice = featureManager.hasFeature(IOplusFeatureConfigList.FEATURE_FOLD);
        boolean z = true;
        this.mIsFlipDevice = featureManager.hasFeature(IOplusFeatureConfigList.FEATURE_FOLD) && featureManager.hasFeature(IOplusFeatureConfigList.FEATURE_FOLD_REMAP_DISPLAY_DISABLED);
        if ((!this.mIsFoldDevice || !featureManager.hasFeature(IOplusFeatureConfigList.FEATURE_FOLD_REMAP_DISPLAY_DISABLED) || !featureManager.hasFeature(IOplusFeatureConfigList.OPLUS_FEATURE_FLEXIBLE_SUB_DISPLAY_SUPPORT)) && !SystemProperties.getBoolean("sys.debug.scale.sub_display", false)) {
            z = false;
        }
        this.mHasFSDFeature = z;
    }

    public static FlexibleWindowManager getInstance() {
        if (sInstance == null) {
            synchronized (FlexibleWindowManager.class) {
                if (sInstance == null) {
                    sInstance = new FlexibleWindowManager();
                }
            }
        }
        return sInstance;
    }

    public boolean registerFlexibleWindowObserver(IFlexibleWindowObserver observer) {
        try {
            return this.mOAtms.registerFlexibleWindowObserver(observer);
        } catch (RemoteException e) {
            Log.e(TAG, "registerFlexibleWindowObserver remoteException ", e);
            return false;
        }
    }

    public boolean unregisterFlexibleWindowObserver(IFlexibleWindowObserver observer) {
        try {
            return this.mOAtms.unregisterFlexibleWindowObserver(observer);
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterFlexibleWindowObserver remoteException ", e);
            return false;
        }
    }

    public Bundle calculateFlexibleWindowBounds(Intent intent, int appOrientation, int displayId) {
        try {
            if (intent == null || displayId == -1) {
                Log.e(TAG, "no intent or invalid display for calculating bounds");
                return new Bundle();
            }
            return this.mOAtms.calculateFlexibleWindowBounds(intent, appOrientation, displayId);
        } catch (RemoteException e) {
            Log.e(TAG, "calculateFlexibleWindowBounds remoteException ", e);
            return new Bundle();
        }
    }

    public void setInterceptBackPressedOnTaskRoot(WindowContainerToken token, boolean interceptBackPressed) {
        try {
            ActivityTaskManager.getService().getWindowOrganizerController().getTaskOrganizerController().setInterceptBackPressedOnTaskRoot(token, interceptBackPressed);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void applyTransaction(WindowContainerTransaction windowContainerTransaction) {
        try {
            ActivityTaskManager.getService().getWindowOrganizerController().applyTransaction(windowContainerTransaction);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isSupportPocketStudio(Context context) {
        Context applicationContext;
        if (!this.mHasPocketStudioFeature || context == null || (applicationContext = context.getApplicationContext()) == null || applicationContext.getResources() == null || applicationContext.getResources().getConfiguration() == null) {
            return false;
        }
        if (!this.mIsFoldDevice) {
            return true;
        }
        Configuration configuration = applicationContext.getResources().getConfiguration();
        int windowingMode = configuration.windowConfiguration.getWindowingMode();
        if (windowingMode == 1 || windowingMode == 0) {
            return configuration.smallestScreenWidthDp >= 600;
        }
        Display display = ((WindowManager) applicationContext.getSystemService(WindowManager.class)).getDefaultDisplay();
        int height = display.getMode().getPhysicalHeight();
        int width = display.getMode().getPhysicalWidth();
        int smallestScreenWidthDp = (Math.min(height, width) * 160) / configuration.densityDpi;
        return smallestScreenWidthDp >= 600;
    }

    public boolean isInPocketStudio(int displayId) {
        try {
            return this.mOAtms.isInPocketStudio(displayId);
        } catch (RemoteException e) {
            Log.e(TAG, "isInPocketStudio remoteException ", e);
            return false;
        }
    }

    public boolean isInPocketStudioForStandard(int displayId) {
        try {
            return this.mOAtms.isInPocketStudioForStandard(displayId);
        } catch (RemoteException e) {
            Log.e(TAG, "isInPocketStudioForStandard remoteException ", e);
            return false;
        }
    }

    public Map<Integer, Rect> getPocketStudioTaskRegion(int displayId) {
        try {
            return this.mOAtms.getPocketStudioTaskRegion(displayId);
        } catch (RemoteException e) {
            Log.e(TAG, "getPocketStudioTaskRegion remoteException ", e);
            return Collections.emptyMap();
        }
    }

    public void setMinimizedPocketStudio(boolean minimized, int displayId) {
        try {
            this.mOAtms.setMinimizedPocketStudio(minimized, displayId);
        } catch (RemoteException e) {
            Log.e(TAG, "setMinimizedPocketStudio remoteException ", e);
        }
    }

    public boolean isMinimizedPocketStudio(int displayId) {
        try {
            return this.mOAtms.isMinimizedPocketStudio(displayId);
        } catch (RemoteException e) {
            Log.e(TAG, "isMinimizedPocketStudio remoteException ", e);
            return false;
        }
    }

    public boolean isClickAtPocketStudioArea(int displayId, int rowX, int rowY) {
        try {
            return this.mOAtms.isClickAtPocketStudioArea(displayId, rowX, rowY);
        } catch (RemoteException e) {
            Log.e(TAG, "isClickAtPocketStudioArea remoteException ", e);
            return false;
        }
    }

    public void setFlexibleFrame(int taskId, Bundle bundle) {
        try {
            this.mOAtms.setFlexibleFrame(taskId, bundle);
        } catch (RemoteException e) {
            Log.e(TAG, "setFlexibleFrame remoteException ", e);
        }
    }

    public List<ActivityManager.RecentTaskInfo> getRecentEmbeddedTasksForContainer(int containerTaskId) {
        if (containerTaskId == -1) {
            return new ArrayList();
        }
        try {
            return this.mOAtms.getRecentEmbeddedTasksForContainer(containerTaskId);
        } catch (RemoteException e) {
            Log.e(TAG, "getRecentEmbeddedTasksForContainer remoteException ", e);
            return new ArrayList();
        }
    }

    public void notifyEmbeddedTasksChangeFocus(boolean isStateSteady, int containerTaskId) {
        try {
            this.mOAtms.notifyEmbeddedTasksChangeFocus(isStateSteady, containerTaskId);
        } catch (RemoteException e) {
            Log.e(TAG, "notifyEmbeddedTasksChangeFocus remoteException ", e);
        }
    }

    public void notifyFlexibleSplitScreenStateChanged(String event, Bundle bundle, int containerTaskId) {
        try {
            this.mOAtms.notifyFlexibleSplitScreenStateChanged(event, bundle, containerTaskId);
        } catch (RemoteException e) {
            Log.e(TAG, "notifyEmbeddedTasksChangeFocus remoteException ", e);
        }
    }

    public void setEmbeddedContainerTask(int embeddedTaskId, int containerTaskId) {
        try {
            this.mOAtms.setEmbeddedContainerTask(embeddedTaskId, containerTaskId);
        } catch (RemoteException e) {
            Log.e(TAG, "setEmbeddedContainerTask remoteException ", e);
        }
    }

    public void updateTaskVisibility(WindowContainerToken windowContainerToken, int containerTaskId, boolean visible) {
        try {
            this.mOAtms.updateTaskVisibility(windowContainerToken, containerTaskId, visible);
        } catch (RemoteException e) {
            Log.e(TAG, "updateTaskVisibility remoteException ", e);
        }
    }

    public void removeEmbeddedContainerTask(int embeddedTaskId, int containerTaskId) {
        try {
            this.mOAtms.removeEmbeddedContainerTask(embeddedTaskId, containerTaskId);
        } catch (RemoteException e) {
            Log.e(TAG, "removeEmbeddedContainerTask remoteException ", e);
        }
    }

    public void exitFlexibleEmbeddedTask(int embeddedTaskId) {
        try {
            this.mOAtms.exitFlexibleEmbeddedTask(embeddedTaskId);
        } catch (RemoteException e) {
            Log.e(TAG, "removeEmbeddedContainerTask remoteException ", e);
        }
    }

    public void setFlexibleTaskEmbedding(int embeddedTaskId, boolean state) {
        try {
            this.mOAtms.setFlexibleTaskEmbedding(embeddedTaskId, state);
        } catch (RemoteException e) {
            Log.e(TAG, "setFlexibleTaskEmbedding remoteException ", e);
        }
    }

    public void resetFlexibleTask(int embeddedTaskId, boolean needResize, boolean doAnimation) {
        try {
            this.mOAtms.resetFlexibleTask(embeddedTaskId, needResize, doAnimation);
        } catch (RemoteException e) {
            Log.e(TAG, "resetFlexibleTask remoteException ", e);
        }
    }

    public boolean startFlexibleWindowForRecentTasks(int taskId, Rect rect) {
        if (hasForbidScreenScreenFeature()) {
            Log.i(TAG, "flexibleWindowForRecentTasks is disabled for enterprise order");
            return false;
        }
        if (isEnterpriseDisableSplitScreen()) {
            Log.i(TAG, "flexibleWindowForRecentTasks isEnterpriseDisableSplitScreen");
            return false;
        }
        Log.i(TAG, "flexibleWindowForRecentTasks taskId:" + taskId);
        try {
            return this.mOAtms.startFlexibleWindowForRecentTasks(taskId, rect);
        } catch (RemoteException e) {
            Log.e(TAG, "flexibleWindowForRecentTasks RemoteException");
            return false;
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

    public void startActivityInTask(Intent intent, int taskId) {
        if (intent == null || taskId == -1) {
            return;
        }
        try {
            this.mOAtms.startActivityInTask(intent, taskId);
        } catch (RemoteException e) {
            Log.e(TAG, "startActivityInTask RemoteException");
        }
    }

    public int startAnyActivity(Intent intent, Bundle bundle) {
        if (intent == null) {
            return 102;
        }
        try {
            return this.mOAtms.startAnyActivity(intent, bundle);
        } catch (RemoteException e) {
            Log.e(TAG, "startAnyActivity RemoteException");
            return 102;
        }
    }

    private boolean isInAppInnerSplitScreen(int displayId) {
        try {
            return this.mOAtms.isInAppInnerSplitScreen(displayId);
        } catch (RemoteException e) {
            Log.e(TAG, "isInAppInnerSplitScreen RemoteException");
            return false;
        }
    }

    public void registerEmbeddedWindowContainerCallback(IEmbeddedWindowCallback callback, int containerTaskId) {
        if (callback == null) {
            return;
        }
        synchronized (this.mCallbackMap) {
            if (this.mCallbackMap.get(callback) != null) {
                Log.e(TAG, "already register before");
                return;
            }
            EmbeddedWindowContainerCallbackDelegate delegate = new EmbeddedWindowContainerCallbackDelegate(callback);
            try {
                this.mOAtms.registerEmbeddedWindowContainerCallback(delegate, containerTaskId);
                this.mCallbackMap.put(callback, delegate);
            } catch (RemoteException e) {
                Log.e(TAG, "registerEmbeddedWindowContainerCallback remoteException " + e);
            }
        }
    }

    public void unregisterEmbeddedWindowContainerCallback(IEmbeddedWindowCallback callback, int containerTaskId) {
        if (callback == null) {
            return;
        }
        synchronized (this.mCallbackMap) {
            EmbeddedWindowContainerCallbackDelegate delegate = this.mCallbackMap.get(callback);
            if (delegate != null) {
                try {
                    this.mCallbackMap.remove(callback);
                    this.mOAtms.unregisterEmbeddedWindowContainerCallback(delegate, containerTaskId);
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterEmbeddedWindowContainerCallback remoteException " + e);
                }
            }
        }
    }

    public void startPocketStudio(Context context, List<Intent> intentList, int[] taskIdList, int[] userIdList, int focusIndex, Bundle bundle) {
        Bundle canvasExtra = new Bundle();
        canvasExtra.putInt(KEY_CANVAS_FOCUS_INDEX, focusIndex);
        startPocketStudio(context, intentList, taskIdList, userIdList, bundle, canvasExtra);
    }

    public void startPocketStudio(Context context, List<Intent> intentList, int[] taskIdList, int[] userIdList, Bundle activityBundle, Bundle canvasExtra) {
        if (!this.mHasPocketStudioFeature || context == null || intentList == null || intentList.size() < 2) {
            return;
        }
        try {
            if (this.mOAtms.shouldSkipStartPocketStudio(intentList, canvasExtra)) {
                return;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "startPocketStudio remoteException " + e);
        }
        Intent intent = new Intent(ACTION_POCKET_STUDIO);
        intent.addCategory("android.intent.category.DEFAULT");
        Bundle canvasIntentBundle = new Bundle();
        if (canvasExtra != null) {
            canvasIntentBundle = new Bundle(canvasExtra);
        }
        canvasIntentBundle.putParcelableList(KEY_CANVAS_INTENT_LIST, intentList);
        canvasIntentBundle.putIntArray(KEY_CANVAS_TASK_ID_LIST, taskIdList);
        canvasIntentBundle.putIntArray(KEY_CANVAS_USER_ID_LIST, userIdList);
        intent.putExtras(canvasIntentBundle);
        intent.addFlags(402653184);
        if (activityBundle != null && activityBundle.getBoolean(KEY_ACTIVITY_NO_ANIM)) {
            intent.addFlags(65536);
        }
        intent.getIntentExt().addOplusFlags(16384);
        try {
            context.startActivityAsUser(intent, activityBundle, UserHandle.CURRENT);
        } catch (ActivityNotFoundException e2) {
            Log.e(TAG, "startPocketStudio activity not found.");
        }
    }

    public boolean isPocketStudioMultiWindowSupported(TaskInfo taskInfo) {
        if (taskInfo == null) {
            return false;
        }
        return taskInfo.supportsPocketStudioMultiWindow;
    }

    public boolean isInFlexibleEmbedded(TaskInfo taskInfo) {
        if (taskInfo == null) {
            return false;
        }
        return taskInfo.isInFlexibleEmbedded;
    }

    public void setIsInFocusAnimating(boolean isInFocusAnimating) {
        try {
            this.mOAtms.setIsInFocusAnimating(isInFocusAnimating);
        } catch (RemoteException e) {
            Log.e(TAG, "setIsInFocusAnimating remoteException ", e);
        }
    }

    public Bundle setExtraBundle(ActivityOptions options, Bundle exBundle) {
        if (options == null || exBundle == null) {
            return null;
        }
        options.setExtraBundle(exBundle);
        return options.toBundle();
    }

    public static boolean isFlexibleActivitySuitable(Configuration configuration) {
        OplusExtraConfiguration extraConfiguration = getExtraConfiguration(configuration);
        return extraConfiguration != null && extraConfiguration.isFlexibleActivitySuitable();
    }

    public static boolean isFlexibleActivity(Configuration configuration) {
        OplusExtraConfiguration extraConfiguration = getExtraConfiguration(configuration);
        return (extraConfiguration == null || (extraConfiguration.getFlag() & 4) == 0) ? false : true;
    }

    public static List<Rect> layoutRectCalculate(List<Intent> intentList, Intent focus, int layoutOrientation, boolean panoramaMode) {
        if (intentList == null || intentList.size() == 0) {
            return new ArrayList();
        }
        try {
            int focusIndex = intentList.indexOf(focus);
            if (focusIndex < 0) {
                Log.w(TAG, "layoutRectCalculate focus intent invalid");
                focusIndex = 0;
            }
            return OplusActivityTaskManager.getService().calculateCanvasLayoutRect(intentList, focusIndex, layoutOrientation, panoramaMode);
        } catch (RemoteException e) {
            Log.e(TAG, "layoutRectCalculate RemoteException");
            return new ArrayList();
        }
    }

    public static List<Rect> layoutReplaceRectCalculate(List<Intent> intentList, Intent focusIntent, int replace, int displayId, boolean panoramaMode) {
        if (intentList == null || intentList.size() == 0) {
            return new ArrayList();
        }
        try {
            int focusIndex = intentList.indexOf(focusIntent);
            if (focusIndex < 0) {
                Log.w(TAG, "layoutRectCalculate focus intent invalid");
                focusIndex = 0;
            }
            return OplusActivityTaskManager.getService().calculateReplaceCanvasLayoutRect(intentList, focusIndex, replace, displayId, panoramaMode);
        } catch (RemoteException e) {
            Log.e(TAG, "layoutReplaceRectCalculate RemoteException");
            return new ArrayList();
        }
    }

    public static OplusExtraConfiguration getExtraConfiguration(Configuration configuration) {
        OplusBaseConfiguration baseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, configuration);
        if (baseConfiguration != null) {
            return baseConfiguration.getOplusExtraConfiguration();
        }
        return null;
    }

    public void adjustWindowFrame(WindowManager.LayoutParams attrs, Rect windowBounds, int windowingMode, Rect outDisplayFrame, Rect outParentFrame) {
        if (!ActivityThread.isSystem()) {
            return;
        }
        try {
            this.mOAtms.adjustWindowFrame(attrs, windowingMode, outDisplayFrame, outParentFrame);
        } catch (RemoteException e) {
            Log.e(TAG, "adjustWindowFrameForZoom remoteException");
        }
    }

    public boolean hasFSDFeature() {
        return this.mHasFSDFeature;
    }

    public void handleConfigurationChanged(Application application, Configuration config) {
        Configuration fakeDisplayConfiguration;
        if (!this.mHasFSDFeature || application == null) {
            return;
        }
        int scenario = config.getOplusExtraConfiguration().getScenario();
        int appDisplayId = application.getDisplayId();
        boolean shouldUpdate = false;
        if (scenario == 0 && appDisplayId == 1) {
            application.updateDisplay(0);
        } else if (scenario == 3 && appDisplayId == 0) {
            application.updateDisplay(1);
        }
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        if (activityThread == null || ActivityThread.isSystem() || activityThread.getConfiguration() == null || (fakeDisplayConfiguration = activityThread.mOplusActivityThreadExt.getFakeDisplayConfiguration()) == null) {
            return;
        }
        int appScenario = fakeDisplayConfiguration.getOplusExtraConfiguration().getScenario();
        if ((appScenario == 3 || scenario == 3) && appScenario != scenario && fakeDisplayConfiguration.diff(config) != 0) {
            shouldUpdate = true;
        }
        if (shouldUpdate) {
            fakeDisplayConfiguration.updateFrom(config);
        }
    }

    public void hookHandleBindApplication(Configuration configuration) {
        ActivityThread activityThread;
        Configuration fakeDisplayConfiguration;
        if (this.mHasFSDFeature && configuration != null && (activityThread = ActivityThread.currentActivityThread()) != null && (fakeDisplayConfiguration = activityThread.mOplusActivityThreadExt.getFakeDisplayConfiguration()) != null && configuration != null && configuration.getOplusExtraConfiguration().getScenario() == 3) {
            fakeDisplayConfiguration.updateFrom(configuration);
        }
    }

    public boolean isFlipDevice() {
        return this.mIsFlipDevice;
    }

    public boolean isFlexibleTaskEnabled(int displayId) {
        try {
            return this.mOAtms.isFlexibleTaskEnabled(displayId);
        } catch (RemoteException e) {
            Log.e(TAG, "isFlexibleTaskEnabled remoteException ", e);
            return false;
        }
    }

    public boolean setFlexibleTaskEnabled(int displayId, boolean enabled) {
        try {
            return this.mOAtms.setFlexibleTaskEnabled(displayId, enabled);
        } catch (RemoteException e) {
            Log.e(TAG, "setFlexibleTaskEnabled remoteException ", e);
            return false;
        }
    }

    public Rect adjustEndAbsForFlexibleMinimizeIfNeed(TransitionInfo.Change change) {
        try {
            return this.mOAtms.adjustEndAbsForFlexibleMinimizeIfNeed(change);
        } catch (RemoteException e) {
            Log.e(TAG, "isInMinimized remoteException ", e);
            return null;
        }
    }
}
