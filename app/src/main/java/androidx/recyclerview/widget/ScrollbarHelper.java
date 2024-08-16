package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: ScrollbarHelper.java */
/* renamed from: androidx.recyclerview.widget.o, reason: use source file name */
/* loaded from: classes.dex */
class ScrollbarHelper {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(RecyclerView.z zVar, OrientationHelper orientationHelper, View view, View view2, RecyclerView.p pVar, boolean z10) {
        if (pVar.J() == 0 || zVar.b() == 0 || view == null || view2 == null) {
            return 0;
        }
        if (!z10) {
            return Math.abs(pVar.j0(view) - pVar.j0(view2)) + 1;
        }
        return Math.min(orientationHelper.o(), orientationHelper.d(view2) - orientationHelper.g(view));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(RecyclerView.z zVar, OrientationHelper orientationHelper, View view, View view2, RecyclerView.p pVar, boolean z10, boolean z11) {
        int max;
        if (pVar.J() == 0 || zVar.b() == 0 || view == null || view2 == null) {
            return 0;
        }
        int min = Math.min(pVar.j0(view), pVar.j0(view2));
        int max2 = Math.max(pVar.j0(view), pVar.j0(view2));
        if (z11) {
            max = Math.max(0, (zVar.b() - max2) - 1);
        } else {
            max = Math.max(0, min);
        }
        if (!z10) {
            return max;
        }
        return Math.round((max * (Math.abs(orientationHelper.d(view2) - orientationHelper.g(view)) / (Math.abs(pVar.j0(view) - pVar.j0(view2)) + 1))) + (orientationHelper.n() - orientationHelper.g(view)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(RecyclerView.z zVar, OrientationHelper orientationHelper, View view, View view2, RecyclerView.p pVar, boolean z10) {
        if (pVar.J() == 0 || zVar.b() == 0 || view == null || view2 == null) {
            return 0;
        }
        if (!z10) {
            return zVar.b();
        }
        return (int) (((orientationHelper.d(view2) - orientationHelper.g(view)) / (Math.abs(pVar.j0(view) - pVar.j0(view2)) + 1)) * zVar.b());
    }
}
