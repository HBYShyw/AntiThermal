package kb;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class b implements InvocationHandler {

    /* renamed from: a, reason: collision with root package name */
    private final Class f14246a;

    /* renamed from: b, reason: collision with root package name */
    private final Map f14247b;

    /* renamed from: c, reason: collision with root package name */
    private final ma.h f14248c;

    /* renamed from: d, reason: collision with root package name */
    private final ma.h f14249d;

    /* renamed from: e, reason: collision with root package name */
    private final List f14250e;

    public b(Class cls, Map map, ma.h hVar, ma.h hVar2, List list) {
        this.f14246a = cls;
        this.f14247b = map;
        this.f14248c = hVar;
        this.f14249d = hVar2;
        this.f14250e = list;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) {
        Object i10;
        i10 = c.i(this.f14246a, this.f14247b, this.f14248c, this.f14249d, this.f14250e, obj, method, objArr);
        return i10;
    }
}
