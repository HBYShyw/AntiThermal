package com.coui.appcompat.theme;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

/* compiled from: COUIThemeUtils.java */
/* renamed from: com.coui.appcompat.theme.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIThemeUtils {

    /* renamed from: a, reason: collision with root package name */
    private static final ThreadLocal<TypedValue> f7917a = new ThreadLocal<>();

    /* renamed from: b, reason: collision with root package name */
    public static final int[] f7918b = {-16842910};

    /* renamed from: c, reason: collision with root package name */
    public static final int[] f7919c = {R.attr.state_focused};

    /* renamed from: d, reason: collision with root package name */
    public static final int[] f7920d = {R.attr.state_activated};

    /* renamed from: e, reason: collision with root package name */
    public static final int[] f7921e = {R.attr.state_pressed};

    /* renamed from: f, reason: collision with root package name */
    public static final int[] f7922f = {16842912};

    /* renamed from: g, reason: collision with root package name */
    public static final int[] f7923g = {R.attr.state_selected};

    /* renamed from: h, reason: collision with root package name */
    public static final int[] f7924h = {-16842919, -16842908};

    /* renamed from: i, reason: collision with root package name */
    static final int[] f7925i = new int[0];

    /* renamed from: j, reason: collision with root package name */
    private static final int[] f7926j = new int[1];

    public static int a(Context context, int i10) {
        int[] iArr = f7926j;
        iArr[0] = i10;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes((AttributeSet) null, iArr);
        try {
            return obtainStyledAttributes.getColor(0, 0);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }
}
