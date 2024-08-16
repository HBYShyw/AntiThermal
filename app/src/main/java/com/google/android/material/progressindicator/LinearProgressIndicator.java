package com.google.android.material.progressindicator;

import android.content.Context;
import android.util.AttributeSet;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$style;

/* loaded from: classes.dex */
public final class LinearProgressIndicator extends BaseProgressIndicator<LinearProgressIndicatorSpec> {

    /* renamed from: t, reason: collision with root package name */
    public static final int f9057t = R$style.Widget_MaterialComponents_LinearProgressIndicator;

    public LinearProgressIndicator(Context context) {
        this(context, null);
    }

    private void s() {
        setIndeterminateDrawable(IndeterminateDrawable.t(getContext(), (LinearProgressIndicatorSpec) this.f9035e));
        setProgressDrawable(DeterminateDrawable.v(getContext(), (LinearProgressIndicatorSpec) this.f9035e));
    }

    public int getIndeterminateAnimationType() {
        return ((LinearProgressIndicatorSpec) this.f9035e).f9058g;
    }

    public int getIndicatorDirection() {
        return ((LinearProgressIndicatorSpec) this.f9035e).f9059h;
    }

    @Override // com.google.android.material.progressindicator.BaseProgressIndicator
    public void o(int i10, boolean z10) {
        S s7 = this.f9035e;
        if (s7 != 0 && ((LinearProgressIndicatorSpec) s7).f9058g == 0 && isIndeterminate()) {
            return;
        }
        super.o(i10, z10);
    }

    @Override // android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        S s7 = this.f9035e;
        LinearProgressIndicatorSpec linearProgressIndicatorSpec = (LinearProgressIndicatorSpec) s7;
        boolean z11 = true;
        if (((LinearProgressIndicatorSpec) s7).f9059h != 1 && ((ViewCompat.x(this) != 1 || ((LinearProgressIndicatorSpec) this.f9035e).f9059h != 2) && (ViewCompat.x(this) != 0 || ((LinearProgressIndicatorSpec) this.f9035e).f9059h != 3))) {
            z11 = false;
        }
        linearProgressIndicatorSpec.f9060i = z11;
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        int paddingLeft = i10 - (getPaddingLeft() + getPaddingRight());
        int paddingTop = i11 - (getPaddingTop() + getPaddingBottom());
        IndeterminateDrawable<LinearProgressIndicatorSpec> indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable != null) {
            indeterminateDrawable.setBounds(0, 0, paddingLeft, paddingTop);
        }
        DeterminateDrawable<LinearProgressIndicatorSpec> progressDrawable = getProgressDrawable();
        if (progressDrawable != null) {
            progressDrawable.setBounds(0, 0, paddingLeft, paddingTop);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.BaseProgressIndicator
    /* renamed from: r, reason: merged with bridge method [inline-methods] */
    public LinearProgressIndicatorSpec i(Context context, AttributeSet attributeSet) {
        return new LinearProgressIndicatorSpec(context, attributeSet);
    }

    public void setIndeterminateAnimationType(int i10) {
        if (((LinearProgressIndicatorSpec) this.f9035e).f9058g == i10) {
            return;
        }
        if (q() && isIndeterminate()) {
            throw new IllegalStateException("Cannot change indeterminate animation type while the progress indicator is show in indeterminate mode.");
        }
        S s7 = this.f9035e;
        ((LinearProgressIndicatorSpec) s7).f9058g = i10;
        ((LinearProgressIndicatorSpec) s7).e();
        if (i10 == 0) {
            getIndeterminateDrawable().w(new LinearIndeterminateContiguousAnimatorDelegate((LinearProgressIndicatorSpec) this.f9035e));
        } else {
            getIndeterminateDrawable().w(new LinearIndeterminateDisjointAnimatorDelegate(getContext(), (LinearProgressIndicatorSpec) this.f9035e));
        }
        invalidate();
    }

    @Override // com.google.android.material.progressindicator.BaseProgressIndicator
    public void setIndicatorColor(int... iArr) {
        super.setIndicatorColor(iArr);
        ((LinearProgressIndicatorSpec) this.f9035e).e();
    }

    public void setIndicatorDirection(int i10) {
        S s7 = this.f9035e;
        ((LinearProgressIndicatorSpec) s7).f9059h = i10;
        LinearProgressIndicatorSpec linearProgressIndicatorSpec = (LinearProgressIndicatorSpec) s7;
        boolean z10 = true;
        if (i10 != 1 && ((ViewCompat.x(this) != 1 || ((LinearProgressIndicatorSpec) this.f9035e).f9059h != 2) && (ViewCompat.x(this) != 0 || i10 != 3))) {
            z10 = false;
        }
        linearProgressIndicatorSpec.f9060i = z10;
        invalidate();
    }

    @Override // com.google.android.material.progressindicator.BaseProgressIndicator
    public void setTrackCornerRadius(int i10) {
        super.setTrackCornerRadius(i10);
        ((LinearProgressIndicatorSpec) this.f9035e).e();
        invalidate();
    }

    public LinearProgressIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.linearProgressIndicatorStyle);
    }

    public LinearProgressIndicator(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10, f9057t);
        s();
    }
}
