package com.android.server.wm;

import android.graphics.Rect;
import android.util.proto.ProtoOutputStream;
import android.view.DisplayCutout;
import android.view.DisplayInfo;
import android.view.DisplayShape;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.PrivacyIndicatorBounds;
import android.view.RoundedCorners;
import android.view.WindowInsets;
import java.io.PrintWriter;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayFrames {
    public final Rect mDisplayCutoutSafe;
    private final IDisplayFramesExt mDisplayFramesExt;
    public int mHeight;
    public final InsetsState mInsetsState;
    public int mRotation;
    public final Rect mUnrestricted;
    public int mWidth;
    private final IDisplayFramesWrapper mWrapper;
    private static final int ID_DISPLAY_CUTOUT_LEFT = InsetsSource.createId((Object) null, 0, WindowInsets.Type.displayCutout());
    private static final int ID_DISPLAY_CUTOUT_TOP = InsetsSource.createId((Object) null, 1, WindowInsets.Type.displayCutout());
    private static final int ID_DISPLAY_CUTOUT_RIGHT = InsetsSource.createId((Object) null, 2, WindowInsets.Type.displayCutout());
    private static final int ID_DISPLAY_CUTOUT_BOTTOM = InsetsSource.createId((Object) null, 3, WindowInsets.Type.displayCutout());
    private static final IDisplayFramesStaticWrapper STATIC_WRAPPER = new DisplayFramesStaticWrapper();

    public DisplayFrames(InsetsState insetsState, DisplayInfo displayInfo, DisplayCutout displayCutout, RoundedCorners roundedCorners, PrivacyIndicatorBounds privacyIndicatorBounds, DisplayShape displayShape) {
        this.mUnrestricted = new Rect();
        this.mDisplayCutoutSafe = new Rect();
        this.mWrapper = new DisplayFramesWrapper();
        IDisplayFramesExt iDisplayFramesExt = (IDisplayFramesExt) ExtLoader.type(IDisplayFramesExt.class).base(this).create();
        this.mDisplayFramesExt = iDisplayFramesExt;
        this.mInsetsState = insetsState;
        iDisplayFramesExt.setDisplayId(displayInfo.displayId);
        update(displayInfo.rotation, displayInfo.logicalWidth, displayInfo.logicalHeight, displayCutout, roundedCorners, privacyIndicatorBounds, displayShape);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayFrames() {
        this.mUnrestricted = new Rect();
        this.mDisplayCutoutSafe = new Rect();
        this.mWrapper = new DisplayFramesWrapper();
        this.mDisplayFramesExt = (IDisplayFramesExt) ExtLoader.type(IDisplayFramesExt.class).base(this).create();
        this.mInsetsState = new InsetsState();
    }

    public boolean update(int i, int i2, int i3, DisplayCutout displayCutout, RoundedCorners roundedCorners, PrivacyIndicatorBounds privacyIndicatorBounds, DisplayShape displayShape) {
        InsetsState insetsState = this.mInsetsState;
        Rect rect = this.mDisplayCutoutSafe;
        if (this.mRotation == i && this.mWidth == i2 && this.mHeight == i3 && insetsState.getDisplayCutout().equals(displayCutout) && insetsState.getRoundedCorners().equals(roundedCorners) && insetsState.getPrivacyIndicatorBounds().equals(privacyIndicatorBounds)) {
            return false;
        }
        this.mRotation = i;
        this.mWidth = i2;
        this.mHeight = i3;
        Rect rect2 = this.mUnrestricted;
        rect2.set(0, 0, i2, i3);
        insetsState.setDisplayFrame(rect2);
        insetsState.setDisplayCutout(displayCutout);
        insetsState.setRoundedCorners(roundedCorners);
        insetsState.setPrivacyIndicatorBounds(privacyIndicatorBounds);
        insetsState.setDisplayShape(displayShape);
        insetsState.getDisplayCutoutSafe(rect);
        if (rect.left > rect2.left) {
            insetsState.getOrCreateSource(ID_DISPLAY_CUTOUT_LEFT, WindowInsets.Type.displayCutout()).setFrame(rect2.left, rect2.top, rect.left, rect2.bottom);
        } else {
            insetsState.removeSource(ID_DISPLAY_CUTOUT_LEFT);
        }
        if (rect.top > rect2.top) {
            insetsState.getOrCreateSource(ID_DISPLAY_CUTOUT_TOP, WindowInsets.Type.displayCutout()).setFrame(rect2.left, rect2.top, rect2.right, rect.top);
        } else {
            insetsState.removeSource(ID_DISPLAY_CUTOUT_TOP);
        }
        if (rect.right < rect2.right) {
            insetsState.getOrCreateSource(ID_DISPLAY_CUTOUT_RIGHT, WindowInsets.Type.displayCutout()).setFrame(rect.right, rect2.top, rect2.right, rect2.bottom);
        } else {
            insetsState.removeSource(ID_DISPLAY_CUTOUT_RIGHT);
        }
        if (rect.bottom < rect2.bottom) {
            insetsState.getOrCreateSource(ID_DISPLAY_CUTOUT_BOTTOM, WindowInsets.Type.displayCutout()).setFrame(rect2.left, rect.bottom, rect2.right, rect2.bottom);
            this.mDisplayFramesExt.removeSecondaryDisplaySource(insetsState, i2, i3);
            return true;
        }
        insetsState.removeSource(ID_DISPLAY_CUTOUT_BOTTOM);
        return true;
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        protoOutputStream.end(protoOutputStream.start(j));
    }

    public void dump(String str, PrintWriter printWriter) {
        printWriter.println(str + "DisplayFrames w=" + this.mWidth + " h=" + this.mHeight + " r=" + this.mRotation);
    }

    public static IDisplayFramesStaticWrapper getStaticWrapper() {
        return STATIC_WRAPPER;
    }

    public IDisplayFramesWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class DisplayFramesWrapper extends DisplayFramesStaticWrapper implements IDisplayFramesWrapper {
        private DisplayFramesWrapper() {
            super();
        }

        @Override // com.android.server.wm.IDisplayFramesWrapper
        public IDisplayFramesExt getExtImpl() {
            return DisplayFrames.this.mDisplayFramesExt;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private static class DisplayFramesStaticWrapper implements IDisplayFramesStaticWrapper {
        private DisplayFramesStaticWrapper() {
        }

        @Override // com.android.server.wm.IDisplayFramesStaticWrapper
        public final int getTopDisplayCutoutId() {
            return DisplayFrames.ID_DISPLAY_CUTOUT_TOP;
        }

        @Override // com.android.server.wm.IDisplayFramesStaticWrapper
        public final int getBottomDisplayCutoutId() {
            return DisplayFrames.ID_DISPLAY_CUTOUT_BOTTOM;
        }

        @Override // com.android.server.wm.IDisplayFramesStaticWrapper
        public final int getLeftDisplayCutoutId() {
            return DisplayFrames.ID_DISPLAY_CUTOUT_LEFT;
        }

        @Override // com.android.server.wm.IDisplayFramesStaticWrapper
        public final int getRightDisplayCutoutId() {
            return DisplayFrames.ID_DISPLAY_CUTOUT_RIGHT;
        }
    }
}
