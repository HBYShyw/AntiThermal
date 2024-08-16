package com.coui.appcompat.lifecycle;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.h;
import com.coui.appcompat.sidepane.COUISidePaneLayout;
import com.coui.appcompat.sidepane.COUISidePaneUtils;

/* loaded from: classes.dex */
public class COUISidePaneLifeCycleObserver implements LifecycleObserver {

    /* renamed from: e, reason: collision with root package name */
    private boolean f6259e;

    /* renamed from: f, reason: collision with root package name */
    private COUISidePaneLayout f6260f;

    /* renamed from: g, reason: collision with root package name */
    private View f6261g;

    /* renamed from: h, reason: collision with root package name */
    private View f6262h;

    /* renamed from: i, reason: collision with root package name */
    private View f6263i;

    /* renamed from: j, reason: collision with root package name */
    public Activity f6264j;

    /* renamed from: k, reason: collision with root package name */
    public int f6265k;

    /* renamed from: l, reason: collision with root package name */
    private final View.OnLayoutChangeListener f6266l;

    /* renamed from: m, reason: collision with root package name */
    private final COUISidePaneLayout.h f6267m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUISidePaneLifeCycleObserver.this.f6260f.q()) {
                return;
            }
            COUISidePaneUtils.d(COUISidePaneLifeCycleObserver.this.f6262h, COUISidePaneLifeCycleObserver.this.f6264j);
        }
    }

    @OnLifecycleEvent(h.b.ON_CREATE)
    private void componentCreate() {
        e(true);
        this.f6260f.addOnLayoutChangeListener(this.f6266l);
        this.f6260f.setLifeCycleObserverListener(this.f6267m);
    }

    @OnLifecycleEvent(h.b.ON_DESTROY)
    private void componentDestroy() {
        this.f6260f.removeOnLayoutChangeListener(this.f6266l);
        this.f6260f.setPanelSlideListener(null);
    }

    @OnLifecycleEvent(h.b.ON_RESUME)
    private void componentRestore() {
        d();
    }

    private void d() {
        if (!COUISidePaneUtils.b(this.f6264j) && !COUISidePaneUtils.c(this.f6264j)) {
            View view = this.f6263i;
            if (view != null) {
                view.setVisibility(8);
            }
            View view2 = this.f6262h;
            if (view2 == null || !(view2.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                return;
            }
            MarginLayoutParamsCompat.d((ViewGroup.MarginLayoutParams) this.f6262h.getLayoutParams(), 0);
            return;
        }
        View view3 = this.f6263i;
        if (view3 != null) {
            view3.setVisibility(this.f6260f.q() ? 0 : 8);
        }
        if (this.f6262h == null || this.f6260f.q()) {
            return;
        }
        COUISidePaneUtils.d(this.f6262h, this.f6264j);
    }

    public void e(boolean z10) {
        if (!COUISidePaneUtils.b(this.f6264j) && !COUISidePaneUtils.c(this.f6264j)) {
            View view = this.f6263i;
            if (view != null) {
                view.setVisibility(8);
            }
            View view2 = this.f6261g;
            if (view2 != null) {
                view2.setVisibility(0);
            }
            if (z10) {
                this.f6260f.setCreateIcon(false);
                this.f6260f.g();
                this.f6260f.getChildAt(0).setVisibility(8);
                this.f6260f.setIconViewVisible(8);
            } else {
                this.f6260f.setDefaultShowPane(Boolean.FALSE);
            }
            View view3 = this.f6262h;
            if (view3 == null || !(view3.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) || z10) {
                return;
            }
            MarginLayoutParamsCompat.d((ViewGroup.MarginLayoutParams) this.f6262h.getLayoutParams(), 0);
            return;
        }
        View view4 = this.f6261g;
        if (view4 != null) {
            view4.setVisibility(8);
        }
        if (this.f6259e) {
            this.f6260f.setFirstViewWidth(this.f6265k);
            this.f6260f.getChildAt(0).getLayoutParams().width = this.f6265k;
        }
        this.f6260f.setCoverStyle(false);
        this.f6260f.setDefaultShowPane(Boolean.TRUE);
        View view5 = this.f6263i;
        if (view5 != null) {
            view5.setVisibility(this.f6260f.q() ? 0 : 8);
        }
        if (this.f6262h != null) {
            if (!this.f6260f.q()) {
                COUISidePaneUtils.d(this.f6262h, this.f6264j);
            }
            if (z10) {
                return;
            }
            this.f6260f.post(new a());
        }
    }
}
