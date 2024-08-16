package androidx.window.embedding;

import android.app.Activity;
import androidx.window.core.ExperimentalWindowApi;
import kotlin.Metadata;
import za.k;

/* compiled from: SplitInfo.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B!\b\u0000\u0012\u0006\u0010\u0010\u001a\u00020\f\u0012\u0006\u0010\u0013\u001a\u00020\f\u0012\u0006\u0010\u0019\u001a\u00020\u0014¢\u0006\u0004\b\u001a\u0010\u001bJ\u0011\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002J\u0013\u0010\u0007\u001a\u00020\u00042\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\t\u001a\u00020\bH\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016R\u0017\u0010\u0010\u001a\u00020\f8\u0006¢\u0006\f\n\u0004\b\u0005\u0010\r\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0013\u001a\u00020\f8\u0006¢\u0006\f\n\u0004\b\u0011\u0010\r\u001a\u0004\b\u0012\u0010\u000fR\u0017\u0010\u0019\u001a\u00020\u00148\u0006¢\u0006\f\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018¨\u0006\u001c"}, d2 = {"Landroidx/window/embedding/SplitInfo;", "", "Landroid/app/Activity;", "activity", "", "a", "other", "equals", "", "hashCode", "", "toString", "Landroidx/window/embedding/ActivityStack;", "Landroidx/window/embedding/ActivityStack;", "getPrimaryActivityStack", "()Landroidx/window/embedding/ActivityStack;", "primaryActivityStack", "b", "getSecondaryActivityStack", "secondaryActivityStack", "", "c", "F", "getSplitRatio", "()F", "splitRatio", "<init>", "(Landroidx/window/embedding/ActivityStack;Landroidx/window/embedding/ActivityStack;F)V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SplitInfo {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata and from toString */
    private final ActivityStack primaryActivityStack;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final ActivityStack secondaryActivityStack;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final float splitRatio;

    public SplitInfo(ActivityStack activityStack, ActivityStack activityStack2, float f10) {
        k.e(activityStack, "primaryActivityStack");
        k.e(activityStack2, "secondaryActivityStack");
        this.primaryActivityStack = activityStack;
        this.secondaryActivityStack = activityStack2;
        this.splitRatio = f10;
    }

    public final boolean a(Activity activity) {
        k.e(activity, "activity");
        return this.primaryActivityStack.a(activity) || this.secondaryActivityStack.a(activity);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SplitInfo)) {
            return false;
        }
        SplitInfo splitInfo = (SplitInfo) other;
        if (k.a(this.primaryActivityStack, splitInfo.primaryActivityStack) && k.a(this.secondaryActivityStack, splitInfo.secondaryActivityStack)) {
            return (this.splitRatio > splitInfo.splitRatio ? 1 : (this.splitRatio == splitInfo.splitRatio ? 0 : -1)) == 0;
        }
        return false;
    }

    public int hashCode() {
        return (((this.primaryActivityStack.hashCode() * 31) + this.secondaryActivityStack.hashCode()) * 31) + Float.hashCode(this.splitRatio);
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("SplitInfo:{");
        sb2.append("primaryActivityStack=" + this.primaryActivityStack + ',');
        sb2.append("secondaryActivityStack=" + this.secondaryActivityStack + ',');
        sb2.append("splitRatio=" + this.splitRatio + '}');
        String sb3 = sb2.toString();
        k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }
}
