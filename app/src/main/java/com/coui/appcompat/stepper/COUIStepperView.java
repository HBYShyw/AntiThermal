package com.coui.appcompat.stepper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.coui.appcompat.stepper.COUIStepperView;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$style;
import com.support.appcompat.R$styleable;
import java.util.Observable;
import java.util.Observer;
import x2.ObservableStep;
import x2.OnStepChangeListener;

/* loaded from: classes.dex */
public class COUIStepperView extends ConstraintLayout implements Observer {
    private Context B;
    private ObservableStep C;
    private ImageView D;
    private ImageView E;
    private TextView F;
    private OnStepChangeListener G;
    private int H;
    private final Runnable I;
    private final Runnable J;
    private LongPressProxy K;
    private LongPressProxy L;

    public COUIStepperView(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void G() {
        performHapticFeedback(308, 0);
        J();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void H() {
        performHapticFeedback(308, 0);
        I();
    }

    private int getNumForMaxWidth() {
        int i10 = 1;
        float f10 = 0.0f;
        for (int i11 = 0; i11 < 10; i11++) {
            float measureText = this.F.getPaint().measureText(String.valueOf(i11));
            if (measureText > f10) {
                i10 = i11;
                f10 = measureText;
            }
        }
        return i10;
    }

    protected void F(AttributeSet attributeSet, int i10) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIStepperView, i10, R$style.COUIStepperViewDefStyle);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.COUIStepperView_couiStepperTextStyle, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(R$styleable.COUIStepperView_couiStepperPlusImage, 0);
        int resourceId3 = obtainStyledAttributes.getResourceId(R$styleable.COUIStepperView_couiStepperMinusImage, 0);
        int i11 = obtainStyledAttributes.getInt(R$styleable.COUIStepperView_couiMaximum, CpuLevelConfig.ThermalCpuLevelPolicy.CPU_POWER_DEFAULT);
        int i12 = obtainStyledAttributes.getInt(R$styleable.COUIStepperView_couiMinimum, -999);
        int i13 = obtainStyledAttributes.getInt(R$styleable.COUIStepperView_couiDefStep, 0);
        this.H = obtainStyledAttributes.getInt(R$styleable.COUIStepperView_couiUnit, 1);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(getContext()).inflate(R$layout.coui_stepper_view, this);
        ImageView imageView = (ImageView) findViewById(R$id.plus);
        this.D = imageView;
        imageView.setImageDrawable(ContextCompat.e(getContext(), resourceId2));
        this.K = new LongPressProxy(this.D, this.I);
        ImageView imageView2 = (ImageView) findViewById(R$id.minus);
        this.E = imageView2;
        imageView2.setImageDrawable(ContextCompat.e(getContext(), resourceId3));
        this.L = new LongPressProxy(this.E, this.J);
        TextView textView = (TextView) findViewById(R$id.indicator);
        this.F = textView;
        textView.setTextAppearance(resourceId);
        ObservableStep observableStep = new ObservableStep();
        this.C = observableStep;
        observableStep.addObserver(this);
        setMaximum(i11);
        setMinimum(i12);
        setCurStep(i13);
    }

    public void I() {
        ObservableStep observableStep = this.C;
        observableStep.f(observableStep.c() - getUnit());
    }

    public void J() {
        ObservableStep observableStep = this.C;
        observableStep.f(observableStep.c() + getUnit());
    }

    public void K() {
        this.K.g();
        this.L.g();
        this.C.deleteObservers();
        this.G = null;
    }

    public int getCurStep() {
        return this.C.c();
    }

    public int getMaximum() {
        return this.C.a();
    }

    public int getMinimum() {
        return this.C.b();
    }

    public int getUnit() {
        return this.H;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int numForMaxWidth = getNumForMaxWidth();
        String[] split = String.valueOf(getMaximum()).split("");
        StringBuilder sb2 = new StringBuilder();
        for (int i12 = 0; i12 < split.length; i12++) {
            sb2.append(numForMaxWidth);
        }
        this.F.setWidth(Math.round(this.F.getPaint().measureText(sb2.toString())));
        super.onMeasure(i10, i11);
    }

    public void setCurStep(int i10) {
        this.C.f(i10);
    }

    public void setMaximum(int i10) {
        this.C.d(i10);
    }

    public void setMinimum(int i10) {
        this.C.e(i10);
    }

    public void setOnStepChangeListener(OnStepChangeListener onStepChangeListener) {
        this.G = onStepChangeListener;
    }

    public void setUnit(int i10) {
        this.H = i10;
    }

    @Override // java.util.Observer
    public void update(Observable observable, Object obj) {
        int c10 = ((ObservableStep) observable).c();
        int intValue = ((Integer) obj).intValue();
        this.D.setEnabled(c10 < getMaximum() && isEnabled());
        this.E.setEnabled(c10 > getMinimum() && isEnabled());
        this.F.setText(String.valueOf(c10));
        OnStepChangeListener onStepChangeListener = this.G;
        if (onStepChangeListener != null) {
            onStepChangeListener.a(c10, intValue);
        }
    }

    public COUIStepperView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiStepperViewStyle);
    }

    public COUIStepperView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.I = new Runnable() { // from class: x2.b
            @Override // java.lang.Runnable
            public final void run() {
                COUIStepperView.this.G();
            }
        };
        this.J = new Runnable() { // from class: x2.a
            @Override // java.lang.Runnable
            public final void run() {
                COUIStepperView.this.H();
            }
        };
        this.B = context;
        F(attributeSet, i10);
    }
}
