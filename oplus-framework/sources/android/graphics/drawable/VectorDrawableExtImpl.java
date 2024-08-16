package android.graphics.drawable;

import android.common.OplusFeatureCache;
import android.content.res.Configuration;
import android.content.res.IResourcesExt;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.IVectorDrawableExt;
import android.graphics.drawable.VectorDrawable;
import android.text.TextUtils;
import com.android.internal.graphics.ColorUtils;
import com.oplus.darkmode.IOplusDarkModeManager;
import java.util.ArrayList;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class VectorDrawableExtImpl implements IVectorDrawableExt {

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements IVectorDrawableExt.IStaticExt {
        private static final int ALPHA_LIMIT = 75;
        private static final float CHANGE_UNIT = 0.5f;
        private static final int MODE_FLAG = 16711680;
        private static final String WIDTH_SYMBOL = "path_width";
        private IResourcesExt mBaseResources;
        private int mFilterColor;
        private int mShouldRestoreFillColor = -1;
        private int mShouldRestoreStrokeColor = -1;
        private boolean mHasOriginColor = false;
        private boolean mShouldRestoreFilterColor = false;
        private SumEntity mHEntity = new SumEntity();
        private SumEntity mSEntity = new SumEntity();
        private SumEntity mLEntity = new SumEntity();

        /* loaded from: classes.dex */
        public static class SingleHolder {
            private static final StaticExtImpl INSTACNE = new StaticExtImpl();
        }

        public static StaticExtImpl getInstance(Object object) {
            return SingleHolder.INSTACNE;
        }

        public long changePaintWhenVectorDraw(VectorDrawable vectorDrawable, Canvas canvas, ColorFilter colorFilter, VectorDrawable.VGroup vGroup) {
            if (isDarkMode(canvas)) {
                if (colorFilter != null) {
                    changeFilter(colorFilter);
                } else {
                    calculateVectorColor(vGroup);
                    ColorFilter newFilter = ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).getColorFilterWhenDrawVectorDrawable(this.mHEntity, this.mSEntity, this.mLEntity);
                    this.mHEntity.reset();
                    this.mSEntity.reset();
                    this.mLEntity.reset();
                    if (newFilter == null) {
                        return 0L;
                    }
                    return newFilter.getNativeInstance();
                }
            }
            if (colorFilter == null) {
                return 0L;
            }
            return colorFilter.getNativeInstance();
        }

        public void resetPaintWhenVectorDraw(VectorDrawable vectorDrawable, Canvas canvas, ColorFilter colorFilter) {
            if (isDarkMode(canvas) && colorFilter != null) {
                restoreFilter(colorFilter);
            }
        }

        public void hookVFullInflate(IResourcesExt res) {
            this.mBaseResources = res;
        }

        public Float calculateStrokeWidth(String pathName, float defaultWidth) {
            IResourcesExt iResourcesExt;
            if (!TextUtils.isEmpty(pathName) && pathName.startsWith(WIDTH_SYMBOL) && (iResourcesExt = this.mBaseResources) != null) {
                Configuration configuration = iResourcesExt.getConfiguration();
                if (configuration == null) {
                    return Float.valueOf(defaultWidth);
                }
                OplusExtraConfiguration extraConfig = configuration.getOplusExtraConfiguration();
                if (extraConfig == null) {
                    return Float.valueOf(defaultWidth);
                }
                int fontVariationSettings = extraConfig.mFontVariationSettings;
                float adjustWidth = ((16711680 & fontVariationSettings) >> 16) * 0.5f;
                return Float.valueOf(adjustWidth > 0.0f ? adjustWidth : defaultWidth);
            }
            return Float.valueOf(defaultWidth);
        }

        private void calculatePathColor(VectorDrawable.VFullPath vObject) {
            int fillColor = vObject.getFillColor();
            float fillAlpha = vObject.getFillAlpha();
            if (Color.alpha(fillColor) * fillAlpha >= 75.0f) {
                float[] hsl = new float[3];
                ColorUtils.colorToHSL(fillColor, hsl);
                this.mHEntity.add(hsl[0]);
                this.mSEntity.add(hsl[1]);
                this.mLEntity.add(hsl[2]);
            }
            int strokeColor = vObject.getStrokeColor();
            float strokeAlpha = vObject.getStrokeAlpha();
            if (Color.alpha(strokeColor) * strokeAlpha >= 75.0f) {
                float[] hsl2 = new float[3];
                ColorUtils.colorToHSL(strokeColor, hsl2);
                this.mHEntity.add(hsl2[0]);
                this.mSEntity.add(hsl2[1]);
                this.mLEntity.add(hsl2[2]);
            }
        }

        private void calculateVectorColor(VectorDrawable.VGroup vGroup) {
            if (vGroup == null) {
                return;
            }
            ArrayList children = vGroup.getWrapper().getChildren();
            if (children.size() == 0) {
                return;
            }
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) instanceof VectorDrawable.VGroup) {
                    calculateVectorColor((VectorDrawable.VGroup) children.get(i));
                } else if (children.get(i) instanceof VectorDrawable.VFullPath) {
                    calculatePathColor((VectorDrawable.VFullPath) children.get(i));
                }
            }
        }

        private void changeFilter(ColorFilter colorFilter) {
            if (colorFilter instanceof BlendModeColorFilter) {
                this.mFilterColor = ((BlendModeColorFilter) colorFilter).getColor();
                this.mShouldRestoreFilterColor = true;
                ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).changeColorFilterInDarkMode(colorFilter);
            } else if (colorFilter instanceof PorterDuffColorFilter) {
                this.mFilterColor = ((PorterDuffColorFilter) colorFilter).getColor();
                this.mShouldRestoreFilterColor = true;
                ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).changeColorFilterInDarkMode(colorFilter);
            }
        }

        private void restoreFilter(ColorFilter colorFilter) {
            if (colorFilter != null && this.mShouldRestoreFilterColor) {
                this.mShouldRestoreFilterColor = false;
                if (colorFilter instanceof BlendModeColorFilter) {
                    ((BlendModeColorFilter) colorFilter).getWrapper().setColor(this.mFilterColor);
                } else if (colorFilter instanceof PorterDuffColorFilter) {
                    ((PorterDuffColorFilter) colorFilter).getWrapper().setColor(this.mFilterColor);
                }
            }
        }

        private boolean isDarkMode(Canvas canvas) {
            if (canvas != null) {
                return canvas.mBaseCanvasExt.isDarkMode();
            }
            return false;
        }
    }
}
