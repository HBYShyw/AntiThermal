package com.oplus.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.widget.OplusViewExplorerByTouchHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusMenuView extends View {
    private static final int DISPLAYDSEN = 160;
    static final int MAX_MENU_ITEM_COUNTS = 5;
    private static final String TAG = "OplusMenuView";
    static final int VIEW_STATE_ENABLED = 16842910;
    static final int VIEW_STATE_PRESSED = 16842919;
    private int[] mBottom;
    private int mIconTextDis;
    private boolean mIsSelected;
    private int mItemCounts;
    private int mItemHeight;
    private int mItemWidth;
    private int[] mLeft;
    private int mNormalColor;
    private Runnable mOnclickRunnable;
    private List<OplusItem> mOplusItemList;
    private OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction mOplusViewTalkBalkInteraction;
    private int mPaddingLeft;
    private int mPaddingTop;
    private Paint mPaint;
    private int[] mRight;
    private float mScale;
    private Rect mSelectRect;
    private int mSelectedColor;
    private int mSelectedPosition;
    private float mTextSize;
    private int[] mTop;
    private final OplusViewExplorerByTouchHelper mTouchHelper;
    static final int[] STATE_ENABLED = {16842910};
    static final int[] STATE_UNENABLED = {-16842910};
    static final int[] STATE_PRESSED = {16842919, 16842910};
    static final int[] STATE_NORMAL = {-16842919, 16842910};
    private static int IETMNUMBERS = 5;

    public OplusMenuView(Context context) {
        this(context, null);
    }

    public OplusMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v37, types: [android.view.View$AccessibilityDelegate, com.android.internal.widget.OplusViewExplorerByTouchHelper] */
    public OplusMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mOplusItemList = new ArrayList();
        this.mItemCounts = 0;
        this.mSelectRect = new Rect();
        this.mIsSelected = false;
        this.mSelectedPosition = -1;
        this.mTextSize = 30.0f;
        this.mScale = 0.0f;
        this.mOnclickRunnable = new Runnable() { // from class: com.oplus.widget.OplusMenuView.1
            @Override // java.lang.Runnable
            public void run() {
            }
        };
        this.mOplusViewTalkBalkInteraction = new OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction() { // from class: com.oplus.widget.OplusMenuView.2
            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public void getItemBounds(int position, Rect rect) {
                OplusMenuView.this.getRect(position, rect);
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public void performAction(int virtualViewId, int actiontype, boolean resolvePara) {
                ((OplusItem) OplusMenuView.this.mOplusItemList.get(OplusMenuView.this.mSelectedPosition)).getOnItemClickListener().OnMenuItemClick(OplusMenuView.this.mSelectedPosition);
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public int getCurrentPosition() {
                return OplusMenuView.this.mSelectedPosition;
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public int getItemCounts() {
                return OplusMenuView.this.mOplusItemList.size();
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public int getVirtualViewAt(float x, float y) {
                return OplusMenuView.this.selectedIndex(x, y);
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public CharSequence getItemDescription(int virtualViewId) {
                return ((OplusItem) OplusMenuView.this.mOplusItemList.get(virtualViewId)).getText();
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public CharSequence getClassName() {
                return null;
            }

            @Override // com.android.internal.widget.OplusViewExplorerByTouchHelper.OplusViewTalkBalkInteraction
            public int getDisablePosition() {
                return -1;
            }
        };
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setTextAlign(Paint.Align.CENTER);
        this.mPaint.setAntiAlias(true);
        this.mSelectedColor = getResources().getColor(201719852);
        this.mNormalColor = getResources().getColor(201719853);
        this.mTextSize = (int) getResources().getDimension(201654319);
        this.mPaddingLeft = (int) getResources().getDimension(201654328);
        this.mPaddingTop = (int) getResources().getDimension(201654329);
        this.mItemHeight = (int) getResources().getDimension(201654330);
        this.mItemWidth = (int) getResources().getDimension(201654331);
        this.mIconTextDis = (int) getResources().getDimension(201654332);
        this.mPaint.setTextSize(this.mTextSize);
        this.mScale = context.getResources().getDisplayMetrics().densityDpi;
        ?? oplusViewExplorerByTouchHelper = new OplusViewExplorerByTouchHelper(this);
        this.mTouchHelper = oplusViewExplorerByTouchHelper;
        oplusViewExplorerByTouchHelper.setOplusViewTalkBalkInteraction(this.mOplusViewTalkBalkInteraction);
        setAccessibilityDelegate(oplusViewExplorerByTouchHelper);
        setImportantForAccessibility(1);
    }

    public void setItem(List<OplusItem> dl) {
        this.mOplusItemList = dl;
        int size = dl.size();
        if (size > 5) {
            this.mItemCounts = 5;
            this.mOplusItemList = this.mOplusItemList.subList(0, 5);
        } else {
            this.mItemCounts = size;
        }
        int i = 0;
        while (true) {
            int i2 = this.mItemCounts;
            if (i < i2) {
                initStateListDrawable(i);
                i++;
            } else {
                IETMNUMBERS = i2;
                this.mLeft = new int[i2];
                this.mTop = new int[i2];
                this.mRight = new int[i2];
                this.mBottom = new int[i2];
                this.mTouchHelper.invalidateRoot();
                return;
            }
        }
    }

    private void initStateListDrawable(int i) {
        Drawable drawable = this.mOplusItemList.get(i).getIcon();
        StateListDrawable statelistDrawable = new StateListDrawable();
        int[] iArr = STATE_PRESSED;
        drawable.setState(iArr);
        statelistDrawable.addState(iArr, drawable.getCurrent());
        int[] iArr2 = STATE_ENABLED;
        drawable.setState(iArr2);
        statelistDrawable.addState(iArr2, drawable.getCurrent());
        int[] iArr3 = STATE_UNENABLED;
        drawable.setState(iArr3);
        statelistDrawable.addState(iArr3, drawable.getCurrent());
        int[] iArr4 = STATE_NORMAL;
        drawable.setState(iArr4);
        statelistDrawable.addState(iArr4, drawable.getCurrent());
        this.mOplusItemList.get(i).setIcon(statelistDrawable);
        this.mOplusItemList.get(i).getIcon().setCallback(this);
        clearState();
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        Drawable drawable;
        int i = this.mSelectedPosition;
        if (i >= 0 && i < this.mItemCounts && (drawable = this.mOplusItemList.get(i).getIcon()) != null && drawable.isStateful()) {
            drawable.setState(getDrawableState());
        }
        super.drawableStateChanged();
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable who) {
        super.verifyDrawable(who);
        return true;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaddingLeft = (int) getResources().getDimension(201654328);
        if (IETMNUMBERS < 5) {
            this.mPaddingLeft = ((getWidth() / IETMNUMBERS) - this.mItemWidth) / 2;
        }
        for (int i = 0; i < this.mItemCounts; i++) {
            getRect(i, this.mSelectRect);
            OplusItem ci = this.mOplusItemList.get(i);
            ci.getIcon().setBounds(this.mSelectRect);
            ci.getIcon().draw(canvas);
            if (this.mSelectedPosition == i && this.mIsSelected) {
                this.mPaint.setColor(this.mSelectedColor);
            } else {
                this.mPaint.setColor(this.mNormalColor);
            }
            canvas.drawText(ci.getText(), this.mPaddingLeft + (this.mItemWidth / 2) + ((getWidth() / IETMNUMBERS) * i), this.mPaddingTop + this.mItemHeight + this.mIconTextDis + (this.mTextSize / 2.0f), this.mPaint);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getRect(int index, Rect rect) {
        this.mLeft[index] = this.mPaddingLeft + ((getWidth() / IETMNUMBERS) * index);
        int[] iArr = this.mTop;
        int i = this.mPaddingTop;
        iArr[index] = i;
        int[] iArr2 = this.mRight;
        int i2 = this.mItemWidth;
        int[] iArr3 = this.mLeft;
        iArr2[index] = i2 + iArr3[index];
        int[] iArr4 = this.mBottom;
        int i3 = i + this.mItemHeight;
        iArr4[index] = i3;
        rect.set(iArr3[index], iArr[index], iArr2[index], i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int selectedIndex(float eventX, float eventY) {
        int position = (int) (eventX / (getWidth() / IETMNUMBERS));
        return position < this.mItemCounts ? position : -2;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        event.getPointerCount();
        int eventY = (int) event.getY();
        int eventX = (int) event.getX();
        boolean inMenuView = false;
        if (eventY < this.mItemHeight * (this.mScale / 160.0f) && eventY > 0) {
            inMenuView = true;
        }
        switch (event.getAction()) {
            case 0:
                int i = this.mSelectedPosition;
                if (i >= 0 && eventX > this.mLeft[i] && eventX < this.mRight[i]) {
                    this.mIsSelected = true;
                }
                return true;
            case 1:
                int i2 = this.mSelectedPosition;
                if (i2 >= 0 && inMenuView) {
                    this.mOplusItemList.get(i2).getOnItemClickListener().OnMenuItemClick(this.mSelectedPosition);
                    this.mTouchHelper.sendEventForVirtualView(this.mSelectedPosition, 1);
                }
                clearState();
                return false;
            default:
                return true;
        }
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        int pointerCount = event.getPointerCount();
        if (pointerCount == 1 && eventY >= 0.0f) {
            switch (event.getAction()) {
                case 0:
                    int selectedIndex = selectedIndex(event.getX(), event.getY());
                    this.mSelectedPosition = selectedIndex;
                    if (selectedIndex < 0 || eventX <= this.mLeft[selectedIndex] || eventX >= this.mRight[selectedIndex]) {
                        this.mSelectedPosition = -1;
                        break;
                    }
                    break;
            }
        } else {
            clearState();
        }
        return super.dispatchTouchEvent(event);
    }

    private void clearState() {
        for (OplusItem ci : this.mOplusItemList) {
            Drawable d = ci.getIcon();
            if (d != null && d.isStateful()) {
                d.setState(STATE_NORMAL);
            }
        }
        this.mIsSelected = false;
        this.mSelectedPosition = -1;
        invalidate();
    }

    public void clearAccessibilityFocus() {
        OplusViewExplorerByTouchHelper oplusViewExplorerByTouchHelper = this.mTouchHelper;
        if (oplusViewExplorerByTouchHelper != null) {
            oplusViewExplorerByTouchHelper.clearFocusedVirtualView();
        }
    }

    boolean restoreAccessibilityFocus(int position) {
        if (position < 0 || position >= this.mItemCounts) {
            return false;
        }
        OplusViewExplorerByTouchHelper oplusViewExplorerByTouchHelper = this.mTouchHelper;
        if (oplusViewExplorerByTouchHelper != null) {
            oplusViewExplorerByTouchHelper.setFocusedVirtualView(position);
            return true;
        }
        return true;
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent event) {
        if (this.mTouchHelper.dispatchHoverEvent(event)) {
            return true;
        }
        return super.dispatchHoverEvent(event);
    }
}
