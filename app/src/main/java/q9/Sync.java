package q9;

import android.os.health.TimerStat;
import android.util.ArrayMap;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;

/* compiled from: Sync.java */
/* renamed from: q9.l, reason: use source file name */
/* loaded from: classes2.dex */
public class Sync extends BasicPowerItem {

    /* renamed from: b, reason: collision with root package name */
    public int f17089b;

    /* renamed from: c, reason: collision with root package name */
    public int f17090c;

    /* renamed from: d, reason: collision with root package name */
    public long f17091d;

    /* renamed from: e, reason: collision with root package name */
    public long f17092e;

    /* renamed from: f, reason: collision with root package name */
    public int f17093f;

    /* renamed from: g, reason: collision with root package name */
    public int f17094g;

    /* renamed from: h, reason: collision with root package name */
    public long f17095h;

    /* renamed from: i, reason: collision with root package name */
    public long f17096i;

    /* renamed from: j, reason: collision with root package name */
    public int f17097j;

    /* renamed from: k, reason: collision with root package name */
    public int f17098k;

    /* renamed from: l, reason: collision with root package name */
    public long f17099l;

    /* renamed from: m, reason: collision with root package name */
    public long f17100m;

    public Sync(PowerProfile powerProfile) {
        super(powerProfile);
        this.f17089b = 0;
        this.f17090c = 0;
        this.f17091d = 0L;
        this.f17092e = 0L;
        this.f17093f = 0;
        this.f17094g = 0;
        this.f17095h = 0L;
        this.f17096i = 0L;
        this.f17097j = 0;
        this.f17098k = 0;
        this.f17099l = 0L;
        this.f17100m = 0L;
    }

    public void a() {
        this.f17089b = 0;
        this.f17091d = 0L;
        this.f17093f = 0;
        this.f17097j = 0;
        this.f17095h = 0L;
        this.f17099l = 0L;
    }

    public void b(UidSipper uidSipper, boolean z10) {
        ArrayMap syncMap = uidSipper.getSyncMap();
        int i10 = 0;
        long j10 = 0;
        if (syncMap.size() != 0) {
            int i11 = 0;
            while (i10 < syncMap.size()) {
                TimerStat timerStat = (TimerStat) syncMap.valueAt(i10);
                i11 += timerStat.getCount();
                j10 += (timerStat.getTime() + 500) / 1000;
                i10++;
            }
            i10 = i11;
        }
        if (z10) {
            this.f17089b = i10;
            this.f17091d = j10;
            this.f17090c += i10 - i10;
            this.f17092e += j10 - j10;
            return;
        }
        this.f17090c += i10 - this.f17089b;
        this.f17092e += j10 - this.f17091d;
        this.f17089b = i10;
        this.f17091d = j10;
    }

    public void c(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        if (uidSipper == null) {
            return;
        }
        ArrayMap syncMap = uidSipper.getSyncMap();
        int i10 = 0;
        long j10 = 0;
        if (syncMap.size() != 0) {
            int i11 = 0;
            while (i10 < syncMap.size()) {
                TimerStat timerStat = (TimerStat) syncMap.valueAt(i10);
                i11 += timerStat.getCount();
                j10 += (timerStat.getTime() + 500) / 1000;
                i10++;
            }
            i10 = i11;
        }
        if (z10 && z11) {
            this.f17093f = i10;
            this.f17095h = j10;
        }
        if (z10 && !z11) {
            if (z12) {
                this.f17093f = i10;
                this.f17098k += i10 - this.f17097j;
                this.f17095h = j10;
                this.f17100m += j10 - this.f17099l;
            } else {
                this.f17098k += i10 - this.f17097j;
                this.f17100m += j10 - this.f17099l;
                this.f17097j = i10;
                this.f17099l = j10;
            }
        }
        if (z10 || z11) {
            return;
        }
        this.f17097j = i10;
        this.f17094g += i10 - this.f17093f;
        this.f17099l = j10;
        this.f17096i += j10 - this.f17095h;
    }

    public void d() {
        this.f17090c = 0;
        this.f17092e = 0L;
        this.f17094g = 0;
        this.f17096i = 0L;
        this.f17098k = 0;
        this.f17100m = 0L;
    }
}
