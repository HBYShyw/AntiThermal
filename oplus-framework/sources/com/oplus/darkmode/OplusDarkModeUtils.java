package com.oplus.darkmode;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.IBaseCanvasExt;
import android.graphics.IBitmapExt;
import android.graphics.IPathExt;
import android.graphics.IPathWrapper;
import android.graphics.Path;
import android.graphics.PorterDuffColorFilter;
import android.util.DisplayMetrics;
import android.view.IViewExt;
import android.view.View;
import com.android.internal.graphics.ColorUtils;

/* loaded from: classes.dex */
public class OplusDarkModeUtils {
    public static boolean isMaybeBlackColor(int color) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        return isMaybeBlackColor(hsl);
    }

    public static boolean isMaybeWhiteColor(int color) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        return isMaybeWhiteColor(hsl);
    }

    public static boolean isMaybeWhiteColor(float[] hsl) {
        if (hsl[2] >= 0.96d) {
            return true;
        }
        if (hsl[1] != 0.0f || hsl[2] < 0.8d) {
            return ((double) hsl[1]) <= 0.05d && ((double) hsl[2]) >= 0.9d;
        }
        return true;
    }

    public static boolean isMaybeBlackColor(float[] hsl) {
        if (hsl[2] <= 0.04d) {
            return true;
        }
        if (hsl[1] == 0.0f && hsl[2] <= 0.21d) {
            return true;
        }
        if (hsl[0] != 180.0f || hsl[1] > 0.1d || hsl[2] > 0.21d) {
            return ((double) hsl[1]) <= 0.05d && ((double) hsl[2]) <= 0.11d;
        }
        return true;
    }

    public static float getDpDensity(Application application) {
        if (application != null) {
            try {
                return application.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160.0f;
            } catch (Exception e) {
            }
        }
        return DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0f;
    }

    public static float getScreenWidth(Application application) {
        if (application != null) {
            try {
                return application.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
            } catch (Exception e) {
                return 1440.0f;
            }
        }
        return 1440.0f;
    }

    public static float getDpDensity(View view) {
        if (view != null) {
            try {
                return view.getContext().getResources().getDisplayMetrics().densityDpi / 160.0f;
            } catch (Exception e) {
            }
        }
        return DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0f;
    }

    public static float getScreenWidth(View view) {
        if (view != null) {
            try {
                return view.getContext().getResources().getDisplayMetrics().widthPixels;
            } catch (Exception e) {
                return 1440.0f;
            }
        }
        return 1440.0f;
    }

    public static int makeColorBackground(int originColor, float backgroundMaxL) {
        double[] lab = new double[3];
        ColorUtils.colorToLAB(originColor, lab);
        double invertedL = 100.0d - lab[0];
        if (invertedL < lab[0]) {
            if (backgroundMaxL != -1.0f) {
                invertedL = backgroundMaxL + ((invertedL / 50.0d) * (50.0f - backgroundMaxL));
            }
            lab[0] = invertedL;
            int newColor = ColorUtils.LABToColor(lab[0], lab[1], lab[2]);
            return Color.argb(Color.alpha(originColor), Color.red(newColor), Color.green(newColor), Color.blue(newColor));
        }
        return originColor;
    }

    public static int makeColorForeground(int originColor, float foregroundMinL) {
        double[] lab = new double[3];
        ColorUtils.colorToLAB(originColor, lab);
        double invertedL = 100.0d - lab[0];
        if (invertedL > lab[0]) {
            if (foregroundMinL != -1.0f) {
                invertedL = (((invertedL - 50.0d) / 50.0d) * (foregroundMinL - 50.0f)) + 50.0d;
            }
            lab[0] = invertedL;
            int newColor = ColorUtils.LABToColor(lab[0], lab[1], lab[2]);
            return Color.argb(Color.alpha(originColor), Color.red(newColor), Color.green(newColor), Color.blue(newColor));
        }
        return originColor;
    }

    public static int makeSVGColorForeground(int originColor, float foregroundMinL) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(originColor, hsl);
        if (isMaybeBlackColor(hsl)) {
            hsl[2] = 1.0f - hsl[2];
            int newColor = ColorUtils.HSLToColor(hsl);
            return Color.argb(Color.alpha(originColor), Color.red(newColor), Color.green(newColor), Color.blue(newColor));
        }
        return originColor;
    }

    public static int makeSVGColorBackground(int originColor, float backgroundMaxL) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(originColor, hsl);
        if (isMaybeWhiteColor(hsl)) {
            hsl[2] = 1.0f - hsl[2];
            int newColor = ColorUtils.HSLToColor(hsl);
            return Color.argb(Color.alpha(originColor), Color.red(newColor), Color.green(newColor), Color.blue(newColor));
        }
        return originColor;
    }

    public static void setCanvasTransformType(Canvas canvas, int transformType, int width, int height) {
        IBaseCanvasExt baseCanvas = getOplusCanvas(canvas);
        if (baseCanvas != null) {
            baseCanvas.setTransformType(transformType);
            baseCanvas.setViewArea(width, height);
        }
    }

    public static boolean hasCalculatedColor(Bitmap bitmap) {
        IBitmapExt bitmapExt = getBitmapExt(bitmap);
        return bitmapExt != null && bitmapExt.hasCalculatedColor();
    }

    public static void setHasCalculatedColor(Bitmap bitmap, boolean value) {
        IBitmapExt bitmapExt = getBitmapExt(bitmap);
        if (bitmapExt != null) {
            bitmapExt.setHasCalculatedColor(value);
        }
    }

    public static boolean isAddArea(Path path) {
        IPathWrapper wrapper;
        IPathExt pathExt;
        if (path != null && (wrapper = path.getWrapper()) != null && (pathExt = wrapper.getPathExt()) != null) {
            return pathExt.isAddArea();
        }
        return false;
    }

    public static void setColorFilterColor(ColorFilter filter, int color) {
        if (filter instanceof BlendModeColorFilter) {
            ((BlendModeColorFilter) filter).getWrapper().setColor(color);
        } else if (filter instanceof PorterDuffColorFilter) {
            ((PorterDuffColorFilter) filter).getWrapper().setColor(color);
        }
    }

    public static boolean isViewSrc(Bitmap bitmap) {
        IBitmapExt bitmapExt = getBitmapExt(bitmap);
        return bitmapExt != null && bitmapExt.isViewSrc();
    }

    public static boolean isCanvasBaseBitmap(Bitmap bitmap) {
        IBitmapExt bitmapExt = getBitmapExt(bitmap);
        return bitmapExt != null && bitmapExt.isCanvasBaseBitmap();
    }

    public static boolean isAssetSource(Bitmap bitmap) {
        IBitmapExt bitmapExt = getBitmapExt(bitmap);
        return bitmapExt != null && bitmapExt.isAssetSource();
    }

    public static int getColorState(Bitmap bitmap) {
        IBitmapExt bitmapExt = getBitmapExt(bitmap);
        if (bitmapExt != null) {
            return bitmapExt.getColorState();
        }
        return 0;
    }

    public static void setColorState(Bitmap bitmap, int colorState) {
        IBitmapExt bitmapExt = getBitmapExt(bitmap);
        if (bitmapExt != null) {
            bitmapExt.setColorState(colorState);
        }
    }

    public static void setCrudeState(View view, int state) {
        IViewExt viewExt = getViewExt(view);
        if (viewExt != null) {
            viewExt.setCrudeState(state);
        }
    }

    public static int getCrudeState(View view) {
        IViewExt viewExt = getViewExt(view);
        if (viewExt != null) {
            return viewExt.getCrudeState();
        }
        return 0;
    }

    public static int getViewType(View view) {
        IViewExt viewExt = getViewExt(view);
        if (viewExt != null) {
            return viewExt.getOplusViewType();
        }
        return 0;
    }

    public static void setViewType(View view, int viewType) {
        IViewExt viewExt = getViewExt(view);
        if (viewExt != null) {
            viewExt.setOplusViewTypeLocked(viewType);
        }
    }

    public static int getViewType(Canvas canvas) {
        IBaseCanvasExt baseCanvas = getOplusCanvas(canvas);
        if (baseCanvas != null) {
            return baseCanvas.getOplusViewType();
        }
        return 0;
    }

    public static void setViewType(Canvas canvas, int viewType) {
        IBaseCanvasExt baseCanvas = getOplusCanvas(canvas);
        if (baseCanvas != null) {
            baseCanvas.setOplusViewTypeLocked(viewType);
        }
    }

    private static IBaseCanvasExt getOplusCanvas(Canvas canvas) {
        return canvas.mBaseCanvasExt;
    }

    private static IBitmapExt getBitmapExt(Bitmap bitmap) {
        if (bitmap != null) {
            return bitmap.mBitmapExt;
        }
        return null;
    }

    private static IViewExt getViewExt(View view) {
        return view.getViewWrapper().getViewExt();
    }

    public static boolean isBiliBili(Application application) {
        return false;
    }

    public static boolean isWeiBo(Application application) {
        if (application != null) {
            try {
                if (application.getApplicationContext() != null) {
                    return "com.sina.weibo".equals(application.getApplicationContext().getPackageName());
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
