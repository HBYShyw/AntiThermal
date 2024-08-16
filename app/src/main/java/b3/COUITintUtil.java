package b3;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.DrawableCompat;

/* compiled from: COUITintUtil.java */
/* renamed from: b3.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUITintUtil {
    public static Bitmap a(Bitmap bitmap, int i10) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(i10, PorterDuff.Mode.SRC_IN));
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        createBitmap.setDensity(bitmap.getDensity());
        new Canvas(createBitmap).drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static Drawable b(Drawable drawable, int i10) {
        Drawable l10 = DrawableCompat.l(drawable);
        DrawableCompat.h(l10.mutate(), i10);
        return l10;
    }

    public static Drawable c(Drawable drawable, ColorStateList colorStateList) {
        Drawable l10 = DrawableCompat.l(drawable);
        DrawableCompat.i(l10.mutate(), colorStateList);
        return l10;
    }
}
