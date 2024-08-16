package com.oplus.view;

import android.os.RemoteException;
import android.view.OplusBaseLayoutParams;
import android.view.SurfaceControl;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import com.oplus.util.OplusTypeCastingHelper;
import com.oplus.view.IOplusWindowManagerConstans;
import com.oplus.widget.OplusMaxLinearLayout;
import com.oplus.zoomwindow.OplusZoomWindowManager;

/* loaded from: classes.dex */
public final class OplusWindowManager implements IOplusWindowManagerConstans {
    public static final String TAG = "OplusWindowManager";

    /* loaded from: classes.dex */
    public static class LayoutParams extends IOplusWindowManagerConstans.BaseLayoutParams {
    }

    public static void setNoMoveAnimation(WindowManager.LayoutParams lp, boolean noAnim) {
        if (noAnim) {
            lp.privateFlags |= 64;
        } else {
            lp.privateFlags &= -65;
        }
    }

    public static void setNavigationBarColor(WindowManager.LayoutParams lp, int color, boolean update) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            if (update) {
                cblp.mOplusLayoutParams.setUpdateNavigationBar(update);
                cblp.mOplusLayoutParams.setNavigationBarColor(color);
            } else if (isColorLight(color)) {
                cblp.navigationBarVisibility |= Integer.MIN_VALUE;
                cblp.navigationBarColor = color;
            } else {
                cblp.navigationBarVisibility &= OplusMaxLinearLayout.INVALID_MAX_VALUE;
                cblp.navigationBarColor = color;
            }
        }
    }

    public static boolean updateDarkNavigationBar(WindowManager.LayoutParams lp) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            return cblp.mOplusLayoutParams.hasNavigationBar() && cblp.mOplusLayoutParams.isUpdateNavigationBar() && !isColorLight(cblp.mOplusLayoutParams.getNavigationBarColor());
        }
        return true;
    }

    public static void setHasStatusBar(WindowManager.LayoutParams lp, boolean value) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            cblp.mOplusLayoutParams.setHasStatusBar(value);
        }
    }

    public static void setHasNavigationBar(WindowManager.LayoutParams lp, boolean value) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            cblp.mOplusLayoutParams.setHasNavigationBar(value);
        }
    }

    public static void setCustomSystemBar(WindowManager.LayoutParams lp, boolean value) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            cblp.mOplusLayoutParams.setCustomSystemBar(value);
        }
    }

    public static void setSystemAppWindow(WindowManager.LayoutParams lp, boolean value) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            cblp.mOplusLayoutParams.setSystemAppWindow(value);
        }
    }

    public static void setFullScreenWindow(WindowManager.LayoutParams lp, boolean value) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            cblp.mOplusLayoutParams.setFullScreenWindow(value);
        }
    }

    public static boolean skipSystemUiVisibility(WindowManager.LayoutParams lp) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            return cblp.mOplusLayoutParams.getSkipSystemUiVisibility();
        }
        return false;
    }

    public static void setSkipSystemUiVisibility(WindowManager.LayoutParams lp, boolean value) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            cblp.mOplusLayoutParams.setSkipSystemUiVisibility(value);
        }
    }

    public static boolean isUseLastStatusBarTint(WindowManager.LayoutParams lp) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            return cblp.mOplusLayoutParams.isUseLastStatusBarTint();
        }
        return false;
    }

    public static void setUseLastStatusBarTint(WindowManager.LayoutParams lp, boolean value) {
        OplusBaseLayoutParams cblp = typeCast(lp);
        if (cblp != null) {
            cblp.mOplusLayoutParams.setUseLastStatusBarTint(value);
        }
    }

    public static boolean updateSpecialSystemBar(WindowManager.LayoutParams lp) {
        return isUseLastStatusBarTint(lp) || updateDarkNavigationBar(lp);
    }

    @Deprecated
    public static boolean isInMultiWindowMode() {
        int dockSide = -1;
        try {
            dockSide = WindowManagerGlobal.getWindowManagerService().getDockedStackSide();
        } catch (RemoteException e) {
        }
        return -1 != dockSide;
    }

    public static boolean isColorLight(int color) {
        if (color == 0) {
            return false;
        }
        int alpha = ((-16777216) & color) >>> 24;
        int red = (16711680 & color) >>> 16;
        int green = (65280 & color) >>> 8;
        int blue = color & 255;
        int grayLevel = (int) ((red * 0.299f) + (green * 0.587f) + (blue * 0.114f));
        if (grayLevel <= 192 || alpha <= 156) {
            return false;
        }
        return true;
    }

    public static void syncSurfaceTransaction(SurfaceControl.Transaction mergeTransaction, SurfaceControl sc, View view) {
        SyncRtSurfaceTransactionApplier applier = new SyncRtSurfaceTransactionApplier(view);
        SyncRtSurfaceTransactionApplier.SurfaceParams params = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(sc).withMergeTransaction(mergeTransaction).build();
        applier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{params});
    }

    public static void hideNavigationBarBackButton(Window window, boolean isHide) {
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (layoutParams == null) {
            return;
        }
        if (isHide) {
            layoutParams.systemUiVisibility |= OplusZoomWindowManager.FLAG_TOUCH_OUTSIDE_CONTROL_VIEW;
        } else {
            layoutParams.systemUiVisibility &= -4194305;
        }
        window.setAttributes(layoutParams);
    }

    private static OplusBaseLayoutParams typeCast(WindowManager.LayoutParams lp) {
        return (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, lp);
    }
}
