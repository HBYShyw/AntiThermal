package hc;

import cd.AnnotatedCallableKind;
import cd.AnnotationAndConstantLoader;
import cd.ProtoContainer;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import fd.StorageManager;
import gd.g0;
import hc.KotlinJvmBinaryClass;
import hc.MemberSignature;
import hc.b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lb.SpecialJvmAnnotations;
import lc.Flags;
import nc.JvmProtoBufUtil;
import oc.ClassId;
import oc.Name;
import pb.SourceElement;
import uc.q;
import za.Lambda;

/* compiled from: AbstractBinaryClassAnnotationAndConstantLoader.kt */
/* renamed from: hc.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractBinaryClassAnnotationAndConstantLoader<A, C> extends hc.b<A, a<? extends A, ? extends C>> implements AnnotationAndConstantLoader<A, C> {

    /* renamed from: b, reason: collision with root package name */
    private final fd.g<KotlinJvmBinaryClass, a<A, C>> f12093b;

    /* compiled from: AbstractBinaryClassAnnotationAndConstantLoader.kt */
    /* renamed from: hc.a$a */
    /* loaded from: classes2.dex */
    public static final class a<A, C> extends b.a<A> {

        /* renamed from: a, reason: collision with root package name */
        private final Map<MemberSignature, List<A>> f12094a;

        /* renamed from: b, reason: collision with root package name */
        private final Map<MemberSignature, C> f12095b;

        /* renamed from: c, reason: collision with root package name */
        private final Map<MemberSignature, C> f12096c;

        /* JADX WARN: Multi-variable type inference failed */
        public a(Map<MemberSignature, ? extends List<? extends A>> map, Map<MemberSignature, ? extends C> map2, Map<MemberSignature, ? extends C> map3) {
            za.k.e(map, "memberAnnotations");
            za.k.e(map2, "propertyConstants");
            za.k.e(map3, "annotationParametersDefaultValues");
            this.f12094a = map;
            this.f12095b = map2;
            this.f12096c = map3;
        }

        @Override // hc.b.a
        public Map<MemberSignature, List<A>> a() {
            return this.f12094a;
        }

        public final Map<MemberSignature, C> b() {
            return this.f12096c;
        }

        public final Map<MemberSignature, C> c() {
            return this.f12095b;
        }
    }

    /* compiled from: AbstractBinaryClassAnnotationAndConstantLoader.kt */
    /* renamed from: hc.a$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.p<a<? extends A, ? extends C>, MemberSignature, C> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f12097e = new b();

        b() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final C invoke(a<? extends A, ? extends C> aVar, MemberSignature memberSignature) {
            za.k.e(aVar, "$this$loadConstantFromProperty");
            za.k.e(memberSignature, "it");
            return aVar.b().get(memberSignature);
        }
    }

    /* compiled from: AbstractBinaryClassAnnotationAndConstantLoader.kt */
    /* renamed from: hc.a$c */
    /* loaded from: classes2.dex */
    public static final class c implements KotlinJvmBinaryClass.d {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ AbstractBinaryClassAnnotationAndConstantLoader<A, C> f12098a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ HashMap<MemberSignature, List<A>> f12099b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ KotlinJvmBinaryClass f12100c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ HashMap<MemberSignature, C> f12101d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ HashMap<MemberSignature, C> f12102e;

        /* compiled from: AbstractBinaryClassAnnotationAndConstantLoader.kt */
        /* renamed from: hc.a$c$a */
        /* loaded from: classes2.dex */
        public final class a extends b implements KotlinJvmBinaryClass.e {

            /* renamed from: d, reason: collision with root package name */
            final /* synthetic */ c f12103d;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public a(c cVar, MemberSignature memberSignature) {
                super(cVar, memberSignature);
                za.k.e(memberSignature, "signature");
                this.f12103d = cVar;
            }

            @Override // hc.KotlinJvmBinaryClass.e
            public KotlinJvmBinaryClass.a c(int i10, ClassId classId, SourceElement sourceElement) {
                za.k.e(classId, "classId");
                za.k.e(sourceElement, "source");
                MemberSignature e10 = MemberSignature.f12206b.e(d(), i10);
                List<A> list = this.f12103d.f12099b.get(e10);
                if (list == null) {
                    list = new ArrayList<>();
                    this.f12103d.f12099b.put(e10, list);
                }
                return this.f12103d.f12098a.w(classId, sourceElement, list);
            }
        }

        /* compiled from: AbstractBinaryClassAnnotationAndConstantLoader.kt */
        /* renamed from: hc.a$c$b */
        /* loaded from: classes2.dex */
        public class b implements KotlinJvmBinaryClass.c {

            /* renamed from: a, reason: collision with root package name */
            private final MemberSignature f12104a;

            /* renamed from: b, reason: collision with root package name */
            private final ArrayList<A> f12105b;

            /* renamed from: c, reason: collision with root package name */
            final /* synthetic */ c f12106c;

            public b(c cVar, MemberSignature memberSignature) {
                za.k.e(memberSignature, "signature");
                this.f12106c = cVar;
                this.f12104a = memberSignature;
                this.f12105b = new ArrayList<>();
            }

            @Override // hc.KotlinJvmBinaryClass.c
            public void a() {
                if (!this.f12105b.isEmpty()) {
                    this.f12106c.f12099b.put(this.f12104a, this.f12105b);
                }
            }

            @Override // hc.KotlinJvmBinaryClass.c
            public KotlinJvmBinaryClass.a b(ClassId classId, SourceElement sourceElement) {
                za.k.e(classId, "classId");
                za.k.e(sourceElement, "source");
                return this.f12106c.f12098a.w(classId, sourceElement, this.f12105b);
            }

            protected final MemberSignature d() {
                return this.f12104a;
            }
        }

        c(AbstractBinaryClassAnnotationAndConstantLoader<A, C> abstractBinaryClassAnnotationAndConstantLoader, HashMap<MemberSignature, List<A>> hashMap, KotlinJvmBinaryClass kotlinJvmBinaryClass, HashMap<MemberSignature, C> hashMap2, HashMap<MemberSignature, C> hashMap3) {
            this.f12098a = abstractBinaryClassAnnotationAndConstantLoader;
            this.f12099b = hashMap;
            this.f12100c = kotlinJvmBinaryClass;
            this.f12101d = hashMap2;
            this.f12102e = hashMap3;
        }

        @Override // hc.KotlinJvmBinaryClass.d
        public KotlinJvmBinaryClass.e a(Name name, String str) {
            za.k.e(name, "name");
            za.k.e(str, "desc");
            MemberSignature.a aVar = MemberSignature.f12206b;
            String b10 = name.b();
            za.k.d(b10, "name.asString()");
            return new a(this, aVar.d(b10, str));
        }

        @Override // hc.KotlinJvmBinaryClass.d
        public KotlinJvmBinaryClass.c b(Name name, String str, Object obj) {
            C E;
            za.k.e(name, "name");
            za.k.e(str, "desc");
            MemberSignature.a aVar = MemberSignature.f12206b;
            String b10 = name.b();
            za.k.d(b10, "name.asString()");
            MemberSignature a10 = aVar.a(b10, str);
            if (obj != null && (E = this.f12098a.E(str, obj)) != null) {
                this.f12102e.put(a10, E);
            }
            return new b(this, a10);
        }
    }

    /* compiled from: AbstractBinaryClassAnnotationAndConstantLoader.kt */
    /* renamed from: hc.a$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.p<a<? extends A, ? extends C>, MemberSignature, C> {

        /* renamed from: e, reason: collision with root package name */
        public static final d f12107e = new d();

        d() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final C invoke(a<? extends A, ? extends C> aVar, MemberSignature memberSignature) {
            za.k.e(aVar, "$this$loadConstantFromProperty");
            za.k.e(memberSignature, "it");
            return aVar.c().get(memberSignature);
        }
    }

    /* compiled from: AbstractBinaryClassAnnotationAndConstantLoader.kt */
    /* renamed from: hc.a$e */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.l<KotlinJvmBinaryClass, a<? extends A, ? extends C>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ AbstractBinaryClassAnnotationAndConstantLoader<A, C> f12108e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(AbstractBinaryClassAnnotationAndConstantLoader<A, C> abstractBinaryClassAnnotationAndConstantLoader) {
            super(1);
            this.f12108e = abstractBinaryClassAnnotationAndConstantLoader;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final a<A, C> invoke(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
            za.k.e(kotlinJvmBinaryClass, "kotlinClass");
            return this.f12108e.D(kotlinJvmBinaryClass);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AbstractBinaryClassAnnotationAndConstantLoader(StorageManager storageManager, p pVar) {
        super(pVar);
        za.k.e(storageManager, "storageManager");
        za.k.e(pVar, "kotlinClassFinder");
        this.f12093b = storageManager.d(new e(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final a<A, C> D(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        kotlinJvmBinaryClass.c(new c(this, hashMap, kotlinJvmBinaryClass, hashMap3, hashMap2), q(kotlinJvmBinaryClass));
        return new a<>(hashMap, hashMap2, hashMap3);
    }

    private final C F(ProtoContainer protoContainer, jc.n nVar, AnnotatedCallableKind annotatedCallableKind, g0 g0Var, ya.p<? super a<? extends A, ? extends C>, ? super MemberSignature, ? extends C> pVar) {
        C invoke;
        KotlinJvmBinaryClass o10 = o(protoContainer, t(protoContainer, true, true, Flags.A.d(nVar.V()), JvmProtoBufUtil.f(nVar)));
        if (o10 == null) {
            return null;
        }
        MemberSignature r10 = r(nVar, protoContainer.b(), protoContainer.d(), annotatedCallableKind, o10.b().d().d(DeserializedDescriptorResolver.f12166b.a()));
        if (r10 == null || (invoke = pVar.invoke(this.f12093b.invoke(o10), r10)) == null) {
            return null;
        }
        return mb.o.d(g0Var) ? G(invoke) : invoke;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // hc.b
    /* renamed from: B, reason: merged with bridge method [inline-methods] */
    public a<A, C> p(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        za.k.e(kotlinJvmBinaryClass, "binaryClass");
        return this.f12093b.invoke(kotlinJvmBinaryClass);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean C(ClassId classId, Map<Name, ? extends uc.g<?>> map) {
        za.k.e(classId, "annotationClassId");
        za.k.e(map, "arguments");
        if (!za.k.a(classId, SpecialJvmAnnotations.f14656a.a())) {
            return false;
        }
        uc.g<?> gVar = map.get(Name.f(ThermalBaseConfig.Item.ATTR_VALUE));
        uc.q qVar = gVar instanceof uc.q ? (uc.q) gVar : null;
        if (qVar == null) {
            return false;
        }
        q.b b10 = qVar.b();
        q.b.C0110b c0110b = b10 instanceof q.b.C0110b ? (q.b.C0110b) b10 : null;
        if (c0110b == null) {
            return false;
        }
        return u(c0110b.b());
    }

    protected abstract C E(String str, Object obj);

    protected abstract C G(C c10);

    @Override // cd.AnnotationAndConstantLoader
    public C b(ProtoContainer protoContainer, jc.n nVar, g0 g0Var) {
        za.k.e(protoContainer, "container");
        za.k.e(nVar, "proto");
        za.k.e(g0Var, "expectedType");
        return F(protoContainer, nVar, AnnotatedCallableKind.PROPERTY_GETTER, g0Var, b.f12097e);
    }

    @Override // cd.AnnotationAndConstantLoader
    public C f(ProtoContainer protoContainer, jc.n nVar, g0 g0Var) {
        za.k.e(protoContainer, "container");
        za.k.e(nVar, "proto");
        za.k.e(g0Var, "expectedType");
        return F(protoContainer, nVar, AnnotatedCallableKind.PROPERTY, g0Var, d.f12107e);
    }
}
