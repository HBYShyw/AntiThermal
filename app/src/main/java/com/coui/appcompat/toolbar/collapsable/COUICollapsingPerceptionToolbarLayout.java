package com.coui.appcompat.toolbar.collapsable;

import android.content.Context;
import android.util.AttributeSet;
import com.coui.appcompat.toolbar.collapsable.COUICollapsingPerceptionToolbarLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.COUICollapsingToolbarLayout;
import d3.OnToolbarLayoutScrollStateListener;

/* loaded from: classes.dex */
public class COUICollapsingPerceptionToolbarLayout extends COUICollapsingToolbarLayout {

    /* renamed from: e, reason: collision with root package name */
    private AppBarLayout.OnOffsetChangedListener f8028e;

    /* renamed from: f, reason: collision with root package name */
    private OnToolbarLayoutScrollStateListener f8029f;

    public COUICollapsingPerceptionToolbarLayout(Context context) {
        super(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(AppBarLayout appBarLayout, int i10) {
        int i11;
        if (i10 == 0) {
            i11 = 0;
        } else {
            i11 = Math.abs(i10) >= appBarLayout.getTotalScrollRange() ? 1 : 2;
        }
        OnToolbarLayoutScrollStateListener onToolbarLayoutScrollStateListener = this.f8029f;
        if (onToolbarLayoutScrollStateListener != null) {
            onToolbarLayoutScrollStateListener.a(i10, appBarLayout.getTotalScrollRange(), i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.appbar.COUICollapsingToolbarLayout, com.google.android.material.appbar.CollapsingToolbarLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() instanceof AppBarLayout) {
            this.f8028e = new AppBarLayout.OnOffsetChangedListener() { // from class: d3.a
                @Override // com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener, com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
                public final void onOffsetChanged(AppBarLayout appBarLayout, int i10) {
                    COUICollapsingPerceptionToolbarLayout.this.b(appBarLayout, i10);
                }
            };
            ((AppBarLayout) getParent()).addOnOffsetChangedListener(this.f8028e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.appbar.COUICollapsingToolbarLayout, com.google.android.material.appbar.CollapsingToolbarLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getParent() instanceof AppBarLayout) {
            ((AppBarLayout) getParent()).removeOnOffsetChangedListener(this.f8028e);
        }
    }

    public void setOnToolbarLayoutScrollStateListener(OnToolbarLayoutScrollStateListener onToolbarLayoutScrollStateListener) {
        this.f8029f = onToolbarLayoutScrollStateListener;
    }

    public COUICollapsingPerceptionToolbarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public COUICollapsingPerceptionToolbarLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
