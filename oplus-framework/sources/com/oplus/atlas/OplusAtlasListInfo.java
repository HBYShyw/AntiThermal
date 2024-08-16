package com.oplus.atlas;

/* loaded from: classes.dex */
public class OplusAtlasListInfo {
    private String mAttribute;
    private String mListInfo;
    private String mModule;
    private String mName;

    public void setModule(String value) {
        this.mModule = value;
    }

    public void setName(String value) {
        this.mName = value;
    }

    public void setAttribute(String value) {
        this.mAttribute = value;
    }

    public String getModule() {
        return this.mModule;
    }

    public String getName() {
        return this.mName;
    }

    public String getAttribute() {
        return this.mAttribute;
    }

    public String getListInfo() {
        String str = this.mModule + "," + this.mName + "," + this.mAttribute;
        this.mListInfo = str;
        return str;
    }
}
