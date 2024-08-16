package ma;

import za.DefaultConstructorMarker;

/* compiled from: UInt.kt */
/* renamed from: ma.x, reason: use source file name */
/* loaded from: classes2.dex */
public final class UInt implements Comparable<UInt> {

    /* renamed from: f, reason: collision with root package name */
    public static final a f15202f = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private final int f15203e;

    /* compiled from: UInt.kt */
    /* renamed from: ma.x$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private /* synthetic */ UInt(int i10) {
        this.f15203e = i10;
    }

    public static final /* synthetic */ UInt a(int i10) {
        return new UInt(i10);
    }

    public static int b(int i10) {
        return i10;
    }

    public static boolean c(int i10, Object obj) {
        return (obj instanceof UInt) && i10 == ((UInt) obj).f();
    }

    public static int d(int i10) {
        return Integer.hashCode(i10);
    }

    public static String e(int i10) {
        return String.valueOf(i10 & 4294967295L);
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UInt uInt) {
        return UnsignedUtils.a(f(), uInt.f());
    }

    public boolean equals(Object obj) {
        return c(this.f15203e, obj);
    }

    public final /* synthetic */ int f() {
        return this.f15203e;
    }

    public int hashCode() {
        return d(this.f15203e);
    }

    public String toString() {
        return e(this.f15203e);
    }
}
