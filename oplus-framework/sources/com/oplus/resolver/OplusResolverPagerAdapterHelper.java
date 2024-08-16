package com.oplus.resolver;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.OplusThemeResources;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import com.android.internal.widget.DefaultItemAnimator;
import com.android.internal.widget.GridLayoutManager;
import com.android.internal.widget.RecyclerView;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.multiapp.OplusMultiAppManager;
import com.oplus.util.OplusResolverIntentUtil;
import com.oplus.util.OplusResolverUtil;
import com.oplus.widget.OplusItem;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

/* loaded from: classes.dex */
public class OplusResolverPagerAdapterHelper implements IOplusResolverGridItemClickListener, IOplusGalleryPagerAdapter {
    private static final String TAG = "OplusResolverPagerAdapterHelper";
    private Dialog mAlertDialog;
    private int mColumnSize;
    private Context mContext;
    private OplusResolverCustomIconHelper mCustomIconHelper;
    private IOplusResolverGridItemClickListener mItemClickListener;
    private int mMoreIconTotalPosition;
    private final OplusOShareManager mOShareManager;
    private OplusResolverInfoHelper mResolveInfoHelper;
    private UserHandle mUserHandle;
    private final Handler mHandler = new Handler();
    private LruCache<String, OplusItem> mMemoryCache = new LruCache<>(100);
    private boolean mNeedMoreIcon = false;
    private boolean mNeedAnimation = false;
    private OplusItem mDefaultOplusItem = null;
    private OplusItem mMoreItem = new OplusItem();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private HashMap<Integer, Thread> mTaskHashMap = new HashMap<>();
    private HashMap<Integer, OplusResolverGridAdapter> mAdapterCache = new HashMap<>();
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() { // from class: com.oplus.resolver.OplusResolverPagerAdapterHelper.1
        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
            OplusResolverPagerAdapterHelper.this.mOShareManager.onResume();
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
            OplusResolverPagerAdapterHelper.this.mOShareManager.onPause();
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
            OplusResolverPagerAdapterHelper.this.destroyUI(activity);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public OplusResolverPagerAdapterHelper(Context context, Dialog dialog) {
        this.mContext = context;
        this.mAlertDialog = dialog;
        this.mOShareManager = new OplusOShareManager(context);
        this.mResolveInfoHelper = OplusResolverInfoHelper.getInstance(context);
        this.mCustomIconHelper = new OplusResolverCustomIconHelper(context);
        this.mMoreItem.setText(this.mContext.getString(201588895));
        this.mMoreItem.setIcon(this.mCustomIconHelper.getAdaptiveIcon(201850947));
        this.mUserHandle = UserHandle.of(context.getUserId());
        registerActivityLifecycle(this.mContext);
    }

    @Override // com.oplus.resolver.IOplusGalleryPagerAdapter
    public View createPagerView(List<OplusItem> appinfo, int pagerNumber, int pagerSize) {
        Log.d(TAG, "COLUMN_SIZE=" + this.mColumnSize + ",pagerNumber=" + pagerNumber);
        RecyclerView gridView = new RecyclerView(this.mContext);
        gridView.setOverScrollMode(0);
        final int padding = this.mContext.getResources().getDimensionPixelOffset(201654424);
        gridView.setPaddingRelative(padding, 0, padding, 0);
        gridView.setItemAnimator(new DefaultItemAnimator());
        gridView.setLayoutManager(new GridLayoutManager(this.mContext, this.mColumnSize));
        if (this.mColumnSize != 8) {
            gridView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: com.oplus.resolver.OplusResolverPagerAdapterHelper.2
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int position = parent.getChildAdapterPosition(view);
                    int itemWidth = OplusResolverPagerAdapterHelper.this.mContext.getResources().getDimensionPixelSize(201654472);
                    int width = parent.getMeasuredWidth();
                    double w = ((width - (OplusResolverPagerAdapterHelper.this.mColumnSize * itemWidth)) - (padding * 2)) / (OplusResolverPagerAdapterHelper.this.mColumnSize * (OplusResolverPagerAdapterHelper.this.mColumnSize - 1));
                    int p = position % OplusResolverPagerAdapterHelper.this.mColumnSize;
                    if (parent.getLayoutDirection() == 1) {
                        outRect.right = (int) (p * w);
                    } else {
                        outRect.left = (int) (p * w);
                    }
                    Log.d(OplusResolverPagerAdapterHelper.TAG, "position=" + position + ",itemWidth=" + itemWidth + ",width=" + width + ",w=" + w + ",p=" + p + ",outRect.left=" + outRect.left + "padding=" + padding);
                }
            });
        }
        OplusResolverGridAdapter adapter = new OplusResolverGridAdapter(this.mContext, this.mOShareManager);
        if (hasOsharePackage(appinfo)) {
            this.mOShareManager.initOShareService();
        }
        if (appinfo != null) {
            int moreIconPosition = this.mMoreIconTotalPosition % pagerSize;
            int moreIconPageCount = (int) Math.ceil((r4 + 1) / pagerSize);
            if (this.mNeedMoreIcon && moreIconPageCount == pagerNumber + 1) {
                adapter.setOplusItems(initAppInfoTop(appinfo, moreIconPosition));
            } else {
                adapter.setOplusItems(appinfo);
                if (this.mNeedAnimation && moreIconPageCount == pagerNumber + 1) {
                    this.mNeedAnimation = false;
                    adapter.startMoreAnimation(moreIconPosition);
                }
            }
            adapter.setPagerNumber(pagerNumber);
            adapter.setOnItemClickListener(this);
        }
        gridView.setAdapter(adapter);
        this.mAdapterCache.put(Integer.valueOf(pagerNumber), adapter);
        return gridView;
    }

    @Override // com.oplus.resolver.IOplusGalleryPagerAdapter
    public OplusItem[][] listToArray(List<OplusItem> oplusItems) {
        int rowCounts = (int) Math.min(Math.ceil(oplusItems.size() / this.mColumnSize), 2.0d);
        OplusItem[][] array = (OplusItem[][]) Array.newInstance((Class<?>) OplusItem.class, rowCounts, this.mColumnSize);
        int start = 0;
        int end = this.mColumnSize + 0;
        for (int i = 0; start < oplusItems.size() && i < rowCounts; i++) {
            int lend = end < oplusItems.size() ? end : oplusItems.size();
            List<OplusItem> l = oplusItems.subList(start, lend);
            System.arraycopy(l.toArray(), 0, array[i], 0, l.size());
            start = end;
            end = start + this.mColumnSize;
        }
        return array;
    }

    @Override // com.oplus.resolver.IOplusGalleryPagerAdapter
    public void setOplusResolverItemEventListener(IOplusResolverGridItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override // com.oplus.resolver.IOplusGalleryPagerAdapter
    public void updateUserHandle(UserHandle userHandle) {
        if (userHandle != null) {
            this.mUserHandle = userHandle;
        }
    }

    @Override // com.oplus.resolver.IOplusGalleryPagerAdapter
    public void setColumnSize(int columnSize) {
        this.mColumnSize = columnSize;
    }

    @Override // com.oplus.resolver.IOplusGalleryPagerAdapter
    public void unRegister() {
        Log.d(TAG, "unRegister");
        destroyUI(this.mContext);
    }

    public void clickMoreIcon() {
        this.mNeedAnimation = true;
        this.mNeedMoreIcon = false;
    }

    public boolean isNeedMoreIcon() {
        return this.mNeedMoreIcon;
    }

    public int getMoreIconTotalPosition() {
        return this.mMoreIconTotalPosition;
    }

    public boolean isMoreIconPosition(int position) {
        return this.mNeedMoreIcon && this.mMoreIconTotalPosition == position;
    }

    public void updateNeedMoreIcon(Intent intent, int allRiListSize) {
        boolean isChooser = OplusResolverIntentUtil.isChooserAction(intent);
        int resolveTopSize = this.mResolveInfoHelper.getResolveTopSize(intent);
        boolean isExport = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_RESOLVER_SHARE_EMAIL);
        if (!isChooser && isExport && resolveTopSize > 0 && resolveTopSize < allRiListSize) {
            this.mNeedMoreIcon = true;
            this.mMoreIconTotalPosition = resolveTopSize;
        } else {
            this.mNeedMoreIcon = false;
            this.mMoreIconTotalPosition = 0;
        }
        Log.d(TAG, "init:resolveTopSize=" + resolveTopSize + ",mRiList=" + allRiListSize + ",mNeedMoreIcon=" + this.mNeedMoreIcon + ",isExport=" + isExport + ",isChooser=" + isChooser);
    }

    @Override // com.oplus.resolver.IOplusGalleryPagerAdapter
    public void dismiss() {
        Dialog dialog = this.mAlertDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mAlertDialog.dismiss();
            return;
        }
        Context context = this.mContext;
        if (context != null && (context instanceof Activity) && !((Activity) context).isFinishing()) {
            ((Activity) this.mContext).finish();
        }
    }

    @Override // com.oplus.resolver.IOplusGalleryPagerAdapter
    public List<OplusItem> loadBitmap(Intent originIntent, List<ResolveInfo> riList, int pagerNumber, int pagerSize, int placeholderCount) {
        int defaultSize;
        List<OplusItem> defaultAppInfo = new ArrayList<>();
        List<OplusItem> appInfo = new ArrayList<>();
        if (riList == null || riList.isEmpty()) {
            int defaultSize2 = (pagerNumber + 1) * pagerSize > placeholderCount ? placeholderCount - (pagerNumber * pagerSize) : pagerSize;
            defaultSize = defaultSize2;
        } else {
            int defaultSize3 = (pagerNumber + 1) * pagerSize > riList.size() ? riList.size() - (pagerNumber * pagerSize) : pagerSize;
            defaultSize = defaultSize3;
        }
        if (riList != null && !riList.isEmpty()) {
            List<ResolveInfo> resolveInfos = riList.subList(pagerNumber * pagerSize, (pagerNumber + 1) * pagerSize > riList.size() ? riList.size() : (pagerNumber + 1) * pagerSize);
            for (ResolveInfo info : resolveInfos) {
                String key = getMemCacheKey(info, originIntent);
                OplusItem item = getBitmapFromMemCache(key);
                if (item != null) {
                    Drawable dr = item.getIcon();
                    if (dr != null) {
                        Drawable.ConstantState state = dr.getConstantState();
                        Drawable icon = state.newDrawable();
                        item.setIcon(icon);
                    }
                    if (this.mResolveInfoHelper.getPinnedList() != null && this.mResolveInfoHelper.getPinnedList().contains(info)) {
                        item.setPinned(true);
                    } else {
                        item.setPinned(false);
                    }
                    appInfo.add(item);
                }
            }
        }
        if (appInfo.size() == defaultSize) {
            return appInfo;
        }
        if (this.mDefaultOplusItem == null) {
            this.mDefaultOplusItem = this.mCustomIconHelper.getDefaultAppInfo();
        }
        int columnCounts = this.mColumnSize;
        int rowCounts = (int) Math.min(Math.ceil(defaultSize / columnCounts), 2.0d);
        for (int i = 0; i < rowCounts; i++) {
            for (int j = 0; j < defaultSize - (i * columnCounts) && j < columnCounts; j++) {
                OplusItem item2 = new OplusItem();
                item2.setIcon(this.mDefaultOplusItem.getIcon().getConstantState().newDrawable());
                item2.setText(this.mDefaultOplusItem.getText());
                defaultAppInfo.add(item2);
            }
        }
        if (riList != null && !riList.isEmpty()) {
            if (this.mTaskHashMap.get(Integer.valueOf(pagerNumber)) == null) {
                BitmapTask bTask = new BitmapTask(originIntent, riList, pagerNumber, pagerSize);
                this.mTaskHashMap.put(Integer.valueOf(pagerNumber), new Thread(bTask));
                this.executor.execute(bTask);
            } else {
                Thread oldThread = this.mTaskHashMap.get(Integer.valueOf(pagerNumber));
                oldThread.interrupt();
                BitmapTask newTask = new BitmapTask(originIntent, riList, pagerNumber, pagerSize);
                this.mTaskHashMap.put(Integer.valueOf(pagerNumber), new Thread(newTask));
                this.executor.execute(newTask);
            }
        }
        return defaultAppInfo;
    }

    private List<OplusItem> initAppInfoTop(List<OplusItem> appinfo, int iconPosition) {
        List<OplusItem> appInfoTop = new ArrayList<>();
        int i = 0;
        int length = appinfo.size();
        while (true) {
            if (i >= length) {
                break;
            }
            if (i == iconPosition) {
                appInfoTop.add(this.mMoreItem);
                break;
            }
            appInfoTop.add(appinfo.get(i));
            i++;
        }
        return appInfoTop;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OplusItem getBitmapFromMemCache(String key) {
        return this.mMemoryCache.get(key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getMemCacheKey(ResolveInfo info, Intent originIntent) {
        boolean isMulti = isMultiAppResolveInfo(info);
        ActivityInfo ai = info.activityInfo;
        String key = this.mUserHandle.getIdentifier() + ai.applicationInfo.packageName + ai.name + isMulti;
        if (OplusResolverUtil.isChooserCtsTest(this.mContext, originIntent)) {
            return key + ((Object) info.nonLocalizedLabel);
        }
        return key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void addBitmapToMemoryCache(String key, OplusItem appinfo) {
        if (getBitmapFromMemCache(key) == null) {
            this.mMemoryCache.put(key, appinfo);
        }
    }

    @Override // com.oplus.resolver.IOplusResolverGridItemClickListener
    public void onItemClick(int pagerNumber, int position) {
        IOplusResolverGridItemClickListener iOplusResolverGridItemClickListener = this.mItemClickListener;
        if (iOplusResolverGridItemClickListener != null) {
            iOplusResolverGridItemClickListener.onItemClick(pagerNumber, position);
        }
    }

    @Override // com.oplus.resolver.IOplusResolverGridItemClickListener
    public void onItemLongClick(int pagerNumber, int position, View icon) {
        if (this.mItemClickListener != null && gallerySupportNewTips(this.mContext)) {
            this.mItemClickListener.onItemLongClick(pagerNumber, position, icon);
        }
    }

    @Override // com.oplus.resolver.IOplusResolverGridItemClickListener
    public void onItemLongClick(int pagerNumber, int position) {
        if (this.mItemClickListener != null && !gallerySupportNewTips(this.mContext)) {
            this.mItemClickListener.onItemLongClick(pagerNumber, position);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class BitmapTask implements Runnable {
        private Intent mOriginIntent;
        private int mPagerNumber;
        private int mPagerSize;
        private List<ResolveInfo> mRiList;

        BitmapTask(Intent originIntent, List<ResolveInfo> riList, int pagerNumber, int pagerSize) {
            this.mOriginIntent = originIntent;
            this.mRiList = riList;
            this.mPagerNumber = pagerNumber;
            this.mPagerSize = pagerSize;
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x0113 A[Catch: Exception -> 0x0158, TryCatch #0 {Exception -> 0x0158, blocks: (B:2:0x0000, B:4:0x0016, B:5:0x0023, B:6:0x002b, B:8:0x0031, B:10:0x004d, B:13:0x005c, B:15:0x0062, B:16:0x00d7, B:18:0x00e3, B:20:0x00f3, B:21:0x0103, B:23:0x0113, B:25:0x0125, B:26:0x011b, B:28:0x00fb, B:29:0x006e, B:31:0x0087, B:33:0x0095, B:34:0x009d, B:36:0x00a3, B:38:0x00af, B:40:0x00b3, B:42:0x0136, B:44:0x0140, B:47:0x0148, B:49:0x001d), top: B:1:0x0000 }] */
        /* JADX WARN: Removed duplicated region for block: B:26:0x011b A[Catch: Exception -> 0x0158, TryCatch #0 {Exception -> 0x0158, blocks: (B:2:0x0000, B:4:0x0016, B:5:0x0023, B:6:0x002b, B:8:0x0031, B:10:0x004d, B:13:0x005c, B:15:0x0062, B:16:0x00d7, B:18:0x00e3, B:20:0x00f3, B:21:0x0103, B:23:0x0113, B:25:0x0125, B:26:0x011b, B:28:0x00fb, B:29:0x006e, B:31:0x0087, B:33:0x0095, B:34:0x009d, B:36:0x00a3, B:38:0x00af, B:40:0x00b3, B:42:0x0136, B:44:0x0140, B:47:0x0148, B:49:0x001d), top: B:1:0x0000 }] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            UserHandle modifyMultiUserHandle;
            try {
                final List<OplusItem> oplusItems = new ArrayList<>();
                List<ResolveInfo> list = this.mRiList;
                int i = this.mPagerNumber;
                int i2 = this.mPagerSize;
                List<ResolveInfo> resolveInfos = list.subList(i * i2, (i + 1) * i2 > list.size() ? this.mRiList.size() : (this.mPagerNumber + 1) * this.mPagerSize);
                for (ResolveInfo info : resolveInfos) {
                    boolean isMulti = OplusResolverPagerAdapterHelper.this.isMultiAppResolveInfo(info);
                    String key = OplusResolverPagerAdapterHelper.this.getMemCacheKey(info, this.mOriginIntent);
                    OplusItem item = OplusResolverPagerAdapterHelper.this.getBitmapFromMemCache(key);
                    if (item != null && !OplusResolverUtil.isChooserCtsTest(OplusResolverPagerAdapterHelper.this.mContext, this.mOriginIntent)) {
                        Drawable dr = item.getIcon();
                        if (dr != null) {
                            Drawable.ConstantState state = dr.getConstantState();
                            Drawable icon = state.newDrawable();
                            item.setIcon(icon);
                        }
                        if (OplusResolverPagerAdapterHelper.this.mResolveInfoHelper.getPinnedList() == null && OplusResolverPagerAdapterHelper.this.mResolveInfoHelper.getPinnedList().contains(info)) {
                            item.setPinned(true);
                        } else {
                            item.setPinned(false);
                        }
                        PackageManager packageManager = OplusResolverPagerAdapterHelper.this.mContext.getPackageManager();
                        Drawable icon2 = item.getIcon();
                        if (isMulti) {
                            OplusResolverPagerAdapterHelper oplusResolverPagerAdapterHelper = OplusResolverPagerAdapterHelper.this;
                            modifyMultiUserHandle = oplusResolverPagerAdapterHelper.modifyMultiUserHandle(oplusResolverPagerAdapterHelper.mUserHandle);
                        } else {
                            modifyMultiUserHandle = new UserHandle(999);
                        }
                        item.setIcon(packageManager.getUserBadgedIcon(icon2, modifyMultiUserHandle));
                        oplusItems.add(item);
                        OplusResolverPagerAdapterHelper.this.addBitmapToMemoryCache(key, item);
                    }
                    PackageManager packageManager2 = OplusResolverPagerAdapterHelper.this.mContext.getPackageManager();
                    item = OplusResolverPagerAdapterHelper.this.mCustomIconHelper.getAppInfo(this.mOriginIntent, info, packageManager2);
                    if (isMulti) {
                        if ((this.mOriginIntent.getIntentExt().getOplusFlags() & 8192) != 0) {
                            Log.d(OplusResolverPagerAdapterHelper.TAG, "keep original chooser name for multi app");
                        } else {
                            ComponentInfo ci = info.getComponentInfo();
                            if (ci != null) {
                                String name = OplusMultiAppManager.getInstance().getMultiAppAlias(ci.packageName);
                                if (name != null) {
                                    item.setLabel(name);
                                }
                            } else {
                                item.setLabel(item.getText() + Resources.getSystem().getString(201588864));
                            }
                        }
                    }
                    if (OplusResolverPagerAdapterHelper.this.mResolveInfoHelper.getPinnedList() == null) {
                    }
                    item.setPinned(false);
                    PackageManager packageManager3 = OplusResolverPagerAdapterHelper.this.mContext.getPackageManager();
                    Drawable icon22 = item.getIcon();
                    if (isMulti) {
                    }
                    item.setIcon(packageManager3.getUserBadgedIcon(icon22, modifyMultiUserHandle));
                    oplusItems.add(item);
                    OplusResolverPagerAdapterHelper.this.addBitmapToMemoryCache(key, item);
                }
                if (Thread.currentThread().isInterrupted()) {
                    Log.e("ResolverBitmapTask", "interrupt current Thread");
                } else {
                    OplusResolverPagerAdapterHelper.this.mHandler.post(new Runnable() { // from class: com.oplus.resolver.OplusResolverPagerAdapterHelper.BitmapTask.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (OplusResolverPagerAdapterHelper.this.mTaskHashMap.size() != 0) {
                                OplusResolverPagerAdapterHelper.this.mTaskHashMap.remove(Integer.valueOf(BitmapTask.this.mPagerNumber));
                                if (OplusResolverPagerAdapterHelper.this.mAdapterCache.get(Integer.valueOf(BitmapTask.this.mPagerNumber)) != null) {
                                    OplusResolverGridAdapter gridAdapter = (OplusResolverGridAdapter) OplusResolverPagerAdapterHelper.this.mAdapterCache.get(Integer.valueOf(BitmapTask.this.mPagerNumber));
                                    OplusResolverPagerAdapterHelper.this.updateAdapterItems(gridAdapter, oplusItems, BitmapTask.this.mPagerNumber, BitmapTask.this.mPagerSize);
                                    gridAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateAdapterItems(OplusResolverGridAdapter adapter, List<OplusItem> list, int pagerNumber, int pagerSize) {
        if (list != null) {
            if (hasOsharePackage(list)) {
                this.mOShareManager.initOShareService();
            }
            int moreIconPosition = this.mMoreIconTotalPosition % pagerSize;
            int moreIconPageCount = (int) Math.ceil((r0 + 1) / pagerSize);
            if (this.mNeedMoreIcon && moreIconPageCount == pagerNumber + 1) {
                adapter.setOplusItems(initAppInfoTop(list, moreIconPosition));
            } else {
                adapter.setOplusItems(list);
                if (this.mNeedAnimation && moreIconPageCount == pagerNumber + 1) {
                    this.mNeedAnimation = false;
                    adapter.startMoreAnimation(moreIconPosition);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserHandle modifyMultiUserHandle(UserHandle userHandle) {
        if (userHandle.getIdentifier() != 999) {
            return userHandle;
        }
        return UserHandle.of(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMultiAppResolveInfo(ResolveInfo info) {
        if (info != null && info.getComponentInfo() != null) {
            return OplusMultiAppManager.getInstance().isMultiAppUserId(UserHandle.getUserId(info.getComponentInfo().applicationInfo.uid));
        }
        return false;
    }

    public void clearDrawableCache() {
        if (this.mMemoryCache.size() != 0) {
            this.mMemoryCache.evictAll();
        }
    }

    private void registerActivityLifecycle(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) this.mContext).registerActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
    }

    private boolean hasOsharePackage(List<OplusItem> appinfo) {
        return appinfo != null && appinfo.stream().filter(new Predicate() { // from class: com.oplus.resolver.OplusResolverPagerAdapterHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$hasOsharePackage$0;
                lambda$hasOsharePackage$0 = OplusResolverPagerAdapterHelper.this.lambda$hasOsharePackage$0((OplusItem) obj);
                return lambda$hasOsharePackage$0;
            }
        }).count() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$hasOsharePackage$0(OplusItem info) {
        return OplusOShareUtil.isOsharePackage(this.mContext, info.getPackageName());
    }

    private boolean gallerySupportNewTips(Context context) {
        if (context.getPackageName().equals(OplusThemeResources.FRAMEWORK_PACKAGE)) {
            return true;
        }
        try {
            ApplicationInfo application = context.getPackageManager().getApplicationInfo(this.mContext.getString(201589210), 128);
            Bundle bundle = application.metaData;
            if (bundle == null) {
                return false;
            }
            return bundle.getBoolean("support_gallery_new_ui", false);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyUI(Context context) {
        if (context instanceof Activity) {
            ((Activity) this.mContext).unregisterActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
        }
        if (OplusResolverSharedPrefs.getResolverSharedPrefs(this.mContext).getBoolean(OplusResolverSharedPrefs.OTA_COLOROS13, true) && !OplusResolverSharedPrefs.getResolverSharedPrefs(this.mContext).getBoolean(OplusResolverSharedPrefs.OTA_COLOROS13_TIPS, true)) {
            OplusResolverSharedPrefs.getResolverSharedPrefs(this.mContext).edit().putBoolean(OplusResolverSharedPrefs.OTA_COLOROS13, false).apply();
        }
        this.mOShareManager.onDestroy();
    }
}
