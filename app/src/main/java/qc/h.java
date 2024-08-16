package qc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import qc.h.b;
import qc.j;
import qc.l;
import qc.q;
import qc.z;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FieldSet.java */
/* loaded from: classes2.dex */
public final class h<FieldDescriptorType extends b<FieldDescriptorType>> {

    /* renamed from: d, reason: collision with root package name */
    private static final h f17289d = new h(true);

    /* renamed from: b, reason: collision with root package name */
    private boolean f17291b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f17292c = false;

    /* renamed from: a, reason: collision with root package name */
    private final v<FieldDescriptorType, Object> f17290a = v.o(16);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FieldSet.java */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f17293a;

        /* renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f17294b;

        static {
            int[] iArr = new int[z.b.values().length];
            f17294b = iArr;
            try {
                iArr[z.b.f17379g.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f17294b[z.b.f17380h.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f17294b[z.b.f17381i.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f17294b[z.b.f17382j.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f17294b[z.b.f17383k.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f17294b[z.b.f17384l.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f17294b[z.b.f17385m.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f17294b[z.b.f17386n.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f17294b[z.b.f17387o.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f17294b[z.b.f17390r.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                f17294b[z.b.f17391s.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                f17294b[z.b.f17393u.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                f17294b[z.b.f17394v.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                f17294b[z.b.f17395w.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                f17294b[z.b.f17396x.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                f17294b[z.b.f17388p.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                f17294b[z.b.f17389q.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                f17294b[z.b.f17392t.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            int[] iArr2 = new int[z.c.values().length];
            f17293a = iArr2;
            try {
                iArr2[z.c.INT.ordinal()] = 1;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                f17293a[z.c.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                f17293a[z.c.FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                f17293a[z.c.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                f17293a[z.c.BOOLEAN.ordinal()] = 5;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                f17293a[z.c.STRING.ordinal()] = 6;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                f17293a[z.c.BYTE_STRING.ordinal()] = 7;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                f17293a[z.c.ENUM.ordinal()] = 8;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                f17293a[z.c.MESSAGE.ordinal()] = 9;
            } catch (NoSuchFieldError unused27) {
            }
        }
    }

    /* compiled from: FieldSet.java */
    /* loaded from: classes2.dex */
    public interface b<T extends b<T>> extends Comparable<T> {
        z.c getLiteJavaType();

        z.b getLiteType();

        int getNumber();

        boolean isPacked();

        boolean isRepeated();

        q.a k(q.a aVar, q qVar);
    }

    private h() {
    }

    private Object c(Object obj) {
        if (!(obj instanceof byte[])) {
            return obj;
        }
        byte[] bArr = (byte[]) obj;
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    private static int d(z.b bVar, int i10, Object obj) {
        int D = f.D(i10);
        if (bVar == z.b.f17388p) {
            D *= 2;
        }
        return D + e(bVar, obj);
    }

    private static int e(z.b bVar, Object obj) {
        switch (a.f17294b[bVar.ordinal()]) {
            case 1:
                return f.g(((Double) obj).doubleValue());
            case 2:
                return f.m(((Float) obj).floatValue());
            case 3:
                return f.q(((Long) obj).longValue());
            case 4:
                return f.F(((Long) obj).longValue());
            case 5:
                return f.p(((Integer) obj).intValue());
            case 6:
                return f.k(((Long) obj).longValue());
            case 7:
                return f.j(((Integer) obj).intValue());
            case 8:
                return f.b(((Boolean) obj).booleanValue());
            case 9:
                return f.C((String) obj);
            case 10:
                if (obj instanceof d) {
                    return f.e((d) obj);
                }
                return f.c((byte[]) obj);
            case 11:
                return f.E(((Integer) obj).intValue());
            case 12:
                return f.x(((Integer) obj).intValue());
            case 13:
                return f.y(((Long) obj).longValue());
            case 14:
                return f.z(((Integer) obj).intValue());
            case 15:
                return f.B(((Long) obj).longValue());
            case 16:
                return f.n((q) obj);
            case 17:
                if (obj instanceof l) {
                    return f.r((l) obj);
                }
                return f.t((q) obj);
            case 18:
                if (obj instanceof j.a) {
                    return f.i(((j.a) obj).getNumber());
                }
                return f.i(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public static int f(b<?> bVar, Object obj) {
        z.b liteType = bVar.getLiteType();
        int number = bVar.getNumber();
        if (bVar.isRepeated()) {
            int i10 = 0;
            if (bVar.isPacked()) {
                Iterator it = ((List) obj).iterator();
                while (it.hasNext()) {
                    i10 += e(liteType, it.next());
                }
                return f.D(number) + i10 + f.v(i10);
            }
            Iterator it2 = ((List) obj).iterator();
            while (it2.hasNext()) {
                i10 += d(liteType, number, it2.next());
            }
            return i10;
        }
        return d(liteType, number, obj);
    }

    public static <T extends b<T>> h<T> g() {
        return f17289d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int l(z.b bVar, boolean z10) {
        if (z10) {
            return 2;
        }
        return bVar.b();
    }

    private boolean o(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType key = entry.getKey();
        if (key.getLiteJavaType() == z.c.MESSAGE) {
            if (key.isRepeated()) {
                Iterator it = ((List) entry.getValue()).iterator();
                while (it.hasNext()) {
                    if (!((q) it.next()).isInitialized()) {
                        return false;
                    }
                }
            } else {
                Object value = entry.getValue();
                if (value instanceof q) {
                    if (!((q) value).isInitialized()) {
                        return false;
                    }
                } else {
                    if (value instanceof l) {
                        return true;
                    }
                    throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
                }
            }
        }
        return true;
    }

    private void s(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType key = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof l) {
            value = ((l) value).e();
        }
        if (key.isRepeated()) {
            Object h10 = h(key);
            if (h10 == null) {
                h10 = new ArrayList();
            }
            Iterator it = ((List) value).iterator();
            while (it.hasNext()) {
                ((List) h10).add(c(it.next()));
            }
            this.f17290a.p(key, h10);
            return;
        }
        if (key.getLiteJavaType() == z.c.MESSAGE) {
            Object h11 = h(key);
            if (h11 == null) {
                this.f17290a.p(key, c(value));
                return;
            } else {
                this.f17290a.p(key, key.k(((q) h11).toBuilder(), (q) value).build());
                return;
            }
        }
        this.f17290a.p(key, c(value));
    }

    public static <T extends b<T>> h<T> t() {
        return new h<>();
    }

    public static Object u(e eVar, z.b bVar, boolean z10) {
        switch (a.f17294b[bVar.ordinal()]) {
            case 1:
                return Double.valueOf(eVar.m());
            case 2:
                return Float.valueOf(eVar.q());
            case 3:
                return Long.valueOf(eVar.t());
            case 4:
                return Long.valueOf(eVar.M());
            case 5:
                return Integer.valueOf(eVar.s());
            case 6:
                return Long.valueOf(eVar.p());
            case 7:
                return Integer.valueOf(eVar.o());
            case 8:
                return Boolean.valueOf(eVar.k());
            case 9:
                if (z10) {
                    return eVar.J();
                }
                return eVar.I();
            case 10:
                return eVar.l();
            case 11:
                return Integer.valueOf(eVar.L());
            case 12:
                return Integer.valueOf(eVar.E());
            case 13:
                return Long.valueOf(eVar.F());
            case 14:
                return Integer.valueOf(eVar.G());
            case 15:
                return Long.valueOf(eVar.H());
            case 16:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle nested groups.");
            case 17:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle embedded messages.");
            case 18:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle enums.");
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0024, code lost:
    
        if ((r3 instanceof qc.j.a) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x002d, code lost:
    
        if ((r3 instanceof byte[]) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001b, code lost:
    
        if ((r3 instanceof qc.l) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0030, code lost:
    
        r0 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void w(z.b bVar, Object obj) {
        Objects.requireNonNull(obj);
        boolean z10 = true;
        boolean z11 = false;
        switch (a.f17293a[bVar.a().ordinal()]) {
            case 1:
                z11 = obj instanceof Integer;
                break;
            case 2:
                z11 = obj instanceof Long;
                break;
            case 3:
                z11 = obj instanceof Float;
                break;
            case 4:
                z11 = obj instanceof Double;
                break;
            case 5:
                z11 = obj instanceof Boolean;
                break;
            case 6:
                z11 = obj instanceof String;
                break;
            case 7:
                if (!(obj instanceof d)) {
                    break;
                }
                z11 = z10;
                break;
            case 8:
                if (!(obj instanceof Integer)) {
                    break;
                }
                z11 = z10;
                break;
            case 9:
                if (!(obj instanceof q)) {
                    break;
                }
                z11 = z10;
                break;
        }
        if (!z11) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
    }

    private static void x(f fVar, z.b bVar, int i10, Object obj) {
        if (bVar == z.b.f17388p) {
            fVar.Y(i10, (q) obj);
        } else {
            fVar.w0(i10, l(bVar, false));
            y(fVar, bVar, obj);
        }
    }

    private static void y(f fVar, z.b bVar, Object obj) {
        switch (a.f17294b[bVar.ordinal()]) {
            case 1:
                fVar.R(((Double) obj).doubleValue());
                return;
            case 2:
                fVar.X(((Float) obj).floatValue());
                return;
            case 3:
                fVar.c0(((Long) obj).longValue());
                return;
            case 4:
                fVar.z0(((Long) obj).longValue());
                return;
            case 5:
                fVar.b0(((Integer) obj).intValue());
                return;
            case 6:
                fVar.V(((Long) obj).longValue());
                return;
            case 7:
                fVar.U(((Integer) obj).intValue());
                return;
            case 8:
                fVar.M(((Boolean) obj).booleanValue());
                return;
            case 9:
                fVar.v0((String) obj);
                return;
            case 10:
                if (obj instanceof d) {
                    fVar.P((d) obj);
                    return;
                } else {
                    fVar.N((byte[]) obj);
                    return;
                }
            case 11:
                fVar.y0(((Integer) obj).intValue());
                return;
            case 12:
                fVar.q0(((Integer) obj).intValue());
                return;
            case 13:
                fVar.r0(((Long) obj).longValue());
                return;
            case 14:
                fVar.s0(((Integer) obj).intValue());
                return;
            case 15:
                fVar.u0(((Long) obj).longValue());
                return;
            case 16:
                fVar.Z((q) obj);
                return;
            case 17:
                fVar.e0((q) obj);
                return;
            case 18:
                if (obj instanceof j.a) {
                    fVar.T(((j.a) obj).getNumber());
                    return;
                } else {
                    fVar.T(((Integer) obj).intValue());
                    return;
                }
            default:
                return;
        }
    }

    public static void z(b<?> bVar, Object obj, f fVar) {
        z.b liteType = bVar.getLiteType();
        int number = bVar.getNumber();
        if (bVar.isRepeated()) {
            List list = (List) obj;
            if (bVar.isPacked()) {
                fVar.w0(number, 2);
                int i10 = 0;
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    i10 += e(liteType, it.next());
                }
                fVar.o0(i10);
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    y(fVar, liteType, it2.next());
                }
                return;
            }
            Iterator it3 = list.iterator();
            while (it3.hasNext()) {
                x(fVar, liteType, number, it3.next());
            }
            return;
        }
        if (obj instanceof l) {
            x(fVar, liteType, number, ((l) obj).e());
        } else {
            x(fVar, liteType, number, obj);
        }
    }

    public void a(FieldDescriptorType fielddescriptortype, Object obj) {
        List list;
        if (fielddescriptortype.isRepeated()) {
            w(fielddescriptortype.getLiteType(), obj);
            Object h10 = h(fielddescriptortype);
            if (h10 == null) {
                list = new ArrayList();
                this.f17290a.p(fielddescriptortype, list);
            } else {
                list = (List) h10;
            }
            list.add(obj);
            return;
        }
        throw new IllegalArgumentException("addRepeatedField() can only be called on repeated fields.");
    }

    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public h<FieldDescriptorType> clone() {
        h<FieldDescriptorType> t7 = t();
        for (int i10 = 0; i10 < this.f17290a.j(); i10++) {
            Map.Entry<FieldDescriptorType, Object> i11 = this.f17290a.i(i10);
            t7.v(i11.getKey(), i11.getValue());
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.f17290a.k()) {
            t7.v(entry.getKey(), entry.getValue());
        }
        t7.f17292c = this.f17292c;
        return t7;
    }

    public Object h(FieldDescriptorType fielddescriptortype) {
        Object obj = this.f17290a.get(fielddescriptortype);
        return obj instanceof l ? ((l) obj).e() : obj;
    }

    public Object i(FieldDescriptorType fielddescriptortype, int i10) {
        if (fielddescriptortype.isRepeated()) {
            Object h10 = h(fielddescriptortype);
            if (h10 != null) {
                return ((List) h10).get(i10);
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
    }

    public int j(FieldDescriptorType fielddescriptortype) {
        if (fielddescriptortype.isRepeated()) {
            Object h10 = h(fielddescriptortype);
            if (h10 == null) {
                return 0;
            }
            return ((List) h10).size();
        }
        throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
    }

    public int k() {
        int i10 = 0;
        for (int i11 = 0; i11 < this.f17290a.j(); i11++) {
            Map.Entry<FieldDescriptorType, Object> i12 = this.f17290a.i(i11);
            i10 += f(i12.getKey(), i12.getValue());
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.f17290a.k()) {
            i10 += f(entry.getKey(), entry.getValue());
        }
        return i10;
    }

    public boolean m(FieldDescriptorType fielddescriptortype) {
        if (fielddescriptortype.isRepeated()) {
            throw new IllegalArgumentException("hasField() can only be called on non-repeated fields.");
        }
        return this.f17290a.get(fielddescriptortype) != null;
    }

    public boolean n() {
        for (int i10 = 0; i10 < this.f17290a.j(); i10++) {
            if (!o(this.f17290a.i(i10))) {
                return false;
            }
        }
        Iterator<Map.Entry<FieldDescriptorType, Object>> it = this.f17290a.k().iterator();
        while (it.hasNext()) {
            if (!o(it.next())) {
                return false;
            }
        }
        return true;
    }

    public Iterator<Map.Entry<FieldDescriptorType, Object>> p() {
        if (this.f17292c) {
            return new l.c(this.f17290a.entrySet().iterator());
        }
        return this.f17290a.entrySet().iterator();
    }

    public void q() {
        if (this.f17291b) {
            return;
        }
        this.f17290a.n();
        this.f17291b = true;
    }

    public void r(h<FieldDescriptorType> hVar) {
        for (int i10 = 0; i10 < hVar.f17290a.j(); i10++) {
            s(hVar.f17290a.i(i10));
        }
        Iterator<Map.Entry<FieldDescriptorType, Object>> it = hVar.f17290a.k().iterator();
        while (it.hasNext()) {
            s(it.next());
        }
    }

    public void v(FieldDescriptorType fielddescriptortype, Object obj) {
        if (fielddescriptortype.isRepeated()) {
            if (obj instanceof List) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll((List) obj);
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    w(fielddescriptortype.getLiteType(), it.next());
                }
                obj = arrayList;
            } else {
                throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            }
        } else {
            w(fielddescriptortype.getLiteType(), obj);
        }
        if (obj instanceof l) {
            this.f17292c = true;
        }
        this.f17290a.p(fielddescriptortype, obj);
    }

    private h(boolean z10) {
        q();
    }
}
