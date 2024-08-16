package com.android.internal.app;

import android.app.OplusUxIconConfigParser;
import android.app.uxicons.CustomAdaptiveIconConfig;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.OplusBaseConfiguration;
import android.graphics.drawable.Drawable;
import android.os.oplusdevicepolicy.OplusDevicepolicyManager;
import android.text.TextUtils;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.multiapp.OplusMultiAppManager;
import com.oplus.resolver.NearbyUtil;
import com.oplus.resolver.OplusResolverCustomIconHelper;
import com.oplus.resolver.OplusResolverInfoHelper;
import com.oplus.util.OplusTypeCastingHelper;
import java.util.List;

/* loaded from: classes.dex */
public class ResolverHelperExtImpl implements IResolverHelperExt {
    public ResolverHelperExtImpl(Object base) {
    }

    public void sortResolverInfo(Context context, List<ResolveInfo> resolveInfos, Intent intent, int profilePage) {
        OplusResolverInfoHelper.getInstance(context.getApplicationContext()).resort(NearbyUtil.getNearbySharingComponent(context.getApplicationContext()), resolveInfos, intent, profilePage);
    }

    public Drawable getAppInfoDrawable(Context context, Intent originIntent, ResolveInfo info, PackageManager mPm) {
        return new OplusResolverCustomIconHelper(context.getApplicationContext(), 0).oplusLoadIconForResolveInfo(originIntent, info, mPm);
    }

    public boolean hasOplusFeature(String featureName) {
        return OplusFeatureConfigManager.getInstance().hasFeature(featureName);
    }

    public boolean hasDevicepolicy(String name) {
        return OplusDevicepolicyManager.getInstance().getBoolean(name, 1, false);
    }

    public int getIconScalePx(Context context) {
        OplusBaseConfiguration oplusBaseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, context.getResources().getConfiguration());
        if (oplusBaseConfiguration != null) {
            long iconConfig = oplusBaseConfiguration.mOplusExtraConfiguration.mUxIconConfig;
            int maxIconSize = new CustomAdaptiveIconConfig.Builder(context.getResources()).create().getDefaultIconSize();
            return maxIconSize - OplusUxIconConfigParser.getPxFromIconConfigDp(context.getResources(), (int) ((iconConfig >> 16) & 65535));
        }
        return -1;
    }

    public String getMultiAppAlias(String pkgName) {
        if (!TextUtils.isEmpty(pkgName)) {
            return OplusMultiAppManager.getInstance().getMultiAppAlias(pkgName);
        }
        return "";
    }
}
