package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.R$id;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class ActionBarContainer extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    private boolean f818e;

    /* renamed from: f, reason: collision with root package name */
    private View f819f;

    /* renamed from: g, reason: collision with root package name */
    private View f820g;

    /* renamed from: h, reason: collision with root package name */
    private View f821h;

    /* renamed from: i, reason: collision with root package name */
    Drawable f822i;

    /* renamed from: j, reason: collision with root package name */
    Drawable f823j;

    /* renamed from: k, reason: collision with root package name */
    Drawable f824k;

    /* renamed from: l, reason: collision with root package name */
    boolean f825l;

    /* renamed from: m, reason: collision with root package name */
    boolean f826m;

    /* renamed from: n, reason: collision with root package name */
    private int f827n;

    /* loaded from: classes.dex */
    private static class a {
        public static void a(ActionBarContainer actionBarContainer) {
            actionBarContainer.invalidateOutline();
        }
    }

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    private int a(View view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        return view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    private boolean b(View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.f822i;
        if (drawable != null && drawable.isStateful()) {
            this.f822i.setState(getDrawableState());
        }
        Drawable drawable2 = this.f823j;
        if (drawable2 != null && drawable2.isStateful()) {
            this.f823j.setState(getDrawableState());
        }
        Drawable drawable3 = this.f824k;
        if (drawable3 == null || !drawable3.isStateful()) {
            return;
        }
        this.f824k.setState(getDrawableState());
    }

    public View getTabContainer() {
        return this.f819f;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.f822i;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.f823j;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        Drawable drawable3 = this.f824k;
        if (drawable3 != null) {
            drawable3.jumpToCurrentState();
        }
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.f820g = findViewById(R$id.action_bar);
        this.f821h = findViewById(R$id.action_context_bar);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        super.onHoverEvent(motionEvent);
        return true;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.f818e || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        Drawable drawable;
        super.onLayout(z10, i10, i11, i12, i13);
        View view = this.f819f;
        boolean z11 = true;
        boolean z12 = false;
        boolean z13 = (view == null || view.getVisibility() == 8) ? false : true;
        if (view != null && view.getVisibility() != 8) {
            int measuredHeight = getMeasuredHeight();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            int measuredHeight2 = measuredHeight - view.getMeasuredHeight();
            int i14 = layoutParams.bottomMargin;
            view.layout(i10, measuredHeight2 - i14, i12, measuredHeight - i14);
        }
        if (this.f825l) {
            Drawable drawable2 = this.f824k;
            if (drawable2 != null) {
                drawable2.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
            z11 = z12;
        } else {
            if (this.f822i != null) {
                if (this.f820g.getVisibility() == 0) {
                    this.f822i.setBounds(this.f820g.getLeft(), this.f820g.getTop(), this.f820g.getRight(), this.f820g.getBottom());
                } else {
                    View view2 = this.f821h;
                    if (view2 != null && view2.getVisibility() == 0) {
                        this.f822i.setBounds(this.f821h.getLeft(), this.f821h.getTop(), this.f821h.getRight(), this.f821h.getBottom());
                    } else {
                        this.f822i.setBounds(0, 0, 0, 0);
                    }
                }
                z12 = true;
            }
            this.f826m = z13;
            if (z13 && (drawable = this.f823j) != null) {
                drawable.setBounds(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            z11 = z12;
        }
        if (z11) {
            invalidate();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int a10;
        int i12;
        if (this.f820g == null && View.MeasureSpec.getMode(i11) == Integer.MIN_VALUE && (i12 = this.f827n) >= 0) {
            i11 = View.MeasureSpec.makeMeasureSpec(Math.min(i12, View.MeasureSpec.getSize(i11)), Integer.MIN_VALUE);
        }
        super.onMeasure(i10, i11);
        if (this.f820g == null) {
            return;
        }
        int mode = View.MeasureSpec.getMode(i11);
        View view = this.f819f;
        if (view == null || view.getVisibility() == 8 || mode == 1073741824) {
            return;
        }
        if (!b(this.f820g)) {
            a10 = a(this.f820g);
        } else {
            a10 = !b(this.f821h) ? a(this.f821h) : 0;
        }
        setMeasuredDimension(getMeasuredWidth(), Math.min(a10 + a(this.f819f), mode == Integer.MIN_VALUE ? View.MeasureSpec.getSize(i11) : Integer.MAX_VALUE));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public void setPrimaryBackground(Drawable drawable) {
        Drawable drawable2 = this.f822i;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.f822i);
        }
        this.f822i = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            View view = this.f820g;
            if (view != null) {
                this.f822i.setBounds(view.getLeft(), this.f820g.getTop(), this.f820g.getRight(), this.f820g.getBottom());
            }
        }
        boolean z10 = true;
        if (!this.f825l ? this.f822i != null || this.f823j != null : this.f824k != null) {
            z10 = false;
        }
        setWillNotDraw(z10);
        invalidate();
        a.a(this);
    }

    public void setSplitBackground(Drawable drawable) {
        Drawable drawable2;
        Drawable drawable3 = this.f824k;
        if (drawable3 != null) {
            drawable3.setCallback(null);
            unscheduleDrawable(this.f824k);
        }
        this.f824k = drawable;
        boolean z10 = false;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.f825l && (drawable2 = this.f824k) != null) {
                drawable2.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }
        if (!this.f825l ? !(this.f822i != null || this.f823j != null) : this.f824k == null) {
            z10 = true;
        }
        setWillNotDraw(z10);
        invalidate();
        a.a(this);
    }

    public void setStackedBackground(Drawable drawable) {
        Drawable drawable2;
        Drawable drawable3 = this.f823j;
        if (drawable3 != null) {
            drawable3.setCallback(null);
            unscheduleDrawable(this.f823j);
        }
        this.f823j = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.f826m && (drawable2 = this.f823j) != null) {
                drawable2.setBounds(this.f819f.getLeft(), this.f819f.getTop(), this.f819f.getRight(), this.f819f.getBottom());
            }
        }
        boolean z10 = true;
        if (!this.f825l ? this.f822i != null || this.f823j != null : this.f824k != null) {
            z10 = false;
        }
        setWillNotDraw(z10);
        invalidate();
        a.a(this);
    }

    public void setTabContainer(ScrollingTabContainerView scrollingTabContainerView) {
        View view = this.f819f;
        if (view != null) {
            removeView(view);
        }
        this.f819f = scrollingTabContainerView;
        if (scrollingTabContainerView != null) {
            addView(scrollingTabContainerView);
            ViewGroup.LayoutParams layoutParams = scrollingTabContainerView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            scrollingTabContainerView.setAllowCollapse(false);
        }
    }

    public void setTransitioning(boolean z10) {
        this.f818e = z10;
        setDescendantFocusability(z10 ? 393216 : 262144);
    }

    @Override // android.view.View
    public void setVisibility(int i10) {
        super.setVisibility(i10);
        boolean z10 = i10 == 0;
        Drawable drawable = this.f822i;
        if (drawable != null) {
            drawable.setVisible(z10, false);
        }
        Drawable drawable2 = this.f823j;
        if (drawable2 != null) {
            drawable2.setVisible(z10, false);
        }
        Drawable drawable3 = this.f824k;
        if (drawable3 != null) {
            drawable3.setVisible(z10, false);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return null;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback, int i10) {
        if (i10 != 0) {
            return super.startActionModeForChild(view, callback, i10);
        }
        return null;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return (drawable == this.f822i && !this.f825l) || (drawable == this.f823j && this.f826m) || ((drawable == this.f824k && this.f825l) || super.verifyDrawable(drawable));
    }

    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ViewCompat.p0(this, new ActionBarBackgroundDrawable(this));
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActionBar);
        this.f822i = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_background);
        this.f823j = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_backgroundStacked);
        this.f827n = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ActionBar_height, -1);
        boolean z10 = true;
        if (getId() == R$id.split_action_bar) {
            this.f825l = true;
            this.f824k = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_backgroundSplit);
        }
        obtainStyledAttributes.recycle();
        if (!this.f825l ? this.f822i != null || this.f823j != null : this.f824k != null) {
            z10 = false;
        }
        setWillNotDraw(z10);
    }
}
