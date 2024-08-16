package com.coui.appcompat.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityManagerCompat;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUILoadProgress extends AppCompatButton {

    /* renamed from: v, reason: collision with root package name */
    private static final DecelerateInterpolator f7174v = new DecelerateInterpolator();

    /* renamed from: w, reason: collision with root package name */
    private static final int[] f7175w = {R$attr.coui_state_default};

    /* renamed from: x, reason: collision with root package name */
    private static final int[] f7176x = {R$attr.coui_state_wait};

    /* renamed from: y, reason: collision with root package name */
    private static final int[] f7177y = {R$attr.coui_state_fail};

    /* renamed from: z, reason: collision with root package name */
    private static final int[] f7178z = {R$attr.coui_state_ing};

    /* renamed from: h, reason: collision with root package name */
    private final String f7179h;

    /* renamed from: i, reason: collision with root package name */
    private final boolean f7180i;

    /* renamed from: j, reason: collision with root package name */
    private final AccessibilityManager f7181j;

    /* renamed from: k, reason: collision with root package name */
    public int f7182k;

    /* renamed from: l, reason: collision with root package name */
    public int f7183l;

    /* renamed from: m, reason: collision with root package name */
    public int f7184m;

    /* renamed from: n, reason: collision with root package name */
    protected boolean f7185n;

    /* renamed from: o, reason: collision with root package name */
    protected float f7186o;

    /* renamed from: p, reason: collision with root package name */
    private int f7187p;

    /* renamed from: q, reason: collision with root package name */
    private Drawable f7188q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f7189r;

    /* renamed from: s, reason: collision with root package name */
    private b f7190s;

    /* renamed from: t, reason: collision with root package name */
    private b f7191t;

    /* renamed from: u, reason: collision with root package name */
    private a f7192u;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f7193e;

        /* renamed from: f, reason: collision with root package name */
        int f7194f;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        /* synthetic */ SavedState(Parcel parcel, k kVar) {
            this(parcel);
        }

        public String toString() {
            return "CompoundButton.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " mState = " + this.f7193e + " mProgress = " + this.f7194f + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeValue(Integer.valueOf(this.f7193e));
            parcel.writeValue(Integer.valueOf(this.f7194f));
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f7193e = ((Integer) parcel.readValue(null)).intValue();
            this.f7194f = ((Integer) parcel.readValue(null)).intValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        private a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUILoadProgress.this.sendAccessibilityEvent(4);
        }

        /* synthetic */ a(COUILoadProgress cOUILoadProgress, k kVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        void a(COUILoadProgress cOUILoadProgress, int i10);
    }

    public COUILoadProgress(Context context) {
        this(context, null);
    }

    private void a() {
        this.f7183l = 0;
        this.f7184m = 100;
    }

    private void c() {
        a aVar = this.f7192u;
        if (aVar == null) {
            this.f7192u = new a(this, null);
        } else {
            removeCallbacks(aVar);
        }
        postDelayed(this.f7192u, 10L);
    }

    void b(int i10) {
        AccessibilityManager accessibilityManager = this.f7181j;
        if (accessibilityManager != null && accessibilityManager.isEnabled() && AccessibilityManagerCompat.b(this.f7181j)) {
            c();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.f7188q != null) {
            this.f7188q.setState(getDrawableState());
            invalidate();
        }
    }

    public int getMax() {
        return this.f7184m;
    }

    public int getProgress() {
        return this.f7183l;
    }

    public int getState() {
        return this.f7182k;
    }

    @Override // android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.f7188q;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i10) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i10 + 1);
        if (getState() == 0) {
            Button.mergeDrawableStates(onCreateDrawableState, f7175w);
        }
        if (getState() == 1) {
            Button.mergeDrawableStates(onCreateDrawableState, f7178z);
        }
        if (getState() == 2) {
            Button.mergeDrawableStates(onCreateDrawableState, f7176x);
        }
        if (getState() == 3) {
            Button.mergeDrawableStates(onCreateDrawableState, f7177y);
        }
        return onCreateDrawableState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDetachedFromWindow() {
        a aVar = this.f7192u;
        if (aVar != null) {
            removeCallbacks(aVar);
        }
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setState(savedState.f7193e);
        setProgress(savedState.f7194f);
        requestLayout();
    }

    @Override // android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        setFreezesText(true);
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f7193e = getState();
        savedState.f7194f = this.f7183l;
        return savedState;
    }

    @Override // android.view.View
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public void setButtonDrawable(int i10) {
        if (i10 == 0 || i10 != this.f7187p) {
            this.f7187p = i10;
            setButtonDrawable(i10 != 0 ? getResources().getDrawable(this.f7187p) : null);
        }
    }

    public void setMax(int i10) {
        if (i10 < 0) {
            i10 = 0;
        }
        if (i10 != this.f7184m) {
            this.f7184m = i10;
            if (this.f7183l > i10) {
                this.f7183l = i10;
            }
            invalidate();
        }
    }

    public void setOnStateChangeListener(b bVar) {
        this.f7190s = bVar;
    }

    void setOnStateChangeWidgetListener(b bVar) {
        this.f7191t = bVar;
    }

    public void setProgress(int i10) {
        if (i10 < 0) {
            i10 = 0;
        }
        int i11 = this.f7184m;
        if (i10 > i11) {
            i10 = i11;
        }
        if (i10 != this.f7183l) {
            this.f7183l = i10;
        }
        if (this.f7185n) {
            this.f7185n = false;
        }
        invalidate();
        b(i10);
    }

    public void setState(int i10) {
        if (this.f7182k != i10) {
            this.f7182k = i10;
            refreshDrawableState();
            if (this.f7189r) {
                return;
            }
            this.f7189r = true;
            b bVar = this.f7190s;
            if (bVar != null) {
                bVar.a(this, this.f7182k);
            }
            b bVar2 = this.f7191t;
            if (bVar2 != null) {
                bVar2.a(this, this.f7182k);
            }
            this.f7189r = false;
        }
    }

    public void toggle() {
        int i10 = this.f7182k;
        if (i10 == 0) {
            setState(1);
            return;
        }
        if (i10 == 1) {
            setState(2);
        } else if (i10 == 2) {
            setState(1);
        } else if (i10 == 3) {
            setState(1);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f7188q;
    }

    public COUILoadProgress(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiLoadProgressStyle);
    }

    public COUILoadProgress(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7179h = "COUILoadProgress";
        this.f7180i = false;
        this.f7185n = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUILoadProgress, i10, 0);
        int integer = obtainStyledAttributes.getInteger(R$styleable.COUILoadProgress_couiState, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.COUILoadProgress_couiDefaultDrawable);
        if (drawable != null) {
            setButtonDrawable(drawable);
        }
        setProgress(obtainStyledAttributes.getInt(R$styleable.COUILoadProgress_couiProgress, this.f7183l));
        setState(integer);
        obtainStyledAttributes.recycle();
        a();
        if (ViewCompat.v(this) == 0) {
            ViewCompat.w0(this, 1);
        }
        this.f7181j = (AccessibilityManager) context.getSystemService("accessibility");
    }

    public void setButtonDrawable(Drawable drawable) {
        if (drawable != null) {
            Drawable drawable2 = this.f7188q;
            if (drawable2 != null) {
                drawable2.setCallback(null);
                unscheduleDrawable(this.f7188q);
            }
            drawable.setCallback(this);
            drawable.setState(getDrawableState());
            drawable.setVisible(getVisibility() == 0, false);
            this.f7188q = drawable;
            drawable.setState(null);
            setMinHeight(this.f7188q.getIntrinsicHeight());
        }
        refreshDrawableState();
    }
}
