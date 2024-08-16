package com.oplus.uah.info;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class UAHEventRequest {
    private int mEventId;
    private ArrayList<UAHResourceInfo> mList;
    private String mSceneName;
    private int mTimeout;

    public UAHEventRequest(int eventId, String sceneName, int timeout, ArrayList<UAHResourceInfo> list) {
        this.mEventId = -1;
        this.mSceneName = "";
        this.mTimeout = -1;
        this.mEventId = eventId;
        this.mSceneName = sceneName;
        this.mTimeout = timeout;
        this.mList = list;
    }

    public int getEventId() {
        return this.mEventId;
    }

    public String getSceneName() {
        return this.mSceneName;
    }

    public int getTimeout() {
        return this.mTimeout;
    }

    public ArrayList<UAHResourceInfo> getList() {
        return this.mList;
    }

    public String toString() {
        return "UahEventRequest{eventId='" + this.mEventId + "', sceneName='" + this.mSceneName + "', timeout=" + this.mTimeout + "', list=" + this.mList.toString() + '}';
    }
}
