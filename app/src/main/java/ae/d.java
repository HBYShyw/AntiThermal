package ae;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import com.oplus.backup.sdk.common.utils.Constants;
import com.oplus.thermalcontrol.ThermalControlConfig;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import fb.PrimitiveRanges;
import fb._Ranges;
import he.Header;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.collections.PrimitiveIterators;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import kotlin.collections.r;
import kotlin.collections.s;
import me.BufferedSink;
import me.BufferedSource;
import me.Source;
import me.g;
import me.q;
import sd.Charsets;
import sd.StringsJVM;
import sd.j;
import sd.v;
import za.PrimitiveCompanionObjects;
import za.k;
import zd.EventListener;
import zd.Headers;
import zd.HttpUrl;
import zd.OkHttpClient;
import zd.RequestBody;
import zd.ResponseBody;
import zd.b0;
import zd.e;

/* compiled from: Util.kt */
@Metadata(bv = {}, d1 = {"\u0000Ú\u0001\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\f\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\u001a\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0000\u001a\u0016\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\b\u001a;\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\f*\b\u0012\u0004\u0012\u00020\u00060\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\f2\u000e\u0010\u000f\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00060\u000e¢\u0006\u0004\b\u0010\u0010\u0011\u001a7\u0010\u0012\u001a\u00020\b*\b\u0012\u0004\u0012\u00020\u00060\f2\u000e\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\f2\u000e\u0010\u000f\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00060\u000e¢\u0006\u0004\b\u0012\u0010\u0013\u001a\u0014\u0010\u0016\u001a\u00020\u0006*\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\b\u001a-\u0010\u0019\u001a\u00020\u0018*\b\u0012\u0004\u0012\u00020\u00060\f2\u0006\u0010\u0017\u001a\u00020\u00062\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u000e¢\u0006\u0004\b\u0019\u0010\u001a\u001a%\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00060\f*\b\u0012\u0004\u0012\u00020\u00060\f2\u0006\u0010\u0017\u001a\u00020\u0006¢\u0006\u0004\b\u001b\u0010\u001c\u001a\u001e\u0010\u001f\u001a\u00020\u0018*\u00020\u00062\b\b\u0002\u0010\u001d\u001a\u00020\u00182\b\b\u0002\u0010\u001e\u001a\u00020\u0018\u001a\u001e\u0010 \u001a\u00020\u0018*\u00020\u00062\b\b\u0002\u0010\u001d\u001a\u00020\u00182\b\b\u0002\u0010\u001e\u001a\u00020\u0018\u001a\u001e\u0010!\u001a\u00020\u0006*\u00020\u00062\b\b\u0002\u0010\u001d\u001a\u00020\u00182\b\b\u0002\u0010\u001e\u001a\u00020\u0018\u001a&\u0010#\u001a\u00020\u0018*\u00020\u00062\u0006\u0010\"\u001a\u00020\u00062\b\b\u0002\u0010\u001d\u001a\u00020\u00182\b\b\u0002\u0010\u001e\u001a\u00020\u0018\u001a&\u0010&\u001a\u00020\u0018*\u00020\u00062\u0006\u0010%\u001a\u00020$2\b\b\u0002\u0010\u001d\u001a\u00020\u00182\b\b\u0002\u0010\u001e\u001a\u00020\u0018\u001a\n\u0010'\u001a\u00020\u0018*\u00020\u0006\u001a\n\u0010(\u001a\u00020\b*\u00020\u0006\u001a\u000e\u0010)\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u0006\u001a)\u0010-\u001a\u00020\u00062\u0006\u0010*\u001a\u00020\u00062\u0012\u0010,\u001a\n\u0012\u0006\b\u0001\u0012\u00020+0\f\"\u00020+¢\u0006\u0004\b-\u0010.\u001a\u0012\u00102\u001a\u000200*\u00020/2\u0006\u00101\u001a\u000200\u001a\n\u00103\u001a\u00020\u0018*\u00020$\u001a\u0010\u00107\u001a\u000206*\b\u0012\u0004\u0012\u00020504\u001a\u0010\u00108\u001a\b\u0012\u0004\u0012\u00020504*\u000206\u001a\u0012\u00109\u001a\u00020\b*\u00020\u00142\u0006\u0010\r\u001a\u00020\u0014\u001a\n\u0010<\u001a\u00020;*\u00020:\u001a\u0015\u0010?\u001a\u00020\u0018*\u00020=2\u0006\u0010>\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010A\u001a\u00020\u0018*\u00020@2\u0006\u0010>\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010B\u001a\u00020\u0000*\u00020\u00182\u0006\u0010>\u001a\u00020\u0000H\u0086\u0004\u001a\u0012\u0010E\u001a\u00020\u0004*\u00020C2\u0006\u0010D\u001a\u00020\u0018\u001a\n\u0010F\u001a\u00020\u0018*\u00020/\u001a\u001a\u0010K\u001a\u00020\b*\u00020G2\u0006\u0010H\u001a\u00020\u00182\u0006\u0010J\u001a\u00020I\u001a\u001a\u0010M\u001a\u00020\b*\u00020G2\u0006\u0010L\u001a\u00020\u00182\u0006\u0010J\u001a\u00020I\u001a\u0012\u0010P\u001a\u00020\b*\u00020N2\u0006\u0010O\u001a\u00020/\u001a\u0012\u0010S\u001a\u00020\u0018*\u00020Q2\u0006\u0010R\u001a\u00020=\u001a\u0014\u0010T\u001a\u00020\u0018*\u00020\u00062\b\b\u0002\u0010\u001d\u001a\u00020\u0018\u001a\n\u0010V\u001a\u00020\u0000*\u00020U\u001a\u0012\u0010X\u001a\u00020\u0000*\u00020\u00062\u0006\u0010W\u001a\u00020\u0000\u001a\u0014\u0010Y\u001a\u00020\u0018*\u0004\u0018\u00010\u00062\u0006\u0010W\u001a\u00020\u0018\u001a\u001c\u0010Z\u001a\b\u0012\u0004\u0012\u00028\u000004\"\u0004\b\u0000\u0010X*\b\u0012\u0004\u0012\u00028\u000004\u001a/\u0010\\\u001a\b\u0012\u0004\u0012\u00028\u000004\"\u0004\b\u0000\u0010X2\u0012\u0010[\u001a\n\u0012\u0006\b\u0001\u0012\u00028\u00000\f\"\u00028\u0000H\u0007¢\u0006\u0004\b\\\u0010]\u001a.\u0010_\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010^\"\u0004\b\u0000\u0010K\"\u0004\b\u0001\u0010!*\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010^\u001a\n\u0010a\u001a\u00020\u0004*\u00020`\u001a\n\u0010b\u001a\u00020\u0004*\u00020N\u001a'\u0010e\u001a\u00020\u0004\"\u0004\b\u0000\u0010P*\b\u0012\u0004\u0012\u00028\u00000c2\u0006\u0010d\u001a\u00028\u0000H\u0000¢\u0006\u0004\be\u0010f\u001a \u0010k\u001a\u00020j*\u00060gj\u0002`h2\u0010\u0010i\u001a\f\u0012\b\u0012\u00060gj\u0002`h04¨\u0006l"}, d2 = {"", "arrayLength", "offset", "count", "Lma/f0;", "k", "", "name", "", "daemon", "Ljava/util/concurrent/ThreadFactory;", "L", "", "other", "Ljava/util/Comparator;", "comparator", "D", "([Ljava/lang/String;[Ljava/lang/String;Ljava/util/Comparator;)[Ljava/lang/String;", "t", "([Ljava/lang/String;[Ljava/lang/String;Ljava/util/Comparator;)Z", "Lzd/u;", "includeDefaultPort", "P", ThermalBaseConfig.Item.ATTR_VALUE, "", "w", "([Ljava/lang/String;Ljava/lang/String;Ljava/util/Comparator;)I", "n", "([Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;", "startIndex", "endIndex", "y", "A", "V", "delimiters", "p", "", "delimiter", "o", "x", "i", "F", "format", "", Constants.MessagerConstants.ARGS_KEY, "s", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "Lme/f;", "Ljava/nio/charset/Charset;", ThermalControlConfig.CONFIG_TYPE_DEFAULT, "H", "G", "", "Lhe/c;", "Lzd/t;", "O", "N", "j", "Lzd/r;", "Lzd/r$c;", "g", "", "mask", "d", "", "e", "f", "Lme/e;", "medium", "Y", "I", "Lme/a0;", "duration", "Ljava/util/concurrent/TimeUnit;", "timeUnit", "K", "timeout", "r", "Ljava/net/Socket;", "source", "E", "Lme/d;", "b", "J", "C", "Lzd/b0;", "u", "defaultValue", "T", "U", "R", "elements", "v", "([Ljava/lang/Object;)Ljava/util/List;", "", "S", "Ljava/io/Closeable;", "l", "m", "", "element", "c", "(Ljava/util/List;Ljava/lang/Object;)V", "Ljava/lang/Exception;", "Lkotlin/Exception;", "suppressed", "", "X", "okhttp"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class d {

    /* renamed from: a, reason: collision with root package name */
    public static final byte[] f237a;

    /* renamed from: b, reason: collision with root package name */
    public static final Headers f238b = Headers.f20708f.g(new String[0]);

    /* renamed from: c, reason: collision with root package name */
    public static final ResponseBody f239c;

    /* renamed from: d, reason: collision with root package name */
    public static final RequestBody f240d;

    /* renamed from: e, reason: collision with root package name */
    private static final q f241e;

    /* renamed from: f, reason: collision with root package name */
    public static final TimeZone f242f;

    /* renamed from: g, reason: collision with root package name */
    private static final j f243g;

    /* renamed from: h, reason: collision with root package name */
    public static final boolean f244h;

    /* renamed from: i, reason: collision with root package name */
    public static final String f245i;

    static {
        String j02;
        String k02;
        byte[] bArr = new byte[0];
        f237a = bArr;
        f239c = ResponseBody.a.c(ResponseBody.f20532e, bArr, null, 1, null);
        f240d = RequestBody.a.d(RequestBody.f20496a, bArr, null, 0, 0, 7, null);
        q.a aVar = q.f15512h;
        g.a aVar2 = g.f15493h;
        f241e = aVar.d(aVar2.a("efbbbf"), aVar2.a("feff"), aVar2.a("fffe"), aVar2.a("0000ffff"), aVar2.a("ffff0000"));
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        k.b(timeZone);
        f242f = timeZone;
        f243g = new j("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
        f244h = false;
        String name = OkHttpClient.class.getName();
        k.d(name, "OkHttpClient::class.java.name");
        j02 = v.j0(name, "okhttp3.");
        k02 = v.k0(j02, "Client");
        f245i = k02;
    }

    public static final int A(String str, int i10, int i11) {
        k.e(str, "<this>");
        int i12 = i11 - 1;
        if (i10 <= i12) {
            while (true) {
                int i13 = i12 - 1;
                char charAt = str.charAt(i12);
                if (!((((charAt == '\t' || charAt == '\n') || charAt == '\f') || charAt == '\r') || charAt == ' ')) {
                    return i12 + 1;
                }
                if (i12 == i10) {
                    break;
                }
                i12 = i13;
            }
        }
        return i10;
    }

    public static /* synthetic */ int B(String str, int i10, int i11, int i12, Object obj) {
        if ((i12 & 1) != 0) {
            i10 = 0;
        }
        if ((i12 & 2) != 0) {
            i11 = str.length();
        }
        return A(str, i10, i11);
    }

    public static final int C(String str, int i10) {
        k.e(str, "<this>");
        int length = str.length();
        while (i10 < length) {
            int i11 = i10 + 1;
            char charAt = str.charAt(i10);
            if (charAt != ' ' && charAt != '\t') {
                return i10;
            }
            i10 = i11;
        }
        return str.length();
    }

    public static final String[] D(String[] strArr, String[] strArr2, Comparator<? super String> comparator) {
        k.e(strArr, "<this>");
        k.e(strArr2, "other");
        k.e(comparator, "comparator");
        ArrayList arrayList = new ArrayList();
        int length = strArr.length;
        int i10 = 0;
        while (i10 < length) {
            String str = strArr[i10];
            i10++;
            int length2 = strArr2.length;
            int i11 = 0;
            while (true) {
                if (i11 < length2) {
                    String str2 = strArr2[i11];
                    i11++;
                    if (comparator.compare(str, str2) == 0) {
                        arrayList.add(str);
                        break;
                    }
                }
            }
        }
        Object[] array = arrayList.toArray(new String[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        return (String[]) array;
    }

    public static final boolean E(Socket socket, BufferedSource bufferedSource) {
        k.e(socket, "<this>");
        k.e(bufferedSource, "source");
        try {
            int soTimeout = socket.getSoTimeout();
            try {
                socket.setSoTimeout(1);
                boolean z10 = !bufferedSource.s();
                socket.setSoTimeout(soTimeout);
                return z10;
            } catch (Throwable th) {
                socket.setSoTimeout(soTimeout);
                throw th;
            }
        } catch (SocketTimeoutException unused) {
            return true;
        } catch (IOException unused2) {
            return false;
        }
    }

    public static final boolean F(String str) {
        boolean r10;
        boolean r11;
        boolean r12;
        boolean r13;
        k.e(str, "name");
        r10 = StringsJVM.r(str, "Authorization", true);
        if (r10) {
            return true;
        }
        r11 = StringsJVM.r(str, "Cookie", true);
        if (r11) {
            return true;
        }
        r12 = StringsJVM.r(str, "Proxy-Authorization", true);
        if (r12) {
            return true;
        }
        r13 = StringsJVM.r(str, "Set-Cookie", true);
        return r13;
    }

    public static final int G(char c10) {
        if ('0' <= c10 && c10 < ':') {
            return c10 - '0';
        }
        char c11 = 'a';
        if (!('a' <= c10 && c10 < 'g')) {
            c11 = 'A';
            if (!('A' <= c10 && c10 < 'G')) {
                return -1;
            }
        }
        return (c10 - c11) + 10;
    }

    public static final Charset H(BufferedSource bufferedSource, Charset charset) {
        k.e(bufferedSource, "<this>");
        k.e(charset, ThermalControlConfig.CONFIG_TYPE_DEFAULT);
        int z10 = bufferedSource.z(f241e);
        if (z10 == -1) {
            return charset;
        }
        if (z10 == 0) {
            Charset charset2 = StandardCharsets.UTF_8;
            k.d(charset2, "UTF_8");
            return charset2;
        }
        if (z10 == 1) {
            Charset charset3 = StandardCharsets.UTF_16BE;
            k.d(charset3, "UTF_16BE");
            return charset3;
        }
        if (z10 == 2) {
            Charset charset4 = StandardCharsets.UTF_16LE;
            k.d(charset4, "UTF_16LE");
            return charset4;
        }
        if (z10 == 3) {
            return Charsets.f18468a.a();
        }
        if (z10 == 4) {
            return Charsets.f18468a.b();
        }
        throw new AssertionError();
    }

    public static final int I(BufferedSource bufferedSource) {
        k.e(bufferedSource, "<this>");
        return d(bufferedSource.M(), 255) | (d(bufferedSource.M(), 255) << 16) | (d(bufferedSource.M(), 255) << 8);
    }

    public static final int J(me.d dVar, byte b10) {
        k.e(dVar, "<this>");
        int i10 = 0;
        while (!dVar.s() && dVar.L(0L) == b10) {
            i10++;
            dVar.M();
        }
        return i10;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0051, code lost:
    
        if (r5 == Long.MAX_VALUE) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0053, code lost:
    
        r11.timeout().a();
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0080, code lost:
    
        return r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x005b, code lost:
    
        r11.timeout().d(r0 + r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x007d, code lost:
    
        if (r5 != Long.MAX_VALUE) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final boolean K(Source source, int i10, TimeUnit timeUnit) {
        boolean z10;
        k.e(source, "<this>");
        k.e(timeUnit, "timeUnit");
        long nanoTime = System.nanoTime();
        long c10 = source.timeout().e() ? source.timeout().c() - nanoTime : Long.MAX_VALUE;
        source.timeout().d(Math.min(c10, timeUnit.toNanos(i10)) + nanoTime);
        try {
            me.d dVar = new me.d();
            while (source.read(dVar, 8192L) != -1) {
                dVar.b();
            }
            z10 = true;
        } catch (InterruptedIOException unused) {
            z10 = false;
        } catch (Throwable th) {
            if (c10 == Long.MAX_VALUE) {
                source.timeout().a();
            } else {
                source.timeout().d(nanoTime + c10);
            }
            throw th;
        }
    }

    public static final ThreadFactory L(final String str, final boolean z10) {
        k.e(str, "name");
        return new ThreadFactory() { // from class: ae.b
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                Thread M;
                M = d.M(str, z10, runnable);
                return M;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Thread M(String str, boolean z10, Runnable runnable) {
        k.e(str, "$name");
        Thread thread = new Thread(runnable, str);
        thread.setDaemon(z10);
        return thread;
    }

    public static final List<Header> N(Headers headers) {
        PrimitiveRanges k10;
        int u7;
        k.e(headers, "<this>");
        k10 = _Ranges.k(0, headers.size());
        u7 = s.u(k10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<Integer> it = k10.iterator();
        while (it.hasNext()) {
            int b10 = ((PrimitiveIterators) it).b();
            arrayList.add(new Header(headers.e(b10), headers.g(b10)));
        }
        return arrayList;
    }

    public static final Headers O(List<Header> list) {
        k.e(list, "<this>");
        Headers.a aVar = new Headers.a();
        for (Header header : list) {
            aVar.c(header.getF12278a().w(), header.getF12279b().w());
        }
        return aVar.d();
    }

    public static final String P(HttpUrl httpUrl, boolean z10) {
        boolean I;
        String f20716d;
        k.e(httpUrl, "<this>");
        I = v.I(httpUrl.getF20716d(), ":", false, 2, null);
        if (I) {
            f20716d = '[' + httpUrl.getF20716d() + ']';
        } else {
            f20716d = httpUrl.getF20716d();
        }
        if (!z10 && httpUrl.getF20717e() == HttpUrl.f20711k.c(httpUrl.getF20713a())) {
            return f20716d;
        }
        return f20716d + COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + httpUrl.getF20717e();
    }

    public static /* synthetic */ String Q(HttpUrl httpUrl, boolean z10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            z10 = false;
        }
        return P(httpUrl, z10);
    }

    public static final <T> List<T> R(List<? extends T> list) {
        List B0;
        k.e(list, "<this>");
        B0 = _Collections.B0(list);
        List<T> unmodifiableList = Collections.unmodifiableList(B0);
        k.d(unmodifiableList, "unmodifiableList(toMutableList())");
        return unmodifiableList;
    }

    public static final <K, V> Map<K, V> S(Map<K, ? extends V> map) {
        Map<K, V> i10;
        k.e(map, "<this>");
        if (map.isEmpty()) {
            i10 = m0.i();
            return i10;
        }
        Map<K, V> unmodifiableMap = Collections.unmodifiableMap(new LinkedHashMap(map));
        k.d(unmodifiableMap, "{\n    Collections.unmodi…(LinkedHashMap(this))\n  }");
        return unmodifiableMap;
    }

    public static final long T(String str, long j10) {
        k.e(str, "<this>");
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException unused) {
            return j10;
        }
    }

    public static final int U(String str, int i10) {
        Long valueOf;
        if (str == null) {
            valueOf = null;
        } else {
            try {
                valueOf = Long.valueOf(Long.parseLong(str));
            } catch (NumberFormatException unused) {
                return i10;
            }
        }
        if (valueOf == null) {
            return i10;
        }
        long longValue = valueOf.longValue();
        if (longValue > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (longValue < 0) {
            return 0;
        }
        return (int) longValue;
    }

    public static final String V(String str, int i10, int i11) {
        k.e(str, "<this>");
        int y4 = y(str, i10, i11);
        String substring = str.substring(y4, A(str, y4, i11));
        k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ String W(String str, int i10, int i11, int i12, Object obj) {
        if ((i12 & 1) != 0) {
            i10 = 0;
        }
        if ((i12 & 2) != 0) {
            i11 = str.length();
        }
        return V(str, i10, i11);
    }

    public static final Throwable X(Exception exc, List<? extends Exception> list) {
        k.e(exc, "<this>");
        k.e(list, "suppressed");
        if (list.size() > 1) {
            System.out.println(list);
        }
        Iterator<? extends Exception> it = list.iterator();
        while (it.hasNext()) {
            ma.b.a(exc, it.next());
        }
        return exc;
    }

    public static final void Y(BufferedSink bufferedSink, int i10) {
        k.e(bufferedSink, "<this>");
        bufferedSink.t((i10 >>> 16) & 255);
        bufferedSink.t((i10 >>> 8) & 255);
        bufferedSink.t(i10 & 255);
    }

    public static final <E> void c(List<E> list, E e10) {
        k.e(list, "<this>");
        if (list.contains(e10)) {
            return;
        }
        list.add(e10);
    }

    public static final int d(byte b10, int i10) {
        return b10 & i10;
    }

    public static final int e(short s7, int i10) {
        return s7 & i10;
    }

    public static final long f(int i10, long j10) {
        return i10 & j10;
    }

    public static final EventListener.c g(final EventListener eventListener) {
        k.e(eventListener, "<this>");
        return new EventListener.c() { // from class: ae.c
            @Override // zd.EventListener.c
            public final EventListener a(e eVar) {
                EventListener h10;
                h10 = d.h(EventListener.this, eVar);
                return h10;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final EventListener h(EventListener eventListener, e eVar) {
        k.e(eventListener, "$this_asFactory");
        k.e(eVar, "it");
        return eventListener;
    }

    public static final boolean i(String str) {
        k.e(str, "<this>");
        return f243g.b(str);
    }

    public static final boolean j(HttpUrl httpUrl, HttpUrl httpUrl2) {
        k.e(httpUrl, "<this>");
        k.e(httpUrl2, "other");
        return k.a(httpUrl.getF20716d(), httpUrl2.getF20716d()) && httpUrl.getF20717e() == httpUrl2.getF20717e() && k.a(httpUrl.getF20713a(), httpUrl2.getF20713a());
    }

    public static final void k(long j10, long j11, long j12) {
        if ((j11 | j12) < 0 || j11 > j10 || j10 - j11 < j12) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static final void l(Closeable closeable) {
        k.e(closeable, "<this>");
        try {
            closeable.close();
        } catch (RuntimeException e10) {
            throw e10;
        } catch (Exception unused) {
        }
    }

    public static final void m(Socket socket) {
        k.e(socket, "<this>");
        try {
            socket.close();
        } catch (AssertionError e10) {
            throw e10;
        } catch (RuntimeException e11) {
            if (!k.a(e11.getMessage(), "bio == null")) {
                throw e11;
            }
        } catch (Exception unused) {
        }
    }

    public static final String[] n(String[] strArr, String str) {
        int D;
        k.e(strArr, "<this>");
        k.e(str, ThermalBaseConfig.Item.ATTR_VALUE);
        Object[] copyOf = Arrays.copyOf(strArr, strArr.length + 1);
        k.d(copyOf, "copyOf(this, newSize)");
        String[] strArr2 = (String[]) copyOf;
        D = _Arrays.D(strArr2);
        strArr2[D] = str;
        return strArr2;
    }

    public static final int o(String str, char c10, int i10, int i11) {
        k.e(str, "<this>");
        while (i10 < i11) {
            int i12 = i10 + 1;
            if (str.charAt(i10) == c10) {
                return i10;
            }
            i10 = i12;
        }
        return i11;
    }

    public static final int p(String str, String str2, int i10, int i11) {
        boolean H;
        k.e(str, "<this>");
        k.e(str2, "delimiters");
        while (i10 < i11) {
            int i12 = i10 + 1;
            H = v.H(str2, str.charAt(i10), false, 2, null);
            if (H) {
                return i10;
            }
            i10 = i12;
        }
        return i11;
    }

    public static /* synthetic */ int q(String str, char c10, int i10, int i11, int i12, Object obj) {
        if ((i12 & 2) != 0) {
            i10 = 0;
        }
        if ((i12 & 4) != 0) {
            i11 = str.length();
        }
        return o(str, c10, i10, i11);
    }

    public static final boolean r(Source source, int i10, TimeUnit timeUnit) {
        k.e(source, "<this>");
        k.e(timeUnit, "timeUnit");
        try {
            return K(source, i10, timeUnit);
        } catch (IOException unused) {
            return false;
        }
    }

    public static final String s(String str, Object... objArr) {
        k.e(str, "format");
        k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
        PrimitiveCompanionObjects primitiveCompanionObjects = PrimitiveCompanionObjects.f20358a;
        Locale locale = Locale.US;
        Object[] copyOf = Arrays.copyOf(objArr, objArr.length);
        String format = String.format(locale, str, Arrays.copyOf(copyOf, copyOf.length));
        k.d(format, "format(locale, format, *args)");
        return format;
    }

    public static final boolean t(String[] strArr, String[] strArr2, Comparator<? super String> comparator) {
        k.e(strArr, "<this>");
        k.e(comparator, "comparator");
        if (!(strArr.length == 0) && strArr2 != null) {
            if (!(strArr2.length == 0)) {
                int length = strArr.length;
                int i10 = 0;
                while (i10 < length) {
                    String str = strArr[i10];
                    i10++;
                    Iterator a10 = za.b.a(strArr2);
                    while (a10.hasNext()) {
                        if (comparator.compare(str, (String) a10.next()) == 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static final long u(b0 b0Var) {
        k.e(b0Var, "<this>");
        String d10 = b0Var.getF20510j().d("Content-Length");
        if (d10 == null) {
            return -1L;
        }
        return T(d10, -1L);
    }

    @SafeVarargs
    public static final <T> List<T> v(T... tArr) {
        List m10;
        k.e(tArr, "elements");
        Object[] objArr = (Object[]) tArr.clone();
        m10 = r.m(Arrays.copyOf(objArr, objArr.length));
        List<T> unmodifiableList = Collections.unmodifiableList(m10);
        k.d(unmodifiableList, "unmodifiableList(listOf(*elements.clone()))");
        return unmodifiableList;
    }

    public static final int w(String[] strArr, String str, Comparator<String> comparator) {
        k.e(strArr, "<this>");
        k.e(str, ThermalBaseConfig.Item.ATTR_VALUE);
        k.e(comparator, "comparator");
        int length = strArr.length;
        for (int i10 = 0; i10 < length; i10++) {
            if (comparator.compare(strArr[i10], str) == 0) {
                return i10;
            }
        }
        return -1;
    }

    public static final int x(String str) {
        k.e(str, "<this>");
        int length = str.length();
        int i10 = 0;
        while (i10 < length) {
            int i11 = i10 + 1;
            char charAt = str.charAt(i10);
            if (k.f(charAt, 31) <= 0 || k.f(charAt, 127) >= 0) {
                return i10;
            }
            i10 = i11;
        }
        return -1;
    }

    public static final int y(String str, int i10, int i11) {
        k.e(str, "<this>");
        while (i10 < i11) {
            int i12 = i10 + 1;
            char charAt = str.charAt(i10);
            if (!((((charAt == '\t' || charAt == '\n') || charAt == '\f') || charAt == '\r') || charAt == ' ')) {
                return i10;
            }
            i10 = i12;
        }
        return i11;
    }

    public static /* synthetic */ int z(String str, int i10, int i11, int i12, Object obj) {
        if ((i12 & 1) != 0) {
            i10 = 0;
        }
        if ((i12 & 2) != 0) {
            i11 = str.length();
        }
        return y(str, i10, i11);
    }
}
