package com.android.server.location.contexthub;

import android.content.Context;
import android.hardware.contexthub.ContextHubMessage;
import android.hardware.contexthub.NanoappBinary;
import android.hardware.contexthub.NanoappInfo;
import android.hardware.contexthub.NanoappRpcService;
import android.hardware.contexthub.V1_0.ContextHubMsg;
import android.hardware.contexthub.V1_0.NanoAppBinary;
import android.hardware.contexthub.V1_2.HubAppInfo;
import android.hardware.location.ContextHubInfo;
import android.hardware.location.NanoAppMessage;
import android.hardware.location.NanoAppRpcService;
import android.hardware.location.NanoAppState;
import android.util.Log;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class ContextHubServiceUtil {
    private static final String CONTEXT_HUB_PERMISSION = "android.permission.ACCESS_CONTEXT_HUB";
    private static final String DATE_FORMAT = "MM/dd HH:mm:ss.SSS";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZoneId.systemDefault());
    private static final char HOST_ENDPOINT_BROADCAST = 65535;
    private static final String TAG = "ContextHubServiceUtil";

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int toTransactionResult(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 5) {
            return 4;
        }
        int i2 = 2;
        if (i != 2) {
            i2 = 3;
            if (i != 3) {
                return 1;
            }
        }
        return i2;
    }

    ContextHubServiceUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HashMap<Integer, ContextHubInfo> createContextHubInfoMap(List<ContextHubInfo> list) {
        HashMap<Integer, ContextHubInfo> hashMap = new HashMap<>();
        for (ContextHubInfo contextHubInfo : list) {
            hashMap.put(Integer.valueOf(contextHubInfo.getId()), contextHubInfo);
        }
        return hashMap;
    }

    static void copyToByteArrayList(byte[] bArr, ArrayList<Byte> arrayList) {
        arrayList.clear();
        arrayList.ensureCapacity(bArr.length);
        for (byte b : bArr) {
            arrayList.add(Byte.valueOf(b));
        }
    }

    static byte[] createPrimitiveByteArray(ArrayList<Byte> arrayList) {
        byte[] bArr = new byte[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            bArr[i] = arrayList.get(i).byteValue();
        }
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int[] createPrimitiveIntArray(Collection<Integer> collection) {
        int[] iArr = new int[collection.size()];
        Iterator<Integer> it = collection.iterator();
        int i = 0;
        while (it.hasNext()) {
            iArr[i] = it.next().intValue();
            i++;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static NanoAppBinary createHidlNanoAppBinary(android.hardware.location.NanoAppBinary nanoAppBinary) {
        NanoAppBinary nanoAppBinary2 = new NanoAppBinary();
        nanoAppBinary2.appId = nanoAppBinary.getNanoAppId();
        nanoAppBinary2.appVersion = nanoAppBinary.getNanoAppVersion();
        nanoAppBinary2.flags = nanoAppBinary.getFlags();
        nanoAppBinary2.targetChreApiMajorVersion = nanoAppBinary.getTargetChreApiMajorVersion();
        nanoAppBinary2.targetChreApiMinorVersion = nanoAppBinary.getTargetChreApiMinorVersion();
        try {
            copyToByteArrayList(nanoAppBinary.getBinaryNoHeader(), nanoAppBinary2.customBinary);
        } catch (IndexOutOfBoundsException e) {
            Log.w(TAG, e.getMessage());
        } catch (NullPointerException unused) {
            Log.w(TAG, "NanoApp binary was null");
        }
        return nanoAppBinary2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static NanoappBinary createAidlNanoAppBinary(android.hardware.location.NanoAppBinary nanoAppBinary) {
        NanoappBinary nanoappBinary = new NanoappBinary();
        nanoappBinary.nanoappId = nanoAppBinary.getNanoAppId();
        nanoappBinary.nanoappVersion = nanoAppBinary.getNanoAppVersion();
        nanoappBinary.flags = nanoAppBinary.getFlags();
        nanoappBinary.targetChreApiMajorVersion = nanoAppBinary.getTargetChreApiMajorVersion();
        nanoappBinary.targetChreApiMinorVersion = nanoAppBinary.getTargetChreApiMinorVersion();
        nanoappBinary.customBinary = new byte[0];
        try {
            nanoappBinary.customBinary = nanoAppBinary.getBinaryNoHeader();
        } catch (IndexOutOfBoundsException e) {
            Log.w(TAG, e.getMessage());
        } catch (NullPointerException unused) {
            Log.w(TAG, "NanoApp binary was null");
        }
        return nanoappBinary;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<NanoAppState> createNanoAppStateList(List<HubAppInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (HubAppInfo hubAppInfo : list) {
            android.hardware.contexthub.V1_0.HubAppInfo hubAppInfo2 = hubAppInfo.info_1_0;
            arrayList.add(new NanoAppState(hubAppInfo2.appId, hubAppInfo2.version, hubAppInfo2.enabled, hubAppInfo.permissions));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<NanoAppState> createNanoAppStateList(NanoappInfo[] nanoappInfoArr) {
        ArrayList arrayList = new ArrayList();
        for (NanoappInfo nanoappInfo : nanoappInfoArr) {
            ArrayList arrayList2 = new ArrayList();
            for (NanoappRpcService nanoappRpcService : nanoappInfo.rpcServices) {
                arrayList2.add(new NanoAppRpcService(nanoappRpcService.id, nanoappRpcService.version));
            }
            arrayList.add(new NanoAppState(nanoappInfo.nanoappId, nanoappInfo.nanoappVersion, nanoappInfo.enabled, new ArrayList(Arrays.asList(nanoappInfo.permissions)), arrayList2));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ContextHubMsg createHidlContextHubMessage(short s, NanoAppMessage nanoAppMessage) {
        ContextHubMsg contextHubMsg = new ContextHubMsg();
        contextHubMsg.appName = nanoAppMessage.getNanoAppId();
        contextHubMsg.hostEndPoint = s;
        contextHubMsg.msgType = nanoAppMessage.getMessageType();
        copyToByteArrayList(nanoAppMessage.getMessageBody(), contextHubMsg.msg);
        return contextHubMsg;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ContextHubMessage createAidlContextHubMessage(short s, NanoAppMessage nanoAppMessage) {
        ContextHubMessage contextHubMessage = new ContextHubMessage();
        contextHubMessage.nanoappId = nanoAppMessage.getNanoAppId();
        contextHubMessage.hostEndPoint = (char) s;
        contextHubMessage.messageType = nanoAppMessage.getMessageType();
        contextHubMessage.messageBody = nanoAppMessage.getMessageBody();
        contextHubMessage.permissions = new String[0];
        return contextHubMessage;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static NanoAppMessage createNanoAppMessage(ContextHubMsg contextHubMsg) {
        return NanoAppMessage.createMessageFromNanoApp(contextHubMsg.appName, contextHubMsg.msgType, createPrimitiveByteArray(contextHubMsg.msg), contextHubMsg.hostEndPoint == -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static NanoAppMessage createNanoAppMessage(ContextHubMessage contextHubMessage) {
        return NanoAppMessage.createMessageFromNanoApp(contextHubMessage.nanoappId, contextHubMessage.messageType, contextHubMessage.messageBody, contextHubMessage.hostEndPoint == 65535);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkPermissions(Context context) {
        context.enforceCallingOrSelfPermission(CONTEXT_HUB_PERMISSION, "ACCESS_CONTEXT_HUB permission required to use Context Hub");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList<HubAppInfo> toHubAppInfo_1_2(ArrayList<android.hardware.contexthub.V1_0.HubAppInfo> arrayList) {
        ArrayList<HubAppInfo> arrayList2 = new ArrayList<>();
        Iterator<android.hardware.contexthub.V1_0.HubAppInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            android.hardware.contexthub.V1_0.HubAppInfo next = it.next();
            HubAppInfo hubAppInfo = new HubAppInfo();
            android.hardware.contexthub.V1_0.HubAppInfo hubAppInfo2 = hubAppInfo.info_1_0;
            hubAppInfo2.appId = next.appId;
            hubAppInfo2.version = next.version;
            hubAppInfo2.memUsage = next.memUsage;
            hubAppInfo2.enabled = next.enabled;
            hubAppInfo.permissions = new ArrayList();
            arrayList2.add(hubAppInfo);
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int toContextHubEvent(int i) {
        if (i == 1) {
            return 1;
        }
        Log.e(TAG, "toContextHubEvent: Unknown event type: " + i);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int toContextHubEventFromAidl(int i) {
        if (i == 1) {
            return 1;
        }
        Log.e(TAG, "toContextHubEventFromAidl: Unknown event type: " + i);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String formatDateFromTimestamp(long j) {
        return DATE_FORMATTER.format(Instant.ofEpochMilli(j));
    }
}
