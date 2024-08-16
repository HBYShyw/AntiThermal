package ed;

import ad.ContextClassReceiver;
import cd.ErrorReporter;
import cd.MemberDeserializer;
import cd.NameResolverUtil;
import cd.ProtoContainer;
import cd.ProtoEnumFlags;
import cd.ProtoEnumFlagsUtils;
import cd.d0;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import fb._Ranges;
import gb.KDeclarationContainer;
import gd.AbstractClassTypeConstructor;
import gd.TypeConstructor;
import gd.g0;
import gd.o0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jc.c;
import jc.q;
import jc.r;
import jc.t;
import jc.w;
import kotlin.collections.MapsJVM;
import kotlin.collections.MutableCollections;
import kotlin.collections._Collections;
import kotlin.collections._Sets;
import kotlin.collections.s;
import lc.BinaryVersion;
import lc.Flags;
import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import lc.protoTypeTableUtil;
import ma.o;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.InlineClassRepresentation;
import pb.Modality;
import pb.MultiFieldValueClassRepresentation;
import pb.NotFoundClasses;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.ScopesHolderForClass;
import pb.SimpleFunctionDescriptor;
import pb.SourceElement;
import pb.SupertypeLoopChecker;
import pb.TypeParameterDescriptor;
import pb.ValueClassRepresentation;
import pb.ValueParameterDescriptor;
import pb.g1;
import pb.u;
import pb.v;
import qb.AnnotationDescriptor;
import sb.AbstractClassDescriptor;
import sb.ClassConstructorDescriptorImpl;
import sb.EnumEntrySyntheticClassDescriptor;
import sb.FunctionDescriptorImpl;
import sb.ReceiverParameterDescriptorImpl;
import sc.DescriptorFactory;
import sc.OverridingUtil;
import za.FunctionReference;
import za.Lambda;
import za.Reflection;
import zc.MemberScopeImpl;
import zc.ResolutionScope;
import zc.StaticScopeForKotlinEnum;
import zc.h;

/* compiled from: DeserializedClassDescriptor.kt */
/* renamed from: ed.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class DeserializedClassDescriptor extends AbstractClassDescriptor implements DeclarationDescriptor {
    private final fd.j<ValueClassRepresentation<o0>> A;
    private final ProtoContainer.a B;
    private final qb.g C;

    /* renamed from: j, reason: collision with root package name */
    private final jc.c f11035j;

    /* renamed from: k, reason: collision with root package name */
    private final BinaryVersion f11036k;

    /* renamed from: l, reason: collision with root package name */
    private final SourceElement f11037l;

    /* renamed from: m, reason: collision with root package name */
    private final ClassId f11038m;

    /* renamed from: n, reason: collision with root package name */
    private final Modality f11039n;

    /* renamed from: o, reason: collision with root package name */
    private final u f11040o;

    /* renamed from: p, reason: collision with root package name */
    private final ClassKind f11041p;

    /* renamed from: q, reason: collision with root package name */
    private final cd.m f11042q;

    /* renamed from: r, reason: collision with root package name */
    private final MemberScopeImpl f11043r;

    /* renamed from: s, reason: collision with root package name */
    private final b f11044s;

    /* renamed from: t, reason: collision with root package name */
    private final ScopesHolderForClass<a> f11045t;

    /* renamed from: u, reason: collision with root package name */
    private final c f11046u;

    /* renamed from: v, reason: collision with root package name */
    private final DeclarationDescriptor f11047v;

    /* renamed from: w, reason: collision with root package name */
    private final fd.j<ClassConstructorDescriptor> f11048w;

    /* renamed from: x, reason: collision with root package name */
    private final fd.i<Collection<ClassConstructorDescriptor>> f11049x;

    /* renamed from: y, reason: collision with root package name */
    private final fd.j<ClassDescriptor> f11050y;

    /* renamed from: z, reason: collision with root package name */
    private final fd.i<Collection<ClassDescriptor>> f11051z;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$a */
    /* loaded from: classes2.dex */
    public final class a extends DeserializedMemberScope {

        /* renamed from: g, reason: collision with root package name */
        private final hd.g f11052g;

        /* renamed from: h, reason: collision with root package name */
        private final fd.i<Collection<DeclarationDescriptor>> f11053h;

        /* renamed from: i, reason: collision with root package name */
        private final fd.i<Collection<g0>> f11054i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ DeserializedClassDescriptor f11055j;

        /* compiled from: DeserializedClassDescriptor.kt */
        /* renamed from: ed.d$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0032a extends Lambda implements ya.a<List<? extends Name>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ List<Name> f11056e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0032a(List<Name> list) {
                super(0);
                this.f11056e = list;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<Name> invoke() {
                return this.f11056e;
            }
        }

        /* compiled from: DeserializedClassDescriptor.kt */
        /* renamed from: ed.d$a$b */
        /* loaded from: classes2.dex */
        static final class b extends Lambda implements ya.a<Collection<? extends DeclarationDescriptor>> {
            b() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Collection<DeclarationDescriptor> invoke() {
                return a.this.j(zc.d.f20436o, zc.h.f20461a.a(), xb.d.WHEN_GET_ALL_DESCRIPTORS);
            }
        }

        /* compiled from: DeserializedClassDescriptor.kt */
        /* renamed from: ed.d$a$c */
        /* loaded from: classes2.dex */
        public static final class c extends sc.i {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ List<D> f11058a;

            c(List<D> list) {
                this.f11058a = list;
            }

            @Override // sc.j
            public void a(CallableMemberDescriptor callableMemberDescriptor) {
                za.k.e(callableMemberDescriptor, "fakeOverride");
                OverridingUtil.K(callableMemberDescriptor, null);
                this.f11058a.add(callableMemberDescriptor);
            }

            @Override // sc.i
            protected void e(CallableMemberDescriptor callableMemberDescriptor, CallableMemberDescriptor callableMemberDescriptor2) {
                za.k.e(callableMemberDescriptor, "fromSuper");
                za.k.e(callableMemberDescriptor2, "fromCurrent");
                if (callableMemberDescriptor2 instanceof FunctionDescriptorImpl) {
                    ((FunctionDescriptorImpl) callableMemberDescriptor2).d1(v.f16746a, callableMemberDescriptor);
                }
            }
        }

        /* compiled from: DeserializedClassDescriptor.kt */
        /* renamed from: ed.d$a$d */
        /* loaded from: classes2.dex */
        static final class d extends Lambda implements ya.a<Collection<? extends g0>> {
            d() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Collection<g0> invoke() {
                return a.this.f11052g.g(a.this.B());
            }
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public a(DeserializedClassDescriptor deserializedClassDescriptor, hd.g gVar) {
            super(r2, r3, r4, r5, new C0032a(r1));
            int u7;
            za.k.e(gVar, "kotlinTypeRefiner");
            this.f11055j = deserializedClassDescriptor;
            cd.m j12 = deserializedClassDescriptor.j1();
            List<jc.i> C0 = deserializedClassDescriptor.k1().C0();
            za.k.d(C0, "classProto.functionList");
            List<jc.n> Q0 = deserializedClassDescriptor.k1().Q0();
            za.k.d(Q0, "classProto.propertyList");
            List<r> Y0 = deserializedClassDescriptor.k1().Y0();
            za.k.d(Y0, "classProto.typeAliasList");
            List<Integer> N0 = deserializedClassDescriptor.k1().N0();
            za.k.d(N0, "classProto.nestedClassNameList");
            NameResolver g6 = deserializedClassDescriptor.j1().g();
            u7 = s.u(N0, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = N0.iterator();
            while (it.hasNext()) {
                arrayList.add(NameResolverUtil.b(g6, ((Number) it.next()).intValue()));
            }
            this.f11052g = gVar;
            this.f11053h = p().h().g(new b());
            this.f11054i = p().h().g(new d());
        }

        private final <D extends CallableMemberDescriptor> void A(Name name, Collection<? extends D> collection, List<D> list) {
            p().c().m().a().v(name, collection, new ArrayList(list), B(), new c(list));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final DeserializedClassDescriptor B() {
            return this.f11055j;
        }

        public void C(Name name, xb.b bVar) {
            za.k.e(name, "name");
            za.k.e(bVar, "location");
            wb.a.a(p().c().o(), bVar, B(), name);
        }

        @Override // ed.DeserializedMemberScope, zc.MemberScopeImpl, zc.h
        public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
            za.k.e(name, "name");
            za.k.e(bVar, "location");
            C(name, bVar);
            return super.a(name, bVar);
        }

        @Override // ed.DeserializedMemberScope, zc.MemberScopeImpl, zc.h
        public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
            za.k.e(name, "name");
            za.k.e(bVar, "location");
            C(name, bVar);
            return super.c(name, bVar);
        }

        @Override // ed.DeserializedMemberScope, zc.MemberScopeImpl, zc.ResolutionScope
        public ClassifierDescriptor e(Name name, xb.b bVar) {
            ClassDescriptor f10;
            za.k.e(name, "name");
            za.k.e(bVar, "location");
            C(name, bVar);
            c cVar = B().f11046u;
            return (cVar == null || (f10 = cVar.f(name)) == null) ? super.e(name, bVar) : f10;
        }

        @Override // zc.MemberScopeImpl, zc.ResolutionScope
        public Collection<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
            za.k.e(dVar, "kindFilter");
            za.k.e(lVar, "nameFilter");
            return this.f11053h.invoke();
        }

        @Override // ed.DeserializedMemberScope
        protected void i(Collection<DeclarationDescriptor> collection, ya.l<? super Name, Boolean> lVar) {
            za.k.e(collection, "result");
            za.k.e(lVar, "nameFilter");
            c cVar = B().f11046u;
            Collection<ClassDescriptor> d10 = cVar != null ? cVar.d() : null;
            if (d10 == null) {
                d10 = kotlin.collections.r.j();
            }
            collection.addAll(d10);
        }

        @Override // ed.DeserializedMemberScope
        protected void k(Name name, List<SimpleFunctionDescriptor> list) {
            za.k.e(name, "name");
            za.k.e(list, "functions");
            ArrayList arrayList = new ArrayList();
            Iterator<g0> it = this.f11054i.invoke().iterator();
            while (it.hasNext()) {
                arrayList.addAll(it.next().u().c(name, xb.d.FOR_ALREADY_TRACKED));
            }
            list.addAll(p().c().c().b(name, this.f11055j));
            A(name, arrayList, list);
        }

        @Override // ed.DeserializedMemberScope
        protected void l(Name name, List<PropertyDescriptor> list) {
            za.k.e(name, "name");
            za.k.e(list, "descriptors");
            ArrayList arrayList = new ArrayList();
            Iterator<g0> it = this.f11054i.invoke().iterator();
            while (it.hasNext()) {
                arrayList.addAll(it.next().u().a(name, xb.d.FOR_ALREADY_TRACKED));
            }
            A(name, arrayList, list);
        }

        @Override // ed.DeserializedMemberScope
        protected ClassId m(Name name) {
            za.k.e(name, "name");
            ClassId d10 = this.f11055j.f11038m.d(name);
            za.k.d(d10, "classId.createNestedClassId(name)");
            return d10;
        }

        @Override // ed.DeserializedMemberScope
        protected Set<Name> s() {
            List<g0> q10 = B().f11044s.q();
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            Iterator<T> it = q10.iterator();
            while (it.hasNext()) {
                Set<Name> f10 = ((g0) it.next()).u().f();
                if (f10 == null) {
                    return null;
                }
                MutableCollections.z(linkedHashSet, f10);
            }
            return linkedHashSet;
        }

        @Override // ed.DeserializedMemberScope
        protected Set<Name> t() {
            List<g0> q10 = B().f11044s.q();
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            Iterator<T> it = q10.iterator();
            while (it.hasNext()) {
                MutableCollections.z(linkedHashSet, ((g0) it.next()).u().b());
            }
            linkedHashSet.addAll(p().c().c().a(this.f11055j));
            return linkedHashSet;
        }

        @Override // ed.DeserializedMemberScope
        protected Set<Name> u() {
            List<g0> q10 = B().f11044s.q();
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            Iterator<T> it = q10.iterator();
            while (it.hasNext()) {
                MutableCollections.z(linkedHashSet, ((g0) it.next()).u().d());
            }
            return linkedHashSet;
        }

        @Override // ed.DeserializedMemberScope
        protected boolean x(SimpleFunctionDescriptor simpleFunctionDescriptor) {
            za.k.e(simpleFunctionDescriptor, "function");
            return p().c().s().d(this.f11055j, simpleFunctionDescriptor);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$b */
    /* loaded from: classes2.dex */
    public final class b extends AbstractClassTypeConstructor {

        /* renamed from: d, reason: collision with root package name */
        private final fd.i<List<TypeParameterDescriptor>> f11060d;

        /* compiled from: DeserializedClassDescriptor.kt */
        /* renamed from: ed.d$b$a */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.a<List<? extends TypeParameterDescriptor>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ DeserializedClassDescriptor f11062e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(DeserializedClassDescriptor deserializedClassDescriptor) {
                super(0);
                this.f11062e = deserializedClassDescriptor;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<TypeParameterDescriptor> invoke() {
                return g1.d(this.f11062e);
            }
        }

        public b() {
            super(DeserializedClassDescriptor.this.j1().h());
            this.f11060d = DeserializedClassDescriptor.this.j1().h().g(new a(DeserializedClassDescriptor.this));
        }

        @Override // gd.TypeConstructor
        public List<TypeParameterDescriptor> getParameters() {
            return this.f11060d.invoke();
        }

        @Override // gd.AbstractTypeConstructor
        protected Collection<g0> h() {
            int u7;
            List m02;
            List z02;
            int u10;
            String b10;
            FqName b11;
            List<q> o10 = protoTypeTableUtil.o(DeserializedClassDescriptor.this.k1(), DeserializedClassDescriptor.this.j1().j());
            DeserializedClassDescriptor deserializedClassDescriptor = DeserializedClassDescriptor.this;
            u7 = s.u(o10, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = o10.iterator();
            while (it.hasNext()) {
                arrayList.add(deserializedClassDescriptor.j1().i().q((q) it.next()));
            }
            m02 = _Collections.m0(arrayList, DeserializedClassDescriptor.this.j1().c().c().c(DeserializedClassDescriptor.this));
            ArrayList<NotFoundClasses.b> arrayList2 = new ArrayList();
            Iterator it2 = m02.iterator();
            while (it2.hasNext()) {
                ClassifierDescriptor v7 = ((g0) it2.next()).W0().v();
                NotFoundClasses.b bVar = v7 instanceof NotFoundClasses.b ? (NotFoundClasses.b) v7 : null;
                if (bVar != null) {
                    arrayList2.add(bVar);
                }
            }
            if (!arrayList2.isEmpty()) {
                ErrorReporter i10 = DeserializedClassDescriptor.this.j1().c().i();
                DeserializedClassDescriptor deserializedClassDescriptor2 = DeserializedClassDescriptor.this;
                u10 = s.u(arrayList2, 10);
                ArrayList arrayList3 = new ArrayList(u10);
                for (NotFoundClasses.b bVar2 : arrayList2) {
                    ClassId k10 = wc.c.k(bVar2);
                    if (k10 == null || (b11 = k10.b()) == null || (b10 = b11.b()) == null) {
                        b10 = bVar2.getName().b();
                    }
                    arrayList3.add(b10);
                }
                i10.a(deserializedClassDescriptor2, arrayList3);
            }
            z02 = _Collections.z0(m02);
            return z02;
        }

        @Override // gd.AbstractTypeConstructor
        protected SupertypeLoopChecker l() {
            return SupertypeLoopChecker.a.f16675a;
        }

        public String toString() {
            String name = DeserializedClassDescriptor.this.getName().toString();
            za.k.d(name, "name.toString()");
            return name;
        }

        @Override // gd.TypeConstructor
        public boolean w() {
            return true;
        }

        @Override // gd.ClassifierBasedTypeConstructor, gd.TypeConstructor
        /* renamed from: x, reason: merged with bridge method [inline-methods] */
        public DeserializedClassDescriptor v() {
            return DeserializedClassDescriptor.this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$c */
    /* loaded from: classes2.dex */
    public final class c {

        /* renamed from: a, reason: collision with root package name */
        private final Map<Name, jc.g> f11063a;

        /* renamed from: b, reason: collision with root package name */
        private final fd.h<Name, ClassDescriptor> f11064b;

        /* renamed from: c, reason: collision with root package name */
        private final fd.i<Set<Name>> f11065c;

        /* compiled from: DeserializedClassDescriptor.kt */
        /* renamed from: ed.d$c$a */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.l<Name, ClassDescriptor> {

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ DeserializedClassDescriptor f11068f;

            /* JADX INFO: Access modifiers changed from: package-private */
            /* compiled from: DeserializedClassDescriptor.kt */
            /* renamed from: ed.d$c$a$a, reason: collision with other inner class name */
            /* loaded from: classes2.dex */
            public static final class C0033a extends Lambda implements ya.a<List<? extends AnnotationDescriptor>> {

                /* renamed from: e, reason: collision with root package name */
                final /* synthetic */ DeserializedClassDescriptor f11069e;

                /* renamed from: f, reason: collision with root package name */
                final /* synthetic */ jc.g f11070f;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                C0033a(DeserializedClassDescriptor deserializedClassDescriptor, jc.g gVar) {
                    super(0);
                    this.f11069e = deserializedClassDescriptor;
                    this.f11070f = gVar;
                }

                @Override // ya.a
                /* renamed from: a, reason: merged with bridge method [inline-methods] */
                public final List<AnnotationDescriptor> invoke() {
                    List<AnnotationDescriptor> z02;
                    z02 = _Collections.z0(this.f11069e.j1().c().d().h(this.f11069e.o1(), this.f11070f));
                    return z02;
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(DeserializedClassDescriptor deserializedClassDescriptor) {
                super(1);
                this.f11068f = deserializedClassDescriptor;
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final ClassDescriptor invoke(Name name) {
                za.k.e(name, "name");
                jc.g gVar = (jc.g) c.this.f11063a.get(name);
                if (gVar == null) {
                    return null;
                }
                DeserializedClassDescriptor deserializedClassDescriptor = this.f11068f;
                return EnumEntrySyntheticClassDescriptor.U0(deserializedClassDescriptor.j1().h(), deserializedClassDescriptor, name, c.this.f11065c, new ed.a(deserializedClassDescriptor.j1().h(), new C0033a(deserializedClassDescriptor, gVar)), SourceElement.f16664a);
            }
        }

        /* compiled from: DeserializedClassDescriptor.kt */
        /* renamed from: ed.d$c$b */
        /* loaded from: classes2.dex */
        static final class b extends Lambda implements ya.a<Set<? extends Name>> {
            b() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Set<Name> invoke() {
                return c.this.e();
            }
        }

        public c() {
            int u7;
            int e10;
            int c10;
            List<jc.g> x02 = DeserializedClassDescriptor.this.k1().x0();
            za.k.d(x02, "classProto.enumEntryList");
            u7 = s.u(x02, 10);
            e10 = MapsJVM.e(u7);
            c10 = _Ranges.c(e10, 16);
            LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
            for (Object obj : x02) {
                linkedHashMap.put(NameResolverUtil.b(DeserializedClassDescriptor.this.j1().g(), ((jc.g) obj).A()), obj);
            }
            this.f11063a = linkedHashMap;
            this.f11064b = DeserializedClassDescriptor.this.j1().h().b(new a(DeserializedClassDescriptor.this));
            this.f11065c = DeserializedClassDescriptor.this.j1().h().g(new b());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Set<Name> e() {
            Set<Name> k10;
            HashSet hashSet = new HashSet();
            Iterator<g0> it = DeserializedClassDescriptor.this.n().q().iterator();
            while (it.hasNext()) {
                for (DeclarationDescriptor declarationDescriptor : ResolutionScope.a.a(it.next().u(), null, null, 3, null)) {
                    if ((declarationDescriptor instanceof SimpleFunctionDescriptor) || (declarationDescriptor instanceof PropertyDescriptor)) {
                        hashSet.add(declarationDescriptor.getName());
                    }
                }
            }
            List<jc.i> C0 = DeserializedClassDescriptor.this.k1().C0();
            za.k.d(C0, "classProto.functionList");
            DeserializedClassDescriptor deserializedClassDescriptor = DeserializedClassDescriptor.this;
            Iterator<T> it2 = C0.iterator();
            while (it2.hasNext()) {
                hashSet.add(NameResolverUtil.b(deserializedClassDescriptor.j1().g(), ((jc.i) it2.next()).Y()));
            }
            List<jc.n> Q0 = DeserializedClassDescriptor.this.k1().Q0();
            za.k.d(Q0, "classProto.propertyList");
            DeserializedClassDescriptor deserializedClassDescriptor2 = DeserializedClassDescriptor.this;
            Iterator<T> it3 = Q0.iterator();
            while (it3.hasNext()) {
                hashSet.add(NameResolverUtil.b(deserializedClassDescriptor2.j1().g(), ((jc.n) it3.next()).X()));
            }
            k10 = _Sets.k(hashSet, hashSet);
            return k10;
        }

        public final Collection<ClassDescriptor> d() {
            Set<Name> keySet = this.f11063a.keySet();
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = keySet.iterator();
            while (it.hasNext()) {
                ClassDescriptor f10 = f((Name) it.next());
                if (f10 != null) {
                    arrayList.add(f10);
                }
            }
            return arrayList;
        }

        public final ClassDescriptor f(Name name) {
            za.k.e(name, "name");
            return this.f11064b.invoke(name);
        }
    }

    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.a<List<? extends AnnotationDescriptor>> {
        d() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<AnnotationDescriptor> invoke() {
            List<AnnotationDescriptor> z02;
            z02 = _Collections.z0(DeserializedClassDescriptor.this.j1().c().d().i(DeserializedClassDescriptor.this.o1()));
            return z02;
        }
    }

    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$e */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.a<ClassDescriptor> {
        e() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ClassDescriptor invoke() {
            return DeserializedClassDescriptor.this.b1();
        }
    }

    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$f */
    /* loaded from: classes2.dex */
    static final class f extends Lambda implements ya.a<Collection<? extends ClassConstructorDescriptor>> {
        f() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<ClassConstructorDescriptor> invoke() {
            return DeserializedClassDescriptor.this.c1();
        }
    }

    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$g */
    /* loaded from: classes2.dex */
    /* synthetic */ class g extends FunctionReference implements ya.l<hd.g, a> {
        g(Object obj) {
            super(1, obj);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(a.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "<init>(Lorg/jetbrains/kotlin/serialization/deserialization/descriptors/DeserializedClassDescriptor;Lorg/jetbrains/kotlin/types/checker/KotlinTypeRefiner;)V";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final a invoke(hd.g gVar) {
            za.k.e(gVar, "p0");
            return new a((DeserializedClassDescriptor) this.f20351f, gVar);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "<init>";
        }
    }

    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$h */
    /* loaded from: classes2.dex */
    static final class h extends Lambda implements ya.a<ClassConstructorDescriptor> {
        h() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ClassConstructorDescriptor invoke() {
            return DeserializedClassDescriptor.this.f1();
        }
    }

    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$i */
    /* loaded from: classes2.dex */
    static final class i extends Lambda implements ya.a<Collection<? extends ClassDescriptor>> {
        i() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<ClassDescriptor> invoke() {
            return DeserializedClassDescriptor.this.h1();
        }
    }

    /* compiled from: DeserializedClassDescriptor.kt */
    /* renamed from: ed.d$j */
    /* loaded from: classes2.dex */
    static final class j extends Lambda implements ya.a<ValueClassRepresentation<o0>> {
        j() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ValueClassRepresentation<o0> invoke() {
            return DeserializedClassDescriptor.this.i1();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeserializedClassDescriptor(cd.m mVar, jc.c cVar, NameResolver nameResolver, BinaryVersion binaryVersion, SourceElement sourceElement) {
        super(mVar.h(), NameResolverUtil.a(nameResolver, cVar.z0()).j());
        MemberScopeImpl memberScopeImpl;
        qb.g nVar;
        za.k.e(mVar, "outerContext");
        za.k.e(cVar, "classProto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(binaryVersion, "metadataVersion");
        za.k.e(sourceElement, "sourceElement");
        this.f11035j = cVar;
        this.f11036k = binaryVersion;
        this.f11037l = sourceElement;
        this.f11038m = NameResolverUtil.a(nameResolver, cVar.z0());
        ProtoEnumFlags protoEnumFlags = ProtoEnumFlags.f5188a;
        this.f11039n = protoEnumFlags.b(Flags.f14670e.d(cVar.y0()));
        this.f11040o = ProtoEnumFlagsUtils.a(protoEnumFlags, Flags.f14669d.d(cVar.y0()));
        ClassKind a10 = protoEnumFlags.a(Flags.f14671f.d(cVar.y0()));
        this.f11041p = a10;
        List<jc.s> b12 = cVar.b1();
        za.k.d(b12, "classProto.typeParameterList");
        t c12 = cVar.c1();
        za.k.d(c12, "classProto.typeTable");
        TypeTable typeTable = new TypeTable(c12);
        VersionRequirement.a aVar = VersionRequirement.f14699b;
        w e12 = cVar.e1();
        za.k.d(e12, "classProto.versionRequirementTable");
        cd.m a11 = mVar.a(this, b12, nameResolver, typeTable, aVar.a(e12), binaryVersion);
        this.f11042q = a11;
        ClassKind classKind = ClassKind.ENUM_CLASS;
        if (a10 == classKind) {
            memberScopeImpl = new StaticScopeForKotlinEnum(a11.h(), this);
        } else {
            memberScopeImpl = h.b.f20465b;
        }
        this.f11043r = memberScopeImpl;
        this.f11044s = new b();
        this.f11045t = ScopesHolderForClass.f16749e.a(this, a11.h(), a11.c().m().c(), new g(this));
        this.f11046u = a10 == classKind ? new c() : null;
        DeclarationDescriptor e10 = mVar.e();
        this.f11047v = e10;
        this.f11048w = a11.h().f(new h());
        this.f11049x = a11.h().g(new f());
        this.f11050y = a11.h().f(new e());
        this.f11051z = a11.h().g(new i());
        this.A = a11.h().f(new j());
        NameResolver g6 = a11.g();
        TypeTable j10 = a11.j();
        DeserializedClassDescriptor deserializedClassDescriptor = e10 instanceof DeserializedClassDescriptor ? (DeserializedClassDescriptor) e10 : null;
        this.B = new ProtoContainer.a(cVar, g6, j10, sourceElement, deserializedClassDescriptor != null ? deserializedClassDescriptor.B : null);
        if (!Flags.f14668c.d(cVar.y0()).booleanValue()) {
            nVar = qb.g.f17195b.b();
        } else {
            nVar = new n(a11.h(), new d());
        }
        this.C = nVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ClassDescriptor b1() {
        if (!this.f11035j.f1()) {
            return null;
        }
        ClassifierDescriptor e10 = l1().e(NameResolverUtil.b(this.f11042q.g(), this.f11035j.l0()), xb.d.FROM_DESERIALIZATION);
        if (e10 instanceof ClassDescriptor) {
            return (ClassDescriptor) e10;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Collection<ClassConstructorDescriptor> c1() {
        List n10;
        List m02;
        List m03;
        List<ClassConstructorDescriptor> g12 = g1();
        n10 = kotlin.collections.r.n(Z());
        m02 = _Collections.m0(g12, n10);
        m03 = _Collections.m0(m02, this.f11042q.c().c().e(this));
        return m03;
    }

    private final InlineClassRepresentation<o0> d1() {
        Object T;
        Name name;
        o0 o0Var;
        Object obj = null;
        if (!y() && !q()) {
            return null;
        }
        if (q() && !this.f11035j.i1() && !this.f11035j.j1() && !this.f11035j.k1() && this.f11035j.G0() > 0) {
            return null;
        }
        if (this.f11035j.i1()) {
            name = NameResolverUtil.b(this.f11042q.g(), this.f11035j.D0());
        } else if (!this.f11036k.c(1, 5, 1)) {
            ClassConstructorDescriptor Z = Z();
            if (Z != null) {
                List<ValueParameterDescriptor> l10 = Z.l();
                za.k.d(l10, "constructor.valueParameters");
                T = _Collections.T(l10);
                name = ((ValueParameterDescriptor) T).getName();
                za.k.d(name, "{\n                // Bef…irst().name\n            }");
            } else {
                throw new IllegalStateException(("Inline class has no primary constructor: " + this).toString());
            }
        } else {
            throw new IllegalStateException(("Inline class has no underlying property name in metadata: " + this).toString());
        }
        q i10 = protoTypeTableUtil.i(this.f11035j, this.f11042q.j());
        if (i10 == null || (o0Var = d0.n(this.f11042q.i(), i10, false, 2, null)) == null) {
            Iterator<T> it = l1().a(name, xb.d.FROM_DESERIALIZATION).iterator();
            Object obj2 = null;
            boolean z10 = false;
            while (true) {
                if (it.hasNext()) {
                    Object next = it.next();
                    if (((PropertyDescriptor) next).r0() == null) {
                        if (z10) {
                            break;
                        }
                        z10 = true;
                        obj2 = next;
                    }
                } else if (z10) {
                    obj = obj2;
                }
            }
            PropertyDescriptor propertyDescriptor = (PropertyDescriptor) obj;
            if (propertyDescriptor != null) {
                g0 type = propertyDescriptor.getType();
                za.k.c(type, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
                o0Var = (o0) type;
            } else {
                throw new IllegalStateException(("Value class has no underlying property: " + this).toString());
            }
        }
        return new InlineClassRepresentation<>(name, o0Var);
    }

    private final MultiFieldValueClassRepresentation<o0> e1() {
        int u7;
        List<q> M0;
        int u10;
        List G0;
        int u11;
        List<Integer> H0 = this.f11035j.H0();
        za.k.d(H0, "classProto.multiFieldValueClassUnderlyingNameList");
        u7 = s.u(H0, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (Integer num : H0) {
            NameResolver g6 = this.f11042q.g();
            za.k.d(num, "it");
            arrayList.add(NameResolverUtil.b(g6, num.intValue()));
        }
        if (!(!arrayList.isEmpty())) {
            arrayList = null;
        }
        if (arrayList == null) {
            return null;
        }
        if (q()) {
            o a10 = ma.u.a(Integer.valueOf(this.f11035j.K0()), Integer.valueOf(this.f11035j.J0()));
            if (za.k.a(a10, ma.u.a(Integer.valueOf(arrayList.size()), 0))) {
                List<Integer> L0 = this.f11035j.L0();
                za.k.d(L0, "classProto.multiFieldVal…ClassUnderlyingTypeIdList");
                u11 = s.u(L0, 10);
                M0 = new ArrayList<>(u11);
                for (Integer num2 : L0) {
                    TypeTable j10 = this.f11042q.j();
                    za.k.d(num2, "it");
                    M0.add(j10.a(num2.intValue()));
                }
            } else {
                if (!za.k.a(a10, ma.u.a(0, Integer.valueOf(arrayList.size())))) {
                    throw new IllegalStateException(("Illegal multi-field value class representation: " + this).toString());
                }
                M0 = this.f11035j.M0();
            }
            za.k.d(M0, "when (typeIdCount to typ…tation: $this\")\n        }");
            u10 = s.u(M0, 10);
            ArrayList arrayList2 = new ArrayList(u10);
            for (q qVar : M0) {
                d0 i10 = this.f11042q.i();
                za.k.d(qVar, "it");
                arrayList2.add(d0.n(i10, qVar, false, 2, null));
            }
            G0 = _Collections.G0(arrayList, arrayList2);
            return new MultiFieldValueClassRepresentation<>(G0);
        }
        throw new IllegalArgumentException(("Not a value class: " + this).toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ClassConstructorDescriptor f1() {
        Object obj;
        if (this.f11041p.b()) {
            ClassConstructorDescriptorImpl l10 = DescriptorFactory.l(this, SourceElement.f16664a);
            l10.p1(x());
            return l10;
        }
        List<jc.d> o02 = this.f11035j.o0();
        za.k.d(o02, "classProto.constructorList");
        Iterator<T> it = o02.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (!Flags.f14678m.d(((jc.d) obj).E()).booleanValue()) {
                break;
            }
        }
        jc.d dVar = (jc.d) obj;
        if (dVar != null) {
            return this.f11042q.f().i(dVar, true);
        }
        return null;
    }

    private final List<ClassConstructorDescriptor> g1() {
        int u7;
        List<jc.d> o02 = this.f11035j.o0();
        za.k.d(o02, "classProto.constructorList");
        ArrayList<jc.d> arrayList = new ArrayList();
        for (Object obj : o02) {
            Boolean d10 = Flags.f14678m.d(((jc.d) obj).E());
            za.k.d(d10, "IS_SECONDARY.get(it.flags)");
            if (d10.booleanValue()) {
                arrayList.add(obj);
            }
        }
        u7 = s.u(arrayList, 10);
        ArrayList arrayList2 = new ArrayList(u7);
        for (jc.d dVar : arrayList) {
            MemberDeserializer f10 = this.f11042q.f();
            za.k.d(dVar, "it");
            arrayList2.add(f10.i(dVar, false));
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Collection<ClassDescriptor> h1() {
        List j10;
        if (this.f11039n != Modality.SEALED) {
            j10 = kotlin.collections.r.j();
            return j10;
        }
        List<Integer> R0 = this.f11035j.R0();
        za.k.d(R0, "fqNames");
        if (!R0.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            for (Integer num : R0) {
                cd.k c10 = this.f11042q.c();
                NameResolver g6 = this.f11042q.g();
                za.k.d(num, ThermalBaseConfig.Item.ATTR_INDEX);
                ClassDescriptor b10 = c10.b(NameResolverUtil.a(g6, num.intValue()));
                if (b10 != null) {
                    arrayList.add(b10);
                }
            }
            return arrayList;
        }
        return sc.a.f18417a.a(this, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ValueClassRepresentation<o0> i1() {
        InlineClassRepresentation<o0> d12 = d1();
        MultiFieldValueClassRepresentation<o0> e12 = e1();
        if (d12 != null && e12 != null) {
            throw new IllegalArgumentException("Class cannot have both inline class representation and multi field class representation: " + this);
        }
        if ((!q() && !y()) || d12 != null || e12 != null) {
            return d12 != null ? d12 : e12;
        }
        throw new IllegalArgumentException("Value class has no value class representation: " + this);
    }

    private final a l1() {
        return this.f11045t.c(this.f11042q.c().m().c());
    }

    @Override // pb.ClassDescriptor, pb.ClassifierDescriptorWithTypeParameters
    public List<TypeParameterDescriptor> B() {
        return this.f11042q.i().j();
    }

    @Override // pb.MemberDescriptor
    public boolean D() {
        Boolean d10 = Flags.f14674i.d(this.f11035j.y0());
        za.k.d(d10, "IS_EXTERNAL_CLASS.get(classProto.flags)");
        return d10.booleanValue();
    }

    @Override // pb.ClassDescriptor
    public boolean F() {
        return Flags.f14671f.d(this.f11035j.y0()) == c.EnumC0065c.COMPANION_OBJECT;
    }

    @Override // pb.ClassDescriptor
    public ValueClassRepresentation<o0> G0() {
        return this.A.invoke();
    }

    @Override // pb.ClassDescriptor
    public boolean L() {
        Boolean d10 = Flags.f14677l.d(this.f11035j.y0());
        za.k.d(d10, "IS_FUN_INTERFACE.get(classProto.flags)");
        return d10.booleanValue();
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return false;
    }

    @Override // sb.AbstractClassDescriptor, pb.ClassDescriptor
    public List<ReceiverParameterDescriptor> P0() {
        int u7;
        List<q> b10 = protoTypeTableUtil.b(this.f11035j, this.f11042q.j());
        u7 = s.u(b10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = b10.iterator();
        while (it.hasNext()) {
            arrayList.add(new ReceiverParameterDescriptorImpl(S0(), new ContextClassReceiver(this, this.f11042q.i().q((q) it.next()), null, null), qb.g.f17195b.b()));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.t
    public zc.h Q(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this.f11045t.c(gVar);
    }

    @Override // pb.ClassDescriptor
    public boolean R0() {
        Boolean d10 = Flags.f14673h.d(this.f11035j.y0());
        za.k.d(d10, "IS_DATA.get(classProto.flags)");
        return d10.booleanValue();
    }

    @Override // pb.ClassDescriptor
    public Collection<ClassDescriptor> S() {
        return this.f11051z.invoke();
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        Boolean d10 = Flags.f14675j.d(this.f11035j.y0());
        za.k.d(d10, "IS_EXPECT_CLASS.get(classProto.flags)");
        return d10.booleanValue();
    }

    @Override // pb.ClassDescriptor
    public ClassConstructorDescriptor Z() {
        return this.f11048w.invoke();
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    public DeclarationDescriptor b() {
        return this.f11047v;
    }

    @Override // pb.ClassDescriptor
    public ClassDescriptor c0() {
        return this.f11050y.invoke();
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public u g() {
        return this.f11040o;
    }

    @Override // pb.ClassDescriptor
    public ClassKind getKind() {
        return this.f11041p;
    }

    @Override // qb.a
    public qb.g i() {
        return this.C;
    }

    public final cd.m j1() {
        return this.f11042q;
    }

    public final jc.c k1() {
        return this.f11035j;
    }

    public final BinaryVersion m1() {
        return this.f11036k;
    }

    @Override // pb.ClassifierDescriptor
    public TypeConstructor n() {
        return this.f11044s;
    }

    @Override // pb.ClassDescriptor
    /* renamed from: n1, reason: merged with bridge method [inline-methods] */
    public MemberScopeImpl a0() {
        return this.f11043r;
    }

    @Override // pb.ClassDescriptor, pb.MemberDescriptor
    public Modality o() {
        return this.f11039n;
    }

    public final ProtoContainer.a o1() {
        return this.B;
    }

    @Override // pb.ClassDescriptor
    public Collection<ClassConstructorDescriptor> p() {
        return this.f11049x.invoke();
    }

    public final boolean p1(Name name) {
        za.k.e(name, "name");
        return l1().q().contains(name);
    }

    @Override // pb.ClassDescriptor
    public boolean q() {
        Boolean d10 = Flags.f14676k.d(this.f11035j.y0());
        za.k.d(d10, "IS_VALUE_CLASS.get(classProto.flags)");
        return d10.booleanValue() && this.f11036k.c(1, 4, 2);
    }

    @Override // pb.ClassifierDescriptorWithTypeParameters
    public boolean r() {
        Boolean d10 = Flags.f14672g.d(this.f11035j.y0());
        za.k.d(d10, "IS_INNER.get(classProto.flags)");
        return d10.booleanValue();
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("deserialized ");
        sb2.append(U() ? "expect " : "");
        sb2.append("class ");
        sb2.append(getName());
        return sb2.toString();
    }

    @Override // pb.ClassDescriptor
    public boolean y() {
        Boolean d10 = Flags.f14676k.d(this.f11035j.y0());
        za.k.d(d10, "IS_VALUE_CLASS.get(classProto.flags)");
        return d10.booleanValue() && this.f11036k.e(1, 4, 1);
    }

    @Override // pb.DeclarationDescriptorWithSource
    public SourceElement z() {
        return this.f11037l;
    }
}
