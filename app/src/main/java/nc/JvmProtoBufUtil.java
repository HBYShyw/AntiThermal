package nc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jc.l;
import jc.n;
import jc.q;
import jc.u;
import kotlin.collections._Collections;
import kotlin.collections.r;
import kotlin.collections.s;
import lc.Flags;
import lc.NameResolver;
import lc.ProtoBufUtil;
import lc.TypeTable;
import lc.protoTypeTableUtil;
import ma.o;
import mc.JvmProtoBuf;
import nc.JvmMemberSignature;
import qc.i;
import za.k;

/* compiled from: JvmProtoBufUtil.kt */
/* renamed from: nc.i */
/* loaded from: classes2.dex */
public final class JvmProtoBufUtil {

    /* renamed from: a */
    public static final JvmProtoBufUtil f16006a = new JvmProtoBufUtil();

    /* renamed from: b */
    private static final qc.g f16007b;

    static {
        qc.g d10 = qc.g.d();
        JvmProtoBuf.a(d10);
        k.d(d10, "newInstance().apply(JvmP…f::registerAllExtensions)");
        f16007b = d10;
    }

    private JvmProtoBufUtil() {
    }

    public static /* synthetic */ JvmMemberSignature.a d(JvmProtoBufUtil jvmProtoBufUtil, n nVar, NameResolver nameResolver, TypeTable typeTable, boolean z10, int i10, Object obj) {
        if ((i10 & 8) != 0) {
            z10 = true;
        }
        return jvmProtoBufUtil.c(nVar, nameResolver, typeTable, z10);
    }

    public static final boolean f(n nVar) {
        k.e(nVar, "proto");
        Flags.b a10 = JvmFlags.f15985a.a();
        Object p10 = nVar.p(JvmProtoBuf.f15368e);
        k.d(p10, "proto.getExtension(JvmProtoBuf.flags)");
        Boolean d10 = a10.d(((Number) p10).intValue());
        k.d(d10, "JvmFlags.IS_MOVED_FROM_I…nsion(JvmProtoBuf.flags))");
        return d10.booleanValue();
    }

    private final String g(q qVar, NameResolver nameResolver) {
        if (qVar.g0()) {
            return ClassMapperLite.b(nameResolver.a(qVar.R()));
        }
        return null;
    }

    public static final o<f, jc.c> h(byte[] bArr, String[] strArr) {
        k.e(bArr, "bytes");
        k.e(strArr, "strings");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        return new o<>(f16006a.k(byteArrayInputStream, strArr), jc.c.r1(byteArrayInputStream, f16007b));
    }

    public static final o<f, jc.c> i(String[] strArr, String[] strArr2) {
        k.e(strArr, "data");
        k.e(strArr2, "strings");
        byte[] e10 = BitEncoding.e(strArr);
        k.d(e10, "decodeBytes(data)");
        return h(e10, strArr2);
    }

    public static final o<f, jc.i> j(String[] strArr, String[] strArr2) {
        k.e(strArr, "data");
        k.e(strArr2, "strings");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(BitEncoding.e(strArr));
        return new o<>(f16006a.k(byteArrayInputStream, strArr2), jc.i.z0(byteArrayInputStream, f16007b));
    }

    private final f k(InputStream inputStream, String[] strArr) {
        JvmProtoBuf.e y4 = JvmProtoBuf.e.y(inputStream, f16007b);
        k.d(y4, "parseDelimitedFrom(this, EXTENSION_REGISTRY)");
        return new f(y4, strArr);
    }

    public static final o<f, l> l(byte[] bArr, String[] strArr) {
        k.e(bArr, "bytes");
        k.e(strArr, "strings");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        return new o<>(f16006a.k(byteArrayInputStream, strArr), l.Y(byteArrayInputStream, f16007b));
    }

    public static final o<f, l> m(String[] strArr, String[] strArr2) {
        k.e(strArr, "data");
        k.e(strArr2, "strings");
        byte[] e10 = BitEncoding.e(strArr);
        k.d(e10, "decodeBytes(data)");
        return l(e10, strArr2);
    }

    public final qc.g a() {
        return f16007b;
    }

    public final JvmMemberSignature.b b(jc.d dVar, NameResolver nameResolver, TypeTable typeTable) {
        int u7;
        String c02;
        k.e(dVar, "proto");
        k.e(nameResolver, "nameResolver");
        k.e(typeTable, "typeTable");
        i.f<jc.d, JvmProtoBuf.c> fVar = JvmProtoBuf.f15364a;
        k.d(fVar, "constructorSignature");
        JvmProtoBuf.c cVar = (JvmProtoBuf.c) ProtoBufUtil.a(dVar, fVar);
        String string = (cVar == null || !cVar.u()) ? "<init>" : nameResolver.getString(cVar.s());
        if (cVar != null && cVar.t()) {
            c02 = nameResolver.getString(cVar.r());
        } else {
            List<u> H = dVar.H();
            k.d(H, "proto.valueParameterList");
            u7 = s.u(H, 10);
            ArrayList arrayList = new ArrayList(u7);
            for (u uVar : H) {
                JvmProtoBufUtil jvmProtoBufUtil = f16006a;
                k.d(uVar, "it");
                String g6 = jvmProtoBufUtil.g(protoTypeTableUtil.q(uVar, typeTable), nameResolver);
                if (g6 == null) {
                    return null;
                }
                arrayList.add(g6);
            }
            c02 = _Collections.c0(arrayList, "", "(", ")V", 0, null, null, 56, null);
        }
        return new JvmMemberSignature.b(string, c02);
    }

    public final JvmMemberSignature.a c(n nVar, NameResolver nameResolver, TypeTable typeTable, boolean z10) {
        String g6;
        k.e(nVar, "proto");
        k.e(nameResolver, "nameResolver");
        k.e(typeTable, "typeTable");
        i.f<n, JvmProtoBuf.d> fVar = JvmProtoBuf.f15367d;
        k.d(fVar, "propertySignature");
        JvmProtoBuf.d dVar = (JvmProtoBuf.d) ProtoBufUtil.a(nVar, fVar);
        if (dVar == null) {
            return null;
        }
        JvmProtoBuf.b v7 = dVar.A() ? dVar.v() : null;
        if (v7 == null && z10) {
            return null;
        }
        int X = (v7 == null || !v7.u()) ? nVar.X() : v7.s();
        if (v7 == null || !v7.t()) {
            g6 = g(protoTypeTableUtil.n(nVar, typeTable), nameResolver);
            if (g6 == null) {
                return null;
            }
        } else {
            g6 = nameResolver.getString(v7.r());
        }
        return new JvmMemberSignature.a(nameResolver.getString(X), g6);
    }

    public final JvmMemberSignature.b e(jc.i iVar, NameResolver nameResolver, TypeTable typeTable) {
        List n10;
        int u7;
        List m02;
        int u10;
        String c02;
        String sb2;
        k.e(iVar, "proto");
        k.e(nameResolver, "nameResolver");
        k.e(typeTable, "typeTable");
        i.f<jc.i, JvmProtoBuf.c> fVar = JvmProtoBuf.f15365b;
        k.d(fVar, "methodSignature");
        JvmProtoBuf.c cVar = (JvmProtoBuf.c) ProtoBufUtil.a(iVar, fVar);
        int Y = (cVar == null || !cVar.u()) ? iVar.Y() : cVar.s();
        if (cVar != null && cVar.t()) {
            sb2 = nameResolver.getString(cVar.r());
        } else {
            n10 = r.n(protoTypeTableUtil.k(iVar, typeTable));
            List<u> k02 = iVar.k0();
            k.d(k02, "proto.valueParameterList");
            u7 = s.u(k02, 10);
            ArrayList arrayList = new ArrayList(u7);
            for (u uVar : k02) {
                k.d(uVar, "it");
                arrayList.add(protoTypeTableUtil.q(uVar, typeTable));
            }
            m02 = _Collections.m0(n10, arrayList);
            u10 = s.u(m02, 10);
            ArrayList arrayList2 = new ArrayList(u10);
            Iterator it = m02.iterator();
            while (it.hasNext()) {
                String g6 = f16006a.g((q) it.next(), nameResolver);
                if (g6 == null) {
                    return null;
                }
                arrayList2.add(g6);
            }
            String g10 = g(protoTypeTableUtil.m(iVar, typeTable), nameResolver);
            if (g10 == null) {
                return null;
            }
            StringBuilder sb3 = new StringBuilder();
            c02 = _Collections.c0(arrayList2, "", "(", ")", 0, null, null, 56, null);
            sb3.append(c02);
            sb3.append(g10);
            sb2 = sb3.toString();
        }
        return new JvmMemberSignature.b(nameResolver.getString(Y), sb2);
    }
}
