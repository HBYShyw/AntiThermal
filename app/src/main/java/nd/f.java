package nd;

import pb.FunctionDescriptor;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
public interface f {

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        public static String a(f fVar, FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "functionDescriptor");
            if (fVar.a(functionDescriptor)) {
                return null;
            }
            return fVar.c();
        }
    }

    boolean a(FunctionDescriptor functionDescriptor);

    String b(FunctionDescriptor functionDescriptor);

    String c();
}
