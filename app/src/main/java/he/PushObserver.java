package he;

import java.util.List;
import kotlin.Metadata;
import me.BufferedSource;

/* compiled from: PushObserver.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0013J\u001e\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H&J&\u0010\u000b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\n\u001a\u00020\u0007H&J(\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u0007H&J\u0018\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u0010H&¨\u0006\u0014"}, d2 = {"Lhe/l;", "", "", "streamId", "", "Lhe/c;", "requestHeaders", "", "b", "responseHeaders", "last", "c", "Lme/f;", "source", "byteCount", "d", "Lhe/b;", "errorCode", "Lma/f0;", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.l, reason: use source file name */
/* loaded from: classes2.dex */
public interface PushObserver {

    /* renamed from: a, reason: collision with root package name */
    public static final a f12463a = a.f12465a;

    /* renamed from: b, reason: collision with root package name */
    public static final PushObserver f12464b = new a.C0051a();

    /* compiled from: PushObserver.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u0007B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0017\u0010\u0003\u001a\u00020\u00028\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0001¨\u0006\b"}, d2 = {"Lhe/l$a;", "", "Lhe/l;", "CANCEL", "Lhe/l;", "<init>", "()V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.l$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f12465a = new a();

        /* compiled from: PushObserver.kt */
        @Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0014\u0010\u0015J\u001e\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0016J&\u0010\u000b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\n\u001a\u00020\u0007H\u0016J(\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u0007H\u0016J\u0018\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u0010H\u0016¨\u0006\u0016"}, d2 = {"Lhe/l$a$a;", "Lhe/l;", "", "streamId", "", "Lhe/c;", "requestHeaders", "", "b", "responseHeaders", "last", "c", "Lme/f;", "source", "byteCount", "d", "Lhe/b;", "errorCode", "Lma/f0;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
        /* renamed from: he.l$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        private static final class C0051a implements PushObserver {
            @Override // he.PushObserver
            public void a(int i10, ErrorCode errorCode) {
                za.k.e(errorCode, "errorCode");
            }

            @Override // he.PushObserver
            public boolean b(int streamId, List<Header> requestHeaders) {
                za.k.e(requestHeaders, "requestHeaders");
                return true;
            }

            @Override // he.PushObserver
            public boolean c(int streamId, List<Header> responseHeaders, boolean last) {
                za.k.e(responseHeaders, "responseHeaders");
                return true;
            }

            @Override // he.PushObserver
            public boolean d(int streamId, BufferedSource source, int byteCount, boolean last) {
                za.k.e(source, "source");
                source.V(byteCount);
                return true;
            }
        }

        private a() {
        }
    }

    void a(int i10, ErrorCode errorCode);

    boolean b(int streamId, List<Header> requestHeaders);

    boolean c(int streamId, List<Header> responseHeaders, boolean last);

    boolean d(int streamId, BufferedSource source, int byteCount, boolean last);
}
