package com.coui.appcompat.tips;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import com.coui.appcompat.cardview.COUICardView;
import com.support.control.R$color;
import com.support.control.R$dimen;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public abstract class COUICustomTopTips extends COUICardView {

    /* renamed from: n, reason: collision with root package name */
    private View f7929n;

    /* renamed from: o, reason: collision with root package name */
    private AnimatorSet f7930o;

    /* renamed from: p, reason: collision with root package name */
    private AnimatorSet f7931p;

    /* renamed from: q, reason: collision with root package name */
    private Animator.AnimatorListener f7932q;

    /* renamed from: r, reason: collision with root package name */
    private Animator.AnimatorListener f7933r;

    public COUICustomTopTips(Context context) {
        this(context, null);
    }

    protected void c() {
        COUIDarkModeUtil.b(this, false);
        setContentView(getContentViewId());
        setRadius(getContext().getResources().getDimensionPixelSize(R$dimen.coui_toptips_view_radius));
        setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.c(getContext(), R$color.coui_toptips_background)));
    }

    public AnimatorSet getAnimatorSetDismiss() {
        return this.f7931p;
    }

    public AnimatorSet getAnimatorSetShow() {
        return this.f7930o;
    }

    public View getContentView() {
        return this.f7929n;
    }

    public abstract int getContentViewId();

    public void setAnimatorDismissListener(Animator.AnimatorListener animatorListener) {
        this.f7933r = animatorListener;
    }

    public void setAnimatorSetDismiss(AnimatorSet animatorSet) {
        this.f7931p = animatorSet;
    }

    public void setAnimatorSetShow(AnimatorSet animatorSet) {
        this.f7930o = animatorSet;
    }

    public void setAnimatorShowListener(Animator.AnimatorListener animatorListener) {
        this.f7932q = animatorListener;
    }

    public void setContentView(View view) {
        if (this.f7929n == null) {
            this.f7929n = view;
            addView(view);
            return;
        }
        throw new RuntimeException("Repeat calls are not allowed!!");
    }

    public COUICustomTopTips(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICustomTopTips(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        c();
    }

    public void setContentView(int i10) {
        if (i10 == 0) {
            return;
        }
        setContentView(LayoutInflater.from(getContext()).inflate(i10, (ViewGroup) this, false));
    }
}
