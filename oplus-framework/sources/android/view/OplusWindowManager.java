package android.view;

import android.common.OplusFeatureCache;
import android.content.ComponentName;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Singleton;
import android.view.DisplayCutout;
import android.view.IOplusWindowManager;
import android.view.IWindowManager;
import com.oplus.animation.LaunchViewInfo;
import com.oplus.app.OplusScreenShotOptions;
import com.oplus.app.OplusScreenShotResult;
import com.oplus.app.OplusWindowInfo;
import com.oplus.darkmode.IOplusDarkModeListener;
import com.oplus.direct.OplusDirectFindCmd;
import com.oplus.foldswitch.OplusFoldSwitchStateObserver;
import com.oplus.screenshot.IOplusScreenShotEuclidManager;
import com.oplus.screenshot.IOplusScrollCaptureResponseListener;
import com.oplus.wallpaper.IOplusWallpaperObserver;
import java.util.List;

/* loaded from: classes.dex */
public class OplusWindowManager {
    private static final Singleton<IOplusWindowManager> IOplusActivityManagerSingleton = new Singleton<IOplusWindowManager>() { // from class: android.view.OplusWindowManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusWindowManager m298create() {
            try {
                IBinder b = ServiceManager.getService("window");
                IWindowManager wm = IWindowManager.Stub.asInterface(b);
                IBinder extbinder = wm.asBinder().getExtension();
                Log.d("OplusWindowManager", "get WMS extension: " + b);
                return IOplusWindowManager.Stub.asInterface(extbinder);
            } catch (Exception e) {
                Log.e("OplusWindowManager", "create OplusWindowManagerServiceEnhance singleton failed: " + e.getMessage());
                return null;
            }
        }
    };
    private static final String OFFERTX = "offertX";
    private static final String OFFERTY = "offertY";
    private static final String SCALE = "scale";
    public static final int SECOND_DEFAULT_DISPLAY = 1;
    private static final String TAG = "OplusWindowManager";
    private final IOplusLongshotWindowManager mOplusLongshot = ((IOplusScreenShotEuclidManager) OplusFeatureCache.getOrCreate(IOplusScreenShotEuclidManager.DEFAULT, new Object[0])).getIOplusLongshotWindowManager();

    private static IOplusWindowManager getService() {
        return (IOplusWindowManager) IOplusActivityManagerSingleton.get();
    }

    public static OplusWindowManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final OplusWindowManager INSTANCE = new OplusWindowManager();

        private LazyHolder() {
        }
    }

    public boolean isLockWndShow() throws RemoteException {
        if (getService() != null) {
            return getService().isLockWndShow();
        }
        Log.e("OplusWindowManager", "isLockWndShow failed because service has not been created");
        return false;
    }

    public void keyguardSetApkLockScreenShowing(boolean showing) throws RemoteException {
        if (getService() != null) {
            getService().keyguardSetApkLockScreenShowing(showing);
        } else {
            Log.e("OplusWindowManager", "keyguardSetApkLockScreenShowing failed because service has not been created");
        }
    }

    public IBinder getApkUnlockWindow() throws RemoteException {
        if (getService() != null) {
            return getService().getApkUnlockWindow();
        }
        Log.e("OplusWindowManager", "getApkUnlockWindow failed because service has not been created");
        return null;
    }

    public void keyguardShowSecureApkLock(boolean show) throws RemoteException {
        if (getService() != null) {
            getService().keyguardShowSecureApkLock(show);
        } else {
            Log.e("OplusWindowManager", "keyguardShowSecureApkLock failed because service has not been created");
        }
    }

    public boolean isLockOnShow() throws RemoteException {
        if (getService() != null) {
            return getService().isLockOnShow();
        }
        Log.e("OplusWindowManager", "isLockOnShow failed because service has not been created");
        return false;
    }

    public boolean isSIMUnlockRunning() throws RemoteException {
        if (getService() != null) {
            return getService().isSIMUnlockRunning();
        }
        Log.e("OplusWindowManager", "isSIMUnlockRunning failed because service has not been created");
        return false;
    }

    public boolean isInputShow() throws RemoteException {
        if (getService() != null) {
            return getService().isInputShow();
        }
        Log.e("OplusWindowManager", "isInputShow failed because service has not been created");
        return false;
    }

    public boolean isFullScreen() throws RemoteException {
        if (getService() != null) {
            return getService().isFullScreen();
        }
        Log.e("OplusWindowManager", "isFullScreen failed because service has not been created");
        return false;
    }

    public boolean isStatusBarVisible() throws RemoteException {
        if (getService() != null) {
            return getService().isStatusBarVisible();
        }
        Log.e("OplusWindowManager", "isStatusBarVisible failed because service has not been created");
        return false;
    }

    public boolean isRotatingLw() throws RemoteException {
        if (getService() != null) {
            return getService().isRotatingLw();
        }
        Log.e("OplusWindowManager", "isRotatingLw failed because service has not been created");
        return false;
    }

    public void setMagnification(Bundle bundle) throws RemoteException {
    }

    public void setMagnificationSpecEx(MagnificationSpec spec) throws RemoteException {
        if (getService() != null) {
            getService().setMagnificationSpecEx(spec);
        } else {
            Log.e("OplusWindowManager", "setMagnificationSpecEx failed because service has not been created");
        }
    }

    public void requestDismissKeyguard() throws RemoteException {
        if (getService() != null) {
            getService().requestDismissKeyguard();
        } else {
            Log.e("OplusWindowManager", "requestDismissKeyguard failed because service has not been created");
        }
    }

    public void requestKeyguard(String command) throws RemoteException {
        if (getService() != null) {
            getService().requestKeyguard(command);
        } else {
            Log.e("OplusWindowManager", "requestKeyguard failed because service has not been created");
        }
    }

    public boolean isWindowShownForUid(int uid) throws RemoteException {
        if (getService() != null) {
            return getService().isWindowShownForUid(uid);
        }
        Log.e("OplusWindowManager", "isWindowShownForUid failed because service has not been created");
        return false;
    }

    public void removeWindowShownOnKeyguard() throws RemoteException {
        if (getService() != null) {
            getService().removeWindowShownOnKeyguard();
        } else {
            Log.e("OplusWindowManager", "removeWindowShownOnKeyguard failed because service has not been created");
        }
    }

    public String getCurrentFocus() throws RemoteException {
        if (getService() != null) {
            return getService().getCurrentFocus();
        }
        Log.e("OplusWindowManager", "getCurrentFocus failed because service has not been created");
        return "";
    }

    public DisplayCutout getRawDisplayCutout() throws RemoteException {
        if (getService() != null) {
            DisplayCutout.ParcelableWrapper rawCutout = getService().getRawDisplayCutout();
            if (rawCutout != null) {
                return rawCutout.get();
            }
            return null;
        }
        Log.e("OplusWindowManager", "getRawDisplayCutout failed because service has not been created");
        return null;
    }

    public Rect getFloatWindowRect(int displayId) throws RemoteException {
        if (getService() != null) {
            return getService().getFloatWindowRect(displayId);
        }
        Log.e("OplusWindowManager", "getFloatWindowRect failed because service has not been created");
        return null;
    }

    public void startOplusDragWindow(String packageName, int resId, int mode, Bundle options) throws RemoteException {
        if (getService() != null) {
            getService().startOplusDragWindow(packageName, resId, mode, options);
        } else {
            Log.e("OplusWindowManager", "startOplusDragWindow failed because service has not been created");
        }
    }

    public void registerOplusWindowStateObserver(IOplusWindowStateObserver observer) throws RemoteException {
        if (getService() != null) {
            getService().registerOplusWindowStateObserver(observer);
        } else {
            Log.e("OplusWindowManager", "registerOplusWindowStateObserver failed because service has not been created");
        }
    }

    public void unregisterOplusWindowStateObserver(IOplusWindowStateObserver observer) throws RemoteException {
        if (getService() != null) {
            getService().unregisterOplusWindowStateObserver(observer);
        } else {
            Log.e("OplusWindowManager", "unregisterOplusWindowStateObserver failed because service has not been created");
        }
    }

    public boolean isInFreeformMode() throws RemoteException {
        if (getService() != null) {
            return getService().isInFreeformMode();
        }
        Log.e("OplusWindowManager", "isInFreeformMode failed because service has not been created");
        return false;
    }

    public void getFreeformStackBounds(Rect outBounds) throws RemoteException {
        if (getService() != null) {
            getService().getFreeformStackBounds(outBounds);
        } else {
            Log.e("OplusWindowManager", "getFreeformStackBounds failed because service has not been created");
        }
    }

    public boolean isActivityNeedPalette(String pkg, String activityName) throws RemoteException {
        if (getService() != null) {
            return getService().isActivityNeedPalette(pkg, activityName);
        }
        Log.e("OplusWindowManager", "isActivityNeedPalette failed because service has not been created");
        return false;
    }

    public int getNavBarOplusFromAdaptation(String pkg, String activityName) throws RemoteException {
        if (getService() != null) {
            return getService().getNavBarOplusFromAdaptation(pkg, activityName);
        }
        Log.e("OplusWindowManager", "getNavBarOplusFromAdaptation failed because service has not been created");
        return 0;
    }

    public int getStatusBarOplusFromAdaptation(String pkg, String activityName) throws RemoteException {
        if (getService() != null) {
            return getService().getStatusBarOplusFromAdaptation(pkg, activityName);
        }
        Log.e("OplusWindowManager", "getStatusBarOplusFromAdaptation failed because service has not been created");
        return 0;
    }

    public int getImeBgOplusFromAdaptation(String pkg) throws RemoteException {
        if (getService() != null) {
            return getService().getImeBgOplusFromAdaptation(pkg);
        }
        Log.e("OplusWindowManager", "getImeBgOplusFromAdaptation failed because service has not been created");
        return 0;
    }

    public int getTypedWindowLayer(int type) throws RemoteException {
        if (getService() != null) {
            return getService().getTypedWindowLayer(type);
        }
        Log.e("OplusWindowManager", "getTypedWindowLayer failed because service has not been created");
        return 0;
    }

    public int getFocusedWindowIgnoreHomeMenuKey() throws RemoteException {
        if (getService() != null) {
            return getService().getFocusedWindowIgnoreHomeMenuKey();
        }
        Log.e("OplusWindowManager", "getFocusedWindowIgnoreHomeMenuKey failed because service has not been created");
        return 0;
    }

    public void registerOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener listener) throws RemoteException {
        if (getService() != null) {
            getService().registerOnUiModeConfigurationChangeFinishListener(listener);
        } else {
            Log.e("OplusWindowManager", "registerOnUiModeConfigurationChangeFinishListener failed because service has not been created");
        }
    }

    public void unregisterOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener listener) throws RemoteException {
        if (getService() != null) {
            getService().unregisterOnUiModeConfigurationChangeFinishListener(listener);
        } else {
            Log.e("OplusWindowManager", "unregisterOnUiModeConfigurationChangeFinishListener failed because service has not been created");
        }
    }

    public void setBootAnimationRotationLock(boolean lockRotation) throws RemoteException {
        if (getService() != null) {
            getService().setBootAnimationRotationLock(lockRotation);
        } else {
            Log.e("OplusWindowManager", "setBootAnimationRotationLock failed because service has not been created");
        }
    }

    public boolean updateInvalidRegion(String regionKey, List<RectF> region, boolean disposable, boolean isDelete, Bundle extras) throws RemoteException {
        if (getService() != null) {
            return getService().updateInvalidRegion(regionKey, region, disposable, isDelete, extras);
        }
        Log.e("OplusWindowManager", "updateInvalidRegion failed because service has not been created");
        return false;
    }

    public boolean setJoyStickConfig(int configType, String config) throws RemoteException {
        if (getService() != null) {
            return getService().setJoyStickConfig(configType, config);
        }
        Log.e("OplusWindowManager", "setJoyStickConfig failed because service has not been created");
        return false;
    }

    public boolean setJoyStickStatus(int configStatus) throws RemoteException {
        if (getService() != null) {
            return getService().setJoyStickStatus(configStatus);
        }
        Log.e("OplusWindowManager", "setJoyStickStatus failed because service has not been created");
        return false;
    }

    public boolean setJoyStickSwitch(int switchStatus) throws RemoteException {
        if (getService() != null) {
            return getService().setJoyStickSwitch(switchStatus);
        }
        Log.e("OplusWindowManager", "setJoyStickSwitch failed because service has not been created");
        return false;
    }

    public void getWindowVisibleDisplayFrame(IWindowSession session, IWindow client, Rect outDisplayFrame) throws RemoteException {
        if (getService() != null) {
            getService().getWindowVisibleDisplayFrame(session, client, outDisplayFrame);
        } else {
            Log.e("OplusWindowManager", "setJoyStickSwitch failed because service has not been created");
        }
    }

    public void showTransientNavbar(int showFlag) throws RemoteException {
        if (getService() != null) {
            getService().showTransientNavbar(showFlag);
        } else {
            Log.e("OplusWindowManager", "showTransientNavbar failed because service has not been created");
        }
    }

    public void getFocusedWindowFrame(Rect frame) throws RemoteException {
        if (getService() != null) {
            getService().getFocusedWindowFrame(frame);
        } else {
            Log.e("OplusWindowManager", "getFocusedWindowFrame failed because service has not been created");
        }
    }

    public int getLongshotSurfaceLayer() throws RemoteException {
        if (getService() != null) {
            return getService().getLongshotSurfaceLayer();
        }
        Log.e("OplusWindowManager", "getLongshotSurfaceLayer failed because service has not been created");
        return 0;
    }

    public int getLongshotSurfaceLayerByType(int type) throws RemoteException {
        if (getService() != null) {
            return getService().getLongshotSurfaceLayerByType(type);
        }
        Log.e("OplusWindowManager", "getLongshotSurfaceLayerByType failed because service has not been created");
        return 0;
    }

    public void longshotNotifyConnected(boolean isConnected) throws RemoteException {
        if (getService() != null) {
            getService().longshotNotifyConnected(isConnected);
        } else {
            Log.e("OplusWindowManager", "longshotNotifyConnected failed because service has not been created");
        }
    }

    public boolean isNavigationBarVisible() throws RemoteException {
        if (getService() != null) {
            return getService().isNavigationBarVisible();
        }
        Log.e("OplusWindowManager", "isNavigationBarVisible failed because service has not been created");
        return false;
    }

    public boolean isShortcutsPanelShow() throws RemoteException {
        if (getService() != null) {
            return getService().isShortcutsPanelShow();
        }
        Log.e("OplusWindowManager", "isShortcutsPanelShow failed because service has not been created");
        return false;
    }

    public void longshotInjectInput(InputEvent event, int mode) throws RemoteException {
        if (getService() != null) {
            getService().longshotInjectInput(event, mode);
        } else {
            Log.e("OplusWindowManager", "isShortcutsPanelShow failed because service has not been created");
        }
    }

    public boolean isKeyguardShowingAndNotOccluded() throws RemoteException {
        if (getService() != null) {
            return getService().isKeyguardShowingAndNotOccluded();
        }
        Log.e("OplusWindowManager", "isShortcutsPanelShow failed because service has not been created");
        return false;
    }

    public void longshotInjectInputBegin() throws RemoteException {
        if (getService() != null) {
            getService().longshotInjectInputBegin();
        } else {
            Log.e("OplusWindowManager", "isShortcutsPanelShow failed because service has not been created");
        }
    }

    public void longshotInjectInputEnd() throws RemoteException {
        if (getService() != null) {
            getService().longshotInjectInputEnd();
        } else {
            Log.e("OplusWindowManager", "isShortcutsPanelShow failed because service has not been created");
        }
    }

    public IBinder getLongshotWindowByType(int type) throws RemoteException {
        if (getService() != null) {
            return getService().getLongshotWindowByType(type);
        }
        Log.e("OplusWindowManager", "getLongshotWindowByType failed because service has not been created");
        return null;
    }

    public SurfaceControl getLongshotWindowByTypeForR(int type) throws RemoteException {
        if (getService() != null) {
            return getService().getLongshotWindowByTypeForR(type);
        }
        Log.e("OplusWindowManager", "getLongshotWindowByTypeForR failed because service has not been created");
        return null;
    }

    public boolean isVolumeShow() throws RemoteException {
        if (getService() != null) {
            return getService().isVolumeShow();
        }
        Log.e("OplusWindowManager", "isVolumeShow failed because service has not been created");
        return false;
    }

    public boolean isFloatAssistExpand() throws RemoteException {
        if (getService() != null) {
            return getService().isFloatAssistExpand();
        }
        Log.e("OplusWindowManager", "isFloatAssistExpand failed because service has not been created");
        return false;
    }

    public boolean isEdgePanelExpand() throws RemoteException {
        if (getService() != null) {
            return getService().isEdgePanelExpand();
        }
        Log.e("OplusWindowManager", "isEdgePanelExpand failed because service has not been created");
        return false;
    }

    public void requestScrollCapture(int displayId, IBinder behindClient, int taskId, IOplusScrollCaptureResponseListener listener, Bundle extras) throws RemoteException {
        if (getService() != null) {
            getService().requestScrollCapture(displayId, behindClient, taskId, listener, extras);
        } else {
            Log.e("OplusWindowManager", "requestScrollCapture failed because service has not been created");
        }
    }

    public void directFindCmd(OplusDirectFindCmd findCmd) throws RemoteException {
        if (getService() != null) {
            getService().directFindCmd(findCmd);
        } else {
            Log.e("OplusWindowManager", "isEdgePanelExpand failed because service has not been created");
        }
    }

    public List<OplusWindowInfo> getAllVisibleWindowInfo() throws RemoteException {
        if (getService() != null) {
            return getService().getAllVisibleWindowInfo();
        }
        Log.e("OplusWindowManager", "isEdgePanelExpand failed because service has not been created");
        return null;
    }

    public List<OplusWindowInfo> getVisibleWindowInfo(int displayId) throws RemoteException {
        if (getService() != null) {
            return getService().getVisibleWindowInfo(displayId);
        }
        Log.e("OplusWindowManager", "getVisibleWindowInfo failed because service has not been created");
        return null;
    }

    public List<Rect> getSplitAreaRegion() throws RemoteException {
        if (getService() != null) {
            return getService().getSplitAreaRegion();
        }
        Log.e("TAG", "isEdgePanelExpand failed because service has not been created");
        return null;
    }

    public void startGestureAnmation(IOplusGestureAnimationRunner guestureAnimationRunner, Bundle bOptions) throws RemoteException {
        if (getService() != null) {
            getService().startGestureAnmation(guestureAnimationRunner, bOptions);
        } else {
            Log.e("OplusWindowManager", "isEdgePanelExpand failed because service has not been created");
        }
    }

    public boolean setInsetAnimationTid(int pid, int tid, boolean enable) throws RemoteException {
        if (getService() != null) {
            return getService().setInsetAnimationTid(pid, tid, enable);
        }
        Log.e("OplusWindowManager", "isEdgePanelExpand failed because service has not been created");
        return false;
    }

    public void registerRemoteAnimationsForWindow(IWindow window, RemoteAnimationDefinition definition) throws RemoteException {
        if (getService() != null) {
            getService().registerRemoteAnimationsForWindow(window, definition);
        } else {
            Log.e("OplusWindowManager", "registerRemoteAnimationsForWindow failed because service has not been created");
        }
    }

    public void unregisterRemoteAnimationsForWindow(IWindow window) throws RemoteException {
        if (getService() != null) {
            getService().unregisterRemoteAnimationsForWindow(window);
        } else {
            Log.e("OplusWindowManager", "unregisterRemoteAnimationsForWindow failed because service has not been created");
        }
    }

    public void setOplusLaunchViewInfoForWindow(IWindow window, LaunchViewInfo viewInfo) throws RemoteException {
        if (getService() != null) {
            getService().setOplusLaunchViewInfoForWindow(window, viewInfo);
        } else {
            Log.e("OplusWindowManager", "setOplusLaunchViewInfoForWindow failed because service has not been created");
        }
    }

    public void clearOplusLaunchViewInfoForWindow(IWindow window) throws RemoteException {
        if (getService() != null) {
            getService().clearOplusLaunchViewInfoForWindow(window);
        } else {
            Log.e("OplusWindowManager", "clearOplusLaunchViewInfoForWindow failed because service has not been created");
        }
    }

    public void addBracketMirrorRootLeash(IBinder mirrorToken, SurfaceControl mirrorRoot, SurfaceControl hostRoot) {
        if (getService() != null) {
            try {
                getService().addBracketMirrorRootLeash(mirrorToken, mirrorRoot, hostRoot);
                return;
            } catch (RemoteException e) {
                Log.e("OplusWindowManager", "addBracketMirrorRootLeash, remoteException:", new Throwable());
                return;
            }
        }
        Log.e("OplusWindowManager", "addBracketMirrorRootLeash failed because service has not been created");
    }

    public void removeBracketMirrorRootLeash(IBinder mirrorToken) {
        if (getService() != null) {
            try {
                getService().removeBracketMirrorRootLeash(mirrorToken);
                return;
            } catch (RemoteException e) {
                Log.e("OplusWindowManager", "removeBracketMirrorRootLeash, remoteException:", new Throwable());
                return;
            }
        }
        Log.e("OplusWindowManager", "removeBracketMirrorRootLeash failed because service has not been created");
    }

    public OplusScreenShotResult getScreenshot(OplusScreenShotOptions options) throws RemoteException {
        if (getService() != null) {
            return getService().getScreenshot(options);
        }
        Log.e("OplusWindowManager", "getScreenshot failed because service has not been created");
        return null;
    }

    public void registerWallpaperObserver(IOplusWallpaperObserver observer, int displayId) throws RemoteException {
        if (getService() != null) {
            getService().registerWallpaperObserver(observer, displayId);
        } else {
            Log.e("OplusWindowManager", "registerWallpaperObserver failed because service has not been created");
        }
    }

    public void unregisterWallpaperObserver(IOplusWallpaperObserver observer, int displayId) throws RemoteException {
        if (getService() != null) {
            getService().unregisterWallpaperObserver(observer, displayId);
        } else {
            Log.e("OplusWindowManager", "unregisterWallpaperObserver failed because service has not been created");
        }
    }

    public void transferTouchFocus(IBinder fromChannelToken, IBinder toChannelToken) throws RemoteException {
        if (getService() != null) {
            getService().transferTouchFocus(fromChannelToken, toChannelToken);
        } else {
            Log.e("OplusWindowManager", "transferTouchFocus failed because service has not been created");
        }
    }

    public boolean transferTouch(IBinder destChannelToken, int displayId) throws RemoteException {
        if (getService() != null) {
            return getService().transferTouch(destChannelToken, displayId);
        }
        Log.e("OplusWindowManager", "transferTouch failed because service has not been created");
        return false;
    }

    public int getFocusedWindowBackGestureRestriction() throws RemoteException {
        if (getService() != null) {
            return getService().getFocusedWindowBackGestureRestriction();
        }
        Log.e("OplusWindowManager", "getFocusedWindowBackGestureRestriction failed because service has not been created");
        return 0;
    }

    public boolean isRotationLocked(int displayId) throws RemoteException {
        if (getService() != null) {
            return getService().isRotationLocked(displayId);
        }
        Log.e("OplusWindowManager", "isRotationLocked failed because service has not been created");
        return false;
    }

    public void setRotationLock(boolean enabled, int displayId) throws RemoteException {
        if (getService() != null) {
            getService().setRotationLock(enabled, displayId);
        } else {
            Log.e("OplusWindowManager", "setRotationLock failed because service has not been created");
        }
    }

    public List<Bundle> requestVisibleWindows() throws RemoteException {
        if (getService() != null) {
            return getService().requestVisibleWindows();
        }
        Log.e("OplusWindowManager", "requestVisibleWindows failed because service has not been created");
        return null;
    }

    public List<ComponentName> notifyScreenshotListeners(int displayId) throws RemoteException {
        if (getService() != null) {
            return getService().notifyScreenshotListeners(displayId);
        }
        Log.e("OplusWindowManager", "notifyScreenshotListeners failed because service has not been created");
        return null;
    }

    public int getDisplayIdForPackageName(String packageName) throws RemoteException {
        if (getService() != null) {
            return getService().getDisplayIdForPackageName(packageName);
        }
        Log.e("OplusWindowManager", "getDisplayIdForPackageName failed because service has not been created");
        return 0;
    }

    public void registerFoldSwitchStateObserver(OplusFoldSwitchStateObserver observer) throws RemoteException {
        if (getService() != null) {
            getService().registerOplusFoldSwitchStateObserver(observer);
        } else {
            Log.e("OplusWindowManager", "registerOplusFoldSwitchStateObserver failed because service has not been created");
        }
    }

    public void unregisterFoldSwitchStateObserver(OplusFoldSwitchStateObserver observer) throws RemoteException {
        if (getService() != null) {
            getService().unregisterOplusFoldSwitchStateObserver(observer);
        } else {
            Log.e("OplusWindowManager", "registerOplusFoldSwitchStateObserver failed because service has not been created");
        }
    }

    public boolean setPreferredDisplayMode(int modeId) throws RemoteException {
        if (getService() != null) {
            return getService().setPreferredDisplayMode(modeId);
        }
        Log.e("OplusWindowManager", "setPreferredDisplayMode failed because service has not been created");
        return false;
    }
}
