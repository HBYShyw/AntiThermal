package androidx.emoji2.text;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;
import androidx.core.util.Preconditions;

/* compiled from: EmojiSpan.java */
/* renamed from: androidx.emoji2.text.i, reason: use source file name */
/* loaded from: classes.dex */
public abstract class EmojiSpan extends ReplacementSpan {

    /* renamed from: f, reason: collision with root package name */
    private final EmojiMetadata f2633f;

    /* renamed from: e, reason: collision with root package name */
    private final Paint.FontMetricsInt f2632e = new Paint.FontMetricsInt();

    /* renamed from: g, reason: collision with root package name */
    private short f2634g = -1;

    /* renamed from: h, reason: collision with root package name */
    private short f2635h = -1;

    /* renamed from: i, reason: collision with root package name */
    private float f2636i = 1.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmojiSpan(EmojiMetadata emojiMetadata) {
        Preconditions.e(emojiMetadata, "metadata cannot be null");
        this.f2633f = emojiMetadata;
    }

    public final EmojiMetadata a() {
        return this.f2633f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int b() {
        return this.f2634g;
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, @SuppressLint({"UnknownNullness"}) CharSequence charSequence, int i10, int i11, Paint.FontMetricsInt fontMetricsInt) {
        paint.getFontMetricsInt(this.f2632e);
        Paint.FontMetricsInt fontMetricsInt2 = this.f2632e;
        this.f2636i = (Math.abs(fontMetricsInt2.descent - fontMetricsInt2.ascent) * 1.0f) / this.f2633f.e();
        this.f2635h = (short) (this.f2633f.e() * this.f2636i);
        short i12 = (short) (this.f2633f.i() * this.f2636i);
        this.f2634g = i12;
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fontMetricsInt3 = this.f2632e;
            fontMetricsInt.ascent = fontMetricsInt3.ascent;
            fontMetricsInt.descent = fontMetricsInt3.descent;
            fontMetricsInt.top = fontMetricsInt3.top;
            fontMetricsInt.bottom = fontMetricsInt3.bottom;
        }
        return i12;
    }
}
