package android.view;

import android.common.OplusFeatureCache;
import android.graphics.Canvas;
import android.graphics.IBaseCanvasExt;
import android.graphics.Rect;
import android.view.debug.IOplusViewDebugManager;
import com.oplus.darkmode.IOplusDarkModeManager;
import com.oplus.debug.InputLog;

/* loaded from: classes.dex */
public class ViewGroupExtImpl implements IViewGroupExt {
    private ViewGroup mViewGroup;

    public ViewGroupExtImpl(Object viewGroup) {
        this.mViewGroup = null;
        this.mViewGroup = (ViewGroup) viewGroup;
    }

    public boolean isLevelDebug() {
        return InputLog.isLevelDebug();
    }

    public boolean isLevelVerbose() {
        return InputLog.isLevelVerbose();
    }

    public void d(String tag, String msg) {
        if (InputLog.isLevelDebug()) {
            InputLog.d(tag, msg);
        }
    }

    public void v(String tag, String msg) {
        if (InputLog.isLevelVerbose()) {
            InputLog.v(tag, msg);
        }
    }

    public void hookdispatchTouchEvent(MotionEvent ev, IViewExt viewExt) {
        viewExt.onTouchEvent(ev);
    }

    public void markDispatchDraw(ViewGroup viewGroup, Canvas canvas) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).markDispatchDraw(viewGroup, canvas);
    }

    public void markDrawChild(ViewGroup viewGroup, View view, Canvas canvas) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).markDrawChild(viewGroup, view, canvas);
    }

    public boolean hookdrawChild(Canvas canvas, View transientChild, long drawingTime) {
        IBaseCanvasExt baseCanvasExt = canvas.mBaseCanvasExt;
        Rect clipChldRect = null;
        if (baseCanvasExt != null) {
            clipChldRect = baseCanvasExt.getClipChildRect();
        }
        if (clipChldRect == null) {
            return true;
        }
        Rect childRect = new Rect();
        transientChild.getBoundsOnScreen(childRect, true);
        if (Rect.intersects(clipChldRect, childRect)) {
            return true;
        }
        return false;
    }

    public boolean markOnDispatchTouchEvent(MotionEvent event, View view) {
        return getViewDebugManager().markOnDispatchTouchEvent(event, view);
    }

    public void markOnAddView(View child) {
        getViewDebugManager().markOnAddView(child);
    }

    public void markOnRemoveView(View child) {
        getViewDebugManager().markOnRemoveView(child);
    }

    public IOplusViewDebugManager getViewDebugManager() {
        return (IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0]);
    }
}
