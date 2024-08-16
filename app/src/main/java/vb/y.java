package vb;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

/* compiled from: ReflectJavaRecordComponent.kt */
/* loaded from: classes2.dex */
public final class y extends t implements fc.w {

    /* renamed from: a, reason: collision with root package name */
    private final Object f19261a;

    public y(Object obj) {
        za.k.e(obj, "recordComponent");
        this.f19261a = obj;
    }

    @Override // vb.t
    public Member X() {
        Method c10 = a.f19203a.c(this.f19261a);
        if (c10 != null) {
            return c10;
        }
        throw new NoSuchMethodError("Can't find `getAccessor` method");
    }

    @Override // fc.w
    public boolean a() {
        return false;
    }

    @Override // fc.w
    public fc.x getType() {
        Class<?> d10 = a.f19203a.d(this.f19261a);
        if (d10 != null) {
            return new ReflectJavaClassifierType(d10);
        }
        throw new NoSuchMethodError("Can't find `getType` method");
    }
}
