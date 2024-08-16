package com.oplus.screenshot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import com.oplus.content.OplusContext;
import com.oplus.feature.OplusDisableFeatures;
import com.oplus.util.OplusLog;
import com.oplus.view.OplusWindowUtils;
import java.util.List;

/* loaded from: classes.dex */
public final class OplusLongshotUtils {
    private static final boolean DBG = true;
    public static final Intent INTENT_HOME = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME");
    public static final String PACKAGE_EXSERVICEUI = "com.oplus.exserviceui";
    public static final String PACKAGE_GALLERY = "gallery3d";
    public static final String PACKAGE_SCREENSHOT = "com.oplus.screenshot";
    public static final String PACKAGE_SYSTEMUI = "com.android.systemui";
    private static final String TAG = "LongshotDump/OplusLongshotUtils";
    public static final String TAG_LONGSHOT = "Screenshot";
    public static final int VALUE_FIVE = 5;
    public static final int VALUE_FOUR = 4;
    public static final int VALUE_THREE = 3;
    public static final int VALUE_TWO = 2;
    public static final String VIEW_DECOR = "com.android.internal.policy.impl.PhoneWindow$DecorView";
    public static final String VIEW_NAVIGATIONBAR = "com.android.systemui.statusbar.phone.NavigationBarView";
    public static final String VIEW_STATUSBAR = "com.android.systemui.statusbar.phone.StatusBarWindowView";

    public static OplusScreenshotManager getScreenshotManager(Context context) {
        return (OplusScreenshotManager) context.getSystemService(OplusContext.SCREENSHOT_SERVICE);
    }

    public static boolean isDisabled(Context context) {
        return context.getPackageManager().hasSystemFeature(OplusDisableFeatures.SystemCenter.LONGSHOT);
    }

    public static boolean isInstalled(Context context) {
        StringBuilder sb;
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.oplus.screenshot", 4);
            result = true;
            sb = new StringBuilder();
        } catch (PackageManager.NameNotFoundException e) {
            sb = new StringBuilder();
        } catch (Throwable th) {
            OplusLog.w(true, TAG, "isInstalled : false");
            throw th;
        }
        OplusLog.w(true, TAG, sb.append("isInstalled : ").append(result).toString());
        return result;
    }

    public static boolean isHomeApp(Context context) {
        String packageName = context.getPackageName();
        List<ResolveInfo> homeList = context.getPackageManager().queryIntentActivities(INTENT_HOME, 65536);
        for (ResolveInfo ri : homeList) {
            if (packageName.equals(ri.activityInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExServiceUiApp(String owningPackage) {
        return OplusWindowUtils.isExServiceUiApp(owningPackage);
    }

    public static boolean isExServiceUiApp(Context context) {
        return isExServiceUiApp(context.getPackageName());
    }

    public static boolean isScreenshotApp(String owningPackage) {
        return OplusWindowUtils.isScreenshotApp(owningPackage);
    }

    public static boolean isScreenshotApp(Context context) {
        return isScreenshotApp(context.getPackageName());
    }

    public static boolean isSystemUiApp(String owningPackage) {
        return OplusWindowUtils.isSystemUiApp(owningPackage);
    }

    public static boolean isSystemUiApp(Context context) {
        return isSystemUiApp(context.getPackageName());
    }

    public static boolean isDecorView(Object view) {
        return view.getClass().getName().equals(VIEW_DECOR);
    }

    public static boolean isStatusBarView(Object view) {
        return view.getClass().getName().equals(VIEW_STATUSBAR);
    }

    public static boolean isNavigationBarView(Object view) {
        return view.getClass().getName().equals(VIEW_NAVIGATIONBAR);
    }

    public static boolean isStatusBar(int type) {
        return OplusWindowUtils.isStatusBar(type);
    }

    public static boolean isNavigationBar(int type) {
        return OplusWindowUtils.isNavigationBar(type);
    }

    public static boolean isSystemUiBar(int type, CharSequence title) {
        return OplusWindowUtils.isSystemUiBar(type, title);
    }

    public static boolean isTickerPanel(String owningPackage, CharSequence title) {
        if (isSystemUiApp(owningPackage)) {
            return "TickerPanel".equals(title);
        }
        return false;
    }

    public static boolean isSystemWindow(String owningPackage, CharSequence title, int type) {
        if (isExServiceUiApp(owningPackage)) {
            return true;
        }
        if (isSystemUiApp(owningPackage)) {
            return isSystemUiBar(type, title);
        }
        return false;
    }

    public static boolean isAllSystemWindow(String owningPackage, CharSequence title, int type) {
        if (isScreenshotApp(owningPackage)) {
            return true;
        }
        return isSystemWindow(owningPackage, title, type);
    }

    public static boolean isInputMethodWindow(int windowType, CharSequence title) {
        return OplusWindowUtils.isInputMethodWindow(windowType, title);
    }

    public static String getBaseClassNameOf(View view) {
        AccessibilityNodeInfo node = null;
        try {
            node = view.createAccessibilityNodeInfo();
        } catch (Exception e) {
        }
        if (node == null) {
            return null;
        }
        CharSequence className = node.getClassName();
        try {
            node.recycle();
        } catch (Exception e2) {
        }
        if (className == null) {
            return null;
        }
        return className.toString();
    }

    public static boolean isWebFromBaseName(String baseClassName) {
        if (baseClassName == null) {
            return false;
        }
        return baseClassName.contains("WebView") || baseClassName.contains("webkit");
    }

    public static boolean isWebContent(String className) {
        if (className == null || !className.startsWith("org.chromium.content.browser.") || !className.endsWith("ContentView")) {
            return false;
        }
        return true;
    }

    public static boolean canScrollVertically(View view) {
        try {
            boolean result = testScrollVertically(view);
            return result;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean canScrollVerticallyForward(OplusLongshotViewBase view) {
        return view.canLongScroll();
    }

    public static boolean canScrollVerticallyWithPadding(OplusLongshotViewBase view, int padding) {
        int offset = view.computeLongScrollOffset();
        int range = view.computeLongScrollRange() - view.computeLongScrollExtent();
        return range != 0 && offset < (range + (-1)) + padding;
    }

    public static boolean isFloatAssistant(String owningPackage) {
        return OplusWindowUtils.isFloatAssistant(owningPackage);
    }

    public static boolean isGallery(String owningPackage) {
        return OplusWindowUtils.isGallery(owningPackage);
    }

    private static boolean testScrollVertically(View view) {
        if (view.canScrollVertically(1) || view.canScrollVertically(-1)) {
            return true;
        }
        return false;
    }
}
