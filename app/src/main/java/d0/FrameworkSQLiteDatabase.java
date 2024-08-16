package d0;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.text.TextUtils;
import android.util.Pair;
import c0.SimpleSQLiteQuery;
import c0.SupportSQLiteDatabase;
import c0.SupportSQLiteQuery;
import c0.SupportSQLiteStatement;
import java.util.List;

/* compiled from: FrameworkSQLiteDatabase.java */
/* renamed from: d0.a, reason: use source file name */
/* loaded from: classes.dex */
class FrameworkSQLiteDatabase implements SupportSQLiteDatabase {

    /* renamed from: f, reason: collision with root package name */
    private static final String[] f10653f = {"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};

    /* renamed from: g, reason: collision with root package name */
    private static final String[] f10654g = new String[0];

    /* renamed from: e, reason: collision with root package name */
    private final SQLiteDatabase f10655e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FrameworkSQLiteDatabase.java */
    /* renamed from: d0.a$a */
    /* loaded from: classes.dex */
    public class a implements SQLiteDatabase.CursorFactory {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ SupportSQLiteQuery f10656a;

        a(SupportSQLiteQuery supportSQLiteQuery) {
            this.f10656a = supportSQLiteQuery;
        }

        @Override // android.database.sqlite.SQLiteDatabase.CursorFactory
        public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
            this.f10656a.c(new FrameworkSQLiteProgram(sQLiteQuery));
            return new SQLiteCursor(sQLiteCursorDriver, str, sQLiteQuery);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FrameworkSQLiteDatabase(SQLiteDatabase sQLiteDatabase) {
        this.f10655e = sQLiteDatabase;
    }

    @Override // c0.SupportSQLiteDatabase
    public void A() {
        this.f10655e.setTransactionSuccessful();
    }

    @Override // c0.SupportSQLiteDatabase
    public int B(String str, int i10, ContentValues contentValues, String str2, Object[] objArr) {
        if (contentValues != null && contentValues.size() != 0) {
            StringBuilder sb2 = new StringBuilder(120);
            sb2.append("UPDATE ");
            sb2.append(f10653f[i10]);
            sb2.append(str);
            sb2.append(" SET ");
            int size = contentValues.size();
            int length = objArr == null ? size : objArr.length + size;
            Object[] objArr2 = new Object[length];
            int i11 = 0;
            for (String str3 : contentValues.keySet()) {
                sb2.append(i11 > 0 ? "," : "");
                sb2.append(str3);
                objArr2[i11] = contentValues.get(str3);
                sb2.append("=?");
                i11++;
            }
            if (objArr != null) {
                for (int i12 = size; i12 < length; i12++) {
                    objArr2[i12] = objArr[i12 - size];
                }
            }
            if (!TextUtils.isEmpty(str2)) {
                sb2.append(" WHERE ");
                sb2.append(str2);
            }
            SupportSQLiteStatement n10 = n(sb2.toString());
            SimpleSQLiteQuery.d(n10, objArr2);
            return n10.l();
        }
        throw new IllegalArgumentException("Empty values");
    }

    @Override // c0.SupportSQLiteDatabase
    public Cursor H(String str) {
        return q(new SimpleSQLiteQuery(str));
    }

    @Override // c0.SupportSQLiteDatabase
    public long K(String str, int i10, ContentValues contentValues) {
        return this.f10655e.insertWithOnConflict(str, null, contentValues, i10);
    }

    @Override // c0.SupportSQLiteDatabase
    public void P() {
        this.f10655e.endTransaction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(SQLiteDatabase sQLiteDatabase) {
        return this.f10655e == sQLiteDatabase;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f10655e.close();
    }

    @Override // c0.SupportSQLiteDatabase
    public int d(String str, String str2, Object[] objArr) {
        String str3;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("DELETE FROM ");
        sb2.append(str);
        if (TextUtils.isEmpty(str2)) {
            str3 = "";
        } else {
            str3 = " WHERE " + str2;
        }
        sb2.append(str3);
        SupportSQLiteStatement n10 = n(sb2.toString());
        SimpleSQLiteQuery.d(n10, objArr);
        return n10.l();
    }

    @Override // c0.SupportSQLiteDatabase
    public boolean d0() {
        return this.f10655e.inTransaction();
    }

    @Override // c0.SupportSQLiteDatabase
    public void e() {
        this.f10655e.beginTransaction();
    }

    @Override // c0.SupportSQLiteDatabase
    public List<Pair<String, String>> f() {
        return this.f10655e.getAttachedDbs();
    }

    @Override // c0.SupportSQLiteDatabase
    public String getPath() {
        return this.f10655e.getPath();
    }

    @Override // c0.SupportSQLiteDatabase
    public void i(String str) {
        this.f10655e.execSQL(str);
    }

    @Override // c0.SupportSQLiteDatabase
    public boolean isOpen() {
        return this.f10655e.isOpen();
    }

    @Override // c0.SupportSQLiteDatabase
    public SupportSQLiteStatement n(String str) {
        return new FrameworkSQLiteStatement(this.f10655e.compileStatement(str));
    }

    @Override // c0.SupportSQLiteDatabase
    public Cursor q(SupportSQLiteQuery supportSQLiteQuery) {
        return this.f10655e.rawQueryWithFactory(new a(supportSQLiteQuery), supportSQLiteQuery.b(), f10654g, null);
    }
}
