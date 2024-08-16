package ob;

import fd.StorageManager;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.r;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import sb.SimpleFunctionDescriptorImpl;
import za.DefaultConstructorMarker;
import zc.GivenFunctionsMemberScope;

/* compiled from: CloneableClassScope.kt */
/* renamed from: ob.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class CloneableClassScope extends GivenFunctionsMemberScope {

    /* renamed from: e, reason: collision with root package name */
    public static final a f16335e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private static final Name f16336f;

    /* compiled from: CloneableClassScope.kt */
    /* renamed from: ob.a$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Name a() {
            return CloneableClassScope.f16336f;
        }
    }

    static {
        Name f10 = Name.f("clone");
        za.k.d(f10, "identifier(\"clone\")");
        f16336f = f10;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CloneableClassScope(StorageManager storageManager, ClassDescriptor classDescriptor) {
        super(storageManager, classDescriptor);
        za.k.e(storageManager, "storageManager");
        za.k.e(classDescriptor, "containingClass");
    }

    @Override // zc.GivenFunctionsMemberScope
    protected List<FunctionDescriptor> i() {
        List<ReceiverParameterDescriptor> j10;
        List<? extends TypeParameterDescriptor> j11;
        List<ValueParameterDescriptor> j12;
        List<FunctionDescriptor> e10;
        SimpleFunctionDescriptorImpl u12 = SimpleFunctionDescriptorImpl.u1(l(), qb.g.f17195b.b(), f16336f, CallableMemberDescriptor.a.DECLARATION, SourceElement.f16664a);
        ReceiverParameterDescriptor S0 = l().S0();
        j10 = r.j();
        j11 = r.j();
        j12 = r.j();
        u12.a1(null, S0, j10, j11, j12, wc.c.j(l()).i(), Modality.OPEN, DescriptorVisibilities.f16731c);
        e10 = CollectionsJVM.e(u12);
        return e10;
    }
}
