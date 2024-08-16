package androidx.transition;

import android.animation.ObjectAnimator;
import android.animation.TypeConverter;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Property;

/* compiled from: ObjectAnimatorUtils.java */
/* renamed from: androidx.transition.l, reason: use source file name */
/* loaded from: classes.dex */
class ObjectAnimatorUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> ObjectAnimator a(T t7, Property<T, PointF> property, Path path) {
        return ObjectAnimator.ofObject(t7, property, (TypeConverter) null, path);
    }
}
