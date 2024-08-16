package jb;

import gb.KParameter;
import java.util.List;
import kotlin.collections._Collections;
import oc.Name;
import pb.CallableDescriptor;
import pb.FunctionDescriptor;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.ValueParameterDescriptor;
import za.Lambda;

/* compiled from: ReflectionObjectRenderer.kt */
/* renamed from: jb.k0, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectionObjectRenderer {

    /* renamed from: a, reason: collision with root package name */
    public static final ReflectionObjectRenderer f13232a = new ReflectionObjectRenderer();

    /* renamed from: b, reason: collision with root package name */
    private static final rc.c f13233b = rc.c.f17708g;

    /* compiled from: ReflectionObjectRenderer.kt */
    /* renamed from: jb.k0$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f13234a;

        static {
            int[] iArr = new int[KParameter.a.values().length];
            try {
                iArr[KParameter.a.EXTENSION_RECEIVER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[KParameter.a.INSTANCE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[KParameter.a.VALUE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f13234a = iArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectionObjectRenderer.kt */
    /* renamed from: jb.k0$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l<ValueParameterDescriptor, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f13235e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(ValueParameterDescriptor valueParameterDescriptor) {
            ReflectionObjectRenderer reflectionObjectRenderer = ReflectionObjectRenderer.f13232a;
            gd.g0 type = valueParameterDescriptor.getType();
            za.k.d(type, "it.type");
            return reflectionObjectRenderer.h(type);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectionObjectRenderer.kt */
    /* renamed from: jb.k0$c */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<ValueParameterDescriptor, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f13236e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(ValueParameterDescriptor valueParameterDescriptor) {
            ReflectionObjectRenderer reflectionObjectRenderer = ReflectionObjectRenderer.f13232a;
            gd.g0 type = valueParameterDescriptor.getType();
            za.k.d(type, "it.type");
            return reflectionObjectRenderer.h(type);
        }
    }

    private ReflectionObjectRenderer() {
    }

    private final void a(StringBuilder sb2, ReceiverParameterDescriptor receiverParameterDescriptor) {
        if (receiverParameterDescriptor != null) {
            gd.g0 type = receiverParameterDescriptor.getType();
            za.k.d(type, "receiver.type");
            sb2.append(h(type));
            sb2.append(".");
        }
    }

    private final void b(StringBuilder sb2, CallableDescriptor callableDescriptor) {
        ReceiverParameterDescriptor i10 = o0.i(callableDescriptor);
        ReceiverParameterDescriptor r02 = callableDescriptor.r0();
        a(sb2, i10);
        boolean z10 = (i10 == null || r02 == null) ? false : true;
        if (z10) {
            sb2.append("(");
        }
        a(sb2, r02);
        if (z10) {
            sb2.append(")");
        }
    }

    private final String c(CallableDescriptor callableDescriptor) {
        if (callableDescriptor instanceof PropertyDescriptor) {
            return g((PropertyDescriptor) callableDescriptor);
        }
        if (callableDescriptor instanceof FunctionDescriptor) {
            return d((FunctionDescriptor) callableDescriptor);
        }
        throw new IllegalStateException(("Illegal callable: " + callableDescriptor).toString());
    }

    public final String d(FunctionDescriptor functionDescriptor) {
        za.k.e(functionDescriptor, "descriptor");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("fun ");
        ReflectionObjectRenderer reflectionObjectRenderer = f13232a;
        reflectionObjectRenderer.b(sb2, functionDescriptor);
        rc.c cVar = f13233b;
        Name name = functionDescriptor.getName();
        za.k.d(name, "descriptor.name");
        sb2.append(cVar.v(name, true));
        List<ValueParameterDescriptor> l10 = functionDescriptor.l();
        za.k.d(l10, "descriptor.valueParameters");
        _Collections.a0(l10, sb2, ", ", "(", ")", 0, null, b.f13235e, 48, null);
        sb2.append(": ");
        gd.g0 f10 = functionDescriptor.f();
        za.k.b(f10);
        sb2.append(reflectionObjectRenderer.h(f10));
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public final String e(FunctionDescriptor functionDescriptor) {
        za.k.e(functionDescriptor, "invoke");
        StringBuilder sb2 = new StringBuilder();
        ReflectionObjectRenderer reflectionObjectRenderer = f13232a;
        reflectionObjectRenderer.b(sb2, functionDescriptor);
        List<ValueParameterDescriptor> l10 = functionDescriptor.l();
        za.k.d(l10, "invoke.valueParameters");
        _Collections.a0(l10, sb2, ", ", "(", ")", 0, null, c.f13236e, 48, null);
        sb2.append(" -> ");
        gd.g0 f10 = functionDescriptor.f();
        za.k.b(f10);
        sb2.append(reflectionObjectRenderer.h(f10));
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public final String f(KParameterImpl kParameterImpl) {
        za.k.e(kParameterImpl, "parameter");
        StringBuilder sb2 = new StringBuilder();
        int i10 = a.f13234a[kParameterImpl.getKind().ordinal()];
        if (i10 == 1) {
            sb2.append("extension receiver parameter");
        } else if (i10 == 2) {
            sb2.append("instance parameter");
        } else if (i10 == 3) {
            sb2.append("parameter #" + kParameterImpl.j() + ' ' + kParameterImpl.getName());
        }
        sb2.append(" of ");
        sb2.append(f13232a.c(kParameterImpl.g().L()));
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public final String g(PropertyDescriptor propertyDescriptor) {
        za.k.e(propertyDescriptor, "descriptor");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(propertyDescriptor.p0() ? "var " : "val ");
        ReflectionObjectRenderer reflectionObjectRenderer = f13232a;
        reflectionObjectRenderer.b(sb2, propertyDescriptor);
        rc.c cVar = f13233b;
        Name name = propertyDescriptor.getName();
        za.k.d(name, "descriptor.name");
        sb2.append(cVar.v(name, true));
        sb2.append(": ");
        gd.g0 type = propertyDescriptor.getType();
        za.k.d(type, "descriptor.type");
        sb2.append(reflectionObjectRenderer.h(type));
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public final String h(gd.g0 g0Var) {
        za.k.e(g0Var, "type");
        return f13233b.w(g0Var);
    }
}
