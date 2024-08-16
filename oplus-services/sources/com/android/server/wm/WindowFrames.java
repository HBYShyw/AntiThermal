package com.android.server.wm;

import android.graphics.Rect;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowFrames {
    private static final StringBuilder sTmpSB = new StringBuilder();
    private boolean mContentChanged;
    private boolean mInsetsChanged;
    private boolean mParentFrameWasClippedByDisplayCutout;
    public final Rect mParentFrame = new Rect();
    public final Rect mDisplayFrame = new Rect();
    final Rect mFrame = new Rect();
    final Rect mLastFrame = new Rect();
    final Rect mRelFrame = new Rect();
    final Rect mLastRelFrame = new Rect();
    private boolean mFrameSizeChanged = false;
    final Rect mCompatFrame = new Rect();
    boolean mLastForceReportingResized = false;
    boolean mForceReportingResized = false;

    public void setFrames(Rect rect, Rect rect2) {
        this.mParentFrame.set(rect);
        this.mDisplayFrame.set(rect2);
    }

    public void setParentFrameWasClippedByDisplayCutout(boolean z) {
        this.mParentFrameWasClippedByDisplayCutout = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean parentFrameWasClippedByDisplayCutout() {
        return this.mParentFrameWasClippedByDisplayCutout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean didFrameSizeChange() {
        return (this.mLastFrame.width() == this.mFrame.width() && this.mLastFrame.height() == this.mFrame.height()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setReportResizeHints() {
        this.mLastForceReportingResized |= this.mForceReportingResized;
        boolean didFrameSizeChange = this.mFrameSizeChanged | didFrameSizeChange();
        this.mFrameSizeChanged = didFrameSizeChange;
        return this.mLastForceReportingResized || didFrameSizeChange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFrameSizeChangeReported() {
        return this.mFrameSizeChanged || didFrameSizeChange();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearReportResizeHints() {
        this.mLastForceReportingResized = false;
        this.mFrameSizeChanged = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onResizeHandled() {
        this.mForceReportingResized = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceReportingResized() {
        this.mForceReportingResized = true;
    }

    public void setContentChanged(boolean z) {
        this.mContentChanged = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasContentChanged() {
        return this.mContentChanged;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInsetsChanged(boolean z) {
        this.mInsetsChanged = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasInsetsChanged() {
        return this.mInsetsChanged;
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        this.mParentFrame.dumpDebug(protoOutputStream, 1146756268040L);
        this.mDisplayFrame.dumpDebug(protoOutputStream, 1146756268036L);
        this.mFrame.dumpDebug(protoOutputStream, 1146756268037L);
        this.mCompatFrame.dumpDebug(protoOutputStream, 1146756268048L);
        protoOutputStream.end(start);
    }

    public void dump(PrintWriter printWriter, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("Frames: parent=");
        Rect rect = this.mParentFrame;
        StringBuilder sb2 = sTmpSB;
        sb.append(rect.toShortString(sb2));
        sb.append(" display=");
        sb.append(this.mDisplayFrame.toShortString(sb2));
        sb.append(" frame=");
        sb.append(this.mFrame.toShortString(sb2));
        sb.append(" last=");
        sb.append(this.mLastFrame.toShortString(sb2));
        sb.append(" insetsChanged=");
        sb.append(this.mInsetsChanged);
        printWriter.println(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getInsetsChangedInfo() {
        return "forceReportingResized=" + this.mLastForceReportingResized + " insetsChanged=" + this.mInsetsChanged;
    }
}
