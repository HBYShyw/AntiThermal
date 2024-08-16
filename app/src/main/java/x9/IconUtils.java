package x9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.oplus.battery.R;

/* compiled from: IconUtils.java */
/* renamed from: x9.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class IconUtils {
    public static Bitmap a(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    public static Drawable b(Context context, Drawable drawable) {
        int dimension = (int) context.getResources().getDimension(R.dimen.app_icon_size_in_list);
        try {
            return c(context, drawable, dimension, dimension);
        } catch (Exception e10) {
            Log.e("OplusIconUtils", "zoomDrawable error.");
            e10.printStackTrace();
            return drawable;
        }
    }

    private static Drawable c(Context context, Drawable drawable, int i10, int i11) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap a10 = a(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(i10 / intrinsicWidth, i11 / intrinsicHeight);
        return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(a10, 0, 0, intrinsicWidth, intrinsicHeight, matrix, true));
    }
}
