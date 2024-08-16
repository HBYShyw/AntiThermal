package cc;

import fc.y;
import gd.TypeUsage;
import gd.Variance;
import gd.g0;
import gd.h0;
import gd.o0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.s;
import pb.DeclarationDescriptor;
import pb.SourceElement;
import sb.AbstractLazyTypeParameterDescriptor;

/* compiled from: LazyJavaTypeParameterDescriptor.kt */
/* renamed from: cc.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class LazyJavaTypeParameterDescriptor extends AbstractLazyTypeParameterDescriptor {

    /* renamed from: o, reason: collision with root package name */
    private final bc.g f5180o;

    /* renamed from: p, reason: collision with root package name */
    private final y f5181p;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyJavaTypeParameterDescriptor(bc.g gVar, y yVar, int i10, DeclarationDescriptor declarationDescriptor) {
        super(gVar.e(), declarationDescriptor, new bc.d(gVar, yVar, false, 4, null), yVar.getName(), Variance.INVARIANT, false, i10, SourceElement.f16664a, gVar.a().v());
        za.k.e(gVar, "c");
        za.k.e(yVar, "javaTypeParameter");
        za.k.e(declarationDescriptor, "containingDeclaration");
        this.f5180o = gVar;
        this.f5181p = yVar;
    }

    private final List<g0> V0() {
        int u7;
        List<g0> e10;
        Collection<fc.j> upperBounds = this.f5181p.getUpperBounds();
        if (upperBounds.isEmpty()) {
            o0 i10 = this.f5180o.d().t().i();
            za.k.d(i10, "c.module.builtIns.anyType");
            o0 I = this.f5180o.d().t().I();
            za.k.d(I, "c.module.builtIns.nullableAnyType");
            e10 = CollectionsJVM.e(h0.d(i10, I));
            return e10;
        }
        u7 = s.u(upperBounds, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = upperBounds.iterator();
        while (it.hasNext()) {
            arrayList.add(this.f5180o.g().o((fc.j) it.next(), dc.b.b(TypeUsage.COMMON, false, false, this, 3, null)));
        }
        return arrayList;
    }

    @Override // sb.AbstractTypeParameterDescriptor
    protected List<g0> O0(List<? extends g0> list) {
        za.k.e(list, "bounds");
        return this.f5180o.a().r().i(this, list, this.f5180o);
    }

    @Override // sb.AbstractTypeParameterDescriptor
    protected void T0(g0 g0Var) {
        za.k.e(g0Var, "type");
    }

    @Override // sb.AbstractTypeParameterDescriptor
    protected List<g0> U0() {
        return V0();
    }
}
