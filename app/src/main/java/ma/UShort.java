package ma;

import za.DefaultConstructorMarker;

/* compiled from: UShort.kt */
/* renamed from: ma.c0, reason: use source file name */
/* loaded from: classes2.dex */
public final class UShort implements Comparable<UShort> {

    /* renamed from: f, reason: collision with root package name */
    public static final a f15161f = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private final short f15162e;

    /* compiled from: UShort.kt */
    /* renamed from: ma.c0$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private /* synthetic */ UShort(short s7) {
        this.f15162e = s7;
    }

    public static final /* synthetic */ UShort a(short s7) {
        return new UShort(s7);
    }

    public static short b(short s7) {
        return s7;
    }

    public static boolean c(short s7, Object obj) {
        return (obj instanceof UShort) && s7 == ((UShort) obj).f();
    }

    public static int d(short s7) {
        return Short.hashCode(s7);
    }

    public static String e(short s7) {
        return String.valueOf(s7 & 65535);
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UShort uShort) {
        return za.k.f(f() & 65535, uShort.f() & 65535);
    }

    public boolean equals(Object obj) {
        return c(this.f15162e, obj);
    }

    public final /* synthetic */ short f() {
        return this.f15162e;
    }

    public int hashCode() {
        return d(this.f15162e);
    }

    public String toString() {
        return e(this.f15162e);
    }
}
