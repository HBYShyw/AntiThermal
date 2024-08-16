package com.oplus.widget;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.Dialog;
import android.common.OplusFeatureCache;
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
import android.provider.Settings;
import android.telephony.OplusTelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.android.internal.app.IOplusResolverStyle;
import com.android.internal.app.IntentForwarderActivity;
import com.oplus.resolver.IOplusResolverGridItemClickListener;
import com.oplus.resolver.OplusGalleryPagerAdapterHelper;
import com.oplus.resolver.OplusResolverInfoHelper;
import com.oplus.resolver.OplusResolverPinnedUtil;
import com.oplus.util.OplusResolverUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Deprecated
/* loaded from: classes.dex */
public class OplusResolverPagerAdapter extends OplusPagerAdapter implements IOplusResolverGridItemClickListener {
    public static int COLUMN_SIZE = 4;
    private static final int COLUMN_SIZE_DEFAULT = 4;
    private static final int COLUMN_SIZE_FOR_CTS = 8;
    private static final int COLUMN_SIZE_MODULUS = 2;
    private static final int COLUMN_SIZE_SPECIAL = 3;
    private static final int DEFAULT_CHECK_DP_WIDTH = 360;
    public static final String KEY_FOLDING_MODE = "oplus_system_folding_mode";
    public static final String TAG = "OplusResolverPagerAdapter";
    private Activity mActivity;
    private IntentSender mChosenComponentSender;
    private Context mContext;
    private boolean mIsChecked;
    private boolean mIsChooserCtsTest;
    private Intent mOriginIntent;
    private OplusGalleryPagerAdapterHelper mPagerAdapterHelper;
    private int mPagerSize;
    private int mPlaceholderCount;
    private List<ResolveInfo> mRiList;
    private boolean mSafeForwardingMode;

    @Deprecated
    public OplusResolverPagerAdapter(Context context, List<OplusGridView> listOplusGridView, List<ResolveInfo> riList, int pagecount, Intent intent, CheckBox checkbox, Dialog alertDialog, boolean safeForwardingMode) {
        this.mPagerSize = COLUMN_SIZE;
        this.mIsChecked = false;
        this.mActivity = (Activity) context;
        this.mContext = context;
        this.mRiList = riList;
        this.mOriginIntent = intent;
        this.mSafeForwardingMode = safeForwardingMode;
        OplusGalleryPagerAdapterHelper oplusGalleryPagerAdapterHelper = new OplusGalleryPagerAdapterHelper(context, alertDialog);
        this.mPagerAdapterHelper = oplusGalleryPagerAdapterHelper;
        oplusGalleryPagerAdapterHelper.setOplusResolverItemEventListener(this);
        updatePageSize(false, null);
        if (checkbox != null) {
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.oplus.widget.OplusResolverPagerAdapter$$ExternalSyntheticLambda0
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    OplusResolverPagerAdapter.this.lambda$new$0(compoundButton, z);
                }
            });
        }
        OplusResolverInfoHelper.getInstance(context).sortGalleryPinnedList(riList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(CompoundButton buttonView, boolean isChecked) {
        this.mIsChecked = isChecked;
    }

    public OplusResolverPagerAdapter(Context context, List<ResolveInfo> riList, Intent intent, CheckBox checkbox, boolean safeForwardingMode) {
        this(false, null, context, riList, intent, checkbox, safeForwardingMode);
    }

    public OplusResolverPagerAdapter(boolean workProfile, UserHandle userHandle, Context context, List<ResolveInfo> riList, Intent intent, CheckBox checkbox, boolean safeForwardingMode) {
        this.mPagerSize = COLUMN_SIZE;
        this.mIsChecked = false;
        this.mActivity = (Activity) context;
        this.mContext = context;
        this.mRiList = riList;
        this.mOriginIntent = intent;
        this.mSafeForwardingMode = safeForwardingMode;
        OplusGalleryPagerAdapterHelper oplusGalleryPagerAdapterHelper = new OplusGalleryPagerAdapterHelper(context, null);
        this.mPagerAdapterHelper = oplusGalleryPagerAdapterHelper;
        oplusGalleryPagerAdapterHelper.setOplusResolverItemEventListener(this);
        updatePageSize(workProfile, userHandle);
        if (checkbox != null) {
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.oplus.widget.OplusResolverPagerAdapter$$ExternalSyntheticLambda1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    OplusResolverPagerAdapter.this.lambda$new$1(compoundButton, z);
                }
            });
        }
        this.mIsChooserCtsTest = OplusResolverUtil.isChooserCtsTest(this.mContext, intent);
        resetColumnSize();
        OplusResolverInfoHelper.getInstance(context).sortGalleryPinnedList(riList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(CompoundButton buttonView, boolean isChecked) {
        this.mIsChecked = isChecked;
    }

    @Override // com.oplus.widget.OplusPagerAdapter
    public int getCount() {
        return (int) Math.ceil(this.mRiList.size() / this.mPagerSize);
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

    public void updatePageSize(boolean workProfile) {
        updatePageSize(workProfile, null);
    }

    private void resetColumnSize() {
        int i = this.mIsChooserCtsTest ? 8 : 4;
        COLUMN_SIZE = i;
        this.mPagerAdapterHelper.setColumnSize(i);
    }

    public void updatePageSize(boolean workProfile, UserHandle userHandle) {
        Configuration cfg = this.mContext.getResources().getConfiguration();
        resetColumnSize();
        boolean isMainScreen = false;
        try {
            isMainScreen = Settings.Global.getInt(this.mActivity.getContentResolver(), "oplus_system_folding_mode") == 1;
        } catch (Settings.SettingNotFoundException e) {
            Log.e("OplusResolverPagerAdapter", "get value error" + e);
        }
        Log.d("OplusResolverPagerAdapter", "updatePageSize isMainScreen" + isMainScreen);
        if (isMainScreen) {
            if (cfg.orientation == 2 && this.mActivity.isInMultiWindowMode()) {
                this.mPagerSize = COLUMN_SIZE;
            } else {
                if (cfg.screenWidthDp < 360) {
                    COLUMN_SIZE = 3;
                }
                this.mPagerSize = COLUMN_SIZE * 2;
            }
        } else if (cfg.orientation != 2 && !this.mActivity.isInMultiWindowMode()) {
            this.mPagerSize = COLUMN_SIZE * 2;
        } else {
            this.mPagerSize = COLUMN_SIZE;
        }
        if (workProfile) {
            this.mPagerSize += COLUMN_SIZE;
        }
        this.mPagerAdapterHelper.updateUserHandle(userHandle);
        this.mPagerAdapterHelper.setColumnSize(COLUMN_SIZE);
    }

    @Override // com.oplus.resolver.IOplusResolverGridItemClickListener
    public void onItemClick(int pagerNumber, int position) {
        OnItemClick((this.mPagerSize * pagerNumber) + position);
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x0125, code lost:
    
        if (r0 != null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x012b, code lost:
    
        if (r0.hasNext() == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x012d, code lost:
    
        r11 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0137, code lost:
    
        if (r11.match(r10) < 0) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0139, code lost:
    
        r12 = r11.getPort();
        r13 = r11.getHost();
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0141, code lost:
    
        if (r12 < 0) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0143, code lost:
    
        r14 = java.lang.Integer.toString(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0149, code lost:
    
        r3.addDataAuthority(r13, r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0148, code lost:
    
        r14 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x014e, code lost:
    
        r11 = r7.filter.pathsIterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0154, code lost:
    
        if (r11 == null) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0156, code lost:
    
        r12 = r10.getPath();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x015a, code lost:
    
        if (r12 == null) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0160, code lost:
    
        if (r11.hasNext() == false) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0162, code lost:
    
        r13 = r11.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x016c, code lost:
    
        if (r13.match(r12) == false) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x016e, code lost:
    
        r3.addDataPath(r13.getPath(), r13.getType());
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
            ResolveInfo resolveInfo = this.mRiList.get(position);
            Intent intent2 = getReplacementIntent(resolveInfo, intent);
            ActivityInfo ai = resolveInfo.activityInfo;
            intent2.setComponent(new ComponentName(ai.applicationInfo.packageName, ai.name));
            if (!isInLockTaskMode()) {
                safelyStartActivity(intent2, this.mActivity, this.mSafeForwardingMode);
                this.mActivity.overridePendingTransition(((IOplusResolverStyle) OplusFeatureCache.getOrCreate(IOplusResolverStyle.DEFAULT, new Object[0])).getActivityStartAnimationRes(), ((IOplusResolverStyle) OplusFeatureCache.getOrCreate(IOplusResolverStyle.DEFAULT, new Object[0])).getActivityStartAnimationRes());
            }
            ResolveInfo ri = this.mRiList.get(position);
            if (this.mIsChecked) {
                if (intent2.getAction() != null) {
                    filter.addAction(intent2.getAction());
                }
                Set<String> categories = intent2.getCategories();
                if (categories != null) {
                    Iterator<String> it = categories.iterator();
                    while (it.hasNext()) {
                        filter.addCategory(it.next());
                    }
                }
                filter.addCategory("android.intent.category.DEFAULT");
                int cat = 268369920 & ri.match;
                Uri data = intent2.getData();
                if (cat == 6291456 && (mimeType = intent2.resolveType(this.mContext)) != null) {
                    try {
                        filter.addDataType(mimeType);
                    } catch (IntentFilter.MalformedMimeTypeException e) {
                        filter = null;
                    }
                }
                if (data != null && data.getScheme() != null && (cat != 6291456 || (!"file".equals(data.getScheme()) && !OplusTelephonyManager.BUNDLE_CONTENT.equals(data.getScheme())))) {
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
                this.mContext.getPackageManager().addPreferredActivity(filter, bestMatch, set, intent2.getComponent());
            }
            this.mPagerAdapterHelper.dismiss();
        }
    }

    @Override // com.oplus.resolver.IOplusResolverGridItemClickListener
    public void onItemLongClick(int pagerNumber, int position, View icon) {
        if (this.mOplusResolverItemEventListener != null) {
            this.mOplusResolverItemEventListener.onItemLongClick((this.mPagerSize * pagerNumber) + position, icon);
        } else if (this.mRiList.size() > (this.mPagerSize * pagerNumber) + position) {
            new OplusResolverPinnedUtil(this.mContext, this.mRiList).showTargetDetails(icon, this.mRiList.get((this.mPagerSize * pagerNumber) + position));
        } else {
            Log.e("OplusResolverPagerAdapter", "onItemLongClick error size:" + this.mRiList.size() + "  " + (this.mPagerSize * pagerNumber) + position);
        }
    }

    @Override // com.oplus.resolver.IOplusResolverGridItemClickListener
    public void onItemLongClick(int pagerNumber, int position) {
        OnItemLongClick((this.mPagerSize * pagerNumber) + position);
    }

    public void OnItemLongClick(int position) {
        if (this.mOplusResolverItemEventListener != null) {
            this.mOplusResolverItemEventListener.OnItemLongClick(position);
        }
    }

    @Deprecated
    public void unRegister() {
        this.mPagerAdapterHelper.unRegister();
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

    private Intent getReplacementIntent(ResolveInfo resolveInfo, Intent defIntent) {
        if (resolveInfo.targetUserId == -2) {
            return defIntent;
        }
        ActivityInfo aInfo = resolveInfo.activityInfo;
        if (aInfo.name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_PARENT) || aInfo.name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_MANAGED_PROFILE)) {
            Intent result = Intent.createChooser(defIntent, null);
            result.putExtra("android.intent.extra.AUTO_LAUNCH_SINGLE_CHOICE", false);
            return result;
        }
        return defIntent;
    }

    @Override // com.oplus.widget.OplusPagerAdapter
    public void notifyDataSetChanged() {
        OplusResolverInfoHelper.getInstance(this.mContext).sortGalleryPinnedList(this.mRiList);
        super.notifyDataSetChanged();
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
}
