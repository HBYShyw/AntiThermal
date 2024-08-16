package com.android.internal.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.util.Log;
import android.widget.BaseAdapter;
import com.android.internal.app.ResolverActivity;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.resolver.OplusOShareUtil;
import com.oplus.resolver.OplusResolverUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/* loaded from: classes.dex */
public class ResolverListAdapterExtImpl implements IResolverListAdapterExt {
    private static final String INTENT_VIEW_DATA = "aHR0cHM6Ly93d3cuZXhhbXBsZS5jb20=";
    private static final String PACKAGE_BROWSER = "Y29tLmhleXRhcC5icm93c2Vy";
    private static final String TAG = "ResolverListAdapterExtImpl";
    private List mPlaceholderResolveList;
    private final BaseAdapter mResolverListAdapter;

    public ResolverListAdapterExtImpl(Object base) {
        this.mResolverListAdapter = (BaseAdapter) base;
    }

    public List getPlaceholderResolveList() {
        return this.mPlaceholderResolveList;
    }

    public void setPlaceholderResolveList(List infos) {
        if (isOriginUi()) {
            return;
        }
        List list = this.mPlaceholderResolveList;
        if (list == null) {
            this.mPlaceholderResolveList = new ArrayList();
        } else {
            list.clear();
        }
        this.mPlaceholderResolveList.addAll(infos);
    }

    public boolean hasCustomFlag(int flag) {
        Context rlcc = (Context) OplusResolverUtils.getFiledValue(this.mResolverListAdapter, "mContext");
        IResolverActivityWrapper resolverActivityWrapper = (IResolverActivityWrapper) OplusResolverUtils.invokeMethod(rlcc, "getResolverWrapper", new Object[0]);
        if (resolverActivityWrapper == null) {
            return false;
        }
        return resolverActivityWrapper.getResolverActivityExt().hasCustomFlag(flag);
    }

    public ResolveInfo getResolveInfo(Intent ii, PackageManager mPm) {
        ResolveInfo ri = null;
        ActivityInfo ai = null;
        ComponentName cn2 = ii.getComponent();
        if (cn2 != null) {
            try {
                ai = mPm.getActivityInfo(ii.getComponent(), 0);
                ri = new ResolveInfo();
                ri.activityInfo = ai;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        if (ai == null) {
            ri = mPm.resolveActivity(ii, 65536);
            ai = ri != null ? ri.activityInfo : null;
        }
        if (ai == null) {
            Log.w(TAG, "No activity found for " + ii);
            return null;
        }
        return ri;
    }

    public boolean sortComponentsNull(List sortedComponents, boolean originShow) {
        if (originShow && isOriginUi()) {
            return (sortedComponents == null || sortedComponents.isEmpty()) ? false : true;
        }
        if (originShow && !isOriginUi()) {
            return true;
        }
        if ((!originShow && isOriginUi()) || originShow || isOriginUi()) {
            return true;
        }
        return (sortedComponents == null || sortedComponents.isEmpty()) ? false : true;
    }

    public boolean isOriginUi() {
        return false;
    }

    public ResolveInfo getExternalResolvedInfo(Context context, List multiResolveInfoList, List<Intent> intents, List<ResolverActivity.ResolvedComponentInfo> currentResolveList, UserHandle userHandle) {
        Intent intent;
        ResolveInfo oshareResolveInfo;
        ResolveInfo resolveInfo;
        if (intents == null || intents.isEmpty() || currentResolveList == null || (intent = intents.get(0)) == null) {
            return null;
        }
        if (userHandle != null && userHandle.getIdentifier() != 0 && "android.intent.action.VIEW".equals(intent.getAction()) && intent.getClipData() == null && intent.getExtras() == null && intent.getType() == null) {
            String intentViewData = new String(Base64.getDecoder().decode(INTENT_VIEW_DATA.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            if (intentViewData.equals(intent.getDataString())) {
                String browser = new String(Base64.getDecoder().decode(PACKAGE_BROWSER.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                for (ResolverActivity.ResolvedComponentInfo resolvedComponentInfo : currentResolveList) {
                    if (resolvedComponentInfo != null && resolvedComponentInfo.getCount() > 0 && (resolveInfo = resolvedComponentInfo.getResolveInfoAt(0)) != null && resolveInfo.activityInfo != null && browser.equals(resolveInfo.activityInfo.packageName)) {
                        currentResolveList.remove(resolvedComponentInfo);
                        Log.d(TAG, "addExternalResolvedInfo remove browser in apps, userId:" + userHandle.getIdentifier());
                        return null;
                    }
                }
            }
        }
        String actionStr = intent.getAction();
        if (actionStr == null || ((!actionStr.equalsIgnoreCase("android.intent.action.SEND") && !actionStr.equalsIgnoreCase("android.intent.action.SEND_MULTIPLE")) || intent.getPackage() != null)) {
            Log.d(TAG, "addExternalResolvedInfo is not send Action");
            return null;
        }
        if (!OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_RESOLVER_SHARE_EMAIL)) {
            return null;
        }
        if (("testData".equals(intent.getDataString()) || "text/non-matching-data".equals(intent.getDataString())) && intent.getExtras() == null && intent.getType() == null) {
            return null;
        }
        if ((currentResolveList.size() == 1 && hasCustomFlag(512) && multiResolveInfoList != null && multiResolveInfoList.size() > 0 && multiResolveInfoList.get(0) != null) || OplusOShareUtil.isNoOshareApplication(context, intent) || (oshareResolveInfo = OplusOShareUtil.getOshareComponent(context, intent)) == null || oshareResolveInfo.activityInfo == null) {
            return null;
        }
        return oshareResolveInfo;
    }
}
