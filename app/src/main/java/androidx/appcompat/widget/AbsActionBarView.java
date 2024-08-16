package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AbsActionBarView.java */
/* renamed from: androidx.appcompat.widget.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class AbsActionBarView extends ViewGroup {

    /* renamed from: e, reason: collision with root package name */
    protected final a f1143e;

    /* renamed from: f, reason: collision with root package name */
    protected final Context f1144f;

    /* renamed from: g, reason: collision with root package name */
    protected ActionMenuView f1145g;

    /* renamed from: h, reason: collision with root package name */
    protected ActionMenuPresenter f1146h;

    /* renamed from: i, reason: collision with root package name */
    protected int f1147i;

    /* renamed from: j, reason: collision with root package name */
    protected ViewPropertyAnimatorCompat f1148j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f1149k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f1150l;

    /* compiled from: AbsActionBarView.java */
    /* renamed from: androidx.appcompat.widget.a$a */
    /* loaded from: classes.dex */
    protected class a implements ViewPropertyAnimatorListener {

        /* renamed from: a, reason: collision with root package name */
        private boolean f1151a = false;

        /* renamed from: b, reason: collision with root package name */
        int f1152b;

        protected a() {
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void a(View view) {
            this.f1151a = true;
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void b(View view) {
            if (this.f1151a) {
                return;
            }
            AbsActionBarView absActionBarView = AbsActionBarView.this;
            absActionBarView.f1148j = null;
            AbsActionBarView.super.setVisibility(this.f1152b);
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void c(View view) {
            AbsActionBarView.super.setVisibility(0);
            this.f1151a = false;
        }

        public a d(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, int i10) {
            AbsActionBarView.this.f1148j = viewPropertyAnimatorCompat;
            this.f1152b = i10;
            return this;
        }
    }

    AbsActionBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int d(int i10, int i11, boolean z10) {
        return z10 ? i10 - i11 : i10 + i11;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int c(View view, int i10, int i11, int i12) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i10, Integer.MIN_VALUE), i11);
        return Math.max(0, (i10 - view.getMeasuredWidth()) - i12);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int e(View view, int i10, int i11, int i12, boolean z10) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i13 = i11 + ((i12 - measuredHeight) / 2);
        if (z10) {
            view.layout(i10 - measuredWidth, i13, i10, measuredHeight + i13);
        } else {
            view.layout(i10, i13, i10 + measuredWidth, measuredHeight + i13);
        }
        return z10 ? -measuredWidth : measuredWidth;
    }

    public ViewPropertyAnimatorCompat f(int i10, long j10) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.f1148j;
        if (viewPropertyAnimatorCompat != null) {
            viewPropertyAnimatorCompat.c();
        }
        if (i10 == 0) {
            if (getVisibility() != 0) {
                setAlpha(0.0f);
            }
            ViewPropertyAnimatorCompat b10 = ViewCompat.d(this).b(1.0f);
            b10.g(j10);
            b10.i(this.f1143e.d(b10, i10));
            return b10;
        }
        ViewPropertyAnimatorCompat b11 = ViewCompat.d(this).b(0.0f);
        b11.g(j10);
        b11.i(this.f1143e.d(b11, i10));
        return b11;
    }

    public int getAnimatedVisibility() {
        if (this.f1148j != null) {
            return this.f1143e.f1152b;
        }
        return getVisibility();
    }

    public int getContentHeight() {
        return this.f1147i;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
        setContentHeight(obtainStyledAttributes.getLayoutDimension(R$styleable.ActionBar_height, 0));
        obtainStyledAttributes.recycle();
        ActionMenuPresenter actionMenuPresenter = this.f1146h;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.y(configuration);
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.f1150l = false;
        }
        if (!this.f1150l) {
            boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.f1150l = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.f1150l = false;
        }
        return true;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.f1149k = false;
        }
        if (!this.f1149k) {
            boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.f1149k = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.f1149k = false;
        }
        return true;
    }

    public void setContentHeight(int i10) {
        this.f1147i = i10;
        requestLayout();
    }

    @Override // android.view.View
    public void setVisibility(int i10) {
        if (i10 != getVisibility()) {
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.f1148j;
            if (viewPropertyAnimatorCompat != null) {
                viewPropertyAnimatorCompat.c();
            }
            super.setVisibility(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbsActionBarView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1143e = new a();
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R$attr.actionBarPopupTheme, typedValue, true) && typedValue.resourceId != 0) {
            this.f1144f = new ContextThemeWrapper(context, typedValue.resourceId);
        } else {
            this.f1144f = context;
        }
    }
}
