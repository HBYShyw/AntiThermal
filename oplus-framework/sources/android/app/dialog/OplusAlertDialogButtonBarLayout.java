package android.app.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.oplus.internal.R;

/* loaded from: classes.dex */
public class OplusAlertDialogButtonBarLayout extends LinearLayout {
    private View mButDivider1;
    private View mButDivider2;
    private int mButDividerSize;
    private View mContentPanel;
    private Context mContext;
    private View mCustomPanel;
    private boolean mForceVertical;
    private int mHorButDividerVerMargin;
    private int mHorButHorPadding;
    private int mHorButPanelMinHeight;
    private Button mNegButton;
    private Button mNeuButton;
    private View mParentView;
    private Button mPosButton;
    private View mTopPanel;
    private int mVerButDividerHorMargin;
    private int mVerButDividerVerMargin;
    private int mVerButHorPadding;
    private int mVerButMinHeight;
    private int mVerButPaddingOffset;
    private int mVerButVerPadding;
    private int mVerNegButVerPaddingOffset;
    private int mVerPaddingBottom;

    public OplusAlertDialogButtonBarLayout(Context context) {
        super(context, null);
    }

    public OplusAlertDialogButtonBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusAlertDialogButtonBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        this.mHorButHorPadding = context.getResources().getDimensionPixelSize(201654481);
        this.mVerButHorPadding = this.mContext.getResources().getDimensionPixelSize(201654482);
        this.mButDividerSize = this.mContext.getResources().getDimensionPixelSize(201654297);
        this.mVerButMinHeight = this.mContext.getResources().getDimensionPixelSize(201654303);
        this.mVerButPaddingOffset = this.mContext.getResources().getDimensionPixelSize(201654308);
        this.mVerButVerPadding = this.mContext.getResources().getDimensionPixelSize(201654483);
        this.mVerButDividerHorMargin = this.mContext.getResources().getDimensionPixelSize(201654484);
        this.mVerButDividerVerMargin = this.mContext.getResources().getDimensionPixelSize(201654485);
        this.mHorButDividerVerMargin = this.mContext.getResources().getDimensionPixelSize(201654486);
        this.mHorButPanelMinHeight = this.mContext.getResources().getDimensionPixelSize(201654487);
        TypedArray typedArray = this.mContext.obtainStyledAttributes(attrs, R.styleable.OplusAlertDialogButtonBarLayout);
        this.mForceVertical = typedArray.getBoolean(0, false);
        this.mVerNegButVerPaddingOffset = typedArray.getDimensionPixelOffset(1, 0);
        typedArray.recycle();
    }

    public void setForceVertical(boolean forceVertical) {
        this.mForceVertical = forceVertical;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initView();
        if (this.mForceVertical || needSetButVertical(getMeasuredWidth())) {
            if (!isVertical()) {
                setButtonsVertical();
            }
            resetVerButsPadding();
            resetVerButsBackground();
            resetVerDividerVisibility();
            resetVerPaddingBottom();
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        if (isVertical()) {
            setButtonsHorizontal();
        }
        resetHorDividerVisibility();
        resetHorPaddingBottom();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView() {
        if (this.mPosButton == null || this.mNegButton == null || this.mNeuButton == null || this.mButDivider1 == null || this.mButDivider2 == null || this.mParentView == null || this.mTopPanel == null || this.mContentPanel == null || this.mCustomPanel == null) {
            this.mPosButton = (Button) findViewById(android.R.id.button1);
            this.mNegButton = (Button) findViewById(android.R.id.button2);
            this.mNeuButton = (Button) findViewById(android.R.id.button3);
            this.mButDivider1 = findViewById(201457824);
            this.mButDivider2 = findViewById(201457825);
            View view = (View) getParent();
            this.mParentView = view;
            this.mTopPanel = view.findViewById(16909659);
            this.mContentPanel = this.mParentView.findViewById(android.R.id.date);
            this.mCustomPanel = this.mParentView.findViewById(android.R.id.eight);
        }
    }

    private boolean needSetButVertical(int parentWidth) {
        int buttonCount = getButtonCount();
        if (buttonCount == 0) {
            return false;
        }
        int buttonWidth = ((parentWidth - ((buttonCount - 1) * this.mButDividerSize)) / buttonCount) - (this.mHorButHorPadding * 2);
        int posButWidth = getBtnTextMeasureLength(this.mPosButton);
        int negButWidth = getBtnTextMeasureLength(this.mNegButton);
        int neuButWidth = getBtnTextMeasureLength(this.mNeuButton);
        return posButWidth > buttonWidth || negButWidth > buttonWidth || neuButWidth > buttonWidth;
    }

    private int getBtnTextMeasureLength(Button btn) {
        if (btn != null && btn.getVisibility() == 0) {
            if (btn.isAllCaps()) {
                return (int) btn.getPaint().measureText(btn.getText().toString().toUpperCase());
            }
            return (int) btn.getPaint().measureText(btn.getText().toString());
        }
        return 0;
    }

    private void setButtonsVertical() {
        setOrientation(1);
        setMinimumHeight(0);
        setNeuButVertical();
        setVerButDivider2();
        setNegButVertical();
        setVerButDivider1();
        setPosButVertical();
    }

    private void setPosButVertical() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mPosButton.getLayoutParams();
        params.weight = 0.0f;
        params.width = -1;
        params.height = -2;
        this.mPosButton.setLayoutParams(params);
        Button button = this.mPosButton;
        int i = this.mVerButHorPadding;
        int i2 = this.mVerButVerPadding;
        button.setPaddingRelative(i, this.mVerButPaddingOffset + i2, i, i2);
        this.mPosButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
    }

    private void setNegButVertical() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mNegButton.getLayoutParams();
        params.weight = 0.0f;
        params.width = -1;
        params.height = -2;
        this.mNegButton.setLayoutParams(params);
        Button button = this.mNegButton;
        int i = this.mVerButHorPadding;
        int i2 = this.mVerButVerPadding;
        button.setPaddingRelative(i, i2, i, this.mVerButPaddingOffset + i2);
        this.mNegButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
        bringChildToFront(this.mNegButton);
    }

    private void setNeuButVertical() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mNeuButton.getLayoutParams();
        params.weight = 0.0f;
        params.width = -1;
        params.height = -2;
        this.mNeuButton.setLayoutParams(params);
        Button button = this.mNeuButton;
        int i = this.mVerButHorPadding;
        int i2 = this.mVerButVerPadding;
        button.setPaddingRelative(i, i2, i, i2);
        this.mNeuButton.setMinHeight(this.mVerButMinHeight);
        bringChildToFront(this.mNeuButton);
    }

    private void setVerButDivider1() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mButDivider1.getLayoutParams();
        params.width = -1;
        params.height = this.mButDividerSize;
        params.setMarginStart(this.mVerButDividerHorMargin);
        params.setMarginEnd(this.mVerButDividerHorMargin);
        params.topMargin = this.mVerButDividerVerMargin;
        params.bottomMargin = 0;
        this.mButDivider1.setLayoutParams(params);
    }

    private void setVerButDivider2() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mButDivider2.getLayoutParams();
        params.width = -1;
        params.height = this.mButDividerSize;
        params.setMarginStart(this.mVerButDividerHorMargin);
        params.setMarginEnd(this.mVerButDividerHorMargin);
        params.topMargin = 0;
        params.bottomMargin = 0;
        this.mButDivider2.setLayoutParams(params);
        bringChildToFront(this.mButDivider2);
    }

    private void setButtonsHorizontal() {
        setOrientation(0);
        setMinimumHeight(this.mHorButPanelMinHeight);
        setHorButDivider1();
        setNeuButHorizontal();
        setHorButDivider2();
        setPosButHorizontal();
        setNegButHorizontal();
    }

    private void setPosButHorizontal() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mPosButton.getLayoutParams();
        params.weight = 1.0f;
        params.width = 0;
        params.height = -1;
        this.mPosButton.setLayoutParams(params);
        Button button = this.mPosButton;
        int i = this.mHorButHorPadding;
        button.setPaddingRelative(i, 0, i, 0);
        this.mPosButton.setMinHeight(0);
        bringChildToFront(this.mPosButton);
    }

    private void setNegButHorizontal() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mNegButton.getLayoutParams();
        params.weight = 1.0f;
        params.width = 0;
        params.height = -1;
        this.mNegButton.setLayoutParams(params);
        Button button = this.mNegButton;
        int i = this.mHorButHorPadding;
        button.setPaddingRelative(i, 0, i, 0);
        this.mNegButton.setMinHeight(0);
    }

    private void setNeuButHorizontal() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mNeuButton.getLayoutParams();
        params.weight = 1.0f;
        params.width = 0;
        params.height = -1;
        this.mNeuButton.setLayoutParams(params);
        Button button = this.mNeuButton;
        int i = this.mHorButHorPadding;
        button.setPaddingRelative(i, 0, i, 0);
        this.mNeuButton.setMinHeight(0);
        bringChildToFront(this.mNeuButton);
    }

    private void setHorButDivider1() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mButDivider1.getLayoutParams();
        params.width = this.mButDividerSize;
        params.height = -1;
        params.setMarginStart(0);
        params.setMarginEnd(0);
        params.topMargin = this.mHorButDividerVerMargin;
        params.bottomMargin = this.mHorButDividerVerMargin;
        this.mButDivider1.setLayoutParams(params);
        bringChildToFront(this.mButDivider1);
    }

    private void setHorButDivider2() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mButDivider2.getLayoutParams();
        params.width = this.mButDividerSize;
        params.height = -1;
        params.setMarginStart(0);
        params.setMarginEnd(0);
        params.topMargin = this.mHorButDividerVerMargin;
        params.bottomMargin = this.mHorButDividerVerMargin;
        this.mButDivider2.setLayoutParams(params);
        bringChildToFront(this.mButDivider2);
    }

    private void resetVerButsPadding() {
        if (this.mForceVertical) {
            if (hasContent(this.mNegButton)) {
                if (hasContent(this.mPosButton) || hasContent(this.mNeuButton) || hasContent(this.mTopPanel) || hasContent(this.mContentPanel) || hasContent(this.mCustomPanel)) {
                    Button button = this.mNegButton;
                    int i = this.mVerButHorPadding;
                    int i2 = this.mVerButVerPadding;
                    int i3 = this.mVerNegButVerPaddingOffset;
                    button.setPaddingRelative(i, i2 + i3, i, i2 + i3);
                    this.mNegButton.setMinHeight(this.mVerButMinHeight + (this.mVerNegButVerPaddingOffset * 2));
                } else {
                    Button button2 = this.mNegButton;
                    int i4 = this.mVerButHorPadding;
                    int i5 = this.mVerButVerPadding;
                    button2.setPaddingRelative(i4, this.mVerButPaddingOffset + i5, i4, i5);
                    this.mNegButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
                }
            }
            if (hasContent(this.mNeuButton)) {
                if (hasContent(this.mNegButton)) {
                    if (hasContent(this.mPosButton) || hasContent(this.mTopPanel) || hasContent(this.mContentPanel) || hasContent(this.mCustomPanel)) {
                        Button button3 = this.mNeuButton;
                        int i6 = this.mVerButHorPadding;
                        int i7 = this.mVerButVerPadding;
                        button3.setPaddingRelative(i6, i7, i6, this.mVerButPaddingOffset + i7);
                        this.mNeuButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
                    } else {
                        Button button4 = this.mNeuButton;
                        int i8 = this.mVerButHorPadding;
                        int i9 = this.mVerButVerPadding;
                        int i10 = this.mVerButPaddingOffset;
                        button4.setPaddingRelative(i8, i9 + i10, i8, i9 + i10);
                        this.mNeuButton.setMinHeight(this.mVerButMinHeight + (this.mVerButPaddingOffset * 2));
                    }
                } else if (hasContent(this.mPosButton) || hasContent(this.mTopPanel) || hasContent(this.mContentPanel) || hasContent(this.mCustomPanel)) {
                    Button button5 = this.mNeuButton;
                    int i11 = this.mVerButHorPadding;
                    int i12 = this.mVerButVerPadding;
                    button5.setPaddingRelative(i11, i12, i11, i12);
                    this.mNeuButton.setMinHeight(this.mVerButMinHeight);
                } else {
                    Button button6 = this.mNeuButton;
                    int i13 = this.mVerButHorPadding;
                    int i14 = this.mVerButVerPadding;
                    button6.setPaddingRelative(i13, this.mVerButPaddingOffset + i14, i13, i14);
                    this.mNeuButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
                }
            }
            if (hasContent(this.mPosButton)) {
                if (hasContent(this.mTopPanel) || hasContent(this.mContentPanel) || hasContent(this.mCustomPanel)) {
                    if (hasContent(this.mNegButton)) {
                        if (hasContent(this.mNeuButton)) {
                            Button button7 = this.mPosButton;
                            int i15 = this.mVerButHorPadding;
                            int i16 = this.mVerButVerPadding;
                            button7.setPaddingRelative(i15, i16, i15, i16);
                            this.mPosButton.setMinHeight(this.mVerButMinHeight);
                            return;
                        }
                        Button button8 = this.mPosButton;
                        int i17 = this.mVerButHorPadding;
                        int i18 = this.mVerButVerPadding;
                        button8.setPaddingRelative(i17, i18, i17, this.mVerButPaddingOffset + i18);
                        this.mPosButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
                        return;
                    }
                    if (hasContent(this.mNeuButton)) {
                        Button button9 = this.mPosButton;
                        int i19 = this.mVerButHorPadding;
                        int i20 = this.mVerButVerPadding;
                        button9.setPaddingRelative(i19, i20, i19, this.mVerButPaddingOffset + i20);
                        this.mPosButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
                        return;
                    }
                    Button button10 = this.mPosButton;
                    int i21 = this.mVerButHorPadding;
                    int i22 = this.mVerButVerPadding;
                    button10.setPaddingRelative(i21, i22, i21, i22);
                    this.mPosButton.setMinHeight(this.mVerButMinHeight);
                    return;
                }
                if (hasContent(this.mNegButton)) {
                    if (hasContent(this.mNeuButton)) {
                        Button button11 = this.mPosButton;
                        int i23 = this.mVerButHorPadding;
                        int i24 = this.mVerButVerPadding;
                        button11.setPaddingRelative(i23, this.mVerButPaddingOffset + i24, i23, i24);
                        this.mPosButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
                        return;
                    }
                    Button button12 = this.mPosButton;
                    int i25 = this.mVerButHorPadding;
                    int i26 = this.mVerButVerPadding;
                    int i27 = this.mVerButPaddingOffset;
                    button12.setPaddingRelative(i25, i26 + i27, i25, i26 + i27);
                    this.mPosButton.setMinHeight(this.mVerButMinHeight + (this.mVerButPaddingOffset * 2));
                    return;
                }
                if (hasContent(this.mNeuButton)) {
                    Button button13 = this.mPosButton;
                    int i28 = this.mVerButHorPadding;
                    int i29 = this.mVerButVerPadding;
                    int i30 = this.mVerButPaddingOffset;
                    button13.setPaddingRelative(i28, i29 + i30, i28, i29 + i30);
                    this.mPosButton.setMinHeight(this.mVerButMinHeight + (this.mVerButPaddingOffset * 2));
                    return;
                }
                Button button14 = this.mPosButton;
                int i31 = this.mVerButHorPadding;
                int i32 = this.mVerButVerPadding;
                button14.setPaddingRelative(i31, this.mVerButPaddingOffset + i32, i31, i32);
                this.mPosButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
                return;
            }
            return;
        }
        if (hasContent(this.mPosButton)) {
            if (hasContent(this.mNeuButton) || hasContent(this.mNegButton)) {
                Button button15 = this.mPosButton;
                int i33 = this.mVerButHorPadding;
                int i34 = this.mVerButVerPadding;
                button15.setPaddingRelative(i33, i34, i33, i34);
                this.mPosButton.setMinHeight(this.mVerButMinHeight);
            } else {
                Button button16 = this.mPosButton;
                int i35 = this.mVerButHorPadding;
                int i36 = this.mVerButVerPadding;
                button16.setPaddingRelative(i35, i36, i35, this.mVerButPaddingOffset + i36);
                this.mPosButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
            }
        }
        if (hasContent(this.mNeuButton)) {
            if (hasContent(this.mNegButton)) {
                Button button17 = this.mNeuButton;
                int i37 = this.mVerButHorPadding;
                int i38 = this.mVerButVerPadding;
                button17.setPaddingRelative(i37, i38, i37, i38);
                this.mNeuButton.setMinHeight(this.mVerButMinHeight);
            } else {
                Button button18 = this.mNeuButton;
                int i39 = this.mVerButHorPadding;
                int i40 = this.mVerButVerPadding;
                button18.setPaddingRelative(i39, i40, i39, this.mVerButPaddingOffset + i40);
                this.mNeuButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
            }
        }
        if (hasContent(this.mNegButton)) {
            Button button19 = this.mNegButton;
            int i41 = this.mVerButHorPadding;
            int i42 = this.mVerButVerPadding;
            button19.setPaddingRelative(i41, i42, i41, this.mVerButPaddingOffset + i42);
            this.mNegButton.setMinHeight(this.mVerButMinHeight + this.mVerButPaddingOffset);
        }
    }

    private void resetVerButsBackground() {
        Button target;
        if (this.mForceVertical && !hasContent(this.mTopPanel) && !hasContent(this.mContentPanel) && !hasContent(this.mCustomPanel)) {
            if (getButtonCount() == 1) {
                if (hasContent(this.mPosButton)) {
                    target = this.mPosButton;
                } else {
                    target = hasContent(this.mNeuButton) ? this.mNeuButton : this.mNegButton;
                }
                target.setBackgroundResource(201850940);
                return;
            }
            if (getButtonCount() == 2) {
                Button target2 = hasContent(this.mPosButton) ? this.mPosButton : this.mNeuButton;
                target2.setBackgroundResource(201850940);
            } else if (getButtonCount() == 3) {
                Button target3 = this.mPosButton;
                target3.setBackgroundResource(201850940);
            }
        }
    }

    private void resetVerDividerVisibility() {
        if (this.mForceVertical) {
            if (getButtonCount() != 0) {
                if (hasContent(this.mNegButton)) {
                    if (hasContent(this.mNeuButton) || hasContent(this.mPosButton) || hasContent(this.mTopPanel) || hasContent(this.mContentPanel) || hasContent(this.mCustomPanel)) {
                        this.mButDivider1.setVisibility(8);
                        this.mButDivider2.setVisibility(0);
                        return;
                    } else {
                        this.mButDivider1.setVisibility(8);
                        this.mButDivider2.setVisibility(8);
                        return;
                    }
                }
                this.mButDivider1.setVisibility(8);
                this.mButDivider2.setVisibility(8);
                return;
            }
            this.mButDivider1.setVisibility(8);
            this.mButDivider2.setVisibility(8);
            return;
        }
        if (getButtonCount() != 0) {
            this.mButDivider1.setVisibility(4);
            this.mButDivider2.setVisibility(8);
        } else {
            this.mButDivider1.setVisibility(8);
            this.mButDivider2.setVisibility(8);
        }
    }

    private void resetHorDividerVisibility() {
        if (getButtonCount() == 2) {
            if (hasContent(this.mPosButton)) {
                this.mButDivider1.setVisibility(8);
                this.mButDivider2.setVisibility(0);
                return;
            } else {
                this.mButDivider1.setVisibility(0);
                this.mButDivider2.setVisibility(8);
                return;
            }
        }
        if (getButtonCount() == 3) {
            this.mButDivider1.setVisibility(0);
            this.mButDivider2.setVisibility(0);
        } else {
            this.mButDivider1.setVisibility(8);
            this.mButDivider2.setVisibility(8);
        }
    }

    private void resetVerPaddingBottom() {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), this.mVerPaddingBottom);
    }

    private void resetHorPaddingBottom() {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), 0);
    }

    private int getButtonCount() {
        int count = 0;
        if (hasContent(this.mPosButton)) {
            count = 0 + 1;
        }
        if (hasContent(this.mNegButton)) {
            count++;
        }
        if (hasContent(this.mNeuButton)) {
            return count + 1;
        }
        return count;
    }

    private boolean isVertical() {
        return getOrientation() == 1;
    }

    private boolean hasContent(View view) {
        return view != null && view.getVisibility() == 0;
    }

    public void setVerButDividerVerMargin(int verButDividerVerMargin) {
        this.mVerButDividerVerMargin = verButDividerVerMargin;
    }

    public void setVerButVerPadding(int verButVerPadding) {
        this.mVerButVerPadding = verButVerPadding;
    }

    public void setVerNegButVerPaddingOffset(int verNegButVerPaddingOffset) {
        this.mVerNegButVerPaddingOffset = verNegButVerPaddingOffset;
    }

    public void setVerPaddingBottom(int verPaddingBottom) {
        this.mVerPaddingBottom = verPaddingBottom;
    }

    public void setVerButPaddingOffset(int verButPaddingOffset) {
        this.mVerButPaddingOffset = verButPaddingOffset;
    }
}
