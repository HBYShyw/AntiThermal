package cd;

import bd.SerializerExtensionProtocol;
import cd.ProtoContainer;
import gd.g0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jc.b;
import lc.NameResolver;
import lc.ProtoBufUtil;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import qb.AnnotationDescriptor;

/* compiled from: AnnotationAndConstantLoaderImpl.kt */
/* renamed from: cd.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class AnnotationAndConstantLoaderImpl implements AnnotationAndConstantLoader<AnnotationDescriptor, uc.g<?>> {

    /* renamed from: a, reason: collision with root package name */
    private final SerializerExtensionProtocol f5207a;

    /* renamed from: b, reason: collision with root package name */
    private final AnnotationDeserializer f5208b;

    /* compiled from: AnnotationAndConstantLoaderImpl.kt */
    /* renamed from: cd.d$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f5209a;

        static {
            int[] iArr = new int[AnnotatedCallableKind.values().length];
            try {
                iArr[AnnotatedCallableKind.PROPERTY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[AnnotatedCallableKind.PROPERTY_GETTER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[AnnotatedCallableKind.PROPERTY_SETTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f5209a = iArr;
        }
    }

    public AnnotationAndConstantLoaderImpl(ModuleDescriptor moduleDescriptor, NotFoundClasses notFoundClasses, SerializerExtensionProtocol serializerExtensionProtocol) {
        za.k.e(moduleDescriptor, "module");
        za.k.e(notFoundClasses, "notFoundClasses");
        za.k.e(serializerExtensionProtocol, "protocol");
        this.f5207a = serializerExtensionProtocol;
        this.f5208b = new AnnotationDeserializer(moduleDescriptor, notFoundClasses);
    }

    @Override // cd.AnnotationLoader
    public List<AnnotationDescriptor> a(ProtoContainer protoContainer, jc.n nVar) {
        List<AnnotationDescriptor> j10;
        za.k.e(protoContainer, "container");
        za.k.e(nVar, "proto");
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // cd.AnnotationLoader
    public List<AnnotationDescriptor> c(ProtoContainer protoContainer, jc.n nVar) {
        List<AnnotationDescriptor> j10;
        za.k.e(protoContainer, "container");
        za.k.e(nVar, "proto");
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // cd.AnnotationLoader
    public List<AnnotationDescriptor> d(jc.q qVar, NameResolver nameResolver) {
        int u7;
        za.k.e(qVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        List list = (List) qVar.p(this.f5207a.k());
        if (list == null) {
            list = kotlin.collections.r.j();
        }
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(this.f5208b.a((jc.b) it.next(), nameResolver));
        }
        return arrayList;
    }

    @Override // cd.AnnotationLoader
    public List<AnnotationDescriptor> e(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind) {
        List list;
        int u7;
        za.k.e(protoContainer, "container");
        za.k.e(qVar, "proto");
        za.k.e(annotatedCallableKind, "kind");
        if (qVar instanceof jc.d) {
            list = (List) ((jc.d) qVar).p(this.f5207a.c());
        } else if (qVar instanceof jc.i) {
            list = (List) ((jc.i) qVar).p(this.f5207a.f());
        } else {
            if (!(qVar instanceof jc.n)) {
                throw new IllegalStateException(("Unknown message: " + qVar).toString());
            }
            int i10 = a.f5209a[annotatedCallableKind.ordinal()];
            if (i10 == 1) {
                list = (List) ((jc.n) qVar).p(this.f5207a.h());
            } else if (i10 == 2) {
                list = (List) ((jc.n) qVar).p(this.f5207a.i());
            } else if (i10 == 3) {
                list = (List) ((jc.n) qVar).p(this.f5207a.j());
            } else {
                throw new IllegalStateException("Unsupported callable kind with property proto".toString());
            }
        }
        if (list == null) {
            list = kotlin.collections.r.j();
        }
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(this.f5208b.a((jc.b) it.next(), protoContainer.b()));
        }
        return arrayList;
    }

    @Override // cd.AnnotationLoader
    public List<AnnotationDescriptor> g(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind) {
        List<AnnotationDescriptor> j10;
        za.k.e(protoContainer, "container");
        za.k.e(qVar, "proto");
        za.k.e(annotatedCallableKind, "kind");
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // cd.AnnotationLoader
    public List<AnnotationDescriptor> h(ProtoContainer protoContainer, jc.g gVar) {
        int u7;
        za.k.e(protoContainer, "container");
        za.k.e(gVar, "proto");
        List list = (List) gVar.p(this.f5207a.d());
        if (list == null) {
            list = kotlin.collections.r.j();
        }
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(this.f5208b.a((jc.b) it.next(), protoContainer.b()));
        }
        return arrayList;
    }

    @Override // cd.AnnotationLoader
    public List<AnnotationDescriptor> i(ProtoContainer.a aVar) {
        int u7;
        za.k.e(aVar, "container");
        List list = (List) aVar.f().p(this.f5207a.a());
        if (list == null) {
            list = kotlin.collections.r.j();
        }
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(this.f5208b.a((jc.b) it.next(), aVar.b()));
        }
        return arrayList;
    }

    @Override // cd.AnnotationLoader
    public List<AnnotationDescriptor> j(jc.s sVar, NameResolver nameResolver) {
        int u7;
        za.k.e(sVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        List list = (List) sVar.p(this.f5207a.l());
        if (list == null) {
            list = kotlin.collections.r.j();
        }
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(this.f5208b.a((jc.b) it.next(), nameResolver));
        }
        return arrayList;
    }

    @Override // cd.AnnotationLoader
    public List<AnnotationDescriptor> k(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind, int i10, jc.u uVar) {
        int u7;
        za.k.e(protoContainer, "container");
        za.k.e(qVar, "callableProto");
        za.k.e(annotatedCallableKind, "kind");
        za.k.e(uVar, "proto");
        List list = (List) uVar.p(this.f5207a.g());
        if (list == null) {
            list = kotlin.collections.r.j();
        }
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(this.f5208b.a((jc.b) it.next(), protoContainer.b()));
        }
        return arrayList;
    }

    @Override // cd.AnnotationAndConstantLoader
    /* renamed from: l, reason: merged with bridge method [inline-methods] */
    public uc.g<?> b(ProtoContainer protoContainer, jc.n nVar, g0 g0Var) {
        za.k.e(protoContainer, "container");
        za.k.e(nVar, "proto");
        za.k.e(g0Var, "expectedType");
        return null;
    }

    @Override // cd.AnnotationAndConstantLoader
    /* renamed from: m, reason: merged with bridge method [inline-methods] */
    public uc.g<?> f(ProtoContainer protoContainer, jc.n nVar, g0 g0Var) {
        za.k.e(protoContainer, "container");
        za.k.e(nVar, "proto");
        za.k.e(g0Var, "expectedType");
        b.C0061b.c cVar = (b.C0061b.c) ProtoBufUtil.a(nVar, this.f5207a.b());
        if (cVar == null) {
            return null;
        }
        return this.f5208b.f(g0Var, cVar, protoContainer.b());
    }
}
