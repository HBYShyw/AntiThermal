package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatImageHelper;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import b4.ShadowViewDelegate;
import c4.ShapeAppearanceModel;
import c4.Shapeable;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButtonImpl;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.stateful.ExtendableSavedState;
import d4.MaterialThemeOverlay;
import java.util.List;
import p3.MotionSpec;
import p3.TransformationCallback;
import v3.ExpandableWidget;
import v3.ExpandableWidgetHelper;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class FloatingActionButton extends VisibilityAwareImageButton implements ExpandableWidget, Shapeable, CoordinatorLayout.b {

    /* renamed from: u, reason: collision with root package name */
    private static final int f8819u = R$style.Widget_Design_FloatingActionButton;

    /* renamed from: e, reason: collision with root package name */
    private ColorStateList f8820e;

    /* renamed from: f, reason: collision with root package name */
    private PorterDuff.Mode f8821f;

    /* renamed from: g, reason: collision with root package name */
    private ColorStateList f8822g;

    /* renamed from: h, reason: collision with root package name */
    private PorterDuff.Mode f8823h;

    /* renamed from: i, reason: collision with root package name */
    private ColorStateList f8824i;

    /* renamed from: j, reason: collision with root package name */
    private int f8825j;

    /* renamed from: k, reason: collision with root package name */
    private int f8826k;

    /* renamed from: l, reason: collision with root package name */
    private int f8827l;

    /* renamed from: m, reason: collision with root package name */
    private int f8828m;

    /* renamed from: n, reason: collision with root package name */
    private int f8829n;

    /* renamed from: o, reason: collision with root package name */
    boolean f8830o;

    /* renamed from: p, reason: collision with root package name */
    final Rect f8831p;

    /* renamed from: q, reason: collision with root package name */
    private final Rect f8832q;

    /* renamed from: r, reason: collision with root package name */
    private final AppCompatImageHelper f8833r;

    /* renamed from: s, reason: collision with root package name */
    private final ExpandableWidgetHelper f8834s;

    /* renamed from: t, reason: collision with root package name */
    private FloatingActionButtonImpl f8835t;

    /* loaded from: classes.dex */
    public static class Behavior extends BaseBehavior<FloatingActionButton> {
        public Behavior() {
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        /* renamed from: d */
        public /* bridge */ /* synthetic */ boolean getInsetDodgeRect(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, Rect rect) {
            return super.getInsetDodgeRect(coordinatorLayout, floatingActionButton, rect);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        /* renamed from: g */
        public /* bridge */ /* synthetic */ boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            return super.onDependentViewChanged(coordinatorLayout, floatingActionButton, view);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        /* renamed from: h */
        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int i10) {
            return super.onLayoutChild(coordinatorLayout, floatingActionButton, i10);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onAttachedToLayoutParams(CoordinatorLayout.e eVar) {
            super.onAttachedToLayoutParams(eVar);
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements FloatingActionButtonImpl.j {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ b f8839a;

        a(b bVar) {
            this.f8839a = bVar;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.j
        public void a() {
            this.f8839a.b(FloatingActionButton.this);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.j
        public void b() {
            this.f8839a.a(FloatingActionButton.this);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class b {
        public void a(FloatingActionButton floatingActionButton) {
        }

        public void b(FloatingActionButton floatingActionButton) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class c implements ShadowViewDelegate {
        c() {
        }

        @Override // b4.ShadowViewDelegate
        public void a(int i10, int i11, int i12, int i13) {
            FloatingActionButton.this.f8831p.set(i10, i11, i12, i13);
            FloatingActionButton floatingActionButton = FloatingActionButton.this;
            floatingActionButton.setPadding(i10 + floatingActionButton.f8828m, i11 + FloatingActionButton.this.f8828m, i12 + FloatingActionButton.this.f8828m, i13 + FloatingActionButton.this.f8828m);
        }

        @Override // b4.ShadowViewDelegate
        public boolean b() {
            return FloatingActionButton.this.f8830o;
        }

        @Override // b4.ShadowViewDelegate
        public void c(Drawable drawable) {
            if (drawable != null) {
                FloatingActionButton.super.setBackgroundDrawable(drawable);
            }
        }
    }

    /* loaded from: classes.dex */
    class d<T extends FloatingActionButton> implements FloatingActionButtonImpl.i {

        /* renamed from: a, reason: collision with root package name */
        private final TransformationCallback<T> f8842a;

        d(TransformationCallback<T> transformationCallback) {
            this.f8842a = transformationCallback;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.i
        public void a() {
            this.f8842a.b(FloatingActionButton.this);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.i
        public void b() {
            this.f8842a.a(FloatingActionButton.this);
        }

        public boolean equals(Object obj) {
            return (obj instanceof d) && ((d) obj).f8842a.equals(this.f8842a);
        }

        public int hashCode() {
            return this.f8842a.hashCode();
        }
    }

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    private FloatingActionButtonImpl g() {
        return new FloatingActionButtonImplLollipop(this, new c());
    }

    private FloatingActionButtonImpl getImpl() {
        if (this.f8835t == null) {
            this.f8835t = g();
        }
        return this.f8835t;
    }

    private int j(int i10) {
        int i11 = this.f8827l;
        if (i11 != 0) {
            return i11;
        }
        Resources resources = getResources();
        if (i10 != -1) {
            if (i10 != 1) {
                return resources.getDimensionPixelSize(R$dimen.design_fab_size_normal);
            }
            return resources.getDimensionPixelSize(R$dimen.design_fab_size_mini);
        }
        if (Math.max(resources.getConfiguration().screenWidthDp, resources.getConfiguration().screenHeightDp) < 470) {
            return j(1);
        }
        return j(0);
    }

    private void o(Rect rect) {
        int i10 = rect.left;
        Rect rect2 = this.f8831p;
        rect.left = i10 + rect2.left;
        rect.top += rect2.top;
        rect.right -= rect2.right;
        rect.bottom -= rect2.bottom;
    }

    private void p() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        ColorStateList colorStateList = this.f8822g;
        if (colorStateList == null) {
            DrawableCompat.a(drawable);
            return;
        }
        int colorForState = colorStateList.getColorForState(getDrawableState(), 0);
        PorterDuff.Mode mode = this.f8823h;
        if (mode == null) {
            mode = PorterDuff.Mode.SRC_IN;
        }
        drawable.mutate().setColorFilter(AppCompatDrawableManager.e(colorForState, mode));
    }

    private static int q(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i11);
        int size = View.MeasureSpec.getSize(i11);
        if (mode == Integer.MIN_VALUE) {
            return Math.min(i10, size);
        }
        if (mode == 0) {
            return i10;
        }
        if (mode == 1073741824) {
            return size;
        }
        throw new IllegalArgumentException();
    }

    private FloatingActionButtonImpl.j t(b bVar) {
        if (bVar == null) {
            return null;
        }
        return new a(bVar);
    }

    @Override // v3.ExpandableWidget
    public boolean a() {
        return this.f8834s.c();
    }

    public void d(Animator.AnimatorListener animatorListener) {
        getImpl().e(animatorListener);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        getImpl().D(getDrawableState());
    }

    public void e(Animator.AnimatorListener animatorListener) {
        getImpl().f(animatorListener);
    }

    public void f(TransformationCallback<? extends FloatingActionButton> transformationCallback) {
        getImpl().g(new d(transformationCallback));
    }

    @Override // android.view.View
    public ColorStateList getBackgroundTintList() {
        return this.f8820e;
    }

    @Override // android.view.View
    public PorterDuff.Mode getBackgroundTintMode() {
        return this.f8821f;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.b
    public CoordinatorLayout.Behavior<FloatingActionButton> getBehavior() {
        return new Behavior();
    }

    public float getCompatElevation() {
        return getImpl().m();
    }

    public float getCompatHoveredFocusedTranslationZ() {
        return getImpl().p();
    }

    public float getCompatPressedTranslationZ() {
        return getImpl().s();
    }

    public Drawable getContentBackground() {
        return getImpl().l();
    }

    public int getCustomSize() {
        return this.f8827l;
    }

    public int getExpandedComponentIdHint() {
        return this.f8834s.b();
    }

    public MotionSpec getHideMotionSpec() {
        return getImpl().o();
    }

    @Deprecated
    public int getRippleColor() {
        ColorStateList colorStateList = this.f8824i;
        if (colorStateList != null) {
            return colorStateList.getDefaultColor();
        }
        return 0;
    }

    public ColorStateList getRippleColorStateList() {
        return this.f8824i;
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return (ShapeAppearanceModel) Preconditions.d(getImpl().t());
    }

    public MotionSpec getShowMotionSpec() {
        return getImpl().u();
    }

    public int getSize() {
        return this.f8826k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSizeDimension() {
        return j(this.f8826k);
    }

    public ColorStateList getSupportBackgroundTintList() {
        return getBackgroundTintList();
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return getBackgroundTintMode();
    }

    public ColorStateList getSupportImageTintList() {
        return this.f8822g;
    }

    public PorterDuff.Mode getSupportImageTintMode() {
        return this.f8823h;
    }

    public boolean getUseCompatPadding() {
        return this.f8830o;
    }

    @Deprecated
    public boolean h(Rect rect) {
        if (!ViewCompat.Q(this)) {
            return false;
        }
        rect.set(0, 0, getWidth(), getHeight());
        o(rect);
        return true;
    }

    public void i(Rect rect) {
        rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        o(rect);
    }

    @Override // android.widget.ImageView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        getImpl().z();
    }

    public void k(b bVar) {
        l(bVar, true);
    }

    void l(b bVar, boolean z10) {
        getImpl().v(t(bVar), z10);
    }

    public boolean m() {
        return getImpl().x();
    }

    public boolean n() {
        return getImpl().y();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getImpl().A();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getImpl().C();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i10, int i11) {
        int sizeDimension = getSizeDimension();
        this.f8828m = (sizeDimension - this.f8829n) / 2;
        getImpl().e0();
        int min = Math.min(q(sizeDimension, i10), q(sizeDimension, i11));
        Rect rect = this.f8831p;
        setMeasuredDimension(rect.left + min + rect.right, min + rect.top + rect.bottom);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof ExtendableSavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        ExtendableSavedState extendableSavedState = (ExtendableSavedState) parcelable;
        super.onRestoreInstanceState(extendableSavedState.getSuperState());
        this.f8834s.d((Bundle) Preconditions.d(extendableSavedState.f9254e.get("expandableWidgetHelper")));
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (onSaveInstanceState == null) {
            onSaveInstanceState = new Bundle();
        }
        ExtendableSavedState extendableSavedState = new ExtendableSavedState(onSaveInstanceState);
        extendableSavedState.f9254e.put("expandableWidgetHelper", this.f8834s.e());
        return extendableSavedState;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && h(this.f8832q) && !this.f8832q.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void r(b bVar) {
        s(bVar, true);
    }

    void s(b bVar, boolean z10) {
        getImpl().b0(t(bVar), z10);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i10) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public void setBackgroundResource(int i10) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        if (this.f8820e != colorStateList) {
            this.f8820e = colorStateList;
            getImpl().K(colorStateList);
        }
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        if (this.f8821f != mode) {
            this.f8821f = mode;
            getImpl().L(mode);
        }
    }

    public void setCompatElevation(float f10) {
        getImpl().M(f10);
    }

    public void setCompatElevationResource(int i10) {
        setCompatElevation(getResources().getDimension(i10));
    }

    public void setCompatHoveredFocusedTranslationZ(float f10) {
        getImpl().P(f10);
    }

    public void setCompatHoveredFocusedTranslationZResource(int i10) {
        setCompatHoveredFocusedTranslationZ(getResources().getDimension(i10));
    }

    public void setCompatPressedTranslationZ(float f10) {
        getImpl().T(f10);
    }

    public void setCompatPressedTranslationZResource(int i10) {
        setCompatPressedTranslationZ(getResources().getDimension(i10));
    }

    public void setCustomSize(int i10) {
        if (i10 >= 0) {
            if (i10 != this.f8827l) {
                this.f8827l = i10;
                requestLayout();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Custom size must be non-negative");
    }

    @Override // android.view.View
    public void setElevation(float f10) {
        super.setElevation(f10);
        getImpl().f0(f10);
    }

    public void setEnsureMinTouchTargetSize(boolean z10) {
        if (z10 != getImpl().n()) {
            getImpl().N(z10);
            requestLayout();
        }
    }

    public void setExpandedComponentIdHint(int i10) {
        this.f8834s.f(i10);
    }

    public void setHideMotionSpec(MotionSpec motionSpec) {
        getImpl().O(motionSpec);
    }

    public void setHideMotionSpecResource(int i10) {
        setHideMotionSpec(MotionSpec.d(getContext(), i10));
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        if (getDrawable() != drawable) {
            super.setImageDrawable(drawable);
            getImpl().d0();
            if (this.f8822g != null) {
                p();
            }
        }
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i10) {
        this.f8833r.i(i10);
        p();
    }

    public void setMaxImageSize(int i10) {
        this.f8829n = i10;
        getImpl().R(i10);
    }

    public void setRippleColor(int i10) {
        setRippleColor(ColorStateList.valueOf(i10));
    }

    @Override // android.view.View
    public void setScaleX(float f10) {
        super.setScaleX(f10);
        getImpl().H();
    }

    @Override // android.view.View
    public void setScaleY(float f10) {
        super.setScaleY(f10);
        getImpl().H();
    }

    public void setShadowPaddingEnabled(boolean z10) {
        getImpl().V(z10);
    }

    @Override // c4.Shapeable
    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        getImpl().W(shapeAppearanceModel);
    }

    public void setShowMotionSpec(MotionSpec motionSpec) {
        getImpl().X(motionSpec);
    }

    public void setShowMotionSpecResource(int i10) {
        setShowMotionSpec(MotionSpec.d(getContext(), i10));
    }

    public void setSize(int i10) {
        this.f8827l = 0;
        if (i10 != this.f8826k) {
            this.f8826k = i10;
            requestLayout();
        }
    }

    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        setBackgroundTintList(colorStateList);
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        setBackgroundTintMode(mode);
    }

    public void setSupportImageTintList(ColorStateList colorStateList) {
        if (this.f8822g != colorStateList) {
            this.f8822g = colorStateList;
            p();
        }
    }

    public void setSupportImageTintMode(PorterDuff.Mode mode) {
        if (this.f8823h != mode) {
            this.f8823h = mode;
            p();
        }
    }

    @Override // android.view.View
    public void setTranslationX(float f10) {
        super.setTranslationX(f10);
        getImpl().I();
    }

    @Override // android.view.View
    public void setTranslationY(float f10) {
        super.setTranslationY(f10);
        getImpl().I();
    }

    @Override // android.view.View
    public void setTranslationZ(float f10) {
        super.setTranslationZ(f10);
        getImpl().I();
    }

    public void setUseCompatPadding(boolean z10) {
        if (this.f8830o != z10) {
            this.f8830o = z10;
            getImpl().B();
        }
    }

    @Override // com.google.android.material.internal.VisibilityAwareImageButton, android.widget.ImageView, android.view.View
    public void setVisibility(int i10) {
        super.setVisibility(i10);
    }

    /* loaded from: classes.dex */
    protected static class BaseBehavior<T extends FloatingActionButton> extends CoordinatorLayout.Behavior<T> {

        /* renamed from: a, reason: collision with root package name */
        private Rect f8836a;

        /* renamed from: b, reason: collision with root package name */
        private b f8837b;

        /* renamed from: c, reason: collision with root package name */
        private boolean f8838c;

        public BaseBehavior() {
            this.f8838c = true;
        }

        private static boolean e(View view) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.e) {
                return ((CoordinatorLayout.e) layoutParams).f() instanceof BottomSheetBehavior;
            }
            return false;
        }

        private void f(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton) {
            int i10;
            Rect rect = floatingActionButton.f8831p;
            if (rect == null || rect.centerX() <= 0 || rect.centerY() <= 0) {
                return;
            }
            CoordinatorLayout.e eVar = (CoordinatorLayout.e) floatingActionButton.getLayoutParams();
            int i11 = 0;
            if (floatingActionButton.getRight() >= coordinatorLayout.getWidth() - ((ViewGroup.MarginLayoutParams) eVar).rightMargin) {
                i10 = rect.right;
            } else {
                i10 = floatingActionButton.getLeft() <= ((ViewGroup.MarginLayoutParams) eVar).leftMargin ? -rect.left : 0;
            }
            if (floatingActionButton.getBottom() >= coordinatorLayout.getHeight() - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin) {
                i11 = rect.bottom;
            } else if (floatingActionButton.getTop() <= ((ViewGroup.MarginLayoutParams) eVar).topMargin) {
                i11 = -rect.top;
            }
            if (i11 != 0) {
                ViewCompat.W(floatingActionButton, i11);
            }
            if (i10 != 0) {
                ViewCompat.V(floatingActionButton, i10);
            }
        }

        private boolean i(View view, FloatingActionButton floatingActionButton) {
            return this.f8838c && ((CoordinatorLayout.e) floatingActionButton.getLayoutParams()).e() == view.getId() && floatingActionButton.getUserSetVisibility() == 0;
        }

        private boolean j(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton) {
            if (!i(appBarLayout, floatingActionButton)) {
                return false;
            }
            if (this.f8836a == null) {
                this.f8836a = new Rect();
            }
            Rect rect = this.f8836a;
            DescendantOffsetUtils.getDescendantRect(coordinatorLayout, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                floatingActionButton.l(this.f8837b, false);
                return true;
            }
            floatingActionButton.s(this.f8837b, false);
            return true;
        }

        private boolean k(View view, FloatingActionButton floatingActionButton) {
            if (!i(view, floatingActionButton)) {
                return false;
            }
            if (view.getTop() < (floatingActionButton.getHeight() / 2) + ((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.e) floatingActionButton.getLayoutParams())).topMargin) {
                floatingActionButton.l(this.f8837b, false);
                return true;
            }
            floatingActionButton.s(this.f8837b, false);
            return true;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public boolean getInsetDodgeRect(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, Rect rect) {
            Rect rect2 = floatingActionButton.f8831p;
            rect.set(floatingActionButton.getLeft() + rect2.left, floatingActionButton.getTop() + rect2.top, floatingActionButton.getRight() - rect2.right, floatingActionButton.getBottom() - rect2.bottom);
            return true;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: g, reason: merged with bridge method [inline-methods] */
        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            if (view instanceof AppBarLayout) {
                j(coordinatorLayout, (AppBarLayout) view, floatingActionButton);
                return false;
            }
            if (!e(view)) {
                return false;
            }
            k(view, floatingActionButton);
            return false;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int i10) {
            List<View> v7 = coordinatorLayout.v(floatingActionButton);
            int size = v7.size();
            for (int i11 = 0; i11 < size; i11++) {
                View view = v7.get(i11);
                if (view instanceof AppBarLayout) {
                    if (j(coordinatorLayout, (AppBarLayout) view, floatingActionButton)) {
                        break;
                    }
                } else {
                    if (e(view) && k(view, floatingActionButton)) {
                        break;
                    }
                }
            }
            coordinatorLayout.M(floatingActionButton, i10);
            f(coordinatorLayout, floatingActionButton);
            return true;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onAttachedToLayoutParams(CoordinatorLayout.e eVar) {
            if (eVar.f2069h == 0) {
                eVar.f2069h = 80;
            }
        }

        public BaseBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.FloatingActionButton_Behavior_Layout);
            this.f8838c = obtainStyledAttributes.getBoolean(R$styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, true);
            obtainStyledAttributes.recycle();
        }
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.floatingActionButtonStyle);
    }

    public void setRippleColor(ColorStateList colorStateList) {
        if (this.f8824i != colorStateList) {
            this.f8824i = colorStateList;
            getImpl().U(this.f8824i);
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public FloatingActionButton(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r6), attributeSet, i10);
        int i11 = f8819u;
        this.f8831p = new Rect();
        this.f8832q = new Rect();
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.FloatingActionButton, i10, i11, new int[0]);
        this.f8820e = MaterialResources.a(context2, obtainStyledAttributes, R$styleable.FloatingActionButton_backgroundTint);
        this.f8821f = ViewUtils.parseTintMode(obtainStyledAttributes.getInt(R$styleable.FloatingActionButton_backgroundTintMode, -1), null);
        this.f8824i = MaterialResources.a(context2, obtainStyledAttributes, R$styleable.FloatingActionButton_rippleColor);
        this.f8826k = obtainStyledAttributes.getInt(R$styleable.FloatingActionButton_fabSize, -1);
        this.f8827l = obtainStyledAttributes.getDimensionPixelSize(R$styleable.FloatingActionButton_fabCustomSize, 0);
        this.f8825j = obtainStyledAttributes.getDimensionPixelSize(R$styleable.FloatingActionButton_borderWidth, 0);
        float dimension = obtainStyledAttributes.getDimension(R$styleable.FloatingActionButton_elevation, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(R$styleable.FloatingActionButton_hoveredFocusedTranslationZ, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(R$styleable.FloatingActionButton_pressedTranslationZ, 0.0f);
        this.f8830o = obtainStyledAttributes.getBoolean(R$styleable.FloatingActionButton_useCompatPadding, false);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.mtrl_fab_min_touch_target);
        setMaxImageSize(obtainStyledAttributes.getDimensionPixelSize(R$styleable.FloatingActionButton_maxImageSize, 0));
        MotionSpec c10 = MotionSpec.c(context2, obtainStyledAttributes, R$styleable.FloatingActionButton_showMotionSpec);
        MotionSpec c11 = MotionSpec.c(context2, obtainStyledAttributes, R$styleable.FloatingActionButton_hideMotionSpec);
        ShapeAppearanceModel m10 = ShapeAppearanceModel.g(context2, attributeSet, i10, i11, ShapeAppearanceModel.f4815m).m();
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.FloatingActionButton_ensureMinTouchTargetSize, false);
        setEnabled(obtainStyledAttributes.getBoolean(R$styleable.FloatingActionButton_android_enabled, true));
        obtainStyledAttributes.recycle();
        AppCompatImageHelper appCompatImageHelper = new AppCompatImageHelper(this);
        this.f8833r = appCompatImageHelper;
        appCompatImageHelper.g(attributeSet, i10);
        this.f8834s = new ExpandableWidgetHelper(this);
        getImpl().W(m10);
        getImpl().w(this.f8820e, this.f8821f, this.f8824i, this.f8825j);
        getImpl().S(dimensionPixelSize);
        getImpl().M(dimension);
        getImpl().P(dimension2);
        getImpl().T(dimension3);
        getImpl().X(c10);
        getImpl().O(c11);
        getImpl().N(z10);
        setScaleType(ImageView.ScaleType.MATRIX);
    }
}
