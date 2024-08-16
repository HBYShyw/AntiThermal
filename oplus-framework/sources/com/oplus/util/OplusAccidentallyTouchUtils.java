package com.oplus.util;

import android.app.OplusActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.FileObserver;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class OplusAccidentallyTouchUtils {
    private static final String ACCIDENTALLYTOUCH_FILE = "/data/oplus/os/guardelf/sys_accidentally_touch_config_list.xml";
    private static final String ACCIDENTALLYTOUCH_PATH = "/data/oplus/os/guardelf";
    private static final int DENSITY_XXHIGH = 3;
    private static final int OFFSET_1 = 5;
    private static final int OFFSET_2 = 80;
    private static final String TAG = "OplusAccidentallyTouch";
    private static final String TAG_BEZEL = "bezel";
    private static final String TAG_BEZEL_AREA = "bezel_area";
    private static final String TAG_BEZEL_ENABLE = "bezel_enable";
    private static final String TAG_EDGE_ENABLE = "edge_enable";
    private static final String TAG_EDGE_PKG = "edge_pkg";
    private static final String TAG_EDGE_T = "edge_t";
    private static final String TAG_EDGE_T1 = "edge_t1";
    private static final String TAG_EDGE_T2 = "edge_t2";
    private static final String TAG_ENABLE = "enable_accidentallytouch";
    private static final String TAG_LEFT_OFFSET = "left_offset";
    private static final String TAG_POINT_LEFT_OFFSET = "point_left_offset";
    private static final String TAG_POINT_RIGHT_OFFSET = "point_right_offset";
    private static final String TAG_RIGHT_OFFSET = "right_offset";
    private int mActivePointerId;
    private Context mContext;
    private MotionEvent mExtraEvent;
    private boolean mNeedExtraEvent;
    private int mScrapPointerId;
    private String mXmlEdgeEnable;
    private String mXmlEdgeT;
    private String mXmlEdgeT1;
    private String mXmlEdgeT2;
    private String mXmlEnable;
    private String mXmlLeftOffset;
    private String mXmlPointLeftOffset;
    private String mXmlPointRightOffset;
    private String mXmlRightOffset;
    private static final boolean PROPERTY_ENABLE = SystemProperties.getBoolean("debug.accidentally.touch", true);
    private static OplusAccidentallyTouchData mTouchData = null;
    private static OplusAccidentallyTouchUtils mTouchUtils = null;
    private boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private FileObserverPolicy mAccidentallyTouchFileObserver = null;
    private final Object mAccidentallyTouchLock = new Object();
    private List<String> mXmlEdgeList = null;
    private List<String> mXmlSingleList = null;
    private List<String> mXmlMultiList = null;
    private List<String> mXmlWhiteList = null;
    private ArrayList<String> mBezelList = new ArrayList<>();
    private boolean mBezelEnable = true;
    private String mBezelArea = "7";
    private ArrayList<String> mDefaultList = new ArrayList<>(Arrays.asList("com.tencent.mm"));
    private boolean mEnableAccidentallyTouch = true;
    private int mLeftOffset = 5;
    private int mRightOffset = 5;
    private int mPointLeftOffset = 80;
    private int mPointRightOffset = 80;
    private boolean mEdgeEnable = true;
    private int mEdgeT = 10;
    private int mEdgeT1 = 10;
    private int mEdgeT2 = 30;
    private ArrayList<String> mEdgeList = new ArrayList<>();
    private boolean mIsEdgePkg = false;
    private ArrayList<String> mSingleList = new ArrayList<>();
    private ArrayList<String> mMultiList = new ArrayList<>();
    private ArrayList<String> mWhiteList = new ArrayList<>();
    private boolean mEnableSingleSence = true;
    private boolean mEnableMultiSence = true;
    private boolean mIsSingleSence = false;
    private boolean mIsMultiSence = false;
    private boolean mIsWhiteApp = false;
    private boolean mSmallScreenMode = false;
    private int mDisplayWidth = 1080;

    private OplusAccidentallyTouchUtils() {
    }

    public static OplusAccidentallyTouchUtils getInstance() {
        if (mTouchUtils == null) {
            mTouchUtils = new OplusAccidentallyTouchUtils();
        }
        return mTouchUtils;
    }

    public void init() {
        initDir();
        initFileObserver();
        if (mTouchData == null) {
            mTouchData = new OplusAccidentallyTouchData();
        }
        this.mXmlSingleList = mTouchData.getSingleTouchList();
        this.mXmlMultiList = mTouchData.getMultiTouchList();
        this.mXmlWhiteList = mTouchData.getTouchWhiteList();
        this.mXmlEdgeList = mTouchData.getEdgeList();
        synchronized (this.mAccidentallyTouchLock) {
            readConfigFile();
        }
    }

    public OplusAccidentallyTouchData getTouchData() {
        if (mTouchData == null) {
            mTouchData = new OplusAccidentallyTouchData();
        }
        return mTouchData;
    }

    private void initDir() {
        File dir = new File(ACCIDENTALLYTOUCH_PATH);
        File file = new File(ACCIDENTALLYTOUCH_FILE);
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeModFile(ACCIDENTALLYTOUCH_FILE);
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
        File file = new File(ACCIDENTALLYTOUCH_FILE);
        if (!file.exists()) {
            return;
        }
        this.mXmlSingleList.clear();
        this.mXmlMultiList.clear();
        this.mXmlWhiteList.clear();
        this.mXmlEdgeList.clear();
        this.mBezelList.clear();
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
                            if ("o".equals(tag)) {
                                String value = parser.getAttributeValue(null, "att");
                                if (this.DEBUG) {
                                    Log.d(TAG, "single-toush list : " + value);
                                }
                                if (value != null) {
                                    this.mXmlSingleList.add(value);
                                }
                            }
                            if ("p".equals(tag)) {
                                String value2 = parser.getAttributeValue(null, "att");
                                if (this.DEBUG) {
                                    Log.d(TAG, "multi-touch list : " + value2);
                                }
                                if (value2 != null) {
                                    this.mXmlMultiList.add(value2);
                                }
                            }
                            if ("w".equals(tag)) {
                                String value3 = parser.getAttributeValue(null, "att");
                                if (this.DEBUG) {
                                    Log.d(TAG, "white list : " + value3);
                                }
                                if (value3 != null) {
                                    this.mXmlWhiteList.add(value3);
                                }
                            }
                            if (TAG_BEZEL_ENABLE.equals(tag)) {
                                String value4 = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "bezel enable : " + value4);
                                }
                                if (value4 != null) {
                                    if ("true".equals(value4)) {
                                        this.mBezelEnable = true;
                                    } else {
                                        this.mBezelEnable = false;
                                    }
                                }
                            }
                            if (TAG_BEZEL.equals(tag)) {
                                String value5 = parser.getAttributeValue(null, "att");
                                if (this.DEBUG) {
                                    Log.d(TAG, "bezel list : " + value5);
                                }
                                if (value5 != null) {
                                    this.mBezelList.add(value5);
                                }
                            }
                            if (TAG_BEZEL_AREA.equals(tag)) {
                                this.mBezelArea = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "bezel_area = " + this.mBezelArea);
                                }
                            }
                            if (TAG_ENABLE.equals(tag)) {
                                this.mXmlEnable = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "mXmlEnable = " + this.mXmlEnable);
                                }
                                mTouchData.setAccidentalltyTouchEnable(this.mXmlEnable);
                            }
                            if (TAG_LEFT_OFFSET.equals(tag)) {
                                this.mXmlLeftOffset = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "mXmlLeftOffset = " + this.mXmlLeftOffset);
                                }
                                mTouchData.setLeftOffset(this.mXmlLeftOffset);
                            }
                            if (TAG_RIGHT_OFFSET.equals(tag)) {
                                this.mXmlRightOffset = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "mXmlRightOffset = " + this.mXmlRightOffset);
                                }
                                mTouchData.setRightOffset(this.mXmlRightOffset);
                            }
                            if (TAG_POINT_LEFT_OFFSET.equals(tag)) {
                                this.mXmlPointLeftOffset = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "mXmlPointLeftOffset = " + this.mXmlPointLeftOffset);
                                }
                                mTouchData.setPointLeftOffset(this.mXmlPointLeftOffset);
                            }
                            if (TAG_POINT_RIGHT_OFFSET.equals(tag)) {
                                this.mXmlPointRightOffset = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "mXmlPointRightOffset = " + this.mXmlPointRightOffset);
                                }
                                mTouchData.setPointRightOffset(this.mXmlPointRightOffset);
                            }
                            if (TAG_EDGE_ENABLE.equals(tag)) {
                                this.mXmlEdgeEnable = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "mXmlEdgeEnable = " + this.mXmlEdgeEnable);
                                }
                                mTouchData.setEdgeEnable(this.mXmlEdgeEnable);
                            }
                            if (TAG_EDGE_PKG.equals(tag)) {
                                String value6 = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "edge list: " + value6);
                                }
                                if (value6 != null) {
                                    this.mXmlEdgeList.add(value6);
                                }
                            }
                            if (TAG_EDGE_T.equals(tag)) {
                                this.mXmlEdgeT = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "mXmlEdgeT = " + this.mXmlEdgeT);
                                }
                                mTouchData.setEdgeT(this.mXmlEdgeT);
                            }
                            if (TAG_EDGE_T1.equals(tag)) {
                                this.mXmlEdgeT1 = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "mXmlEdgeT1 = " + this.mXmlEdgeT1);
                                }
                                mTouchData.setEdgeT1(this.mXmlEdgeT1);
                            }
                            if (TAG_EDGE_T2.equals(tag)) {
                                this.mXmlEdgeT2 = parser.nextText();
                                if (this.DEBUG) {
                                    Log.d(TAG, "mXmlEdgeT2 = " + this.mXmlEdgeT2);
                                }
                                mTouchData.setEdgeT2(this.mXmlEdgeT2);
                            }
                        }
                    } while (type != 1);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (stream != null) {
                        stream.close();
                    }
                }
            } catch (Throwable th) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    private void initFileObserver() {
        FileObserverPolicy fileObserverPolicy = new FileObserverPolicy(ACCIDENTALLYTOUCH_FILE);
        this.mAccidentallyTouchFileObserver = fileObserverPolicy;
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
            if (OplusAccidentallyTouchUtils.this.DEBUG) {
                Log.d(OplusAccidentallyTouchUtils.TAG, "FileObserver:" + event + "," + path);
            }
            if (event == 8) {
                synchronized (OplusAccidentallyTouchUtils.this.mAccidentallyTouchLock) {
                    Log.d(OplusAccidentallyTouchUtils.TAG, "readConfigFile");
                    OplusAccidentallyTouchUtils.this.readConfigFile();
                }
            }
        }
    }

    public boolean isBezelEnable(String windowState) {
        if (!this.mBezelEnable || this.mBezelList == null || TextUtils.isEmpty(windowState)) {
            return false;
        }
        for (int i = 0; i < this.mBezelList.size(); i++) {
            if (windowState.contains(this.mBezelList.get(i))) {
                return true;
            }
        }
        return false;
    }

    public String getBezelArea() {
        return this.mBezelArea;
    }

    public MotionEvent getExtraEvent() {
        if (this.mNeedExtraEvent) {
            return this.mExtraEvent;
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:125:0x0277, code lost:
    
        if (r1 <= r2) goto L105;
     */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0127  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MotionEvent getMotionEvent(MotionEvent event, View view) {
        boolean inEdge;
        boolean isInEdge = false;
        this.mNeedExtraEvent = false;
        switch (event.getActionMasked()) {
            case 0:
                Context context = this.mContext;
                if (context != null) {
                    this.mDisplayWidth = context.getResources().getDisplayMetrics().widthPixels;
                }
                if (view == null) {
                    this.mEnableSingleSence = true;
                    this.mEnableMultiSence = true;
                }
                this.mIsSingleSence = false;
                this.mIsMultiSence = false;
                int downX = (int) event.getRawX();
                if (downX > this.mLeftOffset || downX < 0) {
                    int i = this.mDisplayWidth;
                    if (downX >= i - this.mRightOffset) {
                    }
                    if (!this.mEnableSingleSence && isInEdge) {
                        this.mIsSingleSence = true;
                        if (this.DEBUG) {
                            Log.d(TAG, "accidentally touch scrap 1: " + event);
                        }
                        return null;
                    }
                }
                isInEdge = true;
                return !this.mEnableSingleSence ? event : event;
            case 1:
                if (this.mIsSingleSence) {
                    this.mIsSingleSence = false;
                    if (this.DEBUG) {
                        Log.d(TAG, "accidentally touch scrap 3: " + event);
                    }
                    return null;
                }
                if (this.mIsMultiSence) {
                    int upIndex = event.getActionIndex();
                    if (this.mScrapPointerId == event.getPointerId(upIndex)) {
                        this.mIsMultiSence = false;
                        if (this.DEBUG) {
                            Log.d(TAG, "accidentally touch scrap 4: " + event);
                        }
                        return null;
                    }
                    return event;
                }
                return event;
            case 2:
                if (this.mIsSingleSence) {
                    int moveX = (int) event.getRawX();
                    boolean notEdge = moveX >= this.mLeftOffset && moveX <= this.mDisplayWidth - this.mRightOffset;
                    if (!notEdge) {
                        return null;
                    }
                    this.mIsSingleSence = false;
                    event.setAction(0);
                    if (this.DEBUG) {
                        Log.d(TAG, "accidentally touch dispatch 3: " + event);
                    }
                }
                if (this.mIsMultiSence) {
                    if (event.getPointerCount() > 1) {
                        MotionEvent event2 = event.split(event.getPointerIdBits() - getFirstIds(event));
                        event2.setDownTime(event2.getEventTime());
                        return event2;
                    }
                    int moveIndex = event.getActionIndex();
                    if (this.mScrapPointerId == event.getPointerId(moveIndex)) {
                        return null;
                    }
                    return event;
                }
                return event;
            case 3:
            case 4:
            default:
                return event;
            case 5:
                int actionX = (int) event.getX(0);
                this.mIsMultiSence = false;
                if (actionX < 0 || actionX > this.mPointLeftOffset) {
                    int i2 = this.mDisplayWidth;
                    if (actionX < i2 - this.mPointRightOffset || actionX > i2) {
                        inEdge = false;
                        if (this.DEBUG) {
                            Log.d(TAG, "original event: " + event);
                            Log.d(TAG, "inEdge:" + inEdge + ", actionX:" + actionX + ", mDisplayWidth:" + this.mDisplayWidth + ", mPointLeftOffset:" + this.mPointLeftOffset + ", mPointRightOffset:" + this.mPointRightOffset);
                        }
                        if ((!inEdge || this.mIsWhiteApp) && this.mEnableMultiSence && event.getPointerCount() > 1) {
                            if (event.getActionIndex() != 0) {
                                if (this.DEBUG) {
                                    Log.d(TAG, "the action index is 0, break");
                                    return event;
                                }
                                return event;
                            }
                            this.mIsSingleSence = false;
                            this.mIsMultiSence = true;
                            if (event.getPointerCount() == 2) {
                                this.mScrapPointerId = event.getPointerId(0);
                                MotionEvent cancel = MotionEvent.obtain(event);
                                cancel.setAction(3);
                                if (this.DEBUG) {
                                    Log.d(TAG, "accidentally touch add: " + cancel);
                                }
                                if (view != null) {
                                    view.dispatchPointerEvent(cancel);
                                }
                                this.mNeedExtraEvent = true;
                                this.mExtraEvent = cancel;
                            }
                            MotionEvent event3 = event.split(event.getPointerIdBits() - getFirstIds(event));
                            event3.setDownTime(event3.getEventTime());
                            if (this.DEBUG) {
                                Log.d(TAG, "accidentally touch dispatch 1: " + event3);
                                return event3;
                            }
                            return event3;
                        }
                        return event;
                    }
                }
                inEdge = true;
                if (this.DEBUG) {
                }
                if (!inEdge) {
                }
                if (event.getActionIndex() != 0) {
                }
                break;
            case 6:
                int pointerIndex = event.getActionIndex();
                if (this.mIsMultiSence && this.mScrapPointerId == event.getPointerId(pointerIndex)) {
                    this.mIsMultiSence = false;
                    if (this.DEBUG) {
                        Log.d(TAG, "accidentally touch scrap 2: " + event);
                    }
                    return null;
                }
                if (this.mIsMultiSence) {
                    if (this.DEBUG) {
                        Log.d(TAG, "original event: " + event);
                    }
                    MotionEvent event4 = event.split(event.getPointerIdBits() - getFirstIds(event));
                    event4.setDownTime(event4.getEventTime());
                    if (this.DEBUG) {
                        Log.d(TAG, "accidentally touch dispatch 2: " + event4);
                        return event4;
                    }
                    return event4;
                }
                return event;
        }
    }

    public MotionEvent updatePointerEvent(MotionEvent event, View mView, Configuration mLastConfiguration) {
        return event;
    }

    public List<Object> handlePointerEvent(MotionEvent event, View mView, Configuration mLastConfiguration) {
        List<Object> list = new ArrayList<>();
        list.add(0, 0);
        list.add(1, event);
        return list;
    }

    public void initData(Context context) {
        try {
            Context applicationContext = context.getApplicationContext();
            this.mContext = applicationContext;
            if (applicationContext == null) {
                this.mContext = context;
            }
            OplusActivityManager manager = new OplusActivityManager();
            OplusAccidentallyTouchData data = manager.getAccidentallyTouchData();
            if (data != null) {
                this.mSingleList = data.getSingleTouchList();
                this.mMultiList = data.getMultiTouchList();
                this.mWhiteList = data.getTouchWhiteList();
                String leftOffset = data.getLeftOffset();
                String rightOffset = data.getRightOffset();
                String pointLeftOffset = data.getPointLeftOffset();
                String pointRightOffset = data.getPointRightOffset();
                String enable = data.getAccidentalltyTouchEnable();
                String edgeEnable = data.getEdgeEnable();
                String edgeT = data.getEdgeT();
                String edgeT1 = data.getEdgeT1();
                String edgeT2 = data.getEdgeT2();
                ArrayList<String> edgeList = data.getEdgeList();
                this.mEdgeList = edgeList;
                this.mIsEdgePkg = isInList(edgeList, context.getPackageName());
                if (edgeEnable != null) {
                    if ("true".equals(edgeEnable)) {
                        this.mEdgeEnable = true;
                    } else if ("false".equals(edgeEnable)) {
                        this.mEdgeEnable = false;
                    }
                }
                if (edgeT != null) {
                    this.mEdgeT = Integer.parseInt(edgeT);
                }
                if (edgeT1 != null) {
                    this.mEdgeT1 = Integer.parseInt(edgeT1);
                }
                if (edgeT2 != null) {
                    this.mEdgeT2 = Integer.parseInt(edgeT2);
                }
                this.mLeftOffset = 5;
                this.mRightOffset = 5;
                this.mPointLeftOffset = 80;
                this.mPointRightOffset = 80;
                if (leftOffset != null) {
                    this.mLeftOffset = Integer.parseInt(leftOffset);
                }
                if (rightOffset != null) {
                    this.mRightOffset = Integer.parseInt(rightOffset);
                }
                if (pointLeftOffset != null) {
                    this.mPointLeftOffset = Integer.parseInt(pointLeftOffset);
                }
                if (pointRightOffset != null) {
                    this.mPointRightOffset = Integer.parseInt(pointRightOffset);
                }
                if (enable != null) {
                    if ("true".equals(enable)) {
                        this.mEnableAccidentallyTouch = true;
                    } else {
                        this.mEnableAccidentallyTouch = false;
                    }
                }
                this.mEnableSingleSence = !isInList(this.mSingleList, context.getPackageName());
                this.mEnableMultiSence = !isInList(this.mMultiList, context.getPackageName());
                this.mIsWhiteApp = isInList(this.mWhiteList, context.getPackageName());
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                float scale = dm.density / 3.0f;
                this.mDisplayWidth = dm.widthPixels;
                this.mPointLeftOffset = (int) (this.mPointLeftOffset * scale);
                this.mPointRightOffset = (int) (this.mPointRightOffset * scale);
                if (this.DEBUG) {
                    Log.d(TAG, "density:" + dm.density + ", mEnableMulti:" + this.mEnableMultiSence + ", pointLeftOffset:" + pointLeftOffset + ", mLeftOffset:" + this.mLeftOffset + ", mPointLeftOffset:" + this.mPointLeftOffset);
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "init data RemoteException , " + e);
        } catch (Error e2) {
            Log.e(TAG, "init data Error , " + e2);
        } catch (Exception e3) {
            Log.e(TAG, "init data Exception , " + e3);
        }
    }

    private boolean isInList(ArrayList<String> list, String packageName) {
        if (list != null && !list.isEmpty() && list.contains(packageName)) {
            return true;
        }
        if (!this.mDefaultList.isEmpty() && this.mDefaultList.contains(packageName)) {
            return true;
        }
        return false;
    }

    private int getFirstIds(MotionEvent event) {
        if (event != null) {
            return 1 << event.getPointerId(0);
        }
        return 1;
    }

    public boolean getEdgeEnable() {
        return this.mEdgeEnable;
    }

    public int getEdgeT() {
        return this.mEdgeT;
    }

    public int getOriEdgeT1() {
        return this.mEdgeT1;
    }

    public boolean isEdgePkg() {
        return this.mIsEdgePkg;
    }

    public ArrayList<String> getEdgeList() {
        return this.mEdgeList;
    }

    public int getEdgeT1() {
        if (this.mIsEdgePkg) {
            return this.mEdgeT;
        }
        return this.mEdgeT1;
    }

    public int getEdgeT2() {
        return this.mEdgeT2;
    }
}
