package cd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import fb._Ranges;
import gd.g0;
import gd.o0;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jc.b;
import kotlin.collections.MapsJVM;
import kotlin.collections.PrimitiveIterators;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import lc.Flags;
import lc.NameResolver;
import mb.KotlinBuiltIns;
import oc.ClassId;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import pb.SourceElement;
import pb.ValueParameterDescriptor;
import pb.findClassInModule;
import qb.AnnotationDescriptor;
import qb.AnnotationDescriptorImpl;

/* compiled from: AnnotationDeserializer.kt */
/* renamed from: cd.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class AnnotationDeserializer {

    /* renamed from: a, reason: collision with root package name */
    private final ModuleDescriptor f5224a;

    /* renamed from: b, reason: collision with root package name */
    private final NotFoundClasses f5225b;

    /* compiled from: AnnotationDeserializer.kt */
    /* renamed from: cd.e$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f5226a;

        static {
            int[] iArr = new int[b.C0061b.c.EnumC0064c.values().length];
            try {
                iArr[b.C0061b.c.EnumC0064c.BYTE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.CHAR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.SHORT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.INT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.LONG.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.FLOAT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.DOUBLE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.BOOLEAN.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.STRING.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.CLASS.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.ENUM.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.ANNOTATION.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                iArr[b.C0061b.c.EnumC0064c.ARRAY.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            f5226a = iArr;
        }
    }

    public AnnotationDeserializer(ModuleDescriptor moduleDescriptor, NotFoundClasses notFoundClasses) {
        za.k.e(moduleDescriptor, "module");
        za.k.e(notFoundClasses, "notFoundClasses");
        this.f5224a = moduleDescriptor;
        this.f5225b = notFoundClasses;
    }

    private final boolean b(uc.g<?> gVar, g0 g0Var, b.C0061b.c cVar) {
        Iterable k10;
        b.C0061b.c.EnumC0064c N = cVar.N();
        int i10 = N == null ? -1 : a.f5226a[N.ordinal()];
        if (i10 == 10) {
            ClassifierDescriptor v7 = g0Var.W0().v();
            ClassDescriptor classDescriptor = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
            if (classDescriptor != null && !KotlinBuiltIns.k0(classDescriptor)) {
                return false;
            }
        } else {
            if (i10 != 13) {
                return za.k.a(gVar.a(this.f5224a), g0Var);
            }
            if ((gVar instanceof uc.b) && ((uc.b) gVar).b().size() == cVar.E().size()) {
                g0 k11 = c().k(g0Var);
                za.k.d(k11, "builtIns.getArrayElementType(expectedType)");
                uc.b bVar = (uc.b) gVar;
                k10 = kotlin.collections.r.k(bVar.b());
                if (!(k10 instanceof Collection) || !((Collection) k10).isEmpty()) {
                    Iterator it = k10.iterator();
                    while (it.hasNext()) {
                        int b10 = ((PrimitiveIterators) it).b();
                        uc.g<?> gVar2 = bVar.b().get(b10);
                        b.C0061b.c C = cVar.C(b10);
                        za.k.d(C, "value.getArrayElement(i)");
                        if (!b(gVar2, k11, C)) {
                            return false;
                        }
                    }
                }
            } else {
                throw new IllegalStateException(("Deserialized ArrayValue should have the same number of elements as the original array value: " + gVar).toString());
            }
        }
        return true;
    }

    private final KotlinBuiltIns c() {
        return this.f5224a.t();
    }

    private final ma.o<Name, uc.g<?>> d(b.C0061b c0061b, Map<Name, ? extends ValueParameterDescriptor> map, NameResolver nameResolver) {
        ValueParameterDescriptor valueParameterDescriptor = map.get(NameResolverUtil.b(nameResolver, c0061b.r()));
        if (valueParameterDescriptor == null) {
            return null;
        }
        Name b10 = NameResolverUtil.b(nameResolver, c0061b.r());
        g0 type = valueParameterDescriptor.getType();
        za.k.d(type, "parameter.type");
        b.C0061b.c s7 = c0061b.s();
        za.k.d(s7, "proto.value");
        return new ma.o<>(b10, g(type, s7, nameResolver));
    }

    private final ClassDescriptor e(ClassId classId) {
        return findClassInModule.c(this.f5224a, classId, this.f5225b);
    }

    private final uc.g<?> g(g0 g0Var, b.C0061b.c cVar, NameResolver nameResolver) {
        uc.g<?> f10 = f(g0Var, cVar, nameResolver);
        if (!b(f10, g0Var, cVar)) {
            f10 = null;
        }
        if (f10 != null) {
            return f10;
        }
        return uc.k.f18996b.a("Unexpected argument value: actual type " + cVar.N() + " != expected type " + g0Var);
    }

    public final AnnotationDescriptor a(jc.b bVar, NameResolver nameResolver) {
        Map i10;
        Object r02;
        int u7;
        int e10;
        int c10;
        za.k.e(bVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        ClassDescriptor e11 = e(NameResolverUtil.a(nameResolver, bVar.v()));
        i10 = m0.i();
        if (bVar.s() != 0 && !ErrorUtils.m(e11) && sc.e.t(e11)) {
            Collection<ClassConstructorDescriptor> p10 = e11.p();
            za.k.d(p10, "annotationClass.constructors");
            r02 = _Collections.r0(p10);
            ClassConstructorDescriptor classConstructorDescriptor = (ClassConstructorDescriptor) r02;
            if (classConstructorDescriptor != null) {
                List<ValueParameterDescriptor> l10 = classConstructorDescriptor.l();
                za.k.d(l10, "constructor.valueParameters");
                u7 = kotlin.collections.s.u(l10, 10);
                e10 = MapsJVM.e(u7);
                c10 = _Ranges.c(e10, 16);
                LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
                for (Object obj : l10) {
                    linkedHashMap.put(((ValueParameterDescriptor) obj).getName(), obj);
                }
                List<b.C0061b> t7 = bVar.t();
                za.k.d(t7, "proto.argumentList");
                ArrayList arrayList = new ArrayList();
                for (b.C0061b c0061b : t7) {
                    za.k.d(c0061b, "it");
                    ma.o<Name, uc.g<?>> d10 = d(c0061b, linkedHashMap, nameResolver);
                    if (d10 != null) {
                        arrayList.add(d10);
                    }
                }
                i10 = m0.q(arrayList);
            }
        }
        return new AnnotationDescriptorImpl(e11.x(), i10, SourceElement.f16664a);
    }

    public final uc.g<?> f(g0 g0Var, b.C0061b.c cVar, NameResolver nameResolver) {
        uc.g<?> dVar;
        int u7;
        za.k.e(g0Var, "expectedType");
        za.k.e(cVar, ThermalBaseConfig.Item.ATTR_VALUE);
        za.k.e(nameResolver, "nameResolver");
        Boolean d10 = Flags.O.d(cVar.J());
        za.k.d(d10, "IS_UNSIGNED.get(value.flags)");
        boolean booleanValue = d10.booleanValue();
        b.C0061b.c.EnumC0064c N = cVar.N();
        switch (N == null ? -1 : a.f5226a[N.ordinal()]) {
            case 1:
                byte L = (byte) cVar.L();
                if (booleanValue) {
                    dVar = new uc.w(L);
                    break;
                } else {
                    dVar = new uc.d(L);
                    break;
                }
            case 2:
                return new uc.e((char) cVar.L());
            case 3:
                short L2 = (short) cVar.L();
                if (booleanValue) {
                    dVar = new uc.z(L2);
                    break;
                } else {
                    dVar = new uc.u(L2);
                    break;
                }
            case 4:
                int L3 = (int) cVar.L();
                if (booleanValue) {
                    dVar = new uc.x(L3);
                    break;
                } else {
                    dVar = new uc.m(L3);
                    break;
                }
            case 5:
                long L4 = cVar.L();
                return booleanValue ? new uc.y(L4) : new uc.r(L4);
            case 6:
                return new uc.l(cVar.K());
            case 7:
                return new uc.i(cVar.H());
            case 8:
                return new uc.c(cVar.L() != 0);
            case 9:
                return new uc.v(nameResolver.getString(cVar.M()));
            case 10:
                return new uc.q(NameResolverUtil.a(nameResolver, cVar.F()), cVar.B());
            case 11:
                return new uc.j(NameResolverUtil.a(nameResolver, cVar.F()), NameResolverUtil.b(nameResolver, cVar.I()));
            case 12:
                jc.b A = cVar.A();
                za.k.d(A, "value.annotation");
                dVar = new uc.a(a(A, nameResolver));
                break;
            case 13:
                List<b.C0061b.c> E = cVar.E();
                za.k.d(E, "value.arrayElementList");
                u7 = kotlin.collections.s.u(E, 10);
                ArrayList arrayList = new ArrayList(u7);
                for (b.C0061b.c cVar2 : E) {
                    o0 i10 = c().i();
                    za.k.d(i10, "builtIns.anyType");
                    za.k.d(cVar2, "it");
                    arrayList.add(f(i10, cVar2, nameResolver));
                }
                return new DeserializedArrayValue(arrayList, g0Var);
            default:
                throw new IllegalStateException(("Unsupported annotation argument type: " + cVar.N() + " (expected " + g0Var + ')').toString());
        }
        return dVar;
    }
}
