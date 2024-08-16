package com.android.server.audio;

import android.media.AudioPlaybackConfiguration;
import java.util.HashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IPlaybackActivityMonitorWrapper {
    default Object getPlayerLock() {
        return null;
    }

    default HashMap<Integer, AudioPlaybackConfiguration> getPlayers() {
        return null;
    }

    default IPlaybackActivityMonitorExt getExtImpl() {
        return new IPlaybackActivityMonitorExt() { // from class: com.android.server.audio.IPlaybackActivityMonitorWrapper.1
        };
    }
}
