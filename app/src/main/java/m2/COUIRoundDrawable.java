package m2;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/* compiled from: COUIRoundDrawable.java */
/* renamed from: m2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIRoundDrawable extends Drawable {

    /* renamed from: a, reason: collision with root package name */
    private Paint f14890a;

    /* renamed from: b, reason: collision with root package name */
    private Paint f14891b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f14892c;

    /* renamed from: d, reason: collision with root package name */
    private RectF f14893d;

    /* renamed from: e, reason: collision with root package name */
    private Path f14894e;

    /* renamed from: f, reason: collision with root package name */
    private Path f14895f;

    /* renamed from: g, reason: collision with root package name */
    private a f14896g;

    /* renamed from: h, reason: collision with root package name */
    private PorterDuffColorFilter f14897h;

    /* renamed from: i, reason: collision with root package name */
    private PorterDuffColorFilter f14898i;

    public COUIRoundDrawable() {
        this(new a());
    }

    private void b() {
        this.f14894e = COUIShapePath.a(this.f14894e, e(), this.f14896g.f14907i);
    }

    private void c() {
        this.f14895f = COUIShapePath.a(this.f14895f, e(), this.f14896g.f14907i);
    }

    private PorterDuffColorFilter d(ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return new PorterDuffColorFilter(colorStateList.getColorForState(getState(), 0), mode);
    }

    private boolean f() {
        Paint paint = this.f14890a;
        return ((paint == null || paint.getColor() == 0) && this.f14897h == null) ? false : true;
    }

    private boolean g() {
        Paint paint = this.f14891b;
        return ((paint == null || paint.getStrokeWidth() <= 0.0f || this.f14891b.getColor() == 0) && this.f14898i == null) ? false : true;
    }

    private static int i(int i10, int i11) {
        return (i10 * (i11 + (i11 >>> 7))) >>> 8;
    }

    private boolean k(int[] iArr) {
        boolean z10;
        int color;
        int colorForState;
        int color2;
        int colorForState2;
        if (this.f14896g.f14900b == null || color2 == (colorForState2 = this.f14896g.f14900b.getColorForState(iArr, (color2 = this.f14890a.getColor())))) {
            z10 = false;
        } else {
            this.f14890a.setColor(colorForState2);
            z10 = true;
        }
        if (this.f14896g.f14901c == null || color == (colorForState = this.f14896g.f14901c.getColorForState(iArr, (color = this.f14891b.getColor())))) {
            return z10;
        }
        this.f14891b.setColor(colorForState);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.f14890a.setColorFilter(this.f14897h);
        int alpha = this.f14890a.getAlpha();
        this.f14890a.setAlpha(i(alpha, this.f14896g.f14906h));
        this.f14891b.setStrokeWidth(this.f14896g.f14905g);
        this.f14891b.setColorFilter(this.f14898i);
        int alpha2 = this.f14891b.getAlpha();
        this.f14891b.setAlpha(i(alpha2, this.f14896g.f14906h));
        if (this.f14892c) {
            c();
            b();
            this.f14892c = false;
        }
        if (f()) {
            canvas.drawPath(this.f14894e, this.f14890a);
        }
        if (g()) {
            canvas.drawPath(this.f14895f, this.f14891b);
        }
        this.f14890a.setAlpha(alpha);
        this.f14891b.setAlpha(alpha2);
    }

    protected RectF e() {
        this.f14893d.set(getBounds());
        return this.f14893d;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.f14896g;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public void h() {
        this.f14892c = false;
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        this.f14892c = true;
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        ColorStateList colorStateList3;
        ColorStateList colorStateList4;
        return super.isStateful() || ((colorStateList = this.f14896g.f14903e) != null && colorStateList.isStateful()) || (((colorStateList2 = this.f14896g.f14902d) != null && colorStateList2.isStateful()) || (((colorStateList3 = this.f14896g.f14901c) != null && colorStateList3.isStateful()) || ((colorStateList4 = this.f14896g.f14900b) != null && colorStateList4.isStateful())));
    }

    public void j(float f10) {
        this.f14896g.f14907i = f10;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        this.f14896g = new a(this.f14896g);
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        this.f14892c = true;
        super.onBoundsChange(rect);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        boolean k10 = k(iArr);
        if (k10) {
            invalidateSelf();
        }
        return k10;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        a aVar = this.f14896g;
        if (aVar.f14906h != i10) {
            aVar.f14906h = i10;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        a aVar = this.f14896g;
        if (aVar.f14899a != colorFilter) {
            aVar.f14899a = colorFilter;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setTint(int i10) {
        setTintList(ColorStateList.valueOf(i10));
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        a aVar = this.f14896g;
        aVar.f14903e = colorStateList;
        PorterDuffColorFilter d10 = d(colorStateList, aVar.f14904f);
        this.f14898i = d10;
        this.f14897h = d10;
        h();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        a aVar = this.f14896g;
        aVar.f14904f = mode;
        PorterDuffColorFilter d10 = d(aVar.f14903e, mode);
        this.f14898i = d10;
        this.f14897h = d10;
        h();
    }

    public COUIRoundDrawable(a aVar) {
        this.f14890a = new Paint(1);
        this.f14891b = new Paint(1);
        this.f14893d = new RectF();
        this.f14894e = new Path();
        this.f14895f = new Path();
        this.f14896g = aVar;
        this.f14890a.setStyle(Paint.Style.FILL);
        this.f14891b.setStyle(Paint.Style.STROKE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIRoundDrawable.java */
    /* renamed from: m2.a$a */
    /* loaded from: classes.dex */
    public static final class a extends Drawable.ConstantState {

        /* renamed from: a, reason: collision with root package name */
        public ColorFilter f14899a;

        /* renamed from: b, reason: collision with root package name */
        public ColorStateList f14900b;

        /* renamed from: c, reason: collision with root package name */
        public ColorStateList f14901c;

        /* renamed from: d, reason: collision with root package name */
        public ColorStateList f14902d;

        /* renamed from: e, reason: collision with root package name */
        public ColorStateList f14903e;

        /* renamed from: f, reason: collision with root package name */
        public PorterDuff.Mode f14904f;

        /* renamed from: g, reason: collision with root package name */
        public float f14905g;

        /* renamed from: h, reason: collision with root package name */
        public int f14906h;

        /* renamed from: i, reason: collision with root package name */
        public float f14907i;

        public a() {
            this.f14899a = null;
            this.f14900b = null;
            this.f14901c = null;
            this.f14902d = null;
            this.f14903e = null;
            this.f14904f = PorterDuff.Mode.SRC_IN;
            this.f14906h = 255;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            COUIRoundDrawable cOUIRoundDrawable = new COUIRoundDrawable(this);
            cOUIRoundDrawable.f14892c = true;
            return cOUIRoundDrawable;
        }

        public a(a aVar) {
            this.f14899a = null;
            this.f14900b = null;
            this.f14901c = null;
            this.f14902d = null;
            this.f14903e = null;
            this.f14904f = PorterDuff.Mode.SRC_IN;
            this.f14906h = 255;
            this.f14899a = aVar.f14899a;
            this.f14900b = aVar.f14900b;
            this.f14901c = aVar.f14901c;
            this.f14902d = aVar.f14902d;
            this.f14903e = aVar.f14903e;
            this.f14905g = aVar.f14905g;
            this.f14907i = aVar.f14907i;
        }
    }
}
