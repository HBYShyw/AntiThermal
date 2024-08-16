package android.app;

import android.app.ILoadedApkExt;
import android.app.LoadedApk;
import android.common.OplusFeatureCache;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.IOplusThemeManager;
import android.content.res.IResourcesImplExt;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.view.autolayout.IOplusAutoLayoutManager;
import android.window.WindowContext;
import android.window.WindowTokenClient;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: classes.dex */
public class LoadedApkExtImpl implements ILoadedApkExt {
    private static final String TAG = "LoadedApkExtImpl";
    private static final int APK_CONTAINS_ENT_CERT = SystemProperties.getInt("sys.apk_contains_ent_cert", 0);
    private static Method sMethodLogP = null;

    public LoadedApkExtImpl(Object loadedApk) {
    }

    public static void sLogP(String tag, String content) {
        if (sMethodLogP == null) {
            try {
                Class cls = Class.forName("android.util.Log");
                Method declaredMethod = cls.getDeclaredMethod("p", String.class, String.class);
                sMethodLogP = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (ClassNotFoundException e) {
                Log.i(TAG, "ClassNotFoundException : " + e.getMessage());
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                Log.i(TAG, "NoSuchMethodException : " + e2.getMessage());
                e2.printStackTrace();
            } catch (SecurityException e3) {
                e3.printStackTrace();
            }
        }
        Method method = sMethodLogP;
        if (method != null) {
            try {
                method.invoke(null, tag, content);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
    }

    public static Integer sGetOverrideDisplayId(ApplicationInfo applicationInfo) {
        int launchingDisplayId;
        if (applicationInfo == null || (launchingDisplayId = applicationInfo.mApplicationInfoExt.getLaunchingDisplayId()) == -1) {
            return null;
        }
        return Integer.valueOf(launchingDisplayId);
    }

    public boolean checkUnregisterReceiver(Context context) {
        WindowTokenClient wtc;
        return (context instanceof WindowContext) && (wtc = context.getWindowContextToken()) != null && wtc.getWrapper().isContextWeakRefRelease();
    }

    public void preMakePaths(ApplicationInfo aInfo, List<String> outZipPaths) {
        ((IOplusAutoLayoutManager) OplusFeatureCache.getOrCreate(IOplusAutoLayoutManager.mDefault, new Object[0])).preMakePaths(aInfo, outZipPaths);
    }

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements ILoadedApkExt.IStaticExt {
        private static volatile StaticExtImpl sInstance = null;

        private StaticExtImpl() {
        }

        public static StaticExtImpl getInstance(Object base) {
            if (sInstance == null) {
                synchronized (StaticExtImpl.class) {
                    if (sInstance == null) {
                        sInstance = new StaticExtImpl();
                    }
                }
            }
            return sInstance;
        }

        public void addPathInMakePaths(ActivityThread activityThread, ApplicationInfo aInfo, List<String> outZipPaths) {
            boolean z = false;
            IOplusEnterpriseAndOperatorFeature iOplusEnterpriseAndOperatorFeature = (IOplusEnterpriseAndOperatorFeature) OplusFeatureCache.getOrCreate(IOplusEnterpriseAndOperatorFeature.DEFAULT, new Object[0]);
            if (activityThread != null && LoadedApkExtImpl.APK_CONTAINS_ENT_CERT == 1) {
                z = true;
            }
            iOplusEnterpriseAndOperatorFeature.addCustomMdmJarToPath(z, aInfo, outZipPaths);
        }

        public void modifyResourcesInGetResources(IResourcesImplExt resourcesImplExt, String packageName) {
            ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).init(resourcesImplExt, packageName);
        }

        public String innerReceiverToString(LoadedApk.ReceiverDispatcher receiverDispatcher) {
            if (receiverDispatcher != null) {
                return String.valueOf(receiverDispatcher.getIntentReceiver());
            }
            return "";
        }

        public void activityThreadHandlerOnServiceDispatcherConnected(Handler activityThread, ComponentName name, Runnable runConnection) {
            OplusExSystemServiceHelper.getInstance().ensureMediaProviderPriority(activityThread, name, runConnection);
        }

        public boolean beforeOnReceiveInArgsRunnable(LoadedApk.ReceiverDispatcher.Args args, boolean ordered, Intent intent) {
            if (ordered) {
                int flag = intent.getFlags();
                args.mPendingResultExt.setBroadcastState(flag, 2);
            }
            if ((intent.getFlags() & 524288) == 0) {
                return false;
            }
            intent.removeFlags(524288);
            return true;
        }

        public void afterOnReceiveInArgsRunnable(LoadedApk.ReceiverDispatcher.Args args, boolean ordered, Intent intent, boolean hasOplusQueueFlag) {
            if (ordered) {
                int flag = intent.getFlags();
                args.mPendingResultExt.setBroadcastState(flag, 3);
            }
            if (hasOplusQueueFlag) {
                intent.addFlags(524288);
            }
        }

        public void inExceptionOnReceiveInArgsRunnable(LoadedApk.ReceiverDispatcher.Args args, Intent intent, boolean hasOplusQueueFlag) {
            if (hasOplusQueueFlag) {
                intent.addFlags(524288);
            }
        }

        public void onPerformReceiveWithIntentNonNull(LoadedApk.ReceiverDispatcher.Args args, boolean ordered, Intent intent) {
            if (intent != null && ordered) {
                int flag = intent.getFlags();
                args.mPendingResultExt.setBroadcastState(flag, 1);
            }
        }
    }
}
