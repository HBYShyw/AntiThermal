package db;

import java.io.Serializable;
import za.DefaultConstructorMarker;

/* compiled from: Random.kt */
/* renamed from: db.c, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class Random {

    /* renamed from: e, reason: collision with root package name */
    public static final a f10892e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private static final Random f10893f = ta.b.f18703a.b();

    /* compiled from: Random.kt */
    /* renamed from: db.c$a */
    /* loaded from: classes2.dex */
    public static final class a extends Random implements Serializable {

        /* compiled from: Random.kt */
        /* renamed from: db.c$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        private static final class C0030a implements Serializable {

            /* renamed from: e, reason: collision with root package name */
            public static final C0030a f10894e = new C0030a();
            private static final long serialVersionUID = 0;

            private C0030a() {
            }

            private final Object readResolve() {
                return Random.f10892e;
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final Object writeReplace() {
            return C0030a.f10894e;
        }

        @Override // db.Random
        public int b() {
            return Random.f10893f.b();
        }
    }

    public abstract int b();
}
