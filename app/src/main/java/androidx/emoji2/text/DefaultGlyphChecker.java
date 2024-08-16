package androidx.emoji2.text;

import android.text.TextPaint;
import androidx.core.graphics.PaintCompat;
import androidx.emoji2.text.EmojiCompat;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DefaultGlyphChecker.java */
/* renamed from: androidx.emoji2.text.d, reason: use source file name */
/* loaded from: classes.dex */
public class DefaultGlyphChecker implements EmojiCompat.d {

    /* renamed from: b, reason: collision with root package name */
    private static final ThreadLocal<StringBuilder> f2578b = new ThreadLocal<>();

    /* renamed from: a, reason: collision with root package name */
    private final TextPaint f2579a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultGlyphChecker() {
        TextPaint textPaint = new TextPaint();
        this.f2579a = textPaint;
        textPaint.setTextSize(10.0f);
    }

    private static StringBuilder b() {
        ThreadLocal<StringBuilder> threadLocal = f2578b;
        if (threadLocal.get() == null) {
            threadLocal.set(new StringBuilder());
        }
        return threadLocal.get();
    }

    @Override // androidx.emoji2.text.EmojiCompat.d
    public boolean a(CharSequence charSequence, int i10, int i11, int i12) {
        StringBuilder b10 = b();
        b10.setLength(0);
        while (i10 < i11) {
            b10.append(charSequence.charAt(i10));
            i10++;
        }
        return PaintCompat.a(this.f2579a, b10.toString());
    }
}
