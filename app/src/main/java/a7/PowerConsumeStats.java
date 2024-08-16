package a7;

import android.content.Context;
import android.os.SystemClock;
import b6.LocalLog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/* compiled from: PowerConsumeStats.java */
/* renamed from: a7.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class PowerConsumeStats {

    /* renamed from: g, reason: collision with root package name */
    static final Comparator<b> f68g = new a();

    /* renamed from: a, reason: collision with root package name */
    long f69a = 0;

    /* renamed from: b, reason: collision with root package name */
    int f70b = -1;

    /* renamed from: c, reason: collision with root package name */
    private boolean f71c = false;

    /* renamed from: d, reason: collision with root package name */
    private int f72d = -1;

    /* renamed from: e, reason: collision with root package name */
    int f73e = 1;

    /* renamed from: f, reason: collision with root package name */
    Context f74f;

    /* compiled from: PowerConsumeStats.java */
    /* renamed from: a7.a$a */
    /* loaded from: classes.dex */
    class a implements Comparator<b> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(b bVar, b bVar2) {
            double d10 = bVar.f77g;
            double d11 = bVar2.f77g;
            if (d10 < d11) {
                return 1;
            }
            return d10 < d11 ? -1 : 0;
        }
    }

    /* compiled from: PowerConsumeStats.java */
    /* renamed from: a7.a$b */
    /* loaded from: classes.dex */
    public static class b implements Serializable {

        /* renamed from: e, reason: collision with root package name */
        public int f75e;

        /* renamed from: f, reason: collision with root package name */
        public String f76f = "";

        /* renamed from: g, reason: collision with root package name */
        public double f77g;

        public String toString() {
            return "uid = " + this.f75e + ", pkg = " + this.f76f + ", power = " + this.f77g;
        }
    }

    public PowerConsumeStats(Context context) {
        this.f74f = context;
    }

    private int d(long j10, int i10) {
        long j11 = j10 / 60000;
        if (i10 >= 800 && j11 >= 5) {
            return 4;
        }
        if (i10 < 650 || j11 < 10) {
            return i10 >= 500 ? 2 : 1;
        }
        return 3;
    }

    public void a(int i10, int i11) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.f70b = i10;
        if (this.f72d == -1) {
            j(elapsedRealtime, i10);
        }
        LocalLog.a("PowerConsumeStats", "doMonitoring is monitoring " + this.f71c);
        if (this.f71c) {
            int i12 = this.f72d;
            if (i12 - i10 >= 3 || (i12 - i10 >= 1 && elapsedRealtime - this.f69a >= (i12 - i10) * 10 * 60000)) {
                int c10 = c(elapsedRealtime, i10);
                int d10 = d(elapsedRealtime - this.f69a, c10);
                LocalLog.a("PowerConsumeStats", "doMonitoring get new state " + d10 + ", time interval = " + (elapsedRealtime - this.f69a) + ", averageCurrent = " + c10);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("doMonitoring get old state ");
                sb2.append(this.f73e);
                LocalLog.a("PowerConsumeStats", sb2.toString());
                if (d10 != this.f73e) {
                    this.f73e = d10;
                }
            }
        }
    }

    public abstract ArrayList<b> b();

    abstract int c(long j10, int i10);

    public abstract void e();

    public void f(int i10) {
        LocalLog.a("PowerConsumeStats", "startMonitoring, old value is " + this.f71c);
        if (this.f71c) {
            return;
        }
        this.f71c = true;
        j(SystemClock.elapsedRealtime(), i10);
        g(i10);
    }

    abstract void g(int i10);

    public void h(int i10) {
        if (this.f71c) {
            this.f71c = false;
            i();
        }
    }

    abstract void i();

    public void j(long j10, int i10) {
        LocalLog.a("PowerConsumeStats", "updateMonitorInfo");
        this.f72d = i10;
        this.f69a = j10;
        k(j10, i10);
    }

    abstract void k(long j10, int i10);
}
