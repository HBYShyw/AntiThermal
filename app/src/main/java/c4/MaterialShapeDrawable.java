package c4;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import androidx.core.util.ObjectsCompat;
import b4.ShadowRenderer;
import c4.ShapeAppearanceModel;
import c4.ShapeAppearancePathProvider;
import c4.o;
import com.google.android.material.R$attr;
import java.util.BitSet;
import r3.MaterialColors;
import u3.ElevationOverlayProvider;

/* compiled from: MaterialShapeDrawable.java */
/* renamed from: c4.h, reason: use source file name */
/* loaded from: classes.dex */
public class MaterialShapeDrawable extends Drawable implements Shapeable {
    private static final String B = MaterialShapeDrawable.class.getSimpleName();
    private static final Paint C;
    private boolean A;

    /* renamed from: e, reason: collision with root package name */
    private c f4764e;

    /* renamed from: f, reason: collision with root package name */
    private final o.g[] f4765f;

    /* renamed from: g, reason: collision with root package name */
    private final o.g[] f4766g;

    /* renamed from: h, reason: collision with root package name */
    private final BitSet f4767h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f4768i;

    /* renamed from: j, reason: collision with root package name */
    private final Matrix f4769j;

    /* renamed from: k, reason: collision with root package name */
    private final Path f4770k;

    /* renamed from: l, reason: collision with root package name */
    private final Path f4771l;

    /* renamed from: m, reason: collision with root package name */
    private final RectF f4772m;

    /* renamed from: n, reason: collision with root package name */
    private final RectF f4773n;

    /* renamed from: o, reason: collision with root package name */
    private final Region f4774o;

    /* renamed from: p, reason: collision with root package name */
    private final Region f4775p;

    /* renamed from: q, reason: collision with root package name */
    private ShapeAppearanceModel f4776q;

    /* renamed from: r, reason: collision with root package name */
    private final Paint f4777r;

    /* renamed from: s, reason: collision with root package name */
    private final Paint f4778s;

    /* renamed from: t, reason: collision with root package name */
    private final ShadowRenderer f4779t;

    /* renamed from: u, reason: collision with root package name */
    private final ShapeAppearancePathProvider.b f4780u;

    /* renamed from: v, reason: collision with root package name */
    private final ShapeAppearancePathProvider f4781v;

    /* renamed from: w, reason: collision with root package name */
    private PorterDuffColorFilter f4782w;

    /* renamed from: x, reason: collision with root package name */
    private PorterDuffColorFilter f4783x;

    /* renamed from: y, reason: collision with root package name */
    private int f4784y;

    /* renamed from: z, reason: collision with root package name */
    private final RectF f4785z;

    /* compiled from: MaterialShapeDrawable.java */
    /* renamed from: c4.h$a */
    /* loaded from: classes.dex */
    class a implements ShapeAppearancePathProvider.b {
        a() {
        }

        @Override // c4.ShapeAppearancePathProvider.b
        public void a(o oVar, Matrix matrix, int i10) {
            MaterialShapeDrawable.this.f4767h.set(i10 + 4, oVar.e());
            MaterialShapeDrawable.this.f4766g[i10] = oVar.f(matrix);
        }

        @Override // c4.ShapeAppearancePathProvider.b
        public void b(o oVar, Matrix matrix, int i10) {
            MaterialShapeDrawable.this.f4767h.set(i10, oVar.e());
            MaterialShapeDrawable.this.f4765f[i10] = oVar.f(matrix);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialShapeDrawable.java */
    /* renamed from: c4.h$b */
    /* loaded from: classes.dex */
    public class b implements ShapeAppearanceModel.c {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ float f4787a;

        b(float f10) {
            this.f4787a = f10;
        }

        @Override // c4.ShapeAppearanceModel.c
        public CornerSize a(CornerSize cornerSize) {
            return cornerSize instanceof RelativeCornerSize ? cornerSize : new AdjustedCornerSize(this.f4787a, cornerSize);
        }
    }

    static {
        Paint paint = new Paint(1);
        C = paint;
        paint.setColor(-1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    /* synthetic */ MaterialShapeDrawable(c cVar, a aVar) {
        this(cVar);
    }

    private float F() {
        if (O()) {
            return this.f4778s.getStrokeWidth() / 2.0f;
        }
        return 0.0f;
    }

    private boolean M() {
        c cVar = this.f4764e;
        int i10 = cVar.f4805q;
        return i10 != 1 && cVar.f4806r > 0 && (i10 == 2 || W());
    }

    private boolean N() {
        Paint.Style style = this.f4764e.f4810v;
        return style == Paint.Style.FILL_AND_STROKE || style == Paint.Style.FILL;
    }

    private boolean O() {
        Paint.Style style = this.f4764e.f4810v;
        return (style == Paint.Style.FILL_AND_STROKE || style == Paint.Style.STROKE) && this.f4778s.getStrokeWidth() > 0.0f;
    }

    private void Q() {
        super.invalidateSelf();
    }

    private void T(Canvas canvas) {
        if (M()) {
            canvas.save();
            V(canvas);
            if (!this.A) {
                m(canvas);
                canvas.restore();
                return;
            }
            int width = (int) (this.f4785z.width() - getBounds().width());
            int height = (int) (this.f4785z.height() - getBounds().height());
            if (width >= 0 && height >= 0) {
                Bitmap createBitmap = Bitmap.createBitmap(((int) this.f4785z.width()) + (this.f4764e.f4806r * 2) + width, ((int) this.f4785z.height()) + (this.f4764e.f4806r * 2) + height, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(createBitmap);
                float f10 = (getBounds().left - this.f4764e.f4806r) - width;
                float f11 = (getBounds().top - this.f4764e.f4806r) - height;
                canvas2.translate(-f10, -f11);
                m(canvas2);
                canvas.drawBitmap(createBitmap, f10, f11, (Paint) null);
                createBitmap.recycle();
                canvas.restore();
                return;
            }
            throw new IllegalStateException("Invalid shadow bounds. Check that the treatments result in a valid path.");
        }
    }

    private static int U(int i10, int i11) {
        return (i10 * (i11 + (i11 >>> 7))) >>> 8;
    }

    private void V(Canvas canvas) {
        canvas.translate(A(), B());
    }

    private PorterDuffColorFilter e(Paint paint, boolean z10) {
        if (!z10) {
            return null;
        }
        int color = paint.getColor();
        int k10 = k(color);
        this.f4784y = k10;
        if (k10 != color) {
            return new PorterDuffColorFilter(k10, PorterDuff.Mode.SRC_IN);
        }
        return null;
    }

    private void f(RectF rectF, Path path) {
        g(rectF, path);
        if (this.f4764e.f4798j != 1.0f) {
            this.f4769j.reset();
            Matrix matrix = this.f4769j;
            float f10 = this.f4764e.f4798j;
            matrix.setScale(f10, f10, rectF.width() / 2.0f, rectF.height() / 2.0f);
            path.transform(this.f4769j);
        }
        path.computeBounds(this.f4785z, true);
    }

    private void h() {
        ShapeAppearanceModel y4 = D().y(new b(-F()));
        this.f4776q = y4;
        this.f4781v.d(y4, this.f4764e.f4799k, u(), this.f4771l);
    }

    private PorterDuffColorFilter i(ColorStateList colorStateList, PorterDuff.Mode mode, boolean z10) {
        int colorForState = colorStateList.getColorForState(getState(), 0);
        if (z10) {
            colorForState = k(colorForState);
        }
        this.f4784y = colorForState;
        return new PorterDuffColorFilter(colorForState, mode);
    }

    private PorterDuffColorFilter j(ColorStateList colorStateList, PorterDuff.Mode mode, Paint paint, boolean z10) {
        if (colorStateList != null && mode != null) {
            return i(colorStateList, mode, z10);
        }
        return e(paint, z10);
    }

    public static MaterialShapeDrawable l(Context context, float f10) {
        int c10 = MaterialColors.c(context, R$attr.colorSurface, MaterialShapeDrawable.class.getSimpleName());
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        materialShapeDrawable.P(context);
        materialShapeDrawable.a0(ColorStateList.valueOf(c10));
        materialShapeDrawable.Z(f10);
        return materialShapeDrawable;
    }

    private void m(Canvas canvas) {
        if (this.f4767h.cardinality() > 0) {
            Log.w(B, "Compatibility shadow requested but can't be drawn for all operations in this shape.");
        }
        if (this.f4764e.f4807s != 0) {
            canvas.drawPath(this.f4770k, this.f4779t.c());
        }
        for (int i10 = 0; i10 < 4; i10++) {
            this.f4765f[i10].b(this.f4779t, this.f4764e.f4806r, canvas);
            this.f4766g[i10].b(this.f4779t, this.f4764e.f4806r, canvas);
        }
        if (this.A) {
            int A = A();
            int B2 = B();
            canvas.translate(-A, -B2);
            canvas.drawPath(this.f4770k, C);
            canvas.translate(A, B2);
        }
    }

    private void n(Canvas canvas) {
        p(canvas, this.f4777r, this.f4770k, this.f4764e.f4789a, t());
    }

    private boolean o0(int[] iArr) {
        boolean z10;
        int color;
        int colorForState;
        int color2;
        int colorForState2;
        if (this.f4764e.f4792d == null || color2 == (colorForState2 = this.f4764e.f4792d.getColorForState(iArr, (color2 = this.f4777r.getColor())))) {
            z10 = false;
        } else {
            this.f4777r.setColor(colorForState2);
            z10 = true;
        }
        if (this.f4764e.f4793e == null || color == (colorForState = this.f4764e.f4793e.getColorForState(iArr, (color = this.f4778s.getColor())))) {
            return z10;
        }
        this.f4778s.setColor(colorForState);
        return true;
    }

    private void p(Canvas canvas, Paint paint, Path path, ShapeAppearanceModel shapeAppearanceModel, RectF rectF) {
        if (shapeAppearanceModel.u(rectF)) {
            float a10 = shapeAppearanceModel.t().a(rectF) * this.f4764e.f4799k;
            canvas.drawRoundRect(rectF, a10, a10, paint);
        } else {
            canvas.drawPath(path, paint);
        }
    }

    private boolean p0() {
        PorterDuffColorFilter porterDuffColorFilter = this.f4782w;
        PorterDuffColorFilter porterDuffColorFilter2 = this.f4783x;
        c cVar = this.f4764e;
        this.f4782w = j(cVar.f4795g, cVar.f4796h, this.f4777r, true);
        c cVar2 = this.f4764e;
        this.f4783x = j(cVar2.f4794f, cVar2.f4796h, this.f4778s, false);
        c cVar3 = this.f4764e;
        if (cVar3.f4809u) {
            this.f4779t.d(cVar3.f4795g.getColorForState(getState(), 0));
        }
        return (ObjectsCompat.a(porterDuffColorFilter, this.f4782w) && ObjectsCompat.a(porterDuffColorFilter2, this.f4783x)) ? false : true;
    }

    private void q0() {
        float L = L();
        this.f4764e.f4806r = (int) Math.ceil(0.75f * L);
        this.f4764e.f4807s = (int) Math.ceil(L * 0.25f);
        p0();
        Q();
    }

    private RectF u() {
        this.f4773n.set(t());
        float F = F();
        this.f4773n.inset(F, F);
        return this.f4773n;
    }

    public int A() {
        c cVar = this.f4764e;
        return (int) (cVar.f4807s * Math.sin(Math.toRadians(cVar.f4808t)));
    }

    public int B() {
        c cVar = this.f4764e;
        return (int) (cVar.f4807s * Math.cos(Math.toRadians(cVar.f4808t)));
    }

    public int C() {
        return this.f4764e.f4806r;
    }

    public ShapeAppearanceModel D() {
        return this.f4764e.f4789a;
    }

    public ColorStateList E() {
        return this.f4764e.f4793e;
    }

    public float G() {
        return this.f4764e.f4800l;
    }

    public ColorStateList H() {
        return this.f4764e.f4795g;
    }

    public float I() {
        return this.f4764e.f4789a.r().a(t());
    }

    public float J() {
        return this.f4764e.f4789a.t().a(t());
    }

    public float K() {
        return this.f4764e.f4804p;
    }

    public float L() {
        return v() + K();
    }

    public void P(Context context) {
        this.f4764e.f4790b = new ElevationOverlayProvider(context);
        q0();
    }

    public boolean R() {
        ElevationOverlayProvider elevationOverlayProvider = this.f4764e.f4790b;
        return elevationOverlayProvider != null && elevationOverlayProvider.e();
    }

    public boolean S() {
        return this.f4764e.f4789a.u(t());
    }

    public boolean W() {
        if (S()) {
            return false;
        }
        this.f4770k.isConvex();
        return false;
    }

    public void X(float f10) {
        setShapeAppearanceModel(this.f4764e.f4789a.w(f10));
    }

    public void Y(CornerSize cornerSize) {
        setShapeAppearanceModel(this.f4764e.f4789a.x(cornerSize));
    }

    public void Z(float f10) {
        c cVar = this.f4764e;
        if (cVar.f4803o != f10) {
            cVar.f4803o = f10;
            q0();
        }
    }

    public void a0(ColorStateList colorStateList) {
        c cVar = this.f4764e;
        if (cVar.f4792d != colorStateList) {
            cVar.f4792d = colorStateList;
            onStateChange(getState());
        }
    }

    public void b0(float f10) {
        c cVar = this.f4764e;
        if (cVar.f4799k != f10) {
            cVar.f4799k = f10;
            this.f4768i = true;
            invalidateSelf();
        }
    }

    public void c0(int i10, int i11, int i12, int i13) {
        c cVar = this.f4764e;
        if (cVar.f4797i == null) {
            cVar.f4797i = new Rect();
        }
        this.f4764e.f4797i.set(i10, i11, i12, i13);
        invalidateSelf();
    }

    public void d0(Paint.Style style) {
        this.f4764e.f4810v = style;
        Q();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.f4777r.setColorFilter(this.f4782w);
        int alpha = this.f4777r.getAlpha();
        this.f4777r.setAlpha(U(alpha, this.f4764e.f4801m));
        this.f4778s.setColorFilter(this.f4783x);
        this.f4778s.setStrokeWidth(this.f4764e.f4800l);
        int alpha2 = this.f4778s.getAlpha();
        this.f4778s.setAlpha(U(alpha2, this.f4764e.f4801m));
        if (this.f4768i) {
            h();
            f(t(), this.f4770k);
            this.f4768i = false;
        }
        T(canvas);
        if (N()) {
            n(canvas);
        }
        if (O()) {
            q(canvas);
        }
        this.f4777r.setAlpha(alpha);
        this.f4778s.setAlpha(alpha2);
    }

    public void e0(float f10) {
        c cVar = this.f4764e;
        if (cVar.f4802n != f10) {
            cVar.f4802n = f10;
            q0();
        }
    }

    public void f0(boolean z10) {
        this.A = z10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void g(RectF rectF, Path path) {
        ShapeAppearancePathProvider shapeAppearancePathProvider = this.f4781v;
        c cVar = this.f4764e;
        shapeAppearancePathProvider.e(cVar.f4789a, cVar.f4799k, rectF, this.f4780u, path);
    }

    public void g0(int i10) {
        this.f4779t.d(i10);
        this.f4764e.f4809u = false;
        Q();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f4764e.f4801m;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.f4764e;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    @TargetApi(21)
    public void getOutline(Outline outline) {
        if (this.f4764e.f4805q == 2) {
            return;
        }
        if (S()) {
            outline.setRoundRect(getBounds(), I() * this.f4764e.f4799k);
        } else {
            f(t(), this.f4770k);
            this.f4770k.isConvex();
            try {
                outline.setConvexPath(this.f4770k);
            } catch (IllegalArgumentException unused) {
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        Rect rect2 = this.f4764e.f4797i;
        if (rect2 != null) {
            rect.set(rect2);
            return true;
        }
        return super.getPadding(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        this.f4774o.set(getBounds());
        f(t(), this.f4770k);
        this.f4775p.setPath(this.f4770k, this.f4774o);
        this.f4774o.op(this.f4775p, Region.Op.DIFFERENCE);
        return this.f4774o;
    }

    public void h0(int i10) {
        c cVar = this.f4764e;
        if (cVar.f4808t != i10) {
            cVar.f4808t = i10;
            Q();
        }
    }

    public void i0(int i10) {
        c cVar = this.f4764e;
        if (cVar.f4805q != i10) {
            cVar.f4805q = i10;
            Q();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        this.f4768i = true;
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        ColorStateList colorStateList3;
        ColorStateList colorStateList4;
        return super.isStateful() || ((colorStateList = this.f4764e.f4795g) != null && colorStateList.isStateful()) || (((colorStateList2 = this.f4764e.f4794f) != null && colorStateList2.isStateful()) || (((colorStateList3 = this.f4764e.f4793e) != null && colorStateList3.isStateful()) || ((colorStateList4 = this.f4764e.f4792d) != null && colorStateList4.isStateful())));
    }

    public void j0(int i10) {
        c cVar = this.f4764e;
        if (cVar.f4807s != i10) {
            cVar.f4807s = i10;
            Q();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int k(int i10) {
        float L = L() + y();
        ElevationOverlayProvider elevationOverlayProvider = this.f4764e.f4790b;
        return elevationOverlayProvider != null ? elevationOverlayProvider.c(i10, L) : i10;
    }

    public void k0(float f10, int i10) {
        n0(f10);
        m0(ColorStateList.valueOf(i10));
    }

    public void l0(float f10, ColorStateList colorStateList) {
        n0(f10);
        m0(colorStateList);
    }

    public void m0(ColorStateList colorStateList) {
        c cVar = this.f4764e;
        if (cVar.f4793e != colorStateList) {
            cVar.f4793e = colorStateList;
            onStateChange(getState());
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        this.f4764e = new c(this.f4764e);
        return this;
    }

    public void n0(float f10) {
        this.f4764e.f4800l = f10;
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void o(Canvas canvas, Paint paint, Path path, RectF rectF) {
        p(canvas, paint, path, this.f4764e.f4789a, rectF);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        this.f4768i = true;
        super.onBoundsChange(rect);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        boolean z10 = o0(iArr) || p0();
        if (z10) {
            invalidateSelf();
        }
        return z10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void q(Canvas canvas) {
        p(canvas, this.f4778s, this.f4771l, this.f4776q, u());
    }

    public float r() {
        return this.f4764e.f4789a.j().a(t());
    }

    public float s() {
        return this.f4764e.f4789a.l().a(t());
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        c cVar = this.f4764e;
        if (cVar.f4801m != i10) {
            cVar.f4801m = i10;
            Q();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f4764e.f4791c = colorFilter;
        Q();
    }

    @Override // c4.Shapeable
    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.f4764e.f4789a = shapeAppearanceModel;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTint(int i10) {
        setTintList(ColorStateList.valueOf(i10));
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        this.f4764e.f4795g = colorStateList;
        p0();
        Q();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        c cVar = this.f4764e;
        if (cVar.f4796h != mode) {
            cVar.f4796h = mode;
            p0();
            Q();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RectF t() {
        this.f4772m.set(getBounds());
        return this.f4772m;
    }

    public float v() {
        return this.f4764e.f4803o;
    }

    public ColorStateList w() {
        return this.f4764e.f4792d;
    }

    public float x() {
        return this.f4764e.f4799k;
    }

    public float y() {
        return this.f4764e.f4802n;
    }

    public int z() {
        return this.f4784y;
    }

    public MaterialShapeDrawable() {
        this(new ShapeAppearanceModel());
    }

    public MaterialShapeDrawable(Context context, AttributeSet attributeSet, int i10, int i11) {
        this(ShapeAppearanceModel.e(context, attributeSet, i10, i11).m());
    }

    public MaterialShapeDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        this(new c(shapeAppearanceModel, null));
    }

    private MaterialShapeDrawable(c cVar) {
        ShapeAppearancePathProvider shapeAppearancePathProvider;
        this.f4765f = new o.g[4];
        this.f4766g = new o.g[4];
        this.f4767h = new BitSet(8);
        this.f4769j = new Matrix();
        this.f4770k = new Path();
        this.f4771l = new Path();
        this.f4772m = new RectF();
        this.f4773n = new RectF();
        this.f4774o = new Region();
        this.f4775p = new Region();
        Paint paint = new Paint(1);
        this.f4777r = paint;
        Paint paint2 = new Paint(1);
        this.f4778s = paint2;
        this.f4779t = new ShadowRenderer();
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            shapeAppearancePathProvider = ShapeAppearancePathProvider.k();
        } else {
            shapeAppearancePathProvider = new ShapeAppearancePathProvider();
        }
        this.f4781v = shapeAppearancePathProvider;
        this.f4785z = new RectF();
        this.A = true;
        this.f4764e = cVar;
        paint2.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        p0();
        o0(getState());
        this.f4780u = new a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialShapeDrawable.java */
    /* renamed from: c4.h$c */
    /* loaded from: classes.dex */
    public static final class c extends Drawable.ConstantState {

        /* renamed from: a, reason: collision with root package name */
        public ShapeAppearanceModel f4789a;

        /* renamed from: b, reason: collision with root package name */
        public ElevationOverlayProvider f4790b;

        /* renamed from: c, reason: collision with root package name */
        public ColorFilter f4791c;

        /* renamed from: d, reason: collision with root package name */
        public ColorStateList f4792d;

        /* renamed from: e, reason: collision with root package name */
        public ColorStateList f4793e;

        /* renamed from: f, reason: collision with root package name */
        public ColorStateList f4794f;

        /* renamed from: g, reason: collision with root package name */
        public ColorStateList f4795g;

        /* renamed from: h, reason: collision with root package name */
        public PorterDuff.Mode f4796h;

        /* renamed from: i, reason: collision with root package name */
        public Rect f4797i;

        /* renamed from: j, reason: collision with root package name */
        public float f4798j;

        /* renamed from: k, reason: collision with root package name */
        public float f4799k;

        /* renamed from: l, reason: collision with root package name */
        public float f4800l;

        /* renamed from: m, reason: collision with root package name */
        public int f4801m;

        /* renamed from: n, reason: collision with root package name */
        public float f4802n;

        /* renamed from: o, reason: collision with root package name */
        public float f4803o;

        /* renamed from: p, reason: collision with root package name */
        public float f4804p;

        /* renamed from: q, reason: collision with root package name */
        public int f4805q;

        /* renamed from: r, reason: collision with root package name */
        public int f4806r;

        /* renamed from: s, reason: collision with root package name */
        public int f4807s;

        /* renamed from: t, reason: collision with root package name */
        public int f4808t;

        /* renamed from: u, reason: collision with root package name */
        public boolean f4809u;

        /* renamed from: v, reason: collision with root package name */
        public Paint.Style f4810v;

        public c(ShapeAppearanceModel shapeAppearanceModel, ElevationOverlayProvider elevationOverlayProvider) {
            this.f4792d = null;
            this.f4793e = null;
            this.f4794f = null;
            this.f4795g = null;
            this.f4796h = PorterDuff.Mode.SRC_IN;
            this.f4797i = null;
            this.f4798j = 1.0f;
            this.f4799k = 1.0f;
            this.f4801m = 255;
            this.f4802n = 0.0f;
            this.f4803o = 0.0f;
            this.f4804p = 0.0f;
            this.f4805q = 0;
            this.f4806r = 0;
            this.f4807s = 0;
            this.f4808t = 0;
            this.f4809u = false;
            this.f4810v = Paint.Style.FILL_AND_STROKE;
            this.f4789a = shapeAppearanceModel;
            this.f4790b = elevationOverlayProvider;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this, null);
            materialShapeDrawable.f4768i = true;
            return materialShapeDrawable;
        }

        public c(c cVar) {
            this.f4792d = null;
            this.f4793e = null;
            this.f4794f = null;
            this.f4795g = null;
            this.f4796h = PorterDuff.Mode.SRC_IN;
            this.f4797i = null;
            this.f4798j = 1.0f;
            this.f4799k = 1.0f;
            this.f4801m = 255;
            this.f4802n = 0.0f;
            this.f4803o = 0.0f;
            this.f4804p = 0.0f;
            this.f4805q = 0;
            this.f4806r = 0;
            this.f4807s = 0;
            this.f4808t = 0;
            this.f4809u = false;
            this.f4810v = Paint.Style.FILL_AND_STROKE;
            this.f4789a = cVar.f4789a;
            this.f4790b = cVar.f4790b;
            this.f4800l = cVar.f4800l;
            this.f4791c = cVar.f4791c;
            this.f4792d = cVar.f4792d;
            this.f4793e = cVar.f4793e;
            this.f4796h = cVar.f4796h;
            this.f4795g = cVar.f4795g;
            this.f4801m = cVar.f4801m;
            this.f4798j = cVar.f4798j;
            this.f4807s = cVar.f4807s;
            this.f4805q = cVar.f4805q;
            this.f4809u = cVar.f4809u;
            this.f4799k = cVar.f4799k;
            this.f4802n = cVar.f4802n;
            this.f4803o = cVar.f4803o;
            this.f4804p = cVar.f4804p;
            this.f4806r = cVar.f4806r;
            this.f4808t = cVar.f4808t;
            this.f4794f = cVar.f4794f;
            this.f4810v = cVar.f4810v;
            if (cVar.f4797i != null) {
                this.f4797i = new Rect(cVar.f4797i);
            }
        }
    }
}
