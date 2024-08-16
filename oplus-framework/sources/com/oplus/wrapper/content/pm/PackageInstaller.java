package com.oplus.wrapper.content.pm;

import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class PackageInstaller {
    private final android.content.pm.PackageInstaller mPackageInstaller;
    private static final String TAG = "PackageInstaller";
    private static final boolean LOGD = Log.isLoggable(TAG, 3);

    public PackageInstaller(android.content.pm.PackageInstaller packageInstaller) {
        this.mPackageInstaller = packageInstaller;
    }

    public int createSession(PackageInstaller.SessionParams params) throws IOException {
        return this.mPackageInstaller.createSession(params);
    }

    public PackageInstaller.Session openSession(int sessionId) throws IOException {
        return this.mPackageInstaller.openSession(sessionId);
    }

    public void abandonSession(int sessionId) {
        this.mPackageInstaller.abandonSession(sessionId);
    }

    /* loaded from: classes.dex */
    public static class Session {
        private final PackageInstaller.Session mSession;

        public Session(PackageInstaller.Session session) {
            this.mSession = session;
        }

        public void commit(IntentSender statusReceiver) {
            this.mSession.commit(statusReceiver);
        }

        public void write(String name, long offsetBytes, long lengthBytes, ParcelFileDescriptor fd) throws IOException {
            this.mSession.write(name, offsetBytes, lengthBytes, fd);
        }

        public OutputStream openWrite(String name, long offsetBytes, long lengthBytes) throws IOException {
            return this.mSession.openWrite(name, offsetBytes, lengthBytes);
        }

        public void fsync(OutputStream out) throws IOException {
            this.mSession.fsync(out);
        }

        public void close() {
            this.mSession.close();
        }
    }

    /* loaded from: classes.dex */
    public static class SessionParamsWrapper {
        public static final int COMPILE_MODE_EVERYTHING = 24576;
        public static final int COMPILE_MODE_EXTRACT = 4096;
        public static final int COMPILE_MODE_MASK = 61440;
        public static final int COMPILE_MODE_QIUCKEN = 12288;
        public static final int COMPILE_MODE_SPEED = 20480;
        public static final int COMPILE_MODE_SPEED_PROFILE = 16384;
        public static final int COMPILE_MODE_VERIFY = 8192;
        public static final int CPUS_FLAG_INSTALL = 255;
        public static final int CPUS_MASK = 255;
        public static final int DEXOPT_FLAG_INSTALL_DEFAULT = 17663;
        public static final int THREAD_NUM_1 = 256;
        public static final int THREAD_NUM_2 = 512;
        public static final int THREAD_NUM_4 = 1024;
        public static final int THREAD_NUM_8 = 2048;
        public static final int THREAD_NUM_MASK = 3840;
        private final PackageInstaller.SessionParams mSessionParams;

        public SessionParamsWrapper(PackageInstaller.SessionParams sessionParams) {
            this.mSessionParams = sessionParams;
        }

        public void setDexoptFlag(int flag) {
            if (this.mSessionParams.mSessionParamsExt == null) {
                Log.w(PackageInstaller.TAG, "SessionParams.mSessionParamsExt is null, do nothing and return");
            } else {
                this.mSessionParams.mSessionParamsExt.setDexoptFlag(flag);
            }
        }

        public int getDexoptFlag() {
            if (this.mSessionParams.mSessionParamsExt == null) {
                Log.w(PackageInstaller.TAG, "SessionParams.mSessionParamsExt is null, can't get extraDexoptFlags");
                return 0;
            }
            return this.mSessionParams.mSessionParamsExt.getDexoptFlag();
        }

        public PackageInstaller.SessionParams getSessionParams() {
            return this.mSessionParams;
        }
    }
}
