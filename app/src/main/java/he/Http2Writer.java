package he;

import he.Hpack;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import me.BufferedSink;
import za.DefaultConstructorMarker;

/* compiled from: Http2Writer.kt */
@Metadata(bv = {}, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u00015B\u0017\u0012\u0006\u00101\u001a\u000200\u0012\u0006\u00102\u001a\u00020\u0016¢\u0006\u0004\b3\u00104J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u0006\u0010\b\u001a\u00020\u0006J\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\tJ$\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\u00022\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rJ\u0006\u0010\u0011\u001a\u00020\u0006J\u0016\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0012J\u0006\u0010\u0015\u001a\u00020\u0002J(\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0019\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0005\u001a\u00020\u0002J(\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00022\b\u0010\u001c\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0005\u001a\u00020\u0002J\u000e\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\tJ\u001e\u0010#\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00162\u0006\u0010!\u001a\u00020\u00022\u0006\u0010\"\u001a\u00020\u0002J\u001e\u0010'\u001a\u00020\u00062\u0006\u0010$\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010&\u001a\u00020%J\u0016\u0010)\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010(\u001a\u00020\u0004J&\u0010,\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010*\u001a\u00020\u00022\u0006\u0010+\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002J\b\u0010-\u001a\u00020\u0006H\u0016J$\u0010/\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u000e0\r¨\u00066"}, d2 = {"Lhe/j;", "Ljava/io/Closeable;", "", "streamId", "", "byteCount", "Lma/f0;", "h0", "c", "Lhe/m;", "peerSettings", "b", "promisedStreamId", "", "Lhe/c;", "requestHeaders", "U", "flush", "Lhe/b;", "errorCode", "X", "N", "", "outFinished", "Lme/d;", "source", "m", "flags", "buffer", "u", "settings", "a0", "ack", "payload1", "payload2", "S", "lastGoodStreamId", "", "debugData", "w", "windowSizeIncrement", "e0", "length", "type", "v", "close", "headerBlock", "L", "Lme/e;", "sink", "client", "<init>", "(Lme/e;Z)V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class Http2Writer implements Closeable {

    /* renamed from: k, reason: collision with root package name */
    public static final a f12448k = new a(null);

    /* renamed from: l, reason: collision with root package name */
    private static final Logger f12449l = Logger.getLogger(Http2.class.getName());

    /* renamed from: e, reason: collision with root package name */
    private final BufferedSink f12450e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f12451f;

    /* renamed from: g, reason: collision with root package name */
    private final me.d f12452g;

    /* renamed from: h, reason: collision with root package name */
    private int f12453h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f12454i;

    /* renamed from: j, reason: collision with root package name */
    private final Hpack.b f12455j;

    /* compiled from: Http2Writer.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007R\u001c\u0010\u0004\u001a\n \u0003*\u0004\u0018\u00010\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005¨\u0006\b"}, d2 = {"Lhe/j$a;", "", "Ljava/util/logging/Logger;", "kotlin.jvm.PlatformType", "logger", "Ljava/util/logging/Logger;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.j$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public Http2Writer(BufferedSink bufferedSink, boolean z10) {
        za.k.e(bufferedSink, "sink");
        this.f12450e = bufferedSink;
        this.f12451f = z10;
        me.d dVar = new me.d();
        this.f12452g = dVar;
        this.f12453h = 16384;
        this.f12455j = new Hpack.b(0, false, dVar, 3, null);
    }

    private final void h0(int i10, long j10) {
        while (j10 > 0) {
            long min = Math.min(this.f12453h, j10);
            j10 -= min;
            v(i10, (int) min, 9, j10 == 0 ? 4 : 0);
            this.f12450e.write(this.f12452g, min);
        }
    }

    public final synchronized void L(boolean z10, int i10, List<Header> list) {
        za.k.e(list, "headerBlock");
        if (!this.f12454i) {
            this.f12455j.g(list);
            long v02 = this.f12452g.v0();
            long min = Math.min(this.f12453h, v02);
            int i11 = v02 == min ? 4 : 0;
            if (z10) {
                i11 |= 1;
            }
            v(i10, (int) min, 1, i11);
            this.f12450e.write(this.f12452g, min);
            if (v02 > min) {
                h0(i10, v02 - min);
            }
        } else {
            throw new IOException("closed");
        }
    }

    /* renamed from: N, reason: from getter */
    public final int getF12453h() {
        return this.f12453h;
    }

    public final synchronized void S(boolean z10, int i10, int i11) {
        if (!this.f12454i) {
            v(0, 8, 6, z10 ? 1 : 0);
            this.f12450e.p(i10);
            this.f12450e.p(i11);
            this.f12450e.flush();
        } else {
            throw new IOException("closed");
        }
    }

    public final synchronized void U(int i10, int i11, List<Header> list) {
        za.k.e(list, "requestHeaders");
        if (!this.f12454i) {
            this.f12455j.g(list);
            long v02 = this.f12452g.v0();
            int min = (int) Math.min(this.f12453h - 4, v02);
            long j10 = min;
            v(i10, min + 4, 5, v02 == j10 ? 4 : 0);
            this.f12450e.p(i11 & Integer.MAX_VALUE);
            this.f12450e.write(this.f12452g, j10);
            if (v02 > j10) {
                h0(i10, v02 - j10);
            }
        } else {
            throw new IOException("closed");
        }
    }

    public final synchronized void X(int i10, ErrorCode errorCode) {
        za.k.e(errorCode, "errorCode");
        if (!this.f12454i) {
            if (errorCode.getF12270e() != -1) {
                v(i10, 4, 3, 0);
                this.f12450e.p(errorCode.getF12270e());
                this.f12450e.flush();
            } else {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
        } else {
            throw new IOException("closed");
        }
    }

    public final synchronized void a0(Settings settings) {
        za.k.e(settings, "settings");
        if (!this.f12454i) {
            int i10 = 0;
            v(0, settings.i() * 6, 4, 0);
            while (i10 < 10) {
                int i11 = i10 + 1;
                if (settings.f(i10)) {
                    this.f12450e.k(i10 != 4 ? i10 != 7 ? i10 : 4 : 3);
                    this.f12450e.p(settings.a(i10));
                }
                i10 = i11;
            }
            this.f12450e.flush();
        } else {
            throw new IOException("closed");
        }
    }

    public final synchronized void b(Settings settings) {
        za.k.e(settings, "peerSettings");
        if (!this.f12454i) {
            this.f12453h = settings.e(this.f12453h);
            if (settings.b() != -1) {
                this.f12455j.e(settings.b());
            }
            v(0, 0, 4, 1);
            this.f12450e.flush();
        } else {
            throw new IOException("closed");
        }
    }

    public final synchronized void c() {
        if (!this.f12454i) {
            if (this.f12451f) {
                Logger logger = f12449l;
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(ae.d.s(za.k.l(">> CONNECTION ", Http2.f12303b.j()), new Object[0]));
                }
                this.f12450e.F(Http2.f12303b);
                this.f12450e.flush();
                return;
            }
            return;
        }
        throw new IOException("closed");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        this.f12454i = true;
        this.f12450e.close();
    }

    public final synchronized void e0(int i10, long j10) {
        if (this.f12454i) {
            throw new IOException("closed");
        }
        if (j10 != 0 && j10 <= 2147483647L) {
            v(i10, 4, 8, 0);
            this.f12450e.p((int) j10);
            this.f12450e.flush();
        } else {
            throw new IllegalArgumentException(za.k.l("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: ", Long.valueOf(j10)).toString());
        }
    }

    public final synchronized void flush() {
        if (!this.f12454i) {
            this.f12450e.flush();
        } else {
            throw new IOException("closed");
        }
    }

    public final synchronized void m(boolean z10, int i10, me.d dVar, int i11) {
        if (!this.f12454i) {
            u(i10, z10 ? 1 : 0, dVar, i11);
        } else {
            throw new IOException("closed");
        }
    }

    public final void u(int i10, int i11, me.d dVar, int i12) {
        v(i10, i12, 0, i11);
        if (i12 > 0) {
            BufferedSink bufferedSink = this.f12450e;
            za.k.b(dVar);
            bufferedSink.write(dVar, i12);
        }
    }

    public final void v(int i10, int i11, int i12, int i13) {
        Logger logger = f12449l;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(Http2.f12302a.c(false, i10, i11, i12, i13));
        }
        if (!(i11 <= this.f12453h)) {
            throw new IllegalArgumentException(("FRAME_SIZE_ERROR length > " + this.f12453h + ": " + i11).toString());
        }
        if ((Integer.MIN_VALUE & i10) == 0) {
            ae.d.Y(this.f12450e, i11);
            this.f12450e.t(i12 & 255);
            this.f12450e.t(i13 & 255);
            this.f12450e.p(i10 & Integer.MAX_VALUE);
            return;
        }
        throw new IllegalArgumentException(za.k.l("reserved bit set: ", Integer.valueOf(i10)).toString());
    }

    public final synchronized void w(int i10, ErrorCode errorCode, byte[] bArr) {
        za.k.e(errorCode, "errorCode");
        za.k.e(bArr, "debugData");
        if (!this.f12454i) {
            if (errorCode.getF12270e() != -1) {
                v(0, bArr.length + 8, 7, 0);
                this.f12450e.p(i10);
                this.f12450e.p(errorCode.getF12270e());
                if (!(bArr.length == 0)) {
                    this.f12450e.f0(bArr);
                }
                this.f12450e.flush();
            } else {
                throw new IllegalArgumentException("errorCode.httpCode == -1".toString());
            }
        } else {
            throw new IOException("closed");
        }
    }
}
