package com.qualcomm.listen;

import android.util.Log;
import com.qualcomm.listen.ListenTypes;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes.dex */
public class ListenSoundModel {
    private static final int SVA_KEYWORD_TYPE_PDK = 2;
    private static final int SVA_KEYWORD_TYPE_UDK = 3;
    private static final int SVA_SOUNDMODEL_TYPE = 1;
    private static final String TAG = "ListenSoundModel";
    private static final int UNKNOWN_SOUNDMODEL_TYPE = 0;
    private static final int VERSION_0100 = 256;
    private static final int VERSION_0200 = 512;
    private static final int VERSION_UNKNOWN = 0;

    public static native int SetSoundModelTuningParams(int i, ByteBuffer byteBuffer);

    public static native int createUdkSm(String str, String str2, int i, ShortBuffer[] shortBufferArr, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ListenTypes.ConfidenceData confidenceData);

    public static native int deleteData(ByteBuffer byteBuffer, String str, String str2, ByteBuffer byteBuffer2);

    public static native int extend(ByteBuffer byteBuffer, String str, String str2, int i, ShortBuffer[] shortBufferArr, ByteBuffer byteBuffer2, ListenTypes.ConfidenceData confidenceData);

    private static native int getInfo(ByteBuffer byteBuffer, ListenTypes.SVASoundModelInfo sVASoundModelInfo);

    public static native int getSizeAfterDelete(ByteBuffer byteBuffer, String str, String str2);

    public static native int getSizeWhenExtended(ByteBuffer byteBuffer, String str, String str2);

    public static native int getSizeWhenMerged(ByteBuffer[] byteBufferArr);

    public static native int getTypeVersion(ByteBuffer byteBuffer, ListenTypes.SoundModelInfo soundModelInfo);

    public static native int getUdkSmSize(String str, String str2, ShortBuffer[] shortBufferArr, ByteBuffer byteBuffer);

    public static native int merge(ByteBuffer[] byteBufferArr, ByteBuffer byteBuffer);

    private static native int parseVWUDetectionEventData(ByteBuffer byteBuffer, ListenTypes.EventData eventData, ListenTypes.VoiceWakeupDetectionData voiceWakeupDetectionData);

    private static native int parseVWUDetectionEventDataV2(ByteBuffer byteBuffer, ListenTypes.EventData eventData, ListenTypes.VoiceWakeupDetectionDataV2 voiceWakeupDetectionDataV2);

    public static native int verifyUdkRecording(ByteBuffer byteBuffer, ShortBuffer shortBuffer);

    public static native int verifyUdkRecording(ByteBuffer byteBuffer, ShortBuffer[] shortBufferArr);

    public static native int verifyUserRecording(ByteBuffer byteBuffer, String str, ShortBuffer shortBuffer);

    public static native int verifyUserRecordingQuality(ByteBuffer byteBuffer, String str, ShortBuffer shortBuffer, boolean z, ListenTypes.QualityCheckInfo qualityCheckInfo);

    static {
        Log.d(TAG, "Load liblistenjni.qti");
        System.loadLibrary("listenjni.qti");
    }

    @Deprecated
    public static int verifyUserRecording(ByteBuffer userIndependentModel, ShortBuffer userRecording) {
        int status = verifyUserRecording(userIndependentModel, null, userRecording);
        return status;
    }

    @Deprecated
    public static int getSizeWhenExtended(ByteBuffer userIndependentModel) {
        int status = getSizeWhenExtended(userIndependentModel, null, null);
        return status;
    }

    @Deprecated
    public static int extend(ByteBuffer userIndependentModel, int numUserRecordings, ShortBuffer[] userRecordings, ByteBuffer extendedSoundModel, ListenTypes.ConfidenceData confidenceData) {
        int status = extend(userIndependentModel, null, null, numUserRecordings, userRecordings, extendedSoundModel, confidenceData);
        return status;
    }

    public static ListenTypes.DetectionData parseDetectionEventData(ByteBuffer registeredSoundModel, ListenTypes.EventData eventPayload) {
        ListenTypes.DetectionData detData;
        ListenTypes.SoundModelInfo soundModelInfo = new ListenTypes.SoundModelInfo();
        int status = getTypeVersion(registeredSoundModel, soundModelInfo);
        if (status != 0) {
            Log.e(TAG, "parseDetectionEventData() get SM Info failed w/ " + status);
            return null;
        }
        if (soundModelInfo.type != 1) {
            Log.e(TAG, "parseDetectionEventData() SM type " + soundModelInfo.type + " unsupported!");
            return null;
        }
        if (soundModelInfo.version == 256) {
            Log.d(TAG, "SM type is SVA 1.0");
            ListenTypes.VoiceWakeupDetectionData vwuDetData = new ListenTypes.VoiceWakeupDetectionData();
            vwuDetData.status = parseVWUDetectionEventData(registeredSoundModel, eventPayload, vwuDetData);
            vwuDetData.type = ListenTypes.VWU_EVENT_0100;
            detData = vwuDetData;
        } else {
            Log.d(TAG, "SM type is SVA 2.0");
            ListenTypes.VoiceWakeupDetectionDataV2 vwuDetDataV2 = new ListenTypes.VoiceWakeupDetectionDataV2();
            vwuDetDataV2.status = parseVWUDetectionEventDataV2(registeredSoundModel, eventPayload, vwuDetDataV2);
            vwuDetDataV2.type = ListenTypes.VWU_EVENT_0200;
            detData = vwuDetDataV2;
        }
        if (detData.status != 0) {
            Log.e(TAG, "parseDetectionEventData() returns status " + detData.status);
            return null;
        }
        return detData;
    }

    public static ListenTypes.SoundModelInfo query(ByteBuffer soundModel) {
        ListenTypes.SoundModelInfo genSMInfo = new ListenTypes.SoundModelInfo();
        ListenTypes.SVASoundModelInfo soundModelInfo = new ListenTypes.SVASoundModelInfo();
        Log.d(TAG, "query() getTypeVersion");
        int status = getTypeVersion(soundModel, genSMInfo);
        if (status != 0) {
            Log.e(TAG, "query() getTypeVersion failed, returned " + status);
            return null;
        }
        soundModelInfo.type = genSMInfo.type;
        soundModelInfo.version = genSMInfo.version;
        soundModelInfo.size = genSMInfo.size;
        if (soundModelInfo.type != 1) {
            Log.e(TAG, "query() SM type " + genSMInfo.type + " unsupported!");
            return null;
        }
        if (genSMInfo.version == 256) {
            Log.d(TAG, "query() only returns type and version for SVA 1.0 SoundModel");
            ListenTypes.KeywordUserCounts counts = new ListenTypes.KeywordUserCounts();
            soundModelInfo.counts = counts;
            soundModelInfo.counts.numKeywords = (short) 1;
            soundModelInfo.counts.numUsers = (short) 0;
            soundModelInfo.counts.numUserKWPairs = (short) 1;
            soundModelInfo.keywordInfo = null;
            soundModelInfo.userNames = null;
        } else {
            Log.d(TAG, "query() getInfoV2 called");
            int status2 = getInfo(soundModel, soundModelInfo);
            if (status2 != 0) {
                Log.e(TAG, "query() getInfoV2 failed, returned " + status2);
                return null;
            }
        }
        return soundModelInfo;
    }
}
