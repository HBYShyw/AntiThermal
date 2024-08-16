package pb;

import java.util.Map;
import kotlin.collections.MapsJVM;

/* compiled from: Visibilities.kt */
/* renamed from: pb.m1, reason: use source file name */
/* loaded from: classes2.dex */
public final class Visibilities {

    /* renamed from: a, reason: collision with root package name */
    public static final Visibilities f16708a = new Visibilities();

    /* renamed from: b, reason: collision with root package name */
    private static final Map<n1, Integer> f16709b;

    /* renamed from: c, reason: collision with root package name */
    private static final h f16710c;

    /* compiled from: Visibilities.kt */
    /* renamed from: pb.m1$a */
    /* loaded from: classes2.dex */
    public static final class a extends n1 {

        /* renamed from: c, reason: collision with root package name */
        public static final a f16711c = new a();

        private a() {
            super("inherited", false);
        }
    }

    /* compiled from: Visibilities.kt */
    /* renamed from: pb.m1$b */
    /* loaded from: classes2.dex */
    public static final class b extends n1 {

        /* renamed from: c, reason: collision with root package name */
        public static final b f16712c = new b();

        private b() {
            super("internal", false);
        }
    }

    /* compiled from: Visibilities.kt */
    /* renamed from: pb.m1$c */
    /* loaded from: classes2.dex */
    public static final class c extends n1 {

        /* renamed from: c, reason: collision with root package name */
        public static final c f16713c = new c();

        private c() {
            super("invisible_fake", false);
        }
    }

    /* compiled from: Visibilities.kt */
    /* renamed from: pb.m1$d */
    /* loaded from: classes2.dex */
    public static final class d extends n1 {

        /* renamed from: c, reason: collision with root package name */
        public static final d f16714c = new d();

        private d() {
            super("local", false);
        }
    }

    /* compiled from: Visibilities.kt */
    /* renamed from: pb.m1$e */
    /* loaded from: classes2.dex */
    public static final class e extends n1 {

        /* renamed from: c, reason: collision with root package name */
        public static final e f16715c = new e();

        private e() {
            super("private", false);
        }
    }

    /* compiled from: Visibilities.kt */
    /* renamed from: pb.m1$f */
    /* loaded from: classes2.dex */
    public static final class f extends n1 {

        /* renamed from: c, reason: collision with root package name */
        public static final f f16716c = new f();

        private f() {
            super("private_to_this", false);
        }

        @Override // pb.n1
        public String b() {
            return "private/*private to this*/";
        }
    }

    /* compiled from: Visibilities.kt */
    /* renamed from: pb.m1$g */
    /* loaded from: classes2.dex */
    public static final class g extends n1 {

        /* renamed from: c, reason: collision with root package name */
        public static final g f16717c = new g();

        private g() {
            super("protected", true);
        }
    }

    /* compiled from: Visibilities.kt */
    /* renamed from: pb.m1$h */
    /* loaded from: classes2.dex */
    public static final class h extends n1 {

        /* renamed from: c, reason: collision with root package name */
        public static final h f16718c = new h();

        private h() {
            super("public", true);
        }
    }

    /* compiled from: Visibilities.kt */
    /* renamed from: pb.m1$i */
    /* loaded from: classes2.dex */
    public static final class i extends n1 {

        /* renamed from: c, reason: collision with root package name */
        public static final i f16719c = new i();

        private i() {
            super("unknown", false);
        }
    }

    static {
        Map c10;
        Map<n1, Integer> b10;
        c10 = MapsJVM.c();
        c10.put(f.f16716c, 0);
        c10.put(e.f16715c, 0);
        c10.put(b.f16712c, 1);
        c10.put(g.f16717c, 1);
        h hVar = h.f16718c;
        c10.put(hVar, 2);
        b10 = MapsJVM.b(c10);
        f16709b = b10;
        f16710c = hVar;
    }

    private Visibilities() {
    }

    public final Integer a(n1 n1Var, n1 n1Var2) {
        za.k.e(n1Var, "first");
        za.k.e(n1Var2, "second");
        if (n1Var == n1Var2) {
            return 0;
        }
        Map<n1, Integer> map = f16709b;
        Integer num = map.get(n1Var);
        Integer num2 = map.get(n1Var2);
        if (num == null || num2 == null || za.k.a(num, num2)) {
            return null;
        }
        return Integer.valueOf(num.intValue() - num2.intValue());
    }

    public final boolean b(n1 n1Var) {
        za.k.e(n1Var, "visibility");
        return n1Var == e.f16715c || n1Var == f.f16716c;
    }
}
