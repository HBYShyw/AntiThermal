package he;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import kotlin.Metadata;
import ma.Unit;
import me.AsyncTimeout;
import me.BufferedSource;
import me.Sink;
import me.Source;
import me.Timeout;
import za.DefaultConstructorMarker;
import zd.Headers;

/* compiled from: Http2Stream.kt */
@Metadata(bv = {}, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0019\u0018\u00002\u00020\u0001:\u0004 \u001c!\u0011B3\b\u0000\u0012\u0006\u0010#\u001a\u00020\u0015\u0012\u0006\u0010(\u001a\u00020'\u0012\u0006\u0010[\u001a\u00020\u0006\u0012\u0006\u0010\u0019\u001a\u00020\u0006\u0012\b\u0010\u0018\u001a\u0004\u0018\u00010\b¢\u0006\u0004\b\\\u0010]J\u001a\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0002J\u0006\u0010\t\u001a\u00020\bJ\u0006\u0010\u000b\u001a\u00020\nJ\u0006\u0010\f\u001a\u00020\nJ\u0006\u0010\u000e\u001a\u00020\rJ\u0018\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0003\u001a\u00020\u0002J\u0016\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0015J\u0016\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\u0006J\u000e\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u0003\u001a\u00020\u0002J\u000f\u0010\u001c\u001a\u00020\u0010H\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\u000e\u0010 \u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u001eJ\u000f\u0010!\u001a\u00020\u0010H\u0000¢\u0006\u0004\b!\u0010\u001dJ\u000f\u0010\"\u001a\u00020\u0010H\u0000¢\u0006\u0004\b\"\u0010\u001dR\u0017\u0010#\u001a\u00020\u00158\u0006¢\u0006\f\n\u0004\b#\u0010$\u001a\u0004\b%\u0010&R\u0017\u0010(\u001a\u00020'8\u0006¢\u0006\f\n\u0004\b(\u0010)\u001a\u0004\b*\u0010+R*\u0010-\u001a\u00020\u001e2\u0006\u0010,\u001a\u00020\u001e8\u0006@@X\u0086\u000e¢\u0006\u0012\n\u0004\b-\u0010.\u001a\u0004\b/\u00100\"\u0004\b1\u00102R*\u00103\u001a\u00020\u001e2\u0006\u0010,\u001a\u00020\u001e8\u0006@@X\u0086\u000e¢\u0006\u0012\n\u0004\b3\u0010.\u001a\u0004\b4\u00100\"\u0004\b5\u00102R*\u00106\u001a\u00020\u001e2\u0006\u0010,\u001a\u00020\u001e8\u0006@@X\u0086\u000e¢\u0006\u0012\n\u0004\b6\u0010.\u001a\u0004\b7\u00100\"\u0004\b8\u00102R*\u00109\u001a\u00020\u001e2\u0006\u0010,\u001a\u00020\u001e8\u0006@@X\u0086\u000e¢\u0006\u0012\n\u0004\b9\u0010.\u001a\u0004\b:\u00100\"\u0004\b;\u00102R\u001e\u0010\u0014\u001a\u00060<R\u00020\u00008\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0014\u0010=\u001a\u0004\b>\u0010?R\u001e\u0010A\u001a\u00060@R\u00020\u00008\u0000X\u0080\u0004¢\u0006\f\n\u0004\bA\u0010B\u001a\u0004\bC\u0010DR\u001e\u0010F\u001a\u00060ER\u00020\u00008\u0000X\u0080\u0004¢\u0006\f\n\u0004\bF\u0010G\u001a\u0004\bH\u0010IR\u001e\u0010J\u001a\u00060ER\u00020\u00008\u0000X\u0080\u0004¢\u0006\f\n\u0004\bJ\u0010G\u001a\u0004\bK\u0010IR$\u0010\u0003\u001a\u0004\u0018\u00010\u00028@@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010L\u001a\u0004\bM\u0010N\"\u0004\bO\u0010PR$\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0005\u0010Q\u001a\u0004\bR\u0010S\"\u0004\bT\u0010UR\u0011\u0010X\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\bV\u0010WR\u0011\u0010Z\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\bY\u0010W¨\u0006^"}, d2 = {"Lhe/i;", "", "Lhe/b;", "errorCode", "Ljava/io/IOException;", "errorException", "", "e", "Lzd/t;", "E", "Lme/b0;", "v", "G", "Lme/y;", "n", "rstStatusCode", "Lma/f0;", "d", "f", "Lme/f;", "source", "", "length", "w", "headers", "inFinished", "x", "y", "b", "()V", "", "delta", "a", "c", "F", "id", "I", "j", "()I", "Lhe/f;", "connection", "Lhe/f;", "g", "()Lhe/f;", "<set-?>", "readBytesTotal", "J", "l", "()J", "C", "(J)V", "readBytesAcknowledged", "k", "B", "writeBytesTotal", "r", "D", "writeBytesMaximum", "q", "setWriteBytesMaximum$okhttp", "Lhe/i$c;", "Lhe/i$c;", "p", "()Lhe/i$c;", "Lhe/i$b;", "sink", "Lhe/i$b;", "o", "()Lhe/i$b;", "Lhe/i$d;", "readTimeout", "Lhe/i$d;", "m", "()Lhe/i$d;", "writeTimeout", "s", "Lhe/b;", "h", "()Lhe/b;", "z", "(Lhe/b;)V", "Ljava/io/IOException;", "i", "()Ljava/io/IOException;", "A", "(Ljava/io/IOException;)V", "u", "()Z", "isOpen", "t", "isLocallyInitiated", "outFinished", "<init>", "(ILhe/f;ZZLzd/t;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class Http2Stream {

    /* renamed from: o, reason: collision with root package name */
    public static final a f12420o = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final int f12421a;

    /* renamed from: b, reason: collision with root package name */
    private final Http2Connection f12422b;

    /* renamed from: c, reason: collision with root package name */
    private long f12423c;

    /* renamed from: d, reason: collision with root package name */
    private long f12424d;

    /* renamed from: e, reason: collision with root package name */
    private long f12425e;

    /* renamed from: f, reason: collision with root package name */
    private long f12426f;

    /* renamed from: g, reason: collision with root package name */
    private final ArrayDeque<Headers> f12427g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f12428h;

    /* renamed from: i, reason: collision with root package name */
    private final c f12429i;

    /* renamed from: j, reason: collision with root package name */
    private final b f12430j;

    /* renamed from: k, reason: collision with root package name */
    private final d f12431k;

    /* renamed from: l, reason: collision with root package name */
    private final d f12432l;

    /* renamed from: m, reason: collision with root package name */
    private ErrorCode f12433m;

    /* renamed from: n, reason: collision with root package name */
    private IOException f12434n;

    /* compiled from: Http2Stream.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\t\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0003\u001a\u00020\u00028\u0000X\u0080T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lhe/i$a;", "", "", "EMIT_BUFFER_SIZE", "J", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.i$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: Http2Stream.kt */
    @Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0011\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bH\u0016J\b\u0010\u000b\u001a\u00020\u0004H\u0016J\b\u0010\r\u001a\u00020\fH\u0016J\b\u0010\u000e\u001a\u00020\u0004H\u0016R\"\u0010\u000f\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\"\u0010\u0015\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010\u0010\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0014¨\u0006\u001a"}, d2 = {"Lhe/i$b;", "Lme/y;", "", "outFinishedOnLastFrame", "Lma/f0;", "b", "Lme/d;", "source", "", "byteCount", "write", "flush", "Lme/b0;", "timeout", "close", "finished", "Z", "m", "()Z", "setFinished", "(Z)V", "closed", "c", "u", "<init>", "(Lhe/i;Z)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.i$b */
    /* loaded from: classes2.dex */
    public final class b implements Sink {

        /* renamed from: e, reason: collision with root package name */
        private boolean f12435e;

        /* renamed from: f, reason: collision with root package name */
        private final me.d f12436f;

        /* renamed from: g, reason: collision with root package name */
        private Headers f12437g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f12438h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ Http2Stream f12439i;

        public b(Http2Stream http2Stream, boolean z10) {
            za.k.e(http2Stream, "this$0");
            this.f12439i = http2Stream;
            this.f12435e = z10;
            this.f12436f = new me.d();
        }

        private final void b(boolean z10) {
            long min;
            boolean z11;
            Http2Stream http2Stream = this.f12439i;
            synchronized (http2Stream) {
                http2Stream.getF12432l().v();
                while (http2Stream.getF12425e() >= http2Stream.getF12426f() && !getF12435e() && !getF12438h() && http2Stream.h() == null) {
                    try {
                        http2Stream.F();
                    } finally {
                        http2Stream.getF12432l().C();
                    }
                }
                http2Stream.getF12432l().C();
                http2Stream.c();
                min = Math.min(http2Stream.getF12426f() - http2Stream.getF12425e(), this.f12436f.v0());
                http2Stream.D(http2Stream.getF12425e() + min);
                z11 = z10 && min == this.f12436f.v0();
                Unit unit = Unit.f15173a;
            }
            this.f12439i.getF12432l().v();
            try {
                this.f12439i.getF12422b().Z0(this.f12439i.getF12421a(), z11, this.f12436f, min);
            } finally {
                http2Stream = this.f12439i;
            }
        }

        /* renamed from: c, reason: from getter */
        public final boolean getF12438h() {
            return this.f12438h;
        }

        @Override // me.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            Http2Stream http2Stream = this.f12439i;
            if (ae.d.f244h && Thread.holdsLock(http2Stream)) {
                throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + http2Stream);
            }
            Http2Stream http2Stream2 = this.f12439i;
            synchronized (http2Stream2) {
                if (getF12438h()) {
                    return;
                }
                boolean z10 = http2Stream2.h() == null;
                Unit unit = Unit.f15173a;
                if (!this.f12439i.getF12430j().f12435e) {
                    boolean z11 = this.f12436f.v0() > 0;
                    if (this.f12437g != null) {
                        while (this.f12436f.v0() > 0) {
                            b(false);
                        }
                        Http2Connection f12422b = this.f12439i.getF12422b();
                        int f12421a = this.f12439i.getF12421a();
                        Headers headers = this.f12437g;
                        za.k.b(headers);
                        f12422b.a1(f12421a, z10, ae.d.N(headers));
                    } else if (z11) {
                        while (this.f12436f.v0() > 0) {
                            b(true);
                        }
                    } else if (z10) {
                        this.f12439i.getF12422b().Z0(this.f12439i.getF12421a(), true, null, 0L);
                    }
                }
                synchronized (this.f12439i) {
                    u(true);
                    Unit unit2 = Unit.f15173a;
                }
                this.f12439i.getF12422b().flush();
                this.f12439i.b();
            }
        }

        @Override // me.Sink, java.io.Flushable
        public void flush() {
            Http2Stream http2Stream = this.f12439i;
            if (ae.d.f244h && Thread.holdsLock(http2Stream)) {
                throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + http2Stream);
            }
            Http2Stream http2Stream2 = this.f12439i;
            synchronized (http2Stream2) {
                http2Stream2.c();
                Unit unit = Unit.f15173a;
            }
            while (this.f12436f.v0() > 0) {
                b(false);
                this.f12439i.getF12422b().flush();
            }
        }

        /* renamed from: m, reason: from getter */
        public final boolean getF12435e() {
            return this.f12435e;
        }

        @Override // me.Sink
        public Timeout timeout() {
            return this.f12439i.getF12432l();
        }

        public final void u(boolean z10) {
            this.f12438h = z10;
        }

        @Override // me.Sink
        public void write(me.d dVar, long j10) {
            za.k.e(dVar, "source");
            Http2Stream http2Stream = this.f12439i;
            if (ae.d.f244h && Thread.holdsLock(http2Stream)) {
                throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + http2Stream);
            }
            this.f12436f.write(dVar, j10);
            while (this.f12436f.v0() >= 16384) {
                b(false);
            }
        }
    }

    /* compiled from: Http2Stream.kt */
    @Metadata(bv = {}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\r\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0019\b\u0000\u0012\u0006\u0010'\u001a\u00020\u0002\u0012\u0006\u0010\u0011\u001a\u00020\u0010¢\u0006\u0004\b(\u0010)J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0002H\u0016J\u001f\u0010\u000b\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u000b\u0010\fJ\b\u0010\u000e\u001a\u00020\rH\u0016J\b\u0010\u000f\u001a\u00020\u0004H\u0016R\"\u0010\u0011\u001a\u00020\u00108\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0017\u001a\u00020\u00068\u0006¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR\u0017\u0010\u001b\u001a\u00020\u00068\u0006¢\u0006\f\n\u0004\b\u001b\u0010\u0018\u001a\u0004\b\u001c\u0010\u001aR$\u0010\u001e\u001a\u0004\u0018\u00010\u001d8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001e\u0010\u001f\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\"\u0010$\u001a\u00020\u00108\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b$\u0010\u0012\u001a\u0004\b%\u0010\u0014\"\u0004\b&\u0010\u0016¨\u0006*"}, d2 = {"Lhe/i$c;", "Lme/a0;", "", "read", "Lma/f0;", "S", "Lme/d;", "sink", "byteCount", "Lme/f;", "source", "v", "(Lme/f;J)V", "Lme/b0;", "timeout", "close", "", "finished", "Z", "c", "()Z", "L", "(Z)V", "receiveBuffer", "Lme/d;", "u", "()Lme/d;", "readBuffer", "m", "Lzd/t;", "trailers", "Lzd/t;", "getTrailers", "()Lzd/t;", "N", "(Lzd/t;)V", "closed", "b", "w", "maxByteCount", "<init>", "(Lhe/i;JZ)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.i$c */
    /* loaded from: classes2.dex */
    public final class c implements Source {

        /* renamed from: e, reason: collision with root package name */
        private final long f12440e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f12441f;

        /* renamed from: g, reason: collision with root package name */
        private final me.d f12442g;

        /* renamed from: h, reason: collision with root package name */
        private final me.d f12443h;

        /* renamed from: i, reason: collision with root package name */
        private Headers f12444i;

        /* renamed from: j, reason: collision with root package name */
        private boolean f12445j;

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ Http2Stream f12446k;

        public c(Http2Stream http2Stream, long j10, boolean z10) {
            za.k.e(http2Stream, "this$0");
            this.f12446k = http2Stream;
            this.f12440e = j10;
            this.f12441f = z10;
            this.f12442g = new me.d();
            this.f12443h = new me.d();
        }

        private final void S(long j10) {
            Http2Stream http2Stream = this.f12446k;
            if (ae.d.f244h && Thread.holdsLock(http2Stream)) {
                throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + http2Stream);
            }
            this.f12446k.getF12422b().Y0(j10);
        }

        public final void L(boolean z10) {
            this.f12441f = z10;
        }

        public final void N(Headers headers) {
            this.f12444i = headers;
        }

        /* renamed from: b, reason: from getter */
        public final boolean getF12445j() {
            return this.f12445j;
        }

        /* renamed from: c, reason: from getter */
        public final boolean getF12441f() {
            return this.f12441f;
        }

        @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            long v02;
            Http2Stream http2Stream = this.f12446k;
            synchronized (http2Stream) {
                w(true);
                v02 = getF12443h().v0();
                getF12443h().b();
                http2Stream.notifyAll();
                Unit unit = Unit.f15173a;
            }
            if (v02 > 0) {
                S(v02);
            }
            this.f12446k.b();
        }

        /* renamed from: m, reason: from getter */
        public final me.d getF12443h() {
            return this.f12443h;
        }

        /* JADX WARN: Code restructure failed: missing block: B:46:0x00ce, code lost:
        
            throw new java.io.IOException("stream closed");
         */
        @Override // me.Source
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public long read(me.d sink, long byteCount) {
            long j10;
            boolean z10;
            za.k.e(sink, "sink");
            long j11 = 0;
            if (!(byteCount >= 0)) {
                throw new IllegalArgumentException(za.k.l("byteCount < 0: ", Long.valueOf(byteCount)).toString());
            }
            while (true) {
                IOException iOException = null;
                Http2Stream http2Stream = this.f12446k;
                synchronized (http2Stream) {
                    http2Stream.getF12431k().v();
                    try {
                        if (http2Stream.h() != null && (iOException = http2Stream.getF12434n()) == null) {
                            ErrorCode h10 = http2Stream.h();
                            za.k.b(h10);
                            iOException = new StreamResetException(h10);
                        }
                        if (getF12445j()) {
                            break;
                        }
                        if (getF12443h().v0() > j11) {
                            j10 = getF12443h().read(sink, Math.min(byteCount, getF12443h().v0()));
                            http2Stream.C(http2Stream.getF12423c() + j10);
                            long f12423c = http2Stream.getF12423c() - http2Stream.getF12424d();
                            if (iOException == null && f12423c >= http2Stream.getF12422b().getF12325w().c() / 2) {
                                http2Stream.getF12422b().e1(http2Stream.getF12421a(), f12423c);
                                http2Stream.B(http2Stream.getF12423c());
                            }
                        } else if (getF12441f() || iOException != null) {
                            j10 = -1;
                        } else {
                            http2Stream.F();
                            j10 = -1;
                            z10 = true;
                            http2Stream.getF12431k().C();
                            Unit unit = Unit.f15173a;
                        }
                        z10 = false;
                        http2Stream.getF12431k().C();
                        Unit unit2 = Unit.f15173a;
                    } catch (Throwable th) {
                        http2Stream.getF12431k().C();
                        throw th;
                    }
                }
                if (!z10) {
                    if (j10 != -1) {
                        S(j10);
                        return j10;
                    }
                    if (iOException == null) {
                        return -1L;
                    }
                    throw iOException;
                }
                j11 = 0;
            }
        }

        @Override // me.Source
        public Timeout timeout() {
            return this.f12446k.getF12431k();
        }

        /* renamed from: u, reason: from getter */
        public final me.d getF12442g() {
            return this.f12442g;
        }

        public final void v(BufferedSource source, long byteCount) {
            boolean f12441f;
            boolean z10;
            boolean z11;
            long j10;
            za.k.e(source, "source");
            Http2Stream http2Stream = this.f12446k;
            if (ae.d.f244h && Thread.holdsLock(http2Stream)) {
                throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + http2Stream);
            }
            while (byteCount > 0) {
                synchronized (this.f12446k) {
                    f12441f = getF12441f();
                    z10 = true;
                    z11 = getF12443h().v0() + byteCount > this.f12440e;
                    Unit unit = Unit.f15173a;
                }
                if (z11) {
                    source.V(byteCount);
                    this.f12446k.f(ErrorCode.FLOW_CONTROL_ERROR);
                    return;
                }
                if (f12441f) {
                    source.V(byteCount);
                    return;
                }
                long read = source.read(this.f12442g, byteCount);
                if (read != -1) {
                    byteCount -= read;
                    Http2Stream http2Stream2 = this.f12446k;
                    synchronized (http2Stream2) {
                        if (getF12445j()) {
                            j10 = getF12442g().v0();
                            getF12442g().b();
                        } else {
                            if (getF12443h().v0() != 0) {
                                z10 = false;
                            }
                            getF12443h().C0(getF12442g());
                            if (z10) {
                                http2Stream2.notifyAll();
                            }
                            j10 = 0;
                        }
                    }
                    if (j10 > 0) {
                        S(j10);
                    }
                } else {
                    throw new EOFException();
                }
            }
        }

        public final void w(boolean z10) {
            this.f12445j = z10;
        }
    }

    /* compiled from: Http2Stream.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0080\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\b\u0010\tJ\b\u0010\u0003\u001a\u00020\u0002H\u0014J\u0012\u0010\u0006\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0014J\u0006\u0010\u0007\u001a\u00020\u0002¨\u0006\n"}, d2 = {"Lhe/i$d;", "Lme/c;", "Lma/f0;", "B", "Ljava/io/IOException;", "cause", "x", "C", "<init>", "(Lhe/i;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.i$d */
    /* loaded from: classes2.dex */
    public final class d extends AsyncTimeout {

        /* renamed from: o, reason: collision with root package name */
        final /* synthetic */ Http2Stream f12447o;

        public d(Http2Stream http2Stream) {
            za.k.e(http2Stream, "this$0");
            this.f12447o = http2Stream;
        }

        @Override // me.AsyncTimeout
        protected void B() {
            this.f12447o.f(ErrorCode.CANCEL);
            this.f12447o.getF12422b().R0();
        }

        public final void C() {
            if (w()) {
                throw x(null);
            }
        }

        @Override // me.AsyncTimeout
        protected IOException x(IOException cause) {
            SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
            if (cause != null) {
                socketTimeoutException.initCause(cause);
            }
            return socketTimeoutException;
        }
    }

    public Http2Stream(int i10, Http2Connection http2Connection, boolean z10, boolean z11, Headers headers) {
        za.k.e(http2Connection, "connection");
        this.f12421a = i10;
        this.f12422b = http2Connection;
        this.f12426f = http2Connection.getF12326x().c();
        ArrayDeque<Headers> arrayDeque = new ArrayDeque<>();
        this.f12427g = arrayDeque;
        this.f12429i = new c(this, http2Connection.getF12325w().c(), z11);
        this.f12430j = new b(this, z10);
        this.f12431k = new d(this);
        this.f12432l = new d(this);
        if (headers != null) {
            if (!t()) {
                arrayDeque.add(headers);
                return;
            }
            throw new IllegalStateException("locally-initiated streams shouldn't have headers yet".toString());
        }
        if (!t()) {
            throw new IllegalStateException("remotely-initiated streams should have headers".toString());
        }
    }

    private final boolean e(ErrorCode errorCode, IOException errorException) {
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        synchronized (this) {
            if (h() != null) {
                return false;
            }
            if (getF12429i().getF12441f() && getF12430j().getF12435e()) {
                return false;
            }
            z(errorCode);
            A(errorException);
            notifyAll();
            Unit unit = Unit.f15173a;
            this.f12422b.Q0(this.f12421a);
            return true;
        }
    }

    public final void A(IOException iOException) {
        this.f12434n = iOException;
    }

    public final void B(long j10) {
        this.f12424d = j10;
    }

    public final void C(long j10) {
        this.f12423c = j10;
    }

    public final void D(long j10) {
        this.f12425e = j10;
    }

    public final synchronized Headers E() {
        Headers removeFirst;
        this.f12431k.v();
        while (this.f12427g.isEmpty() && this.f12433m == null) {
            try {
                F();
            } catch (Throwable th) {
                this.f12431k.C();
                throw th;
            }
        }
        this.f12431k.C();
        if (!this.f12427g.isEmpty()) {
            removeFirst = this.f12427g.removeFirst();
            za.k.d(removeFirst, "headersQueue.removeFirst()");
        } else {
            IOException iOException = this.f12434n;
            if (iOException != null) {
                throw iOException;
            }
            ErrorCode errorCode = this.f12433m;
            za.k.b(errorCode);
            throw new StreamResetException(errorCode);
        }
        return removeFirst;
    }

    public final void F() {
        try {
            wait();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
        }
    }

    public final Timeout G() {
        return this.f12432l;
    }

    public final void a(long j10) {
        this.f12426f += j10;
        if (j10 > 0) {
            notifyAll();
        }
    }

    public final void b() {
        boolean z10;
        boolean u7;
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        synchronized (this) {
            z10 = !getF12429i().getF12441f() && getF12429i().getF12445j() && (getF12430j().getF12435e() || getF12430j().getF12438h());
            u7 = u();
            Unit unit = Unit.f15173a;
        }
        if (z10) {
            d(ErrorCode.CANCEL, null);
        } else {
            if (u7) {
                return;
            }
            this.f12422b.Q0(this.f12421a);
        }
    }

    public final void c() {
        if (!this.f12430j.getF12438h()) {
            if (!this.f12430j.getF12435e()) {
                if (this.f12433m != null) {
                    IOException iOException = this.f12434n;
                    if (iOException != null) {
                        throw iOException;
                    }
                    ErrorCode errorCode = this.f12433m;
                    za.k.b(errorCode);
                    throw new StreamResetException(errorCode);
                }
                return;
            }
            throw new IOException("stream finished");
        }
        throw new IOException("stream closed");
    }

    public final void d(ErrorCode errorCode, IOException iOException) {
        za.k.e(errorCode, "rstStatusCode");
        if (e(errorCode, iOException)) {
            this.f12422b.c1(this.f12421a, errorCode);
        }
    }

    public final void f(ErrorCode errorCode) {
        za.k.e(errorCode, "errorCode");
        if (e(errorCode, null)) {
            this.f12422b.d1(this.f12421a, errorCode);
        }
    }

    /* renamed from: g, reason: from getter */
    public final Http2Connection getF12422b() {
        return this.f12422b;
    }

    public final synchronized ErrorCode h() {
        return this.f12433m;
    }

    /* renamed from: i, reason: from getter */
    public final IOException getF12434n() {
        return this.f12434n;
    }

    /* renamed from: j, reason: from getter */
    public final int getF12421a() {
        return this.f12421a;
    }

    /* renamed from: k, reason: from getter */
    public final long getF12424d() {
        return this.f12424d;
    }

    /* renamed from: l, reason: from getter */
    public final long getF12423c() {
        return this.f12423c;
    }

    /* renamed from: m, reason: from getter */
    public final d getF12431k() {
        return this.f12431k;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0011 A[Catch: all -> 0x0023, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0005, B:10:0x0011, B:15:0x0017, B:16:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0017 A[Catch: all -> 0x0023, TRY_ENTER, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0005, B:10:0x0011, B:15:0x0017, B:16:0x0022), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Sink n() {
        boolean z10;
        synchronized (this) {
            if (!this.f12428h && !t()) {
                z10 = false;
                if (!z10) {
                    Unit unit = Unit.f15173a;
                } else {
                    throw new IllegalStateException("reply before requesting the sink".toString());
                }
            }
            z10 = true;
            if (!z10) {
            }
        }
        return this.f12430j;
    }

    /* renamed from: o, reason: from getter */
    public final b getF12430j() {
        return this.f12430j;
    }

    /* renamed from: p, reason: from getter */
    public final c getF12429i() {
        return this.f12429i;
    }

    /* renamed from: q, reason: from getter */
    public final long getF12426f() {
        return this.f12426f;
    }

    /* renamed from: r, reason: from getter */
    public final long getF12425e() {
        return this.f12425e;
    }

    /* renamed from: s, reason: from getter */
    public final d getF12432l() {
        return this.f12432l;
    }

    public final boolean t() {
        return this.f12422b.getF12307e() == ((this.f12421a & 1) == 1);
    }

    public final synchronized boolean u() {
        if (this.f12433m != null) {
            return false;
        }
        if ((this.f12429i.getF12441f() || this.f12429i.getF12445j()) && (this.f12430j.getF12435e() || this.f12430j.getF12438h())) {
            if (this.f12428h) {
                return false;
            }
        }
        return true;
    }

    public final Timeout v() {
        return this.f12431k;
    }

    public final void w(BufferedSource bufferedSource, int i10) {
        za.k.e(bufferedSource, "source");
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        this.f12429i.v(bufferedSource, i10);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0051 A[Catch: all -> 0x006c, TryCatch #0 {, blocks: (B:10:0x0038, B:14:0x0040, B:16:0x0051, B:17:0x0058, B:24:0x0048), top: B:9:0x0038 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void x(Headers headers, boolean z10) {
        boolean u7;
        za.k.e(headers, "headers");
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        synchronized (this) {
            if (this.f12428h && z10) {
                getF12429i().N(headers);
                if (z10) {
                    getF12429i().L(true);
                }
                u7 = u();
                notifyAll();
                Unit unit = Unit.f15173a;
            }
            this.f12428h = true;
            this.f12427g.add(headers);
            if (z10) {
            }
            u7 = u();
            notifyAll();
            Unit unit2 = Unit.f15173a;
        }
        if (u7) {
            return;
        }
        this.f12422b.Q0(this.f12421a);
    }

    public final synchronized void y(ErrorCode errorCode) {
        za.k.e(errorCode, "errorCode");
        if (this.f12433m == null) {
            this.f12433m = errorCode;
            notifyAll();
        }
    }

    public final void z(ErrorCode errorCode) {
        this.f12433m = errorCode;
    }
}
