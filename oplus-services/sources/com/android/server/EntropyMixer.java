package com.android.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.AtomicFile;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class EntropyMixer extends Binder {

    @VisibleForTesting
    static final String DEVICE_SPECIFIC_INFO_HEADER = "Copyright (C) 2009 The Android Open Source Project\nAll Your Randomness Are Belong To Us\n";

    @VisibleForTesting
    static final int SEED_FILE_SIZE = 512;
    private static final int SEED_UPDATE_PERIOD = 10800000;
    private static final String TAG = "EntropyMixer";
    private static final int UPDATE_SEED_MSG = 1;
    private final BroadcastReceiver mBroadcastReceiver;
    private final Handler mHandler;
    private final File randomReadDevice;
    private final File randomWriteDevice;
    private final AtomicFile seedFile;
    private static final long START_TIME = System.currentTimeMillis();
    private static final long START_NANOTIME = System.nanoTime();

    public EntropyMixer(Context context) {
        this(context, new File(getSystemDir(), "entropy.dat"), new File("/dev/urandom"), new File("/dev/urandom"));
    }

    @VisibleForTesting
    EntropyMixer(Context context, File file, File file2, File file3) {
        Handler handler = new Handler(IoThread.getHandler().getLooper()) { // from class: com.android.server.EntropyMixer.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    Slog.e(EntropyMixer.TAG, "Will not process invalid message");
                } else {
                    EntropyMixer.this.updateSeedFile();
                    EntropyMixer.this.scheduleSeedUpdater();
                }
            }
        };
        this.mHandler = handler;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.EntropyMixer.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                EntropyMixer.this.updateSeedFile();
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.seedFile = new AtomicFile((File) Preconditions.checkNotNull(file));
        this.randomReadDevice = (File) Preconditions.checkNotNull(file2);
        this.randomWriteDevice = (File) Preconditions.checkNotNull(file3);
        loadInitialEntropy();
        updateSeedFile();
        scheduleSeedUpdater();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.ACTION_SHUTDOWN");
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.REBOOT");
        context.registerReceiver(broadcastReceiver, intentFilter, null, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleSeedUpdater() {
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, 10800000L);
    }

    private void loadInitialEntropy() {
        byte[] readSeedFile = readSeedFile();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.randomWriteDevice);
            try {
                if (readSeedFile.length != 0) {
                    fileOutputStream.write(readSeedFile);
                    Slog.i(TAG, "Loaded existing seed file");
                }
                fileOutputStream.write(getDeviceSpecificInformation());
                fileOutputStream.close();
            } finally {
            }
        } catch (IOException e) {
            Slog.e(TAG, "Error writing to " + this.randomWriteDevice, e);
        }
    }

    private byte[] readSeedFile() {
        try {
            return this.seedFile.readFully();
        } catch (FileNotFoundException unused) {
            return new byte[0];
        } catch (IOException e) {
            Slog.e(TAG, "Error reading " + this.seedFile.getBaseFile(), e);
            return new byte[0];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSeedFile() {
        FileInputStream fileInputStream;
        byte[] readSeedFile = readSeedFile();
        byte[] bArr = new byte[512];
        try {
            fileInputStream = new FileInputStream(this.randomReadDevice);
            try {
            } finally {
            }
        } catch (IOException e) {
            Slog.e(TAG, "Error reading " + this.randomReadDevice + "; seed file won't be properly updated", e);
        }
        if (fileInputStream.read(bArr) != 512) {
            throw new IOException("unexpected EOF");
        }
        fileInputStream.close();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update("Android EntropyMixer v1".getBytes());
            messageDigest.update(longToBytes(System.currentTimeMillis()));
            messageDigest.update(longToBytes(System.nanoTime()));
            messageDigest.update(longToBytes(readSeedFile.length));
            messageDigest.update(readSeedFile);
            messageDigest.update(longToBytes(512));
            messageDigest.update(bArr);
            byte[] digest = messageDigest.digest();
            System.arraycopy(digest, 0, bArr, 512 - digest.length, digest.length);
            writeNewSeed(bArr);
            if (readSeedFile.length == 0) {
                Slog.i(TAG, "Created seed file");
            } else {
                Slog.i(TAG, "Updated seed file");
            }
        } catch (NoSuchAlgorithmException e2) {
            Slog.wtf(TAG, "SHA-256 algorithm not found; seed file won't be updated", e2);
        }
    }

    private void writeNewSeed(byte[] bArr) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = this.seedFile.startWrite();
            fileOutputStream.write(bArr);
            this.seedFile.finishWrite(fileOutputStream);
        } catch (IOException e) {
            Slog.e(TAG, "Error writing " + this.seedFile.getBaseFile(), e);
            this.seedFile.failWrite(fileOutputStream);
        }
    }

    private static byte[] longToBytes(long j) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.putLong(j);
        return allocate.array();
    }

    private byte[] getDeviceSpecificInformation() {
        return (DEVICE_SPECIFIC_INFO_HEADER + START_TIME + '\n' + START_NANOTIME + '\n' + SystemProperties.get("ro.serialno") + '\n' + SystemProperties.get("ro.bootmode") + '\n' + SystemProperties.get("ro.baseband") + '\n' + SystemProperties.get("ro.carrier") + '\n' + SystemProperties.get("ro.bootloader") + '\n' + SystemProperties.get("ro.hardware") + '\n' + SystemProperties.get("ro.revision") + '\n' + SystemProperties.get("ro.build.fingerprint") + '\n' + new Object().hashCode() + '\n' + System.currentTimeMillis() + '\n' + System.nanoTime() + '\n').getBytes();
    }

    private static File getSystemDir() {
        File file = new File(Environment.getDataDirectory(), "system");
        file.mkdirs();
        return file;
    }
}
