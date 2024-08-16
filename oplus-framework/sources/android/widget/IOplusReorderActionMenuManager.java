package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.Menu;

/* loaded from: classes.dex */
public interface IOplusReorderActionMenuManager extends IOplusCommonFeature {
    public static final IOplusReorderActionMenuManager DEFAULT = new IOplusReorderActionMenuManager() { // from class: android.widget.IOplusReorderActionMenuManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusReorderActionMenuManager;
    }

    default IOplusReorderActionMenuManager getDefault() {
        return DEFAULT;
    }

    default IOplusReorderActionMenuManager newInstance() {
        return DEFAULT;
    }

    default void init(Context context) {
    }

    default void onInitializeReorderActionMenu(Menu menu, Context context, TextView textView) {
    }

    default boolean isOplusReorderActionMenu(Intent intent) {
        return false;
    }

    default void hookFireIntent(TextView textView, Intent intent) {
    }

    default boolean raiseOplusMenuPriority(int order, CharSequence label, Intent intent, ResolveInfo resolveInfo, Menu menu) {
        return false;
    }
}
