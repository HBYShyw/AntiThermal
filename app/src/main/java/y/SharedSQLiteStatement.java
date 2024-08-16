package y;

import androidx.room.RoomDatabase;
import c0.SupportSQLiteStatement;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: SharedSQLiteStatement.java */
/* renamed from: y.e, reason: use source file name */
/* loaded from: classes.dex */
public abstract class SharedSQLiteStatement {

    /* renamed from: a, reason: collision with root package name */
    private final AtomicBoolean f19742a = new AtomicBoolean(false);

    /* renamed from: b, reason: collision with root package name */
    private final RoomDatabase f19743b;

    /* renamed from: c, reason: collision with root package name */
    private volatile SupportSQLiteStatement f19744c;

    public SharedSQLiteStatement(RoomDatabase roomDatabase) {
        this.f19743b = roomDatabase;
    }

    private SupportSQLiteStatement c() {
        return this.f19743b.d(d());
    }

    private SupportSQLiteStatement e(boolean z10) {
        if (z10) {
            if (this.f19744c == null) {
                this.f19744c = c();
            }
            return this.f19744c;
        }
        return c();
    }

    public SupportSQLiteStatement a() {
        b();
        return e(this.f19742a.compareAndSet(false, true));
    }

    protected void b() {
        this.f19743b.a();
    }

    protected abstract String d();

    public void f(SupportSQLiteStatement supportSQLiteStatement) {
        if (supportSQLiteStatement == this.f19744c) {
            this.f19742a.set(false);
        }
    }
}
