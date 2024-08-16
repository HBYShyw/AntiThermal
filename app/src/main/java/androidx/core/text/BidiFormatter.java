package androidx.core.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

/* compiled from: BidiFormatter.java */
/* renamed from: androidx.core.text.a, reason: use source file name */
/* loaded from: classes.dex */
public final class BidiFormatter {

    /* renamed from: d, reason: collision with root package name */
    static final TextDirectionHeuristicCompat f2267d;

    /* renamed from: e, reason: collision with root package name */
    private static final String f2268e;

    /* renamed from: f, reason: collision with root package name */
    private static final String f2269f;

    /* renamed from: g, reason: collision with root package name */
    static final BidiFormatter f2270g;

    /* renamed from: h, reason: collision with root package name */
    static final BidiFormatter f2271h;

    /* renamed from: a, reason: collision with root package name */
    private final boolean f2272a;

    /* renamed from: b, reason: collision with root package name */
    private final int f2273b;

    /* renamed from: c, reason: collision with root package name */
    private final TextDirectionHeuristicCompat f2274c;

    /* compiled from: BidiFormatter.java */
    /* renamed from: androidx.core.text.a$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private boolean f2275a;

        /* renamed from: b, reason: collision with root package name */
        private int f2276b;

        /* renamed from: c, reason: collision with root package name */
        private TextDirectionHeuristicCompat f2277c;

        public a() {
            c(BidiFormatter.e(Locale.getDefault()));
        }

        private static BidiFormatter b(boolean z10) {
            return z10 ? BidiFormatter.f2271h : BidiFormatter.f2270g;
        }

        private void c(boolean z10) {
            this.f2275a = z10;
            this.f2277c = BidiFormatter.f2267d;
            this.f2276b = 2;
        }

        public BidiFormatter a() {
            if (this.f2276b == 2 && this.f2277c == BidiFormatter.f2267d) {
                return b(this.f2275a);
            }
            return new BidiFormatter(this.f2275a, this.f2276b, this.f2277c);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: BidiFormatter.java */
    /* renamed from: androidx.core.text.a$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: f, reason: collision with root package name */
        private static final byte[] f2278f = new byte[1792];

        /* renamed from: a, reason: collision with root package name */
        private final CharSequence f2279a;

        /* renamed from: b, reason: collision with root package name */
        private final boolean f2280b;

        /* renamed from: c, reason: collision with root package name */
        private final int f2281c;

        /* renamed from: d, reason: collision with root package name */
        private int f2282d;

        /* renamed from: e, reason: collision with root package name */
        private char f2283e;

        static {
            for (int i10 = 0; i10 < 1792; i10++) {
                f2278f[i10] = Character.getDirectionality(i10);
            }
        }

        b(CharSequence charSequence, boolean z10) {
            this.f2279a = charSequence;
            this.f2280b = z10;
            this.f2281c = charSequence.length();
        }

        private static byte c(char c10) {
            return c10 < 1792 ? f2278f[c10] : Character.getDirectionality(c10);
        }

        private byte f() {
            char charAt;
            int i10 = this.f2282d;
            do {
                int i11 = this.f2282d;
                if (i11 <= 0) {
                    break;
                }
                CharSequence charSequence = this.f2279a;
                int i12 = i11 - 1;
                this.f2282d = i12;
                charAt = charSequence.charAt(i12);
                this.f2283e = charAt;
                if (charAt == '&') {
                    return (byte) 12;
                }
            } while (charAt != ';');
            this.f2282d = i10;
            this.f2283e = ';';
            return (byte) 13;
        }

        private byte g() {
            char charAt;
            do {
                int i10 = this.f2282d;
                if (i10 >= this.f2281c) {
                    return (byte) 12;
                }
                CharSequence charSequence = this.f2279a;
                this.f2282d = i10 + 1;
                charAt = charSequence.charAt(i10);
                this.f2283e = charAt;
            } while (charAt != ';');
            return (byte) 12;
        }

        private byte h() {
            char charAt;
            int i10 = this.f2282d;
            while (true) {
                int i11 = this.f2282d;
                if (i11 <= 0) {
                    break;
                }
                CharSequence charSequence = this.f2279a;
                int i12 = i11 - 1;
                this.f2282d = i12;
                char charAt2 = charSequence.charAt(i12);
                this.f2283e = charAt2;
                if (charAt2 == '<') {
                    return (byte) 12;
                }
                if (charAt2 == '>') {
                    break;
                }
                if (charAt2 == '\"' || charAt2 == '\'') {
                    do {
                        int i13 = this.f2282d;
                        if (i13 > 0) {
                            CharSequence charSequence2 = this.f2279a;
                            int i14 = i13 - 1;
                            this.f2282d = i14;
                            charAt = charSequence2.charAt(i14);
                            this.f2283e = charAt;
                        }
                    } while (charAt != charAt2);
                }
            }
            this.f2282d = i10;
            this.f2283e = '>';
            return (byte) 13;
        }

        private byte i() {
            char charAt;
            int i10 = this.f2282d;
            while (true) {
                int i11 = this.f2282d;
                if (i11 < this.f2281c) {
                    CharSequence charSequence = this.f2279a;
                    this.f2282d = i11 + 1;
                    char charAt2 = charSequence.charAt(i11);
                    this.f2283e = charAt2;
                    if (charAt2 == '>') {
                        return (byte) 12;
                    }
                    if (charAt2 == '\"' || charAt2 == '\'') {
                        do {
                            int i12 = this.f2282d;
                            if (i12 < this.f2281c) {
                                CharSequence charSequence2 = this.f2279a;
                                this.f2282d = i12 + 1;
                                charAt = charSequence2.charAt(i12);
                                this.f2283e = charAt;
                            }
                        } while (charAt != charAt2);
                    }
                } else {
                    this.f2282d = i10;
                    this.f2283e = '<';
                    return (byte) 13;
                }
            }
        }

        byte a() {
            char charAt = this.f2279a.charAt(this.f2282d - 1);
            this.f2283e = charAt;
            if (Character.isLowSurrogate(charAt)) {
                int codePointBefore = Character.codePointBefore(this.f2279a, this.f2282d);
                this.f2282d -= Character.charCount(codePointBefore);
                return Character.getDirectionality(codePointBefore);
            }
            this.f2282d--;
            byte c10 = c(this.f2283e);
            if (!this.f2280b) {
                return c10;
            }
            char c11 = this.f2283e;
            if (c11 == '>') {
                return h();
            }
            return c11 == ';' ? f() : c10;
        }

        byte b() {
            char charAt = this.f2279a.charAt(this.f2282d);
            this.f2283e = charAt;
            if (Character.isHighSurrogate(charAt)) {
                int codePointAt = Character.codePointAt(this.f2279a, this.f2282d);
                this.f2282d += Character.charCount(codePointAt);
                return Character.getDirectionality(codePointAt);
            }
            this.f2282d++;
            byte c10 = c(this.f2283e);
            if (!this.f2280b) {
                return c10;
            }
            char c11 = this.f2283e;
            if (c11 == '<') {
                return i();
            }
            return c11 == '&' ? g() : c10;
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:46:0x0045. Please report as an issue. */
        int d() {
            this.f2282d = 0;
            int i10 = 0;
            int i11 = 0;
            int i12 = 0;
            while (this.f2282d < this.f2281c && i10 == 0) {
                byte b10 = b();
                if (b10 != 0) {
                    if (b10 == 1 || b10 == 2) {
                        if (i12 == 0) {
                            return 1;
                        }
                    } else if (b10 != 9) {
                        switch (b10) {
                            case 14:
                            case 15:
                                i12++;
                                i11 = -1;
                                continue;
                            case 16:
                            case 17:
                                i12++;
                                i11 = 1;
                                continue;
                            case 18:
                                i12--;
                                i11 = 0;
                                continue;
                        }
                    }
                } else if (i12 == 0) {
                    return -1;
                }
                i10 = i12;
            }
            if (i10 == 0) {
                return 0;
            }
            if (i11 != 0) {
                return i11;
            }
            while (this.f2282d > 0) {
                switch (a()) {
                    case 14:
                    case 15:
                        if (i10 == i12) {
                            return -1;
                        }
                        i12--;
                    case 16:
                    case 17:
                        if (i10 == i12) {
                            return 1;
                        }
                        i12--;
                    case 18:
                        i12++;
                }
            }
            return 0;
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:33:0x001c. Please report as an issue. */
        int e() {
            this.f2282d = this.f2281c;
            int i10 = 0;
            while (true) {
                int i11 = i10;
                while (this.f2282d > 0) {
                    byte a10 = a();
                    if (a10 != 0) {
                        if (a10 == 1 || a10 == 2) {
                            if (i10 == 0) {
                                return 1;
                            }
                            if (i11 == 0) {
                                break;
                            }
                        } else if (a10 != 9) {
                            switch (a10) {
                                case 14:
                                case 15:
                                    if (i11 == i10) {
                                        return -1;
                                    }
                                    i10--;
                                    break;
                                case 16:
                                case 17:
                                    if (i11 == i10) {
                                        return 1;
                                    }
                                    i10--;
                                    break;
                                case 18:
                                    i10++;
                                    break;
                                default:
                                    if (i11 != 0) {
                                        break;
                                    } else {
                                        break;
                                    }
                            }
                        } else {
                            continue;
                        }
                    } else {
                        if (i10 == 0) {
                            return -1;
                        }
                        if (i11 == 0) {
                            break;
                        }
                    }
                }
                return 0;
            }
        }
    }

    static {
        TextDirectionHeuristicCompat textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.f2294c;
        f2267d = textDirectionHeuristicCompat;
        f2268e = Character.toString((char) 8206);
        f2269f = Character.toString((char) 8207);
        f2270g = new BidiFormatter(false, 2, textDirectionHeuristicCompat);
        f2271h = new BidiFormatter(true, 2, textDirectionHeuristicCompat);
    }

    BidiFormatter(boolean z10, int i10, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        this.f2272a = z10;
        this.f2273b = i10;
        this.f2274c = textDirectionHeuristicCompat;
    }

    private static int a(CharSequence charSequence) {
        return new b(charSequence, false).d();
    }

    private static int b(CharSequence charSequence) {
        return new b(charSequence, false).e();
    }

    public static BidiFormatter c() {
        return new a().a();
    }

    static boolean e(Locale locale) {
        return TextUtilsCompat.a(locale) == 1;
    }

    private String f(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        boolean a10 = textDirectionHeuristicCompat.a(charSequence, 0, charSequence.length());
        if (this.f2272a || !(a10 || b(charSequence) == 1)) {
            return this.f2272a ? (!a10 || b(charSequence) == -1) ? f2269f : "" : "";
        }
        return f2268e;
    }

    private String g(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        boolean a10 = textDirectionHeuristicCompat.a(charSequence, 0, charSequence.length());
        if (this.f2272a || !(a10 || a(charSequence) == 1)) {
            return this.f2272a ? (!a10 || a(charSequence) == -1) ? f2269f : "" : "";
        }
        return f2268e;
    }

    public boolean d() {
        return (this.f2273b & 2) != 0;
    }

    public CharSequence h(CharSequence charSequence) {
        return i(charSequence, this.f2274c, true);
    }

    public CharSequence i(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat, boolean z10) {
        if (charSequence == null) {
            return null;
        }
        boolean a10 = textDirectionHeuristicCompat.a(charSequence, 0, charSequence.length());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (d() && z10) {
            spannableStringBuilder.append((CharSequence) g(charSequence, a10 ? TextDirectionHeuristicsCompat.f2293b : TextDirectionHeuristicsCompat.f2292a));
        }
        if (a10 != this.f2272a) {
            spannableStringBuilder.append(a10 ? (char) 8235 : (char) 8234);
            spannableStringBuilder.append(charSequence);
            spannableStringBuilder.append((char) 8236);
        } else {
            spannableStringBuilder.append(charSequence);
        }
        if (z10) {
            spannableStringBuilder.append((CharSequence) f(charSequence, a10 ? TextDirectionHeuristicsCompat.f2293b : TextDirectionHeuristicsCompat.f2292a));
        }
        return spannableStringBuilder;
    }

    public String j(String str) {
        return k(str, this.f2274c, true);
    }

    public String k(String str, TextDirectionHeuristicCompat textDirectionHeuristicCompat, boolean z10) {
        if (str == null) {
            return null;
        }
        return i(str, textDirectionHeuristicCompat, z10).toString();
    }
}
