package qb;

import gd.g0;
import id.ErrorUtils;
import java.util.Map;
import oc.FqName;
import oc.Name;
import pb.ClassDescriptor;
import pb.SourceElement;

/* compiled from: AnnotationDescriptor.kt */
/* renamed from: qb.c, reason: use source file name */
/* loaded from: classes2.dex */
public interface AnnotationDescriptor {

    /* compiled from: AnnotationDescriptor.kt */
    /* renamed from: qb.c$a */
    /* loaded from: classes2.dex */
    public static final class a {
        public static FqName a(AnnotationDescriptor annotationDescriptor) {
            ClassDescriptor i10 = wc.c.i(annotationDescriptor);
            if (i10 == null) {
                return null;
            }
            if (ErrorUtils.m(i10)) {
                i10 = null;
            }
            if (i10 != null) {
                return wc.c.h(i10);
            }
            return null;
        }
    }

    Map<Name, uc.g<?>> a();

    FqName d();

    g0 getType();

    SourceElement z();
}
