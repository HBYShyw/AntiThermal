package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$styleable;
import java.util.ArrayList;
import w1.COUIDarkModeUtil;

@Deprecated
/* loaded from: classes.dex */
public class COUICircleProgressBar extends View {
    private Paint A;
    private int B;
    private int C;
    private RectF D;
    private float E;
    private int F;

    /* renamed from: e, reason: collision with root package name */
    private int f7077e;

    /* renamed from: f, reason: collision with root package name */
    private int f7078f;

    /* renamed from: g, reason: collision with root package name */
    private int f7079g;

    /* renamed from: h, reason: collision with root package name */
    private int f7080h;

    /* renamed from: i, reason: collision with root package name */
    private int f7081i;

    /* renamed from: j, reason: collision with root package name */
    private int f7082j;

    /* renamed from: k, reason: collision with root package name */
    private int f7083k;

    /* renamed from: l, reason: collision with root package name */
    private int f7084l;

    /* renamed from: m, reason: collision with root package name */
    private int f7085m;

    /* renamed from: n, reason: collision with root package name */
    private int f7086n;

    /* renamed from: o, reason: collision with root package name */
    private int f7087o;

    /* renamed from: p, reason: collision with root package name */
    private int f7088p;

    /* renamed from: q, reason: collision with root package name */
    private int f7089q;

    /* renamed from: r, reason: collision with root package name */
    private int f7090r;

    /* renamed from: s, reason: collision with root package name */
    private float f7091s;

    /* renamed from: t, reason: collision with root package name */
    private float f7092t;

    /* renamed from: u, reason: collision with root package name */
    private float f7093u;

    /* renamed from: v, reason: collision with root package name */
    private Context f7094v;

    /* renamed from: w, reason: collision with root package name */
    private b f7095w;

    /* renamed from: x, reason: collision with root package name */
    private AccessibilityManager f7096x;

    /* renamed from: y, reason: collision with root package name */
    private Paint f7097y;

    /* renamed from: z, reason: collision with root package name */
    private ArrayList<c> f7098z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f7099e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        public String toString() {
            return "COUICircleProgressBar.SavedState { " + Integer.toHexString(System.identityHashCode(this)) + " mProgress = " + this.f7099e + " }";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeValue(Integer.valueOf(this.f7099e));
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f7099e = ((Integer) parcel.readValue(null)).intValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        private b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUICircleProgressBar.this.sendAccessibilityEvent(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class c {
        public c() {
        }
    }

    public COUICircleProgressBar(Context context) {
        this(context, null);
    }

    private void a(Canvas canvas) {
        this.A.setStrokeWidth(this.f7082j);
        int i10 = this.C;
        canvas.drawCircle(i10, i10, this.E, this.A);
    }

    private void b() {
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        for (int i10 = 0; i10 < 360; i10++) {
            this.f7098z.add(new c());
        }
        c();
        d();
        setProgress(this.f7085m);
        setMax(this.f7084l);
        this.f7096x = (AccessibilityManager) this.f7094v.getSystemService("accessibility");
    }

    private void c() {
        Paint paint = new Paint(1);
        this.A = paint;
        paint.setColor(this.f7078f);
        this.A.setStyle(Paint.Style.STROKE);
    }

    private void d() {
        Paint paint = new Paint(1);
        this.f7097y = paint;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.f7097y.setColor(this.f7077e);
        this.f7097y.setStyle(Paint.Style.STROKE);
        this.f7097y.setStrokeWidth(this.f7082j);
        this.f7097y.setStrokeCap(Paint.Cap.ROUND);
    }

    private void f() {
        b bVar = this.f7095w;
        if (bVar == null) {
            this.f7095w = new b();
        } else {
            removeCallbacks(bVar);
        }
        postDelayed(this.f7095w, 10L);
    }

    private void g() {
        int i10 = this.f7084l;
        if (i10 > 0) {
            int i11 = (int) (this.f7085m / (i10 / 360.0f));
            this.f7086n = i11;
            if (360 - i11 < 2) {
                this.f7086n = 360;
            }
            this.f7087o = this.f7086n;
        } else {
            this.f7087o = 0;
            this.f7086n = 0;
        }
        invalidate();
    }

    void e() {
        AccessibilityManager accessibilityManager = this.f7096x;
        if (accessibilityManager != null && accessibilityManager.isEnabled() && this.f7096x.isTouchExplorationEnabled()) {
            f();
        }
    }

    public int getMax() {
        return this.f7084l;
    }

    public int getProgress() {
        return this.f7085m;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        b bVar = this.f7095w;
        if (bVar != null) {
            removeCallbacks(bVar);
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        a(canvas);
        canvas.save();
        int i10 = this.C;
        canvas.rotate(-90.0f, i10, i10);
        canvas.drawArc(this.D, 0.0f, this.f7086n, false, this.f7097y);
        canvas.restore();
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        setMeasuredDimension(this.f7079g, this.f7080h);
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setProgress(savedState.f7099e);
        requestLayout();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f7099e = this.f7085m;
        return savedState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        this.B = this.f7082j / 2;
        this.C = getWidth() / 2;
        this.E = r3 - this.B;
        int i14 = this.C;
        float f10 = this.E;
        this.D = new RectF(i14 - f10, i14 - f10, i14 + f10, i14 + f10);
    }

    public void setHeight(int i10) {
        this.f7080h = i10;
    }

    public void setMax(int i10) {
        if (i10 < 0) {
            i10 = 0;
        }
        if (i10 != this.f7084l) {
            this.f7084l = i10;
            if (this.f7085m > i10) {
                this.f7085m = i10;
            }
        }
        g();
    }

    public void setProgress(int i10) {
        Log.i("COUICircleProgressBar", "setProgress: " + i10);
        if (i10 < 0) {
            i10 = 0;
        }
        int i11 = this.f7084l;
        if (i10 > i11) {
            i10 = i11;
        }
        if (i10 != this.f7085m) {
            this.f7085m = i10;
        }
        g();
        e();
    }

    public void setProgressBarBgCircleColor(int i10) {
        this.f7078f = i10;
        c();
    }

    public void setProgressBarColor(int i10) {
        this.f7077e = i10;
        d();
    }

    public void setProgressBarType(int i10) {
        this.f7081i = i10;
    }

    public void setWidth(int i10) {
        this.f7079g = i10;
    }

    public COUICircleProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiCircleProgressBarStyle);
    }

    public COUICircleProgressBar(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7079g = 0;
        this.f7080h = 0;
        this.f7081i = 0;
        this.f7082j = 0;
        this.f7083k = 0;
        this.f7084l = 100;
        this.f7085m = 0;
        this.f7086n = 0;
        this.f7087o = -1;
        this.f7091s = 1.0f;
        this.f7098z = new ArrayList<>();
        COUIDarkModeUtil.b(this, false);
        this.f7094v = context;
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.F = attributeSet.getStyleAttribute();
        } else {
            this.F = i10;
        }
        this.f7094v = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICircleProgressBar, i10, 0);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_loading_view_default_length);
        this.f7079g = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICircleProgressBar_couiCircleProgressBarWidth, dimensionPixelSize);
        this.f7080h = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICircleProgressBar_couiCircleProgressBarHeight, dimensionPixelSize);
        this.f7081i = obtainStyledAttributes.getInteger(R$styleable.COUICircleProgressBar_couiCircleProgressBarType, 0);
        this.f7077e = obtainStyledAttributes.getColor(R$styleable.COUICircleProgressBar_couiCircleProgressBarColor, 0);
        this.f7078f = obtainStyledAttributes.getColor(R$styleable.COUICircleProgressBar_couiCircleProgressBarBgCircleColor, 0);
        this.f7085m = obtainStyledAttributes.getInteger(R$styleable.COUICircleProgressBar_couiCircleProgress, this.f7085m);
        this.f7084l = obtainStyledAttributes.getInteger(R$styleable.COUICircleProgressBar_couiCircleMax, this.f7084l);
        obtainStyledAttributes.recycle();
        this.f7088p = context.getResources().getDimensionPixelSize(R$dimen.coui_circle_loading_strokewidth);
        this.f7089q = context.getResources().getDimensionPixelSize(R$dimen.coui_circle_loading_medium_strokewidth);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R$dimen.coui_circle_loading_large_strokewidth);
        this.f7090r = dimensionPixelSize2;
        this.f7082j = this.f7088p;
        int i11 = this.f7081i;
        if (1 == i11) {
            this.f7082j = this.f7089q;
        } else if (2 == i11) {
            this.f7082j = dimensionPixelSize2;
        }
        this.f7083k = this.f7082j >> 1;
        this.f7092t = this.f7079g >> 1;
        this.f7093u = this.f7080h >> 1;
        b();
    }
}
