package com.oplus.oguard.data.database;

import a0.DBUtil;
import a0.TableInfo;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import c0.SupportSQLiteDatabase;
import c0.SupportSQLiteOpenHelper;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.deepthinker.sdk.app.awareness.capability.impl.AppUseTimeEvent;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import i7.AppPowerRecordDao;
import i7.AppPowerRecordDao_Impl;
import java.util.HashMap;
import java.util.HashSet;

/* loaded from: classes.dex */
public final class OGuardDataBase_Impl extends OGuardDataBase {

    /* renamed from: m, reason: collision with root package name */
    private volatile AppPowerRecordDao f9944m;

    /* loaded from: classes.dex */
    class a extends RoomOpenHelper.a {
        a(int i10) {
            super(i10);
        }

        @Override // androidx.room.RoomOpenHelper.a
        public void a(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS `app_power_record` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `valid` INTEGER NOT NULL, `uid` INTEGER NOT NULL, `app_name` TEXT, `cpu_time` INTEGER NOT NULL, `small_cpu_time` INTEGER NOT NULL, `middle_cpu_time` INTEGER NOT NULL, `big_cpu_time` INTEGER NOT NULL, `alarm_wakeup_count` INTEGER NOT NULL, `wakelock_time` INTEGER NOT NULL, `gps_time` INTEGER NOT NULL, `sensor_time` INTEGER NOT NULL, `wifi_time` INTEGER NOT NULL, `wifi_bytes` INTEGER NOT NULL, `wifi_scan_count` INTEGER NOT NULL, `cell_time` INTEGER NOT NULL, `cell_bytes` INTEGER NOT NULL, `traffic_count` INTEGER NOT NULL, `bt_scan_time` INTEGER NOT NULL, `background_duration` INTEGER NOT NULL, `start_time` TEXT, `end_time` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `screen_state` INTEGER NOT NULL)");
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS `app_guard_event` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `uid` INTEGER NOT NULL, `app_name` TEXT, `guard_type` INTEGER NOT NULL, `reason` TEXT, `level` INTEGER NOT NULL, `time` INTEGER NOT NULL, `result` INTEGER NOT NULL)");
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS `app_info` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `uid` INTEGER NOT NULL, `pkg_name` TEXT, `app_type` INTEGER NOT NULL, `optimize_value` INTEGER NOT NULL, `background_value` INTEGER NOT NULL)");
            supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            supportSQLiteDatabase.i("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b1a85e67f5961a0efd3b985d0de6928a')");
        }

        @Override // androidx.room.RoomOpenHelper.a
        public void b(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.i("DROP TABLE IF EXISTS `app_power_record`");
            supportSQLiteDatabase.i("DROP TABLE IF EXISTS `app_guard_event`");
            supportSQLiteDatabase.i("DROP TABLE IF EXISTS `app_info`");
        }

        @Override // androidx.room.RoomOpenHelper.a
        protected void c(SupportSQLiteDatabase supportSQLiteDatabase) {
            if (((RoomDatabase) OGuardDataBase_Impl.this).f3900h != null) {
                int size = ((RoomDatabase) OGuardDataBase_Impl.this).f3900h.size();
                for (int i10 = 0; i10 < size; i10++) {
                    ((RoomDatabase.b) ((RoomDatabase) OGuardDataBase_Impl.this).f3900h.get(i10)).a(supportSQLiteDatabase);
                }
            }
        }

        @Override // androidx.room.RoomOpenHelper.a
        public void d(SupportSQLiteDatabase supportSQLiteDatabase) {
            ((RoomDatabase) OGuardDataBase_Impl.this).f3893a = supportSQLiteDatabase;
            OGuardDataBase_Impl.this.o(supportSQLiteDatabase);
            if (((RoomDatabase) OGuardDataBase_Impl.this).f3900h != null) {
                int size = ((RoomDatabase) OGuardDataBase_Impl.this).f3900h.size();
                for (int i10 = 0; i10 < size; i10++) {
                    ((RoomDatabase.b) ((RoomDatabase) OGuardDataBase_Impl.this).f3900h.get(i10)).b(supportSQLiteDatabase);
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
            HashMap hashMap = new HashMap(24);
            hashMap.put("_id", new TableInfo.a("_id", "INTEGER", true, 1));
            hashMap.put("valid", new TableInfo.a("valid", "INTEGER", true, 0));
            hashMap.put(TriggerEvent.EXTRA_UID, new TableInfo.a(TriggerEvent.EXTRA_UID, "INTEGER", true, 0));
            hashMap.put("app_name", new TableInfo.a("app_name", "TEXT", false, 0));
            hashMap.put("cpu_time", new TableInfo.a("cpu_time", "INTEGER", true, 0));
            hashMap.put("small_cpu_time", new TableInfo.a("small_cpu_time", "INTEGER", true, 0));
            hashMap.put("middle_cpu_time", new TableInfo.a("middle_cpu_time", "INTEGER", true, 0));
            hashMap.put("big_cpu_time", new TableInfo.a("big_cpu_time", "INTEGER", true, 0));
            hashMap.put("alarm_wakeup_count", new TableInfo.a("alarm_wakeup_count", "INTEGER", true, 0));
            hashMap.put("wakelock_time", new TableInfo.a("wakelock_time", "INTEGER", true, 0));
            hashMap.put("gps_time", new TableInfo.a("gps_time", "INTEGER", true, 0));
            hashMap.put("sensor_time", new TableInfo.a("sensor_time", "INTEGER", true, 0));
            hashMap.put("wifi_time", new TableInfo.a("wifi_time", "INTEGER", true, 0));
            hashMap.put("wifi_bytes", new TableInfo.a("wifi_bytes", "INTEGER", true, 0));
            hashMap.put("wifi_scan_count", new TableInfo.a("wifi_scan_count", "INTEGER", true, 0));
            hashMap.put("cell_time", new TableInfo.a("cell_time", "INTEGER", true, 0));
            hashMap.put("cell_bytes", new TableInfo.a("cell_bytes", "INTEGER", true, 0));
            hashMap.put("traffic_count", new TableInfo.a("traffic_count", "INTEGER", true, 0));
            hashMap.put("bt_scan_time", new TableInfo.a("bt_scan_time", "INTEGER", true, 0));
            hashMap.put("background_duration", new TableInfo.a("background_duration", "INTEGER", true, 0));
            hashMap.put(AppUseTimeEvent.BUNDLE_KEY_START_TIME, new TableInfo.a(AppUseTimeEvent.BUNDLE_KEY_START_TIME, "TEXT", false, 0));
            hashMap.put(AppUseTimeEvent.BUNDLE_KEY_END_TIME, new TableInfo.a(AppUseTimeEvent.BUNDLE_KEY_END_TIME, "INTEGER", true, 0));
            hashMap.put("duration", new TableInfo.a("duration", "INTEGER", true, 0));
            hashMap.put("screen_state", new TableInfo.a("screen_state", "INTEGER", true, 0));
            TableInfo tableInfo = new TableInfo("app_power_record", hashMap, new HashSet(0), new HashSet(0));
            TableInfo a10 = TableInfo.a(supportSQLiteDatabase, "app_power_record");
            if (tableInfo.equals(a10)) {
                HashMap hashMap2 = new HashMap(8);
                hashMap2.put("_id", new TableInfo.a("_id", "INTEGER", true, 1));
                hashMap2.put(TriggerEvent.EXTRA_UID, new TableInfo.a(TriggerEvent.EXTRA_UID, "INTEGER", true, 0));
                hashMap2.put("app_name", new TableInfo.a("app_name", "TEXT", false, 0));
                hashMap2.put("guard_type", new TableInfo.a("guard_type", "INTEGER", true, 0));
                hashMap2.put("reason", new TableInfo.a("reason", "TEXT", false, 0));
                hashMap2.put("level", new TableInfo.a("level", "INTEGER", true, 0));
                hashMap2.put("time", new TableInfo.a("time", "INTEGER", true, 0));
                hashMap2.put("result", new TableInfo.a("result", "INTEGER", true, 0));
                TableInfo tableInfo2 = new TableInfo("app_guard_event", hashMap2, new HashSet(0), new HashSet(0));
                TableInfo a11 = TableInfo.a(supportSQLiteDatabase, "app_guard_event");
                if (tableInfo2.equals(a11)) {
                    HashMap hashMap3 = new HashMap(6);
                    hashMap3.put("_id", new TableInfo.a("_id", "INTEGER", true, 1));
                    hashMap3.put(TriggerEvent.EXTRA_UID, new TableInfo.a(TriggerEvent.EXTRA_UID, "INTEGER", true, 0));
                    hashMap3.put(UserProfileConstants.COLUMN_PKG_NAME, new TableInfo.a(UserProfileConstants.COLUMN_PKG_NAME, "TEXT", false, 0));
                    hashMap3.put("app_type", new TableInfo.a("app_type", "INTEGER", true, 0));
                    hashMap3.put("optimize_value", new TableInfo.a("optimize_value", "INTEGER", true, 0));
                    hashMap3.put("background_value", new TableInfo.a("background_value", "INTEGER", true, 0));
                    TableInfo tableInfo3 = new TableInfo("app_info", hashMap3, new HashSet(0), new HashSet(0));
                    TableInfo a12 = TableInfo.a(supportSQLiteDatabase, "app_info");
                    if (tableInfo3.equals(a12)) {
                        return;
                    }
                    throw new IllegalStateException("Migration didn't properly handle app_info(com.oplus.oguard.data.database.table.AppInfo).\n Expected:\n" + tableInfo3 + "\n Found:\n" + a12);
                }
                throw new IllegalStateException("Migration didn't properly handle app_guard_event(com.oplus.oguard.data.database.table.AppGuardEvent).\n Expected:\n" + tableInfo2 + "\n Found:\n" + a11);
            }
            throw new IllegalStateException("Migration didn't properly handle app_power_record(com.oplus.oguard.data.database.table.AppPowerRecord).\n Expected:\n" + tableInfo + "\n Found:\n" + a10);
        }
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker e() {
        return new InvalidationTracker(this, new HashMap(0), new HashMap(0), "app_power_record", "app_guard_event", "app_info");
    }

    @Override // androidx.room.RoomDatabase
    protected SupportSQLiteOpenHelper f(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.f3829a.a(SupportSQLiteOpenHelper.b.a(databaseConfiguration.f3830b).c(databaseConfiguration.f3831c).b(new RoomOpenHelper(databaseConfiguration, new a(4), "b1a85e67f5961a0efd3b985d0de6928a", "fcd57f8c409587524aefa9a0e990d45a")).a());
    }

    @Override // com.oplus.oguard.data.database.OGuardDataBase
    public AppPowerRecordDao u() {
        AppPowerRecordDao appPowerRecordDao;
        if (this.f9944m != null) {
            return this.f9944m;
        }
        synchronized (this) {
            if (this.f9944m == null) {
                this.f9944m = new AppPowerRecordDao_Impl(this);
            }
            appPowerRecordDao = this.f9944m;
        }
        return appPowerRecordDao;
    }
}
