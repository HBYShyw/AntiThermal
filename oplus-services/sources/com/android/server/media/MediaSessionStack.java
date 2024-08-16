package com.android.server.media;

import android.media.Session2Token;
import android.media.session.MediaSession;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class MediaSessionStack {
    private static final boolean DEBUG = MediaSessionService.DEBUG;
    private static final int DUMP_EVENTS_MAX_COUNT = 70;
    private static final String TAG = "MediaSessionStack";
    private final AudioPlayerStateMonitor mAudioPlayerStateMonitor;
    private MediaSessionRecordImpl mMediaButtonSession;
    private final OnMediaButtonSessionChangedListener mOnMediaButtonSessionChangedListener;
    private final List<MediaSessionRecordImpl> mSessions = new ArrayList();
    private final SparseArray<List<MediaSessionRecord>> mCachedActiveLists = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface OnMediaButtonSessionChangedListener {
        void onMediaButtonSessionChanged(MediaSessionRecordImpl mediaSessionRecordImpl, MediaSessionRecordImpl mediaSessionRecordImpl2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaSessionStack(AudioPlayerStateMonitor audioPlayerStateMonitor, OnMediaButtonSessionChangedListener onMediaButtonSessionChangedListener) {
        this.mAudioPlayerStateMonitor = audioPlayerStateMonitor;
        this.mOnMediaButtonSessionChangedListener = onMediaButtonSessionChangedListener;
    }

    public void addSession(MediaSessionRecordImpl mediaSessionRecordImpl) {
        Slog.i(TAG, TextUtils.formatSimple("addSession to bottom of stack | record: %s", new Object[]{mediaSessionRecordImpl}));
        this.mSessions.add(mediaSessionRecordImpl);
        clearCache(mediaSessionRecordImpl.getUserId());
        updateMediaButtonSessionIfNeeded();
    }

    public void removeSession(MediaSessionRecordImpl mediaSessionRecordImpl) {
        Slog.i(TAG, TextUtils.formatSimple("removeSession | record: %s", new Object[]{mediaSessionRecordImpl}));
        this.mSessions.remove(mediaSessionRecordImpl);
        if (this.mMediaButtonSession == mediaSessionRecordImpl) {
            updateMediaButtonSession(null);
        }
        clearCache(mediaSessionRecordImpl.getUserId());
    }

    public boolean contains(MediaSessionRecordImpl mediaSessionRecordImpl) {
        return this.mSessions.contains(mediaSessionRecordImpl);
    }

    public MediaSessionRecord getMediaSessionRecord(MediaSession.Token token) {
        for (MediaSessionRecordImpl mediaSessionRecordImpl : this.mSessions) {
            if (mediaSessionRecordImpl instanceof MediaSessionRecord) {
                MediaSessionRecord mediaSessionRecord = (MediaSessionRecord) mediaSessionRecordImpl;
                if (Objects.equals(mediaSessionRecord.getSessionToken(), token)) {
                    return mediaSessionRecord;
                }
            }
        }
        return null;
    }

    public void onPlaybackStateChanged(MediaSessionRecordImpl mediaSessionRecordImpl, boolean z) {
        MediaSessionRecordImpl findMediaButtonSession;
        if (z) {
            Slog.i(TAG, TextUtils.formatSimple("onPlaybackStateChanged - Pushing session to top | record: %s", new Object[]{mediaSessionRecordImpl}));
            this.mSessions.remove(mediaSessionRecordImpl);
            this.mSessions.add(0, mediaSessionRecordImpl);
            clearCache(mediaSessionRecordImpl.getUserId());
        }
        MediaSessionRecordImpl mediaSessionRecordImpl2 = this.mMediaButtonSession;
        if (mediaSessionRecordImpl2 == null || mediaSessionRecordImpl2.getUid() != mediaSessionRecordImpl.getUid() || (findMediaButtonSession = findMediaButtonSession(this.mMediaButtonSession.getUid())) == this.mMediaButtonSession || (findMediaButtonSession.getSessionPolicies() & 2) != 0) {
            return;
        }
        updateMediaButtonSession(findMediaButtonSession);
    }

    public void onSessionActiveStateChanged(MediaSessionRecordImpl mediaSessionRecordImpl) {
        clearCache(mediaSessionRecordImpl.getUserId());
    }

    public void updateMediaButtonSessionIfNeeded() {
        if (DEBUG) {
            Log.d(TAG, "updateMediaButtonSessionIfNeeded, callers=" + getCallers(2));
        }
        List<Integer> sortedAudioPlaybackClientUids = this.mAudioPlayerStateMonitor.getSortedAudioPlaybackClientUids();
        for (int i = 0; i < sortedAudioPlaybackClientUids.size(); i++) {
            int intValue = sortedAudioPlaybackClientUids.get(i).intValue();
            MediaSessionRecordImpl findMediaButtonSession = findMediaButtonSession(intValue);
            if (findMediaButtonSession == null) {
                if (DEBUG) {
                    Log.d(TAG, "updateMediaButtonSessionIfNeeded, skipping uid=" + intValue);
                }
            } else {
                boolean z = (findMediaButtonSession.getSessionPolicies() & 2) != 0;
                if (DEBUG) {
                    Log.d(TAG, "updateMediaButtonSessionIfNeeded, checking uid=" + intValue + ", mediaButtonSession=" + findMediaButtonSession + ", ignoreButtonSession=" + z);
                }
                if (!z) {
                    this.mAudioPlayerStateMonitor.cleanUpAudioPlaybackUids(findMediaButtonSession.getUid());
                    if (findMediaButtonSession != this.mMediaButtonSession) {
                        updateMediaButtonSession(findMediaButtonSession);
                        return;
                    }
                    return;
                }
            }
        }
    }

    public void updateMediaButtonSessionBySessionPolicyChange(MediaSessionRecord mediaSessionRecord) {
        if ((mediaSessionRecord.getSessionPolicies() & 2) != 0) {
            if (mediaSessionRecord == this.mMediaButtonSession) {
                updateMediaButtonSession(null);
                return;
            }
            return;
        }
        updateMediaButtonSessionIfNeeded();
    }

    private MediaSessionRecordImpl findMediaButtonSession(int i) {
        MediaSessionRecordImpl mediaSessionRecordImpl = null;
        for (MediaSessionRecordImpl mediaSessionRecordImpl2 : this.mSessions) {
            if (!(mediaSessionRecordImpl2 instanceof MediaSession2Record) && i == mediaSessionRecordImpl2.getUid()) {
                if (mediaSessionRecordImpl2.checkPlaybackActiveState(this.mAudioPlayerStateMonitor.isPlaybackActive(mediaSessionRecordImpl2.getUid()))) {
                    return mediaSessionRecordImpl2;
                }
                if (mediaSessionRecordImpl == null) {
                    mediaSessionRecordImpl = mediaSessionRecordImpl2;
                }
            }
        }
        return mediaSessionRecordImpl;
    }

    public List<MediaSessionRecord> getActiveSessions(int i) {
        List<MediaSessionRecord> list = this.mCachedActiveLists.get(i);
        if (list != null) {
            return list;
        }
        List<MediaSessionRecord> priorityList = getPriorityList(true, i);
        this.mCachedActiveLists.put(i, priorityList);
        return priorityList;
    }

    public List<Session2Token> getSession2Tokens(int i) {
        ArrayList arrayList = new ArrayList();
        for (MediaSessionRecordImpl mediaSessionRecordImpl : this.mSessions) {
            if (i == -1 || mediaSessionRecordImpl.getUserId() == i) {
                if (mediaSessionRecordImpl.isActive() && (mediaSessionRecordImpl instanceof MediaSession2Record)) {
                    arrayList.add(((MediaSession2Record) mediaSessionRecordImpl).getSession2Token());
                }
            }
        }
        return arrayList;
    }

    public MediaSessionRecordImpl getMediaButtonSession() {
        if (this.mMediaButtonSession != null) {
            Log.d(TAG, "getMediaButtonSession() mMediaButtonSession=" + this.mMediaButtonSession);
            return this.mMediaButtonSession;
        }
        updateMediaButtonSessionIfNeeded();
        int size = this.mSessions.size();
        MediaSessionRecordImpl mediaSessionRecordImpl = this.mMediaButtonSession;
        if (mediaSessionRecordImpl != null || size <= 0) {
            return mediaSessionRecordImpl;
        }
        Log.d(TAG, "getMediaButtonSession, mSessions.get(0)");
        return this.mSessions.get(0);
    }

    public void updateMediaButtonSession(MediaSessionRecordImpl mediaSessionRecordImpl) {
        MediaSessionRecordImpl mediaSessionRecordImpl2 = this.mMediaButtonSession;
        this.mMediaButtonSession = mediaSessionRecordImpl;
        this.mOnMediaButtonSessionChangedListener.onMediaButtonSessionChanged(mediaSessionRecordImpl2, mediaSessionRecordImpl);
    }

    public MediaSessionRecordImpl getDefaultVolumeSession() {
        List<MediaSessionRecord> priorityList = getPriorityList(true, -1);
        int size = priorityList.size();
        for (int i = 0; i < size; i++) {
            MediaSessionRecord mediaSessionRecord = priorityList.get(i);
            if (mediaSessionRecord.checkPlaybackActiveState(true) && mediaSessionRecord.canHandleVolumeKey()) {
                return mediaSessionRecord;
            }
        }
        return null;
    }

    public MediaSessionRecordImpl getDefaultRemoteSession(int i) {
        List<MediaSessionRecord> priorityList = getPriorityList(true, i);
        int size = priorityList.size();
        for (int i2 = 0; i2 < size; i2++) {
            MediaSessionRecord mediaSessionRecord = priorityList.get(i2);
            if (!mediaSessionRecord.isPlaybackTypeLocal()) {
                return mediaSessionRecord;
            }
        }
        return null;
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "Media button session is " + this.mMediaButtonSession);
        printWriter.println(str + "Sessions Stack - have " + this.mSessions.size() + " sessions:");
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("  ");
        String sb2 = sb.toString();
        Iterator<MediaSessionRecordImpl> it = this.mSessions.iterator();
        while (it.hasNext()) {
            it.next().dump(printWriter, sb2);
        }
    }

    public List<MediaSessionRecord> getPriorityList(boolean z, int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        int i3 = 0;
        for (MediaSessionRecordImpl mediaSessionRecordImpl : this.mSessions) {
            if (mediaSessionRecordImpl instanceof MediaSessionRecord) {
                MediaSessionRecord mediaSessionRecord = (MediaSessionRecord) mediaSessionRecordImpl;
                if (i == -1 || i == mediaSessionRecord.getUserId()) {
                    if (mediaSessionRecord.isActive()) {
                        if (mediaSessionRecord.checkPlaybackActiveState(true)) {
                            arrayList.add(i3, mediaSessionRecord);
                            i2++;
                            i3++;
                        } else {
                            arrayList.add(i2, mediaSessionRecord);
                            i2++;
                        }
                    } else if (!z) {
                        arrayList.add(mediaSessionRecord);
                    }
                }
            }
        }
        return arrayList;
    }

    private void clearCache(int i) {
        this.mCachedActiveLists.remove(i);
        this.mCachedActiveLists.remove(-1);
    }

    private static String getCallers(int i) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(getCaller(stackTrace, i2));
            sb.append(" ");
        }
        return sb.toString();
    }

    private static String getCaller(StackTraceElement[] stackTraceElementArr, int i) {
        int i2 = i + 4;
        if (i2 >= stackTraceElementArr.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement stackTraceElement = stackTraceElementArr[i2];
        return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
    }
}
