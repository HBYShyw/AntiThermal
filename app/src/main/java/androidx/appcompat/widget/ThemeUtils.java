package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import androidx.appcompat.R$styleable;
import androidx.core.graphics.ColorUtils;

/* compiled from: ThemeUtils.java */
/* renamed from: androidx.appcompat.widget.c0, reason: use source file name */
/* loaded from: classes.dex */
public class ThemeUtils {

    /* renamed from: a, reason: collision with root package name */
    private static final ThreadLocal<TypedValue> f1179a = new ThreadLocal<>();

    /* renamed from: b, reason: collision with root package name */
    static final int[] f1180b = {-16842910};

    /* renamed from: c, reason: collision with root package name */
    static final int[] f1181c = {R.attr.state_focused};

    /* renamed from: d, reason: collision with root package name */
    static final int[] f1182d = {R.attr.state_activated};

    /* renamed from: e, reason: collision with root package name */
    static final int[] f1183e = {R.attr.state_pressed};

    /* renamed from: f, reason: collision with root package name */
    static final int[] f1184f = {16842912};

    /* renamed from: g, reason: collision with root package name */
    static final int[] f1185g = {R.attr.state_selected};

    /* renamed from: h, reason: collision with root package name */
    static final int[] f1186h = {-16842919, -16842908};

    /* renamed from: i, reason: collision with root package name */
    static final int[] f1187i = new int[0];

    /* renamed from: j, reason: collision with root package name */
    private static final int[] f1188j = new int[1];

    public static void a(View view, Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R$styleable.AppCompatTheme);
        try {
            if (!obtainStyledAttributes.hasValue(R$styleable.AppCompatTheme_windowActionBar)) {
                Log.e("ThemeUtils", "View " + view.getClass() + " is an AppCompat widget that can only be used with a Theme.AppCompat theme (or descendant).");
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static int b(Context context, int i10) {
        ColorStateList e10 = e(context, i10);
        if (e10 != null && e10.isStateful()) {
            return e10.getColorForState(f1180b, e10.getDefaultColor());
        }
        TypedValue f10 = f();
        context.getTheme().resolveAttribute(R.attr.disabledAlpha, f10, true);
        return d(context, i10, f10.getFloat());
    }

    public static int c(Context context, int i10) {
        int[] iArr = f1188j;
        iArr[0] = i10;
        TintTypedArray v7 = TintTypedArray.v(context, null, iArr);
        try {
            return v7.b(0, 0);
        } finally {
            v7.x();
        }
    }

    static int d(Context context, int i10, float f10) {
        return ColorUtils.n(c(context, i10), Math.round(Color.alpha(r0) * f10));
    }

    public static ColorStateList e(Context context, int i10) {
        int[] iArr = f1188j;
        iArr[0] = i10;
        TintTypedArray v7 = TintTypedArray.v(context, null, iArr);
        try {
            return v7.c(0);
        } finally {
            v7.x();
        }
    }

    private static TypedValue f() {
        ThreadLocal<TypedValue> threadLocal = f1179a;
        TypedValue typedValue = threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }
}
