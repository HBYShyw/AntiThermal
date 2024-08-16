package q9;

import android.os.health.TimerStat;
import android.util.ArrayMap;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;

/* compiled from: Job.java */
/* loaded from: classes2.dex */
public class g extends BasicPowerItem {

    /* renamed from: b, reason: collision with root package name */
    public int f17012b;

    /* renamed from: c, reason: collision with root package name */
    public int f17013c;

    /* renamed from: d, reason: collision with root package name */
    public long f17014d;

    /* renamed from: e, reason: collision with root package name */
    public long f17015e;

    /* renamed from: f, reason: collision with root package name */
    public int f17016f;

    /* renamed from: g, reason: collision with root package name */
    public int f17017g;

    /* renamed from: h, reason: collision with root package name */
    public long f17018h;

    /* renamed from: i, reason: collision with root package name */
    public long f17019i;

    /* renamed from: j, reason: collision with root package name */
    public int f17020j;

    /* renamed from: k, reason: collision with root package name */
    public int f17021k;

    /* renamed from: l, reason: collision with root package name */
    public long f17022l;

    /* renamed from: m, reason: collision with root package name */
    public long f17023m;

    public g(PowerProfile powerProfile) {
        super(powerProfile);
        this.f17012b = 0;
        this.f17013c = 0;
        this.f17014d = 0L;
        this.f17015e = 0L;
        this.f17016f = 0;
        this.f17017g = 0;
        this.f17018h = 0L;
        this.f17019i = 0L;
        this.f17020j = 0;
        this.f17021k = 0;
        this.f17022l = 0L;
        this.f17023m = 0L;
    }

    public void a() {
        this.f17012b = 0;
        this.f17014d = 0L;
        this.f17016f = 0;
        this.f17020j = 0;
        this.f17018h = 0L;
        this.f17022l = 0L;
    }

    public void b(UidSipper uidSipper, boolean z10) {
        ArrayMap jobMap = uidSipper.getJobMap();
        int i10 = 0;
        long j10 = 0;
        if (jobMap.size() != 0) {
            int i11 = 0;
            while (i10 < jobMap.size()) {
                TimerStat timerStat = (TimerStat) jobMap.valueAt(i10);
                i11 += timerStat.getCount();
                j10 += (timerStat.getTime() + 500) / 1000;
                i10++;
            }
            i10 = i11;
        }
        if (z10) {
            this.f17012b = i10;
            this.f17014d = j10;
            this.f17013c += i10 - i10;
            this.f17015e += j10 - j10;
            return;
        }
        this.f17013c += i10 - this.f17012b;
        this.f17015e += j10 - this.f17014d;
        this.f17012b = i10;
        this.f17014d = j10;
    }

    public void c(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        if (uidSipper == null) {
            return;
        }
        ArrayMap jobMap = uidSipper.getJobMap();
        int i10 = 0;
        long j10 = 0;
        if (jobMap.size() != 0) {
            int i11 = 0;
            while (i10 < jobMap.size()) {
                TimerStat timerStat = (TimerStat) jobMap.valueAt(i10);
                i11 += timerStat.getCount();
                j10 += (timerStat.getTime() + 500) / 1000;
                i10++;
            }
            i10 = i11;
        }
        if (z10 && z11) {
            this.f17016f = i10;
            this.f17018h = j10;
        }
        if (z10 && !z11) {
            if (!z12) {
                this.f17016f = i10;
                this.f17021k += i10 - this.f17020j;
                this.f17018h = j10;
                this.f17023m += j10 - this.f17022l;
            } else {
                this.f17021k += i10 - this.f17020j;
                this.f17023m += j10 - this.f17022l;
                this.f17020j = i10;
                this.f17022l = j10;
            }
        }
        if (z10 || z11) {
            return;
        }
        this.f17020j = i10;
        this.f17017g += i10 - this.f17016f;
        this.f17022l = j10;
        this.f17019i += j10 - this.f17018h;
    }

    public void d() {
        this.f17013c = 0;
        this.f17015e = 0L;
        this.f17017g = 0;
        this.f17019i = 0L;
        this.f17021k = 0;
        this.f17023m = 0L;
    }
}
