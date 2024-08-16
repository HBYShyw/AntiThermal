package androidx.lifecycle;

import android.view.View;
import androidx.lifecycle.runtime.R$id;

/* compiled from: ViewTreeLifecycleOwner.java */
/* renamed from: androidx.lifecycle.l0, reason: use source file name */
/* loaded from: classes.dex */
public class ViewTreeLifecycleOwner {
    public static void a(View view, o oVar) {
        view.setTag(R$id.view_tree_lifecycle_owner, oVar);
    }
}
