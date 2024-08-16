package com.oplus.startupapp.data.database;

import android.content.Context;
import android.util.Log;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import c0.SupportSQLiteDatabase;
import y9.RecordDao;
import z.Migration;

/* loaded from: classes2.dex */
public abstract class RecordDatabase extends RoomDatabase {

    /* renamed from: l, reason: collision with root package name */
    private static final Migration f10506l = new a(1, 3);

    /* renamed from: m, reason: collision with root package name */
    private static final Migration f10507m = new b(2, 3);

    /* renamed from: n, reason: collision with root package name */
    private static final Migration f10508n = new c(3, 4);

    /* renamed from: o, reason: collision with root package name */
    private static volatile RecordDatabase f10509o;

    /* loaded from: classes2.dex */
    class a extends Migration {
        a(int i10, int i11) {
            super(i10, i11);
        }

        @Override // z.Migration
        public void a(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.i("create table if not exists malicious_record (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,caller_pkg TEXT ,called_pkg TEXT ,date INTEGER NOT NULL DEFAULT 0,time INTEGER NOT NULL DEFAULT 0,count INTEGER NOT NULL DEFAULT 0,cpn TEXT );");
            supportSQLiteDatabase.i("create table if not exists malicious_detail_record (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,called_pkg TEXT ,time INTEGER NOT NULL DEFAULT 0,date INTEGER NOT NULL DEFAULT 0,type TEXT );");
        }
    }

    /* loaded from: classes2.dex */
    class b extends Migration {
        b(int i10, int i11) {
            super(i10, i11);
        }

        @Override // z.Migration
        public void a(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.i("ALTER TABLE malicious_record ADD COLUMN  cpn TEXT ");
            supportSQLiteDatabase.i("create table if not exists malicious_detail_record (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,called_pkg TEXT ,time INTEGER NOT NULL DEFAULT 0,date INTEGER NOT NULL DEFAULT 0,type TEXT );");
        }
    }

    /* loaded from: classes2.dex */
    class c extends Migration {
        c(int i10, int i11) {
            super(i10, i11);
        }

        @Override // z.Migration
        public void a(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.i("ALTER TABLE record ADD COLUMN  day_count INTEGER NOT NULL DEFAULT 0 ");
            supportSQLiteDatabase.i("ALTER TABLE record ADD COLUMN  night_count INTEGER NOT NULL DEFAULT 0 ");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class d extends RoomDatabase.b {
        d() {
        }

        @Override // androidx.room.RoomDatabase.b
        public void a(SupportSQLiteDatabase supportSQLiteDatabase) {
            super.a(supportSQLiteDatabase);
            supportSQLiteDatabase.i("create table if not exists record (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,caller_pkg TEXT ,called_pkg TEXT ,launch_mode TEXT ,launch_type TEXT ,reason TEXT ,date INTEGER NOT NULL DEFAULT 0,time INTEGER NOT NULL DEFAULT 0,count INTEGER NOT NULL DEFAULT 0,day_count INTEGER NOT NULL DEFAULT 0,night_count INTEGER NOT NULL DEFAULT 0);");
            supportSQLiteDatabase.i("create table if not exists appToShow (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,packageName TEXT ,label TEXT ,isBootBroadcast INTEGER NOT NULL DEFAULT 0 ,isChecked INTEGER NOT NULL DEFAULT 1,isSuggested INTEGER NOT NULL DEFAULT 0,isBootStart INTEGER NOT NULL DEFAULT 0);");
            supportSQLiteDatabase.i("create table if not exists unstable_record (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,unstable_key TEXT ,package_name TEXT ,user_id INTEGER NOT NULL,exception_class TEXT ,exception_msg TEXT ,last_unstable_time INTEGER NOT NULL,frequent_unstable_count INTEGER NOT NULL,restrict_begin_time INTEGER NOT NULL,unstable_reason TEXT ,unstable_crash_time_upload TEXT );");
            supportSQLiteDatabase.i("create table if not exists malicious_record (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,caller_pkg TEXT ,called_pkg TEXT ,date INTEGER NOT NULL DEFAULT 0,time INTEGER NOT NULL DEFAULT 0,count INTEGER NOT NULL DEFAULT 0,cpn TEXT );");
            supportSQLiteDatabase.i("create table if not exists malicious_detail_record (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,called_pkg TEXT ,time INTEGER NOT NULL DEFAULT 0,date INTEGER NOT NULL DEFAULT 0,type TEXT );");
            Log.d("StartupManager", "onCreate: ---record.db");
        }

        @Override // androidx.room.RoomDatabase.b
        public void b(SupportSQLiteDatabase supportSQLiteDatabase) {
            super.b(supportSQLiteDatabase);
            Log.d("StartupManager", "onOpen: ----record.db");
        }
    }

    private static RecordDatabase t(Context context) {
        return (RecordDatabase) Room.a(context, RecordDatabase.class, "record.db").a(new d()).e().c().f().b(f10506l, f10507m, f10508n).d();
    }

    public static RecordDatabase u(Context context) {
        if (f10509o == null) {
            synchronized (RecordDatabase.class) {
                if (f10509o == null) {
                    f10509o = t(context);
                }
            }
        }
        return f10509o;
    }

    public abstract RecordDao v();
}
