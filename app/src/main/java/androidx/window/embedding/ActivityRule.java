package androidx.window.embedding;

import androidx.window.core.ExperimentalWindowApi;
import java.util.Set;
import kotlin.Metadata;
import za.k;

/* compiled from: ActivityRule.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001J\u0013\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016R\u0017\u0010\f\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000bR\u001d\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\r8\u0006¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0014"}, d2 = {"Landroidx/window/embedding/ActivityRule;", "Landroidx/window/embedding/EmbeddingRule;", "", "other", "", "equals", "", "hashCode", "a", "Z", "getAlwaysExpand", "()Z", "alwaysExpand", "", "Landroidx/window/embedding/ActivityFilter;", "b", "Ljava/util/Set;", "getFilters", "()Ljava/util/Set;", "filters", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class ActivityRule extends EmbeddingRule {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final boolean alwaysExpand;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final Set<ActivityFilter> filters;

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ActivityRule)) {
            return false;
        }
        ActivityRule activityRule = (ActivityRule) other;
        return k.a(this.filters, activityRule.filters) && this.alwaysExpand == activityRule.alwaysExpand;
    }

    public int hashCode() {
        return (this.filters.hashCode() * 31) + Boolean.hashCode(this.alwaysExpand);
    }
}
