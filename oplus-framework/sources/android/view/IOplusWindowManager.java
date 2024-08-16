package android.view;

import android.content.ComponentName;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.DisplayCutout;
import android.view.IOplusGestureAnimationRunner;
import android.view.IOplusWindowStateObserver;
import android.view.IWindow;
import android.view.IWindowSession;
import com.oplus.animation.LaunchViewInfo;
import com.oplus.app.OplusScreenShotOptions;
import com.oplus.app.OplusScreenShotResult;
import com.oplus.app.OplusWindowInfo;
import com.oplus.darkmode.IOplusDarkModeListener;
import com.oplus.direct.OplusDirectFindCmd;
import com.oplus.foldswitch.IOplusFoldSwitchStateObserver;
import com.oplus.screenshot.IOplusScrollCaptureResponseListener;
import com.oplus.wallpaper.IOplusWallpaperObserver;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusWindowManager extends IInterface {
    public static final String DESCRIPTOR = "android.view.IOplusWindowManager";

    void addBracketMirrorRootLeash(IBinder iBinder, SurfaceControl surfaceControl, SurfaceControl surfaceControl2) throws RemoteException;

    void clearOplusLaunchViewInfoForWindow(IWindow iWindow) throws RemoteException;

    void directFindCmd(OplusDirectFindCmd oplusDirectFindCmd) throws RemoteException;

    List<OplusWindowInfo> getAllVisibleWindowInfo() throws RemoteException;

    IBinder getApkUnlockWindow() throws RemoteException;

    String getCurrentFocus() throws RemoteException;

    int getDisplayIdForPackageName(String str) throws RemoteException;

    Rect getFloatWindowRect(int i) throws RemoteException;

    int getFocusedWindowBackGestureRestriction() throws RemoteException;

    void getFocusedWindowFrame(Rect rect) throws RemoteException;

    int getFocusedWindowIgnoreHomeMenuKey() throws RemoteException;

    void getFreeformStackBounds(Rect rect) throws RemoteException;

    int getImeBgOplusFromAdaptation(String str) throws RemoteException;

    int getLongshotSurfaceLayer() throws RemoteException;

    int getLongshotSurfaceLayerByType(int i) throws RemoteException;

    IBinder getLongshotWindowByType(int i) throws RemoteException;

    SurfaceControl getLongshotWindowByTypeForR(int i) throws RemoteException;

    int getNavBarOplusFromAdaptation(String str, String str2) throws RemoteException;

    DisplayCutout.ParcelableWrapper getRawDisplayCutout() throws RemoteException;

    OplusScreenShotResult getScreenshot(OplusScreenShotOptions oplusScreenShotOptions) throws RemoteException;

    List<Rect> getSplitAreaRegion() throws RemoteException;

    int getStatusBarOplusFromAdaptation(String str, String str2) throws RemoteException;

    int getTypedWindowLayer(int i) throws RemoteException;

    List<OplusWindowInfo> getVisibleWindowInfo(int i) throws RemoteException;

    void getWindowVisibleDisplayFrame(IWindowSession iWindowSession, IWindow iWindow, Rect rect) throws RemoteException;

    boolean isActivityNeedPalette(String str, String str2) throws RemoteException;

    boolean isEdgePanelExpand() throws RemoteException;

    boolean isFloatAssistExpand() throws RemoteException;

    boolean isFullScreen() throws RemoteException;

    boolean isInFreeformMode() throws RemoteException;

    boolean isInputShow() throws RemoteException;

    boolean isKeyguardShowingAndNotOccluded() throws RemoteException;

    boolean isLockOnShow() throws RemoteException;

    boolean isLockWndShow() throws RemoteException;

    boolean isNavigationBarVisible() throws RemoteException;

    boolean isRotatingLw() throws RemoteException;

    boolean isRotationLocked(int i) throws RemoteException;

    boolean isSIMUnlockRunning() throws RemoteException;

    boolean isShortcutsPanelShow() throws RemoteException;

    boolean isStatusBarVisible() throws RemoteException;

    boolean isVolumeShow() throws RemoteException;

    boolean isWindowShownForUid(int i) throws RemoteException;

    void keyguardSetApkLockScreenShowing(boolean z) throws RemoteException;

    void keyguardShowSecureApkLock(boolean z) throws RemoteException;

    void longshotInjectInput(InputEvent inputEvent, int i) throws RemoteException;

    void longshotInjectInputBegin() throws RemoteException;

    void longshotInjectInputEnd() throws RemoteException;

    void longshotNotifyConnected(boolean z) throws RemoteException;

    List<ComponentName> notifyScreenshotListeners(int i) throws RemoteException;

    void registerOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener iOplusDarkModeListener) throws RemoteException;

    void registerOplusFoldSwitchStateObserver(IOplusFoldSwitchStateObserver iOplusFoldSwitchStateObserver) throws RemoteException;

    void registerOplusWindowStateObserver(IOplusWindowStateObserver iOplusWindowStateObserver) throws RemoteException;

    void registerRemoteAnimationsForWindow(IWindow iWindow, RemoteAnimationDefinition remoteAnimationDefinition) throws RemoteException;

    void registerWallpaperObserver(IOplusWallpaperObserver iOplusWallpaperObserver, int i) throws RemoteException;

    void removeBracketMirrorRootLeash(IBinder iBinder) throws RemoteException;

    void removeWindowShownOnKeyguard() throws RemoteException;

    void requestDismissKeyguard() throws RemoteException;

    void requestKeyguard(String str) throws RemoteException;

    void requestScrollCapture(int i, IBinder iBinder, int i2, IOplusScrollCaptureResponseListener iOplusScrollCaptureResponseListener, Bundle bundle) throws RemoteException;

    List<Bundle> requestVisibleWindows() throws RemoteException;

    void setAppAnimatingState(boolean z, String str) throws RemoteException;

    void setBootAnimationRotationLock(boolean z) throws RemoteException;

    boolean setInsetAnimationTid(int i, int i2, boolean z) throws RemoteException;

    boolean setJoyStickConfig(int i, String str) throws RemoteException;

    boolean setJoyStickStatus(int i) throws RemoteException;

    boolean setJoyStickSwitch(int i) throws RemoteException;

    void setMagnification(Bundle bundle) throws RemoteException;

    void setMagnificationSpecEx(MagnificationSpec magnificationSpec) throws RemoteException;

    void setOplusLaunchViewInfoForWindow(IWindow iWindow, LaunchViewInfo launchViewInfo) throws RemoteException;

    boolean setPreferredDisplayMode(int i) throws RemoteException;

    void setRotationLock(boolean z, int i) throws RemoteException;

    void showTransientNavbar(int i) throws RemoteException;

    void startGestureAnmation(IOplusGestureAnimationRunner iOplusGestureAnimationRunner, Bundle bundle) throws RemoteException;

    void startOplusDragWindow(String str, int i, int i2, Bundle bundle) throws RemoteException;

    boolean transferTouch(IBinder iBinder, int i) throws RemoteException;

    void transferTouchFocus(IBinder iBinder, IBinder iBinder2) throws RemoteException;

    void unregisterOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener iOplusDarkModeListener) throws RemoteException;

    void unregisterOplusFoldSwitchStateObserver(IOplusFoldSwitchStateObserver iOplusFoldSwitchStateObserver) throws RemoteException;

    void unregisterOplusWindowStateObserver(IOplusWindowStateObserver iOplusWindowStateObserver) throws RemoteException;

    void unregisterRemoteAnimationsForWindow(IWindow iWindow) throws RemoteException;

    void unregisterWallpaperObserver(IOplusWallpaperObserver iOplusWallpaperObserver, int i) throws RemoteException;

    boolean updateInvalidRegion(String str, List<RectF> list, boolean z, boolean z2, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusWindowManager {
        @Override // android.view.IOplusWindowManager
        public boolean isLockWndShow() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void keyguardSetApkLockScreenShowing(boolean showing) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public IBinder getApkUnlockWindow() throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public void keyguardShowSecureApkLock(boolean show) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean isLockOnShow() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isSIMUnlockRunning() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isInputShow() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isFullScreen() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isStatusBarVisible() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isRotatingLw() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void setMagnification(Bundle bundle) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void setMagnificationSpecEx(MagnificationSpec spec) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void requestDismissKeyguard() throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void requestKeyguard(String command) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean isWindowShownForUid(int uid) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void removeWindowShownOnKeyguard() throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public String getCurrentFocus() throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public DisplayCutout.ParcelableWrapper getRawDisplayCutout() throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public Rect getFloatWindowRect(int displayId) throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public void startOplusDragWindow(String packageName, int resId, int mode, Bundle options) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void registerOplusWindowStateObserver(IOplusWindowStateObserver observer) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void unregisterOplusWindowStateObserver(IOplusWindowStateObserver observer) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean isInFreeformMode() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void getFreeformStackBounds(Rect outBounds) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean isActivityNeedPalette(String pkg, String activityName) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public int getNavBarOplusFromAdaptation(String pkg, String activityName) throws RemoteException {
            return 0;
        }

        @Override // android.view.IOplusWindowManager
        public int getStatusBarOplusFromAdaptation(String pkg, String activityName) throws RemoteException {
            return 0;
        }

        @Override // android.view.IOplusWindowManager
        public int getImeBgOplusFromAdaptation(String pkg) throws RemoteException {
            return 0;
        }

        @Override // android.view.IOplusWindowManager
        public int getTypedWindowLayer(int type) throws RemoteException {
            return 0;
        }

        @Override // android.view.IOplusWindowManager
        public int getFocusedWindowIgnoreHomeMenuKey() throws RemoteException {
            return 0;
        }

        @Override // android.view.IOplusWindowManager
        public void registerOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener listener) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void unregisterOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener listener) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void setBootAnimationRotationLock(boolean lockRotation) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean updateInvalidRegion(String regionKey, List<RectF> region, boolean disposable, boolean isDelete, Bundle extras) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean setJoyStickConfig(int configType, String config) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean setJoyStickStatus(int configStatus) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean setJoyStickSwitch(int switchStatus) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void getWindowVisibleDisplayFrame(IWindowSession session, IWindow client, Rect outDisplayFrame) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public List<OplusWindowInfo> getAllVisibleWindowInfo() throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public List<OplusWindowInfo> getVisibleWindowInfo(int displayId) throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public List<Rect> getSplitAreaRegion() throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public void showTransientNavbar(int showFlag) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void startGestureAnmation(IOplusGestureAnimationRunner guestureAnimationRunner, Bundle bOptions) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean setInsetAnimationTid(int pid, int tid, boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void directFindCmd(OplusDirectFindCmd findCmd) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void registerRemoteAnimationsForWindow(IWindow window, RemoteAnimationDefinition definition) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void unregisterRemoteAnimationsForWindow(IWindow window) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void setOplusLaunchViewInfoForWindow(IWindow window, LaunchViewInfo launchViewInfo) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void clearOplusLaunchViewInfoForWindow(IWindow window) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void addBracketMirrorRootLeash(IBinder mirrorToken, SurfaceControl mirrorRoot, SurfaceControl hostRoot) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void removeBracketMirrorRootLeash(IBinder mirrorToken) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void getFocusedWindowFrame(Rect frame) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public int getLongshotSurfaceLayer() throws RemoteException {
            return 0;
        }

        @Override // android.view.IOplusWindowManager
        public int getLongshotSurfaceLayerByType(int type) throws RemoteException {
            return 0;
        }

        @Override // android.view.IOplusWindowManager
        public void longshotNotifyConnected(boolean isConnected) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean isNavigationBarVisible() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isShortcutsPanelShow() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void longshotInjectInput(InputEvent event, int mode) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean isKeyguardShowingAndNotOccluded() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void longshotInjectInputBegin() throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void longshotInjectInputEnd() throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public IBinder getLongshotWindowByType(int type) throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public SurfaceControl getLongshotWindowByTypeForR(int type) throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public OplusScreenShotResult getScreenshot(OplusScreenShotOptions options) throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isVolumeShow() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isFloatAssistExpand() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isEdgePanelExpand() throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void requestScrollCapture(int displayId, IBinder behindClient, int taskId, IOplusScrollCaptureResponseListener listener, Bundle extras) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void registerWallpaperObserver(IOplusWallpaperObserver observer, int displayId) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void unregisterWallpaperObserver(IOplusWallpaperObserver observer, int displayId) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void transferTouchFocus(IBinder fromChannelToken, IBinder toChannelToken) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean transferTouch(IBinder destChannelToken, int displayId) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public int getFocusedWindowBackGestureRestriction() throws RemoteException {
            return 0;
        }

        @Override // android.view.IOplusWindowManager
        public boolean isRotationLocked(int displayId) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void setRotationLock(boolean enabled, int displayId) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public List<Bundle> requestVisibleWindows() throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public List<ComponentName> notifyScreenshotListeners(int displayId) throws RemoteException {
            return null;
        }

        @Override // android.view.IOplusWindowManager
        public int getDisplayIdForPackageName(String packageName) throws RemoteException {
            return 0;
        }

        @Override // android.view.IOplusWindowManager
        public void registerOplusFoldSwitchStateObserver(IOplusFoldSwitchStateObserver observer) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public void unregisterOplusFoldSwitchStateObserver(IOplusFoldSwitchStateObserver observer) throws RemoteException {
        }

        @Override // android.view.IOplusWindowManager
        public boolean setPreferredDisplayMode(int modeId) throws RemoteException {
            return false;
        }

        @Override // android.view.IOplusWindowManager
        public void setAppAnimatingState(boolean animating, String pkg) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusWindowManager {
        static final int TRANSACTION_addBracketMirrorRootLeash = 50;
        static final int TRANSACTION_clearOplusLaunchViewInfoForWindow = 49;
        static final int TRANSACTION_directFindCmd = 45;
        static final int TRANSACTION_getAllVisibleWindowInfo = 39;
        static final int TRANSACTION_getApkUnlockWindow = 3;
        static final int TRANSACTION_getCurrentFocus = 17;
        static final int TRANSACTION_getDisplayIdForPackageName = 78;
        static final int TRANSACTION_getFloatWindowRect = 19;
        static final int TRANSACTION_getFocusedWindowBackGestureRestriction = 73;
        static final int TRANSACTION_getFocusedWindowFrame = 52;
        static final int TRANSACTION_getFocusedWindowIgnoreHomeMenuKey = 30;
        static final int TRANSACTION_getFreeformStackBounds = 24;
        static final int TRANSACTION_getImeBgOplusFromAdaptation = 28;
        static final int TRANSACTION_getLongshotSurfaceLayer = 53;
        static final int TRANSACTION_getLongshotSurfaceLayerByType = 54;
        static final int TRANSACTION_getLongshotWindowByType = 62;
        static final int TRANSACTION_getLongshotWindowByTypeForR = 63;
        static final int TRANSACTION_getNavBarOplusFromAdaptation = 26;
        static final int TRANSACTION_getRawDisplayCutout = 18;
        static final int TRANSACTION_getScreenshot = 64;
        static final int TRANSACTION_getSplitAreaRegion = 41;
        static final int TRANSACTION_getStatusBarOplusFromAdaptation = 27;
        static final int TRANSACTION_getTypedWindowLayer = 29;
        static final int TRANSACTION_getVisibleWindowInfo = 40;
        static final int TRANSACTION_getWindowVisibleDisplayFrame = 38;
        static final int TRANSACTION_isActivityNeedPalette = 25;
        static final int TRANSACTION_isEdgePanelExpand = 67;
        static final int TRANSACTION_isFloatAssistExpand = 66;
        static final int TRANSACTION_isFullScreen = 8;
        static final int TRANSACTION_isInFreeformMode = 23;
        static final int TRANSACTION_isInputShow = 7;
        static final int TRANSACTION_isKeyguardShowingAndNotOccluded = 59;
        static final int TRANSACTION_isLockOnShow = 5;
        static final int TRANSACTION_isLockWndShow = 1;
        static final int TRANSACTION_isNavigationBarVisible = 56;
        static final int TRANSACTION_isRotatingLw = 10;
        static final int TRANSACTION_isRotationLocked = 74;
        static final int TRANSACTION_isSIMUnlockRunning = 6;
        static final int TRANSACTION_isShortcutsPanelShow = 57;
        static final int TRANSACTION_isStatusBarVisible = 9;
        static final int TRANSACTION_isVolumeShow = 65;
        static final int TRANSACTION_isWindowShownForUid = 15;
        static final int TRANSACTION_keyguardSetApkLockScreenShowing = 2;
        static final int TRANSACTION_keyguardShowSecureApkLock = 4;
        static final int TRANSACTION_longshotInjectInput = 58;
        static final int TRANSACTION_longshotInjectInputBegin = 60;
        static final int TRANSACTION_longshotInjectInputEnd = 61;
        static final int TRANSACTION_longshotNotifyConnected = 55;
        static final int TRANSACTION_notifyScreenshotListeners = 77;
        static final int TRANSACTION_registerOnUiModeConfigurationChangeFinishListener = 31;
        static final int TRANSACTION_registerOplusFoldSwitchStateObserver = 79;
        static final int TRANSACTION_registerOplusWindowStateObserver = 21;
        static final int TRANSACTION_registerRemoteAnimationsForWindow = 46;
        static final int TRANSACTION_registerWallpaperObserver = 69;
        static final int TRANSACTION_removeBracketMirrorRootLeash = 51;
        static final int TRANSACTION_removeWindowShownOnKeyguard = 16;
        static final int TRANSACTION_requestDismissKeyguard = 13;
        static final int TRANSACTION_requestKeyguard = 14;
        static final int TRANSACTION_requestScrollCapture = 68;
        static final int TRANSACTION_requestVisibleWindows = 76;
        static final int TRANSACTION_setAppAnimatingState = 82;
        static final int TRANSACTION_setBootAnimationRotationLock = 33;
        static final int TRANSACTION_setInsetAnimationTid = 44;
        static final int TRANSACTION_setJoyStickConfig = 35;
        static final int TRANSACTION_setJoyStickStatus = 36;
        static final int TRANSACTION_setJoyStickSwitch = 37;
        static final int TRANSACTION_setMagnification = 11;
        static final int TRANSACTION_setMagnificationSpecEx = 12;
        static final int TRANSACTION_setOplusLaunchViewInfoForWindow = 48;
        static final int TRANSACTION_setPreferredDisplayMode = 81;
        static final int TRANSACTION_setRotationLock = 75;
        static final int TRANSACTION_showTransientNavbar = 42;
        static final int TRANSACTION_startGestureAnmation = 43;
        static final int TRANSACTION_startOplusDragWindow = 20;
        static final int TRANSACTION_transferTouch = 72;
        static final int TRANSACTION_transferTouchFocus = 71;
        static final int TRANSACTION_unregisterOnUiModeConfigurationChangeFinishListener = 32;
        static final int TRANSACTION_unregisterOplusFoldSwitchStateObserver = 80;
        static final int TRANSACTION_unregisterOplusWindowStateObserver = 22;
        static final int TRANSACTION_unregisterRemoteAnimationsForWindow = 47;
        static final int TRANSACTION_unregisterWallpaperObserver = 70;
        static final int TRANSACTION_updateInvalidRegion = 34;

        public Stub() {
            attachInterface(this, IOplusWindowManager.DESCRIPTOR);
        }

        public static IOplusWindowManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusWindowManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusWindowManager)) {
                return (IOplusWindowManager) iin;
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
                    return "isLockWndShow";
                case 2:
                    return "keyguardSetApkLockScreenShowing";
                case 3:
                    return "getApkUnlockWindow";
                case 4:
                    return "keyguardShowSecureApkLock";
                case 5:
                    return "isLockOnShow";
                case 6:
                    return "isSIMUnlockRunning";
                case 7:
                    return "isInputShow";
                case 8:
                    return "isFullScreen";
                case 9:
                    return "isStatusBarVisible";
                case 10:
                    return "isRotatingLw";
                case 11:
                    return "setMagnification";
                case 12:
                    return "setMagnificationSpecEx";
                case 13:
                    return "requestDismissKeyguard";
                case 14:
                    return "requestKeyguard";
                case 15:
                    return "isWindowShownForUid";
                case 16:
                    return "removeWindowShownOnKeyguard";
                case 17:
                    return "getCurrentFocus";
                case 18:
                    return "getRawDisplayCutout";
                case 19:
                    return "getFloatWindowRect";
                case 20:
                    return "startOplusDragWindow";
                case 21:
                    return "registerOplusWindowStateObserver";
                case 22:
                    return "unregisterOplusWindowStateObserver";
                case 23:
                    return "isInFreeformMode";
                case 24:
                    return "getFreeformStackBounds";
                case 25:
                    return "isActivityNeedPalette";
                case 26:
                    return "getNavBarOplusFromAdaptation";
                case 27:
                    return "getStatusBarOplusFromAdaptation";
                case 28:
                    return "getImeBgOplusFromAdaptation";
                case 29:
                    return "getTypedWindowLayer";
                case 30:
                    return "getFocusedWindowIgnoreHomeMenuKey";
                case 31:
                    return "registerOnUiModeConfigurationChangeFinishListener";
                case 32:
                    return "unregisterOnUiModeConfigurationChangeFinishListener";
                case 33:
                    return "setBootAnimationRotationLock";
                case 34:
                    return "updateInvalidRegion";
                case 35:
                    return "setJoyStickConfig";
                case 36:
                    return "setJoyStickStatus";
                case 37:
                    return "setJoyStickSwitch";
                case 38:
                    return "getWindowVisibleDisplayFrame";
                case 39:
                    return "getAllVisibleWindowInfo";
                case 40:
                    return "getVisibleWindowInfo";
                case 41:
                    return "getSplitAreaRegion";
                case 42:
                    return "showTransientNavbar";
                case 43:
                    return "startGestureAnmation";
                case 44:
                    return "setInsetAnimationTid";
                case 45:
                    return "directFindCmd";
                case 46:
                    return "registerRemoteAnimationsForWindow";
                case 47:
                    return "unregisterRemoteAnimationsForWindow";
                case 48:
                    return "setOplusLaunchViewInfoForWindow";
                case 49:
                    return "clearOplusLaunchViewInfoForWindow";
                case 50:
                    return "addBracketMirrorRootLeash";
                case 51:
                    return "removeBracketMirrorRootLeash";
                case 52:
                    return "getFocusedWindowFrame";
                case 53:
                    return "getLongshotSurfaceLayer";
                case 54:
                    return "getLongshotSurfaceLayerByType";
                case 55:
                    return "longshotNotifyConnected";
                case 56:
                    return "isNavigationBarVisible";
                case 57:
                    return "isShortcutsPanelShow";
                case 58:
                    return "longshotInjectInput";
                case 59:
                    return "isKeyguardShowingAndNotOccluded";
                case 60:
                    return "longshotInjectInputBegin";
                case 61:
                    return "longshotInjectInputEnd";
                case 62:
                    return "getLongshotWindowByType";
                case 63:
                    return "getLongshotWindowByTypeForR";
                case 64:
                    return "getScreenshot";
                case 65:
                    return "isVolumeShow";
                case 66:
                    return "isFloatAssistExpand";
                case 67:
                    return "isEdgePanelExpand";
                case 68:
                    return "requestScrollCapture";
                case 69:
                    return "registerWallpaperObserver";
                case 70:
                    return "unregisterWallpaperObserver";
                case 71:
                    return "transferTouchFocus";
                case 72:
                    return "transferTouch";
                case 73:
                    return "getFocusedWindowBackGestureRestriction";
                case 74:
                    return "isRotationLocked";
                case 75:
                    return "setRotationLock";
                case 76:
                    return "requestVisibleWindows";
                case 77:
                    return "notifyScreenshotListeners";
                case 78:
                    return "getDisplayIdForPackageName";
                case 79:
                    return "registerOplusFoldSwitchStateObserver";
                case 80:
                    return "unregisterOplusFoldSwitchStateObserver";
                case 81:
                    return "setPreferredDisplayMode";
                case 82:
                    return "setAppAnimatingState";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusWindowManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusWindowManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _result = isLockWndShow();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            keyguardSetApkLockScreenShowing(_arg0);
                            reply.writeNoException();
                            return true;
                        case 3:
                            IBinder _result2 = getApkUnlockWindow();
                            reply.writeNoException();
                            reply.writeStrongBinder(_result2);
                            return true;
                        case 4:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            keyguardShowSecureApkLock(_arg02);
                            reply.writeNoException();
                            return true;
                        case 5:
                            boolean _result3 = isLockOnShow();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 6:
                            boolean _result4 = isSIMUnlockRunning();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 7:
                            boolean _result5 = isInputShow();
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 8:
                            boolean _result6 = isFullScreen();
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 9:
                            boolean _result7 = isStatusBarVisible();
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 10:
                            boolean _result8 = isRotatingLw();
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 11:
                            Bundle _arg03 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            setMagnification(_arg03);
                            reply.writeNoException();
                            return true;
                        case 12:
                            MagnificationSpec _arg04 = (MagnificationSpec) data.readTypedObject(MagnificationSpec.CREATOR);
                            data.enforceNoDataAvail();
                            setMagnificationSpecEx(_arg04);
                            reply.writeNoException();
                            return true;
                        case 13:
                            requestDismissKeyguard();
                            reply.writeNoException();
                            return true;
                        case 14:
                            String _arg05 = data.readString();
                            data.enforceNoDataAvail();
                            requestKeyguard(_arg05);
                            reply.writeNoException();
                            return true;
                        case 15:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result9 = isWindowShownForUid(_arg06);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 16:
                            removeWindowShownOnKeyguard();
                            reply.writeNoException();
                            return true;
                        case 17:
                            String _result10 = getCurrentFocus();
                            reply.writeNoException();
                            reply.writeString(_result10);
                            return true;
                        case 18:
                            DisplayCutout.ParcelableWrapper _result11 = getRawDisplayCutout();
                            reply.writeNoException();
                            reply.writeTypedObject(_result11, 1);
                            return true;
                        case 19:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            Rect _result12 = getFloatWindowRect(_arg07);
                            reply.writeNoException();
                            reply.writeTypedObject(_result12, 1);
                            return true;
                        case 20:
                            String _arg08 = data.readString();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            Bundle _arg3 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            startOplusDragWindow(_arg08, _arg1, _arg2, _arg3);
                            reply.writeNoException();
                            return true;
                        case 21:
                            IOplusWindowStateObserver _arg09 = IOplusWindowStateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerOplusWindowStateObserver(_arg09);
                            reply.writeNoException();
                            return true;
                        case 22:
                            IOplusWindowStateObserver _arg010 = IOplusWindowStateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterOplusWindowStateObserver(_arg010);
                            reply.writeNoException();
                            return true;
                        case 23:
                            boolean _result13 = isInFreeformMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 24:
                            Rect _arg011 = new Rect();
                            data.enforceNoDataAvail();
                            getFreeformStackBounds(_arg011);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg011, 1);
                            return true;
                        case 25:
                            String _arg012 = data.readString();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result14 = isActivityNeedPalette(_arg012, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        case 26:
                            String _arg013 = data.readString();
                            String _arg13 = data.readString();
                            data.enforceNoDataAvail();
                            int _result15 = getNavBarOplusFromAdaptation(_arg013, _arg13);
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        case 27:
                            String _arg014 = data.readString();
                            String _arg14 = data.readString();
                            data.enforceNoDataAvail();
                            int _result16 = getStatusBarOplusFromAdaptation(_arg014, _arg14);
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        case 28:
                            String _arg015 = data.readString();
                            data.enforceNoDataAvail();
                            int _result17 = getImeBgOplusFromAdaptation(_arg015);
                            reply.writeNoException();
                            reply.writeInt(_result17);
                            return true;
                        case 29:
                            int _arg016 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result18 = getTypedWindowLayer(_arg016);
                            reply.writeNoException();
                            reply.writeInt(_result18);
                            return true;
                        case 30:
                            int _result19 = getFocusedWindowIgnoreHomeMenuKey();
                            reply.writeNoException();
                            reply.writeInt(_result19);
                            return true;
                        case 31:
                            IOplusDarkModeListener _arg017 = IOplusDarkModeListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerOnUiModeConfigurationChangeFinishListener(_arg017);
                            return true;
                        case 32:
                            IOplusDarkModeListener _arg018 = IOplusDarkModeListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterOnUiModeConfigurationChangeFinishListener(_arg018);
                            return true;
                        case 33:
                            boolean _arg019 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setBootAnimationRotationLock(_arg019);
                            reply.writeNoException();
                            return true;
                        case 34:
                            String _arg020 = data.readString();
                            List<RectF> _arg15 = data.createTypedArrayList(RectF.CREATOR);
                            boolean _arg22 = data.readBoolean();
                            boolean _arg32 = data.readBoolean();
                            Bundle _arg4 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result20 = updateInvalidRegion(_arg020, _arg15, _arg22, _arg32, _arg4);
                            reply.writeNoException();
                            reply.writeBoolean(_result20);
                            return true;
                        case 35:
                            int _arg021 = data.readInt();
                            String _arg16 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result21 = setJoyStickConfig(_arg021, _arg16);
                            reply.writeNoException();
                            reply.writeBoolean(_result21);
                            return true;
                        case 36:
                            int _arg022 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result22 = setJoyStickStatus(_arg022);
                            reply.writeNoException();
                            reply.writeBoolean(_result22);
                            return true;
                        case 37:
                            int _arg023 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result23 = setJoyStickSwitch(_arg023);
                            reply.writeNoException();
                            reply.writeBoolean(_result23);
                            return true;
                        case 38:
                            IWindowSession _arg024 = IWindowSession.Stub.asInterface(data.readStrongBinder());
                            IWindow _arg17 = IWindow.Stub.asInterface(data.readStrongBinder());
                            Rect _arg23 = new Rect();
                            data.enforceNoDataAvail();
                            getWindowVisibleDisplayFrame(_arg024, _arg17, _arg23);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg23, 1);
                            return true;
                        case 39:
                            List<OplusWindowInfo> _result24 = getAllVisibleWindowInfo();
                            reply.writeNoException();
                            reply.writeTypedList(_result24, 1);
                            return true;
                        case 40:
                            int _arg025 = data.readInt();
                            data.enforceNoDataAvail();
                            List<OplusWindowInfo> _result25 = getVisibleWindowInfo(_arg025);
                            reply.writeNoException();
                            reply.writeTypedList(_result25, 1);
                            return true;
                        case 41:
                            List<Rect> _result26 = getSplitAreaRegion();
                            reply.writeNoException();
                            reply.writeTypedList(_result26, 1);
                            return true;
                        case 42:
                            int _arg026 = data.readInt();
                            data.enforceNoDataAvail();
                            showTransientNavbar(_arg026);
                            return true;
                        case 43:
                            IOplusGestureAnimationRunner _arg027 = IOplusGestureAnimationRunner.Stub.asInterface(data.readStrongBinder());
                            Bundle _arg18 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            startGestureAnmation(_arg027, _arg18);
                            reply.writeNoException();
                            return true;
                        case 44:
                            int _arg028 = data.readInt();
                            int _arg19 = data.readInt();
                            boolean _arg24 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result27 = setInsetAnimationTid(_arg028, _arg19, _arg24);
                            reply.writeNoException();
                            reply.writeBoolean(_result27);
                            return true;
                        case 45:
                            OplusDirectFindCmd _arg029 = (OplusDirectFindCmd) data.readTypedObject(OplusDirectFindCmd.CREATOR);
                            data.enforceNoDataAvail();
                            directFindCmd(_arg029);
                            return true;
                        case 46:
                            IWindow _arg030 = IWindow.Stub.asInterface(data.readStrongBinder());
                            RemoteAnimationDefinition _arg110 = (RemoteAnimationDefinition) data.readTypedObject(RemoteAnimationDefinition.CREATOR);
                            data.enforceNoDataAvail();
                            registerRemoteAnimationsForWindow(_arg030, _arg110);
                            reply.writeNoException();
                            return true;
                        case 47:
                            IWindow _arg031 = IWindow.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterRemoteAnimationsForWindow(_arg031);
                            reply.writeNoException();
                            return true;
                        case 48:
                            IWindow _arg032 = IWindow.Stub.asInterface(data.readStrongBinder());
                            LaunchViewInfo _arg111 = (LaunchViewInfo) data.readTypedObject(LaunchViewInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setOplusLaunchViewInfoForWindow(_arg032, _arg111);
                            reply.writeNoException();
                            return true;
                        case 49:
                            IBinder _arg033 = data.readStrongBinder();
                            IWindow _arg034 = IWindow.Stub.asInterface(_arg033);
                            data.enforceNoDataAvail();
                            clearOplusLaunchViewInfoForWindow(_arg034);
                            reply.writeNoException();
                            return true;
                        case 50:
                            IBinder _arg035 = data.readStrongBinder();
                            SurfaceControl _arg112 = (SurfaceControl) data.readTypedObject(SurfaceControl.CREATOR);
                            SurfaceControl _arg25 = (SurfaceControl) data.readTypedObject(SurfaceControl.CREATOR);
                            data.enforceNoDataAvail();
                            addBracketMirrorRootLeash(_arg035, _arg112, _arg25);
                            reply.writeNoException();
                            return true;
                        case 51:
                            IBinder _arg036 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            removeBracketMirrorRootLeash(_arg036);
                            reply.writeNoException();
                            return true;
                        case 52:
                            Rect _arg037 = new Rect();
                            data.enforceNoDataAvail();
                            getFocusedWindowFrame(_arg037);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg037, 1);
                            return true;
                        case 53:
                            int _result28 = getLongshotSurfaceLayer();
                            reply.writeNoException();
                            reply.writeInt(_result28);
                            return true;
                        case 54:
                            int _arg038 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result29 = getLongshotSurfaceLayerByType(_arg038);
                            reply.writeNoException();
                            reply.writeInt(_result29);
                            return true;
                        case 55:
                            boolean _arg039 = data.readBoolean();
                            data.enforceNoDataAvail();
                            longshotNotifyConnected(_arg039);
                            return true;
                        case 56:
                            boolean _result30 = isNavigationBarVisible();
                            reply.writeNoException();
                            reply.writeBoolean(_result30);
                            return true;
                        case 57:
                            boolean _result31 = isShortcutsPanelShow();
                            reply.writeNoException();
                            reply.writeBoolean(_result31);
                            return true;
                        case 58:
                            InputEvent _arg040 = (InputEvent) data.readTypedObject(InputEvent.CREATOR);
                            int _arg113 = data.readInt();
                            data.enforceNoDataAvail();
                            longshotInjectInput(_arg040, _arg113);
                            return true;
                        case 59:
                            boolean _result32 = isKeyguardShowingAndNotOccluded();
                            reply.writeNoException();
                            reply.writeBoolean(_result32);
                            return true;
                        case 60:
                            longshotInjectInputBegin();
                            return true;
                        case 61:
                            longshotInjectInputEnd();
                            return true;
                        case 62:
                            int _arg041 = data.readInt();
                            data.enforceNoDataAvail();
                            IBinder _result33 = getLongshotWindowByType(_arg041);
                            reply.writeNoException();
                            reply.writeStrongBinder(_result33);
                            return true;
                        case 63:
                            int _arg042 = data.readInt();
                            data.enforceNoDataAvail();
                            SurfaceControl _result34 = getLongshotWindowByTypeForR(_arg042);
                            reply.writeNoException();
                            reply.writeTypedObject(_result34, 1);
                            return true;
                        case 64:
                            OplusScreenShotOptions _arg043 = (OplusScreenShotOptions) data.readTypedObject(OplusScreenShotOptions.CREATOR);
                            data.enforceNoDataAvail();
                            OplusScreenShotResult _result35 = getScreenshot(_arg043);
                            reply.writeNoException();
                            reply.writeTypedObject(_result35, 1);
                            return true;
                        case 65:
                            boolean _result36 = isVolumeShow();
                            reply.writeNoException();
                            reply.writeBoolean(_result36);
                            return true;
                        case 66:
                            boolean _result37 = isFloatAssistExpand();
                            reply.writeNoException();
                            reply.writeBoolean(_result37);
                            return true;
                        case 67:
                            boolean _result38 = isEdgePanelExpand();
                            reply.writeNoException();
                            reply.writeBoolean(_result38);
                            return true;
                        case 68:
                            int _arg044 = data.readInt();
                            IBinder _arg114 = data.readStrongBinder();
                            int _arg26 = data.readInt();
                            IOplusScrollCaptureResponseListener _arg33 = IOplusScrollCaptureResponseListener.Stub.asInterface(data.readStrongBinder());
                            Bundle _arg42 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            requestScrollCapture(_arg044, _arg114, _arg26, _arg33, _arg42);
                            reply.writeNoException();
                            return true;
                        case 69:
                            IOplusWallpaperObserver _arg045 = IOplusWallpaperObserver.Stub.asInterface(data.readStrongBinder());
                            int _arg115 = data.readInt();
                            data.enforceNoDataAvail();
                            registerWallpaperObserver(_arg045, _arg115);
                            reply.writeNoException();
                            return true;
                        case 70:
                            IBinder _arg046 = data.readStrongBinder();
                            IOplusWallpaperObserver _arg047 = IOplusWallpaperObserver.Stub.asInterface(_arg046);
                            int _arg116 = data.readInt();
                            data.enforceNoDataAvail();
                            unregisterWallpaperObserver(_arg047, _arg116);
                            reply.writeNoException();
                            return true;
                        case 71:
                            IBinder _arg048 = data.readStrongBinder();
                            IBinder _arg117 = data.readStrongBinder();
                            data.enforceNoDataAvail();
                            transferTouchFocus(_arg048, _arg117);
                            reply.writeNoException();
                            return true;
                        case 72:
                            IBinder _arg049 = data.readStrongBinder();
                            int _arg118 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result39 = transferTouch(_arg049, _arg118);
                            reply.writeNoException();
                            reply.writeBoolean(_result39);
                            return true;
                        case 73:
                            int _result40 = getFocusedWindowBackGestureRestriction();
                            reply.writeNoException();
                            reply.writeInt(_result40);
                            return true;
                        case 74:
                            int _arg050 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result41 = isRotationLocked(_arg050);
                            reply.writeNoException();
                            reply.writeBoolean(_result41);
                            return true;
                        case 75:
                            boolean _arg051 = data.readBoolean();
                            int _arg119 = data.readInt();
                            data.enforceNoDataAvail();
                            setRotationLock(_arg051, _arg119);
                            reply.writeNoException();
                            return true;
                        case 76:
                            List<Bundle> _result42 = requestVisibleWindows();
                            reply.writeNoException();
                            reply.writeTypedList(_result42, 1);
                            return true;
                        case 77:
                            int _arg052 = data.readInt();
                            data.enforceNoDataAvail();
                            List<ComponentName> _result43 = notifyScreenshotListeners(_arg052);
                            reply.writeNoException();
                            reply.writeTypedList(_result43, 1);
                            return true;
                        case 78:
                            String _arg053 = data.readString();
                            data.enforceNoDataAvail();
                            int _result44 = getDisplayIdForPackageName(_arg053);
                            reply.writeNoException();
                            reply.writeInt(_result44);
                            return true;
                        case 79:
                            IOplusFoldSwitchStateObserver _arg054 = IOplusFoldSwitchStateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerOplusFoldSwitchStateObserver(_arg054);
                            reply.writeNoException();
                            return true;
                        case 80:
                            IOplusFoldSwitchStateObserver _arg055 = IOplusFoldSwitchStateObserver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterOplusFoldSwitchStateObserver(_arg055);
                            reply.writeNoException();
                            return true;
                        case 81:
                            int _arg056 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result45 = setPreferredDisplayMode(_arg056);
                            reply.writeNoException();
                            reply.writeBoolean(_result45);
                            return true;
                        case 82:
                            boolean _arg057 = data.readBoolean();
                            String _arg120 = data.readString();
                            data.enforceNoDataAvail();
                            setAppAnimatingState(_arg057, _arg120);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusWindowManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusWindowManager.DESCRIPTOR;
            }

            @Override // android.view.IOplusWindowManager
            public boolean isLockWndShow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void keyguardSetApkLockScreenShowing(boolean showing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeBoolean(showing);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public IBinder getApkUnlockWindow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void keyguardShowSecureApkLock(boolean show) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeBoolean(show);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isLockOnShow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isSIMUnlockRunning() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isInputShow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isFullScreen() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isStatusBarVisible() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isRotatingLw() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void setMagnification(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void setMagnificationSpecEx(MagnificationSpec spec) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeTypedObject(spec, 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void requestDismissKeyguard() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void requestKeyguard(String command) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeString(command);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isWindowShownForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void removeWindowShownOnKeyguard() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public String getCurrentFocus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public DisplayCutout.ParcelableWrapper getRawDisplayCutout() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    DisplayCutout.ParcelableWrapper _result = (DisplayCutout.ParcelableWrapper) _reply.readTypedObject(DisplayCutout.ParcelableWrapper.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public Rect getFloatWindowRect(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    Rect _result = (Rect) _reply.readTypedObject(Rect.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void startOplusDragWindow(String packageName, int resId, int mode, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(resId);
                    _data.writeInt(mode);
                    _data.writeTypedObject(options, 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void registerOplusWindowStateObserver(IOplusWindowStateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void unregisterOplusWindowStateObserver(IOplusWindowStateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isInFreeformMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void getFreeformStackBounds(Rect outBounds) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outBounds.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isActivityNeedPalette(String pkg, String activityName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(activityName);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public int getNavBarOplusFromAdaptation(String pkg, String activityName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(activityName);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public int getStatusBarOplusFromAdaptation(String pkg, String activityName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(activityName);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public int getImeBgOplusFromAdaptation(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public int getTypedWindowLayer(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public int getFocusedWindowIgnoreHomeMenuKey() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void registerOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(31, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void unregisterOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(32, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void setBootAnimationRotationLock(boolean lockRotation) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeBoolean(lockRotation);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean updateInvalidRegion(String regionKey, List<RectF> region, boolean disposable, boolean isDelete, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeString(regionKey);
                    _data.writeTypedList(region, 0);
                    _data.writeBoolean(disposable);
                    _data.writeBoolean(isDelete);
                    _data.writeTypedObject(extras, 0);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean setJoyStickConfig(int configType, String config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(configType);
                    _data.writeString(config);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean setJoyStickStatus(int configStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(configStatus);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean setJoyStickSwitch(int switchStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(switchStatus);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void getWindowVisibleDisplayFrame(IWindowSession session, IWindow client, Rect outDisplayFrame) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(session);
                    _data.writeStrongInterface(client);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outDisplayFrame.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public List<OplusWindowInfo> getAllVisibleWindowInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    List<OplusWindowInfo> _result = _reply.createTypedArrayList(OplusWindowInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public List<OplusWindowInfo> getVisibleWindowInfo(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    List<OplusWindowInfo> _result = _reply.createTypedArrayList(OplusWindowInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public List<Rect> getSplitAreaRegion() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    List<Rect> _result = _reply.createTypedArrayList(Rect.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void showTransientNavbar(int showFlag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(showFlag);
                    this.mRemote.transact(42, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void startGestureAnmation(IOplusGestureAnimationRunner guestureAnimationRunner, Bundle bOptions) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(guestureAnimationRunner);
                    _data.writeTypedObject(bOptions, 0);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean setInsetAnimationTid(int pid, int tid, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(tid);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void directFindCmd(OplusDirectFindCmd findCmd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeTypedObject(findCmd, 0);
                    this.mRemote.transact(45, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void registerRemoteAnimationsForWindow(IWindow window, RemoteAnimationDefinition definition) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(window);
                    _data.writeTypedObject(definition, 0);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void unregisterRemoteAnimationsForWindow(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(window);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void setOplusLaunchViewInfoForWindow(IWindow window, LaunchViewInfo launchViewInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(window);
                    _data.writeTypedObject(launchViewInfo, 0);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void clearOplusLaunchViewInfoForWindow(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(window);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void addBracketMirrorRootLeash(IBinder mirrorToken, SurfaceControl mirrorRoot, SurfaceControl hostRoot) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongBinder(mirrorToken);
                    _data.writeTypedObject(mirrorRoot, 0);
                    _data.writeTypedObject(hostRoot, 0);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void removeBracketMirrorRootLeash(IBinder mirrorToken) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongBinder(mirrorToken);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void getFocusedWindowFrame(Rect frame) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        frame.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public int getLongshotSurfaceLayer() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public int getLongshotSurfaceLayerByType(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void longshotNotifyConnected(boolean isConnected) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeBoolean(isConnected);
                    this.mRemote.transact(55, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isNavigationBarVisible() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isShortcutsPanelShow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void longshotInjectInput(InputEvent event, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeTypedObject(event, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(58, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isKeyguardShowingAndNotOccluded() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void longshotInjectInputBegin() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(60, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void longshotInjectInputEnd() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(61, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public IBinder getLongshotWindowByType(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public SurfaceControl getLongshotWindowByTypeForR(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    SurfaceControl _result = (SurfaceControl) _reply.readTypedObject(SurfaceControl.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public OplusScreenShotResult getScreenshot(OplusScreenShotOptions options) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeTypedObject(options, 0);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    OplusScreenShotResult _result = (OplusScreenShotResult) _reply.readTypedObject(OplusScreenShotResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isVolumeShow() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isFloatAssistExpand() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isEdgePanelExpand() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void requestScrollCapture(int displayId, IBinder behindClient, int taskId, IOplusScrollCaptureResponseListener listener, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeStrongBinder(behindClient);
                    _data.writeInt(taskId);
                    _data.writeStrongInterface(listener);
                    _data.writeTypedObject(extras, 0);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void registerWallpaperObserver(IOplusWallpaperObserver observer, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    _data.writeInt(displayId);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void unregisterWallpaperObserver(IOplusWallpaperObserver observer, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    _data.writeInt(displayId);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void transferTouchFocus(IBinder fromChannelToken, IBinder toChannelToken) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongBinder(fromChannelToken);
                    _data.writeStrongBinder(toChannelToken);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean transferTouch(IBinder destChannelToken, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongBinder(destChannelToken);
                    _data.writeInt(displayId);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public int getFocusedWindowBackGestureRestriction() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean isRotationLocked(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void setRotationLock(boolean enabled, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    _data.writeInt(displayId);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public List<Bundle> requestVisibleWindows() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    List<Bundle> _result = _reply.createTypedArrayList(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public List<ComponentName> notifyScreenshotListeners(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    List<ComponentName> _result = _reply.createTypedArrayList(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public int getDisplayIdForPackageName(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void registerOplusFoldSwitchStateObserver(IOplusFoldSwitchStateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void unregisterOplusFoldSwitchStateObserver(IOplusFoldSwitchStateObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeStrongInterface(observer);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public boolean setPreferredDisplayMode(int modeId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeInt(modeId);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IOplusWindowManager
            public void setAppAnimatingState(boolean animating, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusWindowManager.DESCRIPTOR);
                    _data.writeBoolean(animating);
                    _data.writeString(pkg);
                    this.mRemote.transact(82, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 81;
        }
    }
}
