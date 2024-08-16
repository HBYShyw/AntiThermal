package android.app;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.IOplusActivityManager;
import android.app.usage.UsageStats;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.GraphicBuffer;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IOplusKeyEventObserver;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SharedMemory;
import android.text.TextUtils;
import android.util.Log;
import android.util.Singleton;
import android.util.Slog;
import android.util.SparseArray;
import android.view.IRecentsAnimationController;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;
import android.window.TaskSnapshot;
import com.oplus.app.IOplusAppStartController;
import com.oplus.app.IOplusAppSwitchObserver;
import com.oplus.app.IOplusFreeformConfigChangedListener;
import com.oplus.app.IOplusGameSpaceController;
import com.oplus.app.IOplusHansListener;
import com.oplus.app.IOplusPermissionRecordController;
import com.oplus.app.IOplusProtectConnection;
import com.oplus.app.IOplusSplitScreenObserver;
import com.oplus.app.IOplusZoomWindowConfigChangedListener;
import com.oplus.app.IProcessTerminateObserver;
import com.oplus.app.ISecurityPageController;
import com.oplus.app.ITerminateObserver;
import com.oplus.app.OplusAppInfo;
import com.oplus.app.OplusAppSwitchConfig;
import com.oplus.app.OplusProcessTerminateObserver;
import com.oplus.bracket.IOplusBracketModeChangedListener;
import com.oplus.compactwindow.IOplusCompactWindowObserver;
import com.oplus.compatmode.IOplusCompatModeSession;
import com.oplus.confinemode.IOplusConfineModeObserver;
import com.oplus.darkmode.OplusDarkModeData;
import com.oplus.datasync.ISysStateChangeCallback;
import com.oplus.eap.IOplusEapDataCallback;
import com.oplus.eventhub.sdk.EventCallback;
import com.oplus.eventhub.sdk.aidl.IEventCallback;
import com.oplus.favorite.IOplusFavoriteQueryCallback;
import com.oplus.globaldrag.OplusGlobalDragAndDropRUSConfig;
import com.oplus.lockscreen.IOplusLockScreenCallback;
import com.oplus.miragewindow.IOplusMirageDisplayObserver;
import com.oplus.miragewindow.IOplusMirageSessionCallback;
import com.oplus.miragewindow.IOplusMirageWindowObserver;
import com.oplus.miragewindow.IOplusMirageWindowSession;
import com.oplus.miragewindow.OplusMirageWindowInfo;
import com.oplus.multiapp.OplusMultiAppConfig;
import com.oplus.multisearch.IOplusMultiSearchManagerSession;
import com.oplus.osense.complexscene.OplusComplexSceneObserver;
import com.oplus.quickreply.IQuickReplyCallback;
import com.oplus.splitscreen.IOplusSplitScreenSession;
import com.oplus.util.OplusAccidentallyTouchData;
import com.oplus.util.OplusDisplayCompatData;
import com.oplus.util.OplusDisplayOptimizationData;
import com.oplus.util.OplusPackageFreezeData;
import com.oplus.util.OplusProcDependData;
import com.oplus.util.OplusReflectData;
import com.oplus.util.OplusResolveData;
import com.oplus.util.OplusSecureKeyboardData;
import com.oplus.util.OplusUXIconData;
import com.oplus.verifycode.IOplusVerifyCodeListener;
import com.oplus.zoomwindow.IOplusZoomAppObserver;
import com.oplus.zoomwindow.IOplusZoomTaskController;
import com.oplus.zoomwindow.IOplusZoomWindowObserver;
import com.oplus.zoomwindow.OplusZoomControlViewInfo;
import com.oplus.zoomwindow.OplusZoomFloatHandleViewInfo;
import com.oplus.zoomwindow.OplusZoomInputEventInfo;
import com.oplus.zoomwindow.OplusZoomWindowInfo;
import com.oplus.zoomwindow.OplusZoomWindowRUSConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusActivityManager {
    public static final int COMPLEX_SCENE_FLOAT_WINDOW = 2;
    public static final int COMPLEX_SCENE_FOCUS_CHANGED = 3;
    public static final int COMPLEX_SCENE_SPLIT_SCREEN = 1;
    private static final Singleton<IOplusActivityManager> IOplusActivityManagerSingleton = new Singleton<IOplusActivityManager>() { // from class: android.app.OplusActivityManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusActivityManager m8create() {
            try {
                IBinder b = ServiceManager.getService("activity");
                IActivityManager am = IActivityManager.Stub.asInterface(b);
                IBinder extbinder = am.asBinder().getExtension();
                Log.d(OplusActivityManager.TAG, "get AMS extension: " + b);
                return IOplusActivityManager.Stub.asInterface(extbinder);
            } catch (Exception e) {
                Log.e(OplusActivityManager.TAG, "create OplusActivityManagerServiceEnhance singleton failed: " + e.getMessage());
                return null;
            }
        }
    };
    public static final int PAYJOY_OPEN = 1;
    private static final String TAG = "OplusActivityManager";
    private final OplusActivityTaskManager mOplusAtm = OplusActivityTaskManager.getInstance();

    /* loaded from: classes.dex */
    public interface ITaskStackListenerWrapper {
        void onActivityPinned(String str, int i, int i2, int i3);

        void onActivityUnpinned();

        void onTaskSnapshotChanged(int i, TaskSnapshotWrapper taskSnapshotWrapper);
    }

    private static IOplusActivityManager getService() {
        return (IOplusActivityManager) IOplusActivityManagerSingleton.get();
    }

    public static OplusActivityManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final OplusActivityManager INSTANCE = new OplusActivityManager();

        private LazyHolder() {
        }
    }

    public void swapDockedFullscreenStack() throws RemoteException {
    }

    public void setSecureController(IActivityController controller) throws RemoteException {
        this.mOplusAtm.setSecureController(controller);
    }

    public ComponentName getTopActivityComponentName() throws RemoteException {
        return this.mOplusAtm.getTopActivityComponentName();
    }

    public ApplicationInfo getTopApplicationInfo() throws RemoteException {
        return this.mOplusAtm.getTopApplicationInfo();
    }

    public List<OplusAppInfo> getAllTopAppInfos() throws RemoteException {
        return this.mOplusAtm.getAllTopAppInfos();
    }

    public List<String> getFreeformConfigList(int type) {
        return null;
    }

    public boolean isFreeformEnabled() {
        return false;
    }

    public boolean addFreeformConfigChangedListener(IOplusFreeformConfigChangedListener listener) {
        return false;
    }

    public boolean removeFreeformConfigChangedListener(IOplusFreeformConfigChangedListener listener) {
        return false;
    }

    public boolean registerAppSwitchObserver(String pkgName, IOplusAppSwitchObserver observer, OplusAppSwitchConfig config) throws RemoteException {
        return this.mOplusAtm.registerAppSwitchObserver(pkgName, observer, config);
    }

    public boolean unregisterAppSwitchObserver(String pkgName, OplusAppSwitchConfig config) throws RemoteException {
        return this.mOplusAtm.unregisterAppSwitchObserver(pkgName, config);
    }

    public int getSplitScreenState(Intent intent) throws RemoteException {
        return this.mOplusAtm.getSplitScreenState(intent);
    }

    public ComponentName getDockTopAppName() {
        return null;
    }

    public List<String> getAllTopPkgName() {
        return null;
    }

    public ApplicationInfo getFreeFormAppInfo() {
        return null;
    }

    public final int startActivityForFreeform(Intent intent, Bundle bOptions, int userId, String callPkg) {
        return -1;
    }

    public final void exitOplusosFreeform(Bundle bOptions) {
    }

    public int startZoomWindow(Intent intent, Bundle options, int userId, String callPkg) throws RemoteException {
        return this.mOplusAtm.startZoomWindow(intent, options, userId, callPkg);
    }

    public boolean registerZoomWindowObserver(IOplusZoomWindowObserver observer) throws RemoteException {
        return this.mOplusAtm.registerZoomWindowObserver(observer);
    }

    public boolean unregisterZoomWindowObserver(IOplusZoomWindowObserver observer) throws RemoteException {
        return this.mOplusAtm.unregisterZoomWindowObserver(observer);
    }

    public boolean registerZoomAppObserver(IOplusZoomAppObserver observer) throws RemoteException {
        return this.mOplusAtm.registerZoomAppObserver(observer);
    }

    public boolean unregisterZoomAppObserver(IOplusZoomAppObserver observer) throws RemoteException {
        return this.mOplusAtm.unregisterZoomAppObserver(observer);
    }

    public OplusZoomWindowInfo getCurrentZoomWindowState() throws RemoteException {
        return this.mOplusAtm.getCurrentZoomWindowState();
    }

    public void hideZoomWindow(int flag) throws RemoteException {
        this.mOplusAtm.hideZoomWindow(flag);
    }

    public List<String> getZoomAppConfigList(int type) throws RemoteException {
        return this.mOplusAtm.getZoomAppConfigList(type);
    }

    public void onInputEvent(OplusZoomInputEventInfo inputEventInfo) throws RemoteException {
        this.mOplusAtm.onInputEvent(inputEventInfo);
    }

    public void onControlViewChanged(OplusZoomControlViewInfo cvInfo) throws RemoteException {
        this.mOplusAtm.onControlViewChanged(cvInfo);
    }

    public void onFloatHandleViewChanged(OplusZoomFloatHandleViewInfo floatHandleInfo) throws RemoteException {
        this.mOplusAtm.onFloatHandleViewChanged(floatHandleInfo);
    }

    public IOplusZoomTaskController getZoomTaskController() throws RemoteException {
        return this.mOplusAtm.getZoomTaskController();
    }

    public int getRenderThreadTid(int pid) throws RemoteException {
        return this.mOplusAtm.getRenderThreadTid(pid);
    }

    public boolean setInputConsumerEnabled(boolean enabled, IRecentsAnimationController controller) throws RemoteException {
        return this.mOplusAtm.setInputConsumerEnabled(enabled, controller);
    }

    public void adjustWindowFrameForZoom(WindowManager.LayoutParams attrs, int windowingMode, Rect outDisplayFrame, Rect outParentFrame) throws RemoteException {
        this.mOplusAtm.adjustWindowFrameForZoom(attrs, windowingMode, outDisplayFrame, outParentFrame);
    }

    public boolean isZoomSupportMultiWindow(String packageName, ComponentName componentName) throws RemoteException {
        return this.mOplusAtm.isZoomSupportMultiWindow(packageName, componentName);
    }

    public boolean isSupportZoomWindowMode() throws RemoteException {
        return this.mOplusAtm.isSupportZoomWindowMode();
    }

    public boolean isSupportZoomMode(String target, int userId, String callPkg, Bundle extension) throws RemoteException {
        return this.mOplusAtm.isSupportZoomMode(target, userId, callPkg, extension);
    }

    public boolean handleShowCompatibilityToast(String target, int userId, String callPkg, Bundle extension, int type) throws RemoteException {
        return this.mOplusAtm.handleShowCompatibilityToast(target, userId, callPkg, extension, type);
    }

    public void startMiniZoomFromZoom(int startWay) throws RemoteException {
        this.mOplusAtm.startMiniZoomFromZoom(startWay);
    }

    public OplusZoomWindowRUSConfig getZoomWindowConfig() throws RemoteException {
        return this.mOplusAtm.getZoomWindowConfig();
    }

    public void setZoomWindowConfig(OplusZoomWindowRUSConfig config) throws RemoteException {
        this.mOplusAtm.setZoomWindowConfig(config);
    }

    public boolean addZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener listener) throws RemoteException {
        return this.mOplusAtm.addZoomWindowConfigChangedListener(listener);
    }

    public boolean removeZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener listener) throws RemoteException {
        return this.mOplusAtm.removeZoomWindowConfigChangedListener(listener);
    }

    public boolean isZoomSimpleModeEnable() throws RemoteException {
        return this.mOplusAtm.isZoomSimpleModeEnable();
    }

    public void notifyZoomStateChange(String packageName, int action) throws RemoteException {
        this.mOplusAtm.notifyZoomStateChange(packageName, action);
    }

    /* loaded from: classes.dex */
    public static class TaskSnapshotWrapper {
        TaskSnapshot mTaskSnapshot;

        public TaskSnapshotWrapper(TaskSnapshot taskSnapshot) {
            this.mTaskSnapshot = taskSnapshot;
        }

        public void destroy() {
            GraphicBuffer snapshotInfo;
            try {
                TaskSnapshot taskSnapshot = this.mTaskSnapshot;
                if (taskSnapshot != null && (snapshotInfo = taskSnapshot.getSnapshot()) != null) {
                    snapshotInfo.destroy();
                }
            } catch (Exception e) {
                System.gc();
            }
        }

        public Bitmap getSnapshotBitmap() {
            return null;
        }
    }

    public static void registerTaskStackListener(final ITaskStackListenerWrapper listener) {
        TaskStackListener taskStackListener = new TaskStackListener() { // from class: android.app.OplusActivityManager.2
            public void onTaskSnapshotChanged(int taskId, TaskSnapshot snapshot) {
                ITaskStackListenerWrapper.this.onTaskSnapshotChanged(taskId, new TaskSnapshotWrapper(snapshot));
            }

            public void onActivityUnpinned() {
                ITaskStackListenerWrapper.this.onActivityUnpinned();
            }

            public void onActivityPinned(String packageName, int userId, int taskId, int stackId) {
                ITaskStackListenerWrapper.this.onActivityPinned(packageName, userId, taskId, stackId);
            }
        };
        try {
            ActivityTaskManager.getService().registerTaskStackListener(taskStackListener);
        } catch (RemoteException e) {
            Log.w(TAG, "registerTaskStackListener failed.");
        }
    }

    public static List<ActivityManager.RunningTaskInfo> getFilteredTasks(int num, boolean filterOnlyVisibleRecents) {
        return null;
    }

    public boolean writeEdgeTouchPreventParam(String callPkg, String scenePkg, List<String> paramCmdList) throws RemoteException {
        return this.mOplusAtm.writeEdgeTouchPreventParam(callPkg, scenePkg, paramCmdList);
    }

    public void setDefaultEdgeTouchPreventParam(String callPkg, List<String> paramCmdList) throws RemoteException {
        this.mOplusAtm.setDefaultEdgeTouchPreventParam(callPkg, paramCmdList);
    }

    public boolean resetDefaultEdgeTouchPreventParam(String callPkg) throws RemoteException {
        return this.mOplusAtm.resetDefaultEdgeTouchPreventParam(callPkg);
    }

    public boolean isSupportEdgeTouchPrevent() throws RemoteException {
        return this.mOplusAtm.isSupportEdgeTouchPrevent();
    }

    public void setEdgeTouchCallRules(String callPkg, Map<String, List<String>> rulesMap) throws RemoteException {
        this.mOplusAtm.setEdgeTouchCallRules(callPkg, rulesMap);
    }

    public int splitScreenForEdgePanel(Intent intent, int userId) throws RemoteException {
        return this.mOplusAtm.splitScreenForEdgePanel(intent, userId);
    }

    public boolean isAppCallRefuseMode() throws RemoteException {
        return (getConfineMode() & 1) != 0;
    }

    public void setAppCallRefuseMode(boolean enable) throws RemoteException {
        setConfineMode(1, enable);
    }

    public void setChildSpaceMode(boolean mode) throws RemoteException {
        setConfineMode(2, mode);
    }

    public void setAllowLaunchApps(List<String> allowLaunchApps) throws RemoteException {
        setPermitList(2, 5, allowLaunchApps, false);
        setPermitList(2, 5, allowLaunchApps, true);
    }

    public void setConfineMode(int mode, boolean on) throws RemoteException {
        this.mOplusAtm.setConfineMode(mode, on);
    }

    public int getConfineMode() throws RemoteException {
        return this.mOplusAtm.getConfineMode();
    }

    public void setPermitList(int mode, int type, List<String> permits, boolean isMultiApp) throws RemoteException {
        this.mOplusAtm.setPermitList(mode, type, permits, isMultiApp);
    }

    public int getFontVariationAdaption(String packagename) throws RemoteException {
        return getFontVariationAdaptionData(packagename);
    }

    public void setGimbalLaunchPkg(String pkgName) throws RemoteException {
        this.mOplusAtm.setGimbalLaunchPkg(pkgName);
    }

    public void setPackagesState(Map<String, Integer> packageMap) throws RemoteException {
        this.mOplusAtm.setPackagesState(packageMap);
    }

    public boolean registerLockScreenCallback(IOplusLockScreenCallback callback) throws RemoteException {
        return this.mOplusAtm.registerLockScreenCallback(callback);
    }

    public boolean unregisterLockScreenCallback(IOplusLockScreenCallback callback) throws RemoteException {
        return this.mOplusAtm.unregisterLockScreenCallback(callback);
    }

    public void notifySplitScreenStateChanged(String event, Bundle bundle, boolean broadcast) throws RemoteException {
        this.mOplusAtm.notifySplitScreenStateChanged(event, bundle, broadcast);
    }

    public boolean setSplitScreenObserver(IOplusSplitScreenObserver observer) throws RemoteException {
        return this.mOplusAtm.setSplitScreenObserver(observer);
    }

    public boolean isInSplitScreenMode() throws RemoteException {
        return this.mOplusAtm.isInSplitScreenMode();
    }

    public boolean dismissSplitScreenMode(int type) throws RemoteException {
        return this.mOplusAtm.dismissSplitScreenMode(type);
    }

    public boolean registerSplitScreenObserver(int observerId, IOplusSplitScreenObserver observer) throws RemoteException {
        return this.mOplusAtm.registerSplitScreenObserver(observerId, observer);
    }

    public boolean unregisterSplitScreenObserver(int observerId, IOplusSplitScreenObserver observer) throws RemoteException {
        return this.mOplusAtm.unregisterSplitScreenObserver(observerId, observer);
    }

    public Bundle getSplitScreenStatus(String event) throws RemoteException {
        return this.mOplusAtm.getSplitScreenStatus(event);
    }

    public boolean splitScreenForTopApp(int type) throws RemoteException {
        return this.mOplusAtm.splitScreenForTopApp(type);
    }

    public boolean splitScreenForRecentTasks(int taskId) throws RemoteException {
        return this.mOplusAtm.splitScreenForRecentTasks(taskId);
    }

    public int setTaskWindowingModeSplitScreen(int taskId) throws RemoteException {
        return this.mOplusAtm.setTaskWindowingModeSplitScreen(taskId);
    }

    public boolean splitScreenForEdgePanel(Intent intent, boolean launchToPrimary, int launchArea) throws RemoteException {
        return this.mOplusAtm.splitScreenForEdgePanelExt(intent, launchToPrimary, launchArea);
    }

    public boolean hasLargeScreenFeature() throws RemoteException {
        return this.mOplusAtm.hasLargeScreenFeature();
    }

    public boolean isFolderInnerScreen() throws RemoteException {
        return this.mOplusAtm.isFolderInnerScreen();
    }

    public Rect getMinimizedBounds(int dockedSide) throws RemoteException {
        return this.mOplusAtm.getMinimizedBounds(dockedSide);
    }

    public IOplusSplitScreenSession getSplitScreenSession() throws RemoteException {
        return this.mOplusAtm.getSplitScreenSession();
    }

    public Bundle getLeftRightBoundsForIme() throws RemoteException {
        return this.mOplusAtm.getLeftRightBoundsForIme();
    }

    public boolean registerKeyEventObserver(String observerFingerPrint, IOplusKeyEventObserver observer, int listenFlag) throws RemoteException {
        return this.mOplusAtm.registerKeyEventObserver(observerFingerPrint, observer, listenFlag);
    }

    public boolean unregisterKeyEventObserver(String observerFingerPrint) throws RemoteException {
        return this.mOplusAtm.unregisterKeyEventObserver(observerFingerPrint);
    }

    public boolean registerKeyEventInterceptor(String interceptorFingerPrint, IOplusKeyEventObserver observer, Map<Integer, Integer> configs) throws RemoteException {
        return this.mOplusAtm.registerKeyEventInterceptor(interceptorFingerPrint, observer, configs);
    }

    public boolean unregisterKeyEventInterceptor(String interceptorFingerPrint) throws RemoteException {
        return this.mOplusAtm.unregisterKeyEventInterceptor(interceptorFingerPrint);
    }

    public boolean updateAppData(String module, Bundle bundle) throws RemoteException {
        return this.mOplusAtm.updateAppData(module, bundle);
    }

    public boolean registerSysStateChangeObserver(String module, ISysStateChangeCallback callback) throws RemoteException {
        return this.mOplusAtm.registerSysStateChangeObserver(module, callback);
    }

    public boolean unregisterSysStateChangeObserver(String module, ISysStateChangeCallback callback) throws RemoteException {
        return this.mOplusAtm.unregisterSysStateChangeObserver(module, callback);
    }

    public OplusGlobalDragAndDropRUSConfig getGlobalDragAndDropConfig() throws RemoteException {
        return this.mOplusAtm.getGlobalDragAndDropConfig();
    }

    public void setGlobalDragAndDropConfig(OplusGlobalDragAndDropRUSConfig config) throws RemoteException {
        this.mOplusAtm.setGlobalDragAndDropConfig(config);
    }

    public boolean registerMirageWindowObserver(IOplusMirageWindowObserver observer) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager registerMirageWindowObserver");
        return this.mOplusAtm.registerMirageWindowObserver(observer);
    }

    public boolean unregisterMirageWindowObserver(IOplusMirageWindowObserver observer) throws RemoteException {
        return this.mOplusAtm.unregisterMirageWindowObserver(observer);
    }

    public boolean registerMirageDisplayObserver(IOplusMirageDisplayObserver observer) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager registerMirageDisplayObserver");
        return this.mOplusAtm.registerMirageDisplayObserver(observer);
    }

    public boolean unregisterMirageDisplayObserver(IOplusMirageDisplayObserver observer) throws RemoteException {
        return this.mOplusAtm.unregisterMirageDisplayObserver(observer);
    }

    public int createMirageDisplay(Surface surface) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager createMirageDisplay");
        return this.mOplusAtm.createMirageDisplay(surface);
    }

    public Bundle getTaskInfo(ComponentName name) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager getTaskInfo");
        return this.mOplusAtm.getTaskInfo(name);
    }

    public void startMirageWindowMode(ComponentName cpnName, int taskId, int flags, Bundle options) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager startMirageWindowMode");
        this.mOplusAtm.startMirageWindowModeWithName(cpnName, taskId, flags, options);
    }

    public int startMirageWindowMode(Intent intent, Bundle options) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager startMirageWindowMode");
        return this.mOplusAtm.startMirageWindowMode(intent, options);
    }

    public boolean isMirageWindowShow() throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager isMirageWindowShow");
        return this.mOplusAtm.isMirageWindowShow();
    }

    public void stopMirageWindowMode() throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager stopMirageWindowMode");
        this.mOplusAtm.stopMirageWindowModeOld();
    }

    public void stopMirageWindowMode(Bundle options) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager stopMirageWindowMode");
        this.mOplusAtm.stopMirageWindowMode(options);
    }

    public void setMirageDisplaySurfaceById(int displayId, Surface surface) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager setMirageDisplaySurfaceById");
        this.mOplusAtm.setMirageDisplaySurfaceById(displayId, surface);
    }

    public void setMirageDisplaySurfaceByMode(int mode, Surface surface) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager setMirageDisplaySurfaceByMode");
        this.mOplusAtm.setMirageDisplaySurfaceByMode(mode, surface);
    }

    public int getMirageDisplayCastMode(int displayId) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager getMirageDisplayCastMode");
        return this.mOplusAtm.getMirageDisplayCastMode(displayId);
    }

    public void expandToFullScreen() throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager expandTofullscreen");
        this.mOplusAtm.expandToFullScreen();
    }

    public void setMirageWindowSilent(String pkgName) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager setMirageWindowSilent");
        this.mOplusAtm.setMirageWindowSilent(pkgName);
    }

    public boolean isSupportMirageWindowMode() throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager isSupportMirageWindowMode");
        return this.mOplusAtm.isSupportMirageWindowMode();
    }

    public OplusMirageWindowInfo getMirageWindowInfo() throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager getMirageWindowInfo");
        return this.mOplusAtm.getMirageWindowInfo();
    }

    public boolean updateMirageWindowCastFlag(int castFlag, Bundle options) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager updateMirageWindowCastFlag");
        return this.mOplusAtm.updateMirageWindowCastFlag(castFlag, options);
    }

    public boolean updatePrivacyProtectionList(List<String> name, boolean append) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager updatePrivacyProtectionList");
        return this.mOplusAtm.updatePrivacyProtectionList(name, append);
    }

    public boolean updatePrivacyProtectionList(List<String> name, boolean append, boolean isDefault, Bundle options) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager default updatePrivacyProtectionList");
        return this.mOplusAtm.updatePrivacyProtectionListWithBundle(name, append, isDefault, options);
    }

    public IOplusMirageWindowSession createMirageWindowSession(IOplusMirageSessionCallback callback) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager createMirageWindowSession");
        return this.mOplusAtm.createMirageWindowSession(callback);
    }

    public int getGetDisplayIdForPackageName(String packageName) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager getGetDisplayIdForPackageName");
        return this.mOplusAtm.getGetDisplayIdForPackageName(packageName);
    }

    public void feedbackUserSelection(int eventId, int selection, Bundle extension) throws RemoteException {
        Slog.d("MirageDisplayWindow", "OplusActivityManager feedbackUserSelection");
        this.mOplusAtm.feedbackUserSelection(eventId, selection, extension);
    }

    public boolean updateCarModeMultiLaunchWhiteList(String list) throws RemoteException {
        Slog.d("MirageDisplayWindow", "OplusActivityManager updateCarModeMultiLaunchWhiteList");
        return this.mOplusAtm.updateCarModeMultiLaunchWhiteList(list);
    }

    public boolean rebindDisplayIfNeeded(int castDisplayId, int mirageDisplayId) throws RemoteException {
        Slog.d("MirageDisplayWindow", "OplusActivityManager rebindDisplayIfNeeded");
        return this.mOplusAtm.rebindDisplayIfNeeded(castDisplayId, mirageDisplayId);
    }

    public boolean registerConfineModeObserver(IOplusConfineModeObserver observer) throws RemoteException {
        return this.mOplusAtm.registerConfineModeObserver(observer);
    }

    public boolean unregisterConfineModeObserver(IOplusConfineModeObserver observer) throws RemoteException {
        return this.mOplusAtm.unregisterConfineModeObserver(observer);
    }

    public String readNodeFile(int nodeFlag) throws RemoteException {
        return this.mOplusAtm.readNodeFile(nodeFlag);
    }

    public String readNodeFileByDevice(int deviceId, int nodeFlag) throws RemoteException {
        return this.mOplusAtm.readNodeFileByDevice(deviceId, nodeFlag);
    }

    public boolean writeNodeFile(int nodeFlag, String info) throws RemoteException {
        return this.mOplusAtm.writeNodeFile(nodeFlag, info);
    }

    public boolean writeNodeFileByDevice(int deviceId, int nodeFlag, String info) throws RemoteException {
        return this.mOplusAtm.writeNodeFileByDevice(deviceId, nodeFlag, info);
    }

    public boolean isTouchNodeSupport(int deviceId, int nodeFlag) throws RemoteException {
        return this.mOplusAtm.isTouchNodeSupport(deviceId, nodeFlag);
    }

    public int createVirtualDisplayDevice(Bundle bundle) throws RemoteException {
        return this.mOplusAtm.createVirtualDisplayDevice(bundle);
    }

    public void releaseVirtualDisplayDevice(int displayId) throws RemoteException {
        this.mOplusAtm.releaseVirtualDisplayDevice(displayId);
    }

    public void moveTaskToDisplay(int taskId, int displayId, boolean top) throws RemoteException {
        this.mOplusAtm.moveTaskToDisplay(taskId, displayId, top);
    }

    public void updatePermissionChoice(String packageName, String permission, int choice) throws RemoteException {
        if (getService() != null) {
            getService().updatePermissionChoice(packageName, permission, choice);
        } else {
            Log.e(TAG, "updatePermissionChoice failed because service has not been created");
        }
    }

    public void setPermissionInterceptEnable(boolean enabled) throws RemoteException {
        if (getService() != null) {
            getService().setPermissionInterceptEnable(enabled);
        } else {
            Log.e(TAG, "setPermissionInterceptEnable failed because service has not been created");
        }
    }

    public boolean isPermissionInterceptEnabled() throws RemoteException {
        if (getService() != null) {
            return getService().isPermissionInterceptEnabled();
        }
        Log.e(TAG, "isPermissionInterceptEnabled failed because service has not been created");
        return false;
    }

    public void setSystemProperties(String properties, String value) throws RemoteException {
    }

    public void killPidForce(int pid) throws RemoteException {
        if (getService() != null) {
            getService().killPidForce(pid);
        } else {
            Log.e(TAG, "killPidForce failed because service has not been created");
        }
    }

    public void grantOplusPermissionByGroup(String packageName, String permission) throws RemoteException {
        if (getService() != null) {
            getService().grantOplusPermissionByGroup(packageName, permission);
        } else {
            Log.e(TAG, "grantOplusPermissionByGroup failed because service has not been created");
        }
    }

    public void revokeOplusPermissionByGroup(String packageName, String permission) throws RemoteException {
        if (getService() != null) {
            getService().revokeOplusPermissionByGroup(packageName, permission);
        } else {
            Log.e(TAG, "revokeOplusPermissionByGroup failed because service has not been created");
        }
    }

    public void handleAppForNotification(String pkgName, int uid, int otherInfo) throws RemoteException {
        if (getService() != null) {
            getService().handleAppForNotification(pkgName, uid, otherInfo);
        } else {
            Log.e(TAG, "revokeOplusPermissionByGroup failed because service has not been created");
        }
    }

    public OplusAccidentallyTouchData getAccidentallyTouchData() throws RemoteException {
        if (getService() != null) {
            return getService().getAccidentallyTouchData();
        }
        Log.e(TAG, "getAccidentallyTouchData failed because service has not been created");
        return null;
    }

    public void setGameSpaceController(IOplusGameSpaceController watcher) throws RemoteException {
        if (getService() != null) {
            getService().setGameSpaceController(watcher);
        } else {
            Log.e(TAG, "setGameSpaceController failed because service has not been created");
        }
    }

    public List<String> getGlobalPkgWhiteList(int type) throws RemoteException {
        if (getService() != null) {
            return getService().getGlobalPkgWhiteList(type);
        }
        Log.e(TAG, "getGlobalPkgWhiteList failed because service has not been created");
        return Collections.emptyList();
    }

    public List<String> getGlobalProcessWhiteList() throws RemoteException {
        if (getService() != null) {
            return getService().getGlobalProcessWhiteList();
        }
        Log.e(TAG, "getGlobalProcessWhiteList failed because service has not been created");
        return Collections.emptyList();
    }

    @Deprecated
    public void addStageProtectInfo(String pkg, String fromPkg, long timeout) throws RemoteException {
        Slog.i("OplusSelfProtectManager", "failed to add self-protect, pkg: " + pkg + " fromPkg: " + fromPkg);
    }

    public void addStageProtectInfo(String callerPkg, String protectPkg, List<String> processList, String reason, long timeout, IOplusProtectConnection connection) throws RemoteException {
        if (getService() != null) {
            getService().addStageProtectInfo(callerPkg, protectPkg, processList, reason, timeout, connection);
        } else {
            Log.e(TAG, "addStageProtectInfo failed because service has not been created");
        }
    }

    public void removeStageProtectInfo(String protectPkg, String callerPkg) throws RemoteException {
        if (getService() != null) {
            getService().removeStageProtectInfo(protectPkg, callerPkg);
        } else {
            Log.e(TAG, "removeStageProtectInfo failed because service has not been created");
        }
    }

    @Deprecated(since = "No longer use")
    public OplusDisplayOptimizationData getDisplayOptimizationData() throws RemoteException {
        if (getService() != null) {
            return getService().getDisplayOptimizationData();
        }
        Log.e(TAG, "getDisplayOptimizationData failed because service has not been created");
        return new OplusDisplayOptimizationData();
    }

    public OplusSecureKeyboardData getSecureKeyboardData() throws RemoteException {
        if (getService() != null) {
            return getService().getSecureKeyboardData();
        }
        Log.e(TAG, "getSecureKeyboardData failed because service has not been created");
        return null;
    }

    public List<String> getStageProtectListFromPkg(String pkg, int type) throws RemoteException {
        if (getService() != null) {
            return getService().getStageProtectListFromPkg(pkg, type);
        }
        Log.e(TAG, "getStageProtectListFromPkg failed because service has not been created");
        return Collections.emptyList();
    }

    public void handleAppFromControlCenter(String pkgName, int uid) throws RemoteException {
        if (getService() != null) {
            getService().handleAppFromControlCenter(pkgName, uid);
        } else {
            Log.e(TAG, "handleAppFromControlCenter failed because service has not been created");
        }
    }

    public OplusDisplayCompatData getDisplayCompatData() throws RemoteException {
        if (getService() != null) {
            return getService().getDisplayCompatData();
        }
        Log.e(TAG, "getDisplayCompatData failed because service has not been created");
        return new OplusDisplayCompatData();
    }

    public boolean getIsSupportMultiApp() throws RemoteException {
        if (getService() != null) {
            return getService().getIsSupportMultiApp();
        }
        Log.e(TAG, "getIsSupportMultiApp failed because service has not been created");
        return false;
    }

    public List<String> getMultiAppList(int type) throws RemoteException {
        if (getService() != null) {
            return getService().getMultiAppList(type);
        }
        Log.e(TAG, "getMultiAppList failed because service has not been created");
        return Collections.emptyList();
    }

    public String getMultiAppAlias(String pkgName) throws RemoteException {
        if (getService() != null) {
            return getService().getMultiAppAlias(pkgName);
        }
        Log.e(TAG, "getMultiAppAlias failed because service has not been created");
        return "";
    }

    public int setMultiAppConfig(OplusMultiAppConfig config) throws RemoteException {
        if (getService() != null) {
            return getService().setMultiAppConfig(config);
        }
        Log.e(TAG, "setMultiAppConfig failed because service has not been created");
        return 0;
    }

    public OplusMultiAppConfig getMultiAppConfig() throws RemoteException {
        if (getService() != null) {
            return getService().getMultiAppConfig();
        }
        Log.e(TAG, "getMultiAppConfig failed because service has not been created");
        return new OplusMultiAppConfig();
    }

    public int setMultiAppAlias(String pkgName, String alias) throws RemoteException {
        if (getService() != null) {
            return getService().setMultiAppAlias(pkgName, alias);
        }
        Log.e(TAG, "setMultiAppAlias failed because service has not been created");
        return 0;
    }

    public int setMultiAppAccessMode(String pkgName, int accessMode) throws RemoteException {
        if (getService() != null) {
            return getService().setMultiAppAccessMode(pkgName, accessMode);
        }
        Log.e(TAG, "setMultiAppAccessMode failed because service has not been created");
        return 0;
    }

    public int getMultiAppAccessMode(String pkgName) throws RemoteException {
        if (getService() != null) {
            return getService().getMultiAppAccessMode(pkgName);
        }
        Log.e(TAG, "getMultiAppAccessMode failed because service has not been created");
        return 0;
    }

    public Map<String, Integer> getMultiAppAllAccessMode() throws RemoteException {
        Map<String, Integer> resMap = new HashMap<>();
        if (getService() != null) {
            Bundle outBundle = new Bundle();
            getService().getMultiAppAllAccessMode(outBundle);
            ArrayList<Integer> valList = outBundle.getIntegerArrayList("value");
            ArrayList<String> keyList = outBundle.getStringArrayList("key");
            if (valList != null && keyList != null && valList.size() != keyList.size()) {
                for (int i = 0; i < keyList.size(); i++) {
                    resMap.put(keyList.get(i), valList.get(i));
                }
            }
        } else {
            Log.e(TAG, "getMultiAppAllAccessMode failed because service has not been created");
        }
        return resMap;
    }

    public boolean isMultiApp(int userId, String pkgName) throws RemoteException {
        if (getService() != null) {
            return getService().isMultiApp(userId, pkgName);
        }
        Log.e(TAG, "isMultiApp failed because service has not been created");
        return false;
    }

    public int setMultiAppStatus(String pkgName, int status) throws RemoteException {
        if (getService() != null) {
            return getService().setMultiAppStatus(pkgName, status);
        }
        Log.e(TAG, "setMultiAppStatus failed because service has not been created");
        return 0;
    }

    public int getMultiAppMaxCreateNum() throws RemoteException {
        if (getService() != null) {
            return getService().getMultiAppMaxCreateNum();
        }
        Log.e(TAG, "getMultiAppMaxCreateNum failed because service has not been created");
        return 0;
    }

    public void scanFileIfNeed(int userId, String path) throws RemoteException {
        if (getService() != null) {
            getService().scanFileIfNeed(userId, path);
        } else {
            Log.e(TAG, "scanFileIfNeed failed because service has not been created");
        }
    }

    public void addMiniProgramShare(String shareAppPkgName, String miniProgramPkgName, String miniProgramSignature) throws RemoteException {
        if (getService() != null) {
            getService().addMiniProgramShare(shareAppPkgName, miniProgramPkgName, miniProgramSignature);
        } else {
            Log.e(TAG, "addMiniProgramShare failed because service has not been created");
        }
    }

    public void removeMiniProgramShare(String shareAppPkgName, String miniProgramPkgName, String miniProgramSignature) throws RemoteException {
        if (getService() != null) {
            getService().addMiniProgramShare(shareAppPkgName, miniProgramPkgName, miniProgramSignature);
        } else {
            Log.e(TAG, "addMiniProgramShare failed because service has not been created");
        }
    }

    public OplusResolveData getResolveData() throws RemoteException {
        if (getService() != null) {
            return getService().getResolveData();
        }
        Log.e(TAG, "getResolveData failed because service has not been created");
        return null;
    }

    public OplusReflectData getReflectData() throws RemoteException {
        if (getService() != null) {
            return getService().getReflectData();
        }
        Log.e(TAG, "getReflectData failed because service has not been created");
        return null;
    }

    public void addFastAppWCPay(String originAppCpn, String fastAppCpn) throws RemoteException {
        if (getService() != null) {
            getService().addFastAppWCPay(originAppCpn, fastAppCpn);
        } else {
            Log.e(TAG, "addFastAppWCPay failed because service has not been created");
        }
    }

    public void removeFastAppWCPay(String originAppCpn, String fastAppCpn) throws RemoteException {
        if (getService() != null) {
            getService().removeFastAppWCPay(originAppCpn, fastAppCpn);
        } else {
            Log.e(TAG, "removeFastAppWCPay failed because service has not been created");
        }
    }

    public List<OplusPackageFreezeData> getRunningProcesses() throws RemoteException {
        if (getService() != null) {
            return getService().getRunningProcesses();
        }
        Log.e(TAG, "removeFastAppWCPay failed because service has not been created");
        return Collections.emptyList();
    }

    public void setAppStartMonitorController(IOplusAppStartController watcher) throws RemoteException {
        if (getService() != null) {
            getService().setAppStartMonitorController(watcher);
        } else {
            Log.e(TAG, "setAppStartMonitorController failed because service has not been created");
        }
    }

    public void addFastAppThirdLogin(String callerPkg, String replacePkg) throws RemoteException {
        if (getService() != null) {
            getService().addFastAppThirdLogin(callerPkg, replacePkg);
        } else {
            Log.e(TAG, "addFastAppThirdLogin failed because service has not been created");
        }
    }

    public void removeFastAppThirdLogin(String callerPkg, String replacePkg) throws RemoteException {
        if (getService() != null) {
            getService().removeFastAppThirdLogin(callerPkg, replacePkg);
        } else {
            Log.e(TAG, "removeFastAppThirdLogin failed because service has not been created");
        }
    }

    public void favoriteQueryRule(String packageName, IOplusFavoriteQueryCallback callback) throws RemoteException {
        if (getService() != null) {
            getService().favoriteQueryRule(packageName, callback);
        } else {
            Log.e(TAG, "favoriteQueryRule failed because service has not been created");
        }
    }

    public void addBackgroundRestrictedInfo(String callerPkg, List<String> targetPkgList) throws RemoteException {
        if (getService() != null) {
            getService().addBackgroundRestrictedInfo(callerPkg, targetPkgList);
        } else {
            Log.e(TAG, "removeFastAppThirdLogin failed because service has not been created");
        }
    }

    public void setPreventIndulgeController(IOplusAppStartController controller) throws RemoteException {
        if (getService() != null) {
            getService().setPreventIndulgeController(controller);
        } else {
            Log.e(TAG, "setPreventIndulgeController failed because service has not been created");
        }
    }

    public void addPreventIndulgeList(List<String> pkgNames) throws RemoteException {
        if (getService() != null) {
            getService().addPreventIndulgeList(pkgNames);
        } else {
            Log.e(TAG, "addPreventIndulgeList failed because service has not been created");
        }
    }

    public boolean putConfigInfo(String configName, Bundle bundle, int flag, int userId) throws RemoteException {
        if (getService() != null) {
            return getService().putConfigInfo(configName, bundle, flag, userId);
        }
        Log.e(TAG, "putConfigInfo failed because service has not been created");
        return false;
    }

    public Bundle getConfigInfo(String configName, int flag, int userId) throws RemoteException {
        if (getService() != null) {
            return getService().getConfigInfo(configName, flag, userId);
        }
        Log.e(TAG, "putConfigInfo failed because service has not been created");
        return new Bundle();
    }

    public float updateCpuTracker(long lastUpdateTime) throws RemoteException {
        if (getService() != null) {
            return getService().updateCpuTracker(lastUpdateTime);
        }
        Log.e(TAG, "putConfigInfo failed because service has not been created");
        return 1.0f;
    }

    public List<String> getCpuWorkingStats() throws RemoteException {
        if (getService() != null) {
            return getService().getCpuWorkingStats();
        }
        Log.e(TAG, "putConfigInfo failed because service has not been created");
        return Collections.emptyList();
    }

    public SharedMemory getCpuLimitLatestInfos(String pkgName) throws RemoteException {
        if (getService() != null) {
            SharedMemory infos = getService().getCpuLimitLatestLogs(pkgName);
            return infos;
        }
        Log.e(TAG, "getCpuLimitLatestLogs failed because service has not been created");
        return null;
    }

    public long getCpuTotalLoad() throws RemoteException {
        if (getService() != null) {
            long percent = getService().getTotalCpuLoadPercent();
            return percent;
        }
        Log.e(TAG, "getTotalCpuLoadPercent failed because service has not been created");
        return 1L;
    }

    public List<String> getTopPidsLoadInfos(int topNum) throws RemoteException {
        List<String> list = new ArrayList<>();
        if (getService() != null) {
            List<String> list2 = getService().getTopLoadPidsInfos(topNum);
            return list2;
        }
        Log.e(TAG, "getTopLoadPidsInfos failed because service has not been created");
        return list;
    }

    public void forceTrimAppMemory(int level) throws RemoteException {
        if (getService() != null) {
            getService().forceTrimAppMemory(level);
        } else {
            Log.e(TAG, "forceTrimAppMemory failed because service has not been created");
        }
    }

    public void setPermissionRecordController(IOplusPermissionRecordController watcher) throws RemoteException {
        if (getService() != null) {
            getService().setPermissionRecordController(watcher);
        } else {
            Log.e(TAG, "setPermissionRecordController failed because service has not been created");
        }
    }

    public boolean isNightMode() throws RemoteException {
        if (getService() != null) {
            boolean success = getService().isNightMode();
            return success;
        }
        Log.e(TAG, "isNightMode failed because service has not been created");
        return false;
    }

    public OplusDarkModeData getDarkModeData(String packageName) throws RemoteException {
        if (getService() != null) {
            OplusDarkModeData darkModeData = getService().getDarkModeData(packageName);
            return darkModeData;
        }
        Log.e(TAG, "getDarkModeData failed because service has not been created");
        return null;
    }

    public boolean dumpProcPerfData(Bundle bundle) throws RemoteException {
        if (getService() != null) {
            return getService().dumpProcPerfData(bundle);
        }
        Log.e(TAG, "dumpProcPerfData failed because service has not been created");
        return false;
    }

    public List<String> getProcCommonInfoList(int type) throws RemoteException {
        if (getService() != null) {
            return getService().getProcCommonInfoList(type);
        }
        Log.e(TAG, "getProcCommonInfoList failed because service has not been created");
        return Collections.emptyList();
    }

    public List<OplusProcDependData> getProcDependency(int pid) throws RemoteException {
        if (getService() != null) {
            return getService().getProcDependency(pid);
        }
        Log.e(TAG, "getProcDependency failed because service has not been created");
        return null;
    }

    public List<OplusProcDependData> getProcDependency(String packageName, int userId) throws RemoteException {
        if (getService() != null) {
            return getService().getProcDependencyByUserId(packageName, userId);
        }
        Log.e(TAG, "getProcDependency failed because service has not been created");
        return null;
    }

    public List<String> getTaskPkgList(int taskId) throws RemoteException {
        if (getService() != null) {
            return getService().getTaskPkgList(taskId);
        }
        Log.e(TAG, "getTaskPkgList failed because service has not been created");
        return Collections.emptyList();
    }

    public void syncPermissionRecord() throws RemoteException {
        if (getService() != null) {
            getService().syncPermissionRecord();
        } else {
            Log.e(TAG, "syncPermissionRecord failed because service has not been created");
        }
    }

    public void updateUidCpuTracker() throws RemoteException {
        if (getService() != null) {
            getService().updateUidCpuTracker();
        } else {
            Log.e(TAG, "updateUidCpuTracker failed because service has not been created");
        }
    }

    public List<String> getUidCpuWorkingStats() throws RemoteException {
        if (getService() != null) {
            return getService().getUidCpuWorkingStats();
        }
        Log.e(TAG, "getUidCpuWorkingStats failed because service has not been created");
        return Collections.emptyList();
    }

    public List<String> getProcCmdline(int[] pids) throws RemoteException {
        if (getService() != null) {
            List<String> list = getService().getProcCmdline(pids);
            if (list != null && list.size() != 0) {
                return list;
            }
        } else {
            Log.e(TAG, "getProcCmdline failed because service has not been created");
        }
        return Collections.emptyList();
    }

    public void activeGc(int[] pids) throws RemoteException {
        if (getService() != null) {
            getService().activeGc(pids);
        } else {
            Log.e(TAG, "getProcCmdline failed because service has not been created");
        }
    }

    public void finishNotOrderReceiver(IBinder who, int hasCode, int resultCode, String resultData, Bundle resultExtras, boolean resultAbort) throws RemoteException {
        if (getService() != null) {
            getService().finishNotOrderReceiver(who, hasCode, resultCode, resultData, resultExtras, resultAbort);
        } else {
            Log.e(TAG, "getProcCmdline failed because service has not been created");
        }
    }

    public void reportSkippedFrames(long currentTime, long skippedFrames) throws RemoteException {
        reportSkippedFrames(currentTime, false, false, skippedFrames);
    }

    public void reportSkippedFrames(long currentTime, boolean isAnimation, boolean isForeground, long skippedFrames) throws RemoteException {
        if (getService() != null) {
            getService().reportSkippedFrames(currentTime, isAnimation, isForeground, skippedFrames);
        } else {
            Log.e(TAG, "reportSkippedFrames failed because service has not been created");
        }
    }

    public void reportSkippedFrames(long currentTime, boolean isAnimation, boolean isForeground, long skippedFrames, String pckName) throws RemoteException {
        if (getService() != null) {
            getService().reportSkippedFramesProcName(currentTime, isAnimation, isForeground, skippedFrames, pckName);
        } else {
            Log.e(TAG, "reportSkippedFrames failed because service has not been created");
        }
    }

    public void reportSkippedFrames(long currentTime, boolean isAnimation, boolean isForeground, long skippedFrames, String pckName, String activityName) throws RemoteException {
        if (getService() != null) {
            getService().reportSkippedFramesActivityName(currentTime, isAnimation, isForeground, skippedFrames, pckName, activityName);
        } else {
            Log.e(TAG, "reportSkippedFrames failed because service has not been created");
        }
    }

    public String queryProcessNameFromPid(int pid) throws RemoteException {
        if (getService() != null) {
            return getService().queryProcessNameFromPid(pid);
        }
        Log.e(TAG, "queryProcessNameFromPid failed because service has not been created");
        return "";
    }

    public void notifyAppKillReason(int pid, int uid, int reason, int subReason, String msg) throws RemoteException {
        if (getService() != null) {
            getService().notifyAppKillReason(pid, uid, reason, subReason, msg);
        } else {
            Log.e(TAG, "queryProcessNameFromPid failed because service has not been created");
        }
    }

    public int getFontVariationAdaptionData(String packagename) throws RemoteException {
        if (getService() != null) {
            return getService().getFontVariationAdaptionData(packagename);
        }
        Log.e(TAG, "getFontVariationAdaptionData failed because service has not been created");
        return 0;
    }

    public boolean registerHansListener(String callerPkg, IOplusHansListener listener) throws RemoteException {
        if (getService() != null) {
            return getService().registerHansListener(callerPkg, listener);
        }
        Log.e(TAG, "registerHansListener failed because service has not been created");
        return false;
    }

    public boolean unregisterHansListener(String callerPkg, IOplusHansListener listener) throws RemoteException {
        if (getService() != null) {
            return getService().unregisterHansListener(callerPkg, listener);
        }
        Log.e(TAG, "unregisterHansListener failed because service has not been created");
        return false;
    }

    public boolean setAppFreeze(String callerPkg, Bundle bundle) throws RemoteException {
        if (getService() != null) {
            return getService().setAppFreeze(callerPkg, bundle);
        }
        Log.e(TAG, "setAppFreeze failed because service has not been created");
        return false;
    }

    public void notifyAthenaOnekeyClearRunning(int state) throws RemoteException {
        if (getService() != null) {
            getService().notifyAthenaOnekeyClearRunning(state);
        } else {
            Log.e(TAG, "notifyAthenaOnekeyClearRunning failed because service has not been created");
        }
    }

    public void executeResPreload(String pkgName, int userId, int preloadType, String preloadReason) throws RemoteException {
        if (getService() != null) {
            getService().executeResPreload(pkgName, userId, preloadType, preloadReason);
        } else {
            Log.e(TAG, "executeResPreload failed because service has not been created");
        }
    }

    public Bundle getResPreloadInfo(int days, int userId) throws RemoteException {
        if (getService() != null) {
            return getService().getResPreloadInfo(days, userId);
        }
        Log.e(TAG, "getResPreloadInfo failed because service has not been created");
        return new Bundle();
    }

    public long getPreloadIOSize() throws RemoteException {
        if (getService() != null) {
            return getService().getPreloadIOSize();
        }
        Log.e(TAG, "getResPreloadInfo failed because service has not been created");
        return 0L;
    }

    public boolean getPreloadStatus(String pkgName, int userId) throws RemoteException {
        if (getService() != null) {
            return getService().getPreloadStatus(pkgName, userId);
        }
        Log.e(TAG, "getResPreloadInfo failed because service has not been created");
        return false;
    }

    public Bundle getPreloadPkgList() throws RemoteException {
        if (getService() != null) {
            return getService().getPreloadPkgList();
        }
        Log.e(TAG, "getPreloadPkgList failed because service has not been created");
        return new Bundle();
    }

    public List<String> getPkgPreloadFiles(String pkgName) throws RemoteException {
        if (getService() != null) {
            return getService().getPkgPreloadFiles(pkgName);
        }
        Log.e(TAG, "getPkgPreloadFiles failed because service has not been created");
        return Collections.emptyList();
    }

    public void enterFastFreezer(String callerPkg, int[] uids, long timeout, String reason) throws RemoteException {
        if (getService() != null) {
            getService().enterFastFreezer(callerPkg, uids, timeout, reason);
        } else {
            Log.e(TAG, "enterFastFreezer failed because service has not been created");
        }
    }

    public void exitFastFreezer(String callerPkg, String reason) throws RemoteException {
        if (getService() != null) {
            getService().exitFastFreezer(callerPkg, reason);
        } else {
            Log.e(TAG, "exitFastFreezer failed because service has not been created");
        }
    }

    public boolean isFrozenByHans(String packageName, int uid) throws RemoteException {
        if (getService() != null) {
            return getService().isFrozenByHans(packageName, uid);
        }
        Log.e(TAG, "isFrozenByHans failed because service has not been created");
        return false;
    }

    private SparseArray<Long> StringToSpareArrayLong(String spareArrayStr) {
        String[] spStrs;
        SparseArray<Long> sparray = new SparseArray<>();
        if (spareArrayStr.equals("empty") || TextUtils.isEmpty(spareArrayStr)) {
            return sparray;
        }
        try {
            spStrs = spareArrayStr.split(";");
        } catch (Exception e) {
        }
        if (spStrs == null) {
            return sparray;
        }
        for (String str : spStrs) {
            String[] keyValue = str.split("=");
            if (keyValue != null) {
                sparray.put(Integer.parseInt(keyValue[0]), Long.valueOf(Long.parseLong(keyValue[1])));
            }
        }
        return sparray;
    }

    public SparseArray<Long> getTrafficBytesList(ArrayList<Integer> uids) throws RemoteException {
        if (getService() != null) {
            Bundle inBundle = new Bundle();
            Bundle outBundle = new Bundle();
            inBundle.putIntegerArrayList("getTrafficBytesList", uids);
            String spareArrayStr = getService().getTrafficBytesList(inBundle, outBundle);
            return StringToSpareArrayLong(spareArrayStr);
        }
        Log.e(TAG, "getTrafficBytesList failed because service has not been created");
        return null;
    }

    public SparseArray<Long> getTrafficPacketList(ArrayList<Integer> uids) throws RemoteException {
        if (getService() != null) {
            Bundle inBundle = new Bundle();
            Bundle outBundle = new Bundle();
            inBundle.putIntegerArrayList("getTrafficPacketList", uids);
            String spareArrayStr = getService().getTrafficPacketList(inBundle, outBundle);
            return StringToSpareArrayLong(spareArrayStr);
        }
        Log.e(TAG, "getTrafficPacketList failed because service has not been created");
        return null;
    }

    public void registerEapDataCallback(IOplusEapDataCallback callback) throws RemoteException {
        if (getService() != null) {
            getService().registerEapDataCallback(callback);
        } else {
            Log.e(TAG, "registerEapDataCallback failed because service has not been created");
        }
    }

    public void unregisterEapDataCallback(IOplusEapDataCallback callback) throws RemoteException {
        if (getService() != null) {
            getService().unregisterEapDataCallback(callback);
        } else {
            Log.e(TAG, "unregisterEapDataCallback failed because service has not been created");
        }
    }

    public void updateANRDumpState(SharedMemory sharedMemory) throws RemoteException {
        if (getService() != null) {
            getService().updateANRDumpState(sharedMemory);
        } else {
            Log.e(TAG, "updateANRDumpState failed because service has not been created");
        }
    }

    public void registerAbConfigCallback(EventCallback callback, Context context) throws RemoteException {
        if (getService() != null && context != null) {
            getService().registerAbConfigCallback(callback, context.getPackageName());
        } else {
            Log.e(TAG, "registerAbConfigCallback failed because service has not been created");
        }
    }

    public void unregisterAbConfigCallback(EventCallback callback) throws RemoteException {
        if (getService() != null) {
            getService().unregisterAbConfigCallback(callback);
        } else {
            Log.e(TAG, "unregisterAbConfigCallback failed because service has not been created");
        }
    }

    public void registerErrorInfoCallback(IEventCallback callback) throws RemoteException {
        if (getService() != null) {
            getService().registerErrorInfoCallback(callback);
        } else {
            Log.e(TAG, "registerErrorInfoCallback failed because service has not been created");
        }
    }

    public void unregisterErrorInfoCallback(IEventCallback callback) throws RemoteException {
        if (getService() != null) {
            getService().unregisterErrorInfoCallback(callback);
        } else {
            Log.e(TAG, "unregisterErrorInfoCallback failed because service has not been created");
        }
    }

    public void clientTransactionComplete(IBinder token, int seq) throws RemoteException {
        this.mOplusAtm.clientTransactionComplete(token, seq);
    }

    public void updateUntrustedTouchConfig(String configData, boolean isRus) throws RemoteException {
        this.mOplusAtm.updateUntrustedTouchConfig(configData, isRus);
    }

    public boolean shouldInterceptBackKeyForMultiSearch(IBinder activityToken, boolean down) throws RemoteException {
        return this.mOplusAtm.shouldInterceptBackKeyForMultiSearch(activityToken, down);
    }

    public IOplusMultiSearchManagerSession getMultiSearchSession() throws RemoteException {
        return this.mOplusAtm.getMultiSearchSession();
    }

    public List<ActivityManager.RecentTaskInfo> getAllVisibleTasksInfo(int userId) throws RemoteException {
        if (getService() != null) {
            return getService().getAllVisibleTasksInfo(userId);
        }
        Log.e(TAG, "getTrafficPacketList failed because service has not been created");
        return Collections.emptyList();
    }

    public void anrViaTheiaEvent(int pid, String reason) throws RemoteException {
        if (getService() != null) {
            getService().anrViaTheiaEvent(pid, reason);
        } else {
            Log.e(TAG, "anrViaTheiaEvent failed because service has not been created");
        }
    }

    public boolean registerSecurityPageCallback(ISecurityPageController observer) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager registerSecurityPageCallback");
        return this.mOplusAtm.registerSecurityPageCallback(observer);
    }

    public boolean unregisterSecurityPageCallback(ISecurityPageController observer) throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager unregisterSecurityPageCallback");
        return this.mOplusAtm.unregisterSecurityPageCallback(observer);
    }

    public boolean getSecurityFlagCurrentPage() throws RemoteException {
        Slog.i("MirageDisplayWindow", "OplusActivityManager getSecurityFlagCurrentPage");
        return this.mOplusAtm.getSecurityFlagCurrentPage();
    }

    public List<String> getStageProtectListFromPkgAsUser(String pkg, int type, int userId) throws RemoteException {
        if (getService() != null) {
            return getService().getStageProtectListFromPkgAsUser(pkg, type, userId);
        }
        Log.e(TAG, "getStageProtectListFromPkgAsUser failed because service has not been created");
        return Collections.emptyList();
    }

    public List<String> getStageProtectList(int type) throws RemoteException {
        if (getService() != null) {
            return getService().getStageProtectList(type);
        }
        Log.e(TAG, "getStageProtectList failed because service has not been created");
        return Collections.emptyList();
    }

    public List<String> getStageProtectListAsUser(int type, int userId) throws RemoteException {
        if (getService() != null) {
            return getService().getStageProtectListAsUser(type, userId);
        }
        Log.e(TAG, "getStageProtectListAsUser failed because service has not been created");
        return Collections.emptyList();
    }

    public OplusUXIconData getUXIconData() throws RemoteException {
        if (getService() != null) {
            return getService().getUXIconData();
        }
        Log.e(TAG, "getStageProtectListAsUser failed because service has not been created");
        return null;
    }

    public void updateDeferStartingWindowApps(List<String> packages, boolean removeImmdiately) throws RemoteException {
        this.mOplusAtm.updateDeferStartingWindowApps(packages, removeImmdiately);
    }

    public String getAppThemeVersion(String pkgName, boolean change) throws RemoteException {
        return this.mOplusAtm.getAppThemeVersion(pkgName, change);
    }

    public void compactProcess(ArrayList<Integer> pids, int compactionFlags, int advice) throws RemoteException {
        if (getService() != null) {
            Bundle inBundle = new Bundle();
            inBundle.putIntegerArrayList("compactProcess", pids);
            getService().compactProcess(inBundle, compactionFlags, advice);
            return;
        }
        Log.e(TAG, "compactProcess failed because service has not been created");
    }

    public boolean inDownloading(int uid, int thresholdSpeed, boolean rough) throws RemoteException {
        if (getService() != null) {
            return getService().inDownloading(uid, thresholdSpeed, rough);
        }
        Log.e(TAG, "inDownloading failed because service has not been created");
        return false;
    }

    public List<OplusPackageFreezeData> getDownloadingList(int thresholdSpeed, boolean rough) throws RemoteException {
        if (getService() != null) {
            return getService().getDownloadingList(thresholdSpeed, rough);
        }
        Log.e(TAG, "getDownloadingList failed because service has not been created");
        return Collections.emptyList();
    }

    public List<UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime) throws RemoteException {
        if (getService() != null) {
            return getService().queryUsageStats(intervalType, beginTime, endTime);
        }
        Log.e(TAG, "queryUsageStats failed because service has not been created");
        return null;
    }

    public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessInfos(List<Integer> pids) throws RemoteException {
        if (getService() != null) {
            Bundle inBundle = new Bundle();
            ArrayList<Integer> temp = null;
            if (pids != null) {
                temp = new ArrayList<>();
                temp.addAll(pids);
            }
            inBundle.putIntegerArrayList("getRunningAppProcessInfos", temp);
            return getService().getRunningAppProcessInfos(inBundle);
        }
        Log.e(TAG, "getRunningAppProcessInfos failed because service has not been created");
        return Collections.emptyList();
    }

    public List<OplusPackageFreezeData> getPackageFreezeDataInfos(List<Integer> pids) throws RemoteException {
        if (getService() != null) {
            Bundle inBundle = new Bundle();
            ArrayList<Integer> temp = null;
            if (pids != null) {
                temp = new ArrayList<>();
                temp.addAll(pids);
            }
            inBundle.putIntegerArrayList("getPackageFreezeDataInfos", temp);
            return getService().getPackageFreezeDataInfos(inBundle);
        }
        Log.e(TAG, "getPackageFreezeDataInfos failed because service has not been created");
        return Collections.emptyList();
    }

    public void notifyUiSwitched(String uiInfo, int status) throws RemoteException {
        if (getService() != null) {
            getService().notifyUiSwitched(uiInfo, status);
        } else {
            Log.e(TAG, "notifyUiSwitched failed because service has not been created");
        }
    }

    public boolean addOrRemoveOplusVerifyCodeListener(boolean add, IOplusVerifyCodeListener listener) throws RemoteException {
        if (getService() != null) {
            return getService().addOrRemoveOplusVerifyCodeListener(add, listener);
        }
        Log.e(TAG, "addOrRemoveOplusVerifyCodeListener failed because service has not been created");
        return false;
    }

    public int getFocusMode() throws RemoteException {
        return this.mOplusAtm.getFocusMode();
    }

    public Rect getFocusBounds(boolean isPrimary) throws RemoteException {
        return this.mOplusAtm.getFocusBounds(isPrimary);
    }

    public ComponentName getFocusComponent(boolean isPrimary) throws RemoteException {
        return this.mOplusAtm.getFocusComponent(isPrimary);
    }

    public Point getRealSize() throws RemoteException {
        return this.mOplusAtm.getRealSize();
    }

    public Bundle callMethod(String method, String packageName, int param1, boolean param2, String param3, Bundle object) throws RemoteException {
        return this.mOplusAtm.callMethod(method, packageName, param1, param2, param3, object);
    }

    public Bundle invokeSync(String packageName, String method, String params, Bundle object) throws RemoteException {
        return this.mOplusAtm.invokeSync(packageName, method, params, object);
    }

    public int startCompactWindow() throws RemoteException {
        return this.mOplusAtm.startCompactWindow();
    }

    public int exitCompactWindow() throws RemoteException {
        return this.mOplusAtm.exitCompactWindow();
    }

    public boolean isCurrentAppSupportCompactMode() throws RemoteException {
        return this.mOplusAtm.isCurrentAppSupportCompactMode();
    }

    public int moveCompactWindowToLeft() throws RemoteException {
        return this.mOplusAtm.moveCompactWindowToLeft();
    }

    public int moveCompactWindowToRight() throws RemoteException {
        return this.mOplusAtm.moveCompactWindowToRight();
    }

    public boolean registerCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
        return this.mOplusAtm.registerCompactWindowObserver(observer);
    }

    public boolean unregisterCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
        return this.mOplusAtm.unregisterCompactWindowObserver(observer);
    }

    public Map<String, ArrayList<String>> getPWAppInfo() throws RemoteException {
        return this.mOplusAtm.getPWAppInfo();
    }

    public boolean onProtocolUpdated(String content) throws RemoteException {
        return this.mOplusAtm.onProtocolUpdated(content);
    }

    public boolean addBracketWindowConfigChangedListener(IOplusBracketModeChangedListener listener) throws RemoteException {
        return this.mOplusAtm.addBracketWindowConfigChangedListener(listener);
    }

    public boolean removeBracketWindowConfigChangedListener(IOplusBracketModeChangedListener listener) throws RemoteException {
        return this.mOplusAtm.removeBracketWindowConfigChangedListener(listener);
    }

    public boolean isNeedDisableWindowLayoutInfo(ComponentName componentName) throws RemoteException {
        return this.mOplusAtm.isNeedDisableWindowLayoutInfo(componentName);
    }

    public void bindQuickReplyService(IQuickReplyCallback callback) throws RemoteException {
        this.mOplusAtm.bindQuickReplyService(callback);
    }

    public void unbindQuickReplyService() throws RemoteException {
        this.mOplusAtm.unbindQuickReplyService();
    }

    public boolean sendMessage(PendingIntent weChatIntent, String message) throws RemoteException {
        return this.mOplusAtm.sendMessage(weChatIntent, message);
    }

    public void setHwuiTaskThreads(int pid, int tid) throws RemoteException {
        if (getService() != null) {
            getService().setHwuiTaskThreads(pid, tid);
        } else {
            Log.e(TAG, "setHwuiTaskThreads failed because service has not been created");
        }
    }

    public void setGlThreads(int glID, int tid) throws RemoteException {
        if (getService() != null) {
            getService().setGlThreads(glID, tid);
        } else {
            Log.e(TAG, "setGlThreads failed because service has not been created");
        }
    }

    public void onBackPressedOnTheiaMonitor(long pressNow) throws RemoteException {
        if (getService() != null) {
            getService().onBackPressedOnTheiaMonitor(pressNow);
        } else {
            Log.e(TAG, "onBackPressedOnTheiaMonitor failed because service has not been created");
        }
    }

    public void sendTheiaEvent(long category, Intent args) throws RemoteException {
        if (getService() != null) {
            getService().sendTheiaEvent(category, args);
        } else {
            Log.e(TAG, "sendTheiaEvent failed because service has not been created");
        }
    }

    public void reportBindApplicationFinished(String pkgName, int userId, int pid) throws RemoteException {
        if (getService() != null) {
            getService().reportBindApplicationFinished(pkgName, userId, pid);
        } else {
            Log.e(TAG, "reportBindApplicationFinished failed because service has not been created");
        }
    }

    public void unfreezeForKernel(int type, int callerPid, int targetUid, String rpcName, int code) throws RemoteException {
        if (getService() != null) {
            getService().unfreezeForKernel(type, callerPid, targetUid, rpcName, code);
        } else {
            Log.e(TAG, "unfreezeForKernel failed because service has not been created");
        }
    }

    public void asyncReportFrames(String packageName, int skippedFrames) throws RemoteException {
        if (getService() != null) {
            getService().asyncReportFrames(packageName, skippedFrames);
        } else {
            Log.e(TAG, "asyncReportFrames failed because service has not been created");
        }
    }

    public void unfreezeForKernelTargetPid(int type, int callerPid, int callerUid, int targetPid, int targetUid, String rpcName, int code) throws RemoteException {
        if (getService() != null) {
            getService().unfreezeForKernelTargetPid(type, callerPid, callerUid, targetPid, targetUid, rpcName, code);
        } else {
            Log.e(TAG, "unfreezeForKernelTargetPid failed because service has not been created");
        }
    }

    public void cleanPackageResources(String packageName, int uid) throws RemoteException {
        if (getService() != null) {
            getService().cleanPackageResources(packageName, uid);
        } else {
            Log.e(TAG, "cleanPackageResources failed because service has not been created");
        }
    }

    public void forceStopPackageAndSaveActivity(String packageName, int userId) throws RemoteException {
        if (getService() != null) {
            getService().forceStopPackageAndSaveActivity(packageName, userId);
        } else {
            Log.e(TAG, "forceStopPackageAndSaveActivity failed because service has not been created");
        }
    }

    public int[] getRunningPidsByUid(int uid) throws RemoteException {
        if (getService() != null) {
            return getService().getRunningPidsByUid(uid);
        }
        Log.e(TAG, "getRunningPidsByUid failed because service has not been created");
        return null;
    }

    public boolean requestDeviceFolded(int folded, boolean enableSecDisplay) throws RemoteException {
        if (getService() != null) {
            return getService().requestDeviceFolded(folded, enableSecDisplay);
        }
        Log.e(TAG, "requestDeviceFolded failed because service has not been created");
        return false;
    }

    public void sendFlingTransit(MotionEvent ev, int duration) throws RemoteException {
        if (getService() != null) {
            getService().sendFlingTransit(ev, duration);
        } else {
            Log.e(TAG, "sendFlingTransit failed because service has not been created");
        }
    }

    public void setSceneActionTransit(String scene, String action, int timeout) throws RemoteException {
        if (getService() != null) {
            getService().setSceneActionTransit(scene, action, timeout);
        } else {
            Log.e(TAG, "setSceneActionTransit failed because service has not been created");
        }
    }

    public IOplusCompatModeSession getCompatModeSession() throws RemoteException {
        if (getService() != null) {
            return getService().getCompatModeSession();
        }
        Log.e(TAG, "getCompatModeSession failed because service has not been created");
        return null;
    }

    public boolean registerTerminateObserver(OplusProcessTerminateObserver observer) {
        if (observer == null) {
            return false;
        }
        try {
            IProcessTerminateObserver iptObserver = IProcessTerminateObserver.Stub.asInterface(observer.asBinder());
            if (getService() != null) {
                return getService().registerTerminateObserver(iptObserver);
            }
            Log.e(TAG, "registerTerminateObserver failed because service has not been created");
            return false;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean unregisterTerminateObserver(OplusProcessTerminateObserver observer) {
        if (observer == null) {
            return false;
        }
        try {
            IProcessTerminateObserver iptObserver = IProcessTerminateObserver.Stub.asInterface(observer.asBinder());
            if (getService() != null) {
                return getService().unregisterTerminateObserver(iptObserver);
            }
            Log.e(TAG, "unregisterTerminateObserver failed because service has not been created");
            return false;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void notifyProcessTerminateFinish(OplusProcessTerminateObserver observer) {
        if (observer == null) {
            return;
        }
        try {
            IProcessTerminateObserver.Stub.asInterface(observer.asBinder());
            if (getService() != null) {
                getService().notifyProcessTerminateFinish(observer);
            } else {
                Log.e(TAG, "notifyProcessTerminateFinish failed because service has not been created");
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean registerTerminateObserver(IProcessTerminateObserver observer) throws RemoteException {
        if (getService() != null) {
            return getService().registerTerminateObserver(observer);
        }
        Log.e(TAG, "registerTerminateObserver failed because service has not been created");
        return false;
    }

    public boolean unregisterTerminateObserver(IProcessTerminateObserver observer) throws RemoteException {
        if (getService() != null) {
            return getService().unregisterTerminateObserver(observer);
        }
        Log.e(TAG, "unregisterTerminateObserver failed because service has not been created");
        return false;
    }

    public int[] getTerminateObservers() throws RemoteException {
        if (getService() != null) {
            return getService().getTerminateObservers();
        }
        Log.e(TAG, "getTerminateObservers failed because service has not been created");
        return null;
    }

    public void notifyProcessTerminate(int[] pids, String reason) throws RemoteException {
        if (getService() != null) {
            getService().notifyProcessTerminate(pids, reason);
        } else {
            Log.e(TAG, "notifyProcessTerminate failed because service has not been created");
        }
    }

    public boolean registerTerminateStateObserver(ITerminateObserver observer) throws RemoteException {
        if (getService() != null) {
            return getService().registerTerminateStateObserver(observer);
        }
        Log.e(TAG, "registerTerminateStateObserver failed because service has not been created");
        return false;
    }

    public boolean unregisterTerminateStateObserver(ITerminateObserver observer) throws RemoteException {
        if (getService() != null) {
            return getService().unregisterTerminateStateObserver(observer);
        }
        Log.e(TAG, "unregisterTerminateStateObserver failed because service has not been created");
        return false;
    }

    public void asyncReportDalvikMem(Bundle activityThread, int pid, long dalvikMax, long dalvikUsed, long uptime) throws RemoteException {
        if (getService() != null) {
            getService().asyncReportDalvikMem(activityThread, pid, dalvikMax, dalvikUsed, uptime);
        } else {
            Log.e(TAG, "asyncReportDalvikMem failed because service has not been created");
        }
    }

    public boolean registerComplexSceneObserver(Bundle bundle, OplusComplexSceneObserver observer) throws RemoteException {
        if (bundle == null || observer == null) {
            Log.w(TAG, "registerComplexSceneObserver failed! bundle or observer is null");
            return false;
        }
        if (getService() != null) {
            return getService().registerComplexSceneObserver(bundle, observer);
        }
        Log.w(TAG, "registerComplexSceneObserver failed because service has not been created");
        return false;
    }

    public boolean unregisterComplexSceneObserver(Bundle bundle, OplusComplexSceneObserver observer) throws RemoteException {
        if (bundle == null || observer == null) {
            Log.w(TAG, "unregisterComplexSceneObserver failed! bundle or observer is null");
            return false;
        }
        if (getService() != null) {
            return getService().unregisterComplexSceneObserver(bundle, observer);
        }
        Log.e(TAG, "unregisterComplexSceneObserver failed because service has not been created");
        return false;
    }

    public String getFastAppReplacePkg(String callerPkg) throws RemoteException {
        if (getService() != null) {
            return getService().getFastAppReplacePkg(callerPkg);
        }
        Log.e(TAG, "getFastAppReplacePkg failed because service has not been created");
        return null;
    }

    public int getLoopCpuLoad() throws RemoteException {
        if (getService() != null) {
            return getService().getLoopCpuLoad();
        }
        Log.e(TAG, "get Looper cpu load failed because service has not been created");
        return -1;
    }

    public void addOplusLoopLoadTime(long timeEnd, String msg) throws RemoteException {
        if (getService() != null) {
            getService().addOplusLoopLoadTime(timeEnd, msg);
        } else {
            Log.e(TAG, "get Looper cpu load failed because service has not been created");
        }
    }

    public Bundle autoLayoutCall(Bundle bundle) throws RemoteException {
        if (getService() != null) {
            return getService().autoLayoutCall(bundle);
        }
        Log.e(TAG, "autoLayoutCall failed because service has not been created");
        return null;
    }

    public void grantUriPermissionToUser(Context context, String targetPkg, Uri uri, int modeFlags, int userId) throws RemoteException {
        IApplicationThread caller = context.getIApplicationThread();
        if (getService() != null) {
            getService().grantUriPermissionToUser(caller, targetPkg, uri, modeFlags, userId);
        } else {
            Log.e(TAG, "grantUriPermissionToUser failed because service has not been created");
        }
    }

    public void trimSystemMemory(int level, boolean needGc) throws RemoteException {
        if (getService() != null) {
            getService().trimSystemMemory(level, needGc);
        } else {
            Log.e(TAG, "trimSystemMemory failed because service has not been created");
        }
    }

    public boolean setPayJoyFlag(int payjoyFlag) {
        try {
            return getService().setPayJoyFlag(1);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getPayJoyFlag() {
        try {
            return getService().getPayJoyFlag();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
