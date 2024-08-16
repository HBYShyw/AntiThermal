package android.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import com.oplus.internal.R;

/* loaded from: classes.dex */
public class OplusAlertDialogBuilder extends AlertDialog.Builder {
    private static final int DEF_STYLE_ATTR = 16842845;
    private static final int DEF_WINDOW_GRAVITY = 17;
    private static final String TAG = "OplusAlertDialogBuilder";
    private boolean hasAdapter;
    private boolean hasMessage;
    private boolean hasSetView;
    private boolean hasTitle;
    private int mCustomContentLayoutRes;
    private int mGravity;
    private DialogInterface.OnClickListener mItemClickListener;
    private CharSequence[] mItems;
    private CharSequence[] mSummaryItems;

    public OplusAlertDialogBuilder(Context context) {
        super(context);
        this.hasTitle = false;
        this.hasMessage = false;
        this.hasAdapter = false;
        this.hasSetView = false;
        initAttrs();
    }

    public OplusAlertDialogBuilder(Context context, int i) {
        super(context, i);
        this.hasTitle = false;
        this.hasMessage = false;
        this.hasAdapter = false;
        this.hasSetView = false;
        initAttrs();
    }

    public static OplusAlertDialogBuilder createCenterBuilder(Context context) {
        return new OplusAlertDialogBuilder(context, 201523241);
    }

    public static OplusAlertDialogBuilder createBottomBuilder(Context context) {
        return new OplusAlertDialogBuilder(context, 201523242);
    }

    private void initAttrs() {
        TypedArray attributes = getContext().obtainStyledAttributes(null, R.styleable.OplusAlertDialogBuilder, 16842845, 0);
        this.mGravity = attributes.getInt(0, 17);
        this.mCustomContentLayoutRes = attributes.getResourceId(1, 0);
        attributes.recycle();
    }

    @Override // android.app.AlertDialog.Builder
    public AlertDialog create() {
        initCustomPanel();
        initAdapter();
        AlertDialog dialog = super.create();
        initWindow(dialog);
        return dialog;
    }

    private void initCustomPanel() {
        int i;
        if (this.hasSetView || (i = this.mCustomContentLayoutRes) == 0) {
            return;
        }
        setView(i);
    }

    protected void initAdapter() {
        if (this.hasAdapter) {
            return;
        }
        CharSequence[] charSequenceArr = this.mItems;
        boolean hasItem = charSequenceArr != null && charSequenceArr.length > 0;
        if (hasItem) {
            setAdapter((ListAdapter) new OplusAlertDialogItemAdapter(getContext(), this.hasTitle, this.hasMessage, this.mItems, this.mSummaryItems), this.mItemClickListener);
        }
    }

    private void initWindow(AlertDialog dialog) {
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setGravity(this.mGravity);
        window.getDecorView().setOnTouchListener(new InsetTouchListener(dialog));
        makeWindowMatchParent(window);
    }

    private void makeWindowMatchParent(Window window) {
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = -1;
        window.setAttributes(layoutParams);
    }

    @Override // android.app.AlertDialog.Builder
    public AlertDialog show() {
        AlertDialog dialog = super.show();
        initCustomPanelVisibility(dialog);
        initListPanel(dialog);
        return dialog;
    }

    private void initCustomPanelVisibility(AlertDialog dialog) {
        Window window = dialog.getWindow();
        if (window != null && this.hasSetView) {
            ViewGroup customPanel = (ViewGroup) window.findViewById(android.R.id.eight);
            if (customPanel != null) {
                customPanel.setVisibility(0);
            }
            ViewGroup custom = (ViewGroup) window.findViewById(android.R.id.custom);
            if (custom != null) {
                custom.setVisibility(0);
            }
        }
    }

    private void initListPanel(AlertDialog dialog) {
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        ViewGroup listPanel = (ViewGroup) window.findViewById(201457734);
        View listView = dialog.getListView();
        boolean hasListView = (listPanel == null || listView == null) ? false : true;
        if (hasListView) {
            listView.setPadding(0, 0, 0, 0);
        }
        boolean isNeedToAddListView = this.hasMessage && hasListView;
        if (isNeedToAddListView) {
            int marginTop = getContext().getResources().getDimensionPixelOffset(201654488);
            listPanel.setPadding(0, marginTop, 0, 0);
            listPanel.addView(listView, new ViewGroup.LayoutParams(-1, -1));
        }
        if (this.mGravity == 80) {
            View scrollView = (ViewGroup) window.findViewById(android.R.id.stateAlwaysVisible);
            if (isNeedToAddListView && scrollView != null) {
                setViewHorizontalWeight(scrollView, 1);
                setViewHorizontalWeight(listPanel, 1);
            }
        }
    }

    private void setViewHorizontalWeight(View view, int weight) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (!(lp instanceof LinearLayout.LayoutParams)) {
            return;
        }
        lp.height = 0;
        ((LinearLayout.LayoutParams) lp).weight = weight;
        view.setLayoutParams(lp);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class InsetTouchListener implements View.OnTouchListener {
        private Dialog dialog;
        private final int prePieSlop;

        public InsetTouchListener(Dialog dialog) {
            this.dialog = dialog;
            this.prePieSlop = ViewConfiguration.get(dialog.getContext()).getScaledWindowTouchSlop();
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent event) {
            View parentPanel = view.findViewById(android.R.id.resolver_empty_state_title);
            RectF contentRect = new RectF(parentPanel.getLeft() + parentPanel.getPaddingLeft(), parentPanel.getTop() + parentPanel.getPaddingTop(), parentPanel.getRight() - parentPanel.getPaddingRight(), parentPanel.getBottom() - parentPanel.getPaddingBottom());
            if (contentRect.contains(event.getX(), event.getY())) {
                return false;
            }
            MotionEvent outsideEvent = MotionEvent.obtain(event);
            if (event.getAction() == 1) {
                outsideEvent.setAction(4);
            }
            view.performClick();
            boolean isConsumed = this.dialog.onTouchEvent(outsideEvent);
            outsideEvent.recycle();
            return isConsumed;
        }
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setTitle(CharSequence title) {
        this.hasTitle = !TextUtils.isEmpty(title);
        super.setTitle(title);
        return this;
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setTitle(int resId) {
        this.hasTitle = !TextUtils.isEmpty(getContext().getString(resId));
        super.setTitle(resId);
        return this;
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setMessage(CharSequence message) {
        this.hasMessage = !TextUtils.isEmpty(message);
        super.setMessage(message);
        return this;
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setMessage(int resId) {
        this.hasMessage = !TextUtils.isEmpty(getContext().getString(resId));
        super.setMessage(resId);
        return this;
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setView(int layoutResId) {
        this.hasSetView = true;
        super.setView(layoutResId);
        return this;
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setView(View view) {
        this.hasSetView = true;
        super.setView(view);
        return this;
    }

    public OplusAlertDialogBuilder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        this.hasSetView = true;
        super.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
        return this;
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setItems(int itemsId, DialogInterface.OnClickListener listener) {
        this.mItems = getContext().getResources().getTextArray(itemsId);
        this.mItemClickListener = listener;
        super.setItems(itemsId, listener);
        return this;
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
        this.mItems = items;
        this.mItemClickListener = listener;
        super.setItems(items, listener);
        return this;
    }

    public OplusAlertDialogBuilder setSummaryItems(int itemsId) {
        this.mSummaryItems = getContext().getResources().getTextArray(itemsId);
        return this;
    }

    public OplusAlertDialogBuilder setSummaryItems(CharSequence[] summaryItems) {
        this.mSummaryItems = summaryItems;
        return this;
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener) {
        this.hasAdapter = adapter != null;
        this.mItemClickListener = listener;
        super.setAdapter(adapter, listener);
        return this;
    }

    @Override // android.app.AlertDialog.Builder
    public OplusAlertDialogBuilder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener) {
        this.hasAdapter = adapter != null;
        this.mItemClickListener = listener;
        super.setSingleChoiceItems(adapter, checkedItem, listener);
        return this;
    }
}
