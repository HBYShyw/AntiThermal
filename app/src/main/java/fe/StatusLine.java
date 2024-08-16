package fe;

import java.net.ProtocolException;
import kotlin.Metadata;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import zd.Protocol;

/* compiled from: StatusLine.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0001\u000bB\u001f\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0002¢\u0006\u0004\b\t\u0010\nJ\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\f"}, d2 = {"Lfe/k;", "", "", "toString", "Lzd/y;", "protocol", "", "code", "message", "<init>", "(Lzd/y;ILjava/lang/String;)V", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: fe.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class StatusLine {

    /* renamed from: d, reason: collision with root package name */
    public static final a f11476d = new a(null);

    /* renamed from: a, reason: collision with root package name */
    public final Protocol f11477a;

    /* renamed from: b, reason: collision with root package name */
    public final int f11478b;

    /* renamed from: c, reason: collision with root package name */
    public final String f11479c;

    /* compiled from: StatusLine.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002R\u0014\u0010\u0007\u001a\u00020\u00068\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u00068\u0006X\u0086T¢\u0006\u0006\n\u0004\b\t\u0010\bR\u0014\u0010\n\u001a\u00020\u00068\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00068\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\b¨\u0006\u000e"}, d2 = {"Lfe/k$a;", "", "", "statusLine", "Lfe/k;", "a", "", "HTTP_CONTINUE", "I", "HTTP_MISDIRECTED_REQUEST", "HTTP_PERM_REDIRECT", "HTTP_TEMP_REDIRECT", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: fe.k$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final StatusLine a(String statusLine) {
            boolean D;
            boolean D2;
            Protocol protocol;
            String str;
            za.k.e(statusLine, "statusLine");
            D = StringsJVM.D(statusLine, "HTTP/1.", false, 2, null);
            int i10 = 9;
            if (D) {
                if (statusLine.length() >= 9 && statusLine.charAt(8) == ' ') {
                    int charAt = statusLine.charAt(7) - '0';
                    if (charAt == 0) {
                        protocol = Protocol.HTTP_1_0;
                    } else if (charAt == 1) {
                        protocol = Protocol.HTTP_1_1;
                    } else {
                        throw new ProtocolException(za.k.l("Unexpected status line: ", statusLine));
                    }
                } else {
                    throw new ProtocolException(za.k.l("Unexpected status line: ", statusLine));
                }
            } else {
                D2 = StringsJVM.D(statusLine, "ICY ", false, 2, null);
                if (D2) {
                    protocol = Protocol.HTTP_1_0;
                    i10 = 4;
                } else {
                    throw new ProtocolException(za.k.l("Unexpected status line: ", statusLine));
                }
            }
            int i11 = i10 + 3;
            if (statusLine.length() >= i11) {
                try {
                    String substring = statusLine.substring(i10, i11);
                    za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                    int parseInt = Integer.parseInt(substring);
                    if (statusLine.length() <= i11) {
                        str = "";
                    } else if (statusLine.charAt(i11) == ' ') {
                        str = statusLine.substring(i10 + 4);
                        za.k.d(str, "this as java.lang.String).substring(startIndex)");
                    } else {
                        throw new ProtocolException(za.k.l("Unexpected status line: ", statusLine));
                    }
                    return new StatusLine(protocol, parseInt, str);
                } catch (NumberFormatException unused) {
                    throw new ProtocolException(za.k.l("Unexpected status line: ", statusLine));
                }
            }
            throw new ProtocolException(za.k.l("Unexpected status line: ", statusLine));
        }
    }

    public StatusLine(Protocol protocol, int i10, String str) {
        za.k.e(protocol, "protocol");
        za.k.e(str, "message");
        this.f11477a = protocol;
        this.f11478b = i10;
        this.f11479c = str;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        if (this.f11477a == Protocol.HTTP_1_0) {
            sb2.append("HTTP/1.0");
        } else {
            sb2.append("HTTP/1.1");
        }
        sb2.append(' ');
        sb2.append(this.f11478b);
        sb2.append(' ');
        sb2.append(this.f11479c);
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }
}
