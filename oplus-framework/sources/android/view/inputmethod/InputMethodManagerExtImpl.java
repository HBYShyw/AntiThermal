package android.view.inputmethod;

import android.app.AppGlobals;
import android.content.pm.OplusPackageManager;
import android.content.res.Configuration;
import android.inputmethodservice.OplusInputMethodServiceInternal;
import android.os.Bundle;
import android.view.ImeFocusController;
import android.view.InsetsState;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewRootImpl;
import com.android.internal.inputmethod.IRemoteInputConnection;
import com.oplus.util.OplusInputMethodUtil;

/* loaded from: classes.dex */
public class InputMethodManagerExtImpl implements IInputMethodManagerExt, ViewRootImpl.ConfigChangedCallback {
    private static final String CLASS_FLUTTER_VIEW = "io.flutter.embedding.android.FlutterView";
    private static final int DEFAULT_SCENARIO = -1;
    private static final int FORCE_DISABLE_SHOW_FORCE_SOFTINPUT = 701;
    private static final String TAG = "InputMethodManager";
    private static final long TIMEOUT_TOUCH_MILLIS = 500;
    private static OplusPackageManager sPackageManager = null;
    private static int sScenario = -1;
    private static int sWindowingMode = 0;
    private final Runnable mCheckFocusRunnable = new Runnable() { // from class: android.view.inputmethod.InputMethodManagerExtImpl.1
        @Override // java.lang.Runnable
        public void run() {
            if (InputMethodManagerExtImpl.this.mCurRootView != null && InputMethodManagerExtImpl.this.mCurRootView.getView() != null && InputMethodManagerExtImpl.this.mCurRootView.getView().hasImeFocus() && InputMethodManagerExtImpl.this.mCurRootView.getView().hasWindowFocus()) {
                InputMethodManagerExtImpl.this.logDebug("mCheckFocusRunnable checkFocus");
                InputMethodManagerExtImpl.this.mForceNewFocusOnce = true;
                InputMethodManagerExtImpl.this.mInputMethodManager.checkFocus();
            }
        }
    };
    private ViewRootImpl mCurRootView;
    private boolean mForceNewFocusOnce;
    private InputMethodManager mInputMethodManager;
    private long mLastTouchTime;
    private int mStartInputFlagsByShow;
    private int mStartInputResultCode;

    public InputMethodManagerExtImpl(Object base) {
        this.mInputMethodManager = (InputMethodManager) base;
        updateDebugToClass();
    }

    private void updateDebugToClass() {
        OplusInputMethodUtil.updateDebugToClass(InputMethodManager.class);
        OplusInputMethodUtil.updateDebugToClass(ImeFocusController.class);
        OplusInputMethodUtil.updateDebugImeToClass(BaseInputConnection.class);
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

    public int adjustForceFlag(int flags) {
        if (flags == 2) {
            if (sPackageManager == null) {
                sPackageManager = new OplusPackageManager();
            }
            if (sPackageManager.inCptWhiteList(701, AppGlobals.getInitialPackage())) {
                logDebug("adjustForceFlag: flags set to 0");
                return 0;
            }
        }
        return flags;
    }

    public boolean configDebug(String[] args) {
        if (OplusInputMethodUtil.dynamicallyConfigDebugByDumpArgs(args, "imm")) {
            updateDebugToClass();
            return true;
        }
        return false;
    }

    public void updateCursorAnchorInfoToSynergy(CursorAnchorInfo cursorAnchorInfo) {
        OplusInputMethodManagerInternal.getInstance().updateCursorAnchorInfoToSynergy(cursorAnchorInfo);
    }

    public void invalidateInputToSynergy(EditorInfo editorInfo, IRemoteInputConnection inputConnection, int sessionId) {
        if (editorInfo != null && editorInfo.extras != null) {
            editorInfo.extras.remove(OplusInputMethodUtil.KEY_INPUT_METHOD_SCENES);
        }
        OplusInputMethodManagerInternal.getInstance().invalidateInputToSynergy(editorInfo, inputConnection, sessionId);
    }

    public void updateCurrentRootView(ViewRootImpl rootView) {
        ViewRootImpl viewRootImpl = this.mCurRootView;
        if (rootView != viewRootImpl) {
            if (viewRootImpl != null) {
                if (viewRootImpl.getView() != null) {
                    this.mCurRootView.getView().removeCallbacks(this.mCheckFocusRunnable);
                }
                ViewRootImpl.removeConfigCallback(this);
            }
            this.mCurRootView = rootView;
            if (rootView == null) {
                sScenario = -1;
                sWindowingMode = 0;
            } else {
                ViewRootImpl.addConfigCallback(this);
                sScenario = this.mCurRootView.mContext.getResources().getConfiguration().mOplusExtraConfiguration.getScenario();
                sWindowingMode = this.mCurRootView.mContext.getResources().getConfiguration().windowConfiguration.getWindowingMode();
            }
            logDebug("updateCurrentRootView mCurRootView = " + this.mCurRootView + " sScenario = " + sScenario + " sWindowingMode = " + sWindowingMode);
            this.mLastTouchTime = 0L;
        }
    }

    public void attachInfoToEditorInfo(EditorInfo editorInfo) {
        if (editorInfo != null) {
            int flags = 0;
            logDebug("attachInfoToEditorInfo: mCurRootView = " + this.mCurRootView + " sScenario = " + sScenario + " sWindowingMode = " + sWindowingMode);
            int i = sWindowingMode;
            if (i == 6 || sScenario == 2) {
                flags = 0 | 1;
                logDebug("attachInfoToEditorInfo: multi window mode");
            } else if (i == 100) {
                flags = 0 | 2;
                logDebug("attachInfoToEditorInfo: zoom mode");
            }
            if (editorInfo.extras == null) {
                editorInfo.extras = new Bundle();
            }
            editorInfo.extras.putInt(OplusInputMethodUtil.KEY_INPUT_METHOD_SCENES, flags);
        }
    }

    public void onConfigurationChanged(Configuration globalConfig) {
        if (this.mCurRootView != null && globalConfig != null) {
            int scenario = globalConfig.mOplusExtraConfiguration.getScenario();
            int windowingMode = globalConfig.windowConfiguration.getWindowingMode();
            if (scenario != sScenario || windowingMode != sWindowingMode) {
                sScenario = scenario;
                sWindowingMode = windowingMode;
                logDebug("onConfigurationChanged mCurRootView = " + this.mCurRootView + " sScenario = " + sScenario + " sWindowingMode = " + sWindowingMode);
                if (this.mCurRootView.getView() != null) {
                    this.mCurRootView.getView().removeCallbacks(this.mCheckFocusRunnable);
                    this.mCurRootView.getView().post(this.mCheckFocusRunnable);
                }
            }
        }
    }

    public void onStartInputResult(int resultCode) {
        this.mStartInputResultCode = resultCode;
    }

    public void onViewRootTouchEvent(ViewRootImpl rootView, MotionEvent event) {
        if (rootView != null && event != null) {
            int action = event.getAction();
            if (action == 1 || action == 3) {
                OplusInputMethodManagerInternal.getInstance().updateTouchDeviceId(event.getDeviceId());
            }
            if (rootView == this.mCurRootView) {
                if (action == 0 || action == 1) {
                    this.mLastTouchTime = System.currentTimeMillis();
                }
            }
        }
    }

    public void onCallShowBeforeCheckFocus(View view) {
        this.mForceNewFocusOnce = false;
        this.mStartInputFlagsByShow = 0;
        if (this.mStartInputResultCode == 15 && view != null && view.hasFocus() && view.hasWindowFocus()) {
            long touchElapsedTime = System.currentTimeMillis() - this.mLastTouchTime;
            logDebug("onCallShowBeforeCheckFocus: touchElapsedTime = " + touchElapsedTime + " view = " + view.getClass().getName());
            if (touchElapsedTime < TIMEOUT_TOUCH_MILLIS) {
                this.mLastTouchTime = 0L;
                this.mForceNewFocusOnce = true;
                this.mStartInputFlagsByShow |= 256;
                if (CLASS_FLUTTER_VIEW.equals(view.getClass().getName())) {
                    this.mStartInputFlagsByShow |= 2;
                }
            }
        }
    }

    public int adjustStartInputFlags(int flags) {
        return this.mStartInputFlagsByShow | flags;
    }

    public boolean needForceNewFocus() {
        boolean forceNewFocus = this.mForceNewFocusOnce;
        this.mForceNewFocusOnce = false;
        if (forceNewFocus) {
            logDebug("needForceNewFocus forceNewFocus " + forceNewFocus);
        }
        return forceNewFocus;
    }

    public boolean ignoreFinishInput(int displayId) {
        ViewRootImpl viewRootImpl = this.mCurRootView;
        View rootView = viewRootImpl != null ? viewRootImpl.getView() : null;
        return (rootView == null || displayId == 0 || !OplusInputMethodUtil.isMirageDisplay(rootView.getContext(), displayId)) ? false : true;
    }

    public void updateNavInsets(int windowType, InsetsState insetsState) {
        if (windowType == 2011) {
            OplusInputMethodServiceInternal.getInstance().updateNavInsets(insetsState);
        }
    }
}
