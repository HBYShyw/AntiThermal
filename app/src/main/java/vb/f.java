package vb;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.lang.annotation.Annotation;
import oc.Name;
import za.DefaultConstructorMarker;

/* compiled from: ReflectJavaAnnotationArguments.kt */
/* loaded from: classes2.dex */
public abstract class f implements fc.b {

    /* renamed from: b, reason: collision with root package name */
    public static final a f19232b = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final Name f19233a;

    /* compiled from: ReflectJavaAnnotationArguments.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final f a(Object obj, Name name) {
            za.k.e(obj, ThermalBaseConfig.Item.ATTR_VALUE);
            return reflectClassUtil.h(obj.getClass()) ? new q(name, (Enum) obj) : obj instanceof Annotation ? new g(name, (Annotation) obj) : obj instanceof Object[] ? new j(name, (Object[]) obj) : obj instanceof Class ? new m(name, (Class) obj) : new s(name, obj);
        }
    }

    private f(Name name) {
        this.f19233a = name;
    }

    public /* synthetic */ f(Name name, DefaultConstructorMarker defaultConstructorMarker) {
        this(name);
    }

    @Override // fc.b
    public Name getName() {
        return this.f19233a;
    }
}
