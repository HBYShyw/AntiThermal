package androidx.window.embedding;

import android.view.WindowMetrics;
import kotlin.Metadata;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: EmbeddingAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"Landroid/view/WindowMetrics;", "windowMetrics", "", "a", "(Landroid/view/WindowMetrics;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class EmbeddingAdapter$translateParentMetricsPredicate$1 extends Lambda implements l<WindowMetrics, Boolean> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ SplitRule f4358e;

    @Override // ya.l
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke(WindowMetrics windowMetrics) {
        k.e(windowMetrics, "windowMetrics");
        return Boolean.valueOf(this.f4358e.a(windowMetrics));
    }
}
