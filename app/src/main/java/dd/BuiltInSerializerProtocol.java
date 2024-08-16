package dd;

import bd.SerializerExtensionProtocol;
import java.util.List;
import jc.b;
import jc.l;
import jc.n;
import jc.q;
import jc.s;
import jc.u;
import kc.BuiltInsProtoBuf;
import oc.FqName;
import qc.g;
import qc.i;
import sd.StringsJVM;
import za.k;

/* compiled from: BuiltInSerializerProtocol.kt */
/* renamed from: dd.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class BuiltInSerializerProtocol extends SerializerExtensionProtocol {

    /* renamed from: n, reason: collision with root package name */
    public static final BuiltInSerializerProtocol f10926n = new BuiltInSerializerProtocol();

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private BuiltInSerializerProtocol() {
        super(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        g d10 = g.d();
        BuiltInsProtoBuf.a(d10);
        k.d(d10, "newInstance().apply(Buil…f::registerAllExtensions)");
        i.f<l, Integer> fVar = BuiltInsProtoBuf.f14282a;
        k.d(fVar, "packageFqName");
        i.f<jc.d, List<jc.b>> fVar2 = BuiltInsProtoBuf.f14284c;
        k.d(fVar2, "constructorAnnotation");
        i.f<jc.c, List<jc.b>> fVar3 = BuiltInsProtoBuf.f14283b;
        k.d(fVar3, "classAnnotation");
        i.f<jc.i, List<jc.b>> fVar4 = BuiltInsProtoBuf.f14285d;
        k.d(fVar4, "functionAnnotation");
        i.f<n, List<jc.b>> fVar5 = BuiltInsProtoBuf.f14286e;
        k.d(fVar5, "propertyAnnotation");
        i.f<n, List<jc.b>> fVar6 = BuiltInsProtoBuf.f14287f;
        k.d(fVar6, "propertyGetterAnnotation");
        i.f<n, List<jc.b>> fVar7 = BuiltInsProtoBuf.f14288g;
        k.d(fVar7, "propertySetterAnnotation");
        i.f<jc.g, List<jc.b>> fVar8 = BuiltInsProtoBuf.f14290i;
        k.d(fVar8, "enumEntryAnnotation");
        i.f<n, b.C0061b.c> fVar9 = BuiltInsProtoBuf.f14289h;
        k.d(fVar9, "compileTimeValue");
        i.f<u, List<jc.b>> fVar10 = BuiltInsProtoBuf.f14291j;
        k.d(fVar10, "parameterAnnotation");
        i.f<q, List<jc.b>> fVar11 = BuiltInsProtoBuf.f14292k;
        k.d(fVar11, "typeAnnotation");
        i.f<s, List<jc.b>> fVar12 = BuiltInsProtoBuf.f14293l;
        k.d(fVar12, "typeParameterAnnotation");
    }

    private final String o(FqName fqName) {
        if (fqName.d()) {
            return "default-package";
        }
        String b10 = fqName.g().b();
        k.d(b10, "fqName.shortName().asString()");
        return b10;
    }

    public final String m(FqName fqName) {
        k.e(fqName, "fqName");
        return o(fqName) + ".kotlin_builtins";
    }

    public final String n(FqName fqName) {
        String y4;
        k.e(fqName, "fqName");
        StringBuilder sb2 = new StringBuilder();
        String b10 = fqName.b();
        k.d(b10, "fqName.asString()");
        y4 = StringsJVM.y(b10, '.', '/', false, 4, null);
        sb2.append(y4);
        sb2.append('/');
        sb2.append(m(fqName));
        return sb2.toString();
    }
}
