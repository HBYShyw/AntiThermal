package com.android.internal.app;

import android.R;
import android.app.Activity;
import android.common.OplusFeatureCache;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.service.wallpaper.WallpaperServiceExtImpl;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OplusViewRootUtil;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.app.OplusResolverManager;
import com.android.internal.app.chooser.DisplayResolveInfo;
import com.android.internal.widget.ResolverDrawerLayout;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.multiapp.OplusMultiAppManager;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.resolver.ApplicationEnableUtil;
import com.oplus.resolver.OplusOShareUtil;
import com.oplus.resolver.OplusResolverApkPreView;
import com.oplus.resolver.OplusResolverCustomIconHelper;
import com.oplus.resolver.OplusResolverDialogHelper;
import com.oplus.resolver.OplusResolverInfoHelper;
import com.oplus.resolver.OplusResolverUtils;
import com.oplus.resolver.widget.OplusResolverDrawerLayout;
import com.oplus.theme.IOplusThemeStyle;
import com.oplus.theme.OplusThemeStyle;
import com.oplus.util.OplusChangeTextUtil;
import com.oplus.util.OplusResolverIntentUtil;
import com.oplus.util.OplusResolverUtil;
import com.oplus.util.OplusRoundRectUtil;
import com.oplus.widget.OplusItem;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusResolverManager implements IOplusResolverManager {
    private static final float CHOOSER_PREVIEW_COPY_BUTTON_TEXTSIZE = 14.0f;
    private static final String FEATURE_FOLD = "oplus.hardware.type.fold";
    private static final String FEATURE_FOLD_REMAP_DISPLAY_DISABLED = "oplus.software.fold_remap_display_disabled";
    private static final int FIRST_SELECTED_POSITION = 0;
    private static final String GALLERY_PIN_LIST = "gallery_pin_list";
    private static final int MAIN_APP_ACCESS = 1;
    private static final String NAVIGATION_MODE = "navigation_mode";
    private static final int NAVIGATION_MODE_GESTURE = 2;
    private static final String PINNED_SHARED_PREFS_NAME = "chooser_pin_settings";
    private static final String TAG = "OplusResolverManager";
    private static final String TYPE_GALLERY = "gallery";
    private OplusResolverApkPreView mApkPreView;
    private OplusResolverDrawerLayout mContentPanel;
    private OplusResolverCustomIconHelper mCustomIconHelper;
    private String mGalleryPinList;
    private boolean mIsMainScreen;
    private int mLayoutId;
    protected boolean mOpenFlag;
    private SharedPreferences mPinnedSharedPrefs;
    private View mPreView;
    private ViewGroup mPreviewContainer;
    private OplusResolverInfoHelper mResolveInfoHelper;
    private Activity mResolverActivity;
    private IResolverActivityExt mResolverActivityExt;
    private OplusResolverDialogHelper mResolverDialogHelper;
    private View mRootView;
    private TabHost mTabHost;
    private TabChangeListener mTabListener;
    private ImageView mTransparentBottomView;
    protected boolean mIsActionSend = false;
    private Set<Integer> mLoadedPages = new HashSet();
    private int mLastSelected = -1;
    private int mLastOrientation = -1;
    private int mClickX = -1;
    private int mClickY = -1;
    private View mWhiteResolverLayout = null;
    private OplusItem mOplusItem = null;
    private ComponentCallbacks mComponentCallbacks = new AnonymousClass2();

    @Override // com.android.internal.app.IOplusResolverManager
    public boolean isOriginUi() {
        return false;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public boolean addExtraOneAppFinish() {
        return !this.mIsActionSend;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void onCreate(IResolverActivityExt resolverActivityExt) {
        this.mResolverActivityExt = resolverActivityExt;
        Activity resolverActivity = resolverActivityExt.getResolverActivity();
        this.mResolverActivity = resolverActivity;
        this.mLastOrientation = resolverActivity.getResources().getConfiguration().orientation;
        this.mResolverActivity.setTheme(((IOplusThemeStyle) OplusFeatureCache.getOrCreate(IOplusThemeStyle.DEFAULT, new Object[0])).getDialogAlertShareThemeStyle(OplusThemeStyle.DEFAULT_DIALOG_SHARE_THEME));
        this.mResolverActivity.getWindow().addFlags(16777216);
        this.mResolverActivity.getWindow().addFlags(Integer.MIN_VALUE);
        Parcelable targetParcelable = this.mResolverActivity.getIntent().getParcelableExtra("android.intent.extra.INTENT");
        if (targetParcelable == null || !(targetParcelable instanceof Intent)) {
            this.mResolverActivity.overridePendingTransition(201981974, 0);
        } else {
            if (!OplusResolverUtils.asFollowerPanel(this.mResolverActivity) || this.mClickX == -1 || this.mClickY == -1) {
                this.mResolverActivity.overridePendingTransition(201981974, 0);
            } else {
                this.mResolverActivity.overridePendingTransition(201982007, 0);
            }
        }
        this.mCustomIconHelper = new OplusResolverCustomIconHelper(this.mResolverActivity, 0);
        this.mResolverActivity.registerComponentCallbacks(this.mComponentCallbacks);
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void onResume() {
        OplusResolverDialogHelper oplusResolverDialogHelper = this.mResolverDialogHelper;
        if (oplusResolverDialogHelper != null) {
            oplusResolverDialogHelper.oShareResume();
        }
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void onPause() {
        OplusResolverDialogHelper oplusResolverDialogHelper = this.mResolverDialogHelper;
        if (oplusResolverDialogHelper != null) {
            oplusResolverDialogHelper.oSharePause();
        }
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void onDestroy() {
        OplusResolverDialogHelper oplusResolverDialogHelper = this.mResolverDialogHelper;
        if (oplusResolverDialogHelper != null) {
            oplusResolverDialogHelper.dialogHelperDestroy();
            this.mResolverDialogHelper.removeProfileSelectedListener(this.mContentPanel);
            this.mResolverDialogHelper.removeProfileSelectedListener(this.mTabListener);
        }
        OplusResolverInfoHelper oplusResolverInfoHelper = this.mResolveInfoHelper;
        if (oplusResolverInfoHelper != null) {
            oplusResolverInfoHelper.clearWhiteResolverInfo();
        }
        OplusResolverApkPreView oplusResolverApkPreView = this.mApkPreView;
        if (oplusResolverApkPreView != null) {
            oplusResolverApkPreView.onDestroy();
        }
        Activity activity = this.mResolverActivity;
        if (activity != null) {
            activity.unregisterComponentCallbacks(this.mComponentCallbacks);
        }
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void initActionSend() {
        String actionStr = getTargetIntent().getAction();
        this.mIsActionSend = actionStr != null && (actionStr.equalsIgnoreCase("android.intent.action.SEND") || actionStr.equalsIgnoreCase("android.intent.action.SEND_MULTIPLE")) && getTargetIntent().getPackage() == null;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public boolean isLastChosen() {
        return this.mOpenFlag;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void initView(boolean safeForwardingMode, boolean supportsAlwaysUseOption) {
        initDialogHelper(supportsAlwaysUseOption);
        Boolean isTabLoaded = (Boolean) OplusResolverUtils.invokeMethod(getActiveResolverAdapter(), "isTabLoaded", new Object[0]);
        if (getTargetIntent().getBooleanExtra("oplus_drag2Sharing_flag", false) && ((isTabLoaded != null && isTabLoaded.booleanValue() && getDisplayList().isEmpty()) || shouldShowEmptyState())) {
            Toast.makeText(this.mResolverActivity, 201588964, 0).show();
            this.mResolverActivity.finish();
        } else {
            updateView(safeForwardingMode, supportsAlwaysUseOption);
            this.mLoadedPages.add(Integer.valueOf(getCurrentPage()));
            this.mResolverDialogHelper.initChooserTopBroadcast();
        }
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void updateView(boolean safeForwardingMode, boolean supportsAlwaysUseOption) {
        OplusResolverDialogHelper oplusResolverDialogHelper;
        if (this.mResolveInfoHelper == null || (oplusResolverDialogHelper = this.mResolverDialogHelper) == null || this.mContentPanel != null) {
            return;
        }
        View view = oplusResolverDialogHelper.createView(getActiveUserHandle().equals(OplusResolverUtils.invokeMethod(this.mResolverActivity, "getWorkProfileUserHandle", new Object[0])), getActiveUserHandle(), safeForwardingMode, getPlaceholderCount());
        IChooserActivityWrapper iChooserActivityWrapper = (IChooserActivityWrapper) OplusResolverUtils.invokeMethod(this.mResolverActivity, "getChooserWrapper", new Object[0]);
        if (iChooserActivityWrapper != null) {
            ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.datePicker);
            this.mPreviewContainer = viewGroup;
            this.mPreView = iChooserActivityWrapper.getCreateContentPreviewView(viewGroup);
            initMultiappCheckBoxIfNeed(view);
        }
        TextView closeIcon = (TextView) view.findViewById(201457835);
        if (closeIcon != null) {
            closeIcon.setOnClickListener(new View.OnClickListener() { // from class: com.android.internal.app.OplusResolverManager$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    OplusResolverManager.this.lambda$updateView$0(view2);
                }
            });
        }
        this.mRootView = view;
        setContentView(view);
        this.mOpenFlag = this.mResolverDialogHelper.initCheckBox(this.mResolverActivity.getIntent(), view, supportsAlwaysUseOption);
        this.mResolverDialogHelper.showRecommend(this.mResolverActivity);
        ItemClickListener clickListener = new ItemClickListener();
        this.mResolverDialogHelper.setOnItemClickListener(clickListener);
        this.mResolverDialogHelper.setIsSplitScreenMode(this.mResolverActivity.isInMultiWindowMode());
        if (this.mIsActionSend) {
            TextView resolverTitle = (TextView) view.findViewById(201457856);
            if (resolverTitle != null) {
                resolverTitle.setVisibility(8);
            }
            this.mResolverDialogHelper.setOnItemLongClickListener(clickListener);
        }
        changeOShareAndNfc(view, getCurrentPage());
        initWhiteResolveUI(view);
        if (OplusResolverUtils.invokeMethod(this.mResolverActivity, "getWorkProfileUserHandle", new Object[0]) != null && ResolverActivity.ENABLE_TABBED_VIEW) {
            this.mContentPanel.setIsUserMaxHeight(OplusResolverUtils.invokeMethod(this.mResolverActivity, "getChooserWrapper", new Object[0]) != null, view.findViewById(201457782));
            this.mResolverDialogHelper.addProfileSelectedListener(this.mContentPanel);
            OplusResolverDialogHelper oplusResolverDialogHelper2 = this.mResolverDialogHelper;
            TabChangeListener tabChangeListener = this.mTabListener;
            if (tabChangeListener == null) {
                tabChangeListener = new TabChangeListener(getMultiProfilePagerAdapter());
                this.mTabListener = tabChangeListener;
            }
            oplusResolverDialogHelper2.initTabView(view, tabChangeListener, getCurrentPage());
        }
        this.mTransparentBottomView = (ImageView) view.findViewById(201457808);
        this.mTabHost = (TabHost) view.findViewById(201457677);
        updateViewByConfigChanged(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateView$0(View v) {
        this.mResolverActivity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initWhiteResolveUI(View rootView) {
        if (!this.mIsActionSend && this.mResolveInfoHelper.getWhiteResolveInfo(getCurrentPage()) != null) {
            ViewStub whiteResolveLayout = (ViewStub) rootView.findViewById(201457852);
            if (this.mWhiteResolverLayout == null) {
                View inflate = whiteResolveLayout.inflate();
                this.mWhiteResolverLayout = inflate;
                ViewTreeObserver observer = inflate.getViewTreeObserver();
                observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.internal.app.OplusResolverManager.1
                    @Override // android.view.ViewTreeObserver.OnPreDrawListener
                    public boolean onPreDraw() {
                        if (OplusResolverManager.this.mWhiteResolverLayout.getWidth() > 0 && OplusResolverManager.this.mWhiteResolverLayout.getHeight() > 0) {
                            OplusResolverManager.this.mWhiteResolverLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                            OplusResolverManager oplusResolverManager = OplusResolverManager.this;
                            Drawable background = oplusResolverManager.getBackgroundDrawable(oplusResolverManager.mWhiteResolverLayout.getWidth(), OplusResolverManager.this.mWhiteResolverLayout.getHeight());
                            OplusResolverManager.this.mWhiteResolverLayout.setBackground(background);
                            return true;
                        }
                        return true;
                    }
                });
            }
            this.mWhiteResolverLayout.setVisibility(0);
            ImageView resolveIcon = (ImageView) this.mWhiteResolverLayout.findViewById(201457853);
            TextView componentName = (TextView) this.mWhiteResolverLayout.findViewById(201457855);
            ResolveInfo resolveInfo = this.mResolveInfoHelper.getWhiteResolveInfo(getCurrentPage()).getResolveInfo();
            OplusItem oplusItem = getOplusItem(resolveInfo, getTargetIntent());
            if (oplusItem != null) {
                resolveIcon.setImageDrawable(oplusItem.getIcon());
                if (oplusItem.getText() != null) {
                    componentName.setText(this.mResolverActivity.getResources().getString(201589231, oplusItem.getText()));
                }
            }
            this.mWhiteResolverLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.internal.app.OplusResolverManager$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    OplusResolverManager.this.lambda$initWhiteResolveUI$1(view);
                }
            });
            return;
        }
        View view = this.mWhiteResolverLayout;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initWhiteResolveUI$1(View v) {
        handleClickEvent(this.mResolveInfoHelper.getWhiteResolveInfo(getCurrentPage()));
    }

    private OplusItem getOplusItem(ResolveInfo resolveInfo, Intent intent) {
        if (this.mOplusItem == null) {
            PackageManager packageManager = this.mResolverActivity.getPackageManager();
            this.mOplusItem = this.mCustomIconHelper.getAppInfo(intent, resolveInfo, packageManager);
        }
        return this.mOplusItem;
    }

    private void initMultiappCheckBoxIfNeed(View view) {
        final String packageName;
        if (getTargetIntent() != null && getTargetIntent().getComponent() != null) {
            packageName = getTargetIntent().getComponent().getPackageName();
        } else {
            packageName = "";
        }
        boolean needInit = OplusMultiAppManager.getInstance().isMultiApp(999, packageName);
        if (!needInit) {
            return;
        }
        View multiappCheckBoxContainer = view.findViewById(201457829);
        if (multiappCheckBoxContainer instanceof ViewStub) {
            ((ViewStub) multiappCheckBoxContainer).inflate();
        }
        CheckBox multiappAlwaysOption = (CheckBox) view.findViewById(201457828);
        if (multiappAlwaysOption != null) {
            multiappAlwaysOption.setOnClickListener(new View.OnClickListener() { // from class: com.android.internal.app.OplusResolverManager$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    OplusResolverManager.this.lambda$initMultiappCheckBoxIfNeed$2(packageName, view2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initMultiappCheckBoxIfNeed$2(String packageName, View v) {
        Toast.makeText(this.mResolverActivity, 201589183, 0).show();
        OplusMultiAppManager.getInstance().setMultiAppAccessMode(packageName, 1);
        List<Class> paramCls = new ArrayList<>();
        paramCls.add(Integer.TYPE);
        paramCls.add(Boolean.TYPE);
        paramCls.add(Boolean.TYPE);
        OplusResolverUtils.invokeMethod(this.mResolverActivity, "startSelected", paramCls, 0, false, false);
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mResolverActivity != null) {
            getCurrentIsMainScreen();
            Log.d(TAG, WallpaperServiceExtImpl.REASON_CONFIG_CHANGE);
            updatePreviewArea();
            OplusResolverUtils.invokeMethod(getActiveResolverAdapter(), "handlePackagesChanged", new Object[0]);
            updateViewByConfigChanged(newConfig);
            updateMiniBackground();
        }
    }

    private void getCurrentIsMainScreen() {
        boolean z = false;
        this.mIsMainScreen = false;
        try {
            if (Settings.Global.getInt(this.mResolverActivity.getContentResolver(), "oplus_system_folding_mode") == 1 && !isDragonflyFoldScreen()) {
                z = true;
            }
            this.mIsMainScreen = z;
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "get value error" + e);
        }
        Log.d(TAG, "isMainScreen=" + this.mIsMainScreen);
    }

    private void updateViewByConfigChanged(Configuration newConfig) {
        boolean show;
        if (this.mResolverDialogHelper == null || this.mTabHost == null || this.mTransparentBottomView == null) {
            Log.d(TAG, "the view not init");
            return;
        }
        Configuration config = newConfig == null ? this.mResolverActivity.getResources().getConfiguration() : newConfig;
        this.mResolverDialogHelper.setIsSplitScreenMode(this.mResolverActivity.isInMultiWindowMode());
        Display display = ((WindowManager) this.mResolverActivity.getSystemService("window")).getDefaultDisplay();
        int orientation = display.getRotation();
        if (this.mIsMainScreen) {
            show = !this.mResolverActivity.isInMultiWindowMode() || config.orientation == 2;
        } else {
            show = !this.mResolverActivity.isInMultiWindowMode() && config.orientation == 2;
        }
        Log.d(TAG, "orientation=" + orientation);
        boolean isGestureNavMode = Settings.Secure.getInt(this.mResolverActivity.getContentResolver(), NAVIGATION_MODE, 0) == 2;
        this.mResolverActivity.getWindow().setDecorFitsSystemWindows(this.mIsMainScreen || !this.mResolverActivity.isInMultiWindowMode() || orientation == 0 || orientation == 2 || !isGestureNavMode);
        Activity activity = this.mResolverActivity;
        if (OplusResolverUtils.calculateNotSmallScreen(activity, activity.getResources().getConfiguration())) {
            Log.d(TAG, "isTablet");
            show = true;
            this.mResolverActivity.getWindow().setDecorFitsSystemWindows(true);
        }
        if (OplusResolverUtils.asFollowerPanel(this.mResolverActivity) && this.mClickX != -1 && this.mClickY != -1) {
            setRadiusBackground(true);
        } else {
            showTransparentBottomView(show);
        }
        Log.d(TAG, "show=" + show);
        this.mContentPanel.setConfiguration(this.mIsMainScreen, config, this.mResolverActivity.isInMultiWindowMode(), show);
    }

    private void setRadiusBackground(boolean show) {
        if (show) {
            this.mTabHost.setBackground(this.mResolverActivity.getDrawable(201851020));
        } else {
            this.mTabHost.setBackgroundColor(this.mResolverActivity.getColor(201719864));
        }
    }

    private void showTransparentBottomView(boolean show) {
        int i;
        if (show) {
            int navigationBarEnableStatus = Settings.Secure.getIntForUser(this.mResolverActivity.getContentResolver(), OplusViewRootUtil.KEY_NAVIGATIONBAR_MODE, 0, -2);
            this.mTabHost.setBackground(this.mResolverActivity.getDrawable(201851020));
            ViewGroup.LayoutParams params = this.mTransparentBottomView.getLayoutParams();
            Resources resources = this.mResolverActivity.getResources();
            if (navigationBarEnableStatus != 0) {
                i = 201654471;
            } else {
                i = 201654469;
            }
            params.height = resources.getDimensionPixelSize(i);
            this.mTransparentBottomView.setLayoutParams(params);
            this.mTransparentBottomView.setVisibility(0);
            return;
        }
        this.mTabHost.setBackgroundColor(this.mResolverActivity.getColor(201719864));
        this.mTransparentBottomView.setVisibility(8);
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void setResolverContent() {
        if (this.mResolverDialogHelper == null || this.mResolverActivity.isFinishing() || this.mResolverActivity.getWindow().getDecorView() == null) {
            return;
        }
        if (OplusResolverUtil.isChooserCtsTest(this.mResolverActivity, getTargetIntent())) {
            try {
                new OplusChooserCtsConnection().testCts(this.mResolverActivity, this, getActiveResolverAdapter(), getActiveUserHandle());
            } catch (Exception e) {
            }
        }
        List<ResolveInfo> resolveInfos = getResolveInfoList(getDisplayList());
        OplusResolverUtil.sortCtsTest(this.mResolverActivity, getTargetIntent(), resolveInfos);
        resortListAndNotifyChange(resolveInfos);
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void initPreferenceAndPinList() {
        if (this.mResolveInfoHelper == null) {
            return;
        }
        this.mPinnedSharedPrefs = getPinnedSharedPrefs(this.mResolverActivity);
        boolean isChoose = OplusResolverIntentUtil.isChooserAction(getTargetIntent());
        String type = OplusResolverIntentUtil.getIntentType(getTargetIntent());
        if (isChoose && "gallery".equals(type)) {
            this.mGalleryPinList = Settings.Secure.getString(this.mResolverActivity.getContentResolver(), GALLERY_PIN_LIST);
        }
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void statisticsData(ResolveInfo ri, int which) {
        String referrerPackage = (String) OplusResolverUtils.invokeMethod(this.mResolverActivity, "getReferrerPackageName", new Object[0]);
        OplusResolverInfoHelper oplusResolverInfoHelper = this.mResolveInfoHelper;
        if (oplusResolverInfoHelper != null) {
            oplusResolverInfoHelper.statisticsData(ri, getTargetIntent(), which, referrerPackage);
        }
    }

    @Override // com.android.internal.app.IOplusResolverManager
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0128, code lost:
    
        if (r8.equals("jar") != false) goto L86;
     */
    @Override // com.android.internal.app.IOplusResolverManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getChooserPreFileSingleIconView(boolean singleFile, String fileName, Uri uri) {
        char c;
        if (!singleFile) {
            return 201850988;
        }
        String type = this.mResolverActivity.getContentResolver().getType(uri);
        Intent intent = new Intent();
        intent.setType(type);
        int iconRes = 201850989;
        String preType = OplusResolverIntentUtil.getIntentType(intent);
        char c2 = 7;
        switch (preType.hashCode()) {
            case 110834:
                if (preType.equals(OplusResolverIntentUtil.DEFAULT_APP_PDF)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 111220:
                if (preType.equals(OplusResolverIntentUtil.DEFAULT_APP_PPT)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 115312:
                if (preType.equals(OplusResolverIntentUtil.DEFAULT_APP_TEXT)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 3655434:
                if (preType.equals(OplusResolverIntentUtil.DEFAULT_APP_WORD)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 93166550:
                if (preType.equals(OplusResolverIntentUtil.DEFAULT_APP_AUDIO)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 96948919:
                if (preType.equals(OplusResolverIntentUtil.DEFAULT_APP_EXCEL)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 112202875:
                if (preType.equals(OplusResolverIntentUtil.DEFAULT_APP_VIDEO)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1554253136:
                if (preType.equals(OplusResolverIntentUtil.DEFAULT_APP_APPLICATION)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                iconRes = 201850992;
                break;
            case 1:
                iconRes = 201850983;
                break;
            case 2:
                iconRes = 201850985;
                break;
            case 3:
                iconRes = 201850990;
                break;
            case 4:
                iconRes = 201850991;
                break;
            case 5:
                iconRes = 201850980;
                break;
            case 6:
                iconRes = 201850979;
                break;
            case 7:
                iconRes = 201851032;
                break;
        }
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex != -1) {
            String suffix = fileName.substring(suffixIndex + 1).toLowerCase();
            switch (suffix.hashCode()) {
                case 1827:
                    if (suffix.equals("7z")) {
                        c2 = 6;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 96796:
                    if (suffix.equals(SplitConstants.KEY_APK)) {
                        c2 = 2;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 104089:
                    if (suffix.equals("ics")) {
                        c2 = 11;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 104987:
                    break;
                case 107421:
                    if (suffix.equals("lrc")) {
                        c2 = 4;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 112675:
                    if (suffix.equals("rar")) {
                        c2 = 1;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 116569:
                    if (suffix.equals("vcf")) {
                        c2 = '\n';
                        break;
                    }
                    c2 = 65535;
                    break;
                case 120609:
                    if (suffix.equals("zip")) {
                        c2 = 0;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 3120248:
                    if (suffix.equals("epub")) {
                        c2 = 5;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 3213227:
                    if (suffix.equals("html")) {
                        c2 = 3;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 3623755:
                    if (suffix.equals("vmsg")) {
                        c2 = '\t';
                        break;
                    }
                    c2 = 65535;
                    break;
                case 110327241:
                    if (suffix.equals("theme")) {
                        c2 = '\b';
                        break;
                    }
                    c2 = 65535;
                    break;
                default:
                    c2 = 65535;
                    break;
            }
            switch (c2) {
                case 0:
                    return 201850982;
                case 1:
                    return 201850981;
                case 2:
                    return 201850979;
                case 3:
                    return 201850986;
                case 4:
                    return 201850987;
                case 5:
                    return 201850984;
                case 6:
                    return 201851035;
                case 7:
                    return 201851036;
                case '\b':
                    return 201851031;
                case '\t':
                    return 201851033;
                case '\n':
                    return 201851030;
                case 11:
                    return 201851029;
                default:
                    return iconRes;
            }
        }
        return iconRes;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public boolean tryApkResourceName(Uri uri, ImageView imageView, TextView textView) {
        if (imageView == null || textView == null || imageView.getVisibility() != 0 || textView.getVisibility() != 0) {
            return false;
        }
        String type = this.mResolverActivity.getContentResolver().getType(uri);
        Intent intent = new Intent();
        intent.setType(type);
        String preType = OplusResolverIntentUtil.getIntentType(intent);
        if (!OplusResolverIntentUtil.DEFAULT_APP_APPLICATION.equals(preType)) {
            return false;
        }
        Log.d(TAG, "try apk preview");
        OplusResolverApkPreView oplusResolverApkPreView = new OplusResolverApkPreView(this.mResolverActivity);
        this.mApkPreView = oplusResolverApkPreView;
        oplusResolverApkPreView.execute(uri, imageView, textView);
        return true;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public ViewGroup getDisplayFileContentPreview(LayoutInflater layoutInflater, ViewGroup parent) {
        View fileLayout;
        ViewGroup view = (ViewGroup) layoutInflater.inflate(201917468, parent, false);
        if (view != null && hasNotOShareOrNearby() && (fileLayout = view.findViewById(R.id.date_picker_header_date)) != null) {
            fileLayout.setPadding(fileLayout.getPaddingStart(), fileLayout.getPaddingTop(), fileLayout.getPaddingRight(), 0);
        }
        return view;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public ViewGroup getDisplayImageContentPreview(LayoutInflater layoutInflater, ViewGroup parent) {
        ViewGroup view = (ViewGroup) layoutInflater.inflate(201917469, parent, false);
        if (view != null && hasNotOShareOrNearby()) {
            view.setPadding(view.getPaddingStart(), view.getPaddingTop(), view.getPaddingRight(), 0);
        }
        return view;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public ViewGroup getDisplayTextContentPreview(LayoutInflater layoutInflater, ViewGroup parent) {
        ViewGroup view = (ViewGroup) layoutInflater.inflate(201917470, parent, false);
        if (view != null && hasNotOShareOrNearby()) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = 0;
                view.setLayoutParams(layoutParams);
            }
        }
        return view;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void clearInactiveProfileCache(int page) {
        if (this.mLoadedPages.size() == 1) {
            return;
        }
        this.mLoadedPages.remove(Integer.valueOf(1 - page));
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void showActiveEmptyViewState() {
        if (shouldShowEmptyState()) {
            showEmptyViewState();
        }
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void restoreProfilePager(int pageNumber) {
        Activity activity;
        OplusResolverDialogHelper oplusResolverDialogHelper = this.mResolverDialogHelper;
        if (oplusResolverDialogHelper != null && (activity = this.mResolverActivity) != null) {
            oplusResolverDialogHelper.restoreProfilePager(activity.getWindow().getDecorView(), pageNumber);
        }
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public ViewGroup getChooserProfileDescriptor(LayoutInflater inflater) {
        return (ViewGroup) inflater.inflate(201917472, (ViewGroup) null, false);
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public ViewGroup getResolverProfileDescriptor(LayoutInflater inflater) {
        return (ViewGroup) inflater.inflate(201917471, (ViewGroup) null, false);
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void displayTextAddActionButton(ViewGroup actionRow, final View.OnClickListener listener) {
        TextView copyButton = new TextView(this.mResolverActivity);
        copyButton.setText(this.mResolverActivity.getResources().getString(201589203));
        copyButton.setTextSize(CHOOSER_PREVIEW_COPY_BUTTON_TEXTSIZE);
        copyButton.setMinHeight(this.mResolverActivity.getResources().getDimensionPixelSize(201654495));
        copyButton.setMinWidth(this.mResolverActivity.getResources().getDimensionPixelSize(201654494));
        copyButton.setBackground(this.mResolverActivity.getDrawable(201851037));
        copyButton.setTextColor(this.mResolverActivity.getColor(201719895));
        copyButton.setGravity(17);
        copyButton.setForceDarkAllowed(false);
        actionRow.addView(copyButton);
        actionRow.setPadding(0, 0, 0, 0);
        copyButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.internal.app.OplusResolverManager$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                OplusResolverManager.this.lambda$displayTextAddActionButton$3(listener, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayTextAddActionButton$3(View.OnClickListener listener, View view) {
        Toast.makeText(this.mResolverActivity, 201588936, 0).show();
        listener.onClick(view);
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void setCustomRoundImage(Paint roundRectPaint, Paint textPaint, Paint overlayPaint) {
        roundRectPaint.setStrokeWidth(this.mResolverActivity.getResources().getDimensionPixelSize(201654423));
        textPaint.setTextSize(this.mResolverActivity.getResources().getDimensionPixelSize(201654420));
        overlayPaint.setColor(this.mResolverActivity.getResources().getColor(201719870));
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public int getCornerRadius(Context context) {
        return context.getResources().getDimensionPixelSize(201654419);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable getBackgroundDrawable(int width, int height) {
        int radius = this.mResolverActivity.getResources().getDimensionPixelOffset(201654425);
        int backgroundColor = this.mResolverActivity.getResources().getColor(201719864);
        int foregroundColor = this.mResolverActivity.getResources().getColor(201719871);
        return OplusRoundRectUtil.getInstance().getRoundRectDrawable(width, height, radius, backgroundColor, foregroundColor);
    }

    private void displayTextSuitSize(int suitSize, TextView... childs) {
        float fontScale = this.mResolverActivity.getResources().getConfiguration().fontScale;
        for (TextView child : childs) {
            if (child == null) {
                return;
            }
            float textSize = child.getTextSize();
            child.setTextSize(0, (int) OplusChangeTextUtil.getSuitableFontSize(textSize, fontScale, suitSize));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeOShareAndNfc(View view, int position) {
        if (position == 1) {
            this.mResolverDialogHelper.tearDown(view);
        } else if (this.mIsActionSend) {
            this.mResolverDialogHelper.initOShareView(view);
            this.mResolverActivity.getWindow().getDecorView().post(new Runnable() { // from class: com.android.internal.app.OplusResolverManager$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    OplusResolverManager.this.lambda$changeOShareAndNfc$4();
                }
            });
        } else {
            this.mResolverDialogHelper.initNfcView(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$changeOShareAndNfc$4() {
        this.mResolverDialogHelper.initOShareService();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class TabChangeListener implements OplusResolverDialogHelper.OnProfileSelectedListener {
        private Object mMultiProfilePagerAdapter;

        TabChangeListener(Object multiProfilePagerAdapter) {
            this.mMultiProfilePagerAdapter = multiProfilePagerAdapter;
        }

        @Override // com.oplus.resolver.OplusResolverDialogHelper.OnProfileSelectedListener
        public void onProfileSelected(int position) {
            OplusResolverManager.this.setCurrentPage(position);
            OplusResolverManager oplusResolverManager = OplusResolverManager.this;
            oplusResolverManager.changeOShareAndNfc(oplusResolverManager.mResolverActivity.getWindow().getDecorView(), position);
            OplusResolverManager oplusResolverManager2 = OplusResolverManager.this;
            oplusResolverManager2.initWhiteResolveUI(oplusResolverManager2.mRootView);
            if (OplusResolverManager.this.mLoadedPages.contains(Integer.valueOf(position))) {
                if (!OplusResolverManager.this.shouldShowEmptyState()) {
                    OplusResolverManager.this.setResolverContent();
                    return;
                } else {
                    OplusResolverManager.this.showEmptyViewState();
                    return;
                }
            }
            OplusResolverManager.this.mLoadedPages.add(Integer.valueOf(position));
            List<Class> paramCls = new ArrayList<>();
            paramCls.add(Boolean.TYPE);
            OplusResolverUtils.invokeMethod(this.mMultiProfilePagerAdapter, "rebuildActiveTab", paramCls, true);
            if (OplusResolverManager.this.shouldShowEmptyState()) {
                OplusResolverManager.this.showEmptyViewState();
            } else {
                OplusResolverManager.this.mResolverDialogHelper.reLoadTabPlaceholderCount(OplusResolverManager.this.getActiveUserHandle().equals(OplusResolverUtils.invokeMethod(OplusResolverManager.this.mResolverActivity, "getWorkProfileUserHandle", new Object[0])), OplusResolverManager.this.getActiveUserHandle(), OplusResolverManager.this.getPlaceholderCount());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldShowEmptyState() {
        int currentPage = getCurrentPage();
        if (currentPage == -1) {
            return true;
        }
        List<Class> paramCls = new ArrayList<>();
        paramCls.add(Integer.TYPE);
        Object descriptor = OplusResolverUtils.invokeMethod(getMultiProfilePagerAdapter(), "getItem", paramCls, Integer.valueOf(currentPage));
        View view = (View) OplusResolverUtils.getFiledValue(descriptor, "rootView");
        return view == null || view.findViewById(R.id.single).getVisibility() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEmptyViewState() {
        View rootView;
        int currentPage = getCurrentPage();
        if (currentPage == -1) {
            return;
        }
        List<Class> paramCls = new ArrayList<>();
        paramCls.add(Integer.TYPE);
        Object descriptor = OplusResolverUtils.invokeMethod(getMultiProfilePagerAdapter(), "getItem", paramCls, Integer.valueOf(currentPage));
        ViewGroup parent = (ViewGroup) this.mResolverActivity.findViewById(R.id.tabcontent);
        if (parent == null || (rootView = (View) OplusResolverUtils.getFiledValue(descriptor, "rootView")) == null) {
            return;
        }
        Object drawable = ((ImageView) rootView.findViewById(R.id.singleTask)).getDrawable();
        if (parent.getChildCount() == 2) {
            if (parent.getChildAt(1) != rootView) {
                Object drawable2 = ((ImageView) parent.getChildAt(1).findViewById(R.id.singleTask)).getDrawable();
                if (drawable2 != null && (drawable2 instanceof Animatable) && ((Animatable) drawable2).isRunning()) {
                    ((Animatable) drawable2).stop();
                }
                parent.removeViewAt(1);
            } else {
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                    return;
                }
                return;
            }
        } else {
            parent.getChildAt(0).setVisibility(8);
        }
        parent.addView(rootView);
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPlaceholderCount() {
        IResolverListAdapterWrapper iResolverListAdapterWrapper = (IResolverListAdapterWrapper) OplusResolverUtils.invokeMethod(getActiveResolverAdapter(), "getWrapper", new Object[0]);
        if (iResolverListAdapterWrapper == null) {
            return 0;
        }
        IResolverListAdapterExt resolverListAdapterExt = iResolverListAdapterWrapper.getResolverListAdapterExt();
        List<ResolveInfo> resolveInfos = getUnsortResolveInfo(resolverListAdapterExt.getPlaceholderResolveList());
        int apkSubContractSize = this.mResolveInfoHelper.getApkSubContractListSize(resolveInfos, OplusResolverIntentUtil.getIntentType(getTargetIntent()));
        int size = this.mResolveInfoHelper.getExpandSizeWithoutMoreIcon(resolveInfos, getTargetIntent());
        if (!OplusResolverIntentUtil.isChooserAction(getTargetIntent()) && size > 0 && size < resolveInfos.size()) {
            return (size + 1) - apkSubContractSize;
        }
        return resolveInfos.size() - apkSubContractSize;
    }

    private Object getMultiProfilePagerAdapter() {
        return this.mResolverActivityExt.getMultiProfilePagerAdapter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BaseAdapter getActiveResolverAdapter() {
        return (BaseAdapter) OplusResolverUtils.invokeMethod(getMultiProfilePagerAdapter(), "getActiveListAdapter", new Object[0]);
    }

    private int getCurrentPage() {
        Integer currentPage = (Integer) OplusResolverUtils.invokeMethod(getMultiProfilePagerAdapter(), "getCurrentPage", new Object[0]);
        if (currentPage == null) {
            return -1;
        }
        return currentPage.intValue();
    }

    private List getDisplayList() {
        return (List) OplusResolverUtils.getFiledValue(getActiveResolverAdapter(), "mDisplayList");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserHandle getActiveUserHandle() {
        return (UserHandle) OplusResolverUtils.invokeMethod(getActiveResolverAdapter(), "getUserHandle", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentPage(int page) {
        IAbstractMultiProfilePagerAdapterWrapper wrapper = (IAbstractMultiProfilePagerAdapterWrapper) OplusResolverUtils.invokeMethod(getMultiProfilePagerAdapter(), "getWrapper", new Object[0]);
        if (wrapper != null) {
            wrapper.setCurrentPage(page);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Intent getTargetIntent() {
        return (Intent) OplusResolverUtils.invokeMethod(this.mResolverActivity, "getTargetIntent", new Object[0]);
    }

    private List<ResolveInfo> getUnsortResolveInfo(List componentInfos) {
        List<ResolveInfo> resolveInfos = new ArrayList<>();
        boolean hasProcessSort = true;
        if (getDisplayList() == null || getDisplayList().isEmpty()) {
            hasProcessSort = false;
            List<Class> paramCls = new ArrayList<>();
            paramCls.add(List.class);
            paramCls.add(Boolean.TYPE);
            OplusResolverUtils.invokeMethod(getActiveResolverAdapter(), "processSortedList", paramCls, componentInfos, false);
        }
        if (getDisplayList() != null && !getDisplayList().isEmpty()) {
            for (Object resolveInfo : getDisplayList()) {
                if (resolveInfo != null) {
                    Object result = OplusResolverUtils.invokeMethod(resolveInfo, "getResolveInfo", new Object[0]);
                    if (result instanceof ResolveInfo) {
                        resolveInfos.add((ResolveInfo) result);
                    }
                }
            }
        }
        if (!hasProcessSort && getDisplayList() != null) {
            getDisplayList().clear();
        }
        return resolveInfos;
    }

    public void resortListAndNotifyChange() {
        resortListAndNotifyChange(getResolveInfoList(getDisplayList()));
    }

    private void resortListAndNotifyChange(List<ResolveInfo> resolveInfos) {
        if (!shouldShowEmptyState() && !getDisplayList().isEmpty()) {
            ViewGroup parent = (ViewGroup) this.mResolverActivity.findViewById(R.id.tabcontent);
            if (parent.getChildCount() == 2) {
                parent.getChildAt(0).setVisibility(0);
                parent.removeViewAt(1);
            }
            this.mResolverDialogHelper.resortListAndNotifyChange(getActiveUserHandle().equals(OplusResolverUtils.invokeMethod(this.mResolverActivity, "getWorkProfileUserHandle", new Object[0])), getActiveUserHandle(), resolveInfos, getCurrentPage());
            initWhiteResolveUI(this.mRootView);
            resortDisplayList(this.mResolverDialogHelper.getResolveInforList());
            return;
        }
        if (shouldShowEmptyState()) {
            showEmptyViewState();
        }
    }

    private void resortDisplayList(List<ResolveInfo> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            List displayList = getDisplayList();
            for (ResolveInfo resolveInfo : list) {
                for (Object dresolveInfo : displayList) {
                    if (resolveInfo.equals(OplusResolverUtils.invokeMethod(dresolveInfo, "getResolveInfo", new Object[0]))) {
                        arrayList.add(dresolveInfo);
                    }
                }
            }
            displayList.clear();
            displayList.addAll(arrayList);
        }
    }

    private void initDialogHelper(boolean supportsAlwaysUseOption) {
        this.mResolveInfoHelper = OplusResolverInfoHelper.getInstance(this.mResolverActivity);
        this.mResolverDialogHelper = new OplusResolverDialogHelper(this.mResolverActivity, getTargetIntent(), null, supportsAlwaysUseOption, getResolveInfoList(getDisplayList()), getCurrentPage());
    }

    private List<ResolveInfo> getResolveInfoList(List drlist) {
        List<ResolveInfo> mRiList = new ArrayList<>();
        if (drlist != null) {
            mRiList.clear();
            for (Object resolve : drlist) {
                Object resolveInfo = OplusResolverUtils.invokeMethod(resolve, "getResolveInfo", new Object[0]);
                if (resolveInfo instanceof ResolveInfo) {
                    if (resolve instanceof DisplayResolveInfo) {
                        this.mResolveInfoHelper.setWhiteResolveInfo((DisplayResolveInfo) resolve, getTargetIntent(), getCurrentPage());
                    }
                    mRiList.add((ResolveInfo) resolveInfo);
                }
            }
        }
        return mRiList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ItemClickListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        ItemClickListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OplusResolverManager.this.handleClickEvent(view, position);
        }

        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (OplusResolverManager.this.mIsActionSend) {
                List<ResolveInfo> resolveInfoList = OplusResolverManager.this.mResolverDialogHelper.getResolveInforList();
                if (resolveInfoList == null || resolveInfoList.size() <= position) {
                    Log.e(OplusResolverManager.TAG, "onItemLongClick size error");
                    return true;
                }
                ResolveInfo ri = resolveInfoList.get(position);
                String type = OplusResolverIntentUtil.getIntentType(OplusResolverManager.this.getTargetIntent());
                OplusResolverManager.this.mResolverDialogHelper.showTargetDetails(view, ri, OplusResolverManager.this.mPinnedSharedPrefs, type, OplusResolverManager.this.getActiveResolverAdapter());
            }
            return true;
        }
    }

    private void handleClickEvent(DisplayResolveInfo displayResolveInfo) {
        if (displayResolveInfo == null) {
            Log.e(TAG, "handleClickEvent displayResolveInfo is null, so return");
            return;
        }
        boolean always = false;
        CheckBox alwaysOption = (CheckBox) this.mResolverActivity.findViewById(R.id.clip_children_set_tag);
        if (alwaysOption != null && alwaysOption.getVisibility() == 0) {
            always = alwaysOption.isChecked();
        }
        if (this.mResolverActivity.isFinishing()) {
            Log.d(TAG, "resolverActivity is finishing, so return");
            return;
        }
        this.mResolverActivityExt.statisticsData(displayResolveInfo.getResolveInfo(), -1);
        if (this.mResolverActivity.onTargetSelected(displayResolveInfo, always)) {
            this.mResolverActivity.finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleClickEvent(View view, int position) {
        List displayList = getDisplayList();
        if (displayList == null || displayList.size() <= position) {
            Log.e(TAG, "handleClickEvent size error");
            return;
        }
        if (OplusResolverUtil.isResolverCtsTest(this.mResolverActivity, getTargetIntent(), (ResolveInfo) OplusResolverUtils.invokeMethod(getDisplayList().get(position), "getResolveInfo", new Object[0]))) {
            this.mLastSelected = position;
            setButtonAlwaysListener();
            return;
        }
        boolean always = false;
        CheckBox alwaysOption = (CheckBox) this.mResolverActivity.findViewById(R.id.clip_children_set_tag);
        if (alwaysOption != null && alwaysOption.getVisibility() == 0) {
            always = alwaysOption.isChecked();
        }
        ResolveInfo resolveInfo = (ResolveInfo) OplusResolverUtils.invokeMethod(displayList.get(position), "getResolveInfo", new Object[0]);
        if (resolveInfo != null && resolveInfo.activityInfo != null) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (OplusOShareUtil.isOsharePackage(this.mResolverActivity, packageName)) {
                if (!ApplicationEnableUtil.applicationEnable(this.mResolverActivity.getPackageManager(), packageName)) {
                    ApplicationEnableUtil.showApplicationEnableDialog(this.mResolverActivity, packageName, new DialogInterface.OnClickListener() { // from class: com.android.internal.app.OplusResolverManager$$ExternalSyntheticLambda8
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            OplusResolverManager.this.lambda$handleClickEvent$5(dialogInterface, i);
                        }
                    });
                    return;
                } else {
                    OplusOShareUtil.startOshareActivity(this.mResolverActivity, getTargetIntent());
                    return;
                }
            }
        }
        List<Class> paramCls = new ArrayList<>();
        paramCls.add(Integer.TYPE);
        paramCls.add(Boolean.TYPE);
        paramCls.add(Boolean.TYPE);
        OplusResolverUtils.invokeMethod(this.mResolverActivity, "startSelected", paramCls, Integer.valueOf(position), Boolean.valueOf(always), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClickEvent$5(DialogInterface dialog, int which) {
        OplusOShareUtil.startOshareActivity(this.mResolverActivity, getTargetIntent());
    }

    static SharedPreferences getPinnedSharedPrefs(Context context) {
        File prefsFile = new File(new File(Environment.getDataUserCePackageDirectory(StorageManager.UUID_PRIVATE_INTERNAL, context.getUserId(), context.getPackageName()), "shared_prefs"), "chooser_pin_settings.xml");
        return context.getSharedPreferences(prefsFile, 0);
    }

    private void setContentView(View view) {
        int i;
        int i2;
        this.mResolverActivity.setContentView(view);
        if (shouldShowEmptyState()) {
            showEmptyViewState();
        }
        OplusResolverDrawerLayout oplusResolverDrawerLayout = (OplusResolverDrawerLayout) this.mResolverActivity.findViewById(R.id.date);
        this.mContentPanel = oplusResolverDrawerLayout;
        oplusResolverDrawerLayout.setOnDismissedListener(new ResolverDrawerLayout.OnDismissedListener() { // from class: com.android.internal.app.OplusResolverManager$$ExternalSyntheticLambda2
            public final void onDismissed() {
                OplusResolverManager.this.lambda$setContentView$6();
            }
        });
        int anchorHeight = getTargetIntent().getIntExtra("start_chooser_anchor_height", 0);
        int anchorWidth = getTargetIntent().getIntExtra("start_chooser_anchor_width", 0);
        if (OplusResolverUtils.asFollowerPanel(this.mResolverActivity) && (i = this.mClickX) != -1 && (i2 = this.mClickY) != -1) {
            this.mContentPanel.updateAnchorCoordinate(i, i2, anchorHeight, anchorWidth);
        }
        getCurrentIsMainScreen();
        updatePreviewArea();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setContentView$6() {
        this.mResolverActivity.finish();
    }

    private Point getScreenSize() {
        Point point = new Point();
        WindowManager windowManager = (WindowManager) this.mResolverActivity.getSystemService("window");
        windowManager.getDefaultDisplay().getRealSize(point);
        return point;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.internal.app.OplusResolverManager$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements ComponentCallbacks {
        AnonymousClass2() {
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
            if (OplusResolverManager.this.mResolverActivity != null) {
                if (OplusResolverUtils.asFollowerPanel(OplusResolverManager.this.mResolverActivity) && OplusResolverManager.this.mClickX != -1 && OplusResolverManager.this.mClickY != -1 && configuration.orientation != OplusResolverManager.this.mLastOrientation && !OplusResolverManager.this.mResolverActivity.isFinishing()) {
                    OplusResolverManager.this.mResolverActivity.finish();
                    return;
                }
                OplusResolverManager.this.mResolverActivity.getWindow().getDecorView().post(new Runnable() { // from class: com.android.internal.app.OplusResolverManager$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusResolverManager.AnonymousClass2.this.lambda$onConfigurationChanged$0();
                    }
                });
            }
            if (OplusResolverManager.this.mResolverDialogHelper != null) {
                OplusResolverManager.this.mResolverDialogHelper.clearDrawableCache();
            }
            if (OplusResolverManager.this.mResolveInfoHelper != null) {
                OplusResolverManager.this.mResolveInfoHelper.clearWhiteResolverInfo();
            }
            OplusResolverManager.this.mOplusItem = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigurationChanged$0() {
            OplusResolverManager.this.updatePreviewArea();
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePreviewArea() {
        ViewGroup viewGroup = this.mPreviewContainer;
        if (viewGroup != null && this.mPreView != null) {
            viewGroup.setVisibility(0);
            if (this.mPreView.getParent() == null && !preViewAllGone(this.mPreView)) {
                this.mPreviewContainer.addView(this.mPreView, 0);
            }
        }
    }

    private void setButtonAlwaysListener() {
        CheckBox buttonAlways;
        if (this.mLastSelected != -1 && (buttonAlways = (CheckBox) this.mResolverActivity.findViewById(R.id.clip_children_set_tag)) != null && !buttonAlways.hasOnClickListeners()) {
            buttonAlways.setOnClickListener(new View.OnClickListener() { // from class: com.android.internal.app.OplusResolverManager$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    OplusResolverManager.this.lambda$setButtonAlwaysListener$7(view);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setButtonAlwaysListener$7(View view) {
        List<Class> paramCls = new ArrayList<>();
        paramCls.add(Integer.TYPE);
        paramCls.add(Boolean.TYPE);
        paramCls.add(Boolean.TYPE);
        OplusResolverUtils.invokeMethod(this.mResolverActivity, "startSelected", paramCls, Integer.valueOf(this.mLastSelected), true, false);
    }

    private boolean preViewAllGone(View parent) {
        if (parent == null || parent.getVisibility() == 8) {
            return true;
        }
        if (!(parent instanceof ViewGroup)) {
            return false;
        }
        int count = ((ViewGroup) parent).getChildCount();
        for (int i = 0; i < count; i++) {
            if (((ViewGroup) parent).getChildAt(i).getVisibility() != 8) {
                return false;
            }
        }
        return true;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public String getChooserPreFileName(Context context, int fileCount, String file) {
        return context.getResources().getQuantityString(202440710, fileCount, Integer.valueOf(fileCount));
    }

    private boolean isDragonflyFoldScreen() {
        return OplusFeatureConfigManager.getInstacne().hasFeature("oplus.hardware.type.fold") && OplusFeatureConfigManager.getInstacne().hasFeature("oplus.software.fold_remap_display_disabled");
    }

    private boolean hasNotOShareOrNearby() {
        if (OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_RESOLVER_SHARE_EMAIL)) {
            IChooserActivityWrapper wrapper = (IChooserActivityWrapper) OplusResolverUtils.invokeMethod(this.mResolverActivity, "getChooserWrapper", new Object[0]);
            if (wrapper == null) {
                return false;
            }
            return wrapper.shouldNearbyShareBeFirstInRankedRow() || wrapper.getNearbySharingTarget(getTargetIntent()) == null;
        }
        return OplusOShareUtil.isNoOshareApplication(this.mResolverActivity, getTargetIntent());
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public Bitmap orientationThumbnailBitmap(ContentResolver contentResolver, Uri uri, Bitmap originBitmap) {
        try {
            Cursor cursor = contentResolver.query(uri, new String[]{"orientation"}, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        float orientation = cursor.getFloat(0);
                        Log.d(TAG, "orientationThumbnailBitmap:" + orientation);
                        if (orientation != 0.0f) {
                            int width = originBitmap.getWidth();
                            int height = originBitmap.getHeight();
                            Matrix m = new Matrix();
                            m.setRotate(orientation, width / 2, height / 2);
                            Bitmap createBitmap = Bitmap.createBitmap(originBitmap, 0, 0, width, height, m, false);
                            if (cursor != null) {
                                cursor.close();
                            }
                            return createBitmap;
                        }
                    }
                } finally {
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "orientationThumbnailBitmap exception:" + e.getMessage());
        }
        return originBitmap;
    }

    @Override // com.android.internal.app.IOplusResolverManager
    public void configureMiniResolverContent() {
        this.mResolverActivity.setTheme(OplusThemeStyle.DEFAULT_DIALOG_THEME);
        this.mResolverActivity.requestWindowFeature(1);
        Activity activity = this.mResolverActivity;
        this.mLayoutId = 201917507;
        activity.setContentView(201917507);
        updateMiniBackground();
        this.mResolverActivity.getWindow().getDecorView().setOnTouchListener(new InsetTouchListener(this.mResolverActivity));
    }

    private void updateMiniBackground() {
        if (this.mLayoutId <= 0) {
            return;
        }
        final View rootView = this.mResolverActivity.findViewById(R.id.resolver_empty_state_title);
        View openButton = this.mResolverActivity.findViewById(R.id.clip_vertical);
        if (rootView != null) {
            this.mResolverActivity.getWindow().setNavigationBarContrastEnforced(false);
            Activity activity = this.mResolverActivity;
            if (!OplusResolverUtils.calculateNotSmallScreen(activity, activity.getResources().getConfiguration())) {
                this.mResolverActivity.getWindow().getDecorView().setSystemUiVisibility(768);
                this.mResolverActivity.getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.internal.app.OplusResolverManager$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnApplyWindowInsetsListener
                    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                        WindowInsets lambda$updateMiniBackground$8;
                        lambda$updateMiniBackground$8 = OplusResolverManager.this.lambda$updateMiniBackground$8(rootView, view, windowInsets);
                        return lambda$updateMiniBackground$8;
                    }
                });
                this.mResolverActivity.getWindow().setGravity(80);
                rootView.setBackgroundResource(201851059);
                this.mResolverActivity.getWindow().setNavigationBarColor(this.mResolverActivity.getColor(201719864));
                if (openButton != null) {
                    ViewGroup.LayoutParams layoutParams = openButton.getLayoutParams();
                    layoutParams.width = this.mResolverActivity.getResources().getDimensionPixelSize(201654506);
                    openButton.setLayoutParams(layoutParams);
                    return;
                }
                return;
            }
            this.mResolverActivity.getWindow().setGravity(17);
            rootView.setBackgroundResource(201851060);
            this.mResolverActivity.getWindow().setNavigationBarColor(this.mResolverActivity.getColor(R.color.transparent));
            rootView.setPadding(0, 0, 0, 0);
            if (openButton != null) {
                ViewGroup.LayoutParams layoutParams2 = openButton.getLayoutParams();
                layoutParams2.width = this.mResolverActivity.getResources().getDimensionPixelSize(OplusResolverUtils.isLargeScreen(this.mResolverActivity.getResources().getConfiguration()) ? 201654508 : 201654507);
                openButton.setLayoutParams(layoutParams2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$updateMiniBackground$8(View rootView, View v, WindowInsets insets) {
        int bottom = insets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()).bottom;
        rootView.setPadding(0, 0, 0, bottom);
        return this.mResolverActivity.getWindow().getDecorView().onApplyWindowInsets(insets);
    }

    /* loaded from: classes.dex */
    static class InsetTouchListener implements View.OnTouchListener {
        private final Context mContext;
        private final int mPrePieSlop;

        public InsetTouchListener(Context context) {
            this.mContext = context;
            this.mPrePieSlop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent event) {
            View parentPanel = view.findViewById(R.id.resolver_empty_state_title);
            RectF contentRect = new RectF(parentPanel.getLeft() + parentPanel.getPaddingLeft(), parentPanel.getTop() + parentPanel.getPaddingTop(), parentPanel.getRight() - parentPanel.getPaddingRight(), parentPanel.getBottom() - parentPanel.getPaddingBottom());
            if (contentRect.contains(event.getX(), event.getY())) {
                return false;
            }
            MotionEvent outsideEvent = MotionEvent.obtain(event);
            if (event.getAction() == 1) {
                outsideEvent.setAction(4);
            }
            view.performClick();
            outsideEvent.recycle();
            ((Activity) this.mContext).finish();
            return true;
        }
    }
}
