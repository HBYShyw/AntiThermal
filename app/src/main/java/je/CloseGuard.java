package je;

import java.lang.reflect.Method;
import kotlin.Metadata;
import za.DefaultConstructorMarker;

/* compiled from: CloseGuard.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u0001:\u0001\u0004B%\u0012\b\u0010\t\u001a\u0004\u0018\u00010\b\u0012\b\u0010\n\u001a\u0004\u0018\u00010\b\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\b¢\u0006\u0004\b\f\u0010\rJ\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0003\u001a\u00020\u0002J\u0010\u0010\u0007\u001a\u00020\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001¨\u0006\u000e"}, d2 = {"Lje/h;", "", "", "closer", "a", "closeGuardInstance", "", "b", "Ljava/lang/reflect/Method;", "getMethod", "openMethod", "warnIfOpenMethod", "<init>", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: je.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class CloseGuard {

    /* renamed from: d, reason: collision with root package name */
    public static final a f13924d = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final Method f13925a;

    /* renamed from: b, reason: collision with root package name */
    private final Method f13926b;

    /* renamed from: c, reason: collision with root package name */
    private final Method f13927c;

    /* compiled from: CloseGuard.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u0003\u001a\u00020\u0002¨\u0006\u0006"}, d2 = {"Lje/h$a;", "", "Lje/h;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: je.h$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final CloseGuard a() {
            Method method;
            Method method2;
            Method method3 = null;
            try {
                Class<?> cls = Class.forName("dalvik.system.CloseGuard");
                Method method4 = cls.getMethod("get", new Class[0]);
                method2 = cls.getMethod("open", String.class);
                method = cls.getMethod("warnIfOpen", new Class[0]);
                method3 = method4;
            } catch (Exception unused) {
                method = null;
                method2 = null;
            }
            return new CloseGuard(method3, method2, method);
        }
    }

    public CloseGuard(Method method, Method method2, Method method3) {
        this.f13925a = method;
        this.f13926b = method2;
        this.f13927c = method3;
    }

    public final Object a(String closer) {
        za.k.e(closer, "closer");
        Method method = this.f13925a;
        if (method != null) {
            try {
                Object invoke = method.invoke(null, new Object[0]);
                Method method2 = this.f13926b;
                za.k.b(method2);
                method2.invoke(invoke, closer);
                return invoke;
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public final boolean b(Object closeGuardInstance) {
        if (closeGuardInstance == null) {
            return false;
        }
        try {
            Method method = this.f13927c;
            za.k.b(method);
            method.invoke(closeGuardInstance, new Object[0]);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
