package com.android.server.devicepolicy;

import android.app.admin.StartInstallingUpdateCallback;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.os.UpdateEngine;
import android.os.UpdateEngineCallback;
import android.util.Log;
import com.android.server.devicepolicy.DevicePolicyManagerService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AbUpdateInstaller extends UpdateInstaller {
    private static final int DOWNLOAD_STATE_INITIALIZATION_ERROR = 20;
    private static final int OFFSET_TO_FILE_NAME = 30;
    private static final String PAYLOAD_BIN = "payload.bin";
    private static final String PAYLOAD_PROPERTIES_TXT = "payload_properties.txt";
    public static final String UNKNOWN_ERROR = "Unknown error with error code = ";
    private static final Map<Integer, Integer> errorCodesMap = buildErrorCodesMap();
    private static final Map<Integer, String> errorStringsMap = buildErrorStringsMap();
    private Enumeration<? extends ZipEntry> mEntries;
    private long mOffsetForUpdate;
    private ZipFile mPackedUpdateFile;
    private List<String> mProperties;
    private long mSizeForUpdate;
    private boolean mUpdateInstalled;

    private static Map<Integer, Integer> buildErrorCodesMap() {
        HashMap hashMap = new HashMap();
        hashMap.put(1, 1);
        hashMap.put(20, 2);
        hashMap.put(51, 2);
        hashMap.put(12, 3);
        hashMap.put(11, 3);
        hashMap.put(6, 3);
        hashMap.put(10, 3);
        hashMap.put(26, 3);
        hashMap.put(5, 1);
        hashMap.put(7, 1);
        hashMap.put(9, 1);
        hashMap.put(52, 1);
        return hashMap;
    }

    private static Map<Integer, String> buildErrorStringsMap() {
        HashMap hashMap = new HashMap();
        hashMap.put(1, UNKNOWN_ERROR);
        hashMap.put(20, "The delta update payload was targeted for another version or the source partitionwas modified after it was installed");
        hashMap.put(5, "Failed to finish the configured postinstall works.");
        hashMap.put(7, "Failed to open one of the partitions it tried to write to or read data from.");
        hashMap.put(6, "Payload mismatch error.");
        hashMap.put(9, "Failed to read the payload data from the given URL.");
        hashMap.put(10, "Payload hash error.");
        hashMap.put(11, "Payload size mismatch error.");
        hashMap.put(12, "Failed to verify the signature of the payload.");
        hashMap.put(52, "The payload has been successfully installed,but the active slot was not flipped.");
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbUpdateInstaller(Context context, ParcelFileDescriptor parcelFileDescriptor, StartInstallingUpdateCallback startInstallingUpdateCallback, DevicePolicyManagerService.Injector injector, DevicePolicyConstants devicePolicyConstants) {
        super(context, parcelFileDescriptor, startInstallingUpdateCallback, injector, devicePolicyConstants);
        this.mUpdateInstalled = false;
    }

    @Override // com.android.server.devicepolicy.UpdateInstaller
    public void installUpdateInThread() {
        if (this.mUpdateInstalled) {
            throw new IllegalStateException("installUpdateInThread can be called only once.");
        }
        try {
            setState();
            applyPayload(Paths.get(this.mCopiedUpdateFile.getAbsolutePath(), new String[0]).toUri().toString());
        } catch (ZipException e) {
            Log.w("UpdateInstaller", e);
            notifyCallbackOnError(3, Log.getStackTraceString(e));
        } catch (IOException e2) {
            Log.w("UpdateInstaller", e2);
            notifyCallbackOnError(1, Log.getStackTraceString(e2));
        }
    }

    private void setState() throws IOException {
        this.mUpdateInstalled = true;
        this.mPackedUpdateFile = new ZipFile(this.mCopiedUpdateFile);
        this.mProperties = new ArrayList();
        this.mSizeForUpdate = -1L;
        this.mOffsetForUpdate = 0L;
        this.mEntries = this.mPackedUpdateFile.entries();
    }

    private UpdateEngine buildBoundUpdateEngine() {
        UpdateEngine updateEngine = new UpdateEngine();
        updateEngine.bind(new DelegatingUpdateEngineCallback(this, updateEngine));
        return updateEngine;
    }

    private void applyPayload(String str) throws IOException {
        if (updateStateForPayload()) {
            String[] strArr = (String[]) this.mProperties.stream().toArray(new IntFunction() { // from class: com.android.server.devicepolicy.AbUpdateInstaller$$ExternalSyntheticLambda0
                @Override // java.util.function.IntFunction
                public final Object apply(int i) {
                    String[] lambda$applyPayload$0;
                    lambda$applyPayload$0 = AbUpdateInstaller.lambda$applyPayload$0(i);
                    return lambda$applyPayload$0;
                }
            });
            if (this.mSizeForUpdate == -1) {
                Log.w("UpdateInstaller", "Failed to find payload entry in the given package.");
                notifyCallbackOnError(3, "Failed to find payload entry in the given package.");
                return;
            }
            try {
                buildBoundUpdateEngine().applyPayload(str, this.mOffsetForUpdate, this.mSizeForUpdate, strArr);
            } catch (Exception e) {
                Log.w("UpdateInstaller", "Failed to install update from file.", e);
                notifyCallbackOnError(1, "Failed to install update from file.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String[] lambda$applyPayload$0(int i) {
        return new String[i];
    }

    private boolean updateStateForPayload() throws IOException {
        long j = 0;
        while (this.mEntries.hasMoreElements()) {
            ZipEntry nextElement = this.mEntries.nextElement();
            String name = nextElement.getName();
            j += buildOffsetForEntry(nextElement, name);
            if (nextElement.isDirectory()) {
                j -= nextElement.getCompressedSize();
            } else if (PAYLOAD_BIN.equals(name)) {
                if (nextElement.getMethod() != 0) {
                    Log.w("UpdateInstaller", "Invalid compression method.");
                    notifyCallbackOnError(3, "Invalid compression method.");
                    return false;
                }
                this.mSizeForUpdate = nextElement.getCompressedSize();
                this.mOffsetForUpdate = j - nextElement.getCompressedSize();
            } else if (PAYLOAD_PROPERTIES_TXT.equals(name)) {
                updatePropertiesForEntry(nextElement);
            }
        }
        return true;
    }

    private long buildOffsetForEntry(ZipEntry zipEntry, String str) {
        return str.length() + 30 + zipEntry.getCompressedSize() + (zipEntry.getExtra() == null ? 0 : zipEntry.getExtra().length);
    }

    private void updatePropertiesForEntry(ZipEntry zipEntry) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.mPackedUpdateFile.getInputStream(zipEntry)));
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    this.mProperties.add(readLine);
                } else {
                    bufferedReader.close();
                    return;
                }
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class DelegatingUpdateEngineCallback extends UpdateEngineCallback {
        private UpdateEngine mUpdateEngine;
        private UpdateInstaller mUpdateInstaller;

        public void onStatusUpdate(int i, float f) {
        }

        DelegatingUpdateEngineCallback(UpdateInstaller updateInstaller, UpdateEngine updateEngine) {
            this.mUpdateInstaller = updateInstaller;
            this.mUpdateEngine = updateEngine;
        }

        public void onPayloadApplicationComplete(int i) {
            this.mUpdateEngine.unbind();
            if (i == 0) {
                this.mUpdateInstaller.notifyCallbackOnSuccess();
                return;
            }
            this.mUpdateInstaller.notifyCallbackOnError(((Integer) AbUpdateInstaller.errorCodesMap.getOrDefault(Integer.valueOf(i), 1)).intValue(), (String) AbUpdateInstaller.errorStringsMap.getOrDefault(Integer.valueOf(i), AbUpdateInstaller.UNKNOWN_ERROR + i));
        }
    }
}
