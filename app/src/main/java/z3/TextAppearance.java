package z3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.R$styleable;

/* compiled from: TextAppearance.java */
/* renamed from: z3.d, reason: use source file name */
/* loaded from: classes.dex */
public class TextAppearance {

    /* renamed from: a, reason: collision with root package name */
    public final ColorStateList f20210a;

    /* renamed from: b, reason: collision with root package name */
    public final ColorStateList f20211b;

    /* renamed from: c, reason: collision with root package name */
    public final ColorStateList f20212c;

    /* renamed from: d, reason: collision with root package name */
    public final String f20213d;

    /* renamed from: e, reason: collision with root package name */
    public final int f20214e;

    /* renamed from: f, reason: collision with root package name */
    public final int f20215f;

    /* renamed from: g, reason: collision with root package name */
    public final boolean f20216g;

    /* renamed from: h, reason: collision with root package name */
    public final float f20217h;

    /* renamed from: i, reason: collision with root package name */
    public final float f20218i;

    /* renamed from: j, reason: collision with root package name */
    public final float f20219j;

    /* renamed from: k, reason: collision with root package name */
    public final boolean f20220k;

    /* renamed from: l, reason: collision with root package name */
    public final float f20221l;

    /* renamed from: m, reason: collision with root package name */
    private ColorStateList f20222m;

    /* renamed from: n, reason: collision with root package name */
    private float f20223n;

    /* renamed from: o, reason: collision with root package name */
    private final int f20224o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f20225p = false;

    /* renamed from: q, reason: collision with root package name */
    private Typeface f20226q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TextAppearance.java */
    /* renamed from: z3.d$a */
    /* loaded from: classes.dex */
    public class a extends ResourcesCompat.e {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ TextAppearanceFontCallback f20227a;

        a(TextAppearanceFontCallback textAppearanceFontCallback) {
            this.f20227a = textAppearanceFontCallback;
        }

        @Override // androidx.core.content.res.ResourcesCompat.e
        /* renamed from: h */
        public void f(int i10) {
            TextAppearance.this.f20225p = true;
            this.f20227a.onFontRetrievalFailed(i10);
        }

        @Override // androidx.core.content.res.ResourcesCompat.e
        /* renamed from: i */
        public void g(Typeface typeface) {
            TextAppearance textAppearance = TextAppearance.this;
            textAppearance.f20226q = Typeface.create(typeface, textAppearance.f20214e);
            TextAppearance.this.f20225p = true;
            this.f20227a.onFontRetrieved(TextAppearance.this.f20226q, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TextAppearance.java */
    /* renamed from: z3.d$b */
    /* loaded from: classes.dex */
    public class b extends TextAppearanceFontCallback {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context f20229a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ TextPaint f20230b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ TextAppearanceFontCallback f20231c;

        b(Context context, TextPaint textPaint, TextAppearanceFontCallback textAppearanceFontCallback) {
            this.f20229a = context;
            this.f20230b = textPaint;
            this.f20231c = textAppearanceFontCallback;
        }

        @Override // z3.TextAppearanceFontCallback
        public void onFontRetrievalFailed(int i10) {
            this.f20231c.onFontRetrievalFailed(i10);
        }

        @Override // z3.TextAppearanceFontCallback
        public void onFontRetrieved(Typeface typeface, boolean z10) {
            TextAppearance.this.p(this.f20229a, this.f20230b, typeface);
            this.f20231c.onFontRetrieved(typeface, z10);
        }
    }

    public TextAppearance(Context context, int i10) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i10, R$styleable.TextAppearance);
        l(obtainStyledAttributes.getDimension(R$styleable.TextAppearance_android_textSize, 0.0f));
        k(MaterialResources.a(context, obtainStyledAttributes, R$styleable.TextAppearance_android_textColor));
        this.f20210a = MaterialResources.a(context, obtainStyledAttributes, R$styleable.TextAppearance_android_textColorHint);
        this.f20211b = MaterialResources.a(context, obtainStyledAttributes, R$styleable.TextAppearance_android_textColorLink);
        this.f20214e = obtainStyledAttributes.getInt(R$styleable.TextAppearance_android_textStyle, 0);
        this.f20215f = obtainStyledAttributes.getInt(R$styleable.TextAppearance_android_typeface, 1);
        int f10 = MaterialResources.f(obtainStyledAttributes, R$styleable.TextAppearance_fontFamily, R$styleable.TextAppearance_android_fontFamily);
        this.f20224o = obtainStyledAttributes.getResourceId(f10, 0);
        this.f20213d = obtainStyledAttributes.getString(f10);
        this.f20216g = obtainStyledAttributes.getBoolean(R$styleable.TextAppearance_textAllCaps, false);
        this.f20212c = MaterialResources.a(context, obtainStyledAttributes, R$styleable.TextAppearance_android_shadowColor);
        this.f20217h = obtainStyledAttributes.getFloat(R$styleable.TextAppearance_android_shadowDx, 0.0f);
        this.f20218i = obtainStyledAttributes.getFloat(R$styleable.TextAppearance_android_shadowDy, 0.0f);
        this.f20219j = obtainStyledAttributes.getFloat(R$styleable.TextAppearance_android_shadowRadius, 0.0f);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(i10, R$styleable.MaterialTextAppearance);
        int i11 = R$styleable.MaterialTextAppearance_android_letterSpacing;
        this.f20220k = obtainStyledAttributes2.hasValue(i11);
        this.f20221l = obtainStyledAttributes2.getFloat(i11, 0.0f);
        obtainStyledAttributes2.recycle();
    }

    private void d() {
        String str;
        if (this.f20226q == null && (str = this.f20213d) != null) {
            this.f20226q = Typeface.create(str, this.f20214e);
        }
        if (this.f20226q == null) {
            int i10 = this.f20215f;
            if (i10 == 1) {
                this.f20226q = Typeface.SANS_SERIF;
            } else if (i10 == 2) {
                this.f20226q = Typeface.SERIF;
            } else if (i10 != 3) {
                this.f20226q = Typeface.DEFAULT;
            } else {
                this.f20226q = Typeface.MONOSPACE;
            }
            this.f20226q = Typeface.create(this.f20226q, this.f20214e);
        }
    }

    private boolean m(Context context) {
        if (TextAppearanceConfig.a()) {
            return true;
        }
        int i10 = this.f20224o;
        return (i10 != 0 ? ResourcesCompat.c(context, i10) : null) != null;
    }

    public Typeface e() {
        d();
        return this.f20226q;
    }

    public Typeface f(Context context) {
        if (this.f20225p) {
            return this.f20226q;
        }
        if (!context.isRestricted()) {
            try {
                Typeface h10 = ResourcesCompat.h(context, this.f20224o);
                this.f20226q = h10;
                if (h10 != null) {
                    this.f20226q = Typeface.create(h10, this.f20214e);
                }
            } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
            } catch (Exception e10) {
                Log.d("TextAppearance", "Error loading font " + this.f20213d, e10);
            }
        }
        d();
        this.f20225p = true;
        return this.f20226q;
    }

    public void g(Context context, TextPaint textPaint, TextAppearanceFontCallback textAppearanceFontCallback) {
        p(context, textPaint, e());
        h(context, new b(context, textPaint, textAppearanceFontCallback));
    }

    public void h(Context context, TextAppearanceFontCallback textAppearanceFontCallback) {
        if (m(context)) {
            f(context);
        } else {
            d();
        }
        int i10 = this.f20224o;
        if (i10 == 0) {
            this.f20225p = true;
        }
        if (this.f20225p) {
            textAppearanceFontCallback.onFontRetrieved(this.f20226q, true);
            return;
        }
        try {
            ResourcesCompat.j(context, i10, new a(textAppearanceFontCallback), null);
        } catch (Resources.NotFoundException unused) {
            this.f20225p = true;
            textAppearanceFontCallback.onFontRetrievalFailed(1);
        } catch (Exception e10) {
            Log.d("TextAppearance", "Error loading font " + this.f20213d, e10);
            this.f20225p = true;
            textAppearanceFontCallback.onFontRetrievalFailed(-3);
        }
    }

    public ColorStateList i() {
        return this.f20222m;
    }

    public float j() {
        return this.f20223n;
    }

    public void k(ColorStateList colorStateList) {
        this.f20222m = colorStateList;
    }

    public void l(float f10) {
        this.f20223n = f10;
    }

    public void n(Context context, TextPaint textPaint, TextAppearanceFontCallback textAppearanceFontCallback) {
        o(context, textPaint, textAppearanceFontCallback);
        ColorStateList colorStateList = this.f20222m;
        textPaint.setColor(colorStateList != null ? colorStateList.getColorForState(textPaint.drawableState, colorStateList.getDefaultColor()) : -16777216);
        float f10 = this.f20219j;
        float f11 = this.f20217h;
        float f12 = this.f20218i;
        ColorStateList colorStateList2 = this.f20212c;
        textPaint.setShadowLayer(f10, f11, f12, colorStateList2 != null ? colorStateList2.getColorForState(textPaint.drawableState, colorStateList2.getDefaultColor()) : 0);
    }

    public void o(Context context, TextPaint textPaint, TextAppearanceFontCallback textAppearanceFontCallback) {
        if (m(context)) {
            p(context, textPaint, f(context));
        } else {
            g(context, textPaint, textAppearanceFontCallback);
        }
    }

    public void p(Context context, TextPaint textPaint, Typeface typeface) {
        Typeface a10 = TypefaceUtils.a(context, typeface);
        if (a10 != null) {
            typeface = a10;
        }
        textPaint.setTypeface(typeface);
        int i10 = this.f20214e & (~typeface.getStyle());
        textPaint.setFakeBoldText((i10 & 1) != 0);
        textPaint.setTextSkewX((i10 & 2) != 0 ? -0.25f : 0.0f);
        textPaint.setTextSize(this.f20223n);
        if (this.f20220k) {
            textPaint.setLetterSpacing(this.f20221l);
        }
    }
}
