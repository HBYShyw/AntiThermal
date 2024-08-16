package i0;

import android.os.Handler;
import android.os.Looper;
import e1.CryptoConfigUtil;
import e1.i;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import m0.CryptoCore;
import n0.SessionSceneDataRepository;
import org.json.JSONException;
import p0.EccDigitalEnvelopeFunction;
import p0.RsaDigitalEnvelopeFunction;
import s0.CryptoConfig;
import s0.NegotiationAlgorithmEnum;
import s0.NegotiationParam;
import s0.SceneConfig;
import s0.j;
import t0.BizDataNotFoundException;
import t0.InvalidAlgorithmException;
import t0.SceneNotFoundException;
import t0.SessionExpiredException;
import y0.NoiseNegotiationFunction;

/* compiled from: Session.java */
/* renamed from: i0.e, reason: use source file name */
/* loaded from: classes.dex */
public class Session {

    /* renamed from: a, reason: collision with root package name */
    private final CryptoCore f12475a;

    /* renamed from: c, reason: collision with root package name */
    private String f12477c;

    /* renamed from: f, reason: collision with root package name */
    private Handler f12480f;

    /* renamed from: g, reason: collision with root package name */
    private Runnable f12481g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f12482h;

    /* renamed from: j, reason: collision with root package name */
    private int f12484j;

    /* renamed from: k, reason: collision with root package name */
    private final Object f12485k = new Object();

    /* renamed from: d, reason: collision with root package name */
    private final SessionSceneDataRepository f12478d = new SessionSceneDataRepository();

    /* renamed from: e, reason: collision with root package name */
    private final Map<String, CryptoConfig> f12479e = new HashMap();

    /* renamed from: i, reason: collision with root package name */
    private final Map<String, NegotiationParam> f12483i = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    private final Map<String, j> f12476b = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Session.java */
    /* renamed from: i0.e$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f12486a;

        static {
            int[] iArr = new int[NegotiationAlgorithmEnum.values().length];
            f12486a = iArr;
            try {
                iArr[NegotiationAlgorithmEnum.RSA.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f12486a[NegotiationAlgorithmEnum.EC.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f12486a[NegotiationAlgorithmEnum.NOISE_NN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f12486a[NegotiationAlgorithmEnum.NOISE_NK.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f12486a[NegotiationAlgorithmEnum.NOISE_KK.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f12486a[NegotiationAlgorithmEnum.NOISE_IK.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f12486a[NegotiationAlgorithmEnum.NOISE_IX.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public Session(CryptoCore cryptoCore, int i10) {
        this.f12484j = 60;
        this.f12475a = cryptoCore;
        this.f12484j = i10;
    }

    private void c() {
        this.f12478d.b();
        this.f12479e.clear();
        this.f12483i.clear();
        synchronized (this.f12485k) {
            this.f12476b.clear();
        }
    }

    private j e(String str) {
        NegotiationAlgorithmEnum c10 = h(str).c();
        synchronized (this.f12485k) {
            switch (a.f12486a[c10.ordinal()]) {
                case 1:
                    if (!this.f12476b.containsKey("rsa")) {
                        this.f12476b.put("rsa", new RsaDigitalEnvelopeFunction(this.f12475a, this.f12478d, this.f12479e));
                    }
                    return this.f12476b.get("rsa");
                case 2:
                    if (!this.f12476b.containsKey("ecc")) {
                        this.f12476b.put("ecc", new EccDigitalEnvelopeFunction(this.f12475a, this.f12483i, this.f12478d, this.f12479e));
                    }
                    return this.f12476b.get("ecc");
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    if (!this.f12476b.containsKey("noise")) {
                        this.f12476b.put("noise", new NoiseNegotiationFunction(this.f12475a, this.f12483i, this.f12478d, this.f12479e));
                    }
                    return this.f12476b.get("noise");
                default:
                    throw new InvalidAlgorithmException(c10.name());
            }
        }
    }

    private String f(Collection<String> collection) {
        try {
            if (!this.f12482h) {
                HashMap hashMap = new HashMap();
                for (String str : collection) {
                    CryptoConfig cryptoConfig = this.f12479e.get(str);
                    if (cryptoConfig != null) {
                        hashMap.put(str, cryptoConfig);
                    } else {
                        throw new NullPointerException("scene(" + str + ") not found in cryptoConfigs.");
                    }
                }
                return CryptoConfigUtil.a(hashMap);
            }
            throw new SessionExpiredException();
        } catch (NullPointerException | JSONException | SessionExpiredException e10) {
            i.b("Session", "getHeader error. " + e10);
            throw new EncryptException(e10);
        }
    }

    private SceneConfig h(String str) {
        return this.f12475a.s(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void i() {
        this.f12482h = true;
        c();
    }

    public void b(String str, Map<String, NegotiationParam> map) {
        Runnable runnable;
        this.f12482h = false;
        Handler handler = this.f12480f;
        if (handler != null && (runnable = this.f12481g) != null) {
            handler.removeCallbacks(runnable);
        }
        if (this.f12480f == null) {
            this.f12480f = new Handler(Looper.getMainLooper());
        }
        if (this.f12481g == null) {
            this.f12481g = new Runnable() { // from class: i0.d
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.i();
                }
            };
        }
        this.f12480f.postDelayed(this.f12481g, this.f12484j * 1000);
        if (map != null) {
            this.f12483i.putAll(map);
        }
        this.f12477c = str;
    }

    public String d(String str, String str2) {
        try {
            if (!this.f12482h) {
                this.f12475a.O();
                return e(str2).a(str.getBytes(StandardCharsets.UTF_8), this.f12475a.k(this.f12477c), str2);
            }
            throw new SessionExpiredException();
        } catch (BizDataNotFoundException | InvalidAlgorithmException | SceneNotFoundException | SessionExpiredException e10) {
            i.b("Session", "encrypt error. " + e10);
            throw new EncryptException(e10);
        }
    }

    public String g(String... strArr) {
        return f(Arrays.asList(strArr));
    }

    public boolean j() {
        if (this.f12482h) {
            return false;
        }
        this.f12480f.removeCallbacks(this.f12481g);
        c();
        return true;
    }
}
