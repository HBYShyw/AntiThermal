package androidx.room;

import android.database.Cursor;
import c0.SimpleSQLiteQuery;
import c0.SupportSQLiteDatabase;
import c0.SupportSQLiteOpenHelper;
import java.util.Iterator;
import java.util.List;
import y.RoomMasterTable;
import z.Migration;

/* compiled from: RoomOpenHelper.java */
/* renamed from: androidx.room.i, reason: use source file name */
/* loaded from: classes.dex */
public class RoomOpenHelper extends SupportSQLiteOpenHelper.a {

    /* renamed from: b, reason: collision with root package name */
    private DatabaseConfiguration f3924b;

    /* renamed from: c, reason: collision with root package name */
    private final a f3925c;

    /* renamed from: d, reason: collision with root package name */
    private final String f3926d;

    /* renamed from: e, reason: collision with root package name */
    private final String f3927e;

    /* compiled from: RoomOpenHelper.java */
    /* renamed from: androidx.room.i$a */
    /* loaded from: classes.dex */
    public static abstract class a {

        /* renamed from: a, reason: collision with root package name */
        public final int f3928a;

        public a(int i10) {
            this.f3928a = i10;
        }

        protected abstract void a(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void b(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void c(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void d(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void e(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void f(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void g(SupportSQLiteDatabase supportSQLiteDatabase);
    }

    public RoomOpenHelper(DatabaseConfiguration databaseConfiguration, a aVar, String str, String str2) {
        super(aVar.f3928a);
        this.f3924b = databaseConfiguration;
        this.f3925c = aVar;
        this.f3926d = str;
        this.f3927e = str2;
    }

    private void h(SupportSQLiteDatabase supportSQLiteDatabase) {
        if (j(supportSQLiteDatabase)) {
            Cursor q10 = supportSQLiteDatabase.q(new SimpleSQLiteQuery("SELECT identity_hash FROM room_master_table WHERE id = 42 LIMIT 1"));
            try {
                r1 = q10.moveToFirst() ? q10.getString(0) : null;
            } finally {
                q10.close();
            }
        }
        if (!this.f3926d.equals(r1) && !this.f3927e.equals(r1)) {
            throw new IllegalStateException("Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.");
        }
    }

    private void i(SupportSQLiteDatabase supportSQLiteDatabase) {
        supportSQLiteDatabase.i("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
    }

    private static boolean j(SupportSQLiteDatabase supportSQLiteDatabase) {
        Cursor H = supportSQLiteDatabase.H("SELECT 1 FROM sqlite_master WHERE type = 'table' AND name='room_master_table'");
        try {
            boolean z10 = false;
            if (H.moveToFirst()) {
                if (H.getInt(0) != 0) {
                    z10 = true;
                }
            }
            return z10;
        } finally {
            H.close();
        }
    }

    private void k(SupportSQLiteDatabase supportSQLiteDatabase) {
        i(supportSQLiteDatabase);
        supportSQLiteDatabase.i(RoomMasterTable.a(this.f3926d));
    }

    @Override // c0.SupportSQLiteOpenHelper.a
    public void b(SupportSQLiteDatabase supportSQLiteDatabase) {
        super.b(supportSQLiteDatabase);
    }

    @Override // c0.SupportSQLiteOpenHelper.a
    public void d(SupportSQLiteDatabase supportSQLiteDatabase) {
        k(supportSQLiteDatabase);
        this.f3925c.a(supportSQLiteDatabase);
        this.f3925c.c(supportSQLiteDatabase);
    }

    @Override // c0.SupportSQLiteOpenHelper.a
    public void e(SupportSQLiteDatabase supportSQLiteDatabase, int i10, int i11) {
        g(supportSQLiteDatabase, i10, i11);
    }

    @Override // c0.SupportSQLiteOpenHelper.a
    public void f(SupportSQLiteDatabase supportSQLiteDatabase) {
        super.f(supportSQLiteDatabase);
        h(supportSQLiteDatabase);
        this.f3925c.d(supportSQLiteDatabase);
        this.f3924b = null;
    }

    @Override // c0.SupportSQLiteOpenHelper.a
    public void g(SupportSQLiteDatabase supportSQLiteDatabase, int i10, int i11) {
        boolean z10;
        List<Migration> c10;
        DatabaseConfiguration databaseConfiguration = this.f3924b;
        if (databaseConfiguration == null || (c10 = databaseConfiguration.f3832d.c(i10, i11)) == null) {
            z10 = false;
        } else {
            this.f3925c.f(supportSQLiteDatabase);
            Iterator<Migration> it = c10.iterator();
            while (it.hasNext()) {
                it.next().a(supportSQLiteDatabase);
            }
            this.f3925c.g(supportSQLiteDatabase);
            this.f3925c.e(supportSQLiteDatabase);
            k(supportSQLiteDatabase);
            z10 = true;
        }
        if (z10) {
            return;
        }
        DatabaseConfiguration databaseConfiguration2 = this.f3924b;
        if (databaseConfiguration2 != null && !databaseConfiguration2.a(i10, i11)) {
            this.f3925c.b(supportSQLiteDatabase);
            this.f3925c.a(supportSQLiteDatabase);
            return;
        }
        throw new IllegalStateException("A migration from " + i10 + " to " + i11 + " was required but not found. Please provide the necessary Migration path via RoomDatabase.Builder.addMigration(Migration ...) or allow for destructive migrations via one of the RoomDatabase.Builder.fallbackToDestructiveMigration* methods.");
    }
}
