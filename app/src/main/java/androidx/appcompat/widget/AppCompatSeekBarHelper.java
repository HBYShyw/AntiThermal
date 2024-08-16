package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
import androidx.appcompat.R$styleable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

/* compiled from: AppCompatSeekBarHelper.java */
/* renamed from: androidx.appcompat.widget.n, reason: use source file name */
/* loaded from: classes.dex */
class AppCompatSeekBarHelper extends AppCompatProgressBarHelper {

    /* renamed from: d, reason: collision with root package name */
    private final SeekBar f1266d;

    /* renamed from: e, reason: collision with root package name */
    private Drawable f1267e;

    /* renamed from: f, reason: collision with root package name */
    private ColorStateList f1268f;

    /* renamed from: g, reason: collision with root package name */
    private PorterDuff.Mode f1269g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f1270h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f1271i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatSeekBarHelper(SeekBar seekBar) {
        super(seekBar);
        this.f1268f = null;
        this.f1269g = null;
        this.f1270h = false;
        this.f1271i = false;
        this.f1266d = seekBar;
    }

    private void f() {
        Drawable drawable = this.f1267e;
        if (drawable != null) {
            if (this.f1270h || this.f1271i) {
                Drawable l10 = DrawableCompat.l(drawable.mutate());
                this.f1267e = l10;
                if (this.f1270h) {
                    DrawableCompat.i(l10, this.f1268f);
                }
                if (this.f1271i) {
                    DrawableCompat.j(this.f1267e, this.f1269g);
                }
                if (this.f1267e.isStateful()) {
                    this.f1267e.setState(this.f1266d.getDrawableState());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.appcompat.widget.AppCompatProgressBarHelper
    public void c(AttributeSet attributeSet, int i10) {
        super.c(attributeSet, i10);
        Context context = this.f1266d.getContext();
        int[] iArr = R$styleable.AppCompatSeekBar;
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, iArr, i10, 0);
        SeekBar seekBar = this.f1266d;
        ViewCompat.j0(seekBar, seekBar.getContext(), iArr, attributeSet, w10.r(), i10, 0);
        Drawable h10 = w10.h(R$styleable.AppCompatSeekBar_android_thumb);
        if (h10 != null) {
            this.f1266d.setThumb(h10);
        }
        j(w10.g(R$styleable.AppCompatSeekBar_tickMark));
        int i11 = R$styleable.AppCompatSeekBar_tickMarkTintMode;
        if (w10.s(i11)) {
            this.f1269g = t.d(w10.k(i11, -1), this.f1269g);
            this.f1271i = true;
        }
        int i12 = R$styleable.AppCompatSeekBar_tickMarkTint;
        if (w10.s(i12)) {
            this.f1268f = w10.c(i12);
            this.f1270h = true;
        }
        w10.x();
        f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Canvas canvas) {
        if (this.f1267e != null) {
            int max = this.f1266d.getMax();
            if (max > 1) {
                int intrinsicWidth = this.f1267e.getIntrinsicWidth();
                int intrinsicHeight = this.f1267e.getIntrinsicHeight();
                int i10 = intrinsicWidth >= 0 ? intrinsicWidth / 2 : 1;
                int i11 = intrinsicHeight >= 0 ? intrinsicHeight / 2 : 1;
                this.f1267e.setBounds(-i10, -i11, i10, i11);
                float width = ((this.f1266d.getWidth() - this.f1266d.getPaddingLeft()) - this.f1266d.getPaddingRight()) / max;
                int save = canvas.save();
                canvas.translate(this.f1266d.getPaddingLeft(), this.f1266d.getHeight() / 2);
                for (int i12 = 0; i12 <= max; i12++) {
                    this.f1267e.draw(canvas);
                    canvas.translate(width, 0.0f);
                }
                canvas.restoreToCount(save);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h() {
        Drawable drawable = this.f1267e;
        if (drawable != null && drawable.isStateful() && drawable.setState(this.f1266d.getDrawableState())) {
            this.f1266d.invalidateDrawable(drawable);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        Drawable drawable = this.f1267e;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    void j(Drawable drawable) {
        Drawable drawable2 = this.f1267e;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f1267e = drawable;
        if (drawable != null) {
            drawable.setCallback(this.f1266d);
            DrawableCompat.g(drawable, ViewCompat.x(this.f1266d));
            if (drawable.isStateful()) {
                drawable.setState(this.f1266d.getDrawableState());
            }
            f();
        }
        this.f1266d.invalidate();
    }
}
