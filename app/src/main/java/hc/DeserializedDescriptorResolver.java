package hc;

import cd.ClassData;
import cd.IncompatibleVersionErrorData;
import ed.DeserializedPackageMemberScope;
import ic.KotlinClassHeader;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.collections.SetsJVM;
import kotlin.collections.s0;
import nc.JvmMetadataVersion;
import nc.JvmProtoBufUtil;
import oc.Name;
import pb.ClassDescriptor;
import pb.PackageFragmentDescriptor;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: DeserializedDescriptorResolver.kt */
/* renamed from: hc.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class DeserializedDescriptorResolver {

    /* renamed from: b, reason: collision with root package name */
    public static final a f12166b = new a(null);

    /* renamed from: c, reason: collision with root package name */
    private static final Set<KotlinClassHeader.a> f12167c;

    /* renamed from: d, reason: collision with root package name */
    private static final Set<KotlinClassHeader.a> f12168d;

    /* renamed from: e, reason: collision with root package name */
    private static final JvmMetadataVersion f12169e;

    /* renamed from: f, reason: collision with root package name */
    private static final JvmMetadataVersion f12170f;

    /* renamed from: g, reason: collision with root package name */
    private static final JvmMetadataVersion f12171g;

    /* renamed from: a, reason: collision with root package name */
    public cd.k f12172a;

    /* compiled from: DeserializedDescriptorResolver.kt */
    /* renamed from: hc.h$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final JvmMetadataVersion a() {
            return DeserializedDescriptorResolver.f12171g;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DeserializedDescriptorResolver.kt */
    /* renamed from: hc.h$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.a<Collection<? extends Name>> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f12173e = new b();

        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<Name> invoke() {
            List j10;
            j10 = kotlin.collections.r.j();
            return j10;
        }
    }

    static {
        Set<KotlinClassHeader.a> d10;
        Set<KotlinClassHeader.a> h10;
        d10 = SetsJVM.d(KotlinClassHeader.a.CLASS);
        f12167c = d10;
        h10 = s0.h(KotlinClassHeader.a.FILE_FACADE, KotlinClassHeader.a.MULTIFILE_CLASS_PART);
        f12168d = h10;
        f12169e = new JvmMetadataVersion(1, 1, 2);
        f12170f = new JvmMetadataVersion(1, 1, 11);
        f12171g = new JvmMetadataVersion(1, 1, 13);
    }

    private final ed.e c(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        return d().g().b() ? ed.e.STABLE : kotlinJvmBinaryClass.b().j() ? ed.e.FIR_UNSTABLE : kotlinJvmBinaryClass.b().k() ? ed.e.IR_UNSTABLE : ed.e.STABLE;
    }

    private final IncompatibleVersionErrorData<JvmMetadataVersion> e(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        if (f() || kotlinJvmBinaryClass.b().d().h()) {
            return null;
        }
        return new IncompatibleVersionErrorData<>(kotlinJvmBinaryClass.b().d(), JvmMetadataVersion.f15994i, kotlinJvmBinaryClass.a(), kotlinJvmBinaryClass.e());
    }

    private final boolean f() {
        return d().g().d();
    }

    private final boolean g(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        return !d().g().c() && kotlinJvmBinaryClass.b().i() && za.k.a(kotlinJvmBinaryClass.b().d(), f12170f);
    }

    private final boolean h(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        return (d().g().e() && (kotlinJvmBinaryClass.b().i() || za.k.a(kotlinJvmBinaryClass.b().d(), f12169e))) || g(kotlinJvmBinaryClass);
    }

    private final String[] j(KotlinJvmBinaryClass kotlinJvmBinaryClass, Set<? extends KotlinClassHeader.a> set) {
        KotlinClassHeader b10 = kotlinJvmBinaryClass.b();
        String[] a10 = b10.a();
        if (a10 == null) {
            a10 = b10.b();
        }
        if (a10 == null || !set.contains(b10.c())) {
            return null;
        }
        return a10;
    }

    public final zc.h b(PackageFragmentDescriptor packageFragmentDescriptor, KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        ma.o<nc.f, jc.l> oVar;
        za.k.e(packageFragmentDescriptor, "descriptor");
        za.k.e(kotlinJvmBinaryClass, "kotlinClass");
        String[] j10 = j(kotlinJvmBinaryClass, f12168d);
        if (j10 == null) {
            return null;
        }
        String[] g6 = kotlinJvmBinaryClass.b().g();
        try {
        } catch (Throwable th) {
            if (f() || kotlinJvmBinaryClass.b().d().h()) {
                throw th;
            }
            oVar = null;
        }
        if (g6 == null) {
            return null;
        }
        try {
            oVar = JvmProtoBufUtil.m(j10, g6);
            if (oVar == null) {
                return null;
            }
            nc.f a10 = oVar.a();
            jc.l b10 = oVar.b();
            JvmPackagePartSource jvmPackagePartSource = new JvmPackagePartSource(kotlinJvmBinaryClass, b10, a10, e(kotlinJvmBinaryClass), h(kotlinJvmBinaryClass), c(kotlinJvmBinaryClass));
            return new DeserializedPackageMemberScope(packageFragmentDescriptor, b10, a10, kotlinJvmBinaryClass.b().d(), jvmPackagePartSource, d(), "scope for " + jvmPackagePartSource + " in " + packageFragmentDescriptor, b.f12173e);
        } catch (qc.k e10) {
            throw new IllegalStateException("Could not read data from " + kotlinJvmBinaryClass.a(), e10);
        }
    }

    public final cd.k d() {
        cd.k kVar = this.f12172a;
        if (kVar != null) {
            return kVar;
        }
        za.k.s("components");
        return null;
    }

    public final ClassData i(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        String[] g6;
        ma.o<nc.f, jc.c> oVar;
        za.k.e(kotlinJvmBinaryClass, "kotlinClass");
        String[] j10 = j(kotlinJvmBinaryClass, f12167c);
        if (j10 == null || (g6 = kotlinJvmBinaryClass.b().g()) == null) {
            return null;
        }
        try {
            try {
                oVar = JvmProtoBufUtil.i(j10, g6);
            } catch (qc.k e10) {
                throw new IllegalStateException("Could not read data from " + kotlinJvmBinaryClass.a(), e10);
            }
        } catch (Throwable th) {
            if (f() || kotlinJvmBinaryClass.b().d().h()) {
                throw th;
            }
            oVar = null;
        }
        if (oVar == null) {
            return null;
        }
        return new ClassData(oVar.a(), oVar.b(), kotlinJvmBinaryClass.b().d(), new KotlinJvmBinarySourceElement(kotlinJvmBinaryClass, e(kotlinJvmBinaryClass), h(kotlinJvmBinaryClass), c(kotlinJvmBinaryClass)));
    }

    public final ClassDescriptor k(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        za.k.e(kotlinJvmBinaryClass, "kotlinClass");
        ClassData i10 = i(kotlinJvmBinaryClass);
        if (i10 == null) {
            return null;
        }
        return d().f().d(kotlinJvmBinaryClass.e(), i10);
    }

    public final void l(cd.k kVar) {
        za.k.e(kVar, "<set-?>");
        this.f12172a = kVar;
    }

    public final void m(f fVar) {
        za.k.e(fVar, "components");
        l(fVar.a());
    }
}
