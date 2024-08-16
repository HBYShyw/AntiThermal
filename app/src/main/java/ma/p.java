package ma;

import java.io.Serializable;
import za.DefaultConstructorMarker;

/* compiled from: Result.kt */
/* loaded from: classes2.dex */
public final class p<T> implements Serializable {

    /* renamed from: e, reason: collision with root package name */
    public static final a f15184e = new a(null);

    /* compiled from: Result.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: Result.kt */
    /* loaded from: classes2.dex */
    public static final class b implements Serializable {

        /* renamed from: e, reason: collision with root package name */
        public final Throwable f15185e;

        public b(Throwable th) {
            za.k.e(th, "exception");
            this.f15185e = th;
        }

        public boolean equals(Object obj) {
            return (obj instanceof b) && za.k.a(this.f15185e, ((b) obj).f15185e);
        }

        public int hashCode() {
            return this.f15185e.hashCode();
        }

        public String toString() {
            return "Failure(" + this.f15185e + ')';
        }
    }

    public static <T> Object a(Object obj) {
        return obj;
    }

    public static final Throwable b(Object obj) {
        if (obj instanceof b) {
            return ((b) obj).f15185e;
        }
        return null;
    }

    public static final boolean c(Object obj) {
        return obj instanceof b;
    }

    public static final boolean d(Object obj) {
        return !(obj instanceof b);
    }
}
