package com.android.server.audio;

import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.media.MediaMetrics;
import android.net.INetd;
import com.android.server.audio.AudioDeviceInventory;
import com.android.server.utils.EventLogger;
import java.util.Locale;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AudioServiceEvents {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PhoneStateEvent extends EventLogger.Event {
        static final int MODE_IN_COMMUNICATION_TIMEOUT = 1;
        static final int MODE_SET = 0;
        private static final String mMetricsId = "audio.mode";
        final int mActualMode;
        final int mOp;
        final int mOwnerPid;
        final String mPackage;
        final int mRequestedMode;
        final int mRequesterPid;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PhoneStateEvent(String str, int i, int i2, int i3, int i4) {
            this.mOp = 0;
            this.mPackage = str;
            this.mRequesterPid = i;
            this.mRequestedMode = i2;
            this.mOwnerPid = i3;
            this.mActualMode = i4;
            logMetricEvent();
        }

        PhoneStateEvent(String str, int i) {
            this.mOp = 1;
            this.mPackage = str;
            this.mOwnerPid = i;
            this.mRequesterPid = 0;
            this.mRequestedMode = 0;
            this.mActualMode = 0;
            logMetricEvent();
        }

        public String eventToString() {
            int i = this.mOp;
            if (i != 0) {
                if (i == 1) {
                    return "mode IN COMMUNICATION timeout for package=" + this.mPackage + " pid=" + this.mOwnerPid;
                }
                return "FIXME invalid op:" + this.mOp;
            }
            return "setMode(" + AudioSystem.modeToString(this.mRequestedMode) + ") from package=" + this.mPackage + " pid=" + this.mRequesterPid + " selected mode=" + AudioSystem.modeToString(this.mActualMode) + " by pid=" + this.mOwnerPid;
        }

        private void logMetricEvent() {
            int i = this.mOp;
            if (i == 0) {
                new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.EVENT, "set").set(MediaMetrics.Property.REQUESTED_MODE, AudioSystem.modeToString(this.mRequestedMode)).set(MediaMetrics.Property.MODE, AudioSystem.modeToString(this.mActualMode)).set(MediaMetrics.Property.CALLING_PACKAGE, this.mPackage).record();
            } else {
                if (i != 1) {
                    return;
                }
                new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.EVENT, "inCommunicationTimeout").set(MediaMetrics.Property.CALLING_PACKAGE, this.mPackage).record();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class WiredDevConnectEvent extends EventLogger.Event {
        final AudioDeviceInventory.WiredDeviceConnectionState mState;

        /* JADX INFO: Access modifiers changed from: package-private */
        public WiredDevConnectEvent(AudioDeviceInventory.WiredDeviceConnectionState wiredDeviceConnectionState) {
            this.mState = wiredDeviceConnectionState;
        }

        public String eventToString() {
            return "setWiredDeviceConnectionState( type:" + Integer.toHexString(this.mState.mAttributes.getInternalType()) + " state:" + AudioSystem.deviceStateToString(this.mState.mState) + " addr:" + this.mState.mAttributes.getAddress() + " name:" + this.mState.mAttributes.getName() + ") from " + this.mState.mCaller;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ForceUseEvent extends EventLogger.Event {
        final int mConfig;
        final String mReason;
        final int mUsage;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ForceUseEvent(int i, int i2, String str) {
            this.mUsage = i;
            this.mConfig = i2;
            this.mReason = str;
        }

        public String eventToString() {
            return "setForceUse(" + AudioSystem.forceUseUsageToString(this.mUsage) + ", " + AudioSystem.forceUseConfigToString(this.mConfig) + ") due to " + this.mReason;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class VolChangedBroadcastEvent extends EventLogger.Event {
        final int mAliasStreamType;
        final int mIndex;
        final int mStreamType;

        /* JADX INFO: Access modifiers changed from: package-private */
        public VolChangedBroadcastEvent(int i, int i2, int i3) {
            this.mStreamType = i;
            this.mAliasStreamType = i2;
            this.mIndex = i3;
        }

        public String eventToString() {
            return "sending VOLUME_CHANGED stream:" + AudioSystem.streamToString(this.mStreamType) + " index:" + this.mIndex + " alias:" + AudioSystem.streamToString(this.mAliasStreamType);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static final class DeviceVolumeEvent extends EventLogger.Event {
        final String mCaller;
        final String mDeviceAddress;
        final int mDeviceForStream;
        final String mDeviceNativeType;
        final boolean mSkipped;
        final int mStream;
        final int mVolIndex;

        /* JADX INFO: Access modifiers changed from: package-private */
        public DeviceVolumeEvent(int i, int i2, AudioDeviceAttributes audioDeviceAttributes, int i3, String str, boolean z) {
            this.mStream = i;
            this.mVolIndex = i2;
            String str2 = "0x" + Integer.toHexString(audioDeviceAttributes.getInternalType());
            this.mDeviceNativeType = str2;
            String address = audioDeviceAttributes.getAddress();
            this.mDeviceAddress = address;
            this.mDeviceForStream = i3;
            this.mCaller = str;
            this.mSkipped = z;
            new MediaMetrics.Item("audio.volume.event").set(MediaMetrics.Property.EVENT, "setDeviceVolume").set(MediaMetrics.Property.STREAM_TYPE, AudioSystem.streamToString(i)).set(MediaMetrics.Property.INDEX, Integer.valueOf(i2)).set(MediaMetrics.Property.DEVICE, str2).set(MediaMetrics.Property.ADDRESS, address).set(MediaMetrics.Property.CALLING_PACKAGE, str).record();
        }

        public String eventToString() {
            StringBuilder sb = new StringBuilder("setDeviceVolume(stream:");
            sb.append(AudioSystem.streamToString(this.mStream));
            sb.append(" index:");
            sb.append(this.mVolIndex);
            sb.append(" device:");
            sb.append(this.mDeviceNativeType);
            sb.append(" addr:");
            sb.append(this.mDeviceAddress);
            sb.append(") from ");
            sb.append(this.mCaller);
            if (this.mSkipped) {
                sb.append(" skipped [device in use]");
            } else {
                sb.append(" currDevForStream:Ox");
                sb.append(Integer.toHexString(this.mDeviceForStream));
            }
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class VolumeEvent extends EventLogger.Event {
        static final int VOL_ADJUST_GROUP_VOL = 11;
        static final int VOL_ADJUST_STREAM_VOL = 1;
        static final int VOL_ADJUST_SUGG_VOL = 0;
        static final int VOL_ADJUST_VOL_UID = 5;
        static final int VOL_MODE_CHANGE_HEARING_AID = 7;
        static final int VOL_MUTE_STREAM_INT = 9;
        static final int VOL_SET_AVRCP_VOL = 4;
        static final int VOL_SET_GROUP_VOL = 8;
        static final int VOL_SET_HEARING_AID_VOL = 3;
        static final int VOL_SET_LE_AUDIO_VOL = 10;
        static final int VOL_SET_STREAM_VOL = 2;
        static final int VOL_VOICE_ACTIVITY_HEARING_AID = 6;
        private static final String mMetricsId = "audio.volume.event";
        final String mCaller;
        final String mGroupName;
        final int mOp;
        final int mStream;
        final int mVal1;
        final int mVal2;

        /* JADX INFO: Access modifiers changed from: package-private */
        public VolumeEvent(int i, int i2, int i3, int i4, String str) {
            this.mOp = i;
            this.mStream = i2;
            this.mVal1 = i3;
            this.mVal2 = i4;
            this.mCaller = str;
            this.mGroupName = null;
            logMetricEvent();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public VolumeEvent(int i, int i2, int i3) {
            this.mOp = i;
            this.mVal1 = i2;
            this.mVal2 = i3;
            this.mStream = -1;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public VolumeEvent(int i, int i2) {
            this.mOp = i;
            this.mVal1 = i2;
            this.mVal2 = 0;
            this.mStream = -1;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public VolumeEvent(int i, boolean z, int i2, int i3) {
            this.mOp = i;
            this.mStream = i2;
            this.mVal1 = i3;
            this.mVal2 = z ? 1 : 0;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public VolumeEvent(int i, int i2, int i3, int i4) {
            this.mOp = i;
            this.mStream = i3;
            this.mVal1 = i4;
            this.mVal2 = i2;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public VolumeEvent(int i, String str, int i2, int i3, String str2) {
            this.mOp = i;
            this.mStream = -1;
            this.mVal1 = i2;
            this.mVal2 = i3;
            this.mCaller = str2;
            this.mGroupName = str;
            logMetricEvent();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public VolumeEvent(int i, int i2, boolean z) {
            this.mOp = i;
            this.mStream = i2;
            this.mVal1 = z ? 1 : 0;
            this.mVal2 = 0;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        private void logMetricEvent() {
            String str;
            int i = this.mOp;
            String str2 = INetd.IF_STATE_UP;
            switch (i) {
                case 0:
                case 1:
                case 5:
                    if (i == 0) {
                        str = "adjustSuggestedStreamVolume";
                    } else if (i == 1) {
                        str = "adjustStreamVolume";
                    } else if (i != 5) {
                        return;
                    } else {
                        str = "adjustStreamVolumeForUid";
                    }
                    MediaMetrics.Item item = new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.CALLING_PACKAGE, this.mCaller);
                    MediaMetrics.Key key = MediaMetrics.Property.DIRECTION;
                    if (this.mVal1 <= 0) {
                        str2 = INetd.IF_STATE_DOWN;
                    }
                    item.set(key, str2).set(MediaMetrics.Property.EVENT, str).set(MediaMetrics.Property.FLAGS, Integer.valueOf(this.mVal2)).set(MediaMetrics.Property.STREAM_TYPE, AudioSystem.streamToString(this.mStream)).record();
                    return;
                case 2:
                    new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.CALLING_PACKAGE, this.mCaller).set(MediaMetrics.Property.EVENT, "setStreamVolume").set(MediaMetrics.Property.FLAGS, Integer.valueOf(this.mVal2)).set(MediaMetrics.Property.INDEX, Integer.valueOf(this.mVal1)).set(MediaMetrics.Property.STREAM_TYPE, AudioSystem.streamToString(this.mStream)).record();
                    return;
                case 3:
                    new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.EVENT, "setHearingAidVolume").set(MediaMetrics.Property.GAIN_DB, Double.valueOf(this.mVal2)).set(MediaMetrics.Property.INDEX, Integer.valueOf(this.mVal1)).record();
                    return;
                case 4:
                    new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.EVENT, "setAvrcpVolume").set(MediaMetrics.Property.INDEX, Integer.valueOf(this.mVal1)).record();
                    return;
                case 6:
                    new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.EVENT, "voiceActivityHearingAid").set(MediaMetrics.Property.INDEX, Integer.valueOf(this.mVal1)).set(MediaMetrics.Property.STATE, this.mVal2 == 1 ? "active" : "inactive").set(MediaMetrics.Property.STREAM_TYPE, AudioSystem.streamToString(this.mStream)).record();
                    return;
                case 7:
                    new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.EVENT, "modeChangeHearingAid").set(MediaMetrics.Property.INDEX, Integer.valueOf(this.mVal1)).set(MediaMetrics.Property.MODE, AudioSystem.modeToString(this.mVal2)).set(MediaMetrics.Property.STREAM_TYPE, AudioSystem.streamToString(this.mStream)).record();
                    return;
                case 8:
                    new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.CALLING_PACKAGE, this.mCaller).set(MediaMetrics.Property.EVENT, "setVolumeIndexForAttributes").set(MediaMetrics.Property.FLAGS, Integer.valueOf(this.mVal2)).set(MediaMetrics.Property.GROUP, this.mGroupName).set(MediaMetrics.Property.INDEX, Integer.valueOf(this.mVal1)).record();
                    return;
                case 9:
                default:
                    return;
                case 10:
                    new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.EVENT, "setLeAudioVolume").set(MediaMetrics.Property.INDEX, Integer.valueOf(this.mVal1)).set(MediaMetrics.Property.MAX_INDEX, Integer.valueOf(this.mVal2)).record();
                    return;
                case 11:
                    MediaMetrics.Item item2 = new MediaMetrics.Item(mMetricsId).set(MediaMetrics.Property.CALLING_PACKAGE, this.mCaller);
                    MediaMetrics.Key key2 = MediaMetrics.Property.DIRECTION;
                    if (this.mVal1 <= 0) {
                        str2 = INetd.IF_STATE_DOWN;
                    }
                    item2.set(key2, str2).set(MediaMetrics.Property.EVENT, "adjustVolumeGroupVolume").set(MediaMetrics.Property.FLAGS, Integer.valueOf(this.mVal2)).set(MediaMetrics.Property.GROUP, this.mGroupName).record();
                    return;
            }
        }

        public String eventToString() {
            switch (this.mOp) {
                case 0:
                    return "adjustSuggestedStreamVolume(sugg:" + AudioSystem.streamToString(this.mStream) + " dir:" + AudioManager.adjustToString(this.mVal1) + " flags:0x" + Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 1:
                    return "adjustStreamVolume(stream:" + AudioSystem.streamToString(this.mStream) + " dir:" + AudioManager.adjustToString(this.mVal1) + " flags:0x" + Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 2:
                    return "setStreamVolume(stream:" + AudioSystem.streamToString(this.mStream) + " index:" + this.mVal1 + " flags:0x" + Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 3:
                    return "setHearingAidVolume: index:" + this.mVal1 + " gain dB:" + this.mVal2;
                case 4:
                    return "setAvrcpVolume: index:" + this.mVal1;
                case 5:
                    return "adjustStreamVolumeForUid(stream:" + AudioSystem.streamToString(this.mStream) + " dir:" + AudioManager.adjustToString(this.mVal1) + " flags:0x" + Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 6:
                    StringBuilder sb = new StringBuilder("Voice activity change (");
                    sb.append(this.mVal2 == 1 ? "active" : "inactive");
                    sb.append(") causes setting HEARING_AID volume to idx:");
                    sb.append(this.mVal1);
                    sb.append(" stream:");
                    sb.append(AudioSystem.streamToString(this.mStream));
                    return sb.toString();
                case 7:
                    return "setMode(" + AudioSystem.modeToString(this.mVal2) + ") causes setting HEARING_AID volume to idx:" + this.mVal1 + " stream:" + AudioSystem.streamToString(this.mStream);
                case 8:
                    return "setVolumeIndexForAttributes(group: group: " + this.mGroupName + " index:" + this.mVal1 + " flags:0x" + Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 9:
                    StringBuilder sb2 = new StringBuilder("VolumeStreamState.muteInternally(stream:");
                    sb2.append(AudioSystem.streamToString(this.mStream));
                    sb2.append(this.mVal1 == 1 ? ", muted)" : ", unmuted)");
                    return sb2.toString();
                case 10:
                    return "setLeAudioVolume: index:" + this.mVal1 + " maxIndex:" + this.mVal2;
                case 11:
                    return "adjustVolumeGroupVolume(group:" + this.mGroupName + " dir:" + AudioManager.adjustToString(this.mVal1) + " flags:0x" + Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                default:
                    return "FIXME invalid op:" + this.mOp;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static final class SoundDoseEvent extends EventLogger.Event {
        static final int DOSE_ACCUMULATION_START = 3;
        static final int DOSE_REPEAT_5X = 2;
        static final int DOSE_UPDATE = 1;
        static final int MOMENTARY_EXPOSURE = 0;
        final int mEventType;
        final float mFloatValue;
        final long mLongValue;

        private SoundDoseEvent(int i, float f, long j) {
            this.mEventType = i;
            this.mFloatValue = f;
            this.mLongValue = j;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static SoundDoseEvent getMomentaryExposureEvent(float f) {
            return new SoundDoseEvent(0, f, 0L);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static SoundDoseEvent getDoseUpdateEvent(float f, long j) {
            return new SoundDoseEvent(1, f, j);
        }

        static SoundDoseEvent getDoseRepeat5xEvent() {
            return new SoundDoseEvent(2, 0.0f, 0L);
        }

        static SoundDoseEvent getDoseAccumulationStartEvent() {
            return new SoundDoseEvent(3, 0.0f, 0L);
        }

        public String eventToString() {
            int i = this.mEventType;
            if (i == 0) {
                return String.format("momentary exposure MEL=%.2f", Float.valueOf(this.mFloatValue));
            }
            if (i == 1) {
                return String.format(Locale.US, "dose update CSD=%.1f%% total duration=%d", Float.valueOf(this.mFloatValue * 100.0f), Long.valueOf(this.mLongValue));
            }
            if (i == 2) {
                return "CSD reached 500%";
            }
            if (i == 3) {
                return "CSD accumulating: RS2 entered";
            }
            return "FIXME invalid event type:" + this.mEventType;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class StreamMuteEvent extends EventLogger.Event {
        final boolean mMuted;
        final String mSource;
        final int mStreamType;

        /* JADX INFO: Access modifiers changed from: package-private */
        public StreamMuteEvent(int i, boolean z, String str) {
            this.mStreamType = i;
            this.mMuted = z;
            this.mSource = str;
        }

        public String eventToString() {
            String str;
            int i;
            if (this.mStreamType <= AudioSystem.getNumStreamTypes() && (i = this.mStreamType) >= 0) {
                str = AudioSystem.STREAM_NAMES[i];
            } else {
                str = "stream " + this.mStreamType;
            }
            StringBuilder sb = new StringBuilder(str);
            sb.append(this.mMuted ? " muting by " : " unmuting by ");
            sb.append(this.mSource);
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class StreamUnmuteErrorEvent extends EventLogger.Event {
        final int mRingerZenMutedStreams;
        final int mStreamType;

        /* JADX INFO: Access modifiers changed from: package-private */
        public StreamUnmuteErrorEvent(int i, int i2) {
            this.mStreamType = i;
            this.mRingerZenMutedStreams = i2;
        }

        public String eventToString() {
            String str;
            int i;
            if (this.mStreamType <= AudioSystem.getNumStreamTypes() && (i = this.mStreamType) >= 0) {
                str = AudioSystem.STREAM_NAMES[i];
            } else {
                str = "stream " + this.mStreamType;
            }
            return "Invalid call to unmute " + str + " despite muted streams 0x" + Integer.toHexString(this.mRingerZenMutedStreams);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class RingerZenMutedStreamsEvent extends EventLogger.Event {
        final int mRingerZenMutedStreams;
        final String mSource;

        /* JADX INFO: Access modifiers changed from: package-private */
        public RingerZenMutedStreamsEvent(int i, String str) {
            this.mRingerZenMutedStreams = i;
            this.mSource = str;
        }

        public String eventToString() {
            return "RingerZenMutedStreams 0x" + Integer.toHexString(this.mRingerZenMutedStreams) + " from " + this.mSource;
        }
    }
}
