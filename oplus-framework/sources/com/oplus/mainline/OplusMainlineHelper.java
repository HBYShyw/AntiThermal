package com.oplus.mainline;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Slog;
import com.oplus.romupdate.RomUpdateObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Calendar;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class OplusMainlineHelper {
    public static final String COLUMN_NAME_1 = "version";
    public static final String COLUMN_NAME_2 = "xml";
    public static final String CONFIG_FILE_PATH = "data/oplus/os/config/sys_mainline_limit.xml";
    public static final Uri CONTENT_URI_WHITE_LIST = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");
    private static final int DATE_INDEX_DAY = 2;
    private static final int DATE_INDEX_MONTH = 1;
    private static final int DATE_INDEX_YEAR = 0;
    private static final int DATE_LENGTH = 3;
    private static final String DATE_SPLIT_TAG = "-";
    public static final String FILTER_NAME = "sys_mainline_limit";
    private static final String FORBIDDEN_LIMIT_TAG_NAME = "forbidden_limit";
    private static final long MAX_LIMIT = 5184000000L;
    public static final String OPLUS_COMPONENT_SAFE_PERMISSION = "oplus.permission.OPLUS_COMPONENT_SAFE";
    private static final String TAG = "OplusMainlineHelper";
    private static volatile OplusMainlineHelper sHelper;
    private Context mContext;
    private long mLimitDateInMillis = 0;

    private OplusMainlineHelper() {
    }

    public static OplusMainlineHelper getInstance() {
        if (sHelper == null) {
            synchronized (OplusMainlineHelper.class) {
                if (sHelper == null) {
                    sHelper = new OplusMainlineHelper();
                }
            }
        }
        return sHelper;
    }

    public void init(Context context) {
        Slog.d(TAG, "init");
        this.mContext = context;
        readConfigFromFile();
    }

    public void initUpdateBroadcastReceiver() {
        RomUpdateObserver.getInstance().register(FILTER_NAME, new RomUpdateObserver.OnReceiveListener() { // from class: com.oplus.mainline.OplusMainlineHelper.1
            public void onReceive(Context context) {
                OplusMainlineHelper.this.dealConfigFromProvider();
                if (OplusMainlineHelper.this.isMainlineLimited()) {
                    OplusMainlineHelper.this.abandonStagedApex();
                }
            }
        });
    }

    public boolean isMainlineLimited() {
        boolean res = System.currentTimeMillis() <= this.mLimitDateInMillis;
        Slog.d(TAG, "isMainlineLimited = " + res);
        return res;
    }

    public void dealConfigFromProvider() {
        String configStr = getDataFromProvider();
        if (TextUtils.isEmpty(configStr)) {
            Slog.d(TAG, "config str is null");
        } else {
            parseConfigFromXML(configStr);
            saveConfigToFile(configStr, CONFIG_FILE_PATH);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void abandonStagedApex() {
        PackageManager packageManager = this.mContext.getPackageManager();
        PackageInstaller installer = packageManager.getPackageInstaller();
        List<PackageInstaller.SessionInfo> sessionInfoList = installer.getActiveStagedSessions();
        for (PackageInstaller.SessionInfo sessionInfo : sessionInfoList) {
            if ("com.android.vending".equals(sessionInfo.installerPackageName)) {
                if ((sessionInfo.installFlags & 131072) != 0) {
                    installer.abandonSession(sessionInfo.getSessionId());
                    Slog.d(TAG, "abandon staged apex session = " + sessionInfo.appPackageName);
                } else if (sessionInfo.childSessionIds != null && sessionInfo.childSessionIds.length > 0) {
                    int[] iArr = sessionInfo.childSessionIds;
                    int length = iArr.length;
                    int i = 0;
                    while (true) {
                        if (i < length) {
                            int id = iArr[i];
                            PackageInstaller.SessionInfo childInfo = installer.getSessionInfo(id);
                            if ((childInfo.installFlags & 131072) == 0) {
                                i++;
                            } else {
                                installer.abandonSession(sessionInfo.getSessionId());
                                Slog.d(TAG, "abandon staged session with apex children = " + sessionInfo.appPackageName);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private String getDataFromProvider() {
        String returnStr = null;
        String[] projection = {"version", "xml"};
        Context context = this.mContext;
        if (context == null) {
            Slog.w(TAG, "null context, can not read rus db");
            return null;
        }
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = CONTENT_URI_WHITE_LIST;
            ContentProviderClient cpc = contentResolver.acquireUnstableContentProviderClient(uri);
            try {
                if (cpc != null) {
                    try {
                        Cursor cursor = cpc.query(uri, projection, "filtername=\"sys_mainline_limit\"", null, null);
                        if (cursor != null) {
                            try {
                                if (cursor.getCount() > 0) {
                                    int versioncolumnIndex = cursor.getColumnIndex("version");
                                    int xmlcolumnIndex = cursor.getColumnIndex("xml");
                                    cursor.moveToNext();
                                    int configVersion = cursor.getInt(versioncolumnIndex);
                                    returnStr = cursor.getString(xmlcolumnIndex);
                                    Slog.d(TAG, "config updated, version = " + configVersion);
                                }
                            } catch (Throwable th) {
                                if (cursor != null) {
                                    try {
                                        cursor.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                }
                                throw th;
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    } catch (Exception e) {
                        Slog.e(TAG, "We can not get data from provider, because of " + e);
                        if (cpc != null) {
                            cpc.close();
                        }
                        return null;
                    }
                }
                if (cpc != null) {
                    cpc.close();
                }
                return returnStr;
            } finally {
            }
        } catch (Exception e2) {
            Slog.e(TAG, "We can not get data from provider, because of " + e2);
            return null;
        }
    }

    private String readStringFromFile(File file) {
        StringBuilder buffer = new StringBuilder();
        if (file == null || !file.exists()) {
            Slog.e(TAG, "file is null or not exists.");
            return buffer.toString();
        }
        try {
            InputStream is = new FileInputStream(file);
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    buffer.append(line);
                }
                is.close();
            } finally {
            }
        } catch (Exception e) {
            Slog.e(TAG, e.getMessage());
        }
        return buffer.toString();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0027. Please report as an issue. */
    private void parseConfigFromXML(String content) {
        int type;
        if (TextUtils.isEmpty(content)) {
            Slog.e(TAG, "content is null.");
            return;
        }
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(content));
            parser.nextTag();
            do {
                type = parser.next();
                switch (type) {
                    case 2:
                        if (!parser.getName().equals(FORBIDDEN_LIMIT_TAG_NAME)) {
                            break;
                        } else {
                            String str = parser.nextText();
                            if (!TextUtils.isEmpty(str)) {
                                String[] dateArray = str.split(DATE_SPLIT_TAG);
                                if (dateArray.length != 3) {
                                    Slog.e(TAG, "wrong date format");
                                    break;
                                } else {
                                    Calendar date = Calendar.getInstance();
                                    try {
                                        date.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]) - 1, Integer.parseInt(dateArray[2]));
                                        this.mLimitDateInMillis = Math.min(date.getTimeInMillis(), System.currentTimeMillis() + MAX_LIMIT);
                                        break;
                                    } catch (NumberFormatException e) {
                                        Slog.e(TAG, "wrong date value");
                                        break;
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                }
            } while (type != 1);
        } catch (Exception e2) {
            Slog.e(TAG, e2.getMessage());
        }
    }

    public void readConfigFromFile() {
        File file = new File(CONFIG_FILE_PATH);
        if (!file.exists()) {
            Slog.d(TAG, "config file not exists.");
        } else {
            Slog.d(TAG, "config file is exists.");
            parseConfigFromXML(readStringFromFile(file));
        }
    }

    public boolean saveConfigToFile(String content, String filePath) {
        if (TextUtils.isEmpty(content)) {
            Slog.d(TAG, "content is null.");
            return false;
        }
        File file = new File(filePath);
        File parent = new File(file.getParent());
        if (!parent.isDirectory()) {
            parent.mkdirs();
        }
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            try {
                outStream.write(content.getBytes());
                Slog.d(TAG, "saveConfigToFile done.");
                outStream.close();
                return true;
            } finally {
            }
        } catch (Exception e) {
            Slog.e(TAG, e.getMessage());
            return false;
        }
    }
}
