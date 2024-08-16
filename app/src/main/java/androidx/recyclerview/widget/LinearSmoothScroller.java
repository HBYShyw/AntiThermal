package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;

/* compiled from: LinearSmoothScroller.java */
/* renamed from: androidx.recyclerview.widget.j, reason: use source file name */
/* loaded from: classes.dex */
public class LinearSmoothScroller extends RecyclerView.y {

    /* renamed from: k, reason: collision with root package name */
    protected PointF f3789k;

    /* renamed from: l, reason: collision with root package name */
    private final DisplayMetrics f3790l;

    /* renamed from: n, reason: collision with root package name */
    private float f3792n;

    /* renamed from: i, reason: collision with root package name */
    protected final LinearInterpolator f3787i = new LinearInterpolator();

    /* renamed from: j, reason: collision with root package name */
    protected final DecelerateInterpolator f3788j = new DecelerateInterpolator();

    /* renamed from: m, reason: collision with root package name */
    private boolean f3791m = false;

    /* renamed from: o, reason: collision with root package name */
    protected int f3793o = 0;

    /* renamed from: p, reason: collision with root package name */
    protected int f3794p = 0;

    public LinearSmoothScroller(Context context) {
        this.f3790l = context.getResources().getDisplayMetrics();
    }

    private float A() {
        if (!this.f3791m) {
            this.f3792n = v(this.f3790l);
            this.f3791m = true;
        }
        return this.f3792n;
    }

    private int y(int i10, int i11) {
        int i12 = i10 - i11;
        if (i10 * i12 <= 0) {
            return 0;
        }
        return i12;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int B() {
        PointF pointF = this.f3789k;
        if (pointF != null) {
            float f10 = pointF.y;
            if (f10 != 0.0f) {
                return f10 > 0.0f ? 1 : -1;
            }
        }
        return 0;
    }

    protected void C(RecyclerView.y.a aVar) {
        PointF a10 = a(f());
        if (a10 != null && (a10.x != 0.0f || a10.y != 0.0f)) {
            i(a10);
            this.f3789k = a10;
            this.f3793o = (int) (a10.x * 10000.0f);
            this.f3794p = (int) (a10.y * 10000.0f);
            aVar.d((int) (this.f3793o * 1.2f), (int) (this.f3794p * 1.2f), (int) (x(DataLinkConstants.RUS_UPDATE) * 1.2f), this.f3787i);
            return;
        }
        aVar.b(f());
        r();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.y
    protected void l(int i10, int i11, RecyclerView.z zVar, RecyclerView.y.a aVar) {
        if (c() == 0) {
            r();
            return;
        }
        this.f3793o = y(this.f3793o, i10);
        int y4 = y(this.f3794p, i11);
        this.f3794p = y4;
        if (this.f3793o == 0 && y4 == 0) {
            C(aVar);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.y
    protected void m() {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.y
    protected void n() {
        this.f3794p = 0;
        this.f3793o = 0;
        this.f3789k = null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.y
    protected void o(View view, RecyclerView.z zVar, RecyclerView.y.a aVar) {
        int t7 = t(view, z());
        int u7 = u(view, B());
        int w10 = w((int) Math.sqrt((t7 * t7) + (u7 * u7)));
        if (w10 > 0) {
            aVar.d(-t7, -u7, w10, this.f3788j);
        }
    }

    public int s(int i10, int i11, int i12, int i13, int i14) {
        if (i14 == -1) {
            return i12 - i10;
        }
        if (i14 != 0) {
            if (i14 == 1) {
                return i13 - i11;
            }
            throw new IllegalArgumentException("snap preference should be one of the constants defined in SmoothScroller, starting with SNAP_");
        }
        int i15 = i12 - i10;
        if (i15 > 0) {
            return i15;
        }
        int i16 = i13 - i11;
        if (i16 < 0) {
            return i16;
        }
        return 0;
    }

    public int t(View view, int i10) {
        RecyclerView.p e10 = e();
        if (e10 == null || !e10.k()) {
            return 0;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        return s(e10.Q(view) - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, e10.T(view) + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, e10.f0(), e10.q0() - e10.g0(), i10);
    }

    public int u(View view, int i10) {
        RecyclerView.p e10 = e();
        if (e10 == null || !e10.l()) {
            return 0;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        return s(e10.U(view) - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, e10.O(view) + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin, e10.i0(), e10.W() - e10.d0(), i10);
    }

    protected float v(DisplayMetrics displayMetrics) {
        return 25.0f / displayMetrics.densityDpi;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int w(int i10) {
        return (int) Math.ceil(x(i10) / 0.3356d);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int x(int i10) {
        return (int) Math.ceil(Math.abs(i10) * A());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int z() {
        PointF pointF = this.f3789k;
        if (pointF != null) {
            float f10 = pointF.x;
            if (f10 != 0.0f) {
                return f10 > 0.0f ? 1 : -1;
            }
        }
        return 0;
    }
}
