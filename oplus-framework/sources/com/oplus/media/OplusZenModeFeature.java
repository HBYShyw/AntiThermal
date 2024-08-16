package com.oplus.media;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Parcel;
import android.os.Process;
import android.util.Log;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class OplusZenModeFeature implements IOplusZenModeFeature {
    private static final String ATLAS_KEY_AUDIO_CHECK_LISTINFO_BYNAME = "check_listinfo_byname";
    private static final String ATLAS_KEY_AUDIO_GET_LISTINFO_BYNAME = "get_listinfo_byname";
    public static final int MUTE_FLAG = 1;
    private static final String SYSTEM_NOTIFICATION_AUDIO_PATH = "/system/media/audio/notifications/";
    private static final String TAG = "OplusZenModeFeature";
    public static final int UNMUTE_FLAG = 0;
    private Method mAtlasGetParameters;
    private Object mAtlasInstance;
    private Method mshouldInterceptSound;
    private Object mNotificationManager = null;
    private boolean mNeedMute = false;
    private boolean mInterceptFlag = false;
    private boolean mRecoverFlag = false;
    private int mStreamType = Integer.MIN_VALUE;

    public OplusZenModeFeature() {
        Log.d(TAG, "new OplusZenModeFeature");
        try {
            Class atlasManager = Class.forName("com.oplus.atlas.OplusAtlasManager");
            Method methodGetInstance = atlasManager.getMethod("getInstance", Context.class);
            this.mAtlasGetParameters = atlasManager.getMethod("getParameters", String.class);
            this.mAtlasInstance = methodGetInstance.invoke(new Object(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.oplus.media.IOplusZenModeFeature
    public Parcel checkZenMode() {
        String currentPackageName;
        Method method;
        int i = this.mStreamType;
        if (i == 0 || i == 3 || (currentPackageName = ActivityThread.currentPackageName()) == null || currentPackageName.length() <= 0) {
            return null;
        }
        String str = null;
        try {
            str = (String) this.mAtlasGetParameters.invoke(this.mAtlasInstance, "check_listinfo_byname=zenmode=" + currentPackageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (str == null || !str.equals("true")) {
            return null;
        }
        Log.i(TAG, "The package name is " + currentPackageName + "qq & wecha zenmode control !");
        int myUid = Process.myUid();
        if (this.mNotificationManager == null) {
            try {
                Class<?> cls = Class.forName("android.app.OplusNotificationManager");
                this.mshouldInterceptSound = cls.getMethod("shouldInterceptSound", String.class, Integer.TYPE);
                this.mNotificationManager = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
                this.mshouldInterceptSound.setAccessible(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        Object obj = this.mNotificationManager;
        if (obj != null && (method = this.mshouldInterceptSound) != null) {
            try {
                this.mInterceptFlag = ((Boolean) method.invoke(obj, currentPackageName, Integer.valueOf(myUid))).booleanValue();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        Parcel obtain = Parcel.obtain();
        obtain.writeInt(this.mInterceptFlag ? 1 : 0);
        return obtain;
    }

    @Override // com.oplus.media.IOplusZenModeFeature
    public Parcel checkWechatMute() {
        String currentPackageName = ActivityThread.currentPackageName();
        if (currentPackageName == null || currentPackageName.length() <= 0) {
            return null;
        }
        String str = null;
        try {
            str = (String) this.mAtlasGetParameters.invoke(this.mAtlasInstance, "get_listinfo_byname=zenmode=" + currentPackageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (str != null && str.equals("wechat_mute")) {
            Log.i(TAG, "The package name is " + currentPackageName + "wecha zenmode control !");
            int i = this.mStreamType;
            if (i == 5 || i == 2) {
                Parcel obtain = Parcel.obtain();
                obtain.writeInt(this.mInterceptFlag ? 1 : 0);
                return obtain;
            }
        }
        return null;
    }

    @Override // com.oplus.media.IOplusZenModeFeature
    public void resetZenModeFlag() {
        this.mInterceptFlag = false;
    }

    @Override // com.oplus.media.IOplusZenModeFeature
    public void setAudioStreamType(int type) {
        this.mStreamType = type;
    }
}
