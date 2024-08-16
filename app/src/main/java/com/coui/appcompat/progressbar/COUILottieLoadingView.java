package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.oplus.anim.EffectiveAnimationView;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$string;
import com.support.appcompat.R$styleable;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUILottieLoadingView extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    private final EffectiveAnimationView f7221e;

    public COUILottieLoadingView(Context context) {
        this(context, null);
    }

    public EffectiveAnimationView getLoadingView() {
        return this.f7221e;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EffectiveAnimationView effectiveAnimationView = this.f7221e;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.w();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EffectiveAnimationView effectiveAnimationView = this.f7221e;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.t();
        }
    }

    public COUILottieLoadingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiLottieLoadingViewStyle);
    }

    public COUILottieLoadingView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUILottieLoadingView, i10, 0);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUILottieLoadingView_couiLottieLoadingViewWidth, getResources().getDimensionPixelOffset(R$dimen.coui_lottie_loading_view_large_width));
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUILottieLoadingView_couiLottieLoadingViewHeight, getResources().getDimensionPixelOffset(R$dimen.coui_lottie_loading_view_large_height));
        String string = obtainStyledAttributes.getString(R$styleable.COUILottieLoadingView_couiLottieLoadingJsonName);
        string = string == null ? getResources().getString(R$string.coui_lottie_loading_large_json) : string;
        obtainStyledAttributes.recycle();
        EffectiveAnimationView effectiveAnimationView = new EffectiveAnimationView(context);
        this.f7221e = effectiveAnimationView;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize2);
        layoutParams.gravity = 17;
        effectiveAnimationView.setLayoutParams(layoutParams);
        effectiveAnimationView.setRepeatCount(-1);
        effectiveAnimationView.setAnimation(string);
        addView(effectiveAnimationView);
    }
}
