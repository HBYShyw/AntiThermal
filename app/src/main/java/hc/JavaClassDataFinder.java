package hc;

import cd.ClassData;
import cd.ClassDataFinder;
import oc.ClassId;

/* compiled from: JavaClassDataFinder.kt */
/* renamed from: hc.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaClassDataFinder implements ClassDataFinder {

    /* renamed from: a, reason: collision with root package name */
    private final p f12174a;

    /* renamed from: b, reason: collision with root package name */
    private final DeserializedDescriptorResolver f12175b;

    public JavaClassDataFinder(p pVar, DeserializedDescriptorResolver deserializedDescriptorResolver) {
        za.k.e(pVar, "kotlinClassFinder");
        za.k.e(deserializedDescriptorResolver, "deserializedDescriptorResolver");
        this.f12174a = pVar;
        this.f12175b = deserializedDescriptorResolver;
    }

    @Override // cd.ClassDataFinder
    public ClassData a(ClassId classId) {
        za.k.e(classId, "classId");
        KotlinJvmBinaryClass b10 = q.b(this.f12174a, classId);
        if (b10 == null) {
            return null;
        }
        za.k.a(b10.e(), classId);
        return this.f12175b.i(b10);
    }
}
