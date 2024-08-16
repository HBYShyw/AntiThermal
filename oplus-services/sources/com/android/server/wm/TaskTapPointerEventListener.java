package com.android.server.wm;

import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.input.InputManagerGlobal;
import android.util.Slog;
import android.view.MotionEvent;
import android.view.WindowManagerPolicyConstants;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskTapPointerEventListener implements WindowManagerPolicyConstants.PointerEventListener {
    private final DisplayContent mDisplayContent;
    private final WindowManagerService mService;
    private final Region mTouchExcludeRegion = new Region();
    private final Rect mTmpRect = new Rect();
    private int mPointerIconType = 1;
    public ITaskTapPointerEventListenerSocExt mSocExt = (ITaskTapPointerEventListenerSocExt) ExtLoader.type(ITaskTapPointerEventListenerSocExt.class).base(this).create();
    public ITaskTapPointerEventListenerExt mTaskTapListenerExt = (ITaskTapPointerEventListenerExt) ExtLoader.type(ITaskTapPointerEventListenerExt.class).base(this).create();

    public TaskTapPointerEventListener(WindowManagerService windowManagerService, DisplayContent displayContent) {
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
    }

    private void restorePointerIcon(int i, int i2) {
        if (this.mPointerIconType != 1) {
            this.mPointerIconType = 1;
            this.mService.mH.removeMessages(55);
            this.mService.mH.obtainMessage(55, i, i2, this.mDisplayContent).sendToTarget();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x0087, code lost:
    
        if (r1 > r2.bottom) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0096, code lost:
    
        if (r1 > r2.bottom) goto L34;
     */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00a9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onPointerEvent(MotionEvent motionEvent) {
        int x;
        float y;
        Task task;
        int i;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            if (motionEvent.getSource() == 8194) {
                x = (int) motionEvent.getXCursorPosition();
                y = motionEvent.getYCursorPosition();
            } else {
                x = (int) motionEvent.getX();
                y = motionEvent.getY();
            }
            int i2 = (int) y;
            synchronized (this) {
                if (!this.mTouchExcludeRegion.contains(x, i2)) {
                    this.mService.mTaskPositioningController.handleTapOutsideTask(this.mDisplayContent, x, i2);
                }
            }
        } else if (actionMasked == 7 || actionMasked == 9) {
            int x2 = (int) motionEvent.getX();
            int y2 = (int) motionEvent.getY();
            synchronized (this) {
                if (this.mTouchExcludeRegion.contains(x2, y2)) {
                    restorePointerIcon(x2, y2);
                } else {
                    try {
                        task = this.mDisplayContent.findTaskForResizePoint(x2, y2);
                    } catch (Exception e) {
                        Slog.d("TaskTapPointerEventListener", " findTaskForResizePoint ex=" + e);
                        task = null;
                    }
                    if (task != null) {
                        task.getDimBounds(this.mTmpRect);
                        if (!this.mTmpRect.isEmpty() && !this.mTmpRect.contains(x2, y2)) {
                            Rect rect = this.mTmpRect;
                            i = 1014;
                            if (x2 < rect.left) {
                                if (y2 >= rect.top) {
                                }
                                i = 1017;
                                if (this.mPointerIconType != i) {
                                    this.mPointerIconType = i;
                                    if (i == 1) {
                                        this.mService.mH.removeMessages(55);
                                        this.mService.mH.obtainMessage(55, x2, y2, this.mDisplayContent).sendToTarget();
                                    } else {
                                        InputManagerGlobal.getInstance().setPointerIconType(this.mPointerIconType);
                                    }
                                }
                            } else {
                                if (x2 > rect.right) {
                                    if (y2 >= rect.top) {
                                    }
                                    i = 1016;
                                } else if (y2 < rect.top || y2 > rect.bottom) {
                                    i = 1015;
                                }
                                if (this.mPointerIconType != i) {
                                }
                            }
                        }
                    }
                    i = 1;
                    if (this.mPointerIconType != i) {
                    }
                }
            }
        } else if (actionMasked == 10) {
            restorePointerIcon((int) motionEvent.getX(), (int) motionEvent.getY());
        }
        this.mSocExt.onPointerEventCheck();
        this.mTaskTapListenerExt.onPointerEvent(this.mDisplayContent, motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTouchExcludeRegion(Region region) {
        synchronized (this) {
            this.mTouchExcludeRegion.set(region);
        }
    }
}
