package d0;

import android.database.sqlite.SQLiteStatement;
import c0.SupportSQLiteStatement;

/* compiled from: FrameworkSQLiteStatement.java */
/* renamed from: d0.e, reason: use source file name */
/* loaded from: classes.dex */
class FrameworkSQLiteStatement extends FrameworkSQLiteProgram implements SupportSQLiteStatement {

    /* renamed from: f, reason: collision with root package name */
    private final SQLiteStatement f10665f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FrameworkSQLiteStatement(SQLiteStatement sQLiteStatement) {
        super(sQLiteStatement);
        this.f10665f = sQLiteStatement;
    }

    @Override // c0.SupportSQLiteStatement
    public int l() {
        return this.f10665f.executeUpdateDelete();
    }

    @Override // c0.SupportSQLiteStatement
    public long q0() {
        return this.f10665f.executeInsert();
    }
}
