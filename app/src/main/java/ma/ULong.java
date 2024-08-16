package ma;

import za.DefaultConstructorMarker;

/* compiled from: ULong.kt */
/* renamed from: ma.z, reason: use source file name */
/* loaded from: classes2.dex */
public final class ULong implements Comparable<ULong> {

    /* renamed from: f, reason: collision with root package name */
    public static final a f15207f = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private final long f15208e;

    /* compiled from: ULong.kt */
    /* renamed from: ma.z$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private /* synthetic */ ULong(long j10) {
        this.f15208e = j10;
    }

    public static final /* synthetic */ ULong a(long j10) {
        return new ULong(j10);
    }

    public static long b(long j10) {
        return j10;
    }

    public static boolean c(long j10, Object obj) {
        return (obj instanceof ULong) && j10 == ((ULong) obj).f();
    }

    public static int d(long j10) {
        return Long.hashCode(j10);
    }

    public static String e(long j10) {
        return UnsignedUtils.c(j10);
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(ULong uLong) {
        return UnsignedUtils.b(f(), uLong.f());
    }

    public boolean equals(Object obj) {
        return c(this.f15208e, obj);
    }

    public final /* synthetic */ long f() {
        return this.f15208e;
    }

    public int hashCode() {
        return d(this.f15208e);
    }

    public String toString() {
        return e(this.f15208e);
    }
}
