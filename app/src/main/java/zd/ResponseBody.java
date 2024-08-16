package zd;

import java.io.Closeable;
import java.nio.charset.Charset;
import kotlin.Metadata;
import me.BufferedSource;
import sd.Charsets;
import za.DefaultConstructorMarker;

/* compiled from: ResponseBody.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001:\u0001\u0010B\u0007¢\u0006\u0004\b\u000e\u0010\u000fJ\b\u0010\u0003\u001a\u00020\u0002H\u0002J\n\u0010\u0005\u001a\u0004\u0018\u00010\u0004H&J\b\u0010\u0007\u001a\u00020\u0006H&J\b\u0010\t\u001a\u00020\bH&J\u0006\u0010\u000b\u001a\u00020\nJ\b\u0010\r\u001a\u00020\fH\u0016¨\u0006\u0011"}, d2 = {"Lzd/c0;", "Ljava/io/Closeable;", "Ljava/nio/charset/Charset;", "b", "Lzd/w;", "m", "", "c", "Lme/f;", "u", "", "v", "Lma/f0;", "close", "<init>", "()V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.c0 */
/* loaded from: classes2.dex */
public abstract class ResponseBody implements Closeable {

    /* renamed from: e */
    public static final a f20532e = new a(null);

    /* compiled from: ResponseBody.kt */
    @Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eJ\u001f\u0010\u0006\u001a\u00020\u0005*\u00020\u00022\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0007¢\u0006\u0004\b\u0006\u0010\u0007J)\u0010\u000b\u001a\u00020\u0005*\u00020\b2\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\n\u001a\u00020\tH\u0007¢\u0006\u0004\b\u000b\u0010\f¨\u0006\u000f"}, d2 = {"Lzd/c0$a;", "", "", "Lzd/w;", "contentType", "Lzd/c0;", "b", "([BLzd/w;)Lzd/c0;", "Lme/f;", "", "contentLength", "a", "(Lme/f;Lzd/w;J)Lzd/c0;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.c0$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: ResponseBody.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\n\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\b"}, d2 = {"zd/c0$a$a", "Lzd/c0;", "Lzd/w;", "m", "", "c", "Lme/f;", "u", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: zd.c0$a$a */
        /* loaded from: classes2.dex */
        public static final class C0124a extends ResponseBody {

            /* renamed from: f */
            final /* synthetic */ MediaType f20533f;

            /* renamed from: g */
            final /* synthetic */ long f20534g;

            /* renamed from: h */
            final /* synthetic */ BufferedSource f20535h;

            C0124a(MediaType mediaType, long j10, BufferedSource bufferedSource) {
                this.f20533f = mediaType;
                this.f20534g = j10;
                this.f20535h = bufferedSource;
            }

            @Override // zd.ResponseBody
            /* renamed from: c, reason: from getter */
            public long getF20534g() {
                return this.f20534g;
            }

            @Override // zd.ResponseBody
            /* renamed from: m, reason: from getter */
            public MediaType getF20533f() {
                return this.f20533f;
            }

            @Override // zd.ResponseBody
            /* renamed from: u, reason: from getter */
            public BufferedSource getF20535h() {
                return this.f20535h;
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ ResponseBody c(a aVar, byte[] bArr, MediaType mediaType, int i10, Object obj) {
            if ((i10 & 1) != 0) {
                mediaType = null;
            }
            return aVar.b(bArr, mediaType);
        }

        public final ResponseBody a(BufferedSource bufferedSource, MediaType mediaType, long j10) {
            za.k.e(bufferedSource, "<this>");
            return new C0124a(mediaType, j10, bufferedSource);
        }

        public final ResponseBody b(byte[] bArr, MediaType mediaType) {
            za.k.e(bArr, "<this>");
            return a(new me.d().f0(bArr), mediaType, bArr.length);
        }
    }

    private final Charset b() {
        MediaType f20533f = getF20533f();
        Charset c10 = f20533f == null ? null : f20533f.c(Charsets.f18469b);
        return c10 == null ? Charsets.f18469b : c10;
    }

    /* renamed from: c */
    public abstract long getF20534g();

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        ae.d.l(getF20535h());
    }

    /* renamed from: m */
    public abstract MediaType getF20533f();

    /* renamed from: u */
    public abstract BufferedSource getF20535h();

    public final String v() {
        BufferedSource f20535h = getF20535h();
        try {
            String J = f20535h.J(ae.d.H(f20535h, b()));
            wa.Closeable.a(f20535h, null);
            return J;
        } finally {
        }
    }
}
