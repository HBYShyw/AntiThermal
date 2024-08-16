package android.view;

import android.common.OplusFeatureCache;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.widget.IOplusTextViewRTLUtilForUG;
import com.oplus.favorite.IOplusFavoriteManager;
import com.oplus.screenshot.IOplusLongshotController;
import com.oplus.screenshot.OplusLongshotViewController;
import com.oplus.screenshot.OplusLongshotViewInfo;
import com.oplus.util.OplusContextUtil;
import com.oplus.util.OplusTypeCastingHelper;
import com.oplus.view.IOplusScrollBarEffect;
import com.oplus.view.OplusScrollBarEffect;

/* loaded from: classes.dex */
public class OplusViewHooksImp implements IOplusViewHooks {
    private OplusContextUtil mContextUtil = null;
    private final IOplusLongshotController mLongshotController;
    private final IOplusScrollBarEffect mScrollBarEffect;
    private final View mView;

    public OplusViewHooksImp(View view, Resources res) {
        this.mView = view;
        ViewExtImpl viewExt = (ViewExtImpl) OplusTypeCastingHelper.typeCasting(ViewExtImpl.class, view.getViewWrapper().getViewExt());
        this.mLongshotController = new OplusLongshotViewController(viewExt);
        this.mScrollBarEffect = createScrollBarEffect(res);
        ((IOplusTextViewRTLUtilForUG) OplusFeatureCache.getOrCreate(IOplusTextViewRTLUtilForUG.DEFAULT, new Object[0])).initRtlParameter(res);
    }

    @Override // android.view.IOplusViewHooks, com.oplus.view.IOplusScrollBarEffect.ViewCallback
    public boolean awakenScrollBars() {
        return this.mView.awakenScrollBars();
    }

    @Override // android.view.IOplusViewHooks, com.oplus.view.IOplusScrollBarEffect.ViewCallback
    public boolean isLayoutRtl() {
        return this.mView.isLayoutRtl();
    }

    @Override // android.view.IOplusViewHooks
    public IOplusLongshotController getLongshotController() {
        return this.mLongshotController;
    }

    @Override // android.view.IOplusViewHooks
    public IOplusScrollBarEffect getScrollBarEffect() {
        return this.mScrollBarEffect;
    }

    @Override // android.view.IOplusViewHooks
    public boolean isOplusStyle() {
        if (this.mContextUtil == null) {
            this.mContextUtil = new OplusContextUtil(this.mView.getContext());
        }
        return this.mContextUtil.isOplusStyle();
    }

    @Override // android.view.IOplusViewHooks
    public boolean isOplusOSStyle() {
        if (this.mContextUtil == null) {
            this.mContextUtil = new OplusContextUtil(this.mView.getContext());
        }
        return this.mContextUtil.isOplusOSStyle();
    }

    @Override // android.view.IOplusViewHooks
    public int getOverScrollMode(int overScrollMode) {
        return getLongshotController().getOverScrollMode(overScrollMode);
    }

    @Override // android.view.IOplusViewHooks
    public boolean findViewsLongshotInfo(OplusLongshotViewInfo info) {
        return getLongshotController().findInfo(info);
    }

    @Override // android.view.IOplusViewHooks
    public boolean isLongshotConnected() {
        return getLongshotController().isLongshotConnected();
    }

    @Override // android.view.IOplusViewHooks
    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent, int oldScrollY, boolean result) {
        return getLongshotController().overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent, oldScrollY, result);
    }

    @Override // android.view.IOplusViewHooks
    public void performClick() {
        IOplusFavoriteManager favoriteManager = (IOplusFavoriteManager) OplusFeatureCache.getOrCreate(IOplusFavoriteManager.DEFAULT, new Object[0]);
        favoriteManager.processClick(this.mView, null);
        favoriteManager.logViewInfo(this.mView);
    }

    @Override // android.view.IOplusViewHooks
    public Bitmap getOplusCustomDrawingCache(Rect clip, int viewTop, int mPrivateFlags) {
        Canvas canvas;
        int drawingCacheBackgroundColor = this.mView.getDrawingCacheBackgroundColor();
        try {
            int width = this.mView.getWidth();
            int height = this.mView.getHeight();
            if (width > 0 && height > 0) {
                Bitmap.Config quality = Bitmap.Config.ARGB_8888;
                Bitmap colorCustomBitmap = Bitmap.createBitmap(this.mView.getResources().getDisplayMetrics(), width, height, quality);
                colorCustomBitmap.setDensity(this.mView.getResources().getDisplayMetrics().densityDpi);
                if (colorCustomBitmap == null) {
                    return null;
                }
                boolean clear = drawingCacheBackgroundColor != 0;
                View.AttachInfo attachInfo = this.mView.mAttachInfo;
                if (attachInfo != null) {
                    canvas = attachInfo.mCanvas;
                    if (canvas == null) {
                        canvas = new Canvas();
                    }
                    canvas.setBitmap(colorCustomBitmap);
                    attachInfo.mCanvas = null;
                } else {
                    canvas = new Canvas(colorCustomBitmap);
                }
                if (clip != null && !clip.isEmpty()) {
                    canvas.mBaseCanvasExt.setClipChildRect(clip);
                    if (clip.top > viewTop) {
                        canvas.clipRect(clip.left, clip.top - viewTop, clip.right, clip.bottom - viewTop);
                    }
                }
                if (clear) {
                    colorCustomBitmap.eraseColor(drawingCacheBackgroundColor);
                }
                int restoreCount = canvas.save();
                if ((mPrivateFlags & 128) == 128) {
                    int i = mPrivateFlags & (-2097153);
                    this.mView.dispatchDraw(canvas);
                    if (this.mView.getOverlay() != null && !this.mView.getOverlay().isEmpty()) {
                        this.mView.getOverlay().getOverlayView().draw(canvas);
                    }
                } else {
                    this.mView.draw(canvas);
                }
                canvas.restoreToCount(restoreCount);
                canvas.mBaseCanvasExt.setClipChildRect((Rect) null);
                canvas.setBitmap(null);
                return colorCustomBitmap;
            }
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private IOplusScrollBarEffect createScrollBarEffect(Resources res) {
        if (res == null) {
            return OplusScrollBarEffect.NO_EFFECT;
        }
        return new OplusScrollBarEffect(res, this);
    }

    @Override // android.view.IOplusViewHooks
    public boolean needHook() {
        return true;
    }
}
