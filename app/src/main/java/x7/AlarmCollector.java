package x7;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;
import b6.LocalLog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* compiled from: AlarmCollector.java */
/* renamed from: x7.a, reason: use source file name */
/* loaded from: classes.dex */
public class AlarmCollector {

    /* renamed from: d, reason: collision with root package name */
    private static final Uri f19556d = Uri.parse("content://com.oplus.alarmclock.alarmclock/alarm");

    /* renamed from: e, reason: collision with root package name */
    private static final String[] f19557e = {"_id", "hour", "minutes", "daysofweek", "enabled"};

    /* renamed from: f, reason: collision with root package name */
    private static volatile AlarmCollector f19558f = null;

    /* renamed from: a, reason: collision with root package name */
    private Context f19559a = null;

    /* renamed from: b, reason: collision with root package name */
    List<Long> f19560b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private ContentObserver f19561c = new a(new Handler());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AlarmCollector.java */
    /* renamed from: x7.a$a */
    /* loaded from: classes.dex */
    public class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            AlarmCollector alarmCollector = AlarmCollector.this;
            alarmCollector.f19560b = alarmCollector.j(alarmCollector.f19559a, System.currentTimeMillis());
        }
    }

    public static Calendar d(b bVar) {
        int f10;
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();
        calendar.set(11, bVar.f19565c);
        calendar.set(12, bVar.f19566d);
        calendar.set(13, 0);
        calendar.set(14, 0);
        if (calendar.getTimeInMillis() < timeInMillis) {
            calendar.add(6, 1);
        }
        int b10 = bVar.b();
        if (h(b10) && (f10 = f(calendar, b10)) > 0) {
            calendar.add(7, f10);
        }
        return calendar;
    }

    public static AlarmCollector e() {
        if (f19558f == null) {
            synchronized (AlarmCollector.class) {
                if (f19558f == null) {
                    f19558f = new AlarmCollector();
                }
            }
        }
        return f19558f;
    }

    static int f(Calendar calendar, int i10) {
        int i11 = (calendar.get(7) + 5) % 7;
        int i12 = 0;
        while (i12 < 7 && !i(i10, (i11 + i12) % 7)) {
            i12++;
        }
        return i12;
    }

    public static boolean h(int i10) {
        return i10 != 0;
    }

    private static boolean i(int i10, int i11) {
        return (i10 & (1 << i11)) > 0;
    }

    public void b() {
        this.f19559a.getContentResolver().unregisterContentObserver(this.f19561c);
    }

    public List<Long> c() {
        return this.f19560b;
    }

    public void g(Context context) {
        this.f19559a = context;
        try {
            context.getContentResolver().registerContentObserver(f19556d, true, this.f19561c);
        } catch (Exception e10) {
            LocalLog.b("AlarmCollector", "registerContentObserver e:" + e10.getMessage());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x001e, code lost:
    
        if (r10.moveToFirst() != false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0020, code lost:
    
        r3 = new x7.AlarmCollector.b(r9, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002c, code lost:
    
        if (r10.getInt(4) != 1) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x002e, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0031, code lost:
    
        r3.f19564b = r4;
        r3.f19563a = r10.getInt(0);
        r3.f19565c = r10.getInt(1);
        r3.f19566d = r10.getInt(2);
        r3.f19568f = r10.getInt(3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0050, code lost:
    
        if (r3.f19564b != true) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0052, code lost:
    
        r3 = d(r3).getTimeInMillis();
        b6.LocalLog.a("AlarmCollector", "alarmTimeMills = " + x7.ChargeProtectionUtils.h(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0074, code lost:
    
        if (r11 >= r3) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0076, code lost:
    
        r1.add(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0081, code lost:
    
        if (r10.moveToNext() != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0030, code lost:
    
        r4 = false;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [x7.a$a] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r2v3, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public List<Long> j(Context context, long j10) {
        ArrayList arrayList = new ArrayList();
        ?? r22 = 0;
        r22 = 0;
        r22 = 0;
        r22 = 0;
        try {
            try {
                Cursor query = context.getContentResolver().query(f19556d, f19557e, null, null, "hour, minutes ASC, _id DESC");
                if (query != null) {
                    try {
                    } catch (SQLException e10) {
                        e = e10;
                        r22 = query;
                        LocalLog.b("AlarmCollector", "queryAndParseAlarm SQLException:" + e.getMessage());
                        if (r22 != 0) {
                            r22.close();
                        }
                        return arrayList;
                    } catch (Throwable th) {
                        th = th;
                        r22 = query;
                        if (r22 != 0) {
                            r22.close();
                        }
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
            } catch (SQLException e11) {
                e = e11;
            }
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AlarmCollector.java */
    /* renamed from: x7.a$b */
    /* loaded from: classes.dex */
    public class b {

        /* renamed from: a, reason: collision with root package name */
        int f19563a;

        /* renamed from: b, reason: collision with root package name */
        boolean f19564b;

        /* renamed from: c, reason: collision with root package name */
        int f19565c;

        /* renamed from: d, reason: collision with root package name */
        int f19566d;

        /* renamed from: e, reason: collision with root package name */
        int f19567e;

        /* renamed from: f, reason: collision with root package name */
        private int f19568f;

        private b() {
            this.f19568f = 0;
        }

        public int b() {
            return this.f19568f;
        }

        public String toString() {
            return "Alarm{id=" + this.f19563a + ", enabled=" + this.f19564b + ", hour=" + this.f19565c + ", minutes=" + this.f19566d + ", daysOfWeek=" + this.f19567e + "}";
        }

        /* synthetic */ b(AlarmCollector alarmCollector, a aVar) {
            this();
        }
    }
}
