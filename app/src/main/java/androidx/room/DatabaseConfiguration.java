package androidx.room;

import android.content.Context;
import androidx.room.RoomDatabase;
import c0.SupportSQLiteOpenHelper;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/* compiled from: DatabaseConfiguration.java */
/* renamed from: androidx.room.a, reason: use source file name */
/* loaded from: classes.dex */
public class DatabaseConfiguration {

    /* renamed from: a, reason: collision with root package name */
    public final SupportSQLiteOpenHelper.c f3829a;

    /* renamed from: b, reason: collision with root package name */
    public final Context f3830b;

    /* renamed from: c, reason: collision with root package name */
    public final String f3831c;

    /* renamed from: d, reason: collision with root package name */
    public final RoomDatabase.d f3832d;

    /* renamed from: e, reason: collision with root package name */
    public final List<RoomDatabase.b> f3833e;

    /* renamed from: f, reason: collision with root package name */
    public final boolean f3834f;

    /* renamed from: g, reason: collision with root package name */
    public final RoomDatabase.c f3835g;

    /* renamed from: h, reason: collision with root package name */
    public final Executor f3836h;

    /* renamed from: i, reason: collision with root package name */
    public final Executor f3837i;

    /* renamed from: j, reason: collision with root package name */
    public final boolean f3838j;

    /* renamed from: k, reason: collision with root package name */
    public final boolean f3839k;

    /* renamed from: l, reason: collision with root package name */
    public final boolean f3840l;

    /* renamed from: m, reason: collision with root package name */
    private final Set<Integer> f3841m;

    public DatabaseConfiguration(Context context, String str, SupportSQLiteOpenHelper.c cVar, RoomDatabase.d dVar, List<RoomDatabase.b> list, boolean z10, RoomDatabase.c cVar2, Executor executor, Executor executor2, boolean z11, boolean z12, boolean z13, Set<Integer> set) {
        this.f3829a = cVar;
        this.f3830b = context;
        this.f3831c = str;
        this.f3832d = dVar;
        this.f3833e = list;
        this.f3834f = z10;
        this.f3835g = cVar2;
        this.f3836h = executor;
        this.f3837i = executor2;
        this.f3838j = z11;
        this.f3839k = z12;
        this.f3840l = z13;
        this.f3841m = set;
    }

    public boolean a(int i10, int i11) {
        Set<Integer> set;
        if ((i10 > i11) && this.f3840l) {
            return false;
        }
        return this.f3839k && ((set = this.f3841m) == null || !set.contains(Integer.valueOf(i10)));
    }
}
