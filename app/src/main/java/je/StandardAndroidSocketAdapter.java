package je;

import ie.Platform;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import za.DefaultConstructorMarker;

/* compiled from: StandardAndroidSocketAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0001\nB3\u0012\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00030\u0002\u0012\u000e\u0010\u0006\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00050\u0002\u0012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0002¢\u0006\u0004\b\b\u0010\t¨\u0006\u000b"}, d2 = {"Lje/l;", "Lje/f;", "Ljava/lang/Class;", "Ljavax/net/ssl/SSLSocket;", "sslSocketClass", "Ljavax/net/ssl/SSLSocketFactory;", "sslSocketFactoryClass", "paramClass", "<init>", "(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/Class;)V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: je.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class StandardAndroidSocketAdapter extends AndroidSocketAdapter {

    /* renamed from: j, reason: collision with root package name */
    public static final a f13932j = new a(null);

    /* renamed from: h, reason: collision with root package name */
    private final Class<? super SSLSocketFactory> f13933h;

    /* renamed from: i, reason: collision with root package name */
    private final Class<?> f13934i;

    /* compiled from: StandardAndroidSocketAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0003\u001a\u00020\u0002¨\u0006\b"}, d2 = {"Lje/l$a;", "", "", "packageName", "Lje/k;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: je.l$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ SocketAdapter b(a aVar, String str, int i10, Object obj) {
            if ((i10 & 1) != 0) {
                str = "com.android.org.conscrypt";
            }
            return aVar.a(str);
        }

        public final SocketAdapter a(String packageName) {
            za.k.e(packageName, "packageName");
            try {
                Class<?> cls = Class.forName(za.k.l(packageName, ".OpenSSLSocketImpl"));
                Class<?> cls2 = Class.forName(za.k.l(packageName, ".OpenSSLSocketFactoryImpl"));
                Class<?> cls3 = Class.forName(za.k.l(packageName, ".SSLParametersImpl"));
                za.k.d(cls3, "paramsClass");
                return new StandardAndroidSocketAdapter(cls, cls2, cls3);
            } catch (Exception e10) {
                Platform.f12870a.g().j("unable to load android socket classes", 5, e10);
                return null;
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StandardAndroidSocketAdapter(Class<? super SSLSocket> cls, Class<? super SSLSocketFactory> cls2, Class<?> cls3) {
        super(cls);
        za.k.e(cls, "sslSocketClass");
        za.k.e(cls2, "sslSocketFactoryClass");
        za.k.e(cls3, "paramClass");
        this.f13933h = cls2;
        this.f13934i = cls3;
    }
}
