package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.widget.ImageView;

/* compiled from: ImageViewCompat.java */
/* renamed from: androidx.core.widget.g, reason: use source file name */
/* loaded from: classes.dex */
public class ImageViewCompat {

    /* compiled from: ImageViewCompat.java */
    /* renamed from: androidx.core.widget.g$a */
    /* loaded from: classes.dex */
    static class a {
        static ColorStateList a(ImageView imageView) {
            return imageView.getImageTintList();
        }

        static PorterDuff.Mode b(ImageView imageView) {
            return imageView.getImageTintMode();
        }

        static void c(ImageView imageView, ColorStateList colorStateList) {
            imageView.setImageTintList(colorStateList);
        }

        static void d(ImageView imageView, PorterDuff.Mode mode) {
            imageView.setImageTintMode(mode);
        }
    }

    public static ColorStateList a(ImageView imageView) {
        return a.a(imageView);
    }

    public static PorterDuff.Mode b(ImageView imageView) {
        return a.b(imageView);
    }

    public static void c(ImageView imageView, ColorStateList colorStateList) {
        a.c(imageView, colorStateList);
    }

    public static void d(ImageView imageView, PorterDuff.Mode mode) {
        a.d(imageView, mode);
    }
}
