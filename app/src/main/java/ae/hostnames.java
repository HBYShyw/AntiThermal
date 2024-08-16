package ae;

import java.net.IDN;
import java.net.InetAddress;
import java.util.Locale;
import kotlin.Metadata;
import sd.StringsJVM;
import sd.v;
import za.k;

/* compiled from: hostnames.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\u001a\f\u0010\u0001\u001a\u0004\u0018\u00010\u0000*\u00020\u0000\u001a\f\u0010\u0003\u001a\u00020\u0002*\u00020\u0000H\u0002\u001a\"\u0010\t\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0002\u001a0\u0010\r\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\u0005H\u0002\u001a\u0010\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\nH\u0002¨\u0006\u000f"}, d2 = {"", "e", "", "a", "input", "", "pos", "limit", "Ljava/net/InetAddress;", "c", "", "address", "addressOffset", "b", "d", "okhttp"}, k = 2, mv = {1, 6, 0})
/* renamed from: ae.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class hostnames {
    private static final boolean a(String str) {
        int U;
        int length = str.length();
        int i10 = 0;
        while (i10 < length) {
            int i11 = i10 + 1;
            char charAt = str.charAt(i10);
            if (k.f(charAt, 31) <= 0 || k.f(charAt, 127) >= 0) {
                return true;
            }
            U = v.U(" #%/:?@[\\]", charAt, 0, false, 6, null);
            if (U != -1) {
                return true;
            }
            i10 = i11;
        }
        return false;
    }

    private static final boolean b(String str, int i10, int i11, byte[] bArr, int i12) {
        int i13 = i12;
        while (i10 < i11) {
            if (i13 == bArr.length) {
                return false;
            }
            if (i13 != i12) {
                if (str.charAt(i10) != '.') {
                    return false;
                }
                i10++;
            }
            int i14 = i10;
            int i15 = 0;
            while (i14 < i11) {
                char charAt = str.charAt(i14);
                if (k.f(charAt, 48) < 0 || k.f(charAt, 57) > 0) {
                    break;
                }
                if ((i15 == 0 && i10 != i14) || (i15 = ((i15 * 10) + charAt) - 48) > 255) {
                    return false;
                }
                i14++;
            }
            if (i14 - i10 == 0) {
                return false;
            }
            bArr[i13] = (byte) i15;
            i13++;
            i10 = i14;
        }
        return i13 == i12 + 4;
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0097, code lost:
    
        if (r13 == 16) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0099, code lost:
    
        if (r14 != (-1)) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x009b, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x009c, code lost:
    
        r0 = r13 - r14;
        java.lang.System.arraycopy(r9, r14, r9, 16 - r0, r0);
        java.util.Arrays.fill(r9, r14, (16 - r13) + r14, (byte) 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00ad, code lost:
    
        return java.net.InetAddress.getByAddress(r9);
     */
    /* JADX WARN: Removed duplicated region for block: B:15:0x006b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static final InetAddress c(String str, int i10, int i11) {
        boolean C;
        boolean C2;
        int i12;
        boolean C3;
        byte[] bArr = new byte[16];
        int i13 = i10;
        int i14 = -1;
        int i15 = -1;
        int i16 = 0;
        while (true) {
            if (i13 < i11) {
                if (i16 != 16) {
                    int i17 = i13 + 2;
                    if (i17 <= i11) {
                        C3 = StringsJVM.C(str, "::", i13, false, 4, null);
                        if (C3) {
                            if (i14 != -1) {
                                return null;
                            }
                            i16 += 2;
                            if (i17 == i11) {
                                i14 = i16;
                                break;
                            }
                            i15 = i17;
                            i14 = i16;
                            i13 = i15;
                            int i18 = 0;
                            while (i13 < i11) {
                                int G = d.G(str.charAt(i13));
                                if (G == -1) {
                                    break;
                                }
                                i18 = (i18 << 4) + G;
                                i13++;
                            }
                            i12 = i13 - i15;
                            if (i12 == 0 || i12 > 4) {
                                break;
                            }
                            int i19 = i16 + 1;
                            bArr[i16] = (byte) ((i18 >>> 8) & 255);
                            i16 = i19 + 1;
                            bArr[i19] = (byte) (i18 & 255);
                        }
                    }
                    if (i16 != 0) {
                        C = StringsJVM.C(str, ":", i13, false, 4, null);
                        if (C) {
                            i13++;
                        } else {
                            C2 = StringsJVM.C(str, ".", i13, false, 4, null);
                            if (!C2 || !b(str, i15, i11, bArr, i16 - 2)) {
                                return null;
                            }
                            i16 += 2;
                        }
                    }
                    i15 = i13;
                    i13 = i15;
                    int i182 = 0;
                    while (i13 < i11) {
                    }
                    i12 = i13 - i15;
                    if (i12 == 0) {
                        break;
                    }
                    break;
                }
                return null;
            }
            break;
        }
        return null;
    }

    private static final String d(byte[] bArr) {
        int i10 = 0;
        int i11 = -1;
        int i12 = 0;
        int i13 = 0;
        while (i12 < bArr.length) {
            int i14 = i12;
            while (i14 < 16 && bArr[i14] == 0 && bArr[i14 + 1] == 0) {
                i14 += 2;
            }
            int i15 = i14 - i12;
            if (i15 > i13 && i15 >= 4) {
                i11 = i12;
                i13 = i15;
            }
            i12 = i14 + 2;
        }
        me.d dVar = new me.d();
        while (i10 < bArr.length) {
            if (i10 == i11) {
                dVar.t(58);
                i10 += i13;
                if (i10 == 16) {
                    dVar.t(58);
                }
            } else {
                if (i10 > 0) {
                    dVar.t(58);
                }
                dVar.T((d.d(bArr[i10], 255) << 8) | d.d(bArr[i10 + 1], 255));
                i10 += 2;
            }
        }
        return dVar.o0();
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0035 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final String e(String str) {
        boolean I;
        boolean D;
        InetAddress c10;
        boolean q10;
        k.e(str, "<this>");
        I = v.I(str, ":", false, 2, null);
        if (I) {
            D = StringsJVM.D(str, "[", false, 2, null);
            if (D) {
                q10 = StringsJVM.q(str, "]", false, 2, null);
                if (q10) {
                    c10 = c(str, 1, str.length() - 1);
                    if (c10 != null) {
                        return null;
                    }
                    byte[] address = c10.getAddress();
                    if (address.length == 16) {
                        k.d(address, "address");
                        return d(address);
                    }
                    if (address.length == 4) {
                        return c10.getHostAddress();
                    }
                    throw new AssertionError("Invalid IPv6 address: '" + str + '\'');
                }
            }
            c10 = c(str, 0, str.length());
            if (c10 != null) {
            }
        } else {
            try {
                String ascii = IDN.toASCII(str);
                k.d(ascii, "toASCII(host)");
                Locale locale = Locale.US;
                k.d(locale, "US");
                String lowerCase = ascii.toLowerCase(locale);
                k.d(lowerCase, "this as java.lang.String).toLowerCase(locale)");
                if (lowerCase.length() == 0) {
                    return null;
                }
                if (a(lowerCase)) {
                    return null;
                }
                return lowerCase;
            } catch (IllegalArgumentException unused) {
                return null;
            }
        }
    }
}
