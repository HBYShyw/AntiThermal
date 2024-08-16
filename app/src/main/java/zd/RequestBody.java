package zd;

import java.nio.charset.Charset;
import kotlin.Metadata;
import me.BufferedSink;
import sd.Charsets;
import za.DefaultConstructorMarker;

/* compiled from: RequestBody.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001:\u0001\u0005B\u0007¢\u0006\u0004\b\r\u0010\u000eJ\n\u0010\u0003\u001a\u0004\u0018\u00010\u0002H&J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u0006H&J\b\u0010\u000b\u001a\u00020\nH\u0016J\b\u0010\f\u001a\u00020\nH\u0016¨\u0006\u000f"}, d2 = {"Lzd/a0;", "", "Lzd/w;", "b", "", "a", "Lme/e;", "sink", "Lma/f0;", "f", "", "d", "e", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.a0 */
/* loaded from: classes2.dex */
public abstract class RequestBody {

    /* renamed from: a */
    public static final a f20496a = new a(null);

    /* compiled from: RequestBody.kt */
    @Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\u0010\b\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0010\u0010\u0011J\u001f\u0010\u0006\u001a\u00020\u0005*\u00020\u00022\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0007¢\u0006\u0004\b\u0006\u0010\u0007J3\u0010\f\u001a\u00020\u0005*\u00020\b2\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\tH\u0007¢\u0006\u0004\b\f\u0010\rJ\u001a\u0010\u000f\u001a\u00020\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000e\u001a\u00020\u0002H\u0007¨\u0006\u0012"}, d2 = {"Lzd/a0$a;", "", "", "Lzd/w;", "contentType", "Lzd/a0;", "a", "(Ljava/lang/String;Lzd/w;)Lzd/a0;", "", "", "offset", "byteCount", "c", "([BLzd/w;II)Lzd/a0;", "content", "b", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.a0$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: RequestBody.kt */
        @Metadata(bv = {}, d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\n\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\n"}, d2 = {"zd/a0$a$a", "Lzd/a0;", "Lzd/w;", "b", "", "a", "Lme/e;", "sink", "Lma/f0;", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: zd.a0$a$a */
        /* loaded from: classes2.dex */
        public static final class C0122a extends RequestBody {

            /* renamed from: b */
            final /* synthetic */ MediaType f20497b;

            /* renamed from: c */
            final /* synthetic */ int f20498c;

            /* renamed from: d */
            final /* synthetic */ byte[] f20499d;

            /* renamed from: e */
            final /* synthetic */ int f20500e;

            C0122a(MediaType mediaType, int i10, byte[] bArr, int i11) {
                this.f20497b = mediaType;
                this.f20498c = i10;
                this.f20499d = bArr;
                this.f20500e = i11;
            }

            @Override // zd.RequestBody
            public long a() {
                return this.f20498c;
            }

            @Override // zd.RequestBody
            /* renamed from: b, reason: from getter */
            public MediaType getF20497b() {
                return this.f20497b;
            }

            @Override // zd.RequestBody
            public void f(BufferedSink bufferedSink) {
                za.k.e(bufferedSink, "sink");
                bufferedSink.O(this.f20499d, this.f20500e, this.f20498c);
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ RequestBody d(a aVar, byte[] bArr, MediaType mediaType, int i10, int i11, int i12, Object obj) {
            if ((i12 & 1) != 0) {
                mediaType = null;
            }
            if ((i12 & 2) != 0) {
                i10 = 0;
            }
            if ((i12 & 4) != 0) {
                i11 = bArr.length;
            }
            return aVar.c(bArr, mediaType, i10, i11);
        }

        public final RequestBody a(String str, MediaType mediaType) {
            za.k.e(str, "<this>");
            Charset charset = Charsets.f18469b;
            if (mediaType != null) {
                Charset d10 = MediaType.d(mediaType, null, 1, null);
                if (d10 == null) {
                    mediaType = MediaType.f20732e.b(mediaType + "; charset=utf-8");
                } else {
                    charset = d10;
                }
            }
            byte[] bytes = str.getBytes(charset);
            za.k.d(bytes, "this as java.lang.String).getBytes(charset)");
            return c(bytes, mediaType, 0, bytes.length);
        }

        public final RequestBody b(MediaType contentType, String content) {
            za.k.e(content, "content");
            return a(content, contentType);
        }

        public final RequestBody c(byte[] bArr, MediaType mediaType, int i10, int i11) {
            za.k.e(bArr, "<this>");
            ae.d.k(bArr.length, i10, i11);
            return new C0122a(mediaType, i11, bArr, i10);
        }
    }

    public static final RequestBody c(MediaType mediaType, String str) {
        return f20496a.b(mediaType, str);
    }

    public abstract long a();

    /* renamed from: b */
    public abstract MediaType getF20497b();

    public boolean d() {
        return false;
    }

    public boolean e() {
        return false;
    }

    public abstract void f(BufferedSink bufferedSink);
}
