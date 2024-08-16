package ib;

import gb.KClass;
import gb.KClassifier;
import gb.KType;
import gb.KTypeParameter;
import java.util.Iterator;
import java.util.List;
import jb.KTypeImpl;
import jb.KotlinReflectionInternalError;
import kotlin.collections._Collections;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import za.Reflection;
import za.k;

/* compiled from: KTypesJvm.kt */
/* renamed from: ib.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class KTypesJvm {
    /* JADX WARN: Multi-variable type inference failed */
    public static final KClass<?> a(KClassifier kClassifier) {
        ClassDescriptor classDescriptor;
        KClass<?> b10;
        Object V;
        k.e(kClassifier, "<this>");
        if (kClassifier instanceof KClass) {
            return (KClass) kClassifier;
        }
        if (kClassifier instanceof KTypeParameter) {
            List<KType> upperBounds = ((KTypeParameter) kClassifier).getUpperBounds();
            Iterator<T> it = upperBounds.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                KType kType = (KType) next;
                k.c(kType, "null cannot be cast to non-null type kotlin.reflect.jvm.internal.KTypeImpl");
                ClassifierDescriptor v7 = ((KTypeImpl) kType).m().W0().v();
                classDescriptor = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
                if ((classDescriptor == null || classDescriptor.getKind() == ClassKind.INTERFACE || classDescriptor.getKind() == ClassKind.ANNOTATION_CLASS) ? false : true) {
                    classDescriptor = next;
                    break;
                }
            }
            KType kType2 = (KType) classDescriptor;
            if (kType2 == null) {
                V = _Collections.V(upperBounds);
                kType2 = (KType) V;
            }
            return (kType2 == null || (b10 = b(kType2)) == null) ? Reflection.b(Object.class) : b10;
        }
        throw new KotlinReflectionInternalError("Cannot calculate JVM erasure for type: " + kClassifier);
    }

    public static final KClass<?> b(KType kType) {
        KClass<?> a10;
        k.e(kType, "<this>");
        KClassifier c10 = kType.c();
        if (c10 != null && (a10 = a(c10)) != null) {
            return a10;
        }
        throw new KotlinReflectionInternalError("Cannot calculate JVM erasure for type: " + kType);
    }
}
