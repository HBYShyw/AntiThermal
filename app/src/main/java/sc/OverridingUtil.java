package sc;

import gd.TypeConstructor;
import gd.d0;
import gd.f1;
import gd.g0;
import gd.i0;
import hd.KotlinTypeChecker;
import hd.KotlinTypePreparator;
import hd.g;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.ServiceLoader;
import java.util.Set;
import kotlin.collections._Collections;
import ma.Unit;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorWithVisibility;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.MemberDescriptor;
import pb.Modality;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.u;
import qd.SmartSet;
import sb.FunctionDescriptorImpl;
import sb.PropertyAccessorDescriptorImpl;
import sb.PropertyDescriptorImpl;
import sc.ExternalOverridabilityCondition;

/* compiled from: OverridingUtil.java */
/* renamed from: sc.k, reason: use source file name */
/* loaded from: classes2.dex */
public class OverridingUtil {

    /* renamed from: e, reason: collision with root package name */
    private static final List<ExternalOverridabilityCondition> f18439e;

    /* renamed from: f, reason: collision with root package name */
    public static final OverridingUtil f18440f;

    /* renamed from: g, reason: collision with root package name */
    private static final KotlinTypeChecker.a f18441g;

    /* renamed from: a, reason: collision with root package name */
    private final hd.g f18442a;

    /* renamed from: b, reason: collision with root package name */
    private final KotlinTypePreparator f18443b;

    /* renamed from: c, reason: collision with root package name */
    private final KotlinTypeChecker.a f18444c;

    /* renamed from: d, reason: collision with root package name */
    private final ya.p<g0, g0, Boolean> f18445d;

    /* compiled from: OverridingUtil.java */
    /* renamed from: sc.k$a */
    /* loaded from: classes2.dex */
    static class a implements KotlinTypeChecker.a {
        a() {
        }

        private static /* synthetic */ void b(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "a";
            } else {
                objArr[0] = "b";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/OverridingUtil$1";
            objArr[2] = "equals";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // hd.KotlinTypeChecker.a
        public boolean a(TypeConstructor typeConstructor, TypeConstructor typeConstructor2) {
            if (typeConstructor == null) {
                b(0);
            }
            if (typeConstructor2 == null) {
                b(1);
            }
            return typeConstructor.equals(typeConstructor2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [D] */
    /* compiled from: OverridingUtil.java */
    /* renamed from: sc.k$b */
    /* loaded from: classes2.dex */
    public static class b<D> implements ya.p<D, D, ma.o<CallableDescriptor, CallableDescriptor>> {
        b() {
        }

        /* JADX WARN: Incorrect types in method signature: (TD;TD;)Lma/o<Lpb/a;Lpb/a;>; */
        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public ma.o invoke(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2) {
            return new ma.o(callableDescriptor, callableDescriptor2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OverridingUtil.java */
    /* renamed from: sc.k$c */
    /* loaded from: classes2.dex */
    public static class c implements ya.l<CallableMemberDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ DeclarationDescriptor f18446e;

        c(DeclarationDescriptor declarationDescriptor) {
            this.f18446e = declarationDescriptor;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            return Boolean.valueOf(callableMemberDescriptor.b() == this.f18446e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OverridingUtil.java */
    /* renamed from: sc.k$d */
    /* loaded from: classes2.dex */
    public static class d implements ya.l<CallableMemberDescriptor, CallableDescriptor> {
        d() {
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public CallableMemberDescriptor invoke(CallableMemberDescriptor callableMemberDescriptor) {
            return callableMemberDescriptor;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OverridingUtil.java */
    /* renamed from: sc.k$e */
    /* loaded from: classes2.dex */
    public static class e implements ya.l<CallableMemberDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ClassDescriptor f18447e;

        e(ClassDescriptor classDescriptor) {
            this.f18447e = classDescriptor;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            boolean z10 = false;
            if (!DescriptorVisibilities.g(callableMemberDescriptor.g()) && DescriptorVisibilities.h(callableMemberDescriptor, this.f18447e, false)) {
                z10 = true;
            }
            return Boolean.valueOf(z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OverridingUtil.java */
    /* renamed from: sc.k$f */
    /* loaded from: classes2.dex */
    public static class f implements ya.l<CallableMemberDescriptor, CallableDescriptor> {
        f() {
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public CallableDescriptor invoke(CallableMemberDescriptor callableMemberDescriptor) {
            return callableMemberDescriptor;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OverridingUtil.java */
    /* renamed from: sc.k$g */
    /* loaded from: classes2.dex */
    public static class g implements ya.l<CallableMemberDescriptor, Unit> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ j f18448e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ CallableMemberDescriptor f18449f;

        g(j jVar, CallableMemberDescriptor callableMemberDescriptor) {
            this.f18448e = jVar;
            this.f18449f = callableMemberDescriptor;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Unit invoke(CallableMemberDescriptor callableMemberDescriptor) {
            this.f18448e.b(this.f18449f, callableMemberDescriptor);
            return Unit.f15173a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OverridingUtil.java */
    /* renamed from: sc.k$h */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class h {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f18450a;

        /* renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f18451b;

        /* renamed from: c, reason: collision with root package name */
        static final /* synthetic */ int[] f18452c;

        static {
            int[] iArr = new int[Modality.values().length];
            f18452c = iArr;
            try {
                iArr[Modality.FINAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f18452c[Modality.SEALED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f18452c[Modality.OPEN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f18452c[Modality.ABSTRACT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[i.a.values().length];
            f18451b = iArr2;
            try {
                iArr2[i.a.OVERRIDABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f18451b[i.a.CONFLICT.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f18451b[i.a.INCOMPATIBLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            int[] iArr3 = new int[ExternalOverridabilityCondition.b.values().length];
            f18450a = iArr3;
            try {
                iArr3[ExternalOverridabilityCondition.b.OVERRIDABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f18450a[ExternalOverridabilityCondition.b.CONFLICT.ordinal()] = 2;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f18450a[ExternalOverridabilityCondition.b.INCOMPATIBLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                f18450a[ExternalOverridabilityCondition.b.UNKNOWN.ordinal()] = 4;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    /* compiled from: OverridingUtil.java */
    /* renamed from: sc.k$i */
    /* loaded from: classes2.dex */
    public static class i {

        /* renamed from: c, reason: collision with root package name */
        private static final i f18453c = new i(a.OVERRIDABLE, "SUCCESS");

        /* renamed from: a, reason: collision with root package name */
        private final a f18454a;

        /* renamed from: b, reason: collision with root package name */
        private final String f18455b;

        /* compiled from: OverridingUtil.java */
        /* renamed from: sc.k$i$a */
        /* loaded from: classes2.dex */
        public enum a {
            OVERRIDABLE,
            INCOMPATIBLE,
            CONFLICT
        }

        public i(a aVar, String str) {
            if (aVar == null) {
                a(3);
            }
            if (str == null) {
                a(4);
            }
            this.f18454a = aVar;
            this.f18455b = str;
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0038  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0049  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x005a  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x003b  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x0040  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0045  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private static /* synthetic */ void a(int i10) {
            String format;
            String str = (i10 == 1 || i10 == 2 || i10 == 3 || i10 == 4) ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
            Object[] objArr = new Object[(i10 == 1 || i10 == 2 || i10 == 3 || i10 == 4) ? 3 : 2];
            if (i10 != 1 && i10 != 2) {
                if (i10 == 3) {
                    objArr[0] = "success";
                } else if (i10 != 4) {
                    objArr[0] = "kotlin/reflect/jvm/internal/impl/resolve/OverridingUtil$OverrideCompatibilityInfo";
                }
                switch (i10) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/OverridingUtil$OverrideCompatibilityInfo";
                        break;
                    case 5:
                        objArr[1] = "getResult";
                        break;
                    case 6:
                        objArr[1] = "getDebugMessage";
                        break;
                    default:
                        objArr[1] = "success";
                        break;
                }
                if (i10 != 1) {
                    objArr[2] = "incompatible";
                } else if (i10 == 2) {
                    objArr[2] = "conflict";
                } else if (i10 == 3 || i10 == 4) {
                    objArr[2] = "<init>";
                }
                format = String.format(str, objArr);
                if (i10 == 1 && i10 != 2 && i10 != 3 && i10 != 4) {
                    throw new IllegalStateException(format);
                }
                throw new IllegalArgumentException(format);
            }
            objArr[0] = "debugMessage";
            switch (i10) {
            }
            if (i10 != 1) {
            }
            format = String.format(str, objArr);
            if (i10 == 1) {
            }
            throw new IllegalArgumentException(format);
        }

        public static i b(String str) {
            if (str == null) {
                a(2);
            }
            return new i(a.CONFLICT, str);
        }

        public static i d(String str) {
            if (str == null) {
                a(1);
            }
            return new i(a.INCOMPATIBLE, str);
        }

        public static i e() {
            i iVar = f18453c;
            if (iVar == null) {
                a(0);
            }
            return iVar;
        }

        public a c() {
            a aVar = this.f18454a;
            if (aVar == null) {
                a(5);
            }
            return aVar;
        }
    }

    static {
        List<ExternalOverridabilityCondition> z02;
        z02 = _Collections.z0(ServiceLoader.load(ExternalOverridabilityCondition.class, ExternalOverridabilityCondition.class.getClassLoader()));
        f18439e = z02;
        a aVar = new a();
        f18441g = aVar;
        f18440f = new OverridingUtil(aVar, g.a.f12215a, KotlinTypePreparator.a.f12214a, null);
    }

    private OverridingUtil(KotlinTypeChecker.a aVar, hd.g gVar, KotlinTypePreparator kotlinTypePreparator, ya.p<g0, g0, Boolean> pVar) {
        if (aVar == null) {
            a(5);
        }
        if (gVar == null) {
            a(6);
        }
        if (kotlinTypePreparator == null) {
            a(7);
        }
        this.f18444c = aVar;
        this.f18442a = gVar;
        this.f18443b = kotlinTypePreparator;
        this.f18445d = pVar;
    }

    private static boolean A(PropertyAccessorDescriptor propertyAccessorDescriptor, PropertyAccessorDescriptor propertyAccessorDescriptor2) {
        if (propertyAccessorDescriptor == null || propertyAccessorDescriptor2 == null) {
            return true;
        }
        return H(propertyAccessorDescriptor, propertyAccessorDescriptor2);
    }

    public static boolean B(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2) {
        if (callableDescriptor == null) {
            a(67);
        }
        if (callableDescriptor2 == null) {
            a(68);
        }
        g0 f10 = callableDescriptor.f();
        g0 f11 = callableDescriptor2.f();
        if (!H(callableDescriptor, callableDescriptor2)) {
            return false;
        }
        f1 l10 = f18440f.l(callableDescriptor.m(), callableDescriptor2.m());
        if (callableDescriptor instanceof FunctionDescriptor) {
            return G(callableDescriptor, f10, callableDescriptor2, f11, l10);
        }
        if (callableDescriptor instanceof PropertyDescriptor) {
            PropertyDescriptor propertyDescriptor = (PropertyDescriptor) callableDescriptor;
            PropertyDescriptor propertyDescriptor2 = (PropertyDescriptor) callableDescriptor2;
            if (!A(propertyDescriptor.k(), propertyDescriptor2.k())) {
                return false;
            }
            if (propertyDescriptor.p0() && propertyDescriptor2.p0()) {
                return gd.f.f11759a.k(l10, f10.Z0(), f11.Z0());
            }
            return (propertyDescriptor.p0() || !propertyDescriptor2.p0()) && G(callableDescriptor, f10, callableDescriptor2, f11, l10);
        }
        throw new IllegalArgumentException("Unexpected callable: " + callableDescriptor.getClass());
    }

    private static boolean C(CallableDescriptor callableDescriptor, Collection<CallableDescriptor> collection) {
        if (callableDescriptor == null) {
            a(71);
        }
        if (collection == null) {
            a(72);
        }
        Iterator<CallableDescriptor> it = collection.iterator();
        while (it.hasNext()) {
            if (!B(callableDescriptor, it.next())) {
                return false;
            }
        }
        return true;
    }

    private static boolean G(CallableDescriptor callableDescriptor, g0 g0Var, CallableDescriptor callableDescriptor2, g0 g0Var2, f1 f1Var) {
        if (callableDescriptor == null) {
            a(73);
        }
        if (g0Var == null) {
            a(74);
        }
        if (callableDescriptor2 == null) {
            a(75);
        }
        if (g0Var2 == null) {
            a(76);
        }
        if (f1Var == null) {
            a(77);
        }
        return gd.f.f11759a.r(f1Var, g0Var.Z0(), g0Var2.Z0());
    }

    private static boolean H(DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility, DeclarationDescriptorWithVisibility declarationDescriptorWithVisibility2) {
        if (declarationDescriptorWithVisibility == null) {
            a(69);
        }
        if (declarationDescriptorWithVisibility2 == null) {
            a(70);
        }
        Integer d10 = DescriptorVisibilities.d(declarationDescriptorWithVisibility.g(), declarationDescriptorWithVisibility2.g());
        return d10 == null || d10.intValue() >= 0;
    }

    public static boolean I(MemberDescriptor memberDescriptor, MemberDescriptor memberDescriptor2, boolean z10) {
        if (memberDescriptor == null) {
            a(57);
        }
        if (memberDescriptor2 == null) {
            a(58);
        }
        return !DescriptorVisibilities.g(memberDescriptor2.g()) && DescriptorVisibilities.h(memberDescriptor2, memberDescriptor, z10);
    }

    public static <D extends CallableDescriptor> boolean J(D d10, D d11, boolean z10, boolean z11) {
        if (d10 == null) {
            a(13);
        }
        if (d11 == null) {
            a(14);
        }
        if (!d10.equals(d11) && DescriptorEquivalenceForOverrides.f18421a.f(d10.a(), d11.a(), z10, z11)) {
            return true;
        }
        CallableDescriptor a10 = d11.a();
        Iterator it = sc.e.d(d10).iterator();
        while (it.hasNext()) {
            if (DescriptorEquivalenceForOverrides.f18421a.f(a10, (CallableDescriptor) it.next(), z10, z11)) {
                return true;
            }
        }
        return false;
    }

    public static void K(CallableMemberDescriptor callableMemberDescriptor, ya.l<CallableMemberDescriptor, Unit> lVar) {
        u uVar;
        if (callableMemberDescriptor == null) {
            a(107);
        }
        for (CallableMemberDescriptor callableMemberDescriptor2 : callableMemberDescriptor.e()) {
            if (callableMemberDescriptor2.g() == DescriptorVisibilities.f16735g) {
                K(callableMemberDescriptor2, lVar);
            }
        }
        if (callableMemberDescriptor.g() != DescriptorVisibilities.f16735g) {
            return;
        }
        u h10 = h(callableMemberDescriptor);
        if (h10 == null) {
            if (lVar != null) {
                lVar.invoke(callableMemberDescriptor);
            }
            uVar = DescriptorVisibilities.f16733e;
        } else {
            uVar = h10;
        }
        if (callableMemberDescriptor instanceof PropertyDescriptorImpl) {
            ((PropertyDescriptorImpl) callableMemberDescriptor).l1(uVar);
            Iterator<PropertyAccessorDescriptor> it = ((PropertyDescriptor) callableMemberDescriptor).C().iterator();
            while (it.hasNext()) {
                K(it.next(), h10 == null ? null : lVar);
            }
            return;
        }
        if (callableMemberDescriptor instanceof FunctionDescriptorImpl) {
            ((FunctionDescriptorImpl) callableMemberDescriptor).s1(uVar);
            return;
        }
        PropertyAccessorDescriptorImpl propertyAccessorDescriptorImpl = (PropertyAccessorDescriptorImpl) callableMemberDescriptor;
        propertyAccessorDescriptorImpl.X0(uVar);
        if (uVar != propertyAccessorDescriptorImpl.K0().g()) {
            propertyAccessorDescriptorImpl.V0(false);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <H> H L(Collection<H> collection, ya.l<H, CallableDescriptor> lVar) {
        List h02;
        Object S;
        Object S2;
        Object S3;
        Object S4;
        if (collection == null) {
            a(78);
        }
        if (lVar == 0) {
            a(79);
        }
        if (collection.size() == 1) {
            S4 = _Collections.S(collection);
            H h10 = (H) S4;
            if (h10 == null) {
                a(80);
            }
            return h10;
        }
        ArrayList arrayList = new ArrayList(2);
        h02 = _Collections.h0(collection, lVar);
        S = _Collections.S(collection);
        H h11 = (H) S;
        CallableDescriptor callableDescriptor = (CallableDescriptor) lVar.invoke(h11);
        for (H h12 : collection) {
            CallableDescriptor callableDescriptor2 = (CallableDescriptor) lVar.invoke(h12);
            if (C(callableDescriptor2, h02)) {
                arrayList.add(h12);
            }
            if (B(callableDescriptor2, callableDescriptor) && !B(callableDescriptor, callableDescriptor2)) {
                h11 = h12;
            }
        }
        if (arrayList.isEmpty()) {
            if (h11 == null) {
                a(81);
            }
            return h11;
        }
        if (arrayList.size() == 1) {
            S3 = _Collections.S(arrayList);
            H h13 = (H) S3;
            if (h13 == null) {
                a(82);
            }
            return h13;
        }
        H h14 = null;
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            if (!d0.b(((CallableDescriptor) lVar.invoke(next)).f())) {
                h14 = next;
                break;
            }
        }
        if (h14 != null) {
            return h14;
        }
        S2 = _Collections.S(arrayList);
        H h15 = (H) S2;
        if (h15 == null) {
            a(84);
        }
        return h15;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:93:0x0265. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:94:0x0268. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:95:0x026b. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0277 A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0139  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0141  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0155  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0058 A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0035 A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x016f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01b6  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01c8  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01fb  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0200  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x020a  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x020f  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0219  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0221  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0226  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0229  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x022c  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0231  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x023c  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0246  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0255 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0268  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void a(int i10) {
        String str;
        int i11;
        Object[] objArr;
        if (i10 != 11 && i10 != 12 && i10 != 16 && i10 != 21 && i10 != 95 && i10 != 98 && i10 != 103 && i10 != 44 && i10 != 45) {
            switch (i10) {
                default:
                    switch (i10) {
                        default:
                            switch (i10) {
                                default:
                                    switch (i10) {
                                        case 90:
                                        case 91:
                                        case 92:
                                            break;
                                        default:
                                            str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                                            break;
                                    }
                                case 80:
                                case 81:
                                case 82:
                                case 83:
                                case 84:
                                    str = "@NotNull method %s.%s must not return null";
                                    break;
                            }
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                            break;
                    }
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                    break;
            }
            if (i10 != 11 && i10 != 12 && i10 != 16 && i10 != 21 && i10 != 95 && i10 != 98 && i10 != 103 && i10 != 44 && i10 != 45) {
                switch (i10) {
                    default:
                        switch (i10) {
                            default:
                                switch (i10) {
                                    default:
                                        switch (i10) {
                                            case 90:
                                            case 91:
                                            case 92:
                                                break;
                                            default:
                                                i11 = 3;
                                                break;
                                        }
                                    case 80:
                                    case 81:
                                    case 82:
                                    case 83:
                                    case 84:
                                        i11 = 2;
                                        break;
                                }
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 38:
                            case 39:
                                break;
                        }
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                        break;
                }
                objArr = new Object[i11];
                switch (i10) {
                    case 1:
                    case 7:
                        objArr[0] = "kotlinTypePreparator";
                        break;
                    case 2:
                        objArr[0] = "customSubtype";
                        break;
                    case 3:
                    case 6:
                    default:
                        objArr[0] = "kotlinTypeRefiner";
                        break;
                    case 4:
                        objArr[0] = "equalityAxioms";
                        break;
                    case 5:
                        objArr[0] = "axioms";
                        break;
                    case 8:
                    case 9:
                        objArr[0] = "candidateSet";
                        break;
                    case 10:
                        objArr[0] = "transformFirst";
                        break;
                    case 11:
                    case 12:
                    case 16:
                    case 21:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 44:
                    case 45:
                    case 80:
                    case 81:
                    case 82:
                    case 83:
                    case 84:
                    case 90:
                    case 91:
                    case 92:
                    case 95:
                    case 98:
                    case 103:
                        objArr[0] = "kotlin/reflect/jvm/internal/impl/resolve/OverridingUtil";
                        break;
                    case 13:
                        objArr[0] = "f";
                        break;
                    case 14:
                        objArr[0] = "g";
                        break;
                    case 15:
                    case 17:
                        objArr[0] = "descriptor";
                        break;
                    case 18:
                        objArr[0] = "result";
                        break;
                    case 19:
                    case 22:
                    case 30:
                    case 40:
                        objArr[0] = "superDescriptor";
                        break;
                    case 20:
                    case 23:
                    case 31:
                    case 41:
                        objArr[0] = "subDescriptor";
                        break;
                    case 42:
                        objArr[0] = "firstParameters";
                        break;
                    case 43:
                        objArr[0] = "secondParameters";
                        break;
                    case 46:
                        objArr[0] = "typeInSuper";
                        break;
                    case 47:
                        objArr[0] = "typeInSub";
                        break;
                    case 48:
                    case 51:
                    case 77:
                        objArr[0] = "typeCheckerState";
                        break;
                    case 49:
                        objArr[0] = "superTypeParameter";
                        break;
                    case 50:
                        objArr[0] = "subTypeParameter";
                        break;
                    case 52:
                        objArr[0] = "name";
                        break;
                    case 53:
                        objArr[0] = "membersFromSupertypes";
                        break;
                    case 54:
                        objArr[0] = "membersFromCurrent";
                        break;
                    case 55:
                    case 61:
                    case 64:
                    case 86:
                    case 89:
                    case 96:
                        objArr[0] = "current";
                        break;
                    case 56:
                    case 62:
                    case 66:
                    case 87:
                    case 106:
                        objArr[0] = "strategy";
                        break;
                    case 57:
                        objArr[0] = "overriding";
                        break;
                    case 58:
                        objArr[0] = "fromSuper";
                        break;
                    case 59:
                        objArr[0] = "fromCurrent";
                        break;
                    case 60:
                        objArr[0] = "descriptorsFromSuper";
                        break;
                    case 63:
                    case 65:
                        objArr[0] = "notOverridden";
                        break;
                    case 67:
                    case 69:
                    case 73:
                        objArr[0] = "a";
                        break;
                    case 68:
                    case 70:
                    case 75:
                        objArr[0] = "b";
                        break;
                    case 71:
                        objArr[0] = "candidate";
                        break;
                    case 72:
                    case 88:
                    case 93:
                    case 109:
                        objArr[0] = "descriptors";
                        break;
                    case 74:
                        objArr[0] = "aReturnType";
                        break;
                    case 76:
                        objArr[0] = "bReturnType";
                        break;
                    case 78:
                    case 85:
                        objArr[0] = "overridables";
                        break;
                    case 79:
                    case 101:
                        objArr[0] = "descriptorByHandle";
                        break;
                    case 94:
                        objArr[0] = "classModality";
                        break;
                    case 97:
                        objArr[0] = "toFilter";
                        break;
                    case 99:
                    case 104:
                        objArr[0] = "overrider";
                        break;
                    case 100:
                    case 105:
                        objArr[0] = "extractFrom";
                        break;
                    case 102:
                        objArr[0] = "onConflict";
                        break;
                    case 107:
                    case 108:
                        objArr[0] = "memberDescriptor";
                        break;
                }
                if (i10 != 11 || i10 == 12) {
                    objArr[1] = "filterOverrides";
                } else if (i10 != 16) {
                    if (i10 != 21) {
                        if (i10 == 95) {
                            objArr[1] = "getMinimalModality";
                        } else if (i10 == 98) {
                            objArr[1] = "filterVisibleFakeOverrides";
                        } else if (i10 == 103) {
                            objArr[1] = "extractMembersOverridableInBothWays";
                        } else if (i10 != 44 && i10 != 45) {
                            switch (i10) {
                                case 24:
                                case 25:
                                case 26:
                                case 27:
                                case 28:
                                case 29:
                                    break;
                                default:
                                    switch (i10) {
                                        case 32:
                                        case 33:
                                        case 34:
                                        case 35:
                                        case 36:
                                        case 37:
                                        case 38:
                                        case 39:
                                            objArr[1] = "isOverridableByWithoutExternalConditions";
                                            break;
                                        default:
                                            switch (i10) {
                                                case 80:
                                                case 81:
                                                case 82:
                                                case 83:
                                                case 84:
                                                    objArr[1] = "selectMostSpecificMember";
                                                    break;
                                                default:
                                                    switch (i10) {
                                                        case 90:
                                                        case 91:
                                                        case 92:
                                                            objArr[1] = "determineModalityForFakeOverride";
                                                            break;
                                                        default:
                                                            objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/OverridingUtil";
                                                            break;
                                                    }
                                            }
                                    }
                            }
                        } else {
                            objArr[1] = "createTypeCheckerState";
                        }
                    }
                    objArr[1] = "isOverridableBy";
                } else {
                    objArr[1] = "getOverriddenDeclarations";
                }
                switch (i10) {
                    case 1:
                    case 2:
                        objArr[2] = "createWithTypePreparatorAndCustomSubtype";
                        break;
                    case 3:
                    case 4:
                        objArr[2] = "create";
                        break;
                    case 5:
                    case 6:
                    case 7:
                        objArr[2] = "<init>";
                        break;
                    case 8:
                        objArr[2] = "filterOutOverridden";
                        break;
                    case 9:
                    case 10:
                        objArr[2] = "filterOverrides";
                        break;
                    case 11:
                    case 12:
                    case 16:
                    case 21:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 44:
                    case 45:
                    case 80:
                    case 81:
                    case 82:
                    case 83:
                    case 84:
                    case 90:
                    case 91:
                    case 92:
                    case 95:
                    case 98:
                    case 103:
                        break;
                    case 13:
                    case 14:
                        objArr[2] = "overrides";
                        break;
                    case 15:
                        objArr[2] = "getOverriddenDeclarations";
                        break;
                    case 17:
                    case 18:
                        objArr[2] = "collectOverriddenDeclarations";
                        break;
                    case 19:
                    case 20:
                    case 22:
                    case 23:
                        objArr[2] = "isOverridableBy";
                        break;
                    case 30:
                    case 31:
                        objArr[2] = "isOverridableByWithoutExternalConditions";
                        break;
                    case 40:
                    case 41:
                        objArr[2] = "getBasicOverridabilityProblem";
                        break;
                    case 42:
                    case 43:
                        objArr[2] = "createTypeCheckerState";
                        break;
                    case 46:
                    case 47:
                    case 48:
                        objArr[2] = "areTypesEquivalent";
                        break;
                    case 49:
                    case 50:
                    case 51:
                        objArr[2] = "areTypeParametersEquivalent";
                        break;
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                        objArr[2] = "generateOverridesInFunctionGroup";
                        break;
                    case 57:
                    case 58:
                        objArr[2] = "isVisibleForOverride";
                        break;
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                        objArr[2] = "extractAndBindOverridesForMember";
                        break;
                    case 63:
                        objArr[2] = "allHasSameContainingDeclaration";
                        break;
                    case 64:
                    case 65:
                    case 66:
                        objArr[2] = "createAndBindFakeOverrides";
                        break;
                    case 67:
                    case 68:
                        objArr[2] = "isMoreSpecific";
                        break;
                    case 69:
                    case 70:
                        objArr[2] = "isVisibilityMoreSpecific";
                        break;
                    case 71:
                    case 72:
                        objArr[2] = "isMoreSpecificThenAllOf";
                        break;
                    case 73:
                    case 74:
                    case 75:
                    case 76:
                    case 77:
                        objArr[2] = "isReturnTypeMoreSpecific";
                        break;
                    case 78:
                    case 79:
                        objArr[2] = "selectMostSpecificMember";
                        break;
                    case 85:
                    case 86:
                    case 87:
                        objArr[2] = "createAndBindFakeOverride";
                        break;
                    case 88:
                    case 89:
                        objArr[2] = "determineModalityForFakeOverride";
                        break;
                    case 93:
                    case 94:
                        objArr[2] = "getMinimalModality";
                        break;
                    case 96:
                    case 97:
                        objArr[2] = "filterVisibleFakeOverrides";
                        break;
                    case 99:
                    case 100:
                    case 101:
                    case 102:
                    case 104:
                    case 105:
                    case 106:
                        objArr[2] = "extractMembersOverridableInBothWays";
                        break;
                    case 107:
                        objArr[2] = "resolveUnknownVisibilityForMember";
                        break;
                    case 108:
                        objArr[2] = "computeVisibilityToInherit";
                        break;
                    case 109:
                        objArr[2] = "findMaxVisibility";
                        break;
                    default:
                        objArr[2] = "createWithTypeRefiner";
                        break;
                }
                String format = String.format(str, objArr);
                if (i10 != 11 && i10 != 12 && i10 != 16 && i10 != 21 && i10 != 95 && i10 != 98 && i10 != 103 && i10 != 44 && i10 != 45) {
                    switch (i10) {
                        default:
                            switch (i10) {
                                default:
                                    switch (i10) {
                                        default:
                                            switch (i10) {
                                                case 90:
                                                case 91:
                                                case 92:
                                                    break;
                                                default:
                                                    throw new IllegalArgumentException(format);
                                            }
                                        case 80:
                                        case 81:
                                        case 82:
                                        case 83:
                                        case 84:
                                            throw new IllegalStateException(format);
                                    }
                                case 32:
                                case 33:
                                case 34:
                                case 35:
                                case 36:
                                case 37:
                                case 38:
                                case 39:
                                    break;
                            }
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                            break;
                    }
                }
                throw new IllegalStateException(format);
            }
            i11 = 2;
            objArr = new Object[i11];
            switch (i10) {
            }
            if (i10 != 11) {
            }
            objArr[1] = "filterOverrides";
            switch (i10) {
            }
            String format2 = String.format(str, objArr);
            if (i10 != 11) {
                switch (i10) {
                }
            }
            throw new IllegalStateException(format2);
        }
        str = "@NotNull method %s.%s must not return null";
        if (i10 != 11) {
            switch (i10) {
            }
            objArr = new Object[i11];
            switch (i10) {
            }
            if (i10 != 11) {
            }
            objArr[1] = "filterOverrides";
            switch (i10) {
            }
            String format22 = String.format(str, objArr);
            if (i10 != 11) {
            }
            throw new IllegalStateException(format22);
        }
        i11 = 2;
        objArr = new Object[i11];
        switch (i10) {
        }
        if (i10 != 11) {
        }
        objArr[1] = "filterOverrides";
        switch (i10) {
        }
        String format222 = String.format(str, objArr);
        if (i10 != 11) {
        }
        throw new IllegalStateException(format222);
    }

    private static boolean b(Collection<CallableMemberDescriptor> collection) {
        boolean J;
        if (collection == null) {
            a(63);
        }
        if (collection.size() < 2) {
            return true;
        }
        J = _Collections.J(collection, new c(collection.iterator().next().b()));
        return J;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0054, code lost:
    
        r1.remove();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean c(TypeParameterDescriptor typeParameterDescriptor, TypeParameterDescriptor typeParameterDescriptor2, f1 f1Var) {
        if (typeParameterDescriptor == null) {
            a(49);
        }
        if (typeParameterDescriptor2 == null) {
            a(50);
        }
        if (f1Var == null) {
            a(51);
        }
        List<g0> upperBounds = typeParameterDescriptor.getUpperBounds();
        ArrayList arrayList = new ArrayList(typeParameterDescriptor2.getUpperBounds());
        if (upperBounds.size() != arrayList.size()) {
            return false;
        }
        for (g0 g0Var : upperBounds) {
            ListIterator listIterator = arrayList.listIterator();
            while (listIterator.hasNext()) {
                if (d(g0Var, (g0) listIterator.next(), f1Var)) {
                    break;
                }
            }
            return false;
        }
        return true;
    }

    private static boolean d(g0 g0Var, g0 g0Var2, f1 f1Var) {
        if (g0Var == null) {
            a(46);
        }
        if (g0Var2 == null) {
            a(47);
        }
        if (f1Var == null) {
            a(48);
        }
        if (i0.a(g0Var) && i0.a(g0Var2)) {
            return true;
        }
        return gd.f.f11759a.k(f1Var, g0Var.Z0(), g0Var2.Z0());
    }

    private static i e(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2) {
        if ((callableDescriptor.r0() == null) != (callableDescriptor2.r0() == null)) {
            return i.d("Receiver presence mismatch");
        }
        if (callableDescriptor.l().size() != callableDescriptor2.l().size()) {
            return i.d("Value parameter number mismatch");
        }
        return null;
    }

    private static void f(CallableMemberDescriptor callableMemberDescriptor, Set<CallableMemberDescriptor> set) {
        if (callableMemberDescriptor == null) {
            a(17);
        }
        if (set == null) {
            a(18);
        }
        if (callableMemberDescriptor.getKind().a()) {
            set.add(callableMemberDescriptor);
            return;
        }
        if (!callableMemberDescriptor.e().isEmpty()) {
            Iterator<? extends CallableMemberDescriptor> it = callableMemberDescriptor.e().iterator();
            while (it.hasNext()) {
                f(it.next(), set);
            }
        } else {
            throw new IllegalStateException("No overridden descriptors found for (fake override) " + callableMemberDescriptor);
        }
    }

    private static List<g0> g(CallableDescriptor callableDescriptor) {
        ReceiverParameterDescriptor r02 = callableDescriptor.r0();
        ArrayList arrayList = new ArrayList();
        if (r02 != null) {
            arrayList.add(r02.getType());
        }
        Iterator<ValueParameterDescriptor> it = callableDescriptor.l().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getType());
        }
        return arrayList;
    }

    private static u h(CallableMemberDescriptor callableMemberDescriptor) {
        if (callableMemberDescriptor == null) {
            a(108);
        }
        Collection<? extends CallableMemberDescriptor> e10 = callableMemberDescriptor.e();
        u u7 = u(e10);
        if (u7 == null) {
            return null;
        }
        if (callableMemberDescriptor.getKind() == CallableMemberDescriptor.a.FAKE_OVERRIDE) {
            for (CallableMemberDescriptor callableMemberDescriptor2 : e10) {
                if (callableMemberDescriptor2.o() != Modality.ABSTRACT && !callableMemberDescriptor2.g().equals(u7)) {
                    return null;
                }
            }
            return u7;
        }
        return u7.f();
    }

    public static OverridingUtil i(hd.g gVar, KotlinTypeChecker.a aVar) {
        if (gVar == null) {
            a(3);
        }
        if (aVar == null) {
            a(4);
        }
        return new OverridingUtil(aVar, gVar, KotlinTypePreparator.a.f12214a, null);
    }

    private static void j(Collection<CallableMemberDescriptor> collection, ClassDescriptor classDescriptor, j jVar) {
        if (collection == null) {
            a(85);
        }
        if (classDescriptor == null) {
            a(86);
        }
        if (jVar == null) {
            a(87);
        }
        Collection<CallableMemberDescriptor> t7 = t(classDescriptor, collection);
        boolean isEmpty = t7.isEmpty();
        if (!isEmpty) {
            collection = t7;
        }
        CallableMemberDescriptor T0 = ((CallableMemberDescriptor) L(collection, new d())).T0(classDescriptor, n(collection, classDescriptor), isEmpty ? DescriptorVisibilities.f16736h : DescriptorVisibilities.f16735g, CallableMemberDescriptor.a.FAKE_OVERRIDE, false);
        jVar.d(T0, collection);
        jVar.a(T0);
    }

    private static void k(ClassDescriptor classDescriptor, Collection<CallableMemberDescriptor> collection, j jVar) {
        if (classDescriptor == null) {
            a(64);
        }
        if (collection == null) {
            a(65);
        }
        if (jVar == null) {
            a(66);
        }
        if (b(collection)) {
            Iterator<CallableMemberDescriptor> it = collection.iterator();
            while (it.hasNext()) {
                j(Collections.singleton(it.next()), classDescriptor, jVar);
            }
        } else {
            LinkedList linkedList = new LinkedList(collection);
            while (!linkedList.isEmpty()) {
                j(q(VisibilityUtil.a(linkedList), linkedList, jVar), classDescriptor, jVar);
            }
        }
    }

    private f1 l(List<TypeParameterDescriptor> list, List<TypeParameterDescriptor> list2) {
        if (list == null) {
            a(42);
        }
        if (list2 == null) {
            a(43);
        }
        if (list.isEmpty()) {
            f1 I0 = new OverridingUtilTypeSystemContext(null, this.f18444c, this.f18442a, this.f18443b, this.f18445d).I0(true, true);
            if (I0 == null) {
                a(44);
            }
            return I0;
        }
        HashMap hashMap = new HashMap();
        for (int i10 = 0; i10 < list.size(); i10++) {
            hashMap.put(list.get(i10).n(), list2.get(i10).n());
        }
        f1 I02 = new OverridingUtilTypeSystemContext(hashMap, this.f18444c, this.f18442a, this.f18443b, this.f18445d).I0(true, true);
        if (I02 == null) {
            a(45);
        }
        return I02;
    }

    public static OverridingUtil m(hd.g gVar) {
        if (gVar == null) {
            a(0);
        }
        return new OverridingUtil(f18441g, gVar, KotlinTypePreparator.a.f12214a, null);
    }

    private static Modality n(Collection<CallableMemberDescriptor> collection, ClassDescriptor classDescriptor) {
        if (collection == null) {
            a(88);
        }
        if (classDescriptor == null) {
            a(89);
        }
        boolean z10 = false;
        boolean z11 = false;
        boolean z12 = false;
        for (CallableMemberDescriptor callableMemberDescriptor : collection) {
            int i10 = h.f18452c[callableMemberDescriptor.o().ordinal()];
            if (i10 == 1) {
                Modality modality = Modality.FINAL;
                if (modality == null) {
                    a(90);
                }
                return modality;
            }
            if (i10 == 2) {
                throw new IllegalStateException("Member cannot have SEALED modality: " + callableMemberDescriptor);
            }
            if (i10 == 3) {
                z11 = true;
            } else if (i10 == 4) {
                z12 = true;
            }
        }
        if (classDescriptor.U() && classDescriptor.o() != Modality.ABSTRACT && classDescriptor.o() != Modality.SEALED) {
            z10 = true;
        }
        if (z11 && !z12) {
            Modality modality2 = Modality.OPEN;
            if (modality2 == null) {
                a(91);
            }
            return modality2;
        }
        if (!z11 && z12) {
            Modality o10 = z10 ? classDescriptor.o() : Modality.ABSTRACT;
            if (o10 == null) {
                a(92);
            }
            return o10;
        }
        HashSet hashSet = new HashSet();
        Iterator<CallableMemberDescriptor> it = collection.iterator();
        while (it.hasNext()) {
            hashSet.addAll(z(it.next()));
        }
        return y(r(hashSet), z10, classDescriptor.o());
    }

    private Collection<CallableMemberDescriptor> o(CallableMemberDescriptor callableMemberDescriptor, Collection<? extends CallableMemberDescriptor> collection, ClassDescriptor classDescriptor, j jVar) {
        if (callableMemberDescriptor == null) {
            a(59);
        }
        if (collection == null) {
            a(60);
        }
        if (classDescriptor == null) {
            a(61);
        }
        if (jVar == null) {
            a(62);
        }
        ArrayList arrayList = new ArrayList(collection.size());
        SmartSet c10 = SmartSet.c();
        for (CallableMemberDescriptor callableMemberDescriptor2 : collection) {
            i.a c11 = D(callableMemberDescriptor2, callableMemberDescriptor, classDescriptor).c();
            boolean I = I(callableMemberDescriptor, callableMemberDescriptor2, false);
            int i10 = h.f18451b[c11.ordinal()];
            if (i10 == 1) {
                if (I) {
                    c10.add(callableMemberDescriptor2);
                }
                arrayList.add(callableMemberDescriptor2);
            } else if (i10 == 2) {
                if (I) {
                    jVar.c(callableMemberDescriptor2, callableMemberDescriptor);
                }
                arrayList.add(callableMemberDescriptor2);
            }
        }
        jVar.d(callableMemberDescriptor, c10);
        return arrayList;
    }

    public static <H> Collection<H> p(H h10, Collection<H> collection, ya.l<H, CallableDescriptor> lVar, ya.l<H, Unit> lVar2) {
        if (h10 == null) {
            a(99);
        }
        if (collection == null) {
            a(100);
        }
        if (lVar == null) {
            a(101);
        }
        if (lVar2 == null) {
            a(102);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(h10);
        CallableDescriptor invoke = lVar.invoke(h10);
        Iterator<H> it = collection.iterator();
        while (it.hasNext()) {
            H next = it.next();
            CallableDescriptor invoke2 = lVar.invoke(next);
            if (h10 == next) {
                it.remove();
            } else {
                i.a x10 = x(invoke, invoke2);
                if (x10 == i.a.OVERRIDABLE) {
                    arrayList.add(next);
                    it.remove();
                } else if (x10 == i.a.CONFLICT) {
                    lVar2.invoke(next);
                    it.remove();
                }
            }
        }
        return arrayList;
    }

    private static Collection<CallableMemberDescriptor> q(CallableMemberDescriptor callableMemberDescriptor, Queue<CallableMemberDescriptor> queue, j jVar) {
        if (callableMemberDescriptor == null) {
            a(104);
        }
        if (queue == null) {
            a(105);
        }
        if (jVar == null) {
            a(106);
        }
        return p(callableMemberDescriptor, queue, new f(), new g(jVar, callableMemberDescriptor));
    }

    public static <D extends CallableDescriptor> Set<D> r(Set<D> set) {
        if (set == null) {
            a(8);
        }
        return s(set, !set.isEmpty() && wc.c.u(wc.c.p(set.iterator().next())), null, new b());
    }

    public static <D> Set<D> s(Set<D> set, boolean z10, ya.a<?> aVar, ya.p<? super D, ? super D, ma.o<CallableDescriptor, CallableDescriptor>> pVar) {
        if (set == null) {
            a(9);
        }
        if (pVar == null) {
            a(10);
        }
        if (set.size() <= 1) {
            return set;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Object obj : set) {
            if (aVar != null) {
                aVar.invoke();
            }
            Iterator it = linkedHashSet.iterator();
            while (true) {
                if (it.hasNext()) {
                    ma.o<CallableDescriptor, CallableDescriptor> invoke = pVar.invoke(obj, (Object) it.next());
                    CallableDescriptor a10 = invoke.a();
                    CallableDescriptor b10 = invoke.b();
                    if (J(a10, b10, z10, true)) {
                        it.remove();
                    } else if (J(b10, a10, z10, true)) {
                        break;
                    }
                } else {
                    linkedHashSet.add(obj);
                    break;
                }
            }
        }
        return linkedHashSet;
    }

    private static Collection<CallableMemberDescriptor> t(ClassDescriptor classDescriptor, Collection<CallableMemberDescriptor> collection) {
        List P;
        if (classDescriptor == null) {
            a(96);
        }
        if (collection == null) {
            a(97);
        }
        P = _Collections.P(collection, new e(classDescriptor));
        if (P == null) {
            a(98);
        }
        return P;
    }

    public static u u(Collection<? extends CallableMemberDescriptor> collection) {
        u uVar;
        if (collection == null) {
            a(109);
        }
        if (collection.isEmpty()) {
            return DescriptorVisibilities.f16740l;
        }
        Iterator<? extends CallableMemberDescriptor> it = collection.iterator();
        loop0: while (true) {
            uVar = null;
            while (it.hasNext()) {
                u g6 = it.next().g();
                if (uVar != null) {
                    Integer d10 = DescriptorVisibilities.d(g6, uVar);
                    if (d10 == null) {
                        break;
                    }
                    if (d10.intValue() > 0) {
                    }
                }
                uVar = g6;
            }
        }
        if (uVar == null) {
            return null;
        }
        Iterator<? extends CallableMemberDescriptor> it2 = collection.iterator();
        while (it2.hasNext()) {
            Integer d11 = DescriptorVisibilities.d(uVar, it2.next().g());
            if (d11 == null || d11.intValue() < 0) {
                return null;
            }
        }
        return uVar;
    }

    public static i w(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2) {
        boolean z10;
        if (callableDescriptor == null) {
            a(40);
        }
        if (callableDescriptor2 == null) {
            a(41);
        }
        boolean z11 = callableDescriptor instanceof FunctionDescriptor;
        if ((z11 && !(callableDescriptor2 instanceof FunctionDescriptor)) || (((z10 = callableDescriptor instanceof PropertyDescriptor)) && !(callableDescriptor2 instanceof PropertyDescriptor))) {
            return i.d("Member kind mismatch");
        }
        if (!z11 && !z10) {
            throw new IllegalArgumentException("This type of CallableDescriptor cannot be checked for overridability: " + callableDescriptor);
        }
        if (!callableDescriptor.getName().equals(callableDescriptor2.getName())) {
            return i.d("Name mismatch");
        }
        i e10 = e(callableDescriptor, callableDescriptor2);
        if (e10 != null) {
            return e10;
        }
        return null;
    }

    public static i.a x(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2) {
        OverridingUtil overridingUtil = f18440f;
        i.a c10 = overridingUtil.D(callableDescriptor2, callableDescriptor, null).c();
        i.a c11 = overridingUtil.D(callableDescriptor, callableDescriptor2, null).c();
        i.a aVar = i.a.OVERRIDABLE;
        if (c10 == aVar && c11 == aVar) {
            return aVar;
        }
        i.a aVar2 = i.a.CONFLICT;
        return (c10 == aVar2 || c11 == aVar2) ? aVar2 : i.a.INCOMPATIBLE;
    }

    private static Modality y(Collection<CallableMemberDescriptor> collection, boolean z10, Modality modality) {
        if (collection == null) {
            a(93);
        }
        if (modality == null) {
            a(94);
        }
        Modality modality2 = Modality.ABSTRACT;
        for (CallableMemberDescriptor callableMemberDescriptor : collection) {
            Modality o10 = (z10 && callableMemberDescriptor.o() == Modality.ABSTRACT) ? modality : callableMemberDescriptor.o();
            if (o10.compareTo(modality2) < 0) {
                modality2 = o10;
            }
        }
        if (modality2 == null) {
            a(95);
        }
        return modality2;
    }

    public static Set<CallableMemberDescriptor> z(CallableMemberDescriptor callableMemberDescriptor) {
        if (callableMemberDescriptor == null) {
            a(15);
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        f(callableMemberDescriptor, linkedHashSet);
        return linkedHashSet;
    }

    public i D(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, ClassDescriptor classDescriptor) {
        if (callableDescriptor == null) {
            a(19);
        }
        if (callableDescriptor2 == null) {
            a(20);
        }
        i E = E(callableDescriptor, callableDescriptor2, classDescriptor, false);
        if (E == null) {
            a(21);
        }
        return E;
    }

    public i E(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, ClassDescriptor classDescriptor, boolean z10) {
        if (callableDescriptor == null) {
            a(22);
        }
        if (callableDescriptor2 == null) {
            a(23);
        }
        i F = F(callableDescriptor, callableDescriptor2, z10);
        boolean z11 = F.c() == i.a.OVERRIDABLE;
        for (ExternalOverridabilityCondition externalOverridabilityCondition : f18439e) {
            if (externalOverridabilityCondition.a() != ExternalOverridabilityCondition.a.CONFLICTS_ONLY && (!z11 || externalOverridabilityCondition.a() != ExternalOverridabilityCondition.a.SUCCESS_ONLY)) {
                int i10 = h.f18450a[externalOverridabilityCondition.b(callableDescriptor, callableDescriptor2, classDescriptor).ordinal()];
                if (i10 == 1) {
                    z11 = true;
                } else {
                    if (i10 == 2) {
                        i b10 = i.b("External condition failed");
                        if (b10 == null) {
                            a(24);
                        }
                        return b10;
                    }
                    if (i10 == 3) {
                        i d10 = i.d("External condition");
                        if (d10 == null) {
                            a(25);
                        }
                        return d10;
                    }
                }
            }
        }
        if (!z11) {
            return F;
        }
        for (ExternalOverridabilityCondition externalOverridabilityCondition2 : f18439e) {
            if (externalOverridabilityCondition2.a() == ExternalOverridabilityCondition.a.CONFLICTS_ONLY) {
                int i11 = h.f18450a[externalOverridabilityCondition2.b(callableDescriptor, callableDescriptor2, classDescriptor).ordinal()];
                if (i11 == 1) {
                    throw new IllegalStateException("Contract violation in " + externalOverridabilityCondition2.getClass().getName() + " condition. It's not supposed to end with success");
                }
                if (i11 == 2) {
                    i b11 = i.b("External condition failed");
                    if (b11 == null) {
                        a(27);
                    }
                    return b11;
                }
                if (i11 == 3) {
                    i d11 = i.d("External condition");
                    if (d11 == null) {
                        a(28);
                    }
                    return d11;
                }
            }
        }
        i e10 = i.e();
        if (e10 == null) {
            a(29);
        }
        return e10;
    }

    public i F(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, boolean z10) {
        if (callableDescriptor == null) {
            a(30);
        }
        if (callableDescriptor2 == null) {
            a(31);
        }
        i w10 = w(callableDescriptor, callableDescriptor2);
        if (w10 != null) {
            return w10;
        }
        List<g0> g6 = g(callableDescriptor);
        List<g0> g10 = g(callableDescriptor2);
        List<TypeParameterDescriptor> m10 = callableDescriptor.m();
        List<TypeParameterDescriptor> m11 = callableDescriptor2.m();
        int i10 = 0;
        if (m10.size() != m11.size()) {
            while (i10 < g6.size()) {
                if (!KotlinTypeChecker.f12213a.d(g6.get(i10), g10.get(i10))) {
                    i d10 = i.d("Type parameter number mismatch");
                    if (d10 == null) {
                        a(33);
                    }
                    return d10;
                }
                i10++;
            }
            i b10 = i.b("Type parameter number mismatch");
            if (b10 == null) {
                a(34);
            }
            return b10;
        }
        f1 l10 = l(m10, m11);
        for (int i11 = 0; i11 < m10.size(); i11++) {
            if (!c(m10.get(i11), m11.get(i11), l10)) {
                i d11 = i.d("Type parameter bounds mismatch");
                if (d11 == null) {
                    a(35);
                }
                return d11;
            }
        }
        for (int i12 = 0; i12 < g6.size(); i12++) {
            if (!d(g6.get(i12), g10.get(i12), l10)) {
                i d12 = i.d("Value parameter type mismatch");
                if (d12 == null) {
                    a(36);
                }
                return d12;
            }
        }
        if ((callableDescriptor instanceof FunctionDescriptor) && (callableDescriptor2 instanceof FunctionDescriptor) && ((FunctionDescriptor) callableDescriptor).C0() != ((FunctionDescriptor) callableDescriptor2).C0()) {
            i b11 = i.b("Incompatible suspendability");
            if (b11 == null) {
                a(37);
            }
            return b11;
        }
        if (z10) {
            g0 f10 = callableDescriptor.f();
            g0 f11 = callableDescriptor2.f();
            if (f10 != null && f11 != null) {
                if (i0.a(f11) && i0.a(f10)) {
                    i10 = 1;
                }
                if (i10 == 0 && !gd.f.f11759a.r(l10, f11.Z0(), f10.Z0())) {
                    i b12 = i.b("Return type mismatch");
                    if (b12 == null) {
                        a(38);
                    }
                    return b12;
                }
            }
        }
        i e10 = i.e();
        if (e10 == null) {
            a(39);
        }
        return e10;
    }

    public void v(Name name, Collection<? extends CallableMemberDescriptor> collection, Collection<? extends CallableMemberDescriptor> collection2, ClassDescriptor classDescriptor, j jVar) {
        if (name == null) {
            a(52);
        }
        if (collection == null) {
            a(53);
        }
        if (collection2 == null) {
            a(54);
        }
        if (classDescriptor == null) {
            a(55);
        }
        if (jVar == null) {
            a(56);
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet(collection);
        Iterator<? extends CallableMemberDescriptor> it = collection2.iterator();
        while (it.hasNext()) {
            linkedHashSet.removeAll(o(it.next(), collection, classDescriptor, jVar));
        }
        k(classDescriptor, linkedHashSet, jVar);
    }
}
