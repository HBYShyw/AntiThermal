package f2;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/* compiled from: COUINavigationBarUtil.java */
/* renamed from: f2.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUINavigationBarUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final Uri f11306a = Settings.Secure.getUriFor("nav_bar_immersive");

    public static int a(Context context) {
        if (context == null) {
            return 0;
        }
        Resources resources = context.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics2);
        float f10 = displayMetrics2.density;
        float f11 = displayMetrics.density;
        if (f10 == f11) {
            return dimensionPixelSize;
        }
        return (int) ((dimensionPixelSize * (f10 / f11)) + 0.5f);
    }

    public static boolean b(Context context) {
        if (!c(context)) {
            return false;
        }
        Settings.Secure.getInt(context.getContentResolver(), "hide_navigationbar_enable", 0);
        Settings.Secure.getInt(context.getContentResolver(), "manual_hide_navigationbar", 0);
        return true;
    }

    public static boolean c(Context context) {
        boolean z10 = false;
        if (context == null) {
            return false;
        }
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        boolean z11 = identifier > 0 ? resources.getBoolean(identifier) : false;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str = (String) cls.getMethod("get", String.class).invoke(cls, "qemu.hw.mainkeys");
            if (!"1".equals(str)) {
                z10 = "0".equals(str) ? true : z11;
            }
            return z10;
        } catch (Exception e10) {
            Log.d("NavigationBarUtils", "fail to get navigation bar status message is " + e10.getMessage());
            return z11;
        }
    }
}
