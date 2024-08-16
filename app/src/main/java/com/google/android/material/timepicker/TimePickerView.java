package com.google.android.material.timepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;

/* loaded from: classes.dex */
class TimePickerView extends ConstraintLayout {
    private final Chip B;
    private final Chip C;
    private final ClockHandView D;
    private final ClockFaceView E;
    private final MaterialButtonToggleGroup F;
    private final View.OnClickListener G;
    private f H;
    private g I;
    private e J;

    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TimePickerView.this.I != null) {
                TimePickerView.this.I.a(((Integer) view.getTag(R$id.selection_type)).intValue());
            }
        }
    }

    /* loaded from: classes.dex */
    class b implements MaterialButtonToggleGroup.d {
        b() {
        }

        @Override // com.google.android.material.button.MaterialButtonToggleGroup.d
        public void a(MaterialButtonToggleGroup materialButtonToggleGroup, int i10, boolean z10) {
            int i11 = i10 == R$id.material_clock_period_pm_button ? 1 : 0;
            if (TimePickerView.this.H == null || !z10) {
                return;
            }
            TimePickerView.this.H.a(i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends GestureDetector.SimpleOnGestureListener {
        c() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            e eVar = TimePickerView.this.J;
            if (eVar == null) {
                return false;
            }
            eVar.a();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements View.OnTouchListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ GestureDetector f9528e;

        d(GestureDetector gestureDetector) {
            this.f9528e = gestureDetector;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (((Checkable) view).isChecked()) {
                return this.f9528e.onTouchEvent(motionEvent);
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    interface e {
        void a();
    }

    /* loaded from: classes.dex */
    interface f {
        void a(int i10);
    }

    /* loaded from: classes.dex */
    interface g {
        void a(int i10);
    }

    public TimePickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    private void G() {
        Chip chip = this.B;
        int i10 = R$id.selection_type;
        chip.setTag(i10, 12);
        this.C.setTag(i10, 10);
        this.B.setOnClickListener(this.G);
        this.C.setOnClickListener(this.G);
        this.B.setAccessibilityClassName("android.view.View");
        this.C.setAccessibilityClassName("android.view.View");
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void H() {
        d dVar = new d(new GestureDetector(getContext(), new c()));
        this.B.setOnTouchListener(dVar);
        this.C.setOnTouchListener(dVar);
    }

    private void I() {
        if (this.F.getVisibility() == 0) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.j(this);
            constraintSet.h(R$id.material_clock_display, ViewCompat.x(this) == 0 ? 2 : 1);
            constraintSet.d(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        I();
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        super.onVisibilityChanged(view, i10);
        if (view == this && i10 == 0) {
            I();
        }
    }

    public TimePickerView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.G = new a();
        LayoutInflater.from(context).inflate(R$layout.material_timepicker, this);
        this.E = (ClockFaceView) findViewById(R$id.material_clock_face);
        MaterialButtonToggleGroup materialButtonToggleGroup = (MaterialButtonToggleGroup) findViewById(R$id.material_clock_period_toggle);
        this.F = materialButtonToggleGroup;
        materialButtonToggleGroup.b(new b());
        this.B = (Chip) findViewById(R$id.material_minute_tv);
        this.C = (Chip) findViewById(R$id.material_hour_tv);
        this.D = (ClockHandView) findViewById(R$id.material_clock_hand);
        H();
        G();
    }
}
