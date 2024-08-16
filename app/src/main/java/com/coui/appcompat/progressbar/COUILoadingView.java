package com.coui.appcompat.progressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$string;
import com.support.appcompat.R$styleable;
import g3.COUIViewExplorerByTouchHelper;
import java.lang.ref.WeakReference;
import m1.COUILinearInterpolator;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUILoadingView extends View {
    private static final String J = COUILoadingView.class.getSimpleName();
    private Paint A;
    private float B;
    private float C;
    private float D;
    private RectF E;
    private float F;
    private float G;
    private int H;
    private COUIViewExplorerByTouchHelper.a I;

    /* renamed from: e, reason: collision with root package name */
    private float[] f7196e;

    /* renamed from: f, reason: collision with root package name */
    private int f7197f;

    /* renamed from: g, reason: collision with root package name */
    private int f7198g;

    /* renamed from: h, reason: collision with root package name */
    private int f7199h;

    /* renamed from: i, reason: collision with root package name */
    private int f7200i;

    /* renamed from: j, reason: collision with root package name */
    private int f7201j;

    /* renamed from: k, reason: collision with root package name */
    private int f7202k;

    /* renamed from: l, reason: collision with root package name */
    private int f7203l;

    /* renamed from: m, reason: collision with root package name */
    private int f7204m;

    /* renamed from: n, reason: collision with root package name */
    private float f7205n;

    /* renamed from: o, reason: collision with root package name */
    private Context f7206o;

    /* renamed from: p, reason: collision with root package name */
    private Paint f7207p;

    /* renamed from: q, reason: collision with root package name */
    private float f7208q;

    /* renamed from: r, reason: collision with root package name */
    private float f7209r;

    /* renamed from: s, reason: collision with root package name */
    private float f7210s;

    /* renamed from: t, reason: collision with root package name */
    private ValueAnimator f7211t;

    /* renamed from: u, reason: collision with root package name */
    private COUIViewExplorerByTouchHelper f7212u;

    /* renamed from: v, reason: collision with root package name */
    private String f7213v;

    /* renamed from: w, reason: collision with root package name */
    private float f7214w;

    /* renamed from: x, reason: collision with root package name */
    private float f7215x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f7216y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f7217z;

    /* loaded from: classes.dex */
    class a implements COUIViewExplorerByTouchHelper.a {

        /* renamed from: a, reason: collision with root package name */
        private int f7218a = -1;

        a() {
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public void a(int i10, Rect rect) {
            if (i10 == 0) {
                rect.set(0, 0, COUILoadingView.this.f7199h, COUILoadingView.this.f7200i);
            }
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public CharSequence b(int i10) {
            if (COUILoadingView.this.f7213v != null) {
                return COUILoadingView.this.f7213v;
            }
            return getClass().getSimpleName();
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public CharSequence c() {
            return null;
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public int d() {
            return -1;
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public int e() {
            return 1;
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public void f(int i10, int i11, boolean z10) {
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public int g(float f10, float f11) {
            return (f10 < 0.0f || f10 > ((float) COUILoadingView.this.f7199h) || f11 < 0.0f || f11 > ((float) COUILoadingView.this.f7200i)) ? -1 : 0;
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public int h() {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        WeakReference<COUILoadingView> f7220a;

        public b(COUILoadingView cOUILoadingView) {
            this.f7220a = new WeakReference<>(cOUILoadingView);
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            valueAnimator.getAnimatedFraction();
            COUILoadingView cOUILoadingView = this.f7220a.get();
            if (cOUILoadingView != null) {
                cOUILoadingView.invalidate();
            }
        }
    }

    public COUILoadingView(Context context) {
        this(context, null);
    }

    private void d() {
        ValueAnimator valueAnimator = this.f7211t;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    private void e() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f7211t = ofFloat;
        ofFloat.setDuration(480L);
        this.f7211t.setInterpolator(new COUILinearInterpolator());
        this.f7211t.addUpdateListener(new b(this));
        this.f7211t.setRepeatMode(1);
        this.f7211t.setRepeatCount(-1);
        this.f7211t.setInterpolator(new COUILinearInterpolator());
    }

    private void f() {
        ValueAnimator valueAnimator = this.f7211t;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.f7211t.removeAllListeners();
            this.f7211t.removeAllUpdateListeners();
            this.f7211t = null;
        }
    }

    private void g(Canvas canvas) {
        float f10 = this.C;
        canvas.drawCircle(f10, f10, this.F, this.A);
    }

    private void h() {
        this.B = this.f7205n / 2.0f;
        this.C = getWidth() / 2;
        this.D = getHeight() / 2;
        this.F = this.C - this.B;
        float f10 = this.C;
        float f11 = this.F;
        this.E = new RectF(f10 - f11, f10 - f11, f10 + f11, f10 + f11);
    }

    private void i() {
        Paint paint = new Paint(1);
        this.A = paint;
        paint.setColor(this.f7198g);
        this.A.setStyle(Paint.Style.STROKE);
        this.A.setStrokeWidth(this.f7205n);
    }

    private void j() {
        Paint paint = new Paint(1);
        this.f7207p = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.f7207p.setColor(this.f7197f);
        this.f7207p.setStrokeWidth(this.f7205n);
        this.f7207p.setStrokeCap(Paint.Cap.ROUND);
    }

    private void k() {
        ValueAnimator valueAnimator = this.f7211t;
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                this.f7211t.cancel();
            }
            this.f7211t.start();
        }
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.f7216y) {
            e();
            this.f7216y = true;
        }
        if (this.f7217z) {
            return;
        }
        k();
        this.f7217z = true;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        f();
        this.f7216y = false;
        this.f7217z = false;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        this.G = ((((float) SystemClock.uptimeMillis()) % 1000.0f) * 360.0f) / 1000.0f;
        g(canvas);
        canvas.save();
        canvas.rotate(-90.0f, this.C, this.D);
        if (this.E == null) {
            h();
        }
        RectF rectF = this.E;
        float f10 = this.G;
        canvas.drawArc(rectF, f10 - 30.0f, (2.0f - Math.abs((180.0f - f10) / 180.0f)) * 60.0f, false, this.f7207p);
        canvas.restore();
    }

    @Override // android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (this.E == null) {
            h();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        setMeasuredDimension(this.f7199h, this.f7200i);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        h();
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        super.onVisibilityChanged(view, i10);
        if (getVisibility() == 0) {
            if (!this.f7216y) {
                e();
                this.f7216y = true;
            }
            if (this.f7217z) {
                return;
            }
            k();
            this.f7217z = true;
            return;
        }
        d();
        this.f7217z = false;
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i10) {
        super.onWindowVisibilityChanged(i10);
        if (i10 == 0) {
            k();
        } else {
            d();
        }
    }

    public void setHeight(int i10) {
        this.f7200i = i10;
    }

    public void setLoadingType(int i10) {
        this.f7201j = i10;
    }

    public void setLoadingViewBgCircleColor(int i10) {
        this.f7198g = i10;
        i();
    }

    public void setLoadingViewColor(int i10) {
        this.f7197f = i10;
        j();
    }

    public void setWidth(int i10) {
        this.f7199h = i10;
    }

    public COUILoadingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiLoadingViewStyle);
    }

    public COUILoadingView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, R$attr.couiLoadingViewStyle, 0);
    }

    public COUILoadingView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10);
        this.f7196e = new float[6];
        this.f7199h = 0;
        this.f7200i = 0;
        this.f7201j = 1;
        this.f7210s = 60.0f;
        this.f7213v = null;
        this.f7214w = 0.1f;
        this.f7215x = 0.4f;
        this.f7216y = false;
        this.f7217z = false;
        this.I = new a();
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.H = attributeSet.getStyleAttribute();
        } else {
            this.H = i10;
        }
        this.f7206o = context;
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUILoadingView, i10, 0);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_loading_view_default_length);
        this.f7199h = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUILoadingView_couiLoadingViewWidth, dimensionPixelSize);
        this.f7200i = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUILoadingView_couiLoadingViewHeight, dimensionPixelSize);
        this.f7201j = obtainStyledAttributes.getInteger(R$styleable.COUILoadingView_couiLoadingViewType, 1);
        this.f7197f = obtainStyledAttributes.getColor(R$styleable.COUILoadingView_couiLoadingViewColor, 0);
        this.f7198g = obtainStyledAttributes.getColor(R$styleable.COUILoadingView_couiLoadingViewBgCircleColor, 0);
        obtainStyledAttributes.recycle();
        this.f7202k = context.getResources().getDimensionPixelSize(R$dimen.coui_circle_loading_strokewidth);
        this.f7203l = context.getResources().getDimensionPixelSize(R$dimen.coui_circle_loading_medium_strokewidth);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R$dimen.coui_circle_loading_large_strokewidth);
        this.f7204m = dimensionPixelSize2;
        this.f7205n = this.f7202k;
        int i12 = this.f7201j;
        if (1 == i12) {
            this.f7205n = this.f7203l;
            this.f7214w = 0.1f;
            this.f7215x = 0.4f;
        } else if (2 == i12) {
            this.f7205n = dimensionPixelSize2;
            this.f7214w = 0.215f;
            this.f7215x = 1.0f;
        }
        this.f7208q = this.f7199h >> 1;
        this.f7209r = this.f7200i >> 1;
        COUIViewExplorerByTouchHelper cOUIViewExplorerByTouchHelper = new COUIViewExplorerByTouchHelper(this);
        this.f7212u = cOUIViewExplorerByTouchHelper;
        cOUIViewExplorerByTouchHelper.c(this.I);
        ViewCompat.l0(this, this.f7212u);
        ViewCompat.w0(this, 1);
        this.f7213v = context.getString(R$string.coui_loading_view_access_string);
        j();
        i();
    }
}
