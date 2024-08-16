package uc;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gd.g0;
import gd.o0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import mb.PrimitiveType;
import pb.ModuleDescriptor;
import za.Lambda;

/* compiled from: ConstantValueFactory.kt */
/* renamed from: uc.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class ConstantValueFactory {

    /* renamed from: a, reason: collision with root package name */
    public static final ConstantValueFactory f18991a = new ConstantValueFactory();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ConstantValueFactory.kt */
    /* renamed from: uc.h$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<ModuleDescriptor, g0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ g0 f18992e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(g0 g0Var) {
            super(1);
            this.f18992e = g0Var;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(ModuleDescriptor moduleDescriptor) {
            za.k.e(moduleDescriptor, "it");
            return this.f18992e;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ConstantValueFactory.kt */
    /* renamed from: uc.h$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l<ModuleDescriptor, g0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ PrimitiveType f18993e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(PrimitiveType primitiveType) {
            super(1);
            this.f18993e = primitiveType;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(ModuleDescriptor moduleDescriptor) {
            za.k.e(moduleDescriptor, "module");
            o0 O = moduleDescriptor.t().O(this.f18993e);
            za.k.d(O, "module.builtIns.getPrimi…KotlinType(componentType)");
            return O;
        }
    }

    private ConstantValueFactory() {
    }

    private final uc.b b(List<?> list, PrimitiveType primitiveType) {
        List z02;
        z02 = _Collections.z0(list);
        ArrayList arrayList = new ArrayList();
        Iterator it = z02.iterator();
        while (it.hasNext()) {
            g<?> c10 = c(it.next());
            if (c10 != null) {
                arrayList.add(c10);
            }
        }
        return new uc.b(arrayList, new b(primitiveType));
    }

    public final uc.b a(List<? extends g<?>> list, g0 g0Var) {
        za.k.e(list, ThermalBaseConfig.Item.ATTR_VALUE);
        za.k.e(g0Var, "type");
        return new uc.b(list, new a(g0Var));
    }

    public final g<?> c(Object obj) {
        List<?> h02;
        List<?> b02;
        List<?> c02;
        List<?> a02;
        List<?> e02;
        List<?> d02;
        List<?> g02;
        List<?> Z;
        if (obj instanceof Byte) {
            return new d(((Number) obj).byteValue());
        }
        if (obj instanceof Short) {
            return new u(((Number) obj).shortValue());
        }
        if (obj instanceof Integer) {
            return new m(((Number) obj).intValue());
        }
        if (obj instanceof Long) {
            return new r(((Number) obj).longValue());
        }
        if (obj instanceof Character) {
            return new e(((Character) obj).charValue());
        }
        if (obj instanceof Float) {
            return new l(((Number) obj).floatValue());
        }
        if (obj instanceof Double) {
            return new i(((Number) obj).doubleValue());
        }
        if (obj instanceof Boolean) {
            return new c(((Boolean) obj).booleanValue());
        }
        if (obj instanceof String) {
            return new v((String) obj);
        }
        if (obj instanceof byte[]) {
            Z = _Arrays.Z((byte[]) obj);
            return b(Z, PrimitiveType.BYTE);
        }
        if (obj instanceof short[]) {
            g02 = _Arrays.g0((short[]) obj);
            return b(g02, PrimitiveType.SHORT);
        }
        if (obj instanceof int[]) {
            d02 = _Arrays.d0((int[]) obj);
            return b(d02, PrimitiveType.INT);
        }
        if (obj instanceof long[]) {
            e02 = _Arrays.e0((long[]) obj);
            return b(e02, PrimitiveType.LONG);
        }
        if (obj instanceof char[]) {
            a02 = _Arrays.a0((char[]) obj);
            return b(a02, PrimitiveType.CHAR);
        }
        if (obj instanceof float[]) {
            c02 = _Arrays.c0((float[]) obj);
            return b(c02, PrimitiveType.FLOAT);
        }
        if (obj instanceof double[]) {
            b02 = _Arrays.b0((double[]) obj);
            return b(b02, PrimitiveType.DOUBLE);
        }
        if (obj instanceof boolean[]) {
            h02 = _Arrays.h0((boolean[]) obj);
            return b(h02, PrimitiveType.BOOLEAN);
        }
        if (obj == null) {
            return new s();
        }
        return null;
    }
}
