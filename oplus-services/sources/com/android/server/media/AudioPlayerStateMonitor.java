package com.android.server.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioPlaybackConfiguration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AudioPlayerStateMonitor {
    private static final boolean DEBUG = MediaSessionService.DEBUG;
    private static String TAG = "AudioPlayerStateMonitor";
    private static AudioPlayerStateMonitor sInstance;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final Map<OnAudioPlayerActiveStateChangedListener, MessageHandler> mListenerMap = new ArrayMap();

    @GuardedBy({"mLock"})
    final Set<Integer> mActiveAudioUids = new ArraySet();

    @GuardedBy({"mLock"})
    ArrayMap<Integer, AudioPlaybackConfiguration> mPrevActiveAudioPlaybackConfigs = new ArrayMap<>();

    @GuardedBy({"mLock"})
    final List<Integer> mSortedAudioPlaybackClientUids = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface OnAudioPlayerActiveStateChangedListener {
        void onAudioPlayerActiveStateChanged(AudioPlaybackConfiguration audioPlaybackConfiguration, boolean z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class MessageHandler extends Handler {
        private static final int MSG_AUDIO_PLAYER_ACTIVE_STATE_CHANGED = 1;
        private final OnAudioPlayerActiveStateChangedListener mListener;

        MessageHandler(Looper looper, OnAudioPlayerActiveStateChangedListener onAudioPlayerActiveStateChangedListener) {
            super(looper);
            this.mListener = onAudioPlayerActiveStateChangedListener;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            this.mListener.onAudioPlayerActiveStateChanged((AudioPlaybackConfiguration) message.obj, message.arg1 != 0);
        }

        void sendAudioPlayerActiveStateChangedMessage(AudioPlaybackConfiguration audioPlaybackConfiguration, boolean z) {
            obtainMessage(1, z ? 1 : 0, 0, audioPlaybackConfiguration).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AudioPlayerStateMonitor getInstance(Context context) {
        AudioPlayerStateMonitor audioPlayerStateMonitor;
        synchronized (AudioPlayerStateMonitor.class) {
            if (sInstance == null) {
                sInstance = new AudioPlayerStateMonitor(context);
            }
            audioPlayerStateMonitor = sInstance;
        }
        return audioPlayerStateMonitor;
    }

    private AudioPlayerStateMonitor(Context context) {
        ((AudioManager) context.getSystemService("audio")).registerAudioPlaybackCallback(new AudioManagerPlaybackListener(), null);
    }

    public void registerListener(OnAudioPlayerActiveStateChangedListener onAudioPlayerActiveStateChangedListener, Handler handler) {
        synchronized (this.mLock) {
            this.mListenerMap.put(onAudioPlayerActiveStateChangedListener, new MessageHandler(handler == null ? Looper.myLooper() : handler.getLooper(), onAudioPlayerActiveStateChangedListener));
        }
    }

    public void unregisterListener(OnAudioPlayerActiveStateChangedListener onAudioPlayerActiveStateChangedListener) {
        synchronized (this.mLock) {
            this.mListenerMap.remove(onAudioPlayerActiveStateChangedListener);
        }
    }

    public List<Integer> getSortedAudioPlaybackClientUids() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            arrayList.addAll(this.mSortedAudioPlaybackClientUids);
        }
        return arrayList;
    }

    public boolean isPlaybackActive(int i) {
        boolean contains;
        synchronized (this.mLock) {
            contains = this.mActiveAudioUids.contains(Integer.valueOf(i));
        }
        return contains;
    }

    public void cleanUpAudioPlaybackUids(int i) {
        synchronized (this.mLock) {
            int identifier = UserHandle.getUserHandleForUid(i).getIdentifier();
            for (int size = this.mSortedAudioPlaybackClientUids.size() - 1; size >= 0 && this.mSortedAudioPlaybackClientUids.get(size).intValue() != i; size--) {
                int intValue = this.mSortedAudioPlaybackClientUids.get(size).intValue();
                if (identifier == UserHandle.getUserHandleForUid(intValue).getIdentifier() && !isPlaybackActive(intValue)) {
                    this.mSortedAudioPlaybackClientUids.remove(size);
                }
            }
        }
    }

    public void dump(Context context, PrintWriter printWriter, String str) {
        synchronized (this.mLock) {
            printWriter.println(str + "Audio playback (lastly played comes first)");
            String str2 = str + "  ";
            for (int i = 0; i < this.mSortedAudioPlaybackClientUids.size(); i++) {
                int intValue = this.mSortedAudioPlaybackClientUids.get(i).intValue();
                printWriter.print(str2 + "uid=" + intValue + " packages=");
                String[] packagesForUid = context.getPackageManager().getPackagesForUid(intValue);
                if (packagesForUid != null && packagesForUid.length > 0) {
                    for (String str3 : packagesForUid) {
                        printWriter.print(str3 + " ");
                    }
                }
                printWriter.println();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void sendAudioPlayerActiveStateChangedMessageLocked(AudioPlaybackConfiguration audioPlaybackConfiguration, boolean z) {
        Iterator<MessageHandler> it = this.mListenerMap.values().iterator();
        while (it.hasNext()) {
            it.next().sendAudioPlayerActiveStateChangedMessage(audioPlaybackConfiguration, z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class AudioManagerPlaybackListener extends AudioManager.AudioPlaybackCallback {
        private AudioManagerPlaybackListener() {
        }

        @Override // android.media.AudioManager.AudioPlaybackCallback
        public void onPlaybackConfigChanged(List<AudioPlaybackConfiguration> list) {
            int i;
            synchronized (AudioPlayerStateMonitor.this.mLock) {
                AudioPlayerStateMonitor.this.mActiveAudioUids.clear();
                ArrayMap<Integer, AudioPlaybackConfiguration> arrayMap = new ArrayMap<>();
                for (AudioPlaybackConfiguration audioPlaybackConfiguration : list) {
                    if (audioPlaybackConfiguration.isActive()) {
                        AudioPlayerStateMonitor.this.mActiveAudioUids.add(Integer.valueOf(audioPlaybackConfiguration.getClientUid()));
                        arrayMap.put(Integer.valueOf(audioPlaybackConfiguration.getPlayerInterfaceId()), audioPlaybackConfiguration);
                    }
                }
                for (int i2 = 0; i2 < arrayMap.size(); i2++) {
                    AudioPlaybackConfiguration valueAt = arrayMap.valueAt(i2);
                    int clientUid = valueAt.getClientUid();
                    if (!AudioPlayerStateMonitor.this.mPrevActiveAudioPlaybackConfigs.containsKey(Integer.valueOf(valueAt.getPlayerInterfaceId()))) {
                        if (AudioPlayerStateMonitor.DEBUG) {
                            Log.d(AudioPlayerStateMonitor.TAG, "Found a new active media playback. " + valueAt);
                        }
                        int indexOf = AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.indexOf(Integer.valueOf(clientUid));
                        if (indexOf != 0) {
                            if (indexOf > 0) {
                                AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.remove(indexOf);
                            }
                            AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.add(0, Integer.valueOf(clientUid));
                        }
                    }
                }
                if (AudioPlayerStateMonitor.this.mActiveAudioUids.size() > 0) {
                    AudioPlayerStateMonitor audioPlayerStateMonitor = AudioPlayerStateMonitor.this;
                    if (!audioPlayerStateMonitor.mActiveAudioUids.contains(audioPlayerStateMonitor.mSortedAudioPlaybackClientUids.get(0))) {
                        int i3 = 1;
                        while (true) {
                            if (i3 >= AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.size()) {
                                i3 = -1;
                                i = -1;
                                break;
                            } else {
                                i = AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.get(i3).intValue();
                                if (AudioPlayerStateMonitor.this.mActiveAudioUids.contains(Integer.valueOf(i))) {
                                    break;
                                } else {
                                    i3++;
                                }
                            }
                        }
                        while (i3 > 0) {
                            List<Integer> list2 = AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids;
                            list2.set(i3, list2.get(i3 - 1));
                            i3--;
                        }
                        AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.set(0, Integer.valueOf(i));
                    }
                }
                for (AudioPlaybackConfiguration audioPlaybackConfiguration2 : list) {
                    if ((AudioPlayerStateMonitor.this.mPrevActiveAudioPlaybackConfigs.remove(Integer.valueOf(audioPlaybackConfiguration2.getPlayerInterfaceId())) != null) != audioPlaybackConfiguration2.isActive()) {
                        AudioPlayerStateMonitor.this.sendAudioPlayerActiveStateChangedMessageLocked(audioPlaybackConfiguration2, false);
                    }
                }
                Iterator<AudioPlaybackConfiguration> it = AudioPlayerStateMonitor.this.mPrevActiveAudioPlaybackConfigs.values().iterator();
                while (it.hasNext()) {
                    AudioPlayerStateMonitor.this.sendAudioPlayerActiveStateChangedMessageLocked(it.next(), true);
                }
                AudioPlayerStateMonitor.this.mPrevActiveAudioPlaybackConfigs = arrayMap;
            }
        }
    }
}
