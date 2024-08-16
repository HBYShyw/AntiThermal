package kb;

import com.oplus.backup.sdk.common.utils.Constants;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.List;

/* compiled from: Caller.kt */
/* loaded from: classes2.dex */
public interface e<M extends Member> {

    /* compiled from: Caller.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        public static <M extends Member> void a(e<? extends M> eVar, Object[] objArr) {
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            if (g.a(eVar) == objArr.length) {
                return;
            }
            throw new IllegalArgumentException("Callable expects " + g.a(eVar) + " arguments, but " + objArr.length + " were provided.");
        }
    }

    List<Type> a();

    M b();

    Object d(Object[] objArr);

    Type f();
}
