package he;

import fb.PrimitiveRanges;
import fb.Progressions;
import fb._Ranges;
import he.Hpack;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import me.BufferedSource;
import me.Source;
import me.Timeout;
import za.DefaultConstructorMarker;

/* compiled from: Http2Reader.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0003!\"\u001aB\u0017\u0012\u0006\u0010\u001d\u001a\u00020\u001c\u0012\u0006\u0010\u001e\u001a\u00020\u0018¢\u0006\u0004\b\u001f\u0010 J(\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J.\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J(\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J(\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J\u0018\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J(\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J(\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J(\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J(\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J(\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J(\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0002J\u000e\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0002J\u0016\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0003\u001a\u00020\u0002J\b\u0010\u001b\u001a\u00020\bH\u0016¨\u0006#"}, d2 = {"Lhe/h;", "Ljava/io/Closeable;", "Lhe/h$c;", "handler", "", "length", "flags", "streamId", "Lma/f0;", "L", "padding", "", "Lhe/c;", "w", "u", "U", "S", "a0", "e0", "X", "N", "v", "h0", "m", "", "requireSettings", "c", "close", "Lme/f;", "source", "client", "<init>", "(Lme/f;Z)V", "a", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class Http2Reader implements Closeable {

    /* renamed from: i, reason: collision with root package name */
    public static final a f12408i = new a(null);

    /* renamed from: j, reason: collision with root package name */
    private static final Logger f12409j;

    /* renamed from: e, reason: collision with root package name */
    private final BufferedSource f12410e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f12411f;

    /* renamed from: g, reason: collision with root package name */
    private final b f12412g;

    /* renamed from: h, reason: collision with root package name */
    private final Hpack.a f12413h;

    /* compiled from: Http2Reader.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ\u001e\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0002R\u0017\u0010\b\u001a\u00020\u00078\u0006¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000b¨\u0006\u000e"}, d2 = {"Lhe/h$a;", "", "", "length", "flags", "padding", "b", "Ljava/util/logging/Logger;", "logger", "Ljava/util/logging/Logger;", "a", "()Ljava/util/logging/Logger;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.h$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Logger a() {
            return Http2Reader.f12409j;
        }

        public final int b(int length, int flags, int padding) {
            if ((flags & 8) != 0) {
                length--;
            }
            if (padding <= length) {
                return length - padding;
            }
            throw new IOException("PROTOCOL_ERROR padding " + padding + " > remaining length " + length);
        }
    }

    /* compiled from: Http2Reader.kt */
    @Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010 \u001a\u00020\u001f¢\u0006\u0004\b!\u0010\"J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\b\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\n\u001a\u00020\tH\u0016J\b\u0010\u000b\u001a\u00020\u0002H\u0016R\"\u0010\r\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\"\u0010\u0013\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0013\u0010\u000e\u001a\u0004\b\u0014\u0010\u0010\"\u0004\b\u0015\u0010\u0012R\"\u0010\u0016\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0016\u0010\u000e\u001a\u0004\b\u0017\u0010\u0010\"\u0004\b\u0018\u0010\u0012R\"\u0010\u0019\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0019\u0010\u000e\u001a\u0004\b\u001a\u0010\u0010\"\u0004\b\u001b\u0010\u0012R\"\u0010\u001c\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001c\u0010\u000e\u001a\u0004\b\u001d\u0010\u0010\"\u0004\b\u001e\u0010\u0012¨\u0006#"}, d2 = {"Lhe/h$b;", "Lme/a0;", "Lma/f0;", "c", "Lme/d;", "sink", "", "byteCount", "read", "Lme/b0;", "timeout", "close", "", "length", "I", "getLength", "()I", "v", "(I)V", "flags", "getFlags", "m", "streamId", "getStreamId", "L", "left", "b", "u", "padding", "getPadding", "w", "Lme/f;", "source", "<init>", "(Lme/f;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.h$b */
    /* loaded from: classes2.dex */
    public static final class b implements Source {

        /* renamed from: e, reason: collision with root package name */
        private final BufferedSource f12414e;

        /* renamed from: f, reason: collision with root package name */
        private int f12415f;

        /* renamed from: g, reason: collision with root package name */
        private int f12416g;

        /* renamed from: h, reason: collision with root package name */
        private int f12417h;

        /* renamed from: i, reason: collision with root package name */
        private int f12418i;

        /* renamed from: j, reason: collision with root package name */
        private int f12419j;

        public b(BufferedSource bufferedSource) {
            za.k.e(bufferedSource, "source");
            this.f12414e = bufferedSource;
        }

        private final void c() {
            int i10 = this.f12417h;
            int I = ae.d.I(this.f12414e);
            this.f12418i = I;
            this.f12415f = I;
            int d10 = ae.d.d(this.f12414e.M(), 255);
            this.f12416g = ae.d.d(this.f12414e.M(), 255);
            a aVar = Http2Reader.f12408i;
            if (aVar.a().isLoggable(Level.FINE)) {
                aVar.a().fine(Http2.f12302a.c(true, this.f12417h, this.f12415f, d10, this.f12416g));
            }
            int o10 = this.f12414e.o() & Integer.MAX_VALUE;
            this.f12417h = o10;
            if (d10 == 9) {
                if (o10 != i10) {
                    throw new IOException("TYPE_CONTINUATION streamId changed");
                }
            } else {
                throw new IOException(d10 + " != TYPE_CONTINUATION");
            }
        }

        public final void L(int i10) {
            this.f12417h = i10;
        }

        /* renamed from: b, reason: from getter */
        public final int getF12418i() {
            return this.f12418i;
        }

        @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        public final void m(int i10) {
            this.f12416g = i10;
        }

        @Override // me.Source
        public long read(me.d sink, long byteCount) {
            za.k.e(sink, "sink");
            while (true) {
                int i10 = this.f12418i;
                if (i10 == 0) {
                    this.f12414e.V(this.f12419j);
                    this.f12419j = 0;
                    if ((this.f12416g & 4) != 0) {
                        return -1L;
                    }
                    c();
                } else {
                    long read = this.f12414e.read(sink, Math.min(byteCount, i10));
                    if (read == -1) {
                        return -1L;
                    }
                    this.f12418i -= (int) read;
                    return read;
                }
            }
        }

        @Override // me.Source
        public Timeout timeout() {
            return this.f12414e.timeout();
        }

        public final void u(int i10) {
            this.f12418i = i10;
        }

        public final void v(int i10) {
            this.f12415f = i10;
        }

        public final void w(int i10) {
            this.f12419j = i10;
        }
    }

    /* compiled from: Http2Reader.kt */
    @Metadata(bv = {}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\n\bf\u0018\u00002\u00020\u0001J(\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0004H&J.\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00042\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\fH&J\u0018\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0010H&J\u0018\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0014H&J\b\u0010\u0017\u001a\u00020\tH&J \u0010\u001b\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u0004H&J \u0010\u001f\u001a\u00020\t2\u0006\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u001dH&J\u0018\u0010\"\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010!\u001a\u00020 H&J(\u0010&\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010#\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\u00042\u0006\u0010%\u001a\u00020\u0002H&J&\u0010)\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010'\u001a\u00020\u00042\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\r0\fH&¨\u0006*"}, d2 = {"Lhe/h$c;", "", "", "inFinished", "", "streamId", "Lme/f;", "source", "length", "Lma/f0;", "b", "associatedStreamId", "", "Lhe/c;", "headerBlock", "c", "Lhe/b;", "errorCode", "l", "clearPrevious", "Lhe/m;", "settings", "n", "a", "ack", "payload1", "payload2", "i", "lastGoodStreamId", "Lme/g;", "debugData", "f", "", "windowSizeIncrement", "d", "streamDependency", "weight", "exclusive", "j", "promisedStreamId", "requestHeaders", "o", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.h$c */
    /* loaded from: classes2.dex */
    public interface c {
        void a();

        void b(boolean z10, int i10, BufferedSource bufferedSource, int i11);

        void c(boolean z10, int i10, int i11, List<Header> list);

        void d(int i10, long j10);

        void f(int i10, ErrorCode errorCode, me.g gVar);

        void i(boolean z10, int i10, int i11);

        void j(int i10, int i11, int i12, boolean z10);

        void l(int i10, ErrorCode errorCode);

        void n(boolean z10, Settings settings);

        void o(int i10, int i11, List<Header> list);
    }

    static {
        Logger logger = Logger.getLogger(Http2.class.getName());
        za.k.d(logger, "getLogger(Http2::class.java.name)");
        f12409j = logger;
    }

    public Http2Reader(BufferedSource bufferedSource, boolean z10) {
        za.k.e(bufferedSource, "source");
        this.f12410e = bufferedSource;
        this.f12411f = z10;
        b bVar = new b(bufferedSource);
        this.f12412g = bVar;
        this.f12413h = new Hpack.a(bVar, 4096, 0, 4, null);
    }

    private final void L(c cVar, int i10, int i11, int i12) {
        if (i12 != 0) {
            boolean z10 = (i11 & 1) != 0;
            int d10 = (i11 & 8) != 0 ? ae.d.d(this.f12410e.M(), 255) : 0;
            if ((i11 & 32) != 0) {
                S(cVar, i12);
                i10 -= 5;
            }
            cVar.c(z10, i12, -1, w(f12408i.b(i10, i11, d10), d10, i11, i12));
            return;
        }
        throw new IOException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0");
    }

    private final void N(c cVar, int i10, int i11, int i12) {
        if (i10 != 8) {
            throw new IOException(za.k.l("TYPE_PING length != 8: ", Integer.valueOf(i10)));
        }
        if (i12 == 0) {
            cVar.i((i11 & 1) != 0, this.f12410e.o(), this.f12410e.o());
            return;
        }
        throw new IOException("TYPE_PING streamId != 0");
    }

    private final void S(c cVar, int i10) {
        int o10 = this.f12410e.o();
        cVar.j(i10, o10 & Integer.MAX_VALUE, ae.d.d(this.f12410e.M(), 255) + 1, (Integer.MIN_VALUE & o10) != 0);
    }

    private final void U(c cVar, int i10, int i11, int i12) {
        if (i10 == 5) {
            if (i12 != 0) {
                S(cVar, i12);
                return;
            }
            throw new IOException("TYPE_PRIORITY streamId == 0");
        }
        throw new IOException("TYPE_PRIORITY length: " + i10 + " != 5");
    }

    private final void X(c cVar, int i10, int i11, int i12) {
        if (i12 != 0) {
            int d10 = (i11 & 8) != 0 ? ae.d.d(this.f12410e.M(), 255) : 0;
            cVar.o(i12, this.f12410e.o() & Integer.MAX_VALUE, w(f12408i.b(i10 - 4, i11, d10), d10, i11, i12));
            return;
        }
        throw new IOException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0");
    }

    private final void a0(c cVar, int i10, int i11, int i12) {
        if (i10 != 4) {
            throw new IOException("TYPE_RST_STREAM length: " + i10 + " != 4");
        }
        if (i12 != 0) {
            int o10 = this.f12410e.o();
            ErrorCode a10 = ErrorCode.f12254f.a(o10);
            if (a10 != null) {
                cVar.l(i12, a10);
                return;
            }
            throw new IOException(za.k.l("TYPE_RST_STREAM unexpected error code: ", Integer.valueOf(o10)));
        }
        throw new IOException("TYPE_RST_STREAM streamId == 0");
    }

    private final void e0(c cVar, int i10, int i11, int i12) {
        PrimitiveRanges k10;
        Progressions j10;
        int o10;
        if (i12 != 0) {
            throw new IOException("TYPE_SETTINGS streamId != 0");
        }
        if ((i11 & 1) != 0) {
            if (i10 == 0) {
                cVar.a();
                return;
            }
            throw new IOException("FRAME_SIZE_ERROR ack frame should be empty!");
        }
        if (i10 % 6 == 0) {
            Settings settings = new Settings();
            k10 = _Ranges.k(0, i10);
            j10 = _Ranges.j(k10, 6);
            int d10 = j10.d();
            int e10 = j10.e();
            int f10 = j10.f();
            if ((f10 > 0 && d10 <= e10) || (f10 < 0 && e10 <= d10)) {
                while (true) {
                    int i13 = d10 + f10;
                    int e11 = ae.d.e(this.f12410e.k0(), 65535);
                    o10 = this.f12410e.o();
                    if (e11 != 2) {
                        if (e11 == 3) {
                            e11 = 4;
                        } else if (e11 == 4) {
                            e11 = 7;
                            if (o10 < 0) {
                                throw new IOException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1");
                            }
                        } else if (e11 == 5 && (o10 < 16384 || o10 > 16777215)) {
                            break;
                        }
                    } else if (o10 != 0 && o10 != 1) {
                        throw new IOException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1");
                    }
                    settings.h(e11, o10);
                    if (d10 == e10) {
                        break;
                    } else {
                        d10 = i13;
                    }
                }
                throw new IOException(za.k.l("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: ", Integer.valueOf(o10)));
            }
            cVar.n(false, settings);
            return;
        }
        throw new IOException(za.k.l("TYPE_SETTINGS length % 6 != 0: ", Integer.valueOf(i10)));
    }

    private final void h0(c cVar, int i10, int i11, int i12) {
        if (i10 == 4) {
            long f10 = ae.d.f(this.f12410e.o(), 2147483647L);
            if (f10 != 0) {
                cVar.d(i12, f10);
                return;
            }
            throw new IOException("windowSizeIncrement was 0");
        }
        throw new IOException(za.k.l("TYPE_WINDOW_UPDATE length !=4: ", Integer.valueOf(i10)));
    }

    private final void u(c cVar, int i10, int i11, int i12) {
        if (i12 == 0) {
            throw new IOException("PROTOCOL_ERROR: TYPE_DATA streamId == 0");
        }
        boolean z10 = (i11 & 1) != 0;
        if (!((i11 & 32) != 0)) {
            int d10 = (i11 & 8) != 0 ? ae.d.d(this.f12410e.M(), 255) : 0;
            cVar.b(z10, i12, this.f12410e, f12408i.b(i10, i11, d10));
            this.f12410e.V(d10);
            return;
        }
        throw new IOException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA");
    }

    private final void v(c cVar, int i10, int i11, int i12) {
        if (i10 < 8) {
            throw new IOException(za.k.l("TYPE_GOAWAY length < 8: ", Integer.valueOf(i10)));
        }
        if (i12 == 0) {
            int o10 = this.f12410e.o();
            int o11 = this.f12410e.o();
            int i13 = i10 - 8;
            ErrorCode a10 = ErrorCode.f12254f.a(o11);
            if (a10 != null) {
                me.g gVar = me.g.f15494i;
                if (i13 > 0) {
                    gVar = this.f12410e.h(i13);
                }
                cVar.f(o10, a10, gVar);
                return;
            }
            throw new IOException(za.k.l("TYPE_GOAWAY unexpected error code: ", Integer.valueOf(o11)));
        }
        throw new IOException("TYPE_GOAWAY streamId != 0");
    }

    private final List<Header> w(int length, int padding, int flags, int streamId) {
        this.f12412g.u(length);
        b bVar = this.f12412g;
        bVar.v(bVar.getF12418i());
        this.f12412g.w(padding);
        this.f12412g.m(flags);
        this.f12412g.L(streamId);
        this.f12413h.k();
        return this.f12413h.e();
    }

    public final boolean c(boolean requireSettings, c handler) {
        za.k.e(handler, "handler");
        try {
            this.f12410e.p0(9L);
            int I = ae.d.I(this.f12410e);
            if (I <= 16384) {
                int d10 = ae.d.d(this.f12410e.M(), 255);
                int d11 = ae.d.d(this.f12410e.M(), 255);
                int o10 = this.f12410e.o() & Integer.MAX_VALUE;
                Logger logger = f12409j;
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(Http2.f12302a.c(true, o10, I, d10, d11));
                }
                if (requireSettings && d10 != 4) {
                    throw new IOException(za.k.l("Expected a SETTINGS frame but was ", Http2.f12302a.b(d10)));
                }
                switch (d10) {
                    case 0:
                        u(handler, I, d11, o10);
                        return true;
                    case 1:
                        L(handler, I, d11, o10);
                        return true;
                    case 2:
                        U(handler, I, d11, o10);
                        return true;
                    case 3:
                        a0(handler, I, d11, o10);
                        return true;
                    case 4:
                        e0(handler, I, d11, o10);
                        return true;
                    case 5:
                        X(handler, I, d11, o10);
                        return true;
                    case 6:
                        N(handler, I, d11, o10);
                        return true;
                    case 7:
                        v(handler, I, d11, o10);
                        return true;
                    case 8:
                        h0(handler, I, d11, o10);
                        return true;
                    default:
                        this.f12410e.V(I);
                        return true;
                }
            }
            throw new IOException(za.k.l("FRAME_SIZE_ERROR: ", Integer.valueOf(I)));
        } catch (EOFException unused) {
            return false;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f12410e.close();
    }

    public final void m(c cVar) {
        za.k.e(cVar, "handler");
        if (this.f12411f) {
            if (!c(true, cVar)) {
                throw new IOException("Required SETTINGS preface not received");
            }
            return;
        }
        BufferedSource bufferedSource = this.f12410e;
        me.g gVar = Http2.f12303b;
        me.g h10 = bufferedSource.h(gVar.t());
        Logger logger = f12409j;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(ae.d.s(za.k.l("<< CONNECTION ", h10.j()), new Object[0]));
        }
        if (!za.k.a(gVar, h10)) {
            throw new IOException(za.k.l("Expected a connection header but was ", h10.w()));
        }
    }
}
