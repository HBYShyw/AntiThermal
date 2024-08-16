package ub;

import oc.ClassId;
import sd.StringsJVM;

/* compiled from: ReflectKotlinClassFinder.kt */
/* loaded from: classes2.dex */
public final class h {
    /* JADX INFO: Access modifiers changed from: private */
    public static final String b(ClassId classId) {
        String y4;
        String b10 = classId.i().b();
        za.k.d(b10, "relativeClassName.asString()");
        y4 = StringsJVM.y(b10, '.', '$', false, 4, null);
        if (classId.h().d()) {
            return y4;
        }
        return classId.h() + '.' + y4;
    }
}
