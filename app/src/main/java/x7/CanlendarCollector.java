package x7;

import android.content.ContentUris;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;
import android.text.format.Time;
import b6.LocalLog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import y5.AppFeature;

/* compiled from: CanlendarCollector.java */
/* renamed from: x7.b, reason: use source file name */
/* loaded from: classes.dex */
public class CanlendarCollector {

    /* renamed from: e, reason: collision with root package name */
    private static final Uri f19570e = Uri.parse("content://com.android.calendar/instances/whenbyday");

    /* renamed from: f, reason: collision with root package name */
    private static final Uri f19571f = Uri.parse("content://com.coloros.calendar/instances/whenbyday");

    /* renamed from: g, reason: collision with root package name */
    private static volatile CanlendarCollector f19572g = null;

    /* renamed from: a, reason: collision with root package name */
    private Context f19573a = null;

    /* renamed from: b, reason: collision with root package name */
    List<Long> f19574b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    Uri f19575c = null;

    /* renamed from: d, reason: collision with root package name */
    private ContentObserver f19576d = new a(new Handler());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CanlendarCollector.java */
    /* renamed from: x7.b$a */
    /* loaded from: classes.dex */
    public class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            LocalLog.b("CanlendarCollector", "canlendarResult onChange");
            CanlendarCollector canlendarCollector = CanlendarCollector.this;
            canlendarCollector.f19574b = canlendarCollector.j(canlendarCollector.f19573a, System.currentTimeMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: CanlendarCollector.java */
    /* renamed from: x7.b$b */
    /* loaded from: classes.dex */
    public class b {

        /* renamed from: a, reason: collision with root package name */
        Long f19578a;

        /* renamed from: b, reason: collision with root package name */
        Long f19579b;

        /* renamed from: c, reason: collision with root package name */
        Long f19580c;

        private b() {
        }

        public String toString() {
            return "Canlendar{id=" + this.f19578a + ", DTSTART=" + this.f19579b + ", DTEND=" + this.f19580c + "}";
        }

        /* synthetic */ b(CanlendarCollector canlendarCollector, a aVar) {
            this();
        }
    }

    private Uri b(int i10, int i11, String str) {
        Uri.Builder buildUpon = (AppFeature.D() ? f19570e : f19571f).buildUpon();
        ContentUris.appendId(buildUpon, i10);
        ContentUris.appendId(buildUpon, i11);
        if (str != null) {
            buildUpon.appendPath(str);
        }
        return buildUpon.build();
    }

    private int f(Cursor cursor, String str) {
        if (cursor.getColumnIndex(str) >= 0) {
            return cursor.getColumnIndex(str);
        }
        return 0;
    }

    public static CanlendarCollector g() {
        if (f19572g == null) {
            synchronized (CanlendarCollector.class) {
                if (f19572g == null) {
                    f19572g = new CanlendarCollector();
                }
            }
        }
        return f19572g;
    }

    public void c() {
        this.f19573a.getContentResolver().unregisterContentObserver(this.f19576d);
    }

    public List<Long> d() {
        return this.f19574b;
    }

    public int e() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, calendar.get(5) + 1);
        calendar.set(11, 23);
        calendar.set(12, 59);
        long timeInMillis = calendar.getTimeInMillis();
        Time time = new Time();
        time.set(timeInMillis);
        return Time.getJulianDay(timeInMillis, time.gmtoff);
    }

    public int h() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        long timeInMillis = calendar.getTimeInMillis();
        Time time = new Time();
        time.set(timeInMillis);
        return Time.getJulianDay(timeInMillis, time.gmtoff);
    }

    public void i(Context context) {
        this.f19573a = context;
        this.f19575c = b(h(), e(), null);
        try {
            this.f19573a.getContentResolver().registerContentObserver(this.f19575c, true, this.f19576d);
        } catch (Exception e10) {
            LocalLog.b("CanlendarCollector", "registerContentObserver e:" + e10.getMessage());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x001c, code lost:
    
        if (r10.moveToFirst() != false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001e, code lost:
    
        r3 = new x7.CanlendarCollector.b(r9, r2);
        r3.f19578a = java.lang.Long.valueOf(r10.getLong(f(r10, "CALENDAR_ID")));
        r3.f19579b = java.lang.Long.valueOf(r10.getLong(f(r10, "dtstart")));
        r3.f19580c = java.lang.Long.valueOf(r10.getLong(f(r10, "dtend")));
        b6.LocalLog.b("CanlendarCollector", "alarmInfo.mDTSTART:" + x7.ChargeProtectionUtils.h(r3.f19579b.longValue()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0079, code lost:
    
        if (r11 >= r3.f19579b.longValue()) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x007b, code lost:
    
        r1.add(r3.f19579b);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0084, code lost:
    
        if (r10.moveToNext() != false) goto L35;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [x7.b$a] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r2v3, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public List<Long> j(Context context, long j10) {
        ArrayList arrayList = new ArrayList();
        ?? r22 = 0;
        r22 = 0;
        try {
            try {
                Cursor query = context.getContentResolver().query(this.f19575c, null, null, null, null);
                if (query != null) {
                    try {
                    } catch (SQLException e10) {
                        e = e10;
                        r22 = query;
                        LocalLog.b("CanlendarCollector", "queryAndParseAlarm SQLException:" + e.getMessage());
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
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (SQLException e11) {
            e = e11;
        }
        return arrayList;
    }

    public void k() {
        this.f19574b = j(this.f19573a, System.currentTimeMillis());
    }
}
