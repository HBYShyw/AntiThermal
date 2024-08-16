package com.android.server.wm;

import android.os.Handler;
import android.view.WindowLayout;
import android.window.ClientWindowFrames;
import com.android.internal.util.ScreenshotHelper;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayPolicyWrapper {
    default int getBottomGestureAdditionalInset() {
        return 0;
    }

    default DisplayContent getDisplayContent() {
        return null;
    }

    default WindowState getFocusedWindow() {
        return null;
    }

    default Handler getHandler() {
        return null;
    }

    default int getNavBarFrameHeight(int i) {
        return 0;
    }

    default int getNavBarHeight(int i) {
        return 0;
    }

    default ScreenshotHelper getScreenshotHelper() {
        return null;
    }

    default Object getServiceAcquireLock() {
        return null;
    }

    default ClientWindowFrames getTmpClientFrames() {
        return null;
    }

    default WindowLayout getWindowLayout() {
        return null;
    }

    default WindowManagerService getWindowManagerService() {
        return null;
    }

    default void setBottomGestureAdditionalInset(int i) {
    }

    default IDisplayPolicyExt getExtImpl() {
        return new IDisplayPolicyExt() { // from class: com.android.server.wm.IDisplayPolicyWrapper.1
        };
    }
}
