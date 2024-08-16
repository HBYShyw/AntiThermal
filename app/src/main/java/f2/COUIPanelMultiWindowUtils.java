package f2;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowInsets;
import com.support.panel.R$dimen;
import e2.COUIOrientationUtil;
import h3.FollowHandManager;
import h3.UIUtil;

/* compiled from: COUIPanelMultiWindowUtils.java */
/* renamed from: f2.f, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPanelMultiWindowUtils {
    public static Activity a(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public static int b(Context context, int i10) {
        return Double.valueOf((context.getResources().getConfiguration().densityDpi * i10) + 0.5d).intValue();
    }

    public static int c(Activity activity, Configuration configuration) {
        Rect e10;
        int i10 = (!q(activity) || (e10 = e(activity)) == null) ? 0 : e10.bottom - e10.top;
        return i10 == 0 ? j(activity, configuration) : i10;
    }

    public static int d(Activity activity, Configuration configuration, WindowInsets windowInsets) {
        int i10;
        if (q(activity)) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int dimensionPixelOffset = activity.getResources().getDimensionPixelOffset(R$dimen.coui_panel_min_padding_top);
            if (n(windowInsets, activity) == 0) {
                dimensionPixelOffset = activity.getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_margin_vertical_without_status_bar);
            }
            i10 = (displayMetrics.heightPixels - windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom) - dimensionPixelOffset;
        } else {
            i10 = 0;
        }
        return i10 == 0 ? k(activity, configuration, windowInsets) : i10;
    }

    public static Rect e(Activity activity) {
        if (activity == null) {
            return null;
        }
        View decorView = activity.getWindow().getDecorView();
        Rect rect = new Rect();
        decorView.getGlobalVisibleRect(rect);
        return rect;
    }

    public static int f(Context context, Configuration configuration) {
        if (context == null || configuration == null) {
            return 0;
        }
        int i10 = configuration.screenWidthDp;
        boolean z10 = (configuration.screenLayout & 15) == 1;
        boolean z11 = configuration.orientation == 2;
        if (i10 < 600.0f && (z10 || !z11)) {
            return 0;
        }
        if (m(context) == 0) {
            return context.createConfigurationContext(configuration).getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_margin_vertical_without_status_bar);
        }
        return context.createConfigurationContext(configuration).getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_margin_bottom_default);
    }

    public static int g(Context context, Configuration configuration, WindowInsets windowInsets) {
        if (context == null || configuration == null) {
            return 0;
        }
        int i10 = configuration.screenWidthDp;
        boolean z10 = (configuration.screenLayout & 15) == 1;
        boolean z11 = configuration.orientation == 2;
        if (i10 < 600.0f && (z10 || !z11)) {
            return 0;
        }
        if ((context.getResources().getConfiguration().screenLayout & 48) == 32) {
            return context.createConfigurationContext(configuration).getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_margin_bottom_smallland_default);
        }
        int dimensionPixelOffset = context.createConfigurationContext(configuration).getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_margin_bottom_default);
        int i11 = windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        return Math.max(0, (FollowHandManager.m(context) && COUIOrientationUtil.b(a(context)) && !v(context) && u(context)) ? Math.max(dimensionPixelOffset, i11) : dimensionPixelOffset - i11);
    }

    public static int h(Context context, Configuration configuration) {
        int j10;
        int f10;
        Activity a10 = a(context);
        if (configuration == null) {
            configuration = context.getResources().getConfiguration();
        }
        if (a10 != null) {
            j10 = c(a10, configuration);
            f10 = f(context, configuration);
        } else {
            j10 = j(context, configuration);
            f10 = f(context, configuration);
        }
        return Math.min(j10 - f10, l(context, context.getResources().getDimensionPixelOffset(R$dimen.coui_panel_max_height)));
    }

    public static int i(Context context, Configuration configuration, WindowInsets windowInsets) {
        int k10;
        int g6;
        Activity a10 = a(context);
        if (configuration == null) {
            configuration = context.getResources().getConfiguration();
        }
        if (a10 != null) {
            k10 = d(a10, configuration, windowInsets);
            g6 = g(context, configuration, windowInsets);
        } else {
            k10 = k(context, configuration, windowInsets);
            g6 = g(context, configuration, windowInsets);
        }
        return Math.min(k10 - g6, l(context, context.getResources().getDimensionPixelOffset(R$dimen.coui_panel_max_height)));
    }

    public static int j(Context context, Configuration configuration) {
        int i10 = 0;
        if (context == null) {
            return 0;
        }
        if (configuration == null) {
            configuration = context.getResources().getConfiguration();
        }
        int c10 = UIUtil.c(context);
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.coui_panel_min_padding_top);
        if (m(context) == 0) {
            dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_margin_vertical_without_status_bar);
        }
        boolean b10 = COUINavigationBarUtil.b(context);
        boolean z10 = ((configuration.screenLayout & 15) == 2) && (configuration.orientation == 2);
        if (b10) {
            if (((((float) configuration.screenWidthDp) >= 600.0f) || s(configuration)) && v(context) && !z10) {
                i10 = COUINavigationBarUtil.a(context);
            }
        }
        return (c10 - dimensionPixelOffset) - i10;
    }

    public static int k(Context context, Configuration configuration, WindowInsets windowInsets) {
        int i10 = 0;
        if (context == null) {
            return 0;
        }
        if (configuration == null) {
            configuration = context.getResources().getConfiguration();
        }
        int c10 = UIUtil.c(context);
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.coui_panel_min_padding_top);
        if (n(windowInsets, context) == 0) {
            dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_margin_vertical_without_status_bar);
        }
        boolean b10 = COUINavigationBarUtil.b(context);
        boolean z10 = ((configuration.screenLayout & 15) == 2) && (configuration.orientation == 2);
        if (b10) {
            if (((((float) configuration.screenWidthDp) >= 600.0f) || s(configuration)) && v(context) && !z10) {
                i10 = COUINavigationBarUtil.a(context);
            }
        }
        return (c10 - dimensionPixelOffset) - i10;
    }

    public static int l(Context context, int i10) {
        return Math.max(i10, b(context, context.getResources().getConfiguration().screenHeightDp - 40));
    }

    public static int m(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static int n(WindowInsets windowInsets, Context context) {
        return o(windowInsets);
    }

    public static int o(WindowInsets windowInsets) {
        return Math.abs(windowInsets.getInsets(WindowInsets.Type.statusBars()).bottom - windowInsets.getInsets(WindowInsets.Type.statusBars()).top);
    }

    public static boolean p(Activity activity) {
        if (activity == null) {
            return true;
        }
        int[] iArr = new int[2];
        activity.getWindow().getDecorView().getLocationOnScreen(iArr);
        return iArr[1] <= m(activity);
    }

    public static boolean q(Activity activity) {
        return activity != null && activity.isInMultiWindowMode();
    }

    public static boolean r(Context context, Configuration configuration) {
        if (configuration == null) {
            configuration = context.getResources().getConfiguration();
        }
        return ((float) configuration.screenHeightDp) > 809.0f;
    }

    public static boolean s(Configuration configuration) {
        return configuration.orientation == 1;
    }

    public static boolean t(Context context, Configuration configuration) {
        if (configuration == null) {
            configuration = context.getResources().getConfiguration();
        }
        return ((float) configuration.screenWidthDp) < 600.0f;
    }

    public static boolean u(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "enable_launcher_taskbar", 0) == 1;
    }

    public static boolean v(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "hide_navigationbar_enable", 0) != 3;
    }
}
