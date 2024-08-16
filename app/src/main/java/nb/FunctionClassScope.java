package nb;

import fd.StorageManager;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.r;
import pb.ClassDescriptor;
import pb.FunctionDescriptor;
import za.k;
import zc.GivenFunctionsMemberScope;

/* compiled from: FunctionClassScope.kt */
/* renamed from: nb.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class FunctionClassScope extends GivenFunctionsMemberScope {

    /* compiled from: FunctionClassScope.kt */
    /* renamed from: nb.d$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f15980a;

        static {
            int[] iArr = new int[FunctionClassKind.values().length];
            try {
                iArr[FunctionClassKind.f15969j.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[FunctionClassKind.f15970k.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            f15980a = iArr;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FunctionClassScope(StorageManager storageManager, FunctionClassDescriptor functionClassDescriptor) {
        super(storageManager, functionClassDescriptor);
        k.e(storageManager, "storageManager");
        k.e(functionClassDescriptor, "containingClass");
    }

    @Override // zc.GivenFunctionsMemberScope
    protected List<FunctionDescriptor> i() {
        List<FunctionDescriptor> e10;
        List<FunctionDescriptor> e11;
        List<FunctionDescriptor> j10;
        ClassDescriptor l10 = l();
        k.c(l10, "null cannot be cast to non-null type org.jetbrains.kotlin.builtins.functions.FunctionClassDescriptor");
        int i10 = a.f15980a[((FunctionClassDescriptor) l10).d1().ordinal()];
        if (i10 == 1) {
            e10 = CollectionsJVM.e(FunctionInvokeDescriptor.I.a((FunctionClassDescriptor) l(), false));
            return e10;
        }
        if (i10 != 2) {
            j10 = r.j();
            return j10;
        }
        e11 = CollectionsJVM.e(FunctionInvokeDescriptor.I.a((FunctionClassDescriptor) l(), true));
        return e11;
    }
}
