package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;

/* compiled from: NestedScrollingParentHelper.java */
/* renamed from: androidx.core.view.s, reason: use source file name */
/* loaded from: classes.dex */
public class NestedScrollingParentHelper {

    /* renamed from: a, reason: collision with root package name */
    private int f2410a;

    /* renamed from: b, reason: collision with root package name */
    private int f2411b;

    public NestedScrollingParentHelper(ViewGroup viewGroup) {
    }

    public int a() {
        return this.f2411b | this.f2410a;
    }

    public void b(View view, View view2, int i10) {
        c(view, view2, i10, 0);
    }

    public void c(View view, View view2, int i10, int i11) {
        if (i11 == 1) {
            this.f2411b = i10;
        } else {
            this.f2410a = i10;
        }
    }

    public void d(View view, int i10) {
        if (i10 == 1) {
            this.f2411b = 0;
        } else {
            this.f2410a = 0;
        }
    }
}
