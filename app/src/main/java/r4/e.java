package r4;

import java.io.EOFException;
import java.util.Objects;
import me.BufferedSource;
import me.g;
import r4.c;

/* compiled from: JsonUtf8Reader.java */
/* loaded from: classes.dex */
final class e extends c {

    /* renamed from: r, reason: collision with root package name */
    private static final g f17503r = g.d("'\\");

    /* renamed from: s, reason: collision with root package name */
    private static final g f17504s = g.d("\"\\");

    /* renamed from: t, reason: collision with root package name */
    private static final g f17505t = g.d("{}[]:, \n\t\r\f/\\;#=");

    /* renamed from: u, reason: collision with root package name */
    private static final g f17506u = g.d("\n\r");

    /* renamed from: v, reason: collision with root package name */
    private static final g f17507v = g.d("*/");

    /* renamed from: l, reason: collision with root package name */
    private final BufferedSource f17508l;

    /* renamed from: m, reason: collision with root package name */
    private final me.d f17509m;

    /* renamed from: n, reason: collision with root package name */
    private int f17510n = 0;

    /* renamed from: o, reason: collision with root package name */
    private long f17511o;

    /* renamed from: p, reason: collision with root package name */
    private int f17512p;

    /* renamed from: q, reason: collision with root package name */
    private String f17513q;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(BufferedSource bufferedSource) {
        Objects.requireNonNull(bufferedSource, "source == null");
        this.f17508l = bufferedSource;
        this.f17509m = bufferedSource.g();
        h0(6);
    }

    private String A0() {
        long g02 = this.f17508l.g0(f17505t);
        me.d dVar = this.f17509m;
        return g02 != -1 ? dVar.t0(g02) : dVar.o0();
    }

    private int B0() {
        int i10;
        String str;
        String str2;
        byte L = this.f17509m.L(0L);
        if (L == 116 || L == 84) {
            i10 = 5;
            str = "true";
            str2 = "TRUE";
        } else if (L == 102 || L == 70) {
            i10 = 6;
            str = "false";
            str2 = "FALSE";
        } else {
            if (L != 110 && L != 78) {
                return 0;
            }
            i10 = 7;
            str = "null";
            str2 = "NULL";
        }
        int length = str.length();
        int i11 = 1;
        while (i11 < length) {
            int i12 = i11 + 1;
            if (!this.f17508l.W(i12)) {
                return 0;
            }
            byte L2 = this.f17509m.L(i11);
            if (L2 != str.charAt(i11) && L2 != str2.charAt(i11)) {
                return 0;
            }
            i11 = i12;
        }
        if (this.f17508l.W(length + 1) && x0(this.f17509m.L(length))) {
            return 0;
        }
        this.f17509m.V(length);
        this.f17510n = i10;
        return i10;
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x0082, code lost:
    
        if (x0(r11) != false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0084, code lost:
    
        if (r6 != 2) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0086, code lost:
    
        if (r7 == false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x008c, code lost:
    
        if (r8 != Long.MIN_VALUE) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x008e, code lost:
    
        if (r10 == false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0092, code lost:
    
        if (r8 != 0) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0094, code lost:
    
        if (r10 != false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0096, code lost:
    
        if (r10 == false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0099, code lost:
    
        r8 = -r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x009a, code lost:
    
        r16.f17511o = r8;
        r16.f17509m.V(r5);
        r16.f17510n = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00a6, code lost:
    
        return 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00a7, code lost:
    
        if (r6 == 2) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00aa, code lost:
    
        if (r6 == 4) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00ad, code lost:
    
        if (r6 != 7) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00b0, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00b2, code lost:
    
        r16.f17512p = r5;
        r16.f17510n = 17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00b8, code lost:
    
        return 17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00b9, code lost:
    
        return 0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int C0() {
        char c10;
        boolean z10 = true;
        int i10 = 0;
        long j10 = 0;
        boolean z11 = true;
        int i11 = 0;
        char c11 = 0;
        boolean z12 = false;
        while (true) {
            int i12 = i11 + 1;
            if (!this.f17508l.W(i12)) {
                break;
            }
            byte L = this.f17509m.L(i11);
            if (L != 43) {
                if (L == 69 || L == 101) {
                    if (c11 != 2 && c11 != 4) {
                        return i10;
                    }
                    c11 = 5;
                } else if (L == 45) {
                    c10 = 6;
                    if (c11 == 0) {
                        c11 = 1;
                        z12 = true;
                    } else if (c11 != 5) {
                        return i10;
                    }
                } else if (L == 46) {
                    c10 = 3;
                    if (c11 != 2) {
                        return i10;
                    }
                } else {
                    if (L < 48 || L > 57) {
                        break;
                    }
                    if (c11 == z10 || c11 == 0) {
                        j10 = -(L - 48);
                        c11 = 2;
                    } else if (c11 == 2) {
                        if (j10 == 0) {
                            return i10;
                        }
                        long j11 = (10 * j10) - (L - 48);
                        z11 &= j10 > -922337203685477580L || (j10 == -922337203685477580L && j11 < j10);
                        j10 = j11;
                    } else if (c11 == 3) {
                        i10 = 0;
                        c11 = 4;
                    } else if (c11 == 5 || c11 == 6) {
                        i10 = 0;
                        c11 = 7;
                    }
                    i10 = 0;
                }
                i11 = i12;
                z10 = true;
            } else {
                c10 = 6;
                if (c11 != 5) {
                    return i10;
                }
            }
            c11 = c10;
            i11 = i12;
            z10 = true;
        }
    }

    private char D0() {
        int i10;
        int i11;
        if (this.f17508l.W(1L)) {
            byte M = this.f17509m.M();
            if (M == 10 || M == 34 || M == 39 || M == 47 || M == 92) {
                return (char) M;
            }
            if (M == 98) {
                return '\b';
            }
            if (M == 102) {
                return '\f';
            }
            if (M == 110) {
                return '\n';
            }
            if (M == 114) {
                return '\r';
            }
            if (M == 116) {
                return '\t';
            }
            if (M != 117) {
                if (this.f17488i) {
                    return (char) M;
                }
                throw t0("Invalid escape sequence: \\" + ((char) M));
            }
            if (this.f17508l.W(4L)) {
                char c10 = 0;
                for (int i12 = 0; i12 < 4; i12++) {
                    byte L = this.f17509m.L(i12);
                    char c11 = (char) (c10 << 4);
                    if (L < 48 || L > 57) {
                        if (L >= 97 && L <= 102) {
                            i10 = L - 97;
                        } else {
                            if (L < 65 || L > 70) {
                                throw t0("\\u" + this.f17509m.t0(4L));
                            }
                            i10 = L - 65;
                        }
                        i11 = i10 + 10;
                    } else {
                        i11 = L - 48;
                    }
                    c10 = (char) (c11 + i11);
                }
                this.f17509m.V(4L);
                return c10;
            }
            throw new EOFException("Unterminated escape sequence at path " + getPath());
        }
        throw t0("Unterminated escape sequence");
    }

    private void E0(g gVar) {
        while (true) {
            long g02 = this.f17508l.g0(gVar);
            if (g02 != -1) {
                if (this.f17509m.L(g02) == 92) {
                    this.f17509m.V(g02 + 1);
                    D0();
                } else {
                    this.f17509m.V(g02 + 1);
                    return;
                }
            } else {
                throw t0("Unterminated string");
            }
        }
    }

    private boolean F0() {
        long I = this.f17508l.I(f17507v);
        boolean z10 = I != -1;
        me.d dVar = this.f17509m;
        dVar.V(z10 ? I + r1.t() : dVar.v0());
        return z10;
    }

    private void G0() {
        long g02 = this.f17508l.g0(f17506u);
        me.d dVar = this.f17509m;
        dVar.V(g02 != -1 ? g02 + 1 : dVar.v0());
    }

    private void H0() {
        long g02 = this.f17508l.g0(f17505t);
        me.d dVar = this.f17509m;
        if (g02 == -1) {
            g02 = dVar.v0();
        }
        dVar.V(g02);
    }

    private void u0() {
        if (!this.f17488i) {
            throw t0("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private int v0() {
        int[] iArr = this.f17485f;
        int i10 = this.f17484e;
        int i11 = iArr[i10 - 1];
        if (i11 == 1) {
            iArr[i10 - 1] = 2;
        } else if (i11 == 2) {
            int y02 = y0(true);
            this.f17509m.M();
            if (y02 != 44) {
                if (y02 != 59) {
                    if (y02 == 93) {
                        this.f17510n = 4;
                        return 4;
                    }
                    throw t0("Unterminated array");
                }
                u0();
            }
        } else {
            if (i11 == 3 || i11 == 5) {
                iArr[i10 - 1] = 4;
                if (i11 == 5) {
                    int y03 = y0(true);
                    this.f17509m.M();
                    if (y03 != 44) {
                        if (y03 != 59) {
                            if (y03 == 125) {
                                this.f17510n = 2;
                                return 2;
                            }
                            throw t0("Unterminated object");
                        }
                        u0();
                    }
                }
                int y04 = y0(true);
                if (y04 == 34) {
                    this.f17509m.M();
                    this.f17510n = 13;
                    return 13;
                }
                if (y04 == 39) {
                    this.f17509m.M();
                    u0();
                    this.f17510n = 12;
                    return 12;
                }
                if (y04 != 125) {
                    u0();
                    if (x0((char) y04)) {
                        this.f17510n = 14;
                        return 14;
                    }
                    throw t0("Expected name");
                }
                if (i11 != 5) {
                    this.f17509m.M();
                    this.f17510n = 2;
                    return 2;
                }
                throw t0("Expected name");
            }
            if (i11 == 4) {
                iArr[i10 - 1] = 5;
                int y05 = y0(true);
                this.f17509m.M();
                if (y05 != 58) {
                    if (y05 == 61) {
                        u0();
                        if (this.f17508l.W(1L) && this.f17509m.L(0L) == 62) {
                            this.f17509m.M();
                        }
                    } else {
                        throw t0("Expected ':'");
                    }
                }
            } else if (i11 == 6) {
                iArr[i10 - 1] = 7;
            } else if (i11 == 7) {
                if (y0(false) == -1) {
                    this.f17510n = 18;
                    return 18;
                }
                u0();
            } else if (i11 == 8) {
                throw new IllegalStateException("JsonReader is closed");
            }
        }
        int y06 = y0(true);
        if (y06 == 34) {
            this.f17509m.M();
            this.f17510n = 9;
            return 9;
        }
        if (y06 == 39) {
            u0();
            this.f17509m.M();
            this.f17510n = 8;
            return 8;
        }
        if (y06 != 44 && y06 != 59) {
            if (y06 == 91) {
                this.f17509m.M();
                this.f17510n = 3;
                return 3;
            }
            if (y06 != 93) {
                if (y06 != 123) {
                    int B0 = B0();
                    if (B0 != 0) {
                        return B0;
                    }
                    int C0 = C0();
                    if (C0 != 0) {
                        return C0;
                    }
                    if (x0(this.f17509m.L(0L))) {
                        u0();
                        this.f17510n = 10;
                        return 10;
                    }
                    throw t0("Expected value");
                }
                this.f17509m.M();
                this.f17510n = 1;
                return 1;
            }
            if (i11 == 1) {
                this.f17509m.M();
                this.f17510n = 4;
                return 4;
            }
        }
        if (i11 != 1 && i11 != 2) {
            throw t0("Unexpected value");
        }
        u0();
        this.f17510n = 7;
        return 7;
    }

    private int w0(String str, c.a aVar) {
        int length = aVar.f17490a.length;
        for (int i10 = 0; i10 < length; i10++) {
            if (str.equals(aVar.f17490a[i10])) {
                this.f17510n = 0;
                this.f17486g[this.f17484e - 1] = str;
                return i10;
            }
        }
        return -1;
    }

    private boolean x0(int i10) {
        if (i10 == 9 || i10 == 10 || i10 == 12 || i10 == 13 || i10 == 32) {
            return false;
        }
        if (i10 != 35) {
            if (i10 == 44) {
                return false;
            }
            if (i10 != 47 && i10 != 61) {
                if (i10 == 123 || i10 == 125 || i10 == 58) {
                    return false;
                }
                if (i10 != 59) {
                    switch (i10) {
                        case 91:
                        case 93:
                            return false;
                        case 92:
                            break;
                        default:
                            return true;
                    }
                }
            }
        }
        u0();
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0025, code lost:
    
        r6.f17509m.V(r3 - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002f, code lost:
    
        if (r1 != 47) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0076, code lost:
    
        if (r1 != 35) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0078, code lost:
    
        u0();
        G0();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x007f, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0039, code lost:
    
        if (r6.f17508l.W(2) != false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x003c, code lost:
    
        u0();
        r3 = r6.f17509m.L(1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0049, code lost:
    
        if (r3 == 42) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x005c, code lost:
    
        r6.f17509m.M();
        r6.f17509m.M();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006a, code lost:
    
        if (F0() == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0073, code lost:
    
        throw t0("Unterminated comment");
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x004b, code lost:
    
        if (r3 == 47) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x004e, code lost:
    
        r6.f17509m.M();
        r6.f17509m.M();
        G0();
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x004d, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x003b, code lost:
    
        return r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int y0(boolean z10) {
        while (true) {
            int i10 = 0;
            while (true) {
                int i11 = i10 + 1;
                if (!this.f17508l.W(i11)) {
                    if (z10) {
                        throw new EOFException("End of input");
                    }
                    return -1;
                }
                byte L = this.f17509m.L(i10);
                if (L != 10 && L != 32 && L != 13 && L != 9) {
                    break;
                }
                i10 = i11;
            }
        }
    }

    private String z0(g gVar) {
        StringBuilder sb2 = null;
        while (true) {
            long g02 = this.f17508l.g0(gVar);
            if (g02 != -1) {
                if (this.f17509m.L(g02) != 92) {
                    if (sb2 == null) {
                        String t02 = this.f17509m.t0(g02);
                        this.f17509m.M();
                        return t02;
                    }
                    sb2.append(this.f17509m.t0(g02));
                    this.f17509m.M();
                    return sb2.toString();
                }
                if (sb2 == null) {
                    sb2 = new StringBuilder();
                }
                sb2.append(this.f17509m.t0(g02));
                this.f17509m.M();
                sb2.append(D0());
            } else {
                throw t0("Unterminated string");
            }
        }
    }

    @Override // r4.c
    public boolean L() {
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 == 5) {
            this.f17510n = 0;
            int[] iArr = this.f17487h;
            int i11 = this.f17484e - 1;
            iArr[i11] = iArr[i11] + 1;
            return true;
        }
        if (i10 == 6) {
            this.f17510n = 0;
            int[] iArr2 = this.f17487h;
            int i12 = this.f17484e - 1;
            iArr2[i12] = iArr2[i12] + 1;
            return false;
        }
        throw new a("Expected a boolean but was " + e0() + " at path " + getPath());
    }

    @Override // r4.c
    public double N() {
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 == 16) {
            this.f17510n = 0;
            int[] iArr = this.f17487h;
            int i11 = this.f17484e - 1;
            iArr[i11] = iArr[i11] + 1;
            return this.f17511o;
        }
        if (i10 == 17) {
            this.f17513q = this.f17509m.t0(this.f17512p);
        } else if (i10 == 9) {
            this.f17513q = z0(f17504s);
        } else if (i10 == 8) {
            this.f17513q = z0(f17503r);
        } else if (i10 == 10) {
            this.f17513q = A0();
        } else if (i10 != 11) {
            throw new a("Expected a double but was " + e0() + " at path " + getPath());
        }
        this.f17510n = 11;
        try {
            double parseDouble = Double.parseDouble(this.f17513q);
            if (!this.f17488i && (Double.isNaN(parseDouble) || Double.isInfinite(parseDouble))) {
                throw new b("JSON forbids NaN and infinities: " + parseDouble + " at path " + getPath());
            }
            this.f17513q = null;
            this.f17510n = 0;
            int[] iArr2 = this.f17487h;
            int i12 = this.f17484e - 1;
            iArr2[i12] = iArr2[i12] + 1;
            return parseDouble;
        } catch (NumberFormatException unused) {
            throw new a("Expected a double but was " + this.f17513q + " at path " + getPath());
        }
    }

    @Override // r4.c
    public int S() {
        String z02;
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 == 16) {
            long j10 = this.f17511o;
            int i11 = (int) j10;
            if (j10 == i11) {
                this.f17510n = 0;
                int[] iArr = this.f17487h;
                int i12 = this.f17484e - 1;
                iArr[i12] = iArr[i12] + 1;
                return i11;
            }
            throw new a("Expected an int but was " + this.f17511o + " at path " + getPath());
        }
        if (i10 == 17) {
            this.f17513q = this.f17509m.t0(this.f17512p);
        } else if (i10 == 9 || i10 == 8) {
            if (i10 == 9) {
                z02 = z0(f17504s);
            } else {
                z02 = z0(f17503r);
            }
            this.f17513q = z02;
            try {
                int parseInt = Integer.parseInt(z02);
                this.f17510n = 0;
                int[] iArr2 = this.f17487h;
                int i13 = this.f17484e - 1;
                iArr2[i13] = iArr2[i13] + 1;
                return parseInt;
            } catch (NumberFormatException unused) {
            }
        } else if (i10 != 11) {
            throw new a("Expected an int but was " + e0() + " at path " + getPath());
        }
        this.f17510n = 11;
        try {
            double parseDouble = Double.parseDouble(this.f17513q);
            int i14 = (int) parseDouble;
            if (i14 == parseDouble) {
                this.f17513q = null;
                this.f17510n = 0;
                int[] iArr3 = this.f17487h;
                int i15 = this.f17484e - 1;
                iArr3[i15] = iArr3[i15] + 1;
                return i14;
            }
            throw new a("Expected an int but was " + this.f17513q + " at path " + getPath());
        } catch (NumberFormatException unused2) {
            throw new a("Expected an int but was " + this.f17513q + " at path " + getPath());
        }
    }

    @Override // r4.c
    public String U() {
        String str;
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 == 14) {
            str = A0();
        } else if (i10 == 13) {
            str = z0(f17504s);
        } else if (i10 == 12) {
            str = z0(f17503r);
        } else if (i10 == 15) {
            str = this.f17513q;
        } else {
            throw new a("Expected a name but was " + e0() + " at path " + getPath());
        }
        this.f17510n = 0;
        this.f17486g[this.f17484e - 1] = str;
        return str;
    }

    @Override // r4.c
    public String X() {
        String t02;
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 == 10) {
            t02 = A0();
        } else if (i10 == 9) {
            t02 = z0(f17504s);
        } else if (i10 == 8) {
            t02 = z0(f17503r);
        } else if (i10 == 11) {
            t02 = this.f17513q;
            this.f17513q = null;
        } else if (i10 == 16) {
            t02 = Long.toString(this.f17511o);
        } else if (i10 == 17) {
            t02 = this.f17509m.t0(this.f17512p);
        } else {
            throw new a("Expected a string but was " + e0() + " at path " + getPath());
        }
        this.f17510n = 0;
        int[] iArr = this.f17487h;
        int i11 = this.f17484e - 1;
        iArr[i11] = iArr[i11] + 1;
        return t02;
    }

    @Override // r4.c
    public void c() {
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 == 3) {
            h0(1);
            this.f17487h[this.f17484e - 1] = 0;
            this.f17510n = 0;
        } else {
            throw new a("Expected BEGIN_ARRAY but was " + e0() + " at path " + getPath());
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f17510n = 0;
        this.f17485f[0] = 8;
        this.f17484e = 1;
        this.f17509m.b();
        this.f17508l.close();
    }

    @Override // r4.c
    public c.b e0() {
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        switch (i10) {
            case 1:
                return c.b.BEGIN_OBJECT;
            case 2:
                return c.b.END_OBJECT;
            case 3:
                return c.b.BEGIN_ARRAY;
            case 4:
                return c.b.END_ARRAY;
            case 5:
            case 6:
                return c.b.BOOLEAN;
            case 7:
                return c.b.NULL;
            case 8:
            case 9:
            case 10:
            case 11:
                return c.b.STRING;
            case 12:
            case 13:
            case 14:
            case 15:
                return c.b.NAME;
            case 16:
            case 17:
                return c.b.NUMBER;
            case 18:
                return c.b.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    @Override // r4.c
    public int i0(c.a aVar) {
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 < 12 || i10 > 15) {
            return -1;
        }
        if (i10 == 15) {
            return w0(this.f17513q, aVar);
        }
        int z10 = this.f17508l.z(aVar.f17491b);
        if (z10 != -1) {
            this.f17510n = 0;
            this.f17486g[this.f17484e - 1] = aVar.f17490a[z10];
            return z10;
        }
        String str = this.f17486g[this.f17484e - 1];
        String U = U();
        int w02 = w0(U, aVar);
        if (w02 == -1) {
            this.f17510n = 15;
            this.f17513q = U;
            this.f17486g[this.f17484e - 1] = str;
        }
        return w02;
    }

    @Override // r4.c
    public void j0() {
        if (!this.f17489j) {
            int i10 = this.f17510n;
            if (i10 == 0) {
                i10 = v0();
            }
            if (i10 == 14) {
                H0();
            } else if (i10 == 13) {
                E0(f17504s);
            } else if (i10 == 12) {
                E0(f17503r);
            } else if (i10 != 15) {
                throw new a("Expected a name but was " + e0() + " at path " + getPath());
            }
            this.f17510n = 0;
            this.f17486g[this.f17484e - 1] = "null";
            return;
        }
        throw new a("Cannot skip unexpected " + e0() + " at " + getPath());
    }

    @Override // r4.c
    public void m() {
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 == 1) {
            h0(3);
            this.f17510n = 0;
            return;
        }
        throw new a("Expected BEGIN_OBJECT but was " + e0() + " at path " + getPath());
    }

    @Override // r4.c
    public void m0() {
        if (!this.f17489j) {
            int i10 = 0;
            do {
                int i11 = this.f17510n;
                if (i11 == 0) {
                    i11 = v0();
                }
                if (i11 == 3) {
                    h0(1);
                } else if (i11 == 1) {
                    h0(3);
                } else {
                    if (i11 == 4) {
                        i10--;
                        if (i10 >= 0) {
                            this.f17484e--;
                        } else {
                            throw new a("Expected a value but was " + e0() + " at path " + getPath());
                        }
                    } else if (i11 == 2) {
                        i10--;
                        if (i10 >= 0) {
                            this.f17484e--;
                        } else {
                            throw new a("Expected a value but was " + e0() + " at path " + getPath());
                        }
                    } else if (i11 == 14 || i11 == 10) {
                        H0();
                    } else if (i11 == 9 || i11 == 13) {
                        E0(f17504s);
                    } else if (i11 == 8 || i11 == 12) {
                        E0(f17503r);
                    } else if (i11 == 17) {
                        this.f17509m.V(this.f17512p);
                    } else if (i11 == 18) {
                        throw new a("Expected a value but was " + e0() + " at path " + getPath());
                    }
                    this.f17510n = 0;
                }
                i10++;
                this.f17510n = 0;
            } while (i10 != 0);
            int[] iArr = this.f17487h;
            int i12 = this.f17484e;
            int i13 = i12 - 1;
            iArr[i13] = iArr[i13] + 1;
            this.f17486g[i12 - 1] = "null";
            return;
        }
        throw new a("Cannot skip unexpected " + e0() + " at " + getPath());
    }

    public String toString() {
        return "JsonReader(" + this.f17508l + ")";
    }

    @Override // r4.c
    public void u() {
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 == 4) {
            int i11 = this.f17484e - 1;
            this.f17484e = i11;
            int[] iArr = this.f17487h;
            int i12 = i11 - 1;
            iArr[i12] = iArr[i12] + 1;
            this.f17510n = 0;
            return;
        }
        throw new a("Expected END_ARRAY but was " + e0() + " at path " + getPath());
    }

    @Override // r4.c
    public void v() {
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        if (i10 == 2) {
            int i11 = this.f17484e - 1;
            this.f17484e = i11;
            this.f17486g[i11] = null;
            int[] iArr = this.f17487h;
            int i12 = i11 - 1;
            iArr[i12] = iArr[i12] + 1;
            this.f17510n = 0;
            return;
        }
        throw new a("Expected END_OBJECT but was " + e0() + " at path " + getPath());
    }

    @Override // r4.c
    public boolean w() {
        int i10 = this.f17510n;
        if (i10 == 0) {
            i10 = v0();
        }
        return (i10 == 2 || i10 == 4 || i10 == 18) ? false : true;
    }
}
