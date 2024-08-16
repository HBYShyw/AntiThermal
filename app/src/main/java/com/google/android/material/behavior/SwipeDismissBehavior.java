package com.google.android.material.behavior;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.widget.ViewDragHelper;

/* loaded from: classes.dex */
public class SwipeDismissBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    /* renamed from: a, reason: collision with root package name */
    ViewDragHelper f8321a;

    /* renamed from: b, reason: collision with root package name */
    c f8322b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f8323c;

    /* renamed from: e, reason: collision with root package name */
    private boolean f8325e;

    /* renamed from: d, reason: collision with root package name */
    private float f8324d = 0.0f;

    /* renamed from: f, reason: collision with root package name */
    int f8326f = 2;

    /* renamed from: g, reason: collision with root package name */
    float f8327g = 0.5f;

    /* renamed from: h, reason: collision with root package name */
    float f8328h = 0.0f;

    /* renamed from: i, reason: collision with root package name */
    float f8329i = 0.5f;

    /* renamed from: j, reason: collision with root package name */
    private final ViewDragHelper.c f8330j = new a();

    /* loaded from: classes.dex */
    class a extends ViewDragHelper.c {

        /* renamed from: a, reason: collision with root package name */
        private int f8331a;

        /* renamed from: b, reason: collision with root package name */
        private int f8332b = -1;

        a() {
        }

        private boolean n(View view, float f10) {
            if (f10 == 0.0f) {
                return Math.abs(view.getLeft() - this.f8331a) >= Math.round(((float) view.getWidth()) * SwipeDismissBehavior.this.f8327g);
            }
            boolean z10 = ViewCompat.x(view) == 1;
            int i10 = SwipeDismissBehavior.this.f8326f;
            if (i10 == 2) {
                return true;
            }
            if (i10 == 0) {
                if (z10) {
                    if (f10 >= 0.0f) {
                        return false;
                    }
                } else if (f10 <= 0.0f) {
                    return false;
                }
                return true;
            }
            if (i10 != 1) {
                return false;
            }
            if (z10) {
                if (f10 <= 0.0f) {
                    return false;
                }
            } else if (f10 >= 0.0f) {
                return false;
            }
            return true;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int a(View view, int i10, int i11) {
            int width;
            int width2;
            int width3;
            boolean z10 = ViewCompat.x(view) == 1;
            int i12 = SwipeDismissBehavior.this.f8326f;
            if (i12 == 0) {
                if (z10) {
                    width = this.f8331a - view.getWidth();
                    width2 = this.f8331a;
                } else {
                    width = this.f8331a;
                    width3 = view.getWidth();
                    width2 = width3 + width;
                }
            } else if (i12 != 1) {
                width = this.f8331a - view.getWidth();
                width2 = this.f8331a + view.getWidth();
            } else if (z10) {
                width = this.f8331a;
                width3 = view.getWidth();
                width2 = width3 + width;
            } else {
                width = this.f8331a - view.getWidth();
                width2 = this.f8331a;
            }
            return SwipeDismissBehavior.f(width, i10, width2);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int b(View view, int i10, int i11) {
            return view.getTop();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int d(View view) {
            return view.getWidth();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void i(View view, int i10) {
            this.f8332b = i10;
            this.f8331a = view.getLeft();
            ViewParent parent = view.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void j(int i10) {
            c cVar = SwipeDismissBehavior.this.f8322b;
            if (cVar != null) {
                cVar.b(i10);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void k(View view, int i10, int i11, int i12, int i13) {
            float width = this.f8331a + (view.getWidth() * SwipeDismissBehavior.this.f8328h);
            float width2 = this.f8331a + (view.getWidth() * SwipeDismissBehavior.this.f8329i);
            float f10 = i10;
            if (f10 <= width) {
                view.setAlpha(1.0f);
            } else if (f10 >= width2) {
                view.setAlpha(0.0f);
            } else {
                view.setAlpha(SwipeDismissBehavior.e(0.0f, 1.0f - SwipeDismissBehavior.h(width, width2, f10), 1.0f));
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void l(View view, float f10, float f11) {
            int i10;
            boolean z10;
            c cVar;
            this.f8332b = -1;
            int width = view.getWidth();
            if (n(view, f10)) {
                int left = view.getLeft();
                int i11 = this.f8331a;
                i10 = left < i11 ? i11 - width : i11 + width;
                z10 = true;
            } else {
                i10 = this.f8331a;
                z10 = false;
            }
            if (SwipeDismissBehavior.this.f8321a.P(i10, view.getTop())) {
                ViewCompat.c0(view, new d(view, z10));
            } else {
                if (!z10 || (cVar = SwipeDismissBehavior.this.f8322b) == null) {
                    return;
                }
                cVar.a(view);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public boolean m(View view, int i10) {
            int i11 = this.f8332b;
            return (i11 == -1 || i11 == i10) && SwipeDismissBehavior.this.d(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements AccessibilityViewCommand {
        b() {
        }

        @Override // androidx.core.view.accessibility.AccessibilityViewCommand
        public boolean perform(View view, AccessibilityViewCommand.a aVar) {
            boolean z10 = false;
            if (!SwipeDismissBehavior.this.d(view)) {
                return false;
            }
            boolean z11 = ViewCompat.x(view) == 1;
            int i10 = SwipeDismissBehavior.this.f8326f;
            if ((i10 == 0 && z11) || (i10 == 1 && !z11)) {
                z10 = true;
            }
            int width = view.getWidth();
            if (z10) {
                width = -width;
            }
            ViewCompat.V(view, width);
            view.setAlpha(0.0f);
            c cVar = SwipeDismissBehavior.this.f8322b;
            if (cVar != null) {
                cVar.a(view);
            }
            return true;
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        void a(View view);

        void b(int i10);
    }

    /* loaded from: classes.dex */
    private class d implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private final View f8335e;

        /* renamed from: f, reason: collision with root package name */
        private final boolean f8336f;

        d(View view, boolean z10) {
            this.f8335e = view;
            this.f8336f = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            c cVar;
            ViewDragHelper viewDragHelper = SwipeDismissBehavior.this.f8321a;
            if (viewDragHelper != null && viewDragHelper.n(true)) {
                ViewCompat.c0(this.f8335e, this);
            } else {
                if (!this.f8336f || (cVar = SwipeDismissBehavior.this.f8322b) == null) {
                    return;
                }
                cVar.a(this.f8335e);
            }
        }
    }

    static float e(float f10, float f11, float f12) {
        return Math.min(Math.max(f10, f11), f12);
    }

    static int f(int i10, int i11, int i12) {
        return Math.min(Math.max(i10, i11), i12);
    }

    private void g(ViewGroup viewGroup) {
        ViewDragHelper p10;
        if (this.f8321a == null) {
            if (this.f8325e) {
                p10 = ViewDragHelper.o(viewGroup, this.f8324d, this.f8330j);
            } else {
                p10 = ViewDragHelper.p(viewGroup, this.f8330j);
            }
            this.f8321a = p10;
        }
    }

    static float h(float f10, float f11, float f12) {
        return (f12 - f10) / (f11 - f10);
    }

    private void m(View view) {
        ViewCompat.e0(view, 1048576);
        if (d(view)) {
            ViewCompat.g0(view, AccessibilityNodeInfoCompat.a.f2338y, null, new b());
        }
    }

    public boolean d(View view) {
        return true;
    }

    public void i(float f10) {
        this.f8329i = e(0.0f, f10, 1.0f);
    }

    public void j(c cVar) {
        this.f8322b = cVar;
    }

    public void k(float f10) {
        this.f8328h = e(0.0f, f10, 1.0f);
    }

    public void l(int i10) {
        this.f8326f = i10;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
        boolean z10 = this.f8323c;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            z10 = coordinatorLayout.F(v7, (int) motionEvent.getX(), (int) motionEvent.getY());
            this.f8323c = z10;
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.f8323c = false;
        }
        if (!z10) {
            return false;
        }
        g(coordinatorLayout);
        return this.f8321a.Q(motionEvent);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v7, int i10) {
        boolean onLayoutChild = super.onLayoutChild(coordinatorLayout, v7, i10);
        if (ViewCompat.v(v7) == 0) {
            ViewCompat.w0(v7, 1);
            m(v7);
        }
        return onLayoutChild;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper = this.f8321a;
        if (viewDragHelper == null) {
            return false;
        }
        viewDragHelper.G(motionEvent);
        return true;
    }
}
