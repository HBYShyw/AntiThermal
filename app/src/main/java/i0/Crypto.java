package i0;

import android.annotation.SuppressLint;
import java.util.concurrent.atomic.AtomicBoolean;
import m0.CryptoCore;
import s0.CryptoInitParameters;

/* compiled from: Crypto.java */
/* renamed from: i0.b, reason: use source file name */
/* loaded from: classes.dex */
public class Crypto {

    /* renamed from: b, reason: collision with root package name */
    private static final Object f12470b = new Object();

    /* renamed from: c, reason: collision with root package name */
    private static final AtomicBoolean f12471c = new AtomicBoolean(false);

    /* renamed from: d, reason: collision with root package name */
    @SuppressLint({"StaticFieldLeak"})
    private static volatile Crypto f12472d;

    /* renamed from: a, reason: collision with root package name */
    private CryptoCore f12473a;

    private Crypto() {
    }

    public static Session a(String str) {
        return d().f12473a.i(str, null);
    }

    private void b(CryptoInitParameters cryptoInitParameters) {
        synchronized (this) {
            if (this.f12473a == null) {
                this.f12473a = new CryptoCore(cryptoInitParameters, 60);
            }
        }
    }

    public static long c(String str) {
        return d().f12473a.n(str);
    }

    private static Crypto d() {
        if (f12472d == null) {
            synchronized (f12470b) {
                if (f12472d == null) {
                    f12472d = new Crypto();
                }
            }
        }
        return f12472d;
    }

    public static void e(CryptoInitParameters cryptoInitParameters) {
        AtomicBoolean atomicBoolean = f12471c;
        if (atomicBoolean.get()) {
            return;
        }
        d().b(cryptoInitParameters);
        atomicBoolean.set(true);
    }

    public static boolean f(Session session) {
        return d().f12473a.J(session);
    }
}
