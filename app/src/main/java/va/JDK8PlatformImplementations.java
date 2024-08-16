package va;

import db.Random;
import eb.PlatformThreadLocalRandom;
import ua.JDK7PlatformImplementations;

/* compiled from: JDK8PlatformImplementations.kt */
/* renamed from: va.a, reason: use source file name */
/* loaded from: classes2.dex */
public class JDK8PlatformImplementations extends JDK7PlatformImplementations {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JDK8PlatformImplementations.kt */
    /* renamed from: va.a$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        public static final a f19201a = new a();

        /* renamed from: b, reason: collision with root package name */
        public static final Integer f19202b;

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
                f19202b = num2;
            }
            num = null;
            if (num != null) {
            }
            f19202b = num2;
        }

        private a() {
        }
    }

    private final boolean c(int i10) {
        Integer num = a.f19202b;
        return num == null || num.intValue() >= i10;
    }

    @Override // ta.a
    public Random b() {
        return c(34) ? new PlatformThreadLocalRandom() : super.b();
    }
}
