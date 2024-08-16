package com.oplus.romupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class RomUpdateHelper {
    private static final String BROADCAST_ACTION_ROM_UPDATE_CONFIG_SUCCES = "oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS";
    private static final String COLUMN_NAME_CONTENT = "xml";
    private static final String COLUMN_NAME_VERSION = "version";
    private static final String COMPONENT_SAFE_PERMISSION = "oplus.permission.OPLUS_COMPONENT_SAFE";
    private static final String ROM_UPDATE_CONFIG_LIST = "ROM_UPDATE_CONFIG_LIST";
    private static final String TAG = "RomUpdateHelper";
    private Context mContext;
    private String mFilterName;
    private Handler mHandler;
    private UpdateInfoListener mListener;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final Uri CONTENT_URI_WHITE_LIST = Uri.parse("content://com.nearme.romupdate.provider.db/update_list");

    /* loaded from: classes.dex */
    public interface UpdateInfoListener {
        void onUpdateInfoChanged(String str);
    }

    public RomUpdateHelper(Context context, String filterName) {
        if (context == null || filterName == null) {
            throw new IllegalArgumentException("The parameters must not be null");
        }
        this.mContext = context;
        this.mFilterName = filterName;
        this.mHandler = new Handler(context.getMainLooper());
    }

    public void setUpdateInfoListener(UpdateInfoListener listener) {
        this.mListener = listener;
    }

    public void registerUpdateBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.oplus.romupdate.RomUpdateHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (RomUpdateHelper.DEBUG) {
                    Log.d("RomUpdateHelper", "Filter = " + RomUpdateHelper.this.mFilterName + ", onReceive intent = " + intent);
                }
                if (intent != null) {
                    try {
                        ArrayList<String> configs = intent.getStringArrayListExtra("ROM_UPDATE_CONFIG_LIST");
                        if (configs != null && configs.contains(RomUpdateHelper.this.mFilterName)) {
                            Pair<Integer, String> info = RomUpdateHelper.this.getDataFromProvider();
                            final String content = (String) info.second;
                            if (content != null && RomUpdateHelper.this.mListener != null) {
                                RomUpdateHelper.this.mHandler.post(new Runnable() { // from class: com.oplus.romupdate.RomUpdateHelper.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (RomUpdateHelper.this.mListener != null) {
                                            RomUpdateHelper.this.mListener.onUpdateInfoChanged(content);
                                        }
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        Log.e("RomUpdateHelper", "onReceive" + RomUpdateHelper.this.mFilterName, e);
                    }
                }
            }
        }, filter, "oplus.permission.OPLUS_COMPONENT_SAFE", null);
    }

    public Pair<Integer, String> getDataFromProvider() {
        int configVersion = -1;
        String content = null;
        String[] projection = {"version", "xml"};
        try {
            Cursor cursor = this.mContext.getContentResolver().query(CONTENT_URI_WHITE_LIST, projection, "filtername=\"" + this.mFilterName + "\"", null, null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) {
                        int versionColumnIndex = cursor.getColumnIndex("version");
                        int xmlColumnIndex = cursor.getColumnIndex("xml");
                        if (cursor.moveToNext()) {
                            configVersion = cursor.getInt(versionColumnIndex);
                            content = cursor.getString(xmlColumnIndex);
                        }
                    }
                } finally {
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("RomUpdateHelper", "getDataFromProvider", e);
        }
        return new Pair<>(Integer.valueOf(configVersion), content);
    }
}
