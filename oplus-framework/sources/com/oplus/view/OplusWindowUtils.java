package com.oplus.view;

import android.telephony.OplusTelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

/* loaded from: classes.dex */
public final class OplusWindowUtils {
    public static final String PACKAGE_ASSISTANTSCREEN = "com.coloros.assistantscreen";
    public static final String PACKAGE_EXSERVICEUI = "com.oplus.exserviceui";
    public static final String PACKAGE_FLOATASSISTANT = "com.coloros.floatassistant";

    @Deprecated
    public static final String PACKAGE_GALLERY = "gallery3d";
    public static final String PACKAGE_INCALL = "com.android.incallui";
    public static final String PACKAGE_SCREENSHOT = "com.oplus.screenshot";
    public static final String PACKAGE_SYSTEMUI = "com.android.systemui";
    public static final String PACKAGE_TALKBACK = "com.google.android.marvin.talkback";
    private static final String TITLE_EDGE_FLOATBAR = "ColorOSEdgeFloatBar";
    private static final String TITLE_EDGE_PANEL = "ColorOSEdgePanel";
    private static final String TITLE_NOTIFICATIONSHADE = "NotificationShade";
    private static final String TITLE_SHORTCUTS_PANEL = "ShortcutsPanel";
    private static final String[] TITLE_DIALOG_VOLUME = {"VolumeDialogImplEx", "OplusVolumeDialogImpl"};
    private static final String[] SYSTEMUI_BARS = {"TickerPanel"};
    public static final String PACKAGE_DIRECTUI = "com.coloros.directui";
    public static final String PACKAGE_DIRECTSERVICE = "com.coloros.colordirectservice";
    private static final String[] DIRECT_APPS = {PACKAGE_DIRECTUI, PACKAGE_DIRECTSERVICE};

    public static boolean isInputMethodWindow(int windowType, CharSequence title) {
        switch (windowType) {
            case OplusTelephonyManager.EVENT_REG_GET_REGION_NETLOCK_STATE_INFO /* 2011 */:
                return "InputMethod".equals(title);
            default:
                return false;
        }
    }

    public static boolean isStatusBar(int type) {
        return 2000 == type;
    }

    public static boolean isNavigationBar(int type) {
        return 2019 == type;
    }

    public static boolean isEdgeFloatBar(int type) {
        return 2314 == type;
    }

    public static boolean isSystemHeadsUp(WindowManager.LayoutParams attrs) {
        return (attrs.flags & 32) == 32;
    }

    public static boolean isSystemFloatBar(int type, CharSequence title) {
        if (isEdgeFloatBar(type)) {
            return true;
        }
        return false;
    }

    public static boolean isSystemUiBar(int type, CharSequence title) {
        if (isNavigationBar(type) || isStatusBar(type)) {
            return true;
        }
        if (!TextUtils.isEmpty(title)) {
            for (String name : SYSTEMUI_BARS) {
                if (name.equals(title)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isShortcutsPanel(CharSequence title) {
        return TITLE_SHORTCUTS_PANEL.equals(title);
    }

    public static boolean isVolumeDialog(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            for (String name : TITLE_DIALOG_VOLUME) {
                if (name.equals(title)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExServiceUiApp(String owningPackage) {
        return isPackage(owningPackage, "com.oplus.exserviceui");
    }

    public static boolean isScreenshotApp(String owningPackage) {
        return isPackage(owningPackage, "com.oplus.screenshot");
    }

    public static boolean isSystemUiApp(String owningPackage) {
        return isPackage(owningPackage, "com.android.systemui");
    }

    public static boolean isFloatAssistant(String owningPackage) {
        return isPackage(owningPackage, PACKAGE_FLOATASSISTANT);
    }

    public static boolean isGallery(String owningPackage) {
        return isPackage(owningPackage, "gallery3d");
    }

    public static boolean isDirectApp(String owningPackage) {
        for (String name : DIRECT_APPS) {
            if (name.equals(owningPackage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSystemInCallApp(String owningPackage) {
        return isPackage(owningPackage, PACKAGE_INCALL);
    }

    public static boolean isEdgeFloatBarTitle(CharSequence title) {
        return TITLE_EDGE_FLOATBAR.equals(title);
    }

    public static boolean isEdgePanelTitle(CharSequence title) {
        return TITLE_EDGE_PANEL.equals(title);
    }

    public static boolean isExpand(WindowManager.LayoutParams attrs) {
        return attrs.width == -1 && attrs.height == -1;
    }

    public static boolean isSystemWindow(WindowManager.LayoutParams attrs) {
        return attrs.type > 2000;
    }

    public static boolean isTalkBack(String owningPackage) {
        return isPackage(owningPackage, PACKAGE_TALKBACK);
    }

    public static boolean isAssistantScreen(String owningPackage) {
        return isPackage(owningPackage, PACKAGE_ASSISTANTSCREEN);
    }

    public static boolean isNotificationShade(CharSequence title) {
        return TITLE_NOTIFICATIONSHADE.equals(title);
    }

    private static boolean isPackage(String owningPackage, String packageName) {
        return (packageName == null || owningPackage == null || !packageName.equals(owningPackage)) ? false : true;
    }
}
