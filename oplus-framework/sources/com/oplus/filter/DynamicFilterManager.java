package com.oplus.filter;

import android.content.Context;
import android.os.RemoteException;
import android.util.Slog;

/* loaded from: classes.dex */
public class DynamicFilterManager {
    public static final String FILTER_BRK_SEARCH_2_WAY = "brk_s2w";
    public static final String FILTER_DISABLE_MEM_INIT = "disable_meminit";
    public static final String FILTER_GL_OOM = "gl_oom";
    public static final String FILTER_GL_THREAD_UX = "gl_thread_ux";
    public static final String FILTER_OFB = "ofb";
    public static final String FILTER_OFB_APP_PARA = "ofb_app_para";
    public static final String FILTER_OFB_TASK_PARA = "ofb_task_para";
    public static final String FILTER_SCROLL_OPT = "scroll_opt";
    public static final String FILTER_SCROLL_OPT_BLACK = "scroll_black";
    public static final String FILTER_TPD = "tpd";
    public static final String FILTER_UI_FIRST_BLACK = "ui_first_black";
    public static final String SERVICE_NAME = "dynamic_filter";
    private static final String TAG = "DynamicFilterManager";
    private Context mContext;
    private IDynamicFilterService mService;

    public DynamicFilterManager(Context context, IDynamicFilterService service) {
        this.mContext = context;
        this.mService = service;
        if (service == null) {
            Slog.e(TAG, "DynamicFilterService was null!");
        }
    }

    public boolean hasFilter(String name) {
        IDynamicFilterService iDynamicFilterService = this.mService;
        if (iDynamicFilterService == null) {
            return false;
        }
        try {
            return iDynamicFilterService.hasFilter(name);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean inFilter(String name, String tag) {
        IDynamicFilterService iDynamicFilterService = this.mService;
        if (iDynamicFilterService == null) {
            return false;
        }
        try {
            return iDynamicFilterService.inFilter(name, tag);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void addToFilter(String name, String tag, String value) {
        IDynamicFilterService iDynamicFilterService = this.mService;
        if (iDynamicFilterService == null) {
            return;
        }
        try {
            iDynamicFilterService.addToFilter(name, tag, value);
        } catch (RemoteException e) {
        }
    }

    public void removeFromFilter(String name, String tag) {
        IDynamicFilterService iDynamicFilterService = this.mService;
        if (iDynamicFilterService == null) {
            return;
        }
        try {
            iDynamicFilterService.removeFromFilter(name, tag);
        } catch (RemoteException e) {
        }
    }

    public String getFilterTagValue(String name, String tag) {
        IDynamicFilterService iDynamicFilterService = this.mService;
        if (iDynamicFilterService == null) {
            return null;
        }
        try {
            return iDynamicFilterService.getFilterTagValue(name, tag);
        } catch (RemoteException e) {
            return null;
        }
    }
}
