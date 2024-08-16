package com.oplus.preloadsplash;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.Trace;
import android.util.Slog;
import android.util.Xml;
import com.oplus.deepthinker.OplusDeepThinkerManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: classes.dex */
public class OplusPreLoadSplashManager implements IOplusPreLoadSplashManager {
    private static final String CACHE_INFO_NAME = ".preloadCache_info.xml";
    private static final int MESSAGE_CACHE_INFO = 8388611;
    private static final int MESSAGE_INIT_ONLINECONFIG = 8388613;
    private static final int MESSAGE_LOAD_CACHE_INFO = 8388608;
    private static final int MESSAGE_PRELOAD_BITMAP = 8388609;
    private static final int MESSAGE_QUIT = 8388612;
    private static final int MESSAGE_SAVE_INFO = 8388610;
    private static final int SAVE_TIME_OUT = 5000;
    private static final String TAG = "OplusPreLoadSplashManager";
    private static final String XML_TAG_CACHEINFOS = "cacheInfos";
    private static final String XML_TAG_ID = "resourcesID";
    private static final String XML_TAG_ITEM = "item";
    private static final String XML_TAG_MAX_FAILE_NUM = "failenum";
    private static final String XML_TAG_NAME = "resourceName";
    private static Object mLock = new Object();
    public static OplusPreLoadSplashManager sInstance;
    private Context mApplicationContext;
    private HandlerThread mHandlerThread;
    private Handler mWorkHandler;
    private boolean mPreloadSplashSupport = false;
    private int mDecodingCount = 0;
    private int mSaveCount = 0;
    private boolean mNeedSaveInfo = true;
    private boolean mHasExit = false;
    private boolean mEnable = false;
    private boolean mDebug = false;
    private int mMinWidth = 900;
    private int mMinHeight = 900;
    private int mMaxSaveCount = 3;
    private int mMaxFailNum = 5;
    private HashMap<Integer, BitmapCacheInfo> mSaveInfoMap = new HashMap<>();
    private HashMap<Integer, Drawable> mCacheDrawableMap = new HashMap<>();
    private HashMap<Integer, BitmapCacheInfo> mCacheInfoMap = new HashMap<>();
    private ArrayList<Integer> mSaveIDList = new ArrayList<>();
    private ArrayList<Integer> mUsedDrawableIDList = new ArrayList<>();
    private HashMap<Integer, WeakReference<Drawable>> mUsingCacheDrawableMap = new HashMap<>();
    private boolean mPreloadDrawableIsUsed = false;
    private int mPreloadDrawableFaileNum = 0;

    /* loaded from: classes.dex */
    public class BitmapCacheInfo {
        public int mBitmapResourceID;
        public String mResourceName;

        public BitmapCacheInfo() {
        }

        public BitmapCacheInfo(String name, int resourceID) {
            this.mResourceName = name;
            this.mBitmapResourceID = resourceID;
        }

        public int getResourceID() {
            return this.mBitmapResourceID;
        }

        public String getResourceName() {
            return this.mResourceName;
        }

        public void setResourcesID(int id) {
            this.mBitmapResourceID = id;
        }

        public void setResourceName(String name) {
            this.mResourceName = name;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ThumbThread extends Thread {
        private BitmapCacheInfo mCache;

        ThumbThread(BitmapCacheInfo cache) {
            this.mCache = null;
            this.mCache = cache;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Process.setThreadPriority(10);
            if (this.mCache != null) {
                synchronized (OplusPreLoadSplashManager.mLock) {
                    OplusPreLoadSplashManager.this.mDecodingCount++;
                }
                OplusPreLoadSplashManager.this.decodeBitmapFromCacheInfo(this.mCache);
                synchronized (OplusPreLoadSplashManager.mLock) {
                    OplusPreLoadSplashManager oplusPreLoadSplashManager = OplusPreLoadSplashManager.this;
                    oplusPreLoadSplashManager.mDecodingCount--;
                }
            }
        }
    }

    public static synchronized OplusPreLoadSplashManager getInstance() {
        OplusPreLoadSplashManager oplusPreLoadSplashManager;
        synchronized (OplusPreLoadSplashManager.class) {
            synchronized (OplusPreLoadSplashManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusPreLoadSplashManager();
                }
                oplusPreLoadSplashManager = sInstance;
            }
            return oplusPreLoadSplashManager;
        }
        return oplusPreLoadSplashManager;
    }

    private OplusPreLoadSplashManager() {
    }

    private void initInner(Context context) {
        this.mApplicationContext = context;
        Trace.traceBegin(64L, "initRUSParam");
        getInfoFromRUS();
        Trace.traceEnd(64L);
        if (!this.mEnable) {
            return;
        }
        this.mPreloadSplashSupport = true;
        HandlerThread handlerThread = new HandlerThread("OpPreLoadThread", 10);
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        Slog.i(TAG, "mHandlerThread -- start");
        Handler handler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.oplus.preloadsplash.OplusPreLoadSplashManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == OplusPreLoadSplashManager.MESSAGE_LOAD_CACHE_INFO) {
                    OplusPreLoadSplashManager.this.handleLoadCacheInfo();
                    return;
                }
                if (msg.what == OplusPreLoadSplashManager.MESSAGE_PRELOAD_BITMAP) {
                    OplusPreLoadSplashManager.this.handlePreLoadDrawable();
                    return;
                }
                if (msg.what == OplusPreLoadSplashManager.MESSAGE_SAVE_INFO) {
                    OplusPreLoadSplashManager.this.handleSaveCacheInfo();
                } else if (msg.what == OplusPreLoadSplashManager.MESSAGE_CACHE_INFO) {
                    OplusPreLoadSplashManager.this.handleCacheDrawableInfo(msg);
                } else if (msg.what == OplusPreLoadSplashManager.MESSAGE_QUIT) {
                    OplusPreLoadSplashManager.this.handleQuitWorkHandler();
                }
            }
        };
        this.mWorkHandler = handler;
        handler.sendEmptyMessage(MESSAGE_LOAD_CACHE_INFO);
        this.mWorkHandler.sendEmptyMessageDelayed(MESSAGE_SAVE_INFO, 5000L);
        this.mWorkHandler.sendEmptyMessageDelayed(MESSAGE_QUIT, 5000L);
    }

    private boolean getInfoFromRUS() {
        try {
            List<String> list = getRUSinfo(ActivityThread.currentProcessName());
            if (list != null && list.size() == 7) {
                this.mEnable = Integer.valueOf(list.get(0)).intValue() != 0;
                this.mDebug = Integer.valueOf(list.get(1)).intValue() != 0;
                this.mMinWidth = Integer.valueOf(list.get(3)).intValue();
                this.mMinHeight = Integer.valueOf(list.get(4)).intValue();
                this.mMaxSaveCount = Integer.valueOf(list.get(5)).intValue();
                this.mMaxFailNum = Integer.valueOf(list.get(6)).intValue();
                Slog.d(TAG, "package:" + ActivityThread.currentProcessName() + "is in the whiteList, paramList = " + list);
                return true;
            }
            this.mEnable = false;
            this.mDebug = false;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            this.mEnable = false;
            this.mDebug = false;
            return false;
        }
    }

    private List<String> getRUSinfo(String packageName) {
        ActivityManager am = (ActivityManager) this.mApplicationContext.getSystemService("activity");
        try {
            Method getPreLoadSplashRUSInfo = am.getClass().getMethod("getPreLoadSplashRUSInfo", String.class);
            getPreLoadSplashRUSInfo.setAccessible(true);
            List<String> list = (List) getPreLoadSplashRUSInfo.invoke(am, packageName);
            return list;
        } catch (Exception e) {
            Slog.e(TAG, "getPreLoadSplashRUSInfo name is not exit:" + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decodeBitmapFromCacheInfo(BitmapCacheInfo cacheInfo) {
        boolean hasExit;
        Bitmap bitmap;
        Trace.traceBegin(64L, "decoeBitmapFromCacheInfo");
        synchronized (mLock) {
            if (cacheInfo != null) {
                if (!this.mHasExit && this.mApplicationContext != null) {
                    int resourceID = cacheInfo.getResourceID();
                    String resourceName = cacheInfo.getResourceName();
                    try {
                        Drawable drawable = this.mApplicationContext.getResources().getDrawableForDensity(resourceID, 0);
                        if (drawable != null) {
                            synchronized (mLock) {
                                hasExit = this.mHasExit;
                            }
                            if (hasExit) {
                                if ((drawable instanceof BitmapDrawable) && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null && !bitmap.isRecycled()) {
                                    bitmap.recycle();
                                }
                                drawable.setCallback(null);
                                Trace.traceEnd(64L);
                                return;
                            }
                            synchronized (this.mCacheDrawableMap) {
                                this.mCacheDrawableMap.put(Integer.valueOf(resourceID), drawable);
                            }
                            if (this.mDebug) {
                                Slog.d(TAG, "decoeBitmapFromCacheInfo success resourceName = " + resourceName + "， resourceID = " + resourceID);
                            }
                        }
                        Trace.traceEnd(64L);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Trace.traceEnd(64L);
                        return;
                    }
                }
            }
            Trace.traceEnd(64L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLoadCacheInfo() {
        if (!isPreLoadCacheEffect()) {
            return;
        }
        readCacheInfoFromXml();
        if (this.mDebug) {
            Slog.d(TAG, "handleLoadCacheInfo,  mPreloadDrawableFaileNum = " + this.mPreloadDrawableFaileNum);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePreLoadDrawable() {
        HashMap<Integer, BitmapCacheInfo> hashMap;
        if (!isPreLoadCacheEffect() || (hashMap = this.mCacheInfoMap) == null || hashMap.size() == 0) {
            return;
        }
        if (this.mDebug) {
            Slog.d(TAG, "handlePreLoadDrawable  - mCacheInfoMap.size = " + this.mCacheInfoMap.size());
        }
        Set<Integer> keySet = this.mCacheInfoMap.keySet();
        for (Integer key : keySet) {
            BitmapCacheInfo cacheInfo = this.mCacheInfoMap.get(key);
            if (this.mDebug) {
                Slog.d(TAG, "handlePreLoadDrawable  - mCacheInfoMap.key = " + key);
            }
            new ThumbThread(cacheInfo).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCacheDrawableInfo(Message msg) {
        Handler handler;
        if (!isPreLoadCacheEffect()) {
            return;
        }
        BitmapCacheInfo bitmapCacheInfo = (BitmapCacheInfo) msg.obj;
        this.mSaveInfoMap.put(Integer.valueOf(bitmapCacheInfo.mBitmapResourceID), bitmapCacheInfo);
        if (this.mSaveInfoMap.size() == this.mMaxSaveCount && (handler = this.mWorkHandler) != null) {
            handler.removeMessages(MESSAGE_SAVE_INFO);
            this.mWorkHandler.sendEmptyMessage(MESSAGE_SAVE_INFO);
        }
    }

    private boolean isRunBackground() {
        boolean isBackground = true;
        Context context = this.mApplicationContext;
        if (context == null) {
            return true;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(ActivityThread.currentProcessName())) {
                String processName = appProcess.processName;
                if (appProcess.importance == 400) {
                    isBackground = true;
                } else if (appProcess.importance == 100 || appProcess.importance == 200) {
                    isBackground = false;
                } else {
                    isBackground = true;
                }
            }
        }
        return isBackground;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSaveCacheInfo() {
        synchronized (mLock) {
            if (isPreLoadCacheEffect() && this.mNeedSaveInfo) {
                this.mNeedSaveInfo = false;
                if (needWriteCacheInfoToXml()) {
                    if (this.mPreloadDrawableIsUsed || (this.mCacheInfoMap.size() == 0 && this.mSaveInfoMap.size() != 0)) {
                        this.mPreloadDrawableFaileNum = 0;
                    } else {
                        this.mPreloadDrawableFaileNum++;
                    }
                    if (this.mPreloadDrawableFaileNum >= this.mMaxFailNum) {
                        this.mPreloadDrawableFaileNum = 0;
                    }
                    writeCacheInfoToXml(this.mSaveInfoMap);
                }
                Handler handler = this.mWorkHandler;
                if (handler != null) {
                    handler.removeMessages(MESSAGE_CACHE_INFO);
                    this.mWorkHandler.removeMessages(MESSAGE_SAVE_INFO);
                }
            }
        }
    }

    private boolean needWriteCacheInfoToXml() {
        if (this.mSaveInfoMap.size() == 0 && isRunBackground()) {
            if (this.mDebug) {
                Slog.d(TAG, "the app start in background");
            }
            return false;
        }
        if (this.mSaveInfoMap.size() != this.mCacheInfoMap.size()) {
            return true;
        }
        Set<Integer> keySet = this.mSaveInfoMap.keySet();
        Integer.valueOf(1);
        for (Integer key : keySet) {
            if (!this.mCacheInfoMap.containsKey(key)) {
                return true;
            }
        }
        boolean z = this.mPreloadDrawableIsUsed;
        return !z || (z && this.mPreloadDrawableFaileNum != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleQuitWorkHandler() {
        synchronized (mLock) {
            this.mHasExit = true;
        }
        if (this.mDebug) {
            Slog.d(TAG, "handleQuitWorkHandler");
        }
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.removeMessages(MESSAGE_LOAD_CACHE_INFO);
            this.mWorkHandler.removeMessages(MESSAGE_SAVE_INFO);
            this.mWorkHandler.removeMessages(MESSAGE_CACHE_INFO);
            this.mWorkHandler.removeMessages(MESSAGE_PRELOAD_BITMAP);
        }
        clearNoUsedDrawableCache();
        this.mCacheInfoMap.clear();
        this.mCacheInfoMap = null;
        this.mSaveInfoMap.clear();
        this.mSaveInfoMap = null;
        this.mSaveIDList.clear();
        this.mSaveIDList = null;
        this.mUsedDrawableIDList.clear();
        this.mUsedDrawableIDList = null;
        HandlerThread handlerThread = this.mHandlerThread;
        if (handlerThread != null) {
            handlerThread.getLooper().quit();
            this.mHandlerThread = null;
        }
    }

    private void clearNoUsedDrawableCache() {
        Bitmap bitmap;
        synchronized (this.mCacheDrawableMap) {
            Iterator<Map.Entry<Integer, Drawable>> it = this.mCacheDrawableMap.entrySet().iterator();
            while (it.hasNext()) {
                try {
                    Map.Entry<Integer, Drawable> entry = it.next();
                    Integer key = entry.getKey();
                    if (this.mUsedDrawableIDList.contains(key)) {
                        this.mUsingCacheDrawableMap.put(key, new WeakReference<>(this.mCacheDrawableMap.get(key)));
                        it.remove();
                    } else {
                        Drawable drawable = this.mCacheDrawableMap.get(key);
                        if (drawable != null) {
                            if ((drawable instanceof BitmapDrawable) && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null && !bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                            drawable.setCallback(null);
                            it.remove();
                        } else {
                            it.remove();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean readCacheInfoFromXml() {
        Trace.traceBegin(64L, "readCacheInfoFromXml");
        if (this.mDebug) {
            Slog.d(TAG, "readCacheInfoFromXml;  PackageName = " + ActivityThread.currentProcessName());
        }
        if (this.mApplicationContext == null) {
            Trace.traceEnd(64L);
            return false;
        }
        if (this.mCacheInfoMap == null) {
            this.mCacheInfoMap = new HashMap<>();
        }
        FileInputStream is = null;
        try {
            try {
                String fileName = ActivityThread.currentProcessName() + CACHE_INFO_NAME;
                File file = new File(this.mApplicationContext.getFilesDir(), fileName);
                if (!file.exists()) {
                    Trace.traceEnd(64L);
                    if (this.mDebug) {
                        Slog.d(TAG, "CacheInfofile is not exists");
                    }
                    try {
                        Trace.traceEnd(64L);
                        if (0 != 0) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
                FileInputStream is2 = new FileInputStream(file);
                BitmapCacheInfo cacheinfo = new BitmapCacheInfo();
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setInput(is2, "utf-8");
                for (int eventType = xmlPullParser.getEventType(); eventType != 1; eventType = xmlPullParser.next()) {
                    switch (eventType) {
                        case 2:
                            if (XML_TAG_CACHEINFOS.equals(xmlPullParser.getName())) {
                                this.mCacheInfoMap.clear();
                            }
                            if (XML_TAG_ITEM.equals(xmlPullParser.getName())) {
                                cacheinfo = new BitmapCacheInfo();
                            }
                            if (XML_TAG_ID.equals(xmlPullParser.getName())) {
                                int value = Integer.valueOf(xmlPullParser.nextText()).intValue();
                                if (cacheinfo != null) {
                                    cacheinfo.setResourcesID(value);
                                }
                            }
                            if (XML_TAG_NAME.equals(xmlPullParser.getName())) {
                                String value2 = xmlPullParser.nextText();
                                if (cacheinfo != null) {
                                    cacheinfo.setResourceName(value2);
                                }
                            }
                            if (XML_TAG_MAX_FAILE_NUM.equals(xmlPullParser.getName())) {
                                int value3 = Integer.valueOf(xmlPullParser.nextText()).intValue();
                                this.mPreloadDrawableFaileNum = value3;
                            }
                        case 3:
                            if (XML_TAG_ITEM.equals(xmlPullParser.getName()) && cacheinfo != null) {
                                this.mCacheInfoMap.put(Integer.valueOf(cacheinfo.getResourceID()), cacheinfo);
                            }
                        default:
                    }
                }
                try {
                    Trace.traceEnd(64L);
                    is2.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return true;
            } catch (Exception e3) {
                e3.printStackTrace();
                try {
                    Trace.traceEnd(64L);
                    if (0 != 0) {
                        is.close();
                    }
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                return false;
            }
        } catch (Throwable th) {
            try {
                Trace.traceEnd(64L);
                if (0 != 0) {
                    is.close();
                }
            } catch (IOException e5) {
                e5.printStackTrace();
            }
            throw th;
        }
    }

    private boolean writeCacheInfoToXml(HashMap<Integer, BitmapCacheInfo> map) {
        HashMap<Integer, BitmapCacheInfo> hashMap = map;
        Trace.traceBegin(64L, "writeCacheInfoToXml");
        if (this.mDebug) {
            Slog.d(TAG, "writeCacheInfoToXml");
        }
        if (hashMap == null || this.mApplicationContext == null) {
            Trace.traceEnd(64L);
            return false;
        }
        FileOutputStream out = null;
        try {
            try {
                String fileName = ActivityThread.currentProcessName() + CACHE_INFO_NAME;
                File file = new File(this.mApplicationContext.getFilesDir(), fileName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                out = new FileOutputStream(file);
                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(out, "UTF-8");
                serializer.startDocument("UTF-8", true);
                String str = null;
                serializer.startTag(null, XML_TAG_CACHEINFOS);
                Set<Integer> keySet = map.keySet();
                Integer count = 1;
                for (Integer key : keySet) {
                    BitmapCacheInfo value = hashMap.get(key);
                    serializer.startTag(str, XML_TAG_ITEM);
                    serializer.attribute(null, OplusDeepThinkerManager.ARG_ID, count.toString());
                    serializer.startTag(null, XML_TAG_ID);
                    serializer.text(key.toString());
                    serializer.endTag(null, XML_TAG_ID);
                    serializer.startTag(null, XML_TAG_NAME);
                    serializer.text(value.getResourceName());
                    serializer.endTag(null, XML_TAG_NAME);
                    serializer.endTag(null, XML_TAG_ITEM);
                    count = Integer.valueOf(count.intValue() + 1);
                    hashMap = map;
                    str = null;
                }
                serializer.startTag(null, XML_TAG_MAX_FAILE_NUM);
                serializer.text(String.valueOf(this.mPreloadDrawableFaileNum));
                serializer.endTag(null, XML_TAG_MAX_FAILE_NUM);
                serializer.endTag(null, XML_TAG_CACHEINFOS);
                serializer.endDocument();
                out.flush();
                try {
                    Trace.traceEnd(64L);
                    out.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return true;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                try {
                    Trace.traceEnd(64L);
                    if (out == null) {
                        return false;
                    }
                    out.close();
                    return false;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return false;
                }
            }
        } finally {
        }
    }

    private void preloadDrawableInner() {
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.sendEmptyMessage(MESSAGE_PRELOAD_BITMAP);
        }
    }

    private Drawable getDrawableFromPreLoadCacheInner(int resourceID) {
        Drawable drawable;
        if (!isPreLoadCacheEffect() || isDecodingDrawable()) {
            return null;
        }
        synchronized (this.mCacheDrawableMap) {
            drawable = this.mCacheDrawableMap.get(Integer.valueOf(resourceID));
        }
        if (drawable == null) {
            return null;
        }
        if (this.mDebug) {
            Slog.d(TAG, "getPreLoadBitmap success，resourceID = " + resourceID);
        }
        this.mUsedDrawableIDList.add(Integer.valueOf(resourceID));
        saveDrawableCacheInfoInner(resourceID, drawable);
        this.mPreloadDrawableIsUsed = true;
        return drawable;
    }

    private boolean saveDrawableCacheInfoInner(int resourceID, Drawable drawable) {
        synchronized (mLock) {
            if (isPreLoadCacheEffect() && !isDecodingDrawable() && this.mNeedSaveInfo && this.mSaveCount < this.mMaxSaveCount && !this.mSaveIDList.contains(Integer.valueOf(resourceID)) && drawable != null && this.mApplicationContext != null) {
                int drawableHeight = drawable.getIntrinsicHeight();
                int drawableWidth = drawable.getIntrinsicWidth();
                if (drawableWidth >= this.mMinWidth && drawableHeight >= this.mMinHeight) {
                    this.mSaveIDList.add(Integer.valueOf(resourceID));
                    String resourceName = this.mApplicationContext.getResources().getResourceName(resourceID);
                    BitmapCacheInfo saveInfo = new BitmapCacheInfo(resourceName, resourceID);
                    Message message = this.mWorkHandler.obtainMessage();
                    message.what = MESSAGE_CACHE_INFO;
                    message.obj = saveInfo;
                    this.mWorkHandler.sendMessage(message);
                    if (this.mDebug) {
                        Slog.d(TAG, "saveDrawableCacheInfo success,  resourceID = " + resourceID + "; resourceName = " + resourceName + "; drawableWidth = " + drawableWidth + "; drawableHeight = " + drawableHeight);
                    }
                    synchronized (mLock) {
                        this.mSaveCount++;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private boolean isPreLoadCacheEffect() {
        synchronized (mLock) {
            return this.mEnable && !this.mHasExit;
        }
    }

    private boolean isDecodingDrawable() {
        synchronized (mLock) {
            return this.mDecodingCount > 0;
        }
    }

    private Drawable getDrawableCacheInner(int resourceID, Resources.Theme theme, Resources wrapper) {
        Drawable.ConstantState state;
        Drawable dr = getDrawableFromPreLoadCacheInner(resourceID);
        if (dr == null) {
            return null;
        }
        if (this.mDebug) {
            Slog.d(TAG, "getDrawableCache -- resourceID = " + resourceID + "; theme = " + theme + "; wrapper = " + wrapper + "; dr = " + dr);
        }
        if (theme != null && (state = dr.getConstantState()) != null) {
            return state.newDrawable(wrapper);
        }
        return dr;
    }

    @Override // com.oplus.preloadsplash.IOplusPreLoadSplashManager
    public void init(Context context) {
        if (context == null) {
            return;
        }
        initInner(context);
    }

    @Override // com.oplus.preloadsplash.IOplusPreLoadSplashManager
    public void preloadCacheDrawable() {
        if (!this.mPreloadSplashSupport) {
            return;
        }
        preloadDrawableInner();
    }

    @Override // com.oplus.preloadsplash.IOplusPreLoadSplashManager
    public Drawable getDrawableCache(int resourceID, int density, Resources.Theme theme, Resources wrapper) {
        if (this.mPreloadSplashSupport && density == 0) {
            return getDrawableCacheInner(resourceID, theme, wrapper);
        }
        return null;
    }

    @Override // com.oplus.preloadsplash.IOplusPreLoadSplashManager
    public boolean saveDrawableCache(int resourceID, Drawable drawable, int density) {
        if (this.mPreloadSplashSupport && density == 0) {
            return saveDrawableCacheInfoInner(resourceID, drawable);
        }
        return false;
    }
}
