package ma;

import za.DefaultConstructorMarker;

/* compiled from: UByte.kt */
/* renamed from: ma.v, reason: use source file name */
/* loaded from: classes2.dex */
public final class UByte implements Comparable<UByte> {

    /* renamed from: f, reason: collision with root package name */
    public static final a f15197f = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private final byte f15198e;

    /* compiled from: UByte.kt */
    /* renamed from: ma.v$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private /* synthetic */ UByte(byte b10) {
        this.f15198e = b10;
    }

    public static final /* synthetic */ UByte a(byte b10) {
        return new UByte(b10);
    }

    public static byte b(byte b10) {
        return b10;
    }

    public static boolean c(byte b10, Object obj) {
        return (obj instanceof UByte) && b10 == ((UByte) obj).f();
    }

    public static int d(byte b10) {
        return Byte.hashCode(b10);
    }

    public static String e(byte b10) {
        return String.valueOf(b10 & 255);
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UByte uByte) {
        return za.k.f(f() & 255, uByte.f() & 255);
    }

    public boolean equals(Object obj) {
        return c(this.f15198e, obj);
    }

    public final /* synthetic */ byte f() {
        return this.f15198e;
    }

    public int hashCode() {
        return d(this.f15198e);
    }

    public String toString() {
        return e(this.f15198e);
    }
}
