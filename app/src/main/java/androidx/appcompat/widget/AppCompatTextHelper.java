package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.LocaleList;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import java.lang.ref.WeakReference;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AppCompatTextHelper.java */
/* renamed from: androidx.appcompat.widget.p, reason: use source file name */
/* loaded from: classes.dex */
public class AppCompatTextHelper {

    /* renamed from: a, reason: collision with root package name */
    private final TextView f1275a;

    /* renamed from: b, reason: collision with root package name */
    private TintInfo f1276b;

    /* renamed from: c, reason: collision with root package name */
    private TintInfo f1277c;

    /* renamed from: d, reason: collision with root package name */
    private TintInfo f1278d;

    /* renamed from: e, reason: collision with root package name */
    private TintInfo f1279e;

    /* renamed from: f, reason: collision with root package name */
    private TintInfo f1280f;

    /* renamed from: g, reason: collision with root package name */
    private TintInfo f1281g;

    /* renamed from: h, reason: collision with root package name */
    private TintInfo f1282h;

    /* renamed from: i, reason: collision with root package name */
    private final AppCompatTextViewAutoSizeHelper f1283i;

    /* renamed from: j, reason: collision with root package name */
    private int f1284j = 0;

    /* renamed from: k, reason: collision with root package name */
    private int f1285k = -1;

    /* renamed from: l, reason: collision with root package name */
    private Typeface f1286l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f1287m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCompatTextHelper.java */
    /* renamed from: androidx.appcompat.widget.p$a */
    /* loaded from: classes.dex */
    public class a extends ResourcesCompat.e {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f1288a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f1289b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ WeakReference f1290c;

        a(int i10, int i11, WeakReference weakReference) {
            this.f1288a = i10;
            this.f1289b = i11;
            this.f1290c = weakReference;
        }

        @Override // androidx.core.content.res.ResourcesCompat.e
        /* renamed from: h */
        public void f(int i10) {
        }

        @Override // androidx.core.content.res.ResourcesCompat.e
        /* renamed from: i */
        public void g(Typeface typeface) {
            int i10 = this.f1288a;
            if (i10 != -1) {
                typeface = f.a(typeface, i10, (this.f1289b & 2) != 0);
            }
            AppCompatTextHelper.this.n(this.f1290c, typeface);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCompatTextHelper.java */
    /* renamed from: androidx.appcompat.widget.p$b */
    /* loaded from: classes.dex */
    public class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ TextView f1292e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Typeface f1293f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ int f1294g;

        b(TextView textView, Typeface typeface, int i10) {
            this.f1292e = textView;
            this.f1293f = typeface;
            this.f1294g = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f1292e.setTypeface(this.f1293f, this.f1294g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCompatTextHelper.java */
    /* renamed from: androidx.appcompat.widget.p$c */
    /* loaded from: classes.dex */
    public static class c {
        static Drawable[] a(TextView textView) {
            return textView.getCompoundDrawablesRelative();
        }

        static void b(TextView textView, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }

        static void c(TextView textView, Locale locale) {
            textView.setTextLocale(locale);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCompatTextHelper.java */
    /* renamed from: androidx.appcompat.widget.p$d */
    /* loaded from: classes.dex */
    public static class d {
        static LocaleList a(String str) {
            return LocaleList.forLanguageTags(str);
        }

        static void b(TextView textView, LocaleList localeList) {
            textView.setTextLocales(localeList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCompatTextHelper.java */
    /* renamed from: androidx.appcompat.widget.p$e */
    /* loaded from: classes.dex */
    public static class e {
        static int a(TextView textView) {
            return textView.getAutoSizeStepGranularity();
        }

        static void b(TextView textView, int i10, int i11, int i12, int i13) {
            textView.setAutoSizeTextTypeUniformWithConfiguration(i10, i11, i12, i13);
        }

        static void c(TextView textView, int[] iArr, int i10) {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i10);
        }

        static boolean d(TextView textView, String str) {
            return textView.setFontVariationSettings(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppCompatTextHelper.java */
    /* renamed from: androidx.appcompat.widget.p$f */
    /* loaded from: classes.dex */
    public static class f {
        static Typeface a(Typeface typeface, int i10, boolean z10) {
            return Typeface.create(typeface, i10, z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatTextHelper(TextView textView) {
        this.f1275a = textView;
        this.f1283i = new AppCompatTextViewAutoSizeHelper(textView);
    }

    private void B(int i10, float f10) {
        this.f1283i.t(i10, f10);
    }

    private void C(Context context, TintTypedArray tintTypedArray) {
        String o10;
        this.f1284j = tintTypedArray.k(R$styleable.TextAppearance_android_textStyle, this.f1284j);
        int k10 = tintTypedArray.k(R$styleable.TextAppearance_android_textFontWeight, -1);
        this.f1285k = k10;
        if (k10 != -1) {
            this.f1284j = (this.f1284j & 2) | 0;
        }
        int i10 = R$styleable.TextAppearance_android_fontFamily;
        if (!tintTypedArray.s(i10) && !tintTypedArray.s(R$styleable.TextAppearance_fontFamily)) {
            int i11 = R$styleable.TextAppearance_android_typeface;
            if (tintTypedArray.s(i11)) {
                this.f1287m = false;
                int k11 = tintTypedArray.k(i11, 1);
                if (k11 == 1) {
                    this.f1286l = Typeface.SANS_SERIF;
                    return;
                } else if (k11 == 2) {
                    this.f1286l = Typeface.SERIF;
                    return;
                } else {
                    if (k11 != 3) {
                        return;
                    }
                    this.f1286l = Typeface.MONOSPACE;
                    return;
                }
            }
            return;
        }
        this.f1286l = null;
        int i12 = R$styleable.TextAppearance_fontFamily;
        if (tintTypedArray.s(i12)) {
            i10 = i12;
        }
        int i13 = this.f1285k;
        int i14 = this.f1284j;
        if (!context.isRestricted()) {
            try {
                Typeface j10 = tintTypedArray.j(i10, this.f1284j, new a(i13, i14, new WeakReference(this.f1275a)));
                if (j10 != null) {
                    if (this.f1285k != -1) {
                        this.f1286l = f.a(Typeface.create(j10, 0), this.f1285k, (this.f1284j & 2) != 0);
                    } else {
                        this.f1286l = j10;
                    }
                }
                this.f1287m = this.f1286l == null;
            } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
            }
        }
        if (this.f1286l != null || (o10 = tintTypedArray.o(i10)) == null) {
            return;
        }
        if (this.f1285k != -1) {
            this.f1286l = f.a(Typeface.create(o10, 0), this.f1285k, (this.f1284j & 2) != 0);
        } else {
            this.f1286l = Typeface.create(o10, this.f1284j);
        }
    }

    private void a(Drawable drawable, TintInfo tintInfo) {
        if (drawable == null || tintInfo == null) {
            return;
        }
        AppCompatDrawableManager.i(drawable, tintInfo, this.f1275a.getDrawableState());
    }

    private static TintInfo d(Context context, AppCompatDrawableManager appCompatDrawableManager, int i10) {
        ColorStateList f10 = appCompatDrawableManager.f(context, i10);
        if (f10 == null) {
            return null;
        }
        TintInfo tintInfo = new TintInfo();
        tintInfo.f1214d = true;
        tintInfo.f1211a = f10;
        return tintInfo;
    }

    private void y(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5, Drawable drawable6) {
        if (drawable5 != null || drawable6 != null) {
            Drawable[] a10 = c.a(this.f1275a);
            TextView textView = this.f1275a;
            if (drawable5 == null) {
                drawable5 = a10[0];
            }
            if (drawable2 == null) {
                drawable2 = a10[1];
            }
            if (drawable6 == null) {
                drawable6 = a10[2];
            }
            if (drawable4 == null) {
                drawable4 = a10[3];
            }
            c.b(textView, drawable5, drawable2, drawable6, drawable4);
            return;
        }
        if (drawable == null && drawable2 == null && drawable3 == null && drawable4 == null) {
            return;
        }
        Drawable[] a11 = c.a(this.f1275a);
        if (a11[0] == null && a11[2] == null) {
            Drawable[] compoundDrawables = this.f1275a.getCompoundDrawables();
            TextView textView2 = this.f1275a;
            if (drawable == null) {
                drawable = compoundDrawables[0];
            }
            if (drawable2 == null) {
                drawable2 = compoundDrawables[1];
            }
            if (drawable3 == null) {
                drawable3 = compoundDrawables[2];
            }
            if (drawable4 == null) {
                drawable4 = compoundDrawables[3];
            }
            textView2.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
            return;
        }
        TextView textView3 = this.f1275a;
        Drawable drawable7 = a11[0];
        if (drawable2 == null) {
            drawable2 = a11[1];
        }
        Drawable drawable8 = a11[2];
        if (drawable4 == null) {
            drawable4 = a11[3];
        }
        c.b(textView3, drawable7, drawable2, drawable8, drawable4);
    }

    private void z() {
        TintInfo tintInfo = this.f1282h;
        this.f1276b = tintInfo;
        this.f1277c = tintInfo;
        this.f1278d = tintInfo;
        this.f1279e = tintInfo;
        this.f1280f = tintInfo;
        this.f1281g = tintInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(int i10, float f10) {
        if (n0.f1273b || l()) {
            return;
        }
        B(i10, f10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        if (this.f1276b != null || this.f1277c != null || this.f1278d != null || this.f1279e != null) {
            Drawable[] compoundDrawables = this.f1275a.getCompoundDrawables();
            a(compoundDrawables[0], this.f1276b);
            a(compoundDrawables[1], this.f1277c);
            a(compoundDrawables[2], this.f1278d);
            a(compoundDrawables[3], this.f1279e);
        }
        if (this.f1280f == null && this.f1281g == null) {
            return;
        }
        Drawable[] a10 = c.a(this.f1275a);
        a(a10[0], this.f1280f);
        a(a10[2], this.f1281g);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        this.f1283i.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int e() {
        return this.f1283i.f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int f() {
        return this.f1283i.g();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g() {
        return this.f1283i.h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] h() {
        return this.f1283i.i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        return this.f1283i.j();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList j() {
        TintInfo tintInfo = this.f1282h;
        if (tintInfo != null) {
            return tintInfo.f1211a;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode k() {
        TintInfo tintInfo = this.f1282h;
        if (tintInfo != null) {
            return tintInfo.f1212b;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l() {
        return this.f1283i.n();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:106:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0203  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x01b8  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x013d  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0157  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01b2  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0222  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0246  */
    @SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void m(AttributeSet attributeSet, int i10) {
        boolean z10;
        boolean z11;
        String str;
        String str2;
        int i11;
        int i12;
        int i13;
        Typeface typeface;
        TintTypedArray v7;
        int i14;
        int i15;
        int f10;
        int f11;
        int f12;
        int[] i16;
        Context context = this.f1275a.getContext();
        AppCompatDrawableManager b10 = AppCompatDrawableManager.b();
        int[] iArr = R$styleable.AppCompatTextHelper;
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, iArr, i10, 0);
        TextView textView = this.f1275a;
        ViewCompat.j0(textView, textView.getContext(), iArr, attributeSet, w10.r(), i10, 0);
        int n10 = w10.n(R$styleable.AppCompatTextHelper_android_textAppearance, -1);
        int i17 = R$styleable.AppCompatTextHelper_android_drawableLeft;
        if (w10.s(i17)) {
            this.f1276b = d(context, b10, w10.n(i17, 0));
        }
        int i18 = R$styleable.AppCompatTextHelper_android_drawableTop;
        if (w10.s(i18)) {
            this.f1277c = d(context, b10, w10.n(i18, 0));
        }
        int i19 = R$styleable.AppCompatTextHelper_android_drawableRight;
        if (w10.s(i19)) {
            this.f1278d = d(context, b10, w10.n(i19, 0));
        }
        int i20 = R$styleable.AppCompatTextHelper_android_drawableBottom;
        if (w10.s(i20)) {
            this.f1279e = d(context, b10, w10.n(i20, 0));
        }
        int i21 = R$styleable.AppCompatTextHelper_android_drawableStart;
        if (w10.s(i21)) {
            this.f1280f = d(context, b10, w10.n(i21, 0));
        }
        int i22 = R$styleable.AppCompatTextHelper_android_drawableEnd;
        if (w10.s(i22)) {
            this.f1281g = d(context, b10, w10.n(i22, 0));
        }
        w10.x();
        boolean z12 = this.f1275a.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean z13 = true;
        if (n10 != -1) {
            TintTypedArray u7 = TintTypedArray.u(context, n10, R$styleable.TextAppearance);
            if (!z12) {
                int i23 = R$styleable.TextAppearance_textAllCaps;
                if (u7.s(i23)) {
                    z10 = u7.a(i23, false);
                    z11 = true;
                    C(context, u7);
                    int i24 = R$styleable.TextAppearance_textLocale;
                    str = !u7.s(i24) ? u7.o(i24) : null;
                    int i25 = R$styleable.TextAppearance_fontVariationSettings;
                    str2 = !u7.s(i25) ? u7.o(i25) : null;
                    u7.x();
                }
            }
            z10 = false;
            z11 = false;
            C(context, u7);
            int i242 = R$styleable.TextAppearance_textLocale;
            if (!u7.s(i242)) {
            }
            int i252 = R$styleable.TextAppearance_fontVariationSettings;
            if (!u7.s(i252)) {
            }
            u7.x();
        } else {
            z10 = false;
            z11 = false;
            str = null;
            str2 = null;
        }
        TintTypedArray w11 = TintTypedArray.w(context, attributeSet, R$styleable.TextAppearance, i10, 0);
        if (!z12) {
            int i26 = R$styleable.TextAppearance_textAllCaps;
            if (w11.s(i26)) {
                z10 = w11.a(i26, false);
                i11 = R$styleable.TextAppearance_textLocale;
                if (w11.s(i11)) {
                    str = w11.o(i11);
                }
                i12 = R$styleable.TextAppearance_fontVariationSettings;
                if (w11.s(i12)) {
                    str2 = w11.o(i12);
                }
                i13 = R$styleable.TextAppearance_android_textSize;
                if (w11.s(i13) && w11.f(i13, -1) == 0) {
                    this.f1275a.setTextSize(0, 0.0f);
                }
                C(context, w11);
                w11.x();
                if (!z12 && z13) {
                    s(z10);
                }
                typeface = this.f1286l;
                if (typeface != null) {
                    if (this.f1285k == -1) {
                        this.f1275a.setTypeface(typeface, this.f1284j);
                    } else {
                        this.f1275a.setTypeface(typeface);
                    }
                }
                if (str2 != null) {
                    e.d(this.f1275a, str2);
                }
                if (str != null) {
                    d.b(this.f1275a, d.a(str));
                }
                this.f1283i.o(attributeSet, i10);
                if (n0.f1273b && this.f1283i.j() != 0) {
                    i16 = this.f1283i.i();
                    if (i16.length > 0) {
                        if (e.a(this.f1275a) != -1.0f) {
                            e.b(this.f1275a, this.f1283i.g(), this.f1283i.f(), this.f1283i.h(), 0);
                        } else {
                            e.c(this.f1275a, i16, 0);
                        }
                    }
                }
                v7 = TintTypedArray.v(context, attributeSet, R$styleable.AppCompatTextView);
                int n11 = v7.n(R$styleable.AppCompatTextView_drawableLeftCompat, -1);
                Drawable c10 = n11 == -1 ? b10.c(context, n11) : null;
                int n12 = v7.n(R$styleable.AppCompatTextView_drawableTopCompat, -1);
                Drawable c11 = n12 == -1 ? b10.c(context, n12) : null;
                int n13 = v7.n(R$styleable.AppCompatTextView_drawableRightCompat, -1);
                Drawable c12 = n13 == -1 ? b10.c(context, n13) : null;
                int n14 = v7.n(R$styleable.AppCompatTextView_drawableBottomCompat, -1);
                Drawable c13 = n14 == -1 ? b10.c(context, n14) : null;
                int n15 = v7.n(R$styleable.AppCompatTextView_drawableStartCompat, -1);
                Drawable c14 = n15 == -1 ? b10.c(context, n15) : null;
                int n16 = v7.n(R$styleable.AppCompatTextView_drawableEndCompat, -1);
                y(c10, c11, c12, c13, c14, n16 == -1 ? b10.c(context, n16) : null);
                i14 = R$styleable.AppCompatTextView_drawableTint;
                if (v7.s(i14)) {
                    TextViewCompat.g(this.f1275a, v7.c(i14));
                }
                i15 = R$styleable.AppCompatTextView_drawableTintMode;
                if (v7.s(i15)) {
                    TextViewCompat.h(this.f1275a, t.d(v7.k(i15, -1), null));
                }
                f10 = v7.f(R$styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
                f11 = v7.f(R$styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
                f12 = v7.f(R$styleable.AppCompatTextView_lineHeight, -1);
                v7.x();
                if (f10 != -1) {
                    TextViewCompat.j(this.f1275a, f10);
                }
                if (f11 != -1) {
                    TextViewCompat.k(this.f1275a, f11);
                }
                if (f12 == -1) {
                    TextViewCompat.l(this.f1275a, f12);
                    return;
                }
                return;
            }
        }
        z13 = z11;
        i11 = R$styleable.TextAppearance_textLocale;
        if (w11.s(i11)) {
        }
        i12 = R$styleable.TextAppearance_fontVariationSettings;
        if (w11.s(i12)) {
        }
        i13 = R$styleable.TextAppearance_android_textSize;
        if (w11.s(i13)) {
            this.f1275a.setTextSize(0, 0.0f);
        }
        C(context, w11);
        w11.x();
        if (!z12) {
            s(z10);
        }
        typeface = this.f1286l;
        if (typeface != null) {
        }
        if (str2 != null) {
        }
        if (str != null) {
        }
        this.f1283i.o(attributeSet, i10);
        if (n0.f1273b) {
            i16 = this.f1283i.i();
            if (i16.length > 0) {
            }
        }
        v7 = TintTypedArray.v(context, attributeSet, R$styleable.AppCompatTextView);
        int n112 = v7.n(R$styleable.AppCompatTextView_drawableLeftCompat, -1);
        if (n112 == -1) {
        }
        int n122 = v7.n(R$styleable.AppCompatTextView_drawableTopCompat, -1);
        if (n122 == -1) {
        }
        int n132 = v7.n(R$styleable.AppCompatTextView_drawableRightCompat, -1);
        if (n132 == -1) {
        }
        int n142 = v7.n(R$styleable.AppCompatTextView_drawableBottomCompat, -1);
        if (n142 == -1) {
        }
        int n152 = v7.n(R$styleable.AppCompatTextView_drawableStartCompat, -1);
        if (n152 == -1) {
        }
        int n162 = v7.n(R$styleable.AppCompatTextView_drawableEndCompat, -1);
        y(c10, c11, c12, c13, c14, n162 == -1 ? b10.c(context, n162) : null);
        i14 = R$styleable.AppCompatTextView_drawableTint;
        if (v7.s(i14)) {
        }
        i15 = R$styleable.AppCompatTextView_drawableTintMode;
        if (v7.s(i15)) {
        }
        f10 = v7.f(R$styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
        f11 = v7.f(R$styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
        f12 = v7.f(R$styleable.AppCompatTextView_lineHeight, -1);
        v7.x();
        if (f10 != -1) {
        }
        if (f11 != -1) {
        }
        if (f12 == -1) {
        }
    }

    void n(WeakReference<TextView> weakReference, Typeface typeface) {
        if (this.f1287m) {
            this.f1286l = typeface;
            TextView textView = weakReference.get();
            if (textView != null) {
                if (ViewCompat.P(textView)) {
                    textView.post(new b(textView, typeface, this.f1284j));
                } else {
                    textView.setTypeface(typeface, this.f1284j);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(boolean z10, int i10, int i11, int i12, int i13) {
        if (n0.f1273b) {
            return;
        }
        c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p() {
        b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(Context context, int i10) {
        String o10;
        TintTypedArray u7 = TintTypedArray.u(context, i10, R$styleable.TextAppearance);
        int i11 = R$styleable.TextAppearance_textAllCaps;
        if (u7.s(i11)) {
            s(u7.a(i11, false));
        }
        int i12 = R$styleable.TextAppearance_android_textSize;
        if (u7.s(i12) && u7.f(i12, -1) == 0) {
            this.f1275a.setTextSize(0, 0.0f);
        }
        C(context, u7);
        int i13 = R$styleable.TextAppearance_fontVariationSettings;
        if (u7.s(i13) && (o10 = u7.o(i13)) != null) {
            e.d(this.f1275a, o10);
        }
        u7.x();
        Typeface typeface = this.f1286l;
        if (typeface != null) {
            this.f1275a.setTypeface(typeface, this.f1284j);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(TextView textView, InputConnection inputConnection, EditorInfo editorInfo) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(boolean z10) {
        this.f1275a.setAllCaps(z10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(int i10, int i11, int i12, int i13) {
        this.f1283i.p(i10, i11, i12, i13);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(int[] iArr, int i10) {
        this.f1283i.q(iArr, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(int i10) {
        this.f1283i.r(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(ColorStateList colorStateList) {
        if (this.f1282h == null) {
            this.f1282h = new TintInfo();
        }
        TintInfo tintInfo = this.f1282h;
        tintInfo.f1211a = colorStateList;
        tintInfo.f1214d = colorStateList != null;
        z();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(PorterDuff.Mode mode) {
        if (this.f1282h == null) {
            this.f1282h = new TintInfo();
        }
        TintInfo tintInfo = this.f1282h;
        tintInfo.f1212b = mode;
        tintInfo.f1213c = mode != null;
        z();
    }
}
