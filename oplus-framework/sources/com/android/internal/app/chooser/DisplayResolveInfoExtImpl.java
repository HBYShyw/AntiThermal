package com.android.internal.app.chooser;

import android.app.Activity;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.app.IResolverActivityExt;

/* loaded from: classes.dex */
public class DisplayResolveInfoExtImpl implements IDisplayResolveInfoExt {
    private DisplayResolveInfo mDisplayResolveInfo;
    private boolean mIsMultiApp = false;

    public DisplayResolveInfoExtImpl(Object object) {
        if (object != null && (object instanceof DisplayResolveInfo)) {
            this.mDisplayResolveInfo = (DisplayResolveInfo) object;
        }
    }

    public void setIsMultiApp(boolean isMultiApp) {
        this.mIsMultiApp = isMultiApp;
    }

    public int changeUserIdIfNeed(IResolverActivityExt resolverActivityExt, Intent intent, int userId) {
        int fromUserId;
        if (resolverActivityExt == null || resolverActivityExt.getResolverActivity() == null) {
            return userId;
        }
        if (resolverActivityExt.hasCustomFlag(512) && intent != null) {
            if (getIsMultiApp()) {
                userId = 999;
                intent.putExtra("android.intent.extra.USER_ID", 999);
            } else {
                userId = 0;
            }
            if (-1 != resolverActivityExt.getResolverActivity().getLaunchedFromUid() && (((fromUserId = UserHandle.getUserId(resolverActivityExt.getResolverActivity().getLaunchedFromUid())) == 999 && userId == 0) || (fromUserId == 0 && userId == 999))) {
                intent.prepareToLeaveUser(fromUserId);
                Log.d("ChooseActivity", "CMAService fillMultiAppInfo for cross form:" + fromUserId + " to:" + userId + " intent=" + intent.toString());
            }
            intent.getIntentExt().addOplusFlags(2048);
        }
        return userId;
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

    public boolean shouldPrepareIntentForCrossProfileLaunch(Activity activity, Intent intent) {
        if ((intent.getIntentExt().getOplusFlags() & 512) != 0 || UserHandle.getUserId(activity.getLaunchedFromUid()) == 999) {
            return false;
        }
        return true;
    }

    private boolean getIsMultiApp() {
        return this.mIsMultiApp;
    }
}
