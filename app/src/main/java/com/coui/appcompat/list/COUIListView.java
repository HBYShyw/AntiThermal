package com.coui.appcompat.list;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$styleable;
import q2.COUIScrollBar;

/* loaded from: classes.dex */
public class COUIListView extends ListView implements COUIScrollBar.c {
    private static final int INVALID_SCROLL_CHOICE_POSITION = -2;
    private static final int SCROLLBARS_NONE = 0;
    private static final int SCROLLBARS_VERTICAL = 512;
    private static final long SCROLL_CHOICE_SCROLL_DELAY = 50;
    private static final String TAG = "COUIListView";
    private COUIScrollBar mCOUIScrollBar;
    private int mCheckItemId;
    private Runnable mDelayedScroll;
    private boolean mFlag;
    private int mLastPosition;
    private int mLastSite;
    private int mLasterPosition;
    private int mLeftOffset;
    private boolean mMultiChoice;
    private int mRightOffset;
    private b mScrollMultiChoiceListener;
    private Drawable mScrollbarThumbVertical;
    private int mScrollbars;
    private int mScrollbarsSize;
    private int mStyle;
    private boolean mUpScroll;

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIListView.this.mUpScroll) {
                COUIListView.this.setSelectionFromTop(r0.getFirstVisiblePosition() - 1, -COUIListView.this.getPaddingTop());
            } else {
                COUIListView cOUIListView = COUIListView.this;
                cOUIListView.alignBottomChild(cOUIListView.getLastVisiblePosition() + 1, COUIListView.this.getPaddingBottom());
            }
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        void a(int i10, View view);
    }

    public COUIListView(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alignBottomChild(int i10, int i11) {
        setSelectionFromTop(i10, (((getHeight() - getPaddingTop()) - getPaddingBottom()) - getChildAt(getChildCount() - 1).getHeight()) + i11);
    }

    private void createCOUIScrollDelegate(Context context) {
        this.mCOUIScrollBar = new COUIScrollBar.b(this).a();
    }

    private void initAttr(Context context, AttributeSet attributeSet, int i10) {
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.mStyle = attributeSet.getStyleAttribute();
        } else {
            this.mStyle = i10;
        }
        if (context != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIListView, i10, 0);
            this.mScrollbars = obtainStyledAttributes.getInteger(R$styleable.COUIListView_couiScrollbars, 0);
            this.mScrollbarsSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIListView_couiScrollbarSize, 0);
            this.mScrollbarThumbVertical = obtainStyledAttributes.getDrawable(R$styleable.COUIListView_couiScrollbarThumbVertical);
            obtainStyledAttributes.recycle();
        }
    }

    private boolean isInScrollRange(MotionEvent motionEvent) {
        int pointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
        int rawX = (int) motionEvent.getRawX();
        int[] iArr = new int[2];
        try {
            if (this.mCheckItemId > 0) {
                CheckBox checkBox = (CheckBox) getChildAt(pointToPosition - getFirstVisiblePosition()).findViewById(this.mCheckItemId);
                checkBox.getLocationOnScreen(iArr);
                int i10 = iArr[0] - this.mLeftOffset;
                int i11 = iArr[0] + this.mRightOffset;
                if (checkBox.getVisibility() == 0 && rawX > i10 && rawX < i11 && pointToPosition > getHeaderViewsCount() - 1 && pointToPosition < getCount() - getFooterViewsCount()) {
                    this.mMultiChoice = true;
                    return true;
                }
                if (motionEvent.getActionMasked() == 0) {
                    this.mMultiChoice = false;
                }
                return false;
            }
            this.mMultiChoice = false;
            return false;
        } catch (Exception unused) {
            if (motionEvent.getActionMasked() == 0) {
                this.mMultiChoice = false;
            }
            return false;
        }
    }

    @Override // android.view.View
    protected boolean awakenScrollBars() {
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        return cOUIScrollBar != null ? cOUIScrollBar.c() : super.awakenScrollBars();
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.e(canvas);
        }
    }

    public COUIScrollBar getCOUIScrollDelegate() {
        return this.mCOUIScrollBar;
    }

    @Override // q2.COUIScrollBar.c
    public View getCOUIScrollableView() {
        return this;
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.h();
        }
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.q();
            this.mCOUIScrollBar = null;
        }
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null && cOUIScrollBar.j(motionEvent)) {
            return true;
        }
        if ((motionEvent.getAction() & 255) == 0 && isInScrollRange(motionEvent)) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0030, code lost:
    
        if (r5 != 2) goto L45;
     */
    @Override // android.widget.AbsListView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null && cOUIScrollBar.l(motionEvent)) {
            return true;
        }
        if (this.mMultiChoice && isInScrollRange(motionEvent)) {
            int pointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                this.mFlag = true;
            } else if (actionMasked == 1) {
                this.mLastPosition = -2;
                this.mLasterPosition = -2;
            }
            if (pointToPosition == getCount() - 1) {
                alignBottomChild(pointToPosition, 0);
            }
            if (this.mFlag && this.mLastPosition != pointToPosition && pointToPosition != -1 && this.mScrollMultiChoiceListener != null) {
                removeCallbacks(this.mDelayedScroll);
                this.mScrollMultiChoiceListener.a(pointToPosition, getChildAt(pointToPosition - getFirstVisiblePosition()));
                if (this.mLastPosition != -2) {
                    if (pointToPosition == getFirstVisiblePosition() && pointToPosition > 0) {
                        this.mUpScroll = true;
                        postDelayed(this.mDelayedScroll, SCROLL_CHOICE_SCROLL_DELAY);
                    } else if (pointToPosition == getLastVisiblePosition() && pointToPosition < getCount()) {
                        this.mUpScroll = false;
                        postDelayed(this.mDelayedScroll, SCROLL_CHOICE_SCROLL_DELAY);
                    }
                }
                if (this.mLasterPosition == pointToPosition) {
                    b bVar = this.mScrollMultiChoiceListener;
                    int i10 = this.mLastPosition;
                    bVar.a(i10, getChildAt(i10 - getFirstVisiblePosition()));
                }
                this.mLasterPosition = this.mLastPosition;
                this.mLastPosition = pointToPosition;
            }
            return true;
        }
        int action = motionEvent.getAction() & 255;
        if (action == 1 || action == 3) {
            this.mUpScroll = true;
            this.mLastPosition = -2;
            this.mLasterPosition = -2;
            this.mFlag = false;
            this.mMultiChoice = true;
            this.mLastSite = -1;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        super.onVisibilityChanged(view, i10);
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.n(view, i10);
        }
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i10) {
        super.onWindowVisibilityChanged(i10);
        COUIScrollBar cOUIScrollBar = this.mCOUIScrollBar;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.o(i10);
        }
    }

    public void refresh() {
        String resourceTypeName = getResources().getResourceTypeName(this.mStyle);
        TypedArray typedArray = null;
        if ("attr".equals(resourceTypeName)) {
            typedArray = getContext().obtainStyledAttributes(null, R$styleable.COUIListView, this.mStyle, 0);
        } else if ("style".equals(resourceTypeName)) {
            typedArray = getContext().obtainStyledAttributes(null, R$styleable.COUIListView, 0, this.mStyle);
        }
        if (typedArray != null) {
            this.mScrollbarThumbVertical = typedArray.getDrawable(R$styleable.COUIListView_couiScrollbarThumbVertical);
            typedArray.recycle();
        }
        if (this.mScrollbars == 512) {
            Drawable drawable = this.mScrollbarThumbVertical;
            if (drawable != null) {
                this.mCOUIScrollBar.s(drawable);
            } else {
                this.mCOUIScrollBar.p();
            }
        }
        invalidate();
    }

    public void setCheckItemId(int i10) {
        this.mCheckItemId = i10;
    }

    public void setNewCOUIScrollDelegate(COUIScrollBar cOUIScrollBar) {
        if (cOUIScrollBar != null) {
            this.mCOUIScrollBar = cOUIScrollBar;
            cOUIScrollBar.h();
            return;
        }
        throw new IllegalArgumentException("setNewFastScrollDelegate must NOT be NULL.");
    }

    public void setScrollMultiChoiceListener(b bVar) {
        this.mScrollMultiChoiceListener = bVar;
    }

    @Override // q2.COUIScrollBar.c
    public int superComputeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override // q2.COUIScrollBar.c
    public int superComputeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override // q2.COUIScrollBar.c
    public int superComputeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override // q2.COUIScrollBar.c
    public void superOnTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
    }

    public COUIListView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.listViewStyle);
    }

    public COUIListView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.mMultiChoice = true;
        this.mLastPosition = -2;
        this.mLasterPosition = -2;
        this.mFlag = false;
        this.mUpScroll = true;
        this.mLastSite = -1;
        this.mCheckItemId = -1;
        this.mScrollbars = 0;
        this.mDelayedScroll = new a();
        initAttr(context, attributeSet, i10);
        if (this.mScrollbars == 512) {
            createCOUIScrollDelegate(context);
            int i11 = this.mScrollbarsSize;
            if (i11 != 0) {
                this.mCOUIScrollBar.t(i11);
            }
            Drawable drawable = this.mScrollbarThumbVertical;
            if (drawable != null) {
                this.mCOUIScrollBar.s(drawable);
            }
        }
        this.mLeftOffset = getResources().getDimensionPixelOffset(R$dimen.coui_listview_scrollchoice_left_offset);
        this.mRightOffset = getResources().getDimensionPixelOffset(R$dimen.coui_listview_scrollchoice_right_offset);
    }
}
