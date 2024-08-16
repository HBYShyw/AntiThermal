package com.oplus.resolver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.PhoneNumberUtilsExtImpl;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.OplusAnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import com.oplus.widget.popupwindow.OplusPopupListWindow;
import com.oplus.widget.popupwindow.PopupListItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class OplusResolverPinnedUtil {
    private static final String GALLERY_PIN_LIST = "gallery_pin_list";
    private static final String TAG = "OplusResolverPinnedUtil";
    private final Context mContext;
    private final OplusPopupListWindow mPopupWindow;
    private final List<ResolveInfo> mRiList;

    public OplusResolverPinnedUtil(Context context, List<ResolveInfo> riList) {
        this.mContext = context;
        OplusPopupListWindow oplusPopupListWindow = new OplusPopupListWindow(context);
        this.mPopupWindow = oplusPopupListWindow;
        oplusPopupListWindow.setDismissTouchOutside(true);
        this.mRiList = riList;
    }

    public void showTargetDetails(View archer, ResolveInfo ri) {
        showTargetDetails(archer, ri, null);
    }

    public void showTargetDetails(View archer, ResolveInfo ri, final BaseAdapter adapter) {
        ComponentName nearby;
        final ComponentName cn2 = ri.activityInfo.getComponentName();
        final String componentName = cn2.flattenToShortString();
        ArrayList<PopupListItem> itemList = new ArrayList<>();
        if (!this.mRiList.isEmpty() && this.mRiList.get(0) == ri && (nearby = NearbyUtil.getNearbySharingComponent(this.mContext)) != null && nearby.getPackageName() != null && nearby.getPackageName().equals(cn2.getPackageName())) {
            showNearbyDetail(archer, cn2.getPackageName());
            return;
        }
        if (itemList.isEmpty()) {
            boolean pinned = false;
            ArrayList<String> pinPrefList = null;
            String galleryPinList = Settings.Secure.getString(this.mContext.getContentResolver(), GALLERY_PIN_LIST);
            if (!TextUtils.isEmpty(galleryPinList)) {
                pinPrefList = new ArrayList<>(Arrays.asList(galleryPinList.split(";")));
            }
            if (pinPrefList != null) {
                pinned = pinPrefList.contains(componentName);
            }
            Log.d(TAG, "showTargetDetails : " + pinPrefList + ", componentName : " + componentName + ", isPinned : " + pinned);
            String[] stringArray = pinned ? this.mContext.getResources().getStringArray(201785377) : this.mContext.getResources().getStringArray(201785376);
            for (String s : stringArray) {
                itemList.add(new PopupListItem(s, true));
            }
        }
        this.mPopupWindow.setItemList(itemList);
        this.mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.oplus.resolver.OplusResolverPinnedUtil$$ExternalSyntheticLambda1
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                OplusResolverPinnedUtil.this.lambda$showTargetDetails$0(componentName, adapter, cn2, adapterView, view, i, j);
            }
        });
        this.mPopupWindow.show(archer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showTargetDetails$0(String componentName, BaseAdapter adapter, ComponentName cn2, AdapterView parent, View view, int position, long id) {
        switch (position) {
            case 0:
                updatePinnedData(componentName);
                if (adapter != null) {
                    OplusResolverUtils.invokeMethod(adapter, "handlePackagesChanged", new Object[0]);
                    break;
                }
                break;
            case 1:
                showApplicationDetail(cn2.getPackageName());
                break;
        }
        this.mPopupWindow.dismiss();
    }

    private void showNearbyDetail(View archer, final String packageName) {
        ArrayList<PopupListItem> itemList = new ArrayList<>();
        itemList.add(new PopupListItem(this.mContext.getString(201589207), true));
        this.mPopupWindow.setItemList(itemList);
        this.mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.oplus.resolver.OplusResolverPinnedUtil$$ExternalSyntheticLambda0
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                OplusResolverPinnedUtil.this.lambda$showNearbyDetail$1(packageName, adapterView, view, i, j);
            }
        });
        this.mPopupWindow.show(archer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showNearbyDetail$1(String packageName, AdapterView parent, View view, int position, long id) {
        showApplicationDetail(packageName);
        this.mPopupWindow.dismiss();
    }

    private void showApplicationDetail(String packageName) {
        Intent in = new Intent().setAction("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", packageName, null)).addFlags(524288);
        this.mContext.startActivity(in);
        Context context = this.mContext;
        ((Activity) context).overridePendingTransition(OplusAnimationUtils.getPlatformAnimation(context, 201981966), OplusAnimationUtils.getPlatformAnimation(this.mContext, 201981969));
    }

    private void updatePinnedData(String componentName) {
        boolean isPinned = false;
        String galleryPinList = Settings.Secure.getString(this.mContext.getContentResolver(), GALLERY_PIN_LIST);
        Log.d(TAG, "pinList = " + galleryPinList);
        List<String> newList = new ArrayList<>();
        if (!TextUtils.isEmpty(galleryPinList)) {
            List<String> list = Arrays.asList(galleryPinList.split(";"));
            isPinned = list.contains(componentName);
            newList = new ArrayList<>(list);
        }
        Log.d(TAG, "newList = " + newList);
        if (isPinned) {
            newList.remove(componentName);
            Log.d(TAG, "remove : " + componentName);
        } else {
            newList.add(0, componentName);
            Log.d(TAG, "add : " + componentName);
        }
        String newString = listToString(newList, PhoneNumberUtilsExtImpl.WAIT);
        Settings.Secure.putString(this.mContext.getContentResolver(), GALLERY_PIN_LIST, newString);
        Log.d(TAG, "putStringForUser : " + newString);
    }

    private String listToString(List<String> list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i));
                sb.append(separator);
            }
        }
        return sb.toString();
    }
}
