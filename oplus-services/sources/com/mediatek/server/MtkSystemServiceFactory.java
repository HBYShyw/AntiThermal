package com.mediatek.server;

import android.util.Slog;
import com.android.server.power.ShutdownThread;
import com.mediatek.server.am.AmsExt;
import com.mediatek.server.anr.AnrManager;
import com.mediatek.server.audio.AudioServiceExt;
import com.mediatek.server.pm.PmsExt;
import com.mediatek.server.powerhal.PowerHalManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class MtkSystemServiceFactory {
    private static Object lock = new Object();
    private static MtkSystemServiceFactory sInstance;

    public static MtkSystemServiceFactory getInstance() {
        if (sInstance == null) {
            try {
                sInstance = (MtkSystemServiceFactory) Class.forName("com.mediatek.server.MtkSystemServiceFactoryImpl", false, MtkSystemServer.sClassLoader).getConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (Exception e) {
                Slog.e("MtkSystemServiceFactory", "getInstance: " + e.toString());
                sInstance = new MtkSystemServiceFactory();
            }
        }
        return sInstance;
    }

    public AnrManager makeAnrManager() {
        return new AnrManager();
    }

    public PowerHalManager makePowerHalManager() {
        return new PowerHalManager();
    }

    public ShutdownThread makeMtkShutdownThread() {
        return new ShutdownThread();
    }

    public AmsExt makeAmsExt() {
        return new AmsExt();
    }

    public AudioServiceExt makeAudioServiceExt() {
        return new AudioServiceExt();
    }

    public PmsExt makePmsExt() {
        return new PmsExt();
    }
}
