package com.oplus.startupapp.data.database;

import a0.DBUtil;
import a0.TableInfo;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import c0.SupportSQLiteDatabase;
import c0.SupportSQLiteOpenHelper;
import java.util.HashMap;
import java.util.HashSet;
import y9.RecordDao;
import y9.RecordDao_Impl;

/* loaded from: classes2.dex */
public final class RecordDatabase_Impl extends RecordDatabase {

    /* renamed from: p, reason: collision with root package name */
    private volatile RecordDao f10510p;

    /* loaded from: classes2.dex */
    class a extends RoomOpenHelper.a {
        a(int i10) {
            super(i10);
        }

        @Override // androidx.room.RoomOpenHelper.a
        public void a(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS `record` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `called_pkg` TEXT, `caller_pkg` TEXT, `called_app` TEXT, `caller_app` TEXT, `launch_mode` TEXT, `launch_type` TEXT, `count` INTEGER NOT NULL, `night_count` INTEGER NOT NULL, `day_count` INTEGER NOT NULL, `time` INTEGER NOT NULL, `date` INTEGER NOT NULL, `reason` TEXT)");
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS `AppToShow` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT, `label` TEXT, `isBootBroadcast` INTEGER NOT NULL, `isChecked` INTEGER NOT NULL, `isSuggested` INTEGER NOT NULL, `isBootStart` INTEGER NOT NULL)");
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS `unstable_record` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `unstable_key` TEXT, `user_id` INTEGER NOT NULL, `last_unstable_time` INTEGER NOT NULL, `restrict_begin_time` INTEGER NOT NULL, `frequent_unstable_count` INTEGER NOT NULL, `package_name` TEXT, `exception_class` TEXT, `exception_msg` TEXT, `unstable_reason` TEXT, `unstable_crash_time_upload` TEXT)");
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS `malicious_record` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `called_pkg` TEXT, `caller_pkg` TEXT, `count` INTEGER NOT NULL, `time` INTEGER NOT NULL, `date` INTEGER NOT NULL, `cpn` TEXT)");
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS `malicious_detail_record` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `called_pkg` TEXT, `time` INTEGER NOT NULL, `date` INTEGER NOT NULL, `type` TEXT)");
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            supportSQLiteDatabase.i("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '21d154f5d21382772d43a3c74551eb41')");
        }

        @Override // androidx.room.RoomOpenHelper.a
        public void b(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.i("DROP TABLE IF EXISTS `record`");
            supportSQLiteDatabase.i("DROP TABLE IF EXISTS `AppToShow`");
            supportSQLiteDatabase.i("DROP TABLE IF EXISTS `unstable_record`");
            supportSQLiteDatabase.i("DROP TABLE IF EXISTS `malicious_record`");
            supportSQLiteDatabase.i("DROP TABLE IF EXISTS `malicious_detail_record`");
        }

        @Override // androidx.room.RoomOpenHelper.a
        protected void c(SupportSQLiteDatabase supportSQLiteDatabase) {
            if (((RoomDatabase) RecordDatabase_Impl.this).f3900h != null) {
                int size = ((RoomDatabase) RecordDatabase_Impl.this).f3900h.size();
                for (int i10 = 0; i10 < size; i10++) {
                    ((RoomDatabase.b) ((RoomDatabase) RecordDatabase_Impl.this).f3900h.get(i10)).a(supportSQLiteDatabase);
                }
            }
        }

        @Override // androidx.room.RoomOpenHelper.a
        public void d(SupportSQLiteDatabase supportSQLiteDatabase) {
            ((RoomDatabase) RecordDatabase_Impl.this).f3893a = supportSQLiteDatabase;
            RecordDatabase_Impl.this.o(supportSQLiteDatabase);
            if (((RoomDatabase) RecordDatabase_Impl.this).f3900h != null) {
                int size = ((RoomDatabase) RecordDatabase_Impl.this).f3900h.size();
                for (int i10 = 0; i10 < size; i10++) {
                    ((RoomDatabase.b) ((RoomDatabase) RecordDatabase_Impl.this).f3900h.get(i10)).b(supportSQLiteDatabase);
                }
            }
        }

        @Override // androidx.room.RoomOpenHelper.a
        public void e(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        @Override // androidx.room.RoomOpenHelper.a
        public void f(SupportSQLiteDatabase supportSQLiteDatabase) {
            DBUtil.a(supportSQLiteDatabase);
        }

        @Override // androidx.room.RoomOpenHelper.a
        protected void g(SupportSQLiteDatabase supportSQLiteDatabase) {
            HashMap hashMap = new HashMap(13);
            hashMap.put("_id", new TableInfo.a("_id", "INTEGER", true, 1));
            hashMap.put("called_pkg", new TableInfo.a("called_pkg", "TEXT", false, 0));
            hashMap.put("caller_pkg", new TableInfo.a("caller_pkg", "TEXT", false, 0));
            hashMap.put("called_app", new TableInfo.a("called_app", "TEXT", false, 0));
            hashMap.put("caller_app", new TableInfo.a("caller_app", "TEXT", false, 0));
            hashMap.put("launch_mode", new TableInfo.a("launch_mode", "TEXT", false, 0));
            hashMap.put("launch_type", new TableInfo.a("launch_type", "TEXT", false, 0));
            hashMap.put("count", new TableInfo.a("count", "INTEGER", true, 0));
            hashMap.put("night_count", new TableInfo.a("night_count", "INTEGER", true, 0));
            hashMap.put("day_count", new TableInfo.a("day_count", "INTEGER", true, 0));
            hashMap.put("time", new TableInfo.a("time", "INTEGER", true, 0));
            hashMap.put("date", new TableInfo.a("date", "INTEGER", true, 0));
            hashMap.put("reason", new TableInfo.a("reason", "TEXT", false, 0));
            TableInfo tableInfo = new TableInfo("record", hashMap, new HashSet(0), new HashSet(0));
            TableInfo a10 = TableInfo.a(supportSQLiteDatabase, "record");
            if (tableInfo.equals(a10)) {
                HashMap hashMap2 = new HashMap(7);
                hashMap2.put("_id", new TableInfo.a("_id", "INTEGER", true, 1));
                hashMap2.put("packageName", new TableInfo.a("packageName", "TEXT", false, 0));
                hashMap2.put("label", new TableInfo.a("label", "TEXT", false, 0));
                hashMap2.put("isBootBroadcast", new TableInfo.a("isBootBroadcast", "INTEGER", true, 0));
                hashMap2.put("isChecked", new TableInfo.a("isChecked", "INTEGER", true, 0));
                hashMap2.put("isSuggested", new TableInfo.a("isSuggested", "INTEGER", true, 0));
                hashMap2.put("isBootStart", new TableInfo.a("isBootStart", "INTEGER", true, 0));
                TableInfo tableInfo2 = new TableInfo("AppToShow", hashMap2, new HashSet(0), new HashSet(0));
                TableInfo a11 = TableInfo.a(supportSQLiteDatabase, "AppToShow");
                if (tableInfo2.equals(a11)) {
                    HashMap hashMap3 = new HashMap(11);
                    hashMap3.put("_id", new TableInfo.a("_id", "INTEGER", true, 1));
                    hashMap3.put("unstable_key", new TableInfo.a("unstable_key", "TEXT", false, 0));
                    hashMap3.put("user_id", new TableInfo.a("user_id", "INTEGER", true, 0));
                    hashMap3.put("last_unstable_time", new TableInfo.a("last_unstable_time", "INTEGER", true, 0));
                    hashMap3.put("restrict_begin_time", new TableInfo.a("restrict_begin_time", "INTEGER", true, 0));
                    hashMap3.put("frequent_unstable_count", new TableInfo.a("frequent_unstable_count", "INTEGER", true, 0));
                    hashMap3.put("package_name", new TableInfo.a("package_name", "TEXT", false, 0));
                    hashMap3.put("exception_class", new TableInfo.a("exception_class", "TEXT", false, 0));
                    hashMap3.put("exception_msg", new TableInfo.a("exception_msg", "TEXT", false, 0));
                    hashMap3.put("unstable_reason", new TableInfo.a("unstable_reason", "TEXT", false, 0));
                    hashMap3.put("unstable_crash_time_upload", new TableInfo.a("unstable_crash_time_upload", "TEXT", false, 0));
                    TableInfo tableInfo3 = new TableInfo("unstable_record", hashMap3, new HashSet(0), new HashSet(0));
                    TableInfo a12 = TableInfo.a(supportSQLiteDatabase, "unstable_record");
                    if (tableInfo3.equals(a12)) {
                        HashMap hashMap4 = new HashMap(7);
                        hashMap4.put("_id", new TableInfo.a("_id", "INTEGER", true, 1));
                        hashMap4.put("called_pkg", new TableInfo.a("called_pkg", "TEXT", false, 0));
                        hashMap4.put("caller_pkg", new TableInfo.a("caller_pkg", "TEXT", false, 0));
                        hashMap4.put("count", new TableInfo.a("count", "INTEGER", true, 0));
                        hashMap4.put("time", new TableInfo.a("time", "INTEGER", true, 0));
                        hashMap4.put("date", new TableInfo.a("date", "INTEGER", true, 0));
                        hashMap4.put("cpn", new TableInfo.a("cpn", "TEXT", false, 0));
                        TableInfo tableInfo4 = new TableInfo("malicious_record", hashMap4, new HashSet(0), new HashSet(0));
                        TableInfo a13 = TableInfo.a(supportSQLiteDatabase, "malicious_record");
                        if (tableInfo4.equals(a13)) {
                            HashMap hashMap5 = new HashMap(5);
                            hashMap5.put("_id", new TableInfo.a("_id", "INTEGER", true, 1));
                            hashMap5.put("called_pkg", new TableInfo.a("called_pkg", "TEXT", false, 0));
                            hashMap5.put("time", new TableInfo.a("time", "INTEGER", true, 0));
                            hashMap5.put("date", new TableInfo.a("date", "INTEGER", true, 0));
                            hashMap5.put("type", new TableInfo.a("type", "TEXT", false, 0));
                            TableInfo tableInfo5 = new TableInfo("malicious_detail_record", hashMap5, new HashSet(0), new HashSet(0));
                            TableInfo a14 = TableInfo.a(supportSQLiteDatabase, "malicious_detail_record");
                            if (tableInfo5.equals(a14)) {
                                return;
                            }
                            throw new IllegalStateException("Migration didn't properly handle malicious_detail_record(com.oplus.startupapp.data.database.table.MaliciousDetailRecord).\n Expected:\n" + tableInfo5 + "\n Found:\n" + a14);
                        }
                        throw new IllegalStateException("Migration didn't properly handle malicious_record(com.oplus.startupapp.data.database.table.MaliciousRecord).\n Expected:\n" + tableInfo4 + "\n Found:\n" + a13);
                    }
                    throw new IllegalStateException("Migration didn't properly handle unstable_record(com.oplus.startupapp.data.database.table.UnstableRecord).\n Expected:\n" + tableInfo3 + "\n Found:\n" + a12);
                }
                throw new IllegalStateException("Migration didn't properly handle AppToShow(com.oplus.startupapp.data.database.table.AppToShow).\n Expected:\n" + tableInfo2 + "\n Found:\n" + a11);
            }
            throw new IllegalStateException("Migration didn't properly handle record(com.oplus.startupapp.data.database.table.Record).\n Expected:\n" + tableInfo + "\n Found:\n" + a10);
        }
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker e() {
        return new InvalidationTracker(this, new HashMap(0), new HashMap(0), "record", "AppToShow", "unstable_record", "malicious_record", "malicious_detail_record");
    }

    @Override // androidx.room.RoomDatabase
    protected SupportSQLiteOpenHelper f(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.f3829a.a(SupportSQLiteOpenHelper.b.a(databaseConfiguration.f3830b).c(databaseConfiguration.f3831c).b(new RoomOpenHelper(databaseConfiguration, new a(4), "21d154f5d21382772d43a3c74551eb41", "7c05f4df93468e9ad004b1d465d50ec5")).a());
    }

    @Override // com.oplus.startupapp.data.database.RecordDatabase
    public RecordDao v() {
        RecordDao recordDao;
        if (this.f10510p != null) {
            return this.f10510p;
        }
        synchronized (this) {
            if (this.f10510p == null) {
                this.f10510p = new RecordDao_Impl(this);
            }
            recordDao = this.f10510p;
        }
        return recordDao;
    }
}
