package com.android.server.policy;

import android.R;
import android.app.ActivityManager;
import android.content.Context;
import android.os.UserManager;
import com.android.internal.globalactions.LongPressAction;
import com.android.internal.globalactions.SinglePressAction;
import com.android.server.policy.WindowManagerPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class RestartAction extends SinglePressAction implements LongPressAction {
    private final Context mContext;
    private final WindowManagerPolicy.WindowManagerFuncs mWindowManagerFuncs;

    public boolean showBeforeProvisioning() {
        return true;
    }

    public boolean showDuringKeyguard() {
        return true;
    }

    public RestartAction(Context context, WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
        super(R.drawable.ic_sysbar_quicksettings, R.string.lockscreen_forgot_pattern_button_text);
        this.mContext = context;
        this.mWindowManagerFuncs = windowManagerFuncs;
    }

    public boolean onLongPress() {
        if (ActivityManager.isUserAMonkey() || ((UserManager) this.mContext.getSystemService(UserManager.class)).hasUserRestriction("no_safe_boot")) {
            return false;
        }
        this.mWindowManagerFuncs.rebootSafeMode(true);
        return true;
    }

    public void onPress() {
        if (ActivityManager.isUserAMonkey()) {
            return;
        }
        this.mWindowManagerFuncs.reboot(false);
    }
}
