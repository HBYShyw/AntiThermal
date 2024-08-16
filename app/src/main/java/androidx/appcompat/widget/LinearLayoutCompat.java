package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.appcompat.R$styleable;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class LinearLayoutCompat extends ViewGroup {
    private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.widget.LinearLayoutCompat";
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    /* loaded from: classes.dex */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    private void forceUniformHeight(int i10, int i11) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i12 = 0; i12 < i10; i12++) {
            View virtualChildAt = getVirtualChildAt(i12);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (((LinearLayout.LayoutParams) layoutParams).height == -1) {
                    int i13 = ((LinearLayout.LayoutParams) layoutParams).width;
                    ((LinearLayout.LayoutParams) layoutParams).width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i11, 0, makeMeasureSpec, 0);
                    ((LinearLayout.LayoutParams) layoutParams).width = i13;
                }
            }
        }
    }

    private void forceUniformWidth(int i10, int i11) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i12 = 0; i12 < i10; i12++) {
            View virtualChildAt = getVirtualChildAt(i12);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (((LinearLayout.LayoutParams) layoutParams).width == -1) {
                    int i13 = ((LinearLayout.LayoutParams) layoutParams).height;
                    ((LinearLayout.LayoutParams) layoutParams).height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i11, 0);
                    ((LinearLayout.LayoutParams) layoutParams).height = i13;
                }
            }
        }
    }

    private void setChildFrame(View view, int i10, int i11, int i12, int i13) {
        view.layout(i10, i11, i12 + i10, i13 + i11);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    void drawDividersHorizontal(Canvas canvas) {
        int right;
        int left;
        int i10;
        int left2;
        int virtualChildCount = getVirtualChildCount();
        boolean b10 = n0.b(this);
        for (int i11 = 0; i11 < virtualChildCount; i11++) {
            View virtualChildAt = getVirtualChildAt(i11);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i11)) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (b10) {
                    left2 = virtualChildAt.getRight() + ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                } else {
                    left2 = (virtualChildAt.getLeft() - ((LinearLayout.LayoutParams) layoutParams).leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, left2);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (b10) {
                    left = virtualChildAt2.getLeft() - ((LinearLayout.LayoutParams) layoutParams2).leftMargin;
                    i10 = this.mDividerWidth;
                    right = left - i10;
                } else {
                    right = virtualChildAt2.getRight() + ((LinearLayout.LayoutParams) layoutParams2).rightMargin;
                }
            } else if (b10) {
                right = getPaddingLeft();
            } else {
                left = getWidth() - getPaddingRight();
                i10 = this.mDividerWidth;
                right = left - i10;
            }
            drawVerticalDivider(canvas, right);
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int bottom;
        int virtualChildCount = getVirtualChildCount();
        for (int i10 = 0; i10 < virtualChildCount; i10++) {
            View virtualChildAt = getVirtualChildAt(i10);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i10)) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LinearLayout.LayoutParams) ((LayoutParams) virtualChildAt.getLayoutParams())).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                bottom = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                bottom = virtualChildAt2.getBottom() + ((LinearLayout.LayoutParams) ((LayoutParams) virtualChildAt2.getLayoutParams())).bottomMargin;
            }
            drawHorizontalDivider(canvas, bottom);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int i10) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i10, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i10);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int i10) {
        this.mDivider.setBounds(i10, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i10, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    @Override // android.view.View
    public int getBaseline() {
        int i10;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i11 = this.mBaselineAlignedChildIndex;
        if (childCount > i11) {
            View childAt = getChildAt(i11);
            int baseline = childAt.getBaseline();
            if (baseline == -1) {
                if (this.mBaselineAlignedChildIndex == 0) {
                    return -1;
                }
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
            int i12 = this.mBaselineChildTop;
            if (this.mOrientation == 1 && (i10 = this.mGravity & 112) != 48) {
                if (i10 == 16) {
                    i12 += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                } else if (i10 == 80) {
                    i12 = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                }
            }
            return i12 + ((LinearLayout.LayoutParams) ((LayoutParams) childAt.getLayoutParams())).topMargin + baseline;
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    int getChildrenSkipCount(View view, int i10) {
        return 0;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public int getGravity() {
        return this.mGravity;
    }

    int getLocationOffset(View view) {
        return 0;
    }

    int getNextLocationOffset(View view) {
        return 0;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    View getVirtualChildAt(int i10) {
        return getChildAt(i10);
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int i10) {
        if (i10 == 0) {
            return (this.mShowDividers & 1) != 0;
        }
        if (i10 == getChildCount()) {
            return (this.mShowDividers & 4) != 0;
        }
        if ((this.mShowDividers & 2) == 0) {
            return false;
        }
        for (int i11 = i10 - 1; i11 >= 0; i11--) {
            if (getChildAt(i11).getVisibility() != 8) {
                return true;
            }
        }
        return false;
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00eb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutHorizontal(int i10, int i11, int i12, int i13) {
        int paddingLeft;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        boolean z10;
        int i19;
        int i20;
        int i21;
        int i22;
        boolean b10 = n0.b(this);
        int paddingTop = getPaddingTop();
        int i23 = i13 - i11;
        int paddingBottom = i23 - getPaddingBottom();
        int paddingBottom2 = (i23 - paddingTop) - getPaddingBottom();
        int virtualChildCount = getVirtualChildCount();
        int i24 = this.mGravity;
        int i25 = i24 & 112;
        boolean z11 = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int b11 = GravityCompat.b(8388615 & i24, ViewCompat.x(this));
        boolean z12 = true;
        if (b11 == 1) {
            paddingLeft = getPaddingLeft() + (((i12 - i10) - this.mTotalLength) / 2);
        } else if (b11 != 5) {
            paddingLeft = getPaddingLeft();
        } else {
            paddingLeft = ((getPaddingLeft() + i12) - i10) - this.mTotalLength;
        }
        if (b10) {
            i14 = virtualChildCount - 1;
            i15 = -1;
        } else {
            i14 = 0;
            i15 = 1;
        }
        int i26 = 0;
        while (i26 < virtualChildCount) {
            int i27 = i14 + (i15 * i26);
            View virtualChildAt = getVirtualChildAt(i27);
            if (virtualChildAt == null) {
                paddingLeft += measureNullChild(i27);
                z10 = z12;
                i16 = paddingTop;
                i17 = virtualChildCount;
                i18 = i25;
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i28 = i26;
                if (z11) {
                    i17 = virtualChildCount;
                    if (((LinearLayout.LayoutParams) layoutParams).height != -1) {
                        i19 = virtualChildAt.getBaseline();
                        i20 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                        if (i20 < 0) {
                            i20 = i25;
                        }
                        i21 = i20 & 112;
                        i18 = i25;
                        if (i21 == 16) {
                            if (i21 == 48) {
                                i22 = ((LinearLayout.LayoutParams) layoutParams).topMargin + paddingTop;
                                if (i19 != -1) {
                                    z10 = true;
                                    i22 += iArr[1] - i19;
                                }
                            } else if (i21 != 80) {
                                i22 = paddingTop;
                            } else {
                                i22 = (paddingBottom - measuredHeight) - ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                                if (i19 != -1) {
                                    i22 -= iArr2[2] - (virtualChildAt.getMeasuredHeight() - i19);
                                }
                            }
                            z10 = true;
                        } else {
                            z10 = true;
                            i22 = ((((paddingBottom2 - measuredHeight) / 2) + paddingTop) + ((LinearLayout.LayoutParams) layoutParams).topMargin) - ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                        }
                        if (hasDividerBeforeChildAt(i27)) {
                            paddingLeft += this.mDividerWidth;
                        }
                        int i29 = ((LinearLayout.LayoutParams) layoutParams).leftMargin + paddingLeft;
                        i16 = paddingTop;
                        setChildFrame(virtualChildAt, i29 + getLocationOffset(virtualChildAt), i22, measuredWidth, measuredHeight);
                        int nextLocationOffset = i29 + measuredWidth + ((LinearLayout.LayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                        i26 = i28 + getChildrenSkipCount(virtualChildAt, i27);
                        paddingLeft = nextLocationOffset;
                        i26++;
                        virtualChildCount = i17;
                        i25 = i18;
                        z12 = z10;
                        paddingTop = i16;
                    }
                } else {
                    i17 = virtualChildCount;
                }
                i19 = -1;
                i20 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                if (i20 < 0) {
                }
                i21 = i20 & 112;
                i18 = i25;
                if (i21 == 16) {
                }
                if (hasDividerBeforeChildAt(i27)) {
                }
                int i292 = ((LinearLayout.LayoutParams) layoutParams).leftMargin + paddingLeft;
                i16 = paddingTop;
                setChildFrame(virtualChildAt, i292 + getLocationOffset(virtualChildAt), i22, measuredWidth, measuredHeight);
                int nextLocationOffset2 = i292 + measuredWidth + ((LinearLayout.LayoutParams) layoutParams).rightMargin + getNextLocationOffset(virtualChildAt);
                i26 = i28 + getChildrenSkipCount(virtualChildAt, i27);
                paddingLeft = nextLocationOffset2;
                i26++;
                virtualChildCount = i17;
                i25 = i18;
                z12 = z10;
                paddingTop = i16;
            } else {
                i16 = paddingTop;
                i17 = virtualChildCount;
                i18 = i25;
                z10 = true;
            }
            i26++;
            virtualChildCount = i17;
            i25 = i18;
            z12 = z10;
            paddingTop = i16;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x009f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutVertical(int i10, int i11, int i12, int i13) {
        int paddingTop;
        int i14;
        int i15;
        int i16;
        int i17;
        int paddingLeft = getPaddingLeft();
        int i18 = i12 - i10;
        int paddingRight = i18 - getPaddingRight();
        int paddingRight2 = (i18 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i19 = this.mGravity;
        int i20 = i19 & 112;
        int i21 = i19 & 8388615;
        if (i20 == 16) {
            paddingTop = getPaddingTop() + (((i13 - i11) - this.mTotalLength) / 2);
        } else if (i20 != 80) {
            paddingTop = getPaddingTop();
        } else {
            paddingTop = ((getPaddingTop() + i13) - i11) - this.mTotalLength;
        }
        int i22 = 0;
        while (i22 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i22);
            if (virtualChildAt == null) {
                paddingTop += measureNullChild(i22);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i23 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                if (i23 < 0) {
                    i23 = i21;
                }
                int b10 = GravityCompat.b(i23, ViewCompat.x(this)) & 7;
                if (b10 == 1) {
                    i14 = ((paddingRight2 - measuredWidth) / 2) + paddingLeft + ((LinearLayout.LayoutParams) layoutParams).leftMargin;
                    i15 = ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                } else if (b10 != 5) {
                    i16 = ((LinearLayout.LayoutParams) layoutParams).leftMargin + paddingLeft;
                    int i24 = i16;
                    if (hasDividerBeforeChildAt(i22)) {
                        paddingTop += this.mDividerHeight;
                    }
                    int i25 = paddingTop + ((LinearLayout.LayoutParams) layoutParams).topMargin;
                    setChildFrame(virtualChildAt, i24, i25 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                    int nextLocationOffset = i25 + measuredHeight + ((LinearLayout.LayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                    i22 += getChildrenSkipCount(virtualChildAt, i22);
                    paddingTop = nextLocationOffset;
                    i17 = 1;
                    i22 += i17;
                } else {
                    i14 = paddingRight - measuredWidth;
                    i15 = ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                }
                i16 = i14 - i15;
                int i242 = i16;
                if (hasDividerBeforeChildAt(i22)) {
                }
                int i252 = paddingTop + ((LinearLayout.LayoutParams) layoutParams).topMargin;
                setChildFrame(virtualChildAt, i242, i252 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                int nextLocationOffset2 = i252 + measuredHeight + ((LinearLayout.LayoutParams) layoutParams).bottomMargin + getNextLocationOffset(virtualChildAt);
                i22 += getChildrenSkipCount(virtualChildAt, i22);
                paddingTop = nextLocationOffset2;
                i17 = 1;
                i22 += i17;
            }
            i17 = 1;
            i22 += i17;
        }
    }

    void measureChildBeforeLayout(View view, int i10, int i11, int i12, int i13, int i14) {
        measureChildWithMargins(view, i11, i12, i13, i14);
    }

    /* JADX WARN: Removed duplicated region for block: B:195:0x0453  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0199  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void measureHorizontal(int i10, int i11) {
        int[] iArr;
        int i12;
        int max;
        int i13;
        int i14;
        int max2;
        int i15;
        int i16;
        int i17;
        float f10;
        int i18;
        boolean z10;
        int baseline;
        int i19;
        int i20;
        int i21;
        char c10;
        int i22;
        int i23;
        boolean z11;
        boolean z12;
        View view;
        int i24;
        boolean z13;
        int measuredHeight;
        int childrenSkipCount;
        int baseline2;
        int i25;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = View.MeasureSpec.getMode(i10);
        int mode2 = View.MeasureSpec.getMode(i11);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] iArr2 = this.mMaxAscent;
        int[] iArr3 = this.mMaxDescent;
        iArr2[3] = -1;
        iArr2[2] = -1;
        iArr2[1] = -1;
        iArr2[0] = -1;
        iArr3[3] = -1;
        iArr3[2] = -1;
        iArr3[1] = -1;
        iArr3[0] = -1;
        boolean z14 = this.mBaselineAligned;
        boolean z15 = this.mUseLargestChild;
        int i26 = 1073741824;
        boolean z16 = mode == 1073741824;
        int i27 = 0;
        int i28 = 0;
        int i29 = 0;
        int i30 = 0;
        int i31 = 0;
        boolean z17 = false;
        int i32 = 0;
        boolean z18 = false;
        boolean z19 = true;
        float f11 = 0.0f;
        while (true) {
            iArr = iArr3;
            if (i27 >= virtualChildCount) {
                break;
            }
            View virtualChildAt = getVirtualChildAt(i27);
            if (virtualChildAt == null) {
                this.mTotalLength += measureNullChild(i27);
            } else if (virtualChildAt.getVisibility() == 8) {
                i27 += getChildrenSkipCount(virtualChildAt, i27);
            } else {
                if (hasDividerBeforeChildAt(i27)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                float f12 = ((LinearLayout.LayoutParams) layoutParams).weight;
                float f13 = f11 + f12;
                if (mode == i26 && ((LinearLayout.LayoutParams) layoutParams).width == 0 && f12 > 0.0f) {
                    if (z16) {
                        this.mTotalLength += ((LinearLayout.LayoutParams) layoutParams).leftMargin + ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                    } else {
                        int i33 = this.mTotalLength;
                        this.mTotalLength = Math.max(i33, ((LinearLayout.LayoutParams) layoutParams).leftMargin + i33 + ((LinearLayout.LayoutParams) layoutParams).rightMargin);
                    }
                    if (z14) {
                        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChildAt.measure(makeMeasureSpec, makeMeasureSpec);
                        i23 = i27;
                        z11 = z15;
                        z12 = z14;
                        view = virtualChildAt;
                    } else {
                        i23 = i27;
                        z11 = z15;
                        z12 = z14;
                        view = virtualChildAt;
                        z17 = true;
                        i24 = 1073741824;
                        if (mode2 == i24 && ((LinearLayout.LayoutParams) layoutParams).height == -1) {
                            z13 = true;
                            z18 = true;
                        } else {
                            z13 = false;
                        }
                        int i34 = ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                        measuredHeight = view.getMeasuredHeight() + i34;
                        i32 = View.combineMeasuredStates(i32, view.getMeasuredState());
                        if (z12 && (baseline2 = view.getBaseline()) != -1) {
                            i25 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                            if (i25 < 0) {
                                i25 = this.mGravity;
                            }
                            int i35 = (((i25 & 112) >> 4) & (-2)) >> 1;
                            iArr2[i35] = Math.max(iArr2[i35], baseline2);
                            iArr[i35] = Math.max(iArr[i35], measuredHeight - baseline2);
                        }
                        i29 = Math.max(i29, measuredHeight);
                        z19 = !z19 && ((LinearLayout.LayoutParams) layoutParams).height == -1;
                        if (((LinearLayout.LayoutParams) layoutParams).weight <= 0.0f) {
                            if (!z13) {
                                i34 = measuredHeight;
                            }
                            i31 = Math.max(i31, i34);
                        } else {
                            int i36 = i31;
                            if (!z13) {
                                i34 = measuredHeight;
                            }
                            i30 = Math.max(i30, i34);
                            i31 = i36;
                        }
                        int i37 = i23;
                        childrenSkipCount = getChildrenSkipCount(view, i37) + i37;
                        f11 = f13;
                        int i38 = childrenSkipCount + 1;
                        iArr3 = iArr;
                        z15 = z11;
                        z14 = z12;
                        i26 = i24;
                        i27 = i38;
                    }
                } else {
                    if (((LinearLayout.LayoutParams) layoutParams).width != 0 || f12 <= 0.0f) {
                        c10 = 65534;
                        i22 = Integer.MIN_VALUE;
                    } else {
                        c10 = 65534;
                        ((LinearLayout.LayoutParams) layoutParams).width = -2;
                        i22 = 0;
                    }
                    i23 = i27;
                    int i39 = i22;
                    z11 = z15;
                    z12 = z14;
                    measureChildBeforeLayout(virtualChildAt, i23, i10, f13 == 0.0f ? this.mTotalLength : 0, i11, 0);
                    if (i39 != Integer.MIN_VALUE) {
                        ((LinearLayout.LayoutParams) layoutParams).width = i39;
                    }
                    int measuredWidth = virtualChildAt.getMeasuredWidth();
                    if (z16) {
                        view = virtualChildAt;
                        this.mTotalLength += ((LinearLayout.LayoutParams) layoutParams).leftMargin + measuredWidth + ((LinearLayout.LayoutParams) layoutParams).rightMargin + getNextLocationOffset(view);
                    } else {
                        view = virtualChildAt;
                        int i40 = this.mTotalLength;
                        this.mTotalLength = Math.max(i40, i40 + measuredWidth + ((LinearLayout.LayoutParams) layoutParams).leftMargin + ((LinearLayout.LayoutParams) layoutParams).rightMargin + getNextLocationOffset(view));
                    }
                    if (z11) {
                        i28 = Math.max(measuredWidth, i28);
                    }
                }
                i24 = 1073741824;
                if (mode2 == i24) {
                }
                z13 = false;
                int i342 = ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                measuredHeight = view.getMeasuredHeight() + i342;
                i32 = View.combineMeasuredStates(i32, view.getMeasuredState());
                if (z12) {
                    i25 = ((LinearLayout.LayoutParams) layoutParams).gravity;
                    if (i25 < 0) {
                    }
                    int i352 = (((i25 & 112) >> 4) & (-2)) >> 1;
                    iArr2[i352] = Math.max(iArr2[i352], baseline2);
                    iArr[i352] = Math.max(iArr[i352], measuredHeight - baseline2);
                }
                i29 = Math.max(i29, measuredHeight);
                if (z19) {
                }
                if (((LinearLayout.LayoutParams) layoutParams).weight <= 0.0f) {
                }
                int i372 = i23;
                childrenSkipCount = getChildrenSkipCount(view, i372) + i372;
                f11 = f13;
                int i382 = childrenSkipCount + 1;
                iArr3 = iArr;
                z15 = z11;
                z14 = z12;
                i26 = i24;
                i27 = i382;
            }
            z11 = z15;
            z12 = z14;
            int i41 = i26;
            childrenSkipCount = i27;
            i24 = i41;
            int i3822 = childrenSkipCount + 1;
            iArr3 = iArr;
            z15 = z11;
            z14 = z12;
            i26 = i24;
            i27 = i3822;
        }
        boolean z20 = z15;
        boolean z21 = z14;
        int i42 = i29;
        int i43 = i30;
        int i44 = i31;
        int i45 = i32;
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
            max = i42;
            i12 = i45;
        } else {
            i12 = i45;
            max = Math.max(i42, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
        }
        if (z20 && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            int i46 = 0;
            while (i46 < virtualChildCount) {
                View virtualChildAt2 = getVirtualChildAt(i46);
                if (virtualChildAt2 == null) {
                    this.mTotalLength += measureNullChild(i46);
                } else if (virtualChildAt2.getVisibility() == 8) {
                    i46 += getChildrenSkipCount(virtualChildAt2, i46);
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                    if (z16) {
                        this.mTotalLength += ((LinearLayout.LayoutParams) layoutParams2).leftMargin + i28 + ((LinearLayout.LayoutParams) layoutParams2).rightMargin + getNextLocationOffset(virtualChildAt2);
                    } else {
                        int i47 = this.mTotalLength;
                        i21 = max;
                        this.mTotalLength = Math.max(i47, i47 + i28 + ((LinearLayout.LayoutParams) layoutParams2).leftMargin + ((LinearLayout.LayoutParams) layoutParams2).rightMargin + getNextLocationOffset(virtualChildAt2));
                        i46++;
                        max = i21;
                    }
                }
                i21 = max;
                i46++;
                max = i21;
            }
        }
        int i48 = max;
        int paddingLeft = this.mTotalLength + getPaddingLeft() + getPaddingRight();
        this.mTotalLength = paddingLeft;
        int resolveSizeAndState = View.resolveSizeAndState(Math.max(paddingLeft, getSuggestedMinimumWidth()), i10, 0);
        int i49 = (16777215 & resolveSizeAndState) - this.mTotalLength;
        if (!z17 && (i49 == 0 || f11 <= 0.0f)) {
            i15 = Math.max(i43, i44);
            if (z20 && mode != 1073741824) {
                for (int i50 = 0; i50 < virtualChildCount; i50++) {
                    View virtualChildAt3 = getVirtualChildAt(i50);
                    if (virtualChildAt3 != null && virtualChildAt3.getVisibility() != 8 && ((LinearLayout.LayoutParams) ((LayoutParams) virtualChildAt3.getLayoutParams())).weight > 0.0f) {
                        virtualChildAt3.measure(View.MeasureSpec.makeMeasureSpec(i28, 1073741824), View.MeasureSpec.makeMeasureSpec(virtualChildAt3.getMeasuredHeight(), 1073741824));
                    }
                }
            }
            i13 = i11;
            i14 = virtualChildCount;
            max2 = i48;
        } else {
            float f14 = this.mWeightSum;
            if (f14 > 0.0f) {
                f11 = f14;
            }
            iArr2[3] = -1;
            iArr2[2] = -1;
            iArr2[1] = -1;
            iArr2[0] = -1;
            iArr[3] = -1;
            iArr[2] = -1;
            iArr[1] = -1;
            iArr[0] = -1;
            this.mTotalLength = 0;
            int i51 = i43;
            int i52 = -1;
            int i53 = i12;
            int i54 = 0;
            while (i54 < virtualChildCount) {
                View virtualChildAt4 = getVirtualChildAt(i54);
                if (virtualChildAt4 == null || virtualChildAt4.getVisibility() == 8) {
                    i16 = i49;
                    i17 = virtualChildCount;
                } else {
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt4.getLayoutParams();
                    float f15 = ((LinearLayout.LayoutParams) layoutParams3).weight;
                    if (f15 > 0.0f) {
                        int i55 = (int) ((i49 * f15) / f11);
                        float f16 = f11 - f15;
                        int i56 = i49 - i55;
                        i17 = virtualChildCount;
                        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i11, getPaddingTop() + getPaddingBottom() + ((LinearLayout.LayoutParams) layoutParams3).topMargin + ((LinearLayout.LayoutParams) layoutParams3).bottomMargin, ((LinearLayout.LayoutParams) layoutParams3).height);
                        if (((LinearLayout.LayoutParams) layoutParams3).width == 0) {
                            i20 = 1073741824;
                            if (mode == 1073741824) {
                                if (i55 <= 0) {
                                    i55 = 0;
                                }
                                virtualChildAt4.measure(View.MeasureSpec.makeMeasureSpec(i55, 1073741824), childMeasureSpec);
                                i53 = View.combineMeasuredStates(i53, virtualChildAt4.getMeasuredState() & (-16777216));
                                f11 = f16;
                                i16 = i56;
                            }
                        } else {
                            i20 = 1073741824;
                        }
                        int measuredWidth2 = virtualChildAt4.getMeasuredWidth() + i55;
                        if (measuredWidth2 < 0) {
                            measuredWidth2 = 0;
                        }
                        virtualChildAt4.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth2, i20), childMeasureSpec);
                        i53 = View.combineMeasuredStates(i53, virtualChildAt4.getMeasuredState() & (-16777216));
                        f11 = f16;
                        i16 = i56;
                    } else {
                        i16 = i49;
                        i17 = virtualChildCount;
                    }
                    if (z16) {
                        this.mTotalLength += virtualChildAt4.getMeasuredWidth() + ((LinearLayout.LayoutParams) layoutParams3).leftMargin + ((LinearLayout.LayoutParams) layoutParams3).rightMargin + getNextLocationOffset(virtualChildAt4);
                        f10 = f11;
                    } else {
                        int i57 = this.mTotalLength;
                        f10 = f11;
                        this.mTotalLength = Math.max(i57, virtualChildAt4.getMeasuredWidth() + i57 + ((LinearLayout.LayoutParams) layoutParams3).leftMargin + ((LinearLayout.LayoutParams) layoutParams3).rightMargin + getNextLocationOffset(virtualChildAt4));
                    }
                    boolean z22 = mode2 != 1073741824 && ((LinearLayout.LayoutParams) layoutParams3).height == -1;
                    int i58 = ((LinearLayout.LayoutParams) layoutParams3).topMargin + ((LinearLayout.LayoutParams) layoutParams3).bottomMargin;
                    int measuredHeight2 = virtualChildAt4.getMeasuredHeight() + i58;
                    i52 = Math.max(i52, measuredHeight2);
                    if (!z22) {
                        i58 = measuredHeight2;
                    }
                    int max3 = Math.max(i51, i58);
                    if (z19) {
                        i18 = -1;
                        if (((LinearLayout.LayoutParams) layoutParams3).height == -1) {
                            z10 = true;
                            if (z21 && (baseline = virtualChildAt4.getBaseline()) != i18) {
                                i19 = ((LinearLayout.LayoutParams) layoutParams3).gravity;
                                if (i19 < 0) {
                                    i19 = this.mGravity;
                                }
                                int i59 = (((i19 & 112) >> 4) & (-2)) >> 1;
                                iArr2[i59] = Math.max(iArr2[i59], baseline);
                                iArr[i59] = Math.max(iArr[i59], measuredHeight2 - baseline);
                            }
                            z19 = z10;
                            i51 = max3;
                            f11 = f10;
                        }
                    } else {
                        i18 = -1;
                    }
                    z10 = false;
                    if (z21) {
                        i19 = ((LinearLayout.LayoutParams) layoutParams3).gravity;
                        if (i19 < 0) {
                        }
                        int i592 = (((i19 & 112) >> 4) & (-2)) >> 1;
                        iArr2[i592] = Math.max(iArr2[i592], baseline);
                        iArr[i592] = Math.max(iArr[i592], measuredHeight2 - baseline);
                    }
                    z19 = z10;
                    i51 = max3;
                    f11 = f10;
                }
                i54++;
                i49 = i16;
                virtualChildCount = i17;
            }
            i13 = i11;
            i14 = virtualChildCount;
            this.mTotalLength += getPaddingLeft() + getPaddingRight();
            max2 = (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) ? i52 : Math.max(i52, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
            i15 = i51;
            i12 = i53;
        }
        if (z19 || mode2 == 1073741824) {
            i15 = max2;
        }
        setMeasuredDimension(resolveSizeAndState | (i12 & (-16777216)), View.resolveSizeAndState(Math.max(i15 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i13, i12 << 16));
        if (z18) {
            forceUniformHeight(i14, i10);
        }
    }

    int measureNullChild(int i10) {
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:158:0x0325  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void measureVertical(int i10, int i11) {
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        boolean z10;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        View view;
        int max;
        boolean z11;
        int max2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = View.MeasureSpec.getMode(i10);
        int mode2 = View.MeasureSpec.getMode(i11);
        int i28 = this.mBaselineAlignedChildIndex;
        boolean z12 = this.mUseLargestChild;
        int i29 = 0;
        int i30 = 0;
        int i31 = 0;
        int i32 = 0;
        int i33 = 0;
        int i34 = 0;
        boolean z13 = false;
        boolean z14 = false;
        float f10 = 0.0f;
        boolean z15 = true;
        while (true) {
            int i35 = 8;
            int i36 = i32;
            if (i34 < virtualChildCount) {
                View virtualChildAt = getVirtualChildAt(i34);
                if (virtualChildAt == null) {
                    this.mTotalLength += measureNullChild(i34);
                    i23 = virtualChildCount;
                    i24 = mode2;
                    i32 = i36;
                } else {
                    int i37 = i29;
                    if (virtualChildAt.getVisibility() == 8) {
                        i34 += getChildrenSkipCount(virtualChildAt, i34);
                        i23 = virtualChildCount;
                        i32 = i36;
                        i29 = i37;
                        i24 = mode2;
                    } else {
                        if (hasDividerBeforeChildAt(i34)) {
                            this.mTotalLength += this.mDividerHeight;
                        }
                        LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                        float f11 = ((LinearLayout.LayoutParams) layoutParams).weight;
                        float f12 = f10 + f11;
                        if (mode2 == 1073741824 && ((LinearLayout.LayoutParams) layoutParams).height == 0 && f11 > 0.0f) {
                            int i38 = this.mTotalLength;
                            this.mTotalLength = Math.max(i38, ((LinearLayout.LayoutParams) layoutParams).topMargin + i38 + ((LinearLayout.LayoutParams) layoutParams).bottomMargin);
                            max = i31;
                            view = virtualChildAt;
                            i26 = i33;
                            z13 = true;
                            i21 = i37;
                            i22 = i30;
                            i23 = virtualChildCount;
                            i24 = mode2;
                            i25 = i36;
                            i27 = i34;
                        } else {
                            int i39 = i30;
                            if (((LinearLayout.LayoutParams) layoutParams).height != 0 || f11 <= 0.0f) {
                                i20 = Integer.MIN_VALUE;
                            } else {
                                ((LinearLayout.LayoutParams) layoutParams).height = -2;
                                i20 = 0;
                            }
                            i21 = i37;
                            int i40 = i20;
                            i22 = i39;
                            int i41 = i31;
                            i23 = virtualChildCount;
                            i24 = mode2;
                            i25 = i36;
                            i26 = i33;
                            i27 = i34;
                            measureChildBeforeLayout(virtualChildAt, i34, i10, 0, i11, f12 == 0.0f ? this.mTotalLength : 0);
                            if (i40 != Integer.MIN_VALUE) {
                                ((LinearLayout.LayoutParams) layoutParams).height = i40;
                            }
                            int measuredHeight = virtualChildAt.getMeasuredHeight();
                            int i42 = this.mTotalLength;
                            view = virtualChildAt;
                            this.mTotalLength = Math.max(i42, i42 + measuredHeight + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin + getNextLocationOffset(view));
                            max = z12 ? Math.max(measuredHeight, i41) : i41;
                        }
                        if (i28 >= 0 && i28 == i27 + 1) {
                            this.mBaselineChildTop = this.mTotalLength;
                        }
                        if (i27 < i28 && ((LinearLayout.LayoutParams) layoutParams).weight > 0.0f) {
                            throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                        }
                        if (mode == 1073741824 || ((LinearLayout.LayoutParams) layoutParams).width != -1) {
                            z11 = false;
                        } else {
                            z11 = true;
                            z14 = true;
                        }
                        int i43 = ((LinearLayout.LayoutParams) layoutParams).leftMargin + ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                        int measuredWidth = view.getMeasuredWidth() + i43;
                        int max3 = Math.max(i22, measuredWidth);
                        int combineMeasuredStates = View.combineMeasuredStates(i21, view.getMeasuredState());
                        z15 = z15 && ((LinearLayout.LayoutParams) layoutParams).width == -1;
                        if (((LinearLayout.LayoutParams) layoutParams).weight > 0.0f) {
                            if (!z11) {
                                i43 = measuredWidth;
                            }
                            i32 = Math.max(i25, i43);
                            max2 = i26;
                        } else {
                            if (!z11) {
                                i43 = measuredWidth;
                            }
                            max2 = Math.max(i26, i43);
                            i32 = i25;
                        }
                        int childrenSkipCount = getChildrenSkipCount(view, i27) + i27;
                        i31 = max;
                        f10 = f12;
                        i33 = max2;
                        i29 = combineMeasuredStates;
                        i34 = childrenSkipCount;
                        i30 = max3;
                    }
                }
                i34++;
                mode2 = i24;
                virtualChildCount = i23;
            } else {
                int i44 = i29;
                int i45 = i31;
                int i46 = i33;
                int i47 = virtualChildCount;
                int i48 = mode2;
                int i49 = i30;
                if (this.mTotalLength > 0) {
                    i12 = i47;
                    if (hasDividerBeforeChildAt(i12)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                } else {
                    i12 = i47;
                }
                if (z12 && (i48 == Integer.MIN_VALUE || i48 == 0)) {
                    this.mTotalLength = 0;
                    int i50 = 0;
                    while (i50 < i12) {
                        View virtualChildAt2 = getVirtualChildAt(i50);
                        if (virtualChildAt2 == null) {
                            this.mTotalLength += measureNullChild(i50);
                        } else if (virtualChildAt2.getVisibility() == i35) {
                            i50 += getChildrenSkipCount(virtualChildAt2, i50);
                        } else {
                            LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                            int i51 = this.mTotalLength;
                            this.mTotalLength = Math.max(i51, i51 + i45 + ((LinearLayout.LayoutParams) layoutParams2).topMargin + ((LinearLayout.LayoutParams) layoutParams2).bottomMargin + getNextLocationOffset(virtualChildAt2));
                        }
                        i50++;
                        i35 = 8;
                    }
                }
                int paddingTop = this.mTotalLength + getPaddingTop() + getPaddingBottom();
                this.mTotalLength = paddingTop;
                int resolveSizeAndState = View.resolveSizeAndState(Math.max(paddingTop, getSuggestedMinimumHeight()), i11, 0);
                int i52 = (16777215 & resolveSizeAndState) - this.mTotalLength;
                if (!z13 && (i52 == 0 || f10 <= 0.0f)) {
                    i15 = Math.max(i46, i36);
                    if (z12 && i48 != 1073741824) {
                        for (int i53 = 0; i53 < i12; i53++) {
                            View virtualChildAt3 = getVirtualChildAt(i53);
                            if (virtualChildAt3 != null && virtualChildAt3.getVisibility() != 8 && ((LinearLayout.LayoutParams) ((LayoutParams) virtualChildAt3.getLayoutParams())).weight > 0.0f) {
                                virtualChildAt3.measure(View.MeasureSpec.makeMeasureSpec(virtualChildAt3.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i45, 1073741824));
                            }
                        }
                    }
                    i14 = i10;
                    i13 = i44;
                } else {
                    float f13 = this.mWeightSum;
                    if (f13 > 0.0f) {
                        f10 = f13;
                    }
                    this.mTotalLength = 0;
                    int i54 = i52;
                    int i55 = i46;
                    i13 = i44;
                    int i56 = 0;
                    while (i56 < i12) {
                        View virtualChildAt4 = getVirtualChildAt(i56);
                        if (virtualChildAt4.getVisibility() == 8) {
                            i16 = i54;
                        } else {
                            LayoutParams layoutParams3 = (LayoutParams) virtualChildAt4.getLayoutParams();
                            float f14 = ((LinearLayout.LayoutParams) layoutParams3).weight;
                            if (f14 > 0.0f) {
                                int i57 = (int) ((i54 * f14) / f10);
                                float f15 = f10 - f14;
                                i16 = i54 - i57;
                                int childMeasureSpec = ViewGroup.getChildMeasureSpec(i10, getPaddingLeft() + getPaddingRight() + ((LinearLayout.LayoutParams) layoutParams3).leftMargin + ((LinearLayout.LayoutParams) layoutParams3).rightMargin, ((LinearLayout.LayoutParams) layoutParams3).width);
                                if (((LinearLayout.LayoutParams) layoutParams3).height == 0) {
                                    i19 = 1073741824;
                                    if (i48 == 1073741824) {
                                        if (i57 <= 0) {
                                            i57 = 0;
                                        }
                                        virtualChildAt4.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(i57, 1073741824));
                                        i13 = View.combineMeasuredStates(i13, virtualChildAt4.getMeasuredState() & (-256));
                                        f10 = f15;
                                    }
                                } else {
                                    i19 = 1073741824;
                                }
                                int measuredHeight2 = virtualChildAt4.getMeasuredHeight() + i57;
                                if (measuredHeight2 < 0) {
                                    measuredHeight2 = 0;
                                }
                                virtualChildAt4.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(measuredHeight2, i19));
                                i13 = View.combineMeasuredStates(i13, virtualChildAt4.getMeasuredState() & (-256));
                                f10 = f15;
                            } else {
                                i16 = i54;
                            }
                            int i58 = ((LinearLayout.LayoutParams) layoutParams3).leftMargin + ((LinearLayout.LayoutParams) layoutParams3).rightMargin;
                            int measuredWidth2 = virtualChildAt4.getMeasuredWidth() + i58;
                            i49 = Math.max(i49, measuredWidth2);
                            float f16 = f10;
                            if (mode != 1073741824) {
                                i17 = i13;
                                i18 = -1;
                                if (((LinearLayout.LayoutParams) layoutParams3).width == -1) {
                                    z10 = true;
                                    if (!z10) {
                                        i58 = measuredWidth2;
                                    }
                                    int max4 = Math.max(i55, i58);
                                    boolean z16 = !z15 && ((LinearLayout.LayoutParams) layoutParams3).width == i18;
                                    int i59 = this.mTotalLength;
                                    this.mTotalLength = Math.max(i59, virtualChildAt4.getMeasuredHeight() + i59 + ((LinearLayout.LayoutParams) layoutParams3).topMargin + ((LinearLayout.LayoutParams) layoutParams3).bottomMargin + getNextLocationOffset(virtualChildAt4));
                                    z15 = z16;
                                    i13 = i17;
                                    i55 = max4;
                                    f10 = f16;
                                }
                            } else {
                                i17 = i13;
                                i18 = -1;
                            }
                            z10 = false;
                            if (!z10) {
                            }
                            int max42 = Math.max(i55, i58);
                            if (z15) {
                            }
                            int i592 = this.mTotalLength;
                            this.mTotalLength = Math.max(i592, virtualChildAt4.getMeasuredHeight() + i592 + ((LinearLayout.LayoutParams) layoutParams3).topMargin + ((LinearLayout.LayoutParams) layoutParams3).bottomMargin + getNextLocationOffset(virtualChildAt4));
                            z15 = z16;
                            i13 = i17;
                            i55 = max42;
                            f10 = f16;
                        }
                        i56++;
                        i54 = i16;
                    }
                    i14 = i10;
                    this.mTotalLength += getPaddingTop() + getPaddingBottom();
                    i15 = i55;
                }
                if (z15 || mode == 1073741824) {
                    i15 = i49;
                }
                setMeasuredDimension(View.resolveSizeAndState(Math.max(i15 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i14, i13), resolveSizeAndState);
                if (z14) {
                    forceUniformWidth(i12, i11);
                    return;
                }
                return;
            }
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            drawDividersVertical(canvas);
        } else {
            drawDividersHorizontal(canvas);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        if (this.mOrientation == 1) {
            layoutVertical(i10, i11, i12, i13);
        } else {
            layoutHorizontal(i10, i11, i12, i13);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i10, int i11) {
        if (this.mOrientation == 1) {
            measureVertical(i10, i11);
        } else {
            measureHorizontal(i10, i11);
        }
    }

    public void setBaselineAligned(boolean z10) {
        this.mBaselineAligned = z10;
    }

    public void setBaselineAlignedChildIndex(int i10) {
        if (i10 >= 0 && i10 < getChildCount()) {
            this.mBaselineAlignedChildIndex = i10;
            return;
        }
        throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable == this.mDivider) {
            return;
        }
        this.mDivider = drawable;
        if (drawable != null) {
            this.mDividerWidth = drawable.getIntrinsicWidth();
            this.mDividerHeight = drawable.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        setWillNotDraw(drawable == null);
        requestLayout();
    }

    public void setDividerPadding(int i10) {
        this.mDividerPadding = i10;
    }

    public void setGravity(int i10) {
        if (this.mGravity != i10) {
            if ((8388615 & i10) == 0) {
                i10 |= 8388611;
            }
            if ((i10 & 112) == 0) {
                i10 |= 48;
            }
            this.mGravity = i10;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int i10) {
        int i11 = i10 & 8388615;
        int i12 = this.mGravity;
        if ((8388615 & i12) != i11) {
            this.mGravity = i11 | ((-8388616) & i12);
            requestLayout();
        }
    }

    public void setMeasureWithLargestChildEnabled(boolean z10) {
        this.mUseLargestChild = z10;
    }

    public void setOrientation(int i10) {
        if (this.mOrientation != i10) {
            this.mOrientation = i10;
            requestLayout();
        }
    }

    public void setShowDividers(int i10) {
        if (i10 != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i10;
    }

    public void setVerticalGravity(int i10) {
        int i11 = i10 & 112;
        int i12 = this.mGravity;
        if ((i12 & 112) != i11) {
            this.mGravity = i11 | (i12 & (-113));
            requestLayout();
        }
    }

    public void setWeightSum(float f10) {
        this.mWeightSum = Math.max(0.0f, f10);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        int i10 = this.mOrientation;
        if (i10 == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i10 == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        int[] iArr = R$styleable.LinearLayoutCompat;
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, iArr, i10, 0);
        ViewCompat.j0(this, context, iArr, attributeSet, w10.r(), i10, 0);
        int k10 = w10.k(R$styleable.LinearLayoutCompat_android_orientation, -1);
        if (k10 >= 0) {
            setOrientation(k10);
        }
        int k11 = w10.k(R$styleable.LinearLayoutCompat_android_gravity, -1);
        if (k11 >= 0) {
            setGravity(k11);
        }
        boolean a10 = w10.a(R$styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!a10) {
            setBaselineAligned(a10);
        }
        this.mWeightSum = w10.i(R$styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = w10.k(R$styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = w10.a(R$styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(w10.g(R$styleable.LinearLayoutCompat_divider));
        this.mShowDividers = w10.k(R$styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = w10.f(R$styleable.LinearLayoutCompat_dividerPadding, 0);
        w10.x();
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }
}
