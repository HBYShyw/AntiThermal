package com.android.server;

import android.common.IOplusCommonFactory;
import android.content.Context;
import android.util.Slog;
import com.android.server.am.ActivityManagerService;
import com.android.server.audio.AudioService;
import com.android.server.wm.ActivityTaskManagerService;
import java.lang.reflect.InvocationTargetException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class OplusCommonServiceFactory implements IOplusCommonFactory {
    private static final String AMS_CLASSNAME = "com.android.server.am.OplusActivityManagerService";
    private static final String AS_CLASSNAME = "com.android.server.audio.OplusAudioService";
    private static final String ATMS_CLASSNAME = "com.android.server.wm.OplusActivityTaskManagerService";
    private static final String MY_TAG = "OplusCommonServiceFactory";
    private static final String WMS_CLASSNAME = "com.android.server.wm.OplusWindowManagerService";
    private final String TAG = getClass().getSimpleName();

    public static final AudioService getOplusAudioService(Context context) {
        return createOplusAudioService(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object newInstance(String str) throws Exception {
        return Class.forName(str).getConstructor(new Class[0]).newInstance(new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void warn(String str) {
        Slog.w(this.TAG, str);
    }

    private static ActivityManagerService createActivityManagerService(Context context, ActivityTaskManagerService activityTaskManagerService) {
        Slog.i(MY_TAG, "createActivityManagerService reflect");
        try {
            return (ActivityManagerService) Class.forName(AMS_CLASSNAME).getDeclaredConstructor(Context.class, ActivityTaskManagerService.class).newInstance(context, activityTaskManagerService);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (InstantiationException e3) {
            e3.printStackTrace();
            return null;
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            return null;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        }
    }

    private static AudioService createOplusAudioService(Context context) {
        Slog.i(MY_TAG, "createOplusAudioService reflect");
        try {
            return (AudioService) Class.forName(AS_CLASSNAME).getDeclaredConstructor(Context.class).newInstance(context);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (InstantiationException e3) {
            e3.printStackTrace();
            return null;
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            return null;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        }
    }
}
