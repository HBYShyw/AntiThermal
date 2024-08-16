package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.core.view.ViewCompat;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: GhostViewPort.java */
@SuppressLint({"ViewConstructor"})
/* renamed from: androidx.transition.h, reason: use source file name */
/* loaded from: classes.dex */
public class GhostViewPort extends ViewGroup implements GhostView {

    /* renamed from: e, reason: collision with root package name */
    ViewGroup f4113e;

    /* renamed from: f, reason: collision with root package name */
    View f4114f;

    /* renamed from: g, reason: collision with root package name */
    final View f4115g;

    /* renamed from: h, reason: collision with root package name */
    int f4116h;

    /* renamed from: i, reason: collision with root package name */
    private Matrix f4117i;

    /* renamed from: j, reason: collision with root package name */
    private final ViewTreeObserver.OnPreDrawListener f4118j;

    /* compiled from: GhostViewPort.java */
    /* renamed from: androidx.transition.h$a */
    /* loaded from: classes.dex */
    class a implements ViewTreeObserver.OnPreDrawListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            View view;
            ViewCompat.b0(GhostViewPort.this);
            GhostViewPort ghostViewPort = GhostViewPort.this;
            ViewGroup viewGroup = ghostViewPort.f4113e;
            if (viewGroup == null || (view = ghostViewPort.f4114f) == null) {
                return true;
            }
            viewGroup.endViewTransition(view);
            ViewCompat.b0(GhostViewPort.this.f4113e);
            GhostViewPort ghostViewPort2 = GhostViewPort.this;
            ghostViewPort2.f4113e = null;
            ghostViewPort2.f4114f = null;
            return true;
        }
    }

    GhostViewPort(View view) {
        super(view.getContext());
        this.f4118j = new a();
        this.f4115g = view;
        setWillNotDraw(false);
        setLayerType(2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static GhostViewPort b(View view, ViewGroup viewGroup, Matrix matrix) {
        GhostViewHolder ghostViewHolder;
        if (view.getParent() instanceof ViewGroup) {
            GhostViewHolder b10 = GhostViewHolder.b(viewGroup);
            GhostViewPort e10 = e(view);
            int i10 = 0;
            if (e10 != null && (ghostViewHolder = (GhostViewHolder) e10.getParent()) != b10) {
                i10 = e10.f4116h;
                ghostViewHolder.removeView(e10);
                e10 = null;
            }
            if (e10 == null) {
                if (matrix == null) {
                    matrix = new Matrix();
                    c(view, viewGroup, matrix);
                }
                e10 = new GhostViewPort(view);
                e10.h(matrix);
                if (b10 == null) {
                    b10 = new GhostViewHolder(viewGroup);
                } else {
                    b10.g();
                }
                d(viewGroup, b10);
                d(viewGroup, e10);
                b10.a(e10);
                e10.f4116h = i10;
            } else if (matrix != null) {
                e10.h(matrix);
            }
            e10.f4116h++;
            return e10;
        }
        throw new IllegalArgumentException("Ghosted views must be parented by a ViewGroup");
    }

    static void c(View view, ViewGroup viewGroup, Matrix matrix) {
        ViewGroup viewGroup2 = (ViewGroup) view.getParent();
        matrix.reset();
        d0.j(viewGroup2, matrix);
        matrix.preTranslate(-viewGroup2.getScrollX(), -viewGroup2.getScrollY());
        d0.k(viewGroup, matrix);
    }

    static void d(View view, View view2) {
        d0.g(view2, view2.getLeft(), view2.getTop(), view2.getLeft() + view.getWidth(), view2.getTop() + view.getHeight());
    }

    static GhostViewPort e(View view) {
        return (GhostViewPort) view.getTag(R$id.ghost_view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void f(View view) {
        GhostViewPort e10 = e(view);
        if (e10 != null) {
            int i10 = e10.f4116h - 1;
            e10.f4116h = i10;
            if (i10 <= 0) {
                ((GhostViewHolder) e10.getParent()).removeView(e10);
            }
        }
    }

    static void g(View view, GhostViewPort ghostViewPort) {
        view.setTag(R$id.ghost_view, ghostViewPort);
    }

    @Override // androidx.transition.GhostView
    public void a(ViewGroup viewGroup, View view) {
        this.f4113e = viewGroup;
        this.f4114f = view;
    }

    void h(Matrix matrix) {
        this.f4117i = matrix;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        g(this.f4115g, this);
        this.f4115g.getViewTreeObserver().addOnPreDrawListener(this.f4118j);
        d0.i(this.f4115g, 4);
        if (this.f4115g.getParent() != null) {
            ((View) this.f4115g.getParent()).invalidate();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        this.f4115g.getViewTreeObserver().removeOnPreDrawListener(this.f4118j);
        d0.i(this.f4115g, 0);
        g(this.f4115g, null);
        if (this.f4115g.getParent() != null) {
            ((View) this.f4115g.getParent()).invalidate();
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        CanvasUtils.a(canvas, true);
        canvas.setMatrix(this.f4117i);
        d0.i(this.f4115g, 0);
        this.f4115g.invalidate();
        d0.i(this.f4115g, 4);
        drawChild(canvas, this.f4115g, getDrawingTime());
        CanvasUtils.a(canvas, false);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
    }

    @Override // android.view.View, androidx.transition.GhostView
    public void setVisibility(int i10) {
        super.setVisibility(i10);
        if (e(this.f4115g) == this) {
            d0.i(this.f4115g, i10 == 0 ? 4 : 0);
        }
    }
}
