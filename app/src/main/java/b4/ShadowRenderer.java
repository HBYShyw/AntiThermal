package b4;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import androidx.core.graphics.ColorUtils;

/* compiled from: ShadowRenderer.java */
/* renamed from: b4.a, reason: use source file name */
/* loaded from: classes.dex */
public class ShadowRenderer {

    /* renamed from: i, reason: collision with root package name */
    private static final int[] f4555i = new int[3];

    /* renamed from: j, reason: collision with root package name */
    private static final float[] f4556j = {0.0f, 0.5f, 1.0f};

    /* renamed from: k, reason: collision with root package name */
    private static final int[] f4557k = new int[4];

    /* renamed from: l, reason: collision with root package name */
    private static final float[] f4558l = {0.0f, 0.0f, 0.5f, 1.0f};

    /* renamed from: a, reason: collision with root package name */
    private final Paint f4559a;

    /* renamed from: b, reason: collision with root package name */
    private final Paint f4560b;

    /* renamed from: c, reason: collision with root package name */
    private final Paint f4561c;

    /* renamed from: d, reason: collision with root package name */
    private int f4562d;

    /* renamed from: e, reason: collision with root package name */
    private int f4563e;

    /* renamed from: f, reason: collision with root package name */
    private int f4564f;

    /* renamed from: g, reason: collision with root package name */
    private final Path f4565g;

    /* renamed from: h, reason: collision with root package name */
    private Paint f4566h;

    public ShadowRenderer() {
        this(-16777216);
    }

    public void a(Canvas canvas, Matrix matrix, RectF rectF, int i10, float f10, float f11) {
        boolean z10 = f11 < 0.0f;
        Path path = this.f4565g;
        if (z10) {
            int[] iArr = f4557k;
            iArr[0] = 0;
            iArr[1] = this.f4564f;
            iArr[2] = this.f4563e;
            iArr[3] = this.f4562d;
        } else {
            path.rewind();
            path.moveTo(rectF.centerX(), rectF.centerY());
            path.arcTo(rectF, f10, f11);
            path.close();
            float f12 = -i10;
            rectF.inset(f12, f12);
            int[] iArr2 = f4557k;
            iArr2[0] = 0;
            iArr2[1] = this.f4562d;
            iArr2[2] = this.f4563e;
            iArr2[3] = this.f4564f;
        }
        float width = rectF.width() / 2.0f;
        if (width <= 0.0f) {
            return;
        }
        float f13 = 1.0f - (i10 / width);
        float[] fArr = f4558l;
        fArr[1] = f13;
        fArr[2] = ((1.0f - f13) / 2.0f) + f13;
        this.f4560b.setShader(new RadialGradient(rectF.centerX(), rectF.centerY(), width, f4557k, fArr, Shader.TileMode.CLAMP));
        canvas.save();
        canvas.concat(matrix);
        canvas.scale(1.0f, rectF.height() / rectF.width());
        if (!z10) {
            canvas.clipPath(path, Region.Op.DIFFERENCE);
            canvas.drawPath(path, this.f4566h);
        }
        canvas.drawArc(rectF, f10, f11, true, this.f4560b);
        canvas.restore();
    }

    public void b(Canvas canvas, Matrix matrix, RectF rectF, int i10) {
        rectF.bottom += i10;
        rectF.offset(0.0f, -i10);
        int[] iArr = f4555i;
        iArr[0] = this.f4564f;
        iArr[1] = this.f4563e;
        iArr[2] = this.f4562d;
        Paint paint = this.f4561c;
        float f10 = rectF.left;
        paint.setShader(new LinearGradient(f10, rectF.top, f10, rectF.bottom, iArr, f4556j, Shader.TileMode.CLAMP));
        canvas.save();
        canvas.concat(matrix);
        canvas.drawRect(rectF, this.f4561c);
        canvas.restore();
    }

    public Paint c() {
        return this.f4559a;
    }

    public void d(int i10) {
        this.f4562d = ColorUtils.n(i10, 68);
        this.f4563e = ColorUtils.n(i10, 20);
        this.f4564f = ColorUtils.n(i10, 0);
        this.f4559a.setColor(this.f4562d);
    }

    public ShadowRenderer(int i10) {
        this.f4565g = new Path();
        this.f4566h = new Paint();
        this.f4559a = new Paint();
        d(i10);
        this.f4566h.setColor(0);
        Paint paint = new Paint(4);
        this.f4560b = paint;
        paint.setStyle(Paint.Style.FILL);
        this.f4561c = new Paint(paint);
    }
}
