package x9;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* compiled from: StatusBarUtil.java */
/* loaded from: classes2.dex */
public class c {
    public static int a() {
        try {
            Class<?> cls = Class.forName("com.oplus.os.OplusBuild");
            return ((Integer) cls.getDeclaredMethod("getOplusOsVERSION", new Class[0]).invoke(cls, new Object[0])).intValue();
        } catch (Exception e10) {
            Log.e("StatusBarUtil", "getRomVersionCode failed. error = " + e10.getMessage());
            return 0;
        }
    }

    public static View b(Context context) {
        View view = new View(context);
        view.setBackgroundColor(context.getColor(R.color.common_window_bg));
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, 0));
        return view;
    }

    public static void c(Activity activity) {
        View decorView;
        Window window = activity.getWindow();
        if (window == null || (decorView = window.getDecorView()) == null) {
            return;
        }
        window.setStatusBarColor(activity.getResources().getColor(R.color.oplus_status_bar_color));
        if (StatusBarUtil.isGestureNavigation(activity)) {
            window.setNavigationBarColor(0);
            decorView.setSystemUiVisibility(1536);
        } else {
            window.setNavigationBarColor(COUIContextUtil.a(activity, R.attr.couiColorBackgroundWithCard));
            decorView.setSystemUiVisibility(1024);
        }
        int systemUiVisibility = decorView.getSystemUiVisibility();
        int a10 = a();
        boolean z10 = activity.getResources().getBoolean(R.bool.list_status_white_enabled);
        if (a10 >= 6 || a10 == 0) {
            window.addFlags(Integer.MIN_VALUE);
            decorView.setSystemUiVisibility(COUIDarkModeUtil.a(activity) ? systemUiVisibility & (-8193) & (-17) : !z10 ? systemUiVisibility | 8192 : systemUiVisibility | 256);
        }
    }
}
