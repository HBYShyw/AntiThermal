package android.server.accessibility;

import android.content.ComponentName;
import com.oplus.view.OplusWindowUtils;
import java.util.Set;

/* loaded from: classes.dex */
public class AccessibilityUserStateExtImpl implements IAccessibilityUserStateExt {
    private final ComponentName mDirectAccessibilityServiceComponentName = new ComponentName(OplusWindowUtils.PACKAGE_DIRECTSERVICE, "com.coloros.colordirectservice.ColorTextAccessibilityService");

    public AccessibilityUserStateExtImpl(Object base) {
    }

    public boolean getAccessibilityUserState(Set<ComponentName> enabledServices) {
        return !enabledServices.isEmpty() && enabledServices.contains(this.mDirectAccessibilityServiceComponentName);
    }
}
