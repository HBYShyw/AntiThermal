package ee;

import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import za.k;
import zd.d0;

/* compiled from: RouteDatabase.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\n\u0010\u000bJ\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0002J\u000e\u0010\t\u001a\u00020\b2\u0006\u0010\u0006\u001a\u00020\u0002¨\u0006\f"}, d2 = {"Lee/h;", "", "Lzd/d0;", "failedRoute", "Lma/f0;", "b", "route", "a", "", "c", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ee.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class RouteDatabase {

    /* renamed from: a, reason: collision with root package name */
    private final Set<d0> f11239a = new LinkedHashSet();

    public final synchronized void a(d0 d0Var) {
        k.e(d0Var, "route");
        this.f11239a.remove(d0Var);
    }

    public final synchronized void b(d0 d0Var) {
        k.e(d0Var, "failedRoute");
        this.f11239a.add(d0Var);
    }

    public final synchronized boolean c(d0 route) {
        k.e(route, "route");
        return this.f11239a.contains(route);
    }
}
