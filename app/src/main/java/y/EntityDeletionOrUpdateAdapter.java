package y;

import androidx.room.RoomDatabase;
import c0.SupportSQLiteStatement;

/* compiled from: EntityDeletionOrUpdateAdapter.java */
/* renamed from: y.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class EntityDeletionOrUpdateAdapter<T> extends SharedSQLiteStatement {
    public EntityDeletionOrUpdateAdapter(RoomDatabase roomDatabase) {
        super(roomDatabase);
    }

    protected abstract void g(SupportSQLiteStatement supportSQLiteStatement, T t7);

    public final int h(T t7) {
        SupportSQLiteStatement a10 = a();
        try {
            g(a10, t7);
            return a10.l();
        } finally {
            f(a10);
        }
    }
}
