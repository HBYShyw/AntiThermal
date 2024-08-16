package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import androidx.core.graphics.ColorUtils;
import com.coui.appcompat.progressbar.COUICircularProgressDrawable;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$styleable;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUICircularProgressBar extends View {
    private AccessibilityManager A;

    /* renamed from: e, reason: collision with root package name */
    private final COUICircularProgressDrawable f7102e;

    /* renamed from: f, reason: collision with root package name */
    private int f7103f;

    /* renamed from: g, reason: collision with root package name */
    private int f7104g;

    /* renamed from: h, reason: collision with root package name */
    private int f7105h;

    /* renamed from: i, reason: collision with root package name */
    private int f7106i;

    /* renamed from: j, reason: collision with root package name */
    private int f7107j;

    /* renamed from: k, reason: collision with root package name */
    private int f7108k;

    /* renamed from: l, reason: collision with root package name */
    private int f7109l;

    /* renamed from: m, reason: collision with root package name */
    private int f7110m;

    /* renamed from: n, reason: collision with root package name */
    private int f7111n;

    /* renamed from: o, reason: collision with root package name */
    private int f7112o;

    /* renamed from: p, reason: collision with root package name */
    private int f7113p;

    /* renamed from: q, reason: collision with root package name */
    private int f7114q;

    /* renamed from: r, reason: collision with root package name */
    private int f7115r;

    /* renamed from: s, reason: collision with root package name */
    private int f7116s;

    /* renamed from: t, reason: collision with root package name */
    private float f7117t;

    /* renamed from: u, reason: collision with root package name */
    private float f7118u;

    /* renamed from: v, reason: collision with root package name */
    private Context f7119v;

    /* renamed from: w, reason: collision with root package name */
    private int f7120w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f7121x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f7122y;

    /* renamed from: z, reason: collision with root package name */
    private b f7123z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f7124e;

        /* renamed from: f, reason: collision with root package name */
        int f7125f;

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
            return "COUICircularProgressBar.SavedState { " + Integer.toHexString(System.identityHashCode(this)) + " mProgress = " + this.f7124e + " mMax = " + this.f7125f + " }";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeValue(Integer.valueOf(this.f7124e));
            parcel.writeValue(Integer.valueOf(this.f7125f));
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f7124e = ((Integer) parcel.readValue(getClass().getClassLoader())).intValue();
            this.f7125f = ((Integer) parcel.readValue(getClass().getClassLoader())).intValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        private b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUICircularProgressBar.this.sendAccessibilityEvent(4);
        }
    }

    public COUICircularProgressBar(Context context) {
        this(context, null);
    }

    private void a() {
        if (2 == this.f7104g) {
            this.f7102e.X(ColorUtils.n(this.f7106i, 89));
        } else {
            this.f7102e.X(this.f7106i);
        }
        this.f7102e.O(1 == this.f7104g);
        this.f7102e.V(this.f7105h);
        this.f7102e.S(this.f7107j);
        this.f7102e.M(this.f7108k);
        COUICircularProgressDrawable cOUICircularProgressDrawable = this.f7102e;
        float f10 = this.f7117t;
        int i10 = this.f7111n;
        cOUICircularProgressDrawable.W(f10 + i10, this.f7118u + i10, this.f7109l - (i10 * 2), this.f7112o);
        this.f7102e.U(this.f7119v.getResources().getDimensionPixelSize(R$dimen.coui_circular_progress_error_diameter), this.f7119v.getResources().getDimensionPixelSize(R$dimen.coui_circular_progress_error_stroke_width));
        this.f7102e.invalidateSelf();
        invalidate();
    }

    private void b() {
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        this.A = (AccessibilityManager) this.f7119v.getSystemService("accessibility");
        setProgress(this.f7114q);
        setMax(this.f7113p);
        a();
    }

    private void d() {
        b bVar = this.f7123z;
        if (bVar == null) {
            this.f7123z = new b();
        } else {
            removeCallbacks(bVar);
        }
        postDelayed(this.f7123z, 10L);
    }

    void c() {
        AccessibilityManager accessibilityManager = this.A;
        if (accessibilityManager != null && accessibilityManager.isEnabled() && this.A.isTouchExplorationEnabled()) {
            d();
        }
    }

    public void e(int i10, boolean z10) {
        if (i10 < 0) {
            i10 = 0;
        }
        int i11 = this.f7113p;
        if (i10 > i11) {
            i10 = i11;
        }
        if (i10 != this.f7114q) {
            this.f7114q = i10;
            this.f7102e.T(i10, z10);
        }
        c();
    }

    public int getMax() {
        return this.f7113p;
    }

    public int getProgress() {
        return this.f7114q;
    }

    public float getVisualProgress() {
        return this.f7102e.r();
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        this.f7102e.N(this);
        super.onAttachedToWindow();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        COUICircularProgressDrawable cOUICircularProgressDrawable = this.f7102e;
        if (cOUICircularProgressDrawable != null) {
            cOUICircularProgressDrawable.L();
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        this.f7102e.draw(canvas);
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int i12 = this.f7109l;
        int i13 = this.f7111n;
        setMeasuredDimension(i12 + (i13 * 2), this.f7110m + (i13 * 2));
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        e(savedState.f7124e, false);
        requestLayout();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f7124e = this.f7114q;
        return savedState;
    }

    public void setMax(int i10) {
        if (i10 < 0) {
            i10 = 0;
        }
        if (i10 != this.f7113p) {
            this.f7113p = i10;
            this.f7102e.P(i10);
            int i11 = this.f7114q;
            int i12 = this.f7113p;
            if (i11 > i12) {
                this.f7114q = i12;
            }
        }
    }

    public void setOnProgressChangedListener(COUICircularProgressDrawable.f fVar) {
        COUICircularProgressDrawable cOUICircularProgressDrawable = this.f7102e;
        if (cOUICircularProgressDrawable != null) {
            cOUICircularProgressDrawable.Q(fVar);
        }
    }

    public void setOnProgressStateAnimationListener(COUICircularProgressDrawable.g gVar) {
        COUICircularProgressDrawable cOUICircularProgressDrawable = this.f7102e;
        if (cOUICircularProgressDrawable != null) {
            cOUICircularProgressDrawable.R(gVar);
        }
    }

    public void setProgress(int i10) {
        e(i10, true);
    }

    public void setProgressBarType(int i10) {
        this.f7104g = i10;
        a();
    }

    public void setProgressSize(int i10) {
        this.f7103f = i10;
        if (i10 == 0) {
            int dimensionPixelOffset = this.f7119v.getResources().getDimensionPixelOffset(R$dimen.coui_circular_progress_medium_length);
            this.f7109l = dimensionPixelOffset;
            this.f7110m = dimensionPixelOffset;
            this.f7112o = this.f7115r;
        } else if (1 == i10) {
            int dimensionPixelOffset2 = this.f7119v.getResources().getDimensionPixelOffset(R$dimen.coui_circular_progress_large_length);
            this.f7109l = dimensionPixelOffset2;
            this.f7110m = dimensionPixelOffset2;
            this.f7112o = this.f7116s;
        }
        this.f7117t = this.f7109l >> 1;
        this.f7118u = this.f7110m >> 1;
        a();
        requestLayout();
    }

    public COUICircularProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiCircularProgressBarStyle);
    }

    public COUICircularProgressBar(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7103f = 0;
        this.f7104g = 0;
        this.f7109l = 0;
        this.f7110m = 0;
        this.f7111n = 0;
        this.f7112o = 0;
        this.f7113p = 100;
        this.f7114q = 0;
        this.f7121x = false;
        this.f7122y = false;
        COUIDarkModeUtil.b(this, false);
        this.f7119v = context;
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f7120w = attributeSet.getStyleAttribute();
        } else {
            this.f7120w = i10;
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_circular_progress_large_length);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICircularProgressBar, i10, 0);
        this.f7109l = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICircularProgressBar_couiCircularProgressBarWidth, dimensionPixelSize);
        this.f7110m = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICircularProgressBar_couiCircularProgressBarHeight, dimensionPixelSize);
        this.f7104g = obtainStyledAttributes.getInteger(R$styleable.COUICircularProgressBar_couiCircularProgressBarType, 0);
        this.f7103f = obtainStyledAttributes.getInteger(R$styleable.COUICircularProgressBar_couiCircularProgressBarSize, 1);
        this.f7105h = obtainStyledAttributes.getColor(R$styleable.COUICircularProgressBar_couiCircularProgressBarColor, 0);
        this.f7106i = obtainStyledAttributes.getColor(R$styleable.COUICircularProgressBar_couiCircularProgressBarTrackColor, 0);
        this.f7107j = obtainStyledAttributes.getColor(R$styleable.COUICircularProgressBar_couiCircularPauseDrawableTint, 0);
        this.f7108k = obtainStyledAttributes.getColor(R$styleable.COUICircularProgressBar_couiCircularErrorDrawableTint, 0);
        this.f7114q = obtainStyledAttributes.getInteger(R$styleable.COUICircularProgressBar_couiCircularProgress, this.f7114q);
        this.f7113p = obtainStyledAttributes.getInteger(R$styleable.COUICircularProgressBar_couiCircularMax, this.f7113p);
        obtainStyledAttributes.recycle();
        this.f7111n = context.getResources().getDimensionPixelSize(R$dimen.coui_circular_progress_default_padding);
        this.f7115r = context.getResources().getDimensionPixelSize(R$dimen.coui_circular_progress_medium_stroke_width);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R$dimen.coui_circular_progress_large_stroke_width);
        this.f7116s = dimensionPixelSize2;
        int i11 = this.f7103f;
        if (i11 == 0) {
            this.f7112o = this.f7115r;
        } else if (1 == i11) {
            this.f7112o = dimensionPixelSize2;
        }
        this.f7117t = this.f7109l >> 1;
        this.f7118u = this.f7110m >> 1;
        this.f7102e = new COUICircularProgressDrawable(context);
        b();
    }
}
