package y;

import androidx.room.RoomDatabase;
import c0.SupportSQLiteStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* compiled from: EntityInsertionAdapter.java */
/* renamed from: y.b, reason: use source file name */
/* loaded from: classes.dex */
public abstract class EntityInsertionAdapter<T> extends SharedSQLiteStatement {
    public EntityInsertionAdapter(RoomDatabase roomDatabase) {
        super(roomDatabase);
    }

    protected abstract void g(SupportSQLiteStatement supportSQLiteStatement, T t7);

    public final void h(Iterable<T> iterable) {
        SupportSQLiteStatement a10 = a();
        try {
            Iterator<T> it = iterable.iterator();
            while (it.hasNext()) {
                g(a10, it.next());
                a10.q0();
            }
        } finally {
            f(a10);
        }
    }

    public final void i(T t7) {
        SupportSQLiteStatement a10 = a();
        try {
            g(a10, t7);
            a10.q0();
        } finally {
            f(a10);
        }
    }

    public final void j(T[] tArr) {
        SupportSQLiteStatement a10 = a();
        try {
            for (T t7 : tArr) {
                g(a10, t7);
                a10.q0();
            }
        } finally {
            f(a10);
        }
    }

    public final List<Long> k(Collection<T> collection) {
        SupportSQLiteStatement a10 = a();
        try {
            ArrayList arrayList = new ArrayList(collection.size());
            int i10 = 0;
            Iterator<T> it = collection.iterator();
            while (it.hasNext()) {
                g(a10, it.next());
                arrayList.add(i10, Long.valueOf(a10.q0()));
                i10++;
            }
            return arrayList;
        } finally {
            f(a10);
        }
    }
}
