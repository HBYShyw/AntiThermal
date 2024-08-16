package com.android.server.audio;

import android.media.AudioAttributes;
import android.media.AudioPlaybackConfiguration;
import android.media.VolumeShaper;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import com.android.server.audio.PlaybackActivityMonitor;
import com.android.server.utils.EventLogger;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class FadeOutManager {
    private static final boolean DEBUG = false;
    static final long DELAY_FADE_IN_OFFENDERS_MS = 2000;
    private static final int[] FADEABLE_USAGES;
    private static final VolumeShaper.Operation PLAY_CREATE_IF_NEEDED;
    private static final VolumeShaper.Operation PLAY_SKIP_RAMP;
    public static final String TAG = "AudioService.FadeOutManager";
    private static final int[] UNFADEABLE_CONTENT_TYPES;
    private static final int[] UNFADEABLE_PLAYER_TYPES;
    private final HashMap<Integer, FadedOutApp> mFadedApps = new HashMap<>();
    static final long FADE_OUT_DURATION_MS = 1000;
    private static final VolumeShaper.Configuration FADEOUT_VSHAPE = new VolumeShaper.Configuration.Builder().setId(2).setCurve(new float[]{0.0f, 0.25f, 1.0f}, new float[]{1.0f, 0.65f, 0.0f}).setOptionFlags(2).setDuration(FADE_OUT_DURATION_MS).build();

    static {
        VolumeShaper.Operation build = new VolumeShaper.Operation.Builder(VolumeShaper.Operation.PLAY).createIfNeeded().build();
        PLAY_CREATE_IF_NEEDED = build;
        UNFADEABLE_PLAYER_TYPES = new int[]{13, 3};
        UNFADEABLE_CONTENT_TYPES = new int[]{1};
        FADEABLE_USAGES = new int[]{14, 1};
        PLAY_SKIP_RAMP = new VolumeShaper.Operation.Builder(build).setXOffset(1.0f).build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean canCauseFadeOut(FocusRequester focusRequester, FocusRequester focusRequester2) {
        return focusRequester.getAudioAttributes().getContentType() != 1 && (focusRequester2.getGrantFlags() & 2) == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean canBeFadedOut(AudioPlaybackConfiguration audioPlaybackConfiguration) {
        return (ArrayUtils.contains(UNFADEABLE_PLAYER_TYPES, audioPlaybackConfiguration.getPlayerType()) || ArrayUtils.contains(UNFADEABLE_CONTENT_TYPES, audioPlaybackConfiguration.getAudioAttributes().getContentType()) || !ArrayUtils.contains(FADEABLE_USAGES, audioPlaybackConfiguration.getAudioAttributes().getUsage())) ? false : true;
    }

    static long getFadeOutDurationOnFocusLossMillis(AudioAttributes audioAttributes) {
        if (!ArrayUtils.contains(UNFADEABLE_CONTENT_TYPES, audioAttributes.getContentType()) && ArrayUtils.contains(FADEABLE_USAGES, audioAttributes.getUsage())) {
            return FADE_OUT_DURATION_MS;
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void fadeOutUid(int i, ArrayList<AudioPlaybackConfiguration> arrayList) {
        Log.i(TAG, "fadeOutUid() uid:" + i);
        if (!this.mFadedApps.containsKey(Integer.valueOf(i))) {
            this.mFadedApps.put(Integer.valueOf(i), new FadedOutApp(i));
        }
        FadedOutApp fadedOutApp = this.mFadedApps.get(Integer.valueOf(i));
        Iterator<AudioPlaybackConfiguration> it = arrayList.iterator();
        while (it.hasNext()) {
            fadedOutApp.addFade(it.next(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void unfadeOutUid(int i, HashMap<Integer, AudioPlaybackConfiguration> hashMap) {
        Log.i(TAG, "unfadeOutUid() uid:" + i);
        FadedOutApp remove = this.mFadedApps.remove(Integer.valueOf(i));
        if (remove == null) {
            return;
        }
        remove.removeUnfadeAll(hashMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void checkFade(AudioPlaybackConfiguration audioPlaybackConfiguration) {
        FadedOutApp fadedOutApp = this.mFadedApps.get(Integer.valueOf(audioPlaybackConfiguration.getClientUid()));
        if (fadedOutApp == null) {
            return;
        }
        fadedOutApp.addFade(audioPlaybackConfiguration, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeReleased(AudioPlaybackConfiguration audioPlaybackConfiguration) {
        FadedOutApp fadedOutApp = this.mFadedApps.get(Integer.valueOf(audioPlaybackConfiguration.getClientUid()));
        if (fadedOutApp == null) {
            return;
        }
        fadedOutApp.removeReleased(audioPlaybackConfiguration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dump(PrintWriter printWriter) {
        Iterator<FadedOutApp> it = this.mFadedApps.values().iterator();
        while (it.hasNext()) {
            it.next().dump(printWriter);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static final class FadedOutApp {
        private final ArrayList<Integer> mFadedPlayers = new ArrayList<>();
        private final int mUid;

        FadedOutApp(int i) {
            this.mUid = i;
        }

        void dump(PrintWriter printWriter) {
            printWriter.print("\t uid:" + this.mUid + " piids:");
            Iterator<Integer> it = this.mFadedPlayers.iterator();
            while (it.hasNext()) {
                printWriter.print(" " + it.next().intValue());
            }
            printWriter.println("");
        }

        void addFade(AudioPlaybackConfiguration audioPlaybackConfiguration, boolean z) {
            int intValue = new Integer(audioPlaybackConfiguration.getPlayerInterfaceId()).intValue();
            if (this.mFadedPlayers.contains(Integer.valueOf(intValue))) {
                return;
            }
            try {
                PlaybackActivityMonitor.sEventLogger.enqueue(new PlaybackActivityMonitor.FadeOutEvent(audioPlaybackConfiguration, z).printLog(FadeOutManager.TAG));
                audioPlaybackConfiguration.getPlayerProxy().applyVolumeShaper(FadeOutManager.FADEOUT_VSHAPE, z ? FadeOutManager.PLAY_SKIP_RAMP : FadeOutManager.PLAY_CREATE_IF_NEEDED);
                this.mFadedPlayers.add(Integer.valueOf(intValue));
            } catch (Exception e) {
                Log.e(FadeOutManager.TAG, "Error fading out player piid:" + intValue + " uid:" + audioPlaybackConfiguration.getClientUid(), e);
            }
        }

        void removeUnfadeAll(HashMap<Integer, AudioPlaybackConfiguration> hashMap) {
            Iterator<Integer> it = this.mFadedPlayers.iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                AudioPlaybackConfiguration audioPlaybackConfiguration = hashMap.get(Integer.valueOf(intValue));
                if (audioPlaybackConfiguration != null) {
                    try {
                        PlaybackActivityMonitor.sEventLogger.enqueue(new EventLogger.StringEvent("unfading out piid:" + intValue).printLog(FadeOutManager.TAG));
                        audioPlaybackConfiguration.getPlayerProxy().applyVolumeShaper(FadeOutManager.FADEOUT_VSHAPE, VolumeShaper.Operation.REVERSE);
                    } catch (Exception e) {
                        Log.e(FadeOutManager.TAG, "Error unfading out player piid:" + intValue + " uid:" + this.mUid, e);
                    }
                }
            }
            this.mFadedPlayers.clear();
        }

        void removeReleased(AudioPlaybackConfiguration audioPlaybackConfiguration) {
            this.mFadedPlayers.remove(new Integer(audioPlaybackConfiguration.getPlayerInterfaceId()));
        }
    }
}
