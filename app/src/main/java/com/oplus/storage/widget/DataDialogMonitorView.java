package com.oplus.storage.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.Slog;
import android.view.View;
import com.oplus.battery.R;
import ha.StorageUtils;

/* loaded from: classes2.dex */
public class DataDialogMonitorView extends View {

    /* renamed from: m, reason: collision with root package name */
    private static final int[] f10613m = {-12465049, -13759, -2152426};

    /* renamed from: n, reason: collision with root package name */
    private static final float[] f10614n = {0.026f, 0.667f, 1.0f};

    /* renamed from: e, reason: collision with root package name */
    private boolean f10615e;

    /* renamed from: f, reason: collision with root package name */
    private PaintFlagsDrawFilter f10616f;

    /* renamed from: g, reason: collision with root package name */
    private float f10617g;

    /* renamed from: h, reason: collision with root package name */
    private float f10618h;

    /* renamed from: i, reason: collision with root package name */
    private float f10619i;

    /* renamed from: j, reason: collision with root package name */
    private long f10620j;

    /* renamed from: k, reason: collision with root package name */
    private long f10621k;

    /* renamed from: l, reason: collision with root package name */
    private Context f10622l;

    public DataDialogMonitorView(Context context) {
        super(context);
        this.f10615e = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        this.f10617g = b(260.0f);
        this.f10618h = b(8.0f);
        this.f10619i = (b(14.33f) * 2.0f) + b(20.0f);
    }

    private long a(long j10) {
        return j10 / 1073741824;
    }

    private float b(float f10) {
        return (getContext().getResources().getDisplayMetrics().density * f10) + ((f10 >= 0.0f ? 1 : -1) * 0.5f);
    }

    private float c(float f10, float f11, float f12) {
        return (f10 - f11) / (f12 - f11);
    }

    private int d(int i10, int i11, float f10) {
        int red = Color.red(i10);
        int blue = Color.blue(i10);
        return Color.argb(255, (int) (red + ((Color.red(i11) - red) * f10) + 0.5d), (int) (Color.green(i10) + ((Color.green(i11) - r10) * f10) + 0.5d), (int) (blue + ((Color.blue(i11) - blue) * f10) + 0.5d));
    }

    private int e(float f10) {
        if (f10 >= 1.0f) {
            return f10613m[r5.length - 1];
        }
        int i10 = 0;
        while (true) {
            float[] fArr = f10614n;
            if (i10 >= fArr.length) {
                return -1;
            }
            if (f10 <= fArr[i10]) {
                if (i10 == 0) {
                    return f10613m[0];
                }
                int[] iArr = f10613m;
                int i11 = i10 - 1;
                return d(iArr[i11], iArr[i10], c(f10, fArr[i11], fArr[i10]));
            }
            i10++;
        }
    }

    @Override // android.view.View
    @SuppressLint({"ResourceAsColor"})
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(this.f10616f);
        int right = getRight() - getLeft();
        int bottom = getBottom() - getTop();
        if (this.f10615e) {
            Slog.d("DataDialogMonitorView", "availableWidth = " + right + " availableHeight = " + bottom);
        }
        int i10 = right / 2;
        int i11 = bottom / 2;
        float f10 = 1.0f;
        float f11 = right;
        float f12 = this.f10617g;
        if (f11 < f12 || bottom < this.f10618h) {
            f10 = Math.min(f11 / (f12 + 4.0f), bottom / this.f10618h);
            canvas.save();
            canvas.scale(f10, f10, i10, i11);
            if (this.f10615e) {
                Slog.d("DataDialogMonitorView", "should scale, scale = " + f10 + ", mPictureWidth= " + this.f10617g + " mPictureHeight = " + this.f10618h);
            }
        }
        this.f10617g *= f10;
        float f13 = this.f10618h * f10;
        this.f10618h = f13;
        float f14 = i11 - (f13 / 2.0f);
        long j10 = SystemProperties.getLong("sys.data.free.bytes", -1L);
        if (j10 < 0) {
            j10 = StorageUtils.d();
        }
        if (this.f10615e) {
            Slog.d("DataDialogMonitorView", "mPictureWidth= " + this.f10617g + " mPictureHeight = " + this.f10618h + " dataFreeSpace = " + j10);
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0.0f, f14, f11, this.f10618h + f14);
        paint.setShader(new LinearGradient(0.0f, f14, f11, f14 + this.f10618h, f10613m, f10614n, Shader.TileMode.MIRROR));
        canvas.drawRoundRect(rectF, 12.0f, 21.0f, paint);
        Paint paint2 = new Paint();
        paint2.setTextSize(b(10.0f));
        paint2.setTextAlign(Paint.Align.RIGHT);
        paint2.setColor(getResources().getColor(R.color.battery_text_color_dark));
        paint2.setFontVariationSettings("'wght' 500");
        int i12 = paint2.getFontMetricsInt().top;
        canvas.drawText(this.f10622l.getResources().getString(R.string.oplus_data_space_monitor_curve), f11, this.f10618h + f14 + b(15.165f), paint2);
        Paint paint3 = new Paint();
        paint3.setTextSize(b(10.0f));
        paint3.setTextAlign(Paint.Align.RIGHT);
        paint3.setColor(getResources().getColor(R.color.battery_text_color_dark));
        paint3.setFontVariationSettings("'wght' 500");
        int i13 = paint3.getFontMetricsInt().bottom;
        String string = this.f10622l.getResources().getString(R.string.oplus_data_used_space);
        float f15 = f11 * 0.3f * ((float) j10);
        if (f15 / ((float) this.f10621k) > b(20.0f)) {
            canvas.drawText(string, (f11 - (f15 / ((float) this.f10621k))) + b(20.0f), f14 - b(12.0f), paint3);
        } else {
            canvas.drawText(string, f11, f14 - b(12.0f), paint3);
        }
        Paint paint4 = new Paint();
        paint4.setTextSize(b(10.0f));
        paint4.setTextAlign(Paint.Align.LEFT);
        paint4.setFontVariationSettings("'wght' 500");
        try {
            paint4.setColor(getResources().getColor(R.color.battery_text_color_dark));
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        int i14 = paint4.getFontMetricsInt().bottom;
        canvas.drawText(String.format(this.f10622l.getResources().getString(R.string.oplus_data_storage_size), Long.valueOf(a(this.f10620j))), 0.0f, this.f10618h + f14 + b(15.165f), paint4);
        Paint paint5 = new Paint();
        paint5.setAntiAlias(true);
        paint5.setColor(e(((f11 - (f15 / ((float) this.f10621k))) + b(6.0f)) / f11));
        paint5.setStyle(Paint.Style.FILL);
        canvas.drawCircle(f11 - (f15 / ((float) this.f10621k)), f14 + (this.f10618h / 2.0f), b(6.0f), paint5);
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        View.MeasureSpec.getMode(i11);
        View.MeasureSpec.getSize(i11);
        setMeasuredDimension(size, (int) (this.f10618h + this.f10619i));
    }

    public DataDialogMonitorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f10615e = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        this.f10617g = b(260.0f);
        this.f10618h = b(8.0f);
        this.f10619i = (b(14.33f) * 2.0f) + b(20.0f);
        this.f10622l = context;
        this.f10616f = new PaintFlagsDrawFilter(0, 3);
        StorageUtils.g(this.f10622l);
        this.f10620j = StorageUtils.j();
        this.f10621k = StorageUtils.f();
    }
}
