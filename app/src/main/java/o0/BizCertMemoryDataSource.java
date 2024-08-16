package o0;

import e1.SceneUtil;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import s0.BizPublicKeyConfig;
import t0.BizDataNotFoundException;
import x0.LocalBizKeyPairs;
import x0.UpgradeCertResponse;

/* compiled from: BizCertMemoryDataSource.java */
/* renamed from: o0.a, reason: use source file name */
/* loaded from: classes.dex */
public class BizCertMemoryDataSource {

    /* renamed from: e, reason: collision with root package name */
    private final Map<String, UpgradeCertResponse> f16096e;

    /* renamed from: h, reason: collision with root package name */
    private long f16099h;

    /* renamed from: i, reason: collision with root package name */
    private long f16100i;

    /* renamed from: a, reason: collision with root package name */
    private final Object f16092a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private final Object f16093b = new Object();

    /* renamed from: c, reason: collision with root package name */
    private final Object f16094c = new Object();

    /* renamed from: d, reason: collision with root package name */
    private final Object f16095d = new Object();

    /* renamed from: f, reason: collision with root package name */
    private final Map<String, LocalBizKeyPairs> f16097f = new ConcurrentHashMap();

    /* renamed from: g, reason: collision with root package name */
    private final Map<String, BizPublicKeyConfig> f16098g = new ConcurrentHashMap();

    public BizCertMemoryDataSource(Set<String> set) {
        this.f16096e = SceneUtil.c(set);
    }

    public long a() {
        long j10;
        synchronized (this.f16092a) {
            j10 = this.f16099h;
        }
        return j10;
    }

    public synchronized BizPublicKeyConfig b(String str) {
        return this.f16098g.get(str);
    }

    public long c() {
        long j10;
        synchronized (this.f16093b) {
            j10 = this.f16100i;
        }
        return j10;
    }

    public LocalBizKeyPairs d(String str) {
        synchronized (this.f16095d) {
            if (!this.f16097f.containsKey(str)) {
                return null;
            }
            return this.f16097f.get(str);
        }
    }

    public Set<String> e() {
        Set<String> keySet;
        synchronized (this.f16095d) {
            keySet = this.f16097f.keySet();
        }
        return keySet;
    }

    public UpgradeCertResponse f(String str) {
        UpgradeCertResponse upgradeCertResponse;
        synchronized (this.f16094c) {
            if (this.f16096e.containsKey(str)) {
                upgradeCertResponse = this.f16096e.get(str);
            } else {
                throw new BizDataNotFoundException("please specify the correct biz name but not " + str);
            }
        }
        return upgradeCertResponse;
    }

    public void g(long j10) {
        synchronized (this.f16092a) {
            this.f16099h = j10;
        }
    }

    public synchronized void h(String str, BizPublicKeyConfig bizPublicKeyConfig) {
        this.f16098g.put(str, bizPublicKeyConfig);
    }

    public void i(long j10) {
        synchronized (this.f16093b) {
            this.f16100i = j10;
        }
    }

    public synchronized void j(String str, LocalBizKeyPairs localBizKeyPairs) {
        synchronized (this.f16095d) {
            this.f16097f.put(str, localBizKeyPairs);
        }
    }
}
