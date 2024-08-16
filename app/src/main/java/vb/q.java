package vb;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import oc.ClassId;
import oc.Name;

/* compiled from: ReflectJavaAnnotationArguments.kt */
/* loaded from: classes2.dex */
public final class q extends f implements fc.m {

    /* renamed from: c, reason: collision with root package name */
    private final Enum<?> f19253c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public q(Name name, Enum<?> r32) {
        super(name, null);
        za.k.e(r32, ThermalBaseConfig.Item.ATTR_VALUE);
        this.f19253c = r32;
    }

    @Override // fc.m
    public ClassId b() {
        Class<?> cls = this.f19253c.getClass();
        if (!cls.isEnum()) {
            cls = cls.getEnclosingClass();
        }
        za.k.d(cls, "enumClass");
        return reflectClassUtil.a(cls);
    }

    @Override // fc.m
    public Name d() {
        return Name.f(this.f19253c.name());
    }
}
