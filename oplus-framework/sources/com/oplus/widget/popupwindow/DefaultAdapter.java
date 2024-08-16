package com.oplus.widget.popupwindow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.oplus.util.OplusChangeTextUtil;
import java.util.List;

/* loaded from: classes.dex */
public class DefaultAdapter extends BaseAdapter {
    private Context mContext;
    private List<PopupListItem> mItemList;
    private int mPopupListItemMinHeight;
    private int mPopupListItemPaddingVertical;
    private int mPopupListPaddingVertical;

    public DefaultAdapter(Context context, List<PopupListItem> itemList) {
        this.mContext = context;
        this.mItemList = itemList;
        Resources res = context.getResources();
        this.mPopupListPaddingVertical = res.getDimensionPixelSize(201654460);
        this.mPopupListItemPaddingVertical = res.getDimensionPixelSize(201654455);
        this.mPopupListItemMinHeight = res.getDimensionPixelSize(201654454);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mItemList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mItemList.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(201917490, parent, false);
            viewHolder.mIcon = (ImageView) convertView.findViewById(201457741);
            viewHolder.mTitle = (TextView) convertView.findViewById(201457742);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getCount() == 1) {
            convertView.setMinimumHeight(this.mPopupListItemMinHeight + (this.mPopupListPaddingVertical * 2));
            int i = this.mPopupListItemPaddingVertical;
            int i2 = this.mPopupListPaddingVertical;
            convertView.setPadding(0, i + i2, 0, i + i2);
        } else if (position == 0) {
            convertView.setMinimumHeight(this.mPopupListItemMinHeight + this.mPopupListPaddingVertical);
            int i3 = this.mPopupListItemPaddingVertical;
            convertView.setPadding(0, this.mPopupListPaddingVertical + i3, 0, i3);
        } else if (position == getCount() - 1) {
            convertView.setMinimumHeight(this.mPopupListItemMinHeight + this.mPopupListPaddingVertical);
            int i4 = this.mPopupListItemPaddingVertical;
            convertView.setPadding(0, i4, 0, this.mPopupListPaddingVertical + i4);
        } else {
            convertView.setMinimumHeight(this.mPopupListItemMinHeight);
            int i5 = this.mPopupListItemPaddingVertical;
            convertView.setPadding(0, i5, 0, i5);
        }
        boolean isEnable = this.mItemList.get(position).isEnable();
        convertView.setEnabled(isEnable);
        setIcon(viewHolder.mIcon, viewHolder.mTitle, this.mItemList.get(position), isEnable);
        setTitle(viewHolder.mTitle, this.mItemList.get(position), isEnable);
        return convertView;
    }

    private void setIcon(ImageView imageView, TextView textView, PopupListItem item, boolean isEnable) {
        Drawable icon;
        LinearLayout.LayoutParams titleLayoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        if (item.getIconId() == 0 && item.getIcon() == null) {
            imageView.setVisibility(8);
            titleLayoutParams.leftMargin = this.mContext.getResources().getDimensionPixelSize(201654451);
            titleLayoutParams.rightMargin = this.mContext.getResources().getDimensionPixelSize(201654451);
            return;
        }
        imageView.setVisibility(0);
        titleLayoutParams.leftMargin = this.mContext.getResources().getDimensionPixelSize(201654449);
        titleLayoutParams.rightMargin = this.mContext.getResources().getDimensionPixelSize(201654450);
        imageView.setEnabled(isEnable);
        if (item.getIcon() == null) {
            icon = this.mContext.getResources().getDrawable(item.getIconId());
        } else {
            icon = item.getIcon();
        }
        imageView.setImageDrawable(icon);
    }

    private void setTitle(TextView textView, PopupListItem item, boolean isEnable) {
        textView.setEnabled(isEnable);
        textView.setText(item.getTitle());
        textView.setTextColor(this.mContext.getResources().getColorStateList(201719887));
        float suitableFontSize = OplusChangeTextUtil.getSuitableFontSize(this.mContext.getResources().getDimensionPixelSize(201654452), this.mContext.getResources().getConfiguration().fontScale, 5);
        textView.setTextSize(0, suitableFontSize);
    }

    /* loaded from: classes.dex */
    static class ViewHolder {
        ImageView mIcon;
        TextView mTitle;

        ViewHolder() {
        }
    }
}
