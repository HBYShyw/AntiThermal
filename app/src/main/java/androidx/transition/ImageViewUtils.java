package androidx.transition;

import android.graphics.Matrix;
import android.widget.ImageView;

/* compiled from: ImageViewUtils.java */
/* renamed from: androidx.transition.j, reason: use source file name */
/* loaded from: classes.dex */
class ImageViewUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ImageView imageView, Matrix matrix) {
        imageView.animateTransform(matrix);
    }
}
