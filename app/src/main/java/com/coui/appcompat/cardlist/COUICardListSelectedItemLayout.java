package com.coui.appcompat.cardlist;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.coui.appcompat.preference.ListSelectedItemLayout;
import com.support.appcompat.R$attr;
import com.support.list.R$dimen;
import com.support.list.R$styleable;
import m2.COUIShapePath;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUICardListSelectedItemLayout extends ListSelectedItemLayout {
    private int A;
    private int B;
    private final int C;

    /* renamed from: m, reason: collision with root package name */
    private float f5562m;

    /* renamed from: n, reason: collision with root package name */
    private int f5563n;

    /* renamed from: o, reason: collision with root package name */
    private Path f5564o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f5565p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f5566q;

    /* renamed from: r, reason: collision with root package name */
    private int f5567r;

    /* renamed from: s, reason: collision with root package name */
    private int f5568s;

    /* renamed from: t, reason: collision with root package name */
    private int f5569t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f5570u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f5571v;

    /* renamed from: w, reason: collision with root package name */
    private f f5572w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f5573x;

    /* renamed from: y, reason: collision with root package name */
    private Paint f5574y;

    /* renamed from: z, reason: collision with root package name */
    private int f5575z;

    /* loaded from: classes.dex */
    class a extends ViewOutlineProvider {
        a() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setPath(COUICardListSelectedItemLayout.this.f5564o);
            COUICardListSelectedItemLayout.this.f5571v = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUICardListSelectedItemLayout.this.q(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements Animator.AnimatorListener {
        c() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ((ListSelectedItemLayout) COUICardListSelectedItemLayout.this).f7049j = 1;
            if (((ListSelectedItemLayout) COUICardListSelectedItemLayout.this).f7047h) {
                ((ListSelectedItemLayout) COUICardListSelectedItemLayout.this).f7047h = false;
                if (COUICardListSelectedItemLayout.this.f5570u) {
                    return;
                }
                ((ListSelectedItemLayout) COUICardListSelectedItemLayout.this).f7045f.start();
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
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (COUICardListSelectedItemLayout.this.f5570u) {
                ((ListSelectedItemLayout) COUICardListSelectedItemLayout.this).f7045f.cancel();
            }
            if (COUICardListSelectedItemLayout.this.f5573x) {
                COUICardListSelectedItemLayout.this.q(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements Animator.AnimatorListener {
        e() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ((ListSelectedItemLayout) COUICardListSelectedItemLayout.this).f7049j = 2;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* loaded from: classes.dex */
    public interface f {
        void a(Configuration configuration);
    }

    public COUICardListSelectedItemLayout(Context context) {
        this(context, null);
    }

    private void o(Context context, boolean z10) {
        if (!z10) {
            this.f5563n = context.getResources().getDimensionPixelOffset(R$dimen.coui_preference_card_margin_horizontal);
        } else {
            this.f5563n = context.getResources().getDimensionPixelOffset(R$dimen.coui_preference_card_margin_horizontal_tiny);
        }
        this.f5575z = COUIContextUtil.a(context, R$attr.couiColorCardBackground);
        this.f5567r = getMinimumHeight();
        this.f5568s = getPaddingTop();
        this.f5569t = getPaddingBottom();
        this.f5564o = new Path();
    }

    private void p() {
        ValueAnimator valueAnimator = this.f7044e;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.f7044e.end();
            this.f7044e = null;
        }
        ValueAnimator valueAnimator2 = this.f7045f;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.f7045f.end();
            this.f7045f = null;
        }
        if (this.f5573x) {
            this.f7044e = ObjectAnimator.ofInt(this.A, this.B);
            this.f7045f = ObjectAnimator.ofInt(this.B, this.A);
            this.f7044e.addUpdateListener(new b());
        } else {
            this.f7044e = ObjectAnimator.ofInt(this, "backgroundColor", this.A, this.B);
            this.f7045f = ObjectAnimator.ofInt(this, "backgroundColor", this.B, this.A);
        }
        this.f7044e.setDuration(150L);
        this.f7044e.setInterpolator(this.f7051l);
        this.f7044e.setEvaluator(new ArgbEvaluator());
        this.f7044e.addListener(new c());
        this.f7045f.setDuration(367L);
        this.f7045f.setInterpolator(this.f7050k);
        this.f7045f.setEvaluator(new ArgbEvaluator());
        this.f7045f.addUpdateListener(new d());
        this.f7045f.addListener(new e());
    }

    private void r() {
        this.f5564o.reset();
        RectF rectF = new RectF(this.f5563n, 0.0f, getWidth() - this.f5563n, getHeight());
        Path path = this.f5564o;
        float f10 = this.f5562m;
        boolean z10 = this.f5565p;
        boolean z11 = this.f5566q;
        this.f5564o = COUIShapePath.b(path, rectF, f10, z10, z10, z11, z11);
    }

    private void setCardRadiusStyle(int i10) {
        if (i10 == 4) {
            this.f5565p = true;
            this.f5566q = true;
        } else if (i10 == 1) {
            this.f5565p = true;
            this.f5566q = false;
        } else if (i10 == 3) {
            this.f5565p = false;
            this.f5566q = true;
        } else {
            this.f5565p = false;
            this.f5566q = false;
        }
    }

    private void setPadding(int i10) {
        int i11;
        if (i10 == 1) {
            r0 = this.C;
            i11 = 0;
        } else if (i10 == 3) {
            i11 = this.C;
        } else {
            r0 = i10 == 4 ? this.C : 0;
            i11 = r0;
        }
        setMinimumHeight(this.f5567r + r0 + i11);
        setPaddingRelative(getPaddingStart(), this.f5568s + r0, getPaddingEnd(), this.f5569t + i11);
    }

    @Override // com.coui.appcompat.preference.ListSelectedItemLayout
    protected void b(Context context) {
        this.A = COUIContextUtil.a(context, R$attr.couiColorCardBackground);
        int a10 = COUIContextUtil.a(context, R$attr.couiColorCardPressed);
        this.B = a10;
        if (this.f5570u) {
            q(a10);
        } else {
            q(this.A);
        }
        p();
    }

    @Override // com.coui.appcompat.preference.ListSelectedItemLayout
    public void c() {
        if (this.f5570u) {
            return;
        }
        super.c();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (!this.f5573x && !this.f5571v) {
            canvas.save();
            canvas.clipPath(this.f5564o);
            super.draw(canvas);
            canvas.restore();
            return;
        }
        super.draw(canvas);
    }

    public boolean getIsSelected() {
        return this.f5570u;
    }

    public int getMarginHorizontal() {
        return this.f5563n;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.preference.ListSelectedItemLayout, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        f fVar = this.f5572w;
        if (fVar != null) {
            fVar.a(configuration);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.f5573x) {
            this.f5574y.setColor(this.f5575z);
            canvas.drawPath(this.f5564o, this.f5574y);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        r();
        if (this.f5573x) {
            return;
        }
        setOutlineProvider(new a());
        setClipToOutline(true);
    }

    public void q(int i10) {
        this.f5575z = i10;
        if (this.f5573x) {
            invalidate();
        } else {
            setBackgroundColor(i10);
        }
    }

    public void setConfigurationChangeListener(f fVar) {
        this.f5572w = fVar;
    }

    public void setIsSelected(boolean z10) {
        if (this.f5570u != z10) {
            this.f5570u = z10;
            if (!z10) {
                q(COUIContextUtil.a(getContext(), R$attr.couiColorCardBackground));
                return;
            }
            ValueAnimator valueAnimator = this.f7044e;
            if (valueAnimator == null || !valueAnimator.isRunning()) {
                q(COUIContextUtil.a(getContext(), R$attr.couiColorCardPressed));
            }
        }
    }

    public void setMarginHorizontal(int i10) {
        this.f5563n = i10;
        requestLayout();
    }

    public void setPositionInGroup(int i10) {
        if (i10 > 0) {
            setPadding(i10);
            setCardRadiusStyle(i10);
            r();
        }
    }

    public COUICardListSelectedItemLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICardListSelectedItemLayout(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUICardListSelectedItemLayout(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f5565p = true;
        this.f5566q = true;
        this.f5571v = false;
        this.f5574y = new Paint();
        this.C = getResources().getDimensionPixelOffset(R$dimen.coui_list_card_head_or_tail_padding);
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICardListSelectedItemLayout, i10, i11);
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.COUICardListSelectedItemLayout_listIsTiny, false);
        this.f5562m = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUICardListSelectedItemLayout_couiCardRadius, context.getResources().getDimensionPixelOffset(R$dimen.coui_preference_card_radius));
        o(getContext(), z10);
        this.f5563n = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUICardListSelectedItemLayout_couiCardListHorizontalMargin, this.f5563n);
        obtainStyledAttributes.recycle();
    }
}
