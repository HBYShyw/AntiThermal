package com.oplus.oms.split.splitload;

import android.app.Application;
import android.content.Context;
import com.oplus.oms.split.splitload.extension.AABExtension;
import com.oplus.oms.split.splitload.extension.AABExtensionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
final class SplitActivator {
    private final AABExtension mAabExtension = AABExtension.getInstance();
    private final Context mAppContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitActivator(Context context) {
        this.mAppContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Application createSplitApplication(ClassLoader classLoader, String splitName) throws SplitLoadException {
        try {
            Application app = this.mAabExtension.createApplication(classLoader, splitName);
            return app;
        } catch (AABExtensionException e) {
            throw new SplitLoadException(-24, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attachSplitApplication(Application application) throws SplitLoadException {
        try {
            this.mAabExtension.activeApplication(application, this.mAppContext);
        } catch (AABExtensionException e) {
            throw new SplitLoadException(-25, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invokeOnCreateForSplitApplication(Application application) throws SplitLoadException {
        if (application != null) {
            try {
                Method method = HiddenApiReflection.findMethod((Class<?>) Application.class, "onCreate", (Class<?>[]) new Class[0]);
                method.invoke(application, new Object[0]);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new SplitLoadException(-25, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createAndActivateSplitContentProviders(ClassLoader classLoader, String splitName) throws SplitLoadException {
        try {
            this.mAabExtension.createAndActivateSplitProviders(classLoader, splitName);
        } catch (AABExtensionException e) {
            throw new SplitLoadException(-26, e);
        }
    }
}
