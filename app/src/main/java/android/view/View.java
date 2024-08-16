package android.view;

import kotlin.Metadata;
import za.k;

/* compiled from: ViewTreeOnBackPressedDispatcherOwner.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u001b\u0010\u0004\u001a\u00020\u0003*\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Landroid/view/View;", "Landroidx/activity/k;", "onBackPressedDispatcherOwner", "Lma/f0;", "a", "(Landroid/view/View;Landroidx/activity/k;)V", "activity_release"}, k = 2, mv = {1, 7, 1})
/* renamed from: androidx.activity.l, reason: use source file name and from Kotlin metadata */
/* loaded from: classes.dex */
public final class View {
    public static final void a(android.view.View view, OnBackPressedDispatcherOwner onBackPressedDispatcherOwner) {
        k.e(view, "<this>");
        k.e(onBackPressedDispatcherOwner, "onBackPressedDispatcherOwner");
        view.setTag(R$id.view_tree_on_back_pressed_dispatcher_owner, onBackPressedDispatcherOwner);
    }
}
