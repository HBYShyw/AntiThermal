package com.android.server.audio;

import android.content.Context;
import android.media.AudioPlaybackConfiguration;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IPlaybackActivityMonitorExt {
    default boolean isInMusicVolumeMap(AudioPlaybackConfiguration audioPlaybackConfiguration, Context context) {
        return false;
    }

    default void setVolumeForUid(float f, int i, String str, boolean z) {
    }

    default void updatePlayerVolumeByApc(AudioPlaybackConfiguration audioPlaybackConfiguration, Context context) {
    }
}
