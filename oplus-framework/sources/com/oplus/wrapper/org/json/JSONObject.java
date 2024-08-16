package com.oplus.wrapper.org.json;

import java.util.Set;

/* loaded from: classes.dex */
public class JSONObject {
    private final org.json.JSONObject mJSONObject;

    public JSONObject(org.json.JSONObject jsonObject) {
        this.mJSONObject = jsonObject;
    }

    public Set<String> keySet() {
        return this.mJSONObject.keySet();
    }
}
