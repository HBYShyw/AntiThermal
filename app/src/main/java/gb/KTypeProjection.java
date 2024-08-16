package gb;

import ma.NoWhenBranchMatchedException;
import za.DefaultConstructorMarker;

/* compiled from: KTypeProjection.kt */
/* renamed from: gb.q, reason: use source file name */
/* loaded from: classes2.dex */
public final class KTypeProjection {

    /* renamed from: c, reason: collision with root package name */
    public static final a f11621c = new a(null);

    /* renamed from: d, reason: collision with root package name */
    public static final KTypeProjection f11622d = new KTypeProjection(null, null);

    /* renamed from: a, reason: collision with root package name */
    private final KVariance f11623a;

    /* renamed from: b, reason: collision with root package name */
    private final KType f11624b;

    /* compiled from: KTypeProjection.kt */
    /* renamed from: gb.q$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final KTypeProjection a(KType kType) {
            za.k.e(kType, "type");
            return new KTypeProjection(KVariance.IN, kType);
        }

        public final KTypeProjection b(KType kType) {
            za.k.e(kType, "type");
            return new KTypeProjection(KVariance.OUT, kType);
        }

        public final KTypeProjection c() {
            return KTypeProjection.f11622d;
        }

        public final KTypeProjection d(KType kType) {
            za.k.e(kType, "type");
            return new KTypeProjection(KVariance.INVARIANT, kType);
        }
    }

    /* compiled from: KTypeProjection.kt */
    /* renamed from: gb.q$b */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f11625a;

        static {
            int[] iArr = new int[KVariance.values().length];
            try {
                iArr[KVariance.INVARIANT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[KVariance.IN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[KVariance.OUT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f11625a = iArr;
        }
    }

    public KTypeProjection(KVariance kVariance, KType kType) {
        String str;
        this.f11623a = kVariance;
        this.f11624b = kType;
        if ((kVariance == null) == (kType == null)) {
            return;
        }
        if (kVariance == null) {
            str = "Star projection must have no type specified.";
        } else {
            str = "The projection variance " + kVariance + " requires type to be specified.";
        }
        throw new IllegalArgumentException(str.toString());
    }

    public final KVariance a() {
        return this.f11623a;
    }

    public final KType b() {
        return this.f11624b;
    }

    public final KType c() {
        return this.f11624b;
    }

    public final KVariance d() {
        return this.f11623a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KTypeProjection)) {
            return false;
        }
        KTypeProjection kTypeProjection = (KTypeProjection) obj;
        return this.f11623a == kTypeProjection.f11623a && za.k.a(this.f11624b, kTypeProjection.f11624b);
    }

    public int hashCode() {
        KVariance kVariance = this.f11623a;
        int hashCode = (kVariance == null ? 0 : kVariance.hashCode()) * 31;
        KType kType = this.f11624b;
        return hashCode + (kType != null ? kType.hashCode() : 0);
    }

    public String toString() {
        KVariance kVariance = this.f11623a;
        int i10 = kVariance == null ? -1 : b.f11625a[kVariance.ordinal()];
        if (i10 == -1) {
            return "*";
        }
        if (i10 == 1) {
            return String.valueOf(this.f11624b);
        }
        if (i10 == 2) {
            return "in " + this.f11624b;
        }
        if (i10 != 3) {
            throw new NoWhenBranchMatchedException();
        }
        return "out " + this.f11624b;
    }
}
