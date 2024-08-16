package zd;

import java.io.IOException;
import kotlin.Metadata;
import za.DefaultConstructorMarker;

/* compiled from: Protocol.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0010\u000e\n\u0002\b\f\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0007B\u0011\b\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002¢\u0006\u0004\b\u0005\u0010\u0006J\b\u0010\u0003\u001a\u00020\u0002H\u0016j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r¨\u0006\u000e"}, d2 = {"Lzd/y;", "", "", "toString", "protocol", "<init>", "(Ljava/lang/String;ILjava/lang/String;)V", "a", "HTTP_1_0", "HTTP_1_1", "SPDY_3", "HTTP_2", "H2_PRIOR_KNOWLEDGE", "QUIC", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.y, reason: use source file name */
/* loaded from: classes2.dex */
public enum Protocol {
    HTTP_1_0("http/1.0"),
    HTTP_1_1("http/1.1"),
    SPDY_3("spdy/3.1"),
    HTTP_2("h2"),
    H2_PRIOR_KNOWLEDGE("h2_prior_knowledge"),
    QUIC("quic");


    /* renamed from: f, reason: collision with root package name */
    public static final a f20787f = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private final String f20795e;

    /* compiled from: Protocol.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0007¨\u0006\b"}, d2 = {"Lzd/y$a;", "", "", "protocol", "Lzd/y;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.y$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Protocol a(String protocol) {
            za.k.e(protocol, "protocol");
            Protocol protocol2 = Protocol.HTTP_1_0;
            if (!za.k.a(protocol, protocol2.f20795e)) {
                protocol2 = Protocol.HTTP_1_1;
                if (!za.k.a(protocol, protocol2.f20795e)) {
                    protocol2 = Protocol.H2_PRIOR_KNOWLEDGE;
                    if (!za.k.a(protocol, protocol2.f20795e)) {
                        protocol2 = Protocol.HTTP_2;
                        if (!za.k.a(protocol, protocol2.f20795e)) {
                            protocol2 = Protocol.SPDY_3;
                            if (!za.k.a(protocol, protocol2.f20795e)) {
                                protocol2 = Protocol.QUIC;
                                if (!za.k.a(protocol, protocol2.f20795e)) {
                                    throw new IOException(za.k.l("Unexpected protocol: ", protocol));
                                }
                            }
                        }
                    }
                }
            }
            return protocol2;
        }
    }

    Protocol(String str) {
        this.f20795e = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.f20795e;
    }
}
