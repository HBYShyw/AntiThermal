package ua;

import za.k;

/* compiled from: JDK7PlatformImplementations.kt */
/* renamed from: ua.a, reason: use source file name */
/* loaded from: classes2.dex */
public class JDK7PlatformImplementations extends ta.a {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JDK7PlatformImplementations.kt */
    /* renamed from: ua.a$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        public static final a f18966a = new a();

        /* renamed from: b, reason: collision with root package name */
        public static final Integer f18967b;

        /* JADX WARN: Removed duplicated region for block: B:7:0x0022  */
        static {
            Integer num;
            Object obj;
            Integer num2 = null;
            try {
                obj = Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
            } catch (Throwable unused) {
            }
            if (obj instanceof Integer) {
                num = (Integer) obj;
                if (num != null) {
                    if (num.intValue() > 0) {
                        num2 = num;
                    }
                }
                f18967b = num2;
            }
            num = null;
            if (num != null) {
            }
            f18967b = num2;
        }

        private a() {
        }
    }

    private final boolean c(int i10) {
        Integer num = a.f18967b;
        return num == null || num.intValue() >= i10;
    }

    @Override // ta.a
    public void a(Throwable th, Throwable th2) {
        k.e(th, "cause");
        k.e(th2, "exception");
        if (c(19)) {
            th.addSuppressed(th2);
        } else {
            super.a(th, th2);
        }
    }
}
