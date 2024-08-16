package ge;

import kotlin.Metadata;
import me.BufferedSource;
import za.DefaultConstructorMarker;
import za.k;
import zd.Headers;

/* compiled from: HeadersReader.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0005B\u000f\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\b\u0010\tJ\u0006\u0010\u0003\u001a\u00020\u0002J\u0006\u0010\u0005\u001a\u00020\u0004¨\u0006\n"}, d2 = {"Lge/a;", "", "", "b", "Lzd/t;", "a", "Lme/f;", "source", "<init>", "(Lme/f;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: ge.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class HeadersReader {

    /* renamed from: c, reason: collision with root package name */
    public static final a f11920c = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final BufferedSource f11921a;

    /* renamed from: b, reason: collision with root package name */
    private long f11922b;

    /* compiled from: HeadersReader.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lge/a$a;", "", "", "HEADER_LIMIT", "I", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: ge.a$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public HeadersReader(BufferedSource bufferedSource) {
        k.e(bufferedSource, "source");
        this.f11921a = bufferedSource;
        this.f11922b = 262144L;
    }

    public final Headers a() {
        Headers.a aVar = new Headers.a();
        while (true) {
            String b10 = b();
            if (b10.length() == 0) {
                return aVar.d();
            }
            aVar.b(b10);
        }
    }

    public final String b() {
        String x10 = this.f11921a.x(this.f11922b);
        this.f11922b -= x10.length();
        return x10;
    }
}
