package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import androidx.appcompat.R$styleable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.CompoundButtonCompat;
import c.AppCompatResources;

/* compiled from: AppCompatCompoundButtonHelper.java */
/* renamed from: androidx.appcompat.widget.f, reason: use source file name */
/* loaded from: classes.dex */
class AppCompatCompoundButtonHelper {

    /* renamed from: a, reason: collision with root package name */
    private final CompoundButton f1205a;

    /* renamed from: b, reason: collision with root package name */
    private ColorStateList f1206b = null;

    /* renamed from: c, reason: collision with root package name */
    private PorterDuff.Mode f1207c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f1208d = false;

    /* renamed from: e, reason: collision with root package name */
    private boolean f1209e = false;

    /* renamed from: f, reason: collision with root package name */
    private boolean f1210f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatCompoundButtonHelper(CompoundButton compoundButton) {
        this.f1205a = compoundButton;
    }

    void a() {
        Drawable a10 = CompoundButtonCompat.a(this.f1205a);
        if (a10 != null) {
            if (this.f1208d || this.f1209e) {
                Drawable mutate = DrawableCompat.l(a10).mutate();
                if (this.f1208d) {
                    DrawableCompat.i(mutate, this.f1206b);
                }
                if (this.f1209e) {
                    DrawableCompat.j(mutate, this.f1207c);
                }
                if (mutate.isStateful()) {
                    mutate.setState(this.f1205a.getDrawableState());
                }
                this.f1205a.setButtonDrawable(mutate);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b(int i10) {
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList c() {
        return this.f1206b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode d() {
        return this.f1207c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x003d A[Catch: all -> 0x0084, TRY_ENTER, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:8:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0060 A[Catch: all -> 0x0084, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:8:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0071 A[Catch: all -> 0x0084, TRY_LEAVE, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:8:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:2:0x001d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void e(AttributeSet attributeSet, int i10) {
        boolean z10;
        int i11;
        int i12;
        int n10;
        int n11;
        Context context = this.f1205a.getContext();
        int[] iArr = R$styleable.CompoundButton;
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, iArr, i10, 0);
        CompoundButton compoundButton = this.f1205a;
        ViewCompat.j0(compoundButton, compoundButton.getContext(), iArr, attributeSet, w10.r(), i10, 0);
        try {
            int i13 = R$styleable.CompoundButton_buttonCompat;
            if (w10.s(i13) && (n11 = w10.n(i13, 0)) != 0) {
                try {
                    CompoundButton compoundButton2 = this.f1205a;
                    compoundButton2.setButtonDrawable(AppCompatResources.b(compoundButton2.getContext(), n11));
                    z10 = true;
                } catch (Resources.NotFoundException unused) {
                }
                if (!z10) {
                    int i14 = R$styleable.CompoundButton_android_button;
                    if (w10.s(i14) && (n10 = w10.n(i14, 0)) != 0) {
                        CompoundButton compoundButton3 = this.f1205a;
                        compoundButton3.setButtonDrawable(AppCompatResources.b(compoundButton3.getContext(), n10));
                    }
                }
                i11 = R$styleable.CompoundButton_buttonTint;
                if (w10.s(i11)) {
                    CompoundButtonCompat.c(this.f1205a, w10.c(i11));
                }
                i12 = R$styleable.CompoundButton_buttonTintMode;
                if (w10.s(i12)) {
                    CompoundButtonCompat.d(this.f1205a, t.d(w10.k(i12, -1), null));
                }
            }
            z10 = false;
            if (!z10) {
            }
            i11 = R$styleable.CompoundButton_buttonTint;
            if (w10.s(i11)) {
            }
            i12 = R$styleable.CompoundButton_buttonTintMode;
            if (w10.s(i12)) {
            }
        } finally {
            w10.x();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        if (this.f1210f) {
            this.f1210f = false;
        } else {
            this.f1210f = true;
            a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(ColorStateList colorStateList) {
        this.f1206b = colorStateList;
        this.f1208d = true;
        a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(PorterDuff.Mode mode) {
        this.f1207c = mode;
        this.f1209e = true;
        a();
    }
}
