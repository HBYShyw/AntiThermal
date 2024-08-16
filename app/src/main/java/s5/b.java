package s5;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import kotlin.Metadata;
import ma.Unit;
import za.k;

/* compiled from: Logger.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u000e\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007J\u0006\u0010\u000b\u001a\u00020\tJ\u0006\u0010\f\u001a\u00020\u0004J\u0016\u0010\u000f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0002J\u0016\u0010\u0010\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0002J\u001e\u0010\u0012\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0002J\u0016\u0010\u0013\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0002¨\u0006\u0016"}, d2 = {"Ls5/b;", "", "", "key", "", "def", "f", "Landroid/content/Context;", "context", "Lma/f0;", "h", "j", "i", TriggerEvent.NOTIFICATION_TAG, "content", "g", "c", "widgetCode", "d", "e", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f18064a;

    /* renamed from: c, reason: collision with root package name */
    public static final b f18066c = new b();

    /* renamed from: b, reason: collision with root package name */
    private static final a f18065b = new a(new Handler());

    /* compiled from: Logger.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"s5/b$a", "Landroid/database/ContentObserver;", "", "selfChange", "Lma/f0;", "onChange", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    public static final class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            super.onChange(z10);
            b bVar = b.f18066c;
            bVar.j();
            Log.d("CardWidget.Logger", "onChange: sDebuggable = " + b.b(bVar));
        }
    }

    /* compiled from: Logger.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0003\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "run", "()V", "<anonymous>"}, k = 3, mv = {1, 4, 2})
    /* renamed from: s5.b$b, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    static final class RunnableC0100b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Context f18067e;

        RunnableC0100b(Context context) {
            this.f18067e = context;
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                b bVar = b.f18066c;
                bVar.j();
                this.f18067e.getContentResolver().registerContentObserver(Settings.System.getUriFor("log_switch_type"), false, b.a(bVar));
            } catch (Exception e10) {
                Log.e("CardWidget. Logger", "initial logger has error:" + e10.getMessage());
            }
        }
    }

    private b() {
    }

    public static final /* synthetic */ a a(b bVar) {
        return f18065b;
    }

    public static final /* synthetic */ boolean b(b bVar) {
        return f18064a;
    }

    private final boolean f(String key, boolean def) {
        Object obj;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            c cVar = c.f18068a;
            k.d(cls, "this");
            obj = cVar.b(cls, "getBoolean", key, Boolean.valueOf(def));
        } catch (ClassNotFoundException unused) {
            obj = null;
        }
        Boolean bool = (Boolean) (obj instanceof Boolean ? obj : null);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public final void c(String str, String str2) {
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        k.e(str2, "content");
        if (f18064a) {
            Log.d("CardWidget." + str, str2);
        }
    }

    public final void d(String str, String str2, String str3) {
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        k.e(str2, "widgetCode");
        k.e(str3, "content");
        c(str, "[DEBUG_" + str2 + ']' + str3);
    }

    public final void e(String str, String str2) {
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        k.e(str2, "content");
        Log.e("CardWidget." + str, str2);
    }

    public final void g(String str, String str2) {
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        k.e(str2, "content");
        Log.d("CardWidget." + str, str2);
    }

    public final void h(Context context) {
        k.e(context, "context");
        new Thread(new RunnableC0100b(context)).start();
    }

    public final boolean i() {
        return f18064a;
    }

    public final void j() {
        synchronized (Boolean.valueOf(f18064a)) {
            f18064a = f18066c.f("persist.sys.assert.panic", false);
            Unit unit = Unit.f15173a;
        }
    }
}
