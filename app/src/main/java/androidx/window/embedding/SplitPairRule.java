package androidx.window.embedding;

import androidx.window.core.ExperimentalWindowApi;
import java.util.Set;
import kotlin.Metadata;
import za.k;

/* compiled from: SplitPairRule.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001J\u0013\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016R\u0017\u0010\f\u001a\u00020\u00068\u0006¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u000f\u001a\u00020\u00068\u0006¢\u0006\f\n\u0004\b\r\u0010\t\u001a\u0004\b\u000e\u0010\u000bR\u0017\u0010\u0014\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013R\u001d\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00160\u00158\u0006¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001a¨\u0006\u001c"}, d2 = {"Landroidx/window/embedding/SplitPairRule;", "Landroidx/window/embedding/SplitRule;", "", "other", "", "equals", "", "hashCode", "f", "I", "getFinishPrimaryWithSecondary", "()I", "finishPrimaryWithSecondary", "g", "getFinishSecondaryWithPrimary", "finishSecondaryWithPrimary", "h", "Z", "getClearTop", "()Z", "clearTop", "", "Landroidx/window/embedding/SplitPairFilter;", "i", "Ljava/util/Set;", "getFilters", "()Ljava/util/Set;", "filters", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SplitPairRule extends SplitRule {

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private final int finishPrimaryWithSecondary;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private final int finishSecondaryWithPrimary;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private final boolean clearTop;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private final Set<SplitPairFilter> filters;

    @Override // androidx.window.embedding.SplitRule
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SplitPairRule) || !super.equals(other)) {
            return false;
        }
        SplitPairRule splitPairRule = (SplitPairRule) other;
        return k.a(this.filters, splitPairRule.filters) && this.finishPrimaryWithSecondary == splitPairRule.finishPrimaryWithSecondary && this.finishSecondaryWithPrimary == splitPairRule.finishSecondaryWithPrimary && this.clearTop == splitPairRule.clearTop;
    }

    @Override // androidx.window.embedding.SplitRule
    public int hashCode() {
        return (((((((super.hashCode() * 31) + this.filters.hashCode()) * 31) + Integer.hashCode(this.finishPrimaryWithSecondary)) * 31) + Integer.hashCode(this.finishSecondaryWithPrimary)) * 31) + Boolean.hashCode(this.clearTop);
    }
}
