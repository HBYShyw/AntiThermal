package b0;

import android.view.View;
import androidx.savedstate.R$id;
import kotlin.Metadata;
import za.k;

/* compiled from: ViewTreeSavedStateRegistryOwner.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u001d\u0010\u0004\u001a\u00020\u0003*\u00020\u00002\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001H\u0007¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Landroid/view/View;", "Lb0/d;", "owner", "Lma/f0;", "a", "(Landroid/view/View;Lb0/d;)V", "savedstate_release"}, k = 2, mv = {1, 6, 0})
/* renamed from: b0.e, reason: use source file name */
/* loaded from: classes.dex */
public final class ViewTreeSavedStateRegistryOwner {
    public static final void a(View view, SavedStateRegistryOwner savedStateRegistryOwner) {
        k.e(view, "<this>");
        view.setTag(R$id.view_tree_saved_state_registry_owner, savedStateRegistryOwner);
    }
}
