package androidx.window.embedding;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import androidx.window.core.ExperimentalWindowApi;
import com.oplus.deepthinker.sdk.app.awareness.fence.impl.SpecifiedLocationFence;
import kotlin.Metadata;
import sd.StringsJVM;
import sd.v;
import za.k;

/* compiled from: MatcherUtils.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\bÁ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0010\u0010\u0011J\u0018\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0002J!\u0010\n\u001a\u00020\u00052\b\u0010\b\u001a\u0004\u0018\u00010\u00072\u0006\u0010\t\u001a\u00020\u0007H\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001f\u0010\u000e\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\u0007H\u0000¢\u0006\u0004\b\u000e\u0010\u000f¨\u0006\u0012"}, d2 = {"Landroidx/window/embedding/MatcherUtils;", "", "", "name", SpecifiedLocationFence.BUNDLE_KEY_PATTERN, "", "c", "Landroid/content/ComponentName;", "activityComponent", "ruleComponent", "b", "(Landroid/content/ComponentName;Landroid/content/ComponentName;)Z", "Landroid/app/Activity;", "activity", "a", "(Landroid/app/Activity;Landroid/content/ComponentName;)Z", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class MatcherUtils {

    /* renamed from: a, reason: collision with root package name */
    public static final MatcherUtils f4378a = new MatcherUtils();

    private MatcherUtils() {
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0045  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final boolean c(String name, String pattern) {
        boolean I;
        int V;
        int a02;
        boolean z10;
        boolean D;
        boolean q10;
        I = v.I(pattern, "*", false, 2, null);
        if (!I) {
            return false;
        }
        if (k.a(pattern, "*")) {
            return true;
        }
        V = v.V(pattern, "*", 0, false, 6, null);
        a02 = v.a0(pattern, "*", 0, false, 6, null);
        if (V == a02) {
            q10 = StringsJVM.q(pattern, "*", false, 2, null);
            if (q10) {
                z10 = true;
                if (!z10) {
                    String substring = pattern.substring(0, pattern.length() - 1);
                    k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                    D = StringsJVM.D(name, substring, false, 2, null);
                    return D;
                }
                throw new IllegalArgumentException("Name pattern with a wildcard must only contain a single wildcard in the end".toString());
            }
        }
        z10 = false;
        if (!z10) {
        }
    }

    public final boolean a(Activity activity, ComponentName ruleComponent) {
        ComponentName component;
        k.e(activity, "activity");
        k.e(ruleComponent, "ruleComponent");
        if (b(activity.getComponentName(), ruleComponent)) {
            return true;
        }
        Intent intent = activity.getIntent();
        if (intent == null || (component = intent.getComponent()) == null) {
            return false;
        }
        return f4378a.b(component, ruleComponent);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x008a A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean b(ComponentName activityComponent, ComponentName ruleComponent) {
        boolean I;
        boolean z10;
        boolean z11;
        k.e(ruleComponent, "ruleComponent");
        if (activityComponent == null) {
            return k.a(ruleComponent.getPackageName(), "*") && k.a(ruleComponent.getClassName(), "*");
        }
        String componentName = activityComponent.toString();
        k.d(componentName, "activityComponent.toString()");
        I = v.I(componentName, "*", false, 2, null);
        if (!I) {
            if (!k.a(activityComponent.getPackageName(), ruleComponent.getPackageName())) {
                String packageName = activityComponent.getPackageName();
                k.d(packageName, "activityComponent.packageName");
                String packageName2 = ruleComponent.getPackageName();
                k.d(packageName2, "ruleComponent.packageName");
                if (!c(packageName, packageName2)) {
                    z10 = false;
                    if (!k.a(activityComponent.getClassName(), ruleComponent.getClassName())) {
                        String className = activityComponent.getClassName();
                        k.d(className, "activityComponent.className");
                        String className2 = ruleComponent.getClassName();
                        k.d(className2, "ruleComponent.className");
                        if (!c(className, className2)) {
                            z11 = false;
                            return !z10 && z11;
                        }
                    }
                    z11 = true;
                    if (z10) {
                    }
                }
            }
            z10 = true;
            if (!k.a(activityComponent.getClassName(), ruleComponent.getClassName())) {
            }
            z11 = true;
            if (z10) {
            }
        }
        throw new IllegalArgumentException("Wildcard can only be part of the rule.".toString());
    }
}
