package qb;

import gd.g0;
import gd.o0;
import java.util.Map;
import mb.KotlinBuiltIns;
import oc.FqName;
import oc.Name;
import pb.SourceElement;
import za.Lambda;

/* compiled from: BuiltInAnnotationDescriptor.kt */
/* renamed from: qb.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class BuiltInAnnotationDescriptor implements AnnotationDescriptor {

    /* renamed from: a, reason: collision with root package name */
    private final KotlinBuiltIns f17199a;

    /* renamed from: b, reason: collision with root package name */
    private final FqName f17200b;

    /* renamed from: c, reason: collision with root package name */
    private final Map<Name, uc.g<?>> f17201c;

    /* renamed from: d, reason: collision with root package name */
    private final ma.h f17202d;

    /* compiled from: BuiltInAnnotationDescriptor.kt */
    /* renamed from: qb.j$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<o0> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final o0 invoke() {
            return BuiltInAnnotationDescriptor.this.f17199a.o(BuiltInAnnotationDescriptor.this.d()).x();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public BuiltInAnnotationDescriptor(KotlinBuiltIns kotlinBuiltIns, FqName fqName, Map<Name, ? extends uc.g<?>> map) {
        ma.h a10;
        za.k.e(kotlinBuiltIns, "builtIns");
        za.k.e(fqName, "fqName");
        za.k.e(map, "allValueArguments");
        this.f17199a = kotlinBuiltIns;
        this.f17200b = fqName;
        this.f17201c = map;
        a10 = ma.j.a(ma.l.PUBLICATION, new a());
        this.f17202d = a10;
    }

    @Override // qb.AnnotationDescriptor
    public Map<Name, uc.g<?>> a() {
        return this.f17201c;
    }

    @Override // qb.AnnotationDescriptor
    public FqName d() {
        return this.f17200b;
    }

    @Override // qb.AnnotationDescriptor
    public g0 getType() {
        Object value = this.f17202d.getValue();
        za.k.d(value, "<get-type>(...)");
        return (g0) value;
    }

    @Override // qb.AnnotationDescriptor
    public SourceElement z() {
        SourceElement sourceElement = SourceElement.f16664a;
        za.k.d(sourceElement, "NO_SOURCE");
        return sourceElement;
    }
}
