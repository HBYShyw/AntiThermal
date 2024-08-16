package e2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/* compiled from: COUIOrientationUtil.java */
/* renamed from: e2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIOrientationUtil {
    public static Point a(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        return point;
    }

    public static boolean b(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context).isInMultiWindowMode();
        }
        return false;
    }
}
