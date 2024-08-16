package m0;

import android.content.Context;
import e1.Base64Utils;
import e1.DateUtil;
import e1.KeyUtil;
import e1.SceneUtil;
import e1.ThreadUtil;
import i0.AuthException;
import i0.EncryptException;
import i0.Session;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import l0.DeviceCertAuthConfig;
import m0.CryptoCore;
import n0.CryptoSceneDataRepository;
import o0.BizCertMemoryDataSource;
import org.json.JSONException;
import org.json.JSONObject;
import s0.BizPublicKeyConfig;
import s0.CertUpgradeCycleEnum;
import s0.CryptoInitParameters;
import s0.NegotiationParam;
import s0.SceneConfig;
import s0.n;
import t0.BizDataNotFoundException;
import t0.InvalidArgumentException;
import t0.SceneNotFoundException;
import w0.CertUpgradeManager;
import w0.KeyRegisterManager;
import x0.LocalBizKeyPairs;
import x0.UpgradeCertResponse;

/* compiled from: CryptoCore.java */
/* renamed from: m0.i, reason: use source file name */
/* loaded from: classes.dex */
public class CryptoCore {

    /* renamed from: c, reason: collision with root package name */
    private final Context f14876c;

    /* renamed from: d, reason: collision with root package name */
    private final String f14877d;

    /* renamed from: e, reason: collision with root package name */
    private final Map<String, String> f14878e;

    /* renamed from: f, reason: collision with root package name */
    private final Set<String> f14879f;

    /* renamed from: g, reason: collision with root package name */
    private final Set<String> f14880g;

    /* renamed from: h, reason: collision with root package name */
    private final Map<String, SceneConfig> f14881h;

    /* renamed from: i, reason: collision with root package name */
    private final CryptoSceneDataRepository f14882i;

    /* renamed from: j, reason: collision with root package name */
    private final BizCertMemoryDataSource f14883j;

    /* renamed from: k, reason: collision with root package name */
    private final int f14884k;

    /* renamed from: a, reason: collision with root package name */
    private final androidx.core.util.e<Session> f14874a = new androidx.core.util.g(5);

    /* renamed from: b, reason: collision with root package name */
    private final Object f14875b = new Object();

    /* renamed from: l, reason: collision with root package name */
    private final Object f14885l = new Object();

    /* renamed from: m, reason: collision with root package name */
    private Future f14886m = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CryptoCore.java */
    /* renamed from: m0.i$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Set f14887e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f14888f;

        a(Set set, int i10) {
            this.f14887e = set;
            this.f14888f = i10;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void b(Set set) {
            CryptoCore.this.M(set);
        }

        @Override // java.lang.Runnable
        public void run() {
            final Set set = this.f14887e;
            ThreadUtil.c(new Runnable() { // from class: m0.h
                @Override // java.lang.Runnable
                public final void run() {
                    CryptoCore.a.this.b(set);
                }
            });
            ThreadUtil.a().postDelayed(this, this.f14888f * 1000);
        }
    }

    public CryptoCore(CryptoInitParameters cryptoInitParameters, int i10) {
        this.f14876c = cryptoInitParameters.d();
        this.f14877d = cryptoInitParameters.e();
        Map<String, String> k10 = SceneUtil.k(cryptoInitParameters.a());
        this.f14878e = k10;
        this.f14881h = SceneUtil.b(cryptoInitParameters.g());
        this.f14882i = new CryptoSceneDataRepository();
        BizCertMemoryDataSource bizCertMemoryDataSource = new BizCertMemoryDataSource(k10.keySet());
        this.f14883j = bizCertMemoryDataSource;
        this.f14884k = i10;
        this.f14879f = SceneUtil.i(cryptoInitParameters.f(), k10.keySet());
        this.f14880g = SceneUtil.h(cryptoInitParameters.h(), cryptoInitParameters.b(), bizCertMemoryDataSource);
        v(cryptoInitParameters.c());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void A(String str) {
        HashSet hashSet = new HashSet();
        hashSet.add(str);
        H(hashSet, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void C(final Set set, final int i10) {
        ThreadUtil.c(new Runnable() { // from class: m0.e
            @Override // java.lang.Runnable
            public final void run() {
                CryptoCore.this.B(set, i10);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void D(CertUpgradeCycleEnum certUpgradeCycleEnum) {
        Set<String> m10 = w0.c.m(this.f14876c, this.f14883j, this.f14878e);
        w0.c.p(this.f14876c, this.f14879f, this.f14883j, this.f14878e, this.f14877d);
        u(m10, certUpgradeCycleEnum);
        w(this.f14879f);
        HashSet hashSet = new HashSet(m10);
        hashSet.addAll(this.f14880g);
        F(hashSet);
        e1.i.a("CryptoCore", "initCryptoResource finish");
    }

    private void F(Set<String> set) {
        Map<String, n> j10 = SceneUtil.j(this, set);
        if (j10 == null) {
            return;
        }
        for (Map.Entry<String, n> entry : j10.entrySet()) {
            String[] r10 = SceneUtil.r(entry.getKey());
            if (r10 != null) {
                String str = r10[0];
                synchronized (this.f14875b) {
                    this.f14882i.b(str, entry.getValue());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: G, reason: merged with bridge method [inline-methods] */
    public void E(String str, SceneConfig sceneConfig, n nVar) {
        try {
            JSONObject jSONObject = new JSONObject();
            if (SceneUtil.g(jSONObject, nVar, this.f14878e.get(str), this.f14877d) && SceneUtil.f(jSONObject, str, sceneConfig.c(), this.f14883j, x(str))) {
                SceneUtil.n(this.f14876c, str, sceneConfig.d(), jSONObject.toString());
            }
        } catch (KeyStoreException | JSONException e10) {
            e1.i.d("CryptoCore", "localSaveSceneData " + str + " sceneData save error. " + e10);
        }
    }

    private void H(Set<String> set, boolean z10) {
        if (set == null || set.isEmpty()) {
            return;
        }
        if (Thread.currentThread().isInterrupted()) {
            e1.i.a("CryptoCore", "registerLocalPublicKeys current thread is interrupted before register");
            return;
        }
        HashMap hashMap = new HashMap();
        for (String str : set) {
            LocalBizKeyPairs k10 = w0.c.k(this.f14876c, str, String.valueOf(DateUtil.a()));
            if (k10 != null) {
                hashMap.put(str, k10);
            }
        }
        if (hashMap.isEmpty()) {
            return;
        }
        HashSet<String> hashSet = new HashSet();
        for (LocalBizKeyPairs localBizKeyPairs : hashMap.values()) {
            if (Thread.currentThread().isInterrupted()) {
                e1.i.a("CryptoCore", "registerLocalPublicKeys current thread is interrupted before register " + localBizKeyPairs.c() + " public key.");
                w0.c.e(this.f14876c, localBizKeyPairs.c(), localBizKeyPairs.f());
            } else if (I(localBizKeyPairs)) {
                hashSet.add(localBizKeyPairs.c());
            }
        }
        HashMap hashMap2 = new HashMap();
        if (!hashSet.isEmpty()) {
            for (String str2 : hashSet) {
                try {
                    String g6 = w0.c.g(this.f14883j.d(str2), this.f14878e.get(str2), this.f14877d, this.f14876c);
                    if (g6 != null) {
                        hashMap2.put(str2, g6);
                    }
                } catch (EncryptException | KeyStoreException | JSONException | InvalidArgumentException e10) {
                    e1.i.b("CryptoCore", "registerLocalPublicKeys a success register record encrypt failed, biz = " + str2 + ". " + e10);
                }
            }
        }
        long j10 = -1;
        if (!z10) {
            j10 = DateUtil.a();
            this.f14883j.i(j10);
        }
        w0.c.t(this.f14876c, hashMap2, j10);
    }

    private boolean I(LocalBizKeyPairs localBizKeyPairs) {
        boolean z10;
        String c10 = localBizKeyPairs.c();
        try {
            z10 = m(localBizKeyPairs).c();
        } catch (AuthException | JSONException | InvalidArgumentException e10) {
            e1.i.b("CryptoCore", "registerPublicKey upload " + c10 + " public key error. " + e10);
            z10 = false;
        }
        if (z10) {
            e1.i.a("CryptoCore", "registerPublicKey register " + c10 + " public key success.");
            LocalBizKeyPairs d10 = this.f14883j.d(c10);
            if (d10 != null) {
                w0.c.e(this.f14876c, c10, d10.f());
            }
            this.f14883j.j(c10, localBizKeyPairs);
        } else {
            e1.i.a("CryptoCore", "registerPublicKey register " + c10 + " public key fail.");
            w0.c.e(this.f14876c, localBizKeyPairs.c(), localBizKeyPairs.f());
        }
        return z10;
    }

    private Set<String> L(Set<String> set) {
        HashSet hashSet = new HashSet();
        Iterator<String> it = set.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String next = it.next();
            if (Thread.currentThread().isInterrupted()) {
                e1.i.a("CryptoCore", "upgradeAllCert current thread is interrupted before upgrade " + next + " certificate.");
                break;
            }
            try {
                String str = this.f14878e.get(next);
                if (str != null) {
                    CertUpgradeManager certUpgradeManager = new CertUpgradeManager(this.f14876c);
                    certUpgradeManager.f(str);
                    certUpgradeManager.e(next);
                    certUpgradeManager.g(this.f14883j.f(next).f());
                    UpgradeCertResponse upgradeCertResponse = new UpgradeCertResponse();
                    int h10 = certUpgradeManager.h(upgradeCertResponse);
                    if (Thread.currentThread().isInterrupted()) {
                        e1.i.a("CryptoCore", "upgradeAllCert current thread is interrupted while upgrading " + next + " certificate.");
                        break;
                    }
                    if (h10 != -1 && str.equals(this.f14878e.get(next))) {
                        if (h10 == 200) {
                            this.f14883j.f(next).b(upgradeCertResponse);
                        }
                        hashSet.add(next);
                    }
                }
            } catch (JSONException | BizDataNotFoundException | InvalidArgumentException e10) {
                e1.i.b("CryptoCore", "upgradeAllCert download " + next + " certificate error. " + e10);
            }
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M(Set<String> set) {
        if (L(set).isEmpty()) {
            return;
        }
        try {
            w0.c.s(this.f14876c, set, this.f14883j, DateUtil.a());
        } catch (KeyStoreException e10) {
            e1.i.d("CryptoCore", "upgradeAllCertAndSave failed to store the downloaded certificate record locally. " + e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: N, reason: merged with bridge method [inline-methods] */
    public void B(Set<String> set, int i10) {
        M(set);
        ThreadUtil.a().postDelayed(new a(set, i10), i10 * 1000);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0057 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private KeyRegisterManager m(LocalBizKeyPairs localBizKeyPairs) {
        UpgradeCertResponse f10;
        long f11;
        PublicKey b10;
        String c10 = localBizKeyPairs.c();
        KeyRegisterManager keyRegisterManager = new KeyRegisterManager();
        keyRegisterManager.f(this.f14877d);
        keyRegisterManager.d(c10);
        keyRegisterManager.g(this.f14878e.get(c10));
        keyRegisterManager.h(localBizKeyPairs.d().getPublic());
        keyRegisterManager.i(localBizKeyPairs.e().getPublic());
        PublicKey publicKey = null;
        if (!x(c10)) {
            try {
                f10 = this.f14883j.f(c10);
            } catch (BizDataNotFoundException unused) {
            }
            if (!f10.g()) {
                publicKey = f10.d().getPublicKey();
                f11 = f10.f();
                if (publicKey == null) {
                    try {
                        BizPublicKeyConfig b11 = this.f14883j.b(c10);
                        if (b11 != null && b11.a() != null) {
                            try {
                                b10 = KeyUtil.b(Base64Utils.a(b11.a()), "RSA");
                            } catch (InvalidKeySpecException unused2) {
                                b10 = KeyUtil.b(Base64Utils.a(b11.a()), "EC");
                            }
                            publicKey = b10;
                            f11 = b11.b();
                        }
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidArgumentException e10) {
                        e1.i.d("CryptoCore", "createKeyRegisterManager generatePublic fail. " + e10);
                    }
                }
                if (publicKey != null && f11 != 0) {
                    DeviceCertAuthConfig deviceCertAuthConfig = new DeviceCertAuthConfig();
                    deviceCertAuthConfig.e(publicKey);
                    deviceCertAuthConfig.f(f11);
                    keyRegisterManager.e(deviceCertAuthConfig);
                }
                return keyRegisterManager;
            }
        }
        f11 = 0;
        if (publicKey == null) {
        }
        if (publicKey != null) {
            DeviceCertAuthConfig deviceCertAuthConfig2 = new DeviceCertAuthConfig();
            deviceCertAuthConfig2.e(publicKey);
            deviceCertAuthConfig2.f(f11);
            keyRegisterManager.e(deviceCertAuthConfig2);
        }
        return keyRegisterManager;
    }

    private void u(Set<String> set, CertUpgradeCycleEnum certUpgradeCycleEnum) {
        final HashSet hashSet = new HashSet(this.f14878e.keySet());
        final int a10 = certUpgradeCycleEnum.a();
        int x10 = w0.c.x(this.f14883j.a(), a10);
        if (x10 < 0) {
            e1.i.a("CryptoCore", "initCertUpgrade upgrade the biz certificate immediately");
            B(hashSet, a10);
            return;
        }
        HashSet hashSet2 = new HashSet(hashSet);
        if (set != null) {
            hashSet2.removeAll(set);
        }
        Set<String> L = L(hashSet2);
        if (!L.isEmpty()) {
            try {
                w0.c.s(this.f14876c, L, this.f14883j, -1L);
            } catch (KeyStoreException e10) {
                e1.i.d("CryptoCore", "initCertUpgrade failed to store the downloaded certificate record locally. " + e10);
            }
        }
        e1.i.a("CryptoCore", "initCertUpgrade upgrade the biz certificate after " + x10 + " seconds");
        ThreadUtil.a().postDelayed(new Runnable() { // from class: m0.d
            @Override // java.lang.Runnable
            public final void run() {
                CryptoCore.this.C(hashSet, a10);
            }
        }, ((long) x10) * 1000);
    }

    private void v(final CertUpgradeCycleEnum certUpgradeCycleEnum) {
        this.f14886m = ThreadUtil.c(new Runnable() { // from class: m0.f
            @Override // java.lang.Runnable
            public final void run() {
                CryptoCore.this.D(certUpgradeCycleEnum);
            }
        });
    }

    private void w(Set<String> set) {
        Set<String> e10 = this.f14883j.e();
        if (w0.c.q(this.f14883j.c()) < 0) {
            if (set.isEmpty()) {
                return;
            }
            e1.i.a("CryptoCore", "initKeyRegister register all application public key immediately");
            H(set, false);
            return;
        }
        HashSet hashSet = new HashSet();
        if (set != null) {
            hashSet.addAll(set);
        }
        if (e10 != null) {
            hashSet.removeAll(e10);
        }
        if (hashSet.isEmpty()) {
            return;
        }
        e1.i.a("CryptoCore", "initKeyRegister register the new application public key immediately");
        H(hashSet, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void y(CertUpgradeManager certUpgradeManager, String str) {
        try {
            UpgradeCertResponse upgradeCertResponse = new UpgradeCertResponse();
            int h10 = certUpgradeManager.h(upgradeCertResponse);
            if (!Thread.currentThread().isInterrupted()) {
                if (h10 == 200) {
                    this.f14883j.f(str).b(upgradeCertResponse);
                }
            } else {
                e1.i.a("CryptoCore", "checkBizCert current thread is interrupted while upgrading " + str + " certificate.");
            }
        } catch (JSONException | BizDataNotFoundException | InvalidArgumentException e10) {
            e1.i.b("CryptoCore", "checkBizCert download " + str + " certificate error. " + e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void z(String str) {
        try {
            HashSet hashSet = new HashSet();
            hashSet.add(str);
            w0.c.s(this.f14876c, hashSet, this.f14883j, -1L);
        } catch (KeyStoreException e10) {
            e1.i.d("CryptoCore", "checkBizCert failed to store the downloaded " + str + " certificate record locally. " + e10);
        }
    }

    public boolean J(Session session) {
        if (session.j()) {
            return this.f14874a.a(session);
        }
        return false;
    }

    public void K(final String str, final n nVar) {
        synchronized (this.f14875b) {
            this.f14882i.b(str, nVar);
        }
        final SceneConfig sceneConfig = this.f14881h.get(nVar.e());
        if (sceneConfig == null || !sceneConfig.e()) {
            return;
        }
        ThreadUtil.c(new Runnable() { // from class: m0.c
            @Override // java.lang.Runnable
            public final void run() {
                CryptoCore.this.E(str, sceneConfig, nVar);
            }
        });
    }

    public void O() {
        if (this.f14886m == null) {
            return;
        }
        synchronized (this.f14885l) {
            Future future = this.f14886m;
            if (future != null && !future.isCancelled()) {
                try {
                    this.f14886m.get(3L, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException unused) {
                    this.f14886m.cancel(true);
                    this.f14886m = null;
                    e1.i.b("CryptoCore", "waitInitCryptoResource CryptoCore init interrupt.");
                }
            }
        }
    }

    public Session i(String str, Map<String, NegotiationParam> map) {
        Session b10 = this.f14874a.b();
        if (b10 == null) {
            b10 = new Session(this, this.f14884k);
        }
        b10.b(str, map);
        return b10;
    }

    public synchronized boolean j(final String str) {
        boolean z10 = true;
        if (this.f14883j.f(str).g()) {
            final CertUpgradeManager certUpgradeManager = new CertUpgradeManager(this.f14876c);
            certUpgradeManager.e(str);
            certUpgradeManager.f(o(str));
            Future<?> c10 = ThreadUtil.c(new Runnable() { // from class: m0.g
                @Override // java.lang.Runnable
                public final void run() {
                    CryptoCore.this.y(certUpgradeManager, str);
                }
            });
            try {
                c10.get(15L, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e10) {
                c10.cancel(true);
                e1.i.b("CryptoCore", "checkBizCert error. " + e10);
            }
            if (this.f14883j.f(str).g()) {
                z10 = false;
            }
            e1.i.a("CryptoCore", "checkBizCert download " + str + " certificate " + z10);
            if (z10) {
                ThreadUtil.c(new Runnable() { // from class: m0.b
                    @Override // java.lang.Runnable
                    public final void run() {
                        CryptoCore.this.z(str);
                    }
                });
            }
            return z10;
        }
        e1.i.a("CryptoCore", "checkBizCert already have " + str + " certificate.");
        return true;
    }

    public String k(String str) {
        if (str != null) {
            if (this.f14878e.containsKey(str) || x(str)) {
                return str;
            }
            throw new BizDataNotFoundException("please use the correct biz name but not " + str);
        }
        throw new BizDataNotFoundException("please specify the correct biz name but not null.");
    }

    public synchronized boolean l(final String str) {
        if (this.f14883j.d(str) == null) {
            if (this.f14879f.contains(str)) {
                Future<?> c10 = ThreadUtil.c(new Runnable() { // from class: m0.a
                    @Override // java.lang.Runnable
                    public final void run() {
                        CryptoCore.this.A(str);
                    }
                });
                try {
                    c10.get(15L, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e10) {
                    c10.cancel(true);
                    e1.i.b("CryptoCore", "checkLocalKey error. " + e10);
                }
                return this.f14883j.d(str) != null;
            }
            throw new InvalidKeyException("Application public key registration required is not set");
        }
        e1.i.a("CryptoCore", "checkLocalKey " + str + " application public key has been uploaded.");
        return true;
    }

    public long n(String str) {
        String k10 = k(str);
        O();
        try {
            if (j(k10)) {
                return this.f14883j.f(k10).h().f();
            }
        } catch (BizDataNotFoundException unused) {
            e1.i.a("CryptoCore", "getBizCertVersion No valid domain name set.");
        }
        BizPublicKeyConfig b10 = this.f14883j.b(k10);
        if (b10 != null) {
            return b10.b();
        }
        throw new BizDataNotFoundException("missing biz certificate.");
    }

    public String o(String str) {
        return this.f14878e.get(str);
    }

    public BizCertMemoryDataSource p() {
        return this.f14883j;
    }

    public Context q() {
        return this.f14876c;
    }

    public String r() {
        return this.f14877d;
    }

    public SceneConfig s(String str) {
        SceneConfig sceneConfig = this.f14881h.get(str);
        if (sceneConfig != null) {
            return sceneConfig;
        }
        throw new SceneNotFoundException(str);
    }

    public n t(String str, String str2) {
        n c10;
        synchronized (this.f14875b) {
            c10 = this.f14882i.c(str, str2);
        }
        return c10;
    }

    public boolean x(String str) {
        return this.f14880g.contains(str);
    }
}
