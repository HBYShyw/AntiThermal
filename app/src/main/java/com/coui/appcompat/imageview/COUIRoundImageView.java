package com.coui.appcompat.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.widget.AppCompatImageView;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$styleable;
import m2.COUIRoundRectUtil;

/* loaded from: classes.dex */
public class COUIRoundImageView extends AppCompatImageView {
    private Paint A;
    private Paint B;
    private int C;
    private Matrix D;
    private BitmapShader E;
    private int F;
    private float G;
    private Drawable H;
    private Bitmap I;
    private float J;
    private int K;
    private Paint L;
    private int M;

    /* renamed from: h, reason: collision with root package name */
    private final RectF f6159h;

    /* renamed from: i, reason: collision with root package name */
    private final RectF f6160i;

    /* renamed from: j, reason: collision with root package name */
    private int f6161j;

    /* renamed from: k, reason: collision with root package name */
    private Context f6162k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f6163l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f6164m;

    /* renamed from: n, reason: collision with root package name */
    private int f6165n;

    /* renamed from: o, reason: collision with root package name */
    private RectF f6166o;

    /* renamed from: p, reason: collision with root package name */
    private RectF f6167p;

    /* renamed from: q, reason: collision with root package name */
    private Drawable f6168q;

    /* renamed from: r, reason: collision with root package name */
    private Bitmap f6169r;

    /* renamed from: s, reason: collision with root package name */
    private int f6170s;

    /* renamed from: t, reason: collision with root package name */
    private int f6171t;

    /* renamed from: u, reason: collision with root package name */
    private int f6172u;

    /* renamed from: v, reason: collision with root package name */
    private int f6173v;

    /* renamed from: w, reason: collision with root package name */
    private BitmapShader f6174w;

    /* renamed from: x, reason: collision with root package name */
    private int f6175x;

    /* renamed from: y, reason: collision with root package name */
    private int f6176y;

    /* renamed from: z, reason: collision with root package name */
    private int f6177z;

    public COUIRoundImageView(Context context) {
        super(context);
        this.f6159h = new RectF();
        this.f6160i = new RectF();
        this.D = new Matrix();
        this.f6162k = context;
        Paint paint = new Paint();
        this.A = paint;
        paint.setAntiAlias(true);
        e();
        Paint paint2 = new Paint();
        this.B = paint2;
        paint2.setAntiAlias(true);
        this.B.setColor(getResources().getColor(R$color.coui_roundimageview_outcircle_color));
        this.B.setStrokeWidth(1.0f);
        this.B.setStyle(Paint.Style.STROKE);
        this.f6161j = 0;
        this.F = getResources().getDimensionPixelSize(R$dimen.coui_roundimageview_default_radius);
        setupShader(getDrawable());
    }

    private Bitmap d(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int max = Math.max(1, drawable.getIntrinsicHeight());
        int max2 = Math.max(1, drawable.getIntrinsicWidth());
        Bitmap createBitmap = Bitmap.createBitmap(max2, max, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, max2, max);
        drawable.draw(canvas);
        return createBitmap;
    }

    private void e() {
        Paint paint = new Paint();
        this.L = paint;
        paint.setStrokeWidth(2.0f);
        this.L.setStyle(Paint.Style.STROKE);
        this.L.setAntiAlias(true);
        this.L.setColor(getResources().getColor(R$color.coui_border));
    }

    private void g() {
        this.D.reset();
        float f10 = (this.f6172u * 1.0f) / this.f6175x;
        float f11 = (this.f6173v * 1.0f) / this.f6176y;
        if (f10 <= 1.0f) {
            f10 = 1.0f;
        }
        float max = Math.max(f10, f11 > 1.0f ? f11 : 1.0f);
        float f12 = (this.f6172u - (this.f6175x * max)) * 0.5f;
        float f13 = (this.f6173v - (this.f6176y * max)) * 0.5f;
        this.D.setScale(max, max);
        Matrix matrix = this.D;
        int i10 = this.f6177z;
        matrix.postTranslate(((int) (f12 + 0.5f)) + (i10 / 2.0f), ((int) (f13 + 0.5f)) + (i10 / 2.0f));
    }

    private void setupShader(Drawable drawable) {
        Drawable drawable2 = getDrawable();
        this.H = drawable2;
        if (drawable2 == null || drawable == null) {
            return;
        }
        if (drawable2 != drawable) {
            this.H = drawable;
        }
        this.f6175x = this.H.getIntrinsicWidth();
        this.f6176y = this.H.getIntrinsicHeight();
        this.I = d(this.H);
        if (this.f6161j == 2) {
            this.f6169r = c();
            Bitmap bitmap = this.f6169r;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            this.f6174w = new BitmapShader(bitmap, tileMode, tileMode);
        }
        Bitmap bitmap2 = this.I;
        if (bitmap2 == null || bitmap2.isRecycled()) {
            return;
        }
        Bitmap bitmap3 = this.I;
        Shader.TileMode tileMode2 = Shader.TileMode.CLAMP;
        this.E = new BitmapShader(bitmap3, tileMode2, tileMode2);
    }

    public Bitmap c() {
        g();
        Bitmap bitmap = this.I;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        this.f6174w = bitmapShader;
        bitmapShader.setLocalMatrix(this.D);
        this.A.setShader(this.f6174w);
        Bitmap createBitmap = Bitmap.createBitmap(this.f6170s, this.f6171t, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        this.f6165n = this.f6172u / 2;
        canvas.drawPath(COUIRoundRectUtil.a().c(this.f6159h, this.f6165n), this.A);
        this.f6168q.setBounds(0, 0, this.f6170s, this.f6171t);
        this.f6168q.draw(canvas);
        return createBitmap;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.H != null) {
            this.H.setState(getDrawableState());
            setupShader(this.H);
            invalidate();
        }
    }

    public void f() {
        this.f6160i.set(0.0f, 0.0f, this.f6170s, this.f6171t);
        this.f6177z = this.f6170s - this.f6172u;
        this.f6159h.set(this.f6160i);
        RectF rectF = this.f6159h;
        int i10 = this.f6177z;
        rectF.inset(i10 / 2, i10 / 2);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        this.J = 1.0f;
        Bitmap bitmap = this.I;
        if (bitmap != null && !bitmap.isRecycled()) {
            int i10 = this.f6161j;
            if (i10 == 0) {
                int min = Math.min(this.I.getWidth(), this.I.getHeight());
                this.K = min;
                this.J = (this.F * 1.0f) / min;
            } else if (i10 == 1) {
                this.J = Math.max((getWidth() * 1.0f) / this.I.getWidth(), (getHeight() * 1.0f) / this.I.getHeight());
            } else if (i10 == 2) {
                this.J = Math.max((getWidth() * 1.0f) / this.f6170s, (getHeight() * 1.0f) / this.f6171t);
                this.D.reset();
                Matrix matrix = this.D;
                float f10 = this.J;
                matrix.setScale(f10, f10);
                this.f6174w.setLocalMatrix(this.D);
                this.A.setShader(this.f6174w);
                canvas.drawRect(this.f6166o, this.A);
                return;
            }
            Matrix matrix2 = this.D;
            float f11 = this.J;
            matrix2.setScale(f11, f11);
            BitmapShader bitmapShader = this.E;
            if (bitmapShader != null) {
                bitmapShader.setLocalMatrix(this.D);
                this.A.setShader(this.E);
            }
        }
        int i11 = this.f6161j;
        if (i11 == 0) {
            if (this.f6163l) {
                float f12 = this.G;
                canvas.drawCircle(f12, f12, f12, this.A);
                float f13 = this.G;
                canvas.drawCircle(f13, f13, f13 - 0.5f, this.B);
                return;
            }
            float f14 = this.G;
            canvas.drawCircle(f14, f14, f14, this.A);
            return;
        }
        if (i11 == 1) {
            if (this.f6166o == null) {
                this.f6166o = new RectF(0.0f, 0.0f, getWidth(), getHeight());
            }
            if (this.f6167p == null) {
                this.f6167p = new RectF(1.0f, 1.0f, getWidth() - 1.0f, getHeight() - 1.0f);
            }
            if (this.f6163l) {
                canvas.drawPath(COUIRoundRectUtil.a().c(this.f6166o, this.f6165n), this.A);
                canvas.drawPath(COUIRoundRectUtil.a().c(this.f6167p, this.f6165n - 1.0f), this.B);
            } else {
                canvas.drawPath(COUIRoundRectUtil.a().c(this.f6166o, this.f6165n), this.A);
            }
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        if (this.f6161j == 0) {
            int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
            if (min == 0) {
                min = this.F;
            }
            this.F = min;
            this.G = min / 2.0f;
            setMeasuredDimension(min, min);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        int i14 = this.f6161j;
        if (i14 == 1 || i14 == 2) {
            this.f6166o = new RectF(0.0f, 0.0f, getWidth(), getHeight());
            this.f6167p = new RectF(1.0f, 1.0f, getWidth() - 1.0f, getHeight() - 1.0f);
        }
    }

    public void setBorderRectRadius(int i10) {
        this.f6165n = i10;
        invalidate();
    }

    public void setHasBorder(boolean z10) {
        this.f6163l = z10;
    }

    public void setHasDefaultPic(boolean z10) {
        this.f6164m = z10;
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setupShader(drawable);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageResource(int i10) {
        super.setImageResource(i10);
        setupShader(this.f6162k.getResources().getDrawable(i10));
    }

    public void setOutCircleColor(int i10) {
        this.C = i10;
        this.B.setColor(i10);
        invalidate();
    }

    public void setType(int i10) {
        if (this.f6161j != i10) {
            this.f6161j = i10;
            if (i10 == 0) {
                int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
                if (min == 0) {
                    min = this.F;
                }
                this.F = min;
                this.G = min / 2.0f;
            }
            invalidate();
        }
    }

    public COUIRoundImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f6159h = new RectF();
        this.f6160i = new RectF();
        if (attributeSet != null) {
            this.M = attributeSet.getStyleAttribute();
        }
        this.D = new Matrix();
        this.f6162k = context;
        Paint paint = new Paint();
        this.A = paint;
        paint.setAntiAlias(true);
        this.A.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        e();
        Paint paint2 = new Paint();
        this.B = paint2;
        paint2.setAntiAlias(true);
        this.B.setStrokeWidth(2.0f);
        this.B.setStyle(Paint.Style.STROKE);
        Drawable drawable = context.getResources().getDrawable(R$drawable.coui_round_image_view_shadow);
        this.f6168q = drawable;
        this.f6170s = drawable.getIntrinsicWidth();
        this.f6171t = this.f6168q.getIntrinsicHeight();
        int dimension = (int) context.getResources().getDimension(R$dimen.coui_roundimageView_src_width);
        this.f6172u = dimension;
        this.f6173v = dimension;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIRoundImageView);
        this.f6165n = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIRoundImageView_couiBorderRadius, (int) TypedValue.applyDimension(1, 1.0f, getResources().getDisplayMetrics()));
        this.f6161j = obtainStyledAttributes.getInt(R$styleable.COUIRoundImageView_couiType, 0);
        this.f6163l = obtainStyledAttributes.getBoolean(R$styleable.COUIRoundImageView_couiHasBorder, false);
        this.f6164m = obtainStyledAttributes.getBoolean(R$styleable.COUIRoundImageView_couiHasDefaultPic, true);
        int color = obtainStyledAttributes.getColor(R$styleable.COUIRoundImageView_couiRoundImageViewOutCircleColor, 0);
        this.C = color;
        this.B.setColor(color);
        f();
        setupShader(getDrawable());
        obtainStyledAttributes.recycle();
    }

    public COUIRoundImageView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6159h = new RectF();
        this.f6160i = new RectF();
        f();
    }
}
