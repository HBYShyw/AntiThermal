package androidx.window.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import com.oplus.backup.sdk.common.utils.Constants;
import gb.KClass;
import gb.e;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import kotlin.Metadata;
import ma.Unit;
import ya.l;
import za.k;

/* compiled from: ConsumerAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0001\u0018\u00002\u00020\u0001:\u0002\u001c\u001dB\u000f\u0012\u0006\u0010\u0019\u001a\u00020\u0017¢\u0006\u0004\b\u001a\u0010\u001bJ\f\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0002H\u0002J4\u0010\n\u001a\u00020\u0001\"\b\b\u0000\u0010\u0004*\u00020\u00012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u00052\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\b0\u0007H\u0002J\u0015\u0010\u000b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0002H\u0000¢\u0006\u0004\b\u000b\u0010\fJB\u0010\u0010\u001a\u00020\b\"\b\b\u0000\u0010\u0004*\u00020\u00012\u0006\u0010\r\u001a\u00020\u00012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u00052\u0006\u0010\u000f\u001a\u00020\u000e2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\b0\u0007JT\u0010\u0016\u001a\u00020\u0015\"\b\b\u0000\u0010\u0004*\u00020\u00012\u0006\u0010\r\u001a\u00020\u00012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u00052\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u00132\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\b0\u0007H\u0007R\u0014\u0010\u0019\u001a\u00020\u00178\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0010\u0010\u0018¨\u0006\u001e"}, d2 = {"Landroidx/window/core/ConsumerAdapter;", "", "Ljava/lang/Class;", "e", "T", "Lgb/d;", "clazz", "Lkotlin/Function1;", "Lma/f0;", "consumer", "b", "c", "()Ljava/lang/Class;", "obj", "", "methodName", "a", "addMethodName", "removeMethodName", "Landroid/app/Activity;", "activity", "Landroidx/window/core/ConsumerAdapter$Subscription;", "d", "Ljava/lang/ClassLoader;", "Ljava/lang/ClassLoader;", "loader", "<init>", "(Ljava/lang/ClassLoader;)V", "ConsumerHandler", "Subscription", "window_release"}, k = 1, mv = {1, 6, 0})
@SuppressLint({"BanUncheckedReflection"})
/* loaded from: classes.dex */
public final class ConsumerAdapter {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final ClassLoader loader;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ConsumerAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0003B)\u0012\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u0016\u0012\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00130\u0018¢\u0006\u0004\b\u001a\u0010\u001bJ%\u0010\b\u001a\u00020\u0007*\u00020\u00042\u0010\u0010\u0006\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0005H\u0002¢\u0006\u0004\b\b\u0010\tJ%\u0010\n\u001a\u00020\u0007*\u00020\u00042\u0010\u0010\u0006\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0005H\u0002¢\u0006\u0004\b\n\u0010\tJ%\u0010\u000b\u001a\u00020\u0007*\u00020\u00042\u0010\u0010\u0006\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0005H\u0002¢\u0006\u0004\b\u000b\u0010\tJ%\u0010\f\u001a\u00020\u0007*\u00020\u00042\u0010\u0010\u0006\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0005H\u0002¢\u0006\u0004\b\f\u0010\tJ2\u0010\u0010\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u00042\u0010\u0010\u000f\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0005H\u0096\u0002¢\u0006\u0004\b\u0010\u0010\u0011J\u0015\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00028\u0000¢\u0006\u0004\b\u0014\u0010\u0015¨\u0006\u001c"}, d2 = {"Landroidx/window/core/ConsumerAdapter$ConsumerHandler;", "", "T", "Ljava/lang/reflect/InvocationHandler;", "Ljava/lang/reflect/Method;", "", Constants.MessagerConstants.ARGS_KEY, "", "c", "(Ljava/lang/reflect/Method;[Ljava/lang/Object;)Z", "d", "b", "e", "obj", Constants.MessagerConstants.METHOD_KEY, "parameters", "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", "parameter", "Lma/f0;", "a", "(Ljava/lang/Object;)V", "Lgb/d;", "clazz", "Lkotlin/Function1;", "consumer", "<init>", "(Lgb/d;Lya/l;)V", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class ConsumerHandler<T> implements InvocationHandler {

        /* renamed from: a, reason: collision with root package name */
        private final KClass<T> f4309a;

        /* renamed from: b, reason: collision with root package name */
        private final l<T, Unit> f4310b;

        /* JADX WARN: Multi-variable type inference failed */
        public ConsumerHandler(KClass<T> kClass, l<? super T, Unit> lVar) {
            k.e(kClass, "clazz");
            k.e(lVar, "consumer");
            this.f4309a = kClass;
            this.f4310b = lVar;
        }

        private final boolean b(Method method, Object[] objArr) {
            if (k.a(method.getName(), "accept")) {
                if (objArr != null && objArr.length == 1) {
                    return true;
                }
            }
            return false;
        }

        private final boolean c(Method method, Object[] objArr) {
            if (k.a(method.getName(), "equals") && method.getReturnType().equals(Boolean.TYPE)) {
                if (objArr != null && objArr.length == 1) {
                    return true;
                }
            }
            return false;
        }

        private final boolean d(Method method, Object[] objArr) {
            return k.a(method.getName(), "hashCode") && method.getReturnType().equals(Integer.TYPE) && objArr == null;
        }

        private final boolean e(Method method, Object[] objArr) {
            return k.a(method.getName(), "toString") && method.getReturnType().equals(String.class) && objArr == null;
        }

        public final void a(T parameter) {
            k.e(parameter, "parameter");
            this.f4310b.invoke(parameter);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] parameters) {
            k.e(obj, "obj");
            k.e(method, Constants.MessagerConstants.METHOD_KEY);
            if (b(method, parameters)) {
                a(e.a(this.f4309a, parameters != null ? parameters[0] : null));
                return Unit.f15173a;
            }
            if (c(method, parameters)) {
                return Boolean.valueOf(obj == (parameters != null ? parameters[0] : null));
            }
            if (d(method, parameters)) {
                return Integer.valueOf(this.f4310b.hashCode());
            }
            if (e(method, parameters)) {
                return this.f4310b.toString();
            }
            throw new UnsupportedOperationException("Unexpected method call object:" + obj + ", method: " + method + ", args: " + parameters);
        }
    }

    /* compiled from: ConsumerAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b`\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0004"}, d2 = {"Landroidx/window/core/ConsumerAdapter$Subscription;", "", "Lma/f0;", "a", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public interface Subscription {
        void a();
    }

    public ConsumerAdapter(ClassLoader classLoader) {
        k.e(classLoader, "loader");
        this.loader = classLoader;
    }

    private final <T> Object b(KClass<T> kClass, l<? super T, Unit> lVar) {
        Object newProxyInstance = Proxy.newProxyInstance(this.loader, new Class[]{e()}, new ConsumerHandler(kClass, lVar));
        k.d(newProxyInstance, "newProxyInstance(loader,…onsumerClass()), handler)");
        return newProxyInstance;
    }

    private final Class<?> e() {
        Class<?> loadClass = this.loader.loadClass("java.util.function.Consumer");
        k.d(loadClass, "loader.loadClass(\"java.util.function.Consumer\")");
        return loadClass;
    }

    public final <T> void a(Object obj, KClass<T> kClass, String str, l<? super T, Unit> lVar) {
        k.e(obj, "obj");
        k.e(kClass, "clazz");
        k.e(str, "methodName");
        k.e(lVar, "consumer");
        obj.getClass().getMethod(str, e()).invoke(obj, b(kClass, lVar));
    }

    public final Class<?> c() {
        try {
            return e();
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }

    public final <T> Subscription d(final Object obj, KClass<T> kClass, String str, String str2, Activity activity, l<? super T, Unit> lVar) {
        k.e(obj, "obj");
        k.e(kClass, "clazz");
        k.e(str, "addMethodName");
        k.e(str2, "removeMethodName");
        k.e(activity, "activity");
        k.e(lVar, "consumer");
        final Object b10 = b(kClass, lVar);
        obj.getClass().getMethod(str, Activity.class, e()).invoke(obj, activity, b10);
        final Method method = obj.getClass().getMethod(str2, e());
        return new Subscription() { // from class: androidx.window.core.ConsumerAdapter$createSubscription$1
            @Override // androidx.window.core.ConsumerAdapter.Subscription
            public void a() {
                method.invoke(obj, b10);
            }
        };
    }
}
