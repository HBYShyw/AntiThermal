package kb;

import com.oplus.backup.sdk.common.utils.Constants;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import kb.CallerImpl;
import kb.e;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._ArraysJvm;
import kotlin.collections.r;
import za.DefaultConstructorMarker;

/* compiled from: InternalUnderlyingValOfInlineClass.kt */
/* renamed from: kb.j, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class InternalUnderlyingValOfInlineClass implements e<Method> {

    /* renamed from: a, reason: collision with root package name */
    private final Method f14274a;

    /* renamed from: b, reason: collision with root package name */
    private final List<Type> f14275b;

    /* renamed from: c, reason: collision with root package name */
    private final Type f14276c;

    /* compiled from: InternalUnderlyingValOfInlineClass.kt */
    /* renamed from: kb.j$a */
    /* loaded from: classes2.dex */
    public static final class a extends InternalUnderlyingValOfInlineClass implements d {

        /* renamed from: d, reason: collision with root package name */
        private final Object f14277d;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public a(Method method, Object obj) {
            super(method, r0, null);
            List j10;
            za.k.e(method, "unboxMethod");
            j10 = r.j();
            this.f14277d = obj;
        }

        @Override // kb.e
        public Object d(Object[] objArr) {
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            e(objArr);
            return c(this.f14277d, objArr);
        }
    }

    /* compiled from: InternalUnderlyingValOfInlineClass.kt */
    /* renamed from: kb.j$b */
    /* loaded from: classes2.dex */
    public static final class b extends InternalUnderlyingValOfInlineClass {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b(Method method) {
            super(method, r0, null);
            List e10;
            za.k.e(method, "unboxMethod");
            e10 = CollectionsJVM.e(method.getDeclaringClass());
        }

        @Override // kb.e
        public Object d(Object[] objArr) {
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            e(objArr);
            Object obj = objArr[0];
            CallerImpl.d dVar = CallerImpl.f14255e;
            return c(obj, objArr.length <= 1 ? new Object[0] : _ArraysJvm.k(objArr, 1, objArr.length));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private InternalUnderlyingValOfInlineClass(Method method, List<? extends Type> list) {
        this.f14274a = method;
        this.f14275b = list;
        Class<?> returnType = method.getReturnType();
        za.k.d(returnType, "unboxMethod.returnType");
        this.f14276c = returnType;
    }

    public /* synthetic */ InternalUnderlyingValOfInlineClass(Method method, List list, DefaultConstructorMarker defaultConstructorMarker) {
        this(method, list);
    }

    @Override // kb.e
    public final List<Type> a() {
        return this.f14275b;
    }

    protected final Object c(Object obj, Object[] objArr) {
        za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
        return this.f14274a.invoke(obj, Arrays.copyOf(objArr, objArr.length));
    }

    public void e(Object[] objArr) {
        e.a.a(this, objArr);
    }

    @Override // kb.e
    public final Type f() {
        return this.f14276c;
    }

    @Override // kb.e
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public final Method b() {
        return null;
    }
}
