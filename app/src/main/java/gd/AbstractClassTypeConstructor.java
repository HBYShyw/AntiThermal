package gd;

import fd.StorageManager;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mb.KotlinBuiltIns;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import qd.SmartList;

/* compiled from: AbstractClassTypeConstructor.java */
/* renamed from: gd.b, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractClassTypeConstructor extends AbstractTypeConstructor {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AbstractClassTypeConstructor(StorageManager storageManager) {
        super(storageManager);
        if (storageManager == null) {
            r(0);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void r(int i10) {
        String format;
        String str = (i10 == 1 || i10 == 3 || i10 == 4) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 1 || i10 == 3 || i10 == 4) ? 2 : 3];
        if (i10 != 1) {
            if (i10 == 2) {
                objArr[0] = "classifier";
            } else if (i10 != 3 && i10 != 4) {
                objArr[0] = "storageManager";
            }
            if (i10 != 1) {
                objArr[1] = "getBuiltIns";
            } else if (i10 == 3 || i10 == 4) {
                objArr[1] = "getAdditionalNeighboursInSupertypeGraph";
            } else {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/types/AbstractClassTypeConstructor";
            }
            if (i10 != 1) {
                if (i10 == 2) {
                    objArr[2] = "isSameClassifier";
                } else if (i10 != 3 && i10 != 4) {
                    objArr[2] = "<init>";
                }
            }
            format = String.format(str, objArr);
            if (i10 == 1 && i10 != 3 && i10 != 4) {
                throw new IllegalArgumentException(format);
            }
            throw new IllegalStateException(format);
        }
        objArr[0] = "kotlin/reflect/jvm/internal/impl/types/AbstractClassTypeConstructor";
        if (i10 != 1) {
        }
        if (i10 != 1) {
        }
        format = String.format(str, objArr);
        if (i10 == 1) {
        }
        throw new IllegalStateException(format);
    }

    @Override // gd.ClassifierBasedTypeConstructor
    protected boolean e(ClassifierDescriptor classifierDescriptor) {
        if (classifierDescriptor == null) {
            r(2);
        }
        return (classifierDescriptor instanceof ClassDescriptor) && c(v(), classifierDescriptor);
    }

    @Override // gd.AbstractTypeConstructor
    protected g0 i() {
        if (KotlinBuiltIns.t0(v())) {
            return null;
        }
        return t().i();
    }

    @Override // gd.AbstractTypeConstructor
    protected Collection<g0> j(boolean z10) {
        DeclarationDescriptor b10 = v().b();
        if (!(b10 instanceof ClassDescriptor)) {
            List emptyList = Collections.emptyList();
            if (emptyList == null) {
                r(3);
            }
            return emptyList;
        }
        SmartList smartList = new SmartList();
        ClassDescriptor classDescriptor = (ClassDescriptor) b10;
        smartList.add(classDescriptor.x());
        ClassDescriptor c02 = classDescriptor.c0();
        if (z10 && c02 != null) {
            smartList.add(c02.x());
        }
        return smartList;
    }

    /* renamed from: s */
    public abstract ClassDescriptor v();

    @Override // gd.TypeConstructor
    public KotlinBuiltIns t() {
        KotlinBuiltIns j10 = wc.c.j(v());
        if (j10 == null) {
            r(1);
        }
        return j10;
    }
}
