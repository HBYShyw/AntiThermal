package com.android.server.graphics.fonts;

import android.R;
import android.annotation.EnforcePermission;
import android.annotation.RequiresPermission;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.fonts.FontUpdateRequest;
import android.graphics.fonts.SystemFonts;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SharedMemory;
import android.os.ShellCallback;
import android.system.ErrnoException;
import android.text.FontConfig;
import android.util.AndroidException;
import android.util.ArrayMap;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.graphics.fonts.IFontManager;
import com.android.internal.security.VerityUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.Preconditions;
import com.android.server.IoThread;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.graphics.fonts.FontManagerService;
import com.android.server.graphics.fonts.UpdatableFontDir;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.DirectByteBuffer;
import java.nio.NioUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class FontManagerService extends IFontManager.Stub {
    private static final String CONFIG_XML_FILE = "/data/fonts/config/config.xml";
    private static final String FONT_FILES_DIR = "/data/fonts/files";
    private static final String TAG = "FontManagerService";
    private final Context mContext;
    private String mDebugCertFilePath;
    private final boolean mIsSafeMode;

    @GuardedBy({"mSerializedFontMapLock"})
    private SharedMemory mSerializedFontMap;
    private final Object mSerializedFontMapLock;

    @GuardedBy({"mUpdatableFontDirLock"})
    private UpdatableFontDir mUpdatableFontDir;
    private final Object mUpdatableFontDirLock;

    @EnforcePermission("android.permission.UPDATE_FONTS")
    @RequiresPermission("android.permission.UPDATE_FONTS")
    public FontConfig getFontConfig() {
        super.getFontConfig_enforcePermission();
        return getSystemFontConfig();
    }

    @RequiresPermission("android.permission.UPDATE_FONTS")
    public int updateFontFamily(List<FontUpdateRequest> list, int i) {
        try {
            Preconditions.checkArgumentNonnegative(i);
            Objects.requireNonNull(list);
            getContext().enforceCallingPermission("android.permission.UPDATE_FONTS", "UPDATE_FONTS permission required.");
            try {
                update(i, list);
                closeFileDescriptors(list);
                return 0;
            } catch (SystemFontException e) {
                Slog.e(TAG, "Failed to update font family", e);
                int errorCode = e.getErrorCode();
                closeFileDescriptors(list);
                return errorCode;
            }
        } catch (Throwable th) {
            closeFileDescriptors(list);
            throw th;
        }
    }

    private static void closeFileDescriptors(List<FontUpdateRequest> list) {
        ParcelFileDescriptor fd;
        if (list == null) {
            return;
        }
        for (FontUpdateRequest fontUpdateRequest : list) {
            if (fontUpdateRequest != null && (fd = fontUpdateRequest.getFd()) != null) {
                try {
                    fd.close();
                } catch (IOException e) {
                    Slog.w(TAG, "Failed to close fd", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SystemFontException extends AndroidException {
        private final int mErrorCode;

        /* JADX INFO: Access modifiers changed from: package-private */
        public SystemFontException(int i, String str, Throwable th) {
            super(str, th);
            this.mErrorCode = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public SystemFontException(int i, String str) {
            super(str);
            this.mErrorCode = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getErrorCode() {
            return this.mErrorCode;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Lifecycle extends SystemService {
        private final FontManagerService mService;

        public Lifecycle(Context context, boolean z) {
            super(context);
            this.mService = new FontManagerService(context, z);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            LocalServices.addService(FontManagerInternal.class, new FontManagerInternal() { // from class: com.android.server.graphics.fonts.FontManagerService.Lifecycle.1
                @Override // com.android.server.graphics.fonts.FontManagerInternal
                public SharedMemory getSerializedSystemFontMap() {
                    return Lifecycle.this.mService.getCurrentFontMap();
                }
            });
            publishBinderService("font", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
            super.onUserSwitching(targetUser, targetUser2);
            Slog.d(FontManagerService.TAG, "onUserSwitching " + targetUser + " to " + targetUser2);
            IoThread.getHandler().post(new Runnable() { // from class: com.android.server.graphics.fonts.FontManagerService$Lifecycle$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FontManagerService.Lifecycle.this.lambda$onUserSwitching$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserSwitching$0() {
            this.mService.updateSerializedFontMap();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class FsverityUtilImpl implements UpdatableFontDir.FsverityUtil {
        private final String[] mDerCertPaths;

        FsverityUtilImpl(String[] strArr) {
            this.mDerCertPaths = strArr;
        }

        @Override // com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil
        public boolean isFromTrustedProvider(String str, byte[] bArr) {
            FileInputStream fileInputStream;
            byte[] fsverityDigest = VerityUtils.getFsverityDigest(str);
            if (fsverityDigest == null) {
                Log.w(FontManagerService.TAG, "Failed to get fs-verity digest for " + str);
                return false;
            }
            for (String str2 : this.mDerCertPaths) {
                try {
                    fileInputStream = new FileInputStream(str2);
                    try {
                    } finally {
                    }
                } catch (IOException unused) {
                    Log.w(FontManagerService.TAG, "Failed to read certificate file: " + str2);
                }
                if (VerityUtils.verifyPkcs7DetachedSignature(bArr, fsverityDigest, fileInputStream)) {
                    fileInputStream.close();
                    return true;
                }
                fileInputStream.close();
            }
            return false;
        }

        @Override // com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil
        public void setUpFsverity(String str) throws IOException {
            VerityUtils.setUpFsverity(str);
        }

        @Override // com.android.server.graphics.fonts.UpdatableFontDir.FsverityUtil
        public boolean rename(File file, File file2) {
            return file.renameTo(file2);
        }
    }

    private FontManagerService(Context context, boolean z) {
        this.mUpdatableFontDirLock = new Object();
        this.mDebugCertFilePath = null;
        this.mSerializedFontMapLock = new Object();
        this.mSerializedFontMap = null;
        if (z) {
            Slog.i(TAG, "Entering safe mode. Deleting all font updates.");
            UpdatableFontDir.deleteAllFiles(new File(FONT_FILES_DIR), new File(CONFIG_XML_FILE));
        }
        this.mContext = context;
        this.mIsSafeMode = z;
        initialize();
    }

    private UpdatableFontDir createUpdatableFontDir() {
        if (this.mIsSafeMode || !VerityUtils.isFsVeritySupported()) {
            return null;
        }
        String[] stringArray = this.mContext.getResources().getStringArray(R.array.config_scrollBarrierVibePattern);
        if (this.mDebugCertFilePath != null && Build.IS_DEBUGGABLE) {
            String[] strArr = new String[stringArray.length + 1];
            System.arraycopy(stringArray, 0, strArr, 0, stringArray.length);
            strArr[stringArray.length] = this.mDebugCertFilePath;
            stringArray = strArr;
        }
        return new UpdatableFontDir(new File(FONT_FILES_DIR), new OtfFontFileParser(), new FsverityUtilImpl(stringArray), new File(CONFIG_XML_FILE));
    }

    public void addDebugCertificate(String str) {
        this.mDebugCertFilePath = str;
    }

    private void initialize() {
        synchronized (this.mUpdatableFontDirLock) {
            UpdatableFontDir createUpdatableFontDir = createUpdatableFontDir();
            this.mUpdatableFontDir = createUpdatableFontDir;
            if (createUpdatableFontDir == null) {
                setSerializedFontMap(serializeSystemServerFontMap());
            } else {
                createUpdatableFontDir.loadFontFileMap();
                updateSerializedFontMap();
            }
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    SharedMemory getCurrentFontMap() {
        SharedMemory sharedMemory;
        synchronized (this.mSerializedFontMapLock) {
            sharedMemory = this.mSerializedFontMap;
        }
        return sharedMemory;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void update(int i, List<FontUpdateRequest> list) throws SystemFontException {
        synchronized (this.mUpdatableFontDirLock) {
            UpdatableFontDir updatableFontDir = this.mUpdatableFontDir;
            if (updatableFontDir == null) {
                throw new SystemFontException(-7, "The font updater is disabled.");
            }
            if (i != -1 && updatableFontDir.getConfigVersion() != i) {
                throw new SystemFontException(-8, "The base config version is older than current.");
            }
            this.mUpdatableFontDir.update(list);
            updateSerializedFontMap();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearUpdates() {
        UpdatableFontDir.deleteAllFiles(new File(FONT_FILES_DIR), new File(CONFIG_XML_FILE));
        initialize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restart() {
        initialize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, File> getFontFileMap() {
        synchronized (this.mUpdatableFontDirLock) {
            UpdatableFontDir updatableFontDir = this.mUpdatableFontDir;
            if (updatableFontDir == null) {
                return Collections.emptyMap();
            }
            return updatableFontDir.getPostScriptMap();
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            new FontManagerShellCommand(this).dumpAll(new IndentingPrintWriter(printWriter, "  "));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new FontManagerShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    public FontConfig getSystemFontConfig() {
        synchronized (this.mUpdatableFontDirLock) {
            UpdatableFontDir updatableFontDir = this.mUpdatableFontDir;
            if (updatableFontDir == null) {
                return SystemFonts.getSystemPreinstalledFontConfig();
            }
            return updatableFontDir.getSystemFontConfig();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSerializedFontMap() {
        SharedMemory serializeFontMap = serializeFontMap(getSystemFontConfig());
        if (serializeFontMap == null) {
            serializeFontMap = serializeSystemServerFontMap();
        }
        setSerializedFontMap(serializeFontMap);
    }

    private static SharedMemory serializeFontMap(FontConfig fontConfig) {
        ArrayMap arrayMap = new ArrayMap();
        try {
            try {
                SharedMemory serializeFontMap = Typeface.serializeFontMap(SystemFonts.buildSystemTypefaces(fontConfig, SystemFonts.buildSystemFallback(fontConfig, arrayMap)));
                for (ByteBuffer byteBuffer : arrayMap.values()) {
                    if (byteBuffer instanceof DirectByteBuffer) {
                        NioUtils.freeDirectBuffer(byteBuffer);
                    }
                }
                return serializeFontMap;
            } catch (ErrnoException | IOException e) {
                Slog.w(TAG, "Failed to serialize updatable font map. Retrying with system image fonts.", e);
                for (ByteBuffer byteBuffer2 : arrayMap.values()) {
                    if (byteBuffer2 instanceof DirectByteBuffer) {
                        NioUtils.freeDirectBuffer(byteBuffer2);
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            for (ByteBuffer byteBuffer3 : arrayMap.values()) {
                if (byteBuffer3 instanceof DirectByteBuffer) {
                    NioUtils.freeDirectBuffer(byteBuffer3);
                }
            }
            throw th;
        }
    }

    private static SharedMemory serializeSystemServerFontMap() {
        try {
            return Typeface.serializeFontMap(Typeface.getSystemFontMap());
        } catch (ErrnoException | IOException e) {
            Slog.e(TAG, "Failed to serialize SystemServer system font map", e);
            return null;
        }
    }

    private void setSerializedFontMap(SharedMemory sharedMemory) {
        SharedMemory sharedMemory2;
        synchronized (this.mSerializedFontMapLock) {
            sharedMemory2 = this.mSerializedFontMap;
            this.mSerializedFontMap = sharedMemory;
        }
        if (sharedMemory2 != null) {
            sharedMemory2.close();
        }
    }
}
