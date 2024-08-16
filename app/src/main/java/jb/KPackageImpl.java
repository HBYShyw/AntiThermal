package jb;

import cd.MemberDeserializer;
import gb.KDeclarationContainer;
import ic.KotlinClassHeader;
import java.util.Collection;
import java.util.List;
import jb.KDeclarationContainerImpl;
import jb.ReflectProperties;
import lc.ProtoBufUtil;
import lc.TypeTable;
import mc.JvmProtoBuf;
import nc.JvmMetadataVersion;
import nc.JvmProtoBufUtil;
import oc.Name;
import pb.ConstructorDescriptor;
import pb.FunctionDescriptor;
import pb.PropertyDescriptor;
import qc.i;
import sd.StringsJVM;
import vb.reflectClassUtil;
import za.FunctionReference;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import zc.h;

/* compiled from: KPackageImpl.kt */
/* renamed from: jb.u, reason: use source file name */
/* loaded from: classes2.dex */
public final class KPackageImpl extends KDeclarationContainerImpl {

    /* renamed from: h, reason: collision with root package name */
    private final Class<?> f13330h;

    /* renamed from: i, reason: collision with root package name */
    private final ReflectProperties.b<a> f13331i;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: KPackageImpl.kt */
    /* renamed from: jb.u$a */
    /* loaded from: classes2.dex */
    public final class a extends KDeclarationContainerImpl.b {

        /* renamed from: j, reason: collision with root package name */
        static final /* synthetic */ gb.l<Object>[] f13332j = {Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "kotlinClass", "getKotlinClass()Lorg/jetbrains/kotlin/descriptors/runtime/components/ReflectKotlinClass;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "scope", "getScope()Lorg/jetbrains/kotlin/resolve/scopes/MemberScope;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "multifileFacade", "getMultifileFacade()Ljava/lang/Class;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "metadata", "getMetadata()Lkotlin/Triple;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "members", "getMembers()Ljava/util/Collection;"))};

        /* renamed from: d, reason: collision with root package name */
        private final ReflectProperties.a f13333d;

        /* renamed from: e, reason: collision with root package name */
        private final ReflectProperties.a f13334e;

        /* renamed from: f, reason: collision with root package name */
        private final ReflectProperties.b f13335f;

        /* renamed from: g, reason: collision with root package name */
        private final ReflectProperties.b f13336g;

        /* renamed from: h, reason: collision with root package name */
        private final ReflectProperties.a f13337h;

        /* compiled from: KPackageImpl.kt */
        /* renamed from: jb.u$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0060a extends Lambda implements ya.a<ub.f> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KPackageImpl f13339e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0060a(KPackageImpl kPackageImpl) {
                super(0);
                this.f13339e = kPackageImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final ub.f invoke() {
                return ub.f.f18974c.a(this.f13339e.e());
            }
        }

        /* compiled from: KPackageImpl.kt */
        /* renamed from: jb.u$a$b */
        /* loaded from: classes2.dex */
        static final class b extends Lambda implements ya.a<Collection<? extends KCallableImpl<?>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KPackageImpl f13340e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ a f13341f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            b(KPackageImpl kPackageImpl, a aVar) {
                super(0);
                this.f13340e = kPackageImpl;
                this.f13341f = aVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Collection<KCallableImpl<?>> invoke() {
                return this.f13340e.L(this.f13341f.f(), KDeclarationContainerImpl.c.DECLARED);
            }
        }

        /* compiled from: KPackageImpl.kt */
        /* renamed from: jb.u$a$c */
        /* loaded from: classes2.dex */
        static final class c extends Lambda implements ya.a<ma.t<? extends nc.f, ? extends jc.l, ? extends JvmMetadataVersion>> {
            c() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final ma.t<nc.f, jc.l, JvmMetadataVersion> invoke() {
                KotlinClassHeader b10;
                ub.f c10 = a.this.c();
                if (c10 == null || (b10 = c10.b()) == null) {
                    return null;
                }
                String[] a10 = b10.a();
                String[] g6 = b10.g();
                if (a10 == null || g6 == null) {
                    return null;
                }
                ma.o<nc.f, jc.l> m10 = JvmProtoBufUtil.m(a10, g6);
                return new ma.t<>(m10.a(), m10.b(), b10.d());
            }
        }

        /* compiled from: KPackageImpl.kt */
        /* renamed from: jb.u$a$d */
        /* loaded from: classes2.dex */
        static final class d extends Lambda implements ya.a<Class<?>> {

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ KPackageImpl f13344f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            d(KPackageImpl kPackageImpl) {
                super(0);
                this.f13344f = kPackageImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Class<?> invoke() {
                String y4;
                KotlinClassHeader b10;
                ub.f c10 = a.this.c();
                String e10 = (c10 == null || (b10 = c10.b()) == null) ? null : b10.e();
                if (e10 == null) {
                    return null;
                }
                if (!(e10.length() > 0)) {
                    return null;
                }
                ClassLoader classLoader = this.f13344f.e().getClassLoader();
                y4 = StringsJVM.y(e10, '/', '.', false, 4, null);
                return classLoader.loadClass(y4);
            }
        }

        /* compiled from: KPackageImpl.kt */
        /* renamed from: jb.u$a$e */
        /* loaded from: classes2.dex */
        static final class e extends Lambda implements ya.a<zc.h> {
            e() {
                super(0);
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final zc.h invoke() {
                ub.f c10 = a.this.c();
                if (c10 != null) {
                    return a.this.a().c().a(c10);
                }
                return h.b.f20465b;
            }
        }

        public a() {
            super();
            this.f13333d = ReflectProperties.d(new C0060a(KPackageImpl.this));
            this.f13334e = ReflectProperties.d(new e());
            this.f13335f = ReflectProperties.b(new d(KPackageImpl.this));
            this.f13336g = ReflectProperties.b(new c());
            this.f13337h = ReflectProperties.d(new b(KPackageImpl.this, this));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public final ub.f c() {
            return (ub.f) this.f13333d.b(this, f13332j[0]);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final ma.t<nc.f, jc.l, JvmMetadataVersion> d() {
            return (ma.t) this.f13336g.b(this, f13332j[3]);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final Class<?> e() {
            return (Class) this.f13335f.b(this, f13332j[2]);
        }

        public final zc.h f() {
            T b10 = this.f13334e.b(this, f13332j[1]);
            za.k.d(b10, "<get-scope>(...)");
            return (zc.h) b10;
        }
    }

    /* compiled from: KPackageImpl.kt */
    /* renamed from: jb.u$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<a> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final a invoke() {
            return new a();
        }
    }

    /* compiled from: KPackageImpl.kt */
    /* renamed from: jb.u$c */
    /* loaded from: classes2.dex */
    /* synthetic */ class c extends FunctionReference implements ya.p<MemberDeserializer, jc.n, PropertyDescriptor> {

        /* renamed from: n, reason: collision with root package name */
        public static final c f13347n = new c();

        c() {
            super(2);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(MemberDeserializer.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "loadProperty(Lorg/jetbrains/kotlin/metadata/ProtoBuf$Property;)Lorg/jetbrains/kotlin/descriptors/PropertyDescriptor;";
        }

        @Override // ya.p
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final PropertyDescriptor invoke(MemberDeserializer memberDeserializer, jc.n nVar) {
            za.k.e(memberDeserializer, "p0");
            za.k.e(nVar, "p1");
            return memberDeserializer.l(nVar);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "loadProperty";
        }
    }

    public KPackageImpl(Class<?> cls) {
        za.k.e(cls, "jClass");
        this.f13330h = cls;
        ReflectProperties.b<a> b10 = ReflectProperties.b(new b());
        za.k.d(b10, "lazy { Data() }");
        this.f13331i = b10;
    }

    private final zc.h U() {
        return this.f13331i.invoke().f();
    }

    @Override // jb.KDeclarationContainerImpl
    public Collection<ConstructorDescriptor> I() {
        List j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // jb.KDeclarationContainerImpl
    public Collection<FunctionDescriptor> J(Name name) {
        za.k.e(name, "name");
        return U().c(name, xb.d.FROM_REFLECTION);
    }

    @Override // jb.KDeclarationContainerImpl
    public PropertyDescriptor K(int i10) {
        ma.t<nc.f, jc.l, JvmMetadataVersion> d10 = this.f13331i.invoke().d();
        if (d10 == null) {
            return null;
        }
        nc.f a10 = d10.a();
        jc.l b10 = d10.b();
        JvmMetadataVersion c10 = d10.c();
        i.f<jc.l, List<jc.n>> fVar = JvmProtoBuf.f15377n;
        za.k.d(fVar, "packageLocalVariable");
        jc.n nVar = (jc.n) ProtoBufUtil.b(b10, fVar, i10);
        if (nVar == null) {
            return null;
        }
        Class<?> e10 = e();
        jc.t Q = b10.Q();
        za.k.d(Q, "packageProto.typeTable");
        return (PropertyDescriptor) o0.h(e10, nVar, a10, new TypeTable(Q), c10, c.f13347n);
    }

    @Override // jb.KDeclarationContainerImpl
    protected Class<?> M() {
        Class<?> e10 = this.f13331i.invoke().e();
        return e10 == null ? e() : e10;
    }

    @Override // jb.KDeclarationContainerImpl
    public Collection<PropertyDescriptor> N(Name name) {
        za.k.e(name, "name");
        return U().a(name, xb.d.FROM_REFLECTION);
    }

    @Override // za.ClassBasedDeclarationContainer
    public Class<?> e() {
        return this.f13330h;
    }

    public boolean equals(Object obj) {
        return (obj instanceof KPackageImpl) && za.k.a(e(), ((KPackageImpl) obj).e());
    }

    public int hashCode() {
        return e().hashCode();
    }

    public String toString() {
        return "file class " + reflectClassUtil.a(e()).b();
    }
}
