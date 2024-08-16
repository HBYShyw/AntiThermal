package com.google.android.material.transformation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import java.util.List;
import v3.ExpandableWidget;

@Deprecated
/* loaded from: classes.dex */
public abstract class ExpandableBehavior extends CoordinatorLayout.Behavior<View> {

    /* renamed from: a, reason: collision with root package name */
    private int f9531a;

    /* loaded from: classes.dex */
    class a implements ViewTreeObserver.OnPreDrawListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f9532e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f9533f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ ExpandableWidget f9534g;

        a(View view, int i10, ExpandableWidget expandableWidget) {
            this.f9532e = view;
            this.f9533f = i10;
            this.f9534g = expandableWidget;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            this.f9532e.getViewTreeObserver().removeOnPreDrawListener(this);
            if (ExpandableBehavior.this.f9531a == this.f9533f) {
                ExpandableBehavior expandableBehavior = ExpandableBehavior.this;
                ExpandableWidget expandableWidget = this.f9534g;
                expandableBehavior.g((View) expandableWidget, this.f9532e, expandableWidget.a(), false);
            }
            return false;
        }
    }

    public ExpandableBehavior() {
        this.f9531a = 0;
    }

    private boolean e(boolean z10) {
        if (!z10) {
            return this.f9531a == 1;
        }
        int i10 = this.f9531a;
        return i10 == 0 || i10 == 2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected ExpandableWidget f(CoordinatorLayout coordinatorLayout, View view) {
        List<View> v7 = coordinatorLayout.v(view);
        int size = v7.size();
        for (int i10 = 0; i10 < size; i10++) {
            View view2 = v7.get(i10);
            if (layoutDependsOn(coordinatorLayout, view, view2)) {
                return (ExpandableWidget) view2;
            }
        }
        return null;
    }

    protected abstract boolean g(View view, View view2, boolean z10, boolean z11);

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public abstract boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
        ExpandableWidget expandableWidget = (ExpandableWidget) view2;
        if (!e(expandableWidget.a())) {
            return false;
        }
        this.f9531a = expandableWidget.a() ? 1 : 2;
        return g((View) expandableWidget, view, expandableWidget.a(), true);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i10) {
        ExpandableWidget f10;
        if (ViewCompat.Q(view) || (f10 = f(coordinatorLayout, view)) == null || !e(f10.a())) {
            return false;
        }
        int i11 = f10.a() ? 1 : 2;
        this.f9531a = i11;
        view.getViewTreeObserver().addOnPreDrawListener(new a(view, i11, f10));
        return false;
    }

    public ExpandableBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f9531a = 0;
    }
}
