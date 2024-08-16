package com.coui.appcompat.edittext;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.animation.Interpolator;
import android.widget.EditText;
import com.coui.appcompat.edittext.COUICutoutDrawable;
import com.coui.appcompat.edittext.COUIEditText;
import com.support.appcompat.R$dimen;
import java.util.ArrayList;
import m1.COUIEaseInterpolator;
import m1.COUILinearInterpolator;

/* compiled from: COUIErrorEditTextHelper.java */
/* renamed from: com.coui.appcompat.edittext.b, reason: use source file name */
/* loaded from: classes.dex */
class COUIErrorEditTextHelper {

    /* renamed from: v, reason: collision with root package name */
    private static final Rect f5808v = new Rect();

    /* renamed from: a, reason: collision with root package name */
    private final EditText f5809a;

    /* renamed from: b, reason: collision with root package name */
    private final COUICutoutDrawable.a f5810b;

    /* renamed from: c, reason: collision with root package name */
    private ColorStateList f5811c;

    /* renamed from: d, reason: collision with root package name */
    private int f5812d;

    /* renamed from: e, reason: collision with root package name */
    private int f5813e;

    /* renamed from: f, reason: collision with root package name */
    private int f5814f;

    /* renamed from: g, reason: collision with root package name */
    private COUICutoutDrawable f5815g;

    /* renamed from: h, reason: collision with root package name */
    private ColorStateList f5816h;

    /* renamed from: i, reason: collision with root package name */
    private ColorStateList f5817i;

    /* renamed from: j, reason: collision with root package name */
    private Paint f5818j;

    /* renamed from: k, reason: collision with root package name */
    private Paint f5819k;

    /* renamed from: l, reason: collision with root package name */
    private AnimatorSet f5820l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f5821m;

    /* renamed from: n, reason: collision with root package name */
    private ArrayList<COUIEditText.i> f5822n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f5823o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f5824p;

    /* renamed from: q, reason: collision with root package name */
    private float f5825q;

    /* renamed from: r, reason: collision with root package name */
    private float f5826r;

    /* renamed from: s, reason: collision with root package name */
    private float f5827s;

    /* renamed from: t, reason: collision with root package name */
    private float f5828t;

    /* renamed from: u, reason: collision with root package name */
    private float f5829u;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIErrorEditTextHelper.java */
    /* renamed from: com.coui.appcompat.edittext.b$a */
    /* loaded from: classes.dex */
    public class a implements TextWatcher {
        a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            COUIErrorEditTextHelper.this.F(false, false, false);
            Editable text = COUIErrorEditTextHelper.this.f5809a.getText();
            int length = text.length();
            COUIErrorEditTextHelper cOUIErrorEditTextHelper = COUIErrorEditTextHelper.this;
            cOUIErrorEditTextHelper.f5828t = cOUIErrorEditTextHelper.f5809a.getPaint().measureText(text, 0, length);
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
            if (COUIErrorEditTextHelper.this.f5829u <= 0.0f) {
                COUIErrorEditTextHelper.this.f5829u = r0.f5809a.getHeight();
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIErrorEditTextHelper.java */
    /* renamed from: com.coui.appcompat.edittext.b$b */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIErrorEditTextHelper.this.f5825q = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIErrorEditTextHelper.java */
    /* renamed from: com.coui.appcompat.edittext.b$c */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (COUIErrorEditTextHelper.this.f5824p) {
                COUIErrorEditTextHelper.this.f5826r = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            }
            COUIErrorEditTextHelper.this.f5809a.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIErrorEditTextHelper.java */
    /* renamed from: com.coui.appcompat.edittext.b$d */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (COUIErrorEditTextHelper.this.f5824p) {
                COUIErrorEditTextHelper.this.f5827s = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIErrorEditTextHelper.java */
    /* renamed from: com.coui.appcompat.edittext.b$e */
    /* loaded from: classes.dex */
    public class e implements Animator.AnimatorListener {

        /* compiled from: COUIErrorEditTextHelper.java */
        /* renamed from: com.coui.appcompat.edittext.b$e$a */
        /* loaded from: classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUIErrorEditTextHelper.this.f5829u = r1.f5809a.getHeight();
            }
        }

        e() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIErrorEditTextHelper.this.G(true, true, true);
            COUIErrorEditTextHelper.this.z(true);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIErrorEditTextHelper.this.f5809a.setSelection(COUIErrorEditTextHelper.this.f5809a.length());
            if (COUIErrorEditTextHelper.this.f5829u <= 0.0f) {
                COUIErrorEditTextHelper.this.f5809a.post(new a());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIErrorEditTextHelper.java */
    /* renamed from: com.coui.appcompat.edittext.b$f */
    /* loaded from: classes.dex */
    public static class f implements Interpolator {

        /* renamed from: b, reason: collision with root package name */
        private static final float[] f5836b = {0.0f, -1.0f, 0.5f, -0.5f, 0.0f};

        /* renamed from: c, reason: collision with root package name */
        private static final int[] f5837c;

        /* renamed from: d, reason: collision with root package name */
        private static final float[] f5838d;

        /* renamed from: a, reason: collision with root package name */
        private final Interpolator f5839a;

        static {
            int[] iArr = {83, 133, 117, 117};
            f5837c = iArr;
            f5838d = new float[iArr.length + 1];
            int i10 = 0;
            int i11 = 0;
            while (true) {
                int[] iArr2 = f5837c;
                if (i10 >= iArr2.length) {
                    return;
                }
                i11 += iArr2[i10];
                i10++;
                f5838d[i10] = i11 / 450.0f;
            }
        }

        /* synthetic */ f(a aVar) {
            this();
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f10) {
            int i10 = 1;
            while (true) {
                float[] fArr = f5838d;
                if (i10 >= fArr.length) {
                    return 0.0f;
                }
                if (f10 <= fArr[i10]) {
                    int i11 = i10 - 1;
                    float interpolation = this.f5839a.getInterpolation((f10 - fArr[i11]) / (fArr[i10] - fArr[i11]));
                    float[] fArr2 = f5836b;
                    return (fArr2[i11] * (1.0f - interpolation)) + (fArr2[i10] * interpolation);
                }
                i10++;
            }
        }

        private f() {
            this.f5839a = new COUIEaseInterpolator();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public COUIErrorEditTextHelper(EditText editText, int i10) {
        this.f5809a = editText;
        COUICutoutDrawable.a aVar = new COUICutoutDrawable.a(editText);
        this.f5810b = aVar;
        aVar.S(i10);
        aVar.Y(new COUILinearInterpolator());
        aVar.V(new COUILinearInterpolator());
        aVar.M(8388659);
    }

    private void A(boolean z10) {
        if (this.f5822n != null) {
            for (int i10 = 0; i10 < this.f5822n.size(); i10++) {
                this.f5822n.get(i10).b(z10);
            }
        }
    }

    private void E(boolean z10, boolean z11) {
        F(z10, z11, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F(boolean z10, boolean z11, boolean z12) {
        if (this.f5821m == z10) {
            return;
        }
        this.f5821m = z10;
        A(z10);
        if (z11) {
            H(z10, z12);
        } else {
            I(z10, z12);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void G(boolean z10, boolean z11, boolean z12) {
        this.f5823o = false;
        if (z10) {
            if (z11) {
                this.f5809a.setTextColor(this.f5811c);
            }
            this.f5809a.setHighlightColor(s(0.3f));
            if (z12) {
                EditText editText = this.f5809a;
                editText.setSelection(0, editText.getText().length());
                return;
            }
            return;
        }
        this.f5809a.setTextColor(this.f5811c);
        this.f5809a.setHighlightColor(this.f5812d);
    }

    private void H(boolean z10, boolean z11) {
        if (z10) {
            m();
            this.f5809a.setTextColor(0);
            this.f5809a.setHighlightColor(0);
            this.f5825q = 0.0f;
            this.f5826r = 0.0f;
            this.f5827s = 0.0f;
            this.f5823o = true;
            this.f5824p = this.f5809a.isFocused();
            this.f5820l.start();
            return;
        }
        m();
        G(false, false, z11);
    }

    private void I(boolean z10, boolean z11) {
        if (z10) {
            this.f5825q = 1.0f;
            this.f5826r = 0.0f;
            this.f5827s = 0.0f;
            G(true, false, z11);
            return;
        }
        G(false, false, z11);
    }

    private void m() {
        if (this.f5820l.isStarted()) {
            this.f5820l.cancel();
        }
    }

    private Layout.Alignment q() {
        switch (this.f5809a.getTextAlignment()) {
            case 1:
                int gravity = this.f5809a.getGravity() & 8388615;
                if (gravity == 1) {
                    return Layout.Alignment.ALIGN_CENTER;
                }
                if (gravity == 3) {
                    return w() ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
                }
                if (gravity == 5) {
                    return w() ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                }
                if (gravity == 8388611) {
                    return Layout.Alignment.ALIGN_NORMAL;
                }
                if (gravity != 8388613) {
                    return Layout.Alignment.ALIGN_NORMAL;
                }
                return Layout.Alignment.ALIGN_OPPOSITE;
            case 2:
                return Layout.Alignment.ALIGN_NORMAL;
            case 3:
                return Layout.Alignment.ALIGN_OPPOSITE;
            case 4:
                return Layout.Alignment.ALIGN_CENTER;
            case 5:
                return Layout.Alignment.ALIGN_NORMAL;
            case 6:
                return Layout.Alignment.ALIGN_OPPOSITE;
            default:
                return Layout.Alignment.ALIGN_NORMAL;
        }
    }

    private int r(int i10, int i11, float f10) {
        if (f10 <= 0.0f) {
            return i10;
        }
        if (f10 >= 1.0f) {
            return i11;
        }
        float f11 = 1.0f - f10;
        int alpha = (int) ((Color.alpha(i10) * f11) + (Color.alpha(i11) * f10));
        int red = (int) ((Color.red(i10) * f11) + (Color.red(i11) * f10));
        int green = (int) ((Color.green(i10) * f11) + (Color.green(i11) * f10));
        int blue = (int) ((Color.blue(i10) * f11) + (Color.blue(i11) * f10));
        if (alpha > 255) {
            alpha = 255;
        }
        if (red > 255) {
            red = 255;
        }
        if (green > 255) {
            green = 255;
        }
        if (blue > 255) {
            blue = 255;
        }
        return Color.argb(alpha, red, green, blue);
    }

    private int s(float f10) {
        return Color.argb((int) (f10 * 255.0f), Color.red(this.f5813e), Color.green(this.f5813e), Color.blue(this.f5813e));
    }

    private void u() {
        float dimension = this.f5809a.getResources().getDimension(R$dimen.coui_edit_text_shake_amplitude);
        COUIEaseInterpolator cOUIEaseInterpolator = new COUIEaseInterpolator();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setInterpolator(cOUIEaseInterpolator);
        ofFloat.setDuration(217L);
        ofFloat.addUpdateListener(new b());
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, dimension);
        ofFloat2.setInterpolator(new f(null));
        ofFloat2.setDuration(450L);
        ofFloat2.addUpdateListener(new c());
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 0.3f);
        ofFloat3.setInterpolator(cOUIEaseInterpolator);
        ofFloat3.setDuration(133L);
        ofFloat3.setStartDelay(80L);
        ofFloat3.addUpdateListener(new d());
        AnimatorSet animatorSet = new AnimatorSet();
        this.f5820l = animatorSet;
        animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3);
        this.f5820l.addListener(new e());
    }

    private boolean w() {
        return this.f5809a.getLayoutDirection() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z(boolean z10) {
        if (this.f5822n != null) {
            for (int i10 = 0; i10 < this.f5822n.size(); i10++) {
                this.f5822n.get(i10).a(z10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B(int i10, ColorStateList colorStateList) {
        this.f5810b.K(i10, colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void C(int i10) {
        this.f5813e = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D(boolean z10) {
        E(z10, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(COUICutoutDrawable.a aVar) {
        this.f5810b.X(aVar.y());
    }

    public void K(ColorStateList colorStateList) {
        this.f5811c = colorStateList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(COUICutoutDrawable.a aVar) {
        this.f5816h = aVar.n();
        this.f5817i = aVar.t();
        this.f5810b.L(this.f5816h);
        this.f5810b.O(this.f5817i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(COUIEditText.i iVar) {
        if (this.f5822n == null) {
            this.f5822n = new ArrayList<>();
        }
        if (this.f5822n.contains(iVar)) {
            return;
        }
        this.f5822n.add(iVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n(Canvas canvas, int i10, int i11, int i12, Paint paint, Paint paint2) {
        this.f5818j.setColor(r(paint.getColor(), this.f5813e, this.f5825q));
        float f10 = i10;
        canvas.drawLine(0.0f, f10, i11, f10, this.f5818j);
        this.f5818j.setColor(r(paint2.getColor(), this.f5813e, this.f5825q));
        canvas.drawLine(0.0f, f10, i12, f10, this.f5818j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(Canvas canvas, GradientDrawable gradientDrawable, int i10) {
        this.f5815g.setBounds(gradientDrawable.getBounds());
        if (gradientDrawable instanceof COUICutoutDrawable) {
            this.f5815g.h(((COUICutoutDrawable) gradientDrawable).a());
        }
        this.f5815g.setStroke(this.f5814f, r(i10, this.f5813e, this.f5825q));
        this.f5815g.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(int[] iArr) {
        this.f5810b.W(iArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(int i10, int i11, int i12, float[] fArr, COUICutoutDrawable.a aVar) {
        this.f5811c = this.f5809a.getTextColors();
        this.f5812d = this.f5809a.getHighlightColor();
        this.f5813e = i10;
        this.f5814f = i11;
        if (i12 == 2) {
            this.f5810b.a0(Typeface.create("sans-serif-medium", 0));
        }
        this.f5810b.Q(aVar.v());
        this.f5810b.M(aVar.o());
        this.f5810b.P(aVar.u());
        COUICutoutDrawable cOUICutoutDrawable = new COUICutoutDrawable();
        this.f5815g = cOUICutoutDrawable;
        cOUICutoutDrawable.setCornerRadii(fArr);
        Paint paint = new Paint();
        this.f5818j = paint;
        paint.setStrokeWidth(this.f5814f);
        this.f5819k = new Paint();
        u();
        this.f5809a.addTextChangedListener(new a());
        J(aVar);
        L(aVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean v() {
        return this.f5821m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(Canvas canvas) {
        float f10;
        float f11;
        if (this.f5823o && this.f5821m) {
            int save = canvas.save();
            if (!w()) {
                canvas.translate(this.f5826r, 0.0f);
            } else {
                canvas.translate(-this.f5826r, 0.0f);
            }
            int compoundPaddingStart = this.f5809a.getCompoundPaddingStart();
            int compoundPaddingEnd = this.f5809a.getCompoundPaddingEnd();
            int width = this.f5809a.getWidth() - compoundPaddingEnd;
            int i10 = width - compoundPaddingStart;
            float x10 = width + this.f5809a.getX() + this.f5809a.getScrollX();
            float f12 = i10;
            float scrollX = (this.f5828t - this.f5809a.getScrollX()) - f12;
            this.f5809a.getLineBounds(0, f5808v);
            int save2 = canvas.save();
            if (!w()) {
                canvas.translate(compoundPaddingStart, r11.top);
            } else {
                canvas.translate(compoundPaddingEnd, r11.top);
            }
            int save3 = canvas.save();
            if (this.f5809a.getBottom() - this.f5809a.getTop() == this.f5829u && this.f5828t > f12) {
                if (!w()) {
                    canvas.translate(-scrollX, 0.0f);
                    canvas.clipRect(this.f5809a.getScrollX(), 0.0f, x10, this.f5829u);
                } else {
                    canvas.clipRect(this.f5809a.getScrollX() + i10, 0.0f, this.f5809a.getScrollX(), this.f5829u);
                }
            }
            Layout layout = this.f5809a.getLayout();
            layout.getPaint().setColor(this.f5811c.getDefaultColor());
            layout.draw(canvas);
            canvas.restoreToCount(save3);
            canvas.restoreToCount(save2);
            Layout.Alignment q10 = q();
            this.f5819k.setColor(s(this.f5827s));
            if ((q10 != Layout.Alignment.ALIGN_NORMAL || w()) && (!(q10 == Layout.Alignment.ALIGN_OPPOSITE && w()) && (!(q10 == Layout.Alignment.ALIGN_NORMAL && w()) && (q10 != Layout.Alignment.ALIGN_OPPOSITE || w())))) {
                float f13 = ((compoundPaddingStart + r4) - compoundPaddingEnd) / 2.0f;
                float f14 = this.f5828t;
                float f15 = f13 - (f14 / 2.0f);
                f10 = f15;
                f11 = f15 + f14;
            } else {
                f10 = compoundPaddingStart;
                f11 = f10;
            }
            canvas.drawRect(f10, r11.top, f11, r11.bottom, this.f5819k);
            canvas.restoreToCount(save);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(COUICutoutDrawable.a aVar) {
        Rect s7 = aVar.s();
        Rect l10 = aVar.l();
        this.f5810b.N(s7.left, s7.top, s7.right, s7.bottom);
        this.f5810b.J(l10.left, l10.top, l10.right, l10.bottom);
        this.f5810b.H();
    }
}
