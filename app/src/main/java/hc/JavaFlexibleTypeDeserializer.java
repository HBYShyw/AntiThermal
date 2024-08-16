package hc;

import cd.FlexibleTypeDeserializer;
import gd.g0;
import gd.h0;
import gd.o0;
import id.ErrorTypeKind;
import id.ErrorUtils;
import mc.JvmProtoBuf;

/* compiled from: JavaFlexibleTypeDeserializer.kt */
/* renamed from: hc.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaFlexibleTypeDeserializer implements FlexibleTypeDeserializer {

    /* renamed from: a, reason: collision with root package name */
    public static final JavaFlexibleTypeDeserializer f12176a = new JavaFlexibleTypeDeserializer();

    private JavaFlexibleTypeDeserializer() {
    }

    @Override // cd.FlexibleTypeDeserializer
    public g0 a(jc.q qVar, String str, o0 o0Var, o0 o0Var2) {
        za.k.e(qVar, "proto");
        za.k.e(str, "flexibleId");
        za.k.e(o0Var, "lowerBound");
        za.k.e(o0Var2, "upperBound");
        if (!za.k.a(str, "kotlin.jvm.PlatformType")) {
            return ErrorUtils.d(ErrorTypeKind.N, str, o0Var.toString(), o0Var2.toString());
        }
        if (qVar.s(JvmProtoBuf.f15370g)) {
            return new dc.h(o0Var, o0Var2);
        }
        return h0.d(o0Var, o0Var2);
    }
}
