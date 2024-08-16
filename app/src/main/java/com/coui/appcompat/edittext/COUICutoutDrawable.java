package com.coui.appcompat.edittext;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import androidx.core.view.GravityCompat;
import java.util.ArrayList;
import java.util.Locale;
import z2.COUIChangeTextUtil;

/* compiled from: COUICutoutDrawable.java */
/* renamed from: com.coui.appcompat.edittext.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUICutoutDrawable extends GradientDrawable {

    /* renamed from: a, reason: collision with root package name */
    private final Paint f5779a = new Paint(1);

    /* renamed from: b, reason: collision with root package name */
    private final RectF f5780b;

    /* renamed from: c, reason: collision with root package name */
    private int f5781c;

    /* compiled from: COUICutoutDrawable.java */
    /* renamed from: com.coui.appcompat.edittext.a$a */
    /* loaded from: classes.dex */
    public static final class a {
        private static final boolean L = false;
        private static final Paint M = null;
        private Paint A;
        private float B;
        private float C;
        private float D;
        private float E;
        private int[] F;
        private boolean G;
        private Interpolator H;
        private Interpolator I;
        private float J;

        /* renamed from: a, reason: collision with root package name */
        private final View f5782a;

        /* renamed from: b, reason: collision with root package name */
        private final Rect f5783b;

        /* renamed from: c, reason: collision with root package name */
        private final Rect f5784c;

        /* renamed from: d, reason: collision with root package name */
        private final RectF f5785d;

        /* renamed from: e, reason: collision with root package name */
        private final TextPaint f5786e;

        /* renamed from: f, reason: collision with root package name */
        private final TextPaint f5787f;

        /* renamed from: g, reason: collision with root package name */
        private boolean f5788g;

        /* renamed from: h, reason: collision with root package name */
        private float f5789h;

        /* renamed from: m, reason: collision with root package name */
        private ColorStateList f5794m;

        /* renamed from: n, reason: collision with root package name */
        private ColorStateList f5795n;

        /* renamed from: o, reason: collision with root package name */
        private float f5796o;

        /* renamed from: p, reason: collision with root package name */
        private float f5797p;

        /* renamed from: q, reason: collision with root package name */
        private float f5798q;

        /* renamed from: r, reason: collision with root package name */
        private float f5799r;

        /* renamed from: s, reason: collision with root package name */
        private float f5800s;

        /* renamed from: t, reason: collision with root package name */
        private float f5801t;

        /* renamed from: u, reason: collision with root package name */
        private CharSequence f5802u;

        /* renamed from: v, reason: collision with root package name */
        private CharSequence f5803v;

        /* renamed from: x, reason: collision with root package name */
        private boolean f5805x;

        /* renamed from: y, reason: collision with root package name */
        private boolean f5806y;

        /* renamed from: z, reason: collision with root package name */
        private Bitmap f5807z;

        /* renamed from: i, reason: collision with root package name */
        private int f5790i = 16;

        /* renamed from: j, reason: collision with root package name */
        private int f5791j = 16;

        /* renamed from: k, reason: collision with root package name */
        private float f5792k = 30.0f;

        /* renamed from: l, reason: collision with root package name */
        private float f5793l = 30.0f;

        /* renamed from: w, reason: collision with root package name */
        private ArrayList<CharSequence> f5804w = new ArrayList<>();
        private int K = 1;

        public a(View view) {
            this.f5782a = view;
            TextPaint textPaint = new TextPaint(129);
            this.f5786e = textPaint;
            this.f5787f = new TextPaint(textPaint);
            this.f5784c = new Rect();
            this.f5783b = new Rect();
            this.f5785d = new RectF();
        }

        private void A(float f10) {
            this.f5785d.left = F(this.f5783b.left, this.f5784c.left, f10, this.H);
            this.f5785d.top = F(this.f5796o, this.f5797p, f10, this.H);
            this.f5785d.right = F(this.f5783b.right, this.f5784c.right, f10, this.H);
            this.f5785d.bottom = F(this.f5783b.bottom, this.f5784c.bottom, f10, this.H);
        }

        private static boolean B(float f10, float f11) {
            return Math.abs(f10 - f11) < 0.001f;
        }

        private boolean C() {
            return this.f5782a.getLayoutDirection() == 1;
        }

        private static float E(float f10, float f11, float f12) {
            return f10 + (f12 * (f11 - f10));
        }

        private static float F(float f10, float f11, float f12, Interpolator interpolator) {
            if (interpolator != null) {
                f12 = interpolator.getInterpolation(f12);
            }
            return E(f10, f11, f12);
        }

        private void G() {
            this.f5788g = this.f5784c.width() > 0 && this.f5784c.height() > 0 && this.f5783b.width() > 0 && this.f5783b.height() > 0;
        }

        private static boolean I(Rect rect, int i10, int i11, int i12, int i13) {
            return rect.left == i10 && rect.top == i11 && rect.right == i12 && rect.bottom == i13;
        }

        private void U(float f10) {
            g(f10);
            boolean z10 = L && this.D != 1.0f;
            this.f5806y = z10;
            if (z10) {
                k();
            }
            this.f5782a.postInvalidate();
        }

        private void Z(CharSequence charSequence, float f10) {
            for (int i10 = 1; i10 < this.K; i10++) {
                CharSequence ellipsize = TextUtils.ellipsize(charSequence, this.f5786e, f10, TextUtils.TruncateAt.END);
                if (i10 != this.K - 1 && !TextUtils.equals(ellipsize, charSequence)) {
                    int length = ellipsize.length();
                    if (TextUtils.equals(ellipsize, TextUtils.ellipsize(charSequence.subSequence(0, length), this.f5786e, f10, TextUtils.TruncateAt.END))) {
                        length--;
                    }
                    this.f5804w.add(charSequence.subSequence(0, length));
                    charSequence = charSequence.subSequence(length, charSequence.length());
                } else {
                    this.f5804w.add(ellipsize);
                    return;
                }
            }
        }

        private static int a(int i10, int i11, float f10) {
            float f11 = 1.0f - f10;
            return Color.argb((int) ((Color.alpha(i10) * f11) + (Color.alpha(i11) * f10)), (int) ((Color.red(i10) * f11) + (Color.red(i11) * f10)), (int) ((Color.green(i10) * f11) + (Color.green(i11) * f10)), (int) ((Color.blue(i10) * f11) + (Color.blue(i11) * f10)));
        }

        private void b() {
            float f10 = this.E;
            g(this.f5793l);
            CharSequence charSequence = this.f5803v;
            float measureText = charSequence != null ? this.f5786e.measureText(charSequence, 0, charSequence.length()) : 0.0f;
            int b10 = GravityCompat.b(this.f5791j, this.f5805x ? 1 : 0);
            if (this.K <= 1) {
                int i10 = b10 & 112;
                if (i10 != 48) {
                    if (i10 != 80) {
                        this.f5797p = this.f5784c.centerY() + (((this.f5786e.descent() - this.f5786e.ascent()) / 2.0f) - this.f5786e.descent());
                    } else {
                        this.f5797p = this.f5784c.bottom;
                    }
                } else if (Locale.getDefault().getLanguage().equals("my")) {
                    this.f5797p = this.f5784c.top - (this.f5786e.ascent() * 1.3f);
                } else {
                    this.f5797p = this.f5784c.top - this.f5786e.ascent();
                }
            } else if (Locale.getDefault().getLanguage().equals("my")) {
                this.f5797p = this.f5784c.top - (this.f5786e.ascent() * 1.3f);
            } else {
                this.f5797p = this.f5784c.top - this.f5786e.ascent();
            }
            int i11 = b10 & 8388615;
            if (i11 == 1) {
                this.f5799r = this.f5784c.centerX() - (measureText / 2.0f);
            } else if (i11 != 5) {
                this.f5799r = this.f5784c.left;
            } else {
                this.f5799r = this.f5784c.right - measureText;
            }
            g(this.f5792k);
            CharSequence charSequence2 = this.f5803v;
            float measureText2 = charSequence2 != null ? this.f5786e.measureText(charSequence2, 0, charSequence2.length()) : 0.0f;
            int b11 = GravityCompat.b(this.f5790i, this.f5805x ? 1 : 0);
            if (this.K > 1) {
                this.f5796o = this.f5783b.top - this.f5786e.ascent();
            } else {
                int i12 = b11 & 112;
                if (i12 == 48) {
                    this.f5796o = this.f5783b.top - this.f5786e.ascent();
                } else if (i12 != 80) {
                    this.f5796o = this.f5783b.centerY() + (((this.f5786e.getFontMetrics().bottom - this.f5786e.getFontMetrics().top) / 2.0f) - this.f5786e.getFontMetrics().bottom);
                } else {
                    this.f5796o = this.f5783b.bottom;
                }
            }
            int i13 = b11 & 8388615;
            if (i13 == 1) {
                this.f5798q = this.f5783b.centerX() - (measureText2 / 2.0f);
            } else if (i13 != 5) {
                this.f5798q = this.f5783b.left;
            } else {
                this.f5798q = this.f5783b.right - measureText2;
            }
            h();
            U(f10);
        }

        private void d() {
            f(this.f5789h);
        }

        private boolean e(CharSequence charSequence) {
            return C();
        }

        private void f(float f10) {
            A(f10);
            this.f5800s = F(this.f5798q, this.f5799r, f10, this.H);
            this.f5801t = F(this.f5796o, this.f5797p, f10, this.H);
            U(F(this.f5792k, this.f5793l, f10, this.I));
            if (this.f5795n != this.f5794m) {
                this.f5786e.setColor(a(r(), q(), f10));
            } else {
                this.f5786e.setColor(q());
            }
            this.f5782a.postInvalidate();
        }

        private void g(float f10) {
            float f11;
            boolean z10;
            if (this.f5802u == null) {
                return;
            }
            float width = this.f5784c.width();
            float width2 = this.f5783b.width();
            if (B(f10, this.f5793l)) {
                f11 = this.f5793l;
                this.D = 1.0f;
            } else {
                float f12 = this.f5792k;
                if (B(f10, f12)) {
                    this.D = 1.0f;
                } else {
                    this.D = f10 / this.f5792k;
                }
                float f13 = this.f5793l / this.f5792k;
                width = width2 * f13 > width ? Math.min(width / f13, width2) : width2;
                f11 = f12;
            }
            if (width > 0.0f) {
                z10 = this.E != f11 || this.G;
                this.E = f11;
                this.G = false;
            } else {
                z10 = false;
            }
            if (this.f5803v == null || z10) {
                this.f5786e.setTextSize(this.E);
                this.f5786e.setLinearText(this.D != 1.0f);
                CharSequence ellipsize = TextUtils.ellipsize(this.f5802u, this.f5786e, width - this.J, TextUtils.TruncateAt.END);
                if (!TextUtils.equals(ellipsize, this.f5803v)) {
                    this.f5803v = ellipsize;
                }
                if (this.K > 1 && !TextUtils.equals(ellipsize, this.f5802u) && this.f5802u.length() > ellipsize.length()) {
                    this.f5804w.clear();
                    int length = ellipsize.length();
                    if (TextUtils.equals(ellipsize, TextUtils.ellipsize(this.f5802u.subSequence(0, length), this.f5786e, width - this.J, TextUtils.TruncateAt.END))) {
                        length--;
                    }
                    this.f5804w.add(this.f5802u.subSequence(0, length));
                    CharSequence charSequence = this.f5802u;
                    Z(charSequence.subSequence(length, charSequence.length()), width - this.J);
                }
            }
            this.f5805x = C();
        }

        private void h() {
            Bitmap bitmap = this.f5807z;
            if (bitmap != null) {
                bitmap.recycle();
                this.f5807z = null;
            }
        }

        private float i(float f10, float f11, float f12) {
            return f10 < f11 ? f11 : f10 > f12 ? f12 : f10;
        }

        private void k() {
            if (this.f5807z != null || this.f5783b.isEmpty() || TextUtils.isEmpty(this.f5803v)) {
                return;
            }
            f(0.0f);
            this.B = this.f5786e.ascent();
            this.C = this.f5786e.descent();
            TextPaint textPaint = this.f5786e;
            CharSequence charSequence = this.f5803v;
            int round = Math.round(textPaint.measureText(charSequence, 0, charSequence.length()));
            int round2 = Math.round(this.C - this.B);
            if (round <= 0 || round2 <= 0) {
                return;
            }
            this.f5807z = Bitmap.createBitmap(round, round2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(this.f5807z);
            CharSequence charSequence2 = this.f5803v;
            canvas.drawText(charSequence2, 0, charSequence2.length(), 0.0f, round2 - this.f5786e.descent(), this.f5786e);
            if (this.A == null) {
                this.A = new Paint(3);
            }
        }

        private int r() {
            int[] iArr = this.F;
            if (iArr != null) {
                return this.f5794m.getColorForState(iArr, 0);
            }
            return this.f5794m.getDefaultColor();
        }

        private void z(TextPaint textPaint) {
            textPaint.setTextSize(this.f5793l);
        }

        final boolean D() {
            ColorStateList colorStateList;
            ColorStateList colorStateList2 = this.f5795n;
            return (colorStateList2 != null && colorStateList2.isStateful()) || ((colorStateList = this.f5794m) != null && colorStateList.isStateful());
        }

        public void H() {
            if (this.f5782a.getHeight() <= 0 || this.f5782a.getWidth() <= 0) {
                return;
            }
            b();
            d();
        }

        public void J(int i10, int i11, int i12, int i13) {
            if (I(this.f5784c, i10, i11, i12, i13)) {
                return;
            }
            this.f5784c.set(i10, i11, i12, i13);
            this.G = true;
            G();
            Log.d("COUICollapseTextHelper", "setCollapsedBounds: " + this.f5784c);
        }

        public void K(int i10, ColorStateList colorStateList) {
            this.f5795n = colorStateList;
            this.f5793l = i10;
            H();
        }

        public void L(ColorStateList colorStateList) {
            if (this.f5795n != colorStateList) {
                this.f5795n = colorStateList;
                H();
            }
        }

        public void M(int i10) {
            if (this.f5791j != i10) {
                this.f5791j = i10;
                H();
            }
        }

        public void N(int i10, int i11, int i12, int i13) {
            if (I(this.f5783b, i10, i11, i12, i13)) {
                return;
            }
            this.f5783b.set(i10, i11, i12, i13);
            this.G = true;
            G();
            Log.d("COUICollapseTextHelper", "setExpandedBounds: " + this.f5783b);
        }

        public void O(ColorStateList colorStateList) {
            if (this.f5794m != colorStateList) {
                this.f5794m = colorStateList;
                H();
            }
        }

        public void P(int i10) {
            if (this.f5790i != i10) {
                this.f5790i = i10;
                H();
            }
        }

        public void Q(float f10) {
            if (this.f5792k != f10) {
                this.f5792k = f10;
                H();
            }
        }

        public void R(float f10) {
            float i10 = i(f10, 0.0f, 1.0f);
            if (i10 != this.f5789h) {
                this.f5789h = i10;
                d();
            }
        }

        public void S(int i10) {
            this.K = Math.min(3, Math.max(1, i10));
        }

        public void T(float f10) {
            if (f10 > 0.0f) {
                this.J = f10;
            }
        }

        public void V(Interpolator interpolator) {
            this.H = interpolator;
            H();
        }

        public final boolean W(int[] iArr) {
            this.F = iArr;
            if (!D()) {
                return false;
            }
            H();
            return true;
        }

        public void X(CharSequence charSequence) {
            if (charSequence == null || !charSequence.equals(this.f5802u)) {
                this.f5802u = charSequence;
                this.f5803v = null;
                this.f5804w.clear();
                h();
                H();
            }
        }

        public void Y(Interpolator interpolator) {
            this.I = interpolator;
            H();
        }

        public void a0(Typeface typeface) {
            COUIChangeTextUtil.a(this.f5786e, true);
            COUIChangeTextUtil.a(this.f5787f, true);
            H();
        }

        public float c() {
            if (this.f5802u == null) {
                return 0.0f;
            }
            z(this.f5787f);
            TextPaint textPaint = this.f5787f;
            CharSequence charSequence = this.f5802u;
            return textPaint.measureText(charSequence, 0, charSequence.length());
        }

        public void j(Canvas canvas) {
            float ascent;
            int save = canvas.save();
            if (this.f5803v != null && this.f5788g) {
                float f10 = this.f5800s;
                float f11 = this.f5801t;
                boolean z10 = this.f5806y && this.f5807z != null;
                if (z10) {
                    ascent = this.B * this.D;
                } else {
                    ascent = this.f5786e.ascent() * this.D;
                    this.f5786e.descent();
                }
                if (z10) {
                    f11 += ascent;
                }
                float f12 = f11;
                float f13 = this.D;
                if (f13 != 1.0f) {
                    canvas.scale(f13, f13, f10, f12);
                }
                if (z10) {
                    canvas.drawBitmap(this.f5807z, f10, f12, this.A);
                } else if (this.K != 1 && this.f5804w.size() > 1) {
                    View view = this.f5782a;
                    int lineHeight = view instanceof EditText ? ((EditText) view).getLineHeight() : 0;
                    for (int i10 = 0; i10 < this.f5804w.size(); i10++) {
                        int i11 = lineHeight * i10;
                        CharSequence charSequence = this.f5804w.get(i10);
                        if (C()) {
                            canvas.drawText(charSequence, 0, charSequence.length(), Math.max(0.0f, f10 - this.J), f12 + i11, this.f5786e);
                        } else {
                            canvas.drawText(charSequence, 0, charSequence.length(), this.J + f10, f12 + i11, this.f5786e);
                        }
                    }
                } else if (C()) {
                    CharSequence charSequence2 = this.f5803v;
                    canvas.drawText(charSequence2, 0, charSequence2.length(), Math.max(0.0f, f10 - this.J), f12, this.f5786e);
                } else {
                    CharSequence charSequence3 = this.f5803v;
                    canvas.drawText(charSequence3, 0, charSequence3.length(), this.J + f10, f12, this.f5786e);
                }
            } else {
                canvas.drawText(" ", 0.0f, 0.0f, this.f5786e);
            }
            canvas.restoreToCount(save);
        }

        public Rect l() {
            return this.f5784c;
        }

        public void m(RectF rectF) {
            float c10;
            float f10;
            boolean e10 = e(this.f5802u);
            if (!e10) {
                c10 = this.f5784c.left;
            } else {
                c10 = this.f5784c.right - c();
            }
            rectF.left = c10;
            Rect rect = this.f5784c;
            rectF.top = rect.top;
            if (!e10) {
                f10 = c10 + c();
            } else {
                f10 = rect.right;
            }
            rectF.right = f10;
            rectF.bottom = this.f5784c.top + p();
        }

        public ColorStateList n() {
            return this.f5795n;
        }

        public int o() {
            return this.f5791j;
        }

        public float p() {
            z(this.f5787f);
            if (Locale.getDefault().getLanguage().equals("my")) {
                return (-this.f5787f.ascent()) * 1.3f;
            }
            return -this.f5787f.ascent();
        }

        public int q() {
            int[] iArr = this.F;
            if (iArr != null) {
                return this.f5795n.getColorForState(iArr, 0);
            }
            return this.f5795n.getDefaultColor();
        }

        public Rect s() {
            return this.f5783b;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ColorStateList t() {
            return this.f5794m;
        }

        public int u() {
            return this.f5790i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public float v() {
            return this.f5792k;
        }

        public float w() {
            return this.f5789h;
        }

        public float x() {
            z(this.f5787f);
            float descent = this.f5787f.descent() - this.f5787f.ascent();
            return Locale.getDefault().getLanguage().equals("my") ? descent * 1.3f : descent;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public CharSequence y() {
            return this.f5802u;
        }
    }

    public COUICutoutDrawable() {
        i();
        this.f5780b = new RectF();
    }

    private void c(Canvas canvas) {
        if (j(getCallback())) {
            return;
        }
        canvas.restoreToCount(this.f5781c);
    }

    private void d(Canvas canvas) {
        Drawable.Callback callback = getCallback();
        if (j(callback)) {
            ((View) callback).setLayerType(2, null);
        } else {
            f(canvas);
        }
    }

    private void f(Canvas canvas) {
        this.f5781c = canvas.saveLayer(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), null);
    }

    private void i() {
        this.f5779a.setStyle(Paint.Style.FILL_AND_STROKE);
        this.f5779a.setColor(-1);
        this.f5779a.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    private boolean j(Drawable.Callback callback) {
        return callback instanceof View;
    }

    public RectF a() {
        return this.f5780b;
    }

    public boolean b() {
        return !this.f5780b.isEmpty();
    }

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        d(canvas);
        super.draw(canvas);
        canvas.drawRect(this.f5780b, this.f5779a);
        c(canvas);
    }

    public void e() {
        g(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void g(float f10, float f11, float f12, float f13) {
        RectF rectF = this.f5780b;
        if (f10 == rectF.left && f11 == rectF.top && f12 == rectF.right && f13 == rectF.bottom) {
            return;
        }
        rectF.set(f10, f11, f12, f13);
        invalidateSelf();
    }

    public void h(RectF rectF) {
        g(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }
}
