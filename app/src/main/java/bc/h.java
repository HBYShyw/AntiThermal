package bc;

import cc.LazyJavaTypeParameterDescriptor;
import fc.y;
import fc.z;
import java.util.Map;
import pb.DeclarationDescriptor;
import pb.TypeParameterDescriptor;
import qd.collections;
import ya.l;
import za.Lambda;

/* compiled from: resolvers.kt */
/* loaded from: classes2.dex */
public final class h implements k {

    /* renamed from: a, reason: collision with root package name */
    private final g f4699a;

    /* renamed from: b, reason: collision with root package name */
    private final DeclarationDescriptor f4700b;

    /* renamed from: c, reason: collision with root package name */
    private final int f4701c;

    /* renamed from: d, reason: collision with root package name */
    private final Map<y, Integer> f4702d;

    /* renamed from: e, reason: collision with root package name */
    private final fd.h<y, LazyJavaTypeParameterDescriptor> f4703e;

    /* compiled from: resolvers.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements l<y, LazyJavaTypeParameterDescriptor> {
        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final LazyJavaTypeParameterDescriptor invoke(y yVar) {
            za.k.e(yVar, "typeParameter");
            Integer num = (Integer) h.this.f4702d.get(yVar);
            if (num == null) {
                return null;
            }
            h hVar = h.this;
            return new LazyJavaTypeParameterDescriptor(bc.a.h(bc.a.a(hVar.f4699a, hVar), hVar.f4700b.i()), yVar, hVar.f4701c + num.intValue(), hVar.f4700b);
        }
    }

    public h(g gVar, DeclarationDescriptor declarationDescriptor, z zVar, int i10) {
        za.k.e(gVar, "c");
        za.k.e(declarationDescriptor, "containingDeclaration");
        za.k.e(zVar, "typeParameterOwner");
        this.f4699a = gVar;
        this.f4700b = declarationDescriptor;
        this.f4701c = i10;
        this.f4702d = collections.d(zVar.m());
        this.f4703e = gVar.e().b(new a());
    }

    @Override // bc.k
    public TypeParameterDescriptor a(y yVar) {
        za.k.e(yVar, "javaTypeParameter");
        LazyJavaTypeParameterDescriptor invoke = this.f4703e.invoke(yVar);
        return invoke != null ? invoke : this.f4699a.f().a(yVar);
    }
}
