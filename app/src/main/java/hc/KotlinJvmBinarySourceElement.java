package hc;

import cd.IncompatibleVersionErrorData;
import nc.JvmMetadataVersion;
import pb.b1;

/* compiled from: KotlinJvmBinarySourceElement.kt */
/* renamed from: hc.t, reason: use source file name */
/* loaded from: classes2.dex */
public final class KotlinJvmBinarySourceElement implements ed.f {

    /* renamed from: b, reason: collision with root package name */
    private final KotlinJvmBinaryClass f12202b;

    /* renamed from: c, reason: collision with root package name */
    private final IncompatibleVersionErrorData<JvmMetadataVersion> f12203c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f12204d;

    /* renamed from: e, reason: collision with root package name */
    private final ed.e f12205e;

    public KotlinJvmBinarySourceElement(KotlinJvmBinaryClass kotlinJvmBinaryClass, IncompatibleVersionErrorData<JvmMetadataVersion> incompatibleVersionErrorData, boolean z10, ed.e eVar) {
        za.k.e(kotlinJvmBinaryClass, "binaryClass");
        za.k.e(eVar, "abiStability");
        this.f12202b = kotlinJvmBinaryClass;
        this.f12203c = incompatibleVersionErrorData;
        this.f12204d = z10;
        this.f12205e = eVar;
    }

    @Override // pb.SourceElement
    public b1 a() {
        b1 b1Var = b1.f16671a;
        za.k.d(b1Var, "NO_SOURCE_FILE");
        return b1Var;
    }

    @Override // ed.f
    public String c() {
        return "Class '" + this.f12202b.e().b().b() + '\'';
    }

    public final KotlinJvmBinaryClass d() {
        return this.f12202b;
    }

    public String toString() {
        return KotlinJvmBinarySourceElement.class.getSimpleName() + ": " + this.f12202b;
    }
}
