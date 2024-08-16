package d0;

import android.database.sqlite.SQLiteProgram;
import c0.SupportSQLiteProgram;

/* compiled from: FrameworkSQLiteProgram.java */
/* renamed from: d0.d, reason: use source file name */
/* loaded from: classes.dex */
class FrameworkSQLiteProgram implements SupportSQLiteProgram {

    /* renamed from: e, reason: collision with root package name */
    private final SQLiteProgram f10664e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FrameworkSQLiteProgram(SQLiteProgram sQLiteProgram) {
        this.f10664e = sQLiteProgram;
    }

    @Override // c0.SupportSQLiteProgram
    public void C(int i10, byte[] bArr) {
        this.f10664e.bindBlob(i10, bArr);
    }

    @Override // c0.SupportSQLiteProgram
    public void Y(int i10) {
        this.f10664e.bindNull(i10);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f10664e.close();
    }

    @Override // c0.SupportSQLiteProgram
    public void j(int i10, String str) {
        this.f10664e.bindString(i10, str);
    }

    @Override // c0.SupportSQLiteProgram
    public void r(int i10, double d10) {
        this.f10664e.bindDouble(i10, d10);
    }

    @Override // c0.SupportSQLiteProgram
    public void y(int i10, long j10) {
        this.f10664e.bindLong(i10, j10);
    }
}
