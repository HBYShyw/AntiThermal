package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: SimpleItemAnimator.java */
/* renamed from: androidx.recyclerview.widget.p, reason: use source file name */
/* loaded from: classes.dex */
public abstract class SimpleItemAnimator extends RecyclerView.m {

    /* renamed from: g, reason: collision with root package name */
    boolean f3804g = true;

    public final void A(RecyclerView.c0 c0Var) {
        I(c0Var);
        h(c0Var);
    }

    public final void B(RecyclerView.c0 c0Var) {
        J(c0Var);
    }

    public final void C(RecyclerView.c0 c0Var, boolean z10) {
        K(c0Var, z10);
        h(c0Var);
    }

    public final void D(RecyclerView.c0 c0Var, boolean z10) {
        L(c0Var, z10);
    }

    public final void E(RecyclerView.c0 c0Var) {
        M(c0Var);
        h(c0Var);
    }

    public final void F(RecyclerView.c0 c0Var) {
        N(c0Var);
    }

    public final void G(RecyclerView.c0 c0Var) {
        O(c0Var);
        h(c0Var);
    }

    public final void H(RecyclerView.c0 c0Var) {
        P(c0Var);
    }

    public void I(RecyclerView.c0 c0Var) {
    }

    public void J(RecyclerView.c0 c0Var) {
    }

    public void K(RecyclerView.c0 c0Var, boolean z10) {
    }

    public void L(RecyclerView.c0 c0Var, boolean z10) {
    }

    public void M(RecyclerView.c0 c0Var) {
    }

    public void N(RecyclerView.c0 c0Var) {
    }

    public void O(RecyclerView.c0 c0Var) {
    }

    public void P(RecyclerView.c0 c0Var) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public boolean a(RecyclerView.c0 c0Var, RecyclerView.m.c cVar, RecyclerView.m.c cVar2) {
        int i10;
        int i11;
        if (cVar != null && ((i10 = cVar.f3481a) != (i11 = cVar2.f3481a) || cVar.f3482b != cVar2.f3482b)) {
            return y(c0Var, i10, cVar.f3482b, i11, cVar2.f3482b);
        }
        return w(c0Var);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public boolean b(RecyclerView.c0 c0Var, RecyclerView.c0 c0Var2, RecyclerView.m.c cVar, RecyclerView.m.c cVar2) {
        int i10;
        int i11;
        int i12 = cVar.f3481a;
        int i13 = cVar.f3482b;
        if (c0Var2.shouldIgnore()) {
            int i14 = cVar.f3481a;
            i11 = cVar.f3482b;
            i10 = i14;
        } else {
            i10 = cVar2.f3481a;
            i11 = cVar2.f3482b;
        }
        return x(c0Var, c0Var2, i12, i13, i10, i11);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public boolean c(RecyclerView.c0 c0Var, RecyclerView.m.c cVar, RecyclerView.m.c cVar2) {
        int i10 = cVar.f3481a;
        int i11 = cVar.f3482b;
        View view = c0Var.itemView;
        int left = cVar2 == null ? view.getLeft() : cVar2.f3481a;
        int top = cVar2 == null ? view.getTop() : cVar2.f3482b;
        if (!c0Var.isRemoved() && (i10 != left || i11 != top)) {
            view.layout(left, top, view.getWidth() + left, view.getHeight() + top);
            return y(c0Var, i10, i11, left, top);
        }
        return z(c0Var);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public boolean d(RecyclerView.c0 c0Var, RecyclerView.m.c cVar, RecyclerView.m.c cVar2) {
        int i10 = cVar.f3481a;
        int i11 = cVar2.f3481a;
        if (i10 == i11 && cVar.f3482b == cVar2.f3482b) {
            E(c0Var);
            return false;
        }
        return y(c0Var, i10, cVar.f3482b, i11, cVar2.f3482b);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.m
    public boolean f(RecyclerView.c0 c0Var) {
        return !this.f3804g || c0Var.isInvalid();
    }

    public abstract boolean w(RecyclerView.c0 c0Var);

    public abstract boolean x(RecyclerView.c0 c0Var, RecyclerView.c0 c0Var2, int i10, int i11, int i12, int i13);

    public abstract boolean y(RecyclerView.c0 c0Var, int i10, int i11, int i12, int i13);

    public abstract boolean z(RecyclerView.c0 c0Var);
}
