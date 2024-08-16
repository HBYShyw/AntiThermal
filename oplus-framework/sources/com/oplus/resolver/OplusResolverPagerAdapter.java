package com.oplus.resolver;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telephony.OplusTelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.oplus.util.OplusResolverUtil;
import com.oplus.widget.OplusGridView;
import com.oplus.widget.OplusItem;
import com.oplus.widget.OplusPagerAdapter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusResolverPagerAdapter extends OplusPagerAdapter implements IOplusResolverGridItemClickListener {
    public static int COLUMN_SIZE = 4;
    public static final int COLUMN_SIZE_DEFAULT = 4;
    public static final int COLUMN_SIZE_FOR_CTS = 8;
    private static final int COLUMN_SIZE_MODULUS = 2;
    private static final int COLUMN_SIZE_SPECIAL = 3;
    public static final int COLUMN_SIZE_TABLET = 5;
    private static final int DEFAULT_CHECK_DP_WIDTH = 360;
    private static final int DEFAULT_CHECK_SCREEN_WIDTH = 640;
    private static final int DEFAULT_EXPORT_CHECK_DP_HEIGHT = 534;
    private static final int DEFAULT_INSIDE_CHECK_DP_HEIGHT = 458;
    public static final String KEY_FOLDING_MODE = "oplus_system_folding_mode";
    public static final String TAG = "OplusResolverPagerAdapter";
    private Activity mActivity;
    private IntentSender mChosenComponentSender;
    private Context mContext;
    private boolean mIsChooserCtsTest;
    private Intent mOriginIntent;
    private OplusResolverPagerAdapterHelper mPagerAdapterHelper;
    private int mPlaceholderCount;
    private List<ResolveInfo> mRiList;
    private boolean mSafeForwardingMode;
    private int mPagerSize = COLUMN_SIZE;
    private boolean mIsChecked = false;

    public OplusResolverPagerAdapter(boolean workProfile, UserHandle userHandle, Context context, List<ResolveInfo> riList, Intent intent, CheckBox checkbox, boolean safeForwardingMode) {
        this.mActivity = (Activity) context;
        this.mContext = context;
        this.mRiList = riList;
        this.mOriginIntent = intent;
        this.mSafeForwardingMode = safeForwardingMode;
        OplusResolverPagerAdapterHelper oplusResolverPagerAdapterHelper = new OplusResolverPagerAdapterHelper(context, null);
        this.mPagerAdapterHelper = oplusResolverPagerAdapterHelper;
        oplusResolverPagerAdapterHelper.setOplusResolverItemEventListener(this);
        updatePageSize(workProfile, userHandle);
        if (checkbox != null) {
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.oplus.resolver.OplusResolverPagerAdapter$$ExternalSyntheticLambda0
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    OplusResolverPagerAdapter.this.lambda$new$0(compoundButton, z);
                }
            });
        }
        this.mIsChooserCtsTest = OplusResolverUtil.isChooserCtsTest(this.mContext, intent);
        resetColumnSize();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(CompoundButton buttonView, boolean isChecked) {
        this.mIsChecked = isChecked;
    }

    @Override // com.oplus.widget.OplusPagerAdapter
    public int getCount() {
        List<ResolveInfo> list = this.mRiList;
        if (list == null || list.isEmpty()) {
            int result = (int) Math.ceil(this.mPlaceholderCount / this.mPagerSize);
            return result;
        }
        if (this.mPagerAdapterHelper.isNeedMoreIcon()) {
            int result2 = (int) Math.ceil((this.mPagerAdapterHelper.getMoreIconTotalPosition() + 1) / this.mPagerSize);
            return result2;
        }
        int result3 = (int) Math.ceil(this.mRiList.size() / this.mPagerSize);
        return result3;
    }

    @Override // com.oplus.widget.OplusPagerAdapter
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override // com.oplus.widget.OplusPagerAdapter
    public Object instantiateItem(ViewGroup container, int position) {
        List<OplusItem> appInfo = this.mPagerAdapterHelper.loadBitmap(this.mOriginIntent, this.mRiList, position, this.mPagerSize, this.mPlaceholderCount);
        onInstantiateDataFinished(position, appInfo);
        View child = this.mPagerAdapterHelper.createPagerView(appInfo, position, this.mPagerSize);
        container.addView(child);
        return child;
    }

    @Override // com.oplus.widget.OplusPagerAdapter
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override // com.oplus.widget.OplusPagerAdapter
    public int getItemPosition(Object object) {
        return -2;
    }

    public void onInstantiateDataFinished(int pagerNumber, List<OplusItem> appList) {
        OplusGridView gridView = new OplusGridView(this.mContext);
        OplusItem[][] items = this.mPagerAdapterHelper.listToArray(appList);
        gridView.setAppInfo(items);
        loadBitmap(pagerNumber, gridView);
    }

    public void updatePageSize() {
        updatePageSize(false, null);
    }

    private void resetColumnSize() {
        int i = this.mIsChooserCtsTest ? 8 : 4;
        COLUMN_SIZE = i;
        this.mPagerAdapterHelper.setColumnSize(i);
    }

    public void updatePageSize(boolean workProfile, UserHandle userHandle) {
        Configuration cfg = this.mContext.getResources().getConfiguration();
        resetColumnSize();
        int panelWidth = OplusResolverUtils.calculateResponsiveUIPanelWidth(this.mActivity);
        COLUMN_SIZE = OplusResolverUtils.calculateColumnCount(this.mActivity, panelWidth);
        int rowSize = OplusResolverUtils.calculateRowCount(cfg);
        this.mPagerSize = COLUMN_SIZE * rowSize;
        if (this.mIsChooserCtsTest) {
            resetColumnSize();
            this.mPagerSize = COLUMN_SIZE * 2;
        }
        if (workProfile) {
            this.mPagerSize += COLUMN_SIZE;
        }
        this.mPagerAdapterHelper.updateUserHandle(userHandle);
        this.mPagerAdapterHelper.setColumnSize(COLUMN_SIZE);
    }

    public void updateNeedMoreIcon(Intent intent) {
        this.mPagerAdapterHelper.updateNeedMoreIcon(intent, this.mRiList.size());
    }

    public boolean isMoreIconPositionAndClick(int position) {
        boolean isMoreIconPosition = this.mPagerAdapterHelper.isMoreIconPosition(position);
        if (isMoreIconPosition) {
            this.mPagerAdapterHelper.clickMoreIcon();
            notifyDataSetChanged();
        }
        return isMoreIconPosition;
    }

    public int getMoreIconTotalPosition() {
        return this.mPagerAdapterHelper.getMoreIconTotalPosition();
    }

    public void setPlaceholderCount(int placeholderCount) {
        this.mPlaceholderCount = placeholderCount;
    }

    @Override // com.oplus.resolver.IOplusResolverGridItemClickListener
    public void onItemClick(int pagerNumber, int position) {
        OnItemClick((this.mPagerSize * pagerNumber) + position);
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x010d, code lost:
    
        if (r0 != null) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0113, code lost:
    
        if (r0.hasNext() == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0115, code lost:
    
        r10 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x011f, code lost:
    
        if (r10.match(r9) < 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0121, code lost:
    
        r11 = r10.getPort();
        r12 = r10.getHost();
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0129, code lost:
    
        if (r11 < 0) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x012b, code lost:
    
        r13 = java.lang.Integer.toString(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0131, code lost:
    
        r3.addDataAuthority(r12, r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0130, code lost:
    
        r13 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0136, code lost:
    
        r10 = r6.filter.pathsIterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x013c, code lost:
    
        if (r10 == null) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x013e, code lost:
    
        r11 = r9.getPath();
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0142, code lost:
    
        if (r11 == null) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0148, code lost:
    
        if (r10.hasNext() == false) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x014a, code lost:
    
        r12 = r10.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0154, code lost:
    
        if (r12.match(r11) == false) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0156, code lost:
    
        r3.addDataPath(r12.getPath(), r12.getType());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void OnItemClick(int position) {
        String mimeType;
        if (this.mOplusResolverItemEventListener != null) {
            this.mOplusResolverItemEventListener.OnItemClick(position);
            return;
        }
        IntentFilter filter = new IntentFilter();
        Intent intent = new Intent(this.mOriginIntent);
        intent.addFlags(50331648);
        if (position < this.mRiList.size() && position >= 0) {
            Log.d("OplusResolverPagerAdapter", "onItemClick : " + position + ", " + this.mRiList);
            ActivityInfo ai = this.mRiList.get(position).activityInfo;
            intent.setComponent(new ComponentName(ai.applicationInfo.packageName, ai.name));
            if (!isInLockTaskMode()) {
                safelyStartActivity(intent, this.mActivity, this.mSafeForwardingMode);
                this.mActivity.overridePendingTransition(201981966, 201981969);
            }
            ResolveInfo ri = this.mRiList.get(position);
            if (this.mIsChecked) {
                if (intent.getAction() != null) {
                    filter.addAction(intent.getAction());
                }
                Set<String> categories = intent.getCategories();
                if (categories != null) {
                    Iterator<String> it = categories.iterator();
                    while (it.hasNext()) {
                        filter.addCategory(it.next());
                    }
                }
                filter.addCategory("android.intent.category.DEFAULT");
                int cat = 268369920 & ri.match;
                Uri data = intent.getData();
                if (cat == 6291456 && (mimeType = intent.resolveType(this.mContext)) != null) {
                    try {
                        filter.addDataType(mimeType);
                    } catch (IntentFilter.MalformedMimeTypeException e) {
                        filter = null;
                    }
                }
                if (filter != null && data != null && data.getScheme() != null && (cat != 6291456 || (!"file".equals(data.getScheme()) && !OplusTelephonyManager.BUNDLE_CONTENT.equals(data.getScheme())))) {
                    filter.addDataScheme(data.getScheme());
                    if (ri.filter != null) {
                        Iterator<IntentFilter.AuthorityEntry> aIt = ri.filter.authoritiesIterator();
                    }
                }
                int N = this.mRiList.size();
                ComponentName[] set = new ComponentName[N];
                int bestMatch = 0;
                int i = 0;
                while (i < N) {
                    ResolveInfo r = this.mRiList.get(i);
                    int N2 = N;
                    set[i] = new ComponentName(r.activityInfo.packageName, r.activityInfo.name);
                    if (r.match > bestMatch) {
                        bestMatch = r.match;
                    }
                    i++;
                    N = N2;
                }
                this.mContext.getPackageManager().addPreferredActivity(filter, bestMatch, set, intent.getComponent());
            }
            this.mPagerAdapterHelper.dismiss();
        }
    }

    @Override // com.oplus.resolver.IOplusResolverGridItemClickListener
    public void onItemLongClick(int pagerNumber, int position) {
        OnItemLongClick((this.mPagerSize * pagerNumber) + position);
    }

    @Override // com.oplus.resolver.IOplusResolverGridItemClickListener
    public void onItemLongClick(int pagerNumber, int position, View icon) {
        if (this.mOplusResolverItemEventListener != null) {
            this.mOplusResolverItemEventListener.onItemLongClick((this.mPagerSize * pagerNumber) + position, icon);
        }
    }

    public void OnItemLongClick(int position) {
        if (this.mOplusResolverItemEventListener != null) {
            this.mOplusResolverItemEventListener.OnItemLongClick(position);
        }
    }

    @Deprecated
    public void unRegister() {
    }

    private static boolean isInLockTaskMode() {
        try {
            return ActivityManagerNative.getDefault().isInLockTaskMode();
        } catch (RemoteException e) {
            return false;
        }
    }

    private void safelyStartActivity(Intent intent, Activity activity, boolean safeForwardingMode) {
        String launchedFromPackage;
        if (!safeForwardingMode) {
            try {
                activity.startActivity(intent);
                onActivityStarted(intent);
                return;
            } catch (RuntimeException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            activity.startActivityAsCaller(intent, null, false, -10000);
            onActivityStarted(intent);
        } catch (RuntimeException e2) {
            try {
                launchedFromPackage = ActivityManagerNative.getDefault().getLaunchedFromPackage(activity.getActivityToken());
            } catch (RemoteException e3) {
                launchedFromPackage = "??";
            }
            Log.d("OplusResolverPagerAdapter", " safelyStartActivity : " + launchedFromPackage);
        }
    }

    private void onActivityStarted(Intent intent) {
        ComponentName target;
        if (this.mChosenComponentSender != null && (target = intent.getComponent()) != null) {
            Intent fillIn = new Intent().putExtra("android.intent.extra.CHOSEN_COMPONENT", target);
            try {
                this.mChosenComponentSender.sendIntent(this.mContext, -1, fillIn, null, null);
            } catch (IntentSender.SendIntentException e) {
            }
        }
    }

    @Deprecated
    public void loadBitmap(int pagerNumber, OplusGridView gridView) {
    }

    public void setChosenComponentSender(IntentSender is) {
        this.mChosenComponentSender = is;
    }

    public void updateIntent(Intent intent) {
        this.mOriginIntent = intent;
    }

    public void clearDrawableCache() {
        OplusResolverPagerAdapterHelper oplusResolverPagerAdapterHelper = this.mPagerAdapterHelper;
        if (oplusResolverPagerAdapterHelper != null) {
            oplusResolverPagerAdapterHelper.clearDrawableCache();
        }
    }
}
