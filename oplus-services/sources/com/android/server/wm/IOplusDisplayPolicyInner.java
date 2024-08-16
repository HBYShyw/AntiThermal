package com.android.server.wm;

import android.content.Context;
import android.os.Handler;
import com.android.internal.util.ScreenshotHelper;
import com.android.server.statusbar.StatusBarManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusDisplayPolicyInner {
    default Context getContext() {
        return null;
    }

    default WindowState getFocusedWindow() {
        return null;
    }

    default Handler getHandler() {
        return null;
    }

    default WindowState getNavigationBar() {
        return null;
    }

    default ScreenshotHelper getScreenshotHelper() {
        return null;
    }

    default Object getServiceAcquireLock() {
        return null;
    }

    default WindowState getStatusBar() {
        return null;
    }

    default StatusBarManagerInternal getStatusBarManagerInternal() {
        return null;
    }

    default WindowState getTopFullscreenOpaqueWindowState() {
        return null;
    }

    default WindowManagerService getWindowManagerService() {
        return null;
    }

    default void updateNavigationBarCanMove() {
    }
}
