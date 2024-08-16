package rc;

import java.util.Set;
import kotlin.collections.s0;
import oc.FqName;

/* compiled from: DescriptorRenderer.kt */
/* loaded from: classes2.dex */
public final class h {

    /* renamed from: a, reason: collision with root package name */
    public static final h f17786a = new h();

    /* renamed from: b, reason: collision with root package name */
    private static final Set<FqName> f17787b;

    static {
        Set<FqName> h10;
        h10 = s0.h(new FqName("kotlin.internal.NoInfer"), new FqName("kotlin.internal.Exact"));
        f17787b = h10;
    }

    private h() {
    }

    public final Set<FqName> a() {
        return f17787b;
    }
}
