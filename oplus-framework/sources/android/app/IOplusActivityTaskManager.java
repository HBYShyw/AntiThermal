package android.app;

import android.app.ActivityManager;
import android.app.IActivityController;
import android.app.IAssistDataReceiver;
import android.app.IOplusActivityTaskManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IOplusKeyEventObserver;
import android.os.Parcel;
import android.os.RemoteException;
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
import com.oplus.splitscreen.OplusSplitScreenManager;
import com.oplus.zoomwindow.IOplusZoomAppObserver;
import com.oplus.zoomwindow.IOplusZoomTaskController;
import com.oplus.zoomwindow.IOplusZoomWindowObserver;
import com.oplus.zoomwindow.OplusZoomControlViewInfo;
import com.oplus.zoomwindow.OplusZoomFloatHandleViewInfo;
import com.oplus.zoomwindow.OplusZoomInputEventInfo;
import com.oplus.zoomwindow.OplusZoomWindowInfo;
import com.oplus.zoomwindow.OplusZoomWindowRUSConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/* loaded from: classes.dex */
public interface IOplusActivityTaskManager extends IInterface {
    public static final String DESCRIPTOR = "android.app.IOplusActivityTaskManager";

    boolean addBracketWindowConfigChangedListener(IOplusBracketModeChangedListener iOplusBracketModeChangedListener) throws RemoteException;

    boolean addFreeformConfigChangedListener(IOplusFreeformConfigChangedListener iOplusFreeformConfigChangedListener) throws RemoteException;

    boolean addZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener iOplusZoomWindowConfigChangedListener) throws RemoteException;

    Rect adjustEndAbsForFlexibleMinimizeIfNeed(TransitionInfo.Change change) throws RemoteException;

    void adjustWindowFrame(WindowManager.LayoutParams layoutParams, int i, Rect rect, Rect rect2) throws RemoteException;

    void adjustWindowFrameForZoom(WindowManager.LayoutParams layoutParams, int i, Rect rect, Rect rect2) throws RemoteException;

    void bindQuickReplyService(IQuickReplyCallback iQuickReplyCallback) throws RemoteException;

    List<Rect> calculateCanvasLayoutRect(List<Intent> list, int i, int i2, boolean z) throws RemoteException;

    Bundle calculateFlexibleWindowBounds(Intent intent, int i, int i2) throws RemoteException;

    List<Rect> calculateReplaceCanvasLayoutRect(List<Intent> list, int i, int i2, int i3, boolean z) throws RemoteException;

    Bundle callMethod(String str, String str2, int i, boolean z, String str3, Bundle bundle) throws RemoteException;

    void clientTransactionComplete(IBinder iBinder, int i) throws RemoteException;

    int createMirageDisplay(Surface surface) throws RemoteException;

    IOplusMirageWindowSession createMirageWindowSession(IOplusMirageSessionCallback iOplusMirageSessionCallback) throws RemoteException;

    int createVirtualDisplayDevice(Bundle bundle) throws RemoteException;

    boolean dismissSplitScreenMode(int i) throws RemoteException;

    int exitCompactWindow() throws RemoteException;

    void exitFlexibleEmbeddedTask(int i) throws RemoteException;

    void expandToFullScreen() throws RemoteException;

    void feedbackUserSelection(int i, int i2, Bundle bundle) throws RemoteException;

    Bundle getActivityConfigs(IBinder iBinder, String str) throws RemoteException;

    List<OplusAppInfo> getAllTopAppInfos() throws RemoteException;

    String getAppThemeVersion(String str, boolean z) throws RemoteException;

    int getConfineMode() throws RemoteException;

    OplusZoomWindowInfo getCurrentZoomWindowState() throws RemoteException;

    TaskSnapshot getEmbeddedChildrenSnapshot(int i, boolean z, boolean z2) throws RemoteException;

    List<OplusPuttEnterInfo> getEnterPuttAppInfos() throws RemoteException;

    Rect getFocusBounds(boolean z) throws RemoteException;

    ComponentName getFocusComponent(boolean z) throws RemoteException;

    int getFocusMode() throws RemoteException;

    List<String> getFreeformConfigList(int i) throws RemoteException;

    int getGetDisplayIdForPackageName(String str) throws RemoteException;

    OplusGlobalDragAndDropRUSConfig getGlobalDragAndDropConfig() throws RemoteException;

    Bundle getLeftRightBoundsForIme() throws RemoteException;

    Rect getMinimizedBounds(int i) throws RemoteException;

    int getMirageDisplayCastMode(int i) throws RemoteException;

    OplusMirageWindowInfo getMirageWindowInfo() throws RemoteException;

    IOplusMultiSearchManagerSession getMultiSearchSession() throws RemoteException;

    Map getPWAppInfo() throws RemoteException;

    List<String> getPauseInRecentsAnimPkgList() throws RemoteException;

    Map getPocketStudioTaskRegion(int i) throws RemoteException;

    Point getRealSize() throws RemoteException;

    List<ActivityManager.RecentTaskInfo> getRecentEmbeddedTasksForContainer(int i) throws RemoteException;

    int getRenderThreadTid(int i) throws RemoteException;

    boolean getSecurityFlagCurrentPage() throws RemoteException;

    IOplusSplitScreenSession getSplitScreenSession() throws RemoteException;

    int getSplitScreenState(Intent intent) throws RemoteException;

    Bundle getSplitScreenStatus(String str) throws RemoteException;

    Bundle getTaskInfo(ComponentName componentName) throws RemoteException;

    ComponentName getTopActivityComponentName() throws RemoteException;

    ApplicationInfo getTopApplicationInfo() throws RemoteException;

    List<String> getZoomAppConfigList(int i) throws RemoteException;

    IOplusZoomTaskController getZoomTaskController() throws RemoteException;

    OplusZoomWindowRUSConfig getZoomWindowConfig() throws RemoteException;

    boolean handleShowCompatibilityToast(String str, int i, String str2, Bundle bundle, int i2) throws RemoteException;

    boolean hasColorSplitFeature() throws RemoteException;

    boolean hasLargeScreenFeature() throws RemoteException;

    void hideZoomWindow(int i) throws RemoteException;

    Bundle invokeSync(String str, String str2, String str3, Bundle bundle) throws RemoteException;

    boolean isClickAtPocketStudioArea(int i, int i2, int i3) throws RemoteException;

    boolean isCurrentAppSupportCompactMode() throws RemoteException;

    boolean isFlexibleTaskEnabled(int i) throws RemoteException;

    boolean isFolderInnerScreen() throws RemoteException;

    boolean isFreeformEnabled() throws RemoteException;

    boolean isInAppInnerSplitScreen(int i) throws RemoteException;

    boolean isInPocketStudio(int i) throws RemoteException;

    boolean isInPocketStudioForStandard(int i) throws RemoteException;

    boolean isInSplitScreenMode() throws RemoteException;

    boolean isLockDeviceMode() throws RemoteException;

    boolean isMinimizedPocketStudio(int i) throws RemoteException;

    boolean isMirageWindowShow() throws RemoteException;

    boolean isNeedDisableWindowLayoutInfo(ComponentName componentName) throws RemoteException;

    boolean isSupportEdgeTouchPrevent() throws RemoteException;

    boolean isSupportMirageWindowMode() throws RemoteException;

    boolean isSupportPuttMode(int i, String str, int i2, String str2, Bundle bundle) throws RemoteException;

    boolean isSupportZoomMode(String str, int i, String str2, Bundle bundle) throws RemoteException;

    boolean isSupportZoomWindowMode() throws RemoteException;

    boolean isTouchNodeSupport(int i, int i2) throws RemoteException;

    boolean isZoomSimpleModeEnable() throws RemoteException;

    boolean isZoomSupportMultiWindow(String str, ComponentName componentName) throws RemoteException;

    boolean lockRotationInGame(int i, String[] strArr) throws RemoteException;

    int moveCompactWindowToLeft() throws RemoteException;

    int moveCompactWindowToRight() throws RemoteException;

    void moveTaskToDisplay(int i, int i2, boolean z) throws RemoteException;

    void notifyEmbeddedTasksChangeFocus(boolean z, int i) throws RemoteException;

    void notifyFlexibleSplitScreenStateChanged(String str, Bundle bundle, int i) throws RemoteException;

    void notifySplitScreenStateChanged(String str, Bundle bundle, boolean z) throws RemoteException;

    boolean notifyTouchNodeChange(int i, long j, int i2, int i3, int i4, String str) throws RemoteException;

    void notifyUiSwitched(String str, int i) throws RemoteException;

    void notifyZoomStateChange(String str, int i) throws RemoteException;

    void onControlViewChanged(OplusZoomControlViewInfo oplusZoomControlViewInfo) throws RemoteException;

    void onFloatHandleViewChanged(OplusZoomFloatHandleViewInfo oplusZoomFloatHandleViewInfo) throws RemoteException;

    void onInputEvent(OplusZoomInputEventInfo oplusZoomInputEventInfo) throws RemoteException;

    boolean onProtocolUpdated(String str) throws RemoteException;

    void pauseInRecentsAnim(int i) throws RemoteException;

    String readNodeFile(int i) throws RemoteException;

    String readNodeFileByDevice(int i, int i2) throws RemoteException;

    boolean rebindDisplayIfNeeded(int i, int i2) throws RemoteException;

    boolean registerAppSwitchObserver(String str, IOplusAppSwitchObserver iOplusAppSwitchObserver, OplusAppSwitchConfig oplusAppSwitchConfig) throws RemoteException;

    boolean registerCompactWindowObserver(IOplusCompactWindowObserver iOplusCompactWindowObserver) throws RemoteException;

    boolean registerConfineModeObserver(IOplusConfineModeObserver iOplusConfineModeObserver) throws RemoteException;

    void registerDragAndDropListener(String str, IDragAndDropListener iDragAndDropListener) throws RemoteException;

    void registerEmbeddedWindowContainerCallback(IEmbeddedWindowContainerCallback iEmbeddedWindowContainerCallback, int i) throws RemoteException;

    boolean registerEventCallback(IOplusTouchNodeCallback iOplusTouchNodeCallback) throws RemoteException;

    boolean registerFlexibleWindowObserver(IFlexibleWindowObserver iFlexibleWindowObserver) throws RemoteException;

    boolean registerKeyEventInterceptor(String str, IOplusKeyEventObserver iOplusKeyEventObserver, Map map) throws RemoteException;

    boolean registerKeyEventObserver(String str, IOplusKeyEventObserver iOplusKeyEventObserver, int i) throws RemoteException;

    boolean registerLockScreenCallback(IOplusLockScreenCallback iOplusLockScreenCallback) throws RemoteException;

    boolean registerMirageDisplayObserver(IOplusMirageDisplayObserver iOplusMirageDisplayObserver) throws RemoteException;

    boolean registerMirageWindowObserver(IOplusMirageWindowObserver iOplusMirageWindowObserver) throws RemoteException;

    boolean registerPuttEventObserver(IPuttEventObserver iPuttEventObserver) throws RemoteException;

    boolean registerPuttObserver(IPuttObserver iPuttObserver) throws RemoteException;

    boolean registerSecurityPageCallback(ISecurityPageController iSecurityPageController) throws RemoteException;

    boolean registerSplitScreenObserver(int i, IOplusSplitScreenObserver iOplusSplitScreenObserver) throws RemoteException;

    boolean registerSysStateChangeObserver(String str, ISysStateChangeCallback iSysStateChangeCallback) throws RemoteException;

    boolean registerZoomAppObserver(IOplusZoomAppObserver iOplusZoomAppObserver) throws RemoteException;

    boolean registerZoomWindowObserver(IOplusZoomWindowObserver iOplusZoomWindowObserver) throws RemoteException;

    void releaseVirtualDisplayDevice(int i) throws RemoteException;

    boolean removeBracketWindowConfigChangedListener(IOplusBracketModeChangedListener iOplusBracketModeChangedListener) throws RemoteException;

    void removeEmbeddedContainerTask(int i, int i2) throws RemoteException;

    boolean removeFreeformConfigChangedListener(IOplusFreeformConfigChangedListener iOplusFreeformConfigChangedListener) throws RemoteException;

    boolean removePuttTask(int i, String str, int i2) throws RemoteException;

    boolean removeZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener iOplusZoomWindowConfigChangedListener) throws RemoteException;

    void reportViewExtractResult(Bundle bundle) throws RemoteException;

    void requestViewExtractData(IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, int i) throws RemoteException;

    boolean resetDefaultEdgeTouchPreventParam(String str) throws RemoteException;

    void resetFlexibleTask(int i, boolean z, boolean z2) throws RemoteException;

    boolean sendMessage(PendingIntent pendingIntent, String str) throws RemoteException;

    void setConfineMode(int i, boolean z, IBinder iBinder) throws RemoteException;

    void setDefaultEdgeTouchPreventParam(String str, List<String> list) throws RemoteException;

    void setEdgeTouchCallRules(String str, Map<String, List<String>> map) throws RemoteException;

    void setEmbeddedContainerTask(int i, int i2) throws RemoteException;

    void setFlexibleFrame(int i, Bundle bundle) throws RemoteException;

    void setFlexibleTaskEmbedding(int i, boolean z) throws RemoteException;

    boolean setFlexibleTaskEnabled(int i, boolean z) throws RemoteException;

    void setGimbalLaunchPkg(String str) throws RemoteException;

    void setGlobalDragAndDropConfig(OplusGlobalDragAndDropRUSConfig oplusGlobalDragAndDropRUSConfig) throws RemoteException;

    boolean setInputConsumerEnabled(boolean z, IRecentsAnimationController iRecentsAnimationController) throws RemoteException;

    void setIsInFocusAnimating(boolean z) throws RemoteException;

    void setMinimizedPocketStudio(boolean z, int i) throws RemoteException;

    void setMirageDisplaySurfaceById(int i, Surface surface) throws RemoteException;

    void setMirageDisplaySurfaceByMode(int i, Surface surface) throws RemoteException;

    void setMirageWindowSilent(String str) throws RemoteException;

    void setPackagesState(Map map) throws RemoteException;

    void setPermitList(int i, int i2, List<String> list, boolean z) throws RemoteException;

    void setSecureController(IActivityController iActivityController) throws RemoteException;

    boolean setSplitScreenObserver(IOplusSplitScreenObserver iOplusSplitScreenObserver) throws RemoteException;

    int setTaskWindowingModeSplitScreen(int i) throws RemoteException;

    void setZoomWindowConfig(OplusZoomWindowRUSConfig oplusZoomWindowRUSConfig) throws RemoteException;

    boolean shouldInterceptBackKeyForMultiSearch(IBinder iBinder, boolean z) throws RemoteException;

    boolean shouldSkipStartPocketStudio(List<Intent> list, Bundle bundle) throws RemoteException;

    boolean splitScreenForApplication(PendingIntent pendingIntent, int i) throws RemoteException;

    int splitScreenForEdgePanel(Intent intent, int i) throws RemoteException;

    boolean splitScreenForEdgePanelExt(Intent intent, boolean z, int i) throws RemoteException;

    boolean splitScreenForRecentTasks(int i) throws RemoteException;

    boolean splitScreenForTopApp(int i) throws RemoteException;

    void startActivityInTask(Intent intent, int i) throws RemoteException;

    int startAnyActivity(Intent intent, Bundle bundle) throws RemoteException;

    int startCompactWindow() throws RemoteException;

    boolean startFlexibleWindowForRecentTasks(int i, Rect rect) throws RemoteException;

    void startLockDeviceMode(String str, String[] strArr) throws RemoteException;

    void startMiniZoomFromZoom(int i) throws RemoteException;

    int startMirageWindowMode(Intent intent, Bundle bundle) throws RemoteException;

    void startMirageWindowModeWithName(ComponentName componentName, int i, int i2, Bundle bundle) throws RemoteException;

    boolean startPutt(PuttParams puttParams) throws RemoteException;

    int startZoomWindow(Intent intent, Bundle bundle, int i, String str) throws RemoteException;

    void stopLockDeviceMode() throws RemoteException;

    void stopMirageWindowMode(Bundle bundle) throws RemoteException;

    void stopMirageWindowModeOld() throws RemoteException;

    boolean stopPutt(String str, int i, Bundle bundle) throws RemoteException;

    SurfaceControl takeScreenshot(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl) throws RemoteException;

    void unbindQuickReplyService() throws RemoteException;

    boolean unregisterAppSwitchObserver(String str, OplusAppSwitchConfig oplusAppSwitchConfig) throws RemoteException;

    boolean unregisterCompactWindowObserver(IOplusCompactWindowObserver iOplusCompactWindowObserver) throws RemoteException;

    boolean unregisterConfineModeObserver(IOplusConfineModeObserver iOplusConfineModeObserver) throws RemoteException;

    void unregisterDragAndDropListener(String str, IDragAndDropListener iDragAndDropListener) throws RemoteException;

    void unregisterEmbeddedWindowContainerCallback(IEmbeddedWindowContainerCallback iEmbeddedWindowContainerCallback, int i) throws RemoteException;

    boolean unregisterEventCallback(IOplusTouchNodeCallback iOplusTouchNodeCallback) throws RemoteException;

    boolean unregisterFlexibleWindowObserver(IFlexibleWindowObserver iFlexibleWindowObserver) throws RemoteException;

    boolean unregisterKeyEventInterceptor(String str) throws RemoteException;

    boolean unregisterKeyEventObserver(String str) throws RemoteException;

    boolean unregisterLockScreenCallback(IOplusLockScreenCallback iOplusLockScreenCallback) throws RemoteException;

    boolean unregisterMirageDisplayObserver(IOplusMirageDisplayObserver iOplusMirageDisplayObserver) throws RemoteException;

    boolean unregisterMirageWindowObserver(IOplusMirageWindowObserver iOplusMirageWindowObserver) throws RemoteException;

    boolean unregisterPuttEventObserver(IPuttEventObserver iPuttEventObserver) throws RemoteException;

    boolean unregisterPuttObserver(IPuttObserver iPuttObserver) throws RemoteException;

    boolean unregisterSecurityPageCallback(ISecurityPageController iSecurityPageController) throws RemoteException;

    boolean unregisterSplitScreenObserver(int i, IOplusSplitScreenObserver iOplusSplitScreenObserver) throws RemoteException;

    boolean unregisterSysStateChangeObserver(String str, ISysStateChangeCallback iSysStateChangeCallback) throws RemoteException;

    boolean unregisterZoomAppObserver(IOplusZoomAppObserver iOplusZoomAppObserver) throws RemoteException;

    boolean unregisterZoomWindowObserver(IOplusZoomWindowObserver iOplusZoomWindowObserver) throws RemoteException;

    boolean updateAppData(String str, Bundle bundle) throws RemoteException;

    boolean updateCarModeMultiLaunchWhiteList(String str) throws RemoteException;

    void updateDeferStartingWindowApps(List<String> list, boolean z) throws RemoteException;

    boolean updateMirageWindowCastFlag(int i, Bundle bundle) throws RemoteException;

    boolean updatePrivacyProtectionList(List<String> list, boolean z) throws RemoteException;

    boolean updatePrivacyProtectionListWithBundle(List<String> list, boolean z, boolean z2, Bundle bundle) throws RemoteException;

    void updateRecordSurfaceViewState(IBinder iBinder, boolean z) throws RemoteException;

    void updateTaskVisibility(WindowContainerToken windowContainerToken, int i, boolean z) throws RemoteException;

    void updateUntrustedTouchConfig(String str, boolean z) throws RemoteException;

    boolean writeEdgeTouchPreventParam(String str, String str2, List<String> list) throws RemoteException;

    boolean writeNodeFile(int i, String str) throws RemoteException;

    boolean writeNodeFileByDevice(int i, int i2, String str) throws RemoteException;

    boolean writeNodeFileFromBt(int i, int i2, String str) throws RemoteException;

    void writeNodeFileOneWay(int i, int i2, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusActivityTaskManager {
        @Override // android.app.IOplusActivityTaskManager
        public void setSecureController(IActivityController controller) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public ComponentName getTopActivityComponentName() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public ApplicationInfo getTopApplicationInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int getSplitScreenState(Intent intent) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public List<OplusAppInfo> getAllTopAppInfos() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public List<String> getFreeformConfigList(int type) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isFreeformEnabled() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean addFreeformConfigChangedListener(IOplusFreeformConfigChangedListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean removeFreeformConfigChangedListener(IOplusFreeformConfigChangedListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerAppSwitchObserver(String pkgName, IOplusAppSwitchObserver observer, OplusAppSwitchConfig config) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterAppSwitchObserver(String pkgName, OplusAppSwitchConfig config) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void registerDragAndDropListener(String pkgName, IDragAndDropListener listener) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void unregisterDragAndDropListener(String pkgName, IDragAndDropListener listener) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public int startZoomWindow(Intent intent, Bundle options, int userId, String callPkg) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerZoomWindowObserver(IOplusZoomWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterZoomWindowObserver(IOplusZoomWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public OplusZoomWindowInfo getCurrentZoomWindowState() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void hideZoomWindow(int flag) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public List<String> getZoomAppConfigList(int type) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isSupportZoomMode(String target, int userId, String callPkg, Bundle extension) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean handleShowCompatibilityToast(String target, int userId, String callPkg, Bundle extension, int type) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void startMiniZoomFromZoom(int startWay) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void notifyZoomStateChange(String packageName, int action) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void onInputEvent(OplusZoomInputEventInfo inputEventInfo) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void onControlViewChanged(OplusZoomControlViewInfo cvInfo) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerZoomAppObserver(IOplusZoomAppObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterZoomAppObserver(IOplusZoomAppObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerSecurityPageCallback(ISecurityPageController observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterSecurityPageCallback(ISecurityPageController observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean getSecurityFlagCurrentPage() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void updateDeferStartingWindowApps(List<String> packages, boolean removeImmdiately) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void onFloatHandleViewChanged(OplusZoomFloatHandleViewInfo floatHandleInfo) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isSupportZoomWindowMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public OplusZoomWindowRUSConfig getZoomWindowConfig() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setZoomWindowConfig(OplusZoomWindowRUSConfig config) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean addZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean removeZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void startLockDeviceMode(String rootPkg, String[] packages) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void stopLockDeviceMode() throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isLockDeviceMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isZoomSimpleModeEnable() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean writeEdgeTouchPreventParam(String callPkg, String scenePkg, List<String> paramCmdList) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setDefaultEdgeTouchPreventParam(String callPkg, List<String> paramCmdList) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean resetDefaultEdgeTouchPreventParam(String callPkg) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isSupportEdgeTouchPrevent() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setEdgeTouchCallRules(String callPkg, Map<String, List<String>> rulesMap) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public int splitScreenForEdgePanel(Intent intent, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setConfineMode(int mode, boolean on, IBinder token) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public int getConfineMode() throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setPermitList(int mode, int type, List<String> permits, boolean isMultiApp) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerKeyEventObserver(String observerFingerPrint, IOplusKeyEventObserver observer, int listenFlag) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterKeyEventObserver(String observerFingerPrint) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean updateAppData(String module, Bundle bundle) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerSysStateChangeObserver(String module, ISysStateChangeCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterSysStateChangeObserver(String module, ISysStateChangeCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setGimbalLaunchPkg(String pkgName) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setPackagesState(Map packageMap) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerLockScreenCallback(IOplusLockScreenCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterLockScreenCallback(IOplusLockScreenCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void notifySplitScreenStateChanged(String event, Bundle bundle, boolean broadcast) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean setSplitScreenObserver(IOplusSplitScreenObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isInSplitScreenMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean dismissSplitScreenMode(int type) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerSplitScreenObserver(int observerId, IOplusSplitScreenObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterSplitScreenObserver(int observerId, IOplusSplitScreenObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Bundle getSplitScreenStatus(String event) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean splitScreenForTopApp(int type) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean splitScreenForRecentTasks(int taskId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int setTaskWindowingModeSplitScreen(int taskId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean splitScreenForEdgePanelExt(Intent intent, boolean launchToPrimary, int launchArea) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean hasLargeScreenFeature() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isFolderInnerScreen() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Rect getMinimizedBounds(int dockedSide) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public IOplusSplitScreenSession getSplitScreenSession() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean splitScreenForApplication(PendingIntent intent, int position) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Bundle getLeftRightBoundsForIme() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean hasColorSplitFeature() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerKeyEventInterceptor(String interceptorFingerPrint, IOplusKeyEventObserver observer, Map configs) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterKeyEventInterceptor(String interceptorFingerPrint) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public OplusGlobalDragAndDropRUSConfig getGlobalDragAndDropConfig() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setGlobalDragAndDropConfig(OplusGlobalDragAndDropRUSConfig config) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerMirageWindowObserver(IOplusMirageWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterMirageWindowObserver(IOplusMirageWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerMirageDisplayObserver(IOplusMirageDisplayObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterMirageDisplayObserver(IOplusMirageDisplayObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int createMirageDisplay(Surface surface) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Bundle getTaskInfo(ComponentName name) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void startMirageWindowModeWithName(ComponentName cpnName, int taskId, int flags, Bundle options) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public int startMirageWindowMode(Intent intent, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isMirageWindowShow() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void stopMirageWindowModeOld() throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void stopMirageWindowMode(Bundle options) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setMirageDisplaySurfaceById(int displyId, Surface surface) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setMirageDisplaySurfaceByMode(int mode, Surface surface) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public int getMirageDisplayCastMode(int displayId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void expandToFullScreen() throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setMirageWindowSilent(String pkgName) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isSupportMirageWindowMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public OplusMirageWindowInfo getMirageWindowInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean updateMirageWindowCastFlag(int castFlag, Bundle options) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean updatePrivacyProtectionList(List<String> name, boolean append) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean updatePrivacyProtectionListWithBundle(List<String> name, boolean append, boolean isDefault, Bundle options) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public IOplusMirageWindowSession createMirageWindowSession(IOplusMirageSessionCallback callback) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int getGetDisplayIdForPackageName(String packageName) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void feedbackUserSelection(int eventId, int selection, Bundle extension) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean updateCarModeMultiLaunchWhiteList(String list) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean rebindDisplayIfNeeded(int castDisplayId, int mirageDisplayId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerConfineModeObserver(IOplusConfineModeObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterConfineModeObserver(IOplusConfineModeObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public String readNodeFile(int nodeFlag) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public String readNodeFileByDevice(int deviceId, int nodeFlag) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean writeNodeFile(int nodeFlag, String info) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean writeNodeFileByDevice(int deviceId, int nodeFlag, String info) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isTouchNodeSupport(int deviceId, int nodeFlag) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean writeNodeFileFromBt(int deviceId, int nodeFlag, String info) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void writeNodeFileOneWay(int deviceId, int nodeFlag, String info) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean notifyTouchNodeChange(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerEventCallback(IOplusTouchNodeCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterEventCallback(IOplusTouchNodeCallback callback) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void clientTransactionComplete(IBinder token, int seq) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void updateUntrustedTouchConfig(String configData, boolean isRus) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public String getAppThemeVersion(String pkgName, boolean change) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean lockRotationInGame(int state, String[] packages) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int startCompactWindow() throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int exitCompactWindow() throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isCurrentAppSupportCompactMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int moveCompactWindowToLeft() throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int moveCompactWindowToRight() throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Map getPWAppInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean onProtocolUpdated(String content) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int getFocusMode() throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Rect getFocusBounds(boolean isPrimary) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public ComponentName getFocusComponent(boolean isPrimary) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Point getRealSize() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Bundle callMethod(String method, String packageName, int param1, boolean param2, String param3, Bundle object) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Bundle invokeSync(String packageName, String method, String params, Bundle objects) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean addBracketWindowConfigChangedListener(IOplusBracketModeChangedListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean removeBracketWindowConfigChangedListener(IOplusBracketModeChangedListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isNeedDisableWindowLayoutInfo(ComponentName componentName) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void bindQuickReplyService(IQuickReplyCallback callback) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void unbindQuickReplyService() throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean sendMessage(PendingIntent weChatIntent, String message) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean shouldInterceptBackKeyForMultiSearch(IBinder activityToken, boolean down) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public IOplusMultiSearchManagerSession getMultiSearchSession() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public SurfaceControl takeScreenshot(WindowContainerToken windowContainerToken, SurfaceControl outSurfaceControl) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public IOplusZoomTaskController getZoomTaskController() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int getRenderThreadTid(int pid) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean setInputConsumerEnabled(boolean enabled, IRecentsAnimationController controller) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void adjustWindowFrameForZoom(WindowManager.LayoutParams attrs, int windowingMode, Rect outDisplayFrame, Rect outParentFrame) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isZoomSupportMultiWindow(String packageName, ComponentName componentName) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void notifyUiSwitched(String uiInfo, int status) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void pauseInRecentsAnim(int taskId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public List<String> getPauseInRecentsAnimPkgList() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerPuttObserver(IPuttObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterPuttObserver(IPuttObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerPuttEventObserver(IPuttEventObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterPuttEventObserver(IPuttEventObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean stopPutt(String puttHash, int action, Bundle options) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean startPutt(PuttParams params) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public List<OplusPuttEnterInfo> getEnterPuttAppInfos() throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean removePuttTask(int action, String pkg, int puttTaskId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isSupportPuttMode(int type, String target, int userId, String callPkg, Bundle options) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean registerFlexibleWindowObserver(IFlexibleWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean unregisterFlexibleWindowObserver(IFlexibleWindowObserver observer) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Bundle calculateFlexibleWindowBounds(Intent intent, int appOrientation, int displayId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isInPocketStudio(int displayId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isInPocketStudioForStandard(int displayId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Map getPocketStudioTaskRegion(int displayId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setMinimizedPocketStudio(boolean minimized, int displayId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isMinimizedPocketStudio(int displayId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setFlexibleFrame(int taskId, Bundle bundle) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setIsInFocusAnimating(boolean isInFocusAnimating) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setEmbeddedContainerTask(int embeddedTaskId, int containerTaskId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void removeEmbeddedContainerTask(int embeddedTaskId, int containerTaskId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void exitFlexibleEmbeddedTask(int embeddedTaskId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void setFlexibleTaskEmbedding(int taskId, boolean state) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void resetFlexibleTask(int embeddedTaskId, boolean needResize, boolean doAnimation) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public List<ActivityManager.RecentTaskInfo> getRecentEmbeddedTasksForContainer(int containerTaskId) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void startActivityInTask(Intent intent, int taskId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public int startAnyActivity(Intent intent, Bundle bundle) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean startFlexibleWindowForRecentTasks(int taskId, Rect rect) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isClickAtPocketStudioArea(int displayId, int rowX, int rowY) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public List<Rect> calculateCanvasLayoutRect(List<Intent> intentList, int focusIndex, int layoutOrientation, boolean panoramaMode) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public List<Rect> calculateReplaceCanvasLayoutRect(List<Intent> intentList, int focusIndex, int replace, int displayId, boolean panoramaMode) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isInAppInnerSplitScreen(int displayId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void notifyEmbeddedTasksChangeFocus(boolean isStateSteady, int containerTaskId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void notifyFlexibleSplitScreenStateChanged(String event, Bundle bundle, int containerTaskId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void registerEmbeddedWindowContainerCallback(IEmbeddedWindowContainerCallback callback, int containerTaskId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void unregisterEmbeddedWindowContainerCallback(IEmbeddedWindowContainerCallback callback, int containerTaskId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public Bundle getActivityConfigs(IBinder token, String packageName) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void adjustWindowFrame(WindowManager.LayoutParams attrs, int windowingMode, Rect outDisplayFrame, Rect outParentFrame) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void updateRecordSurfaceViewState(IBinder token, boolean hasSurfaceView) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void reportViewExtractResult(Bundle bundle) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void requestViewExtractData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void updateTaskVisibility(WindowContainerToken windowContainerToken, int containerTaskId, boolean visible) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean shouldSkipStartPocketStudio(List<Intent> intentList, Bundle canvasExtra) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public int createVirtualDisplayDevice(Bundle bundle) throws RemoteException {
            return 0;
        }

        @Override // android.app.IOplusActivityTaskManager
        public void releaseVirtualDisplayDevice(int displayId) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public void moveTaskToDisplay(int taskId, int displayId, boolean top) throws RemoteException {
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean isFlexibleTaskEnabled(int displayId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public boolean setFlexibleTaskEnabled(int displayId, boolean enabled) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusActivityTaskManager
        public Rect adjustEndAbsForFlexibleMinimizeIfNeed(TransitionInfo.Change change) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusActivityTaskManager
        public TaskSnapshot getEmbeddedChildrenSnapshot(int taskId, boolean isLowResolution, boolean takeSnapshotIfNeeded) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusActivityTaskManager {
        static final int TRANSACTION_addBracketWindowConfigChangedListener = 139;
        static final int TRANSACTION_addFreeformConfigChangedListener = 8;
        static final int TRANSACTION_addZoomWindowConfigChangedListener = 36;
        static final int TRANSACTION_adjustEndAbsForFlexibleMinimizeIfNeed = 204;
        static final int TRANSACTION_adjustWindowFrame = 193;
        static final int TRANSACTION_adjustWindowFrameForZoom = 151;
        static final int TRANSACTION_bindQuickReplyService = 142;
        static final int TRANSACTION_calculateCanvasLayoutRect = 185;
        static final int TRANSACTION_calculateFlexibleWindowBounds = 167;
        static final int TRANSACTION_calculateReplaceCanvasLayoutRect = 186;
        static final int TRANSACTION_callMethod = 137;
        static final int TRANSACTION_clientTransactionComplete = 120;
        static final int TRANSACTION_createMirageDisplay = 86;
        static final int TRANSACTION_createMirageWindowSession = 103;
        static final int TRANSACTION_createVirtualDisplayDevice = 199;
        static final int TRANSACTION_dismissSplitScreenMode = 63;
        static final int TRANSACTION_exitCompactWindow = 125;
        static final int TRANSACTION_exitFlexibleEmbeddedTask = 177;
        static final int TRANSACTION_expandToFullScreen = 96;
        static final int TRANSACTION_feedbackUserSelection = 105;
        static final int TRANSACTION_getActivityConfigs = 192;
        static final int TRANSACTION_getAllTopAppInfos = 5;
        static final int TRANSACTION_getAppThemeVersion = 122;
        static final int TRANSACTION_getConfineMode = 49;
        static final int TRANSACTION_getCurrentZoomWindowState = 17;
        static final int TRANSACTION_getEmbeddedChildrenSnapshot = 205;
        static final int TRANSACTION_getEnterPuttAppInfos = 162;
        static final int TRANSACTION_getFocusBounds = 134;
        static final int TRANSACTION_getFocusComponent = 135;
        static final int TRANSACTION_getFocusMode = 133;
        static final int TRANSACTION_getFreeformConfigList = 6;
        static final int TRANSACTION_getGetDisplayIdForPackageName = 104;
        static final int TRANSACTION_getGlobalDragAndDropConfig = 80;
        static final int TRANSACTION_getLeftRightBoundsForIme = 76;
        static final int TRANSACTION_getMinimizedBounds = 73;
        static final int TRANSACTION_getMirageDisplayCastMode = 95;
        static final int TRANSACTION_getMirageWindowInfo = 99;
        static final int TRANSACTION_getMultiSearchSession = 146;
        static final int TRANSACTION_getPWAppInfo = 131;
        static final int TRANSACTION_getPauseInRecentsAnimPkgList = 155;
        static final int TRANSACTION_getPocketStudioTaskRegion = 170;
        static final int TRANSACTION_getRealSize = 136;
        static final int TRANSACTION_getRecentEmbeddedTasksForContainer = 180;
        static final int TRANSACTION_getRenderThreadTid = 149;
        static final int TRANSACTION_getSecurityFlagCurrentPage = 30;
        static final int TRANSACTION_getSplitScreenSession = 74;
        static final int TRANSACTION_getSplitScreenState = 4;
        static final int TRANSACTION_getSplitScreenStatus = 66;
        static final int TRANSACTION_getTaskInfo = 87;
        static final int TRANSACTION_getTopActivityComponentName = 2;
        static final int TRANSACTION_getTopApplicationInfo = 3;
        static final int TRANSACTION_getZoomAppConfigList = 19;
        static final int TRANSACTION_getZoomTaskController = 148;
        static final int TRANSACTION_getZoomWindowConfig = 34;
        static final int TRANSACTION_handleShowCompatibilityToast = 21;
        static final int TRANSACTION_hasColorSplitFeature = 77;
        static final int TRANSACTION_hasLargeScreenFeature = 71;
        static final int TRANSACTION_hideZoomWindow = 18;
        static final int TRANSACTION_invokeSync = 138;
        static final int TRANSACTION_isClickAtPocketStudioArea = 184;
        static final int TRANSACTION_isCurrentAppSupportCompactMode = 126;
        static final int TRANSACTION_isFlexibleTaskEnabled = 202;
        static final int TRANSACTION_isFolderInnerScreen = 72;
        static final int TRANSACTION_isFreeformEnabled = 7;
        static final int TRANSACTION_isInAppInnerSplitScreen = 187;
        static final int TRANSACTION_isInPocketStudio = 168;
        static final int TRANSACTION_isInPocketStudioForStandard = 169;
        static final int TRANSACTION_isInSplitScreenMode = 62;
        static final int TRANSACTION_isLockDeviceMode = 40;
        static final int TRANSACTION_isMinimizedPocketStudio = 172;
        static final int TRANSACTION_isMirageWindowShow = 90;
        static final int TRANSACTION_isNeedDisableWindowLayoutInfo = 141;
        static final int TRANSACTION_isSupportEdgeTouchPrevent = 45;
        static final int TRANSACTION_isSupportMirageWindowMode = 98;
        static final int TRANSACTION_isSupportPuttMode = 164;
        static final int TRANSACTION_isSupportZoomMode = 20;
        static final int TRANSACTION_isSupportZoomWindowMode = 33;
        static final int TRANSACTION_isTouchNodeSupport = 114;
        static final int TRANSACTION_isZoomSimpleModeEnable = 41;
        static final int TRANSACTION_isZoomSupportMultiWindow = 152;
        static final int TRANSACTION_lockRotationInGame = 123;
        static final int TRANSACTION_moveCompactWindowToLeft = 127;
        static final int TRANSACTION_moveCompactWindowToRight = 128;
        static final int TRANSACTION_moveTaskToDisplay = 201;
        static final int TRANSACTION_notifyEmbeddedTasksChangeFocus = 188;
        static final int TRANSACTION_notifyFlexibleSplitScreenStateChanged = 189;
        static final int TRANSACTION_notifySplitScreenStateChanged = 60;
        static final int TRANSACTION_notifyTouchNodeChange = 117;
        static final int TRANSACTION_notifyUiSwitched = 153;
        static final int TRANSACTION_notifyZoomStateChange = 23;
        static final int TRANSACTION_onControlViewChanged = 25;
        static final int TRANSACTION_onFloatHandleViewChanged = 32;
        static final int TRANSACTION_onInputEvent = 24;
        static final int TRANSACTION_onProtocolUpdated = 132;
        static final int TRANSACTION_pauseInRecentsAnim = 154;
        static final int TRANSACTION_readNodeFile = 110;
        static final int TRANSACTION_readNodeFileByDevice = 111;
        static final int TRANSACTION_rebindDisplayIfNeeded = 107;
        static final int TRANSACTION_registerAppSwitchObserver = 10;
        static final int TRANSACTION_registerCompactWindowObserver = 129;
        static final int TRANSACTION_registerConfineModeObserver = 108;
        static final int TRANSACTION_registerDragAndDropListener = 12;
        static final int TRANSACTION_registerEmbeddedWindowContainerCallback = 190;
        static final int TRANSACTION_registerEventCallback = 118;
        static final int TRANSACTION_registerFlexibleWindowObserver = 165;
        static final int TRANSACTION_registerKeyEventInterceptor = 78;
        static final int TRANSACTION_registerKeyEventObserver = 51;
        static final int TRANSACTION_registerLockScreenCallback = 58;
        static final int TRANSACTION_registerMirageDisplayObserver = 84;
        static final int TRANSACTION_registerMirageWindowObserver = 82;
        static final int TRANSACTION_registerPuttEventObserver = 158;
        static final int TRANSACTION_registerPuttObserver = 156;
        static final int TRANSACTION_registerSecurityPageCallback = 28;
        static final int TRANSACTION_registerSplitScreenObserver = 64;
        static final int TRANSACTION_registerSysStateChangeObserver = 54;
        static final int TRANSACTION_registerZoomAppObserver = 26;
        static final int TRANSACTION_registerZoomWindowObserver = 15;
        static final int TRANSACTION_releaseVirtualDisplayDevice = 200;
        static final int TRANSACTION_removeBracketWindowConfigChangedListener = 140;
        static final int TRANSACTION_removeEmbeddedContainerTask = 176;
        static final int TRANSACTION_removeFreeformConfigChangedListener = 9;
        static final int TRANSACTION_removePuttTask = 163;
        static final int TRANSACTION_removeZoomWindowConfigChangedListener = 37;
        static final int TRANSACTION_reportViewExtractResult = 195;
        static final int TRANSACTION_requestViewExtractData = 196;
        static final int TRANSACTION_resetDefaultEdgeTouchPreventParam = 44;
        static final int TRANSACTION_resetFlexibleTask = 179;
        static final int TRANSACTION_sendMessage = 144;
        static final int TRANSACTION_setConfineMode = 48;
        static final int TRANSACTION_setDefaultEdgeTouchPreventParam = 43;
        static final int TRANSACTION_setEdgeTouchCallRules = 46;
        static final int TRANSACTION_setEmbeddedContainerTask = 175;
        static final int TRANSACTION_setFlexibleFrame = 173;
        static final int TRANSACTION_setFlexibleTaskEmbedding = 178;
        static final int TRANSACTION_setFlexibleTaskEnabled = 203;
        static final int TRANSACTION_setGimbalLaunchPkg = 56;
        static final int TRANSACTION_setGlobalDragAndDropConfig = 81;
        static final int TRANSACTION_setInputConsumerEnabled = 150;
        static final int TRANSACTION_setIsInFocusAnimating = 174;
        static final int TRANSACTION_setMinimizedPocketStudio = 171;
        static final int TRANSACTION_setMirageDisplaySurfaceById = 93;
        static final int TRANSACTION_setMirageDisplaySurfaceByMode = 94;
        static final int TRANSACTION_setMirageWindowSilent = 97;
        static final int TRANSACTION_setPackagesState = 57;
        static final int TRANSACTION_setPermitList = 50;
        static final int TRANSACTION_setSecureController = 1;
        static final int TRANSACTION_setSplitScreenObserver = 61;
        static final int TRANSACTION_setTaskWindowingModeSplitScreen = 69;
        static final int TRANSACTION_setZoomWindowConfig = 35;
        static final int TRANSACTION_shouldInterceptBackKeyForMultiSearch = 145;
        static final int TRANSACTION_shouldSkipStartPocketStudio = 198;
        static final int TRANSACTION_splitScreenForApplication = 75;
        static final int TRANSACTION_splitScreenForEdgePanel = 47;
        static final int TRANSACTION_splitScreenForEdgePanelExt = 70;
        static final int TRANSACTION_splitScreenForRecentTasks = 68;
        static final int TRANSACTION_splitScreenForTopApp = 67;
        static final int TRANSACTION_startActivityInTask = 181;
        static final int TRANSACTION_startAnyActivity = 182;
        static final int TRANSACTION_startCompactWindow = 124;
        static final int TRANSACTION_startFlexibleWindowForRecentTasks = 183;
        static final int TRANSACTION_startLockDeviceMode = 38;
        static final int TRANSACTION_startMiniZoomFromZoom = 22;
        static final int TRANSACTION_startMirageWindowMode = 89;
        static final int TRANSACTION_startMirageWindowModeWithName = 88;
        static final int TRANSACTION_startPutt = 161;
        static final int TRANSACTION_startZoomWindow = 14;
        static final int TRANSACTION_stopLockDeviceMode = 39;
        static final int TRANSACTION_stopMirageWindowMode = 92;
        static final int TRANSACTION_stopMirageWindowModeOld = 91;
        static final int TRANSACTION_stopPutt = 160;
        static final int TRANSACTION_takeScreenshot = 147;
        static final int TRANSACTION_unbindQuickReplyService = 143;
        static final int TRANSACTION_unregisterAppSwitchObserver = 11;
        static final int TRANSACTION_unregisterCompactWindowObserver = 130;
        static final int TRANSACTION_unregisterConfineModeObserver = 109;
        static final int TRANSACTION_unregisterDragAndDropListener = 13;
        static final int TRANSACTION_unregisterEmbeddedWindowContainerCallback = 191;
        static final int TRANSACTION_unregisterEventCallback = 119;
        static final int TRANSACTION_unregisterFlexibleWindowObserver = 166;
        static final int TRANSACTION_unregisterKeyEventInterceptor = 79;
        static final int TRANSACTION_unregisterKeyEventObserver = 52;
        static final int TRANSACTION_unregisterLockScreenCallback = 59;
        static final int TRANSACTION_unregisterMirageDisplayObserver = 85;
        static final int TRANSACTION_unregisterMirageWindowObserver = 83;
        static final int TRANSACTION_unregisterPuttEventObserver = 159;
        static final int TRANSACTION_unregisterPuttObserver = 157;
        static final int TRANSACTION_unregisterSecurityPageCallback = 29;
        static final int TRANSACTION_unregisterSplitScreenObserver = 65;
        static final int TRANSACTION_unregisterSysStateChangeObserver = 55;
        static final int TRANSACTION_unregisterZoomAppObserver = 27;
        static final int TRANSACTION_unregisterZoomWindowObserver = 16;
        static final int TRANSACTION_updateAppData = 53;
        static final int TRANSACTION_updateCarModeMultiLaunchWhiteList = 106;
        static final int TRANSACTION_updateDeferStartingWindowApps = 31;
        static final int TRANSACTION_updateMirageWindowCastFlag = 100;
        static final int TRANSACTION_updatePrivacyProtectionList = 101;
        static final int TRANSACTION_updatePrivacyProtectionListWithBundle = 102;
        static final int TRANSACTION_updateRecordSurfaceViewState = 194;
        static final int TRANSACTION_updateTaskVisibility = 197;
        static final int TRANSACTION_updateUntrustedTouchConfig = 121;
        static final int TRANSACTION_writeEdgeTouchPreventParam = 42;
        static final int TRANSACTION_writeNodeFile = 112;
        static final int TRANSACTION_writeNodeFileByDevice = 113;
        static final int TRANSACTION_writeNodeFileFromBt = 115;
        static final int TRANSACTION_writeNodeFileOneWay = 116;

        public Stub() {
            attachInterface(this, IOplusActivityTaskManager.DESCRIPTOR);
        }

        public static IOplusActivityTaskManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusActivityTaskManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusActivityTaskManager)) {
                return (IOplusActivityTaskManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setSecureController";
                case 2:
                    return "getTopActivityComponentName";
                case 3:
                    return "getTopApplicationInfo";
                case 4:
                    return "getSplitScreenState";
                case 5:
                    return "getAllTopAppInfos";
                case 6:
                    return "getFreeformConfigList";
                case 7:
                    return "isFreeformEnabled";
                case 8:
                    return "addFreeformConfigChangedListener";
                case 9:
                    return "removeFreeformConfigChangedListener";
                case 10:
                    return "registerAppSwitchObserver";
                case 11:
                    return "unregisterAppSwitchObserver";
                case 12:
                    return "registerDragAndDropListener";
                case 13:
                    return "unregisterDragAndDropListener";
                case 14:
                    return "startZoomWindow";
                case 15:
                    return "registerZoomWindowObserver";
                case 16:
                    return "unregisterZoomWindowObserver";
                case 17:
                    return "getCurrentZoomWindowState";
                case 18:
                    return "hideZoomWindow";
                case 19:
                    return "getZoomAppConfigList";
                case 20:
                    return "isSupportZoomMode";
                case 21:
                    return "handleShowCompatibilityToast";
                case 22:
                    return "startMiniZoomFromZoom";
                case 23:
                    return "notifyZoomStateChange";
                case 24:
                    return "onInputEvent";
                case 25:
                    return "onControlViewChanged";
                case 26:
                    return "registerZoomAppObserver";
                case 27:
                    return "unregisterZoomAppObserver";
                case 28:
                    return "registerSecurityPageCallback";
                case 29:
                    return "unregisterSecurityPageCallback";
                case 30:
                    return "getSecurityFlagCurrentPage";
                case 31:
                    return "updateDeferStartingWindowApps";
                case 32:
                    return "onFloatHandleViewChanged";
                case 33:
                    return "isSupportZoomWindowMode";
                case 34:
                    return "getZoomWindowConfig";
                case 35:
                    return "setZoomWindowConfig";
                case 36:
                    return "addZoomWindowConfigChangedListener";
                case 37:
                    return "removeZoomWindowConfigChangedListener";
                case 38:
                    return "startLockDeviceMode";
                case 39:
                    return "stopLockDeviceMode";
                case 40:
                    return "isLockDeviceMode";
                case 41:
                    return "isZoomSimpleModeEnable";
                case 42:
                    return "writeEdgeTouchPreventParam";
                case 43:
                    return "setDefaultEdgeTouchPreventParam";
                case 44:
                    return "resetDefaultEdgeTouchPreventParam";
                case 45:
                    return "isSupportEdgeTouchPrevent";
                case 46:
                    return "setEdgeTouchCallRules";
                case 47:
                    return "splitScreenForEdgePanel";
                case 48:
                    return "setConfineMode";
                case 49:
                    return "getConfineMode";
                case 50:
                    return "setPermitList";
                case 51:
                    return "registerKeyEventObserver";
                case 52:
                    return "unregisterKeyEventObserver";
                case 53:
                    return "updateAppData";
                case 54:
                    return "registerSysStateChangeObserver";
                case 55:
                    return "unregisterSysStateChangeObserver";
                case 56:
                    return "setGimbalLaunchPkg";
                case 57:
                    return "setPackagesState";
                case 58:
                    return "registerLockScreenCallback";
                case 59:
                    return "unregisterLockScreenCallback";
                case 60:
                    return "notifySplitScreenStateChanged";
                case 61:
                    return "setSplitScreenObserver";
                case 62:
                    return OplusSplitScreenManager.KEY_IS_IN_SPLIT_SCREEN_MODE;
                case 63:
                    return "dismissSplitScreenMode";
                case 64:
                    return "registerSplitScreenObserver";
                case 65:
                    return "unregisterSplitScreenObserver";
                case 66:
                    return "getSplitScreenStatus";
                case 67:
                    return "splitScreenForTopApp";
                case 68:
                    return "splitScreenForRecentTasks";
                case 69:
                    return "setTaskWindowingModeSplitScreen";
                case 70:
                    return "splitScreenForEdgePanelExt";
                case 71:
                    return "hasLargeScreenFeature";
                case 72:
                    return "isFolderInnerScreen";
                case 73:
                    return "getMinimizedBounds";
                case 74:
                    return "getSplitScreenSession";
                case 75:
                    return "splitScreenForApplication";
                case 76:
                    return "getLeftRightBoundsForIme";
                case 77:
                    return "hasColorSplitFeature";
                case 78:
                    return "registerKeyEventInterceptor";
                case 79:
                    return "unregisterKeyEventInterceptor";
                case 80:
                    return "getGlobalDragAndDropConfig";
                case 81:
                    return "setGlobalDragAndDropConfig";
                case 82:
                    return "registerMirageWindowObserver";
                case 83:
                    return "unregisterMirageWindowObserver";
                case 84:
                    return "registerMirageDisplayObserver";
                case 85:
                    return "unregisterMirageDisplayObserver";
                case 86:
                    return "createMirageDisplay";
                case 87:
                    return "getTaskInfo";
                case 88:
                    return "startMirageWindowModeWithName";
                case 89:
                    return "startMirageWindowMode";
                case 90:
                    return "isMirageWindowShow";
                case 91:
                    return "stopMirageWindowModeOld";
                case 92:
                    return "stopMirageWindowMode";
                case 93:
                    return "setMirageDisplaySurfaceById";
                case 94:
                    return "setMirageDisplaySurfaceByMode";
                case 95:
                    return "getMirageDisplayCastMode";
                case 96:
                    return "expandToFullScreen";
                case 97:
                    return "setMirageWindowSilent";
                case 98:
                    return "isSupportMirageWindowMode";
                case 99:
                    return "getMirageWindowInfo";
                case 100:
                    return "updateMirageWindowCastFlag";
                case 101:
                    return "updatePrivacyProtectionList";
                case 102:
                    return "updatePrivacyProtectionListWithBundle";
                case 103:
                    return "createMirageWindowSession";
                case 104:
                    return "getGetDisplayIdForPackageName";
                case 105:
                    return "feedbackUserSelection";
                case 106:
                    return "updateCarModeMultiLaunchWhiteList";
                case 107:
                    return "rebindDisplayIfNeeded";
                case 108:
                    return "registerConfineModeObserver";
                case 109:
                    return "unregisterConfineModeObserver";
                case 110:
                    return "readNodeFile";
                case 111:
                    return "readNodeFileByDevice";
                case 112:
                    return "writeNodeFile";
                case 113:
                    return "writeNodeFileByDevice";
                case 114:
                    return "isTouchNodeSupport";
                case 115:
                    return "writeNodeFileFromBt";
                case 116:
                    return "writeNodeFileOneWay";
                case 117:
                    return "notifyTouchNodeChange";
                case 118:
                    return "registerEventCallback";
                case 119:
                    return "unregisterEventCallback";
                case 120:
                    return "clientTransactionComplete";
                case 121:
                    return "updateUntrustedTouchConfig";
                case 122:
                    return "getAppThemeVersion";
                case 123:
                    return "lockRotationInGame";
                case 124:
                    return "startCompactWindow";
                case 125:
                    return "exitCompactWindow";
                case 126:
                    return "isCurrentAppSupportCompactMode";
                case 127:
                    return "moveCompactWindowToLeft";
                case 128:
                    return "moveCompactWindowToRight";
                case 129:
                    return "registerCompactWindowObserver";
                case 130:
                    return "unregisterCompactWindowObserver";
                case 131:
                    return "getPWAppInfo";
                case 132:
                    return "onProtocolUpdated";
                case 133:
                    return "getFocusMode";
                case 134:
                    return "getFocusBounds";
                case 135:
                    return "getFocusComponent";
                case 136:
                    return "getRealSize";
                case 137:
                    return "callMethod";
                case 138:
                    return "invokeSync";
                case 139:
                    return "addBracketWindowConfigChangedListener";
                case 140:
                    return "removeBracketWindowConfigChangedListener";
                case 141:
                    return "isNeedDisableWindowLayoutInfo";
                case 142:
                    return "bindQuickReplyService";
                case 143:
                    return "unbindQuickReplyService";
                case 144:
                    return "sendMessage";
                case 145:
                    return "shouldInterceptBackKeyForMultiSearch";
                case 146:
                    return "getMultiSearchSession";
                case 147:
                    return "takeScreenshot";
                case 148:
                    return "getZoomTaskController";
                case 149:
                    return "getRenderThreadTid";
                case 150:
                    return "setInputConsumerEnabled";
                case 151:
                    return "adjustWindowFrameForZoom";
                case 152:
                    return "isZoomSupportMultiWindow";
                case 153:
                    return "notifyUiSwitched";
                case 154:
                    return "pauseInRecentsAnim";
                case 155:
                    return "getPauseInRecentsAnimPkgList";
                case 156:
                    return "registerPuttObserver";
                case 157:
                    return "unregisterPuttObserver";
                case 158:
                    return "registerPuttEventObserver";
                case 159:
                    return "unregisterPuttEventObserver";
                case 160:
                    return "stopPutt";
                case 161:
                    return "startPutt";
                case TRANSACTION_getEnterPuttAppInfos /* 162 */:
                    return "getEnterPuttAppInfos";
                case 163:
                    return "removePuttTask";
                case 164:
                    return "isSupportPuttMode";
                case 165:
                    return "registerFlexibleWindowObserver";
                case 166:
                    return "unregisterFlexibleWindowObserver";
                case 167:
                    return "calculateFlexibleWindowBounds";
                case 168:
                    return "isInPocketStudio";
                case 169:
                    return "isInPocketStudioForStandard";
                case 170:
                    return "getPocketStudioTaskRegion";
                case 171:
                    return "setMinimizedPocketStudio";
                case 172:
                    return "isMinimizedPocketStudio";
                case 173:
                    return "setFlexibleFrame";
                case 174:
                    return "setIsInFocusAnimating";
                case 175:
                    return "setEmbeddedContainerTask";
                case 176:
                    return "removeEmbeddedContainerTask";
                case 177:
                    return "exitFlexibleEmbeddedTask";
                case 178:
                    return "setFlexibleTaskEmbedding";
                case 179:
                    return "resetFlexibleTask";
                case 180:
                    return "getRecentEmbeddedTasksForContainer";
                case 181:
                    return "startActivityInTask";
                case 182:
                    return "startAnyActivity";
                case 183:
                    return "startFlexibleWindowForRecentTasks";
                case 184:
                    return "isClickAtPocketStudioArea";
                case 185:
                    return "calculateCanvasLayoutRect";
                case 186:
                    return "calculateReplaceCanvasLayoutRect";
                case 187:
                    return "isInAppInnerSplitScreen";
                case 188:
                    return "notifyEmbeddedTasksChangeFocus";
                case 189:
                    return "notifyFlexibleSplitScreenStateChanged";
                case 190:
                    return "registerEmbeddedWindowContainerCallback";
                case 191:
                    return "unregisterEmbeddedWindowContainerCallback";
                case 192:
                    return "getActivityConfigs";
                case 193:
                    return "adjustWindowFrame";
                case 194:
                    return "updateRecordSurfaceViewState";
                case 195:
                    return "reportViewExtractResult";
                case 196:
                    return "requestViewExtractData";
                case 197:
                    return "updateTaskVisibility";
                case 198:
                    return "shouldSkipStartPocketStudio";
                case 199:
                    return "createVirtualDisplayDevice";
                case 200:
                    return "releaseVirtualDisplayDevice";
                case 201:
                    return "moveTaskToDisplay";
                case 202:
                    return "isFlexibleTaskEnabled";
                case 203:
                    return "setFlexibleTaskEnabled";
                case 204:
                    return "adjustEndAbsForFlexibleMinimizeIfNeed";
                case 205:
                    return "getEmbeddedChildrenSnapshot";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, final Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusActivityTaskManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusActivityTaskManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IActivityController _arg0 = IActivityController.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setSecureController(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            ComponentName _result = getTopActivityComponentName();
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 3:
                            ApplicationInfo _result2 = getTopApplicationInfo();
                            reply.writeNoException();
                            reply.writeTypedObject(_result2, 1);
                            return true;
                        case 4:
                            Intent _arg02 = (Intent) data.readTypedObject(Intent.CREATOR);
                            data.enforceNoDataAvail();
                            int _result3 = getSplitScreenState(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 5:
                            List<OplusAppInfo> _result4 = getAllTopAppInfos();
                            reply.writeNoException();
                            reply.writeTypedList(_result4, 1);
                            return true;
                        case 6:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result5 = getFreeformConfigList(_arg03);
                            reply.writeNoException();
                            reply.writeStringList(_result5);
                            return true;
                        case 7:
                            boolean _result6 = isFreeformEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 8:
                            IOplusFreeformConfigChangedListener _arg04 = IOplusFreeformConfigChangedListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result7 = addFreeformConfigChangedListener(_arg04);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 9:
                            IOplusFreeformConfigChangedListener _arg05 = IOplusFreeformConfigChangedListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result8 = removeFreeformConfigChangedListener(_arg05);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 10:
                            String _arg06 = data.readString();
                            IOplusAppSwitchObserver _arg1 = IOplusAppSwitchObserver.Stub.asInterface(data.readStrongBinder());
                            OplusAppSwitchConfig _arg2 = (OplusAppSwitchConfig) data.readTypedObject(OplusAppSwitchConfig.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result9 = registerAppSwitchObserver(_arg06, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 11:
                            String _arg07 = data.readString();
                            OplusAppSwitchConfig _arg12 = (OplusAppSwitchConfig) data.readTypedObject(OplusAppSwitchConfig.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result10 = unregisterAppSwitchObserver(_arg07, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 12:
                            String _arg08 = data.readString();
                            IDragAndDropListener _arg13 = IDragAndDropListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerDragAndDropListener(_arg08, _arg13);
                            reply.writeNoException();
                            return true;
                        case 13:
                            String _arg09 = data.readString();
                            IDragAndDropListener _arg14 = IDragAndDropListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterDragAndDropListener(_arg09, _arg14);
                            reply.writeNoException();
                            return true;
                        case 14:
                            Intent _arg010 = (Intent) data.readTypedObject(Intent.CREATOR);
                            Bundle _arg15 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            int _arg22 = data.readInt();
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            int _result11 = startZoomWindow(_arg010, _arg15, _arg22, _arg3);
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 15:
                            IOplusZoomWindowObserver _arg011 = IOplusZoomWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result12 = registerZoomWindowObserver(_arg011);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 16:
                            IOplusZoomWindowObserver _arg012 = IOplusZoomWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result13 = unregisterZoomWindowObserver(_arg012);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 17:
                            OplusZoomWindowInfo _result14 = getCurrentZoomWindowState();
                            reply.writeNoException();
                            reply.writeTypedObject(_result14, 1);
                            return true;
                        case 18:
                            int _arg013 = data.readInt();
                            data.enforceNoDataAvail();
                            hideZoomWindow(_arg013);
                            reply.writeNoException();
                            return true;
                        case 19:
                            int _arg014 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result15 = getZoomAppConfigList(_arg014);
                            reply.writeNoException();
                            reply.writeStringList(_result15);
                            return true;
                        case 20:
                            String _arg015 = data.readString();
                            int _arg16 = data.readInt();
                            String _arg23 = data.readString();
                            Bundle _arg32 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result16 = isSupportZoomMode(_arg015, _arg16, _arg23, _arg32);
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 21:
                            String _arg016 = data.readString();
                            int _arg17 = data.readInt();
                            String _arg24 = data.readString();
                            Bundle _arg33 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            int _arg4 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result17 = handleShowCompatibilityToast(_arg016, _arg17, _arg24, _arg33, _arg4);
                            reply.writeNoException();
                            reply.writeBoolean(_result17);
                            return true;
                        case 22:
                            int _arg017 = data.readInt();
                            data.enforceNoDataAvail();
                            startMiniZoomFromZoom(_arg017);
                            reply.writeNoException();
                            return true;
                        case 23:
                            String _arg018 = data.readString();
                            int _arg18 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyZoomStateChange(_arg018, _arg18);
                            reply.writeNoException();
                            return true;
                        case 24:
                            OplusZoomInputEventInfo _arg019 = (OplusZoomInputEventInfo) data.readTypedObject(OplusZoomInputEventInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onInputEvent(_arg019);
                            reply.writeNoException();
                            return true;
                        case 25:
                            OplusZoomControlViewInfo _arg020 = (OplusZoomControlViewInfo) data.readTypedObject(OplusZoomControlViewInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onControlViewChanged(_arg020);
                            reply.writeNoException();
                            return true;
                        case 26:
                            IOplusZoomAppObserver _arg021 = IOplusZoomAppObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result18 = registerZoomAppObserver(_arg021);
                            reply.writeNoException();
                            reply.writeBoolean(_result18);
                            return true;
                        case 27:
                            IOplusZoomAppObserver _arg022 = IOplusZoomAppObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result19 = unregisterZoomAppObserver(_arg022);
                            reply.writeNoException();
                            reply.writeBoolean(_result19);
                            return true;
                        case 28:
                            ISecurityPageController _arg023 = ISecurityPageController.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result20 = registerSecurityPageCallback(_arg023);
                            reply.writeNoException();
                            reply.writeBoolean(_result20);
                            return true;
                        case 29:
                            ISecurityPageController _arg024 = ISecurityPageController.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result21 = unregisterSecurityPageCallback(_arg024);
                            reply.writeNoException();
                            reply.writeBoolean(_result21);
                            return true;
                        case 30:
                            boolean _result22 = getSecurityFlagCurrentPage();
                            reply.writeNoException();
                            reply.writeBoolean(_result22);
                            return true;
                        case 31:
                            List<String> _arg025 = data.createStringArrayList();
                            boolean _arg19 = data.readBoolean();
                            data.enforceNoDataAvail();
                            updateDeferStartingWindowApps(_arg025, _arg19);
                            return true;
                        case 32:
                            OplusZoomFloatHandleViewInfo _arg026 = (OplusZoomFloatHandleViewInfo) data.readTypedObject(OplusZoomFloatHandleViewInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onFloatHandleViewChanged(_arg026);
                            reply.writeNoException();
                            return true;
                        case 33:
                            boolean _result23 = isSupportZoomWindowMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result23);
                            return true;
                        case 34:
                            OplusZoomWindowRUSConfig _result24 = getZoomWindowConfig();
                            reply.writeNoException();
                            reply.writeTypedObject(_result24, 1);
                            return true;
                        case 35:
                            OplusZoomWindowRUSConfig _arg027 = (OplusZoomWindowRUSConfig) data.readTypedObject(OplusZoomWindowRUSConfig.CREATOR);
                            data.enforceNoDataAvail();
                            setZoomWindowConfig(_arg027);
                            reply.writeNoException();
                            return true;
                        case 36:
                            IOplusZoomWindowConfigChangedListener _arg028 = IOplusZoomWindowConfigChangedListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result25 = addZoomWindowConfigChangedListener(_arg028);
                            reply.writeNoException();
                            reply.writeBoolean(_result25);
                            return true;
                        case 37:
                            IOplusZoomWindowConfigChangedListener _arg029 = IOplusZoomWindowConfigChangedListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result26 = removeZoomWindowConfigChangedListener(_arg029);
                            reply.writeNoException();
                            reply.writeBoolean(_result26);
                            return true;
                        case 38:
                            String _arg030 = data.readString();
                            String[] _arg110 = data.createStringArray();
                            data.enforceNoDataAvail();
                            startLockDeviceMode(_arg030, _arg110);
                            reply.writeNoException();
                            return true;
                        case 39:
                            stopLockDeviceMode();
                            reply.writeNoException();
                            return true;
                        case 40:
                            boolean _result27 = isLockDeviceMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result27);
                            return true;
                        case 41:
                            boolean _result28 = isZoomSimpleModeEnable();
                            reply.writeNoException();
                            reply.writeBoolean(_result28);
                            return true;
                        case 42:
                            String _arg031 = data.readString();
                            String _arg111 = data.readString();
                            List<String> _arg25 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result29 = writeEdgeTouchPreventParam(_arg031, _arg111, _arg25);
                            reply.writeNoException();
                            reply.writeBoolean(_result29);
                            return true;
                        case 43:
                            String _arg032 = data.readString();
                            List<String> _arg112 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            setDefaultEdgeTouchPreventParam(_arg032, _arg112);
                            reply.writeNoException();
                            return true;
                        case 44:
                            String _arg033 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result30 = resetDefaultEdgeTouchPreventParam(_arg033);
                            reply.writeNoException();
                            reply.writeBoolean(_result30);
                            return true;
                        case 45:
                            boolean _result31 = isSupportEdgeTouchPrevent();
                            reply.writeNoException();
                            reply.writeBoolean(_result31);
                            return true;
                        case 46:
                            String _arg034 = data.readString();
                            int N = data.readInt();
                            final Map<String, List<String>> _arg113 = N < 0 ? null : new HashMap<>();
                            IntStream.range(0, N).forEach(new IntConsumer() { // from class: android.app.IOplusActivityTaskManager$Stub$$ExternalSyntheticLambda0
                                @Override // java.util.function.IntConsumer
                                public final void accept(int i) {
                                    IOplusActivityTaskManager.Stub.lambda$onTransact$0(data, _arg113, i);
                                }
                            });
                            data.enforceNoDataAvail();
                            setEdgeTouchCallRules(_arg034, _arg113);
                            reply.writeNoException();
                            return true;
                        case 47:
                            Intent _arg035 = (Intent) data.readTypedObject(Intent.CREATOR);
                            int _arg114 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result32 = splitScreenForEdgePanel(_arg035, _arg114);
                            reply.writeNoException();
                            reply.writeInt(_result32);
                            return true;
                        case 48:
                            int _arg036 = data.readInt();
                            boolean _arg115 = data.readBoolean();
                            IBinder _arg26 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            setConfineMode(_arg036, _arg115, _arg26);
                            reply.writeNoException();
                            return true;
                        case 49:
                            int _result33 = getConfineMode();
                            reply.writeNoException();
                            reply.writeInt(_result33);
                            return true;
                        case 50:
                            int _arg037 = data.readInt();
                            int _arg116 = data.readInt();
                            List<String> _arg27 = data.createStringArrayList();
                            boolean _arg34 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setPermitList(_arg037, _arg116, _arg27, _arg34);
                            reply.writeNoException();
                            return true;
                        case 51:
                            String _arg038 = data.readString();
                            IOplusKeyEventObserver _arg117 = IOplusKeyEventObserver.Stub.asInterface(data.readStrongBinder());
                            int _arg28 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result34 = registerKeyEventObserver(_arg038, _arg117, _arg28);
                            reply.writeNoException();
                            reply.writeBoolean(_result34);
                            return true;
                        case 52:
                            String _arg039 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result35 = unregisterKeyEventObserver(_arg039);
                            reply.writeNoException();
                            reply.writeBoolean(_result35);
                            return true;
                        case 53:
                            String _arg040 = data.readString();
                            Bundle _arg118 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result36 = updateAppData(_arg040, _arg118);
                            reply.writeNoException();
                            reply.writeBoolean(_result36);
                            return true;
                        case 54:
                            String _arg041 = data.readString();
                            ISysStateChangeCallback _arg119 = ISysStateChangeCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result37 = registerSysStateChangeObserver(_arg041, _arg119);
                            reply.writeNoException();
                            reply.writeBoolean(_result37);
                            return true;
                        case 55:
                            String _arg042 = data.readString();
                            ISysStateChangeCallback _arg120 = ISysStateChangeCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result38 = unregisterSysStateChangeObserver(_arg042, _arg120);
                            reply.writeNoException();
                            reply.writeBoolean(_result38);
                            return true;
                        case 56:
                            String _arg043 = data.readString();
                            data.enforceNoDataAvail();
                            setGimbalLaunchPkg(_arg043);
                            reply.writeNoException();
                            return true;
                        case 57:
                            ClassLoader cl = getClass().getClassLoader();
                            Map _arg044 = data.readHashMap(cl);
                            data.enforceNoDataAvail();
                            setPackagesState(_arg044);
                            reply.writeNoException();
                            return true;
                        case 58:
                            IOplusLockScreenCallback _arg045 = IOplusLockScreenCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result39 = registerLockScreenCallback(_arg045);
                            reply.writeNoException();
                            reply.writeBoolean(_result39);
                            return true;
                        case 59:
                            IOplusLockScreenCallback _arg046 = IOplusLockScreenCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result40 = unregisterLockScreenCallback(_arg046);
                            reply.writeNoException();
                            reply.writeBoolean(_result40);
                            return true;
                        case 60:
                            String _arg047 = data.readString();
                            Bundle _arg121 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            boolean _arg29 = data.readBoolean();
                            data.enforceNoDataAvail();
                            notifySplitScreenStateChanged(_arg047, _arg121, _arg29);
                            reply.writeNoException();
                            return true;
                        case 61:
                            IOplusSplitScreenObserver _arg048 = IOplusSplitScreenObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result41 = setSplitScreenObserver(_arg048);
                            reply.writeNoException();
                            reply.writeBoolean(_result41);
                            return true;
                        case 62:
                            boolean _result42 = isInSplitScreenMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result42);
                            return true;
                        case 63:
                            int _arg049 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result43 = dismissSplitScreenMode(_arg049);
                            reply.writeNoException();
                            reply.writeBoolean(_result43);
                            return true;
                        case 64:
                            int _arg050 = data.readInt();
                            IOplusSplitScreenObserver _arg122 = IOplusSplitScreenObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result44 = registerSplitScreenObserver(_arg050, _arg122);
                            reply.writeNoException();
                            reply.writeBoolean(_result44);
                            return true;
                        case 65:
                            int _arg051 = data.readInt();
                            IOplusSplitScreenObserver _arg123 = IOplusSplitScreenObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result45 = unregisterSplitScreenObserver(_arg051, _arg123);
                            reply.writeNoException();
                            reply.writeBoolean(_result45);
                            return true;
                        case 66:
                            String _arg052 = data.readString();
                            data.enforceNoDataAvail();
                            Bundle _result46 = getSplitScreenStatus(_arg052);
                            reply.writeNoException();
                            reply.writeTypedObject(_result46, 1);
                            return true;
                        case 67:
                            int _arg053 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result47 = splitScreenForTopApp(_arg053);
                            reply.writeNoException();
                            reply.writeBoolean(_result47);
                            return true;
                        case 68:
                            int _arg054 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result48 = splitScreenForRecentTasks(_arg054);
                            reply.writeNoException();
                            reply.writeBoolean(_result48);
                            return true;
                        case 69:
                            int _arg055 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result49 = setTaskWindowingModeSplitScreen(_arg055);
                            reply.writeNoException();
                            reply.writeInt(_result49);
                            return true;
                        case 70:
                            Intent _arg056 = (Intent) data.readTypedObject(Intent.CREATOR);
                            boolean _arg124 = data.readBoolean();
                            int _arg210 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result50 = splitScreenForEdgePanelExt(_arg056, _arg124, _arg210);
                            reply.writeNoException();
                            reply.writeBoolean(_result50);
                            return true;
                        case 71:
                            boolean _result51 = hasLargeScreenFeature();
                            reply.writeNoException();
                            reply.writeBoolean(_result51);
                            return true;
                        case 72:
                            boolean _result52 = isFolderInnerScreen();
                            reply.writeNoException();
                            reply.writeBoolean(_result52);
                            return true;
                        case 73:
                            int _arg057 = data.readInt();
                            data.enforceNoDataAvail();
                            Rect _result53 = getMinimizedBounds(_arg057);
                            reply.writeNoException();
                            reply.writeTypedObject(_result53, 1);
                            return true;
                        case 74:
                            IOplusSplitScreenSession _result54 = getSplitScreenSession();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result54);
                            return true;
                        case 75:
                            PendingIntent _arg058 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                            int _arg125 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result55 = splitScreenForApplication(_arg058, _arg125);
                            reply.writeNoException();
                            reply.writeBoolean(_result55);
                            return true;
                        case 76:
                            Bundle _result56 = getLeftRightBoundsForIme();
                            reply.writeNoException();
                            reply.writeTypedObject(_result56, 1);
                            return true;
                        case 77:
                            boolean _result57 = hasColorSplitFeature();
                            reply.writeNoException();
                            reply.writeBoolean(_result57);
                            return true;
                        case 78:
                            String _arg059 = data.readString();
                            IOplusKeyEventObserver _arg126 = IOplusKeyEventObserver.Stub.asInterface(data.readStrongBinder());
                            ClassLoader cl2 = getClass().getClassLoader();
                            Map _arg211 = data.readHashMap(cl2);
                            data.enforceNoDataAvail();
                            boolean _result58 = registerKeyEventInterceptor(_arg059, _arg126, _arg211);
                            reply.writeNoException();
                            reply.writeBoolean(_result58);
                            return true;
                        case 79:
                            String _arg060 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result59 = unregisterKeyEventInterceptor(_arg060);
                            reply.writeNoException();
                            reply.writeBoolean(_result59);
                            return true;
                        case 80:
                            OplusGlobalDragAndDropRUSConfig _result60 = getGlobalDragAndDropConfig();
                            reply.writeNoException();
                            reply.writeTypedObject(_result60, 1);
                            return true;
                        case 81:
                            OplusGlobalDragAndDropRUSConfig _arg061 = (OplusGlobalDragAndDropRUSConfig) data.readTypedObject(OplusGlobalDragAndDropRUSConfig.CREATOR);
                            data.enforceNoDataAvail();
                            setGlobalDragAndDropConfig(_arg061);
                            reply.writeNoException();
                            return true;
                        case 82:
                            IOplusMirageWindowObserver _arg062 = IOplusMirageWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result61 = registerMirageWindowObserver(_arg062);
                            reply.writeNoException();
                            reply.writeBoolean(_result61);
                            return true;
                        case 83:
                            IOplusMirageWindowObserver _arg063 = IOplusMirageWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result62 = unregisterMirageWindowObserver(_arg063);
                            reply.writeNoException();
                            reply.writeBoolean(_result62);
                            return true;
                        case 84:
                            IOplusMirageDisplayObserver _arg064 = IOplusMirageDisplayObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result63 = registerMirageDisplayObserver(_arg064);
                            reply.writeNoException();
                            reply.writeBoolean(_result63);
                            return true;
                        case 85:
                            IOplusMirageDisplayObserver _arg065 = IOplusMirageDisplayObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result64 = unregisterMirageDisplayObserver(_arg065);
                            reply.writeNoException();
                            reply.writeBoolean(_result64);
                            return true;
                        case 86:
                            Surface _arg066 = (Surface) data.readTypedObject(Surface.CREATOR);
                            data.enforceNoDataAvail();
                            int _result65 = createMirageDisplay(_arg066);
                            reply.writeNoException();
                            reply.writeInt(_result65);
                            return true;
                        case 87:
                            ComponentName _arg067 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            Bundle _result66 = getTaskInfo(_arg067);
                            reply.writeNoException();
                            reply.writeTypedObject(_result66, 1);
                            return true;
                        case 88:
                            ComponentName _arg068 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg127 = data.readInt();
                            int _arg212 = data.readInt();
                            Bundle _arg35 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            startMirageWindowModeWithName(_arg068, _arg127, _arg212, _arg35);
                            reply.writeNoException();
                            return true;
                        case 89:
                            Intent _arg069 = (Intent) data.readTypedObject(Intent.CREATOR);
                            Bundle _arg128 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            int _result67 = startMirageWindowMode(_arg069, _arg128);
                            reply.writeNoException();
                            reply.writeInt(_result67);
                            return true;
                        case 90:
                            boolean _result68 = isMirageWindowShow();
                            reply.writeNoException();
                            reply.writeBoolean(_result68);
                            return true;
                        case 91:
                            stopMirageWindowModeOld();
                            reply.writeNoException();
                            return true;
                        case 92:
                            Bundle _arg070 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            stopMirageWindowMode(_arg070);
                            reply.writeNoException();
                            return true;
                        case 93:
                            int _arg071 = data.readInt();
                            Surface _arg129 = (Surface) data.readTypedObject(Surface.CREATOR);
                            data.enforceNoDataAvail();
                            setMirageDisplaySurfaceById(_arg071, _arg129);
                            reply.writeNoException();
                            return true;
                        case 94:
                            int _arg072 = data.readInt();
                            Surface _arg130 = (Surface) data.readTypedObject(Surface.CREATOR);
                            data.enforceNoDataAvail();
                            setMirageDisplaySurfaceByMode(_arg072, _arg130);
                            reply.writeNoException();
                            return true;
                        case 95:
                            int _arg073 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result69 = getMirageDisplayCastMode(_arg073);
                            reply.writeNoException();
                            reply.writeInt(_result69);
                            return true;
                        case 96:
                            expandToFullScreen();
                            reply.writeNoException();
                            return true;
                        case 97:
                            String _arg074 = data.readString();
                            data.enforceNoDataAvail();
                            setMirageWindowSilent(_arg074);
                            reply.writeNoException();
                            return true;
                        case 98:
                            boolean _result70 = isSupportMirageWindowMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result70);
                            return true;
                        case 99:
                            OplusMirageWindowInfo _result71 = getMirageWindowInfo();
                            reply.writeNoException();
                            reply.writeTypedObject(_result71, 1);
                            return true;
                        case 100:
                            int _arg075 = data.readInt();
                            Bundle _arg131 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result72 = updateMirageWindowCastFlag(_arg075, _arg131);
                            reply.writeNoException();
                            reply.writeBoolean(_result72);
                            return true;
                        case 101:
                            List<String> _arg076 = data.createStringArrayList();
                            boolean _arg132 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result73 = updatePrivacyProtectionList(_arg076, _arg132);
                            reply.writeNoException();
                            reply.writeBoolean(_result73);
                            return true;
                        case 102:
                            List<String> _arg077 = data.createStringArrayList();
                            boolean _arg133 = data.readBoolean();
                            boolean _arg213 = data.readBoolean();
                            Bundle _arg36 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result74 = updatePrivacyProtectionListWithBundle(_arg077, _arg133, _arg213, _arg36);
                            reply.writeNoException();
                            reply.writeBoolean(_result74);
                            return true;
                        case 103:
                            IOplusMirageSessionCallback _arg078 = IOplusMirageSessionCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            IOplusMirageWindowSession _result75 = createMirageWindowSession(_arg078);
                            reply.writeNoException();
                            reply.writeStrongInterface(_result75);
                            return true;
                        case 104:
                            String _arg079 = data.readString();
                            data.enforceNoDataAvail();
                            int _result76 = getGetDisplayIdForPackageName(_arg079);
                            reply.writeNoException();
                            reply.writeInt(_result76);
                            return true;
                        case 105:
                            int _arg080 = data.readInt();
                            int _arg134 = data.readInt();
                            Bundle _arg214 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            feedbackUserSelection(_arg080, _arg134, _arg214);
                            reply.writeNoException();
                            return true;
                        case 106:
                            String _arg081 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result77 = updateCarModeMultiLaunchWhiteList(_arg081);
                            reply.writeNoException();
                            reply.writeBoolean(_result77);
                            return true;
                        case 107:
                            int _arg082 = data.readInt();
                            int _arg135 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result78 = rebindDisplayIfNeeded(_arg082, _arg135);
                            reply.writeNoException();
                            reply.writeBoolean(_result78);
                            return true;
                        case 108:
                            IOplusConfineModeObserver _arg083 = IOplusConfineModeObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result79 = registerConfineModeObserver(_arg083);
                            reply.writeNoException();
                            reply.writeBoolean(_result79);
                            return true;
                        case 109:
                            IOplusConfineModeObserver _arg084 = IOplusConfineModeObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result80 = unregisterConfineModeObserver(_arg084);
                            reply.writeNoException();
                            reply.writeBoolean(_result80);
                            return true;
                        case 110:
                            int _arg085 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result81 = readNodeFile(_arg085);
                            reply.writeNoException();
                            reply.writeString(_result81);
                            return true;
                        case 111:
                            int _arg086 = data.readInt();
                            int _arg136 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result82 = readNodeFileByDevice(_arg086, _arg136);
                            reply.writeNoException();
                            reply.writeString(_result82);
                            return true;
                        case 112:
                            int _arg087 = data.readInt();
                            String _arg137 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result83 = writeNodeFile(_arg087, _arg137);
                            reply.writeNoException();
                            reply.writeBoolean(_result83);
                            return true;
                        case 113:
                            int _arg088 = data.readInt();
                            int _arg138 = data.readInt();
                            String _arg215 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result84 = writeNodeFileByDevice(_arg088, _arg138, _arg215);
                            reply.writeNoException();
                            reply.writeBoolean(_result84);
                            return true;
                        case 114:
                            int _arg089 = data.readInt();
                            int _arg139 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result85 = isTouchNodeSupport(_arg089, _arg139);
                            reply.writeNoException();
                            reply.writeBoolean(_result85);
                            return true;
                        case 115:
                            int _arg090 = data.readInt();
                            int _arg140 = data.readInt();
                            String _arg216 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result86 = writeNodeFileFromBt(_arg090, _arg140, _arg216);
                            reply.writeNoException();
                            reply.writeBoolean(_result86);
                            return true;
                        case 116:
                            int _arg091 = data.readInt();
                            int _arg141 = data.readInt();
                            String _arg217 = data.readString();
                            data.enforceNoDataAvail();
                            writeNodeFileOneWay(_arg091, _arg141, _arg217);
                            reply.writeNoException();
                            return true;
                        case 117:
                            int _arg092 = data.readInt();
                            long _arg142 = data.readLong();
                            int _arg218 = data.readInt();
                            int _arg37 = data.readInt();
                            int _arg42 = data.readInt();
                            String _arg5 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result87 = notifyTouchNodeChange(_arg092, _arg142, _arg218, _arg37, _arg42, _arg5);
                            reply.writeNoException();
                            reply.writeBoolean(_result87);
                            return true;
                        case 118:
                            IOplusTouchNodeCallback _arg093 = IOplusTouchNodeCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result88 = registerEventCallback(_arg093);
                            reply.writeNoException();
                            reply.writeBoolean(_result88);
                            return true;
                        case 119:
                            IBinder _arg094 = data.readStrongBinder();
                            IOplusTouchNodeCallback _arg095 = IOplusTouchNodeCallback.Stub.asInterface(_arg094);
                            data.enforceNoDataAvail();
                            boolean _result89 = unregisterEventCallback(_arg095);
                            reply.writeNoException();
                            reply.writeBoolean(_result89);
                            return true;
                        case 120:
                            IBinder _arg096 = data.readStrongBinder();
                            int _arg143 = data.readInt();
                            data.enforceNoDataAvail();
                            clientTransactionComplete(_arg096, _arg143);
                            reply.writeNoException();
                            return true;
                        case 121:
                            String _arg097 = data.readString();
                            boolean _arg144 = data.readBoolean();
                            data.enforceNoDataAvail();
                            updateUntrustedTouchConfig(_arg097, _arg144);
                            return true;
                        case 122:
                            String _arg098 = data.readString();
                            boolean _arg145 = data.readBoolean();
                            data.enforceNoDataAvail();
                            String _result90 = getAppThemeVersion(_arg098, _arg145);
                            reply.writeNoException();
                            reply.writeString(_result90);
                            return true;
                        case 123:
                            int _arg099 = data.readInt();
                            String[] _arg146 = data.createStringArray();
                            data.enforceNoDataAvail();
                            boolean _result91 = lockRotationInGame(_arg099, _arg146);
                            reply.writeNoException();
                            reply.writeBoolean(_result91);
                            return true;
                        case 124:
                            int _result92 = startCompactWindow();
                            reply.writeNoException();
                            reply.writeInt(_result92);
                            return true;
                        case 125:
                            int _result93 = exitCompactWindow();
                            reply.writeNoException();
                            reply.writeInt(_result93);
                            return true;
                        case 126:
                            boolean _result94 = isCurrentAppSupportCompactMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result94);
                            return true;
                        case 127:
                            int _result95 = moveCompactWindowToLeft();
                            reply.writeNoException();
                            reply.writeInt(_result95);
                            return true;
                        case 128:
                            int _result96 = moveCompactWindowToRight();
                            reply.writeNoException();
                            reply.writeInt(_result96);
                            return true;
                        case 129:
                            IOplusCompactWindowObserver _arg0100 = IOplusCompactWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result97 = registerCompactWindowObserver(_arg0100);
                            reply.writeNoException();
                            reply.writeBoolean(_result97);
                            return true;
                        case 130:
                            IOplusCompactWindowObserver _arg0101 = IOplusCompactWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result98 = unregisterCompactWindowObserver(_arg0101);
                            reply.writeNoException();
                            reply.writeBoolean(_result98);
                            return true;
                        case 131:
                            Map _result99 = getPWAppInfo();
                            reply.writeNoException();
                            reply.writeMap(_result99);
                            return true;
                        case 132:
                            String _arg0102 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result100 = onProtocolUpdated(_arg0102);
                            reply.writeNoException();
                            reply.writeBoolean(_result100);
                            return true;
                        case 133:
                            int _result101 = getFocusMode();
                            reply.writeNoException();
                            reply.writeInt(_result101);
                            return true;
                        case 134:
                            boolean _arg0103 = data.readBoolean();
                            data.enforceNoDataAvail();
                            Rect _result102 = getFocusBounds(_arg0103);
                            reply.writeNoException();
                            reply.writeTypedObject(_result102, 1);
                            return true;
                        case 135:
                            boolean _arg0104 = data.readBoolean();
                            data.enforceNoDataAvail();
                            ComponentName _result103 = getFocusComponent(_arg0104);
                            reply.writeNoException();
                            reply.writeTypedObject(_result103, 1);
                            return true;
                        case 136:
                            Point _result104 = getRealSize();
                            reply.writeNoException();
                            reply.writeTypedObject(_result104, 1);
                            return true;
                        case 137:
                            String _arg0105 = data.readString();
                            String _arg147 = data.readString();
                            int _arg219 = data.readInt();
                            boolean _arg38 = data.readBoolean();
                            String _arg43 = data.readString();
                            Bundle _arg52 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            Bundle _result105 = callMethod(_arg0105, _arg147, _arg219, _arg38, _arg43, _arg52);
                            reply.writeNoException();
                            reply.writeTypedObject(_result105, 1);
                            return true;
                        case 138:
                            String _arg0106 = data.readString();
                            String _arg148 = data.readString();
                            String _arg220 = data.readString();
                            Bundle _arg39 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            Bundle _result106 = invokeSync(_arg0106, _arg148, _arg220, _arg39);
                            reply.writeNoException();
                            reply.writeTypedObject(_result106, 1);
                            return true;
                        case 139:
                            IOplusBracketModeChangedListener _arg0107 = IOplusBracketModeChangedListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result107 = addBracketWindowConfigChangedListener(_arg0107);
                            reply.writeNoException();
                            reply.writeBoolean(_result107);
                            return true;
                        case 140:
                            IOplusBracketModeChangedListener _arg0108 = IOplusBracketModeChangedListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result108 = removeBracketWindowConfigChangedListener(_arg0108);
                            reply.writeNoException();
                            reply.writeBoolean(_result108);
                            return true;
                        case 141:
                            ComponentName _arg0109 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result109 = isNeedDisableWindowLayoutInfo(_arg0109);
                            reply.writeNoException();
                            reply.writeBoolean(_result109);
                            return true;
                        case 142:
                            IQuickReplyCallback _arg0110 = IQuickReplyCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            bindQuickReplyService(_arg0110);
                            reply.writeNoException();
                            return true;
                        case 143:
                            unbindQuickReplyService();
                            reply.writeNoException();
                            return true;
                        case 144:
                            PendingIntent _arg0111 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                            String _arg149 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result110 = sendMessage(_arg0111, _arg149);
                            reply.writeNoException();
                            reply.writeBoolean(_result110);
                            return true;
                        case 145:
                            IBinder _arg0112 = data.readStrongBinder();
                            boolean _arg150 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result111 = shouldInterceptBackKeyForMultiSearch(_arg0112, _arg150);
                            reply.writeNoException();
                            reply.writeBoolean(_result111);
                            return true;
                        case 146:
                            IOplusMultiSearchManagerSession _result112 = getMultiSearchSession();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result112);
                            return true;
                        case 147:
                            WindowContainerToken _arg0113 = (WindowContainerToken) data.readTypedObject(WindowContainerToken.CREATOR);
                            SurfaceControl _arg151 = (SurfaceControl) data.readTypedObject(SurfaceControl.CREATOR);
                            data.enforceNoDataAvail();
                            SurfaceControl _result113 = takeScreenshot(_arg0113, _arg151);
                            reply.writeNoException();
                            reply.writeTypedObject(_result113, 1);
                            return true;
                        case 148:
                            IOplusZoomTaskController _result114 = getZoomTaskController();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result114);
                            return true;
                        case 149:
                            int _arg0114 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result115 = getRenderThreadTid(_arg0114);
                            reply.writeNoException();
                            reply.writeInt(_result115);
                            return true;
                        case 150:
                            boolean _arg0115 = data.readBoolean();
                            IRecentsAnimationController _arg152 = IRecentsAnimationController.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result116 = setInputConsumerEnabled(_arg0115, _arg152);
                            reply.writeNoException();
                            reply.writeBoolean(_result116);
                            return true;
                        case 151:
                            WindowManager.LayoutParams _arg0116 = (WindowManager.LayoutParams) data.readTypedObject(WindowManager.LayoutParams.CREATOR);
                            int _arg153 = data.readInt();
                            Rect _arg221 = (Rect) data.readTypedObject(Rect.CREATOR);
                            Rect _arg310 = (Rect) data.readTypedObject(Rect.CREATOR);
                            data.enforceNoDataAvail();
                            adjustWindowFrameForZoom(_arg0116, _arg153, _arg221, _arg310);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg221, 1);
                            reply.writeTypedObject(_arg310, 1);
                            return true;
                        case 152:
                            String _arg0117 = data.readString();
                            ComponentName _arg154 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result117 = isZoomSupportMultiWindow(_arg0117, _arg154);
                            reply.writeNoException();
                            reply.writeBoolean(_result117);
                            return true;
                        case 153:
                            String _arg0118 = data.readString();
                            int _arg155 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyUiSwitched(_arg0118, _arg155);
                            return true;
                        case 154:
                            int _arg0119 = data.readInt();
                            data.enforceNoDataAvail();
                            pauseInRecentsAnim(_arg0119);
                            return true;
                        case 155:
                            List<String> _result118 = getPauseInRecentsAnimPkgList();
                            reply.writeNoException();
                            reply.writeStringList(_result118);
                            return true;
                        case 156:
                            IPuttObserver _arg0120 = IPuttObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result119 = registerPuttObserver(_arg0120);
                            reply.writeNoException();
                            reply.writeBoolean(_result119);
                            return true;
                        case 157:
                            IPuttObserver _arg0121 = IPuttObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result120 = unregisterPuttObserver(_arg0121);
                            reply.writeNoException();
                            reply.writeBoolean(_result120);
                            return true;
                        case 158:
                            IPuttEventObserver _arg0122 = IPuttEventObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result121 = registerPuttEventObserver(_arg0122);
                            reply.writeNoException();
                            reply.writeBoolean(_result121);
                            return true;
                        case 159:
                            IPuttEventObserver _arg0123 = IPuttEventObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result122 = unregisterPuttEventObserver(_arg0123);
                            reply.writeNoException();
                            reply.writeBoolean(_result122);
                            return true;
                        case 160:
                            String _arg0124 = data.readString();
                            int _arg156 = data.readInt();
                            Bundle _arg222 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result123 = stopPutt(_arg0124, _arg156, _arg222);
                            reply.writeNoException();
                            reply.writeBoolean(_result123);
                            return true;
                        case 161:
                            PuttParams _arg0125 = (PuttParams) data.readTypedObject(PuttParams.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result124 = startPutt(_arg0125);
                            reply.writeNoException();
                            reply.writeBoolean(_result124);
                            return true;
                        case TRANSACTION_getEnterPuttAppInfos /* 162 */:
                            List<OplusPuttEnterInfo> _result125 = getEnterPuttAppInfos();
                            reply.writeNoException();
                            reply.writeTypedList(_result125, 1);
                            return true;
                        case 163:
                            int _arg0126 = data.readInt();
                            String _arg157 = data.readString();
                            int _arg223 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result126 = removePuttTask(_arg0126, _arg157, _arg223);
                            reply.writeNoException();
                            reply.writeBoolean(_result126);
                            return true;
                        case 164:
                            int _arg0127 = data.readInt();
                            String _arg158 = data.readString();
                            int _arg224 = data.readInt();
                            String _arg311 = data.readString();
                            Bundle _arg44 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result127 = isSupportPuttMode(_arg0127, _arg158, _arg224, _arg311, _arg44);
                            reply.writeNoException();
                            reply.writeBoolean(_result127);
                            return true;
                        case 165:
                            IFlexibleWindowObserver _arg0128 = IFlexibleWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result128 = registerFlexibleWindowObserver(_arg0128);
                            reply.writeNoException();
                            reply.writeBoolean(_result128);
                            return true;
                        case 166:
                            IFlexibleWindowObserver _arg0129 = IFlexibleWindowObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result129 = unregisterFlexibleWindowObserver(_arg0129);
                            reply.writeNoException();
                            reply.writeBoolean(_result129);
                            return true;
                        case 167:
                            Intent _arg0130 = (Intent) data.readTypedObject(Intent.CREATOR);
                            int _arg159 = data.readInt();
                            int _arg225 = data.readInt();
                            data.enforceNoDataAvail();
                            Bundle _result130 = calculateFlexibleWindowBounds(_arg0130, _arg159, _arg225);
                            reply.writeNoException();
                            reply.writeTypedObject(_result130, 1);
                            return true;
                        case 168:
                            int _arg0131 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result131 = isInPocketStudio(_arg0131);
                            reply.writeNoException();
                            reply.writeBoolean(_result131);
                            return true;
                        case 169:
                            int _arg0132 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result132 = isInPocketStudioForStandard(_arg0132);
                            reply.writeNoException();
                            reply.writeBoolean(_result132);
                            return true;
                        case 170:
                            int _arg0133 = data.readInt();
                            data.enforceNoDataAvail();
                            Map _result133 = getPocketStudioTaskRegion(_arg0133);
                            reply.writeNoException();
                            reply.writeMap(_result133);
                            return true;
                        case 171:
                            boolean _arg0134 = data.readBoolean();
                            int _arg160 = data.readInt();
                            data.enforceNoDataAvail();
                            setMinimizedPocketStudio(_arg0134, _arg160);
                            reply.writeNoException();
                            return true;
                        case 172:
                            int _arg0135 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result134 = isMinimizedPocketStudio(_arg0135);
                            reply.writeNoException();
                            reply.writeBoolean(_result134);
                            return true;
                        case 173:
                            int _arg0136 = data.readInt();
                            Bundle _arg161 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            setFlexibleFrame(_arg0136, _arg161);
                            return true;
                        case 174:
                            boolean _arg0137 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setIsInFocusAnimating(_arg0137);
                            reply.writeNoException();
                            return true;
                        case 175:
                            int _arg0138 = data.readInt();
                            int _arg162 = data.readInt();
                            data.enforceNoDataAvail();
                            setEmbeddedContainerTask(_arg0138, _arg162);
                            reply.writeNoException();
                            return true;
                        case 176:
                            int _arg0139 = data.readInt();
                            int _arg163 = data.readInt();
                            data.enforceNoDataAvail();
                            removeEmbeddedContainerTask(_arg0139, _arg163);
                            reply.writeNoException();
                            return true;
                        case 177:
                            int _arg0140 = data.readInt();
                            data.enforceNoDataAvail();
                            exitFlexibleEmbeddedTask(_arg0140);
                            reply.writeNoException();
                            return true;
                        case 178:
                            int _arg0141 = data.readInt();
                            boolean _arg164 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setFlexibleTaskEmbedding(_arg0141, _arg164);
                            reply.writeNoException();
                            return true;
                        case 179:
                            int _arg0142 = data.readInt();
                            boolean _arg165 = data.readBoolean();
                            boolean _arg226 = data.readBoolean();
                            data.enforceNoDataAvail();
                            resetFlexibleTask(_arg0142, _arg165, _arg226);
                            reply.writeNoException();
                            return true;
                        case 180:
                            int _arg0143 = data.readInt();
                            data.enforceNoDataAvail();
                            List<ActivityManager.RecentTaskInfo> _result135 = getRecentEmbeddedTasksForContainer(_arg0143);
                            reply.writeNoException();
                            reply.writeTypedList(_result135, 1);
                            return true;
                        case 181:
                            Intent _arg0144 = (Intent) data.readTypedObject(Intent.CREATOR);
                            int _arg166 = data.readInt();
                            data.enforceNoDataAvail();
                            startActivityInTask(_arg0144, _arg166);
                            reply.writeNoException();
                            return true;
                        case 182:
                            Intent _arg0145 = (Intent) data.readTypedObject(Intent.CREATOR);
                            Bundle _arg167 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            int _result136 = startAnyActivity(_arg0145, _arg167);
                            reply.writeNoException();
                            reply.writeInt(_result136);
                            return true;
                        case 183:
                            int _arg0146 = data.readInt();
                            Rect _arg168 = (Rect) data.readTypedObject(Rect.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result137 = startFlexibleWindowForRecentTasks(_arg0146, _arg168);
                            reply.writeNoException();
                            reply.writeBoolean(_result137);
                            return true;
                        case 184:
                            int _arg0147 = data.readInt();
                            int _arg169 = data.readInt();
                            int _arg227 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result138 = isClickAtPocketStudioArea(_arg0147, _arg169, _arg227);
                            reply.writeNoException();
                            reply.writeBoolean(_result138);
                            return true;
                        case 185:
                            List<Intent> _arg0148 = data.createTypedArrayList(Intent.CREATOR);
                            int _arg170 = data.readInt();
                            int _arg228 = data.readInt();
                            boolean _arg312 = data.readBoolean();
                            data.enforceNoDataAvail();
                            List<Rect> _result139 = calculateCanvasLayoutRect(_arg0148, _arg170, _arg228, _arg312);
                            reply.writeNoException();
                            reply.writeTypedList(_result139, 1);
                            return true;
                        case 186:
                            List<Intent> _arg0149 = data.createTypedArrayList(Intent.CREATOR);
                            int _arg171 = data.readInt();
                            int _arg229 = data.readInt();
                            int _arg313 = data.readInt();
                            boolean _arg45 = data.readBoolean();
                            data.enforceNoDataAvail();
                            List<Rect> _result140 = calculateReplaceCanvasLayoutRect(_arg0149, _arg171, _arg229, _arg313, _arg45);
                            reply.writeNoException();
                            reply.writeTypedList(_result140, 1);
                            return true;
                        case 187:
                            int _arg0150 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result141 = isInAppInnerSplitScreen(_arg0150);
                            reply.writeNoException();
                            reply.writeBoolean(_result141);
                            return true;
                        case 188:
                            boolean _arg0151 = data.readBoolean();
                            int _arg172 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyEmbeddedTasksChangeFocus(_arg0151, _arg172);
                            return true;
                        case 189:
                            String _arg0152 = data.readString();
                            Bundle _arg173 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            int _arg230 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyFlexibleSplitScreenStateChanged(_arg0152, _arg173, _arg230);
                            return true;
                        case 190:
                            IEmbeddedWindowContainerCallback _arg0153 = IEmbeddedWindowContainerCallback.Stub.asInterface(data.readStrongBinder());
                            int _arg174 = data.readInt();
                            data.enforceNoDataAvail();
                            registerEmbeddedWindowContainerCallback(_arg0153, _arg174);
                            return true;
                        case 191:
                            IBinder _arg0154 = data.readStrongBinder();
                            IEmbeddedWindowContainerCallback _arg0155 = IEmbeddedWindowContainerCallback.Stub.asInterface(_arg0154);
                            int _arg175 = data.readInt();
                            data.enforceNoDataAvail();
                            unregisterEmbeddedWindowContainerCallback(_arg0155, _arg175);
                            return true;
                        case 192:
                            IBinder _arg0156 = data.readStrongBinder();
                            String _arg176 = data.readString();
                            data.enforceNoDataAvail();
                            Bundle _result142 = getActivityConfigs(_arg0156, _arg176);
                            reply.writeNoException();
                            reply.writeTypedObject(_result142, 1);
                            return true;
                        case 193:
                            WindowManager.LayoutParams _arg0157 = (WindowManager.LayoutParams) data.readTypedObject(WindowManager.LayoutParams.CREATOR);
                            int _arg177 = data.readInt();
                            Rect _arg231 = (Rect) data.readTypedObject(Rect.CREATOR);
                            Rect _arg314 = (Rect) data.readTypedObject(Rect.CREATOR);
                            data.enforceNoDataAvail();
                            adjustWindowFrame(_arg0157, _arg177, _arg231, _arg314);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg231, 1);
                            reply.writeTypedObject(_arg314, 1);
                            return true;
                        case 194:
                            IBinder _arg0158 = data.readStrongBinder();
                            boolean _arg178 = data.readBoolean();
                            data.enforceNoDataAvail();
                            updateRecordSurfaceViewState(_arg0158, _arg178);
                            reply.writeNoException();
                            return true;
                        case 195:
                            Bundle _arg0159 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            reportViewExtractResult(_arg0159);
                            return true;
                        case 196:
                            IAssistDataReceiver _arg0160 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                            Bundle _arg179 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            IBinder _arg232 = data.readStrongBinder();
                            int _arg315 = data.readInt();
                            data.enforceNoDataAvail();
                            requestViewExtractData(_arg0160, _arg179, _arg232, _arg315);
                            return true;
                        case 197:
                            WindowContainerToken _arg0161 = (WindowContainerToken) data.readTypedObject(WindowContainerToken.CREATOR);
                            int _arg180 = data.readInt();
                            boolean _arg233 = data.readBoolean();
                            data.enforceNoDataAvail();
                            updateTaskVisibility(_arg0161, _arg180, _arg233);
                            reply.writeNoException();
                            return true;
                        case 198:
                            List<Intent> _arg0162 = data.createTypedArrayList(Intent.CREATOR);
                            Bundle _arg181 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result143 = shouldSkipStartPocketStudio(_arg0162, _arg181);
                            reply.writeNoException();
                            reply.writeBoolean(_result143);
                            return true;
                        case 199:
                            Bundle _arg0163 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            int _result144 = createVirtualDisplayDevice(_arg0163);
                            reply.writeNoException();
                            reply.writeInt(_result144);
                            return true;
                        case 200:
                            int _arg0164 = data.readInt();
                            data.enforceNoDataAvail();
                            releaseVirtualDisplayDevice(_arg0164);
                            reply.writeNoException();
                            return true;
                        case 201:
                            int _arg0165 = data.readInt();
                            int _arg182 = data.readInt();
                            boolean _arg234 = data.readBoolean();
                            data.enforceNoDataAvail();
                            moveTaskToDisplay(_arg0165, _arg182, _arg234);
                            reply.writeNoException();
                            return true;
                        case 202:
                            int _arg0166 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result145 = isFlexibleTaskEnabled(_arg0166);
                            reply.writeNoException();
                            reply.writeBoolean(_result145);
                            return true;
                        case 203:
                            int _arg0167 = data.readInt();
                            boolean _arg183 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result146 = setFlexibleTaskEnabled(_arg0167, _arg183);
                            reply.writeNoException();
                            reply.writeBoolean(_result146);
                            return true;
                        case 204:
                            TransitionInfo.Change _arg0168 = (TransitionInfo.Change) data.readTypedObject(TransitionInfo.Change.CREATOR);
                            data.enforceNoDataAvail();
                            Rect _result147 = adjustEndAbsForFlexibleMinimizeIfNeed(_arg0168);
                            reply.writeNoException();
                            reply.writeTypedObject(_result147, 1);
                            return true;
                        case 205:
                            int _arg0169 = data.readInt();
                            boolean _arg184 = data.readBoolean();
                            boolean _arg235 = data.readBoolean();
                            data.enforceNoDataAvail();
                            TaskSnapshot _result148 = getEmbeddedChildrenSnapshot(_arg0169, _arg184, _arg235);
                            reply.writeNoException();
                            reply.writeTypedObject(_result148, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onTransact$0(Parcel data, Map _arg1, int i) {
            String k = data.readString();
            _arg1.put(k, data.createStringArrayList());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IOplusActivityTaskManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusActivityTaskManager.DESCRIPTOR;
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setSecureController(IActivityController controller) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(controller);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public ComponentName getTopActivityComponentName() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    ComponentName _result = (ComponentName) _reply.readTypedObject(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public ApplicationInfo getTopApplicationInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    ApplicationInfo _result = (ApplicationInfo) _reply.readTypedObject(ApplicationInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int getSplitScreenState(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public List<OplusAppInfo> getAllTopAppInfos() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    List<OplusAppInfo> _result = _reply.createTypedArrayList(OplusAppInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public List<String> getFreeformConfigList(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isFreeformEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean addFreeformConfigChangedListener(IOplusFreeformConfigChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean removeFreeformConfigChangedListener(IOplusFreeformConfigChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerAppSwitchObserver(String pkgName, IOplusAppSwitchObserver observer, OplusAppSwitchConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongInterface(observer);
                    _data.writeTypedObject(config, 0);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterAppSwitchObserver(String pkgName, OplusAppSwitchConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeTypedObject(config, 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void registerDragAndDropListener(String pkgName, IDragAndDropListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void unregisterDragAndDropListener(String pkgName, IDragAndDropListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int startZoomWindow(Intent intent, Bundle options, int userId, String callPkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeTypedObject(options, 0);
                    _data.writeInt(userId);
                    _data.writeString(callPkg);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerZoomWindowObserver(IOplusZoomWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterZoomWindowObserver(IOplusZoomWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public OplusZoomWindowInfo getCurrentZoomWindowState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    OplusZoomWindowInfo _result = (OplusZoomWindowInfo) _reply.readTypedObject(OplusZoomWindowInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void hideZoomWindow(int flag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(flag);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public List<String> getZoomAppConfigList(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isSupportZoomMode(String target, int userId, String callPkg, Bundle extension) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(target);
                    _data.writeInt(userId);
                    _data.writeString(callPkg);
                    _data.writeTypedObject(extension, 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean handleShowCompatibilityToast(String target, int userId, String callPkg, Bundle extension, int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(target);
                    _data.writeInt(userId);
                    _data.writeString(callPkg);
                    _data.writeTypedObject(extension, 0);
                    _data.writeInt(type);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void startMiniZoomFromZoom(int startWay) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(startWay);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void notifyZoomStateChange(String packageName, int action) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(action);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void onInputEvent(OplusZoomInputEventInfo inputEventInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(inputEventInfo, 0);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void onControlViewChanged(OplusZoomControlViewInfo cvInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(cvInfo, 0);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerZoomAppObserver(IOplusZoomAppObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterZoomAppObserver(IOplusZoomAppObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerSecurityPageCallback(ISecurityPageController observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterSecurityPageCallback(ISecurityPageController observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean getSecurityFlagCurrentPage() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void updateDeferStartingWindowApps(List<String> packages, boolean removeImmdiately) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStringList(packages);
                    _data.writeBoolean(removeImmdiately);
                    this.mRemote.transact(31, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void onFloatHandleViewChanged(OplusZoomFloatHandleViewInfo floatHandleInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(floatHandleInfo, 0);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isSupportZoomWindowMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public OplusZoomWindowRUSConfig getZoomWindowConfig() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    OplusZoomWindowRUSConfig _result = (OplusZoomWindowRUSConfig) _reply.readTypedObject(OplusZoomWindowRUSConfig.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setZoomWindowConfig(OplusZoomWindowRUSConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(config, 0);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean addZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean removeZoomWindowConfigChangedListener(IOplusZoomWindowConfigChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void startLockDeviceMode(String rootPkg, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(rootPkg);
                    _data.writeStringArray(packages);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void stopLockDeviceMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isLockDeviceMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isZoomSimpleModeEnable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean writeEdgeTouchPreventParam(String callPkg, String scenePkg, List<String> paramCmdList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(callPkg);
                    _data.writeString(scenePkg);
                    _data.writeStringList(paramCmdList);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setDefaultEdgeTouchPreventParam(String callPkg, List<String> paramCmdList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(callPkg);
                    _data.writeStringList(paramCmdList);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean resetDefaultEdgeTouchPreventParam(String callPkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(callPkg);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isSupportEdgeTouchPrevent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setEdgeTouchCallRules(String callPkg, Map<String, List<String>> rulesMap) throws RemoteException {
                final Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(callPkg);
                    if (rulesMap == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(rulesMap.size());
                        rulesMap.forEach(new BiConsumer() { // from class: android.app.IOplusActivityTaskManager$Stub$Proxy$$ExternalSyntheticLambda0
                            @Override // java.util.function.BiConsumer
                            public final void accept(Object obj, Object obj2) {
                                IOplusActivityTaskManager.Stub.Proxy.lambda$setEdgeTouchCallRules$0(_data, (String) obj, (List) obj2);
                            }
                        });
                    }
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ void lambda$setEdgeTouchCallRules$0(Parcel _data, String k, List v) {
                _data.writeString(k);
                _data.writeStringList(v);
            }

            @Override // android.app.IOplusActivityTaskManager
            public int splitScreenForEdgePanel(Intent intent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setConfineMode(int mode, boolean on, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeBoolean(on);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int getConfineMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setPermitList(int mode, int type, List<String> permits, boolean isMultiApp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(type);
                    _data.writeStringList(permits);
                    _data.writeBoolean(isMultiApp);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerKeyEventObserver(String observerFingerPrint, IOplusKeyEventObserver observer, int listenFlag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(observerFingerPrint);
                    _data.writeStrongInterface(observer);
                    _data.writeInt(listenFlag);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterKeyEventObserver(String observerFingerPrint) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(observerFingerPrint);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean updateAppData(String module, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerSysStateChangeObserver(String module, ISysStateChangeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterSysStateChangeObserver(String module, ISysStateChangeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setGimbalLaunchPkg(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setPackagesState(Map packageMap) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeMap(packageMap);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerLockScreenCallback(IOplusLockScreenCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterLockScreenCallback(IOplusLockScreenCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void notifySplitScreenStateChanged(String event, Bundle bundle, boolean broadcast) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(event);
                    _data.writeTypedObject(bundle, 0);
                    _data.writeBoolean(broadcast);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean setSplitScreenObserver(IOplusSplitScreenObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isInSplitScreenMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean dismissSplitScreenMode(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerSplitScreenObserver(int observerId, IOplusSplitScreenObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(observerId);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterSplitScreenObserver(int observerId, IOplusSplitScreenObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(observerId);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Bundle getSplitScreenStatus(String event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(event);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean splitScreenForTopApp(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean splitScreenForRecentTasks(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int setTaskWindowingModeSplitScreen(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean splitScreenForEdgePanelExt(Intent intent, boolean launchToPrimary, int launchArea) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeBoolean(launchToPrimary);
                    _data.writeInt(launchArea);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean hasLargeScreenFeature() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isFolderInnerScreen() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Rect getMinimizedBounds(int dockedSide) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(dockedSide);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    Rect _result = (Rect) _reply.readTypedObject(Rect.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public IOplusSplitScreenSession getSplitScreenSession() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    IOplusSplitScreenSession _result = IOplusSplitScreenSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean splitScreenForApplication(PendingIntent intent, int position) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeInt(position);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Bundle getLeftRightBoundsForIme() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean hasColorSplitFeature() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerKeyEventInterceptor(String interceptorFingerPrint, IOplusKeyEventObserver observer, Map configs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(interceptorFingerPrint);
                    _data.writeStrongInterface(observer);
                    _data.writeMap(configs);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterKeyEventInterceptor(String interceptorFingerPrint) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(interceptorFingerPrint);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public OplusGlobalDragAndDropRUSConfig getGlobalDragAndDropConfig() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    OplusGlobalDragAndDropRUSConfig _result = (OplusGlobalDragAndDropRUSConfig) _reply.readTypedObject(OplusGlobalDragAndDropRUSConfig.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setGlobalDragAndDropConfig(OplusGlobalDragAndDropRUSConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(config, 0);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerMirageWindowObserver(IOplusMirageWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterMirageWindowObserver(IOplusMirageWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerMirageDisplayObserver(IOplusMirageDisplayObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterMirageDisplayObserver(IOplusMirageDisplayObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int createMirageDisplay(Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(surface, 0);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Bundle getTaskInfo(ComponentName name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(name, 0);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void startMirageWindowModeWithName(ComponentName cpnName, int taskId, int flags, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(cpnName, 0);
                    _data.writeInt(taskId);
                    _data.writeInt(flags);
                    _data.writeTypedObject(options, 0);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int startMirageWindowMode(Intent intent, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeTypedObject(options, 0);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isMirageWindowShow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void stopMirageWindowModeOld() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void stopMirageWindowMode(Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(options, 0);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setMirageDisplaySurfaceById(int displyId, Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displyId);
                    _data.writeTypedObject(surface, 0);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setMirageDisplaySurfaceByMode(int mode, Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeTypedObject(surface, 0);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int getMirageDisplayCastMode(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void expandToFullScreen() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setMirageWindowSilent(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isSupportMirageWindowMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public OplusMirageWindowInfo getMirageWindowInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                    OplusMirageWindowInfo _result = (OplusMirageWindowInfo) _reply.readTypedObject(OplusMirageWindowInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean updateMirageWindowCastFlag(int castFlag, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(castFlag);
                    _data.writeTypedObject(options, 0);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean updatePrivacyProtectionList(List<String> name, boolean append) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStringList(name);
                    _data.writeBoolean(append);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean updatePrivacyProtectionListWithBundle(List<String> name, boolean append, boolean isDefault, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStringList(name);
                    _data.writeBoolean(append);
                    _data.writeBoolean(isDefault);
                    _data.writeTypedObject(options, 0);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public IOplusMirageWindowSession createMirageWindowSession(IOplusMirageSessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                    IOplusMirageWindowSession _result = IOplusMirageWindowSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int getGetDisplayIdForPackageName(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void feedbackUserSelection(int eventId, int selection, Bundle extension) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(eventId);
                    _data.writeInt(selection);
                    _data.writeTypedObject(extension, 0);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean updateCarModeMultiLaunchWhiteList(String list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(list);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean rebindDisplayIfNeeded(int castDisplayId, int mirageDisplayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(castDisplayId);
                    _data.writeInt(mirageDisplayId);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerConfineModeObserver(IOplusConfineModeObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterConfineModeObserver(IOplusConfineModeObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public String readNodeFile(int nodeFlag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(nodeFlag);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public String readNodeFileByDevice(int deviceId, int nodeFlag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean writeNodeFile(int nodeFlag, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(nodeFlag);
                    _data.writeString(info);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean writeNodeFileByDevice(int deviceId, int nodeFlag, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    _data.writeString(info);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isTouchNodeSupport(int deviceId, int nodeFlag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean writeNodeFileFromBt(int deviceId, int nodeFlag, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    _data.writeString(info);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void writeNodeFileOneWay(int deviceId, int nodeFlag, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    _data.writeString(info);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean notifyTouchNodeChange(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(clientFlag);
                    _data.writeLong(time);
                    _data.writeInt(deviceId);
                    _data.writeInt(nodeFlag);
                    _data.writeInt(data);
                    _data.writeString(info);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerEventCallback(IOplusTouchNodeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterEventCallback(IOplusTouchNodeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void clientTransactionComplete(IBinder token, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(seq);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void updateUntrustedTouchConfig(String configData, boolean isRus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(configData);
                    _data.writeBoolean(isRus);
                    this.mRemote.transact(121, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public String getAppThemeVersion(String pkgName, boolean change) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeBoolean(change);
                    this.mRemote.transact(122, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean lockRotationInGame(int state, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeStringArray(packages);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int startCompactWindow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int exitCompactWindow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isCurrentAppSupportCompactMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int moveCompactWindowToLeft() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int moveCompactWindowToRight() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterCompactWindowObserver(IOplusCompactWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Map getPWAppInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean onProtocolUpdated(String content) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(content);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int getFocusMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Rect getFocusBounds(boolean isPrimary) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeBoolean(isPrimary);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                    Rect _result = (Rect) _reply.readTypedObject(Rect.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public ComponentName getFocusComponent(boolean isPrimary) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeBoolean(isPrimary);
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                    ComponentName _result = (ComponentName) _reply.readTypedObject(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Point getRealSize() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                    Point _result = (Point) _reply.readTypedObject(Point.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Bundle callMethod(String method, String packageName, int param1, boolean param2, String param3, Bundle object) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(method);
                    _data.writeString(packageName);
                    _data.writeInt(param1);
                    _data.writeBoolean(param2);
                    _data.writeString(param3);
                    _data.writeTypedObject(object, 0);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Bundle invokeSync(String packageName, String method, String params, Bundle objects) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(method);
                    _data.writeString(params);
                    _data.writeTypedObject(objects, 0);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean addBracketWindowConfigChangedListener(IOplusBracketModeChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean removeBracketWindowConfigChangedListener(IOplusBracketModeChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isNeedDisableWindowLayoutInfo(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void bindQuickReplyService(IQuickReplyCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void unbindQuickReplyService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean sendMessage(PendingIntent weChatIntent, String message) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(weChatIntent, 0);
                    _data.writeString(message);
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean shouldInterceptBackKeyForMultiSearch(IBinder activityToken, boolean down) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeBoolean(down);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public IOplusMultiSearchManagerSession getMultiSearchSession() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                    IOplusMultiSearchManagerSession _result = IOplusMultiSearchManagerSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public SurfaceControl takeScreenshot(WindowContainerToken windowContainerToken, SurfaceControl outSurfaceControl) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(windowContainerToken, 0);
                    _data.writeTypedObject(outSurfaceControl, 0);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                    SurfaceControl _result = (SurfaceControl) _reply.readTypedObject(SurfaceControl.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public IOplusZoomTaskController getZoomTaskController() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                    IOplusZoomTaskController _result = IOplusZoomTaskController.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int getRenderThreadTid(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean setInputConsumerEnabled(boolean enabled, IRecentsAnimationController controller) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    _data.writeStrongInterface(controller);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void adjustWindowFrameForZoom(WindowManager.LayoutParams attrs, int windowingMode, Rect outDisplayFrame, Rect outParentFrame) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(attrs, 0);
                    _data.writeInt(windowingMode);
                    _data.writeTypedObject(outDisplayFrame, 0);
                    _data.writeTypedObject(outParentFrame, 0);
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outDisplayFrame.readFromParcel(_reply);
                    }
                    if (_reply.readInt() != 0) {
                        outParentFrame.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isZoomSupportMultiWindow(String packageName, ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void notifyUiSwitched(String uiInfo, int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(uiInfo);
                    _data.writeInt(status);
                    this.mRemote.transact(153, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void pauseInRecentsAnim(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(154, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public List<String> getPauseInRecentsAnimPkgList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(155, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerPuttObserver(IPuttObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(156, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterPuttObserver(IPuttObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(157, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerPuttEventObserver(IPuttEventObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(158, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterPuttEventObserver(IPuttEventObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(159, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean stopPutt(String puttHash, int action, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(puttHash);
                    _data.writeInt(action);
                    _data.writeTypedObject(options, 0);
                    this.mRemote.transact(160, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean startPutt(PuttParams params) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(params, 0);
                    this.mRemote.transact(161, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public List<OplusPuttEnterInfo> getEnterPuttAppInfos() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getEnterPuttAppInfos, _data, _reply, 0);
                    _reply.readException();
                    List<OplusPuttEnterInfo> _result = _reply.createTypedArrayList(OplusPuttEnterInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean removePuttTask(int action, String pkg, int puttTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(action);
                    _data.writeString(pkg);
                    _data.writeInt(puttTaskId);
                    this.mRemote.transact(163, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isSupportPuttMode(int type, String target, int userId, String callPkg, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(target);
                    _data.writeInt(userId);
                    _data.writeString(callPkg);
                    _data.writeTypedObject(options, 0);
                    this.mRemote.transact(164, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean registerFlexibleWindowObserver(IFlexibleWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(165, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean unregisterFlexibleWindowObserver(IFlexibleWindowObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(166, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Bundle calculateFlexibleWindowBounds(Intent intent, int appOrientation, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeInt(appOrientation);
                    _data.writeInt(displayId);
                    this.mRemote.transact(167, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isInPocketStudio(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(168, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isInPocketStudioForStandard(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(169, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Map getPocketStudioTaskRegion(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(170, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setMinimizedPocketStudio(boolean minimized, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeBoolean(minimized);
                    _data.writeInt(displayId);
                    this.mRemote.transact(171, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isMinimizedPocketStudio(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(172, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setFlexibleFrame(int taskId, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(173, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setIsInFocusAnimating(boolean isInFocusAnimating) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeBoolean(isInFocusAnimating);
                    this.mRemote.transact(174, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setEmbeddedContainerTask(int embeddedTaskId, int containerTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(embeddedTaskId);
                    _data.writeInt(containerTaskId);
                    this.mRemote.transact(175, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void removeEmbeddedContainerTask(int embeddedTaskId, int containerTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(embeddedTaskId);
                    _data.writeInt(containerTaskId);
                    this.mRemote.transact(176, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void exitFlexibleEmbeddedTask(int embeddedTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(embeddedTaskId);
                    this.mRemote.transact(177, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void setFlexibleTaskEmbedding(int taskId, boolean state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeBoolean(state);
                    this.mRemote.transact(178, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void resetFlexibleTask(int embeddedTaskId, boolean needResize, boolean doAnimation) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(embeddedTaskId);
                    _data.writeBoolean(needResize);
                    _data.writeBoolean(doAnimation);
                    this.mRemote.transact(179, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public List<ActivityManager.RecentTaskInfo> getRecentEmbeddedTasksForContainer(int containerTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(containerTaskId);
                    this.mRemote.transact(180, _data, _reply, 0);
                    _reply.readException();
                    List<ActivityManager.RecentTaskInfo> _result = _reply.createTypedArrayList(ActivityManager.RecentTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void startActivityInTask(Intent intent, int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeInt(taskId);
                    this.mRemote.transact(181, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int startAnyActivity(Intent intent, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(182, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean startFlexibleWindowForRecentTasks(int taskId, Rect rect) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeTypedObject(rect, 0);
                    this.mRemote.transact(183, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isClickAtPocketStudioArea(int displayId, int rowX, int rowY) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(rowX);
                    _data.writeInt(rowY);
                    this.mRemote.transact(184, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public List<Rect> calculateCanvasLayoutRect(List<Intent> intentList, int focusIndex, int layoutOrientation, boolean panoramaMode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedList(intentList, 0);
                    _data.writeInt(focusIndex);
                    _data.writeInt(layoutOrientation);
                    _data.writeBoolean(panoramaMode);
                    this.mRemote.transact(185, _data, _reply, 0);
                    _reply.readException();
                    List<Rect> _result = _reply.createTypedArrayList(Rect.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public List<Rect> calculateReplaceCanvasLayoutRect(List<Intent> intentList, int focusIndex, int replace, int displayId, boolean panoramaMode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedList(intentList, 0);
                    _data.writeInt(focusIndex);
                    _data.writeInt(replace);
                    _data.writeInt(displayId);
                    _data.writeBoolean(panoramaMode);
                    this.mRemote.transact(186, _data, _reply, 0);
                    _reply.readException();
                    List<Rect> _result = _reply.createTypedArrayList(Rect.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isInAppInnerSplitScreen(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(187, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void notifyEmbeddedTasksChangeFocus(boolean isStateSteady, int containerTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeBoolean(isStateSteady);
                    _data.writeInt(containerTaskId);
                    this.mRemote.transact(188, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void notifyFlexibleSplitScreenStateChanged(String event, Bundle bundle, int containerTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeString(event);
                    _data.writeTypedObject(bundle, 0);
                    _data.writeInt(containerTaskId);
                    this.mRemote.transact(189, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void registerEmbeddedWindowContainerCallback(IEmbeddedWindowContainerCallback callback, int containerTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    _data.writeInt(containerTaskId);
                    this.mRemote.transact(190, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void unregisterEmbeddedWindowContainerCallback(IEmbeddedWindowContainerCallback callback, int containerTaskId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    _data.writeInt(containerTaskId);
                    this.mRemote.transact(191, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Bundle getActivityConfigs(IBinder token, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(packageName);
                    this.mRemote.transact(192, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void adjustWindowFrame(WindowManager.LayoutParams attrs, int windowingMode, Rect outDisplayFrame, Rect outParentFrame) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(attrs, 0);
                    _data.writeInt(windowingMode);
                    _data.writeTypedObject(outDisplayFrame, 0);
                    _data.writeTypedObject(outParentFrame, 0);
                    this.mRemote.transact(193, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outDisplayFrame.readFromParcel(_reply);
                    }
                    if (_reply.readInt() != 0) {
                        outParentFrame.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void updateRecordSurfaceViewState(IBinder token, boolean hasSurfaceView) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeBoolean(hasSurfaceView);
                    this.mRemote.transact(194, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void reportViewExtractResult(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(195, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void requestViewExtractData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongInterface(receiver);
                    _data.writeTypedObject(receiverExtras, 0);
                    _data.writeStrongBinder(activityToken);
                    _data.writeInt(flags);
                    this.mRemote.transact(196, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void updateTaskVisibility(WindowContainerToken windowContainerToken, int containerTaskId, boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(windowContainerToken, 0);
                    _data.writeInt(containerTaskId);
                    _data.writeBoolean(visible);
                    this.mRemote.transact(197, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean shouldSkipStartPocketStudio(List<Intent> intentList, Bundle canvasExtra) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedList(intentList, 0);
                    _data.writeTypedObject(canvasExtra, 0);
                    this.mRemote.transact(198, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public int createVirtualDisplayDevice(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(199, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void releaseVirtualDisplayDevice(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(200, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public void moveTaskToDisplay(int taskId, int displayId, boolean top) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(displayId);
                    _data.writeBoolean(top);
                    this.mRemote.transact(201, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean isFlexibleTaskEnabled(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(202, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public boolean setFlexibleTaskEnabled(int displayId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(203, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public Rect adjustEndAbsForFlexibleMinimizeIfNeed(TransitionInfo.Change change) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeTypedObject(change, 0);
                    this.mRemote.transact(204, _data, _reply, 0);
                    _reply.readException();
                    Rect _result = (Rect) _reply.readTypedObject(Rect.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusActivityTaskManager
            public TaskSnapshot getEmbeddedChildrenSnapshot(int taskId, boolean isLowResolution, boolean takeSnapshotIfNeeded) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeBoolean(isLowResolution);
                    _data.writeBoolean(takeSnapshotIfNeeded);
                    this.mRemote.transact(205, _data, _reply, 0);
                    _reply.readException();
                    TaskSnapshot _result = (TaskSnapshot) _reply.readTypedObject(TaskSnapshot.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 204;
        }
    }
}
