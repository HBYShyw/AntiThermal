package hc;

import cd.AnnotationDeserializer;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import fd.StorageManager;
import gd.g0;
import hc.KotlinJvmBinaryClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import lc.NameResolver;
import oc.ClassId;
import oc.Name;
import pb.ClassDescriptor;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import pb.SourceElement;
import pb.ValueParameterDescriptor;
import pb.findClassInModule;
import qb.AnnotationDescriptor;
import qb.AnnotationDescriptorImpl;
import qd.collections;
import uc.ClassLiteralValue;
import uc.ConstantValueFactory;
import zb.DescriptorResolverUtils;

/* compiled from: BinaryClassAnnotationAndConstantLoaderImpl.kt */
/* renamed from: hc.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class BinaryClassAnnotationAndConstantLoaderImpl extends AbstractBinaryClassAnnotationAndConstantLoader<AnnotationDescriptor, uc.g<?>> {

    /* renamed from: c, reason: collision with root package name */
    private final ModuleDescriptor f12139c;

    /* renamed from: d, reason: collision with root package name */
    private final NotFoundClasses f12140d;

    /* renamed from: e, reason: collision with root package name */
    private final AnnotationDeserializer f12141e;

    /* compiled from: BinaryClassAnnotationAndConstantLoaderImpl.kt */
    /* renamed from: hc.d$a */
    /* loaded from: classes2.dex */
    private abstract class a implements KotlinJvmBinaryClass.a {

        /* compiled from: BinaryClassAnnotationAndConstantLoaderImpl.kt */
        /* renamed from: hc.d$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0044a implements KotlinJvmBinaryClass.a {

            /* renamed from: a, reason: collision with root package name */
            private final /* synthetic */ KotlinJvmBinaryClass.a f12143a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ KotlinJvmBinaryClass.a f12144b;

            /* renamed from: c, reason: collision with root package name */
            final /* synthetic */ a f12145c;

            /* renamed from: d, reason: collision with root package name */
            final /* synthetic */ Name f12146d;

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ ArrayList<AnnotationDescriptor> f12147e;

            C0044a(KotlinJvmBinaryClass.a aVar, a aVar2, Name name, ArrayList<AnnotationDescriptor> arrayList) {
                this.f12144b = aVar;
                this.f12145c = aVar2;
                this.f12146d = name;
                this.f12147e = arrayList;
                this.f12143a = aVar;
            }

            @Override // hc.KotlinJvmBinaryClass.a
            public void a() {
                Object q02;
                this.f12144b.a();
                a aVar = this.f12145c;
                Name name = this.f12146d;
                q02 = _Collections.q0(this.f12147e);
                aVar.h(name, new uc.a((AnnotationDescriptor) q02));
            }

            @Override // hc.KotlinJvmBinaryClass.a
            public KotlinJvmBinaryClass.a b(Name name, ClassId classId) {
                za.k.e(classId, "classId");
                return this.f12143a.b(name, classId);
            }

            @Override // hc.KotlinJvmBinaryClass.a
            public void c(Name name, Object obj) {
                this.f12143a.c(name, obj);
            }

            @Override // hc.KotlinJvmBinaryClass.a
            public KotlinJvmBinaryClass.b d(Name name) {
                return this.f12143a.d(name);
            }

            @Override // hc.KotlinJvmBinaryClass.a
            public void e(Name name, ClassId classId, Name name2) {
                za.k.e(classId, "enumClassId");
                za.k.e(name2, "enumEntryName");
                this.f12143a.e(name, classId, name2);
            }

            @Override // hc.KotlinJvmBinaryClass.a
            public void f(Name name, ClassLiteralValue classLiteralValue) {
                za.k.e(classLiteralValue, ThermalBaseConfig.Item.ATTR_VALUE);
                this.f12143a.f(name, classLiteralValue);
            }
        }

        /* compiled from: BinaryClassAnnotationAndConstantLoaderImpl.kt */
        /* renamed from: hc.d$a$b */
        /* loaded from: classes2.dex */
        public static final class b implements KotlinJvmBinaryClass.b {

            /* renamed from: a, reason: collision with root package name */
            private final ArrayList<uc.g<?>> f12148a = new ArrayList<>();

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ BinaryClassAnnotationAndConstantLoaderImpl f12149b;

            /* renamed from: c, reason: collision with root package name */
            final /* synthetic */ Name f12150c;

            /* renamed from: d, reason: collision with root package name */
            final /* synthetic */ a f12151d;

            /* compiled from: BinaryClassAnnotationAndConstantLoaderImpl.kt */
            /* renamed from: hc.d$a$b$a, reason: collision with other inner class name */
            /* loaded from: classes2.dex */
            public static final class C0045a implements KotlinJvmBinaryClass.a {

                /* renamed from: a, reason: collision with root package name */
                private final /* synthetic */ KotlinJvmBinaryClass.a f12152a;

                /* renamed from: b, reason: collision with root package name */
                final /* synthetic */ KotlinJvmBinaryClass.a f12153b;

                /* renamed from: c, reason: collision with root package name */
                final /* synthetic */ b f12154c;

                /* renamed from: d, reason: collision with root package name */
                final /* synthetic */ ArrayList<AnnotationDescriptor> f12155d;

                C0045a(KotlinJvmBinaryClass.a aVar, b bVar, ArrayList<AnnotationDescriptor> arrayList) {
                    this.f12153b = aVar;
                    this.f12154c = bVar;
                    this.f12155d = arrayList;
                    this.f12152a = aVar;
                }

                @Override // hc.KotlinJvmBinaryClass.a
                public void a() {
                    Object q02;
                    this.f12153b.a();
                    ArrayList arrayList = this.f12154c.f12148a;
                    q02 = _Collections.q0(this.f12155d);
                    arrayList.add(new uc.a((AnnotationDescriptor) q02));
                }

                @Override // hc.KotlinJvmBinaryClass.a
                public KotlinJvmBinaryClass.a b(Name name, ClassId classId) {
                    za.k.e(classId, "classId");
                    return this.f12152a.b(name, classId);
                }

                @Override // hc.KotlinJvmBinaryClass.a
                public void c(Name name, Object obj) {
                    this.f12152a.c(name, obj);
                }

                @Override // hc.KotlinJvmBinaryClass.a
                public KotlinJvmBinaryClass.b d(Name name) {
                    return this.f12152a.d(name);
                }

                @Override // hc.KotlinJvmBinaryClass.a
                public void e(Name name, ClassId classId, Name name2) {
                    za.k.e(classId, "enumClassId");
                    za.k.e(name2, "enumEntryName");
                    this.f12152a.e(name, classId, name2);
                }

                @Override // hc.KotlinJvmBinaryClass.a
                public void f(Name name, ClassLiteralValue classLiteralValue) {
                    za.k.e(classLiteralValue, ThermalBaseConfig.Item.ATTR_VALUE);
                    this.f12152a.f(name, classLiteralValue);
                }
            }

            b(BinaryClassAnnotationAndConstantLoaderImpl binaryClassAnnotationAndConstantLoaderImpl, Name name, a aVar) {
                this.f12149b = binaryClassAnnotationAndConstantLoaderImpl;
                this.f12150c = name;
                this.f12151d = aVar;
            }

            @Override // hc.KotlinJvmBinaryClass.b
            public void a() {
                this.f12151d.g(this.f12150c, this.f12148a);
            }

            @Override // hc.KotlinJvmBinaryClass.b
            public void b(Object obj) {
                this.f12148a.add(this.f12149b.I(this.f12150c, obj));
            }

            @Override // hc.KotlinJvmBinaryClass.b
            public KotlinJvmBinaryClass.a c(ClassId classId) {
                za.k.e(classId, "classId");
                ArrayList arrayList = new ArrayList();
                BinaryClassAnnotationAndConstantLoaderImpl binaryClassAnnotationAndConstantLoaderImpl = this.f12149b;
                SourceElement sourceElement = SourceElement.f16664a;
                za.k.d(sourceElement, "NO_SOURCE");
                KotlinJvmBinaryClass.a v7 = binaryClassAnnotationAndConstantLoaderImpl.v(classId, sourceElement, arrayList);
                za.k.b(v7);
                return new C0045a(v7, this, arrayList);
            }

            @Override // hc.KotlinJvmBinaryClass.b
            public void d(ClassId classId, Name name) {
                za.k.e(classId, "enumClassId");
                za.k.e(name, "enumEntryName");
                this.f12148a.add(new uc.j(classId, name));
            }

            @Override // hc.KotlinJvmBinaryClass.b
            public void e(ClassLiteralValue classLiteralValue) {
                za.k.e(classLiteralValue, ThermalBaseConfig.Item.ATTR_VALUE);
                this.f12148a.add(new uc.q(classLiteralValue));
            }
        }

        public a() {
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public KotlinJvmBinaryClass.a b(Name name, ClassId classId) {
            za.k.e(classId, "classId");
            ArrayList arrayList = new ArrayList();
            BinaryClassAnnotationAndConstantLoaderImpl binaryClassAnnotationAndConstantLoaderImpl = BinaryClassAnnotationAndConstantLoaderImpl.this;
            SourceElement sourceElement = SourceElement.f16664a;
            za.k.d(sourceElement, "NO_SOURCE");
            KotlinJvmBinaryClass.a v7 = binaryClassAnnotationAndConstantLoaderImpl.v(classId, sourceElement, arrayList);
            za.k.b(v7);
            return new C0044a(v7, this, name, arrayList);
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void c(Name name, Object obj) {
            h(name, BinaryClassAnnotationAndConstantLoaderImpl.this.I(name, obj));
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public KotlinJvmBinaryClass.b d(Name name) {
            return new b(BinaryClassAnnotationAndConstantLoaderImpl.this, name, this);
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void e(Name name, ClassId classId, Name name2) {
            za.k.e(classId, "enumClassId");
            za.k.e(name2, "enumEntryName");
            h(name, new uc.j(classId, name2));
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void f(Name name, ClassLiteralValue classLiteralValue) {
            za.k.e(classLiteralValue, ThermalBaseConfig.Item.ATTR_VALUE);
            h(name, new uc.q(classLiteralValue));
        }

        public abstract void g(Name name, ArrayList<uc.g<?>> arrayList);

        public abstract void h(Name name, uc.g<?> gVar);
    }

    /* compiled from: BinaryClassAnnotationAndConstantLoaderImpl.kt */
    /* renamed from: hc.d$b */
    /* loaded from: classes2.dex */
    public static final class b extends a {

        /* renamed from: b, reason: collision with root package name */
        private final HashMap<Name, uc.g<?>> f12156b;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ ClassDescriptor f12158d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ClassId f12159e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ List<AnnotationDescriptor> f12160f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ SourceElement f12161g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(ClassDescriptor classDescriptor, ClassId classId, List<AnnotationDescriptor> list, SourceElement sourceElement) {
            super();
            this.f12158d = classDescriptor;
            this.f12159e = classId;
            this.f12160f = list;
            this.f12161g = sourceElement;
            this.f12156b = new HashMap<>();
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void a() {
            if (BinaryClassAnnotationAndConstantLoaderImpl.this.C(this.f12159e, this.f12156b) || BinaryClassAnnotationAndConstantLoaderImpl.this.u(this.f12159e)) {
                return;
            }
            this.f12160f.add(new AnnotationDescriptorImpl(this.f12158d.x(), this.f12156b, this.f12161g));
        }

        @Override // hc.BinaryClassAnnotationAndConstantLoaderImpl.a
        public void g(Name name, ArrayList<uc.g<?>> arrayList) {
            za.k.e(arrayList, "elements");
            if (name == null) {
                return;
            }
            ValueParameterDescriptor b10 = DescriptorResolverUtils.b(name, this.f12158d);
            if (b10 != null) {
                HashMap<Name, uc.g<?>> hashMap = this.f12156b;
                ConstantValueFactory constantValueFactory = ConstantValueFactory.f18991a;
                List<? extends uc.g<?>> c10 = collections.c(arrayList);
                g0 type = b10.getType();
                za.k.d(type, "parameter.type");
                hashMap.put(name, constantValueFactory.a(c10, type));
                return;
            }
            if (BinaryClassAnnotationAndConstantLoaderImpl.this.u(this.f12159e) && za.k.a(name.b(), ThermalBaseConfig.Item.ATTR_VALUE)) {
                ArrayList arrayList2 = new ArrayList();
                for (Object obj : arrayList) {
                    if (obj instanceof uc.a) {
                        arrayList2.add(obj);
                    }
                }
                List<AnnotationDescriptor> list = this.f12160f;
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    list.add(((uc.a) it.next()).b());
                }
            }
        }

        @Override // hc.BinaryClassAnnotationAndConstantLoaderImpl.a
        public void h(Name name, uc.g<?> gVar) {
            za.k.e(gVar, ThermalBaseConfig.Item.ATTR_VALUE);
            if (name != null) {
                this.f12156b.put(name, gVar);
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BinaryClassAnnotationAndConstantLoaderImpl(ModuleDescriptor moduleDescriptor, NotFoundClasses notFoundClasses, StorageManager storageManager, p pVar) {
        super(storageManager, pVar);
        za.k.e(moduleDescriptor, "module");
        za.k.e(notFoundClasses, "notFoundClasses");
        za.k.e(storageManager, "storageManager");
        za.k.e(pVar, "kotlinClassFinder");
        this.f12139c = moduleDescriptor;
        this.f12140d = notFoundClasses;
        this.f12141e = new AnnotationDeserializer(moduleDescriptor, notFoundClasses);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final uc.g<?> I(Name name, Object obj) {
        uc.g<?> c10 = ConstantValueFactory.f18991a.c(obj);
        if (c10 != null) {
            return c10;
        }
        return uc.k.f18996b.a("Unsupported annotation argument: " + name);
    }

    private final ClassDescriptor L(ClassId classId) {
        return findClassInModule.c(this.f12139c, classId, this.f12140d);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // hc.AbstractBinaryClassAnnotationAndConstantLoader
    /* renamed from: J, reason: merged with bridge method [inline-methods] */
    public uc.g<?> E(String str, Object obj) {
        boolean I;
        za.k.e(str, "desc");
        za.k.e(obj, "initializer");
        I = sd.v.I("ZBCS", str, false, 2, null);
        if (I) {
            int intValue = ((Integer) obj).intValue();
            int hashCode = str.hashCode();
            if (hashCode == 66) {
                if (str.equals("B")) {
                    obj = Byte.valueOf((byte) intValue);
                }
                throw new AssertionError(str);
            }
            if (hashCode == 67) {
                if (str.equals("C")) {
                    obj = Character.valueOf((char) intValue);
                }
                throw new AssertionError(str);
            }
            if (hashCode == 83) {
                if (str.equals("S")) {
                    obj = Short.valueOf((short) intValue);
                }
                throw new AssertionError(str);
            }
            if (hashCode == 90 && str.equals("Z")) {
                obj = Boolean.valueOf(intValue != 0);
            }
            throw new AssertionError(str);
        }
        return ConstantValueFactory.f18991a.c(obj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // hc.b
    /* renamed from: K, reason: merged with bridge method [inline-methods] */
    public AnnotationDescriptor y(jc.b bVar, NameResolver nameResolver) {
        za.k.e(bVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        return this.f12141e.a(bVar, nameResolver);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // hc.AbstractBinaryClassAnnotationAndConstantLoader
    /* renamed from: M, reason: merged with bridge method [inline-methods] */
    public uc.g<?> G(uc.g<?> gVar) {
        uc.g<?> yVar;
        za.k.e(gVar, "constant");
        if (gVar instanceof uc.d) {
            yVar = new uc.w(((uc.d) gVar).b().byteValue());
        } else if (gVar instanceof uc.u) {
            yVar = new uc.z(((uc.u) gVar).b().shortValue());
        } else if (gVar instanceof uc.m) {
            yVar = new uc.x(((uc.m) gVar).b().intValue());
        } else {
            if (!(gVar instanceof uc.r)) {
                return gVar;
            }
            yVar = new uc.y(((uc.r) gVar).b().longValue());
        }
        return yVar;
    }

    @Override // hc.b
    protected KotlinJvmBinaryClass.a v(ClassId classId, SourceElement sourceElement, List<AnnotationDescriptor> list) {
        za.k.e(classId, "annotationClassId");
        za.k.e(sourceElement, "source");
        za.k.e(list, "result");
        return new b(L(classId), classId, list, sourceElement);
    }
}
