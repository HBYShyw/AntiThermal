package com.coui.appcompat.seekbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.support.nearx.R$attr;
import com.support.nearx.R$color;
import com.support.nearx.R$dimen;
import com.support.nearx.R$drawable;
import com.support.nearx.R$styleable;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUIIconSeekBar extends View {
    private int A;
    private String B;
    private float C;
    private boolean D;
    private RectF E;
    private Bitmap F;
    private int G;

    /* renamed from: e, reason: collision with root package name */
    protected int f7519e;

    /* renamed from: f, reason: collision with root package name */
    protected float f7520f;

    /* renamed from: g, reason: collision with root package name */
    protected int f7521g;

    /* renamed from: h, reason: collision with root package name */
    protected int f7522h;

    /* renamed from: i, reason: collision with root package name */
    protected boolean f7523i;

    /* renamed from: j, reason: collision with root package name */
    protected RectF f7524j;

    /* renamed from: k, reason: collision with root package name */
    protected AnimatorSet f7525k;

    /* renamed from: l, reason: collision with root package name */
    protected float f7526l;

    /* renamed from: m, reason: collision with root package name */
    protected Paint f7527m;

    /* renamed from: n, reason: collision with root package name */
    protected Interpolator f7528n;

    /* renamed from: o, reason: collision with root package name */
    private int f7529o;

    /* renamed from: p, reason: collision with root package name */
    private OnSeekBarChangeListener f7530p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f7531q;

    /* renamed from: r, reason: collision with root package name */
    private float f7532r;

    /* renamed from: s, reason: collision with root package name */
    protected float f7533s;

    /* renamed from: t, reason: collision with root package name */
    private float f7534t;

    /* renamed from: u, reason: collision with root package name */
    private RectF f7535u;

    /* renamed from: v, reason: collision with root package name */
    private com.coui.appcompat.seekbar.b f7536v;

    /* renamed from: w, reason: collision with root package name */
    protected float f7537w;

    /* renamed from: x, reason: collision with root package name */
    private float f7538x;

    /* renamed from: y, reason: collision with root package name */
    private float f7539y;

    /* renamed from: z, reason: collision with root package name */
    private final Interpolator f7540z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Animator.AnimatorListener {
        a() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (COUIIconSeekBar.this.f7530p != null) {
                OnSeekBarChangeListener onSeekBarChangeListener = COUIIconSeekBar.this.f7530p;
                COUIIconSeekBar cOUIIconSeekBar = COUIIconSeekBar.this;
                onSeekBarChangeListener.c(cOUIIconSeekBar, cOUIIconSeekBar.f7521g, true);
            }
            COUIIconSeekBar.this.u();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUIIconSeekBar.this.f7530p != null) {
                OnSeekBarChangeListener onSeekBarChangeListener = COUIIconSeekBar.this.f7530p;
                COUIIconSeekBar cOUIIconSeekBar = COUIIconSeekBar.this;
                onSeekBarChangeListener.c(cOUIIconSeekBar, cOUIIconSeekBar.f7521g, true);
            }
            COUIIconSeekBar.this.u();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIIconSeekBar.this.t();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ float f7542a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f7543b;

        b(float f10, int i10) {
            this.f7542a = f10;
            this.f7543b = i10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            COUIIconSeekBar cOUIIconSeekBar = COUIIconSeekBar.this;
            cOUIIconSeekBar.f7521g = (int) (floatValue / this.f7542a);
            cOUIIconSeekBar.C = floatValue / this.f7543b;
            COUIIconSeekBar.this.invalidate();
        }
    }

    public COUIIconSeekBar(Context context) {
        this(context, null);
    }

    private void A(MotionEvent motionEvent) {
        float round = Math.round(((motionEvent.getX() - this.f7526l) * f(motionEvent.getX())) + this.f7526l);
        int g6 = g(round);
        int i10 = this.f7521g;
        if (g6 != i10) {
            this.f7526l = round;
            OnSeekBarChangeListener onSeekBarChangeListener = this.f7530p;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.c(this, i10, true);
            }
            v();
        }
    }

    private void e() {
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).requestDisallowInterceptTouchEvent(true);
        }
    }

    private float f(float f10) {
        float seekBarWidth = getSeekBarWidth();
        float f11 = seekBarWidth / 2.0f;
        float interpolation = 1.0f - this.f7540z.getInterpolation(Math.abs(f10 - f11) / f11);
        return (f10 > seekBarWidth - ((float) getPaddingRight()) || f10 < ((float) getPaddingLeft()) || interpolation < this.f7539y) ? this.f7539y : interpolation;
    }

    private int g(float f10) {
        float progressLeftX;
        float f11;
        float f12;
        int progressRightX = getProgressRightX() - getProgressLeftX();
        if (isLayoutRtl()) {
            if (f10 <= getProgressRightX()) {
                if (f10 >= getProgressLeftX()) {
                    f11 = progressRightX;
                    progressLeftX = (f11 - f10) + getProgressLeftX();
                    f12 = progressLeftX / f11;
                }
                f12 = 1.0f;
            }
            f12 = 0.0f;
        } else {
            if (f10 >= getProgressLeftX()) {
                if (f10 <= getProgressRightX()) {
                    progressLeftX = f10 - getProgressLeftX();
                    f11 = progressRightX;
                    f12 = progressLeftX / f11;
                }
                f12 = 1.0f;
            }
            f12 = 0.0f;
        }
        this.C = Math.min(f12, 1.0f);
        float max = 0.0f + (f12 * getMax());
        int i10 = this.f7521g;
        this.f7521g = m(Math.round(max));
        invalidate();
        return i10;
    }

    private int getProgressLeftX() {
        return Math.round(this.f7535u.left + 72.0f + 36.0f + 24.0f);
    }

    private int getProgressRightX() {
        return Math.round(this.f7535u.right - 36.0f);
    }

    private void j(Canvas canvas) {
        this.f7527m.setColor(-1);
        int i10 = (int) (this.f7535u.left + 36.0f + 36.0f);
        Matrix matrix = new Matrix();
        matrix.setScale(72.0f / this.F.getWidth(), 72.0f / this.F.getHeight());
        float f10 = i10;
        matrix.postRotate(this.f7521g * 2, f10, this.f7535u.height() / 2.0f);
        Bitmap bitmap = this.F;
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), this.F.getHeight(), matrix, true);
        Rect rect = new Rect(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
        RectF rectF = new RectF();
        float width = createBitmap.getWidth() >> 1;
        float f11 = f10 - width;
        float f12 = f10 + width;
        float height = ((this.f7535u.height() - createBitmap.getHeight()) / 2.0f) + getPaddingTop();
        rectF.set(f11, height, f12, createBitmap.getHeight() + height);
        canvas.drawBitmap(createBitmap, rect, rectF, this.f7527m);
    }

    private void k(Canvas canvas) {
        Bitmap bitmap;
        this.f7527m.setColor(-1);
        int i10 = this.f7521g;
        if (i10 == 0) {
            bitmap = ((BitmapDrawable) getResources().getDrawable(R$drawable.ic_volume_seekbar_close)).getBitmap();
        } else if (i10 > 0 && i10 <= (this.f7522h >> 1)) {
            bitmap = ((BitmapDrawable) getResources().getDrawable(R$drawable.ic_volume_seekbar_middle)).getBitmap();
        } else {
            bitmap = ((BitmapDrawable) getResources().getDrawable(R$drawable.ic_volume_seekbar_open)).getBitmap();
        }
        Bitmap bitmap2 = bitmap;
        Matrix matrix = new Matrix();
        matrix.setScale(72.0f / this.F.getWidth(), 72.0f / this.F.getHeight());
        Bitmap createBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
        Rect rect = new Rect(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
        RectF rectF = new RectF();
        float f10 = this.f7535u.left + 36.0f;
        float height = ((this.f7535u.height() - createBitmap.getHeight()) / 2.0f) + getPaddingTop();
        rectF.set(f10, height, createBitmap.getWidth() + f10, createBitmap.getHeight() + height);
        canvas.drawBitmap(createBitmap, rect, rectF, this.f7527m);
    }

    private void l() {
        this.f7533s = this.f7532r;
        this.f7538x = this.f7537w;
    }

    private int m(int i10) {
        return Math.max(0, Math.min(i10, this.f7522h));
    }

    private void q() {
        this.f7519e = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        com.coui.appcompat.seekbar.b bVar = new com.coui.appcompat.seekbar.b(this);
        this.f7536v = bVar;
        ViewCompat.l0(this, bVar);
        ViewCompat.w0(this, 1);
        this.f7536v.invalidateRoot();
        Paint paint = new Paint();
        this.f7527m = paint;
        paint.setAntiAlias(true);
        this.f7527m.setDither(true);
        this.F = ((BitmapDrawable) getResources().getDrawable(R$drawable.ic_brightness_seekbar)).getBitmap();
    }

    private void r(MotionEvent motionEvent) {
        int i10 = this.f7521g;
        float seekBarWidth = getSeekBarWidth();
        if (isLayoutRtl()) {
            int i11 = this.f7522h;
            this.f7521g = i11 - Math.round((i11 * ((motionEvent.getX() - getProgressLeftX()) - this.f7534t)) / seekBarWidth);
        } else {
            this.f7521g = Math.round((this.f7522h * ((motionEvent.getX() - getProgressLeftX()) - this.f7534t)) / seekBarWidth);
        }
        int m10 = m(this.f7521g);
        this.f7521g = m10;
        if (i10 != m10) {
            OnSeekBarChangeListener onSeekBarChangeListener = this.f7530p;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.c(this, m10, true);
            }
            v();
        }
        invalidate();
    }

    private int s(int i10, int i11) {
        return View.MeasureSpec.getMode(i10) != 1073741824 ? i11 : View.MeasureSpec.getSize(i10);
    }

    protected void c(float f10) {
        int round;
        float seekBarWidth = getSeekBarWidth();
        if (isLayoutRtl()) {
            int i10 = this.f7522h;
            round = i10 - Math.round((i10 * (((f10 - this.E.left) - getPaddingLeft()) - this.f7534t)) / seekBarWidth);
        } else {
            round = Math.round((this.f7522h * (((f10 - this.E.left) - getPaddingLeft()) - this.f7534t)) / seekBarWidth);
        }
        d(m(round));
    }

    protected void d(int i10) {
        AnimatorSet animatorSet = this.f7525k;
        if (animatorSet == null) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.f7525k = animatorSet2;
            animatorSet2.addListener(new a());
        } else {
            animatorSet.cancel();
        }
        int i11 = this.f7521g;
        int seekBarWidth = getSeekBarWidth();
        float f10 = seekBarWidth / this.f7522h;
        if (f10 > 0.0f) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(i11 * f10, i10 * f10);
            ofFloat.setInterpolator(this.f7528n);
            ofFloat.addUpdateListener(new b(f10, seekBarWidth));
            long abs = (Math.abs(i10 - i11) / this.f7522h) * 483.0f;
            if (abs < 150) {
                abs = 150;
            }
            this.f7525k.setDuration(abs);
            this.f7525k.play(ofFloat);
            this.f7525k.start();
        }
    }

    public int getIncrement() {
        return this.f7529o;
    }

    public int getMax() {
        return this.f7522h;
    }

    public int getProgress() {
        return this.f7521g;
    }

    public String getProgressContentDescription() {
        return this.B;
    }

    protected int getSeekBarWidth() {
        return (int) this.E.width();
    }

    public int getType() {
        return this.G;
    }

    protected void h(Canvas canvas, float f10) {
        float f11;
        float f12;
        float height = (this.f7535u.height() / 2.0f) + getPaddingTop();
        if (isLayoutRtl()) {
            f11 = getProgressRightX();
            f12 = f11 - (this.C * f10);
        } else {
            float progressLeftX = getProgressLeftX();
            f11 = progressLeftX + (this.C * f10);
            f12 = progressLeftX;
        }
        if (f12 <= f11) {
            RectF rectF = this.f7524j;
            float f13 = this.f7533s;
            rectF.set(f12, height - f13, f11, height + f13);
        } else {
            RectF rectF2 = this.f7524j;
            float f14 = this.f7533s;
            rectF2.set(f11, height - f14, f12, height + f14);
        }
        this.f7527m.setColor(ContextCompat.c(getContext(), R$color.coui_icon_seekbar_def_progress_color));
        RectF rectF3 = this.f7524j;
        float f15 = this.f7538x;
        canvas.drawRoundRect(rectF3, f15, f15, this.f7527m);
        int i10 = this.f7521g;
        if (i10 == this.f7522h || i10 <= this.f7538x) {
            return;
        }
        if (isLayoutRtl()) {
            RectF rectF4 = this.f7524j;
            canvas.drawRect(rectF4.left, rectF4.top, rectF4.right - this.f7538x, rectF4.bottom, this.f7527m);
        } else {
            RectF rectF5 = this.f7524j;
            canvas.drawRect(rectF5.left + this.f7538x, rectF5.top, rectF5.right, rectF5.bottom, this.f7527m);
        }
    }

    protected void i(Canvas canvas) {
        this.f7527m.setColor(ContextCompat.c(getContext(), R$color.coui_icon_seekbar_background));
        this.f7535u.set((getWidth() >> 1) - 204.0f, getPaddingTop(), (getWidth() >> 1) + 204.0f, getPaddingTop() + 96.0f);
        canvas.drawRoundRect(this.f7535u, 90.0f, 90.0f, this.f7527m);
        if (getType() == 0) {
            j(canvas);
        } else {
            k(canvas);
        }
        this.f7527m.setColor(ContextCompat.c(getContext(), R$color.coui_icon_seekbar_background_color_normal));
        float height = (this.f7535u.height() / 2.0f) + getPaddingTop();
        float progressLeftX = getProgressLeftX();
        float progressRightX = getProgressRightX();
        RectF rectF = this.E;
        float f10 = this.f7538x;
        rectF.set(progressLeftX, height - f10, progressRightX, height + f10);
        RectF rectF2 = this.E;
        float f11 = this.f7538x;
        canvas.drawRoundRect(rectF2, f11, f11, this.f7527m);
        h(canvas, this.E.width());
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    protected void n(MotionEvent motionEvent) {
        this.f7520f = motionEvent.getX();
        this.f7526l = motionEvent.getX();
    }

    protected void o(MotionEvent motionEvent) {
        float seekBarWidth = getSeekBarWidth();
        if ((this.f7521g * seekBarWidth) / this.f7522h != seekBarWidth / 2.0f || Math.abs(motionEvent.getX() - this.f7526l) >= 20.0f) {
            if (this.f7523i && this.f7531q) {
                A(motionEvent);
                return;
            }
            if (z(motionEvent)) {
                float x10 = motionEvent.getX();
                if (Math.abs(x10 - this.f7520f) > this.f7519e) {
                    y();
                    this.f7526l = x10;
                    r(motionEvent);
                }
            }
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        i(canvas);
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        setMeasuredDimension(s(i10, Math.round(408.0f)), s(i11, Math.round(96.0f)));
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        this.f7531q = false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0015, code lost:
    
        if (r0 != 3) goto L16;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    o(motionEvent);
                }
            }
            p(motionEvent);
        } else {
            this.f7523i = false;
            this.f7531q = false;
            n(motionEvent);
        }
        return true;
    }

    protected void p(MotionEvent motionEvent) {
        if (this.f7523i) {
            u();
            setPressed(false);
        } else if (z(motionEvent)) {
            c(motionEvent.getX());
        }
    }

    public void setIncrement(int i10) {
        this.f7529o = Math.abs(i10);
    }

    public void setMax(int i10) {
        if (i10 < 0) {
            i10 = 0;
        }
        if (i10 != this.f7522h) {
            this.f7522h = i10;
            if (this.f7521g > i10) {
                this.f7521g = i10;
            }
        }
        invalidate();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.f7530p = onSeekBarChangeListener;
    }

    public void setProgress(int i10) {
        w(i10, false);
    }

    public void setProgressContentDescription(String str) {
        this.B = str;
    }

    public void setType(int i10) {
        this.G = i10;
        invalidate();
    }

    public void setVibratorEnable(boolean z10) {
        this.D = z10;
    }

    void t() {
        this.f7523i = true;
        this.f7531q = true;
        OnSeekBarChangeListener onSeekBarChangeListener = this.f7530p;
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.a(this);
        }
    }

    void u() {
        this.f7523i = false;
        this.f7531q = false;
        OnSeekBarChangeListener onSeekBarChangeListener = this.f7530p;
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.b(this);
        }
    }

    protected void v() {
        if (this.D) {
            if (this.f7521g != getMax() && this.f7521g != 0) {
                performHapticFeedback(305, 0);
            } else {
                performHapticFeedback(306, 0);
            }
        }
    }

    public void w(int i10, boolean z10) {
        x(i10, z10, false);
    }

    public void x(int i10, boolean z10, boolean z11) {
        int i11 = this.f7521g;
        int max = Math.max(0, Math.min(i10, this.f7522h));
        if (i11 != max) {
            if (z10) {
                d(max);
            } else {
                this.f7521g = max;
                this.C = max / this.f7522h;
                OnSeekBarChangeListener onSeekBarChangeListener = this.f7530p;
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.c(this, max, z11);
                }
                invalidate();
            }
            v();
        }
    }

    protected void y() {
        setPressed(true);
        t();
        e();
    }

    protected boolean z(MotionEvent motionEvent) {
        float x10 = motionEvent.getX();
        float y4 = motionEvent.getY();
        RectF rectF = this.f7535u;
        return x10 >= rectF.left && x10 <= rectF.right && y4 >= rectF.top && y4 <= rectF.bottom;
    }

    public COUIIconSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiIconSeekBarStyle);
    }

    public COUIIconSeekBar(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7519e = 0;
        this.f7521g = 0;
        this.f7522h = 100;
        this.f7523i = false;
        this.f7524j = new RectF();
        this.f7528n = PathInterpolatorCompat.a(0.3f, 0.0f, 0.1f, 1.0f);
        this.f7529o = 1;
        this.f7531q = false;
        this.f7535u = new RectF();
        this.f7539y = 0.4f;
        this.f7540z = PathInterpolatorCompat.a(0.3f, 0.0f, 0.1f, 1.0f);
        this.C = 0.0f;
        this.D = false;
        this.E = new RectF();
        if (attributeSet != null) {
            this.A = attributeSet.getStyleAttribute();
        }
        if (this.A == 0) {
            this.A = i10;
        }
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIIconSeekBar, i10, 0);
        this.f7534t = getResources().getDimensionPixelSize(R$dimen.coui_icon_seekbar_progress_scale_radius);
        this.f7532r = getResources().getDimensionPixelSize(R$dimen.coui_icon_seekbar_progress_radius);
        this.f7537w = getResources().getDimensionPixelSize(R$dimen.coui_icon_seekbar_intent_background_radius);
        this.G = obtainStyledAttributes.getInteger(R$styleable.COUIIconSeekBar_couiIconSeekBarType, 0);
        this.f7522h = obtainStyledAttributes.getInteger(R$styleable.COUIIconSeekBar_couiIconSeekBarMax, 100);
        int integer = obtainStyledAttributes.getInteger(R$styleable.COUIIconSeekBar_couiIconSeekBarProgress, 0);
        this.f7521g = integer;
        this.C = integer / this.f7522h;
        obtainStyledAttributes.recycle();
        q();
        l();
    }
}
