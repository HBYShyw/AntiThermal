package androidx.window.embedding;

import android.content.Intent;
import androidx.window.core.ExperimentalWindowApi;
import java.util.Set;
import kotlin.Metadata;
import za.k;

/* compiled from: SplitPlaceholderRule.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001J\u0013\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016R\u0017\u0010\r\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0010\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0016\u001a\u00020\u00068\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u001d\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00180\u00178\u0006¢\u0006\f\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001c¨\u0006\u001e"}, d2 = {"Landroidx/window/embedding/SplitPlaceholderRule;", "Landroidx/window/embedding/SplitRule;", "", "other", "", "equals", "", "hashCode", "Landroid/content/Intent;", "f", "Landroid/content/Intent;", "getPlaceholderIntent", "()Landroid/content/Intent;", "placeholderIntent", "g", "Z", "isSticky", "()Z", "h", "I", "getFinishPrimaryWithSecondary", "()I", "finishPrimaryWithSecondary", "", "Landroidx/window/embedding/ActivityFilter;", "i", "Ljava/util/Set;", "getFilters", "()Ljava/util/Set;", "filters", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SplitPlaceholderRule extends SplitRule {

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private final Intent placeholderIntent;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private final boolean isSticky;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private final int finishPrimaryWithSecondary;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private final Set<ActivityFilter> filters;

    @Override // androidx.window.embedding.SplitRule
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SplitPlaceholderRule) || !super.equals(other)) {
            return false;
        }
        SplitPlaceholderRule splitPlaceholderRule = (SplitPlaceholderRule) other;
        return k.a(this.placeholderIntent, splitPlaceholderRule.placeholderIntent) && this.isSticky == splitPlaceholderRule.isSticky && this.finishPrimaryWithSecondary == splitPlaceholderRule.finishPrimaryWithSecondary && k.a(this.filters, splitPlaceholderRule.filters);
    }

    @Override // androidx.window.embedding.SplitRule
    public int hashCode() {
        return (((((((super.hashCode() * 31) + this.placeholderIntent.hashCode()) * 31) + Boolean.hashCode(this.isSticky)) * 31) + Integer.hashCode(this.finishPrimaryWithSecondary)) * 31) + this.filters.hashCode();
    }
}
