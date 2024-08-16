package com.coui.appcompat.tablayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;
import com.support.bars.R$styleable;

/* loaded from: classes.dex */
public final class COUITabItem extends View {

    /* renamed from: e, reason: collision with root package name */
    final CharSequence f7785e;

    /* renamed from: f, reason: collision with root package name */
    final Drawable f7786f;

    /* renamed from: g, reason: collision with root package name */
    final int f7787g;

    public COUITabItem(Context context) {
        this(context, null);
    }

    public COUITabItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TintTypedArray v7 = TintTypedArray.v(context, attributeSet, R$styleable.COUITabItem);
        this.f7785e = v7.p(R$styleable.COUITabItem_android_text);
        this.f7786f = v7.g(R$styleable.COUITabItem_android_icon);
        this.f7787g = v7.n(R$styleable.COUITabItem_android_layout, 0);
        v7.x();
    }
}
