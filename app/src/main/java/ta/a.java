package ta;

import db.Random;
import java.lang.reflect.Method;
import kotlin.collections.j;
import za.k;

/* compiled from: PlatformImplementations.kt */
/* loaded from: classes2.dex */
public class a {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PlatformImplementations.kt */
    /* renamed from: ta.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0104a {

        /* renamed from: a, reason: collision with root package name */
        public static final C0104a f18700a = new C0104a();

        /* renamed from: b, reason: collision with root package name */
        public static final Method f18701b;

        /* renamed from: c, reason: collision with root package name */
        public static final Method f18702c;

        /* JADX WARN: Removed duplicated region for block: B:10:0x003f A[LOOP:0: B:2:0x0015->B:10:0x003f, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:11:0x0043 A[EDGE_INSN: B:11:0x0043->B:12:0x0043 BREAK  A[LOOP:0: B:2:0x0015->B:10:0x003f], SYNTHETIC] */
        static {
            Method method;
            Method method2;
            boolean z10;
            Method[] methods = Throwable.class.getMethods();
            k.d(methods, "throwableMethods");
            int length = methods.length;
            int i10 = 0;
            int i11 = 0;
            while (true) {
                method = null;
                if (i11 >= length) {
                    method2 = null;
                    break;
                }
                method2 = methods[i11];
                if (k.a(method2.getName(), "addSuppressed")) {
                    Class<?>[] parameterTypes = method2.getParameterTypes();
                    k.d(parameterTypes, "it.parameterTypes");
                    if (k.a(j.U(parameterTypes), Throwable.class)) {
                        z10 = true;
                        if (!z10) {
                            break;
                        } else {
                            i11++;
                        }
                    }
                }
                z10 = false;
                if (!z10) {
                }
            }
            f18701b = method2;
            int length2 = methods.length;
            while (true) {
                if (i10 >= length2) {
                    break;
                }
                Method method3 = methods[i10];
                if (k.a(method3.getName(), "getSuppressed")) {
                    method = method3;
                    break;
                }
                i10++;
            }
            f18702c = method;
        }

        private C0104a() {
        }
    }

    public void a(Throwable th, Throwable th2) {
        k.e(th, "cause");
        k.e(th2, "exception");
        Method method = C0104a.f18701b;
        if (method != null) {
            method.invoke(th, th2);
        }
    }

    public Random b() {
        return new db.b();
    }
}
