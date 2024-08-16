package com.oplus.widget.popupwindow;

import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class PopupListItem {
    private Drawable mIcon;
    private int mIconId;
    private boolean mIsEnable;
    private String mTitle;

    public PopupListItem(int iconId, String title, boolean isEnable) {
        this.mIconId = iconId;
        this.mTitle = title;
        this.mIsEnable = isEnable;
    }

    public PopupListItem(String title, boolean isEnable) {
        this((Drawable) null, title, isEnable);
    }

    public PopupListItem(Drawable icon, String title, boolean isEnable) {
        this.mIcon = icon;
        this.mTitle = title;
        this.mIsEnable = isEnable;
    }

    public int getIconId() {
        return this.mIconId;
    }

    public void setIconId(int iconId) {
        this.mIconId = iconId;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public boolean isEnable() {
        return this.mIsEnable;
    }

    public void setEnable(boolean enable) {
        this.mIsEnable = enable;
    }
}
