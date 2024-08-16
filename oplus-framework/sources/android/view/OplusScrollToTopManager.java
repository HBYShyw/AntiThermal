package android.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusScrollToTopManager implements IOplusScrollToTopManager {
    private static final String ACTION_CLICK_ON_STATUS_BAR = "com.oplus.clicktop";
    private static final String ACTION_USER_DISMISS_GUIDE_POPUP = "com.oplus.guide_popup_dismiss_by_user";
    private static final int FOCUS_RUNNABLE_DELAY = 1000;
    private static final String GUIDE_DISMISS_INTENT_EXTRA_KEY = "user_handle";
    private static final String GUIDE_POPUP_DISMISS_BY_USER_KEY = "guide_popup_dismiss_by_user_key";
    private static final String GUIDE_POPUP_DISMISS_BY_USER_VALUE = "guide_popup_dismiss_by_user_value";
    private static final int SHOW_POPUP_DELAY = 200;
    private static final String SYSTEM_UI_PACKAGE_NAME = "com.android.systemui";
    private static final int TOUCH_MOVE_THRESHOLD = 25;
    private static boolean sHasShownGuildPopup;
    private static boolean sIsInWhiteList;
    private PopupWindow mGuidePopup;
    private Handler mH;
    private PointF mLastDownPoint;
    private Handler mMainHandler;
    private Runnable mShowPopupTask;
    private View mView;
    private Rect mWinFrame;
    private WindowManager.LayoutParams mWindowAttributes;
    private static final String TAG = OplusScrollToTopManager.class.getSimpleName();
    private static boolean sHasRegisteredReceiverInSystemUI = false;
    private List<View> mScrollableViews = new ArrayList();
    private boolean mIsDying = false;
    private BroadcastReceiver mDismissPopupBroadcastReceiver = new BroadcastReceiver() { // from class: android.view.OplusScrollToTopManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            OplusScrollToTopManager.this.guidePopupHasDismissed(context, intent);
        }
    };
    private BroadcastReceiver mSystemUIBroadcastReceiver = new BroadcastReceiver() { // from class: android.view.OplusScrollToTopManager.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            OplusScrollToTopManager.this.log("receive click status intent");
            OplusScrollToTopManager.this.getTopScrollableChild();
            for (int i = 0; i < OplusScrollToTopManager.this.mScrollableViews.size(); i++) {
                View scrollTarget = (View) OplusScrollToTopManager.this.mScrollableViews.get(i);
                boolean reflectSucceed = OplusScrollToTopManager.this.reflectSmoothScrollForScrollableView(scrollTarget);
                if (!reflectSucceed) {
                    scrollTarget.scrollTo(0, 0);
                }
                OplusScrollToTopManager.this.dismissPopup(context);
            }
        }
    };
    private DelayRegisterReceiverRunnable mFocusChangeRunnable = new DelayRegisterReceiverRunnable();

    public OplusScrollToTopManager() {
        init();
    }

    /* loaded from: classes.dex */
    private static class Holder {
        private static final OplusScrollToTopManager INSTANCE = new OplusScrollToTopManager();

        private Holder() {
        }
    }

    private void init() {
        initHandlerAndRunnable();
    }

    private void initHandlerAndRunnable() {
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mShowPopupTask = new Runnable() { // from class: android.view.OplusScrollToTopManager.3
            @Override // java.lang.Runnable
            public void run() {
                if (OplusScrollToTopManager.this.mView == null || OplusScrollToTopManager.this.mView.getContext() == null) {
                    return;
                }
                OplusScrollToTopManager oplusScrollToTopManager = OplusScrollToTopManager.this;
                if (!oplusScrollToTopManager.needShowPopup(oplusScrollToTopManager.mView)) {
                    return;
                }
                OplusScrollToTopManager.this.getTopScrollableChild();
                if (OplusScrollToTopManager.this.mScrollableViews.size() > 0) {
                    OplusScrollToTopManager oplusScrollToTopManager2 = OplusScrollToTopManager.this;
                    oplusScrollToTopManager2.showGuidePopup(oplusScrollToTopManager2.mView);
                }
            }
        };
        this.mLastDownPoint = new PointF();
    }

    private void checkOrInitBgHandler() {
        if (this.mH == null) {
            this.mH = OplusViewBackgroundThread.getHandler();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void guidePopupHasDismissed(Context context, Intent intent) {
        try {
            log("received guidePopupHasDismissed intent, write to Settings");
            int userHandle = intent.getIntExtra(GUIDE_DISMISS_INTENT_EXTRA_KEY, -1);
            if (userHandle == -1) {
                log("error on getting userHandle in guidePopupDismiss intent");
            } else {
                Settings.System.putStringForUser(context.getContentResolver(), GUIDE_POPUP_DISMISS_BY_USER_KEY, GUIDE_POPUP_DISMISS_BY_USER_VALUE, userHandle);
                log("writed guide popup dismiss in settings for user " + userHandle);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerGuidePopupDismissReceiverInSystemUI(Context context) {
        log(" registerGuidePopupDismissReceiverInSystemUI");
        if (context == null) {
            log(" registerGuidePopupDismissReceiverInSystemUI context = null");
            return;
        }
        if (this.mView == null) {
            log(" registerGuidePopupDismissReceiverInSystemUI mView = null");
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USER_DISMISS_GUIDE_POPUP);
        try {
            context.registerReceiverAsUser(this.mDismissPopupBroadcastReceiver, UserHandle.ALL, filter, null, null, 2);
            sHasRegisteredReceiverInSystemUI = true;
            log(" after registerGuidePopupDismissReceiverInSystemUI");
        } catch (Exception e) {
            log("register receiver failed " + e.getMessage());
        }
    }

    private void unregisterGuidePopupDismissReceiverInSystemUI(Context context) {
        log("unregisterGuidePopupDismissReceiverInSystemUI");
        if (context == null) {
            log("unregisterGuidePopupDismissReceiverInSystemUI context = null");
            return;
        }
        if (this.mView == null) {
            log("unregisterGuidePopupDismissReceiverInSystemUI mView = null");
            return;
        }
        try {
            context.unregisterReceiver(this.mDismissPopupBroadcastReceiver);
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInWhiteList(Context context) {
        if (context.getDisplayId() != 0) {
            return false;
        }
        return sIsInWhiteList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean reflectSmoothScrollForScrollableView(View scrollTarget) {
        try {
            List<Method> candidateScrollMethods = new ArrayList<>();
            String[] keyNames = {"smoothScrollToPosition", "smoothScrollTo", "scrollToPosition"};
            for (Class cls = scrollTarget.getClass(); cls != null; cls = cls.getSuperclass()) {
                queryScrollMethods(candidateScrollMethods, keyNames, cls);
            }
            boolean reflectSucceed = callScrollMethods(candidateScrollMethods, scrollTarget);
            return reflectSucceed;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    private void queryScrollMethods(List<Method> candidates, String[] keyNames, Class clz) {
        Method[] methods = clz.getDeclaredMethods();
        int methodLength = methods.length;
        for (String keyName : keyNames) {
            for (int k = 0; k < methodLength; k++) {
                String superMethodName = methods[k].getName();
                if (superMethodName.equals(keyName)) {
                    candidates.add(methods[k]);
                }
            }
        }
    }

    private boolean callScrollMethods(List<Method> scrollMethods, View scrollTarget) {
        Method method;
        int paramsCount;
        Class[] paramsTypes;
        for (int i = 0; i < scrollMethods.size(); i++) {
            try {
                method = scrollMethods.get(i);
                method.setAccessible(true);
                method.getName();
                paramsCount = method.getParameterCount();
                paramsTypes = method.getParameterTypes();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            if (paramsCount == 1) {
                method.invoke(scrollTarget, 0);
                log("invoke 1 param method " + method.getName());
                return true;
            }
            if (paramsCount == 2 && paramsTypes[0] == Integer.TYPE && paramsTypes[1] == Integer.TYPE) {
                method.invoke(scrollTarget, 0, 0);
                log("invoke (int, int) method " + method.getName());
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getTopScrollableChild() {
        View view = this.mView;
        if (view == null || !(view instanceof ViewGroup)) {
            return;
        }
        this.mScrollableViews.clear();
        scanScrollTarget((ViewGroup) this.mView);
    }

    private void scanScrollTarget(ViewGroup parentView) {
        int childCount = parentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parentView.getChildAt(i);
            if (viewCanScrollUp(childView)) {
                log("find scrollTarget : " + childView + ", vertical scrollRange = " + childView.computeVerticalScrollRange());
                this.mScrollableViews.add(childView);
            }
        }
        for (int i2 = 0; i2 < childCount; i2++) {
            View childView2 = parentView.getChildAt(i2);
            if (childView2 != null && (childView2 instanceof ViewGroup) && childView2.getVisibility() == 0) {
                scanScrollTarget((ViewGroup) childView2);
            }
        }
    }

    private boolean viewCanScrollUp(View childView) {
        if (childView != null && (childView instanceof ViewGroup) && childView.getVisibility() == 0 && childView.canScrollVertically(-1)) {
            int[] locationOnScreen = new int[2];
            childView.getLocationOnScreen(locationOnScreen);
            if (locationOnScreen[0] < this.mWinFrame.right && locationOnScreen[0] + childView.getMeasuredWidth() > this.mWinFrame.left && childView.computeVerticalScrollRange() != childView.getMeasuredHeight()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSystemUIBroadcastReceiver(Context context) {
        log("registerSystemUIBroadcastReceiver ");
        if (context == null) {
            log("mContext == null");
            return;
        }
        if (this.mView == null) {
            log("mView == null");
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CLICK_ON_STATUS_BAR);
        try {
            context.registerReceiver(this.mSystemUIBroadcastReceiver, filter, 2);
        } catch (Exception e) {
            log(" register receiver failed " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterSystemUIBroadcastReceiver(Context context) {
        log("unregisterSystemUIBroadcastReceiver ");
        if (context == null) {
            log("mContext == null");
            return;
        }
        try {
            context.unregisterReceiver(this.mSystemUIBroadcastReceiver);
        } catch (Exception e) {
            log(" unregisterSystemUIBroadcastReceiver failed " + e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPopup(Context context) {
        PopupWindow popupWindow = this.mGuidePopup;
        if (popupWindow != null && popupWindow.isShowing()) {
            log("dismiss guide popup window");
            this.mGuidePopup.dismiss();
        }
    }

    private boolean clickOnStatusBar(Context context, MotionEvent e) {
        String packageName = context.getPackageName();
        int action = e.getActionMasked();
        if (isCurrentWindowStatusBar(packageName)) {
            if (action == 0) {
                this.mLastDownPoint.set(e.getX(), e.getY());
            } else if (action == 1) {
                boolean moved = Math.abs(e.getX() - this.mLastDownPoint.x) > 25.0f || Math.abs(e.getY() - this.mLastDownPoint.y) > 25.0f;
                if (!moved) {
                    log("click on status bar");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCurrentWindowStatusBar(String packageName) {
        return "com.android.systemui".equals(packageName) && this.mWindowAttributes.type == 2000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needShowPopup(View decorView) {
        return isInWhiteList(decorView.getContext()) && (decorView instanceof ViewGroup) && !sHasShownGuildPopup;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasDismissedPopupBefore(Context context) {
        return sHasShownGuildPopup;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showGuidePopup(View decorView) {
        if (this.mGuidePopup == null) {
            this.mGuidePopup = new PopupWindow(decorView.getContext(), (AttributeSet) null, 0, 0);
        }
        try {
            Context context = decorView.getContext();
            this.mGuidePopup.setContentView(createContentView(decorView));
            this.mGuidePopup.showAtLocation(decorView, 49, 0, 0);
            this.mGuidePopup.setBackgroundDrawable(null);
            log("show guide popup window");
            int userHandle = UserHandle.getUserId(Process.myUid());
            Intent intent = new Intent(ACTION_USER_DISMISS_GUIDE_POPUP);
            intent.putExtra(GUIDE_DISMISS_INTENT_EXTRA_KEY, userHandle);
            context.sendBroadcast(intent);
            sHasShownGuildPopup = true;
        } catch (Exception e) {
            log("unable to show guidePopup " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshGuidePopup(final Context context) {
        this.mMainHandler.post(new Runnable() { // from class: android.view.OplusScrollToTopManager.4
            @Override // java.lang.Runnable
            public void run() {
                if (OplusScrollToTopManager.this.mGuidePopup != null && OplusScrollToTopManager.this.mGuidePopup.isShowing() && OplusScrollToTopManager.this.hasDismissedPopupBefore(context)) {
                    OplusScrollToTopManager.this.mGuidePopup.dismiss();
                }
            }
        });
    }

    private View createContentView(View decorView) {
        View contentView = LayoutInflater.from(decorView.getContext()).inflate(201917497, (ViewGroup) null);
        ImageView deleteIcon = (ImageView) contentView.findViewById(201457826);
        deleteIcon.setOnClickListener(new View.OnClickListener() { // from class: android.view.OplusScrollToTopManager.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OplusScrollToTopManager.this.dismissPopup(v.getContext());
            }
        });
        return contentView;
    }

    private void handleWindowDying() {
        Context context;
        log("window dying");
        this.mIsDying = true;
        View view = this.mView;
        if (view == null || (context = view.getContext()) == null) {
            return;
        }
        checkOrInitBgHandler();
        DelayRegisterReceiverRunnable delayRegisterReceiverRunnable = this.mFocusChangeRunnable;
        if (delayRegisterReceiverRunnable != null) {
            this.mH.removeCallbacks(delayRegisterReceiverRunnable);
        }
        String packageName = context.getPackageName();
        if ("com.android.systemui".equals(packageName)) {
            unregisterGuidePopupDismissReceiverInSystemUI(context);
        } else {
            unregisterSystemUIBroadcastReceiver(context);
        }
    }

    @Override // android.view.IOplusScrollToTopManager
    public void setWindowRootView(View view, WindowManager.LayoutParams params) {
        this.mView = view;
        this.mWindowAttributes = params;
    }

    @Override // android.view.IOplusScrollToTopManager
    public void setWindowFrame(Rect winFrame) {
        this.mWinFrame = winFrame;
    }

    @Override // android.view.IOplusScrollToTopManager
    public void handleWindowFocusChanged(Context context, boolean hasFocus) {
        DelayRegisterReceiverRunnable delayRegisterReceiverRunnable = this.mFocusChangeRunnable;
        if (delayRegisterReceiverRunnable == null) {
            return;
        }
        delayRegisterReceiverRunnable.setParams(context, hasFocus);
        checkOrInitBgHandler();
        this.mH.removeCallbacks(this.mFocusChangeRunnable);
        this.mH.postDelayed(this.mFocusChangeRunnable, 1000L);
    }

    @Override // android.view.IOplusScrollToTopManager
    public void postShowGuidePopupRunnable(View decorView) {
        if (this.mIsDying) {
            return;
        }
        this.mMainHandler.removeCallbacks(this.mShowPopupTask);
        this.mMainHandler.postDelayed(this.mShowPopupTask, 200L);
    }

    @Override // android.view.IOplusScrollToTopManager
    public void processPointerEvent(MotionEvent e, Context context) {
        if (!this.mIsDying && clickOnStatusBar(context, e)) {
            Intent clickIntent = new Intent(ACTION_CLICK_ON_STATUS_BAR);
            context.sendBroadcastAsUser(clickIntent, UserHandle.ALL);
        }
    }

    @Override // android.view.IOplusScrollToTopManager
    public void onWindowDying() {
        if (this.mIsDying) {
            return;
        }
        handleWindowDying();
    }

    @Override // android.view.IOplusScrollToTopManager
    public void setIsInWhiteList(boolean isInWhiteList) {
        log("setIsInWhiteList " + isInWhiteList);
        sIsInWhiteList = isInWhiteList;
    }

    @Override // android.view.IOplusScrollToTopManager
    public boolean getIsInWhiteList() {
        return sIsInWhiteList;
    }

    @Override // android.view.IOplusScrollToTopManager
    public void setNeedShowGuidePopup(boolean needShowGuidePopup) {
        sHasShownGuildPopup = !needShowGuidePopup;
    }

    @Override // android.view.IOplusScrollToTopManager
    public boolean getNeedShowGuildPopup() {
        return !sHasShownGuildPopup;
    }

    public static OplusScrollToTopManager getInstance() {
        return Holder.INSTANCE;
    }

    @Override // android.view.IOplusScrollToTopManager
    public IOplusScrollToTopManager newInstance() {
        return new OplusScrollToTopManager();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String content) {
        StringBuilder builder = new StringBuilder();
        if (this.mWindowAttributes != null) {
            builder.append(((Object) this.mWindowAttributes.getTitle()) + ",");
        }
        builder.append(content);
        Log.d(TAG, builder.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DelayRegisterReceiverRunnable implements Runnable {
        private WeakReference<Context> mContextRef;
        private boolean mHasFocus;

        private DelayRegisterReceiverRunnable() {
        }

        public void setParams(Context context, boolean hasFocus) {
            this.mContextRef = new WeakReference<>(context);
            this.mHasFocus = hasFocus;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (OplusScrollToTopManager.this.mIsDying) {
                    return;
                }
                if (this.mContextRef.get() != null) {
                    Context context = this.mContextRef.get();
                    OplusScrollToTopManager.this.log("This " + OplusScrollToTopManager.this.mView + " change focus to " + this.mHasFocus);
                    if ("com.android.systemui".equals(context.getPackageName())) {
                        boolean isNotificationShade = false;
                        if (OplusScrollToTopManager.this.mWindowAttributes != null) {
                            isNotificationShade = OplusScrollToTopManager.this.mWindowAttributes.getTitle().equals("NotificationShade");
                        }
                        if (isNotificationShade && this.mHasFocus && !OplusScrollToTopManager.sHasRegisteredReceiverInSystemUI) {
                            OplusScrollToTopManager.this.registerGuidePopupDismissReceiverInSystemUI(context);
                        }
                    } else {
                        if (!OplusScrollToTopManager.this.isInWhiteList(context)) {
                            return;
                        }
                        if (this.mHasFocus) {
                            OplusScrollToTopManager.this.registerSystemUIBroadcastReceiver(context);
                        } else {
                            OplusScrollToTopManager.this.unregisterSystemUIBroadcastReceiver(context);
                            OplusScrollToTopManager.this.refreshGuidePopup(context);
                        }
                    }
                    return;
                }
                OplusScrollToTopManager.this.log("lost mContextRef , failed to execute DelayRegisterReceiverRunnable");
            } catch (Exception e) {
                Log.d(OplusScrollToTopManager.TAG, "Failed to execute DelayRegisterReceiverRunnable " + e.getMessage());
            }
        }
    }
}
