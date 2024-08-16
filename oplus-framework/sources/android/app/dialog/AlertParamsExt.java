package android.app.dialog;

import android.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.app.AlertController;
import com.oplus.util.OplusContextUtil;

/* loaded from: classes.dex */
public class AlertParamsExt implements IAlertParamsExt {
    public static final int TYPE_BOTTOM = 1;
    private AlertController.AlertParams mAlertParams;
    public CharSequence[] mSummaries;
    boolean mMessageNeedScroll = false;
    private int mDialogType = 0;

    public AlertParamsExt(Object base) {
        this.mAlertParams = null;
        this.mAlertParams = (AlertController.AlertParams) base;
    }

    public boolean isMessageNeedScroll() {
        return this.mMessageNeedScroll;
    }

    public void setMessageNeedScroll(boolean messageNeedScroll) {
        this.mMessageNeedScroll = messageNeedScroll;
    }

    public int getDialogType() {
        return this.mDialogType;
    }

    public void setDialogType(int dialogType) {
        this.mDialogType = dialogType;
    }

    public void setSummaries(CharSequence[] summaryItems) {
        this.mSummaries = summaryItems;
    }

    public void setItems(CharSequence[] items) {
        AlertController.AlertParams alertParams = this.mAlertParams;
        if (alertParams != null) {
            alertParams.mItems = items;
        }
    }

    Context getContext() {
        return this.mAlertParams.mContext;
    }

    public boolean needHookAdapter(boolean isSingleChoice, boolean isMultiChoice) {
        return (OplusContextUtil.isOplusAlertDialogBuilderStyle(getContext()) || isSingleChoice || isMultiChoice || !OplusContextUtil.isOplusStyle(getContext())) ? false : true;
    }

    public ListAdapter getHookAdapter(Context context, CharSequence title, CharSequence message, CharSequence[] items) {
        boolean hasTitle = !TextUtils.isEmpty(title);
        boolean hasMessage = !TextUtils.isEmpty(message);
        return new SummaryAdapter(context, hasTitle, hasMessage, items, this.mSummaries);
    }

    public void hookAlertParaApply(IAlertControllerExt alertConExt) {
        if (OplusContextUtil.isOplusStyle(getContext())) {
            alertConExt.setMessageNeedScroll(this.mMessageNeedScroll);
            alertConExt.setDialogType(this.mDialogType);
        }
    }

    public void setListStyle(ListView listView, boolean isSingleChoice, boolean isMultiChoice) {
        if (OplusContextUtil.isOplusAlertDialogBuilderStyle(getContext())) {
            return;
        }
        boolean isOplusStyle = OplusContextUtil.isOplusStyle(listView.getContext());
        if (isSingleChoice) {
            if (isOplusStyle) {
                listView.setSelector(201850944);
                listView.setItemsCanFocus(false);
                return;
            }
            return;
        }
        if (isMultiChoice && isOplusStyle) {
            listView.setSelector(201850944);
            listView.setItemsCanFocus(false);
        }
    }

    public View getConvertView(View target, int position, int count) {
        if (OplusContextUtil.isOplusAlertDialogBuilderStyle(getContext())) {
            return target;
        }
        if (target != null && OplusContextUtil.isOplusStyle(target.getContext())) {
            Context context = target.getContext();
            int paddingLeft = context.getResources().getDimensionPixelOffset(201654311);
            int paddingRight = context.getResources().getDimensionPixelOffset(201654312);
            int paddingBottom = context.getResources().getDimensionPixelOffset(201654413);
            int paddingBottomOffset = context.getResources().getDimensionPixelOffset(201654308);
            int paddingTop = context.getResources().getDimensionPixelOffset(201654413);
            int minHeight = context.getResources().getDimensionPixelOffset(201654313);
            if (position == count - 1) {
                target.setMinimumHeight(minHeight + paddingBottomOffset);
                target.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + paddingBottomOffset);
            } else {
                target.setMinimumHeight(minHeight);
                target.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            }
        }
        return target;
    }

    /* loaded from: classes.dex */
    public static class SummaryAdapter extends BaseAdapter {
        private static final int LAYOUT = 201917449;
        private Context mContext;
        private boolean mHasMessage;
        private boolean mHasTitle;
        private CharSequence[] mItems;
        private CharSequence[] mSummaries;

        public SummaryAdapter(Context context, boolean hasTitle, boolean hasMessage, CharSequence[] items, CharSequence[] summaries) {
            this.mHasTitle = hasTitle;
            this.mHasMessage = hasMessage;
            this.mContext = context;
            this.mItems = items;
            this.mSummaries = summaries;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View convertView2 = LayoutInflater.from(this.mContext).inflate(LAYOUT, parent, false);
            TextView itemView = (TextView) convertView2.findViewById(R.id.text1);
            TextView summaryView = (TextView) convertView2.findViewById(201457735);
            CharSequence item = getItem(position);
            CharSequence summary = getSummary(position);
            itemView.setText(item);
            if (TextUtils.isEmpty(summary)) {
                summaryView.setVisibility(8);
            } else {
                summaryView.setVisibility(0);
                summaryView.setText(summary);
            }
            resetPadding(position, convertView2);
            return convertView2;
        }

        private void resetPadding(int position, View convertView) {
            int paddingOffset = this.mContext.getResources().getDimensionPixelSize(201654308);
            int paddingTop = convertView.getPaddingTop();
            int paddingLeft = convertView.getPaddingLeft();
            int paddingBottom = convertView.getPaddingBottom();
            int paddingRight = convertView.getPaddingRight();
            if (getCount() > 1) {
                if (position == getCount() - 1) {
                    convertView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + paddingOffset);
                    convertView.setMinimumHeight(convertView.getMinimumHeight() + paddingOffset);
                } else if (!this.mHasTitle && !this.mHasMessage) {
                    if (position == 0) {
                        convertView.setPadding(paddingLeft, paddingTop + paddingOffset, paddingRight, paddingBottom);
                        convertView.setMinimumHeight(convertView.getMinimumHeight() + paddingOffset);
                    } else {
                        convertView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                    }
                }
            }
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

        public CharSequence getSummary(int position) {
            CharSequence[] charSequenceArr = this.mSummaries;
            if (charSequenceArr != null && position < charSequenceArr.length) {
                return charSequenceArr[position];
            }
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            return true;
        }
    }
}
