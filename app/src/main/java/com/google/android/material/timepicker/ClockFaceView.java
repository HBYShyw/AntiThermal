package com.google.android.material.timepicker;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import c.AppCompatResources;
import com.google.android.material.R$attr;
import com.google.android.material.R$color;
import com.google.android.material.R$dimen;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.timepicker.ClockHandView;
import java.util.Arrays;
import z3.MaterialResources;

/* loaded from: classes.dex */
class ClockFaceView extends RadialViewGroup implements ClockHandView.d {
    private final ClockHandView E;
    private final Rect F;
    private final RectF G;
    private final SparseArray<TextView> H;
    private final AccessibilityDelegateCompat I;
    private final int[] J;
    private final float[] K;
    private final int L;
    private final int M;
    private final int N;
    private final int O;
    private String[] P;
    private float Q;
    private final ColorStateList R;

    /* loaded from: classes.dex */
    class a implements ViewTreeObserver.OnPreDrawListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            if (!ClockFaceView.this.isShown()) {
                return true;
            }
            ClockFaceView.this.getViewTreeObserver().removeOnPreDrawListener(this);
            ClockFaceView.this.F(((ClockFaceView.this.getHeight() / 2) - ClockFaceView.this.E.g()) - ClockFaceView.this.L);
            return true;
        }
    }

    /* loaded from: classes.dex */
    class b extends AccessibilityDelegateCompat {
        b() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            int intValue = ((Integer) view.getTag(R$id.material_value_index)).intValue();
            if (intValue > 0) {
                accessibilityNodeInfoCompat.z0((View) ClockFaceView.this.H.get(intValue - 1));
            }
            accessibilityNodeInfoCompat.Y(AccessibilityNodeInfoCompat.c.a(0, 1, intValue, 1, false, view.isSelected()));
            accessibilityNodeInfoCompat.W(true);
            accessibilityNodeInfoCompat.b(AccessibilityNodeInfoCompat.a.f2322i);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View view, int i10, Bundle bundle) {
            if (i10 == 16) {
                long uptimeMillis = SystemClock.uptimeMillis();
                float x10 = view.getX() + (view.getWidth() / 2.0f);
                float height = (view.getHeight() / 2.0f) + view.getY();
                ClockFaceView.this.E.onTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, x10, height, 0));
                ClockFaceView.this.E.onTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 1, x10, height, 0));
                return true;
            }
            return super.performAccessibilityAction(view, i10, bundle);
        }
    }

    public ClockFaceView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.materialClockStyle);
    }

    private void M() {
        RectF d10 = this.E.d();
        for (int i10 = 0; i10 < this.H.size(); i10++) {
            TextView textView = this.H.get(i10);
            if (textView != null) {
                textView.getDrawingRect(this.F);
                offsetDescendantRectToMyCoords(textView, this.F);
                textView.setSelected(d10.contains(this.F.centerX(), this.F.centerY()));
                textView.getPaint().setShader(N(d10, this.F, textView));
                textView.invalidate();
            }
        }
    }

    private RadialGradient N(RectF rectF, Rect rect, TextView textView) {
        this.G.set(rect);
        this.G.offset(textView.getPaddingLeft(), textView.getPaddingTop());
        if (RectF.intersects(rectF, this.G)) {
            return new RadialGradient(rectF.centerX() - this.G.left, rectF.centerY() - this.G.top, rectF.width() * 0.5f, this.J, this.K, Shader.TileMode.CLAMP);
        }
        return null;
    }

    private static float O(float f10, float f11, float f12) {
        return Math.max(Math.max(f10, f11), f12);
    }

    private void Q(int i10) {
        LayoutInflater from = LayoutInflater.from(getContext());
        int size = this.H.size();
        for (int i11 = 0; i11 < Math.max(this.P.length, size); i11++) {
            TextView textView = this.H.get(i11);
            if (i11 >= this.P.length) {
                removeView(textView);
                this.H.remove(i11);
            } else {
                if (textView == null) {
                    textView = (TextView) from.inflate(R$layout.material_clockface_textview, (ViewGroup) this, false);
                    this.H.put(i11, textView);
                    addView(textView);
                }
                textView.setVisibility(0);
                textView.setText(this.P[i11]);
                textView.setTag(R$id.material_value_index, Integer.valueOf(i11));
                ViewCompat.l0(textView, this.I);
                textView.setTextColor(this.R);
                if (i10 != 0) {
                    textView.setContentDescription(getResources().getString(i10, this.P[i11]));
                }
            }
        }
    }

    @Override // com.google.android.material.timepicker.RadialViewGroup
    public void F(int i10) {
        if (i10 != E()) {
            super.F(i10);
            this.E.j(E());
        }
    }

    public void P(String[] strArr, int i10) {
        this.P = strArr;
        Q(i10);
    }

    @Override // com.google.android.material.timepicker.ClockHandView.d
    public void c(float f10, boolean z10) {
        if (Math.abs(this.Q - f10) > 0.001f) {
            this.Q = f10;
            M();
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo).X(AccessibilityNodeInfoCompat.b.b(1, this.P.length, false, 1));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        M();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int O = (int) (this.O / O(this.M / displayMetrics.heightPixels, this.N / displayMetrics.widthPixels, 1.0f));
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(O, 1073741824);
        setMeasuredDimension(O, O);
        super.onMeasure(makeMeasureSpec, makeMeasureSpec);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public ClockFaceView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.F = new Rect();
        this.G = new RectF();
        this.H = new SparseArray<>();
        this.K = new float[]{0.0f, 0.9f, 1.0f};
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ClockFaceView, i10, R$style.Widget_MaterialComponents_TimePicker_Clock);
        Resources resources = getResources();
        ColorStateList a10 = MaterialResources.a(context, obtainStyledAttributes, R$styleable.ClockFaceView_clockNumberTextColor);
        this.R = a10;
        LayoutInflater.from(context).inflate(R$layout.material_clockface_view, (ViewGroup) this, true);
        ClockHandView clockHandView = (ClockHandView) findViewById(R$id.material_clock_hand);
        this.E = clockHandView;
        this.L = resources.getDimensionPixelSize(R$dimen.material_clock_hand_padding);
        int colorForState = a10.getColorForState(new int[]{R.attr.state_selected}, a10.getDefaultColor());
        this.J = new int[]{colorForState, colorForState, a10.getDefaultColor()};
        clockHandView.b(this);
        int defaultColor = AppCompatResources.a(context, R$color.material_timepicker_clockface).getDefaultColor();
        ColorStateList a11 = MaterialResources.a(context, obtainStyledAttributes, R$styleable.ClockFaceView_clockFaceBackgroundColor);
        setBackgroundColor(a11 != null ? a11.getDefaultColor() : defaultColor);
        getViewTreeObserver().addOnPreDrawListener(new a());
        setFocusable(true);
        obtainStyledAttributes.recycle();
        this.I = new b();
        String[] strArr = new String[12];
        Arrays.fill(strArr, "");
        P(strArr, 0);
        this.M = resources.getDimensionPixelSize(R$dimen.material_time_picker_minimum_screen_height);
        this.N = resources.getDimensionPixelSize(R$dimen.material_time_picker_minimum_screen_width);
        this.O = resources.getDimensionPixelSize(R$dimen.material_clock_size);
    }
}
