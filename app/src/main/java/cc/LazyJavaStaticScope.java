package cc;

import cc.LazyJavaScope;
import com.oplus.backup.sdk.common.utils.Constants;
import fc.r;
import gd.g0;
import java.util.Collection;
import java.util.List;
import oc.Name;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;

/* compiled from: LazyJavaStaticScope.kt */
/* renamed from: cc.m, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class LazyJavaStaticScope extends LazyJavaScope {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyJavaStaticScope(bc.g gVar) {
        super(gVar, null, 2, 0 == true ? 1 : 0);
        za.k.e(gVar, "c");
    }

    @Override // cc.LazyJavaScope
    protected LazyJavaScope.a H(r rVar, List<? extends TypeParameterDescriptor> list, g0 g0Var, List<? extends ValueParameterDescriptor> list2) {
        List j10;
        za.k.e(rVar, Constants.MessagerConstants.METHOD_KEY);
        za.k.e(list, "methodTypeParameters");
        za.k.e(g0Var, "returnType");
        za.k.e(list2, "valueParameters");
        j10 = kotlin.collections.r.j();
        return new LazyJavaScope.a(g0Var, null, list2, list, false, j10);
    }

    @Override // cc.LazyJavaScope
    protected void s(Name name, Collection<PropertyDescriptor> collection) {
        za.k.e(name, "name");
        za.k.e(collection, "result");
    }

    @Override // cc.LazyJavaScope
    protected ReceiverParameterDescriptor z() {
        return null;
    }
}
