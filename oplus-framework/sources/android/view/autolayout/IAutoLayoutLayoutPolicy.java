package android.view.autolayout;

import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public interface IAutoLayoutLayoutPolicy {
    public static final IAutoLayoutLayoutPolicy DEFAULT = new IAutoLayoutLayoutPolicy() { // from class: android.view.autolayout.IAutoLayoutLayoutPolicy.1
    };

    default int[] beforeLayout(View view, int l, int t, int r, int b) {
        return new int[]{l, t, r, b};
    }

    default void afterLayout(View view) {
    }

    default ViewGroup.LayoutParams setLayoutParams(ViewGroup.LayoutParams params) {
        return params;
    }
}
