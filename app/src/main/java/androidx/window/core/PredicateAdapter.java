package androidx.window.core;

import android.annotation.SuppressLint;
import android.util.Pair;
import com.oplus.backup.sdk.common.utils.Constants;
import gb.KClass;
import gb.e;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import kotlin.Metadata;
import ya.l;
import ya.p;
import za.k;

/* compiled from: PredicateAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0001\u0018\u00002\u00020\u0001:\u0003\b\t\nB\u000f\u0012\u0006\u0010\u0005\u001a\u00020\u0002¢\u0006\u0004\b\u0006\u0010\u0007R\u0014\u0010\u0005\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u000b"}, d2 = {"Landroidx/window/core/PredicateAdapter;", "", "Ljava/lang/ClassLoader;", "a", "Ljava/lang/ClassLoader;", "loader", "<init>", "(Ljava/lang/ClassLoader;)V", "BaseHandler", "PairPredicateStubHandler", "PredicateStubHandler", "window_release"}, k = 1, mv = {1, 6, 0})
@SuppressLint({"BanUncheckedReflection"})
/* loaded from: classes.dex */
public final class PredicateAdapter {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final ClassLoader loader;

    /* compiled from: PredicateAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\t\b\"\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0003J2\u0010\t\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00052\u0010\u0010\b\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0007H\u0096\u0002¢\u0006\u0004\b\t\u0010\nJ\u001f\u0010\r\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00028\u0000H&¢\u0006\u0004\b\r\u0010\u000eJ%\u0010\u0010\u001a\u00020\f*\u00020\u00052\u0010\u0010\u000f\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0007H\u0004¢\u0006\u0004\b\u0010\u0010\u0011J%\u0010\u0012\u001a\u00020\f*\u00020\u00052\u0010\u0010\u000f\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0007H\u0004¢\u0006\u0004\b\u0012\u0010\u0011J%\u0010\u0013\u001a\u00020\f*\u00020\u00052\u0010\u0010\u000f\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0007H\u0004¢\u0006\u0004\b\u0013\u0010\u0011J%\u0010\u0014\u001a\u00020\f*\u00020\u00052\u0010\u0010\u000f\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u0007H\u0004¢\u0006\u0004\b\u0014\u0010\u0011¨\u0006\u0015"}, d2 = {"Landroidx/window/core/PredicateAdapter$BaseHandler;", "", "T", "Ljava/lang/reflect/InvocationHandler;", "obj", "Ljava/lang/reflect/Method;", Constants.MessagerConstants.METHOD_KEY, "", "parameters", "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", "parameter", "", "a", "(Ljava/lang/Object;Ljava/lang/Object;)Z", Constants.MessagerConstants.ARGS_KEY, "b", "(Ljava/lang/reflect/Method;[Ljava/lang/Object;)Z", "c", "d", "e", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    private static abstract class BaseHandler<T> implements InvocationHandler {

        /* renamed from: a, reason: collision with root package name */
        private final KClass<T> f4322a;

        public abstract boolean a(Object obj, T parameter);

        protected final boolean b(Method method, Object[] objArr) {
            k.e(method, "<this>");
            if (k.a(method.getName(), "equals") && method.getReturnType().equals(Boolean.TYPE)) {
                if (objArr != null && objArr.length == 1) {
                    return true;
                }
            }
            return false;
        }

        protected final boolean c(Method method, Object[] objArr) {
            k.e(method, "<this>");
            return k.a(method.getName(), "hashCode") && method.getReturnType().equals(Integer.TYPE) && objArr == null;
        }

        protected final boolean d(Method method, Object[] objArr) {
            k.e(method, "<this>");
            if (k.a(method.getName(), "test") && method.getReturnType().equals(Boolean.TYPE)) {
                if (objArr != null && objArr.length == 1) {
                    return true;
                }
            }
            return false;
        }

        protected final boolean e(Method method, Object[] objArr) {
            k.e(method, "<this>");
            return k.a(method.getName(), "toString") && method.getReturnType().equals(String.class) && objArr == null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] parameters) {
            k.e(obj, "obj");
            k.e(method, Constants.MessagerConstants.METHOD_KEY);
            if (d(method, parameters)) {
                return Boolean.valueOf(a(obj, e.a(this.f4322a, parameters != null ? parameters[0] : null)));
            }
            if (b(method, parameters)) {
                Object obj2 = parameters != null ? parameters[0] : null;
                k.b(obj2);
                return Boolean.valueOf(obj == obj2);
            }
            if (c(method, parameters)) {
                return Integer.valueOf(hashCode());
            }
            if (e(method, parameters)) {
                return toString();
            }
            throw new UnsupportedOperationException("Unexpected method call object:" + obj + ", method: " + method + ", args: " + parameters);
        }
    }

    /* compiled from: PredicateAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u0001*\b\b\u0001\u0010\u0003*\u00020\u00012\u0010\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u00050\u0004J \u0010\t\u001a\u00020\b2\u0006\u0010\u0006\u001a\u00020\u00012\u000e\u0010\u0007\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0005H\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016J\b\u0010\r\u001a\u00020\fH\u0016¨\u0006\u000e"}, d2 = {"Landroidx/window/core/PredicateAdapter$PairPredicateStubHandler;", "", "T", "U", "Landroidx/window/core/PredicateAdapter$BaseHandler;", "Landroid/util/Pair;", "obj", "parameter", "", "f", "", "hashCode", "", "toString", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    private static final class PairPredicateStubHandler<T, U> extends BaseHandler<Pair<?, ?>> {

        /* renamed from: b, reason: collision with root package name */
        private final KClass<T> f4323b;

        /* renamed from: c, reason: collision with root package name */
        private final KClass<U> f4324c;

        /* renamed from: d, reason: collision with root package name */
        private final p<T, U, Boolean> f4325d;

        @Override // androidx.window.core.PredicateAdapter.BaseHandler
        /* renamed from: f, reason: merged with bridge method [inline-methods] */
        public boolean a(Object obj, Pair<?, ?> parameter) {
            k.e(obj, "obj");
            k.e(parameter, "parameter");
            return ((Boolean) this.f4325d.invoke(e.a(this.f4323b, parameter.first), e.a(this.f4324c, parameter.second))).booleanValue();
        }

        public int hashCode() {
            return this.f4325d.hashCode();
        }

        public String toString() {
            return this.f4325d.toString();
        }
    }

    /* compiled from: PredicateAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\b\u0012\u0004\u0012\u00028\u00000\u0003J\u001f\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00028\u0000H\u0016¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\n\u001a\u00020\tH\u0016J\b\u0010\f\u001a\u00020\u000bH\u0016¨\u0006\r"}, d2 = {"Landroidx/window/core/PredicateAdapter$PredicateStubHandler;", "", "T", "Landroidx/window/core/PredicateAdapter$BaseHandler;", "obj", "parameter", "", "a", "(Ljava/lang/Object;Ljava/lang/Object;)Z", "", "hashCode", "", "toString", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    private static final class PredicateStubHandler<T> extends BaseHandler<T> {

        /* renamed from: b, reason: collision with root package name */
        private final l<T, Boolean> f4326b;

        @Override // androidx.window.core.PredicateAdapter.BaseHandler
        public boolean a(Object obj, T parameter) {
            k.e(obj, "obj");
            k.e(parameter, "parameter");
            return this.f4326b.invoke(parameter).booleanValue();
        }

        public int hashCode() {
            return this.f4326b.hashCode();
        }

        public String toString() {
            return this.f4326b.toString();
        }
    }

    public PredicateAdapter(ClassLoader classLoader) {
        k.e(classLoader, "loader");
        this.loader = classLoader;
    }
}
