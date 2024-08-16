package v3;

import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/* compiled from: ExpandableWidgetHelper.java */
/* renamed from: v3.b, reason: use source file name */
/* loaded from: classes.dex */
public final class ExpandableWidgetHelper {

    /* renamed from: a, reason: collision with root package name */
    private final View f19046a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f19047b = false;

    /* renamed from: c, reason: collision with root package name */
    private int f19048c = 0;

    /* JADX WARN: Multi-variable type inference failed */
    public ExpandableWidgetHelper(ExpandableWidget expandableWidget) {
        this.f19046a = (View) expandableWidget;
    }

    private void a() {
        ViewParent parent = this.f19046a.getParent();
        if (parent instanceof CoordinatorLayout) {
            ((CoordinatorLayout) parent).p(this.f19046a);
        }
    }

    public int b() {
        return this.f19048c;
    }

    public boolean c() {
        return this.f19047b;
    }

    public void d(Bundle bundle) {
        this.f19047b = bundle.getBoolean("expanded", false);
        this.f19048c = bundle.getInt("expandedComponentIdHint", 0);
        if (this.f19047b) {
            a();
        }
    }

    public Bundle e() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("expanded", this.f19047b);
        bundle.putInt("expandedComponentIdHint", this.f19048c);
        return bundle;
    }

    public void f(int i10) {
        this.f19048c = i10;
    }
}
