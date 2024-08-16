package c0;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import java.io.Closeable;
import java.util.List;

/* compiled from: SupportSQLiteDatabase.java */
/* renamed from: c0.b, reason: use source file name */
/* loaded from: classes.dex */
public interface SupportSQLiteDatabase extends Closeable {
    void A();

    int B(String str, int i10, ContentValues contentValues, String str2, Object[] objArr);

    Cursor H(String str);

    long K(String str, int i10, ContentValues contentValues);

    void P();

    int d(String str, String str2, Object[] objArr);

    boolean d0();

    void e();

    List<Pair<String, String>> f();

    String getPath();

    void i(String str);

    boolean isOpen();

    SupportSQLiteStatement n(String str);

    Cursor q(SupportSQLiteQuery supportSQLiteQuery);
}
