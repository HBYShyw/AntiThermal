package ed;

import cd.MemberDeserializer;
import cd.NameResolverUtil;
import fb._Ranges;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jc.r;
import kotlin.collections.MapsJVM;
import kotlin.collections.MutableCollections;
import kotlin.collections.MutableCollectionsJVM;
import kotlin.collections._Collections;
import kotlin.collections._Sets;
import kotlin.collections.m0;
import kotlin.collections.s;
import ma.Unit;
import oc.ClassId;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.TypeAliasDescriptor;
import qc.q;
import qd.collections;
import rd.Sequence;
import rd._Sequences;
import sc.MemberComparator;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import zc.MemberScopeImpl;
import zc.d;

/* compiled from: DeserializedMemberScope.kt */
/* renamed from: ed.h, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class DeserializedMemberScope extends MemberScopeImpl {

    /* renamed from: f, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f11082f = {Reflection.g(new PropertyReference1Impl(Reflection.b(DeserializedMemberScope.class), "classNames", "getClassNames$deserialization()Ljava/util/Set;")), Reflection.g(new PropertyReference1Impl(Reflection.b(DeserializedMemberScope.class), "classifierNamesLazy", "getClassifierNamesLazy()Ljava/util/Set;"))};

    /* renamed from: b, reason: collision with root package name */
    private final cd.m f11083b;

    /* renamed from: c, reason: collision with root package name */
    private final a f11084c;

    /* renamed from: d, reason: collision with root package name */
    private final fd.i f11085d;

    /* renamed from: e, reason: collision with root package name */
    private final fd.j f11086e;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DeserializedMemberScope.kt */
    /* renamed from: ed.h$a */
    /* loaded from: classes2.dex */
    public interface a {
        Collection<PropertyDescriptor> a(Name name, xb.b bVar);

        Set<Name> b();

        Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar);

        Set<Name> d();

        void e(Collection<DeclarationDescriptor> collection, zc.d dVar, ya.l<? super Name, Boolean> lVar, xb.b bVar);

        TypeAliasDescriptor f(Name name);

        Set<Name> g();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DeserializedMemberScope.kt */
    /* renamed from: ed.h$b */
    /* loaded from: classes2.dex */
    public final class b implements a {

        /* renamed from: o, reason: collision with root package name */
        static final /* synthetic */ gb.l<Object>[] f11087o = {Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "declaredFunctions", "getDeclaredFunctions()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "declaredProperties", "getDeclaredProperties()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "allTypeAliases", "getAllTypeAliases()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "allFunctions", "getAllFunctions()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "allProperties", "getAllProperties()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "typeAliasesByName", "getTypeAliasesByName()Ljava/util/Map;")), Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "functionsByName", "getFunctionsByName()Ljava/util/Map;")), Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "propertiesByName", "getPropertiesByName()Ljava/util/Map;")), Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "functionNames", "getFunctionNames()Ljava/util/Set;")), Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "variableNames", "getVariableNames()Ljava/util/Set;"))};

        /* renamed from: a, reason: collision with root package name */
        private final List<jc.i> f11088a;

        /* renamed from: b, reason: collision with root package name */
        private final List<jc.n> f11089b;

        /* renamed from: c, reason: collision with root package name */
        private final List<r> f11090c;

        /* renamed from: d, reason: collision with root package name */
        private final fd.i f11091d;

        /* renamed from: e, reason: collision with root package name */
        private final fd.i f11092e;

        /* renamed from: f, reason: collision with root package name */
        private final fd.i f11093f;

        /* renamed from: g, reason: collision with root package name */
        private final fd.i f11094g;

        /* renamed from: h, reason: collision with root package name */
        private final fd.i f11095h;

        /* renamed from: i, reason: collision with root package name */
        private final fd.i f11096i;

        /* renamed from: j, reason: collision with root package name */
        private final fd.i f11097j;

        /* renamed from: k, reason: collision with root package name */
        private final fd.i f11098k;

        /* renamed from: l, reason: collision with root package name */
        private final fd.i f11099l;

        /* renamed from: m, reason: collision with root package name */
        private final fd.i f11100m;

        /* renamed from: n, reason: collision with root package name */
        final /* synthetic */ DeserializedMemberScope f11101n;

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$a */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.a<List<? extends SimpleFunctionDescriptor>> {
            a() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<SimpleFunctionDescriptor> invoke() {
                List<SimpleFunctionDescriptor> m02;
                m02 = _Collections.m0(b.this.D(), b.this.t());
                return m02;
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0034b extends Lambda implements ya.a<List<? extends PropertyDescriptor>> {
            C0034b() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<PropertyDescriptor> invoke() {
                List<PropertyDescriptor> m02;
                m02 = _Collections.m0(b.this.E(), b.this.u());
                return m02;
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$c */
        /* loaded from: classes2.dex */
        static final class c extends Lambda implements ya.a<List<? extends TypeAliasDescriptor>> {
            c() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<TypeAliasDescriptor> invoke() {
                return b.this.z();
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$d */
        /* loaded from: classes2.dex */
        static final class d extends Lambda implements ya.a<List<? extends SimpleFunctionDescriptor>> {
            d() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<SimpleFunctionDescriptor> invoke() {
                return b.this.v();
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$e */
        /* loaded from: classes2.dex */
        static final class e extends Lambda implements ya.a<List<? extends PropertyDescriptor>> {
            e() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<PropertyDescriptor> invoke() {
                return b.this.y();
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$f */
        /* loaded from: classes2.dex */
        static final class f extends Lambda implements ya.a<Set<? extends Name>> {

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ DeserializedMemberScope f11108f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            f(DeserializedMemberScope deserializedMemberScope) {
                super(0);
                this.f11108f = deserializedMemberScope;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Set<Name> invoke() {
                Set<Name> k10;
                b bVar = b.this;
                List list = bVar.f11088a;
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                DeserializedMemberScope deserializedMemberScope = bVar.f11101n;
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    linkedHashSet.add(NameResolverUtil.b(deserializedMemberScope.p().g(), ((jc.i) ((q) it.next())).Y()));
                }
                k10 = _Sets.k(linkedHashSet, this.f11108f.t());
                return k10;
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$g */
        /* loaded from: classes2.dex */
        static final class g extends Lambda implements ya.a<Map<Name, ? extends List<? extends SimpleFunctionDescriptor>>> {
            g() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Map<Name, List<SimpleFunctionDescriptor>> invoke() {
                List A = b.this.A();
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (Object obj : A) {
                    Name name = ((SimpleFunctionDescriptor) obj).getName();
                    za.k.d(name, "it.name");
                    Object obj2 = linkedHashMap.get(name);
                    if (obj2 == null) {
                        obj2 = new ArrayList();
                        linkedHashMap.put(name, obj2);
                    }
                    ((List) obj2).add(obj);
                }
                return linkedHashMap;
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$h */
        /* loaded from: classes2.dex */
        static final class h extends Lambda implements ya.a<Map<Name, ? extends List<? extends PropertyDescriptor>>> {
            h() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Map<Name, List<PropertyDescriptor>> invoke() {
                List B = b.this.B();
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (Object obj : B) {
                    Name name = ((PropertyDescriptor) obj).getName();
                    za.k.d(name, "it.name");
                    Object obj2 = linkedHashMap.get(name);
                    if (obj2 == null) {
                        obj2 = new ArrayList();
                        linkedHashMap.put(name, obj2);
                    }
                    ((List) obj2).add(obj);
                }
                return linkedHashMap;
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$i */
        /* loaded from: classes2.dex */
        static final class i extends Lambda implements ya.a<Map<Name, ? extends TypeAliasDescriptor>> {
            i() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Map<Name, TypeAliasDescriptor> invoke() {
                int u7;
                int e10;
                int c10;
                List C = b.this.C();
                u7 = s.u(C, 10);
                e10 = MapsJVM.e(u7);
                c10 = _Ranges.c(e10, 16);
                LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
                for (Object obj : C) {
                    Name name = ((TypeAliasDescriptor) obj).getName();
                    za.k.d(name, "it.name");
                    linkedHashMap.put(name, obj);
                }
                return linkedHashMap;
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$b$j */
        /* loaded from: classes2.dex */
        static final class j extends Lambda implements ya.a<Set<? extends Name>> {

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ DeserializedMemberScope f11113f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            j(DeserializedMemberScope deserializedMemberScope) {
                super(0);
                this.f11113f = deserializedMemberScope;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Set<Name> invoke() {
                Set<Name> k10;
                b bVar = b.this;
                List list = bVar.f11089b;
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                DeserializedMemberScope deserializedMemberScope = bVar.f11101n;
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    linkedHashSet.add(NameResolverUtil.b(deserializedMemberScope.p().g(), ((jc.n) ((q) it.next())).X()));
                }
                k10 = _Sets.k(linkedHashSet, this.f11113f.u());
                return k10;
            }
        }

        public b(DeserializedMemberScope deserializedMemberScope, List<jc.i> list, List<jc.n> list2, List<r> list3) {
            za.k.e(list, "functionList");
            za.k.e(list2, "propertyList");
            za.k.e(list3, "typeAliasList");
            this.f11101n = deserializedMemberScope;
            this.f11088a = list;
            this.f11089b = list2;
            this.f11090c = deserializedMemberScope.p().c().g().f() ? list3 : kotlin.collections.r.j();
            this.f11091d = deserializedMemberScope.p().h().g(new d());
            this.f11092e = deserializedMemberScope.p().h().g(new e());
            this.f11093f = deserializedMemberScope.p().h().g(new c());
            this.f11094g = deserializedMemberScope.p().h().g(new a());
            this.f11095h = deserializedMemberScope.p().h().g(new C0034b());
            this.f11096i = deserializedMemberScope.p().h().g(new i());
            this.f11097j = deserializedMemberScope.p().h().g(new g());
            this.f11098k = deserializedMemberScope.p().h().g(new h());
            this.f11099l = deserializedMemberScope.p().h().g(new f(deserializedMemberScope));
            this.f11100m = deserializedMemberScope.p().h().g(new j(deserializedMemberScope));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<SimpleFunctionDescriptor> A() {
            return (List) fd.m.a(this.f11094g, this, f11087o[3]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<PropertyDescriptor> B() {
            return (List) fd.m.a(this.f11095h, this, f11087o[4]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<TypeAliasDescriptor> C() {
            return (List) fd.m.a(this.f11093f, this, f11087o[2]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<SimpleFunctionDescriptor> D() {
            return (List) fd.m.a(this.f11091d, this, f11087o[0]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<PropertyDescriptor> E() {
            return (List) fd.m.a(this.f11092e, this, f11087o[1]);
        }

        private final Map<Name, Collection<SimpleFunctionDescriptor>> F() {
            return (Map) fd.m.a(this.f11097j, this, f11087o[6]);
        }

        private final Map<Name, Collection<PropertyDescriptor>> G() {
            return (Map) fd.m.a(this.f11098k, this, f11087o[7]);
        }

        private final Map<Name, TypeAliasDescriptor> H() {
            return (Map) fd.m.a(this.f11096i, this, f11087o[5]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<SimpleFunctionDescriptor> t() {
            Set<Name> t7 = this.f11101n.t();
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = t7.iterator();
            while (it.hasNext()) {
                MutableCollections.z(arrayList, w((Name) it.next()));
            }
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<PropertyDescriptor> u() {
            Set<Name> u7 = this.f11101n.u();
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = u7.iterator();
            while (it.hasNext()) {
                MutableCollections.z(arrayList, x((Name) it.next()));
            }
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<SimpleFunctionDescriptor> v() {
            List<jc.i> list = this.f11088a;
            DeserializedMemberScope deserializedMemberScope = this.f11101n;
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                SimpleFunctionDescriptor j10 = deserializedMemberScope.p().f().j((jc.i) ((q) it.next()));
                if (!deserializedMemberScope.x(j10)) {
                    j10 = null;
                }
                if (j10 != null) {
                    arrayList.add(j10);
                }
            }
            return arrayList;
        }

        private final List<SimpleFunctionDescriptor> w(Name name) {
            List<SimpleFunctionDescriptor> D = D();
            DeserializedMemberScope deserializedMemberScope = this.f11101n;
            ArrayList arrayList = new ArrayList();
            for (Object obj : D) {
                if (za.k.a(((DeclarationDescriptor) obj).getName(), name)) {
                    arrayList.add(obj);
                }
            }
            int size = arrayList.size();
            deserializedMemberScope.k(name, arrayList);
            return arrayList.subList(size, arrayList.size());
        }

        private final List<PropertyDescriptor> x(Name name) {
            List<PropertyDescriptor> E = E();
            DeserializedMemberScope deserializedMemberScope = this.f11101n;
            ArrayList arrayList = new ArrayList();
            for (Object obj : E) {
                if (za.k.a(((DeclarationDescriptor) obj).getName(), name)) {
                    arrayList.add(obj);
                }
            }
            int size = arrayList.size();
            deserializedMemberScope.l(name, arrayList);
            return arrayList.subList(size, arrayList.size());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<PropertyDescriptor> y() {
            List<jc.n> list = this.f11089b;
            DeserializedMemberScope deserializedMemberScope = this.f11101n;
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                PropertyDescriptor l10 = deserializedMemberScope.p().f().l((jc.n) ((q) it.next()));
                if (l10 != null) {
                    arrayList.add(l10);
                }
            }
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<TypeAliasDescriptor> z() {
            List<r> list = this.f11090c;
            DeserializedMemberScope deserializedMemberScope = this.f11101n;
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                TypeAliasDescriptor m10 = deserializedMemberScope.p().f().m((r) ((q) it.next()));
                if (m10 != null) {
                    arrayList.add(m10);
                }
            }
            return arrayList;
        }

        @Override // ed.DeserializedMemberScope.a
        public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
            List j10;
            List j11;
            za.k.e(name, "name");
            za.k.e(bVar, "location");
            if (!d().contains(name)) {
                j11 = kotlin.collections.r.j();
                return j11;
            }
            Collection<PropertyDescriptor> collection = G().get(name);
            if (collection != null) {
                return collection;
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }

        @Override // ed.DeserializedMemberScope.a
        public Set<Name> b() {
            return (Set) fd.m.a(this.f11099l, this, f11087o[8]);
        }

        @Override // ed.DeserializedMemberScope.a
        public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
            List j10;
            List j11;
            za.k.e(name, "name");
            za.k.e(bVar, "location");
            if (!b().contains(name)) {
                j11 = kotlin.collections.r.j();
                return j11;
            }
            Collection<SimpleFunctionDescriptor> collection = F().get(name);
            if (collection != null) {
                return collection;
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }

        @Override // ed.DeserializedMemberScope.a
        public Set<Name> d() {
            return (Set) fd.m.a(this.f11100m, this, f11087o[9]);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // ed.DeserializedMemberScope.a
        public void e(Collection<DeclarationDescriptor> collection, zc.d dVar, ya.l<? super Name, Boolean> lVar, xb.b bVar) {
            za.k.e(collection, "result");
            za.k.e(dVar, "kindFilter");
            za.k.e(lVar, "nameFilter");
            za.k.e(bVar, "location");
            if (dVar.a(zc.d.f20424c.i())) {
                for (Object obj : B()) {
                    Name name = ((PropertyDescriptor) obj).getName();
                    za.k.d(name, "it.name");
                    if (lVar.invoke(name).booleanValue()) {
                        collection.add(obj);
                    }
                }
            }
            if (dVar.a(zc.d.f20424c.d())) {
                for (Object obj2 : A()) {
                    Name name2 = ((SimpleFunctionDescriptor) obj2).getName();
                    za.k.d(name2, "it.name");
                    if (lVar.invoke(name2).booleanValue()) {
                        collection.add(obj2);
                    }
                }
            }
        }

        @Override // ed.DeserializedMemberScope.a
        public TypeAliasDescriptor f(Name name) {
            za.k.e(name, "name");
            return H().get(name);
        }

        @Override // ed.DeserializedMemberScope.a
        public Set<Name> g() {
            List<r> list = this.f11090c;
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            DeserializedMemberScope deserializedMemberScope = this.f11101n;
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                linkedHashSet.add(NameResolverUtil.b(deserializedMemberScope.p().g(), ((r) ((q) it.next())).R()));
            }
            return linkedHashSet;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DeserializedMemberScope.kt */
    /* renamed from: ed.h$c */
    /* loaded from: classes2.dex */
    public final class c implements a {

        /* renamed from: j, reason: collision with root package name */
        static final /* synthetic */ gb.l<Object>[] f11114j = {Reflection.g(new PropertyReference1Impl(Reflection.b(c.class), "functionNames", "getFunctionNames()Ljava/util/Set;")), Reflection.g(new PropertyReference1Impl(Reflection.b(c.class), "variableNames", "getVariableNames()Ljava/util/Set;"))};

        /* renamed from: a, reason: collision with root package name */
        private final Map<Name, byte[]> f11115a;

        /* renamed from: b, reason: collision with root package name */
        private final Map<Name, byte[]> f11116b;

        /* renamed from: c, reason: collision with root package name */
        private final Map<Name, byte[]> f11117c;

        /* renamed from: d, reason: collision with root package name */
        private final fd.g<Name, Collection<SimpleFunctionDescriptor>> f11118d;

        /* renamed from: e, reason: collision with root package name */
        private final fd.g<Name, Collection<PropertyDescriptor>> f11119e;

        /* renamed from: f, reason: collision with root package name */
        private final fd.h<Name, TypeAliasDescriptor> f11120f;

        /* renamed from: g, reason: collision with root package name */
        private final fd.i f11121g;

        /* renamed from: h, reason: collision with root package name */
        private final fd.i f11122h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ DeserializedMemberScope f11123i;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$c$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.a {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ qc.s f11124e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ ByteArrayInputStream f11125f;

            /* renamed from: g, reason: collision with root package name */
            final /* synthetic */ DeserializedMemberScope f11126g;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public a(qc.s sVar, ByteArrayInputStream byteArrayInputStream, DeserializedMemberScope deserializedMemberScope) {
                super(0);
                this.f11124e = sVar;
                this.f11125f = byteArrayInputStream;
                this.f11126g = deserializedMemberScope;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final q invoke() {
                return (q) this.f11124e.b(this.f11125f, this.f11126g.p().c().j());
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$c$b */
        /* loaded from: classes2.dex */
        static final class b extends Lambda implements ya.a<Set<? extends Name>> {

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ DeserializedMemberScope f11128f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            b(DeserializedMemberScope deserializedMemberScope) {
                super(0);
                this.f11128f = deserializedMemberScope;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Set<Name> invoke() {
                Set<Name> k10;
                k10 = _Sets.k(c.this.f11115a.keySet(), this.f11128f.t());
                return k10;
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$c$c, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0035c extends Lambda implements ya.l<Name, Collection<? extends SimpleFunctionDescriptor>> {
            C0035c() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Collection<SimpleFunctionDescriptor> invoke(Name name) {
                za.k.e(name, "it");
                return c.this.m(name);
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$c$d */
        /* loaded from: classes2.dex */
        static final class d extends Lambda implements ya.l<Name, Collection<? extends PropertyDescriptor>> {
            d() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Collection<PropertyDescriptor> invoke(Name name) {
                za.k.e(name, "it");
                return c.this.n(name);
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$c$e */
        /* loaded from: classes2.dex */
        static final class e extends Lambda implements ya.l<Name, TypeAliasDescriptor> {
            e() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final TypeAliasDescriptor invoke(Name name) {
                za.k.e(name, "it");
                return c.this.o(name);
            }
        }

        /* compiled from: DeserializedMemberScope.kt */
        /* renamed from: ed.h$c$f */
        /* loaded from: classes2.dex */
        static final class f extends Lambda implements ya.a<Set<? extends Name>> {

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ DeserializedMemberScope f11133f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            f(DeserializedMemberScope deserializedMemberScope) {
                super(0);
                this.f11133f = deserializedMemberScope;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Set<Name> invoke() {
                Set<Name> k10;
                k10 = _Sets.k(c.this.f11116b.keySet(), this.f11133f.u());
                return k10;
            }
        }

        public c(DeserializedMemberScope deserializedMemberScope, List<jc.i> list, List<jc.n> list2, List<r> list3) {
            Map<Name, byte[]> i10;
            za.k.e(list, "functionList");
            za.k.e(list2, "propertyList");
            za.k.e(list3, "typeAliasList");
            this.f11123i = deserializedMemberScope;
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Object obj : list) {
                Name b10 = NameResolverUtil.b(deserializedMemberScope.p().g(), ((jc.i) ((q) obj)).Y());
                Object obj2 = linkedHashMap.get(b10);
                if (obj2 == null) {
                    obj2 = new ArrayList();
                    linkedHashMap.put(b10, obj2);
                }
                ((List) obj2).add(obj);
            }
            this.f11115a = p(linkedHashMap);
            DeserializedMemberScope deserializedMemberScope2 = this.f11123i;
            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
            for (Object obj3 : list2) {
                Name b11 = NameResolverUtil.b(deserializedMemberScope2.p().g(), ((jc.n) ((q) obj3)).X());
                Object obj4 = linkedHashMap2.get(b11);
                if (obj4 == null) {
                    obj4 = new ArrayList();
                    linkedHashMap2.put(b11, obj4);
                }
                ((List) obj4).add(obj3);
            }
            this.f11116b = p(linkedHashMap2);
            if (this.f11123i.p().c().g().f()) {
                DeserializedMemberScope deserializedMemberScope3 = this.f11123i;
                LinkedHashMap linkedHashMap3 = new LinkedHashMap();
                for (Object obj5 : list3) {
                    Name b12 = NameResolverUtil.b(deserializedMemberScope3.p().g(), ((r) ((q) obj5)).R());
                    Object obj6 = linkedHashMap3.get(b12);
                    if (obj6 == null) {
                        obj6 = new ArrayList();
                        linkedHashMap3.put(b12, obj6);
                    }
                    ((List) obj6).add(obj5);
                }
                i10 = p(linkedHashMap3);
            } else {
                i10 = m0.i();
            }
            this.f11117c = i10;
            this.f11118d = this.f11123i.p().h().d(new C0035c());
            this.f11119e = this.f11123i.p().h().d(new d());
            this.f11120f = this.f11123i.p().h().b(new e());
            this.f11121g = this.f11123i.p().h().g(new b(this.f11123i));
            this.f11122h = this.f11123i.p().h().g(new f(this.f11123i));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:4:0x0027, code lost:
        
            if (r5 != null) goto L8;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Collection<SimpleFunctionDescriptor> m(Name name) {
            List<jc.i> j10;
            Sequence g6;
            Map<Name, byte[]> map = this.f11115a;
            qc.s<jc.i> sVar = jc.i.A;
            za.k.d(sVar, "PARSER");
            DeserializedMemberScope deserializedMemberScope = this.f11123i;
            byte[] bArr = map.get(name);
            if (bArr != null) {
                g6 = rd.l.g(new a(sVar, new ByteArrayInputStream(bArr), this.f11123i));
                j10 = _Sequences.C(g6);
            }
            j10 = kotlin.collections.r.j();
            ArrayList arrayList = new ArrayList(j10.size());
            for (jc.i iVar : j10) {
                MemberDeserializer f10 = deserializedMemberScope.p().f();
                za.k.d(iVar, "it");
                SimpleFunctionDescriptor j11 = f10.j(iVar);
                if (!deserializedMemberScope.x(j11)) {
                    j11 = null;
                }
                if (j11 != null) {
                    arrayList.add(j11);
                }
            }
            deserializedMemberScope.k(name, arrayList);
            return collections.c(arrayList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:4:0x0027, code lost:
        
            if (r5 != null) goto L8;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Collection<PropertyDescriptor> n(Name name) {
            List<jc.n> j10;
            Sequence g6;
            Map<Name, byte[]> map = this.f11116b;
            qc.s<jc.n> sVar = jc.n.A;
            za.k.d(sVar, "PARSER");
            DeserializedMemberScope deserializedMemberScope = this.f11123i;
            byte[] bArr = map.get(name);
            if (bArr != null) {
                g6 = rd.l.g(new a(sVar, new ByteArrayInputStream(bArr), this.f11123i));
                j10 = _Sequences.C(g6);
            }
            j10 = kotlin.collections.r.j();
            ArrayList arrayList = new ArrayList(j10.size());
            for (jc.n nVar : j10) {
                MemberDeserializer f10 = deserializedMemberScope.p().f();
                za.k.d(nVar, "it");
                PropertyDescriptor l10 = f10.l(nVar);
                if (l10 != null) {
                    arrayList.add(l10);
                }
            }
            deserializedMemberScope.l(name, arrayList);
            return collections.c(arrayList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final TypeAliasDescriptor o(Name name) {
            r i02;
            byte[] bArr = this.f11117c.get(name);
            if (bArr == null || (i02 = r.i0(new ByteArrayInputStream(bArr), this.f11123i.p().c().j())) == null) {
                return null;
            }
            return this.f11123i.p().f().m(i02);
        }

        private final Map<Name, byte[]> p(Map<Name, ? extends Collection<? extends qc.a>> map) {
            int e10;
            int u7;
            e10 = MapsJVM.e(map.size());
            LinkedHashMap linkedHashMap = new LinkedHashMap(e10);
            Iterator<T> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Iterable iterable = (Iterable) entry.getValue();
                u7 = s.u(iterable, 10);
                ArrayList arrayList = new ArrayList(u7);
                Iterator it2 = iterable.iterator();
                while (it2.hasNext()) {
                    ((qc.a) it2.next()).c(byteArrayOutputStream);
                    arrayList.add(Unit.f15173a);
                }
                linkedHashMap.put(key, byteArrayOutputStream.toByteArray());
            }
            return linkedHashMap;
        }

        @Override // ed.DeserializedMemberScope.a
        public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
            List j10;
            za.k.e(name, "name");
            za.k.e(bVar, "location");
            if (d().contains(name)) {
                return this.f11119e.invoke(name);
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }

        @Override // ed.DeserializedMemberScope.a
        public Set<Name> b() {
            return (Set) fd.m.a(this.f11121g, this, f11114j[0]);
        }

        @Override // ed.DeserializedMemberScope.a
        public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
            List j10;
            za.k.e(name, "name");
            za.k.e(bVar, "location");
            if (b().contains(name)) {
                return this.f11118d.invoke(name);
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }

        @Override // ed.DeserializedMemberScope.a
        public Set<Name> d() {
            return (Set) fd.m.a(this.f11122h, this, f11114j[1]);
        }

        @Override // ed.DeserializedMemberScope.a
        public void e(Collection<DeclarationDescriptor> collection, zc.d dVar, ya.l<? super Name, Boolean> lVar, xb.b bVar) {
            za.k.e(collection, "result");
            za.k.e(dVar, "kindFilter");
            za.k.e(lVar, "nameFilter");
            za.k.e(bVar, "location");
            if (dVar.a(zc.d.f20424c.i())) {
                Set<Name> d10 = d();
                ArrayList arrayList = new ArrayList();
                for (Name name : d10) {
                    if (lVar.invoke(name).booleanValue()) {
                        arrayList.addAll(a(name, bVar));
                    }
                }
                MemberComparator memberComparator = MemberComparator.f18438e;
                za.k.d(memberComparator, "INSTANCE");
                MutableCollectionsJVM.y(arrayList, memberComparator);
                collection.addAll(arrayList);
            }
            if (dVar.a(zc.d.f20424c.d())) {
                Set<Name> b10 = b();
                ArrayList arrayList2 = new ArrayList();
                for (Name name2 : b10) {
                    if (lVar.invoke(name2).booleanValue()) {
                        arrayList2.addAll(c(name2, bVar));
                    }
                }
                MemberComparator memberComparator2 = MemberComparator.f18438e;
                za.k.d(memberComparator2, "INSTANCE");
                MutableCollectionsJVM.y(arrayList2, memberComparator2);
                collection.addAll(arrayList2);
            }
        }

        @Override // ed.DeserializedMemberScope.a
        public TypeAliasDescriptor f(Name name) {
            za.k.e(name, "name");
            return this.f11120f.invoke(name);
        }

        @Override // ed.DeserializedMemberScope.a
        public Set<Name> g() {
            return this.f11117c.keySet();
        }
    }

    /* compiled from: DeserializedMemberScope.kt */
    /* renamed from: ed.h$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.a<Set<? extends Name>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ya.a<Collection<Name>> f11134e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        d(ya.a<? extends Collection<Name>> aVar) {
            super(0);
            this.f11134e = aVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Set<Name> invoke() {
            Set<Name> D0;
            D0 = _Collections.D0(this.f11134e.invoke());
            return D0;
        }
    }

    /* compiled from: DeserializedMemberScope.kt */
    /* renamed from: ed.h$e */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.a<Set<? extends Name>> {
        e() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Set<Name> invoke() {
            Set k10;
            Set<Name> k11;
            Set<Name> s7 = DeserializedMemberScope.this.s();
            if (s7 == null) {
                return null;
            }
            k10 = _Sets.k(DeserializedMemberScope.this.q(), DeserializedMemberScope.this.f11084c.g());
            k11 = _Sets.k(k10, s7);
            return k11;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DeserializedMemberScope(cd.m mVar, List<jc.i> list, List<jc.n> list2, List<r> list3, ya.a<? extends Collection<Name>> aVar) {
        za.k.e(mVar, "c");
        za.k.e(list, "functionList");
        za.k.e(list2, "propertyList");
        za.k.e(list3, "typeAliasList");
        za.k.e(aVar, "classNames");
        this.f11083b = mVar;
        this.f11084c = n(list, list2, list3);
        this.f11085d = mVar.h().g(new d(aVar));
        this.f11086e = mVar.h().f(new e());
    }

    private final a n(List<jc.i> list, List<jc.n> list2, List<r> list3) {
        if (this.f11083b.c().g().a()) {
            return new b(this, list, list2, list3);
        }
        return new c(this, list, list2, list3);
    }

    private final ClassDescriptor o(Name name) {
        return this.f11083b.c().b(m(name));
    }

    private final Set<Name> r() {
        return (Set) fd.m.b(this.f11086e, this, f11082f[1]);
    }

    private final TypeAliasDescriptor v(Name name) {
        return this.f11084c.f(name);
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return this.f11084c.a(name, bVar);
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> b() {
        return this.f11084c.b();
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return this.f11084c.c(name, bVar);
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> d() {
        return this.f11084c.d();
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        if (w(name)) {
            return o(name);
        }
        if (this.f11084c.g().contains(name)) {
            return v(name);
        }
        return null;
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> f() {
        return r();
    }

    protected abstract void i(Collection<DeclarationDescriptor> collection, ya.l<? super Name, Boolean> lVar);

    /* JADX INFO: Access modifiers changed from: protected */
    public final Collection<DeclarationDescriptor> j(zc.d dVar, ya.l<? super Name, Boolean> lVar, xb.b bVar) {
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        za.k.e(bVar, "location");
        ArrayList arrayList = new ArrayList(0);
        d.a aVar = zc.d.f20424c;
        if (dVar.a(aVar.g())) {
            i(arrayList, lVar);
        }
        this.f11084c.e(arrayList, dVar, lVar, bVar);
        if (dVar.a(aVar.c())) {
            for (Name name : q()) {
                if (lVar.invoke(name).booleanValue()) {
                    collections.a(arrayList, o(name));
                }
            }
        }
        if (dVar.a(zc.d.f20424c.h())) {
            for (Name name2 : this.f11084c.g()) {
                if (lVar.invoke(name2).booleanValue()) {
                    collections.a(arrayList, this.f11084c.f(name2));
                }
            }
        }
        return collections.c(arrayList);
    }

    protected void k(Name name, List<SimpleFunctionDescriptor> list) {
        za.k.e(name, "name");
        za.k.e(list, "functions");
    }

    protected void l(Name name, List<PropertyDescriptor> list) {
        za.k.e(name, "name");
        za.k.e(list, "descriptors");
    }

    protected abstract ClassId m(Name name);

    /* JADX INFO: Access modifiers changed from: protected */
    public final cd.m p() {
        return this.f11083b;
    }

    public final Set<Name> q() {
        return (Set) fd.m.a(this.f11085d, this, f11082f[0]);
    }

    protected abstract Set<Name> s();

    protected abstract Set<Name> t();

    protected abstract Set<Name> u();

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean w(Name name) {
        za.k.e(name, "name");
        return q().contains(name);
    }

    protected boolean x(SimpleFunctionDescriptor simpleFunctionDescriptor) {
        za.k.e(simpleFunctionDescriptor, "function");
        return true;
    }
}
