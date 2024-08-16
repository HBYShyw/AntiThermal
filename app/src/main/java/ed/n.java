package ed;

import fd.StorageManager;
import java.util.List;
import qb.AnnotationDescriptor;

/* compiled from: DeserializedAnnotations.kt */
/* loaded from: classes2.dex */
public final class n extends a {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public n(StorageManager storageManager, ya.a<? extends List<? extends AnnotationDescriptor>> aVar) {
        super(storageManager, aVar);
        za.k.e(storageManager, "storageManager");
        za.k.e(aVar, "compute");
    }

    @Override // ed.a, qb.g
    public boolean isEmpty() {
        return false;
    }
}
