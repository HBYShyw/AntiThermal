package com.oplus.theme;

import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.oplus.bluetooth.OplusBluetoothClass;
import com.oplus.util.OplusTypeCastingHelper;
import com.oplus.util.UxScreenUtil;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public final class OplusConvertIcon {
    private static final float COVER_ICON_RADIO = 0.62f;
    private static final int DARKMODE_ICON_TRANSLATE_BIT_LENGTH = 61;
    private static final boolean DEBUG_ENABLE;
    private static final boolean DEBUG_NORMAL;
    private static final float DEFAULT_RELATIVE_BRIGHTNESS = 0.84f;
    private static final float FLOAT_OFFSET = 0.5f;
    private static final float ICON_SIZE_RADIO_OVER_DENSITY_400 = 1.055f;
    private static final String IPHONE_STYLE_BG_NAME = "iphone_style_bg.png";
    private static final String IPHONE_STYLE_FG_NAME = "iphone_style_fg.png";
    private static final String NEW_IPHONE_STYLE_BG_NAME = "new_iphone_style_bg.png";
    private static final String NEW_IPHONE_STYLE_MASK_NAME = "new_iphone_style_mask.png";
    private static final int NORMAL_ICON_SIZE = 168;
    private static final int NORMAL_SCREEN_WIDTH = 1080;
    private static final int PIXEL_ALPHA_THRESHOLD = 220;
    private static final int PIXEL_SAMPLE = 4;
    private static final int PIXEL_THRESHOLD = 6;
    private static final String TAG = "OplusConvertIcon";
    private static final int THEME_ICON_BIT = 65535;
    private static final int THEME_ICON_OFFSET = 32;
    private static final float THEME_ICON_SCALE_MIN = 0.3f;
    private static final float THEME_ICON_SIZE_DIV = 100.0f;
    private static final int THEME_ICON_SIZE_RANGE = 32;
    private static int mUserId;
    private static final Canvas sCanvas;
    private static float sConfigScale;
    private static String sCoverBackgroundPic;
    private static LightingColorFilter sDarkModeColorFilter;
    private static int sDetectMaskBorderOffset;
    private static final String[] sDrawableDirs;
    private static Drawable sIconBackground;
    private static IconBgType sIconBgType;
    private static Drawable sIconForeground;
    private static int sIconHeight;
    private static int sIconLeft;
    private static int sIconSize;
    private static int sIconTop;
    private static int sIconWidth;
    private static volatile long sLastModified;
    private static String sMaskBackgroundPic;
    private static Bitmap sMaskBitmap;
    private static String sMaskForegroundPic;
    private static boolean sNeedDrawForeground;
    private static int sNoTransparentScalePixels;
    private static final Rect sOldBounds;
    private static Paint sPaint;
    private static int sThemeParamScale;
    private static int sThemeParamXOffset;
    private static int sThemeParamYOffset;
    private static float sThemeScale;

    /* loaded from: classes.dex */
    public enum IconBgType {
        MASK,
        COVER,
        SCALE
    }

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        DEBUG_ENABLE = z;
        DEBUG_NORMAL = z;
        sDrawableDirs = new String[]{"res/drawable-hdpi/", "res/drawable-xhdpi/", "res/drawable-xxhdpi/"};
        sLastModified = 0L;
        sOldBounds = new Rect();
        Canvas canvas = new Canvas();
        sCanvas = canvas;
        sThemeParamScale = 128;
        sThemeParamXOffset = 0;
        sThemeParamYOffset = 0;
        sDetectMaskBorderOffset = 10;
        sIconSize = -1;
        sIconWidth = -1;
        sIconHeight = -1;
        sIconLeft = 0;
        sIconTop = 0;
        sConfigScale = 0.0f;
        sThemeScale = 1.0f;
        sNoTransparentScalePixels = 11;
        sMaskBackgroundPic = null;
        sMaskForegroundPic = null;
        sCoverBackgroundPic = null;
        sIconBackground = null;
        sIconForeground = null;
        sMaskBitmap = null;
        sNeedDrawForeground = false;
        sIconBgType = IconBgType.MASK;
        sPaint = new Paint();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(4, 3));
    }

    public static boolean hasInit() {
        if (sCoverBackgroundPic == null && sMaskBackgroundPic == null && sMaskForegroundPic == null) {
            return false;
        }
        return true;
    }

    public static Bitmap convertIconBitmap(Drawable icon, Resources res, boolean isThirdPart) {
        return convertIconBitmap(icon, res, isThirdPart, false);
    }

    public static Bitmap convertIconBitmap(Drawable icon, Resources res, boolean isThirdPart, boolean forceCutAndScale) {
        Bitmap.Config bitmapConfig;
        if (icon == null || res == null) {
            return null;
        }
        Canvas canvas = sCanvas;
        synchronized (canvas) {
            initIconSize(res);
            int width = sIconWidth;
            int height = sIconHeight;
            Bitmap originalBitmap = null;
            if (!(icon instanceof PaintDrawable)) {
                if (!(icon instanceof BitmapDrawable)) {
                    if ((icon instanceof NinePatchDrawable) || (icon instanceof VectorDrawable) || (icon instanceof AdaptiveIconDrawable)) {
                        int intricW = icon.getIntrinsicWidth();
                        int intricH = icon.getIntrinsicHeight();
                        if (intricW >= 0 && intricH >= 0) {
                            originalBitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas2 = new Canvas(originalBitmap);
                            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                            icon.draw(canvas2);
                        }
                        Log.w(TAG, "convertIconBitmap error icon size = " + intricW + ", " + intricH);
                        return null;
                    }
                } else {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) icon;
                    Bitmap bitmap = computeDesity(bitmapDrawable.getBitmap(), res);
                    if (isThirdPart && ((bitmapConfig = (originalBitmap = bitmap).getConfig()) == null || Bitmap.Config.RGBA_F16.equals(bitmapConfig))) {
                        Log.i(TAG, "convertIconBitmap...set the bitmap config to ARGB_8888. bitmapConfig = " + bitmapConfig);
                        originalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    }
                }
            } else {
                PaintDrawable painter = (PaintDrawable) icon;
                painter.setIntrinsicWidth(width);
                painter.setIntrinsicHeight(height);
            }
            int intricW2 = sIconWidth;
            if (intricW2 <= 0) {
                return null;
            }
            Bitmap bitmap2 = Bitmap.createBitmap(intricW2, sIconHeight, Bitmap.Config.ARGB_8888);
            if (originalBitmap != null) {
                bitmap2.setDensity(originalBitmap.getDensity());
            }
            canvas.setBitmap(bitmap2);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            int addPixels = 0;
            boolean hasTransparentPixels = false;
            if (icon instanceof BitmapDrawable) {
                hasTransparentPixels = hasTransparentPixels(originalBitmap);
            }
            if (!hasTransparentPixels) {
                addPixels = sNoTransparentScalePixels;
            }
            if (isThirdPart) {
                setDarkFilterToPaint(res);
                if (forceCutAndScale) {
                    cutAndScaleBitmap(icon, originalBitmap, res, canvas);
                } else if (sIconBgType == IconBgType.COVER) {
                    coverBitmap(icon, originalBitmap, res, canvas);
                } else if (sIconBgType == IconBgType.MASK && sMaskBitmap != null) {
                    maskBitmap(icon, originalBitmap, res, canvas);
                } else {
                    cutAndScaleBitmap(icon, originalBitmap, res, canvas);
                }
            } else {
                setDarkFilterToDrawable(res, icon);
                int sourceWidth = icon.getIntrinsicWidth();
                int sourceHeight = icon.getIntrinsicHeight();
                if (sourceWidth > 0 && sourceHeight > 0) {
                    float ratio = sourceWidth / sourceHeight;
                    if (sourceWidth > sourceHeight) {
                        height = (int) (width / ratio);
                    } else if (sourceHeight > sourceWidth) {
                        width = (int) (height * ratio);
                    }
                }
                int left = (sIconWidth - width) / 2;
                int top = (sIconHeight - height) / 2;
                if (sConfigScale != 0.0f) {
                    canvas.translate(sIconLeft, sIconTop);
                    float f = sConfigScale;
                    canvas.scale(f, f);
                }
                Rect rect = sOldBounds;
                rect.set(icon.getBounds());
                icon.setBounds(left + addPixels, top + addPixels, (left + width) - addPixels, (top + height) - addPixels);
                icon.draw(canvas);
                icon.setBounds(rect);
                canvas.setBitmap(null);
            }
            return bitmap2;
        }
    }

    static void coverBitmapNoCut(Drawable icon, Bitmap originalBitmap, Resources res, Canvas canvas) {
        if (originalBitmap != null) {
            Drawable sIconBackground2 = OplusThirdPartUtil.getLauncherDrawableByName(res, sCoverBackgroundPic);
            if (sIconBackground2 != null) {
                Rect rect = sOldBounds;
                rect.set(sIconBackground2.getBounds());
                sIconBackground2.setBounds(0, 0, sIconWidth, sIconHeight);
                sIconBackground2.draw(canvas);
                sIconBackground2.setBounds(rect);
            }
            float f = 1.0f;
            if (res.getDisplayMetrics().xdpi > 400.0f && OplusThirdPartUtil.mIsDefaultTheme) {
                f = ICON_SIZE_RADIO_OVER_DENSITY_400;
            }
            int sourceWidth = icon.getIntrinsicWidth();
            int sourceHeight = icon.getIntrinsicHeight();
            int width = (int) (sourceWidth * f);
            int height = (int) (sourceHeight * f);
            int l = (sIconWidth - width) / 2;
            int t = (sIconHeight - height) / 2;
            icon.setBounds(l, t, l + width, t + height);
            icon.draw(canvas);
        }
    }

    static void coverBitmap(Drawable icon, Bitmap originalBitmap, Resources res, Canvas canvas) {
        if (originalBitmap != null) {
            if (!originalBitmap.hasAlpha()) {
                originalBitmap.setHasAlpha(true);
            }
            Drawable background = sIconBackground;
            if (background == null) {
                background = OplusThirdPartUtil.getLauncherDrawableByNameForUser(res, sCoverBackgroundPic, mUserId);
            }
            if (background != null) {
                Rect rect = sOldBounds;
                rect.set(background.getBounds());
                background.setBounds(0, 0, sIconWidth, sIconHeight);
                background.draw(canvas);
                background.setBounds(rect);
            }
            OplusMaskBitmapUtilities mbu = OplusMaskBitmapUtilities.getInstance();
            Bitmap scale = originalBitmap.getConfig() != null ? mbu.cutAndScaleBitmap(originalBitmap) : originalBitmap;
            if (scale != null) {
                int w = scale.getWidth();
                int h = scale.getHeight();
                canvas.drawBitmap(scale, ((sIconWidth - w) / 2) + sThemeParamXOffset, ((sIconHeight - h) / 2) + sThemeParamYOffset, sPaint);
            } else {
                Log.i(TAG, "coverBitmap -- scale == null");
            }
        }
        if (sNeedDrawForeground) {
            drawForeground(res, canvas);
        }
    }

    static void cutAndScaleBitmap(Drawable icon, Bitmap originalBitmap, Resources res, Canvas canvas) {
        if (originalBitmap != null) {
            if (!originalBitmap.hasAlpha()) {
                originalBitmap.setHasAlpha(true);
            }
            OplusMaskBitmapUtilities mbu = OplusMaskBitmapUtilities.getInstance();
            Bitmap scale = originalBitmap.getConfig() != null ? mbu.cutAndScaleBitmap(originalBitmap) : originalBitmap;
            if (scale != null) {
                int w = scale.getWidth();
                int h = scale.getHeight();
                canvas.drawBitmap(scale, (sIconWidth - w) / 2, (sIconHeight - h) / 2, sPaint);
                return;
            }
            Log.i(TAG, "cutAndScaleBitmap -- scale == null");
        }
    }

    static void maskBitmap(Drawable icon, Bitmap originalBitmap, Resources res, Canvas canvas) {
        Bitmap originalBitmap2;
        int width = sIconWidth;
        int height = sIconHeight;
        if (originalBitmap != null) {
            originalBitmap2 = originalBitmap;
        } else {
            originalBitmap2 = Bitmap.createBitmap(sIconWidth, sIconHeight, Bitmap.Config.ARGB_8888);
            canvas.setBitmap(originalBitmap2);
            Rect rect = sOldBounds;
            rect.set(icon.getBounds());
            icon.setBounds(0, 0, sIconWidth, sIconHeight);
            icon.draw(canvas);
            icon.setBounds(rect);
        }
        if (originalBitmap2 == null) {
            Log.i(TAG, "maskBitmap -- originalBitmap == null");
        } else {
            if (!originalBitmap2.hasAlpha()) {
                originalBitmap2.setHasAlpha(true);
            }
            OplusMaskBitmapUtilities mbu = OplusMaskBitmapUtilities.getInstance();
            Bitmap scaleAndMaskBitmap = mbu.scaleAndMaskBitmap(originalBitmap2);
            int addPixels = 0;
            Bitmap thirdPartyIconBitmap = null;
            if (scaleAndMaskBitmap == null) {
                Log.i(TAG, "maskBitmap -- scale == null");
            } else {
                int srcWidth = scaleAndMaskBitmap.getWidth();
                int targetWidth = srcWidth;
                boolean hasTransparentPixels = hasTransparentPixels(originalBitmap2);
                if (!hasTransparentPixels && srcWidth > sNoTransparentScalePixels * 2) {
                    addPixels = sNoTransparentScalePixels;
                    targetWidth = srcWidth - (addPixels * 2);
                }
                if (targetWidth <= 0) {
                    Log.i(TAG, "maskBitmap -- targetWidth <= 0");
                } else {
                    float widthScale = (targetWidth * 1.0f) / srcWidth;
                    Matrix matrix = new Matrix();
                    matrix.postScale(widthScale, widthScale, 0.0f, 0.0f);
                    thirdPartyIconBitmap = Bitmap.createBitmap(targetWidth, targetWidth, Bitmap.Config.ARGB_8888);
                    thirdPartyIconBitmap.setDensity(originalBitmap2.getDensity());
                    Canvas tmpCanvas = new Canvas(thirdPartyIconBitmap);
                    Paint paint = new Paint();
                    tmpCanvas.drawBitmap(scaleAndMaskBitmap, matrix, paint);
                }
            }
            if (sConfigScale != 0.0f) {
                canvas.translate(sIconLeft, sIconTop);
                float f = sConfigScale;
                canvas.scale(f, f);
            }
            Drawable background = sIconBackground;
            if (background == null) {
                background = OplusThirdPartUtil.getLauncherDrawableByNameForUser(res, sMaskBackgroundPic, mUserId);
            }
            if (background == null) {
                Log.i(TAG, "maskBitmap -- sIconBackground == null");
            } else {
                int sourceWidth = background.getIntrinsicWidth();
                int sourceHeight = background.getIntrinsicHeight();
                if (sourceWidth > 0 && sourceHeight > 0) {
                    float ratio = sourceWidth / sourceHeight;
                    if (sourceWidth > sourceHeight) {
                        height = (int) (width / ratio);
                    } else if (sourceHeight > sourceWidth) {
                        width = (int) (height * ratio);
                    }
                }
                int left = (sIconWidth - width) / 2;
                int top = (sIconHeight - height) / 2;
                setDarkFilterToDrawable(res, background);
                Rect rect2 = sOldBounds;
                rect2.set(background.getBounds());
                background.setBounds(left + addPixels, top + addPixels, (left + width) - addPixels, (top + height) - addPixels);
                background.draw(canvas);
                background.setBounds(rect2);
            }
            if (scaleAndMaskBitmap != null && thirdPartyIconBitmap != null) {
                int w = scaleAndMaskBitmap.getWidth();
                int h = scaleAndMaskBitmap.getHeight();
                if (w < 0 || h < 0) {
                    Log.i(TAG, "maskBitmap -- w or h < 0");
                } else {
                    int i = sIconWidth;
                    int xEndPosDiffer = ((w - i) / 2) + sThemeParamXOffset;
                    if (xEndPosDiffer > -1) {
                        canvas.drawBitmap(thirdPartyIconBitmap, ((i - w) / 2) + addPixels, ((sIconHeight - h) / 2) + addPixels, new Paint());
                    } else {
                        canvas.drawBitmap(thirdPartyIconBitmap, ((i - w) / 2) + r11 + addPixels, ((sIconHeight - h) / 2) + sThemeParamYOffset + addPixels, sPaint);
                    }
                }
            } else {
                Log.i(TAG, "maskBitmap -- scale == null");
            }
        }
        if (sNeedDrawForeground) {
            drawForeground(res, canvas);
        }
    }

    public static void drawForeground(Resources res, Canvas canvas) {
        Drawable foreground = sIconForeground;
        int width = sIconWidth;
        int height = sIconHeight;
        if (foreground == null) {
            foreground = OplusThirdPartUtil.getLauncherDrawableByNameForUser(res, sMaskForegroundPic, mUserId);
        }
        if (foreground != null) {
            int sourceWidth = foreground.getIntrinsicWidth();
            int sourceHeight = foreground.getIntrinsicHeight();
            if (sourceWidth > 0 && sourceHeight > 0) {
                float ratio = sourceWidth / sourceHeight;
                if (sourceWidth > sourceHeight) {
                    height = (int) (width / ratio);
                } else if (sourceHeight > sourceWidth) {
                    width = (int) (height * ratio);
                }
            }
            int addPixels = 0;
            if (foreground instanceof BitmapDrawable) {
                boolean hasTransparentPixels = hasTransparentPixels(((BitmapDrawable) foreground).getBitmap());
                if (!hasTransparentPixels) {
                    addPixels = sNoTransparentScalePixels;
                }
            }
            int left = (sIconWidth - width) / 2;
            int top = (sIconHeight - height) / 2;
            setDarkFilterToDrawable(res, foreground);
            Rect rect = sOldBounds;
            rect.set(foreground.getBounds());
            foreground.setBounds(left + addPixels, top + addPixels, (left + width) - addPixels, (top + height) - addPixels);
            foreground.draw(canvas);
            foreground.setBounds(rect);
        }
    }

    public static void initThemeParamForUser(Resources res, String maskBg, String maskFg, String coverBg, int useId) {
        OplusIconParam oplusIconParam = new OplusIconParam(OplusThemeUtil.THEME_INFO_NAME);
        oplusIconParam.parseXmlForUser(useId);
        sThemeScale = 1.0f;
        float tempRatio = oplusIconParam.getScale();
        if (tempRatio <= 0.0f) {
            if (sIconBgType == IconBgType.COVER) {
                tempRatio = COVER_ICON_RADIO;
            } else if (sIconBgType == IconBgType.SCALE) {
                tempRatio = 1.0f;
            } else if (sIconBgType == IconBgType.MASK) {
                tempRatio = 1.0f;
            }
        }
        sThemeScale = tempRatio;
        initIconSize(res);
        sThemeParamScale = (int) (sIconSize * tempRatio);
        boolean z = DEBUG_NORMAL;
        if (z) {
            Log.i(TAG, "initThemeParam getScale= " + tempRatio + ", sIconSize = " + sIconSize + ", sConfigScale: " + sConfigScale);
        }
        float tempRatio2 = oplusIconParam.getXOffset();
        sThemeParamXOffset = (int) (sIconSize * tempRatio2);
        if (z) {
            Log.i(TAG, "initThemeParam getXOffset= " + tempRatio2 + ", sThemeParamXOffset = " + sThemeParamXOffset);
        }
        float tempRatio3 = oplusIconParam.getYOffset();
        sThemeParamYOffset = (int) (sIconSize * tempRatio3);
        if (z) {
            Log.i(TAG, "initThemeParam getYOffset= " + tempRatio3 + ", sThemeParamYOffset = " + sThemeParamYOffset);
        }
        sDetectMaskBorderOffset = (int) (sIconSize * oplusIconParam.getDetectMaskBorderOffset());
        setIconBgFgRes(maskBg, maskFg, coverBg);
    }

    public static void initThemeParam(Resources res, String maskBg, String maskFg, String coverBg) {
        OplusIconParam oplusIconParam = new OplusIconParam(OplusThemeUtil.THEME_INFO_NAME);
        oplusIconParam.parseXml();
        float tempRatio = oplusIconParam.getScale();
        if (tempRatio <= 0.0f) {
            if (sIconBgType == IconBgType.COVER) {
                tempRatio = COVER_ICON_RADIO;
            } else if (sIconBgType == IconBgType.SCALE) {
                tempRatio = 1.0f;
            } else if (sIconBgType == IconBgType.MASK) {
                tempRatio = 1.0f;
            }
        }
        initIconSize(res);
        sThemeParamScale = (int) (sIconSize * tempRatio);
        boolean z = DEBUG_NORMAL;
        if (z) {
            Log.i(TAG, "initThemeParam getScale= " + tempRatio + ", sThemeParamScale = " + sThemeParamScale);
        }
        float tempRatio2 = oplusIconParam.getXOffset();
        sThemeParamXOffset = (int) (sIconSize * tempRatio2);
        if (z) {
            Log.i(TAG, "initThemeParam getXOffset= " + tempRatio2 + ", sThemeParamXOffset = " + sThemeParamXOffset);
        }
        float tempRatio3 = oplusIconParam.getYOffset();
        sThemeParamYOffset = (int) (sIconSize * tempRatio3);
        if (z) {
            Log.i(TAG, "initThemeParam getYOffset= " + tempRatio3 + ", sThemeParamYOffset = " + sThemeParamYOffset);
        }
        sDetectMaskBorderOffset = (int) (sIconSize * oplusIconParam.getDetectMaskBorderOffset());
        setIconBgFgRes(maskBg, maskFg, coverBg);
    }

    public static IconBgType getIconBgType(Resources resources) {
        String path = OplusThirdPartUtil.sThemePath;
        ZipFile zipFile = null;
        String launcherName = OplusThirdPartUtil.getLauncherName(path);
        try {
            try {
                ZipFile zipFile2 = new ZipFile(path + launcherName);
                if (judgePicExist(zipFile2, IPHONE_STYLE_BG_NAME)) {
                    if (judgePicExist(zipFile2, IPHONE_STYLE_FG_NAME)) {
                        sNeedDrawForeground = true;
                    }
                    zipFile2.close();
                    IconBgType iconBgType = IconBgType.COVER;
                    sIconBgType = iconBgType;
                    try {
                        zipFile2.close();
                    } catch (IOException e) {
                        Log.e(TAG, "getIconBgType: e = " + e);
                    }
                    return iconBgType;
                }
                if (!judgePicExist(zipFile2, NEW_IPHONE_STYLE_MASK_NAME)) {
                    try {
                        zipFile2.close();
                    } catch (IOException e2) {
                        Log.e(TAG, "getIconBgType: e = " + e2);
                    }
                    IconBgType iconBgType2 = IconBgType.SCALE;
                    sIconBgType = iconBgType2;
                    return iconBgType2;
                }
                if (judgePicExist(zipFile2, IPHONE_STYLE_FG_NAME)) {
                    sNeedDrawForeground = true;
                }
                zipFile2.close();
                IconBgType iconBgType3 = IconBgType.MASK;
                sIconBgType = iconBgType3;
                try {
                    zipFile2.close();
                } catch (IOException e3) {
                    Log.e(TAG, "getIconBgType: e = " + e3);
                }
                return iconBgType3;
            } catch (IOException e4) {
                Log.e(TAG, "getIconBgType: e = " + e4);
                IconBgType iconBgType4 = IconBgType.MASK;
                sIconBgType = iconBgType4;
                if (0 != 0) {
                    try {
                        zipFile.close();
                    } catch (IOException e5) {
                        Log.e(TAG, "getIconBgType: e = " + e5);
                    }
                }
                return iconBgType4;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    zipFile.close();
                } catch (IOException e6) {
                    Log.e(TAG, "getIconBgType: e = " + e6);
                }
            }
            throw th;
        }
    }

    public static IconBgType getIconBgTypeForUser(Resources resources, int userId) {
        String path = OplusThirdPartUtil.sThemePath;
        ZipFile zipFile = null;
        String launcherName = OplusThirdPartUtil.getLauncherName(path);
        try {
            try {
                ZipFile zipFile2 = new ZipFile(path + launcherName);
                if (judgePicExist(zipFile2, IPHONE_STYLE_BG_NAME)) {
                    if (judgePicExist(zipFile2, IPHONE_STYLE_FG_NAME)) {
                        sNeedDrawForeground = true;
                    }
                    zipFile2.close();
                    IconBgType iconBgType = IconBgType.COVER;
                    sIconBgType = iconBgType;
                    try {
                        zipFile2.close();
                    } catch (IOException e) {
                        Log.e(TAG, "getIconBgType: e = " + e);
                    }
                    return iconBgType;
                }
                if (!judgePicExist(zipFile2, NEW_IPHONE_STYLE_MASK_NAME)) {
                    try {
                        zipFile2.close();
                    } catch (IOException e2) {
                        Log.e(TAG, "getIconBgType: e = " + e2);
                    }
                    IconBgType iconBgType2 = IconBgType.SCALE;
                    sIconBgType = iconBgType2;
                    return iconBgType2;
                }
                if (judgePicExist(zipFile2, IPHONE_STYLE_FG_NAME)) {
                    sNeedDrawForeground = true;
                }
                zipFile2.close();
                IconBgType iconBgType3 = IconBgType.MASK;
                sIconBgType = iconBgType3;
                try {
                    zipFile2.close();
                } catch (IOException e3) {
                    Log.e(TAG, "getIconBgType: e = " + e3);
                }
                return iconBgType3;
            } catch (IOException e4) {
                Log.e(TAG, "getIconBgType: e = " + e4);
                IconBgType iconBgType4 = IconBgType.MASK;
                sIconBgType = iconBgType4;
                if (0 != 0) {
                    try {
                        zipFile.close();
                    } catch (IOException e5) {
                        Log.e(TAG, "getIconBgType: e = " + e5);
                    }
                }
                return iconBgType4;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    zipFile.close();
                } catch (IOException e6) {
                    Log.e(TAG, "getIconBgType: e = " + e6);
                }
            }
            throw th;
        }
    }

    public static boolean judgePicExist(String zipFilePath, String picName) {
        StringBuilder sb;
        boolean exist = false;
        ZipFile file = null;
        try {
            try {
                file = new ZipFile(zipFilePath);
                exist = judgePicExist(file, picName);
            } catch (IOException e) {
                Log.e(TAG, "judgePicExist: e " + e);
                if (file != null) {
                    try {
                        file.close();
                    } catch (Exception e2) {
                        e = e2;
                        sb = new StringBuilder();
                        Log.e(TAG, sb.append("judgePicExist: Closing e ").append(e).toString());
                        return exist;
                    }
                }
            }
            try {
                file.close();
            } catch (Exception e3) {
                e = e3;
                sb = new StringBuilder();
                Log.e(TAG, sb.append("judgePicExist: Closing e ").append(e).toString());
                return exist;
            }
            return exist;
        } catch (Throwable th) {
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e4) {
                    Log.e(TAG, "judgePicExist: Closing e " + e4);
                }
            }
            throw th;
        }
    }

    public static boolean judgePicExist(ZipFile zipFile, String picName) {
        for (int i = sDrawableDirs.length - 1; i >= 0; i--) {
            ZipEntry entry = zipFile.getEntry(sDrawableDirs[i] + picName);
            if (entry != null) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap getMaskBitmapForUser(Resources res, String picName, int userId) {
        Bitmap bitmap = sMaskBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            sMaskBitmap = null;
        }
        initIconSize(res);
        Drawable mask = OplusThirdPartUtil.getLauncherDrawableByNameForUser(res, picName, userId);
        sMaskBitmap = Bitmap.createBitmap(sIconWidth, sIconHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = sCanvas;
        synchronized (canvas) {
            canvas.setBitmap(sMaskBitmap);
            if (mask == null) {
                canvas.drawColor(OplusBluetoothClass.Device.UNKNOWN);
            } else {
                mask.setBounds(0, 0, sIconWidth, sIconHeight);
                mask.draw(canvas);
            }
            canvas.setBitmap(null);
        }
        return sMaskBitmap;
    }

    public static Bitmap getMaskBitmap(Resources res, String picName) {
        Bitmap bitmap = sMaskBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            sMaskBitmap = null;
        }
        initIconSize(res);
        Drawable mask = OplusThirdPartUtil.getLauncherDrawableByName(res, picName);
        Bitmap createBitmap = Bitmap.createBitmap(sIconWidth, sIconHeight, Bitmap.Config.ARGB_8888);
        sMaskBitmap = createBitmap;
        Canvas canvas = sCanvas;
        canvas.setBitmap(createBitmap);
        if (mask != null) {
            mask.setBounds(0, 0, sIconWidth, sIconHeight);
            mask.draw(canvas);
        } else {
            canvas.drawColor(OplusBluetoothClass.Device.UNKNOWN);
        }
        canvas.setBitmap(null);
        return sMaskBitmap;
    }

    public static int getThemeParamScale() {
        return sThemeParamScale;
    }

    public static int getIconSize() {
        return sIconSize;
    }

    public static synchronized void initConvertIconForUser(Resources res, int userId) {
        synchronized (OplusConvertIcon.class) {
            if (reloadThemeIcon(res, userId)) {
                mUserId = userId;
                OplusThirdPartUtil.setDefaultTheme(res, userId);
                IconBgType iconBgType = getIconBgTypeForUser(res, userId);
                initThemeParamForUser(res, NEW_IPHONE_STYLE_BG_NAME, IPHONE_STYLE_FG_NAME, IPHONE_STYLE_BG_NAME, userId);
                if (iconBgType == IconBgType.MASK) {
                    OplusMaskBitmapUtilities.getInstance().setMaskBitmap(getMaskBitmapForUser(res, NEW_IPHONE_STYLE_MASK_NAME, userId), sDetectMaskBorderOffset);
                }
                OplusMaskBitmapUtilities.getInstance().setCutAndScalePram(getIconSize(), getThemeParamScale());
            }
        }
    }

    private static boolean hasTransparentPixels(Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        }
        int transparentCount = 0;
        int pixels = 0;
        int xStep = (int) Math.ceil((bitmap.getWidth() * 1.0f) / 4.0f);
        int yStep = (int) Math.ceil((bitmap.getHeight() * 1.0f) / 4.0f);
        for (int i = xStep; i < bitmap.getWidth(); i += xStep) {
            try {
                for (int j = 1; j < 4; j++) {
                    if (Color.alpha(bitmap.getPixel(i, j)) < 220) {
                        pixels++;
                    }
                    if (j == 3 && pixels > 1) {
                        transparentCount++;
                    }
                }
                pixels = 0;
            } catch (IllegalArgumentException e) {
            }
        }
        for (int i2 = xStep; i2 < bitmap.getWidth(); i2 += xStep) {
            for (int j2 = bitmap.getHeight() - 2; j2 > bitmap.getHeight() - 4; j2--) {
                if (Color.alpha(bitmap.getPixel(i2, j2)) < 220) {
                    pixels++;
                }
                if (j2 == 3 && pixels > 1) {
                    transparentCount++;
                }
            }
            pixels = 0;
        }
        for (int i3 = yStep; i3 < bitmap.getHeight(); i3 += yStep) {
            for (int j3 = bitmap.getHeight() - 2; j3 > (bitmap.getHeight() - 4) - 1; j3--) {
                if (Color.alpha(bitmap.getPixel(j3, i3)) < 220) {
                    pixels++;
                }
                if (j3 == 3 && pixels > 1) {
                    transparentCount++;
                }
            }
            pixels = 0;
        }
        for (int i4 = yStep; i4 < bitmap.getHeight(); i4 += yStep) {
            for (int j4 = bitmap.getWidth() - 2; j4 > (bitmap.getWidth() - 4) - 1; j4--) {
                if (Color.alpha(bitmap.getPixel(j4, i4)) < 220) {
                    pixels++;
                }
                if (j4 == bitmap.getWidth() - 4 && pixels > 1) {
                    transparentCount++;
                }
            }
            pixels = 0;
        }
        return transparentCount >= sNoTransparentScalePixels * 2;
    }

    public static void initConvertIcon(Resources res) {
    }

    private static void setIconBgFgRes(String maskBg, String maskFg, String coverBg) {
        sMaskBackgroundPic = maskBg;
        sMaskForegroundPic = maskFg;
        sCoverBackgroundPic = coverBg;
        sIconBackground = null;
        sIconForeground = null;
    }

    private static void initIconSize(Resources res) {
        int width = 0;
        if (res != null) {
            int densityW = UxScreenUtil.getDefaultIconSize(res);
            width = float2int((densityW / res.getDisplayMetrics().density) * (DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0f));
        }
        if (width <= 0) {
            width = 168;
        }
        sIconSize = width;
        sIconWidth = width;
        sIconHeight = width;
        sConfigScale = 0.0f;
        sIconLeft = 0;
        sIconTop = 0;
        if (res == null) {
            return;
        }
        long themeFlags = 0;
        OplusBaseConfiguration baseConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, res.mResourcesExt.getConfiguration());
        if (baseConfig != null && baseConfig.getOplusExtraConfiguration() != null) {
            themeFlags = baseConfig.getOplusExtraConfiguration().mThemeChangedFlags;
        }
        int themeSize = (int) ((themeFlags >> 32) & 65535);
        if (themeSize > 0) {
            float sizeDp = themeSize / 100.0f;
            float result = (DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0f) * sizeDp;
            if (sThemeScale < 0.3f && result > 168.0f) {
                result += (int) (r1 * 32.0f);
            }
            sConfigScale = result / width;
            int offset = float2int((width - result) / 2.0f);
            sIconLeft = offset;
            sIconTop = offset;
            sNoTransparentScalePixels = computeTransparentPixels(res) / 2;
        }
    }

    private static int computeTransparentPixels(Resources resources) {
        int maxIconSize = resources.getDimensionPixelSize(201654414);
        int defaultIconSize = resources.getDimensionPixelSize(201654503);
        return maxIconSize - defaultIconSize;
    }

    private static Bitmap computeDesity(Bitmap bitmap, Resources resources) {
        if (resources == null) {
            return bitmap;
        }
        int dstDensity = resources.getDisplayMetrics().densityDpi;
        int density = bitmap.getDensity();
        float scale = 0.0f;
        if (density != 0) {
            scale = dstDensity / density;
        }
        bitmap.setDensity(dstDensity);
        if (scale <= 1.0f) {
            return bitmap;
        }
        int dstW = (int) ((bitmap.getWidth() * scale) + 0.5f);
        int dstH = (int) ((bitmap.getHeight() * scale) + 0.5f);
        return Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
    }

    private static boolean needSetDarkFilter(Resources res) {
        OplusExtraConfiguration extraConfig;
        boolean isDarkMode = (res.getConfiguration().uiMode & 48) == 32;
        if (isDarkMode) {
            Configuration config = res.getConfiguration();
            OplusBaseConfiguration baseConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, config);
            if (baseConfig != null && (extraConfig = baseConfig.getOplusExtraConfiguration()) != null) {
                Long uxiconConfig = Long.valueOf(extraConfig.mUxIconConfig);
                int darkModeIcon = Long.valueOf(uxiconConfig.longValue() >> 61).intValue() & 1;
                if (darkModeIcon == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void setDarkFilterToDrawable(Resources res, Drawable drawable) {
        if (needSetDarkFilter(res)) {
            if (sDarkModeColorFilter == null) {
                int color = getDarkModeColorInCurrentContrast(0.84f);
                sDarkModeColorFilter = new LightingColorFilter(color, 0);
            }
            drawable.setColorFilter(sDarkModeColorFilter);
            return;
        }
        drawable.setColorFilter(null);
    }

    private static void setDarkFilterToPaint(Resources res) {
        if (needSetDarkFilter(res)) {
            if (sDarkModeColorFilter == null) {
                int color = getDarkModeColorInCurrentContrast(0.84f);
                sDarkModeColorFilter = new LightingColorFilter(color, 0);
            }
            sPaint.setColorFilter(sDarkModeColorFilter);
            return;
        }
        sPaint.setColorFilter(null);
    }

    private static int getDarkModeColorInCurrentContrast(float currentContrast) {
        int currentColorR;
        if (currentContrast == -1.0f) {
            currentColorR = 214;
        } else {
            currentColorR = (int) (255.0f * currentContrast);
        }
        String colorR = Integer.toHexString(currentColorR);
        StringBuilder sb = new StringBuilder();
        sb.append(colorR).append(colorR).append(colorR);
        return Integer.parseInt(sb.toString(), 16);
    }

    private static int float2int(float f) {
        int i = (int) f;
        float offset = f - i;
        if (Math.abs(offset) > 0.5f) {
            return i + 1;
        }
        return i;
    }

    private static boolean reloadThemeIcon(Resources res, int userId) {
        if (res == null || res.mResourcesExt == null || res.mResourcesExt.getConfiguration() == null) {
            return false;
        }
        Configuration config = res.mResourcesExt.getConfiguration();
        OplusBaseConfiguration baseConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, config);
        if (baseConfig == null || !needUpdateThemeIcon(baseConfig.getOplusExtraConfiguration(), userId)) {
            return false;
        }
        return true;
    }

    private static boolean needUpdateThemeIcon(OplusExtraConfiguration configuration, int userId) {
        boolean z = false;
        if (configuration == null) {
            return false;
        }
        File icons = null;
        long flag = configuration.mThemeChangedFlags;
        if ((256 & flag) != 0) {
            if (TextUtils.isEmpty(configuration.mThemePrefix)) {
                icons = new File(OplusThemeUtil.CUSTOM_THEME_PATH, "icons");
            } else {
                icons = new File(configuration.mThemePrefix, "icons");
            }
        } else if ((flag & 1) != 0) {
            if (userId > 0) {
                icons = new File("/data/theme/" + userId, "icons");
            } else {
                icons = new File("/data/theme/", "icons");
            }
        }
        if (DEBUG_ENABLE) {
            Log.i(TAG, "theme icon change: " + flag + ", icons: " + (icons != null ? Long.valueOf(icons.lastModified()) : icons) + ", sLastModified: " + sLastModified + ", isDefaultTheme: " + OplusThirdPartUtil.mIsDefaultTheme);
        }
        if (icons != null && icons.exists() && icons.lastModified() != sLastModified) {
            sLastModified = icons.lastModified();
            return true;
        }
        if ((1 & flag) == 0) {
            z = true;
        }
        return z ^ OplusThirdPartUtil.mIsDefaultTheme;
    }
}
