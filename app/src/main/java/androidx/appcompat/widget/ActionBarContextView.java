package androidx.appcompat.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$id;
import androidx.appcompat.R$layout;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

/* loaded from: classes.dex */
public class ActionBarContextView extends AbsActionBarView {

    /* renamed from: m, reason: collision with root package name */
    private CharSequence f828m;

    /* renamed from: n, reason: collision with root package name */
    private CharSequence f829n;

    /* renamed from: o, reason: collision with root package name */
    private View f830o;

    /* renamed from: p, reason: collision with root package name */
    private View f831p;

    /* renamed from: q, reason: collision with root package name */
    private View f832q;

    /* renamed from: r, reason: collision with root package name */
    private LinearLayout f833r;

    /* renamed from: s, reason: collision with root package name */
    private TextView f834s;

    /* renamed from: t, reason: collision with root package name */
    private TextView f835t;

    /* renamed from: u, reason: collision with root package name */
    private int f836u;

    /* renamed from: v, reason: collision with root package name */
    private int f837v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f838w;

    /* renamed from: x, reason: collision with root package name */
    private int f839x;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ActionMode f840e;

        a(ActionMode actionMode) {
            this.f840e = actionMode;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            this.f840e.c();
        }
    }

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    private void i() {
        if (this.f833r == null) {
            LayoutInflater.from(getContext()).inflate(R$layout.abc_action_bar_title_item, this);
            LinearLayout linearLayout = (LinearLayout) getChildAt(getChildCount() - 1);
            this.f833r = linearLayout;
            this.f834s = (TextView) linearLayout.findViewById(R$id.action_bar_title);
            this.f835t = (TextView) this.f833r.findViewById(R$id.action_bar_subtitle);
            if (this.f836u != 0) {
                this.f834s.setTextAppearance(getContext(), this.f836u);
            }
            if (this.f837v != 0) {
                this.f835t.setTextAppearance(getContext(), this.f837v);
            }
        }
        this.f834s.setText(this.f828m);
        this.f835t.setText(this.f829n);
        boolean z10 = !TextUtils.isEmpty(this.f828m);
        boolean z11 = !TextUtils.isEmpty(this.f829n);
        int i10 = 0;
        this.f835t.setVisibility(z11 ? 0 : 8);
        LinearLayout linearLayout2 = this.f833r;
        if (!z10 && !z11) {
            i10 = 8;
        }
        linearLayout2.setVisibility(i10);
        if (this.f833r.getParent() == null) {
            addView(this.f833r);
        }
    }

    @Override // androidx.appcompat.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ ViewPropertyAnimatorCompat f(int i10, long j10) {
        return super.f(i10, j10);
    }

    public void g() {
        if (this.f830o == null) {
            k();
        }
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-1, -2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(getContext(), attributeSet);
    }

    @Override // androidx.appcompat.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ int getAnimatedVisibility() {
        return super.getAnimatedVisibility();
    }

    @Override // androidx.appcompat.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    public CharSequence getSubtitle() {
        return this.f829n;
    }

    public CharSequence getTitle() {
        return this.f828m;
    }

    public void h(ActionMode actionMode) {
        View view = this.f830o;
        if (view == null) {
            View inflate = LayoutInflater.from(getContext()).inflate(this.f839x, (ViewGroup) this, false);
            this.f830o = inflate;
            addView(inflate);
        } else if (view.getParent() == null) {
            addView(this.f830o);
        }
        View findViewById = this.f830o.findViewById(R$id.action_mode_close_button);
        this.f831p = findViewById;
        findViewById.setOnClickListener(new a(actionMode));
        MenuBuilder menuBuilder = (MenuBuilder) actionMode.e();
        ActionMenuPresenter actionMenuPresenter = this.f1146h;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.r();
        }
        ActionMenuPresenter actionMenuPresenter2 = new ActionMenuPresenter(getContext());
        this.f1146h = actionMenuPresenter2;
        actionMenuPresenter2.C(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
        menuBuilder.addMenuPresenter(this.f1146h, this.f1144f);
        ActionMenuView actionMenuView = (ActionMenuView) this.f1146h.h(this);
        this.f1145g = actionMenuView;
        ViewCompat.p0(actionMenuView, null);
        addView(this.f1145g, layoutParams);
    }

    public boolean j() {
        return this.f838w;
    }

    public void k() {
        removeAllViews();
        this.f832q = null;
        this.f1145g = null;
        this.f1146h = null;
        View view = this.f831p;
        if (view != null) {
            view.setOnClickListener(null);
        }
    }

    public boolean l() {
        ActionMenuPresenter actionMenuPresenter = this.f1146h;
        if (actionMenuPresenter != null) {
            return actionMenuPresenter.D();
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActionMenuPresenter actionMenuPresenter = this.f1146h;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.u();
            this.f1146h.v();
        }
    }

    @Override // androidx.appcompat.widget.AbsActionBarView, android.view.View
    public /* bridge */ /* synthetic */ boolean onHoverEvent(MotionEvent motionEvent) {
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        boolean b10 = n0.b(this);
        int paddingRight = b10 ? (i12 - i10) - getPaddingRight() : getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingTop2 = ((i13 - i11) - getPaddingTop()) - getPaddingBottom();
        View view = this.f830o;
        if (view != null && view.getVisibility() != 8) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f830o.getLayoutParams();
            int i14 = b10 ? marginLayoutParams.rightMargin : marginLayoutParams.leftMargin;
            int i15 = b10 ? marginLayoutParams.leftMargin : marginLayoutParams.rightMargin;
            int d10 = AbsActionBarView.d(paddingRight, i14, b10);
            paddingRight = AbsActionBarView.d(d10 + e(this.f830o, d10, paddingTop, paddingTop2, b10), i15, b10);
        }
        int i16 = paddingRight;
        LinearLayout linearLayout = this.f833r;
        if (linearLayout != null && this.f832q == null && linearLayout.getVisibility() != 8) {
            i16 += e(this.f833r, i16, paddingTop, paddingTop2, b10);
        }
        int i17 = i16;
        View view2 = this.f832q;
        if (view2 != null) {
            e(view2, i17, paddingTop, paddingTop2, b10);
        }
        int paddingLeft = b10 ? getPaddingLeft() : (i12 - i10) - getPaddingRight();
        ActionMenuView actionMenuView = this.f1145g;
        if (actionMenuView != null) {
            e(actionMenuView, paddingLeft, paddingTop, paddingTop2, !b10);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        if (View.MeasureSpec.getMode(i10) == 1073741824) {
            if (View.MeasureSpec.getMode(i11) != 0) {
                int size = View.MeasureSpec.getSize(i10);
                int i12 = this.f1147i;
                if (i12 <= 0) {
                    i12 = View.MeasureSpec.getSize(i11);
                }
                int paddingTop = getPaddingTop() + getPaddingBottom();
                int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
                int i13 = i12 - paddingTop;
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i13, Integer.MIN_VALUE);
                View view = this.f830o;
                if (view != null) {
                    int c10 = c(view, paddingLeft, makeMeasureSpec, 0);
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f830o.getLayoutParams();
                    paddingLeft = c10 - (marginLayoutParams.leftMargin + marginLayoutParams.rightMargin);
                }
                ActionMenuView actionMenuView = this.f1145g;
                if (actionMenuView != null && actionMenuView.getParent() == this) {
                    paddingLeft = c(this.f1145g, paddingLeft, makeMeasureSpec, 0);
                }
                LinearLayout linearLayout = this.f833r;
                if (linearLayout != null && this.f832q == null) {
                    if (this.f838w) {
                        this.f833r.measure(View.MeasureSpec.makeMeasureSpec(0, 0), makeMeasureSpec);
                        int measuredWidth = this.f833r.getMeasuredWidth();
                        boolean z10 = measuredWidth <= paddingLeft;
                        if (z10) {
                            paddingLeft -= measuredWidth;
                        }
                        this.f833r.setVisibility(z10 ? 0 : 8);
                    } else {
                        paddingLeft = c(linearLayout, paddingLeft, makeMeasureSpec, 0);
                    }
                }
                View view2 = this.f832q;
                if (view2 != null) {
                    ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                    int i14 = layoutParams.width;
                    int i15 = i14 != -2 ? 1073741824 : Integer.MIN_VALUE;
                    if (i14 >= 0) {
                        paddingLeft = Math.min(i14, paddingLeft);
                    }
                    int i16 = layoutParams.height;
                    int i17 = i16 == -2 ? Integer.MIN_VALUE : 1073741824;
                    if (i16 >= 0) {
                        i13 = Math.min(i16, i13);
                    }
                    this.f832q.measure(View.MeasureSpec.makeMeasureSpec(paddingLeft, i15), View.MeasureSpec.makeMeasureSpec(i13, i17));
                }
                if (this.f1147i <= 0) {
                    int childCount = getChildCount();
                    int i18 = 0;
                    for (int i19 = 0; i19 < childCount; i19++) {
                        int measuredHeight = getChildAt(i19).getMeasuredHeight() + paddingTop;
                        if (measuredHeight > i18) {
                            i18 = measuredHeight;
                        }
                    }
                    setMeasuredDimension(size, i18);
                    return;
                }
                setMeasuredDimension(size, i12);
                return;
            }
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with android:layout_height=\"wrap_content\"");
        }
        throw new IllegalStateException(getClass().getSimpleName() + " can only be used with android:layout_width=\"match_parent\" (or fill_parent)");
    }

    @Override // androidx.appcompat.widget.AbsActionBarView, android.view.View
    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    @Override // androidx.appcompat.widget.AbsActionBarView
    public void setContentHeight(int i10) {
        this.f1147i = i10;
    }

    public void setCustomView(View view) {
        LinearLayout linearLayout;
        View view2 = this.f832q;
        if (view2 != null) {
            removeView(view2);
        }
        this.f832q = view;
        if (view != null && (linearLayout = this.f833r) != null) {
            removeView(linearLayout);
            this.f833r = null;
        }
        if (view != null) {
            addView(view);
        }
        requestLayout();
    }

    public void setSubtitle(CharSequence charSequence) {
        this.f829n = charSequence;
        i();
    }

    public void setTitle(CharSequence charSequence) {
        this.f828m = charSequence;
        i();
        ViewCompat.o0(this, charSequence);
    }

    public void setTitleOptional(boolean z10) {
        if (z10 != this.f838w) {
            requestLayout();
        }
        this.f838w = z10;
    }

    @Override // androidx.appcompat.widget.AbsActionBarView, android.view.View
    public /* bridge */ /* synthetic */ void setVisibility(int i10) {
        super.setVisibility(i10);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.actionModeStyle);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, R$styleable.ActionMode, i10, 0);
        ViewCompat.p0(this, w10.g(R$styleable.ActionMode_background));
        this.f836u = w10.n(R$styleable.ActionMode_titleTextStyle, 0);
        this.f837v = w10.n(R$styleable.ActionMode_subtitleTextStyle, 0);
        this.f1147i = w10.m(R$styleable.ActionMode_height, 0);
        this.f839x = w10.n(R$styleable.ActionMode_closeItemLayout, R$layout.abc_action_mode_close_item_material);
        w10.x();
    }
}
