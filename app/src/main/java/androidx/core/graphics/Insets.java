package androidx.core.graphics;

import android.graphics.Rect;

/* compiled from: Insets.java */
/* renamed from: androidx.core.graphics.b, reason: use source file name */
/* loaded from: classes.dex */
public final class Insets {

    /* renamed from: e, reason: collision with root package name */
    public static final Insets f2184e = new Insets(0, 0, 0, 0);

    /* renamed from: a, reason: collision with root package name */
    public final int f2185a;

    /* renamed from: b, reason: collision with root package name */
    public final int f2186b;

    /* renamed from: c, reason: collision with root package name */
    public final int f2187c;

    /* renamed from: d, reason: collision with root package name */
    public final int f2188d;

    /* compiled from: Insets.java */
    /* renamed from: androidx.core.graphics.b$a */
    /* loaded from: classes.dex */
    static class a {
        static android.graphics.Insets a(int i10, int i11, int i12, int i13) {
            return android.graphics.Insets.of(i10, i11, i12, i13);
        }
    }

    private Insets(int i10, int i11, int i12, int i13) {
        this.f2185a = i10;
        this.f2186b = i11;
        this.f2187c = i12;
        this.f2188d = i13;
    }

    public static Insets a(Insets insets, Insets insets2) {
        return b(Math.max(insets.f2185a, insets2.f2185a), Math.max(insets.f2186b, insets2.f2186b), Math.max(insets.f2187c, insets2.f2187c), Math.max(insets.f2188d, insets2.f2188d));
    }

    public static Insets b(int i10, int i11, int i12, int i13) {
        if (i10 == 0 && i11 == 0 && i12 == 0 && i13 == 0) {
            return f2184e;
        }
        return new Insets(i10, i11, i12, i13);
    }

    public static Insets c(Rect rect) {
        return b(rect.left, rect.top, rect.right, rect.bottom);
    }

    public static Insets d(android.graphics.Insets insets) {
        return b(insets.left, insets.top, insets.right, insets.bottom);
    }

    public android.graphics.Insets e() {
        return a.a(this.f2185a, this.f2186b, this.f2187c, this.f2188d);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Insets.class != obj.getClass()) {
            return false;
        }
        Insets insets = (Insets) obj;
        return this.f2188d == insets.f2188d && this.f2185a == insets.f2185a && this.f2187c == insets.f2187c && this.f2186b == insets.f2186b;
    }

    public int hashCode() {
        return (((((this.f2185a * 31) + this.f2186b) * 31) + this.f2187c) * 31) + this.f2188d;
    }

    public String toString() {
        return "Insets{left=" + this.f2185a + ", top=" + this.f2186b + ", right=" + this.f2187c + ", bottom=" + this.f2188d + '}';
    }
}
