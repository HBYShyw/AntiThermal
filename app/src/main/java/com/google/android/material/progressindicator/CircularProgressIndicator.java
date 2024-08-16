package com.google.android.material.progressindicator;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.material.R$attr;
import com.google.android.material.R$style;

/* loaded from: classes.dex */
public final class CircularProgressIndicator extends BaseProgressIndicator<CircularProgressIndicatorSpec> {

    /* renamed from: t, reason: collision with root package name */
    public static final int f9053t = R$style.Widget_MaterialComponents_CircularProgressIndicator;

    public CircularProgressIndicator(Context context) {
        this(context, null);
    }

    private void s() {
        setIndeterminateDrawable(IndeterminateDrawable.s(getContext(), (CircularProgressIndicatorSpec) this.f9035e));
        setProgressDrawable(DeterminateDrawable.u(getContext(), (CircularProgressIndicatorSpec) this.f9035e));
    }

    public int getIndicatorDirection() {
        return ((CircularProgressIndicatorSpec) this.f9035e).f9056i;
    }

    public int getIndicatorInset() {
        return ((CircularProgressIndicatorSpec) this.f9035e).f9055h;
    }

    public int getIndicatorSize() {
        return ((CircularProgressIndicatorSpec) this.f9035e).f9054g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.BaseProgressIndicator
    /* renamed from: r, reason: merged with bridge method [inline-methods] */
    public CircularProgressIndicatorSpec i(Context context, AttributeSet attributeSet) {
        return new CircularProgressIndicatorSpec(context, attributeSet);
    }

    public void setIndicatorDirection(int i10) {
        ((CircularProgressIndicatorSpec) this.f9035e).f9056i = i10;
        invalidate();
    }

    public void setIndicatorInset(int i10) {
        S s7 = this.f9035e;
        if (((CircularProgressIndicatorSpec) s7).f9055h != i10) {
            ((CircularProgressIndicatorSpec) s7).f9055h = i10;
            invalidate();
        }
    }

    public void setIndicatorSize(int i10) {
        int max = Math.max(i10, getTrackThickness() * 2);
        S s7 = this.f9035e;
        if (((CircularProgressIndicatorSpec) s7).f9054g != max) {
            ((CircularProgressIndicatorSpec) s7).f9054g = max;
            ((CircularProgressIndicatorSpec) s7).e();
            invalidate();
        }
    }

    @Override // com.google.android.material.progressindicator.BaseProgressIndicator
    public void setTrackThickness(int i10) {
        super.setTrackThickness(i10);
        ((CircularProgressIndicatorSpec) this.f9035e).e();
    }

    public CircularProgressIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.circularProgressIndicatorStyle);
    }

    public CircularProgressIndicator(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10, f9053t);
        s();
    }
}
