package com.android.server.wm;

import android.R;
import android.content.Context;
import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import com.android.server.wm.LocalAnimationAdapter;
import com.android.server.wm.SurfaceAnimator;
import java.io.PrintWriter;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class FadeAnimationController {
    protected final Context mContext;
    protected final DisplayContent mDisplayContent;
    private IFadeAnimationControllerExt mExt = (IFadeAnimationControllerExt) ExtLoader.type(IFadeAnimationControllerExt.class).base(this).create();

    public FadeAnimationController(DisplayContent displayContent) {
        this.mDisplayContent = displayContent;
        this.mContext = displayContent.mWmService.mContext;
    }

    public Animation getFadeInAnimation() {
        return AnimationUtils.loadAnimation(this.mContext, R.anim.fade_in);
    }

    public Animation getFadeOutAnimation() {
        return AnimationUtils.loadAnimation(this.mContext, R.anim.fade_out);
    }

    public void fadeWindowToken(boolean z, WindowToken windowToken, int i) {
        fadeWindowToken(z, windowToken, i, null);
    }

    public void fadeWindowToken(boolean z, WindowToken windowToken, int i, SurfaceAnimator.OnAnimationFinishedCallback onAnimationFinishedCallback) {
        if (windowToken == null || windowToken.getParent() == null) {
            return;
        }
        Animation fadeInAnimation = z ? getFadeInAnimation() : getFadeOutAnimation();
        this.mExt.hookFadeWindowToken(windowToken, z, fadeInAnimation);
        FadeAnimationAdapter createAdapter = fadeInAnimation != null ? createAdapter(createAnimationSpec(fadeInAnimation), z, windowToken) : null;
        if (createAdapter == null) {
            return;
        }
        windowToken.startAnimation(windowToken.getPendingTransaction(), createAdapter, z, i, onAnimationFinishedCallback);
    }

    protected FadeAnimationAdapter createAdapter(LocalAnimationAdapter.AnimationSpec animationSpec, boolean z, WindowToken windowToken) {
        return new FadeAnimationAdapter(animationSpec, windowToken.getSurfaceAnimationRunner(), z, windowToken);
    }

    protected LocalAnimationAdapter.AnimationSpec createAnimationSpec(final Animation animation) {
        return new LocalAnimationAdapter.AnimationSpec() { // from class: com.android.server.wm.FadeAnimationController.1
            final Transformation mTransformation = new Transformation();

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public boolean getShowWallpaper() {
                return true;
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public long getDuration() {
                return animation.getDuration();
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public void apply(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, long j) {
                this.mTransformation.clear();
                animation.getTransformation(j, this.mTransformation);
                transaction.setAlpha(surfaceControl, this.mTransformation.getAlpha());
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public void dump(PrintWriter printWriter, String str) {
                printWriter.print(str);
                printWriter.println(animation);
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public void dumpDebugInner(ProtoOutputStream protoOutputStream) {
                long start = protoOutputStream.start(1146756268033L);
                protoOutputStream.write(1138166333441L, animation.toString());
                protoOutputStream.end(start);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class FadeAnimationAdapter extends LocalAnimationAdapter {
        protected final boolean mShow;
        protected final WindowToken mToken;

        /* JADX INFO: Access modifiers changed from: package-private */
        public FadeAnimationAdapter(LocalAnimationAdapter.AnimationSpec animationSpec, SurfaceAnimationRunner surfaceAnimationRunner, boolean z, WindowToken windowToken) {
            super(animationSpec, surfaceAnimationRunner);
            this.mShow = z;
            this.mToken = windowToken;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean shouldDeferAnimationFinish(Runnable runnable) {
            return !this.mShow;
        }
    }
}
