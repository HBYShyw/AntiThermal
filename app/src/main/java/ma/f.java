package ma;

import fb.PrimitiveRanges;
import za.DefaultConstructorMarker;

/* compiled from: KotlinVersion.kt */
/* loaded from: classes2.dex */
public final class f implements Comparable<f> {

    /* renamed from: i, reason: collision with root package name */
    public static final a f15167i = new a(null);

    /* renamed from: j, reason: collision with root package name */
    public static final f f15168j = g.a();

    /* renamed from: e, reason: collision with root package name */
    private final int f15169e;

    /* renamed from: f, reason: collision with root package name */
    private final int f15170f;

    /* renamed from: g, reason: collision with root package name */
    private final int f15171g;

    /* renamed from: h, reason: collision with root package name */
    private final int f15172h;

    /* compiled from: KotlinVersion.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public f(int i10, int i11, int i12) {
        this.f15169e = i10;
        this.f15170f = i11;
        this.f15171g = i12;
        this.f15172h = b(i10, i11, i12);
    }

    private final int b(int i10, int i11, int i12) {
        boolean z10 = false;
        if (new PrimitiveRanges(0, 255).i(i10) && new PrimitiveRanges(0, 255).i(i11) && new PrimitiveRanges(0, 255).i(i12)) {
            z10 = true;
        }
        if (z10) {
            return (i10 << 16) + (i11 << 8) + i12;
        }
        throw new IllegalArgumentException(("Version components are out of range: " + i10 + '.' + i11 + '.' + i12).toString());
    }

    @Override // java.lang.Comparable
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compareTo(f fVar) {
        za.k.e(fVar, "other");
        return this.f15172h - fVar.f15172h;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        f fVar = obj instanceof f ? (f) obj : null;
        return fVar != null && this.f15172h == fVar.f15172h;
    }

    public int hashCode() {
        return this.f15172h;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.f15169e);
        sb2.append('.');
        sb2.append(this.f15170f);
        sb2.append('.');
        sb2.append(this.f15171g);
        return sb2.toString();
    }

    public f(int i10, int i11) {
        this(i10, i11, 0);
    }
}
