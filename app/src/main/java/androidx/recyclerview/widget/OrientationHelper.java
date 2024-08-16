package androidx.recyclerview.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: OrientationHelper.java */
/* renamed from: androidx.recyclerview.widget.l, reason: use source file name */
/* loaded from: classes.dex */
public abstract class OrientationHelper {

    /* renamed from: a, reason: collision with root package name */
    protected final RecyclerView.p f3796a;

    /* renamed from: b, reason: collision with root package name */
    private int f3797b;

    /* renamed from: c, reason: collision with root package name */
    final Rect f3798c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OrientationHelper.java */
    /* renamed from: androidx.recyclerview.widget.l$a */
    /* loaded from: classes.dex */
    public class a extends OrientationHelper {
        a(RecyclerView.p pVar) {
            super(pVar, null);
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int d(View view) {
            return this.f3796a.T(view) + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).rightMargin;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int e(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return this.f3796a.S(view) + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int f(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return this.f3796a.R(view) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int g(View view) {
            return this.f3796a.Q(view) - ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).leftMargin;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int h() {
            return this.f3796a.q0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int i() {
            return this.f3796a.q0() - this.f3796a.g0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int j() {
            return this.f3796a.g0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int l() {
            return this.f3796a.r0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int m() {
            return this.f3796a.X();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int n() {
            return this.f3796a.f0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int o() {
            return (this.f3796a.q0() - this.f3796a.f0()) - this.f3796a.g0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int q(View view) {
            this.f3796a.p0(view, true, this.f3798c);
            return this.f3798c.right;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int r(View view) {
            this.f3796a.p0(view, true, this.f3798c);
            return this.f3798c.left;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public void s(int i10) {
            this.f3796a.E0(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OrientationHelper.java */
    /* renamed from: androidx.recyclerview.widget.l$b */
    /* loaded from: classes.dex */
    public class b extends OrientationHelper {
        b(RecyclerView.p pVar) {
            super(pVar, null);
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int d(View view) {
            return this.f3796a.O(view) + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).bottomMargin;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int e(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return this.f3796a.R(view) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int f(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return this.f3796a.S(view) + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int g(View view) {
            return this.f3796a.U(view) - ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).topMargin;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int h() {
            return this.f3796a.W();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int i() {
            return this.f3796a.W() - this.f3796a.d0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int j() {
            return this.f3796a.d0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int l() {
            return this.f3796a.X();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int m() {
            return this.f3796a.r0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int n() {
            return this.f3796a.i0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int o() {
            return (this.f3796a.W() - this.f3796a.i0()) - this.f3796a.d0();
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int q(View view) {
            this.f3796a.p0(view, true, this.f3798c);
            return this.f3798c.bottom;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public int r(View view) {
            this.f3796a.p0(view, true, this.f3798c);
            return this.f3798c.top;
        }

        @Override // androidx.recyclerview.widget.OrientationHelper
        public void s(int i10) {
            this.f3796a.F0(i10);
        }
    }

    /* synthetic */ OrientationHelper(RecyclerView.p pVar, a aVar) {
        this(pVar);
    }

    public static OrientationHelper a(RecyclerView.p pVar) {
        return new a(pVar);
    }

    public static OrientationHelper b(RecyclerView.p pVar, int i10) {
        if (i10 == 0) {
            return a(pVar);
        }
        if (i10 == 1) {
            return c(pVar);
        }
        throw new IllegalArgumentException("invalid orientation");
    }

    public static OrientationHelper c(RecyclerView.p pVar) {
        return new b(pVar);
    }

    public abstract int d(View view);

    public abstract int e(View view);

    public abstract int f(View view);

    public abstract int g(View view);

    public abstract int h();

    public abstract int i();

    public abstract int j();

    public RecyclerView.p k() {
        return this.f3796a;
    }

    public abstract int l();

    public abstract int m();

    public abstract int n();

    public abstract int o();

    public int p() {
        if (Integer.MIN_VALUE == this.f3797b) {
            return 0;
        }
        return o() - this.f3797b;
    }

    public abstract int q(View view);

    public abstract int r(View view);

    public abstract void s(int i10);

    public void t() {
        this.f3797b = o();
    }

    private OrientationHelper(RecyclerView.p pVar) {
        this.f3797b = Integer.MIN_VALUE;
        this.f3798c = new Rect();
        this.f3796a = pVar;
    }
}
