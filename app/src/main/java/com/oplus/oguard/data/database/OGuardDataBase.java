package com.oplus.oguard.data.database;

import android.content.Context;
import android.util.Log;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import c0.SupportSQLiteDatabase;
import i7.AppPowerRecordDao;

/* loaded from: classes.dex */
public abstract class OGuardDataBase extends RoomDatabase {

    /* renamed from: l, reason: collision with root package name */
    private static volatile OGuardDataBase f9943l;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends RoomDatabase.b {
        a() {
        }

        @Override // androidx.room.RoomDatabase.b
        public void a(SupportSQLiteDatabase supportSQLiteDatabase) {
            super.a(supportSQLiteDatabase);
            Log.d("OGuardDataBase", "onCreate");
        }

        @Override // androidx.room.RoomDatabase.b
        public void b(SupportSQLiteDatabase supportSQLiteDatabase) {
            super.b(supportSQLiteDatabase);
            Log.d("OGuardDataBase", "onOpen");
        }
    }

    private static OGuardDataBase t(Context context) {
        return (OGuardDataBase) Room.a(context, OGuardDataBase.class, "OGuard.db").a(new a()).f().d();
    }

    public static OGuardDataBase v(Context context) {
        if (f9943l == null) {
            synchronized (OGuardDataBase.class) {
                if (f9943l == null) {
                    f9943l = t(context);
                }
            }
        }
        return f9943l;
    }

    public abstract AppPowerRecordDao u();
}
