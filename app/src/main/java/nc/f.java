package nc;

import java.util.List;
import java.util.Set;
import kotlin.collections._Collections;
import kotlin.collections.s0;
import mc.JvmProtoBuf;
import za.k;

/* compiled from: JvmNameResolver.kt */
/* loaded from: classes2.dex */
public final class f extends JvmNameResolverBase {

    /* renamed from: h, reason: collision with root package name */
    private final JvmProtoBuf.e f15997h;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public f(JvmProtoBuf.e eVar, String[] strArr) {
        super(strArr, r0, h.a(r1));
        Set D0;
        k.e(eVar, "types");
        k.e(strArr, "strings");
        List<Integer> s7 = eVar.s();
        if (s7.isEmpty()) {
            D0 = s0.e();
        } else {
            k.d(s7, "_init_$lambda$0");
            D0 = _Collections.D0(s7);
        }
        List<JvmProtoBuf.e.c> t7 = eVar.t();
        k.d(t7, "types.recordList");
        this.f15997h = eVar;
    }
}
