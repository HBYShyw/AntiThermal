package q9;

import android.os.health.TimerStat;
import android.util.ArrayMap;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.HashMap;

/* compiled from: Sensor.java */
/* renamed from: q9.k, reason: use source file name */
/* loaded from: classes2.dex */
public class Sensor extends BasicPowerItem {
    private double A;

    /* renamed from: b, reason: collision with root package name */
    private HashMap<Integer, android.hardware.Sensor> f17064b;

    /* renamed from: c, reason: collision with root package name */
    public long f17065c;

    /* renamed from: d, reason: collision with root package name */
    public double f17066d;

    /* renamed from: e, reason: collision with root package name */
    public long f17067e;

    /* renamed from: f, reason: collision with root package name */
    public double f17068f;

    /* renamed from: g, reason: collision with root package name */
    public long f17069g;

    /* renamed from: h, reason: collision with root package name */
    public double f17070h;

    /* renamed from: i, reason: collision with root package name */
    public long f17071i;

    /* renamed from: j, reason: collision with root package name */
    public double f17072j;

    /* renamed from: k, reason: collision with root package name */
    public long f17073k;

    /* renamed from: l, reason: collision with root package name */
    public double f17074l;

    /* renamed from: m, reason: collision with root package name */
    public long f17075m;

    /* renamed from: n, reason: collision with root package name */
    public double f17076n;

    /* renamed from: o, reason: collision with root package name */
    public long f17077o;

    /* renamed from: p, reason: collision with root package name */
    public double f17078p;

    /* renamed from: q, reason: collision with root package name */
    public long f17079q;

    /* renamed from: r, reason: collision with root package name */
    public double f17080r;

    /* renamed from: s, reason: collision with root package name */
    public long f17081s;

    /* renamed from: t, reason: collision with root package name */
    public double f17082t;

    /* renamed from: u, reason: collision with root package name */
    public long f17083u;

    /* renamed from: v, reason: collision with root package name */
    public double f17084v;

    /* renamed from: w, reason: collision with root package name */
    public long f17085w;

    /* renamed from: x, reason: collision with root package name */
    public double f17086x;

    /* renamed from: y, reason: collision with root package name */
    public long f17087y;

    /* renamed from: z, reason: collision with root package name */
    public double f17088z;

    public Sensor(PowerProfile powerProfile, HashMap<Integer, android.hardware.Sensor> hashMap) {
        super(powerProfile);
        this.f17065c = 0L;
        this.f17066d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17067e = 0L;
        this.f17068f = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17069g = 0L;
        this.f17070h = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17071i = 0L;
        this.f17072j = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17073k = 0L;
        this.f17074l = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17075m = 0L;
        this.f17076n = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17077o = 0L;
        this.f17078p = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17079q = 0L;
        this.f17080r = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17081s = 0L;
        this.f17082t = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17083u = 0L;
        this.f17084v = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17085w = 0L;
        this.f17086x = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17087y = 0L;
        this.f17088z = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17064b = hashMap;
        this.A = powerProfile.getAveragePower("gps.on");
    }

    public void a() {
        this.f17065c = 0L;
        this.f17066d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17077o = 0L;
        this.f17078p = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17069g = 0L;
        this.f17073k = 0L;
        this.f17070h = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17074l = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17081s = 0L;
        this.f17085w = 0L;
        this.f17082t = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17086x = UserProfileInfo.Constant.NA_LAT_LON;
    }

    public void b(UidSipper uidSipper, boolean z10) {
        float power;
        ArrayMap sensorMap = uidSipper.getSensorMap();
        double d10 = UserProfileInfo.Constant.NA_LAT_LON;
        long j10 = 0;
        double d11 = 0.0d;
        long j11 = 0;
        for (int i10 = 0; i10 < sensorMap.size(); i10++) {
            int intValue = ((Integer) sensorMap.keyAt(i10)).intValue();
            long time = (((TimerStat) sensorMap.valueAt(i10)).getTime() + 500) / 1000;
            switch (intValue) {
                case -10000:
                    d10 = (time * this.A) / 3600000.0d;
                    j11 = time;
                    continue;
                case 11:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap = this.f17064b;
                    if (hashMap != null && hashMap.containsKey(11)) {
                        power = this.f17064b.get(11).getPower();
                        break;
                    }
                    break;
                case 21:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap2 = this.f17064b;
                    if (hashMap2 != null && hashMap2.containsKey(2)) {
                        power = this.f17064b.get(2).getPower();
                        break;
                    }
                    break;
                case 31:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap3 = this.f17064b;
                    if (hashMap3 != null && hashMap3.containsKey(3)) {
                        power = this.f17064b.get(3).getPower();
                        break;
                    }
                    break;
                case 41:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap4 = this.f17064b;
                    if (hashMap4 != null && hashMap4.containsKey(4)) {
                        power = this.f17064b.get(4).getPower();
                        break;
                    }
                    break;
                case 81:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap5 = this.f17064b;
                    if (hashMap5 != null && hashMap5.containsKey(8)) {
                        power = this.f17064b.get(8).getPower();
                        break;
                    }
                    break;
                case 82:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap6 = this.f17064b;
                    if (hashMap6 != null && hashMap6.containsKey(8)) {
                        power = this.f17064b.get(8).getPower();
                        break;
                    }
                    break;
                case 91:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap7 = this.f17064b;
                    if (hashMap7 != null && hashMap7.containsKey(9)) {
                        power = this.f17064b.get(9).getPower();
                        break;
                    }
                    break;
                case 101:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap8 = this.f17064b;
                    if (hashMap8 != null && hashMap8.containsKey(10)) {
                        power = this.f17064b.get(10).getPower();
                        break;
                    }
                    break;
                case 111:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap9 = this.f17064b;
                    if (hashMap9 != null && hashMap9.containsKey(11)) {
                        power = this.f17064b.get(11).getPower();
                        break;
                    }
                    break;
                case 141:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap10 = this.f17064b;
                    if (hashMap10 != null && hashMap10.containsKey(14)) {
                        power = this.f17064b.get(14).getPower();
                        break;
                    }
                    break;
                case 161:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap11 = this.f17064b;
                    if (hashMap11 != null && hashMap11.containsKey(16)) {
                        power = this.f17064b.get(16).getPower();
                        break;
                    }
                    break;
                case 181:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap12 = this.f17064b;
                    if (hashMap12 != null && hashMap12.containsKey(18)) {
                        power = this.f17064b.get(18).getPower();
                        break;
                    }
                    break;
                case 191:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap13 = this.f17064b;
                    if (hashMap13 != null && hashMap13.containsKey(19)) {
                        power = this.f17064b.get(19).getPower();
                        break;
                    }
                    break;
                case 271:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap14 = this.f17064b;
                    if (hashMap14 != null && hashMap14.containsKey(27)) {
                        power = this.f17064b.get(27).getPower();
                        break;
                    }
                    break;
                case 351:
                    j10 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap15 = this.f17064b;
                    if (hashMap15 != null && hashMap15.containsKey(35)) {
                        power = this.f17064b.get(35).getPower();
                        break;
                    }
                    break;
            }
            d11 += (power * ((float) time)) / 3600000.0f;
        }
        if (z10) {
            this.f17065c = j11;
            this.f17066d = d10;
            this.f17077o = j10;
            this.f17078p = d11;
            this.f17067e += j11 - j11;
            this.f17068f += d10 - d10;
            this.f17079q += j10 - j10;
            this.f17080r += d11 - d11;
            return;
        }
        this.f17067e += j11 - this.f17065c;
        this.f17068f += d10 - this.f17066d;
        this.f17079q += j10 - this.f17077o;
        this.f17080r += d11 - this.f17078p;
        this.f17065c = j11;
        this.f17066d = d10;
        this.f17077o = j10;
        this.f17078p = d11;
    }

    public void c(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        float power;
        ArrayMap sensorMap = uidSipper.getSensorMap();
        double d10 = UserProfileInfo.Constant.NA_LAT_LON;
        long j10 = 0;
        double d11 = 0.0d;
        long j11 = 0;
        for (int i10 = 0; i10 < sensorMap.size(); i10++) {
            int intValue = ((Integer) sensorMap.keyAt(i10)).intValue();
            long time = (((TimerStat) sensorMap.valueAt(i10)).getTime() + 500) / 1000;
            switch (intValue) {
                case -10000:
                    d10 = (time * this.A) / 3600000.0d;
                    j10 = time;
                    continue;
                case 11:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap = this.f17064b;
                    if (hashMap != null && hashMap.containsKey(11)) {
                        power = this.f17064b.get(11).getPower();
                        break;
                    }
                    break;
                case 21:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap2 = this.f17064b;
                    if (hashMap2 != null && hashMap2.containsKey(2)) {
                        power = this.f17064b.get(2).getPower();
                        break;
                    }
                    break;
                case 31:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap3 = this.f17064b;
                    if (hashMap3 != null && hashMap3.containsKey(3)) {
                        power = this.f17064b.get(3).getPower();
                        break;
                    }
                    break;
                case 41:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap4 = this.f17064b;
                    if (hashMap4 != null && hashMap4.containsKey(4)) {
                        power = this.f17064b.get(4).getPower();
                        break;
                    }
                    break;
                case 81:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap5 = this.f17064b;
                    if (hashMap5 != null && hashMap5.containsKey(8)) {
                        power = this.f17064b.get(8).getPower();
                        break;
                    }
                    break;
                case 82:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap6 = this.f17064b;
                    if (hashMap6 != null && hashMap6.containsKey(8)) {
                        power = this.f17064b.get(8).getPower();
                        break;
                    }
                    break;
                case 91:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap7 = this.f17064b;
                    if (hashMap7 != null && hashMap7.containsKey(9)) {
                        power = this.f17064b.get(9).getPower();
                        break;
                    }
                    break;
                case 101:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap8 = this.f17064b;
                    if (hashMap8 != null && hashMap8.containsKey(10)) {
                        power = this.f17064b.get(10).getPower();
                        break;
                    }
                    break;
                case 111:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap9 = this.f17064b;
                    if (hashMap9 != null && hashMap9.containsKey(11)) {
                        power = this.f17064b.get(11).getPower();
                        break;
                    }
                    break;
                case 141:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap10 = this.f17064b;
                    if (hashMap10 != null && hashMap10.containsKey(14)) {
                        power = this.f17064b.get(14).getPower();
                        break;
                    }
                    break;
                case 161:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap11 = this.f17064b;
                    if (hashMap11 != null && hashMap11.containsKey(16)) {
                        power = this.f17064b.get(16).getPower();
                        break;
                    }
                    break;
                case 181:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap12 = this.f17064b;
                    if (hashMap12 != null && hashMap12.containsKey(18)) {
                        power = this.f17064b.get(18).getPower();
                        break;
                    }
                    break;
                case 191:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap13 = this.f17064b;
                    if (hashMap13 != null && hashMap13.containsKey(19)) {
                        power = this.f17064b.get(19).getPower();
                        break;
                    }
                    break;
                case 271:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap14 = this.f17064b;
                    if (hashMap14 != null && hashMap14.containsKey(27)) {
                        power = this.f17064b.get(27).getPower();
                        break;
                    }
                    break;
                case 351:
                    j11 += time;
                    HashMap<Integer, android.hardware.Sensor> hashMap15 = this.f17064b;
                    if (hashMap15 != null && hashMap15.containsKey(35)) {
                        power = this.f17064b.get(35).getPower();
                        break;
                    }
                    break;
            }
            d11 += (power * ((float) time)) / 3600000.0f;
        }
        if (z10 && z11) {
            this.f17069g = j10;
            this.f17070h = d10;
            this.f17081s = j11;
            this.f17082t = d11;
            return;
        }
        if (z10 && !z11) {
            if (!z12) {
                this.f17069g = j10;
                this.f17070h = d10;
                this.f17081s = j11;
                this.f17082t = d11;
                this.f17075m += j10 - this.f17073k;
                this.f17076n += d10 - this.f17074l;
                this.f17087y += j11 - this.f17085w;
                this.f17088z += d11 - this.f17086x;
            } else {
                this.f17075m += j10 - this.f17073k;
                this.f17076n += d10 - this.f17074l;
                this.f17087y += j11 - this.f17085w;
                this.f17088z += d11 - this.f17086x;
                this.f17073k = j10;
                this.f17074l = d10;
                this.f17085w = j11;
                this.f17086x = d11;
            }
        }
        if (z10 || z11) {
            return;
        }
        this.f17073k = j10;
        this.f17074l = d10;
        this.f17085w = j11;
        this.f17086x = d11;
        this.f17071i += j10 - this.f17069g;
        this.f17072j += d10 - this.f17070h;
        this.f17083u += j11 - this.f17081s;
        this.f17084v += d11 - this.f17082t;
    }

    public void d() {
        this.f17067e = 0L;
        this.f17068f = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17071i = 0L;
        this.f17072j = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17075m = 0L;
        this.f17076n = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17079q = 0L;
        this.f17080r = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17083u = 0L;
        this.f17084v = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17087y = 0L;
        this.f17088z = UserProfileInfo.Constant.NA_LAT_LON;
    }
}
