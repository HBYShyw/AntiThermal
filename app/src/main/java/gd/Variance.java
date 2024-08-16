package gd;

/* compiled from: Variance.kt */
/* renamed from: gd.w1, reason: use source file name */
/* loaded from: classes2.dex */
public enum Variance {
    INVARIANT("", true, true, 0),
    IN_VARIANCE("in", true, false, -1),
    OUT_VARIANCE("out", false, true, 1);


    /* renamed from: e, reason: collision with root package name */
    private final String f11903e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f11904f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f11905g;

    /* renamed from: h, reason: collision with root package name */
    private final int f11906h;

    Variance(String str, boolean z10, boolean z11, int i10) {
        this.f11903e = str;
        this.f11904f = z10;
        this.f11905g = z11;
        this.f11906h = i10;
    }

    public final boolean b() {
        return this.f11905g;
    }

    public final String c() {
        return this.f11903e;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.f11903e;
    }
}
