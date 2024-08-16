package i1;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;
import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* compiled from: COUIAccessibilityUtil.java */
/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    static final TextUtils.SimpleStringSplitter f12487a = new TextUtils.SimpleStringSplitter(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);

    public static Set<ComponentName> a(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
        if (string == null) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        TextUtils.SimpleStringSplitter simpleStringSplitter = f12487a;
        simpleStringSplitter.setString(string);
        while (simpleStringSplitter.hasNext()) {
            ComponentName unflattenFromString = ComponentName.unflattenFromString(simpleStringSplitter.next());
            if (unflattenFromString != null) {
                hashSet.add(unflattenFromString);
            }
        }
        return hashSet;
    }

    private static boolean b(Context context) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        return accessibilityManager != null && accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled();
    }

    public static boolean c(Context context) {
        return d(context) && b(context);
    }

    private static boolean d(Context context) {
        Set<ComponentName> a10 = a(context);
        if (a10 != null && !a10.isEmpty()) {
            Iterator<ComponentName> it = a10.iterator();
            while (it.hasNext()) {
                if (TextUtils.equals(it.next().getPackageName(), "com.google.android.marvin.talkback")) {
                    return true;
                }
            }
        }
        return false;
    }
}
