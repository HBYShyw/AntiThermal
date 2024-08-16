package hc;

import cd.IncompatibleVersionErrorData;
import lc.NameResolver;
import lc.ProtoBufUtil;
import mc.JvmProtoBuf;
import nc.JvmMetadataVersion;
import oc.ClassId;
import oc.Name;
import pb.b1;
import qc.i;
import xc.JvmClassName;

/* compiled from: JvmPackagePartSource.kt */
/* renamed from: hc.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmPackagePartSource implements ed.f {

    /* renamed from: b, reason: collision with root package name */
    private final JvmClassName f12177b;

    /* renamed from: c, reason: collision with root package name */
    private final JvmClassName f12178c;

    /* renamed from: d, reason: collision with root package name */
    private final IncompatibleVersionErrorData<JvmMetadataVersion> f12179d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f12180e;

    /* renamed from: f, reason: collision with root package name */
    private final ed.e f12181f;

    /* renamed from: g, reason: collision with root package name */
    private final KotlinJvmBinaryClass f12182g;

    /* renamed from: h, reason: collision with root package name */
    private final String f12183h;

    public JvmPackagePartSource(JvmClassName jvmClassName, JvmClassName jvmClassName2, jc.l lVar, NameResolver nameResolver, IncompatibleVersionErrorData<JvmMetadataVersion> incompatibleVersionErrorData, boolean z10, ed.e eVar, KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        String string;
        za.k.e(jvmClassName, "className");
        za.k.e(lVar, "packageProto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(eVar, "abiStability");
        this.f12177b = jvmClassName;
        this.f12178c = jvmClassName2;
        this.f12179d = incompatibleVersionErrorData;
        this.f12180e = z10;
        this.f12181f = eVar;
        this.f12182g = kotlinJvmBinaryClass;
        i.f<jc.l, Integer> fVar = JvmProtoBuf.f15376m;
        za.k.d(fVar, "packageModuleName");
        Integer num = (Integer) ProtoBufUtil.a(lVar, fVar);
        this.f12183h = (num == null || (string = nameResolver.getString(num.intValue())) == null) ? "main" : string;
    }

    @Override // pb.SourceElement
    public b1 a() {
        b1 b1Var = b1.f16671a;
        za.k.d(b1Var, "NO_SOURCE_FILE");
        return b1Var;
    }

    @Override // ed.f
    public String c() {
        return "Class '" + d().b().b() + '\'';
    }

    public final ClassId d() {
        return new ClassId(e().g(), h());
    }

    public JvmClassName e() {
        return this.f12177b;
    }

    public JvmClassName f() {
        return this.f12178c;
    }

    public final KotlinJvmBinaryClass g() {
        return this.f12182g;
    }

    public final Name h() {
        String D0;
        String f10 = e().f();
        za.k.d(f10, "className.internalName");
        D0 = sd.v.D0(f10, '/', null, 2, null);
        Name f11 = Name.f(D0);
        za.k.d(f11, "identifier(className.int….substringAfterLast('/'))");
        return f11;
    }

    public String toString() {
        return JvmPackagePartSource.class.getSimpleName() + ": " + e();
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public JvmPackagePartSource(KotlinJvmBinaryClass kotlinJvmBinaryClass, jc.l lVar, NameResolver nameResolver, IncompatibleVersionErrorData<JvmMetadataVersion> incompatibleVersionErrorData, boolean z10, ed.e eVar) {
        this(r2, r1, lVar, nameResolver, incompatibleVersionErrorData, z10, eVar, kotlinJvmBinaryClass);
        za.k.e(kotlinJvmBinaryClass, "kotlinClass");
        za.k.e(lVar, "packageProto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(eVar, "abiStability");
        JvmClassName b10 = JvmClassName.b(kotlinJvmBinaryClass.e());
        za.k.d(b10, "byClassId(kotlinClass.classId)");
        String e10 = kotlinJvmBinaryClass.b().e();
        JvmClassName jvmClassName = null;
        if (e10 != null) {
            if (e10.length() > 0) {
                jvmClassName = JvmClassName.d(e10);
            }
        }
    }
}
