package com.oplus.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.romupdate.RomUpdateObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* loaded from: classes.dex */
public class RomUpdateHelper {
    public static final String BROADCAST_ACTION_ROM_UPDATE_CONFIG_SUCCES = "oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS";
    private static final String COLUMN_NAME_1 = "version";
    private static final String COLUMN_NAME_2 = "xml";
    private static final String OPLUS_COMPONENT_SAFE_PERMISSION = "oplus.permission.OPLUS_COMPONENT_SAFE";
    public static final String ROM_UPDATE_CONFIG_LIST = "ROM_UPDATE_CONFIG_LIST";
    public static final String TAG = "RomUpdateHelper";
    public Context mContext;
    private String mDataFilePath;
    private String mFilterName;
    private String mSystemFilePath;
    private UpdateInfo mUpdateInfo1;
    private UpdateInfo mUpdateInfo2;
    private boolean which2use = true;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final Uri CONTENT_URI_WHITE_LIST = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class UpdateInfo {
        protected long mVersion = -1;

        /* JADX INFO: Access modifiers changed from: protected */
        public UpdateInfo() {
        }

        public void parseContentFromXML(String content) {
        }

        public boolean clone(UpdateInfo other) {
            return false;
        }

        public boolean insert(int type, String verifyStr) {
            return false;
        }

        public void clear() {
        }

        public void dump() {
        }

        public long getVersion() {
            return this.mVersion;
        }

        public boolean updateToLowerVersion(String newContent) {
            return false;
        }
    }

    public RomUpdateHelper(Context context, String filterName, String systemFile, String dataFile) {
        this.mFilterName = "";
        this.mSystemFilePath = "";
        this.mDataFilePath = "";
        this.mContext = null;
        this.mContext = context;
        this.mFilterName = filterName;
        this.mSystemFilePath = systemFile;
        this.mDataFilePath = dataFile;
    }

    public void init() {
        if (this.mDataFilePath == null || this.mSystemFilePath == null) {
            return;
        }
        File file = new File(this.mDataFilePath);
        if (!file.exists()) {
            file = new File(this.mSystemFilePath);
            if (!file.exists()) {
                return;
            }
        }
        parseContentFromXML(readFromFile(file));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setUpdateInfo(UpdateInfo updateInfo1, UpdateInfo updateInfo2) {
        this.mUpdateInfo1 = updateInfo1;
        this.mUpdateInfo2 = updateInfo2;
    }

    public void initUpdateBroadcastReceiver() {
        if (TextUtils.isEmpty(this.mFilterName)) {
            return;
        }
        RomUpdateObserver.getInstance().register(this.mFilterName, new RomUpdateObserver.OnReceiveListener() { // from class: com.oplus.util.RomUpdateHelper.1
            public void onReceive(Context context) {
                try {
                    if (RomUpdateHelper.DEBUG) {
                        Log.d(RomUpdateHelper.TAG, " onReceive " + RomUpdateHelper.this.getFilterName());
                    }
                    RomUpdateHelper.this.getUpdateFromProvider();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UpdateInfo getUpdateInfo(boolean b) {
        return b ? this.which2use ? this.mUpdateInfo1 : this.mUpdateInfo2 : this.which2use ? this.mUpdateInfo2 : this.mUpdateInfo1;
    }

    private void setFlip() {
        this.which2use = !this.which2use;
    }

    private boolean saveToFile(String content, String filePath) {
        try {
            File file = new File(filePath);
            File parent = new File(file.getParent());
            if (!parent.isDirectory()) {
                parent.mkdirs();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(content.getBytes());
            outStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFilterName() {
        return this.mFilterName;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0075, code lost:
    
        if (r1 != null) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0098, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0094, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0092, code lost:
    
        if (r1 == null) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getDataFromProvider() {
        Cursor cursor = null;
        String returnStr = null;
        String[] projection = {"version", "xml"};
        try {
            try {
                Context context = this.mContext;
                if (context == null) {
                    if (0 != 0) {
                        cursor.close();
                    }
                    return null;
                }
                cursor = context.getContentResolver().query(CONTENT_URI_WHITE_LIST, projection, "filtername=\"" + this.mFilterName + "\"", null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    int versioncolumnIndex = cursor.getColumnIndex("version");
                    int xmlcolumnIndex = cursor.getColumnIndex("xml");
                    cursor.moveToNext();
                    int configVersion = cursor.getInt(versioncolumnIndex);
                    returnStr = cursor.getString(xmlcolumnIndex);
                    Log.d(TAG, "White List updated, version = " + configVersion);
                }
            } catch (Exception e) {
                Log.w(TAG, "We can not get white list data from provider, because of " + e);
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public String readFromFile(File path) {
        if (path == null) {
            return "";
        }
        InputStream is = null;
        try {
            try {
                try {
                    try {
                        is = new FileInputStream(path);
                        BufferedReader in = new BufferedReader(new InputStreamReader(is));
                        StringBuffer buffer = new StringBuffer();
                        while (true) {
                            String line = in.readLine();
                            if (line == null) {
                                break;
                            }
                            buffer.append(line);
                        }
                        String stringBuffer = buffer.toString();
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return stringBuffer;
                    } catch (FileNotFoundException e2) {
                        e2.printStackTrace();
                        if (is == null) {
                            return null;
                        }
                        is.close();
                        return null;
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    if (is == null) {
                        return null;
                    }
                    is.close();
                    return null;
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException e5) {
            e5.printStackTrace();
            return null;
        }
    }

    public void parseContentFromXML(String content) {
        if (getUpdateInfo(true) != null) {
            getUpdateInfo(true).parseContentFromXML(content);
        }
    }

    public void getUpdateFromProvider() {
        try {
            String tmp = getDataFromProvider();
            if (tmp == null) {
                if (DEBUG) {
                    Log.d(TAG, " getUpdateFromProvider data is null " + getFilterName());
                }
            } else {
                if (updateToLowerVersion(tmp)) {
                    return;
                }
                saveToFile(tmp, this.mDataFilePath);
                if (getUpdateInfo(false) == null) {
                    return;
                }
                getUpdateInfo(false).parseContentFromXML(tmp);
                setFlip();
                getUpdateInfo(false).clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean updateToLowerVersion(String newContent) {
        UpdateInfo updateInfo = getUpdateInfo(true);
        if (updateInfo != null && updateInfo.updateToLowerVersion(newContent)) {
            Log.d(TAG, "updateToLowerVersion true, " + updateInfo.hashCode());
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean insertValueInList(int type, String verifyStr) {
        if (getUpdateInfo(false).clone(getUpdateInfo(true)) && getUpdateInfo(false).insert(type, verifyStr)) {
            setFlip();
            getUpdateInfo(false).clear();
            return true;
        }
        log("Failed to insert!");
        return false;
    }

    public void dump() {
        log("which2use = " + this.which2use);
        this.mUpdateInfo1.dump();
        this.mUpdateInfo2.dump();
    }

    public void log(String log) {
        if (DEBUG) {
            Log.d(TAG, log);
        }
    }

    public void log(String log, Exception e) {
        if (DEBUG) {
            Log.d(TAG, log);
        }
    }
}
