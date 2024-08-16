package he;

import de.TaskRunner;
import he.Http2Reader;
import ie.Platform;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import ma.Unit;
import me.BufferedSink;
import me.BufferedSource;
import za.DefaultConstructorMarker;
import za.x;
import za.y;

/* compiled from: Http2Connection.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0092\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010%\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\b\u0084\u0001\u0085\u0001\u0086\u0001\u0087\u0001B\u0015\b\u0000\u0012\b\u0010\u0081\u0001\u001a\u00030\u0080\u0001¢\u0006\u0006\b\u0082\u0001\u0010\u0083\u0001J&\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\b\u001a\u00020\u0007H\u0002J\u0012\u0010\u000e\u001a\u00020\r2\b\u0010\f\u001a\u0004\u0018\u00010\u000bH\u0002J\u0010\u0010\u0010\u001a\u0004\u0018\u00010\t2\u0006\u0010\u000f\u001a\u00020\u0002J\u0019\u0010\u0012\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0011\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u0017\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0014H\u0000¢\u0006\u0004\b\u0016\u0010\u0017J\u001c\u0010\u0018\u001a\u00020\t2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\b\u001a\u00020\u0007J-\u0010\u001b\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u00072\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0000¢\u0006\u0004\b\u001b\u0010\u001cJ(\u0010 \u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u00072\b\u0010\u001e\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001f\u001a\u00020\u0014J\u001f\u0010#\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\"\u001a\u00020!H\u0000¢\u0006\u0004\b#\u0010$J\u001f\u0010&\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010%\u001a\u00020!H\u0000¢\u0006\u0004\b&\u0010$J\u001f\u0010(\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010'\u001a\u00020\u0014H\u0000¢\u0006\u0004\b(\u0010)J\u001e\u0010-\u001a\u00020\r2\u0006\u0010*\u001a\u00020\u00072\u0006\u0010+\u001a\u00020\u00022\u0006\u0010,\u001a\u00020\u0002J\u0006\u0010.\u001a\u00020\rJ\u000e\u0010/\u001a\u00020\r2\u0006\u0010%\u001a\u00020!J\b\u00100\u001a\u00020\rH\u0016J)\u00104\u001a\u00020\r2\u0006\u00101\u001a\u00020!2\u0006\u00102\u001a\u00020!2\b\u00103\u001a\u0004\u0018\u00010\u000bH\u0000¢\u0006\u0004\b4\u00105J\u001c\u00109\u001a\u00020\r2\b\b\u0002\u00106\u001a\u00020\u00072\b\b\u0002\u00108\u001a\u000207H\u0007J\u000e\u0010;\u001a\u00020\u00072\u0006\u0010:\u001a\u00020\u0014J\u000f\u0010<\u001a\u00020\rH\u0000¢\u0006\u0004\b<\u0010=J\u0017\u0010>\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0002H\u0000¢\u0006\u0004\b>\u0010?J%\u0010@\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0000¢\u0006\u0004\b@\u0010AJ-\u0010C\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010B\u001a\u00020\u0007H\u0000¢\u0006\u0004\bC\u0010DJ/\u0010G\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010F\u001a\u00020E2\u0006\u0010\u001f\u001a\u00020\u00022\u0006\u0010B\u001a\u00020\u0007H\u0000¢\u0006\u0004\bG\u0010HJ\u001f\u0010I\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\"\u001a\u00020!H\u0000¢\u0006\u0004\bI\u0010$R\u001a\u0010J\u001a\u00020\u00078\u0000X\u0080\u0004¢\u0006\f\n\u0004\bJ\u0010K\u001a\u0004\bL\u0010MR\u001a\u0010O\u001a\u00020N8\u0000X\u0080\u0004¢\u0006\f\n\u0004\bO\u0010P\u001a\u0004\bQ\u0010RR&\u0010T\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0S8\u0000X\u0080\u0004¢\u0006\f\n\u0004\bT\u0010U\u001a\u0004\bV\u0010WR\u001a\u0010Y\u001a\u00020X8\u0000X\u0080\u0004¢\u0006\f\n\u0004\bY\u0010Z\u001a\u0004\b[\u0010\\R\"\u0010]\u001a\u00020\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b]\u0010^\u001a\u0004\b_\u0010`\"\u0004\ba\u0010bR\"\u0010c\u001a\u00020\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\bc\u0010^\u001a\u0004\bd\u0010`\"\u0004\be\u0010bR\u0017\u0010g\u001a\u00020f8\u0006¢\u0006\f\n\u0004\bg\u0010h\u001a\u0004\bi\u0010jR\"\u0010k\u001a\u00020f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bk\u0010h\u001a\u0004\bl\u0010j\"\u0004\bm\u0010nR$\u0010p\u001a\u00020\u00142\u0006\u0010o\u001a\u00020\u00148\u0006@BX\u0086\u000e¢\u0006\f\n\u0004\bp\u0010q\u001a\u0004\br\u0010sR$\u0010t\u001a\u00020\u00142\u0006\u0010o\u001a\u00020\u00148\u0006@BX\u0086\u000e¢\u0006\f\n\u0004\bt\u0010q\u001a\u0004\bu\u0010sR\u001a\u0010w\u001a\u00020v8\u0000X\u0080\u0004¢\u0006\f\n\u0004\bw\u0010x\u001a\u0004\by\u0010zR\u0017\u0010|\u001a\u00020{8\u0006¢\u0006\f\n\u0004\b|\u0010}\u001a\u0004\b~\u0010\u007f¨\u0006\u0088\u0001"}, d2 = {"Lhe/f;", "Ljava/io/Closeable;", "", "associatedStreamId", "", "Lhe/c;", "requestHeaders", "", "out", "Lhe/i;", "J0", "Ljava/io/IOException;", "e", "Lma/f0;", "u0", "id", "D0", "streamId", "Q0", "(I)Lhe/i;", "", "read", "Y0", "(J)V", "K0", "outFinished", "alternating", "a1", "(IZLjava/util/List;)V", "Lme/d;", "buffer", "byteCount", "Z0", "Lhe/b;", "errorCode", "d1", "(ILhe/b;)V", "statusCode", "c1", "unacknowledgedBytesRead", "e1", "(IJ)V", "reply", "payload1", "payload2", "b1", "flush", "V0", "close", "connectionCode", "streamCode", "cause", "t0", "(Lhe/b;Lhe/b;Ljava/io/IOException;)V", "sendConnectionPreface", "Lde/e;", "taskRunner", "W0", "nowNs", "I0", "R0", "()V", "P0", "(I)Z", "N0", "(ILjava/util/List;)V", "inFinished", "M0", "(ILjava/util/List;Z)V", "Lme/f;", "source", "L0", "(ILme/f;IZ)V", "O0", "client", "Z", "v0", "()Z", "Lhe/f$c;", "listener", "Lhe/f$c;", "y0", "()Lhe/f$c;", "", "streams", "Ljava/util/Map;", "E0", "()Ljava/util/Map;", "", "connectionName", "Ljava/lang/String;", "w0", "()Ljava/lang/String;", "lastGoodStreamId", "I", "x0", "()I", "S0", "(I)V", "nextStreamId", "z0", "T0", "Lhe/m;", "okHttpSettings", "Lhe/m;", "A0", "()Lhe/m;", "peerSettings", "B0", "U0", "(Lhe/m;)V", "<set-?>", "writeBytesTotal", "J", "G0", "()J", "writeBytesMaximum", "F0", "Ljava/net/Socket;", "socket", "Ljava/net/Socket;", "C0", "()Ljava/net/Socket;", "Lhe/j;", "writer", "Lhe/j;", "H0", "()Lhe/j;", "Lhe/f$a;", "builder", "<init>", "(Lhe/f$a;)V", "a", "b", "c", "d", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.f */
/* loaded from: classes2.dex */
public final class Http2Connection implements Closeable {
    public static final b G = new b(null);
    private static final Settings H;
    private long A;
    private long B;
    private final Socket C;
    private final Http2Writer D;
    private final d E;
    private final Set<Integer> F;

    /* renamed from: e */
    private final boolean f12307e;

    /* renamed from: f */
    private final c f12308f;

    /* renamed from: g */
    private final Map<Integer, Http2Stream> f12309g;

    /* renamed from: h */
    private final String f12310h;

    /* renamed from: i */
    private int f12311i;

    /* renamed from: j */
    private int f12312j;

    /* renamed from: k */
    private boolean f12313k;

    /* renamed from: l */
    private final TaskRunner f12314l;

    /* renamed from: m */
    private final de.d f12315m;

    /* renamed from: n */
    private final de.d f12316n;

    /* renamed from: o */
    private final de.d f12317o;

    /* renamed from: p */
    private final PushObserver f12318p;

    /* renamed from: q */
    private long f12319q;

    /* renamed from: r */
    private long f12320r;

    /* renamed from: s */
    private long f12321s;

    /* renamed from: t */
    private long f12322t;

    /* renamed from: u */
    private long f12323u;

    /* renamed from: v */
    private long f12324v;

    /* renamed from: w */
    private final Settings f12325w;

    /* renamed from: x */
    private Settings f12326x;

    /* renamed from: y */
    private long f12327y;

    /* renamed from: z */
    private long f12328z;

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u001e\n\u0002\u0018\u0002\n\u0002\b\u000e\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0014\u001a\u00020\u0013\u0012\u0006\u0010\u001b\u001a\u00020\u001a¢\u0006\u0004\bE\u0010FJ.\u0010\n\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\bH\u0007J\u000e\u0010\r\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020\u000bJ\u000e\u0010\u0010\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u000eJ\u0006\u0010\u0012\u001a\u00020\u0011R\"\u0010\u0014\u001a\u00020\u00138\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001b\u001a\u00020\u001a8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u001d\u0010\u001eR\"\u0010\u0003\u001a\u00020\u00028\u0000@\u0000X\u0080.¢\u0006\u0012\n\u0004\b\u0003\u0010\u001f\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\"\u0010$\u001a\u00020\u00048\u0000@\u0000X\u0080.¢\u0006\u0012\n\u0004\b$\u0010%\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R\"\u0010\u0007\u001a\u00020\u00068\u0000@\u0000X\u0080.¢\u0006\u0012\n\u0004\b\u0007\u0010*\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\"\u0010\t\u001a\u00020\b8\u0000@\u0000X\u0080.¢\u0006\u0012\n\u0004\b\t\u0010/\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\"\u0010\f\u001a\u00020\u000b8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\f\u00104\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\"\u0010:\u001a\u0002098\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b:\u0010;\u001a\u0004\b<\u0010=\"\u0004\b>\u0010?R\"\u0010\u000f\u001a\u00020\u000e8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u000f\u0010@\u001a\u0004\bA\u0010B\"\u0004\bC\u0010D¨\u0006G"}, d2 = {"Lhe/f$a;", "", "Ljava/net/Socket;", "socket", "", "peerName", "Lme/f;", "source", "Lme/e;", "sink", "s", "Lhe/f$c;", "listener", "k", "", "pingIntervalMillis", "l", "Lhe/f;", "a", "", "client", "Z", "b", "()Z", "setClient$okhttp", "(Z)V", "Lde/e;", "taskRunner", "Lde/e;", "j", "()Lde/e;", "Ljava/net/Socket;", "h", "()Ljava/net/Socket;", "q", "(Ljava/net/Socket;)V", "connectionName", "Ljava/lang/String;", "c", "()Ljava/lang/String;", "m", "(Ljava/lang/String;)V", "Lme/f;", "i", "()Lme/f;", "r", "(Lme/f;)V", "Lme/e;", "g", "()Lme/e;", "p", "(Lme/e;)V", "Lhe/f$c;", "d", "()Lhe/f$c;", "n", "(Lhe/f$c;)V", "Lhe/l;", "pushObserver", "Lhe/l;", "f", "()Lhe/l;", "setPushObserver$okhttp", "(Lhe/l;)V", "I", "e", "()I", "o", "(I)V", "<init>", "(ZLde/e;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a */
        private boolean f12329a;

        /* renamed from: b */
        private final TaskRunner f12330b;

        /* renamed from: c */
        public Socket f12331c;

        /* renamed from: d */
        public String f12332d;

        /* renamed from: e */
        public BufferedSource f12333e;

        /* renamed from: f */
        public BufferedSink f12334f;

        /* renamed from: g */
        private c f12335g;

        /* renamed from: h */
        private PushObserver f12336h;

        /* renamed from: i */
        private int f12337i;

        public a(boolean z10, TaskRunner taskRunner) {
            za.k.e(taskRunner, "taskRunner");
            this.f12329a = z10;
            this.f12330b = taskRunner;
            this.f12335g = c.f12339b;
            this.f12336h = PushObserver.f12464b;
        }

        public final Http2Connection a() {
            return new Http2Connection(this);
        }

        /* renamed from: b, reason: from getter */
        public final boolean getF12329a() {
            return this.f12329a;
        }

        public final String c() {
            String str = this.f12332d;
            if (str != null) {
                return str;
            }
            za.k.s("connectionName");
            return null;
        }

        /* renamed from: d, reason: from getter */
        public final c getF12335g() {
            return this.f12335g;
        }

        /* renamed from: e, reason: from getter */
        public final int getF12337i() {
            return this.f12337i;
        }

        /* renamed from: f, reason: from getter */
        public final PushObserver getF12336h() {
            return this.f12336h;
        }

        public final BufferedSink g() {
            BufferedSink bufferedSink = this.f12334f;
            if (bufferedSink != null) {
                return bufferedSink;
            }
            za.k.s("sink");
            return null;
        }

        public final Socket h() {
            Socket socket = this.f12331c;
            if (socket != null) {
                return socket;
            }
            za.k.s("socket");
            return null;
        }

        public final BufferedSource i() {
            BufferedSource bufferedSource = this.f12333e;
            if (bufferedSource != null) {
                return bufferedSource;
            }
            za.k.s("source");
            return null;
        }

        /* renamed from: j, reason: from getter */
        public final TaskRunner getF12330b() {
            return this.f12330b;
        }

        public final a k(c listener) {
            za.k.e(listener, "listener");
            n(listener);
            return this;
        }

        public final a l(int pingIntervalMillis) {
            o(pingIntervalMillis);
            return this;
        }

        public final void m(String str) {
            za.k.e(str, "<set-?>");
            this.f12332d = str;
        }

        public final void n(c cVar) {
            za.k.e(cVar, "<set-?>");
            this.f12335g = cVar;
        }

        public final void o(int i10) {
            this.f12337i = i10;
        }

        public final void p(BufferedSink bufferedSink) {
            za.k.e(bufferedSink, "<set-?>");
            this.f12334f = bufferedSink;
        }

        public final void q(Socket socket) {
            za.k.e(socket, "<set-?>");
            this.f12331c = socket;
        }

        public final void r(BufferedSource bufferedSource) {
            za.k.e(bufferedSource, "<set-?>");
            this.f12333e = bufferedSource;
        }

        public final a s(Socket socket, String peerName, BufferedSource source, BufferedSink sink) {
            String l10;
            za.k.e(socket, "socket");
            za.k.e(peerName, "peerName");
            za.k.e(source, "source");
            za.k.e(sink, "sink");
            q(socket);
            if (getF12329a()) {
                l10 = ae.d.f245i + ' ' + peerName;
            } else {
                l10 = za.k.l("MockWebServer ", peerName);
            }
            m(l10);
            r(source);
            p(sink);
            return this;
        }
    }

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0003\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\b\u001a\u00020\u00078\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u00078\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\tR\u0014\u0010\u000b\u001a\u00020\u00078\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\tR\u0014\u0010\f\u001a\u00020\u00078\u0006X\u0086T¢\u0006\u0006\n\u0004\b\f\u0010\tR\u0014\u0010\r\u001a\u00020\u00078\u0006X\u0086T¢\u0006\u0006\n\u0004\b\r\u0010\t¨\u0006\u0010"}, d2 = {"Lhe/f$b;", "", "Lhe/m;", "DEFAULT_SETTINGS", "Lhe/m;", "a", "()Lhe/m;", "", "AWAIT_PING", "I", "DEGRADED_PING", "DEGRADED_PONG_TIMEOUT_NS", "INTERVAL_PING", "OKHTTP_CLIENT_WINDOW_SIZE", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Settings a() {
            return Http2Connection.H;
        }
    }

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001:\u0001\u0005B\u0007¢\u0006\u0004\b\u000b\u0010\fJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&J\u0018\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bH\u0016¨\u0006\r"}, d2 = {"Lhe/f$c;", "", "Lhe/i;", "stream", "Lma/f0;", "b", "Lhe/f;", "connection", "Lhe/m;", "settings", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$c */
    /* loaded from: classes2.dex */
    public static abstract class c {

        /* renamed from: a */
        public static final b f12338a = new b(null);

        /* renamed from: b */
        public static final c f12339b = new a();

        /* compiled from: Http2Connection.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"he/f$c$a", "Lhe/f$c;", "Lhe/i;", "stream", "Lma/f0;", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: he.f$c$a */
        /* loaded from: classes2.dex */
        public static final class a extends c {
            a() {
            }

            @Override // he.Http2Connection.c
            public void b(Http2Stream http2Stream) {
                za.k.e(http2Stream, "stream");
                http2Stream.d(ErrorCode.REFUSED_STREAM, null);
            }
        }

        /* compiled from: Http2Connection.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lhe/f$c$b;", "", "Lhe/f$c;", "REFUSE_INCOMING_STREAMS", "Lhe/f$c;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: he.f$c$b */
        /* loaded from: classes2.dex */
        public static final class b {
            private b() {
            }

            public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        public void a(Http2Connection http2Connection, Settings settings) {
            za.k.e(http2Connection, "connection");
            za.k.e(settings, "settings");
        }

        public abstract void b(Http2Stream http2Stream);
    }

    /* compiled from: Http2Connection.kt */
    @Metadata(bv = {}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0011\b\u0000\u0012\u0006\u0010.\u001a\u00020-¢\u0006\u0004\b/\u00100J\t\u0010\u0004\u001a\u00020\u0003H\u0096\u0002J(\u0010\f\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u0007H\u0016J.\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u00072\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0016J\u0018\u0010\u0014\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\u0018\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0016H\u0016J\u0016\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0016J\b\u0010\u001a\u001a\u00020\u0003H\u0016J \u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0016J \u0010\"\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010!\u001a\u00020 H\u0016J\u0018\u0010%\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010$\u001a\u00020#H\u0016J(\u0010)\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010&\u001a\u00020\u00072\u0006\u0010'\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u0005H\u0016J&\u0010,\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010*\u001a\u00020\u00072\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0016¨\u00061"}, d2 = {"Lhe/f$d;", "Lhe/h$c;", "Lkotlin/Function0;", "Lma/f0;", "q", "", "inFinished", "", "streamId", "Lme/f;", "source", "length", "b", "associatedStreamId", "", "Lhe/c;", "headerBlock", "c", "Lhe/b;", "errorCode", "l", "clearPrevious", "Lhe/m;", "settings", "n", "p", "a", "ack", "payload1", "payload2", "i", "lastGoodStreamId", "Lme/g;", "debugData", "f", "", "windowSizeIncrement", "d", "streamDependency", "weight", "exclusive", "j", "promisedStreamId", "requestHeaders", "o", "Lhe/h;", "reader", "<init>", "(Lhe/f;Lhe/h;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$d */
    /* loaded from: classes2.dex */
    public final class d implements Http2Reader.c, ya.a<Unit> {

        /* renamed from: e */
        private final Http2Reader f12340e;

        /* renamed from: f */
        final /* synthetic */ Http2Connection f12341f;

        /* compiled from: TaskQueue.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: he.f$d$a */
        /* loaded from: classes2.dex */
        public static final class a extends de.a {

            /* renamed from: e */
            final /* synthetic */ String f12342e;

            /* renamed from: f */
            final /* synthetic */ boolean f12343f;

            /* renamed from: g */
            final /* synthetic */ Http2Connection f12344g;

            /* renamed from: h */
            final /* synthetic */ y f12345h;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public a(String str, boolean z10, Http2Connection http2Connection, y yVar) {
                super(str, z10);
                this.f12342e = str;
                this.f12343f = z10;
                this.f12344g = http2Connection;
                this.f12345h = yVar;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // de.a
            public long f() {
                this.f12344g.getF12308f().a(this.f12344g, (Settings) this.f12345h.f20376e);
                return -1L;
            }
        }

        /* compiled from: TaskQueue.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: he.f$d$b */
        /* loaded from: classes2.dex */
        public static final class b extends de.a {

            /* renamed from: e */
            final /* synthetic */ String f12346e;

            /* renamed from: f */
            final /* synthetic */ boolean f12347f;

            /* renamed from: g */
            final /* synthetic */ Http2Connection f12348g;

            /* renamed from: h */
            final /* synthetic */ Http2Stream f12349h;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public b(String str, boolean z10, Http2Connection http2Connection, Http2Stream http2Stream) {
                super(str, z10);
                this.f12346e = str;
                this.f12347f = z10;
                this.f12348g = http2Connection;
                this.f12349h = http2Stream;
            }

            @Override // de.a
            public long f() {
                try {
                    this.f12348g.getF12308f().b(this.f12349h);
                    return -1L;
                } catch (IOException e10) {
                    Platform.f12870a.g().j(za.k.l("Http2Connection.Listener failure for ", this.f12348g.getF12310h()), 4, e10);
                    try {
                        this.f12349h.d(ErrorCode.PROTOCOL_ERROR, e10);
                        return -1L;
                    } catch (IOException unused) {
                        return -1L;
                    }
                }
            }
        }

        /* compiled from: TaskQueue.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: he.f$d$c */
        /* loaded from: classes2.dex */
        public static final class c extends de.a {

            /* renamed from: e */
            final /* synthetic */ String f12350e;

            /* renamed from: f */
            final /* synthetic */ boolean f12351f;

            /* renamed from: g */
            final /* synthetic */ Http2Connection f12352g;

            /* renamed from: h */
            final /* synthetic */ int f12353h;

            /* renamed from: i */
            final /* synthetic */ int f12354i;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public c(String str, boolean z10, Http2Connection http2Connection, int i10, int i11) {
                super(str, z10);
                this.f12350e = str;
                this.f12351f = z10;
                this.f12352g = http2Connection;
                this.f12353h = i10;
                this.f12354i = i11;
            }

            @Override // de.a
            public long f() {
                this.f12352g.b1(true, this.f12353h, this.f12354i);
                return -1L;
            }
        }

        /* compiled from: TaskQueue.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: he.f$d$d */
        /* loaded from: classes2.dex */
        public static final class C0050d extends de.a {

            /* renamed from: e */
            final /* synthetic */ String f12355e;

            /* renamed from: f */
            final /* synthetic */ boolean f12356f;

            /* renamed from: g */
            final /* synthetic */ d f12357g;

            /* renamed from: h */
            final /* synthetic */ boolean f12358h;

            /* renamed from: i */
            final /* synthetic */ Settings f12359i;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public C0050d(String str, boolean z10, d dVar, boolean z11, Settings settings) {
                super(str, z10);
                this.f12355e = str;
                this.f12356f = z10;
                this.f12357g = dVar;
                this.f12358h = z11;
                this.f12359i = settings;
            }

            @Override // de.a
            public long f() {
                this.f12357g.p(this.f12358h, this.f12359i);
                return -1L;
            }
        }

        public d(Http2Connection http2Connection, Http2Reader http2Reader) {
            za.k.e(http2Connection, "this$0");
            za.k.e(http2Reader, "reader");
            this.f12341f = http2Connection;
            this.f12340e = http2Reader;
        }

        @Override // he.Http2Reader.c
        public void a() {
        }

        @Override // he.Http2Reader.c
        public void b(boolean z10, int i10, BufferedSource bufferedSource, int i11) {
            za.k.e(bufferedSource, "source");
            if (this.f12341f.P0(i10)) {
                this.f12341f.L0(i10, bufferedSource, i11, z10);
                return;
            }
            Http2Stream D0 = this.f12341f.D0(i10);
            if (D0 == null) {
                this.f12341f.d1(i10, ErrorCode.PROTOCOL_ERROR);
                long j10 = i11;
                this.f12341f.Y0(j10);
                bufferedSource.V(j10);
                return;
            }
            D0.w(bufferedSource, i11);
            if (z10) {
                D0.x(ae.d.f238b, true);
            }
        }

        @Override // he.Http2Reader.c
        public void c(boolean z10, int i10, int i11, List<Header> list) {
            za.k.e(list, "headerBlock");
            if (this.f12341f.P0(i10)) {
                this.f12341f.M0(i10, list, z10);
                return;
            }
            Http2Connection http2Connection = this.f12341f;
            synchronized (http2Connection) {
                Http2Stream D0 = http2Connection.D0(i10);
                if (D0 == null) {
                    if (http2Connection.f12313k) {
                        return;
                    }
                    if (i10 <= http2Connection.getF12311i()) {
                        return;
                    }
                    if (i10 % 2 == http2Connection.getF12312j() % 2) {
                        return;
                    }
                    Http2Stream http2Stream = new Http2Stream(i10, http2Connection, false, z10, ae.d.O(list));
                    http2Connection.S0(i10);
                    http2Connection.E0().put(Integer.valueOf(i10), http2Stream);
                    http2Connection.f12314l.i().i(new b(http2Connection.getF12310h() + '[' + i10 + "] onStream", true, http2Connection, http2Stream), 0L);
                    return;
                }
                Unit unit = Unit.f15173a;
                D0.x(ae.d.O(list), z10);
            }
        }

        @Override // he.Http2Reader.c
        public void d(int i10, long j10) {
            if (i10 == 0) {
                Http2Connection http2Connection = this.f12341f;
                synchronized (http2Connection) {
                    http2Connection.B = http2Connection.getB() + j10;
                    http2Connection.notifyAll();
                    Unit unit = Unit.f15173a;
                }
                return;
            }
            Http2Stream D0 = this.f12341f.D0(i10);
            if (D0 != null) {
                synchronized (D0) {
                    D0.a(j10);
                    Unit unit2 = Unit.f15173a;
                }
            }
        }

        @Override // he.Http2Reader.c
        public void f(int i10, ErrorCode errorCode, me.g gVar) {
            int i11;
            Object[] array;
            za.k.e(errorCode, "errorCode");
            za.k.e(gVar, "debugData");
            gVar.t();
            Http2Connection http2Connection = this.f12341f;
            synchronized (http2Connection) {
                i11 = 0;
                array = http2Connection.E0().values().toArray(new Http2Stream[0]);
                if (array != null) {
                    http2Connection.f12313k = true;
                    Unit unit = Unit.f15173a;
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                }
            }
            Http2Stream[] http2StreamArr = (Http2Stream[]) array;
            int length = http2StreamArr.length;
            while (i11 < length) {
                Http2Stream http2Stream = http2StreamArr[i11];
                i11++;
                if (http2Stream.getF12421a() > i10 && http2Stream.t()) {
                    http2Stream.y(ErrorCode.REFUSED_STREAM);
                    this.f12341f.Q0(http2Stream.getF12421a());
                }
            }
        }

        @Override // he.Http2Reader.c
        public void i(boolean z10, int i10, int i11) {
            if (z10) {
                Http2Connection http2Connection = this.f12341f;
                synchronized (http2Connection) {
                    if (i10 == 1) {
                        http2Connection.f12320r++;
                    } else if (i10 != 2) {
                        if (i10 == 3) {
                            http2Connection.f12323u++;
                            http2Connection.notifyAll();
                        }
                        Unit unit = Unit.f15173a;
                    } else {
                        http2Connection.f12322t++;
                    }
                }
                return;
            }
            this.f12341f.f12315m.i(new c(za.k.l(this.f12341f.getF12310h(), " ping"), true, this.f12341f, i10, i11), 0L);
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            q();
            return Unit.f15173a;
        }

        @Override // he.Http2Reader.c
        public void j(int i10, int i11, int i12, boolean z10) {
        }

        @Override // he.Http2Reader.c
        public void l(int i10, ErrorCode errorCode) {
            za.k.e(errorCode, "errorCode");
            if (this.f12341f.P0(i10)) {
                this.f12341f.O0(i10, errorCode);
                return;
            }
            Http2Stream Q0 = this.f12341f.Q0(i10);
            if (Q0 == null) {
                return;
            }
            Q0.y(errorCode);
        }

        @Override // he.Http2Reader.c
        public void n(boolean z10, Settings settings) {
            za.k.e(settings, "settings");
            this.f12341f.f12315m.i(new C0050d(za.k.l(this.f12341f.getF12310h(), " applyAndAckSettings"), true, this, z10, settings), 0L);
        }

        @Override // he.Http2Reader.c
        public void o(int i10, int i11, List<Header> list) {
            za.k.e(list, "requestHeaders");
            this.f12341f.N0(i11, list);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r12v1 */
        /* JADX WARN: Type inference failed for: r12v2, types: [T, he.m] */
        /* JADX WARN: Type inference failed for: r12v3 */
        public final void p(boolean z10, Settings settings) {
            ?? r12;
            long c10;
            int i10;
            Http2Stream[] http2StreamArr;
            za.k.e(settings, "settings");
            y yVar = new y();
            Http2Writer d10 = this.f12341f.getD();
            Http2Connection http2Connection = this.f12341f;
            synchronized (d10) {
                synchronized (http2Connection) {
                    Settings f12326x = http2Connection.getF12326x();
                    if (z10) {
                        r12 = settings;
                    } else {
                        Settings settings2 = new Settings();
                        settings2.g(f12326x);
                        settings2.g(settings);
                        r12 = settings2;
                    }
                    yVar.f20376e = r12;
                    c10 = r12.c() - f12326x.c();
                    i10 = 0;
                    if (c10 != 0 && !http2Connection.E0().isEmpty()) {
                        Object[] array = http2Connection.E0().values().toArray(new Http2Stream[0]);
                        if (array == null) {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                        }
                        http2StreamArr = (Http2Stream[]) array;
                        http2Connection.U0((Settings) yVar.f20376e);
                        http2Connection.f12317o.i(new a(za.k.l(http2Connection.getF12310h(), " onSettings"), true, http2Connection, yVar), 0L);
                        Unit unit = Unit.f15173a;
                    }
                    http2StreamArr = null;
                    http2Connection.U0((Settings) yVar.f20376e);
                    http2Connection.f12317o.i(new a(za.k.l(http2Connection.getF12310h(), " onSettings"), true, http2Connection, yVar), 0L);
                    Unit unit2 = Unit.f15173a;
                }
                try {
                    http2Connection.getD().b((Settings) yVar.f20376e);
                } catch (IOException e10) {
                    http2Connection.u0(e10);
                }
                Unit unit3 = Unit.f15173a;
            }
            if (http2StreamArr != null) {
                int length = http2StreamArr.length;
                while (i10 < length) {
                    Http2Stream http2Stream = http2StreamArr[i10];
                    i10++;
                    synchronized (http2Stream) {
                        http2Stream.a(c10);
                        Unit unit4 = Unit.f15173a;
                    }
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r5v3 */
        /* JADX WARN: Type inference failed for: r5v4, types: [he.h, java.io.Closeable] */
        public void q() {
            ErrorCode errorCode;
            ErrorCode errorCode2;
            ErrorCode errorCode3 = ErrorCode.INTERNAL_ERROR;
            IOException e10 = null;
            try {
                try {
                    this.f12340e.m(this);
                    do {
                    } while (this.f12340e.c(false, this));
                    errorCode2 = ErrorCode.NO_ERROR;
                } catch (IOException e11) {
                    e10 = e11;
                } catch (Throwable th) {
                    th = th;
                    errorCode = errorCode3;
                    this.f12341f.t0(errorCode, errorCode3, e10);
                    ae.d.l(this.f12340e);
                    throw th;
                }
                try {
                    errorCode3 = ErrorCode.CANCEL;
                    this.f12341f.t0(errorCode2, errorCode3, null);
                    errorCode = errorCode2;
                } catch (IOException e12) {
                    e10 = e12;
                    errorCode3 = ErrorCode.PROTOCOL_ERROR;
                    Http2Connection http2Connection = this.f12341f;
                    http2Connection.t0(errorCode3, errorCode3, e10);
                    errorCode = http2Connection;
                    this = this.f12340e;
                    ae.d.l(this);
                }
                this = this.f12340e;
                ae.d.l(this);
            } catch (Throwable th2) {
                th = th2;
                this.f12341f.t0(errorCode, errorCode3, e10);
                ae.d.l(this.f12340e);
                throw th;
            }
        }
    }

    /* compiled from: TaskQueue.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$e */
    /* loaded from: classes2.dex */
    public static final class e extends de.a {

        /* renamed from: e */
        final /* synthetic */ String f12360e;

        /* renamed from: f */
        final /* synthetic */ boolean f12361f;

        /* renamed from: g */
        final /* synthetic */ Http2Connection f12362g;

        /* renamed from: h */
        final /* synthetic */ int f12363h;

        /* renamed from: i */
        final /* synthetic */ me.d f12364i;

        /* renamed from: j */
        final /* synthetic */ int f12365j;

        /* renamed from: k */
        final /* synthetic */ boolean f12366k;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public e(String str, boolean z10, Http2Connection http2Connection, int i10, me.d dVar, int i11, boolean z11) {
            super(str, z10);
            this.f12360e = str;
            this.f12361f = z10;
            this.f12362g = http2Connection;
            this.f12363h = i10;
            this.f12364i = dVar;
            this.f12365j = i11;
            this.f12366k = z11;
        }

        @Override // de.a
        public long f() {
            try {
                boolean d10 = this.f12362g.f12318p.d(this.f12363h, this.f12364i, this.f12365j, this.f12366k);
                if (d10) {
                    this.f12362g.getD().X(this.f12363h, ErrorCode.CANCEL);
                }
                if (!d10 && !this.f12366k) {
                    return -1L;
                }
                synchronized (this.f12362g) {
                    this.f12362g.F.remove(Integer.valueOf(this.f12363h));
                }
                return -1L;
            } catch (IOException unused) {
                return -1L;
            }
        }
    }

    /* compiled from: TaskQueue.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$f */
    /* loaded from: classes2.dex */
    public static final class f extends de.a {

        /* renamed from: e */
        final /* synthetic */ String f12367e;

        /* renamed from: f */
        final /* synthetic */ boolean f12368f;

        /* renamed from: g */
        final /* synthetic */ Http2Connection f12369g;

        /* renamed from: h */
        final /* synthetic */ int f12370h;

        /* renamed from: i */
        final /* synthetic */ List f12371i;

        /* renamed from: j */
        final /* synthetic */ boolean f12372j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public f(String str, boolean z10, Http2Connection http2Connection, int i10, List list, boolean z11) {
            super(str, z10);
            this.f12367e = str;
            this.f12368f = z10;
            this.f12369g = http2Connection;
            this.f12370h = i10;
            this.f12371i = list;
            this.f12372j = z11;
        }

        @Override // de.a
        public long f() {
            boolean c10 = this.f12369g.f12318p.c(this.f12370h, this.f12371i, this.f12372j);
            if (c10) {
                try {
                    this.f12369g.getD().X(this.f12370h, ErrorCode.CANCEL);
                } catch (IOException unused) {
                    return -1L;
                }
            }
            if (!c10 && !this.f12372j) {
                return -1L;
            }
            synchronized (this.f12369g) {
                this.f12369g.F.remove(Integer.valueOf(this.f12370h));
            }
            return -1L;
        }
    }

    /* compiled from: TaskQueue.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$g */
    /* loaded from: classes2.dex */
    public static final class g extends de.a {

        /* renamed from: e */
        final /* synthetic */ String f12373e;

        /* renamed from: f */
        final /* synthetic */ boolean f12374f;

        /* renamed from: g */
        final /* synthetic */ Http2Connection f12375g;

        /* renamed from: h */
        final /* synthetic */ int f12376h;

        /* renamed from: i */
        final /* synthetic */ List f12377i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public g(String str, boolean z10, Http2Connection http2Connection, int i10, List list) {
            super(str, z10);
            this.f12373e = str;
            this.f12374f = z10;
            this.f12375g = http2Connection;
            this.f12376h = i10;
            this.f12377i = list;
        }

        @Override // de.a
        public long f() {
            if (!this.f12375g.f12318p.b(this.f12376h, this.f12377i)) {
                return -1L;
            }
            try {
                this.f12375g.getD().X(this.f12376h, ErrorCode.CANCEL);
                synchronized (this.f12375g) {
                    this.f12375g.F.remove(Integer.valueOf(this.f12376h));
                }
                return -1L;
            } catch (IOException unused) {
                return -1L;
            }
        }
    }

    /* compiled from: TaskQueue.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$h */
    /* loaded from: classes2.dex */
    public static final class h extends de.a {

        /* renamed from: e */
        final /* synthetic */ String f12378e;

        /* renamed from: f */
        final /* synthetic */ boolean f12379f;

        /* renamed from: g */
        final /* synthetic */ Http2Connection f12380g;

        /* renamed from: h */
        final /* synthetic */ int f12381h;

        /* renamed from: i */
        final /* synthetic */ ErrorCode f12382i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public h(String str, boolean z10, Http2Connection http2Connection, int i10, ErrorCode errorCode) {
            super(str, z10);
            this.f12378e = str;
            this.f12379f = z10;
            this.f12380g = http2Connection;
            this.f12381h = i10;
            this.f12382i = errorCode;
        }

        @Override // de.a
        public long f() {
            this.f12380g.f12318p.a(this.f12381h, this.f12382i);
            synchronized (this.f12380g) {
                this.f12380g.F.remove(Integer.valueOf(this.f12381h));
                Unit unit = Unit.f15173a;
            }
            return -1L;
        }
    }

    /* compiled from: TaskQueue.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$i */
    /* loaded from: classes2.dex */
    public static final class i extends de.a {

        /* renamed from: e */
        final /* synthetic */ String f12383e;

        /* renamed from: f */
        final /* synthetic */ boolean f12384f;

        /* renamed from: g */
        final /* synthetic */ Http2Connection f12385g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public i(String str, boolean z10, Http2Connection http2Connection) {
            super(str, z10);
            this.f12383e = str;
            this.f12384f = z10;
            this.f12385g = http2Connection;
        }

        @Override // de.a
        public long f() {
            this.f12385g.b1(false, 2, 0);
            return -1L;
        }
    }

    /* compiled from: TaskQueue.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004"}, d2 = {"he/f$j", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$j */
    /* loaded from: classes2.dex */
    public static final class j extends de.a {

        /* renamed from: e */
        final /* synthetic */ String f12386e;

        /* renamed from: f */
        final /* synthetic */ Http2Connection f12387f;

        /* renamed from: g */
        final /* synthetic */ long f12388g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public j(String str, Http2Connection http2Connection, long j10) {
            super(str, false, 2, null);
            this.f12386e = str;
            this.f12387f = http2Connection;
            this.f12388g = j10;
        }

        @Override // de.a
        public long f() {
            boolean z10;
            synchronized (this.f12387f) {
                if (this.f12387f.f12320r < this.f12387f.f12319q) {
                    z10 = true;
                } else {
                    this.f12387f.f12319q++;
                    z10 = false;
                }
            }
            if (z10) {
                this.f12387f.u0(null);
                return -1L;
            }
            this.f12387f.b1(false, 1, 0);
            return this.f12388g;
        }
    }

    /* compiled from: TaskQueue.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$k */
    /* loaded from: classes2.dex */
    public static final class k extends de.a {

        /* renamed from: e */
        final /* synthetic */ String f12389e;

        /* renamed from: f */
        final /* synthetic */ boolean f12390f;

        /* renamed from: g */
        final /* synthetic */ Http2Connection f12391g;

        /* renamed from: h */
        final /* synthetic */ int f12392h;

        /* renamed from: i */
        final /* synthetic */ ErrorCode f12393i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public k(String str, boolean z10, Http2Connection http2Connection, int i10, ErrorCode errorCode) {
            super(str, z10);
            this.f12389e = str;
            this.f12390f = z10;
            this.f12391g = http2Connection;
            this.f12392h = i10;
            this.f12393i = errorCode;
        }

        @Override // de.a
        public long f() {
            try {
                this.f12391g.c1(this.f12392h, this.f12393i);
                return -1L;
            } catch (IOException e10) {
                this.f12391g.u0(e10);
                return -1L;
            }
        }
    }

    /* compiled from: TaskQueue.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"de/c", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.f$l */
    /* loaded from: classes2.dex */
    public static final class l extends de.a {

        /* renamed from: e */
        final /* synthetic */ String f12394e;

        /* renamed from: f */
        final /* synthetic */ boolean f12395f;

        /* renamed from: g */
        final /* synthetic */ Http2Connection f12396g;

        /* renamed from: h */
        final /* synthetic */ int f12397h;

        /* renamed from: i */
        final /* synthetic */ long f12398i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public l(String str, boolean z10, Http2Connection http2Connection, int i10, long j10) {
            super(str, z10);
            this.f12394e = str;
            this.f12395f = z10;
            this.f12396g = http2Connection;
            this.f12397h = i10;
            this.f12398i = j10;
        }

        @Override // de.a
        public long f() {
            try {
                this.f12396g.getD().e0(this.f12397h, this.f12398i);
                return -1L;
            } catch (IOException e10) {
                this.f12396g.u0(e10);
                return -1L;
            }
        }
    }

    static {
        Settings settings = new Settings();
        settings.h(7, 65535);
        settings.h(5, 16384);
        H = settings;
    }

    public Http2Connection(a aVar) {
        za.k.e(aVar, "builder");
        boolean f12329a = aVar.getF12329a();
        this.f12307e = f12329a;
        this.f12308f = aVar.getF12335g();
        this.f12309g = new LinkedHashMap();
        String c10 = aVar.c();
        this.f12310h = c10;
        this.f12312j = aVar.getF12329a() ? 3 : 2;
        TaskRunner f12330b = aVar.getF12330b();
        this.f12314l = f12330b;
        de.d i10 = f12330b.i();
        this.f12315m = i10;
        this.f12316n = f12330b.i();
        this.f12317o = f12330b.i();
        this.f12318p = aVar.getF12336h();
        Settings settings = new Settings();
        if (aVar.getF12329a()) {
            settings.h(7, 16777216);
        }
        this.f12325w = settings;
        this.f12326x = H;
        this.B = r2.c();
        this.C = aVar.h();
        this.D = new Http2Writer(aVar.g(), f12329a);
        this.E = new d(this, new Http2Reader(aVar.i(), f12329a));
        this.F = new LinkedHashSet();
        if (aVar.getF12337i() != 0) {
            long nanos = TimeUnit.MILLISECONDS.toNanos(aVar.getF12337i());
            i10.i(new j(za.k.l(c10, " ping"), this, nanos), nanos);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0055 A[Catch: all -> 0x0096, TryCatch #0 {, blocks: (B:6:0x0007, B:8:0x0010, B:9:0x0015, B:11:0x0019, B:13:0x0033, B:15:0x003f, B:19:0x004f, B:21:0x0055, B:22:0x0060, B:37:0x0090, B:38:0x0095), top: B:5:0x0007, outer: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final Http2Stream J0(int associatedStreamId, List<Header> requestHeaders, boolean out) {
        int f12312j;
        Http2Stream http2Stream;
        boolean z10;
        boolean z11 = !out;
        synchronized (this.D) {
            synchronized (this) {
                if (getF12312j() > 1073741823) {
                    V0(ErrorCode.REFUSED_STREAM);
                }
                if (!this.f12313k) {
                    f12312j = getF12312j();
                    T0(getF12312j() + 2);
                    http2Stream = new Http2Stream(f12312j, this, z11, false, null);
                    if (out && getA() < getB() && http2Stream.getF12425e() < http2Stream.getF12426f()) {
                        z10 = false;
                        if (http2Stream.u()) {
                            E0().put(Integer.valueOf(f12312j), http2Stream);
                        }
                        Unit unit = Unit.f15173a;
                    }
                    z10 = true;
                    if (http2Stream.u()) {
                    }
                    Unit unit2 = Unit.f15173a;
                } else {
                    throw new ConnectionShutdownException();
                }
            }
            if (associatedStreamId == 0) {
                getD().L(z11, f12312j, requestHeaders);
            } else if (true ^ getF12307e()) {
                getD().U(associatedStreamId, f12312j, requestHeaders);
            } else {
                throw new IllegalArgumentException("client streams shouldn't have associated stream IDs".toString());
            }
        }
        if (z10) {
            this.D.flush();
        }
        return http2Stream;
    }

    public static /* synthetic */ void X0(Http2Connection http2Connection, boolean z10, TaskRunner taskRunner, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            z10 = true;
        }
        if ((i10 & 2) != 0) {
            taskRunner = TaskRunner.f10944i;
        }
        http2Connection.W0(z10, taskRunner);
    }

    public final void u0(IOException iOException) {
        ErrorCode errorCode = ErrorCode.PROTOCOL_ERROR;
        t0(errorCode, errorCode, iOException);
    }

    /* renamed from: A0, reason: from getter */
    public final Settings getF12325w() {
        return this.f12325w;
    }

    /* renamed from: B0, reason: from getter */
    public final Settings getF12326x() {
        return this.f12326x;
    }

    /* renamed from: C0, reason: from getter */
    public final Socket getC() {
        return this.C;
    }

    public final synchronized Http2Stream D0(int id2) {
        return this.f12309g.get(Integer.valueOf(id2));
    }

    public final Map<Integer, Http2Stream> E0() {
        return this.f12309g;
    }

    /* renamed from: F0, reason: from getter */
    public final long getB() {
        return this.B;
    }

    /* renamed from: G0, reason: from getter */
    public final long getA() {
        return this.A;
    }

    /* renamed from: H0, reason: from getter */
    public final Http2Writer getD() {
        return this.D;
    }

    public final synchronized boolean I0(long nowNs) {
        if (this.f12313k) {
            return false;
        }
        if (this.f12322t < this.f12321s) {
            if (nowNs >= this.f12324v) {
                return false;
            }
        }
        return true;
    }

    public final Http2Stream K0(List<Header> requestHeaders, boolean out) {
        za.k.e(requestHeaders, "requestHeaders");
        return J0(0, requestHeaders, out);
    }

    public final void L0(int streamId, BufferedSource source, int byteCount, boolean inFinished) {
        za.k.e(source, "source");
        me.d dVar = new me.d();
        long j10 = byteCount;
        source.p0(j10);
        source.read(dVar, j10);
        this.f12316n.i(new e(this.f12310h + '[' + streamId + "] onData", true, this, streamId, dVar, byteCount, inFinished), 0L);
    }

    public final void M0(int streamId, List<Header> requestHeaders, boolean inFinished) {
        za.k.e(requestHeaders, "requestHeaders");
        this.f12316n.i(new f(this.f12310h + '[' + streamId + "] onHeaders", true, this, streamId, requestHeaders, inFinished), 0L);
    }

    public final void N0(int streamId, List<Header> requestHeaders) {
        za.k.e(requestHeaders, "requestHeaders");
        synchronized (this) {
            if (this.F.contains(Integer.valueOf(streamId))) {
                d1(streamId, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.F.add(Integer.valueOf(streamId));
            this.f12316n.i(new g(this.f12310h + '[' + streamId + "] onRequest", true, this, streamId, requestHeaders), 0L);
        }
    }

    public final void O0(int streamId, ErrorCode errorCode) {
        za.k.e(errorCode, "errorCode");
        this.f12316n.i(new h(this.f12310h + '[' + streamId + "] onReset", true, this, streamId, errorCode), 0L);
    }

    public final boolean P0(int streamId) {
        return streamId != 0 && (streamId & 1) == 0;
    }

    public final synchronized Http2Stream Q0(int streamId) {
        Http2Stream remove;
        remove = this.f12309g.remove(Integer.valueOf(streamId));
        notifyAll();
        return remove;
    }

    public final void R0() {
        synchronized (this) {
            long j10 = this.f12322t;
            long j11 = this.f12321s;
            if (j10 < j11) {
                return;
            }
            this.f12321s = j11 + 1;
            this.f12324v = System.nanoTime() + 1000000000;
            Unit unit = Unit.f15173a;
            this.f12315m.i(new i(za.k.l(this.f12310h, " ping"), true, this), 0L);
        }
    }

    public final void S0(int i10) {
        this.f12311i = i10;
    }

    public final void T0(int i10) {
        this.f12312j = i10;
    }

    public final void U0(Settings settings) {
        za.k.e(settings, "<set-?>");
        this.f12326x = settings;
    }

    public final void V0(ErrorCode errorCode) {
        za.k.e(errorCode, "statusCode");
        synchronized (this.D) {
            x xVar = new x();
            synchronized (this) {
                if (this.f12313k) {
                    return;
                }
                this.f12313k = true;
                xVar.f20375e = getF12311i();
                Unit unit = Unit.f15173a;
                getD().w(xVar.f20375e, errorCode, ae.d.f237a);
            }
        }
    }

    public final void W0(boolean z10, TaskRunner taskRunner) {
        za.k.e(taskRunner, "taskRunner");
        if (z10) {
            this.D.c();
            this.D.a0(this.f12325w);
            if (this.f12325w.c() != 65535) {
                this.D.e0(0, r6 - 65535);
            }
        }
        taskRunner.i().i(new de.c(this.f12310h, true, this.E), 0L);
    }

    public final synchronized void Y0(long read) {
        long j10 = this.f12327y + read;
        this.f12327y = j10;
        long j11 = j10 - this.f12328z;
        if (j11 >= this.f12325w.c() / 2) {
            e1(0, j11);
            this.f12328z += j11;
        }
    }

    public final void Z0(int i10, boolean z10, me.d dVar, long j10) {
        int min;
        long j11;
        if (j10 == 0) {
            this.D.m(z10, i10, dVar, 0);
            return;
        }
        while (j10 > 0) {
            synchronized (this) {
                while (getA() >= getB()) {
                    try {
                        if (E0().containsKey(Integer.valueOf(i10))) {
                            wait();
                        } else {
                            throw new IOException("stream closed");
                        }
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                        throw new InterruptedIOException();
                    }
                }
                min = Math.min((int) Math.min(j10, getB() - getA()), getD().getF12453h());
                j11 = min;
                this.A = getA() + j11;
                Unit unit = Unit.f15173a;
            }
            j10 -= j11;
            this.D.m(z10 && j10 == 0, i10, dVar, min);
        }
    }

    public final void a1(int streamId, boolean outFinished, List<Header> alternating) {
        za.k.e(alternating, "alternating");
        this.D.L(outFinished, streamId, alternating);
    }

    public final void b1(boolean z10, int i10, int i11) {
        try {
            this.D.S(z10, i10, i11);
        } catch (IOException e10) {
            u0(e10);
        }
    }

    public final void c1(int streamId, ErrorCode statusCode) {
        za.k.e(statusCode, "statusCode");
        this.D.X(streamId, statusCode);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        t0(ErrorCode.NO_ERROR, ErrorCode.CANCEL, null);
    }

    public final void d1(int streamId, ErrorCode errorCode) {
        za.k.e(errorCode, "errorCode");
        this.f12315m.i(new k(this.f12310h + '[' + streamId + "] writeSynReset", true, this, streamId, errorCode), 0L);
    }

    public final void e1(int streamId, long unacknowledgedBytesRead) {
        this.f12315m.i(new l(this.f12310h + '[' + streamId + "] windowUpdate", true, this, streamId, unacknowledgedBytesRead), 0L);
    }

    public final void flush() {
        this.D.flush();
    }

    public final void t0(ErrorCode connectionCode, ErrorCode streamCode, IOException cause) {
        int i10;
        za.k.e(connectionCode, "connectionCode");
        za.k.e(streamCode, "streamCode");
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        try {
            V0(connectionCode);
        } catch (IOException unused) {
        }
        Object[] objArr = null;
        synchronized (this) {
            if (!E0().isEmpty()) {
                objArr = E0().values().toArray(new Http2Stream[0]);
                if (objArr != null) {
                    E0().clear();
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                }
            }
            Unit unit = Unit.f15173a;
        }
        Http2Stream[] http2StreamArr = (Http2Stream[]) objArr;
        if (http2StreamArr != null) {
            for (Http2Stream http2Stream : http2StreamArr) {
                try {
                    http2Stream.d(streamCode, cause);
                } catch (IOException unused2) {
                }
            }
        }
        try {
            getD().close();
        } catch (IOException unused3) {
        }
        try {
            getC().close();
        } catch (IOException unused4) {
        }
        this.f12315m.o();
        this.f12316n.o();
        this.f12317o.o();
    }

    /* renamed from: v0, reason: from getter */
    public final boolean getF12307e() {
        return this.f12307e;
    }

    /* renamed from: w0, reason: from getter */
    public final String getF12310h() {
        return this.f12310h;
    }

    /* renamed from: x0, reason: from getter */
    public final int getF12311i() {
        return this.f12311i;
    }

    /* renamed from: y0, reason: from getter */
    public final c getF12308f() {
        return this.f12308f;
    }

    /* renamed from: z0, reason: from getter */
    public final int getF12312j() {
        return this.f12312j;
    }
}
