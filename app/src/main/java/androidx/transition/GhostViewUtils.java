package androidx.transition;

import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;

/* compiled from: GhostViewUtils.java */
/* renamed from: androidx.transition.i, reason: use source file name */
/* loaded from: classes.dex */
class GhostViewUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static GhostView a(View view, ViewGroup viewGroup, Matrix matrix) {
        return GhostViewPort.b(view, viewGroup, matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(View view) {
        GhostViewPort.f(view);
    }
}
