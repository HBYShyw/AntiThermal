package o3;

import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.FrameLayout;

/* compiled from: Util.java */
/* loaded from: classes.dex */
public abstract class a {
    public static final FrameLayout.LayoutParams a(int i10, int i11) {
        return new FrameLayout.LayoutParams(i10, i11);
    }

    public static final FrameLayout.LayoutParams b() {
        return a(-1, -1);
    }

    public static final FrameLayout.LayoutParams c() {
        return a(-1, -2);
    }

    public static final int d(float f10, Resources resources) {
        return (int) TypedValue.applyDimension(1, f10, resources.getDisplayMetrics());
    }
}
