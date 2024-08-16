package sb;

import gd.TypeSubstitutor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.DescriptorVisibilities;
import pb.SourceElement;
import pb.ValueParameterDescriptor;
import pb.VariableDescriptor;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: ValueParameterDescriptorImpl.kt */
/* renamed from: sb.l0, reason: use source file name */
/* loaded from: classes2.dex */
public class ValueParameterDescriptorImpl extends VariableDescriptorImpl implements ValueParameterDescriptor {

    /* renamed from: p, reason: collision with root package name */
    public static final a f18296p = new a(null);

    /* renamed from: j, reason: collision with root package name */
    private final int f18297j;

    /* renamed from: k, reason: collision with root package name */
    private final boolean f18298k;

    /* renamed from: l, reason: collision with root package name */
    private final boolean f18299l;

    /* renamed from: m, reason: collision with root package name */
    private final boolean f18300m;

    /* renamed from: n, reason: collision with root package name */
    private final gd.g0 f18301n;

    /* renamed from: o, reason: collision with root package name */
    private final ValueParameterDescriptor f18302o;

    /* compiled from: ValueParameterDescriptorImpl.kt */
    /* renamed from: sb.l0$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final ValueParameterDescriptorImpl a(CallableDescriptor callableDescriptor, ValueParameterDescriptor valueParameterDescriptor, int i10, qb.g gVar, Name name, gd.g0 g0Var, boolean z10, boolean z11, boolean z12, gd.g0 g0Var2, SourceElement sourceElement, ya.a<? extends List<? extends VariableDescriptor>> aVar) {
            za.k.e(callableDescriptor, "containingDeclaration");
            za.k.e(gVar, "annotations");
            za.k.e(name, "name");
            za.k.e(g0Var, "outType");
            za.k.e(sourceElement, "source");
            if (aVar == null) {
                return new ValueParameterDescriptorImpl(callableDescriptor, valueParameterDescriptor, i10, gVar, name, g0Var, z10, z11, z12, g0Var2, sourceElement);
            }
            return new b(callableDescriptor, valueParameterDescriptor, i10, gVar, name, g0Var, z10, z11, z12, g0Var2, sourceElement, aVar);
        }
    }

    /* compiled from: ValueParameterDescriptorImpl.kt */
    /* renamed from: sb.l0$b */
    /* loaded from: classes2.dex */
    public static final class b extends ValueParameterDescriptorImpl {

        /* renamed from: q, reason: collision with root package name */
        private final ma.h f18303q;

        /* compiled from: ValueParameterDescriptorImpl.kt */
        /* renamed from: sb.l0$b$a */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.a<List<? extends VariableDescriptor>> {
            a() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<VariableDescriptor> invoke() {
                return b.this.W0();
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(CallableDescriptor callableDescriptor, ValueParameterDescriptor valueParameterDescriptor, int i10, qb.g gVar, Name name, gd.g0 g0Var, boolean z10, boolean z11, boolean z12, gd.g0 g0Var2, SourceElement sourceElement, ya.a<? extends List<? extends VariableDescriptor>> aVar) {
            super(callableDescriptor, valueParameterDescriptor, i10, gVar, name, g0Var, z10, z11, z12, g0Var2, sourceElement);
            ma.h b10;
            za.k.e(callableDescriptor, "containingDeclaration");
            za.k.e(gVar, "annotations");
            za.k.e(name, "name");
            za.k.e(g0Var, "outType");
            za.k.e(sourceElement, "source");
            za.k.e(aVar, "destructuringVariables");
            b10 = ma.j.b(aVar);
            this.f18303q = b10;
        }

        @Override // sb.ValueParameterDescriptorImpl, pb.ValueParameterDescriptor
        public ValueParameterDescriptor W(CallableDescriptor callableDescriptor, Name name, int i10) {
            za.k.e(callableDescriptor, "newOwner");
            za.k.e(name, "newName");
            qb.g i11 = i();
            za.k.d(i11, "annotations");
            gd.g0 type = getType();
            za.k.d(type, "type");
            boolean z02 = z0();
            boolean i02 = i0();
            boolean g02 = g0();
            gd.g0 q02 = q0();
            SourceElement sourceElement = SourceElement.f16664a;
            za.k.d(sourceElement, "NO_SOURCE");
            return new b(callableDescriptor, null, i10, i11, name, type, z02, i02, g02, q02, sourceElement, new a());
        }

        public final List<VariableDescriptor> W0() {
            return (List) this.f18303q.getValue();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ValueParameterDescriptorImpl(CallableDescriptor callableDescriptor, ValueParameterDescriptor valueParameterDescriptor, int i10, qb.g gVar, Name name, gd.g0 g0Var, boolean z10, boolean z11, boolean z12, gd.g0 g0Var2, SourceElement sourceElement) {
        super(callableDescriptor, gVar, name, g0Var, sourceElement);
        za.k.e(callableDescriptor, "containingDeclaration");
        za.k.e(gVar, "annotations");
        za.k.e(name, "name");
        za.k.e(g0Var, "outType");
        za.k.e(sourceElement, "source");
        this.f18297j = i10;
        this.f18298k = z10;
        this.f18299l = z11;
        this.f18300m = z12;
        this.f18301n = g0Var2;
        this.f18302o = valueParameterDescriptor == null ? this : valueParameterDescriptor;
    }

    public static final ValueParameterDescriptorImpl T0(CallableDescriptor callableDescriptor, ValueParameterDescriptor valueParameterDescriptor, int i10, qb.g gVar, Name name, gd.g0 g0Var, boolean z10, boolean z11, boolean z12, gd.g0 g0Var2, SourceElement sourceElement, ya.a<? extends List<? extends VariableDescriptor>> aVar) {
        return f18296p.a(callableDescriptor, valueParameterDescriptor, i10, gVar, name, g0Var, z10, z11, z12, g0Var2, sourceElement, aVar);
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        za.k.e(declarationDescriptorVisitor, "visitor");
        return declarationDescriptorVisitor.e(this, d10);
    }

    public Void U0() {
        return null;
    }

    @Override // pb.Substitutable
    /* renamed from: V0, reason: merged with bridge method [inline-methods] */
    public ValueParameterDescriptor c(TypeSubstitutor typeSubstitutor) {
        za.k.e(typeSubstitutor, "substitutor");
        if (typeSubstitutor.k()) {
            return this;
        }
        throw new UnsupportedOperationException();
    }

    @Override // pb.ValueParameterDescriptor
    public ValueParameterDescriptor W(CallableDescriptor callableDescriptor, Name name, int i10) {
        za.k.e(callableDescriptor, "newOwner");
        za.k.e(name, "newName");
        qb.g i11 = i();
        za.k.d(i11, "annotations");
        gd.g0 type = getType();
        za.k.d(type, "type");
        boolean z02 = z0();
        boolean i02 = i0();
        boolean g02 = g0();
        gd.g0 q02 = q0();
        SourceElement sourceElement = SourceElement.f16664a;
        za.k.d(sourceElement, "NO_SOURCE");
        return new ValueParameterDescriptorImpl(callableDescriptor, null, i10, i11, name, type, z02, i02, g02, q02, sourceElement);
    }

    @Override // pb.CallableDescriptor
    public Collection<ValueParameterDescriptor> e() {
        int u7;
        Collection<? extends CallableDescriptor> e10 = b().e();
        za.k.d(e10, "containingDeclaration.overriddenDescriptors");
        u7 = kotlin.collections.s.u(e10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = e10.iterator();
        while (it.hasNext()) {
            arrayList.add(((CallableDescriptor) it.next()).l().get(j()));
        }
        return arrayList;
    }

    @Override // pb.VariableDescriptor
    public /* bridge */ /* synthetic */ uc.g f0() {
        return (uc.g) U0();
    }

    @Override // pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        pb.u uVar = DescriptorVisibilities.f16734f;
        za.k.d(uVar, "LOCAL");
        return uVar;
    }

    @Override // pb.ValueParameterDescriptor
    public boolean g0() {
        return this.f18300m;
    }

    @Override // pb.ValueParameterDescriptor
    public boolean i0() {
        return this.f18299l;
    }

    @Override // pb.ValueParameterDescriptor
    public int j() {
        return this.f18297j;
    }

    @Override // pb.VariableDescriptor
    public boolean p0() {
        return false;
    }

    @Override // pb.ValueParameterDescriptor
    public gd.g0 q0() {
        return this.f18301n;
    }

    @Override // pb.ValueParameterDescriptor
    public boolean z0() {
        if (this.f18298k) {
            CallableDescriptor b10 = b();
            za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.CallableMemberDescriptor");
            if (((CallableMemberDescriptor) b10).getKind().a()) {
                return true;
            }
        }
        return false;
    }

    @Override // sb.DeclarationDescriptorNonRootImpl, pb.DeclarationDescriptor
    public CallableDescriptor b() {
        DeclarationDescriptor b10 = super.b();
        za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.CallableDescriptor");
        return (CallableDescriptor) b10;
    }

    @Override // sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    /* renamed from: a */
    public ValueParameterDescriptor T0() {
        ValueParameterDescriptor valueParameterDescriptor = this.f18302o;
        return valueParameterDescriptor == this ? this : valueParameterDescriptor.T0();
    }
}
