package com.android.server.wm;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.graphics.Point;
import android.view.IWindow;
import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDragDropControllerExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface IOplusDragDropControllerExtCallback {
        default boolean getConsumedResult() {
            return false;
        }

        default void onHandleZoomDrag(float f, float f2) {
        }

        default void postCancelDragAndDrop() {
        }

        default void postEndDrag() {
        }

        default void postPerSuccessformDrag(IWindow iWindow, SurfaceControl surfaceControl, int i, float[] fArr, ClipData clipData) {
        }
    }

    default Point adjustYForZoomWinIfNeed(WindowState windowState, float f, float f2) {
        return null;
    }

    default void closeDnDSplitScreenStateIfNeed() {
    }

    default ValueAnimator createCustormAnimatorIfNeed(int i, DragState dragState) {
        return null;
    }

    default SurfaceControl createDnDAnimationLeash(DragState dragState, SurfaceControl.Transaction transaction, float f, float f2, DisplayContent displayContent) {
        return null;
    }

    default ValueAnimator createReturnAnimationIfNeed(DragState dragState) {
        return null;
    }

    default boolean getConsumedResult() {
        return false;
    }

    default boolean getPlayShrinkAnimState() {
        return false;
    }

    default void grantPermission(WindowState windowState, DragAndDropPermissionsHandler dragAndDropPermissionsHandler) {
    }

    default void handleZoomDrag(float f, float f2) {
    }

    default boolean isInterceptedDrop(WindowState windowState) {
        return false;
    }

    default boolean isSupportDragPkg(String str) {
        return false;
    }

    default boolean notifyDnDSplitScreenCloseIfNeed() {
        return false;
    }

    default ValueAnimator notifyDnDSplitScreenDrop(float f, float f2) {
        return null;
    }

    default void notifyDnDSplitScreenLocation(float f, float f2) {
    }

    default void notifyDnDSplitScreenStartIfNeed(DragState dragState) {
    }

    default void postCancelDragAndDrop() {
    }

    default void postEndDrag() {
    }

    default void postPerSuccessformDrag(IWindow iWindow, SurfaceControl surfaceControl, int i, float[] fArr, ClipData clipData) {
    }

    default void registerCallback(IOplusDragDropControllerExtCallback iOplusDragDropControllerExtCallback) {
    }

    default boolean vibrateIfNeed(WindowState windowState, WindowState windowState2) {
        return false;
    }
}
