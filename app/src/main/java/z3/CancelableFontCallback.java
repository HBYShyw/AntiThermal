package z3;

import android.graphics.Typeface;

/* compiled from: CancelableFontCallback.java */
/* renamed from: z3.a, reason: use source file name */
/* loaded from: classes.dex */
public final class CancelableFontCallback extends TextAppearanceFontCallback {

    /* renamed from: a, reason: collision with root package name */
    private final Typeface f20207a;

    /* renamed from: b, reason: collision with root package name */
    private final a f20208b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f20209c;

    /* compiled from: CancelableFontCallback.java */
    /* renamed from: z3.a$a */
    /* loaded from: classes.dex */
    public interface a {
        void apply(Typeface typeface);
    }

    public CancelableFontCallback(a aVar, Typeface typeface) {
        this.f20207a = typeface;
        this.f20208b = aVar;
    }

    private void b(Typeface typeface) {
        if (this.f20209c) {
            return;
        }
        this.f20208b.apply(typeface);
    }

    public void a() {
        this.f20209c = true;
    }

    @Override // z3.TextAppearanceFontCallback
    public void onFontRetrievalFailed(int i10) {
        b(this.f20207a);
    }

    @Override // z3.TextAppearanceFontCallback
    public void onFontRetrieved(Typeface typeface, boolean z10) {
        b(typeface);
    }
}
