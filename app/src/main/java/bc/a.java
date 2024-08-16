package bc;

import fc.z;
import ma.l;
import pb.ClassOrPackageFragmentDescriptor;
import pb.DeclarationDescriptor;
import yb.JavaTypeQualifiersByElementType;
import za.Lambda;

/* compiled from: context.kt */
/* loaded from: classes2.dex */
public final class a {

    /* compiled from: context.kt */
    /* renamed from: bc.a$a */
    /* loaded from: classes2.dex */
    public static final class C0013a extends Lambda implements ya.a<JavaTypeQualifiersByElementType> {

        /* renamed from: e */
        final /* synthetic */ g f4656e;

        /* renamed from: f */
        final /* synthetic */ ClassOrPackageFragmentDescriptor f4657f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C0013a(g gVar, ClassOrPackageFragmentDescriptor classOrPackageFragmentDescriptor) {
            super(0);
            this.f4656e = gVar;
            this.f4657f = classOrPackageFragmentDescriptor;
        }

        @Override // ya.a
        /* renamed from: a */
        public final JavaTypeQualifiersByElementType invoke() {
            return a.g(this.f4656e, this.f4657f.i());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: context.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.a<JavaTypeQualifiersByElementType> {

        /* renamed from: e */
        final /* synthetic */ g f4658e;

        /* renamed from: f */
        final /* synthetic */ qb.g f4659f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(g gVar, qb.g gVar2) {
            super(0);
            this.f4658e = gVar;
            this.f4659f = gVar2;
        }

        @Override // ya.a
        /* renamed from: a */
        public final JavaTypeQualifiersByElementType invoke() {
            return a.g(this.f4658e, this.f4659f);
        }
    }

    public static final g a(g gVar, k kVar) {
        za.k.e(gVar, "<this>");
        za.k.e(kVar, "typeParameterResolver");
        return new g(gVar.a(), kVar, gVar.c());
    }

    private static final g b(g gVar, DeclarationDescriptor declarationDescriptor, z zVar, int i10, ma.h<JavaTypeQualifiersByElementType> hVar) {
        k f10;
        bc.b a10 = gVar.a();
        if (zVar != null) {
            f10 = new h(gVar, declarationDescriptor, zVar, i10);
        } else {
            f10 = gVar.f();
        }
        return new g(a10, f10, hVar);
    }

    public static final g c(g gVar, ClassOrPackageFragmentDescriptor classOrPackageFragmentDescriptor, z zVar, int i10) {
        ma.h a10;
        za.k.e(gVar, "<this>");
        za.k.e(classOrPackageFragmentDescriptor, "containingDeclaration");
        a10 = ma.j.a(l.NONE, new C0013a(gVar, classOrPackageFragmentDescriptor));
        return b(gVar, classOrPackageFragmentDescriptor, zVar, i10, a10);
    }

    public static /* synthetic */ g d(g gVar, ClassOrPackageFragmentDescriptor classOrPackageFragmentDescriptor, z zVar, int i10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            zVar = null;
        }
        if ((i11 & 4) != 0) {
            i10 = 0;
        }
        return c(gVar, classOrPackageFragmentDescriptor, zVar, i10);
    }

    public static final g e(g gVar, DeclarationDescriptor declarationDescriptor, z zVar, int i10) {
        za.k.e(gVar, "<this>");
        za.k.e(declarationDescriptor, "containingDeclaration");
        za.k.e(zVar, "typeParameterOwner");
        return b(gVar, declarationDescriptor, zVar, i10, gVar.c());
    }

    public static /* synthetic */ g f(g gVar, DeclarationDescriptor declarationDescriptor, z zVar, int i10, int i11, Object obj) {
        if ((i11 & 4) != 0) {
            i10 = 0;
        }
        return e(gVar, declarationDescriptor, zVar, i10);
    }

    public static final JavaTypeQualifiersByElementType g(g gVar, qb.g gVar2) {
        za.k.e(gVar, "<this>");
        za.k.e(gVar2, "additionalAnnotations");
        return gVar.a().a().c(gVar.b(), gVar2);
    }

    public static final g h(g gVar, qb.g gVar2) {
        ma.h a10;
        za.k.e(gVar, "<this>");
        za.k.e(gVar2, "additionalAnnotations");
        if (gVar2.isEmpty()) {
            return gVar;
        }
        bc.b a11 = gVar.a();
        k f10 = gVar.f();
        a10 = ma.j.a(l.NONE, new b(gVar, gVar2));
        return new g(a11, f10, a10);
    }

    public static final g i(g gVar, bc.b bVar) {
        za.k.e(gVar, "<this>");
        za.k.e(bVar, "components");
        return new g(bVar, gVar.f(), gVar.c());
    }
}
