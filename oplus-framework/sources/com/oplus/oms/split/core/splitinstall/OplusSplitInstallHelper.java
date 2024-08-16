package com.oplus.oms.split.core.splitinstall;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitload.SplitCompatResourcesException;
import com.oplus.oms.split.splitload.SplitCompatResourcesLoader;
import com.oplus.oms.split.splitload.SplitLibraryLoaderHelper;
import java.io.File;

/* loaded from: classes.dex */
public class OplusSplitInstallHelper {
    private static final String TAG = "SplitInstallHelper";

    private OplusSplitInstallHelper() {
    }

    public static void loadResources(Activity activity, Resources resources) {
        try {
            SplitCompatResourcesLoader.loadResources(activity, resources);
        } catch (SplitCompatResourcesException throwable) {
            throw new RuntimeException("Failed to load activity resources", throwable);
        }
    }

    public static void loadResources(Service service) {
        try {
            SplitCompatResourcesLoader.loadResources(service, service.getBaseContext().getResources());
        } catch (SplitCompatResourcesException throwable) {
            throw new RuntimeException("Failed to load service resources", throwable);
        }
    }

    public static void loadResources(BroadcastReceiver receiver, Context context) {
        if (context.getClass().getSimpleName().equals("ReceiverRestrictedContext")) {
            try {
                Context base = ((ContextWrapper) context).getBaseContext();
                SplitCompatResourcesLoader.loadResources(base, context.getResources());
            } catch (SplitCompatResourcesException throwable) {
                throw new RuntimeException("Failed to load receiver resources", throwable);
            }
        }
    }

    public static void loadLibrary(Context context, String libraryName) {
        if (!SplitLibraryLoaderHelper.loadSplitLibrary(context, libraryName)) {
            try {
                SplitLog.d(TAG, "loadLibrary system, lib: %s", libraryName);
                System.loadLibrary(libraryName);
            } catch (UnsatisfiedLinkError error) {
                boolean loadLibOK = false;
                try {
                    SplitLog.d(TAG, "loadLibrary application, lib: %s", libraryName);
                    String nativeLibraryDir = context.getApplicationInfo().nativeLibraryDir;
                    String libName = System.mapLibraryName(libraryName);
                    String targetLibFile = nativeLibraryDir + "/" + libName;
                    if (new File(targetLibFile).exists()) {
                        System.load(targetLibFile);
                        loadLibOK = true;
                    }
                    if (!loadLibOK) {
                        SplitLog.e(TAG, "loadLibrary failed, lib: %s", libraryName);
                        throw error;
                    }
                } catch (UnsatisfiedLinkError e) {
                    SplitLog.e(TAG, "loadLibrary error, lib: %s, msg: %s", libraryName, e.getMessage());
                    throw e;
                }
            }
        }
    }
}
