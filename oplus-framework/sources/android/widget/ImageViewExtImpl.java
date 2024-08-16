package android.widget;

import android.common.OplusFeatureCache;
import android.view.autolayout.IOplusAutoLayoutManager;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class ImageViewExtImpl implements IImageViewExt {
    private ImageView mBase;

    public ImageViewExtImpl(Object base) {
        this.mBase = (ImageView) base;
    }

    public ImageView.ScaleType modifyScaleType(ImageView.ScaleType scaleType) {
        return getAutoLayoutManager().modifyScaleType(scaleType);
    }

    private IOplusAutoLayoutManager getAutoLayoutManager() {
        return (IOplusAutoLayoutManager) OplusFeatureCache.getOrCreate(IOplusAutoLayoutManager.mDefault, new Object[0]);
    }
}
