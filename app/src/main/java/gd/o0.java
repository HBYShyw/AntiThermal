package gd;

import java.util.Iterator;
import qb.AnnotationDescriptor;

/* compiled from: KotlinType.kt */
/* loaded from: classes2.dex */
public abstract class o0 extends v1 implements kd.k, kd.l {
    public o0() {
        super(null);
    }

    public abstract o0 d1(boolean z10);

    public abstract o0 e1(c1 c1Var);

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        Iterator<AnnotationDescriptor> it = i().iterator();
        while (it.hasNext()) {
            sd.l.i(sb2, "[", rc.c.s(rc.c.f17711j, it.next(), null, 2, null), "] ");
        }
        sb2.append(W0());
        if (!U0().isEmpty()) {
            kotlin.collections.p.a0(U0(), sb2, ", ", "<", ">", 0, null, null, 112, null);
        }
        if (X0()) {
            sb2.append("?");
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }
}
