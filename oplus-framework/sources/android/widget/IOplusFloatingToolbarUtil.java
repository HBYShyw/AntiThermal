package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.Menu;

/* loaded from: classes.dex */
public interface IOplusFloatingToolbarUtil extends IOplusCommonFeature {
    public static final IOplusFloatingToolbarUtil DEFAULT = new IOplusFloatingToolbarUtil() { // from class: android.widget.IOplusFloatingToolbarUtil.1
    };
    public static final String NAME = "IOplusFloatingToolbarUtil";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusFloatingToolbarUtil;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean setSearchMenuItem(int index, Intent intent, CharSequence title, ResolveInfo resolveInfo, Menu menu) {
        return false;
    }

    default boolean[] handleCursorControllersEnabled(boolean isMenuEnabled, boolean isInsertMenuEnabled, boolean isSelectMenuEnabled, boolean insertionControllerEnabled, boolean selectionControllerEnabled) {
        return new boolean[]{insertionControllerEnabled, selectionControllerEnabled};
    }

    default boolean needAllSelected(boolean needAllSelected) {
        return false;
    }

    default boolean needHook() {
        return false;
    }

    default void updateSelectAllItem(Menu menu, TextView textView, int menuItemOrderSelect) {
    }
}
