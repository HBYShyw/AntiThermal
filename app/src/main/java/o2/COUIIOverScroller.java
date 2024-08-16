package o2;

import android.view.animation.Interpolator;

/* compiled from: COUIIOverScroller.java */
/* renamed from: o2.b, reason: use source file name */
/* loaded from: classes.dex */
public interface COUIIOverScroller {
    float a();

    void abortAnimation();

    int b();

    int c();

    boolean computeScrollOffset();

    void d(float f10);

    float e();

    int f();

    void fling(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17);

    void fling(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19);

    int g();

    void h(float f10);

    void i(Interpolator interpolator);

    boolean j();

    void notifyHorizontalEdgeReached(int i10, int i11, int i12);

    void notifyVerticalEdgeReached(int i10, int i11, int i12);

    void setFinalX(int i10);

    boolean springBack(int i10, int i11, int i12, int i13, int i14, int i15);

    void startScroll(int i10, int i11, int i12, int i13);

    void startScroll(int i10, int i11, int i12, int i13, int i14);
}
