package cd;

import fb._Ranges;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.MapsJVM;
import lc.BinaryVersion;
import lc.NameResolver;
import oc.ClassId;
import pb.SourceElement;

/* compiled from: ProtoBasedClassDataFinder.kt */
/* renamed from: cd.y, reason: use source file name */
/* loaded from: classes2.dex */
public final class ProtoBasedClassDataFinder implements ClassDataFinder {

    /* renamed from: a, reason: collision with root package name */
    private final NameResolver f5321a;

    /* renamed from: b, reason: collision with root package name */
    private final BinaryVersion f5322b;

    /* renamed from: c, reason: collision with root package name */
    private final ya.l<ClassId, SourceElement> f5323c;

    /* renamed from: d, reason: collision with root package name */
    private final Map<ClassId, jc.c> f5324d;

    /* JADX WARN: Multi-variable type inference failed */
    public ProtoBasedClassDataFinder(jc.m mVar, NameResolver nameResolver, BinaryVersion binaryVersion, ya.l<? super ClassId, ? extends SourceElement> lVar) {
        int u7;
        int e10;
        int c10;
        za.k.e(mVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(binaryVersion, "metadataVersion");
        za.k.e(lVar, "classSource");
        this.f5321a = nameResolver;
        this.f5322b = binaryVersion;
        this.f5323c = lVar;
        List<jc.c> E = mVar.E();
        za.k.d(E, "proto.class_List");
        u7 = kotlin.collections.s.u(E, 10);
        e10 = MapsJVM.e(u7);
        c10 = _Ranges.c(e10, 16);
        LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
        for (Object obj : E) {
            linkedHashMap.put(NameResolverUtil.a(this.f5321a, ((jc.c) obj).z0()), obj);
        }
        this.f5324d = linkedHashMap;
    }

    @Override // cd.ClassDataFinder
    public ClassData a(ClassId classId) {
        za.k.e(classId, "classId");
        jc.c cVar = this.f5324d.get(classId);
        if (cVar == null) {
            return null;
        }
        return new ClassData(this.f5321a, cVar, this.f5322b, this.f5323c.invoke(classId));
    }

    public final Collection<ClassId> b() {
        return this.f5324d.keySet();
    }
}
