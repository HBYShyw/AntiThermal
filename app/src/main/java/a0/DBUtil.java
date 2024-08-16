package a0;

import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import androidx.room.RoomDatabase;
import c0.SupportSQLiteDatabase;
import c0.SupportSQLiteQuery;
import java.util.ArrayList;

/* compiled from: DBUtil.java */
/* renamed from: a0.b, reason: use source file name */
/* loaded from: classes.dex */
public class DBUtil {
    public static void a(SupportSQLiteDatabase supportSQLiteDatabase) {
        ArrayList<String> arrayList = new ArrayList();
        Cursor H = supportSQLiteDatabase.H("SELECT name FROM sqlite_master WHERE type = 'trigger'");
        while (H.moveToNext()) {
            try {
                arrayList.add(H.getString(0));
            } catch (Throwable th) {
                H.close();
                throw th;
            }
        }
        H.close();
        for (String str : arrayList) {
            if (str.startsWith("room_fts_content_sync_")) {
                supportSQLiteDatabase.i("DROP TRIGGER IF EXISTS " + str);
            }
        }
    }

    public static Cursor b(RoomDatabase roomDatabase, SupportSQLiteQuery supportSQLiteQuery, boolean z10) {
        Cursor r10 = roomDatabase.r(supportSQLiteQuery);
        if (!z10 || !(r10 instanceof AbstractWindowedCursor)) {
            return r10;
        }
        AbstractWindowedCursor abstractWindowedCursor = (AbstractWindowedCursor) r10;
        int count = abstractWindowedCursor.getCount();
        return (abstractWindowedCursor.hasWindow() ? abstractWindowedCursor.getWindow().getNumRows() : count) < count ? CursorUtil.a(abstractWindowedCursor) : r10;
    }
}
