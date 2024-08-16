package nd;

import nd.f;
import pb.FunctionDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
public abstract class t implements f {

    /* renamed from: a, reason: collision with root package name */
    private final String f16084a;

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class a extends t {

        /* renamed from: b, reason: collision with root package name */
        private final int f16085b;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public a(int i10) {
            super(r0.toString(), null);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("must have at least ");
            sb2.append(i10);
            sb2.append(" value parameter");
            sb2.append(i10 > 1 ? "s" : "");
            this.f16085b = i10;
        }

        @Override // nd.f
        public boolean a(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "functionDescriptor");
            return functionDescriptor.l().size() >= this.f16085b;
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class b extends t {

        /* renamed from: b, reason: collision with root package name */
        private final int f16086b;

        public b(int i10) {
            super("must have exactly " + i10 + " value parameters", null);
            this.f16086b = i10;
        }

        @Override // nd.f
        public boolean a(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "functionDescriptor");
            return functionDescriptor.l().size() == this.f16086b;
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class c extends t {

        /* renamed from: b, reason: collision with root package name */
        public static final c f16087b = new c();

        private c() {
            super("must have no value parameters", null);
        }

        @Override // nd.f
        public boolean a(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "functionDescriptor");
            return functionDescriptor.l().isEmpty();
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class d extends t {

        /* renamed from: b, reason: collision with root package name */
        public static final d f16088b = new d();

        private d() {
            super("must have a single value parameter", null);
        }

        @Override // nd.f
        public boolean a(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "functionDescriptor");
            return functionDescriptor.l().size() == 1;
        }
    }

    private t(String str) {
        this.f16084a = str;
    }

    public /* synthetic */ t(String str, DefaultConstructorMarker defaultConstructorMarker) {
        this(str);
    }

    @Override // nd.f
    public String b(FunctionDescriptor functionDescriptor) {
        return f.a.a(this, functionDescriptor);
    }

    @Override // nd.f
    public String c() {
        return this.f16084a;
    }
}
