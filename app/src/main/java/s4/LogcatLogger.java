package s4;

import android.util.Log;
import com.oplus.anim.EffectiveLogger;
import com.oplus.anim.L;
import java.util.HashSet;
import java.util.Set;

/* compiled from: LogcatLogger.java */
/* renamed from: s4.d, reason: use source file name */
/* loaded from: classes.dex */
public class LogcatLogger implements EffectiveLogger {

    /* renamed from: a, reason: collision with root package name */
    private static final Set<String> f18054a = new HashSet();

    @Override // com.oplus.anim.EffectiveLogger
    public void a(String str, Throwable th) {
        if (L.f9733a) {
            Log.d("LOG_Effective", str, th);
        }
    }

    @Override // com.oplus.anim.EffectiveLogger
    public void b(String str) {
        e(str, null);
    }

    @Override // com.oplus.anim.EffectiveLogger
    public void c(String str, Throwable th) {
        if (L.f9733a) {
            Set<String> set = f18054a;
            if (set.contains(str)) {
                return;
            }
            Log.w("LOG_Effective", str, th);
            set.add(str);
        }
    }

    @Override // com.oplus.anim.EffectiveLogger
    public void d(String str) {
        c(str, null);
    }

    public void e(String str, Throwable th) {
        if (L.f9733a) {
            Log.d("LOG_Effective", str, th);
        }
    }
}
