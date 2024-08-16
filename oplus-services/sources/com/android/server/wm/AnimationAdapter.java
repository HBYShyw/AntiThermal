package com.android.server.wm;

import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import com.android.server.wm.SurfaceAnimator;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface AnimationAdapter {
    public static final long STATUS_BAR_TRANSITION_DURATION = 120;

    void dump(PrintWriter printWriter, String str);

    void dumpDebug(ProtoOutputStream protoOutputStream);

    default int getBackgroundColor() {
        return 0;
    }

    long getDurationHint();

    default boolean getShowBackground() {
        return false;
    }

    boolean getShowWallpaper();

    long getStatusBarTransitionsStartTime();

    void onAnimationCancelled(SurfaceControl surfaceControl);

    default boolean shouldDeferAnimationFinish(Runnable runnable) {
        return false;
    }

    void startAnimation(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i, SurfaceAnimator.OnAnimationFinishedCallback onAnimationFinishedCallback);

    default void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        dumpDebug(protoOutputStream);
        protoOutputStream.end(start);
    }
}
