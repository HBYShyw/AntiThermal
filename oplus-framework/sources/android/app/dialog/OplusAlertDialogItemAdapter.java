package android.app.dialog;

import android.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/* loaded from: classes.dex */
public class OplusAlertDialogItemAdapter extends BaseAdapter {
    private static final int LAYOUT = 201917494;
    private Context mContext;
    private boolean mHasMessage;
    private boolean mHasTitle;
    private CharSequence[] mItems;
    private CharSequence[] mSummaries;

    public OplusAlertDialogItemAdapter(Context context, boolean hasTitle, boolean hasMessage, CharSequence[] items, CharSequence[] summaries) {
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
                return;
            } else {
                if (!this.mHasTitle && !this.mHasMessage) {
                    if (position == 0) {
                        convertView.setPadding(paddingLeft, paddingTop + paddingOffset, paddingRight, paddingBottom);
                        convertView.setMinimumHeight(convertView.getMinimumHeight() + paddingOffset);
                        return;
                    } else {
                        convertView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                        return;
                    }
                }
                return;
            }
        }
        if (getCount() == 1) {
            if (!this.mHasTitle && !this.mHasMessage) {
                convertView.setPadding(paddingLeft, paddingTop + paddingOffset, paddingRight, paddingBottom + paddingOffset);
                convertView.setMinimumHeight(convertView.getMinimumHeight() + (paddingOffset * 2));
            } else {
                convertView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + paddingOffset);
                convertView.setMinimumHeight(convertView.getMinimumHeight() + paddingOffset);
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
