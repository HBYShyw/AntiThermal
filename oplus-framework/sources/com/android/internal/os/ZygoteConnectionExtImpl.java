package com.android.internal.os;

import android.util.Log;
import com.oplus.hiddenapi.IOplusHiddenApiManager;

/* loaded from: classes.dex */
public class ZygoteConnectionExtImpl implements IZygoteConnectionExt {
    private static final String TAG = ZygoteConnectionExtImpl.class.getSimpleName();

    public void afterForkAndSpecializeInChild(ZygoteArguments args, IZygoteArgumentsExt argsExt) {
        if (argsExt != null && argsExt.getOplusHiddenApiExemptions() != null) {
            if (IOplusHiddenApiManager.DEBUG) {
                Log.d(TAG, "set child at forkAndSpecialize: " + args.mPackageName + ", " + argsExt.getOplusHiddenApiExemptions().length);
            }
            ZygoteInit.setApiDenylistExemptions(argsExt.getOplusHiddenApiExemptions());
        }
    }
}
