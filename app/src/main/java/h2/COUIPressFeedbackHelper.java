package h2;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.support.appcompat.R$dimen;
import m1.COUIInEaseInterpolator;
import m1.COUIMoveEaseInterpolator;

/* compiled from: COUIPressFeedbackHelper.java */
/* renamed from: h2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPressFeedbackHelper {

    /* renamed from: k, reason: collision with root package name */
    private ValueAnimator f11972k;

    /* renamed from: p, reason: collision with root package name */
    private int f11977p;

    /* renamed from: q, reason: collision with root package name */
    private View f11978q;

    /* renamed from: t, reason: collision with root package name */
    private float f11981t;

    /* renamed from: w, reason: collision with root package name */
    private float f11984w;

    /* renamed from: x, reason: collision with root package name */
    private float f11985x;

    /* renamed from: a, reason: collision with root package name */
    private final long f11962a = 200;

    /* renamed from: b, reason: collision with root package name */
    private final long f11963b = 340;

    /* renamed from: c, reason: collision with root package name */
    private final float f11964c = 0.92f;

    /* renamed from: d, reason: collision with root package name */
    private final float f11965d = 1.0f;

    /* renamed from: e, reason: collision with root package name */
    private final float f11966e = 0.8f;

    /* renamed from: f, reason: collision with root package name */
    private final float f11967f = 1.0f;

    /* renamed from: g, reason: collision with root package name */
    private final float f11968g = 0.5f;

    /* renamed from: h, reason: collision with root package name */
    private final float f11969h = 0.0f;

    /* renamed from: i, reason: collision with root package name */
    private final PathInterpolator f11970i = new COUIMoveEaseInterpolator();

    /* renamed from: j, reason: collision with root package name */
    private final PathInterpolator f11971j = new COUIInEaseInterpolator();

    /* renamed from: l, reason: collision with root package name */
    private float f11973l = 1.0f;

    /* renamed from: m, reason: collision with root package name */
    private float f11974m = 1.0f;

    /* renamed from: n, reason: collision with root package name */
    private float f11975n = 0.0f;

    /* renamed from: o, reason: collision with root package name */
    private boolean f11976o = false;

    /* renamed from: r, reason: collision with root package name */
    private float f11979r = 0.92f;

    /* renamed from: s, reason: collision with root package name */
    private float f11980s = 0.0f;

    /* renamed from: u, reason: collision with root package name */
    private float f11982u = 0.98f;

    /* renamed from: v, reason: collision with root package name */
    private float f11983v = 0.94f;

    /* compiled from: COUIPressFeedbackHelper.java */
    /* renamed from: h2.a$a */
    /* loaded from: classes.dex */
    class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIPressFeedbackHelper.this.f11973l = ((Float) valueAnimator.getAnimatedValue("scaleHolder")).floatValue();
            COUIPressFeedbackHelper.this.f11974m = ((Float) valueAnimator.getAnimatedValue("brightnessHolder")).floatValue();
            COUIPressFeedbackHelper.this.f11980s = ((Float) valueAnimator.getAnimatedValue("alphaHolder")).floatValue();
            COUIPressFeedbackHelper.this.f11975n = ((Float) valueAnimator.getAnimatedValue("blackAlphaHolder")).floatValue();
            COUIPressFeedbackHelper cOUIPressFeedbackHelper = COUIPressFeedbackHelper.this;
            cOUIPressFeedbackHelper.q(cOUIPressFeedbackHelper.f11973l, COUIPressFeedbackHelper.this.f11978q, COUIPressFeedbackHelper.this.f11979r);
            COUIPressFeedbackHelper cOUIPressFeedbackHelper2 = COUIPressFeedbackHelper.this;
            cOUIPressFeedbackHelper2.p(cOUIPressFeedbackHelper2.f11980s, COUIPressFeedbackHelper.this.f11978q);
        }
    }

    public COUIPressFeedbackHelper(View view, int i10) {
        this.f11977p = i10;
        this.f11978q = view;
        TypedValue typedValue = new TypedValue();
        this.f11978q.getContext().getResources().getValue(R$dimen.button_fill_alpha, typedValue, true);
        this.f11981t = typedValue.getFloat();
        int dimensionPixelOffset = this.f11978q.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_max_end_value_width);
        int dimensionPixelOffset2 = this.f11978q.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_max_end_value_height);
        int dimensionPixelOffset3 = this.f11978q.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_min_end_value_size);
        this.f11984w = dimensionPixelOffset * dimensionPixelOffset2;
        this.f11985x = dimensionPixelOffset3 * dimensionPixelOffset3;
    }

    private float k(int i10, int i11) {
        float f10 = this.f11982u;
        float f11 = i10 * i11;
        float f12 = this.f11984w;
        float f13 = (f11 - f12) * (f10 - this.f11983v);
        float f14 = this.f11985x;
        float f15 = (f13 / (f12 - f14)) + f10;
        if (f11 < f14) {
            return 1.0f;
        }
        return f11 > f12 ? f10 : f15;
    }

    private void l() {
        ValueAnimator valueAnimator = this.f11972k;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.f11972k.cancel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p(float f10, View view) {
        if (f10 == view.getAlpha() || this.f11977p == 1) {
            return;
        }
        view.setAlpha(f10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q(float f10, View view, float f11) {
        float max = Math.max(f11, Math.min(1.0f, f10));
        view.setScaleX(max);
        view.setScaleY(max);
        view.invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0091 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void m(boolean z10) {
        long j10;
        float f10;
        long j11;
        float f11;
        float f12;
        this.f11976o = false;
        TypedValue typedValue = new TypedValue();
        this.f11978q.getContext().getResources().getValue(R$dimen.coui_button_press_black_alpha, typedValue, true);
        float f13 = typedValue.getFloat();
        int i10 = this.f11977p;
        if (i10 == 0) {
            j10 = z10 ? 200L : 340L;
            this.f11979r = k(this.f11978q.getWidth(), this.f11978q.getHeight());
        } else {
            if (i10 == 1) {
                j10 = z10 ? 200L : 340L;
                float f14 = this.f11981t;
                if (z10) {
                    this.f11980s = 0.0f;
                }
                f10 = 0.0f;
                j11 = j10;
                f11 = f14;
                f12 = 1.0f;
            } else if (i10 == 2) {
                f12 = 0.8f;
                j11 = z10 ? 200L : 340L;
                f11 = 1.0f;
                f10 = f11;
            } else if (i10 != 3) {
                j10 = 0;
            } else {
                j10 = z10 ? 200L : 340L;
                if (z10) {
                    this.f11980s = 1.0f;
                }
                this.f11979r = k(this.f11978q.getWidth(), this.f11978q.getHeight());
                j11 = j10;
                f10 = 1.0f;
                f11 = 0.5f;
                f12 = 1.0f;
            }
            l();
            if (this.f11976o) {
                float[] fArr = new float[2];
                fArr[0] = z10 ? 1.0f : this.f11973l;
                fArr[1] = z10 ? this.f11979r : 1.0f;
                PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("scaleHolder", fArr);
                float[] fArr2 = new float[2];
                fArr2[0] = z10 ? 1.0f : this.f11974m;
                fArr2[1] = z10 ? f12 : 1.0f;
                PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat("brightnessHolder", fArr2);
                float[] fArr3 = new float[2];
                fArr3[0] = z10 ? f10 : this.f11980s;
                if (!z10) {
                    f11 = f10;
                }
                fArr3[1] = f11;
                PropertyValuesHolder ofFloat3 = PropertyValuesHolder.ofFloat("alphaHolder", fArr3);
                float[] fArr4 = new float[2];
                fArr4[0] = z10 ? 0.0f : this.f11975n;
                if (!z10) {
                    f13 = 0.0f;
                }
                fArr4[1] = f13;
                ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(ofFloat, ofFloat2, ofFloat3, PropertyValuesHolder.ofFloat("blackAlphaHolder", fArr4));
                this.f11972k = ofPropertyValuesHolder;
                ofPropertyValuesHolder.setInterpolator(z10 ? this.f11970i : this.f11971j);
                this.f11972k.setDuration(j11);
                this.f11972k.addUpdateListener(new a());
                this.f11972k.start();
                return;
            }
            return;
        }
        j11 = j10;
        f12 = 1.0f;
        f11 = 1.0f;
        f10 = f11;
        l();
        if (this.f11976o) {
        }
    }

    public float n() {
        return this.f11980s;
    }

    public float o() {
        return this.f11975n;
    }
}
