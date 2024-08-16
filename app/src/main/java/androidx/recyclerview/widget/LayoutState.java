package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: LayoutState.java */
/* renamed from: androidx.recyclerview.widget.i, reason: use source file name */
/* loaded from: classes.dex */
class LayoutState {

    /* renamed from: b, reason: collision with root package name */
    int f3779b;

    /* renamed from: c, reason: collision with root package name */
    int f3780c;

    /* renamed from: d, reason: collision with root package name */
    int f3781d;

    /* renamed from: e, reason: collision with root package name */
    int f3782e;

    /* renamed from: h, reason: collision with root package name */
    boolean f3785h;

    /* renamed from: i, reason: collision with root package name */
    boolean f3786i;

    /* renamed from: a, reason: collision with root package name */
    boolean f3778a = true;

    /* renamed from: f, reason: collision with root package name */
    int f3783f = 0;

    /* renamed from: g, reason: collision with root package name */
    int f3784g = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(RecyclerView.z zVar) {
        int i10 = this.f3780c;
        return i10 >= 0 && i10 < zVar.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View b(RecyclerView.v vVar) {
        View o10 = vVar.o(this.f3780c);
        this.f3780c += this.f3781d;
        return o10;
    }

    public String toString() {
        return "LayoutState{mAvailable=" + this.f3779b + ", mCurrentPosition=" + this.f3780c + ", mItemDirection=" + this.f3781d + ", mLayoutDirection=" + this.f3782e + ", mStartLine=" + this.f3783f + ", mEndLine=" + this.f3784g + '}';
    }
}
