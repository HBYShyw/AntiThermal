package com.coui.appcompat.snackbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.coui.appcompat.snackbar.COUISnackBarInterface;
import h2.COUIPressFeedbackHelper;

/* loaded from: classes.dex */
public class COUICustomSnackBar extends FrameLayout implements COUISnackBarInterface {
    private final Runnable A;

    /* renamed from: e, reason: collision with root package name */
    private ViewGroup f7657e;

    /* renamed from: f, reason: collision with root package name */
    private View f7658f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f7659g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f7660h;

    /* renamed from: i, reason: collision with root package name */
    private int f7661i;

    /* renamed from: j, reason: collision with root package name */
    private int f7662j;

    /* renamed from: k, reason: collision with root package name */
    private View f7663k;

    /* renamed from: l, reason: collision with root package name */
    private AnimatorSet f7664l;

    /* renamed from: m, reason: collision with root package name */
    private AnimatorSet f7665m;

    /* renamed from: n, reason: collision with root package name */
    private COUISnackBarInterface.d f7666n;

    /* renamed from: o, reason: collision with root package name */
    private COUISnackBarInterface.c f7667o;

    /* renamed from: p, reason: collision with root package name */
    private COUISnackBarInterface.b f7668p;

    /* renamed from: q, reason: collision with root package name */
    private COUISnackBarInterface.a f7669q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f7670r;

    /* renamed from: s, reason: collision with root package name */
    private long f7671s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f7672t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f7673u;

    /* renamed from: v, reason: collision with root package name */
    private float f7674v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f7675w;

    /* renamed from: x, reason: collision with root package name */
    private int f7676x;

    /* renamed from: y, reason: collision with root package name */
    private int f7677y;

    /* renamed from: z, reason: collision with root package name */
    private final COUIPressFeedbackHelper f7678z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Animator.AnimatorListener {
        a() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUICustomSnackBar.this.f7669q != null) {
                COUICustomSnackBar.this.f7669q.a(COUICustomSnackBar.this, animator);
            }
            COUICustomSnackBar.this.setVisibility(8);
            if (COUICustomSnackBar.this.f7657e != null) {
                COUICustomSnackBar.this.f7657e.removeView(COUICustomSnackBar.this);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUICustomSnackBar.this.f7669q != null) {
                COUICustomSnackBar.this.f7669q.b(COUICustomSnackBar.this, animator);
            }
        }
    }

    private void c() {
        if (this.f7665m == null) {
            this.f7665m = COUICustomSnackAnimUtil.a(this);
        }
        this.f7665m.addListener(new a());
        this.f7665m.start();
    }

    public void d() {
        if (this.f7659g) {
            c();
        } else {
            this.f7658f.setVisibility(8);
            ViewGroup viewGroup = this.f7657e;
            if (viewGroup != null) {
                viewGroup.removeView(this);
            }
        }
        COUISnackBarInterface.b bVar = this.f7668p;
        if (bVar != null) {
            bVar.a(this);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0013, code lost:
    
        if (r0 != 3) goto L30;
     */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        g();
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    if (this.f7673u) {
                        float y4 = motionEvent.getY() - this.f7674v;
                        int top = (int) (getTop() + y4);
                        int bottom = (int) (getBottom() + y4);
                        if (top >= this.f7676x && bottom >= this.f7677y) {
                            layout(getLeft(), top, getRight(), bottom);
                            this.f7675w = true;
                        } else {
                            layout(getLeft(), this.f7676x, getRight(), this.f7677y);
                            this.f7675w = false;
                        }
                    }
                }
            }
            if (f()) {
                this.f7678z.m(false);
            }
            if (this.f7675w) {
                d();
            }
        } else {
            this.f7676x = getTop();
            this.f7677y = getBottom();
            this.f7674v = motionEvent.getY();
            if (f()) {
                this.f7678z.m(true);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean e() {
        return this.f7670r;
    }

    public boolean f() {
        return this.f7672t;
    }

    public void g() {
        removeCallbacks(this.A);
        if (e()) {
            postDelayed(this.A, getAutoDismissTime());
        }
    }

    public long getAutoDismissTime() {
        return this.f7671s;
    }

    public View getCustomView() {
        return this.f7658f;
    }

    public AnimatorSet getDismissAnimSet() {
        return this.f7665m;
    }

    public AnimatorSet getShowAnimSet() {
        return this.f7664l;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f7657e = null;
        removeCallbacks(this.A);
    }

    public void setAutoDismiss(boolean z10) {
        this.f7670r = z10;
    }

    public void setAutoDismissTime(long j10) {
        this.f7671s = j10;
    }

    public void setDismissAnimSet(AnimatorSet animatorSet) {
        this.f7665m = animatorSet;
    }

    public void setDismissWithAnim(boolean z10) {
        this.f7659g = z10;
    }

    public void setHeight(int i10) {
        this.f7662j = i10;
    }

    public void setOnDismissAnimListener(COUISnackBarInterface.a aVar) {
        this.f7669q = aVar;
    }

    public void setOnDismissListener(COUISnackBarInterface.b bVar) {
        this.f7668p = bVar;
    }

    public void setOnShowAnimListener(COUISnackBarInterface.c cVar) {
        this.f7667o = cVar;
    }

    public void setOnShowListener(COUISnackBarInterface.d dVar) {
        this.f7666n = dVar;
    }

    protected void setParent(ViewGroup viewGroup) {
        this.f7657e = viewGroup;
    }

    public void setPressFeedBack(boolean z10) {
        this.f7672t = z10;
    }

    public void setShowAnimSet(AnimatorSet animatorSet) {
        this.f7664l = animatorSet;
    }

    public void setShowWithAnim(boolean z10) {
        this.f7660h = z10;
    }

    public void setTouchSlidable(boolean z10) {
        this.f7673u = z10;
    }

    public void setView(View view) {
        this.f7663k = view;
    }

    public void setWidth(int i10) {
        this.f7661i = i10;
    }
}
