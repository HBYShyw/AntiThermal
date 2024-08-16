package cc;

import fc.u;
import fd.StorageManager;
import hc.KotlinJvmBinaryClass;
import hc.KotlinJvmBinaryPackageSourceElement;
import hc.PackagePartProvider;
import hc.q;
import ic.KotlinClassHeader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.collections.m0;
import kotlin.collections.r;
import kotlin.collections.s;
import ma.o;
import oc.ClassId;
import oc.FqName;
import pb.ClassDescriptor;
import pb.SourceElement;
import sb.PackageFragmentDescriptorImpl;
import xc.JvmClassName;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: LazyJavaPackageFragment.kt */
/* renamed from: cc.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class LazyJavaPackageFragment extends PackageFragmentDescriptorImpl {

    /* renamed from: r, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f5109r = {Reflection.g(new PropertyReference1Impl(Reflection.b(LazyJavaPackageFragment.class), "binaryClasses", "getBinaryClasses$descriptors_jvm()Ljava/util/Map;")), Reflection.g(new PropertyReference1Impl(Reflection.b(LazyJavaPackageFragment.class), "partToFacade", "getPartToFacade()Ljava/util/HashMap;"))};

    /* renamed from: k, reason: collision with root package name */
    private final u f5110k;

    /* renamed from: l, reason: collision with root package name */
    private final bc.g f5111l;

    /* renamed from: m, reason: collision with root package name */
    private final fd.i f5112m;

    /* renamed from: n, reason: collision with root package name */
    private final JvmPackageScope f5113n;

    /* renamed from: o, reason: collision with root package name */
    private final fd.i<List<FqName>> f5114o;

    /* renamed from: p, reason: collision with root package name */
    private final qb.g f5115p;

    /* renamed from: q, reason: collision with root package name */
    private final fd.i f5116q;

    /* compiled from: LazyJavaPackageFragment.kt */
    /* renamed from: cc.h$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<Map<String, ? extends KotlinJvmBinaryClass>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Map<String, KotlinJvmBinaryClass> invoke() {
            Map<String, KotlinJvmBinaryClass> q10;
            PackagePartProvider o10 = LazyJavaPackageFragment.this.f5111l.a().o();
            String b10 = LazyJavaPackageFragment.this.d().b();
            za.k.d(b10, "fqName.asString()");
            List<String> a10 = o10.a(b10);
            LazyJavaPackageFragment lazyJavaPackageFragment = LazyJavaPackageFragment.this;
            ArrayList arrayList = new ArrayList();
            for (String str : a10) {
                ClassId m10 = ClassId.m(JvmClassName.d(str).e());
                za.k.d(m10, "topLevel(JvmClassName.by…velClassMaybeWithDollars)");
                KotlinJvmBinaryClass b11 = q.b(lazyJavaPackageFragment.f5111l.a().j(), m10);
                o a11 = b11 != null ? ma.u.a(str, b11) : null;
                if (a11 != null) {
                    arrayList.add(a11);
                }
            }
            q10 = m0.q(arrayList);
            return q10;
        }
    }

    /* compiled from: LazyJavaPackageFragment.kt */
    /* renamed from: cc.h$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<HashMap<JvmClassName, JvmClassName>> {

        /* compiled from: LazyJavaPackageFragment.kt */
        /* renamed from: cc.h$b$a */
        /* loaded from: classes2.dex */
        public /* synthetic */ class a {

            /* renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f5119a;

            static {
                int[] iArr = new int[KotlinClassHeader.a.values().length];
                try {
                    iArr[KotlinClassHeader.a.MULTIFILE_CLASS_PART.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[KotlinClassHeader.a.FILE_FACADE.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                f5119a = iArr;
            }
        }

        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final HashMap<JvmClassName, JvmClassName> invoke() {
            HashMap<JvmClassName, JvmClassName> hashMap = new HashMap<>();
            for (Map.Entry<String, KotlinJvmBinaryClass> entry : LazyJavaPackageFragment.this.V0().entrySet()) {
                String key = entry.getKey();
                KotlinJvmBinaryClass value = entry.getValue();
                JvmClassName d10 = JvmClassName.d(key);
                za.k.d(d10, "byInternalName(partInternalName)");
                KotlinClassHeader b10 = value.b();
                int i10 = a.f5119a[b10.c().ordinal()];
                if (i10 == 1) {
                    String e10 = b10.e();
                    if (e10 != null) {
                        JvmClassName d11 = JvmClassName.d(e10);
                        za.k.d(d11, "byInternalName(header.mu…: continue@kotlinClasses)");
                        hashMap.put(d10, d11);
                    }
                } else if (i10 == 2) {
                    hashMap.put(d10, d10);
                }
            }
            return hashMap;
        }
    }

    /* compiled from: LazyJavaPackageFragment.kt */
    /* renamed from: cc.h$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<List<? extends FqName>> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<FqName> invoke() {
            int u7;
            Collection<u> H = LazyJavaPackageFragment.this.f5110k.H();
            u7 = s.u(H, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = H.iterator();
            while (it.hasNext()) {
                arrayList.add(((u) it.next()).d());
            }
            return arrayList;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyJavaPackageFragment(bc.g gVar, u uVar) {
        super(gVar.d(), uVar.d());
        List j10;
        za.k.e(gVar, "outerContext");
        za.k.e(uVar, "jPackage");
        this.f5110k = uVar;
        bc.g d10 = bc.a.d(gVar, this, null, 0, 6, null);
        this.f5111l = d10;
        this.f5112m = d10.e().g(new a());
        this.f5113n = new JvmPackageScope(d10, uVar, this);
        StorageManager e10 = d10.e();
        c cVar = new c();
        j10 = r.j();
        this.f5114o = e10.a(cVar, j10);
        this.f5115p = d10.a().i().b() ? qb.g.f17195b.b() : bc.e.a(d10, uVar);
        this.f5116q = d10.e().g(new b());
    }

    public final ClassDescriptor U0(fc.g gVar) {
        za.k.e(gVar, "jClass");
        return this.f5113n.j().O(gVar);
    }

    public final Map<String, KotlinJvmBinaryClass> V0() {
        return (Map) fd.m.a(this.f5112m, this, f5109r[0]);
    }

    @Override // pb.PackageFragmentDescriptor
    /* renamed from: W0, reason: merged with bridge method [inline-methods] */
    public JvmPackageScope u() {
        return this.f5113n;
    }

    public final List<FqName> X0() {
        return this.f5114o.invoke();
    }

    @Override // qb.AnnotatedImpl, qb.a
    public qb.g i() {
        return this.f5115p;
    }

    @Override // sb.PackageFragmentDescriptorImpl, sb.DeclarationDescriptorImpl
    public String toString() {
        return "Lazy Java package fragment: " + d() + " of module " + this.f5111l.a().m();
    }

    @Override // sb.PackageFragmentDescriptorImpl, sb.DeclarationDescriptorNonRootImpl, pb.DeclarationDescriptorWithSource
    public SourceElement z() {
        return new KotlinJvmBinaryPackageSourceElement(this);
    }
}
