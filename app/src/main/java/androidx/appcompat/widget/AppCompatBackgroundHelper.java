package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;

/* compiled from: AppCompatBackgroundHelper.java */
/* renamed from: androidx.appcompat.widget.d, reason: use source file name */
/* loaded from: classes.dex */
class AppCompatBackgroundHelper {

    /* renamed from: a, reason: collision with root package name */
    private final View f1189a;

    /* renamed from: d, reason: collision with root package name */
    private TintInfo f1192d;

    /* renamed from: e, reason: collision with root package name */
    private TintInfo f1193e;

    /* renamed from: f, reason: collision with root package name */
    private TintInfo f1194f;

    /* renamed from: c, reason: collision with root package name */
    private int f1191c = -1;

    /* renamed from: b, reason: collision with root package name */
    private final AppCompatDrawableManager f1190b = AppCompatDrawableManager.b();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatBackgroundHelper(View view) {
        this.f1189a = view;
    }

    private boolean a(Drawable drawable) {
        if (this.f1194f == null) {
            this.f1194f = new TintInfo();
        }
        TintInfo tintInfo = this.f1194f;
        tintInfo.a();
        ColorStateList p10 = ViewCompat.p(this.f1189a);
        if (p10 != null) {
            tintInfo.f1214d = true;
            tintInfo.f1211a = p10;
        }
        PorterDuff.Mode q10 = ViewCompat.q(this.f1189a);
        if (q10 != null) {
            tintInfo.f1213c = true;
            tintInfo.f1212b = q10;
        }
        if (!tintInfo.f1214d && !tintInfo.f1213c) {
            return false;
        }
        AppCompatDrawableManager.i(drawable, tintInfo, this.f1189a.getDrawableState());
        return true;
    }

    private boolean k() {
        return this.f1192d != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        Drawable background = this.f1189a.getBackground();
        if (background != null) {
            if (k() && a(background)) {
                return;
            }
            TintInfo tintInfo = this.f1193e;
            if (tintInfo != null) {
                AppCompatDrawableManager.i(background, tintInfo, this.f1189a.getDrawableState());
                return;
            }
            TintInfo tintInfo2 = this.f1192d;
            if (tintInfo2 != null) {
                AppCompatDrawableManager.i(background, tintInfo2, this.f1189a.getDrawableState());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList c() {
        TintInfo tintInfo = this.f1193e;
        if (tintInfo != null) {
            return tintInfo.f1211a;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode d() {
        TintInfo tintInfo = this.f1193e;
        if (tintInfo != null) {
            return tintInfo.f1212b;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(AttributeSet attributeSet, int i10) {
        Context context = this.f1189a.getContext();
        int[] iArr = R$styleable.ViewBackgroundHelper;
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, iArr, i10, 0);
        View view = this.f1189a;
        ViewCompat.j0(view, view.getContext(), iArr, attributeSet, w10.r(), i10, 0);
        try {
            int i11 = R$styleable.ViewBackgroundHelper_android_background;
            if (w10.s(i11)) {
                this.f1191c = w10.n(i11, -1);
                ColorStateList f10 = this.f1190b.f(this.f1189a.getContext(), this.f1191c);
                if (f10 != null) {
                    h(f10);
                }
            }
            int i12 = R$styleable.ViewBackgroundHelper_backgroundTint;
            if (w10.s(i12)) {
                ViewCompat.q0(this.f1189a, w10.c(i12));
            }
            int i13 = R$styleable.ViewBackgroundHelper_backgroundTintMode;
            if (w10.s(i13)) {
                ViewCompat.r0(this.f1189a, t.d(w10.k(i13, -1), null));
            }
        } finally {
            w10.x();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(Drawable drawable) {
        this.f1191c = -1;
        h(null);
        b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(int i10) {
        this.f1191c = i10;
        AppCompatDrawableManager appCompatDrawableManager = this.f1190b;
        h(appCompatDrawableManager != null ? appCompatDrawableManager.f(this.f1189a.getContext(), i10) : null);
        b();
    }

    void h(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (this.f1192d == null) {
                this.f1192d = new TintInfo();
            }
            TintInfo tintInfo = this.f1192d;
            tintInfo.f1211a = colorStateList;
            tintInfo.f1214d = true;
        } else {
            this.f1192d = null;
        }
        b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(ColorStateList colorStateList) {
        if (this.f1193e == null) {
            this.f1193e = new TintInfo();
        }
        TintInfo tintInfo = this.f1193e;
        tintInfo.f1211a = colorStateList;
        tintInfo.f1214d = true;
        b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(PorterDuff.Mode mode) {
        if (this.f1193e == null) {
            this.f1193e = new TintInfo();
        }
        TintInfo tintInfo = this.f1193e;
        tintInfo.f1212b = mode;
        tintInfo.f1213c = true;
        b();
    }
}
