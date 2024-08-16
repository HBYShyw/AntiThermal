package sb;

import fd.StorageManager;
import gd.ClassTypeConstructorImpl;
import gd.TypeConstructor;
import gd.o0;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.Modality;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueClassRepresentation;
import sc.OverridingUtil;
import zc.MemberScopeImpl;
import zc.h;

/* compiled from: EnumEntrySyntheticClassDescriptor.java */
/* renamed from: sb.n, reason: use source file name */
/* loaded from: classes2.dex */
public class EnumEntrySyntheticClassDescriptor extends ClassDescriptorBase {

    /* renamed from: m, reason: collision with root package name */
    private final TypeConstructor f18306m;

    /* renamed from: n, reason: collision with root package name */
    private final zc.h f18307n;

    /* renamed from: o, reason: collision with root package name */
    private final fd.i<Set<Name>> f18308o;

    /* renamed from: p, reason: collision with root package name */
    private final qb.g f18309p;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EnumEntrySyntheticClassDescriptor.java */
    /* renamed from: sb.n$a */
    /* loaded from: classes2.dex */
    public class a extends MemberScopeImpl {

        /* renamed from: b, reason: collision with root package name */
        private final fd.g<Name, Collection<? extends SimpleFunctionDescriptor>> f18310b;

        /* renamed from: c, reason: collision with root package name */
        private final fd.g<Name, Collection<? extends PropertyDescriptor>> f18311c;

        /* renamed from: d, reason: collision with root package name */
        private final fd.i<Collection<DeclarationDescriptor>> f18312d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ EnumEntrySyntheticClassDescriptor f18313e;

        /* compiled from: EnumEntrySyntheticClassDescriptor.java */
        /* renamed from: sb.n$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        class C0102a implements ya.l<Name, Collection<? extends SimpleFunctionDescriptor>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ EnumEntrySyntheticClassDescriptor f18314e;

            C0102a(EnumEntrySyntheticClassDescriptor enumEntrySyntheticClassDescriptor) {
                this.f18314e = enumEntrySyntheticClassDescriptor;
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public Collection<? extends SimpleFunctionDescriptor> invoke(Name name) {
                return a.this.m(name);
            }
        }

        /* compiled from: EnumEntrySyntheticClassDescriptor.java */
        /* renamed from: sb.n$a$b */
        /* loaded from: classes2.dex */
        class b implements ya.l<Name, Collection<? extends PropertyDescriptor>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ EnumEntrySyntheticClassDescriptor f18316e;

            b(EnumEntrySyntheticClassDescriptor enumEntrySyntheticClassDescriptor) {
                this.f18316e = enumEntrySyntheticClassDescriptor;
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public Collection<? extends PropertyDescriptor> invoke(Name name) {
                return a.this.n(name);
            }
        }

        /* compiled from: EnumEntrySyntheticClassDescriptor.java */
        /* renamed from: sb.n$a$c */
        /* loaded from: classes2.dex */
        class c implements ya.a<Collection<DeclarationDescriptor>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ EnumEntrySyntheticClassDescriptor f18318e;

            c(EnumEntrySyntheticClassDescriptor enumEntrySyntheticClassDescriptor) {
                this.f18318e = enumEntrySyntheticClassDescriptor;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public Collection<DeclarationDescriptor> invoke() {
                return a.this.l();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: EnumEntrySyntheticClassDescriptor.java */
        /* renamed from: sb.n$a$d */
        /* loaded from: classes2.dex */
        public class d extends sc.i {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ Set f18320a;

            d(Set set) {
                this.f18320a = set;
            }

            private static /* synthetic */ void f(int i10) {
                Object[] objArr = new Object[3];
                if (i10 == 1) {
                    objArr[0] = "fromSuper";
                } else if (i10 != 2) {
                    objArr[0] = "fakeOverride";
                } else {
                    objArr[0] = "fromCurrent";
                }
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/EnumEntrySyntheticClassDescriptor$EnumEntryScope$4";
                if (i10 == 1 || i10 == 2) {
                    objArr[2] = "conflict";
                } else {
                    objArr[2] = "addFakeOverride";
                }
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
            }

            @Override // sc.j
            public void a(CallableMemberDescriptor callableMemberDescriptor) {
                if (callableMemberDescriptor == null) {
                    f(0);
                }
                OverridingUtil.K(callableMemberDescriptor, null);
                this.f18320a.add(callableMemberDescriptor);
            }

            @Override // sc.i
            protected void e(CallableMemberDescriptor callableMemberDescriptor, CallableMemberDescriptor callableMemberDescriptor2) {
                if (callableMemberDescriptor == null) {
                    f(1);
                }
                if (callableMemberDescriptor2 == null) {
                    f(2);
                }
            }
        }

        public a(EnumEntrySyntheticClassDescriptor enumEntrySyntheticClassDescriptor, StorageManager storageManager) {
            if (storageManager == null) {
                h(0);
            }
            this.f18313e = enumEntrySyntheticClassDescriptor;
            this.f18310b = storageManager.d(new C0102a(enumEntrySyntheticClassDescriptor));
            this.f18311c = storageManager.d(new b(enumEntrySyntheticClassDescriptor));
            this.f18312d = storageManager.g(new c(enumEntrySyntheticClassDescriptor));
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x0022  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x002d  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x005d  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0090  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0095  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x009a  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x009d  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x00a0  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x00a5  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x00a8  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00ad  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x00b5 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00be  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x008b  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x0032  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x0037  */
        /* JADX WARN: Removed duplicated region for block: B:56:0x003c  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0041  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x0046  */
        /* JADX WARN: Removed duplicated region for block: B:59:0x0049  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x004e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private static /* synthetic */ void h(int i10) {
            String str;
            int i11;
            if (i10 != 3 && i10 != 7 && i10 != 9 && i10 != 12) {
                switch (i10) {
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                        break;
                    default:
                        str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                        break;
                }
                if (i10 != 3 && i10 != 7 && i10 != 9 && i10 != 12) {
                    switch (i10) {
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                            break;
                        default:
                            i11 = 3;
                            break;
                    }
                    Object[] objArr = new Object[i11];
                    switch (i10) {
                        case 1:
                        case 4:
                        case 5:
                        case 8:
                        case 10:
                            objArr[0] = "name";
                            break;
                        case 2:
                        case 6:
                            objArr[0] = "location";
                            break;
                        case 3:
                        case 7:
                        case 9:
                        case 12:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                            objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/EnumEntrySyntheticClassDescriptor$EnumEntryScope";
                            break;
                        case 11:
                            objArr[0] = "fromSupertypes";
                            break;
                        case 13:
                            objArr[0] = "kindFilter";
                            break;
                        case 14:
                            objArr[0] = "nameFilter";
                            break;
                        case 20:
                            objArr[0] = "p";
                            break;
                        default:
                            objArr[0] = "storageManager";
                            break;
                    }
                    if (i10 != 3) {
                        objArr[1] = "getContributedVariables";
                    } else if (i10 == 7) {
                        objArr[1] = "getContributedFunctions";
                    } else if (i10 == 9) {
                        objArr[1] = "getSupertypeScope";
                    } else if (i10 != 12) {
                        switch (i10) {
                            case 15:
                                objArr[1] = "getContributedDescriptors";
                                break;
                            case 16:
                                objArr[1] = "computeAllDeclarations";
                                break;
                            case 17:
                                objArr[1] = "getFunctionNames";
                                break;
                            case 18:
                                objArr[1] = "getClassifierNames";
                                break;
                            case 19:
                                objArr[1] = "getVariableNames";
                                break;
                            default:
                                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/EnumEntrySyntheticClassDescriptor$EnumEntryScope";
                                break;
                        }
                    } else {
                        objArr[1] = "resolveFakeOverrides";
                    }
                    switch (i10) {
                        case 1:
                        case 2:
                            objArr[2] = "getContributedVariables";
                            break;
                        case 3:
                        case 7:
                        case 9:
                        case 12:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                            break;
                        case 4:
                            objArr[2] = "computeProperties";
                            break;
                        case 5:
                        case 6:
                            objArr[2] = "getContributedFunctions";
                            break;
                        case 8:
                            objArr[2] = "computeFunctions";
                            break;
                        case 10:
                        case 11:
                            objArr[2] = "resolveFakeOverrides";
                            break;
                        case 13:
                        case 14:
                            objArr[2] = "getContributedDescriptors";
                            break;
                        case 20:
                            objArr[2] = "printScopeStructure";
                            break;
                        default:
                            objArr[2] = "<init>";
                            break;
                    }
                    String format = String.format(str, objArr);
                    if (i10 != 3 && i10 != 7 && i10 != 9 && i10 != 12) {
                        switch (i10) {
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                                break;
                            default:
                                throw new IllegalArgumentException(format);
                        }
                    }
                    throw new IllegalStateException(format);
                }
                i11 = 2;
                Object[] objArr2 = new Object[i11];
                switch (i10) {
                }
                if (i10 != 3) {
                }
                switch (i10) {
                }
                String format2 = String.format(str, objArr2);
                if (i10 != 3) {
                    switch (i10) {
                    }
                }
                throw new IllegalStateException(format2);
            }
            str = "@NotNull method %s.%s must not return null";
            if (i10 != 3) {
                switch (i10) {
                }
                Object[] objArr22 = new Object[i11];
                switch (i10) {
                }
                if (i10 != 3) {
                }
                switch (i10) {
                }
                String format22 = String.format(str, objArr22);
                if (i10 != 3) {
                }
                throw new IllegalStateException(format22);
            }
            i11 = 2;
            Object[] objArr222 = new Object[i11];
            switch (i10) {
            }
            if (i10 != 3) {
            }
            switch (i10) {
            }
            String format222 = String.format(str, objArr222);
            if (i10 != 3) {
            }
            throw new IllegalStateException(format222);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Collection<DeclarationDescriptor> l() {
            HashSet hashSet = new HashSet();
            for (Name name : (Set) this.f18313e.f18308o.invoke()) {
                xb.d dVar = xb.d.FOR_NON_TRACKED_SCOPE;
                hashSet.addAll(c(name, dVar));
                hashSet.addAll(a(name, dVar));
            }
            return hashSet;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Collection<? extends SimpleFunctionDescriptor> m(Name name) {
            if (name == null) {
                h(8);
            }
            return p(name, o().c(name, xb.d.FOR_NON_TRACKED_SCOPE));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Collection<? extends PropertyDescriptor> n(Name name) {
            if (name == null) {
                h(4);
            }
            return p(name, o().a(name, xb.d.FOR_NON_TRACKED_SCOPE));
        }

        private zc.h o() {
            zc.h u7 = this.f18313e.n().q().iterator().next().u();
            if (u7 == null) {
                h(9);
            }
            return u7;
        }

        private <D extends CallableMemberDescriptor> Collection<? extends D> p(Name name, Collection<? extends D> collection) {
            if (name == null) {
                h(10);
            }
            if (collection == null) {
                h(11);
            }
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            OverridingUtil.f18440f.v(name, collection, Collections.emptySet(), this.f18313e, new d(linkedHashSet));
            return linkedHashSet;
        }

        @Override // zc.MemberScopeImpl, zc.h
        public Collection<? extends PropertyDescriptor> a(Name name, xb.b bVar) {
            if (name == null) {
                h(1);
            }
            if (bVar == null) {
                h(2);
            }
            Collection<? extends PropertyDescriptor> invoke = this.f18311c.invoke(name);
            if (invoke == null) {
                h(3);
            }
            return invoke;
        }

        @Override // zc.MemberScopeImpl, zc.h
        public Set<Name> b() {
            Set<Name> set = (Set) this.f18313e.f18308o.invoke();
            if (set == null) {
                h(17);
            }
            return set;
        }

        @Override // zc.MemberScopeImpl, zc.h
        public Collection<? extends SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
            if (name == null) {
                h(5);
            }
            if (bVar == null) {
                h(6);
            }
            Collection<? extends SimpleFunctionDescriptor> invoke = this.f18310b.invoke(name);
            if (invoke == null) {
                h(7);
            }
            return invoke;
        }

        @Override // zc.MemberScopeImpl, zc.h
        public Set<Name> d() {
            Set<Name> set = (Set) this.f18313e.f18308o.invoke();
            if (set == null) {
                h(19);
            }
            return set;
        }

        @Override // zc.MemberScopeImpl, zc.h
        public Set<Name> f() {
            Set<Name> emptySet = Collections.emptySet();
            if (emptySet == null) {
                h(18);
            }
            return emptySet;
        }

        @Override // zc.MemberScopeImpl, zc.ResolutionScope
        public Collection<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
            if (dVar == null) {
                h(13);
            }
            if (lVar == null) {
                h(14);
            }
            Collection<DeclarationDescriptor> invoke = this.f18312d.invoke();
            if (invoke == null) {
                h(15);
            }
            return invoke;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    private EnumEntrySyntheticClassDescriptor(StorageManager storageManager, ClassDescriptor classDescriptor, gd.g0 g0Var, Name name, fd.i<Set<Name>> iVar, qb.g gVar, SourceElement sourceElement) {
        super(storageManager, classDescriptor, name, sourceElement, false);
        if (storageManager == null) {
            J0(6);
        }
        if (classDescriptor == null) {
            J0(7);
        }
        if (g0Var == null) {
            J0(8);
        }
        if (name == null) {
            J0(9);
        }
        if (iVar == null) {
            J0(10);
        }
        if (gVar == null) {
            J0(11);
        }
        if (sourceElement == null) {
            J0(12);
        }
        this.f18309p = gVar;
        this.f18306m = new ClassTypeConstructorImpl(this, Collections.emptyList(), Collections.singleton(g0Var), storageManager);
        this.f18307n = new a(this, storageManager);
        this.f18308o = iVar;
    }

    private static /* synthetic */ void J0(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                str = "@NotNull method %s.%s must not return null";
                break;
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                i11 = 2;
                break;
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
                objArr[0] = "enumClass";
                break;
            case 2:
            case 9:
                objArr[0] = "name";
                break;
            case 3:
            case 10:
                objArr[0] = "enumMemberNames";
                break;
            case 4:
            case 11:
                objArr[0] = "annotations";
                break;
            case 5:
            case 12:
                objArr[0] = "source";
                break;
            case 6:
            default:
                objArr[0] = "storageManager";
                break;
            case 7:
                objArr[0] = "containingClass";
                break;
            case 8:
                objArr[0] = "supertype";
                break;
            case 13:
                objArr[0] = "kotlinTypeRefiner";
                break;
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/EnumEntrySyntheticClassDescriptor";
                break;
        }
        switch (i10) {
            case 14:
                objArr[1] = "getUnsubstitutedMemberScope";
                break;
            case 15:
                objArr[1] = "getStaticScope";
                break;
            case 16:
                objArr[1] = "getConstructors";
                break;
            case 17:
                objArr[1] = "getTypeConstructor";
                break;
            case 18:
                objArr[1] = "getKind";
                break;
            case 19:
                objArr[1] = "getModality";
                break;
            case 20:
                objArr[1] = "getVisibility";
                break;
            case 21:
                objArr[1] = "getAnnotations";
                break;
            case 22:
                objArr[1] = "getDeclaredTypeParameters";
                break;
            case 23:
                objArr[1] = "getSealedSubclasses";
                break;
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/EnumEntrySyntheticClassDescriptor";
                break;
        }
        switch (i10) {
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                objArr[2] = "<init>";
                break;
            case 13:
                objArr[2] = "getUnsubstitutedMemberScope";
                break;
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                break;
            default:
                objArr[2] = "create";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                throw new IllegalStateException(format);
            default:
                throw new IllegalArgumentException(format);
        }
    }

    public static EnumEntrySyntheticClassDescriptor U0(StorageManager storageManager, ClassDescriptor classDescriptor, Name name, fd.i<Set<Name>> iVar, qb.g gVar, SourceElement sourceElement) {
        if (storageManager == null) {
            J0(0);
        }
        if (classDescriptor == null) {
            J0(1);
        }
        if (name == null) {
            J0(2);
        }
        if (iVar == null) {
            J0(3);
        }
        if (gVar == null) {
            J0(4);
        }
        if (sourceElement == null) {
            J0(5);
        }
        return new EnumEntrySyntheticClassDescriptor(storageManager, classDescriptor, classDescriptor.x(), name, iVar, gVar, sourceElement);
    }

    @Override // pb.ClassDescriptor, pb.ClassifierDescriptorWithTypeParameters
    public List<TypeParameterDescriptor> B() {
        List<TypeParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            J0(22);
        }
        return emptyList;
    }

    @Override // pb.ClassDescriptor
    public boolean F() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public ValueClassRepresentation<o0> G0() {
        return null;
    }

    @Override // pb.ClassDescriptor
    public boolean L() {
        return false;
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return false;
    }

    @Override // sb.t
    public zc.h Q(hd.g gVar) {
        if (gVar == null) {
            J0(13);
        }
        zc.h hVar = this.f18307n;
        if (hVar == null) {
            J0(14);
        }
        return hVar;
    }

    @Override // pb.ClassDescriptor
    public boolean R0() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public Collection<ClassDescriptor> S() {
        List emptyList = Collections.emptyList();
        if (emptyList == null) {
            J0(23);
        }
        return emptyList;
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public ClassConstructorDescriptor Z() {
        return null;
    }

    @Override // pb.ClassDescriptor
    public zc.h a0() {
        h.b bVar = h.b.f20465b;
        if (bVar == null) {
            J0(15);
        }
        return bVar;
    }

    @Override // pb.ClassDescriptor
    public ClassDescriptor c0() {
        return null;
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        pb.u uVar = DescriptorVisibilities.f16733e;
        if (uVar == null) {
            J0(20);
        }
        return uVar;
    }

    @Override // pb.ClassDescriptor
    public ClassKind getKind() {
        ClassKind classKind = ClassKind.ENUM_ENTRY;
        if (classKind == null) {
            J0(18);
        }
        return classKind;
    }

    @Override // qb.a
    public qb.g i() {
        qb.g gVar = this.f18309p;
        if (gVar == null) {
            J0(21);
        }
        return gVar;
    }

    @Override // pb.ClassifierDescriptor
    public TypeConstructor n() {
        TypeConstructor typeConstructor = this.f18306m;
        if (typeConstructor == null) {
            J0(17);
        }
        return typeConstructor;
    }

    @Override // pb.ClassDescriptor, pb.MemberDescriptor
    public Modality o() {
        Modality modality = Modality.FINAL;
        if (modality == null) {
            J0(19);
        }
        return modality;
    }

    @Override // pb.ClassDescriptor
    public Collection<ClassConstructorDescriptor> p() {
        List emptyList = Collections.emptyList();
        if (emptyList == null) {
            J0(16);
        }
        return emptyList;
    }

    @Override // pb.ClassDescriptor
    public boolean q() {
        return false;
    }

    @Override // pb.ClassifierDescriptorWithTypeParameters
    public boolean r() {
        return false;
    }

    public String toString() {
        return "enum entry " + getName();
    }

    @Override // pb.ClassDescriptor
    public boolean y() {
        return false;
    }
}
