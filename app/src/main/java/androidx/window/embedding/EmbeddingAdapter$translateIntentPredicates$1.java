package androidx.window.embedding;

import android.content.Intent;
import com.oplus.backup.sdk.common.utils.Constants;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.Metadata;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: EmbeddingAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"Landroid/content/Intent;", Constants.MessagerConstants.INTENT_KEY, "", "a", "(Landroid/content/Intent;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class EmbeddingAdapter$translateIntentPredicates$1 extends Lambda implements l<Intent, Boolean> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ Set<ActivityFilter> f4357e;

    @Override // ya.l
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke(Intent intent) {
        k.e(intent, Constants.MessagerConstants.INTENT_KEY);
        Set<ActivityFilter> set = this.f4357e;
        boolean z10 = false;
        if (!(set instanceof Collection) || !set.isEmpty()) {
            Iterator<T> it = set.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (((ActivityFilter) it.next()).b(intent)) {
                    z10 = true;
                    break;
                }
            }
        }
        return Boolean.valueOf(z10);
    }
}
