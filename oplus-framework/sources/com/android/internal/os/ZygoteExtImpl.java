package com.android.internal.os;

import android.util.Log;
import com.oplus.hiddenapi.IOplusHiddenApiManager;

/* loaded from: classes.dex */
public class ZygoteExtImpl implements IZygoteExt {
    private static final String TAG = ZygoteExtImpl.class.getSimpleName();

    public void afterSpecializeAppProcessInChildMain(ZygoteArguments args, IZygoteArgumentsExt argsExt) {
        if (argsExt != null && argsExt.getOplusHiddenApiExemptions() != null) {
            if (IOplusHiddenApiManager.DEBUG) {
                Log.d(TAG, "set child after childMain: " + args.mPackageName + ", " + argsExt.getOplusHiddenApiExemptions().length);
            }
            ZygoteInit.setApiDenylistExemptions(argsExt.getOplusHiddenApiExemptions());
        }
    }
}
