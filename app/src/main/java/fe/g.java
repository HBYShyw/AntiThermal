package fe;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import ee.Exchange;
import java.util.List;
import kotlin.Metadata;
import zd.b0;
import zd.v;
import zd.z;

/* compiled from: RealInterceptorChain.kt */
@Metadata(bv = {}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001BO\u0012\u0006\u0010\u000f\u001a\u00020\u0012\u0012\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0!\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0002\u0012\u0006\u0010\t\u001a\u00020\u0002\u0012\u0006\u0010\n\u001a\u00020\u0002¢\u0006\u0004\b$\u0010%JM\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00022\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u000b\u0010\fJ\b\u0010\r\u001a\u00020\u0002H\u0016J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016J\u0010\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\u0006H\u0016R\u001a\u0010\u000f\u001a\u00020\u00128\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u000f\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0005\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0007\u001a\u00020\u00068\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0007\u0010\u0019\u001a\u0004\b\u001a\u0010\u001bR\u001a\u0010\b\u001a\u00020\u00028\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\b\u0010\u001c\u001a\u0004\b\u001d\u0010\u001eR\u001a\u0010\t\u001a\u00020\u00028\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\t\u0010\u001c\u001a\u0004\b\u001f\u0010\u001eR\u001a\u0010\n\u001a\u00020\u00028\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\n\u0010\u001c\u001a\u0004\b \u0010\u001e¨\u0006&"}, d2 = {"Lfe/g;", "Lzd/v$a;", "", ThermalBaseConfig.Item.ATTR_INDEX, "Lee/c;", "exchange", "Lzd/z;", "request", "connectTimeoutMillis", "readTimeoutMillis", "writeTimeoutMillis", "b", "(ILee/c;Lzd/z;III)Lfe/g;", "j", "Lzd/e;", "call", "Lzd/b0;", "a", "Lee/e;", "Lee/e;", "d", "()Lee/e;", "Lee/c;", "f", "()Lee/c;", "Lzd/z;", "h", "()Lzd/z;", "I", "e", "()I", "g", "i", "", "Lzd/v;", "interceptors", "<init>", "(Lee/e;Ljava/util/List;ILee/c;Lzd/z;III)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class g implements v.a {

    /* renamed from: a */
    private final ee.e f11461a;

    /* renamed from: b */
    private final List<v> f11462b;

    /* renamed from: c */
    private final int f11463c;

    /* renamed from: d */
    private final Exchange f11464d;

    /* renamed from: e */
    private final z f11465e;

    /* renamed from: f */
    private final int f11466f;

    /* renamed from: g */
    private final int f11467g;

    /* renamed from: h */
    private final int f11468h;

    /* renamed from: i */
    private int f11469i;

    /* JADX WARN: Multi-variable type inference failed */
    public g(ee.e eVar, List<? extends v> list, int i10, Exchange exchange, z zVar, int i11, int i12, int i13) {
        za.k.e(eVar, "call");
        za.k.e(list, "interceptors");
        za.k.e(zVar, "request");
        this.f11461a = eVar;
        this.f11462b = list;
        this.f11463c = i10;
        this.f11464d = exchange;
        this.f11465e = zVar;
        this.f11466f = i11;
        this.f11467g = i12;
        this.f11468h = i13;
    }

    public static /* synthetic */ g c(g gVar, int i10, Exchange exchange, z zVar, int i11, int i12, int i13, int i14, Object obj) {
        if ((i14 & 1) != 0) {
            i10 = gVar.f11463c;
        }
        if ((i14 & 2) != 0) {
            exchange = gVar.f11464d;
        }
        Exchange exchange2 = exchange;
        if ((i14 & 4) != 0) {
            zVar = gVar.f11465e;
        }
        z zVar2 = zVar;
        if ((i14 & 8) != 0) {
            i11 = gVar.f11466f;
        }
        int i15 = i11;
        if ((i14 & 16) != 0) {
            i12 = gVar.f11467g;
        }
        int i16 = i12;
        if ((i14 & 32) != 0) {
            i13 = gVar.f11468h;
        }
        return gVar.b(i10, exchange2, zVar2, i15, i16, i13);
    }

    @Override // zd.v.a
    public b0 a(z request) {
        za.k.e(request, "request");
        if (this.f11463c < this.f11462b.size()) {
            this.f11469i++;
            Exchange exchange = this.f11464d;
            if (exchange != null) {
                if (exchange.getF11161c().g(request.getF20796a())) {
                    if (!(this.f11469i == 1)) {
                        throw new IllegalStateException(("network interceptor " + this.f11462b.get(this.f11463c - 1) + " must call proceed() exactly once").toString());
                    }
                } else {
                    throw new IllegalStateException(("network interceptor " + this.f11462b.get(this.f11463c - 1) + " must retain the same host and port").toString());
                }
            }
            g c10 = c(this, this.f11463c + 1, null, request, 0, 0, 0, 58, null);
            v vVar = this.f11462b.get(this.f11463c);
            b0 a10 = vVar.a(c10);
            if (a10 != null) {
                if (this.f11464d != null) {
                    if (!(this.f11463c + 1 >= this.f11462b.size() || c10.f11469i == 1)) {
                        throw new IllegalStateException(("network interceptor " + vVar + " must call proceed() exactly once").toString());
                    }
                }
                if (a10.getF20511k() != null) {
                    return a10;
                }
                throw new IllegalStateException(("interceptor " + vVar + " returned a response with no body").toString());
            }
            throw new NullPointerException("interceptor " + vVar + " returned null");
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public final g b(int r12, Exchange exchange, z request, int connectTimeoutMillis, int readTimeoutMillis, int writeTimeoutMillis) {
        za.k.e(request, "request");
        return new g(this.f11461a, this.f11462b, r12, exchange, request, connectTimeoutMillis, readTimeoutMillis, writeTimeoutMillis);
    }

    @Override // zd.v.a
    public zd.e call() {
        return this.f11461a;
    }

    /* renamed from: d, reason: from getter */
    public final ee.e getF11461a() {
        return this.f11461a;
    }

    /* renamed from: e, reason: from getter */
    public final int getF11466f() {
        return this.f11466f;
    }

    /* renamed from: f, reason: from getter */
    public final Exchange getF11464d() {
        return this.f11464d;
    }

    /* renamed from: g, reason: from getter */
    public final int getF11467g() {
        return this.f11467g;
    }

    /* renamed from: h, reason: from getter */
    public final z getF11465e() {
        return this.f11465e;
    }

    /* renamed from: i, reason: from getter */
    public final int getF11468h() {
        return this.f11468h;
    }

    public int j() {
        return this.f11467g;
    }

    @Override // zd.v.a
    public z request() {
        return this.f11465e;
    }
}
