package he;

import kotlin.Metadata;
import sd.StringsJVM;

/* compiled from: Http2.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ.\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0004J\u0017\u0010\u000b\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u0004H\u0000¢\u0006\u0004\b\u000b\u0010\fJ\u0016\u0010\r\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0004¨\u0006\u0010"}, d2 = {"Lhe/e;", "", "", "inbound", "", "streamId", "length", "type", "flags", "", "c", "b", "(I)Ljava/lang/String;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class Http2 {

    /* renamed from: a, reason: collision with root package name */
    public static final Http2 f12302a = new Http2();

    /* renamed from: b, reason: collision with root package name */
    public static final me.g f12303b = me.g.f15493h.c("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");

    /* renamed from: c, reason: collision with root package name */
    private static final String[] f12304c = {"DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION"};

    /* renamed from: d, reason: collision with root package name */
    private static final String[] f12305d = new String[64];

    /* renamed from: e, reason: collision with root package name */
    private static final String[] f12306e;

    static {
        String y4;
        String[] strArr = new String[256];
        int i10 = 0;
        for (int i11 = 0; i11 < 256; i11++) {
            String binaryString = Integer.toBinaryString(i11);
            za.k.d(binaryString, "toBinaryString(it)");
            y4 = StringsJVM.y(ae.d.s("%8s", binaryString), ' ', '0', false, 4, null);
            strArr[i11] = y4;
        }
        f12306e = strArr;
        String[] strArr2 = f12305d;
        strArr2[0] = "";
        strArr2[1] = "END_STREAM";
        int[] iArr = {1};
        strArr2[8] = "PADDED";
        int i12 = 0;
        while (i12 < 1) {
            int i13 = iArr[i12];
            i12++;
            String[] strArr3 = f12305d;
            strArr3[i13 | 8] = za.k.l(strArr3[i13], "|PADDED");
        }
        String[] strArr4 = f12305d;
        strArr4[4] = "END_HEADERS";
        strArr4[32] = "PRIORITY";
        strArr4[36] = "END_HEADERS|PRIORITY";
        int[] iArr2 = {4, 32, 36};
        int i14 = 0;
        while (i14 < 3) {
            int i15 = iArr2[i14];
            i14++;
            int i16 = 0;
            while (i16 < 1) {
                int i17 = iArr[i16];
                i16++;
                String[] strArr5 = f12305d;
                int i18 = i17 | i15;
                StringBuilder sb2 = new StringBuilder();
                sb2.append((Object) strArr5[i17]);
                sb2.append('|');
                sb2.append((Object) strArr5[i15]);
                strArr5[i18] = sb2.toString();
                strArr5[i18 | 8] = ((Object) strArr5[i17]) + '|' + ((Object) strArr5[i15]) + "|PADDED";
            }
        }
        int length = f12305d.length;
        while (i10 < length) {
            int i19 = i10 + 1;
            String[] strArr6 = f12305d;
            if (strArr6[i10] == null) {
                strArr6[i10] = f12306e[i10];
            }
            i10 = i19;
        }
    }

    private Http2() {
    }

    public final String a(int type, int flags) {
        String str;
        String z10;
        String z11;
        if (flags == 0) {
            return "";
        }
        if (type != 2 && type != 3) {
            if (type == 4 || type == 6) {
                return flags == 1 ? "ACK" : f12306e[flags];
            }
            if (type != 7 && type != 8) {
                String[] strArr = f12305d;
                if (flags < strArr.length) {
                    str = strArr[flags];
                    za.k.b(str);
                } else {
                    str = f12306e[flags];
                }
                String str2 = str;
                if (type == 5 && (flags & 4) != 0) {
                    z11 = StringsJVM.z(str2, "HEADERS", "PUSH_PROMISE", false, 4, null);
                    return z11;
                }
                if (type != 0 || (flags & 32) == 0) {
                    return str2;
                }
                z10 = StringsJVM.z(str2, "PRIORITY", "COMPRESSED", false, 4, null);
                return z10;
            }
        }
        return f12306e[flags];
    }

    public final String b(int type) {
        String[] strArr = f12304c;
        return type < strArr.length ? strArr[type] : ae.d.s("0x%02x", Integer.valueOf(type));
    }

    public final String c(boolean inbound, int streamId, int length, int type, int flags) {
        return ae.d.s("%s 0x%08x %5d %-13s %s", inbound ? "<<" : ">>", Integer.valueOf(streamId), Integer.valueOf(length), b(type), a(type, flags));
    }
}
