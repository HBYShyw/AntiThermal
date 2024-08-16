package jb;

import gb.KParameter;
import gb.KType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import jb.ReflectProperties;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.ParameterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.ValueParameterDescriptor;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: KParameterImpl.kt */
/* renamed from: jb.v, reason: use source file name */
/* loaded from: classes2.dex */
public final class KParameterImpl implements KParameter {

    /* renamed from: j, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f13348j = {Reflection.g(new PropertyReference1Impl(Reflection.b(KParameterImpl.class), "descriptor", "getDescriptor()Lorg/jetbrains/kotlin/descriptors/ParameterDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(KParameterImpl.class), "annotations", "getAnnotations()Ljava/util/List;"))};

    /* renamed from: e, reason: collision with root package name */
    private final KCallableImpl<?> f13349e;

    /* renamed from: f, reason: collision with root package name */
    private final int f13350f;

    /* renamed from: g, reason: collision with root package name */
    private final KParameter.a f13351g;

    /* renamed from: h, reason: collision with root package name */
    private final ReflectProperties.a f13352h;

    /* renamed from: i, reason: collision with root package name */
    private final ReflectProperties.a f13353i;

    /* compiled from: KParameterImpl.kt */
    /* renamed from: jb.v$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<List<? extends Annotation>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<Annotation> invoke() {
            return o0.e(KParameterImpl.this.m());
        }
    }

    /* compiled from: KParameterImpl.kt */
    /* renamed from: jb.v$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<Type> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Type invoke() {
            ParameterDescriptor m10 = KParameterImpl.this.m();
            if ((m10 instanceof ReceiverParameterDescriptor) && za.k.a(o0.i(KParameterImpl.this.g().L()), m10) && KParameterImpl.this.g().L().getKind() == CallableMemberDescriptor.a.FAKE_OVERRIDE) {
                DeclarationDescriptor b10 = KParameterImpl.this.g().L().b();
                za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                Class<?> p10 = o0.p((ClassDescriptor) b10);
                if (p10 != null) {
                    return p10;
                }
                throw new KotlinReflectionInternalError("Cannot determine receiver Java type of inherited declaration: " + m10);
            }
            return KParameterImpl.this.g().F().a().get(KParameterImpl.this.j());
        }
    }

    public KParameterImpl(KCallableImpl<?> kCallableImpl, int i10, KParameter.a aVar, ya.a<? extends ParameterDescriptor> aVar2) {
        za.k.e(kCallableImpl, "callable");
        za.k.e(aVar, "kind");
        za.k.e(aVar2, "computeDescriptor");
        this.f13349e = kCallableImpl;
        this.f13350f = i10;
        this.f13351g = aVar;
        this.f13352h = ReflectProperties.d(aVar2);
        this.f13353i = ReflectProperties.d(new a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ParameterDescriptor m() {
        T b10 = this.f13352h.b(this, f13348j[0]);
        za.k.d(b10, "<get-descriptor>(...)");
        return (ParameterDescriptor) b10;
    }

    @Override // gb.KParameter
    public boolean a() {
        ParameterDescriptor m10 = m();
        return (m10 instanceof ValueParameterDescriptor) && ((ValueParameterDescriptor) m10).q0() != null;
    }

    public boolean equals(Object obj) {
        if (obj instanceof KParameterImpl) {
            KParameterImpl kParameterImpl = (KParameterImpl) obj;
            if (za.k.a(this.f13349e, kParameterImpl.f13349e) && j() == kParameterImpl.j()) {
                return true;
            }
        }
        return false;
    }

    public final KCallableImpl<?> g() {
        return this.f13349e;
    }

    @Override // gb.KParameter
    public KParameter.a getKind() {
        return this.f13351g;
    }

    @Override // gb.KParameter
    public String getName() {
        ParameterDescriptor m10 = m();
        ValueParameterDescriptor valueParameterDescriptor = m10 instanceof ValueParameterDescriptor ? (ValueParameterDescriptor) m10 : null;
        if (valueParameterDescriptor == null || valueParameterDescriptor.b().O()) {
            return null;
        }
        Name name = valueParameterDescriptor.getName();
        za.k.d(name, "valueParameter.name");
        if (name.g()) {
            return null;
        }
        return name.b();
    }

    @Override // gb.KParameter
    public KType getType() {
        gd.g0 type = m().getType();
        za.k.d(type, "descriptor.type");
        return new KTypeImpl(type, new b());
    }

    public int hashCode() {
        return (this.f13349e.hashCode() * 31) + Integer.hashCode(j());
    }

    @Override // gb.KAnnotatedElement
    public List<Annotation> i() {
        T b10 = this.f13353i.b(this, f13348j[1]);
        za.k.d(b10, "<get-annotations>(...)");
        return (List) b10;
    }

    @Override // gb.KParameter
    public int j() {
        return this.f13350f;
    }

    public String toString() {
        return ReflectionObjectRenderer.f13232a.f(this);
    }

    @Override // gb.KParameter
    public boolean z() {
        ParameterDescriptor m10 = m();
        ValueParameterDescriptor valueParameterDescriptor = m10 instanceof ValueParameterDescriptor ? (ValueParameterDescriptor) m10 : null;
        if (valueParameterDescriptor != null) {
            return wc.c.c(valueParameterDescriptor);
        }
        return false;
    }
}
