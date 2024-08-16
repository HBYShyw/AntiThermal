package android.inputmethodservice;

import android.R;
import android.app.Dialog;
import android.common.OplusFeatureCache;
import android.content.Context;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.graphics.Region;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.WindowInsets;
import android.view.inputmethod.EditorInfo;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.font.IOplusFontManager;
import com.oplus.util.OplusInputMethodUtil;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class InputMethodServiceExtImpl implements IInputMethodServiceExt {
    private static final String ACTION_DISPLAY = "synergy.display";
    private static final int KEYBOARD_PREVENT_TOUCH_DEFAULT = -1;
    private static final int KEYBOARD_PREVENT_TOUCH_ON = 1;
    private static final String KEY_DISPLAY_ID = "displayId";
    private static final String KEY_HIDE_NAVIGATIONBAR_ENABLE = "hide_navigationbar_enable";
    private static final int SYSTEM_FOLDING_MODE_CLOSE = 0;
    private static final String SYSTEM_FOLDING_MODE_KEYS = "oplus_system_folding_mode";
    private static final int SYSTEM_FOLDING_MODE_OPEN = 1;
    private static final String TAG = "InputMethodServiceExtImpl";
    private static final int VALUE_KEYBOARD_POSITION_RAISE = 1;
    private static final int VALUE_KEYBOARD_QUICK_SWITCH_OPEN = 1;
    private static final int VALUE_NAVIGATIONBAR_GESTURE = 2;
    private static final int VALUE_NAVIGATIONBAR_GESTURE_SIDE = 3;
    private static final int VALUE_SWIPE_SIDE_GESTURE_BAR_TYPE_HIDE = 1;
    private static final int VALUE_SWIPE_SIDE_GESTURE_BAR_TYPE_SUSPEND = 0;
    private int mContentHeight;
    private int mContentTopInsets;
    private Context mContext;
    private int mCorrectTopInsets;
    private boolean mDefaultDisplay;
    private int mFoldingMode;
    private int mFullNavHeight;
    private int mHalfNavHeight;
    private boolean mHasShownOnce;
    private int mHideFlags;
    private OplusInputMethodCompatUtils mInputMethodCompatUtils;
    private final InputMethodService mInputMethodService;
    private InsetsState mInsetsState;
    private boolean mIsAndroidPackage;
    private boolean mIsExpRom;
    private int mKeyboardPosition;
    private int mKeyboardPreventTouch;
    private int mKeyboardQuickSwitch;
    private boolean mKeyboardRaise;
    private EditorInfo mLastEditorInfo;
    private List<Rect> mLastExclusionRects;
    private int mLastOrientation;
    private int mMaxFloatingHeight;
    private int mMinFloatingHeight;
    private boolean mNavBarGesture;
    private boolean mNavBarHidden;
    private int mNavHeight;
    private InsetsSource mNavInsetSource;
    private WeakReference<View> mNavigationBarFrame;
    private OplusInputMethodServiceInternal mServiceInternal;
    private boolean mSupportFloating;
    private static final String KEYBOARD_PREVENT_TOUCH = "keyboard_prevent_touch";
    private static final Uri URI_KEYBOARD_PREVENT_TOUCH = Settings.Secure.getUriFor(KEYBOARD_PREVENT_TOUCH);
    private static final Uri URI_SYSTEM_FOLDING_MODE_KEYS = Settings.Global.getUriFor("oplus_system_folding_mode");
    private static final String KEY_KEYBOARD_POSITION = "keyboard_position";
    private static final Uri URI_KEYBOARD_POSITION = Settings.Secure.getUriFor(KEY_KEYBOARD_POSITION);
    private static final String KEY_KEYBOARD_QUICK_SWITCH = "keyboard_quick_switch";
    private static final Uri URI_KEYBOARD_QUICK_SWITCH = Settings.Secure.getUriFor(KEY_KEYBOARD_QUICK_SWITCH);
    private static final Uri URI_HIDE_NAVIGATIONBAR_ENABLE = Settings.Secure.getUriFor("hide_navigationbar_enable");
    private static final String KEY_SWIPE_SIDE_GESTURE_BAR_TYPE = "gesture_side_hide_bar_prevention_enable";
    private static final Uri URI_SWIPE_SIDE_GESTURE_BAR_TYPE = Settings.Secure.getUriFor(KEY_SWIPE_SIDE_GESTURE_BAR_TYPE);
    private int[] mTempLocation = new int[2];
    private StringBuilder mTempString = new StringBuilder();
    private KeyboardMode mKeyboardMode = KeyboardMode.FLOATING;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum KeyboardMode {
        NORMAL,
        FLOATING,
        FULLSCREEN;

        boolean isFloating() {
            return this == FLOATING;
        }
    }

    public InputMethodServiceExtImpl(Object base) {
        InputMethodService inputMethodService = (InputMethodService) base;
        this.mInputMethodService = inputMethodService;
        OplusInputMethodServiceInternal oplusInputMethodServiceInternal = OplusInputMethodServiceInternal.getInstance();
        this.mServiceInternal = oplusInputMethodServiceInternal;
        oplusInputMethodServiceInternal.init(inputMethodService, this);
        updateDebugToClass();
    }

    private void updateDebugToClass() {
        OplusInputMethodUtil.updateDebugToClass(InputMethodService.class);
    }

    public void onInsetsComputed(InputMethodService.Insets insets) {
        onConfigurationChanged(this.mContext.getResources().getConfiguration());
        this.mTempString.setLength(0);
        this.mTempString.append("onInsetsComputed insets: ").append(insets.contentTopInsets).append(" ").append(insets.visibleTopInsets).append(" ").append(insets.touchableInsets).append(" ").append(insets.touchableRegion);
        View decorView = this.mInputMethodService.getWindow().getWindow().getDecorView();
        decorView.getLocationOnScreen(this.mTempLocation);
        int decorBottom = this.mTempLocation[1] + decorView.getHeight();
        this.mTempString.append(", decorView: ").append(Arrays.toString(this.mTempLocation)).append(" ").append(decorView.getWidth()).append("-").append(decorView.getHeight());
        View contentView = decorView.findViewById(R.id.content);
        contentView.getLocationInWindow(this.mTempLocation);
        int contentBottom = this.mTempLocation[1] + contentView.getHeight();
        this.mTempString.append(", contentView: ").append(Arrays.toString(this.mTempLocation)).append(" ").append(contentView.getWidth()).append("-").append(contentView.getHeight());
        View inputFrame = this.mInputMethodService.mInputFrame;
        if (inputFrame.getVisibility() == 0) {
            inputFrame.getLocationInWindow(this.mTempLocation);
            this.mTempString.append(", inputFrame: ").append(Arrays.toString(this.mTempLocation)).append(" ").append(inputFrame.getWidth()).append("-").append(inputFrame.getHeight());
        }
        boolean isShowing = this.mInputMethodService.getWindow().isShowing();
        boolean isExtractShown = this.mInputMethodService.isExtractViewShown();
        this.mHasShownOnce |= isShowing;
        this.mContentHeight = contentBottom - insets.contentTopInsets;
        this.mTempString.append("\nonInsetsComputed isShowing = ").append(isShowing).append(", isExtractShown = ").append(isExtractShown).append(", mSupportFloating = ").append(this.mSupportFloating).append(", mContentHeight = ").append(this.mContentHeight).append("(").append(this.mMinFloatingHeight).append("-").append(this.mMaxFloatingHeight).append(")");
        KeyboardMode keyboardMode = KeyboardMode.NORMAL;
        if (!isShowing) {
            keyboardMode = this.mKeyboardMode;
        } else if (isExtractShown) {
            keyboardMode = KeyboardMode.FULLSCREEN;
        } else if (this.mSupportFloating && this.mDefaultDisplay && this.mContentHeight < this.mMaxFloatingHeight) {
            Region region = calculateTouchableRegion(insets, decorView, inputFrame);
            this.mTempString.append(", region = ").append(region);
            if (this.mContentHeight <= this.mMinFloatingHeight || region.isEmpty() || region.getBounds().width() < decorView.getWidth() || region.getBounds().height() >= this.mMaxFloatingHeight) {
                keyboardMode = KeyboardMode.FLOATING;
            }
        }
        this.mTempString.append(", mKeyboardMode = ").append(this.mKeyboardMode).append("->").append(keyboardMode);
        boolean keyboardModeChanged = this.mKeyboardMode != keyboardMode;
        if (keyboardModeChanged) {
            this.mKeyboardMode = keyboardMode;
            updateNavHeight(isShowing);
        }
        View navigationBarBackground = decorView.findViewById(R.id.navigationBarBackground);
        if (navigationBarBackground != null) {
            navigationBarBackground.setAlpha(this.mKeyboardMode.isFloating() ? 0.0f : 1.0f);
        }
        int correctTopInsets = insets.contentTopInsets;
        boolean decorFitsSystemWindows = this.mInputMethodService.getWindow().getWindow().decorFitsSystemWindows();
        if (!this.mHasShownOnce) {
            correctTopInsets = decorBottom;
        } else if (this.mNavBarGesture) {
            if (isExtractShown) {
                correctTopInsets = decorView.getHeight();
            } else if (this.mKeyboardMode.isFloating()) {
                correctTopInsets = decorView.getHeight() - this.mNavHeight;
            } else if (decorFitsSystemWindows) {
                correctTopInsets = (decorView.getHeight() - this.mNavHeight) - this.mContentHeight;
            }
        }
        this.mTempString.append("\nonInsetsComputed").append(" mHasShownOnce = ").append(this.mHasShownOnce).append(", mNavBarGesture = ").append(this.mNavBarGesture).append(", decorFitsSystemWindows = ").append(decorFitsSystemWindows).append(", correctTopInsets = ").append(this.mCorrectTopInsets).append("->").append(correctTopInsets);
        if (keyboardModeChanged || this.mContentTopInsets != insets.contentTopInsets || this.mCorrectTopInsets != correctTopInsets) {
            OplusInputMethodUtil.logDebugIme(TAG, this.mTempString.toString());
        }
        this.mContentTopInsets = insets.contentTopInsets;
        this.mCorrectTopInsets = correctTopInsets;
        insets.contentTopInsets = correctTopInsets;
    }

    public void hideWindowImmediately(int flags, Dialog dialog) {
        this.mHideFlags = flags;
        if (flags > 0 && dialog != null) {
            OplusInputMethodUtil.logDebugIme(TAG, "hideWindowImmediately: " + flags);
            if ((flags & 256) > 0 || (flags & 4096) > 0) {
                dialog.hide();
            }
        }
    }

    public void hookOnColorChange(Uri uri) {
        if (URI_KEYBOARD_POSITION.equals(uri)) {
            updateKeyboardPosition();
            updateKeyboardRaise();
            return;
        }
        if (URI_KEYBOARD_QUICK_SWITCH.equals(uri)) {
            updateQuickSwitch();
            return;
        }
        if (URI_KEYBOARD_PREVENT_TOUCH.equals(uri)) {
            updatePreventTouch();
            return;
        }
        if (URI_SYSTEM_FOLDING_MODE_KEYS.equals(uri)) {
            updateFoldingMode();
            updateKeyboardRaise();
            attachInfoToEditorInfo(this.mInputMethodService.mInputEditorInfo);
        } else if (URI_HIDE_NAVIGATIONBAR_ENABLE.equals(uri)) {
            updateNavBarGesture();
            updateKeyboardRaise();
        } else if (URI_SWIPE_SIDE_GESTURE_BAR_TYPE.equals(uri)) {
            updateNavBarGesture();
            updateNavHeight(true);
        }
    }

    public void hookOnCreate(ContentObserver settingObserver, Context context) {
        this.mContext = context;
        ((IOplusFontManager) OplusFeatureCache.getOrCreate(IOplusFontManager.DEFAULT, new Object[0])).setIMEFlag(true);
        this.mIsExpRom = !OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_INPUTMETHOD_CN);
        this.mDefaultDisplay = context.getDisplayId() == 0;
        this.mLastOrientation = context.getResources().getConfiguration().orientation;
        OplusInputMethodCompatUtils oplusInputMethodCompatUtils = new OplusInputMethodCompatUtils(context);
        this.mInputMethodCompatUtils = oplusInputMethodCompatUtils;
        this.mHalfNavHeight = oplusInputMethodCompatUtils.getHalfNavHeight();
        this.mFullNavHeight = this.mInputMethodCompatUtils.getFullNavHeight();
        this.mSupportFloating = this.mInputMethodCompatUtils.supportFloating();
        this.mMinFloatingHeight = this.mInputMethodCompatUtils.getMinFloatingHeight();
        this.mMaxFloatingHeight = this.mInputMethodCompatUtils.getMaxFloatingHeight();
        this.mIsAndroidPackage = this.mInputMethodCompatUtils.isAndroidPackage();
        registerContentObserver(URI_KEYBOARD_POSITION, settingObserver);
        updateKeyboardPosition();
        registerContentObserver(URI_KEYBOARD_QUICK_SWITCH, settingObserver);
        updateQuickSwitch();
        registerContentObserver(URI_KEYBOARD_PREVENT_TOUCH, settingObserver);
        updateFoldingMode();
        registerContentObserver(URI_SYSTEM_FOLDING_MODE_KEYS, settingObserver);
        updatePreventTouch();
        registerContentObserver(URI_HIDE_NAVIGATIONBAR_ENABLE, settingObserver);
        registerContentObserver(URI_SWIPE_SIDE_GESTURE_BAR_TYPE, settingObserver);
        updateNavBarGesture();
        updateKeyboardRaise();
    }

    public void hookOnDestroy() {
        this.mServiceInternal.destroy();
    }

    public void configDebug(String[] args) {
        if (OplusInputMethodUtil.dynamicallyConfigDebugByDumpArgs(args, "ims")) {
            updateDebugToClass();
        }
    }

    public void updateExclusionRects(List<Rect> exclusionRects) {
        boolean shouldPreventTouch;
        if (exclusionRects != null && exclusionRects.size() > 0) {
            int i = this.mKeyboardPreventTouch;
            if (i == -1) {
                shouldPreventTouch = this.mIsExpRom;
            } else {
                shouldPreventTouch = i == 1;
            }
            if (this.mNavBarGesture) {
                View rootView = this.mInputMethodService.mInputFrame.getRootView();
                Rect rect = exclusionRects.get(0);
                exclusionRects.clear();
                if (shouldPreventTouch) {
                    exclusionRects.add(new Rect(0, rect.top, this.mInputMethodCompatUtils.getMaxSideGestureWidth(), rootView.getHeight() - this.mInputMethodCompatUtils.getBottomGestureHeight()));
                    exclusionRects.add(new Rect(rootView.getWidth() - this.mInputMethodCompatUtils.getMaxSideGestureWidth(), rect.top, rootView.getWidth(), rootView.getHeight() - this.mInputMethodCompatUtils.getBottomGestureHeight()));
                } else {
                    exclusionRects.add(new Rect(this.mInputMethodCompatUtils.getSideGestureWidth(), rect.top, this.mInputMethodCompatUtils.getMaxSideGestureWidth(), rootView.getHeight() - this.mInputMethodCompatUtils.getBottomGestureHeight()));
                    exclusionRects.add(new Rect(rootView.getWidth() - this.mInputMethodCompatUtils.getMaxSideGestureWidth(), rect.top, rootView.getWidth() - this.mInputMethodCompatUtils.getSideGestureWidth(), rootView.getHeight() - this.mInputMethodCompatUtils.getBottomGestureHeight()));
                }
            }
            if (!exclusionRects.equals(this.mLastExclusionRects)) {
                this.mLastExclusionRects = exclusionRects;
                OplusInputMethodUtil.logDebugIme(TAG, "updateExclusionRects " + this.mLastExclusionRects);
            }
        }
    }

    public boolean isFoldDisplayOpen() {
        return this.mFoldingMode == 1;
    }

    public void onEditorInfoUpdate(EditorInfo editorInfo) {
        if (editorInfo != null) {
            if (editorInfo.extras == null) {
                editorInfo.extras = new Bundle();
            }
            EditorInfo editorInfo2 = this.mLastEditorInfo;
            if (editorInfo2 != null && editorInfo2.extras != null && !editorInfo.extras.containsKey(OplusInputMethodUtil.KEY_INPUT_METHOD_SCENES)) {
                int lastScenes = this.mLastEditorInfo.extras.getInt(OplusInputMethodUtil.KEY_INPUT_METHOD_SCENES);
                editorInfo.extras.putInt(OplusInputMethodUtil.KEY_INPUT_METHOD_SCENES, lastScenes);
                OplusInputMethodUtil.logDebug(TAG, "onEditorInfoUpdate restore scenes = " + lastScenes);
            }
        }
        this.mLastEditorInfo = editorInfo;
        if (OplusInputMethodUtil.isDebug() && editorInfo != null) {
            if ((editorInfo.imeOptions & 33554432) != 0) {
                OplusInputMethodUtil.logDebug(TAG, "onEditorInfoUpdate attribute contains IME_FLAG_NO_FULLSCREEN");
            }
            if ((editorInfo.imeOptions & 268435456) != 0) {
                OplusInputMethodUtil.logDebug(TAG, "onEditorInfoUpdate attribute contains IME_FLAG_NO_EXTRACT_UI");
            }
            if ((editorInfo.internalImeOptions & 1) != 0) {
                OplusInputMethodUtil.logDebug(TAG, "onEditorInfoUpdate attribute contains IME_INTERNAL_FLAG_APP_WINDOW_PORTRAIT");
            }
            int scenes = editorInfo.extras.getInt(OplusInputMethodUtil.KEY_INPUT_METHOD_SCENES);
            OplusInputMethodUtil.logDebug(TAG, "onEditorInfoUpdate scenes = " + scenes);
        }
    }

    public void logDebug(String msg) {
        OplusInputMethodUtil.logDebug(TAG, msg);
    }

    public void logDebugIme(String msg) {
        OplusInputMethodUtil.logDebugIme(TAG, msg);
    }

    public void logMethodCallers(String msg) {
        OplusInputMethodUtil.logMethodCallers(TAG, msg);
    }

    public void updateNavigationBarOnShown(View navigationBarFrame) {
        if (this.mNavBarGesture && navigationBarFrame != null) {
            OplusInputMethodUtil.logDebugIme(TAG, "updateNavigationBarOnShown mNavHeight:" + this.mNavHeight + " mHalfNavHeight:" + this.mHalfNavHeight);
            navigationBarFrame.setVisibility(this.mNavHeight > this.mHalfNavHeight ? 0 : 4);
            WeakReference<View> weakReference = this.mNavigationBarFrame;
            if (weakReference == null || weakReference.get() != navigationBarFrame) {
                this.mNavigationBarFrame = new WeakReference<>(navigationBarFrame);
            }
        }
    }

    public int updateNavButtonFlags(int navButtonFlags) {
        if (this.mKeyboardQuickSwitch != 1 && (navButtonFlags & 2) != 0) {
            OplusInputMethodUtil.logDebugIme(TAG, "updateNavButtonFlags: hide switcher, mKeyboardQuickSwitch = " + this.mKeyboardQuickSwitch);
            return navButtonFlags & (-3);
        }
        return navButtonFlags;
    }

    public void updateExtractViewStyle(View extractFrame) {
        try {
            OplusExtractViewHelper.updateExtractViewStyle(extractFrame);
        } catch (Exception e) {
            OplusInputMethodUtil.logException(TAG, "updateExtractViewStyle", e);
        }
    }

    public void appPrivateCommand(String action, Bundle data) {
        if (ACTION_DISPLAY.equals(action)) {
            int displayId = data.getInt(KEY_DISPLAY_ID);
            this.mInputMethodService.updateDisplay(displayId);
            OplusInputMethodUtil.logDebug(TAG, "updateDisplay " + displayId);
        }
    }

    public boolean shouldIgnoreApplyImeVisibility(boolean setVisible) {
        int flags = this.mHideFlags;
        this.mHideFlags = 0;
        return !setVisible && (flags & 4096) > 0;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation != this.mLastOrientation) {
            this.mLastOrientation = newConfig.orientation;
            OplusInputMethodUtil.logDebug(TAG, "onConfigurationChanged: mLastOrientation = " + this.mLastOrientation);
            updateKeyboardRaise();
        }
    }

    public boolean shouldIgnoreFullscreenMode() {
        return (this.mIsAndroidPackage || OplusInputMethodUtil.isRemapDisplayDisabled() || !isFoldDisplayOpen()) ? false : true;
    }

    private void attachInfoToEditorInfo(EditorInfo editorInfo) {
        int flags;
        if (editorInfo != null && editorInfo.extras != null) {
            int flags2 = editorInfo.extras.getInt(OplusInputMethodUtil.KEY_INPUT_METHOD_SCENES);
            if (this.mFoldingMode == 1) {
                flags = flags2 | 16;
            } else {
                flags = flags2 & (-17);
            }
            OplusInputMethodUtil.logDebug(TAG, "attachInfoToEditorInfo: folding mode = " + this.mFoldingMode);
            editorInfo.extras.putInt(OplusInputMethodUtil.KEY_INPUT_METHOD_SCENES, flags);
        }
    }

    private void registerContentObserver(Uri uri, ContentObserver contentObserver) {
        this.mContext.getContentResolver().registerContentObserver(uri, false, contentObserver);
    }

    private void updateKeyboardPosition() {
        this.mKeyboardPosition = Settings.Secure.getInt(this.mContext.getContentResolver(), KEY_KEYBOARD_POSITION, 1);
        OplusInputMethodUtil.logDebug(TAG, "mKeyboardPosition: " + this.mKeyboardPosition);
    }

    private void updateQuickSwitch() {
        this.mKeyboardQuickSwitch = Settings.Secure.getInt(this.mContext.getContentResolver(), KEY_KEYBOARD_QUICK_SWITCH, 1);
        OplusInputMethodUtil.logDebug(TAG, "mKeyboardQuickSwitch: " + this.mKeyboardQuickSwitch);
    }

    private void updateFoldingMode() {
        this.mFoldingMode = Settings.Global.getInt(this.mInputMethodService.getContentResolver(), "oplus_system_folding_mode", 0);
        OplusInputMethodUtil.logDebug(TAG, "mFoldingMode: " + this.mFoldingMode);
    }

    private void updatePreventTouch() {
        this.mKeyboardPreventTouch = Settings.Secure.getInt(this.mInputMethodService.getContentResolver(), KEYBOARD_PREVENT_TOUCH, -1);
        OplusInputMethodUtil.logDebug(TAG, "mKeyboardPreventTouch: " + this.mKeyboardPreventTouch);
    }

    private void updateNavBarGesture() {
        int navigationBarState = Settings.Secure.getInt(this.mInputMethodService.getContentResolver(), "hide_navigationbar_enable", 0);
        int sideGestureHideState = Settings.Secure.getInt(this.mInputMethodService.getContentResolver(), KEY_SWIPE_SIDE_GESTURE_BAR_TYPE, 0);
        this.mNavBarGesture = navigationBarState == 2 || navigationBarState == 3;
        this.mNavBarHidden = (navigationBarState == 3 && sideGestureHideState == 1) || navigationBarState == 2;
        OplusInputMethodUtil.logDebug(TAG, "mNavBarGesture = " + this.mNavBarGesture + ", mNavBarHidden = " + this.mNavBarHidden);
    }

    private void updateKeyboardRaise() {
        if (this.mNavBarGesture) {
            boolean z = false;
            boolean isPositionRaise = this.mKeyboardPosition == 1;
            boolean isFoldDisplayOpen = isFoldDisplayOpen();
            boolean isPortrait = this.mLastOrientation == 1;
            boolean isRemapDisplayDisabled = OplusInputMethodUtil.isRemapDisplayDisabled();
            if (isPositionRaise && (isPortrait || (isFoldDisplayOpen && !isRemapDisplayDisabled))) {
                z = true;
            }
            this.mKeyboardRaise = z;
            OplusInputMethodUtil.logDebugIme(TAG, "updateKeyboardRaise: mKeyboardRaise = " + this.mKeyboardRaise + ", isPositionRaise = " + isPositionRaise + ", isPortrait = " + isPortrait + ", isRemapDisplayDisabled = " + isRemapDisplayDisabled + ", isFoldDisplayOpen = " + isFoldDisplayOpen);
            updateNavHeight(true);
        }
    }

    private void updateNavHeight(boolean requestFitSystemWindows) {
        View navigationBarFrame;
        if (this.mNavBarGesture) {
            int navHeight = this.mFullNavHeight;
            if (!this.mDefaultDisplay || (this.mNavBarHidden && this.mKeyboardMode.isFloating())) {
                navHeight = 0;
            } else if (this.mKeyboardMode.isFloating() || !this.mKeyboardRaise) {
                navHeight = this.mHalfNavHeight;
            }
            boolean heightChanged = navHeight != this.mNavHeight;
            this.mNavHeight = navHeight;
            if (heightChanged) {
                OplusInputMethodUtil.logDebug(TAG, "updateNavHeight: mDefaultDisplay = " + this.mDefaultDisplay + ", mKeyboardRaise = " + this.mKeyboardRaise + ", mNavBarHidden = " + this.mNavBarHidden + ", mKeyboardMode = " + this.mKeyboardMode + ", navHeight = " + navHeight);
                WeakReference<View> weakReference = this.mNavigationBarFrame;
                if (weakReference != null && (navigationBarFrame = weakReference.get()) != null) {
                    updateNavigationBarOnShown(navigationBarFrame);
                }
                if (requestFitSystemWindows) {
                    View decorView = this.mInputMethodService.getWindow().getWindow().getDecorView();
                    ViewRootImpl viewRoot = decorView == null ? null : decorView.getViewRootImpl();
                    if (viewRoot != null) {
                        if (this.mInsetsState != null) {
                            viewRoot.getInsetsController().onStateChanged(this.mInsetsState);
                        }
                        viewRoot.requestFitSystemWindows();
                        OplusInputMethodUtil.logDebug(TAG, "requestFitSystemWindows");
                    }
                }
            }
        }
    }

    private Region calculateTouchableRegion(InputMethodService.Insets insets, View decorView, View inputFrame) {
        Region region = new Region();
        if (insets.touchableInsets == 3) {
            region.set(insets.touchableRegion);
        } else if (inputFrame.getVisibility() == 0) {
            inputFrame.getLocationInWindow(this.mTempLocation);
            int[] iArr = this.mTempLocation;
            int i = iArr[0];
            Rect rect = new Rect(i, iArr[1], inputFrame.getWidth() + i, this.mTempLocation[1] + inputFrame.getHeight());
            if (insets.touchableInsets == 1) {
                rect.top = insets.contentTopInsets;
            } else if (insets.touchableInsets == 2) {
                rect.top = insets.visibleTopInsets;
            }
            region.set(rect);
        }
        region.op(decorView.getLeft(), decorView.getTop(), decorView.getRight(), decorView.getBottom(), Region.Op.INTERSECT);
        return region;
    }

    public void updateNavInsets(InsetsState insetsState) {
        if (this.mNavBarGesture && insetsState != null) {
            OplusInputMethodUtil.logDebugIme(TAG, "updateNavInsets: insetsState = " + insetsState);
            Rect navRect = new Rect(insetsState.getDisplayFrame());
            navRect.top = navRect.bottom - this.mNavHeight;
            int navType = WindowInsets.Type.navigationBars();
            for (int i = insetsState.sourceSize() - 1; i >= 0; i--) {
                InsetsSource source = insetsState.sourceAt(i);
                if (source.getType() == navType) {
                    insetsState.removeSourceAt(i);
                    if (this.mNavInsetSource == null) {
                        this.mNavInsetSource = new InsetsSource(source);
                    }
                }
            }
            if (this.mNavInsetSource == null) {
                InsetsSource insetsSource = new InsetsSource(InsetsSource.createId(this, 0, navType), navType);
                this.mNavInsetSource = insetsSource;
                insetsSource.setVisible(true);
            }
            this.mNavInsetSource.setFrame(navRect);
            insetsState.addSource(this.mNavInsetSource);
            this.mInsetsState = insetsState;
            OplusInputMethodUtil.logDebugIme(TAG, "updateNavInsets: mNavHeight = " + this.mNavHeight + ", navRect = " + navRect + ", mNavInsetSource = " + this.mNavInsetSource);
        }
    }
}
