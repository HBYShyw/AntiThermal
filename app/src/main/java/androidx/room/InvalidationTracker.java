package androidx.room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import androidx.lifecycle.LiveData;
import c0.SimpleSQLiteQuery;
import c0.SupportSQLiteDatabase;
import c0.SupportSQLiteStatement;
import h.SafeIterableMap;
import j.ArraySet;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

/* compiled from: InvalidationTracker.java */
/* renamed from: androidx.room.e, reason: use source file name */
/* loaded from: classes.dex */
public class InvalidationTracker {

    /* renamed from: m, reason: collision with root package name */
    private static final String[] f3846m = {"UPDATE", "DELETE", "INSERT"};

    /* renamed from: b, reason: collision with root package name */
    final String[] f3848b;

    /* renamed from: c, reason: collision with root package name */
    private Map<String, Set<String>> f3849c;

    /* renamed from: d, reason: collision with root package name */
    final RoomDatabase f3850d;

    /* renamed from: g, reason: collision with root package name */
    volatile SupportSQLiteStatement f3853g;

    /* renamed from: h, reason: collision with root package name */
    private b f3854h;

    /* renamed from: i, reason: collision with root package name */
    private final InvalidationLiveDataContainer f3855i;

    /* renamed from: k, reason: collision with root package name */
    private MultiInstanceInvalidationClient f3857k;

    /* renamed from: e, reason: collision with root package name */
    AtomicBoolean f3851e = new AtomicBoolean(false);

    /* renamed from: f, reason: collision with root package name */
    private volatile boolean f3852f = false;

    /* renamed from: j, reason: collision with root package name */
    @SuppressLint({"RestrictedApi"})
    final SafeIterableMap<c, d> f3856j = new SafeIterableMap<>();

    /* renamed from: l, reason: collision with root package name */
    Runnable f3858l = new a();

    /* renamed from: a, reason: collision with root package name */
    final j.a<String, Integer> f3847a = new j.a<>();

    /* compiled from: InvalidationTracker.java */
    /* renamed from: androidx.room.e$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        private Set<Integer> a() {
            ArraySet arraySet = new ArraySet();
            Cursor r10 = InvalidationTracker.this.f3850d.r(new SimpleSQLiteQuery("SELECT * FROM room_table_modification_log WHERE invalidated = 1;"));
            while (r10.moveToNext()) {
                try {
                    arraySet.add(Integer.valueOf(r10.getInt(0)));
                } catch (Throwable th) {
                    r10.close();
                    throw th;
                }
            }
            r10.close();
            if (!arraySet.isEmpty()) {
                InvalidationTracker.this.f3853g.l();
            }
            return arraySet;
        }

        @Override // java.lang.Runnable
        public void run() {
            Lock h10 = InvalidationTracker.this.f3850d.h();
            Set<Integer> set = null;
            try {
                try {
                    h10.lock();
                } catch (SQLiteException | IllegalStateException e10) {
                    Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", e10);
                }
                if (InvalidationTracker.this.e()) {
                    if (InvalidationTracker.this.f3851e.compareAndSet(true, false)) {
                        if (InvalidationTracker.this.f3850d.m()) {
                            return;
                        }
                        RoomDatabase roomDatabase = InvalidationTracker.this.f3850d;
                        if (roomDatabase.f3899g) {
                            SupportSQLiteDatabase b10 = roomDatabase.j().b();
                            b10.e();
                            try {
                                set = a();
                                b10.A();
                                b10.P();
                            } catch (Throwable th) {
                                b10.P();
                                throw th;
                            }
                        } else {
                            set = a();
                        }
                        if (set == null || set.isEmpty()) {
                            return;
                        }
                        synchronized (InvalidationTracker.this.f3856j) {
                            Iterator<Map.Entry<c, d>> it = InvalidationTracker.this.f3856j.iterator();
                            while (it.hasNext()) {
                                it.next().getValue().a(set);
                            }
                        }
                    }
                }
            } finally {
                h10.unlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: InvalidationTracker.java */
    /* renamed from: androidx.room.e$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        final long[] f3860a;

        /* renamed from: b, reason: collision with root package name */
        final boolean[] f3861b;

        /* renamed from: c, reason: collision with root package name */
        final int[] f3862c;

        /* renamed from: d, reason: collision with root package name */
        boolean f3863d;

        /* renamed from: e, reason: collision with root package name */
        boolean f3864e;

        b(int i10) {
            long[] jArr = new long[i10];
            this.f3860a = jArr;
            boolean[] zArr = new boolean[i10];
            this.f3861b = zArr;
            this.f3862c = new int[i10];
            Arrays.fill(jArr, 0L);
            Arrays.fill(zArr, false);
        }

        int[] a() {
            synchronized (this) {
                if (this.f3863d && !this.f3864e) {
                    int length = this.f3860a.length;
                    int i10 = 0;
                    while (true) {
                        int i11 = 1;
                        if (i10 < length) {
                            boolean z10 = this.f3860a[i10] > 0;
                            boolean[] zArr = this.f3861b;
                            if (z10 != zArr[i10]) {
                                int[] iArr = this.f3862c;
                                if (!z10) {
                                    i11 = 2;
                                }
                                iArr[i10] = i11;
                            } else {
                                this.f3862c[i10] = 0;
                            }
                            zArr[i10] = z10;
                            i10++;
                        } else {
                            this.f3864e = true;
                            this.f3863d = false;
                            return this.f3862c;
                        }
                    }
                }
                return null;
            }
        }

        boolean b(int... iArr) {
            boolean z10;
            synchronized (this) {
                z10 = false;
                for (int i10 : iArr) {
                    long[] jArr = this.f3860a;
                    long j10 = jArr[i10];
                    jArr[i10] = 1 + j10;
                    if (j10 == 0) {
                        this.f3863d = true;
                        z10 = true;
                    }
                }
            }
            return z10;
        }

        boolean c(int... iArr) {
            boolean z10;
            synchronized (this) {
                z10 = false;
                for (int i10 : iArr) {
                    long[] jArr = this.f3860a;
                    long j10 = jArr[i10];
                    jArr[i10] = j10 - 1;
                    if (j10 == 1) {
                        this.f3863d = true;
                        z10 = true;
                    }
                }
            }
            return z10;
        }

        void d() {
            synchronized (this) {
                this.f3864e = false;
            }
        }
    }

    /* compiled from: InvalidationTracker.java */
    /* renamed from: androidx.room.e$c */
    /* loaded from: classes.dex */
    public static abstract class c {

        /* renamed from: a, reason: collision with root package name */
        final String[] f3865a;

        public c(String[] strArr) {
            this.f3865a = (String[]) Arrays.copyOf(strArr, strArr.length);
        }

        boolean a() {
            return false;
        }

        public abstract void b(Set<String> set);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: InvalidationTracker.java */
    /* renamed from: androidx.room.e$d */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        final int[] f3866a;

        /* renamed from: b, reason: collision with root package name */
        private final String[] f3867b;

        /* renamed from: c, reason: collision with root package name */
        final c f3868c;

        /* renamed from: d, reason: collision with root package name */
        private final Set<String> f3869d;

        d(c cVar, int[] iArr, String[] strArr) {
            this.f3868c = cVar;
            this.f3866a = iArr;
            this.f3867b = strArr;
            if (iArr.length == 1) {
                ArraySet arraySet = new ArraySet();
                arraySet.add(strArr[0]);
                this.f3869d = Collections.unmodifiableSet(arraySet);
                return;
            }
            this.f3869d = null;
        }

        void a(Set<Integer> set) {
            int length = this.f3866a.length;
            Set<String> set2 = null;
            for (int i10 = 0; i10 < length; i10++) {
                if (set.contains(Integer.valueOf(this.f3866a[i10]))) {
                    if (length == 1) {
                        set2 = this.f3869d;
                    } else {
                        if (set2 == null) {
                            set2 = new ArraySet<>(length);
                        }
                        set2.add(this.f3867b[i10]);
                    }
                }
            }
            if (set2 != null) {
                this.f3868c.b(set2);
            }
        }

        void b(String[] strArr) {
            Set<String> set = null;
            if (this.f3867b.length == 1) {
                int length = strArr.length;
                int i10 = 0;
                while (true) {
                    if (i10 >= length) {
                        break;
                    }
                    if (strArr[i10].equalsIgnoreCase(this.f3867b[0])) {
                        set = this.f3869d;
                        break;
                    }
                    i10++;
                }
            } else {
                ArraySet arraySet = new ArraySet();
                for (String str : strArr) {
                    String[] strArr2 = this.f3867b;
                    int length2 = strArr2.length;
                    int i11 = 0;
                    while (true) {
                        if (i11 < length2) {
                            String str2 = strArr2[i11];
                            if (str2.equalsIgnoreCase(str)) {
                                arraySet.add(str2);
                                break;
                            }
                            i11++;
                        }
                    }
                }
                if (arraySet.size() > 0) {
                    set = arraySet;
                }
            }
            if (set != null) {
                this.f3868c.b(set);
            }
        }
    }

    /* compiled from: InvalidationTracker.java */
    /* renamed from: androidx.room.e$e */
    /* loaded from: classes.dex */
    static class e extends c {

        /* renamed from: b, reason: collision with root package name */
        final InvalidationTracker f3870b;

        /* renamed from: c, reason: collision with root package name */
        final WeakReference<c> f3871c;

        e(InvalidationTracker invalidationTracker, c cVar) {
            super(cVar.f3865a);
            this.f3870b = invalidationTracker;
            this.f3871c = new WeakReference<>(cVar);
        }

        @Override // androidx.room.InvalidationTracker.c
        public void b(Set<String> set) {
            c cVar = this.f3871c.get();
            if (cVar == null) {
                this.f3870b.i(this);
            } else {
                cVar.b(set);
            }
        }
    }

    public InvalidationTracker(RoomDatabase roomDatabase, Map<String, String> map, Map<String, Set<String>> map2, String... strArr) {
        this.f3850d = roomDatabase;
        this.f3854h = new b(strArr.length);
        this.f3849c = map2;
        this.f3855i = new InvalidationLiveDataContainer(roomDatabase);
        int length = strArr.length;
        this.f3848b = new String[length];
        for (int i10 = 0; i10 < length; i10++) {
            String str = strArr[i10];
            Locale locale = Locale.US;
            String lowerCase = str.toLowerCase(locale);
            this.f3847a.put(lowerCase, Integer.valueOf(i10));
            String str2 = map.get(strArr[i10]);
            if (str2 != null) {
                this.f3848b[i10] = str2.toLowerCase(locale);
            } else {
                this.f3848b[i10] = lowerCase;
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = entry.getValue();
            Locale locale2 = Locale.US;
            String lowerCase2 = value.toLowerCase(locale2);
            if (this.f3847a.containsKey(lowerCase2)) {
                String lowerCase3 = entry.getKey().toLowerCase(locale2);
                j.a<String, Integer> aVar = this.f3847a;
                aVar.put(lowerCase3, aVar.get(lowerCase2));
            }
        }
    }

    private static void c(StringBuilder sb2, String str, String str2) {
        sb2.append("`");
        sb2.append("room_table_modification_trigger_");
        sb2.append(str);
        sb2.append("_");
        sb2.append(str2);
        sb2.append("`");
    }

    private String[] j(String[] strArr) {
        ArraySet arraySet = new ArraySet();
        for (String str : strArr) {
            String lowerCase = str.toLowerCase(Locale.US);
            if (this.f3849c.containsKey(lowerCase)) {
                arraySet.addAll(this.f3849c.get(lowerCase));
            } else {
                arraySet.add(str);
            }
        }
        return (String[]) arraySet.toArray(new String[arraySet.size()]);
    }

    private void l(SupportSQLiteDatabase supportSQLiteDatabase, int i10) {
        supportSQLiteDatabase.i("INSERT OR IGNORE INTO room_table_modification_log VALUES(" + i10 + ", 0)");
        String str = this.f3848b[i10];
        StringBuilder sb2 = new StringBuilder();
        for (String str2 : f3846m) {
            sb2.setLength(0);
            sb2.append("CREATE TEMP TRIGGER IF NOT EXISTS ");
            c(sb2, str, str2);
            sb2.append(" AFTER ");
            sb2.append(str2);
            sb2.append(" ON `");
            sb2.append(str);
            sb2.append("` BEGIN UPDATE ");
            sb2.append("room_table_modification_log");
            sb2.append(" SET ");
            sb2.append("invalidated");
            sb2.append(" = 1");
            sb2.append(" WHERE ");
            sb2.append("table_id");
            sb2.append(" = ");
            sb2.append(i10);
            sb2.append(" AND ");
            sb2.append("invalidated");
            sb2.append(" = 0");
            sb2.append("; END");
            supportSQLiteDatabase.i(sb2.toString());
        }
    }

    private void m(SupportSQLiteDatabase supportSQLiteDatabase, int i10) {
        String str = this.f3848b[i10];
        StringBuilder sb2 = new StringBuilder();
        for (String str2 : f3846m) {
            sb2.setLength(0);
            sb2.append("DROP TRIGGER IF EXISTS ");
            c(sb2, str, str2);
            supportSQLiteDatabase.i(sb2.toString());
        }
    }

    private String[] p(String[] strArr) {
        String[] j10 = j(strArr);
        for (String str : j10) {
            if (!this.f3847a.containsKey(str.toLowerCase(Locale.US))) {
                throw new IllegalArgumentException("There is no table with name " + str);
            }
        }
        return j10;
    }

    @SuppressLint({"RestrictedApi"})
    public void a(c cVar) {
        d i10;
        String[] j10 = j(cVar.f3865a);
        int[] iArr = new int[j10.length];
        int length = j10.length;
        for (int i11 = 0; i11 < length; i11++) {
            Integer num = this.f3847a.get(j10[i11].toLowerCase(Locale.US));
            if (num != null) {
                iArr[i11] = num.intValue();
            } else {
                throw new IllegalArgumentException("There is no table with name " + j10[i11]);
            }
        }
        d dVar = new d(cVar, iArr, j10);
        synchronized (this.f3856j) {
            i10 = this.f3856j.i(cVar, dVar);
        }
        if (i10 == null && this.f3854h.b(iArr)) {
            n();
        }
    }

    public void b(c cVar) {
        a(new e(this, cVar));
    }

    public <T> LiveData<T> d(String[] strArr, boolean z10, Callable<T> callable) {
        return this.f3855i.a(p(strArr), z10, callable);
    }

    boolean e() {
        if (!this.f3850d.q()) {
            return false;
        }
        if (!this.f3852f) {
            this.f3850d.j().b();
        }
        if (this.f3852f) {
            return true;
        }
        Log.e("ROOM", "database is not initialized even though it is open");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(SupportSQLiteDatabase supportSQLiteDatabase) {
        synchronized (this) {
            if (this.f3852f) {
                Log.e("ROOM", "Invalidation tracker is initialized twice :/.");
                return;
            }
            supportSQLiteDatabase.i("PRAGMA temp_store = MEMORY;");
            supportSQLiteDatabase.i("PRAGMA recursive_triggers='ON';");
            supportSQLiteDatabase.i("CREATE TEMP TABLE room_table_modification_log(table_id INTEGER PRIMARY KEY, invalidated INTEGER NOT NULL DEFAULT 0)");
            o(supportSQLiteDatabase);
            this.f3853g = supportSQLiteDatabase.n("UPDATE room_table_modification_log SET invalidated = 0 WHERE invalidated = 1 ");
            this.f3852f = true;
        }
    }

    public void g(String... strArr) {
        synchronized (this.f3856j) {
            Iterator<Map.Entry<c, d>> it = this.f3856j.iterator();
            while (it.hasNext()) {
                Map.Entry<c, d> next = it.next();
                if (!next.getKey().a()) {
                    next.getValue().b(strArr);
                }
            }
        }
    }

    public void h() {
        if (this.f3851e.compareAndSet(false, true)) {
            this.f3850d.k().execute(this.f3858l);
        }
    }

    @SuppressLint({"RestrictedApi"})
    public void i(c cVar) {
        d k10;
        synchronized (this.f3856j) {
            k10 = this.f3856j.k(cVar);
        }
        if (k10 == null || !this.f3854h.c(k10.f3866a)) {
            return;
        }
        n();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(Context context, String str) {
        this.f3857k = new MultiInstanceInvalidationClient(context, str, this, this.f3850d.k());
    }

    void n() {
        if (this.f3850d.q()) {
            o(this.f3850d.j().b());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(SupportSQLiteDatabase supportSQLiteDatabase) {
        if (supportSQLiteDatabase.d0()) {
            return;
        }
        while (true) {
            try {
                Lock h10 = this.f3850d.h();
                h10.lock();
                try {
                    int[] a10 = this.f3854h.a();
                    if (a10 == null) {
                        return;
                    }
                    int length = a10.length;
                    supportSQLiteDatabase.e();
                    for (int i10 = 0; i10 < length; i10++) {
                        try {
                            int i11 = a10[i10];
                            if (i11 == 1) {
                                l(supportSQLiteDatabase, i10);
                            } else if (i11 == 2) {
                                m(supportSQLiteDatabase, i10);
                            }
                        } finally {
                        }
                    }
                    supportSQLiteDatabase.A();
                    supportSQLiteDatabase.P();
                    this.f3854h.d();
                } finally {
                    h10.unlock();
                }
            } catch (SQLiteException | IllegalStateException e10) {
                Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", e10);
                return;
            }
        }
    }
}
