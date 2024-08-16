package fb;

import za.DefaultConstructorMarker;

/* compiled from: PrimitiveRanges.kt */
/* renamed from: fb.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class PrimitiveRanges extends Progressions {

    /* renamed from: i, reason: collision with root package name */
    public static final a f11414i = new a(null);

    /* renamed from: j, reason: collision with root package name */
    private static final PrimitiveRanges f11415j = new PrimitiveRanges(1, 0);

    /* compiled from: PrimitiveRanges.kt */
    /* renamed from: fb.c$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final PrimitiveRanges a() {
            return PrimitiveRanges.f11415j;
        }
    }

    public PrimitiveRanges(int i10, int i11) {
        super(i10, i11, 1);
    }

    @Override // fb.Progressions
    public boolean equals(Object obj) {
        if (obj instanceof PrimitiveRanges) {
            if (!isEmpty() || !((PrimitiveRanges) obj).isEmpty()) {
                PrimitiveRanges primitiveRanges = (PrimitiveRanges) obj;
                if (d() != primitiveRanges.d() || e() != primitiveRanges.e()) {
                }
            }
            return true;
        }
        return false;
    }

    @Override // fb.Progressions
    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return e() + (d() * 31);
    }

    public boolean i(int i10) {
        return d() <= i10 && i10 <= e();
    }

    @Override // fb.Progressions
    public boolean isEmpty() {
        return d() > e();
    }

    public Integer k() {
        return Integer.valueOf(e());
    }

    public Integer l() {
        return Integer.valueOf(d());
    }

    @Override // fb.Progressions
    public String toString() {
        return d() + ".." + e();
    }
}
