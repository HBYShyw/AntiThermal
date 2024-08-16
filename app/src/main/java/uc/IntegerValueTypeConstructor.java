package uc;

import gd.TypeConstructor;
import gd.g0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mb.KotlinBuiltIns;
import pb.ClassifierDescriptor;
import pb.ModuleDescriptor;
import pb.TypeParameterDescriptor;

/* compiled from: IntegerValueTypeConstructor.kt */
/* renamed from: uc.p, reason: use source file name */
/* loaded from: classes2.dex */
public final class IntegerValueTypeConstructor implements TypeConstructor {

    /* renamed from: a, reason: collision with root package name */
    private final long f19010a;

    /* renamed from: b, reason: collision with root package name */
    private final ModuleDescriptor f19011b;

    /* renamed from: c, reason: collision with root package name */
    private final ArrayList<g0> f19012c;

    public Void c() {
        return null;
    }

    @Override // gd.TypeConstructor
    public List<TypeParameterDescriptor> getParameters() {
        List<TypeParameterDescriptor> j10;
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // gd.TypeConstructor
    public Collection<g0> q() {
        return this.f19012c;
    }

    @Override // gd.TypeConstructor
    public KotlinBuiltIns t() {
        return this.f19011b.t();
    }

    public String toString() {
        return "IntegerValueType(" + this.f19010a + ')';
    }

    @Override // gd.TypeConstructor
    public TypeConstructor u(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this;
    }

    @Override // gd.TypeConstructor
    public /* bridge */ /* synthetic */ ClassifierDescriptor v() {
        return (ClassifierDescriptor) c();
    }

    @Override // gd.TypeConstructor
    public boolean w() {
        return false;
    }
}
