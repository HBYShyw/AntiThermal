package com.coui.appcompat.searchhistory;

import a4.RippleUtils;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import com.coui.appcompat.searchhistory.COUIPressFeedbackImageView;
import com.google.android.material.chip.ChipDrawable;
import com.support.control.R$style;
import fb._Ranges;
import java.util.Objects;
import kotlin.Metadata;
import m1.COUIMoveEaseInterpolator;
import za.k;

/* compiled from: COUIPressFeedbackImageView.kt */
@Metadata(bv = {}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u0000 $2\u00020\u0001:\u0001%B\u000f\u0012\u0006\u0010!\u001a\u00020 ¢\u0006\u0004\b\"\u0010#J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0004H\u0002J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0002J\u0010\u0010\f\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0004H\u0002R\u0014\u0010\u0010\u001a\u00020\r8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0012\u001a\u00020\u00048\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0005\u0010\u0011R\u0016\u0010\u0015\u001a\u00020\t8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0013\u0010\u0014R\u0018\u0010\u0019\u001a\u0004\u0018\u00010\u00168\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0017\u0010\u0018R\u0014\u0010\u001d\u001a\u00020\u001a8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001b\u0010\u001cR\u0016\u0010\u001f\u001a\u00020\u00048\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001e\u0010\u0011¨\u0006&"}, d2 = {"Lcom/coui/appcompat/searchhistory/COUIPressFeedbackImageView;", "Landroidx/appcompat/widget/AppCompatImageView;", "Landroid/view/MotionEvent;", "event", "", "i", "animatorPressed", "Lma/f0;", "g", "", "scaleValue", "setScale", "f", "Lcom/google/android/material/chip/a;", "h", "Lcom/google/android/material/chip/a;", "chipDrawable", "Z", "isNeedToDelayCancelScaleAnim", "j", "F", "currentScale", "Landroid/animation/ValueAnimator;", "k", "Landroid/animation/ValueAnimator;", "scaleAnimator", "", "m", "[I", "location", "n", "mAnimatorPressed", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "o", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
@SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
/* loaded from: classes.dex */
public final class COUIPressFeedbackImageView extends AppCompatImageView {

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private final ChipDrawable chipDrawable;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private boolean isNeedToDelayCancelScaleAnim;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    private float currentScale;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    private ValueAnimator scaleAnimator;

    /* renamed from: l, reason: collision with root package name */
    private final COUIMoveEaseInterpolator f7434l;

    /* renamed from: m, reason: collision with root package name and from kotlin metadata */
    private final int[] location;

    /* renamed from: n, reason: collision with root package name and from kotlin metadata */
    private boolean mAnimatorPressed;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIPressFeedbackImageView(Context context) {
        super(context);
        k.e(context, "context");
        ChipDrawable C0 = ChipDrawable.C0(context, null, 0, R$style.Widget_COUI_Chip_Record);
        k.d(C0, "createFromAttributes(context, null, 0, com.support.control.R.style.Widget_COUI_Chip_Record)");
        this.chipDrawable = C0;
        this.currentScale = 1.0f;
        this.f7434l = new COUIMoveEaseInterpolator();
        this.location = new int[2];
        RippleDrawable rippleDrawable = new RippleDrawable(RippleUtils.d(C0.m1()), C0, null);
        C0.R2(false);
        setBackground(rippleDrawable);
        setOnTouchListener(new View.OnTouchListener() { // from class: r2.i
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean e10;
                e10 = COUIPressFeedbackImageView.e(COUIPressFeedbackImageView.this, view, motionEvent);
                return e10;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean e(COUIPressFeedbackImageView cOUIPressFeedbackImageView, View view, MotionEvent motionEvent) {
        k.e(cOUIPressFeedbackImageView, "this$0");
        k.d(motionEvent, "motionEvent");
        if (!cOUIPressFeedbackImageView.i(motionEvent)) {
            cOUIPressFeedbackImageView.mAnimatorPressed = false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            cOUIPressFeedbackImageView.mAnimatorPressed = true;
            cOUIPressFeedbackImageView.g(true);
        } else if (action == 1 || action == 3) {
            cOUIPressFeedbackImageView.mAnimatorPressed = false;
            cOUIPressFeedbackImageView.g(false);
        }
        return false;
    }

    private final void f(boolean z10) {
        boolean z11;
        ValueAnimator valueAnimator;
        ValueAnimator valueAnimator2 = this.scaleAnimator;
        if (valueAnimator2 != null) {
            if (k.a(valueAnimator2 == null ? null : Boolean.valueOf(valueAnimator2.isRunning()), Boolean.TRUE)) {
                if (!z10) {
                    ValueAnimator valueAnimator3 = this.scaleAnimator;
                    float currentPlayTime = (float) (valueAnimator3 == null ? 0L : valueAnimator3.getCurrentPlayTime());
                    ValueAnimator valueAnimator4 = this.scaleAnimator;
                    if (currentPlayTime < ((float) (valueAnimator4 != null ? valueAnimator4.getDuration() : 0L)) * 0.8f) {
                        z11 = true;
                        this.isNeedToDelayCancelScaleAnim = z11;
                        if (!z11 || (valueAnimator = this.scaleAnimator) == null) {
                        }
                        valueAnimator.cancel();
                        return;
                    }
                }
                z11 = false;
                this.isNeedToDelayCancelScaleAnim = z11;
                if (z11) {
                }
            }
        }
    }

    private final void g(final boolean z10) {
        this.isNeedToDelayCancelScaleAnim = false;
        f(z10);
        if (this.isNeedToDelayCancelScaleAnim) {
            return;
        }
        float[] fArr = new float[2];
        fArr[0] = z10 ? 1.0f : this.currentScale;
        fArr[1] = z10 ? 0.9f : 1.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        this.scaleAnimator = ofFloat;
        if (ofFloat != null) {
            ofFloat.setInterpolator(this.f7434l);
        }
        ValueAnimator valueAnimator = this.scaleAnimator;
        if (valueAnimator != null) {
            valueAnimator.setDuration(z10 ? 200L : 340L);
        }
        ValueAnimator valueAnimator2 = this.scaleAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: r2.h
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                    COUIPressFeedbackImageView.h(COUIPressFeedbackImageView.this, z10, valueAnimator3);
                }
            });
        }
        ValueAnimator valueAnimator3 = this.scaleAnimator;
        if (valueAnimator3 == null) {
            return;
        }
        valueAnimator3.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void h(COUIPressFeedbackImageView cOUIPressFeedbackImageView, boolean z10, ValueAnimator valueAnimator) {
        k.e(cOUIPressFeedbackImageView, "this$0");
        long currentPlayTime = valueAnimator.getCurrentPlayTime();
        Object animatedValue = valueAnimator.getAnimatedValue();
        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        cOUIPressFeedbackImageView.currentScale = ((Float) animatedValue).floatValue();
        if (cOUIPressFeedbackImageView.isNeedToDelayCancelScaleAnim && z10 && ((float) currentPlayTime) > ((float) valueAnimator.getDuration()) * 0.8f) {
            valueAnimator.cancel();
            cOUIPressFeedbackImageView.g(false);
        } else {
            cOUIPressFeedbackImageView.setScale(cOUIPressFeedbackImageView.currentScale);
        }
    }

    private final boolean i(MotionEvent event) {
        getLocationOnScreen(this.location);
        return event.getRawX() > ((float) this.location[0]) && event.getRawX() < ((float) (this.location[0] + getWidth())) && event.getRawY() > ((float) this.location[1]) && event.getRawY() < ((float) (this.location[1] + getHeight()));
    }

    private final void setScale(float f10) {
        float e10;
        float b10;
        e10 = _Ranges.e(1.0f, f10);
        b10 = _Ranges.b(0.9f, e10);
        setScaleX(b10);
        setScaleY(b10);
    }
}
