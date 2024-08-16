package com.coui.appcompat.touchsearchview;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.n0;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.oplus.os.LinearmotorVibrator;
import com.oplus.sceneservice.sdk.dataprovider.bean.SceneStatusInfo;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.zoomwindow.OplusZoomWindowManager;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.list.R$array;
import com.support.list.R$drawable;
import com.support.list.R$id;
import com.support.list.R$integer;
import com.support.list.R$layout;
import com.support.list.R$string;
import com.support.list.R$style;
import com.support.list.R$styleable;
import e2.COUIOrientationUtil;
import h3.UIUtil;
import java.util.ArrayList;
import java.util.List;
import k3.VibrateUtils;
import n3.BaseSpringSystem;
import n3.SimpleSpringListener;
import n3.SpringListener;
import n3.SpringSystem;
import n3.f;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;
import z2.COUIChangeTextUtil;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class COUITouchSearchView extends View implements View.OnClickListener {
    private static final int BG_ALIGN_MIDDLE = 0;
    private static final int BG_ALIGN_RIGHT = 2;
    private static final boolean DEBUG = false;
    private static final int ENABLED = 0;
    private static final int ENABLED_MASK = 32;
    private static final int INVALID_POINTER = -1;
    private static final int MIN_COUNT_RATIO = 3;
    public static final int MIN_SECTIONS_NUM = 5;
    private static final int MIN_SIZE_COUNT = 5;
    private static final int PFLAG_DRAWABLE_STATE_DIRTY = 1024;
    private static final int PFLAG_PRESSED = 16384;
    private static final int SEC_WINDOW_SHOW_DELAY_DURATION = 1000;
    private static final String TAG = "COUITouchSearchView";
    private static final int VIEW_STATE_ACCELERATED = 64;
    private static final int VIEW_STATE_ACTIVATED = 32;
    private static final int VIEW_STATE_DRAG_CAN_ACCEPT = 256;
    private static final int VIEW_STATE_DRAG_HOVERED = 512;
    private static final int VIEW_STATE_ENABLED = 8;
    private static final int VIEW_STATE_FOCUSED = 4;
    private static final int VIEW_STATE_HOVERED = 128;
    private static final int[] VIEW_STATE_IDS;
    private static final int VIEW_STATE_PRESSED = 16;
    private static final int VIEW_STATE_SELECTED = 2;
    private static final int VIEW_STATE_WINDOW_FOCUSED = 1;
    private static int sSTYLEABLELENGTH;
    private static int[][] sVIEWSETS;
    private static int[][][] sVIEWSTATESETS;
    private float mAccessibilityTouchDownY;
    private int mActivePointerId;
    private int mBackgroundAlignMode;
    private int mBackgroundLeftMargin;
    private int mBackgroundRightMargin;
    private int mBackgroundWidth;
    private Drawable mCOUITouchFirstPopTopBg;
    private int mCellHeight;
    private Context mContext;
    private ColorStateList mDefaultTextColor;
    private int mDefaultTextSize;
    private Runnable mDismissTask;
    private CharSequence mDisplayKey;
    private CharSequence mDot;
    private boolean mEnableAdaptiveVibrator;
    private PatternExploreByTouchHelper mExploreByTouchHelper;
    private final SpringListener mFirstAlphaListener;
    private boolean mFirstIsCharacter;
    private PopupWindow mFirstKeyPopupWindow;
    private boolean mFirstLayout;
    private f mFirstSpring;
    private Typeface mFontFace;
    private boolean mFrameChanged;
    private Handler mHandler;
    private boolean mHasMotorVibrator;
    private boolean mHeightNotEnough;
    private List<int[]> mIconState;
    private boolean mIsAccessibilityEnabled;
    private int mItemSpacing;
    private String[] mKEYS;
    private ArrayList<Key> mKey;
    private Drawable mKeyCollectDrawable;
    private int mKeyDrawableHeight;
    private int mKeyDrawableWidth;
    private int[] mKeyIndexAndOriginalIndex;
    private int mKeyIndices;
    private int mKeyPaddingX;
    private int mKeyPaddingY;
    private LayoutInflater mLayoutInflater;
    private Object mLinearMotorVibrator;
    private int[] mLocationInScreen;
    private int mLowVelocityThreshold;
    private TextPaint mMeasurePaint;
    private int mMidVelocityThreshold;
    private int mMinHeight;
    private int mPopupFirstTextHeight;
    private TextView mPopupFirstTextView;
    private int mPopupFirstTextWidth;
    private int mPopupFirstWidth;
    private int mPopupSecondTextHeight;
    private int mPopupSecondTextViewSize;
    private int mPopupSecondTextWidth;
    private int mPopupWinSecondNameMaxHeight;
    private int mPopupWindowEndGap;
    private int mPopupWindowEndMargin;
    private int mPopupWindowFirstKeyTextSize;
    private int mPopupWindowFirstLocalx;
    private int mPopupWindowFirstLocaly;
    private int mPopupWindowFirstTextColor;
    private int mPopupWindowMinTop;
    private int mPopupWindowSecondLocalx;
    private int mPopupWindowSecondLocaly;
    private Rect mPositionRect;
    private int mPreviousIndex;
    protected List<Integer> mPrivateFlags;
    private int mScrollViewHeight;
    private ViewGroup mSecondKeyContainer;
    private PopupWindow mSecondKeyPopupWindow;
    private ScrollView mSecondKeyScrollView;
    private int mSecondPopupMargin;
    private int mSecondPopupOffset;
    private BaseSpringSystem mSpringSystem;
    private int mStyle;
    private ColorStateList mTextColor;
    private int mTotalItemHeight;
    private int mTouchPaddingEnd;
    private int mTouchPaddingStart;
    private TouchSearchActionListener mTouchSearchActionListener;
    private int mTouchSlop;
    private int mTrackerMaxVelocity;
    private int mTrackerPeriod;
    private ColorStateList mUserTextColor;
    private int mUserTextSize;
    private VelocityTracker mVelocityTracker;
    private float mVibrateIntensity;
    private int mVibrateLevel;
    private OplusZoomWindowManager mZoomWindowManager;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class PatternExploreByTouchHelper extends ExploreByTouchHelper {
        private Rect mTempRect;

        public PatternExploreByTouchHelper(View view) {
            super(view);
            this.mTempRect = new Rect();
        }

        private Rect getBoundsForVirtualView() {
            Rect rect = this.mTempRect;
            rect.left = 0;
            rect.top = 0;
            rect.right = COUITouchSearchView.this.getWidth();
            rect.bottom = COUITouchSearchView.this.getHeight();
            return rect;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            return (f10 < 0.0f || f10 > ((float) COUITouchSearchView.this.getWidth()) || f11 < 0.0f || f11 > ((float) COUITouchSearchView.this.getHeight())) ? -1 : 0;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            list.add(0);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper, androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            sendEventForVirtualView(i10, 4);
            return false;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            if (COUITouchSearchView.this.mDisplayKey == null || COUITouchSearchView.this.mDisplayKey.equals("")) {
                return;
            }
            accessibilityEvent.setContentDescription(COUITouchSearchView.this.mDisplayKey);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
            if (COUITouchSearchView.this.mDisplayKey != null && !COUITouchSearchView.this.mDisplayKey.equals("")) {
                accessibilityEvent.getText().add(COUITouchSearchView.this.mDisplayKey);
            }
            super.onPopulateEventForVirtualView(i10, accessibilityEvent);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.Z(COUITouchSearchView.this.mDisplayKey);
            accessibilityNodeInfoCompat.y0(COUITouchSearchView.this.mDisplayKey);
            accessibilityNodeInfoCompat.V(COUITouchSearchView.class.getName());
            accessibilityNodeInfoCompat.Q(getBoundsForVirtualView());
        }
    }

    /* loaded from: classes.dex */
    public interface TouchSearchActionListener {
        void onKey(CharSequence charSequence);

        void onLongKey(CharSequence charSequence);

        void onNameClick(CharSequence charSequence);
    }

    static {
        int[] iArr = {R.attr.state_window_focused, 1, R.attr.state_selected, 2, R.attr.state_focused, 4, R.attr.state_enabled, 8, R.attr.state_pressed, 16, R.attr.state_activated, 32, R.attr.state_accelerated, 64, R.attr.state_hovered, 128, R.attr.state_drag_can_accept, 256, R.attr.state_drag_hovered, 512};
        VIEW_STATE_IDS = iArr;
        int length = R$styleable.ViewDrawableStates.length;
        sSTYLEABLELENGTH = length;
        int length2 = iArr.length / 2;
        if (length2 == length) {
            int length3 = iArr.length;
            int[] iArr2 = new int[length3];
            for (int i10 = 0; i10 < sSTYLEABLELENGTH; i10++) {
                int i11 = R$styleable.ViewDrawableStates[i10];
                int i12 = 0;
                while (true) {
                    int[] iArr3 = VIEW_STATE_IDS;
                    if (i12 < iArr3.length) {
                        if (iArr3[i12] == i11) {
                            int i13 = i10 * 2;
                            iArr2[i13] = i11;
                            iArr2[i13 + 1] = iArr3[i12 + 1];
                        }
                        i12 += 2;
                    }
                }
            }
            int i14 = 1 << length2;
            sVIEWSTATESETS = new int[i14][];
            sVIEWSETS = new int[i14];
            for (int i15 = 0; i15 < sVIEWSETS.length; i15++) {
                sVIEWSETS[i15] = new int[Integer.bitCount(i15)];
                int i16 = 0;
                for (int i17 = 0; i17 < length3; i17 += 2) {
                    if ((iArr2[i17 + 1] & i15) != 0) {
                        sVIEWSETS[i15][i16] = iArr2[i17];
                        i16++;
                    }
                }
            }
            return;
        }
        throw new IllegalStateException("VIEW_STATE_IDS array length does not match ViewDrawableStates style array");
    }

    public COUITouchSearchView(Context context) {
        this(context, null);
    }

    private int calDotRadio(int i10, int i11) {
        int i12 = i10 + i11;
        int i13 = i11 + 1;
        int i14 = i12 / i13;
        if (i13 * i14 >= i12) {
            i14--;
        } else if (i14 == 3) {
            i14 = 2;
        }
        return Math.max(2, i14);
    }

    private void computeVelocityWithTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    initVelocityTrackerIfNotExists();
                    this.mVelocityTracker.addMovement(motionEvent);
                    return;
                } else if (action != 3) {
                    return;
                }
            }
            recycleVelocityTracker();
            return;
        }
        initOrResetVelocityTracker();
        this.mVelocityTracker.addMovement(motionEvent);
    }

    private boolean dealWithAccessibilityTouchEvent(MotionEvent motionEvent) {
        int max;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mAccessibilityTouchDownY = motionEvent.getY();
            getLocationInWindow(this.mLocationInScreen);
            updatePopupWindow();
        } else if (action == 1) {
            float y4 = motionEvent.getY() - this.mAccessibilityTouchDownY;
            if (Math.abs(y4) > this.mTouchSlop) {
                if (y4 > 0.0f) {
                    if (this.mHeightNotEnough) {
                        int[] iArr = this.mKeyIndexAndOriginalIndex;
                        int i10 = iArr[1];
                        String[] strArr = this.mKEYS;
                        if (i10 < strArr.length - 1) {
                            iArr[1] = iArr[1] + 1;
                        }
                        if (iArr[1] < 0) {
                            return true;
                        }
                        max = iArr[1];
                        this.mKeyIndices = getKeyIndicesByCharacter(strArr[max]);
                    } else {
                        max = Math.min(this.mKey.size() - 1, this.mKeyIndices + 1);
                        this.mKeyIndices = max;
                    }
                } else if (this.mHeightNotEnough) {
                    int[] iArr2 = this.mKeyIndexAndOriginalIndex;
                    if (iArr2[1] > 0) {
                        iArr2[1] = iArr2[1] - 1;
                    }
                    if (iArr2[1] < 0) {
                        return true;
                    }
                    max = iArr2[1];
                    this.mKeyIndices = getKeyIndicesByCharacter(this.mKEYS[max]);
                } else {
                    max = Math.max(0, this.mKeyIndices - 1);
                    this.mKeyIndices = max;
                }
                int size = this.mKey.size();
                int i11 = this.mKeyIndices;
                if (i11 < 0 || i11 >= size) {
                    return true;
                }
                String str = this.mKEYS[max];
                if (displayChange(str)) {
                    onKeyChanged(str.toString(), this.mKey.get(this.mKeyIndices).getLeft() - this.mKeyPaddingX, this.mKey.get(this.mKeyIndices).getTop());
                    String charSequence = str.toString();
                    this.mDisplayKey = charSequence;
                    TouchSearchActionListener touchSearchActionListener = this.mTouchSearchActionListener;
                    if (touchSearchActionListener != null) {
                        touchSearchActionListener.onKey(charSequence);
                    }
                    invalidateTouchBarText();
                }
                if (!this.mSecondKeyPopupWindow.isShowing()) {
                    startFirstAnimationToDismiss();
                    this.mHandler.postDelayed(this.mDismissTask, 1000L);
                }
            }
        } else if (action == 3) {
            this.mIsAccessibilityEnabled = false;
        }
        return true;
    }

    private boolean dealWithTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getPointerId(motionEvent.getActionIndex()) > 0) {
            return false;
        }
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                    if (action != 3) {
                        if (action == 6) {
                            onSecondaryPointerUp(motionEvent);
                        }
                        return true;
                    }
                }
            }
            this.mActivePointerId = -1;
            this.mDisplayKey = "";
            if (!this.mSecondKeyPopupWindow.isShowing()) {
                startFirstAnimationToDismiss();
            }
            this.mIsAccessibilityEnabled = false;
            return true;
        }
        this.mActivePointerId = motionEvent.getPointerId(0);
        getLocationInWindow(this.mLocationInScreen);
        updatePopupWindow();
        invalidateKey((int) motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId)));
        return true;
    }

    private void detachedFromWindowClosing() {
        if (this.mFirstKeyPopupWindow.isShowing()) {
            this.mFirstKeyPopupWindow.dismiss();
        }
    }

    private boolean displayChange(CharSequence charSequence) {
        return (charSequence == null || charSequence.toString().equals(this.mDisplayKey.toString())) ? false : true;
    }

    private int getCharacterStartIndex() {
        return !this.mFirstIsCharacter ? 1 : 0;
    }

    private int getKeyIndices(int i10) {
        if (this.mKey.size() <= 0) {
            return -1;
        }
        int i11 = 0;
        if (this.mKey.size() != 1 && (this.mKey.size() <= 1 || this.mKey.get(1).getTop() <= i10)) {
            ArrayList<Key> arrayList = this.mKey;
            if (i10 > arrayList.get(arrayList.size() - 1).getTop()) {
                return this.mKey.size() - 1;
            }
            while (i11 < this.mKey.size()) {
                Key key = this.mKey.get(i11);
                if (i10 < key.mTouchTop || i10 > key.mTouchBottom) {
                    i11++;
                }
            }
            return -1;
        }
        return i11;
    }

    private int getKeyIndicesByCharacter(String str) {
        if (this.mHeightNotEnough) {
            for (int i10 = 0; i10 < this.mKey.size(); i10++) {
                Key key = this.mKey.get(i10);
                if (key.mIsDot) {
                    for (int i11 = 0; i11 < key.mHiddenCharList.size(); i11++) {
                        if (str.equals(key.mHiddenCharList.get(i11).mText)) {
                            return i10;
                        }
                    }
                } else if (str.equals(key.mText)) {
                    return i10;
                }
            }
        } else {
            for (int i12 = 0; i12 < this.mKey.size(); i12++) {
                if (this.mKey.get(i12).mText.equals(str)) {
                    return i12;
                }
            }
        }
        return 0;
    }

    private void getKeyIndicesWithDots(int i10) {
        int i11;
        int size = this.mKey.size();
        for (int i12 = 0; i12 < size; i12++) {
            Key key = this.mKey.get(i12);
            int i13 = key.mTouchTop;
            if (i10 >= i13 && i10 <= (i11 = key.mTouchBottom)) {
                if (key.mIsDot) {
                    int max = Math.max(Math.min((i10 - key.mTouchTop) / ((i11 - i13) / key.mHiddenCharList.size()), key.mHiddenCharList.size() - 1), 0);
                    int[] iArr = this.mKeyIndexAndOriginalIndex;
                    iArr[0] = i12;
                    iArr[1] = key.mHiddenCharList.get(max).mIndexInOriginalArray;
                    return;
                }
                int[] iArr2 = this.mKeyIndexAndOriginalIndex;
                iArr2[0] = i12;
                iArr2[1] = key.mIndexInOriginalArray;
                return;
            }
            if (i12 < size - 1 && i10 > key.mTouchBottom && i10 < this.mKey.get(i12 + 1).mTouchTop) {
                return;
            }
        }
    }

    private void initAccessibility(Context context) {
        PatternExploreByTouchHelper patternExploreByTouchHelper = new PatternExploreByTouchHelper(this);
        this.mExploreByTouchHelper = patternExploreByTouchHelper;
        ViewCompat.l0(this, patternExploreByTouchHelper);
        ViewCompat.w0(this, 1);
        this.mExploreByTouchHelper.invalidateRoot();
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initPopupWindow(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mLayoutInflater = layoutInflater;
        View inflate = layoutInflater.inflate(R$layout.coui_touchsearch_poppup_firstkey, (ViewGroup) null);
        this.mPopupFirstTextView = (TextView) inflate.findViewById(R$id.touchsearch_popup_content_textview);
        int e10 = (int) COUIChangeTextUtil.e(this.mPopupWindowFirstKeyTextSize, context.getResources().getConfiguration().fontScale, 4);
        this.mPopupWindowFirstKeyTextSize = e10;
        this.mPopupFirstTextView.setTextSize(0, e10);
        ViewGroup.LayoutParams layoutParams = this.mPopupFirstTextView.getLayoutParams();
        layoutParams.height = this.mPopupFirstTextHeight;
        layoutParams.width = this.mPopupFirstTextWidth;
        this.mPopupFirstTextView.setLayoutParams(layoutParams);
        this.mPopupFirstTextView.setBackground(this.mCOUITouchFirstPopTopBg);
        UIUtil.k(this.mPopupFirstTextView, context.getResources().getDimensionPixelOffset(R$dimen.support_shadow_size_level_four), context.getResources().getColor(R$color.coui_popup_outline_spot_shadow_color), this.mContext.getResources().getDimensionPixelOffset(R$dimen.support_shadow_size_level_for_touch_search_lowerP));
        this.mFirstKeyPopupWindow = new PopupWindow(context);
        COUIDarkModeUtil.b(this.mPopupFirstTextView, false);
        this.mFirstKeyPopupWindow.setWidth(this.mPopupFirstWidth);
        this.mFirstKeyPopupWindow.setHeight(this.mPopupFirstTextHeight);
        this.mFirstKeyPopupWindow.setBackgroundDrawable(null);
        this.mFirstKeyPopupWindow.setContentView(inflate);
        this.mFirstKeyPopupWindow.setAnimationStyle(0);
        this.mFirstKeyPopupWindow.setFocusable(false);
        this.mFirstKeyPopupWindow.setOutsideTouchable(false);
        this.mFirstKeyPopupWindow.setTouchable(false);
        View inflate2 = this.mLayoutInflater.inflate(R$layout.coui_touchsearch_second_name, (ViewGroup) null);
        this.mSecondKeyScrollView = (ScrollView) inflate2.findViewById(R$id.touchsearch_popup_content_scrollview);
        this.mSecondKeyContainer = (ViewGroup) inflate2.findViewById(R$id.touchsearch_popup_content_name);
        PopupWindow popupWindow = new PopupWindow(context);
        this.mSecondKeyPopupWindow = popupWindow;
        popupWindow.setWidth(this.mPopupFirstTextWidth);
        this.mSecondKeyPopupWindow.setContentView(inflate2);
        this.mSecondKeyPopupWindow.setAnimationStyle(0);
        this.mSecondKeyPopupWindow.setBackgroundDrawable(null);
        this.mSecondKeyPopupWindow.setFocusable(false);
        this.mSecondKeyPopupWindow.setOutsideTouchable(false);
        this.mFirstKeyPopupWindow.setEnterTransition(null);
        this.mFirstKeyPopupWindow.setExitTransition(null);
        this.mSecondKeyPopupWindow.setEnterTransition(null);
        this.mSecondKeyPopupWindow.setExitTransition(null);
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void invalidateKey(int i10) {
        String str;
        if (this.mHeightNotEnough) {
            getKeyIndicesWithDots(i10);
            int[] iArr = this.mKeyIndexAndOriginalIndex;
            if (iArr[0] < 0 || iArr[1] < 0) {
                return;
            }
            this.mKeyIndices = iArr[0];
            str = this.mKEYS[iArr[1]];
        } else {
            int keyIndices = getKeyIndices(i10);
            if (keyIndices < 0) {
                return;
            }
            this.mKeyIndices = keyIndices;
            str = this.mKEYS[keyIndices];
        }
        if (displayChange(str)) {
            onKeyChanged(str.toString(), this.mKey.get(this.mKeyIndices).getLeft() - this.mKeyPaddingX, this.mKey.get(this.mKeyIndices).getTop());
            String charSequence = str.toString();
            this.mDisplayKey = charSequence;
            TouchSearchActionListener touchSearchActionListener = this.mTouchSearchActionListener;
            if (touchSearchActionListener != null) {
                touchSearchActionListener.onKey(charSequence);
            }
            invalidateTouchBarText();
        }
    }

    private void invalidateTouchBarText() {
        int i10 = this.mKeyIndices;
        if (i10 != this.mPreviousIndex && -1 != i10) {
            performFeedback();
        }
        int i11 = this.mKeyIndices;
        if (i11 != this.mPreviousIndex && -1 != i11) {
            setIconPressed(i11, true);
            refreshIconState(this.mKeyIndices, this.mKey.get(this.mKeyIndices).getIcon());
            if (this.mTextColor != null) {
                int[] iconState = getIconState(this.mKeyIndices);
                ColorStateList colorStateList = this.mTextColor;
                this.mKey.get(this.mKeyIndices).mTextPaint.setColor(colorStateList.getColorForState(iconState, colorStateList.getDefaultColor()));
                invalidate();
            }
        }
        int i12 = this.mPreviousIndex;
        if (-1 != i12 && this.mKeyIndices != i12 && i12 < this.mKey.size()) {
            setItemRestore(this.mPreviousIndex);
        }
        this.mPreviousIndex = this.mKeyIndices;
    }

    private boolean isZoomWindowShown() {
        try {
            return OplusZoomWindowManager.getInstance().getCurrentZoomWindowState().windowShown;
        } catch (Error e10) {
            Log.d(TAG, "getCurrentZoomWindowState error: " + e10.getMessage());
            return false;
        } catch (Exception e11) {
            Log.d(TAG, "getCurrentZoomWindowState exception: " + e11.getMessage());
            return false;
        }
    }

    private void onKeyChanged(CharSequence charSequence, int i10, int i11) {
        if (this.mFirstKeyPopupWindow == null) {
            return;
        }
        this.mPopupFirstTextView.setText(charSequence);
        int paddingBottom = ((i11 + this.mLocationInScreen[1]) - this.mPopupFirstTextHeight) + this.mPopupFirstTextView.getPaddingBottom();
        if (COUIOrientationUtil.b(getContext()) || isZoomWindowShown()) {
            paddingBottom += UIUtil.f(getContext());
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mPopupFirstTextView.getLayoutParams();
        marginLayoutParams.topMargin = paddingBottom;
        this.mPopupFirstTextView.setLayoutParams(marginLayoutParams);
        startFirstAnimationToShow();
        sendAccessibilityEvent(8192);
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & 65280) >> 8;
        if (motionEvent.getPointerId(action) == this.mActivePointerId) {
            this.mActivePointerId = motionEvent.getPointerId(action == 0 ? 1 : 0);
        }
    }

    private boolean performAdaptiveFeedback() {
        VelocityTracker velocityTracker;
        if (this.mLinearMotorVibrator == null) {
            LinearmotorVibrator e10 = VibrateUtils.e(getContext());
            this.mLinearMotorVibrator = e10;
            this.mHasMotorVibrator = e10 != null;
        }
        if (this.mLinearMotorVibrator == null || (velocityTracker = this.mVelocityTracker) == null) {
            return false;
        }
        velocityTracker.computeCurrentVelocity(this.mTrackerPeriod, this.mTrackerMaxVelocity);
        int abs = (int) Math.abs(this.mVelocityTracker.getYVelocity());
        VibrateUtils.k((LinearmotorVibrator) this.mLinearMotorVibrator, abs > this.mMidVelocityThreshold ? 0 : 1, abs, this.mTrackerMaxVelocity, 1200, 1600, this.mVibrateLevel, this.mVibrateIntensity);
        return true;
    }

    private void performFeedback() {
        if ((this.mHasMotorVibrator && this.mEnableAdaptiveVibrator && performAdaptiveFeedback()) || performHapticFeedback(308)) {
            return;
        }
        performHapticFeedback(302);
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void refreshIcon() {
        int size = this.mKey.size();
        for (int i10 = 0; i10 < size; i10++) {
            int[][][] iArr = sVIEWSTATESETS;
            int[][] iArr2 = sVIEWSETS;
            iArr[i10] = new int[iArr2.length];
            System.arraycopy(iArr2, 0, iArr[i10], 0, iArr2.length);
        }
        for (int i11 = 0; i11 < size; i11++) {
            this.mIconState.add(new int[sSTYLEABLELENGTH]);
            this.mPrivateFlags.add(0);
            refreshIconState(i11, this.mKey.get(i11).getIcon());
            ColorStateList colorStateList = this.mTextColor;
            if (colorStateList != null) {
                this.mKey.get(i11).mTextPaint.setColor(colorStateList.getColorForState(getIconState(i11), this.mTextColor.getDefaultColor()));
            }
        }
    }

    private void reset() {
        this.mKey.clear();
        this.mIconState.clear();
        this.mPrivateFlags.clear();
        int[] iArr = this.mKeyIndexAndOriginalIndex;
        iArr[0] = -1;
        iArr[1] = -1;
    }

    private void setIconPressed(int i10, boolean z10) {
        int intValue = this.mPrivateFlags.get(i10).intValue();
        this.mPrivateFlags.set(i10, Integer.valueOf(z10 ? intValue | PFLAG_PRESSED : intValue & (-16385)));
    }

    private void setItemRestore(int i10) {
        setIconPressed(i10, false);
        refreshIconState(i10, this.mKey.get(i10).getIcon());
        if (this.mTextColor != null) {
            this.mKey.get(i10).mTextPaint.setColor(this.mTextColor.getColorForState(getIconState(i10), this.mTextColor.getDefaultColor()));
        }
        invalidate();
    }

    private void startFirstAnimationToDismiss() {
        this.mFirstSpring.o(UserProfileInfo.Constant.NA_LAT_LON);
        this.mHandler.postDelayed(this.mDismissTask, 1000L);
    }

    private void startFirstAnimationToShow() {
        if (!this.mFirstKeyPopupWindow.isShowing()) {
            if (n0.b(this)) {
                this.mFirstKeyPopupWindow.showAtLocation(this, 0, this.mPopupWindowFirstLocalx + this.mTouchPaddingStart + this.mPopupWindowEndGap, 0);
            } else {
                this.mFirstKeyPopupWindow.showAtLocation(this, 0, (this.mPopupWindowFirstLocalx + this.mTouchPaddingStart) - this.mPopupWindowEndGap, 0);
            }
        }
        this.mFirstSpring.m(1.0d);
        this.mFirstSpring.o(1.0d);
        this.mHandler.removeCallbacks(this.mDismissTask);
    }

    private void update() {
        int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
        if (height < this.mMinHeight) {
            return;
        }
        reset();
        updateKeys(height);
        refreshIcon();
    }

    private void updateBackGroundBound() {
        int i10;
        int i11;
        int i12 = this.mBackgroundAlignMode;
        if (i12 == 0) {
            int width = getWidth();
            int i13 = this.mBackgroundWidth;
            i10 = (width - i13) / 2;
            i11 = i13 + i10;
        } else if (i12 == 2) {
            i11 = getWidth() - this.mBackgroundRightMargin;
            i10 = i11 - this.mBackgroundWidth;
        } else {
            i10 = this.mBackgroundLeftMargin;
            i11 = i10 + this.mBackgroundWidth;
        }
        this.mPositionRect = new Rect(i10, 0, i11, getBottom() - getTop());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v10 */
    /* JADX WARN: Type inference failed for: r10v2, types: [java.lang.String, android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r9v11, types: [int] */
    /* JADX WARN: Type inference failed for: r9v13 */
    /* JADX WARN: Type inference failed for: r9v14 */
    private void updateKeys(int i10) {
        Drawable drawable;
        Drawable drawable2;
        int length = this.mKEYS.length;
        int paddingTop = getPaddingTop();
        Paint.FontMetricsInt fontMetricsInt = this.mMeasurePaint.getFontMetricsInt();
        int i11 = (this.mCellHeight - (fontMetricsInt.bottom - fontMetricsInt.top)) / 2;
        int characterStartIndex = length - getCharacterStartIndex();
        int i12 = this.mCellHeight;
        int i13 = this.mItemSpacing;
        int i14 = length - 1;
        int i15 = (characterStartIndex * i12) + (i13 * i14);
        boolean z10 = this.mFirstIsCharacter;
        if (!z10) {
            i15 += this.mKeyDrawableHeight;
        }
        Rect rect = this.mPositionRect;
        if (rect != null) {
            int i16 = rect.left;
            int i17 = i16 + (((rect.right - i16) - this.mKeyDrawableWidth) / 2);
            int i18 = this.mTouchPaddingStart;
            this.mKeyPaddingX = (i17 + i18) - ((i18 + this.mTouchPaddingEnd) / 2);
        }
        ?? r10 = 0;
        if (i15 > i10) {
            boolean z11 = true;
            this.mHeightNotEnough = true;
            int i19 = i13 + i12;
            int i20 = 1;
            while (i20 < length) {
                i15 -= i19;
                if (i15 <= i10) {
                    break;
                } else {
                    i20++;
                }
            }
            int i21 = length - i20;
            int characterStartIndex2 = ((i21 - 1) - getCharacterStartIndex()) / 2;
            int i22 = i20 > characterStartIndex2 ? characterStartIndex2 : i20;
            if (this.mKEYS[i14].equals("#") && !this.mFirstIsCharacter && i21 % 2 == 0 && i20 > characterStartIndex2) {
                i20++;
                i21--;
                i22--;
            }
            int i23 = (paddingTop + (i10 - i15)) / 2;
            int i24 = i15 / length;
            ArrayList arrayList = new ArrayList(i22);
            for (int i25 = 0; i25 < i20; i25++) {
                int i26 = i25 % i22;
                if (arrayList.size() == i26) {
                    arrayList.add(0);
                }
                arrayList.set(i26, Integer.valueOf(((Integer) arrayList.get(i26)).intValue() + 1));
            }
            if (!this.mFirstIsCharacter && (drawable2 = this.mKeyCollectDrawable) != null) {
                Key key = new Key(drawable2, this.mKEYS[0]);
                key.setLeft(this.mKeyPaddingX);
                key.setTop(i23);
                key.mTouchTop = i23;
                key.mTouchBottom = this.mKeyDrawableHeight + i23;
                this.mKey.add(key);
                i23 += this.mKeyDrawableHeight + this.mItemSpacing;
            }
            int characterStartIndex3 = getCharacterStartIndex();
            boolean z12 = this.mFirstIsCharacter;
            int calDotRadio = calDotRadio(i21, i22);
            int characterStartIndex4 = getCharacterStartIndex();
            int i27 = 0;
            while (characterStartIndex4 < i21) {
                Key key2 = new Key(r10, r10);
                key2.setLeft(this.mKeyPaddingX);
                key2.setTop(i23 + i11);
                if (this.mKey.size() % calDotRadio == z12 && i27 < i22) {
                    key2.mIsDot = z11;
                    key2.mText = this.mDot.toString();
                    key2.mTouchTop = i23;
                    key2.mTouchBottom = this.mCellHeight + i23 + this.mItemSpacing;
                    key2.mHiddenCharList = new ArrayList();
                    int i28 = 0;
                    for (?? r92 = z11; i28 < ((Integer) arrayList.get(i27)).intValue() + r92; r92 = 1) {
                        Key key3 = new Key();
                        key3.mIndexInOriginalArray = characterStartIndex3;
                        key3.mText = this.mKEYS[characterStartIndex3];
                        key2.mHiddenCharList.add(key3);
                        i28++;
                        characterStartIndex3++;
                    }
                    i27++;
                } else {
                    key2.mIndexInOriginalArray = characterStartIndex3;
                    key2.mText = this.mKEYS[characterStartIndex3];
                    key2.mTouchTop = i23;
                    key2.mTouchBottom = this.mCellHeight + i23 + this.mItemSpacing;
                    characterStartIndex3++;
                }
                i23 += this.mCellHeight + this.mItemSpacing;
                this.mKey.add(key2);
                characterStartIndex4++;
                z11 = true;
                r10 = 0;
            }
        } else {
            this.mHeightNotEnough = false;
            int i29 = (paddingTop + (i10 - i15)) / 2;
            if (!z10 && (drawable = this.mKeyCollectDrawable) != null) {
                Key key4 = new Key(drawable, this.mKEYS[0]);
                key4.setLeft(this.mKeyPaddingX);
                key4.setTop(i29);
                this.mKey.add(key4);
                i29 += this.mKeyDrawableHeight + this.mItemSpacing;
            }
            for (int characterStartIndex5 = getCharacterStartIndex(); characterStartIndex5 < length; characterStartIndex5++) {
                Key key5 = new Key(null, this.mKEYS[characterStartIndex5]);
                key5.setLeft(this.mKeyPaddingX);
                key5.setTop(i29 + i11);
                key5.mTouchTop = i29;
                key5.mTouchBottom = this.mCellHeight + i29 + this.mItemSpacing;
                this.mKey.add(key5);
                i29 += this.mCellHeight + this.mItemSpacing;
            }
        }
        this.mTotalItemHeight = i15;
    }

    private void updatePopupWindow() {
        if (this.mKey.size() < 1) {
            return;
        }
        if (n0.b(this)) {
            int measuredWidth = this.mLocationInScreen[0] + getMeasuredWidth() + this.mPopupWindowEndMargin;
            this.mPopupWindowFirstLocalx = measuredWidth;
            this.mPopupWindowSecondLocalx = measuredWidth + this.mPopupFirstTextWidth + this.mSecondPopupMargin;
        } else {
            int i10 = (this.mLocationInScreen[0] - this.mPopupWindowEndMargin) - this.mPopupFirstWidth;
            this.mPopupWindowFirstLocalx = i10;
            this.mPopupWindowSecondLocalx = (i10 - this.mSecondPopupMargin) - this.mPopupSecondTextWidth;
        }
        int b10 = UIUtil.b(getContext());
        this.mPopupWindowFirstLocaly = this.mLocationInScreen[1] - ((b10 - getHeight()) / 2);
        if (this.mFirstKeyPopupWindow.isShowing() && this.mFirstKeyPopupWindow.getHeight() != b10) {
            this.mFirstKeyPopupWindow.update(this.mPopupWindowFirstLocalx, this.mPopupWindowFirstLocaly, this.mPopupFirstWidth, b10);
        } else if (!this.mFirstKeyPopupWindow.isShowing()) {
            this.mFirstKeyPopupWindow.setWidth(this.mPopupFirstWidth);
            this.mFirstKeyPopupWindow.setHeight(b10);
        }
        if (this.mSecondKeyPopupWindow.isShowing()) {
            updateSecondPopup();
        }
    }

    private void updateSecondPopup() {
        if (this.mSecondKeyPopupWindow.isShowing()) {
            this.mSecondKeyPopupWindow.update(this.mPopupWindowSecondLocalx, this.mPopupWindowSecondLocaly, this.mPopupSecondTextWidth, this.mScrollViewHeight);
            return;
        }
        this.mSecondKeyPopupWindow.setWidth(this.mPopupSecondTextWidth);
        this.mSecondKeyPopupWindow.setHeight(this.mScrollViewHeight);
        this.mSecondKeyPopupWindow.showAtLocation(this, 0, this.mPopupWindowSecondLocalx, this.mPopupWindowSecondLocaly);
    }

    public void closing() {
        int i10 = this.mPreviousIndex;
        if (-1 != i10 && this.mKeyIndices != i10 && i10 < this.mKey.size()) {
            setItemRestore(this.mPreviousIndex);
        }
        int size = this.mKey.size();
        int i11 = this.mKeyIndices;
        if (i11 > -1 && i11 < size) {
            setItemRestore(i11);
        }
        this.mPreviousIndex = -1;
        if (this.mFirstKeyPopupWindow.isShowing()) {
            startFirstAnimationToDismiss();
        }
        if (this.mSecondKeyPopupWindow.isShowing()) {
            this.mSecondKeyPopupWindow.dismiss();
        }
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.mExploreByTouchHelper.dispatchHoverEvent(motionEvent) | super.dispatchHoverEvent(motionEvent);
    }

    protected int[] getIconState(int i10) {
        int intValue = this.mPrivateFlags.get(i10).intValue();
        if ((intValue & PFLAG_DRAWABLE_STATE_DIRTY) != 0) {
            this.mIconState.set(i10, onCreateIconState(i10, 0));
            this.mPrivateFlags.set(i10, Integer.valueOf(intValue & (-1025)));
        }
        return this.mIconState.get(i10);
    }

    public PopupWindow getPopupWindow() {
        return this.mFirstKeyPopupWindow;
    }

    public TouchSearchActionListener getTouchSearchActionListener() {
        return this.mTouchSearchActionListener;
    }

    protected void iconStateChanged(int i10, Drawable drawable) {
        int[] iconState = getIconState(i10);
        if (drawable == null || !drawable.isStateful()) {
            return;
        }
        drawable.setState(iconState);
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstSpring.a(this.mFirstAlphaListener);
        this.mFirstSpring.m(1.0d);
        VibrateUtils.i(getContext());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.mTouchSearchActionListener.onNameClick(((TextView) view).getText());
    }

    protected int[] onCreateIconState(int i10, int i11) {
        int intValue = this.mPrivateFlags.get(i10).intValue();
        int i12 = (this.mPrivateFlags.get(i10).intValue() & PFLAG_PRESSED) != 0 ? 16 : 0;
        if ((intValue & 32) == 0) {
            i12 |= 8;
        }
        if (hasWindowFocus()) {
            i12 |= 1;
        }
        int[] iArr = sVIEWSTATESETS[i10][i12];
        if (i11 == 0) {
            return iArr;
        }
        if (iArr != null) {
            int[] iArr2 = new int[iArr.length + i11];
            System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
            return iArr2;
        }
        return new int[i11];
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstSpring.k();
        detachedFromWindowClosing();
        closing();
        VibrateUtils.l();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((getHeight() - getPaddingTop()) - getPaddingBottom() < this.mMinHeight) {
            return;
        }
        if (!this.mFirstIsCharacter && this.mKey.size() > 0 && this.mKey.get(0).getIcon() != null) {
            int left = this.mKey.get(0).getLeft();
            int top = this.mKey.get(0).getTop();
            this.mKeyCollectDrawable.setBounds(left, top, this.mKeyDrawableWidth + left, this.mKeyDrawableHeight + top);
            this.mKeyCollectDrawable.draw(canvas);
        }
        int size = this.mKey.size();
        for (int characterStartIndex = getCharacterStartIndex(); characterStartIndex < size; characterStartIndex++) {
            Paint.FontMetricsInt fontMetricsInt = this.mKey.get(characterStartIndex).mTextPaint.getFontMetricsInt();
            TextPaint textPaint = this.mKey.get(characterStartIndex).mTextPaint;
            String str = this.mKey.get(characterStartIndex).mText;
            if (str != null) {
                canvas.drawText(str, this.mKey.get(characterStartIndex).getLeft() + ((this.mKeyDrawableWidth - ((int) textPaint.measureText(str))) / 2), this.mKey.get(characterStartIndex).getTop() - fontMetricsInt.top, textPaint);
            }
        }
    }

    @Override // android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (this.mFirstLayout || this.mFrameChanged) {
            updateBackGroundBound();
            update();
            if (this.mFirstLayout) {
                this.mFirstLayout = false;
            }
            if (this.mFrameChanged) {
                this.mFrameChanged = false;
            }
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        this.mFrameChanged = true;
        super.onSizeChanged(i10, i11, i12, i13);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mIsAccessibilityEnabled = COUIAccessibilityUtil.isTalkbackEnabled(getContext());
        }
        if (this.mEnableAdaptiveVibrator) {
            computeVelocityWithTouchEvent(motionEvent);
        }
        if (this.mIsAccessibilityEnabled) {
            return dealWithAccessibilityTouchEvent(motionEvent);
        }
        return dealWithTouchEvent(motionEvent);
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArray = null;
        if ("attr".equals(resourceTypeName)) {
            typedArray = this.mContext.obtainStyledAttributes(null, R$styleable.COUITouchSearchView, this.mStyle, 0);
        } else if ("style".equals(resourceTypeName)) {
            typedArray = this.mContext.obtainStyledAttributes(null, R$styleable.COUITouchSearchView, 0, this.mStyle);
        }
        if (typedArray != null) {
            this.mKeyCollectDrawable = typedArray.getDrawable(R$styleable.COUITouchSearchView_couiKeyCollect);
            this.mTextColor = typedArray.getColorStateList(R$styleable.COUITouchSearchView_couiKeyTextColor);
            Drawable drawable = this.mContext.getDrawable(R$drawable.coui_touch_search_popup_bg);
            this.mCOUITouchFirstPopTopBg = drawable;
            setFirstKeyPopupDrawable(drawable);
            setPopupWindowTextColor(typedArray.getColor(R$styleable.COUITouchSearchView_couiPopupWinFirstTextColor, COUIContextUtil.a(this.mContext, R$attr.couiColorPrimaryNeutral)));
            typedArray.recycle();
        }
        ArrayList<Key> arrayList = this.mKey;
        if (arrayList != null && arrayList.size() > 0) {
            this.mKey.get(0).mIcon = this.mKeyCollectDrawable;
        }
        for (int i10 = 0; i10 < this.mKey.size(); i10++) {
            this.mIconState.add(new int[sSTYLEABLELENGTH]);
            this.mPrivateFlags.add(0);
            refreshIconState(i10, this.mKey.get(i10).getIcon());
            ColorStateList colorStateList = this.mTextColor;
            if (colorStateList != null) {
                this.mKey.get(i10).mTextPaint.setColor(colorStateList.getColorForState(getIconState(i10), this.mTextColor.getDefaultColor()));
            }
        }
        invalidate();
    }

    protected void refreshIconState(int i10, Drawable drawable) {
        this.mPrivateFlags.set(i10, Integer.valueOf(this.mPrivateFlags.get(i10).intValue() | PFLAG_DRAWABLE_STATE_DIRTY));
        iconStateChanged(i10, drawable);
    }

    public void setBackgroundAlignMode(int i10) {
        this.mBackgroundAlignMode = i10;
    }

    public void setBackgroundLeftMargin(int i10) {
        this.mBackgroundLeftMargin = i10;
    }

    public void setBackgroundRightMargin(int i10) {
        this.mBackgroundRightMargin = i10;
    }

    public void setCharTextColor(ColorStateList colorStateList) {
        setCharTextColor(colorStateList, false);
    }

    public void setCharTextSize(int i10) {
        if (i10 != 0) {
            this.mUserTextSize = i10;
            this.mMeasurePaint.setTextSize(i10);
        }
    }

    public void setDefaultTextColor(ColorStateList colorStateList) {
        this.mTextColor = colorStateList;
        for (int i10 = 0; i10 < this.mKey.size(); i10++) {
            this.mIconState.add(new int[sSTYLEABLELENGTH]);
            this.mPrivateFlags.add(new Integer(0));
            refreshIconState(i10, this.mKey.get(i10).getIcon());
            ColorStateList colorStateList2 = this.mTextColor;
            if (colorStateList2 != null) {
                this.mKey.get(i10).mTextPaint.setColor(colorStateList2.getColorForState(getIconState(i10), this.mTextColor.getDefaultColor()));
            }
        }
        invalidate();
    }

    public void setDefaultTextSize(int i10) {
        this.mDefaultTextSize = i10;
    }

    public void setEnableAdaptiveVibrator(boolean z10) {
        this.mEnableAdaptiveVibrator = z10;
    }

    public void setFirstKeyIsCharacter(boolean z10) {
        this.mFirstIsCharacter = z10;
    }

    public void setFirstKeyPopupDrawable(Drawable drawable) {
        if (drawable != null) {
            this.mPopupFirstTextView.setText((CharSequence) null);
            this.mPopupFirstTextView.setBackground(drawable);
        } else {
            this.mPopupFirstTextView.setText(this.mKey.get(this.mKeyIndices).mText);
            this.mPopupFirstTextView.setBackground(this.mCOUITouchFirstPopTopBg);
        }
    }

    public void setFirstKeyPopupWindowSize(int i10, int i11) {
        if (this.mPopupFirstTextWidth == i10 && this.mPopupFirstTextHeight == i11) {
            return;
        }
        this.mPopupFirstTextWidth = i10;
        this.mPopupFirstTextHeight = i11;
        ViewGroup.LayoutParams layoutParams = this.mPopupFirstTextView.getLayoutParams();
        layoutParams.height = i11;
        layoutParams.width = i10;
        this.mPopupFirstTextView.setLayoutParams(layoutParams);
        updatePopupWindow();
    }

    public void setKeyCollectDrawable(Drawable drawable) {
        this.mKeyCollectDrawable = drawable;
    }

    public void setKeys(String[] strArr) {
        if (strArr == null || strArr[0].equals(" ") || strArr.length < 5) {
            return;
        }
        this.mKEYS = strArr;
        update();
        invalidate();
    }

    public void setName(String[] strArr) {
        int length = strArr == null ? 0 : strArr.length;
        if (length == 0) {
            return;
        }
        int childCount = this.mSecondKeyContainer.getChildCount();
        if (length > childCount) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.mPopupFirstTextWidth, this.mPopupFirstTextHeight);
            for (int i10 = 0; i10 < length - childCount; i10++) {
                TextView textView = (TextView) this.mLayoutInflater.inflate(R$layout.coui_touchsearch_popup_content_item, (ViewGroup) null);
                textView.setTextSize(0, (int) COUIChangeTextUtil.e(this.mPopupSecondTextViewSize, this.mContext.getResources().getConfiguration().fontScale, 4));
                this.mSecondKeyContainer.addView(textView, layoutParams);
                textView.setOnClickListener(this);
            }
        } else {
            for (int i11 = 0; i11 < childCount - length; i11++) {
                this.mSecondKeyContainer.removeViewAt((childCount - i11) - 1);
            }
        }
        for (int i12 = 0; i12 < length; i12++) {
            ((TextView) this.mSecondKeyContainer.getChildAt(i12)).setText(strArr[i12]);
        }
        int i13 = ((ViewGroup.MarginLayoutParams) this.mPopupFirstTextView.getLayoutParams()).topMargin;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mSecondKeyScrollView.getLayoutParams();
        int i14 = length * this.mPopupSecondTextHeight;
        this.mScrollViewHeight = i14;
        int min = Math.min(i14, this.mPopupWinSecondNameMaxHeight);
        this.mScrollViewHeight = min;
        marginLayoutParams.height = min;
        this.mSecondKeyScrollView.setLayoutParams(marginLayoutParams);
        this.mPopupWindowSecondLocaly = (this.mPopupWindowFirstLocaly + i13) - ((this.mScrollViewHeight - this.mPopupFirstTextHeight) / 2);
        int height = this.mLocationInScreen[1] + getHeight();
        int i15 = this.mSecondPopupOffset;
        int i16 = (height + i15) - this.mScrollViewHeight;
        int i17 = this.mLocationInScreen[1] - i15;
        int i18 = this.mPopupWindowSecondLocaly;
        if (i18 < i17) {
            this.mPopupWindowSecondLocaly = i17;
        } else if (i18 > i16) {
            this.mPopupWindowSecondLocaly = i16;
        }
        updateSecondPopup();
    }

    public void setPopText(String str, String str2) {
        startFirstAnimationToShow();
        this.mPopupFirstTextView.setText(str2);
        this.mKeyIndices = (str.charAt(0) - 'A') + 2;
        if (str.equals("#")) {
            this.mKeyIndices = 1;
        }
        int length = this.mKEYS.length;
    }

    public void setPopupSecondTextHeight(int i10) {
        this.mPopupSecondTextHeight = i10;
    }

    public void setPopupSecondTextViewSize(int i10) {
        this.mPopupSecondTextViewSize = i10;
    }

    public void setPopupSecondTextWidth(int i10) {
        this.mPopupSecondTextWidth = i10;
    }

    public void setPopupTextView(String str) {
        startFirstAnimationToShow();
        setTouchBarSelectedText(str);
    }

    public void setPopupWindowFirstTextSize(int i10) {
        if (this.mPopupWindowFirstKeyTextSize != i10) {
            this.mPopupWindowFirstKeyTextSize = i10;
            this.mPopupFirstTextView.setTextSize(0, i10);
        }
    }

    public void setPopupWindowTextColor(int i10) {
        if (this.mPopupWindowFirstTextColor != i10) {
            this.mPopupWindowFirstTextColor = i10;
            this.mPopupFirstTextView.setTextColor(i10);
            invalidate();
        }
    }

    public void setPopupWindowTopMinCoordinate(int i10) {
        if (this.mPopupWindowMinTop != i10) {
            this.mPopupWindowMinTop = i10;
        }
    }

    public void setSecondPopupMargin(int i10) {
        this.mSecondPopupMargin = i10;
    }

    public void setSecondPopupOffset(int i10) {
        this.mSecondPopupOffset = i10;
    }

    @Deprecated
    public void setSmartShowMode(String[] strArr, int[] iArr) {
        if (strArr == null || iArr == null || strArr[0].equals(" ") || strArr.length < 5) {
            return;
        }
        this.mKEYS = strArr;
        update();
        invalidate();
    }

    public void setTouchBarSelectedText(String str) {
        this.mPopupFirstTextView.setText(str);
        this.mPreviousIndex = this.mKeyIndices;
        this.mKeyIndices = getKeyIndicesByCharacter(str);
        this.mDisplayKey = str;
        if (str.equals("#")) {
            this.mKeyIndices = 1;
        }
        int size = this.mKey.size();
        int i10 = this.mKeyIndices;
        if (i10 < 0 || i10 > size - 1) {
            return;
        }
        invalidateTouchBarText();
    }

    public void setTouchSearchActionListener(TouchSearchActionListener touchSearchActionListener) {
        this.mTouchSearchActionListener = touchSearchActionListener;
    }

    public void setVibrateIntensity(float f10) {
        this.mVibrateIntensity = f10;
    }

    public void setVibrateLevel(int i10) {
        this.mVibrateLevel = i10;
    }

    public COUITouchSearchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.list.R$attr.couiTouchSearchViewStyle);
    }

    public void setCharTextColor(ColorStateList colorStateList, boolean z10) {
        if (colorStateList != null) {
            this.mUserTextColor = colorStateList;
        }
        if (z10) {
            update();
        }
    }

    public COUITouchSearchView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Widget_COUI_COUITouchSearchView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Key {
        List<Key> mHiddenCharList;
        Drawable mIcon;
        int mIndexInOriginalArray;
        boolean mIsDot;
        int mLeft;
        String mText;
        TextPaint mTextPaint;
        int mTop;
        int mTouchBottom;
        int mTouchTop;

        Key() {
            this.mIcon = null;
            this.mText = null;
            this.mTextPaint = null;
        }

        public Drawable getIcon() {
            Drawable drawable = this.mIcon;
            if (drawable != null) {
                return drawable;
            }
            return null;
        }

        public int getLeft() {
            return this.mLeft;
        }

        public String getText() {
            String str = this.mText;
            if (str != null) {
                return str;
            }
            return null;
        }

        public int getTop() {
            return this.mTop;
        }

        public void setLeft(int i10) {
            this.mLeft = i10;
        }

        public void setTop(int i10) {
            this.mTop = i10;
        }

        Key(Drawable drawable, String str) {
            this.mTextPaint = null;
            this.mIcon = drawable;
            this.mText = str;
            this.mTextPaint = new TextPaint(1);
            this.mTextPaint.setTextSize(COUITouchSearchView.this.mUserTextSize == 0 ? COUITouchSearchView.this.mDefaultTextSize : r3);
            COUITouchSearchView.this.mTextColor = COUITouchSearchView.this.mUserTextColor;
            if (COUITouchSearchView.this.mTextColor == null) {
                COUITouchSearchView.this.mTextColor = COUITouchSearchView.this.mDefaultTextColor;
            }
            if (COUITouchSearchView.this.mFontFace != null) {
                this.mTextPaint.setTypeface(COUITouchSearchView.this.mFontFace);
            }
        }
    }

    public COUITouchSearchView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10);
        this.mPrivateFlags = new ArrayList();
        this.mIconState = new ArrayList();
        this.mFirstLayout = true;
        this.mFrameChanged = false;
        this.mDisplayKey = "";
        this.mActivePointerId = -1;
        this.mKeyIndices = -1;
        this.mKeyIndexAndOriginalIndex = new int[]{-1, -1};
        this.mKeyCollectDrawable = null;
        this.mKey = new ArrayList<>();
        this.mPreviousIndex = -1;
        this.mFirstIsCharacter = false;
        this.mDefaultTextColor = null;
        this.mUserTextColor = null;
        this.mTextColor = null;
        this.mDefaultTextSize = 0;
        this.mUserTextSize = 0;
        this.mFontFace = null;
        this.mTrackerPeriod = 1000;
        this.mTrackerMaxVelocity = SceneStatusInfo.SceneConstant.TRIP_ARRIVE_END_STATION_IN_TIME_AND_LOCATION;
        this.mLowVelocityThreshold = SceneStatusInfo.SceneConstant.TRIP_GO_TO_STATION;
        this.mMidVelocityThreshold = SceneStatusInfo.SceneConstant.TRIP_IN_JOURNEY;
        this.mEnableAdaptiveVibrator = true;
        this.mHasMotorVibrator = true;
        this.mLinearMotorVibrator = null;
        this.mVibrateIntensity = 1.0f;
        SpringSystem g6 = SpringSystem.g();
        this.mSpringSystem = g6;
        this.mFirstSpring = g6.c();
        this.mFirstAlphaListener = new SimpleSpringListener() { // from class: com.coui.appcompat.touchsearchview.COUITouchSearchView.1
            @Override // n3.SimpleSpringListener, n3.SpringListener
            public void onSpringUpdate(f fVar) {
                View findViewById;
                double c10 = fVar.c();
                if (COUITouchSearchView.this.mFirstKeyPopupWindow == null || COUITouchSearchView.this.mFirstKeyPopupWindow.getContentView() == null || (findViewById = COUITouchSearchView.this.mFirstKeyPopupWindow.getContentView().findViewById(R$id.touchsearch_popup_content_textview)) == null) {
                    return;
                }
                findViewById.setAlpha((float) c10);
            }
        };
        this.mDismissTask = new Runnable() { // from class: com.coui.appcompat.touchsearchview.COUITouchSearchView.2
            @Override // java.lang.Runnable
            public void run() {
                if (COUITouchSearchView.this.mFirstSpring.e() == UserProfileInfo.Constant.NA_LAT_LON) {
                    COUITouchSearchView.this.mFirstKeyPopupWindow.dismiss();
                }
            }
        };
        this.mHandler = new Handler();
        this.mLocationInScreen = new int[2];
        COUIDarkModeUtil.b(this, false);
        this.mContext = context;
        Resources resources = getResources();
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i11;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUITouchSearchView, i10, i11);
        this.mBackgroundAlignMode = obtainStyledAttributes.getInt(R$styleable.COUITouchSearchView_couiBackgroundAlignMode, 0);
        this.mBackgroundLeftMargin = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITouchSearchView_couiMarginLeft, 0);
        this.mBackgroundRightMargin = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITouchSearchView_couiMarginRigh, 0);
        this.mPopupFirstTextHeight = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITouchSearchView_couiPopupWinFirstHeight, resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_popup_first_default_height));
        this.mPopupFirstTextWidth = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITouchSearchView_couiPopupWinFirstWidth, resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_popup_first_default_width));
        this.mPopupSecondTextHeight = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITouchSearchView_couiPopupWinSecondHeight, this.mPopupFirstTextHeight);
        this.mPopupSecondTextWidth = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITouchSearchView_couiPopupWinSecondWidth, this.mPopupFirstTextWidth);
        this.mSecondPopupOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITouchSearchView_couiPopupWinSecondOffset, resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_popupwin_default_offset));
        this.mSecondPopupMargin = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUITouchSearchView_couiPopupWinSecondMargin, resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_popupwin_second_marginEnd));
        this.mVibrateLevel = obtainStyledAttributes.getInteger(R$styleable.COUITouchSearchView_couiTouchSearchVibrateLevel, 0);
        this.mPopupWindowMinTop = obtainStyledAttributes.getInteger(R$styleable.COUITouchSearchView_couiPopupWinMinTop, resources.getInteger(R$integer.coui_touchsearch_popupwin_default_top_mincoordinate));
        this.mPopupSecondTextViewSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITouchSearchView_couiPopupWinSecondTextSize, context.getResources().getDimensionPixelSize(com.support.list.R$dimen.coui_touchsearch_popupwin_second_textsize));
        this.mPopupWinSecondNameMaxHeight = resources.getDimensionPixelSize(com.support.list.R$dimen.coui_touchsearch_popupname_max_height);
        this.mPopupWindowFirstKeyTextSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITouchSearchView_couiPopupWinFirstTextSize, resources.getDimensionPixelSize(com.support.list.R$dimen.coui_touchsearch_popupwin_first_textsize));
        this.mPopupWindowFirstTextColor = obtainStyledAttributes.getColor(R$styleable.COUITouchSearchView_couiPopupWinFirstTextColor, COUIContextUtil.a(context, R$attr.couiColorPrimaryNeutral));
        this.mBackgroundRightMargin += resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_right_margin);
        this.mPopupWindowEndMargin = resources.getDimensionPixelSize(com.support.list.R$dimen.coui_touchsearch_popupwin_right_margin);
        this.mItemSpacing = resources.getDimensionPixelSize(com.support.list.R$dimen.coui_touchsearch_item_spacing);
        this.mCellHeight = resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_each_item_height);
        this.mPopupWindowEndGap = resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_touch_end_gap);
        this.mMinHeight = this.mCellHeight * 5;
        this.mTouchPaddingStart = resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_touch_padding_start);
        this.mTouchPaddingEnd = resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_touch_padding_end);
        this.mPopupFirstWidth = resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_popup_first_layout_width);
        this.mDot = resources.getString(R$string.coui_touchsearch_dot);
        this.mKeyCollectDrawable = MaterialResources.e(context, obtainStyledAttributes, R$styleable.COUITouchSearchView_couiKeyCollect);
        this.mDefaultTextColor = MaterialResources.a(context, obtainStyledAttributes, R$styleable.COUITouchSearchView_couiKeyTextColor);
        this.mFirstIsCharacter = obtainStyledAttributes.getBoolean(R$styleable.COUITouchSearchView_couiFirstIsCharacter, false);
        this.mEnableAdaptiveVibrator = obtainStyledAttributes.getBoolean(R$styleable.COUITouchSearchView_couiAdaptiveVibrator, true);
        this.mHasMotorVibrator = VibrateUtils.h(context);
        this.mCOUITouchFirstPopTopBg = context.getDrawable(R$drawable.coui_touch_search_popup_bg);
        Drawable drawable = this.mKeyCollectDrawable;
        if (drawable != null) {
            this.mKeyDrawableWidth = drawable.getIntrinsicWidth();
            this.mKeyDrawableHeight = this.mKeyCollectDrawable.getIntrinsicHeight();
        }
        this.mDefaultTextSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUITouchSearchView_couiKeyTextSize, resources.getDimensionPixelSize(com.support.list.R$dimen.coui_touchsearch_key_textsize));
        this.mBackgroundWidth = resources.getDimensionPixelOffset(com.support.list.R$dimen.coui_touchsearch_background_width);
        if (!this.mFirstIsCharacter) {
            this.mKEYS = resources.getStringArray(R$array.normal_touchsearch_keys);
        } else {
            this.mKEYS = resources.getStringArray(R$array.special_touchsearch_keys);
        }
        TextPaint textPaint = new TextPaint(1);
        this.mMeasurePaint = textPaint;
        textPaint.setTextSize(this.mDefaultTextSize);
        initPopupWindow(context);
        obtainStyledAttributes.recycle();
        this.mFontFace = Typeface.create("sans-serif-medium", 0);
        initAccessibility(context);
    }
}
