package je;

import android.util.Log;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.sceneservice.sdk.dataprovider.bean.SceneStatusInfo;
import de.TaskRunner;
import he.Http2;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import kotlin.collections.m0;
import sd._Strings;
import sd.v;
import zd.OkHttpClient;

/* compiled from: AndroidLog.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0007\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u0010\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\b\u001a\u00020\u00072\u0006\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u0002H\u0002J1\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u00022\b\u0010\r\u001a\u0004\u0018\u00010\fH\u0000¢\u0006\u0004\b\u000e\u0010\u000fJ\u0006\u0010\u0010\u001a\u00020\u0007¨\u0006\u0013"}, d2 = {"Lje/c;", "", "", "loggerName", "d", "logger", TriggerEvent.NOTIFICATION_TAG, "Lma/f0;", "c", "", "logLevel", "message", "", "t", "a", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/Throwable;)V", "b", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    public static final c f13910a = new c();

    /* renamed from: b, reason: collision with root package name */
    private static final CopyOnWriteArraySet<Logger> f13911b = new CopyOnWriteArraySet<>();

    /* renamed from: c, reason: collision with root package name */
    private static final Map<String, String> f13912c;

    static {
        Map<String, String> s7;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Package r12 = OkHttpClient.class.getPackage();
        String name = r12 == null ? null : r12.getName();
        if (name != null) {
            linkedHashMap.put(name, "OkHttp");
        }
        String name2 = OkHttpClient.class.getName();
        za.k.d(name2, "OkHttpClient::class.java.name");
        linkedHashMap.put(name2, "okhttp.OkHttpClient");
        String name3 = Http2.class.getName();
        za.k.d(name3, "Http2::class.java.name");
        linkedHashMap.put(name3, "okhttp.Http2");
        String name4 = TaskRunner.class.getName();
        za.k.d(name4, "TaskRunner::class.java.name");
        linkedHashMap.put(name4, "okhttp.TaskRunner");
        linkedHashMap.put("okhttp3.mockwebserver.MockWebServer", "okhttp.MockWebServer");
        s7 = m0.s(linkedHashMap);
        f13912c = s7;
    }

    private c() {
    }

    private final void c(String str, String str2) {
        Level level;
        Logger logger = Logger.getLogger(str);
        if (f13911b.add(logger)) {
            logger.setUseParentHandlers(false);
            if (Log.isLoggable(str2, 3)) {
                level = Level.FINE;
            } else {
                level = Log.isLoggable(str2, 4) ? Level.INFO : Level.WARNING;
            }
            logger.setLevel(level);
            logger.addHandler(d.f13913a);
        }
    }

    private final String d(String loggerName) {
        String M0;
        String str = f13912c.get(loggerName);
        if (str != null) {
            return str;
        }
        M0 = _Strings.M0(loggerName, 23);
        return M0;
    }

    public final void a(String loggerName, int logLevel, String message, Throwable t7) {
        int U;
        int min;
        za.k.e(loggerName, "loggerName");
        za.k.e(message, "message");
        String d10 = d(loggerName);
        if (Log.isLoggable(d10, logLevel)) {
            if (t7 != null) {
                message = message + '\n' + ((Object) Log.getStackTraceString(t7));
            }
            int i10 = 0;
            int length = message.length();
            while (i10 < length) {
                U = v.U(message, '\n', i10, false, 4, null);
                if (U == -1) {
                    U = length;
                }
                while (true) {
                    min = Math.min(U, i10 + SceneStatusInfo.SceneConstant.TRIP_ARRIVE_START_STATION);
                    String substring = message.substring(i10, min);
                    za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                    Log.println(logLevel, d10, substring);
                    if (min >= U) {
                        break;
                    } else {
                        i10 = min;
                    }
                }
                i10 = min + 1;
            }
        }
    }

    public final void b() {
        for (Map.Entry<String, String> entry : f13912c.entrySet()) {
            c(entry.getKey(), entry.getValue());
        }
    }
}
