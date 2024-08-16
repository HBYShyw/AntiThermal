package nd;

import nd.f;
import pb.FunctionDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
public abstract class k implements f {

    /* renamed from: a, reason: collision with root package name */
    private final String f16031a;

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class a extends k {

        /* renamed from: b, reason: collision with root package name */
        public static final a f16032b = new a();

        private a() {
            super("must be a member function", null);
        }

        @Override // nd.f
        public boolean a(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "functionDescriptor");
            return functionDescriptor.m0() != null;
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class b extends k {

        /* renamed from: b, reason: collision with root package name */
        public static final b f16033b = new b();

        private b() {
            super("must be a member or an extension function", null);
        }

        @Override // nd.f
        public boolean a(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "functionDescriptor");
            return (functionDescriptor.m0() == null && functionDescriptor.r0() == null) ? false : true;
        }
    }

    private k(String str) {
        this.f16031a = str;
    }

    public /* synthetic */ k(String str, DefaultConstructorMarker defaultConstructorMarker) {
        this(str);
    }

    @Override // nd.f
    public String b(FunctionDescriptor functionDescriptor) {
        return f.a.a(this, functionDescriptor);
    }

    @Override // nd.f
    public String c() {
        return this.f16031a;
    }
}
