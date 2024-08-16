package o2;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: COUIFlingLocateHelper.java */
/* renamed from: o2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIFlingLocateHelper {

    /* renamed from: a, reason: collision with root package name */
    private COUIRecyclerView f16103a;

    /* renamed from: c, reason: collision with root package name */
    private OrientationHelper f16105c;

    /* renamed from: d, reason: collision with root package name */
    private RecyclerView.p f16106d;

    /* renamed from: e, reason: collision with root package name */
    private Context f16107e;

    /* renamed from: b, reason: collision with root package name */
    private int f16104b = 0;

    /* renamed from: f, reason: collision with root package name */
    private RecyclerView.t f16108f = new a();

    /* compiled from: COUIFlingLocateHelper.java */
    /* renamed from: o2.a$a */
    /* loaded from: classes.dex */
    class a extends RecyclerView.t {

        /* renamed from: a, reason: collision with root package name */
        boolean f16109a = false;

        a() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.t
        public void onScrollStateChanged(RecyclerView recyclerView, int i10) {
            super.onScrollStateChanged(recyclerView, i10);
            if (i10 == 0 && this.f16109a) {
                this.f16109a = false;
                COUIFlingLocateHelper.this.m();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.t
        public void onScrolled(RecyclerView recyclerView, int i10, int i11) {
            if (i10 == 0 && i11 == 0) {
                return;
            }
            this.f16109a = true;
        }
    }

    private float c(RecyclerView.p pVar, OrientationHelper orientationHelper) {
        int J = pVar.J();
        if (J == 0) {
            return 1.0f;
        }
        View view = null;
        int i10 = Integer.MIN_VALUE;
        int i11 = Integer.MAX_VALUE;
        View view2 = null;
        for (int i12 = 0; i12 < J; i12++) {
            View I = pVar.I(i12);
            int j02 = pVar.j0(I);
            if (j02 != -1 && j02 != pVar.Y() - 1 && j02 != 0) {
                if (j02 < i11) {
                    view = I;
                    i11 = j02;
                }
                if (j02 > i10) {
                    view2 = I;
                    i10 = j02;
                }
            }
        }
        if (view == null || view2 == null) {
            return 1.0f;
        }
        int max = Math.max(orientationHelper.d(view), orientationHelper.d(view2)) - Math.min(orientationHelper.g(view), orientationHelper.g(view2));
        if (max == 0) {
            return 1.0f;
        }
        return (max * 1.0f) / ((i10 - i11) + 1);
    }

    private View d(RecyclerView.p pVar, OrientationHelper orientationHelper) {
        int J = pVar.J();
        View view = null;
        if (J == 0) {
            return null;
        }
        int n10 = orientationHelper.n() + (orientationHelper.o() / 2);
        int i10 = Integer.MAX_VALUE;
        for (int i11 = 0; i11 < J; i11++) {
            View I = pVar.I(i11);
            int abs = Math.abs((pVar.Q(I) + (pVar.S(I) / 2)) - n10);
            if (abs < i10) {
                view = I;
                i10 = abs;
            }
        }
        return view;
    }

    private View f(RecyclerView.p pVar, OrientationHelper orientationHelper) {
        int n10;
        int J = pVar.J();
        View view = null;
        if (J == 0) {
            return null;
        }
        if (pVar instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) pVar;
            boolean z10 = linearLayoutManager.X1() == pVar.Y() - 1;
            boolean z11 = linearLayoutManager.c2() == pVar.Y() - 1;
            if (z10 || z11) {
                return null;
            }
        }
        if (k(this.f16107e)) {
            n10 = orientationHelper.i();
        } else {
            n10 = orientationHelper.n();
        }
        int i10 = Integer.MAX_VALUE;
        for (int i11 = 0; i11 < J; i11++) {
            View I = pVar.I(i11);
            int abs = Math.abs((k(this.f16107e) ? orientationHelper.d(I) : orientationHelper.g(I)) - n10);
            if (abs < i10) {
                view = I;
                i10 = abs;
            }
        }
        return view;
    }

    private OrientationHelper g(RecyclerView.p pVar) {
        OrientationHelper orientationHelper = this.f16105c;
        if (orientationHelper == null || orientationHelper.k() != pVar) {
            this.f16105c = OrientationHelper.a(pVar);
        }
        return this.f16105c;
    }

    private RecyclerView.p i() {
        RecyclerView.p pVar = this.f16106d;
        if (pVar == null || pVar != this.f16103a.getLayoutManager()) {
            this.f16106d = this.f16103a.getLayoutManager();
        }
        return this.f16106d;
    }

    private boolean k(Context context) {
        return context != null && context.getResources().getConfiguration().getLayoutDirection() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() {
        View e10;
        int g6;
        int n10;
        RecyclerView.p i10 = i();
        if (i10 == null || (e10 = e(i10)) == null) {
            return;
        }
        int i11 = this.f16104b;
        if (i11 == 2) {
            int n11 = g(i10).n() + (g(i10).o() / 2);
            int Y = i10.Y() - 1;
            if (i10.j0(e10) == 0) {
                n11 = k(this.f16107e) ? g(i10).i() - (g(i10).e(e10) / 2) : g(i10).n() + (g(i10).e(e10) / 2);
            }
            if (i10.j0(e10) == Y) {
                n11 = k(this.f16107e) ? g(i10).n() + (g(i10).e(e10) / 2) : g(i10).i() - (g(i10).e(e10) / 2);
            }
            int g10 = (g(i10).g(e10) + (g(i10).e(e10) / 2)) - n11;
            if (Math.abs(g10) > 1.0f) {
                this.f16103a.smoothScrollBy(g10, 0);
                return;
            }
            return;
        }
        if (i11 == 1) {
            if (k(this.f16107e)) {
                g6 = g(i10).d(e10);
                n10 = g(i10).i();
            } else {
                g6 = g(i10).g(e10);
                n10 = g(i10).n();
            }
            int i12 = g6 - n10;
            if (Math.abs(i12) > 1.0f) {
                this.f16103a.smoothScrollBy(i12, 0);
            }
        }
    }

    public void b(COUIRecyclerView cOUIRecyclerView) {
        this.f16103a = cOUIRecyclerView;
        this.f16107e = cOUIRecyclerView.getContext();
    }

    public View e(RecyclerView.p pVar) {
        if (pVar.k()) {
            int i10 = this.f16104b;
            if (i10 == 2) {
                return d(pVar, g(pVar));
            }
            if (i10 == 1) {
                return f(pVar, g(pVar));
            }
        }
        return null;
    }

    public int h() {
        return this.f16104b;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int j(int i10) {
        View e10;
        int i11;
        int g6;
        RecyclerView.p i12 = i();
        int Y = i12.Y();
        if (Y == 0 || (e10 = e(i12)) == null) {
            return -1;
        }
        int j02 = i12.j0(e10);
        int i13 = Y - 1;
        PointF a10 = ((RecyclerView.y.b) i12).a(i13);
        if (a10 == null) {
            return -1;
        }
        float f10 = 1.0f;
        if (i12.k()) {
            f10 = c(i12, g(i12));
            i11 = Math.round(i10 / f10);
            if (a10.x < 0.0f) {
                i11 = -i11;
            }
        } else {
            i11 = 0;
        }
        int i14 = i11 + j02;
        if (i14 != j02 && i14 >= 0 && i14 < Y) {
            int i15 = this.f16104b;
            if (i15 == 2) {
                View view = null;
                if (i12.j0(e10) == 0 && i12.J() != 0) {
                    view = i12.I(i12.J() - 1);
                }
                if (i12.j0(e10) == i13 && i12.J() != 0) {
                    view = i12.I(0);
                }
                int n10 = g(i12).n() + (g(i12).o() / 2);
                if (view != null) {
                    g6 = g(i12).g(view) + (g(i12).e(view) / 2) + (k(this.f16107e) ? -((int) ((i14 - i12.j0(view)) * f10)) : (int) ((i14 - i12.j0(view)) * f10));
                } else {
                    g6 = g(i12).g(e10) + (g(i12).e(e10) / 2) + (k(this.f16107e) ? -((int) ((i14 - i12.j0(e10)) * f10)) : (int) ((i14 - i12.j0(e10)) * f10));
                }
                return g6 - n10;
            }
            if (i15 == 1) {
                int i16 = i14 - j02;
                return ((k(this.f16107e) ? g(i12).d(e10) : g(i12).g(e10)) + (k(this.f16107e) ? -((int) (i16 * f10)) : (int) (i16 * f10))) - (k(this.f16107e) ? g(i12).i() : g(i12).n());
            }
        }
        return -1;
    }

    public void l(int i10) {
        this.f16104b = i10;
        this.f16103a.addOnScrollListener(this.f16108f);
    }

    public void n() {
        if (this.f16104b != 0) {
            m();
        }
    }
}
