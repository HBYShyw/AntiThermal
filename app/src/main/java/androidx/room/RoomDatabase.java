package androidx.room;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Looper;
import android.util.Log;
import androidx.core.app.ActivityManagerCompat;
import c0.SupportSQLiteDatabase;
import c0.SupportSQLiteOpenHelper;
import c0.SupportSQLiteQuery;
import c0.SupportSQLiteStatement;
import d0.FrameworkSQLiteOpenHelperFactory;
import g.ArchTaskExecutor;
import j.SparseArrayCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import z.Migration;

/* compiled from: RoomDatabase.java */
/* renamed from: androidx.room.h, reason: use source file name */
/* loaded from: classes.dex */
public abstract class RoomDatabase {

    /* renamed from: a, reason: collision with root package name */
    @Deprecated
    protected volatile SupportSQLiteDatabase f3893a;

    /* renamed from: b, reason: collision with root package name */
    private Executor f3894b;

    /* renamed from: c, reason: collision with root package name */
    private Executor f3895c;

    /* renamed from: d, reason: collision with root package name */
    private SupportSQLiteOpenHelper f3896d;

    /* renamed from: f, reason: collision with root package name */
    private boolean f3898f;

    /* renamed from: g, reason: collision with root package name */
    boolean f3899g;

    /* renamed from: h, reason: collision with root package name */
    @Deprecated
    protected List<b> f3900h;

    /* renamed from: i, reason: collision with root package name */
    private final ReentrantReadWriteLock f3901i = new ReentrantReadWriteLock();

    /* renamed from: j, reason: collision with root package name */
    private final ThreadLocal<Integer> f3902j = new ThreadLocal<>();

    /* renamed from: k, reason: collision with root package name */
    private final Map<String, Object> f3903k = new ConcurrentHashMap();

    /* renamed from: e, reason: collision with root package name */
    private final InvalidationTracker f3897e = e();

    /* compiled from: RoomDatabase.java */
    /* renamed from: androidx.room.h$a */
    /* loaded from: classes.dex */
    public static class a<T extends RoomDatabase> {

        /* renamed from: a, reason: collision with root package name */
        private final Class<T> f3904a;

        /* renamed from: b, reason: collision with root package name */
        private final String f3905b;

        /* renamed from: c, reason: collision with root package name */
        private final Context f3906c;

        /* renamed from: d, reason: collision with root package name */
        private ArrayList<b> f3907d;

        /* renamed from: e, reason: collision with root package name */
        private Executor f3908e;

        /* renamed from: f, reason: collision with root package name */
        private Executor f3909f;

        /* renamed from: g, reason: collision with root package name */
        private SupportSQLiteOpenHelper.c f3910g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f3911h;

        /* renamed from: j, reason: collision with root package name */
        private boolean f3913j;

        /* renamed from: l, reason: collision with root package name */
        private boolean f3915l;

        /* renamed from: n, reason: collision with root package name */
        private Set<Integer> f3917n;

        /* renamed from: o, reason: collision with root package name */
        private Set<Integer> f3918o;

        /* renamed from: i, reason: collision with root package name */
        private c f3912i = c.AUTOMATIC;

        /* renamed from: k, reason: collision with root package name */
        private boolean f3914k = true;

        /* renamed from: m, reason: collision with root package name */
        private final d f3916m = new d();

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(Context context, Class<T> cls, String str) {
            this.f3906c = context;
            this.f3904a = cls;
            this.f3905b = str;
        }

        public a<T> a(b bVar) {
            if (this.f3907d == null) {
                this.f3907d = new ArrayList<>();
            }
            this.f3907d.add(bVar);
            return this;
        }

        public a<T> b(Migration... migrationArr) {
            if (this.f3918o == null) {
                this.f3918o = new HashSet();
            }
            for (Migration migration : migrationArr) {
                this.f3918o.add(Integer.valueOf(migration.f20163a));
                this.f3918o.add(Integer.valueOf(migration.f20164b));
            }
            this.f3916m.b(migrationArr);
            return this;
        }

        public a<T> c() {
            this.f3911h = true;
            return this;
        }

        @SuppressLint({"RestrictedApi"})
        public T d() {
            Executor executor;
            if (this.f3906c != null) {
                if (this.f3904a != null) {
                    Executor executor2 = this.f3908e;
                    if (executor2 == null && this.f3909f == null) {
                        Executor e10 = ArchTaskExecutor.e();
                        this.f3909f = e10;
                        this.f3908e = e10;
                    } else if (executor2 != null && this.f3909f == null) {
                        this.f3909f = executor2;
                    } else if (executor2 == null && (executor = this.f3909f) != null) {
                        this.f3908e = executor;
                    }
                    Set<Integer> set = this.f3918o;
                    if (set != null && this.f3917n != null) {
                        for (Integer num : set) {
                            if (this.f3917n.contains(num)) {
                                throw new IllegalArgumentException("Inconsistency detected. A Migration was supplied to addMigration(Migration... migrations) that has a start or end version equal to a start version supplied to fallbackToDestructiveMigrationFrom(int... startVersions). Start version: " + num);
                            }
                        }
                    }
                    if (this.f3910g == null) {
                        this.f3910g = new FrameworkSQLiteOpenHelperFactory();
                    }
                    Context context = this.f3906c;
                    DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(context, this.f3905b, this.f3910g, this.f3916m, this.f3907d, this.f3911h, this.f3912i.a(context), this.f3908e, this.f3909f, this.f3913j, this.f3914k, this.f3915l, this.f3917n);
                    T t7 = (T) Room.b(this.f3904a, "_Impl");
                    t7.n(databaseConfiguration);
                    return t7;
                }
                throw new IllegalArgumentException("Must provide an abstract class that extends RoomDatabase");
            }
            throw new IllegalArgumentException("Cannot provide null context for the database.");
        }

        public a<T> e() {
            this.f3913j = this.f3905b != null;
            return this;
        }

        public a<T> f() {
            this.f3914k = false;
            this.f3915l = true;
            return this;
        }
    }

    /* compiled from: RoomDatabase.java */
    /* renamed from: androidx.room.h$b */
    /* loaded from: classes.dex */
    public static abstract class b {
        public void a(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public void b(SupportSQLiteDatabase supportSQLiteDatabase) {
        }
    }

    /* compiled from: RoomDatabase.java */
    /* renamed from: androidx.room.h$c */
    /* loaded from: classes.dex */
    public enum c {
        AUTOMATIC,
        TRUNCATE,
        WRITE_AHEAD_LOGGING;

        @SuppressLint({"NewApi"})
        c a(Context context) {
            if (this != AUTOMATIC) {
                return this;
            }
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (activityManager != null && !ActivityManagerCompat.a(activityManager)) {
                return WRITE_AHEAD_LOGGING;
            }
            return TRUNCATE;
        }
    }

    /* compiled from: RoomDatabase.java */
    /* renamed from: androidx.room.h$d */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        private SparseArrayCompat<SparseArrayCompat<Migration>> f3923a = new SparseArrayCompat<>();

        private void a(Migration migration) {
            int i10 = migration.f20163a;
            int i11 = migration.f20164b;
            SparseArrayCompat<Migration> e10 = this.f3923a.e(i10);
            if (e10 == null) {
                e10 = new SparseArrayCompat<>();
                this.f3923a.i(i10, e10);
            }
            Migration e11 = e10.e(i11);
            if (e11 != null) {
                Log.w("ROOM", "Overriding migration " + e11 + " with " + migration);
            }
            e10.a(i11, migration);
        }

        /* JADX WARN: Removed duplicated region for block: B:34:0x0019 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:9:0x001a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private List<Migration> d(List<Migration> list, boolean z10, int i10, int i11) {
            SparseArrayCompat<Migration> e10;
            boolean z11;
            int i12;
            int i13;
            int i14 = z10 ? -1 : 1;
            do {
                if (z10) {
                    if (i10 >= i11) {
                        return list;
                    }
                    e10 = this.f3923a.e(i10);
                    if (e10 == null) {
                        int k10 = e10.k();
                        z11 = false;
                        if (z10) {
                            i13 = k10 - 1;
                            i12 = -1;
                        } else {
                            i12 = k10;
                            i13 = 0;
                        }
                        while (true) {
                            if (i13 == i12) {
                                break;
                            }
                            int h10 = e10.h(i13);
                            if (!z10 ? h10 < i11 || h10 >= i10 : h10 > i11 || h10 <= i10) {
                                list.add(e10.l(i13));
                                z11 = true;
                                i10 = h10;
                                break;
                            }
                            i13 += i14;
                        }
                    } else {
                        return null;
                    }
                } else {
                    if (i10 <= i11) {
                        return list;
                    }
                    e10 = this.f3923a.e(i10);
                    if (e10 == null) {
                    }
                }
            } while (z11);
            return null;
        }

        public void b(Migration... migrationArr) {
            for (Migration migration : migrationArr) {
                a(migration);
            }
        }

        public List<Migration> c(int i10, int i11) {
            if (i10 == i11) {
                return Collections.emptyList();
            }
            return d(new ArrayList(), i11 > i10, i10, i11);
        }
    }

    private static boolean p() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public void a() {
        if (!this.f3898f && p()) {
            throw new IllegalStateException("Cannot access database on the main thread since it may potentially lock the UI for a long period of time.");
        }
    }

    public void b() {
        if (!m() && this.f3902j.get() != null) {
            throw new IllegalStateException("Cannot access database on a different coroutine context inherited from a suspending transaction.");
        }
    }

    @Deprecated
    public void c() {
        a();
        SupportSQLiteDatabase b10 = this.f3896d.b();
        this.f3897e.o(b10);
        b10.e();
    }

    public SupportSQLiteStatement d(String str) {
        a();
        b();
        return this.f3896d.b().n(str);
    }

    protected abstract InvalidationTracker e();

    protected abstract SupportSQLiteOpenHelper f(DatabaseConfiguration databaseConfiguration);

    @Deprecated
    public void g() {
        this.f3896d.b().P();
        if (m()) {
            return;
        }
        this.f3897e.h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Lock h() {
        return this.f3901i.readLock();
    }

    public InvalidationTracker i() {
        return this.f3897e;
    }

    public SupportSQLiteOpenHelper j() {
        return this.f3896d;
    }

    public Executor k() {
        return this.f3894b;
    }

    public Executor l() {
        return this.f3895c;
    }

    public boolean m() {
        return this.f3896d.b().d0();
    }

    public void n(DatabaseConfiguration databaseConfiguration) {
        SupportSQLiteOpenHelper f10 = f(databaseConfiguration);
        this.f3896d = f10;
        boolean z10 = databaseConfiguration.f3835g == c.WRITE_AHEAD_LOGGING;
        f10.a(z10);
        this.f3900h = databaseConfiguration.f3833e;
        this.f3894b = databaseConfiguration.f3836h;
        this.f3895c = new TransactionExecutor(databaseConfiguration.f3837i);
        this.f3898f = databaseConfiguration.f3834f;
        this.f3899g = z10;
        if (databaseConfiguration.f3838j) {
            this.f3897e.k(databaseConfiguration.f3830b, databaseConfiguration.f3831c);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void o(SupportSQLiteDatabase supportSQLiteDatabase) {
        this.f3897e.f(supportSQLiteDatabase);
    }

    public boolean q() {
        SupportSQLiteDatabase supportSQLiteDatabase = this.f3893a;
        return supportSQLiteDatabase != null && supportSQLiteDatabase.isOpen();
    }

    public Cursor r(SupportSQLiteQuery supportSQLiteQuery) {
        a();
        b();
        return this.f3896d.b().q(supportSQLiteQuery);
    }

    @Deprecated
    public void s() {
        this.f3896d.b().A();
    }
}
