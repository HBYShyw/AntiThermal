package z6;

import android.content.Context;
import android.util.Log;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import o9.HighPowerHelper;
import o9.HighPowerSipper;

/* compiled from: HighPowerDetector.java */
/* renamed from: z6.a, reason: use source file name */
/* loaded from: classes.dex */
public class HighPowerDetector extends PowerDetector {

    /* renamed from: c, reason: collision with root package name */
    private Context f20242c;

    /* renamed from: d, reason: collision with root package name */
    HashMap<String, HighPowerSipper> f20243d = new HashMap<>();

    public HighPowerDetector(Context context) {
        this.f20242c = null;
        this.f20242c = context;
    }

    private ConcurrentHashMap<String, HighPowerSipper> d() {
        Log.d("HighPowerDetector", "refresh battery stats");
        return HighPowerHelper.f(this.f20242c).e();
    }

    @Override // z6.PowerDetector
    public ConcurrentHashMap<String, HighPowerSipper> a() {
        return c();
    }

    @Override // z6.PowerDetector
    public void b(long j10, int i10) {
        this.f20243d.clear();
        this.f20243d.putAll(d());
    }

    public ConcurrentHashMap<String, HighPowerSipper> c() {
        return d();
    }
}
