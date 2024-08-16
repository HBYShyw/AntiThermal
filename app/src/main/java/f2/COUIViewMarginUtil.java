package f2;

import android.view.View;
import android.view.ViewGroup;

/* compiled from: COUIViewMarginUtil.java */
/* renamed from: f2.i, reason: use source file name */
/* loaded from: classes.dex */
public class COUIViewMarginUtil {
    public static int a(View view, int i10) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                if (i10 == 0) {
                    return ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                }
                if (i10 == 1) {
                    return ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                }
                if (i10 == 2) {
                    return ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                }
                if (i10 != 3) {
                    return 0;
                }
                return ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            }
        }
        return 0;
    }

    public static void b(View view, int i10, int i11) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                if (i10 == 0) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = i11;
                } else if (i10 == 1) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = i11;
                } else if (i10 == 2) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = i11;
                } else if (i10 == 3) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = i11;
                }
                view.setLayoutParams(layoutParams);
            }
        }
    }
}
