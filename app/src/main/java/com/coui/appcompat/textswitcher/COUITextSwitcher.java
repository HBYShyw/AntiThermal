package com.coui.appcompat.textswitcher;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.PathInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.coui.appcompat.textswitcher.COUITextSwitcher;
import com.support.appcompat.R$anim;
import com.support.appcompat.R$styleable;
import m1.COUIAnimationListenerAdapter;
import m1.COUIMoveEaseInterpolator;

/* loaded from: classes.dex */
public class COUITextSwitcher extends TextSwitcher implements ViewSwitcher.ViewFactory {

    /* renamed from: r, reason: collision with root package name */
    private static final PathInterpolator f7871r = new COUIMoveEaseInterpolator();

    /* renamed from: e, reason: collision with root package name */
    private Context f7872e;

    /* renamed from: f, reason: collision with root package name */
    private int f7873f;

    /* renamed from: g, reason: collision with root package name */
    private int f7874g;

    /* renamed from: h, reason: collision with root package name */
    private int f7875h;

    /* renamed from: i, reason: collision with root package name */
    private int f7876i;

    /* renamed from: j, reason: collision with root package name */
    private int f7877j;

    /* renamed from: k, reason: collision with root package name */
    private int f7878k;

    /* renamed from: l, reason: collision with root package name */
    private int f7879l;

    /* renamed from: m, reason: collision with root package name */
    private ValueAnimator f7880m;

    /* renamed from: n, reason: collision with root package name */
    private ValueAnimator f7881n;

    /* renamed from: o, reason: collision with root package name */
    private float f7882o;

    /* renamed from: p, reason: collision with root package name */
    private float f7883p;

    /* renamed from: q, reason: collision with root package name */
    private CharSequence f7884q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends COUIAnimationListenerAdapter {
        a() {
        }

        @Override // m1.COUIAnimationListenerAdapter, android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            if (COUITextSwitcher.this.f7881n != null) {
                COUITextSwitcher.this.f7881n.end();
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            if (COUITextSwitcher.this.f7881n != null) {
                COUITextSwitcher.this.f7881n.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends COUIAnimationListenerAdapter {
        b() {
        }

        @Override // m1.COUIAnimationListenerAdapter, android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            if (COUITextSwitcher.this.f7880m != null) {
                COUITextSwitcher.this.f7880m.end();
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            if (COUITextSwitcher.this.f7880m != null) {
                COUITextSwitcher.this.f7880m.start();
            }
        }
    }

    public COUITextSwitcher(Context context) {
        this(context, null);
    }

    private void e(boolean z10) {
        if (this.f7880m == null || z10) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.f7880m = ofFloat;
            ofFloat.setDuration(300L);
            this.f7880m.setInterpolator(f7871r);
            this.f7880m.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: y2.b
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    COUITextSwitcher.this.f(valueAnimator);
                }
            });
        }
        if (this.f7881n == null || z10) {
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.0f, 0.0f);
            this.f7881n = ofFloat2;
            ofFloat2.setDuration(300L);
            this.f7881n.setStartDelay(300L);
            this.f7881n.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: y2.a
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    COUITextSwitcher.this.g(valueAnimator);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue() * this.f7882o;
        if (floatValue > 0.0f) {
            m(floatValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue() * this.f7882o;
        if (floatValue > 0.0f) {
            l(floatValue);
        } else {
            h();
        }
    }

    private void i() {
        int i10 = this.f7878k;
        if (i10 != 0) {
            if (i10 == 1) {
                e(false);
                j();
                k();
                return;
            } else if (i10 == 2) {
                setInAnimation(this.f7872e, R$anim.coui_trans_in);
                setOutAnimation(this.f7872e, R$anim.coui_trans_out);
                return;
            } else {
                if (i10 != 3) {
                    return;
                }
                setInAnimation(this.f7872e, R$anim.coui_alpha_in);
                setOutAnimation(this.f7872e, R$anim.coui_alpha_out);
                return;
            }
        }
        int i11 = this.f7879l;
        if (i11 == 0) {
            setInAnimation(this.f7872e, R$anim.coui_trans_alpha_up_in);
            setOutAnimation(this.f7872e, R$anim.coui_trans_alpha_up_out);
            return;
        }
        if (i11 == 1) {
            setInAnimation(this.f7872e, R$anim.coui_trans_alpha_down_in);
            setOutAnimation(this.f7872e, R$anim.coui_trans_alpha_down_out);
        } else if (i11 == 2) {
            setInAnimation(this.f7872e, R$anim.coui_trans_alpha_left_in);
            setOutAnimation(this.f7872e, R$anim.coui_trans_alpha_left_out);
        } else {
            if (i11 != 3) {
                return;
            }
            setInAnimation(this.f7872e, R$anim.coui_trans_alpha_right_in);
            setOutAnimation(this.f7872e, R$anim.coui_trans_alpha_out_right);
        }
    }

    private void j() {
        AnimationSet animationSet = new AnimationSet(true);
        float f10 = this.f7883p;
        ScaleAnimation scaleAnimation = new ScaleAnimation(f10, 1.0f, f10, 1.0f, 1, 0.5f, 1, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        animationSet.setInterpolator(f7871r);
        animationSet.setStartOffset(300L);
        animationSet.setDuration(300L);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new a());
        setInAnimation(animationSet);
    }

    private void k() {
        AnimationSet animationSet = new AnimationSet(true);
        float f10 = this.f7883p;
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, f10, 1.0f, f10, 1, 0.5f, 1, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        animationSet.setInterpolator(f7871r);
        animationSet.setDuration(300L);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new b());
        setOutAnimation(animationSet);
    }

    private void l(float f10) {
        getCurrentView().setRenderEffect(RenderEffect.createBlurEffect(f10, f10, Shader.TileMode.CLAMP));
    }

    private void m(float f10) {
        getPreviousView().setRenderEffect(RenderEffect.createBlurEffect(f10, f10, Shader.TileMode.CLAMP));
    }

    public View getPreviousView() {
        int displayedChild = getDisplayedChild() - 1;
        if (displayedChild >= getChildCount()) {
            displayedChild = 0;
        } else if (displayedChild < 0) {
            displayedChild = getChildCount() - 1;
        }
        return getChildAt(displayedChild);
    }

    public void h() {
        getCurrentView().setRenderEffect(null);
    }

    @Override // android.widget.ViewSwitcher.ViewFactory
    public View makeView() {
        TextView textView;
        int i10 = this.f7876i;
        if (i10 != 0 && i10 != 1) {
            textView = new SpacingTextView(this.f7872e);
        } else {
            textView = new TextView(this.f7872e);
        }
        CharSequence charSequence = this.f7884q;
        if (charSequence != null) {
            textView.setText(charSequence);
        }
        int i11 = this.f7873f;
        if (i11 != -1) {
            textView.setTextSize(0, i11);
        }
        int i12 = this.f7874g;
        if (i12 != 0) {
            textView.setTextColor(i12);
        }
        int i13 = this.f7875h;
        if (i13 == 1) {
            textView.setEllipsize(TextUtils.TruncateAt.START);
        } else if (i13 == 2) {
            textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        } else if (i13 == 3) {
            textView.setEllipsize(TextUtils.TruncateAt.END);
        } else if (i13 == 4) {
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }
        int i14 = this.f7876i;
        if (i14 == 1) {
            textView.setTypeface(Typeface.defaultFromStyle(1));
        } else if (i14 == 2) {
            textView.setTypeface(Typeface.defaultFromStyle(2));
        } else if (i14 == 3) {
            textView.setTypeface(Typeface.defaultFromStyle(3));
        }
        int i15 = this.f7877j;
        if (i15 != 0) {
            textView.setTextAppearance(this.f7872e, i15);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    public void setAnimDirection(int i10) {
        if (this.f7879l != i10) {
            this.f7879l = i10;
            i();
        }
    }

    public void setAnimEffect(int i10) {
        if (this.f7878k != i10) {
            this.f7878k = i10;
            i();
        }
    }

    public void setBlurRadius(int i10) {
        if (this.f7878k != 1) {
            Log.d("COUITextSwitcher", "You can not set blur radius for the anim effect not contain blur anim");
            return;
        }
        float f10 = i10;
        if (f10 != this.f7882o) {
            this.f7882o = f10;
            e(true);
        }
    }

    public void setScale(float f10) {
        if (this.f7878k != 1) {
            Log.d("COUITextSwitcher", "You can not set scale for the anim effect not contain blur anim");
        } else if (f10 != this.f7883p) {
            this.f7883p = f10;
            k();
            j();
        }
    }

    public COUITextSwitcher(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7873f = -1;
        this.f7874g = 0;
        this.f7872e = context;
        int[] iArr = R$styleable.COUITextSwitcher;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        saveAttributeDataForStyleable(context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        this.f7884q = obtainStyledAttributes.getText(R$styleable.COUITextSwitcher_couiText);
        this.f7873f = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITextSwitcher_couiTextSize, this.f7873f);
        this.f7874g = obtainStyledAttributes.getColor(R$styleable.COUITextSwitcher_couiTextColor, this.f7874g);
        this.f7875h = obtainStyledAttributes.getInt(R$styleable.COUITextSwitcher_couiEllipsize, 0);
        this.f7876i = obtainStyledAttributes.getInt(R$styleable.COUITextSwitcher_couiTextStyle, 0);
        this.f7877j = obtainStyledAttributes.getResourceId(R$styleable.COUITextSwitcher_couiSupportTextAppearance, 0);
        if (getInAnimation() == null && getOutAnimation() == null) {
            this.f7878k = obtainStyledAttributes.getInt(R$styleable.COUITextSwitcher_couiAnimationEffect, 3);
            this.f7879l = obtainStyledAttributes.getInt(R$styleable.COUITextSwitcher_couiAnimationDirection, 0);
            this.f7882o = obtainStyledAttributes.getFloat(R$styleable.COUITextSwitcher_couiBlurRadius, 10.0f);
            this.f7883p = obtainStyledAttributes.getFloat(R$styleable.COUITextSwitcher_couiScale, 1.22f);
            i();
        }
        obtainStyledAttributes.recycle();
        setFactory(this);
    }
}
