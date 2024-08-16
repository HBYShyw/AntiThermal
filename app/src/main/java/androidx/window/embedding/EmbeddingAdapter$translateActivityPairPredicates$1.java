package androidx.window.embedding;

import android.app.Activity;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.Metadata;
import ya.p;
import za.Lambda;
import za.k;

/* compiled from: EmbeddingAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"Landroid/app/Activity;", "first", "second", "", "a", "(Landroid/app/Activity;Landroid/app/Activity;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class EmbeddingAdapter$translateActivityPairPredicates$1 extends Lambda implements p<Activity, Activity, Boolean> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ Set<SplitPairFilter> f4355e;

    @Override // ya.p
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke(Activity activity, Activity activity2) {
        k.e(activity, "first");
        k.e(activity2, "second");
        Set<SplitPairFilter> set = this.f4355e;
        boolean z10 = false;
        if (!(set instanceof Collection) || !set.isEmpty()) {
            Iterator<T> it = set.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (((SplitPairFilter) it.next()).b(activity, activity2)) {
                    z10 = true;
                    break;
                }
            }
        }
        return Boolean.valueOf(z10);
    }
}
