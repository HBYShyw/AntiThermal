package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: PagerSnapHelper.java */
/* renamed from: androidx.recyclerview.widget.m, reason: use source file name */
/* loaded from: classes.dex */
public class PagerSnapHelper extends SnapHelper {

    /* renamed from: d, reason: collision with root package name */
    private OrientationHelper f3799d;

    /* renamed from: e, reason: collision with root package name */
    private OrientationHelper f3800e;

    /* compiled from: PagerSnapHelper.java */
    /* renamed from: androidx.recyclerview.widget.m$a */
    /* loaded from: classes.dex */
    class a extends LinearSmoothScroller {
        a(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.y
        protected void o(View view, RecyclerView.z zVar, RecyclerView.y.a aVar) {
            PagerSnapHelper pagerSnapHelper = PagerSnapHelper.this;
            int[] c10 = pagerSnapHelper.c(pagerSnapHelper.f3805a.getLayoutManager(), view);
            int i10 = c10[0];
            int i11 = c10[1];
            int w10 = w(Math.max(Math.abs(i10), Math.abs(i11)));
            if (w10 > 0) {
                aVar.d(i10, i11, w10, this.f3788j);
            }
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        protected float v(DisplayMetrics displayMetrics) {
            return 100.0f / displayMetrics.densityDpi;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public int x(int i10) {
            return Math.min(100, super.x(i10));
        }
    }

    private int k(View view, OrientationHelper orientationHelper) {
        return (orientationHelper.g(view) + (orientationHelper.e(view) / 2)) - (orientationHelper.n() + (orientationHelper.o() / 2));
    }

    private View l(RecyclerView.p pVar, OrientationHelper orientationHelper) {
        int J = pVar.J();
        View view = null;
        if (J == 0) {
            return null;
        }
        int n10 = orientationHelper.n() + (orientationHelper.o() / 2);
        int i10 = Integer.MAX_VALUE;
        for (int i11 = 0; i11 < J; i11++) {
            View I = pVar.I(i11);
            int abs = Math.abs((orientationHelper.g(I) + (orientationHelper.e(I) / 2)) - n10);
            if (abs < i10) {
                view = I;
                i10 = abs;
            }
        }
        return view;
    }

    private OrientationHelper m(RecyclerView.p pVar) {
        OrientationHelper orientationHelper = this.f3800e;
        if (orientationHelper == null || orientationHelper.f3796a != pVar) {
            this.f3800e = OrientationHelper.a(pVar);
        }
        return this.f3800e;
    }

    private OrientationHelper n(RecyclerView.p pVar) {
        if (pVar.l()) {
            return o(pVar);
        }
        if (pVar.k()) {
            return m(pVar);
        }
        return null;
    }

    private OrientationHelper o(RecyclerView.p pVar) {
        OrientationHelper orientationHelper = this.f3799d;
        if (orientationHelper == null || orientationHelper.f3796a != pVar) {
            this.f3799d = OrientationHelper.c(pVar);
        }
        return this.f3799d;
    }

    private boolean p(RecyclerView.p pVar, int i10, int i11) {
        return pVar.k() ? i10 > 0 : i11 > 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean q(RecyclerView.p pVar) {
        PointF a10;
        int Y = pVar.Y();
        if (!(pVar instanceof RecyclerView.y.b) || (a10 = ((RecyclerView.y.b) pVar).a(Y - 1)) == null) {
            return false;
        }
        return a10.x < 0.0f || a10.y < 0.0f;
    }

    @Override // androidx.recyclerview.widget.SnapHelper
    public int[] c(RecyclerView.p pVar, View view) {
        int[] iArr = new int[2];
        if (pVar.k()) {
            iArr[0] = k(view, m(pVar));
        } else {
            iArr[0] = 0;
        }
        if (pVar.l()) {
            iArr[1] = k(view, o(pVar));
        } else {
            iArr[1] = 0;
        }
        return iArr;
    }

    @Override // androidx.recyclerview.widget.SnapHelper
    protected RecyclerView.y d(RecyclerView.p pVar) {
        if (pVar instanceof RecyclerView.y.b) {
            return new a(this.f3805a.getContext());
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.SnapHelper
    public View f(RecyclerView.p pVar) {
        if (pVar.l()) {
            return l(pVar, o(pVar));
        }
        if (pVar.k()) {
            return l(pVar, m(pVar));
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.SnapHelper
    public int g(RecyclerView.p pVar, int i10, int i11) {
        OrientationHelper n10;
        int Y = pVar.Y();
        if (Y == 0 || (n10 = n(pVar)) == null) {
            return -1;
        }
        int i12 = Integer.MIN_VALUE;
        int i13 = Integer.MAX_VALUE;
        int J = pVar.J();
        View view = null;
        View view2 = null;
        for (int i14 = 0; i14 < J; i14++) {
            View I = pVar.I(i14);
            if (I != null) {
                int k10 = k(I, n10);
                if (k10 <= 0 && k10 > i12) {
                    view2 = I;
                    i12 = k10;
                }
                if (k10 >= 0 && k10 < i13) {
                    view = I;
                    i13 = k10;
                }
            }
        }
        boolean p10 = p(pVar, i10, i11);
        if (p10 && view != null) {
            return pVar.j0(view);
        }
        if (!p10 && view2 != null) {
            return pVar.j0(view2);
        }
        if (p10) {
            view = view2;
        }
        if (view == null) {
            return -1;
        }
        int j02 = pVar.j0(view) + (q(pVar) == p10 ? -1 : 1);
        if (j02 < 0 || j02 >= Y) {
            return -1;
        }
        return j02;
    }
}
