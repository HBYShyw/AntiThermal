package c;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.core.content.ContextCompat;

/* compiled from: AppCompatResources.java */
@SuppressLint({"RestrictedAPI"})
/* renamed from: c.a, reason: use source file name */
/* loaded from: classes.dex */
public final class AppCompatResources {
    public static ColorStateList a(Context context, int i10) {
        return ContextCompat.d(context, i10);
    }

    public static Drawable b(Context context, int i10) {
        return ResourceManagerInternal.g().i(context, i10);
    }
}
