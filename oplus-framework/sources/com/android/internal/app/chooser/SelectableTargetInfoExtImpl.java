package com.android.internal.app.chooser;

import android.os.UserHandle;
import com.android.internal.app.IResolverActivityExt;

/* loaded from: classes.dex */
public class SelectableTargetInfoExtImpl implements ISelectableTargetInfoExt {
    public SelectableTargetInfoExtImpl(Object base) {
    }

    public boolean shouldPrepareIntentForCrossProfileLaunch(IResolverActivityExt resolverActivityExt) {
        if (resolverActivityExt == null || resolverActivityExt.getResolverActivity() == null) {
            return true;
        }
        if (!resolverActivityExt.hasCustomFlag(512) && UserHandle.getUserId(resolverActivityExt.getResolverActivity().getLaunchedFromUid()) != 999) {
            return true;
        }
        return false;
    }
}
