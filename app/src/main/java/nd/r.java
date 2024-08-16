package nd;

import gd.g0;
import gd.o0;
import mb.KotlinBuiltIns;
import nd.f;
import pb.FunctionDescriptor;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
public abstract class r implements f {

    /* renamed from: a, reason: collision with root package name */
    private final String f16072a;

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<KotlinBuiltIns, g0> f16073b;

    /* renamed from: c, reason: collision with root package name */
    private final String f16074c;

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class a extends r {

        /* renamed from: d, reason: collision with root package name */
        public static final a f16075d = new a();

        /* compiled from: modifierChecks.kt */
        /* renamed from: nd.r$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0084a extends Lambda implements ya.l<KotlinBuiltIns, g0> {

            /* renamed from: e, reason: collision with root package name */
            public static final C0084a f16076e = new C0084a();

            C0084a() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final g0 invoke(KotlinBuiltIns kotlinBuiltIns) {
                za.k.e(kotlinBuiltIns, "$this$null");
                o0 n10 = kotlinBuiltIns.n();
                za.k.d(n10, "booleanType");
                return n10;
            }
        }

        private a() {
            super("Boolean", C0084a.f16076e, null);
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class b extends r {

        /* renamed from: d, reason: collision with root package name */
        public static final b f16077d = new b();

        /* compiled from: modifierChecks.kt */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.l<KotlinBuiltIns, g0> {

            /* renamed from: e, reason: collision with root package name */
            public static final a f16078e = new a();

            a() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final g0 invoke(KotlinBuiltIns kotlinBuiltIns) {
                za.k.e(kotlinBuiltIns, "$this$null");
                o0 D = kotlinBuiltIns.D();
                za.k.d(D, "intType");
                return D;
            }
        }

        private b() {
            super("Int", a.f16078e, null);
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class c extends r {

        /* renamed from: d, reason: collision with root package name */
        public static final c f16079d = new c();

        /* compiled from: modifierChecks.kt */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.l<KotlinBuiltIns, g0> {

            /* renamed from: e, reason: collision with root package name */
            public static final a f16080e = new a();

            a() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final g0 invoke(KotlinBuiltIns kotlinBuiltIns) {
                za.k.e(kotlinBuiltIns, "$this$null");
                o0 Z = kotlinBuiltIns.Z();
                za.k.d(Z, "unitType");
                return Z;
            }
        }

        private c() {
            super("Unit", a.f16080e, null);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private r(String str, ya.l<? super KotlinBuiltIns, ? extends g0> lVar) {
        this.f16072a = str;
        this.f16073b = lVar;
        this.f16074c = "must return " + str;
    }

    public /* synthetic */ r(String str, ya.l lVar, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, lVar);
    }

    @Override // nd.f
    public boolean a(FunctionDescriptor functionDescriptor) {
        za.k.e(functionDescriptor, "functionDescriptor");
        return za.k.a(functionDescriptor.f(), this.f16073b.invoke(wc.c.j(functionDescriptor)));
    }

    @Override // nd.f
    public String b(FunctionDescriptor functionDescriptor) {
        return f.a.a(this, functionDescriptor);
    }

    @Override // nd.f
    public String c() {
        return this.f16074c;
    }
}
