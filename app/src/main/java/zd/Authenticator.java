package zd;

import be.JavaNetAuthenticator;
import kotlin.Metadata;

/* compiled from: Authenticator.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bæ\u0080\u0001\u0018\u00002\u00020\u0001:\u0001\u0007J\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0005\u001a\u00020\u0004H&¨\u0006\b"}, d2 = {"Lzd/b;", "", "Lzd/d0;", "route", "Lzd/b0;", "response", "Lzd/z;", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.b, reason: use source file name */
/* loaded from: classes2.dex */
public interface Authenticator {

    /* renamed from: a, reason: collision with root package name */
    public static final a f20501a = a.f20504a;

    /* renamed from: b, reason: collision with root package name */
    public static final Authenticator f20502b = new a.C0123a();

    /* renamed from: c, reason: collision with root package name */
    public static final Authenticator f20503c = new JavaNetAuthenticator(null, 1, null);

    /* compiled from: Authenticator.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\bB\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007R\u0017\u0010\u0003\u001a\u00020\u00028\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0001R\u0017\u0010\u0005\u001a\u00020\u00028\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004¨\u0006\u0001¨\u0006\t"}, d2 = {"Lzd/b$a;", "", "Lzd/b;", "JAVA_NET_AUTHENTICATOR", "Lzd/b;", "NONE", "<init>", "()V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.b$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f20504a = new a();

        /* compiled from: Authenticator.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\b\u0010\tJ\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016¨\u0006\n"}, d2 = {"Lzd/b$a$a;", "Lzd/b;", "Lzd/d0;", "route", "Lzd/b0;", "response", "Lzd/z;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: zd.b$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        private static final class C0123a implements Authenticator {
            @Override // zd.Authenticator
            public z a(d0 route, b0 response) {
                za.k.e(response, "response");
                return null;
            }
        }

        private a() {
        }
    }

    z a(d0 route, b0 response);
}
