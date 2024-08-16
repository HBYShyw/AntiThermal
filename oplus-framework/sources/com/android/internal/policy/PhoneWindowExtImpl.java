package com.android.internal.policy;

import android.content.Context;
import android.content.pm.OplusPackageManager;
import android.content.res.TypedArray;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowExtImpl;
import android.view.WindowInsets;
import com.oplus.bluetooth.OplusBluetoothClass;
import com.oplus.debug.InputLog;
import com.oplus.statusbar.OplusStatusBarController;

/* loaded from: classes.dex */
public class PhoneWindowExtImpl extends WindowExtImpl implements IPhoneWindowExt {
    private static final String TAG = "PhoneWindowExtImpl";
    private Context mContext;
    public boolean mIsUseDefaultNavigationBarColor;
    private OplusPackageManager mOpm;
    private PhoneWindow mPhoneWindow;

    public PhoneWindowExtImpl(Object base) {
        super(base);
        this.mIsUseDefaultNavigationBarColor = false;
        this.mPhoneWindow = (PhoneWindow) base;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mOpm = new OplusPackageManager(this.mContext);
    }

    public void setSystemBarColor(int color) {
        DecorView decorView = this.mPhoneWindow.getWrapper().getDecorView();
        this.mPhoneWindow.mStatusBarColor = color;
        this.mPhoneWindow.getWrapper().setForcedStatusBarColor(true);
        this.mPhoneWindow.mNavigationBarColor = color;
        this.mPhoneWindow.getWrapper().setForcedNavigationBarColor(true);
        this.mPhoneWindow.mEnsureStatusBarContrastWhenTransparent = false;
        this.mPhoneWindow.mEnsureNavigationBarContrastWhenTransparent = false;
        if (decorView != null) {
            decorView.updateColorViews((WindowInsets) null, false);
        }
        Window.WindowControllerCallback callback = this.mPhoneWindow.getWindowControllerCallback();
        if (callback != null) {
            this.mPhoneWindow.getWindowControllerCallback().updateStatusBarColor(color);
            this.mPhoneWindow.getWindowControllerCallback().updateNavigationBarColor(color);
        }
    }

    public ViewGroup getContentParent() {
        return this.mPhoneWindow.mContentParent;
    }

    public CharSequence getWindowTitle() {
        return this.mPhoneWindow.getWrapper().getTitle();
    }

    public boolean isUseDefaultNavigationBarColor() {
        return this.mIsUseDefaultNavigationBarColor && !this.mPhoneWindow.getWrapper().isForcedNavigationBarColor();
    }

    public void hookGenerateLayout(PhoneWindow phoneWindow, TypedArray a, Context context) {
        int navigationBarThemeColor = a.getColor(35, this.mOpm.isClosedSuperFirewall() ? OplusBluetoothClass.Device.UNKNOWN : 1);
        if (navigationBarThemeColor == 1) {
            this.mIsUseDefaultNavigationBarColor = true;
            navigationBarThemeColor = OplusStatusBarController.getDefaultNavigationBarColor(context);
        }
        phoneWindow.mNavigationBarColor = OplusStatusBarController.getInstance().caculateSystemBarColor(context, context.getPackageName(), context.getClass().getName(), navigationBarThemeColor, 1);
    }

    public void hookForInputLogV(String msg) {
        InputLog.v(TAG, msg);
    }
}
