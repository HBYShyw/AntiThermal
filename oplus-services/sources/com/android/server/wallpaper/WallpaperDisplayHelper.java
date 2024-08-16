package com.android.server.wallpaper;

import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Binder;
import android.os.Debug;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayInfo;
import com.android.server.wm.WindowManagerInternal;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WallpaperDisplayHelper {
    private static final String TAG = "WallpaperDisplayHelper";
    private final SparseArray<DisplayData> mDisplayDatas = new SparseArray<>();
    private final DisplayManager mDisplayManager;
    private IWallpaperManagerServiceExt mWallpaperManagerServiceExt;
    private final WindowManagerInternal mWindowManagerInternal;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class DisplayData {
        final int mDisplayId;
        int mWidth = -1;
        int mHeight = -1;
        final Rect mPadding = new Rect(0, 0, 0, 0);

        DisplayData(int i) {
            this.mDisplayId = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WallpaperDisplayHelper(DisplayManager displayManager, WindowManagerInternal windowManagerInternal, IWallpaperManagerServiceExt iWallpaperManagerServiceExt) {
        this.mDisplayManager = displayManager;
        this.mWindowManagerInternal = windowManagerInternal;
        this.mWallpaperManagerServiceExt = iWallpaperManagerServiceExt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayData getDisplayDataOrCreate(int i) {
        DisplayData displayData = this.mDisplayDatas.get(i);
        if (displayData != null) {
            return displayData;
        }
        DisplayData displayData2 = new DisplayData(i);
        ensureSaneWallpaperDisplaySize(displayData2, i);
        this.mDisplayDatas.append(i, displayData2);
        return displayData2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeDisplayData(int i) {
        this.mDisplayDatas.remove(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureSaneWallpaperDisplaySize(DisplayData displayData, int i) {
        int maximumSizeDimension = getMaximumSizeDimension(i);
        if (displayData.mWidth < maximumSizeDimension) {
            displayData.mWidth = maximumSizeDimension;
        }
        if (displayData.mHeight < maximumSizeDimension) {
            displayData.mHeight = maximumSizeDimension;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMaximumSizeDimension(int i) {
        Display display = this.mDisplayManager.getDisplay(i);
        if (display == null) {
            Slog.w(TAG, "Invalid displayId=" + i + " " + Debug.getCallers(4));
            display = this.mDisplayManager.getDisplay(0);
        }
        int maximumSizeDimension = display.getMaximumSizeDimension();
        this.mWallpaperManagerServiceExt.saveMaximumSizeDimension(i, maximumSizeDimension);
        return maximumSizeDimension;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachDisplayData(Consumer<DisplayData> consumer) {
        for (int size = this.mDisplayDatas.size() - 1; size >= 0; size--) {
            consumer.accept(this.mDisplayDatas.valueAt(size));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Display[] getDisplays(WallpaperData wallpaperData) {
        return this.mWallpaperManagerServiceExt.getDisplays(this.mDisplayManager, wallpaperData);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayManager getDisplayManager() {
        return this.mDisplayManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayInfo getDisplayInfo(int i) {
        DisplayInfo displayInfo = new DisplayInfo();
        this.mDisplayManager.getDisplay(i).getDisplayInfo(displayInfo);
        return displayInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUsableDisplay(int i, int i2) {
        return isUsableDisplay(this.mDisplayManager.getDisplay(i), i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUsableDisplay(Display display, int i) {
        if (display == null || !display.hasAccess(i)) {
            return false;
        }
        int displayId = display.getDisplayId();
        if (displayId == 0 || this.mWallpaperManagerServiceExt.isUsableDisplay(display)) {
            return true;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mWindowManagerInternal.shouldShowSystemDecorOnDisplay(displayId);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidDisplay(int i) {
        return this.mDisplayManager.getDisplay(i) != null;
    }
}
