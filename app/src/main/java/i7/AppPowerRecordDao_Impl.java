package i7;

import a0.DBUtil;
import android.database.Cursor;
import androidx.room.RoomDatabase;
import c0.SupportSQLiteStatement;
import j7.AppGuardEvent;
import j7.AppInfo;
import j7.AppPowerRecord;
import y.EntityInsertionAdapter;
import y.RoomSQLiteQuery;
import y.SharedSQLiteStatement;

/* compiled from: AppPowerRecordDao_Impl.java */
/* renamed from: i7.b, reason: use source file name */
/* loaded from: classes.dex */
public final class AppPowerRecordDao_Impl extends AppPowerRecordDao {

    /* renamed from: a, reason: collision with root package name */
    private final RoomDatabase f12653a;

    /* renamed from: b, reason: collision with root package name */
    private final EntityInsertionAdapter f12654b;

    /* renamed from: c, reason: collision with root package name */
    private final EntityInsertionAdapter f12655c;

    /* renamed from: d, reason: collision with root package name */
    private final EntityInsertionAdapter f12656d;

    /* renamed from: e, reason: collision with root package name */
    private final SharedSQLiteStatement f12657e;

    /* renamed from: f, reason: collision with root package name */
    private final SharedSQLiteStatement f12658f;

    /* renamed from: g, reason: collision with root package name */
    private final SharedSQLiteStatement f12659g;

    /* renamed from: h, reason: collision with root package name */
    private final SharedSQLiteStatement f12660h;

    /* renamed from: i, reason: collision with root package name */
    private final SharedSQLiteStatement f12661i;

    /* renamed from: j, reason: collision with root package name */
    private final SharedSQLiteStatement f12662j;

    /* compiled from: AppPowerRecordDao_Impl.java */
    /* renamed from: i7.b$a */
    /* loaded from: classes.dex */
    class a extends EntityInsertionAdapter<AppPowerRecord> {
        a(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "INSERT OR REPLACE INTO `app_power_record`(`_id`,`valid`,`uid`,`app_name`,`cpu_time`,`small_cpu_time`,`middle_cpu_time`,`big_cpu_time`,`alarm_wakeup_count`,`wakelock_time`,`gps_time`,`sensor_time`,`wifi_time`,`wifi_bytes`,`wifi_scan_count`,`cell_time`,`cell_bytes`,`traffic_count`,`bt_scan_time`,`background_duration`,`start_time`,`end_time`,`duration`,`screen_state`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        @Override // y.EntityInsertionAdapter
        /* renamed from: l, reason: merged with bridge method [inline-methods] */
        public void g(SupportSQLiteStatement supportSQLiteStatement, AppPowerRecord appPowerRecord) {
            supportSQLiteStatement.y(1, appPowerRecord.f13027a);
            supportSQLiteStatement.y(2, appPowerRecord.f13028b ? 1L : 0L);
            supportSQLiteStatement.y(3, appPowerRecord.f13029c);
            String str = appPowerRecord.f13030d;
            if (str == null) {
                supportSQLiteStatement.Y(4);
            } else {
                supportSQLiteStatement.j(4, str);
            }
            supportSQLiteStatement.y(5, appPowerRecord.f13031e);
            supportSQLiteStatement.y(6, appPowerRecord.f13032f);
            supportSQLiteStatement.y(7, appPowerRecord.f13033g);
            supportSQLiteStatement.y(8, appPowerRecord.f13034h);
            supportSQLiteStatement.y(9, appPowerRecord.f13035i);
            supportSQLiteStatement.y(10, appPowerRecord.f13036j);
            supportSQLiteStatement.y(11, appPowerRecord.f13037k);
            supportSQLiteStatement.y(12, appPowerRecord.f13038l);
            supportSQLiteStatement.y(13, appPowerRecord.f13039m);
            supportSQLiteStatement.y(14, appPowerRecord.f13040n);
            supportSQLiteStatement.y(15, appPowerRecord.f13041o);
            supportSQLiteStatement.y(16, appPowerRecord.f13042p);
            supportSQLiteStatement.y(17, appPowerRecord.f13043q);
            supportSQLiteStatement.y(18, appPowerRecord.f13044r);
            supportSQLiteStatement.y(19, appPowerRecord.f13045s);
            supportSQLiteStatement.y(20, appPowerRecord.f13046t);
            String str2 = appPowerRecord.f13047u;
            if (str2 == null) {
                supportSQLiteStatement.Y(21);
            } else {
                supportSQLiteStatement.j(21, str2);
            }
            supportSQLiteStatement.y(22, appPowerRecord.f13048v);
            supportSQLiteStatement.y(23, appPowerRecord.f13049w);
            supportSQLiteStatement.y(24, appPowerRecord.f13050x);
        }
    }

    /* compiled from: AppPowerRecordDao_Impl.java */
    /* renamed from: i7.b$b */
    /* loaded from: classes.dex */
    class b extends EntityInsertionAdapter<AppGuardEvent> {
        b(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "INSERT OR REPLACE INTO `app_guard_event`(`_id`,`uid`,`app_name`,`guard_type`,`reason`,`level`,`time`,`result`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
        }

        @Override // y.EntityInsertionAdapter
        /* renamed from: l, reason: merged with bridge method [inline-methods] */
        public void g(SupportSQLiteStatement supportSQLiteStatement, AppGuardEvent appGuardEvent) {
            supportSQLiteStatement.y(1, appGuardEvent.f13013a);
            supportSQLiteStatement.y(2, appGuardEvent.f13014b);
            String str = appGuardEvent.f13015c;
            if (str == null) {
                supportSQLiteStatement.Y(3);
            } else {
                supportSQLiteStatement.j(3, str);
            }
            supportSQLiteStatement.y(4, appGuardEvent.f13016d);
            String str2 = appGuardEvent.f13017e;
            if (str2 == null) {
                supportSQLiteStatement.Y(5);
            } else {
                supportSQLiteStatement.j(5, str2);
            }
            supportSQLiteStatement.y(6, appGuardEvent.f13018f);
            supportSQLiteStatement.y(7, appGuardEvent.f13019g);
            supportSQLiteStatement.y(8, appGuardEvent.f13020h);
        }
    }

    /* compiled from: AppPowerRecordDao_Impl.java */
    /* renamed from: i7.b$c */
    /* loaded from: classes.dex */
    class c extends EntityInsertionAdapter<AppInfo> {
        c(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "INSERT OR REPLACE INTO `app_info`(`_id`,`uid`,`pkg_name`,`app_type`,`optimize_value`,`background_value`) VALUES (nullif(?, 0),?,?,?,?,?)";
        }

        @Override // y.EntityInsertionAdapter
        /* renamed from: l, reason: merged with bridge method [inline-methods] */
        public void g(SupportSQLiteStatement supportSQLiteStatement, AppInfo appInfo) {
            supportSQLiteStatement.y(1, appInfo.f13021a);
            supportSQLiteStatement.y(2, appInfo.f13022b);
            String str = appInfo.f13023c;
            if (str == null) {
                supportSQLiteStatement.Y(3);
            } else {
                supportSQLiteStatement.j(3, str);
            }
            supportSQLiteStatement.y(4, appInfo.f13024d);
            supportSQLiteStatement.y(5, appInfo.f13025e);
            supportSQLiteStatement.y(6, appInfo.f13026f);
        }
    }

    /* compiled from: AppPowerRecordDao_Impl.java */
    /* renamed from: i7.b$d */
    /* loaded from: classes.dex */
    class d extends SharedSQLiteStatement {
        d(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM app_info";
        }
    }

    /* compiled from: AppPowerRecordDao_Impl.java */
    /* renamed from: i7.b$e */
    /* loaded from: classes.dex */
    class e extends SharedSQLiteStatement {
        e(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM app_info WHERE uid == ? AND pkg_name == ?";
        }
    }

    /* compiled from: AppPowerRecordDao_Impl.java */
    /* renamed from: i7.b$f */
    /* loaded from: classes.dex */
    class f extends SharedSQLiteStatement {
        f(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE app_info SET app_type = ? WHERE pkg_name == ?";
        }
    }

    /* compiled from: AppPowerRecordDao_Impl.java */
    /* renamed from: i7.b$g */
    /* loaded from: classes.dex */
    class g extends SharedSQLiteStatement {
        g(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE app_info SET optimize_value = ? WHERE pkg_name == ?";
        }
    }

    /* compiled from: AppPowerRecordDao_Impl.java */
    /* renamed from: i7.b$h */
    /* loaded from: classes.dex */
    class h extends SharedSQLiteStatement {
        h(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE app_info SET background_value = ? WHERE pkg_name == ?";
        }
    }

    /* compiled from: AppPowerRecordDao_Impl.java */
    /* renamed from: i7.b$i */
    /* loaded from: classes.dex */
    class i extends SharedSQLiteStatement {
        i(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM app_power_record WHERE end_time < ?";
        }
    }

    public AppPowerRecordDao_Impl(RoomDatabase roomDatabase) {
        this.f12653a = roomDatabase;
        this.f12654b = new a(roomDatabase);
        this.f12655c = new b(roomDatabase);
        this.f12656d = new c(roomDatabase);
        this.f12657e = new d(roomDatabase);
        this.f12658f = new e(roomDatabase);
        this.f12659g = new f(roomDatabase);
        this.f12660h = new g(roomDatabase);
        this.f12661i = new h(roomDatabase);
        this.f12662j = new i(roomDatabase);
    }

    @Override // i7.AppPowerRecordDao
    public void a(long j10) {
        this.f12653a.b();
        SupportSQLiteStatement a10 = this.f12662j.a();
        a10.y(1, j10);
        this.f12653a.c();
        try {
            a10.l();
            this.f12653a.s();
        } finally {
            this.f12653a.g();
            this.f12662j.f(a10);
        }
    }

    @Override // i7.AppPowerRecordDao
    public int b() {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT COUNT(*) FROM app_info", 0);
        this.f12653a.b();
        Cursor b10 = DBUtil.b(this.f12653a, m10, false);
        try {
            return b10.moveToFirst() ? b10.getInt(0) : 0;
        } finally {
            b10.close();
            m10.w();
        }
    }

    @Override // i7.AppPowerRecordDao
    public void c(AppPowerRecord appPowerRecord) {
        this.f12653a.b();
        this.f12653a.c();
        try {
            this.f12654b.i(appPowerRecord);
            this.f12653a.s();
        } finally {
            this.f12653a.g();
        }
    }
}
