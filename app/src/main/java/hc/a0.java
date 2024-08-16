package hc;

import gd.g0;
import java.util.Collection;
import kotlin.collections._Collections;
import pb.ClassDescriptor;

/* compiled from: methodSignatureMapping.kt */
/* loaded from: classes2.dex */
public final class a0 implements z<m> {

    /* renamed from: a, reason: collision with root package name */
    public static final a0 f12109a = new a0();

    private a0() {
    }

    @Override // hc.z
    public String a(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "classDescriptor");
        return null;
    }

    @Override // hc.z
    public void b(g0 g0Var, ClassDescriptor classDescriptor) {
        za.k.e(g0Var, "kotlinType");
        za.k.e(classDescriptor, "descriptor");
    }

    @Override // hc.z
    public g0 c(Collection<? extends g0> collection) {
        String c02;
        za.k.e(collection, "types");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("There should be no intersection type in existing descriptors, but found: ");
        c02 = _Collections.c0(collection, null, null, null, 0, null, null, 63, null);
        sb2.append(c02);
        throw new AssertionError(sb2.toString());
    }

    @Override // hc.z
    public g0 d(g0 g0Var) {
        za.k.e(g0Var, "kotlinType");
        return null;
    }

    @Override // hc.z
    public String e(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "classDescriptor");
        return null;
    }

    @Override // hc.z
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public m f(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "classDescriptor");
        return null;
    }
}
