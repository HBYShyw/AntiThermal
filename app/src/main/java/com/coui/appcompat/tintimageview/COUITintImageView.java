package com.coui.appcompat.tintimageview;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.TintTypedArray;
import b3.COUITintManager;

/* loaded from: classes.dex */
public class COUITintImageView extends AppCompatImageView {

    /* renamed from: i, reason: collision with root package name */
    private static final int[] f7927i = {R.attr.background, R.attr.src};

    /* renamed from: h, reason: collision with root package name */
    private final COUITintManager f7928h;

    public COUITintImageView(Context context) {
        this(context, null);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageResource(int i10) {
        setImageDrawable(this.f7928h.b(i10));
    }

    public COUITintImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUITintImageView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        TintTypedArray w10 = TintTypedArray.w(getContext(), attributeSet, f7927i, i10, 0);
        if (w10.t() > 0) {
            if (w10.s(0)) {
                setBackgroundDrawable(w10.g(0));
            }
            if (w10.s(1)) {
                setImageDrawable(w10.g(1));
            }
        }
        w10.x();
        this.f7928h = COUITintManager.a(context);
    }
}
