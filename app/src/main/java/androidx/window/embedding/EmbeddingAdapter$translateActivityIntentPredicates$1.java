package androidx.window.embedding;

import android.app.Activity;
import android.content.Intent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.Metadata;
import ya.p;
import za.Lambda;
import za.k;

/* compiled from: EmbeddingAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"Landroid/app/Activity;", "first", "Landroid/content/Intent;", "second", "", "a", "(Landroid/app/Activity;Landroid/content/Intent;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class EmbeddingAdapter$translateActivityIntentPredicates$1 extends Lambda implements p<Activity, Intent, Boolean> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ Set<SplitPairFilter> f4354e;

    @Override // ya.p
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke(Activity activity, Intent intent) {
        k.e(activity, "first");
        k.e(intent, "second");
        Set<SplitPairFilter> set = this.f4354e;
        boolean z10 = false;
        if (!(set instanceof Collection) || !set.isEmpty()) {
            Iterator<T> it = set.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (((SplitPairFilter) it.next()).a(activity, intent)) {
                    z10 = true;
                    break;
                }
            }
        }
        return Boolean.valueOf(z10);
    }
}
