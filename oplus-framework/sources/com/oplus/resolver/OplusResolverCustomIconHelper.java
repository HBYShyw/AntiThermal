package com.oplus.resolver;

import android.common.OplusFeatureCache;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.IOplusThemeManager;
import android.content.res.IUxIconPackageManagerExt;
import android.graphics.drawable.Drawable;
import com.oplus.util.OplusResolverIntentUtil;
import com.oplus.widget.OplusItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusResolverCustomIconHelper implements IOplusResolverCustomIconHelper {
    private static final Map<String, String> DEFAULT_RESOLVER_ICON;
    private static final String TAG = "OplusResolverCustomIconHelper";
    private static final int mColumnCounts = OplusResolverPagerAdapter.COLUMN_SIZE;
    private Context mContext;
    private Map<String, String> mIconNameMap;

    static {
        HashMap hashMap = new HashMap();
        DEFAULT_RESOLVER_ICON = hashMap;
        hashMap.put("cn.wps.moffice_eng/cn.wps.moffice.documentmanager.PreStartActivity2/excel", "share_excel");
        hashMap.put("cn.wps.moffice_eng/cn.wps.moffice.documentmanager.PreStartActivity2/pdf", "share_pdf");
        hashMap.put("cn.wps.moffice_eng/cn.wps.moffice.documentmanager.PreStartActivity2/ppt", "share_ppt");
        hashMap.put("cn.wps.moffice_eng/cn.wps.moffice.documentmanager.PreStartActivity2/word", "share_word");
        hashMap.put("com.android.bluetooth/.opp.BluetoothOppLauncherActivity", "share_blue");
        hashMap.put("com.baidu.netdisk/.p2pshare.ui.ReceiverP2PShareFileActivity", "share_lightnin");
        hashMap.put("com.eg.android.AlipayGphone/com.alipay.mobile.quinox.splash.ShareScanQRDispenseActivity", "share_scan");
        hashMap.put("com.mt.mtxx.mtxx/com.meitu.mtxx.img.IMGMainActivity", "share_edit");
        hashMap.put("com.mt.mtxx.mtxx/.beauty.BeautyMainActivity", "share_retouch");
        hashMap.put("com.sina.weibo/.weiyou.share.WeiyouShareDispatcher", "share_chat");
        hashMap.put("com.sina.weibo/.story.publisher.StoryDispatcher", "share_story");
        hashMap.put("com.tencent.eim/com.tencent.mobileqq.activity.qfileJumpActivity", "share_pc");
        hashMap.put("com.tencent.mm/.ui.tools.AddFavoriteUI", "share_fav");
        hashMap.put("com.tencent.mm/.ui.tools.ShareToTimeLineUI", "share_moment");
        hashMap.put("com.tencent.mobileqq/cooperation.qqfav.widget.QfavJumpActivity", "share_fav");
        hashMap.put("com.tencent.mobileqq/.activity.qfileJumpActivity", "share_pc");
        hashMap.put("com.tencent.mobileqq/cooperation.qlink.QlinkShareJumpActivity", "share_quick");
        hashMap.put("com.tencent.qqlite/cooperation.qqfav.widget.QfavJumpActivity", "share_fav");
        hashMap.put("com.tencent.qqlite/com.tencent.mobileqq.activity.qfileJumpActivity", "share_pc");
        hashMap.put("com.tencent.tim/cooperation.qqfav.widget.QfavJumpActivity", "share_fav");
        hashMap.put("com.UCMobile/.share", "share_chat");
    }

    public OplusResolverCustomIconHelper(Context context, int compatibleArgs) {
        this(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OplusResolverCustomIconHelper(Context context) {
        this.mContext = context;
        Map<String, String> iconsMap = OplusResolverInfoHelper.getInstance(context).getIconsMap();
        this.mIconNameMap = iconsMap;
        if (iconsMap == null || iconsMap.isEmpty()) {
            this.mIconNameMap = DEFAULT_RESOLVER_ICON;
        }
    }

    @Override // com.oplus.resolver.IOplusResolverCustomIconHelper
    public OplusItem getAppInfo(Intent originIntent, ResolveInfo info, PackageManager mPm) {
        if (info == null) {
            return null;
        }
        OplusItem appInfo = new OplusItem();
        appInfo.setText(info.loadLabel(mPm).toString());
        if (info.activityInfo != null) {
            appInfo.setPackageName(info.activityInfo.packageName);
            ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
            if (applicationInfo != null) {
                String label = applicationInfo.loadLabel(mPm).toString();
                if (!appInfo.getText().equals(label)) {
                    appInfo.setLabel(label);
                }
            }
        }
        appInfo.setIcon(oplusLoadIconForResolveInfo(originIntent, info, mPm));
        return appInfo;
    }

    public List<OplusItem> getDefaultAppInfo(int count) {
        Drawable drawable = this.mContext.getDrawable(201850995);
        List<OplusItem> appInfo = new ArrayList<>();
        int rowCounts = (int) Math.min(Math.ceil(count / mColumnCounts), 2.0d);
        for (int i = 0; i < rowCounts; i++) {
            int j = 0;
            while (true) {
                int i2 = mColumnCounts;
                if (j < count - (i * i2) && j < i2) {
                    OplusItem item = new OplusItem();
                    item.setIcon(drawable.getConstantState().newDrawable());
                    item.setText("");
                    appInfo.add(item);
                    j++;
                }
            }
        }
        return appInfo;
    }

    public OplusItem getDefaultAppInfo() {
        Drawable drawable = this.mContext.getDrawable(201850995);
        OplusItem appInfo = new OplusItem();
        appInfo.setIcon(drawable);
        appInfo.setText("");
        return appInfo;
    }

    public Drawable getAdaptiveIcon(int originDrawableId) {
        IUxIconPackageManagerExt uxIconPackageManagerExt;
        try {
            ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), 128);
            IUxIconPackageManagerExt uxIconPackageManagerExt2 = (IUxIconPackageManagerExt) this.mContext.getPackageManager().mPackageManagerExt.getUxIconPackageManagerExt();
            if (uxIconPackageManagerExt2 != null) {
                uxIconPackageManagerExt = uxIconPackageManagerExt2;
            } else {
                uxIconPackageManagerExt = IUxIconPackageManagerExt.DEFAULT;
            }
            return ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).loadOverlayResolverDrawable(uxIconPackageManagerExt, applicationInfo.packageName, originDrawableId, applicationInfo, null);
        } catch (PackageManager.NameNotFoundException e) {
            return this.mContext.getDrawable(originDrawableId);
        }
    }

    @Override // com.oplus.resolver.IOplusResolverCustomIconHelper
    public Drawable oplusLoadIconForResolveInfo(Intent originIntent, ResolveInfo ri, PackageManager mPm) {
        IUxIconPackageManagerExt uxIconPackageManagerExt;
        if (mPm == null || ri == null) {
            return null;
        }
        if (ri.activityInfo == null) {
            return ri.loadIcon(mPm);
        }
        int resId = ri.getIconResource();
        String packageName = ri.activityInfo.packageName;
        ApplicationInfo applicationInfo = ri.activityInfo.applicationInfo;
        if (ri.resolvePackageName != null && ri.icon != 0) {
            resId = ri.icon;
            if (ri.activityInfo.packageName != null && !ri.resolvePackageName.contains(ri.activityInfo.packageName)) {
                packageName = ri.resolvePackageName;
                applicationInfo = null;
            }
        }
        if (resId != 0) {
            IUxIconPackageManagerExt uxIconPackageManagerExt2 = (IUxIconPackageManagerExt) mPm.mPackageManagerExt.getUxIconPackageManagerExt();
            if (uxIconPackageManagerExt2 != null) {
                uxIconPackageManagerExt = uxIconPackageManagerExt2;
            } else {
                uxIconPackageManagerExt = IUxIconPackageManagerExt.DEFAULT;
            }
            Drawable dr = ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).loadOverlayResolverDrawable(uxIconPackageManagerExt, packageName, resId, applicationInfo, getResolveDrawableName(packageName, ri.activityInfo.name, OplusResolverIntentUtil.getIntentType(originIntent)));
            if (dr != null) {
                return dr;
            }
        }
        return ri.loadIcon(mPm);
    }

    private String getResolveDrawableName(String packageName, String resolveActivity, String type) {
        StringBuilder key = new StringBuilder();
        key.append(packageName).append("/").append(resolveActivity).append("/").append(type);
        String iconName = this.mIconNameMap.get(key.toString());
        if (iconName == null && resolveActivity.startsWith(packageName)) {
            key.delete(0, key.length());
            key.append(packageName).append("/").append(resolveActivity.substring(packageName.length())).append("/").append(type);
            iconName = this.mIconNameMap.get(key.toString());
        }
        if (iconName == null) {
            key.delete(0, key.length());
            key.append(packageName).append("/").append(resolveActivity);
            iconName = this.mIconNameMap.get(key.toString());
        }
        if (iconName == null && resolveActivity.startsWith(packageName)) {
            key.delete(0, key.length());
            key.append(packageName).append("/").append(resolveActivity.substring(packageName.length()));
            return this.mIconNameMap.get(key.toString());
        }
        return iconName;
    }
}
