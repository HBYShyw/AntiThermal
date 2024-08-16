package ce;

import ae.d;
import ce.CacheStrategy;
import kotlin.Metadata;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import za.k;
import zd.EventListener;
import zd.Headers;
import zd.Protocol;
import zd.ResponseBody;
import zd.b0;
import zd.c;
import zd.e;
import zd.v;
import zd.z;

/* compiled from: CacheInterceptor.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0005B\u0011\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006¢\u0006\u0004\b\b\u0010\tJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\n"}, d2 = {"Lce/a;", "Lzd/v;", "Lzd/v$a;", "chain", "Lzd/b0;", "a", "Lzd/c;", "cache", "<init>", "(Lzd/c;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ce.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class CacheInterceptor implements v {

    /* renamed from: a, reason: collision with root package name */
    public static final a f5334a = new a(null);

    /* compiled from: CacheInterceptor.kt */
    @Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0002J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0002J\u0010\u0010\f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\tH\u0002J\u0010\u0010\r\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\tH\u0002¨\u0006\u0010"}, d2 = {"Lce/a$a;", "", "Lzd/b0;", "response", "f", "Lzd/t;", "cachedHeaders", "networkHeaders", "c", "", "fieldName", "", "e", "d", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ce.a$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Headers c(Headers cachedHeaders, Headers networkHeaders) {
            boolean r10;
            boolean D;
            Headers.a aVar = new Headers.a();
            int size = cachedHeaders.size();
            int i10 = 0;
            int i11 = 0;
            while (i11 < size) {
                int i12 = i11 + 1;
                String e10 = cachedHeaders.e(i11);
                String g6 = cachedHeaders.g(i11);
                r10 = StringsJVM.r("Warning", e10, true);
                if (r10) {
                    D = StringsJVM.D(g6, "1", false, 2, null);
                    if (D) {
                        i11 = i12;
                    }
                }
                if (d(e10) || !e(e10) || networkHeaders.d(e10) == null) {
                    aVar.c(e10, g6);
                }
                i11 = i12;
            }
            int size2 = networkHeaders.size();
            while (i10 < size2) {
                int i13 = i10 + 1;
                String e11 = networkHeaders.e(i10);
                if (!d(e11) && e(e11)) {
                    aVar.c(e11, networkHeaders.g(i10));
                }
                i10 = i13;
            }
            return aVar.d();
        }

        private final boolean d(String fieldName) {
            boolean r10;
            boolean r11;
            boolean r12;
            r10 = StringsJVM.r("Content-Length", fieldName, true);
            if (r10) {
                return true;
            }
            r11 = StringsJVM.r("Content-Encoding", fieldName, true);
            if (r11) {
                return true;
            }
            r12 = StringsJVM.r("Content-Type", fieldName, true);
            return r12;
        }

        private final boolean e(String fieldName) {
            boolean r10;
            boolean r11;
            boolean r12;
            boolean r13;
            boolean r14;
            boolean r15;
            boolean r16;
            boolean r17;
            r10 = StringsJVM.r("Connection", fieldName, true);
            if (!r10) {
                r11 = StringsJVM.r("Keep-Alive", fieldName, true);
                if (!r11) {
                    r12 = StringsJVM.r("Proxy-Authenticate", fieldName, true);
                    if (!r12) {
                        r13 = StringsJVM.r("Proxy-Authorization", fieldName, true);
                        if (!r13) {
                            r14 = StringsJVM.r("TE", fieldName, true);
                            if (!r14) {
                                r15 = StringsJVM.r("Trailers", fieldName, true);
                                if (!r15) {
                                    r16 = StringsJVM.r("Transfer-Encoding", fieldName, true);
                                    if (!r16) {
                                        r17 = StringsJVM.r("Upgrade", fieldName, true);
                                        if (!r17) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final b0 f(b0 response) {
            return (response == null ? null : response.getF20511k()) != null ? response.e0().b(null).c() : response;
        }
    }

    public CacheInterceptor(c cVar) {
    }

    @Override // zd.v
    public b0 a(v.a chain) {
        k.e(chain, "chain");
        e call = chain.call();
        CacheStrategy b10 = new CacheStrategy.b(System.currentTimeMillis(), chain.request(), null).b();
        z f5336a = b10.getF5336a();
        b0 f5337b = b10.getF5337b();
        ee.e eVar = call instanceof ee.e ? (ee.e) call : null;
        EventListener f11190i = eVar == null ? null : eVar.getF11190i();
        if (f11190i == null) {
            f11190i = EventListener.f20700b;
        }
        if (f5336a == null && f5337b == null) {
            b0 c10 = new b0.a().s(chain.request()).q(Protocol.HTTP_1_1).g(504).n("Unsatisfiable Request (only-if-cached)").b(d.f239c).t(-1L).r(System.currentTimeMillis()).c();
            f11190i.z(call, c10);
            return c10;
        }
        if (f5336a == null) {
            k.b(f5337b);
            b0 c11 = f5337b.e0().d(f5334a.f(f5337b)).c();
            f11190i.b(call, c11);
            return c11;
        }
        if (f5337b != null) {
            f11190i.a(call, f5337b);
        }
        b0 a10 = chain.a(f5336a);
        if (f5337b != null) {
            boolean z10 = false;
            if (a10 != null && a10.getCode() == 304) {
                z10 = true;
            }
            if (!z10) {
                ResponseBody f20511k = f5337b.getF20511k();
                if (f20511k != null) {
                    d.l(f20511k);
                }
            } else {
                b0.a e02 = f5337b.e0();
                a aVar = f5334a;
                e02.l(aVar.c(f5337b.getF20510j(), a10.getF20510j())).t(a10.getF20515o()).r(a10.getF20516p()).d(aVar.f(f5337b)).o(aVar.f(a10)).c();
                ResponseBody f20511k2 = a10.getF20511k();
                k.b(f20511k2);
                f20511k2.close();
                k.b(null);
                throw null;
            }
        }
        k.b(a10);
        b0.a e03 = a10.e0();
        a aVar2 = f5334a;
        return e03.d(aVar2.f(f5337b)).o(aVar2.f(a10)).c();
    }
}
