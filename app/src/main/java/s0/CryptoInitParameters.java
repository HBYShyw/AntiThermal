package s0;

import android.content.Context;
import java.util.List;
import java.util.Map;

/* compiled from: CryptoInitParameters.java */
/* renamed from: s0.f, reason: use source file name */
/* loaded from: classes.dex */
public class CryptoInitParameters {

    /* renamed from: a, reason: collision with root package name */
    private final Context f17950a;

    /* renamed from: b, reason: collision with root package name */
    private final String f17951b;

    /* renamed from: c, reason: collision with root package name */
    private final Map<String, String> f17952c;

    /* renamed from: d, reason: collision with root package name */
    private final Map<String, BizPublicKeyConfig> f17953d;

    /* renamed from: e, reason: collision with root package name */
    private final List<SceneConfig> f17954e;

    /* renamed from: f, reason: collision with root package name */
    private final CertUpgradeCycleEnum f17955f;

    /* renamed from: g, reason: collision with root package name */
    private final String[] f17956g;

    /* renamed from: h, reason: collision with root package name */
    private final String[] f17957h;

    /* compiled from: CryptoInitParameters.java */
    /* renamed from: s0.f$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private final Context f17958a;

        /* renamed from: b, reason: collision with root package name */
        private String f17959b = null;

        /* renamed from: c, reason: collision with root package name */
        private Map<String, String> f17960c = null;

        /* renamed from: d, reason: collision with root package name */
        private Map<String, BizPublicKeyConfig> f17961d = null;

        /* renamed from: e, reason: collision with root package name */
        private List<SceneConfig> f17962e = null;

        /* renamed from: f, reason: collision with root package name */
        private CertUpgradeCycleEnum f17963f = CertUpgradeCycleEnum.TWO_WEEKS;

        /* renamed from: g, reason: collision with root package name */
        private String[] f17964g = null;

        /* renamed from: h, reason: collision with root package name */
        private String[] f17965h = null;

        public b(Context context) {
            this.f17958a = context;
        }

        public CryptoInitParameters i() {
            return new CryptoInitParameters(this);
        }

        public b j(Map<String, String> map) {
            this.f17960c = map;
            return this;
        }

        public b k(List<SceneConfig> list) {
            this.f17962e = list;
            return this;
        }
    }

    public Map<String, String> a() {
        return this.f17952c;
    }

    public Map<String, BizPublicKeyConfig> b() {
        return this.f17953d;
    }

    public CertUpgradeCycleEnum c() {
        return this.f17955f;
    }

    public Context d() {
        return this.f17950a;
    }

    public String e() {
        return this.f17951b;
    }

    public String[] f() {
        return this.f17956g;
    }

    public List<SceneConfig> g() {
        return this.f17954e;
    }

    public String[] h() {
        return this.f17957h;
    }

    private CryptoInitParameters(b bVar) {
        this.f17950a = bVar.f17958a;
        this.f17951b = bVar.f17959b;
        this.f17952c = bVar.f17960c;
        this.f17954e = bVar.f17962e;
        this.f17955f = bVar.f17963f;
        this.f17956g = bVar.f17964g;
        this.f17957h = bVar.f17965h;
        this.f17953d = bVar.f17961d;
    }
}
