package vb;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import oc.Name;

/* compiled from: ReflectJavaAnnotationArguments.kt */
/* loaded from: classes2.dex */
public final class s extends f implements fc.o {

    /* renamed from: c, reason: collision with root package name */
    private final Object f19255c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s(Name name, Object obj) {
        super(name, null);
        za.k.e(obj, ThermalBaseConfig.Item.ATTR_VALUE);
        this.f19255c = obj;
    }

    @Override // fc.o
    public Object getValue() {
        return this.f19255c;
    }
}
