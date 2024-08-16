package android.view;

import android.app.Activity;
import android.app.OplusBracketModeUnit;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.InsetsSourceControl;
import android.view.SurfaceControl;
import android.view.View;
import android.view.WindowManager;
import com.android.internal.policy.DecorView;
import com.oplus.bracket.OplusBracketLog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class OplusViewMirrorManager {
    private static final String KEY_REASON = "reason";
    private static final String KEY_VIEW_CLASS = "class";
    private static final String KEY_VIEW_ID = "id";
    private static final String KEY_VIEW_LABEL = "label";
    private static final int MSG_ENTER_MIRROR_MODE = 1;
    private static final int MSG_EXIT_MIRROR_MODE = 2;
    private static final int MSG_TRY_ENTER_MIRROR_MODE = 3;
    private static final String REASON_TRY_ENTER = "TryEnter";
    private static final int STATE_MATCH_NONE = 0;
    private static final int STATE_MATCH_NORMAL_VIEW = 1;
    private static final int STATE_MATCH_SURFACE_VIEW = 2;
    private static final String TAG = "OplusViewMirrorManager";
    private static final String VALUE_MIRROR_CONTENT_VIEW = "mirror_content_view";
    private static volatile OplusViewMirrorManager sInstance;
    private ArrayList<OplusBracketModeUnit.SeparateUiView> mActivitySeparateUiViewList;
    private OplusMirrorContentLayout mContentView;
    private Context mContext;
    private OplusBracketModeUnit mGlobalBracketUnitData;
    private ViewRootImpl mHostViewRoot;
    private boolean mIsInMirrorMode;
    private ViewMirrorWindowlessManager mMirrorWindowlessManager;
    private SurfaceControlViewHost mSurfaceControlViewHost;
    final ViewMirrorHandler mHandler = new ViewMirrorHandler();
    final OplusMirrorViewSysUIHelper mSysUiHelper = new OplusMirrorViewSysUIHelper(this);
    private Object mLock = new Object();
    private int mWidth = 0;
    private int mHeight = 0;
    private ArrayList<Class> mGlobalSeparateViewTypes = new ArrayList<>();
    private boolean mSupportMirrorSurfaceView = false;
    private CopyOnWriteArrayList<View> mTargetViews = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<SurfaceView> mTargetSurfaceViews = new CopyOnWriteArrayList<>();
    private boolean mHostViewRefresh = false;
    private ConcurrentHashMap<WeakReference<ViewRootImpl>, Boolean> mHostViewRootMaps = new ConcurrentHashMap<>();

    private OplusViewMirrorManager() {
    }

    public static OplusViewMirrorManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusViewMirrorManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusViewMirrorManager();
                }
            }
        }
        return sInstance;
    }

    public void attachToWindow(ViewRootImpl hostViewRoot) {
        if (hostViewRoot == null) {
            return;
        }
        updateHostViewRootIfNeeded(hostViewRoot);
        if (this.mIsInMirrorMode && isSupportMirrorMode(hostViewRoot)) {
            this.mHandler.removeMessages(3);
            tryEnterMirrorMode(hostViewRoot.mBasePackageName, REASON_TRY_ENTER);
        }
    }

    private void updateHostViewRootIfNeeded(ViewRootImpl viewRoot) {
        if (!isSupportMirrorMode(viewRoot)) {
            OplusBracketLog.e(TAG, "updateHostViewRootIfNeeded, not support " + viewRoot);
            return;
        }
        if (isMirrorContentView(viewRoot.getView())) {
            OplusBracketLog.e(TAG, "updateHostViewRootIfNeeded, skip mirror content, viewRoot:" + viewRoot);
            return;
        }
        if (viewRoot.getWrapper().isRemoved()) {
            OplusBracketLog.e(TAG, "updateHostViewRootIfNeeded, skip removed viewRoot:" + viewRoot);
            return;
        }
        OplusBracketLog.d(TAG, "updateHostViewRootIfNeeded, decorView:" + viewRoot.getView() + ", hostVewRoot:" + viewRoot);
        this.mHostViewRoot = viewRoot;
        if (obtainCachedHostViewRoot(viewRoot) == null) {
            this.mHostViewRootMaps.put(new WeakReference<>(viewRoot), false);
        }
        updateBracketSeparateDataSource(this.mHostViewRoot);
    }

    private WeakReference<ViewRootImpl> obtainCachedHostViewRoot(ViewRootImpl viewRoot) {
        if (viewRoot == null || this.mHostViewRootMaps.isEmpty()) {
            return null;
        }
        for (WeakReference<ViewRootImpl> rootKey : this.mHostViewRootMaps.keySet()) {
            if (rootKey.get() == viewRoot) {
                return rootKey;
            }
        }
        return null;
    }

    private String obtainActivityInfoFromViewRoot(ViewRootImpl viewRoot) {
        if (viewRoot != null && (viewRoot.getView() instanceof DecorView)) {
            DecorView decorView = viewRoot.getView();
            Context windowContext = decorView.getWrapper().getWindow().getContext();
            if (windowContext instanceof Activity) {
                return ((Activity) windowContext).getComponentName().flattenToShortString();
            }
            return null;
        }
        return null;
    }

    private void updateBracketSeparateDataSource(ViewRootImpl viewRoot) {
        String activityName = obtainActivityInfoFromViewRoot(viewRoot);
        if (activityName == null) {
            OplusBracketLog.e(TAG, "updateBracketSeparateDataSource, can't obtain activity info");
        }
        if (this.mGlobalBracketUnitData.mSeparateActivityUnitList.size() > 0 && activityName != null) {
            OplusBracketModeUnit.SeparateActivityUnit activityUnit = this.mGlobalBracketUnitData.mSeparateActivityUnitList.get(activityName);
            if (activityUnit == null || activityUnit.separateUiViewList == null || activityUnit.separateUiViewList.isEmpty()) {
                OplusBracketLog.d(TAG, "updateBracketSeparateDataSource, match empty view list, activityName:" + activityName);
                this.mSupportMirrorSurfaceView = false;
                ArrayList<OplusBracketModeUnit.SeparateUiView> arrayList = this.mActivitySeparateUiViewList;
                if (arrayList != null) {
                    arrayList.clear();
                    this.mActivitySeparateUiViewList = null;
                    return;
                }
                return;
            }
            ArrayList<OplusBracketModeUnit.SeparateUiView> arrayList2 = activityUnit.separateUiViewList;
            this.mActivitySeparateUiViewList = arrayList2;
            this.mSupportMirrorSurfaceView = checkSupportMirrorSurfaceView(arrayList2);
            OplusBracketLog.d(TAG, "updateBracketSeparateDataSource success, activity:" + activityName + ", separateUiViewList:" + this.mActivitySeparateUiViewList);
        }
    }

    private boolean checkSupportMirrorSurfaceView(ArrayList<OplusBracketModeUnit.SeparateUiView> activityUnitViewList) {
        if (!this.mGlobalSeparateViewTypes.isEmpty() && this.mGlobalSeparateViewTypes.toString().contains("SurfaceView")) {
            return true;
        }
        Iterator<OplusBracketModeUnit.SeparateUiView> it = activityUnitViewList.iterator();
        while (it.hasNext()) {
            OplusBracketModeUnit.SeparateUiView separateUiView = it.next();
            String separateUiViewKey = separateUiView.key;
            String separateUiViewValue = separateUiView.value;
            if (separateUiViewKey.equals(KEY_VIEW_CLASS)) {
                try {
                    Class clazz = Class.forName(separateUiViewValue);
                    if (clazz.toString().contains("SurfaceView")) {
                        OplusBracketLog.d(TAG, "checkSupportMirrorSurfaceView true");
                        return true;
                    }
                    continue;
                } catch (ClassNotFoundException e) {
                    OplusBracketLog.e(TAG, "checkSupportMirrorSurfaceView, illegal view class:" + separateUiViewValue);
                }
            }
        }
        return false;
    }

    private int matchSeparateViewSource(View view) {
        int matchState = 0;
        ArrayList<OplusBracketModeUnit.SeparateUiView> arrayList = this.mActivitySeparateUiViewList;
        if (arrayList == null || arrayList.isEmpty()) {
            return 0;
        }
        if (this.mGlobalSeparateViewTypes != null) {
            for (int i = 0; i < this.mGlobalSeparateViewTypes.size(); i++) {
                if (this.mGlobalSeparateViewTypes.get(i).isInstance(view)) {
                    OplusBracketLog.e(TAG, "matchSeparateViewSource success,  for global view list:" + view);
                    return view instanceof SurfaceView ? 2 : 1;
                }
            }
        }
        Iterator<OplusBracketModeUnit.SeparateUiView> it = this.mActivitySeparateUiViewList.iterator();
        while (it.hasNext()) {
            OplusBracketModeUnit.SeparateUiView separateUiView = it.next();
            String separateUiViewKey = separateUiView.key;
            String separateUiViewValue = separateUiView.value;
            if (separateUiViewKey.equals("id")) {
                if (view != null) {
                    try {
                        if (view.getId() != -1) {
                            String resIDName = view.getResources().getResourceName(view.getId());
                            if (resIDName.contains(separateUiViewValue)) {
                                matchState = 1;
                            }
                        }
                    } catch (Resources.NotFoundException e) {
                        OplusBracketLog.e(TAG, "matchSeparateViewSource, illegal view id:" + view.getId());
                        matchState = 0;
                    }
                }
            } else if (separateUiViewKey.equals(KEY_VIEW_LABEL)) {
                if (view != null && view.toString() != null && view.toString().contains(separateUiViewValue)) {
                    matchState = 1;
                    Log.i(TAG, "matchSeparateViewSource a normal view:" + view);
                }
            } else if (separateUiViewKey.equals(KEY_VIEW_CLASS) && view != null) {
                try {
                    Class clazz = Class.forName(separateUiViewValue);
                    if (clazz.isInstance(view)) {
                        if (view instanceof SurfaceView) {
                            OplusBracketLog.d(TAG, "matchSeparateViewSource a SurfaceView:" + view);
                            matchState = 2;
                        } else {
                            matchState = 1;
                        }
                    }
                } catch (ClassNotFoundException e2) {
                    OplusBracketLog.e(TAG, "matchSeparateViewSource, illegal view class:" + separateUiViewValue);
                }
            }
            if (matchState > 0) {
                OplusBracketLog.d(TAG, "matchSeparateViewSource success, matchState:" + matchState + ", view:" + view);
            }
        }
        return matchState;
    }

    public void relayout(ViewRootImpl viewRoot, int width, int height) {
        if (isMirrorContentView(viewRoot.getView())) {
            OplusBracketLog.d(TAG, "relayout, skip mirror viewRootImpl:" + viewRoot);
            return;
        }
        int i = this.mWidth;
        if ((width != i || height != this.mHeight) && this.mHeight < i && isSupportMirrorMode(viewRoot) && this.mIsInMirrorMode && viewRoot == this.mHostViewRoot) {
            OplusBracketLog.d(TAG, "relayout, mWidth：" + this.mWidth + ", mHeight: " + this.mHeight + ", new width:" + width + ", new height:" + height + ", mContentView:" + this.mContentView + ", mHostViewRoot:" + this.mHostViewRoot);
            this.mWidth = width;
            this.mHeight = height;
            if (this.mContentView != null) {
                this.mSurfaceControlViewHost.relayout(width, height);
            }
        }
    }

    public void initMirrorContentIfNeed() {
        Display display;
        ViewRootImpl viewRootImpl = this.mHostViewRoot;
        if (viewRootImpl == null || !isSupportMirrorMode(viewRootImpl) || !this.mIsInMirrorMode) {
            OplusBracketLog.e(TAG, "initMirrorContent failed, mHostViewRoot:" + this.mHostViewRoot + ", isSupportMirrorMode:" + isSupportMirrorMode(this.mHostViewRoot) + ", isInMirrorMode:" + this.mIsInMirrorMode);
            return;
        }
        if (this.mContentView == null) {
            this.mHostViewRefresh = false;
            Context context = this.mHostViewRoot.mContext;
            this.mContext = context;
            if (context == null) {
                return;
            }
            int hostWidth = this.mHostViewRoot.getWidth();
            int hostHeight = this.mHostViewRoot.getHeight();
            OplusBracketLog.d(TAG, "initMirrorContent hostViewRoot：" + this.mHostViewRoot + ", hostWidth:" + hostWidth + ", hostHeight:" + hostHeight + ", mWidth:" + this.mWidth + ", mHeight:" + this.mHeight);
            Context context2 = this.mContext;
            if (context2 != null && (display = context2.getDisplay()) != null) {
                Display.Mode mode = display.getMode();
                if (mode != null) {
                    if (hostWidth != display.getMode().getPhysicalHeight()) {
                        hostWidth = display.getMode().getPhysicalHeight();
                        OplusBracketLog.d(TAG, "initMirrorContent hostViewRoot：" + this.mHostViewRoot + ", change hostWidth:" + hostWidth);
                    }
                    if (hostHeight != display.getMode().getPhysicalWidth()) {
                        hostHeight = display.getMode().getPhysicalWidth();
                        OplusBracketLog.d(TAG, "initMirrorContent hostViewRoot：" + this.mHostViewRoot + ", change hostHeight:" + hostHeight);
                    }
                }
            }
            OplusMirrorContentLayout oplusMirrorContentLayout = new OplusMirrorContentLayout(this.mContext);
            this.mContentView = oplusMirrorContentLayout;
            oplusMirrorContentLayout.setBackgroundColor(0);
            this.mContentView.addTargetViews(this.mTargetViews);
            this.mContentView.setViewSizeLimit(isViewSizeLimit());
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(0, 0, 2038, 32, -3);
            int i = this.mWidth;
            if (i <= 1) {
                lp.width = Math.max(i, hostWidth);
                lp.height = Math.max(this.mHeight, hostHeight);
            } else {
                lp.width = i;
                lp.height = this.mHeight;
            }
            lp.token = new Binder();
            lp.inputFeatures |= 1;
            lp.setTitle("OplusViewMirrorLayer");
            if (this.mSurfaceControlViewHost == null) {
                Context context3 = this.mContext;
                Context windowContext = context3.createWindowContext(context3.getDisplay(), 2038, null);
                SurfaceControl surfaceViewBoundsLayer = null;
                if (this.mSupportMirrorSurfaceView && (surfaceViewBoundsLayer = this.mHostViewRoot.getWrapper().getSurfaceViewBoundsLayer()) == null) {
                    surfaceViewBoundsLayer = this.mHostViewRoot.getBoundsLayer();
                }
                this.mMirrorWindowlessManager = new ViewMirrorWindowlessManager(this.mContext.getResources().getConfiguration(), null, null, this.mHostViewRoot.getSurfaceControl(), surfaceViewBoundsLayer);
                this.mSurfaceControlViewHost = new SurfaceControlViewHost(windowContext, this.mContext.getDisplay(), this.mMirrorWindowlessManager, "untracked");
            }
            this.mContentView.setTag(VALUE_MIRROR_CONTENT_VIEW);
            this.mSurfaceControlViewHost.setView(this.mContentView, lp);
            this.mHostViewRoot.getWrapper().getExtImpl().addMirrorSurfaceControl(getMirrorToken(), getMirrorRootLeash());
            adjustSurfaceViewListRelativeZ();
            return;
        }
        invalidateHostViewsIfNeeded(this.mHostViewRoot, true);
    }

    public void tryEnterMirrorMode(String packageName, String reason) {
        OplusBracketLog.d(TAG, "tryEnterMirrorMode reason:" + reason + ", packageName:" + packageName);
        Message msg = this.mHandler.obtainMessage(3, packageName);
        Bundle bundle = new Bundle();
        bundle.putString("reason", reason);
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
    }

    public void enterMirrorMode(String packageName, String reason) {
        OplusBracketLog.d(TAG, "enterMirrorMode reason:" + reason + ", packageName:" + packageName);
        Message msg = this.mHandler.obtainMessage(1, packageName);
        Bundle bundle = new Bundle();
        bundle.putString("reason", reason);
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enterMirrorModeInternal(String packageName, String reason) {
        ViewRootImpl viewRoot;
        ViewRootImpl viewRootImpl = this.mHostViewRoot;
        boolean isSurfaceValid = (viewRootImpl == null || viewRootImpl.getSurfaceControl() == null || !this.mHostViewRoot.getSurfaceControl().isValid()) ? false : true;
        if ((this.mHostViewRoot == null || !isSurfaceValid) && this.mHostViewRootMaps.size() > 0) {
            OplusBracketLog.d(TAG, "enterMirrorModeInternal hostViewRoot is invalid, obtain object from cache!");
            for (WeakReference<ViewRootImpl> rootKey : this.mHostViewRootMaps.keySet()) {
                if (this.mHostViewRootMaps.get(rootKey).booleanValue() && (viewRoot = rootKey.get()) != null && viewRoot.getSurfaceControl() != null && viewRoot.getSurfaceControl().isValid()) {
                    this.mHostViewRoot = viewRoot;
                    isSurfaceValid = true;
                }
            }
        }
        OplusBracketLog.d(TAG, "enterMirrorModeInternal, app:" + packageName + ", reason:" + reason + ", mHostViewRoot:" + this.mHostViewRoot + ", isSurfaceValid:" + isSurfaceValid);
        if (!isSupportMirrorMode(this.mHostViewRoot)) {
            OplusBracketLog.d(TAG, "enterMirrorModeInternal not support, app:" + packageName + ", mHostViewRoot:" + this.mHostViewRoot + ", mHostViewMaps.size:" + this.mHostViewRootMaps.size());
            return;
        }
        if (isSurfaceValid) {
            initMirrorContentIfNeed();
        }
        if (!REASON_TRY_ENTER.equals(reason)) {
            this.mSysUiHelper.initSysUIVisibility();
            this.mSysUiHelper.initCompatSystemBarsViewVisibility();
        }
    }

    public void exitMirrorMode(String packageName, String reason) {
        OplusBracketLog.d(TAG, "exitMirrorMode " + packageName + ", reason：" + reason);
        Message msg = this.mHandler.obtainMessage(2, packageName);
        Bundle bundle = new Bundle();
        bundle.putString("reason", reason);
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitMirrorModeInternal(String packageName, String reason) {
        OplusBracketLog.d(TAG, "exitMirrorModeInternal release, app:" + packageName + ", reason：" + reason);
        this.mHandler.removeMessages(2);
        this.mIsInMirrorMode = false;
        this.mSysUiHelper.restoreSysUIVisibility();
        this.mSysUiHelper.restoreCompatSystemBarsViewVisibility();
        release(false);
    }

    public void die(ViewRootImpl viewRoot) {
        OplusBracketLog.d(TAG, "die ? app:" + viewRoot.mContext.getPackageName() + ", viewRoot:" + viewRoot + ", mHostViewRoot:" + this.mHostViewRoot);
        WeakReference<ViewRootImpl> cachedViewRoot = obtainCachedHostViewRoot(viewRoot);
        if (cachedViewRoot != null) {
            this.mHostViewRootMaps.remove(cachedViewRoot);
        }
        if (viewRoot == this.mHostViewRoot) {
            OplusBracketLog.d(TAG, "die really, mirrorRoot:" + getMirrorRootLeash());
            release(true);
        }
    }

    public void markTargetViewIfNeed(ViewRootImpl viewRoot, View view) {
        if (!isSupportMirrorMode(viewRoot) || isMirrorContentView(viewRoot.getView())) {
            return;
        }
        int state = matchSeparateViewSource(view);
        if (state == 1 && !this.mTargetViews.contains(view)) {
            this.mTargetViews.add(view);
            OplusMirrorContentLayout oplusMirrorContentLayout = this.mContentView;
            if (oplusMirrorContentLayout != null) {
                oplusMirrorContentLayout.addTargetView(view);
                this.mContentView.requestLayout();
            }
            OplusBracketLog.d(TAG, "markTargetViewIfNeed " + view + ", list size:" + this.mTargetViews.size());
            return;
        }
        if (state == 2 && !this.mTargetSurfaceViews.contains(view) && this.mSupportMirrorSurfaceView && (view instanceof SurfaceView)) {
            this.mTargetSurfaceViews.add((SurfaceView) view);
            OplusBracketLog.d(TAG, "markTargetViewIfNeed a SurfaceView:" + view + ", list size:" + this.mTargetSurfaceViews.size());
        }
    }

    public void clearTargetViewIfNeed(ViewRootImpl viewRoot, View view) {
        if (!isSupportMirrorMode(viewRoot) || isMirrorContentView(viewRoot.getView())) {
            return;
        }
        if ((view instanceof SurfaceView) && this.mTargetSurfaceViews.contains(view)) {
            this.mTargetSurfaceViews.remove(view);
            OplusBracketLog.d(TAG, "clearTargetViewIfNeed a SurfaceView:" + view + ", list size:" + this.mTargetSurfaceViews.size());
        }
        if (view != null && this.mTargetViews.contains(view)) {
            this.mTargetViews.remove(view);
            OplusMirrorContentLayout oplusMirrorContentLayout = this.mContentView;
            if (oplusMirrorContentLayout != null) {
                oplusMirrorContentLayout.removeTargetView(view);
                this.mContentView.requestLayout();
            }
            OplusBracketLog.d(TAG, "clearTargetViewIfNeed:" + view + ", list size:" + this.mTargetViews.size());
        }
    }

    public boolean shouldDrawOnMirrorContent(ViewRootImpl viewRoot, View view) {
        if (!isSupportMirrorMode(viewRoot) || !this.mIsInMirrorMode || isMirrorContentView(viewRoot.getView()) || !this.mTargetViews.contains(view)) {
            return false;
        }
        boolean isViewSizeLimit = isViewSizeLimit();
        OplusBracketLog.d(TAG, "shouldDrawOnMirrorContent yes, view=" + view + ", width:" + view.getWidth() + ", viewSizeLimit:" + isViewSizeLimit);
        return OplusViewMirrorUtils.checkViewSizeConstraints(isViewSizeLimit(), view, viewRoot);
    }

    public void drawRoot(ViewRootImpl hostViewRoot) {
        if (isSupportMirrorMode(hostViewRoot) && this.mContentView != null && !isMirrorContentView(hostViewRoot.getView()) && this.mContentView.getViewRootImpl() != null) {
            View.AttachInfo attachInfo = this.mContentView.getViewRootImpl().mAttachInfo;
            deduplicateTargetViewsIfNeeded();
            if (attachInfo != null) {
                ThreadedRenderer threadedRenderer = attachInfo.mThreadedRenderer;
                OplusMirrorContentLayout oplusMirrorContentLayout = this.mContentView;
                threadedRenderer.draw(oplusMirrorContentLayout, attachInfo, oplusMirrorContentLayout.getViewRootImpl());
            }
            invalidateHostViewsIfNeeded(hostViewRoot, false);
        }
    }

    public SurfaceControl getMirrorRootLeash() {
        ViewMirrorWindowlessManager viewMirrorWindowlessManager = this.mMirrorWindowlessManager;
        if (viewMirrorWindowlessManager == null) {
            return null;
        }
        SurfaceControl result = viewMirrorWindowlessManager.getMirrorRootLeash();
        return result;
    }

    public IBinder getMirrorToken() {
        ViewMirrorWindowlessManager viewMirrorWindowlessManager = this.mMirrorWindowlessManager;
        if (viewMirrorWindowlessManager == null) {
            return null;
        }
        IBinder token = viewMirrorWindowlessManager.getMirrorToken();
        return token;
    }

    public void notifySurfaceDestroyed(ViewRootImpl viewRoot) {
        WeakReference<ViewRootImpl> cachedViewRoot = obtainCachedHostViewRoot(viewRoot);
        if (cachedViewRoot != null) {
            OplusBracketLog.d(TAG, "notifySurfaceDestroyed " + viewRoot + ", cachedViewRoot:" + cachedViewRoot);
            this.mHostViewRootMaps.put(cachedViewRoot, false);
        }
        OplusBracketLog.d(TAG, "notifySurfaceDestroyed " + viewRoot + ", mHostViewRoot:" + this.mHostViewRoot);
        if (viewRoot == this.mHostViewRoot) {
            release(false);
        }
    }

    public void updateBlastSurface(ViewRootImpl viewRoot) {
        if (viewRoot == null) {
            return;
        }
        WeakReference<ViewRootImpl> cachedViewRoot = obtainCachedHostViewRoot(viewRoot);
        if (cachedViewRoot != null) {
            OplusBracketLog.d(TAG, "updateBlastSurface " + viewRoot + ", cachedViewRoot:" + cachedViewRoot);
            this.mHostViewRootMaps.put(cachedViewRoot, true);
        }
        if (viewRoot == this.mHostViewRoot) {
            if (this.mIsInMirrorMode && isSupportMirrorMode(viewRoot) && this.mContentView == null) {
                OplusBracketLog.d(TAG, "updateBlastSurface reenter " + viewRoot);
                this.mHandler.removeMessages(3);
                tryEnterMirrorMode(viewRoot.mBasePackageName, REASON_TRY_ENTER);
                return;
            }
            return;
        }
        if (this.mContentView == null && isSupportMirrorMode(viewRoot)) {
            OplusBracketLog.d(TAG, "updateBlastSurface, force reenter, viewRoot:" + viewRoot);
            release(false);
            updateHostViewRootIfNeeded(viewRoot);
            if (this.mIsInMirrorMode) {
                this.mHandler.removeMessages(3);
                tryEnterMirrorMode(viewRoot.mBasePackageName, REASON_TRY_ENTER);
            }
        }
    }

    public void release(boolean die) {
        OplusBracketLog.d(TAG, "release, die:" + die);
        ViewRootImpl viewRootImpl = this.mHostViewRoot;
        if (viewRootImpl != null) {
            viewRootImpl.getWrapper().getExtImpl().notifySurfaceViewReplaced();
            this.mHostViewRoot.getWrapper().getExtImpl().removeMirrorSurfaceControl(getMirrorToken());
        }
        SurfaceControlViewHost surfaceControlViewHost = this.mSurfaceControlViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mSurfaceControlViewHost = null;
        }
        OplusMirrorContentLayout oplusMirrorContentLayout = this.mContentView;
        if (oplusMirrorContentLayout != null) {
            oplusMirrorContentLayout.release();
            this.mContentView = null;
        }
        ViewMirrorWindowlessManager viewMirrorWindowlessManager = this.mMirrorWindowlessManager;
        if (viewMirrorWindowlessManager != null) {
            viewMirrorWindowlessManager.release();
        }
        if (die) {
            this.mTargetViews.clear();
            this.mTargetSurfaceViews.clear();
            this.mHostViewRoot = null;
            this.mWidth = 0;
            this.mHeight = 0;
        }
        this.mSysUiHelper.release(die);
        this.mContext = null;
    }

    private boolean isMirrorContentView(View view) {
        if (view == null) {
            return false;
        }
        Object tag = view.getTag();
        if (!(tag instanceof String) || !tag.equals(VALUE_MIRROR_CONTENT_VIEW)) {
            return false;
        }
        return true;
    }

    public boolean isSupportMirrorMode(ViewRootImpl viewRoot) {
        String activityName;
        if (this.mGlobalBracketUnitData == null || viewRoot == null || (activityName = obtainActivityInfoFromViewRoot(viewRoot)) == null || this.mGlobalBracketUnitData.mSeparateActivityUnitList == null) {
            return false;
        }
        OplusBracketModeUnit.SeparateActivityUnit activityUnit = this.mGlobalBracketUnitData.mSeparateActivityUnitList.get(activityName);
        return this.mGlobalBracketUnitData.mSupportSeparateUi && activityUnit != null;
    }

    public boolean isViewSizeLimit() {
        OplusBracketModeUnit oplusBracketModeUnit = this.mGlobalBracketUnitData;
        if (oplusBracketModeUnit == null) {
            return false;
        }
        return oplusBracketModeUnit.mViewSizeLimit;
    }

    public void updateViewByOrder(ViewRootImpl viewRoot, View view) {
        OplusMirrorContentLayout oplusMirrorContentLayout;
        if (isSupportMirrorMode(viewRoot) && this.mIsInMirrorMode && !isMirrorContentView(viewRoot.getView()) && (oplusMirrorContentLayout = this.mContentView) != null) {
            oplusMirrorContentLayout.updateViewByOrder(view);
        }
    }

    public void setHostViewRefresh(boolean hostViewRefresh) {
        this.mHostViewRefresh = hostViewRefresh;
    }

    private void invalidateHostViewsIfNeeded(ViewRootImpl hostViewRoot, boolean force) {
        boolean isMirrorView = isMirrorContentView(hostViewRoot.getView());
        if ((!this.mHostViewRefresh || force) && !this.mTargetViews.isEmpty()) {
            OplusBracketLog.d(TAG, "invalidateHostViewsIfNeeded, force:" + force + ", isMirrorView:" + isMirrorView + ", hostViewRoot:" + hostViewRoot + ", targetView size:" + this.mTargetViews.size());
            Iterator<View> it = this.mTargetViews.iterator();
            while (it.hasNext()) {
                View view = it.next();
                view.requestLayout();
            }
        }
    }

    private void adjustSurfaceViewListRelativeZ() {
        if (this.mHostViewRoot != null && this.mContentView != null) {
            OplusBracketLog.d(TAG, "adjustSurfaceViewListRelativeZ, surfaceViews:" + this.mTargetSurfaceViews);
            SurfaceControl.Transaction t = new SurfaceControl.Transaction();
            Iterator<SurfaceView> it = this.mTargetSurfaceViews.iterator();
            while (it.hasNext()) {
                SurfaceView view = it.next();
                updateSurfaceViewRelativeZ(this.mHostViewRoot, view, t);
            }
            this.mHostViewRoot.applyTransactionOnDraw(t);
        }
    }

    private void deduplicateTargetViewsIfNeeded() {
        CopyOnWriteArrayList<View> tempList = this.mTargetViews;
        int prevSize = this.mTargetViews.size();
        Iterator<View> it = this.mTargetViews.iterator();
        while (it.hasNext()) {
            View view = it.next();
            if (checkViewInTargetViews(view, tempList)) {
                tempList.remove(view);
            }
        }
        if (prevSize > this.mTargetViews.size()) {
            OplusBracketLog.d(TAG, "deduplicateTargetViewsIfNeeded success, prevSize:" + prevSize + ", currentSize:" + this.mTargetViews.size() + ", mTargetViews:" + this.mTargetViews);
            this.mContentView.release();
            this.mContentView.addTargetViews(this.mTargetViews);
        }
    }

    private boolean checkViewInTargetViews(View view, CopyOnWriteArrayList<View> loopList) {
        if (view.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (loopList.contains(parent)) {
                return true;
            }
            return checkViewInTargetViews(parent, loopList);
        }
        return false;
    }

    public void initBracketModeUnitDataSource(OplusBracketModeUnit bracketModeUnit) {
        OplusBracketLog.d(TAG, "initBracketModeUnitDataSource, bracketModeUnit:" + bracketModeUnit);
        if (bracketModeUnit != null) {
            this.mGlobalBracketUnitData = bracketModeUnit;
            if (bracketModeUnit.mSupportSeparateUi) {
                OplusBracketLog.d(TAG, "initBracketModeUnitDataSource, supportSeparateUi true");
            }
            this.mGlobalSeparateViewTypes.clear();
            if (!this.mGlobalBracketUnitData.mSeparateGlobalViewList.isEmpty()) {
                Iterator<OplusBracketModeUnit.SeparateUiView> it = this.mGlobalBracketUnitData.mSeparateGlobalViewList.iterator();
                while (it.hasNext()) {
                    OplusBracketModeUnit.SeparateUiView uiView = it.next();
                    if (uiView.key.equals(KEY_VIEW_CLASS)) {
                        try {
                            Class clazz = Class.forName(uiView.value);
                            this.mGlobalSeparateViewTypes.add(clazz);
                            OplusBracketLog.d(TAG, "initBracketModeUnitDataSource, add clazz=" + clazz.toString() + " to mGlobalSeparateViewTypes:" + this.mGlobalSeparateViewTypes);
                        } catch (ClassNotFoundException e) {
                            OplusBracketLog.e(TAG, "initBracketModeUnitDataSource, illegal className:" + uiView.value);
                        }
                    }
                }
            }
            if (this.mGlobalBracketUnitData.mSeparateActivityUnitList.size() > 0) {
                OplusBracketLog.d(TAG, "initBracketModeUnitDataSource, separateActivityUnitList:" + this.mGlobalBracketUnitData.mSeparateActivityUnitList);
            }
        }
    }

    public void updateSurfaceViewRelativeZ(ViewRootImpl viewRoot, SurfaceView view, SurfaceControl.Transaction t) {
        if (this.mIsInMirrorMode && isSupportMirrorMode(viewRoot) && this.mSupportMirrorSurfaceView && getMirrorRootLeash() != null && getMirrorRootLeash().isValid() && viewRoot == this.mHostViewRoot) {
            boolean isCachedView = this.mTargetSurfaceViews.contains(view);
            boolean isValidView = view.getSurfaceControl() != null && view.getSurfaceControl().isValid();
            OplusBracketLog.d(TAG, "updateSurfaceViewRelativeZ, viewRoot:" + viewRoot + ", isCachedView:" + isCachedView + ", isValidView:" + isValidView + ", v:" + view);
            SurfaceControl backgroundControl = view.mBackgroundControl;
            SurfaceControl surfaceControl = view.mSurfaceControl;
            if (backgroundControl != null && backgroundControl.isValid()) {
                t.setRelativeLayer(backgroundControl, getMirrorRootLeash(), Integer.MIN_VALUE);
            }
            if (surfaceControl != null && surfaceControl.isValid()) {
                t.setRelativeLayer(surfaceControl, getMirrorRootLeash(), view.mSubLayer);
            }
        }
    }

    public Rect adjustSurfaceViewFrameIfNeed(ViewRootImpl viewRoot, SurfaceView view, Rect inRec) {
        boolean isLegalRec = false;
        boolean z = true;
        boolean isSurfaceValid = view.getSurfaceControl() != null && view.getSurfaceControl().isValid();
        Rect outRec = inRec;
        Rect limitRec = new Rect(0, 0, (this.mWidth * 5) / 6, (this.mHeight * 2) / 3);
        Rect absInRec = new Rect(0, 0, inRec.width(), inRec.height());
        if (this.mIsInMirrorMode && isSupportMirrorMode(viewRoot) && this.mTargetSurfaceViews.contains(view) && this.mWidth > 0 && viewRoot == this.mHostViewRoot) {
            if (!absInRec.contains(limitRec) && !limitRec.contains(absInRec)) {
                z = false;
            }
            isLegalRec = z;
            if (!isLegalRec) {
                outRec = new Rect(0, 0, this.mWidth, this.mHeight);
            } else if (isLegalRec) {
                if (inRec.left < 0 || inRec.top < 0) {
                    outRec = absInRec;
                } else if (absInRec.width() > this.mWidth || absInRec.height() > this.mHeight) {
                    outRec = new Rect(0, 0, this.mWidth, this.mHeight);
                }
            }
        }
        OplusBracketLog.d(TAG, "adjustSurfaceViewFrameIfNeed, viewRoot:" + viewRoot + ", inRec:" + inRec + ", absInRec:" + absInRec + ", limitRec:" + limitRec + ", outRec:" + outRec + ", isLegalRec:" + isLegalRec + ", isSurfaceValid:" + isSurfaceValid + ", v:" + view);
        return outRec;
    }

    public void hookDispatchDispatchSystemUiVisibilityChanged(ViewRootImpl viewRoot) {
        this.mSysUiHelper.hookDispatchDispatchSystemUiVisibilityChanged(viewRoot);
    }

    /* loaded from: classes.dex */
    public static class ViewMirrorWindowlessManager extends WindowlessWindowManager {
        SurfaceControl mHostRootLeash;
        SurfaceControl mMirrorRootLeash;
        IBinder mMirrorToken;
        SurfaceControl mSurfaceViewBoundsLayer;

        public ViewMirrorWindowlessManager(Configuration c, SurfaceControl rootSurface, IBinder hostInputToken, SurfaceControl hostRootLeash, SurfaceControl surfaceViewBoundsLayer) {
            super(c, rootSurface, hostInputToken);
            this.mHostRootLeash = hostRootLeash;
            this.mSurfaceViewBoundsLayer = surfaceViewBoundsLayer;
        }

        protected SurfaceControl getParentSurface(IWindow window, WindowManager.LayoutParams attrs) {
            SurfaceControl.Builder builder = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("OplusViewMirrorLayerContainer").setHidden(true).setCallsite("OplusViewMirrorManager mirrorLeash");
            SurfaceControl build = builder.build();
            this.mMirrorRootLeash = build;
            return build;
        }

        public int addToDisplay(IWindow window, WindowManager.LayoutParams attrs, int viewVisibility, int displayId, int requestedVisibleTypes, InputChannel outInputChannel, InsetsState outInsetsState, InsetsSourceControl.Array outActiveControls, Rect outAttachedFrame, float[] outSizeCompatScale) {
            int result = super.addToDisplay(window, attrs, viewVisibility, displayId, requestedVisibleTypes, outInputChannel, outInsetsState, outActiveControls, outAttachedFrame, outSizeCompatScale);
            this.mMirrorToken = window.asBinder();
            SurfaceControl mirrorRootControl = getSurfaceControl(window);
            if (this.mSurfaceViewBoundsLayer != null) {
                SurfaceControl.Transaction t = new SurfaceControl.Transaction();
                t.reparent(this.mSurfaceViewBoundsLayer, mirrorRootControl).show(this.mSurfaceViewBoundsLayer);
                t.apply();
            }
            OplusBracketLog.d(OplusViewMirrorManager.TAG, "addToDisplay mirrorRootSC:" + mirrorRootControl + ", mSurfaceViewBoundsLayer:" + this.mSurfaceViewBoundsLayer);
            return result;
        }

        public SurfaceControl getMirrorRootLeash() {
            return this.mMirrorRootLeash;
        }

        public IBinder getMirrorToken() {
            return this.mMirrorToken;
        }

        public void release() {
            SurfaceControl surfaceControl;
            SurfaceControl surfaceControl2 = this.mSurfaceViewBoundsLayer;
            if (surfaceControl2 != null && surfaceControl2.isValid() && (surfaceControl = this.mHostRootLeash) != null && surfaceControl.isValid()) {
                SurfaceControl.Transaction t = new SurfaceControl.Transaction();
                t.reparent(this.mSurfaceViewBoundsLayer, this.mHostRootLeash);
                t.apply();
            }
            this.mMirrorRootLeash = null;
            this.mHostRootLeash = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ViewMirrorHandler extends Handler {
        private ViewMirrorHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    OplusViewMirrorManager.this.mIsInMirrorMode = true;
                    Bundle data = message.getData();
                    if (data != null) {
                        OplusViewMirrorManager.this.enterMirrorModeInternal((String) message.obj, data.getString("reason"));
                        return;
                    }
                    return;
                case 2:
                    Bundle exitData = message.getData();
                    if (exitData != null) {
                        OplusViewMirrorManager.this.exitMirrorModeInternal((String) message.obj, exitData.getString("reason"));
                        return;
                    }
                    return;
                case 3:
                    Bundle tryData = message.getData();
                    if (tryData != null) {
                        OplusViewMirrorManager.this.enterMirrorModeInternal((String) message.obj, tryData.getString("reason"));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public ViewRootImpl getHostViewRoot() {
        return this.mHostViewRoot;
    }

    public boolean isInMirrorMode() {
        return this.mIsInMirrorMode;
    }
}
