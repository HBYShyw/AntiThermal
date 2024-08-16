package com.android.internal.app;

import android.R;
import android.app.Activity;
import android.common.OplusFrameworkFactory;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.customize.OplusCustomizeRestrictionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.multiapp.OplusMultiAppManager;
import com.oplus.resolver.OplusResolverUtils;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ResolverActivityExtImpl implements IResolverActivityExt {
    private static final String TAG = "ResolverActivityExt";
    private int mCustomFlag = 0;
    private Object mMultiProfilePagerAdapter;
    private final Activity mResolverActivity;
    protected IOplusResolverManager mResolverManager;
    private UserHandle mWorkProfileUserHandle;

    public ResolverActivityExtImpl(Object base) {
        this.mResolverManager = IOplusResolverManager.DEFAULT;
        this.mResolverActivity = (Activity) base;
        this.mResolverManager = (IOplusResolverManager) OplusFrameworkFactory.getInstance().getFeature(IOplusResolverManager.DEFAULT, new Object[0]);
    }

    public void setMultiProfilePagerAdapter(Object abstractMultiProfilePagerAdapter) {
        this.mMultiProfilePagerAdapter = abstractMultiProfilePagerAdapter;
    }

    public Activity getResolverActivity() {
        return this.mResolverActivity;
    }

    public String getReferrerPackageName() {
        return (String) OplusResolverUtils.invokeMethod(this.mResolverActivity, "getReferrerPackageName", new Object[0]);
    }

    public Object getMultiProfilePagerAdapter() {
        return this.mMultiProfilePagerAdapter;
    }

    public UserHandle getWorkProfileUserHandle() {
        return this.mWorkProfileUserHandle;
    }

    public void getWorkProfileUserHandle(UserHandle userHandle) {
        this.mWorkProfileUserHandle = userHandle;
    }

    public boolean hasCustomFlag(int flag) {
        return (this.mCustomFlag & flag) != 0;
    }

    public void cacheCustomInfo(Intent intent) {
        if (intent != null) {
            this.mCustomFlag = intent.getIntentExt().getOplusFlags();
        }
    }

    public void hookFinish() {
        if (!isOriginUi()) {
            this.mResolverActivity.overridePendingTransition(0, 201981975);
        }
    }

    public void hookonMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        this.mResolverManager.onMultiWindowModeChanged();
    }

    public void hookonConfigurationChanged(Configuration newConfig) {
        this.mResolverManager.onConfigurationChanged(newConfig);
    }

    public void hookonCreate(Bundle savedInstanceState) {
        this.mResolverManager.onCreate(this);
        cacheCustomInfo(this.mResolverActivity.getIntent());
    }

    public void hookonResume() {
        this.mResolverManager.onResume();
    }

    public void hookonPause() {
        this.mResolverManager.onPause();
    }

    public void hookonDestroy() {
        this.mResolverManager.onDestroy();
    }

    public boolean hookonSaveInstanceState(Bundle outState, String tabKey) {
        if (isOriginUi()) {
            return false;
        }
        Integer currentPage = (Integer) OplusResolverUtils.invokeMethod(this.mMultiProfilePagerAdapter, "getCurrentPage", new Object[0]);
        if (currentPage != null) {
            outState.putInt(tabKey, currentPage.intValue());
            return true;
        }
        return true;
    }

    public boolean hookonRestoreInstanceState(Bundle savedInstanceState, String tabKey) {
        if (isOriginUi()) {
            return false;
        }
        restoreProfilePager(savedInstanceState.getInt(tabKey));
        OplusResolverUtils.invokeMethod(this.mMultiProfilePagerAdapter, "clearInactiveProfileCache", new Object[0]);
        return true;
    }

    public boolean isOriginUi() {
        return this.mResolverManager.isOriginUi();
    }

    public boolean addExtraOneAppFinish() {
        return this.mResolverManager.addExtraOneAppFinish();
    }

    public boolean hookIsLastChosen() {
        return this.mResolverManager.isLastChosen();
    }

    public void hookconfigureContentView(boolean safeForwardingMode, boolean supportsAlwaysUseOption, int layoutId) {
        initActionSend();
        initView(safeForwardingMode, supportsAlwaysUseOption);
    }

    public void initView(boolean safeForwardingMode, boolean supportsAlwaysUseOption) {
        this.mResolverManager.initView(safeForwardingMode, supportsAlwaysUseOption);
    }

    public void updateView(boolean safeForwardingMode, boolean supportsAlwaysUseOption) {
        this.mResolverManager.updateView(safeForwardingMode, supportsAlwaysUseOption);
    }

    public void initActionSend() {
        this.mResolverManager.initActionSend();
    }

    public void statisticsData(ResolveInfo ri, int which) {
        this.mResolverManager.statisticsData(ri, which);
    }

    public boolean hookonListRebuilt() {
        if (!isOriginUi()) {
            this.mResolverManager.setResolverContent();
            return true;
        }
        return false;
    }

    public boolean initPreferenceAndPinList() {
        this.mResolverManager.initPreferenceAndPinList();
        return isOriginUi();
    }

    public void performAnimation() {
        if (!isOriginUi()) {
            this.mResolverActivity.overridePendingTransition(201981966, 201981969);
        }
    }

    public void setChooserPreFileSingleIconView(int iconResId, ImageView fileIconView, boolean needTry, TextView fileNameView, boolean singleFile, String fileName, Uri uri) {
        if (!needTry || !tryApkResourceName(uri, fileIconView, fileNameView)) {
            fileIconView.setImageResource(this.mResolverManager.getChooserPreFileSingleIconView(singleFile, fileName, uri));
        }
    }

    public String getChooserPreFileName(Context context, int fileCount, String fileName) {
        return this.mResolverManager.getChooserPreFileName(context, fileCount, fileName);
    }

    public ViewGroup getDisplayFileContentPreview(LayoutInflater layoutInflater, ViewGroup parent) {
        return this.mResolverManager.getDisplayFileContentPreview(layoutInflater, parent);
    }

    public ViewGroup hookdisplayTextContentPreview(Intent targetIntent, LayoutInflater layoutInflater, ViewGroup parent, View.OnClickListener listener) {
        ViewGroup contentPreviewLayout = this.mResolverManager.getDisplayTextContentPreview(layoutInflater, parent);
        ViewGroup actionRow = (ViewGroup) contentPreviewLayout.findViewById(R.id.content_preview_image_area);
        displayTextAddActionButton(actionRow, listener);
        return contentPreviewLayout;
    }

    public ViewGroup getDisplayImageContentPreview(LayoutInflater layoutInflater, ViewGroup parent) {
        return this.mResolverManager.getDisplayImageContentPreview(layoutInflater, parent);
    }

    public void restoreProfilePager(int pageNumber) {
        this.mResolverManager.restoreProfilePager(pageNumber);
    }

    public boolean tryApkResourceName(Uri uri, ImageView imageView, TextView textView) {
        return this.mResolverManager.tryApkResourceName(uri, imageView, textView);
    }

    public void displayTextAddActionButton(ViewGroup actionRow, View.OnClickListener listener) {
        this.mResolverManager.displayTextAddActionButton(actionRow, listener);
    }

    public void setCustomRoundImage(Paint roundRectPaint, Paint textPaint, Paint overlayPaint) {
        this.mResolverManager.setCustomRoundImage(roundRectPaint, textPaint, overlayPaint);
    }

    public int getCornerRadius(Context context) {
        return this.mResolverManager.getCornerRadius(context);
    }

    public boolean hookonCopyButtonClicked(ArrayList<Uri> streams) {
        if (streams != null && !streams.isEmpty()) {
            return false;
        }
        return true;
    }

    public ClipData getclipData(Intent targetIntent) {
        if (targetIntent == null) {
            return null;
        }
        String extraText = targetIntent.getStringExtra("android.intent.extra.TEXT");
        Uri extraStream = (Uri) targetIntent.getParcelableExtra("android.intent.extra.STREAM");
        if (extraText != null) {
            ClipData clipData = ClipData.newPlainText(null, extraText);
            return clipData;
        }
        if (extraStream != null) {
            ClipData clipData2 = ClipData.newUri(this.mResolverActivity.getContentResolver(), null, extraStream);
            return clipData2;
        }
        Log.w(TAG, "No data available to copy to clipboard");
        return null;
    }

    public boolean hookgetNearbySharingComponent() {
        return (isOriginUi() || OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_RESOLVER_SHARE_EMAIL)) ? false : true;
    }

    public void hookmaybeHideContentPreview() {
        IChooserActivityWrapper wrapper;
        if (!isOriginUi() && (wrapper = (IChooserActivityWrapper) OplusResolverUtils.invokeMethod(this.mResolverActivity, "getChooserWrapper", new Object[0])) != null) {
            wrapper.hideStickyContentPreview();
        }
    }

    public boolean getFileSharedDisabled() {
        boolean isDisabled = OplusCustomizeRestrictionManager.getInstance(this.mResolverActivity).getFileSharedDisabled();
        if (isDisabled) {
            Toast.makeText(this.mResolverActivity.getApplicationContext(), 201589214, 0).show();
        }
        return isDisabled;
    }

    public void clearInactiveProfileCache(int page) {
        this.mResolverManager.clearInactiveProfileCache(page);
    }

    public void adaptFontSize(TextView textView) {
        float textSizePixel = textView.getTextSize();
        float fontScale = textView.getResources().getConfiguration().fontScale;
        float textSizeNoScale = textSizePixel / fontScale;
        textView.setTextSize(0, textSizeNoScale);
    }

    public Bitmap orientationThumbnailBitmap(ContentResolver contentResolver, Uri uri, Bitmap originBitmap) {
        return this.mResolverManager.orientationThumbnailBitmap(contentResolver, uri, originBitmap);
    }

    public void setMultiAppAccessMode(String pkgName, int accessMode) {
        OplusMultiAppManager.getInstance().setMultiAppAccessMode(pkgName, accessMode);
    }

    public void hookeConfigureMiniResolverContent() {
        this.mResolverManager.configureMiniResolverContent();
    }
}
