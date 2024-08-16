package com.android.server.voiceinteraction;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class RecognitionServiceInfo {
    private static final String TAG = "RecognitionServiceInfo";
    private final String mParseError;
    private final boolean mSelectableAsDefault;
    private final ServiceInfo mServiceInfo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<RecognitionServiceInfo> getAvailableServices(Context context, int i) {
        ArrayList arrayList = new ArrayList();
        Iterator it = context.getPackageManager().queryIntentServicesAsUser(new Intent("android.speech.RecognitionService"), 786432, i).iterator();
        while (it.hasNext()) {
            RecognitionServiceInfo parseInfo = parseInfo(context.getPackageManager(), ((ResolveInfo) it.next()).serviceInfo);
            if (!TextUtils.isEmpty(parseInfo.mParseError)) {
                Log.w(TAG, "Parse error in getAvailableServices: " + parseInfo.mParseError);
            }
            arrayList.add(parseInfo);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RecognitionServiceInfo parseInfo(PackageManager packageManager, ServiceInfo serviceInfo) {
        String str;
        XmlResourceParser loadXmlMetaData;
        boolean z = true;
        try {
            loadXmlMetaData = serviceInfo.loadXmlMetaData(packageManager, "android.speech");
        } catch (PackageManager.NameNotFoundException | IOException | XmlPullParserException e) {
            str = "Error parsing recognition service meta-data: " + e;
        }
        try {
            if (loadXmlMetaData == null) {
                RecognitionServiceInfo recognitionServiceInfo = new RecognitionServiceInfo(serviceInfo, true, "No android.speech meta-data for " + serviceInfo.packageName);
                if (loadXmlMetaData != null) {
                    loadXmlMetaData.close();
                }
                return recognitionServiceInfo;
            }
            Resources resourcesForApplication = packageManager.getResourcesForApplication(serviceInfo.applicationInfo);
            AttributeSet asAttributeSet = Xml.asAttributeSet(loadXmlMetaData);
            for (int i = 0; i != 1 && i != 2; i = loadXmlMetaData.next()) {
            }
            if (!"recognition-service".equals(loadXmlMetaData.getName())) {
                throw new XmlPullParserException("Meta-data does not start with recognition-service tag");
            }
            TypedArray obtainAttributes = resourcesForApplication.obtainAttributes(asAttributeSet, R.styleable.RecognitionService);
            z = obtainAttributes.getBoolean(1, true);
            obtainAttributes.recycle();
            loadXmlMetaData.close();
            str = "";
            return new RecognitionServiceInfo(serviceInfo, z, str);
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
    }

    private RecognitionServiceInfo(ServiceInfo serviceInfo, boolean z, String str) {
        this.mServiceInfo = serviceInfo;
        this.mSelectableAsDefault = z;
        this.mParseError = str;
    }

    public String getParseError() {
        return this.mParseError;
    }

    public ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public boolean isSelectableAsDefault() {
        return this.mSelectableAsDefault;
    }
}
