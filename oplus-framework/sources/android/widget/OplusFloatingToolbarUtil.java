package android.widget;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.Menu;

/* loaded from: classes.dex */
public class OplusFloatingToolbarUtil implements IOplusFloatingToolbarUtil {
    private static final String SEARCH_PACKAGE_NAME = "com.heytap.browser";

    @Override // android.widget.IOplusFloatingToolbarUtil
    public boolean setSearchMenuItem(int index, Intent intent, CharSequence title, ResolveInfo resolveInfo, Menu menu) {
        if (!SEARCH_PACKAGE_NAME.equals(resolveInfo.activityInfo.packageName)) {
            return false;
        }
        menu.add(0, 0, index, title).setIntent(intent).setShowAsAction(1);
        return true;
    }

    @Override // android.widget.IOplusFloatingToolbarUtil
    public boolean[] handleCursorControllersEnabled(boolean isMenuEnabled, boolean isInsertMenuEnabled, boolean isSelectMenuEnabled, boolean insertionControllerEnabled, boolean selectionControllerEnabled) {
        if (!isMenuEnabled) {
            insertionControllerEnabled = false;
            selectionControllerEnabled = false;
        } else if (!isInsertMenuEnabled) {
            insertionControllerEnabled = false;
        } else if (!isSelectMenuEnabled) {
            selectionControllerEnabled = false;
        }
        return new boolean[]{insertionControllerEnabled, selectionControllerEnabled};
    }

    @Override // android.widget.IOplusFloatingToolbarUtil
    public boolean needAllSelected(boolean needAllSelected) {
        return needAllSelected;
    }

    @Override // android.widget.IOplusFloatingToolbarUtil
    public boolean needHook() {
        return true;
    }

    @Override // android.widget.IOplusFloatingToolbarUtil
    public void updateSelectAllItem(Menu menu, TextView textView, int menuItemOrderSelect) {
        boolean canSelect = textView.canSelectText() && !textView.hasPasswordTransformationMethod() && textView.getSelectionStart() == textView.getSelectionEnd();
        boolean selectItemExists = menu.findItem(201457773) != null;
        if (canSelect && !selectItemExists) {
            menu.add(0, 201457773, menuItemOrderSelect, 201588938).setShowAsAction(1);
        } else if (!canSelect && selectItemExists) {
            menu.removeItem(201457773);
        }
    }
}
