package cd;

import lc.TypeTable;
import pb.CallableDescriptor;
import pb.FunctionDescriptor;

/* compiled from: ContractDeserializer.kt */
/* renamed from: cd.j, reason: use source file name */
/* loaded from: classes2.dex */
public interface ContractDeserializer {

    /* renamed from: a, reason: collision with root package name */
    public static final a f5239a = a.f5240a;

    /* compiled from: ContractDeserializer.kt */
    /* renamed from: cd.j$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f5240a = new a();

        /* renamed from: b, reason: collision with root package name */
        private static final ContractDeserializer f5241b = new C0016a();

        /* compiled from: ContractDeserializer.kt */
        /* renamed from: cd.j$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0016a implements ContractDeserializer {
            C0016a() {
            }

            @Override // cd.ContractDeserializer
            public ma.o a(jc.i iVar, FunctionDescriptor functionDescriptor, TypeTable typeTable, d0 d0Var) {
                za.k.e(iVar, "proto");
                za.k.e(functionDescriptor, "ownerFunction");
                za.k.e(typeTable, "typeTable");
                za.k.e(d0Var, "typeDeserializer");
                return null;
            }
        }

        private a() {
        }

        public final ContractDeserializer a() {
            return f5241b;
        }
    }

    ma.o<CallableDescriptor.a<?>, Object> a(jc.i iVar, FunctionDescriptor functionDescriptor, TypeTable typeTable, d0 d0Var);
}
