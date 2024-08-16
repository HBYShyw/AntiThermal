package com.android.server.wm;

import android.os.Looper;
import android.util.Slog;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.MotionEvent;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DragInputEventReceiver extends InputEventReceiver {
    private final DragDropController mDragDropController;
    private boolean mIsStartEvent;
    private boolean mMuteInput;
    private boolean mStylusButtonDownAtStart;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DragInputEventReceiver(InputChannel inputChannel, Looper looper, DragDropController dragDropController) {
        super(inputChannel, looper);
        this.mIsStartEvent = true;
        this.mMuteInput = false;
        this.mDragDropController = dragDropController;
    }

    public void onInputEvent(InputEvent inputEvent) {
        try {
            if ((inputEvent instanceof MotionEvent) && (inputEvent.getSource() & 2) != 0 && !this.mMuteInput) {
                MotionEvent motionEvent = (MotionEvent) inputEvent;
                float rawX = motionEvent.getRawX();
                float rawY = motionEvent.getRawY();
                boolean z = (motionEvent.getButtonState() & 32) != 0;
                if (this.mIsStartEvent) {
                    this.mStylusButtonDownAtStart = z;
                    this.mIsStartEvent = false;
                }
                int action = motionEvent.getAction();
                if (action == 0) {
                    if (WindowManagerDebugConfig.DEBUG_DRAG) {
                        Slog.w("WindowManager", "Unexpected ACTION_DOWN in drag layer");
                    }
                    return;
                }
                if (action == 1) {
                    if (WindowManagerDebugConfig.DEBUG_DRAG) {
                        Slog.d("WindowManager", "Got UP on move channel; dropping at " + rawX + "," + rawY);
                    }
                    this.mMuteInput = true;
                } else if (action != 2) {
                    if (action != 3) {
                        return;
                    }
                    if (WindowManagerDebugConfig.DEBUG_DRAG) {
                        Slog.d("WindowManager", "Drag cancelled!");
                    }
                    this.mMuteInput = true;
                    this.mDragDropController.sendHandlerMessage(2, null);
                    this.mDragDropController.mDragDropControllerExt.postCancelDragAndDrop();
                } else if (this.mStylusButtonDownAtStart && !z) {
                    if (WindowManagerDebugConfig.DEBUG_DRAG) {
                        Slog.d("WindowManager", "Button no longer pressed; dropping at " + rawX + "," + rawY);
                    }
                    this.mMuteInput = true;
                }
                this.mDragDropController.handleMotionEvent(!this.mMuteInput, rawX, rawY);
                finishInputEvent(inputEvent, true);
            }
        } catch (Exception e) {
            Slog.e("WindowManager", "Exception caught by drag handleMotion", e);
        } finally {
            finishInputEvent(inputEvent, false);
        }
    }
}
