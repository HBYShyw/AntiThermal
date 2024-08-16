package com.android.server.pm;

import android.R;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.content.pm.ModuleInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.XmlUtils;
import com.android.server.pm.verify.domain.DomainVerificationLegacySettings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ModuleInfoProvider {
    private static final String MODULE_METADATA_KEY = "android.content.pm.MODULE_METADATA";
    private static final String TAG = "PackageManager.ModuleInfoProvider";
    private final ApexManager mApexManager;
    private final Context mContext;
    private volatile boolean mMetadataLoaded;
    private final Map<String, ModuleInfo> mModuleInfo;
    private IPackageManager mPackageManager;
    private volatile String mPackageName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ModuleInfoProvider(Context context) {
        this.mContext = context;
        this.mApexManager = ApexManager.getInstance();
        this.mModuleInfo = new ArrayMap();
    }

    @VisibleForTesting
    public ModuleInfoProvider(XmlResourceParser xmlResourceParser, Resources resources, ApexManager apexManager) {
        this.mContext = null;
        this.mApexManager = apexManager;
        this.mModuleInfo = new ArrayMap();
        loadModuleMetadata(xmlResourceParser, resources);
    }

    private IPackageManager getPackageManager() {
        if (this.mPackageManager == null) {
            this.mPackageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        }
        return this.mPackageManager;
    }

    public void systemReady() {
        this.mPackageName = this.mContext.getResources().getString(R.string.config_overrideComponentUiPackage);
        if (TextUtils.isEmpty(this.mPackageName)) {
            Slog.w(TAG, "No configured module metadata provider.");
            return;
        }
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(this.mPackageName, 128L, 0);
            Resources resources = this.mContext.createPackageContext(this.mPackageName, 0).getResources();
            loadModuleMetadata(resources.getXml(packageInfo.applicationInfo.metaData.getInt(MODULE_METADATA_KEY)), resources);
        } catch (PackageManager.NameNotFoundException | RemoteException e) {
            Slog.w(TAG, "Unable to discover metadata package: " + this.mPackageName, e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0020, code lost:
    
        android.util.Slog.w(com.android.server.pm.ModuleInfoProvider.TAG, "Unexpected metadata element: " + r7.getName());
        r6.mModuleInfo.clear();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void loadModuleMetadata(XmlResourceParser xmlResourceParser, Resources resources) {
        try {
            try {
                XmlUtils.beginDocument(xmlResourceParser, "module-metadata");
                while (true) {
                    XmlUtils.nextElement(xmlResourceParser);
                    if (xmlResourceParser.getEventType() == 1) {
                        break;
                    }
                    if (!"module".equals(xmlResourceParser.getName())) {
                        break;
                    }
                    CharSequence text = resources.getText(Integer.parseInt(xmlResourceParser.getAttributeValue(null, "name").substring(1)));
                    String readStringAttribute = XmlUtils.readStringAttribute(xmlResourceParser, DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
                    boolean readBooleanAttribute = XmlUtils.readBooleanAttribute(xmlResourceParser, "isHidden");
                    ModuleInfo moduleInfo = new ModuleInfo();
                    moduleInfo.setHidden(readBooleanAttribute);
                    moduleInfo.setPackageName(readStringAttribute);
                    moduleInfo.setName(text);
                    moduleInfo.setApexModuleName(this.mApexManager.getApexModuleNameForPackageName(readStringAttribute));
                    this.mModuleInfo.put(readStringAttribute, moduleInfo);
                }
            } catch (IOException | XmlPullParserException e) {
                Slog.w(TAG, "Error parsing module metadata", e);
                this.mModuleInfo.clear();
            }
        } finally {
            xmlResourceParser.close();
            this.mMetadataLoaded = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<ModuleInfo> getInstalledModules(int i) {
        if (!this.mMetadataLoaded) {
            throw new IllegalStateException("Call to getInstalledModules before metadata loaded");
        }
        if ((131072 & i) != 0) {
            return new ArrayList(this.mModuleInfo.values());
        }
        try {
            List list = getPackageManager().getInstalledPackages(i | 1073741824, UserHandle.getCallingUserId()).getList();
            ArrayList arrayList = new ArrayList(list.size());
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ModuleInfo moduleInfo = this.mModuleInfo.get(((PackageInfo) it.next()).packageName);
                if (moduleInfo != null) {
                    arrayList.add(moduleInfo);
                }
            }
            return arrayList;
        } catch (RemoteException e) {
            Slog.w(TAG, "Unable to retrieve all package names", e);
            return Collections.emptyList();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ModuleInfo getModuleInfo(String str, int i) {
        if (!this.mMetadataLoaded) {
            throw new IllegalStateException("Call to getModuleInfo before metadata loaded");
        }
        if ((i & 1) != 0) {
            for (ModuleInfo moduleInfo : this.mModuleInfo.values()) {
                if (str.equals(moduleInfo.getApexModuleName())) {
                    return moduleInfo;
                }
            }
            return null;
        }
        return this.mModuleInfo.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getPackageName() {
        if (!this.mMetadataLoaded) {
            throw new IllegalStateException("Call to getVersion before metadata loaded");
        }
        return this.mPackageName;
    }
}
