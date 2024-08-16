package x8;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.ArrayMap;
import android.util.Log;
import b6.LocalLog;

/* compiled from: DatabaseHelper.java */
/* renamed from: x8.b, reason: use source file name */
/* loaded from: classes2.dex */
public class DatabaseHelper extends SQLiteOpenHelper {

    /* renamed from: g, reason: collision with root package name */
    private static volatile DatabaseHelper f19652g;

    /* renamed from: e, reason: collision with root package name */
    private Context f19653e;

    /* renamed from: f, reason: collision with root package name */
    private SQLiteDatabase f19654f;

    public DatabaseHelper(Context context) {
        super(context, "BatteryStats.db", (SQLiteDatabase.CursorFactory) null, 4);
        this.f19654f = null;
        this.f19653e = context;
    }

    private SQLiteDatabase m() {
        if (this.f19654f == null) {
            this.f19654f = getWritableDatabase();
        }
        return this.f19654f;
    }

    public static DatabaseHelper u(Context context) {
        if (f19652g == null) {
            synchronized (DatabaseHelper.class) {
                if (f19652g == null) {
                    f19652g = new DatabaseHelper(context);
                }
            }
        }
        return f19652g;
    }

    private void v(String str) {
        LocalLog.a("BatteryStatsProvider", "initBatteryStatsDatabase, sendBroadcast action = " + str);
    }

    public void b() {
        m().execSQL("delete from battery_stats_list;");
    }

    public ArrayMap<String, String> c() {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        if (m() == null) {
            return arrayMap;
        }
        Cursor rawQuery = m().rawQuery("select * from app_label_list;", null);
        while (rawQuery.moveToNext()) {
            arrayMap.put(rawQuery.getString(1), rawQuery.getString(2));
        }
        rawQuery.close();
        return arrayMap;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists battery_stats_list (_id INTEGER PRIMARY KEY AUTOINCREMENT,drain_type TEXT,packageWithHighestDrain TEXT,pkg_name TEXT,usage_time LONG,foreground_act_time LONG,background_act_time LONG,awake_time LONG,wlan_tx_bytes LONG,wlan_rx_bytes LONG,power DOUBLE,saved_time_millis LONG,saved_time TEXT,icon BLOB,sipper_uid INTEGER,battery_level INTEGER,battery_status INTEGER);");
        sQLiteDatabase.execSQL("create table if not exists app_label_list (_id INTEGER PRIMARY KEY AUTOINCREMENT,pkg_name TEXT,app_label TEXT);");
        LocalLog.a("BatteryStatsProvider", "onCreate");
        v("action_create_battery_stats_database");
        this.f19654f = sQLiteDatabase;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i10, int i11) {
        Log.w("BatteryStatsProvider", "onDowngrade, oldVersion = " + i10 + ", newVersion = " + i11);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists app_label_list (_id INTEGER PRIMARY KEY AUTOINCREMENT,pkg_name TEXT,app_label TEXT);");
        super.onOpen(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i10, int i11) {
        Log.w("BatteryStatsProvider", "Upgrading BatteryStats database from version " + i10 + " to " + i11);
        if (i10 < 2) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS battery_stats_list");
            sQLiteDatabase.execSQL("create table if not exists battery_stats_list (_id INTEGER PRIMARY KEY AUTOINCREMENT,drain_type TEXT,packageWithHighestDrain TEXT,pkg_name TEXT,usage_time LONG,foreground_act_time LONG,background_act_time LONG,awake_time LONG,wlan_tx_bytes LONG,wlan_rx_bytes LONG,power DOUBLE,saved_time_millis LONG,saved_time TEXT,icon BLOB,sipper_uid INTEGER,battery_level INTEGER,battery_status INTEGER);");
            v("action_upgrade_battery_stats_database");
        }
        if (i11 <= i10 || i11 != 4) {
            return;
        }
        try {
            sQLiteDatabase.execSQL("alter table battery_stats_list rename to battery_stats_temp");
            sQLiteDatabase.execSQL("create table if not exists battery_stats_list (_id INTEGER PRIMARY KEY AUTOINCREMENT,drain_type TEXT,packageWithHighestDrain TEXT,pkg_name TEXT,usage_time LONG,foreground_act_time LONG,background_act_time LONG,awake_time LONG,wlan_tx_bytes LONG,wlan_rx_bytes LONG,power DOUBLE,saved_time_millis LONG,saved_time TEXT,icon BLOB,sipper_uid INTEGER,battery_level INTEGER,battery_status INTEGER);");
            sQLiteDatabase.execSQL("insert into battery_stats_list select *,' ' from battery_stats_temp");
        } catch (Throwable th) {
            Log.e("BatteryStatsProvider", "Fail to upgrade database e=" + th);
        }
        v("action_upgrade_battery_stats_database");
    }

    public String w() {
        StringBuilder sb2 = new StringBuilder();
        Cursor rawQuery = m().rawQuery("select * from battery_stats_list where power >= 1;", null);
        while (rawQuery.moveToNext()) {
            sb2.append(rawQuery.getString(2));
            sb2.append(", ");
            sb2.append(rawQuery.getString(5));
            sb2.append(", ");
            sb2.append(rawQuery.getString(10));
            sb2.append(", ");
            sb2.append(rawQuery.getString(12));
            sb2.append(", ");
            sb2.append("\n");
        }
        rawQuery.close();
        return sb2.toString();
    }
}
