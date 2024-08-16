package ub;

import hc.DeserializedDescriptorResolver;
import hc.KotlinJvmBinaryClass;
import hc.q;
import ic.KotlinClassHeader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import oc.ClassId;
import oc.FqName;
import sb.EmptyPackageFragmentDesciptor;
import xc.JvmClassName;
import zc.ChainedMemberScope;

/* compiled from: PackagePartScopeCache.kt */
/* renamed from: ub.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class PackagePartScopeCache {

    /* renamed from: a, reason: collision with root package name */
    private final DeserializedDescriptorResolver f18968a;

    /* renamed from: b, reason: collision with root package name */
    private final g f18969b;

    /* renamed from: c, reason: collision with root package name */
    private final ConcurrentHashMap<ClassId, zc.h> f18970c;

    public PackagePartScopeCache(DeserializedDescriptorResolver deserializedDescriptorResolver, g gVar) {
        za.k.e(deserializedDescriptorResolver, "resolver");
        za.k.e(gVar, "kotlinClassFinder");
        this.f18968a = deserializedDescriptorResolver;
        this.f18969b = gVar;
        this.f18970c = new ConcurrentHashMap<>();
    }

    public final zc.h a(f fVar) {
        Collection e10;
        List z02;
        za.k.e(fVar, "fileClass");
        ConcurrentHashMap<ClassId, zc.h> concurrentHashMap = this.f18970c;
        ClassId e11 = fVar.e();
        zc.h hVar = concurrentHashMap.get(e11);
        if (hVar == null) {
            FqName h10 = fVar.e().h();
            za.k.d(h10, "fileClass.classId.packageFqName");
            if (fVar.b().c() == KotlinClassHeader.a.MULTIFILE_CLASS) {
                List<String> f10 = fVar.b().f();
                e10 = new ArrayList();
                Iterator<T> it = f10.iterator();
                while (it.hasNext()) {
                    ClassId m10 = ClassId.m(JvmClassName.d((String) it.next()).e());
                    za.k.d(m10, "topLevel(JvmClassName.by…velClassMaybeWithDollars)");
                    KotlinJvmBinaryClass b10 = q.b(this.f18969b, m10);
                    if (b10 != null) {
                        e10.add(b10);
                    }
                }
            } else {
                e10 = CollectionsJVM.e(fVar);
            }
            EmptyPackageFragmentDesciptor emptyPackageFragmentDesciptor = new EmptyPackageFragmentDesciptor(this.f18968a.d().p(), h10);
            ArrayList arrayList = new ArrayList();
            Iterator it2 = e10.iterator();
            while (it2.hasNext()) {
                zc.h b11 = this.f18968a.b(emptyPackageFragmentDesciptor, (KotlinJvmBinaryClass) it2.next());
                if (b11 != null) {
                    arrayList.add(b11);
                }
            }
            z02 = _Collections.z0(arrayList);
            zc.h a10 = ChainedMemberScope.f20418d.a("package " + h10 + " (" + fVar + ')', z02);
            zc.h putIfAbsent = concurrentHashMap.putIfAbsent(e11, a10);
            hVar = putIfAbsent == null ? a10 : putIfAbsent;
        }
        za.k.d(hVar, "cache.getOrPut(fileClass…ileClass)\", scopes)\n    }");
        return hVar;
    }
}
