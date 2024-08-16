package androidx.transition;

import android.graphics.Matrix;
import android.view.View;

/* compiled from: ViewUtilsApi29.java */
/* renamed from: androidx.transition.i0, reason: use source file name */
/* loaded from: classes.dex */
class ViewUtilsApi29 extends ViewUtilsApi23 {
    @Override // androidx.transition.ViewUtilsBase
    public float b(View view) {
        return view.getTransitionAlpha();
    }

    @Override // androidx.transition.ViewUtilsBase
    public void d(View view, Matrix matrix) {
        view.setAnimationMatrix(matrix);
    }

    @Override // androidx.transition.ViewUtilsBase
    public void e(View view, int i10, int i11, int i12, int i13) {
        view.setLeftTopRightBottom(i10, i11, i12, i13);
    }

    @Override // androidx.transition.ViewUtilsBase
    public void f(View view, float f10) {
        view.setTransitionAlpha(f10);
    }

    @Override // androidx.transition.ViewUtilsBase
    public void g(View view, int i10) {
        view.setTransitionVisibility(i10);
    }

    @Override // androidx.transition.ViewUtilsBase
    public void h(View view, Matrix matrix) {
        view.transformMatrixToGlobal(matrix);
    }

    @Override // androidx.transition.ViewUtilsBase
    public void i(View view, Matrix matrix) {
        view.transformMatrixToLocal(matrix);
    }
}
