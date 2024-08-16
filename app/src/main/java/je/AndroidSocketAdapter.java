package je;

import ie.AndroidPlatform;
import ie.Platform;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.net.ssl.SSLSocket;
import je.DeferredSocketAdapter;
import kotlin.Metadata;
import sd.Charsets;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import zd.Protocol;

/* compiled from: AndroidSocketAdapter.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u0001:\u0001\u0006B\u0017\u0012\u000e\u0010\u0010\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00040\u000f¢\u0006\u0004\b\u0011\u0010\u0012J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J(\u0010\r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00042\b\u0010\b\u001a\u0004\u0018\u00010\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0016J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0005\u001a\u00020\u0004H\u0016¨\u0006\u0013"}, d2 = {"Lje/f;", "Lje/k;", "", "b", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "a", "", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "d", "c", "Ljava/lang/Class;", "sslSocketClass", "<init>", "(Ljava/lang/Class;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: je.f, reason: use source file name */
/* loaded from: classes2.dex */
public class AndroidSocketAdapter implements SocketAdapter {

    /* renamed from: f, reason: collision with root package name */
    public static final a f13914f;

    /* renamed from: g, reason: collision with root package name */
    private static final DeferredSocketAdapter.a f13915g;

    /* renamed from: a, reason: collision with root package name */
    private final Class<? super SSLSocket> f13916a;

    /* renamed from: b, reason: collision with root package name */
    private final Method f13917b;

    /* renamed from: c, reason: collision with root package name */
    private final Method f13918c;

    /* renamed from: d, reason: collision with root package name */
    private final Method f13919d;

    /* renamed from: e, reason: collision with root package name */
    private final Method f13920e;

    /* compiled from: AndroidSocketAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u0018\u0010\u0006\u001a\u00020\u00052\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00030\u0002H\u0002J\u000e\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007R\u0017\u0010\u000b\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000e¨\u0006\u0011"}, d2 = {"Lje/f$a;", "", "Ljava/lang/Class;", "Ljavax/net/ssl/SSLSocket;", "actualSSLSocketClass", "Lje/f;", "b", "", "packageName", "Lje/j$a;", "c", "playProviderFactory", "Lje/j$a;", "d", "()Lje/j$a;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: je.f$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: AndroidSocketAdapter.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\b"}, d2 = {"je/f$a$a", "Lje/j$a;", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "a", "Lje/k;", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: je.f$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0068a implements DeferredSocketAdapter.a {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ String f13921a;

            C0068a(String str) {
                this.f13921a = str;
            }

            @Override // je.DeferredSocketAdapter.a
            public boolean a(SSLSocket sslSocket) {
                boolean D;
                za.k.e(sslSocket, "sslSocket");
                String name = sslSocket.getClass().getName();
                za.k.d(name, "sslSocket.javaClass.name");
                D = StringsJVM.D(name, za.k.l(this.f13921a, "."), false, 2, null);
                return D;
            }

            @Override // je.DeferredSocketAdapter.a
            public SocketAdapter b(SSLSocket sslSocket) {
                za.k.e(sslSocket, "sslSocket");
                return AndroidSocketAdapter.f13914f.b(sslSocket.getClass());
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final AndroidSocketAdapter b(Class<? super SSLSocket> actualSSLSocketClass) {
            Class<? super SSLSocket> cls = actualSSLSocketClass;
            while (cls != null && !za.k.a(cls.getSimpleName(), "OpenSSLSocketImpl")) {
                cls = cls.getSuperclass();
                if (cls == null) {
                    throw new AssertionError(za.k.l("No OpenSSLSocketImpl superclass of socket of type ", actualSSLSocketClass));
                }
            }
            za.k.b(cls);
            return new AndroidSocketAdapter(cls);
        }

        public final DeferredSocketAdapter.a c(String packageName) {
            za.k.e(packageName, "packageName");
            return new C0068a(packageName);
        }

        public final DeferredSocketAdapter.a d() {
            return AndroidSocketAdapter.f13915g;
        }
    }

    static {
        a aVar = new a(null);
        f13914f = aVar;
        f13915g = aVar.c("com.google.android.gms.org.conscrypt");
    }

    public AndroidSocketAdapter(Class<? super SSLSocket> cls) {
        za.k.e(cls, "sslSocketClass");
        this.f13916a = cls;
        Method declaredMethod = cls.getDeclaredMethod("setUseSessionTickets", Boolean.TYPE);
        za.k.d(declaredMethod, "sslSocketClass.getDeclar…:class.javaPrimitiveType)");
        this.f13917b = declaredMethod;
        this.f13918c = cls.getMethod("setHostname", String.class);
        this.f13919d = cls.getMethod("getAlpnSelectedProtocol", new Class[0]);
        this.f13920e = cls.getMethod("setAlpnProtocols", byte[].class);
    }

    @Override // je.SocketAdapter
    public boolean a(SSLSocket sslSocket) {
        za.k.e(sslSocket, "sslSocket");
        return this.f13916a.isInstance(sslSocket);
    }

    @Override // je.SocketAdapter
    public boolean b() {
        return AndroidPlatform.f12843f.b();
    }

    @Override // je.SocketAdapter
    public String c(SSLSocket sslSocket) {
        za.k.e(sslSocket, "sslSocket");
        if (!a(sslSocket)) {
            return null;
        }
        try {
            byte[] bArr = (byte[]) this.f13919d.invoke(sslSocket, new Object[0]);
            if (bArr == null) {
                return null;
            }
            return new String(bArr, Charsets.f18469b);
        } catch (IllegalAccessException e10) {
            throw new AssertionError(e10);
        } catch (InvocationTargetException e11) {
            Throwable cause = e11.getCause();
            if ((cause instanceof NullPointerException) && za.k.a(((NullPointerException) cause).getMessage(), "ssl == null")) {
                return null;
            }
            throw new AssertionError(e11);
        }
    }

    @Override // je.SocketAdapter
    public void d(SSLSocket sSLSocket, String str, List<? extends Protocol> list) {
        za.k.e(sSLSocket, "sslSocket");
        za.k.e(list, "protocols");
        if (a(sSLSocket)) {
            try {
                this.f13917b.invoke(sSLSocket, Boolean.TRUE);
                if (str != null) {
                    this.f13918c.invoke(sSLSocket, str);
                }
                this.f13920e.invoke(sSLSocket, Platform.f12870a.c(list));
            } catch (IllegalAccessException e10) {
                throw new AssertionError(e10);
            } catch (InvocationTargetException e11) {
                throw new AssertionError(e11);
            }
        }
    }
}
