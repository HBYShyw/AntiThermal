package androidx.window.embedding;

import android.app.Activity;
import androidx.window.core.ExperimentalWindowApi;
import java.util.List;
import kotlin.Metadata;
import za.k;

/* compiled from: ActivityStack.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B\u001f\u0012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00020\f\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0004¢\u0006\u0004\b\u0014\u0010\u0015J\u0011\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002J\u0013\u0010\u0007\u001a\u00020\u00042\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\t\u001a\u00020\bH\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016R \u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00020\f8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0005\u0010\r\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0013\u001a\u00020\u00048\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0011\u0010\u0012¨\u0006\u0016"}, d2 = {"Landroidx/window/embedding/ActivityStack;", "", "Landroid/app/Activity;", "activity", "", "a", "other", "equals", "", "hashCode", "", "toString", "", "Ljava/util/List;", "getActivities$window_release", "()Ljava/util/List;", "activities", "b", "Z", "isEmpty", "<init>", "(Ljava/util/List;Z)V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class ActivityStack {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata and from toString */
    private final List<Activity> activities;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final boolean isEmpty;

    /* JADX WARN: Multi-variable type inference failed */
    public ActivityStack(List<? extends Activity> list, boolean z10) {
        k.e(list, "activities");
        this.activities = list;
        this.isEmpty = z10;
    }

    public final boolean a(Activity activity) {
        k.e(activity, "activity");
        return this.activities.contains(activity);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ActivityStack)) {
            return false;
        }
        ActivityStack activityStack = (ActivityStack) other;
        return (k.a(this.activities, activityStack.activities) || this.isEmpty == activityStack.isEmpty) ? false : true;
    }

    public int hashCode() {
        return ((this.isEmpty ? 1 : 0) * 31) + this.activities.hashCode();
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ActivityStack{");
        sb2.append("activities=" + this.activities);
        sb2.append("isEmpty=" + this.isEmpty + '}');
        String sb3 = sb2.toString();
        k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }
}
