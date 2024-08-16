package com.oplus.hardware.devicecase;

import android.R;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.SystemProperties;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.android.internal.policy.PhoneWindow;
import java.util.List;

/* loaded from: classes.dex */
public class DeviceCaseCoverPanel {
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final float FLOAT_SALT = 0.5f;
    private static final int OPLUS_WINDOW_TYPE_CASE_PANEL = 2323;
    private static final int OPLUS_WINDOW_TYPE_SUB_CASE_PANEL = 2324;
    private static final int SCREEN_TIME_OUT = 10000;
    private static final String TAG = "DeviceCaseCoverPanel";
    private FrameLayout mContentFrame;
    private final Context mContext;
    private View mCustomView;
    private View mDecorView;
    private final View mDefaultRootView;
    private Display mDisplay;
    private final Window mWindow;
    private final WindowManager mWindowManager;
    private int mWindowType = -1;
    private int mContentFlag = 0;
    private int mViewPortLeft = 0;
    private int mViewPortTop = 0;
    private int mViewPortRight = 0;
    private int mViewPortBottom = 0;

    /* JADX INFO: Access modifiers changed from: protected */
    public DeviceCaseCoverPanel(Context context, List<Rect> viewPorts) {
        this.mDisplay = null;
        this.mContext = context;
        DisplayManager dm = (DisplayManager) context.getSystemService("display");
        if (dm != null) {
            this.mDisplay = dm.getDisplay(0);
        }
        parseViewPorts(viewPorts);
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        this.mWindowManager = windowManager;
        PhoneWindow phoneWindow = new PhoneWindow(context);
        this.mWindow = phoneWindow;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.setFitInsetsSides(0);
        params.setFitInsetsTypes(0);
        params.format = -3;
        params.flags = 17368328;
        params.layoutInDisplayCutoutMode = 3;
        params.privateFlags = 16;
        params.screenOrientation = 5;
        params.setTitle("OplusDeviceCaseWindow(" + context.getPackageName() + ")");
        phoneWindow.setAttributes(params);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mDefaultRootView = inflater.inflate(201917499, (ViewGroup) null);
        phoneWindow.setLayout(-1, -1);
        phoneWindow.setBackgroundDrawableResource(R.color.background_dark);
        phoneWindow.requestFeature(1);
        phoneWindow.setWindowManager(windowManager, null, null);
        phoneWindow.setGravity(17);
        phoneWindow.setDecorFitsSystemWindows(false);
    }

    private void parseViewPorts(List<Rect> viewPorts) {
        if (viewPorts == null || viewPorts.size() == 0) {
            Log.e(TAG, "parseViewPorts, viewPorts is empty!");
            return;
        }
        Rect defaultRect = viewPorts.get(0);
        Display display = this.mDisplay;
        if (display != null) {
            Display.Mode[] modes = display.getSupportedModes();
            if (modes != null && modes.length > 1) {
                Display.Mode activeMode = this.mDisplay.getMode();
                int maxWidth = activeMode.getPhysicalWidth();
                int maxHeight = activeMode.getPhysicalHeight();
                for (Display.Mode mode : modes) {
                    if (maxWidth < mode.getPhysicalWidth()) {
                        maxWidth = mode.getPhysicalWidth();
                        maxHeight = mode.getPhysicalHeight();
                    }
                }
                this.mViewPortLeft = Math.round(defaultRect.left * (activeMode.getPhysicalWidth() / (maxWidth + 0.5f)));
                this.mViewPortRight = Math.round(defaultRect.right * (activeMode.getPhysicalWidth() / (maxWidth + 0.5f)));
                this.mViewPortTop = Math.round(defaultRect.top * (activeMode.getPhysicalHeight() / (maxHeight + 0.5f)));
                this.mViewPortBottom = Math.round(defaultRect.bottom * (activeMode.getPhysicalHeight() / (maxHeight + 0.5f)));
            } else {
                this.mViewPortLeft = defaultRect.left;
                this.mViewPortRight = defaultRect.right;
                this.mViewPortTop = defaultRect.top;
                this.mViewPortBottom = defaultRect.bottom;
            }
            if (DEBUG) {
                Log.d(TAG, "parseViewPorts, finalLeft=" + this.mViewPortLeft + ", finalRight=" + this.mViewPortRight + ", finalTop=" + this.mViewPortTop + ", finalBottom=" + this.mViewPortBottom + ", supportModes=" + modes);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setWindowType(int type) {
        if (type == 2100) {
            this.mWindowType = 2323;
        } else if (type == 2101) {
            this.mWindowType = 2324;
        } else {
            Log.e(TAG, "setWindowType, not support window type:" + type);
            throw new IllegalArgumentException("Invalid Window Type: " + type + " for package " + this.mContext.getPackageName());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setContentFlag(int contentFlag) {
        if (contentFlag != 0 && contentFlag != 1) {
            Log.w(TAG, "setContentFlag invalid flag, keep current value:" + this.mContentFlag);
        } else {
            this.mContentFlag = contentFlag;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setupCustomView(View customView) {
        if (customView != null) {
            this.mCustomView = customView;
            detachCustomView();
            if (this.mContentFlag == 0) {
                this.mWindow.setContentView(this.mDefaultRootView);
                FrameLayout frameLayout = (FrameLayout) this.mDefaultRootView.findViewById(R.id.content);
                this.mContentFrame = frameLayout;
                frameLayout.removeAllViews();
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.mViewPortRight - this.mViewPortLeft, this.mViewPortBottom - this.mViewPortTop);
                this.mContentFrame.addView(customView, layoutParams);
                if (this.mContext.getResources().getConfiguration().getLayoutDirection() == 1) {
                    this.mContentFrame.setPadding(0, this.mViewPortTop, this.mViewPortLeft, 0);
                    return;
                } else {
                    this.mContentFrame.setPadding(this.mViewPortLeft, this.mViewPortTop, 0, 0);
                    return;
                }
            }
            this.mWindow.setContentView(customView);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void show() {
        if (this.mWindowType == -1) {
            Log.e(TAG, "show error, invalid window type");
            return;
        }
        if (this.mDecorView != null) {
            Log.w(TAG, "show repeated, the window is already exist!");
            return;
        }
        this.mDecorView = this.mWindow.getDecorView();
        WindowManager.LayoutParams wlp = this.mWindow.getAttributes();
        wlp.type = this.mWindowType;
        if (this.mWindowType == 2324) {
            wlp.windowAnimations = 201523244;
        }
        if (this.mWindowType == 2323) {
            wlp.userActivityTimeout = 10000L;
        }
        Log.d(TAG, "show cover window, windowType=" + this.mWindowType + ", contentFlag=" + this.mContentFlag);
        this.mWindowManager.addView(this.mDecorView, wlp);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void remove(View view) {
        if (this.mDecorView == null) {
            return;
        }
        if (this.mWindow.isDestroyed()) {
            Log.e(TAG, "Tried to remove but the CoverPanel's window as already destroyed!");
            return;
        }
        if (!isMatchToDecorView(view)) {
            Log.e(TAG, "customView cannot match current decorView");
        }
        try {
            try {
                detachCustomView();
                this.mWindowManager.removeViewImmediate(this.mDecorView);
            } catch (Exception exception) {
                Log.e(TAG, "remove failed!", exception);
            }
        } finally {
            this.mContentFrame = null;
            this.mDecorView = null;
            this.mCustomView = null;
            this.mWindow.closeAllPanels();
        }
    }

    private void detachCustomView() {
        ViewGroup parent;
        View view = this.mCustomView;
        if (view != null && (parent = (ViewGroup) view.getParent()) != null) {
            Log.w(TAG, "detachCustomView, already have a parent(" + parent + "), remove it from parent first");
            parent.removeAllViews();
        }
    }

    private boolean isMatchToDecorView(View customView) {
        if (DEBUG) {
            Log.d(TAG, "isMatchToDecorView, customView:{" + customView + ", winToken=" + customView.getWindowToken() + ", winId=" + customView.getWindowId() + "}\n, mDecorView:{" + this.mDecorView + ", winToken=" + this.mDecorView.getWindowToken() + ", winId=" + this.mDecorView.getWindowId());
        }
        if (customView.getWindowToken() != this.mDecorView.getWindowToken()) {
            return false;
        }
        return true;
    }
}
