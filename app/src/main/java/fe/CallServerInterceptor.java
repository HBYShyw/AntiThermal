package fe;

import ee.Exchange;
import java.net.ProtocolException;
import kotlin.Metadata;
import me.BufferedSink;
import me.n;
import sd.StringsJVM;
import zd.RequestBody;
import zd.ResponseBody;
import zd.b0;
import zd.v;
import zd.z;

/* compiled from: CallServerInterceptor.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\b\u0010\tJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\n"}, d2 = {"Lfe/b;", "Lzd/v;", "Lzd/v$a;", "chain", "Lzd/b0;", "a", "", "forWebSocket", "<init>", "(Z)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: fe.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class CallServerInterceptor implements v {

    /* renamed from: a, reason: collision with root package name */
    private final boolean f11454a;

    public CallServerInterceptor(boolean z10) {
        this.f11454a = z10;
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x013b, code lost:
    
        if (r12 != false) goto L42;
     */
    @Override // zd.v
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public b0 a(v.a chain) {
        b0.a aVar;
        boolean z10;
        b0 c10;
        boolean r10;
        boolean r11;
        boolean r12;
        za.k.e(chain, "chain");
        g gVar = (g) chain;
        Exchange f11464d = gVar.getF11464d();
        za.k.b(f11464d);
        z f11465e = gVar.getF11465e();
        RequestBody f20799d = f11465e.getF20799d();
        long currentTimeMillis = System.currentTimeMillis();
        f11464d.t(f11465e);
        if (HttpMethod.a(f11465e.getF20797b()) && f20799d != null) {
            r12 = StringsJVM.r("100-continue", f11465e.d("Expect"), true);
            if (r12) {
                f11464d.f();
                aVar = f11464d.p(true);
                f11464d.r();
                z10 = false;
            } else {
                aVar = null;
                z10 = true;
            }
            if (aVar == null) {
                if (f20799d.d()) {
                    f11464d.f();
                    f20799d.f(n.a(f11464d.c(f11465e, true)));
                } else {
                    BufferedSink a10 = n.a(f11464d.c(f11465e, false));
                    f20799d.f(a10);
                    a10.close();
                }
            } else {
                f11464d.n();
                if (!f11464d.getF11164f().v()) {
                    f11464d.m();
                }
            }
        } else {
            f11464d.n();
            aVar = null;
            z10 = true;
        }
        if (f20799d == null || !f20799d.d()) {
            f11464d.e();
        }
        if (aVar == null) {
            aVar = f11464d.p(false);
            za.k.b(aVar);
            if (z10) {
                f11464d.r();
                z10 = false;
            }
        }
        b0 c11 = aVar.s(f11465e).j(f11464d.getF11164f().getF11214g()).t(currentTimeMillis).r(System.currentTimeMillis()).c();
        int code = c11.getCode();
        if (code == 100) {
            b0.a p10 = f11464d.p(false);
            za.k.b(p10);
            if (z10) {
                f11464d.r();
            }
            c11 = p10.s(f11465e).j(f11464d.getF11164f().getF11214g()).t(currentTimeMillis).r(System.currentTimeMillis()).c();
            code = c11.getCode();
        }
        f11464d.q(c11);
        if (this.f11454a && code == 101) {
            c10 = c11.e0().b(ae.d.f239c).c();
        } else {
            c10 = c11.e0().b(f11464d.o(c11)).c();
        }
        r10 = StringsJVM.r("close", c10.getF20505e().d("Connection"), true);
        if (!r10) {
            r11 = StringsJVM.r("close", b0.S(c10, "Connection", null, 2, null), true);
        }
        f11464d.m();
        if (code == 204 || code == 205) {
            ResponseBody f20511k = c10.getF20511k();
            if ((f20511k == null ? -1L : f20511k.getF11471g()) > 0) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("HTTP ");
                sb2.append(code);
                sb2.append(" had non-zero Content-Length: ");
                ResponseBody f20511k2 = c10.getF20511k();
                sb2.append(f20511k2 != null ? Long.valueOf(f20511k2.getF11471g()) : null);
                throw new ProtocolException(sb2.toString());
            }
        }
        return c10;
    }
}
