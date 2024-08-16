package androidx.transition;

import android.view.ViewGroup;

/* compiled from: ViewGroupUtils.java */
/* loaded from: classes.dex */
class a0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(ViewGroup viewGroup, int i10) {
        return viewGroup.getChildDrawingOrder(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static z b(ViewGroup viewGroup) {
        return new y(viewGroup);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(ViewGroup viewGroup, boolean z10) {
        viewGroup.suppressLayout(z10);
    }
}
