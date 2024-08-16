package k2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$styleable;
import m2.COUIRoundRectUtil;

/* compiled from: COUIHintRedDotHelper.java */
/* renamed from: k2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIHintRedDotHelper {

    /* renamed from: a, reason: collision with root package name */
    private int f14009a;

    /* renamed from: b, reason: collision with root package name */
    private int f14010b;

    /* renamed from: c, reason: collision with root package name */
    private int f14011c;

    /* renamed from: d, reason: collision with root package name */
    private int f14012d;

    /* renamed from: e, reason: collision with root package name */
    private int f14013e;

    /* renamed from: f, reason: collision with root package name */
    private int f14014f;

    /* renamed from: g, reason: collision with root package name */
    private int f14015g;

    /* renamed from: h, reason: collision with root package name */
    private int f14016h;

    /* renamed from: i, reason: collision with root package name */
    private int f14017i;

    /* renamed from: j, reason: collision with root package name */
    private int f14018j;

    /* renamed from: k, reason: collision with root package name */
    private int f14019k;

    /* renamed from: l, reason: collision with root package name */
    private int f14020l;

    /* renamed from: m, reason: collision with root package name */
    private int f14021m;

    /* renamed from: n, reason: collision with root package name */
    private int f14022n;

    /* renamed from: o, reason: collision with root package name */
    private TextPaint f14023o;

    /* renamed from: p, reason: collision with root package name */
    private Paint f14024p;

    public COUIHintRedDotHelper(Context context, AttributeSet attributeSet, int[] iArr, int i10, int i11) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i10, i11);
        this.f14009a = obtainStyledAttributes.getColor(R$styleable.COUIHintRedDot_couiHintRedDotColor, 0);
        this.f14010b = obtainStyledAttributes.getColor(R$styleable.COUIHintRedDot_couiHintRedDotTextColor, 0);
        this.f14011c = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIHintRedDot_couiHintTextSize, 0);
        this.f14012d = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIHintRedDot_couiSmallWidth, 0);
        this.f14013e = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIHintRedDot_couiMediumWidth, 0);
        this.f14014f = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIHintRedDot_couiLargeWidth, 0);
        this.f14016h = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIHintRedDot_couiHeight, 0);
        this.f14017i = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIHintRedDot_couiCornerRadius, 0);
        this.f14018j = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIHintRedDot_couiDotDiameter, 0);
        this.f14020l = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIHintRedDot_couiEllipsisDiameter, 0);
        obtainStyledAttributes.recycle();
        this.f14019k = context.getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_rect_radius);
        this.f14015g = context.getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_navi_small_width);
        this.f14021m = context.getResources().getDimensionPixelSize(R$dimen.coui_hint_red_dot_ellipsis_spacing);
        this.f14022n = context.getResources().getDimensionPixelSize(R$dimen.coui_dot_stroke_width);
        TextPaint textPaint = new TextPaint();
        this.f14023o = textPaint;
        textPaint.setAntiAlias(true);
        this.f14023o.setColor(this.f14010b);
        this.f14023o.setTextSize(this.f14011c);
        this.f14023o.setTypeface(Typeface.create("sans-serif-medium", 0));
        Paint paint = new Paint();
        this.f14024p = paint;
        paint.setAntiAlias(true);
        this.f14024p.setColor(this.f14009a);
        this.f14024p.setStyle(Paint.Style.FILL);
    }

    private void a(Canvas canvas, int i10, int i11, RectF rectF) {
        if (i10 <= 0) {
            return;
        }
        this.f14023o.setAlpha(Math.max(0, Math.min(255, i11)));
        if (i10 < 1000) {
            String valueOf = String.valueOf(i10);
            Paint.FontMetricsInt fontMetricsInt = this.f14023o.getFontMetricsInt();
            int measureText = (int) this.f14023o.measureText(valueOf);
            float f10 = rectF.left;
            canvas.drawText(valueOf, f10 + (((rectF.right - f10) - measureText) / 2.0f), (((rectF.top + rectF.bottom) - fontMetricsInt.ascent) - fontMetricsInt.descent) / 2.0f, this.f14023o);
            return;
        }
        float f11 = (rectF.left + rectF.right) / 2.0f;
        float f12 = (rectF.top + rectF.bottom) / 2.0f;
        for (int i12 = -1; i12 <= 1; i12++) {
            int i13 = this.f14021m;
            canvas.drawCircle(((i13 + r2) * i12) + f11, f12, this.f14020l / 2.0f, this.f14023o);
        }
    }

    private void b(Canvas canvas, RectF rectF) {
        float f10 = rectF.bottom;
        float f11 = rectF.top;
        float f12 = (f10 - f11) / 2.0f;
        canvas.drawCircle(rectF.left + f12, f11 + f12, f12, this.f14024p);
    }

    private void c(Canvas canvas, RectF rectF) {
        float f10 = rectF.bottom;
        float f11 = rectF.top;
        float f12 = (f10 - f11) / 2.0f;
        canvas.drawCircle(rectF.left + f12, f11 + f12, f12 - this.f14022n, this.f14024p);
    }

    private void e(Canvas canvas, Object obj, RectF rectF) {
        Path c10;
        boolean z10 = obj instanceof String;
        if (z10) {
            if (TextUtils.isEmpty((CharSequence) obj)) {
                return;
            }
        } else if (obj instanceof Integer) {
            if (((Integer) obj).intValue() <= 0) {
                return;
            }
        } else {
            throw new IllegalArgumentException("params 'number' must be String or Integer!");
        }
        if (Math.min(rectF.right - rectF.left, rectF.bottom - rectF.top) < this.f14017i * 2) {
            c10 = COUIRoundRectUtil.a().c(rectF, ((int) Math.min(rectF.right - rectF.left, rectF.bottom - rectF.top)) / 2);
        } else {
            c10 = COUIRoundRectUtil.a().c(rectF, this.f14017i);
        }
        canvas.drawPath(c10, this.f14024p);
        if (z10) {
            g(canvas, (String) obj, 255, rectF);
        } else {
            a(canvas, ((Integer) obj).intValue(), 255, rectF);
        }
    }

    private void g(Canvas canvas, String str, int i10, RectF rectF) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.f14023o.setAlpha(Math.max(0, Math.min(255, i10)));
        float measureText = this.f14023o.measureText(str);
        if (measureText < this.f14023o.measureText(String.valueOf(1000))) {
            Paint.FontMetricsInt fontMetricsInt = this.f14023o.getFontMetricsInt();
            float f10 = rectF.left;
            canvas.drawText(str, f10 + (((rectF.right - f10) - measureText) / 2.0f), (((rectF.top + rectF.bottom) - fontMetricsInt.ascent) - fontMetricsInt.descent) / 2.0f, this.f14023o);
            return;
        }
        float f11 = (rectF.left + rectF.right) / 2.0f;
        float f12 = (rectF.top + rectF.bottom) / 2.0f;
        for (int i11 = -1; i11 <= 1; i11++) {
            int i12 = this.f14021m;
            canvas.drawCircle(((i12 + r2) * i11) + f11, f12, this.f14020l / 2.0f, this.f14023o);
        }
    }

    private int h() {
        return this.f14016h;
    }

    private int i(int i10) {
        if (i10 < 10) {
            return Math.max(this.f14012d, this.f14016h);
        }
        if (i10 < 100) {
            return Math.max(this.f14013e, this.f14016h);
        }
        if (i10 < 1000) {
            return Math.max(this.f14014f, this.f14016h);
        }
        return Math.max(this.f14013e, this.f14016h);
    }

    private int j(String str) {
        if (TextUtils.isEmpty(str)) {
            return this.f14012d;
        }
        if (p(str)) {
            return i(Integer.parseInt(str));
        }
        float measureText = (int) this.f14023o.measureText(str);
        if (measureText < this.f14023o.measureText(String.valueOf(10))) {
            return Math.max(this.f14012d, this.f14016h);
        }
        if (measureText < this.f14023o.measureText(String.valueOf(100))) {
            return Math.max(this.f14013e, this.f14016h);
        }
        if (measureText < this.f14023o.measureText(String.valueOf(1000))) {
            return Math.max(this.f14014f, this.f14016h);
        }
        return Math.max(this.f14013e, this.f14016h);
    }

    private int k(int i10) {
        if (i10 < 10) {
            return this.f14015g;
        }
        if (i10 < 100) {
            return this.f14012d;
        }
        return this.f14013e;
    }

    private int l(String str) {
        float measureText = (int) this.f14023o.measureText(str);
        if (measureText < this.f14023o.measureText(String.valueOf(10))) {
            return this.f14015g;
        }
        if (measureText < this.f14023o.measureText(String.valueOf(100))) {
            return this.f14012d;
        }
        return this.f14013e;
    }

    private boolean p(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int length = str.length();
        do {
            length--;
            if (length < 0) {
                return true;
            }
        } while (Character.isDigit(str.charAt(length)));
        return false;
    }

    public void d(Canvas canvas, int i10, int i11, int i12, int i13, RectF rectF) {
        canvas.drawPath(COUIRoundRectUtil.a().c(rectF, this.f14017i), this.f14024p);
        if (i11 > i13) {
            a(canvas, i10, i11, rectF);
            a(canvas, i12, i13, rectF);
        } else {
            a(canvas, i12, i13, rectF);
            a(canvas, i10, i11, rectF);
        }
    }

    public void f(Canvas canvas, int i10, Object obj, RectF rectF) {
        if (i10 == 1) {
            b(canvas, rectF);
            return;
        }
        if (i10 == 2 || i10 == 3) {
            e(canvas, obj, rectF);
        } else {
            if (i10 != 4) {
                return;
            }
            c(canvas, rectF);
        }
    }

    public int m(int i10) {
        if (i10 != 1) {
            if (i10 == 2) {
                return h();
            }
            if (i10 == 3) {
                return this.f14013e / 2;
            }
            if (i10 != 4) {
                return 0;
            }
        }
        return this.f14018j;
    }

    public int n(int i10, int i11) {
        if (i10 != 1) {
            if (i10 == 2) {
                return i(i11);
            }
            if (i10 == 3) {
                return k(i11);
            }
            if (i10 != 4) {
                return 0;
            }
        }
        return this.f14018j;
    }

    public int o(int i10, String str) {
        if (i10 != 1) {
            if (i10 == 2) {
                return j(str);
            }
            if (i10 == 3) {
                return l(str);
            }
            if (i10 != 4) {
                return 0;
            }
        }
        return this.f14018j;
    }

    public void q(int i10) {
        this.f14009a = i10;
        this.f14024p.setColor(i10);
    }

    public void r(int i10) {
        this.f14017i = i10;
    }

    public void s(int i10) {
        this.f14018j = i10;
    }

    public void t(int i10) {
        this.f14020l = i10;
    }

    public void u(int i10) {
        this.f14014f = i10;
    }

    public void v(int i10) {
        this.f14013e = i10;
    }

    public void w(int i10) {
        this.f14012d = i10;
    }

    public void x(int i10) {
        this.f14010b = i10;
        this.f14023o.setColor(i10);
    }

    public void y(int i10) {
        this.f14011c = i10;
    }

    public void z(int i10) {
        this.f14016h = i10;
        r(i10 / 2);
    }
}
