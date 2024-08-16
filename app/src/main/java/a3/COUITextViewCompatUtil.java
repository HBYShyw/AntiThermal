package a3;

import android.widget.TextView;
import z2.COUIChangeTextUtil;

/* compiled from: COUITextViewCompatUtil.java */
/* renamed from: a3.c, reason: use source file name */
/* loaded from: classes.dex */
public class COUITextViewCompatUtil {
    public static void a(TextView textView) {
        b(textView);
        COUIChangeTextUtil.c(textView, 4);
    }

    public static void b(TextView textView) {
        if (textView == null) {
            return;
        }
        textView.setBackground(new COUITextPressRippleDrawable(textView.getContext()));
    }
}
