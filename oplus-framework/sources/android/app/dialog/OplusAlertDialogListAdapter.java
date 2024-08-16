package android.app.dialog;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/* loaded from: classes.dex */
public class OplusAlertDialogListAdapter extends BaseAdapter {
    private final int LAYOUT = 201917466;
    private Context mContext;
    private CharSequence[] mItems;
    private int[] mTextAppearances;

    /* loaded from: classes.dex */
    public class ViewHolder {
        TextView mTextView;

        public ViewHolder() {
        }
    }

    public OplusAlertDialogListAdapter(Context context, CharSequence[] items, int[] textAppearances) {
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
            convertView = LayoutInflater.from(this.mContext).inflate(201917466, parent, false);
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
        int paddingTop = this.mContext.getResources().getDimensionPixelSize(201654483);
        int paddingLeft = this.mContext.getResources().getDimensionPixelSize(201654408);
        int paddingBottom = this.mContext.getResources().getDimensionPixelSize(201654483);
        int paddingRight = this.mContext.getResources().getDimensionPixelSize(201654409);
        int minHeight = this.mContext.getResources().getDimensionPixelSize(201654313);
        if (getCount() > 1) {
            if (position == getCount() - 1) {
                convertView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + paddingOffset);
                convertView.setMinimumHeight(minHeight + paddingOffset);
            } else {
                convertView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                convertView.setMinimumHeight(minHeight);
            }
        }
    }
}
