package android.view;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.debug.InputLog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusSystemUINavigationGesture implements IOplusSystemUINavigationGesture {
    private static final String DISPLAY_CATEGORY_ALL_INCLUDING_DISABLED = "android.hardware.display.category.ALL_INCLUDING_DISABLED";
    private static final boolean FEATURE_FOLD;
    private static final int GESTURE_MOTION_SLOW_MOVE_TIME = 400;
    private static final int LEFT_SIDE = 0;
    private static final int MSG_GESTURE_CONFIG_CHECK = 1002;
    private static final int MSG_GESTURE_MOTION_DOWN = 1001;
    private static final int NON_SIDE = -1;
    private static final String OEM_SYSTEMUI_PACKAGE_NAME = "com.android.systemui";
    private static final float PIXEL_OFFSET = 0.5f;
    public static final float PORTRAIT_NON_DETECT_SCALE = 0.0f;
    private static final boolean REMAP_DISPLAY_DISABLED;
    private static final int RIGHT_SIDE = 1;
    private static final String SYSTEM_FOLDING_MODE_KEY = "oplus_system_folding_mode";
    private static final String TAG = "OplusSystemUINavigationGesture";
    private Context mContext;
    private ContentObserver mFoldOpenObserver;
    private int mLongPressTimeout;
    private int mSideGestureAreaWidth;
    private int mSideGestureKeyAnimThreshold;
    private ViewRootImpl mViewRootImpl;
    private static final int MAX_LONG_PRESS_TIMEOUT = SystemProperties.getInt("gestures.back_timeout", 250);
    private static final float SIDE_GESTURE_EDGE_MOVE_SCALE = SystemProperties.getInt("persist.gesture_button.side.move.scale", 45) * 0.001f;
    private static final float SIDE_GESTURE_EDGE_HORIZONTAL_SCALE = SystemProperties.getInt("persist.gesture_button.side.hor.scale", 250) * 0.01f;
    private boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private ArrayList<MotionEvent> mBackupEventList = new ArrayList<>();
    private boolean mGestureButtonActive = false;
    private boolean mIsKeyguard = false;
    Region mSystemGestureExclusionRegion = Region.obtain();
    boolean mSwipeTimeoSlow = false;
    boolean mReachDistance = false;
    int mSwipeSide = -1;
    private float mRawX = 0.0f;
    private float mRawY = 0.0f;
    private int mScreenHeight = -1;
    private int mScreenWidth = -1;
    private boolean mHasInitData = false;
    private boolean mIsFoldOpen = false;
    private Display mSecondaryDisplay = null;
    private IOplusSystemUINavigationGestureExt mOplusSystemUINavigationGestureExt = new OplusSystemUINavigationGestureExtImpl();
    private final SystemUINavigationGestureHandler mHandler = new SystemUINavigationGestureHandler();
    private Region mSideBarSceneRegion = new Region();
    private boolean mSmartSideBarEnabled = false;
    private boolean mSideBarQueueMotionConsumed = true;
    private boolean mCheckForGestureSideBar = false;
    private boolean mGestureSideBarActive = false;

    static {
        FEATURE_FOLD = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_FOLD) || OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_REVISE_SQUARE_DISPLAY_ORIENTATION);
        REMAP_DISPLAY_DISABLED = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_FOLD_REMAP_DISPLAY_DISABLED);
    }

    public OplusSystemUINavigationGesture(ViewRootImpl viewRoot, Context context) {
        this.mViewRootImpl = viewRoot;
        this.mContext = context;
    }

    private void initData() {
        if (!this.mHasInitData) {
            if (this.DEBUG) {
                Log.d(TAG, "[GESTURE_BUTTON] init data");
            }
            this.mHasInitData = true;
            this.mIsFoldOpen = Settings.Global.getInt(this.mContext.getContentResolver(), "oplus_system_folding_mode", -1) == 1;
            this.mSecondaryDisplay = getSecondaryDisplay();
            handleGestureConfigCheck();
            registerNavGestureListener();
        }
    }

    public void registerNavGestureListener() {
        boolean hasFeature = OplusFeatureConfigManager.getInstacne().hasFeature(IOplusFeatureConfigList.FEATURE_SMART_SIDEBAR);
        this.mSmartSideBarEnabled = hasFeature;
        if (hasFeature) {
            OplusViewBackgroundThread.getHandler().post(new Runnable() { // from class: android.view.OplusSystemUINavigationGesture.1
                @Override // java.lang.Runnable
                public void run() {
                    OplusSystemUINavigationGesture.this.mOplusSystemUINavigationGestureExt.registerSmartSideBarRegion(OplusSystemUINavigationGesture.this.mContext);
                }
            });
            this.mFoldOpenObserver = new ContentObserver(this.mContext.getMainThreadHandler()) { // from class: android.view.OplusSystemUINavigationGesture.2
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange) {
                    OplusSystemUINavigationGesture oplusSystemUINavigationGesture = OplusSystemUINavigationGesture.this;
                    oplusSystemUINavigationGesture.mIsFoldOpen = Settings.Global.getInt(oplusSystemUINavigationGesture.mContext.getContentResolver(), "oplus_system_folding_mode", -1) == 1;
                    OplusSystemUINavigationGesture.this.handleGestureConfigCheck();
                }
            };
            this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("oplus_system_folding_mode"), false, this.mFoldOpenObserver);
        }
    }

    @Override // android.view.IOplusSystemUINavigationGesture
    public void unRegisterNavGestureListener() {
        if (this.mFoldOpenObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mFoldOpenObserver);
        }
        if (this.mSmartSideBarEnabled) {
            OplusViewBackgroundThread.getHandler().post(new Runnable() { // from class: android.view.OplusSystemUINavigationGesture.3
                @Override // java.lang.Runnable
                public void run() {
                    OplusSystemUINavigationGesture.this.mOplusSystemUINavigationGestureExt.unregisterSmartSideBarRegion();
                }
            });
        }
    }

    private Display getSecondaryDisplay() {
        Display display = this.mSecondaryDisplay;
        if (display != null) {
            return display;
        }
        Context context = this.mContext;
        if (context != null && FEATURE_FOLD && REMAP_DISPLAY_DISABLED) {
            DisplayManager dm = (DisplayManager) context.getSystemService("display");
            Display defaultDisplay = dm.getDisplay(0);
            for (Display d : dm.getDisplays(DISPLAY_CATEGORY_ALL_INCLUDING_DISABLED)) {
                if (d.getDisplayId() != 0 && d.getType() == defaultDisplay.getType()) {
                    Log.d(TAG, "[GESTURE_SIDE_BAR] has secondaryDisplay");
                    this.mSecondaryDisplay = d;
                    return d;
                }
            }
        }
        return null;
    }

    private boolean isSecondaryDisplay() {
        return this.mHasInitData && this.mSecondaryDisplay != null && this.mViewRootImpl.mAttachInfo.mDisplay.getDisplayId() == this.mSecondaryDisplay.getDisplayId();
    }

    @Override // android.view.IOplusSystemUINavigationGesture
    public void checkKeyguardAndConfig(String tag) {
        if ("com.android.systemui".equals(this.mViewRootImpl.mBasePackageName)) {
            if (tag != null) {
                if (tag.contains("StatusBar") || tag.contains("NotificationShade")) {
                    this.mIsKeyguard = true;
                } else {
                    this.mIsKeyguard = false;
                }
            }
            if (this.DEBUG) {
                Log.d(TAG, "[GESTURE_BUTTON] tag = " + tag);
            }
        }
    }

    @Override // android.view.IOplusSystemUINavigationGesture
    public void handleGestureMotionDown(View view) {
        for (int i = 0; i < this.mBackupEventList.size(); i++) {
            try {
                boolean ishandled = view.dispatchPointerEvent(this.mBackupEventList.get(i));
                if (this.DEBUG) {
                    Log.d(TAG, "[GESTURE_BUTTON] MSG_GESTURE_MOTION_DOWN 1 dispatch mBackupEventList: " + this.mBackupEventList + ", handled=" + ishandled);
                }
            } catch (Exception e) {
                Log.e(TAG, "mView does not exist, so discard the remaining points. " + e);
            }
        }
        this.mGestureButtonActive = false;
    }

    @Override // android.view.IOplusSystemUINavigationGesture
    public void handleGestureConfigCheck() {
        if (this.mHasInitData) {
            int screenWidth = this.mViewRootImpl.mAttachInfo.mDisplay.getMode().getPhysicalWidth();
            int screenHeight = this.mViewRootImpl.mAttachInfo.mDisplay.getMode().getPhysicalHeight();
            this.mScreenHeight = Math.max(screenWidth, screenHeight);
            this.mScreenWidth = Math.min(screenWidth, screenHeight);
            if (!REMAP_DISPLAY_DISABLED && this.mIsFoldOpen) {
                this.mScreenHeight = Math.min(screenWidth, screenHeight);
                this.mScreenWidth = Math.max(screenWidth, screenHeight);
            }
            this.mSideGestureKeyAnimThreshold = (int) (this.mScreenWidth * SIDE_GESTURE_EDGE_MOVE_SCALE);
            this.mLongPressTimeout = Math.min(MAX_LONG_PRESS_TIMEOUT, ViewConfiguration.getLongPressTimeout());
            this.mSideGestureAreaWidth = this.mContext.getResources().getDimensionPixelSize(201654474);
            if (this.DEBUG) {
                Log.d(TAG, "[GESTURE_BUTTON] mIsFoldOpen = " + this.mIsFoldOpen);
                Log.d(TAG, "[GESTURE_BUTTON] mScreenHeight = " + this.mScreenHeight);
                Log.d(TAG, "[GESTURE_BUTTON] mScreenWidth = " + this.mScreenWidth);
                Log.d(TAG, "[GESTURE_BUTTON] density = " + this.mContext.getResources().getDisplayMetrics().density);
                if (this.mViewRootImpl.mView != null) {
                    Log.d(TAG, "[GESTURE_BUTTON] Config View: " + (InputLog.isLevelDebug() ? this.mViewRootImpl.mView : this.mViewRootImpl.mView.getClass().getName()));
                } else {
                    Log.d(TAG, "[GESTURE_BUTTON] Config View: null");
                }
            }
        }
    }

    @Override // android.view.IOplusSystemUINavigationGesture
    public void setSystemGestureExclusionRegion(List<Rect> rects) {
        this.mSystemGestureExclusionRegion = rectsToRegion(rects);
        if (this.DEBUG) {
            Log.v(TAG, "systemGestureExclusionChanged region=" + this.mSystemGestureExclusionRegion + " list:");
            for (Rect rect : rects) {
                Log.v(TAG, "systemGestureExclusionChanged rect=" + rect);
            }
        }
    }

    public static Region rectsToRegion(List<Rect> rects) {
        Region result = Region.obtain();
        if (rects != null) {
            for (Rect r : rects) {
                if (r != null && !r.isEmpty()) {
                    result.op(r, Region.Op.UNION);
                }
            }
        }
        return result;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private boolean processSideBarSceneEvent(MotionEvent event) {
        if (event.getPointerCount() == 1 || event.getActionMasked() == 5) {
            int action = event.getActionMasked();
            if (action == 0 || this.mCheckForGestureSideBar || this.mGestureSideBarActive) {
                if (action == 0) {
                    this.mRawX = event.getRawX();
                    this.mRawY = event.getRawY();
                    this.mSideBarSceneRegion = this.mOplusSystemUINavigationGestureExt.getSmartSideBarRegion();
                }
                if (this.DEBUG) {
                    if (this.mViewRootImpl.mView == null) {
                        Log.d(TAG, "[GESTURE_SIDE_BAR] View: null");
                    } else {
                        Log.d(TAG, "[GESTURE_SIDE_BAR] View: " + (InputLog.isLevelDebug() ? this.mViewRootImpl.mView : this.mViewRootImpl.mView.getClass().getName()));
                    }
                    Log.d(TAG, "[GESTURE_SIDE_BAR] Motion: " + action);
                    Log.d(TAG, "[GESTURE_SIDE_BAR] raw: [" + event.getRawX() + "," + event.getRawY() + "]");
                    Log.d(TAG, "[GESTURE_SIDE_BAR] mCheckForGestureSideBar: " + this.mCheckForGestureSideBar);
                }
            }
            switch (action) {
                case 0:
                    this.mCheckForGestureSideBar = false;
                    this.mGestureSideBarActive = false;
                    this.mSideBarQueueMotionConsumed = true;
                    this.mBackupEventList.clear();
                    this.mSwipeSide = -1;
                    this.mReachDistance = false;
                    this.mSwipeTimeoSlow = false;
                    boolean hit = false;
                    if (this.mSideBarSceneRegion.contains((int) this.mRawX, (int) this.mRawY)) {
                        hit = true;
                        this.mSwipeSide = this.mRawX < ((float) this.mSideGestureAreaWidth) ? 0 : 1;
                        if (1 != 0) {
                            Log.d(TAG, "[GESTURE_SIDE_BAR] swipe from " + this.mSwipeSide);
                            if (this.mIsKeyguard) {
                                hit = false;
                            }
                        }
                    }
                    if (hit) {
                        this.mCheckForGestureSideBar = true;
                        Log.d(TAG, "[GESTURE_SIDE_BAR] Hit Side Bar Region !");
                        this.mBackupEventList.add(MotionEvent.obtain(event));
                        Message msg = Message.obtain(this.mHandler, 1001, this.mViewRootImpl.mView);
                        this.mSideBarQueueMotionConsumed = false;
                        this.mHandler.sendMessageDelayed(msg, this.mLongPressTimeout);
                        if (this.DEBUG) {
                            Log.d(TAG, "[GESTURE_SIDE_BAR] sendMessages for MSG_GESTURE_MOTION_DOWN ");
                        }
                        return true;
                    }
                    break;
                case 1:
                case 3:
                case 5:
                    if (this.mGestureSideBarActive) {
                        this.mCheckForGestureSideBar = false;
                        this.mGestureSideBarActive = false;
                        this.mSideBarQueueMotionConsumed = true;
                        return true;
                    }
                    if (this.mCheckForGestureSideBar) {
                        if (this.mHandler.hasMessages(1001)) {
                            this.mHandler.removeMessages(1001);
                        }
                        if (!this.mSideBarQueueMotionConsumed && action != 3) {
                            for (int i = 0; i < this.mBackupEventList.size(); i++) {
                                try {
                                    boolean ishandled = this.mViewRootImpl.mView.dispatchPointerEvent(this.mBackupEventList.get(i));
                                    if (this.DEBUG) {
                                        Log.d(TAG, "[GESTURE_SIDE_BAR] 3 dispatch mBackupEventList: " + this.mBackupEventList + ", handled=" + ishandled);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "mView does not exist, so discard the remaining points. " + e);
                                }
                            }
                        }
                        this.mSideBarQueueMotionConsumed = true;
                        this.mCheckForGestureSideBar = false;
                        this.mGestureSideBarActive = false;
                        if (action == 3) {
                            return true;
                        }
                    }
                    break;
                case 2:
                    if (this.mCheckForGestureSideBar) {
                        this.mBackupEventList.add(MotionEvent.obtain(event));
                        if (event.getEventTime() - event.getDownTime() > 400) {
                            this.mSwipeTimeoSlow = true;
                        }
                        int threshold = this.mSideGestureKeyAnimThreshold;
                        int horizontalThreshold = (int) (this.mSideGestureKeyAnimThreshold * SIDE_GESTURE_EDGE_HORIZONTAL_SCALE);
                        if (Math.abs(event.getRawX() - this.mRawX) >= threshold) {
                            this.mReachDistance = true;
                        } else if (Math.abs(event.getRawY() - this.mRawY) > horizontalThreshold) {
                            this.mSwipeTimeoSlow = true;
                        }
                        if ((this.mReachDistance || this.mSwipeTimeoSlow) && this.DEBUG) {
                            Log.d(TAG, "[GESTURE_SIDE_BAR] reach=" + this.mReachDistance + " slow=" + this.mSwipeTimeoSlow + " side=" + this.mSwipeSide + " threshold=" + threshold + " horThreshold=" + horizontalThreshold + " time=" + (event.getEventTime() - event.getDownTime()) + " xMove=" + Math.abs(event.getRawX() - this.mRawX) + " yMove=" + Math.abs(event.getRawY() - this.mRawY));
                        }
                        boolean z = this.mSwipeTimeoSlow;
                        boolean z2 = this.mReachDistance;
                        boolean trigger = (!z) & z2 & this.mCheckForGestureSideBar;
                        if (trigger) {
                            this.mGestureSideBarActive = true;
                            if (this.mHandler.hasMessages(1001)) {
                                this.mHandler.removeMessages(1001);
                            }
                            Log.d(TAG, "[GESTURE_SIDE_BAR] trigger!");
                            this.mCheckForGestureSideBar = false;
                            this.mSideBarQueueMotionConsumed = true;
                        } else if (z2 || z) {
                            if (this.mHandler.hasMessages(1001)) {
                                this.mHandler.removeMessages(1001);
                            }
                            if (!this.mSideBarQueueMotionConsumed) {
                                for (int i2 = 0; i2 < this.mBackupEventList.size(); i2++) {
                                    try {
                                        boolean ishandled2 = this.mViewRootImpl.mView.dispatchPointerEvent(this.mBackupEventList.get(i2));
                                        if (this.DEBUG) {
                                            Log.d(TAG, "[GESTURE_SIDE_BAR] 2 dispatch mBackupEventList: " + this.mBackupEventList + ", handled=" + ishandled2);
                                        }
                                    } catch (Exception e2) {
                                        Log.e(TAG, "mView does not exist, so discard the remaining points. " + e2);
                                    }
                                }
                            }
                            this.mCheckForGestureSideBar = false;
                            this.mGestureSideBarActive = false;
                            this.mSideBarQueueMotionConsumed = true;
                        } else if (this.mHandler.hasMessages(1001)) {
                            this.mHandler.removeMessages(1001);
                            Message msg2 = Message.obtain(this.mHandler, 1001, this.mViewRootImpl.mView);
                            this.mSideBarQueueMotionConsumed = false;
                            this.mHandler.sendMessageDelayed(msg2, this.mLongPressTimeout);
                        }
                        if (this.DEBUG) {
                            Log.d(TAG, "[GESTURE_SIDE_BAR] reachDistance=" + this.mReachDistance + ", swipeTimeoSlow=" + this.mSwipeTimeoSlow);
                        }
                        return true;
                    }
                    if (this.mGestureSideBarActive) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    @Override // android.view.IOplusSystemUINavigationGesture
    public boolean processGestureEvent(MotionEvent event) {
        initData();
        if (this.mSmartSideBarEnabled && !isSecondaryDisplay() && processSideBarSceneEvent(event)) {
            if (this.DEBUG) {
                Log.d(TAG, "[GESTURE_SIDE_BAR] SideBarGesture handled");
                return true;
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class SystemUINavigationGestureHandler extends Handler {
        SystemUINavigationGestureHandler() {
        }

        @Override // android.os.Handler
        public String getMessageName(Message message) {
            switch (message.what) {
                case 1001:
                    return "MSG_GESTURE_MOTION_DOWN";
                case 1002:
                    return "MSG_GESTURE_CONFIG_CHECK";
                default:
                    return super.getMessageName(message);
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    OplusSystemUINavigationGesture.this.handleGestureMotionDown((View) msg.obj);
                    return;
                case 1002:
                    OplusSystemUINavigationGesture.this.handleGestureConfigCheck();
                    return;
                default:
                    return;
            }
        }
    }
}
