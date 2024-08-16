package nb;

import gd.TypeSubstitutor;
import gd.Variance;
import gd.g0;
import gd.o0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.collections.IndexedValue;
import kotlin.collections._Collections;
import kotlin.collections.r;
import kotlin.collections.s;
import ma.o;
import mb.functionTypes;
import nd.OperatorNameConventions;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import qb.g;
import sb.FunctionDescriptorImpl;
import sb.SimpleFunctionDescriptorImpl;
import sb.ValueParameterDescriptorImpl;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: FunctionInvokeDescriptor.kt */
/* renamed from: nb.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class FunctionInvokeDescriptor extends SimpleFunctionDescriptorImpl {
    public static final a I = new a(null);

    /* compiled from: FunctionInvokeDescriptor.kt */
    /* renamed from: nb.e$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final ValueParameterDescriptor b(FunctionInvokeDescriptor functionInvokeDescriptor, int i10, TypeParameterDescriptor typeParameterDescriptor) {
            String lowerCase;
            String b10 = typeParameterDescriptor.getName().b();
            k.d(b10, "typeParameter.name.asString()");
            if (k.a(b10, "T")) {
                lowerCase = "instance";
            } else if (k.a(b10, "E")) {
                lowerCase = "receiver";
            } else {
                lowerCase = b10.toLowerCase(Locale.ROOT);
                k.d(lowerCase, "this as java.lang.String).toLowerCase(Locale.ROOT)");
            }
            g b11 = g.f17195b.b();
            Name f10 = Name.f(lowerCase);
            k.d(f10, "identifier(name)");
            o0 x10 = typeParameterDescriptor.x();
            k.d(x10, "typeParameter.defaultType");
            SourceElement sourceElement = SourceElement.f16664a;
            k.d(sourceElement, "NO_SOURCE");
            return new ValueParameterDescriptorImpl(functionInvokeDescriptor, null, i10, b11, f10, x10, false, false, false, null, sourceElement);
        }

        public final FunctionInvokeDescriptor a(FunctionClassDescriptor functionClassDescriptor, boolean z10) {
            List<ReceiverParameterDescriptor> j10;
            List<? extends TypeParameterDescriptor> j11;
            Iterable<IndexedValue> F0;
            int u7;
            Object e02;
            k.e(functionClassDescriptor, "functionClass");
            List<TypeParameterDescriptor> B = functionClassDescriptor.B();
            FunctionInvokeDescriptor functionInvokeDescriptor = new FunctionInvokeDescriptor(functionClassDescriptor, null, CallableMemberDescriptor.a.DECLARATION, z10, null);
            ReceiverParameterDescriptor S0 = functionClassDescriptor.S0();
            j10 = r.j();
            j11 = r.j();
            ArrayList arrayList = new ArrayList();
            for (Object obj : B) {
                if (!(((TypeParameterDescriptor) obj).s() == Variance.IN_VARIANCE)) {
                    break;
                }
                arrayList.add(obj);
            }
            F0 = _Collections.F0(arrayList);
            u7 = s.u(F0, 10);
            ArrayList arrayList2 = new ArrayList(u7);
            for (IndexedValue indexedValue : F0) {
                arrayList2.add(FunctionInvokeDescriptor.I.b(functionInvokeDescriptor, indexedValue.c(), (TypeParameterDescriptor) indexedValue.d()));
            }
            e02 = _Collections.e0(B);
            functionInvokeDescriptor.a1(null, S0, j10, j11, arrayList2, ((TypeParameterDescriptor) e02).x(), Modality.ABSTRACT, DescriptorVisibilities.f16733e);
            functionInvokeDescriptor.i1(true);
            return functionInvokeDescriptor;
        }
    }

    private FunctionInvokeDescriptor(DeclarationDescriptor declarationDescriptor, FunctionInvokeDescriptor functionInvokeDescriptor, CallableMemberDescriptor.a aVar, boolean z10) {
        super(declarationDescriptor, functionInvokeDescriptor, g.f17195b.b(), OperatorNameConventions.f16054i, aVar, SourceElement.f16664a);
        o1(true);
        q1(z10);
        h1(false);
    }

    public /* synthetic */ FunctionInvokeDescriptor(DeclarationDescriptor declarationDescriptor, FunctionInvokeDescriptor functionInvokeDescriptor, CallableMemberDescriptor.a aVar, boolean z10, DefaultConstructorMarker defaultConstructorMarker) {
        this(declarationDescriptor, functionInvokeDescriptor, aVar, z10);
    }

    private final FunctionDescriptor y1(List<Name> list) {
        int u7;
        Name name;
        List<o> G0;
        boolean z10;
        int size = l().size() - list.size();
        boolean z11 = true;
        if (size == 0) {
            List<ValueParameterDescriptor> l10 = l();
            k.d(l10, "valueParameters");
            G0 = _Collections.G0(list, l10);
            if (!(G0 instanceof Collection) || !G0.isEmpty()) {
                for (o oVar : G0) {
                    if (!k.a((Name) oVar.a(), ((ValueParameterDescriptor) oVar.b()).getName())) {
                        z10 = false;
                        break;
                    }
                }
            }
            z10 = true;
            if (z10) {
                return this;
            }
        }
        List<ValueParameterDescriptor> l11 = l();
        k.d(l11, "valueParameters");
        u7 = s.u(l11, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (ValueParameterDescriptor valueParameterDescriptor : l11) {
            Name name2 = valueParameterDescriptor.getName();
            k.d(name2, "it.name");
            int j10 = valueParameterDescriptor.j();
            int i10 = j10 - size;
            if (i10 >= 0 && (name = list.get(i10)) != null) {
                name2 = name;
            }
            arrayList.add(valueParameterDescriptor.W(this, name2, j10));
        }
        FunctionDescriptorImpl.c b12 = b1(TypeSubstitutor.f11868b);
        if (!list.isEmpty()) {
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                if (((Name) it.next()) == null) {
                    break;
                }
            }
        }
        z11 = false;
        FunctionDescriptorImpl.c s7 = b12.G(z11).c(arrayList).s(a());
        k.d(s7, "newCopyBuilder(TypeSubst…   .setOriginal(original)");
        FunctionDescriptor V0 = super.V0(s7);
        k.b(V0);
        return V0;
    }

    @Override // sb.FunctionDescriptorImpl, pb.MemberDescriptor
    public boolean D() {
        return false;
    }

    @Override // sb.SimpleFunctionDescriptorImpl, sb.FunctionDescriptorImpl
    protected FunctionDescriptorImpl U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, g gVar, SourceElement sourceElement) {
        k.e(declarationDescriptor, "newOwner");
        k.e(aVar, "kind");
        k.e(gVar, "annotations");
        k.e(sourceElement, "source");
        return new FunctionInvokeDescriptor(declarationDescriptor, (FunctionInvokeDescriptor) functionDescriptor, aVar, C0());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.FunctionDescriptorImpl
    public FunctionDescriptor V0(FunctionDescriptorImpl.c cVar) {
        int u7;
        k.e(cVar, "configuration");
        FunctionInvokeDescriptor functionInvokeDescriptor = (FunctionInvokeDescriptor) super.V0(cVar);
        if (functionInvokeDescriptor == null) {
            return null;
        }
        List<ValueParameterDescriptor> l10 = functionInvokeDescriptor.l();
        k.d(l10, "substituted.valueParameters");
        boolean z10 = false;
        if (!(l10 instanceof Collection) || !l10.isEmpty()) {
            Iterator<T> it = l10.iterator();
            while (it.hasNext()) {
                g0 type = ((ValueParameterDescriptor) it.next()).getType();
                k.d(type, "it.type");
                if (functionTypes.d(type) != null) {
                    break;
                }
            }
        }
        z10 = true;
        if (z10) {
            return functionInvokeDescriptor;
        }
        List<ValueParameterDescriptor> l11 = functionInvokeDescriptor.l();
        k.d(l11, "substituted.valueParameters");
        u7 = s.u(l11, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it2 = l11.iterator();
        while (it2.hasNext()) {
            g0 type2 = ((ValueParameterDescriptor) it2.next()).getType();
            k.d(type2, "it.type");
            arrayList.add(functionTypes.d(type2));
        }
        return functionInvokeDescriptor.y1(arrayList);
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor
    public boolean X() {
        return false;
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor
    public boolean y() {
        return false;
    }
}
