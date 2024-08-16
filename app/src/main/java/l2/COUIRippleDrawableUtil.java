package l2;

import android.view.View;

/* compiled from: COUIRippleDrawableUtil.java */
/* renamed from: l2.c, reason: use source file name */
/* loaded from: classes.dex */
public class COUIRippleDrawableUtil {
    public static void a(View view, int i10) {
        if (view == null) {
            return;
        }
        view.setBackground(new COUIPressRippleDrawable(view.getContext(), i10));
    }
}
