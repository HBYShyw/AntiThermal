package cd;

import gd.g0;
import gd.o0;

/* compiled from: FlexibleTypeDeserializer.kt */
/* renamed from: cd.s, reason: use source file name */
/* loaded from: classes2.dex */
public interface FlexibleTypeDeserializer {

    /* compiled from: FlexibleTypeDeserializer.kt */
    /* renamed from: cd.s$a */
    /* loaded from: classes2.dex */
    public static final class a implements FlexibleTypeDeserializer {

        /* renamed from: a, reason: collision with root package name */
        public static final a f5286a = new a();

        private a() {
        }

        @Override // cd.FlexibleTypeDeserializer
        public g0 a(jc.q qVar, String str, o0 o0Var, o0 o0Var2) {
            za.k.e(qVar, "proto");
            za.k.e(str, "flexibleId");
            za.k.e(o0Var, "lowerBound");
            za.k.e(o0Var2, "upperBound");
            throw new IllegalArgumentException("This method should not be used.");
        }
    }

    g0 a(jc.q qVar, String str, o0 o0Var, o0 o0Var2);
}
