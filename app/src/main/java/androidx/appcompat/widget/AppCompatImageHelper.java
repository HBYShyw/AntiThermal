package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ImageViewCompat;
import c.AppCompatResources;

/* compiled from: AppCompatImageHelper.java */
/* renamed from: androidx.appcompat.widget.k, reason: use source file name */
/* loaded from: classes.dex */
public class AppCompatImageHelper {

    /* renamed from: a, reason: collision with root package name */
    private final ImageView f1234a;

    /* renamed from: b, reason: collision with root package name */
    private TintInfo f1235b;

    /* renamed from: c, reason: collision with root package name */
    private TintInfo f1236c;

    /* renamed from: d, reason: collision with root package name */
    private TintInfo f1237d;

    /* renamed from: e, reason: collision with root package name */
    private int f1238e = 0;

    public AppCompatImageHelper(ImageView imageView) {
        this.f1234a = imageView;
    }

    private boolean a(Drawable drawable) {
        if (this.f1237d == null) {
            this.f1237d = new TintInfo();
        }
        TintInfo tintInfo = this.f1237d;
        tintInfo.a();
        ColorStateList a10 = ImageViewCompat.a(this.f1234a);
        if (a10 != null) {
            tintInfo.f1214d = true;
            tintInfo.f1211a = a10;
        }
        PorterDuff.Mode b10 = ImageViewCompat.b(this.f1234a);
        if (b10 != null) {
            tintInfo.f1213c = true;
            tintInfo.f1212b = b10;
        }
        if (!tintInfo.f1214d && !tintInfo.f1213c) {
            return false;
        }
        AppCompatDrawableManager.i(drawable, tintInfo, this.f1234a.getDrawableState());
        return true;
    }

    private boolean l() {
        return this.f1235b != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        if (this.f1234a.getDrawable() != null) {
            this.f1234a.getDrawable().setLevel(this.f1238e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        Drawable drawable = this.f1234a.getDrawable();
        if (drawable != null) {
            t.b(drawable);
        }
        if (drawable != null) {
            if (l() && a(drawable)) {
                return;
            }
            TintInfo tintInfo = this.f1236c;
            if (tintInfo != null) {
                AppCompatDrawableManager.i(drawable, tintInfo, this.f1234a.getDrawableState());
                return;
            }
            TintInfo tintInfo2 = this.f1235b;
            if (tintInfo2 != null) {
                AppCompatDrawableManager.i(drawable, tintInfo2, this.f1234a.getDrawableState());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList d() {
        TintInfo tintInfo = this.f1236c;
        if (tintInfo != null) {
            return tintInfo.f1211a;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode e() {
        TintInfo tintInfo = this.f1236c;
        if (tintInfo != null) {
            return tintInfo.f1212b;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f() {
        return !(this.f1234a.getBackground() instanceof RippleDrawable);
    }

    public void g(AttributeSet attributeSet, int i10) {
        int n10;
        Context context = this.f1234a.getContext();
        int[] iArr = R$styleable.AppCompatImageView;
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, iArr, i10, 0);
        ImageView imageView = this.f1234a;
        ViewCompat.j0(imageView, imageView.getContext(), iArr, attributeSet, w10.r(), i10, 0);
        try {
            Drawable drawable = this.f1234a.getDrawable();
            if (drawable == null && (n10 = w10.n(R$styleable.AppCompatImageView_srcCompat, -1)) != -1 && (drawable = AppCompatResources.b(this.f1234a.getContext(), n10)) != null) {
                this.f1234a.setImageDrawable(drawable);
            }
            if (drawable != null) {
                t.b(drawable);
            }
            int i11 = R$styleable.AppCompatImageView_tint;
            if (w10.s(i11)) {
                ImageViewCompat.c(this.f1234a, w10.c(i11));
            }
            int i12 = R$styleable.AppCompatImageView_tintMode;
            if (w10.s(i12)) {
                ImageViewCompat.d(this.f1234a, t.d(w10.k(i12, -1), null));
            }
        } finally {
            w10.x();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(Drawable drawable) {
        this.f1238e = drawable.getLevel();
    }

    public void i(int i10) {
        if (i10 != 0) {
            Drawable b10 = AppCompatResources.b(this.f1234a.getContext(), i10);
            if (b10 != null) {
                t.b(b10);
            }
            this.f1234a.setImageDrawable(b10);
        } else {
            this.f1234a.setImageDrawable(null);
        }
        c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(ColorStateList colorStateList) {
        if (this.f1236c == null) {
            this.f1236c = new TintInfo();
        }
        TintInfo tintInfo = this.f1236c;
        tintInfo.f1211a = colorStateList;
        tintInfo.f1214d = true;
        c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(PorterDuff.Mode mode) {
        if (this.f1236c == null) {
            this.f1236c = new TintInfo();
        }
        TintInfo tintInfo = this.f1236c;
        tintInfo.f1212b = mode;
        tintInfo.f1213c = true;
        c();
    }
}
