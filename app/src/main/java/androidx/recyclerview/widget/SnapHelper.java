package androidx.recyclerview.widget;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: SnapHelper.java */
/* renamed from: androidx.recyclerview.widget.q, reason: use source file name */
/* loaded from: classes.dex */
public abstract class SnapHelper extends RecyclerView.r {

    /* renamed from: a, reason: collision with root package name */
    RecyclerView f3805a;

    /* renamed from: b, reason: collision with root package name */
    private Scroller f3806b;

    /* renamed from: c, reason: collision with root package name */
    private final RecyclerView.t f3807c = new a();

    /* compiled from: SnapHelper.java */
    /* renamed from: androidx.recyclerview.widget.q$a */
    /* loaded from: classes.dex */
    class a extends RecyclerView.t {

        /* renamed from: a, reason: collision with root package name */
        boolean f3808a = false;

        a() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.t
        public void onScrollStateChanged(RecyclerView recyclerView, int i10) {
            super.onScrollStateChanged(recyclerView, i10);
            if (i10 == 0 && this.f3808a) {
                this.f3808a = false;
                SnapHelper.this.j();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.t
        public void onScrolled(RecyclerView recyclerView, int i10, int i11) {
            if (i10 == 0 && i11 == 0) {
                return;
            }
            this.f3808a = true;
        }
    }

    private void e() {
        this.f3805a.removeOnScrollListener(this.f3807c);
        this.f3805a.setOnFlingListener(null);
    }

    private void h() {
        if (this.f3805a.getOnFlingListener() == null) {
            this.f3805a.addOnScrollListener(this.f3807c);
            this.f3805a.setOnFlingListener(this);
            return;
        }
        throw new IllegalStateException("An instance of OnFlingListener already set.");
    }

    private boolean i(RecyclerView.p pVar, int i10, int i11) {
        RecyclerView.y d10;
        int g6;
        if (!(pVar instanceof RecyclerView.y.b) || (d10 = d(pVar)) == null || (g6 = g(pVar, i10, i11)) == -1) {
            return false;
        }
        d10.p(g6);
        pVar.L1(d10);
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.r
    public boolean a(int i10, int i11) {
        RecyclerView.p layoutManager = this.f3805a.getLayoutManager();
        if (layoutManager == null || this.f3805a.getAdapter() == null) {
            return false;
        }
        int minFlingVelocity = this.f3805a.getMinFlingVelocity();
        return (Math.abs(i11) > minFlingVelocity || Math.abs(i10) > minFlingVelocity) && i(layoutManager, i10, i11);
    }

    public void b(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.f3805a;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            e();
        }
        this.f3805a = recyclerView;
        if (recyclerView != null) {
            h();
            this.f3806b = new Scroller(this.f3805a.getContext(), new DecelerateInterpolator());
            j();
        }
    }

    public abstract int[] c(RecyclerView.p pVar, View view);

    protected abstract RecyclerView.y d(RecyclerView.p pVar);

    public abstract View f(RecyclerView.p pVar);

    public abstract int g(RecyclerView.p pVar, int i10, int i11);

    void j() {
        RecyclerView.p layoutManager;
        View f10;
        RecyclerView recyclerView = this.f3805a;
        if (recyclerView == null || (layoutManager = recyclerView.getLayoutManager()) == null || (f10 = f(layoutManager)) == null) {
            return;
        }
        int[] c10 = c(layoutManager, f10);
        if (c10[0] == 0 && c10[1] == 0) {
            return;
        }
        this.f3805a.smoothScrollBy(c10[0], c10[1]);
    }
}
