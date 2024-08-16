package com.android.server.gpu;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import android.updatabledriver.UpdatableDriverProto;
import android.util.Base64;
import com.android.framework.protobuf.InvalidProtocolBufferException;
import com.android.internal.annotations.GuardedBy;
import com.android.server.SystemService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class GpuService extends SystemService {
    private static final int BASE64_FLAGS = 3;
    public static final boolean DEBUG = false;
    private static final String DEV_DRIVER_PROPERTY = "ro.gfx.driver.1";
    private static final String PROD_DRIVER_PROPERTY = "ro.gfx.driver.0";
    public static final String TAG = "GpuService";
    private static final String UPDATABLE_DRIVER_PRODUCTION_ALLOWLIST_FILENAME = "allowlist.txt";
    private ContentResolver mContentResolver;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private UpdatableDriverProto.Denylists mDenylists;
    private final String mDevDriverPackageName;
    private DeviceConfigListener mDeviceConfigListener;
    private final Object mDeviceConfigLock;
    private final boolean mHasDevDriver;
    private final boolean mHasProdDriver;
    private final Object mLock;
    private final PackageManager mPackageManager;
    private final String mProdDriverPackageName;
    private long mProdDriverVersionCode;
    private SettingsObserver mSettingsObserver;

    private static native void nSetUpdatableDriverPath(String str);

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    public GpuService(Context context) {
        super(context);
        this.mLock = new Object();
        this.mDeviceConfigLock = new Object();
        this.mContext = context;
        String str = SystemProperties.get(PROD_DRIVER_PROPERTY);
        this.mProdDriverPackageName = str;
        this.mProdDriverVersionCode = -1L;
        String str2 = SystemProperties.get(DEV_DRIVER_PROPERTY);
        this.mDevDriverPackageName = str2;
        this.mPackageManager = context.getPackageManager();
        boolean z = !TextUtils.isEmpty(str);
        this.mHasProdDriver = z;
        boolean z2 = !TextUtils.isEmpty(str2);
        this.mHasDevDriver = z2;
        if (z2 || z) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            getContext().registerReceiverAsUser(new PackageReceiver(), UserHandle.ALL, intentFilter, null, null);
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 1000) {
            this.mContentResolver = this.mContext.getContentResolver();
            if (this.mHasProdDriver || this.mHasDevDriver) {
                this.mSettingsObserver = new SettingsObserver();
                this.mDeviceConfigListener = new DeviceConfigListener();
                fetchProductionDriverPackageProperties();
                processDenylists();
                setDenylist();
                fetchPrereleaseDriverPackageProperties();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class SettingsObserver extends ContentObserver {
        private final Uri mProdDriverDenylistsUri;

        SettingsObserver() {
            super(new Handler());
            Uri uriFor = Settings.Global.getUriFor("updatable_driver_production_denylists");
            this.mProdDriverDenylistsUri = uriFor;
            GpuService.this.mContentResolver.registerContentObserver(uriFor, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (uri != null && this.mProdDriverDenylistsUri.equals(uri)) {
                GpuService.this.processDenylists();
                GpuService.this.setDenylist();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class DeviceConfigListener implements DeviceConfig.OnPropertiesChangedListener {
        DeviceConfigListener() {
            DeviceConfig.addOnPropertiesChangedListener("game_driver", GpuService.this.mContext.getMainExecutor(), this);
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            synchronized (GpuService.this.mDeviceConfigLock) {
                if (properties.getKeyset().contains("updatable_driver_production_denylists")) {
                    GpuService.this.parseDenylists(properties.getString("updatable_driver_production_denylists", ""));
                    GpuService.this.setDenylist();
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class PackageReceiver extends BroadcastReceiver {
        private PackageReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
            boolean equals = schemeSpecificPart.equals(GpuService.this.mProdDriverPackageName);
            boolean equals2 = schemeSpecificPart.equals(GpuService.this.mDevDriverPackageName);
            if (equals || equals2) {
                String action = intent.getAction();
                action.hashCode();
                char c = 65535;
                switch (action.hashCode()) {
                    case 172491798:
                        if (action.equals("android.intent.action.PACKAGE_CHANGED")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 525384130:
                        if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1544582882:
                        if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                        if (equals) {
                            GpuService.this.fetchProductionDriverPackageProperties();
                            GpuService.this.setDenylist();
                            return;
                        } else {
                            if (equals2) {
                                GpuService.this.fetchPrereleaseDriverPackageProperties();
                                return;
                            }
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }

    private static void assetToSettingsGlobal(Context context, Context context2, String str, String str2, CharSequence charSequence) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context2.getAssets().open(str)));
            ArrayList arrayList = new ArrayList();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    arrayList.add(readLine);
                } else {
                    Settings.Global.putString(context.getContentResolver(), str2, String.join(charSequence, arrayList));
                    return;
                }
            }
        } catch (IOException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchProductionDriverPackageProperties() {
        try {
            ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(this.mProdDriverPackageName, AudioDevice.OUT_FM);
            if (applicationInfo.targetSdkVersion < 26) {
                return;
            }
            Settings.Global.putString(this.mContentResolver, "updatable_driver_production_allowlist", "");
            this.mProdDriverVersionCode = applicationInfo.longVersionCode;
            assetToSettingsGlobal(this.mContext, this.mContext.createPackageContext(this.mProdDriverPackageName, 4), UPDATABLE_DRIVER_PRODUCTION_ALLOWLIST_FILENAME, "updatable_driver_production_allowlist", ",");
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDenylists() {
        String property = DeviceConfig.getProperty("game_driver", "updatable_driver_production_denylists");
        if (property == null) {
            property = Settings.Global.getString(this.mContentResolver, "updatable_driver_production_denylists");
        }
        if (property == null) {
            property = "";
        }
        parseDenylists(property);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseDenylists(String str) {
        synchronized (this.mLock) {
            this.mDenylists = null;
            try {
                this.mDenylists = UpdatableDriverProto.Denylists.parseFrom(Base64.decode(str, 3));
            } catch (IllegalArgumentException | InvalidProtocolBufferException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDenylist() {
        Settings.Global.putString(this.mContentResolver, "updatable_driver_production_denylist", "");
        synchronized (this.mLock) {
            UpdatableDriverProto.Denylists denylists = this.mDenylists;
            if (denylists == null) {
                return;
            }
            for (UpdatableDriverProto.Denylist denylist : denylists.getDenylistsList()) {
                if (denylist.getVersionCode() == this.mProdDriverVersionCode) {
                    Settings.Global.putString(this.mContentResolver, "updatable_driver_production_denylist", String.join(",", denylist.getPackageNamesList()));
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchPrereleaseDriverPackageProperties() {
        try {
            ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(this.mDevDriverPackageName, AudioDevice.OUT_FM);
            if (applicationInfo.targetSdkVersion < 26) {
                return;
            }
            setUpdatableDriverPath(applicationInfo);
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    private void setUpdatableDriverPath(ApplicationInfo applicationInfo) {
        if (applicationInfo.primaryCpuAbi == null) {
            nSetUpdatableDriverPath("");
            return;
        }
        nSetUpdatableDriverPath(applicationInfo.sourceDir + "!/lib/");
    }
}
