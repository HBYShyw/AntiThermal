package com.android.server.app;

import android.R;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Slog;
import android.util.Xml;
import com.android.server.SystemService;
import com.android.server.app.GameServiceConfiguration;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class GameServiceProviderSelectorImpl implements GameServiceProviderSelector {
    private static final boolean DEBUG = false;
    private static final String GAME_SERVICE_NODE_NAME = "game-service";
    private static final String TAG = "GameServiceProviderSelector";
    private final PackageManager mPackageManager;
    private final Resources mResources;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GameServiceProviderSelectorImpl(Resources resources, PackageManager packageManager) {
        this.mResources = resources;
        this.mPackageManager = packageManager;
    }

    @Override // com.android.server.app.GameServiceProviderSelector
    public GameServiceConfiguration get(SystemService.TargetUser targetUser, String str) {
        GameServiceConfiguration gameServiceConfiguration;
        ComponentName determineGameSessionServiceFromGameService;
        if (targetUser == null) {
            return null;
        }
        int i = 0;
        if (!(targetUser.isFull() && !targetUser.isManagedProfile())) {
            Slog.i(TAG, "Game Service not supported for user: " + targetUser.getUserIdentifier());
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            str = this.mResources.getString(R.string.display_manager_overlay_display_name);
            i = AudioDevice.OUT_FM;
        }
        if (TextUtils.isEmpty(str)) {
            Slog.w(TAG, "No game service package defined");
            return null;
        }
        int userIdentifier = targetUser.getUserIdentifier();
        List queryIntentServicesAsUser = this.mPackageManager.queryIntentServicesAsUser(new Intent("android.service.games.action.GAME_SERVICE").setPackage(str), i | 128, userIdentifier);
        if (queryIntentServicesAsUser == null || queryIntentServicesAsUser.isEmpty()) {
            Slog.w(TAG, "No available game service found for user id: " + userIdentifier);
            return new GameServiceConfiguration(str, null);
        }
        Iterator it = queryIntentServicesAsUser.iterator();
        while (true) {
            if (!it.hasNext()) {
                gameServiceConfiguration = null;
                break;
            }
            ServiceInfo serviceInfo = ((ResolveInfo) it.next()).serviceInfo;
            if (serviceInfo != null && (determineGameSessionServiceFromGameService = determineGameSessionServiceFromGameService(serviceInfo)) != null) {
                gameServiceConfiguration = new GameServiceConfiguration(str, new GameServiceConfiguration.GameServiceComponentConfiguration(new UserHandle(userIdentifier), serviceInfo.getComponentName(), determineGameSessionServiceFromGameService));
                break;
            }
        }
        if (gameServiceConfiguration != null) {
            return gameServiceConfiguration;
        }
        Slog.w(TAG, "No valid game service found for user id: " + userIdentifier);
        return new GameServiceConfiguration(str, null);
    }

    private ComponentName determineGameSessionServiceFromGameService(ServiceInfo serviceInfo) {
        int next;
        try {
            XmlResourceParser loadXmlMetaData = serviceInfo.loadXmlMetaData(this.mPackageManager, "android.game_service");
            try {
                if (loadXmlMetaData == null) {
                    Slog.w(TAG, "No android.game_service meta-data found for " + serviceInfo.getComponentName());
                    if (loadXmlMetaData != null) {
                        loadXmlMetaData.close();
                    }
                    return null;
                }
                Resources resourcesForApplication = this.mPackageManager.getResourcesForApplication(serviceInfo.packageName);
                AttributeSet asAttributeSet = Xml.asAttributeSet(loadXmlMetaData);
                do {
                    next = loadXmlMetaData.next();
                    if (next == 1) {
                        break;
                    }
                } while (next != 2);
                if (!GAME_SERVICE_NODE_NAME.equals(loadXmlMetaData.getName())) {
                    Slog.w(TAG, "Meta-data does not start with game-service tag");
                    loadXmlMetaData.close();
                    return null;
                }
                TypedArray obtainAttributes = resourcesForApplication.obtainAttributes(asAttributeSet, com.android.internal.R.styleable.GameService);
                String string = obtainAttributes.getString(0);
                obtainAttributes.recycle();
                loadXmlMetaData.close();
                if (TextUtils.isEmpty(string)) {
                    Slog.w(TAG, "No gameSessionService specified");
                    return null;
                }
                ComponentName componentName = new ComponentName(serviceInfo.packageName, string);
                try {
                    this.mPackageManager.getServiceInfo(componentName, 0);
                    return componentName;
                } catch (PackageManager.NameNotFoundException unused) {
                    Slog.w(TAG, "GameSessionService does not exist: " + componentName);
                    return null;
                }
            } catch (Throwable th) {
                if (loadXmlMetaData != null) {
                    try {
                        loadXmlMetaData.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (PackageManager.NameNotFoundException | IOException | XmlPullParserException e) {
            Slog.w("Error while parsing meta-data for " + serviceInfo.getComponentName(), e);
            return null;
        }
    }
}
