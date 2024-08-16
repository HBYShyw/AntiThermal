package androidx.emoji2.text;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import t.MetadataItem;

/* compiled from: EmojiMetadata.java */
/* renamed from: androidx.emoji2.text.g, reason: use source file name */
/* loaded from: classes.dex */
public class EmojiMetadata {

    /* renamed from: d, reason: collision with root package name */
    private static final ThreadLocal<MetadataItem> f2615d = new ThreadLocal<>();

    /* renamed from: a, reason: collision with root package name */
    private final int f2616a;

    /* renamed from: b, reason: collision with root package name */
    private final MetadataRepo f2617b;

    /* renamed from: c, reason: collision with root package name */
    private volatile int f2618c = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmojiMetadata(MetadataRepo metadataRepo, int i10) {
        this.f2617b = metadataRepo;
        this.f2616a = i10;
    }

    private MetadataItem g() {
        ThreadLocal<MetadataItem> threadLocal = f2615d;
        MetadataItem metadataItem = threadLocal.get();
        if (metadataItem == null) {
            metadataItem = new MetadataItem();
            threadLocal.set(metadataItem);
        }
        this.f2617b.d().j(metadataItem, this.f2616a);
        return metadataItem;
    }

    public void a(Canvas canvas, float f10, float f11, Paint paint) {
        Typeface g6 = this.f2617b.g();
        Typeface typeface = paint.getTypeface();
        paint.setTypeface(g6);
        canvas.drawText(this.f2617b.c(), this.f2616a * 2, 2, f10, f11, paint);
        paint.setTypeface(typeface);
    }

    public int b(int i10) {
        return g().h(i10);
    }

    public int c() {
        return g().i();
    }

    @SuppressLint({"KotlinPropertyAccess"})
    public int d() {
        return this.f2618c;
    }

    public short e() {
        return g().k();
    }

    public int f() {
        return g().l();
    }

    public short h() {
        return g().m();
    }

    public short i() {
        return g().n();
    }

    public boolean j() {
        return g().j();
    }

    @SuppressLint({"KotlinPropertyAccess"})
    public void k(boolean z10) {
        this.f2618c = z10 ? 2 : 1;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(super.toString());
        sb2.append(", id:");
        sb2.append(Integer.toHexString(f()));
        sb2.append(", codepoints:");
        int c10 = c();
        for (int i10 = 0; i10 < c10; i10++) {
            sb2.append(Integer.toHexString(b(i10)));
            sb2.append(" ");
        }
        return sb2.toString();
    }
}
