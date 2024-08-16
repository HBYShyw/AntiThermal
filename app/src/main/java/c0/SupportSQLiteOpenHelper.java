package c0;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.util.Pair;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/* compiled from: SupportSQLiteOpenHelper.java */
/* renamed from: c0.c, reason: use source file name */
/* loaded from: classes.dex */
public interface SupportSQLiteOpenHelper {

    /* compiled from: SupportSQLiteOpenHelper.java */
    /* renamed from: c0.c$a */
    /* loaded from: classes.dex */
    public static abstract class a {

        /* renamed from: a, reason: collision with root package name */
        public final int f4724a;

        public a(int i10) {
            this.f4724a = i10;
        }

        private void a(String str) {
            if (str.equalsIgnoreCase(":memory:") || str.trim().length() == 0) {
                return;
            }
            Log.w("SupportSQLite", "deleting the database file: " + str);
            try {
                SQLiteDatabase.deleteDatabase(new File(str));
            } catch (Exception e10) {
                Log.w("SupportSQLite", "delete failed: ", e10);
            }
        }

        public void b(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public void c(SupportSQLiteDatabase supportSQLiteDatabase) {
            Log.e("SupportSQLite", "Corruption reported by sqlite on database: " + supportSQLiteDatabase.getPath());
            if (!supportSQLiteDatabase.isOpen()) {
                a(supportSQLiteDatabase.getPath());
                return;
            }
            List<Pair<String, String>> list = null;
            try {
                try {
                    list = supportSQLiteDatabase.f();
                } finally {
                    if (list != null) {
                        Iterator<Pair<String, String>> it = list.iterator();
                        while (it.hasNext()) {
                            a((String) it.next().second);
                        }
                    } else {
                        a(supportSQLiteDatabase.getPath());
                    }
                }
            } catch (SQLiteException unused) {
            }
            try {
                supportSQLiteDatabase.close();
            } catch (IOException unused2) {
            }
        }

        public abstract void d(SupportSQLiteDatabase supportSQLiteDatabase);

        public abstract void e(SupportSQLiteDatabase supportSQLiteDatabase, int i10, int i11);

        public void f(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public abstract void g(SupportSQLiteDatabase supportSQLiteDatabase, int i10, int i11);
    }

    /* compiled from: SupportSQLiteOpenHelper.java */
    /* renamed from: c0.c$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        public final Context f4725a;

        /* renamed from: b, reason: collision with root package name */
        public final String f4726b;

        /* renamed from: c, reason: collision with root package name */
        public final a f4727c;

        /* compiled from: SupportSQLiteOpenHelper.java */
        /* renamed from: c0.c$b$a */
        /* loaded from: classes.dex */
        public static class a {

            /* renamed from: a, reason: collision with root package name */
            Context f4728a;

            /* renamed from: b, reason: collision with root package name */
            String f4729b;

            /* renamed from: c, reason: collision with root package name */
            a f4730c;

            a(Context context) {
                this.f4728a = context;
            }

            public b a() {
                a aVar = this.f4730c;
                if (aVar != null) {
                    Context context = this.f4728a;
                    if (context != null) {
                        return new b(context, this.f4729b, aVar);
                    }
                    throw new IllegalArgumentException("Must set a non-null context to create the configuration.");
                }
                throw new IllegalArgumentException("Must set a callback to create the configuration.");
            }

            public a b(a aVar) {
                this.f4730c = aVar;
                return this;
            }

            public a c(String str) {
                this.f4729b = str;
                return this;
            }
        }

        b(Context context, String str, a aVar) {
            this.f4725a = context;
            this.f4726b = str;
            this.f4727c = aVar;
        }

        public static a a(Context context) {
            return new a(context);
        }
    }

    /* compiled from: SupportSQLiteOpenHelper.java */
    /* renamed from: c0.c$c */
    /* loaded from: classes.dex */
    public interface c {
        SupportSQLiteOpenHelper a(b bVar);
    }

    void a(boolean z10);

    SupportSQLiteDatabase b();
}
