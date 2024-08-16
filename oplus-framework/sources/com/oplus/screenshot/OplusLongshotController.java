package com.oplus.screenshot;

import android.view.IViewExt;
import android.view.KeyEvent;

/* loaded from: classes.dex */
public abstract class OplusLongshotController implements IOplusLongshotController {
    final String mSource;
    final OplusLongshotViewBase mViewBase;

    public OplusLongshotController(OplusLongshotViewBase view, String source) {
        this.mViewBase = view;
        this.mSource = source;
    }

    @Override // com.oplus.screenshot.IOplusLongshotController
    public boolean findInfo(OplusLongshotViewInfo info) {
        findUnsupported(info);
        return true;
    }

    private void findUnsupported(OplusLongshotViewInfo info) {
        IViewExt iViewExt = this.mViewBase;
        if (iViewExt instanceof IViewExt) {
            IViewExt viewExt = iViewExt;
            KeyEvent.Callback view = viewExt.getView();
            if (view instanceof OplusLongshotUnsupported) {
                OplusLongshotUnsupported mView = (OplusLongshotUnsupported) view;
                if (mView.isLongshotUnsupported()) {
                    info.setUnsupported();
                }
            }
        }
    }
}
