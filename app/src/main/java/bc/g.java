package bc;

import fd.StorageManager;
import pb.ModuleDescriptor;
import yb.JavaTypeQualifiersByElementType;

/* compiled from: context.kt */
/* loaded from: classes2.dex */
public final class g {

    /* renamed from: a, reason: collision with root package name */
    private final b f4694a;

    /* renamed from: b, reason: collision with root package name */
    private final k f4695b;

    /* renamed from: c, reason: collision with root package name */
    private final ma.h<JavaTypeQualifiersByElementType> f4696c;

    /* renamed from: d, reason: collision with root package name */
    private final ma.h f4697d;

    /* renamed from: e, reason: collision with root package name */
    private final dc.d f4698e;

    public g(b bVar, k kVar, ma.h<JavaTypeQualifiersByElementType> hVar) {
        za.k.e(bVar, "components");
        za.k.e(kVar, "typeParameterResolver");
        za.k.e(hVar, "delegateForDefaultTypeQualifiers");
        this.f4694a = bVar;
        this.f4695b = kVar;
        this.f4696c = hVar;
        this.f4697d = hVar;
        this.f4698e = new dc.d(this, kVar);
    }

    public final b a() {
        return this.f4694a;
    }

    public final JavaTypeQualifiersByElementType b() {
        return (JavaTypeQualifiersByElementType) this.f4697d.getValue();
    }

    public final ma.h<JavaTypeQualifiersByElementType> c() {
        return this.f4696c;
    }

    public final ModuleDescriptor d() {
        return this.f4694a.m();
    }

    public final StorageManager e() {
        return this.f4694a.u();
    }

    public final k f() {
        return this.f4695b;
    }

    public final dc.d g() {
        return this.f4698e;
    }
}
