package com.oplus.view;

import android.content.Context;
import android.view.OplusBaseLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import com.oplus.util.OplusTypeCastingHelper;

/* loaded from: classes.dex */
public final class OplusWindowAttributesManager {
    public static final int BACK_GESTURE_EXCLUSION_NO_RESTRICTION = 1;
    public static final int BACK_GESTURE_RESTRICTION_DEFAULT = 0;
    public static final int BACK_SWITCH_APP_GESTURE_DISABLED = 2;
    public static final int DEFAULT_STATUS_BAR = 0;
    public static final int DISABLE_STATUS_BAR = 1;
    public static final int ENABLE_STATUS_BAR = 2;
    public static final int IGNORE_HOME_KEY = 2;
    public static final int IGNORE_HOME_MENU_KEY = 1;
    public static final int IGNORE_MENU_KEY = 3;
    private static final String SAFE_WINDOW_PERMISSION = "com.oplus.permission.safe.WINDOW";
    public static final int UNSET_ANY_KEY = 0;

    private OplusWindowAttributesManager() {
    }

    public static int getIgnoreHomeMenuKeyState(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        OplusBaseLayoutParams baseLp = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, lp);
        if (baseLp == null) {
            return 0;
        }
        return baseLp.ignoreHomeMenuKey;
    }

    public static void setIgnoreHomeMenuKeyState(Window window, int flag) {
        Context context = window.getContext();
        if (context == null) {
            return;
        }
        if (context.checkSelfPermission(SAFE_WINDOW_PERMISSION) != 0) {
            throw new SecurityException("current process does not have com.oplus.permission.safe.WINDOW permission to set the home or menu key state.");
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        OplusBaseLayoutParams baseLp = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, lp);
        if (baseLp != null) {
            baseLp.ignoreHomeMenuKey = flag;
            window.setAttributes(lp);
        }
    }

    public static int getStatusBarState(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        OplusBaseLayoutParams baseLp = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, lp);
        if (baseLp == null) {
            return 0;
        }
        return baseLp.isDisableStatusBar;
    }

    public static void setStatusBarState(Window window, int flag) {
        Context context = window.getContext();
        if (context == null) {
            return;
        }
        if (context.checkSelfPermission(SAFE_WINDOW_PERMISSION) != 0) {
            throw new SecurityException("current process does not have com.oplus.permission.safe.WINDOW permission to set the status bar state.");
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        OplusBaseLayoutParams baseLp = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, lp);
        if (baseLp != null) {
            baseLp.isDisableStatusBar = flag;
            window.setAttributes(lp);
        }
    }

    public static void setBackGestureRestriction(Window window, int restriction) {
        WindowManager.LayoutParams lp = window.getAttributes();
        OplusBaseLayoutParams baseLp = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, lp);
        if (baseLp != null) {
            baseLp.backGestureRestriction = restriction;
            window.setAttributes(lp);
        }
    }

    public static int getBackGestureRestriction(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        OplusBaseLayoutParams baseLp = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, lp);
        if (baseLp == null) {
            return 0;
        }
        return baseLp.backGestureRestriction;
    }
}
