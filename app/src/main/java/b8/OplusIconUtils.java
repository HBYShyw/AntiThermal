package b8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.oplus.battery.R;

/* compiled from: OplusIconUtils.java */
/* renamed from: b8.a, reason: use source file name */
/* loaded from: classes.dex */
public final class OplusIconUtils {
    public static Bitmap a(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            intrinsicWidth = 108;
            intrinsicHeight = 108;
        }
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public static Drawable b(Context context, Drawable drawable) {
        int dimension = (int) context.getResources().getDimension(R.dimen.app_icon_size_in_list);
        return c(context, drawable, dimension, dimension);
    }

    public static Drawable c(Context context, Drawable drawable, int i10, int i11) {
        int i12;
        int i13;
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            i12 = 108;
            i13 = 108;
        } else {
            i12 = intrinsicWidth;
            i13 = intrinsicHeight;
        }
        Bitmap a10 = a(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(i10 / i12, i11 / i13);
        return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(a10, 0, 0, i12, i13, matrix, true));
    }
}
