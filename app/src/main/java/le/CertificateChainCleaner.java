package le;

import ie.Platform;
import java.security.cert.Certificate;
import java.util.List;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: CertificateChainCleaner.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001:\u0001\u0007B\u0007¢\u0006\u0004\b\b\u0010\tJ$\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\u00022\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00022\u0006\u0010\u0006\u001a\u00020\u0005H&¨\u0006\n"}, d2 = {"Lle/c;", "", "", "Ljava/security/cert/Certificate;", "chain", "", "hostname", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: le.c, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class CertificateChainCleaner {

    /* renamed from: a, reason: collision with root package name */
    public static final a f14708a = new a(null);

    /* compiled from: CertificateChainCleaner.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002¨\u0006\b"}, d2 = {"Lle/c$a;", "", "Ljavax/net/ssl/X509TrustManager;", "trustManager", "Lle/c;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: le.c$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final CertificateChainCleaner a(X509TrustManager trustManager) {
            k.e(trustManager, "trustManager");
            return Platform.f12870a.g().c(trustManager);
        }
    }

    public abstract List<Certificate> a(List<? extends Certificate> chain, String hostname);
}
