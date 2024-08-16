package com.android.server;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Binder;
import android.os.FileUtils;
import android.provider.Settings;
import android.util.Slog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CertBlacklister extends Binder {
    private static final String BLACKLIST_ROOT;
    public static final String PUBKEY_BLACKLIST_KEY = "pubkey_blacklist";
    public static final String PUBKEY_PATH;
    public static final String SERIAL_BLACKLIST_KEY = "serial_blacklist";
    public static final String SERIAL_PATH;
    private static final String TAG = "CertBlacklister";

    static {
        String str = System.getenv("ANDROID_DATA") + "/misc/keychain/";
        BLACKLIST_ROOT = str;
        PUBKEY_PATH = str + "pubkey_blacklist.txt";
        SERIAL_PATH = str + "serial_blacklist.txt";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class BlacklistObserver extends ContentObserver {
        private final ContentResolver mContentResolver;
        private final String mKey;
        private final String mName;
        private final String mPath;
        private final File mTmpDir;

        public BlacklistObserver(String str, String str2, String str3, ContentResolver contentResolver) {
            super(null);
            this.mKey = str;
            this.mName = str2;
            this.mPath = str3;
            this.mTmpDir = new File(str3).getParentFile();
            this.mContentResolver = contentResolver;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            writeBlacklist();
        }

        public String getValue() {
            return Settings.Secure.getString(this.mContentResolver, this.mKey);
        }

        private void writeBlacklist() {
            new Thread("BlacklistUpdater") { // from class: com.android.server.CertBlacklister.BlacklistObserver.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    synchronized (BlacklistObserver.this.mTmpDir) {
                        String value = BlacklistObserver.this.getValue();
                        if (value != null) {
                            Slog.i(CertBlacklister.TAG, "Certificate blacklist changed, updating...");
                            FileOutputStream fileOutputStream = null;
                            try {
                                try {
                                    File createTempFile = File.createTempFile("journal", "", BlacklistObserver.this.mTmpDir);
                                    createTempFile.setReadable(true, false);
                                    FileOutputStream fileOutputStream2 = new FileOutputStream(createTempFile);
                                    try {
                                        fileOutputStream2.write(value.getBytes());
                                        FileUtils.sync(fileOutputStream2);
                                        createTempFile.renameTo(new File(BlacklistObserver.this.mPath));
                                        Slog.i(CertBlacklister.TAG, "Certificate blacklist updated");
                                        IoUtils.closeQuietly(fileOutputStream2);
                                    } catch (IOException e) {
                                        e = e;
                                        fileOutputStream = fileOutputStream2;
                                        Slog.e(CertBlacklister.TAG, "Failed to write blacklist", e);
                                        IoUtils.closeQuietly(fileOutputStream);
                                    } catch (Throwable th) {
                                        th = th;
                                        fileOutputStream = fileOutputStream2;
                                        IoUtils.closeQuietly(fileOutputStream);
                                        throw th;
                                    }
                                } catch (IOException e2) {
                                    e = e2;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        }
                    }
                }
            }.start();
        }
    }

    public CertBlacklister(Context context) {
        registerObservers(context.getContentResolver());
    }

    private BlacklistObserver buildPubkeyObserver(ContentResolver contentResolver) {
        return new BlacklistObserver(PUBKEY_BLACKLIST_KEY, "pubkey", PUBKEY_PATH, contentResolver);
    }

    private BlacklistObserver buildSerialObserver(ContentResolver contentResolver) {
        return new BlacklistObserver(SERIAL_BLACKLIST_KEY, "serial", SERIAL_PATH, contentResolver);
    }

    private void registerObservers(ContentResolver contentResolver) {
        contentResolver.registerContentObserver(Settings.Secure.getUriFor(PUBKEY_BLACKLIST_KEY), true, buildPubkeyObserver(contentResolver));
        contentResolver.registerContentObserver(Settings.Secure.getUriFor(SERIAL_BLACKLIST_KEY), true, buildSerialObserver(contentResolver));
    }
}
