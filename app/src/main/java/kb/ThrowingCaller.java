package kb;

import com.oplus.backup.sdk.common.utils.Constants;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.List;
import kotlin.collections.r;

/* compiled from: ThrowingCaller.kt */
/* renamed from: kb.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class ThrowingCaller implements e {

    /* renamed from: a, reason: collision with root package name */
    public static final ThrowingCaller f14278a = new ThrowingCaller();

    private ThrowingCaller() {
    }

    @Override // kb.e
    public List<Type> a() {
        List<Type> j10;
        j10 = r.j();
        return j10;
    }

    @Override // kb.e
    public /* bridge */ /* synthetic */ Member b() {
        return (Member) c();
    }

    public Void c() {
        return null;
    }

    @Override // kb.e
    public Object d(Object[] objArr) {
        za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
        throw new UnsupportedOperationException("call/callBy are not supported for this declaration.");
    }

    @Override // kb.e
    public Type f() {
        Class cls = Void.TYPE;
        za.k.d(cls, "TYPE");
        return cls;
    }
}
