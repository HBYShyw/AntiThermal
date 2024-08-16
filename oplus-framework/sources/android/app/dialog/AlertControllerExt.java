package android.app.dialog;

import android.R;
import android.content.ComponentCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.OplusThemeResources;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OplusViewRootUtil;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.internal.app.AlertController;
import com.android.internal.app.IAlertControllerWrapper;
import com.oplus.exfunction.ExFunctionManager;
import com.oplus.util.OplusContextUtil;
import com.oplus.widget.OplusAlertLinearLayout;
import com.oplus.widget.OplusRecyclerListView;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class AlertControllerExt implements IAlertControllerExt {
    private static final int DEFAULT_DISPALY_ID = 0;
    private static final int FULL_SCREEN_FLAG = -2147482112;
    private static final String TAG = "AlertControllerExt";
    private static final int TYPE_CENTER = 0;
    private AlertController mAlertController;
    private Button mButtonNegative;
    private CharSequence mButtonNegativeText;
    private Button mButtonNeutral;
    private CharSequence mButtonNeutralText;
    private TextPaint mButtonPaint;
    private ViewStub mButtonPanelStub;
    private Button mButtonPositive;
    private CharSequence mButtonPositiveText;
    private Context mContext;
    private boolean mIsValidateNavigationBar;
    private CharSequence mMessage;
    private TextView mMessageView;
    private ScrollView mScrollView;
    private CharSequence mTitle;
    private Window mWindow = null;
    boolean mMessageNeedScroll = false;
    private int mDialogType = 0;
    private Handler mHandler;
    private ContentObserver mObserver = new ContentObserver(this.mHandler) { // from class: android.app.dialog.AlertControllerExt.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            AlertControllerExt.this.mHandler.sendEmptyMessage(1);
        }
    };
    private ComponentCallbacks mComponentCallbacks = new ComponentCallbacks() { // from class: android.app.dialog.AlertControllerExt.2
        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
            AlertControllerExt.this.mHandler.sendEmptyMessage(2);
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void onConfigurationChanged() {
        if (OplusContextUtil.isOplusAlertDialogBuilderStyle(this.mContext)) {
            return;
        }
        updateWindowAttributes();
        updateSpaceHeight();
        updateBg();
    }

    public AlertControllerExt(Object base) {
        this.mAlertController = null;
        this.mAlertController = (AlertController) base;
    }

    public void init(Context context, DialogInterface di, Window window) {
        this.mContext = context;
        this.mWindow = window;
        this.mHandler = new BottomSpaceHandler(this);
        initButtonPaint();
    }

    private void initButtonPaint() {
        TextPaint textPaint = new TextPaint();
        this.mButtonPaint = textPaint;
        textPaint.setTextSize(this.mContext.getResources().getDimensionPixelSize(201654306));
    }

    public boolean isCenterDialog() {
        return getDialogType() == 0;
    }

    public void setupView() {
        if (OplusContextUtil.isOplusAlertDialogBuilderStyle(this.mContext)) {
            return;
        }
        updateWindowAttributes();
        setupAnimationAndGravity();
        addBottomSpace();
        updateBg();
        Object listView = getListView();
        if (listView instanceof OplusRecyclerListView) {
            OplusRecyclerListView colorRecyclerListView = (OplusRecyclerListView) listView;
            colorRecyclerListView.setNeedClip(needClipListView());
        }
        this.mButtonPanelStub = (ViewStub) this.mWindow.findViewById(201457728);
        if (isCenterDialog()) {
            if (needSetButtonsVertical()) {
                setButtonsVertical();
                return;
            } else {
                setButtonsHorizontal();
                return;
            }
        }
        setButtonsVertical();
    }

    public void setupContent(final ViewGroup contentPanel) {
        if (OplusContextUtil.isOplusAlertDialogBuilderStyle(this.mContext)) {
            return;
        }
        loadRefObject();
        ViewGroup listPanel = (ViewGroup) contentPanel.findViewById(201457734);
        if (this.mMessage != null && listPanel != null && getListView() != null) {
            listPanel.addView(getListView(), new ViewGroup.LayoutParams(-1, -1));
        }
        if (isCenterDialog()) {
            if (this.mMessage != null) {
                relayoutMessageView(contentPanel);
            }
        } else {
            relayoutListAndMessage(listPanel);
        }
        contentPanel.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: android.app.dialog.AlertControllerExt.3
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                contentPanel.removeOnAttachStateChangeListener(this);
                if (AlertControllerExt.this.mComponentCallbacks != null) {
                    AlertControllerExt.this.mContext.unregisterComponentCallbacks(AlertControllerExt.this.mComponentCallbacks);
                    AlertControllerExt.this.mComponentCallbacks = null;
                }
                AlertControllerExt.this.mContext.getContentResolver().unregisterContentObserver(AlertControllerExt.this.mObserver);
            }
        });
    }

    public void setupButtons(ViewGroup buttonPanel) {
        if (OplusContextUtil.isOplusAlertDialogBuilderStyle(this.mContext)) {
            return;
        }
        loadRefObject();
        resetButtonsPadding();
        setButtonsBackground();
    }

    public boolean isOplusStyle(Context context) {
        return OplusContextUtil.isOplusStyle(context);
    }

    private void relayoutListAndMessage(ViewGroup listPanel) {
        if (isMessageNeedScroll()) {
            ScrollView scrollView = this.mScrollView;
            if (scrollView != null) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) scrollView.getLayoutParams();
                lp.height = 0;
                lp.weight = 1.0f;
                this.mScrollView.setLayoutParams(lp);
            }
            if (listPanel != null) {
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) listPanel.getLayoutParams();
                lp2.height = 0;
                lp2.weight = 1.0f;
                listPanel.setLayoutParams(lp2);
            }
        }
    }

    private void relayoutMessageView(ViewGroup contentPanel) {
        if (this.mMessageView == null) {
            this.mMessageView = (TextView) contentPanel.findViewById(R.id.message);
        }
        this.mMessageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: android.app.dialog.AlertControllerExt.4
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                int lineCount = AlertControllerExt.this.mMessageView.getLineCount();
                if (lineCount > 1) {
                    AlertControllerExt.this.mMessageView.setTextAlignment(2);
                } else {
                    AlertControllerExt.this.mMessageView.setTextAlignment(4);
                }
                AlertControllerExt.this.mMessageView.setText(AlertControllerExt.this.mMessageView.getText());
                AlertControllerExt.this.mMessageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void updateWindowAttributes() {
        Point realSize = getScreenSize();
        boolean port = realSize.x < realSize.y;
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        WindowManager.LayoutParams params = this.mWindow.getAttributes();
        if (port) {
            this.mWindow.setGravity(81);
            this.mWindow.clearFlags(FULL_SCREEN_FLAG);
            params.width = Math.min(realSize.x, displayMetrics.widthPixels);
            params.height = -2;
            return;
        }
        this.mWindow.setGravity(17);
        this.mWindow.addFlags(FULL_SCREEN_FLAG);
        params.width = Math.min(realSize.y, displayMetrics.widthPixels);
        params.height = this.mContext.getResources().getDimensionPixelSize(201654314);
    }

    private void setupAnimationAndGravity() {
        WindowManager.LayoutParams params = this.mWindow.getAttributes();
        if (isCenterDialog()) {
            params.windowAnimations = 201523224;
            params.gravity = 17;
        } else {
            params.windowAnimations = 201523222;
        }
        this.mWindow.setAttributes(params);
    }

    private void addBottomSpace() {
        if (!isCenterDialog()) {
            observeHideNavigationBar();
            this.mContext.registerComponentCallbacks(this.mComponentCallbacks);
        }
        if (needAddBottomView()) {
            updateSpaceHeight();
            updateWindowFlag();
            WindowManager.LayoutParams params = this.mWindow.getAttributes();
            addPrivateFlag(params);
            if (isSystemDialog(params)) {
                params.y -= spaceHeight();
            }
            this.mWindow.setAttributes(params);
        }
    }

    private void observeHideNavigationBar() {
        ContentResolver resolver = this.mContext.getContentResolver();
        resolver.registerContentObserver(Settings.Secure.getUriFor("manual_hide_navigationbar"), false, this.mObserver);
    }

    private int spaceHeight() {
        int result;
        if (!isFullScreen()) {
            return 0;
        }
        if (isGravityCenter()) {
            result = 0;
        } else if (isNavigationBarShow()) {
            result = navigationBarHeight();
        } else {
            result = this.mContext.getResources().getDimensionPixelSize(201654307);
        }
        if (!this.mIsValidateNavigationBar) {
            return 0;
        }
        return result;
    }

    private int navigationBarHeight() {
        Resources resources = this.mContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", OplusThemeResources.FRAMEWORK_PACKAGE);
        return resources.getDimensionPixelSize(resourceId);
    }

    private boolean isNavigationBarShow() {
        if (!supportNavigationBar()) {
            return false;
        }
        int navigationBarStatus = Settings.Secure.getInt(this.mContext.getContentResolver(), OplusViewRootUtil.KEY_NAVIGATIONBAR_MODE, 0);
        int navigationBarHideStatus = Settings.Secure.getInt(this.mContext.getContentResolver(), "manual_hide_navigationbar", 0);
        this.mIsValidateNavigationBar = (navigationBarStatus == -1 || navigationBarHideStatus == -1) ? false : true;
        return navigationBarStatus == 0 || (navigationBarStatus == 1 && navigationBarHideStatus == 0);
    }

    private boolean supportNavigationBar() {
        try {
            return WindowManagerGlobal.getWindowManagerService().hasNavigationBar(0);
        } catch (RemoteException e) {
            Log.d(TAG, "fail to get navigationBar's status, return false");
            return false;
        }
    }

    private boolean isGravityCenter() {
        return this.mWindow.getAttributes().gravity == 17;
    }

    private boolean needAddBottomView() {
        return !isCenterDialog() && isFullScreen();
    }

    private boolean isFullScreen() {
        try {
            int dockedSide = WindowManagerGlobal.getWindowManagerService().getDockedStackSide();
            return dockedSide == -1;
        } catch (Exception e) {
            Log.d(TAG, "isFullScreen operation failed.Return false.Failed msg is " + e.getMessage());
            return false;
        }
    }

    private boolean isPortrait() {
        int realScreenWidth = getScreenSize().x;
        int realScreenHeight = getScreenSize().y;
        return realScreenWidth < realScreenHeight;
    }

    private Point getScreenSize() {
        Point point = new Point();
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        windowManager.getDefaultDisplay().getRealSize(point);
        return point;
    }

    private void updateWindowFlag() {
        if (isGravityCenter()) {
            this.mWindow.clearFlags(FULL_SCREEN_FLAG);
        } else if (isNavigationBarShow()) {
            this.mWindow.setNavigationBarColor(-1);
            this.mWindow.clearFlags(ExFunctionManager.USER_FLAG_REPAIR_MODE);
            this.mWindow.getDecorView().setSystemUiVisibility(16);
            this.mWindow.addFlags(FULL_SCREEN_FLAG);
        }
    }

    private void addPrivateFlag(WindowManager.LayoutParams params) {
        params.privateFlags |= 16777216;
        params.privateFlags |= 64;
    }

    private void updateBg() {
        View parentPanel = this.mWindow.findViewById(R.id.resolver_empty_state_title);
        if (parentPanel != null && (parentPanel instanceof OplusAlertLinearLayout)) {
            OplusAlertLinearLayout oplusAlertLinearLayout = (OplusAlertLinearLayout) parentPanel;
            int gravity = this.mWindow.getAttributes().gravity;
            if (gravity == 17) {
                oplusAlertLinearLayout.setNeedClip(true);
                oplusAlertLinearLayout.setHasShadow(true);
            } else {
                oplusAlertLinearLayout.setNeedClip(false);
                oplusAlertLinearLayout.setHasShadow(false);
            }
        }
    }

    private boolean needClipListView() {
        return (hasMessage() || hasTitle() || isCenterDialog()) ? false : true;
    }

    private void resetButtonsPadding() {
        Button button = this.mButtonNeutral;
        if (button != null && this.mButtonPositive != null) {
            int paddingLeft = button.getPaddingLeft();
            int paddingTop = this.mButtonNeutral.getPaddingTop();
            int paddingRight = this.mButtonNeutral.getPaddingRight();
            int paddingBottom = this.mButtonNeutral.getPaddingBottom();
            int paddingOffset = this.mContext.getResources().getDimensionPixelSize(201654308);
            if (!isCenterDialog()) {
                int buttonColor = this.mContext.getResources().getColor(201719841);
                this.mButtonPositive.setTextColor(buttonColor);
                this.mButtonNegative.setTextColor(buttonColor);
                boolean hasMessage = !TextUtils.isEmpty(this.mMessage);
                boolean hasTitle = !TextUtils.isEmpty(this.mTitle);
                this.mButtonNeutral.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + paddingOffset);
                Button button2 = this.mButtonNeutral;
                button2.setMinHeight(button2.getMinHeight() + paddingOffset);
                if (!hasMessage && !hasTitle && hasNeutralText() && !hasPositiveText()) {
                    this.mButtonNeutral.setPadding(paddingLeft, paddingTop + paddingOffset, paddingRight, paddingBottom);
                    Button button3 = this.mButtonNeutral;
                    button3.setMinHeight(button3.getMinHeight() + paddingOffset);
                }
            }
            if (needSetButtonsVertical()) {
                this.mButtonPositive.setPadding(paddingLeft, paddingTop + paddingOffset, paddingRight, paddingBottom);
                Button button4 = this.mButtonPositive;
                button4.setMinHeight(button4.getMinHeight() + paddingOffset);
                this.mButtonNegative.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + paddingOffset);
                Button button5 = this.mButtonNegative;
                button5.setMinHeight(button5.getMinHeight() + paddingOffset);
            }
        }
    }

    private void setButtonsBackground() {
        Button target;
        Button target2;
        if (!isCenterDialog() && !hasTitle() && !hasMessage() && getListView() == null && !hasCustomView()) {
            if (isSingleButton()) {
                if (hasPositiveText()) {
                    target2 = this.mButtonPositive;
                } else {
                    target2 = hasNeutralText() ? this.mButtonNeutral : this.mButtonNegative;
                }
                if (target2 != null) {
                    target2.setBackgroundResource(201850940);
                    return;
                }
                return;
            }
            if (isDoubleButtons()) {
                Button target3 = hasPositiveText() ? this.mButtonPositive : this.mButtonNeutral;
                if (target3 != null) {
                    target3.setBackgroundResource(201850940);
                    return;
                }
                return;
            }
            if (isTripleButtons() && (target = this.mButtonPositive) != null) {
                target.setBackgroundResource(201850940);
            }
        }
    }

    private boolean hasCustomView() {
        FrameLayout customLayout = (FrameLayout) this.mWindow.findViewById(R.id.custom);
        return customLayout.getChildCount() != 0;
    }

    private boolean isSingleButton() {
        return buttonCount() == 1;
    }

    private boolean isDoubleButtons() {
        return buttonCount() == 2;
    }

    private boolean isTripleButtons() {
        return buttonCount() == 3;
    }

    private boolean needSetButtonsVertical() {
        if (buttonCount() == 0) {
            return false;
        }
        int buttonPadding = this.mContext.getResources().getDimensionPixelOffset(201654309);
        int buttonWidth = (parentWidth() / buttonCount()) - (buttonPadding * 2);
        int positiveTextWidth = hasPositiveText() ? (int) this.mButtonPaint.measureText(this.mButtonPositiveText.toString()) : 0;
        int negativeTextWidth = hasNegativeText() ? (int) this.mButtonPaint.measureText(this.mButtonNegativeText.toString()) : 0;
        int neutralTextWidth = hasNeutralText() ? (int) this.mButtonPaint.measureText(this.mButtonNeutralText.toString()) : 0;
        return positiveTextWidth > buttonWidth || negativeTextWidth > buttonWidth || neutralTextWidth > buttonWidth;
    }

    private int buttonCount() {
        int count = 0;
        if (hasPositiveText()) {
            count = 0 + 1;
        }
        if (hasNegativeText()) {
            count++;
        }
        if (hasNeutralText()) {
            return count + 1;
        }
        return count;
    }

    private boolean hasPositiveText() {
        return !TextUtils.isEmpty(this.mButtonPositiveText);
    }

    private boolean hasNegativeText() {
        return !TextUtils.isEmpty(this.mButtonNegativeText);
    }

    private boolean hasNeutralText() {
        return !TextUtils.isEmpty(this.mButtonNeutralText);
    }

    private boolean hasMessage() {
        return !TextUtils.isEmpty(this.mMessage);
    }

    private boolean hasTitle() {
        return !TextUtils.isEmpty(this.mTitle);
    }

    private int parentWidth() {
        View parentPanel = this.mWindow.findViewById(R.id.resolver_empty_state_title);
        int parentPadding = 0;
        if (parentPanel != null) {
            parentPadding = parentPanel.getPaddingLeft();
        }
        return this.mWindow.getAttributes().width - (parentPadding * 2);
    }

    private void setButtonsVertical() {
        ViewStub viewStub = this.mButtonPanelStub;
        if (viewStub == null) {
            Log.e(TAG, "mButtonPanelStub is null when setButtonsVertical");
            return;
        }
        viewStub.setLayoutResource(201917447);
        this.mButtonPanelStub.inflate();
        View divider1 = this.mWindow.findViewById(201457729);
        View divider2 = this.mWindow.findViewById(201457730);
        if (isCenterDialog() && !TextUtils.isEmpty(this.mMessage)) {
            divider1.setVisibility(0);
        } else {
            divider2.setVisibility(0);
        }
    }

    private void setButtonsHorizontal() {
        ViewStub viewStub = this.mButtonPanelStub;
        if (viewStub == null) {
            Log.e(TAG, "mButtonPanelStub is null when setButtonsHorizontal");
            return;
        }
        viewStub.setLayoutResource(201917448);
        this.mButtonPanelStub.inflate();
        showHorizontalDivider();
    }

    private void showHorizontalDivider() {
        ImageView dividerOne = (ImageView) this.mWindow.findViewById(201457732);
        ImageView dividerTwo = (ImageView) this.mWindow.findViewById(201457733);
        if (buttonCount() == 2) {
            dividerOne.setVisibility(0);
        }
        if (buttonCount() == 3) {
            dividerOne.setVisibility(0);
            dividerTwo.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpaceHeight() {
        View space;
        if (OplusContextUtil.isOplusAlertDialogBuilderStyle(this.mContext)) {
            return;
        }
        ViewGroup parent = (ViewGroup) this.mWindow.findViewById(R.id.resolver_empty_state_title);
        if (parent != null && (space = parent.findViewById(201457731)) != null) {
            ViewGroup.LayoutParams params = space.getLayoutParams();
            params.height = spaceHeight();
            space.setLayoutParams(params);
        }
        updateWindowFlag();
        WindowManager.LayoutParams params2 = this.mWindow.getAttributes();
        if (isSystemDialog(params2)) {
            if (isNavigationBarShow()) {
                if (isGravityCenter()) {
                    params2.y = 0;
                }
            } else {
                params2.y = 0;
            }
        }
        this.mWindow.setAttributes(params2);
    }

    private boolean isSystemDialog(WindowManager.LayoutParams params) {
        return params.type == 2003 || params.type == 2038;
    }

    /* loaded from: classes.dex */
    private static final class BottomSpaceHandler extends Handler {
        private static final int MSG_CONFIGURATION_CHANGED = 2;
        private static final int MSG_UPDATE_SPACE_HEIGHT = 1;
        private WeakReference<AlertControllerExt> mReference;

        public BottomSpaceHandler(AlertControllerExt controller) {
            this.mReference = new WeakReference<>(controller);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            AlertControllerExt controller = this.mReference.get();
            if (controller == null) {
                return;
            }
            switch (msg.what) {
                case 1:
                    controller.updateSpaceHeight();
                    return;
                case 2:
                    controller.onConfigurationChanged();
                    return;
                default:
                    return;
            }
        }
    }

    public boolean isMessageNeedScroll() {
        return this.mMessageNeedScroll;
    }

    public void setMessageNeedScroll(boolean messageNeedScroll) {
        this.mMessageNeedScroll = messageNeedScroll;
    }

    public void setDialogType(int dialogType) {
        this.mDialogType = dialogType;
    }

    public int getDialogType() {
        return this.mDialogType;
    }

    ListView getListView() {
        return this.mAlertController.getListView();
    }

    void loadRefObject() {
        if (this.mAlertController == null) {
            Log.d(TAG, "Failed to laodRefObject since mAlertController is null");
            return;
        }
        System.currentTimeMillis();
        IAlertControllerWrapper wrapper = this.mAlertController.getWrapper();
        if (this.mMessage == null && wrapper.getMessage() != null) {
            this.mMessage = wrapper.getMessage();
        }
        if (this.mTitle == null && wrapper.getTitle() != null) {
            this.mTitle = wrapper.getTitle();
        }
        if (this.mMessageView == null && wrapper.getMessageView() != null) {
            this.mMessageView = wrapper.getMessageView();
        }
        if (this.mButtonPositive == null && wrapper.getPositiveButton() != null) {
            this.mButtonPositive = wrapper.getPositiveButton();
        }
        if (this.mButtonPositiveText == null && wrapper.getPositionButtonText() != null) {
            this.mButtonPositiveText = wrapper.getPositionButtonText();
        }
        if (this.mButtonNegative == null && wrapper.getNegativeButton() != null) {
            this.mButtonNegative = wrapper.getNegativeButton();
        }
        if (this.mButtonNegativeText == null && wrapper.getNegativeButtonText() != null) {
            this.mButtonNegativeText = wrapper.getNegativeButtonText();
        }
        if (this.mButtonNeutral == null && wrapper.getNeutralButton() != null) {
            this.mButtonNeutral = wrapper.getNeutralButton();
        }
        if (this.mButtonNeutralText == null && wrapper.getNeutralButtonText() != null) {
            this.mButtonNeutralText = wrapper.getNeutralButtonText();
        }
        if (this.mScrollView == null && wrapper.getScrollView() != null) {
            this.mScrollView = wrapper.getScrollView();
        }
    }
}
