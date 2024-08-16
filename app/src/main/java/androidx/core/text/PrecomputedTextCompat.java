package androidx.core.text;

import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.TextDirectionHeuristic;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import androidx.core.util.ObjectsCompat;

/* compiled from: PrecomputedTextCompat.java */
/* renamed from: androidx.core.text.c, reason: use source file name */
/* loaded from: classes.dex */
public class PrecomputedTextCompat implements Spannable {

    /* renamed from: g, reason: collision with root package name */
    private static final Object f2284g = new Object();

    /* renamed from: e, reason: collision with root package name */
    private final Spannable f2285e;

    /* renamed from: f, reason: collision with root package name */
    private final PrecomputedText f2286f;

    /* compiled from: PrecomputedTextCompat.java */
    /* renamed from: androidx.core.text.c$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final TextPaint f2287a;

        /* renamed from: b, reason: collision with root package name */
        private final TextDirectionHeuristic f2288b;

        /* renamed from: c, reason: collision with root package name */
        private final int f2289c;

        /* renamed from: d, reason: collision with root package name */
        private final int f2290d;

        /* renamed from: e, reason: collision with root package name */
        final PrecomputedText.Params f2291e;

        public a(PrecomputedText.Params params) {
            this.f2287a = params.getTextPaint();
            this.f2288b = params.getTextDirection();
            this.f2289c = params.getBreakStrategy();
            this.f2290d = params.getHyphenationFrequency();
            this.f2291e = params;
        }

        public boolean a(a aVar) {
            if (this.f2289c == aVar.b() && this.f2290d == aVar.c() && this.f2287a.getTextSize() == aVar.e().getTextSize() && this.f2287a.getTextScaleX() == aVar.e().getTextScaleX() && this.f2287a.getTextSkewX() == aVar.e().getTextSkewX() && this.f2287a.getLetterSpacing() == aVar.e().getLetterSpacing() && TextUtils.equals(this.f2287a.getFontFeatureSettings(), aVar.e().getFontFeatureSettings()) && this.f2287a.getFlags() == aVar.e().getFlags() && this.f2287a.getTextLocales().equals(aVar.e().getTextLocales())) {
                return this.f2287a.getTypeface() == null ? aVar.e().getTypeface() == null : this.f2287a.getTypeface().equals(aVar.e().getTypeface());
            }
            return false;
        }

        public int b() {
            return this.f2289c;
        }

        public int c() {
            return this.f2290d;
        }

        public TextDirectionHeuristic d() {
            return this.f2288b;
        }

        public TextPaint e() {
            return this.f2287a;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return a(aVar) && this.f2288b == aVar.d();
        }

        public int hashCode() {
            return ObjectsCompat.b(Float.valueOf(this.f2287a.getTextSize()), Float.valueOf(this.f2287a.getTextScaleX()), Float.valueOf(this.f2287a.getTextSkewX()), Float.valueOf(this.f2287a.getLetterSpacing()), Integer.valueOf(this.f2287a.getFlags()), this.f2287a.getTextLocales(), this.f2287a.getTypeface(), Boolean.valueOf(this.f2287a.isElegantTextHeight()), this.f2288b, Integer.valueOf(this.f2289c), Integer.valueOf(this.f2290d));
        }

        public String toString() {
            StringBuilder sb2 = new StringBuilder("{");
            sb2.append("textSize=" + this.f2287a.getTextSize());
            sb2.append(", textScaleX=" + this.f2287a.getTextScaleX());
            sb2.append(", textSkewX=" + this.f2287a.getTextSkewX());
            sb2.append(", letterSpacing=" + this.f2287a.getLetterSpacing());
            sb2.append(", elegantTextHeight=" + this.f2287a.isElegantTextHeight());
            sb2.append(", textLocale=" + this.f2287a.getTextLocales());
            sb2.append(", typeface=" + this.f2287a.getTypeface());
            sb2.append(", variationSettings=" + this.f2287a.getFontVariationSettings());
            sb2.append(", textDir=" + this.f2288b);
            sb2.append(", breakStrategy=" + this.f2289c);
            sb2.append(", hyphenationFrequency=" + this.f2290d);
            sb2.append("}");
            return sb2.toString();
        }
    }

    public PrecomputedText a() {
        Spannable spannable = this.f2285e;
        if (spannable instanceof PrecomputedText) {
            return (PrecomputedText) spannable;
        }
        return null;
    }

    @Override // java.lang.CharSequence
    public char charAt(int i10) {
        return this.f2285e.charAt(i10);
    }

    @Override // android.text.Spanned
    public int getSpanEnd(Object obj) {
        return this.f2285e.getSpanEnd(obj);
    }

    @Override // android.text.Spanned
    public int getSpanFlags(Object obj) {
        return this.f2285e.getSpanFlags(obj);
    }

    @Override // android.text.Spanned
    public int getSpanStart(Object obj) {
        return this.f2285e.getSpanStart(obj);
    }

    @Override // android.text.Spanned
    public <T> T[] getSpans(int i10, int i11, Class<T> cls) {
        return (T[]) this.f2286f.getSpans(i10, i11, cls);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.f2285e.length();
    }

    @Override // android.text.Spanned
    public int nextSpanTransition(int i10, int i11, Class cls) {
        return this.f2285e.nextSpanTransition(i10, i11, cls);
    }

    @Override // android.text.Spannable
    public void removeSpan(Object obj) {
        if (!(obj instanceof MetricAffectingSpan)) {
            this.f2286f.removeSpan(obj);
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
    }

    @Override // android.text.Spannable
    public void setSpan(Object obj, int i10, int i11, int i12) {
        if (!(obj instanceof MetricAffectingSpan)) {
            this.f2286f.setSpan(obj, i10, i11, i12);
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int i10, int i11) {
        return this.f2285e.subSequence(i10, i11);
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.f2285e.toString();
    }
}
