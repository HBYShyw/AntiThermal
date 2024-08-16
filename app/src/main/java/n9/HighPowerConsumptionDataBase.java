package n9;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import b6.LocalLog;
import x8.DataBaseUtil;

/* compiled from: HighPowerConsumptionDataBase.java */
/* renamed from: n9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class HighPowerConsumptionDataBase extends SQLiteOpenHelper {

    /* renamed from: e, reason: collision with root package name */
    public static final Uri f15917e = DataBaseUtil.f19649a;

    public HighPowerConsumptionDataBase(Context context) {
        super(context, "HighPowerConsumption.db", (SQLiteDatabase.CursorFactory) null, 3);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists high_power_consumption_one (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name TEXT,time TEXT,cpu DOUBLE,wakelock DOUBLE,job DOUBLE,wifiscan DOUBLE,camera DOUBLE,flashlight DOUBLE,gps DOUBLE,alarm INTEGER);");
        sQLiteDatabase.execSQL("create table if not exists high_power_consumption_two (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name TEXT,time TEXT,cpu DOUBLE,wakelock DOUBLE,job DOUBLE,wifiscan DOUBLE,camera DOUBLE,flashlight DOUBLE,gps DOUBLE,alarm INTEGER);");
        sQLiteDatabase.execSQL("create table if not exists high_power_consumption_three (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name TEXT,time TEXT,cpu DOUBLE,wakelock DOUBLE,job DOUBLE,wifiscan DOUBLE,camera DOUBLE,flashlight DOUBLE,gps DOUBLE,alarm INTEGER);");
        LocalLog.a("HighPowerConsumptionDataBase", "onCreate");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i10, int i11) {
        onUpgrade(sQLiteDatabase, i10, i11);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i10, int i11) {
        LocalLog.l("HighPowerConsumptionDataBase", "Upgrading HighPower database from version " + i10 + " to " + i11);
        if (i10 < 2) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS high_power_consumption_one");
            sQLiteDatabase.execSQL("create table if not exists high_power_consumption_one (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name TEXT,time TEXT,cpu DOUBLE,wakelock DOUBLE,job DOUBLE,wifiscan DOUBLE,camera DOUBLE,flashlight DOUBLE,gps DOUBLE,alarm INTEGER);");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS high_power_consumption_two");
            sQLiteDatabase.execSQL("create table if not exists high_power_consumption_two (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name TEXT,time TEXT,cpu DOUBLE,wakelock DOUBLE,job DOUBLE,wifiscan DOUBLE,camera DOUBLE,flashlight DOUBLE,gps DOUBLE,alarm INTEGER);");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS high_power_consumption_three");
            sQLiteDatabase.execSQL("create table if not exists high_power_consumption_three (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name TEXT,time TEXT,cpu DOUBLE,wakelock DOUBLE,job DOUBLE,wifiscan DOUBLE,camera DOUBLE,flashlight DOUBLE,gps DOUBLE,alarm INTEGER);");
        }
        if (i11 <= i10 || i11 != 3) {
            return;
        }
        try {
            sQLiteDatabase.execSQL("alter table high_power_consumption_one rename to high_power_consumption_one_temp");
            sQLiteDatabase.execSQL("create table if not exists high_power_consumption_one (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name TEXT,time TEXT,cpu DOUBLE,wakelock DOUBLE,job DOUBLE,wifiscan DOUBLE,camera DOUBLE,flashlight DOUBLE,gps DOUBLE,alarm INTEGER);");
            sQLiteDatabase.execSQL("insert into high_power_consumption_one select *,' ' from high_power_consumption_one_temp");
            sQLiteDatabase.execSQL("drop table high_power_consumption_one_temp");
            sQLiteDatabase.execSQL("alter table high_power_consumption_two rename to high_power_consumption_two_temp");
            sQLiteDatabase.execSQL("create table if not exists high_power_consumption_two (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name TEXT,time TEXT,cpu DOUBLE,wakelock DOUBLE,job DOUBLE,wifiscan DOUBLE,camera DOUBLE,flashlight DOUBLE,gps DOUBLE,alarm INTEGER);");
            sQLiteDatabase.execSQL("insert into high_power_consumption_two select *,' ' from high_power_consumption_two_temp");
            sQLiteDatabase.execSQL("drop table high_power_consumption_two_temp");
            sQLiteDatabase.execSQL("alter table high_power_consumption_three rename to high_power_consumption_three_temp");
            sQLiteDatabase.execSQL("create table if not exists high_power_consumption_three (_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name TEXT,time TEXT,cpu DOUBLE,wakelock DOUBLE,job DOUBLE,wifiscan DOUBLE,camera DOUBLE,flashlight DOUBLE,gps DOUBLE,alarm INTEGER);");
            sQLiteDatabase.execSQL("insert into high_power_consumption_three select *,' ' from high_power_consumption_three_temp");
            sQLiteDatabase.execSQL("drop table high_power_consumption_three_temp");
        } catch (Throwable th) {
            LocalLog.b("HighPowerConsumptionDataBase", "Fail to upgrade database e=" + th);
        }
    }
}
