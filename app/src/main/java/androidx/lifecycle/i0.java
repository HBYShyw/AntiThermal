package androidx.lifecycle;

import kotlin.Metadata;
import w.a;

/* compiled from: ViewModelProvider.kt */
@Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0010\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\u0000¨\u0006\u0004"}, d2 = {"Landroidx/lifecycle/k0;", "owner", "Lw/a;", "a", "lifecycle-viewmodel_release"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class i0 {
    public static final w.a a(ViewModelStoreOwner viewModelStoreOwner) {
        za.k.e(viewModelStoreOwner, "owner");
        if (viewModelStoreOwner instanceof HasDefaultViewModelProviderFactory) {
            w.a defaultViewModelCreationExtras = ((HasDefaultViewModelProviderFactory) viewModelStoreOwner).getDefaultViewModelCreationExtras();
            za.k.d(defaultViewModelCreationExtras, "{\n        owner.defaultV…ModelCreationExtras\n    }");
            return defaultViewModelCreationExtras;
        }
        return a.C0113a.f19305b;
    }
}
