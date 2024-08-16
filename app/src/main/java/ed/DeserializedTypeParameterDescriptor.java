package ed;

import cd.NameResolverUtil;
import cd.ProtoEnumFlags;
import cd.d0;
import fd.StorageManager;
import gd.g0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jc.q;
import jc.s;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import lc.protoTypeTableUtil;
import oc.Name;
import pb.DeclarationDescriptor;
import pb.SourceElement;
import pb.SupertypeLoopChecker;
import qb.AnnotationDescriptor;
import sb.AbstractLazyTypeParameterDescriptor;
import za.Lambda;

/* compiled from: DeserializedTypeParameterDescriptor.kt */
/* renamed from: ed.m, reason: use source file name */
/* loaded from: classes2.dex */
public final class DeserializedTypeParameterDescriptor extends AbstractLazyTypeParameterDescriptor {

    /* renamed from: o, reason: collision with root package name */
    private final cd.m f11150o;

    /* renamed from: p, reason: collision with root package name */
    private final s f11151p;

    /* renamed from: q, reason: collision with root package name */
    private final ed.a f11152q;

    /* compiled from: DeserializedTypeParameterDescriptor.kt */
    /* renamed from: ed.m$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<List<? extends AnnotationDescriptor>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<AnnotationDescriptor> invoke() {
            List<AnnotationDescriptor> z02;
            z02 = _Collections.z0(DeserializedTypeParameterDescriptor.this.f11150o.c().d().j(DeserializedTypeParameterDescriptor.this.X0(), DeserializedTypeParameterDescriptor.this.f11150o.g()));
            return z02;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public DeserializedTypeParameterDescriptor(cd.m mVar, s sVar, int i10) {
        super(r2, r3, r4, r5, r0.d(r1), sVar.J(), i10, SourceElement.f16664a, SupertypeLoopChecker.a.f16675a);
        za.k.e(mVar, "c");
        za.k.e(sVar, "proto");
        StorageManager h10 = mVar.h();
        DeclarationDescriptor e10 = mVar.e();
        qb.g b10 = qb.g.f17195b.b();
        Name b11 = NameResolverUtil.b(mVar.g(), sVar.I());
        ProtoEnumFlags protoEnumFlags = ProtoEnumFlags.f5188a;
        s.c O = sVar.O();
        za.k.d(O, "proto.variance");
        this.f11150o = mVar;
        this.f11151p = sVar;
        this.f11152q = new ed.a(mVar.h(), new a());
    }

    @Override // sb.AbstractTypeParameterDescriptor
    protected List<g0> U0() {
        int u7;
        List<g0> e10;
        List<q> s7 = protoTypeTableUtil.s(this.f11151p, this.f11150o.j());
        if (s7.isEmpty()) {
            e10 = CollectionsJVM.e(wc.c.j(this).y());
            return e10;
        }
        d0 i10 = this.f11150o.i();
        u7 = kotlin.collections.s.u(s7, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = s7.iterator();
        while (it.hasNext()) {
            arrayList.add(i10.q((q) it.next()));
        }
        return arrayList;
    }

    @Override // qb.AnnotatedImpl, qb.a
    /* renamed from: W0, reason: merged with bridge method [inline-methods] */
    public ed.a i() {
        return this.f11152q;
    }

    public final s X0() {
        return this.f11151p;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.AbstractTypeParameterDescriptor
    /* renamed from: Y0, reason: merged with bridge method [inline-methods] */
    public Void T0(g0 g0Var) {
        za.k.e(g0Var, "type");
        throw new IllegalStateException("There should be no cycles for deserialized type parameters, but found for: " + this);
    }
}
