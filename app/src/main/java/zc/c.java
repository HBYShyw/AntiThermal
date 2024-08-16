package zc;

import zc.d;

/* compiled from: MemberScope.kt */
/* loaded from: classes2.dex */
public abstract class c {

    /* compiled from: MemberScope.kt */
    /* loaded from: classes2.dex */
    public static final class a extends c {

        /* renamed from: a, reason: collision with root package name */
        public static final a f20421a = new a();

        /* renamed from: b, reason: collision with root package name */
        private static final int f20422b;

        static {
            d.a aVar = d.f20424c;
            f20422b = (~(aVar.i() | aVar.d())) & aVar.b();
        }

        private a() {
        }

        @Override // zc.c
        public int a() {
            return f20422b;
        }
    }

    /* compiled from: MemberScope.kt */
    /* loaded from: classes2.dex */
    public static final class b extends c {

        /* renamed from: a, reason: collision with root package name */
        public static final b f20423a = new b();

        private b() {
        }

        @Override // zc.c
        public int a() {
            return 0;
        }
    }

    public abstract int a();

    public String toString() {
        return getClass().getSimpleName();
    }
}
