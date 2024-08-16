package com.oplus.resolver;

import android.R;
import android.app.Activity;
import android.app.dialog.IOplusAlertDialogBuilderExt;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.android.internal.app.IResolverActivityWrapper;
import com.oplus.resolver.widget.OplusResolverPageIndicator;
import com.oplus.util.OplusContextUtil;
import com.oplus.util.OplusResolverIntentUtil;
import com.oplus.widget.OplusPagerAdapter;
import com.oplus.widget.OplusViewPager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oplus.util.OplusStatistics;
import system.ext.loader.core.ExtLoader;

/* loaded from: classes.dex */
public class OplusResolverDialogHelper {
    private static final String ACTION_CHOOSER_STOP = "oplus.intent.action.STOP_CHOOSER";
    private static final String CODE = "20120";
    private static final String GALLERY_PIN_LIST = "gallery_pin_list";
    private static final String KEY = "49";
    private static final String KEY_TYPE = "type";
    private static final String NEW_APP_HEYTAP_MARKET = "com.heytap.market";
    private static final String PERSON_PROFILE = "person_profile";
    private static final String RECOMMEND_EVENT_ID = "resolver_recommend";
    private static final String SECRET = "be7a52eaeb67a660ecfdcff7c742c8a2";
    private static final String TAG = "OplusResolverDialogHelper";
    private static final String TYPE_GALLERY = "gallery";
    private static final String WORK_PROFILE = "work_profile";
    private BroadcastReceiver mAPKChangedReceiver;
    private Activity mActivity;
    private BroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private IOplusAlertDialogBuilderExt mDialogBuilder;
    private List<ResolveInfo> mList;
    private AdapterView.OnItemLongClickListener mLongClickListener;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private OplusResolverOshare mOplusResolverOshare;
    private Intent mOriginIntent;
    private OplusResolverPagerAdapter mPagerAdapter;
    private Set<OnProfileSelectedListener> mProfileSelectedListener;
    private OplusResolverInfoHelper mResolveInfoHelper;
    private List<ResolveInfo> mRiList;
    private OplusResolverDialogViewPager mViewPager;

    /* loaded from: classes.dex */
    public interface OnProfileSelectedListener {
        void onProfileSelected(int i);
    }

    public OplusResolverDialogHelper(Activity context, Intent intent, Intent[] initialIntents, boolean alwaysUseOption, List<ResolveInfo> displayResolverlist, int profilePage) {
        this.mRiList = new ArrayList();
        this.mList = new ArrayList();
        this.mProfileSelectedListener = new HashSet();
        this.mDialogBuilder = (IOplusAlertDialogBuilderExt) ExtLoader.type(IOplusAlertDialogBuilderExt.class).base(this).create();
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.oplus.resolver.OplusResolverDialogHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent2) {
                String action = intent2.getAction();
                if (OplusResolverDialogHelper.ACTION_CHOOSER_STOP.equals(action) && OplusResolverDialogHelper.this.mActivity != null && !OplusResolverDialogHelper.this.mActivity.isFinishing()) {
                    OplusResolverDialogHelper.this.mActivity.finish();
                }
            }
        };
        this.mAPKChangedReceiver = new BroadcastReceiver() { // from class: com.oplus.resolver.OplusResolverDialogHelper.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent2) {
                if (OplusResolverDialogHelper.this.mActivity != null && !OplusResolverDialogHelper.this.mActivity.isFinishing()) {
                    OplusResolverDialogHelper.this.mActivity.finish();
                }
            }
        };
        this.mLongClickListener = null;
        this.mOnItemClickListener = null;
        this.mContext = context;
        this.mActivity = context;
        this.mOriginIntent = intent;
        this.mOplusResolverOshare = new OplusResolverOshare(context, intent);
        if (displayResolverlist != null) {
            this.mRiList = displayResolverlist;
        } else {
            if (intent == null || initialIntents == null) {
                return;
            }
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
            this.mRiList = queryIntentActivities;
            if (queryIntentActivities.size() == 0) {
                Intent in = new Intent();
                if (intent.getAction() != null) {
                    in.setAction(intent.getAction());
                }
                if (intent.getType() != null) {
                    in.setType(intent.getType());
                }
                if (intent.getExtras() != null) {
                    in.putExtras(intent.getExtras());
                }
                this.mRiList = context.getPackageManager().queryIntentActivities(in, (alwaysUseOption ? 64 : 0) | 65536);
            }
            addInitiaIntents(initialIntents);
        }
        Log.d(TAG, "init " + this.mRiList + ", " + this.mOriginIntent);
        OplusResolverInfoHelper oplusResolverInfoHelper = OplusResolverInfoHelper.getInstance(context);
        this.mResolveInfoHelper = oplusResolverInfoHelper;
        oplusResolverInfoHelper.resort(NearbyUtil.getNearbySharingComponent(context), this.mRiList, intent, profilePage);
        this.mList.addAll(this.mRiList);
        Log.d(TAG, "resort " + this.mRiList);
    }

    public OplusResolverDialogHelper(Activity context, Intent intent, Intent[] initialIntents, boolean alwaysUseOption, List<ResolveInfo> displayResolverlist) {
        this(context, intent, initialIntents, alwaysUseOption, displayResolverlist, 0);
    }

    public void initOShareService() {
        this.mOplusResolverOshare.initOShareService();
    }

    public void initOShareView(View oShareView) {
        this.mOplusResolverOshare.initOShareView(oShareView);
    }

    public void tearDown(View parent) {
        this.mOplusResolverOshare.dismissOShareView();
        View nfcView = parent.findViewById(201457780);
        if (!(nfcView instanceof ViewStub)) {
            nfcView.setVisibility(8);
        }
    }

    public void setIsSplitScreenMode(boolean value) {
        this.mOplusResolverOshare.setIsSplitScreenMode(value);
    }

    public List<ResolveInfo> getResolveInforList() {
        return this.mList;
    }

    private void addInitiaIntents(Intent[] initialIntents) {
        if (initialIntents != null) {
            for (Intent ii : initialIntents) {
                if (ii != null) {
                    ActivityInfo ai = ii.resolveActivityInfo(this.mContext.getPackageManager(), 0);
                    if (ai == null) {
                        Log.w(TAG, "No activity found for " + ii);
                    } else {
                        ResolveInfo ri = new ResolveInfo();
                        ri.activityInfo = ai;
                        if (ii instanceof LabeledIntent) {
                            LabeledIntent li = (LabeledIntent) ii;
                            ri.resolvePackageName = li.getSourcePackage();
                            ri.labelRes = li.getLabelResource();
                            ri.nonLocalizedLabel = li.getNonLocalizedLabel();
                            ri.icon = li.getIconResource();
                        }
                        this.mList.add(ri);
                    }
                }
            }
        }
    }

    private void setResolveView(boolean workProfile, UserHandle userHandle, final OplusResolverDialogViewPager viewPager, final OplusResolverPageIndicator dotView, CheckBox mCheckbox, boolean safeForwardingMode, int placeholderCount) {
        OplusResolverPagerAdapter oplusResolverPagerAdapter = new OplusResolverPagerAdapter(workProfile, userHandle, this.mContext, this.mList, this.mOriginIntent, mCheckbox, safeForwardingMode);
        this.mPagerAdapter = oplusResolverPagerAdapter;
        this.mViewPager = viewPager;
        oplusResolverPagerAdapter.setPlaceholderCount(placeholderCount);
        viewPager.setAdapter(this.mPagerAdapter);
        viewPager.setResolverItemEventListener(new OplusPagerAdapter.OplusResolverItemEventListener() { // from class: com.oplus.resolver.OplusResolverDialogHelper.3
            @Override // com.oplus.widget.OplusPagerAdapter.OplusResolverItemEventListener
            public void OnItemLongClick(int position) {
            }

            @Override // com.oplus.widget.OplusPagerAdapter.OplusResolverItemEventListener
            public void onItemLongClick(int position, View icon) {
                if (OplusResolverDialogHelper.this.mLongClickListener != null) {
                    OplusResolverDialogHelper.this.mLongClickListener.onItemLongClick(null, icon, position, -1L);
                    viewPager.performHapticFeedback(0);
                }
            }

            @Override // com.oplus.widget.OplusPagerAdapter.OplusResolverItemEventListener
            public void OnItemClick(int position) {
                OplusResolverDialogHelper oplusResolverDialogHelper = OplusResolverDialogHelper.this;
                if (!oplusResolverDialogHelper.clickMoreIcon(oplusResolverDialogHelper.mActivity, position) && OplusResolverDialogHelper.this.mOnItemClickListener != null) {
                    OplusResolverDialogHelper.this.mOnItemClickListener.onItemClick(null, null, position, -1L);
                }
            }
        });
        viewPager.setOnPageChangeListener(new OplusViewPager.OnPageChangeListener() { // from class: com.oplus.resolver.OplusResolverDialogHelper.4
            @Override // com.oplus.widget.OplusViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
                dotView.onPageSelected(position);
            }

            @Override // com.oplus.widget.OplusViewPager.OnPageChangeListener
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                dotView.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override // com.oplus.widget.OplusViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int state) {
                dotView.onPageScrollStateChanged(state);
            }
        });
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener longclickListener) {
        this.mLongClickListener = longclickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public OplusResolverPagerAdapter getPagerAdapter() {
        return this.mPagerAdapter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clickMoreIcon(Activity activity, int position) {
        Log.d(TAG, "clickMoreIcon : " + position);
        if (this.mPagerAdapter.isMoreIconPositionAndClick(position)) {
            OplusResolverPageIndicator dotsView = (OplusResolverPageIndicator) activity.findViewById(201457783);
            int pageCount = this.mPagerAdapter.getCount();
            dotsView.setDotsCount(pageCount);
            return true;
        }
        return false;
    }

    public void showTargetDetails(View archer, ResolveInfo ri, SharedPreferences prefs, String type, BaseAdapter adapter) {
        new OplusResolverPinnedUtil(this.mContext, this.mRiList).showTargetDetails(archer, ri, adapter);
    }

    public void showRecommend(Activity activity) {
        final View marketJump = activity.findViewById(201457794);
        if (marketJump == null) {
            return;
        }
        if (!isMarketEnable()) {
            marketJump.setVisibility(8);
            Log.d(TAG, "Market is disable");
            return;
        }
        final String intentType = OplusResolverIntentUtil.getIntentType(this.mOriginIntent);
        if (!this.mResolveInfoHelper.isMarketRecommendType(intentType)) {
            marketJump.setVisibility(8);
            Log.d(TAG, "not is MarketRecommend Type");
        } else {
            AsyncTask<Void, Void, Boolean> sortingTask = new AsyncTask<Void, Void, Boolean>() { // from class: com.oplus.resolver.OplusResolverDialogHelper.5
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public Boolean doInBackground(Void... param) {
                    OplusResolverDialogHelper oplusResolverDialogHelper = OplusResolverDialogHelper.this;
                    return Boolean.valueOf(oplusResolverDialogHelper.support(oplusResolverDialogHelper.mContext));
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public void onPostExecute(Boolean result) {
                    OplusResolverDialogHelper.this.showMarket(result.booleanValue(), marketJump, intentType);
                }
            };
            sortingTask.execute(new Void[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMarket(boolean support, View marketJump, String intentType) {
        if (!support) {
            marketJump.setVisibility(8);
            return;
        }
        marketJump.setVisibility(0);
        if (OplusResolverIntentUtil.DEFAULT_APP_TEXT.equals(intentType)) {
            intentType = "text";
        }
        final String type = intentType;
        marketJump.setOnClickListener(new View.OnClickListener() { // from class: com.oplus.resolver.OplusResolverDialogHelper$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                OplusResolverDialogHelper.this.lambda$showMarket$1(type, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showMarket$0(String type) {
        startRecommend(this.mContext, type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showMarket$1(final String type, View v) {
        new Thread(new Runnable() { // from class: com.oplus.resolver.OplusResolverDialogHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                OplusResolverDialogHelper.this.lambda$showMarket$0(type);
            }
        }).start();
    }

    private boolean startRecommend(Context context, String type) {
        int code = 0;
        try {
            Uri uri = Uri.parse("content://oaps_mk");
            Bundle bundle = new Bundle();
            bundle.putString("rtp", type);
            bundle.putString("goback", "1");
            bundle.putString("secret", SECRET);
            bundle.putString("enterId", KEY);
            bundle.putString("sgtp", "1");
            Bundle responseBundle = call(context, uri, "/recapp", bundle);
            if (responseBundle != null && responseBundle.containsKey("code")) {
                code = responseBundle.getInt("code");
            }
            Log.d(TAG, "startRecommend:" + type + ",response:" + code);
            if (code == 1) {
                HashMap<String, String> map = new HashMap<>();
                map.put("type", type);
                OplusStatistics.onCommon(this.mContext, CODE, RECOMMEND_EVENT_ID, (Map<String, String>) map, false);
                Log.d(TAG, "statistics data [resolver_recommend]: " + map);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return code == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean support(Context context) {
        Uri uri = Uri.parse("content://oaps_mk");
        Bundle bundle = new Bundle();
        bundle.putString("tp", "/recapp");
        bundle.putString("secret", SECRET);
        bundle.putString("enterId", KEY);
        bundle.putString("sgtp", "1");
        int code = 0;
        try {
            Bundle responseBundle = call(context, uri, "/support", bundle);
            if (responseBundle != null && responseBundle.containsKey("code")) {
                code = responseBundle.getInt("code");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        Log.d(TAG, "oaps support:" + code);
        return code == 1;
    }

    private Bundle call(Context context, Uri uri, String path, Bundle bundle) {
        try {
            ContentResolver resolver = context.getContentResolver();
            return resolver.call(uri, path, "", bundle);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    private boolean isMarketEnable() {
        boolean exist = false;
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = this.mContext.getPackageManager().getPackageInfo("com.heytap.market", 8192);
        } catch (Exception e) {
            exist = false;
        }
        if (pkgInfo != null && pkgInfo.applicationInfo != null && pkgInfo.applicationInfo.enabled) {
            return true;
        }
        return exist;
    }

    public void dialogHelperDestroy() {
        this.mOplusResolverOshare.onDestroy();
        try {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        } catch (Exception e) {
            Log.d(TAG, "fail to unregister receiver, " + e);
        }
        try {
            this.mContext.unregisterReceiver(this.mAPKChangedReceiver);
        } catch (Exception e2) {
            Log.d(TAG, "fail to unregister receiver, " + e2);
        }
    }

    public void oSharePause() {
        OplusResolverOshare oplusResolverOshare = this.mOplusResolverOshare;
        if (oplusResolverOshare != null) {
            oplusResolverOshare.onPause();
        }
    }

    public void oShareResume() {
        OplusResolverOshare oplusResolverOshare = this.mOplusResolverOshare;
        if (oplusResolverOshare != null) {
            oplusResolverOshare.onResume();
        }
    }

    public void initNfcView(View rootView) {
        Intent intent = this.mOriginIntent;
        if (intent == null || intent.getAction() == null) {
            return;
        }
        String action = this.mOriginIntent.getAction();
        if (!"android.nfc.action.NDEF_DISCOVERED".equals(action) && !"android.nfc.action.TECH_DISCOVERED".equals(action) && !"android.nfc.action.TAG_DISCOVERED".equals(action)) {
            return;
        }
        View nfcPanel = rootView.findViewById(201457780);
        if (nfcPanel instanceof ViewStub) {
            View view = ((ViewStub) nfcPanel).inflate();
            view.setId(201457780);
        } else {
            nfcPanel.setVisibility(0);
        }
    }

    public void initTabView(View view, OnProfileSelectedListener listener, int currentPage) {
        this.mProfileSelectedListener.add(listener);
        final TabHost tabHost = (TabHost) view.findViewById(201457677);
        tabHost.setup();
        IResolverActivityWrapper resolverActivityWrapper = (IResolverActivityWrapper) OplusResolverUtils.invokeMethod(this.mContext, "getResolverWrapper", new Object[0]);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(PERSON_PROFILE).setContent(201457674).setIndicator(resolverActivityWrapper != null ? resolverActivityWrapper.getPersonalTabLabel() : this.mContext.getString(R.string.zen_mode_forever_dnd));
        tabHost.addTab(tabSpec);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec(WORK_PROFILE).setContent(201457674).setIndicator(resolverActivityWrapper != null ? resolverActivityWrapper.getWorkTabLabel() : this.mContext.getString(R.string.zen_upgrade_notification_title));
        tabHost.addTab(tabSpec2);
        initTabProfile(tabHost.getTabWidget());
        resetTabsHeaderStyle(tabHost.getTabWidget());
        tabHost.setCurrentTab(1 - currentPage);
        tabHost.setCurrentTab(currentPage);
        updateActiveTabStyle(tabHost);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() { // from class: com.oplus.resolver.OplusResolverDialogHelper$$ExternalSyntheticLambda2
            @Override // android.widget.TabHost.OnTabChangeListener
            public final void onTabChanged(String str) {
                OplusResolverDialogHelper.this.lambda$initTabView$2(tabHost, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initTabView$2(TabHost tabHost, String tabId) {
        resetTabsHeaderStyle(tabHost.getTabWidget());
        updateActiveTabStyle(tabHost);
        for (OnProfileSelectedListener l : this.mProfileSelectedListener) {
            if (PERSON_PROFILE.equals(tabId)) {
                l.onProfileSelected(0);
            } else {
                l.onProfileSelected(1);
            }
        }
    }

    public void initChooserTopBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CHOOSER_STOP);
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
        IntentFilter packageFilter = new IntentFilter();
        packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        packageFilter.addDataScheme("package");
        this.mContext.registerReceiver(this.mAPKChangedReceiver, packageFilter);
    }

    public View createView(boolean workProfile, UserHandle userHandle, boolean safeForwardingMode, int count) {
        View view = this.mActivity.getLayoutInflater().inflate(201917454, (ViewGroup) null);
        OplusResolverDialogViewPager viewPager = (OplusResolverDialogViewPager) view.findViewById(201457782);
        OplusResolverPageIndicator dotView = (OplusResolverPageIndicator) view.findViewById(201457783);
        setResolveView(workProfile, userHandle, viewPager, dotView, null, safeForwardingMode, count);
        updateDotView(dotView);
        return view;
    }

    public void reLoadTabPlaceholderCount(boolean workProfile, UserHandle userHandle, int placeholderCount) {
        this.mPagerAdapter.setPlaceholderCount(placeholderCount);
        this.mRiList.clear();
        this.mList.clear();
        updatePageSize(workProfile, userHandle);
        this.mPagerAdapter.notifyDataSetChanged();
        this.mViewPager.setCurrentItem(0);
    }

    private void resetTabsHeaderStyle(TabWidget tabWidget) {
        int length = tabWidget.getChildCount();
        for (int i = 0; i < length; i++) {
            tabWidget.getChildAt(i).setBackground(null);
            TextView tx = (TextView) tabWidget.getChildAt(i).findViewById(R.id.title);
            tx.setTextColor(this.mContext.getColor(R.color.black));
        }
    }

    private void updateActiveTabStyle(TabHost tabHost) {
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(201850975);
        TextView tx = (TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(R.id.title);
        tx.setTextColor(OplusContextUtil.getAttrColor(this.mContext, 201392133));
    }

    private void initTabProfile(TabWidget tabHost) {
        ViewGroup.MarginLayoutParams tabHostParams = (ViewGroup.MarginLayoutParams) tabHost.getLayoutParams();
        tabHostParams.bottomMargin = this.mContext.getResources().getDimensionPixelOffset(201654345);
        tabHost.setLayoutParams(tabHostParams);
        int length = tabHost.getChildCount();
        for (int i = 0; i < length; i++) {
            ViewGroup.LayoutParams params = tabHost.getChildAt(i).getLayoutParams();
            params.height = this.mContext.getResources().getDimensionPixelSize(201654418);
            tabHost.getChildAt(i).setLayoutParams(params);
            TextView tx = (TextView) tabHost.getChildAt(i).findViewById(R.id.title);
            tx.setTextSize(0, this.mContext.getResources().getDimensionPixelSize(201654289));
        }
    }

    private void updateDotView(View dotView) {
        OplusResolverPagerAdapter oplusResolverPagerAdapter = this.mPagerAdapter;
        if (oplusResolverPagerAdapter == null || dotView == null || !(dotView instanceof OplusResolverPageIndicator)) {
            return;
        }
        int dotCount = oplusResolverPagerAdapter.getCount();
        ((OplusResolverPageIndicator) dotView).setDotsCount(dotCount);
    }

    public boolean initCheckBox(Intent intent, View view, boolean alwaysUseOption) {
        View checkBoxContainer = null;
        if (alwaysUseOption) {
            checkBoxContainer = view.findViewById(201457793);
            if (checkBoxContainer instanceof ViewStub) {
                checkBoxContainer = ((ViewStub) checkBoxContainer).inflate();
            }
        }
        boolean openFlag = false;
        try {
            openFlag = intent.getBooleanExtra("oplus_filemanager_openflag", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (openFlag && checkBoxContainer != null) {
            checkBoxContainer.setVisibility(8);
        }
        return openFlag;
    }

    public void resortListAndNotifyChange(boolean workProfile, UserHandle userHandle, List<ResolveInfo> displayResolverlist) {
        resortListAndNotifyChange(workProfile, userHandle, displayResolverlist, 0);
    }

    public void resortListAndNotifyChange(boolean workProfile, UserHandle userHandle, List<ResolveInfo> displayResolverlist, int profilePage) {
        int oldMoreIcon = getPagerAdapter().getMoreIconTotalPosition();
        int oldCount = this.mList.size();
        int oldPageCount = this.mPagerAdapter.getCount();
        if (displayResolverlist != null) {
            this.mRiList = displayResolverlist;
        }
        this.mResolveInfoHelper.resort(NearbyUtil.getNearbySharingComponent(this.mContext), this.mRiList, this.mOriginIntent, profilePage);
        this.mList.clear();
        this.mList.addAll(this.mRiList);
        Log.d(TAG, "sort " + this.mRiList + ", " + this.mOriginIntent);
        updatePageSize(workProfile, userHandle);
        boolean needUpdateMoreIcon = ((OplusResolverIntentUtil.isChooserAction(this.mOriginIntent) || oldMoreIcon == this.mResolveInfoHelper.getResolveTopSize(this.mOriginIntent)) && oldCount == this.mList.size() && oldPageCount == this.mPagerAdapter.getCount()) ? false : true;
        if (needUpdateMoreIcon) {
            this.mPagerAdapter.updateNeedMoreIcon(this.mOriginIntent);
            updateDotView(this.mActivity.findViewById(201457783));
        }
        this.mPagerAdapter.notifyDataSetChanged();
        OplusResolverDialogViewPager oplusResolverDialogViewPager = this.mViewPager;
        if (oplusResolverDialogViewPager != null && needUpdateMoreIcon) {
            oplusResolverDialogViewPager.setCurrentItem(0);
        }
    }

    private void updatePageSize(boolean workProfile, UserHandle userHandle) {
        OplusResolverPagerAdapter oplusResolverPagerAdapter = this.mPagerAdapter;
        if (oplusResolverPagerAdapter != null) {
            oplusResolverPagerAdapter.updatePageSize(workProfile, userHandle);
        }
    }

    public void addProfileSelectedListener(OnProfileSelectedListener listener) {
        if (listener != null) {
            this.mProfileSelectedListener.add(listener);
        }
    }

    public void removeProfileSelectedListener(OnProfileSelectedListener listener) {
        if (listener != null) {
            this.mProfileSelectedListener.remove(listener);
        }
    }

    public void restoreProfilePager(View view, int pageNumber) {
        TabHost tabHost = (TabHost) view.findViewById(201457677);
        if (tabHost != null) {
            tabHost.setCurrentTab(pageNumber);
        }
    }

    public void clearDrawableCache() {
        OplusResolverPagerAdapter oplusResolverPagerAdapter = this.mPagerAdapter;
        if (oplusResolverPagerAdapter != null) {
            oplusResolverPagerAdapter.clearDrawableCache();
        }
    }
}
