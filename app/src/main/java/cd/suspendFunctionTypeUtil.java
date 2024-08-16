package cd;

import mb.StandardNames;
import oc.CallableId;
import oc.FqName;
import oc.Name;

/* compiled from: suspendFunctionTypeUtil.kt */
/* renamed from: cd.c0, reason: use source file name */
/* loaded from: classes2.dex */
public final class suspendFunctionTypeUtil {

    /* renamed from: a, reason: collision with root package name */
    public static final FqName f5205a = new FqName("kotlin.suspend");

    /* renamed from: b, reason: collision with root package name */
    public static final CallableId f5206b;

    static {
        FqName fqName = StandardNames.f15283u;
        Name f10 = Name.f("suspend");
        za.k.d(f10, "identifier(\"suspend\")");
        f5206b = new CallableId(fqName, f10);
    }
}
