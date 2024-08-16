package bc;

import fc.y;
import pb.TypeParameterDescriptor;

/* compiled from: resolvers.kt */
/* loaded from: classes2.dex */
public interface k {

    /* compiled from: resolvers.kt */
    /* loaded from: classes2.dex */
    public static final class a implements k {

        /* renamed from: a, reason: collision with root package name */
        public static final a f4706a = new a();

        private a() {
        }

        @Override // bc.k
        public TypeParameterDescriptor a(y yVar) {
            za.k.e(yVar, "javaTypeParameter");
            return null;
        }
    }

    TypeParameterDescriptor a(y yVar);
}
