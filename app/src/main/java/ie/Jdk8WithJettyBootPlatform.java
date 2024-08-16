package ie;

import com.oplus.backup.sdk.common.utils.Constants;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.SSLSocket;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;
import zd.Protocol;

/* compiled from: Jdk8WithJettyBootPlatform.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0002\u0016\u000bB7\u0012\u0006\u0010\u000e\u001a\u00020\r\u0012\u0006\u0010\u000f\u001a\u00020\r\u0012\u0006\u0010\u0010\u001a\u00020\r\u0012\n\u0010\u0012\u001a\u0006\u0012\u0002\b\u00030\u0011\u0012\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0011¢\u0006\u0004\b\u0014\u0010\u0015J(\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016J\u0010\u0010\u000b\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0012\u0010\f\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0017"}, d2 = {"Lie/e;", "Lie/h;", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "hostname", "", "Lzd/y;", "protocols", "Lma/f0;", "e", "b", "g", "Ljava/lang/reflect/Method;", "putMethod", "getMethod", "removeMethod", "Ljava/lang/Class;", "clientProviderClass", "serverProviderClass", "<init>", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/Class;)V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ie.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class Jdk8WithJettyBootPlatform extends Platform {

    /* renamed from: i, reason: collision with root package name */
    public static final b f12856i = new b(null);

    /* renamed from: d, reason: collision with root package name */
    private final Method f12857d;

    /* renamed from: e, reason: collision with root package name */
    private final Method f12858e;

    /* renamed from: f, reason: collision with root package name */
    private final Method f12859f;

    /* renamed from: g, reason: collision with root package name */
    private final Class<?> f12860g;

    /* renamed from: h, reason: collision with root package name */
    private final Class<?> f12861h;

    /* compiled from: Jdk8WithJettyBootPlatform.kt */
    @Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00110\u0018¢\u0006\u0004\b\u001a\u0010\u001bJ2\u0010\b\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u0006H\u0096\u0002¢\u0006\u0004\b\b\u0010\tR\"\u0010\u000b\u001a\u00020\n8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R$\u0010\u0012\u001a\u0004\u0018\u00010\u00118\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017¨\u0006\u001c"}, d2 = {"Lie/e$a;", "Ljava/lang/reflect/InvocationHandler;", "", "proxy", "Ljava/lang/reflect/Method;", Constants.MessagerConstants.METHOD_KEY, "", Constants.MessagerConstants.ARGS_KEY, "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", "", "unsupported", "Z", "b", "()Z", "setUnsupported", "(Z)V", "", "selected", "Ljava/lang/String;", "a", "()Ljava/lang/String;", "setSelected", "(Ljava/lang/String;)V", "", "protocols", "<init>", "(Ljava/util/List;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ie.e$a */
    /* loaded from: classes2.dex */
    private static final class a implements InvocationHandler {

        /* renamed from: a, reason: collision with root package name */
        private final List<String> f12862a;

        /* renamed from: b, reason: collision with root package name */
        private boolean f12863b;

        /* renamed from: c, reason: collision with root package name */
        private String f12864c;

        public a(List<String> list) {
            k.e(list, "protocols");
            this.f12862a = list;
        }

        /* renamed from: a, reason: from getter */
        public final String getF12864c() {
            return this.f12864c;
        }

        /* renamed from: b, reason: from getter */
        public final boolean getF12863b() {
            return this.f12863b;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) {
            k.e(proxy, "proxy");
            k.e(method, Constants.MessagerConstants.METHOD_KEY);
            if (args == null) {
                args = new Object[0];
            }
            String name = method.getName();
            Class<?> returnType = method.getReturnType();
            if (k.a(name, "supports") && k.a(Boolean.TYPE, returnType)) {
                return Boolean.TRUE;
            }
            if (k.a(name, "unsupported") && k.a(Void.TYPE, returnType)) {
                this.f12863b = true;
                return null;
            }
            if (k.a(name, "protocols")) {
                if (args.length == 0) {
                    return this.f12862a;
                }
            }
            if ((k.a(name, "selectProtocol") || k.a(name, "select")) && k.a(String.class, returnType) && args.length == 1 && (args[0] instanceof List)) {
                Object obj = args[0];
                Objects.requireNonNull(obj, "null cannot be cast to non-null type kotlin.collections.List<*>");
                List list = (List) obj;
                int size = list.size();
                if (size >= 0) {
                    int i10 = 0;
                    while (true) {
                        int i11 = i10 + 1;
                        Object obj2 = list.get(i10);
                        Objects.requireNonNull(obj2, "null cannot be cast to non-null type kotlin.String");
                        String str = (String) obj2;
                        if (this.f12862a.contains(str)) {
                            this.f12864c = str;
                            return str;
                        }
                        if (i10 == size) {
                            break;
                        }
                        i10 = i11;
                    }
                }
                String str2 = this.f12862a.get(0);
                this.f12864c = str2;
                return str2;
            }
            if ((k.a(name, "protocolSelected") || k.a(name, "selected")) && args.length == 1) {
                Object obj3 = args[0];
                Objects.requireNonNull(obj3, "null cannot be cast to non-null type kotlin.String");
                this.f12864c = (String) obj3;
                return null;
            }
            return method.invoke(this, Arrays.copyOf(args, args.length));
        }
    }

    /* compiled from: Jdk8WithJettyBootPlatform.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002¨\u0006\u0006"}, d2 = {"Lie/e$b;", "", "Lie/h;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ie.e$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Platform a() {
            String property = System.getProperty("java.specification.version", "unknown");
            try {
                k.d(property, "jvmVersion");
                if (Integer.parseInt(property) >= 9) {
                    return null;
                }
            } catch (NumberFormatException unused) {
            }
            try {
                Class<?> cls = Class.forName("org.eclipse.jetty.alpn.ALPN", true, null);
                Class<?> cls2 = Class.forName(k.l("org.eclipse.jetty.alpn.ALPN", "$Provider"), true, null);
                Class<?> cls3 = Class.forName(k.l("org.eclipse.jetty.alpn.ALPN", "$ClientProvider"), true, null);
                Class<?> cls4 = Class.forName(k.l("org.eclipse.jetty.alpn.ALPN", "$ServerProvider"), true, null);
                Method method = cls.getMethod("put", SSLSocket.class, cls2);
                Method method2 = cls.getMethod("get", SSLSocket.class);
                Method method3 = cls.getMethod(EventType.STATE_PACKAGE_CHANGED_REMOVE, SSLSocket.class);
                k.d(method, "putMethod");
                k.d(method2, "getMethod");
                k.d(method3, "removeMethod");
                k.d(cls3, "clientProviderClass");
                k.d(cls4, "serverProviderClass");
                return new Jdk8WithJettyBootPlatform(method, method2, method3, cls3, cls4);
            } catch (ClassNotFoundException | NoSuchMethodException unused2) {
                return null;
            }
        }
    }

    public Jdk8WithJettyBootPlatform(Method method, Method method2, Method method3, Class<?> cls, Class<?> cls2) {
        k.e(method, "putMethod");
        k.e(method2, "getMethod");
        k.e(method3, "removeMethod");
        k.e(cls, "clientProviderClass");
        k.e(cls2, "serverProviderClass");
        this.f12857d = method;
        this.f12858e = method2;
        this.f12859f = method3;
        this.f12860g = cls;
        this.f12861h = cls2;
    }

    @Override // ie.Platform
    public void b(SSLSocket sSLSocket) {
        k.e(sSLSocket, "sslSocket");
        try {
            this.f12859f.invoke(null, sSLSocket);
        } catch (IllegalAccessException e10) {
            throw new AssertionError("failed to remove ALPN", e10);
        } catch (InvocationTargetException e11) {
            throw new AssertionError("failed to remove ALPN", e11);
        }
    }

    @Override // ie.Platform
    public void e(SSLSocket sSLSocket, String str, List<? extends Protocol> list) {
        k.e(sSLSocket, "sslSocket");
        k.e(list, "protocols");
        try {
            this.f12857d.invoke(null, sSLSocket, Proxy.newProxyInstance(Platform.class.getClassLoader(), new Class[]{this.f12860g, this.f12861h}, new a(Platform.f12870a.b(list))));
        } catch (IllegalAccessException e10) {
            throw new AssertionError("failed to set ALPN", e10);
        } catch (InvocationTargetException e11) {
            throw new AssertionError("failed to set ALPN", e11);
        }
    }

    @Override // ie.Platform
    public String g(SSLSocket sslSocket) {
        k.e(sslSocket, "sslSocket");
        try {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(this.f12858e.invoke(null, sslSocket));
            if (invocationHandler != null) {
                a aVar = (a) invocationHandler;
                if (!aVar.getF12863b() && aVar.getF12864c() == null) {
                    Platform.k(this, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", 0, null, 6, null);
                    return null;
                }
                if (aVar.getF12863b()) {
                    return null;
                }
                return aVar.getF12864c();
            }
            throw new NullPointerException("null cannot be cast to non-null type okhttp3.internal.platform.Jdk8WithJettyBootPlatform.AlpnProvider");
        } catch (IllegalAccessException e10) {
            throw new AssertionError("failed to get ALPN selected protocol", e10);
        } catch (InvocationTargetException e11) {
            throw new AssertionError("failed to get ALPN selected protocol", e11);
        }
    }
}
