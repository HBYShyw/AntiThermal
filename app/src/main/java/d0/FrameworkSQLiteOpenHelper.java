package d0;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import c0.SupportSQLiteDatabase;
import c0.SupportSQLiteOpenHelper;

/* compiled from: FrameworkSQLiteOpenHelper.java */
/* renamed from: d0.b, reason: use source file name */
/* loaded from: classes.dex */
class FrameworkSQLiteOpenHelper implements SupportSQLiteOpenHelper {

    /* renamed from: a, reason: collision with root package name */
    private final a f10658a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FrameworkSQLiteOpenHelper.java */
    /* renamed from: d0.b$a */
    /* loaded from: classes.dex */
    public static class a extends SQLiteOpenHelper {

        /* renamed from: e, reason: collision with root package name */
        final FrameworkSQLiteDatabase[] f10659e;

        /* renamed from: f, reason: collision with root package name */
        final SupportSQLiteOpenHelper.a f10660f;

        /* renamed from: g, reason: collision with root package name */
        private boolean f10661g;

        /* compiled from: FrameworkSQLiteOpenHelper.java */
        /* renamed from: d0.b$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class C0029a implements DatabaseErrorHandler {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ SupportSQLiteOpenHelper.a f10662a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ FrameworkSQLiteDatabase[] f10663b;

            C0029a(SupportSQLiteOpenHelper.a aVar, FrameworkSQLiteDatabase[] frameworkSQLiteDatabaseArr) {
                this.f10662a = aVar;
                this.f10663b = frameworkSQLiteDatabaseArr;
            }

            @Override // android.database.DatabaseErrorHandler
            public void onCorruption(SQLiteDatabase sQLiteDatabase) {
                this.f10662a.c(a.c(this.f10663b, sQLiteDatabase));
            }
        }

        a(Context context, String str, FrameworkSQLiteDatabase[] frameworkSQLiteDatabaseArr, SupportSQLiteOpenHelper.a aVar) {
            super(context, str, null, aVar.f4724a, new C0029a(aVar, frameworkSQLiteDatabaseArr));
            this.f10660f = aVar;
            this.f10659e = frameworkSQLiteDatabaseArr;
        }

        static FrameworkSQLiteDatabase c(FrameworkSQLiteDatabase[] frameworkSQLiteDatabaseArr, SQLiteDatabase sQLiteDatabase) {
            FrameworkSQLiteDatabase frameworkSQLiteDatabase = frameworkSQLiteDatabaseArr[0];
            if (frameworkSQLiteDatabase == null || !frameworkSQLiteDatabase.b(sQLiteDatabase)) {
                frameworkSQLiteDatabaseArr[0] = new FrameworkSQLiteDatabase(sQLiteDatabase);
            }
            return frameworkSQLiteDatabaseArr[0];
        }

        FrameworkSQLiteDatabase b(SQLiteDatabase sQLiteDatabase) {
            return c(this.f10659e, sQLiteDatabase);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper, java.lang.AutoCloseable
        public synchronized void close() {
            super.close();
            this.f10659e[0] = null;
        }

        synchronized SupportSQLiteDatabase m() {
            this.f10661g = false;
            SQLiteDatabase writableDatabase = super.getWritableDatabase();
            if (this.f10661g) {
                close();
                return m();
            }
            return b(writableDatabase);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onConfigure(SQLiteDatabase sQLiteDatabase) {
            this.f10660f.b(b(sQLiteDatabase));
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            this.f10660f.d(b(sQLiteDatabase));
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i10, int i11) {
            this.f10661g = true;
            this.f10660f.e(b(sQLiteDatabase), i10, i11);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            if (this.f10661g) {
                return;
            }
            this.f10660f.f(b(sQLiteDatabase));
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i10, int i11) {
            this.f10661g = true;
            this.f10660f.g(b(sQLiteDatabase), i10, i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FrameworkSQLiteOpenHelper(Context context, String str, SupportSQLiteOpenHelper.a aVar) {
        this.f10658a = c(context, str, aVar);
    }

    private a c(Context context, String str, SupportSQLiteOpenHelper.a aVar) {
        return new a(context, str, new FrameworkSQLiteDatabase[1], aVar);
    }

    @Override // c0.SupportSQLiteOpenHelper
    public void a(boolean z10) {
        this.f10658a.setWriteAheadLoggingEnabled(z10);
    }

    @Override // c0.SupportSQLiteOpenHelper
    public SupportSQLiteDatabase b() {
        return this.f10658a.m();
    }
}
