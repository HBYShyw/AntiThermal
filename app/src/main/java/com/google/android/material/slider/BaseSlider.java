package com.google.android.material.slider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.SeekBar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import c.AppCompatResources;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$attr;
import com.google.android.material.R$color;
import com.google.android.material.R$dimen;
import com.google.android.material.R$string;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewOverlayImpl;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.slider.BaseOnChangeListener;
import com.google.android.material.slider.BaseOnSliderTouchListener;
import com.google.android.material.slider.BaseSlider;
import d4.MaterialThemeOverlay;
import e4.TooltipDrawable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import p3.AnimationUtils;
import z3.MaterialResources;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class BaseSlider<S extends BaseSlider<S, L, T>, L extends BaseOnChangeListener<S>, T extends BaseOnSliderTouchListener<S>> extends View {

    /* renamed from: i0, reason: collision with root package name */
    private static final String f9144i0 = BaseSlider.class.getSimpleName();

    /* renamed from: j0, reason: collision with root package name */
    static final int f9145j0 = R$style.Widget_MaterialComponents_Slider;
    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    private float F;
    private MotionEvent G;
    private LabelFormatter H;
    private boolean I;
    private float J;
    private float K;
    private ArrayList<Float> L;
    private int M;
    private int N;
    private float O;
    private float[] P;
    private boolean Q;
    private int R;
    private boolean S;
    private boolean T;
    private boolean U;
    private ColorStateList V;
    private ColorStateList W;

    /* renamed from: a0, reason: collision with root package name */
    private ColorStateList f9146a0;

    /* renamed from: b0, reason: collision with root package name */
    private ColorStateList f9147b0;

    /* renamed from: c0, reason: collision with root package name */
    private ColorStateList f9148c0;

    /* renamed from: d0, reason: collision with root package name */
    private final MaterialShapeDrawable f9149d0;

    /* renamed from: e, reason: collision with root package name */
    private final Paint f9150e;

    /* renamed from: e0, reason: collision with root package name */
    private Drawable f9151e0;

    /* renamed from: f, reason: collision with root package name */
    private final Paint f9152f;

    /* renamed from: f0, reason: collision with root package name */
    private List<Drawable> f9153f0;

    /* renamed from: g, reason: collision with root package name */
    private final Paint f9154g;

    /* renamed from: g0, reason: collision with root package name */
    private float f9155g0;

    /* renamed from: h, reason: collision with root package name */
    private final Paint f9156h;

    /* renamed from: h0, reason: collision with root package name */
    private int f9157h0;

    /* renamed from: i, reason: collision with root package name */
    private final Paint f9158i;

    /* renamed from: j, reason: collision with root package name */
    private final Paint f9159j;

    /* renamed from: k, reason: collision with root package name */
    private final e f9160k;

    /* renamed from: l, reason: collision with root package name */
    private final AccessibilityManager f9161l;

    /* renamed from: m, reason: collision with root package name */
    private BaseSlider<S, L, T>.d f9162m;

    /* renamed from: n, reason: collision with root package name */
    private final f f9163n;

    /* renamed from: o, reason: collision with root package name */
    private final List<TooltipDrawable> f9164o;

    /* renamed from: p, reason: collision with root package name */
    private final List<L> f9165p;

    /* renamed from: q, reason: collision with root package name */
    private final List<T> f9166q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f9167r;

    /* renamed from: s, reason: collision with root package name */
    private ValueAnimator f9168s;

    /* renamed from: t, reason: collision with root package name */
    private ValueAnimator f9169t;

    /* renamed from: u, reason: collision with root package name */
    private final int f9170u;

    /* renamed from: v, reason: collision with root package name */
    private int f9171v;

    /* renamed from: w, reason: collision with root package name */
    private int f9172w;

    /* renamed from: x, reason: collision with root package name */
    private int f9173x;

    /* renamed from: y, reason: collision with root package name */
    private int f9174y;

    /* renamed from: z, reason: collision with root package name */
    private int f9175z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SliderState extends View.BaseSavedState {
        public static final Parcelable.Creator<SliderState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        float f9176e;

        /* renamed from: f, reason: collision with root package name */
        float f9177f;

        /* renamed from: g, reason: collision with root package name */
        ArrayList<Float> f9178g;

        /* renamed from: h, reason: collision with root package name */
        float f9179h;

        /* renamed from: i, reason: collision with root package name */
        boolean f9180i;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SliderState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SliderState createFromParcel(Parcel parcel) {
                return new SliderState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SliderState[] newArray(int i10) {
                return new SliderState[i10];
            }
        }

        /* synthetic */ SliderState(Parcel parcel, a aVar) {
            this(parcel);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeFloat(this.f9176e);
            parcel.writeFloat(this.f9177f);
            parcel.writeList(this.f9178g);
            parcel.writeFloat(this.f9179h);
            parcel.writeBooleanArray(new boolean[]{this.f9180i});
        }

        SliderState(Parcelable parcelable) {
            super(parcelable);
        }

        private SliderState(Parcel parcel) {
            super(parcel);
            this.f9176e = parcel.readFloat();
            this.f9177f = parcel.readFloat();
            ArrayList<Float> arrayList = new ArrayList<>();
            this.f9178g = arrayList;
            parcel.readList(arrayList, Float.class.getClassLoader());
            this.f9179h = parcel.readFloat();
            this.f9180i = parcel.createBooleanArray()[0];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements f {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ AttributeSet f9181a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f9182b;

        a(AttributeSet attributeSet, int i10) {
            this.f9181a = attributeSet;
            this.f9182b = i10;
        }

        @Override // com.google.android.material.slider.BaseSlider.f
        public TooltipDrawable a() {
            TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(BaseSlider.this.getContext(), this.f9181a, R$styleable.Slider, this.f9182b, BaseSlider.f9145j0, new int[0]);
            TooltipDrawable V = BaseSlider.V(BaseSlider.this.getContext(), obtainStyledAttributes);
            obtainStyledAttributes.recycle();
            return V;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            Iterator it = BaseSlider.this.f9164o.iterator();
            while (it.hasNext()) {
                ((TooltipDrawable) it.next()).C0(floatValue);
            }
            ViewCompat.b0(BaseSlider.this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {
        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            Iterator it = BaseSlider.this.f9164o.iterator();
            while (it.hasNext()) {
                ViewUtils.getContentViewOverlay(BaseSlider.this).remove((TooltipDrawable) it.next());
            }
        }
    }

    /* loaded from: classes.dex */
    private static class e extends ExploreByTouchHelper {

        /* renamed from: a, reason: collision with root package name */
        private final BaseSlider<?, ?, ?> f9188a;

        /* renamed from: b, reason: collision with root package name */
        final Rect f9189b;

        e(BaseSlider<?, ?, ?> baseSlider) {
            super(baseSlider);
            this.f9189b = new Rect();
            this.f9188a = baseSlider;
        }

        private String a(int i10) {
            if (i10 == this.f9188a.getValues().size() - 1) {
                return this.f9188a.getContext().getString(R$string.material_slider_range_end);
            }
            return i10 == 0 ? this.f9188a.getContext().getString(R$string.material_slider_range_start) : "";
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            for (int i10 = 0; i10 < this.f9188a.getValues().size(); i10++) {
                this.f9188a.h0(i10, this.f9189b);
                if (this.f9189b.contains((int) f10, (int) f11)) {
                    return i10;
                }
            }
            return -1;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            for (int i10 = 0; i10 < this.f9188a.getValues().size(); i10++) {
                list.add(Integer.valueOf(i10));
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            if (!this.f9188a.isEnabled()) {
                return false;
            }
            if (i11 != 4096 && i11 != 8192) {
                if (i11 == 16908349 && bundle != null && bundle.containsKey("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE")) {
                    if (this.f9188a.f0(i10, bundle.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"))) {
                        this.f9188a.i0();
                        this.f9188a.postInvalidate();
                        invalidateVirtualView(i10);
                        return true;
                    }
                }
                return false;
            }
            float l10 = this.f9188a.l(20);
            if (i11 == 8192) {
                l10 = -l10;
            }
            if (this.f9188a.J()) {
                l10 = -l10;
            }
            if (!this.f9188a.f0(i10, q.a.a(this.f9188a.getValues().get(i10).floatValue() + l10, this.f9188a.getValueFrom(), this.f9188a.getValueTo()))) {
                return false;
            }
            this.f9188a.i0();
            this.f9188a.postInvalidate();
            invalidateVirtualView(i10);
            return true;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.b(AccessibilityNodeInfoCompat.a.L);
            List<Float> values = this.f9188a.getValues();
            float floatValue = values.get(i10).floatValue();
            float valueFrom = this.f9188a.getValueFrom();
            float valueTo = this.f9188a.getValueTo();
            if (this.f9188a.isEnabled()) {
                if (floatValue > valueFrom) {
                    accessibilityNodeInfoCompat.a(8192);
                }
                if (floatValue < valueTo) {
                    accessibilityNodeInfoCompat.a(4096);
                }
            }
            accessibilityNodeInfoCompat.p0(AccessibilityNodeInfoCompat.d.a(1, valueFrom, valueTo, floatValue));
            accessibilityNodeInfoCompat.V(SeekBar.class.getName());
            StringBuilder sb2 = new StringBuilder();
            if (this.f9188a.getContentDescription() != null) {
                sb2.append(this.f9188a.getContentDescription());
                sb2.append(",");
            }
            if (values.size() > 1) {
                sb2.append(a(i10));
                sb2.append(this.f9188a.A(floatValue));
            }
            accessibilityNodeInfoCompat.Z(sb2.toString());
            this.f9188a.h0(i10, this.f9189b);
            accessibilityNodeInfoCompat.Q(this.f9189b);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface f {
        TooltipDrawable a();
    }

    public BaseSlider(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String A(float f10) {
        if (E()) {
            return this.H.a(f10);
        }
        return String.format(((float) ((int) f10)) == f10 ? "%.0f" : "%.2f", Float.valueOf(f10));
    }

    private static float B(ValueAnimator valueAnimator, float f10) {
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return f10;
        }
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        valueAnimator.cancel();
        return floatValue;
    }

    private float C(int i10, float f10) {
        float minSeparation = getMinSeparation();
        if (this.f9157h0 == 0) {
            minSeparation = q(minSeparation);
        }
        if (J()) {
            minSeparation = -minSeparation;
        }
        int i11 = i10 + 1;
        int i12 = i10 - 1;
        return q.a.a(f10, i12 < 0 ? this.J : this.L.get(i12).floatValue() + minSeparation, i11 >= this.L.size() ? this.K : this.L.get(i11).floatValue() - minSeparation);
    }

    private int D(ColorStateList colorStateList) {
        return colorStateList.getColorForState(getDrawableState(), colorStateList.getDefaultColor());
    }

    private Drawable F(Drawable drawable) {
        Drawable newDrawable = drawable.mutate().getConstantState().newDrawable();
        h(newDrawable);
        return newDrawable;
    }

    private void G() {
        this.f9150e.setStrokeWidth(this.f9175z);
        this.f9152f.setStrokeWidth(this.f9175z);
        this.f9158i.setStrokeWidth(this.f9175z / 2.0f);
        this.f9159j.setStrokeWidth(this.f9175z / 2.0f);
    }

    private boolean H() {
        ViewParent parent = getParent();
        while (true) {
            if (!(parent instanceof ViewGroup)) {
                return false;
            }
            ViewGroup viewGroup = (ViewGroup) parent;
            if ((viewGroup.canScrollVertically(1) || viewGroup.canScrollVertically(-1)) && viewGroup.shouldDelayChildPressedState()) {
                return true;
            }
            parent = parent.getParent();
        }
    }

    private boolean I(float f10) {
        double doubleValue = new BigDecimal(Float.toString(f10)).divide(new BigDecimal(Float.toString(this.O)), MathContext.DECIMAL64).doubleValue();
        return Math.abs(((double) Math.round(doubleValue)) - doubleValue) < 1.0E-4d;
    }

    private void K(Resources resources) {
        this.f9173x = resources.getDimensionPixelSize(R$dimen.mtrl_slider_widget_height);
        int dimensionPixelOffset = resources.getDimensionPixelOffset(R$dimen.mtrl_slider_track_side_padding);
        this.f9171v = dimensionPixelOffset;
        this.A = dimensionPixelOffset;
        this.f9172w = resources.getDimensionPixelSize(R$dimen.mtrl_slider_thumb_radius);
        this.B = resources.getDimensionPixelOffset(R$dimen.mtrl_slider_track_top);
        this.E = resources.getDimensionPixelSize(R$dimen.mtrl_slider_label_padding);
    }

    private void L() {
        if (this.O <= 0.0f) {
            return;
        }
        k0();
        int min = Math.min((int) (((this.K - this.J) / this.O) + 1.0f), (this.R / (this.f9175z * 2)) + 1);
        float[] fArr = this.P;
        if (fArr == null || fArr.length != min * 2) {
            this.P = new float[min * 2];
        }
        float f10 = this.R / (min - 1);
        for (int i10 = 0; i10 < min * 2; i10 += 2) {
            float[] fArr2 = this.P;
            fArr2[i10] = this.A + ((i10 / 2) * f10);
            fArr2[i10 + 1] = m();
        }
    }

    private void M(Canvas canvas, int i10, int i11) {
        if (c0()) {
            canvas.drawCircle((int) (this.A + (R(this.L.get(this.N).floatValue()) * i10)), i11, this.D, this.f9156h);
        }
    }

    private void N(Canvas canvas) {
        if (!this.Q || this.O <= 0.0f) {
            return;
        }
        float[] activeRange = getActiveRange();
        int X = X(this.P, activeRange[0]);
        int X2 = X(this.P, activeRange[1]);
        int i10 = X * 2;
        canvas.drawPoints(this.P, 0, i10, this.f9158i);
        int i11 = X2 * 2;
        canvas.drawPoints(this.P, i10, i11 - i10, this.f9159j);
        float[] fArr = this.P;
        canvas.drawPoints(fArr, i11, fArr.length - i11, this.f9158i);
    }

    private void O() {
        this.A = this.f9171v + Math.max(this.C - this.f9172w, 0);
        if (ViewCompat.Q(this)) {
            j0(getWidth());
        }
    }

    private boolean P(int i10) {
        int i11 = this.N;
        int c10 = (int) q.a.c(i11 + i10, 0L, this.L.size() - 1);
        this.N = c10;
        if (c10 == i11) {
            return false;
        }
        if (this.M != -1) {
            this.M = c10;
        }
        i0();
        postInvalidate();
        return true;
    }

    private boolean Q(int i10) {
        if (J()) {
            i10 = i10 == Integer.MIN_VALUE ? Integer.MAX_VALUE : -i10;
        }
        return P(i10);
    }

    private float R(float f10) {
        float f11 = this.J;
        float f12 = (f10 - f11) / (this.K - f11);
        return J() ? 1.0f - f12 : f12;
    }

    private Boolean S(int i10, KeyEvent keyEvent) {
        if (i10 != 61) {
            if (i10 != 66) {
                if (i10 != 81) {
                    if (i10 == 69) {
                        P(-1);
                        return Boolean.TRUE;
                    }
                    if (i10 != 70) {
                        switch (i10) {
                            case 21:
                                Q(-1);
                                return Boolean.TRUE;
                            case 22:
                                Q(1);
                                return Boolean.TRUE;
                            case 23:
                                break;
                            default:
                                return null;
                        }
                    }
                }
                P(1);
                return Boolean.TRUE;
            }
            this.M = this.N;
            postInvalidate();
            return Boolean.TRUE;
        }
        if (keyEvent.hasNoModifiers()) {
            return Boolean.valueOf(P(1));
        }
        if (keyEvent.isShiftPressed()) {
            return Boolean.valueOf(P(-1));
        }
        return Boolean.FALSE;
    }

    private void T() {
        Iterator<T> it = this.f9166q.iterator();
        while (it.hasNext()) {
            it.next().a(this);
        }
    }

    private void U() {
        Iterator<T> it = this.f9166q.iterator();
        while (it.hasNext()) {
            it.next().b(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static TooltipDrawable V(Context context, TypedArray typedArray) {
        return TooltipDrawable.v0(context, null, 0, typedArray.getResourceId(R$styleable.Slider_labelStyle, R$style.Widget_MaterialComponents_Tooltip));
    }

    private static int X(float[] fArr, float f10) {
        return Math.round(f10 * ((fArr.length / 2) - 1));
    }

    private void Y(Context context, AttributeSet attributeSet, int i10) {
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R$styleable.Slider, i10, f9145j0, new int[0]);
        this.J = obtainStyledAttributes.getFloat(R$styleable.Slider_android_valueFrom, 0.0f);
        this.K = obtainStyledAttributes.getFloat(R$styleable.Slider_android_valueTo, 1.0f);
        setValues(Float.valueOf(this.J));
        this.O = obtainStyledAttributes.getFloat(R$styleable.Slider_android_stepSize, 0.0f);
        int i11 = R$styleable.Slider_trackColor;
        boolean hasValue = obtainStyledAttributes.hasValue(i11);
        int i12 = hasValue ? i11 : R$styleable.Slider_trackColorInactive;
        if (!hasValue) {
            i11 = R$styleable.Slider_trackColorActive;
        }
        ColorStateList a10 = MaterialResources.a(context, obtainStyledAttributes, i12);
        if (a10 == null) {
            a10 = AppCompatResources.a(context, R$color.material_slider_inactive_track_color);
        }
        setTrackInactiveTintList(a10);
        ColorStateList a11 = MaterialResources.a(context, obtainStyledAttributes, i11);
        if (a11 == null) {
            a11 = AppCompatResources.a(context, R$color.material_slider_active_track_color);
        }
        setTrackActiveTintList(a11);
        this.f9149d0.a0(MaterialResources.a(context, obtainStyledAttributes, R$styleable.Slider_thumbColor));
        int i13 = R$styleable.Slider_thumbStrokeColor;
        if (obtainStyledAttributes.hasValue(i13)) {
            setThumbStrokeColor(MaterialResources.a(context, obtainStyledAttributes, i13));
        }
        setThumbStrokeWidth(obtainStyledAttributes.getDimension(R$styleable.Slider_thumbStrokeWidth, 0.0f));
        ColorStateList a12 = MaterialResources.a(context, obtainStyledAttributes, R$styleable.Slider_haloColor);
        if (a12 == null) {
            a12 = AppCompatResources.a(context, R$color.material_slider_halo_color);
        }
        setHaloTintList(a12);
        this.Q = obtainStyledAttributes.getBoolean(R$styleable.Slider_tickVisible, true);
        int i14 = R$styleable.Slider_tickColor;
        boolean hasValue2 = obtainStyledAttributes.hasValue(i14);
        int i15 = hasValue2 ? i14 : R$styleable.Slider_tickColorInactive;
        if (!hasValue2) {
            i14 = R$styleable.Slider_tickColorActive;
        }
        ColorStateList a13 = MaterialResources.a(context, obtainStyledAttributes, i15);
        if (a13 == null) {
            a13 = AppCompatResources.a(context, R$color.material_slider_inactive_tick_marks_color);
        }
        setTickInactiveTintList(a13);
        ColorStateList a14 = MaterialResources.a(context, obtainStyledAttributes, i14);
        if (a14 == null) {
            a14 = AppCompatResources.a(context, R$color.material_slider_active_tick_marks_color);
        }
        setTickActiveTintList(a14);
        setThumbRadius(obtainStyledAttributes.getDimensionPixelSize(R$styleable.Slider_thumbRadius, 0));
        setHaloRadius(obtainStyledAttributes.getDimensionPixelSize(R$styleable.Slider_haloRadius, 0));
        setThumbElevation(obtainStyledAttributes.getDimension(R$styleable.Slider_thumbElevation, 0.0f));
        setTrackHeight(obtainStyledAttributes.getDimensionPixelSize(R$styleable.Slider_trackHeight, 0));
        setLabelBehavior(obtainStyledAttributes.getInt(R$styleable.Slider_labelBehavior, 0));
        if (!obtainStyledAttributes.getBoolean(R$styleable.Slider_android_enabled, true)) {
            setEnabled(false);
        }
        obtainStyledAttributes.recycle();
    }

    private void Z(int i10) {
        BaseSlider<S, L, T>.d dVar = this.f9162m;
        if (dVar == null) {
            this.f9162m = new d(this, null);
        } else {
            removeCallbacks(dVar);
        }
        this.f9162m.a(i10);
        postDelayed(this.f9162m, 200L);
    }

    private void a0(TooltipDrawable tooltipDrawable, float f10) {
        tooltipDrawable.D0(A(f10));
        int R = (this.A + ((int) (R(f10) * this.R))) - (tooltipDrawable.getIntrinsicWidth() / 2);
        int m10 = m() - (this.E + this.C);
        tooltipDrawable.setBounds(R, m10 - tooltipDrawable.getIntrinsicHeight(), tooltipDrawable.getIntrinsicWidth() + R, m10);
        Rect rect = new Rect(tooltipDrawable.getBounds());
        DescendantOffsetUtils.offsetDescendantRect(ViewUtils.getContentView(this), this, rect);
        tooltipDrawable.setBounds(rect);
        ViewUtils.getContentViewOverlay(this).add(tooltipDrawable);
    }

    private boolean b0() {
        return this.f9174y == 3;
    }

    private boolean c0() {
        return this.S || !(getBackground() instanceof RippleDrawable);
    }

    private boolean d0(float f10) {
        return f0(this.M, f10);
    }

    private double e0(float f10) {
        float f11 = this.O;
        if (f11 <= 0.0f) {
            return f10;
        }
        return Math.round(f10 * r2) / ((int) ((this.K - this.J) / f11));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean f0(int i10, float f10) {
        this.N = i10;
        if (Math.abs(f10 - this.L.get(i10).floatValue()) < 1.0E-4d) {
            return false;
        }
        this.L.set(i10, Float.valueOf(C(i10, f10)));
        r(i10);
        return true;
    }

    private boolean g0() {
        return d0(getValueOfTouchPosition());
    }

    private float[] getActiveRange() {
        float floatValue = ((Float) Collections.max(getValues())).floatValue();
        float floatValue2 = ((Float) Collections.min(getValues())).floatValue();
        if (this.L.size() == 1) {
            floatValue2 = this.J;
        }
        float R = R(floatValue2);
        float R2 = R(floatValue);
        return J() ? new float[]{R2, R} : new float[]{R, R2};
    }

    private float getValueOfTouchPosition() {
        double e02 = e0(this.f9155g0);
        if (J()) {
            e02 = 1.0d - e02;
        }
        float f10 = this.K;
        return (float) ((e02 * (f10 - r4)) + this.J);
    }

    private float getValueOfTouchPositionAbsolute() {
        float f10 = this.f9155g0;
        if (J()) {
            f10 = 1.0f - f10;
        }
        float f11 = this.K;
        float f12 = this.J;
        return (f10 * (f11 - f12)) + f12;
    }

    private void h(Drawable drawable) {
        int i10 = this.C * 2;
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth == -1 && intrinsicHeight == -1) {
            drawable.setBounds(0, 0, i10, i10);
        } else {
            float max = i10 / Math.max(intrinsicWidth, intrinsicHeight);
            drawable.setBounds(0, 0, (int) (intrinsicWidth * max), (int) (intrinsicHeight * max));
        }
    }

    private void i(TooltipDrawable tooltipDrawable) {
        tooltipDrawable.B0(ViewUtils.getContentView(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i0() {
        if (c0() || getMeasuredWidth() <= 0) {
            return;
        }
        Drawable background = getBackground();
        if (background instanceof RippleDrawable) {
            int R = (int) ((R(this.L.get(this.N).floatValue()) * this.R) + this.A);
            int m10 = m();
            int i10 = this.D;
            DrawableCompat.f(background, R - i10, m10 - i10, R + i10, m10 + i10);
        }
    }

    private Float j(int i10) {
        float l10 = this.T ? l(20) : k();
        if (i10 == 21) {
            if (!J()) {
                l10 = -l10;
            }
            return Float.valueOf(l10);
        }
        if (i10 == 22) {
            if (J()) {
                l10 = -l10;
            }
            return Float.valueOf(l10);
        }
        if (i10 == 69) {
            return Float.valueOf(-l10);
        }
        if (i10 == 70 || i10 == 81) {
            return Float.valueOf(l10);
        }
        return null;
    }

    private void j0(int i10) {
        this.R = Math.max(i10 - (this.A * 2), 0);
        L();
    }

    private float k() {
        float f10 = this.O;
        if (f10 == 0.0f) {
            return 1.0f;
        }
        return f10;
    }

    private void k0() {
        if (this.U) {
            n0();
            o0();
            m0();
            p0();
            l0();
            s0();
            this.U = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float l(int i10) {
        float k10 = k();
        return (this.K - this.J) / k10 <= i10 ? k10 : Math.round(r1 / r2) * k10;
    }

    private void l0() {
        float minSeparation = getMinSeparation();
        if (minSeparation >= 0.0f) {
            float f10 = this.O;
            if (f10 <= 0.0f || minSeparation <= 0.0f) {
                return;
            }
            if (this.f9157h0 == 1) {
                if (minSeparation < f10 || !I(minSeparation)) {
                    throw new IllegalStateException(String.format("minSeparation(%s) must be greater or equal and a multiple of stepSize(%s) when using stepSize(%s)", Float.valueOf(minSeparation), Float.valueOf(this.O), Float.valueOf(this.O)));
                }
                return;
            }
            throw new IllegalStateException(String.format("minSeparation(%s) cannot be set as a dimension when using stepSize(%s)", Float.valueOf(minSeparation), Float.valueOf(this.O)));
        }
        throw new IllegalStateException(String.format("minSeparation(%s) must be greater or equal to 0", Float.valueOf(minSeparation)));
    }

    private int m() {
        return this.B + ((this.f9174y == 1 || b0()) ? this.f9164o.get(0).getIntrinsicHeight() : 0);
    }

    private void m0() {
        if (this.O > 0.0f && !q0(this.K)) {
            throw new IllegalStateException(String.format("The stepSize(%s) must be 0, or a factor of the valueFrom(%s)-valueTo(%s) range", Float.valueOf(this.O), Float.valueOf(this.J), Float.valueOf(this.K)));
        }
    }

    private ValueAnimator n(boolean z10) {
        TimeInterpolator timeInterpolator;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(B(z10 ? this.f9169t : this.f9168s, z10 ? 0.0f : 1.0f), z10 ? 1.0f : 0.0f);
        ofFloat.setDuration(z10 ? 83L : 117L);
        if (z10) {
            timeInterpolator = AnimationUtils.f16559e;
        } else {
            timeInterpolator = AnimationUtils.f16557c;
        }
        ofFloat.setInterpolator(timeInterpolator);
        ofFloat.addUpdateListener(new b());
        return ofFloat;
    }

    private void n0() {
        if (this.J >= this.K) {
            throw new IllegalStateException(String.format("valueFrom(%s) must be smaller than valueTo(%s)", Float.valueOf(this.J), Float.valueOf(this.K)));
        }
    }

    private void o() {
        if (this.f9164o.size() > this.L.size()) {
            List<TooltipDrawable> subList = this.f9164o.subList(this.L.size(), this.f9164o.size());
            for (TooltipDrawable tooltipDrawable : subList) {
                if (ViewCompat.P(this)) {
                    p(tooltipDrawable);
                }
            }
            subList.clear();
        }
        while (this.f9164o.size() < this.L.size()) {
            TooltipDrawable a10 = this.f9163n.a();
            this.f9164o.add(a10);
            if (ViewCompat.P(this)) {
                i(a10);
            }
        }
        int i10 = this.f9164o.size() == 1 ? 0 : 1;
        Iterator<TooltipDrawable> it = this.f9164o.iterator();
        while (it.hasNext()) {
            it.next().n0(i10);
        }
    }

    private void o0() {
        if (this.K <= this.J) {
            throw new IllegalStateException(String.format("valueTo(%s) must be greater than valueFrom(%s)", Float.valueOf(this.K), Float.valueOf(this.J)));
        }
    }

    private void p(TooltipDrawable tooltipDrawable) {
        ViewOverlayImpl contentViewOverlay = ViewUtils.getContentViewOverlay(this);
        if (contentViewOverlay != null) {
            contentViewOverlay.remove(tooltipDrawable);
            tooltipDrawable.x0(ViewUtils.getContentView(this));
        }
    }

    private void p0() {
        Iterator<Float> it = this.L.iterator();
        while (it.hasNext()) {
            Float next = it.next();
            if (next.floatValue() < this.J || next.floatValue() > this.K) {
                throw new IllegalStateException(String.format("Slider value(%s) must be greater or equal to valueFrom(%s), and lower or equal to valueTo(%s)", next, Float.valueOf(this.J), Float.valueOf(this.K)));
            }
            if (this.O > 0.0f && !q0(next.floatValue())) {
                throw new IllegalStateException(String.format("Value(%s) must be equal to valueFrom(%s) plus a multiple of stepSize(%s) when using stepSize(%s)", next, Float.valueOf(this.J), Float.valueOf(this.O), Float.valueOf(this.O)));
            }
        }
    }

    private float q(float f10) {
        if (f10 == 0.0f) {
            return 0.0f;
        }
        float f11 = (f10 - this.A) / this.R;
        float f12 = this.J;
        return (f11 * (f12 - this.K)) + f12;
    }

    private boolean q0(float f10) {
        return I(f10 - this.J);
    }

    private void r(int i10) {
        Iterator<L> it = this.f9165p.iterator();
        while (it.hasNext()) {
            it.next().a(this, this.L.get(i10).floatValue(), true);
        }
        AccessibilityManager accessibilityManager = this.f9161l;
        if (accessibilityManager == null || !accessibilityManager.isEnabled()) {
            return;
        }
        Z(i10);
    }

    private float r0(float f10) {
        return (R(f10) * this.R) + this.A;
    }

    private void s() {
        for (L l10 : this.f9165p) {
            Iterator<Float> it = this.L.iterator();
            while (it.hasNext()) {
                l10.a(this, it.next().floatValue(), false);
            }
        }
    }

    private void s0() {
        float f10 = this.O;
        if (f10 == 0.0f) {
            return;
        }
        if (((int) f10) != f10) {
            Log.w(f9144i0, String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the value correctly.", "stepSize", Float.valueOf(f10)));
        }
        float f11 = this.J;
        if (((int) f11) != f11) {
            Log.w(f9144i0, String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the value correctly.", "valueFrom", Float.valueOf(f11)));
        }
        float f12 = this.K;
        if (((int) f12) != f12) {
            Log.w(f9144i0, String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the value correctly.", "valueTo", Float.valueOf(f12)));
        }
    }

    private void setValuesInternal(ArrayList<Float> arrayList) {
        if (!arrayList.isEmpty()) {
            Collections.sort(arrayList);
            if (this.L.size() == arrayList.size() && this.L.equals(arrayList)) {
                return;
            }
            this.L = arrayList;
            this.U = true;
            this.N = 0;
            i0();
            o();
            s();
            postInvalidate();
            return;
        }
        throw new IllegalArgumentException("At least one value must be set");
    }

    private void t(Canvas canvas, int i10, int i11) {
        float[] activeRange = getActiveRange();
        int i12 = this.A;
        float f10 = i10;
        float f11 = i11;
        canvas.drawLine(i12 + (activeRange[0] * f10), f11, i12 + (activeRange[1] * f10), f11, this.f9152f);
    }

    private void u(Canvas canvas, int i10, int i11) {
        float[] activeRange = getActiveRange();
        float f10 = i10;
        float f11 = this.A + (activeRange[1] * f10);
        if (f11 < r1 + i10) {
            float f12 = i11;
            canvas.drawLine(f11, f12, r1 + i10, f12, this.f9150e);
        }
        int i12 = this.A;
        float f13 = i12 + (activeRange[0] * f10);
        if (f13 > i12) {
            float f14 = i11;
            canvas.drawLine(i12, f14, f13, f14, this.f9150e);
        }
    }

    private void v(Canvas canvas, int i10, int i11, float f10, Drawable drawable) {
        canvas.save();
        canvas.translate((this.A + ((int) (R(f10) * i10))) - (drawable.getBounds().width() / 2.0f), i11 - (drawable.getBounds().height() / 2.0f));
        drawable.draw(canvas);
        canvas.restore();
    }

    private void w(Canvas canvas, int i10, int i11) {
        for (int i12 = 0; i12 < this.L.size(); i12++) {
            float floatValue = this.L.get(i12).floatValue();
            Drawable drawable = this.f9151e0;
            if (drawable != null) {
                v(canvas, i10, i11, floatValue, drawable);
            } else if (i12 < this.f9153f0.size()) {
                v(canvas, i10, i11, floatValue, this.f9153f0.get(i12));
            } else {
                if (!isEnabled()) {
                    canvas.drawCircle(this.A + (R(floatValue) * i10), i11, this.C, this.f9154g);
                }
                v(canvas, i10, i11, floatValue, this.f9149d0);
            }
        }
    }

    private void x() {
        if (this.f9174y == 2) {
            return;
        }
        if (!this.f9167r) {
            this.f9167r = true;
            ValueAnimator n10 = n(true);
            this.f9168s = n10;
            this.f9169t = null;
            n10.start();
        }
        Iterator<TooltipDrawable> it = this.f9164o.iterator();
        for (int i10 = 0; i10 < this.L.size() && it.hasNext(); i10++) {
            if (i10 != this.N) {
                a0(it.next(), this.L.get(i10).floatValue());
            }
        }
        if (it.hasNext()) {
            a0(it.next(), this.L.get(this.N).floatValue());
            return;
        }
        throw new IllegalStateException(String.format("Not enough labels(%d) to display all the values(%d)", Integer.valueOf(this.f9164o.size()), Integer.valueOf(this.L.size())));
    }

    private void y() {
        if (this.f9167r) {
            this.f9167r = false;
            ValueAnimator n10 = n(false);
            this.f9169t = n10;
            this.f9168s = null;
            n10.addListener(new c());
            this.f9169t.start();
        }
    }

    private void z(int i10) {
        if (i10 == 1) {
            P(Integer.MAX_VALUE);
            return;
        }
        if (i10 == 2) {
            P(Integer.MIN_VALUE);
        } else if (i10 == 17) {
            Q(Integer.MAX_VALUE);
        } else {
            if (i10 != 66) {
                return;
            }
            Q(Integer.MIN_VALUE);
        }
    }

    public boolean E() {
        return this.H != null;
    }

    final boolean J() {
        return ViewCompat.x(this) == 1;
    }

    protected boolean W() {
        if (this.M != -1) {
            return true;
        }
        float valueOfTouchPositionAbsolute = getValueOfTouchPositionAbsolute();
        float r02 = r0(valueOfTouchPositionAbsolute);
        this.M = 0;
        float abs = Math.abs(this.L.get(0).floatValue() - valueOfTouchPositionAbsolute);
        for (int i10 = 1; i10 < this.L.size(); i10++) {
            float abs2 = Math.abs(this.L.get(i10).floatValue() - valueOfTouchPositionAbsolute);
            float r03 = r0(this.L.get(i10).floatValue());
            if (Float.compare(abs2, abs) > 1) {
                break;
            }
            boolean z10 = !J() ? r03 - r02 >= 0.0f : r03 - r02 <= 0.0f;
            if (Float.compare(abs2, abs) < 0) {
                this.M = i10;
            } else {
                if (Float.compare(abs2, abs) != 0) {
                    continue;
                } else {
                    if (Math.abs(r03 - r02) < this.f9170u) {
                        this.M = -1;
                        return false;
                    }
                    if (z10) {
                        this.M = i10;
                    }
                }
            }
            abs = abs2;
        }
        return this.M != -1;
    }

    @Override // android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.f9160k.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.f9150e.setColor(D(this.f9148c0));
        this.f9152f.setColor(D(this.f9147b0));
        this.f9158i.setColor(D(this.f9146a0));
        this.f9159j.setColor(D(this.W));
        for (TooltipDrawable tooltipDrawable : this.f9164o) {
            if (tooltipDrawable.isStateful()) {
                tooltipDrawable.setState(getDrawableState());
            }
        }
        if (this.f9149d0.isStateful()) {
            this.f9149d0.setState(getDrawableState());
        }
        this.f9156h.setColor(D(this.V));
        this.f9156h.setAlpha(63);
    }

    @Override // android.view.View
    public CharSequence getAccessibilityClassName() {
        return SeekBar.class.getName();
    }

    final int getAccessibilityFocusedVirtualViewId() {
        return this.f9160k.getAccessibilityFocusedVirtualViewId();
    }

    public int getActiveThumbIndex() {
        return this.M;
    }

    public int getFocusedThumbIndex() {
        return this.N;
    }

    public int getHaloRadius() {
        return this.D;
    }

    public ColorStateList getHaloTintList() {
        return this.V;
    }

    public int getLabelBehavior() {
        return this.f9174y;
    }

    protected float getMinSeparation() {
        return 0.0f;
    }

    public float getStepSize() {
        return this.O;
    }

    public float getThumbElevation() {
        return this.f9149d0.v();
    }

    public int getThumbRadius() {
        return this.C;
    }

    public ColorStateList getThumbStrokeColor() {
        return this.f9149d0.E();
    }

    public float getThumbStrokeWidth() {
        return this.f9149d0.G();
    }

    public ColorStateList getThumbTintList() {
        return this.f9149d0.w();
    }

    public ColorStateList getTickActiveTintList() {
        return this.W;
    }

    public ColorStateList getTickInactiveTintList() {
        return this.f9146a0;
    }

    public ColorStateList getTickTintList() {
        if (this.f9146a0.equals(this.W)) {
            return this.W;
        }
        throw new IllegalStateException("The inactive and active ticks are different colors. Use the getTickColorInactive() and getTickColorActive() methods instead.");
    }

    public ColorStateList getTrackActiveTintList() {
        return this.f9147b0;
    }

    public int getTrackHeight() {
        return this.f9175z;
    }

    public ColorStateList getTrackInactiveTintList() {
        return this.f9148c0;
    }

    public int getTrackSidePadding() {
        return this.A;
    }

    public ColorStateList getTrackTintList() {
        if (this.f9148c0.equals(this.f9147b0)) {
            return this.f9147b0;
        }
        throw new IllegalStateException("The inactive and active parts of the track are different colors. Use the getInactiveTrackColor() and getActiveTrackColor() methods instead.");
    }

    public int getTrackWidth() {
        return this.R;
    }

    public float getValueFrom() {
        return this.J;
    }

    public float getValueTo() {
        return this.K;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Float> getValues() {
        return new ArrayList(this.L);
    }

    void h0(int i10, Rect rect) {
        int R = this.A + ((int) (R(getValues().get(i10).floatValue()) * this.R));
        int m10 = m();
        int i11 = this.C;
        rect.set(R - i11, m10 - i11, R + i11, m10 + i11);
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Iterator<TooltipDrawable> it = this.f9164o.iterator();
        while (it.hasNext()) {
            i(it.next());
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        BaseSlider<S, L, T>.d dVar = this.f9162m;
        if (dVar != null) {
            removeCallbacks(dVar);
        }
        this.f9167r = false;
        Iterator<TooltipDrawable> it = this.f9164o.iterator();
        while (it.hasNext()) {
            p(it.next());
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.U) {
            k0();
            L();
        }
        super.onDraw(canvas);
        int m10 = m();
        u(canvas, this.R, m10);
        if (((Float) Collections.max(getValues())).floatValue() > this.J) {
            t(canvas, this.R, m10);
        }
        N(canvas);
        if ((this.I || isFocused() || b0()) && isEnabled()) {
            M(canvas, this.R, m10);
            if (this.M == -1 && !b0()) {
                y();
            } else {
                x();
            }
        } else {
            y();
        }
        w(canvas, this.R, m10);
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean z10, int i10, Rect rect) {
        super.onFocusChanged(z10, i10, rect);
        if (!z10) {
            this.M = -1;
            this.f9160k.clearKeyboardFocusForVirtualView(this.N);
        } else {
            z(i10);
            this.f9160k.requestKeyboardFocusForVirtualView(this.N);
        }
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i10, KeyEvent keyEvent) {
        if (!isEnabled()) {
            return super.onKeyDown(i10, keyEvent);
        }
        if (this.L.size() == 1) {
            this.M = 0;
        }
        if (this.M == -1) {
            Boolean S = S(i10, keyEvent);
            return S != null ? S.booleanValue() : super.onKeyDown(i10, keyEvent);
        }
        this.T |= keyEvent.isLongPress();
        Float j10 = j(i10);
        if (j10 != null) {
            if (d0(this.L.get(this.M).floatValue() + j10.floatValue())) {
                i0();
                postInvalidate();
            }
            return true;
        }
        if (i10 != 23) {
            if (i10 == 61) {
                if (keyEvent.hasNoModifiers()) {
                    return P(1);
                }
                if (keyEvent.isShiftPressed()) {
                    return P(-1);
                }
                return false;
            }
            if (i10 != 66) {
                return super.onKeyDown(i10, keyEvent);
            }
        }
        this.M = -1;
        postInvalidate();
        return true;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i10, KeyEvent keyEvent) {
        this.T = false;
        return super.onKeyUp(i10, keyEvent);
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, View.MeasureSpec.makeMeasureSpec(this.f9173x + ((this.f9174y == 1 || b0()) ? this.f9164o.get(0).getIntrinsicHeight() : 0), 1073741824));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SliderState sliderState = (SliderState) parcelable;
        super.onRestoreInstanceState(sliderState.getSuperState());
        this.J = sliderState.f9176e;
        this.K = sliderState.f9177f;
        setValuesInternal(sliderState.f9178g);
        this.O = sliderState.f9179h;
        if (sliderState.f9180i) {
            requestFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SliderState sliderState = new SliderState(super.onSaveInstanceState());
        sliderState.f9176e = this.J;
        sliderState.f9177f = this.K;
        sliderState.f9178g = new ArrayList<>(this.L);
        sliderState.f9179h = this.O;
        sliderState.f9180i = hasFocus();
        return sliderState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        j0(i10);
        i0();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        float x10 = motionEvent.getX();
        float f10 = (x10 - this.A) / this.R;
        this.f9155g0 = f10;
        float max = Math.max(0.0f, f10);
        this.f9155g0 = max;
        this.f9155g0 = Math.min(1.0f, max);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.F = x10;
            if (!H()) {
                getParent().requestDisallowInterceptTouchEvent(true);
                if (W()) {
                    requestFocus();
                    this.I = true;
                    g0();
                    i0();
                    invalidate();
                    T();
                }
            }
        } else if (actionMasked == 1) {
            this.I = false;
            MotionEvent motionEvent2 = this.G;
            if (motionEvent2 != null && motionEvent2.getActionMasked() == 0 && Math.abs(this.G.getX() - motionEvent.getX()) <= this.f9170u && Math.abs(this.G.getY() - motionEvent.getY()) <= this.f9170u && W()) {
                T();
            }
            if (this.M != -1) {
                g0();
                this.M = -1;
                U();
            }
            invalidate();
        } else if (actionMasked == 2) {
            if (!this.I) {
                if (H() && Math.abs(x10 - this.F) < this.f9170u) {
                    return false;
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                T();
            }
            if (W()) {
                this.I = true;
                g0();
                i0();
                invalidate();
            }
        }
        setPressed(this.I);
        this.G = MotionEvent.obtain(motionEvent);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setActiveThumbIndex(int i10) {
        this.M = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCustomThumbDrawable(int i10) {
        setCustomThumbDrawable(getResources().getDrawable(i10));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCustomThumbDrawablesForValues(int... iArr) {
        Drawable[] drawableArr = new Drawable[iArr.length];
        for (int i10 = 0; i10 < iArr.length; i10++) {
            drawableArr[i10] = getResources().getDrawable(iArr[i10]);
        }
        setCustomThumbDrawablesForValues(drawableArr);
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        setLayerType(z10 ? 0 : 2, null);
    }

    public void setFocusedThumbIndex(int i10) {
        if (i10 >= 0 && i10 < this.L.size()) {
            this.N = i10;
            this.f9160k.requestKeyboardFocusForVirtualView(i10);
            postInvalidate();
            return;
        }
        throw new IllegalArgumentException("index out of range");
    }

    public void setHaloRadius(int i10) {
        if (i10 == this.D) {
            return;
        }
        this.D = i10;
        Drawable background = getBackground();
        if (!c0() && (background instanceof RippleDrawable)) {
            t3.a.b((RippleDrawable) background, this.D);
        } else {
            postInvalidate();
        }
    }

    public void setHaloRadiusResource(int i10) {
        setHaloRadius(getResources().getDimensionPixelSize(i10));
    }

    public void setHaloTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.V)) {
            return;
        }
        this.V = colorStateList;
        Drawable background = getBackground();
        if (!c0() && (background instanceof RippleDrawable)) {
            ((RippleDrawable) background).setColor(colorStateList);
            return;
        }
        this.f9156h.setColor(D(colorStateList));
        this.f9156h.setAlpha(63);
        invalidate();
    }

    public void setLabelBehavior(int i10) {
        if (this.f9174y != i10) {
            this.f9174y = i10;
            requestLayout();
        }
    }

    public void setLabelFormatter(LabelFormatter labelFormatter) {
        this.H = labelFormatter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSeparationUnit(int i10) {
        this.f9157h0 = i10;
        this.U = true;
        postInvalidate();
    }

    public void setStepSize(float f10) {
        if (f10 < 0.0f) {
            throw new IllegalArgumentException(String.format("The stepSize(%s) must be 0, or a factor of the valueFrom(%s)-valueTo(%s) range", Float.valueOf(f10), Float.valueOf(this.J), Float.valueOf(this.K)));
        }
        if (this.O != f10) {
            this.O = f10;
            this.U = true;
            postInvalidate();
        }
    }

    public void setThumbElevation(float f10) {
        this.f9149d0.Z(f10);
    }

    public void setThumbElevationResource(int i10) {
        setThumbElevation(getResources().getDimension(i10));
    }

    public void setThumbRadius(int i10) {
        if (i10 == this.C) {
            return;
        }
        this.C = i10;
        O();
        this.f9149d0.setShapeAppearanceModel(ShapeAppearanceModel.a().q(0, this.C).m());
        MaterialShapeDrawable materialShapeDrawable = this.f9149d0;
        int i11 = this.C;
        materialShapeDrawable.setBounds(0, 0, i11 * 2, i11 * 2);
        Drawable drawable = this.f9151e0;
        if (drawable != null) {
            h(drawable);
        }
        Iterator<Drawable> it = this.f9153f0.iterator();
        while (it.hasNext()) {
            h(it.next());
        }
        postInvalidate();
    }

    public void setThumbRadiusResource(int i10) {
        setThumbRadius(getResources().getDimensionPixelSize(i10));
    }

    public void setThumbStrokeColor(ColorStateList colorStateList) {
        this.f9149d0.m0(colorStateList);
        postInvalidate();
    }

    public void setThumbStrokeColorResource(int i10) {
        if (i10 != 0) {
            setThumbStrokeColor(AppCompatResources.a(getContext(), i10));
        }
    }

    public void setThumbStrokeWidth(float f10) {
        this.f9149d0.n0(f10);
        postInvalidate();
    }

    public void setThumbStrokeWidthResource(int i10) {
        if (i10 != 0) {
            setThumbStrokeWidth(getResources().getDimension(i10));
        }
    }

    public void setThumbTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f9149d0.w())) {
            return;
        }
        this.f9149d0.a0(colorStateList);
        invalidate();
    }

    public void setTickActiveTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.W)) {
            return;
        }
        this.W = colorStateList;
        this.f9159j.setColor(D(colorStateList));
        invalidate();
    }

    public void setTickInactiveTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f9146a0)) {
            return;
        }
        this.f9146a0 = colorStateList;
        this.f9158i.setColor(D(colorStateList));
        invalidate();
    }

    public void setTickTintList(ColorStateList colorStateList) {
        setTickInactiveTintList(colorStateList);
        setTickActiveTintList(colorStateList);
    }

    public void setTickVisible(boolean z10) {
        if (this.Q != z10) {
            this.Q = z10;
            postInvalidate();
        }
    }

    public void setTrackActiveTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f9147b0)) {
            return;
        }
        this.f9147b0 = colorStateList;
        this.f9152f.setColor(D(colorStateList));
        invalidate();
    }

    public void setTrackHeight(int i10) {
        if (this.f9175z != i10) {
            this.f9175z = i10;
            G();
            postInvalidate();
        }
    }

    public void setTrackInactiveTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f9148c0)) {
            return;
        }
        this.f9148c0 = colorStateList;
        this.f9150e.setColor(D(colorStateList));
        invalidate();
    }

    public void setTrackTintList(ColorStateList colorStateList) {
        setTrackInactiveTintList(colorStateList);
        setTrackActiveTintList(colorStateList);
    }

    public void setValueFrom(float f10) {
        this.J = f10;
        this.U = true;
        postInvalidate();
    }

    public void setValueTo(float f10) {
        this.K = f10;
        this.U = true;
        postInvalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setValues(Float... fArr) {
        ArrayList<Float> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, fArr);
        setValuesInternal(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class d implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        int f9186e;

        private d() {
            this.f9186e = -1;
        }

        void a(int i10) {
            this.f9186e = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            BaseSlider.this.f9160k.sendEventForVirtualView(this.f9186e, 4);
        }

        /* synthetic */ d(BaseSlider baseSlider, a aVar) {
            this();
        }
    }

    public BaseSlider(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.sliderStyle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCustomThumbDrawable(Drawable drawable) {
        this.f9151e0 = F(drawable);
        this.f9153f0.clear();
        postInvalidate();
    }

    public BaseSlider(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, f9145j0), attributeSet, i10);
        this.f9164o = new ArrayList();
        this.f9165p = new ArrayList();
        this.f9166q = new ArrayList();
        this.f9167r = false;
        this.I = false;
        this.L = new ArrayList<>();
        this.M = -1;
        this.N = -1;
        this.O = 0.0f;
        this.Q = true;
        this.T = false;
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        this.f9149d0 = materialShapeDrawable;
        this.f9153f0 = Collections.emptyList();
        this.f9157h0 = 0;
        Context context2 = getContext();
        Paint paint = new Paint();
        this.f9150e = paint;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        Paint paint2 = new Paint();
        this.f9152f = paint2;
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        Paint paint3 = new Paint(1);
        this.f9154g = paint3;
        paint3.setStyle(Paint.Style.FILL);
        paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Paint paint4 = new Paint(1);
        this.f9156h = paint4;
        paint4.setStyle(Paint.Style.FILL);
        Paint paint5 = new Paint();
        this.f9158i = paint5;
        paint5.setStyle(Paint.Style.STROKE);
        paint5.setStrokeCap(Paint.Cap.ROUND);
        Paint paint6 = new Paint();
        this.f9159j = paint6;
        paint6.setStyle(Paint.Style.STROKE);
        paint6.setStrokeCap(Paint.Cap.ROUND);
        K(context2.getResources());
        this.f9163n = new a(attributeSet, i10);
        Y(context2, attributeSet, i10);
        setFocusable(true);
        setClickable(true);
        materialShapeDrawable.i0(2);
        this.f9170u = ViewConfiguration.get(context2).getScaledTouchSlop();
        e eVar = new e(this);
        this.f9160k = eVar;
        ViewCompat.l0(this, eVar);
        this.f9161l = (AccessibilityManager) getContext().getSystemService("accessibility");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setValues(List<Float> list) {
        setValuesInternal(new ArrayList<>(list));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCustomThumbDrawablesForValues(Drawable... drawableArr) {
        this.f9151e0 = null;
        this.f9153f0 = new ArrayList();
        for (Drawable drawable : drawableArr) {
            this.f9153f0.add(F(drawable));
        }
        postInvalidate();
    }
}
