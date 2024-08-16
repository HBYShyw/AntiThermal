package com.oplus.powermanager.fuelgaue.base;

import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class TopMarginView {
    private List<WeakReference<View>> mViews;

    private void ensureList() {
        if (this.mViews == null) {
            this.mViews = new ArrayList();
        }
    }

    public static String getPropertyName() {
        return "topMargin";
    }

    public static int getViewTopMargin(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return 0;
        }
        return ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
    }

    public static void setViewTopMargin(View view, int i10) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        marginLayoutParams.topMargin = i10;
        view.setLayoutParams(marginLayoutParams);
    }

    public TopMarginView addView(View view) {
        ensureList();
        this.mViews.add(new WeakReference<>(view));
        return this;
    }

    public int getTopMargin() {
        List<WeakReference<View>> list = this.mViews;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        for (WeakReference<View> weakReference : this.mViews) {
            if (weakReference != null && weakReference.get() != null) {
                return getViewTopMargin(weakReference.get());
            }
        }
        return 0;
    }

    public void setTopMargin(int i10) {
        List<WeakReference<View>> list = this.mViews;
        if (list == null || list.isEmpty()) {
            return;
        }
        for (WeakReference<View> weakReference : this.mViews) {
            if (weakReference != null && weakReference.get() != null) {
                setViewTopMargin(weakReference.get(), i10);
            }
        }
    }
}
