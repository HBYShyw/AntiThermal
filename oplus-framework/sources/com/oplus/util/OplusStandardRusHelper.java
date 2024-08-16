package com.oplus.util;

import android.content.Context;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.oplus.util.RomUpdateHelper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public class OplusStandardRusHelper extends RomUpdateHelper {
    public static final String BUILD_SDK_NAME = "build_sdk";
    private static final int CONST_FOUR = 4;
    private static final int CONST_THREE = 3;
    private static final int CONST_ZERO = 0;
    private static final int LETTER_NUM = 26;
    public static final String OS_VERSION_NAME = "os_version";
    private static final String TAG = "OplusStandardRusHelper";
    public static final String VERSION_NAME = "version";
    private static final HashMap<String, WeakReference<OplusStandardRusHelper>> mRefMap = new HashMap<>();
    private OplusRusHelperCallback mCallback;
    protected final Context mContext;
    private String mSystemFile;

    /* loaded from: classes.dex */
    public enum MatchMode {
        MODE_NORMAL_MODE_MATCH,
        MODE_CONTAIN_MODE_MATCH
    }

    /* loaded from: classes.dex */
    public abstract class OplusRusHelperCallback {
        public OplusRusHelperCallback() {
        }

        public void onUpdate() {
        }
    }

    public OplusStandardRusHelper(Context context, String filterName, String systemFile, String dataFile) {
        super(context, filterName, systemFile, dataFile);
        this.mContext = context;
        this.mCallback = new OplusRusHelperCallback() { // from class: com.oplus.util.OplusStandardRusHelper.1
        };
        this.mSystemFile = systemFile;
        recordReferences(filterName, this);
        setUpdateInfo(new StandardUpdateInfo(), new StandardUpdateInfo());
        try {
            init();
        } catch (Exception e) {
        }
    }

    @Override // com.oplus.util.RomUpdateHelper
    public void init() {
        super.init();
        String content = readFromFile(new File(this.mSystemFile));
        StandardUpdateInfo tempInfo = (StandardUpdateInfo) getUpdateInfo(false);
        StandardUpdateInfo currentInfo = (StandardUpdateInfo) getUpdateInfo(true);
        tempInfo.parseContentFromXML(content);
        if (!checkValidityForData(tempInfo, currentInfo)) {
            currentInfo.clone(tempInfo);
        } else {
            tempInfo.clear();
        }
    }

    public void setRusCallback(OplusRusHelperCallback cb) {
        this.mCallback = cb;
    }

    private boolean checkValidityForData(StandardUpdateInfo systemInfo, StandardUpdateInfo dataInfo) {
        if (systemInfo.getOsVersion() != 0 && systemInfo.getOsVersion() != dataInfo.getOsVersion()) {
            Slog.w(TAG, "os version not match, data invalid, system:" + systemInfo.getOsVersion() + " data:" + dataInfo.getOsVersion());
            return false;
        }
        if (systemInfo.getBuildSdk() != 0 && systemInfo.getBuildSdk() != dataInfo.getBuildSdk()) {
            Slog.w(TAG, "build sdk not match, data invalid, system:" + systemInfo.getBuildSdk() + " data:" + dataInfo.getBuildSdk());
            return false;
        }
        if (systemInfo.getVersion() > dataInfo.getVersion()) {
            Slog.w(TAG, "lower data version, data invalid, system:" + systemInfo.getVersion() + " data:" + dataInfo.getVersion());
            return false;
        }
        return true;
    }

    private void recordReferences(String filterName, OplusStandardRusHelper self) {
        HashMap<String, WeakReference<OplusStandardRusHelper>> hashMap = mRefMap;
        if (hashMap.containsKey(filterName)) {
            if (hashMap.get(filterName).get() != null) {
                Slog.w(TAG, "multible RusHelper for same type:" + filterName);
            }
            hashMap.replace(filterName, new WeakReference<>(self));
        }
        hashMap.put(filterName, new WeakReference<>(self));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class StandardUpdateInfo extends RomUpdateHelper.UpdateInfo {
        private int mBuildSdk;
        private int mOsVersion;
        private final SparseArray<ArrayList<String>> mWhiteList;

        public StandardUpdateInfo() {
            super();
            this.mWhiteList = new SparseArray<>();
            this.mOsVersion = 0;
            this.mBuildSdk = 0;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Failed to find 'out' block for switch in B:10:0x002b. Please report as an issue. */
        @Override // com.oplus.util.RomUpdateHelper.UpdateInfo
        public void parseContentFromXML(String content) {
            if (content == null) {
                return;
            }
            FileReader xmlReader = null;
            StringReader strReader = null;
            this.mWhiteList.clear();
            this.mVersion = -1L;
            this.mOsVersion = 0;
            this.mBuildSdk = 0;
            try {
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    strReader = new StringReader(content);
                    parser.setInput(strReader);
                    for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                        switch (eventType) {
                            case 0:
                            case 1:
                            default:
                            case 2:
                                char[] typeChar = parser.getName().toCharArray();
                                if (typeChar.length > 3) {
                                    parser.next();
                                    updateConfigVersion(String.valueOf(typeChar), parser.getText());
                                    updateOsVersion(String.valueOf(typeChar), parser.getText());
                                    updateSdkVersion(String.valueOf(typeChar), parser.getText());
                                } else {
                                    int type = char2int(typeChar);
                                    parser.next();
                                    if (type >= 0) {
                                        ArrayList<String> tmp = this.mWhiteList.get(type);
                                        if (tmp == null) {
                                            ArrayList<String> tmp2 = new ArrayList<>();
                                            tmp2.add(parser.getText());
                                            this.mWhiteList.put(type, tmp2);
                                        } else {
                                            tmp.add(parser.getText());
                                        }
                                    }
                                }
                        }
                    }
                    if (0 != 0) {
                        try {
                            xmlReader.close();
                        } catch (IOException e) {
                            OplusStandardRusHelper.this.log("Got execption close permReader.", e);
                        }
                    }
                    strReader.close();
                    OplusStandardRusHelper.this.mCallback.onUpdate();
                } catch (IOException e2) {
                    OplusStandardRusHelper.this.log("Got execption parsing permissions.", e2);
                    if (0 != 0) {
                        try {
                            xmlReader.close();
                        } catch (IOException e3) {
                            OplusStandardRusHelper.this.log("Got execption close permReader.", e3);
                            return;
                        }
                    }
                    if (strReader != null) {
                        strReader.close();
                    }
                } catch (XmlPullParserException e4) {
                    OplusStandardRusHelper.this.log("Got execption parsing permissions.", e4);
                    if (0 != 0) {
                        try {
                            xmlReader.close();
                        } catch (IOException e5) {
                            OplusStandardRusHelper.this.log("Got execption close permReader.", e5);
                            return;
                        }
                    }
                    if (strReader != null) {
                        strReader.close();
                    }
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        xmlReader.close();
                    } catch (IOException e6) {
                        OplusStandardRusHelper.this.log("Got execption close permReader.", e6);
                        throw th;
                    }
                }
                if (strReader != null) {
                    strReader.close();
                }
                throw th;
            }
        }

        private void updateConfigVersion(String type, String value) {
            if ("version".equals(type)) {
                this.mVersion = Long.parseLong(value);
            }
        }

        private void updateOsVersion(String type, String value) {
            if (OplusStandardRusHelper.OS_VERSION_NAME.equals(type)) {
                this.mOsVersion = Integer.parseInt(value);
            }
        }

        private void updateSdkVersion(String type, String value) {
            if (OplusStandardRusHelper.BUILD_SDK_NAME.equals(type)) {
                this.mBuildSdk = Integer.parseInt(value);
            }
        }

        public int getOsVersion() {
            return this.mOsVersion;
        }

        public int getBuildSdk() {
            return this.mBuildSdk;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0028. Please report as an issue. */
        /* JADX WARN: Removed duplicated region for block: B:28:0x009a A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:58:0x00ab A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // com.oplus.util.RomUpdateHelper.UpdateInfo
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean updateToLowerVersion(String content) {
            String tagName;
            long version = -1;
            int osVersion = 0;
            int buildSdk = 0;
            if (content == null) {
                return true;
            }
            FileReader xmlReader = null;
            StringReader strReader = null;
            try {
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    strReader = new StringReader(content);
                    parser.setInput(strReader);
                    int eventType = parser.getEventType();
                    boolean foundVersion = false;
                    boolean foundOsVersion = false;
                    boolean foundBuildSdk = false;
                    for (int i = 1; eventType != i; i = 1) {
                        switch (eventType) {
                            case 0:
                                if (!foundVersion && foundOsVersion && foundBuildSdk) {
                                    if (0 != 0) {
                                        try {
                                            xmlReader.close();
                                        } catch (IOException e) {
                                            OplusStandardRusHelper.this.log("Got execption close permReader.", e);
                                        }
                                    }
                                    strReader.close();
                                    if (getOsVersion() == 0 && getOsVersion() > osVersion) {
                                        Slog.w(OplusStandardRusHelper.TAG, "lower os version, data invalid, current:" + getOsVersion() + " new:" + osVersion);
                                        return true;
                                    }
                                    if (getBuildSdk() == 0 && getBuildSdk() > buildSdk) {
                                        Slog.w(OplusStandardRusHelper.TAG, "lower build sdk, data invalid, current:" + getBuildSdk() + " new:" + buildSdk);
                                        return true;
                                    }
                                    if (getVersion() == -1 && getVersion() > version) {
                                        Slog.w(OplusStandardRusHelper.TAG, "lower version, data invalid, current:" + getVersion() + " new:" + version);
                                        return true;
                                    }
                                }
                                eventType = parser.next();
                                break;
                            case 1:
                            default:
                                if (!foundVersion) {
                                }
                                eventType = parser.next();
                                break;
                            case 2:
                                String tagName2 = parser.getName();
                                if (foundVersion) {
                                    tagName = tagName2;
                                } else {
                                    tagName = tagName2;
                                    if ("version".equals(tagName)) {
                                        parser.next();
                                        parser.getText();
                                        version = Long.parseLong(parser.getText());
                                        foundVersion = true;
                                    }
                                }
                                if (!foundOsVersion && OplusStandardRusHelper.OS_VERSION_NAME.equals(tagName)) {
                                    parser.next();
                                    parser.getText();
                                    osVersion = Integer.parseInt(parser.getText());
                                    foundOsVersion = true;
                                }
                                if (!foundBuildSdk && OplusStandardRusHelper.BUILD_SDK_NAME.equals(tagName)) {
                                    parser.next();
                                    parser.getText();
                                    buildSdk = Integer.parseInt(parser.getText());
                                    foundBuildSdk = true;
                                }
                                if (!foundVersion) {
                                }
                                eventType = parser.next();
                                break;
                            case 3:
                                if (!foundVersion) {
                                }
                                eventType = parser.next();
                                break;
                        }
                    }
                    if (0 != 0) {
                    }
                    strReader.close();
                    if (getOsVersion() == 0) {
                    }
                    if (getBuildSdk() == 0) {
                    }
                    return getVersion() == -1 ? false : false;
                } catch (IOException e2) {
                    OplusStandardRusHelper.this.log("Got execption parsing permissions.", e2);
                    if (0 != 0) {
                        try {
                            xmlReader.close();
                        } catch (IOException e3) {
                            OplusStandardRusHelper.this.log("Got execption close permReader.", e3);
                            return true;
                        }
                    }
                    if (strReader == null) {
                        return true;
                    }
                    strReader.close();
                    return true;
                } catch (XmlPullParserException e4) {
                    OplusStandardRusHelper.this.log("Got execption parsing permissions.", e4);
                    if (0 != 0) {
                        try {
                            xmlReader.close();
                        } catch (IOException e5) {
                            OplusStandardRusHelper.this.log("Got execption close permReader.", e5);
                            return true;
                        }
                    }
                    if (strReader == null) {
                        return true;
                    }
                    strReader.close();
                    return true;
                }
            } finally {
            }
        }

        @Override // com.oplus.util.RomUpdateHelper.UpdateInfo
        public boolean clone(RomUpdateHelper.UpdateInfo input) {
            StandardUpdateInfo tmp = (StandardUpdateInfo) input;
            SparseArray<ArrayList<String>> other = tmp.getAllList();
            if (other == null || other.size() == 0) {
                OplusStandardRusHelper.this.log("Source object is empty");
                return false;
            }
            this.mWhiteList.clear();
            this.mVersion = tmp.getVersion();
            this.mOsVersion = tmp.getOsVersion();
            this.mBuildSdk = tmp.getBuildSdk();
            for (int i = 0; i < other.size(); i++) {
                int key = other.keyAt(i);
                ArrayList<String> source = other.get(key);
                this.mWhiteList.put(key, (ArrayList) source.clone());
            }
            return true;
        }

        @Override // com.oplus.util.RomUpdateHelper.UpdateInfo
        public boolean insert(int type, String verifyStr) {
            ArrayList<String> tmp = this.mWhiteList.get(type);
            if (tmp != null) {
                tmp.add(verifyStr);
                return true;
            }
            return false;
        }

        @Override // com.oplus.util.RomUpdateHelper.UpdateInfo
        public void clear() {
            this.mWhiteList.clear();
            this.mVersion = -1L;
            this.mOsVersion = 0;
            this.mBuildSdk = 0;
        }

        private int char2int(char[] in) {
            int out = 0;
            if (in.length < 1) {
                return -1;
            }
            for (int n = 0; n < in.length; n++) {
                out = (int) (out + ((in[n] - 'a') * Math.pow(26.0d, (in.length - n) - 1)));
            }
            return out;
        }

        public String dumpToString() {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("UpdateInfo [" + hashCode() + ", version = " + getVersion() + "]\n");
            for (int i = 0; i < this.mWhiteList.size(); i++) {
                int key = this.mWhiteList.keyAt(i);
                strBuilder.append("type = " + key);
                ArrayList<String> obj = this.mWhiteList.get(key);
                strBuilder.append(", value = " + obj + "\n");
            }
            return strBuilder.toString();
        }

        public boolean isInWhiteList(int type, String verifyStr) {
            if (this.mWhiteList.indexOfKey(type) >= 0 && this.mWhiteList.get(type).contains(verifyStr)) {
                return true;
            }
            return false;
        }

        public ArrayList<String> getOneList(int type) {
            ArrayList<String> oneList = this.mWhiteList.get(type);
            if (oneList != null) {
                return (ArrayList) oneList.clone();
            }
            return null;
        }

        public SparseArray<ArrayList<String>> getAllList() {
            return this.mWhiteList.clone();
        }
    }

    public String dumpToString() {
        StandardUpdateInfo temp = (StandardUpdateInfo) getUpdateInfo(true);
        return temp.dumpToString();
    }

    public SparseArray<ArrayList<String>> getAllList() {
        return ((StandardUpdateInfo) getUpdateInfo(true)).getAllList();
    }

    public ArrayList<String> getOneList(int type) {
        return ((StandardUpdateInfo) getUpdateInfo(true)).getOneList(type);
    }

    public boolean isInWhiteList(int type, String verifyStr) {
        return isInWhiteList(type, verifyStr, false);
    }

    public boolean isInWhiteList(int type, String verifyStr, boolean containMode) {
        StandardUpdateInfo tempInfo;
        if (verifyStr == null || (tempInfo = (StandardUpdateInfo) getUpdateInfo(true)) == null) {
            return false;
        }
        if (containMode) {
            ArrayList<String> tmp = tempInfo.getOneList(type);
            if (tmp == null) {
                return false;
            }
            return isContained(tmp, verifyStr);
        }
        return tempInfo.isInWhiteList(type, verifyStr);
    }

    public static boolean isInWhiteList(String filterName, int type, String verifyStr) throws IllegalStateException {
        return isInWhiteList(filterName, type, verifyStr, MatchMode.MODE_NORMAL_MODE_MATCH);
    }

    public static boolean isInWhiteList(String filterName, int type, String verifyStr, MatchMode mode) throws IllegalStateException {
        HashMap<String, WeakReference<OplusStandardRusHelper>> hashMap = mRefMap;
        if (!hashMap.containsKey(filterName)) {
            throw new IllegalStateException(filterName + " Rushelper has not been initialized");
        }
        WeakReference<OplusStandardRusHelper> ref = hashMap.get(filterName);
        if (ref == null || ref.get() == null) {
            Slog.w(TAG, filterName + " ref may have been expired");
            hashMap.remove(filterName);
            return false;
        }
        switch (AnonymousClass2.$SwitchMap$com$oplus$util$OplusStandardRusHelper$MatchMode[mode.ordinal()]) {
            case 1:
                return ref.get().isInWhiteList(type, verifyStr, false);
            case 2:
                return ref.get().isInWhiteList(type, verifyStr, true);
            default:
                Slog.w(TAG, "Unknown mode");
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oplus.util.OplusStandardRusHelper$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$oplus$util$OplusStandardRusHelper$MatchMode;

        static {
            int[] iArr = new int[MatchMode.values().length];
            $SwitchMap$com$oplus$util$OplusStandardRusHelper$MatchMode = iArr;
            try {
                iArr[MatchMode.MODE_NORMAL_MODE_MATCH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$oplus$util$OplusStandardRusHelper$MatchMode[MatchMode.MODE_CONTAIN_MODE_MATCH.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    @Override // com.oplus.util.RomUpdateHelper
    public boolean insertValueInList(int type, String verifyStr) {
        return super.insertValueInList(type, verifyStr);
    }

    private boolean isContained(ArrayList<String> tmpList, String verifyStr) {
        for (int i = 0; i < tmpList.size(); i++) {
            if (verifyStr != null && (verifyStr.contains(tmpList.get(i)) || tmpList.get(i).contains(verifyStr))) {
                return true;
            }
        }
        return false;
    }
}
