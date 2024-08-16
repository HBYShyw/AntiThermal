package android.app;

import android.app.IContextImplExt;
import android.content.Context;
import android.content.Intent;
import android.content.pm.OplusPackageManager;
import android.content.res.Resources;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.Display;
import android.window.WindowContext;
import android.window.WindowTokenClient;
import com.oplus.flexiblewindow.FlexibleWindowManager;
import java.io.File;

/* loaded from: classes.dex */
public class ContextImplExtImpl implements IContextImplExt {
    public ContextImplExtImpl(Object contextImpl) {
    }

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements IContextImplExt.IStaticExt {
        private static final int FORCE_DISABLE_START_BG_APP_SERVICE_CRASH = 696;
        private static final int SKIP_START_BG_APP_SERVICE_CRASH = 711;
        private static final String TAG = "ContextImplExtImpl";
        private static boolean sMediaMounted = false;
        static boolean DEBUG_CIL = SystemProperties.getBoolean("persist.sys.assert.panic", false);

        /* loaded from: classes.dex */
        public static class SingleHolder {
            private static final StaticExtImpl INSTACNE = new StaticExtImpl();
        }

        public static StaticExtImpl getInstance(Object object) {
            return SingleHolder.INSTACNE;
        }

        public boolean hookStartServiceCommon(Context context, Intent service) {
            OplusPackageManager pm = new OplusPackageManager(context);
            if (pm.inCptWhiteList(FORCE_DISABLE_START_BG_APP_SERVICE_CRASH, context.getPackageName())) {
                return true;
            }
            if (service != null) {
                if (DEBUG_CIL) {
                    Log.e(TAG, "The package of the service is " + service.getPackage() + " The component of the service is " + service.getComponent());
                }
                return (service.getComponent() != null && pm.inCptWhiteList(SKIP_START_BG_APP_SERVICE_CRASH, service.getComponent().getPackageName())) || (service.getPackage() != null && pm.inCptWhiteList(SKIP_START_BG_APP_SERVICE_CRASH, service.getPackage()));
            }
            return false;
        }

        public void hookSetResources(Resources mResources, String mPackageName) {
            if (mResources != null && mResources.getImpl() != null && mResources.getImpl().mResourcesImplExt != null) {
                mResources.getImpl().mResourcesImplExt.init(mPackageName);
            }
        }

        public void checkExternalStorageStateMounted(String packageName) {
            int wait = 0;
            while (!sMediaMounted) {
                wait++;
                boolean equals = Environment.getExternalStorageState().equals("mounted");
                sMediaMounted = equals;
                if (!equals) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                    if (wait == 30) {
                        Log.i(TAG, "ensure external dir,storage is not prepared!:" + packageName + ",time's up!");
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        public void fixupDirIfNeed(File dir, StorageManager sm) {
            if (!dir.canWrite() || !dir.canRead()) {
                Log.i(TAG, "path:" + dir + " can not read or write, try to fix it");
                sm.fixupAppDir(dir);
            }
        }

        public boolean checkUnregisterReceiver(Context context) {
            WindowTokenClient wtc;
            return (context instanceof WindowContext) && (wtc = context.getWindowContextToken()) != null && wtc.getWrapper().isContextWeakRefRelease();
        }

        public void onRegisterReceiver() {
            if (StrictMode.mStrictModeExt.isPerfMonitorEnable()) {
                StrictMode.mStrictModeExt.onRegisterReceiver();
            }
        }

        public void onBindService() {
            if (StrictMode.mStrictModeExt.isPerfMonitorEnable()) {
                StrictMode.mStrictModeExt.onBindService();
            }
        }

        public void onContentProvider() {
            if (StrictMode.mStrictModeExt.isPerfMonitorEnable()) {
                StrictMode.mStrictModeExt.onContentProvider();
            }
        }

        public Display getDisplayNoVerify(Context context) {
            if ((context instanceof Service) && context != null && ((Service) context).getBaseContext() != null && FlexibleWindowManager.getInstance().hasFSDFeature()) {
                Context appContext = context.getApplicationContext();
                Resources appResource = appContext == null ? null : appContext.getResources();
                int scenario = appResource == null ? 0 : appResource.getConfiguration().getOplusExtraConfiguration().getScenario();
                if (appResource != null && scenario == 3 && appContext.getDisplayNoVerify().getDisplayId() == 1) {
                    return appContext.getDisplayNoVerify();
                }
            }
            return null;
        }
    }
}
