package android.graphics.drawable;

import android.app.uxicons.CustomAdaptiveIconConfig;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import com.oplus.bluetooth.OplusBluetoothClass;

/* loaded from: classes.dex */
public class AdaptiveIconDrawableExtImpl implements IAdaptiveIconDrawableExt {
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final float EXTRA_INSET_PERCENTAGE = 0.25f;
    private static final String TAG = "AdaptiveIconDrawableExt";
    private AdaptiveIconDrawable mBase;
    private CustomAdaptiveIconConfig mConfig;
    private Canvas mOplusCanvas;
    private OplusLayerState mOplusLayerState;
    private Bitmap mOplusLayersBitmap;
    private Path mOplusMask;
    private Path mOplusMaskScaleOnly;
    private Matrix mCustomMatrix = new Matrix();
    private Rect mBackgroundSizeBounds = new Rect();
    private Rect mBackgroundPositionBounds = new Rect();
    private Rect mForegroundSizeBounds = new Rect();
    private Rect mForegroundPositionBounds = new Rect();
    private Paint mOplusPaint = new Paint(7);

    public AdaptiveIconDrawableExtImpl(Object base) {
        this.mBase = (AdaptiveIconDrawable) base;
    }

    public AdaptiveIconDrawable getAdaptiveIconDrawable() {
        return this.mBase;
    }

    public float getForegroundScalePercent() {
        float fgScale = this.mConfig.getForegroundScalePercent() * this.mConfig.getScalePercent() * 1.0f;
        if (!this.mConfig.getIsPlatformDrawable() && this.mConfig.getIsAdaptiveIconDrawable()) {
            return (1.0f * fgScale) / DEFAULT_VIEW_PORT_SCALE;
        }
        return fgScale;
    }

    public void init() {
        CustomAdaptiveIconConfig customAdaptiveIconConfig = this.mConfig;
        if (customAdaptiveIconConfig != null && customAdaptiveIconConfig.getCustomMask() != null) {
            this.mOplusCanvas = new Canvas();
            this.mOplusMask = new Path(this.mConfig.getCustomMask());
            this.mOplusMaskScaleOnly = new Path(this.mOplusMask);
            this.mOplusLayerState = new OplusLayerState(this.mBase.mLayerState, this.mConfig);
        }
    }

    private boolean drawIcon(Canvas canvas) {
        CustomAdaptiveIconConfig customAdaptiveIconConfig = this.mConfig;
        if (customAdaptiveIconConfig == null || this.mOplusLayersBitmap == null || this.mOplusCanvas == null || customAdaptiveIconConfig.getCustomMask() == null) {
            return false;
        }
        canvas.save();
        Bitmap layersBitmap = this.mOplusLayersBitmap;
        this.mOplusCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        this.mOplusCanvas.setBitmap(layersBitmap);
        if (!this.mConfig.getIsPlatformDrawable() && this.mConfig.getIsAdaptiveIconDrawable()) {
            this.mOplusCanvas.drawColor(OplusBluetoothClass.Device.UNKNOWN);
        }
        if (this.mBase.getBackground() != null) {
            this.mOplusCanvas.save();
            this.mOplusCanvas.translate(this.mBackgroundPositionBounds.left, this.mBackgroundPositionBounds.top);
            this.mBase.getBackground().draw(this.mOplusCanvas);
            this.mOplusCanvas.translate(-this.mBackgroundPositionBounds.left, -this.mBackgroundPositionBounds.top);
            this.mOplusCanvas.restore();
        }
        if (this.mBase.getForeground() != null) {
            this.mOplusCanvas.save();
            this.mOplusCanvas.translate(this.mForegroundPositionBounds.left, this.mForegroundPositionBounds.top);
            this.mBase.getForeground().draw(this.mOplusCanvas);
            this.mOplusCanvas.translate(-this.mForegroundPositionBounds.left, -this.mForegroundPositionBounds.top);
            this.mOplusCanvas.restore();
        }
        this.mOplusPaint.setShader(new BitmapShader(layersBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        if (this.mOplusMaskScaleOnly != null) {
            Rect bounds = this.mBase.getBounds();
            canvas.translate(bounds.left, bounds.top);
            canvas.drawPath(this.mOplusMaskScaleOnly, this.mOplusPaint);
            canvas.translate(-bounds.left, -bounds.top);
        }
        canvas.restore();
        return true;
    }

    protected boolean onDrawableBoundsChange(Rect bounds) {
        CustomAdaptiveIconConfig customAdaptiveIconConfig = this.mConfig;
        if (customAdaptiveIconConfig == null || customAdaptiveIconConfig.getCustomMask() == null) {
            return false;
        }
        try {
            int sizeOffset = updateBounds(bounds);
            updateDrawableBounds();
            updateMaskBounds(bounds, sizeOffset);
            updateParams(bounds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private int updateBounds(Rect b) {
        int bgSize = (int) Math.ceil(b.width() * this.mConfig.getScalePercent());
        int bgDiffSize = b.width() - bgSize;
        int offset = (int) (bgDiffSize / 2.0f);
        this.mBackgroundPositionBounds.set(offset, offset, bgSize + offset, bgSize + offset);
        this.mBackgroundSizeBounds.set(0, 0, bgSize, bgSize);
        int fgSize = (int) Math.ceil(b.width() * this.mConfig.getScalePercent() * this.mConfig.getForegroundScalePercent());
        int fgDiffSize = b.width() - fgSize;
        int fgOffset = (int) (fgDiffSize / 2.0f);
        this.mForegroundSizeBounds.set(0, 0, fgSize, fgSize);
        this.mForegroundPositionBounds.set(fgOffset, fgOffset, fgSize + fgOffset, fgSize + fgOffset);
        if (!this.mConfig.getIsPlatformDrawable() && this.mConfig.getIsAdaptiveIconDrawable()) {
            updateThirdPartAdaptiveIconDrawableBound(this.mBackgroundSizeBounds);
            updateThirdPartAdaptiveIconDrawableBound(this.mForegroundSizeBounds);
        }
        return offset;
    }

    private void updateThirdPartAdaptiveIconDrawableBound(Rect bounds) {
        int cX = bounds.width() / 2;
        int cY = bounds.height() / 2;
        int insetWidth = (int) (bounds.width() / 1.3333334f);
        int insetHeight = (int) (bounds.height() / 1.3333334f);
        bounds.set(cX - insetWidth, cY - insetHeight, cX + insetWidth, cY + insetHeight);
    }

    private void updateDrawableBounds() {
        AdaptiveIconDrawable.ChildDrawable bg = this.mBase.mLayerState.mChildren[0];
        if (bg != null && bg.mDrawable != null) {
            bg.mDrawable.setBounds(this.mBackgroundSizeBounds);
        }
        AdaptiveIconDrawable.ChildDrawable fg = this.mBase.mLayerState.mChildren[1];
        if (fg != null && fg.mDrawable != null) {
            fg.mDrawable.setBounds(this.mForegroundSizeBounds);
        }
    }

    private void updateMaskBounds(Rect b, int sizeOffset) {
        this.mCustomMatrix.reset();
        this.mCustomMatrix.setScale(((b.width() * this.mConfig.getScalePercent()) * 1.0f) / 150.0f, ((b.height() * this.mConfig.getScalePercent()) * 1.0f) / 150.0f);
        this.mOplusMask.transform(this.mCustomMatrix, this.mOplusMaskScaleOnly);
        this.mOplusMaskScaleOnly.offset(sizeOffset, sizeOffset);
    }

    private void updateParams(Rect bounds) {
        Bitmap bitmap = this.mOplusLayersBitmap;
        if (bitmap == null || bitmap.getWidth() != bounds.width() || this.mOplusLayersBitmap.getHeight() != this.mOplusLayersBitmap.getHeight()) {
            this.mOplusLayersBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888, true, ColorSpace.get(ColorSpace.Named.DISPLAY_P3));
        }
        this.mOplusPaint.setAntiAlias(true);
        this.mOplusPaint.setShader(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class OplusLayerState extends Drawable.ConstantState {
        private int mChangingConfigurations;
        private CustomAdaptiveIconConfig mConfig;
        private AdaptiveIconDrawable.LayerState mParentLayerState;

        OplusLayerState(AdaptiveIconDrawable.LayerState layerState, CustomAdaptiveIconConfig config) {
            this.mParentLayerState = layerState;
            this.mConfig = config;
            this.mChangingConfigurations = layerState.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            AdaptiveIconDrawable drawable = new AdaptiveIconDrawable(this.mParentLayerState, (Resources) null);
            IAdaptiveIconDrawableExt ext = drawable.getWrapper().getAdaptiveIconDrawableExt();
            if (ext != null && (ext instanceof AdaptiveIconDrawableExtImpl)) {
                ((AdaptiveIconDrawableExtImpl) ext).mConfig = this.mConfig;
                ext.init();
            }
            return drawable;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            AdaptiveIconDrawable drawable = new AdaptiveIconDrawable(this.mParentLayerState, res);
            IAdaptiveIconDrawableExt ext = drawable.getWrapper().getAdaptiveIconDrawableExt();
            if (ext != null && (ext instanceof AdaptiveIconDrawableExtImpl)) {
                ((AdaptiveIconDrawableExtImpl) ext).mConfig = this.mConfig;
                ext.init();
            }
            return drawable;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }
    }

    public boolean shouldHookGetConstantState() {
        CustomAdaptiveIconConfig customAdaptiveIconConfig = this.mConfig;
        return (customAdaptiveIconConfig == null || customAdaptiveIconConfig.getCustomMask() == null) ? false : true;
    }

    public Drawable.ConstantState getConstantState() {
        return this.mOplusLayerState;
    }

    public boolean hookDraw(Canvas canvas) {
        return drawIcon(canvas);
    }

    public boolean hookOnBoundsChange(Rect bounds) {
        return onDrawableBoundsChange(bounds);
    }

    public Path hookGetIconMask() {
        return this.mOplusMask;
    }

    public boolean hookGetIntrinsicHeight() {
        return this.mConfig != null;
    }

    public boolean hookGetIntrinsicWidth() {
        return this.mConfig != null;
    }

    public void buildAdaptiveIconDrawable(Resources res, int customIconSize, int customIconFgSize, Path customMask, boolean isPlatformDrawable, boolean isAdaptiveIconDrawable) {
        this.mConfig = new CustomAdaptiveIconConfig.Builder(res).setCustomIconSize(customIconSize).setCustomIconFgSize(customIconFgSize).setCustomMask(customMask).setIsPlatformDrawable(isPlatformDrawable).setIsAdaptiveIconDrawable(isAdaptiveIconDrawable).create();
        init();
    }
}
