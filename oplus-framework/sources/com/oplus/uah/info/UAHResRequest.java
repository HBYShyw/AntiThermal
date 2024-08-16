package com.oplus.uah.info;

import android.os.Bundle;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class UAHResRequest {
    private Bundle mInfo;
    private ArrayList<UAHResourceInfo> mList;
    private int mTimeout;

    public UAHResRequest(int timeout, ArrayList<UAHResourceInfo> list) {
        this.mTimeout = -1;
        this.mInfo = null;
        this.mTimeout = timeout;
        this.mList = list;
    }

    public UAHResRequest(Bundle info) {
        this.mTimeout = -1;
        this.mInfo = null;
        this.mInfo = info;
    }

    public ArrayList<UAHResourceInfo> getList() {
        return this.mList;
    }

    public int getTimeout() {
        return this.mTimeout;
    }

    public String toString() {
        return "UahEventRequest{resList='" + this.mList + "', timeout=" + this.mTimeout + ", info=" + this.mInfo + '}';
    }
}
