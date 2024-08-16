package androidx.window.embedding;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import androidx.window.core.ExperimentalWindowApi;
import com.oplus.backup.sdk.common.utils.Constants;
import kotlin.Metadata;
import za.k;

/* compiled from: ActivityFilter.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006J\u0013\u0010\n\u001a\u00020\u00042\b\u0010\t\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\f\u001a\u00020\u000bH\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016R\u0017\u0010\u0013\u001a\u00020\u000f8\u0006¢\u0006\f\n\u0004\b\b\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012R\u0019\u0010\u0017\u001a\u0004\u0018\u00010\r8\u0006¢\u0006\f\n\u0004\b\u0005\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016¨\u0006\u0018"}, d2 = {"Landroidx/window/embedding/ActivityFilter;", "", "Landroid/content/Intent;", Constants.MessagerConstants.INTENT_KEY, "", "b", "Landroid/app/Activity;", "activity", "a", "other", "equals", "", "hashCode", "", "toString", "Landroid/content/ComponentName;", "Landroid/content/ComponentName;", "getComponentName", "()Landroid/content/ComponentName;", "componentName", "Ljava/lang/String;", "getIntentAction", "()Ljava/lang/String;", "intentAction", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class ActivityFilter {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata and from toString */
    private final ComponentName componentName;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata and from toString */
    private final String intentAction;

    public final boolean a(Activity activity) {
        k.e(activity, "activity");
        if (MatcherUtils.f4378a.a(activity, this.componentName)) {
            String str = this.intentAction;
            if (str != null) {
                Intent intent = activity.getIntent();
                if (k.a(str, intent != null ? intent.getAction() : null)) {
                }
            }
            return true;
        }
        return false;
    }

    public final boolean b(Intent intent) {
        k.e(intent, Constants.MessagerConstants.INTENT_KEY);
        if (!MatcherUtils.f4378a.b(intent.getComponent(), this.componentName)) {
            return false;
        }
        String str = this.intentAction;
        return str == null || k.a(str, intent.getAction());
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ActivityFilter)) {
            return false;
        }
        ActivityFilter activityFilter = (ActivityFilter) other;
        return k.a(this.componentName, activityFilter.componentName) && k.a(this.intentAction, activityFilter.intentAction);
    }

    public int hashCode() {
        int hashCode = this.componentName.hashCode() * 31;
        String str = this.intentAction;
        return hashCode + (str != null ? str.hashCode() : 0);
    }

    public String toString() {
        return "ActivityFilter(componentName=" + this.componentName + ", intentAction=" + this.intentAction + ')';
    }
}
