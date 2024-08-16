package com.oplus.dialog;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/* loaded from: classes.dex */
public class OplusListDialog implements DialogInterface {
    private ListAdapter mAdapter;
    private AlertDialog.Builder mBuilder;
    private Context mContext;
    private int mCustomRes;
    private View mCustomView;
    private AlertDialog mDialog;
    private boolean mHasCustom;
    private CharSequence[] mItems;
    private CharSequence mMessage;
    private TextView mMessageView;
    private DialogInterface.OnClickListener mOnClickListener;
    private int[] mTextAppearances;

    public OplusListDialog(Context context) {
        this.mContext = context;
        this.mBuilder = new AlertDialog.Builder(context);
    }

    public OplusListDialog(Context context, int theme) {
        this.mContext = context;
        this.mBuilder = new AlertDialog.Builder(context, theme);
    }

    public OplusListDialog setItems(CharSequence[] items, int[] textAppearances, DialogInterface.OnClickListener onClickListener) {
        this.mItems = items;
        this.mTextAppearances = textAppearances;
        this.mOnClickListener = onClickListener;
        return this;
    }

    public OplusListDialog setTitle(CharSequence title) {
        this.mBuilder.setTitle(title);
        return this;
    }

    public OplusListDialog setMessage(CharSequence message) {
        this.mMessage = message;
        return this;
    }

    public OplusListDialog setCustomView(int layout) {
        this.mCustomRes = layout;
        this.mHasCustom = true;
        return this;
    }

    public OplusListDialog setCustomView(View customView) {
        this.mCustomView = customView;
        this.mHasCustom = true;
        return this;
    }

    public void show() {
        if (this.mDialog == null) {
            this.mDialog = create();
        }
        this.mDialog.show();
    }

    public AlertDialog create() {
        View layout = LayoutInflater.from(this.mContext).inflate(201917465, (ViewGroup) null);
        setupMessage(layout);
        setupCustomPanel(layout);
        if (this.mItems != null || this.mAdapter != null) {
            setupListPanel(layout);
        }
        this.mBuilder.setView(layout);
        AlertDialog dialog = this.mBuilder.create();
        return dialog;
    }

    @Override // android.content.DialogInterface
    public void cancel() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.cancel();
        }
    }

    public AlertDialog getDialog() {
        if (this.mDialog == null) {
            this.mDialog = create();
        }
        return this.mDialog;
    }

    @Override // android.content.DialogInterface
    public void dismiss() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public boolean isShowing() {
        AlertDialog alertDialog = this.mDialog;
        return alertDialog != null && alertDialog.isShowing();
    }

    private void setupMessage(View parentPanel) {
        TextView textView = (TextView) parentPanel.findViewById(201457772);
        this.mMessageView = textView;
        textView.setText(this.mMessage);
        if (TextUtils.isEmpty(this.mMessage)) {
            this.mMessageView.setVisibility(8);
            return;
        }
        View divider = parentPanel.findViewById(201457771);
        divider.setVisibility(0);
        this.mMessageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.oplus.dialog.OplusListDialog.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                int lineCount = OplusListDialog.this.mMessageView.getLineCount();
                if (lineCount > 1) {
                    OplusListDialog.this.mMessageView.setTextAlignment(2);
                } else {
                    OplusListDialog.this.mMessageView.setTextAlignment(4);
                }
                OplusListDialog.this.mMessageView.setText(OplusListDialog.this.mMessageView.getText());
                OplusListDialog.this.mMessageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setupCustomPanel(View parentPanel) {
        if (this.mHasCustom) {
            FrameLayout customPanel = (FrameLayout) parentPanel.findViewById(201457770);
            View view = this.mCustomView;
            if (view != null) {
                customPanel.addView(view);
            } else {
                View customView = LayoutInflater.from(this.mContext).inflate(this.mCustomRes, (ViewGroup) null);
                customPanel.addView(customView);
            }
        }
    }

    private void setupListPanel(View parentPanel) {
        ListView listView = (ListView) parentPanel.findViewById(201457759);
        listView.setAdapter(getAdapter());
        if (this.mOnClickListener != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.oplus.dialog.OplusListDialog.2
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    OplusListDialog.this.mOnClickListener.onClick(OplusListDialog.this.mDialog, position);
                }
            });
        }
    }

    private ListAdapter getAdapter() {
        ListAdapter listAdapter = this.mAdapter;
        return listAdapter == null ? new Adapter(this.mContext, this.mItems, this.mTextAppearances) : listAdapter;
    }

    public OplusListDialog setAdapter(ListAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Adapter extends BaseAdapter {
        private static final int LAYOUT = 201917466;
        private Context mContext;
        private CharSequence[] mItems;
        private int[] mTextAppearances;

        Adapter(Context context, CharSequence[] items, int[] textAppearances) {
            this.mContext = context;
            this.mItems = items;
            this.mTextAppearances = textAppearances;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            CharSequence[] charSequenceArr = this.mItems;
            if (charSequenceArr == null) {
                return 0;
            }
            return charSequenceArr.length;
        }

        @Override // android.widget.Adapter
        public CharSequence getItem(int position) {
            CharSequence[] charSequenceArr = this.mItems;
            if (charSequenceArr == null) {
                return null;
            }
            return charSequenceArr[position];
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View convertView2 = getViewInternal(position, convertView, parent);
            resetPadding(position, convertView2);
            return convertView2;
        }

        private View getViewInternal(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.mContext).inflate(LAYOUT, parent, false);
                TextView itemView = (TextView) convertView.findViewById(R.id.text1);
                viewHolder = new ViewHolder();
                viewHolder.mTextView = itemView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mTextView.setText(getItem(position));
            int[] iArr = this.mTextAppearances;
            if (iArr != null) {
                int textAppearance = iArr[position];
                if (textAppearance > 0) {
                    viewHolder.mTextView.setTextAppearance(this.mContext, textAppearance);
                } else {
                    viewHolder.mTextView.setTextAppearance(this.mContext, 201523240);
                }
            }
            return convertView;
        }

        private void resetPadding(int position, View convertView) {
            int paddingOffset = this.mContext.getResources().getDimensionPixelSize(201654308);
            int paddingTop = this.mContext.getResources().getDimensionPixelSize(201654410);
            int paddingLeft = this.mContext.getResources().getDimensionPixelSize(201654408);
            int paddingBottom = this.mContext.getResources().getDimensionPixelSize(201654411);
            int paddingRight = this.mContext.getResources().getDimensionPixelSize(201654409);
            int minHeight = this.mContext.getResources().getDimensionPixelSize(201654313);
            if (getCount() > 1) {
                if (position == getCount() - 1) {
                    convertView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + paddingOffset);
                    convertView.setMinimumHeight(minHeight + paddingOffset);
                } else if (position == 0) {
                    convertView.setPadding(paddingLeft, paddingTop + paddingOffset, paddingRight, paddingBottom);
                    convertView.setMinimumHeight(minHeight + paddingOffset);
                } else {
                    convertView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                    convertView.setMinimumHeight(minHeight);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ViewHolder {
        TextView mTextView;

        private ViewHolder() {
        }
    }
}
