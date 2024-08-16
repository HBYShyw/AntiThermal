package com.android.internal.app;

import android.R;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/* loaded from: classes.dex */
public interface IOplusResolverManager extends IOplusCommonFeature {
    public static final IOplusResolverManager DEFAULT = new IOplusResolverManager() { // from class: com.android.internal.app.IOplusResolverManager.1
    };

    default IOplusResolverManager getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusResolverManager;
    }

    default boolean isOriginUi() {
        return true;
    }

    default void onCreate(IResolverActivityExt activity) {
    }

    default void onResume() {
    }

    default void onPause() {
    }

    default void onDestroy() {
    }

    default void initActionSend() {
    }

    default boolean addExtraOneAppFinish() {
        return true;
    }

    default boolean isLastChosen() {
        return false;
    }

    default void initView(boolean safeForwardingMode, boolean supportsAlwaysUseOption) {
    }

    default void updateView(boolean safeForwardingMode, boolean supportsAlwaysUseOption) {
    }

    default void onMultiWindowModeChanged() {
    }

    default void onConfigurationChanged(Configuration newConfig) {
    }

    default void setResolverContent() {
    }

    default void initPreferenceAndPinList() {
    }

    default void statisticsData(ResolveInfo ri, int which) {
    }

    default ResolveInfo getResolveInfo(Intent ii, PackageManager mPm) {
        ActivityInfo ai = ii.resolveActivityInfo(mPm, 0);
        if (ai == null) {
            return null;
        }
        ResolveInfo ri = new ResolveInfo();
        ri.activityInfo = ai;
        return ri;
    }

    default int getChooserPreFileSingleIconView(boolean singleFile, String fileName, Uri uri) {
        return singleFile ? R.drawable.cling_button : R.drawable.ic_lock_airplane_mode;
    }

    default ViewGroup getDisplayImageContentPreview(LayoutInflater layoutInflater, ViewGroup parent) {
        return (ViewGroup) layoutInflater.inflate(R.layout.date_picker_header_material, parent, false);
    }

    default ViewGroup getDisplayTextContentPreview(LayoutInflater layoutInflater, ViewGroup parent) {
        return (ViewGroup) layoutInflater.inflate(R.layout.date_picker_legacy, parent, false);
    }

    default ViewGroup getDisplayFileContentPreview(LayoutInflater layoutInflater, ViewGroup parent) {
        return (ViewGroup) layoutInflater.inflate(R.layout.date_picker_dialog, parent, false);
    }

    default void clearInactiveProfileCache(int page) {
    }

    default void showActiveEmptyViewState() {
    }

    default void restoreProfilePager(int pageNumber) {
    }

    default boolean tryApkResourceName(Uri uri, ImageView imageView, TextView textView) {
        return false;
    }

    default ViewGroup getChooserProfileDescriptor(LayoutInflater inflater) {
        return (ViewGroup) inflater.inflate(R.layout.date_picker_legacy_holo, (ViewGroup) null, false);
    }

    default ViewGroup getResolverProfileDescriptor(LayoutInflater inflater) {
        return (ViewGroup) inflater.inflate(R.layout.simple_account_item, (ViewGroup) null, false);
    }

    default void displayTextAddActionButton(ViewGroup actionRow, View.OnClickListener listener) {
    }

    default void setCustomRoundImage(Paint roundRectPaint, Paint textPaint, Paint overlayPaint) {
    }

    default int getCornerRadius(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.config_appTransitionAnimationDurationScaleDefault);
    }

    default String getChooserPreFileName(Context context, int fileCount, String fileName) {
        return context.getResources().getQuantityString(R.string.keyguard_accessibility_status, fileCount - 1, fileName, Integer.valueOf(fileCount - 1));
    }

    @Deprecated
    default boolean isLoadTheme() {
        return true;
    }

    @Deprecated
    default void setLastChosen(ResolverListController controller, Intent intent, IntentFilter filter, int bestMatch) throws RemoteException {
    }

    @Deprecated
    default void addNearbyAction(ViewGroup parent, Intent targetIntent) {
    }

    @Deprecated
    default boolean isOpShareUi() {
        return false;
    }

    @Deprecated
    default void setOriginContentView(int layoutId) {
    }

    @Deprecated
    default void setActionButtonTextColor(Button button) {
    }

    @Deprecated
    default void setChooserHeadBackground(View elevatedView) {
    }

    @Deprecated
    default View createTypeNormalView(View v, int targetWidth) {
        return v;
    }

    @Deprecated
    default int getChooserActionRowId() {
        return R.id.content_preview_image_area;
    }

    default Bitmap orientationThumbnailBitmap(ContentResolver contentResolver, Uri uri, Bitmap originBitmap) {
        return originBitmap;
    }

    default void configureMiniResolverContent() {
    }
}
