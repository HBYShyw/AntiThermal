package com.oplus.screenshot;

/* loaded from: classes.dex */
public class OplusLongshotCustomController extends OplusLongshotController {
    private final OplusScreenshotManager mScreenshotManager;

    public OplusLongshotCustomController(OplusLongshotViewBase view, String source) {
        super(view, source);
        this.mScreenshotManager = OplusLongshotUtils.getScreenshotManager(view.getContext());
    }

    public void onLongScroll() {
        if (!canLongshot()) {
            OplusLongshotEvent event = new OplusLongshotEvent(this.mViewBase.getClass().getName(), 0, true);
            this.mScreenshotManager.notifyOverScroll(event);
        }
    }

    private boolean canLongshot() {
        OplusLongshotViewBase view = this.mViewBase;
        int offset = view.computeLongScrollOffset();
        int range = view.computeLongScrollRange() - view.computeLongScrollExtent();
        return range != 0 && offset > 0;
    }
}
