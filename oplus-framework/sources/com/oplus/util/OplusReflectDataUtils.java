package com.oplus.util;

import android.app.OplusActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.FileObserver;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class OplusReflectDataUtils {
    private static final String OPLUS_DIRECT_CONFIG_DIR = "/data/oplus/oplusos/oplusdirect";
    private static final String OPLUS_DIRECT_CONFIG_FILE_PATH = "/data/oplus/oplusos/oplusdirect/sys_direct_widget_config_list.xml";
    private static final String TAG = "OplusReflectDataUtils";
    private static final String TAG_ENABLE = "reflect_enable";
    private static final String TAG_RELECT_ATTR_CLASS = "className";
    private static final String TAG_RELECT_ATTR_FIELD = "field";
    private static final String TAG_RELECT_ATTR_LEVEL = "fieldLevel";
    private static final String TAG_RELECT_ATTR_PACKAGE = "packageName";
    private static final String TAG_RELECT_ATTR_VERSION = "versionCode";
    private static final String TAG_RELECT_WIDGET = "reflect_widget";
    private static OplusReflectData mReflectData = null;
    private static OplusReflectDataUtils mReflectUtils = null;
    private boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private FileObserverPolicy mReflectDataFileObserver = null;
    private final Object mAccidentallyReflectLock = new Object();
    private boolean mReflectEnable = true;
    private boolean mHasInit = false;

    private OplusReflectDataUtils() {
    }

    public static OplusReflectDataUtils getInstance() {
        if (mReflectUtils == null) {
            mReflectUtils = new OplusReflectDataUtils();
        }
        return mReflectUtils;
    }

    public void init() {
        initDir();
        initFileObserver();
        if (mReflectData == null) {
            mReflectData = new OplusReflectData();
        }
        synchronized (this.mAccidentallyReflectLock) {
            readConfigFile();
        }
    }

    private OplusReflectData getInitData(Context context) {
        if (!this.mHasInit) {
            this.mHasInit = true;
            initData();
            String packageName = context.getPackageName();
            int versionCode = -1;
            try {
                versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                mReflectData = null;
            }
            if (mReflectData != null && !TextUtils.isEmpty(packageName) && versionCode > 0) {
                mReflectData.initList(packageName, versionCode);
            } else {
                OplusReflectData oplusReflectData = mReflectData;
                if (oplusReflectData != null) {
                    oplusReflectData.setReflectEnable(false);
                }
            }
        }
        return mReflectData;
    }

    public OplusReflectData getData() {
        if (mReflectData == null) {
            mReflectData = new OplusReflectData();
        }
        return mReflectData;
    }

    private void initDir() {
        File directDir = new File(OPLUS_DIRECT_CONFIG_DIR);
        File directConfigFile = new File(OPLUS_DIRECT_CONFIG_FILE_PATH);
        try {
            if (!directDir.exists()) {
                directDir.mkdirs();
            }
            if (!directConfigFile.exists()) {
                directConfigFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeModFile(OPLUS_DIRECT_CONFIG_FILE_PATH);
    }

    private void changeModFile(String fileName) {
        try {
            File file = new File(fileName);
            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_WRITE);
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_WRITE);
            Path path = Paths.get(file.getAbsolutePath(), new String[0]);
            Files.setPosixFilePermissions(path, perms);
        } catch (Exception e) {
            Log.w(TAG, " " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readConfigFile() {
        int type;
        File file = new File(OPLUS_DIRECT_CONFIG_FILE_PATH);
        if (!file.exists()) {
            return;
        }
        OplusReflectData oplusReflectData = mReflectData;
        if (oplusReflectData == null) {
            mReflectData = new OplusReflectData();
        } else {
            oplusReflectData.clearList();
        }
        FileInputStream stream = null;
        try {
            try {
                try {
                    stream = new FileInputStream(file);
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(stream, null);
                    do {
                        type = parser.next();
                        if (type == 2) {
                            String tag = parser.getName();
                            if (TAG_ENABLE.equals(tag)) {
                                String reflectEnable = parser.nextText();
                                if ("true".equalsIgnoreCase(reflectEnable)) {
                                    this.mReflectEnable = true;
                                } else if ("false".equalsIgnoreCase(reflectEnable)) {
                                    this.mReflectEnable = false;
                                }
                                mReflectData.setReflectEnable(this.mReflectEnable);
                            } else if (TAG_RELECT_WIDGET.equals(tag)) {
                                OplusReflectWidget reflectWidget = new OplusReflectWidget();
                                int attrNum = parser.getAttributeCount();
                                for (int i = 0; i < attrNum; i++) {
                                    String name = parser.getAttributeName(i);
                                    String value = parser.getAttributeValue(i);
                                    if ("packageName".equals(name)) {
                                        reflectWidget.setPackageName(value);
                                    } else if (TAG_RELECT_ATTR_VERSION.equals(name)) {
                                        reflectWidget.setVersionCode(Integer.parseInt(value));
                                    } else if (TAG_RELECT_ATTR_CLASS.equals(name)) {
                                        reflectWidget.setClassName(value);
                                    } else if (TAG_RELECT_ATTR_LEVEL.equals(name)) {
                                        reflectWidget.setFieldLevel(Integer.parseInt(value));
                                    } else if (TAG_RELECT_ATTR_FIELD.equals(name)) {
                                        reflectWidget.setField(value);
                                    }
                                }
                                mReflectData.addReflectWidget(reflectWidget);
                            }
                        }
                    } while (type != 1);
                    stream.close();
                } catch (Throwable th) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    private void initFileObserver() {
        FileObserverPolicy fileObserverPolicy = new FileObserverPolicy(OPLUS_DIRECT_CONFIG_FILE_PATH);
        this.mReflectDataFileObserver = fileObserverPolicy;
        fileObserverPolicy.startWatching();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class FileObserverPolicy extends FileObserver {
        private String focusPath;

        public FileObserverPolicy(String path) {
            super(path, 8);
            this.focusPath = path;
        }

        @Override // android.os.FileObserver
        public void onEvent(int event, String path) {
            if (event == 8 && this.focusPath.equals(OplusReflectDataUtils.OPLUS_DIRECT_CONFIG_FILE_PATH)) {
                synchronized (OplusReflectDataUtils.this.mAccidentallyReflectLock) {
                    OplusReflectDataUtils.this.readConfigFile();
                }
            }
        }
    }

    public void initData() {
        try {
            OplusActivityManager mOplusActivityManager = new OplusActivityManager();
            mReflectData = mOplusActivityManager.getReflectData();
        } catch (RemoteException e) {
            mReflectData = null;
            Log.e(TAG, "init data error , " + e);
        }
    }

    public Field getTextField(Context context, Class<? extends View> viewClass) {
        OplusReflectWidget reflectWidget;
        Field textField = null;
        OplusReflectData reflectData = getInitData(context);
        if (reflectData != null && reflectData.isReflectEnable() && (reflectWidget = reflectData.findWidget(context, context.getPackageName(), viewClass.getName())) != null) {
            try {
                int level = reflectWidget.getFieldLevel();
                String fieldName = reflectWidget.getField();
                if (level == 0) {
                    textField = viewClass.getDeclaredField(fieldName);
                } else if (level == 1) {
                    textField = viewClass.getSuperclass().getDeclaredField(fieldName);
                } else if (level == 2) {
                    textField = viewClass.getSuperclass().getSuperclass().getDeclaredField(fieldName);
                }
                if (textField != null) {
                    textField.setAccessible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return textField;
    }
}
