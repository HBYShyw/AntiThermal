package com.coui.appcompat.preference;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.support.appcompat.R$color;
import m1.COUILinearInterpolator;

/* loaded from: classes.dex */
public class ListSelectedItemLayout extends COUICheckedLinearLayout {

    /* renamed from: e, reason: collision with root package name */
    protected ValueAnimator f7044e;

    /* renamed from: f, reason: collision with root package name */
    protected ValueAnimator f7045f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f7046g;

    /* renamed from: h, reason: collision with root package name */
    protected boolean f7047h;

    /* renamed from: i, reason: collision with root package name */
    private Drawable f7048i;

    /* renamed from: j, reason: collision with root package name */
    protected int f7049j;

    /* renamed from: k, reason: collision with root package name */
    protected Interpolator f7050k;

    /* renamed from: l, reason: collision with root package name */
    protected Interpolator f7051l;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ListSelectedItemLayout.this.f7048i.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
            ListSelectedItemLayout listSelectedItemLayout = ListSelectedItemLayout.this;
            listSelectedItemLayout.setBackground(listSelectedItemLayout.f7048i);
            ListSelectedItemLayout.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements Animator.AnimatorListener {
        b() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ListSelectedItemLayout listSelectedItemLayout = ListSelectedItemLayout.this;
            listSelectedItemLayout.f7049j = 1;
            if (listSelectedItemLayout.f7047h) {
                listSelectedItemLayout.f7047h = false;
                listSelectedItemLayout.f7045f.start();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ListSelectedItemLayout.this.f7048i.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
            ListSelectedItemLayout listSelectedItemLayout = ListSelectedItemLayout.this;
            listSelectedItemLayout.setBackground(listSelectedItemLayout.f7048i);
            ListSelectedItemLayout.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements Animator.AnimatorListener {
        d() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ListSelectedItemLayout.this.f7049j = 2;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    public ListSelectedItemLayout(Context context) {
        this(context, null);
    }

    protected void b(Context context) {
        if (this.f7048i == null) {
            ColorDrawable colorDrawable = new ColorDrawable(context.getResources().getColor(R$color.coui_list_color_pressed));
            this.f7048i = colorDrawable;
            colorDrawable.setAlpha(0);
            setBackground(this.f7048i);
        }
        int alpha = Color.alpha(context.getResources().getColor(R$color.coui_list_selector_color_pressed));
        ValueAnimator ofInt = ValueAnimator.ofInt(0, alpha);
        this.f7044e = ofInt;
        ofInt.setDuration(150L);
        this.f7044e.setInterpolator(this.f7051l);
        this.f7044e.addUpdateListener(new a());
        this.f7044e.addListener(new b());
        ValueAnimator ofInt2 = ValueAnimator.ofInt(alpha, 0);
        this.f7045f = ofInt2;
        ofInt2.setDuration(367L);
        this.f7045f.setInterpolator(this.f7050k);
        this.f7045f.addUpdateListener(new c());
        this.f7045f.addListener(new d());
    }

    public void c() {
        if (this.f7045f.isRunning()) {
            this.f7045f.cancel();
        }
        if (this.f7044e.isRunning()) {
            this.f7044e.cancel();
        }
        this.f7044e.start();
    }

    public void d() {
        if (this.f7044e.isRunning()) {
            this.f7047h = true;
        } else {
            if (this.f7045f.isRunning() || this.f7049j != 1) {
                return;
            }
            this.f7045f.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        b(getContext());
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isEnabled() && isClickable() && this.f7046g) {
            int action = motionEvent.getAction();
            if (action == 0) {
                c();
            } else if (action == 1 || action == 3) {
                d();
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setBackgroundAnimationDrawable(Drawable drawable) {
        this.f7048i = drawable;
    }

    public void setBackgroundAnimationEnabled(boolean z10) {
        this.f7046g = z10;
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        if (!z10 && isEnabled()) {
            d();
        }
        super.setEnabled(z10);
    }

    public ListSelectedItemLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ListSelectedItemLayout(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public ListSelectedItemLayout(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f7046g = true;
        this.f7047h = false;
        this.f7049j = 2;
        this.f7050k = new PathInterpolator(0.17f, 0.17f, 0.67f, 1.0f);
        this.f7051l = new COUILinearInterpolator();
        b(getContext());
    }
}
