package androidx.window.embedding;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import androidx.window.core.ExperimentalWindowApi;
import kotlin.Metadata;
import za.k;

/* compiled from: SplitPairFilter.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001J\u0016\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002J\u0016\u0010\t\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0007J\u0013\u0010\u000b\u001a\u00020\u00052\b\u0010\n\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\r\u001a\u00020\fH\u0016J\b\u0010\u000f\u001a\u00020\u000eH\u0016R\u0017\u0010\u0014\u001a\u00020\u00108\u0006¢\u0006\f\n\u0004\b\t\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0016\u001a\u00020\u00108\u0006¢\u0006\f\n\u0004\b\u0006\u0010\u0011\u001a\u0004\b\u0015\u0010\u0013R\u0019\u0010\u001b\u001a\u0004\u0018\u00010\u000e8\u0006¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001a¨\u0006\u001c"}, d2 = {"Landroidx/window/embedding/SplitPairFilter;", "", "Landroid/app/Activity;", "primaryActivity", "secondaryActivity", "", "b", "Landroid/content/Intent;", "secondaryActivityIntent", "a", "other", "equals", "", "hashCode", "", "toString", "Landroid/content/ComponentName;", "Landroid/content/ComponentName;", "getPrimaryActivityName", "()Landroid/content/ComponentName;", "primaryActivityName", "getSecondaryActivityName", "secondaryActivityName", "c", "Ljava/lang/String;", "getSecondaryActivityIntentAction", "()Ljava/lang/String;", "secondaryActivityIntentAction", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SplitPairFilter {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final ComponentName primaryActivityName;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata and from toString */
    private final ComponentName secondaryActivityName;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata and from toString */
    private final String secondaryActivityAction;

    public final boolean a(Activity primaryActivity, Intent secondaryActivityIntent) {
        k.e(primaryActivity, "primaryActivity");
        k.e(secondaryActivityIntent, "secondaryActivityIntent");
        ComponentName componentName = primaryActivity.getComponentName();
        MatcherUtils matcherUtils = MatcherUtils.f4378a;
        if (!matcherUtils.b(componentName, this.primaryActivityName) || !matcherUtils.b(secondaryActivityIntent.getComponent(), this.secondaryActivityName)) {
            return false;
        }
        String str = this.secondaryActivityAction;
        return str == null || k.a(str, secondaryActivityIntent.getAction());
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x003e, code lost:
    
        if (a(r6, r7) != false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean b(Activity primaryActivity, Activity secondaryActivity) {
        k.e(primaryActivity, "primaryActivity");
        k.e(secondaryActivity, "secondaryActivity");
        MatcherUtils matcherUtils = MatcherUtils.f4378a;
        boolean z10 = true;
        boolean z11 = matcherUtils.b(primaryActivity.getComponentName(), this.primaryActivityName) && matcherUtils.b(secondaryActivity.getComponentName(), this.secondaryActivityName);
        if (secondaryActivity.getIntent() == null) {
            return z11;
        }
        if (z11) {
            Intent intent = secondaryActivity.getIntent();
            k.d(intent, "secondaryActivity.intent");
        }
        z10 = false;
        return z10;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SplitPairFilter)) {
            return false;
        }
        SplitPairFilter splitPairFilter = (SplitPairFilter) other;
        return k.a(this.primaryActivityName, splitPairFilter.primaryActivityName) && k.a(this.secondaryActivityName, splitPairFilter.secondaryActivityName) && k.a(this.secondaryActivityAction, splitPairFilter.secondaryActivityAction);
    }

    public int hashCode() {
        int hashCode = ((this.primaryActivityName.hashCode() * 31) + this.secondaryActivityName.hashCode()) * 31;
        String str = this.secondaryActivityAction;
        return hashCode + (str != null ? str.hashCode() : 0);
    }

    public String toString() {
        return "SplitPairFilter{primaryActivityName=" + this.primaryActivityName + ", secondaryActivityName=" + this.secondaryActivityName + ", secondaryActivityAction=" + this.secondaryActivityAction + '}';
    }
}
