package h3;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.support.appcompat.R$dimen;

/* compiled from: UIUtil.java */
/* renamed from: h3.b, reason: use source file name */
/* loaded from: classes.dex */
public class UIUtil {
    public static int a(MotionEvent motionEvent, int i10) {
        return Math.min(Math.max(0, i10), motionEvent.getPointerCount() - 1);
    }

    public static int b(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int c(Context context) {
        return d(context).y;
    }

    public static Point d(Context context) {
        WindowManager windowManager;
        Display defaultDisplay;
        Point point = new Point();
        if (context != null && (windowManager = (WindowManager) context.getSystemService("window")) != null && (defaultDisplay = windowManager.getDefaultDisplay()) != null) {
            defaultDisplay.getRealSize(point);
        }
        return point;
    }

    public static int e(Context context) {
        return d(context).x;
    }

    public static int f(Context context) {
        int identifier = context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            try {
                return context.getApplicationContext().getResources().getDimensionPixelSize(identifier);
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        }
        return -1;
    }

    public static void g(View view, boolean z10) {
        if (view == null) {
            return;
        }
        view.forceHasOverlappingRendering(z10);
    }

    public static void h(View view, int i10) {
        if (view == null) {
            return;
        }
        view.setOutlineAmbientShadowColor(i10);
    }

    public static void i(View view, int i10) {
        if (view == null) {
            return;
        }
        view.setOutlineSpotShadowColor(i10);
    }

    public static void j(View view, int i10, int i11) {
        k(view, i10, i11, view.getResources().getDimensionPixelOffset(R$dimen.support_shadow_size_level_for_lowerP));
    }

    public static void k(View view, int i10, int i11, int i12) {
        if (view == null) {
            return;
        }
        view.setOutlineSpotShadowColor(i11);
        view.setElevation(i10);
    }
}
