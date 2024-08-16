package com.android.server.media;

import android.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRoutesInfo;
import android.media.IAudioRoutesObserver;
import android.media.IAudioService;
import android.media.MediaRoute2Info;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.media.DeviceRouteController;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class LegacyDeviceRouteController implements DeviceRouteController {
    private static final String DEVICE_ROUTE_ID = "DEVICE_ROUTE";
    private static final String TAG = "LDeviceRouteController";
    private final AudioManager mAudioManager;
    private final AudioRoutesObserver mAudioRoutesObserver;
    private final IAudioService mAudioService;
    private final Context mContext;
    private MediaRoute2Info mDeviceRoute;
    private int mDeviceVolume;
    private final DeviceRouteController.OnDeviceRouteChangedListener mOnDeviceRouteChangedListener;

    @Override // com.android.server.media.DeviceRouteController
    public boolean selectRoute(Integer num) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public LegacyDeviceRouteController(Context context, AudioManager audioManager, IAudioService iAudioService, DeviceRouteController.OnDeviceRouteChangedListener onDeviceRouteChangedListener) {
        AudioRoutesInfo audioRoutesInfo = null;
        AudioRoutesObserver audioRoutesObserver = new AudioRoutesObserver();
        this.mAudioRoutesObserver = audioRoutesObserver;
        Objects.requireNonNull(context);
        Objects.requireNonNull(audioManager);
        Objects.requireNonNull(iAudioService);
        Objects.requireNonNull(onDeviceRouteChangedListener);
        this.mContext = context;
        this.mOnDeviceRouteChangedListener = onDeviceRouteChangedListener;
        this.mAudioManager = audioManager;
        this.mAudioService = iAudioService;
        try {
            audioRoutesInfo = iAudioService.startWatchingRoutes(audioRoutesObserver);
        } catch (RemoteException e) {
            Slog.w(TAG, "Cannot connect to audio service to start listen to routes", e);
        }
        this.mDeviceRoute = createRouteFromAudioInfo(audioRoutesInfo);
    }

    @Override // com.android.server.media.DeviceRouteController
    public synchronized MediaRoute2Info getDeviceRoute() {
        return this.mDeviceRoute;
    }

    @Override // com.android.server.media.DeviceRouteController
    public synchronized boolean updateVolume(int i) {
        if (this.mDeviceVolume == i) {
            return false;
        }
        this.mDeviceVolume = i;
        this.mDeviceRoute = new MediaRoute2Info.Builder(this.mDeviceRoute).setVolume(i).build();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0038 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MediaRoute2Info createRouteFromAudioInfo(AudioRoutesInfo audioRoutesInfo) {
        int i;
        int i2;
        MediaRoute2Info build;
        if (audioRoutesInfo != null) {
            int i3 = audioRoutesInfo.mainType;
            int i4 = i3 & 2;
            i = R.string.face_acquired_too_right;
            if (i4 != 0) {
                i2 = 4;
            } else if ((i3 & 1) != 0) {
                i2 = 3;
            } else if ((i3 & 4) != 0) {
                i2 = 13;
                i = R.string.face_acquired_too_left;
            } else if ((i3 & 8) != 0) {
                i2 = 9;
                i = R.string.face_acquired_too_low;
            } else if ((i3 & 16) != 0) {
                i2 = 11;
                i = R.string.face_acquired_too_similar;
            }
            synchronized (this) {
                build = new MediaRoute2Info.Builder(DEVICE_ROUTE_ID, this.mContext.getResources().getText(i).toString()).setVolumeHandling(this.mAudioManager.isVolumeFixed() ? 0 : 1).setVolume(this.mDeviceVolume).setVolumeMax(this.mAudioManager.getStreamMaxVolume(3)).setType(i2).addFeature("android.media.route.feature.LIVE_AUDIO").addFeature("android.media.route.feature.LIVE_VIDEO").addFeature("android.media.route.feature.LOCAL_PLAYBACK").setConnectionState(2).build();
            }
            return build;
        }
        i = R.string.face_acquired_too_high;
        i2 = 2;
        synchronized (this) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceRouteUpdate(MediaRoute2Info mediaRoute2Info) {
        this.mOnDeviceRouteChangedListener.onDeviceRouteChanged(mediaRoute2Info);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class AudioRoutesObserver extends IAudioRoutesObserver.Stub {
        private AudioRoutesObserver() {
        }

        public void dispatchAudioRoutesChanged(AudioRoutesInfo audioRoutesInfo) {
            MediaRoute2Info createRouteFromAudioInfo = LegacyDeviceRouteController.this.createRouteFromAudioInfo(audioRoutesInfo);
            synchronized (LegacyDeviceRouteController.this) {
                LegacyDeviceRouteController.this.mDeviceRoute = createRouteFromAudioInfo;
            }
            LegacyDeviceRouteController.this.notifyDeviceRouteUpdate(createRouteFromAudioInfo);
        }
    }
}
