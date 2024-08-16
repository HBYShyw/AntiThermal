package a4;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import c4.Shapeable;

/* compiled from: RippleDrawableCompat.java */
/* renamed from: a4.a, reason: use source file name */
/* loaded from: classes.dex */
public class RippleDrawableCompat extends Drawable implements Shapeable {

    /* renamed from: e, reason: collision with root package name */
    private b f33e;

    @Override // android.graphics.drawable.Drawable
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public RippleDrawableCompat mutate() {
        this.f33e = new b(this.f33e);
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        b bVar = this.f33e;
        if (bVar.f35b) {
            bVar.f34a.draw(canvas);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.f33e;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.f33e.f34a.getOpacity();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.f33e.f34a.setBounds(rect);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        if (this.f33e.f34a.setState(iArr)) {
            onStateChange = true;
        }
        boolean e10 = RippleUtils.e(iArr);
        b bVar = this.f33e;
        if (bVar.f35b == e10) {
            return onStateChange;
        }
        bVar.f35b = e10;
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        this.f33e.f34a.setAlpha(i10);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f33e.f34a.setColorFilter(colorFilter);
    }

    @Override // c4.Shapeable
    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.f33e.f34a.setShapeAppearanceModel(shapeAppearanceModel);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTint(int i10) {
        this.f33e.f34a.setTint(i10);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        this.f33e.f34a.setTintList(colorStateList);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        this.f33e.f34a.setTintMode(mode);
    }

    public RippleDrawableCompat(ShapeAppearanceModel shapeAppearanceModel) {
        this(new b(new MaterialShapeDrawable(shapeAppearanceModel)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: RippleDrawableCompat.java */
    /* renamed from: a4.a$b */
    /* loaded from: classes.dex */
    public static final class b extends Drawable.ConstantState {

        /* renamed from: a, reason: collision with root package name */
        MaterialShapeDrawable f34a;

        /* renamed from: b, reason: collision with root package name */
        boolean f35b;

        public b(MaterialShapeDrawable materialShapeDrawable) {
            this.f34a = materialShapeDrawable;
            this.f35b = false;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public RippleDrawableCompat newDrawable() {
            return new RippleDrawableCompat(new b(this));
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        public b(b bVar) {
            this.f34a = (MaterialShapeDrawable) bVar.f34a.getConstantState().newDrawable();
            this.f35b = bVar.f35b;
        }
    }

    private RippleDrawableCompat(b bVar) {
        this.f33e = bVar;
    }
}
