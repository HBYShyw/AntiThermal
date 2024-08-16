package androidx.emoji2.text;

import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableString;
import androidx.core.text.PrecomputedTextCompat;
import java.util.stream.IntStream;

/* compiled from: UnprecomputeTextOnModificationSpannable.java */
/* renamed from: androidx.emoji2.text.p, reason: use source file name */
/* loaded from: classes.dex */
class UnprecomputeTextOnModificationSpannable implements Spannable {

    /* renamed from: e, reason: collision with root package name */
    private boolean f2663e = false;

    /* renamed from: f, reason: collision with root package name */
    private Spannable f2664f;

    /* compiled from: UnprecomputeTextOnModificationSpannable.java */
    /* renamed from: androidx.emoji2.text.p$a */
    /* loaded from: classes.dex */
    private static class a {
        static IntStream a(CharSequence charSequence) {
            return charSequence.chars();
        }

        static IntStream b(CharSequence charSequence) {
            return charSequence.codePoints();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: UnprecomputeTextOnModificationSpannable.java */
    /* renamed from: androidx.emoji2.text.p$b */
    /* loaded from: classes.dex */
    public static class b {
        b() {
        }

        boolean a(CharSequence charSequence) {
            throw null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: UnprecomputeTextOnModificationSpannable.java */
    /* renamed from: androidx.emoji2.text.p$c */
    /* loaded from: classes.dex */
    public static class c extends b {
        c() {
        }

        @Override // androidx.emoji2.text.UnprecomputeTextOnModificationSpannable.b
        boolean a(CharSequence charSequence) {
            return (charSequence instanceof PrecomputedText) || (charSequence instanceof PrecomputedTextCompat);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnprecomputeTextOnModificationSpannable(Spannable spannable) {
        this.f2664f = spannable;
    }

    private void a() {
        Spannable spannable = this.f2664f;
        if (!this.f2663e && c().a(spannable)) {
            this.f2664f = new SpannableString(spannable);
        }
        this.f2663e = true;
    }

    static b c() {
        return new c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Spannable b() {
        return this.f2664f;
    }

    @Override // java.lang.CharSequence
    public char charAt(int i10) {
        return this.f2664f.charAt(i10);
    }

    @Override // java.lang.CharSequence
    public IntStream chars() {
        return a.a(this.f2664f);
    }

    @Override // java.lang.CharSequence
    public IntStream codePoints() {
        return a.b(this.f2664f);
    }

    @Override // android.text.Spanned
    public int getSpanEnd(Object obj) {
        return this.f2664f.getSpanEnd(obj);
    }

    @Override // android.text.Spanned
    public int getSpanFlags(Object obj) {
        return this.f2664f.getSpanFlags(obj);
    }

    @Override // android.text.Spanned
    public int getSpanStart(Object obj) {
        return this.f2664f.getSpanStart(obj);
    }

    @Override // android.text.Spanned
    public <T> T[] getSpans(int i10, int i11, Class<T> cls) {
        return (T[]) this.f2664f.getSpans(i10, i11, cls);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.f2664f.length();
    }

    @Override // android.text.Spanned
    public int nextSpanTransition(int i10, int i11, Class cls) {
        return this.f2664f.nextSpanTransition(i10, i11, cls);
    }

    @Override // android.text.Spannable
    public void removeSpan(Object obj) {
        a();
        this.f2664f.removeSpan(obj);
    }

    @Override // android.text.Spannable
    public void setSpan(Object obj, int i10, int i11, int i12) {
        a();
        this.f2664f.setSpan(obj, i10, i11, i12);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int i10, int i11) {
        return this.f2664f.subSequence(i10, i11);
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.f2664f.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnprecomputeTextOnModificationSpannable(CharSequence charSequence) {
        this.f2664f = new SpannableString(charSequence);
    }
}
