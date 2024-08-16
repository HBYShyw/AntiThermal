package zd;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections._Arrays;

/* compiled from: Dns.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0006J\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0007"}, d2 = {"Lzd/q;", "", "", "hostname", "", "Ljava/net/InetAddress;", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.q, reason: use source file name */
/* loaded from: classes2.dex */
public interface Dns {

    /* renamed from: a, reason: collision with root package name */
    public static final a f20696a = a.f20698a;

    /* renamed from: b, reason: collision with root package name */
    public static final Dns f20697b = new a.C0126a();

    /* compiled from: Dns.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u0007B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0017\u0010\u0003\u001a\u00020\u00028\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0001¨\u0006\b"}, d2 = {"Lzd/q$a;", "", "Lzd/q;", "SYSTEM", "Lzd/q;", "<init>", "()V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.q$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f20698a = new a();

        /* compiled from: Dns.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0007\u0010\bJ\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"Lzd/q$a$a;", "Lzd/q;", "", "hostname", "", "Ljava/net/InetAddress;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: zd.q$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        private static final class C0126a implements Dns {
            @Override // zd.Dns
            public List<InetAddress> a(String hostname) {
                List<InetAddress> f02;
                za.k.e(hostname, "hostname");
                try {
                    InetAddress[] allByName = InetAddress.getAllByName(hostname);
                    za.k.d(allByName, "getAllByName(hostname)");
                    f02 = _Arrays.f0(allByName);
                    return f02;
                } catch (NullPointerException e10) {
                    UnknownHostException unknownHostException = new UnknownHostException(za.k.l("Broken system behaviour for dns lookup of ", hostname));
                    unknownHostException.initCause(e10);
                    throw unknownHostException;
                }
            }
        }

        private a() {
        }
    }

    List<InetAddress> a(String hostname);
}
