package android.graphics;

import android.common.OplusFeatureCache;
import android.graphics.IBaseCanvasExt;
import android.view.autolayout.IOplusAutoLayoutManager;
import android.view.rgbnormalize.IOplusRGBNormalizeManager;
import com.oplus.darkmode.IOplusDarkModeManager;

/* loaded from: classes.dex */
public class BaseCanvasExtImpl implements IBaseCanvasExt {
    private IBaseCanvasExt.OnCanvasDrawCallback mCanvasCallback;
    private Rect mClipChildRect;
    private int mTransformType = 2;
    private int mViewHeight;
    private int mViewType;
    private int mViewWidth;

    public BaseCanvasExtImpl(Object base) {
    }

    public void setViewArea(int width, int height) {
        this.mViewWidth = width;
        this.mViewHeight = height;
    }

    public int getViewAreaWidth() {
        return this.mViewWidth;
    }

    public int getViewAreaHeight() {
        return this.mViewHeight;
    }

    public int getOplusViewType() {
        return this.mViewType;
    }

    public void setOplusViewTypeLocked(int viewType) {
        this.mViewType = viewType;
    }

    public int getTransformType() {
        return this.mTransformType;
    }

    public void setTransformType(int mTransformType) {
        this.mTransformType = mTransformType;
    }

    public RectF getRectF(float width, float height) {
        mRectF.set(0.0f, 0.0f, width, height);
        return mRectF;
    }

    public RectF getRectF(float left, float top, float right, float bottom) {
        mRectF.set(left, top, right, bottom);
        return mRectF;
    }

    public RectF getRectF(Rect rect) {
        return new RectF(rect);
    }

    public void setIsCanvasBaseBitmap(Bitmap bitmap, boolean value) {
        IBitmapExt bitmapExt;
        if (bitmap != null && (bitmapExt = bitmap.mBitmapExt) != null) {
            bitmapExt.setIsCanvasBaseBitmap(value);
        }
    }

    public boolean isHardwareAccelerated() {
        return false;
    }

    public boolean isDarkMode() {
        return ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).isInDarkMode(isHardwareAccelerated());
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public int[] changeColors(int[] colors) {
        return colors;
    }

    public IBaseCanvasExt.Entity changeBitmap(Paint paint, Bitmap bitmap, RectF rectF) {
        boolean isDarkMode = isDarkMode();
        IBaseCanvasExt.Entity entity = null;
        if (isDarkMode) {
            entity = new IBaseCanvasExt.Entity();
            IBaseCanvasExt.RealPaintState realPaintState = ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).getRealPaintState(paint);
            entity.newPaint = ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).getPaintWhenDrawBitmap(paint, bitmap, ensureRect(rectF), this);
            entity.realPaintState = realPaintState;
            entity.isDarkMode = isDarkMode;
        }
        getOplusRgbNormalizeManager().hookPaintBitmap(paint, bitmap);
        if (getCanvasDrawCallback() != null) {
            getCanvasDrawCallback().onBitmapDraw(bitmap);
        }
        return entity;
    }

    public int changeColor(int color) {
        if (isDarkMode()) {
            return ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).changeWhenDrawColor(color, isDarkMode(), this);
        }
        return color;
    }

    public IBaseCanvasExt.Entity changePatch(NinePatch patch, Paint paint, RectF rectF) {
        boolean isDarkMode = isDarkMode();
        if (!isDarkMode) {
            return null;
        }
        IBaseCanvasExt.Entity entity = new IBaseCanvasExt.Entity();
        IBaseCanvasExt.RealPaintState realPaintState = ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).getRealPaintState(paint);
        entity.newPaint = ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).getPaintWhenDrawPatch(patch, paint, ensureRect(rectF), this);
        entity.realPaintState = realPaintState;
        entity.isDarkMode = isDarkMode;
        return entity;
    }

    public IBaseCanvasExt.Entity changeArea(Paint paint, RectF rectF) {
        boolean isDarkMode = isDarkMode();
        IBaseCanvasExt.Entity entity = null;
        if (isDarkMode) {
            entity = new IBaseCanvasExt.Entity();
            IBaseCanvasExt.RealPaintState realPaintState = ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).getRealPaintState(paint);
            ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).changePaintWhenDrawArea(paint, ensureRect(rectF), this);
            entity.realPaintState = realPaintState;
            entity.isDarkMode = isDarkMode;
        }
        getOplusRgbNormalizeManager().hookPaintColor(paint);
        return entity;
    }

    public IBaseCanvasExt.Entity changeArea(Paint paint, RectF rectF, Path path) {
        boolean isDarkMode = isDarkMode();
        IBaseCanvasExt.Entity entity = null;
        if (isDarkMode) {
            entity = new IBaseCanvasExt.Entity();
            IBaseCanvasExt.RealPaintState realPaintState = ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).getRealPaintState(paint);
            ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).changePaintWhenDrawArea(paint, ensureRect(rectF), path, this);
            entity.realPaintState = realPaintState;
            entity.isDarkMode = isDarkMode;
        }
        getOplusRgbNormalizeManager().hookPaintColor(paint);
        return entity;
    }

    public IBaseCanvasExt.Entity changeText(Paint paint, String s) {
        boolean isDarkMode = isDarkMode();
        IBaseCanvasExt.Entity entity = null;
        if (isDarkMode) {
            entity = new IBaseCanvasExt.Entity();
            IBaseCanvasExt.RealPaintState realPaintState = ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).getRealPaintState(paint);
            ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).changePaintWhenDrawText(paint, this);
            entity.realPaintState = realPaintState;
            entity.isDarkMode = isDarkMode;
        }
        getOplusRgbNormalizeManager().hookPaintColor(paint);
        if (getCanvasDrawCallback() != null) {
            getCanvasDrawCallback().onTextDraw(s);
        }
        return entity;
    }

    public void resetEntity(IBaseCanvasExt.Entity entity, Paint paint) {
        if (entity != null && entity.isDarkMode && entity.realPaintState != null && paint != null) {
            ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).resetRealPaintIfNeed(paint, entity.realPaintState);
        }
    }

    private RectF ensureRect(RectF rectF) {
        if (rectF == null) {
            mRectF.set(0.0f, 0.0f, getWidth(), getHeight());
            return mRectF;
        }
        return rectF;
    }

    public void setClipChildRect(Rect rect) {
        if (rect == null) {
            this.mClipChildRect = null;
            return;
        }
        Rect rect2 = this.mClipChildRect;
        if (rect2 == null) {
            this.mClipChildRect = rect;
        } else {
            rect2.set(rect);
        }
    }

    public Rect getClipChildRect() {
        return this.mClipChildRect;
    }

    public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
        getAutoLayoutManager().drawBitmap(bitmap, left, top, paint);
    }

    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
        getAutoLayoutManager().drawBitmap(bitmap, matrix, paint);
    }

    public Rect drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
        return getAutoLayoutManager().drawBitmap(bitmap, src, dst, paint);
    }

    public void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {
        getAutoLayoutManager().drawBitmap(bitmap, src, dst, paint);
    }

    public void start() {
        getAutoLayoutManager().start();
    }

    public void end() {
        getAutoLayoutManager().end();
        getOplusRgbNormalizeManager().clear();
    }

    private IOplusAutoLayoutManager getAutoLayoutManager() {
        return (IOplusAutoLayoutManager) OplusFeatureCache.getOrCreate(IOplusAutoLayoutManager.mDefault, new Object[0]);
    }

    private IOplusRGBNormalizeManager getOplusRgbNormalizeManager() {
        return (IOplusRGBNormalizeManager) OplusFeatureCache.getOrCreate(IOplusRGBNormalizeManager.DEFAULT, new Object[0]);
    }

    public void setCanvasDrawCallback(IBaseCanvasExt.OnCanvasDrawCallback canvasCallback) {
        this.mCanvasCallback = canvasCallback;
    }

    public IBaseCanvasExt.OnCanvasDrawCallback getCanvasDrawCallback() {
        return this.mCanvasCallback;
    }
}
