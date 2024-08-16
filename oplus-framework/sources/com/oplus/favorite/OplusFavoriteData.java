package com.oplus.favorite;

/* loaded from: classes.dex */
public class OplusFavoriteData {
    public static final String DATA_CONTENT = "data_content";
    public static final String DATA_EXTRA = "data_extra";
    public static final String DATA_TITLE = "data_title";
    public static final String DATA_URL = "data_url";
    private String mTitle = null;
    private String mUrl = null;
    private String mContent = "";
    private String mExtra = "";

    public String toString() {
        return "FavoriteData{mTitle:" + this.mTitle + ", mUrl=" + this.mUrl + ", mContent=" + this.mContent + ", mExtra=" + this.mExtra + "}";
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public void setExtra(String extra) {
        this.mExtra = extra;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getContent() {
        return this.mContent;
    }

    public String getExtra() {
        return this.mExtra;
    }
}
