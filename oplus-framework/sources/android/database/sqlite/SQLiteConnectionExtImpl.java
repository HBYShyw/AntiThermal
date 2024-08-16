package android.database.sqlite;

import java.util.function.Supplier;

/* loaded from: classes.dex */
public class SQLiteConnectionExtImpl implements ISQLiteConnectionExt {
    private ThreadLocal<Boolean> mIsInMemoryDb = ThreadLocal.withInitial(new Supplier() { // from class: android.database.sqlite.SQLiteConnectionExtImpl$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return SQLiteConnectionExtImpl.lambda$new$0();
        }
    });
    private ThreadLocal<Boolean> mIsReadOnlyDb = ThreadLocal.withInitial(new Supplier() { // from class: android.database.sqlite.SQLiteConnectionExtImpl$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return SQLiteConnectionExtImpl.lambda$new$1();
        }
    });

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Boolean lambda$new$0() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Boolean lambda$new$1() {
        return false;
    }

    public SQLiteConnectionExtImpl(Object base) {
    }

    public void updateConfig(boolean isInMemoryDb, boolean readOnlyDb) {
        this.mIsInMemoryDb.set(Boolean.valueOf(isInMemoryDb));
        this.mIsReadOnlyDb.set(Boolean.valueOf(readOnlyDb));
    }

    public boolean isInMemoryDb() {
        return this.mIsInMemoryDb.get().booleanValue();
    }

    public boolean isReadOnlyDb() {
        return this.mIsReadOnlyDb.get().booleanValue();
    }
}
