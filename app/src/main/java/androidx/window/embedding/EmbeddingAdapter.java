package androidx.window.embedding;

import androidx.window.core.ExperimentalWindowApi;
import androidx.window.core.PredicateAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.s;
import za.k;

/* compiled from: EmbeddingAdapter.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0001\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u000b\u001a\u00020\t¢\u0006\u0004\b\f\u0010\rJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006R\u0014\u0010\u000b\u001a\u00020\t8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010\n¨\u0006\u000e"}, d2 = {"Landroidx/window/embedding/EmbeddingAdapter;", "", "Landroidx/window/extensions/embedding/SplitInfo;", "splitInfo", "Landroidx/window/embedding/SplitInfo;", "a", "", "splitInfoList", "b", "Landroidx/window/core/PredicateAdapter;", "Landroidx/window/core/PredicateAdapter;", "predicateAdapter", "<init>", "(Landroidx/window/core/PredicateAdapter;)V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class EmbeddingAdapter {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final PredicateAdapter predicateAdapter;

    public EmbeddingAdapter(PredicateAdapter predicateAdapter) {
        k.e(predicateAdapter, "predicateAdapter");
        this.predicateAdapter = predicateAdapter;
    }

    private final SplitInfo a(androidx.window.extensions.embedding.SplitInfo splitInfo) {
        boolean z10;
        androidx.window.extensions.embedding.ActivityStack primaryActivityStack = splitInfo.getPrimaryActivityStack();
        k.d(primaryActivityStack, "splitInfo.primaryActivityStack");
        boolean z11 = false;
        try {
            z10 = primaryActivityStack.isEmpty();
        } catch (NoSuchMethodError unused) {
            z10 = false;
        }
        List activities = primaryActivityStack.getActivities();
        k.d(activities, "primaryActivityStack.activities");
        ActivityStack activityStack = new ActivityStack(activities, z10);
        androidx.window.extensions.embedding.ActivityStack secondaryActivityStack = splitInfo.getSecondaryActivityStack();
        k.d(secondaryActivityStack, "splitInfo.secondaryActivityStack");
        try {
            z11 = secondaryActivityStack.isEmpty();
        } catch (NoSuchMethodError unused2) {
        }
        List activities2 = secondaryActivityStack.getActivities();
        k.d(activities2, "secondaryActivityStack.activities");
        return new SplitInfo(activityStack, new ActivityStack(activities2, z11), splitInfo.getSplitRatio());
    }

    public final List<SplitInfo> b(List<? extends androidx.window.extensions.embedding.SplitInfo> splitInfoList) {
        int u7;
        k.e(splitInfoList, "splitInfoList");
        u7 = s.u(splitInfoList, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = splitInfoList.iterator();
        while (it.hasNext()) {
            arrayList.add(a((androidx.window.extensions.embedding.SplitInfo) it.next()));
        }
        return arrayList;
    }
}
