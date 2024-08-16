package jb;

import ed.DeserializedClassDescriptor;
import hc.JvmPackagePartSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import jb.i;
import lc.NameResolver;
import lc.ProtoBufUtil;
import lc.TypeTable;
import mc.JvmProtoBuf;
import nc.JvmMemberSignature;
import nc.JvmProtoBufUtil;
import oc.NameUtils;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.PackageFragmentDescriptor;
import pb.PropertyDescriptor;
import qc.i;
import vb.reflectClassUtil;
import yb.JvmAbi;
import za.DefaultConstructorMarker;

/* compiled from: RuntimeTypeMapper.kt */
/* loaded from: classes2.dex */
public abstract class j {

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class a extends j {

        /* renamed from: a, reason: collision with root package name */
        private final Field f13208a;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(Field field) {
            super(null);
            za.k.e(field, "field");
            this.f13208a = field;
        }

        @Override // jb.j
        public String a() {
            StringBuilder sb2 = new StringBuilder();
            String name = this.f13208a.getName();
            za.k.d(name, "field.name");
            sb2.append(JvmAbi.b(name));
            sb2.append("()");
            Class<?> type = this.f13208a.getType();
            za.k.d(type, "field.type");
            sb2.append(reflectClassUtil.b(type));
            return sb2.toString();
        }

        public final Field b() {
            return this.f13208a;
        }
    }

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class b extends j {

        /* renamed from: a, reason: collision with root package name */
        private final Method f13209a;

        /* renamed from: b, reason: collision with root package name */
        private final Method f13210b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(Method method, Method method2) {
            super(null);
            za.k.e(method, "getterMethod");
            this.f13209a = method;
            this.f13210b = method2;
        }

        @Override // jb.j
        public String a() {
            return m0.a(this.f13209a);
        }

        public final Method b() {
            return this.f13209a;
        }

        public final Method c() {
            return this.f13210b;
        }
    }

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class c extends j {

        /* renamed from: a, reason: collision with root package name */
        private final PropertyDescriptor f13211a;

        /* renamed from: b, reason: collision with root package name */
        private final jc.n f13212b;

        /* renamed from: c, reason: collision with root package name */
        private final JvmProtoBuf.d f13213c;

        /* renamed from: d, reason: collision with root package name */
        private final NameResolver f13214d;

        /* renamed from: e, reason: collision with root package name */
        private final TypeTable f13215e;

        /* renamed from: f, reason: collision with root package name */
        private final String f13216f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public c(PropertyDescriptor propertyDescriptor, jc.n nVar, JvmProtoBuf.d dVar, NameResolver nameResolver, TypeTable typeTable) {
            super(null);
            String str;
            za.k.e(propertyDescriptor, "descriptor");
            za.k.e(nVar, "proto");
            za.k.e(dVar, "signature");
            za.k.e(nameResolver, "nameResolver");
            za.k.e(typeTable, "typeTable");
            this.f13211a = propertyDescriptor;
            this.f13212b = nVar;
            this.f13213c = dVar;
            this.f13214d = nameResolver;
            this.f13215e = typeTable;
            if (dVar.B()) {
                str = nameResolver.getString(dVar.w().s()) + nameResolver.getString(dVar.w().r());
            } else {
                JvmMemberSignature.a d10 = JvmProtoBufUtil.d(JvmProtoBufUtil.f16006a, nVar, nameResolver, typeTable, false, 8, null);
                if (d10 != null) {
                    String d11 = d10.d();
                    str = JvmAbi.b(d11) + c() + "()" + d10.e();
                } else {
                    throw new KotlinReflectionInternalError("No field signature for property: " + propertyDescriptor);
                }
            }
            this.f13216f = str;
        }

        private final String c() {
            String str;
            DeclarationDescriptor b10 = this.f13211a.b();
            za.k.d(b10, "descriptor.containingDeclaration");
            if (za.k.a(this.f13211a.g(), DescriptorVisibilities.f16732d) && (b10 instanceof DeserializedClassDescriptor)) {
                jc.c k12 = ((DeserializedClassDescriptor) b10).k1();
                i.f<jc.c, Integer> fVar = JvmProtoBuf.f15372i;
                za.k.d(fVar, "classModuleName");
                Integer num = (Integer) ProtoBufUtil.a(k12, fVar);
                if (num == null || (str = this.f13214d.getString(num.intValue())) == null) {
                    str = "main";
                }
                return '$' + NameUtils.a(str);
            }
            if (!za.k.a(this.f13211a.g(), DescriptorVisibilities.f16729a) || !(b10 instanceof PackageFragmentDescriptor)) {
                return "";
            }
            PropertyDescriptor propertyDescriptor = this.f13211a;
            za.k.c(propertyDescriptor, "null cannot be cast to non-null type org.jetbrains.kotlin.serialization.deserialization.descriptors.DeserializedPropertyDescriptor");
            ed.f j02 = ((ed.j) propertyDescriptor).j0();
            if (!(j02 instanceof JvmPackagePartSource)) {
                return "";
            }
            JvmPackagePartSource jvmPackagePartSource = (JvmPackagePartSource) j02;
            if (jvmPackagePartSource.f() == null) {
                return "";
            }
            return '$' + jvmPackagePartSource.h().b();
        }

        @Override // jb.j
        public String a() {
            return this.f13216f;
        }

        public final PropertyDescriptor b() {
            return this.f13211a;
        }

        public final NameResolver d() {
            return this.f13214d;
        }

        public final jc.n e() {
            return this.f13212b;
        }

        public final JvmProtoBuf.d f() {
            return this.f13213c;
        }

        public final TypeTable g() {
            return this.f13215e;
        }
    }

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class d extends j {

        /* renamed from: a, reason: collision with root package name */
        private final i.e f13217a;

        /* renamed from: b, reason: collision with root package name */
        private final i.e f13218b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public d(i.e eVar, i.e eVar2) {
            super(null);
            za.k.e(eVar, "getterSignature");
            this.f13217a = eVar;
            this.f13218b = eVar2;
        }

        @Override // jb.j
        public String a() {
            return this.f13217a.a();
        }

        public final i.e b() {
            return this.f13217a;
        }

        public final i.e c() {
            return this.f13218b;
        }
    }

    private j() {
    }

    public /* synthetic */ j(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract String a();
}
