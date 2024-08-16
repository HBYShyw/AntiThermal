package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import androidx.appcompat.R$styleable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.CheckedTextViewCompat;
import c.AppCompatResources;

/* compiled from: AppCompatCheckedTextViewHelper.java */
/* renamed from: androidx.appcompat.widget.e, reason: use source file name */
/* loaded from: classes.dex */
class AppCompatCheckedTextViewHelper {

    /* renamed from: a, reason: collision with root package name */
    private final CheckedTextView f1195a;

    /* renamed from: b, reason: collision with root package name */
    private ColorStateList f1196b = null;

    /* renamed from: c, reason: collision with root package name */
    private PorterDuff.Mode f1197c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f1198d = false;

    /* renamed from: e, reason: collision with root package name */
    private boolean f1199e = false;

    /* renamed from: f, reason: collision with root package name */
    private boolean f1200f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatCheckedTextViewHelper(CheckedTextView checkedTextView) {
        this.f1195a = checkedTextView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        Drawable a10 = CheckedTextViewCompat.a(this.f1195a);
        if (a10 != null) {
            if (this.f1198d || this.f1199e) {
                Drawable mutate = DrawableCompat.l(a10).mutate();
                if (this.f1198d) {
                    DrawableCompat.i(mutate, this.f1196b);
                }
                if (this.f1199e) {
                    DrawableCompat.j(mutate, this.f1197c);
                }
                if (mutate.isStateful()) {
                    mutate.setState(this.f1195a.getDrawableState());
                }
                this.f1195a.setCheckMarkDrawable(mutate);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList b() {
        return this.f1196b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode c() {
        return this.f1197c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x003d A[Catch: all -> 0x0084, TRY_ENTER, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:8:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0060 A[Catch: all -> 0x0084, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:8:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0071 A[Catch: all -> 0x0084, TRY_LEAVE, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:8:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:2:0x001d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void d(AttributeSet attributeSet, int i10) {
        boolean z10;
        int i11;
        int i12;
        int n10;
        int n11;
        Context context = this.f1195a.getContext();
        int[] iArr = R$styleable.CheckedTextView;
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, iArr, i10, 0);
        CheckedTextView checkedTextView = this.f1195a;
        ViewCompat.j0(checkedTextView, checkedTextView.getContext(), iArr, attributeSet, w10.r(), i10, 0);
        try {
            int i13 = R$styleable.CheckedTextView_checkMarkCompat;
            if (w10.s(i13) && (n11 = w10.n(i13, 0)) != 0) {
                try {
                    CheckedTextView checkedTextView2 = this.f1195a;
                    checkedTextView2.setCheckMarkDrawable(AppCompatResources.b(checkedTextView2.getContext(), n11));
                    z10 = true;
                } catch (Resources.NotFoundException unused) {
                }
                if (!z10) {
                    int i14 = R$styleable.CheckedTextView_android_checkMark;
                    if (w10.s(i14) && (n10 = w10.n(i14, 0)) != 0) {
                        CheckedTextView checkedTextView3 = this.f1195a;
                        checkedTextView3.setCheckMarkDrawable(AppCompatResources.b(checkedTextView3.getContext(), n10));
                    }
                }
                i11 = R$styleable.CheckedTextView_checkMarkTint;
                if (w10.s(i11)) {
                    CheckedTextViewCompat.b(this.f1195a, w10.c(i11));
                }
                i12 = R$styleable.CheckedTextView_checkMarkTintMode;
                if (w10.s(i12)) {
                    CheckedTextViewCompat.c(this.f1195a, t.d(w10.k(i12, -1), null));
                }
            }
            z10 = false;
            if (!z10) {
            }
            i11 = R$styleable.CheckedTextView_checkMarkTint;
            if (w10.s(i11)) {
            }
            i12 = R$styleable.CheckedTextView_checkMarkTintMode;
            if (w10.s(i12)) {
            }
        } finally {
            w10.x();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        if (this.f1200f) {
            this.f1200f = false;
        } else {
            this.f1200f = true;
            a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(ColorStateList colorStateList) {
        this.f1196b = colorStateList;
        this.f1198d = true;
        a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(PorterDuff.Mode mode) {
        this.f1197c = mode;
        this.f1199e = true;
        a();
    }
}
