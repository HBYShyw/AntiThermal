package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.CheckedTextView;

/* compiled from: CheckedTextViewCompat.java */
/* renamed from: androidx.core.widget.b, reason: use source file name */
/* loaded from: classes.dex */
public final class CheckedTextViewCompat {

    /* compiled from: CheckedTextViewCompat.java */
    /* renamed from: androidx.core.widget.b$a */
    /* loaded from: classes.dex */
    private static class a {
        static Drawable a(CheckedTextView checkedTextView) {
            return checkedTextView.getCheckMarkDrawable();
        }
    }

    /* compiled from: CheckedTextViewCompat.java */
    /* renamed from: androidx.core.widget.b$b */
    /* loaded from: classes.dex */
    private static class b {
        static void a(CheckedTextView checkedTextView, ColorStateList colorStateList) {
            checkedTextView.setCheckMarkTintList(colorStateList);
        }

        static void b(CheckedTextView checkedTextView, PorterDuff.Mode mode) {
            checkedTextView.setCheckMarkTintMode(mode);
        }
    }

    public static Drawable a(CheckedTextView checkedTextView) {
        return a.a(checkedTextView);
    }

    public static void b(CheckedTextView checkedTextView, ColorStateList colorStateList) {
        b.a(checkedTextView, colorStateList);
    }

    public static void c(CheckedTextView checkedTextView, PorterDuff.Mode mode) {
        b.b(checkedTextView, mode);
    }
}
