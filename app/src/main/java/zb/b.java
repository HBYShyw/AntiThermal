package zb;

import ac.PossiblyExternalAnnotationDescriptor;
import fd.m;
import gb.l;
import gd.o0;
import java.util.Collection;
import java.util.Map;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import oc.FqName;
import oc.Name;
import pb.SourceElement;
import qb.AnnotationDescriptor;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import za.k;

/* compiled from: JavaAnnotationMapper.kt */
/* loaded from: classes2.dex */
public class b implements AnnotationDescriptor, PossiblyExternalAnnotationDescriptor {

    /* renamed from: f, reason: collision with root package name */
    static final /* synthetic */ l<Object>[] f20383f = {Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "type", "getType()Lorg/jetbrains/kotlin/types/SimpleType;"))};

    /* renamed from: a, reason: collision with root package name */
    private final FqName f20384a;

    /* renamed from: b, reason: collision with root package name */
    private final SourceElement f20385b;

    /* renamed from: c, reason: collision with root package name */
    private final fd.i f20386c;

    /* renamed from: d, reason: collision with root package name */
    private final fc.b f20387d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f20388e;

    /* compiled from: JavaAnnotationMapper.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<o0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ bc.g f20389e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ b f20390f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(bc.g gVar, b bVar) {
            super(0);
            this.f20389e = gVar;
            this.f20390f = bVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final o0 invoke() {
            o0 x10 = this.f20389e.d().t().o(this.f20390f.d()).x();
            k.d(x10, "c.module.builtIns.getBui…qName(fqName).defaultType");
            return x10;
        }
    }

    public b(bc.g gVar, fc.a aVar, FqName fqName) {
        SourceElement sourceElement;
        fc.b bVar;
        Collection<fc.b> b10;
        Object U;
        k.e(gVar, "c");
        k.e(fqName, "fqName");
        this.f20384a = fqName;
        if (aVar == null || (sourceElement = gVar.a().t().a(aVar)) == null) {
            sourceElement = SourceElement.f16664a;
            k.d(sourceElement, "NO_SOURCE");
        }
        this.f20385b = sourceElement;
        this.f20386c = gVar.e().g(new a(gVar, this));
        if (aVar == null || (b10 = aVar.b()) == null) {
            bVar = null;
        } else {
            U = _Collections.U(b10);
            bVar = (fc.b) U;
        }
        this.f20387d = bVar;
        this.f20388e = aVar != null && aVar.h();
    }

    @Override // qb.AnnotationDescriptor
    public Map<Name, uc.g<?>> a() {
        Map<Name, uc.g<?>> i10;
        i10 = m0.i();
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final fc.b b() {
        return this.f20387d;
    }

    @Override // qb.AnnotationDescriptor
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 getType() {
        return (o0) m.a(this.f20386c, this, f20383f[0]);
    }

    @Override // qb.AnnotationDescriptor
    public FqName d() {
        return this.f20384a;
    }

    @Override // ac.PossiblyExternalAnnotationDescriptor
    public boolean h() {
        return this.f20388e;
    }

    @Override // qb.AnnotationDescriptor
    public SourceElement z() {
        return this.f20385b;
    }
}
