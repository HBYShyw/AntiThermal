package sb;

import ad.ExtensionReceiver;
import ad.ImplicitReceiver;
import gd.DescriptorSubstitutor;
import gd.TypeSubstitutor;
import gd.Variance;
import gd.n1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections._Collections;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.VariableDescriptor;
import qd.SmartList;
import sb.ValueParameterDescriptorImpl;
import sc.DescriptorFactory;

/* compiled from: FunctionDescriptorImpl.java */
/* renamed from: sb.p, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class FunctionDescriptorImpl extends DeclarationDescriptorNonRootImpl implements FunctionDescriptor {
    private boolean A;
    private boolean B;
    private Collection<? extends FunctionDescriptor> C;
    private volatile ya.a<Collection<FunctionDescriptor>> D;
    private final FunctionDescriptor E;
    private final CallableMemberDescriptor.a F;
    private FunctionDescriptor G;
    protected Map<CallableDescriptor.a<?>, Object> H;

    /* renamed from: i, reason: collision with root package name */
    private List<TypeParameterDescriptor> f18326i;

    /* renamed from: j, reason: collision with root package name */
    private List<ValueParameterDescriptor> f18327j;

    /* renamed from: k, reason: collision with root package name */
    private gd.g0 f18328k;

    /* renamed from: l, reason: collision with root package name */
    private List<ReceiverParameterDescriptor> f18329l;

    /* renamed from: m, reason: collision with root package name */
    private ReceiverParameterDescriptor f18330m;

    /* renamed from: n, reason: collision with root package name */
    private ReceiverParameterDescriptor f18331n;

    /* renamed from: o, reason: collision with root package name */
    private Modality f18332o;

    /* renamed from: p, reason: collision with root package name */
    private pb.u f18333p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f18334q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f18335r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f18336s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f18337t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f18338u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f18339v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f18340w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f18341x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f18342y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f18343z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FunctionDescriptorImpl.java */
    /* renamed from: sb.p$a */
    /* loaded from: classes2.dex */
    public class a implements ya.a<Collection<FunctionDescriptor>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ TypeSubstitutor f18344e;

        a(TypeSubstitutor typeSubstitutor) {
            this.f18344e = typeSubstitutor;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Collection<FunctionDescriptor> invoke() {
            SmartList smartList = new SmartList();
            Iterator<? extends FunctionDescriptor> it = FunctionDescriptorImpl.this.e().iterator();
            while (it.hasNext()) {
                smartList.add(it.next().c(this.f18344e));
            }
            return smartList;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FunctionDescriptorImpl.java */
    /* renamed from: sb.p$b */
    /* loaded from: classes2.dex */
    public static class b implements ya.a<List<VariableDescriptor>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ List f18346e;

        b(List list) {
            this.f18346e = list;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public List<VariableDescriptor> invoke() {
            return this.f18346e;
        }
    }

    /* compiled from: FunctionDescriptorImpl.java */
    /* renamed from: sb.p$c */
    /* loaded from: classes2.dex */
    public class c implements FunctionDescriptor.a<FunctionDescriptor> {

        /* renamed from: a, reason: collision with root package name */
        protected n1 f18347a;

        /* renamed from: b, reason: collision with root package name */
        protected DeclarationDescriptor f18348b;

        /* renamed from: c, reason: collision with root package name */
        protected Modality f18349c;

        /* renamed from: d, reason: collision with root package name */
        protected pb.u f18350d;

        /* renamed from: e, reason: collision with root package name */
        protected FunctionDescriptor f18351e;

        /* renamed from: f, reason: collision with root package name */
        protected CallableMemberDescriptor.a f18352f;

        /* renamed from: g, reason: collision with root package name */
        protected List<ValueParameterDescriptor> f18353g;

        /* renamed from: h, reason: collision with root package name */
        protected List<ReceiverParameterDescriptor> f18354h;

        /* renamed from: i, reason: collision with root package name */
        protected ReceiverParameterDescriptor f18355i;

        /* renamed from: j, reason: collision with root package name */
        protected ReceiverParameterDescriptor f18356j;

        /* renamed from: k, reason: collision with root package name */
        protected gd.g0 f18357k;

        /* renamed from: l, reason: collision with root package name */
        protected Name f18358l;

        /* renamed from: m, reason: collision with root package name */
        protected boolean f18359m;

        /* renamed from: n, reason: collision with root package name */
        protected boolean f18360n;

        /* renamed from: o, reason: collision with root package name */
        protected boolean f18361o;

        /* renamed from: p, reason: collision with root package name */
        protected boolean f18362p;

        /* renamed from: q, reason: collision with root package name */
        private boolean f18363q;

        /* renamed from: r, reason: collision with root package name */
        private List<TypeParameterDescriptor> f18364r;

        /* renamed from: s, reason: collision with root package name */
        private qb.g f18365s;

        /* renamed from: t, reason: collision with root package name */
        private boolean f18366t;

        /* renamed from: u, reason: collision with root package name */
        private Map<CallableDescriptor.a<?>, Object> f18367u;

        /* renamed from: v, reason: collision with root package name */
        private Boolean f18368v;

        /* renamed from: w, reason: collision with root package name */
        protected boolean f18369w;

        /* renamed from: x, reason: collision with root package name */
        final /* synthetic */ FunctionDescriptorImpl f18370x;

        public c(FunctionDescriptorImpl functionDescriptorImpl, n1 n1Var, DeclarationDescriptor declarationDescriptor, Modality modality, pb.u uVar, CallableMemberDescriptor.a aVar, List<ValueParameterDescriptor> list, List<ReceiverParameterDescriptor> list2, ReceiverParameterDescriptor receiverParameterDescriptor, gd.g0 g0Var, Name name) {
            if (n1Var == null) {
                u(0);
            }
            if (declarationDescriptor == null) {
                u(1);
            }
            if (modality == null) {
                u(2);
            }
            if (uVar == null) {
                u(3);
            }
            if (aVar == null) {
                u(4);
            }
            if (list == null) {
                u(5);
            }
            if (list2 == null) {
                u(6);
            }
            if (g0Var == null) {
                u(7);
            }
            this.f18370x = functionDescriptorImpl;
            this.f18351e = null;
            this.f18356j = functionDescriptorImpl.f18331n;
            this.f18359m = true;
            this.f18360n = false;
            this.f18361o = false;
            this.f18362p = false;
            this.f18363q = functionDescriptorImpl.B0();
            this.f18364r = null;
            this.f18365s = null;
            this.f18366t = functionDescriptorImpl.L0();
            this.f18367u = new LinkedHashMap();
            this.f18368v = null;
            this.f18369w = false;
            this.f18347a = n1Var;
            this.f18348b = declarationDescriptor;
            this.f18349c = modality;
            this.f18350d = uVar;
            this.f18352f = aVar;
            this.f18353g = list;
            this.f18354h = list2;
            this.f18355i = receiverParameterDescriptor;
            this.f18357k = g0Var;
            this.f18358l = name;
        }

        private static /* synthetic */ void u(int i10) {
            String str;
            int i11;
            switch (i10) {
                case 9:
                case 11:
                case 13:
                case 15:
                case 16:
                case 18:
                case 20:
                case 22:
                case 24:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 36:
                case 38:
                case 40:
                case 41:
                case 42:
                    str = "@NotNull method %s.%s must not return null";
                    break;
                case 10:
                case 12:
                case 14:
                case 17:
                case 19:
                case 21:
                case 23:
                case 25:
                case 35:
                case 37:
                case 39:
                default:
                    str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                    break;
            }
            switch (i10) {
                case 9:
                case 11:
                case 13:
                case 15:
                case 16:
                case 18:
                case 20:
                case 22:
                case 24:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 36:
                case 38:
                case 40:
                case 41:
                case 42:
                    i11 = 2;
                    break;
                case 10:
                case 12:
                case 14:
                case 17:
                case 19:
                case 21:
                case 23:
                case 25:
                case 35:
                case 37:
                case 39:
                default:
                    i11 = 3;
                    break;
            }
            Object[] objArr = new Object[i11];
            switch (i10) {
                case 1:
                    objArr[0] = "newOwner";
                    break;
                case 2:
                    objArr[0] = "newModality";
                    break;
                case 3:
                    objArr[0] = "newVisibility";
                    break;
                case 4:
                case 14:
                    objArr[0] = "kind";
                    break;
                case 5:
                    objArr[0] = "newValueParameterDescriptors";
                    break;
                case 6:
                    objArr[0] = "newContextReceiverParameters";
                    break;
                case 7:
                    objArr[0] = "newReturnType";
                    break;
                case 8:
                    objArr[0] = "owner";
                    break;
                case 9:
                case 11:
                case 13:
                case 15:
                case 16:
                case 18:
                case 20:
                case 22:
                case 24:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 36:
                case 38:
                case 40:
                case 41:
                case 42:
                    objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/FunctionDescriptorImpl$CopyConfiguration";
                    break;
                case 10:
                    objArr[0] = "modality";
                    break;
                case 12:
                    objArr[0] = "visibility";
                    break;
                case 17:
                    objArr[0] = "name";
                    break;
                case 19:
                case 21:
                    objArr[0] = "parameters";
                    break;
                case 23:
                    objArr[0] = "type";
                    break;
                case 25:
                    objArr[0] = "contextReceiverParameters";
                    break;
                case 35:
                    objArr[0] = "additionalAnnotations";
                    break;
                case 37:
                default:
                    objArr[0] = "substitution";
                    break;
                case 39:
                    objArr[0] = "userDataKey";
                    break;
            }
            switch (i10) {
                case 9:
                    objArr[1] = "setOwner";
                    break;
                case 10:
                case 12:
                case 14:
                case 17:
                case 19:
                case 21:
                case 23:
                case 25:
                case 35:
                case 37:
                case 39:
                default:
                    objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/FunctionDescriptorImpl$CopyConfiguration";
                    break;
                case 11:
                    objArr[1] = "setModality";
                    break;
                case 13:
                    objArr[1] = "setVisibility";
                    break;
                case 15:
                    objArr[1] = "setKind";
                    break;
                case 16:
                    objArr[1] = "setCopyOverrides";
                    break;
                case 18:
                    objArr[1] = "setName";
                    break;
                case 20:
                    objArr[1] = "setValueParameters";
                    break;
                case 22:
                    objArr[1] = "setTypeParameters";
                    break;
                case 24:
                    objArr[1] = "setReturnType";
                    break;
                case 26:
                    objArr[1] = "setContextReceiverParameters";
                    break;
                case 27:
                    objArr[1] = "setExtensionReceiverParameter";
                    break;
                case 28:
                    objArr[1] = "setDispatchReceiverParameter";
                    break;
                case 29:
                    objArr[1] = "setOriginal";
                    break;
                case 30:
                    objArr[1] = "setSignatureChange";
                    break;
                case 31:
                    objArr[1] = "setPreserveSourceElement";
                    break;
                case 32:
                    objArr[1] = "setDropOriginalInContainingParts";
                    break;
                case 33:
                    objArr[1] = "setHiddenToOvercomeSignatureClash";
                    break;
                case 34:
                    objArr[1] = "setHiddenForResolutionEverywhereBesideSupercalls";
                    break;
                case 36:
                    objArr[1] = "setAdditionalAnnotations";
                    break;
                case 38:
                    objArr[1] = "setSubstitution";
                    break;
                case 40:
                    objArr[1] = "putUserData";
                    break;
                case 41:
                    objArr[1] = "getSubstitution";
                    break;
                case 42:
                    objArr[1] = "setJustForTypeSubstitution";
                    break;
            }
            switch (i10) {
                case 8:
                    objArr[2] = "setOwner";
                    break;
                case 9:
                case 11:
                case 13:
                case 15:
                case 16:
                case 18:
                case 20:
                case 22:
                case 24:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 36:
                case 38:
                case 40:
                case 41:
                case 42:
                    break;
                case 10:
                    objArr[2] = "setModality";
                    break;
                case 12:
                    objArr[2] = "setVisibility";
                    break;
                case 14:
                    objArr[2] = "setKind";
                    break;
                case 17:
                    objArr[2] = "setName";
                    break;
                case 19:
                    objArr[2] = "setValueParameters";
                    break;
                case 21:
                    objArr[2] = "setTypeParameters";
                    break;
                case 23:
                    objArr[2] = "setReturnType";
                    break;
                case 25:
                    objArr[2] = "setContextReceiverParameters";
                    break;
                case 35:
                    objArr[2] = "setAdditionalAnnotations";
                    break;
                case 37:
                    objArr[2] = "setSubstitution";
                    break;
                case 39:
                    objArr[2] = "putUserData";
                    break;
                default:
                    objArr[2] = "<init>";
                    break;
            }
            String format = String.format(str, objArr);
            switch (i10) {
                case 9:
                case 11:
                case 13:
                case 15:
                case 16:
                case 18:
                case 20:
                case 22:
                case 24:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 36:
                case 38:
                case 40:
                case 41:
                case 42:
                    throw new IllegalStateException(format);
                case 10:
                case 12:
                case 14:
                case 17:
                case 19:
                case 21:
                case 23:
                case 25:
                case 35:
                case 37:
                case 39:
                default:
                    throw new IllegalArgumentException(format);
            }
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: B, reason: merged with bridge method [inline-methods] */
        public c b(qb.g gVar) {
            if (gVar == null) {
                u(35);
            }
            this.f18365s = gVar;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: C, reason: merged with bridge method [inline-methods] */
        public c l(boolean z10) {
            this.f18359m = z10;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: D, reason: merged with bridge method [inline-methods] */
        public c g(ReceiverParameterDescriptor receiverParameterDescriptor) {
            this.f18356j = receiverParameterDescriptor;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: E, reason: merged with bridge method [inline-methods] */
        public c a() {
            this.f18362p = true;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: F, reason: merged with bridge method [inline-methods] */
        public c j(ReceiverParameterDescriptor receiverParameterDescriptor) {
            this.f18355i = receiverParameterDescriptor;
            return this;
        }

        public c G(boolean z10) {
            this.f18368v = Boolean.valueOf(z10);
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: H, reason: merged with bridge method [inline-methods] */
        public c k() {
            this.f18366t = true;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: I, reason: merged with bridge method [inline-methods] */
        public c o() {
            this.f18363q = true;
            return this;
        }

        public c J(boolean z10) {
            this.f18369w = z10;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: K, reason: merged with bridge method [inline-methods] */
        public c q(CallableMemberDescriptor.a aVar) {
            if (aVar == null) {
                u(14);
            }
            this.f18352f = aVar;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: L, reason: merged with bridge method [inline-methods] */
        public c f(Modality modality) {
            if (modality == null) {
                u(10);
            }
            this.f18349c = modality;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: M, reason: merged with bridge method [inline-methods] */
        public c m(Name name) {
            if (name == null) {
                u(17);
            }
            this.f18358l = name;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: N, reason: merged with bridge method [inline-methods] */
        public c s(CallableMemberDescriptor callableMemberDescriptor) {
            this.f18351e = (FunctionDescriptor) callableMemberDescriptor;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: O, reason: merged with bridge method [inline-methods] */
        public c r(DeclarationDescriptor declarationDescriptor) {
            if (declarationDescriptor == null) {
                u(8);
            }
            this.f18348b = declarationDescriptor;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: P, reason: merged with bridge method [inline-methods] */
        public c h() {
            this.f18361o = true;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: Q, reason: merged with bridge method [inline-methods] */
        public c e(gd.g0 g0Var) {
            if (g0Var == null) {
                u(23);
            }
            this.f18357k = g0Var;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: R, reason: merged with bridge method [inline-methods] */
        public c t() {
            this.f18360n = true;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: S, reason: merged with bridge method [inline-methods] */
        public c d(n1 n1Var) {
            if (n1Var == null) {
                u(37);
            }
            this.f18347a = n1Var;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: T, reason: merged with bridge method [inline-methods] */
        public c n(List<TypeParameterDescriptor> list) {
            if (list == null) {
                u(21);
            }
            this.f18364r = list;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: U, reason: merged with bridge method [inline-methods] */
        public c c(List<ValueParameterDescriptor> list) {
            if (list == null) {
                u(19);
            }
            this.f18353g = list;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: V, reason: merged with bridge method [inline-methods] */
        public c p(pb.u uVar) {
            if (uVar == null) {
                u(12);
            }
            this.f18350d = uVar;
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor build() {
            return this.f18370x.V0(this);
        }

        @Override // pb.FunctionDescriptor.a
        public <V> FunctionDescriptor.a<FunctionDescriptor> i(CallableDescriptor.a<V> aVar, V v7) {
            if (aVar == null) {
                u(39);
            }
            this.f18367u.put(aVar, v7);
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FunctionDescriptorImpl(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, qb.g gVar, Name name, CallableMemberDescriptor.a aVar, SourceElement sourceElement) {
        super(declarationDescriptor, gVar, name, sourceElement);
        if (declarationDescriptor == null) {
            P(0);
        }
        if (gVar == null) {
            P(1);
        }
        if (name == null) {
            P(2);
        }
        if (aVar == null) {
            P(3);
        }
        if (sourceElement == null) {
            P(4);
        }
        this.f18333p = DescriptorVisibilities.f16737i;
        this.f18334q = false;
        this.f18335r = false;
        this.f18336s = false;
        this.f18337t = false;
        this.f18338u = false;
        this.f18339v = false;
        this.f18340w = false;
        this.f18341x = false;
        this.f18342y = false;
        this.f18343z = false;
        this.A = true;
        this.B = false;
        this.C = null;
        this.D = null;
        this.G = null;
        this.H = null;
        this.E = functionDescriptor == null ? this : functionDescriptor;
        this.F = aVar;
    }

    private static /* synthetic */ void P(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 9:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 26:
            case 27:
                str = "@NotNull method %s.%s must not return null";
                break;
            case 10:
            case 11:
            case 12:
            case 17:
            case 22:
            case 24:
            case 25:
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 9:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 26:
            case 27:
                i11 = 2;
                break;
            case 10:
            case 11:
            case 12:
            case 17:
            case 22:
            case 24:
            case 25:
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
                objArr[0] = "annotations";
                break;
            case 2:
                objArr[0] = "name";
                break;
            case 3:
                objArr[0] = "kind";
                break;
            case 4:
                objArr[0] = "source";
                break;
            case 5:
                objArr[0] = "contextReceiverParameters";
                break;
            case 6:
                objArr[0] = "typeParameters";
                break;
            case 7:
            case 28:
            case 30:
                objArr[0] = "unsubstitutedValueParameters";
                break;
            case 8:
            case 10:
                objArr[0] = "visibility";
                break;
            case 9:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 26:
            case 27:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/FunctionDescriptorImpl";
                break;
            case 11:
                objArr[0] = "unsubstitutedReturnType";
                break;
            case 12:
                objArr[0] = "extensionReceiverParameter";
                break;
            case 17:
                objArr[0] = "overriddenDescriptors";
                break;
            case 22:
                objArr[0] = "originalSubstitutor";
                break;
            case 24:
            case 29:
            case 31:
                objArr[0] = "substitutor";
                break;
            case 25:
                objArr[0] = "configuration";
                break;
            default:
                objArr[0] = "containingDeclaration";
                break;
        }
        switch (i10) {
            case 9:
                objArr[1] = "initialize";
                break;
            case 10:
            case 11:
            case 12:
            case 17:
            case 22:
            case 24:
            case 25:
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/FunctionDescriptorImpl";
                break;
            case 13:
                objArr[1] = "getContextReceiverParameters";
                break;
            case 14:
                objArr[1] = "getOverriddenDescriptors";
                break;
            case 15:
                objArr[1] = "getModality";
                break;
            case 16:
                objArr[1] = "getVisibility";
                break;
            case 18:
                objArr[1] = "getTypeParameters";
                break;
            case 19:
                objArr[1] = "getValueParameters";
                break;
            case 20:
                objArr[1] = "getOriginal";
                break;
            case 21:
                objArr[1] = "getKind";
                break;
            case 23:
                objArr[1] = "newCopyBuilder";
                break;
            case 26:
                objArr[1] = "copy";
                break;
            case 27:
                objArr[1] = "getSourceToUseForCopy";
                break;
        }
        switch (i10) {
            case 5:
            case 6:
            case 7:
            case 8:
                objArr[2] = "initialize";
                break;
            case 9:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 26:
            case 27:
                break;
            case 10:
                objArr[2] = "setVisibility";
                break;
            case 11:
                objArr[2] = "setReturnType";
                break;
            case 12:
                objArr[2] = "setExtensionReceiverParameter";
                break;
            case 17:
                objArr[2] = "setOverriddenDescriptors";
                break;
            case 22:
                objArr[2] = "substitute";
                break;
            case 24:
                objArr[2] = "newCopyBuilder";
                break;
            case 25:
                objArr[2] = "doSubstitute";
                break;
            case 28:
            case 29:
            case 30:
            case 31:
                objArr[2] = "getSubstitutedValueParameters";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 9:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 26:
            case 27:
                throw new IllegalStateException(format);
            case 10:
            case 11:
            case 12:
            case 17:
            case 22:
            case 24:
            case 25:
            default:
                throw new IllegalArgumentException(format);
        }
    }

    private SourceElement W0(boolean z10, FunctionDescriptor functionDescriptor) {
        SourceElement sourceElement;
        if (z10) {
            if (functionDescriptor == null) {
                functionDescriptor = a();
            }
            sourceElement = functionDescriptor.z();
        } else {
            sourceElement = SourceElement.f16664a;
        }
        if (sourceElement == null) {
            P(27);
        }
        return sourceElement;
    }

    public static List<ValueParameterDescriptor> X0(FunctionDescriptor functionDescriptor, List<ValueParameterDescriptor> list, TypeSubstitutor typeSubstitutor) {
        if (list == null) {
            P(28);
        }
        if (typeSubstitutor == null) {
            P(29);
        }
        return Y0(functionDescriptor, list, typeSubstitutor, false, false, null);
    }

    public static List<ValueParameterDescriptor> Y0(FunctionDescriptor functionDescriptor, List<ValueParameterDescriptor> list, TypeSubstitutor typeSubstitutor, boolean z10, boolean z11, boolean[] zArr) {
        if (list == null) {
            P(30);
        }
        if (typeSubstitutor == null) {
            P(31);
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (ValueParameterDescriptor valueParameterDescriptor : list) {
            gd.g0 type = valueParameterDescriptor.getType();
            Variance variance = Variance.IN_VARIANCE;
            gd.g0 p10 = typeSubstitutor.p(type, variance);
            gd.g0 q02 = valueParameterDescriptor.q0();
            gd.g0 p11 = q02 == null ? null : typeSubstitutor.p(q02, variance);
            if (p10 == null) {
                return null;
            }
            if ((p10 != valueParameterDescriptor.getType() || q02 != p11) && zArr != null) {
                zArr[0] = true;
            }
            arrayList.add(ValueParameterDescriptorImpl.T0(functionDescriptor, z10 ? null : valueParameterDescriptor, valueParameterDescriptor.j(), valueParameterDescriptor.i(), valueParameterDescriptor.getName(), p10, valueParameterDescriptor.z0(), valueParameterDescriptor.i0(), valueParameterDescriptor.g0(), p11, z11 ? valueParameterDescriptor.z() : SourceElement.f16664a, valueParameterDescriptor instanceof ValueParameterDescriptorImpl.b ? new b(((ValueParameterDescriptorImpl.b) valueParameterDescriptor).W0()) : null));
        }
        return arrayList;
    }

    private void c1() {
        ya.a<Collection<FunctionDescriptor>> aVar = this.D;
        if (aVar != null) {
            this.C = aVar.invoke();
            this.D = null;
        }
    }

    private void j1(boolean z10) {
        this.f18342y = z10;
    }

    private void k1(boolean z10) {
        this.f18341x = z10;
    }

    private void m1(FunctionDescriptor functionDescriptor) {
        this.G = functionDescriptor;
    }

    public FunctionDescriptor.a<? extends FunctionDescriptor> A() {
        c b12 = b1(TypeSubstitutor.f11868b);
        if (b12 == null) {
            P(23);
        }
        return b12;
    }

    @Override // pb.FunctionDescriptor
    public boolean B0() {
        return this.f18341x;
    }

    public boolean C0() {
        return this.f18343z;
    }

    public boolean D() {
        return this.f18336s;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void D0(Collection<? extends CallableMemberDescriptor> collection) {
        if (collection == 0) {
            P(17);
        }
        this.C = collection;
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            if (((FunctionDescriptor) it.next()).L0()) {
                this.f18342y = true;
                return;
            }
        }
    }

    public <V> V E(CallableDescriptor.a<V> aVar) {
        Map<CallableDescriptor.a<?>, Object> map = this.H;
        if (map == null) {
            return null;
        }
        return (V) map.get(aVar);
    }

    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return declarationDescriptorVisitor.h(this, d10);
    }

    @Override // pb.FunctionDescriptor
    public boolean L0() {
        return this.f18342y;
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return this.f18340w;
    }

    public boolean O() {
        return this.B;
    }

    @Override // pb.FunctionDescriptor
    public boolean Q0() {
        if (this.f18335r) {
            return true;
        }
        Iterator<? extends FunctionDescriptor> it = a().e().iterator();
        while (it.hasNext()) {
            if (it.next().Q0()) {
                return true;
            }
        }
        return false;
    }

    @Override // pb.CallableMemberDescriptor
    public FunctionDescriptor T0(DeclarationDescriptor declarationDescriptor, Modality modality, pb.u uVar, CallableMemberDescriptor.a aVar, boolean z10) {
        FunctionDescriptor build = A().r(declarationDescriptor).f(modality).p(uVar).q(aVar).l(z10).build();
        if (build == null) {
            P(26);
        }
        return build;
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        return this.f18339v;
    }

    protected abstract FunctionDescriptorImpl U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, qb.g gVar, SourceElement sourceElement);

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public FunctionDescriptor V0(c cVar) {
        qb.g i10;
        ReceiverParameterDescriptorImpl receiverParameterDescriptorImpl;
        ReceiverParameterDescriptor receiverParameterDescriptor;
        gd.g0 p10;
        if (cVar == null) {
            P(25);
        }
        boolean[] zArr = new boolean[1];
        if (cVar.f18365s != null) {
            i10 = qb.i.a(i(), cVar.f18365s);
        } else {
            i10 = i();
        }
        DeclarationDescriptor declarationDescriptor = cVar.f18348b;
        FunctionDescriptor functionDescriptor = cVar.f18351e;
        FunctionDescriptorImpl U0 = U0(declarationDescriptor, functionDescriptor, cVar.f18352f, cVar.f18358l, i10, W0(cVar.f18361o, functionDescriptor));
        List<TypeParameterDescriptor> m10 = cVar.f18364r == null ? m() : cVar.f18364r;
        zArr[0] = zArr[0] | (!m10.isEmpty());
        ArrayList arrayList = new ArrayList(m10.size());
        TypeSubstitutor c10 = DescriptorSubstitutor.c(m10, cVar.f18347a, U0, arrayList, zArr);
        if (c10 == null) {
            return null;
        }
        ArrayList arrayList2 = new ArrayList();
        if (!cVar.f18354h.isEmpty()) {
            for (ReceiverParameterDescriptor receiverParameterDescriptor2 : cVar.f18354h) {
                gd.g0 p11 = c10.p(receiverParameterDescriptor2.getType(), Variance.IN_VARIANCE);
                if (p11 == null) {
                    return null;
                }
                arrayList2.add(DescriptorFactory.b(U0, p11, ((ImplicitReceiver) receiverParameterDescriptor2.getValue()).a(), receiverParameterDescriptor2.i()));
                zArr[0] = (p11 != receiverParameterDescriptor2.getType()) | zArr[0];
            }
        }
        ReceiverParameterDescriptor receiverParameterDescriptor3 = cVar.f18355i;
        if (receiverParameterDescriptor3 != null) {
            gd.g0 p12 = c10.p(receiverParameterDescriptor3.getType(), Variance.IN_VARIANCE);
            if (p12 == null) {
                return null;
            }
            ReceiverParameterDescriptorImpl receiverParameterDescriptorImpl2 = new ReceiverParameterDescriptorImpl(U0, new ExtensionReceiver(U0, p12, cVar.f18355i.getValue()), cVar.f18355i.i());
            zArr[0] = (p12 != cVar.f18355i.getType()) | zArr[0];
            receiverParameterDescriptorImpl = receiverParameterDescriptorImpl2;
        } else {
            receiverParameterDescriptorImpl = null;
        }
        ReceiverParameterDescriptor receiverParameterDescriptor4 = cVar.f18356j;
        if (receiverParameterDescriptor4 != 0) {
            ReceiverParameterDescriptor c11 = receiverParameterDescriptor4.c(c10);
            if (c11 == null) {
                return null;
            }
            zArr[0] = zArr[0] | (c11 != cVar.f18356j);
            receiverParameterDescriptor = c11;
        } else {
            receiverParameterDescriptor = null;
        }
        List<ValueParameterDescriptor> Y0 = Y0(U0, cVar.f18353g, c10, cVar.f18362p, cVar.f18361o, zArr);
        if (Y0 == null || (p10 = c10.p(cVar.f18357k, Variance.OUT_VARIANCE)) == null) {
            return null;
        }
        zArr[0] = zArr[0] | (p10 != cVar.f18357k);
        if (!zArr[0] && cVar.f18369w) {
            return this;
        }
        U0.a1(receiverParameterDescriptorImpl, receiverParameterDescriptor, arrayList2, arrayList, Y0, p10, cVar.f18349c, cVar.f18350d);
        U0.o1(this.f18334q);
        U0.l1(this.f18335r);
        U0.g1(this.f18336s);
        U0.n1(this.f18337t);
        U0.r1(this.f18338u);
        U0.q1(this.f18343z);
        U0.f1(this.f18339v);
        U0.e1(this.f18340w);
        U0.h1(this.A);
        U0.k1(cVar.f18363q);
        U0.j1(cVar.f18366t);
        U0.i1(cVar.f18368v != null ? cVar.f18368v.booleanValue() : this.B);
        if (!cVar.f18367u.isEmpty() || this.H != null) {
            Map<CallableDescriptor.a<?>, Object> map = cVar.f18367u;
            Map<CallableDescriptor.a<?>, Object> map2 = this.H;
            if (map2 != null) {
                for (Map.Entry<CallableDescriptor.a<?>, Object> entry : map2.entrySet()) {
                    if (!map.containsKey(entry.getKey())) {
                        map.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            if (map.size() == 1) {
                U0.H = Collections.singletonMap(map.keySet().iterator().next(), map.values().iterator().next());
            } else {
                U0.H = map;
            }
        }
        if (cVar.f18360n || l0() != null) {
            U0.m1((l0() != null ? l0() : this).c(c10));
        }
        if (cVar.f18359m && !a().e().isEmpty()) {
            if (cVar.f18347a.f()) {
                ya.a<Collection<FunctionDescriptor>> aVar = this.D;
                if (aVar != null) {
                    U0.D = aVar;
                } else {
                    U0.D0(e());
                }
            } else {
                U0.D = new a(c10);
            }
        }
        return U0;
    }

    public boolean X() {
        return this.f18338u;
    }

    @Override // pb.FunctionDescriptor
    public boolean Y() {
        if (this.f18334q) {
            return true;
        }
        Iterator<? extends FunctionDescriptor> it = a().e().iterator();
        while (it.hasNext()) {
            if (it.next().Y()) {
                return true;
            }
        }
        return false;
    }

    public boolean Z0() {
        return this.A;
    }

    public FunctionDescriptorImpl a1(ReceiverParameterDescriptor receiverParameterDescriptor, ReceiverParameterDescriptor receiverParameterDescriptor2, List<ReceiverParameterDescriptor> list, List<? extends TypeParameterDescriptor> list2, List<ValueParameterDescriptor> list3, gd.g0 g0Var, Modality modality, pb.u uVar) {
        List<TypeParameterDescriptor> z02;
        List<ValueParameterDescriptor> z03;
        if (list == null) {
            P(5);
        }
        if (list2 == null) {
            P(6);
        }
        if (list3 == null) {
            P(7);
        }
        if (uVar == null) {
            P(8);
        }
        z02 = _Collections.z0(list2);
        this.f18326i = z02;
        z03 = _Collections.z0(list3);
        this.f18327j = z03;
        this.f18328k = g0Var;
        this.f18332o = modality;
        this.f18333p = uVar;
        this.f18330m = receiverParameterDescriptor;
        this.f18331n = receiverParameterDescriptor2;
        this.f18329l = list;
        for (int i10 = 0; i10 < list2.size(); i10++) {
            TypeParameterDescriptor typeParameterDescriptor = list2.get(i10);
            if (typeParameterDescriptor.j() != i10) {
                throw new IllegalStateException(typeParameterDescriptor + " index is " + typeParameterDescriptor.j() + " but position is " + i10);
            }
        }
        for (int i11 = 0; i11 < list3.size(); i11++) {
            ValueParameterDescriptor valueParameterDescriptor = list3.get(i11);
            if (valueParameterDescriptor.j() != i11 + 0) {
                throw new IllegalStateException(valueParameterDescriptor + "index is " + valueParameterDescriptor.j() + " but position is " + i11);
            }
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public c b1(TypeSubstitutor typeSubstitutor) {
        if (typeSubstitutor == null) {
            P(24);
        }
        return new c(this, typeSubstitutor.j(), b(), o(), g(), getKind(), l(), w0(), r0(), f(), null);
    }

    public <V> void d1(CallableDescriptor.a<V> aVar, Object obj) {
        if (this.H == null) {
            this.H = new LinkedHashMap();
        }
        this.H.put(aVar, obj);
    }

    public Collection<? extends FunctionDescriptor> e() {
        c1();
        Collection<? extends FunctionDescriptor> collection = this.C;
        if (collection == null) {
            collection = Collections.emptyList();
        }
        if (collection == null) {
            P(14);
        }
        return collection;
    }

    public void e1(boolean z10) {
        this.f18340w = z10;
    }

    public gd.g0 f() {
        return this.f18328k;
    }

    public void f1(boolean z10) {
        this.f18339v = z10;
    }

    @Override // pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        pb.u uVar = this.f18333p;
        if (uVar == null) {
            P(16);
        }
        return uVar;
    }

    public void g1(boolean z10) {
        this.f18336s = z10;
    }

    @Override // pb.CallableMemberDescriptor
    public CallableMemberDescriptor.a getKind() {
        CallableMemberDescriptor.a aVar = this.F;
        if (aVar == null) {
            P(21);
        }
        return aVar;
    }

    public void h1(boolean z10) {
        this.A = z10;
    }

    public void i1(boolean z10) {
        this.B = z10;
    }

    @Override // pb.CallableDescriptor
    public List<ValueParameterDescriptor> l() {
        List<ValueParameterDescriptor> list = this.f18327j;
        if (list == null) {
            P(19);
        }
        return list;
    }

    @Override // pb.FunctionDescriptor
    public FunctionDescriptor l0() {
        return this.G;
    }

    public void l1(boolean z10) {
        this.f18335r = z10;
    }

    @Override // pb.CallableDescriptor
    public List<TypeParameterDescriptor> m() {
        List<TypeParameterDescriptor> list = this.f18326i;
        if (list != null) {
            return list;
        }
        throw new IllegalStateException("typeParameters == null for " + this);
    }

    @Override // pb.CallableDescriptor
    public ReceiverParameterDescriptor m0() {
        return this.f18331n;
    }

    public void n1(boolean z10) {
        this.f18337t = z10;
    }

    @Override // pb.MemberDescriptor
    public Modality o() {
        Modality modality = this.f18332o;
        if (modality == null) {
            P(15);
        }
        return modality;
    }

    public void o1(boolean z10) {
        this.f18334q = z10;
    }

    public void p1(gd.g0 g0Var) {
        if (g0Var == null) {
            P(11);
        }
        this.f18328k = g0Var;
    }

    public void q1(boolean z10) {
        this.f18343z = z10;
    }

    @Override // pb.CallableDescriptor
    public ReceiverParameterDescriptor r0() {
        return this.f18330m;
    }

    public void r1(boolean z10) {
        this.f18338u = z10;
    }

    public void s1(pb.u uVar) {
        if (uVar == null) {
            P(10);
        }
        this.f18333p = uVar;
    }

    @Override // pb.CallableDescriptor
    public List<ReceiverParameterDescriptor> w0() {
        List<ReceiverParameterDescriptor> list = this.f18329l;
        if (list == null) {
            P(13);
        }
        return list;
    }

    public boolean y() {
        return this.f18337t;
    }

    @Override // pb.FunctionDescriptor, pb.Substitutable
    public FunctionDescriptor c(TypeSubstitutor typeSubstitutor) {
        if (typeSubstitutor == null) {
            P(22);
        }
        return typeSubstitutor.k() ? this : b1(typeSubstitutor).s(a()).h().J(true).build();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [pb.y] */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4 */
    @Override // sb.DeclarationDescriptorNonRootImpl, sb.DeclarationDescriptorImpl, pb.DeclarationDescriptor
    public FunctionDescriptor a() {
        FunctionDescriptor functionDescriptor = this.E;
        ?? r12 = this;
        if (functionDescriptor != this) {
            r12 = functionDescriptor.a();
        }
        if (r12 == 0) {
            P(20);
        }
        return r12;
    }
}
