package kb;

import com.oplus.backup.sdk.common.utils.Constants;
import fb.PrimitiveRanges;
import fb._Ranges;
import gd.g0;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import jb.KotlinReflectionInternalError;
import jb.o0;
import kb.CallerImpl;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.ValueParameterDescriptor;
import sc.inlineClassesUtils;

/* compiled from: InlineClassAwareCaller.kt */
/* loaded from: classes2.dex */
public final class h<M extends Member> implements e<M> {

    /* renamed from: a, reason: collision with root package name */
    private final e<M> f14268a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f14269b;

    /* renamed from: c, reason: collision with root package name */
    private final a f14270c;

    /* compiled from: InlineClassAwareCaller.kt */
    /* loaded from: classes2.dex */
    private static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final PrimitiveRanges f14271a;

        /* renamed from: b, reason: collision with root package name */
        private final Method[] f14272b;

        /* renamed from: c, reason: collision with root package name */
        private final Method f14273c;

        public a(PrimitiveRanges primitiveRanges, Method[] methodArr, Method method) {
            za.k.e(primitiveRanges, "argumentRange");
            za.k.e(methodArr, "unbox");
            this.f14271a = primitiveRanges;
            this.f14272b = methodArr;
            this.f14273c = method;
        }

        public final PrimitiveRanges a() {
            return this.f14271a;
        }

        public final Method[] b() {
            return this.f14272b;
        }

        public final Method c() {
            return this.f14273c;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x004a, code lost:
    
        if ((r12 instanceof kb.d) != false) goto L26;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public h(CallableMemberDescriptor callableMemberDescriptor, e<? extends M> eVar, boolean z10) {
        PrimitiveRanges k10;
        a aVar;
        Class<?> h10;
        za.k.e(callableMemberDescriptor, "descriptor");
        za.k.e(eVar, "caller");
        this.f14268a = eVar;
        this.f14269b = z10;
        g0 f10 = callableMemberDescriptor.f();
        za.k.b(f10);
        Class<?> h11 = i.h(f10);
        Method d10 = h11 != null ? i.d(h11, callableMemberDescriptor) : null;
        if (inlineClassesUtils.a(callableMemberDescriptor)) {
            aVar = new a(PrimitiveRanges.f11414i.a(), new Method[0], d10);
        } else {
            int i10 = -1;
            if (!(eVar instanceof CallerImpl.h.c)) {
                if (!(callableMemberDescriptor instanceof ConstructorDescriptor)) {
                    if (callableMemberDescriptor.m0() != null && !(eVar instanceof d)) {
                        DeclarationDescriptor b10 = callableMemberDescriptor.b();
                        za.k.d(b10, "descriptor.containingDeclaration");
                        if (!inlineClassesUtils.b(b10)) {
                            i10 = 1;
                        }
                    }
                    i10 = 0;
                }
            }
            ArrayList arrayList = new ArrayList();
            ReceiverParameterDescriptor r02 = callableMemberDescriptor.r0();
            g0 type = r02 != null ? r02.getType() : null;
            if (type != null) {
                arrayList.add(type);
            } else if (callableMemberDescriptor instanceof ConstructorDescriptor) {
                ClassDescriptor K = ((ConstructorDescriptor) callableMemberDescriptor).K();
                za.k.d(K, "descriptor.constructedClass");
                if (K.r()) {
                    DeclarationDescriptor b11 = K.b();
                    za.k.c(b11, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                    arrayList.add(((ClassDescriptor) b11).x());
                }
            } else {
                DeclarationDescriptor b12 = callableMemberDescriptor.b();
                za.k.d(b12, "descriptor.containingDeclaration");
                if ((b12 instanceof ClassDescriptor) && inlineClassesUtils.b(b12)) {
                    arrayList.add(((ClassDescriptor) b12).x());
                }
            }
            List<ValueParameterDescriptor> l10 = callableMemberDescriptor.l();
            za.k.d(l10, "descriptor.valueParameters");
            Iterator<T> it = l10.iterator();
            while (it.hasNext()) {
                arrayList.add(((ValueParameterDescriptor) it.next()).getType());
            }
            int size = arrayList.size() + i10 + (this.f14269b ? (((arrayList.size() + 32) - 1) / 32) + 1 : 0) + (((callableMemberDescriptor instanceof FunctionDescriptor) && ((FunctionDescriptor) callableMemberDescriptor).C0()) ? 1 : 0);
            if (g.a(this) == size) {
                k10 = _Ranges.k(Math.max(i10, 0), arrayList.size() + i10);
                Method[] methodArr = new Method[size];
                int i11 = 0;
                while (i11 < size) {
                    methodArr[i11] = (!(i11 <= k10.e() && k10.d() <= i11) || (h10 = i.h((g0) arrayList.get(i11 - i10))) == null) ? null : i.f(h10, callableMemberDescriptor);
                    i11++;
                }
                aVar = new a(k10, methodArr, d10);
            } else {
                throw new KotlinReflectionInternalError("Inconsistent number of parameters in the descriptor and Java reflection object: " + g.a(this) + " != " + size + "\nCalling: " + callableMemberDescriptor + "\nParameter types: " + a() + ")\nDefault: " + this.f14269b);
            }
        }
        this.f14270c = aVar;
    }

    @Override // kb.e
    public List<Type> a() {
        return this.f14268a.a();
    }

    @Override // kb.e
    public M b() {
        return this.f14268a.b();
    }

    @Override // kb.e
    public Object d(Object[] objArr) {
        Object invoke;
        za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
        a aVar = this.f14270c;
        PrimitiveRanges a10 = aVar.a();
        Method[] b10 = aVar.b();
        Method c10 = aVar.c();
        Object[] copyOf = Arrays.copyOf(objArr, objArr.length);
        za.k.d(copyOf, "copyOf(this, size)");
        int d10 = a10.d();
        int e10 = a10.e();
        if (d10 <= e10) {
            while (true) {
                Method method = b10[d10];
                Object obj = objArr[d10];
                if (method != null) {
                    if (obj != null) {
                        obj = method.invoke(obj, new Object[0]);
                    } else {
                        Class<?> returnType = method.getReturnType();
                        za.k.d(returnType, "method.returnType");
                        obj = o0.g(returnType);
                    }
                }
                copyOf[d10] = obj;
                if (d10 == e10) {
                    break;
                }
                d10++;
            }
        }
        Object d11 = this.f14268a.d(copyOf);
        return (c10 == null || (invoke = c10.invoke(null, d11)) == null) ? d11 : invoke;
    }

    @Override // kb.e
    public Type f() {
        return this.f14268a.f();
    }
}
