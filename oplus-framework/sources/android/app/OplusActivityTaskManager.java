package android.app;

import android.app.ActivityManager;
import android.app.IOplusActivityTaskManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IOplusKeyEventObserver;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Singleton;
import android.view.IRecentsAnimationController;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.window.TaskSnapshot;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import com.oplus.app.IOplusAppSwitchObserver;
import com.oplus.app.IOplusFreeformConfigChangedListener;
import com.oplus.app.IOplusSplitScreenObserver;
import com.oplus.app.IOplusZoomWindowConfigChangedListener;
import com.oplus.app.ISecurityPageController;
import com.oplus.app.OplusAppInfo;
import com.oplus.app.OplusAppSwitchConfig;
import com.oplus.bracket.IOplusBracketModeChangedListener;
import com.oplus.compactwindow.IOplusCompactWindowObserver;
import com.oplus.confinemode.IOplusConfineModeObserver;
import com.oplus.datasync.ISysStateChangeCallback;
import com.oplus.flexiblewindow.IEmbeddedWindowContainerCallback;
import com.oplus.flexiblewindow.IFlexibleWindowObserver;
import com.oplus.globaldrag.IDragAndDropListener;
import com.oplus.globaldrag.OplusGlobalDragAndDropRUSConfig;
import com.oplus.lockscreen.IOplusLockScreenCallback;
import com.oplus.miragewindow.IOplusMirageDisplayObserver;
import com.oplus.miragewindow.IOplusMirageSessionCallback;
import com.oplus.miragewindow.IOplusMirageWindowObserver;
import com.oplus.miragewindow.IOplusMirageWindowSession;
import com.oplus.miragewindow.OplusMirageWindowInfo;
import com.oplus.multisearch.IOplusMultiSearchManagerSession;
import com.oplus.os.IOplusTouchNodeCallback;
import com.oplus.putt.IPuttEventObserver;
import com.oplus.putt.IPuttObserver;
import com.oplus.putt.OplusPuttEnterInfo;
import com.oplus.putt.PuttParams;
import com.oplus.quickreply.IQuickReplyCallback;
import com.oplus.splitscreen.IOplusSplitScreenSession;
import com.oplus.zoomwindow.IOplusZoomAppObserver;
import com.oplus.zoomwindow.IOplusZoomTaskController;
import com.oplus.zoomwindow.IOplusZoomWindowObserver;
import com.oplus.zoomwindow.OplusZoomControlViewInfo;
import com.oplus.zoomwindow.OplusZoomFloatHandleViewInfo;
import com.oplus.zoomwindow.OplusZoomInputEventInfo;
import com.oplus.zoomwindow.OplusZoomWindowInfo;
import com.oplus.zoomwindow.OplusZoomWindowRUSConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusActivityTaskManager {
    private static final Singleton<OplusActivityTaskManager> sInstance = new Singleton<OplusActivityTaskManager>() { // from class: android.app.OplusActivityTaskManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public OplusActivityTaskManager m10create() {
            return new OplusActivityTaskManager();
        }
    };
    private static final Singleton<IOplusActivityTaskManager> IOplusActivityTaskManagerSingleton = new Singleton<IOplusActivityTaskManager>() { // from class: android.app.OplusActivityTaskManager.2
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusActivityTaskManager m11create() {
            try {
                IOplusActivityTaskManager oplusActivityTaskManager = IOplusActivityTaskManager.Stub.asInterface(ServiceManager.getService("activity_task").getExtension());
                return oplusActivityTaskManager;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    };

    @Deprecated
    public OplusActivityTaskManager() {
    }

    public static OplusActivityTaskManager getInstance() {
        return (OplusActivityTaskManager) sInstance.get();
    }

    public static IOplusActivityTaskManager getService() {
        return (IOplusActivityTaskManager) IOplusActivityTaskManagerSingleton.get();
    }

    public void setSecureController(IActivityController watcher) throws RemoteException {
        getService().setSecureController(watcher);
    }

    public ComponentName getTopActivityComponentName() throws RemoteException {
        return getService().getTopActivityComponentName();
    }

    public ApplicationInfo getTopApplicationInfo() throws RemoteException {
        return getService().getTopApplicationInfo();
    }

    public void swapDockedFullscreenStack() throws RemoteException {
    }

    public int getSplitScreenState(Intent intent) throws RemoteException {
        return getService().getSplitScreenState(intent);
    }

    public List<OplusAppInfo> getAllTopAppInfos() throws RemoteException {
        return getService().getAllTopAppInfos();
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
        return getService().registerAppSwitchObserver(pkgName, observer, config);
    }

    public boolean unregisterAppSwitchObserver(String pkgName, OplusAppSwitchConfig config) throws RemoteException {
        return getService().unregisterAppSwitchObserver(pkgName, config);
    }

    public void registerDragAndDropListener(String pkgName, IDragAndDropListener listener) throws RemoteException {
        getService().registerDragAndDropListener(pkgName, listener);
    }

    public void unregisterDragAndDropListener(String pkgName, IDragAndDropListener listener) throws RemoteException {
        getService().unregisterDragAndDropListener(pkgName, listener);
    }

    public int startZoomWindow(Intent intent, Bundle options, int userId, String callPkg) throws RemoteException {
        return getService().startZoomWindow(intent, options, userId, callPkg);
    }

    public boolean registerZoomWindowObserver(IOplusZoomWindowObserver observer) throws RemoteException {
        return getService().registerZoomWindowObserver(observer);
    }

    public boolean unregisterZoomWindowObserver(IOplusZoomWindowObserver observer) throws RemoteException {
        return getService().unregisterZoomWindowObserver(observer);
    }

    public OplusZoomWindowInfo getCurrentZoomWindowState() throws RemoteException {
        return getService().getCurrentZoomWindowState();
    }

    public void hideZoomWindow(int flag) throws RemoteException {
        getService().hideZoomWindow(flag);
    }

    public List<String> getZoomAppConfigList(int type) throws RemoteException {
        return getService().getZoomAppConfigList(type);
    }

    public void onInputEvent(OplusZoomInputEventInfo inputEventInfo) throws RemoteException {
        getService().onInputEvent(inputEventInfo);
    }

    public void onControlViewChanged(OplusZoomControlViewInfo cvInfo) throws RemoteException {
        getService().onControlViewChanged(cvInfo);
    }

    public boolean isSupportZoomWindowMode() throws RemoteException {
        return getService().isSupportZoomWindowMode();
    }

    public boolean isSupportZoomMode(String target, int userId, String callPkg, Bundle extension) throws RemoteException {
        return getService().isSupportZoomMode(target, userId, callPkg, extension);
    }

    public OplusZoomWindowRUSConfig getZoomWindowConfig() throws RemoteException {
        return getService().getZoomWindowConfig();
    }

    public void setZoomWindowConfig(OplusZoomWindowRUSConfig config) throws RemoteException {
        getService().setZoomWindowConfig(config);
    }

    public void onFloatHandleViewChanged(OplusZoomFloatHandleViewInfo floatHandleInfo) throws RemoteException {
        getService().onFloatHandleViewChanged(floatHandleInfo);
    }

    public IOplusZoomTaskController getZoomTaskController() throws RemoteException {
        return getService().getZoomTaskController();
    }

    public int getRenderThreadTid(int pid) throws RemoteException {
        return getService().getRenderThreadTid(pid);
    }

    public boolean setInputConsumerEnabled(boolean enabled, IRecentsAnimationController controller) throws RemoteException {
        return getService().setInputConsumerEnabled(enabled, controller);
    }

    public void adjustWindowFrameForZoom(WindowManager.LayoutParams attrs, int windowingMode, Rect outDisplayFrame, Rect outParentFrame) throws RemoteException {
        getService().adjustWindowFrameForZoom(attrs, windowingMode, outDisplayFrame, outParentFrame);
    }

    public boolean isZoomSupportMultiWindow(String packageName, ComponentName componentName) throws RemoteException {
        return getService().isZoomSupportMultiWindow(packageName, componentName);
    }

    public void updateUntrustedTouchConfig(String configData, boolean isRus) throws RemoteException {
        getService().updateUntrustedTouchConfig(configData, isRus);
    }

    public boolean handleShowCompatibilityToast(String target, int userId, String callPkg, Bundle extension, int type) throws RemoteException {
        return getService().handleShowCompatibilityToast(target, userId, callPkg, extension, type);
    }

    public void startMiniZoomFromZoom(int startWay) throws RemoteException {
        getService().startMiniZoomFromZoom(startWay);
    }

    public boolean isZoomSimpleModeEnable() throws RemoteException {
        return getService().isZoomSimpleModeEnable();
    }

    public void notifyZoomStateChange(String packageName, int action) throws RemoteException {
        getService().notifyZoomStateChange(packageName, action);
    }

    public boolean addZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener listener) throws RemoteException {
        return getService().addZoomWindowConfigChangedListener(listener);
    }

    public boolean removeZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener listener) throws RemoteException {
        return getService().addZoomWindowConfigChangedListener(listener);
    }

    public void startLockDeviceMode(String rootPkg, String[] packages) throws RemoteException {
        getService().startLockDeviceMode(rootPkg, packages);
    }

    public void stopLockDeviceMode() throws RemoteException {
        getService().stopLockDeviceMode();
    }

    public boolean isLockDeviceMode() throws RemoteException {
        return getService().isLockDeviceMode();
    }

    public boolean writeEdgeTouchPreventParam(String callPkg, String scenePkg, List<String> paramCmdList) throws RemoteException {
        return getService().writeEdgeTouchPreventParam(callPkg, scenePkg, paramCmdList);
    }

    public void setDefaultEdgeTouchPreventParam(String callPkg, List<String> paramCmdList) throws RemoteException {
        getService().setDefaultEdgeTouchPreventParam(callPkg, paramCmdList);
    }

    public boolean resetDefaultEdgeTouchPreventParam(String callPkg) throws RemoteException {
        return getService().resetDefaultEdgeTouchPreventParam(callPkg);
    }

    public boolean isSupportEdgeTouchPrevent() throws RemoteException {
        return getService().isSupportEdgeTouchPrevent();
    }

    public void setEdgeTouchCallRules(String callPkg, Map<String, List<String>> rulesMap) throws RemoteException {
        getService().setEdgeTouchCallRules(callPkg, rulesMap);
    }

    public int splitScreenForEdgePanel(Intent intent, int userId) throws RemoteException {
        return getService().splitScreenForEdgePanel(intent, userId);
    }

    public boolean splitScreenForApplication(Context context, Intent intent, int position) throws RemoteException {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 1140850688, null);
        return getService().splitScreenForApplication(pendingIntent, position);
    }

    public void setConfineMode(int mode, boolean on) throws RemoteException {
        getService().setConfineMode(mode, on, null);
    }

    public void setConfineModeAsLink(int mode, boolean on, IBinder token) throws RemoteException {
        getService().setConfineMode(mode, on, token);
    }

    public int getConfineMode() throws RemoteException {
        return getService().getConfineMode();
    }

    public void setPermitList(int mode, int type, List<String> permits, boolean isMultiApp) throws RemoteException {
        getService().setPermitList(mode, type, permits, isMultiApp);
    }

    public void setGimbalLaunchPkg(String pkgName) throws RemoteException {
        getService().setGimbalLaunchPkg(pkgName);
    }

    public void setPackagesState(Map<String, Integer> packageMap) throws RemoteException {
        getService().setPackagesState(packageMap);
    }

    public boolean registerLockScreenCallback(IOplusLockScreenCallback callback) throws RemoteException {
        return getService().registerLockScreenCallback(callback);
    }

    public boolean unregisterLockScreenCallback(IOplusLockScreenCallback callback) throws RemoteException {
        return getService().unregisterLockScreenCallback(callback);
    }

    public void notifySplitScreenStateChanged(String event, Bundle bundle, boolean broadcast) throws RemoteException {
        getService().notifySplitScreenStateChanged(event, bundle, broadcast);
    }

    public boolean setSplitScreenObserver(IOplusSplitScreenObserver observer) throws RemoteException {
        return getService().setSplitScreenObserver(observer);
    }

    public boolean isInSplitScreenMode() throws RemoteException {
        return getService().isInSplitScreenMode();
    }

    public boolean dismissSplitScreenMode(int type) throws RemoteException {
        return getService().dismissSplitScreenMode(type);
    }

    public boolean registerSplitScreenObserver(int observerId, IOplusSplitScreenObserver observer) throws RemoteException {
        return getService().registerSplitScreenObserver(observerId, observer);
    }

    public boolean unregisterSplitScreenObserver(int observerId, IOplusSplitScreenObserver observer) throws RemoteException {
        return getService().unregisterSplitScreenObserver(observerId, observer);
    }

    public Bundle getSplitScreenStatus(String event) throws RemoteException {
        return getService().getSplitScreenStatus(event);
    }

    public boolean splitScreenForTopApp(int type) throws RemoteException {
        return getService().splitScreenForTopApp(type);
    }

    public boolean splitScreenForRecentTasks(int taskId) throws RemoteException {
        return getService().splitScreenForRecentTasks(taskId);
    }

    public int setTaskWindowingModeSplitScreen(int taskId) throws RemoteException {
        return getService().setTaskWindowingModeSplitScreen(taskId);
    }

    public boolean splitScreenForEdgePanelExt(Intent intent, boolean launchToPrimary, int launchArea) throws RemoteException {
        return getService().splitScreenForEdgePanelExt(intent, launchToPrimary, launchArea);
    }

    public boolean hasLargeScreenFeature() throws RemoteException {
        return getService().hasLargeScreenFeature();
    }

    public boolean isFolderInnerScreen() throws RemoteException {
        return getService().isFolderInnerScreen();
    }

    public Rect getMinimizedBounds(int dockedSide) throws RemoteException {
        return getService().getMinimizedBounds(dockedSide);
    }

    public TaskSnapshot getEmbeddedChildrenSnapshot(int taskId, boolean isLowResolution, boolean takeSnapshotIfNeeded) throws RemoteException {
        return getService().getEmbeddedChildrenSnapshot(taskId, isLowResolution, takeSnapshotIfNeeded);
    }

    public IOplusSplitScreenSession getSplitScreenSession() throws RemoteException {
        return getService().getSplitScreenSession();
    }

    public Bundle getLeftRightBoundsForIme() throws RemoteException {
        return getService().getLeftRightBoundsForIme();
    }

    public boolean hasColorSplitFeature() throws RemoteException {
        return getService().hasColorSplitFeature();
    }

    public boolean registerKeyEventObserver(String observerFingerPrint, IOplusKeyEventObserver observer, int listenFlag) throws RemoteException {
        return getService().registerKeyEventObserver(observerFingerPrint, observer, listenFlag);
    }

    public boolean unregisterKeyEventObserver(String observerFingerPrint) throws RemoteException {
        return getService().unregisterKeyEventObserver(observerFingerPrint);
    }

    public boolean updateAppData(String module, Bundle bundle) throws RemoteException {
        return getService().updateAppData(module, bundle);
    }

    public boolean registerSysStateChangeObserver(String module, ISysStateChangeCallback callback) throws RemoteException {
        return getService().registerSysStateChangeObserver(module, callback);
    }

    public boolean unregisterSysStateChangeObserver(String module, ISysStateChangeCallback callback) throws RemoteException {
        return getService().unregisterSysStateChangeObserver(module, callback);
    }

    public boolean registerKeyEventInterceptor(String interceptorFingerPrint, IOplusKeyEventObserver observer, Map<Integer, Integer> configs) throws RemoteException {
        return getService().registerKeyEventInterceptor(interceptorFingerPrint, observer, configs);
    }

    public boolean unregisterKeyEventInterceptor(String interceptorFingerPrint) throws RemoteException {
        return getService().unregisterKeyEventInterceptor(interceptorFingerPrint);
    }

    public OplusGlobalDragAndDropRUSConfig getGlobalDragAndDropConfig() throws RemoteException {
        new OplusGlobalDragAndDropRUSConfig();
        OplusGlobalDragAndDropRUSConfig config = getService().getGlobalDragAndDropConfig();
        return config;
    }

    public void setGlobalDragAndDropConfig(OplusGlobalDragAndDropRUSConfig config) throws RemoteException {
        getService().setGlobalDragAndDropConfig(config);
    }

    public boolean registerMirageWindowObserver(IOplusMirageWindowObserver observer) throws RemoteException {
        return getService().registerMirageWindowObserver(observer);
    }

    public boolean unregisterMirageWindowObserver(IOplusMirageWindowObserver observer) throws RemoteException {
        return getService().unregisterMirageWindowObserver(observer);
    }

    public boolean registerMirageDisplayObserver(IOplusMirageDisplayObserver observer) throws RemoteException {
        return getService().registerMirageDisplayObserver(observer);
    }

    public boolean unregisterMirageDisplayObserver(IOplusMirageDisplayObserver observer) throws RemoteException {
        return getService().unregisterMirageDisplayObserver(observer);
    }

    public void feedbackUserSelection(int eventId, int selection, Bundle extension) throws RemoteException {
        getService().feedbackUserSelection(eventId, selection, extension);
    }

    public boolean updateCarModeMultiLaunchWhiteList(String list) throws RemoteException {
        return getService().updateCarModeMultiLaunchWhiteList(list);
    }

    public int createMirageDisplay(Surface surface) throws RemoteException {
        return getService().createMirageDisplay(surface);
    }

    public Bundle getTaskInfo(ComponentName name) throws RemoteException {
        return getService().getTaskInfo(name);
    }

    public void startMirageWindowModeWithName(ComponentName cpnName, int taskId, int flags, Bundle options) throws RemoteException {
        getService().startMirageWindowModeWithName(cpnName, taskId, flags, options);
    }

    public int startMirageWindowMode(Intent intent, Bundle options) throws RemoteException {
        return getService().startMirageWindowMode(intent, options);
    }

    public boolean isMirageWindowShow() throws RemoteException {
        return getService().isMirageWindowShow();
    }

    public void stopMirageWindowModeOld() throws RemoteException {
        getService().stopMirageWindowModeOld();
    }

    public void stopMirageWindowMode(Bundle options) throws RemoteException {
        getService().stopMirageWindowMode(options);
    }

    public void setMirageDisplaySurfaceById(int displayId, Surface surface) throws RemoteException {
        getService().setMirageDisplaySurfaceById(displayId, surface);
    }

    public void setMirageDisplaySurfaceByMode(int mode, Surface surface) throws RemoteException {
        getService().setMirageDisplaySurfaceByMode(mode, surface);
    }

    public int getMirageDisplayCastMode(int displayId) throws RemoteException {
        return getService().getMirageDisplayCastMode(displayId);
    }

    public void expandToFullScreen() throws RemoteException {
        getService().expandToFullScreen();
    }

    public void setMirageWindowSilent(String pkgName) throws RemoteException {
        getService().setMirageWindowSilent(pkgName);
    }

    public boolean isSupportMirageWindowMode() throws RemoteException {
        return getService().isSupportMirageWindowMode();
    }

    public OplusMirageWindowInfo getMirageWindowInfo() throws RemoteException {
        new OplusMirageWindowInfo();
        OplusMirageWindowInfo info = getService().getMirageWindowInfo();
        return info;
    }

    public boolean updateMirageWindowCastFlag(int castFlag, Bundle options) throws RemoteException {
        return getService().updateMirageWindowCastFlag(castFlag, options);
    }

    public boolean updatePrivacyProtectionList(List<String> name, boolean replace) throws RemoteException {
        return getService().updatePrivacyProtectionList(name, replace);
    }

    public boolean updatePrivacyProtectionListWithBundle(List<String> name, boolean append, boolean isDefault, Bundle options) throws RemoteException {
        return getService().updatePrivacyProtectionListWithBundle(name, append, isDefault, options);
    }

    public IOplusMirageWindowSession createMirageWindowSession(IOplusMirageSessionCallback callback) throws RemoteException {
        return getService().createMirageWindowSession(callback);
    }

    public int getGetDisplayIdForPackageName(String packageName) throws RemoteException {
        return getService().getGetDisplayIdForPackageName(packageName);
    }

    public boolean rebindDisplayIfNeeded(int castDisplayId, int mirageDisplayId) throws RemoteException {
        return getService().rebindDisplayIfNeeded(castDisplayId, mirageDisplayId);
    }

    public boolean registerConfineModeObserver(IOplusConfineModeObserver observer) throws RemoteException {
        return getService().registerConfineModeObserver(observer);
    }

    public boolean unregisterConfineModeObserver(IOplusConfineModeObserver observer) throws RemoteException {
        return getService().unregisterConfineModeObserver(observer);
    }

    public String readNodeFile(int nodeFlag) throws RemoteException {
        return getService().readNodeFile(nodeFlag);
    }

    public String readNodeFileByDevice(int deviceId, int nodeFlag) throws RemoteException {
        return getService().readNodeFileByDevice(deviceId, nodeFlag);
    }

    public boolean writeNodeFile(int nodeFlag, String info) throws RemoteException {
        return getService().writeNodeFile(nodeFlag, info);
    }

    public boolean writeNodeFileByDevice(int deviceId, int nodeFlag, String info) throws RemoteException {
        return getService().writeNodeFileByDevice(deviceId, nodeFlag, info);
    }

    public boolean isTouchNodeSupport(int deviceId, int nodeFlag) throws RemoteException {
        return getService().isTouchNodeSupport(deviceId, nodeFlag);
    }

    public boolean writeNodeFileFromBt(int deviceId, int nodeFlag, String info) throws RemoteException {
        return getService().writeNodeFileFromBt(deviceId, nodeFlag, info);
    }

    public void writeNodeFileOneWay(int deviceId, int nodeFlag, String info) throws RemoteException {
        getService().writeNodeFileOneWay(deviceId, nodeFlag, info);
    }

    public boolean notifyTouchNodeChange(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) throws RemoteException {
        return getService().notifyTouchNodeChange(clientFlag, time, deviceId, nodeFlag, data, info);
    }

    public boolean registerEventCallback(IOplusTouchNodeCallback callback) throws RemoteException {
        return getService().registerEventCallback(callback);
    }

    public boolean unregisterEventCallback(IOplusTouchNodeCallback callback) throws RemoteException {
        return getService().unregisterEventCallback(callback);
    }

    public void clientTransactionComplete(IBinder token, int seq) throws RemoteException {
        getService().clientTransactionComplete(token, seq);
    }

    public boolean registerZoomAppObserver(IOplusZoomAppObserver observer) throws RemoteException {
        return getService().registerZoomAppObserver(observer);
    }

    public boolean unregisterZoomAppObserver(IOplusZoomAppObserver observer) throws RemoteException {
        return getService().unregisterZoomAppObserver(observer);
    }

    public boolean registerFlexibleWindowObserver(IFlexibleWindowObserver observer) throws RemoteException {
        return getService().registerFlexibleWindowObserver(observer);
    }

    public boolean unregisterFlexibleWindowObserver(IFlexibleWindowObserver observer) throws RemoteException {
        return getService().unregisterFlexibleWindowObserver(observer);
    }

    public Bundle calculateFlexibleWindowBounds(Intent intent, int appOrientation, int displayId) throws RemoteException {
        return getService().calculateFlexibleWindowBounds(intent, appOrientation, displayId);
    }

    public boolean isInPocketStudio(int displayId) throws RemoteException {
        return getService().isInPocketStudio(displayId);
    }

    public void setMinimizedPocketStudio(boolean minimized, int displayId) throws RemoteException {
        getService().setMinimizedPocketStudio(minimized, displayId);
    }

    public boolean isMinimizedPocketStudio(int displayId) throws RemoteException {
        return getService().isMinimizedPocketStudio(displayId);
    }

    public boolean isInPocketStudioForStandard(int displayId) throws RemoteException {
        return getService().isInPocketStudioForStandard(displayId);
    }

    public Map<Integer, Rect> getPocketStudioTaskRegion(int displayId) throws RemoteException {
        return getService().getPocketStudioTaskRegion(displayId);
    }

    public void setFlexibleFrame(int taskId, Bundle bundle) throws RemoteException {
        getService().setFlexibleFrame(taskId, bundle);
    }

    public void setIsInFocusAnimating(boolean isInFocusAnimating) throws RemoteException {
        getService().setIsInFocusAnimating(isInFocusAnimating);
    }

    public void notifyEmbeddedTasksChangeFocus(boolean isStateSteady, int containerTaskId) throws RemoteException {
        getService().notifyEmbeddedTasksChangeFocus(isStateSteady, containerTaskId);
    }

    public void notifyFlexibleSplitScreenStateChanged(String event, Bundle bundle, int containerTaskId) throws RemoteException {
        getService().notifyFlexibleSplitScreenStateChanged(event, bundle, containerTaskId);
    }

    public void setEmbeddedContainerTask(int embeddedTaskId, int containerTaskId) throws RemoteException {
        getService().setEmbeddedContainerTask(embeddedTaskId, containerTaskId);
    }

    public void updateTaskVisibility(WindowContainerToken windowContainerToken, int containerTaskId, boolean visible) throws RemoteException {
        getService().updateTaskVisibility(windowContainerToken, containerTaskId, visible);
    }

    public void removeEmbeddedContainerTask(int embeddedTaskId, int containerTaskId) throws RemoteException {
        getService().removeEmbeddedContainerTask(embeddedTaskId, containerTaskId);
    }

    public void exitFlexibleEmbeddedTask(int embeddedTaskId) throws RemoteException {
        getService().exitFlexibleEmbeddedTask(embeddedTaskId);
    }

    public void setFlexibleTaskEmbedding(int embeddedTaskId, boolean state) throws RemoteException {
        getService().setFlexibleTaskEmbedding(embeddedTaskId, state);
    }

    public void resetFlexibleTask(int embeddedTaskId, boolean needResize, boolean doAnimation) throws RemoteException {
        getService().resetFlexibleTask(embeddedTaskId, needResize, doAnimation);
    }

    public List<ActivityManager.RecentTaskInfo> getRecentEmbeddedTasksForContainer(int containerTaskId) throws RemoteException {
        return getService().getRecentEmbeddedTasksForContainer(containerTaskId);
    }

    public void startActivityInTask(Intent intent, int taskId) throws RemoteException {
        getService().startActivityInTask(intent, taskId);
    }

    public int startAnyActivity(Intent intent, Bundle bundle) throws RemoteException {
        return getService().startAnyActivity(intent, bundle);
    }

    public boolean startFlexibleWindowForRecentTasks(int taskId, Rect rect) throws RemoteException {
        return getService().startFlexibleWindowForRecentTasks(taskId, rect);
    }

    public boolean isClickAtPocketStudioArea(int displayId, int rowX, int rowY) throws RemoteException {
        return getService().isClickAtPocketStudioArea(displayId, rowX, rowY);
    }

    public Bundle getActivityConfigs(IBinder token, String packageName) throws RemoteException {
        return getService().getActivityConfigs(token, packageName);
    }

    public boolean registerSecurityPageCallback(ISecurityPageController observer) throws RemoteException {
        return getService().registerSecurityPageCallback(observer);
    }

    public boolean unregisterSecurityPageCallback(ISecurityPageController observer) throws RemoteException {
        return getService().unregisterSecurityPageCallback(observer);
    }

    public boolean getSecurityFlagCurrentPage() throws RemoteException {
        return getService().getSecurityFlagCurrentPage();
    }

    public void updateDeferStartingWindowApps(List<String> packages, boolean removeImmdiately) throws RemoteException {
        getService().updateDeferStartingWindowApps(packages, removeImmdiately);
    }

    public String getAppThemeVersion(String pkgName, boolean change) throws RemoteException {
        return getService().getAppThemeVersion(pkgName, change);
    }

    public boolean lockRotationInGame(int state, String[] packages) throws RemoteException {
        return getService().lockRotationInGame(state, packages);
    }

    public boolean shouldInterceptBackKeyForMultiSearch(IBinder activityToken, boolean down) throws RemoteException {
        return getService().shouldInterceptBackKeyForMultiSearch(activityToken, down);
    }

    public IOplusMultiSearchManagerSession getMultiSearchSession() throws RemoteException {
        return getService().getMultiSearchSession();
    }

    public SurfaceControl takeScreenshot(WindowContainerToken windowContainerToken, SurfaceControl outSurfaceControl) throws RemoteException {
        return getService().takeScreenshot(windowContainerToken, outSurfaceControl);
    }

    public int startCompactWindow() throws RemoteException {
        return getService().startCompactWindow();
    }

    public int exitCompactWindow() throws RemoteException {
        return getService().exitCompactWindow();
    }

    public boolean isCurrentAppSupportCompactMode() throws RemoteException {
        return getService().isCurrentAppSupportCompactMode();
    }

    public int moveCompactWindowToLeft() throws RemoteException {
        return getService().moveCompactWindowToLeft();
    }

    public int moveCompactWindowToRight() throws RemoteException {
        return getService().moveCompactWindowToRight();
    }

    public boolean registerCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
        return getService().registerCompactWindowObserver(observer);
    }

    public boolean unregisterCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
        return getService().unregisterCompactWindowObserver(observer);
    }

    public Map<String, ArrayList<String>> getPWAppInfo() throws RemoteException {
        return getService().getPWAppInfo();
    }

    public boolean onProtocolUpdated(String content) throws RemoteException {
        return getService().onProtocolUpdated(content);
    }

    public int getFocusMode() throws RemoteException {
        return getService().getFocusMode();
    }

    public Rect getFocusBounds(boolean isPrimary) throws RemoteException {
        return getService().getFocusBounds(isPrimary);
    }

    public ComponentName getFocusComponent(boolean isPrimary) throws RemoteException {
        return getService().getFocusComponent(isPrimary);
    }

    public Point getRealSize() throws RemoteException {
        return getService().getRealSize();
    }

    public Bundle callMethod(String method, String packageName, int param1, boolean param2, String param3, Bundle object) throws RemoteException {
        return getService().callMethod(method, packageName, param1, param2, param3, object);
    }

    public Bundle invokeSync(String packageName, String method, String params, Bundle objects) throws RemoteException {
        return getService().invokeSync(packageName, method, params, objects);
    }

    public void bindQuickReplyService(IQuickReplyCallback callback) throws RemoteException {
        getService().bindQuickReplyService(callback);
    }

    public void unbindQuickReplyService() throws RemoteException {
        getService().unbindQuickReplyService();
    }

    public boolean sendMessage(PendingIntent weChatIntent, String message) throws RemoteException {
        return getService().sendMessage(weChatIntent, message);
    }

    public boolean addBracketWindowConfigChangedListener(IOplusBracketModeChangedListener listener) throws RemoteException {
        return getService().addBracketWindowConfigChangedListener(listener);
    }

    public boolean removeBracketWindowConfigChangedListener(IOplusBracketModeChangedListener listener) throws RemoteException {
        return getService().removeBracketWindowConfigChangedListener(listener);
    }

    public boolean isNeedDisableWindowLayoutInfo(ComponentName componentName) throws RemoteException {
        return getService().isNeedDisableWindowLayoutInfo(componentName);
    }

    public void notifyUiSwitched(String uiInfo, int status) throws RemoteException {
        getService().notifyUiSwitched(uiInfo, status);
    }

    public void pauseInRecentsAnim(int taskId) throws RemoteException {
        getService().pauseInRecentsAnim(taskId);
    }

    public List<String> getPauseInRecentsAnimPkgList() throws RemoteException {
        return getService().getPauseInRecentsAnimPkgList();
    }

    public boolean registerPuttObserver(IPuttObserver observer) throws RemoteException {
        return getService().registerPuttObserver(observer);
    }

    public boolean unregisterPuttObserver(IPuttObserver observer) throws RemoteException {
        return getService().unregisterPuttObserver(observer);
    }

    public boolean registerPuttEventObserver(IPuttEventObserver observer) throws RemoteException {
        return getService().registerPuttEventObserver(observer);
    }

    public boolean unregisterPuttEventObserver(IPuttEventObserver observer) throws RemoteException {
        return getService().unregisterPuttEventObserver(observer);
    }

    public boolean stopPutt(String puttHash, int exitAction, Bundle options) throws RemoteException {
        return getService().stopPutt(puttHash, exitAction, options);
    }

    public boolean startPutt(PuttParams params) throws RemoteException {
        return getService().startPutt(params);
    }

    public List<OplusPuttEnterInfo> getEnterPuttAppInfos() throws RemoteException {
        return getService().getEnterPuttAppInfos();
    }

    public boolean removePuttTask(int exitAction, String pkg, int puttTaskId) throws RemoteException {
        return getService().removePuttTask(exitAction, pkg, puttTaskId);
    }

    public boolean isSupportPuttMode(int type, String target, int userId, String callPkg, Bundle options) throws RemoteException {
        return getService().isSupportPuttMode(type, target, userId, callPkg, options);
    }

    public boolean isInAppInnerSplitScreen(int displayId) throws RemoteException {
        return getService().isInAppInnerSplitScreen(displayId);
    }

    public void registerEmbeddedWindowContainerCallback(IEmbeddedWindowContainerCallback callback, int containerTaskId) throws RemoteException {
        getService().registerEmbeddedWindowContainerCallback(callback, containerTaskId);
    }

    public void unregisterEmbeddedWindowContainerCallback(IEmbeddedWindowContainerCallback callback, int containerTaskId) throws RemoteException {
        getService().unregisterEmbeddedWindowContainerCallback(callback, containerTaskId);
    }

    public void adjustWindowFrame(WindowManager.LayoutParams attrs, int windowingMode, Rect outDisplayFrame, Rect outParentFrame) throws RemoteException {
        getService().adjustWindowFrame(attrs, windowingMode, outDisplayFrame, outParentFrame);
    }

    public void updateRecordSurfaceViewState(IBinder token, boolean hasSurfaceView) throws RemoteException {
        getService().updateRecordSurfaceViewState(token, hasSurfaceView);
    }

    public void reportViewExtractResult(Bundle bundle) throws RemoteException {
        getService().reportViewExtractResult(bundle);
    }

    public void requestViewExtractData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
        getService().requestViewExtractData(receiver, receiverExtras, activityToken, flags);
    }

    public boolean shouldSkipStartPocketStudio(List<Intent> intentList, Bundle canvasExtra) throws RemoteException {
        return getService().shouldSkipStartPocketStudio(intentList, canvasExtra);
    }

    public int createVirtualDisplayDevice(Bundle bundle) throws RemoteException {
        return getService().createVirtualDisplayDevice(bundle);
    }

    public void releaseVirtualDisplayDevice(int displayId) throws RemoteException {
        getService().releaseVirtualDisplayDevice(displayId);
    }

    public void moveTaskToDisplay(int taskId, int displayId, boolean top) throws RemoteException {
        getService().moveTaskToDisplay(taskId, displayId, top);
    }

    public boolean isFlexibleTaskEnabled(int displayId) throws RemoteException {
        return getService().isFlexibleTaskEnabled(displayId);
    }

    public boolean setFlexibleTaskEnabled(int displayId, boolean enabled) throws RemoteException {
        return getService().setFlexibleTaskEnabled(displayId, enabled);
    }

    public Rect adjustEndAbsForFlexibleMinimizeIfNeed(TransitionInfo.Change change) throws RemoteException {
        return getService().adjustEndAbsForFlexibleMinimizeIfNeed(change);
    }
}
