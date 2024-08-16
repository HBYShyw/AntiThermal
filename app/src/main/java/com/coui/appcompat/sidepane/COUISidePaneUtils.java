package com.coui.appcompat.sidepane;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.support.bars.R$dimen;

/* compiled from: COUISidePaneUtils.java */
/* renamed from: com.coui.appcompat.sidepane.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUISidePaneUtils {
    public static boolean a(Activity activity) {
        return activity != null && activity.isInMultiWindowMode();
    }

    public static boolean b(Context context) {
        return context.getResources().getConfiguration().screenWidthDp >= 600 && context.getResources().getConfiguration().screenHeightDp >= 900;
    }

    public static boolean c(Activity activity) {
        return activity.getResources().getConfiguration().screenWidthDp >= 600 && (activity.getResources().getConfiguration().screenHeightDp >= 480 || a(activity));
    }

    public static void d(View view, Context context) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(context.getResources().getDimensionPixelOffset(R$dimen.coui_side_pane_layout_toolbar_margin_start));
        }
    }
}
