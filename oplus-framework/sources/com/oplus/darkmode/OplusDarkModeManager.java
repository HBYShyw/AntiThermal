package com.oplus.darkmode;

import android.app.Application;
import android.app.OplusActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.HardwareRenderer;
import android.graphics.IBaseCanvasExt;
import android.graphics.NinePatch;
import android.graphics.OplusBaseHardwareRenderer;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RecordingCanvas;
import android.graphics.RectF;
import android.graphics.RenderNode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.SumEntity;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.view.IViewExt;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.android.internal.R;
import com.oplus.bluetooth.OplusBluetoothClass;
import com.oplus.os.WaveformEffect;
import com.oplus.util.OplusTypeCastingHelper;
import com.oplus.zoomwindow.OplusZoomWindowManager;

/* loaded from: classes.dex */
public class OplusDarkModeManager implements IOplusDarkModeManager {
    private static final String ATTRS_OPLUSOS_FORCE_DARK_CUSTOM = "enableFollowSystemForceDarkRank";
    private static final String ATTRS_OPLUSOS_SELECT_FORCE_DARK_TYPE = "selectSystemForceDarkType";
    private static final boolean DBG;
    private static final boolean DEBUG;
    private static final float DEFAULT_BACKGROUNDMAXL = 0.0f;
    private static final float DEFAULT_DIALOGBGMAXL = 27.0f;
    private static final float DEFAULT_FOREGROUNDMINL = 100.0f;
    private static final float GENTLE_BACKGROUNDMAXL = 19.0f;
    private static final float MIDDLE_BACKGROUNDMAXL = 9.0f;
    private static final String TAG = "OplusDarkModeManager";
    private static boolean mIsHidden;
    private static boolean mShouldInvalidWorld;
    private static int mSupportStyle;
    private static boolean mUseThirdPartyInvert;
    private OplusActivityManager mOplusActivityManager;
    private boolean mIsChangeSystemUiVisibility = false;
    private float mDialogBgMaxL = 27.0f;
    private float mBackgroundMaxL = 0.0f;
    private float mForegroundMinL = 100.0f;
    private boolean mIsOplusOSForceDarkCustom = false;
    private int mSelectForceDarkType = -1;

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        DBG = z;
        DEBUG = z | false;
        mIsHidden = false;
        mShouldInvalidWorld = false;
    }

    /* loaded from: classes.dex */
    private static class Holder {
        private static final OplusDarkModeManager INSTANCE = new OplusDarkModeManager();

        private Holder() {
        }
    }

    public static OplusDarkModeManager getInstance() {
        return Holder.INSTANCE;
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public boolean useForcePowerSave() {
        return false;
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public int changeSystemUiVisibility(int oldSystemUiVisibility) {
        if (mUseThirdPartyInvert) {
            if ((oldSystemUiVisibility & 8192) != 0) {
                this.mIsChangeSystemUiVisibility = true;
            }
            int vis = oldSystemUiVisibility & OplusZoomWindowManager.ACTION_MASK_ON_SHOWING_OF_MINI_ZOOM_MODE;
            return vis & (-17);
        }
        if (this.mIsChangeSystemUiVisibility) {
            this.mIsChangeSystemUiVisibility = false;
            int vis2 = oldSystemUiVisibility | 8192;
            return vis2 | 16;
        }
        return oldSystemUiVisibility;
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public boolean shouldInterceptConfigRelaunch(int diff, Configuration configuration) {
        OplusBaseConfiguration OplusBaseConfiguration;
        if ((268435456 & diff) == 0 || (OplusBaseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, configuration)) == null || OplusBaseConfiguration.getOplusExtraConfiguration().mOplusConfigType != 1) {
            return false;
        }
        boolean hasOtherDiff = ((-268435457) & diff) != 0;
        if (hasOtherDiff) {
            if (DEBUG) {
                Log.d(TAG, "shouldInterceptConfigRelaunch-->has dark mode rank change but also other diff-->diff:" + diff);
            }
            return false;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldInterceptConfigRelaunch-->success-->diff:" + diff);
        }
        return true;
    }

    private boolean setDarkStyleArgs(Configuration configuration) {
        OplusBaseConfiguration OplusBaseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, configuration);
        if (OplusBaseConfiguration != null) {
            this.mDialogBgMaxL = OplusBaseConfiguration.getOplusExtraConfiguration().mDarkModeDialogBgMaxL;
            this.mBackgroundMaxL = OplusBaseConfiguration.getOplusExtraConfiguration().mDarkModeBackgroundMaxL;
            float f = OplusBaseConfiguration.getOplusExtraConfiguration().mDarkModeForegroundMinL;
            this.mForegroundMinL = f;
            if (this.mDialogBgMaxL == -1.0f) {
                this.mDialogBgMaxL = 27.0f;
            }
            if (this.mBackgroundMaxL == -1.0f) {
                this.mBackgroundMaxL = 0.0f;
            }
            if (f == -1.0f) {
                this.mForegroundMinL = 100.0f;
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public boolean setDarkModeProgress(View decor, Configuration configuration) {
        if (decor == null) {
            return false;
        }
        boolean result = false;
        if (setDarkStyleArgs(configuration)) {
            ThreadedRenderer renderer = decor.getThreadedRenderer();
            if (this.mIsOplusOSForceDarkCustom) {
                result = setForceDarkArgs(renderer, this.mDialogBgMaxL, this.mBackgroundMaxL, this.mForegroundMinL);
                if (DEBUG) {
                    Log.d(TAG, "setDarkModeProgress-->" + decor.getContext().getPackageName() + "-->mIsOplusOSForceDarkCustom-->mDialogBgMaxL:" + this.mDialogBgMaxL + "-->mBackgroundMaxL:" + this.mBackgroundMaxL + ",mForegroundMinL:" + this.mForegroundMinL);
                }
            } else {
                result = setForceDarkArgs(renderer, 27.0f, 0.0f, 100.0f);
            }
            if (mUseThirdPartyInvert) {
                checkThirdInvertArgs();
                if (mShouldInvalidWorld) {
                    invalidateWorld(decor);
                    mShouldInvalidWorld = false;
                    if (DEBUG) {
                        Log.d(TAG, "setDarkModeProgress-->" + decor.getContext().getPackageName() + "-->checkThirdInvertArgs to invalidateWorld");
                    }
                }
            }
        }
        return result;
    }

    private void checkThirdInvertArgs() {
        float oldBg = OplusDarkModeThirdInvertManager.getInstance().getBackgroundMaxL();
        float oldFg = OplusDarkModeThirdInvertManager.getInstance().getForegroundMinL();
        switch (mSupportStyle) {
            case 2:
                this.mBackgroundMaxL = 0.0f;
                this.mForegroundMinL = 100.0f;
                break;
            case 3:
                this.mBackgroundMaxL = MIDDLE_BACKGROUNDMAXL;
                this.mForegroundMinL = 100.0f;
                break;
            case 4:
                this.mBackgroundMaxL = GENTLE_BACKGROUNDMAXL;
                this.mForegroundMinL = 100.0f;
                break;
        }
        OplusDarkModeThirdInvertManager.getInstance().setBackgroundMaxL(this.mBackgroundMaxL);
        OplusDarkModeThirdInvertManager.getInstance().setForegroundMinL(this.mForegroundMinL);
        if (oldBg != this.mBackgroundMaxL || oldFg != this.mForegroundMinL) {
            mShouldInvalidWorld = true;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void refreshForceDark(View view, OplusDarkModeData oplusDarkModeData) {
        if (view == null) {
            return;
        }
        int i = this.mSelectForceDarkType;
        if (i != -1) {
            changeUsageForceDarkAlgorithmType(view, i);
            if (DEBUG) {
                Log.d(TAG, "refreshForceDark-->" + view.getContext().getPackageName() + "-->changeUsageForceDarkAlgorithmType-->mSelectForceDarkType:" + this.mSelectForceDarkType);
            }
        }
        boolean z = mUseThirdPartyInvert;
        if (this.mIsOplusOSForceDarkCustom) {
            mUseThirdPartyInvert = false;
            OplusDarkModeThirdInvertManager.getInstance().setIsSupportDarkModeStatus(0);
        } else {
            mUseThirdPartyInvert = parseOpenByDarkModeData(view.getContext(), oplusDarkModeData);
            OplusDarkModeThirdInvertManager.getInstance().setIsSupportDarkModeStatus(mUseThirdPartyInvert ? 1 : 0);
            if (DEBUG) {
                Log.d(TAG, "refreshForceDark-->" + view.getContext().getPackageName() + "-->mUseThirdPartyInvert:" + mUseThirdPartyInvert);
            }
            if (mUseThirdPartyInvert) {
                setDarkStyleArgs(view.getContext().getResources().getConfiguration());
                checkThirdInvertArgs();
            }
        }
        byte b = z != mUseThirdPartyInvert;
        if (!mShouldInvalidWorld && b != false) {
            mShouldInvalidWorld = true;
        }
        if (mShouldInvalidWorld) {
            invalidateWorld(view);
            mShouldInvalidWorld = false;
            if (DEBUG) {
                Log.d(TAG, "refreshForceDark-->" + view.getContext().getPackageName() + "-->invalidateWorld");
            }
        }
    }

    private boolean setForceDarkArgs(HardwareRenderer renderer, float dialogBgMaxL, float backgroundMaxL, float foregroundMinL) {
        OplusBaseHardwareRenderer OplusBaseHardwareRenderer = (OplusBaseHardwareRenderer) OplusTypeCastingHelper.typeCasting(OplusBaseHardwareRenderer.class, renderer);
        if (OplusBaseHardwareRenderer != null) {
            return OplusBaseHardwareRenderer.setForceDarkArgs(dialogBgMaxL, backgroundMaxL, foregroundMinL);
        }
        return false;
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public boolean ensureWebSettingDarkMode(WebView webView) {
        IViewExt viewExt = webView.getViewWrapper().getViewExt();
        if (viewExt != null) {
            if (mUseThirdPartyInvert) {
                int originForceDark = viewExt.getOriginWebSettingForceDark();
                if (originForceDark == -1) {
                    int autoForceDark = webView.getSettings().getForceDark();
                    viewExt.setOriginWebSettingForceDark(autoForceDark);
                }
                webView.getSettings().setForceDark(2);
                if (DEBUG) {
                    Log.d(TAG, "ensureWebSettingDarkMode-->" + webView.getContext().getPackageName() + "-->ON");
                }
                return true;
            }
            int originForceDark2 = viewExt.getOriginWebSettingForceDark();
            if (originForceDark2 != -1) {
                webView.getSettings().setForceDark(viewExt.getOriginWebSettingForceDark());
                viewExt.setOriginWebSettingForceDark(-1);
                if (DEBUG) {
                    Log.d(TAG, "ensureWebSettingDarkMode-->" + webView.getContext().getPackageName() + "-->RESTORE");
                }
                return true;
            }
            return false;
        }
        return false;
    }

    void invalidateWorld(View view) {
        view.invalidate();
        if (view instanceof ViewGroup) {
            if (view instanceof WebView) {
                ensureWebSettingDarkMode((WebView) view);
            }
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                invalidateWorld(parent.getChildAt(i));
            }
        }
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void logForceDarkAllowedStatus(Context context, boolean forceDarkAllowedDefault) {
        if (DEBUG) {
            TypedArray a = context.obtainStyledAttributes(R.styleable.Theme);
            boolean isLightTheme = a.getBoolean(WaveformEffect.EFFECT_OTHER_COMPATIBLE_2, true);
            boolean forceDarkAllowed = a.getBoolean(WaveformEffect.EFFECT_OTHER_COMPATIBLE_1, forceDarkAllowedDefault);
            boolean useAutoDark = isLightTheme && forceDarkAllowed;
            Log.d(TAG, "logForceDarkAllowedStatus-->" + context.getPackageName() + ",isLightTheme:" + isLightTheme + ",forceDarkAllowed:" + forceDarkAllowed + ",useAutoDark:" + useAutoDark);
            a.recycle();
        }
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void logConfigurationNightError(Context context, boolean isNightConfiguration) {
        if (isNightConfiguration) {
            Resources resources = context.getResources();
            String packageName = context.getPackageName();
            int oplusOSForceDarkCustomId = resources.getIdentifier(ATTRS_OPLUSOS_FORCE_DARK_CUSTOM, "attr", packageName);
            int oplusOSForceDarkCustomType = resources.getIdentifier(ATTRS_OPLUSOS_SELECT_FORCE_DARK_TYPE, "attr", packageName);
            int[] idAttr = {oplusOSForceDarkCustomId, oplusOSForceDarkCustomType};
            try {
                TypedArray attributes = context.getTheme().obtainStyledAttributes(idAttr);
                this.mIsOplusOSForceDarkCustom = attributes.getBoolean(0, false);
                this.mSelectForceDarkType = attributes.getInt(1, -1);
                attributes.recycle();
            } catch (NumberFormatException e) {
                Log.e(TAG, "logConfigurationNightError: error = " + e.getMessage());
            }
            if (DEBUG) {
                Log.d(TAG, "packageName:" + packageName + ",mIsOplusOSForceDarkCustom:" + this.mIsOplusOSForceDarkCustom);
            }
        }
    }

    private static boolean isSystemApp(Context context) {
        return (context.getApplicationInfo().flags & 1) > 0;
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void forceDarkWithoutTheme(Context context, View view, boolean z) {
        if (context == null) {
            if (DEBUG) {
                Log.d(TAG, "forceDarkWithoutTheme-->context is null!");
                return;
            }
            return;
        }
        if (this.mIsOplusOSForceDarkCustom) {
            return;
        }
        if (z) {
            mUseThirdPartyInvert = false;
            OplusDarkModeThirdInvertManager.getInstance().setIsSupportDarkModeStatus(0);
            if (DEBUG) {
                Log.d(TAG, "forceDarkWithoutTheme-->" + context.getPackageName() + "-->app use owner force dark!");
                return;
            }
            return;
        }
        boolean z2 = mUseThirdPartyInvert;
        mUseThirdPartyInvert = shouldUseColorForceDark(context, true);
        OplusDarkModeThirdInvertManager.getInstance().setIsSupportDarkModeStatus(mUseThirdPartyInvert ? 1 : 0);
        boolean z3 = DEBUG;
        if (z3) {
            Log.d(TAG, "forceDarkWithoutTheme-->" + context.getPackageName() + "-->mUseThirdPartyInvert:" + mUseThirdPartyInvert);
        }
        if (!mShouldInvalidWorld) {
            mShouldInvalidWorld = z2 != mUseThirdPartyInvert;
        }
        if (mShouldInvalidWorld) {
            invalidateWorld(view);
            mShouldInvalidWorld = false;
            if (z3) {
                Log.d(TAG, "forceDarkWithoutTheme-->" + context.getPackageName() + "-->invalidateWorld");
            }
        }
    }

    public boolean shouldUseColorForceDark(Context context, boolean useCache) {
        if (context == null || isSystemApp(context)) {
            return false;
        }
        return parseOpenByDarkModeData(context, getDarkModeData(context, context.getPackageName(), useCache));
    }

    private boolean parseOpenByDarkModeData(Context context, OplusDarkModeData oplusDarkModeData) {
        if (oplusDarkModeData == null || context == null) {
            return false;
        }
        boolean alreadyClickByUser = oplusDarkModeData.mAlreadyClickByUser;
        boolean isOpen = oplusDarkModeData.mOpenByUser;
        long versionCode = context.getApplicationInfo().longVersionCode;
        boolean isNewVersion = oplusDarkModeData.mVersionCode == -1 || versionCode >= oplusDarkModeData.mVersionCode;
        boolean oldIsHidden = mIsHidden;
        int type = isNewVersion ? oplusDarkModeData.mCurType : oplusDarkModeData.mOldType;
        int oldSupportStyle = mSupportStyle;
        switch (type) {
            case 1:
                mSupportStyle = 0;
                mIsHidden = true;
                isOpen = false;
                break;
            case 2:
            case 3:
            case 4:
                mSupportStyle = type;
                mIsHidden = false;
                break;
            default:
                mSupportStyle = 0;
                mIsHidden = false;
                break;
        }
        if (!mShouldInvalidWorld && (oldSupportStyle != mSupportStyle || oldIsHidden != mIsHidden)) {
            mShouldInvalidWorld = true;
        }
        if (!mIsHidden && !isOpen && !alreadyClickByUser) {
            if (oplusDarkModeData.mIsRecommend == 3) {
                isOpen = true;
            } else if (oplusDarkModeData.mIsRecommend == 1) {
                isOpen = isNewVersion;
            } else if (oplusDarkModeData.mIsRecommend == 2) {
                isOpen = isNewVersion ? false : true;
            }
        }
        if (DEBUG) {
            Log.d(TAG, "parseOpenByDarkModeData-->" + context.getPackageName() + "-->ver:" + oplusDarkModeData.mVersionCode + "-->isRec:" + oplusDarkModeData.mIsRecommend + "-->oldType:" + oplusDarkModeData.mOldType + "-->curType:" + oplusDarkModeData.mCurType + "-->mAlreadyClickByUser:" + oplusDarkModeData.mAlreadyClickByUser + "-->openByUser:" + oplusDarkModeData.mOpenByUser + "-->open:" + isOpen);
        }
        return isOpen;
    }

    public OplusDarkModeData getDarkModeData(Context context, String packageName, boolean useCache) {
        if (packageName == null) {
            return null;
        }
        if (this.mOplusActivityManager == null) {
            this.mOplusActivityManager = new OplusActivityManager();
        }
        try {
            OplusDarkModeData oplusDarkModeData = this.mOplusActivityManager.getDarkModeData(packageName);
            return oplusDarkModeData;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void changeUsageForceDarkAlgorithmType(View view, int type) {
        IViewExt viewExt;
        if (view != null && (viewExt = view.getViewWrapper().getViewExt()) != null) {
            viewExt.setUsageForceDarkAlgorithmType(type);
        }
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public IOplusDarkModeManager newOplusDarkModeManager() {
        return new OplusDarkModeManager();
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public boolean darkenSplitScreenDrawable(View decorView, Drawable resizingDrawable, int left, int top, int right, int bottom, RecordingCanvas canvas, RenderNode renderNode) {
        Configuration configuration = decorView.getResources().getConfiguration();
        if ((configuration.uiMode & 48) == 32) {
            renderNode.setForceDarkAllowed(false);
            if (resizingDrawable instanceof StateListDrawable) {
                resizingDrawable = resizingDrawable.getCurrent();
            }
            if ((resizingDrawable instanceof ColorDrawable) && resizingDrawable.getAlpha() != 0) {
                ColorDrawable colorDrawable = new ColorDrawable(OplusBluetoothClass.Device.UNKNOWN);
                colorDrawable.setBounds(left, top, right, bottom);
                colorDrawable.draw(canvas);
                return false;
            }
            if ((resizingDrawable instanceof NinePatchDrawable) && resizingDrawable.getAlpha() != 0) {
                resizingDrawable.setTint(OplusBluetoothClass.Device.UNKNOWN);
                resizingDrawable.setBounds(left, top, right, bottom);
                resizingDrawable.draw(canvas);
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void initDarkModeStatus(Application application) {
        OplusDarkModeThirdInvertManager.attachApplication(application);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void changeColorFilterInDarkMode(ColorFilter colorFilter) {
        OplusDarkModeThirdInvertManager.getInstance().changeColorFilterInDarkMode(colorFilter);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public boolean isInDarkMode(boolean isHardware) {
        return OplusDarkModeThirdInvertManager.getInstance().isInDarkMode(isHardware);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public IBaseCanvasExt.RealPaintState getRealPaintState(Paint paint) {
        return OplusDarkModeThirdInvertManager.getInstance().getRealPaintState(paint);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void changePaintWhenDrawText(Paint paint, IBaseCanvasExt canvas) {
        OplusDarkModeThirdInvertManager.getInstance().changePaintWhenDrawText(paint, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void resetRealPaintIfNeed(Paint paint, IBaseCanvasExt.RealPaintState realPaintState) {
        OplusDarkModeThirdInvertManager.getInstance().resetRealPaintIfNeed(paint, realPaintState);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void changePaintWhenDrawArea(Paint paint, RectF rectF, IBaseCanvasExt canvas) {
        OplusDarkModeThirdInvertManager.getInstance().changePaintWhenDrawArea(paint, rectF, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void changePaintWhenDrawArea(Paint paint, RectF rectF, Path path, IBaseCanvasExt canvas) {
        OplusDarkModeThirdInvertManager.getInstance().changePaintWhenDrawArea(paint, rectF, path, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void changePaintWhenDrawPatch(NinePatch patch, Paint paint, RectF rectF, IBaseCanvasExt canvas) {
        OplusDarkModeThirdInvertManager.getInstance().changePaintWhenDrawPatch(patch, paint, rectF, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public int changeWhenDrawColor(int color, boolean isDarkMode, IBaseCanvasExt canvas) {
        return OplusDarkModeThirdInvertManager.getInstance().changeWhenDrawColor(color, isDarkMode, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void changePaintWhenDrawBitmap(Paint paint, Bitmap bitmap, RectF rectF, IBaseCanvasExt canvas) {
        OplusDarkModeThirdInvertManager.getInstance().changePaintWhenDrawBitmap(paint, bitmap, rectF, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public int[] getDarkModeColors(int[] colors, IBaseCanvasExt canvas) {
        return OplusDarkModeThirdInvertManager.getInstance().getDarkModeColors(colors, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public Paint getPaintWhenDrawPatch(NinePatch patch, Paint paint, RectF rectF, IBaseCanvasExt canvas) {
        return OplusDarkModeThirdInvertManager.getInstance().changePaintWhenDrawPatch(patch, paint, rectF, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public Paint getPaintWhenDrawBitmap(Paint paint, Bitmap bitmap, RectF rectF, IBaseCanvasExt canvas) {
        return OplusDarkModeThirdInvertManager.getInstance().changePaintWhenDrawBitmap(paint, bitmap, rectF, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void markDispatchDraw(ViewGroup viewGroup, Canvas canvas) {
        OplusDarkModeThirdInvertManager.getInstance().markDispatchDraw(viewGroup, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void markDrawChild(ViewGroup viewGroup, View view, Canvas canvas) {
        OplusDarkModeThirdInvertManager.getInstance().markDrawChild(viewGroup, view, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void markBackground(View view, Canvas canvas) {
        OplusDarkModeThirdInvertManager.getInstance().markBackground(view, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void markForeground(View view, Canvas canvas) {
        OplusDarkModeThirdInvertManager.getInstance().markForeground(view, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void markOnDraw(View view, Canvas canvas) {
        OplusDarkModeThirdInvertManager.getInstance().markOnDraw(view, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void markDrawFadingEdge(View view, Canvas canvas) {
        OplusDarkModeThirdInvertManager.getInstance().markDrawFadingEdge(view, canvas);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public void markViewTypeBySize(View view) {
        OplusDarkModeThirdInvertManager.getInstance().markViewTypeBySize(view);
    }

    @Override // com.oplus.darkmode.IOplusDarkModeManager
    public ColorFilter getColorFilterWhenDrawVectorDrawable(SumEntity hEntity, SumEntity sEntity, SumEntity lEntity) {
        return OplusDarkModeThirdInvertManager.getInstance().getColorFilterWhenDrawVectorDrawable(hEntity, sEntity, lEntity);
    }
}
