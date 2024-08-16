package com.oplus.resolver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import com.oplus.widget.OplusItem;
import java.util.List;

/* loaded from: classes.dex */
public class OplusGalleryPagerAdapterHelper {
    private static final String FEATURE_PACKAGE = "com.oplus.resolver.OplusResolverPagerAdapterHelper";
    private static final String TAG = "OplusResolverPagerAdapterHelper";
    private IOplusGalleryPagerAdapter mAdapterHelper;
    private Context mContext;

    public OplusGalleryPagerAdapterHelper(Context context, Dialog dialog) {
        this.mAdapterHelper = IOplusGalleryPagerAdapter.DEFAULT;
        this.mContext = context;
        try {
            this.mAdapterHelper = (IOplusGalleryPagerAdapter) Class.forName(FEATURE_PACKAGE).getDeclaredConstructor(Context.class, Dialog.class).newInstance(context, dialog);
        } catch (Exception e) {
            Log.d(TAG, "cannot constructor error:" + e.getMessage());
        }
    }

    public View createPagerView(List<OplusItem> appinfo, int pagerNumber, int pagerSize) {
        View view = this.mAdapterHelper.createPagerView(appinfo, pagerNumber, pagerSize);
        if (view == null) {
            return new View(this.mContext);
        }
        return view;
    }

    public OplusItem[][] listToArray(List<OplusItem> oplusItems) {
        return this.mAdapterHelper.listToArray(oplusItems);
    }

    public void dismiss() {
        this.mAdapterHelper.dismiss();
    }

    public List<OplusItem> loadBitmap(Intent originIntent, List<ResolveInfo> riList, int pagerNumber, int pagerSize, int placeholderCount) {
        return this.mAdapterHelper.loadBitmap(originIntent, riList, pagerNumber, pagerSize, placeholderCount);
    }

    public void setOplusResolverItemEventListener(IOplusResolverGridItemClickListener listener) {
        this.mAdapterHelper.setOplusResolverItemEventListener(listener);
    }

    public void updateUserHandle(UserHandle userHandle) {
        this.mAdapterHelper.updateUserHandle(userHandle);
    }

    public void unRegister() {
        this.mAdapterHelper.unRegister();
    }

    public void setColumnSize(int columnSize) {
        this.mAdapterHelper.setColumnSize(columnSize);
    }
}
