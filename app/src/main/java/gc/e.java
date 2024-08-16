package gc;

import za.DefaultConstructorMarker;

/* compiled from: typeQualifiers.kt */
/* loaded from: classes2.dex */
public final class e {

    /* renamed from: e, reason: collision with root package name */
    public static final a f11657e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private static final e f11658f = new e(null, null, false, false, 8, null);

    /* renamed from: a, reason: collision with root package name */
    private final h f11659a;

    /* renamed from: b, reason: collision with root package name */
    private final f f11660b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f11661c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f11662d;

    /* compiled from: typeQualifiers.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final e a() {
            return e.f11658f;
        }
    }

    public e(h hVar, f fVar, boolean z10, boolean z11) {
        this.f11659a = hVar;
        this.f11660b = fVar;
        this.f11661c = z10;
        this.f11662d = z11;
    }

    public final boolean b() {
        return this.f11661c;
    }

    public final f c() {
        return this.f11660b;
    }

    public final h d() {
        return this.f11659a;
    }

    public final boolean e() {
        return this.f11662d;
    }

    public /* synthetic */ e(h hVar, f fVar, boolean z10, boolean z11, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(hVar, fVar, z10, (i10 & 8) != 0 ? false : z11);
    }
}
