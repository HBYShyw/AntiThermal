package yb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.MutableCollections;
import oc.FqName;
import oc.Name;
import pb.ClassDescriptor;
import qb.AnnotationDescriptor;

/* compiled from: AnnotationTypeQualifierResolver.kt */
/* renamed from: yb.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class AnnotationTypeQualifierResolver extends AbstractAnnotationTypeQualifierResolver<AnnotationDescriptor> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AnnotationTypeQualifierResolver(JavaTypeEnhancementState javaTypeEnhancementState) {
        super(javaTypeEnhancementState);
        za.k.e(javaTypeEnhancementState, "javaTypeEnhancementState");
    }

    private final List<String> y(uc.g<?> gVar) {
        List<String> j10;
        List<String> e10;
        if (gVar instanceof uc.b) {
            List<? extends uc.g<?>> b10 = ((uc.b) gVar).b();
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = b10.iterator();
            while (it.hasNext()) {
                MutableCollections.z(arrayList, y((uc.g) it.next()));
            }
            return arrayList;
        }
        if (gVar instanceof uc.j) {
            e10 = CollectionsJVM.e(((uc.j) gVar).c().d());
            return e10;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // yb.AbstractAnnotationTypeQualifierResolver
    /* renamed from: u, reason: merged with bridge method [inline-methods] */
    public Iterable<String> b(AnnotationDescriptor annotationDescriptor, boolean z10) {
        List<String> y4;
        za.k.e(annotationDescriptor, "<this>");
        Map<Name, uc.g<?>> a10 = annotationDescriptor.a();
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<Name, uc.g<?>> entry : a10.entrySet()) {
            Name key = entry.getKey();
            uc.g<?> value = entry.getValue();
            if (z10 && !za.k.a(key, b0.f20020c)) {
                y4 = kotlin.collections.r.j();
            } else {
                y4 = y(value);
            }
            MutableCollections.z(arrayList, y4);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // yb.AbstractAnnotationTypeQualifierResolver
    /* renamed from: v, reason: merged with bridge method [inline-methods] */
    public FqName i(AnnotationDescriptor annotationDescriptor) {
        za.k.e(annotationDescriptor, "<this>");
        return annotationDescriptor.d();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // yb.AbstractAnnotationTypeQualifierResolver
    /* renamed from: w, reason: merged with bridge method [inline-methods] */
    public Object j(AnnotationDescriptor annotationDescriptor) {
        za.k.e(annotationDescriptor, "<this>");
        ClassDescriptor i10 = wc.c.i(annotationDescriptor);
        za.k.b(i10);
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // yb.AbstractAnnotationTypeQualifierResolver
    /* renamed from: x, reason: merged with bridge method [inline-methods] */
    public Iterable<AnnotationDescriptor> k(AnnotationDescriptor annotationDescriptor) {
        List j10;
        qb.g i10;
        za.k.e(annotationDescriptor, "<this>");
        ClassDescriptor i11 = wc.c.i(annotationDescriptor);
        if (i11 != null && (i10 = i11.i()) != null) {
            return i10;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }
}
