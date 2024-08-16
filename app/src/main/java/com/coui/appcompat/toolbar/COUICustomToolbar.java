package com.coui.appcompat.toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R$attr;
import com.coui.appcompat.toolbar.collapsable.COUICollapsingPerceptionToolbarLayout;
import d3.OnToolbarLayoutScrollStateListener;
import java.math.BigDecimal;

/* loaded from: classes.dex */
public abstract class COUICustomToolbar extends COUIToolbar {

    /* renamed from: p0, reason: collision with root package name */
    private View f7973p0;

    public COUICustomToolbar(Context context) {
        this(context, null);
    }

    protected abstract int getCustomResId();

    public View getCustomView() {
        return this.f7973p0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() instanceof COUICollapsingPerceptionToolbarLayout) {
            ((COUICollapsingPerceptionToolbarLayout) getParent()).setOnToolbarLayoutScrollStateListener(new OnToolbarLayoutScrollStateListener() { // from class: com.coui.appcompat.toolbar.a
                @Override // d3.OnToolbarLayoutScrollStateListener
                public final void a(int i10, int i11, int i12) {
                    COUICustomToolbar.this.q(i10, i11, i12);
                }
            });
        }
    }

    protected void p() {
        setCustomView(getCustomResId());
        if (getCustomView() == null) {
            return;
        }
        getCustomView().setVisibility(4);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void q(int i10, int i11, int i12) {
        if (getCustomView() == null) {
            return;
        }
        if (i12 == 0) {
            getCustomView().setVisibility(4);
            getCustomView().setAlpha(0.0f);
        } else {
            if (i12 == 1) {
                getCustomView().setVisibility(0);
                getCustomView().setAlpha(1.0f);
                return;
            }
            if (getCustomView().getVisibility() != 0) {
                getCustomView().setVisibility(0);
            }
            float floatValue = new BigDecimal(Math.abs(i10)).divide(new BigDecimal(i11), 2, 4).floatValue();
            if (getCustomView().getAlpha() != floatValue) {
                getCustomView().setAlpha(floatValue);
            }
        }
    }

    public void setCustomView(int i10) {
        if (i10 == 0) {
            return;
        }
        setCustomView(LayoutInflater.from(getContext()).inflate(i10, (ViewGroup) this, false));
    }

    public COUICustomToolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.toolbarStyle);
    }

    public void setCustomView(View view) {
        if (this.f7973p0 == null) {
            this.f7973p0 = view;
            addView(view);
            return;
        }
        throw new RuntimeException("Repeat calls are not allowed!!");
    }

    public COUICustomToolbar(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        p();
    }
}
