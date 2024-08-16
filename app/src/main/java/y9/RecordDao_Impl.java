package y9;

import a0.CursorUtil;
import a0.DBUtil;
import a0.StringUtil;
import android.database.Cursor;
import android.util.ArrayMap;
import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;
import c0.SupportSQLiteStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import y.EntityDeletionOrUpdateAdapter;
import y.EntityInsertionAdapter;
import y.RoomSQLiteQuery;
import y.SharedSQLiteStatement;
import z9.AppToShow;
import z9.MaliciousDetailRecord;
import z9.MaliciousRecord;
import z9.Record;
import z9.UnstableRecord;

/* compiled from: RecordDao_Impl.java */
/* renamed from: y9.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class RecordDao_Impl extends RecordDao {
    private final SharedSQLiteStatement A;
    private final SharedSQLiteStatement B;
    private final SharedSQLiteStatement C;
    private final SharedSQLiteStatement D;
    private final SharedSQLiteStatement E;
    private final SharedSQLiteStatement F;
    private final SharedSQLiteStatement G;
    private final SharedSQLiteStatement H;
    private final SharedSQLiteStatement I;

    /* renamed from: a, reason: collision with root package name */
    private final RoomDatabase f19932a;

    /* renamed from: b, reason: collision with root package name */
    private final EntityInsertionAdapter f19933b;

    /* renamed from: c, reason: collision with root package name */
    private final EntityInsertionAdapter f19934c;

    /* renamed from: d, reason: collision with root package name */
    private final EntityInsertionAdapter f19935d;

    /* renamed from: e, reason: collision with root package name */
    private final EntityInsertionAdapter f19936e;

    /* renamed from: f, reason: collision with root package name */
    private final EntityInsertionAdapter f19937f;

    /* renamed from: g, reason: collision with root package name */
    private final EntityDeletionOrUpdateAdapter f19938g;

    /* renamed from: h, reason: collision with root package name */
    private final SharedSQLiteStatement f19939h;

    /* renamed from: i, reason: collision with root package name */
    private final SharedSQLiteStatement f19940i;

    /* renamed from: j, reason: collision with root package name */
    private final SharedSQLiteStatement f19941j;

    /* renamed from: k, reason: collision with root package name */
    private final SharedSQLiteStatement f19942k;

    /* renamed from: l, reason: collision with root package name */
    private final SharedSQLiteStatement f19943l;

    /* renamed from: m, reason: collision with root package name */
    private final SharedSQLiteStatement f19944m;

    /* renamed from: n, reason: collision with root package name */
    private final SharedSQLiteStatement f19945n;

    /* renamed from: o, reason: collision with root package name */
    private final SharedSQLiteStatement f19946o;

    /* renamed from: p, reason: collision with root package name */
    private final SharedSQLiteStatement f19947p;

    /* renamed from: q, reason: collision with root package name */
    private final SharedSQLiteStatement f19948q;

    /* renamed from: r, reason: collision with root package name */
    private final SharedSQLiteStatement f19949r;

    /* renamed from: s, reason: collision with root package name */
    private final SharedSQLiteStatement f19950s;

    /* renamed from: t, reason: collision with root package name */
    private final SharedSQLiteStatement f19951t;

    /* renamed from: u, reason: collision with root package name */
    private final SharedSQLiteStatement f19952u;

    /* renamed from: v, reason: collision with root package name */
    private final SharedSQLiteStatement f19953v;

    /* renamed from: w, reason: collision with root package name */
    private final SharedSQLiteStatement f19954w;

    /* renamed from: x, reason: collision with root package name */
    private final SharedSQLiteStatement f19955x;

    /* renamed from: y, reason: collision with root package name */
    private final SharedSQLiteStatement f19956y;

    /* renamed from: z, reason: collision with root package name */
    private final SharedSQLiteStatement f19957z;

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$a */
    /* loaded from: classes2.dex */
    class a extends SharedSQLiteStatement {
        a(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE record SET called_app = ? WHERE called_pkg == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$a0 */
    /* loaded from: classes2.dex */
    class a0 extends SharedSQLiteStatement {
        a0(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM malicious_detail_record";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$b */
    /* loaded from: classes2.dex */
    class b extends SharedSQLiteStatement {
        b(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM record WHERE called_pkg == ? AND caller_pkg == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$b0 */
    /* loaded from: classes2.dex */
    class b0 implements Callable<List<Record>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ RoomSQLiteQuery f19961a;

        b0(RoomSQLiteQuery roomSQLiteQuery) {
            this.f19961a = roomSQLiteQuery;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public List<Record> call() {
            Cursor b10 = DBUtil.b(RecordDao_Impl.this.f19932a, this.f19961a, false);
            try {
                int b11 = CursorUtil.b(b10, "_id");
                int b12 = CursorUtil.b(b10, "called_pkg");
                int b13 = CursorUtil.b(b10, "caller_pkg");
                int b14 = CursorUtil.b(b10, "called_app");
                int b15 = CursorUtil.b(b10, "caller_app");
                int b16 = CursorUtil.b(b10, "launch_mode");
                int b17 = CursorUtil.b(b10, "launch_type");
                int b18 = CursorUtil.b(b10, "count");
                int b19 = CursorUtil.b(b10, "night_count");
                int b20 = CursorUtil.b(b10, "day_count");
                int b21 = CursorUtil.b(b10, "time");
                int b22 = CursorUtil.b(b10, "date");
                int b23 = CursorUtil.b(b10, "reason");
                ArrayList arrayList = new ArrayList(b10.getCount());
                while (b10.moveToNext()) {
                    Record record = new Record();
                    record.f20323a = b10.getLong(b11);
                    record.f20324b = b10.getString(b12);
                    record.f20325c = b10.getString(b13);
                    record.f20326d = b10.getString(b14);
                    record.f20327e = b10.getString(b15);
                    record.f20328f = b10.getString(b16);
                    record.f20329g = b10.getString(b17);
                    record.f20330h = b10.getLong(b18);
                    record.f20331i = b10.getLong(b19);
                    record.f20332j = b10.getLong(b20);
                    record.f20333k = b10.getLong(b21);
                    record.f20334l = b10.getLong(b22);
                    b23 = b23;
                    record.f20335m = b10.getString(b23);
                    arrayList = arrayList;
                    arrayList.add(record);
                }
                return arrayList;
            } finally {
                b10.close();
            }
        }

        protected void finalize() {
            this.f19961a.w();
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$c */
    /* loaded from: classes2.dex */
    class c extends SharedSQLiteStatement {
        c(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM record WHERE called_pkg == ? OR caller_pkg == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$c0 */
    /* loaded from: classes2.dex */
    class c0 implements Callable<List<Record>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ RoomSQLiteQuery f19964a;

        c0(RoomSQLiteQuery roomSQLiteQuery) {
            this.f19964a = roomSQLiteQuery;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public List<Record> call() {
            Cursor b10 = DBUtil.b(RecordDao_Impl.this.f19932a, this.f19964a, false);
            try {
                int b11 = CursorUtil.b(b10, "_id");
                int b12 = CursorUtil.b(b10, "called_pkg");
                int b13 = CursorUtil.b(b10, "caller_pkg");
                int b14 = CursorUtil.b(b10, "called_app");
                int b15 = CursorUtil.b(b10, "caller_app");
                int b16 = CursorUtil.b(b10, "launch_mode");
                int b17 = CursorUtil.b(b10, "launch_type");
                int b18 = CursorUtil.b(b10, "count");
                int b19 = CursorUtil.b(b10, "night_count");
                int b20 = CursorUtil.b(b10, "day_count");
                int b21 = CursorUtil.b(b10, "time");
                int b22 = CursorUtil.b(b10, "date");
                int b23 = CursorUtil.b(b10, "reason");
                ArrayList arrayList = new ArrayList(b10.getCount());
                while (b10.moveToNext()) {
                    Record record = new Record();
                    record.f20323a = b10.getLong(b11);
                    record.f20324b = b10.getString(b12);
                    record.f20325c = b10.getString(b13);
                    record.f20326d = b10.getString(b14);
                    record.f20327e = b10.getString(b15);
                    record.f20328f = b10.getString(b16);
                    record.f20329g = b10.getString(b17);
                    record.f20330h = b10.getLong(b18);
                    record.f20331i = b10.getLong(b19);
                    record.f20332j = b10.getLong(b20);
                    record.f20333k = b10.getLong(b21);
                    record.f20334l = b10.getLong(b22);
                    b23 = b23;
                    record.f20335m = b10.getString(b23);
                    arrayList = arrayList;
                    arrayList.add(record);
                }
                return arrayList;
            } finally {
                b10.close();
            }
        }

        protected void finalize() {
            this.f19964a.w();
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$d */
    /* loaded from: classes2.dex */
    class d extends SharedSQLiteStatement {
        d(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM record WHERE date < ? OR date > ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$d0 */
    /* loaded from: classes2.dex */
    class d0 implements Callable<List<AppToShow>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ RoomSQLiteQuery f19967a;

        d0(RoomSQLiteQuery roomSQLiteQuery) {
            this.f19967a = roomSQLiteQuery;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public List<AppToShow> call() {
            Cursor b10 = DBUtil.b(RecordDao_Impl.this.f19932a, this.f19967a, false);
            try {
                int b11 = CursorUtil.b(b10, "_id");
                int b12 = CursorUtil.b(b10, "packageName");
                int b13 = CursorUtil.b(b10, "label");
                int b14 = CursorUtil.b(b10, "isBootBroadcast");
                int b15 = CursorUtil.b(b10, "isChecked");
                int b16 = CursorUtil.b(b10, "isSuggested");
                int b17 = CursorUtil.b(b10, "isBootStart");
                ArrayList arrayList = new ArrayList(b10.getCount());
                while (b10.moveToNext()) {
                    AppToShow appToShow = new AppToShow();
                    appToShow.f20304a = b10.getLong(b11);
                    appToShow.f20305b = b10.getString(b12);
                    appToShow.f20306c = b10.getString(b13);
                    boolean z10 = true;
                    appToShow.f20307d = b10.getInt(b14) != 0;
                    appToShow.f20308e = b10.getInt(b15) != 0;
                    appToShow.f20309f = b10.getInt(b16) != 0;
                    if (b10.getInt(b17) == 0) {
                        z10 = false;
                    }
                    appToShow.f20310g = z10;
                    arrayList.add(appToShow);
                }
                return arrayList;
            } finally {
                b10.close();
            }
        }

        protected void finalize() {
            this.f19967a.w();
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$e */
    /* loaded from: classes2.dex */
    class e extends SharedSQLiteStatement {
        e(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM record WHERE date == ? AND launch_mode ==?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$e0 */
    /* loaded from: classes2.dex */
    class e0 implements Callable<List<AppToShow>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ RoomSQLiteQuery f19970a;

        e0(RoomSQLiteQuery roomSQLiteQuery) {
            this.f19970a = roomSQLiteQuery;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public List<AppToShow> call() {
            Cursor b10 = DBUtil.b(RecordDao_Impl.this.f19932a, this.f19970a, false);
            try {
                int b11 = CursorUtil.b(b10, "_id");
                int b12 = CursorUtil.b(b10, "packageName");
                int b13 = CursorUtil.b(b10, "label");
                int b14 = CursorUtil.b(b10, "isBootBroadcast");
                int b15 = CursorUtil.b(b10, "isChecked");
                int b16 = CursorUtil.b(b10, "isSuggested");
                int b17 = CursorUtil.b(b10, "isBootStart");
                ArrayList arrayList = new ArrayList(b10.getCount());
                while (b10.moveToNext()) {
                    AppToShow appToShow = new AppToShow();
                    appToShow.f20304a = b10.getLong(b11);
                    appToShow.f20305b = b10.getString(b12);
                    appToShow.f20306c = b10.getString(b13);
                    boolean z10 = true;
                    appToShow.f20307d = b10.getInt(b14) != 0;
                    appToShow.f20308e = b10.getInt(b15) != 0;
                    appToShow.f20309f = b10.getInt(b16) != 0;
                    if (b10.getInt(b17) == 0) {
                        z10 = false;
                    }
                    appToShow.f20310g = z10;
                    arrayList.add(appToShow);
                }
                return arrayList;
            } finally {
                b10.close();
            }
        }

        protected void finalize() {
            this.f19970a.w();
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$f */
    /* loaded from: classes2.dex */
    class f extends SharedSQLiteStatement {
        f(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE record SET count = ? , time = ?, night_count = ?, day_count = ? WHERE called_pkg == ? AND caller_pkg == ? AND launch_mode == ? AND launch_type == ? AND date == ? AND reason == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$f0 */
    /* loaded from: classes2.dex */
    class f0 extends EntityInsertionAdapter<UnstableRecord> {
        f0(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "INSERT OR REPLACE INTO `unstable_record`(`_id`,`unstable_key`,`user_id`,`last_unstable_time`,`restrict_begin_time`,`frequent_unstable_count`,`package_name`,`exception_class`,`exception_msg`,`unstable_reason`,`unstable_crash_time_upload`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
        }

        @Override // y.EntityInsertionAdapter
        /* renamed from: l, reason: merged with bridge method [inline-methods] */
        public void g(SupportSQLiteStatement supportSQLiteStatement, UnstableRecord unstableRecord) {
            supportSQLiteStatement.y(1, unstableRecord.f20336a);
            String str = unstableRecord.f20337b;
            if (str == null) {
                supportSQLiteStatement.Y(2);
            } else {
                supportSQLiteStatement.j(2, str);
            }
            supportSQLiteStatement.y(3, unstableRecord.f20338c);
            supportSQLiteStatement.y(4, unstableRecord.f20339d);
            supportSQLiteStatement.y(5, unstableRecord.f20340e);
            supportSQLiteStatement.y(6, unstableRecord.f20341f);
            String str2 = unstableRecord.f20342g;
            if (str2 == null) {
                supportSQLiteStatement.Y(7);
            } else {
                supportSQLiteStatement.j(7, str2);
            }
            String str3 = unstableRecord.f20343h;
            if (str3 == null) {
                supportSQLiteStatement.Y(8);
            } else {
                supportSQLiteStatement.j(8, str3);
            }
            String str4 = unstableRecord.f20344i;
            if (str4 == null) {
                supportSQLiteStatement.Y(9);
            } else {
                supportSQLiteStatement.j(9, str4);
            }
            String str5 = unstableRecord.f20345j;
            if (str5 == null) {
                supportSQLiteStatement.Y(10);
            } else {
                supportSQLiteStatement.j(10, str5);
            }
            String str6 = unstableRecord.f20346k;
            if (str6 == null) {
                supportSQLiteStatement.Y(11);
            } else {
                supportSQLiteStatement.j(11, str6);
            }
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$g */
    /* loaded from: classes2.dex */
    class g extends SharedSQLiteStatement {
        g(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE record SET called_pkg = ? WHERE called_pkg == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$g0 */
    /* loaded from: classes2.dex */
    class g0 extends EntityInsertionAdapter<MaliciousRecord> {
        g0(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "INSERT OR REPLACE INTO `malicious_record`(`_id`,`called_pkg`,`caller_pkg`,`count`,`time`,`date`,`cpn`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
        }

        @Override // y.EntityInsertionAdapter
        /* renamed from: l, reason: merged with bridge method [inline-methods] */
        public void g(SupportSQLiteStatement supportSQLiteStatement, MaliciousRecord maliciousRecord) {
            supportSQLiteStatement.y(1, maliciousRecord.f20316a);
            String str = maliciousRecord.f20317b;
            if (str == null) {
                supportSQLiteStatement.Y(2);
            } else {
                supportSQLiteStatement.j(2, str);
            }
            String str2 = maliciousRecord.f20318c;
            if (str2 == null) {
                supportSQLiteStatement.Y(3);
            } else {
                supportSQLiteStatement.j(3, str2);
            }
            supportSQLiteStatement.y(4, maliciousRecord.f20319d);
            supportSQLiteStatement.y(5, maliciousRecord.f20320e);
            supportSQLiteStatement.y(6, maliciousRecord.f20321f);
            String str3 = maliciousRecord.f20322g;
            if (str3 == null) {
                supportSQLiteStatement.Y(7);
            } else {
                supportSQLiteStatement.j(7, str3);
            }
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$h */
    /* loaded from: classes2.dex */
    class h extends SharedSQLiteStatement {
        h(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE record SET caller_pkg = ? WHERE caller_pkg == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$h0 */
    /* loaded from: classes2.dex */
    class h0 extends EntityInsertionAdapter<MaliciousDetailRecord> {
        h0(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "INSERT OR REPLACE INTO `malicious_detail_record`(`_id`,`called_pkg`,`time`,`date`,`type`) VALUES (nullif(?, 0),?,?,?,?)";
        }

        @Override // y.EntityInsertionAdapter
        /* renamed from: l, reason: merged with bridge method [inline-methods] */
        public void g(SupportSQLiteStatement supportSQLiteStatement, MaliciousDetailRecord maliciousDetailRecord) {
            supportSQLiteStatement.y(1, maliciousDetailRecord.f20311a);
            String str = maliciousDetailRecord.f20312b;
            if (str == null) {
                supportSQLiteStatement.Y(2);
            } else {
                supportSQLiteStatement.j(2, str);
            }
            supportSQLiteStatement.y(3, maliciousDetailRecord.f20313c);
            supportSQLiteStatement.y(4, maliciousDetailRecord.f20314d);
            String str2 = maliciousDetailRecord.f20315e;
            if (str2 == null) {
                supportSQLiteStatement.Y(5);
            } else {
                supportSQLiteStatement.j(5, str2);
            }
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$i */
    /* loaded from: classes2.dex */
    class i extends SharedSQLiteStatement {
        i(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE appToShow SET isChecked = ? WHERE packageName == ? AND isBootStart == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$i0 */
    /* loaded from: classes2.dex */
    class i0 extends EntityDeletionOrUpdateAdapter<UnstableRecord> {
        i0(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE OR ABORT `unstable_record` SET `_id` = ?,`unstable_key` = ?,`user_id` = ?,`last_unstable_time` = ?,`restrict_begin_time` = ?,`frequent_unstable_count` = ?,`package_name` = ?,`exception_class` = ?,`exception_msg` = ?,`unstable_reason` = ?,`unstable_crash_time_upload` = ? WHERE `_id` = ?";
        }

        @Override // y.EntityDeletionOrUpdateAdapter
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public void g(SupportSQLiteStatement supportSQLiteStatement, UnstableRecord unstableRecord) {
            supportSQLiteStatement.y(1, unstableRecord.f20336a);
            String str = unstableRecord.f20337b;
            if (str == null) {
                supportSQLiteStatement.Y(2);
            } else {
                supportSQLiteStatement.j(2, str);
            }
            supportSQLiteStatement.y(3, unstableRecord.f20338c);
            supportSQLiteStatement.y(4, unstableRecord.f20339d);
            supportSQLiteStatement.y(5, unstableRecord.f20340e);
            supportSQLiteStatement.y(6, unstableRecord.f20341f);
            String str2 = unstableRecord.f20342g;
            if (str2 == null) {
                supportSQLiteStatement.Y(7);
            } else {
                supportSQLiteStatement.j(7, str2);
            }
            String str3 = unstableRecord.f20343h;
            if (str3 == null) {
                supportSQLiteStatement.Y(8);
            } else {
                supportSQLiteStatement.j(8, str3);
            }
            String str4 = unstableRecord.f20344i;
            if (str4 == null) {
                supportSQLiteStatement.Y(9);
            } else {
                supportSQLiteStatement.j(9, str4);
            }
            String str5 = unstableRecord.f20345j;
            if (str5 == null) {
                supportSQLiteStatement.Y(10);
            } else {
                supportSQLiteStatement.j(10, str5);
            }
            String str6 = unstableRecord.f20346k;
            if (str6 == null) {
                supportSQLiteStatement.Y(11);
            } else {
                supportSQLiteStatement.j(11, str6);
            }
            supportSQLiteStatement.y(12, unstableRecord.f20336a);
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$j */
    /* loaded from: classes2.dex */
    class j extends SharedSQLiteStatement {
        j(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE appToShow SET isSuggested = ? WHERE packageName == ? AND isBootStart == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$j0 */
    /* loaded from: classes2.dex */
    class j0 extends SharedSQLiteStatement {
        j0(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE record SET called_app = ? WHERE _id == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$k */
    /* loaded from: classes2.dex */
    class k extends EntityInsertionAdapter<Record> {
        k(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "INSERT OR REPLACE INTO `record`(`_id`,`called_pkg`,`caller_pkg`,`called_app`,`caller_app`,`launch_mode`,`launch_type`,`count`,`night_count`,`day_count`,`time`,`date`,`reason`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
        }

        @Override // y.EntityInsertionAdapter
        /* renamed from: l, reason: merged with bridge method [inline-methods] */
        public void g(SupportSQLiteStatement supportSQLiteStatement, Record record) {
            supportSQLiteStatement.y(1, record.f20323a);
            String str = record.f20324b;
            if (str == null) {
                supportSQLiteStatement.Y(2);
            } else {
                supportSQLiteStatement.j(2, str);
            }
            String str2 = record.f20325c;
            if (str2 == null) {
                supportSQLiteStatement.Y(3);
            } else {
                supportSQLiteStatement.j(3, str2);
            }
            String str3 = record.f20326d;
            if (str3 == null) {
                supportSQLiteStatement.Y(4);
            } else {
                supportSQLiteStatement.j(4, str3);
            }
            String str4 = record.f20327e;
            if (str4 == null) {
                supportSQLiteStatement.Y(5);
            } else {
                supportSQLiteStatement.j(5, str4);
            }
            String str5 = record.f20328f;
            if (str5 == null) {
                supportSQLiteStatement.Y(6);
            } else {
                supportSQLiteStatement.j(6, str5);
            }
            String str6 = record.f20329g;
            if (str6 == null) {
                supportSQLiteStatement.Y(7);
            } else {
                supportSQLiteStatement.j(7, str6);
            }
            supportSQLiteStatement.y(8, record.f20330h);
            supportSQLiteStatement.y(9, record.f20331i);
            supportSQLiteStatement.y(10, record.f20332j);
            supportSQLiteStatement.y(11, record.f20333k);
            supportSQLiteStatement.y(12, record.f20334l);
            String str7 = record.f20335m;
            if (str7 == null) {
                supportSQLiteStatement.Y(13);
            } else {
                supportSQLiteStatement.j(13, str7);
            }
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$k0 */
    /* loaded from: classes2.dex */
    class k0 extends SharedSQLiteStatement {
        k0(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE record SET caller_app = ? WHERE _id == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$l */
    /* loaded from: classes2.dex */
    class l extends SharedSQLiteStatement {
        l(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE appToShow SET label = ? WHERE packageName == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$l0 */
    /* loaded from: classes2.dex */
    class l0 extends SharedSQLiteStatement {
        l0(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE record SET caller_app = ? WHERE caller_pkg == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$m */
    /* loaded from: classes2.dex */
    class m extends SharedSQLiteStatement {
        m(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE  FROM appToShow WHERE packageName == ? AND isBootStart == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$n */
    /* loaded from: classes2.dex */
    class n extends SharedSQLiteStatement {
        n(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE  FROM appToShow WHERE packageName == ? ";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$o */
    /* loaded from: classes2.dex */
    class o extends SharedSQLiteStatement {
        o(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM appToShow";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$p */
    /* loaded from: classes2.dex */
    class p extends SharedSQLiteStatement {
        p(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE  FROM appToShow WHERE isBootStart == 'true'";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$q */
    /* loaded from: classes2.dex */
    class q extends SharedSQLiteStatement {
        q(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE  FROM appToShow WHERE isBootStart == 'false'";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$r */
    /* loaded from: classes2.dex */
    class r extends SharedSQLiteStatement {
        r(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE unstable_record SET frequent_unstable_count = ? , last_unstable_time = ?, unstable_crash_time_upload =?, frequent_unstable_count =? WHERE unstable_key == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$s */
    /* loaded from: classes2.dex */
    class s extends SharedSQLiteStatement {
        s(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM unstable_record WHERE unstable_key == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$t */
    /* loaded from: classes2.dex */
    class t extends SharedSQLiteStatement {
        t(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM sqlite_sequence WHERE name== ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$u */
    /* loaded from: classes2.dex */
    class u extends SharedSQLiteStatement {
        u(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM malicious_record WHERE called_pkg =? or caller_pkg =?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$v */
    /* loaded from: classes2.dex */
    class v extends EntityInsertionAdapter<AppToShow> {
        v(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "INSERT OR REPLACE INTO `AppToShow`(`_id`,`packageName`,`label`,`isBootBroadcast`,`isChecked`,`isSuggested`,`isBootStart`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
        }

        @Override // y.EntityInsertionAdapter
        /* renamed from: l, reason: merged with bridge method [inline-methods] */
        public void g(SupportSQLiteStatement supportSQLiteStatement, AppToShow appToShow) {
            supportSQLiteStatement.y(1, appToShow.f20304a);
            String str = appToShow.f20305b;
            if (str == null) {
                supportSQLiteStatement.Y(2);
            } else {
                supportSQLiteStatement.j(2, str);
            }
            String str2 = appToShow.f20306c;
            if (str2 == null) {
                supportSQLiteStatement.Y(3);
            } else {
                supportSQLiteStatement.j(3, str2);
            }
            supportSQLiteStatement.y(4, appToShow.f20307d ? 1L : 0L);
            supportSQLiteStatement.y(5, appToShow.f20308e ? 1L : 0L);
            supportSQLiteStatement.y(6, appToShow.f20309f ? 1L : 0L);
            supportSQLiteStatement.y(7, appToShow.f20310g ? 1L : 0L);
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$w */
    /* loaded from: classes2.dex */
    class w extends SharedSQLiteStatement {
        w(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM malicious_record WHERE date < ? OR date > ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$x */
    /* loaded from: classes2.dex */
    class x extends SharedSQLiteStatement {
        x(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "UPDATE malicious_record SET count = ? , time = ? , cpn = ? WHERE called_pkg == ? AND caller_pkg == ? AND date == ? AND cpn == ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$y */
    /* loaded from: classes2.dex */
    class y extends SharedSQLiteStatement {
        y(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM malicious_detail_record WHERE date = ?";
        }
    }

    /* compiled from: RecordDao_Impl.java */
    /* renamed from: y9.b$z */
    /* loaded from: classes2.dex */
    class z extends SharedSQLiteStatement {
        z(RoomDatabase roomDatabase) {
            super(roomDatabase);
        }

        @Override // y.SharedSQLiteStatement
        public String d() {
            return "DELETE FROM malicious_detail_record WHERE date < ? OR date > ?";
        }
    }

    public RecordDao_Impl(RoomDatabase roomDatabase) {
        this.f19932a = roomDatabase;
        this.f19933b = new k(roomDatabase);
        this.f19934c = new v(roomDatabase);
        this.f19935d = new f0(roomDatabase);
        this.f19936e = new g0(roomDatabase);
        this.f19937f = new h0(roomDatabase);
        this.f19938g = new i0(roomDatabase);
        this.f19939h = new j0(roomDatabase);
        this.f19940i = new k0(roomDatabase);
        this.f19941j = new l0(roomDatabase);
        this.f19942k = new a(roomDatabase);
        this.f19943l = new b(roomDatabase);
        this.f19944m = new c(roomDatabase);
        this.f19945n = new d(roomDatabase);
        this.f19946o = new e(roomDatabase);
        this.f19947p = new f(roomDatabase);
        this.f19948q = new g(roomDatabase);
        this.f19949r = new h(roomDatabase);
        this.f19950s = new i(roomDatabase);
        this.f19951t = new j(roomDatabase);
        this.f19952u = new l(roomDatabase);
        this.f19953v = new m(roomDatabase);
        this.f19954w = new n(roomDatabase);
        this.f19955x = new o(roomDatabase);
        this.f19956y = new p(roomDatabase);
        this.f19957z = new q(roomDatabase);
        this.A = new r(roomDatabase);
        this.B = new s(roomDatabase);
        this.C = new t(roomDatabase);
        this.D = new u(roomDatabase);
        this.E = new w(roomDatabase);
        this.F = new x(roomDatabase);
        this.G = new y(roomDatabase);
        this.H = new z(roomDatabase);
        this.I = new a0(roomDatabase);
    }

    @Override // y9.RecordDao
    public void A(AppToShow... appToShowArr) {
        this.f19932a.b();
        this.f19932a.c();
        try {
            this.f19934c.j(appToShowArr);
            this.f19932a.s();
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public List<Long> B(List<AppToShow> list) {
        this.f19932a.b();
        this.f19932a.c();
        try {
            List<Long> k10 = this.f19934c.k(list);
            this.f19932a.s();
            return k10;
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public void C(List<MaliciousDetailRecord> list) {
        this.f19932a.b();
        this.f19932a.c();
        try {
            this.f19937f.h(list);
            this.f19932a.s();
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public void D(MaliciousRecord maliciousRecord) {
        this.f19932a.b();
        this.f19932a.c();
        try {
            this.f19936e.i(maliciousRecord);
            this.f19932a.s();
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public void E(Record record) {
        this.f19932a.b();
        this.f19932a.c();
        try {
            this.f19933b.i(record);
            this.f19932a.s();
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public void F(UnstableRecord unstableRecord) {
        this.f19932a.b();
        this.f19932a.c();
        try {
            this.f19935d.i(unstableRecord);
            this.f19932a.s();
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public long G() {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT COUNT(*) FROM malicious_detail_record", 0);
        this.f19932a.b();
        Cursor b10 = DBUtil.b(this.f19932a, m10, false);
        try {
            return b10.moveToFirst() ? b10.getLong(0) : 0L;
        } finally {
            b10.close();
            m10.w();
        }
    }

    @Override // y9.RecordDao
    public List<MaliciousRecord> H(String str, String str2, long j10, String str3) {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM malicious_record WHERE called_pkg = ? AND caller_pkg =? AND date =? AND cpn =?", 4);
        if (str2 == null) {
            m10.Y(1);
        } else {
            m10.j(1, str2);
        }
        if (str == null) {
            m10.Y(2);
        } else {
            m10.j(2, str);
        }
        m10.y(3, j10);
        if (str3 == null) {
            m10.Y(4);
        } else {
            m10.j(4, str3);
        }
        this.f19932a.b();
        Cursor b10 = DBUtil.b(this.f19932a, m10, false);
        try {
            int b11 = CursorUtil.b(b10, "_id");
            int b12 = CursorUtil.b(b10, "called_pkg");
            int b13 = CursorUtil.b(b10, "caller_pkg");
            int b14 = CursorUtil.b(b10, "count");
            int b15 = CursorUtil.b(b10, "time");
            int b16 = CursorUtil.b(b10, "date");
            int b17 = CursorUtil.b(b10, "cpn");
            ArrayList arrayList = new ArrayList(b10.getCount());
            while (b10.moveToNext()) {
                MaliciousRecord maliciousRecord = new MaliciousRecord();
                maliciousRecord.f20316a = b10.getLong(b11);
                maliciousRecord.f20317b = b10.getString(b12);
                maliciousRecord.f20318c = b10.getString(b13);
                maliciousRecord.f20319d = b10.getLong(b14);
                maliciousRecord.f20320e = b10.getLong(b15);
                maliciousRecord.f20321f = b10.getLong(b16);
                maliciousRecord.f20322g = b10.getString(b17);
                arrayList.add(maliciousRecord);
            }
            return arrayList;
        } finally {
            b10.close();
            m10.w();
        }
    }

    @Override // y9.RecordDao
    public long I() {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT MIN(date) FROM malicious_detail_record", 0);
        this.f19932a.b();
        Cursor b10 = DBUtil.b(this.f19932a, m10, false);
        try {
            return b10.moveToFirst() ? b10.getLong(0) : 0L;
        } finally {
            b10.close();
            m10.w();
        }
    }

    @Override // y9.RecordDao
    public int J(boolean z10, List<String> list, boolean z11) {
        this.f19932a.b();
        StringBuilder b10 = StringUtil.b();
        b10.append("UPDATE appToShow SET isChecked = ");
        b10.append("?");
        b10.append(" WHERE packageName IN (");
        int size = list.size();
        StringUtil.a(b10, size);
        b10.append(") AND isBootStart == ");
        b10.append("?");
        SupportSQLiteStatement d10 = this.f19932a.d(b10.toString());
        d10.y(1, z10 ? 1L : 0L);
        int i10 = 2;
        for (String str : list) {
            if (str == null) {
                d10.Y(i10);
            } else {
                d10.j(i10, str);
            }
            i10++;
        }
        d10.y(size + 2, z11 ? 1L : 0L);
        this.f19932a.c();
        try {
            int l10 = d10.l();
            this.f19932a.s();
            return l10;
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public int K(boolean z10, String str, boolean z11) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.f19950s.a();
        a10.y(1, z10 ? 1L : 0L);
        if (str == null) {
            a10.Y(2);
        } else {
            a10.j(2, str);
        }
        a10.y(3, z11 ? 1L : 0L);
        this.f19932a.c();
        try {
            int l10 = a10.l();
            this.f19932a.s();
            return l10;
        } finally {
            this.f19932a.g();
            this.f19950s.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void L(ArrayMap<String, MaliciousRecord> arrayMap) {
        this.f19932a.c();
        try {
            super.L(arrayMap);
            this.f19932a.s();
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public void M(String str, String str2, long j10, long j11, long j12, String str3) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.F.a();
        a10.y(1, j12);
        a10.y(2, j11);
        if (str3 == null) {
            a10.Y(3);
        } else {
            a10.j(3, str3);
        }
        if (str2 == null) {
            a10.Y(4);
        } else {
            a10.j(4, str2);
        }
        if (str == null) {
            a10.Y(5);
        } else {
            a10.j(5, str);
        }
        a10.y(6, j10);
        if (str3 == null) {
            a10.Y(7);
        } else {
            a10.j(7, str3);
        }
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.F.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void N(String str, String str2, String str3, String str4, long j10, long j11, long j12, long j13, long j14, String str5) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.f19947p.a();
        a10.y(1, j12);
        a10.y(2, j11);
        a10.y(3, j13);
        a10.y(4, j14);
        if (str == null) {
            a10.Y(5);
        } else {
            a10.j(5, str);
        }
        if (str2 == null) {
            a10.Y(6);
        } else {
            a10.j(6, str2);
        }
        if (str3 == null) {
            a10.Y(7);
        } else {
            a10.j(7, str3);
        }
        if (str4 == null) {
            a10.Y(8);
        } else {
            a10.j(8, str4);
        }
        a10.y(9, j10);
        if (str5 == null) {
            a10.Y(10);
        } else {
            a10.j(10, str5);
        }
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.f19947p.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void O(UnstableRecord unstableRecord) {
        this.f19932a.b();
        this.f19932a.c();
        try {
            this.f19938g.h(unstableRecord);
            this.f19932a.s();
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public void a() {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.f19955x.a();
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.f19955x.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void b(long j10, long j11) {
        this.f19932a.c();
        try {
            super.b(j10, j11);
            this.f19932a.s();
        } finally {
            this.f19932a.g();
        }
    }

    @Override // y9.RecordDao
    public void c(String str) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.f19954w.a();
        if (str == null) {
            a10.Y(1);
        } else {
            a10.j(1, str);
        }
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.f19954w.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void d(long j10) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.G.a();
        a10.y(1, j10);
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.G.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void e(long j10, long j11) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.H.a();
        a10.y(1, j10);
        a10.y(2, j11);
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.H.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void f(long j10, long j11) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.E.a();
        a10.y(1, j10);
        a10.y(2, j11);
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.E.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void g(long j10, long j11) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.f19945n.a();
        a10.y(1, j10);
        a10.y(2, j11);
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.f19945n.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void h(long j10, String str) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.f19946o.a();
        a10.y(1, j10);
        if (str == null) {
            a10.Y(2);
        } else {
            a10.j(2, str);
        }
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.f19946o.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void i(String str) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.C.a();
        if (str == null) {
            a10.Y(1);
        } else {
            a10.j(1, str);
        }
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.C.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void j(String str) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.f19944m.a();
        if (str == null) {
            a10.Y(1);
        } else {
            a10.j(1, str);
        }
        if (str == null) {
            a10.Y(2);
        } else {
            a10.j(2, str);
        }
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.f19944m.f(a10);
        }
    }

    @Override // y9.RecordDao
    public void k(String str) {
        this.f19932a.b();
        SupportSQLiteStatement a10 = this.B.a();
        if (str == null) {
            a10.Y(1);
        } else {
            a10.j(1, str);
        }
        this.f19932a.c();
        try {
            a10.l();
            this.f19932a.s();
        } finally {
            this.f19932a.g();
            this.B.f(a10);
        }
    }

    @Override // y9.RecordDao
    public LiveData<List<AppToShow>> l(boolean z10, boolean z11) {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM AppToShow WHERE isBootStart == ? AND isSuggested == ?  ", 2);
        m10.y(1, z10 ? 1L : 0L);
        m10.y(2, z11 ? 1L : 0L);
        return this.f19932a.i().d(new String[]{"AppToShow"}, false, new d0(m10));
    }

    @Override // y9.RecordDao
    public List<AppToShow> m(boolean z10, boolean z11, boolean z12) {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM appToShow WHERE isBootStart == ? AND isSuggested == ? AND isChecked == ? ", 3);
        m10.y(1, z10 ? 1L : 0L);
        m10.y(2, z11 ? 1L : 0L);
        m10.y(3, z12 ? 1L : 0L);
        this.f19932a.b();
        Cursor b10 = DBUtil.b(this.f19932a, m10, false);
        try {
            int b11 = CursorUtil.b(b10, "_id");
            int b12 = CursorUtil.b(b10, "packageName");
            int b13 = CursorUtil.b(b10, "label");
            int b14 = CursorUtil.b(b10, "isBootBroadcast");
            int b15 = CursorUtil.b(b10, "isChecked");
            int b16 = CursorUtil.b(b10, "isSuggested");
            int b17 = CursorUtil.b(b10, "isBootStart");
            ArrayList arrayList = new ArrayList(b10.getCount());
            while (b10.moveToNext()) {
                AppToShow appToShow = new AppToShow();
                appToShow.f20304a = b10.getLong(b11);
                appToShow.f20305b = b10.getString(b12);
                appToShow.f20306c = b10.getString(b13);
                appToShow.f20307d = b10.getInt(b14) != 0;
                appToShow.f20308e = b10.getInt(b15) != 0;
                appToShow.f20309f = b10.getInt(b16) != 0;
                appToShow.f20310g = b10.getInt(b17) != 0;
                arrayList.add(appToShow);
            }
            return arrayList;
        } finally {
            b10.close();
            m10.w();
        }
    }

    @Override // y9.RecordDao
    public List<AppToShow> n(boolean z10) {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM appToShow WHERE isBootStart == ? ORDER BY  label ", 1);
        m10.y(1, z10 ? 1L : 0L);
        this.f19932a.b();
        Cursor b10 = DBUtil.b(this.f19932a, m10, false);
        try {
            int b11 = CursorUtil.b(b10, "_id");
            int b12 = CursorUtil.b(b10, "packageName");
            int b13 = CursorUtil.b(b10, "label");
            int b14 = CursorUtil.b(b10, "isBootBroadcast");
            int b15 = CursorUtil.b(b10, "isChecked");
            int b16 = CursorUtil.b(b10, "isSuggested");
            int b17 = CursorUtil.b(b10, "isBootStart");
            ArrayList arrayList = new ArrayList(b10.getCount());
            while (b10.moveToNext()) {
                AppToShow appToShow = new AppToShow();
                appToShow.f20304a = b10.getLong(b11);
                appToShow.f20305b = b10.getString(b12);
                appToShow.f20306c = b10.getString(b13);
                appToShow.f20307d = b10.getInt(b14) != 0;
                appToShow.f20308e = b10.getInt(b15) != 0;
                appToShow.f20309f = b10.getInt(b16) != 0;
                appToShow.f20310g = b10.getInt(b17) != 0;
                arrayList.add(appToShow);
            }
            return arrayList;
        } finally {
            b10.close();
            m10.w();
        }
    }

    @Override // y9.RecordDao
    public List<Record> o() {
        RoomSQLiteQuery roomSQLiteQuery;
        int b10;
        int b11;
        int b12;
        int b13;
        int b14;
        int b15;
        int b16;
        int b17;
        int b18;
        int b19;
        int b20;
        int b21;
        int b22;
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM record WHERE launch_mode == 1  ORDER BY time DESC", 0);
        this.f19932a.b();
        Cursor b23 = DBUtil.b(this.f19932a, m10, false);
        try {
            b10 = CursorUtil.b(b23, "_id");
            b11 = CursorUtil.b(b23, "called_pkg");
            b12 = CursorUtil.b(b23, "caller_pkg");
            b13 = CursorUtil.b(b23, "called_app");
            b14 = CursorUtil.b(b23, "caller_app");
            b15 = CursorUtil.b(b23, "launch_mode");
            b16 = CursorUtil.b(b23, "launch_type");
            b17 = CursorUtil.b(b23, "count");
            b18 = CursorUtil.b(b23, "night_count");
            b19 = CursorUtil.b(b23, "day_count");
            b20 = CursorUtil.b(b23, "time");
            b21 = CursorUtil.b(b23, "date");
            b22 = CursorUtil.b(b23, "reason");
            roomSQLiteQuery = m10;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = m10;
        }
        try {
            ArrayList arrayList = new ArrayList(b23.getCount());
            while (b23.moveToNext()) {
                Record record = new Record();
                record.f20323a = b23.getLong(b10);
                record.f20324b = b23.getString(b11);
                record.f20325c = b23.getString(b12);
                record.f20326d = b23.getString(b13);
                record.f20327e = b23.getString(b14);
                record.f20328f = b23.getString(b15);
                record.f20329g = b23.getString(b16);
                record.f20330h = b23.getLong(b17);
                record.f20331i = b23.getLong(b18);
                record.f20332j = b23.getLong(b19);
                record.f20333k = b23.getLong(b20);
                record.f20334l = b23.getLong(b21);
                b22 = b22;
                record.f20335m = b23.getString(b22);
                arrayList = arrayList;
                arrayList.add(record);
            }
            b23.close();
            roomSQLiteQuery.w();
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
            b23.close();
            roomSQLiteQuery.w();
            throw th;
        }
    }

    @Override // y9.RecordDao
    public LiveData<List<Record>> p() {
        return this.f19932a.i().d(new String[]{"record"}, false, new c0(RoomSQLiteQuery.m("SELECT * FROM record WHERE launch_mode == 1 ORDER BY time DESC", 0)));
    }

    @Override // y9.RecordDao
    public List<Record> q() {
        RoomSQLiteQuery roomSQLiteQuery;
        int b10;
        int b11;
        int b12;
        int b13;
        int b14;
        int b15;
        int b16;
        int b17;
        int b18;
        int b19;
        int b20;
        int b21;
        int b22;
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM record WHERE launch_mode == 0 OR launch_mode == 2 ORDER BY time DESC", 0);
        this.f19932a.b();
        Cursor b23 = DBUtil.b(this.f19932a, m10, false);
        try {
            b10 = CursorUtil.b(b23, "_id");
            b11 = CursorUtil.b(b23, "called_pkg");
            b12 = CursorUtil.b(b23, "caller_pkg");
            b13 = CursorUtil.b(b23, "called_app");
            b14 = CursorUtil.b(b23, "caller_app");
            b15 = CursorUtil.b(b23, "launch_mode");
            b16 = CursorUtil.b(b23, "launch_type");
            b17 = CursorUtil.b(b23, "count");
            b18 = CursorUtil.b(b23, "night_count");
            b19 = CursorUtil.b(b23, "day_count");
            b20 = CursorUtil.b(b23, "time");
            b21 = CursorUtil.b(b23, "date");
            b22 = CursorUtil.b(b23, "reason");
            roomSQLiteQuery = m10;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = m10;
        }
        try {
            ArrayList arrayList = new ArrayList(b23.getCount());
            while (b23.moveToNext()) {
                Record record = new Record();
                record.f20323a = b23.getLong(b10);
                record.f20324b = b23.getString(b11);
                record.f20325c = b23.getString(b12);
                record.f20326d = b23.getString(b13);
                record.f20327e = b23.getString(b14);
                record.f20328f = b23.getString(b15);
                record.f20329g = b23.getString(b16);
                record.f20330h = b23.getLong(b17);
                record.f20331i = b23.getLong(b18);
                record.f20332j = b23.getLong(b19);
                record.f20333k = b23.getLong(b20);
                record.f20334l = b23.getLong(b21);
                b22 = b22;
                record.f20335m = b23.getString(b22);
                arrayList = arrayList;
                arrayList.add(record);
            }
            b23.close();
            roomSQLiteQuery.w();
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
            b23.close();
            roomSQLiteQuery.w();
            throw th;
        }
    }

    @Override // y9.RecordDao
    public LiveData<List<Record>> r() {
        return this.f19932a.i().d(new String[]{"record"}, false, new b0(RoomSQLiteQuery.m("SELECT * FROM record WHERE launch_mode == 0 OR launch_mode == 2 ORDER BY time DESC", 0)));
    }

    @Override // y9.RecordDao
    public List<AppToShow> s(boolean z10, boolean z11) {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM appToShow WHERE isBootStart == ? AND isChecked == ?", 2);
        m10.y(1, z10 ? 1L : 0L);
        m10.y(2, z11 ? 1L : 0L);
        this.f19932a.b();
        Cursor b10 = DBUtil.b(this.f19932a, m10, false);
        try {
            int b11 = CursorUtil.b(b10, "_id");
            int b12 = CursorUtil.b(b10, "packageName");
            int b13 = CursorUtil.b(b10, "label");
            int b14 = CursorUtil.b(b10, "isBootBroadcast");
            int b15 = CursorUtil.b(b10, "isChecked");
            int b16 = CursorUtil.b(b10, "isSuggested");
            int b17 = CursorUtil.b(b10, "isBootStart");
            ArrayList arrayList = new ArrayList(b10.getCount());
            while (b10.moveToNext()) {
                AppToShow appToShow = new AppToShow();
                appToShow.f20304a = b10.getLong(b11);
                appToShow.f20305b = b10.getString(b12);
                appToShow.f20306c = b10.getString(b13);
                appToShow.f20307d = b10.getInt(b14) != 0;
                appToShow.f20308e = b10.getInt(b15) != 0;
                appToShow.f20309f = b10.getInt(b16) != 0;
                appToShow.f20310g = b10.getInt(b17) != 0;
                arrayList.add(appToShow);
            }
            return arrayList;
        } finally {
            b10.close();
            m10.w();
        }
    }

    @Override // y9.RecordDao
    public LiveData<List<AppToShow>> t(boolean z10, boolean z11, boolean z12) {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM appToShow WHERE isBootStart == ? AND isSuggested == ? AND isChecked == ? ", 3);
        m10.y(1, z10 ? 1L : 0L);
        m10.y(2, z11 ? 1L : 0L);
        m10.y(3, z12 ? 1L : 0L);
        return this.f19932a.i().d(new String[]{"appToShow"}, false, new e0(m10));
    }

    @Override // y9.RecordDao
    public List<AppToShow> u(boolean z10, boolean z11, boolean z12) {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM appToShow WHERE isBootStart == ? AND isSuggested == ? AND isChecked == ? ", 3);
        m10.y(1, z10 ? 1L : 0L);
        m10.y(2, z11 ? 1L : 0L);
        m10.y(3, z12 ? 1L : 0L);
        this.f19932a.b();
        Cursor b10 = DBUtil.b(this.f19932a, m10, false);
        try {
            int b11 = CursorUtil.b(b10, "_id");
            int b12 = CursorUtil.b(b10, "packageName");
            int b13 = CursorUtil.b(b10, "label");
            int b14 = CursorUtil.b(b10, "isBootBroadcast");
            int b15 = CursorUtil.b(b10, "isChecked");
            int b16 = CursorUtil.b(b10, "isSuggested");
            int b17 = CursorUtil.b(b10, "isBootStart");
            ArrayList arrayList = new ArrayList(b10.getCount());
            while (b10.moveToNext()) {
                AppToShow appToShow = new AppToShow();
                appToShow.f20304a = b10.getLong(b11);
                appToShow.f20305b = b10.getString(b12);
                appToShow.f20306c = b10.getString(b13);
                appToShow.f20307d = b10.getInt(b14) != 0;
                appToShow.f20308e = b10.getInt(b15) != 0;
                appToShow.f20309f = b10.getInt(b16) != 0;
                appToShow.f20310g = b10.getInt(b17) != 0;
                arrayList.add(appToShow);
            }
            return arrayList;
        } finally {
            b10.close();
            m10.w();
        }
    }

    @Override // y9.RecordDao
    public List<Record> v(String str, Long l10) {
        RoomSQLiteQuery roomSQLiteQuery;
        int b10;
        int b11;
        int b12;
        int b13;
        int b14;
        int b15;
        int b16;
        int b17;
        int b18;
        int b19;
        int b20;
        int b21;
        int b22;
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM record WHERE launch_mode == ? AND date == ?", 2);
        if (str == null) {
            m10.Y(1);
        } else {
            m10.j(1, str);
        }
        if (l10 == null) {
            m10.Y(2);
        } else {
            m10.y(2, l10.longValue());
        }
        this.f19932a.b();
        Cursor b23 = DBUtil.b(this.f19932a, m10, false);
        try {
            b10 = CursorUtil.b(b23, "_id");
            b11 = CursorUtil.b(b23, "called_pkg");
            b12 = CursorUtil.b(b23, "caller_pkg");
            b13 = CursorUtil.b(b23, "called_app");
            b14 = CursorUtil.b(b23, "caller_app");
            b15 = CursorUtil.b(b23, "launch_mode");
            b16 = CursorUtil.b(b23, "launch_type");
            b17 = CursorUtil.b(b23, "count");
            b18 = CursorUtil.b(b23, "night_count");
            b19 = CursorUtil.b(b23, "day_count");
            b20 = CursorUtil.b(b23, "time");
            b21 = CursorUtil.b(b23, "date");
            b22 = CursorUtil.b(b23, "reason");
            roomSQLiteQuery = m10;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = m10;
        }
        try {
            ArrayList arrayList = new ArrayList(b23.getCount());
            while (b23.moveToNext()) {
                Record record = new Record();
                record.f20323a = b23.getLong(b10);
                record.f20324b = b23.getString(b11);
                record.f20325c = b23.getString(b12);
                record.f20326d = b23.getString(b13);
                record.f20327e = b23.getString(b14);
                record.f20328f = b23.getString(b15);
                record.f20329g = b23.getString(b16);
                record.f20330h = b23.getLong(b17);
                record.f20331i = b23.getLong(b18);
                record.f20332j = b23.getLong(b19);
                record.f20333k = b23.getLong(b20);
                record.f20334l = b23.getLong(b21);
                b22 = b22;
                record.f20335m = b23.getString(b22);
                arrayList = arrayList;
                arrayList.add(record);
            }
            b23.close();
            roomSQLiteQuery.w();
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
            b23.close();
            roomSQLiteQuery.w();
            throw th;
        }
    }

    @Override // y9.RecordDao
    public List<Record> w(String str, Long l10, Long l11) {
        RoomSQLiteQuery roomSQLiteQuery;
        int b10;
        int b11;
        int b12;
        int b13;
        int b14;
        int b15;
        int b16;
        int b17;
        int b18;
        int b19;
        int b20;
        int b21;
        int b22;
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM record WHERE launch_mode == ? AND date >= ? AND date <= ?", 3);
        if (str == null) {
            m10.Y(1);
        } else {
            m10.j(1, str);
        }
        if (l10 == null) {
            m10.Y(2);
        } else {
            m10.y(2, l10.longValue());
        }
        if (l11 == null) {
            m10.Y(3);
        } else {
            m10.y(3, l11.longValue());
        }
        this.f19932a.b();
        Cursor b23 = DBUtil.b(this.f19932a, m10, false);
        try {
            b10 = CursorUtil.b(b23, "_id");
            b11 = CursorUtil.b(b23, "called_pkg");
            b12 = CursorUtil.b(b23, "caller_pkg");
            b13 = CursorUtil.b(b23, "called_app");
            b14 = CursorUtil.b(b23, "caller_app");
            b15 = CursorUtil.b(b23, "launch_mode");
            b16 = CursorUtil.b(b23, "launch_type");
            b17 = CursorUtil.b(b23, "count");
            b18 = CursorUtil.b(b23, "night_count");
            b19 = CursorUtil.b(b23, "day_count");
            b20 = CursorUtil.b(b23, "time");
            b21 = CursorUtil.b(b23, "date");
            b22 = CursorUtil.b(b23, "reason");
            roomSQLiteQuery = m10;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = m10;
        }
        try {
            ArrayList arrayList = new ArrayList(b23.getCount());
            while (b23.moveToNext()) {
                Record record = new Record();
                record.f20323a = b23.getLong(b10);
                record.f20324b = b23.getString(b11);
                record.f20325c = b23.getString(b12);
                record.f20326d = b23.getString(b13);
                record.f20327e = b23.getString(b14);
                record.f20328f = b23.getString(b15);
                record.f20329g = b23.getString(b16);
                record.f20330h = b23.getLong(b17);
                record.f20331i = b23.getLong(b18);
                record.f20332j = b23.getLong(b19);
                record.f20333k = b23.getLong(b20);
                record.f20334l = b23.getLong(b21);
                b22 = b22;
                record.f20335m = b23.getString(b22);
                arrayList = arrayList;
                arrayList.add(record);
            }
            b23.close();
            roomSQLiteQuery.w();
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
            b23.close();
            roomSQLiteQuery.w();
            throw th;
        }
    }

    @Override // y9.RecordDao
    public List<Record> x(String str, String str2, String str3, String str4, Long l10, String str5) {
        RoomSQLiteQuery roomSQLiteQuery;
        int b10;
        int b11;
        int b12;
        int b13;
        int b14;
        int b15;
        int b16;
        int b17;
        int b18;
        int b19;
        int b20;
        int b21;
        int b22;
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM record WHERE called_pkg == ? AND caller_pkg == ? AND launch_mode == ? AND launch_type == ? AND date == ? AND reason == ?", 6);
        if (str == null) {
            m10.Y(1);
        } else {
            m10.j(1, str);
        }
        if (str2 == null) {
            m10.Y(2);
        } else {
            m10.j(2, str2);
        }
        if (str3 == null) {
            m10.Y(3);
        } else {
            m10.j(3, str3);
        }
        if (str4 == null) {
            m10.Y(4);
        } else {
            m10.j(4, str4);
        }
        if (l10 == null) {
            m10.Y(5);
        } else {
            m10.y(5, l10.longValue());
        }
        if (str5 == null) {
            m10.Y(6);
        } else {
            m10.j(6, str5);
        }
        this.f19932a.b();
        Cursor b23 = DBUtil.b(this.f19932a, m10, false);
        try {
            b10 = CursorUtil.b(b23, "_id");
            b11 = CursorUtil.b(b23, "called_pkg");
            b12 = CursorUtil.b(b23, "caller_pkg");
            b13 = CursorUtil.b(b23, "called_app");
            b14 = CursorUtil.b(b23, "caller_app");
            b15 = CursorUtil.b(b23, "launch_mode");
            b16 = CursorUtil.b(b23, "launch_type");
            b17 = CursorUtil.b(b23, "count");
            b18 = CursorUtil.b(b23, "night_count");
            b19 = CursorUtil.b(b23, "day_count");
            b20 = CursorUtil.b(b23, "time");
            b21 = CursorUtil.b(b23, "date");
            b22 = CursorUtil.b(b23, "reason");
            roomSQLiteQuery = m10;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = m10;
        }
        try {
            ArrayList arrayList = new ArrayList(b23.getCount());
            while (b23.moveToNext()) {
                Record record = new Record();
                record.f20323a = b23.getLong(b10);
                record.f20324b = b23.getString(b11);
                record.f20325c = b23.getString(b12);
                record.f20326d = b23.getString(b13);
                record.f20327e = b23.getString(b14);
                record.f20328f = b23.getString(b15);
                record.f20329g = b23.getString(b16);
                record.f20330h = b23.getLong(b17);
                record.f20331i = b23.getLong(b18);
                record.f20332j = b23.getLong(b19);
                record.f20333k = b23.getLong(b20);
                record.f20334l = b23.getLong(b21);
                b22 = b22;
                record.f20335m = b23.getString(b22);
                arrayList = arrayList;
                arrayList.add(record);
            }
            b23.close();
            roomSQLiteQuery.w();
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
            b23.close();
            roomSQLiteQuery.w();
            throw th;
        }
    }

    @Override // y9.RecordDao
    public List<UnstableRecord> y() {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM unstable_record ", 0);
        this.f19932a.b();
        Cursor b10 = DBUtil.b(this.f19932a, m10, false);
        try {
            int b11 = CursorUtil.b(b10, "_id");
            int b12 = CursorUtil.b(b10, "unstable_key");
            int b13 = CursorUtil.b(b10, "user_id");
            int b14 = CursorUtil.b(b10, "last_unstable_time");
            int b15 = CursorUtil.b(b10, "restrict_begin_time");
            int b16 = CursorUtil.b(b10, "frequent_unstable_count");
            int b17 = CursorUtil.b(b10, "package_name");
            int b18 = CursorUtil.b(b10, "exception_class");
            int b19 = CursorUtil.b(b10, "exception_msg");
            int b20 = CursorUtil.b(b10, "unstable_reason");
            int b21 = CursorUtil.b(b10, "unstable_crash_time_upload");
            ArrayList arrayList = new ArrayList(b10.getCount());
            while (b10.moveToNext()) {
                String string = b10.getString(b12);
                UnstableRecord unstableRecord = new UnstableRecord(b10.getLong(b13), b10.getLong(b14), b10.getLong(b15), b10.getLong(b16), b10.getString(b18), b10.getString(b19), string, b10.getString(b20), b10.getString(b21), b10.getString(b17));
                int i10 = b12;
                int i11 = b13;
                unstableRecord.f20336a = b10.getLong(b11);
                arrayList.add(unstableRecord);
                b12 = i10;
                b13 = i11;
            }
            return arrayList;
        } finally {
            b10.close();
            m10.w();
        }
    }

    @Override // y9.RecordDao
    public List<UnstableRecord> z(String str) {
        RoomSQLiteQuery m10 = RoomSQLiteQuery.m("SELECT * FROM unstable_record WHERE unstable_key == ?", 1);
        if (str == null) {
            m10.Y(1);
        } else {
            m10.j(1, str);
        }
        this.f19932a.b();
        Cursor b10 = DBUtil.b(this.f19932a, m10, false);
        try {
            int b11 = CursorUtil.b(b10, "_id");
            int b12 = CursorUtil.b(b10, "unstable_key");
            int b13 = CursorUtil.b(b10, "user_id");
            int b14 = CursorUtil.b(b10, "last_unstable_time");
            int b15 = CursorUtil.b(b10, "restrict_begin_time");
            int b16 = CursorUtil.b(b10, "frequent_unstable_count");
            int b17 = CursorUtil.b(b10, "package_name");
            int b18 = CursorUtil.b(b10, "exception_class");
            int b19 = CursorUtil.b(b10, "exception_msg");
            int b20 = CursorUtil.b(b10, "unstable_reason");
            int b21 = CursorUtil.b(b10, "unstable_crash_time_upload");
            ArrayList arrayList = new ArrayList(b10.getCount());
            while (b10.moveToNext()) {
                String string = b10.getString(b12);
                UnstableRecord unstableRecord = new UnstableRecord(b10.getLong(b13), b10.getLong(b14), b10.getLong(b15), b10.getLong(b16), b10.getString(b18), b10.getString(b19), string, b10.getString(b20), b10.getString(b21), b10.getString(b17));
                int i10 = b12;
                int i11 = b13;
                unstableRecord.f20336a = b10.getLong(b11);
                arrayList.add(unstableRecord);
                b12 = i10;
                b13 = i11;
            }
            return arrayList;
        } finally {
            b10.close();
            m10.w();
        }
    }
}
