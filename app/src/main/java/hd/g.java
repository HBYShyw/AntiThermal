package hd;

import gd.AbstractTypeRefiner;
import gd.TypeConstructor;
import gd.g0;
import java.util.Collection;
import oc.ClassId;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.ModuleDescriptor;

/* compiled from: KotlinTypeRefiner.kt */
/* loaded from: classes2.dex */
public abstract class g extends AbstractTypeRefiner {

    /* compiled from: KotlinTypeRefiner.kt */
    /* loaded from: classes2.dex */
    public static final class a extends g {

        /* renamed from: a, reason: collision with root package name */
        public static final a f12215a = new a();

        private a() {
        }

        @Override // hd.g
        public ClassDescriptor b(ClassId classId) {
            za.k.e(classId, "classId");
            return null;
        }

        @Override // hd.g
        public <S extends zc.h> S c(ClassDescriptor classDescriptor, ya.a<? extends S> aVar) {
            za.k.e(classDescriptor, "classDescriptor");
            za.k.e(aVar, "compute");
            return aVar.invoke();
        }

        @Override // hd.g
        public boolean d(ModuleDescriptor moduleDescriptor) {
            za.k.e(moduleDescriptor, "moduleDescriptor");
            return false;
        }

        @Override // hd.g
        public boolean e(TypeConstructor typeConstructor) {
            za.k.e(typeConstructor, "typeConstructor");
            return false;
        }

        @Override // hd.g
        public Collection<g0> g(ClassDescriptor classDescriptor) {
            za.k.e(classDescriptor, "classDescriptor");
            Collection<g0> q10 = classDescriptor.n().q();
            za.k.d(q10, "classDescriptor.typeConstructor.supertypes");
            return q10;
        }

        @Override // gd.AbstractTypeRefiner
        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public g0 a(kd.i iVar) {
            za.k.e(iVar, "type");
            return (g0) iVar;
        }

        @Override // hd.g
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public ClassDescriptor f(DeclarationDescriptor declarationDescriptor) {
            za.k.e(declarationDescriptor, "descriptor");
            return null;
        }
    }

    public abstract ClassDescriptor b(ClassId classId);

    public abstract <S extends zc.h> S c(ClassDescriptor classDescriptor, ya.a<? extends S> aVar);

    public abstract boolean d(ModuleDescriptor moduleDescriptor);

    public abstract boolean e(TypeConstructor typeConstructor);

    public abstract ClassifierDescriptor f(DeclarationDescriptor declarationDescriptor);

    public abstract Collection<g0> g(ClassDescriptor classDescriptor);

    /* renamed from: h */
    public abstract g0 a(kd.i iVar);
}
