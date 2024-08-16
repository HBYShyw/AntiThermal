package androidx.lifecycle;

import android.view.View;
import androidx.lifecycle.viewmodel.R$id;

/* compiled from: ViewTreeViewModelStoreOwner.java */
/* renamed from: androidx.lifecycle.m0, reason: use source file name */
/* loaded from: classes.dex */
public class ViewTreeViewModelStoreOwner {
    public static void a(View view, ViewModelStoreOwner viewModelStoreOwner) {
        view.setTag(R$id.view_tree_view_model_store_owner, viewModelStoreOwner);
    }
}
