package vb;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import oc.FqName;
import xa.JvmClassMapping;

/* compiled from: ReflectJavaAnnotationOwner.kt */
/* loaded from: classes2.dex */
public final class i {
    public static final ReflectJavaAnnotation a(Annotation[] annotationArr, FqName fqName) {
        Annotation annotation;
        za.k.e(annotationArr, "<this>");
        za.k.e(fqName, "fqName");
        int length = annotationArr.length;
        int i10 = 0;
        while (true) {
            if (i10 >= length) {
                annotation = null;
                break;
            }
            annotation = annotationArr[i10];
            if (za.k.a(reflectClassUtil.a(JvmClassMapping.b(JvmClassMapping.a(annotation))).b(), fqName)) {
                break;
            }
            i10++;
        }
        if (annotation != null) {
            return new ReflectJavaAnnotation(annotation);
        }
        return null;
    }

    public static final List<ReflectJavaAnnotation> b(Annotation[] annotationArr) {
        za.k.e(annotationArr, "<this>");
        ArrayList arrayList = new ArrayList(annotationArr.length);
        for (Annotation annotation : annotationArr) {
            arrayList.add(new ReflectJavaAnnotation(annotation));
        }
        return arrayList;
    }
}
