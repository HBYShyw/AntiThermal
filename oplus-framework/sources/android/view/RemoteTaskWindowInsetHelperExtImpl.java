package android.view;

import android.content.Context;
import android.content.res.OplusThemeResources;
import android.content.res.Resources;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;

/* loaded from: classes.dex */
class RemoteTaskWindowInsetHelperExtImpl implements IRemoteTaskWindowInsetHelperExt {
    public static final int NAV_BAR_MODE_THREE_BUTTON = 0;
    private static final String VIRTUAL_DISPLAY_PREFIX = "MS_VIRTUAL_DISPLAY";
    private final Context mContext;
    private int mCurrentDisplayId;
    private int mLastDisplayId;

    public RemoteTaskWindowInsetHelperExtImpl(Object base) {
        Context context = (Context) base;
        this.mContext = context;
        this.mLastDisplayId = context.getDisplayId();
        this.mCurrentDisplayId = context.getDisplayId();
    }

    public void updateDisplayId(int id) {
        int i = this.mCurrentDisplayId;
        if (i != id) {
            this.mLastDisplayId = i;
            this.mCurrentDisplayId = id;
        }
    }

    public InsetsSource updateInsetSourceIfNeeded(InsetsSource source, int type, Rect frame) {
        if (!isDisplayChanged() || source == null || !isLTWVirtualDisplay()) {
            return source;
        }
        if (this.mCurrentDisplayId <= 0) {
            switch (type) {
                case 1:
                    int rotation = frame.right;
                    source.setFrame(0, 0, rotation, getStatusBarHeight());
                    break;
                case 2:
                    int rotation2 = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(this.mCurrentDisplayId).getRotation();
                    switch (rotation2) {
                        case 0:
                        case 2:
                            source.setFrame(0, frame.bottom - getNavigationBarHeight(1), frame.right, frame.bottom);
                            break;
                        case 1:
                            if (getNavigationBarInteractionMode() == 0) {
                                source.setFrame(frame.right - getNavigationBarHeight(2), 0, frame.right, frame.bottom);
                                break;
                            } else {
                                source.setFrame(0, frame.bottom - getNavigationBarHeight(2), frame.right, frame.bottom);
                                break;
                            }
                        case 3:
                            if (getNavigationBarInteractionMode() != 0) {
                                source.setFrame(0, frame.bottom - getNavigationBarHeight(2), frame.right, frame.bottom);
                                break;
                            } else {
                                source.setFrame(0, 0, getNavigationBarHeight(2), frame.bottom);
                                break;
                            }
                    }
            }
        } else {
            source.setFrame(0, 0, 0, 0);
        }
        return source;
    }

    public boolean isDisplayChanged() {
        return this.mCurrentDisplayId != this.mLastDisplayId;
    }

    private boolean isLTWVirtualDisplay() {
        String displayName;
        Display lastDisplay = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(this.mLastDisplayId);
        if (lastDisplay == null || (displayName = lastDisplay.getName()) == null || !displayName.startsWith(VIRTUAL_DISPLAY_PREFIX)) {
            return false;
        }
        return true;
    }

    private int getStatusBarHeight() {
        int statusBarHeightId = this.mContext.getResources().getIdentifier("status_bar_height", "dimen", OplusThemeResources.FRAMEWORK_PACKAGE);
        return this.mContext.getResources().getDimensionPixelSize(statusBarHeightId);
    }

    private int getNavigationBarHeight(int orientation) {
        String name;
        if (orientation == 1) {
            name = "navigation_bar_height";
        } else {
            name = "navigation_bar_height_landscape";
        }
        int navBarHeightId = this.mContext.getResources().getIdentifier(name, "dimen", OplusThemeResources.FRAMEWORK_PACKAGE);
        return this.mContext.getResources().getDimensionPixelSize(navBarHeightId);
    }

    private int getNavigationBarInteractionMode() {
        Resources resources = this.mContext.getResources();
        int resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", OplusThemeResources.FRAMEWORK_PACKAGE);
        if (resourceId > 0) {
            return resources.getInteger(resourceId);
        }
        return 0;
    }
}
