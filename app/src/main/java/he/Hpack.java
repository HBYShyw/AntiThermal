package he;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections._ArraysJvm;
import kotlin.collections._Collections;
import me.BufferedSource;
import me.Source;
import za.DefaultConstructorMarker;

/* compiled from: Hpack.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u000b\bÆ\u0002\u0018\u00002\u00020\u0001:\u0002\u0007\u0010B\t\b\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u0014\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00040\u0002H\u0002J\u000e\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003R\u001d\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\b8\u0006¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR#\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00040\u00028\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0014"}, d2 = {"Lhe/d;", "", "", "Lme/g;", "", "d", "name", "a", "", "Lhe/c;", "STATIC_HEADER_TABLE", "[Lhe/c;", "c", "()[Lhe/c;", "NAME_TO_FIRST_INDEX", "Ljava/util/Map;", "b", "()Ljava/util/Map;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class Hpack {

    /* renamed from: a, reason: collision with root package name */
    public static final Hpack f12281a;

    /* renamed from: b, reason: collision with root package name */
    private static final Header[] f12282b;

    /* renamed from: c, reason: collision with root package name */
    private static final Map<me.g, Integer> f12283c;

    static {
        Hpack hpack = new Hpack();
        f12281a = hpack;
        me.g gVar = Header.f12274g;
        me.g gVar2 = Header.f12275h;
        me.g gVar3 = Header.f12276i;
        me.g gVar4 = Header.f12273f;
        f12282b = new Header[]{new Header(Header.f12277j, ""), new Header(gVar, "GET"), new Header(gVar, "POST"), new Header(gVar2, "/"), new Header(gVar2, "/index.html"), new Header(gVar3, "http"), new Header(gVar3, "https"), new Header(gVar4, "200"), new Header(gVar4, "204"), new Header(gVar4, "206"), new Header(gVar4, "304"), new Header(gVar4, "400"), new Header(gVar4, "404"), new Header(gVar4, "500"), new Header("accept-charset", ""), new Header("accept-encoding", "gzip, deflate"), new Header("accept-language", ""), new Header("accept-ranges", ""), new Header("accept", ""), new Header("access-control-allow-origin", ""), new Header("age", ""), new Header("allow", ""), new Header("authorization", ""), new Header("cache-control", ""), new Header("content-disposition", ""), new Header("content-encoding", ""), new Header("content-language", ""), new Header("content-length", ""), new Header("content-location", ""), new Header("content-range", ""), new Header("content-type", ""), new Header("cookie", ""), new Header("date", ""), new Header("etag", ""), new Header("expect", ""), new Header("expires", ""), new Header("from", ""), new Header("host", ""), new Header("if-match", ""), new Header("if-modified-since", ""), new Header("if-none-match", ""), new Header("if-range", ""), new Header("if-unmodified-since", ""), new Header("last-modified", ""), new Header("link", ""), new Header("location", ""), new Header("max-forwards", ""), new Header("proxy-authenticate", ""), new Header("proxy-authorization", ""), new Header("range", ""), new Header("referer", ""), new Header("refresh", ""), new Header("retry-after", ""), new Header("server", ""), new Header("set-cookie", ""), new Header("strict-transport-security", ""), new Header("transfer-encoding", ""), new Header("user-agent", ""), new Header("vary", ""), new Header("via", ""), new Header("www-authenticate", "")};
        f12283c = hpack.d();
    }

    private Hpack() {
    }

    private final Map<me.g, Integer> d() {
        Header[] headerArr = f12282b;
        LinkedHashMap linkedHashMap = new LinkedHashMap(headerArr.length);
        int length = headerArr.length;
        int i10 = 0;
        while (i10 < length) {
            int i11 = i10 + 1;
            Header[] headerArr2 = f12282b;
            if (!linkedHashMap.containsKey(headerArr2[i10].f12278a)) {
                linkedHashMap.put(headerArr2[i10].f12278a, Integer.valueOf(i10));
            }
            i10 = i11;
        }
        Map<me.g, Integer> unmodifiableMap = Collections.unmodifiableMap(linkedHashMap);
        za.k.d(unmodifiableMap, "unmodifiableMap(result)");
        return unmodifiableMap;
    }

    public final me.g a(me.g name) {
        za.k.e(name, "name");
        int t7 = name.t();
        int i10 = 0;
        while (i10 < t7) {
            int i11 = i10 + 1;
            byte e10 = name.e(i10);
            if (65 <= e10 && e10 <= 90) {
                throw new IOException(za.k.l("PROTOCOL_ERROR response malformed: mixed case name: ", name.w()));
            }
            i10 = i11;
        }
        return name;
    }

    public final Map<me.g, Integer> b() {
        return f12283c;
    }

    public final Header[] c() {
        return f12282b;
    }

    /* compiled from: Hpack.kt */
    @Metadata(bv = {}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B#\b\u0007\u0012\u0006\u0010 \u001a\u00020\u001f\u0012\u0006\u0010!\u001a\u00020\u0005\u0012\b\b\u0002\u0010\"\u001a\u00020\u0005¢\u0006\u0004\b#\u0010$J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0004\u001a\u00020\u0002H\u0002J\u0010\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0002J\u0010\u0010\t\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0005H\u0002J\u0010\u0010\n\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0002J\u0010\u0010\u000b\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0005H\u0002J\b\u0010\f\u001a\u00020\u0002H\u0002J\u0010\u0010\u000e\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u0005H\u0002J\b\u0010\u000f\u001a\u00020\u0002H\u0002J\u0010\u0010\u0011\u001a\u00020\u00102\u0006\u0010\b\u001a\u00020\u0005H\u0002J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\b\u001a\u00020\u0005H\u0002J\u0018\u0010\u0016\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0014H\u0002J\b\u0010\u0017\u001a\u00020\u0005H\u0002J\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00140\u0018J\u0006\u0010\u001a\u001a\u00020\u0002J\u0016\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u0005J\u0006\u0010\u001e\u001a\u00020\u0010¨\u0006%"}, d2 = {"Lhe/d$a;", "", "Lma/f0;", "a", "b", "", "bytesToRecover", "d", ThermalBaseConfig.Item.ATTR_INDEX, "l", "c", "p", "q", "nameIndex", "n", "o", "Lme/g;", "f", "", "h", "Lhe/c;", "entry", "g", "i", "", "e", "k", "firstByte", "prefixMask", "m", "j", "Lme/a0;", "source", "headerTableSizeSetting", "maxDynamicTableByteCount", "<init>", "(Lme/a0;II)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.d$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final int f12284a;

        /* renamed from: b, reason: collision with root package name */
        private int f12285b;

        /* renamed from: c, reason: collision with root package name */
        private final List<Header> f12286c;

        /* renamed from: d, reason: collision with root package name */
        private final BufferedSource f12287d;

        /* renamed from: e, reason: collision with root package name */
        public Header[] f12288e;

        /* renamed from: f, reason: collision with root package name */
        private int f12289f;

        /* renamed from: g, reason: collision with root package name */
        public int f12290g;

        /* renamed from: h, reason: collision with root package name */
        public int f12291h;

        public a(Source source, int i10, int i11) {
            za.k.e(source, "source");
            this.f12284a = i10;
            this.f12285b = i11;
            this.f12286c = new ArrayList();
            this.f12287d = me.n.b(source);
            this.f12288e = new Header[8];
            this.f12289f = r2.length - 1;
        }

        private final void a() {
            int i10 = this.f12285b;
            int i11 = this.f12291h;
            if (i10 < i11) {
                if (i10 == 0) {
                    b();
                } else {
                    d(i11 - i10);
                }
            }
        }

        private final void b() {
            _ArraysJvm.n(this.f12288e, null, 0, 0, 6, null);
            this.f12289f = this.f12288e.length - 1;
            this.f12290g = 0;
            this.f12291h = 0;
        }

        private final int c(int index) {
            return this.f12289f + 1 + index;
        }

        private final int d(int bytesToRecover) {
            int i10;
            int i11 = 0;
            if (bytesToRecover > 0) {
                int length = this.f12288e.length;
                while (true) {
                    length--;
                    i10 = this.f12289f;
                    if (length < i10 || bytesToRecover <= 0) {
                        break;
                    }
                    Header header = this.f12288e[length];
                    za.k.b(header);
                    int i12 = header.f12280c;
                    bytesToRecover -= i12;
                    this.f12291h -= i12;
                    this.f12290g--;
                    i11++;
                }
                Header[] headerArr = this.f12288e;
                System.arraycopy(headerArr, i10 + 1, headerArr, i10 + 1 + i11, this.f12290g);
                this.f12289f += i11;
            }
            return i11;
        }

        private final me.g f(int index) {
            if (h(index)) {
                return Hpack.f12281a.c()[index].f12278a;
            }
            int c10 = c(index - Hpack.f12281a.c().length);
            if (c10 >= 0) {
                Header[] headerArr = this.f12288e;
                if (c10 < headerArr.length) {
                    Header header = headerArr[c10];
                    za.k.b(header);
                    return header.f12278a;
                }
            }
            throw new IOException(za.k.l("Header index too large ", Integer.valueOf(index + 1)));
        }

        private final void g(int i10, Header header) {
            this.f12286c.add(header);
            int i11 = header.f12280c;
            if (i10 != -1) {
                Header header2 = this.f12288e[c(i10)];
                za.k.b(header2);
                i11 -= header2.f12280c;
            }
            int i12 = this.f12285b;
            if (i11 > i12) {
                b();
                return;
            }
            int d10 = d((this.f12291h + i11) - i12);
            if (i10 == -1) {
                int i13 = this.f12290g + 1;
                Header[] headerArr = this.f12288e;
                if (i13 > headerArr.length) {
                    Header[] headerArr2 = new Header[headerArr.length * 2];
                    System.arraycopy(headerArr, 0, headerArr2, headerArr.length, headerArr.length);
                    this.f12289f = this.f12288e.length - 1;
                    this.f12288e = headerArr2;
                }
                int i14 = this.f12289f;
                this.f12289f = i14 - 1;
                this.f12288e[i14] = header;
                this.f12290g++;
            } else {
                this.f12288e[i10 + c(i10) + d10] = header;
            }
            this.f12291h += i11;
        }

        private final boolean h(int index) {
            return index >= 0 && index <= Hpack.f12281a.c().length - 1;
        }

        private final int i() {
            return ae.d.d(this.f12287d.M(), 255);
        }

        private final void l(int i10) {
            if (h(i10)) {
                this.f12286c.add(Hpack.f12281a.c()[i10]);
                return;
            }
            int c10 = c(i10 - Hpack.f12281a.c().length);
            if (c10 >= 0) {
                Header[] headerArr = this.f12288e;
                if (c10 < headerArr.length) {
                    List<Header> list = this.f12286c;
                    Header header = headerArr[c10];
                    za.k.b(header);
                    list.add(header);
                    return;
                }
            }
            throw new IOException(za.k.l("Header index too large ", Integer.valueOf(i10 + 1)));
        }

        private final void n(int i10) {
            g(-1, new Header(f(i10), j()));
        }

        private final void o() {
            g(-1, new Header(Hpack.f12281a.a(j()), j()));
        }

        private final void p(int i10) {
            this.f12286c.add(new Header(f(i10), j()));
        }

        private final void q() {
            this.f12286c.add(new Header(Hpack.f12281a.a(j()), j()));
        }

        public final List<Header> e() {
            List<Header> z02;
            z02 = _Collections.z0(this.f12286c);
            this.f12286c.clear();
            return z02;
        }

        public final me.g j() {
            int i10 = i();
            boolean z10 = (i10 & 128) == 128;
            long m10 = m(i10, 127);
            if (z10) {
                me.d dVar = new me.d();
                Huffman.f12456a.b(this.f12287d, m10, dVar);
                return dVar.e0();
            }
            return this.f12287d.h(m10);
        }

        public final void k() {
            while (!this.f12287d.s()) {
                int d10 = ae.d.d(this.f12287d.M(), 255);
                if (d10 == 128) {
                    throw new IOException("index == 0");
                }
                if ((d10 & 128) == 128) {
                    l(m(d10, 127) - 1);
                } else if (d10 == 64) {
                    o();
                } else if ((d10 & 64) == 64) {
                    n(m(d10, 63) - 1);
                } else if ((d10 & 32) == 32) {
                    int m10 = m(d10, 31);
                    this.f12285b = m10;
                    if (m10 >= 0 && m10 <= this.f12284a) {
                        a();
                    } else {
                        throw new IOException(za.k.l("Invalid dynamic table size update ", Integer.valueOf(this.f12285b)));
                    }
                } else if (d10 != 16 && d10 != 0) {
                    p(m(d10, 15) - 1);
                } else {
                    q();
                }
            }
        }

        public final int m(int firstByte, int prefixMask) {
            int i10 = firstByte & prefixMask;
            if (i10 < prefixMask) {
                return i10;
            }
            int i11 = 0;
            while (true) {
                int i12 = i();
                if ((i12 & 128) == 0) {
                    return prefixMask + (i12 << i11);
                }
                prefixMask += (i12 & 127) << i11;
                i11 += 7;
            }
        }

        public /* synthetic */ a(Source source, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
            this(source, i10, (i12 & 4) != 0 ? i10 : i11);
        }
    }

    /* compiled from: Hpack.kt */
    @Metadata(bv = {}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B%\b\u0007\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0017\u0012\u0006\u0010\u001a\u001a\u00020\u0019¢\u0006\u0004\b\u001b\u0010\u001cJ\b\u0010\u0003\u001a\u00020\u0002H\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u0010\u0010\t\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0007H\u0002J\b\u0010\n\u001a\u00020\u0002H\u0002J\u0014\u0010\r\u001a\u00020\u00022\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\u000bJ\u001e\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004J\u000e\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0012J\u000e\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0004¨\u0006\u001d"}, d2 = {"Lhe/d$b;", "", "Lma/f0;", "b", "", "bytesToRecover", "c", "Lhe/c;", "entry", "d", "a", "", "headerBlock", "g", ThermalBaseConfig.Item.ATTR_VALUE, "prefixMask", "bits", "h", "Lme/g;", "data", "f", "headerTableSizeSetting", "e", "", "useCompression", "Lme/d;", "out", "<init>", "(IZLme/d;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.d$b */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        public int f12292a;

        /* renamed from: b, reason: collision with root package name */
        private final boolean f12293b;

        /* renamed from: c, reason: collision with root package name */
        private final me.d f12294c;

        /* renamed from: d, reason: collision with root package name */
        private int f12295d;

        /* renamed from: e, reason: collision with root package name */
        private boolean f12296e;

        /* renamed from: f, reason: collision with root package name */
        public int f12297f;

        /* renamed from: g, reason: collision with root package name */
        public Header[] f12298g;

        /* renamed from: h, reason: collision with root package name */
        private int f12299h;

        /* renamed from: i, reason: collision with root package name */
        public int f12300i;

        /* renamed from: j, reason: collision with root package name */
        public int f12301j;

        public b(int i10, boolean z10, me.d dVar) {
            za.k.e(dVar, "out");
            this.f12292a = i10;
            this.f12293b = z10;
            this.f12294c = dVar;
            this.f12295d = Integer.MAX_VALUE;
            this.f12297f = i10;
            this.f12298g = new Header[8];
            this.f12299h = r2.length - 1;
        }

        private final void a() {
            int i10 = this.f12297f;
            int i11 = this.f12301j;
            if (i10 < i11) {
                if (i10 == 0) {
                    b();
                } else {
                    c(i11 - i10);
                }
            }
        }

        private final void b() {
            _ArraysJvm.n(this.f12298g, null, 0, 0, 6, null);
            this.f12299h = this.f12298g.length - 1;
            this.f12300i = 0;
            this.f12301j = 0;
        }

        private final int c(int bytesToRecover) {
            int i10;
            int i11 = 0;
            if (bytesToRecover > 0) {
                int length = this.f12298g.length;
                while (true) {
                    length--;
                    i10 = this.f12299h;
                    if (length < i10 || bytesToRecover <= 0) {
                        break;
                    }
                    Header header = this.f12298g[length];
                    za.k.b(header);
                    bytesToRecover -= header.f12280c;
                    int i12 = this.f12301j;
                    Header header2 = this.f12298g[length];
                    za.k.b(header2);
                    this.f12301j = i12 - header2.f12280c;
                    this.f12300i--;
                    i11++;
                }
                Header[] headerArr = this.f12298g;
                System.arraycopy(headerArr, i10 + 1, headerArr, i10 + 1 + i11, this.f12300i);
                Header[] headerArr2 = this.f12298g;
                int i13 = this.f12299h;
                Arrays.fill(headerArr2, i13 + 1, i13 + 1 + i11, (Object) null);
                this.f12299h += i11;
            }
            return i11;
        }

        private final void d(Header header) {
            int i10 = header.f12280c;
            int i11 = this.f12297f;
            if (i10 > i11) {
                b();
                return;
            }
            c((this.f12301j + i10) - i11);
            int i12 = this.f12300i + 1;
            Header[] headerArr = this.f12298g;
            if (i12 > headerArr.length) {
                Header[] headerArr2 = new Header[headerArr.length * 2];
                System.arraycopy(headerArr, 0, headerArr2, headerArr.length, headerArr.length);
                this.f12299h = this.f12298g.length - 1;
                this.f12298g = headerArr2;
            }
            int i13 = this.f12299h;
            this.f12299h = i13 - 1;
            this.f12298g[i13] = header;
            this.f12300i++;
            this.f12301j += i10;
        }

        public final void e(int i10) {
            this.f12292a = i10;
            int min = Math.min(i10, 16384);
            int i11 = this.f12297f;
            if (i11 == min) {
                return;
            }
            if (min < i11) {
                this.f12295d = Math.min(this.f12295d, min);
            }
            this.f12296e = true;
            this.f12297f = min;
            a();
        }

        public final void f(me.g gVar) {
            za.k.e(gVar, "data");
            if (this.f12293b) {
                Huffman huffman = Huffman.f12456a;
                if (huffman.d(gVar) < gVar.t()) {
                    me.d dVar = new me.d();
                    huffman.c(gVar, dVar);
                    me.g e02 = dVar.e0();
                    h(e02.t(), 127, 128);
                    this.f12294c.F(e02);
                    return;
                }
            }
            h(gVar.t(), 127, 0);
            this.f12294c.F(gVar);
        }

        public final void g(List<Header> list) {
            int i10;
            int i11;
            za.k.e(list, "headerBlock");
            if (this.f12296e) {
                int i12 = this.f12295d;
                if (i12 < this.f12297f) {
                    h(i12, 31, 32);
                }
                this.f12296e = false;
                this.f12295d = Integer.MAX_VALUE;
                h(this.f12297f, 31, 32);
            }
            int size = list.size();
            int i13 = 0;
            while (i13 < size) {
                int i14 = i13 + 1;
                Header header = list.get(i13);
                me.g v7 = header.f12278a.v();
                me.g gVar = header.f12279b;
                Hpack hpack = Hpack.f12281a;
                Integer num = hpack.b().get(v7);
                if (num != null) {
                    i11 = num.intValue() + 1;
                    if (2 <= i11 && i11 < 8) {
                        if (za.k.a(hpack.c()[i11 - 1].f12279b, gVar)) {
                            i10 = i11;
                        } else if (za.k.a(hpack.c()[i11].f12279b, gVar)) {
                            i11++;
                            i10 = i11;
                        }
                    }
                    i10 = i11;
                    i11 = -1;
                } else {
                    i10 = -1;
                    i11 = -1;
                }
                if (i11 == -1) {
                    int i15 = this.f12299h + 1;
                    int length = this.f12298g.length;
                    while (true) {
                        if (i15 >= length) {
                            break;
                        }
                        int i16 = i15 + 1;
                        Header header2 = this.f12298g[i15];
                        za.k.b(header2);
                        if (za.k.a(header2.f12278a, v7)) {
                            Header header3 = this.f12298g[i15];
                            za.k.b(header3);
                            if (za.k.a(header3.f12279b, gVar)) {
                                i11 = Hpack.f12281a.c().length + (i15 - this.f12299h);
                                break;
                            } else if (i10 == -1) {
                                i10 = Hpack.f12281a.c().length + (i15 - this.f12299h);
                            }
                        }
                        i15 = i16;
                    }
                }
                if (i11 != -1) {
                    h(i11, 127, 128);
                } else if (i10 == -1) {
                    this.f12294c.t(64);
                    f(v7);
                    f(gVar);
                    d(header);
                } else if (v7.u(Header.f12272e) && !za.k.a(Header.f12277j, v7)) {
                    h(i10, 15, 0);
                    f(gVar);
                } else {
                    h(i10, 63, 64);
                    f(gVar);
                    d(header);
                }
                i13 = i14;
            }
        }

        public final void h(int i10, int i11, int i12) {
            if (i10 < i11) {
                this.f12294c.t(i10 | i12);
                return;
            }
            this.f12294c.t(i12 | i11);
            int i13 = i10 - i11;
            while (i13 >= 128) {
                this.f12294c.t(128 | (i13 & 127));
                i13 >>>= 7;
            }
            this.f12294c.t(i13);
        }

        public /* synthetic */ b(int i10, boolean z10, me.d dVar, int i11, DefaultConstructorMarker defaultConstructorMarker) {
            this((i11 & 1) != 0 ? 4096 : i10, (i11 & 2) != 0 ? true : z10, dVar);
        }
    }
}
