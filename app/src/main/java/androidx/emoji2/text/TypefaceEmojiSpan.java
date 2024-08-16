package androidx.emoji2.text;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

/* compiled from: TypefaceEmojiSpan.java */
/* renamed from: androidx.emoji2.text.o, reason: use source file name */
/* loaded from: classes.dex */
public final class TypefaceEmojiSpan extends EmojiSpan {

    /* renamed from: j, reason: collision with root package name */
    private static Paint f2662j;

    public TypefaceEmojiSpan(EmojiMetadata emojiMetadata) {
        super(emojiMetadata);
    }

    private static Paint c() {
        if (f2662j == null) {
            TextPaint textPaint = new TextPaint();
            f2662j = textPaint;
            textPaint.setColor(EmojiCompat.b().c());
            f2662j.setStyle(Paint.Style.FILL);
        }
        return f2662j;
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, @SuppressLint({"UnknownNullness"}) CharSequence charSequence, int i10, int i11, float f10, int i12, int i13, int i14, Paint paint) {
        if (EmojiCompat.b().i()) {
            canvas.drawRect(f10, i12, f10 + b(), i14, c());
        }
        a().a(canvas, f10, i13, paint);
    }
}
